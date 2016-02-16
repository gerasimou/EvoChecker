package pse;

public interface PSEMultMany
{
	public void setMult(int matId, final double min[], final double max[]);
	public void getMult(int matId, final double resMin[], final double resMax[]);
	public void mult(int matCnt, final int iterationCnt);
	public void getSum(int matId, final double[] sumMin, final double[] sumMax);
	public void setSum(int matId, final double[] sumMin, final double[] sumMax);
	public void setWeight(final double[] weight, double weightDef, int weightOff);
}
