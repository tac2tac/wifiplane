// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.opengl.tess;


// Referenced classes of package processing.opengl.tess:
//            PriorityQ, PriorityQHeap

class PriorityQSort extends PriorityQ
{
    private static class Stack
    {

        int p;
        int r;

        private Stack()
        {
        }

    }


    public PriorityQSort(PriorityQ.Leq leq1)
    {
        heap = new PriorityQHeap(leq1);
        keys = new Object[32];
        size = 0;
        max = 32;
        initialized = false;
        leq = leq1;
    }

    private static boolean GT(PriorityQ.Leq leq1, Object obj, Object obj1)
    {
        boolean flag;
        if(!PriorityQ.LEQ(leq1, obj, obj1))
            flag = true;
        else
            flag = false;
        return flag;
    }

    private static boolean LT(PriorityQ.Leq leq1, Object obj, Object obj1)
    {
        boolean flag;
        if(!PriorityQ.LEQ(leq1, obj1, obj))
            flag = true;
        else
            flag = false;
        return flag;
    }

    private static void Swap(int ai[], int i, int j)
    {
        int k = ai[i];
        ai[i] = ai[j];
        ai[j] = k;
    }

    void pqDelete(int i)
    {
        if(i >= 0)
        {
            heap.pqDelete(i);
        } else
        {
            i = -(i + 1);
            if(!$assertionsDisabled && (i >= max || keys[i] == null))
                throw new AssertionError();
            keys[i] = null;
            while(size > 0 && keys[order[size - 1]] == null) 
                size = size - 1;
        }
    }

    void pqDeletePriorityQ()
    {
        if(heap != null)
            heap.pqDeletePriorityQ();
        order = null;
        keys = null;
    }

    Object pqExtractMin()
    {
        if(size != 0) goto _L2; else goto _L1
_L1:
        Object obj = heap.pqExtractMin();
_L4:
        return obj;
_L2:
        Object obj1 = keys[order[size - 1]];
        if(!heap.pqIsEmpty())
        {
            obj = heap.pqMinimum();
            if(LEQ(leq, obj, obj1))
            {
                obj = heap.pqExtractMin();
                continue; /* Loop/switch isn't completed */
            }
        }
        do
        {
            size = size - 1;
            obj = obj1;
            if(size <= 0)
                continue; /* Loop/switch isn't completed */
        } while(keys[order[size - 1]] == null);
        obj = obj1;
        if(true) goto _L4; else goto _L3
_L3:
    }

    boolean pqInit()
    {
        Stack astack[] = new Stack[50];
        for(int i = 0; i < astack.length; i++)
            astack[i] = new Stack();

        int l = 0x7830f0c3;
        order = new int[size + 1];
        int k1 = size - 1;
        int i2 = 0;
        for(int j = 0; j <= k1; j++)
        {
            order[j] = i2;
            i2++;
        }

        astack[0].p = 0;
        astack[0].r = k1;
        int k = 1;
        i2 = l;
        while(--k >= 0) 
        {
            int l1 = astack[k].p;
            int j2;
            for(j2 = astack[k].r; j2 > l1 + 10;)
            {
                int k2 = Math.abs(0x5bc19f0d * i2 + 1);
                i2 = k2 % ((j2 - l1) + 1) + l1;
                int i3 = order[i2];
                order[i2] = order[l1];
                order[l1] = i3;
                int j3 = l1 - 1;
                i2 = j2 + 1;
                int i1;
                int l3;
                do
                {
                    do
                    {
                        l3 = j3 + 1;
                        j3 = l3;
                    } while(GT(leq, keys[order[l3]], keys[i3]));
                    do
                    {
                        i1 = i2 - 1;
                        i2 = i1;
                    } while(LT(leq, keys[order[i1]], keys[i3]));
                    Swap(order, l3, i1);
                    i2 = i1;
                    j3 = l3;
                } while(l3 < i1);
                Swap(order, l3, i1);
                if(l3 - l1 < j2 - i1)
                {
                    astack[k].p = i1 + 1;
                    astack[k].r = j2;
                    k++;
                    j2 = l3 - 1;
                    i2 = k2;
                } else
                {
                    astack[k].p = l1;
                    astack[k].r = l3 - 1;
                    k++;
                    l1 = i1 + 1;
                    i2 = k2;
                }
            }

            int j1 = l1 + 1;
            while(j1 <= j2) 
            {
                int l2 = order[j1];
                int k3;
                for(k3 = j1; k3 > l1 && LT(leq, keys[order[k3 - 1]], keys[l2]); k3--)
                    order[k3] = order[k3 - 1];

                order[k3] = l2;
                j1++;
            }
        }
        max = size;
        initialized = true;
        heap.pqInit();
        return true;
    }

    int pqInsert(Object obj)
    {
        int i = 0x7fffffff;
        if(!initialized) goto _L2; else goto _L1
_L1:
        i = heap.pqInsert(obj);
_L4:
        return i;
_L2:
        int j = size;
        int k = size + 1;
        size = k;
        if(k >= max)
        {
            Object aobj[] = keys;
            max = max << 1;
            Object aobj1[] = new Object[max];
            System.arraycopy(((Object) (keys)), 0, ((Object) (aobj1)), 0, keys.length);
            keys = aobj1;
            if(keys == null)
            {
                keys = aobj;
                continue; /* Loop/switch isn't completed */
            }
        }
        if(!$assertionsDisabled && j == 0x7fffffff)
            throw new AssertionError();
        keys[j] = obj;
        i = -(j + 1);
        if(true) goto _L4; else goto _L3
_L3:
    }

    boolean pqIsEmpty()
    {
        boolean flag;
        if(size == 0 && heap.pqIsEmpty())
            flag = true;
        else
            flag = false;
        return flag;
    }

    Object pqMinimum()
    {
        if(size != 0) goto _L2; else goto _L1
_L1:
        Object obj = heap.pqMinimum();
_L4:
        return obj;
_L2:
        Object obj1 = keys[order[size - 1]];
        if(!heap.pqIsEmpty())
        {
            Object obj2 = heap.pqMinimum();
            obj = obj2;
            if(PriorityQ.LEQ(leq, obj2, obj1))
                continue; /* Loop/switch isn't completed */
        }
        obj = obj1;
        if(true) goto _L4; else goto _L3
_L3:
    }

    static final boolean $assertionsDisabled;
    PriorityQHeap heap;
    boolean initialized;
    Object keys[];
    PriorityQ.Leq leq;
    int max;
    int order[];
    int size;

    static 
    {
        boolean flag;
        if(!processing/opengl/tess/PriorityQSort.desiredAssertionStatus())
            flag = true;
        else
            flag = false;
        $assertionsDisabled = flag;
    }
}
