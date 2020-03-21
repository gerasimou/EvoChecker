package evochecker.evolvable;

public abstract class Evolvable {
	
	protected String name;
	protected String identifier;
	protected boolean param;

	protected Evolvable (String name, EvolvableID evolvableID, boolean param){
		this.name 		= name;
		this.identifier	= EvolvableID.getEvolvableIDLiteral(evolvableID);// +name;
		this.param		= param;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getIdentifier (){
		return this.identifier;
	}
	
	public boolean isParam(){
		return this.param;
	}
	
	public String toString(){
		String paramStr = param ? ", param" : ""; 
		return (this.name +" ("+ this.identifier + paramStr +")"); 
	}
	
	public abstract String getCommand(Object variable);
}
