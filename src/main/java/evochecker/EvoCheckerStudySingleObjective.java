package evochecker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import evochecker.auxiliary.Utility;
import evochecker.genetic.GenotypeFactory;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.jmetal.GeneticProblem;
import evochecker.genetic.jmetal.experiments.Experiment;
import evochecker.genetic.jmetal.single.AlgorithmSteps;
import evochecker.genetic.jmetal.single.GeneticProblemSingle;
import evochecker.genetic.jmetal.single.GeneticProblemSingleFX;
import evochecker.genetic.jmetal.single.RandomSearchSingle_Settings;
import evochecker.genetic.jmetal.single.SingleGA_Settings;
import evochecker.parser.ParserEngine;
import evochecker.prism.Property;
import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.experiments.Settings;
import jmetal.util.JMException;

public class EvoCheckerStudySingleObjective extends Experiment{
	
	/** properties handler*/
	private static Properties prop = new Properties();

	/** properties list*/
	private List<Property> propertyList;
	
	/** problem trying to solve*/
	private Problem problem;
	
	/** problem genes*/
	private List<AbstractGene> genes = new ArrayList<AbstractGene>();
	
	/** parser engine handler*/
	private ParserEngine parserEngine;
	
	/** model filename*/
	private String 		modelFilename;		
	
	/** property filename*/
	private String 		propertiesFilename;
	
	private int times = 0;
	
	
	  
	  public static void main(String[] args) throws IOException{
		  
		  prop.load(new FileInputStream("res/config.properties"));

		  int runs = Integer.parseInt(Utility.getProperty("RUNS"));
		  
//		  for (int runNum=0; runNum<runs; runNum++){
			  long start = System.currentTimeMillis();
		  
			  EvoCheckerStudySingleObjective evoStudy = new EvoCheckerStudySingleObjective();
			  try {
				
				  //init evoChecker
				  evoStudy.initializeProblem();
				  //give a name for the study
				  evoStudy.experimentName_ 	= Utility.getProperty("EXPERIMENT");
				  //name the algorithms
				  evoStudy.algorithmNameList_ = new String[]{"pgGA", "pRandomSearchSingle"};
//				  evoStudy.algorithmNameList_ = new String[]{Utility.getProperty("ALGORITHM").toUpperCase()};
				  //name the problem
				  evoStudy.problemList_		= new String[]{"GeneticProblemSingle"};
//				  name the pareto front - empty if unknown
				  evoStudy.paretoFrontFile_	= new String[1];//Space allocation for 1 front;
//				  name the indicators
//				  evoStudy.indicatorList_		= new String[]{"HV", "GD", "IGD", "GENSPREAD", "EPSILON", };		  
				  
				  //output directory
				  String outputDir					= Utility.getProperty("OUTPUTDIR");
				  if (outputDir == null || (!new File(outputDir).exists()))
					  throw new FileNotFoundException("Output directory not specified");
				  evoStudy.experimentBaseDirectory_	= outputDir + evoStudy.experimentName_;
				  //pareto front directory - empty if unknown
				  evoStudy.paretoFrontDirectory_ 	= "";
				  
				  //init the array containing the settings of the algorithms
				  int numOfAlgorithms			= evoStudy.algorithmNameList_.length;
				  evoStudy.algorithmSettings_	= new Settings[numOfAlgorithms];
				  
				  //set the runs
				  evoStudy.independentRuns_		= runs;
				  
				  //init the experiment
				  evoStudy.initExperiment();
				  
				  //Run the experiments - number of threads
				  evoStudy.runExperiment(1);
				  
				  //Generate quality indicators
//				  evoStudy.generateQualityIndicators();
	
//				  Generate latex tables
//				  evoStudy.generateLatexTables();			
			} 
			  catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
			  
			long end = System.currentTimeMillis();
				
			//export time taken to file
			long timeUsed = (end - start)/1000;
//			System.err.println("Run " + runNum +":\t" + timeUsed);
//			String fileName = Utility.getProperty("OUTPUTDIR") + Utility.getProperty("EXPERIMENT") + "/run.csv";
//			Utility.exportToFile(fileName, timeUsed+"");
//		  }
	  }

	  
	  
	  
	/**
	 * Initialise the problem and the properties associated with the problem
	 * Note that in the next iteration of this code, 
	 * the initialisation should be done by reading the properties file
	 * @throws Exception
	 */
	private void initializeProblem() throws Exception {
		//1 Get model and properties filenames
		modelFilename 		= Utility.getProperty("MODEL_TEMPLATE_FILE");
		propertiesFilename	= Utility.getProperty("PROPERTIES_FILE");
		
		//2) parse model template
		parserEngine 		= new ParserEngine(modelFilename, propertiesFilename);
		
		//3) create chromosome
		genes				= GenotypeFactory.createChromosome(parserEngine.getEvolvableList());
		
		//4) create (gene,evolvable element) pairs
		parserEngine.createMapping();
		
		//5) create properties list
		propertyList = new ArrayList<Property>();
			
		//FX
		propertyList.add(new Property(true));
		propertyList.add(new Property(false));
		propertyList.add(new Property(false));
		propertyList.add(new Property(true));
		int numOfConstraints = 1;
	
	
		//6) instantiate the problem
		problem = new GeneticProblemSingleFX(genes, propertyList, parserEngine, numOfConstraints);
	}
		
  
	/**
	 * Setup algorithm settings and initialise an algorithm, when needed
	 */
	public synchronized void algorithmSettings(String problemName, int problemIndex, Algorithm[] algorithm)  throws ClassNotFoundException {  	
		try{
			int numberOfAlgorithms = algorithmNameList_.length;
	
		    HashMap[] parameters = new HashMap[numberOfAlgorithms];
			for (int i = 0; i < numberOfAlgorithms; i++) {
				parameters[i] = new HashMap();
			}// for

			if (!(paretoFrontFile_[problemIndex] == null) && !paretoFrontFile_[problemIndex].equals("")) {
				for (int i = 0; i < numberOfAlgorithms; i++)
					parameters[i].put("paretoFrontFile_", paretoFrontFile_[problemIndex]);
			} // if

			// set the algorithms
			switch (times % numberOfAlgorithms) {
				case 0: 	{ algorithm[0] = new SingleGA_Settings(problemName, problem).configure();
							  ((AlgorithmSteps)algorithm[0]).initialise(); //only for single-objective algorithms
							  break;
							}
				case 1: 	{ 	algorithm[1] = new RandomSearchSingle_Settings(problemName, problem).configure();
				  				((AlgorithmSteps)algorithm[1]).initialise(); //only for single-objective algorithms
								break;
							}
				default:	{   throw new JMException("Error in algorithmSettings()");
							}
			}
			times++;
		} catch (IllegalArgumentException ex) {
			Logger.getLogger(EvoCheckerStudySingleObjective.class.getName()).log(Level.SEVERE, null, ex);
		} catch (JMException ex) {
			Logger.getLogger(EvoCheckerStudySingleObjective.class.getName()).log(Level.SEVERE, null, ex);
		}
	} // algorithmSettings
	
	
	/** get property handler*/
	public static Properties getProp() {
		return prop;
	}

	
	
}
