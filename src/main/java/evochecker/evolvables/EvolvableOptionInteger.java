package evochecker.evolvables;

import java.util.List;


public class EvolvableOptionInteger extends EvolvableOption {
	
	public EvolvableOptionInteger (String name, List<Number> options, EvolvableID evolvableID, boolean param){
		super(name, options, evolvableID, param);
	}
	

	/**
	 * Get command
	 */
	@Override
	public String getConcreteCommand(Object variable) {
		int index = (int)variable;
		return "const int " + name +" = "+ (int)optionsList.get(index) +";\n";
	}
	
	
	/**
	 * Get command
	 */
	@Override
	public String getParametricCommand() {
		return "const int " + name +";\n";
	}
	
	public EvolvableOptionInteger (EvolvableOptionInteger anEvolvable){
		this(anEvolvable.name, anEvolvable.optionsList, EvolvableID.OPTION, anEvolvable.param);
	}
	
}
