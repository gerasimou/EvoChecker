package evochecker.parser.evolvable;

public class EvolvableDouble extends Evolvable {
	
	public EvolvableDouble(String name, Number minValue, Number maxValue){
		super(name, minValue, maxValue, EvolvableID.CONSTANT_DOUBLE);
	}
	
	public String toString(){
		String str = super.toString();
		str += "["+ this.minValue +":"+ this.maxValue +"]";
		return str;
	}


	@Override
	public String getCommand(Object variable) {
		return "const double " + name +" = "+ (double)variable  +";\n";
	}
}
