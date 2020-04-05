//==============================================================================
//	
//	Copyright (c) 2020-
//	Authors:
//	* Simos Gerasimou (University of York)
//  * Faisal Alhwikem (University of York)
//	
//------------------------------------------------------------------------------
//	
//	This file is part of EvoChecker.
//	
//==============================================================================
package evochecker.language.parser;

import java.util.List;

import evochecker.evolvables.Evolvable;

public interface IModelParser {

	
	public void printEvolvableElements();
	
	public String getInternalModelRepresentation();

	public List<Evolvable> getEvolvableList ();

	public String getPropertyFileName();

}
