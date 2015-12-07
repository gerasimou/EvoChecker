package evochecker.genetic.jmetal.metaheuristics;

import java.util.List;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.metaheuristics.spea2.SPEA2;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;
import jmetal.util.Ranking;
import jmetal.util.Spea2Fitness;

/**
 * Parallel implementation of SPEA2
 * @author sgerasimou
 *
 */
public class pSPEA2 extends Algorithm{
	IParallelEvaluator parallelEvaluator_;

	/**
	 * Constructor 
	 * @param problem Problem to solve
	 * @param evaluator Parallel evaluator
	 */
	public pSPEA2(Problem problem, IParallelEvaluator evaluator) {
		super(problem);
		parallelEvaluator_ = evaluator;
	}//pSPEA2

	
	/**
	 * Runs the SPEA2 algorithm
   * @return a <code>SolutionSet</code> that is a set of non dominated solutions
   * as a result of the algorithm execution
   * @throws JMException 
   */
	@Override
	public SolutionSet execute() throws JMException, ClassNotFoundException {
		int populationSize;
		int maxEvaluations;
		int evaluations;
		int archiveSize;
		
		// QualityIndicator object
	    QualityIndicator indicators; 
		
		//Solution sets 
		SolutionSet population;
		SolutionSet archive;
		SolutionSet offSpringSolutionSet;
		
		//operators
		Operator mutationOperator;
		Operator crossoverOperator;
		Operator selectionOperator;
		
	    //Read the parameters
		populationSize 	= ((Integer)getInputParameter("populationSize")).intValue();
		archiveSize    	= ((Integer)getInputParameter("archiveSize")).intValue();
	    maxEvaluations 	= ((Integer)getInputParameter("maxEvaluations")).intValue();

	    //Read the indicators
	    indicators		= (QualityIndicator) getInputParameter("indicators");
	    
	    //Start the evaluator (parallel)
	    parallelEvaluator_.startEvaluator(problem_);
	    
	    //Read the operators
	    crossoverOperator 	= operators_.get("crossover");
	    mutationOperator	= operators_.get("mutation");
	    selectionOperator 	= operators_.get("selection");
	    
	    //initialise the variables
	    population			= new SolutionSet(populationSize);
	    archive				= new SolutionSet(archiveSize);
	    evaluations			= 0;
		
	    //Create the initial solution set
	    Solution newSolution;
	    for (int i=0; i<populationSize; i++){
	    	newSolution = new Solution(problem_);
	    	parallelEvaluator_.addSolutionForEvaluation(newSolution);
	    }
	    
	    //Run and evaluate the initial solution set
	    List<Solution> solutionList = parallelEvaluator_.parallelEvaluation();
	    for (Solution solution : solutionList){
	    	population.add(solution);
	    	evaluations++;
	    }
	    
	    //Generations
	    while (evaluations < maxEvaluations){
	    	System.out.println("Evaluations:\t" + evaluations);

	    	SolutionSet union = ((SolutionSet)population).union(archive);
	    	Spea2Fitness spea = new Spea2Fitness(union);
	    	spea.fitnessAssign();
	    	archive = spea.environmentalSelection(archiveSize);
	    	
	    	//create a new offspring population
	    	offSpringSolutionSet = new SolutionSet(populationSize);
	    	Solution[] parents = new Solution[2];
	    	
//	    	while (offSpringSolutionSet.size() < populationSize){
	    	for (int index=0; index<populationSize; index++){
	    		int j = 0;
	    		do{
	    			j++;
	    			parents[0] = (Solution)selectionOperator.execute(archive);
	    		}
	    		while (j < SPEA2.TOURNAMENTS_ROUNDS); // do-while
	    		int k =0;
	    		do{
	    			k++;                
	    	        parents[1] = (Solution)selectionOperator.execute(archive);    
	    		} 
	    		while (k < SPEA2.TOURNAMENTS_ROUNDS); // do-while
	    		
	    		//crossover
	            Solution [] offSpring = (Solution [])crossoverOperator.execute(parents);            

	            //mutation
	            mutationOperator.execute(offSpring[0]);
	            
	            parallelEvaluator_.addSolutionForEvaluation(offSpring[0]);
	            
	    	}//for

	    	//parallel evaluation
	    	List<Solution> solutions = parallelEvaluator_.parallelEvaluation();
	    	
	    	for (Solution solution : solutions){
	    		offSpringSolutionSet.add(solution);
	    		evaluations++;
	    	}
	    	
	        //create a offSpring solutionSet
	        population = offSpringSolutionSet;
	    }//while
	    
	    parallelEvaluator_.stopEvaluators();
	    
	    Ranking ranking = new Ranking(archive);
	    return ranking.getSubfront(0);
	    
	} // execute    
}//pSPEA2
