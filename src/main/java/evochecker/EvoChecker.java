//==============================================================================
//	
//	Copyright (c) 2015-
//	Authors:
//	* Simos Gerasimou (University of York)
//  * Faisal Alhwikem (University of York)
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import evochecker.auxiliary.ConfigurationChecker;
import evochecker.auxiliary.Constants;
import evochecker.auxiliary.FileUtil;
import evochecker.auxiliary.Utility;
import evochecker.exception.EvoCheckerException;
import evochecker.genetic.GenotypeFactory;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.jmetal.metaheuristics.settings.MOCell_Settings;
import evochecker.genetic.jmetal.metaheuristics.settings.NSGAII_Settings;
import evochecker.genetic.jmetal.metaheuristics.settings.RandomSearch_Settings;
import evochecker.genetic.jmetal.metaheuristics.settings.SPEA2_Settings;
import evochecker.genetic.problem.GeneticModelProblem;
import evochecker.genetic.problem.GeneticProblem;
import evochecker.genetic.problem.GeneticProblemParametric;
import evochecker.genetic.problem.GeneticProblemParametricParallel;
import evochecker.language.parser.IModelInstantiator;
import evochecker.language.parser.ModelInstantiatorUltimate;
import evochecker.language.parser.ModelInstantiatorParametric;
import evochecker.lifecycle.EvoCheckerInitialiser;
import evochecker.lifecycle.Export;
import evochecker.plotting.PlotFactory;
import evochecker.properties.Property;
import evochecker.properties.PropertyFactory;
import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;

import ultimate.Ultimate;

/**
 * Main EvoChecker class
 * 
 * @author sgerasimou
 *
 */
public class EvoChecker {

	/** problem trying to solve */
	private Problem problem;

	/** properties list */
	private List<Property> objectivesList;
	private List<Property> constraintsList;

	/** problem genes */
	private List<AbstractGene> genes = new ArrayList<AbstractGene>();

	/** parser engine handler */
	private IModelInstantiator modelInstantiator;

	/** model filename */
	private String modelFilename;

	/** property filename */
	private String propertiesFilename;

	/** algorithm to be executed */
	private Algorithm algorithm;

	/** problem name */
	private String algorithmName;

	/** problem name */
	private String problemName;

	/** Pareto front filename */
	private String paretoFrontFile;

	/** Pareto set filename */
	private String paretoSetFile;

	/** Solution set */
	private SolutionSet solutions;

	/** */
	private EvoCheckerType ecType;

	/** */
	private double executionTime;

	/** */
	private String outputDir;

	private static Boolean commandLineInvoked = false;

	private static String modelFilenameCli;
	private static String propertiesFilenameCli;
	private static String configFilePathCli;
	private static boolean printHelpCli = false;

	private static Ultimate ultimateInstance;

	public EvoChecker() {

	}

	private static Options options = new Options();

	// private static SharedContext sharedContext = SharedContext.getInstance();

	private static void setUpCLI() {
		Option help = new Option("help", "Prints usage help information");

		Option projectFile = Option.builder("pf")
				.argName("path")
				.hasArg()
				.desc("Path to the EvoChecker model file. If specified, will override any in the config file.")
				.build();

		Option property = Option.builder("p")
				.argName("path")
				.hasArg()
				.desc("Path to a file containing EvoChecker objectives and constraints. If specified, will override any in the config file.")
				.build();

		Option configFile = Option.builder("cf")
				.argName("path")
				.hasArg()
				.desc("Path to the configuration file")
				.build();

		options.addOption(help);
		options.addOption(projectFile);
		options.addOption(property);
		options.addOption(configFile);
	}

	private static void parseArgs(String[] args) {
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(options, args);
			modelFilenameCli = line.getOptionValue("pf");
			propertiesFilenameCli = line.getOptionValue("p");
			configFilePathCli = line.getOptionValue("cf");

			if (line.hasOption("help")) {
				printHelpCli = true;
			} else if (modelFilenameCli == null || propertiesFilenameCli == null || configFilePathCli == null) {
				System.out.println("One or more options was not specified.");
				printHelpCli = true;
			}

		} catch (ParseException e) {
			System.err.println("Parsing failed.  Reason: " + e.getMessage());
		}
	}

	/**
	 * Main
	 * 
	 * @param args
	 * @throws EvoCheckerException
	 */
	public static void main(String[] args) throws EvoCheckerException {

		EvoChecker ec = new EvoChecker();
		// Ultimate ultimate = new Ultimate();

		// ultimate.loadProjectFromFile("/home/brendandevlin-hill/ultimate_casino/casino.ultimate");
		// ultimate.setModelID("casino");
		// HashMap<String, String> internalParams = new HashMap<>();
		// internalParams.put("weighting", "0.6");
		// ultimate.setInternalParameters(internalParams);
		// try {
		// ultimate.execute();
		// } catch (IOException e) {
		// System.err.println("Error executing Ultimate: " + e.getMessage());
		// return;
		// }
		// System.out.println(ultimate.getResultsInfo());

		if (args.length > 0) {
			System.out.println("EvoChecker command line arguments: " + Arrays.toString(args));
			setUpCLI();
			parseArgs(args);
			commandLineInvoked = true;
			if (printHelpCli) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("headless", options);
				return;
			}
			ec.setConfigurationFile(configFilePathCli, modelFilenameCli, propertiesFilenameCli);
		} else {
			// use default config file
			ec.setConfigurationFile("config.properties");
		}

		ec.start();

		try {
			ec.ExportToFile();
		} catch (JMException | EvoCheckerException e) {
			System.err.println("Error exporting results: " + e.getMessage());
			e.printStackTrace();
		}

		ec.printStatistics();
		ec.closeDown();
	}

	public void setUltimateInstance(Ultimate ultimate){
		ultimateInstance = ultimate;
	}

	public void setUltimateVerificationProperty(String property){
		ultimateInstance.setVerificationProperty(property);
	}

	public static Ultimate getUltimateInstance(){
		return ultimateInstance;
	}

	public void setConfigurationFile(String configFile) {
		setConfigurationFile(configFile, null, null);
	}

	public void setConfigurationFile(String configFile, String modelFile, String propertyFile) {
		try {
			Utility.setPropertiesFile(configFile);
			if (modelFile != null) {
				Utility.setModelFileOverride(modelFile);
			}
			if (propertyFile != null) {
				Utility.setPropertiesFileOverride(propertyFile);
			}
		} catch (EvoCheckerException e) {
			System.out.println("Error setting configuration: " + e.getMessage());
		}
	}

	public void start() {
		long start = System.currentTimeMillis();

		// set up initialisation and cmi options
		EvoCheckerInitialiser initialiser = new EvoCheckerInitialiser();

		try {
			// make initialisations

			System.out.println("Initialising options...");
			initialiser.initialiseEvoCheckerOptions();
			System.out.println("Initialising problem...");
			initialiser.initializeEvoCheckerProblem();
			System.out.println("Initialising algorithm...");
			initialiser.initialiseEvoCheckerAlgorithm();
			System.out.println("Initialising output...");
			initialiser.initialiseOutputData();

			modelFilename = initialiser.getModelFilename();
			propertiesFilename = initialiser.getPropertiesFilename();
			algorithmName = initialiser.getAlgorithmName();
			problemName = initialiser.getProblemName();
			ecType = initialiser.getEcType();

			modelInstantiator = initialiser.getModelInstantiator();
			genes = initialiser.getGenes();
			objectivesList = initialiser.getObjectivesList();
			constraintsList = initialiser.getConstraintsList();
			problem = initialiser.getProblem();

			algorithm = initialiser.getAlgorithm();

			outputDir = initialiser.getOutputDir();

			System.out.println("Executing EvoChecker...");
			solutions = execute();

			long end = System.currentTimeMillis();
			executionTime = (end - start) / 1000.0;
			System.out.printf("Time:\t%s\n", executionTime);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}




	/**
	 * Export results to file
	 * 
	 * @throws JMException
	 * @throws EvoCheckerException
	 */
	public void ExportToFile() throws JMException, EvoCheckerException {
		Export.exportResults(
				objectivesList,
				genes,
				algorithmName,
				problemName,
				solutions,
				outputDir);
	}

	/**
	 * Execute
	 * 
	 * @throws Exception
	 */
	protected SolutionSet execute() throws Exception {
		// Execute the Algorithm
		SolutionSet solutions = algorithm.execute();

		return solutions;
	}

	/**
	 * Make finalisations of algorithm
	 */
	private void closeDown() {

	}

	// what are these unused methods for?
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
			for (int i = 0; i < population.size(); i++) {
				Solution solution = population.get(i);
				double constraintValue = solution.getOverallConstraintViolation();
				if (constraintValue < 0) {
					System.out.println(constraintValue + "\t" + Arrays.toString(solution.getDecisionVariables()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private void setupIndicators(Algorithm algorithm, Problem problem, String paretoFrontFile) {
		// Object to get quality indicators
		QualityIndicator indicators;
		indicators = new QualityIndicator(problem, paretoFrontFile);

		// Add the indicator object to the algorithm
		algorithm.setInputParameter("indicators", indicators);
	}

	public void setProperty(String key, String value) {
		try {
			Utility.setProperty(key, value);
		} catch (EvoCheckerException e) {
			e.printStackTrace();
		}
	}

	public String getProperty(String propName) {
		return Utility.getProperty(propName);
	}

	public String getParetoFrontFile() {
		return this.paretoFrontFile;
	}

	public String getParetoSetFile() {
		return this.paretoSetFile;
	}

	public int getObjectivesNum() {
		return this.objectivesList.size();
	}

	public int getConstraintsNum() {
		return this.constraintsList.size();
	}

	public double getExecutionTime() {
		return executionTime;
	}

	/**
	 * Get statistics
	 */
	public String getStatistics() {
		return ((GeneticModelProblem) problem).getStatistics();
	}

	/**
	 * Print statistics
	 */
	public void printStatistics() {
		System.out.println(getStatistics());
	}

	/**
	 * Return Pareto front file name
	 * 
	 * @return
	 */
	public String getParetoFrontFileName() {
		return paretoFrontFile;
	}

	/**
	 * Return Pareto set file name
	 * 
	 * @return
	 */
	public String getParetoSetFileName() {
		return paretoSetFile;
	}

	protected Algorithm getAlgorithm() {
		return algorithm;
	}

	protected String getAlgorithmName() {
		return algorithmName;
	}

	protected String getProblemName() {
		return problemName;
	}

	public Problem getProblem() {
		return problem;
	}

	protected String getModelFileName() {
		return modelFilename;
	}

	protected String getPropertiesFileName() {
		return propertiesFilename;
	}

	protected EvoCheckerType getEvoCheckerType() {
		return ecType;
	}

	protected List<AbstractGene> getGenes() {
		return genes;
	}

	protected List<Property> getConstraints() {
		return constraintsList;
	}

	public List<Property> getObjectives() {
		return objectivesList;
	}

	protected IModelInstantiator getModelInstantiator() {
		return modelInstantiator;
	}

	protected void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}

	protected void setProblemName(String problemName) {
		this.problemName = problemName;
	}

	protected void setProblem(Problem problem) {
		this.problem = problem;
	}

	protected void setModelFileName(String modelFileName) {
		this.modelFilename = modelFileName;
	}

	protected void setPropertiesFileName(String propertiesFileName) {
		this.propertiesFilename = propertiesFileName;
	}

	protected void setEvoCheckerType(EvoCheckerType type) {
		this.ecType = type;
	}

	protected void setGenes(List<AbstractGene> genes) {
		this.genes = genes;
	}

	protected void setModelInstantiator(IModelInstantiator modelInstantiator) {
		this.modelInstantiator = modelInstantiator;
	}

	protected void setObjectives(List<Property> objectives) {
		this.objectivesList = objectives;
	}

	protected void setConstraints(List<Property> constraints) {
		this.constraintsList = constraints;
	}

	public SolutionSet getSolutions() {
		return this.solutions;
	}

}
