//  RandomSearch_Settings.java 
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

import evochecker.auxiliary.Utility;
import evochecker.evaluator.MultiProcessModelEvaluator;
import evochecker.genetic.jmetal.metaheuristics.pRandomSearch;
import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.experiments.Settings;
import jmetal.util.JMException;

/**
 * Settings class of algorithm RandomSearch
 */
public class RandomSearch_Settings extends Settings{
	public int maxEvaluations_;
	public int populationSize_;

	/***
	 * Constructor
	 * @param problemName Problem name
	 * @param problem Problem to solve
	 */
	public RandomSearch_Settings(String problemName, Problem problem){
		super(problemName);
		problem_ 			= problem;
		maxEvaluations_ 	= Integer.parseInt(Utility.getProperty("MAX_EVALUATIONS", "100"));
		populationSize_ 	= Integer.parseInt(Utility.getProperty("POPULATION_SIZE", "100"));

	}//RandomSearch_Settings


	/**
	 * Configure the random search algorithm with default parameter experiments.settings
	 * @return an algorithm object
	 * @throws jmetal.util.JMException
	 */
	@Override
	public Algorithm configure() throws JMException {
		//construct the parallel evaluator
		MultiProcessModelEvaluator mpPrismEvaluator = new MultiProcessModelEvaluator();
		
		//create a new algorithm object
		Algorithm algorithm = new pRandomSearch(problem_, mpPrismEvaluator);
		
		//Algorithm Parameters
	    algorithm.setInputParameter("maxEvaluations", maxEvaluations_);
		algorithm.setInputParameter("populationSize", populationSize_);
		return algorithm;
	}

}
