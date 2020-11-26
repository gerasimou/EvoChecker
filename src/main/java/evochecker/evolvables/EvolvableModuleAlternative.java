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

import java.util.ArrayList;
import java.util.List;

import evochecker.exception.EvoCheckerException;


/**
 * Class representing an evolvable module (with alternatives) element
 * @author sgerasimou
 *
 */
public class EvolvableModuleAlternative extends EvolvableRange {

	/** a list of evolvable modules*/
	List<EvolvableModule> evolvableModuleList;
	
	
	/**
	 * Class constructor
	 * @param name
	 */
	public EvolvableModuleAlternative (String name){
		super(name, 0, 0, EvolvableID.MODULE, false);//by default an alternative module changes the structure of the model, hence param = true
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
	 * Get command
	 */
	@Override
	public String getConcreteCommand(Object variable) {
		int index = (int)variable;
		return evolvableModuleList.get(index).getModuleString() + rewardStructureSpecific(index);
 	}
	
	
	private String rewardStructureSpecific(int index){
		return "\nconst int "+ this.name.toUpperCase() +"="+ (index) + ";";
	}
	
	
	public String toString(){
		String str = super.toString();
		str += " Modules: "+ evolvableModuleList.size();
		return str;
	}
	
	
	/**
	 * Copy constructor
	 * @param name
	 */
	public EvolvableModuleAlternative (EvolvableModuleAlternative anEvolvable){
		super(anEvolvable.name, anEvolvable.minValue, anEvolvable.maxValue, EvolvableID.MODULE, true);
		this.evolvableModuleList = new ArrayList<EvolvableModule>();
		for (EvolvableModule evolvableModule : anEvolvable.evolvableModuleList){
			this.evolvableModuleList.add(new EvolvableModule(evolvableModule));
		}
	}


	@Override
	public String getParametricCommand() {
		try {
			throw new EvoCheckerException ("Unsupported Function");
		} catch (EvoCheckerException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
