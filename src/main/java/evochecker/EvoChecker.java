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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import evochecker.auxiliary.Constants;
import evochecker.auxiliary.Utility;
import evochecker.exception.EvoCheckerException;
import evochecker.genetic.GenotypeFactory;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.jmetal.metaheuristics.settings.RandomSearch_Settings;
import evochecker.genetic.problem.GeneticModelProblem;
import evochecker.language.parser.IModelInstantiator;
import evochecker.lifecycle.EvoCheckerInitialiser;
import evochecker.lifecycle.Export;
import evochecker.lifecycle.IUltimate;
import evochecker.properties.Property;
import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;

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
	private String defaultOutputDirectory;

	private static String modelFilenameCli;
	private static String propertiesFilenameCli;
	private static String configFilePathCli;
	private static boolean printHelpCli = false;

	private static IUltimate ultimateInstance;
	private static int currentProgress;
	private boolean paretoFrontPlottingEnabled;

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
		ec.setParetoFrontPlottingEnabled(Boolean.parseBoolean(Utility.getPropertyIgnoreNull(Constants.PLOT_PARETO_FRONT)));

		ec.start();

		try {
			ec.ExportToFile();
		} catch (JMException | EvoCheckerException e) {
			System.err.println("Error exporting results: " + e.getMessage());
			e.printStackTrace();
		}

		if (ec.paretoFrontPlottingEnabled) {
			ec.plotParetoFront();
		}

		ec.printStatistics();
		ec.closeDown();
	}

	public void setUltimateInstance(IUltimate ultimate) {
		ultimateInstance = ultimate;
	}

	public void setUltimateVerificationProperty(String property) {
		ultimateInstance.setVerificationProperty(property);
	}

	public static IUltimate getUltimateInstance() {
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

	public void start() throws EvoCheckerException {
		long start = System.currentTimeMillis();

		// set up initialisation and cmi options
		EvoCheckerInitialiser initialiser = new EvoCheckerInitialiser();

		try {
			// make initialisations

			initialiser.initialiseEvoCheckerOptions();
			initialiser.initializeEvoCheckerProblem();
			initialiser.initialiseEvoCheckerAlgorithm();
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
			defaultOutputDirectory = initialiser.getDefaultOutputDir();

			updateProgress(0);
			solutions = execute();

			long end = System.currentTimeMillis();
			executionTime = (end - start) / 1000.0;

		} catch (Exception e) {
			e.printStackTrace();
			throw new EvoCheckerException("EvoChecker encountered an error during execution: " + e.getMessage());
		}
	}

	/**
	 * Export results to file
	 * 
	 * @throws JMException
	 * @throws EvoCheckerException
	 */
	public void ExportToFile(String directory, boolean makeTemporary) throws JMException, EvoCheckerException {
		String[] files = Export.exportResults(
				objectivesList,
				genes,
				algorithmName,
				problemName,
				solutions,
				directory,
				makeTemporary);

		this.paretoFrontFile = files[0];
		this.paretoSetFile = files[1];
	}

	// override to preserve old behaviour of this method
	public void ExportToFile() throws JMException, EvoCheckerException{
		ExportToFile(defaultOutputDirectory, false);
	}

	public void plotParetoFront() {
		Export.displayParetoFrontPlot(paretoFrontFile, this.getObjectives().size());
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

	public String getAlgorithmName() {
		return algorithmName;
	}

	public String getProblemName() {
		return problemName;
	}

	public Problem getProblem() {
		return problem;
	}

	public String getModelFileName() {
		return modelFilename;
	}

	public String getPropertiesFileName() {
		return propertiesFilename;
	}

	public EvoCheckerType getEvoCheckerType() {
		return ecType;
	}

	public List<AbstractGene> getGenes() {
		return genes;
	}

	public List<Property> getConstraints() {
		return constraintsList;
	}

	public List<Property> getObjectives() {
		return objectivesList;
	}

	protected IModelInstantiator getModelInstantiator() {
		return modelInstantiator;
	}

	public void setAlgorithmName(String algorithmName) {
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

	public int getMaxEvalutations() {
		return Integer.parseInt(Utility.getProperty(Constants.POPULATION_SIZE_KEYWORD));
	}

	public static void updateProgress(int numEvaluations) {
		currentProgress = numEvaluations;
		if (ultimateInstance != null) {
			EvoChecker.ultimateInstance.updateSynthesisProgress(currentProgress);
		}
	}

	public int getCurrentProgress() {
		return currentProgress;
	}

	public void setParetoFrontPlottingEnabled(boolean paretoFrontPlottingOn) {
		this.paretoFrontPlottingEnabled = paretoFrontPlottingOn;
	}

	public List<String> getInternalParameterNames() {
		return GenotypeFactory.getEvolvableNames();
	}

}
