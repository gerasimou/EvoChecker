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
public class Seeding {
	
	
	public static List<Solution> getSeededSolutions(Problem problem_, int populationSize) throws ClassNotFoundException, JMException {
		
	    //--- Get the number of solutions to seed
	    Integer seedingNumSolutions = 0;
	    try {
	    	String reloadPercentage = Utility.getProperty(Constants.SEED_PERCENTAGE);
	    	seedingNumSolutions = reloadPercentage.isEmpty() ? 0 : Math.round( Integer.parseInt(Utility.getProperty(Constants.SEED_PERCENTAGE)) * populationSize / 100.0f);
	    	if (seedingNumSolutions == 0) {
	    		System.out.println("[Seeding] Population to seed is 0. Skipping seeding.");
	    		return new ArrayList<Solution>();
	    	}
	    	System.out.println("[Seeding] Seeding porcentage define at "+ reloadPercentage + "%. ");
	    } catch (Exception e) {
	    	System.out.println("[Seeding] No RELOAD_KEYWORD found. Skipping seeding.");
	    	return new ArrayList<Solution>();
	    }
	    
    	// Check if the seed type is valid
//	    String seedType = ;
//	    if (!seedType.equals(Constants.SEED.RANDOM.toString()) && 
//	    	!seedType.equals(Constants.SEED.KMEANS.toString()) && 
//	    	!seedType.equals(Constants.SEED.PSC.toString())) {
//    		System.err.println("[Seeding] Invalid SEED_TYPE: " + seedType);
//    		System.exit(0);
//    	}
    	
    	//--- Get previous solutions from file
	    List<Solution> prevSolutions =  getPreviousSolutionSet(problem_);
	    if (prevSolutions.isEmpty()) {
	    	System.out.println("[Seeding] No seedable solutions found.");
	    	return new ArrayList<Solution>();
	    }
	    	    	    
	    //--- Seed the population
	    List<Solution> solutions2Seed = new ArrayList<Solution>();
	    String seedType = Utility.getProperty(Constants.SEED_TYPE).toUpperCase();
	    // - random seeding
	    if (seedType.equals(Constants.SEED.RANDOM.toString())) {
	    	System.out.println("[Seeding] SEED_TYPE: RANDOM");
//	    	solutionLines = getFirstNSolutions(solutionLines, seedingNumSolutions);
			solutions2Seed = Seeding.getRandomNSolutions(prevSolutions, seedingNumSolutions);
    	}
	    // - kmeans seeding
	    else if (seedType.equals(Constants.SEED.KMEANS.toString())) {
	    	System.out.println("[Seeding] SEED_TYPE: KMEANS");
			solutions2Seed = Seeding.getKmeansNSolutions(prevSolutions, seedingNumSolutions);
    	}
	    // - PSC seeding
    	else if (seedType.equals(Constants.SEED.PSC.toString())) { //Pareto Simplicial Complex (PSC)
    		System.out.println("[Seeding] SEED_TYPE: PSC");
    		solutions2Seed = Seeding.getPSCNSolutions(prevSolutions, seedingNumSolutions);
    	}
    	else { // error
    		System.err.println("[Seeding] Invalid SEED_TYPE: " + seedType);
    		System.exit(0);
    	}
	    
		return solutions2Seed;
	}
	
	/**
	 * This method returns the first N solutions randomly from previous solutions.
	 * @param prevSolutions
	 * @param seedingNumSolutions
	 * @return
	 */
	private static List<Solution> getRandomNSolutions(List<Solution> prevSolutions, Integer seedingNumSolutions) {
		return prevSolutions.subList(0, Math.min(prevSolutions.size(), seedingNumSolutions));
	}
	
	/**
	 * This method returns the first N solutions from previous solutions using Kmeans clustering.
	 * @param prevSolutions
	 * @param seedingNumSolutions
	 * @return
	 */
	private static List<Solution> getKmeansNSolutions(List<Solution> prevSolutions, Integer seedingNumSolutions) {
		// TODO: implement Kmeans clustering
		return new ArrayList<Solution>();
	}
	
	/**
	 * This method returns the first N solutions from previous solutions using PSC.
	 * @param prevSolutions
	 * @param seedingNumSolutions
	 * @return
	 */
	 private static List<Solution> getPSCNSolutions(List<Solution> prevSolutions, Integer seedingNumSolutions) {
		 // TODO: implement PSC
	 	 return new ArrayList<Solution>();
	 }
	
	
   
	
	/**
	* This reads a previous Pareto set solutions file. It returns all usable solution lines as strings.
	* Usable solutions are those that do not contain NaN values.
    * Solutions are read from the NSGAII folder. The most recent file is read.
    * @return list of strings representing solutions read from previous Pareto set file
    * 
    */
	private static List<String> readPreviousPopulationSetFile() {
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
	* This method gets solutions read from a previous Pareto set file.
	* @param problem_ the problem
	* @return list of solutions to seed
	* @throws ClassNotFoundException
	* @throws JMException
	*/
    private static List<Solution> getPreviousSolutionSet(Problem problem_) throws ClassNotFoundException, JMException {
	   List<Solution> prevSolutions = new ArrayList<Solution>();
	   // Read previous Pareto set file
	   List<String> solutionLines =  readPreviousPopulationSetFile();
	   // Convert the list of strings to a list of solutions
	   for (String line : solutionLines) {
		   // Create new random solution
		   Solution newSolution = new Solution(problem_); 
	       // get data (some decision variables are reals, others integers)
    	   ArrayReal real_arr=(ArrayReal) newSolution.getDecisionVariables()[0];
           ArrayInt  int_arr=(ArrayInt) newSolution.getDecisionVariables()[1];
           // Parse previous solution
	       String[] sol_str_list = line.trim().split("\\s");
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
	   
	   
}