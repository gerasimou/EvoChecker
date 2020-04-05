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
	
	/** indicating its position in the properties file
	 *  this is used later when evaluating the solutions **/
	protected int index;
			

	public Property(boolean maximization, String expression, int index){
		this.maximization 	= maximization;
		this.expression		= expression;
		this.index			= index;
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
	
	
	public int getIndex() {
		return this.index;
	}
	
	public abstract double evaluate (double result);// {return 0;};
	

}
