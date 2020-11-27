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

	@Override
	public  List<String> evaluate(BufferedReader in, PrintWriter out) throws Exception{
		return evaluateByInvocation(out, in);
	}
	

	protected List<String> evaluateByInvocation(PrintWriter out, BufferedReader in) throws Exception {
		//Prepare model
		//System.out.println(genes.get(0).getAllele() +"\t"+ genes.get(1).getAllele() );
		String model 		= modelInstantiator.getConcreteModel(this.genes);
		String propertyFile = modelInstantiator.getPropertyFileName();
		
		return modelInvoker.invoke(model, propertyFile, objectivesList, constraintsList, out, in);
	}
}
