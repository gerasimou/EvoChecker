package evochecker.genetic.jmetal.single;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import jmetal.core.Solution;
import jmetal.util.JMException;
import evochecker.auxiliary.Utility;
//import org.apache.commons.lang.NotImplementedException;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.jmetal.GeneticProblem;
import evochecker.genetic.jmetal.encoding.ArrayReal;
import evochecker.parser.InstantiatorInterface;
import evochecker.prism.Property;

public class GeneticProblemSingleUUV extends GeneticProblemSingle {

	private static final long serialVersionUID = 1L;

	/**
	 * Class constructor
	 * @param genes
	 * @param properties
	 * @param instantiator
	 * @param numOfConstraints
	 */
	public GeneticProblemSingleUUV(List<AbstractGene> genes, List<Property> properties, InstantiatorInterface instantiator, int numOfConstraints) {
		super(genes, properties, instantiator, numOfConstraints);
	}

	
	
	protected void evaluateObjectives(Solution solution, List<String> fitnessList){
		try {
			for (int index = 0; index < this.numberOfObjectives_-2; index++) {
				double result = Double.parseDouble(fitnessList.get(index));
				solution.setObjective(index, result);
	//			System.out.printf("FITNESS: %.3f\t", result);
			}
			solution.setObjective(numberOfObjectives_-2, ((ArrayReal)solution.getDecisionVariables()[0]).getValue(0));
		} 
		catch (JMException e) {
			e.printStackTrace();
		}
	}
	
	
	public void evaluateConstraints(Solution solution, List<String> fitnessList, boolean cost) throws JMException {
		//UUV constraints
		double totalViolation 				= 0;
		
		double measurementsConstraint 	= 500;
		double measurementsResult 		= Double.parseDouble(fitnessList.get(0));
		double measurementsViolation	= measurementsResult - measurementsConstraint;
		if (measurementsViolation < 0)
			totalViolation = measurementsViolation;

		double energyConstraint			= 1000;
		double energyResult				= Double.parseDouble(fitnessList.get(1));
		double energyViolation			= energyConstraint - energyResult; 
		if (energyViolation < 0)
			totalViolation += energyViolation;
		
		if (totalViolation <0){
			solution.setOverallConstraintViolation(totalViolation);
			solution.setNumberOfViolatedConstraint(1);
		}
		else{
			solution.setOverallConstraintViolation(0);
			solution.setNumberOfViolatedConstraint(0);
		}
	}
}
