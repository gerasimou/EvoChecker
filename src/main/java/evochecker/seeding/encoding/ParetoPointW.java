// See documentation for wrapper: https://commons.apache.org/proper/commons-math/userguide/ml.html
package evochecker.seeding.encoding;

import org.apache.commons.math3.ml.clustering.Clusterable;

public class ParetoPointW {
	
	//wrapper class
	public static class ParetoPointWrapper implements Clusterable {
	    private double[] pointVals;
	    private ParetoPoint paretoPoint;
	    public double[] centroid;
		private PreviousPareto prevPareto;
	
	    public ParetoPointWrapper(ParetoPoint paretoPoint, PreviousPareto prevPareto) {
	        this.paretoPoint = paretoPoint;
	        this.pointVals = paretoPoint.getAllVals();
	        this.prevPareto = prevPareto;
	    }
	
	    public ParetoPoint getParetoPoint() {
	    	//TODO 
	    	//ParetoPoint has method getAllValsList (check not needed to be normalised)
	    	//2 normalise getPoint -- check this is not used to get Solution at the end of seeding
	    	return paretoPoint;
	    }
	
	    public double[] getPoint() {
	    	// normalise point
	    	return getNormalisePoint();
	    	// point 
	    	//return pointVals;
	    }
	    
	    
	    //normalise point
	    private double[] getNormalisePoint() {
	    	double[] normPoint = new double[this.pointVals.length];
	    	for (int i=0; i<this.pointVals.length; i++) {
	    		
	    		double point = this.pointVals[i];
	    		double min = this.prevPareto.getMinVals(i);
	    		double max = this.prevPareto.getMaxVals(i);
	    		
	    		try {
	    			if (max-min == 0) {
	    				normPoint[i] = point; // no normalisation
	    			}
	    			normPoint[i] = (point-min)/(max-min);
	    		} catch (Exception e) {
	    			System.out.println("[ParetoPointW] Warning in normalisation. Not normalising point: "+ normPoint.toString() + " Error message:" + e);
	    			normPoint[i] = point; // no normalisation
	    		}
	    	}
	    	return normPoint;
	    }
	}
		
}
