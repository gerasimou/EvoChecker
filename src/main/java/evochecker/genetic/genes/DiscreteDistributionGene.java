package evochecker.genetic.genes;

public class DiscreteDistributionGene extends AbstractGene {

	private int numberOfOutcomes;
	
	public int getNumberOfOutcomes() {
		return numberOfOutcomes;
	}

	public DiscreteDistributionGene(String name, int numberOfOutcomes) {
		super(name, 0.0, 1.0);
		this.numberOfOutcomes = numberOfOutcomes;
	}
	
}
