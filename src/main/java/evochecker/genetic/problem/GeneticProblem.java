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
	
	
	/**
	 * Copy constructor
	 * @param aProblem
	 * @throws EvoCheckerException
	 */
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
	public boolean parallelEvaluate(BufferedReader in, PrintWriter out, Solution solution) throws JMException, EvoCheckerException {
		//Populate genes
		this.populateGenesWithRealSolution(solution);
		this.populateGenesWithIntSolution(solution);
		
		
		try {
			//parametric work goes here
			List<String> resultsList = null; 
//			resultsList = parametricWork(out, in);
			resultsList = evaluateByInvocation(out, in);

			//evaluate objectives
			for (int i = 0; i < numberOfObjectives_; i++) {
				Property p = objectivesList.get(i);
				int index  = p.getIndex();
				double value  = Double.parseDouble(resultsList.get(index));
				double result = p.evaluate(value);
				solution.setObjective(i, result);
				if (verbose)
					System.out.print("O" +(i+1) + "):"+ result +"\t");
			}

			//evaluate constraints
			this.evaluateConstraints(solution, resultsList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (verbose)
			System.out.println();
		
		return true;
	}	
	

	private List<String> evaluateByInvocation(PrintWriter out, BufferedReader in) throws Exception {
		//Prepare model
		//System.out.println(genes.get(0).getAllele() +"\t"+ genes.get(1).getAllele() );
		String model 		= modelInstantiator.getConcreteModel(this.genes);
		String propertyFile = modelInstantiator.getPropertyFileName();
		
		return modelInvoker.invoke(model, propertyFile, out, in);
	}
}
