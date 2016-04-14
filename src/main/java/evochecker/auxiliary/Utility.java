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
import java.util.List;
import java.util.Properties;

import jmetal.core.Solution;
import jmetal.util.Configuration;

public class Utility {
	
	private static String fileName = "res/config.properties";
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
		}
	}
	
	
	public static void setProperty (String key, String value){
		loadPropertiesInstance();
		properties.setProperty(key, value);
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
	
	
	public static void exportToFile(String fileName, String output, boolean append){
		try {
			FileWriter writer = new FileWriter(fileName, append);
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
			
			exportToFile(outputFileName, outputStr, false);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@SuppressWarnings("resource")
	public static String readFile(String fileName) {
		StringBuilder model = new StringBuilder(100);
		BufferedReader bfr = null;

		try {
			bfr = new BufferedReader(new FileReader(new File(fileName)));
			String line = null;
			while ((line = bfr.readLine()) != null) {
				model.append(line + "\n");
			}
			model.delete(model.length() - 1, model.length());
			return model.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public static void exportToFile(List<Object> outputList, String fileName){
		try {
			FileWriter writer = new FileWriter(fileName);
			for (Object str : outputList){	
				writer.append(str +"\n");
			}
				writer.flush();
				writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Writes the decision encodings.variable values of the
	 * <code>Solution</code> solutions objects into the set in a file.
	 * 
	 * @param path
	 *            The output file name
	 */
	static public void printVariablesToFile(String path, Solution solution, boolean append) {
		try {
			FileOutputStream fos = new FileOutputStream(path, append);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);

			int numberOfVariables = solution.getDecisionVariables().length;
			for (int j = 0; j < numberOfVariables; j++)
				bw.write(solution.getDecisionVariables()[j].toString() + " ");
			bw.newLine();
			bw.flush();
			bw.close();
		} catch (IOException e) {
			Configuration.logger_.severe("Error acceding to the file");
			e.printStackTrace();
		}
	} // printVariablesToFile
}
