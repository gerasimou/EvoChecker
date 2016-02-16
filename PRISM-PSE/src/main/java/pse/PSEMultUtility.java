package pse;

import prism.Pair;
import prism.PrismLog;

import java.util.BitSet;

final public class PSEMultUtility
{
	public static PSEMultOptions getOptions()
	{
		return new PSEMultOptions(getOptFmt(), getOptOcl(), getOptPara(), getOptMany(), getOptAdaptiveFoxGlynn());
	}

	public static void weightedSumToBoth(
		final int n, final double w,
		final double[] inMin, final double[] inMax,
		final double[] sumMin, final double[] sumMax)
	{
		if (w != 0) {
			for (int j = 0; j < n; ++j) {
				sumMin[j] += w * inMin[j];
				sumMax[j] += w * inMax[j];
			}
		}
	}

	public static void weightedSumTo(final int n, final double w, final double[] in, final double[] sum)
	{
		if (w != 0) {
			for (int j = 0; j < n; ++j) {
				sum[j] += w * in[j];
			}
		}
	}

	private static PSEMultFormat getOptFmt()
	{
		String envFmt = System.getenv("PSE_FMT");
		if (envFmt != null) {
			if (envFmt.equals("CSR")) {
				return PSEMultFormat.CSR;
			} else if (envFmt.equals("ELL")) {
				return PSEMultFormat.ELL;
			} else if (envFmt.equals("ELLFW")) {
				return PSEMultFormat.ELLFW;
			}
		}
		return PSEMultFormat.CSR;
	}

	private static boolean getOptOcl()
	{
		String envOCL = System.getenv("PSE_OCL");
		return (envOCL != null && envOCL.equals("1"));
	}

	private static int getOptPara()
	{
		String envPARA = System.getenv("PSE_PARA");
		if (envPARA == null) {
			return 0;
		}
		return Integer.parseInt(envPARA);
	}

	private static int getOptMany()
	{
		String envMANY = System.getenv("PSE_MANY");
		if (envMANY == null) {
			return 0;
		}
		return Integer.parseInt(envMANY);
	}

	private static boolean getOptAdaptiveFoxGlynn()
	{
		String envOCL = System.getenv("PSE_ADAPTIVE_FOX_GLYNN");
		return (envOCL != null && envOCL.equals("1"));
	}
}
