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

public class ModelInstantiatorUltimate implements IModelInstantiator {

	/** map that keeps pairs of genes and evolvable elements*/
	protected Map<AbstractGene, Evolvable> elementsMap;

	protected ModelParser parser;
	
	
	protected Map<String, Object> concreteChromosome;
	
	public ModelInstantiatorUltimate(String modelFilename, String propertiesFilename) {
		parser = new ModelParser(modelFilename, propertiesFilename);
		
		elementsMap = new HashMap<AbstractGene, Evolvable>();
		
		concreteChromosome = new HashMap<String, Object>();
	}
	
	
	public ModelInstantiatorUltimate(String modelFilename, String propertiesFilename, ModelParser modelParser) {
		parser = modelParser;
		
		elementsMap = new HashMap<AbstractGene, Evolvable>();
		
		concreteChromosome = new HashMap<String, Object>();
	}
	
	/**
	 * Copy constructor
	 * @param instantiator
	 * @throws EvoCheckerException
	 */
	public ModelInstantiatorUltimate (ModelInstantiatorUltimate instantiator) throws EvoCheckerException{
		parser = new ModelParser(instantiator.parser);		
	}
	
	
	public void createMapping() {
		Map<AbstractGene, Evolvable> map = GenotypeFactory.getGeneEvolvableMap(); 
		for (Map.Entry<AbstractGene, Evolvable> entry : map.entrySet()) {
			this.elementsMap.put(entry.getKey(), entry.getValue());
		}
	}

	

	// concrete models are a bit of an issue because the way the ULTIMATE API works
	// is to simply take the internal parameters to be set and set them itself.
	// In this case the 'concrete model' is meant to be a String which defines the entire model
	//
	// I wonder if we could adapt ULTIMATE such that the internal parameters can be specified
	// in the .ultimate file. Certainly possible but a bit janky and would require ULTIMATE reading
	// that over and over.
	//
	// maybe I could actually have GeneticProblemUltimate avoid use of the concrete model and
	// rather set the internal parameters directly

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
