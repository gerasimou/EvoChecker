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
import java.util.Arrays;
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
import java.nio.file.*;

public class Utility {

	private static String fileName;
	private static Properties properties;
	private static String modelFileOverride;
	private static String propertiesFileOverride;

	public static void setPropertiesFile(String filename) {
		fileName = filename;
	}

	public static void setPropertiesFileOverride(String propertyFile) {
		if (isFilePath(propertyFile)) {
			propertiesFileOverride = propertyFile;
		} else {
			System.out.println("Creating a temporary property file for property: " + propertyFile);
			setPropertiesFromString(propertyFile);
		}
	}

	public static void setModelFileOverride(String modelFile) {
		modelFileOverride = modelFile;
	}

	public static boolean isFilePath(String pathString) {
		try {
			Path path = Paths.get(pathString);
			return Files.exists(path) && Files.isRegularFile(path);
		} catch (Exception e) {
			// If an exception occurs (e.g., invalid path), return false
			return false;
		}
	}

	public static void setPropertiesFromString(String propertyContent) {
		try {
			// Create a temporary file
			Path tempFile = Files.createTempFile("tempPropertyFile", ".properties");
			String fileContents = "";

			//format the property file contents
			String[] elements = propertyContent.split("//"); // e.g. objective,min:P=? [F s=7 & d=5]
			// System.out.println("Elements: " + Arrays.toString(elements));
			for (String element : elements) {
				String[] keyValue = element.split(":");
				// System.out.println("KeyValue: " + Arrays.toString(keyValue));
				if (keyValue[0].trim().isEmpty()) {
					continue;
				}
				else if (keyValue.length == 2) {
					fileContents += "//" + keyValue[0].trim() + "\n" + keyValue[1].trim() + "\n\n";
				} else {
					// System.out.println("PROBLEM: " + Arrays.toString(keyValue) + " " + keyValue.length);
					throw new EvoCheckerException("Invalid property format: '" + element + "' in " + propertyContent);
				}
			}


			// Write the property content to the temporary file
			Files.write(tempFile, fileContents.getBytes());

			// Update the propertiesFileOverride with the path to the temporary file
			propertiesFileOverride = tempFile.toAbsolutePath().toString();

			System.out.println("Temporary property file created at: " + propertiesFileOverride);
		} catch (IOException | EvoCheckerException e) {
			System.err.println("Error creating temporary property file: " + e.getMessage());
			// e.printStackTrace();
			System.exit(1);
		}
	}

	private static void loadPropertiesInstance() {
		try {
			if (properties == null) {
				properties = new Properties();
				properties.load(new FileInputStream(fileName));
				if (modelFileOverride != null) {
					properties.setProperty("MODEL_TEMPLATE_FILE", modelFileOverride);
				}
				if (propertiesFileOverride != null) {
					properties.setProperty("PROPERTIES_FILE", propertiesFileOverride);
				}
			}
		} catch (IOException | NullPointerException e) {
			if (fileName == null) {
				System.out.println("Properties file has not been specified. Exiting.");
			} else {
				System.out.println("Error loading properties file: " + fileName + ". Exiting.");
			}
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static String getProperty(String key) {
		String result = getPropertyIgnoreNull(key);
		if (result == null)
			throw new IllegalArgumentException(key.toUpperCase() + " name not found!");
		return result;
	}

	public static String getPropertyIgnoreNull(String key) {
		loadPropertiesInstance();
		String result = properties.getProperty(key).strip();
		return result;
	}

	public static String getProperty(String key, String defaultValue) {
		loadPropertiesInstance();
		String output = properties.getProperty(key);
		return (output != null ? output.trim() : defaultValue.trim());
	}

	public static StringProperties getAllProperties() {
		return new StringProperties(properties);
	}

	public static void setProperty(String key, String value) throws EvoCheckerException {
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
	 * 
	 * @param path The output file name
	 */
	public static void printObjectivesToFile(String path, List<Solution> solutions, List<Property> objectivesList) {
		try {
			/* Open the file */
			FileOutputStream fos = new FileOutputStream(path, true);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);

			int numOfObjectives = objectivesList.size();
			for (Solution solution : solutions) {
				StringBuilder objString = new StringBuilder();
				for (int i = 0; i < numOfObjectives; i++) {
					if (objectivesList.get(i).isMaximization())
						objString.append(-(solution.getObjective(i)));
					else
						objString.append(solution.getObjective(i));
					if (i < numOfObjectives - 1)
						objString.append("\t");
				}
				bw.write(objString.toString());
				bw.newLine();
				// }
			}
			/* Close the file */
			bw.close();
		} catch (IOException e) {
			Configuration.logger_.severe("Error acceding to the file");
			e.printStackTrace();
		}
	} // printObjectivesToFile

	/**
	 * Writes the decision encodings.variable values of the <code>Solution</code>
	 * solutions objects into the set in a file.
	 * 
	 * @param path The output file name
	 */
	public static void printVariablesToFile(String path, List<Solution> solutions) {
		try {
			FileOutputStream fos = new FileOutputStream(path, true);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);

			int numberOfVariables = solutions.get(0).getDecisionVariables().length;
			for (Solution aSolutionsList_ : solutions) {
				for (int j = 0; j < numberOfVariables; j++) {
					Variable v = aSolutionsList_.getDecisionVariables()[j];
					bw.write(v.toString() + "\t");
				}
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			Configuration.logger_.severe("Error acceding to the file");
			e.printStackTrace();
		}
	} // printVariablesToFile

	/**
	 * Writes the decision encodings.variable values of the <code>Solution</code>
	 * solutions objects into the set in a file.
	 * 
	 * @param path The output file name
	 */
	public static void printVariablesToFile2(String path, List<Solution> solutions,
			Map<AbstractGene, Evolvable> elementsMap, List<AbstractGene> genes) {
		List<Integer> indexes = new ArrayList<>();
		List<EvolvableOption> evolvableOptionsList = new ArrayList<EvolvableOption>();
		int i = -1;
		for (AbstractGene g : genes) {
			if (g instanceof IntegerGene) {
				i++;
				if (elementsMap.get(g) instanceof EvolvableOption) {
					indexes.add(i);
					evolvableOptionsList.add((EvolvableOption) elementsMap.get(g));
				}
			}
		}

		try {
			FileOutputStream fos = new FileOutputStream(path, true);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);

			for (Solution aSolutionsList_ : solutions) {
				int numOfIntVariables = ((ArrayInt) aSolutionsList_.getDecisionVariables()[1]).getLength();
				int k = 0;
				for (int j = 0; j < numOfIntVariables; j++) {
					int value = ((ArrayInt) aSolutionsList_.getDecisionVariables()[1]).getValue(j);
					if (indexes.contains(j)) {
						bw.write(evolvableOptionsList.get(k++).getOption(value) + " ");
					} else
						bw.write(value + " ");
				}
				bw.write(aSolutionsList_.getDecisionVariables()[0].toString());
				bw.newLine();
			}
			bw.close();
		} catch (IOException | JMException e) {
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
				return bashInvoker(bashCommand) + "/bin/java";
			} else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
				bashCommand = "which java";
				return bashInvoker(bashCommand);
			} else
				throw new EvoCheckerException("EvoChecker currently supports only OSX and Unix.");

		} catch (EvoCheckerException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String findAvailablePort(int initPort) {
		String result;
		do {
			String bashCommand = "lsof -i:" + initPort++;
			result = bashInvoker(bashCommand);
		} while (result != null);
		return --initPort + "";
	}

	public static String findPython3Path() {
		String bashCommand = "which python";
		String out = bashInvoker(bashCommand);
		return out;
	}

	public static String runtimeLibsDirSpecified() throws EvoCheckerException {
		String os = getOperatingSystem();
		String variable = null;

		if (os.contains("mac")) {
			variable = "DYLD_LIBRARY_PATH";
		} else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
			variable = "LD_LIBRARY_PATH";
		} else
			throw new EvoCheckerException("EvoChecker currently supports only OSX and Unix.");

		if (System.console() == null) {// within an IDE
			// System.out.println("IDE");
			String out = System.getenv(variable);
			if (out == null || !new File(out).isDirectory())
				return variable;
		} else {// within a terminal
				// System.out.println("Terminal");
				// assume that the script has been specified correctly
			if (System.getenv(variable) == null) {
				throw new EvoCheckerException("Environment variable " + variable + " has not been set. Exiting.");
			}
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String args[]) {
		// System.out.println(Utility.bashInvoker("which java"));
		// System.out.println(Utility.bashInvoker("which python3"));
		// System.out.println(Utility.bashInvoker("which storm-pars"));
		System.out.println(Utility.bashInvoker("echo $PATH"));
		// System.out.println(Utility.bashInvoker("storm-pars"));
	}

}
