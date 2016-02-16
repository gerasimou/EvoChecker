package _mainX;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import parser.Values;
import parser.ast.ModulesFile;
import parser.ast.PropertiesFile;
import parser.ast.Property;
import prism.Prism;
import prism.PrismException;
import prism.PrismFileLog;
import prism.PrismLog;
import prism.Result;
import prism.ResultsCollection;
import pse.BoxRegion;
import pse.BoxRegionValues;
import pse.DecompositionProcedure;
import pse.BoxRegionValues.StateValuesPair;

public class PrismPSE_API {

	/** Prism log: where to log prism results*/
	private final String PRISMOUTPUTFILENAME = "output_Prism.txt";

	/** file keeping the properties*/
	private File propertyFile;

	/** file keeping the model*/
	private File modelFile;
	
	/** list of properties to check*/
	private List<Property> propertiesToCheck;

	/** how many properties will be checked*/
	int numPropertiesToCheck;

	/** Prism main log*/
	private PrismLog mainLog;
	
	/** Main prism handler*/
	private Prism prism;
	
	/** Modules file instance*/
	private ModulesFile modulesFile;
	
	/** Properties file instance*/
	private PropertiesFile propertiesFile;
	
	private String modelString;

	//PSE related parameters:parameter space exploration info
	/** PSE time*/
	private String pseTime;
	
	/** PSE accuracy*/
	private double pseAccuracy;
	
	/** Parameter names*/
	private String[] pseNames = null;
	
	/** Parameters lower bounds*/
	private double[] pseLowerBounds = null;
	
	/** Parameters upper bounds */
	private double[] pseUpperBounds = null;
	
	
	/**
	 * Class constructor
	 * 
	 * @param modelFile
	 *            - the Markov model file to be provided as input to PRISM
	 * @param propertiesFile
	 *            - the temporal logic file to be provided as input to PRISM
	 */
	public PrismPSE_API(String propertiesFilename) {
		try {
			this.propertyFile = new File(propertiesFilename);

			// initialise PRISM
			mainLog = new PrismFileLog(PRISMOUTPUTFILENAME, false);
			prism = new Prism(mainLog, mainLog);
			prism.initialise();
			prism.setLinEqMethod(1);
			prism.setMaxIters(100000);

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		} 
//		finally {
//			mainLog.close();
//		}
	}

	
	/** 
	 * Load the model given as string 
	 * @param modelString
	 */
	public void loadModel(String modelString) {
		// and build the model
		try {
			this.modelString = modelString;
			modulesFile = prism.parseModelString(this.modelString);
			modulesFile.setUndefinedConstants(null);
			propertiesFile = prism.parsePropertiesFile(modulesFile,propertyFile);
			propertiesFile.setUndefinedConstants(null);
			prism.buildModel(modulesFile);
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	
	/**
	 * This function receives data for the model and returns a double value for
	 * the quantified property.
	 */
	public List<Double> runPrism() {

		List<Double> results = new ArrayList<Double>();

		try {
			// run QV
			for (int i = 0; i < propertiesFile.getNumProperties(); i++) {
				Result result = prism.modelCheck(propertiesFile,propertiesFile.getProperty(i));
//				System.out.println(propertiesFile.getProperty(i));
				if (result.getResult() instanceof Boolean) {
					boolean booleanResult = (Boolean) result.getResult();
					
					if (booleanResult) {
						results.add(1.0);
					} else {
						results.add(0.0);
					}
				} else
					results.add(Double.parseDouble(result.getResult().toString()));
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Model checking error");
			System.exit(-1);
		}
		return results;
	}


	/**
	 * Close down PrismAPI & PRISM
	 */
	 public void closeDown(){
		if (mainLog!=null)
			mainLog.close();
		modulesFile = null;
		propertiesFile = null;
		propertyFile = null;
		prism = null;
	}
	
	
	
	// **************** PRISM-PSE ****************//
	// *******************************************//
	
	public PrismPSE_API(String mFilename, String propFilename){
		try{
			//init prism log
			mainLog = new PrismFileLog(PRISMOUTPUTFILENAME, false);
			//init prism object
			prism	 			= new Prism(mainLog, mainLog);
			//read files
			String modelFilename 		= mFilename;
			String propertiesFilename	= propFilename;
			//parse model file
			modulesFile = prism.parseModelFile(new File(modelFilename));
			//parse properties file
			propertiesFile = prism.parsePropertiesFile(modulesFile, new File(propertiesFilename));
			//load model
			prism.loadPRISMModel(modulesFile);
			//properties to check
			propertiesToCheck = new ArrayList<Property>();
			numPropertiesToCheck = propertiesFile.getNumProperties();
			for (int i = 0; i < numPropertiesToCheck; i++) {
				propertiesToCheck.add(propertiesFile.getPropertyObject(i));
			}
			//parse undefined constants
			Values definedMFConstants = new Values();
			prism.setPRISMModelConstants(definedMFConstants);
//			modulesFile.setUndefinedConstants(null);
		}
		catch (PrismException | FileNotFoundException e){
			e.printStackTrace();
		}
	}
	
	/**
 	 * This function receives data for the model and returns a double value for
	 * the quantified property.
	 * 
	 * pse.DecompositionProcedure.Type decompositionType, PropertiesFile propertiesFile, Property prop,
	 * String[] paramNames, double[] paramLowerBounds, double[] paramUpperBounds, double accuracy)
	 * @return 
	 */
	public List<Result> launchPrismPSE(DecompositionProcedure.Type pseCheckType, String pseSwitch, double pseAccuracy) throws PrismException, FileNotFoundException{
		List<Result> resultsList = new ArrayList<Result>();
		
		//pse check type
		DecompositionProcedure.Type decompositionProcedure = pseCheckType;
		//pse parameters and ranges
		String paramsRanges = pseSwitch.trim();
		//pse accuracy
		this.pseAccuracy		= pseAccuracy;
		//find parameter ranges
		findParameterRanges(pseSwitch);
		//init results storage
//		ResultsCollection results[] = new ResultsCollection[3];
		//run
		for (int i = 0; i < numPropertiesToCheck; i++) {
			Result res = prism.modelCheckPSE(pseCheckType, propertiesFile, propertiesToCheck.get(i), pseNames, pseLowerBounds, pseUpperBounds, pseAccuracy);
			resultsList.add(res);
		}
		//return the list
		return resultsList;
	}


	
	/**
 	 * Create data structures with lower & upper bounds for all parameters.
 	 * <b>copied for PrismCL.java</b>
	 * @param pseSwitch
	 * @throws PrismException
	 */
	private void findParameterRanges(String pseSwitch) throws PrismException{
		String[] pseDefs = pseSwitch.split(",");
		pseNames = new String[pseDefs.length];
		pseLowerBounds = new double[pseDefs.length];
		pseUpperBounds = new double[pseDefs.length];
		for (int pdNr = 0; pdNr < pseDefs.length; pdNr++) {
			if (!pseDefs[pdNr].contains("=")) {
				throw new PrismException("No range given for parameter " + pseNames[pdNr]);
			} else {
				String[] pseDefSplit = pseDefs[pdNr].split("=");
				pseNames[pdNr] = pseDefSplit[0].trim();
				String[] upperLower = pseDefSplit[1].split(":");
				if (upperLower.length != 2)
					throw new PrismException("\"" + pseDefSplit[1] + "\" cannot be used as range for parameter " + pseNames[pdNr]);

				try {
					pseLowerBounds[pdNr] = Double.parseDouble(upperLower[0].trim());
					pseUpperBounds[pdNr] = Double.parseDouble(upperLower[1].trim());
				} catch (NumberFormatException e) {
					throw new PrismException(
							"Invalid range \"" + pseDefSplit[1] + "\" for parameter " + pseNames[pdNr] +
							" (bounds must be doubles)");
				}
				if (pseLowerBounds[pdNr] > pseUpperBounds[pdNr]) {
					throw new PrismException(
							"Invalid range \"" + pseDefSplit[1] + "\" for parameter " + pseNames[pdNr] +
							" (lower bound greater than upper)");
				}
			}
		}
	}
	

}
