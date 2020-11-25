package evochecker.evolvables;

import java.util.List;


public class EvolvableOptionDouble extends EvolvableOption {
	
	public EvolvableOptionDouble (String name, List<Number> options, EvolvableID evolvableID, boolean param){
		super(name, options, evolvableID, param);
	}
	

	/**
	 * Get command
	 */
	@Override
	public String getConcreteCommand(Object variable) {
		int index = (int)variable;
		return "const double " + name +" = "+ (double)optionsList.get(index) +";\n";
	}
	
	
	/**
	 * Get command
	 */
	@Override
	public String getParametricCommand() {
		return "const double " + name +";\n";

	}
	
	public EvolvableOptionDouble (EvolvableOptionDouble anEvolvable){
		this(anEvolvable.name, anEvolvable.optionsList, EvolvableID.OPTION, anEvolvable.param);
	}
	
}
