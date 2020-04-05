package evochecker.evolvables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import evochecker.exception.LanguageException;

public class EvolvableOption extends Evolvable{
	
	protected List<Number> optionsList;
	protected VariableType type;

	public EvolvableOption (String name, List<Number> options, VariableType type, EvolvableID evolvableID, boolean param){
		super(name, evolvableID, param);
		this.type = type;
		optionsList = new ArrayList<Number>();
		for (Number option : options) 
			optionsList.add(option);
	}
	
	/**
	 * Print toString()
	 */
	public String toString(){
		String str = super.toString();
		str += Arrays.toString(optionsList.toArray());
		return str;
//		return getCommand(0);
	}

	
	/**
	 * Get command
	 */
	@Override
	public String getCommand(Object variable) {
		try {
			throw new LanguageException("EvolvableOption (" + this.getName() + ") currently unsupported ");
		}
		catch (LanguageException e) {
			e.printStackTrace();
			System.exit(0);
		}
//		return "const " + VariableType.getVariableTypeLiteral(type) + " " + name +" = "+ variable  +";\n";
		return  null;
	}
	
	public EvolvableOption (EvolvableOption anEvolvable){
		this(anEvolvable.name, anEvolvable.optionsList, anEvolvable.type, EvolvableID.OPTION, anEvolvable.param);
	}
	
}
