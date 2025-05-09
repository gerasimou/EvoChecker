package evochecker.seeding;

import java.util.List;

import evochecker.seeding.encoding.ParetoPoint;
import evochecker.seeding.encoding.PreviousPareto;

public interface ISeeding {
	
	/**
	 * This method selects and returns the solutions to seed the population.
	 * @param prevSolutions: list of all previous solutions
	 * @param seedingNumSolutions: number of solutions to seed
	 * @return list of solutions to seed
	 */
	public List<ParetoPoint> getNSolutions(PreviousPareto prevPareto, Integer seedingNumSolutions);
		
	
	/**
	 * This method sets the parameters for the seeding strategy.
	 * @return list of parameters
	 */
	public void setParameters();
	
	
}
