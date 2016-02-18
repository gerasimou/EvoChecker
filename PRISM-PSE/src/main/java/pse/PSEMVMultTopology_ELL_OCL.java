package pse;

import static org.jocl.CL.CL_MEM_COPY_HOST_PTR;
import static org.jocl.CL.CL_MEM_READ_ONLY;
import static org.jocl.CL.clCreateBuffer;
import static org.jocl.CL.clReleaseMemObject;

import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_context;
import org.jocl.cl_mem;

final public class PSEMVMultTopology_ELL_OCL implements Releaseable
{
	public PSEMVMultTopology_ELL_OCL(PSEMVCreateData_ELL data, cl_context clContext)
	{
		this.stCnt = data.stCnt;

		this.matPRowCnt = data.matPRowCnt;
		this.matPColPerRow = data.matPColPerRow;
		this.matPEnabled = data.matPRowCnt * matPColPerRow > 0;

		if (matPEnabled) {
			final Pointer matPCol_ = Pointer.to(data.matPCol);
			final Pointer matPRow_ = Pointer.to(data.matPRow);
			this.matPCol = clCreateBuffer(clContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_uint * matPRowCnt * matPColPerRow, matPCol_, null);
			this.matPRow = clCreateBuffer(clContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_uint * matPRowCnt, matPRow_, null);
		}

		this.matNRowCnt = data.matNRowCnt;
		this.matNColPerRow = data.matNColPerRow;
		this.matNEnabled = data.matNRowCnt * matNColPerRow > 0;

		if (matNEnabled) {
			final Pointer matNCol_ = Pointer.to(data.matNCol);
			final Pointer matNRow_ = Pointer.to(data.matNRow);
			this.matNCol = clCreateBuffer(clContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_uint * matNRowCnt * matNColPerRow, matNCol_, null);
			this.matNRow = clCreateBuffer(clContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_uint * matNRowCnt, matNRow_, null);
		}
	}

	final public void release()
	{
		if (matNEnabled) {
			clReleaseMemObject(matNCol);
			clReleaseMemObject(matNRow);
		}

		if (matPEnabled) {
			clReleaseMemObject(matPCol);
			clReleaseMemObject(matPRow);
		}
	}

	final public int stCnt;

	final public boolean matNEnabled;
	final public int matNRowCnt;
	final public int matNColPerRow;
	public cl_mem matNCol;
	public cl_mem matNRow;

	final public boolean matPEnabled;
	final public int matPRowCnt;
	final public int matPColPerRow;
	public cl_mem matPCol;
	public cl_mem matPRow;
}
