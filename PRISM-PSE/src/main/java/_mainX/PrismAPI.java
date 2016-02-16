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

public class PrismAPI {

	private File propertyFile;
	private File modelFile;
	private final String PRISMOUTPUTFILENAME = "output_Prism.txt";
	
	// PRISM vars
	private PrismLog mainLog;
	private Prism prism;
	private ModulesFile modulesFile;
	private PropertiesFile propertiesFile;
	private String modelString;
	
	private List<Property> propertiesToCheck;

	
	
	// parameter space exploration info
	private String pseTime;
	private double pseAccuracy;
	private String[] pseNames = null;
	private double[] pseLowerBounds = null;
	private double[] pseUpperBounds = null;
	
	
	/**
	 * Class constructor
	 * 
	 * @param modelFile
	 *            - the Markov model file to be provided as input to PRISM
	 * @param propertiesFile
	 *            - the temporal logic file to be provided as input to PRISM
	 */
	public PrismAPI(String propertiesFilename) {
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
	
	
	public PrismAPI(){
		try {
			// initialise PRISM
			mainLog = new PrismFileLog(PRISMOUTPUTFILENAME, false);
			prism = new Prism(mainLog, mainLog);
			prism.initialise();
			prism.setLinEqMethod(1);
			prism.setMaxIters(100000);

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		} 
	}

	
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
	 * This function receives data for the model and returns a double value for
	 * the quantified property.
	 * 
	 * pse.DecompositionProcedure.Type decompositionType, PropertiesFile propertiesFile, Property prop,
	 * String[] paramNames, double[] paramLowerBounds, double[] paramUpperBounds, double accuracy)
	 */
	public void runPrismPSE(pse.DecompositionProcedure.Type pseCheckType) {
		try {
			String pseNames[] 		= new String[]{"c_fail", "c_hw_repair_rate"};
			double pseLowerBounds[]	= new double[]{0.01, 0.5};
			double pseUpperBounds[]	= new double[]{0.1, 0.6};
			double pseAccuracy		= 1000;
			
			for (int i = 0; i < propertiesFile.getNumProperties(); i++) {
				Result res = prism.modelCheckPSE(pseCheckType, propertiesFile, propertiesToCheck.get(i), 
												 pseNames, pseLowerBounds, pseUpperBounds, pseAccuracy);				
//				Result result = prism.modelCheck(propertiesFile,propertiesFile.getProperty(i));
			}
		} 	
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Model checking error");
			System.exit(-1);
		}
	}
	
	
	
	
	
	public void closeDown(){
		if (mainLog!=null)
			mainLog.close();
		modulesFile = null;
		propertiesFile = null;
		propertyFile = null;
//		prism.closeDown();
		prism = null;
	}
	
	
	
	// **************** PRISM-PSE ****************//
	// *******************************************//
	
	public PrismAPI(boolean x){
		
	}
	
	public void launchPrismPSE(String mFilename, String propFilename) throws PrismException, FileNotFoundException{
		PrismLog mainLog = new PrismFileLog(PRISMOUTPUTFILENAME, false);
		
		//prism object
		Prism	 prism	 			= new Prism(mainLog, mainLog);
		//read files
		String modelFilename 		= mFilename;
		String propertiesFilename	= propFilename;
		//pse check type
		DecompositionProcedure.Type pseCheckType = DecompositionProcedure.Type.SIMPLE;
		//pse switch
		String pseSwitch			= "c_fail=0.01:0.1,c_hw_repair_rate=0.5:0.6".trim();
		//pse accuracy
		Double pseAccuracy			= 40.0;
		//initialise prism
		prism.initialise();
		//find parameter ranges
		findParameterRanges(pseSwitch);
		//parse model file
		modulesFile = prism.parseModelFile(new File(modelFilename));
		//parse properties file
		propertiesFile = prism.parsePropertiesFile(modulesFile, new File(propertiesFilename));
		//load model
		prism.loadPRISMModel(modulesFile);
		//properties to check
		propertiesToCheck = new ArrayList<Property>();
		int numPropertiesToCheck = propertiesFile.getNumProperties();
		for (int i = 0; i < numPropertiesToCheck; i++) {
			propertiesToCheck.add(propertiesFile.getPropertyObject(i));
		}
		//parse undefined constants
		Values definedMFConstants = new Values();
		prism.setPRISMModelConstants(definedMFConstants);
//		modulesFile.setUndefinedConstants(null);
		//init results storage
//		ResultsCollection results[] = new ResultsCollection[3];
		//run
		for (int i = 0; i < numPropertiesToCheck; i++) {
			Result res = prism.modelCheckPSE(pseCheckType, propertiesFile, propertiesToCheck.get(i), pseNames, pseLowerBounds, pseUpperBounds, pseAccuracy);
			BoxRegionValues boxresults = (BoxRegionValues)res.getResult();
			System.out.println(boxresults.size() +"\t"+ boxresults.entrySet().size());
			for (Map.Entry<BoxRegion, StateValuesPair> entry : boxresults.entrySet()){
				BoxRegion box = entry.getKey();
				System.out.println(box.toString() +"\t"+ entry.getValue().getMin().getValue(0) +"\t"+ entry.getValue().getMax().getValue(0));
			}
		}
	}

	
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
