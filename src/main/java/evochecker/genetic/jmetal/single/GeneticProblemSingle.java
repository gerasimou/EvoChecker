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
	}


	public void parallelEvaluate(Solution solution, PrintWriter out, BufferedReader in) throws JMException {
		this.populateGenesWithRealSolution(solution);
		this.populateGenesWithIntSolution(solution);

		// Invoke prism....
		String model = instantiator.getPrismModelInstance(this.genes);
//		System.out.println(model);
		String propertyFile = instantiator.getPrismPropertyFileName();

		
		List<String> fitnessList;
		try {
			fitnessList = this.invokePrism(model, propertyFile, out, in);

			for (int i = 0; i < this.numberOfObjectives_; i++) {
				Property p = this.properties.get(i);
				double result;
				if (p.isMaximization()) {
					result = new BigDecimal(- Double.parseDouble(fitnessList.get(i))).setScale(3, RoundingMode.HALF_DOWN).doubleValue();
				}
				else{
					result = new BigDecimal(Double.parseDouble(fitnessList.get(i))).setScale(3, RoundingMode.HALF_UP).doubleValue();
				}
				solution.setObjective(i, result);
				System.out.print("FITNESS: "+ result +"\t");
//				} 
//				else {
//					solution.setObjective(i, result);
//					System.out.print("FITNESS: " + result +"\t");
//				}
			}
			
//			this.evaluateConstraints(solution, fitnessList);
			this.evaluateConstraints(solution, fitnessList, true );
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	  public void evaluateConstraints(Solution solution, List<String> fitnessList, boolean cost) throws JMException {
		  double costConstraint = Double.parseDouble(Utility.getProperty("TIME_THRESHOLD", "50"));
			for (int i=0; i < this.numberOfConstraints_; i++){
				int index		= numberOfObjectives_ + i;
				double result 	= Double.parseDouble(fitnessList.get(index));
				double violation = new BigDecimal(costConstraint-result).setScale(3, RoundingMode.HALF_DOWN).doubleValue() ;
				System.out.print("Constraint:" + (result) );
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
