package pse;

public class PSEVMMultManyManager_OCL implements PSEMultManyManager<PSEVMMultMany_OCL>
{

	public PSEVMMultManyManager_OCL(PSEModel model)
	{
		this.model = model;
		this.releaser = new Releaser();
	}

	@Override
	final public void update(int matId, PSEVMMultMany_OCL mult, double q)
	{
		mult.update(matId, model.getCreateData_VM_CSR(q));
	}

	@Override
	final public PSEVMMultMany_OCL create(int matCnt)
	{
		PSEVMCreateData_CSR data = model.getCreateData_VM_CSR();
		PSEVMMultSettings_OCL multOpts = PSEVMMultSettings_OCL.Default();
		releaser.releaseLater(multOpts);
		PSEVMMultTopology_OCL multTopo = new PSEVMMultTopology_OCL(data, multOpts.clContext);
		releaser.releaseLater(multTopo);

		PSEVMMultMany_OCL mult = new PSEVMMultMany_OCL(multOpts, multTopo, matCnt);
		releaser.releaseLater(mult);
		return mult;
	}

	@Override
	final public void release()
	{
		releaser.release();
	}

	final private PSEModel model;
	final private Releaser releaser;
}
