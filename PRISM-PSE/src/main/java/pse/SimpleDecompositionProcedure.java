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

/**
 * Simple decomposition procedure that ensures that the minimised & maximised
 * probabilities are "close enough" to each other, i.e. accurate enough with
 * respect to the {@code accuracy} value.
 */
public final class SimpleDecompositionProcedure extends DecompositionProcedure
{
	/** greatest acceptable difference between maximised and minimised probability
	 *  of the same state */
	private double accuracy;

	/**
	 * Singleton decomposition procedure during which no decomposition occurs
	 * whatsoever.
	 */
	public static final class NoDecomposing extends DecompositionProcedure
	{
		private static final NoDecomposing INSTANCE = new NoDecomposing();

		private NoDecomposing() {}

		public static NoDecomposing getInstance()
		{
			return INSTANCE;
		}
	}

	public SimpleDecompositionProcedure(double accuracy)
	{
		this.accuracy = accuracy;
	}

	@Override
	protected void verifySingleRegion(BoxRegion region, double probsMin[], double probsMax[]) throws DecompositionNeeded
	{
		assert probsMin.length == probsMax.length;
		for (int state = 0; state < probsMin.length; state++) {
			if (probsMax[state] - probsMin[state] > accuracy) {
				throw new DecompositionNeeded("Significant inaccuracy was obtained " +
						"in state " + state  + ",\n" +
						probsMax[state] + " - " + probsMin[state] + " > " + accuracy,
						region, "inaccurate region");
			}
		}
	}
}
