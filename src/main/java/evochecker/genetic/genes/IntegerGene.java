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

package evochecker.genetic.genes;

/**
 * Class representing an integer gene
 * @author sgerasimou
 *
 */
public class IntegerGene extends AbstractGene{
	
	/**
	 * Class constructor
	 * @param name
	 * @param minValue
	 * @param maxValue
	 */
	public IntegerGene(String name, int minValue, int maxValue){
		super(name, minValue, maxValue, (minValue+maxValue)/2);
	}
}
