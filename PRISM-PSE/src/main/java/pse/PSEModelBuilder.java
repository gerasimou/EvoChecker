//==============================================================================
//	
//	Copyright (c) 2013-
//	Authors:
//	* Ernst Moritz Hahn <emhahn@cs.ox.ac.uk> (University of Oxford)
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

import java.util.LinkedList;
import java.util.List;

import parser.State;
import parser.ast.Expression;
import parser.ast.ModulesFile;
import prism.ModelType;
import prism.PrismComponent;
import prism.PrismException;
import explicit.IndexedSet;
import explicit.StateStorage;

/**
 * Class to build PSE models, taking advantage
 * of {@link PSEModelExplorer} functionality.
 */
public final class PSEModelBuilder extends PrismComponent
{
	private PSEModel model;
	private PSEModelExplorer explorer;
	private State[] stateArray;


	public State[] getStateArray()
	{
		return stateArray;
	}

	/**
	 * Constructor.
	 */
	public PSEModelBuilder(PrismComponent parent, PSEModelExplorer explorer) throws PrismException
	{
		super(parent);
		this.explorer = explorer;
	}

	/**
	 * Builds PSE model with all necessities included.
	 * 
	 * @throws PrismException in case the model cannot be constructed
	 */
	public void build() throws PrismException
	{
		long time;

		mainLog.print("\nBuilding model...\n");

		time = System.currentTimeMillis();
		ModulesFile modulesFile = (ModulesFile) explorer.getModulesFile();
		model = constructModel(modulesFile);
		model.setParameterSpace(explorer.getCompleteSpace());
		time = System.currentTimeMillis() - time;

		mainLog.println("\nTime for model construction: " + time / 1000.0 + " seconds.");
		mainLog.flush();
	}

	/**
	 * Returns the constructed PSE model.
	 * 
	 * @return constructed PSE model
	 */
	public PSEModel getModel()
	{
		return model;
	}

	/**
	 * Reserves memory needed for parametric model and reserves necessary space.
	 * Afterwards, transition probabilities etc. can be added. 
	 */
	private void reserveMemoryAndExploreStates(PSEModel model, StateStorage<State> states)
			throws PrismException
	{
		int numStates = 0;
		int numTotalSuccessors = 0;

		LinkedList<State> explore = new LinkedList<State>();

		State state = explorer.getDefaultInitialState();
		states.add(state);
		explore.add(state);
		numStates++;

		while (!explore.isEmpty()) {
			state = explore.removeFirst();
			explorer.queryState(state);
			int numChoices = explorer.getNumChoices();
			for (int choiceNr = 0; choiceNr < numChoices; choiceNr++) {
				// CTMCs should have only a single transition within a choice
				assert explorer.getNumTransitions(choiceNr) == 1;

				numTotalSuccessors += 1;
				State stateNew = explorer.computeTransitionTarget(choiceNr, 0);
				if (states.add(stateNew)) {
					numStates++;
					explore.add(stateNew);
				}
			}
		}

		model.reserveMem(numStates, numTotalSuccessors);
	}

	/**
	 * Constructs PSE model once modules file etc. has been prepared.
	 * 
	 * @param modulesFile modules file of which to construct PSE model
	 * @return PSE model constructed
	 * @throws PrismException thrown if model cannot be constructed
	 */
	private PSEModel constructModel(ModulesFile modulesFile) throws PrismException
	{
		ModelType modelType;
		PSEModel model;

		if (modulesFile.getInitialStates() != null) {
			throw new PrismException("Explicit model construction does not support multiple initial stateArray");
		}

		modelType = modulesFile.getModelType();
		if (modelType != ModelType.CTMC) {
			throw new PrismException("Unsupported model type: " + modelType);
		}

		mainLog.print("\nComputing reachable stateArray...");
		mainLog.flush();
		long timer = System.currentTimeMillis();

		model = new PSEModel();

		StateStorage<State> states = new IndexedSet<State>(true);
		reserveMemoryAndExploreStates(model, states);
		int[] permut = states.buildSortingPermutation();
		List<State> statesList = states.toPermutedArrayList(permut);
		model.setStatesList(statesList);
		model.addInitialState(permut[0]);
		for (State state : statesList) {
			explorer.queryState(state);
			int numChoices = explorer.getNumChoices();
			Expression sumOut = Expression.Double(0.0);
			for (int choiceNr = 0; choiceNr < numChoices; choiceNr++) {
				// CTMCs should have only a single transition within a choice
				assert explorer.getNumTransitions(choiceNr) == 1;

				int succNr = explorer.getTotalIndexOfTransition(choiceNr, 0);
				int reaction = explorer.getReaction(succNr);
				State stateNew = explorer.computeTransitionTarget(succNr);
				Expression rateExpr = explorer.getTransitionProbability(succNr);
				PSEModelExplorer.RateParametersAndPopulation paramsAndPopulation = explorer.extractRateParametersAndPopulation(rateExpr);
				String action = explorer.getTransitionAction(succNr);
				model.addTransition(reaction, permut[states.get(state)], permut[states.get(stateNew)],
						paramsAndPopulation.first, paramsAndPopulation.second, action);
				sumOut = Expression.Plus(sumOut, rateExpr);
			}
			model.setSumLeaving(sumOut);
			model.finishState();
		}

		this.stateArray = new State[states.size()];
		for (State s : statesList) {
			stateArray[permut[states.get(s)]] = s;
		}

		mainLog.println();

		mainLog.print("Reachable states exploration and model construction");
		mainLog.println(" done in " + ((System.currentTimeMillis() - timer) / 1000.0) + " secs.");

		return model;
	}
}
