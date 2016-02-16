package _mainX;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import parser.ast.ModulesFile;
import parser.ast.PropertiesFile;
import parser.ast.Property;
import prism.Prism;
import prism.PrismFileLog;
import prism.PrismLog;
import prism.Result;

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
	
	
	
	
	public PrismAPI(String propertiesFilename, String modelFilename){
		try {
			this.propertyFile 		= new File(propertiesFilename);
			this.modelFile 			= new File(modelFilename);			
			
			// initialise PRISM
			mainLog = new PrismFileLog(PRISMOUTPUTFILENAME, false);
			prism = new Prism(mainLog, mainLog);
			prism.initialise();
			
			// and build the model
			modulesFile = prism.parseModelFile(modelFile);
//			modulesFile.setUndefinedConstants(null);
			propertiesFile = prism.parsePropertiesFile(modulesFile,propertyFile);
//			propertiesFile.setUndefinedConstants(null);
			prism.buildModel(modulesFile);
			
			// setup properties to check
			this.propertiesToCheck	= new ArrayList<Property>();
			for (int i=0; i<propertiesFile.getNumProperties(); i++){
				propertiesToCheck.add(propertiesFile.getPropertyObject(i));
			}

		} 
		catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		} 

	}

}
