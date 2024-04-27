package evochecker.plotting;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import evochecker.auxiliary.ConfigurationChecker;
import evochecker.auxiliary.Constants;
import evochecker.auxiliary.Utility;
import evochecker.exception.EvoCheckerException;

public class PlotFactory {

	private static final String script2DFile = "scripts/plotFront2D.py";
	private static final String script3DFile = "scripts/plotFront3D.py";
	private static String scriptFile   = null;

	
	public static void plotParetoFront(String frontFile, int objectivesNum) {
		try {
			System.out.println("\nGenerating Pareto Front plot");
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
	
	private static void plotParetoFront(String frontFile, boolean twoD) throws Exception {
		String paretoFrontPath = new File(frontFile).getAbsolutePath();
		
		//use default scripts if script file is null
		if (twoD && scriptFile == null)			
			scriptFile = script2DFile;
		else if (!twoD && scriptFile == null)
			scriptFile = script3DFile;
		
		String python3Dir = Utility.getProperty(Constants.PYTHON3_DIRECTORY, ConfigurationChecker.NAN);
		if (python3Dir.equals(ConfigurationChecker.NAN))
			throw new EvoCheckerException(Constants.PYTHON3_DIRECTORY + " not found in configuration script!\n");

		else {			
		    ProcessBuilder processBuilder = new ProcessBuilder(python3Dir, scriptFile , paretoFrontPath);
		    processBuilder.redirectErrorStream(true);
		 
		    Process process = processBuilder.start();
		    InputStream is = process.getInputStream();
		    
		    List<String> output = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.toList());
		    
		    System.out.println(Arrays.toString(output.toArray()));
		 
		    int exitCode = process.waitFor();
		}
	}
	
	public static void setParetoFrontScriptFile (String scriptFilename) {
		scriptFile = scriptFilename;
	}
	
	
	public static void main(String[] args) throws EvoCheckerException {
		String configFile = "config.properties";
		Utility.setPropertiesFile(configFile);

		String script2DFile = "/Users/simos/Git/EvoChecker/data/PAL/NSGAII/PAL_NSGAII_260124_1836254979711536701415111_Front";
		PlotFactory.plotParetoFront(script2DFile, 2);		
	}
}
