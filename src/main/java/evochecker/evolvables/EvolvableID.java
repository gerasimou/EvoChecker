/**
 * 
 */
package evochecker.evolvables;

import evochecker.exception.LanguageException;

/**
 * An enumerator that keeps all the evolvable model identifies 
 * @author sgerasimou
 *
 */

public enum EvolvableID {
	CONSTANT_INT, CONSTANT_DOUBLE, DISTRIBUTION, MODULE, OPTION, BOOL;
	
	 public static String getEvolvableIDLiteral(EvolvableID evolvableID) {
		switch (evolvableID){
			case CONSTANT_INT:{
				return "int";
			}
			case CONSTANT_DOUBLE:{
				return "double";
			}
			case DISTRIBUTION:{
				return "dist";
			}
			case MODULE:{
				return "module";
			}
			case OPTION:{
				return "option";
			}
			case BOOL:{
				return "bool";
			}			
			default:{
				try {
					throw new LanguageException("Evolvable ID not recognised");
				}
				catch (LanguageException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}