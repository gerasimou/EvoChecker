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

import java.util.ArrayList;
import java.util.List;


/**
 * Class representing an evolvable module (with alternatives) element
 * @author sgerasimou
 *
 */
public class EvolvableModuleAlternative extends Evolvable {

	/** a list of evolvable modules*/
	List<EvolvableModule> evolvableModuleList;
	
	
	/**
	 * Class constructor
	 * @param name
	 */
	public EvolvableModuleAlternative (String name){
		super(name, 0, 0, EvolvableID.MODULE);
		this.evolvableModuleList = new ArrayList<EvolvableModule>();
	}
	
	
	/** 
	 * create identical module
	 * @param evolvableModule
	 */
	public void addEvolvableModule (EvolvableModule evolvableModule){
		//TODO Fix inconsistency with AlternativeModuleGene
		this.evolvableModuleList.add(new EvolvableModule(evolvableModule));
		this.maxValue = ((int)maxValue + 1);
	}
	
	
	/**
	 * Get list of evolvable modules
	 * @return
	 */
	public List<EvolvableModule> getEvolvableModuleList(){
		return this.evolvableModuleList;
	}
	
	
	/**
	 * Return the number of alternative modules
	 */
	@Override
	public Number getMaxValue(){
		return super.getMaxValue();
	}

	
	/**
	 * Get command
	 */
	@Override
	public String getCommand(Object variable) {
		int index = (int)variable;
		return evolvableModuleList.get(index).moduleString + rewardStructureSpecific(index);
//		return "const double " + name +" = "+ (double)variables[0]  +";";
	}
	
	
	private String rewardStructureSpecific(int index){
		return "\nconst int "+ this.name.toUpperCase() +"="+ (index) + ";";
	}
	
	
	/**
	 * Class constructor
	 * @param name
	 */
	public EvolvableModuleAlternative (EvolvableModuleAlternative anEvolvable){
		super(anEvolvable.name, anEvolvable.minValue, anEvolvable.maxValue, EvolvableID.MODULE);
		this.evolvableModuleList = new ArrayList<EvolvableModule>();
		for (EvolvableModule evolvableModule : anEvolvable.evolvableModuleList){
			this.evolvableModuleList.add(new EvolvableModule(evolvableModule));
		}
	}
	
	
}
