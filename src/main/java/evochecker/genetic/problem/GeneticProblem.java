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

package evochecker.genetic.problem;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mariuszgromada.math.mxparser.Function;

import evochecker.evolvables.Evolvable;
import evochecker.exception.EvoCheckerException;
import evochecker.genetic.GenotypeFactory;
import evochecker.genetic.genes.AbstractGene;
import evochecker.language.parser.IModelInstantiator;
import evochecker.properties.Property;
import jmetal.core.Solution;
import jmetal.util.JMException;

public class GeneticProblem extends GeneticModelProblem {

	private static final long serialVersionUID = -2679872853510614319L;

	private Map<List<Object>, List<Function>> algebraicExpessionsArchive;

	/**
	 * Class constructor: create a new Genetic Problem instance
	 * @param genes
	 * @param properties
	 * @param instantiator
	 * @param numOfConstraints
	 */
	public GeneticProblem(List<AbstractGene> genes, IModelInstantiator instantiator,
						  List<Property> objectivesList, List<Property> constraintsList, String problemName){
		super(genes, instantiator, objectivesList, constraintsList, problemName);
		
		// construct a dictionary for archiving
		algebraicExpessionsArchive = new HashMap<List<Object>, List<Function>>();
	}
	
	
	/**
	 * Copy constructor
	 * @param aProblem
	 * @throws EvoCheckerException
	 */
	public GeneticProblem (GeneticProblem aProblem) throws EvoCheckerException{		
		super((GeneticModelProblem)aProblem);

		// construct a dictionary for archiving
		algebraicExpessionsArchive = new HashMap<List<Object>, List<Function>>();
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
		
		
		try {
			//parametric work goes here
			List<String> resultsList = null; 
//			resultsList = parametricWork(out, in);
			resultsList = evaluateByInvocation(out, in);

			//evaluate objectives
			for (int i = 0; i < numberOfObjectives_; i++) {
				Property p = objectivesList.get(i);
				int index  = p.getIndex();
				double value  = Double.parseDouble(resultsList.get(index));
				double result = p.evaluate(value);
				solution.setObjective(i, result);
				if (verbose)
					System.out.print("O" +(i+1) + "):"+ result +"\t");
			}

			//evaluate constraints
			this.evaluateConstraints(solution, resultsList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (verbose)
			System.out.println();
	}	
	
	
	private List<String> parametricWork(PrintWriter out, BufferedReader in) throws Exception{
		List<Object> 		alleles 	= new ArrayList<Object>();
		
		String structParams = "";
		for (Entry<AbstractGene, Evolvable> e : GenotypeFactory.getGeneEvolvableMap().entrySet()) {
//			System.out.println(e.getKey().getAllele());
			if (!e.getValue().isParam()) {
				AbstractGene g = genes.get(genes.indexOf(e.getKey()));
				alleles.add((g.getAllele()));
//				evolvables.add(e.getValue());
				structParams += e.getValue().getName() +",";
			}
		}
		structParams = structParams.substring(0, structParams.length()-1);

		List<Function> functionsList = null;
//		try {
			if (alleles.size()>0) {
				functionsList = algebraicExpessionsArchive.get(alleles);
				if (functionsList == null) {
					functionsList = new ArrayList<>();
					List<String> rationalFunctionsList = new ArrayList<>();
					rationalFunctionsList = generateRationalFunctions(out, in);
						
						for (String rf : rationalFunctionsList) {
							String[] rationalFunction = formatRationalFunction(rf);
							Function f = new Function("f(" + structParams + ") = " + rationalFunction[0] +"/"+ rationalFunction[1]);
							boolean isFunctionOK = f.checkSyntax();
							functionsList.add(f);
						}
						algebraicExpessionsArchive.put(alleles, functionsList);
						
				}
				
				return evaluateAlgebraicExpressions(functionsList, alleles);
			}
			else {
				return evaluateByInvocation(out, in);
			}
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
	}
	

	private List<String> evaluateByInvocation(PrintWriter out, BufferedReader in) throws Exception {
		//Prepare model
		//System.out.println(genes.get(0).getAllele() +"\t"+ genes.get(1).getAllele() );
		String model 		= modelInstantiator.getConcreteModel(this.genes);
		String propertyFile = modelInstantiator.getPropertyFileName();
		
		return modelInvoker.invoke(model, propertyFile, out, in);
	}
	
	
	private String[] formatRationalFunction (String rationalFunction) {
		String [] rationalFunctionParts = new String[2];
		rationalFunctionParts = rationalFunction.replace("{","").replace("}","").strip().split("\\|");
		for (int i=0; i<2; i++) {
			String rfPart = rationalFunctionParts[i];
			rfPart 			= rfPart.replaceAll("\\s+", "");
	        Pattern pattern = Pattern.compile("([0-9]+|[a-z\\)])(?=[a-z\\(])");
	        Matcher m = pattern.matcher(rfPart);
	        rationalFunctionParts[i] = m.replaceAll("$1*");
		}
        return rationalFunctionParts;
	}
	
	
	private List<String> generateRationalFunctions(PrintWriter out, BufferedReader in) throws Exception {
		String model 		= modelInstantiator.getParametricModel(genes, findGenesUsingType(true));
		String propertyFile = modelInstantiator.getPropertyFileName();
		return modelInvoker.invoke(model, propertyFile, out, in, findGenesUsingType(false));
	}
	
	
	private List<String> evaluateAlgebraicExpressions(List<Function> algebraicExpressions, List<Object> alleles){
		List<String> results = new ArrayList<>();
		double[] arguments = new double[alleles.size()];
		for (int i=0; i<alleles.size(); i++) {
			arguments[i] = Double.parseDouble(alleles.get(i) +"");		
		}
		for (Function f : algebraicExpressions) { 
			results.add(f.calculate(arguments) +"");
		}
		
		return results;
	}
	

	private List<AbstractGene> findGenesUsingType (boolean param) {
		List<AbstractGene> 		genes 	= new ArrayList<AbstractGene>();

		for (Entry<AbstractGene, Evolvable> e : GenotypeFactory.getGeneEvolvableMap().entrySet()) {
			if (param && e.getValue().isParam()) {
				genes.add(e.getKey());
			}
			else if (!param && !e.getValue().isParam()) {
				genes.add(e.getKey());				
			}
		}
		return genes;
	}
}
