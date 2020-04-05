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
package evochecker.language.parser;

import java.util.List;

import evochecker.evolvables.Evolvable;
import evochecker.genetic.genes.AbstractGene;


/**
 * Interface used to instantiate a concrete model 
 * @author sgerasimou
 *
 */
public interface IModelInstantiator{
	
	
	/**
	 * Return a valid prism model instance
	 * @param genes
	 * @return
	 */
	public String getConcreteModel(List<AbstractGene> genes);
	
	
	/**
	 * Return the name of properties file
	 */
	public String getPropertyFileName();	
	
	
	public void createMapping();

	
	public List<Evolvable> getEvolvableList ();
}
