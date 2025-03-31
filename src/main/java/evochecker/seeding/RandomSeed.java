//==============================================================================
//	
 //	Copyright (c) 2015-
//	Authors:
//	* Simos Gerasimou (University of York)
//  * Faisal Alhwikem (University of York)
//	
//------------------------------------------------------------------------------
//	
//	This file is part of EvoChecker.
//	
//==============================================================================
package evochecker.seeding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import evochecker.auxiliary.ConfigurationChecker;
import evochecker.auxiliary.Constants;
import evochecker.auxiliary.FileUtil;
import evochecker.auxiliary.Utility;
import evochecker.evaluator.IParallelEvaluator;
import evochecker.exception.EvoCheckerException;
import evochecker.genetic.GenotypeFactory;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.jmetal.encoding.ArrayInt;
import evochecker.genetic.jmetal.encoding.ArrayReal;
import evochecker.genetic.jmetal.metaheuristics.settings.MOCell_Settings;
import evochecker.genetic.jmetal.metaheuristics.settings.NSGAII_Settings;
import evochecker.genetic.jmetal.metaheuristics.settings.RandomSearch_Settings;
import evochecker.genetic.jmetal.metaheuristics.settings.SPEA2_Settings;
import evochecker.genetic.problem.GeneticModelProblem;
import evochecker.genetic.problem.GeneticProblem;
import evochecker.genetic.problem.GeneticProblemParametric;
import evochecker.genetic.problem.GeneticProblemParametricParallel;
import evochecker.language.parser.IModelInstantiator;
import evochecker.language.parser.ModelInstantiator;
import evochecker.language.parser.ModelInstantiatorParametric;
import evochecker.plotting.PlotFactory;
import evochecker.properties.Property;
import evochecker.properties.PropertyFactory;
import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;


/**
 * Main EvoChecker class
 * @author gricelvazquez
 *
 */
public class RandomSeed {
	

	/**
	* This method returns the first N solutions from the list of solutions.
	* If the list is smaller than N, the whole list is returned.
	* @param solutionLines list of solutions
	* @param populationSize2seed number of solutions to return
	* @return list of solutions
	*/
	private static List<String> getFirstNSolutions(List<String> solutionLines, int populationSize2seed) {
		return solutionLines.subList(0, Math.min(solutionLines.size(), populationSize2seed));
	}
   
	
	/**
	* This reads a previous Pareto set solutions file. It returns all usable solution lines as strings.
	* Usable solutions are those that do not contain NaN values.
    * Solutions are read from the NSGAII folder. The most recent file is read.
    * @return list of strings representing solutions read from previous Pareto set file
    * 
    */
	private static List<String> getPreviousSavedPopulation() {
	   // Construct the path to the Pareto set directory
	   String baseDirectory = "./data/" + Utility.getProperty(Constants.PROBLEM_KEYWORD) + "/NSGAII/";
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
    	   // check for files which names contain "Set"
           if (fileName.split("_")[fileName.split("_").length-1].equals("Set")) {
               File file = new File(baseDirectory + fileName);
               long lastModified = file.lastModified();
               // save the most recent file
               if (lastModified > latestModifiedTime) {
                   latestParetoSetFile = fileName;
                   latestModifiedTime = lastModified;
               }
           }
       }
       // If file is found, read its contents
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
           System.out.println("[Seeding] Reading previous Pareto Set: " + latestParetoSetFile + " with " + solutionLines.size() + " reusable solutions.");
       }
       return solutionLines;
   }
   
   
	   /**
	* This method seeds the population with solutions read from a previous Pareto set file.
	* @param seedingNumSolutions number of solutions to seed
	* @param problem_ the problem
	* @param population the population
	* @param populationSize the population size
	* @param reloadPercentage the percentage of the population to seed
	* @return the number of solutions seeded
	* @throws ClassNotFoundException
	* @throws JMException
	*/
    public static List<Solution> seedRandomSolutions(Integer seedingNumSolutions, Problem problem_,
			SolutionSet population, int populationSize, String reloadPercentage) throws ClassNotFoundException, JMException {
    	
    	if (seedingNumSolutions == 0) {
			System.out.println("[Seeding] Population to seed is 0. Skipping seeding.");
			return new ArrayList<Solution>();
		}
    	
    	
	   System.out.println("[Seeding] Seeding porcentage define at "+ reloadPercentage + "%. ");
   	   
	   List<Solution> prevSolutions = new ArrayList<Solution>(); //Pareto set file content
	   
	   // Get previous Pareto set solutions
	   List<String> solutionLines =  getPreviousSavedPopulation();
	   // leave only the first N previous solutions (other possible functions: random, best, more spread, etc. Not currently implemented)
	   solutionLines = getFirstNSolutions(solutionLines, seedingNumSolutions);
	   
	   // print
	   System.out.println((solutionLines.isEmpty() ? "[Seeding] No seedable solutions found." 
			   : "[Seeding] Seeding "+String.valueOf(solutionLines.size())+" solutions (found out of "
    				+String.valueOf(seedingNumSolutions)+" expected solutions to seed).") );
	   
	   // For each solution in solutionLines, create a new solution and add it to the population
	   for (String line : solutionLines) {
		   // Create new random solution
		   Solution newSolution = new Solution(problem_); 
	       // get data (some decision variables are reals, others integers)
    	   ArrayReal real_arr=(ArrayReal) newSolution.getDecisionVariables()[0];
           ArrayInt  int_arr=(ArrayInt) newSolution.getDecisionVariables()[1];

		   // Parse previous solution
	       String[] sol_str_list = line.trim().split("\\s");
           
          //--Sanity check
          //System.out.println("[] Solution before ('randomly' generated):");
          //System.out.println(newSolution.getDecisionVariables()[0]);
          //System.out.println(newSolution.getDecisionVariables()[1]);
		  //System.out.println("[] Solution from file:");
		  //System.out.println(line);
          //--
          
           // Replace random solution with data from previous, one by one value (reals and ints)
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
		    
		   prevSolutions.add(newSolution);   
	   }//for
	   return prevSolutions;
	}
   
//   public void seedRandomSolutions(int populationSize2seed) {
//	   // a) check if reloading from previous population
//	    int populationSize2seed = 0;
//	    try { populationSize2seed = ;
//	    }catch (Exception e) {System.out.println("[Seeding] No RELOAD_KEYWORD found. Skipping seeding.");}
//	    
//	    List<String> solutionLines = new ArrayList<String>(); //Pareto set file content
//	    
//		if (populationSize2seed>0) {
//	    	// get previous Pareto set solutions
//	    	solutionLines =  getPreviousSavedPopulation();
//	    	
//	        // leave only the first populationSize2seed num of solutions (other possible functions: random, best, worst, etc. Not currently implemented)
//	    	solutionLines = getFirstNSolutions(solutionLines, populationSize2seed);
//	    	
//	    	System.out.println("[Seeding] Seeding porcentage define at "+ String.valueOf(Utility.getProperty(Constants.RELOAD_PERCENTAGE))+ "%. "
//	    			+ (solutionLines.isEmpty() ? "No seedable solutions found." : "Seeding "+String.valueOf(solutionLines.size())+" feasible solutions found out of "
//	    					+String.valueOf(populationSize)+" total population size.") );
//		}
//		else System.out.println("[Seeding] Population to seed is 0. Skipping seeding.");
//		
//		
//		// generate solution set
//	    for (int i = 0; i < populationSize; i++) {
//	      // create new solution
//	      newSolution = new Solution(problem_);
//	      
//	      // replace from previous population
//	      if (!solutionLines.isEmpty()) {
//	    	  String line = solutionLines.remove(0);
//	    	  // parse line
//		      String[] sol_str_list = line.trim().split("\\s"); 
//		      // get random generated solution (some are reals, others integers)
//	    	  ArrayReal real_arr=(ArrayReal) newSolution.getDecisionVariables()[0];
//	          ArrayInt  int_arr=(ArrayInt) newSolution.getDecisionVariables()[1];
//	          
//	          //--Sanity check
//	          //System.out.println("[] Solution before ('randomly' generated):");
//	          //System.out.println(newSolution.getDecisionVariables()[0]);
//	          //System.out.println(newSolution.getDecisionVariables()[1]);
////		          System.out.println("[] Solution from file:");
////		          System.out.println(line);
//	          //--
//	          
//	          // replace decision variables one by one (real or int)
//	          int count_int=0;
//			  int count_real=0;
//			  for(String sol: sol_str_list) {
//				  try {
//			            Integer.parseInt(sol);
//			            int val=Integer.parseInt(sol);
//			            int_arr.setValue(count_int, val);
//			            count_int+=1;
//					  
//			        } catch (NumberFormatException e) {
//			        	double val=Double.parseDouble(sol);
//	  					real_arr.setValue(count_real, val);
//	  					count_real+=1;
//			        }
//			  }//for
//	        }//if
//	      
//	      // --Sanity check on update happening
////		      System.out.println("[] Solution after (replaced if no NaN exist):");
////		      System.out.println(newSolution.getDecisionVariables()[0]);
////		      System.out.println(newSolution.getDecisionVariables()[1]);
//	      //--
//	      
//	      // add solution to parallel evaluation
//	      parallelEvaluator_.addSolutionForEvaluation(newSolution) ;
//	      
//	    }//for
//	    
//	    
//	    List<Solution> solutionList = parallelEvaluator_.parallelEvaluation() ;
//	    for (Solution solution : solutionList) {
//	      population.add(solution) ;
//	      evaluations ++ ;
//	    }
//	    
//	    int times = 1;
//   }




	   
	   
	   
}
