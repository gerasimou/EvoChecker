//  MultithreadedEvaluator.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//
//  Copyright (c) 2013 Antonio J. Nebro
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package evochecker.genetic.jmetal;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import evochecker.auxiliary.Utility;
import evochecker.genetic.jmetal.metaheuristics.IParallelEvaluator;
import jmetal.core.Problem;
import jmetal.core.Solution;

public class MultiProcessPrismEvaluator implements IParallelEvaluator {
	private int numberOfProcesses;
	private Problem problem;

	private Socket[] socket = null;
	private PrintWriter[] out;
	private BufferedReader[] in;

	private List<Solution> taskList;
	private Thread[] threads;
	private RunnableExecutor[] runnables;
	private CopyOnWriteArrayList<Solution> results;
	
	static int portId = 8880;
	

	private class RunnableExecutor implements Runnable {
		private List<Solution> tasks = new ArrayList<Solution>();
		private PrintWriter out;
		private BufferedReader in;

		public RunnableExecutor(PrintWriter out, BufferedReader in) {
			this.in = in;
			this.out = out;
			this.tasks = new ArrayList<Solution>();
		}

		public void addTask(Solution task) {
			this.tasks.add(task);
		}

		@Override
		public void run() {
//			 System.out.println("Running thread....");
			for (Solution task : this.tasks) {
				try {
					GeneticProblem geneticProblem = (GeneticProblem) problem;
					geneticProblem.parallelEvaluate(task, out, in);
				} catch (Exception e) {
					e.printStackTrace();
				}
//				 System.out.println("Adding result");
				results.add(task);
			}
		}
	}

	public MultiProcessPrismEvaluator(int processes) {
		if (processes <= 0) {
			String processesNum = Utility.getProperty("PROCESSORS");
			if (processesNum!=null)
				numberOfProcesses = Integer.parseInt(processesNum);
			else if (processesNum == null || processesNum.equals("-1"))
				numberOfProcesses = Runtime.getRuntime().availableProcessors();
		} else {
			numberOfProcesses = processes;
		}
		this.startPrismExecutors();
		this.reset();
	}

	private void startPrismExecutors() {
		try {
			 System.out.println("Initialization");
			socket = new Socket[numberOfProcesses];
			in = new BufferedReader[numberOfProcesses];
			out = new PrintWriter[numberOfProcesses];

//			Properties properties = new Properties();
//			properties.load(new FileInputStream("res/config.properties"));
			
			int initPort = Integer.parseInt(Utility.getProperty("INIT_PORT_NUM"));
			
	      	Thread.sleep(1000);
			
			String params[] = new String[4];
//			params[0] = "/System/Library/Frameworks/JavaVM.framework/Versions/Current/Commands/java";
//			params[0] = "/usr/lib/jvm/java-7-oracle/jre/bin/java";
//			params[0] = "/opt/yarcc/infrastructure/java/1.8.0_05/1/default/bin/java";
//			params[0] = "/home/margara/jdk1.7.0_60/bin/java";
//			params[0] = "/tools/jdk1.7.0_05/bin/java";
			params[0] = Utility.getProperty("JVM");
			params[1] = "-jar";
			params[2] = "res/executor.jar";
			for (int i = 0; i < numberOfProcesses; i++) {
				
				boolean isAlive = false;
				int portNum = initPort+i;// portId++;
				System.out.println("Starting PRISM server " + portNum);
				params[3] = String.valueOf(portNum);
				do {
					Thread.sleep(3000);
					Process p = Runtime.getRuntime().exec(params);
					isAlive = p.isAlive();
				} 
				while (!isAlive);
				
				
//				BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				
//				Thread t = new Thread(new Show(p, error));
//				t.start();
				
//				BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
				
//				Thread t2 = new Thread(new Show(p, input));
//				t2.start();

				System.out.println("Connecting");
//				socket[i] = new Socket("127.0.0.1", portNum);
//				in[i] = new BufferedReader(new InputStreamReader(socket[i].getInputStream()));
//				out[i] = new PrintWriter(socket[i].getOutputStream());
				 makeConnection(portNum, i, params);
			}

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void makeConnection(int portNum, int i, String[] params){
		try{
			socket[i] = new Socket("127.0.0.1", portNum);
			in[i] = new BufferedReader(new InputStreamReader(socket[i].getInputStream()));
			out[i] = new PrintWriter(socket[i].getOutputStream());		
		} catch (IOException e) {
			try {
				Thread.sleep(1000);
				Process p = Runtime.getRuntime().exec(params);
			} catch (InterruptedException | IOException e1) {
				e1.printStackTrace();
			}			
			makeConnection(portNum, i, params);
		}

	}

	public void startEvaluator(Problem problem) {
		 System.out.println("Cores: " + numberOfProcesses);
		this.problem = problem;
	}

	public void addSolutionForEvaluation(Solution solution) {
//		 System.out.println("Adding a solution to be evaluated");
		taskList.add(solution);
	}

	private void startThreads() {
		for (Thread t : this.threads) {
			t.start();
		}

//		System.out.println("Thread started....");

		for (Thread t : this.threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void assignTasks() {
		for (int i = 0; i < this.taskList.size(); i++) {
//			System.out.println("Assigning tasks");
			this.runnables[i % this.runnables.length].addTask(this.taskList
					.get(i));
		}
	}
	
	private void reset() {
		threads = new Thread[numberOfProcesses];
		runnables = new RunnableExecutor[numberOfProcesses];
		taskList = new ArrayList<Solution>();

		for (int i = 0; i < numberOfProcesses; i++) {
			runnables[i] = new RunnableExecutor(out[i], in[i]);
			threads[i] = new Thread(runnables[i]);
		}
	}

	public List<Solution> parallelEvaluation() {
//		System.out.println("Parallel evaluation");
		results = new CopyOnWriteArrayList<Solution>();
		this.assignTasks();
		this.startThreads();
//		System.out.println("End of parallel evaluation....");
		this.reset();
		return this.results;
	}

	public void stopEvaluator() {
		for (int i = 0; i < this.numberOfProcesses; i++) {
			try {
				this.in[i].close();
				this.out[i].close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

//	private class Show implements Runnable {
//		private BufferedReader buff;
//		private Process p;
//
//		public Show(Process p, BufferedReader buff) {
//			this.buff = buff;
//			this.p = p;
//		}
//
//		@Override
//		public void run() {
//			String line;
//			try {
//				while ((line = buff.readLine()) != null) {
//				    System.out.println(line);
//				}
//				p.waitFor();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		}
//	}
}
