package evochecker.auxiliary;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import evochecker.genetic.GenotypeFactory;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.jmetal.GeneticProblem;
import evochecker.genetic.jmetal.metaheuristics.NSGAII_Settings;
import evochecker.genetic.jmetal.single.AlgorithmSteps;
import evochecker.genetic.jmetal.single.GeneticProblemSingleFX;
import evochecker.genetic.jmetal.single.GeneticProblemSingleUUV;
import evochecker.parser.ParserEngine;
import evochecker.prism.Property;
import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.util.JMException;

public class ExhaustiveSearchMain {

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
	
	/** algorithm to be executed*/
	private Algorithm algorithm;
		
	/** adaptation step*/
	public static int adaptationStep;
	
	/** get property handler*/
	public static Properties getProp() {
		return prop;
	}
	
	
	public ExhaustiveSearchMain() {
		// TODO Auto-generated constructor stub
	}

	
	
	
	
	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		long start = System.currentTimeMillis();

		try {
			prop.load(new FileInputStream("res/config.properties"));
			
			//instantiate evochecker
			ExhaustiveSearchMain exSearch = new ExhaustiveSearchMain();

			//initialise problem
			exSearch.initializeProblem();
			
			//initialise algorithm
			exSearch.initialiseAlgorithm();
			
			//execute adaptation step
			String str 	= Utility.getProperty("MODEL_TEMPLATE_FILE");
			str			= StringUtils.replace(str, "UUV/", "UUV/runtime/");
			str 		= StringUtils.replace(str, ".", "1.");
			exSearch.adaptSystem(str);
			
			//close down
			exSearch.closeDown();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		
		System.err.println("Time:\t" + (end - start)/1000);
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
//		propertyList.add(new Property(true));
//		propertyList.add(new Property(false));
//		propertyList.add(new Property(false));
//		propertyList.add(new Property(true));
//		int numOfConstraints = 1;
		
		//UUV
		propertyList.add(new Property(true));
		propertyList.add(new Property(false));
		propertyList.add(new Property(false));
		propertyList.add(new Property(false));
		propertyList.add(new Property(false));
		int numOfConstraints = 2;
		

		//6) instantiate the problem
		if ( Utility.getProperty("ALGORITHM").toUpperCase().equals("EX_SEARCH")){
			if (modelFilename.toUpperCase().contains("UUV"))
				problem = new GeneticProblemSingleUUV(genes, propertyList, parserEngine, numOfConstraints);
			else 
				problem = new GeneticProblemSingleFX(genes, propertyList, parserEngine, numOfConstraints);
		}
		else{
			throw new Exception("Algorithm not recognised");
		}
	}

	
	/**
	 * initialise algorithm
	 * @throws Exception
	 */
	private void initialiseAlgorithm() throws Exception{
		String algorithmStr = Utility.getProperty("ALGORITHM").toUpperCase();
		if (algorithmStr != null){
			if (algorithmStr.equals("EX_SEARCH")){
				algorithm = new ExhaustiveSearch("GeneticProblem", problem, parserEngine.getEvolvableList());
				((AlgorithmSteps)algorithm).initialise(); //only for single-objective algorithms
			}
			else 
				throw new Exception("Algorithm not recognised");
		}
	}
	
	
	
	/**
	 * Perform an adaptation scenario
	 * @param modelFilename
	 * @throws Exception
	 */
	private void adaptSystem(String modelFilename) throws Exception{		
		while (new File(modelFilename).exists()){
			System.out.println(modelFilename);
			parserEngine.updateInternalModelRepresentation(Utility.readFile(modelFilename));
			execute();
			modelFilename = StringUtils.replace(modelFilename, adaptationStep + ".", ++adaptationStep + ".", 1);
		}
	}
	
	
	
	/**
	 * Execute
	 * @throws Exception
	 */
	private void execute() throws Exception{
		
		// Execute the Algorithm
		SolutionSet population = algorithm.execute();
 
		
		//Print results to console
		System.out.println("-------------------------------------------------");
		System.out.println("SOLUTION: \t" + population.size());
		for (int i=0; i<population.size(); i++){
			Solution solution = population.get(i);
			for (int objective=0; objective<solution.getNumberOfObjectives(); objective++){
				System.out.printf("%.3f\t", solution.getObjective(objective));
			}
			double constraintValue = solution.getOverallConstraintViolation();
			if (constraintValue<0){
				System.out.println(constraintValue +"\t"+ Arrays.toString(solution.getDecisionVariables()));
			}
		}
		
		//Store results
		String algorithmStr = Utility.getProperty("ALGORITHM").toUpperCase();
		String seeding = Utility.getProperty("SEEDING").toUpperCase();
		Utility.exportToFile("data/FUN_"+algorithmStr +"_"+ seeding, population.get(0).toString(), true);
		Utility.printVariablesToFile("data/VAR_"+algorithmStr +"_"+ seeding, population.get(0), true);
	}	
	
	
	/**
	 * Make finalisations of algorithm
	 */
	private void closeDown(){
		//only for single-objective algorithms
		if (algorithm instanceof AlgorithmSteps)
			((AlgorithmSteps)algorithm).finalise(); 
	}
}
