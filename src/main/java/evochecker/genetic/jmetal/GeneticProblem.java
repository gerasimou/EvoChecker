//==============================================================================
//	
//	Copyright (c) 2015-
//	Authors:
//	* Simos Gerasimou (University of York)
//	
//------------------------------------------------------------------------------
//	
//	This file is part of EvoChecker.
//	
//==============================================================================

package evochecker.genetic.jmetal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import org.spg.language.parser.InstantiatorInterface;

import evochecker.exception.EvoCheckerException;
//import org.apache.commons.lang.NotImplementedException;
import evochecker.genetic.genes.AbstractGene;
import evochecker.properties.Property;
import jmetal.core.Solution;
import jmetal.util.JMException;

public class GeneticProblem extends GeneticModelProblem {

	private static final long serialVersionUID = -2679872853510614319L;
	protected static  int eval = 0;
	public static synchronized int getEval() {
		return eval;
	}

	
	
	/**
	 * Class constructor: create a new Genetic Problem instance
	 * @param genes
	 * @param properties
	 * @param instantiator
	 * @param numOfConstraints
	 */
	public GeneticProblem(List<AbstractGene> genes, InstantiatorInterface instantiator,
						  List<Property> objectivesList, List<Property> constraintsList, String problemName){
		super(genes, instantiator, objectivesList, constraintsList, problemName);
	}
	
	
	public GeneticProblem (GeneticProblem aProblem) throws EvoCheckerException{		
		super((GeneticModelProblem)aProblem);
	}

	/** 
	 * Evaluate 
	 * @param solution
	 * @param out
	 * @param in
	 * @throws JMException
	 * @throws EvoCheckerException 
	 */
	@Override
	public void parallelEvaluate(BufferedReader in, PrintWriter out, Solution solution) throws JMException, EvoCheckerException {
		//Populate genes
		this.populateGenesWithRealSolution(solution);
		this.populateGenesWithIntSolution(solution);

		//Prepare params
		String model = instantiator.getValidModelInstance(this.genes);
//		System.out.println(model);
		String propertyFile = instantiator.getPrismPropertyFileName();
//		Utility.exportToFile("model.txt", model);

		List<String> resultsList;
		try {
			resultsList = this.invokePrism(model, propertyFile, out, in);

			for (int i = 0; i < numberOfObjectives_; i++) {
				Property p = objectivesList.get(i);
				double result;
				if (p.isMaximization()) {
					result = new BigDecimal(- Double.parseDouble(resultsList.get(i))).setScale(3, RoundingMode.HALF_DOWN).doubleValue();
				}
				else{
					result = new BigDecimal(Double.parseDouble(resultsList.get(i))).setScale(3, RoundingMode.HALF_UP).doubleValue();
				}
				solution.setObjective(i, result);
				System.out.print("FITNESS: "+ result +"\t");
			}
			
			if (numberOfConstraints_>0)
				this.evaluateConstraints(solution, resultsList);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println();
	}


	/**
	 * Prism invocation method
	 * @param model
	 * @param propertyFile
	 * @param out
	 * @param in
	 * @return
	 * @throws IOException
	 */
	protected List<String> invokePrism(String model, String propertyFile, PrintWriter out, BufferedReader in)
			throws IOException {
//		System.out.println("Sending to PRISM: "+propertyFile);
//		System.out.println("Sending to PRISM: "+model);
		out.print(model + "@" + propertyFile + "\nEND\n");
		out.flush();

		String line;
		StringBuilder modelBuilder = new StringBuilder();
		do {
//			System.out.println("Waiting PRISM");
			line = in.readLine();
			if (line.endsWith("END"))
				break;
			modelBuilder.append(line);
			modelBuilder.append("\n");
		} while (true);

		String res[] = modelBuilder.toString().trim().split("@");
//		System.out.println("Received from PRISM: "+ modelBuilder.toString());
		return Arrays.asList(res);
	}
	
	
	protected List<String> invokePrism(BufferedReader in, PrintWriter out, String output) throws IOException {
		out.print(output);
		out.flush();

		String line;
		StringBuilder modelBuilder = new StringBuilder();
		do {
//			System.out.println("Waiting PRISM");
			line = in.readLine();
			if (line.endsWith("END"))
				break;
			modelBuilder.append(line);
			modelBuilder.append("\n");
		} while (true);

		String res[] = modelBuilder.toString().trim().split("@");
//		System.out.println("Received from PRISM: "+ modelBuilder.toString());
		return Arrays.asList(res);
	}
	
	
}
