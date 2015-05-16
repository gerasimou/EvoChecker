package evochecker.genetic.genes;

public abstract class AbstractGene {
	
	private Number minValue;
	private Number maxValue;
	private String name;
	private Object allele;
	
	public AbstractGene(String name, Number minValue, Number maxValue){
		this.name = name;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	public String getName(){
		return this.name;
	}
	
	public Number getMinValue() {
		return minValue;
	}

	public Number getMaxValue() {
		return maxValue;
	}
	
	public Object getAllele(){
		return this.allele;
	}
	
	public void setAllele(Object allele){
		this.allele = allele;
	}
}
