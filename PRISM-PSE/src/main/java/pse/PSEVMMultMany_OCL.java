package pse;

import org.jocl.*;

import static org.jocl.CL.*;

public class PSEVMMultMany_OCL implements PSEMultMany, Releaseable
{
	public PSEVMMultMany_OCL(PSEVMMultSettings_OCL opts, PSEVMMultTopology_OCL topo, int matCnt)
	{
		this.matCnt = matCnt;
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
			final int len = Sizeof.cl_double * stCnt * matCnt;
			this.matOMinDiagVal = clCreateBuffer(clContext(), CL_MEM_READ_ONLY, len, null, null);
			if (enabledMatI || enabledMatIO) {
				this.matOMaxDiagVal = clCreateBuffer(clContext(), CL_MEM_READ_ONLY, len, null, null);
			}
		}
		if (enabledMatNP) {
			final int len = Sizeof.cl_double * topo.matNPTrgBegHost[stCnt];
			this.matNPVal = clCreateBuffer(clContext(), CL_MEM_READ_ONLY, len, null, null);
			this.matNPInitialized = false;

			if (enabledMatI || enabledMatIO) {
				clKernelMatNP = OCLProgram.createKernel("PSE_VM_NP_CSR_BOTH_MANY", clProgram);
				clSetKernelArg(clKernelMatNP, 1, Sizeof.cl_uint, Pointer.to(new int[]{stCnt}));
				clSetKernelArg(clKernelMatNP, 2, Sizeof.cl_mem, Pointer.to(this.matOMinDiagVal));
				clSetKernelArg(clKernelMatNP, 3, Sizeof.cl_mem, Pointer.to(this.matOMaxDiagVal));
				clSetKernelArg(clKernelMatNP, 4, Sizeof.cl_mem, Pointer.to(this.matNPVal));
				clSetKernelArg(clKernelMatNP, 5, Sizeof.cl_mem, Pointer.to(topo.matNPSrc));
				clSetKernelArg(clKernelMatNP, 6, Sizeof.cl_mem, Pointer.to(topo.matNPTrgBeg));
			} else {
				clKernelMatNP = OCLProgram.createKernel("PSE_VM_NP_CSR_MANY", clProgram);
				clSetKernelArg(clKernelMatNP, 1, Sizeof.cl_uint, Pointer.to(new int[]{stCnt}));
				clSetKernelArg(clKernelMatNP, 2, Sizeof.cl_mem, Pointer.to(this.matOMinDiagVal));
				clSetKernelArg(clKernelMatNP, 3, Sizeof.cl_mem, Pointer.to(this.matNPVal));
				clSetKernelArg(clKernelMatNP, 4, Sizeof.cl_mem, Pointer.to(topo.matNPSrc));
				clSetKernelArg(clKernelMatNP, 5, Sizeof.cl_mem, Pointer.to(topo.matNPTrgBeg));
			}
		} else {
			if (enabledMatI || enabledMatIO) {
				clKernelDiag = OCLProgram.createKernel("PSE_VM_DIAG_BOTH_MANY", clProgram);
				clSetKernelArg(clKernelDiag, 1, Sizeof.cl_uint, Pointer.to(new int[]{stCnt}));
				clSetKernelArg(clKernelDiag, 2, Sizeof.cl_mem, Pointer.to(this.matOMinDiagVal));
				clSetKernelArg(clKernelDiag, 3, Sizeof.cl_mem, Pointer.to(this.matOMaxDiagVal));
			} else {
				clKernelDiag = OCLProgram.createKernel("PSE_VM_DIAG_MANY", clProgram);
				clSetKernelArg(clKernelDiag, 1, Sizeof.cl_uint, Pointer.to(new int[]{stCnt}));
				clSetKernelArg(clKernelDiag, 2, Sizeof.cl_mem, Pointer.to(this.matOMinDiagVal));
			}
		}

		if (enabledMatIO) {
			clKernelMatIO = OCLProgram.createKernel("PSE_VM_IO_CSR_BOTH_MANY", clProgram);

			final int len = Sizeof.cl_double * topo.matIOTrgBegHost[stCnt] * matCnt;
			this.matIOLowerVal0 = clCreateBuffer(clContext(), CL_MEM_READ_ONLY,	len, null, null);
			this.matIOLowerVal1 = clCreateBuffer(clContext(), CL_MEM_READ_ONLY,	len, null, null);
			this.matIOUpperVal0 = clCreateBuffer(clContext(), CL_MEM_READ_ONLY,	len, null, null);
			this.matIOUpperVal1 = clCreateBuffer(clContext(), CL_MEM_READ_ONLY, len, null, null);

			clSetKernelArg(clKernelMatIO, 1, Sizeof.cl_uint, Pointer.to(new int[]{stCnt}));
			clSetKernelArg(clKernelMatIO, 2, Sizeof.cl_mem, Pointer.to(this.matIOLowerVal0));
			clSetKernelArg(clKernelMatIO, 3, Sizeof.cl_mem, Pointer.to(this.matIOLowerVal1));
			clSetKernelArg(clKernelMatIO, 4, Sizeof.cl_mem, Pointer.to(this.matIOUpperVal0));
			clSetKernelArg(clKernelMatIO, 5, Sizeof.cl_mem, Pointer.to(this.matIOUpperVal1));
			clSetKernelArg(clKernelMatIO, 6, Sizeof.cl_mem, Pointer.to(topo.matIOSrc));
			clSetKernelArg(clKernelMatIO, 7, Sizeof.cl_mem, Pointer.to(topo.matIOTrgBeg));
		}

		if (enabledMatI) {
			clKernelMatI = OCLProgram.createKernel("PSE_VM_I_CSR_BOTH_MANY", clProgram);

			final int len = Sizeof.cl_double * topo.matITrgBegHost[stCnt] * matCnt;
			this.matIMaxVal = clCreateBuffer(clContext(), CL_MEM_READ_ONLY, len, null, null);
			this.matIMinVal = clCreateBuffer(clContext(), CL_MEM_READ_ONLY, len, null, null);

			clSetKernelArg(clKernelMatI, 1, Sizeof.cl_uint, Pointer.to(new int[]{stCnt}));
			clSetKernelArg(clKernelMatI, 2, Sizeof.cl_mem, Pointer.to(this.matIMinVal));
			clSetKernelArg(clKernelMatI, 3, Sizeof.cl_mem, Pointer.to(this.matIMaxVal));
			clSetKernelArg(clKernelMatI, 4, Sizeof.cl_mem, Pointer.to(topo.matISrc));
			clSetKernelArg(clKernelMatI, 5, Sizeof.cl_mem, Pointer.to(topo.matITrgBeg));
		}

		{
			final int len = Sizeof.cl_double * stCnt * matCnt;
			if (enabledMatI || enabledMatIO) {
				clKernelSum = OCLProgram.createKernel("WeightedSumToBoth", clProgram);
				this.sumMin = clCreateBuffer(clContext(), CL_MEM_READ_WRITE, len, null, null);
				this.sumMax = clCreateBuffer(clContext(), CL_MEM_READ_WRITE, len, null, null);
				clSetKernelArg(clKernelSum, 4, Sizeof.cl_mem, Pointer.to(this.sumMin));
				clSetKernelArg(clKernelSum, 5, Sizeof.cl_mem, Pointer.to(this.sumMax));
			} else {
				clKernelSum = OCLProgram.createKernel("WeightedSumTo", clProgram);
				this.sumMin = clCreateBuffer(clContext(), CL_MEM_READ_WRITE, len, null, null);
				clSetKernelArg(clKernelSum, 3, Sizeof.cl_mem, Pointer.to(this.sumMin));
			}
		}

		{
			final int len = Sizeof.cl_double * stCnt * matCnt;
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

	public void update(int matId, PSEVMCreateData_CSR data)
	{
		assert(matId < matCnt);

		totalIterationCnt = 0;

		{
			final int len = Sizeof.cl_double * stCnt;
			final int off = len * matId;

			final Pointer matOMinDiagVal_ = Pointer.to(data.matOMinDiagVal);
			final Pointer matOMaxDiagVal_ = Pointer.to(data.matOMaxDiagVal);
			clEnqueueWriteBuffer(clCommandQueue(), matOMinDiagVal, false, off, len, matOMinDiagVal_, 0, null, null);
			if (enabledMatI || enabledMatIO) {
				clEnqueueWriteBuffer(clCommandQueue(), matOMaxDiagVal, false, off, len, matOMaxDiagVal_, 0, null, null);
			}
		}

		if (enabledMatNP) {
			final int len = Sizeof.cl_double * topo.matNPTrgBegHost[stCnt];
			clEnqueueWriteBuffer(clCommandQueue(), matNPVal, false, 0, len, Pointer.to(data.matNPVal), 0, null, null);
			matNPInitialized = true;
		}

		if (enabledMatIO) {
			final int len = Sizeof.cl_double * topo.matIOTrgBegHost[stCnt];
			final int off = len * matId;

			final Pointer matIOLowerVal0_ = Pointer.to(data.matIOLowerVal0);
			final Pointer matIOLowerVal1_ = Pointer.to(data.matIOLowerVal1);
			final Pointer matIOUpperVal0_ = Pointer.to(data.matIOUpperVal0);
			final Pointer matIOUpperVal1_ = Pointer.to(data.matIOUpperVal1);

			clEnqueueWriteBuffer(clCommandQueue(), matIOLowerVal0, false, off, len, matIOLowerVal0_, 0, null, null);
			clEnqueueWriteBuffer(clCommandQueue(), matIOLowerVal1, false, off, len, matIOLowerVal1_, 0, null, null);
			clEnqueueWriteBuffer(clCommandQueue(), matIOUpperVal0, false, off, len, matIOUpperVal0_, 0, null, null);
			clEnqueueWriteBuffer(clCommandQueue(), matIOUpperVal1, false, off, len, matIOUpperVal1_, 0, null, null);
		}

		if (enabledMatI) {
			final int len = Sizeof.cl_double * topo.matITrgBegHost[stCnt];
			final int off = len * matId;

			final Pointer matIMinVal_ = Pointer.to(data.matIMinVal);
			final Pointer matIMaxVal_ = Pointer.to(data.matIMaxVal);

			clEnqueueWriteBuffer(clCommandQueue(), matIMinVal, false, off, len, matIMinVal_, 0, null, null);
			clEnqueueWriteBuffer(clCommandQueue(), matIMaxVal, false, off, len, matIMaxVal_, 0, null, null);
		}

		{
			final int len = Sizeof.cl_double * stCnt;
			final int off = len * matId;

			final double[] zeroes = new double[stCnt];
			final Pointer zeroes_ = Pointer.to(zeroes);
			clEnqueueWriteBuffer(clCommandQueue(), sumMin, false, off, len, zeroes_, 0, null, null);
			if (enabledMatI || enabledMatIO) {
				clEnqueueWriteBuffer(clCommandQueue(), sumMax, false, off, len, zeroes_, 0, null, null);
			}
		}

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
	final public void getSum(int matId, final double[] sumMin, final double[] sumMax)
	{
		final int len = Sizeof.cl_double * stCnt;
		final int off = len * matId;
		if (enabledMatI || enabledMatIO) {
			clEnqueueReadBuffer(clCommandQueue(), this.sumMin, true, off, len, Pointer.to(sumMin), 0, null, null);
			clEnqueueReadBuffer(clCommandQueue(), this.sumMax, true, off, len, Pointer.to(sumMax), 0, null, null);
		} else {
			clEnqueueReadBuffer(clCommandQueue(), this.sumMin, true, off, len, Pointer.to(sumMin), 0, null, null);
			clEnqueueReadBuffer(clCommandQueue(), this.sumMin, true, off, len, Pointer.to(sumMax), 0, null, null);
		}
		clFinish(clCommandQueue());
	}

	@Override
	final public void setSum(int matId, final double[] sumMin, final double[] sumMax)
	{
		final int len = Sizeof.cl_double * stCnt;
		final int off = len * matId;
		clEnqueueWriteBuffer(clCommandQueue(), this.sumMin, false, off, len, Pointer.to(sumMin), 0, null, null);
		if (enabledMatI || enabledMatIO) {
			clEnqueueWriteBuffer(clCommandQueue(), this.sumMax, false, off, len, Pointer.to(sumMax), 0, null, null);
		}
	}

	@Override
	final public void getMult(int matId, final double resMin[], final double resMax[])
	{
		final int len = Sizeof.cl_double * stCnt;
		final int off = len * matId;
		if (enabledMatI || enabledMatIO) {
			clEnqueueReadBuffer(clCommandQueue(), minMem, true, off, len, Pointer.to(resMin), 0, null, null);
			clEnqueueReadBuffer(clCommandQueue(), maxMem, true, off, len, Pointer.to(resMax), 0, null, null);
		}
		else {
			clEnqueueReadBuffer(clCommandQueue(), minMem, true, off, len, Pointer.to(resMin), 0, null, null);
			clEnqueueReadBuffer(clCommandQueue(), minMem, true, off, len, Pointer.to(resMax), 0, null, null);
		}
		clFinish(clCommandQueue());
	}

	@Override
	final public void setMult(int matId, final double min[], final double max[])
	{
		final int len = Sizeof.cl_double * stCnt;
		final int off = len * matId;
		clEnqueueWriteBuffer(clCommandQueue(), minMem, true, off, len, Pointer.to(min), 0, null, null);
		if (enabledMatI || enabledMatIO) {
			clEnqueueWriteBuffer(clCommandQueue(), maxMem, true, off, len, Pointer.to(max), 0, null, null);
		}
	}

	@Override
	final public void mult(int matCnt, int iterationCnt)
	{
		assert (this.matCnt >= matCnt);

		final long[] lws = new long[]{OCLProgram.localWorkSize(64)};
		final long[] gws = new long[]{leastGreaterMultiple(stCnt * matCnt, lws[0])};

		// Set the mat cnt
		if (enabledMatNP) {
			clSetKernelArg(clKernelMatNP, 0, Sizeof.cl_uint, Pointer.to(new int[]{matCnt}));
		} else {
			clSetKernelArg(clKernelDiag, 0, Sizeof.cl_uint, Pointer.to(new int[]{matCnt}));
		}
		if (enabledMatIO) {
			clSetKernelArg(clKernelMatIO, 0, Sizeof.cl_uint, Pointer.to(new int[]{matCnt}));
		}
		if (enabledMatI) {
			clSetKernelArg(clKernelMatI, 0, Sizeof.cl_uint, Pointer.to(new int[]{matCnt}));
		}
		clSetKernelArg(clKernelSum, 0, Sizeof.cl_uint, Pointer.to(new int[]{stCnt * matCnt}));
		if (enabledMatI || enabledMatIO) {
			for (int i = 0; i < iterationCnt; ++i) {
				if (enabledMatNP) {
					clSetKernelArg(clKernelMatNP, 7, Sizeof.cl_mem, Pointer.to(minMem));
					clSetKernelArg(clKernelMatNP, 8, Sizeof.cl_mem, Pointer.to(maxMem));
					clSetKernelArg(clKernelMatNP, 9, Sizeof.cl_mem, Pointer.to(resMinMem));
					clSetKernelArg(clKernelMatNP, 10, Sizeof.cl_mem, Pointer.to(resMaxMem));
					clEnqueueNDRangeKernel(clCommandQueue(), clKernelMatNP, 1, null, gws, lws, 0, null, null);
				} else {
					clSetKernelArg(clKernelDiag, 4, Sizeof.cl_mem, Pointer.to(minMem));
					clSetKernelArg(clKernelDiag, 5, Sizeof.cl_mem, Pointer.to(maxMem));
					clSetKernelArg(clKernelDiag, 6, Sizeof.cl_mem, Pointer.to(resMinMem));
					clSetKernelArg(clKernelDiag, 7, Sizeof.cl_mem, Pointer.to(resMaxMem));
					clEnqueueNDRangeKernel(clCommandQueue(), clKernelDiag, 1, null, gws, lws, 0, null, null);
				}

				if (enabledMatIO) {
					clSetKernelArg(clKernelMatIO, 8, Sizeof.cl_mem, Pointer.to(minMem));
					clSetKernelArg(clKernelMatIO, 9, Sizeof.cl_mem, Pointer.to(maxMem));
					clSetKernelArg(clKernelMatIO, 10, Sizeof.cl_mem, Pointer.to(resMinMem));
					clSetKernelArg(clKernelMatIO, 11, Sizeof.cl_mem, Pointer.to(resMaxMem));
					clEnqueueNDRangeKernel(clCommandQueue(), clKernelMatIO, 1, null, gws, lws, 0, null, null);
				}


				if (enabledMatI) {
					clSetKernelArg(clKernelMatI, 6, Sizeof.cl_mem, Pointer.to(minMem));
					clSetKernelArg(clKernelMatI, 7, Sizeof.cl_mem, Pointer.to(maxMem));
					clSetKernelArg(clKernelMatI, 8, Sizeof.cl_mem, Pointer.to(resMinMem));
					clSetKernelArg(clKernelMatI, 9, Sizeof.cl_mem, Pointer.to(resMaxMem));
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
					clSetKernelArg(clKernelMatNP, 6, Sizeof.cl_mem, Pointer.to(minMem));
					clSetKernelArg(clKernelMatNP, 7, Sizeof.cl_mem, Pointer.to(resMinMem));
					clEnqueueNDRangeKernel(clCommandQueue(), clKernelMatNP, 1, null, gws, lws, 0, null, null);
				} else {
					clSetKernelArg(clKernelDiag, 3, Sizeof.cl_mem, Pointer.to(maxMem));
					clSetKernelArg(clKernelDiag, 4, Sizeof.cl_mem, Pointer.to(resMaxMem));
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

	final private int matCnt;
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