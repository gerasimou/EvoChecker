// See documentation for wrapper: https://commons.apache.org/proper/commons-math/userguide/ml.html
package evochecker.seeding.encoding;

import org.apache.commons.math3.ml.clustering.Clusterable;

public class ParetoPointW {
	
	//wrapper class
	public static class ParetoPointWrapper implements Clusterable {
	    private double[] pointVals;
	    private ParetoPoint paretoPoint;
	    public double[] centroid;
	
	    public ParetoPointWrapper(ParetoPoint paretoPoint) {
	        this.paretoPoint = paretoPoint;
	        this.pointVals = paretoPoint.getAllVals();
	    }
	
	    public ParetoPoint getParetoPoint() {
	    	//TODO 
	    	//ParetoPoint has method getAllValsList (check not needed to be normalised)
	    	//2 normalise getPoint -- check this is not used to get Solution at the end of seeding
	    	return paretoPoint;
	    }
	
	    public double[] getPoint() {
	        // normalise point
	    	return pointVals;
	    }
	}
		
}
