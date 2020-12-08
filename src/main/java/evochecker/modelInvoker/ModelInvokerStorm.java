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
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import evochecker.auxiliary.FileUtil;
import evochecker.properties.Objective;
import evochecker.properties.Property;

public class ModelInvokerStorm implements IModelInvoker {

	public File tempDir;
	public String OUTPUT = "storm.out";
	public String ERROR  = "storm.err";
	List<String> resultsList;
	List<Property> properties;
	
	public ModelInvokerStorm() {
		tempDir = new File("temp");
		tempDir.mkdirs();
		tempDir.deleteOnExit();
		
		resultsList = new  CopyOnWriteArrayList<>();
		properties = new CopyOnWriteArrayList<>();
	}
	
	
	public IModelInvoker copy() {
		return new ModelInvokerStorm(); 
	}
	
	
	@Override
	public List<String> invoke(String model, String propertyFile, List<Property> objectives, List<Property> constraints, PrintWriter out, BufferedReader in) throws IOException {
		ModelInvokerPrism prism = new ModelInvokerPrism();
		return prism.invoke(model, propertyFile, objectives, constraints, out, in);
//				prepareStorm(model, objectives, constraints, "/usr/local/bin/storm");		
	}


	@Override
	public List<String> invokeParam(String model, String propertyFile, List<Property> objectives, List<Property> constraints, PrintWriter out, BufferedReader in) throws IOException {
		return prepareStorm(model, objectives, constraints, "/usr/local/bin/storm-pars");
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
