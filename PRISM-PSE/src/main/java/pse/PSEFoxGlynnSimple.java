package pse;

import prism.PrismException;
import prism.PrismLog;

import java.util.Map;

public final class PSEFoxGlynnSimple<Mult extends PSEMult> implements PSEFoxGlynn
{
	public PSEFoxGlynnSimple(PSEMultOptions multOptions, PSEModel model, PSEMultManager<Mult> multManager, int iterStep, PrismLog log)
	{
		this.multOptions = multOptions;
		this.model = model;
		this.multManager = multManager;
		this.iterStep = iterStep;

		this.log = log;

		this.solnMin = new double[model.getNumStates()];
		this.solnMax = new double[model.getNumStates()];

		this.mult = multManager.create();

		System.err.printf("%s<%s>\n", this.getClass().toString(), mult.getClass().toString());
	}

	@Override
	final public int compute
		( DistributionGetter distributionGetter
		, UniformisationRateGetter uniformisationRateGetter
		, ParametersGetter parametersGetter
		, double t

		, DecompositionProcedure decompositionProcedure

		, BoxRegionValues in
		, BoxRegionValues outPrev
		, BoxRegionValues out
		) throws PrismException, DecompositionProcedure.DecompositionNeeded
	{
		final int n = model.getNumStates();

		int iters;
		int itersTotal = 0;

		double q = 0;
		double[] weight = null;
		double weightDef = 0;
		int fgL = 0;
		int fgR = 0;
		if (!multOptions.getAdaptiveFoxGlynn()) {
			q = uniformisationRateGetter.getUniformisationRate(model);
			PSEFoxGlynn.Params params = parametersGetter.getParameters(q, t);
			weight = params.weight;
			weightDef = params.weightDef;
			fgL = params.fgL;
			fgR = params.fgR;
			mult.setWeight(weight, weightDef, fgL);
		}

		for (Map.Entry<BoxRegion, BoxRegionValues.StateValuesPair> entry : in) {
			BoxRegion region = entry.getKey();

			if (outPrev.hasRegion(region)) {
				out.put(region, outPrev.getMin(region), outPrev.getMax(region));
				continue;
			}

			// Configure parameter space
			model.evaluateParameters(region);
			multManager.update(mult);
			if (multOptions.getAdaptiveFoxGlynn()) {
				q = uniformisationRateGetter.getUniformisationRate(model);
				Params params = parametersGetter.getParameters(q, t);
				weight = params.weight;
				weightDef = params.weightDef;
				fgL = params.fgL;
				fgR = params.fgR;
				mult.setWeight(weight, weightDef, fgL);
			}

			log.println("Computing probabilities for parameter region " + region);
			log.println(String.format("Fox-Glynn q = %s, left = %s, right = %s", q, fgL, fgR));
			log.println();

			distributionGetter.getDistribution(entry, 0, solnMin, solnMax);
			final double[] sumMin = new double[n];
			final double[] sumMax = new double[n];
			// If necessary, do 0th element of summation (doesn't require any matrix powers)
			{
				double w = (fgL == 0) ? weight[0] : weightDef;
				if (w != 0) {
					for (int i = 0; i < n; i++) {
						sumMin[i] = w * solnMin[i];
						sumMax[i] = w * solnMax[i];
					}
					mult.setSum(sumMin, sumMax);
				}
			}

			iters = 0;
			mult.setMult(solnMin, solnMax);
			while (iters < fgR) {
				int itersStep;
				if (iters == 0 && weightDef == 0) {
					itersStep = Math.min(fgR,Math.max(Utility.leastGreaterMultiple(fgL, iterStep), iterStep));
				} else {
					itersStep = Math.min(iterStep, fgR - iters);
				}
				mult.mult(itersStep);
				iters += itersStep;
				itersTotal += itersStep;

				mult.getSum(sumMin, sumMax);
				decompositionProcedure.examinePartialComputation(out, region, sumMin, sumMax);
			}
			mult.getSum(sumMin, sumMax);
			decompositionProcedure.examinePartialComputation(out, region, sumMin, sumMax);
			out.put(region, sumMin, sumMax);
		}
		log.print(String.format("PSEFoxGlynnSimple: iters_total=%s\n", itersTotal));
		return itersTotal;
	}

	final private PSEModel model;
	final private PSEMultOptions multOptions;
	final private PSEMultManager<Mult> multManager;
	final private int iterStep;

	final private PrismLog log;

	final private double[] solnMin;
	final private double[] solnMax;

	final private Mult mult;
}
