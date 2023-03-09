// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.opengl.tess;


// Referenced classes of package processing.opengl.tess:
//            GLUvertex, Geom, PriorityQSort

abstract class PriorityQ
{
    public static interface Leq
    {

        public abstract boolean leq(Object obj, Object obj1);
    }

    public static class PQhandleElem
    {

        Object key;
        int node;

        public PQhandleElem()
        {
        }
    }

    public static class PQnode
    {

        int handle;

        public PQnode()
        {
        }
    }


    PriorityQ()
    {
    }

    public static boolean LEQ(Leq leq, Object obj, Object obj1)
    {
        return Geom.VertLeq((GLUvertex)obj, (GLUvertex)obj1);
    }

    static PriorityQ pqNewPriorityQ(Leq leq)
    {
        return new PriorityQSort(leq);
    }

    abstract void pqDelete(int i);

    abstract void pqDeletePriorityQ();

    abstract Object pqExtractMin();

    abstract boolean pqInit();

    abstract int pqInsert(Object obj);

    abstract boolean pqIsEmpty();

    abstract Object pqMinimum();

    public static final int INIT_SIZE = 32;
}
