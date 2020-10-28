package evochecker.plotting;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import evochecker.exception.EvoCheckerException;

public class PlotFactory {

	
	public static void plotParetoFront(String frontFile, String identifier, int objectivesNum) {
		try {
			if (objectivesNum == 2)
					plotParetoFront(frontFile, true);
			else if (objectivesNum == 3)
				plotParetoFront(frontFile, false);
			else
				throw new EvoCheckerException("Can only plot Pareto front for 2 or 3 objectives");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	
	public static void plotParetoFront(String frontFile, boolean twoD) throws Exception {
		String paretoFrontPath = new File(frontFile).getAbsolutePath();
		
		String scriptFile = null;
		if (twoD)
			scriptFile = "scripts/plotFront2D.py";
		else
			scriptFile = "scripts/plotFront3D.py";
		
	    ProcessBuilder processBuilder = new ProcessBuilder("/usr/local/bin/python3", scriptFile , paretoFrontPath);
	    processBuilder.redirectErrorStream(true);
	 
	    Process process = processBuilder.start();
	    InputStream is = process.getInputStream();
	    
	    List<String> output = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.toList());
	    
	    System.out.println(Arrays.toString(output.toArray()));
	 
	    int exitCode = process.waitFor();
	}
}
