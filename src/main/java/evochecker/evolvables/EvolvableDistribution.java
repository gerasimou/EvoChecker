package evochecker.evolvables;

import java.util.ArrayList;
import java.util.List;

public class EvolvableDistribution extends Evolvable {
	
	private List<EvolvableDouble> evolvableDoubleList;
	private int cardinality;

	public EvolvableDistribution(String name, double[][] transitionsBounds, boolean param){
		super(name, EvolvableID.DISTRIBUTION, param);
		
		this.evolvableDoubleList = new ArrayList<EvolvableDouble>();
		this.cardinality		 = transitionsBounds.length;
		generateEvolvableDoubleList(name, transitionsBounds);
	}
	
	
	/**
	 * Class constructor: create identical object
	 * @param name
	 */
	public EvolvableDistribution (EvolvableDistribution evolvableDistribution){
		super(evolvableDistribution.getName(), EvolvableID.DISTRIBUTION, evolvableDistribution.param);
		
		this.evolvableDoubleList = new ArrayList<EvolvableDouble>();
		this.cardinality		 = evolvableDistribution.cardinality;

		int numOfTransitions = evolvableDistribution.evolvableDoubleList.size();
		for (int transitionIndex=0; transitionIndex<numOfTransitions; transitionIndex++){
			double minValue 	= (double) evolvableDistribution.evolvableDoubleList.get(transitionIndex).minValue;
			double maxValue 	= (double) evolvableDistribution.evolvableDoubleList.get(transitionIndex).maxValue;			
			this.evolvableDoubleList.add(new EvolvableDouble(name+(transitionIndex+1), minValue, maxValue, evolvableDistribution.param));
		}		
	}
	
	
	private void generateEvolvableDoubleList(String name, double[][] transitionsBounds){
		int numOfTransitions = transitionsBounds.length;
		for (int transitionIndex=0; transitionIndex<numOfTransitions; transitionIndex++){
			double minValue 	= transitionsBounds[transitionIndex][0];
			double maxValue	= transitionsBounds[transitionIndex][1];
			this.evolvableDoubleList.add(new EvolvableDouble(name+(transitionIndex+1), minValue, maxValue, param));
		}
	}
	
	
	public List<EvolvableDouble> getEvolvableDoubleList(){
		return this.evolvableDoubleList;
	}
	
	
	public int getCardinality(){
		return this.cardinality;
	}
	
	
	@Override
	public String toString(){
		String str = super.toString() +  "["+ this.cardinality+"]";
		for (EvolvableDouble evolvable : evolvableDoubleList){
			str += "["+ evolvable.minValue +".."+ evolvable.maxValue +"]";
		}
		return str;
	}

	

	@Override
	public String getCommand(Object variable) {
		StringBuilder str = new StringBuilder();
		double[] transitionProb = (double[])variable;
		for (int index=0; index<transitionProb.length; index++){
			str.append(evolvableDoubleList.get(index).getCommand(transitionProb[index]));
		}
		return str.toString();
	}

		
}
