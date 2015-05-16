//  ArrayReal.java
package evochecker.genetic.jmetal.encoding;

import jmetal.core.Problem;
import jmetal.core.Variable;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

/**
 * Class implementing a decision encodings.variable representing an array of
 * real values. The real values of the array have their own bounds.
 */
public class ArrayReal extends Variable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1875456131471940297L;

	/**
	 * Problem using the type
	 */
	private Problem problem_;

	/**
	 * Stores an array of real values
	 */
	public Double[] array_;

	/**
	 * Stores the length of the array
	 */
	private int size_;

	/**
	 * Store the lower and upper bounds of each value of the array in case of
	 * having each one different limits
	 */
	private double[] lowerBounds_;
	private double[] upperBounds_;

	/**
	 * Constructor
	 */
	public ArrayReal() {
		problem_ = null;
		size_ = 0;
		array_ = null;
		lowerBounds_ = null;
		upperBounds_ = null;
	} // Constructor

	public ArrayReal(int size, Problem problem, double[] lowerBounds,
			double[] upperBounds) {
		problem_ = problem;
		size_ = size;
		array_ = new Double[size_];

		lowerBounds_ = new double[size_];
		upperBounds_ = new double[size_];

		for (int i = 0; i < size_; i++) {
			lowerBounds_[i] = lowerBounds[i];
			upperBounds_[i] = upperBounds[i];
			array_[i] = PseudoRandom.randDouble(lowerBounds_[i],
					upperBounds_[i]);
		}

	} // Constructor

	/**
	 * Constructor
	 *
	 * @param size
	 *            Size of the array
	 */
	public ArrayReal(int size, Problem problem) {
		problem_ = problem;
		size_ = size;
		array_ = new Double[size_];

		for (int i = 0; i < size_; i++) {
			array_[i] = PseudoRandom.randDouble()
					* (problem_.getUpperLimit(i) - problem_.getLowerLimit(i))
					+ problem_.getLowerLimit(i);
		} // for
	} // Constructor

	/**
	 * Copy Constructor
	 *
	 * @param arrayReal
	 *            The arrayReal to copy
	 */
	private ArrayReal(ArrayReal arrayReal) {
		problem_ = arrayReal.problem_;
		size_ = arrayReal.size_;
		array_ = new Double[size_];
		upperBounds_ = new double[size_];
		lowerBounds_ = new double[size_];

		for (int i = 0; i < size_; i++) {
			array_[i] = arrayReal.array_[i];
			lowerBounds_[i] = arrayReal.lowerBounds_[i];
			upperBounds_[i] = arrayReal.upperBounds_[i];
		} // for

	} // Copy Constructor

	@Override
	public Variable deepCopy() {
		return new ArrayReal(this);
	} // deepCopy

	/**
	 * Returns the length of the arrayReal.
	 *
	 * @return The length
	 */
	public int getLength() {
		return size_;
	} // getLength

	/**
	 * getValue
	 *
	 * @param index
	 *            Index of value to be returned
	 * @return the value in position index
	 */
	public double getValue(int index) throws JMException {
		if ((index >= 0) && (index < size_))
			return array_[index];
		else {
			Configuration.logger_
					.severe(jmetal.encodings.variable.ArrayReal.class
							+ ".getValue(): index value (" + index
							+ ") invalid");
			throw new JMException(jmetal.encodings.variable.ArrayReal.class
					+ ".ArrayReal: index value (" + index + ") invalid");
		} // if
	} // getValue

	/**
	 * setValue
	 *
	 * @param index
	 *            Index of value to be returned
	 * @param value
	 *            The value to be set in position index
	 */
	public void setValue(int index, double value) throws JMException {
		if ((index >= 0) && (index < size_))
			array_[index] = value;
		else {
			Configuration.logger_
					.severe(jmetal.encodings.variable.ArrayReal.class
							+ ".setValue(): index value (" + index
							+ ") invalid");
			throw new JMException(jmetal.encodings.variable.ArrayReal.class
					+ ": index value (" + index + ") invalid");
		} // else
	} // setValue

	/**
	 * Get the lower bound of a value
	 *
	 * @param index
	 *            The index of the value
	 * @return the lower bound
	 */
	public double getLowerBound(int index) throws JMException {
		if ((index >= 0) && (index < size_))
			return this.lowerBounds_[index];
		else {
			Configuration.logger_
					.severe(jmetal.encodings.variable.ArrayReal.class
							+ ".getLowerBound(): index value (" + index
							+ ") invalid");
			throw new JMException(jmetal.encodings.variable.ArrayReal.class
					+ ".getLowerBound: index value (" + index + ") invalid");
		} // else
	} // getLowerBound

	/**
	 * Get the upper bound of a value
	 *
	 * @param index
	 *            The index of the value
	 * @return the upper bound
	 */
	public double getUpperBound(int index) throws JMException {
		if ((index >= 0) && (index < size_))
			return this.upperBounds_[index];
		else {
			Configuration.logger_
					.severe(jmetal.encodings.variable.ArrayReal.class
							+ ".getUpperBound(): index value (" + index
							+ ") invalid");
			throw new JMException(jmetal.encodings.variable.ArrayReal.class
					+ ".getUpperBound: index value (" + index + ") invalid");
		} // else
	} // getLowerBound

	/**
	 * Returns a string representing the object
	 *
	 * @return The string
	 */
	
	//TODO: If there is no double(real) evolvable element in the model, an exception is thrown
	public String toString() {
		if (size_ <= 0)
			return "";
		String string;

		string = "";
		for (int i = 0; i < (size_ - 1); i++)
			string += array_[i] + " ";

		string += array_[size_ - 1];
		return string;
	} // toString
} // ArrayReal
