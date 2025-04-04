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
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import evochecker.auxiliary.Constants;
import evochecker.auxiliary.Utility;
import evochecker.seeding.auxiliary.ParetoPoint;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.util.JMException;


/**
 * EvoChecker class
 * @author gricelvazquez
 * April 2025
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
	    	System.out.println("[Seeding] Seeding porcentage define at "+ reloadPercentage + "%. Searching for "+seedingNumSolutions+" solutions");
	    } catch (Exception e) {
	    	System.out.println("[Seeding] No RELOAD_KEYWORD found. Skipping seeding.");
	    	return new ArrayList<Solution>();
	    }
	    
    	
    	//--- Get previous solutions from file
	    List<ParetoPoint> prevSolutions =  getPreviousSolutions(problem_);
	    if (prevSolutions.isEmpty()) {
	    	System.out.println("[Seeding] No seedable solutions found.");
	    	return new ArrayList<Solution>();
	    }
	    if (prevSolutions.size()<= seedingNumSolutions) { // if less reusable solutions than wanted
	    	System.out.println("[Seeding] Seeding "+ prevSolutions.size() +" reusable solutions");
	    	return getSolutions2Seed(prevSolutions);
	    }
	    
	    //--- Get the seeding strategy
	    String seedType = Utility.getProperty(Constants.SEED_TYPE).toUpperCase();
	    ISeeding seedStrategy = null;
	    
	    // - random seeding
	    if (seedType.equals(Constants.SEED.RANDOM.toString()))
	    	seedStrategy = new Random();
	    // - kmeans++ seeding
	    else if (seedType.equals(Constants.SEED.KMEANS.toString()))
	    	seedStrategy = new KMeansPlusPlus();
    	// - dbscan seeding
		else if (seedType.equals(Constants.SEED.DBSCAN.toString()))
			seedStrategy = new DBSCAN();
	    
	    
	    // TODO
	    // - Agglomerative clustering
	    // - DBSCAN clustering
	    // - Hierarchical clustering (also called hierarchical cluster analysis or HCA) 
    	else { // error
    		System.err.println("[Seeding] Invalid SEED_TYPE: " + seedType);
    		System.exit(0);
    	}
	    
	    //--- Seed the population
	    seedStrategy.setParameters();
	    List<ParetoPoint> solutions2Seed = seedStrategy.getNSolutions(prevSolutions, seedingNumSolutions);
	    
	    System.out.println("[Seeding] SEED_TYPE: "+ seedType);
	    System.out.println("[Seeding] Seeding "+solutions2Seed.size() + " solutions.");
	    
		return getSolutions2Seed(solutions2Seed);
	}
	
	
	
	/**
	 * Retrieve JMetal solutions from ParetoPoint objects
	 * @param solutions2Seed
	 * @return Solution list
	 */
	private static List<Solution> getSolutions2Seed(List<ParetoPoint> solutions2Seed) {
		List<Solution> solutions = new ArrayList<Solution>();
		for (ParetoPoint point: solutions2Seed) {
			solutions.add(point.getSolution());
		}
		return solutions;
	}
	
	

	/**
	* This reads a previous Pareto set or front solutions file. It returns all usable solution lines as strings.
	* Usable solutions are those that do not contain NaN values.
    * Solutions are read from the NSGAII folder. The most recent file is read.
    * @param setFront the type of file to read (either "Set" or "Front")
    * @return list of strings representing solutions read from previous Pareto set file
    * 
    */
	private static List<String> readPreviousPopulationFile(String setFront) {
	   // Construct the path to the Pareto set directory
	   String baseDirectory = "./data/" + Utility.getProperty(Constants.PROBLEM_KEYWORD) + "/NSGAII/";
	   File paretoSetDirectory = new File(baseDirectory);
	   String[] existingFiles = paretoSetDirectory.list();
	   // Find the most recently modified file
	   long latestModifiedTime = 0;
	   String latestParetoSetFile = "";
	   List<String> solutionLines = new ArrayList<>();  // Holds the lines of the Pareto set file
	   if (existingFiles == null) {
		   return solutionLines;
	   }
       for (String fileName : existingFiles) {
    	   // check for files which names end in "Set"/"Front"
           if (fileName.split("_")[fileName.split("_").length-1].equals(setFront)) {
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
           // Remove the first line (header)
           solutionLines.remove(0);
           //remove the empty lines
           solutionLines.removeIf(String::isEmpty);
           // Remove lines containing "NaN"
           solutionLines.removeIf(line -> line.contains("NaN"));
           System.out.println("[Seeding] Reading previous Pareto "+ setFront+": " + latestParetoSetFile + " with " + solutionLines.size() + " reusable solutions.");
       }
       return solutionLines;
   }
   
	

	
   
	/**
	* This method gets solutions read from a previous Pareto set and front file.
	* It returns a list of ParetoPoint objects.
	* @param problem_ the problem
	* @return list of solutions to seed
	* @throws ClassNotFoundException
	* @throws JMException
	*/
    private static ArrayList<ParetoPoint> getPreviousSolutions(Problem problem_) throws ClassNotFoundException, JMException {
	   // Read previous Pareto sol files
	   List<String> solutionLines_set =  readPreviousPopulationFile("Set");
	   List<String> solutionLines_front =  readPreviousPopulationFile("Front");
	   
	   ArrayList<ParetoPoint> points = new ArrayList<ParetoPoint>();
	   
	   // Convert the list of strings to a list of Point objects
	   for (int i = 0; i < solutionLines_set.size(); i++) {
		   // Parse previous solution
	       String[] set_str_list = solutionLines_set.get(i).trim().split("\\s");
           String[] front_str_list = solutionLines_front.get(i).trim().split("\\s");
		   
           // create point object
           List<Double> setValuesD = new ArrayList<Double>(); 
   		   List<Integer> setValuesI = new ArrayList<Integer>();
   		   List<Double> frontValues = new ArrayList<Double>();
   		   
   		   for (String  sol: set_str_list) {
   			   try {
   				   int val=Integer.parseInt(sol);
   				   setValuesI.add(val);
   			   } catch (NumberFormatException e) {
   				   double val=Double.parseDouble(sol);
   				   setValuesD.add(val);
   			   }
   		   }
   		   for (String sol: front_str_list) {
			   double val=Double.parseDouble(sol);
			   frontValues.add(val);
   		   }
   		   points.add(new ParetoPoint(setValuesD, setValuesI, frontValues, problem_));
	   }
	return points;
	}
	   
	   
}