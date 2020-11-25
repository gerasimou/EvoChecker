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

import evochecker.genetic.genes.AbstractGene;


/**
 * Interface used to instantiate a concrete model 
 * @author sgerasimou
 *
 */
public interface IModelInstantiatorParametric extends IModelInstantiator{
	
	
	/**
	 * Return a valid <b>parametric</b> model instance
	 * @param genes
	 * @return
	 */
	public String getParametricModel(List<AbstractGene> genes, List<AbstractGene> structGenes);	
	
}
