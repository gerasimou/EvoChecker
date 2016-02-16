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

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import prism.PrismLog;

/**
 * A collection of {@link BoxRegion}s where each of them is associated
 * with a collection of string labels.
 */
@SuppressWarnings("serial")
final class LabelledBoxRegions extends HashMap<BoxRegion, Collection<String>> 
{
	public LabelledBoxRegions()
	{
		super();
	}

	public LabelledBoxRegions(BoxRegion region, String label)
	{
		super();
		add(region, label);
	}

	/**
	 * Adds a region {@code region} without a label.
	 * If {@code region} is already contained, no change occurs.
	 * 
	 * @param region region to add
	 * @return collection of labels associated with {@code region}
	 */
	public Collection<String> add(BoxRegion region)
	{
		return add(region, null);
	}

	/**
	 * Adds a region {@code region} labelled with {@code label}.
	 * If {@code region} is already contained, {@code label} is appended
	 * to the collection of labels associated with {@code region}.
	 * 
	 * @param region region to add
	 * @param label string to label {@code region}
	 * @return collection of labels associated with {@code region}
	 * (including {@code label})
	 */
	public Collection<String> add(BoxRegion region, String label)
	{
		if (!containsKey(region)) {
			put(region, new LinkedList<String>());
		}
		Collection<String> labels = get(region);
		if (label != null) {
			labels.add(label);
		}
		return labels;
	}

	/**
	 * Determines whether {@code region} is contained.
	 * 
	 * @param region region to look for
	 * @return true iff {@code region} is contained
	 */
	public boolean contains(BoxRegion region)
	{
		return containsKey(region);
	}

	/**
	 * Pretty-prints the stored box regions with their respective labels.
	 * 
	 * @param log file into which to print
	 */
	public void print(PrismLog log)
	{
		if (isEmpty()) {
			log.println(" * [none]");
		} else {
			for (Map.Entry<BoxRegion, Collection<String>> entry : entrySet()) {
				log.print(" * " + entry.getKey());
				if (!entry.getValue().isEmpty()) {
					log.print(" " + entry.getValue());
				}
				log.println();
			}
		}
	}
}
