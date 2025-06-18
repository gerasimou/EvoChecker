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
import java.util.List;

import evochecker.exception.EvoCheckerException;
import evochecker.genetic.genes.AbstractGene;
import evochecker.language.parser.IModelInstantiator;
import evochecker.properties.Property;

public class GeneticProblemUltimate extends GeneticModelProblem {

	private static final long serialVersionUID = -2679872853510614319L;

	/**
	 * Class constructor: create a new Genetic Problem instance
	 * 
	 * @param genes
	 * @param properties
	 * @param instantiator
	 * @param numOfConstraints
	 */
	public GeneticProblemUltimate(List<AbstractGene> genes, IModelInstantiator instantiator,
			List<Property> objectivesList, List<Property> constraintsList, String problemName) {
		super(genes, instantiator, objectivesList, constraintsList, problemName);

	}

	/**
	 * Copy constructor
	 * 
	 * @param aProblem
	 * @throws EvoCheckerException
	 */
	public GeneticProblemUltimate(GeneticProblemUltimate aProblem) throws EvoCheckerException {
		super((GeneticModelProblem) aProblem);
	}

	@Override
	public List<String> evaluate(BufferedReader in, PrintWriter out) throws Exception {
		return evaluateByInvocation(out, in);
	}

	protected List<String> evaluateByInvocation(PrintWriter out, BufferedReader in) throws Exception {
		
		String model = modelInstantiator.getConcreteModel(this.genes);
		String propertyFile = modelInstantiator.getPropertyFileName();

		return modelInvoker.invoke(model, propertyFile, objectivesList, constraintsList, out, in);
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
}
