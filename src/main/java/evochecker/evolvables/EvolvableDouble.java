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
 * Class representing an evolvable double element
 * @author sgerasimou
 *
 */
public class EvolvableDouble extends EvolvableRange{
	
	/**
	 * Class constructor
	 * @param name
	 * @param minValue
	 * @param maxValue
	 */
	public EvolvableDouble(String name, Number minValue, Number maxValue, boolean param){
		super(name, minValue, maxValue, EvolvableID.CONSTANT_DOUBLE, param);
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
	 * Get concrete command 
	 */
	@Override
	public String getConcreteCommand(Object variable) {
		//change made to accommodate PrismPSY
//		return "const double " + name +";";
		return "const double " + name +" = "+ (double)variable  +";\n";
	}

	
	/**
	 * Get parametric command 
	 */
	@Override
	public String getParametricCommand() {
		return "const double " + name +";\n";
	}

	
	public EvolvableDouble (EvolvableDouble anEvolvable){
		this(anEvolvable.name, anEvolvable.minValue, anEvolvable.maxValue, anEvolvable.param);
	}
}

