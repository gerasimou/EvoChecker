//==============================================================================
//	
//	Copyright (c) 2013-
//	Authors:
//	* Dave Parker <david.parker@comlab.ox.ac.uk> (University of Oxford)
//  * Frits Dannenberg <frits.dannenberg@cs.ox.ac.uk> (University of Oxford)
//	* Ernst Moritz Hahn <emhahn@cs.ox.ac.uk> (University of Oxford)
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Map;

import explicit.BirthProcess;
import explicit.StateValues;
import parser.ast.Expression;
import parser.ast.ExpressionIdent;
import parser.ast.LabelList;
import parser.ast.RewardStruct;
import parser.type.TypeDouble;
import parser.Values;
import parser.State;
import prism.*;

/**
 * Implementation of fast adaptive uniformisation (FAU).
 */
public final class PSEFastAdaptiveUniformisation extends PrismComponent
{
	/**
	 * Stores properties of states needed for fast adaptive method.
	 * This includes the current-step probability, next-state probability,
	 * and the transient probability (sum of step probabilities weighted
	 * with birth process distributions). It also contains the list of successor
	 * states and the rates to them, the number of incoming transitions
	 * (references) and a flag whether the state has a significant probability
	 * mass (alive).
	 */
	private final static class StateProp
	{
		/** current-step probability.
		 * should contain initial probability before actual analysis is started.
		 * will contain transient probability after analysis. */
		private BigDecimal prob;
		/** next-state probability */
		private BigDecimal nextProb;
		/** sum probability weighted with birth process distribution */
		private BigDecimal sumProb;
		/** number of incoming transitions of relevant states */
		private int references;	// TODO: get rid of once certain that preds/succs work properly
		/** true if and only if state probability above relevance threshold */
		private boolean alive;
		/** parent transition map */
		private TransitionMap transitionMap;

		/**
		 * Constructs a new state property object.
		 */
		StateProp(TransitionMap transitionMap)
		{
			prob = BigDecimal.ZERO;
			nextProb = BigDecimal.ZERO;
			sumProb = BigDecimal.ZERO;
			references = 0;
			alive = true;
			this.transitionMap = transitionMap;
		}

		/**
		 * Set current state probability.
		 * 
		 * @param prob current state probability to set
		 */
		void setProb(BigDecimal prob)
		{
			this.prob = prob;
		}

		/**
		 * Gets current state probability.
		 * 
		 * @return current state probability
		 */
		BigDecimal getProb()
		{
			return prob;
		}

		/**
		 * Sets next state probability.
		 * 
		 * @param nextProb next state probability to set
		 */
		void setNextProb(BigDecimal nextProb)
		{
			this.nextProb = nextProb;
		}

		/**
		 * Gets next state probability.
		 * 
		 * @return next state probability
		 */
		BigDecimal getNextProb()
		{
			return nextProb;
		}

		/**
		 * Adds value to next state probability.
		 * 
		 * @param add value to add to next state probability
		 */
		void addToNextProb(BigDecimal add)
		{
			this.nextProb = this.nextProb.add(add);
		}

		/**
		 * Sets weighted sum probability.
		 * 
		 * @param sum weighted sum probability to set.
		 */
		void setSumProb(BigDecimal sum)
		{
			this.sumProb = sum;
		}

		/**
		 * Adds current probability times {@code poisson} to weighted sum probability.
		 * 
		 * @param poisson this value times current probability will be added to sum probability
		 */
		void addToSumProb(BigDecimal poisson)
		{
			sumProb = sumProb.add(poisson.multiply(prob));
		}

		/**
		 * Gets weighted sum probability.
		 * 
		 * @return weighted sum probability
		 */
		BigDecimal getSumProb()
		{
			return sumProb;
		}

		/**
		 * Prepares next iteration step.
		 * Sets current probability to next probability, and sets next
		 * probability to zero.
		 */
		void prepareNextIteration()
		{
			prob = nextProb;
			nextProb = BigDecimal.ZERO;
		}

		/**
		 * Sets whether state is alive.
		 * 
		 * @param alive whether state should be set to being alive
		 */
		void setAlive(boolean alive)
		{
			this.alive = alive;
		}

		/**
		 * Checks whether state is alive.
		 * 
		 * @return true iff state is alive
		 */
		boolean isAlive()
		{
			return alive;
		}

		/**
		 * Increments the number of references of this state.
		 * The number of references should correspond to the number of alive
		 * states which have this state as successor state.
		 */
		void incReferences()
		{
			references++;
		}

		/**
		 * Decrements the number of references of this state.
		 * The number of references should correspond to the number of alive
		 * states which have this state as successor state.
		 */
		void decReferences()
		{
			references--;
		}

		/**
		 * Deletes this state.
		 * This means basically removing all of its successors. Beforehand,
		 * their reference counter is decreased, because this state does no
		 * longer count as a model state. It is left in the model however,
		 * because it might still be the successor state of some alive state.
		 */
		void delete()
		{
			transitionMap.removeOutgoingTransitions(this);
			alive = false;
			prob = BigDecimal.ZERO;
			nextProb = BigDecimal.ZERO;
		}

		/**
		 * Checks whether this state can be removed.
		 * This is only the case if its probability is below the threshold
		 * specified, and then only if there are no transitions from alive
		 * states into this state.
		 * 
		 * @return true if and only if this state can be removed
		 */
		boolean canRemove()
		{
			return !alive && references == 0;
		}

		/**
		 * Returns the sum of all rates leaving to successor states.
		 * 
		 * @return sum of all rates leaving to successor states
		 */
		BigDecimal sumRates() throws PrismException
		{
			HashSet<Transition> outgoing = transitionMap.getOutgoingTransitions(this);
			if (outgoing.isEmpty()) {
				if (alive) {
					// This workaround is necessary since self-loops
					// aren't stored in transitionMap.
					return BigDecimal.ONE;
				} else {
					return BigDecimal.ZERO;
				}
			}

			BigDecimal sum = BigDecimal.ZERO;
			for (Transition trans : outgoing) {
				sum = sum.add(trans.getRatePopulation().multiply(trans.getRateParamsUpper()));
			}
			return sum;
		}

		void setSink()
		{
			transitionMap.removeOutgoingTransitions(this);
		}
	}

	private final static class Transition
	{
		int reaction;
		StateProp from;
		StateProp to;
		Expression rateParams;
		BigDecimal rateParamsLower;
		BigDecimal rateParamsUpper;
		BigDecimal ratePopulation;
		boolean parametrised = false;

		Transition(int reaction, StateProp from, StateProp to, Expression rateParams, double ratePopulation, BoxRegion region)
				throws PrismException
		{
			this.reaction = reaction;
			this.from = from;
			this.to = to;
			this.rateParams = rateParams;
			this.ratePopulation = new BigDecimal(ratePopulation);
			evaluateRateParams(region);
		}

		void evaluateRateParams(BoxRegion region) throws PrismException
		{
			rateParamsLower = new BigDecimal(rateParams.evaluateDouble(region.getLowerBounds()));
			rateParamsUpper = new BigDecimal(rateParams.evaluateDouble(region.getUpperBounds()));
			parametrised = rateParamsLower != rateParamsUpper;
		}

		int getReaction()
		{
			return reaction;
		}

		BigDecimal getRateParamsLower()
		{
			return rateParamsLower;
		}

		BigDecimal getRateParamsUpper()
		{
			return rateParamsUpper;
		}

		BigDecimal getRatePopulation()
		{
			return ratePopulation;
		}

		StateProp fromState()
		{
			return from;
		}

		StateProp toState()
		{
			return to;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((from == null) ? 0 : from.hashCode());
			result = prime * result + reaction;
			result = prime * result + ((to == null) ? 0 : to.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Transition other = (Transition) obj;
			if (from == null) {
				if (other.from != null)
					return false;
			} else if (!from.equals(other.from))
				return false;
			if (reaction != other.reaction)
				return false;
			if (to == null) {
				if (other.to != null)
					return false;
			} else if (!to.equals(other.to))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Transition [reaction=" + reaction + ", from=" + from
					+ ", to=" + to + "]";
		}
	}

	@SuppressWarnings("serial")
	private final static class TransitionMap extends HashMap<StateProp, HashSet<Pair<Transition, Transition>>>
	{
		TransitionMap()
		{
			super();
		}

		HashSet<StateProp> getPredecessors(StateProp state)
		{
			HashSet<StateProp> result = new HashSet<StateProp>();
			for (Pair<Transition, Transition> transPair : get(state)) {
				if (transPair.first != null) {
					result.add(transPair.first.fromState());
				}
			}
			return result;
		}

		HashSet<StateProp> getSuccessors(StateProp state)
		{
			HashSet<StateProp> result = new HashSet<StateProp>();
			for (Pair<Transition, Transition> transPair : get(state)) {
				if (transPair.second != null) {
					result.add(transPair.second.toState());
				}
			}
			return result;
		}

		HashSet<Transition> getOutgoingTransitions(StateProp state)
		{
			HashSet<Transition> result = new HashSet<Transition>();
			for (Pair<Transition, Transition> transPair : get(state)) {
				if (transPair.second != null) {
					result.add(transPair.second);
				}
			}
			return result;
		}

		// TODO: delete, use getOutgoingTransitions(state) + manual check instead
		HashSet<Transition> filterOutgoingTransitions(StateProp state, int reaction)
		{
			HashSet<Transition> result = new HashSet<Transition>();
			for (Pair<Transition, Transition> transPair : get(state)) {
				if (transPair.second != null && transPair.second.getReaction() == reaction) {
					result.add(transPair.second);
				}
			}
			return result;
		}
		
		HashSet<Transition> filterIncomingTransitions(StateProp state, int reaction)
		{
			HashSet<Transition> result = new HashSet<Transition>();
			for (Pair<Transition, Transition> transPair : get(state)) {
				if (transPair.first != null && transPair.first.getReaction() == reaction) {
					result.add(transPair.first);
				}
			}
			return result;
		}

		boolean hasPredecessors(StateProp state)
		{
			return !getPredecessors(state).isEmpty();
		}

		boolean hasSuccessors(StateProp state)
		{
			return !getSuccessors(state).isEmpty();
		}

		void addTransition(Transition trans)
		{
			StateProp state = trans.fromState();
			StateProp succState = trans.toState();
			if (!containsKey(state)) {
				put(state, new HashSet<Pair<Transition, Transition>>());
			}
			if (!containsKey(succState)) {
				put(succState, new HashSet<Pair<Transition, Transition>>());
			}

			boolean done = false;
			for (Transition transTo : filterIncomingTransitions(state, trans.getReaction())) {
				done = true;
				get(state).add(new Pair<Transition, Transition>(transTo, trans));
				get(state).remove(new Pair<Transition, Transition>(transTo, null));
			}
			if (!done) {
				get(state).add(new Pair<Transition, Transition>(null, trans));
			}

			done = false;
			for (Transition transFrom : filterOutgoingTransitions(succState, trans.getReaction())) {
				done = true;
				get(succState).add(new Pair<Transition, Transition>(trans, transFrom));
				get(succState).remove(new Pair<Transition, Transition>(null, transFrom));
			}
			if (!done) {
				get(succState).add(new Pair<Transition, Transition>(trans, null));
			}
			
			succState.incReferences();
		}

		void removeOutgoingTransitions(StateProp state)
		{
			HashSet<Pair<Transition, Transition>> toAdd = new HashSet<Pair<Transition, Transition>>();
			HashSet<Pair<Transition, Transition>> toRemove = new HashSet<Pair<Transition, Transition>>();

			for (StateProp succ : getSuccessors(state)) {
				for (Pair<Transition, Transition> transPair : get(succ)) {
					if (transPair.first != null && transPair.first.fromState().equals(state)) {
						if (transPair.second != null) {
							Pair<Transition, Transition> newPair = new Pair<Transition, Transition>(null, transPair.second);
							toAdd.add(newPair);
						}
						toRemove.add(transPair);
						succ.decReferences();
					}
				}
				get(succ).addAll(toAdd);
				get(succ).removeAll(toRemove);
				toAdd.clear();
				toRemove.clear();
			}

			for (Pair<Transition, Transition> transPair : get(state)) {
				if (transPair.second != null) {
					assert transPair.second.fromState().equals(state);
					if (transPair.first != null) {
						Pair<Transition, Transition> newPair = new Pair<Transition, Transition>(transPair.first, null);
						toAdd.add(newPair);
					}
					toRemove.add(transPair);
				}
			}
			get(state).addAll(toAdd);
			get(state).removeAll(toRemove);
		}
	}

	public enum VectorType {
		MIN("minimised"), MAX("maximised");
		private String verbose;

		private VectorType(String verbose) {
			this.verbose = verbose;
		}

		@Override
		public String toString() {
			return verbose;
		}
	}

	/** model exploration component to generate new states */
	private PSEModelExplorer modelExplorer;
	/** */
	private TransitionMap transitionMap = new TransitionMap();
	/** */
	private BoxRegion currentRegion;
	/** probability allowed to drop birth process */
	private double epsilon;
	/** probability threshold when to drop states in discrete-time process */
	private double delta;
	/** number of intervals to divide time into */
	private int numIntervals;
	/** iterations after which switch to sparse matrix if no new/dropped states */
	private int arrayThreshold;
	
	/** reward structure to use for analysis */
	private RewardStruct rewStruct = null;
	/** result value of analysis */
	private double value;
	/** model constants */
	private Values constantValues = null;
	/** maps from state (assignment of variable values) to property object */
	private LinkedHashMap<State,StateProp> states;
	/** states for which successor rates are to be computed */
	private ArrayList<State> addDistr;
	/** states which are to be deleted */
	private ArrayList<State> deleteStates;
	/** initial size of state hash map */
	private final int initSize = 3000;
	/** maximal total leaving rate of all states alive */
	private BigDecimal maxRate = BigDecimal.ZERO;
	/** target state set - used for reachability (until or finally properties) */
	private Expression target;
	/** number of consecutive iterations without new states are state drops */
	private int itersUnchanged;
	/** sum of probabilities in stages of birth process seen so far */
	private double birthProbSum;
	/** birth process used for time discretisation */
	private BirthProcess birthProc;
	/** states which fulfill this will be made absorbing - for until props */
	private Expression sink;
	/** if true, don't drop further states.
	 * Used to avoid excessive probability loss in some cases. */
	private boolean keepSumProb;
	/** maximal number of states ever stored during analysis */
	private int maxNumStates;
	/** list of special labels we need to maintain, like "init", "deadlock", etc. */
	private LabelList specialLabels;
	/** set of initial states of the model */
	private HashSet<State> initStates;
	/** total loss of probability in discrete-time process */
	private BigDecimal totalProbLoss;
	/** probability mass intentionally set to zero */
	private BigDecimal totalProbSetZero;

	/**
	 * Constructor.
	 */
	public PSEFastAdaptiveUniformisation(PrismComponent parent, PSEModelExplorer modelExplorer) throws PrismException
	{
		super(parent);

		this.modelExplorer = modelExplorer;
		maxNumStates = 0;

		epsilon = settings.getDouble(PrismSettings.PRISM_FAU_EPSILON);
		delta = settings.getDouble(PrismSettings.PRISM_FAU_DELTA);
		numIntervals = settings.getInteger(PrismSettings.PRISM_FAU_INTERVALS);
		arrayThreshold = settings.getInteger(PrismSettings.PRISM_FAU_ARRAYTHRESHOLD);
		rewStruct = null;
		target = Expression.False();
		sink = Expression.False();
		specialLabels = new LabelList();
		specialLabels.addLabel(new ExpressionIdent("deadlock"), new ExpressionIdent("deadlock"));
		specialLabels.addLabel(new ExpressionIdent("init"), new ExpressionIdent("init"));
	}

	public void configureParameterSpace(BoxRegion region) throws PrismException
	{
		currentRegion = region;
		for (HashSet<Pair<Transition, Transition>> transPairs : transitionMap.values()) {
			for (Pair<Transition, Transition> transPair : transPairs) {
				if (transPair.second != null) {
					transPair.second.evaluateRateParams(region);
				}
			}
		}
	}

	/**
	 * Returns maximal number of states used during analysis.
	 * 
	 * @return maximal number of states used during analysis
	 */
	public int getMaxNumStates()
	{
		return maxNumStates;
	}

	/**
	 * Sets which states shall be treated as sink states.
	 * To be used for properties like "a U<=T b" where states "b || !a" have
	 * to be made absorbing.
	 * 
	 * @param sink expressing stating which states are sink states
	 * @throws PrismException thrown if problems in underlying function occurs
	 */
	public void setSink(Expression sink) throws PrismException
	{
		this.sink = sink;
		if (states != null) {
			for (Map.Entry<State,StateProp> statePair : states.entrySet()) {
				State state = statePair.getKey();
				StateProp prop = statePair.getValue();
				modelExplorer.queryState(state);
				specialLabels.setLabel(0, modelExplorer.getNumTransitions() == 0 ? Expression.True() : Expression.False());
				specialLabels.setLabel(1, initStates.contains(state) ? Expression.True() : Expression.False());
				Expression evSink = sink.deepCopy();
				evSink = (Expression) evSink.expandLabels(specialLabels);
				if (evSink.evaluateBoolean(constantValues, state)) {
					prop.setSink();
				}
			}
		}
	}

	/**
	 * Get the number of states in the current window.
	 */
	public int getNumStates()
	{
		return states.size();
	}

	/**
	 * Compute transient probability distribution (forwards).
	 * Start from initial state (or uniform distribution over multiple initial states).
	 */
	public StateValues doTransient(double time) throws PrismException
	{
		return doTransient(time, null);
	}

	/**
	 * Compute transient probability distribution (forwards).
	 * Use the passed in vector initDist as the initial probability distribution (time 0).
	 * In case initDist is null starts at the default initial state with prob 1.
	 * 
	 * @param time Time point
	 * @param initDist Initial distribution
	 */
	public StateValues doTransient(double time, BoxRegionValues initDist) throws PrismException
	{
		totalProbLoss = BigDecimal.ZERO;
		totalProbSetZero = BigDecimal.ZERO;
		maxNumStates = 0;
		mainLog.println("\nComputing probabilities (fast adaptive uniformisation)...");
		//mainLog.println("\nComputing " + vectorType + " probabilities (PSE+FAU)...");

		// TODO: initDist currently ignored
		addDistr = null;

		/* run fast adaptive uniformisation */
		computeTransientProbsAdaptive(time); //, vectorType);

		/* prepare and return results */
		ArrayList<State> statesList = new ArrayList<State>(states.size());
		Double[] probsArr = new Double[states.size()];
		int probsArrEntry = 0;
		//System.out.println(states);
		for (Map.Entry<State,StateProp> statePair : states.entrySet()) {
			statesList.add(statePair.getKey());
			probsArr[probsArrEntry] = statePair.getValue().getProb().doubleValue();
			probsArrEntry++;
		}
		StateValues probs = new StateValues(TypeDouble.getInstance(), probsArr, statesList);

		mainLog.println("\nTotal probability lost is : " + getTotalDiscreteLoss());
		mainLog.println("Maximal number of states stored during analysis : " + getMaxNumStates());
		
		return probs;
	}

	/**
	 * Compute transient probabilities using fast adaptive uniformisation
	 * Compute the probability of being in each state at time {@code t}.
	 * If corresponding options are set, also computes cumulative rewards.
	 * For space efficiency, the initial distribution vector will be modified and values over-written,  
	 * so if you wanted it, take a copy. 
	 * @param time time point
	 */
	public void computeTransientProbsAdaptive(double time) throws PrismException
	{
		if (addDistr == null) {
			addDistr = new ArrayList<State>();
			deleteStates = new ArrayList<State>();
			states = new LinkedHashMap<State,StateProp>(initSize);
			value = 0.0;
			prepareInitialDistribution();
		}

		double initIval = settings.getDouble(PrismSettings.PRISM_FAU_INITIVAL);
		if (time - initIval < 0.0) {
			initIval = 0.0;
		}
		if (initIval != 0.0) {
			iterateAdaptiveInterval(initIval);
			for (StateProp prop : states.values()) {
				prop.setProb(prop.getSumProb());
				prop.setSumProb(BigDecimal.ZERO);
				prop.setNextProb(BigDecimal.ZERO);
			}
			updateStates();
		}

		for (int ivalNr = 0; ivalNr < numIntervals; ivalNr++) {
			double interval = (time - initIval) / numIntervals;
			iterateAdaptiveInterval(interval);
			for (StateProp prop : states.values()) {
				prop.setProb(prop.getSumProb());
				prop.setSumProb(BigDecimal.ZERO);
				prop.setNextProb(BigDecimal.ZERO);
			}
			updateStates();
		}
		for (Map.Entry<State,StateProp> statePair : states.entrySet()) {
			State state = statePair.getKey();
			modelExplorer.queryState(state);
			specialLabels.setLabel(0, modelExplorer.getNumTransitions() == 0 ? Expression.True() : Expression.False());
			specialLabels.setLabel(1, initStates.contains(state) ? Expression.True() : Expression.False());
			Expression evTarget = target.deepCopy();
			evTarget = (Expression) evTarget.expandLabels(specialLabels);
		}
	}

	/**
	 * Performs fast adaptive uniformisation for a single time interval.
	 * 
	 * @param interval duration of time interval
	 * @throws PrismException
	 */
	private void iterateAdaptiveInterval(double interval) throws PrismException
	{
		birthProc = new BirthProcess();
		birthProc.setTime(interval);
		birthProc.setEpsilon(epsilon);

		int iters = 0;
		birthProbSum = 0.0;
		itersUnchanged = 0;
		keepSumProb = false;
		while (birthProbSum < (1 - epsilon)) {
			if (birthProbSum >= epsilon/2) {
				keepSumProb = true;
			}
			//if ((itersUnchanged == arrayThreshold)) {
			//	iters = arrayIterate(iters);
			//} else {
				//?? long birthProcTimer = System.currentTimeMillis();
				double prob = birthProc.calculateNextProb(maxRate.doubleValue());
				//System.out.println("birth proc prob = " + prob);
				//?? birthProcTimer = System.currentTimeMillis() - birthProcTimer;
				birthProbSum += prob;
				//collectValuePostIter(prob, birthProbSum);
				//System.out.println("states = " + states);
				for (StateProp prop : states.values()) {
					//System.out.println("birth proc prob (big decim) = " + new BigDecimal(prob));
					//System.out.println("sum prob before = " + prop.getSumProb());
					//System.out.println("state prob (that is multiplied by birth proc prob) = " + prop.getProb());
					prop.addToSumProb(new BigDecimal(prob));
					//System.out.println("sum prob after = " + prop.getSumProb());
				}
				
				for (Map.Entry<State, StateProp> statePair : states.entrySet()) {
					StateProp prop = statePair.getValue();
					//System.out.println("XX: " + prop.getProb() + " | sum = " + prop.getSumProb());
				}
				
				
				vmMult(maxRate, VectorType.MIN);
				
				for (Map.Entry<State, StateProp> statePair : states.entrySet()) {
					StateProp prop = statePair.getValue();
					//System.out.println("YY: " + prop.getProb() + " | sum = " + prop.getSumProb());
				}

				//System.out.println("PRED MOJIM UPDATESTATES");
				updateStates();
				//System.out.println("PO MOJOM UPDATESTATES");

				for (Map.Entry<State, StateProp> statePair : states.entrySet()) {
					StateProp prop = statePair.getValue();
					//System.out.println("ZZ: " + prop.getProb() + " | sum = " + prop.getSumProb());
				}

				
				iters++;
			//}
		}

		//computeTotalDiscreteLoss();
		totalProbLoss = totalProbLoss.add(totalProbSetZero);
	}

	// TODO arrayIterate
	// TODO collectVluePostIter (that is relevant only for cumulative rewards)

	/**
	 * Updates state values once a transient analysis of time interval finished.
	 * Deletes states which can be deleted according to their current
	 * probability and the threshold. Computes new maximal rate for remaining
	 * states. Computes transitions to successors of states which have become
	 * alive to to probability threshold only after a transient analysis has
	 * finished.
	 * 
	 * @throws PrismException thrown if something goes wrong
	 */
	private void updateStates() throws PrismException
	{
		maxRate = BigDecimal.ZERO;
		addDistr.clear();
		for (Map.Entry<State, StateProp> statePair : states.entrySet()) {
			State state = statePair.getKey();
			StateProp prop = statePair.getValue();
			//System.out.println("YY2: " + prop.getProb() + " | sum = " + prop.getSumProb());
			//System.out.println("delta = " + delta);
			if (prop.getProb().compareTo(new BigDecimal(delta)) > 0) {
				prop.setAlive(true);
				if (!transitionMap.hasSuccessors(prop)) {
					itersUnchanged = 0;
					addDistr.add(state);
				} else {
					maxRate = maxRate.max(prop.sumRates());
				}
			} else {
				//System.out.println("WAAAAT");
				//System.out.println(prop.getProb());
				totalProbLoss = totalProbLoss.add(prop.getProb());
				prop.delete();
			}
		}
		for (int stateNr = 0; stateNr < addDistr.size(); stateNr++) {
			computeStateRates(addDistr.get(stateNr));
			maxRate = maxRate.max(states.get(addDistr.get(stateNr)).sumRates());
		}
		maxRate = maxRate.multiply(new BigDecimal(1.02));

		removeDeletedStates();
	}

	/**
	 * Removes all states subject to removal.
	 * This affects states which both have a present-state probability below
	 * the given threshold, and do not have incoming transitions from states
	 * with a relevant probability mass.
	 */
	private void removeDeletedStates()
	{
		boolean unchanged = true;
		for (Map.Entry<State,StateProp> statePair : states.entrySet()) {
			State state = statePair.getKey();
			StateProp prop = statePair.getValue();
			if (prop.canRemove()) {
				deleteStates.add(state);
				unchanged = false;
			}
		}
		if (!keepSumProb) {
			for (int i = 0; i < deleteStates.size(); i++) {
				states.remove(deleteStates.get(i));
			}
		}
		if (unchanged) {
			itersUnchanged++;
		} else {
			itersUnchanged = 0;
		}
		deleteStates.clear();
	}
    
	/**
	 * Prepares initial distribution for the case of a single initial state.
	 * 
	 * @throws PrismException
	 */
    private void prepareInitialDistribution() throws PrismException
    {
    	initStates = new HashSet<State>();
		State initState = modelExplorer.getDefaultInitialState();
		initStates.add(initState);
		addToModel(initState);
		computeStateRates(initState);
		states.get(initState).setProb(BigDecimal.ONE);
		maxRate = states.get(initState).sumRates().multiply(new BigDecimal(1.02));
	}

	/**
	 * Returns the total probability loss.
	 * 
	 * @return
	 */
	public BigDecimal getTotalDiscreteLoss()
	{
		return totalProbLoss;
	}

	/**
	 * Adds a @{code state} to model.
	 * Computes reward for this states, creates entry in map of states,
	 * and updates number of states
	 * 
	 * @param state state to add
	 * @throws PrismException thrown if something wrong happens in underlying methods
	 */
	private void addToModel(State state) throws PrismException
	{
		StateProp prop = new StateProp(transitionMap);
		//prop.setReward(computeRewards(state));
		states.put(state, prop);
		maxNumStates = Math.max(maxNumStates, states.size());
	}

	/**
	 * Computes successor rates and rewards for a given state.
	 * Rewards computed depend on the reward structure set by
	 * {@code setRewardStruct}.
	 * 
	 * @param state state to compute successor rates and rewards for
	 * @throws PrismException thrown if something goes wrong
	 */
	private void computeStateRates(State state) throws PrismException
	{
		modelExplorer.queryState(state);
		/*
		specialLabels.setLabel(0, modelExplorer.getNumTransitions() == 0 ? Expression.True() : Expression.False());
		specialLabels.setLabel(1, initStates.contains(state) ? Expression.True() : Expression.False());
		Expression evSink = sink.deepCopy();
		evSink = (Expression) evSink.expandLabels(specialLabels);
		if (evSink.evaluateBoolean(constantValues, state)) {
			
			rateExprs = new Expression[1];
			succStates = new StateProp[1];
			rateExprs[0] = Expression.Double(1.0);
			succStates[0] = states.get(state);
			
		} else*/ {
			int nt = modelExplorer.getNumTransitions();
			for (int i = 0; i < nt; i++) {
				State succState = modelExplorer.computeTransitionTarget(i);
				StateProp succProp = states.get(succState);
				if (succProp == null) {
					addToModel(succState);
					modelExplorer.queryState(state);
					succProp = states.get(succState);
				}

				Expression rateExpr = modelExplorer.getTransitionProbability(i);
				int reaction = modelExplorer.getReaction(i);
				PSEModelExplorer.RateParametersAndPopulation paramsPop = modelExplorer.extractRateParametersAndPopulation(rateExpr);
				Expression rateParams = paramsPop.getParameters();
				double ratePop = paramsPop.getPopulation();

				Transition newTrans = new Transition(reaction, states.get(state), succProp, rateParams, ratePop, currentRegion);
				transitionMap.addTransition(newTrans);
			}
			/*
			// XXX: Do we need deadlocks? PSEModelBuilder ignores them, too.
			if (nt == 0) {
				succRates = new double[1];
				succStates = new StateProp[1];
				succRates[0] = 1.0;
				succStates[0] = states.get(state);
			}
			*/
		}
	}

	/**
	 * Perform a single vector-matrix multiplication.
	 * 
	 * @param maxRate maximal total leaving rate sum in living states
	 */
	private void vmMult(BigDecimal maxRate, VectorType vectorType)
	{
		for (StateProp state : states.values()) {
			state.setNextProb(state.getProb());
			//System.out.println("PRED TRANZICIAMI: nextprob = " + state.getNextProb());
			//System.out.println(state);
			//System.out.println(transitionMap);
			for (Pair<Transition, Transition> transPair : transitionMap.get(state)) {
				// Incoming transitions
				if (transPair.second == null) {
					Transition predTrans = transPair.first;
					//System.out.println("incoming = " + vmMultIn(predTrans, maxRate, vectorType));
					state.addToNextProb(vmMultIn(predTrans, maxRate, vectorType));
				}

				// Outgoing transitions
				else if (transPair.first == null) {
					Transition trans = transPair.second;
					//System.out.println("outgoing = " + vmMultOut(trans, maxRate, vectorType).negate());
					state.addToNextProb(vmMultOut(trans, maxRate, vectorType).negate());
				}

				// Both incoming and outgoing
				else {
					Transition predTrans = transPair.first;
					Transition trans = transPair.second;

					BigDecimal predProb = predTrans.fromState().getProb();
					BigDecimal prob = trans.fromState().getProb();

					assert predTrans.getRateParamsLower().equals(trans.getRateParamsLower());
					assert predTrans.getRateParamsUpper().equals(trans.getRateParamsUpper());

					BigDecimal midSumNumerator = predProb.multiply(predTrans.getRatePopulation()).subtract(prob.multiply(trans.getRatePopulation()));
					BigDecimal resProb = midSumNumerator.divide(maxRate, 750, RoundingMode.HALF_EVEN);
					if (midSumNumerator.compareTo(BigDecimal.ZERO) > 0.0) {
						switch (vectorType) {
						case MIN:
							resProb = resProb.multiply(predTrans.getRateParamsLower());
							break;
						case MAX:
							resProb = resProb.multiply(predTrans.getRateParamsUpper());
							break;
						}
					} else {
						switch (vectorType) {
						case MIN:
							resProb = resProb.multiply(predTrans.getRateParamsUpper());
							break;
						case MAX:
							resProb = resProb.multiply(predTrans.getRateParamsLower());
							break;
						}
					}
					//System.out.println("both = " + resProb);
					state.addToNextProb(resProb);
				}
			}
			//System.out.println("PO TRANZICIACH: nextprob = " + state.getNextProb());
		}
		for (StateProp prop : states.values()) {
			if (!prop.isAlive()) {
				totalProbLoss = totalProbLoss.add(prop.getProb());
			}
			//System.out.println("PRED PREPARE: prob = " + prop.getProb() + " | nextprob = " + prop.getNextProb());
			prop.prepareNextIteration();
			//System.out.println("PO PREPARE: prob = " + prop.getProb() + " | nextprob = " + prop.getNextProb());
		}
	}

	private BigDecimal vmMultIn(Transition predTrans, BigDecimal maxRate, VectorType vectorType)
	{
		BigDecimal result = predTrans.fromState().getProb().multiply(predTrans.getRatePopulation()).divide(maxRate, 750, RoundingMode.HALF_EVEN);
		switch (vectorType) {
		case MIN:
			result = result.multiply(predTrans.getRateParamsLower());
			break;
		case MAX:
			result = result.multiply(predTrans.getRateParamsUpper());
			break;
		}
		return result;
	}

	private BigDecimal vmMultOut(Transition trans, BigDecimal maxRate, VectorType vectorType)
	{
		//System.out.println("prob = " + trans.fromState().getProb());
		//System.out.println("pop = " + trans.getRatePopulation());
		//System.out.println("max rate = " + maxRate);
		//System.out.println(trans.fromState().getProb().doubleValue() * trans.getRatePopulation() / maxRate);
		BigDecimal result = trans.fromState().getProb().multiply(trans.getRatePopulation()).divide(maxRate, 750, RoundingMode.HALF_EVEN);
		switch (vectorType) {
		case MIN:
			result = result.multiply(trans.getRateParamsUpper());
			break;
		case MAX:
			result = result.multiply(trans.getRateParamsLower());
			break;
		}
		//System.out.println("vmMultOut res = " + result);
		return result;
	}
}
