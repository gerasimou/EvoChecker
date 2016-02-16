package pse;

import prism.PrismLog;

import java.util.BitSet;
import java.util.HashMap;

final public class PSEFoxGlynnManager implements Releaseable
{
	public PSEFoxGlynnManager()
	{
		pseFoxGlynnVM = null;
		pseFoxGlynnMV = new HashMap<BitSet, PSEFoxGlynn>();
		releaser = new Releaser();
		options = PSEMultUtility.getOptions();
	}

	@Override
	public final void release()
	{
		releaser.release();
	}

	public final PSEFoxGlynn getFoxGlynnVM(PSEModel model,
		int iterStep, PrismLog prismLog)
	{
		if (pseFoxGlynnVM == null) {
			if (options.getMany() > 0) {
				PSEMultManyManager manager = getMultManyManagerVM(options, model);
				releaser.releaseLater(manager);
				pseFoxGlynnVM = new PSEFoxGlynnMany(options, model, manager, iterStep, prismLog);
			} else if (options.getPara() > 0) {
				PSEMultManager manager = getMultManagerVM(options, model);
				releaser.releaseLater(manager);
				pseFoxGlynnVM = new PSEFoxGlynnParallel(options, model, manager, iterStep, prismLog);
			} else {
				PSEMultManager manager = getMultManagerVM(options, model);
				releaser.releaseLater(manager);
				pseFoxGlynnVM = new PSEFoxGlynnSimple(options, model, manager, iterStep, prismLog);
			}
		}
		return pseFoxGlynnVM;
	}

	public final PSEFoxGlynn getFoxGlynnMV(PSEModel model, BitSet subset, boolean complement,
		int iterStep, PrismLog prismLog)
	{
		BitSet key = Utility.makeBitSetComplement(subset, complement, model.getNumStates());
		PSEFoxGlynn res = pseFoxGlynnMV.get(key);
		if (res == null) {
			if (options.getMany() > 0) {
				PSEMultManyManager manager = getMultManyManagerMV(options, model, subset, complement);
				releaser.releaseLater(manager);
				res = new PSEFoxGlynnMany(options, model, manager, iterStep, prismLog);
			} else if (options.getPara() > 0) {
				PSEMultManager manager = getMultManagerMV(options, model, subset, complement);
				releaser.releaseLater(manager);
				res = new PSEFoxGlynnParallel(options, model, manager, iterStep, prismLog);
			} else {
				PSEMultManager manager = getMultManagerMV(options, model, subset, complement);
				releaser.releaseLater(manager);
				res = new PSEFoxGlynnSimple(options, model, manager, iterStep, prismLog);
			}
			pseFoxGlynnMV.put(key, res);
		}
		return res;
	}

	private final static PSEMultManager getMultManagerVM(PSEMultOptions options, PSEModel model)
	{
		if (options.getOcl()) {
			return new PSEVMMultManager_OCL(model);
		} else {
			return new PSEVMMultManager_CPU(model);
		}
	}

	private final static PSEMultManager getMultManagerMV(PSEMultOptions options, PSEModel model, BitSet modelSubset, boolean modelSubsetComplement)
	{
		if (options.getOcl()) {
			return new PSEMVMultManager_OCL(model, modelSubset, modelSubsetComplement);
		} else {
			return new PSEMVMultManager_CPU(model, modelSubset, modelSubsetComplement);
		}
	}

	private final static PSEMultManyManager getMultManyManagerVM(PSEMultOptions options, PSEModel model)
	{
		if (options.getOcl()) {
			return new PSEVMMultManyManager_OCL(model);
		}
		return null;
	}

	private final static PSEMultManyManager getMultManyManagerMV(PSEMultOptions options, PSEModel model, BitSet modelSubset, boolean modelSubsetComplement)
	{
		if (options.getOcl()) {
			switch (options.getFmt()) {
			case CSR:
				return new PSEMVMultManyManager_OCL(model, modelSubset, modelSubsetComplement);
			case ELL:
				return new PSEMVMultManyManager_ELL_OCL(model, modelSubset, modelSubsetComplement);
			case ELLFW:
				return new PSEMVMultManyManager_ELLFW_OCL(model, modelSubset, modelSubsetComplement);
			}
		}
		return null;
	}

	private PSEFoxGlynn pseFoxGlynnVM;
	private HashMap<BitSet, PSEFoxGlynn> pseFoxGlynnMV;
	final private Releaser releaser;
	final private PSEMultOptions options;
}
