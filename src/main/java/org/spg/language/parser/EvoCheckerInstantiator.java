package org.spg.language.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import evochecker.evolvable.Evolvable;
import evochecker.evolvable.EvolvableDistribution;
import evochecker.evolvable.EvolvableDouble;
import evochecker.evolvable.EvolvableInteger;
import evochecker.evolvable.EvolvableModuleAlternative;
import evochecker.exception.EvoCheckerException;
import evochecker.genetic.GenotypeFactory;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.genes.AlternativeModuleGene;
import evochecker.genetic.genes.DistributionGene;
import evochecker.genetic.genes.DoubleGene;
import evochecker.genetic.genes.IntegerGene;

public class EvoCheckerInstantiator extends ParserEngine2 implements InstantiatorInterface {

	/** map that keeps pairs of genes and evolvable elements*/
	private Map<AbstractGene, Evolvable> elementsMap;

	List<AbstractGene> genesList;

	
	public EvoCheckerInstantiator(String fileName, String propertiesFilename) {
		super(fileName, propertiesFilename);
		
		elementsMap = new HashMap<AbstractGene, Evolvable>();
	}
	
	
	/**
	 * Copy constructor
	 * @param aParser
	 * @throws EvoCheckerException
	 */
	public EvoCheckerInstantiator (EvoCheckerInstantiator aParser) throws EvoCheckerException{
		EvoCheckerInstantiator parser = (EvoCheckerInstantiator)aParser;
		this.internalModelRepresentation	= parser.internalModelRepresentation;
		this.propertiesFilename				= parser.propertiesFilename;
		this.evolvableList					= new ArrayList<Evolvable>();
		for (Evolvable element : parser.evolvableList)
			if (element instanceof EvolvableInteger)
				this.evolvableList.add(new EvolvableInteger((EvolvableInteger)element));
			else if (element instanceof EvolvableDouble)
				this.evolvableList.add(new EvolvableDouble((EvolvableDouble)element));
			else if (element instanceof EvolvableDistribution)
				this.evolvableList.add(new EvolvableDistribution((EvolvableDistribution)element));
			else if (element instanceof EvolvableModuleAlternative)
				this.evolvableList.add(new EvolvableModuleAlternative((EvolvableModuleAlternative)element));
//			else if (element instanceof EvolvableOption)
//				this.evolvableList.add(new EvolvableOption((EvolvableOption))element);
		
		genesList = GenotypeFactory.createChromosome(evolvableList);
//		
////		GenotypeFactory.createChromosome(evolvableList);
		this.elementsMap					= new LinkedHashMap<AbstractGene, Evolvable>();
		for (int i=0; i<genesList.size(); i++){
			this.elementsMap.put(genesList.get(i), evolvableList.get(i));
		}
	}
	
	public void createMapping() {
		Map<AbstractGene, Evolvable> map = GenotypeFactory.getMapping();
		for (Map.Entry<AbstractGene, Evolvable> entry : map.entrySet()) {
			this.elementsMap.put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public String getValidModelInstance(List<AbstractGene> genes) {
		StringBuilder concreteModel = new StringBuilder(this.internalModelRepresentation);
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
	public String getPrismPropertyFileName() {
		return this.propertiesFilename;
	}
	
	public List<AbstractGene> getGeneList(){
		return (List<AbstractGene>)Arrays.asList(this.elementsMap.keySet().toArray(new AbstractGene[0]));				
	}

}
