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

import evochecker.evolvables.EvolvableDistribution;
import evochecker.evolvables.EvolvableDouble;
import evochecker.evolvables.EvolvableInteger;
import evochecker.exception.EvoCheckerException;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.genes.AlternativeModuleGene;
import evochecker.genetic.genes.DistributionGene;
import evochecker.genetic.genes.DoubleGene;
import evochecker.genetic.genes.IntegerGene;

public class ModelInstantiatorParametric extends ModelInstantiator implements IModelInstantiatorParametric {

	public ModelInstantiatorParametric(String modelFilename, String propertiesFilename) {
		super(modelFilename, propertiesFilename);
	}
	
	
	/**
	 * Copy constructor
	 * @param aParser
	 * @throws EvoCheckerException
	 */
	public ModelInstantiatorParametric (ModelInstantiatorParametric aParser) throws EvoCheckerException{
		super(aParser);
	}
	

	@Override
	public String getParametricModel(List<AbstractGene> genes, List<AbstractGene> structGenes) {
		StringBuilder parametricModel = new StringBuilder(parser.getInternalModelRepresentation());
		for (AbstractGene gene : structGenes) {
			if (gene instanceof IntegerGene) {
				parametricModel.append(elementsMap.get(gene).getConcreteCommand(gene.getAllele()));
			} 
			else if (gene instanceof DoubleGene) {
				parametricModel.append(elementsMap.get(gene).getConcreteCommand(gene.getAllele()));
			} 
			else if (gene instanceof DistributionGene) {
				parametricModel.append(elementsMap.get(gene)
										.getConcreteCommand((double[]) 
												gene.getAllele()));
			} 
			else if (gene instanceof AlternativeModuleGene) {
				parametricModel.append(elementsMap.get(gene).getConcreteCommand(gene.getAllele()));
			}	
		}
		
		for (AbstractGene gene : genes) {
			if (!structGenes.contains(gene)) {
				if (gene instanceof IntegerGene) {
					parametricModel.append(((EvolvableInteger)elementsMap.get(gene)).getParametricCommand());
				} 
				else if (gene instanceof DoubleGene) {
					parametricModel.append(((EvolvableDouble)elementsMap.get(gene)).getParametricCommand());
				} 
				else if (gene instanceof DistributionGene) {
					parametricModel.append(((EvolvableDistribution)elementsMap.get(gene)).getParametricCommand());
				} 
				else
					throw new IllegalArgumentException("Incorrectly generated chromosome");				
			}
		}
		return parametricModel.toString();		
	}
}
