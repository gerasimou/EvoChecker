package evochecker.auxiliary;

import evochecker.EvoChecker;
import evochecker.genetic.jmetal.MultiProcessPrismEvaluator;
import evochecker.genetic.jmetal.metaheuristics.IParallelEvaluator;
import evochecker.genetic.jmetal.single.AlgorithmSteps;
import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.util.JMException;

public class ExhaustiveSearch extends Algorithm implements AlgorithmSteps {

	/** Parallel Evaluator handle*/
	private IParallelEvaluator parallelEvaluator_ ;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExhaustiveSearch(String problemName, Problem problem) {
		super(problem);
	
		//Create algorithm and parallel objects
		parallelEvaluator_ = new MultiProcessPrismEvaluator(0);
	}

	
	@Override
	public void initialise() {
		// TODO Auto-generated method stub
	}
	  
	
	@Override
	public void createInitialPopulation() throws ClassNotFoundException {
		// TODO Auto-generated method stub

	}

	
	@Override
	public SolutionSet execute() throws JMException, ClassNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	
	/**
	 * Do any necessary cleanup
	 */
	public void finalise(){
		//stop the evaluators
		parallelEvaluator_.stopEvaluators();
	}

	
	/**
	 * Log intermediate data
	 */ 
	private void logIntermediateData(int iterations){
//		Utility.exportToFile("data/FUN_SGA_"+ seeding +"_E"+ EvoChecker.adaptationStep +"_"+ iterations, 
//				  			   population.get(0).toString(), true);
//		Utility.printVariablesToFile("data/VAR_SGA_"+ seeding +"_E"+ EvoChecker.adaptationStep +"_"+ iterations,
//							   population.get(0), true);  
	}	
}
