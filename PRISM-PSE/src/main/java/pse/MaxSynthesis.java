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

import java.util.Map.Entry;

import prism.PrismException;

/**
 * Decomposition procedure solving the max synthesis problem, with
 * two approaches to computing the demarcation probability bound: <ul>
 * <li>naive approach, implemented as {@link MaxSynthesisNaive};
 * <li>sampling-based approach, implemented as {@link MaxSynthesisSampling}.
 * </ul>
 */
abstract class MaxSynthesis extends OptimisingSynthesis
{
	public MaxSynthesis(double probTolerance)
	{
		super(probTolerance);
		captionForOptimising = "maximising";
	}

	protected abstract double getMaximalLowerBound(BoxRegionValues regionValues) throws PrismException;

	@Override
	protected void determineOptimisingRegions(BoxRegionValues regionValues) throws PrismException
	{
		// Determine the maximal lower bound
		double maximalLowerBound = getMaximalLowerBound(regionValues);
		demarcationProbBounds.add(maximalLowerBound);

		// Determine the (non-)maximising regions
		optimisingRegions.clear();
		for (Entry<BoxRegion, BoxRegionValues.StateValuesPair> entry : regionValues) {
			if (nonOptimisingRegions.contains(entry.getKey())) {
				continue;
			}
			double upperProbBound = (Double) entry.getValue().getMax().getValue(initState);
			double lowerProbBound = (Double) entry.getValue().getMin().getValue(initState);
			if (upperProbBound + Epsilon < maximalLowerBound) {
				nonOptimisingRegions.add(entry.getKey(), "upper prob bound = " + upperProbBound);
				nonOptimisingRegions.add(entry.getKey(), "lower prob bound = " + lowerProbBound);
			} else {
				optimisingRegions.add(entry.getKey(), "upper prob bound = " + upperProbBound);
				optimisingRegions.add(entry.getKey(), "lower prob bound = " + lowerProbBound);
			}
		}
	}

	@Override
	public String toString()
	{
		return "max synthesis";
	}
}
