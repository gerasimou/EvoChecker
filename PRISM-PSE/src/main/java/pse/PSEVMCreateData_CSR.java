package pse;

public class PSEVMCreateData_CSR
{
    public PSEVMCreateData_CSR
        ( int stCnt

        , double[] matIOLowerVal0
        , double[] matIOLowerVal1
        , double[] matIOUpperVal0
        , double[] matIOUpperVal1
        , int[] matIOSrc
        , int[] matIOTrgBeg

        , double[] matIMinVal
        , double[] matIMaxVal
        , int[] matISrc
        , int[] matITrgBeg

        , double[] matOMinDiagVal
        , double[] matOMaxDiagVal
        , double[] matNPVal
        , int[] matNPSrc
        , int[] matNPTrgBeg
        )
    {
        this.stCnt = stCnt;

        this.matIOLowerVal0 = matIOLowerVal0;
        this.matIOLowerVal1 = matIOLowerVal1;
        this.matIOUpperVal0 = matIOUpperVal0;
        this.matIOUpperVal1 = matIOUpperVal1;
        this.matIOSrc = matIOSrc;
        this.matIOTrgBeg = matIOTrgBeg;

        this.matIMinVal = matIMinVal;
        this.matIMaxVal = matIMaxVal;
        this.matISrc = matISrc;
        this.matITrgBeg = matITrgBeg;

        this.matOMinDiagVal = matOMinDiagVal;
        this.matOMaxDiagVal = matOMaxDiagVal;
        this.matNPVal = matNPVal;
        this.matNPSrc = matNPSrc;
        this.matNPTrgBeg = matNPTrgBeg;
    }

    final public int stCnt;

    final public double[] matIOLowerVal0;
    final public double[] matIOLowerVal1;
    final public double[] matIOUpperVal0;
    final public double[] matIOUpperVal1;
    final public int[] matIOSrc;
    final public int[] matIOTrgBeg;

    final public double[] matIMinVal;
    final public double[] matIMaxVal;
    final public int[] matISrc;
    final public int[] matITrgBeg;

    final public double[] matOMinDiagVal;
    final public double[] matOMaxDiagVal;
    final public double[] matNPVal;
    final public int[] matNPSrc;
    final public int[] matNPTrgBeg;
}
