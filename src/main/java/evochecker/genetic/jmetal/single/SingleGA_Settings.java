package evochecker.genetic.jmetal.single;

import java.util.HashMap;

import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.experiments.Settings;
//import jmetal.metaheuristics.singleObjective.geneticAlgorithm.pgGA;
import jmetal.operators.crossover.Crossover;
import jmetal.operators.mutation.Mutation;
import jmetal.operators.selection.Selection;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.JMException;
import evochecker.auxiliary.Utility;
import evochecker.genetic.jmetal.GeneticProblem;
import evochecker.genetic.jmetal.MultiProcessPrismEvaluator;
import evochecker.genetic.jmetal.operators.CrossoverFactory;
import evochecker.genetic.jmetal.operators.MutationFactory;

public class SingleGA_Settings extends Settings{
	public int populationSize_;
	public int maxEvaluations_;
	public double realCrossoverProbability_;
	public double intCrossoverProbability_;
	public double realMutationProbability_;
	public double intMutationProbability_;
	public double distributionIndex_;

	/**
	 * Constructor
	 */
	public SingleGA_Settings (String problemName, Problem problem){
		super(problemName);
		problem_ = problem;
		// Default experiments.settings
		populationSize_ 			= Integer.parseInt(Utility.getProperty("POPULATION_SIZE", "100"));
		maxEvaluations_ 			= Integer.parseInt(Utility.getProperty("MAX_EVALUATIONS", "100"));

		realCrossoverProbability_ 	= 0.9;
		intCrossoverProbability_ 	= 0.9;//0.5;
		realMutationProbability_ 	= 1.0 / ((GeneticProblemSingle)problem_).getNumOfRealVariables();//  0.4;
		intMutationProbability_ 	= 1.0 / ((GeneticProblemSingle)problem_).getNumOfIntVariables();//0.4;
		distributionIndex_ 			= 20;
	} // SingleGA_Settings
	
	
	

	/**
	 * Configure Single_GA with default parameter experiments.settings
	 * 
	 * @return A Single_GA algorithm object
	 * @throws jmetal.util.JMException
	 */
	@Override
	public Algorithm configure() throws JMException {
		Algorithm algorithm;
		Selection selection;
		Crossover crossover;
		Mutation mutation;

		HashMap<String, Double> parameters; // Operator parameters

		//Create algorithm and parallel objects
		MultiProcessPrismEvaluator evaluator = new MultiProcessPrismEvaluator(0);
		algorithm = new pgGA(problem_, evaluator); //pNSGAII(problem_, evaluator);

		// Algorithm parameters
		algorithm.setInputParameter("populationSize", populationSize_);
		algorithm.setInputParameter("maxEvaluations", maxEvaluations_);

		// Mutation and Crossover for Real codification
		parameters = new HashMap<String, Double>();

		parameters.put("realCrossoverProbability", this.realCrossoverProbability_);
		parameters.put("intCrossoverProbability", this.intCrossoverProbability_);
		parameters.put("distributionIndex", this.distributionIndex_);
		crossover = CrossoverFactory.getCrossoverOperator("SBXSinglePointCrossover", parameters);

		parameters = new HashMap<String, Double>();
		parameters.put("realMutationProbability", this.realMutationProbability_);
		parameters.put("intMutationProbability", this.intMutationProbability_);
		parameters.put("distributionIndex", this.distributionIndex_);
		mutation = MutationFactory.getMutationOperator("PolynomialUniformMutation", parameters);

		// Add the operators to the algorithm
		algorithm.addOperator("crossover", crossover);
		algorithm.addOperator("mutation", mutation);

		// Selection Operator
		parameters = null;
		selection = SelectionFactory.getSelectionOperator("BinaryTournament",parameters);

		algorithm.addOperator("selection", selection);

		return algorithm;
	} // configure

}
