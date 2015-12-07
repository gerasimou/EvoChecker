package evochecker.parser;

import java.util.List;

import evochecker.genetic.genes.AbstractGene;

public interface InstantiatorInterface {
	
	
	/**
	 * Return a valid prism model instance
	 * @param individual
	 * @return
	 */
	public String getPrismModelInstance(List<AbstractGene> individual);
	
	
	/**
	 * Return the name of properties file
	 */
	public String getPrismPropertyFileName();

}
