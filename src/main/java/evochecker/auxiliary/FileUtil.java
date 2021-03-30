//==============================================================================
//	
 //	Copyright (c) 2015-
//	Authors:
//	* Simos Gerasimou (University of York)
//	
//------------------------------------------------------------------------------
//	
//	This file is part of EvoChecker.
//	
//==============================================================================

package evochecker.auxiliary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Properties;

import evochecker.exception.LanguageException;

/**
 * Utility class with helper functions
 * @author sgerasimou
 *
 */
public class FileUtil {
	
	private static String fileName = "resources/config.properties";
	private static Properties properties;
	
	private static void loadPropertiesInstance(){
		try {
			if (properties == null){
				properties = new StringProperties();
				properties.load(new FileInputStream(fileName));
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void loadPropertiesInstance(Properties props){
		if ( (properties == null) && (props != null) )
			properties = props;
	}
	
	
	public static void setProperty (String key, String value) throws LanguageException{
		loadPropertiesInstance();
		if (properties.setProperty(key, value) ==null)
			throw new LanguageException("Key: " + key + " does not exist!");
	}

	
	public static String getProperty (String key){
		loadPropertiesInstance();
		String result = properties.getProperty(key); 
		if ( (result == null) || (result.isEmpty()))
			  throw new IllegalArgumentException(key.toUpperCase() + " keyword not found in configuration file!");
		return result;		
	}
	
	
	public static String getProperty (String key, String defaultValue){
		loadPropertiesInstance();
		String output = properties.getProperty(key);
		return ((output != null && !output.isEmpty()) ? output : defaultValue);
	}

	
	public static StringProperties getAllProperties() {
		return new StringProperties(properties);
	}
	
	
	/**
	 * 
	 * @param fileName
	 * @param output
	 * @param append
	 */
	public static void saveToFile(String fileName, String output, boolean append){
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

	
	/**
	 * 
	 * @param inputFileName
	 * @param outputFileName
	 * @param outputStr
	 */
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
			
			saveToFile(outputFileName, outputStr, false);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	* Read file
	* @param fileName
	* @return
	*/
	@SuppressWarnings("resource")
	public static String readFile(String fileName) {
		try {
			File f = new File(fileName);
			if (!f.exists() || f.isDirectory())
				throw new IOException("File does not exist! " + f );
		
			StringBuilder model = new StringBuilder(100);
			BufferedReader bfr = null;

			bfr = new BufferedReader(new FileReader(f));
			String line = null;
			while ((line = bfr.readLine()) != null) {
				model.append(line + "\n");
			}
			model.delete(model.length() - 1, model.length());
			return model.toString();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return null;
	}

	
	public static void exportToFile(List<String> outputList, String fileName){
		try {
			FileWriter writer = new FileWriter(fileName);
			for (String str : outputList){	
				writer.append(str +"\n");
			}
				writer.flush();
				writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void createDir(String filePath) {
		File file = new File(filePath);
		if (!file.exists())
			file.mkdirs();
	}
	
	
	public static boolean deleteDirectory(File directoryToBeDeleted) {
	    File[] allContents = directoryToBeDeleted.listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	            deleteDirectory(file);
	        }
	    }
	    return directoryToBeDeleted.delete();
	}
	
}
