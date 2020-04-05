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
package evochecker.evolvables;

public enum VariableType {
	INT, DOUBLE;
	
	 public static String getVariableTypeLiteral(VariableType varType){
		switch (varType){
			case INT:{
				return "int";
			}
			case DOUBLE:{
				return "double";
			}
			default:{
				throw new RuntimeException("Variable Type not recognised");
			}
		}
	}
}
