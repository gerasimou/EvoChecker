//==============================================================================
//	
//	Copyright (c) 2014-
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

import java.io.File;
import java.util.*;

import parser.Values;
import parser.ast.Expression;
import prism.ModelType;
import prism.Pair;
import prism.PrismException;
import prism.PrismLog;
import explicit.ModelExplicit;

/**
 * Represents a parametrised CTMC model to be used for PSE-based
 * techniques of analysis.
 * 
 * @see PSEModelBuilder
 */
public final class PSEModel extends ModelExplicit
{
	/** complete parameter space */
	private BoxRegion completeSpace;
	/** total number of probabilistic transitions over all states */
	private int numTransitions;
	/** begin and end of state transitions */
	private int[] rows;
	/** origins of distribution branches */
	private int[] trStSrc;
	/** targets of distribution branches */
	private int[] trStTrg;
	/** all transitions' rate parameters, as expressions */
	private Expression[] rateParams;
	/** all transitions' rate parameters, evaluated with lower bounds of current region */
	private double[] trRateLower;
	/** all transitions' rate parameters, evaluated with upper bounds of current region */
	private double[] trRateUpper;
	/** species populations in all transitions' origin states */
	private double[] trRatePopul;
	/** indication on all transitions, whether their rate depends on parameters */
	private boolean[] parametrisedTransitions;
	/** all transitions' reactions, i.e. transition kinds */
	private int[] reactions;
	/** labels - per transition, <i>not</i> per action */
	private String[] labels;
	/** total sum of leaving rates for a state, as expressions */
	private Expression[] exitRatesExpr;
	/** total sum of leaving rates for a state, evaluated */
	private double[] exitRates;
	/** set of hash codes for deciding whether state has predecessors via reaction */
	private Set<Integer> predecessorsViaReaction;
	/** map from state to transitions coming into the state (and not outgoing) */
	private int[] trsI;
	private int[] trsIBeg;
	/** map from state to transitions both incoming in and outgoing from the state */
	private int[] trsIO;
	private int[] trsIOBeg;
	/** map from state to transitions going out from the state (and not incoming) */
	private int[] trsO;
	private int[] trsOBeg;
	/** map from state to transitions that are nopt parametrised */
	private int[] trsNP;
	private int[] trsNPBeg;

	private PSEMultOptions multOptions;

	/**
	 * Constructs a new parametric model.
	 */
	PSEModel()
	{
		numStates = 0;
		numTransitions = 0;
		initialStates = new LinkedList<Integer>();
		deadlocks = new TreeSet<Integer>();
		predecessorsViaReaction = new HashSet<Integer>();
		multOptions = PSEMultUtility.getOptions();
	}

	// Accessors (for Model)

	@Override
	public ModelType getModelType()
	{
		return ModelType.CTMC;
	}

	public BoxRegion getCompleteSpace()
	{
		return completeSpace;
	}

	@Override
	public Values getConstantValues()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public int getNumTransitions()
	{
		return numTransitions;
	}

	@Override
	public Iterator<Integer> getSuccessorsIterator(int s)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean isSuccessor(int s1, int s2)
	{
		for (int trans = stateBegin(s1); trans < stateEnd(s1); trans++) {
			if (toState(trans) == s2)
				return true;
		}
		return false;
	}

	@Override
	public boolean allSuccessorsInSet(int s, BitSet set)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean someSuccessorsInSet(int s, BitSet set)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void findDeadlocks(boolean fix) throws PrismException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void checkForDeadlocks() throws PrismException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void checkForDeadlocks(BitSet except) throws PrismException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void buildFromPrismExplicit(String filename) throws PrismException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void exportToPrismExplicit(String baseFilename) throws PrismException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void exportToPrismExplicitTra(String filename) throws PrismException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void exportToPrismExplicitTra(File file) throws PrismException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void exportToPrismExplicitTra(PrismLog log)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void exportToDotFile(String filename) throws PrismException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void exportToDotFile(String filename, BitSet mark) throws PrismException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void exportTransitionsToDotFile(int i, PrismLog out)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void exportToPrismLanguage(String filename) throws PrismException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String infoString()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String infoStringTable()
	{
		String s = "";
		s += "States:      " + numStates + " (" + getNumInitialStates() + " initial)\n";
		s += "Transitions: " + getNumTransitions() + "\n";
		return s;
	}

	/**
	 * Allocates memory for subsequent construction of model. 
	 * 
	 * @param numStates number of states of the model
	 * @param numTransitions total number of probabilistic transitions of the model
	 */
	void reserveMem(int numStates, int numTransitions)
	{
		rows = new int[numStates + 1];
		labels = new String[numTransitions];
		reactions = new int[numTransitions];
		rateParams = new Expression[numTransitions];
		trRateLower = new double[numTransitions];
		trRateUpper = new double[numTransitions];
		parametrisedTransitions = new boolean[numTransitions];
		trRatePopul = new double[numTransitions];
		trStTrg = new int[numTransitions];
		trStSrc = new int[numTransitions];
		exitRatesExpr = new Expression[numStates];
		exitRates = new double[numStates];
	}

	/**
	 * Finishes the current state.
	 * Starting with the 0th state, this function shall be called once all
	 * transitions outgoing from the current nth state have been added.
	 * Subsequent method calls of {@code addTransition}
	 * will then apply to the (n+1)th state. Notice that this method must be
	 * called for each state of the method, even the last one, once all its
	 * transitions have been added.
	 */
	void finishState()
	{
		rows[numStates + 1] = numTransitions;
		numStates++;
	}

	/**
	 * Adds a probabilistic transition from the current state.
	 * 
	 * @param reaction kind of the transition being added
	 * @param fromState from which state the transition goes
	 * @param toState to which state the transition leads
	 * @param rateParamsExpr the transition's rate parameters as expression
	 * @param ratePopulation the transition's origin state's species population
	 * @param action action with which the transition is labelled
	 */
	void addTransition(int reaction, int fromState, int toState, Expression rateParamsExpr, double ratePopulation, String action)
	{
		reactions[numTransitions] = reaction;
		trStSrc[numTransitions] = fromState;
		trStTrg[numTransitions] = toState;
		rateParams[numTransitions] = rateParamsExpr;
		trRatePopul[numTransitions] = ratePopulation;
		labels[numTransitions] = action;

		predecessorsViaReaction.add(toState ^ reaction);

		numTransitions++;
	}

	/**
	 * Sets the total sum of leaving rates from the current state.
	 * 
	 * @param leaving sum of leaving rates from the current state
	 */
	void setSumLeaving(Expression leaving)
	{
		exitRatesExpr[numStates] = leaving;
	}

	/**
	 * Returns the number of the first transition going from {@code state}.
	 * 
	 * 
	 * @param state state to return number of first transition of
	 * @return number of first transition going from {@code state}
	 */
	int stateBegin(int state)
	{
		return rows[state];
	}

	/**
	 * Returns the number of the last transition going from {@code state} plus one.
	 * 
	 * @param state state to return number of last transition of
	 * @return number of last transition going from {@code state} plus one
	 */
	int stateEnd(int state)
	{
		return rows[state + 1];
	}

	/**
	 * Returns whether the rate of the given transition depends on parameters.
	 * 
	 * @param trans transition about which to decide whether it's parametrised
	 * @return true iff the rate {@code trans} depends on parameters
	 */
	boolean isParametrised(int trans)
	{
		//return true;
		return parametrisedTransitions[trans];
	}

	/**
	 * Returns reaction to which the given transition belongs.
	 * 
	 * @param trans transition to return reaction for
	 * @return reaction of {@code trans}
	 */
	int getReaction(int trans)
	{
		return reactions[trans];
	}

	/**
	 * Returns the predecessor state of the given transition.
	 * 
	 * @param trans transition to return predecessor for
	 * @return predecessor state of {@code trans}
	 */
	int fromState(int trans)
	{
		return trStSrc[trans];
	}

	/**
	 * Returns the successor state of the given transition.
	 * 
	 * @param trans transition to return successor for
	 * @return successor state of {@code trans}
	 */
	int toState(int trans)
	{
		return trStTrg[trans];
	}

	/**
	 * Returns the label of the given transition.
	 * 
	 * @param trans transition to return label of
	 * @return label of {@code trans}
	 */
	String getLabel(int trans)
	{
		return labels[trans];
	}

	/**
	 * Computes the maximum exit rate over all states in the model,
	 * i.e. max_i { sum_j R(i,j) }.
	 * 
	 * @return maximum exit rate
	 */
	double getMaxExitRate()
	{
		double max = Double.NEGATIVE_INFINITY;
		for (int state = 0; state < numStates; ++state) {
			if (exitRates[state] > max) {
				max = exitRates[state];
			}
		}
		return max;
	}

	/**
	 * Computes the maximum exit rate over states in {@code subset},
	 * i.e. max_{i in subset} { sum_j R(i,j) }.
	 * 
	 * @param subset subset of states over which to compute maximum exit rate
	 * @return maximum exit rate over states in {@code subset}
	 */
	double getMaxExitRate(BitSet subset)
	{
		if (subset == null) {
			// Will loop over all states
			subset = new BitSet(numStates);
			subset.set(0, numStates);
		}
		double max = Double.NEGATIVE_INFINITY;
		for (int state = subset.nextSetBit(0); state >= 0; state = subset.nextSetBit(state + 1)) {
			if (exitRates[state] > max) {
				max = exitRates[state];
			}
		}
		return max;
	}

	/**
	 * Computes the default rate used to uniformise this parametrised CTMC.
	 */
	double getDefaultUniformisationRate()
	{
		return 1.02 * getMaxExitRate();
	}

	/**
	 * Computes the default rate used to uniformise this parametrised CTMC,
	 * assuming that all states *not* in {@code nonAbs} have been made absorbing.
	 */
	double getDefaultUniformisationRate(BitSet nonAbs)
	{
		return 1.02 * getMaxExitRate(nonAbs);
	}

	/**
	 * Analyses the model's transitions in order to divide them between exclusively
	 * incoming, exclusively outgoing or both incoming/outgoing from the perspective
	 * of particular states. The results are stored in {@code trsI},
	 * {@code trsO} and {@code trsIO}, respectively.
	 */
	public void computeInOutTransitions()
	{
		System.err.printf("State cnt: %s\nTrans cnt: %s\n", numStates, numTransitions);
		if (trsI != null)
			return;

		// Initialise the transition sets
		Map<Integer, List<Integer>> trsI = new HashMap<Integer, List<Integer>>(numStates);
		Map<Integer, List<Integer>> trsO = new HashMap<Integer, List<Integer>>(numStates);
		Map<Integer, List<Pair<Integer,Integer>>> trsIO = new HashMap<Integer, List<Pair<Integer, Integer>>>(numStates);
		Map<Integer, List<Integer>> trsNP = new HashMap<Integer, List<Integer>>(numStates);
		for (int state = 0; state < numStates; state++) {
			trsI.put(state, new LinkedList<Integer>());
			trsO.put(state, new LinkedList<Integer>());
			trsIO.put(state, new LinkedList<Pair<Integer, Integer>>());
			trsNP.put(state, new LinkedList<Integer>());
		}

		// Populate the sets with transition indices
		int trsICnt = 0;
		int trsOCnt = 0;
		int trsIOCnt = 0;
		int trsNPCnt = 0;
		for (int pred = 0; pred < numStates; pred++) {
			for (int predTrans = stateBegin(pred); predTrans < stateEnd(pred); predTrans++) {
				boolean inout = false;
				int predReaction = getReaction(predTrans);
				int state = toState(predTrans);
				if (!isParametrised(predTrans)) {
					trsNP.get(state).add(predTrans);
					trsNPCnt += 1;
					continue;
				}
				for (int trans = stateBegin(state); trans < stateEnd(state); trans++) {
					if (getReaction(trans) == predReaction) {
						inout = true;
						trsIO.get(state).add(new Pair<Integer, Integer>(predTrans, trans));
						trsIOCnt += 2;
						break;
					}
				}
				if (!inout) {
					trsI.get(state).add(predTrans);
					trsICnt += 1;
				}
				if (!predecessorsViaReaction.contains(pred ^ predReaction)) {
					trsO.get(pred).add(predTrans);
					trsOCnt += 1;
				}
			}
		}

		this.trsI = new int[trsICnt]; this.trsIBeg = new int[getNumStates() + 1];
		this.trsO = new int[trsOCnt]; this.trsOBeg = new int[getNumStates() + 1];
		this.trsIO = new int[trsIOCnt]; this.trsIOBeg = new int[getNumStates() + 1];
		this.trsNP = new int[trsNPCnt]; this.trsNPBeg = new int[getNumStates() + 1];
		trsICnt = 0;
		trsOCnt = 0;
		trsIOCnt = 0;
		trsNPCnt = 0;
		for (int s = 0; s < getNumStates(); ++s) {
			trsIBeg[s] = trsICnt;
			trsOBeg[s] = trsOCnt;
			trsIOBeg[s] = trsIOCnt;
			trsNPBeg[s] = trsNPCnt;
			for (Integer t : trsI.get(s)) {
				this.trsI[trsICnt++] = t;
			}
			for (Integer t : trsO.get(s)) {
				this.trsO[trsOCnt++] = t;
			}
			for (Pair<Integer,Integer> t : trsIO.get(s)) {
				this.trsIO[trsIOCnt++] = t.first;
				this.trsIO[trsIOCnt++] = t.second;
			}
			for (Integer t : trsNP.get(s)) {
				this.trsNP[trsNPCnt++] = t;
			}
		}
		trsIBeg[getNumStates()] = trsICnt;
		trsOBeg[getNumStates()] = trsOCnt;
		trsIOBeg[getNumStates()] = trsIOCnt;
		trsNPBeg[getNumStates()] = trsNPCnt;
	}

    final public PSEVMCreateData_CSR getCreateData_VM_CSR()
	{
		return getCreateData_VM_CSR(getDefaultUniformisationRate());
	}

	final public PSEVMCreateData_CSR getCreateData_VM_CSR(double q)
	{
	    final double qrec = 1.0 / q;

	    VectorOfDouble matIMinVal = new VectorOfDouble();
		VectorOfDouble matIMaxVal = new VectorOfDouble();
	    VectorOfInt matISrc = new VectorOfInt();
	    int[] matITrgBeg = new int [numStates + 1];
	    int matIPos = 0;

	    VectorOfDouble matNPVal = new VectorOfDouble();
	    VectorOfInt matNPSrc = new VectorOfInt();
	    int[] matNPTrgBeg = new int [numStates + 1];
	    int matPos = 0;

	    double[] matNPMinDiagVal = new double[numStates];
	    double[] matNPMaxDiagVal = new double[numStates];
	    for (int i = 0; i < numStates; ++i)
	    {
		    matNPMinDiagVal[i] = 1;
		    matNPMaxDiagVal[i] = 1;
	    }

	    VectorOfDouble matIOLowerVal0 = new VectorOfDouble();
	    VectorOfDouble matIOLowerVal1 = new VectorOfDouble();
	    VectorOfDouble matIOUpperVal0 = new VectorOfDouble();
	    VectorOfDouble matIOUpperVal1 = new VectorOfDouble();
	    VectorOfInt matIOSrc = new VectorOfInt();
	    int[] matIOTrgBeg = new int [numStates + 1];
	    int matIOPos = 0;

	    for (int state = 0; state < numStates; ++state)
	    {
		    matITrgBeg[state] = matIPos;
		    matNPTrgBeg[state] = matPos;
		    matIOTrgBeg[state] = matIOPos;

		    for (int i = trsIOBeg[state]; i < trsIOBeg[state + 1]; i += 2)
		    {
			    final int t0 = trsIO[i];
			    final int t1 = trsIO[i + 1];
			    final int v0 = trStSrc[t0];
			    final int v1 = trStTrg[t0]; // == trStSrc[t1]

			    final double valLower0 = trRateLower[t0] * trRatePopul[t0] * qrec;
			    final double valLower1 = trRateLower[t1] * trRatePopul[t1] * qrec;
			    final double valUpper0 = trRateUpper[t0] * trRatePopul[t0] * qrec;
			    final double valUpper1 = trRateUpper[t1] * trRatePopul[t1] * qrec;

			    // The rate params of t0 and t1 must be identical
			    // assert trRateLower[t0] == trRateLower[t1];
			    // assert trRateUpper[t0] == trRateUpper[t1];
			    //
			    // The lower rate == 0 iff upper rate == 0
			    // assert (trRateLower[t0] == 0 && trRateUpper[t0] == 0) ||
			    //        (trRateLower[t0] != 0 && trRateUpper[t0] != 0)
			    //

			    // if (valLower0 != 0) should be enough -- see above
			    if (!(valLower0 == 0 && valLower1 == 0 && valUpper0 == 0 && valUpper1 == 0))
			    {
				    matIOLowerVal0.pushBack(valLower0);
				    matIOLowerVal1.pushBack(valLower1);
				    matIOUpperVal0.pushBack(valUpper0);
				    matIOUpperVal1.pushBack(valUpper1);

				    matIOSrc.pushBack(v0);
				    ++matIOPos;
			    }
		    }

		    for (int i = trsIBeg[state]; i < trsIBeg[state + 1]; ++i)
		    {
				final int t = trsI[i];
			    final double valMin = trRateLower[t] * trRatePopul[t] * qrec;
			    final double valMax = trRateUpper[t] * trRatePopul[t] * qrec;
			    if (valMin != 0 || valMax != 0)
			    {
				    matIMinVal.pushBack(valMin);
				    matIMaxVal.pushBack(valMax);
				    matISrc.pushBack(trStSrc[t]);
				    ++matIPos;
			    }
		    }

		    for (int i = trsOBeg[state]; i < trsOBeg[state + 1]; ++i)
		    {
				final int t = trsO[i];
			    matNPMinDiagVal[trStSrc[t]] -= trRateUpper[t] * trRatePopul[t] * qrec;
			    matNPMaxDiagVal[trStSrc[t]] -= trRateLower[t] * trRatePopul[t] * qrec;
		    }

			for (int i = trsNPBeg[state]; i < trsNPBeg[state + 1]; ++i)
		    {
				final int t = trsNP[i];
			    final double val = trRateLower[t] * trRatePopul[t] * qrec;
			    matNPMinDiagVal[trStSrc[t]] -= val;
			    matNPMaxDiagVal[trStSrc[t]] -= val;
			    if (val != 0)
			    {
				    matNPVal.pushBack(val);
				    matNPSrc.pushBack(trStSrc[t]);
				    ++matPos;
			    }
		    }
	    }

	    matITrgBeg[numStates] = matIPos;
	    matNPTrgBeg[numStates] = matPos;
	    matIOTrgBeg[numStates] = matIOPos;

		return new PSEVMCreateData_CSR
			( numStates
			, matIOLowerVal0.data()
			, matIOLowerVal1.data()
			, matIOUpperVal0.data()
			, matIOUpperVal1.data()
			, matIOSrc.data()
			, matIOTrgBeg

			, matIMinVal.data()
			, matIMaxVal.data()
			, matISrc.data()
			, matITrgBeg

			, matNPMinDiagVal
			, matNPMaxDiagVal
			, matNPVal.data()
			, matNPSrc.data()
			, matNPTrgBeg
			);
	}

	final public PSEMVCreateData_ELL getCreateData_MV_ELL(BitSet subset, boolean complement)
	{
		return getCreateData_MV_ELL(subset, complement, getDefaultUniformisationRate(subset));
	}

	final public PSEMVCreateData_ELL getCreateData_MV_ELL(BitSet subset, boolean complement, double q)
	{
		final double qrec = 1.0 / q;
		subset = Utility.makeBitSetComplement(subset, complement, getNumStates());

		int totPNZ = 0;
		int totNNZ = 0;

		int matPColPerRow = 0;
		int matPRowCnt = 0;
		int matNColPerRow = 0;
		int matNRowCnt = 0;
		for (int state = subset.nextSetBit(0); state >= 0; state = subset.nextSetBit(state + 1)) {
			int matPNZ = 0;
			int matNNZ = 0;
			for (int t = stateBegin(state); t < stateEnd(state); ++t) {
				if (isParametrised(t)) {
					final double valLower = trRateLower[t] * trRatePopul[t] * qrec;
					final double valUpper = trRateUpper[t] * trRatePopul[t] * qrec;
					if (valLower != 0 || valUpper != 0) ++matPNZ;
				} else {
					final double val = trRateLower[t] * trRatePopul[t] * qrec;
					if (val != 0) ++matNNZ;
				}
			}
			if (matPNZ > 0) ++matPRowCnt;
			if (matNNZ > 0) ++matNRowCnt;
			totPNZ += matPNZ;
			totNNZ += matNNZ;
			matPColPerRow = Math.max(matPColPerRow, matPNZ);
			matNColPerRow = Math.max(matNColPerRow, matNNZ);
		}

		System.err.printf("totPNZ %s; totNNZ %s; totP %s; totN %s;\n",
			totPNZ, totNNZ, matPColPerRow * matPRowCnt, matNColPerRow * matNRowCnt);

		int matPValCnt = matPRowCnt * matPColPerRow;
		double[] matPValLower = new double[matPValCnt];
		double[] matPValUpper = new double[matPValCnt];
		int[] matPCol = new int[matPValCnt];
		int[] matPRow = new int[matPRowCnt];

		int matNValCnt = matNRowCnt * matNColPerRow;
		double[] matNVal = new double[matNValCnt];
		int[] matNCol = new int[matNValCnt];
		int[] matNRow = new int[matNRowCnt];

		int pr = 0;
		int nr = 0;
		for (int state = subset.nextSetBit(0); state >= 0; state = subset.nextSetBit(state + 1)) {
			int matPNZ = 0;
			int matNNZ = 0;
			for (int t = stateBegin(state); t < stateEnd(state); ++t) {
				if (isParametrised(t)) {
					final double valLower = trRateLower[t] * trRatePopul[t] * qrec;
					final double valUpper = trRateUpper[t] * trRatePopul[t] * qrec;
					final int col = trStTrg[t];
					if (valLower != 0 || valUpper != 0) {
						matPValLower[pr + matPRowCnt * matPNZ] = valLower;
						matPValUpper[pr + matPRowCnt * matPNZ] = valUpper;
						matPCol[pr + matPRowCnt * matPNZ] = col;
						++matPNZ;
					}
				} else {
					final double val = trRateLower[t] * trRatePopul[t] * qrec;
					final int col = trStTrg[t];
					if (val != 0) {
						matNVal[nr + matNRowCnt * matNNZ] = val;
						matNCol[nr + matNRowCnt * matNNZ] = col;
						++matNNZ;
					}
				}
			}
			if (matPNZ > 0) matPRow[pr++] = state;
			if (matNNZ > 0) matNRow[nr++] = state;
		}

		return new PSEMVCreateData_ELL(getNumStates(),
			matPValLower, matPValUpper, matPCol, matPRow, matPRowCnt, matPColPerRow,
			matNVal, matNCol, matNRow, matNRowCnt, matNColPerRow);
	}

	final public PSEMVCreateData_ELLFW getCreateData_MV_ELLFW(BitSet subset, boolean complement)
	{
		return getCreateData_MV_ELLFW(subset, complement, getDefaultUniformisationRate(subset));
	}

	final public PSEMVCreateData_ELLFW getCreateData_MV_ELLFW(BitSet subset, boolean complement, double q)
	{
		final double qrec = 1.0 / q;
		subset = Utility.makeBitSetComplement(subset, complement, getNumStates());

		final int warpSize = 32;
		final int rowCnt = subset.cardinality();

		int totNZ = 0;
		int totXX = 0;

		int matPRowCnt = rowCnt;
		VectorOfDouble matPValLower = new VectorOfDouble();
		VectorOfDouble matPValUpper = new VectorOfDouble();
		VectorOfInt matPCol = new VectorOfInt();
		VectorOfInt matPSegOff = new VectorOfInt();
		int[] matPRow = new int[matPRowCnt];

		int matNRowCnt = rowCnt;
		VectorOfDouble matNVal = new VectorOfDouble();
		VectorOfInt matNCol = new VectorOfInt();
		VectorOfInt matNSegOff = new VectorOfInt();
		int[] matNRow = new int[matNRowCnt];

		int pSegOff = 0;
		int nSegOff = 0;
		int rowId = 0;
		int row = subset.nextSetBit(0);
		for (int ii = 0; ii < rowCnt;) {
			final int stepSize = Math.min(warpSize, rowCnt - ii);

			int matPNZ = 0;
			int matNNZ = 0;

			int r = row;
			for (int is = 0; is < stepSize; ++is) {
				int pnz = 0;
				int nnz = 0;
				//System.err.printf("!XR ");
				for (int t = stateBegin(r); t < stateEnd(r); ++t) {
					if (isParametrised(t)) {
						final double valLower = trRateLower[t] * trRatePopul[t] * qrec;
						final double valUpper = trRateUpper[t] * trRatePopul[t] * qrec;
						if (valLower != 0 || valUpper != 0) ++pnz;
						//System.err.printf("%s ", valLower);
					} else {
						final double val = trRateLower[t] * trRatePopul[t] * qrec;
						if (val != 0) ++nnz;
					}
				}
				//System.err.printf("\n");
				totNZ += pnz;
				totNZ += nnz;
				matPNZ = Math.max(matPNZ, pnz);
				matNNZ = Math.max(matNNZ, nnz);
				r = subset.nextSetBit(r + 1);
			}

			matPValLower.pushBack(stepSize * matPNZ, 0);
			matPValUpper.pushBack(stepSize * matPNZ, 0);
			matPCol.pushBack(stepSize * matPNZ, 0);

			matNVal.pushBack(stepSize * matNNZ, 0);
			matNCol.pushBack(stepSize * matNNZ, 0);
			totXX = stepSize * matPNZ + stepSize * matNNZ;
			for (int is = 0; is < stepSize; ++is) {
				int pPos = pSegOff + is;
				int nPos = nSegOff + is;
				for (int t = stateBegin(row); t < stateEnd(row); ++t) {
					if (isParametrised(t)) {
						final double valLower = trRateLower[t] * trRatePopul[t] * qrec;
						final double valUpper = trRateUpper[t] * trRatePopul[t] * qrec;
						final int col = trStTrg[t];
						if (valLower != 0 || valUpper != 0) {
							matPValLower.at(pPos, valLower);
							matPValUpper.at(pPos, valUpper);
							matPCol.at(pPos, col);
							pPos += stepSize;
						}
					} else {
						final double val = trRateLower[t] * trRatePopul[t] * qrec;
						final int col = trStTrg[t];
						if (val != 0) {
							matNVal.at(nPos, val);
							matNCol.at(nPos, col);
							nPos += stepSize;
						}
					}
				}
				matNRow[rowId] = row;
				matPRow[rowId] = row;
				row = subset.nextSetBit(row + 1);
				++rowId;
			}
			matPSegOff.pushBack(pSegOff);
			matNSegOff.pushBack(nSegOff);
			pSegOff += stepSize * matPNZ;
			nSegOff += stepSize * matNNZ;

			ii += stepSize;
		}
		matPSegOff.pushBack(pSegOff);
		matNSegOff.pushBack(nSegOff);

		/*
		System.err.printf("!XV ");
		for (int i = 0; i < matPValLower.size(); ++i) {
			System.err.printf("%s ", matPValLower.data()[i]);
		}
		System.err.printf("\n");
		*/

		int matPN = matPSegOff.size() - 1;
		int matPNrem = ((matPRowCnt % warpSize) > 0) ? (matPRowCnt % warpSize) : warpSize;
		int matNN = matNSegOff.size() - 1;
		int matNNrem = ((matNRowCnt % warpSize) > 0) ? (matNRowCnt % warpSize) : warpSize;
		return new PSEMVCreateData_ELLFW(getNumStates(), warpSize,
			matPValLower.data(), matPValUpper.data(),
			matPCol.data(), matPRow, matPSegOff.data(),
			matPN, matPNrem, matPRowCnt, matPCol.size(),
			matNVal.data(),
			matNCol.data(), matNRow, matNSegOff.data(),
			matNN, matNNrem, matNRowCnt, matNCol.size());
	}

	final public PSEMVCreateData_CSR getCreateData_MV_CSR(BitSet subset, boolean complement)
	{
		return getCreateData_MV_CSR(subset, complement, getDefaultUniformisationRate(subset));
	}

	final public PSEMVCreateData_CSR getCreateData_MV_CSR(BitSet subset, boolean complement, double q)
	{
		final double qrec = 1.0 / q;
		subset = Utility.makeBitSetComplement(subset, complement, getNumStates());

		VectorOfDouble matPValLower = new VectorOfDouble();
		VectorOfDouble matPValUpper = new VectorOfDouble();
		VectorOfInt matPCol = new VectorOfInt();
		int matPRowCntMax = subset.cardinality();
		int[] matPRow = new int [matPRowCntMax];
		int[] matPRowBeg = new int [matPRowCntMax + 1];
		int matPPos = 0;

		VectorOfDouble matNPVal = new VectorOfDouble();
		VectorOfInt matNPCol = new VectorOfInt();
		int matNPRowCntMax = subset.cardinality();
		int[] matNPRow = new int [matNPRowCntMax];
		int[] matNPRowBeg = new int [matNPRowCntMax + 1];
		int matNPPos = 0;

		int matPRowCnt = 0;
		int matNPRowCnt = 0;
		for (int state = subset.nextSetBit(0); state >= 0; state = subset.nextSetBit(state + 1))
		{
			matPRow[matPRowCnt] = state;
			matPRowBeg[matPRowCnt] = matPPos;
			matNPRow[matNPRowCnt] = state;
			matNPRowBeg[matNPRowCnt] = matNPPos;

			boolean matP = false;
			boolean matNP = false;
			for (int t = stateBegin(state); t < stateEnd(state); ++t) {
				if (isParametrised(t)) {
					final double valLower = trRateLower[t] * trRatePopul[t] * qrec;
					final double valUpper = trRateUpper[t] * trRatePopul[t] * qrec;
					final int col = trStTrg[t];

					if (!(valLower == 0 && valUpper == 0)) {
						matP = true;
						matPValLower.pushBack(valLower);
						matPValUpper.pushBack(valUpper);
						matPCol.pushBack(col);
						++matPPos;
					}
				} else {
					final double val = trRateLower[t] * trRatePopul[t] * qrec;
					final int col = trStTrg[t];

					if (val != 0) {
						matNP = true;
						matNPVal.pushBack(val);
						matNPCol.pushBack(col);
						++matNPPos;
					}
				}
			}

			if (matP) {
				++matPRowCnt;
			}
			if (matNP) {
				++matNPRowCnt;
			}
		}
		matPRowBeg[matPRowCnt] = matPPos;
		matNPRowBeg[matNPRowCnt] = matNPPos;

		return new PSEMVCreateData_CSR(numStates,
				matPValLower.data(), matPValUpper.data(), matPCol.data(), matPRow, matPRowBeg, matPRowCnt,
				matNPVal.data(), matNPCol.data(), matNPRow, matNPRowBeg, matNPRowCnt);
	}

	/**
	 * Does a vector-matrix multiplication for this parametrised CTMC's transition
	 * probability matrix (uniformised with rate {@code q}) and the vector's min/max
	 * components ({@code vectMin} and {@code vectMax}, respectively) passed in.
	 * The code follows closely the algorithm described in the article:
	 * <p>
	 * L. Brim‚ M. Češka‚ S. Dražan and D. Šafránek: Exploring Parameter Space
	 * of Stochastic Biochemical Systems Using Quantitative Model Checking
	 * In Computer Aided Verification (CAV'13): 107−123, 2013.
	 * 
	 * @param vectMin vector to multiply by when computing minimised result
	 * @param resultMin vector to store minimised result in
	 * @param vectMax vector to multiply by when computing maximised result
	 * @param resultMax vector to store maximised result in
	 * @see #mvMult(double[], double[], double[], double[])
	 */
	//public void vmMult(double vectMin[], double resultMin[], double vectMax[], double resultMax[])

	/**
	 * Does a matrix-vector multiplication for this parametrised CTMC's transition
	 * probability matrix (uniformised with rate {@code q}) and the vector's min/max
	 * components ({@code vectMin} and {@code vectMax}, respectively) passed in.
	 * <p>
	 * NB: Semantics of {@code mult} is <i>not</i> analogical to that of {@code vmMult},
	 * the difference is crucial:  {@code result[k]_i} in {@code vmMult} is simply
	 * the probability of being in state {@code k} after {@code i} iterations starting
	 * from the initial state.  On the other hand, {@code mult}'s {@code result[k]_i}
	 * denotes the probability that an absorbing state (i.e., a state not in {@code subset})
	 * is reached after {@code i} iterations starting from {@code k}.
	 *
	 * @param vectMin vector to multiply by when computing minimised result
	 * @param resultMin vector to store minimised result in
	 * @param vectMax vector to multiply by when computing maximised result
	 * @param resultMax vector to store maximised result in
	 * @see #vmMult(double[], double[], double[], double[])
	 */
	//public void mvMult(double vectMin[], double resultMin[], double vectMax[], double resultMax[])

	/**
	 * Updates the transition rates and other parametrised data
	 * of this parametrised CTMC according to the given parameter region.
	 * 
	 * @param region parameter region according to which configure the model's
	 * parameter space
	 * @throws PrismException thrown if rates cannot be evaluated with the new
	 * parameter region's bounds
	 */
	public void evaluateParameters(BoxRegion region) throws PrismException
	{
		for (int trans = 0; trans < numTransitions; trans++) {
			trRateLower[trans] = rateParams[trans].evaluateDouble(region.getLowerBounds());
			trRateUpper[trans] = rateParams[trans].evaluateDouble(region.getUpperBounds());
		}
		if (multOptions.getAdaptiveFoxGlynn()) {
			for (int state = 0; state < numStates; state++) {
				exitRates[state] = exitRatesExpr[state].evaluateDouble(region.getUpperBounds());
			}
		}
	}

	/**
	 */
	public void setParameterSpace(BoxRegion region) throws PrismException
	{
		completeSpace = region;
		if (!multOptions.getAdaptiveFoxGlynn()) {
			for (int state = 0; state < numStates; state++) {
				exitRates[state] = exitRatesExpr[state].evaluateDouble(region.getUpperBounds());
			}
		}
		evaluateParameters(region);
		for (int trans = 0; trans < numTransitions; trans++) {
			parametrisedTransitions[trans] = trRateLower[trans] != trRateUpper[trans];
		}
	}


	public double getTrRateLower (int tran){
		return trRateLower[tran];
	}
	

	public double getTrRateUpper (int tran){
		return trRateUpper[tran];
	}
}
