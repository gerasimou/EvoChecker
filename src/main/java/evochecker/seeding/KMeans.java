package evochecker.seeding;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

import evochecker.seeding.auxiliary.ParetoPoint;



/**
 * EvoChecker class
 * @author gricelvazquez
 * April 2025
 */
public class KMeans {
	
	//wrapper class
	public static class ParetoPointWrapper implements Clusterable {
	    private double[] pointVals;
	    private ParetoPoint paretoPoint;
	
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
	
	
	
	
	/**
	 * This method returns the first N solutions from previous solutions using Kmeans clustering.
	 * @param prevSolutions
	 * @param seedingNumSolutions
	 * @return
	 */
	static List<ParetoPoint> getNSolutions(List<ParetoPoint> prevSolutions, Integer seedingNumSolutions, int kmeansIterations) {
	    
		// add locations
		List<ParetoPointWrapper> clusterInput = new ArrayList<ParetoPointWrapper>(prevSolutions.size());
		for (ParetoPoint pp : prevSolutions)
		    clusterInput.add(new ParetoPointWrapper(pp));
		
		// initialize a new clustering algorithm.
		// we did not specify a distance measure; the default (euclidean distance) is used.
		int numClusters = seedingNumSolutions;
		int numIterations = kmeansIterations;
		KMeansPlusPlusClusterer<ParetoPointWrapper> clusterer = new KMeansPlusPlusClusterer<ParetoPointWrapper>(numClusters, numIterations);
		List<CentroidCluster<ParetoPointWrapper>> clusterResults = clusterer.cluster(clusterInput);
		
		System.out.println("Number of clusters: " + clusterResults.size());
		
		// sampling: get K points, one for each cluster, each closest to centroid
		List<ParetoPoint> closestPoints = new ArrayList<ParetoPoint>();
		
		// for each cluster
		for (int i=0; i<clusterResults.size(); i++) {
		    // get centroid
	    	Clusterable centroid = clusterResults.get(i).getCenter();
	    	ParetoPointWrapper closest = null;
	    	
	    	//--Print -- checkpoint
//	    	System.out.println("Cluster " + i);
//	    	System.out.println("Centroid: " + centroid.getPoint()[0] + ", " + centroid.getPoint()[1]);
//		    System.out.println("Num points in cluster: " + clusterResults.get(i).getPoints().size());
	    	//
	    	
		   
		    if (clusterResults.get(i).getPoints().size()>0) { //some clusters might be empty, e.g., when points are repeated or too close
		    	Double minDistance = Double.MAX_VALUE;
		    	// for each point in cluster
			    for (ParetoPointWrapper locationWrapper : clusterResults.get(i).getPoints()) {
			    	// get distance to centroid
			    	DistanceMeasure dm = new EuclideanDistance();
			    	double distance = dm.compute(locationWrapper.getPoint(), centroid.getPoint());
			    	
			    	if (distance < minDistance) {
			    		minDistance = distance;
			    		closest = locationWrapper;
			    	}
			    }
			    closestPoints.add(closest.getParetoPoint());
			}
		}
		return closestPoints;
	}

}
