package pse;

public final class PSEMVCreateData_ELLFW
{
    public PSEMVCreateData_ELLFW(int stCnt, int warpSize,
        double[] matPLowerVal, double[] matPUpperVal,
        int[] matPCol, int[] matPRow, int[] matPSegOff,
        int matPN, int matPNrem, int matPRowCnt, int matPValCnt,

        double[] matNVal,
        int[] matNCol, int[] matNRow, int[] matNSegOff,
        int matNN, int matNNrem, int matNRowCnt, int matNValCnt)
    {
        this.stCnt = stCnt;
        this.warpSize = warpSize;

        this.matPLowerVal = matPLowerVal;
        this.matPUpperVal = matPUpperVal;
        this.matPCol = matPCol;
        this.matPRow = matPRow;
        this.matPSegOff = matPSegOff;
        this.matPN = matPN;
        this.matPNrem = matPNrem;
        this.matPRowCnt = matPRowCnt;
        this.matPValCnt = matPValCnt;

        this.matNVal = matNVal;
        this.matNCol = matNCol;
        this.matNRow = matNRow;
        this.matNSegOff = matNSegOff;
        this.matNN = matNN;
        this.matNNrem = matNNrem;
        this.matNRowCnt = matNRowCnt;
        this.matNValCnt = matNValCnt;
    }

    public final int stCnt;
    public final int warpSize;

    public final double[] matPLowerVal;
    public final double[] matPUpperVal;
    public final int[] matPCol;
    public final int[] matPRow;
    public final int[] matPSegOff;
    public final int matPN;
    public final int matPNrem;
    public final int matPRowCnt;
    public final int matPValCnt;

    public final double[] matNVal;
    public final int[] matNCol;
    public final int[] matNRow;
    public final int[] matNSegOff;
    public final int matNN;
    public final int matNNrem;
    public final int matNRowCnt;
    public final int matNValCnt;
}
