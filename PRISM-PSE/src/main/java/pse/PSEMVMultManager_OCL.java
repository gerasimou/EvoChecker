package pse;

import java.util.BitSet;

final public class PSEMVMultManager_OCL implements PSEMultManager<PSEMVMult_OCL>
{
    public PSEMVMultManager_OCL(PSEModel model, BitSet modelSubset, boolean modelSubsetComplement)
    {
        this.model = model;
        this.modelSubset = Utility.makeBitSetCopy(modelSubset, model.getNumStates());
        this.modelSubsetComplement = modelSubsetComplement;

        this.releaser = new Releaser();
    }

    @Override
    final public void update(PSEMVMult_OCL mult)
    {
        mult.update(model.getCreateData_MV_CSR(modelSubset, modelSubsetComplement));
    }

    @Override
    final public PSEMVMult_OCL create()
    {
        return createGroup(1)[0];
    }

    @Override
    public PSEMVMult_OCL[] createGroup(int n)
    {
        PSEMVCreateData_CSR data = model.getCreateData_MV_CSR(modelSubset, modelSubsetComplement);
        PSEMVMultSettings_OCL multOpts = PSEMVMultSettings_OCL.Default();
        releaser.releaseLater(multOpts);
        PSEMVMultTopology_OCL multTopo = new PSEMVMultTopology_OCL(data, multOpts.clContext);
        releaser.releaseLater(multTopo);

        PSEMVMult_OCL[] group = new PSEMVMult_OCL[n];
        for (int i = 0; i < n; ++i) {
            group[i] = new PSEMVMult_OCL(multOpts, multTopo, data);
            releaser.releaseLater(group[i]);
        }

        return group;
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
