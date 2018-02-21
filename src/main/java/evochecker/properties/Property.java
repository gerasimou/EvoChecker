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

package evochecker.properties;


/**
 * Class representing a property to be checked
 * TODO: add constraint handling and property evaluation
 * @author sgerasimou
 *
 */
public abstract class Property {
	
	protected boolean maximization;
	
	protected String expression;
			

	public Property(boolean maximization, String expression){
		this.maximization 	= maximization;
		this.expression		= expression;
	}

	
	public boolean isMaximization() {
		return maximization;
	}

	
	public void setMaximization(boolean maximization) {
		this.maximization = maximization;
	}
	
	
	public Property (Property aProperty){
		this.maximization 	= aProperty.maximization;
	}
	
	
	public String getExpression() {
		return this.expression;
	}
	
	public abstract double evaluate (double result);// {return 0;};
	

}
