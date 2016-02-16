package pse;

import java.util.BitSet;

public class PSEMVMultManyManager_ELLFW_OCL implements PSEMultManyManager<PSEMVMultMany_ELLFW_OCL>
{

	public PSEMVMultManyManager_ELLFW_OCL(PSEModel model, BitSet modelSubset, boolean modelSubsetComplement)
	{
		this.model = model;
		this.modelSubset = Utility.makeBitSetCopy(modelSubset, model.getNumStates());
		this.modelSubsetComplement = modelSubsetComplement;

		this.releaser = new Releaser();
	}

	@Override
	final public void update(int matId, PSEMVMultMany_ELLFW_OCL mult, double q)
	{
		mult.update(matId, model.getCreateData_MV_ELLFW(modelSubset, modelSubsetComplement, q));
	}

	@Override
	final public PSEMVMultMany_ELLFW_OCL create(int matCnt)
	{
		PSEMVCreateData_ELLFW data = model.getCreateData_MV_ELLFW(modelSubset, modelSubsetComplement);
		PSEMVMultSettings_OCL multOpts = PSEMVMultSettings_OCL.Default();
		releaser.releaseLater(multOpts);
		PSEMVMultTopology_ELLFW_OCL multTopo = new PSEMVMultTopology_ELLFW_OCL(data, multOpts.clContext);
		releaser.releaseLater(multTopo);

		PSEMVMultMany_ELLFW_OCL mult = new PSEMVMultMany_ELLFW_OCL(multOpts, multTopo, matCnt);
		releaser.releaseLater(mult);
		return mult;
	}

	@Override
	final public void release()
	{
		releaser.release();
	}

	final private PSEModel model;
	final private BitSet modelSubset;
	final private boolean modelSubsetComplement;

	final private Releaser releaser;
}
