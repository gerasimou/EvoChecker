package evochecker.parser.evolvable;

public class EvolvableInteger extends Evolvable{
	
	
	public EvolvableInteger(String name, Number minValue, Number maxValue){
		super(name, minValue, maxValue, EvolvableID.CONSTANT_INT);
	}
	
	public String toString(){
		String str = super.toString();
		str += "["+ this.minValue +":"+ this.maxValue +"]";
		return str;
	}
	
	@Override
	public String getCommand(Object variable) {
		return "const int " + name +" = "+ (int)variable  +";\n";
	}
	
}
