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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import evochecker.evolvables.Evolvable;
import evochecker.evolvables.EvolvableDistribution;
import evochecker.exception.EvoCheckerException;
import evochecker.genetic.GenotypeFactory;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.genes.DistributionGene;
import evochecker.genetic.problem.parametric.Archive;
import evochecker.genetic.problem.parametric.RationalFunction;
import evochecker.language.parser.IModelInstantiator;
import evochecker.language.parser.IModelInstantiatorParametric;
import evochecker.modelInvoker.IModelInvoker;
import evochecker.modelInvoker.ModelInvokerStorm;
import evochecker.properties.Property;

public class GeneticProblemParametric2 extends GeneticProblem{

	private static final long serialVersionUID = -2679872853510614319L;

	private Archive archive;
	
	private ConcurrentHashMap<String, Thread> parametricInProcess;
	
	
	private int id=0;
	
	/**
	 * Class constructor: create a new Genetic Problem instance
	 * @param genes
	 * @param properties
	 * @param instantiator
	 * @param numOfConstraints
	 * @throws EvoCheckerException 
	 */
	public GeneticProblemParametric2(List<AbstractGene> genes, IModelInstantiator instantiator,
						  List<Property> objectivesList, List<Property> constraintsList, String problemName) throws EvoCheckerException{
		super(genes, instantiator, objectivesList, constraintsList, problemName);

		if (! (instantiator instanceof IModelInstantiatorParametric))
			throw new EvoCheckerException(instantiator + " not instance of IModelInstantiatorParametric");
		
		// construct a dictionary for archiving
		archive = new Archive();
		
		parametricInProcess = new ConcurrentHashMap<String, Thread>();
	}
	
	
	/**
	 * Copy constructor
	 * @param aProblem
	 * @throws EvoCheckerException
	 */
	public GeneticProblemParametric2 (GeneticProblemParametric2 aProblem) throws EvoCheckerException{		
		super((GeneticProblem)aProblem);

		// construct a dictionary for archiving
		archive = new Archive();
	}
	
	
	@Override
	public  List<String> evaluate(BufferedReader in, PrintWriter out) throws Exception{
		
		String structParamsValues = "";
		String nonStructParamsNames = "";
		List<Number> nonStructParamsValues = new ArrayList<Number>();

		for (Entry<AbstractGene, Evolvable> e : GenotypeFactory.getGeneEvolvableMap().entrySet()) {
//			System.out.println(e.getKey().getAllele());
			
			AbstractGene g = genes.get(genes.indexOf(e.getKey()));

			if (!e.getValue().isParam()) {
				structParamsValues += e.getKey().getAllele()+",";
			}
			else {
				if (g instanceof DistributionGene) {
					nonStructParamsNames += String.join(",", ((EvolvableDistribution)e.getValue()).getEvolvableDoubleNames()) +",";
					double[] distrAllele = ((double[])g.getAllele());
					for (int i=0; i<((DistributionGene)g).getNumberOfOutcomes(); i++) {
						nonStructParamsValues.add(distrAllele[i]);
					}
				}
				else {
					nonStructParamsNames += e.getKey().getName()+",";
					nonStructParamsValues.add((Number)g.getAllele());
				}
			}
		}

		List<RationalFunction> rfList = null;
		if (structParamsValues.length()>0) {
			
			structParamsValues = structParamsValues.substring(0, structParamsValues.length()-1);
			if (nonStructParamsNames.length()>0)
				nonStructParamsNames = nonStructParamsNames.substring(0, nonStructParamsNames.length()-1);

			rfList = archive.get(structParamsValues);
			System.out.print(structParamsValues +"\t");
			if (rfList == null) {//key does not exist in the archive
				archive.miss();

				if (parametricInProcess.get(structParamsValues)==null)
					generateRationalFunctionsParallel(structParamsValues, nonStructParamsNames, out, in);
				
				return evaluateByInvocation(out, in);
			}
			else if (rfList.isEmpty())//bad key retrieved -> normal evaluation
				return evaluateByInvocation(out, in);
			else {//rational functions retrieved -> evaluate the functions
				archive.hit();
				return evaluateAlgebraicExpressions(rfList, nonStructParamsValues);
			}				
		}
		else {//no structural parameters in the model -> normal evaluation (no rational functions)
			return evaluateByInvocation(out, in);
		}
	}

	
	private void generateRationalFunctionsParallel(String structParamsValues, String nonStructParamsNames, PrintWriter out, BufferedReader in) throws Exception {
		try{
			String model 		= ((IModelInstantiatorParametric)modelInstantiator).getParametricModel(genes, findGenesUsingType(false));
			String propertyFile = modelInstantiator.getPropertyFileName();
			RunnableParametricEvaluator paramEvaluator = new RunnableParametricEvaluator(out, in, structParamsValues, nonStructParamsNames, model, propertyFile);
			Thread t = new Thread(paramEvaluator, "ParamEvaluator" + structParamsValues);
			parametricInProcess.put(structParamsValues, t);
			t.start();
		}
		catch (IllegalThreadStateException e) {
			e.printStackTrace();
		}
	}
	
	private List<String> evaluateAlgebraicExpressions(List<RationalFunction> rfList, List<Number> nonStructParamsValues){
		List<String> results = new ArrayList<>();
		double[] arguments = new double[nonStructParamsValues.size()];
		for (int i=0; i<arguments.length; i++) {
			arguments[i] = nonStructParamsValues.get(i).doubleValue();		
		}
		for (RationalFunction rf : rfList) { 
			results.add(rf.evaluate(arguments));
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
	
	
	@SuppressWarnings("deprecation")
	@Override
	public void closeDown() {
		for (Thread t : parametricInProcess.values()) {
			if(t.isAlive())
				t.stop();
		}
	}

	
	
	private class RunnableParametricEvaluator implements Runnable {

		PrintWriter out;
		BufferedReader in;
		String structParamsValues;
		String nonStructParamsNames; 
		String model;
		String propertyFile;
		
		public RunnableParametricEvaluator(PrintWriter out, BufferedReader in, String structParamsValues, String nonStructParamsNames,
										String model, String propertyFile) {
			this.out = out;
			this.in  = in;
			this.structParamsValues		= structParamsValues;
			this.nonStructParamsNames	= nonStructParamsNames;
			this.model					= model;
			this.propertyFile			= propertyFile;
		}
		
		
		@Override
		public void run() {
			try {
				IModelInvoker invoker = modelInvoker.copy();
				((ModelInvokerStorm)invoker).ERROR  += id;
				((ModelInvokerStorm)invoker).OUTPUT += id++;
				List<String> rationalFunctionStringsList = invoker.invokeParam(model, propertyFile, objectivesList, constraintsList, out, in);
				appendToArchive(structParamsValues, nonStructParamsNames, rationalFunctionStringsList);
			} catch (IOException e) {
				e.printStackTrace();
				appendToArchive(structParamsValues, nonStructParamsNames, null);
			}
		}
		
		
		private boolean appendToArchive(String structParamsValues, String nonStructParamsNames, List<String> rationalFunctionStringsList) {
			List<RationalFunction> rfList = new ArrayList<>(); 
			if ((rationalFunctionStringsList == null) || rationalFunctionStringsList.contains(null)) {//model checker did not generate a solution
				archive.badKey();
				archive.put(structParamsValues, rfList);//bad key -> empty list
				return false;//return evaluateByInvocation(out, in);
			}
			else {
				for (String functionAsString : rationalFunctionStringsList) {
					RationalFunction rf = new RationalFunction(functionAsString, nonStructParamsNames);
					rfList.add(rf);
				}
				archive.put(structParamsValues, rfList);
			}
			return true; //return evaluateAlgebraicExpressions(rfList, nonStructParamsValues);
		}
	}
		
}
