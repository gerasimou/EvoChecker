package pse;

public interface PSEMult
{
	public void setMult(double min[], double max[]);
	public void getMult(double resMin[], double resMax[]);
	public void mult(int iterationCnt);
	public void getSum(double[] sumMin, double[] sumMax);
	public void setSum(double[] sumMin, double[] sumMax);
	public void setWeight(double[] weight, double weightDef, int weightOff);
}
