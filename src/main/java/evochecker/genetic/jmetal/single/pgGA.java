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
	  String seeding = Utility.getProperty("SEEDING").toUpperCase();	  
  
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
			  parallelEvaluator_.addSolutionForEvaluation(new Solution(population.get(0)));
			  parallelEvaluator_.addSolutionForEvaluation(new Solution(population.get(1)));
			  // Create the initial solutionSet
			  Solution newSolution;
			  for (int i = 2; i < populationSize; i++) {
				  newSolution = new Solution(problem_);
				  parallelEvaluator_.addSolutionForEvaluation(newSolution) ;
			  }
		  }
		  else if (seeding.equals("POPULATION")){
			  // Create the SAME solutionSet
			  for (int i = 0; i < populationSize; i++) {
				  parallelEvaluator_.addSolutionForEvaluation(new Solution(population.get(i))) ;
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
	  
	  // reset evaluations counter
	  int evaluations = 0;
	  
	  //initialise population
	  createInitialPopulation();
    
	  List<Solution> solutionList = parallelEvaluator_.parallelEvaluation() ;
    
	  for (Solution solution : solutionList) {
		  population.add(solution) ;
		  evaluations ++ ;
	  }

	  //evaluate
	  ObjectiveEvaluation.evaluateObjectives(population, problem_.getNumberOfObjectives());

	  population.sort(comparator) ;

	  // Generations 
	  while (evaluations < maxEvaluations) {
		  System.out.println("\n\nEvaluating: " + evaluations +"\t");
    	
		  // Copy the best two individuals to the offspring population
		  offspringPopulation.add(new Solution(population.get(0))) ;
		  offspringPopulation.add(new Solution(population.get(1))) ;
		  evaluations += 2;

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

		  solutionList = parallelEvaluator_.parallelEvaluation() ;
      
		  for (Solution solution : solutionList) {
			  offspringPopulation.add(solution);
			  evaluations++;	    
		  }

		  // The offspring population becomes the new current population
		  population.clear();
		  for (int i = 0; i < populationSize; i++) {
			  population.add(offspringPopulation.get(i)) ;
		  }
		  offspringPopulation.clear();

		  //evaluate
		  ObjectiveEvaluation.evaluateObjectives(population, problem_.getNumberOfObjectives());
      
		  population.sort(comparator) ;
	  }// while

	  // Return a population with the best individual
	  SolutionSet resultPopulation = new SolutionSet(1) ;
	  resultPopulation.add(population.get(0)) ;

	  System.out.println("Evaluations: " + evaluations ) ;
    
	  //    for (int i = 0; i < populationSize; i++) {
	  //    	Solution solution = population.get(i);
	  //		for (int objective=0; objective<solution.getNumberOfObjectives(); objective++){
	  //			System.out.printf("%.3f\t", solution.getObjective(objective));
	  //		}
	  //		System.out.println();
	  //    }
    
    return resultPopulation ;
  } // execute
  
  
  /**
   * Do any necessary cleanup
   */
  public void finalise(){
	  //stop the evaluators
	  parallelEvaluator_.stopEvaluators();
  }
  
} // pgGA
