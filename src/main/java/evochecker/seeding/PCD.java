package evochecker.seeding;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jmetal.core.Solution;

public class PCD {
	// Pareto Crowding Distance (PCD) Seeding
	// Steps:
	//    Pareto Front Calculation:
	//        calculateParetoFront computes the Pareto front, which is a set of non-dominated solutions. A solution solution1 dominates another solution solution2 if it is no worse in all objectives and better in at least one.
	//    Crowding Distance:
	//        calculateCrowdingDistances computes the crowding distance for each solution in the Pareto front. This distance measures how close a solution is to others in the population. Solutions with higher crowding distances are preferred, as they represent more diverse regions in the objective space.
	//    Diversity Preservation:
	//        In preserveDiversity, we first calculate the crowding distances and then select the seedingNumSolutions solutions with the highest crowding distances. This ensures that the selected solutions are as diverse as possible.
	//Additional Notes:
	//    Crowding Distance: This is one of the most common metrics used to maintain diversity in evolutionary algorithms. It prevents the algorithm from converging prematurely to a single solution and encourages exploration of diverse regions of the objective space.
	//    Boundary Solutions: Boundary solutions (i.e., the first and last in the sorted order of objectives) are given an infinite crowding distance, as they are on the "edge" of the objective space.
	//Potential Improvements:
	//    More Sophisticated Diversity Measures: Instead of using crowding distance, one could use other diversity measures like distribution measures or cluster-based diversity.
	//    Efficiency Considerations: Depending on the size of prevSolutions, the dominance and crowding distance calculations might be inefficient. You could look into optimising these operations if you're dealing with a very large population.
    
	/**
	 * This method returns the first N solutions from previous solutions using PCD.
	 * @param prevSolutions
	 * @param seedingNumSolutions
	 * @return
	 */
	static List<Solution> getNSolutions(List<Solution> prevSolutions, Integer seedingNumSolutions) {
	    // Step 1: Calculate the Pareto front (although assumed to be Pareto optimal already)
	    List<Solution> paretoFront = calculateParetoFront(prevSolutions);

	    // Step 2: Calculate the crowding distance or use other diversity-preserving metrics
	    List<Solution> diverseSolutions = preserveDiversity(paretoFront, seedingNumSolutions);

	    // Step 3: Return the most diverse solutions
	    return diverseSolutions;
	}

	private static List<Solution> calculateParetoFront(List<Solution> prevSolutions) {
	    List<Solution> paretoFront = new ArrayList<>();
	    
	    // For each solution, check if it is dominated by any other solution
	    for (Solution solution1 : prevSolutions) {
	        boolean isDominated = false;
	        
	        for (Solution solution2 : prevSolutions) {
	            if (dominates(solution2, solution1)) {
	                isDominated = true;
	                break;
	            }
	        }
	        
	        // If not dominated, add to the Pareto front
	        if (!isDominated) {
	            paretoFront.add(solution1);
	        }
	    }
	    
	    return paretoFront;
	}

	private static boolean dominates(Solution solution1, Solution solution2) {
	    // Check if solution1 dominates solution2 (in the sense of Pareto dominance)
	    boolean betterInAtLeastOneObjective = false;
	    // TODO: check if requires parallelEvaluator_.addSolutionForEvaluation(newSolution);
	    // TODO: check if requires List<Solution> solutionList = parallelEvaluator_.parallelEvaluation() ;
	    for (int i = 0; i < solution1.getNumberOfObjectives(); i++) {
	        if (solution1.getObjective(i) < solution2.getObjective(i)) {
	            betterInAtLeastOneObjective = true;
	        } else if (solution1.getObjective(i) > solution2.getObjective(i)) {
	            return false;
	        }
	    }
	    return betterInAtLeastOneObjective;
	}

	private static List<Solution> preserveDiversity(List<Solution> paretoFront, int seedingNumSolutions) {
	    // Step 2: Calculate diversity (e.g., crowding distance)
	    List<Solution> diverseSolutions = new ArrayList<>();

	    // Calculate crowding distances for each solution in the Pareto front
	    List<Double> crowdingDistances = calculateCrowdingDistances(paretoFront);

	    // Sort solutions by their crowding distances (higher distance = more diverse)
	    List<Integer> indices = new ArrayList<>();
	    for (int i = 0; i < crowdingDistances.size(); i++) {
	        indices.add(i);
	    }
	    indices.sort((i1, i2) -> Double.compare(crowdingDistances.get(i2), crowdingDistances.get(i1))); // Sort in descending order

	    // Select the top `seedingNumSolutions` based on crowding distance
	    for (int i = 0; i < seedingNumSolutions && i < indices.size(); i++) {
	        diverseSolutions.add(paretoFront.get(indices.get(i)));
	    }

	    return diverseSolutions;
	}

	private static List<Double> calculateCrowdingDistances(List<Solution> paretoFront) {
	    List<Double> crowdingDistances = new ArrayList<>(Collections.nCopies(paretoFront.size(), 0.0));

	    // Calculate the crowding distance for each solution (using each objective)
	    int numObjectives = paretoFront.get(0).getNumberOfObjectives();
	    for (int i = 0; i < numObjectives; i++) {
	        final int objectiveIndex = i;

	        // Sort solutions by the i-th objective
	        paretoFront.sort((sol1, sol2) -> Double.compare(sol1.getObjective(objectiveIndex), sol2.getObjective(objectiveIndex)));

	        // Set the crowding distance of boundary solutions to infinity
	        crowdingDistances.set(0, Double.POSITIVE_INFINITY);
	        crowdingDistances.set(paretoFront.size() - 1, Double.POSITIVE_INFINITY);

	        // Calculate crowding distance for the rest of the solutions
	        for (int j = 1; j < paretoFront.size() - 1; j++) {
	            double distance = (paretoFront.get(j + 1).getObjective(objectiveIndex) - paretoFront.get(j - 1).getObjective(objectiveIndex))
	                    / (paretoFront.get(paretoFront.size() - 1).getObjective(objectiveIndex) - paretoFront.get(0).getObjective(objectiveIndex));
	            crowdingDistances.set(j, crowdingDistances.get(j) + distance);
	        }
	    }

	    return crowdingDistances;
	}

	
	
	
}
