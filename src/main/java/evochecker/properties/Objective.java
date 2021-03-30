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

import evochecker.auxiliary.Constants;
import evochecker.auxiliary.Utility;

public class Objective extends Property {

	private int precision;
	
	public Objective(boolean maximization, String expression, int index) {
		super(maximization, expression, index );
		precision = Integer.parseInt(Utility.getProperty(Constants.DOUBLE_PRECISION, "4"));
	}
	
	
	public Objective (Objective clone) {
		this(clone.maximization, clone.expression, clone.index);
	}


	@Override
	public double evaluate(double result) {
		try {
			if (isMaximization()) {
				return new BigDecimal(-result).setScale(precision, RoundingMode.HALF_DOWN).doubleValue();
			}
			else{
				return new BigDecimal(result).setScale(precision, RoundingMode.HALF_UP).doubleValue();
			}
		}
		catch (NumberFormatException e) {
			if (isMaximization()) {
				return 0.00;
			}
			else{
				return Double.MAX_VALUE;
			}
		}
	}
	
	
	public String toString() {
		return expression +", Max?"+ maximization +"\n";		
	}
	

}
