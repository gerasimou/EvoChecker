package evochecker.seeding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import jmetal.core.Solution;
import jmetal.util.JMException;


//Explanation:
//    Extract decision variables: In the first step, we extract the decision variables from the Solution objects and store them as points in a list.
//    K-Means clustering: The kMeansClustering function implements the K-Means algorithm. It assigns points to the closest centroid, then recalculates centroids until convergence.
//    Euclidean distance: This is used to calculate the distance between points (solutions) and centroids.
//    Select closest solution: After obtaining the centroids, we find the solution closest to each centroid and use it for seeding.
//Additional Notes:
//    Improvement options: It could enhance the initialization of centroids to use smarter methods (e.g., K-Means++), and optimise the K-Means logic.
//    Distance calculation: The Euclidean distance works for real-valued decision variables, but if your problem involves binary or integer variables, you may need to adjust the distance metric accordingly.
    
/**
 * EvoChecker class
 * @author gricelvazquez
 * April 2025
 */
public class KMeans {
	
	
	/**
	 * This method returns the first N solutions from previous solutions using Kmeans clustering.
	 * @param prevSolutions
	 * @param seedingNumSolutions
	 * @return
	 */
	static List<Solution> getNSolutions(List<Solution> prevSolutions, Integer seedingNumSolutions) {
	    // Step 1: Extract the decision variables from the solutions and convert them into a list of points.
	    List<List<Double>> points = new ArrayList<>();
	    for (Solution solution : prevSolutions) {
	        List<Double> point = new ArrayList<>();
	        // Assuming decision variables are stored as a list of Doubles
	        for (int i = 0; i < solution.numberOfVariables(); i++) {
	            try {
	            	System.out.println("Hu" + solution.getDecisionVariables()[i]);
					point.add(solution.getDecisionVariables()[i].getValue());
				} catch (JMException e) {
					e.printStackTrace();
					System.err.println("Error in getting Kmeans N solutions");
					System.exit(1);
				}
	        }
	        System.out.println("point: " + point);
	        points.add(point);
	    }
//	    System.out.println("Hi");

	    // Step 2: Perform K-Means clustering on the list of points.
	    List<List<Double>> centroids = kMeansClustering(points, seedingNumSolutions);

	    // Step 3: For each centroid, find the closest solution in `prevSolutions` and return that as the seed.
	    List<Solution> seededSolutions = new ArrayList<>();
	    for (List<Double> centroid : centroids) {
	        Solution closestSolution = findClosestSolution(prevSolutions, centroid);
	        seededSolutions.add(closestSolution);
	    }

	    return seededSolutions;
	}
	

	private static List<List<Double>> kMeansClustering(List<List<Double>> points, int k) {
	    // Initialize centroids (this is a simple implementation, you could improve this part)
	    List<List<Double>> centroids = new ArrayList<>();
	    
	    // Step 1: Randomly initialize centroids
	    Random rand = new Random();
	    for (int i = 0; i < k; i++) {
	        List<Double> randomCentroid = new ArrayList<>(points.get(rand.nextInt(points.size())));
	        centroids.add(randomCentroid);
	    }

	    boolean centroidsChanged = true;
	    List<Integer> assignments = new ArrayList<>(Collections.nCopies(points.size(), -1));

	    while (centroidsChanged) {
	        centroidsChanged = false;

	        // Step 2: Assign each point to the closest centroid
	        for (int i = 0; i < points.size(); i++) {
	            double minDistance = Double.MAX_VALUE;
	            int closestCentroid = -1;

	            for (int j = 0; j < centroids.size(); j++) {
	                double distance = euclideanDistance(points.get(i), centroids.get(j));
	                if (distance < minDistance) {
	                    minDistance = distance;
	                    closestCentroid = j;
	                }
	            }

	            if (assignments.get(i) != closestCentroid) {
	                assignments.set(i, closestCentroid);
	                centroidsChanged = true;
	            }
	        }

	        // Step 3: Update centroids
	        if (centroidsChanged) {
	            for (int i = 0; i < k; i++) {
	                List<Double> newCentroid = new ArrayList<>(Collections.nCopies(points.get(0).size(), 0.0));
	                int count = 0;
	                for (int j = 0; j < points.size(); j++) {
	                    if (assignments.get(j) == i) {
	                        for (int d = 0; d < points.get(j).size(); d++) {
	                            newCentroid.set(d, newCentroid.get(d) + points.get(j).get(d));
	                        }
	                        count++;
	                    }
	                }
	                if (count > 0) {
	                    for (int d = 0; d < newCentroid.size(); d++) {
	                        newCentroid.set(d, newCentroid.get(d) / count);
	                    }
	                }
	                centroids.set(i, newCentroid);
	            }
	        }
	    }
	    return centroids;
	}

	private static double euclideanDistance(List<Double> point1, List<Double> point2) {
	    double sum = 0.0;
	    for (int i = 0; i < point1.size(); i++) {
	        sum += Math.pow(point1.get(i) - point2.get(i), 2);
	    }
	    return Math.sqrt(sum);
	}

	private static Solution findClosestSolution(List<Solution> prevSolutions, List<Double> centroid) {
	    Solution closestSolution = null;
	    double minDistance = Double.MAX_VALUE;

	    for (Solution solution : prevSolutions) {
	        double distance = 0.0;
	        for (int i = 0; i < solution.numberOfVariables(); i++) {
	            try {
					distance += Math.pow(solution.getDecisionVariables()[i].getValue() - centroid.get(i), 2);
				} catch (JMException e) {
					e.printStackTrace();
					System.err.println("Error in finding closest solution");
					System.exit(1);
				}
	        }
	        distance = Math.sqrt(distance);

	        if (distance < minDistance) {
	            minDistance = distance;
	            closestSolution = solution;
	        }
	    }

	    return closestSolution;
	}

}
