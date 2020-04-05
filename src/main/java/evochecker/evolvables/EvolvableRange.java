package evochecker.evolvables;

public abstract class EvolvableRange extends Evolvable{
	
	protected Number minValue;
	protected Number maxValue;

	protected EvolvableRange (String name, Number minValue, Number maxValue, EvolvableID evolvableID, boolean param){
		super(name, evolvableID, param);
		this.minValue	= minValue;
		this.maxValue	= maxValue;
	}
	
	
	public Number getMinValue(){
		return this.minValue;
	}
	
	public Number getMaxValue(){
		return this.maxValue;
	}		
}
