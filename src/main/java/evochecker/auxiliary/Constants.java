package evochecker.auxiliary;


/**
Collected constants of general utility.
<P>All members of this class are immutable. 
*/

public class Constants {

  /** The caller should be prevented from constructing objects of 
   * this class, by declaring this private constructor. 
   */
  private Constants() {
    //this prevents even the native class from 
    //calling this ctor as well :
    throw new AssertionError();
  }
  
  
  /** Keyword for probabilistic model template*/
  public static final String MODEL_FILE_KEYWORD 		= "MODEL_TEMPLATE_FILE";

  /** Keyword for probabilistic properties file*/
  public static final String PROPERTIES_FILE_KEYWORD 	= "PROPERTIES_FILE";

  /** Keyword for properties */
  public static final String OBJECTIVES_KEYWORD 		= "OBJECTIVES";

  /** Keyword for algorithm*/
  public static final String ALGORITHM_KEYWORD 		= "ALGORITHM";

  /** Keyword for tolerance*/
  public static final String TOLERANCE_KEYWORD 		= "TOLERANCE";

  /** Keyword for multiple tolerance values*/
  public static final String TOLERANCES_KEYWORD 		= "TOLERANCES";

  /** Keyword for multiple runs */
  public static final String RUNS_KEYWORD 			= "RUNS";
  
  /** Keyword for epsilon*/
  public static final String EPSILON_KEYWORD 		= "EPSILON";

  /** Keyword for multiple epsilon values*/
  public static final String EPSILONS_KEYWORD 		= "EPSILONS";

  /** Keyword for problem name*/
  public static final String PROBLEM_KEYWORD 		= "PROBLEM";
  
  /** Keyword for sensitivity*/
  public static final String SENSITIVITY_KEYWORD 		= "SENSITIVITY";
  
  /** Keyword for maximum evaluations*/
  public static final String MAX_EVALUATIONS_KEYWORD 	= "MAX_EVALUATIONS";

  /** Keyword for population size*/
  public static final String POPULATION_SIZE_KEYWORD 	= "POPULATION_SIZE";

  /** Keyword for processors*/
  public static final String PROCESSORS_KEYWORD 		= "PROCESSORS";

  /** Keyword for initial port number*/
  public static final String INITIAL_PORT_KEYWORD 	= "INIT_PORT";

  /** Keyword for initial JVM*/
  public static final String JVM_KEYWORD 			= "JVM";
  
  /** Keyword for interval */
  public static final String INTERVAL_KEYWORD 		= "INTERVAL";

  /** Keyword for dominance relation */
  public static final String DOMINANCE_KEYWORD 		= "DOMINANCE";
  
  /** Keyword for errors */
  public static final String ERRORS_KEYWORD 			= "ERRORS";

  /** Keyword for messages to be shown on the UI */
  public static final String MESSAGE_KEYWORD 		= "MESSAGE";
  
  /** Keyword for finishing execution  */
  public static final String DONE_KEYWORD 			= "DONE";
  
  /** Keyword for graph path*/
  public static final String GRAPH_KEYWORD 			= "GRAPH";

  /** Keyword for output directory*/
  public static final String OUTPUT_DIR_KEYWORD 		= "OUTPUT_DIR";

  /** Keyword for output file suffix*/
  public static final String OUTPUT_FILE_SUFFIX 		= "OUTPUT_FILE_SUFFIX";
  
  /** Keyword for model checking engine*/
  public static final String MODEL_CHECKING_ENGINE 	= "MODEL_CHECKING_ENGINE";

  /** Keyword for libraries required for running the model checking engine*/
  public static final String MODEL_CHECKING_ENGINE_LIBS_DIR = "MODEL_CHECKING_ENGINE_LIBS_DIRECTORY";

  
  
  /** Integer REGEX*/
  public static  final String INTEGER_REGEX = "^\\d+$";

  /** Double REGEX*/
  public static final String DOUBLE_REGEX  = "[0-9]+(.[0-9]+)?";

  
  
  /** Algorithms currently supported by our implementation*/
  public static enum ALGORITHM{
	    NSGAII,
	    SPEA2,
	    MOCELL,
	    RANDOM
	    ;
	}
  
  /** Dominance relations currently supported by our implementation*/
  public static enum DOMINANCE{
	  eDominanceWorstCaseDominance 			("evochecker.genetic.jmetal.util.eDominanceWorstCaseDominanceComparator"),
	  eDominanceRevisedWorstCaseDominance	("evochecker.genetic.jmetal.util.eDominanceRevisedWorstCaseDominanceComparator")
	  ;
	  
	  
	  private String comparatorPath;
	  
	  DOMINANCE(String path){
//		  try {
//			  Class clazz = Class.forName(path);
//			  comparator = (RegionDominanceComparator)clazz.newInstance();
//		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
//			e.printStackTrace();
//		}
		  this.comparatorPath = path;
//		  System.out.println(comparatorPath +"\t"+ path);
	  }
  }
}