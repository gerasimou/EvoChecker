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
package evochecker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.spg.language.parser.ParserEngine;

import evochecker.auxiliary.Constants;
import evochecker.auxiliary.Utility;
import evochecker.exception.EvoCheckerException;
import evochecker.genetic.GenotypeFactory;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.genes.DiscreteDistributionGene;
import evochecker.genetic.jmetal.GeneticProblem;
import evochecker.genetic.jmetal.metaheuristics.MOCell_Settings;
import evochecker.genetic.jmetal.metaheuristics.NSGAII_Settings;
import evochecker.genetic.jmetal.metaheuristics.RandomSearch_Settings;
import evochecker.genetic.jmetal.metaheuristics.SPEA2_Settings;
import evochecker.prism.Property;
import evochecker.prism.PropertyFactory;
import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;

/**
 * Main EvoChecker class
 * @author sgerasimou
 *
 */
public class EvoChecker {

	
	/** problem trying to solve*/
	private Problem problem;
	
	/** properties list*/
	private List<Property> objectivesList;
	private List<Property> constraintsList;

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
	
	/** problem name*/
	private String algorithmName;

	/** problem name*/
	private String problemName;
	

	
	public EvoChecker() {
		
	}
	
	
	/**
	 * Main 
	 * @param args
	 * @throws EvoCheckerException 
	 */	
	public static void main(String[] args) throws EvoCheckerException {
		//check configuration script
		checkConfiguration();

		//instantiate evochecker
		EvoChecker evoChecker = new EvoChecker();
		evoChecker.start();
	}

	
	public void start() {
		long start = System.currentTimeMillis();
		
		try {
			//1) initialise problem
			initializeProblem();
			
			//2) initialise algorithm
			initialiseAlgorithm();
			
			//3) initialise data structures and variables for saving data
			String outputDir = initialiseOutputData();

			//4) execute and save results
			SolutionSet solutions = execute();

			//5) save solutions
			exportResults(outputDir, solutions);
			
			//6) close down
			closeDown();
			
			long end = System.currentTimeMillis();
			System.err.printf("Time:\t%s\n", (end - start)/1000.0);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Initialise the problem and the properties associated with the problem
	 * Note that in the next iteration of this code, 
	 * the initialisation should be done by reading the properties file
	 * @throws Exception
	 */
	private void initializeProblem() throws Exception {
		//1 Get model and properties filenames
		modelFilename 		= Utility.getProperty(Constants.MODEL_FILE_KEYWORD);
		propertiesFilename	= Utility.getProperty(Constants.PROPERTIES_FILE_KEYWORD);
		algorithmName		= Utility.getProperty(Constants.ALGORITHM_KEYWORD).toUpperCase();
		problemName   		= Utility.getProperty(Constants.PROBLEM_KEYWORD).toUpperCase();

		//2) parse model template
		parserEngine 		= new ParserEngine(modelFilename, propertiesFilename);

		//3) create chromosome
		genes				= GenotypeFactory.createChromosome(parserEngine.getEvolvableList());

		//4) create (gene,evolvable element) pairs
		parserEngine.createMapping();
		
		//5) create properties list
		//dummy code to enable parsing properties files when evolvables include an evolvable distribution
		for (AbstractGene gene :genes) {
			if (gene instanceof DiscreteDistributionGene) {
				int numOfOutcomes = ((DiscreteDistributionGene)gene).getNumberOfOutcomes();
				gene.setAllele(new double[numOfOutcomes]);
			}
		}
		String str = parserEngine.getValidModelInstance(genes);
		List<List<Property>> list = PropertyFactory.getObjectivesConstraints(str);
		objectivesList  = list.get(0);
		constraintsList = list.get(1);
		
		for (Property p : objectivesList)
			System.out.println("O: "+p.toString());
		for (Property p : constraintsList)
			System.out.println("C: "+p.toString());

		//6) instantiate the problem
		problem = new GeneticProblem(genes, parserEngine, objectivesList, constraintsList, problemName);
	}	
	
	
	/**
	 * initialise algorithm
	 * @throws Exception
	 */
	private void initialiseAlgorithm() throws Exception{
		
		if (algorithmName != null){
			if (algorithmName.equals(Constants.ALGORITHM.NSGAII.toString())){
				NSGAII_Settings nsgaiiSettings = new NSGAII_Settings(problemName, problem);
				algorithm = nsgaiiSettings.configure();
			}
			else if (algorithmName.equals(Constants.ALGORITHM.RANDOM.toString())){
				RandomSearch_Settings rsSettings = new RandomSearch_Settings(problemName, problem);
				algorithm = rsSettings.configure();
			}
			else if (algorithmName.equals(Constants.ALGORITHM.SPEA2.toString())){
				SPEA2_Settings spea2Settings = new SPEA2_Settings(problemName, problem);
				algorithm = spea2Settings.configure();
			}
			else if (algorithmName.equals(Constants.ALGORITHM.MOCELL.toString())){
				MOCell_Settings mocellSettings = new MOCell_Settings(problemName, problem);
				algorithm = mocellSettings.configure();
			}
//			else if (algorithmStr.equals("SGA")){
//				int numOfConstraints = 0;
//				problem = new GeneticProblemSingle(genes, propertyList, parserEngine, numOfConstraints);
//				SingleGA_Settings sga_setting = new SingleGA_Settings("GeneticProblem", problem);
//				algorithm = sga_setting.configure();
//			}
			else 
				throw new Exception("Algorithm not recognised");
		}
	}
	
		
	/**
	 * Initialise data structure and variables for saving execution results
	 */
	private String initialiseOutputData() {
		//create output dir
		String outputDir = "data" + File.separator 
							+ Utility.getProperty(Constants.PROBLEM_KEYWORD)   + File.separator 
							+ Utility.getProperty(Constants.ALGORITHM_KEYWORD) + File.separator;
		Utility.createDir(outputDir);
		
		return outputDir;

//		int run				= RODESExperimentRuns.getRun();
//		String outputFileSuffix = tolerance +"_"+ epsilon +"_"+ run;

	}
	
	
	/**
	 * Execute
	 * @throws Exception
	 */
	private SolutionSet execute() throws Exception{
		//Execute the Algorithm
		SolutionSet solutions = algorithm.execute();
		
		return solutions;
	}
	
	
	/**
	 * Make finalisations of algorithm
	 */
	private void closeDown(){
	}
	
	
	/**
	 * Export solutions into files
	 * @param population
	 * @throws JMException
	 */
	private void exportResults(String outputDir, SolutionSet solutions) throws JMException {
		//Print results to console
		System.out.println("-------------------------------------------------");
		System.out.println("SOLUTIONS: \t" + solutions.size());

		String identifier	= problemName +"_"+ algorithmName +"_"+ Utility.getTimeStamp();
		solutions.printObjectivesToFile(outputDir + identifier + "_Front");
		solutions.printVariablesToFile(outputDir  + identifier + "_Set");
//		for (int i=0; i<solutions.size(); i++){
//			Solution solution = solutions.get(i);
//			double constraintValue = solution.getOverallConstraintViolation();
//			if (constraintValue<0){
//				System.out.println(constraintValue +"\t"+ Arrays.toString(solution.getDecisionVariables()));
//			}
//		}
		
	}
	

	/**
	 * Check whether the experiment has been configured correctly 
	 * @throws EvoCheckerException 
	 */
	private static void checkConfiguration() throws EvoCheckerException {
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
	
	
	
	public void executeRandomSearch() throws FileNotFoundException, IOException {
		Algorithm algorithm;
		try {
			RandomSearch_Settings rsSettings = new RandomSearch_Settings("GeneticProblem", problem);
			algorithm = rsSettings.configure();
			// Execute the Algorithm
			SolutionSet population = algorithm.execute();
			System.out.println("-------------------------------------------------");
			System.out.println("SOLUTION: \t" + population.size());
			population.printObjectivesToFile("data/FUN_Random");
			population.printVariablesToFile("data/VAR_Random");
			for (int i=0; i<population.size(); i++){
				Solution solution = population.get(i);
				double constraintValue = solution.getOverallConstraintViolation();
				if (constraintValue<0){
					System.out.println(constraintValue +"\t"+ Arrays.toString(solution.getDecisionVariables()));
				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	 
	private void setupIndicators(Algorithm algorithm, Problem problem, String paretoFrontFile){
		// Object to get quality indicators
		  QualityIndicator indicators ;
		  indicators = new QualityIndicator(problem, paretoFrontFile);
		  
		  // Add the indicator object to the algorithm
		    algorithm.setInputParameter("indicators", indicators) ;  
	}
}
