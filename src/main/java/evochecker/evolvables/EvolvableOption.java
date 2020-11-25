package evochecker.evolvables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import evochecker.exception.LanguageException;

public abstract class EvolvableOption extends Evolvable implements IStructuralEvolvable{
	
	protected List<Number> optionsList;

	public EvolvableOption (String name, List<Number> options, EvolvableID evolvableID, boolean param){
		super(name, evolvableID, param);
		optionsList = new ArrayList<Number>();
		for (Number option : options) 
			optionsList.add(option);
	}
	
	
	public int getOptionsSize() {
		return optionsList.size();
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
	public String getConcreteCommand(Object variable) {
		try {
			throw new LanguageException("EvolvableOption (" + this.getName() + ") currently unsupported ");
		}
		catch (LanguageException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return  null;
	}
	
	
	/**
	 * Get command
	 */
	@Override
	public String getParametricCommand() {
		try {
			throw new LanguageException("EvolvableOption (" + this.getName() + ") currently unsupported ");
		}
		catch (LanguageException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return  null;
	}
	
	
	public EvolvableOption (EvolvableOption anEvolvable){
		this(anEvolvable.name, anEvolvable.optionsList, EvolvableID.OPTION, anEvolvable.param);
	}
	
	
	public Number getOption (Object variable) {
		return optionsList.get((int)variable);
	}
	
}
