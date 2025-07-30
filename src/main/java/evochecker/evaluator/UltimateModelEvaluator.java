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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import evochecker.EvoChecker;
import evochecker.auxiliary.Constants;
import evochecker.auxiliary.Utility;
import evochecker.genetic.problem.GeneticModelProblem;
import evochecker.genetic.problem.GeneticProblem;
import jmetal.core.Problem;
import jmetal.core.Solution;
import ultimate.Ultimate;
import evochecker.genetic.problem.GeneticProblemUltimate;

/**
 * Class representing a parallel evaluator
 * 
 * @author sgerasimou
 *
 */
public class UltimateModelEvaluator implements IParallelEvaluator {
	/** number of parallel executions (processes) */
	private int numberOfProcesses;

	private Problem[] problems;

	/** List of solutions */
	private List<Solution> solutionsList;

	/** Solution results list */
	private CopyOnWriteArrayList<Solution> evaluatedSolutions;

	/** Array of threads */
	private Thread[] threads;

	/** Array of runnables */
	private UltimateExecutor[] runnables;

	/** Set of connections array keeping the evaluators instances */
	// private Connection connections[];

	/**
	 * Constructor
	 * 
	 * @param processes
	 * @throws Exception
	 */
	/* TODO: Actually parallelise. Make port logic into a Utility function.
	 * This will require either the parallelisation of ULTIMATE or some sort of cloning of the ULTIMATE
	 * instance which can run across the numerous threads.
	 * 
	 * The latter is probably easier as a first approach.
	*/
	public UltimateModelEvaluator() {

		numberOfProcesses = 1;
		threads = new Thread[numberOfProcesses];
		runnables = new UltimateExecutor[numberOfProcesses];
		solutionsList = new ArrayList<Solution>();

	}

	/**
	 * Initialise evaluator (does NOT execute evaluation)
	 */
	public void startEvaluator(Problem problem) {
		// System.out.println("Cores: " + numberOfProcesses);

		try {
			problems = new Problem[numberOfProcesses];
			if (problem instanceof GeneticProblem) {
				for (int i = 0; i < numberOfProcesses; i++) {
					problems[i] = problem;// new GeneticProblem((GeneticProblem) problem);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add the solution (genetic individual) to the list of solutions to be
	 * evaluated
	 */
	public void addSolutionForEvaluation(Solution solution) {
		// System.out.println("Adding a solution to be evaluated");
		solutionsList.add(solution);
	}

	/**
	 * Run parallel evaluation
	 */
	public List<Solution> parallelEvaluation() {
		// System.out.println("Parallel evaluation");
		evaluatedSolutions = new CopyOnWriteArrayList<Solution>();
		this.reset();
		this.assignSolutions();
		this.startThreads();
		solutionsList.clear();
		// System.out.println("End of parallel evaluation....");
		return this.evaluatedSolutions;
	}

	/**
	 * Assign solutions to parallel processes
	 */
	private void assignSolutions() {
		if (numberOfProcesses > 1) {
			for (int i = 0; i < solutionsList.size(); i++) {
				// System.out.println("Assigning tasks");
				runnables[i % runnables.length].addSolutionForEvaluation(solutionsList
						.get(i));
			}
		} else {
			runnables[0].solutionsList = solutionsList;
		}
	}

	/**
	 * When done, reset the evaluators
	 */
	private void reset() {
		// if (numberOfProcesses > 1) {
		for (int i = 0; i < numberOfProcesses; i++) {
			// runnable executor is evo, maybe I can define an UltimateExecutor
			runnables[i] = new UltimateExecutor(problems[i]);
			threads[i] = new Thread(runnables[i]);
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
		for (Problem p : problems) {
			((GeneticModelProblem) p).closeDown();
		}
	}

	// public void setConnection(int id, Connection c) {
	// connections[id] = c;
	// }

	/**
	 * Inner class
	 * 
	 * @author sgerasimou
	 *
	 */
	private class UltimateExecutor implements Runnable {
		/** List of solutions to be evaluated */
		private List<Solution> solutionsList = new ArrayList<Solution>();

		/** Output */
		private PrintWriter out;

		/** Input */
		private BufferedReader in;

		/** Problem to be handled by this executor */
		Problem runnableProblem;

		// Connection connection;

		/**
		 * Class constructor: create a new runnable executor
		 * 
		 * @param out
		 * @param in
		 */
		public UltimateExecutor(Problem problem) {
			// this.in = in;
			// this.out = out;
			this.runnableProblem = problem;
			this.solutionsList = new ArrayList<Solution>();
		}

		// public UltimateExecutor(Connection c, Problem problem) {
		// this.in = c.getInChannel();
		// this.out = c.getOutChannel();
		// this.runnableProblem = problem;
		// this.solutionsList = new ArrayList<Solution>();
		// this.connection = c;
		// }

		/** Add a solution for evaluation */
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
					if (runnableProblem instanceof GeneticProblemUltimate) {
						boolean OK = ((GeneticProblemUltimate) runnableProblem).evaluateSolution(in, out, solution);
						if (!OK) {
							System.out.println("pEvaluation was not OK!");
						}
					} else
						throw new IllegalArgumentException("Problem not recognised");
				} catch (Exception e) {
					e.printStackTrace();
				}
				// System.out.println("Adding result");
				evaluatedSolutions.add(solution);
			}
		}
	}

	// private class Connection {

	// /** Socket **/
	// private Socket socket;

	// /** Input channel **/
	// private BufferedReader in;

	// /** Output channel **/
	// private PrintWriter out;

	// private final String HOSTNAME = "127.0.0.1";

	// private int portNum;

	// private UltimateModelEvaluator evaluator;

	// private int id;

	// public Connection(int portNum, int id, UltimateModelEvaluator evaluator)
	// throws Exception {
	// this.portNum = portNum;
	// this.evaluator = evaluator;
	// this.id = id;
	// start();
	// }

	// public Connection(Connection c) throws Exception {
	// this(c.portNum, c.id, c.evaluator);
	// evaluator.setConnection(id, this);
	// }

	// public void start() {

	// Ultimate ultimate = EvoChecker.getUltimateInstance();
	// ultimate.setInternalParameters();

	// }

	// public BufferedReader getInChannel() {
	// return in;
	// }

	// public PrintWriter getOutChannel() {
	// return out;
	// }

	// protected int getPort() {
	// return portNum;
	// }

	// public void close() throws IOException {
	// out.close();
	// in.close();
	// }

	// }
}