package evochecker.seeding.auxiliary;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import evochecker.genetic.jmetal.encoding.ArrayInt;
import evochecker.genetic.jmetal.encoding.ArrayReal;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.util.JMException;


/**
 * EvoChecker class
 * @author gricelvazquez
 * April 2025
 */
public class ParetoPoint {
    private List<Double> frontVals; //pareto front
    private List<Double> setValsD; //pareto set doubles
    private List<Integer> setValsI; //pareto set integers
    private Solution solution;
    
    public ParetoPoint(List<Double> setValuesD, 
    		List<Integer> setValuesI, 
    		List<Double> frontValues,
    		Problem _problem) throws JMException, ClassNotFoundException {
        this.setValsD = setValuesD;
        this.setValsI = setValuesI;
        this.frontVals = frontValues;
        this.solution = this.setSolution(_problem);
    }
    
    public Solution getSolution() {
		return this.solution;
	}
    
    /**
	 * Get Pareto front and set values combined as List
	 */
    public List<Double> getAllValsList() {
    	// joint set and front values
    	List<Double> allValues = new ArrayList<>(); 
    	allValues.addAll(setValsI.stream().map(Integer::doubleValue).collect(Collectors.toList()));
    	allValues.addAll(setValsD);
    	allValues.addAll(frontVals);
    	return  allValues;
    }
    
    /**
    * Get Pareto front and set values combined as double[]
    */
    public double[] getAllVals() {
    	//Convert List<Double> to double[]
    	return getAllValsList().stream()
                      .mapToDouble(Double::doubleValue)  // Converts each Double to a primitive double
                      .toArray();
    }
    
    /**
     * Create a JMetal solution object from point values
     * @param _problem 
     * @throws ClassNotFoundException 
     */
    private Solution setSolution(Problem _problem) throws JMException, ClassNotFoundException {
    	Solution newSolution = new Solution(_problem);
    	// get data (some decision variables are reals, others integers)
 	    ArrayReal real_arr=(ArrayReal) newSolution.getDecisionVariables()[0];
        ArrayInt  int_arr=(ArrayInt) newSolution.getDecisionVariables()[1];
        int count_int=0;
        int count_real=0;
        
    	for (int val:this.setValsI) {
    		int_arr.setValue(count_int, val);
    		count_int+=1;
    	}
    	for (double val:this.setValsD) {
			real_arr.setValue(count_real, val);
			count_real+=1;
		}
		return newSolution;
	}
    
    public void printSolution() {
    	System.out.println("Solution:");
    	System.out.println(this.solution.getDecisionVariables()[0]);
    	System.out.println(this.solution.getDecisionVariables()[1]);
    	
    }
}
