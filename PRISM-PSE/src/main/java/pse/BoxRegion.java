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

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import parser.Values;
import explicit.Utils;

/**
 * Box (hyper-rectangle) variant of a parameter region.
 * Each parameter is associated with a lower and upper bound, and
 * the region contains all bounds so that the corresponding dimensions
 * are (not strictly) between these lower and upper bounds.
 * 
 * @see BoxRegionFactory
 */
public final class BoxRegion implements Comparable<BoxRegion>
{
	private Values lowerBounds;
	private Values upperBounds;
	/** volume of the region, not yet computed if zero,
	 *  the actual value is always greater than zero */
	private double volume = 0.0;

	/**
	 * Constructs a new box region.
	 * 
	 * @param lowerBounds region's lower bounds
	 * @param upperBounds region's upper bounds
	 */
	public BoxRegion(Values lowerBounds, Values upperBounds)
	{
		assert lowerBounds.compareTo(upperBounds) <= 0;
		this.lowerBounds = lowerBounds;
		this.upperBounds = upperBounds;
	}

	/**
	 * Creates a new box region with data parsed from string description
	 * given in the format reminiscent of {@link #toString()}.
	 */
	public static BoxRegion fromString(String regionDesc)
	{
		Values lowerBounds = new Values();
		Values upperBounds = new Values();
		for (String paramDesc : regionDesc.split(",")) {
			String[] paramDescParts = paramDesc.split("=|:");
			String paramName = paramDescParts[0].trim();
			lowerBounds.addValue(paramName, Double.parseDouble(paramDescParts[1].trim()));
			upperBounds.addValue(paramName, Double.parseDouble(paramDescParts[2].trim()));
		}
		return new BoxRegion(lowerBounds, upperBounds);
	}

	/**
	 * Gets the region's lower bounds.
	 * 
	 * @return region's lower bounds
	 */
	public Values getLowerBounds()
	{
		return lowerBounds;
	}

	/**
	 * Gets the region's upper bounds.
	 * 
	 * @return region's upper bounds
	 */
	public Values getUpperBounds()
	{
		return upperBounds;
	}

	/**
	 * Computes middle bounds by taking an average of the lower and upper
	 * bound of all the parameters' ranges.
	 * 
	 * @return middle bounds
	 */
	private Values computeMidBounds()
	{
		Values midBounds = new Values();
		for (int i = 0; i < lowerBounds.getNumValues(); i++) {
			double lowerValue = (Double) lowerBounds.getValue(i);
			double upperValue = (Double) upperBounds.getValue(i);
			midBounds.addValue(lowerBounds.getName(i), lowerValue + 0.5 * (upperValue - lowerValue));
		}
		return midBounds;
	}

	/**
	 * Decomposes the region into several mutually disjoint subregions
	 * (with the only exception of the subregions' upper/lower bounds
	 * that may be shared among multiple subregions).
	 * The subregions are also collectively exhaustive in the sense that
	 * their union is precisely the region that is being decomposed.
	 * The corresponding set-theoretical notion is a partition of a set.
	 * 
	 * @return set of subregions forming a decomposition of this region
	 */
	public Set<BoxRegion> decompose()
	{
		Set<BoxRegion> subregions = new HashSet<BoxRegion>();
		Values midBounds = computeMidBounds();
		Set<Integer> allIndices = new HashSet<Integer>();
		for (int i = 0; i < midBounds.getNumValues(); i++) {
			allIndices.add(i);
		}

		for (Set<Integer> indices : Utils.powerSet(allIndices)) {
			Values newLowerBounds = new Values();
			Values newUpperBounds = new Values();
			for (int i = 0; i < midBounds.getNumValues(); i++) {
				String name = midBounds.getName(i);
				double midValue = (Double) midBounds.getValue(i);
				if (indices.contains(i)) {
					newLowerBounds.addValue(name, (Double) lowerBounds.getValue(i));
					newUpperBounds.addValue(name, midValue);
				} else {
					newLowerBounds.addValue(name, midValue);
					newUpperBounds.addValue(name, (Double) upperBounds.getValue(i));
				}
			}
			subregions.add(new BoxRegion(newLowerBounds, newUpperBounds));
		}
		return subregions;
	}

	/**
	 * Returns volume of the region.
	 * (The value gets computed if not yet known.)
	 * 
	 * @return volume of the region
	 */
	public double volume()
	{
		if (volume > 0.0)
			return volume;

		volume = 1.0;
		for (int i = 0; i < lowerBounds.getNumValues(); i++) {
			double lowerValue = (Double) lowerBounds.getValue(i);
			double upperValue = (Double) upperBounds.getValue(i);
			if (lowerValue != upperValue) {
				volume *= upperValue - lowerValue;
			}
		}
		return volume;
	}

	/**
	 * Returns a set of two randomly generated sample points belonging
	 * to this region.
	 * 
	 * @return set of two randomly generated sample points of this region
	 * @see BoxRegion#generateSamplePoints(int)
	 */
	public Set<Point> generateSamplePoints()
	{
		return generateSamplePoints(2);
	}

	/**
	 * Returns a set of {@code numSamples} randomly generated sample points
	 * belonging to this region.
	 * 
	 * @return set of {@code numSamples} randomly generated sample points
	 * of this region
	 */
	public Set<Point> generateSamplePoints(int numSamples)
	{
		Set<Point> samples = new HashSet<Point>();
		Random r = new Random(hashCode());
		while (samples.size() != numSamples) {
			Values dimensions = new Values();
			for (int i = 0; i < lowerBounds.getNumValues(); i++) {
				double lowerValue = (Double) lowerBounds.getValue(i);
				double upperValue = (Double) upperBounds.getValue(i);
				double randomValue = lowerValue + r.nextDouble() * (upperValue - lowerValue);
				dimensions.addValue(lowerBounds.getName(i), randomValue);
			}
			samples.add(new Point(dimensions));
		}
		return samples;
	}

	@Override
	public int compareTo(BoxRegion r)
	{
		int lowerRes = lowerBounds.compareTo(r.lowerBounds);
		int upperRes = upperBounds.compareTo(r.upperBounds);
		if (lowerRes == upperRes)
			return lowerRes;

		int min = Math.min(lowerRes, upperRes);
		int max = Math.max(lowerRes, upperRes);
		if (min == 0)
			return max;
		if (max == 0)
			return min;
		return lowerRes;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((lowerBounds == null) ? 0 : lowerBounds.hashCode());
		result = prime * result
				+ ((upperBounds == null) ? 0 : upperBounds.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BoxRegion other = (BoxRegion) obj;
		if (lowerBounds == null) {
			if (other.lowerBounds != null)
				return false;
		} else if (!lowerBounds.equals(other.lowerBounds))
			return false;
		if (upperBounds == null) {
			if (other.upperBounds != null)
				return false;
		} else if (!upperBounds.equals(other.upperBounds))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < lowerBounds.getNumValues(); i++) {
			if (i != 0) builder.append(",");
			builder.append(lowerBounds.getName(i));
			builder.append("=");
			builder.append((Double) lowerBounds.getValue(i));
			builder.append(":");
			builder.append((Double) upperBounds.getValue(i));
		}
		return builder.toString();
	}
}
