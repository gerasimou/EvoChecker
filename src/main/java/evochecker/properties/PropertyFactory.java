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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.spg.PrismAPI.PrismAPI;

import evochecker.auxiliary.Constants;
import evochecker.auxiliary.Utility;
import evochecker.exception.EvoCheckerException;
import parser.ast.PropertiesFile;

public class PropertyFactory {

	private static String propertiesFilename	= Utility.getProperty(Constants.PROPERTIES_FILE_KEYWORD);
	
	private final static String OBJECTIVE 	= "OBJECTIVE";
	private final static String CONSTRAINT 	= "CONSTRAINT";
	private final static String MAX 		= "MAX";
	private final static String MIN		 	= "MIN";
	
	
	public static List<List<Property>> getObjectivesConstraints(String internalModel){
		List<Property> objectivesList = new ArrayList<Property>();		
		List<Property> constaintsList = new ArrayList<Property>();		
		
		
		try {
			PrismAPI api = new PrismAPI(null);
			api.parseModelAndProperties(internalModel, propertiesFilename);
	
			PropertiesFile  propsFile = api.getPrismPropertiesFile();
			
			
			int numProps = propsFile.getNumProperties();
			for (int index=0; index<numProps; index++) {
				parser.ast.Property prop = propsFile.getPropertyObject(index);

//				System.out.println(prop);
				String comment = prop.getComment();
				if (comment != null) {
					String[] commentElements = comment.trim().split(",");
					
					if (commentElements[0].trim().toUpperCase().equals(OBJECTIVE))
						objectivesList.add(createObjective(commentElements, prop.toString(), index));					
					else if (commentElements[0].trim().toUpperCase().equals(CONSTRAINT))
						constaintsList.add(createConstraint(commentElements, prop.toString(), index));
					else 
						throw new EvoCheckerException("Property " + prop + " is neither a constraint nor an objective "+ prop.getComment());
				}
			}
			
			if (objectivesList.isEmpty())
				throw new EvoCheckerException("No objective found.At least one is required!");
				
			
			List<List<Property>> list= new ArrayList<>();
			list.add(objectivesList);
			list.add(constaintsList);
			return list;
		} 
		catch (EvoCheckerException e) {
			e.printStackTrace();
		}	
		//never happens
		return null;
	}
 
	
	private static Objective createObjective(String[] objElements, String prop, int index) throws EvoCheckerException {
		
		if (objElements.length != 2)
			throw new EvoCheckerException("2 elements are required for specifying an objective (Objective, MAX|MIN), " 
										   + objElements.length +" provided!");
		
		if (objElements[1].trim().toUpperCase().equals(MAX)) 
				return new Objective(true, prop, index);
		else if (objElements[1].trim().toUpperCase().equals(MIN))
			return new Objective(false, prop, index);
		else
			throw new EvoCheckerException("MAX|MIN not specified for objective: " + prop);			
	}
	
	
	private static Constraint createConstraint(String[] constraintElements, String prop, int index) throws EvoCheckerException {		
		if (constraintElements.length != 3)
			throw new EvoCheckerException("3 elements are required for specifying a constraint (Constraint, MAX|MIN, Limit), " 
					   + "Provided: " + Arrays.toString(constraintElements));
		
		String maxMin 	= constraintElements[1].trim().toUpperCase();
		String limit 	= constraintElements[2].trim().toUpperCase();
		
		if (maxMin.equals(MAX) && isDouble(limit) )
				return new Constraint(true, Double.parseDouble(limit), prop, index);
		else  if (maxMin.equals(MIN) && isDouble(limit) )
				return new Constraint(false, Double.parseDouble(limit), prop, index);
		else
			throw new EvoCheckerException("Incorrect specification "+ Arrays.toString(constraintElements) +" for constraints: " + prop);
			
	}
	
	
	
	private static boolean isDouble (final String str) {
		try {
			Double.valueOf(str);
			return true;
		}
		catch (NumberFormatException e) {
			return false;
		}
	}
}
