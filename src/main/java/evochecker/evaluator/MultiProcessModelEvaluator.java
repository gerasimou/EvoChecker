//==============================================================================
//	
//	Copyright (c) 2020-
//	Authors:
//	* Simos Gerasimou (University of York)
//  * Faisal Alhwikem (University of York)
//	
//------------------------------------------------------------------------------
//	
//	This file is part of EvoChecker.
//	
//==============================================================================
package evochecker.evaluator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import evochecker.auxiliary.Constants;
import evochecker.auxiliary.Utility;
import evochecker.genetic.problem.GeneticModelProblem;
import evochecker.genetic.problem.GeneticProblem;
import jmetal.core.Problem;
import jmetal.core.Solution;

/**
 * Class representing a parallel evaluator
 * @author sgerasimou
 *
 */
public class MultiProcessModelEvaluator implements IParallelEvaluator {
	/** number of parallel executions (processes)*/
	private int numberOfProcesses;

	private Problem[] problems;
	
	/** List of solutions*/
	private List<Solution> solutionsList;

	/** Solution results list*/
	private CopyOnWriteArrayList<Solution> evaluatedSolutions;

	/** Array of threads*/
	private Thread[] threads;

	/** Array of runnables*/
	private RunnableExecutor[] runnables;
	
	/** Starting port*/
	static int portId = 8880;
	
	/** Set of connections array keeping the evaluators instances*/
	private Connection connections[];

	
	/**
	 * Constructor
	 * @param processes
	 * @throws Exception 
	 */
	public MultiProcessModelEvaluator(){
		String processesNum = Utility.getProperty(Constants.PROCESSORS_KEYWORD);
		if (processesNum!=null)
			numberOfProcesses = Integer.parseInt(processesNum);
		else if (processesNum == null || processesNum.equals("-1"))
			numberOfProcesses = Runtime.getRuntime().availableProcessors();
			
		//initialise connections and executors
		int initPort = Integer.parseInt(Utility.getProperty(Constants.INITIAL_PORT_KEYWORD));
		connections = new Connection[numberOfProcesses];
		for (int i = 0; i < numberOfProcesses; i++) {
			try {
				connections[i] = new Connection(initPort + i);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		
		//initialise threads, runnables and solutions
		threads 		= new Thread[numberOfProcesses];
		runnables 		= new RunnableExecutor[numberOfProcesses];
		solutionsList 	= new ArrayList<Solution>();
	}

	
	/** 
	 * Initialise evaluator 
	 */
	public void startEvaluator(Problem problem) {
		System.out.println("Cores: " + numberOfProcesses);

		try {		
			problems = new Problem[numberOfProcesses];
			if (problem instanceof GeneticProblem){
				for (int i=0; i<numberOfProcesses; i++){
					problems[i] = new GeneticProblem((GeneticProblem) problem);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/** 
	 * Add the solution to the list of solutions to be evaluated
	 */
	public void addSolutionForEvaluation(Solution solution) {
//		 System.out.println("Adding a solution to be evaluated");
		solutionsList.add(solution);
	}

	
	/**
	 * Run parallel evaluation
	 */
	public List<Solution> parallelEvaluation() {
//		System.out.println("Parallel evaluation");
		evaluatedSolutions = new CopyOnWriteArrayList<Solution>();
		this.reset();
		this.assignSolutions();
		this.startThreads();
		solutionsList.clear();
//		System.out.println("End of parallel evaluation....");
		return this.evaluatedSolutions;
	}
	
	
	/**
	* Assign solutions to parallel processes
	*/
	private void assignSolutions() {
		for (int i = 0; i < this.solutionsList.size(); i++) {
//			System.out.println("Assigning tasks");
			this.runnables[i % this.runnables.length].addSolutionForEvaluation(this.solutionsList
					.get(i));
		}
	}
	

	/**
	 * When done, reset the evaluators
	 */
	private void reset() {	
		for (int i = 0; i < numberOfProcesses; i++) {
			runnables[i] = new RunnableExecutor(connections[i].getOutChannel(), connections[i].getInChannel(), problems[i]);
			threads[i] 	 = new Thread(runnables[i]);
		}
	}


	/**
	 * Start parallel execution
	 */
	private void startThreads() {
		for (Thread t : this.threads) {
			t.start();
		}

		for (Thread t : this.threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	
	/**
	 * Once finished, stop the evaluators
	 */
	public void stopEvaluator() {
		try {
			for (Connection c: connections) {
				c.close();
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Inner class
	 * @author sgerasimou
	 *
	 */
	private class RunnableExecutor implements Runnable {
		/** List of solutions to be evaluated*/
		private List<Solution> solutionsList = new ArrayList<Solution>();

 		/** Output*/
		private PrintWriter out;

		/** Input*/
		private BufferedReader in;

		/** Problem to be handled by this executor*/
		Problem runnableProblem;
		
		
		/**
		 * Class constructor: create a new runnable executor
		 * @param out
		 * @param in
		 */
		public RunnableExecutor(PrintWriter out, BufferedReader in, Problem problem) {
			this.in 			 = in;
			this.out 			 = out;
			this.runnableProblem = problem;
			this.solutionsList 	 = new ArrayList<Solution>();
		}


		/** Add a solution for evaluation*/
		public void addSolutionForEvaluation(Solution solution) {
			this.solutionsList.add(solution);
		}
		

		/**
		 * Run
		 */
		@Override
		public void run() {
			for (Solution solution : this.solutionsList) {
				try {
					if (runnableProblem instanceof GeneticModelProblem){
						((GeneticModelProblem) runnableProblem).parallelEvaluate(in, out, solution);
					}
					else throw new IllegalArgumentException("Problem not recognised");
				} catch (Exception e) {
					e.printStackTrace();
				}
//				 System.out.println("Adding result");
				evaluatedSolutions.add(solution);
			}
		}
	}
	
	
	
	private class Connection {

		/** Socket **/
		private Socket socket;
		
		/** Input channel**/
		private BufferedReader in;
		
		/** Output channel**/
		private PrintWriter out;

		private final String HOSTNAME = "127.0.0.1";

		public Connection(int portNum) throws Exception {
			String params[] = new String[4];
			params[0] = Utility.getProperty(Constants.JVM_KEYWORD);
			params[1] = "-jar";
			params[2] = Utility.getProperty(Constants.MODEL_CHECKING_ENGINE);
			params[3] = String.valueOf(portNum);
			
			
			ProcessBuilder pb = new ProcessBuilder(params);
			Map<String, String> env = pb.environment();
			env.put("DYLD_LIBRARY_PATH", Utility.getProperty(Constants.MODEL_CHECKING_ENGINE_LIBS_DIR)); //OSX
			env.put("LD_LIBRARY_PATH", Utility.getProperty(Constants.MODEL_CHECKING_ENGINE_LIBS_DIR));   //Linux

			boolean alive = false;
			do {
				Process p = pb.start();
				alive = p.isAlive();
				Thread.sleep(1000);
			} while (!alive);

			boolean successful = false;
			while (!successful) {
				try {
					socket	= new Socket(HOSTNAME, portNum);
					in		= new BufferedReader(new InputStreamReader(socket.getInputStream()));
					out		= new PrintWriter(socket.getOutputStream());
					successful = true;
				} catch (IOException | NullPointerException e) {
					Thread.sleep(1000);
					pb.start();
				}
			}
		}		
		
		
		public BufferedReader getInChannel() {
			return in;
		}

		
		public PrintWriter getOutChannel() {
			return out;
		}

		
		public void close() throws IOException {
			out.close();
			in.close();
		}
	}


}
