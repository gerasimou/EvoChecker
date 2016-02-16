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

import java.util.*;
import java.util.Map.Entry;

import parser.ast.Expression;
import parser.ast.ExpressionProb;
import parser.ast.ExpressionReward;
import parser.ast.ExpressionFilter;
import parser.ast.RelOp;
import prism.PrismException;
import prism.PrismLog;

/**
 * Base class for decomposition procedures to solve min and/or max
 * synthesis problems.
 * 
 * @see MinSynthesis
 * @see MaxSynthesis
 */
abstract class OptimisingSynthesis extends DecompositionProcedure
{
	static double Epsilon = 10E-6;

	// Synthesis parameters
	/** greatest acceptable difference between {@link #maximalUpperProbBoundOfOptimising}
	 *  and {@link #minimalLowerProbBoundOfOptimising} */
	protected double probTolerance;
	/** user-friendly caption string to be printed instead of "optimising" */
	protected String captionForOptimising;

	// Properties of the model to be checked
	/** model's initial state */
	protected int initState;

	// Solution structures
	/** regions marked as "optimising" */
	protected LabelledBoxRegions optimisingRegions;
	/** regions marked as "non-optimising" */
	protected LabelledBoxRegions nonOptimisingRegions;
	/** minimal lower probability bound from among {@link #optimisingRegions} */
	protected double minimalLowerProbBoundOfOptimising;
	/** maximal upper probability bound from among {@link #optimisingRegions} */
	protected double maximalUpperProbBoundOfOptimising;
	/** probability bounds as they were successively used to distinguish
	 *  between "optimising" and "non-optimising" regions */
	protected LinkedList<Double> demarcationProbBounds;

	final private int minDecompositions;

	public OptimisingSynthesis(double probTolerance)
	{
		this.probTolerance = probTolerance;
		// TODO: Take this as an arg in the constructor.
		PSEMultOptions options = PSEMultUtility.getOptions();
		this.minDecompositions = Math.max(1, Math.max(options.getMany(), options.getPara()));
	}

	@Override
	public void initialiseModelChecking(PSEModelChecker modelChecker, PSEModel model, Expression propExpr) throws PrismException
	{
		super.initialiseModelChecking(modelChecker, model, propExpr);
		initState = model.getFirstInitialState();
		optimisingRegions = new LabelledBoxRegions();
		nonOptimisingRegions = new LabelledBoxRegions();
		demarcationProbBounds = new LinkedList<Double>();
	}

	@Override
	protected void processPropertyExpression() throws PrismException
	{
		Expression propExprTmp = propExpr;
		while (propExprTmp instanceof ExpressionFilter) {
			propExprTmp = ((ExpressionFilter)propExprTmp).getOperand();
		}
		try {
			RelOp relOp;
			try {
				ExpressionProb probExprTmp = (ExpressionProb) propExprTmp;
				relOp = probExprTmp.getRelOp();
			} catch (ClassCastException e) {
				ExpressionReward rewExpr = (ExpressionReward) propExprTmp;
				relOp = rewExpr.getRelOp();
			}
			if (relOp != RelOp.EQ)
				throw new ClassCastException();
		} catch (ClassCastException e) {
			throw new PrismException("Min and max syntheses require an operator of the form P=? or R=?");
		}
	}

	protected abstract void determineOptimisingRegions(BoxRegionValues regionValues) throws PrismException;

	@Override
	protected void verifyRegionValues(BoxRegionValues regionValues) throws DecompositionNeeded, PrismException
	{
		determineOptimisingRegions(regionValues);

		// Determine the deciding probability bounds
		minimalLowerProbBoundOfOptimising = Double.POSITIVE_INFINITY;
		maximalUpperProbBoundOfOptimising = Double.NEGATIVE_INFINITY;
		BoxRegion regionToDecomposeMin = null;
		BoxRegion regionToDecomposeMax = null;
		List<Entry<BoxRegion, BoxRegionValues.StateValuesPair>> regionsToDecomposeMin =
			new ArrayList<Entry<BoxRegion, BoxRegionValues.StateValuesPair>>();
		List<Entry<BoxRegion, BoxRegionValues.StateValuesPair>> regionsToDecomposeMax =
			new ArrayList<Entry<BoxRegion, BoxRegionValues.StateValuesPair>>();
		int paramCnt = 0;
		for (Entry<BoxRegion, BoxRegionValues.StateValuesPair> entry : regionValues) {
			if (!optimisingRegions.contains(entry.getKey())) {
				continue;
			}
			if (minDecompositions > 1) {
				regionsToDecomposeMin.add(entry);
				regionsToDecomposeMax.add(entry);
			}
			paramCnt = entry.getKey().getLowerBounds().getNumValues();

			double currentLowerProbBound = (Double) entry.getValue().getMin().getValue(initState);
			if (currentLowerProbBound < minimalLowerProbBoundOfOptimising) {
				minimalLowerProbBoundOfOptimising = currentLowerProbBound;
				regionToDecomposeMin = entry.getKey();
			}

			double currentUpperProbBound = (Double) entry.getValue().getMax().getValue(initState);
			if (currentUpperProbBound > maximalUpperProbBoundOfOptimising) {
				maximalUpperProbBoundOfOptimising = currentUpperProbBound;
				regionToDecomposeMax = entry.getKey();
			}
		}

		// Evaluate whether a decomposition is needed
		if (maximalUpperProbBoundOfOptimising - minimalLowerProbBoundOfOptimising > probTolerance) {
			if (minDecompositions > 1) {
				Collections.sort(regionsToDecomposeMin,
					new Comparator<Entry<BoxRegion, BoxRegionValues.StateValuesPair>>()
				{
					@Override
					public int compare(Entry<BoxRegion, BoxRegionValues.StateValuesPair> t0, Entry<BoxRegion,
						BoxRegionValues.StateValuesPair> t1)
					{
						return Double.compare((Double) t0.getValue().getMin().getValue(initState),
							(Double) t1.getValue().getMin().getValue(initState));
					}
				});
				Collections.sort(regionsToDecomposeMax,
					new Comparator<Entry<BoxRegion, BoxRegionValues.StateValuesPair>>()
					{
						@Override
						public int compare(Entry<BoxRegion, BoxRegionValues.StateValuesPair> t0, Entry<BoxRegion,
							BoxRegionValues.StateValuesPair> t1)
						{
							return Double.compare((Double) t1.getValue().getMax().getValue(initState),
								(Double) t0.getValue().getMax().getValue(initState));
						}
					});
				LabelledBoxRegions regionsToDecompose_ = new LabelledBoxRegions();
				Iterator<Entry<BoxRegion,BoxRegionValues.StateValuesPair>> itMin = regionsToDecomposeMin.iterator();
				Iterator<Entry<BoxRegion,BoxRegionValues.StateValuesPair>> itMax = regionsToDecomposeMax.iterator();
				int decomposed = 0;
				int i = 1;
				while (itMin.hasNext() && decomposed < minDecompositions) {
					BoxRegion regMin = itMin.next().getKey();
					decomposed += 1 << regMin.getLowerBounds().getNumValues();
					regionsToDecompose_.add(regMin, String.format("#%s min lower prob bound", i));
					BoxRegion regMax = itMax.next().getKey();
					decomposed += 1 << regMax.getLowerBounds().getNumValues();
					regionsToDecompose_.add(regMax, String.format("#%s max upper prob bound", i));
					++i;
				}
				throw new DecompositionNeeded("Probability tolerance was not satisfied,\n" +
					maximalUpperProbBoundOfOptimising + " - " + minimalLowerProbBoundOfOptimising + " > " + probTolerance,
					regionsToDecompose_);
			} else {
				LabelledBoxRegions regionsToDecompose_ = new LabelledBoxRegions();
				regionsToDecompose_.add(regionToDecomposeMin, "min lower prob bound");
				regionsToDecompose_.add(regionToDecomposeMax, "max upper prob bound");
				throw new DecompositionNeeded("Probability tolerance was not satisfied,\n" +
					maximalUpperProbBoundOfOptimising + " - " + minimalLowerProbBoundOfOptimising + " > " + probTolerance,
					regionsToDecompose_);
			}
		}
	}

	@Override
	public void printSolution(PrismLog log, boolean verbose)
	{
		printIntro(log);

		log.print("\nRegions " + captionForOptimising + " the property satisfaction probability");
		log.println(" (" + optimisingRegions.size() + "):");
		optimisingRegions.print(log);
		log.print("Non-" + captionForOptimising + " regions");
		log.println(" (" + nonOptimisingRegions.size() + "):");
		nonOptimisingRegions.print(log);

		log.println("\nMin lower prob bound of " + captionForOptimising + " regions = " + minimalLowerProbBoundOfOptimising);
		log.println("Max upper prob bound of " + captionForOptimising + " regions = " + maximalUpperProbBoundOfOptimising);
		log.println("Probability tolerance = " + probTolerance);

		if (verbose) {
			log.println("\nDemarcation prob bounds in the order they were used to exclude regions:");
			log.println(demarcationProbBounds);
		}
	}
}
