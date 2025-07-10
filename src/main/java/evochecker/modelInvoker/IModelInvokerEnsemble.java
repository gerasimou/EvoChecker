//==============================================================================
//	
//	Copyright (c) 2020-
//	Authors:
//	* Simos Gerasimou (University of York)
//  * Faisal Alhwikem (University of York)
//	
//------------------------------------------------------------------------------
//	
//	This file is part of EvoChecker.
//	
//==============================================================================
package evochecker.modelInvoker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import evochecker.properties.Property;
import evochecker.genetic.genes.AbstractGene;

/*
 * An interface to represent a model invoker that send commands to a running model checker. This model is to be called
 * from within a genetic problem implementation.
 */
public interface IModelInvokerEnsemble extends IModelInvoker {


	//TODO: Delete carefully. No longer needed.
		
	// Provide default implementations that throw UnsupportedOperationException
	@Override
	default List<String> invoke(String model, String propertyFile, List<Property> objectives, List<Property> constraints, PrintWriter out, BufferedReader in) throws IOException {
		throw new UnsupportedOperationException("'invoke' is not implemeted for IModelInvokerEnsemble. Use 'invokeEnsemble' instead.");
	}

	@Override
	default List<String> invokeParam(String model, String propertyFile, List<Property> objectives, List<Property> constraints, PrintWriter out, BufferedReader in) throws IOException {
		throw new UnsupportedOperationException("'invokeParam' is not implemeted for IModelInvokerEnsemble. Use 'invokeEnsemble' instead.");
	}
	

	public List<String> invokeEnsemble (String modelFile, HashMap<String, List<List<Property>>> objectivesConstraintsHashMap, List<AbstractGene> genes) throws IOException;

	public IModelInvoker copy(int id);
}
