package evochecker.seeding;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

import evochecker.auxiliary.Constants;
import evochecker.auxiliary.Utility;
import evochecker.seeding.auxiliary.ParetoPoint;



/**
 * EvoChecker class
 * @author gricelvazquez
 * April 2025
 */
public class DBSCAN implements ISeeding {
	
	double eps = 0.5; // - maximum radius of the neighborhood to be considered
    int minPts=10; // - minimum number of points needed for a cluster
	private DistanceMeasure distanceMeasure = new EuclideanDistance(); // - distance measure to be used
    
	
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
	public List<ParetoPoint> getNSolutions(List<ParetoPoint> prevSolutions, Integer seedingNumSolutions) {
	    
		
		//--- Set parameters
		this.eps = this.setEps(prevSolutions);
		
		//minPts defined as a function of seedingNumSolutions
		this.minPts = prevSolutions.size() / seedingNumSolutions;
		if (this.minPts < 1) {
			return new ArrayList<ParetoPoint>(prevSolutions);
		}
		
		// add locations
		List<ParetoPointWrapper> clusterInput = new ArrayList<ParetoPointWrapper>(prevSolutions.size());
		for (ParetoPoint pp : prevSolutions)
		    clusterInput.add(new ParetoPointWrapper(pp));
		
		// initialize a new clustering algorithm.
		// we did not specify a distance measure; the default (euclidean distance) is used.
		int numClusters = seedingNumSolutions;
		DBSCANClusterer<ParetoPointWrapper> clusterer = new DBSCANClusterer<ParetoPointWrapper>(this.eps, this.minPts, this.distanceMeasure);
//		KMeansPlusPlusClusterer<ParetoPointWrapper> clusterer = new KMeansPlusPlusClusterer<ParetoPointWrapper>(numClusters, this.numIterations);
		List<Cluster<ParetoPointWrapper>> clusterResults = clusterer.cluster(clusterInput);
		
//		System.out.println("Number of clusters: " + clusterResults.size());
		
		// sampling: get K points, one for each cluster, each closest to centroid
		List<ParetoPoint> closestPoints = new ArrayList<ParetoPoint>();
		
		// for each cluster
		for (int i=0; i<clusterResults.size(); i++) {
			
			//TODO
		    // get centroid of the cluster
	    	//Clusterable centroid = clusterResults.get(i);
	    	//ParetoPointWrapper closest = null;
	    	
	    	//--Print -- checkpoint
//	    	System.out.println("Cluster " + i);
//	    	System.out.println("Centroid: " + centroid.getPoint()[0] + ", " + centroid.getPoint()[1]);
//		    System.out.println("Num points in cluster: " + clusterResults.get(i).getPoints().size());
	    	//
	    	
		   
		    if (clusterResults.get(i).getPoints().size()>0) { //some clusters might be empty, e.g., when points are repeated or too close
		    	Double minDistance = Double.MAX_VALUE;
		    	// get one point from the cluster
			    closestPoints.add(clusterResults.get(i).getPoints().get(0).getParetoPoint());
			    //TODO: replace line above with get the closest point to the centroid
			}
		}
		return closestPoints;
	}
	

	public double setEps(List<ParetoPoint> prevSolutions) {
		//---TODO: set eps as a function of the previous solutions
		double newEps = this.eps;
		//---
		return newEps;
	}



	@Override
	public void setParameters() {
		//this.eps; //set in getNSolutions
		//this.minPts; //set in getNSolutions
		return;
	}

	

}
