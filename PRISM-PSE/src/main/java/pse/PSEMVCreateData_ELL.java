package pse;

public final class PSEMVCreateData_ELL
{
    public PSEMVCreateData_ELL(int stCnt,
        double[] matPLowerVal, double[] matPUpperVal,
        int[] matPCol, int[] matPRow, int matPRowCnt, int matPColPerRow,
        double[] matNVal,
        int[] matNCol, int[] matNRow, int matNRowCnt, int matNColPerRow)
    {
        this.stCnt = stCnt;
        this.matPLowerVal = matPLowerVal;
        this.matPUpperVal = matPUpperVal;
        this.matPCol = matPCol;
        this.matPRow = matPRow;
        this.matPRowCnt = matPRowCnt;
        this.matPColPerRow = matPColPerRow;

        this.matNVal = matNVal;
        this.matNCol = matNCol;
        this.matNRow = matNRow;
        this.matNRowCnt = matNRowCnt;
        this.matNColPerRow = matNColPerRow;
    }

    public final int stCnt;

    public final double[] matPLowerVal;
    public final double[] matPUpperVal;
    public final int[] matPCol;
    public final int[] matPRow;
    public final int matPRowCnt;
    public final int matPColPerRow;

    public final double[] matNVal;
    public final int[] matNCol;
    public final int[] matNRow;
    public final int matNRowCnt;
    public final int matNColPerRow;
}
