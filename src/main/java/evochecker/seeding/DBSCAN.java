package evochecker.seeding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

import evochecker.seeding.ParetoPointW.ParetoPointWrapper;
import evochecker.seeding.auxiliary.ParetoPoint;



/**
 * EvoChecker class
 * @author gricelvazquez
 * April 2025
 */
public class DBSCAN implements ISeeding {
	
	double eps = 0.5; // - maximum radius of the neighborhood to be considered
    int minPts=2; // - minimum number of points needed for a cluster
	private DistanceMeasure distanceMeasure = new EuclideanDistance(); // - distance measure to be used
    
	
	/**
	 * This method returns the first N solutions from previous solutions using Kmeans clustering.
	 * @param prevSolutions
	 * @param seedingNumSolutions
	 * @return
	 */
	public List<ParetoPoint> getNSolutions(List<ParetoPoint> prevSolutions, Integer seedingNumSolutions) {
	    
		List<ParetoPointWrapper> clusterInput = new ArrayList<ParetoPointWrapper>(prevSolutions.size());
		for (ParetoPoint pp : prevSolutions)
		    clusterInput.add(new ParetoPointWrapper(pp));
		
		// initialize a new clustering algorithm.
		DBSCANClusterer<ParetoPointWrapper> clusterer = new DBSCANClusterer<ParetoPointWrapper>(this.eps, this.minPts, this.distanceMeasure);
		List<Cluster<ParetoPointWrapper>> clusterResults = clusterer.cluster(clusterInput);
		
		// sampling closest point to centroid: get K points, one for each cluster, each closest to centroid
		List<ParetoPoint> closestPoints = new ArrayList<ParetoPoint>();
		
		// for each cluster
		for (int i=0; i<clusterResults.size(); i++) {
			ParetoPointWrapper closest = null;
	    	
	    	//--Print -- checkpoint
//	    	System.out.println("Cluster " + i);
//	    	System.out.println("Centroid: " + centroid.getPoint()[0] + ", " + centroid.getPoint()[1]);
//		    System.out.println("Num points in cluster: " + clusterResults.get(i).getPoints().size());
	    	//
	    	
		   
		    if (clusterResults.get(i).getPoints().size()>0) { //some clusters might be empty, e.g., when points are repeated or too close
		    	Double minDistance = Double.MAX_VALUE;
		    	// get centroid
				double[] centroid = getClusterCentroid(clusterResults.get(i));
				// for each point in cluster
			    for (ParetoPointWrapper locationWrapper : clusterResults.get(i).getPoints()) {
			    	// get distance to centroid
			    	DistanceMeasure dm = new EuclideanDistance();
			    	double distance = dm.compute(locationWrapper.getPoint(), centroid);
			    	if (distance < minDistance) {
			    		minDistance = distance;
			    		closest = locationWrapper;
			    	}
			    }
			    closestPoints.add(closest.getParetoPoint());
		    }
		}
		
//		//--Print -- checkpoint
//		System.out.println("Number of clusters: " + clusterResults.size());
				
		// if more points than needed, select the first N
		closestPoints = closestPoints.subList(0, Math.min(seedingNumSolutions, closestPoints.size()));
		
		
//		//--Print -- checkpoint
//		System.out.println("Number of clusters: " + clusterResults.size());
		
		return closestPoints;
	}
	
	
	/**
	 * This method computes the centroid of a cluster.
	 * @param clusterPoints
	 * @return centroidCoords: centroid coordinates
	 */
	public double[] getClusterCentroid(Cluster<ParetoPointWrapper> clusterPoints) {
		int numPoints = clusterPoints.getPoints().size();

		// Skip empty clusters
	    if (numPoints == 0) 
	    	return null;
	    
	    // Initialize variables to hold the sum of coordinates
	    double[] sumCoordinates = new double[clusterPoints.getPoints().get(0).getPoint().length];
	
	    // Sum the coordinates of all points in the cluster
	    for (ParetoPointWrapper point : clusterPoints.getPoints()) {
	        double[] coords = point.getPoint(); // Assuming getCoordinates returns a coordinate array
	        for (int i = 0; i < coords.length; i++) {
	            sumCoordinates[i] += coords[i];
	        }}
	
	    // Compute the mean (centroid)
	    double[] centroidCoords = new double[sumCoordinates.length];
	    for (int i = 0; i < sumCoordinates.length; i++) {
	        centroidCoords[i] = sumCoordinates[i] / numPoints;
	    }
	return centroidCoords;
	}
	
	

	@Override
	public void setParameters() {
		//this.eps;     //set in getNSolutions
		//this.minPts;  //set in getNSolutions
		return;
	}

	

}
