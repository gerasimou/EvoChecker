package pse;

import org.jocl.*;
import static org.jocl.CL.*;

final public class PSEMVMultTopology_OCL implements Releaseable
{
	public PSEMVMultTopology_OCL(PSEMVCreateData_CSR data, cl_context clContext)
	{
		this.stCnt = data.stCnt;
		this.matIOColHost = data.matIOCol;
		this.matIORowHost = data.matIORow;
		this.matPRowBegHost = data.matIORowBeg;
		this.matPRowCnt = data.matIORowCnt;

		this.matNPColHost = data.matNPCol;
		this.matNPRowHost = data.matNPRow;
		this.matNPRowBegHost = data.matNPRowBeg;
		this.matNPRowCnt = data.matNPRowCnt;

		this.enabledMatIO = matPRowCnt > 0 && matPRowBegHost[matPRowCnt] > 0;
		this.enabledMatNP = matNPRowCnt > 0 && matNPRowBegHost[matNPRowCnt] > 0;

		if (enabledMatIO) {
			final Pointer matIOCol_ = Pointer.to(data.matIOCol);
			final Pointer matIORow_ = Pointer.to(data.matIORow);
			final Pointer matIORowBeg_ = Pointer.to(data.matIORowBeg);

			this.matPCol = clCreateBuffer(clContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
				Sizeof.cl_uint * matPRowBegHost[matPRowCnt], matIOCol_, null);
			this.matPRow = clCreateBuffer(clContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
				Sizeof.cl_uint * matPRowCnt, matIORow_, null);
			this.matPRowBeg = clCreateBuffer(clContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
				Sizeof.cl_uint * (matPRowCnt + 1), matIORowBeg_, null);
		}

		if (enabledMatNP) {
			final Pointer matNPCol_ = Pointer.to(data.matNPCol);
			final Pointer matNPRow_ = Pointer.to(data.matNPRow);
			final Pointer matNPRowBeg_ = Pointer.to(data.matNPRowBeg);

			this.matNPCol = clCreateBuffer(clContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
				Sizeof.cl_uint * matNPRowBegHost[matNPRowCnt], matNPCol_, null);
			this.matNPRow = clCreateBuffer(clContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
				Sizeof.cl_uint * matNPRowCnt, matNPRow_, null);
			this.matNPRowBeg = clCreateBuffer(clContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
				Sizeof.cl_uint * (matNPRowCnt + 1), matNPRowBeg_, null);
		}
	}

	final public void release()
	{
		if (enabledMatNP) {
			clReleaseMemObject(matNPRowBeg);
			clReleaseMemObject(matNPRow);
			clReleaseMemObject(matNPCol);
		}
		if (enabledMatIO) {
			clReleaseMemObject(matPRowBeg);
			clReleaseMemObject(matPRow);
			clReleaseMemObject(matPCol);
		}
	}

	final public int stCnt;

	final public int[] matIOColHost;
	final public int[] matIORowHost;
	final public int[] matPRowBegHost;

	final public int[] matNPColHost;
	final public int[] matNPRowHost;
	final public int[] matNPRowBegHost;

	final public int matPRowCnt;
	public cl_mem matPCol;
	public cl_mem matPRow;
	public cl_mem matPRowBeg;
	final public boolean enabledMatIO;

	final public int matNPRowCnt;
	public cl_mem matNPCol;
	public cl_mem matNPRow;
	public cl_mem matNPRowBeg;
	final public boolean enabledMatNP;
}
