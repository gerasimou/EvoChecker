package pse;

import prism.PrismException;
import prism.PrismLog;

import java.util.Iterator;
import java.util.Map;

public final class PSEFoxGlynnMany<Mult extends PSEMultMany> implements PSEFoxGlynn
{
	public PSEFoxGlynnMany(PSEMultOptions multOptions, PSEModel model, PSEMultManyManager<Mult> multManager, int iterStep, PrismLog log)
	{
		this.multOptions = multOptions;
		this.matCntMax = multOptions.getMany();
		if (matCntMax < 0) {
			// TODO: Do something clever here
			matCntMax = 4;
		}

		this.model = model;
		this.multManager = multManager;

		this.log = log;

		this.solnMin = new double[model.getNumStates()];
		this.sumMin = new double[model.getNumStates()];
		this.solnMax = new double[model.getNumStates()];
		this.sumMax = new double[model.getNumStates()];

		this.mult = multManager.create(matCntMax);
		this.entry = new Map.Entry[matCntMax];
		this.regionDecomposed = new boolean[matCntMax];
		this.regionsToDecompose = new LabelledBoxRegions();

		this.iterStep = iterStep;

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
		int itersTotalEffective = 0;

		this.regionsToDecompose.clear();

		Iterator<Map.Entry<BoxRegion, BoxRegionValues.StateValuesPair>> it = in.iterator();
		while (it.hasNext()) {
			int matCntDecomposed = 0;
			int matCnt = 0;
			double maxq = 0;
			while (matCnt < matCntMax && it.hasNext()) {
				Map.Entry<BoxRegion, BoxRegionValues.StateValuesPair> e = it.next();
				BoxRegion region = e.getKey();
				// If the previous region values contain probs for this region, i.e. the region
				// has not been decomposed, then just use the previous result directly.
				if (outPrev.hasRegion(region)) {
					out.put(region, outPrev.getMin(region), outPrev.getMax(region));
					continue;
				}
				this.entry[matCnt] = e;
				this.regionDecomposed[matCnt] = false;

				// Configure parameter space
				model.evaluateParameters(region);
				double q = uniformisationRateGetter.getUniformisationRate(model);
				maxq = Math.max(maxq, q);
				log.println("Computing probabilities for parameter region " + region + " (q = " + q + ")");

				++matCnt;
			}

			if (matCnt == 0) {
				continue;
			}

			for (int i = 0; i < matCnt; ++i) {
				model.evaluateParameters(entry[i].getKey());
				multManager.update(i, mult, maxq);
			}

			// Compute FoxGlynn param from the maximum uniformisation rate.
			PSEFoxGlynn.Params params = parametersGetter.getParameters(maxq, t);
			double[] weight = params.weight;
			double weightDef = params.weightDef;
			int fgL = params.fgL;
			int fgR = params.fgR;
			mult.setWeight(weight, weightDef, fgL);
			log.println(String.format("Fox-Glynn q = %s, left = %s, right = %s", maxq, params.fgL, params.fgR));
			log.println();

			// Initialise solution vectors.
			for (int matId = 0; matId < matCnt; ++matId) {
				distributionGetter.getDistribution(entry[matId], 0, solnMin, solnMax);
				// If necessary, do 0th element of summation (doesn't require any matrix powers)
				{
					double w = (fgL == 0) ? weight[0] : weightDef;
					if (w != 0) {
						for (int i = 0; i < n; i++) {
							sumMin[i] = w * solnMin[i];
							sumMax[i] = w * solnMax[i];
						}
						mult.setSum(matId, sumMin, sumMax);
					}
				}
				mult.setMult(matId, solnMin, solnMax);
			}

			// Start iterations
			iters = 0;
			while (iters < fgR) {
				// Matrix-vector multiply
				int itersStep;
				if (iters == 0 && weightDef == 0) {
					itersStep = Math.min(fgR,Math.max(Utility.leastGreaterMultiple(fgL, iterStep), iterStep));
				} else {
					itersStep = Math.min(iterStep, fgR - iters);
				}

				mult.mult(matCnt, itersStep);
				iters += itersStep;
				itersTotal += itersStep * matCntMax;

				for (int matId = 0; matId < matCnt; ++matId) {
					if (regionDecomposed[matId]) {
						continue;
					}
					itersTotalEffective += itersStep;
					mult.getSum(matId, sumMin, sumMax);
					if (handleCheckRegion(decompositionProcedure, out, entry[matId].getKey(), sumMin, sumMax)) {
						regionDecomposed[matId] = true;
						++matCntDecomposed;
					}
				}
				if (matCntDecomposed == matCnt) {
					break;
				}
			}
			// Examine this region's result after all the iters have been finished
			for (int matId = 0; matId < matCnt; ++matId) {
				if (regionDecomposed[matId]) {
					continue;
				}

				mult.getSum(matId, sumMin, sumMax);
				if (!handleCheckRegion(decompositionProcedure, out, entry[matId].getKey(), sumMin, sumMax)) {
					out.put(entry[matId].getKey(), sumMin.clone(), sumMax.clone());
				}
			}
		}
		if (!regionsToDecompose.isEmpty()) {
			DecompositionProcedure.DecompositionNeeded e =
				new DecompositionProcedure.DecompositionNeeded("significant inaccuracy", regionsToDecompose);
			e.setExaminedRegionValues(out);
			throw e;
		}
		log.print(String.format("PSEFoxGlynnMany: iters_total_effective=%s; iters_total=%s; ratio=%s\n", itersTotalEffective, itersTotal, (double)itersTotalEffective/(double)itersTotal));
		return itersTotalEffective;
	}

	final private boolean handleCheckRegion(DecompositionProcedure decompositionProcedure, BoxRegionValues out, BoxRegion region, double[] sumMin, double[] sumMax)
		throws PrismException
	{
		try {
			decompositionProcedure.examinePartialComputation(out, region, sumMin, sumMax);
		} catch (DecompositionProcedure.DecompositionNeeded err) {
			regionsToDecompose.putAll(err.getLabelledRegionsToDecompose());
			return true;
		}
		return false;
	}

	int matCntMax;
	final private PSEModel model;
	final private PSEMultOptions multOptions;
	final private PSEMultManyManager<Mult> multManager;

	final private PrismLog log;
	final private int iterStep;

	final private double[] solnMin;
	final private double[] sumMin;
	final private double[] solnMax;
	final private double[] sumMax;

	final private Mult mult;

	private Map.Entry<BoxRegion, BoxRegionValues.StateValuesPair>[] entry;
	private boolean[] regionDecomposed;
	private LabelledBoxRegions regionsToDecompose;
}
