package evochecker.genetic.jmetal.single;

import java.util.List;

import org.spg.language.parser.InstantiatorInterface;

import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.jmetal.GeneticProblem;
import evochecker.properties.Property;

public class GeneticProblemSingle extends GeneticProblem {

	private static final long serialVersionUID = 1L;



	public GeneticProblemSingle(List<AbstractGene> genes, InstantiatorInterface instantiator,
			  List<Property> objectivesList, List<Property> constraintsList, String problemName){
		super(genes, instantiator, objectivesList, constraintsList, problemName);
	}
}
