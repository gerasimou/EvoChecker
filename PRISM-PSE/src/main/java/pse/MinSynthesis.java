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
 * Decomposition procedure solving the min synthesis problem, with
 * two approaches to computing the demarcation probability bound: <ul>
 * <li>naive approach, implemented as {@link MinSynthesisNaive};
 * <li>sampling-based approach, implemented as {@link MinSynthesisSampling}.
 * </ul>
 */
abstract class MinSynthesis extends OptimisingSynthesis
{
	public MinSynthesis(double probTolerance)
	{
		super(probTolerance);
		captionForOptimising = "minimising";
	}

	protected abstract double getMinimalUpperBound(BoxRegionValues regionValues) throws PrismException;

	@Override
	protected void determineOptimisingRegions(BoxRegionValues regionValues) throws PrismException
	{
		// Determine the minimal upper bound
		double minimalUpperBound = getMinimalUpperBound(regionValues);
		demarcationProbBounds.add(minimalUpperBound);
		
		// Determine the (non-)minimising regions
		optimisingRegions.clear();
		for (Entry<BoxRegion, BoxRegionValues.StateValuesPair> entry : regionValues) {
			if (nonOptimisingRegions.contains(entry.getKey())) {
				continue;
			}
			double lowerProbBound = (Double) entry.getValue().getMin().getValue(initState);
			double upperProbBound = (Double) entry.getValue().getMax().getValue(initState);
			if (lowerProbBound + Epsilon > minimalUpperBound) {
				nonOptimisingRegions.add(entry.getKey(), "lower prob bound = " + lowerProbBound);
				nonOptimisingRegions.add(entry.getKey(), "upper prob bound = " + upperProbBound);
			} else {
				optimisingRegions.add(entry.getKey(), "lower prob bound = " + lowerProbBound);
				optimisingRegions.add(entry.getKey(), "upper prob bound = " + upperProbBound);
			}
		}
	}

	@Override
	public String toString()
	{
		return "min synthesis";
	}
}
