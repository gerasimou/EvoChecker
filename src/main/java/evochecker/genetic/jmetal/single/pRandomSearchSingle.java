package evochecker.genetic.jmetal.single;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

import evochecker.genetic.jmetal.metaheuristics.IParallelEvaluator;
import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;
import jmetal.util.NonDominatedSolutionList;
import jmetal.util.comparators.ObjectiveComparator;

public class pRandomSearchSingle extends Algorithm{

	IParallelEvaluator parallelEvaluator_ ; 
	Random rand;

  /**
   * Constructor
   * @param problem Problem to solve
   * @param evaluator Parallel evaluator
   */
	public pRandomSearchSingle(Problem problem, IParallelEvaluator evaluator) {
		super(problem);
		parallelEvaluator_ = evaluator;
		rand = new Random (System.currentTimeMillis());
	}//constructor

	@Override
	public SolutionSet execute() throws JMException, ClassNotFoundException {
		int populationSize;
		int maxEvaluations;
		int evaluations;
		
		SolutionSet population;
	    SolutionSet offspringPopulation;
//	    NonDominatedSolutionList ndList = new NonDominatedSolutionList();

//	    QualityIndicator indicators; // QualityIndicator object

	    //Read the parameters
		populationSize 	= ((Integer)getInputParameter("populationSize")).intValue();
		maxEvaluations 	= ((Integer)getInputParameter("maxEvaluations")).intValue();		
//	    indicators 		= (QualityIndicator) getInputParameter("indicators");

	    Comparator comparator        ;
	    comparator = new ObjectiveComparator(3) ; // Single objective comparator
		
	    //Start the parallel evaluator
	    parallelEvaluator_.startEvaluator(problem_);

	    //Initialise the variables
	    population 				= new SolutionSet(populationSize);
	    offspringPopulation 	= new SolutionSet(populationSize);
	    evaluations				= 0;
	    
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
	    	population.add(solution);
//	    	ndList.add(solution);
	    	evaluations++;
	    }

	    //evaluate
	    evaluateObjectives(population);
	    
	    population.sort(comparator) ;

	    
	    //Iterate until the max generations
	    while (evaluations < maxEvaluations){
	        System.out.println("\n\nEvaluating: " + evaluations +"\t");
	    	
	    	 for (int i=0; i<populationSize; i++){
	  	    	initSolution = new Solution(problem_);
	  	    	parallelEvaluator_.addSolutionForEvaluation(initSolution);
	  	    }
	    	
		    //Run parallel evaluation
		   solutionList = parallelEvaluator_.parallelEvaluation();
	    
		   //Add the solutions to the offspring population
		    for (Solution solution : solutionList){
		    	offspringPopulation.add(solution);
		    	evaluations++;
		    }

		    //evaluate
		    evaluateObjectives(offspringPopulation);
		    
		   //Add the solutions to the normal population
		    for (Solution solution : solutionList){
		    	population.add(solution);
		    }
		    offspringPopulation.clear();

		    
		    //sort the population
		    population.sort(comparator);
		    
		  //keep population size to a maximum possible size
		    while (population.size()>populationSize){
		    	population.remove(population.size()-1);
		    }
	    }//while
	    
	    parallelEvaluator_.stopEvaluators();
	    
	    // Return a population with the best individual
	    SolutionSet resultPopulation = new SolutionSet(1) ;
	    resultPopulation.add(population.get(0)) ;
	    
	    
	    System.out.println("Evaluations: " + evaluations ) ;
	    
	    for (int i = 0; i < populationSize; i++) {
	    	Solution solution = population.get(i);
			for (int objective=0; objective<solution.getNumberOfObjectives(); objective++){
				System.out.printf("%.3f\t", solution.getObjective(objective));
			}
			System.out.println();
	    }
	    
	    return resultPopulation ;	    
	}//execute
	
	
	  /**
	   * Evaluate the objectives
	   * @param solutionList
	   */
	  private void evaluateObjectives(SolutionSet population){
		  //get populationSize
		  int populationSize = population.size();
		  
		//get number of objectives
		  int numberOfObjectives = problem_.getNumberOfObjectives()-1;//the last holds the weighted result
		  
		  //maximum value per objective
		  double maximumPerObjective[] = new double[numberOfObjectives];
		  
		  for (int objective=0; objective<numberOfObjectives; objective++){
			  //find maximum		  
			  double maximum = 0;
			  for (int i = 0; i < populationSize; i++) {
				  Solution solution = population.get(i);
				  double result = solution.getObjective(objective);
				  if (result > maximum)
					  maximum = result;
			  }
			  maximumPerObjective[objective] = maximum;
		  }

		  for (int i = 0; i < populationSize; i++) {
			  Solution solution = population.get(i);
				double w = 0.5;
				double evaluation = (w * maximumPerObjective[0]/solution.getObjective(0)) + 
									(1-w) * solution.getObjective(1)/maximumPerObjective[1];
				solution.setObjective(3, evaluation);
		  }
	  }

	
}
