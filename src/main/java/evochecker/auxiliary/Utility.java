package evochecker.auxiliary;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import evochecker.exception.EvoCheckerException;
import evochecker.properties.Property;
import jmetal.core.Solution;
import jmetal.util.Configuration;

public class Utility {
	
	private static String fileName;
	private static Properties properties;
	
	public static void setPropertiesFile (String filename) {
		fileName = filename;
	}
	
	private static void loadPropertiesInstance(){
		try {
			if (properties == null){
				properties = new Properties();
				properties.load(new FileInputStream(fileName));
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static String getProperty (String key){
		loadPropertiesInstance();
		String result = properties.getProperty(key).strip(); 
		if (result == null)
			  throw new IllegalArgumentException(key.toUpperCase() + " name not found!");
		return result;		
	}
	
	
	public static String getProperty (String key, String defaultValue){
		loadPropertiesInstance();
		String output = properties.getProperty(key);
		return (output != null ? output : defaultValue);
	}
	
	
	public static StringProperties getAllProperties() {
		return new StringProperties(properties);
	}

	
	public static void setProperty (String key, String value) throws EvoCheckerException{
		loadPropertiesInstance();
		if (properties.setProperty(key, value) ==null)
			throw new EvoCheckerException("Key: " + key + " does not exist!");
	}
	
	
	public static String getTimeStamp() {
		Date date = Calendar.getInstance().getTime();
	    SimpleDateFormat sdf = new SimpleDateFormat("HHmmss_ddMMyy");
	    return sdf.format(date);	
    }
	
	
	  /** 
	   * Writes the objective function values of the <code>Solution</code> 
	   * objects into the set in a file.
	   * @param path The output file name
	   */
	  public static void printObjectivesToFile(String path, List<Solution> solutions, List<Property> objectivesList){
	    try {
	      /* Open the file */
	      FileOutputStream fos   = new FileOutputStream(path, true)     ;
	      OutputStreamWriter osw = new OutputStreamWriter(fos)    ;
	      BufferedWriter bw      = new BufferedWriter(osw)        ;

	      int numOfObjectives = objectivesList.size();
	      for (Solution solution : solutions) {
	    	  StringBuilder objString = new StringBuilder();
	    	    for (int i = 0; i < numOfObjectives; i++) {
	    	    		if (objectivesList.get(i).isMaximization())
	    	    			objString.append(-(solution.getObjective(i)));
	    	    		else
	    	    			objString.append(solution.getObjective(i));
	    	    		if (i<numOfObjectives-1)
	    	    			objString.append("\t");
	    	    }
	        bw.write(objString.toString());	    	  
	        bw.newLine();
	        //}
	      }
	      /* Close the file */
	      bw.close();
	    }catch (IOException e) {
	      Configuration.logger_.severe("Error acceding to the file");
	      e.printStackTrace();
	    }
	  } // printObjectivesToFile

	  
	  /**
	   * Writes the decision encodings.variable values of the <code>Solution</code>
	   * solutions objects into the set in a file.
	   * @param path The output file name
	   */
	  public static void printVariablesToFile(String path, List<Solution> solutions){
	    try {
	      FileOutputStream fos   = new FileOutputStream(path, true)     ;
	      OutputStreamWriter osw = new OutputStreamWriter(fos)    ;
	      BufferedWriter bw      = new BufferedWriter(osw)        ;            

	      int numberOfVariables = solutions.get(0).getDecisionVariables().length ;
	      for (Solution aSolutionsList_ : solutions) {
	    	  	for (int j = 0; j < numberOfVariables; j++)
	    	  		bw.write(aSolutionsList_.getDecisionVariables()[j].toString() + "\t");
	    	  	bw.newLine();
	      }
	      bw.close();
	    }
	    	catch (IOException e) {
	    		Configuration.logger_.severe("Error acceding to the file");
	    		e.printStackTrace();
	    	}       
	  } // printVariablesToFile
	
}
