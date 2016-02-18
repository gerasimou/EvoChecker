package pse;

import java.util.HashSet;

public class Releaser implements Releaseable
{
    public Releaser()
    {
        toBeReleased = new HashSet<Object>();
    }

    public void releaseLater(Releaseable obj)
    {
        if (obj != null) {
            toBeReleased.add((Object)obj);
        }
    }

    @Override
    public void release()
    {
        for (Object obj : toBeReleased) {
            ((Releaseable)obj).release();
        }
    }

    final private HashSet<Object> toBeReleased;
}
