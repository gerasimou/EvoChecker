package evochecker.genetic.jmetal.single;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import jmetal.core.Solution;
import jmetal.util.JMException;
import evochecker.auxiliary.Utility;
//import org.apache.commons.lang.NotImplementedException;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.jmetal.GeneticProblem;
import evochecker.parser.InstantiatorInterface;
import evochecker.prism.Property;

public class GeneticProblemSingleFX extends GeneticProblemSingle {

	private static final long serialVersionUID = 1L;

	/**
	 * Class constructor
	 * @param genes
	 * @param properties
	 * @param instantiator
	 * @param numOfConstraints
	 */
	public GeneticProblemSingleFX(List<AbstractGene> genes, List<Property> properties, InstantiatorInterface instantiator, int numOfConstraints) {
		super(genes, properties, instantiator, numOfConstraints);
	}

	
	
	protected void evaluateObjectives(Solution solution, List<String> fitnessList){
		for (int index = 0; index < this.numberOfObjectives_-1; index++) {
			double result = Double.parseDouble(fitnessList.get(index));
			solution.setObjective(index, result);
//			System.out.printf("FITNESS: %.3f\t", result);
		}			
	}
	
	
	public void evaluateConstraints(Solution solution, List<String> fitnessList, boolean cost) {
		double reliabilityConstraint = Double.parseDouble(Utility.getProperty("RELIABILITY_THRESHOLD", "0.98"));
		for (int i=0; i < this.numberOfConstraints_; i++){
			int index		= numberOfObjectives_ -1 + i;
			double result 	= Double.parseDouble(fitnessList.get(index));
			double violation = result-reliabilityConstraint;
			
//			System.out.printf("Constraint: %.3f\n", result);
			if (violation < 0){
				solution.setOverallConstraintViolation(violation);
				solution.setNumberOfViolatedConstraint(1);
			}
			else{
				solution.setOverallConstraintViolation(0);
				solution.setNumberOfViolatedConstraint(0);
			}
		}
	}
}
