package evochecker.evolvable;

import java.util.ArrayList;
import java.util.List;

public class EvolvableModule extends EvolvableRange {
	
	List<EvolvableDistribution> evolvableDistributionList;
	String moduleString;
	
	public EvolvableModule(String name, int minValue, int maxValue, String moduleString, boolean param){
		super(name, minValue, maxValue, EvolvableID.MODULE, param);
		this.evolvableDistributionList = new ArrayList<EvolvableDistribution>();
		this.moduleString = new String(moduleString);
	}
	
	public EvolvableModule (EvolvableModule evolvableModule){
		this(evolvableModule.name, 0, 1, evolvableModule.moduleString, evolvableModule.param);
		for (EvolvableDistribution evolvableDistribution : evolvableModule.evolvableDistributionList){
			appendEvolvableDistribution(evolvableDistribution);
		}
	}

	
	public String getModuleString(){
		return this.moduleString;
	}
	
	
	public void setModuleString(String moduleString){
		this.moduleString = moduleString;
	}

	
	public void appendEvolvableDistribution(EvolvableDistribution evolvableDistribution){
		this.evolvableDistributionList.add(new EvolvableDistribution(evolvableDistribution));
	}
	
	
	public String toString(){
		String str = super.toString();
		str += "["+ this.minValue +":"+ this.maxValue +"]";
		str += moduleString;
		return str;
	}
	
	@Override
	public String getCommand(Object variable) {
		return "//Not Implemented Yet";//const double " + name +" = "+ (double)variables[0]  +";";
	}

	
}
