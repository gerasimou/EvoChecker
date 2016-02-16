package pse;

public final class PSEModelForVM
{
    public PSEModelForVM
	( int stCnt, int trCnt
	, double[] matIOLowerVal0
	, double[] matIOLowerVal1
	, double[] matIOUpperVal0
	, double[] matIOUpperVal1
	, int[] matIOSrc
	, int[] matIOTrgBeg

	, double[] matMinVal
	, int[] matMinSrc
	, int[] matMinTrgBeg

	, double[] matMaxVal
	, int[] matMaxSrc
	, int[] matMaxTrgBeg

	, double[] matMinDiagVal
	, double[] matMaxDiagVal
	, double[] matVal
	, int[] matSrc
	, int[] matTrgBeg
	)
    {
        this.stCnt = stCnt;
        this.trCnt = trCnt;

        this.matIOLowerVal0 = matIOLowerVal0;
	    this.matIOLowerVal1 = matIOLowerVal1;
	    this.matIOUpperVal0 = matIOUpperVal0;
	    this.matIOUpperVal1 = matIOUpperVal1;
        this.matIOSrc = matIOSrc;
        this.matIOTrgBeg = matIOTrgBeg;


        this.matMinDiagVal = matMinDiagVal;
        this.matMinVal = matMinVal;
        this.matMinSrc = matMinSrc;
        this.matMinTrgBeg = matMinTrgBeg;

        this.matMaxDiagVal = matMaxDiagVal;
        this.matMaxVal = matMaxVal;
        this.matMaxSrc = matMaxSrc;
        this.matMaxTrgBeg = matMaxTrgBeg;

        this.matVal = matVal;
        this.matSrc = matSrc;
        this.matTrgBeg = matTrgBeg;
    }

    final public void vmMult
        ( double min[], double resMin[]
        , double max[], double resMax[]
        , int iterationCnt
        )
    {
	    for (int i = 0; i < iterationCnt; ++i)
	    {
		    vmMult(min, resMin, max, resMax);
		    final double[] tmp1 = resMin;
		    final double[] tmp2 = resMax;
		    resMin = min;
		    resMax = max;
		    min = tmp1;
		    max = tmp2;
	    }
    }


    final public void vmMult
        ( final double min[], final double resMin[]
        , final double max[], final double resMax[]
        )
    {
        //System.arraycopy(min, 0, resMin, 0, min.length);
        //System.arraycopy(max, 0, resMax, 0, max.length);

	    if (matTrgBeg[stCnt] > 0)
		    SpMV2_CS(stCnt, matMinDiagVal, matMaxDiagVal, matVal, matSrc, matTrgBeg, min, max, resMin, resMax);
        if (matMinTrgBeg[stCnt] > 0)
	        SpMV1_CS(stCnt, matMinVal, matMinSrc, matMinTrgBeg, min, resMin);
	    if (matMaxTrgBeg[stCnt] > 0)
		    SpMV1_CS(stCnt, matMaxVal, matMaxSrc, matMaxTrgBeg, max, resMax);
	    if (matIOTrgBeg[stCnt] > 0)
		    SpMVIO_CS(stCnt
			, matIOLowerVal0, matIOLowerVal1, matIOUpperVal0, matIOUpperVal1
			, matIOSrc, matIOTrgBeg
			, min, max, resMin, resMax
			);
    }

	private void SpMV1_CS
	  ( final int stCnt
	  , final double[] val
	  , final int[] trg
	  , final int[] srcBeg

	  , final double[] vi
	  , final double[] vo
	  )
	{
	  for (int v0 = 0; v0 < stCnt; ++v0)
	  {
		double prod = vo[v0];
		int cb = srcBeg[v0];
		int ce = srcBeg[v0 + 1];

		for (int i = cb; i < ce; ++i)
		{
		  prod += val[i] * vi[trg[i]];
		}
		vo[v0] = prod;
	  }
	}

	private void SpMV2_CS
	  ( int stCnt
	  , final double[] diagVal1
	  , final double[] diagVal2
	  , final double[] val
	  , final int[] trg
	  , final int[] srcBeg

	  , final double[] vi1
	  , final double[] vi2
	  , final double[] vo1
	  , final double[] vo2
	  )
	{
	  for (int v0 = 0; v0 < stCnt; ++v0)
	  {
		double prod1 = vi1[v0] * diagVal1[v0]; //vo1[v0] + vi1[v0] * diagVal1[v0];
		double prod2 = vi2[v0] * diagVal2[v0]; //vo2[v0] + vi2[v0] * diagVal2[v0];

		int cb = srcBeg[v0];
		int ce = srcBeg[v0 + 1];

		for (int i = cb; i < ce; ++i)
		{
		  prod1 += val[i] * vi1[trg[i]];
		  prod2 += val[i] * vi2[trg[i]];
		}
		vo1[v0] = prod1;
		vo2[v0] = prod2;
	  }
	}

	private void SpMVIO_CS
	  ( final int stCnt
	  , final double[] lowerVal0
	  , final double[] lowerVal1
	  , final double[] upperVal0
	  , final double[] upperVal1
	  , final int[] src
	  , final int[] trgBeg

	  , final double[] min0
	  , final double[] max0
	  , final double[] min1
	  , final double[] max1
	  )
	{
	  for (int v1 = 0; v1 < stCnt; ++v1)
	  {
		double prod1 = min1[v1];
		double prod2 = max1[v1];

		final int cb = trgBeg[v1];
		final int ce = trgBeg[v1 + 1];

		for (int i = cb; i < ce; ++i)
		{
		  final int v0 = src[i];
		  final double rlowerMin = (lowerVal0[i] * min0[v0] - lowerVal1[i] * min0[v1]);
		  final double rupperMax = (upperVal0[i] * max0[v0] - upperVal1[i] * max0[v1]);
		  if (rlowerMin > 0.0)
		  {
			prod1 += rlowerMin;
		  }
		  else
		  {
			prod1 += (upperVal0[i] * min0[v0] - upperVal1[i] * min0[v1]);
		  }
		  if (rupperMax > 0.0)
		  {
			prod2 += rupperMax;
		  }
		  else
		  {
			prod2 += (lowerVal0[i] * max0[v0] - lowerVal1[i] * max0[v1]);
		  }
		}
		min1[v1] = prod1;
		max1[v1] = prod2;
	  }
	}

    final private int stCnt;
    final private int trCnt;

    private double[] matIOLowerVal0;
	private double[] matIOLowerVal1;
	private double[] matIOUpperVal0;
	private double[] matIOUpperVal1;

	private int[] matIOSrc;
	private int[] matIOTrgBeg;

    final private double[] matMinDiagVal;
    final private double[] matMinVal;
    final private int[] matMinSrc;
    final private int[] matMinTrgBeg;

    final private double[] matMaxDiagVal;
    final private double[] matMaxVal;
    final private int[] matMaxSrc;
    final private int[] matMaxTrgBeg;

    final private double[] matVal;
    final private int[] matSrc;
    final private int[] matTrgBeg;
}