package evochecker.genetic.jmetal.metaheuristics;

import java.util.Comparator;
import java.util.List;

import evochecker.evaluator.IParallelEvaluator;
import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.Neighborhood;
import jmetal.util.Ranking;
import jmetal.util.archive.CrowdingArchive;
import jmetal.util.comparators.CrowdingComparator;
import jmetal.util.comparators.DominanceComparator;

/**
 * Parallel implementation of MoCell (sMoCell2)
 * @author sgerasimou
 *
 */
public class pMoCell extends Algorithm{

	IParallelEvaluator parallelEvaluator_ ; 

  /**
   * Constructor
   * @param problem Problem to solve
   * @param evaluator Parallel evaluator
   */
	public pMoCell(Problem problem, IParallelEvaluator evaluator) {
		super(problem);
		parallelEvaluator_ = evaluator;
	}//pMoCell
	
	
   /**
   * Runs the SPEA2 algorithm
   * @return a <code>SolutionSet</code> that is a set of non dominated solutions
   * as a result of the algorithm execution
   * @throws JMException 
   */
	@Override
	public SolutionSet execute() throws JMException, ClassNotFoundException {
		int populationSize;
		int archiveSize;
		int maxEvaluations;
		int evaluations;
		//int feedback;
		
		// QualityIndicator object
	    QualityIndicator indicators; 

		//operators
		Operator mutationOperator;
		Operator crossoverOperator;
		Operator selectionOperator;

		//Solution sets 
		SolutionSet population;
		SolutionSet newSolutionSet;
		SolutionSet[] neighbours;

		CrowdingArchive archive;
		
		Neighborhood neigbourhood;
		
		Comparator dominanceComparator 	= new DominanceComparator();
		Comparator crowdingComparator	= new CrowdingComparator();
		
		Distance distance = new Distance();
				
	    //Read the parameters
		populationSize 	= ((Integer)getInputParameter("populationSize")).intValue();
		archiveSize    	= ((Integer)getInputParameter("archiveSize")).intValue();
	    maxEvaluations 	= ((Integer)getInputParameter("maxEvaluations")).intValue();

	    // Read the operators
	    mutationOperator  = operators_.get("mutation");
	    crossoverOperator = operators_.get("crossover");
	    selectionOperator = operators_.get("selection");        

	    //Read the indicators
	    indicators		= (QualityIndicator) getInputParameter("indicators");

	    //Start the evaluator (parallel)
	    parallelEvaluator_.startEvaluator(problem_);

	    //Initialise the variables
	    population 		= new SolutionSet(populationSize);
	    newSolutionSet	= new SolutionSet(populationSize);
	    archive			= new CrowdingArchive(archiveSize, problem_.getNumberOfObjectives());
	    evaluations		= 0;
	    neigbourhood	= new Neighborhood(populationSize);
	    neighbours		= new SolutionSet[populationSize];
	    
	    //Create the initial population
	   Solution newSolution;
	   for (int i=0; i<populationSize; i++){
		   newSolution = new Solution(problem_);
		   parallelEvaluator_.addSolutionForEvaluation(newSolution);
	   }
	   
	   //run parallel evaluation
	   List<Solution> solutionList = parallelEvaluator_.parallelEvaluation() ;

	   for (int i=0; i<solutionList.size(); i++){
		   Solution solution = solutionList.get(i);
		   population.add(solution);
		   solution.setLocation(i);
		   evaluations++;
	   }
	    
	    // Main loop
	    while (evaluations < maxEvaluations){                                 
	    	newSolutionSet = new SolutionSet(populationSize);

	    	for (int index=0; index<population.size(); index++){
	    		Solution individual = new Solution(population.get(index));
	    		
	    		Solution[] parents  = new Solution[2];
	    		Solution[] offSpring;
	    		
	    		neighbours[index] = neigbourhood.getEightNeighbors(population, index);
	    		neighbours[index].add(individual);
	    		
	    		//parents
	            parents[0] = (Solution)selectionOperator.execute(neighbours[index]);
	            if (archive.size()>0) {
	            	parents[1] = (Solution)selectionOperator.execute(archive);
	            } 
	            else {
	            	parents[1] = (Solution)selectionOperator.execute(neighbours[index]);
	            }
	            
	            //Create a new solution, using genetic operators mutation and crossover
	            offSpring = (Solution [])crossoverOperator.execute(parents);
	            mutationOperator.execute(offSpring[0]);
	            
	            //add solution for evaluation
	            parallelEvaluator_.addSolutionForEvaluation(offSpring[0]);
	    	}//for
	    	
	    	//parallel evaluation
	    	List<Solution> solutions = parallelEvaluator_.parallelEvaluation();
	    	
	    	for (int index=0; index<solutions.size(); index++){
	    		//get the individual
	    		int neighboursNum   = neighbours[index].size()-1;
	    		Solution individual = neighbours[index].get(neighboursNum);
	    		
	    		Solution solution   = solutions.get(index);
	    		
	            int flag = dominanceComparator.compare(individual,solution);
	            
	            if (flag == -1) {
	            	newSolutionSet.add(new Solution(population.get(index)));  
	            }
	            
	            if (flag == 1) {//The new individual dominates
	            	solution.setLocation(individual.getLocation());
	            	newSolutionSet.add(solution);
	            	archive.add(new Solution(solution));
	            }
	            else if (flag ==0){//The individuals are non-dominated
	            	neighbours[index].add(solution);
	                Ranking rank = new Ranking(neighbours[index]);
	                for (int j = 0; j < rank.getNumberOfSubfronts(); j++){
	                	distance.crowdingDistanceAssignment(rank.getSubfront(j),problem_.getNumberOfObjectives());
	                }
	                boolean deleteMutant = true;
	                
	                int compareResult = crowdingComparator.compare(individual,solution);
	                if (compareResult == 1){ //The solution is better
	                	deleteMutant = false;
	                }
	                
	                if (!deleteMutant){
	                  solution.setLocation(individual.getLocation());
	                  //currentSolutionSet.reemplace(offSpring[0].getLocation(),offSpring[0]);
	                  newSolutionSet.add(solution);
	                  archive.add(new Solution(solution));
	                }
	                else{
	                  newSolutionSet.add(new Solution(population.get(index)));
	                  archive.add(new Solution(solution));    
	                }
	            }
	    	evaluations++;
	    	}//for
	    	
	    	population = newSolutionSet;

	    }//while	    
		
	    parallelEvaluator_.stopEvaluator();

		return archive;
	}//execute	
}
