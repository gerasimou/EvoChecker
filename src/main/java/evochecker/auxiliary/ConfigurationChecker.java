package evochecker.auxiliary;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

import evochecker.EvoCheckerType;
import evochecker.exception.EvoCheckerException;
import evochecker.modelInvoker.ModelInvokerEngine;

public class ConfigurationChecker {
	
	public final static String NAN = "NAN";
	public final static String TRUE = "TRUE";
	public final static String FALSE = "FALSE";

	/**
	 * Check whether the experiment has been configured correctly 
	 * @throws EvoCheckerException 
	 */
	public static void checkConfiguration() throws EvoCheckerException {
		StringBuilder errors = new StringBuilder();
		
		String runtimeLibDir = Utility.runtimeLibsDirSpecified();
		if (runtimeLibDir != null)
			errors.append(runtimeLibDir  + " has not been specified.\n On Eclipse: Run > Run Configurations > Environment > New > Variable: " 
					+ runtimeLibDir +"; Value:" + Constants.MODEL_CHECKING_ENGINE_LIBS_DIR_DEFAULT);
		
		//check algorithm
		if (Utility.getProperty(Constants.ALGORITHM_KEYWORD, NAN).equals(NAN)) 
			errors.append(Constants.ALGORITHM_KEYWORD + " not found in configuration script!\n");
		else {
			try {
				Constants.ALGORITHM.valueOf(Utility.getProperty(Constants.ALGORITHM_KEYWORD, NAN));
			} catch (IllegalArgumentException e) {
				errors.append(e.getMessage());
			}
		}
		
		//check population size
		if (Utility.getProperty(Constants.POPULATION_SIZE_KEYWORD, NAN).equals(NAN))
			errors.append(Constants.POPULATION_SIZE_KEYWORD + " not found in configuration script!\n");

		//check evaluations
		if (Utility.getProperty(Constants.MAX_EVALUATIONS_KEYWORD, NAN).equals(NAN))
			errors.append(Constants.MAX_EVALUATIONS_KEYWORD + " not found in configuration script!\n");

		//check processors
		if (Utility.getProperty(Constants.PROCESSORS_KEYWORD, NAN).equals(NAN))
			errors.append(Constants.PROCESSORS_KEYWORD + " not found in configuration script!\n");

		//check model file
		if (Utility.getProperty(Constants.MODEL_FILE_KEYWORD, NAN).equals(NAN))
			errors.append(Constants.MODEL_FILE_KEYWORD + " not found in configuration script!\n");

		//check properties file
		if (Utility.getProperty(Constants.PROPERTIES_FILE_KEYWORD, NAN).equals(NAN))
			errors.append(Constants.PROPERTIES_FILE_KEYWORD + " not found in configuration script!\n");

		//check problem name
		if (Utility.getProperty(Constants.PROBLEM_KEYWORD, NAN).equals(NAN))
			errors.append(Constants.PROBLEM_KEYWORD + " not found in configuration script!\n");
		
		
		//check model checking engine libs directory
		File engineLibs;
		if (Utility.getProperty(Constants.MODEL_CHECKING_ENGINE_LIBS_DIR, NAN).equals(NAN))
			engineLibs = new File(Constants.MODEL_CHECKING_ENGINE_LIBS_DIR_DEFAULT);
		else 
			engineLibs = new File(Utility.getProperty(Constants.MODEL_CHECKING_ENGINE_LIBS_DIR));
		
		if (!engineLibs.exists())
			errors.append("Model checking libs at " + engineLibs.getAbsolutePath() + " do not exist!\n" + 
						  "You can specify the libs in the configuration script using " + Constants.MODEL_CHECKING_ENGINE_LIBS_DIR +"\n");
		else
			Utility.setProperty(Constants.MODEL_CHECKING_ENGINE_LIBS_DIR, engineLibs.getAbsolutePath());


		//check and set the Java Path based on operating system 
		if (Utility.getProperty(Constants.JAVA_KEYWORD, NAN).equals(NAN)) {
			String javaPath = Utility.findJavaPath();
			if (javaPath != null)
				Utility.setProperty(Constants.JAVA_KEYWORD, javaPath);
			else
				errors.append(Constants.JAVA_KEYWORD + " not found in configuration script!\n");
		}


		//check port: if unspecified in configuration file, try to fine one randomly
		if (Utility.getProperty(Constants.INITIAL_PORT_KEYWORD, NAN).equals(NAN)) {
			String port = Utility.findAvailablePort(8888);
			Utility.setProperty(Constants.INITIAL_PORT_KEYWORD, port);
		}
		//errors.append(Constants.INITIAL_PORT_KEYWORD + " not found in configuration script!\n");

		
		//check if the Evochecker type has been specified correctly
		EvoCheckerType ecType = null;
		try {
			ecType = EvoCheckerType.valueOf(Utility.getPropertyIgnoreNull(Constants.EVOCHECKER_TYPE).toUpperCase());
		}
		catch (IllegalArgumentException e) {
			errors.append(Constants.EVOCHECKER_TYPE + " incorrectly specified. Allowed options: " + 
						  Arrays.toString(EvoCheckerType.values()) +"\n");			
		}
		catch (NullPointerException e) {
			System.err.println("EVOCHECKER_TYPE not specified, using NORMAL; Options: " + Arrays.toString(EvoCheckerType.values()));
			Utility.setProperty(Constants.EVOCHECKER_TYPE, EvoCheckerType.NORMAL+"");
		}
		

		//check if the Evochecker Engine has been specified correctly
		ModelInvokerEngine ecEngine = null;
		try {
			ecEngine = ModelInvokerEngine.valueOf(Utility.getPropertyIgnoreNull(Constants.EVOCHECKER_ENGINE).toUpperCase());
			if (ecEngine.name() == ModelInvokerEngine.STORM.name().toUpperCase()) {
				String stormParsPath = Utility.bashInvoker("which storm-pars");
				if (stormParsPath == null)
					errors.append("Storm-pars not installed. Please check again");			
			}
		}
		catch (IllegalArgumentException e) {
			errors.append(Constants.EVOCHECKER_ENGINE + " incorrectly specified. Allowed options: " + 
						  Arrays.toString(ModelInvokerEngine.values()) +"\n");			
		}
		catch (NullPointerException e) {
			System.err.println("EVOCHECKER_ENGINE not specified, using PRISM; Options: " + Arrays.toString(ModelInvokerEngine.values()));
			Utility.setProperty(Constants.EVOCHECKER_ENGINE, ModelInvokerEngine.PRISM+"");
		}

		File engine = null;
		if (Utility.getProperty(Constants.MODEL_CHECKING_ENGINE, NAN).equals(NAN)) {
			if (ecType == EvoCheckerType.REGION)
				engine = new File(Constants.MODEL_CHECKING_ENGINE_REGION);
			else if ((ecType == EvoCheckerType.NORMAL) && (ecEngine == ModelInvokerEngine.STORM)) {
				engine = new File(Constants.MODEL_CHECKING_ENGINE_DEFAULT);
				System.err.println("EVOCHECKER_ENGINE specified to Storm. Note that Storm is slower than Prism for model checking concrete instances");
			}
			else if ((ecType == EvoCheckerType.NORMAL) && (ecEngine == ModelInvokerEngine.PRISM)) {
				engine = new File(Constants.MODEL_CHECKING_ENGINE_DEFAULT);
			}
			else if ((ecType == EvoCheckerType.PARAMETRIC) && (ecEngine == ModelInvokerEngine.STORM)) {
				engine = new File(Constants.MODEL_CHECKING_ENGINE_DEFAULT);
			}
			else if ((ecType == EvoCheckerType.PARAMETRIC) && (ecEngine == ModelInvokerEngine.PRISM)) {
				engine = new File(Constants.MODEL_CHECKING_ENGINE_DEFAULT);
				System.err.println("EVOCHECKER_ENGINE specified to Prism. Note that Prism is slower than Storm for parametric model checking");
			}
		}
		else 
			engine = new File(Constants.MODEL_CHECKING_ENGINE_DEFAULT);

		
//
//		//check model checking engine
//		File engine;
//		if (ecEngine == ModelInvokerEngine.PRISM)
//			if (ecType == EvoCheckerType.REGION)
//				engine = new File(Constants.MODEL_CHECKING_ENGINE_REGION);
//			else 
//				engine = new File(Constants.MODEL_CHECKING_ENGINE_DEFAULT);
//		else if (ecEngine == ModelInvokerEngine.STORM)
//			engine = new File(Constants.MODEL_CHECKING_ENGINE_DEFAULT);
//		else 
//			engine = null;
	
		
		if (!engine.exists())
			errors.append("Model Checking engine at " + engine.getAbsolutePath() + " does not exist!\n" + 
						  "You can specify the engine in the configuration script using " + Constants.MODEL_CHECKING_ENGINE_LIBS_DIR +"\n");
		else
			Utility.setProperty(Constants.MODEL_CHECKING_ENGINE, engine.getAbsolutePath());
		
		
		if (errors.length()!=0)
			throw new EvoCheckerException(errors.toString().split("\r\n|\r|\n").length +"\n"+ errors.toString());
		else
			System.out.println(getConfiguration());
	}
	
	
	/**
	 * Print the current EvoChecker configuration
	 * @return
	 */
	private static String getConfiguration() {
		StringBuilder str = new StringBuilder();
		
		str.append("Configuration script\n");
		str.append("==========================================\n");
		
		Properties props = Utility.getAllProperties();
		for (Map.Entry<Object, Object> entry : props.entrySet()) {
			str.append(entry.getKey() +" = "+ entry.getValue() +"\n");
		}
		str.append("==========================================\n");
		
		return str.toString();
	}
}
