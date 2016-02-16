package pse;

import java.io.File;

import java.util.Arrays;

public final class PSEMVMult_CPU implements PSEMult, Releaseable
{
	public PSEMVMult_CPU(PSEMVCreateData_CSR data)
	{
		this.stCnt = data.stCnt;
		this.totalIterationCnt = 0;

		this.enabledMatP = data.matIORowCnt > 0 && data.matIORowBeg[data.matIORowCnt] > 0;
		this.enabledMatNP = data.matNPRowCnt > 0 && data.matNPRowBeg[data.matNPRowCnt] > 0;

		this.matPLowerVal = data.matPLowerVal;
		this.matPUpperVal = data.matPUpperVal;
		this.matPCol = data.matIOCol;
		this.matPRow = data.matIORow;
		this.matPRowBeg = data.matIORowBeg;
		this.matPRowCnt = data.matIORowCnt;

		this.matNPVal = data.matNPVal;
		this.matNPCol = data.matNPCol;
		this.matNPRow = data.matNPRow;
		this.matNPRowBeg = data.matNPRowBeg;
		this.matNPRowCnt = data.matNPRowCnt;

		this.sumMin = new double[stCnt];
		this.min = new double[stCnt];
		this.resMin = new double[stCnt];

		if (enabledMatP) {
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

	@Override
	final public void getSum(final double[] sumMin, final double[] sumMax)
	{
		if (enabledMatP) {
			System.arraycopy(this.sumMin, 0, sumMin, 0, sumMin.length);
			System.arraycopy(this.sumMax, 0, sumMax, 0, sumMax.length);
		} else {
			System.arraycopy(this.sumMin, 0, sumMin, 0, sumMin.length);
			System.arraycopy(this.sumMin, 0, sumMax, 0, sumMax.length);
		}
	}

	@Override
	final public void setSum(final double[] sumMin, final double[] sumMax)
	{
		System.arraycopy(sumMin, 0, this.sumMin, 0, sumMin.length);
		if (enabledMatP) {
			System.arraycopy(sumMax, 0, this.sumMax, 0, sumMax.length);
		}
	}

	@Override
	final public void getMult(final double[] resMin, final double[] resMax)
	{
		if (enabledMatP) {
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
		if (enabledMatP) {
			System.arraycopy(max, 0, this.max, 0, max.length);
		}
	}

	@Override
	final public void mult(int iterationCnt)
	{
		if (enabledMatP) {
			for (int i = 0; i < iterationCnt; ++i) {
				System.arraycopy(min, 0, resMin, 0, resMin.length);
				System.arraycopy(max, 0, resMax, 0, resMax.length);
				if (enabledMatNP) {
					PSE_MV_NP_BOTH(matNPRowCnt, matNPVal,
						matNPCol, matNPRow, matNPRowBeg,
						min, max, resMin, resMax);
				}
				if (enabledMatP) {
					PSE_MV_P_BOTH(matPRowCnt, matPLowerVal, matPUpperVal,
						matPCol, matPRow, matPRowBeg,
						min, max, resMin, resMax);
				}

				++totalIterationCnt;
				PSEMultUtility.weightedSumToBoth(stCnt, getSumWeight(), resMin, resMax, sumMin, sumMax);

				swapSolMem();
			}
		} else {
			for (int i = 0; i < iterationCnt; ++i) {
				System.arraycopy(min, 0, resMin, 0, resMin.length);
				if (enabledMatNP) {
					PSE_MV_NP(matNPRowCnt, matNPVal,
						matNPCol, matNPRow, matNPRowBeg,
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

	final private void PSE_MV_NP
		( int matNPRowCnt
		, final double[] matNPVal
		, final int[] matNPCol
		, final int[] matNPRow
		, final int[] matNPRowBeg

		, final double[] in
		, final double[] out
		)
	{
		for (int ii = 0; ii < matNPRowCnt; ++ii) {
			final int v0 = matNPRow[ii];
			final int tb = matNPRowBeg[ii];
			final int te = matNPRowBeg[ii + 1];
			for (int jj = tb; jj < te; ++jj) {
				final int v1 = matNPCol[jj];
				final double rate = matNPVal[jj];
				out[v0] += rate * (in[v1] - in[v0]);
			}
		}
	}

	final private void PSE_MV_NP_BOTH
		( int matNPRowCnt
		, final double[] matNPVal
		, final int[] matNPCol
		, final int[] matNPRow
		, final int[] matNPRowBeg

		, final double[] inMin
		, final double[] inMax
		, final double[] outMin
		, final double[] outMax
		)
	{
		for (int ii = 0; ii < matNPRowCnt; ++ii) {
			final int v0 = matNPRow[ii];
			final int tb = matNPRowBeg[ii];
			final int te = matNPRowBeg[ii + 1];
			for (int jj = tb; jj < te; ++jj) {
				final int v1 = matNPCol[jj];
				final double rate = matNPVal[jj];
				outMin[v0] += rate * (inMin[v1] - inMin[v0]);
				outMax[v0] += rate * (inMax[v1] - inMax[v0]);
			}
		}
	}

	final private void PSE_MV_P_BOTH
		( int matPRowCnt
		, final double[] matPLowerVal
		, final double[] matPUpperVal
		, final int[] matPCol
		, final int[] matPRow
		, final int[] matPRowBeg

		, final double[] inMin
		, final double[] inMax
		, final double[] outMin
		, final double[] outMax
		)
	{
		for (int ii = 0; ii < matPRowCnt; ++ii) {
			final int v0 = matPRow[ii];
			final int tb = matPRowBeg[ii];
			final int te = matPRowBeg[ii + 1];
			for (int jj = tb; jj < te; ++jj) {
				final int v1 = matPCol[jj];
				final double diffMin = inMin[v1] - inMin[v0];
				final double diffMax = inMax[v1] - inMax[v0];
				if (diffMin > 0) {
					outMin[v0] += matPLowerVal[jj] * diffMin;
				} else {
					outMin[v0] += matPUpperVal[jj] * diffMin;
				}
				if (diffMax > 0) {
					outMax[v0] += matPUpperVal[jj] * diffMax;
				} else {
					outMax[v0] += matPLowerVal[jj] * diffMax;
				}
			}
		}
	}

	public void update(PSEMVCreateData_CSR data)
	{
		this.totalIterationCnt = 0;
		if (enabledMatNP) {
			this.matNPVal = data.matNPVal;
		}
		if (enabledMatP) {
			this.matPLowerVal = data.matPLowerVal;
			this.matPUpperVal = data.matPUpperVal;
		}
		Arrays.fill(sumMin, 0);
		if (enabledMatP) {
			Arrays.fill(sumMax, 0);
		}
	}

	@Override
	public void release()
	{
	}

	final private double getSumWeight()
	{
		if (totalIterationCnt >= weightOff) {
			return weight[totalIterationCnt - weightOff];
		}
		return weightDef;
	}

	private int stCnt;

	private double[] matPLowerVal;
	private double[] matPUpperVal;
	final private int[] matPCol;
	final private int[] matPRow;
	final private int[] matPRowBeg;
    final private int matPRowCnt;
	final private boolean enabledMatP;

	private double[] matNPVal;
	final private int[] matNPCol;
	final private int[] matNPRow;
	final private int[] matNPRowBeg;
	final private int matNPRowCnt;
	final private boolean enabledMatNP;

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
}
