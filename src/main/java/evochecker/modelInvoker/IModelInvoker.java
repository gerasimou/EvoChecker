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
public interface IModelInvoker {

	public List<String> invoke(String model, String propertyFile, List<Property> objectives, List<Property> constraints, PrintWriter out, BufferedReader in) throws IOException;

	public List<String> invokeParam(String model, String propertyFile, List<Property> objectives, List<Property> constraints, PrintWriter out, BufferedReader in) throws IOException;

    default List<String> invokeEnsemble(String modelFile, List<AbstractGene> genes) throws IOException {
        throw new UnsupportedOperationException("Method 'invokeEnsemble' not implemented for this invoker type.");
    }
	
	public IModelInvoker copy(int id);
}
