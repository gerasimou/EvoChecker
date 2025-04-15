package evochecker.seeding;

import java.util.List;

import evochecker.seeding.encoding.ParetoPoint;

public interface ISeeding {
	
	/**
	 * This method selects and returns the solutions to seed the population.
	 * @param prevSolutions: list of all previous solutions
	 * @param seedingNumSolutions: number of solutions to seed
	 * @return list of solutions to seed
	 */
	public List<ParetoPoint> getNSolutions(List<ParetoPoint> prevSolutions, Integer seedingNumSolutions);
		
	
	/**
	 * This method sets the parameters for the seeding strategy.
	 * @return list of parameters
	 */
	public void setParameters();
	
	
}
