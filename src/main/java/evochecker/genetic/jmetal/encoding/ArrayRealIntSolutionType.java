package evochecker.genetic.jmetal.encoding;

import java.util.Arrays;

import jmetal.core.Problem;
import jmetal.core.SolutionType;
import jmetal.core.Variable;

public class ArrayRealIntSolutionType extends SolutionType {

	private final int numberOfIntVariables_;
	private final int numberOfRealVariables_;
	private double[] lowerBounds, upperBounds;

	public ArrayRealIntSolutionType(Problem problem, int realVariables,
			int intVariables, double[] lowerBounds, double[] upperBounds) {
		super(problem);
		numberOfIntVariables_ = intVariables;
		numberOfRealVariables_ = realVariables;
		this.upperBounds = upperBounds;
		this.lowerBounds = lowerBounds;
	} // Constructor

	public Variable[] createVariables() throws ClassNotFoundException {
		Variable[] variables = new Variable[2];

		variables[0] = new ArrayReal(numberOfRealVariables_, problem_,
				Arrays.copyOfRange(lowerBounds, 0, numberOfRealVariables_),
				Arrays.copyOfRange(upperBounds, 0, numberOfRealVariables_));
		variables[1] = new ArrayInt(numberOfIntVariables_, problem_,
				Arrays.copyOfRange(lowerBounds, numberOfRealVariables_,
						numberOfRealVariables_ + numberOfIntVariables_),
				Arrays.copyOfRange(upperBounds, numberOfRealVariables_,
						numberOfRealVariables_ + numberOfIntVariables_));
		return variables;
	} // createVariables
} // ArrayRealAndBinarySolutionType

