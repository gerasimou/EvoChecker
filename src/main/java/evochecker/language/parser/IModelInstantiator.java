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

import java.util.Collection;
import java.util.List;
import java.util.Map;

import evochecker.evolvables.Evolvable;
import evochecker.genetic.genes.AbstractGene;


/**
 * Interface used to instantiate a concrete model 
 * @author sgerasimou
 *
 */
public interface IModelInstantiator {
	
	
	/**
	 * Return a valid model instance
	 * @param genes
	 * @return
	 */
	public String getConcreteModel(Collection<AbstractGene> genes);
	
	
	/**
	 * Return the name of properties file
	 */
	public String getPropertyFileName();	
	
	
	public void createMapping();

	
	public List<Evolvable> getEvolvableList ();

	public List<AbstractGene> getGeneList();
	
	
	public Map<String, Object> getChromosome (List<AbstractGene> genes);
	
	public MODEL_TYPE getModelType();
}
