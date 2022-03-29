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

import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import evochecker.evaluator.IParallelEvaluator;
import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.core.Variable;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.Ranking;
import jmetal.util.comparators.CrowdingComparator;
import evochecker.genetic.jmetal.encoding.ArrayReal;
import evochecker.genetic.jmetal.encoding.ArrayInt;
import java.io.File;
import evochecker.auxiliary.Constants;
import evochecker.auxiliary.FileUtil;
import evochecker.auxiliary.Utility;

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
    int numberOfThreads ;

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

    String set="";
    List<String> lines = new ArrayList<String>();
    if (Boolean.parseBoolean(Utility.getProperty(Constants.RELOAD_KEYWORD)))
    {
      // Find most recent Pareto Set
      String problem_string=Utility.getProperty(Constants.PROBLEM_KEYWORD).toUpperCase();
      String path="./data/"+problem_string+"/NSGAII/";
      File source = new File(path); 
      String[] filelist=source.list();

      long modified=0;

      for(String str: filelist)
      {
        if (str.split("_")[4].equals("Set"))
        {
          File file= new File(path+str);
          long time=file.lastModified();
          if (time>modified)
          {
            set=str;
            modified=time;
          }
        }
      }

      // Print previously found Set to be used
      if (!(set.equals(""))) 
      {
        System.out.println("Loading previous Pareto Set: "+set);
        try 
        {
          BufferedReader set_reader = new BufferedReader(new FileReader(path+set));
          String line;
          while((line=set_reader.readLine())!=null)
          {
            lines.add(line);
            // System.out.println(line);
          }
          set_reader.close();     
        } catch (IOException e)
        {
          e.printStackTrace();
        }
      }
    }
    // Create the initial solutionSet
    Solution newSolution;
    for (int i = 0; i < populationSize; i++) {
      newSolution = new Solution(problem_);

      // Change initial population to previously found set (if exists)
      if (!(set.equals("")))
      {
        if (i<lines.size()-2)
        {
          // System.out.println("Before");
          // System.out.println(newSolution.getDecisionVariables()[0]);
          // System.out.println(newSolution.getDecisionVariables()[1]);
          String result = lines.get(i+2).trim().replaceAll("\\s",",");

          // Get arrays
          ArrayReal real_arr=(ArrayReal) newSolution.getDecisionVariables()[0];
          ArrayInt  int_arr=(ArrayInt) newSolution.getDecisionVariables()[1];

          // Replace real values
          for (int j=0; j<real_arr.getLength(); j++)
          {
            double val=Double.parseDouble(result.split(",")[j]);
            real_arr.setValue(j, val);
          }
          //Replace int values
          for (int j=real_arr.getLength(); j<real_arr.getLength()+int_arr.getLength(); j++)
          {
            int val=Integer.parseInt(result.split(",")[j]);
            int_arr.setValue(j-real_arr.getLength(), val);
          }
          // Sanity check on update happening
          // System.out.println("After");
          // System.out.println(newSolution.getDecisionVariables()[0]);
          // System.out.println(newSolution.getDecisionVariables()[1]);
        }
      }    
      parallelEvaluator_.addSolutionForEvaluation(newSolution) ;
    }

    // for (int i=0; i<populationSize; i++)
    // {
    //   System.out.println(i);
    //   Solution test=population.get(i);
    //   // Variable[] test_v=test.getDecisionVariables();

    //   // for (int j=0; j<test_v.length; j++)
    //   // {
    //   //   System.out.println(test_v[i]);
    //   // }

    //   // Solution test=new Solution();
    //   // population.replace(i, test);
    // }

    List<Solution> solutionList = parallelEvaluator_.parallelEvaluation() ;
    for (Solution solution : solutionList) {
      population.add(solution) ;
      evaluations ++ ;
    }
    
    int times = 1;
    
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
          parallelEvaluator_.addSolutionForEvaluation(offSpring[0]) ;
          parallelEvaluator_.addSolutionForEvaluation(offSpring[1]) ;
        } 
      }

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

      // This piece of code shows how to use the indicator object into the code
      // of NSGA-II. In particular, it finds the number of evaluations required
      // by the algorithm to obtain a Pareto front with a hypervolume higher
      // than the hypervolume of the true Pareto front.
      if ((indicators != null) &&
          (requiredEvaluations == 0)) {
        double HV = indicators.getHypervolume(population);
        if (HV >= (0.98 * indicators.getTrueParetoFrontHypervolume())) {
          requiredEvaluations = evaluations;
        } // if
      }// if
      
      
      
//      //Save the pareto set every 25% evaluations
//      if (maxEvaluations*times/4 <= evaluations){
//        System.out.println("Saving Pareto set");
//        Ranking rank = new Ranking(population);
//        SolutionSet paretoSet = rank.getSubfront(0);
//        paretoSet.printVariablesToFile("data/VAR_NSGAII"+times);
//        paretoSet.printObjectivesToFile("data/FUN_NSGAII"+times);
//        times ++;
//      }
      
    } // while

    parallelEvaluator_.stopEvaluator();

    // Return as output parameter the required evaluations
    setOutputParameter("evaluations", requiredEvaluations);

    // Return the first non-dominated front
    Ranking ranking = new Ranking(population);
    return ranking.getSubfront(0);
  } // execute
} // pNSGAII
