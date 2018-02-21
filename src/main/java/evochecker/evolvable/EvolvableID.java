/**
 * 
 */
package evochecker.evolvable;

/**
 * An enumerator that keeps all the evolvable model identifies 
 * @author sgerasimou
 *
 */

public enum EvolvableID {
	CONSTANT_INT, CONSTANT_DOUBLE, DISTRIBUTION, MODULE;
	
	 public static char getEvolvableIDLiteral(EvolvableID evolvableID){
		switch (evolvableID){
			case CONSTANT_INT:{
				return '%';
			}
			case CONSTANT_DOUBLE:{
				return '%';
			}
			case DISTRIBUTION:{
				return '#';
			}
			case MODULE:{
				return '$';
			}
			default:{
				throw new RuntimeException("Evolvable ID not recognised");
			}
		}
	}
}
