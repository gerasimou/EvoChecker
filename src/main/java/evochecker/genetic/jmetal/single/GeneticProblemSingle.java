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

public class GeneticProblemSingle extends GeneticProblem {

	private static final long serialVersionUID = 1L;

	/**
	 * Class constructor
	 * @param genes
	 * @param properties
	 * @param instantiator
	 * @param numOfConstraints
	 */
	public GeneticProblemSingle(List<AbstractGene> genes, List<Property> properties, InstantiatorInterface instantiator, int numOfConstraints) {
		super(genes, properties, instantiator, numOfConstraints);
		//#objectives = #properties - #constraints + 1 (i.e., the aggregated objective results; fitness function)
		this.numberOfObjectives_ 	= properties.size()-numberOfConstraints_+1;
	}


	@Override
	public void parallelEvaluate(Solution solution, PrintWriter out, BufferedReader in) throws JMException {
		this.populateGenesWithRealSolution(solution);
		this.populateGenesWithIntSolution(solution);

		// Invoke prism....
		String model = instantiator.getPrismModelInstance(this.genes);
//		System.out.println(model);
		String propertyFile = instantiator.getPrismPropertyFileName();

		try {
			List<String> fitnessList = this.invokePrism(model, propertyFile, out, in);
			this.evaluateObjectives(solution, fitnessList);
			this.evaluateConstraints(solution, fitnessList, true);
		} 
		catch (IOException e) {
			System.err.println("Error when invoking PRISM; check the model");
			e.printStackTrace();
			Utility.exportToFile("data/exceptionModel.pm", model, false);
			System.exit(0);
		}
	}
	
	
	private void evaluateObjectives(Solution solution, List<String> fitnessList){
		for (int index = 0; index < this.numberOfObjectives_-1; index++) {
			Property p = this.properties.get(index);
			double result = Double.parseDouble(fitnessList.get(index));
			solution.setObjective(index, result);
//			System.out.printf("FITNESS: %.3f\t", result);
		}			
	}
	
	
	public void evaluateConstraints(Solution solution, List<String> fitnessList, boolean cost) throws JMException {
		double reliabilityConstraint = Double.parseDouble(Utility.getProperty("RELIABILITY_THRESHOLD", "0.97"));
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
		
//		double timeThreshold = 20;
//		double result 	= Double.parseDouble(fitnessList.get(2));
//		double violation = timeThreshold-result;
//		System.out.printf("Constraint: %.3f\n", result);
//		if (violation < 0){
//			solution.setOverallConstraintViolation(violation+solution.getOverallConstraintViolation());
//			solution.setNumberOfViolatedConstraint(1);
//		}

	}
}
