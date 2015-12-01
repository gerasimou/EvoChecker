package evochecker;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.QualityIndicator;
import evochecker.auxiliary.Utility;
import evochecker.genetic.GenotypeFactory;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.jmetal.GeneticProblem;
import evochecker.genetic.jmetal.metaheuristics.MOCell_Settings;
import evochecker.genetic.jmetal.metaheuristics.NSGAII_Settings;
import evochecker.genetic.jmetal.metaheuristics.RandomSearch_Settings;
import evochecker.genetic.jmetal.metaheuristics.SPEA2_Settings;
import evochecker.genetic.jmetal.single.GeneticProblemSingle;
import evochecker.genetic.jmetal.single.SingleGA_Settings;
import evochecker.parser.ParserEngine;
import evochecker.parser.evolvable.Evolvable;
import evochecker.prism.Property;

public class EvoChecker {

	private static Properties prop = new Properties();

	private List<Property> propertyList;
	private Problem problem;
	private List<AbstractGene> genes = new ArrayList<AbstractGene>();
	
	private ParserEngine parserEngine;
	private String 		modelFilename;		//= "models/DPM/dpm.pm";
	private String 		propertiesFilename;// 	= "models/DPM/dpm.pctl";
	
	
	
	public static Properties getProp() {
		return prop;
	}

	
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		try {
			prop.load(new FileInputStream("res/config.properties"));
			
			EvoChecker evoChecker = new EvoChecker();
			evoChecker.modelFilename 		= Utility.getProperty("MODEL_TEMPLATE_FILE","models/DPM/dpm.pm");
			evoChecker.propertiesFilename	= Utility.getProperty("PROPERTIES_FILE", "models/DPM/dpm.pctl");
			evoChecker.initialize();
			
			evoChecker.execute();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		
		System.err.println("Time:\t" + (end - start)/1000);
	}

	
	public void initialize() throws Exception {
		parserEngine 		= new ParserEngine(modelFilename, propertiesFilename);
		genes				= GenotypeFactory.createChromosome(parserEngine.getEvolvableList());
		parserEngine.createMapping();
		
		propertyList = new ArrayList<Property>();
		
		//DPM properties (true for maximisation)
//		propertyList.add(new Property(false));
//		propertyList.add(new Property(false));
//		propertyList.add(new Property(false));
//		propertyList.add(new Property(false));
//		propertyList.add(new Property(false));
//		int numOfConstraints = 2;

		//FX
		propertyList.add(new Property(true));
		propertyList.add(new Property(false));
		propertyList.add(new Property(false));
		propertyList.add(new Property(true));
		int numOfConstraints = 1;

		//Zeroconf
//		propertyList.add(new Property(false));
//		propertyList.add(new Property(false));
//		propertyList.add(new Property(true));
//		int numOfConstraints = 0;

		problem = new GeneticProblem(genes, propertyList, parserEngine, numOfConstraints);
		
	}
	


	public void execute() throws Exception{
		Algorithm algorithm = null;
		String algorithmStr = Utility.getProperty("ALGORITHM").toUpperCase();
		if (algorithmStr != null){
			if (algorithmStr.equals("NSGAII")){
				NSGAII_Settings nsgaiiSettings = new NSGAII_Settings("GeneticProblem", problem);
				algorithm = nsgaiiSettings.configure();
			}
			else if (algorithmStr.equals("RANDOM")){
				RandomSearch_Settings rsSettings = new RandomSearch_Settings("GeneticProblem", problem);
				algorithm = rsSettings.configure();
			}
			else if (algorithmStr.equals("SPEA2")){
				SPEA2_Settings spea2Settings = new SPEA2_Settings("GeneticProblem", problem);
				algorithm = spea2Settings.configure();
			}
			else if (algorithmStr.equals("MOCELL")){
				MOCell_Settings mocellSettings = new MOCell_Settings("GeneticProblem", problem);
				algorithm = mocellSettings.configure();
			}
			else if (algorithmStr.equals("SGA")){
				int numOfConstraints = 0;
				problem = new GeneticProblemSingle(genes, propertyList, parserEngine, numOfConstraints);
				SingleGA_Settings sga_setting = new SingleGA_Settings("GeneticProblem", problem);
				algorithm = sga_setting.configure();
			}
			else 
				throw new Exception("Algorithm not recognised");
		}

		// Execute the Algorithm
		SolutionSet population = algorithm.execute();
		System.out.println("-------------------------------------------------");
		System.out.println("SOLUTION: \t" + population.size());
		population.printObjectivesToFile("data/FUN_"+algorithmStr);
		population.printVariablesToFile("data/VAR_"+algorithmStr);
		for (int i=0; i<population.size(); i++){
			Solution solution = population.get(i);
			double constraintValue = solution.getOverallConstraintViolation();
			if (constraintValue<0){
				System.out.println(constraintValue +"\t"+ Arrays.toString(solution.getDecisionVariables()));
			}
		}
	}
	
	
	public void execute(boolean nsgaii) throws FileNotFoundException, IOException {
		Algorithm algorithm;
		try {
			NSGAII_Settings nsgaiiSettings = new NSGAII_Settings("GeneticProblem", problem);
//			nsgaiiSettings.setCustomNSGAIIParameters();
			algorithm = nsgaiiSettings.configure();
			// Execute the Algorithm
			SolutionSet population = algorithm.execute();
			System.out.println("-------------------------------------------------");
			System.out.println("SOLUTION: \t" + population.size());
			population.printObjectivesToFile("data/FUN");
			population.printVariablesToFile("data/VAR");
//			population.printFeasibleFUN("FUN_Feasible");
//			System.out.println(((ArrayReal) population.get(0).getDecisionVariables()[0]).getValue(0));
//			System.out.println(((ArrayInt) population.get(0).getDecisionVariables()[1]).getValue(0));	
//			System.out.println(population.get(0).getObjective(0));
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

	
	public void setModelFilename (String filename){
		this.modelFilename = filename;
	}
	
	public void setPropertiesFilename (String filename){
		this.propertiesFilename = filename;
	}
	
	
	@SuppressWarnings("unused")
	private void initialiseGenotype(){
		List<Evolvable> evolvableList = parserEngine.getEvolvableList();
		
		try {
			propertyList = new ArrayList<Property>();
//			propertyList.add(new Property(false));
//			propertyList.add(new Property(true));//true for maximisation
//			genes.add(new AlternativeModuleGene("%", 2));
//			genes.add(new IntegerConstGene("@1@", 1, 10));
//			genes.add(new IntegerConstGene("@2@", 1, 10));
//			genes.add(new DiscreteDistributionGene("#", 2));
//			genes.add(new DiscreteDistributionGene("$", 2));
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
	}
		
}
