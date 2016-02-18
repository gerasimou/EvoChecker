package pse;

import java.util.BitSet;
import java.util.Map;

import explicit.FoxGlynn;
import prism.PrismException;

public interface PSEFoxGlynn
{
	public static class Params
	{
		public Params(int fgL, int fgR, double[] weight, double weightDef) throws PrismException
		{
			this.fgL = fgL;
			this.fgR = fgR;
			this.weight = weight;
			this.weightDef = weightDef;
			if (fgR < 0) {
				throw new PrismException("Overflow in Fox-Glynn computation (time bound too big?)");
			}
		}

		@Override
		public String toString()
		{
			return String.format("{ fgL = %s; fgR = %s }", fgL, fgR);
		}

		final public int fgL;
		final public int fgR;
		final public double[] weight;
		final public double weightDef;
	}

	public static interface UniformisationRateGetter
	{
		public double getUniformisationRate(PSEModel model);
	}

	public static final class UniformisationRateGetterWhole implements UniformisationRateGetter
	{
		public double getUniformisationRate(PSEModel model)
		{
			return model.getDefaultUniformisationRate();
		}
	}

	public static final class UniformisationRateGetterSubset implements UniformisationRateGetter
	{
		public UniformisationRateGetterSubset(BitSet subset)
		{
			this.subset = subset;
		}

		public double getUniformisationRate(PSEModel model)
		{
			return model.getDefaultUniformisationRate(subset);
		}

		final private BitSet subset;
	}

	public static interface ParametersGetter
	{
		public Params getParameters(double q, double t) throws PrismException;
	}

	public static final class ParametersGetterProbs implements ParametersGetter
	{
		public ParametersGetterProbs(double weightDef)
		{
			this.weightDef = weightDef;
		}

		@Override
		public Params getParameters(double q, double t) throws PrismException
		{
			double qt = q * t;
			double accuracy = 1e-6 / 8.0;
			FoxGlynn fg = new FoxGlynn(qt, 1e-300, 1e+300, accuracy);

			int fgL = fg.getLeftTruncationPoint();
			int fgR = fg.getRightTruncationPoint();
			double[] weight = fg.getWeights();
			double weightTotal = fg.getTotalWeight();
			for (int i = fgL; i <= fgR; ++i) {
				weight[i - fgL] /= weightTotal;
			}

			return new Params(fgL, fgR, weight, weightDef);
		}

		private final double weightDef;
	}

	public static final class ParametersGetterRewards implements ParametersGetter
	{
		public ParametersGetterRewards()
		{
		}

		@Override
		public Params getParameters(double q, double t) throws PrismException
		{
			double qt = q * t;
			double accuracy = 1e-6 / 8.0;
			FoxGlynn fg = new FoxGlynn(qt, 1e-300, 1e+300, accuracy);

			int fgL = fg.getLeftTruncationPoint();
			int fgR = fg.getRightTruncationPoint();
			double[] weight = fg.getWeights();
			double weightTotal = fg.getTotalWeight();

			weight[0] /= weightTotal;
			for (int i = fgL	+ 1; i <= fgR; ++i) {
				weight[i - fgL] /= weightTotal;
				weight[i - fgL] += weight[i - fgL - 1];
				weight[i - fgL - 1] = (1 - weight[i - fgL - 1]) / q;
			}
			weight[fgR - fgL] = (1 - weight[fgR - fgL]) / q;

			return new Params(fgL, fgR, weight, 1.0 / q);
		}
	}

	public static interface DistributionGetter
	{
		public void getDistribution(Map.Entry<BoxRegion, BoxRegionValues.StateValuesPair> entry,
			int solnOff, final double[] solnMin, final double[] solnMax) throws PrismException;
	}

	public int compute
		( DistributionGetter distributionGetter
		, UniformisationRateGetter uniformisationRateGetter
		, ParametersGetter parametersGetter
		, double t

		, DecompositionProcedure decompositionProcedure

		, BoxRegionValues in
		, BoxRegionValues outPrev
		, BoxRegionValues out
		) throws PrismException, DecompositionProcedure.DecompositionNeeded;
}
