//  pNSGAII.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//
//  Copyright (c) 2013 Antonio J. Nebro
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package evochecker.genetic.jmetal.metaheuristics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import evochecker.EvoChecker;
import evochecker.EvoCheckerType;
import evochecker.auxiliary.Constants;
import evochecker.auxiliary.FileUtil;
import evochecker.auxiliary.Utility;
import evochecker.evaluator.IParallelEvaluator;
import evochecker.exception.EvoCheckerException;
import evochecker.genetic.GenotypeFactory;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.jmetal.metaheuristics.settings.MOCell_Settings;
import evochecker.genetic.jmetal.metaheuristics.settings.NSGAII_Settings;
import evochecker.genetic.jmetal.metaheuristics.settings.RandomSearch_Settings;
import evochecker.genetic.jmetal.metaheuristics.settings.SPEA2_Settings;
import evochecker.language.parser.ModelInstantiator;
import evochecker.language.parser.IModelInstantiator;
import evochecker.language.parser.ModelInstantiatorParametric;
import evochecker.plotting.PlotFactory;
import evochecker.properties.Property;
import evochecker.properties.PropertyFactory;
import evochecker.seeding.Seeding;
import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.Ranking;
import jmetal.util.comparators.CrowdingComparator;

/** 
 *  Implementation of NSGA-II.
 *  This implementation of NSGA-II makes use of a QualityIndicator object
 *  to obtained the convergence speed of the algorithm. This version is used
 *  in the paper:
 *     A.J. Nebro, J.J. Durillo, C.A. Coello Coello, F. Luna, E. Alba 
 *     "A Study of Convergence Speed in Multi-Objective Metaheuristics." 
 *     To be presented in: PPSN'08. Dortmund. September 2008.
 */

public class pNSGAII extends Algorithm {

  IParallelEvaluator parallelEvaluator_ ; 

  /**
   * Constructor
   * @param problem Problem to solve
   * @param evaluator Parallel evaluator
   */
  public pNSGAII(Problem problem, IParallelEvaluator evaluator) {
    super (problem) ;

    parallelEvaluator_ = evaluator ;
  } // pNSGAII
  
  /**   
   * Runs the NSGA-II algorithm.
   * @return a <code>SolutionSet</code> that is a set of non dominated solutions
   * as a result of the algorithm execution
   * @throws JMException 
   */
  public SolutionSet execute() throws JMException, ClassNotFoundException {
    int populationSize;
    int maxEvaluations;
    int evaluations;
    int numberOfThreads;
    int nParetoSaved = 0;
    
    QualityIndicator indicators; // QualityIndicator object
    int requiredEvaluations; // Use in the example of use of the
    // indicators object (see below)

    SolutionSet population;
    SolutionSet offspringPopulation;
    SolutionSet union;

    Operator mutationOperator;
    Operator crossoverOperator;
    Operator selectionOperator;

    Distance distance = new Distance();

    //Read the parameters
    populationSize = ((Integer) getInputParameter("populationSize")).intValue();
    maxEvaluations = ((Integer) getInputParameter("maxEvaluations")).intValue();
    indicators = (QualityIndicator) getInputParameter("indicators");

    parallelEvaluator_.startEvaluator(problem_) ;

    //Initialize the variables
    population = new SolutionSet(populationSize);
    evaluations = 0;
    
    requiredEvaluations = 0;

    //Read the operators
    mutationOperator = operators_.get("mutation");
    crossoverOperator = operators_.get("crossover");
    selectionOperator = operators_.get("selection");
        
    
    // Create the initial solutionSet
    Solution newSolution;
    
    // Check if seeding - get solutions to seed
    List<Solution> solutions2Seed = Seeding.getSeededSolutions(problem_, populationSize);
    
    // add solutions to population	
    for (int i = 0; i < populationSize; i++) {
    	if (solutions2Seed.size() > 0) { //seeded solution
    		newSolution = solutions2Seed.remove(0);
		} else { //random solution
			newSolution = new Solution(problem_);
		}
    	// add sol to evaluate in parallel
		parallelEvaluator_.addSolutionForEvaluation(newSolution);
	} // for
    
    // Evaluate initial population using parallel evaluation
    List<Solution> solutionList = parallelEvaluator_.parallelEvaluation() ;
    for (Solution solution : solutionList) {
		population.add(solution);
		evaluations++;
	}
    
    
    //===============================
    // Save Pareto front/set every N evaluations (if defined SAVE_PARETO_EVERY_N_ITERATIONS)
	  try {
		  nParetoSaved ++;
		  Ranking ranking2Save = new Ranking(population); //save only non-dominated
		  exportResults(nParetoSaved, ranking2Save.getSubfront(0), maxEvaluations);
	  } catch (JMException | EvoCheckerException e) {e.printStackTrace();}
	//===============================
    
    
    // Generations 
    while (evaluations < maxEvaluations) {
    	System.out.println("Evaluations:\t" + evaluations);
	    
    	// Create the offSpring solutionSet      
	    offspringPopulation = new SolutionSet(populationSize);
	    Solution[] parents = new Solution[2];
	    for (int i = 0; i < (populationSize / 2); i++) {
	    	if (evaluations < maxEvaluations) {
		        //obtain parents
		        parents[0] = (Solution) selectionOperator.execute(population);
		        parents[1] = (Solution) selectionOperator.execute(population);
		        Solution[] offSpring = (Solution[]) crossoverOperator.execute(parents);
		        mutationOperator.execute(offSpring[0]);
		        mutationOperator.execute(offSpring[1]);
		        // parallel execution
		        parallelEvaluator_.addSolutionForEvaluation(offSpring[0]) ;
		        parallelEvaluator_.addSolutionForEvaluation(offSpring[1]) ;
	        } // if                
	    } // for
	    
	    List<Solution> solutions = parallelEvaluator_.parallelEvaluation() ;
	    
	    for(Solution solution : solutions) {
	    	offspringPopulation.add(solution);
	        evaluations++;	    
	    }
	
	    // Create the solutionSet union of solutionSet and offSpring
	    union = ((SolutionSet) population).union(offspringPopulation);
	
	    // Ranking the union
	    Ranking ranking = new Ranking(union);

	    int remain = populationSize;
	    int index = 0;
	    SolutionSet front = null;
	    population.clear();

	    // Obtain the next front
	    front = ranking.getSubfront(index);
      
	    while ((remain > 0) && (remain >= front.size())) {
	        //Assign crowding distance to individuals
	        distance.crowdingDistanceAssignment(front, problem_.getNumberOfObjectives());
	        //Add the individuals of this front
	        for (int k = 0; k < front.size(); k++) {
	        	population.add(front.get(k));
	        } // for
        
	        //Decrement remain
		    remain = remain - front.size();
		
		    //Obtain the next front
		    index++;
		    if (remain > 0) {
		    	front = ranking.getSubfront(index);
	        } // if
	    } // while
	
	    // Remain is less than front(index).size, insert only the best one
	    if (remain > 0) {  // front contains individuals to insert                        
	    	distance.crowdingDistanceAssignment(front, problem_.getNumberOfObjectives());
	        front.sort(new CrowdingComparator());
	        for (int k = 0; k < remain; k++) {
	        	population.add(front.get(k));
	        } // for
	        remain = 0;
	    } // if                               
	      
	      
	      //TODO
	      // get HV
	      // get HV of the previous Pareto sol
	      // compare, if between 1 or 2% stop
	      // HV_prev = 
	      // HV_current =
	      // if (HV_current >= (1 - 0.02) * HV_prev) { ...
	      
	      //TODO
	      // use this to stop the algorithm for better comparison with and without seeding
	      
	      
	      
	
	      // This piece of code shows how to use the indicator object into the code
	      // of NSGA-II. In particular, it finds the number of evaluations required
	      // by the algorithm to obtain a Pareto front with a hypervolume higher
	      // than the hypervolume of the true Pareto front.
	      if ((indicators != null) &&
	          (requiredEvaluations == 0)) {
	    	  // TODO: compare the current population with the previous Pareto front
	    	double HV = indicators.getHypervolume(population);
	        if (HV >= (0.98 * indicators.getTrueParetoFrontHypervolume())) {
	          requiredEvaluations = evaluations;
	        } // if
	      }// if
	      
	      
		  //===============================
	      // Save Pareto front/set every N evaluations (if defined SAVE_PARETO_EVERY_N_ITERATIONS)
		  try {
			  nParetoSaved ++;
			  Ranking ranking2Save = new Ranking(population); //save only non-dominated
			  exportResults(nParetoSaved, ranking2Save.getSubfront(0), maxEvaluations);
		  } catch (JMException | EvoCheckerException e) {e.printStackTrace();}
		//===============================
	  
	  } // while
	
      parallelEvaluator_.stopEvaluator();
	
	  // Return as output parameter the required evaluations
	    setOutputParameter("evaluations", requiredEvaluations);
	
	    // Return the first non-dominated front
	    Ranking ranking = new Ranking(population);
	    //ranking.getSubfront(0).printObjectivesToFile("data/FUN_NSGAII");
	    //ranking.getSubfront(0).printVariablesToFile("data/VAR_NSGAII");
	    //ranking.getSubfront(0).printFeasibleFUN("FUN_NSGAII");
	    return ranking.getSubfront(0);
  } // execute
  
  
    /*
    * Save the Pareto front/set to file if SAVE_PARETO_EVERY_N_ITERATIONS is defined
    * and saves time to time.txt file.
    * @param maxEvaluations 
	* @param population
	* @throws JMException
	* @throws EvoCheckerException 
	*/
	private void exportResults(int nPareto, SolutionSet solutions, int maxEvaluations) throws JMException, EvoCheckerException {
		int nIterFileSaving = Integer.MAX_VALUE;
		//check if Pareto set to be saved (SAVE_PARETO_EVERY_N_ITERATIONS)
		try {
			nIterFileSaving = Integer.parseInt(Utility.getProperty(Constants.SAVE_PARETO_EVERY_N_ITERATIONS));
			if (nIterFileSaving <= 0)
				return;
		}
		catch (NullPointerException ex) {
			return;
		}
		if (maxEvaluations%nIterFileSaving==0) {
			// Save Pareto front/set
			_exportResults(nPareto, solutions);
	      	// Save time
			Utility.saveTimeToFile("iteration"+String.valueOf(nPareto));
	    }
	}
	
		
	/* 
	 * Save the population to file as in the EvoChecker.java file
	 */
	private void _exportResults(int nPareto, SolutionSet solutions) throws JMException, EvoCheckerException {
		// Print
		System.out.println("Saving Pareto set, iter: " + nPareto);
		//-------- ---------------------------------------------------
		// a) Set variables as in "EvoChecker.java -- 
		//--- from initialiseUsingSettingsProvided method
		// Set variables : note that these must be as in the EvoChecker.java exportResults
		String modelFilename 		= new File(Utility.getProperty(Constants.MODEL_FILE_KEYWORD)).getAbsolutePath();
		String propertiesFilename	= new File(Utility.getProperty(Constants.PROPERTIES_FILE_KEYWORD)).getAbsolutePath();
		String algorithmName		= Utility.getProperty(Constants.ALGORITHM_KEYWORD).toUpperCase();
		String problemName = Utility.getProperty(Constants.PROBLEM_KEYWORD).toUpperCase();
		EvoCheckerType ecType = null;
		switch (EvoCheckerType.valueOf(Utility.getPropertyIgnoreNull(Constants.EVOCHECKER_TYPE).toUpperCase())) {
			case NORMAL		: ecType = EvoCheckerType.NORMAL; break;
			case PARAMETRIC	: ecType = EvoCheckerType.PARAMETRIC; break;
			case REGION		: ecType = EvoCheckerType.REGION; 
							  throw new EvoCheckerException("EvoChecker Region is still in development!. Exiting");			
		}
		
		//--- from initializeProblem method
		IModelInstantiator modelInstantiator = null;
		
		switch (ecType) {
			case NORMAL		: modelInstantiator = new ModelInstantiator(modelFilename, propertiesFilename); break;
			case PARAMETRIC	: modelInstantiator = new ModelInstantiatorParametric(modelFilename, propertiesFilename);break;
			case REGION		: throw new EvoCheckerException("EvoChecker Region is still in development!. Exiting");			
		}
		List<AbstractGene> genes = GenotypeFactory.createChromosome(modelInstantiator.getEvolvableList(), false);
		
		modelInstantiator.createMapping();
		
		// --from initialiseProperties method
		String str = modelInstantiator.getConcreteModel(genes);
		List<List<Property>> list = PropertyFactory.getObjectivesConstraints(str);
		List<Property> objectivesList  = list.get(0);
//		List<Property> constraintsList = list.get(1);
		
		//--- from makeInitialisations method
		String outputDir = "data" + File.separator 
				+ Utility.getProperty(Constants.PROBLEM_KEYWORD)   + File.separator 
				+ Utility.getProperty(Constants.ALGORITHM_KEYWORD) + File.separator;
//		String paretoFrontFile = null;
//		String paretoSetFile = null;
		
		
		//------------------------------------------------------------
		// b) Export results
		// ---  exportResults method
		String n = String.valueOf(nPareto); //<---- identifier
		System.out.println("-------------------------------------------------");
		System.out.println("SOLUTIONS: \t" + solutions.size());
		System.out.println(n);

		String identifier	= problemName +"_"+ algorithmName +"_"+ Utility.getTimeStamp();
		String frontFile	= outputDir + identifier + "_" + n + "_Front";
		String setFile		= outputDir  + identifier +"_" + n + "_Set";
		 try {
			File pf = File.createTempFile(identifier,"_" + n + "_Front", new File(outputDir));
			File ps = File.createTempFile(identifier,"_" + n + "_Set", new File(outputDir));
			
			frontFile 	= pf.getAbsolutePath();
			setFile		= ps.getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		//generate and save headers
//		StringBuilder setHeader = new StringBuilder();
//		for (AbstractGene gene : genes)
//			setHeader.append(gene.getName() +" ");
		String setHeader = String.join("\t", GenotypeFactory.getEvolvableNames());
		FileUtil.saveToFile(setFile, setHeader +"\n", true);
		StringBuilder frontHeader = new StringBuilder();
		Iterator<Property> it = objectivesList.iterator();
		while (it.hasNext()) {
//		for (Property p : objectivesList) {
			Property p = it.next();
			frontHeader.append(p.getExpression());
			if (it.hasNext())
				frontHeader.append("\t");
		}
		FileUtil.saveToFile(frontFile, frontHeader.toString(), true);
		
		List<Solution> solutionList = new ArrayList<Solution>();
		for (int i=0; i<solutions.size(); i++)
			solutionList.add(solutions.get(i));
		Utility.printObjectivesToFile(frontFile, solutionList, objectivesList);
		Utility.printVariablesToFile2(setFile, solutionList, GenotypeFactory.getGeneEvolvableMap(), genes);
		
		//Assign 
//		paretoFrontFile  = frontFile;
//		paretoSetFile	 = setFile; 
//		solutions.printObjectivesToFile(frontFile);
//		solutions.printVariablesToFile(setFile);
		
		System.out.println("\nPareto Front and Pareto set saved at: " + outputDir);
		System.out.println("Pareto Front: " + frontFile);
		System.out.println("Pareto Set: "   + setFile);
		
	}

} // pNSGAII
