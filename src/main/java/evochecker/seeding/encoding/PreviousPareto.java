package evochecker.seeding.encoding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import evochecker.auxiliary.Constants;
import evochecker.auxiliary.Utility;
import evochecker.seeding.Seeding;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.util.JMException;


/**
 * EvoChecker class
 * @author gricelvazquez
 * April 2025
 */
public class PreviousPareto {
	private List<ParetoPoint> prevSolutions; // list of Pareto point solutions
	private List<Double> minVals; // min values across Pareto solutions
	private List<Double> maxVals; // max values across Pareto solutions
	private HashMap<String, String> paretoFrontSetPath = new HashMap<>(); //keys: "Set"/"Front", values: path to the Pareto set/front file
    
    public PreviousPareto(Problem problem_, String clusterFromPareto) throws JMException, ClassNotFoundException {
        this.prevSolutions = readPreviousSolutions(problem_, clusterFromPareto);
    	this.setMinFrontVals(); // set min and max values across Pareto solutions
    }
    
    
    public List<ParetoPoint> getPrevSolutions() {
    	if (this.prevSolutions == null) {
			System.out.println("[PreviousPareto] Error. reading previous Pareto solutions when none found.");
			System.exit(0);
		} else {
			System.out.println("[PreviousPareto] Found " + this.prevSolutions.size() + " previous Pareto solutions.");
		}
    	return this.prevSolutions;
    }
    
    
    
    /**
	* This method gets solutions read from a previous Pareto set and front file.
	* It returns a list of ParetoPoint objects.
	* @param problem_ the problem
	* @return list of solutions to seed
	* @throws ClassNotFoundException
	* @throws JMException
	*/
    private ArrayList<ParetoPoint> readPreviousSolutions(Problem problem_, String clusterFromPareto) throws ClassNotFoundException, JMException {
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
           List<Double> setValuesD = new ArrayList<Double>();   //doubles
   		   List<Integer> setValuesI = new ArrayList<Integer>(); //integers
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
   		   
   		   
   		   // add point to list
   		   points.add(new ParetoPoint(setValuesD, setValuesI, frontValues, problem_,clusterFromPareto));
	   }
	   
	return points;
	}
    
    
    
    
    
    
    
    /**
	* This reads a previous Pareto set or front solutions file. It returns all usable solution lines as strings.
	* Usable solutions are those that do not contain NaN values.
    * Solutions are read from the NSGAII folder. The most recent file is read.
    * @param setFront the type of file to read (either "Set" or "Front")
    * @return list of strings representing solutions read from previous Pareto set file
    * 
    */
	private List<String> readPreviousPopulationFile(String setFront) {
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
       
       // Store path
       if(!latestParetoSetFile.isEmpty()) 
    	   this.paretoFrontSetPath.put(setFront, latestParetoSetFile);
       
       return solutionLines;
   }
    
    
    
    
    
    
    
    
    // Min/max values across the Pareto solutions 
	// These values are used to normalize the solutions
	private List<Double> setMinFrontVals() {
		double smallest = Double.MAX_VALUE; //to search for smaller values
		double largest = -99.0;             //to search for larger values
		ArrayList<Double> minVals = new ArrayList<>(this.prevSolutions.size());
		ArrayList<Double> maxVals = new ArrayList<>(this.prevSolutions.size());
		for (int i = 0; i < this.prevSolutions.get(0).getAllVals().length; i++) { minVals.add(smallest); }
		for (int i = 0; i < this.prevSolutions.get(0).getAllVals().length; i++) { maxVals.add(largest); }
		
		double[] a = this.prevSolutions.get(0).getAllVals();
		
        //for each Pareto solution
		for (var solution : this.prevSolutions) {
		    double[] vals = solution.getAllVals();
		    for (int i = 0; i < vals.length; i++) {
		    	if (vals[i] < minVals.get(i)) {
		            minVals.set(i, vals[i]);
		        }
		        if (vals[i] > maxVals.get(i)) {
		            maxVals.set(i, vals[i]);
		        }
		    }
		}
		this.minVals = minVals;
		this.maxVals = maxVals;
		return null;
	}

	public List<Double> getMinVals() {
		return this.minVals;
	}
    
	public List<Double> getMaxVals() {
		return this.maxVals;
	}


	public double getMinVals(int i) {
		return this.minVals.get(i);
	}
	
	public double getMaxVals(int i) {
		return this.maxVals.get(i);
	}
}
