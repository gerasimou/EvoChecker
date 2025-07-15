//==============================================================================
//	
//	Copyright (c) 2015-
//	Authors:
//	* Simos Gerasimou (University of York)
//	
//------------------------------------------------------------------------------
//	
//	This file is part of EvoChecker.
//	
//==============================================================================

package evochecker.genetic.problem;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;

import evochecker.exception.EvoCheckerException;
import evochecker.genetic.genes.AbstractGene;
import evochecker.language.parser.IModelInstantiator;
import evochecker.properties.Property;
import jmetal.core.Solution;
import jmetal.util.JMException;
import ultimate.Ultimate;
import evochecker.language.parser.ModelInstantiatorUltimate;
import evochecker.modelInvoker.ModelInvokerUltimate;

public class GeneticProblemUltimate extends GeneticProblem {

	private static final long serialVersionUID = -2679872853510614319L;
	// private HashMap<String, List<List<Property>>> objectivesConstraintsHashMap;
	private String modelFilename;
	private HashMap<String, List<List<Property>>> objectiveConstraintsMap;

	/**
	 * Class constructor: create a new Genetic Problem instance
	 * 
	 * @param genes
	 * @param properties
	 * @param instantiator
	 * @param numOfConstraints
	 */
	public GeneticProblemUltimate(List<AbstractGene> genes, ModelInstantiatorUltimate instantiator,
			List<Property> objectivesList, List<Property> constraintsList,
			HashMap<String, List<List<Property>>> objectiveConstraintsMap,
			String problemName) {

		super(genes, instantiator, objectivesList, constraintsList, problemName);

		// this.objectivesConstraintsHashMap = objectivesConstraintsHashMap;
		this.modelFilename = instantiator.getModelFilename();
		this.objectiveConstraintsMap = objectiveConstraintsMap;
	}

	// /**
	// * Copy constructor
	// *
	// * @param aProblem
	// * @throws EvoCheckerException
	// */
	public GeneticProblemUltimate(GeneticProblemUltimate aProblem) throws EvoCheckerException {
		super((GeneticProblem) aProblem);
		this.modelFilename = aProblem.getModelFilename();

	}

	@Override
	public List<String> evaluate(BufferedReader in, PrintWriter out) throws Exception {

		// invoke ULTIMATE
		return ((ModelInvokerUltimate) modelInvoker).invokeEnsemble(this.modelFilename,
				this.objectiveConstraintsMap, genes);

	}

	@Override
	public boolean evaluateSolution(BufferedReader in, PrintWriter out, Solution solution)
			throws JMException, EvoCheckerException {

		this.populateGenesWithRealSolution(solution);
		this.populateGenesWithIntSolution(solution);

		// invoke ULTIMATE
		List<String> results = ((ModelInvokerUltimate) modelInvoker).invokeEnsemble(this.modelFilename,
				this.objectiveConstraintsMap, genes);

		evaluateObjectives(solution, results);
		evaluateConstraints(solution, results);

		return true;

	}

	// protected List<String> evaluateByInvocation(PrintWriter out, BufferedReader
	// in) throws Exception {

	// String model = modelInstantiator.getConcreteModel(this.genes);
	// String propertyFile = modelInstantiator.getPropertyFileName();

	// return modelInvoker.invoke(model, propertyFile, objectivesList,
	// constraintsList, out, in);
	// }

	private void evaluateObjectives(Solution solution, List<String> resultsList) {
		// evaluate objectives
		for (int i = 0; i < numberOfObjectives_; i++) {
			Property p = objectivesList.get(i);
			int index = p.getIndex();
			double value = Double.parseDouble(resultsList.get(index));
			double result = p.evaluate(value);
			solution.setObjective(i, result);
			if (verbose)
				System.out.print("O" + (i + 1) + "):" + result + "\t");
		}
	}

	public void evaluateConstraints(Solution solution, List<String> resultsList) throws JMException {
		double totalViolation = 0;
		int violatedConstraints = 0;
		for (int i = 0; i < numberOfConstraints_; i++) {
			Property p = constraintsList.get(i);
			int index = p.getIndex();// numberOfObjectives_ + i;
			double value = Double.parseDouble(resultsList.get(index));
			double result = new BigDecimal(value).setScale(4, RoundingMode.HALF_DOWN).doubleValue();

			if (verbose)
				System.out.print("C" + (i + 1) + "):" + result + "\t");

			double constraint = p.evaluate(result);
			if (constraint != 0) {
				totalViolation += constraint;
				violatedConstraints++;
			}
		}

		solution.setOverallConstraintViolation(totalViolation);
		solution.setNumberOfViolatedConstraint(violatedConstraints);
	}

	@Override
	/** Does nothing **/
	public void closeDown() {
	}

	@Override
	/** Does nothing **/
	public String getStatistics() {
		return "";
	}

	public String getModelFilename() {
		return this.modelFilename;
	}
}
