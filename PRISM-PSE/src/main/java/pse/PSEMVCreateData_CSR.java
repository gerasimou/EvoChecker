package pse;

public final class PSEMVCreateData_CSR
{
    public PSEMVCreateData_CSR
        ( int stCnt

        , double[] matPLowerVal
                    , double[] matPUpperVal
                    , int[] matIOCol
                    , int[] matIORow
                    , int[] matIORowBeg
                    , int matIORowCnt

                    , double[] matNPVal
                    , int[] matNPCol
                    , int[] matNPRow
                    , int[] matNPRowBeg
                    , int matNPRowCnt
            )
    {
        this.stCnt = stCnt;
        this.matPLowerVal = matPLowerVal;
        this.matPUpperVal = matPUpperVal;
        this.matIOCol = matIOCol;
        this.matIORow = matIORow;
        this.matIORowBeg = matIORowBeg;
        this.matIORowCnt = matIORowCnt;
        this.matNPVal = matNPVal;
        this.matNPCol = matNPCol;
        this.matNPRow = matNPRow;
        this.matNPRowBeg = matNPRowBeg;
        this.matNPRowCnt = matNPRowCnt;
    }

    public final int stCnt;

    public final double[] matPLowerVal;
    public final double[] matPUpperVal;
    public final int[] matIOCol;
    public final int[] matIORow;
    public final int[] matIORowBeg;
    public final int matIORowCnt;

    public final double[] matNPVal;
    public final int[] matNPCol;
    public final int[] matNPRow;
    public final int[] matNPRowBeg;
    public final int matNPRowCnt;
}
