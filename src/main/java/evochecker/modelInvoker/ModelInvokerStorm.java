package evochecker.modelInvoker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import evochecker.auxiliary.FileUtil;
import evochecker.auxiliary.Utility;
import evochecker.properties.Objective;
import evochecker.properties.Property;

public class ModelInvokerStorm implements IModelInvoker {

	public File tempDir;
	public String OUTPUT = "storm.out";
	public String ERROR  = "storm.err";
	
	private String stormParsPath;
	
	List<String> resultsList;
	private Map<Integer, String> resultsMap;
	
	List<Property> properties;
	
	private Thread[] threads;
	
	
	
	public ModelInvokerStorm() {
		this(0);		
//		FileUtil.deleteDirectory(tempDir);
		tempDir.mkdirs();
		tempDir.deleteOnExit();		
	}
	
	
	private ModelInvokerStorm(int id) {
		tempDir = new File("temp");

		resultsList = new  CopyOnWriteArrayList<>();
		properties = new CopyOnWriteArrayList<>();
		
		stormParsPath = Utility.bashInvoker("which storm-pars");
		
		resultsMap = new ConcurrentHashMap<>();			

		ERROR  += id;
		OUTPUT += id;
	}
	
	
	@Override
	public IModelInvoker copy(int id) {
		return new ModelInvokerStorm(id);
	}
	
	
	@Override
	public List<String> invoke(String model, String propertyFile, List<Property> objectives, List<Property> constraints, PrintWriter out, BufferedReader in) throws IOException {
		ModelInvokerPrism prism = new ModelInvokerPrism();
		return prism.invoke(model, propertyFile, objectives, constraints, out, in);
//				prepareStorm(model, objectives, constraints, "/usr/local/bin/storm");		
	}


	@Override
	public List<String> invokeParam(String model, String propertyFile, List<Property> objectives, List<Property> constraints, PrintWriter out, BufferedReader in) throws IOException {
//		return prepareStorm(model, objectives, constraints, stormParsPath);
		return prepareStorm2(model, objectives, constraints, stormParsPath);
	}
	
	
	private List<String> prepareStorm(String model, List<Property> objectives, List<Property> constraints, String stormCommand) throws IOException{
		File m = new File(tempDir + File.separator + "model.pm");
		m.createNewFile();
		try{
			//Write concrete model needed by storm
			FileWriter fw;
			fw = new FileWriter(m);
			fw.write(model + "\n");
			fw.flush();
			fw.close();
			
			//create output and error files
			File outputFile	= new File(tempDir + File.separator + OUTPUT);
			File errorFile	= new File(tempDir + File.separator + ERROR);
			
			
			resultsList.clear();
			properties.clear();
			properties.addAll(objectives);
			properties.addAll(constraints);
			
			//invoke storm for all properties (objectives and constraints)
			for (Property p : properties){
				boolean successful = invokeStorm(m.getAbsolutePath(), p.getExpression(), outputFile, errorFile, stormCommand);
				
				if (successful) {
					String s = parseOutput(outputFile);
//					System.out.println(s);
					resultsList.add(s);
				}
				else
					return null;
					
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return resultsList;
	}
		
	
	private boolean invokeStorm(String modelPath, String property, File outputRun, File errorRun, String stormCommand){
		List<String> cmd = new ArrayList<String>();

		String prop = property.replaceAll("\"", "\\\\\"");
		
		cmd.add(stormCommand);//"/usr/local/bin/storm-pars");
		cmd.add("-pc");
		cmd.add("--prism");
		cmd.add(modelPath);
		cmd.add("--prop");
		cmd.add("\"" + prop + "\"");
		
		ProcessBuilder pb = new ProcessBuilder();
		String command = String.join(" ", cmd);
		pb.command("/bin/bash", "-c",  command);
		pb.redirectOutput(ProcessBuilder.Redirect.to(outputRun));
		pb.redirectError(ProcessBuilder.Redirect.to(errorRun));
		
		// run and wait until it is up and running
		boolean alive = false;
		try { 
			Process p;
			do {
				p = pb.start();
				alive = p.isAlive();
				Thread.sleep(1000);
			} while (!alive);

			 boolean OK = p.waitFor(300, TimeUnit.SECONDS);
			 p.destroyForcibly();
			 return OK;
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	private String parseOutput(File outputRun) {
		String result = null;
		try {
			// read this file and get the result from it and add it for result
			BufferedReader br = new BufferedReader(new FileReader(outputRun));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("Result")) {
					// format is: Result (initial states): (x*... formula)
					String ss[] = line.split(":");
					result = ss[1].trim();
					break;
				}
			}
			br.close();
			
			if ((result == null) || (result.equalsIgnoreCase("NULL")))
				return null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	
	private List<String> prepareStorm2(String model, List<Property> objectives, List<Property> constraints, String stormCommand) throws IOException{
		File m = new File(tempDir + File.separator + "model.pm");
		m.createNewFile();
		try{
			//Write concrete model needed by storm
			FileWriter fw;
			fw = new FileWriter(m);
			fw.write(model + "\n");
			fw.flush();
			fw.close();
			
			//resultsList.clear();
			resultsMap.clear();
			properties.clear();
			properties.addAll(objectives);
			properties.addAll(constraints);
			
			int propertiesNum = properties.size();
			
			threads = new Thread[propertiesNum];

			
			//invoke storm for all properties (objectives and constraints)
			for (int i=0; i<propertiesNum; i++){
				//get property
				Property p = properties.get(i);
				
				//create output and error files
				File outputFile	= new File(tempDir + File.separator + OUTPUT + "-" + i);
				File errorFile	= new File(tempDir + File.separator + ERROR  + "-" + i);
				
				//prepare and run one Storm thread per property
				RunnableStormParametric stormParamThread = new RunnableStormParametric(m.getAbsolutePath(), p.getExpression(), outputFile, errorFile, stormCommand, i);
				Thread t = new Thread(stormParamThread, "stormParam" + i);
				t.start();
				
				threads[i] = t; 
			}
			
			//wait for threads to finish and then return
			for (Thread t : threads){
				t.join();
			}
			
			//if (resultsList.contains(null))
			if (resultsMap.containsValue(""))
				return null;
		}
		catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		//return resultsList;
		return new ArrayList<String>(resultsMap.values());
	}
	
	
	private class RunnableStormParametric implements Runnable {

		String modelPath;
		String property; 
		File outputRun; 
		File errorRun; 
		String stormCommand;
		int index;
		
		public RunnableStormParametric(String modelPath, String property, File outputRun, File errorRun, String stormCommand, int index) {
			this.modelPath		= modelPath;
			this.property		= property;
			this.outputRun		= outputRun;
			this.errorRun		= errorRun; 
			this.stormCommand	= stormCommand;
			this.index			= index;
		}
		
		@Override
		public void run() {
			boolean successful = invokeStorm();
			
			try {
				if (successful) {
					String s = parseOutput(outputRun);
	//				System.out.println(s);
					//resultsList.add(index, s);
					resultsMap.put(index, s);
				}
				else
					//resultsList.add(index, null);
					resultsMap.put(index, "");
			}
			catch (NullPointerException e) {
				e.printStackTrace();
				System.err.println(outputRun);
				System.exit(0);
			}
		}
		
		
		private boolean invokeStorm(){
			List<String> cmd = new ArrayList<String>();

			String prop = property.replaceAll("\"", "\\\\\"");
			
			cmd.add(stormCommand);//"/usr/local/bin/storm-pars");
			cmd.add("-pc");
			cmd.add("--prism");
			cmd.add(modelPath);
			cmd.add("--prop");
			cmd.add("\"" + prop + "\"");
			
			ProcessBuilder pb = new ProcessBuilder();
			String command = String.join(" ", cmd);
			pb.command("/bin/bash", "-c",  command);
			pb.redirectOutput(ProcessBuilder.Redirect.to(outputRun));
			pb.redirectError(ProcessBuilder.Redirect.to(errorRun));
			
			// run and wait until it is up and running
			boolean alive = false;
			try { 
				Process p;
				do {
					p = pb.start();
					alive = p.isAlive();
					Thread.sleep(1000);
				} while (!alive);

				 boolean OK = p.waitFor(300, TimeUnit.SECONDS);
				 p.destroyForcibly();
				 return OK;
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		
		private String parseOutput(File outputRun) {
			String result = null;
			try {
				// read this file and get the result from it and add it for result
				BufferedReader br = new BufferedReader(new FileReader(outputRun));
				String line;
				while ((line = br.readLine()) != null) {
					if (line.startsWith("Result")) {
						// format is: Result (initial states): (x*... formula)
						String ss[] = line.split(":");
						result = ss[1].trim();
						break;
					}
				}
				br.close();
				
				if ((result == null) || (result.equalsIgnoreCase("NULL")))
					return "";
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return result;
		}
	}
	

	
	public static void  main(String args[]) {
		ModelInvokerStorm storm = new ModelInvokerStorm();
		
		String modelPath	= "/Users/sgerasimou/Documents/Git/EvoChecker/models/dieParam/dieFixed.pm";
		
		String property  = "P=? [ F s=7 & d=5 ]";
		List<Property> objectives = new ArrayList<>();
		objectives.add (new Objective(true, property, 0));
		
		try {
			List<String> resultsList = storm.invoke(FileUtil.readFile(modelPath), null, objectives, new ArrayList<Property>(), null, null);
			System.out.println(Arrays.toString(resultsList.toArray()));
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
