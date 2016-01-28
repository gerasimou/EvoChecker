//  pgGA.java
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

package evochecker.genetic.jmetal.single;

import java.util.Comparator;
import java.util.List;

import evochecker.auxiliary.Utility;
import evochecker.genetic.jmetal.metaheuristics.IParallelEvaluator;
import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.util.JMException;
import jmetal.util.comparators.ObjectiveComparator;

/** 
 * A multithreaded generational genetic algorithm
 */

public class pgGA extends Algorithm implements AlgorithmSteps{
  
  /** Population size*/
  private int populationSize;

  /** Max Evaluations*/
  private int maxEvaluations;

  /** Algorithm population (now a global variable)*/
  private SolutionSet population;

  /** Algorithm offspring population (now a global variable)*/
  private SolutionSet offspringPopulation;

  /** Mutation operator*/
  Operator mutationOperator;

  /** Crossover operator*/
  Operator crossoverOperator;
  
  /** Selection operator*/
  Operator selectionOperator;

  /** Comparator*/
  Comparator comparator;
  
  /** Seeding technique */
  String seeding;

  /** Parallel Evaluator handle*/
  private IParallelEvaluator parallelEvaluator_ ;

  
  /**
   * Constructor
   * @param problem Problem to solve
   * @param evaluator Parallel evaluator
   */
  public pgGA(Problem problem, IParallelEvaluator evaluator) {
    super (problem) ;

    parallelEvaluator_ = evaluator ;
  } // pgGA

  
  /**
   * Do any necessary initialisations
   */
  public void initialise(){
	  //Read the parameters
	  populationSize = ((Integer) getInputParameter("populationSize")).intValue();
	  maxEvaluations = ((Integer) getInputParameter("maxEvaluations")).intValue();

	  //Initialize the variables
	  population 			= new SolutionSet(populationSize);
	  offspringPopulation = new SolutionSet(populationSize) ;

	  //Read the operators
	  mutationOperator = operators_.get("mutation");
	  crossoverOperator = operators_.get("crossover");
	  selectionOperator = operators_.get("selection");

	  // Initialise single objective comparator
	  comparator = new ObjectiveComparator(3) ; 
	  
	  //Start the parallel evaluator
	  parallelEvaluator_.startEvaluator(problem_) ;
  }
  
  
  /**
   * Initialise population
 * @throws ClassNotFoundException 
   */
  public void createInitialPopulation() throws ClassNotFoundException {
	  seeding = Utility.getProperty("SEEDING").toUpperCase();	  
  
	  try{
		  if (seeding.equals("NORMAL")){
			  //clear population
			  population.clear();
			  
			  // Create the initial solutionSet
			  Solution newSolution;
			  for (int i = 0; i < populationSize; i++) {
				  newSolution = new Solution(problem_);
				  parallelEvaluator_.addSolutionForEvaluation(newSolution) ;
			  }
		  }
		  else if (seeding.equals("BEST")){
			  int i = 0;
			  if (population.size()>2){
				  parallelEvaluator_.addSolutionForEvaluation(new Solution(population.get(0)));
				  parallelEvaluator_.addSolutionForEvaluation(new Solution(population.get(1)));
				  i = 2;
			  }
			  // Create the initial solutionSet
			  Solution newSolution;
			  for (; i < populationSize; i++) {
				  newSolution = new Solution(problem_);
				  parallelEvaluator_.addSolutionForEvaluation(newSolution) ;
			  }
		  }
		  else if (seeding.equals("POPULATION")){
			  // Create the SAME solutionSet
			  if (population.size() == populationSize){
				  for (int i = 0; i < populationSize; i++) {
					  parallelEvaluator_.addSolutionForEvaluation(new Solution(population.get(i))) ;
				  }
			  }
			  else{
				  // Create the initial solutionSet
				  Solution newSolution;
				  for (int i = population.size(); i < populationSize; i++) {
					  newSolution = new Solution(problem_);
					  parallelEvaluator_.addSolutionForEvaluation(newSolution) ;
				  }
			  }
		  }
		  else throw new Exception();
	  }
	  catch (Exception e){
		  e.printStackTrace();
		  System.exit(0);
	  }	  	  
  }
  
  
  /**
   * Runs the pgGA algorithm.
   * @return a <code>SolutionSet</code> that is a set of non dominated solutions
   * as a result of the algorithm execution
   * @throws jmetal.util.JMException
   */
  public SolutionSet execute() throws JMException, ClassNotFoundException {
	  Solution bestSolution 	= null;//Double.MAX_VALUE;
	  int bestSolutionSame  = 1;
	  
	  
	  //Reset evaluations counter
	  int evaluations = 0;
	  
	  //Initialise population
	  createInitialPopulation();
    
	  //Run parallel evaluation
	  List<Solution> solutionList = parallelEvaluator_.parallelEvaluation() ;
    
	  //Clear the population 
	  population.clear();
	  
	  //Add solutionList to the population
	  for (Solution solution : solutionList) {
		  population.add(solution) ;
		  evaluations ++ ;
	  }

	  //Evaluate objective
	  ObjectiveEvaluation.evaluateObjectives(population, problem_.getNumberOfObjectives());

	  //Sort population
	  population.sort(comparator) ;
	  
	  //Find best individual
	  bestSolution 		= population.get(0);
	  bestSolutionSame 	= 1;

	  //Evolve 
	  while (evaluations < maxEvaluations) {
		  System.out.println("\nEvaluating: " + evaluations +"\t");
    	
		  // Copy the best two individuals to the offspring population
		  offspringPopulation.add(new Solution(population.get(0))) ;
		  offspringPopulation.add(new Solution(population.get(1))) ;
		  evaluations += 2;

		  //Create offspring population using crossover & mutation
		  Solution[] parents = new Solution[2];
		  for (int i = 0; i < (populationSize / 2) - 1; i++) {
			  if (evaluations < maxEvaluations) {
				  //obtain parents
				  parents[0] = (Solution) selectionOperator.execute(population);
				  parents[1] = (Solution) selectionOperator.execute(population);
				  Solution[] offSpring = (Solution[]) crossoverOperator.execute(parents);
				  mutationOperator.execute(offSpring[0]);
				  mutationOperator.execute(offSpring[1]);
				  parallelEvaluator_.addSolutionForEvaluation(offSpring[0]) ;
				  parallelEvaluator_.addSolutionForEvaluation(offSpring[1]) ;
			  } // if                            
		  }// for

		  //Run parallel evaluation
		  solutionList = parallelEvaluator_.parallelEvaluation() ;

		  //Add solutionList to the population
		  for (Solution solution : solutionList) {
			  offspringPopulation.add(solution);
			  evaluations++;	    
		  }

		  //The offspring population becomes the new current population
		  population.clear();
		  for (int i = 0; i < populationSize; i++) {
			  population.add(offspringPopulation.get(i)) ;
		  }
		  offspringPopulation.clear();

		  //Evaluate objectives
		  ObjectiveEvaluation.evaluateObjectives(population, problem_.getNumberOfObjectives());
      
		  //Sort population
		  population.sort(comparator) ;

		  System.out.println(bestSolution.toString() +"\n"+ population.get(0).toString());
		  

		  //Find best solution & check if it is the same with the previous best solution
		  if (solutionsAreEqual(bestSolution, population.get(0))){
			  bestSolutionSame++;
		  }
		  else{
			  bestSolution = population.get(0);
			  bestSolutionSame = 1;
		  }
		  
		  
		  //Termination criterion: no improvement over X generations
		  if (bestSolutionSame ==  (maxEvaluations/populationSize)/10)
			  break;
	  }// while
	  	  
	  //export evaluations to file
	  System.out.println("Evaluations: " + evaluations ) ;
	  Utility.exportToFile("data/EVAL_SGA_"+seeding, evaluations+"", true);
	  
	  // Return a population with the best individual
	  SolutionSet resultPopulation = new SolutionSet(1) ;
	  resultPopulation.add(population.get(0)) ;

    return resultPopulation ;
  } // execute
  
  
  
  /**
   * Check if two solutions are equal. They are equal when their
   * respective objectives differ less than epsilon, e.g., e=0.001
   * @param solution1
   * @param solution2
   * @return
   */
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
  
} // pgGA
