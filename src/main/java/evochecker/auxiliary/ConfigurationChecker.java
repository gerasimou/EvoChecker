package evochecker.auxiliary;

import java.io.File;
import java.util.Map;
import java.util.Properties;

import evochecker.exception.EvoCheckerException;

public class ConfigurationChecker {
	
	/**
	 * Check whether the experiment has been configured correctly 
	 * @throws EvoCheckerException 
	 */
	public static void checkConfiguration() throws EvoCheckerException {
		StringBuilder errors = new StringBuilder();
		final String NAN = "NAN";
		
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

		//check port
		if (Utility.getProperty(Constants.INITIAL_PORT_KEYWORD, NAN).equals(NAN))
			errors.append(Constants.INITIAL_PORT_KEYWORD + " not found in configuration script!\n");

		//check jvm
		if (Utility.getProperty(Constants.JVM_KEYWORD, NAN).equals(NAN))
			errors.append(Constants.JVM_KEYWORD + " not found in configuration script!\n");

		//check model checking engine
		if (Utility.getProperty(Constants.MODEL_CHECKING_ENGINE, NAN).equals(NAN))
			errors.append(Constants.MODEL_CHECKING_ENGINE + " not found in configuration script!\n");
		else {
			File engine = new File(Utility.getProperty(Constants.MODEL_CHECKING_ENGINE));
			if (!engine.exists())
				errors.append(Utility.getProperty(Constants.MODEL_CHECKING_ENGINE) + " does not exist!\n");
		}

		//check model checking engine libs directory
		if (Utility.getProperty(Constants.MODEL_CHECKING_ENGINE_LIBS_DIR, NAN).equals(NAN))
			errors.append(Constants.MODEL_CHECKING_ENGINE_LIBS_DIR + " not found in configuration script!\n");
		else {
			File engine = new File(Utility.getProperty(Constants.MODEL_CHECKING_ENGINE_LIBS_DIR));
			if (!engine.exists())
				errors.append(Utility.getProperty(Constants.MODEL_CHECKING_ENGINE_LIBS_DIR) + " does not exist!\n");
		}


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
