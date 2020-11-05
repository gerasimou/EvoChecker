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
	
	public Archive() {
		super();
	}
	
	public void hit() {
		hit++;
		System.out.print(toString());
	}

	public void miss() {
		miss++;
		System.out.print(toString());
	}

	public void badKey() {
		badKey++;
		System.out.print(toString());
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
	
	
	public String toString() {
		return hit+"H/"+miss+"M/"+badKey+"B\t";
	}
}
