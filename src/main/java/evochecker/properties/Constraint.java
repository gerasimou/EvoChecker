//==============================================================================
//	
 //	Copyright (c) 2015-
//	Authors:
//	* Simos Gerasimou (University of York)
//  * Faisal Alhwikem (University of York)
//	
//------------------------------------------------------------------------------
//	
//	This file is part of EvoChecker.
//	
//==============================================================================
package evochecker.properties;

public class Constraint extends Property {

	private final double constraint;
	
	private final double VIOLATION_CONSTANT=100; 
	
	public Constraint(boolean maximization, double constraint, String expression, int index) {
		super(maximization, expression, index);
		this.constraint = constraint;
	}
	
	
	public Constraint (Constraint clone) {
		this(clone.maximization, clone.constraint, clone.expression, clone.index);
	}
	
	
	/**
	 * Constraints are inclusive
	 */
	@Override
	public double evaluate(double result) {
		if ((maximization) &&
			(result < constraint))
				return ((result-constraint) * VIOLATION_CONSTANT);
		else if ( (!maximization) &&
				(result > constraint))
				return ((constraint-result) * VIOLATION_CONSTANT);
		else
			return 0;
	}

	
	public String toString() {
		return expression +", Limit=" + constraint +", Max?"+ maximization +"\n";		
	}

}
