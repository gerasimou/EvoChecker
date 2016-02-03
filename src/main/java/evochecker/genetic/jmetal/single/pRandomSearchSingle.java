package evochecker.genetic.jmetal.single;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

import evochecker.EvoChecker;
import evochecker.auxiliary.Utility;
import evochecker.genetic.jmetal.metaheuristics.IParallelEvaluator;
import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;
import jmetal.util.NonDominatedSolutionList;
import jmetal.util.comparators.ObjectiveComparator;

public class pRandomSearchSingle extends Algorithm implements AlgorithmSteps {

	/** Population size */
	private int populationSize;

	/** Max Evaluations */
	private int maxEvaluations;

	/** Algorithm population (now a global variable) */
	private SolutionSet population;

	/** Algorithm offspring population (now a global variable) */
	private SolutionSet offspringPopulation;

	/** Comparator */
	Comparator comparator;

	/** Parallel Evaluator handle */
	private IParallelEvaluator parallelEvaluator_;

	/** Random */
	Random rand;

	
	/**
	 * Constructor
	 * @param problem
	 *            Problem to solve
	 * @param evaluator
	 *            Parallel evaluator
	 */
	public pRandomSearchSingle(Problem problem, IParallelEvaluator evaluator) {
		super(problem);
		parallelEvaluator_ = evaluator;
		rand = new Random(System.currentTimeMillis());
	}// constructor

	
	/**
	 * Do any necessary initialisations
	 */
	public void initialise() {
		// Read the parameters
		populationSize = ((Integer) getInputParameter("populationSize")).intValue();
		maxEvaluations = ((Integer) getInputParameter("maxEvaluations")).intValue();

		// Initialize the variables
		population = new SolutionSet(populationSize);
		offspringPopulation = new SolutionSet(populationSize);

		// Initialise single objective comparator
		comparator = new ObjectiveComparator(3);

		// Start the parallel evaluator
		parallelEvaluator_.startEvaluator(problem_);
	}

	
	
	/**
	 * Initialise population
	 * 
	 * @throws ClassNotFoundException
	 */
	public void createInitialPopulation() throws ClassNotFoundException {
		// Create the initial solution set
		Solution initSolution;
		for (int i = 0; i < populationSize; i++) {
			initSolution = new Solution(problem_);
			parallelEvaluator_.addSolutionForEvaluation(initSolution);
		}
	}

	
	
	@Override
	public SolutionSet execute() throws JMException, ClassNotFoundException {		
	    Solution bestSolution 	= null;//Double.MAX_VALUE;  
	    int bestSolutionSame  = 1;

	    //Reset evaluations counter
		int evaluations = 0;
		
		//Initialise population
		createInitialPopulation();
	    
	    //Run parallel evaluation
	    List<Solution> solutionList = parallelEvaluator_.parallelEvaluation();
	    
		//Clear the population 
		population.clear();

	    //Add the solutions to the population
	    for (Solution solution : solutionList){
	    	population.add(solution);
	    	evaluations++;
	    }
 
	    //Evaluate objective
	    ObjectiveEvaluation.evaluateObjectives(population, problem_.getNumberOfObjectives());
	    
	    //Sort population
	    population.sort(comparator) ;
	    
	    //Find best individual
	    bestSolution 		= population.get(0);
	    bestSolutionSame 	= 1;

	    
		  //Evolve 
	    while (evaluations < maxEvaluations){
	    	System.out.println("\n\nEvaluating: " + evaluations +"\t");
	    	
	    	 for (int i=0; i<populationSize; i++){
	  	    	Solution initSolution = new Solution(problem_);
	  	    	parallelEvaluator_.addSolutionForEvaluation(initSolution);
	  	    }
	    	
		    //Run parallel evaluation
		   solutionList = parallelEvaluator_.parallelEvaluation();
	    
		   //Add the solutions to the offspring population
		    for (Solution solution : solutionList){
		    	offspringPopulation.add(solution);
		    	evaluations++;
		    }

		    //Evaluate objectives
		    ObjectiveEvaluation.evaluateObjectives(offspringPopulation, problem_.getNumberOfObjectives());
		   
		    //increase capacity temporarily 
		    population.setCapacity(populationSize*2);
		    
		   //Add the solutions to the normal population
		    for (Solution solution : solutionList){
		    	population.add(solution);
		    }
		    offspringPopulation.clear();

		    
		    //sort the population
		    population.sort(comparator);
		    
		    //keep population size to a maximum size
		    while (population.size()>populationSize){
		    	population.remove(population.size()-1);
		    }

		    //set capacity to normal
		    population.setCapacity(populationSize);

//			System.out.println(bestSolution.toString() +"\n"+ population.get(0).toString());
			  

			  //Find best solution & check if it is the same with the previous best solution
			  if (solutionsAreEqual(bestSolution, population.get(0))){
				  bestSolutionSame++;
			  }
			  else{
				  bestSolution = population.get(0);
				  bestSolutionSame = 1;
			  }
			  
			  
			  //Log intermediate data every 500 iterations
			  if (evaluations % 500 == 0)
				  logIntermediateData(evaluations);
			  
			  //Termination criterion: no improvement over X generations
			  if (bestSolutionSame ==  (maxEvaluations/populationSize)/5)
				  break;
 
	    }//while
	   
		  //export evaluations to file
		  System.out.println("Evaluations: " + evaluations ) ;
		  Utility.exportToFile("data/EVAL_RS", evaluations+"", true);
	    
		  //update the intermediate log
		    //find the nearest %500 == 0
		    if (evaluations%500!=0)
		    	for (; evaluations%500!=0; evaluations+=50);
		  while (evaluations <= maxEvaluations){
			  logIntermediateData(evaluations);
			  evaluations += 500;
		  }

		  // Return a population with the best individual
	    SolutionSet resultPopulation = new SolutionSet(1) ;
	    resultPopulation.add(population.get(0)) ;
	    return resultPopulation ;	    
	}//execute
	
	
	
	  
	private boolean solutionsAreEqual (Solution solution1, Solution solution2){
		int objectivesTotal = solution1.getNumberOfObjectives()-1;
		for (int index=0; index<objectivesTotal; index++){
			if ( Math.abs(solution1.getObjective(index) - solution2.getObjective(index)) > 0.001)
				return false;
		}
		return true;  
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
		  Utility.exportToFile("data/FUN_RS_E"+ EvoChecker.adaptationStep +"_"+ iterations, 
				  			   population.get(0).toString(), true);
			Utility.printVariablesToFile("data/VAR_RS_E"+ EvoChecker.adaptationStep +"_"+ iterations,
							   population.get(0), true);
	  }
	
}//class
