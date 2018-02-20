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
 * Class representing a double gene
 * @author sgerasimou
 *
 */
public class DoubleGene extends AbstractGene{
	
	/**
	 * Class constructor
	 * @param name
	 * @param minValue
	 * @param maxValue
	 */
	public DoubleGene(String name, double minValue, double maxValue){
		super(name, minValue, maxValue, 0.0);
	}
	
}
