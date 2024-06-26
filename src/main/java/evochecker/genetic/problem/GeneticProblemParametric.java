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
import java.util.List;
import java.util.Map.Entry;

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
import evochecker.properties.Property;

public class GeneticProblemParametric extends GeneticProblem{

	private static final long serialVersionUID = -2679872853510614319L;

	/** archive keeping the rational functions for structural parameter combinations**/
	protected Archive archive;
	
	/**
	 * Class constructor: create a new Genetic Problem instance
	 * @param genes
	 * @param properties
	 * @param instantiator
	 * @param numOfConstraints
	 * @throws EvoCheckerException 
	 */
	public GeneticProblemParametric(List<AbstractGene> genes, IModelInstantiator instantiator,
						  List<Property> objectivesList, List<Property> constraintsList, String problemName) throws EvoCheckerException{
		super(genes, instantiator, objectivesList, constraintsList, problemName);

		if (! (instantiator instanceof IModelInstantiatorParametric))
			throw new EvoCheckerException(instantiator + " not instance of IModelInstantiatorParametric");
		
		// construct a dictionary for archiving
		archive = new Archive(verbose);
	}
	
	
	/**
	 * Copy constructor
	 * @param aProblem
	 * @throws EvoCheckerException
	 */
	public GeneticProblemParametric (GeneticProblemParametric aProblem) throws EvoCheckerException{		
		super((GeneticProblem)aProblem);

		// construct a dictionary for archiving
		archive = new Archive(verbose);
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
				if (verbose)
					System.out.print(structParamsValues +"\t");
				if (rfList == null) {//key does not exist in the archive
					archive.miss();
					rfList = new ArrayList<>();
					List<String> rationalFunctionStringsList = generateRationalFunctions(out, in);
					
					rfList = appendToArchive(structParamsValues, nonStructParamsNames, rationalFunctionStringsList);
					if (rfList.isEmpty())//-> bad key
						return evaluateByInvocation(out, in);
					else//algebraic expression generated
						return evaluateAlgebraicExpressions(rfList, nonStructParamsValues);
					
//					if (rationalFunctionStringsList == null) {//model checker did not generate a solution
//						archive.badKey();
//						archive.put(structParamsValues, rfList);//bad key -> empty list
//						return evaluateByInvocation(out, in);
//					}
//					else {
//						for (String functionAsString : rationalFunctionStringsList) {
//							RationalFunction rf = new RationalFunction(functionAsString, nonStructParamsNames);
//							rfList.add(rf);
//						}
//						archive.put(structParamsValues, rfList);
//					}
//					return evaluateAlgebraicExpressions(rfList, nonStructParamsValues);
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
	
	
	private List<String> generateRationalFunctions(PrintWriter out, BufferedReader in) throws Exception {
		String model 		= ((IModelInstantiatorParametric)modelInstantiator).getParametricModel(genes, findGenesUsingType(false));
		String propertyFile = modelInstantiator.getPropertyFileName();
//		return modelInvoker.invokeParam(model, propertyFile, out, in, findGenesUsingType(true));
		return modelInvoker.invokeParam(model, propertyFile, objectivesList, constraintsList, out, in);
	}
	
	
	protected List<String> evaluateAlgebraicExpressions(List<RationalFunction> rfList, List<Number> nonStructParamsValues){
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
	

	protected List<AbstractGene> findGenesUsingType (boolean param) {
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
	
	
	private List<RationalFunction> appendToArchive(String structParamsValues, String nonStructParamsNames, List<String> rationalFunctionStringsList) {
		List<RationalFunction> rfList = new ArrayList<>(); 
		if ((rationalFunctionStringsList == null) || rationalFunctionStringsList.contains(null)) {//model checker did not generate a solution
			archive.badKey();
			archive.put(structParamsValues, rfList);//bad key -> empty list
			//return false;//return evaluateByInvocation(out, in);
		}
		else {
			for (String functionAsString : rationalFunctionStringsList) {
				RationalFunction rf = new RationalFunction(functionAsString, nonStructParamsNames);
				rfList.add(rf);
			}
			archive.put(structParamsValues, rfList);
		}
//		return true; //return evaluateAlgebraicExpressions(rfList, nonStructParamsValues);
		return rfList;
	}
	
	
	@Override
	public String getStatistics() {
		return archive.getStatistics();
	}
}
