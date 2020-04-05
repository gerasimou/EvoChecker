package evochecker.genetic.problem;

import java.util.List;

import evochecker.genetic.genes.AbstractGene;
import evochecker.language.parser.IModelInstantiator;
import evochecker.properties.Property;

public class GeneticProblemSingle extends GeneticProblem {

	private static final long serialVersionUID = 1L;



	public GeneticProblemSingle(List<AbstractGene> genes, IModelInstantiator instantiator,
			  List<Property> objectivesList, List<Property> constraintsList, String problemName){
		super(genes, instantiator, objectivesList, constraintsList, problemName);
	}
}
