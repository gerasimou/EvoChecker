package evochecker.seeding;

import java.util.List;

import evochecker.seeding.auxiliary.ParetoPoint;
import jmetal.core.Solution;


/**
 * EvoChecker class
 * @author gricelvazquez
 * April 2025
 */
public class Random {
	
	/**
	 * This method returns the first N solutions randomly from previous solutions.
	 * @param prevSolutions
	 * @param seedingNumSolutions
	 * @return
	 */
	static List<ParetoPoint> getNSolutions(List<ParetoPoint> prevSolutions, Integer seedingNumSolutions) {
		return prevSolutions.subList(0, Math.min(prevSolutions.size(), seedingNumSolutions));
	}

}
