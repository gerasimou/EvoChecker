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
 * Abstract gene class
 * @author sgerasimou
 *
 */
public abstract class AbstractGene {
	
	/** Minimum value of this gene*/
	private Number minValue;
	
	/** Maximum value of this gene*/
	private Number maxValue;
	
	/** Name of this gene*/
	private String name;
	
	/** Actual value*/
	private Object allele;

	
	/**
	 * Gene class constructor
	 * @param name
	 * @param minValue
	 * @param maxValue
	 */
	public AbstractGene(String name, Number minValue, Number maxValue, Object allele){
		this.name 		= name;
		this.minValue 	= minValue;
		this.maxValue 	= maxValue;
		this.allele		= allele;
	}
	
	
	public String getName(){
		return this.name;
	}

	
	/** Get the minimum value 
	 * 
	 */
	public Number getMinValue() {
		return minValue;
	}

	
	/**
	 * Get the maximum value
	 * @return
	 */
	public Number getMaxValue() {
		return maxValue;
	}
	
	
	/**
	 * Get the actual value
	 * @return
	 */
	public Object getAllele(){
		return this.allele;
	}
	
	
	/**
	 * Set the value of this genes
	 * @param allele
	 */
	public void setAllele(Object allele){
		this.allele = allele;
	}
}
