package pse;

import java.util.Arrays;

public final class PSEVMMult_CPU implements PSEMult, Releaseable
{
    public PSEVMMult_CPU(PSEVMCreateData_CSR data)
    {
		this.stCnt = data.stCnt;
		this.totalIterationCnt = 0;

		this.enabledMatNP = data.matNPTrgBeg[data.stCnt] > 0;
		this.enabledMatIO = data.matIOTrgBeg[data.stCnt] > 0;
		this.enabledMatI = data.matITrgBeg[data.stCnt] > 0;

        this.matIOLowerVal0 = data.matIOLowerVal0;
	    this.matIOLowerVal1 = data.matIOLowerVal1;
	    this.matIOUpperVal0 = data.matIOUpperVal0;
	    this.matIOUpperVal1 = data.matIOUpperVal1;
        this.matIOSrc = data.matIOSrc;
        this.matIOTrgBeg = data.matIOTrgBeg;

        this.matOMinDiagVal = data.matOMinDiagVal;
	    this.matOMaxDiagVal = data.matOMaxDiagVal;
        this.matIMinVal = data.matIMinVal;
	    this.matIMaxVal = data.matIMaxVal;
        this.matISrc = data.matISrc;
        this.matITrgBeg = data.matITrgBeg;

        this.matNPVal = data.matNPVal;
        this.matNPSrc = data.matNPSrc;
        this.matNPTrgBeg = data.matNPTrgBeg;

	    this.sumMin = new double[stCnt];
		this.min = new double[stCnt];
		this.resMin = new double[stCnt];
		if (enabledMatI || enabledMatIO) {
			this.max = new double[stCnt];
			this.resMax = new double[stCnt];
			this.sumMax = new double[stCnt];
		}
    }

	@Override
	final public void setWeight(double[] weight, double weightDef, int weightOff)
	{
		this.weight = weight;
		this.weightDef = weightDef;
		this.weightOff = weightOff;
	}

	final public void update(PSEVMCreateData_CSR data)
	{
		totalIterationCnt = 0;

		this.matOMinDiagVal = data.matOMinDiagVal;
		if (enabledMatI || enabledMatIO) {
			this.matOMaxDiagVal = data.matOMaxDiagVal;
		}
		if (enabledMatNP) {
			this.matNPVal = data.matNPVal;
		}

		if (enabledMatIO) {
			this.matIOLowerVal0 = data.matIOLowerVal0;
			this.matIOLowerVal1 = data.matIOLowerVal1;
			this.matIOUpperVal0 = data.matIOUpperVal0;
			this.matIOUpperVal1 = data.matIOUpperVal1;
		}

		if (enabledMatI) {
			this.matIMinVal = data.matIMinVal;
			this.matIMaxVal = data.matIMaxVal;
		}

		Arrays.fill(sumMin, 0);
		if (enabledMatI || enabledMatIO) {
			Arrays.fill(sumMax, 0);
		}
	}

	@Override
	final public void release()
	{
	}

	@Override
	final public void getSum(final double[] sumMin, final double[] sumMax)
	{
		if (enabledMatI || enabledMatIO) {
			System.arraycopy(this.sumMin, 0, sumMin, 0, sumMin.length);
			System.arraycopy(this.sumMax, 0, sumMax, 0, sumMax.length);
		}
		else {
			System.arraycopy(this.sumMin, 0, sumMin, 0, sumMin.length);
			System.arraycopy(this.sumMin, 0, sumMax, 0, sumMax.length);
		}
	}

	@Override
	final public void setSum(final double[] sumMin, final double[] sumMax)
	{
		System.arraycopy(sumMin, 0, this.sumMin, 0, sumMin.length);
		if (enabledMatI || enabledMatIO) {
			System.arraycopy(sumMax, 0, this.sumMax, 0, sumMax.length);
		}
	}

	@Override
	final public void getMult(final double[] resMin, final double[] resMax)
	{
		if (enabledMatI || enabledMatIO) {
			System.arraycopy(this.min, 0, resMin, 0, resMin.length);
			System.arraycopy(this.max, 0, resMax, 0, resMax.length);
		} else {
			System.arraycopy(this.min, 0, resMin, 0, resMin.length);
			System.arraycopy(this.min, 0, resMax, 0, resMax.length);
		}
	}

	@Override
	final public void setMult(final double[] min, final double[] max)
	{
		System.arraycopy(min, 0, this.min, 0, min.length);
		if (enabledMatI || enabledMatIO) {
			System.arraycopy(max, 0, this.max, 0, max.length);
		}
	}

	@Override
	final public void mult(int iterationCnt)
	{
		// This if could be jut around the sum code, but we want the loop as tight as possible.
		if (enabledMatI || enabledMatIO) {
			for (int i = 0; i < iterationCnt; ++i) {
				if (enabledMatNP) {
					PSE_VM_NP_BOTH(stCnt, matOMinDiagVal, matOMaxDiagVal, matNPVal,
						matNPSrc, matNPTrgBeg,
						min, max, resMin, resMax);
				} else {
					PSE_VM_DIAG_BOTH(stCnt, matOMinDiagVal, matOMaxDiagVal,
						min, max, resMin, resMax);
				}

				if (enabledMatIO) {
					PSE_VM_IO_BOTH(stCnt,
						matIOLowerVal0, matIOLowerVal1,
						matIOUpperVal0, matIOUpperVal1,
						matIOSrc, matIOTrgBeg,
						min, max, resMin, resMax);
				}

				if (enabledMatI) {
					PSE_VM_I_BOTH(stCnt, matIMinVal, matIMaxVal,
						matISrc, matITrgBeg,
						min, max, resMin, resMax);
				}

				++totalIterationCnt;
				PSEMultUtility.weightedSumToBoth(stCnt, getSumWeight(), resMin, resMax, sumMin, sumMax);

				swapSolMem();
			}
		} else {
			for (int i = 0; i < iterationCnt; ++i) {
				if (enabledMatNP) {
					PSE_VM_NP(stCnt, matOMinDiagVal, matNPVal,
						matNPSrc, matNPTrgBeg,
						min, resMin);
				} else {
					PSE_VM_DIAG(stCnt, matOMinDiagVal,
						min, resMin);
				}

				++totalIterationCnt;
				PSEMultUtility.weightedSumTo(stCnt, getSumWeight(), resMin, sumMin);

				swapSolMem();
			}
		}
	}

	final private void swapSolMem()
	{
		final double[] tmp1 = resMin;
		resMin = min;
		min = tmp1;
		final double[] tmp2 = resMax;
		resMax = max;
		max = tmp2;
	}

	final static private void PSE_VM_DIAG_BOTH(final int matRowCnt,
		final double[] matDiaVal1, final double[] matDiaVal2,
		final double[] inMin, final double[] inMax,
		final double[] outMin, final double[] outMax)
	{
		for (int v0 = 0; v0 < matRowCnt; ++v0) {
			outMin[v0] = inMin[v0] * matDiaVal1[v0];
			outMax[v0] = inMax[v0] * matDiaVal2[v0];
		}
	}

	final static private void PSE_VM_DIAG(final int matRowCnt,
		final double[] matDiaVal1,
		final double[] inMin, final double[] outMin)
	{
		for (int v0 = 0; v0 < matRowCnt; ++v0) {
			outMin[v0] = inMin[v0] * matDiaVal1[v0];
		}
	}

	final static private void PSE_VM_NP_BOTH(final int matRowCnt,
		final double[] matDiaVal1, final double[] matDiaVal2, final double[] matVal,
		final int[] matCol, final int[] matRowBeg,
		final double[] inMin, final double[] inMax,
		final double[] outMin, final double[] outMax)
	{
		for (int v0 = 0; v0 < matRowCnt; ++v0) {
			double dotMin = inMin[v0] * matDiaVal1[v0]; //out[v0] + in[v0] * matDiaVal[v0];
			double dotMax = inMax[v0] * matDiaVal2[v0]; //out[v0] + in[v0] * matDiaVal[v0];

			int cb = matRowBeg[v0];
			int ce = matRowBeg[v0 + 1];
			for (int i = cb; i < ce; ++i) {
				dotMin += matVal[i] * inMin[matCol[i]];
				dotMax += matVal[i] * inMax[matCol[i]];
			}
			outMin[v0] = dotMin;
			outMax[v0] = dotMax;
		}
	}

	final static private void PSE_VM_NP(final int matRowCnt,
		final double[] matDiaVal1, final double[] matVal,
		final int[] matCol, final int[] matRowBeg,
		final double[] inMin, final double[] outMin)
	{
		for (int v0 = 0; v0 < matRowCnt; ++v0) {
			double dotMin = inMin[v0] * matDiaVal1[v0]; //out[v0] + in[v0] * matDiaVal[v0];

			int cb = matRowBeg[v0];
			int ce = matRowBeg[v0 + 1];
			for (int i = cb; i < ce; ++i) {
				dotMin += matVal[i] * inMin[matCol[i]];
			}
			outMin[v0] = dotMin;
		}
	}

	final static private void PSE_VM_IO_BOTH(final int matRowCnt,
		final double[] matLowerVal0, final double[] matLowerVal1,
		final double[] matUpperVal0, final double[] matUpperVal1,
		final int[] matCol, final int[] matRowBeg,
		final double[] inMin, final double[] inMax,
		final double[] outMin, final double[] outMax)
	{
		for (int v1 = 0; v1 < matRowCnt; ++v1) {
			double dotMin = outMin[v1];
			double dotMax = outMax[v1];

			int cb = matRowBeg[v1];
			int ce = matRowBeg[v1 + 1];

			for (int i = cb; i < ce; ++i) {
				final int v0 = matCol[i];
				final double diff1 = (matLowerVal0[i] * inMin[v0] - matLowerVal1[i] * inMin[v1]);
				final double diff2 = (matUpperVal0[i] * inMax[v0] - matUpperVal1[i] * inMax[v1]);
				if (diff1 > 0.0) {
					dotMin += diff1;
				} else {
					dotMin += (matUpperVal0[i] * inMin[v0] - matUpperVal1[i] * inMin[v1]);
				}
				if (diff2 > 0.0) {
					dotMax += diff2;
				} else {
					dotMax += (matLowerVal0[i] * inMax[v0] - matLowerVal1[i] * inMax[v1]);
				}
			}
			outMin[v1] = dotMin;
			outMax[v1] = dotMax;
		}
	}

	final static private void PSE_VM_I_BOTH(final int matRowCnt,
		final double[] matVal1, final double[] matVal2,
		final int[] matCol, final int[] matRowBeg,
		final double[] inMin, final double[] inMax,
		final double[] outMin, final double[] outMax)
	{
		for (int v0 = 0; v0 < matRowCnt; ++v0) {
			int cb = matRowBeg[v0];
			int ce = matRowBeg[v0 + 1];
			double dotMin = outMin[v0];
			double dotMax = outMax[v0];
			for (int i = cb; i < ce; ++i) {
				dotMin += matVal1[i] * inMin[matCol[i]];
				dotMax += matVal2[i] * inMax[matCol[i]];
			}
			outMin[v0] = dotMin;
			outMax[v0] = dotMax;
		}
	}

	final private double getSumWeight()
	{
		if (totalIterationCnt >= weightOff)
		{
			return weight[totalIterationCnt - weightOff];
		}
		return weightDef;
	}

	final private int stCnt;

    private double[] matIOLowerVal0;
	private double[] matIOLowerVal1;
	private double[] matIOUpperVal0;
	private double[] matIOUpperVal1;

	private int[] matIOSrc;
	private int[] matIOTrgBeg;

    private double[] matOMinDiagVal;
	private double[] matOMaxDiagVal;
    private double[] matIMinVal;
	private double[] matIMaxVal;
    final private int[] matISrc;
    final private int[] matITrgBeg;

    private double[] matNPVal;
    final private int[] matNPSrc;
    final private int[] matNPTrgBeg;

	private int totalIterationCnt;
	private double[] weight;
	private double weightDef;
	private int weightOff;
	final private double[] sumMin;
	private double[] sumMax;
	private double[] min;
	private double[] max;
	private double[] resMin;
	private double[] resMax;

	final private boolean enabledMatNP;
	final private boolean enabledMatIO;
	final private boolean enabledMatI;
}