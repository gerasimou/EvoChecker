package evochecker.genetic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.genes.AlternativeModuleGene;
import evochecker.genetic.genes.DiscreteDistributionGene;
import evochecker.genetic.genes.DoubleConstGene;
import evochecker.genetic.genes.IntegerConstGene;
import evochecker.parser.evolvable.Evolvable;
import evochecker.parser.evolvable.EvolvableDistribution;
import evochecker.parser.evolvable.EvolvableDouble;
import evochecker.parser.evolvable.EvolvableInteger;
import evochecker.parser.evolvable.EvolvableModule;
import evochecker.parser.evolvable.EvolvableModuleAlternative;

public class GenotypeFactory{
	
	private static Map<AbstractGene,Evolvable> elementsMap = new HashMap<AbstractGene, Evolvable>();

	public static List<AbstractGene> createChromosome(List<Evolvable> evolvableList) throws Exception{
		List<AbstractGene> genes = new ArrayList<AbstractGene> ();		
		for (Evolvable evolvable : evolvableList){
			AbstractGene gene = initialiseGene(evolvable);
			genes.add(gene);
			elementsMap.put(gene, evolvable);
		}
			return genes;		
	}
		
		
	private static AbstractGene initialiseGene (Evolvable evolvable) throws Exception{
		String name 	= evolvable.getName();
		Number minValue	= evolvable.getMinValue();
		Number maxValue = evolvable.getMaxValue();
		
		if (evolvable instanceof EvolvableDouble){
			return new DoubleConstGene(name, (double)minValue, (double)maxValue);
		}
		else if (evolvable instanceof EvolvableInteger){
			return new IntegerConstGene(name, (int)minValue, (int)maxValue);
		}
		else if (evolvable instanceof EvolvableDistribution){
			//TODO We do not consider specific bounds for distributions yet
			int numberOfOutcomes = ((EvolvableDistribution)evolvable).getCardinality();
			return new DiscreteDistributionGene(name, numberOfOutcomes);
		}
		else if (evolvable instanceof EvolvableModuleAlternative){
			int numberOfAlternatives = (int) ((EvolvableModuleAlternative)evolvable).getMaxValue();
			return new AlternativeModuleGene(name, numberOfAlternatives);
		}
		//TODO here we will modules that can be replicated - not supported at the moment **/
		else if (evolvable instanceof EvolvableModule){

		}

		throw new Exception ("Error in Genotype Factory");
	}

	public static Map<AbstractGene, Evolvable> getMapping(){
		return elementsMap;
	}	
}
