package evochecker.parser.evolvable;

import java.util.ArrayList;
import java.util.List;

public class EvolvableDistribution extends Evolvable {
	
	private List<EvolvableDouble> evolvableDoubleList;
	private int cardinality;

	public EvolvableDistribution(String name, double[][] transitionsBounds){
		super(name, 0, 1, EvolvableID.DISTRIBUTION);
		
		this.evolvableDoubleList = new ArrayList<EvolvableDouble>();
		this.cardinality		 = transitionsBounds.length;
		generateEvolvableDoubleList(name, transitionsBounds);
	}
	
	
	private void generateEvolvableDoubleList(String name, double[][] transitionsBounds){
		int numOfTransitions = transitionsBounds.length;
		for (int transitionIndex=0; transitionIndex<numOfTransitions; transitionIndex++){
			double minValue 	= transitionsBounds[transitionIndex][0];
			double maxValue	= transitionsBounds[transitionIndex][1];
			this.evolvableDoubleList.add(new EvolvableDouble(name+(transitionIndex+1), minValue, maxValue));
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
	
	
	
	//Create identical object
	public EvolvableDistribution (EvolvableDistribution evolvableDistribution){
		super(evolvableDistribution.getName(), 0, 1, EvolvableID.DISTRIBUTION);
		
		this.cardinality		 = evolvableDistribution.cardinality;
		this.evolvableDoubleList = new ArrayList<EvolvableDouble>();

		int numOfTransitions = evolvableDistribution.evolvableDoubleList.size();
		for (int transitionIndex=0; transitionIndex<numOfTransitions; transitionIndex++){
			double minValue 	= (double) evolvableDistribution.evolvableDoubleList.get(transitionIndex).minValue;
			double maxValue 	= (double) evolvableDistribution.evolvableDoubleList.get(transitionIndex).maxValue;			
			this.evolvableDoubleList.add(new EvolvableDouble(name, minValue, maxValue));
		}		
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
