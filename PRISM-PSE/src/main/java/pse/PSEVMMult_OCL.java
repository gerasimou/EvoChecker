package pse;

import org.jocl.*;

import static org.jocl.CL.*;

public class PSEVMMult_OCL implements PSEMult, Releaseable
{
	public PSEVMMult_OCL(PSEVMMultSettings_OCL opts, PSEVMMultTopology_OCL topo, PSEVMCreateData_CSR data)
	{
		this.stCnt = topo.stCnt;
		this.opts = opts;
		this.topo = topo;

		this.clCommandQueue = clCreateCommandQueue(clContext(), opts.clDeviceIdMin, 0, null);

		setExceptionsEnabled(true);

		this.enabledMatNP = topo.enabledMatNP;
		this.enabledMatIO = topo.enabledMatIO;
		this.enabledMatI = topo.enabledMatI;

		this.totalIterationCnt = 0;

		clProgram = OCLProgram.createProgram(OCLProgram.SOURCE, clContext());

		{
			final int len = Sizeof.cl_double * stCnt;
			this.matOMinDiagVal = clCreateBuffer(clContext(), CL_MEM_READ_ONLY, len, null, null);
			if (enabledMatI || enabledMatIO) {
				this.matOMaxDiagVal = clCreateBuffer(clContext(), CL_MEM_READ_ONLY, len, null, null);
			}
		}
		matNPInitialized = false;
		if (enabledMatNP) {
			final int len = Sizeof.cl_double * topo.matNPTrgBegHost[stCnt];
			this.matNPVal = clCreateBuffer(clContext(), CL_MEM_READ_ONLY, len, null, null);
			if (enabledMatI || enabledMatIO) {
				clKernelMatNP = OCLProgram.createKernel("PSE_VM_NP_CSR_BOTH", clProgram);
				clSetKernelArg(clKernelMatNP, 0, Sizeof.cl_uint, Pointer.to(new int[]{stCnt}));
				clSetKernelArg(clKernelMatNP, 1, Sizeof.cl_mem, Pointer.to(this.matOMinDiagVal));
				clSetKernelArg(clKernelMatNP, 2, Sizeof.cl_mem, Pointer.to(this.matOMaxDiagVal));
				clSetKernelArg(clKernelMatNP, 3, Sizeof.cl_mem, Pointer.to(this.matNPVal));
				clSetKernelArg(clKernelMatNP, 4, Sizeof.cl_mem, Pointer.to(topo.matNPSrc));
				clSetKernelArg(clKernelMatNP, 5, Sizeof.cl_mem, Pointer.to(topo.matNPTrgBeg));
			} else {
				clKernelMatNP = OCLProgram.createKernel("PSE_VM_NP_CSR", clProgram);
				clSetKernelArg(clKernelMatNP, 0, Sizeof.cl_uint, Pointer.to(new int[]{stCnt}));
				clSetKernelArg(clKernelMatNP, 1, Sizeof.cl_mem, Pointer.to(this.matOMinDiagVal));
				clSetKernelArg(clKernelMatNP, 2, Sizeof.cl_mem, Pointer.to(this.matNPVal));
				clSetKernelArg(clKernelMatNP, 3, Sizeof.cl_mem, Pointer.to(topo.matNPSrc));
				clSetKernelArg(clKernelMatNP, 4, Sizeof.cl_mem, Pointer.to(topo.matNPTrgBeg));
			}
		} else {
			if (enabledMatI || enabledMatIO) {
				clKernelDiag = OCLProgram.createKernel("PSE_VM_DIAG_BOTH", clProgram);
				clSetKernelArg(clKernelDiag, 0, Sizeof.cl_uint, Pointer.to(new int[]{stCnt}));
				clSetKernelArg(clKernelDiag, 1, Sizeof.cl_mem, Pointer.to(this.matOMinDiagVal));
				clSetKernelArg(clKernelDiag, 2, Sizeof.cl_mem, Pointer.to(this.matOMaxDiagVal));
			} else {
				clKernelDiag = OCLProgram.createKernel("PSE_VM_DIAG", clProgram);
				clSetKernelArg(clKernelDiag, 0, Sizeof.cl_uint, Pointer.to(new int[]{stCnt}));
				clSetKernelArg(clKernelDiag, 1, Sizeof.cl_mem, Pointer.to(this.matOMinDiagVal));
			}
		}

		if (enabledMatIO) {
			clKernelMatIO = OCLProgram.createKernel("PSE_VM_IO_CSR_BOTH", clProgram);

			final int len = Sizeof.cl_double * topo.matIOTrgBegHost[stCnt];
			this.matIOLowerVal0 = clCreateBuffer(clContext(), CL_MEM_READ_ONLY, len, null, null);
			this.matIOLowerVal1 = clCreateBuffer(clContext(), CL_MEM_READ_ONLY, len, null, null);
			this.matIOUpperVal0 = clCreateBuffer(clContext(), CL_MEM_READ_ONLY, len, null, null);
			this.matIOUpperVal1 = clCreateBuffer(clContext(), CL_MEM_READ_ONLY, len, null, null);

			clSetKernelArg(clKernelMatIO, 0, Sizeof.cl_uint, Pointer.to(new int[]{stCnt}));
			clSetKernelArg(clKernelMatIO, 1, Sizeof.cl_mem, Pointer.to(this.matIOLowerVal0));
			clSetKernelArg(clKernelMatIO, 2, Sizeof.cl_mem, Pointer.to(this.matIOLowerVal1));
			clSetKernelArg(clKernelMatIO, 3, Sizeof.cl_mem, Pointer.to(this.matIOUpperVal0));
			clSetKernelArg(clKernelMatIO, 4, Sizeof.cl_mem, Pointer.to(this.matIOUpperVal1));
			clSetKernelArg(clKernelMatIO, 5, Sizeof.cl_mem, Pointer.to(topo.matIOSrc));
			clSetKernelArg(clKernelMatIO, 6, Sizeof.cl_mem, Pointer.to(topo.matIOTrgBeg));
		}

		if (enabledMatI) {
			clKernelMatI = OCLProgram.createKernel("PSE_VM_I_CSR_BOTH", clProgram);

			final int len = Sizeof.cl_double * topo.matITrgBegHost[stCnt];
			this.matIMaxVal = clCreateBuffer(clContext(), CL_MEM_READ_ONLY, len, null, null);
			this.matIMinVal = clCreateBuffer(clContext(), CL_MEM_READ_ONLY, len, null, null);

			clSetKernelArg(clKernelMatI, 0, Sizeof.cl_uint, Pointer.to(new int[]{stCnt}));
			clSetKernelArg(clKernelMatI, 1, Sizeof.cl_mem, Pointer.to(this.matIMinVal));
			clSetKernelArg(clKernelMatI, 2, Sizeof.cl_mem, Pointer.to(this.matIMaxVal));
			clSetKernelArg(clKernelMatI, 3, Sizeof.cl_mem, Pointer.to(topo.matISrc));
			clSetKernelArg(clKernelMatI, 4, Sizeof.cl_mem, Pointer.to(topo.matITrgBeg));
		}

		{
			final int len = Sizeof.cl_double * stCnt;
			if (enabledMatI || enabledMatIO) {
				clKernelSum = OCLProgram.createKernel("WeightedSumToBoth", clProgram);
				this.sumMin = clCreateBuffer(clContext(), CL_MEM_READ_WRITE, len, null, null);
				this.sumMax = clCreateBuffer(clContext(), CL_MEM_READ_WRITE, len, null, null);
				clSetKernelArg(clKernelSum, 0, Sizeof.cl_uint, Pointer.to(new int[]{stCnt}));
				clSetKernelArg(clKernelSum, 4, Sizeof.cl_mem, Pointer.to(this.sumMin));
				clSetKernelArg(clKernelSum, 5, Sizeof.cl_mem, Pointer.to(this.sumMax));

			} else {
				clKernelSum = OCLProgram.createKernel("WeightedSumTo", clProgram);
				this.sumMin = clCreateBuffer(clContext(), CL_MEM_READ_WRITE, len, null, null);
				clSetKernelArg(clKernelSum, 0, Sizeof.cl_uint, Pointer.to(new int[]{stCnt}));
				clSetKernelArg(clKernelSum, 3, Sizeof.cl_mem, Pointer.to(this.sumMin));
			}
		}

		{
			final int len = Sizeof.cl_double * stCnt;
			minMem = clCreateBuffer(clContext(), CL_MEM_READ_WRITE, len, null, null);
			resMinMem = clCreateBuffer(clContext(), CL_MEM_READ_WRITE, len, null, null);

			if (enabledMatI || enabledMatIO) {
				maxMem = clCreateBuffer(clContext(), CL_MEM_READ_WRITE, len, null, null);
				resMaxMem = clCreateBuffer(clContext(), CL_MEM_READ_WRITE, len, null, null);
			}
		}
	}

	@Override
	final public void setWeight(double[] weight, double weightDef, int weightOff)
	{
		this.weight = weight;
		this.weightDef = weightDef;
		this.weightOff = weightOff;
	}

	public void update(PSEVMCreateData_CSR data)
	{
		totalIterationCnt = 0;

		{
			final int len = Sizeof.cl_double * stCnt;
			final Pointer matOMinDiagVal_ = Pointer.to(data.matOMinDiagVal);
			clEnqueueWriteBuffer(clCommandQueue(), matOMinDiagVal, false, 0, len, matOMinDiagVal_, 0, null, null);
			if (enabledMatI || enabledMatIO) {
				final Pointer matOMaxDiagVal_ = Pointer.to(data.matOMaxDiagVal);
				clEnqueueWriteBuffer(clCommandQueue(), matOMaxDiagVal, false, 0, len, matOMaxDiagVal_, 0, null, null);
			}
		}
		if (enabledMatNP) {
			final int len = Sizeof.cl_double * topo.matNPTrgBegHost[stCnt];
			final Pointer matNPVal_ = Pointer.to(data.matNPVal);
			clEnqueueWriteBuffer(clCommandQueue(), matNPVal, false, 0, len, matNPVal_, 0, null, null);
			matNPInitialized = true;
		}

		if (enabledMatIO) {
			final int len = Sizeof.cl_double * topo.matIOTrgBegHost[stCnt];
			final Pointer matIOLowerVal0_ = Pointer.to(data.matIOLowerVal0);
			final Pointer matIOLowerVal1_ = Pointer.to(data.matIOLowerVal1);
			final Pointer matIOUpperVal0_ = Pointer.to(data.matIOUpperVal0);
			final Pointer matIOUpperVal1_ = Pointer.to(data.matIOUpperVal1);
			clEnqueueWriteBuffer(clCommandQueue(), matIOLowerVal0, false, 0, len, matIOLowerVal0_, 0, null, null);
			clEnqueueWriteBuffer(clCommandQueue(), matIOLowerVal1, false, 0, len, matIOLowerVal1_, 0, null, null);
			clEnqueueWriteBuffer(clCommandQueue(), matIOUpperVal0, false, 0, len, matIOUpperVal0_, 0, null, null);
			clEnqueueWriteBuffer(clCommandQueue(), matIOUpperVal1, false, 0, len, matIOUpperVal1_, 0, null, null);
		}

		if (enabledMatI) {
			final int len = Sizeof.cl_double * topo.matITrgBegHost[stCnt];
			final Pointer matIMinVal_ = Pointer.to(data.matIMinVal);
			final Pointer matIMaxVal_ = Pointer.to(data.matIMaxVal);
			clEnqueueWriteBuffer(clCommandQueue(), matIMinVal, false, 0, len, matIMinVal_, 0, null, null);
			clEnqueueWriteBuffer(clCommandQueue(), matIMaxVal, false, 0, len, matIMaxVal_, 0, null, null);
		}

		{
			final int len = Sizeof.cl_double * stCnt;
			final double[] zeroes = new double[stCnt];
			final Pointer zeroes_ = Pointer.to(zeroes);
			clEnqueueWriteBuffer(clCommandQueue(), sumMin, false, 0, len, zeroes_, 0, null, null);
			if (enabledMatI || enabledMatIO) {
				clEnqueueWriteBuffer(clCommandQueue(), sumMax, false, 0, len, zeroes_, 0, null, null);
			}
		}

		clFinish(clCommandQueue());
	}

	@Override
	public void release()
	{
		clReleaseCommandQueue(clCommandQueue);

		// MAT NP
		clReleaseMemObject(matOMinDiagVal);
		if (enabledMatI || enabledMatIO) {
			clReleaseMemObject(matOMaxDiagVal);
		}
		if (enabledMatNP) {
			clReleaseKernel(clKernelMatNP);
			clReleaseMemObject(matNPVal);
		} else {
			clReleaseKernel(clKernelDiag);
		}

		// MAT IO
		if (enabledMatIO) {
			clReleaseKernel(clKernelMatIO);
			clReleaseMemObject(matIOLowerVal0);
			clReleaseMemObject(matIOLowerVal1);
			clReleaseMemObject(matIOUpperVal0);
			clReleaseMemObject(matIOUpperVal1);
		}

		// MAT I
		if (enabledMatI) {
			clReleaseKernel(clKernelMatI);
			clReleaseMemObject(matIMinVal);
			clReleaseMemObject(matIMaxVal);
		}

		// SUM
		clReleaseMemObject(sumMin);
		if(enabledMatI || enabledMatIO) {
			clReleaseMemObject(sumMax);
		}

		// SOL
		clReleaseMemObject(minMem);
		clReleaseMemObject(resMinMem);
		if (enabledMatI || enabledMatIO) {
			clReleaseMemObject(maxMem);
			clReleaseMemObject(resMaxMem);
		}
	}

	@Override
	final public void getSum(final double[] sumMin, final double[] sumMax)
	{
		final int len = Sizeof.cl_double * stCnt;
		if (enabledMatI || enabledMatIO) {
			clEnqueueReadBuffer(clCommandQueue(), this.sumMin, true, 0, len, Pointer.to(sumMin), 0, null, null);
			clEnqueueReadBuffer(clCommandQueue(), this.sumMax, true, 0, len, Pointer.to(sumMax), 0, null, null);
		}
		else {
			clEnqueueReadBuffer(clCommandQueue(), this.sumMin, true, 0, len, Pointer.to(sumMin), 0, null, null);
			clEnqueueReadBuffer(clCommandQueue(), this.sumMin, true, 0, len, Pointer.to(sumMax), 0, null, null);
		}
		clFinish(clCommandQueue());
	}

	@Override
	final public void setSum(final double[] sumMin, final double[] sumMax)
	{
		final int len = Sizeof.cl_double * stCnt;
		clEnqueueWriteBuffer(clCommandQueue(), this.sumMin, false, 0, len, Pointer.to(sumMin), 0, null, null);
		if (enabledMatI || enabledMatIO) {
			clEnqueueWriteBuffer(clCommandQueue(), this.sumMax, false, 0, len, Pointer.to(sumMax), 0, null, null);
		}
	}

	@Override
	final public void setMult(final double min[], final double max[])
	{
		final int len = Sizeof.cl_double * stCnt;
		clEnqueueWriteBuffer(clCommandQueue(), minMem, true, 0, len, Pointer.to(min), 0, null, null);
		if (enabledMatI || enabledMatIO) {
			clEnqueueWriteBuffer(clCommandQueue(), maxMem, true, 0, len, Pointer.to(max), 0, null, null);
		}
	}

	@Override
	final public void getMult(final double resMin[], final double resMax[])
	{
		final int len = Sizeof.cl_double * stCnt;
		if (enabledMatI || enabledMatIO) {
			clEnqueueReadBuffer(clCommandQueue(), minMem, true, 0, len, Pointer.to(resMin), 0, null, null);
			clEnqueueReadBuffer(clCommandQueue(), maxMem, true, 0, len, Pointer.to(resMax), 0, null, null);
		}
		else {
			clEnqueueReadBuffer(clCommandQueue(), minMem, true, 0, len, Pointer.to(resMin), 0, null, null);
			clEnqueueReadBuffer(clCommandQueue(), minMem, true, 0, len, Pointer.to(resMax), 0, null, null);
		}
		clFinish(clCommandQueue());
	}

	@Override
	final public void mult(int iterationCnt)
	{
		final long[] lws = new long[]{OCLProgram.localWorkSize(64)};
		final long[] gws = new long[]{leastGreaterMultiple(stCnt, lws[0])};

		if (enabledMatI || enabledMatIO) {
			for (int i = 0; i < iterationCnt; ++i) {
				if (enabledMatNP) {
					clSetKernelArg(clKernelMatNP, 6, Sizeof.cl_mem, Pointer.to(minMem));
					clSetKernelArg(clKernelMatNP, 7, Sizeof.cl_mem, Pointer.to(maxMem));
					clSetKernelArg(clKernelMatNP, 8, Sizeof.cl_mem, Pointer.to(resMinMem));
					clSetKernelArg(clKernelMatNP, 9, Sizeof.cl_mem, Pointer.to(resMaxMem));
					clEnqueueNDRangeKernel(clCommandQueue(), clKernelMatNP, 1, null, gws, lws, 0, null, null);
				} else {
					clSetKernelArg(clKernelDiag, 3, Sizeof.cl_mem, Pointer.to(minMem));
					clSetKernelArg(clKernelDiag, 4, Sizeof.cl_mem, Pointer.to(maxMem));
					clSetKernelArg(clKernelDiag, 5, Sizeof.cl_mem, Pointer.to(resMinMem));
					clSetKernelArg(clKernelDiag, 6, Sizeof.cl_mem, Pointer.to(resMaxMem));
					clEnqueueNDRangeKernel(clCommandQueue(), clKernelDiag, 1, null, gws, lws, 0, null, null);
				}

				if (enabledMatIO) {
					clSetKernelArg(clKernelMatIO, 7, Sizeof.cl_mem, Pointer.to(minMem));
					clSetKernelArg(clKernelMatIO, 8, Sizeof.cl_mem, Pointer.to(maxMem));
					clSetKernelArg(clKernelMatIO, 9, Sizeof.cl_mem, Pointer.to(resMinMem));
					clSetKernelArg(clKernelMatIO, 10, Sizeof.cl_mem, Pointer.to(resMaxMem));
					clEnqueueNDRangeKernel(clCommandQueue(), clKernelMatIO, 1, null, gws, lws, 0, null, null);
				}

				if (enabledMatI) {
					clSetKernelArg(clKernelMatI, 5, Sizeof.cl_mem, Pointer.to(minMem));
					clSetKernelArg(clKernelMatI, 6, Sizeof.cl_mem, Pointer.to(maxMem));
					clSetKernelArg(clKernelMatI, 7, Sizeof.cl_mem, Pointer.to(resMinMem));
					clSetKernelArg(clKernelMatI, 8, Sizeof.cl_mem, Pointer.to(resMaxMem));
					clEnqueueNDRangeKernel(clCommandQueue(), clKernelMatI, 1, null, gws, lws, 0, null, null);
				}

				++totalIterationCnt;

				if (getSumWeight() != 0) {
					clSetKernelArg(clKernelSum, 1, Sizeof.cl_double, Pointer.to(new double[]{getSumWeight()}));
					clSetKernelArg(clKernelSum, 2, Sizeof.cl_mem, Pointer.to(resMinMem));
					clSetKernelArg(clKernelSum, 3, Sizeof.cl_mem, Pointer.to(resMaxMem));
					clEnqueueNDRangeKernel(clCommandQueue(), clKernelSum, 1, null, gws, lws, 0, null, null);
				}

				swapSolMem();
			}
		} else {
			for (int i = 0; i < iterationCnt; ++i) {
				if (enabledMatNP) {
					clSetKernelArg(clKernelMatNP, 5, Sizeof.cl_mem, Pointer.to(minMem));
					clSetKernelArg(clKernelMatNP, 6, Sizeof.cl_mem, Pointer.to(resMinMem));
					clEnqueueNDRangeKernel(clCommandQueue(), clKernelMatNP, 1, null, gws, lws, 0, null, null);
				} else {
					clSetKernelArg(clKernelDiag, 2, Sizeof.cl_mem, Pointer.to(maxMem));
					clSetKernelArg(clKernelDiag, 3, Sizeof.cl_mem, Pointer.to(resMaxMem));
					clEnqueueNDRangeKernel(clCommandQueue(), clKernelDiag, 1, null, gws, lws, 0, null, null);
				}

				++totalIterationCnt;

				if (getSumWeight() != 0) {
					clSetKernelArg(clKernelSum, 1, Sizeof.cl_double, Pointer.to(new double[]{getSumWeight()}));
					clSetKernelArg(clKernelSum, 2, Sizeof.cl_mem, Pointer.to(resMinMem));
					clEnqueueNDRangeKernel(clCommandQueue(), clKernelSum, 1, null, gws, lws, 0, null, null);
				}

				swapSolMem();
			}
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

	private static long leastGreaterMultiple(long x, long z)
	{
		return x + (z - x % z) % z;
	}

	final private cl_command_queue clCommandQueue() { return clCommandQueue; }
	final private cl_context clContext() { return opts.clContext; }

	final private int stCnt;
	final private PSEVMMultTopology_OCL topo;
	final private PSEVMMultSettings_OCL opts;

	final private boolean enabledMatNP;
	final private boolean enabledMatIO;
	final private boolean enabledMatI;

	final private cl_program clProgram;
	final private cl_command_queue clCommandQueue;

	private cl_kernel clKernelMatIO;
	private cl_kernel clKernelMatI;
	private cl_kernel clKernelMatNP;
	private cl_kernel clKernelDiag;
	final private cl_kernel clKernelSum;

	private int totalIterationCnt;
	private cl_mem sumMin;
	private cl_mem sumMax;
	private double[] weight;
	private double   weightDef;
	private int      weightOff;

	private cl_mem matIOLowerVal0;
	private cl_mem matIOLowerVal1;
	private cl_mem matIOUpperVal0;
	private cl_mem matIOUpperVal1;

	private cl_mem matIMaxVal;
	private cl_mem matIMinVal;

	private cl_mem matOMinDiagVal;
	private cl_mem matOMaxDiagVal;
	private cl_mem matNPVal;
	private boolean matNPInitialized;

	private cl_mem minMem;
	private cl_mem maxMem;
	private cl_mem resMinMem;
	private cl_mem resMaxMem;
}