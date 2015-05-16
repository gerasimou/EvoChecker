package evochecker.parser;

import java.util.List;

import evochecker.genetic.genes.AbstractGene;

public interface InstantiatorInterface {
	
	public String getPrismModelInstance(List<AbstractGene> individual);
	
	public String getPrismPropertyFileName();

}
