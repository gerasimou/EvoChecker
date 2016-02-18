package pse;

import java.util.BitSet;

public class Utility
{
    public static long leastGreaterMultiple(long x, long z)
    {
        return x + (z - x % z) % z;
    }
    public static int leastGreaterMultiple(int x, int z)
    {
        return x + (z - x % z) % z;
    }

    public static BitSet makeBitSetCopy(BitSet original, int n)
    {
        if (original == null) {
            original = new BitSet(n);
            original.set(0, n);
            return original;
        }
        return (BitSet)original.clone();
    }

    public static BitSet makeBitSetComplement(BitSet original, boolean complement, int n)
    {
        if (original == null) {
            original = new BitSet(n);
            original.set(complement ? 1 : 0, n);
            return original;
        }

        original = (BitSet)original.clone();
        if (complement) {
            original.flip(0, n);
        }
        return original;
    }
}
