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

package evochecker.evolvable;

/**
 * Class representing and evolvable integer element
 * @author sgerasimou
 *
 */
public class EvolvableInteger extends Evolvable{
	
	/**
	 * Class constructor
	 * @param name
	 * @param minValue
	 * @param maxValue
	 */
	public EvolvableInteger(String name, Number minValue, Number maxValue){
		super(name, minValue, maxValue, EvolvableID.CONSTANT_INT);
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
	public String getCommand(Object variable) {
		return "const int " + name +" = "+ (int)variable  +";\n";
	}
	
	public EvolvableInteger (Evolvable anEvolvable){
		this(anEvolvable.name, anEvolvable.minValue, anEvolvable.maxValue);
	}
	
}
