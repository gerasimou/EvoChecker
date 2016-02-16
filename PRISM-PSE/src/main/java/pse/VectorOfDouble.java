package pse;

public class VectorOfDouble
{
    public VectorOfDouble()
    {
        this(16);
    }

    public VectorOfDouble(int capacity)
    {
        this.capacity = capacity;
        this.size = 0;
        this.data = new double[capacity];
    }

    public void pushBack(double val)
    {
        if (capacity <= size)
        {
            grow();
        }
        data[size++] = val;
    }

    public void pushBack(int n, double val)
    {
        for (int i = 0; i < n; ++i) {
            pushBack(val);
        }
    }

    public void at(int n, double val) {
        data[n] = val;
    }

    public int capacity()
    {
        return capacity;
    }

    public int size()
    {
        return size;
    }

    public double[] data()
    {
        return data;
    }

    private void grow()
    {
        capacity *= 1.5;
        double[] dataOld = data;
        data = new double[capacity];
        System.arraycopy(dataOld, 0, data, 0, size);
    }

    private int capacity;
    private int size;
    private double[] data;
}
