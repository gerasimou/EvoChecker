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

import jmetal.core.*;
import jmetal.util.JMException;
import jmetal.util.comparators.ObjectiveComparator;

import java.util.Comparator;
import java.util.List;

import evochecker.genetic.jmetal.metaheuristics.IParallelEvaluator;

/** 
 * A multithreaded generational genetic algorithm
 */

public class pgGA extends Algorithm {

  IParallelEvaluator parallelEvaluator_ ;

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
   * Runs the pgGA algorithm.
   * @return a <code>SolutionSet</code> that is a set of non dominated solutions
   * as a result of the algorithm execution
   * @throws jmetal.util.JMException
   */
  public SolutionSet execute() throws JMException, ClassNotFoundException {
    int populationSize;
    int maxEvaluations;
    int evaluations;
//    int numberOfThreads ;

    SolutionSet population;
    SolutionSet offspringPopulation;
    SolutionSet union;

    Operator mutationOperator;
    Operator crossoverOperator;
    Operator selectionOperator;

    Comparator comparator        ;
    comparator = new ObjectiveComparator(3) ; // Single objective comparator

    //Read the parameters
    populationSize = ((Integer) getInputParameter("populationSize")).intValue();
    maxEvaluations = ((Integer) getInputParameter("maxEvaluations")).intValue();

    parallelEvaluator_.startEvaluator(problem_) ;

    //Initialize the variables
    population = new SolutionSet(populationSize);
    offspringPopulation = new SolutionSet(populationSize) ;

    evaluations = 0;

    //Read the operators
    mutationOperator = operators_.get("mutation");
    crossoverOperator = operators_.get("crossover");
    selectionOperator = operators_.get("selection");

    // Create the initial solutionSet
    Solution newSolution;
    for (int i = 0; i < populationSize; i++) {
      newSolution = new Solution(problem_);
      parallelEvaluator_.addSolutionForEvaluation(newSolution) ;
    }

    
    List<Solution> solutionList = parallelEvaluator_.parallelEvaluation() ;
    
    for (Solution solution : solutionList) {
      population.add(solution) ;
      evaluations ++ ;
    }

    //normalise objectives
//    normaliseObjectives(population);

    //evaluate
    evaluateObjectives(population);

    population.sort(comparator) ;

    // Generations 
    while (evaluations < maxEvaluations) {
       System.out.println("\n\nEvaluating: " + evaluations +"\t");
    	
      // Copy the best two individuals to the offspring population
      offspringPopulation.add(new Solution(population.get(0))) ;
      offspringPopulation.add(new Solution(population.get(1))) ;

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
      } // for

      solutionList = parallelEvaluator_.parallelEvaluation() ;
      
      for(Solution solution : solutionList) {
        offspringPopulation.add(solution);
        evaluations++;	    
      }

      // The offspring population becomes the new current population
      population.clear();
      for (int i = 0; i < populationSize; i++) {
        population.add(offspringPopulation.get(i)) ;
      }
      offspringPopulation.clear();

      //normalise objectives
//      normaliseObjectives(population);

      //evaluate
      evaluateObjectives(population);
      
      population.sort(comparator) ;
    } // while

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
  } // execute
  
  
  
  /**
   * Normalise objectives [0,1] for all objectives
   * @param solutionList
   */
//  private void normaliseObjectives(SolutionSet population){
//	  //get number of objectives
//	  int numberOfObjectives = problem_.getNumberOfObjectives();
//	  
//	  //get populationSize
//	  int populationSize = population.size();
//	  
//	  for (int objective=0; objective<numberOfObjectives-1; objective++){
//		  //find maximum		  
//		  double maximum = 0;
//		  for (int i = 0; i < populationSize; i++) {
//			  Solution solution = population.get(i);
//			  double result = solution.getObjective(objective);
//			  if (result > maximum)
//				  maximum = result;
//		  }
//		  //do normalisation
//		  for (int i = 0; i < populationSize; i++) {
//			  Solution solution = population.get(i);
//			  double result = solution.getObjective(objective);
//			  solution.setObjective(objective, result/maximum);
//		  }
//	  }
//  }
  
  
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
  
} // pgGA
