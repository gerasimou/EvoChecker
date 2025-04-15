package evochecker.seeding;

import java.util.List;

import evochecker.seeding.encoding.ParetoPoint;


/**
 * EvoChecker class
 * @author gricelvazquez
 * April 2025
 */
public class Random implements ISeeding {
	
	/**
	 * This method returns the first N solutions randomly from previous solutions.
	 * @param prevSolutions
	 * @param seedingNumSolutions
	 * @return
	 */
	@Override
	public List<ParetoPoint> getNSolutions(List<ParetoPoint> prevSolutions, Integer seedingNumSolutions) {
		return prevSolutions.subList(0, Math.min(prevSolutions.size(), seedingNumSolutions));
	}

	@Override
	public void setParameters() {
		// No parameters to set for random seeding
		return;
	}

}
