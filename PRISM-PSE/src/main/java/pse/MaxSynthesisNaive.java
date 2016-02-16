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

/**
 * Decomposition procedure solving the max synthesis problem using
 * the naive approach to determine the demarcation probability bound.
 */
public final class MaxSynthesisNaive extends MaxSynthesis
{
	public MaxSynthesisNaive(double probTolerance)
	{
		super(probTolerance);
	}

	/**
	 * Naive approach to determining the maximal lower bound.
	 */
	@Override
	protected double getMaximalLowerBound(BoxRegionValues regionValues)
	{
		double maximalLowerBound = Double.NEGATIVE_INFINITY;
		for (Entry<BoxRegion, BoxRegionValues.StateValuesPair> entry : regionValues) {
			double currentLowerBound = (Double) entry.getValue().getMin().getValue(initState);
			if (currentLowerBound > maximalLowerBound) {
				maximalLowerBound = currentLowerBound;
			}
		}
		return maximalLowerBound;
	}

	@Override
	public String toString()
	{
		return super.toString() + " (naive)";
	}
}
