package evochecker.parser.evolvable;

import java.util.ArrayList;
import java.util.List;

public class EvolvableModuleAlternative extends Evolvable {

	List<EvolvableModule> evolvableModuleList;
	
	public EvolvableModuleAlternative (String name){
		super(name, 0, 0, EvolvableID.MODULE);
		this.evolvableModuleList = new ArrayList<EvolvableModule>();
	}
	
	//create identical module
	public void addEvolvableModule (EvolvableModule evolvableModule){
		//TODO Fix inconsistency with AlternativeModuleGene
		this.evolvableModuleList.add(new EvolvableModule(evolvableModule));
		this.maxValue = ((int)maxValue + 1);
	}
	
	
	public List<EvolvableModule> getEvolvableModuleList(){
		return this.evolvableModuleList;
	}
	
	
	@Override
	/**
	 * Return the number of alternative modules
	 */
	public Number getMaxValue(){
		return super.getMaxValue();
	}
	
	@Override
	public String getCommand(Object variable) {
		int index = (int)variable;
		return evolvableModuleList.get(index).moduleString + rewardStructureSpecific(index);
//		return "const double " + name +" = "+ (double)variables[0]  +";";
	}
	
	private String rewardStructureSpecific(int index){
		return "\nconst int "+ this.name.toUpperCase() +"="+ (index) + ";";
	}
	
}
