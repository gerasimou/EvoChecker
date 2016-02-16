package pse;

import java.util.BitSet;

public class PSEMVMultManyManager_ELL_OCL implements PSEMultManyManager<PSEMVMultMany_ELL_OCL>
{

	public PSEMVMultManyManager_ELL_OCL(PSEModel model, BitSet modelSubset, boolean modelSubsetComplement)
	{
		this.model = model;
		this.modelSubset = Utility.makeBitSetCopy(modelSubset, model.getNumStates());
		this.modelSubsetComplement = modelSubsetComplement;

		this.releaser = new Releaser();
	}

	@Override
	final public void update(int matId, PSEMVMultMany_ELL_OCL mult, double q)
	{
		mult.update(matId, model.getCreateData_MV_ELL(modelSubset, modelSubsetComplement, q));
	}

	@Override
	final public PSEMVMultMany_ELL_OCL create(int matCnt)
	{
		PSEMVCreateData_ELL data = model.getCreateData_MV_ELL(modelSubset, modelSubsetComplement);
		PSEMVMultSettings_OCL multOpts = PSEMVMultSettings_OCL.Default();
		releaser.releaseLater(multOpts);
		PSEMVMultTopology_ELL_OCL multTopo = new PSEMVMultTopology_ELL_OCL(data, multOpts.clContext);
		releaser.releaseLater(multTopo);

		PSEMVMultMany_ELL_OCL mult = new PSEMVMultMany_ELL_OCL(multOpts, multTopo, matCnt);
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
