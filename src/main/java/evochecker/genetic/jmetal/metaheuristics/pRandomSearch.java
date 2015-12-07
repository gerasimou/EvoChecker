package evochecker.genetic.jmetal.metaheuristics;

import java.util.List;
import java.util.Random;

import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;
import jmetal.util.NonDominatedSolutionList;

public class pRandomSearch extends Algorithm{

	IParallelEvaluator parallelEvaluator_ ; 
	Random rand;

  /**
   * Constructor
   * @param problem Problem to solve
   * @param evaluator Parallel evaluator
   */
	public pRandomSearch(Problem problem, IParallelEvaluator evaluator) {
		super(problem);
		parallelEvaluator_ = evaluator;
		rand = new Random (System.currentTimeMillis());
	}//constructor

	@Override
	public SolutionSet execute() throws JMException, ClassNotFoundException {
		int populationSize;
		int maxEvaluations;
		int evaluations;
		
//		SolutionSet population;
	    NonDominatedSolutionList ndList = new NonDominatedSolutionList();

	    QualityIndicator indicators; // QualityIndicator object

	    //Read the parameters
		populationSize 	= ((Integer)getInputParameter("populationSize")).intValue();
		maxEvaluations 	= ((Integer)getInputParameter("maxEvaluations")).intValue();		
	    indicators 		= (QualityIndicator) getInputParameter("indicators");
		
	    //Start the parallel evaluator
	    parallelEvaluator_.startEvaluator(problem_);

	    //Initialise the variables
//	    population 	= new SolutionSet(populationSize);
	    evaluations	= 0;
	    
	    //Create the initial solution set
	    Solution initSolution;
	    for (int i=0; i<populationSize; i++){
	    	initSolution = new Solution(problem_);
	    	parallelEvaluator_.addSolutionForEvaluation(initSolution);
	    }
	    
	    //Run parallel evaluation
	    List<Solution> solutionList = parallelEvaluator_.parallelEvaluation();
	    
	    //Add the solutions to the population
	    for (Solution solution : solutionList){
//	    	population.add(solution);
	    	ndList.add(solution);
	    	evaluations++;
	    }
	    
	    
	    //Iterate until the max generations
	    while (evaluations < maxEvaluations){
	    	System.out.println("Evaluations:\t" + evaluations);
	    	
	    	 for (int i=0; i<populationSize; i++){
	  	    	initSolution = new Solution(problem_);
	  	    	parallelEvaluator_.addSolutionForEvaluation(initSolution);
	  	    }
	    	
		    //Run parallel evaluation
		   solutionList = parallelEvaluator_.parallelEvaluation();
	    
		   //Add the solutions to the population
		    for (Solution solution : solutionList){
		    	ndList.add(solution);
		    	evaluations++;
		    }
		    
		    
		  //keep pareto size to a maximum population size
		    while (ndList.size()>populationSize){
		    	int size 	= ndList.size();
		    	int index	= rand.nextInt(size);
		    	ndList.remove(index);
		    }
	    }
	    
	    parallelEvaluator_.stopEvaluators();
	    
	    // Return as output parameter the required evaluations
//	    setOutputParameter("evaluations", requiredEvaluations);

	    return ndList;
	    
	}//execute

	
}
