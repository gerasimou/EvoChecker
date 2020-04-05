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
import java.util.HashMap;
import java.util.LinkedHashMap;
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

public class EvoCheckerInstantiator extends ParserEngine implements IModelInstantiator {

	/** map that keeps pairs of genes and evolvable elements*/
	private Map<AbstractGene, Evolvable> elementsMap;

	List<AbstractGene> genesList;

	
	public EvoCheckerInstantiator(String modelFilename, String propertiesFilename) {
		super(modelFilename, propertiesFilename);
		
		elementsMap = new HashMap<AbstractGene, Evolvable>();
	}
	
	
	/**
	 * Copy constructor
	 * @param aParser
	 * @throws EvoCheckerException
	 */
	public EvoCheckerInstantiator (EvoCheckerInstantiator aParser) throws EvoCheckerException{
		super(aParser);
		
		List<Evolvable> evolvableList = getEvolvableList();
		
		genesList = GenotypeFactory.createChromosome(evolvableList);

		this.elementsMap					= new LinkedHashMap<AbstractGene, Evolvable>();
		for (int i=0; i<genesList.size(); i++){
			this.elementsMap.put(genesList.get(i), evolvableList.get(i));
		}
	}
	
	public void createMapping() {
		Map<AbstractGene, Evolvable> map = GenotypeFactory.getGeneEvolvableMap(); 
		for (Map.Entry<AbstractGene, Evolvable> entry : map.entrySet()) {
			this.elementsMap.put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public String getConcreteModel(List<AbstractGene> genes) {
		StringBuilder concreteModel = new StringBuilder(getInternalModelRepresentation());
		for (AbstractGene gene : genes) {
			if (gene instanceof IntegerGene) {
				concreteModel.append(elementsMap.get(gene).getCommand(gene.getAllele()));
			} 
			else if (gene instanceof DoubleGene) {
				concreteModel.append(elementsMap.get(gene).getCommand(gene.getAllele()));
			} 
			else if (gene instanceof DistributionGene) {
				concreteModel.append(elementsMap.get(gene)
										.getCommand((double[]) 
												gene.getAllele()));
			} 
			else if (gene instanceof AlternativeModuleGene) {
				concreteModel.append(elementsMap.get(gene).getCommand(gene.getAllele()));
			}
			
			
		}
		// System.err.println(concreteModel);
		return concreteModel.toString();
	}

	
	@Override
	public String getPropertyFileName() {
		return super.getPropertyFileName();
	}
	
	
	/**
	 * Get list of evolvable elements
	 * @return
	 */
	@Override
	public List<Evolvable> getEvolvableList (){
		return super.getEvolvableList();
	}
	
	public List<AbstractGene> getGeneList(){
		return (List<AbstractGene>)Arrays.asList(this.elementsMap.keySet().toArray(new AbstractGene[0]));				
	}

}
