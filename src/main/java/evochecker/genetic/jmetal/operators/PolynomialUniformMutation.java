package evochecker.genetic.jmetal.operators;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import evochecker.genetic.jmetal.encoding.ArrayRealIntSolutionType;
import evochecker.genetic.jmetal.encoding.XInt;
import evochecker.genetic.jmetal.encoding.XReal;
import jmetal.core.Solution;
import jmetal.operators.mutation.Mutation;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

public class PolynomialUniformMutation extends Mutation {
	private static final double ETA_M_DEFAULT_ = 20.0;
	private final double eta_m_ = ETA_M_DEFAULT_;

	private Double realMutationProbability_ = null;
	private Double intMutationProbability_ = null;
	private double distributionIndex_ = eta_m_;

	/**
	 * Valid solution types to apply this operator
	 */
	private static final List VALID_TYPES = Arrays
			.asList(ArrayRealIntSolutionType.class);

	/**
	 * Constructor
	 */
	public PolynomialUniformMutation(HashMap<String, Object> parameters) {
		super(parameters);
		if (parameters.get("realMutationProbability") != null)
			realMutationProbability_ = (Double) parameters
					.get("realMutationProbability");
		if (parameters.get("intMutationProbability") != null)
			intMutationProbability_ = (Double) parameters
					.get("intMutationProbability");
		if (parameters.get("distributionIndex") != null)
			distributionIndex_ = (Double) parameters.get("distributionIndex");
	} // PolynomialBitFlipMutation

	@Override
	public Object execute(Object object) throws JMException {
		Solution solution = (Solution) object;

		if (!VALID_TYPES.contains(solution.getType().getClass())) {
			Configuration.logger_
					.severe("PolynomialBitFlipMutation.execute: the solution "
							+ "type " + solution.getType()
							+ " is not allowed with this operator");

			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".execute()");
		} // if

		doMutation(realMutationProbability_, intMutationProbability_, solution);
		return solution;
	} // execute

	public void doMutation(Double realProbability, Double intProbability,
			Solution solution) throws JMException {
		double rnd, delta1, delta2, mut_pow, deltaq;
		double y, yl, yu, val, xy;

		XReal xReal = new XReal(solution);
		XInt xInt = new XInt(solution);

		// Polynomial mutation applied to the array real
		for (int var = 0; var < xReal.getNumberOfDecisionVariables(); var++) {
			if (PseudoRandom.randDouble() <= realProbability) {
				y = xReal.getValue(var);
				yl = xReal.getLowerBound(var);
				yu = xReal.getUpperBound(var);
				delta1 = (y - yl) / (yu - yl);
				delta2 = (yu - y) / (yu - yl);
				rnd = PseudoRandom.randDouble();
				mut_pow = 1.0 / (eta_m_ + 1.0);
				if (rnd <= 0.5) {
					xy = 1.0 - delta1;
					val = 2.0 * rnd + (1.0 - 2.0 * rnd)
							* (Math.pow(xy, (distributionIndex_ + 1.0)));
					deltaq = java.lang.Math.pow(val, mut_pow) - 1.0;
				} else {
					xy = 1.0 - delta2;
					val = 2.0
							* (1.0 - rnd)
							+ 2.0
							* (rnd - 0.5)
							* (java.lang.Math.pow(xy,
									(distributionIndex_ + 1.0)));
					deltaq = 1.0 - (java.lang.Math.pow(val, mut_pow));
				}
				y = y + deltaq * (yu - yl);
				if (y < yl)
					y = yl;
				if (y > yu)
					y = yu;
				xReal.setValue(var, y);
			} // if
		} // for

		// Int mutation....
		if (PseudoRandom.randDouble() <= intProbability) {
			int location = PseudoRandom.randInt(0,
					xInt.getNumberOfDecisionVariables()-1);
			int newValue = PseudoRandom.randInt(xInt.getLowerBound(location),
					xInt.getUpperBound(location));
			xInt.setValue(location, newValue);
		}

	} // doMutation
}
