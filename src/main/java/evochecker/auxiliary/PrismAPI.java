package evochecker.auxiliary;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import parser.ast.ModulesFile;
import parser.ast.PropertiesFile;
import prism.Prism;
import prism.PrismFileLog;
import prism.PrismLog;
import prism.Result;

public class PrismAPI {

	private String PROPERTIESFILENAME;
	private final String PRISMOUTPUTFILENAME = "output_Prism.txt";
	// PRISM vars
	private PrismLog mainLog;
	private Prism prism;
	private ModulesFile modulesFile;
	private PropertiesFile propertiesFile;
	private String modelString;
	private File propertyFile;
//	private static PrismAPI instance;


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
			this.PROPERTIESFILENAME = propertiesFilename;
			this.propertyFile = new File(PROPERTIESFILENAME);

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
	
	
	public PrismAPI(){
		this(null);
	}
	
	
	public void setPropertiesFile(String propertiesFilename){
		this.PROPERTIESFILENAME = propertiesFilename;
		this.propertyFile = new File(PROPERTIESFILENAME);
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
	
	
	public void closeDown(){
		if (mainLog!=null)
			mainLog.close();
		modulesFile = null;
		propertiesFile = null;
		propertyFile = null;
		prism = null;
	}

}
