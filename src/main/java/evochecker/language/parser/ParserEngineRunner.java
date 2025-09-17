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
package evochecker.language.parser;

import java.util.Arrays;

import evochecker.exception.EvoCheckerException;



public class ParserEngineRunner{

	
	public static void main (String args[]) throws EvoCheckerException {
		String study = "pomdp";
		
		String 		modelFilename		= "models/" + study +"/dressing-workflow.pomdp";
		String 		propertiesFilename 	= "models/" + study +"/dressing-workflow.pctl";
	
		System.out.println("Checking " + modelFilename);
		ModelParser parser = new ModelParser(modelFilename, propertiesFilename);
		parser.parse();
	
		String internalModel 		= parser.getInternalModelRepresentation();
		System.out.println(internalModel);

		String evolvablesStr 		= Arrays.toString(parser.getEvolvableList().toArray());
		System.out.println(evolvablesStr);
	}
}
