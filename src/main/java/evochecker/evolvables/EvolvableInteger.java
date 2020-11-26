//==============================================================================
//	
 //	Copyright (c) 2015-
//	Authors:
//	* Simos Gerasimou (University of York)
//	
//------------------------------------------------------------------------------
//	
//	This file is part of EvoChecker.
//	
//==============================================================================

package evochecker.evolvables;

/**
 * Class representing and evolvable integer element
 * @author sgerasimou
 *
 */
public class EvolvableInteger extends EvolvableRange{
	
	/**
	 * Class constructor
	 * @param name
	 * @param minValue
	 * @param maxValue
	 */
	public EvolvableInteger(String name, Number minValue, Number maxValue, boolean param){
		super(name, minValue, maxValue, EvolvableID.CONSTANT_INT, param);
	}
	
	
	/**
	 * Print toString()
	 */
	public String toString(){
		String str = super.toString();
		str += "["+ this.minValue +":"+ this.maxValue +"]";
		return str;
	}

	
	/**
	 * Get command
	 */
	@Override
	public String getConcreteCommand(Object variable) {
		return "const int " + name +" = "+ (int)variable  +";\n";
	}
	
	
	/**
	 * Get parametric command 
	 */
	@Override
	public String getParametricCommand() {
		return "const int " + name +";\n";
	}
	
	public EvolvableInteger (EvolvableInteger anEvolvable){
		this(anEvolvable.name, anEvolvable.minValue, anEvolvable.maxValue, anEvolvable.param);
	}
	
}
