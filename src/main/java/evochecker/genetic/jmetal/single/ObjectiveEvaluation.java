package evochecker.genetic.jmetal.single;

import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.util.JMException;

/**
 * Class that performs a linear aggregation of the n QoS objectives 
 * for each individual from the population
 * @author sgerasimou
 *
 */
public class ObjectiveEvaluation {
	
	 /**
	   * Evaluate the objectives
	   * @param solutionList
	 * @throws JMException 
	   */
	  protected static void evaluateObjectives(SolutionSet population, int totalNumberOfObjectives) throws JMException{
		  //get populationSize
		  int populationSize = population.size();
		  
		//get number of objectives
		  int numberOfObjectives = totalNumberOfObjectives-1;//the last holds the weighted result
		  
		  //maximum value per objective
		  double maximumPerObjective[] = new double[numberOfObjectives];
		  
		  //find the maximum per objective
		  for (int objective=0; objective<numberOfObjectives; objective++){
			  //find maximum		  
			  double maximum = 0;
			  for (int i = 0; i < populationSize; i++) {
				  Solution solution = population.get(i);
				  double result = solution.getObjective(objective);
				  if (result > maximum)
					  maximum = result;
			  }
			  maximumPerObjective[objective] = maximum;
		  }
		  maximumPerObjective[2] = 10.0;

		  for (int i = 0; i < populationSize; i++) {
			  Solution solution = population.get(i);
				double w1 = 0.1;
				double w2 = 1- w1;
				double cost = 10;
				if (solution.getOverallConstraintViolation()>=0){
					cost 	= (w1 * maximumPerObjective[2] / solution.getObjective(2)) +
							  (w2 * solution.getObjective(1) / maximumPerObjective[1]) ;							
				}
				solution.setObjective(totalNumberOfObjectives-1, cost);
		  }
	  }
	  
	  
	  
		 /**
	   * Evaluate the objectives
	   * @param solutionList
	   */
	  protected static void evaluateObjectivesFX(SolutionSet population, int totalNumberOfObjectives){
		  //get populationSize
		  int populationSize = population.size();
		  
		//get number of objectives
		  int numberOfObjectives = totalNumberOfObjectives-1;//the last holds the weighted result
		  
		  //maximum value per objective
		  double maximumPerObjective[] = new double[numberOfObjectives];
		  
		  //find the maximum per objective
		  for (int objective=0; objective<numberOfObjectives; objective++){
			  //find maximum		  
			  double maximum = 0;
			  for (int i = 0; i < populationSize; i++) {
				  Solution solution = population.get(i);
				  double result = solution.getObjective(objective);
				  if (result > maximum)
					  maximum = result;
			  }
			  maximumPerObjective[objective] = maximum;
		  }

		  for (int i = 0; i < populationSize; i++) {
			  Solution solution = population.get(i);
				double w1 = 0.2;
				double w2 = 0.4;
				double evaluation = 10;
				if (solution.getOverallConstraintViolation()>=0){
					evaluation 	= 	(w1 * maximumPerObjective[0]/solution.getObjective(0)) + 
									(w2 * solution.getObjective(1)/maximumPerObjective[1]) + 
									((1-w1-w2) * solution.getObjective(2)/maximumPerObjective[2]);
				}
				solution.setObjective(3, evaluation);
		  }
	  }
}
