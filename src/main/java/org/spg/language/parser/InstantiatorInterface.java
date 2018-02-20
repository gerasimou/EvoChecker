package org.spg.language.parser;

import java.util.List;

import evochecker.genetic.genes.AbstractGene;


/**
 * Interface used to instantiate a concrete model 
 * @author sgerasimou
 *
 */
public interface InstantiatorInterface {
	
	
	/**
	 * Return a valid prism model instance
	 * @param individual
	 * @return
	 */
	public String getValidModelInstance(List<AbstractGene> individual);
	
	
	/**
	 * Return the name of properties file
	 */
	public String getPrismPropertyFileName();	
}
