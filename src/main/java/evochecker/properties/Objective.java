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

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Objective extends Property {

	protected boolean isMaximization;

	
	public Objective(boolean maximization, String expression, int index) {
		super(maximization, expression, index );
		this.isMaximization = maximization;
	}
	
	
	public Objective (Objective clone) {
		this(clone.maximization, clone.expression, clone.index);
	}


	@Override
	public double evaluate(double result) {
		if (isMaximization()) {
			return new BigDecimal(-result).setScale(3, RoundingMode.HALF_DOWN).doubleValue();
		}
		else{
			return new BigDecimal(result).setScale(3, RoundingMode.HALF_UP).doubleValue();
		}
	}
	
	
	public String toString() {
		return expression +", Max?"+ maximization +"\n";		
	}
	

}
