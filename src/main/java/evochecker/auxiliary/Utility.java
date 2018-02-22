package evochecker.auxiliary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
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
	
	private static String fileName = "resources/config.properties";
	private static Properties properties;
	
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
		String result = properties.getProperty(key); 
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
	
	
	public static void exportToFile(String fileName, String output){
		try {
			FileWriter writer = new FileWriter(fileName, true);
			writer.append(output +"\n");
			writer.flush();
			writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public static void createFileAndExport(String inputFileName, String outputFileName, String outputStr){
		FileChannel inputChannel 	= null;
		FileChannel outputChannel	= null;
				
		try {
			File input 	= new File(inputFileName);
			File output 	= new File(outputFileName);
			
			inputChannel 	= new FileInputStream(input).getChannel();
			outputChannel	= new FileOutputStream(output).getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());

			inputChannel.close();
			outputChannel.close();
			
			exportToFile(outputFileName, outputStr);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void createDir(String filePath) {
		File file = new File(filePath);
		if (!file.exists())
			file.mkdirs();
	}
	
	
	@SuppressWarnings("resource")
	public static String readFile(String fileName) {
		try {
			File f = new File(fileName);
			if (!f.exists() || f.isDirectory())
				throw new IOException("File does not exist! " + f );
		
			StringBuilder model = new StringBuilder(100);
			BufferedReader bfr = null;

			bfr = new BufferedReader(new FileReader(f));
			String line = null;
			while ((line = bfr.readLine()) != null) {
				model.append(line + "\n");
			}
			model.delete(model.length() - 1, model.length());
			return model.toString();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return null;
	}
	
	
	public static String getTimeStamp() {
		Date date = Calendar.getInstance().getTime();
	    SimpleDateFormat sdf = new SimpleDateFormat("hhmmss_ddMMyy");
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
	    	    			objString.append((solution.getObjective(i)*-1) + " ");
	    	    		else
	    	    			objString.append(solution.getObjective(i) +" ");
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
	    	  		bw.write(aSolutionsList_.getDecisionVariables()[j].toString() + " ");
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
