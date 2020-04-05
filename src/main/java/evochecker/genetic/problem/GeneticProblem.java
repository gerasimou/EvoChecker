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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import evochecker.exception.EvoCheckerException;
//import org.apache.commons.lang.NotImplementedException;
import evochecker.genetic.genes.AbstractGene;
import evochecker.language.parser.IModelInstantiator;
import evochecker.properties.Property;
import jmetal.core.Solution;
import jmetal.util.JMException;

public class GeneticProblem extends GeneticModelProblem {

	private static final long serialVersionUID = -2679872853510614319L;

	
	/**
	 * Class constructor: create a new Genetic Problem instance
	 * @param genes
	 * @param properties
	 * @param instantiator
	 * @param numOfConstraints
	 */
	public GeneticProblem(List<AbstractGene> genes, IModelInstantiator instantiator,
						  List<Property> objectivesList, List<Property> constraintsList, String problemName){
		super(genes, instantiator, objectivesList, constraintsList, problemName);
	}
	
	
	public GeneticProblem (GeneticProblem aProblem) throws EvoCheckerException{		
		super((GeneticModelProblem)aProblem);
	}

	
	/** 
	 * Evaluate 
	 * @param solution
	 * @param out
	 * @param in
	 * @throws JMException
	 * @throws EvoCheckerException 
	 */
	@Override
	public void parallelEvaluate(BufferedReader in, PrintWriter out, Solution solution) throws JMException, EvoCheckerException {
		//Populate genes
		this.populateGenesWithRealSolution(solution);
		this.populateGenesWithIntSolution(solution);

		//Prepare params
		String model 		= instantiator.getConcreteModel(this.genes);
		String propertyFile = instantiator.getPropertyFileName();
		
		try {
			List<String> resultsList = modelInvoker.invoke(model, propertyFile, out, in);

			//evaluate constraints
			for (int i = 0; i < numberOfObjectives_; i++) {
				Property p = objectivesList.get(i);
				int index  = p.getIndex();
				double value  = Double.parseDouble(resultsList.get(index));
				double result = p.evaluate(value);
				solution.setObjective(i, result);
				System.out.print("FITNESS: "+ result +"\t");
			}

			//evaluate constraints
			this.evaluateConstraints(solution, resultsList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println();
	}	
}
