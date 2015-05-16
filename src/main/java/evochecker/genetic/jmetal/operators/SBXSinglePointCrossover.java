//  SBXSinglePointCrossover.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
// 
//  Copyright (c) 2011 Antonio J. Nebro
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package evochecker.genetic.jmetal.operators;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import evochecker.genetic.jmetal.encoding.ArrayRealIntSolutionType;
import evochecker.genetic.jmetal.encoding.XInt;
import evochecker.genetic.jmetal.encoding.XReal;
import jmetal.core.Solution;
import jmetal.operators.crossover.Crossover;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

public class SBXSinglePointCrossover extends Crossover {
	/**
	 * EPS defines the minimum difference allowed between real values
	 */
	private static final double EPS = 1.0e-14;

	private static final double ETA_C_DEFAULT_ = 20.0;
	private Double realCrossoverProbability_ = null;
	private Double intCrossoverProbability_ = null;
	private double distributionIndex_ = ETA_C_DEFAULT_;

	/**
	 * Valid solution types to apply this operator
	 */
	private static final List VALID_TYPES = Arrays
			.asList(ArrayRealIntSolutionType.class);

	/**
	 * Constructor
	 */
	public SBXSinglePointCrossover(HashMap<String, Object> parameters) {
		super(parameters);

		if (parameters.get("realCrossoverProbability") != null)
			realCrossoverProbability_ = (Double) parameters
					.get("realCrossoverProbability");
		if (parameters.get("intCrossoverProbability") != null)
			intCrossoverProbability_ = (Double) parameters
					.get("intCrossoverProbability");
		if (parameters.get("distributionIndex") != null)
			distributionIndex_ = (Double) parameters.get("distributionIndex");
	} // Constructor

	/**
	 * Perform the crossover operation.
	 * 
	 * @param realProbability
	 *            Crossover probability
	 * @param parent1
	 *            The first parent
	 * @param parent2
	 *            The second parent
	 * @return An array containing the two offsprings
	 */
	public Solution[] doCrossover(Double realProbability,
			Double intProbability, Solution parent1, Solution parent2)
			throws JMException {

		Solution[] offSpring = new Solution[2];

		offSpring[0] = new Solution(parent1);
		offSpring[1] = new Solution(parent2);

		// SBX crossover
		double rand;
		double y1, y2, yL, yu;
		double c1, c2;
		double alpha, beta, betaq;
		double valueX1R, valueX2R;
		int valueX1I, valueX2I;

		// Real part
		XReal x1R = new XReal(parent1);
		XReal x2R = new XReal(parent2);
		XReal offs1R = new XReal(offSpring[0]);
		XReal offs2R = new XReal(offSpring[1]);

		// Int part
		XInt x1I = new XInt(parent1);
		XInt x2I = new XInt(parent2);
		XInt offs1I = new XInt(offSpring[0]);
		XInt offs2I = new XInt(offSpring[1]);

		// SBX for real part
		if (PseudoRandom.randDouble() <= realProbability) {
			for (int i = 0; i < x1R.getNumberOfDecisionVariables(); i++) {
				valueX1R = x1R.getValue(i);
				valueX2R = x2R.getValue(i);
				if (PseudoRandom.randDouble() <= 0.5) {
					if (java.lang.Math.abs(valueX1R - valueX2R) > EPS) {

						if (valueX1R < valueX2R) {
							y1 = valueX1R;
							y2 = valueX2R;
						} else {
							y1 = valueX2R;
							y2 = valueX1R;
						} // if

						yL = x1R.getLowerBound(i);
						yu = x1R.getUpperBound(i);
						rand = PseudoRandom.randDouble();
						beta = 1.0 + (2.0 * (y1 - yL) / (y2 - y1));
						alpha = 2.0 - java.lang.Math.pow(beta,
								-(distributionIndex_ + 1.0));

						if (rand <= (1.0 / alpha)) {
							betaq = java.lang.Math.pow((rand * alpha),
									(1.0 / (distributionIndex_ + 1.0)));
						} else {
							betaq = java.lang.Math.pow((1.0 / (2.0 - rand
									* alpha)),
									(1.0 / (distributionIndex_ + 1.0)));
						} // if

						c1 = 0.5 * ((y1 + y2) - betaq * (y2 - y1));
						beta = 1.0 + (2.0 * (yu - y2) / (y2 - y1));
						alpha = 2.0 - java.lang.Math.pow(beta,
								-(distributionIndex_ + 1.0));

						if (rand <= (1.0 / alpha)) {
							betaq = java.lang.Math.pow((rand * alpha),
									(1.0 / (distributionIndex_ + 1.0)));
						} else {
							betaq = java.lang.Math.pow((1.0 / (2.0 - rand
									* alpha)),
									(1.0 / (distributionIndex_ + 1.0)));
						} // if

						c2 = 0.5 * ((y1 + y2) + betaq * (y2 - y1));

						if (c1 < yL)
							c1 = yL;

						if (c2 < yL)
							c2 = yL;

						if (c1 > yu)
							c1 = yu;

						if (c2 > yu)
							c2 = yu;

						if (PseudoRandom.randDouble() <= 0.5) {
							offs1R.setValue(i, c2);
							offs2R.setValue(i, c1);
						} else {
							offs1R.setValue(i, c1);
							offs2R.setValue(i, c2);
						} // if
					} // if
					else {
						offs1R.setValue(i, valueX1R);
						offs2R.setValue(i, valueX2R);
					} // if
				} // if
				else {
					offs1R.setValue(i, valueX2R);
					offs2R.setValue(i, valueX1R);
				} // else
			} // for
		} // if

		// Single point crossover for integer part
		if (PseudoRandom.randDouble() <= intProbability) {
			int crossoverPoint = PseudoRandom.randInt(0,
					x1I.getNumberOfDecisionVariables() - 1);
			for (int i = crossoverPoint; i < x1I.getNumberOfDecisionVariables(); i++) {
				valueX1I = (int) x1I.getValue(i);
				valueX2I = (int) x2I.getValue(i);
				offs1I.setValue(i, valueX2I);
				offs2I.setValue(i, valueX1I);
			} // for
		} // if

		return offSpring;
	} // doCrossover

	@Override
	public Object execute(Object object) throws JMException {
		Solution[] parents = (Solution[]) object;

		if (parents.length != 2) {
			Configuration.logger_
					.severe("SBXSinglePointCrossover.execute: operator "
							+ "needs two parents");
			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".execute()");
		} // if

		if (!(VALID_TYPES.contains(parents[0].getType().getClass()) && VALID_TYPES
				.contains(parents[1].getType().getClass()))) {
			Configuration.logger_
					.severe("SBXSinglePointCrossover.execute: the solutions "
							+ "type " + parents[0].getType()
							+ " is not allowed with this operator");

			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".execute()");
		} // if
		Solution[] offSpring;
		offSpring = doCrossover(realCrossoverProbability_,
				intCrossoverProbability_, parents[0], parents[1]);

		return offSpring;
	} // execute

} // SBXSinglePointCrossover

