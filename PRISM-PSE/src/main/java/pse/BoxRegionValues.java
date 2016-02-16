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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import parser.type.TypeDouble;
import prism.Pair;
import prism.PrismException;
import prism.PrismLog;
import explicit.StateValues;

/**
 * Map from a parameter region to a pair of minimised & maximised
 * state values.
 * 
 * @see BoxRegion
 * @see StateValues
 * @see StateValuesPair
 */
@SuppressWarnings("serial")
public class BoxRegionValues extends TreeMap<BoxRegion, BoxRegionValues.StateValuesPair> implements Iterable<Entry<BoxRegion, BoxRegionValues.StateValuesPair>>
{
	/** for {@link StateValues}' constructor */
	private PSEModel model;

	/**
	 * Data structure holding a pair of {@link StateValues}. The pair's
	 * components are stored in such a manner that the first is the result
	 * of minimising computations, while the second of maximising.
	 */
	public static class StateValuesPair extends Pair<StateValues, StateValues>
	{
		public StateValuesPair(StateValues min, StateValues max)
		{
			super(min, max);
		}

		public StateValues getMin()
		{
			return first;
		}

		public StateValues getMax()
		{
			return second;
		}

		public void swap()
		{
			StateValues tmpFirst = first; 
			first = second;
			second = tmpFirst;
		}
	}

	// FIXME: The following two constructors don't set the property `model`,
	// which some of the methods require for correct operation.
	public BoxRegionValues()
	{
		super();
	}

	public BoxRegionValues(BoxRegion region, StateValues minValues, StateValues maxValues)
	{
		this();
		put(region, minValues, maxValues);
	}

	public BoxRegionValues(PSEModel model)
	{
		super();
		this.model = model;
	}

	public BoxRegionValues(PSEModel model, BoxRegion region, StateValues minValues, StateValues maxValues)
	{
		this(model);
		put(region, minValues, maxValues);
	}

	public BoxRegionValues(PSEModel model, BoxRegion region, double[] minValues, double[] maxValues)
	{
		this(model);
		put(region, minValues, maxValues);
	}

	/**
	 * Creates region values with a single region, in which both the minimised and
	 * the maximised values are set to 1.0 for all states.
	 * 
	 * @param model model for obtaining information about states
	 * @param region region to populate with ones
	 * @return region values with all ones
	 * @throws PrismException in case the all-ones state values could not be created
	 */
	public static BoxRegionValues createWithAllOnes(PSEModel model, BoxRegion region) throws PrismException
	{
		StateValues ones = new StateValues(TypeDouble.getInstance(), new Double(1.0), model);
		return new BoxRegionValues(model, region, ones, ones.deepCopy());
	}

	/**
	 * Returns true if and only if this region values object contains only 1.0,
	 * i.e., for all regions, for both minimised & maximised, for all states,
	 * the value must be equal to 1.0.
	 * 
	 * @return true iff these region values consist exclusively of 1.0
	 * @throws PrismException in case the all-ones state values could not be created
	 */
	public boolean isAllOnes() throws PrismException
	{
		double[] onesD = new StateValues(TypeDouble.getInstance(), new Double(1.0), model).getDoubleArray();
		for (BoxRegionValues.StateValuesPair valuesPair : values()) {
			if (!Arrays.equals(valuesPair.getMin().getDoubleArray(), onesD)) {
				return false;
			}
			if (!Arrays.equals(valuesPair.getMax().getDoubleArray(), onesD)) {
				return false;
			}
		}
		return true;
	}

	public StateValuesPair put(BoxRegion region) throws PrismException
	{
		// `empty` is all zeros, actually.
		StateValues empty = new StateValues(TypeDouble.getInstance(), model);
		return put(region, empty, empty.deepCopy());
	}

	public StateValuesPair put(BoxRegion region, StateValues minValues, StateValues maxValues)
	{
		return put(region, new StateValuesPair(minValues, maxValues));
	}

	public StateValuesPair put(BoxRegion region, double[] min, double[] max)
	{
		StateValues minValues = StateValues.createFromDoubleArray(min, model);
		StateValues maxValues = StateValues.createFromDoubleArray(max, model);
		return put(region, minValues, maxValues);
	}

	public StateValuesPair put(BoxRegion region, BitSet min, BitSet max)
	{
		StateValues minValues = StateValues.createFromBitSet(min, model);
		StateValues maxValues = StateValues.createFromBitSet(max, model);
		return put(region, minValues, maxValues);
	}

	/**
	 * Replaces {@code region} with its subregions, retaining the {@link StateValuesPair}
	 * object associated with {@code region}.
	 * 
	 * @param region region to decompose and replace with its subregions
	 * @see BoxRegion#decompose()
	 */
	public void decomposeRegion(BoxRegion region)
	{
		StateValuesPair oldValuesPair = remove(region);
		for (BoxRegion subregion : region.decompose()) {
			put(subregion, oldValuesPair);
		}
	}

	public boolean hasRegion(BoxRegion region)
	{
		return containsKey(region);
	}

	public StateValues getMin(BoxRegion region)
	{
		return get(region).getMin();
	}

	public StateValues getMax(BoxRegion region)
	{
		return get(region).getMax();
	}

	public int getNumRegions()
	{
		return keySet().size();
	}

	@Override
	public Iterator<Entry<BoxRegion, BoxRegionValues.StateValuesPair>> iterator()
	{
		return entrySet().iterator();
	}

	/**
	 * Pretty-prints the region values.
	 * 
	 * @param log file into which to print
	 */
	public void print(PrismLog log)
	{
		print(log, true, false, true, true);
	}

	/**
	 */
	public void print(PrismLog log, boolean printSparse, boolean printMatlab, boolean printStates, boolean printIndices)
	{
		for (Entry<BoxRegion, BoxRegionValues.StateValuesPair> entry : entrySet()) {
			log.println("\n== Region " + entry.getKey() + " ==");
			log.println("\n=== Minimised state values ===\n");
			entry.getValue().getMin().print(log, printSparse, printMatlab, printStates, printIndices);
			log.println("\n=== Maximised state values ===\n");
			entry.getValue().getMax().print(log, printSparse, printMatlab, printStates, printIndices);
		}
	}

	/**
	 */
	public void readFromFile(File file) throws PrismException
	{
		int count = 0;
		int lineNum = 0;

		boolean firstParsed = false;
		boolean canStartMin = false;
		boolean canStartMax = false;
		boolean processingMin = true;   // false implies processing max
		boolean valuesFilled = true;

		BoxRegion currentRegion = null;
		Pattern regionPattern = Pattern.compile("== Region (.+) ==");
		Pattern minPattern = Pattern.compile("=== Minimised state values ===");
		Pattern maxPattern = Pattern.compile("=== Maximised state values ===");
		Matcher m;

		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			while (in.ready()) {
				String s = in.readLine().trim();
				lineNum++;
				if (!("".equals(s))) {
					if (!firstParsed) {
						firstParsed = true;
						// We could match with regionPattern here, the following, however,
						// is faster and captures even possible malformed input.
						if (s.charAt(0) != '=') {
							// Below, just delegating to standard StateValues.readFromFile().
							in.close();
							StateValues dist = new StateValues(TypeDouble.getInstance(), model);
							dist.readFromFile(file);
							put(model.getCompleteSpace(), dist, dist);
							return;
						}
					}

					m = regionPattern.matcher(s);
					if (m.find()) {
						if (!valuesFilled || canStartMin || canStartMax) {
							in.close();
							throw new PrismException("Cannot start new region " +
													 "at line " + lineNum + " of file \"" + file + "\"");
						}
						currentRegion = BoxRegion.fromString(m.group(1));
						put(currentRegion);
						canStartMin = true;
						canStartMax = true;
						continue;
					}

					m = minPattern.matcher(s);
					if (m.find()) {
						if (!canStartMin || !valuesFilled) {
							in.close();
							throw new PrismException("Cannot start new minimised values " +
													 "at line " + lineNum + " of file \"" + file + "\"");
						}
						canStartMin = false;
						processingMin = true;
						valuesFilled = false;
						count = 0;
						continue;
					}

					m = maxPattern.matcher(s);
					if (m.find()) {
						if (!canStartMax || !valuesFilled) {
							in.close();
							throw new PrismException("Cannot start new maximised values " +
													 "at line " + lineNum + " of file \"" + file + "\"");
						}
						canStartMax = false;
						processingMin = false;
						valuesFilled = false;
						count = 0;
						continue;
					}

					// An error is triggered from within readElementFromFile() if valuesFilled.
					if (processingMin) {
						getMin(currentRegion).readElementFromFile(file, s, count);
						count++;
					} else {
						getMax(currentRegion).readElementFromFile(file, s, count);
						count++;
					}

					assert getMin(currentRegion).getSize() == getMax(currentRegion).getSize();
					if (count == getMin(currentRegion).getSize()) {
						valuesFilled = true;
					}
				}
			}
			in.close();
			if (!valuesFilled) {
				throw new PrismException("Too few values in file \"" + file + "\"");
			}
		} catch (IOException e) {
			throw new PrismException("File I/O error reading from \"" + file + "\"");
		} catch (NumberFormatException e) {
			throw new PrismException("Error detected at line " + lineNum + " of file \"" + file + "\"");
		}
	}

	// TODO to facilitate garbage-collecting
	//public void clear()
}
