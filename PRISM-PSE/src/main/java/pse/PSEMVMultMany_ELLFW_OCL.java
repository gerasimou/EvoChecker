package pse;

import org.jocl.*;

import static org.jocl.CL.*;

public final class PSEMVMultMany_ELLFW_OCL implements PSEMultMany, Releaseable
{
	public PSEMVMultMany_ELLFW_OCL(PSEMVMultSettings_OCL opts, PSEMVMultTopology_ELLFW_OCL topo, int matCnt)
	{
		this.matCnt = matCnt;
		this.stCnt = topo.stCnt;
		this.topo = topo;
		this.opts = opts;

		matPRowCntPadded = topo.matPRowCnt + topo.warpSize - topo.matPNrem;
		matNRowCntPadded = topo.matNRowCnt + topo.warpSize - topo.matNNrem;

		this.clCommandQueue = clCreateCommandQueue(clContext(), opts.clDeviceIdMax, 0, null);

		setExceptionsEnabled(true);

		this.totalIterationCnt = 0;

		clProgram = OCLProgram.createProgram(OCLProgram.SOURCE, clContext());

		if (topo.matNEnabled) {
			this.matNInitialized = false;

			clKernelMatN = OCLProgram.createKernel("PSE_MV_N_ELLFW_MANY", clProgram);
			final int len = Sizeof.cl_double * topo.matNValCnt;
			matNVal = clCreateBuffer(clContext(), CL_MEM_READ_ONLY, len, null, null);

			clSetKernelArg(clKernelMatN, 1, Sizeof.cl_uint, Pointer.to(new int[]{topo.warpSize}));
			clSetKernelArg(clKernelMatN, 2, Sizeof.cl_uint, Pointer.to(new int[]{topo.matNN}));
			clSetKernelArg(clKernelMatN, 3, Sizeof.cl_uint, Pointer.to(new int[]{topo.matNNrem}));
			clSetKernelArg(clKernelMatN, 4, Sizeof.cl_uint, Pointer.to(new int[]{topo.stCnt}));
			clSetKernelArg(clKernelMatN, 5, Sizeof.cl_uint, Pointer.to(new int[]{matNRowCntPadded}));
			clSetKernelArg(clKernelMatN, 6, Sizeof.cl_mem, Pointer.to(this.matNVal));
			clSetKernelArg(clKernelMatN, 7, Sizeof.cl_mem, Pointer.to(topo.matNCol));
			clSetKernelArg(clKernelMatN, 8, Sizeof.cl_mem, Pointer.to(topo.matNRow));
			clSetKernelArg(clKernelMatN, 9, Sizeof.cl_mem, Pointer.to(topo.matNSegOff));
		}

		if (topo.matPEnabled) {
			clKernelMatP = OCLProgram.createKernel("PSE_MV_P_ELLFW_MANY", clProgram);
			final int len = Sizeof.cl_double * topo.matPValCnt * matCnt;
			this.matPLowerVal = clCreateBuffer(clContext(), CL_MEM_READ_ONLY, len, null, null);
			this.matPUpperVal = clCreateBuffer(clContext(), CL_MEM_READ_ONLY, len, null, null);

			clSetKernelArg(clKernelMatP, 1, Sizeof.cl_uint, Pointer.to(new int[]{topo.warpSize}));
			clSetKernelArg(clKernelMatP, 2, Sizeof.cl_uint, Pointer.to(new int[]{topo.matPN}));
			clSetKernelArg(clKernelMatP, 3, Sizeof.cl_uint, Pointer.to(new int[]{topo.matPNrem}));
			clSetKernelArg(clKernelMatP, 4, Sizeof.cl_uint, Pointer.to(new int[]{topo.stCnt}));
			clSetKernelArg(clKernelMatP, 5, Sizeof.cl_uint, Pointer.to(new int[]{matPRowCntPadded}));
			clSetKernelArg(clKernelMatP, 6, Sizeof.cl_mem, Pointer.to(this.matPLowerVal));
			clSetKernelArg(clKernelMatP, 7, Sizeof.cl_mem, Pointer.to(this.matPUpperVal));
			clSetKernelArg(clKernelMatP, 8, Sizeof.cl_mem, Pointer.to(topo.matPCol));
			clSetKernelArg(clKernelMatP, 9, Sizeof.cl_mem, Pointer.to(topo.matPRow));
			clSetKernelArg(clKernelMatP, 10, Sizeof.cl_mem, Pointer.to(topo.matPSegOff));
		}

		{
			final int len = Sizeof.cl_double * stCnt * matCnt;
			clKernelSum = OCLProgram.createKernel("WeightedSumToBoth", clProgram);
			this.sumMin = clCreateBuffer(clContext(), CL_MEM_READ_WRITE, len, null, null);
			this.sumMax = clCreateBuffer(clContext(), CL_MEM_READ_WRITE, len, null, null);
			clSetKernelArg(clKernelSum, 4, Sizeof.cl_mem, Pointer.to(this.sumMin));
			clSetKernelArg(clKernelSum, 5, Sizeof.cl_mem, Pointer.to(this.sumMax));

			minMem = clCreateBuffer(clContext(), CL_MEM_READ_WRITE, len, null, null);
			resMinMem = clCreateBuffer(clContext(), CL_MEM_READ_WRITE, len, null, null);
			maxMem = clCreateBuffer(clContext(), CL_MEM_READ_WRITE, len, null, null);
			resMaxMem = clCreateBuffer(clContext(), CL_MEM_READ_WRITE, len, null, null);
		}
	}

	@Override
	final public void setWeight(double[] weight, double weightDef, int weightOff)
	{
		this.weight = weight;
		this.weightDef = weightDef;
		this.weightOff = weightOff;
	}

	final public void update(int matId, PSEMVCreateData_ELLFW data)
	{
		assert(matId < matCnt);

		totalIterationCnt = 0;

		if (topo.matNEnabled) {
			final int len = Sizeof.cl_double * topo.matNValCnt;
			clEnqueueWriteBuffer(clCommandQueue(), matNVal, false, 0, len, Pointer.to(data.matNVal), 0, null, null);
			matNInitialized = true;
		}

		if (topo.matPEnabled) {
			final int len = Sizeof.cl_double * topo.matPValCnt;
			final int off = matId * len;

			final Pointer matPLowerVal_ = Pointer.to(data.matPLowerVal);
			final Pointer matPUpperVal_ = Pointer.to(data.matPUpperVal);
			clEnqueueWriteBuffer(clCommandQueue(), matPLowerVal, false, off, len, matPLowerVal_, 0, null, null);
			clEnqueueWriteBuffer(clCommandQueue(), matPUpperVal, false, off, len, matPUpperVal_, 0, null, null);
		}

		{
			final int len = Sizeof.cl_double * stCnt;
			final int off = matId * len;

			final double[] zeroes = new double[stCnt];
			final Pointer zeroes_ = Pointer.to(zeroes);
			clEnqueueWriteBuffer(clCommandQueue(), sumMin, false, off, len, zeroes_, 0, null, null);
			clEnqueueWriteBuffer(clCommandQueue(), sumMax, false, off, len, zeroes_, 0, null, null);
		}
	}

	@Override
	final public void release()
	{
		if (topo.matPEnabled) {
			clReleaseKernel(clKernelMatP);
			clReleaseMemObject(matPLowerVal);
			clReleaseMemObject(matPUpperVal);
		}

		if (topo.matNEnabled) {
			clReleaseKernel(clKernelMatN);
			clReleaseMemObject(matNVal);
		}

		clReleaseMemObject(maxMem);
		clReleaseMemObject(resMaxMem);
		clReleaseMemObject(minMem);
		clReleaseMemObject(resMinMem);

		clReleaseKernel(clKernelSum);
		clReleaseMemObject(sumMin);
		clReleaseMemObject(sumMax);

		clReleaseCommandQueue(clCommandQueue);
		clReleaseProgram(clProgram);
	}

	@Override
	final public void getSum(int matId, final double[] sumMin, final double[] sumMax)
	{
		final int len = Sizeof.cl_double * stCnt;
		final int off = matId * len;
		clEnqueueReadBuffer(clCommandQueue(), this.sumMin, true, off, len, Pointer.to(sumMin), 0, null, null);
		clEnqueueReadBuffer(clCommandQueue(), this.sumMax, true, off, len, Pointer.to(sumMax), 0, null, null);
		clFinish(clCommandQueue());
	}

	@Override
	final public void setSum(int matId, final double[] sumMin, final double[] sumMax)
	{
		final int len = Sizeof.cl_double * stCnt;
		final int off = matId * len;
		clEnqueueWriteBuffer(clCommandQueue(), this.sumMin, false, off, len, Pointer.to(sumMin), 0, null, null);
		clEnqueueWriteBuffer(clCommandQueue(), this.sumMax, false, off, len, Pointer.to(sumMax), 0, null, null);
	}

	@Override
	final public void getMult(int matId, final double resMin[], final double resMax[])
	{
		final int len = Sizeof.cl_double * stCnt;
		final int off = matId * len;
		clEnqueueReadBuffer(clCommandQueue(), minMem, true, off, len, Pointer.to(resMin), 0, null, null);
		clEnqueueReadBuffer(clCommandQueue(), maxMem, true, off, len, Pointer.to(resMax), 0, null, null);
		clFinish(clCommandQueue());
	}

	@Override
	final public void setMult(int matId, final double min[], final double max[])
	{
		final int len = Sizeof.cl_double * stCnt;
		final int off = matId * len;
		clEnqueueWriteBuffer(clCommandQueue(), minMem, true, off, len, Pointer.to(min), 0, null, null);
		clEnqueueWriteBuffer(clCommandQueue(), maxMem, true, off, len, Pointer.to(max), 0, null, null);
	}

	@Override
	final public void mult(int matCnt, int iterationCnt)
	{
		assert(matCnt <= this.matCnt);

		final long[] lws = new long[]{OCLProgram.localWorkSize(64)};
		final long[] gwsP = new long[]{Utility.leastGreaterMultiple(matPRowCntPadded * matCnt, lws[0])};
		final long[] gwsNP = new long[]{Utility.leastGreaterMultiple(matNRowCntPadded * matCnt, lws[0])};
		final long[] gwsSum = new long[]{Utility.leastGreaterMultiple(stCnt * matCnt, lws[0])};

		if (topo.matPEnabled) clSetKernelArg(clKernelMatP, 0, Sizeof.cl_uint, Pointer.to(new int[]{matCnt}));
		if (topo.matNEnabled) clSetKernelArg(clKernelMatN, 0, Sizeof.cl_uint, Pointer.to(new int[]{matCnt}));
		clSetKernelArg(clKernelSum, 0, Sizeof.cl_uint, Pointer.to(new int[]{stCnt * matCnt}));
		for (int i = 0; i < iterationCnt; ++i) {
			final int len = Sizeof.cl_double * stCnt * matCnt;
			clEnqueueCopyBuffer(clCommandQueue(), minMem, resMinMem, 0, 0, len, 0, null, null);
			clEnqueueCopyBuffer(clCommandQueue(), maxMem, resMaxMem, 0, 0, len, 0, null, null);

			if (topo.matPEnabled) {
				clSetKernelArg(clKernelMatP, 11, Sizeof.cl_mem, Pointer.to(minMem));
				clSetKernelArg(clKernelMatP, 12, Sizeof.cl_mem, Pointer.to(maxMem));
				clSetKernelArg(clKernelMatP, 13, Sizeof.cl_mem, Pointer.to(resMinMem));
				clSetKernelArg(clKernelMatP, 14, Sizeof.cl_mem, Pointer.to(resMaxMem));
				clEnqueueNDRangeKernel(clCommandQueue(), clKernelMatP, 1, null, gwsP, lws, 0, null, null);
			}

			if (topo.matNEnabled) {
				clSetKernelArg(clKernelMatN, 10, Sizeof.cl_mem, Pointer.to(minMem));
				clSetKernelArg(clKernelMatN, 11, Sizeof.cl_mem, Pointer.to(maxMem));
				clSetKernelArg(clKernelMatN, 12, Sizeof.cl_mem, Pointer.to(resMinMem));
				clSetKernelArg(clKernelMatN, 13, Sizeof.cl_mem, Pointer.to(resMaxMem));
				clEnqueueNDRangeKernel(clCommandQueue(), clKernelMatN, 1, null, gwsNP, lws, 0, null, null);
			}

			++totalIterationCnt;

			// SUM
			if (getSumWeight() != 0) {
				clSetKernelArg(clKernelSum, 1, Sizeof.cl_double, Pointer.to(new double[]{getSumWeight()}));
				clSetKernelArg(clKernelSum, 2, Sizeof.cl_mem, Pointer.to(resMinMem));
				clSetKernelArg(clKernelSum, 3, Sizeof.cl_mem, Pointer.to(resMaxMem));
				clEnqueueNDRangeKernel(clCommandQueue(), clKernelSum, 1, null, gwsSum, lws, 0, null, null);
			}

			swapSolMem();
		}
	}

	final private void swapSolMem()
	{
		final cl_mem tmpMin = minMem;
		final cl_mem tmpMax = maxMem;
		minMem = resMinMem;
		maxMem = resMaxMem;
		resMinMem = tmpMin;
		resMaxMem = tmpMax;
	}

	final private double getSumWeight()
	{
		if (totalIterationCnt >= weightOff) {
			return weight[totalIterationCnt - weightOff];
		}
		return weightDef;
	}

	final private cl_command_queue clCommandQueue() { return clCommandQueue; }
	final private cl_context clContext() { return opts.clContext; }

	final private int matCnt;
	final private int stCnt;
	final private PSEMVMultTopology_ELLFW_OCL topo;
	final private PSEMVMultSettings_OCL opts;

	final private int matPRowCntPadded;
	final private int matNRowCntPadded;

	final private cl_program clProgram;
	final private cl_command_queue clCommandQueue;

	private cl_kernel clKernelMatP;
	private cl_kernel clKernelMatN;
	private cl_kernel clKernelSum;

	private int totalIterationCnt;
	private cl_mem sumMin;
	private cl_mem sumMax;
	private double[] weight;
	private double   weightDef;
	private int      weightOff;

	private cl_mem matPLowerVal;
	private cl_mem matPUpperVal;

	private boolean matNInitialized;
	private cl_mem matNVal;

	private cl_mem minMem;
	private cl_mem resMinMem;
	private cl_mem maxMem;
	private cl_mem resMaxMem;
}
