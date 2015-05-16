package evochecker.parser.evolvable;

public abstract class Evolvable {
	
	protected String name;
	protected String identifier;
	protected Number minValue;
	protected Number maxValue;

	public Evolvable (String name, Number minValue, Number maxValue, EvolvableID evolvableID){
		this.name 		= name;
		this.minValue	= minValue;
		this.maxValue	= maxValue;
		this.identifier	= EvolvableID.getEvolvableIDLiteral(evolvableID) + name; 
	}
	
	
	public Number getMinValue(){
		return this.minValue;
	}
	
	public Number getMaxValue(){
		return this.maxValue;
	}
		
	public String getName(){
		return this.name;
	}
	
	public String getIdentifier (){
		return this.identifier;
	}
	
	public String toString(){
		return (this.name +" ("+ this.identifier +")"); 
	}
	
	public abstract String getCommand(Object variable);
}
