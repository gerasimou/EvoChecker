//==============================================================================
//	
//	Copyright (c) 2014-
//	Authors:
//	* Andrej Tokarcik <andrejtokarcik@gmail.com> (Masaryk University)
//	
//------------------------------------------------------------------------------
//	
//	This file is part of PRISM.
//	
//	PRISM is free software; you can redistribute it and/or modify
//	it under the terms of the GNU General Public License as published by
//	the Free Software Foundation; either version 2 of the License, or
//	(at your option) any later version.
//	
//	PRISM is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU General Public License for more details.
//	
//	You should have received a copy of the GNU General Public License
//	along with PRISM; if not, write to the Free Software Foundation,
//	Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//	
//==============================================================================

package pse;

import java.util.Set;

import parser.ast.Expression;
import parser.ast.ExpressionFilter;
import prism.PrismException;
import prism.PrismLog;

/**
 * Class to enforce decomposing of the parameter space during
 * various PSE analyses.
 * 
 * @see PSEModelChecker
 */
public abstract class DecompositionProcedure
{
	protected PSEModelChecker modelChecker;
	protected PSEModel model;
	protected Expression propExpr;

	public enum Type
	{
		SIMPLE, THRESHOLD, MIN_NAIVE, MIN_SAMPLING, MAX_NAIVE, MAX_SAMPLING;
	}

	/**
	 * Exception raised when results didn't pass a test
	 * of the decomposition procedure, and thus decomposition
	 * is needed.
	 */
	@SuppressWarnings("serial")
	public static class DecompositionNeeded extends Exception
	{
		/** description of the problem due to which decomposition is needed */
		protected String reason;
		/** which regions are to be decomposed to resolve the problem */
		protected LabelledBoxRegions regionsToDecompose;
		/** result that didn't pass the examination */
		protected BoxRegionValues examinedRegionValues;

		public DecompositionNeeded(String reason, LabelledBoxRegions regionsToDecompose)
		{
			this.reason = reason;
			this.regionsToDecompose = regionsToDecompose;
		}

		public DecompositionNeeded(String reason, BoxRegion region, String regionLabel)
		{
			this.reason = reason;
			this.regionsToDecompose = new LabelledBoxRegions(region, regionLabel);
		}

		/**
		 * Gets set of regions that need to be decomposed.
		 * 
		 * @return set of regions that need to be decomposed
		 */
		public Set<BoxRegion> getRegionsToDecompose()
		{
			return regionsToDecompose.keySet();
		}

		public LabelledBoxRegions getLabelledRegionsToDecompose()
		{
			return regionsToDecompose;
		}

		/**
		 * Pretty-prints the list of regions to be decomposed as well as
		 * the respective reasons why decomposition is needed.
		 * 
		 * @param log file into which to print
		 */
		public void printRegionsToDecompose(PrismLog log)
		{
			log.println("\nThe following " + regionsToDecompose.size() + " regions are to be decomposed:");
			regionsToDecompose.print(log);
			log.println("Reason: " + reason + "\n");
		}

		/**
		 * Sets result that didn't pass the examination.
		 * 
		 * @param examinedRegionValues examined region values
		 */
		public void setExaminedRegionValues(BoxRegionValues examinedRegionValues)
		{
			this.examinedRegionValues = examinedRegionValues;
		}

		/**
		 * Gets result that didn't pass the examination.
		 * 
		 * @return examined region values
		 */
		public BoxRegionValues getExaminedRegionValues()
		{
			return examinedRegionValues;
		}
	}

	/**
	 * Initialises the decomposition procedure prior to model checking.
	 * 
	 * @param modelChecker model checker used for model checking
	 * @param model model to be checked
	 * @param propExpr property expression to be checked
	 * @throws PrismException in case property expression could not be processed
	 */
	public void initialiseModelChecking(PSEModelChecker modelChecker, PSEModel model, Expression propExpr) throws PrismException
	{
		this.modelChecker = modelChecker;
		this.model = model;
		this.propExpr = propExpr;
		processPropertyExpression();
	}

	/**
	 * Processes and possibly modifies the property expression
	 * as passed in during initialisation for model checking.
	 * 
	 * @throws PrismException
	 */
	protected void processPropertyExpression() throws PrismException
	{
		// Wrap a filter round the property, if needed
		// (in order to extract the final result of model checking)
		propExpr = ExpressionFilter.addDefaultFilterIfNeeded(propExpr, model.getNumInitialStates() == 1);
	}

	/**
	 * Gets property expression to be model checked.
	 * 
	 * @return property expression to be model checked
	 */
	public Expression getPropertyExpression()
	{
		return propExpr;
	}

	/**
	 * Examines partially computed result.
	 * 
	 * @param regionValues most recent results for all regions processed so far
	 * @param region region currently being processed
	 * @param probsMin minimised probabilities in this region as processed so far
	 * @param probsMax maximised probabilities in this region as processed so far
	 * @throws DecompositionNeeded if partial result didn't meet the criteria
	 * characteristic for the decomposition procedure
	 * @throws PrismException if inner method failed
	 */
	final public void examinePartialComputation(BoxRegionValues regionValues, BoxRegion region, double probsMin[], double probsMax[])
			throws DecompositionNeeded, PrismException
	{
		try {
			handleNaN(region, probsMin, probsMax);
			verifySingleRegion(region, probsMin, probsMax);
		} catch (DecompositionNeeded e) {
			e.setExaminedRegionValues(regionValues);
			throw e;
		}
	}

	final public DecompositionNeeded examinePartialComputationNoThrow(BoxRegionValues regionValues, BoxRegion region, double probsMin[], double probsMax[])
			throws PrismException
	{
		DecompositionNeeded decompositionNeeded = null;
		try {
			handleNaN(region, probsMin, probsMax);
			verifySingleRegion(region, probsMin, probsMax);
		} catch (DecompositionNeeded e) {
			e.setExaminedRegionValues(regionValues);
			decompositionNeeded = e;
		}
		return decompositionNeeded;
	}

	protected void handleNaN(BoxRegion region, double probsMin[], double probsMax[])
			throws DecompositionNeeded
	{
		assert probsMin.length == probsMax.length;
		for (int state = 0; state < probsMin.length; state++) {
			// probsMin cannot contain a NaN if probsMax doesn't contain one as well, I guess
			if (Double.isNaN(probsMax[state])) {
				throw new DecompositionNeeded("NaN was found in state " + state, region, "NaN");
			}
		}
	}

	/**
	 * Verifies that a single region's probabilities are accurate enough,
	 * or whatever other criteria the decomposition procedure is interested in.
	 * 
	 * @param region region for which {@code probsMin} and {@code probsMax}
	 * were computed
	 * @param probsMin minimised probabilities
	 * @param probsMax maximised probabilities
	 * @throws DecompositionNeeded if {@code probsMin} and {@code probsMax}
	 * didn't meet the decomposition procedure's criteria
	 * @throws PrismException
	 */
	protected void verifySingleRegion(BoxRegion region, double probsMin[], double probsMax[])
			throws DecompositionNeeded, PrismException
	{
	}

	/**
	 * Examines wholly computed result.
	 * 
	 * @param regionValues proposed final result
	 * @throws DecompositionNeeded if partial result didn't meet the criteria
	 * characteristic for the decomposition procedure
	 * @throws PrismException if inner method failed
	 */
	final public void examineWholeComputation(BoxRegionValues regionValues)
			throws DecompositionNeeded, PrismException
	{
		try {
			verifyRegionValues(regionValues);
		} catch (DecompositionNeeded e) {
			e.setExaminedRegionValues(regionValues);
			throw e;
		}
	}

	final public DecompositionNeeded examineWholeComputationNoThrow(BoxRegionValues regionValues)
		throws PrismException
	{
		DecompositionNeeded decompositionNeeded = null;
		try {
			verifyRegionValues(regionValues);
		} catch (DecompositionNeeded e) {
			e.setExaminedRegionValues(regionValues);
			decompositionNeeded = e;
		}
		return decompositionNeeded;
	}

	/**
	 * Verifies that the proposed final result passes the decomposition
	 * procedure's criteria.
	 * 
	 * @param regionValues proposed final result
	 * @throws DecompositionNeeded if {@code regionValues} didn't meet
	 * the decomposition procedure's criteria
	 * @throws PrismException
	 */
	protected void verifyRegionValues(BoxRegionValues regionValues)
			throws DecompositionNeeded, PrismException
	{
	}

	/**
	 * Prints the solution in a manner suitable for the problem
	 * addressed by the decomposition procedure.
	 * 
	 * @param log file into which to print
	 * @param verbose whether output ought to be verbose
	 */
	public void printSolution(PrismLog log, boolean verbose)
	{
		// The default filter added above takes care of printing the solution
	}

	protected void printIntro(PrismLog log)
	{
		log.println("\nSolution of " + toString() + " for property " + propExpr + ":");
	}
}
