package evochecker.genetic.jmetal.single;

import jmetal.core.SolutionSet;
import jmetal.util.JMException;

public interface AlgorithmSteps {
	
	
  
	/**
   * Do any necessary initialisations
   */
	public void initialise();
	
  
	/**
	* Do any necessary cleanup
	*/
	public void finalise();
	
	
	/**
	 * Execute algorithm logic
	 * @return
	 * @throws JMException
	 * @throws ClassNotFoundException
	 */
	public SolutionSet execute() throws JMException, ClassNotFoundException;


}
