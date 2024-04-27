package evochecker.auxiliary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import evochecker.evolvables.Evolvable;
import evochecker.evolvables.EvolvableOption;
import evochecker.exception.EvoCheckerException;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.genes.IntegerGene;
import evochecker.genetic.jmetal.encoding.ArrayInt;
import evochecker.properties.Property;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.util.Configuration;
import jmetal.util.JMException;

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
		String result = getPropertyIgnoreNull (key); 
		if (result == null)
			  throw new IllegalArgumentException(key.toUpperCase() + " name not found!");
		return result;		
	}
	
	
	public static String getPropertyIgnoreNull (String key){
		loadPropertiesInstance();
		String result = properties.getProperty(key).strip(); 
		return result;				
	}
	
	
	public static String getProperty (String key, String defaultValue){
		loadPropertiesInstance();
		String output = properties.getProperty(key);
		return (output != null ? output.trim() : defaultValue.trim());
	}
	
	
	public static StringProperties getAllProperties() {
		return new StringProperties(properties);
	}

	
	public static void setProperty (String key, String value) throws EvoCheckerException{
		loadPropertiesInstance();
		properties.setProperty(key, value);
	}
	
	
	public static String getTimeStamp() {
		Date date = Calendar.getInstance().getTime();
	    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy_HHmmss");
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
	    	  	for (int j = 0; j < numberOfVariables; j++) {
	    	  		Variable v = aSolutionsList_.getDecisionVariables()[j];
	    	  		bw.write(v.toString() + "\t");
	    	  	}
	    	  	bw.newLine();
	      }
	      bw.close();
	    }
	    	catch (IOException e) {
	    		Configuration.logger_.severe("Error acceding to the file");
	    		e.printStackTrace();
	    	}       
	  } // printVariablesToFile
	  
	  
	  /**
	   * Writes the decision encodings.variable values of the <code>Solution</code>
	   * solutions objects into the set in a file.
	   * @param path The output file name
	   */
	  public static void printVariablesToFile2(String path, List<Solution> solutions, Map<AbstractGene, Evolvable> elementsMap, List<AbstractGene> genes){
		  List<Integer> indexes = new ArrayList<>();
		  List<EvolvableOption> evolvableOptionsList = new ArrayList<EvolvableOption>(); 
		  int i = -1;
		  for (AbstractGene g : genes) {
			  if (g instanceof IntegerGene){ 
				 i++;
				 if (elementsMap.get(g) instanceof EvolvableOption){
					 indexes.add(i);
					 evolvableOptionsList.add((EvolvableOption)elementsMap.get(g));
				 }				 
			  } 
		  }
		  
	    try {
	    	FileOutputStream fos   = new FileOutputStream(path, true);
	    	OutputStreamWriter osw = new OutputStreamWriter(fos);
	    	BufferedWriter bw      = new BufferedWriter(osw);            
	      
	    	for (Solution aSolutionsList_ : solutions) {
	    		int numOfIntVariables = ((ArrayInt)aSolutionsList_.getDecisionVariables()[1]).getLength();
	    		int k = 0;
    		  	for (int j=0; j<numOfIntVariables; j++) {
    		  		int value = ((ArrayInt)aSolutionsList_.getDecisionVariables()[1]).getValue(j);
    		  		if (indexes.contains(j)) {
    		  			bw.write(evolvableOptionsList.get(k++).getOption(value) +" ");
    		  		}
    		  		else
    		  			bw.write(value +" ");    		  			
    		  	}
    		  	bw.write(aSolutionsList_.getDecisionVariables()[0].toString());
	    	  	bw.newLine();
	    	}
	      bw.close();
	    }
    	catch (IOException | JMException e) {
	    		Configuration.logger_.severe("Error acceding to the file");
	    		e.printStackTrace();
	    	}       
	  } // printVariablesToFile
	
	  
	  public static String findJavaPath() {
		  try {
			String bashCommand = null;
			
			String os = getOperatingSystem();
			
			if (os.contains("mac")) {
				bashCommand = "/usr/libexec/java_home";
				return bashInvoker(bashCommand)+"/bin/java";
			}
			else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
				bashCommand = "which java";
				return bashInvoker(bashCommand);
			}
			else
				throw new EvoCheckerException("EvoChecker currently supports only OSX and Unix.");
				
		  } 
		  catch (EvoCheckerException e) {
			  e.printStackTrace();
		  }
		  return null;
	  }	
	  
	  
	  public static String findAvailablePort (int initPort) {
		  String result; 
		  do {
			  String bashCommand = "lsof -i:" + initPort++;
			  result = bashInvoker(bashCommand);			 
		  }
		  while (result != null);
			 return --initPort +"";
	  }
	  
	  
	  public static String findPython3Path () {
		  String bashCommand = "which python";
		  String out = bashInvoker(bashCommand); 
		  return out;			 
	  }
	  
	  
	  public static String runtimeLibsDirSpecified() throws EvoCheckerException {
		  String os = getOperatingSystem();
		  String variable = null;
		  
		  if (os.contains("mac")) {
				variable = "DYLD_LIBRARY_PATH";
			}
			else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
				variable = "LD_LIBRARY_PATH";
			}
			else
				throw new EvoCheckerException("EvoChecker currently supports only OSX and Unix.");
		
		  if (System.console() == null) {//within an IDE
			  //System.out.println("IDE");
			  String out = System.getenv(variable);
			  if (out==null || !new File(out).isDirectory())
					return variable;			  
		  }
		  else {//within a terminal
			  //System.out.println("Terminal");
			  //assume that the script has been specified correctly
			  System.out.println("Running from terminal. Make sure you have set the environment variable " + variable +" in the launch script");
		  }
			
		 return null;
	  }
		
		
	  public static String getOperatingSystem() {
		  return System.getProperty("os.name").toLowerCase();
	  }
		
			
	  public static String bashInvoker(String command) {
		  try {
			  command = "export PATH=/usr/local/bin:$PATH; " + command;
			  ProcessBuilder pb = new ProcessBuilder();
			  pb.command("bash", "-c", command);
			  Process process = pb.start();
			  BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			  return reader.readLine(); 
		  } 
		  catch (IOException e) {
			e.printStackTrace();
		  }
		  return null;
	}
	
	  
	  
	 public static void main (String args[]) {
		 //System.out.println(Utility.bashInvoker("which java"));
//		 System.out.println(Utility.bashInvoker("which python3"));
//		 System.out.println(Utility.bashInvoker("which storm-pars"));
		 System.out.println(Utility.bashInvoker("echo $PATH"));
//		 System.out.println(Utility.bashInvoker("storm-pars"));
	 }
	  
}
