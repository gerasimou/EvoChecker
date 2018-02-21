package evochecker.properties;

public class Objective extends Property {

	protected boolean isMaximization;

	
	public Objective(boolean maximization, String expression) {
		super(maximization, expression);
		this.isMaximization = maximization;
	}
	
	
	public Objective (Objective clone) {
		this(clone.maximization, clone.expression);
	}


	@Override
	public double evaluate(double result) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	public String toString() {
		return expression +", Max?"+ maximization +"\n";		
	}
	

}
