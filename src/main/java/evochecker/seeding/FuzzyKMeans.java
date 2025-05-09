package evochecker.seeding;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.FuzzyKMeansClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

import evochecker.auxiliary.Constants;
import evochecker.auxiliary.Utility;
import evochecker.seeding.encoding.ParetoPoint;
import evochecker.seeding.encoding.PreviousPareto;
import evochecker.seeding.encoding.ParetoPointW.ParetoPointWrapper;



/**
 * EvoChecker class
 * @author gricelvazquez
 * April 2025
 */
public class FuzzyKMeans implements ISeeding {
	
	private Integer numIterations = 10000; //default value
	private DistanceMeasure distanceMeasure = new EuclideanDistance(); // - distance measure to be used
	private double fuzziness = 1.05; // - fuzziness parameter (> 1)
	
	/**
	 * This method returns the first N solutions from previous solutions using Kmeans clustering.
	 * @param prevSolutions
	 * @param seedingNumSolutions
	 * @return
	 */
	public List<ParetoPoint> getNSolutions(PreviousPareto prevPareto, Integer seedingNumSolutions) {
	    
		// get previous solutions
		List<ParetoPoint> prevSolutions = prevPareto.getPrevSolutions();
		
		List<ParetoPointWrapper> clusterInput = new ArrayList<ParetoPointWrapper>(prevSolutions.size());
		for (ParetoPoint pp : prevSolutions)
		    clusterInput.add(new ParetoPointWrapper(pp));
		
		// initialize a new clustering algorithm.
		int numClusters = seedingNumSolutions;
		FuzzyKMeansClusterer<ParetoPointWrapper> clusterer = new FuzzyKMeansClusterer<ParetoPointWrapper>(numClusters, this.fuzziness, this.numIterations, this.distanceMeasure);
		List<CentroidCluster<ParetoPointWrapper>> clusterResults = clusterer.cluster(clusterInput);
		
		//System.out.println("Number of clusters: " + clusterResults.size());
		
		// sampling closest point to centroid: get K points, one for each cluster, each closest to centroid
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




	@Override
	public void setParameters() {
		// - distance measure
		this.distanceMeasure = new EuclideanDistance(); // - distance measure to be used
		
		// - fuzziness parameter (> 1)
		try {
			this.fuzziness = Double.parseDouble(Utility.getProperty(Constants.SEED_FUZZINESS));
			if (this.fuzziness <= 1) {
				System.out.println("[SeedingFuzzyKM] Fuzziness parameter must be greater than 1.");
				System.exit(0);
			}else {
				System.out.println("[SeedingFuzzyKM] Fuzziness parameter: " + this.fuzziness);
			}
			
		}// except default value
		catch (Exception e) {
			System.out.println("[SeedingFuzzyKM] No KMEANS_FUZZINESS found. Using default value: " + this.fuzziness);
		}
		
		// - number of kmean iterations
		try {
			this.numIterations = Integer.parseInt(Utility.getProperty(Constants.SEED_KMEANS_ITERATIONS));
		}// except default value
		catch (Exception e) {
			System.out.println("[SeedingFuzzyKM] No KMEANS_ITERATIONS found. Using default value: " + this.numIterations);
		}
		return;
	}

	

}
