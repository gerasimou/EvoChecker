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
import evochecker.genetic.problem.GeneticProblemParametric2;
import evochecker.language.parser.IModelInstantiator;
import evochecker.language.parser.ModelInstantiator;
import evochecker.language.parser.ModelInstantiatorParametric;
import evochecker.plotting.PlotFactory;
import evochecker.properties.Property;
import evochecker.properties.PropertyFactory;
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
	private IModelInstantiator modelInstantiator;

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
	
	/** Pareto front filename*/
	private String paretoFrontFile;

	/** Pareto set filename*/
	private String paretoSetFile;

	
	/** */
	private EvoCheckerType ecType;

	/** */
	private double executionTime;
	
	
	public EvoChecker() {
		
	}
	
	
	/**
	 * Main 
	 * @param args
	 * @throws EvoCheckerException 
	 */	
	public static void main(String[] args) throws EvoCheckerException {
		//instantiate evochecker
		EvoChecker ec = new EvoChecker();
		
		if (args.length > 0)
			ec.setConfigurationFile(args[0]);
		else 
			//use default config file
			ec.setConfigurationFile("config.properties");
		
		ec.start();
	}
	
	
	public void setConfigurationFile(String configFile) {
		Utility.setPropertiesFile(configFile);
	}

	
	public void start() {
		long start = System.currentTimeMillis();
		
		try {
			//0) check configuration script
			ConfigurationChecker.checkConfiguration();

			//1) initialise problem
			initializeProblem();
			
			//2) initialise algorithm
			initialiseAlgorithm();
			
			//3) initialise data structures and variables for saving data
			String outputDir = initialiseOutputData();

			//4) execute and save results
			SolutionSet solutions = execute();

			long end = System.currentTimeMillis();
			executionTime = (end - start)/1000.0;

			//5) save solutions
			exportResults(outputDir, solutions);
			
			//6) close down
			closeDown();
			
			System.err.printf("Time:\t%s\n", executionTime);
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
		modelFilename 		= new File(Utility.getProperty(Constants.MODEL_FILE_KEYWORD)).getAbsolutePath();
		propertiesFilename	= new File(Utility.getProperty(Constants.PROPERTIES_FILE_KEYWORD)).getAbsolutePath();
		algorithmName		= Utility.getProperty(Constants.ALGORITHM_KEYWORD).toUpperCase();
		problemName   		= Utility.getProperty(Constants.PROBLEM_KEYWORD).toUpperCase();
		
		switch (EvoCheckerType.valueOf(Utility.getPropertyIgnoreNull(Constants.EVOCHECKER_TYPE).toUpperCase())) {
			case NORMAL		: ecType = EvoCheckerType.NORMAL; break;
			case PARAMETRIC	: ecType = EvoCheckerType.PARAMETRIC; break;
			case REGION		: ecType = EvoCheckerType.REGION; 
							  throw new EvoCheckerException("EvoChecker Region is still in development!. Exiting");			
		}

		//2) parse model template
		switch (ecType) {
			case NORMAL		: modelInstantiator = new ModelInstantiator(modelFilename, propertiesFilename); break;
			case PARAMETRIC	: modelInstantiator = new ModelInstantiatorParametric(modelFilename, propertiesFilename);break;
			case REGION		: throw new EvoCheckerException("EvoChecker Region is still in development!. Exiting");			
		}

		//3) create chromosome
		genes				= GenotypeFactory.createChromosome(modelInstantiator.getEvolvableList());

		//4) create (gene,evolvable element) pairs
		modelInstantiator.createMapping();
		
		//5) create properties list
		String str = modelInstantiator.getConcreteModel(genes);
		List<List<Property>> list = PropertyFactory.getObjectivesConstraints(str);
		objectivesList  = list.get(0);
		constraintsList = list.get(1);
		
		System.out.println("Objectives (O)/Constraints(C)");
		for (Property p : objectivesList)
			System.out.print("O: "+p.toString());
		for (Property p : constraintsList)
			System.out.print("C: "+p.toString());
		System.out.println();
		
		//6) instantiate the problem
		switch (ecType) {
			case NORMAL		: problem = new GeneticProblem(genes, modelInstantiator, objectivesList, constraintsList, problemName); break;
			case PARAMETRIC	: problem = new GeneticProblemParametric2 (genes, modelInstantiator, objectivesList, constraintsList, problemName);break;
			case REGION		: throw new EvoCheckerException("EvoChecker Region is still in development!. Exiting");			
		}
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
		FileUtil.createDir(outputDir);
		
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
		System.out.println("Starting  evolution");
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
		String frontFile	= outputDir + identifier + "_Front";
		String setFile		= outputDir  + identifier + "_Set";
		
		//generate and save headers
//		StringBuilder setHeader = new StringBuilder();
//		for (AbstractGene gene : genes)
//			setHeader.append(gene.getName() +" ");
		String setHeader = String.join(" ", GenotypeFactory.getEvolvableNames());
		FileUtil.saveToFile(setFile, setHeader +"\n", true);
		StringBuilder frontHeader = new StringBuilder();
		Iterator<Property> it = objectivesList.iterator();
		while (it.hasNext()) {
//		for (Property p : objectivesList) {
			Property p = it.next();
			frontHeader.append(p.getExpression());
			if (it.hasNext())
				frontHeader.append("\t");
		}
		FileUtil.saveToFile(frontFile, frontHeader.toString(), true);
		
		List<Solution> solutionList = new ArrayList<Solution>();
		for (int i=0; i<solutions.size(); i++)
			solutionList.add(solutions.get(i));
		Utility.printObjectivesToFile(frontFile, solutionList, objectivesList);
		Utility.printVariablesToFile2(setFile, solutionList, GenotypeFactory.getGeneEvolvableMap(), genes);
		
		//Assign 
		paretoFrontFile  = frontFile;
		paretoSetFile	 = setFile; 
//		solutions.printObjectivesToFile(frontFile);
//		solutions.printVariablesToFile(setFile);
		
		System.out.println("\nPareto Front and Pareto set saved at: " + outputDir);
		System.out.println("Pareto Front: " + frontFile);
		System.out.println("Pareto Set: "   + setFile);
		
		
		//show Pareto front plot if specified in configuration file
		boolean plotParetoFront = Boolean.parseBoolean(Utility.getProperty(Constants.PLOT_PARETO_FRONT));
		if (plotParetoFront)
			PlotFactory.plotParetoFront(frontFile, identifier, objectivesList.size());
	}

	
	/**
	 * Return Pareto front file name
	 * @return
	 */
	public String getParetoFrontFileName() {
		return paretoFrontFile;
	}
	

	/**
	 * Return Pareto set file name
	 * @return
	 */
	public String getParetoSetFileName() {
		return paretoSetFile;
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
	
	 
	@SuppressWarnings("unused")
	private void setupIndicators(Algorithm algorithm, Problem problem, String paretoFrontFile){
		// Object to get quality indicators
		  QualityIndicator indicators ;
		  indicators = new QualityIndicator(problem, paretoFrontFile);
		  
		  // Add the indicator object to the algorithm
		    algorithm.setInputParameter("indicators", indicators) ;  
	}
	
	
	public double getExecutionTime() {
		return executionTime;
	}
	
	
	public void setProperty(String key, String value) {
		try {
			Utility.setProperty(key, value);
		} catch (EvoCheckerException e) {
			e.printStackTrace();
		}
	}
}
