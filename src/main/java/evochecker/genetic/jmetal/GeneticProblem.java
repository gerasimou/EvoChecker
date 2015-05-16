package evochecker.genetic.jmetal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.util.JMException;
import evochecker.auxiliary.Utility;
//import org.apache.commons.lang.NotImplementedException;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.genes.AlternativeModuleGene;
import evochecker.genetic.genes.DiscreteDistributionGene;
import evochecker.genetic.genes.DoubleConstGene;
import evochecker.genetic.genes.IntegerConstGene;
import evochecker.genetic.jmetal.encoding.ArrayInt;
import evochecker.genetic.jmetal.encoding.ArrayReal;
import evochecker.genetic.jmetal.encoding.ArrayRealIntSolutionType;
import evochecker.parser.InstantiatorInterface;
import evochecker.prism.Property;

public class GeneticProblem extends Problem {

	private static final long serialVersionUID = -2679872853510614319L;
	protected static  int eval = 0;
	public static synchronized int getEval() {
		return eval;
	}

	protected List<AbstractGene> genes;
	protected InstantiatorInterface instantiator;
	protected List<Property> properties;
	private int intVariables, realVariables;

	public GeneticProblem(List<AbstractGene> genes, List<Property> properties, InstantiatorInterface instantiator, int numOfConstraints) {
		this.genes 					= genes;
		this.instantiator 			= instantiator;
		this.numberOfConstraints_ 	= numOfConstraints;
		this.numberOfObjectives_ 	= properties.size()-numberOfConstraints_;
		this.properties 			= properties;
		this.initializeLimits();
	}

	private int computeRealVariables(int baseIndex) {
		int realVariables = baseIndex;
		for (AbstractGene g : genes) {
			if (g instanceof DiscreteDistributionGene) {
				int outcomes = ((DiscreteDistributionGene) g)
						.getNumberOfOutcomes();
				int total = realVariables + outcomes;
				for (int j = realVariables; j < total; j++) {
					lowerLimit_[j] = g.getMinValue().doubleValue();
					upperLimit_[j] = g.getMaxValue().doubleValue();
					realVariables++;
				}
			}

			if (g instanceof DoubleConstGene) {
				lowerLimit_[realVariables] = g.getMinValue().doubleValue();
				upperLimit_[realVariables] = g.getMaxValue().doubleValue();
				realVariables++;
			}
		}
		return realVariables - baseIndex;
	}

	private int computeIntVariables(int baseIndex) {
		int intVariables = baseIndex;
		for (AbstractGene g : genes) {
			if (g instanceof IntegerConstGene
					|| g instanceof AlternativeModuleGene) {
				lowerLimit_[intVariables] = g.getMinValue().doubleValue();
				upperLimit_[intVariables] = g.getMaxValue().doubleValue();
				// System.out.println("MIN VALUE: "+
				// g.getMinValue().doubleValue());
				// System.out.println("MAX VALUE: "+
				// g.getMaxValue().doubleValue());
				intVariables++;
			}
		}
		return intVariables - baseIndex;
	}

	private void initializeLimits() {
		computeNumberOfVariables();
		// System.out.println("Found variables: " + this.numberOfVariables_);
		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];
		realVariables = this.computeRealVariables(0);
		intVariables = this.computeIntVariables(realVariables);

		solutionType_ = new ArrayRealIntSolutionType(this, realVariables,intVariables, this.lowerLimit_, this.upperLimit_);
	}

	protected void populateGenesWithRealSolution(Solution solution)
			throws JMException {
		ArrayReal realPart = (ArrayReal) solution.getDecisionVariables()[0];
		int currentIndex = 0;

		for (int i = 0; i < genes.size(); i++) {
			AbstractGene g = genes.get(i);
			if (g instanceof DiscreteDistributionGene) {
				int outcomes = ((DiscreteDistributionGene) g).getNumberOfOutcomes();
				double cumulative = 0;
				double[] outcomesValues = new double[outcomes];
				int index = 0;

				for (int j = currentIndex; j < currentIndex + outcomes; j++) {
					cumulative += realPart.getValue(j);
					outcomesValues[index] = realPart.getValue(j);
					index++;
				}
				currentIndex = currentIndex + outcomes;
				
				//TODO real values normalised here: is this good for a CTMC?
				for (int j = 0; j < outcomes; j++) {
					outcomesValues[j] = outcomesValues[j] / cumulative;
				}
				g.setAllele(outcomesValues);
			}

			if (g instanceof DoubleConstGene) {
				double value = realPart.getValue(currentIndex);
				currentIndex++;
				g.setAllele(value);
			}
		}
	}

	protected void populateGenesWithIntSolution(Solution solution)
			throws JMException {
		int currentIndex = 0;
		for (int i = 0; i < genes.size(); i++) {
			AbstractGene g = genes.get(i);

			if (g instanceof IntegerConstGene
					|| g instanceof AlternativeModuleGene) {
				ArrayInt intPart = (ArrayInt) solution.getDecisionVariables()[1];
				g.setAllele(intPart.getValue(currentIndex));
				currentIndex++;
			}

		}
	}

	private void computeNumberOfVariables() {
		this.numberOfVariables_ = 0;
		for (AbstractGene g : genes) {
			// Discrete distribution generates a number of genes equal to the
			// number of their outcomes
			if (g instanceof DiscreteDistributionGene) {
				int outcomes = ((DiscreteDistributionGene) g)
						.getNumberOfOutcomes();
				this.numberOfVariables_ += outcomes;
			} else {
				this.numberOfVariables_++;
			}
		}
	}

	public void parallelEvaluate(Solution solution, PrintWriter out, BufferedReader in) throws JMException {
//		double[] x = new double[numberOfVariables_];
		this.populateGenesWithRealSolution(solution);
		this.populateGenesWithIntSolution(solution);
		eval++;
		// System.out.println("-------------------------------------------------");
		if (eval % 1000 == 0)
			System.out.print("Evaluating: " + this.getEval() +"\t");

//		for (int i = 0; i < this.realVariables; i++) {
//			x[i] = ((ArrayReal) solution.getDecisionVariables()[0]).getValue(i);
//			// System.out.println(x[i]);
//		}
//
//		for (int i = 0; i < this.intVariables; i++) {
//			x[i] = ((ArrayInt) solution.getDecisionVariables()[1]).getValue(i);
//			 System.out.print((int) x[i] +" ");
//		}

		// Invoke prism....
		String model = instantiator.getPrismModelInstance(this.genes);
//		System.out.println(model);
		String propertyFile = instantiator.getPrismPropertyFileName();
//		Utility.exportToFile("model.txt", model);

		
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
			
			if (numberOfConstraints_>0){
				this.evaluateConstraints(solution, fitnessList);
//				this.evaluateConstraintsDPM(solution, fitnessList);
//				this.evaluateConstraints(solution, fitnessList, true );
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println();
	}


	protected List<String> invokePrism(String model, String propertyFile, PrintWriter out, BufferedReader in)
			throws IOException {
//		System.out.println("Sending to PRISM: "+propertyFile);
//		System.out.println("Sending to PRISM: "+model);
		out.print(model + "@" + propertyFile + "\nEND\n");
		out.flush();

		String line;
		StringBuilder modelBuilder = new StringBuilder();
		do {
//			System.out.println("Waiting PRISM");
			line = in.readLine();
			if (line.endsWith("END"))
				break;
			modelBuilder.append(line);
			modelBuilder.append("\n");
		} while (true);

		String res[] = modelBuilder.toString().trim().split("@");
//		System.out.println("Received from PRISM: "+ modelBuilder.toString());
		return Arrays.asList(res);
	}

	
	@Override
	public void evaluate(Solution arg0) throws JMException {
		try {
			System.err.println("evaluate");
			throw new Exception();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		
	}
	
	 /** 
	  * Evaluates the constraint overhead of a solution 
	  * @param solution The solution
	 * @throws JMException 
	  */  
	public void evaluateConstraints(Solution solution, List<String> fitnessList) throws JMException {
		  double reliabilityConstraint = Double.parseDouble(Utility.getProperty("RELIABILITY_THRESHOLD", "0.8"));
			for (int i=0; i < this.numberOfConstraints_; i++){
				int index		= numberOfObjectives_ + i;
				double result 	= new BigDecimal(Double.parseDouble(fitnessList.get(index))- reliabilityConstraint).setScale(3, RoundingMode.HALF_DOWN).doubleValue() ;
//				System.out.print("Constraint:" + (result) );
				if (result < 0){
					solution.setOverallConstraintViolation(result*100);
					solution.setNumberOfViolatedConstraint(1);
				}
				else{
					solution.setOverallConstraintViolation(0);
					solution.setNumberOfViolatedConstraint(0);
				}
			}
	  }
	  
	
	  public void evaluateConstraints(Solution solution, List<String> fitnessList, boolean cost) throws JMException {
		  double costConstraint = Double.parseDouble(Utility.getProperty("TIME_THRESHOLD", "50"));
			for (int i=0; i < this.numberOfConstraints_; i++){
				int index		= numberOfObjectives_ + i;
				double result 	= Double.parseDouble(fitnessList.get(index));
				double violation = new BigDecimal(costConstraint-result).setScale(3, RoundingMode.HALF_DOWN).doubleValue() ;
//				System.out.print("Constraint:" + (result) );
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
	  
	  
	  public void evaluateConstraintsDPM(Solution solution, List<String> fitnessList) throws JMException {
		  double totalViolation = 0;
		  for (int i=0; i < this.numberOfConstraints_; i++){
			  //set up queuesLength
			  int queueLength 	= ((ArrayInt) solution.getDecisionVariables()[1]).getValue(i);
			  
			  int index			= numberOfObjectives_ + i;
			  double result 	= Double.parseDouble(fitnessList.get(index));
			  double violation	= new BigDecimal(queueLength*0.9-result).setScale(3, RoundingMode.HALF_DOWN).doubleValue() ;
//			  System.out.print("Constraint:" + (violation) +"\t" );
			  
			  if (violation<0)
				  totalViolation   += violation*10;
		  }
		  if (totalViolation < 0){
			  solution.setOverallConstraintViolation(totalViolation);
			  solution.setNumberOfViolatedConstraint(1);
		  }
		  else{
			  solution.setOverallConstraintViolation(0);
			  solution.setNumberOfViolatedConstraint(0);
		  }
	  }
	  
	  
	  
	  public int getNumOfIntVariables(){
		  return (this.intVariables);
	  }

	  public int getNumOfRealVariables(){
		  return (this.realVariables);
	  }
}
