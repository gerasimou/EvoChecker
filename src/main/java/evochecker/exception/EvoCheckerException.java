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

package evochecker.exception;

/**
 * Class representing an evochecker exception
 * @author sgerasimou
 *
 */
public class EvoCheckerException extends Exception {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3322424663356101425L;

	public EvoCheckerException(String s)
	{
		super(s);
	}
	
	public String toString()
	{
		return "Error: " + getMessage() + ".";
	}
}
