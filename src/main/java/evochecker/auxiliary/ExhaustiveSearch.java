package evochecker.auxiliary;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import evochecker.genetic.jmetal.MultiProcessPrismEvaluator;
import evochecker.genetic.jmetal.encoding.ArrayInt;
import evochecker.genetic.jmetal.encoding.ArrayReal;
import evochecker.genetic.jmetal.encoding.ArrayRealIntSolutionType;
import evochecker.genetic.jmetal.metaheuristics.IParallelEvaluator;
import evochecker.genetic.jmetal.single.AlgorithmSteps;
import evochecker.genetic.jmetal.single.ObjectiveEvaluation;
import evochecker.parser.evolvable.Evolvable;
import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.core.SolutionType;
import jmetal.util.JMException;
import jmetal.util.comparators.ObjectiveComparator;

public class ExhaustiveSearch extends Algorithm implements AlgorithmSteps {

	/** Parallel Evaluator handle*/
	private IParallelEvaluator parallelEvaluator_ ;
	
	/** Algorithm population (now a global variable)*/
	private SolutionSet population;
	  
	/** Comparator*/
	Comparator comparator;
	
	/** Solution type*/
	SolutionType solutionType;
	  
	
	
	  
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExhaustiveSearch(String problemName, Problem problem, List<Evolvable> evolvableList) {
		super(problem);
	
		//Create algorithm and parallel objects
		parallelEvaluator_ = new MultiProcessPrismEvaluator(0);
		
	}

	
	@Override
	public void initialise() {
		  // Initialise single objective comparator
		  comparator = new ObjectiveComparator(problem_.getNumberOfObjectives()-1) ; 
		  
		  //Start the parallel evaluator
		  parallelEvaluator_.startEvaluator(problem_) ;
  }
	  
	
	@Override
	public void createInitialPopulation() throws ClassNotFoundException {
		Solution solution = new Solution(problem_);
		List<Object[]> intOptionsList 		= new ArrayList<Object[]>();
		List<Object[]> doubleOptionsList 	= new ArrayList<Object[]>();

		solutionType = solution.getType();
		
		ArrayReal arrayReal	= (ArrayReal)solution.getDecisionVariables()[0];
		ArrayInt  arrayInt 	= (ArrayInt)solution.getDecisionVariables()[1];
		
		try {
			int arrayIntSize 	= ((ArrayInt)solution.getDecisionVariables()[1]).getLength();
			for (int i=0; i<arrayIntSize; i++){
				int lowerBound 				= (int) ((ArrayInt)solution.getDecisionVariables()[1]).getLowerBound(i);
				int upperBound 				= (int) ((ArrayInt)solution.getDecisionVariables()[1]).getUpperBound(i);
				List<Integer> intOptions	= new ArrayList<Integer> ();
				for (int j=lowerBound; j<=upperBound; j++){
					intOptions.add(j);
				}
				intOptionsList.add(intOptions.toArray());
			}	
			
			int arrayRealSize 	= ((ArrayReal)solution.getDecisionVariables()[0]).getLength();
			for (int i=0; i<arrayRealSize; i++){
				double lowerBound 			= ((ArrayReal)solution.getDecisionVariables()[0]).getLowerBound(i);
				double upperBound 			= ((ArrayReal)solution.getDecisionVariables()[0]).getUpperBound(i);
				List<Double> doubleOptions	= new ArrayList<Double> ();
				for (double j=lowerBound; j<=upperBound; j+=0.1){//0.01){
					doubleOptions.add(j);
				}
				doubleOptionsList.add(doubleOptions.toArray());
			}
			
			createExhaustiveSolutionSet(intOptionsList, doubleOptionsList, arrayInt, arrayReal);
			
			System.out.println(population.size());
			
		} catch (JMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void createExhaustiveSolutionSet(List<Object[]> intOptionsList, List<Object[]> doubleOptionsList,
											ArrayInt arrayInt, ArrayReal arrayReal) throws JMException, ClassNotFoundException{
		
		//create all possible integer combinations
		int intMax1			= intOptionsList.get(0).length;
		int intMax2			= intOptionsList.get(1).length;
		int intMax3			= intOptionsList.get(2).length;
		int intMax4			= intOptionsList.get(3).length;
		int intArray[][] 	= new int[intMax1 * intMax2 * intMax3 * intMax4][intOptionsList.size()];
		
		int index = 0;
		for (int i=0; i<intMax1; i++){

			for (int j=0; j<intMax2; j++){
				
				for (int k=0; k<intMax3; k++){
					
					for (int l=0; l<intMax4; l++){
						intArray[index][0] = (int) intOptionsList.get(0)[i];  
						intArray[index][1] = (int) intOptionsList.get(1)[j];  
						intArray[index][2] = (int) intOptionsList.get(2)[k];  
						intArray[index][3] = (int) intOptionsList.get(3)[l];
						index++;
					}
				}
			}
		}
		
		//create all possible double combinations
		int doubleMax1			= doubleOptionsList.get(0).length;
		double doubleArray[][]	= new double[doubleMax1][doubleOptionsList.size()];
		
		index = 0;
		for (int i=0; i<doubleMax1; i++){
			doubleArray[index++][0] = (double) doubleOptionsList.get(0)[i];
		}
		
		population = new SolutionSet(intArray.length * doubleArray.length);
		
		
		//create ArrayRealIntSolutionType
		for (int intIndex=0; intIndex<intArray.length; intIndex++){
			ArrayInt arrayIntNew = (ArrayInt) arrayInt.deepCopy();
			for (int j=0; j<arrayIntNew.getLength(); j++){
				arrayIntNew.setValue(j, intArray[intIndex][j]);
			}
			
			for (int doubleIndex=0; doubleIndex<doubleArray.length; doubleIndex++){
				ArrayReal arrayRealNew = (ArrayReal)arrayReal.deepCopy();
				for (int j=0; j<arrayRealNew.getLength(); j++){
					arrayRealNew.setValue(j, doubleArray[doubleIndex][j]);
				}
			
				//create new soltuion
				Solution newSolution = new Solution(problem_, 
										((ArrayRealIntSolutionType)solutionType).createVariables(arrayRealNew, arrayIntNew));
			
				//add new solution to population
				population.add(newSolution);
			}
		}
		
		
	}

	
	@Override
	public SolutionSet execute() throws JMException, ClassNotFoundException {
		createInitialPopulation();
		
		SolutionSet solutionSet = new SolutionSet(population.size());
		
		for (int i=0; i<population.size(); i++){
			Solution solution = population.get(i);
			parallelEvaluator_.addSolutionForEvaluation(solution);
			
			if (i%1001 == 1000){
				List<Solution> solutionList = parallelEvaluator_.parallelEvaluation();
				
				  //Add solutionList to the population
				  for (Solution sol : solutionList) {
					  solutionSet.add(solution) ;
				  }
			}
		}
		
		
		for (int i=0; i<solutionSet.size(); i++){ 
			//Evaluate objectives
			ObjectiveEvaluation.evaluateObjectives(solutionSet, problem_.getNumberOfObjectives());
    
			//Sort population
			solutionSet.sort(comparator) ;
		}
		
		return null;
	}

	
	/**
	 * Do any necessary cleanup
	 */
	public void finalise(){
		//stop the evaluators
		parallelEvaluator_.stopEvaluators();
	}

	
	/**
	 * Log intermediate data
	 */ 
	private void logIntermediateData(int iterations){
//		Utility.exportToFile("data/FUN_SGA_"+ seeding +"_E"+ EvoChecker.adaptationStep +"_"+ iterations, 
//				  			   population.get(0).toString(), true);
//		Utility.printVariablesToFile("data/VAR_SGA_"+ seeding +"_E"+ EvoChecker.adaptationStep +"_"+ iterations,
//							   population.get(0), true);  
	}	
}
