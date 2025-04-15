package evochecker.seeding;

import org.apache.commons.math3.ml.clustering.Clusterable;

import evochecker.seeding.auxiliary.ParetoPoint;

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
	        return paretoPoint;
	    }
	
	    public double[] getPoint() {
	        return pointVals;
	    }
	}
		
}
