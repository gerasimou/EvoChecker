package evochecker.genetic.problem.parametric;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Archive extends ConcurrentHashMap<String, List<RationalFunction>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int hit;
	private int miss;
	private int badKey;
	private int missedParallel;
	private boolean verbose;
	
	public Archive(boolean verbose) {
		super();
		this.verbose = verbose;
	}
	
	public void hit() {
		hit++;
		print();
	}

	public void miss() {
		miss++;
		print();
	}
	
	public void missParallel() {
		missedParallel++;
	}

	public void badKey() {
		badKey++;
		print();
	}
	
	
	public int getHit() {
		return hit;
	}
	
	public int getMiss() {
		return miss;
	}
	
	public double getHitRatio() {
		return ((double)hit)/(hit+miss);
	}
	
	
	private void print() {
		if (verbose)
			System.out.print(toString());
	}
	
	public String toString() {
		return hit+"H/"+miss+"M/"+badKey+"B\t";
	}
	
	public String getStatistics() {
		return hit+"H/"+miss+"M/"+badKey+"B/"+missedParallel+"C";
	}
}
