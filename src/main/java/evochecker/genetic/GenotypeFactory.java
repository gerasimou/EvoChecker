//==============================================================================
//	
 //	Copyright (c) 2015-
//	Authors:
//	* Simos Gerasimou (University of York)
//	
//------------------------------------------------------------------------------
//	
//	This file is part of EvoChecker.
//	
//==============================================================================
package evochecker.genetic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import evochecker.evolvables.Evolvable;
import evochecker.evolvables.EvolvableDistribution;
import evochecker.evolvables.EvolvableDouble;
import evochecker.evolvables.EvolvableInteger;
import evochecker.evolvables.EvolvableModule;
import evochecker.evolvables.EvolvableModuleAlternative;
import evochecker.evolvables.EvolvableOption;
import evochecker.evolvables.EvolvableRange;
import evochecker.exception.EvoCheckerException;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.genes.AlternativeModuleGene;
import evochecker.genetic.genes.DistributionGene;
import evochecker.genetic.genes.DoubleGene;
import evochecker.genetic.genes.IntegerGene;

/**
 * Factory constructing the genotype of an evochecker construct
 * @author sgerasimou
 *
 */
public class GenotypeFactory{
	
	/** Map between a gene and an evolvable element*/
	private static Map<AbstractGene,Evolvable> geneEvolvableMap = new HashMap<AbstractGene, Evolvable>();
	
	private static List<String> evolvableNames = new ArrayList<String>();

	/**
	 * Create a list chromosome (or individual) as a sequence of genes
	 */
	public static List<AbstractGene> createChromosome(List<Evolvable> evolvableList) throws EvoCheckerException{
		List<AbstractGene> genes = new ArrayList<AbstractGene> ();		
		for (Evolvable evolvable : evolvableList){
			AbstractGene gene = initialiseGene(evolvable);
			genes.add(gene);
			geneEvolvableMap.put(gene, evolvable);
		}
		if (genes.size()==0)
			throw new EvoCheckerException("The moodel contains no evovlables. EvoChecker stops");

		setupEvolvableNamesList(evolvableList);
		return genes;		
	}
		

	private static void setupEvolvableNamesList(List<Evolvable> evolvableList) throws EvoCheckerException{
		List<String> evolvableNamesI = new ArrayList<String>();
		List<String> evolvableNamesD = new ArrayList<String>();
		for (Evolvable evolvable : evolvableList){
			if (evolvable instanceof EvolvableDistribution)
				evolvableNamesD.addAll(Arrays.asList(((EvolvableDistribution)evolvable).getEvolvableDoubleNames()));
			else if (evolvable instanceof EvolvableDouble)
				evolvableNamesD.add(evolvable.getName());
			else 
				evolvableNamesI.add(evolvable.getName());
		}
		evolvableNames.addAll(evolvableNamesD);
		evolvableNames.addAll(evolvableNamesI);
	}
	
	
	/**
	 * Initialise this gene 
	 */
	private static AbstractGene initialiseGene (Evolvable evolvable) throws EvoCheckerException{
		String name 	= evolvable.getName();
		
		if (evolvable instanceof EvolvableDouble){
			Number minValue	= ((EvolvableRange)evolvable).getMinValue();
			Number maxValue = ((EvolvableRange)evolvable).getMaxValue();
			return new DoubleGene(name, (double)minValue, (double)maxValue);
		}
		else if (evolvable instanceof EvolvableInteger){
			Number minValue	= ((EvolvableRange)evolvable).getMinValue();
			Number maxValue = ((EvolvableRange)evolvable).getMaxValue();
			return new IntegerGene(name, (int)minValue, (int)maxValue);
		}
		else if (evolvable instanceof EvolvableDistribution){
			//TODO We do not consider specific bounds for distributions yet
			int numberOfOutcomes = ((EvolvableDistribution)evolvable).getCardinality();
			return new DistributionGene(name, numberOfOutcomes);
		}
		else if (evolvable instanceof EvolvableModuleAlternative){
			int numberOfAlternatives = (int) ((EvolvableModuleAlternative)evolvable).getMaxValue();
			return new AlternativeModuleGene(name, numberOfAlternatives);
		}
		//TODO here we will modules that can be replicated - not supported at the moment **/
		else if (evolvable instanceof EvolvableModule){
			throw new EvoCheckerException("EvolvableModule not yet supported");
		}
		else if (evolvable instanceof EvolvableOption){
			int maxValue = ((EvolvableOption)evolvable).getOptionsSize()-1;
			return new IntegerGene(name, 0, maxValue);
		}

		throw new EvoCheckerException ("Error in Genotype Factory");
	}

	
	/**
	 * Get the generated pair of genes and evolvable elements
	 * @return
	 */
	public static Map<AbstractGene, Evolvable> getGeneEvolvableMap(){
		return geneEvolvableMap;
	}	
	
	
	public static List<String> getEvolvableNames(){
		return evolvableNames;
	}
}
