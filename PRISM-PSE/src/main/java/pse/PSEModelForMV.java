package pse;

public final class PSEModelForMV
{
	public PSEModelForMV
	( int stCnt

	, double[] matLowerVal
	, double[] matUpperVal
	, int[] matCol
	, int[] matRow
	, int[] matRowBeg
	, int matRowCnt

	, double[] matNPVal
	, int[] matNPCol
	, int[] matNPRow
	, int[] matNPRowBeg
	, int matNPRowCnt
	)
	{
		this.stCnt = stCnt;
		this.matLowerVal = matLowerVal;
		this.matUpperVal = matUpperVal;
		this.matCol = matCol;
		this.matRow = matRow;
		this.matRowBeg = matRowBeg;
		this.matRowCnt = matRowCnt;
		this.hasMat = matRowCnt > 0 && matRowBeg[matRowCnt] > 0;

		this.matNPVal = matNPVal;
		this.matNPCol = matNPCol;
		this.matNPRow = matNPRow;
		this.matNPRowBeg = matNPRowBeg;
		this.matNPRowCnt = matNPRowCnt;
		this.hasNPMat = matNPRowCnt > 0 && matNPRowBeg[matNPRowCnt] > 0;
	}

	final public void mvMult
		( final double min[], final double resMin[]
		, final double max[], final double resMax[]
		)
	{
		System.arraycopy(min, 0, resMin, 0, resMin.length);
		System.arraycopy(max, 0, resMax, 0, resMax.length);

		if (hasMat) {
			for (int ii = 0; ii < matRowCnt; ++ii) {
				final int v0 = matRow[ii];
				final int tb = matRowBeg[ii];
				final int te = matRowBeg[ii + 1];
				for (int jj = tb; jj < te; ++jj) {
					final int v1 = matCol[jj];
					if (min[v1] > min[v0]) {
						resMin[v0] += matLowerVal[jj] * (min[v1] - min[v0]);
					} else {
						resMin[v0] += matUpperVal[jj] * (min[v1] - min[v0]);
					}
					if (max[v1] > max[v0]) {
						resMax[v0] += matUpperVal[jj] * (max[v1] - max[v0]);
					} else {
						resMax[v0] += matLowerVal[jj] * (max[v1] - max[v0]);
					}
				}
			}
		}

		if (hasNPMat) {
			for (int ii = 0; ii < matNPRowCnt; ++ii) {
				final int v0 = matNPRow[ii];
				final int tb = matNPRowBeg[ii];
				final int te = matNPRowBeg[ii + 1];
				for (int jj = tb; jj < te; ++jj) {
					final int v1 = matNPCol[jj];
					final double rate = matNPVal[jj];
					resMin[v0] += rate * (min[v1] - min[v0]);
					resMax[v0] += rate * (max[v1] - max[v0]);
				}
			}
		}
	}

	private int stCnt;

	final private double[] matLowerVal;
	final private double[] matUpperVal;
	final private int[] matCol;
	final private int[] matRow;
	final private int[] matRowBeg;
    final private int matRowCnt;
	final private boolean hasMat;

	final private double[] matNPVal;
	final private int[] matNPCol;
	final private int[] matNPRow;
	final private int[] matNPRowBeg;
	final private int matNPRowCnt;
	final private boolean hasNPMat;
}
