//  MOCell_Settings.java 
//
//  Authors:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package evochecker.genetic.jmetal.metaheuristics.settings;

import java.util.HashMap;
import java.util.Properties;

import evochecker.auxiliary.ConfigurationChecker;
import evochecker.auxiliary.Constants;
import evochecker.auxiliary.Utility;
import evochecker.evaluator.MultiProcessModelEvaluator;
import evochecker.genetic.jmetal.metaheuristics.pMoCell;
import evochecker.genetic.jmetal.operators.CrossoverFactory;
import evochecker.genetic.jmetal.operators.MutationFactory;
import evochecker.genetic.problem.GeneticProblem;
import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.experiments.Settings;
//import jmetal.operators.crossover.CrossoverFactory;
//import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.JMException;

/**
 * Settings class of algorithm MOCell
 */
public class MOCell_Settings extends Settings{

  public int populationSize_                ;
  public int maxEvaluations_                ;
  public int archiveSize_                   ;
  public int feedback_                      ;
  public double intMutationProbability_   ;
  public double realMutationProbability_   ;
  public double intCrossoverProbability_  ;
  public double realCrossoverProbability_  ;
  public double crossoverDistributionIndex_ ;
  public double mutationDistributionIndex_  ;

  
	//default mutation operator;
	String mutationOperator = "PolynomialUniformMutation";

  /**
   * Constructor
   */
  public MOCell_Settings(String problemName, Problem problem) {
    super(problemName) ;
    
    problem_ = problem;

    
	// Default experiments.settings
	populationSize_ 			= Integer.parseInt(Utility.getProperty(Constants.POPULATION_SIZE_KEYWORD, "100"));
	archiveSize_				= populationSize_;
	maxEvaluations_ 			= Integer.parseInt(Utility.getProperty(Constants.MAX_EVALUATIONS_KEYWORD, "100"));	
    feedback_                   = 20    ;

	realCrossoverProbability_	= 0.9;
	intCrossoverProbability_	= 0.9;
	
	String realMutationProbability = Utility.getProperty(Constants.REAL_MUTATION_PROBABILITY, ConfigurationChecker.NAN);
	if (realMutationProbability.equals(ConfigurationChecker.NAN)) { 		
		int realVars = ((GeneticProblem)problem_).getNumOfRealVariables();
		if (realVars > 0)
			realMutationProbability_ 	= 1.0 / realVars;
		else 
			realMutationProbability_ 	=0;
	}
	else
		realMutationProbability_ = Double.parseDouble(realMutationProbability);
	
	
	String intMutationProbability = Utility.getProperty(Constants.INTEGER_MUTATION_PROBABILITY, ConfigurationChecker.NAN);
	if (intMutationProbability.equals(ConfigurationChecker.NAN)) { 		
		int intVars = ((GeneticProblem)problem_).getNumOfIntVariables();
		if (intVars > 0)
			intMutationProbability_ 	= 1.0 / intVars;
		else 
			intMutationProbability_	= 0;
	}
	else
		intMutationProbability_ = Double.parseDouble(intMutationProbability);
	
	
	//specify mutation operator
	String mutOperator =  Utility.getProperty("MUTATION_OPERATOR", ConfigurationChecker.NAN);
	if (!mutOperator.equals(ConfigurationChecker.NAN)) 
		mutationOperator = mutOperator;
	
//	realMutationProbability_ 	= 1.0 / ((GeneticProblem)problem_).getNumOfRealVariables();//  0.4;
//	intMutationProbability_ 	= 1.0 / ((GeneticProblem)problem_).getNumOfIntVariables();//0.4;
    crossoverDistributionIndex_ = 20.0  ;
    mutationDistributionIndex_  = 20.0  ;
  }// MOCell_Settings

  
  
  /**
   * Configure the MOCell algorithm with default parameter experiments.settings
   * @return an algorithm object
   * @throws jmetal.util.JMException
   */
  public Algorithm configure() throws JMException {
    Algorithm algorithm ;

    Operator  crossover ;         // Crossover operator
    Operator  mutation  ;         // Mutation operator
    Operator  selection ;         // Selection operator

    HashMap<String, Double>  parameters ; // Operator parameters

	// Creating the algorithm
	MultiProcessModelEvaluator evaluator = new MultiProcessModelEvaluator();
	algorithm  = new pMoCell(problem_, evaluator);

    // Algorithm parameters
    algorithm.setInputParameter("populationSize", populationSize_);
    algorithm.setInputParameter("maxEvaluations", maxEvaluations_);
    algorithm.setInputParameter("archiveSize",archiveSize_ );
    algorithm.setInputParameter("feedBack", feedback_);


    // Mutation and Crossover for Real codification 
	parameters = new HashMap<String, Double>();

	parameters.put("realCrossoverProbability", this.realCrossoverProbability_);
	parameters.put("intCrossoverProbability", this.intCrossoverProbability_);
	parameters.put("distributionIndex", this.crossoverDistributionIndex_);
	crossover = CrossoverFactory.getCrossoverOperator("SBXSinglePointCrossover", parameters);

	parameters = new HashMap<String, Double>();
	parameters.put("realMutationProbability", this.realMutationProbability_);
	parameters.put("intMutationProbability", this.intMutationProbability_);
	parameters.put("distributionIndex", this.mutationDistributionIndex_);
	mutation = MutationFactory.getMutationOperator(mutationOperator, parameters);		

    // Selection operator 
    parameters = null ;
    selection = SelectionFactory.getSelectionOperator("BinaryTournament", parameters) ;                           
    
    // Add the operators to the algorithm
    algorithm.addOperator("crossover",crossover);
    algorithm.addOperator("mutation",mutation);
    algorithm.addOperator("selection",selection);
   
   return algorithm ;
  } // configure

  /**
   * Configure MOCell with user-defined parameter experiments.settings
   * @return A MOCell algorithm object
   */
  @Override
  public Algorithm configure(Properties configuration) throws JMException {
    return null;
  }
} // MOCell_Settings
