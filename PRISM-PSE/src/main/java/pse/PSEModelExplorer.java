//==============================================================================
//	
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import explicit.ModelExplorer;
import param.SymbolicEngine;
import param.TransitionList;
import parser.State;
import parser.Values;
import parser.ast.Expression;
import parser.ast.ExpressionConstant;
import parser.ast.ModulesFile;
import parser.visitor.ASTTraverse;
import prism.Pair;
import prism.PrismException;

/**
 * Class providing means for exploration of structures arising from modules
 * files, i.e. state space and action-annotated transition graph with rates.
 * It is useful for separate prior model building (as in {@link PSEModelBuilder})
 * as well as for on-the-fly querying of model data (as in FAU).
 */
public final class PSEModelExplorer implements ModelExplorer<Expression>
{
	/** complete parameter space */
	private BoxRegion completeSpace;
	/** symbolic engine providing essential methods */
	private SymbolicEngine engine;
	/** last queried state */
	private State currentState;
	/** transition list of last queried state */
	private TransitionList transitionList;
	/** map from rate expression to respective rate parameters and population,
	 *  the latter having been extracted from the former */
	private Map<Expression, RateParametersAndPopulation> rateDataCache = new HashMap<Expression, RateParametersAndPopulation>();

	/**
	 * Special data structure for holding a pair consisting of rate parameters
	 * and rate population.
	 */
	static class RateParametersAndPopulation extends Pair<Expression, Double>
	{
		RateParametersAndPopulation(Expression first, Double second)
		{
			super(first, second);
		}

		Expression getParameters()
		{
			return first;
		}

		double getPopulation()
		{
			return second;
		}
	}

	/**
	 * Constructor.
	 * 
	 * @param modulesFile modules file whose structures are to be explored
	 * @throws PrismException in case {@code modulesFile} cannot be processed
	 */
	public PSEModelExplorer(ModulesFile modulesFile) throws PrismException
	{
		modulesFile = (ModulesFile) modulesFile.deepCopy().replaceConstants(modulesFile.getConstantValues()).simplify();
		engine = new SymbolicEngine(modulesFile);
	}

	/**
	 * Sets parameter information.
	 * Obviously, all of {@code paramNames}, {@code lower}, {@code upper}
	 * must have the same length, and {@code lower} bounds of parameters must
	 * not be higher than {@code upper} bounds.
	 * 
	 * @param paramNames names of parameters
	 * @param lower lower bounds of parameters' ranges
	 * @param upper upper bounds of parameters' ranges
	 */
	public void setParameters(String[] paramNames, double[] lower, double[] upper)
	{
		Values lowerParams = new Values();
		Values upperParams = new Values();
		for (int i = 0; i < paramNames.length; i++) {
			lowerParams.addValue(paramNames[i], lower[i]);
			upperParams.addValue(paramNames[i], upper[i]);
		}
		completeSpace = new BoxRegion(lowerParams, upperParams);
	}

	/**
	 * Gets the complete parameter space.
	 * 
	 * @return complete parameter space
	 */
	public BoxRegion getCompleteSpace()
	{
		return completeSpace;
	}

	/**
	 * Extracts parameters and the transition origin's species population
	 * from the given rate expression.
	 * 
	 * @param rateExpression rate expression to extract parameters
	 * and population from
	 * @return special pair of rate parameters and population
	 * @throws PrismException in case of an error during handling of 
	 * of {@code rateExpression}
	 */
	RateParametersAndPopulation extractRateParametersAndPopulation(Expression rateExpression)
			throws PrismException
	{
		if (rateDataCache.containsKey(rateExpression)) {
			return rateDataCache.get(rateExpression);
		}

		final List<ExpressionConstant> containedParameters = new ArrayList<ExpressionConstant>();
		rateExpression.accept(new ASTTraverse()
		{
			// TODO: visit() for ExpressionLiteral to capture rate population directly.
			// Subsequently, the whole method could be made static and moved
			// into pse.RateUtils or something.
			public Object visit(ExpressionConstant e)
			{
				containedParameters.add(e);
				return null;
			}
		});

		Expression rateParameters = Expression.Double(1);
		for (ExpressionConstant parameterExpr : containedParameters) {
			rateParameters = Expression.Times(rateParameters, parameterExpr);
		}
		double ratePopulation = Expression.Divide(rateExpression, rateParameters).evaluateDouble(completeSpace.getUpperBounds());
		RateParametersAndPopulation result = new RateParametersAndPopulation(rateParameters, ratePopulation);
		rateDataCache.put(rateExpression, result);
		return result;
	}

	/**
	 * Generalisation of {@link #extractRateParametersAndPopulation(Expression)}
	 * for arrays of rate expressions, one output parameters/population pair
	 * per one input rate expression.
	 * 
	 * @param rateExpressions array of rate expressions
	 * @return array of special pairs of rate parameters and population
	 * @throws PrismException in case of an error during handling of 
	 * of a {@code rateExpressions} item
	 */
	RateParametersAndPopulation[] extractRateParametersAndPopulation(Expression[] rateExpressions) throws PrismException
	{
		if (rateExpressions == null) {
			return null;
		}
		int n = rateExpressions.length;
		RateParametersAndPopulation[] result = new RateParametersAndPopulation[n];
		for (int i = 0; i < n; i++) {
			result[i] = extractRateParametersAndPopulation(rateExpressions[i]);
		}
		return result;
	}

	protected ModulesFile getModulesFile()
	{
		return engine.getModulesFile();
	}

	@Override
	public State getDefaultInitialState() throws PrismException
	{
		return getModulesFile().getDefaultInitialState();
	}

	@Override
	public void queryState(State state) throws PrismException
	{
		currentState = state;
		transitionList = engine.calculateTransitions(state);
	}

	@Override
	public void queryState(State state, double time) throws PrismException
	{
		queryState(state);
	}

	@Override
	public int getNumChoices() throws PrismException
	{
		return transitionList.getNumChoices();
	}

	@Override
	public int getNumTransitions() throws PrismException
	{
		return transitionList.getNumTransitions();
	}

	@Override
	public int getNumTransitions(int choiceNr) throws PrismException
	{
		return transitionList.getChoice(choiceNr).size();
	}

	protected int getTotalIndexOfTransition(int choiceNr, int offset)
	{
		return transitionList.getTotalIndexOfTransition(choiceNr, offset);
	}

	@Override
	public String getTransitionAction(int choiceNr, int offset) throws PrismException
	{
		int a = transitionList.getTransitionModuleOrActionIndex(getTotalIndexOfTransition(choiceNr, offset));
		return a < 0 ? null : getModulesFile().getSynch(a - 1);
	}

	@Override
	public String getTransitionAction(int succNr) throws PrismException
	{
		int a = transitionList.getTransitionModuleOrActionIndex(succNr);
		return a < 0 ? null : getModulesFile().getSynch(a - 1);
	}

	@Override
	public Expression getTransitionProbability(int choiceNr, int offset) throws PrismException
	{
		return getTransitionProbability(getTotalIndexOfTransition(choiceNr, offset));
	}

	@Override
	public Expression getTransitionProbability(int succNr) throws PrismException
	{
		return transitionList.getTransitionProbability(succNr);
	}

	@Override
	public State computeTransitionTarget(int choiceNr, int offset) throws PrismException
	{
		return computeTransitionTarget(getTotalIndexOfTransition(choiceNr, offset));
	}

	@Override
	public State computeTransitionTarget(int succNr) throws PrismException
	{
		return transitionList.computeTransitionTarget(succNr, currentState);
	}

	public int getReaction(int succNr)
	{
		return transitionList.getChoiceOfTransition(succNr).hashCode();
	}
	
}
