package evochecker.modelInvoker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ModelInvokerStorm implements IModelInvoker {

	public File tempDir;
	public String OUTPUT = "storm.out";
	public String ERROR  = "storm.err";
	
	
	public ModelInvokerStorm() {
		tempDir = new File("temp");
		tempDir.mkdirs();
		tempDir.deleteOnExit();
	}
	
	
	@Override
	public List<String> invoke(String model, String propertyFile, PrintWriter out, BufferedReader in)
			throws IOException {
		return null;
	}


	@Override
	public List<String> invokeParam(String model, String propertyFile, PrintWriter out, BufferedReader in) throws IOException {
		File m = new File(tempDir + File.separator + "model.pm");
		try{
			FileWriter fw;
			fw = new FileWriter(model);
			fw.write(m + "\n");
			fw.flush();
			fw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	
	public static void  main(String args[]) {
		ModelInvokerStorm storm = new ModelInvokerStorm();
		String modelPath = "/Users/simosgerasimou/git/EvoCheckerNew/EvoChecker/models/dieParam/die.pm";
		String property  = "P=? [ F s=7 & d=5 ]";
		
		File outputRun	= new File(storm.tempDir + File.separator + storm.OUTPUT);
		File errorRun	= new File(storm.tempDir + File.separator + storm.ERROR);

		
		//invoke storm
		boolean successful = storm.invokeStorm(modelPath, property, outputRun, errorRun);
		
		if (successful) {
			String s = storm.parseOutput(outputRun);
			System.out.println(s);
		}
	}
	
	
	private boolean invokeStorm(String modelPath, String property, File outputRun, File errorRun){
		List<String> cmd = new ArrayList<String>();

		cmd.add("/usr/local/bin/storm-pars");
		cmd.add("--prism");
		cmd.add(modelPath);
		cmd.add("--prop");
		cmd.add("\"" + property + "\"");
		
		ProcessBuilder pb = new ProcessBuilder();
		String command = String.join(" ", cmd);
		pb.command("/bin/bash", "-c",  command);
		pb.redirectOutput(java.lang.ProcessBuilder.Redirect.to(outputRun));
		pb.redirectError(java.lang.ProcessBuilder.Redirect.to(errorRun));
		
		
		// run and wait until it is up and running
		boolean alive = false;
		try { 
			do {
				Process p = pb.start();
				alive = p.isAlive();
				Thread.sleep(1000);
			} while (!alive);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		return true;
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}
