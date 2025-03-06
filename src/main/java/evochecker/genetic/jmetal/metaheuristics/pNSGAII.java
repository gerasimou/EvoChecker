//  pNSGAII.java
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

package evochecker.genetic.jmetal.metaheuristics;

import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import evochecker.evaluator.IParallelEvaluator;
import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.Ranking;
import jmetal.util.comparators.CrowdingComparator;
import jmetal.core.Variable;

import evochecker.genetic.jmetal.encoding.ArrayReal;
import evochecker.genetic.jmetal.encoding.ArrayInt;
import java.io.File;
import evochecker.auxiliary.Constants;
import evochecker.auxiliary.FileUtil;
import evochecker.auxiliary.Utility;

/** 
 *  Implementation of NSGA-II.
 *  This implementation of NSGA-II makes use of a QualityIndicator object
 *  to obtained the convergence speed of the algorithm. This version is used
 *  in the paper:
 *     A.J. Nebro, J.J. Durillo, C.A. Coello Coello, F. Luna, E. Alba 
 *     "A Study of Convergence Speed in Multi-Objective Metaheuristics." 
 *     To be presented in: PPSN'08. Dortmund. September 2008.
 */

public class pNSGAII extends Algorithm {

  IParallelEvaluator parallelEvaluator_ ; 

  /**
   * Constructor
   * @param problem Problem to solve
   * @param evaluator Parallel evaluator
   */
  public pNSGAII(Problem problem, IParallelEvaluator evaluator) {
    super (problem) ;

    parallelEvaluator_ = evaluator ;
  } // pNSGAII
  
  
  
  /**
   * This reads a previous Pareto set solutions. It returns up to populationSize2seed solutions.
   * Solutions are read from the NSGAII folder. The most recent file is read.
   * If solutions contain NaN, they are removed.
 * @return list of strings representing solutions read from previous Pareto set file
   * 
   */
   public List<String> getPreviousSavedPopulation() {
       // Construct the path to the Pareto set directory
       String baseDirectory = "./data/" + Utility.getProperty(Constants.PROBLEM_KEYWORD).toUpperCase() + "/NSGAII/";
       File paretoSetDirectory = new File(baseDirectory);
       String[] existingFiles = paretoSetDirectory.list();

       // Find the most recently modified Pareto set file
       long latestModifiedTime = 0;
       String latestParetoSetFile = "";
       List<String> solutionLines = new ArrayList<>();  // Holds the lines of the Pareto set file
       
       if (existingFiles == null) {
		   return solutionLines;
	   }
       
       for (String fileName : existingFiles) {
           // Check for files that match the "Set" pattern
           if (fileName.split("_")[4].equals("Set")) {
               File file = new File(baseDirectory + fileName);
               long lastModified = file.lastModified();
               if (lastModified > latestModifiedTime) {
                   latestParetoSetFile = fileName;
                   latestModifiedTime = lastModified;
               }
           }
       }

       // If a Pareto set file is found, read its contents
       if (!latestParetoSetFile.isEmpty()) {
           try (BufferedReader reader = new BufferedReader(new FileReader(baseDirectory + latestParetoSetFile))) {
               String line;
               while ((line = reader.readLine()) != null) {
                   solutionLines.add(line);
               }
           } catch (IOException e) {
               e.printStackTrace();
           }

           // Remove the first two lines (header and empty line)
           if (solutionLines.size() > 2) {
               solutionLines.remove(0); // Remove header
               solutionLines.remove(0); // Remove empty line
           }

           // Remove lines containing "NaN"
           solutionLines.removeIf(line -> line.contains("NaN"));
           
           System.out.println("[Seeding] Reading previous Pareto Set: " + latestParetoSetFile);
       }

       return solutionLines;
   }
   
   /**
	* This method returns the first N solutions from the list of solutions.
	* If the list is smaller than N, the whole list is returned.
	* @param solutionLines list of solutions
	* @param populationSize2seed number of solutions to return
	* @return list of solutions
	*/
   private List<String> getFirstNSolutions(List<String> solutionLines, int populationSize2seed) {
		return solutionLines.subList(0, Math.min(solutionLines.size(), populationSize2seed));
	}

  
  
  /**   
   * Runs the NSGA-II algorithm.
   * @return a <code>SolutionSet</code> that is a set of non dominated solutions
   * as a result of the algorithm execution
   * @throws JMException 
   */
  public SolutionSet execute() throws JMException, ClassNotFoundException {
    int populationSize;
    int maxEvaluations;
    int evaluations;
    int numberOfThreads ;
    
    QualityIndicator indicators; // QualityIndicator object
    int requiredEvaluations; // Use in the example of use of the
    // indicators object (see below)

    SolutionSet population;
    SolutionSet offspringPopulation;
    SolutionSet union;

    Operator mutationOperator;
    Operator crossoverOperator;
    Operator selectionOperator;

    Distance distance = new Distance();

    //Read the parameters
    populationSize = ((Integer) getInputParameter("populationSize")).intValue();
    maxEvaluations = ((Integer) getInputParameter("maxEvaluations")).intValue();
    indicators = (QualityIndicator) getInputParameter("indicators");

    parallelEvaluator_.startEvaluator(problem_) ;

    //Initialize the variables
    population = new SolutionSet(populationSize);
    evaluations = 0;
    requiredEvaluations = 0;

    //Read the operators
    mutationOperator = operators_.get("mutation");
    crossoverOperator = operators_.get("crossover");
    selectionOperator = operators_.get("selection");

    //Create the initial solutionSet
    Solution newSolution;
    
    // a) check if reloading from previous population
    int populationSize2seed = 0;
    try { populationSize2seed = Math.round( Integer.parseInt(Utility.getProperty(Constants.RELOAD_PERCENTAGE)) * populationSize / 100.0f);
    }catch (Exception e) {System.out.println("[Seeding] No RELOAD_KEYWORD found. Skipping seeding.");}
    
    List<String> solutionLines = new ArrayList<String>(); //Pareto set file content
    
	if (populationSize2seed>0) {
    	// get previous Pareto set solutions
    	solutionLines = getPreviousSavedPopulation();
    	
        // leave only the first populationSize2seed num of solutions (other possible functions: random, best, worst, etc. Not currently implemented)
    	solutionLines = getFirstNSolutions(solutionLines, populationSize2seed);
    	
    	System.out.println("[Seeding] Seeding porcentage define at "+ String.valueOf(Utility.getProperty(Constants.RELOAD_PERCENTAGE))+ "%. "
    			+ (solutionLines.isEmpty() ? "No seedable solutions found." : "Seeding "+String.valueOf(solutionLines.size())+" feasible solutions found out of "
    					+String.valueOf(populationSize)+" total population size.") );
	}
	else System.out.println("[Seeding] Population to seed is 0. Skipping seeding.");
	
	
	// generate solution set
    for (int i = 0; i < populationSize; i++) {
      // create new solution
      newSolution = new Solution(problem_);
      
      // replace from previous population
      if (!solutionLines.isEmpty()) {
    	  String line = solutionLines.remove(0);
    	  // parse line
	      String[] sol_str_list = line.trim().split("\\s"); 
	      // get random generated solution (some are reals, others integers)
    	  ArrayReal real_arr=(ArrayReal) newSolution.getDecisionVariables()[0];
          ArrayInt  int_arr=(ArrayInt) newSolution.getDecisionVariables()[1];
          
          //--Sanity check
          //System.out.println("[] Solution before ('randomly' generated):");
          //System.out.println(newSolution.getDecisionVariables()[0]);
          //System.out.println(newSolution.getDecisionVariables()[1]);
//          System.out.println("[] Solution from file:");
//          System.out.println(line);
          //--
          
          // replace decision variables one by one (real or int)
          int count_int=0;
		  int count_real=0;
		  for(String sol: sol_str_list) {
			  try {
		            Integer.parseInt(sol);
		            int val=Integer.parseInt(sol);
		            int_arr.setValue(count_int, val);
		            count_int+=1;
				  
		        } catch (NumberFormatException e) {
		        	double val=Double.parseDouble(sol);
  					real_arr.setValue(count_real, val);
  					count_real+=1;
		        }
		  }//for
        }//if
      
      // --Sanity check on update happening
//      System.out.println("[] Solution after (replaced if no NaN exist):");
//      System.out.println(newSolution.getDecisionVariables()[0]);
//      System.out.println(newSolution.getDecisionVariables()[1]);
      //--
      
      // add solution to parallel evaluation
      parallelEvaluator_.addSolutionForEvaluation(newSolution) ;
      
    }//for
    
    
    List<Solution> solutionList = parallelEvaluator_.parallelEvaluation() ;
    for (Solution solution : solutionList) {
      population.add(solution) ;
      evaluations ++ ;
    }
    
    int times = 1;
    
    // Generations 
    while (evaluations < maxEvaluations) {
    	System.out.println("Evaluations:\t" + evaluations);
    	    	
      // Create the offSpring solutionSet      
      offspringPopulation = new SolutionSet(populationSize);
      Solution[] parents = new Solution[2];
      for (int i = 0; i < (populationSize / 2); i++) {
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

      List<Solution> solutions = parallelEvaluator_.parallelEvaluation() ;

      for(Solution solution : solutions) {
        offspringPopulation.add(solution);
        evaluations++;	    
      }

      // Create the solutionSet union of solutionSet and offSpring
      union = ((SolutionSet) population).union(offspringPopulation);

      // Ranking the union
      Ranking ranking = new Ranking(union);

      int remain = populationSize;
      int index = 0;
      SolutionSet front = null;
      population.clear();

      // Obtain the next front
      front = ranking.getSubfront(index);

      while ((remain > 0) && (remain >= front.size())) {
        //Assign crowding distance to individuals
        distance.crowdingDistanceAssignment(front, problem_.getNumberOfObjectives());
        //Add the individuals of this front
        for (int k = 0; k < front.size(); k++) {
          population.add(front.get(k));
        } // for

        //Decrement remain
        remain = remain - front.size();

        //Obtain the next front
        index++;
        if (remain > 0) {
          front = ranking.getSubfront(index);
        } // if        
      } // while

      // Remain is less than front(index).size, insert only the best one
      if (remain > 0) {  // front contains individuals to insert                        
        distance.crowdingDistanceAssignment(front, problem_.getNumberOfObjectives());
        front.sort(new CrowdingComparator());
        for (int k = 0; k < remain; k++) {
          population.add(front.get(k));
        } // for

        remain = 0;
      } // if                               

      // This piece of code shows how to use the indicator object into the code
      // of NSGA-II. In particular, it finds the number of evaluations required
      // by the algorithm to obtain a Pareto front with a hypervolume higher
      // than the hypervolume of the true Pareto front.
      if ((indicators != null) &&
          (requiredEvaluations == 0)) {
        double HV = indicators.getHypervolume(population);
        if (HV >= (0.98 * indicators.getTrueParetoFrontHypervolume())) {
          requiredEvaluations = evaluations;
        } // if
      }// if
      
      
      
//	  	//Save the pareto set every 25% evaluations
//	  	if (maxEvaluations*times/4 <= evaluations){
//	  		System.out.println("Saving Pareto set");
//	  		Ranking rank = new Ranking(population);
//	  		SolutionSet paretoSet = rank.getSubfront(0);
//	  		paretoSet.printVariablesToFile("data/VAR_NSGAII"+times);
//	  		paretoSet.printObjectivesToFile("data/FUN_NSGAII"+times);
//	  		times ++;
//	  	}
      
    } // while

    parallelEvaluator_.stopEvaluator();

    // Return as output parameter the required evaluations
    setOutputParameter("evaluations", requiredEvaluations);

    // Return the first non-dominated front
    Ranking ranking = new Ranking(population);
    return ranking.getSubfront(0);
  } // execute




} // pNSGAII
