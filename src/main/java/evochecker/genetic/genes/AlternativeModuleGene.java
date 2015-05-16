package evochecker.genetic.genes;

public class AlternativeModuleGene extends AbstractGene {

	public AlternativeModuleGene(String name, int numberOfAlternatives) {
		super(name, 0, numberOfAlternatives-1);
	}
}
