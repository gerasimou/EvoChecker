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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import evochecker.evolvables.Evolvable;
import evochecker.exception.EvoCheckerException;
import evochecker.genetic.GenotypeFactory;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.genes.AlternativeModuleGene;
import evochecker.genetic.genes.DistributionGene;
import evochecker.genetic.genes.DoubleGene;
import evochecker.genetic.genes.IntegerGene;

public class ModelInstantiator implements IModelInstantiator {

	/** map that keeps pairs of genes and evolvable elements*/
	protected Map<AbstractGene, Evolvable> elementsMap;

	protected ModelParser parser;
	
	
	protected Map<String, Object> concreteChromosome;
	
	public ModelInstantiator(String modelFilename, String propertiesFilename) {
		parser = new ModelParser(modelFilename, propertiesFilename);
		
		elementsMap = new HashMap<AbstractGene, Evolvable>();
		
		concreteChromosome = new HashMap<String, Object>();
	}
	
	
	public ModelInstantiator(String modelFilename, String propertiesFilename, ModelParser modelParser) {
		parser = modelParser;
		
		elementsMap = new HashMap<AbstractGene, Evolvable>();
		
		concreteChromosome = new HashMap<String, Object>();
	}
	
	/**
	 * Copy constructor
	 * @param instantiator
	 * @throws EvoCheckerException
	 */
	public ModelInstantiator (ModelInstantiator instantiator) throws EvoCheckerException{
		parser = new ModelParser(instantiator.parser);		
	}
	
	
	public void createMapping() {
		Map<AbstractGene, Evolvable> map = GenotypeFactory.getGeneEvolvableMap(); 
		for (Map.Entry<AbstractGene, Evolvable> entry : map.entrySet()) {
			this.elementsMap.put(entry.getKey(), entry.getValue());
		}
	}

	
	@Override
	public String getConcreteModel(Collection<AbstractGene> genes) {
		StringBuilder concreteModel = new StringBuilder(parser.getModelType().toString().toLowerCase() +"\n\n");
		
		for (AbstractGene gene : genes) {
			if (gene instanceof IntegerGene) {
				concreteModel.append(elementsMap.get(gene).getConcreteCommand(gene.getAllele()));
			} 
			else if (gene instanceof DoubleGene) {
				concreteModel.append(elementsMap.get(gene).getConcreteCommand(gene.getAllele()));
			} 
			else if (gene instanceof DistributionGene) {
				concreteModel.append(elementsMap.get(gene)
										.getConcreteCommand((double[]) 
												gene.getAllele()));
			} 
			else if (gene instanceof AlternativeModuleGene) {
				concreteModel.append(elementsMap.get(gene).getConcreteCommand(gene.getAllele()));
			}	
		}
		
		concreteModel.append("\n" + parser.getInternalModelRepresentation());
		
		// System.err.println(concreteModel);
		return concreteModel.toString();
	}

	
	@Override
	public String getPropertyFileName() {
		return parser.getPropertyFileName();
	}
	
	
	/**
	 * Get list of evolvable elements
	 * @return
	 */
	@Override
	public List<Evolvable> getEvolvableList (){
		return parser.getEvolvableList();
	}
	
	public List<AbstractGene> getGeneList(){
		return (List<AbstractGene>)Arrays.asList(this.elementsMap.keySet().toArray(new AbstractGene[0]));				
	}
	
	
	public Map<String, Object> getChromosome (List<AbstractGene> genes) {
		concreteChromosome.clear();
		
		for (AbstractGene gene : genes) {
			concreteChromosome.put(elementsMap.get(gene).getName(), gene.getAllele());
		}
		return concreteChromosome;
	}
	
	
	protected String getInternalModelRepresentation() {
		return parser.getInternalModelRepresentation();
	}


	@Override
	public MODEL_TYPE getModelType() {
		return parser.getModelType();
	}
	
	
}
