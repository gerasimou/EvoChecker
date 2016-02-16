package pse;

import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_context;
import org.jocl.cl_mem;

import static org.jocl.CL.*;

final public class PSEMVMultTopology_ELLFW_OCL implements Releaseable
{
	public PSEMVMultTopology_ELLFW_OCL(PSEMVCreateData_ELLFW data, cl_context clContext)
	{
		this.stCnt = data.stCnt;
		this.warpSize = data.warpSize;

		this.matPN = data.matPN;
		this.matPNrem = data.matPNrem;
		this.matPRowCnt = data.matPRowCnt;
		this.matPValCnt = data.matPValCnt;
		this.matPEnabled = matPValCnt > 0;

		if (matPEnabled) {
			final Pointer matPCol_ = Pointer.to(data.matPCol);
			final Pointer matPRow_ = Pointer.to(data.matPRow);
			final Pointer matPSegOff_ = Pointer.to(data.matPSegOff);
			this.matPCol = clCreateBuffer(clContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_uint * matPValCnt, matPCol_, null);
			this.matPRow = clCreateBuffer(clContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_uint * matPRowCnt, matPRow_, null);
			this.matPSegOff = clCreateBuffer(clContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_uint * (matPN + 1), matPSegOff_, null);
		}

		this.matNN = data.matNN;
		this.matNNrem = data.matNNrem;
		this.matNRowCnt = data.matNRowCnt;
		this.matNValCnt = data.matNValCnt;
		this.matNEnabled = matNValCnt > 0;

		if (matNEnabled) {
			final Pointer matNCol_ = Pointer.to(data.matNCol);
			final Pointer matNRow_ = Pointer.to(data.matNRow);
			final Pointer matNSegOff_ = Pointer.to(data.matNSegOff);
			this.matNCol = clCreateBuffer(clContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_uint * matNValCnt, matNCol_, null);
			this.matNRow = clCreateBuffer(clContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_uint * matNRowCnt, matNRow_, null);
			this.matNSegOff = clCreateBuffer(clContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_uint * (matNN + 1), matNSegOff_, null);
		}
	}

	final public void release()
	{
		if (matNEnabled) {
			clReleaseMemObject(matNCol);
			clReleaseMemObject(matNRow);
			clReleaseMemObject(matNSegOff);
		}

		if (matPEnabled) {
			clReleaseMemObject(matPCol);
			clReleaseMemObject(matPRow);
			clReleaseMemObject(matPSegOff);
		}
	}

	final public int stCnt;
	final public int warpSize;

	final public boolean matNEnabled;
	final public int matNN;
	final public int matNNrem;
	public cl_mem matNCol;
	public cl_mem matNRow;
	public cl_mem matNSegOff;
	public final int matNRowCnt;
	public final int matNValCnt;

	final public boolean matPEnabled;
	final public int matPN;
	final public int matPNrem;
	public cl_mem matPCol;
	public cl_mem matPRow;
	public cl_mem matPSegOff;
	public final int matPRowCnt;
	public final int matPValCnt;
}
