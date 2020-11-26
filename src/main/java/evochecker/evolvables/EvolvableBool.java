package evochecker.evolvables;

public class EvolvableBool extends Evolvable{

	public EvolvableBool(String name, EvolvableID evolvableID, boolean param) {
		super(name, evolvableID, param);
	}

	@Override
	public String getConcreteCommand(Object variable) {
		return "const bool " + name +" = "+ ((int)variable==1?"true":"false")  +";\n";	
	}

	
	@Override
	public String getParametricCommand() {
		return "const bool " + name +";\n";
	}
	

	/**
	 * Print toString()
	 */
	public String toString(){
		return super.toString();
	}

}
