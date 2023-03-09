// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.opengl.tess;


// Referenced classes of package processing.opengl.tess:
//            PriorityQ

class PriorityQHeap extends PriorityQ
{

    public PriorityQHeap(PriorityQ.Leq leq1)
    {
        size = 0;
        max = 32;
        nodes = new PriorityQ.PQnode[33];
        for(int i = 0; i < nodes.length; i++)
            nodes[i] = new PriorityQ.PQnode();

        handles = new PriorityQ.PQhandleElem[33];
        for(int j = 0; j < handles.length; j++)
            handles[j] = new PriorityQ.PQhandleElem();

        initialized = false;
        freeList = 0;
        leq = leq1;
        nodes[1].handle = 1;
        handles[1].key = null;
    }

    void FloatDown(int i)
    {
        PriorityQ.PQnode apqnode[] = nodes;
        PriorityQ.PQhandleElem apqhandleelem[] = handles;
        int j = apqnode[i].handle;
        int k = i;
        do
        {
            int l = k << 1;
            i = l;
            if(l < size)
            {
                i = l;
                if(LEQ(leq, apqhandleelem[apqnode[l + 1].handle].key, apqhandleelem[apqnode[l].handle].key))
                    i = l + 1;
            }
            if(!$assertionsDisabled && i > max)
                throw new AssertionError();
            l = apqnode[i].handle;
            if(i > size || LEQ(leq, apqhandleelem[j].key, apqhandleelem[l].key))
            {
                apqnode[k].handle = j;
                apqhandleelem[j].node = k;
                return;
            }
            apqnode[k].handle = l;
            apqhandleelem[l].node = k;
            k = i;
        } while(true);
    }

    void FloatUp(int i)
    {
        PriorityQ.PQnode apqnode[] = nodes;
        PriorityQ.PQhandleElem apqhandleelem[] = handles;
        int j = apqnode[i].handle;
        do
        {
            int k = i >> 1;
            int l = apqnode[k].handle;
            if(k == 0 || LEQ(leq, apqhandleelem[l].key, apqhandleelem[j].key))
            {
                apqnode[i].handle = j;
                apqhandleelem[j].node = i;
                return;
            }
            apqnode[i].handle = l;
            apqhandleelem[l].node = i;
            i = k;
        } while(true);
    }

    void pqDelete(int i)
    {
        PriorityQ.PQnode apqnode[] = nodes;
        PriorityQ.PQhandleElem apqhandleelem[] = handles;
        if(!$assertionsDisabled && (i < 1 || i > max || apqhandleelem[i].key == null))
            throw new AssertionError();
        int j = apqhandleelem[i].node;
        apqnode[j].handle = apqnode[size].handle;
        apqhandleelem[apqnode[j].handle].node = j;
        int k = size - 1;
        size = k;
        if(j <= k)
            if(j <= 1 || LEQ(leq, apqhandleelem[apqnode[j >> 1].handle].key, apqhandleelem[apqnode[j].handle].key))
                FloatDown(j);
            else
                FloatUp(j);
        apqhandleelem[i].key = null;
        apqhandleelem[i].node = freeList;
        freeList = i;
    }

    void pqDeletePriorityQ()
    {
        handles = null;
        nodes = null;
    }

    Object pqExtractMin()
    {
        PriorityQ.PQnode apqnode[] = nodes;
        PriorityQ.PQhandleElem apqhandleelem[] = handles;
        int i = apqnode[1].handle;
        Object obj = apqhandleelem[i].key;
        if(size > 0)
        {
            apqnode[1].handle = apqnode[size].handle;
            apqhandleelem[apqnode[1].handle].node = 1;
            apqhandleelem[i].key = null;
            apqhandleelem[i].node = freeList;
            freeList = i;
            i = size - 1;
            size = i;
            if(i > 0)
                FloatDown(1);
        }
        return obj;
    }

    boolean pqInit()
    {
        for(int i = size; i >= 1; i--)
            FloatDown(i);

        initialized = true;
        return true;
    }

    int pqInsert(Object obj)
    {
        int i;
        i = size + 1;
        size = i;
        if(i * 2 <= max) goto _L2; else goto _L1
_L1:
        PriorityQ.PQnode apqnode[];
        PriorityQ.PQhandleElem apqhandleelem[];
        apqnode = nodes;
        apqhandleelem = handles;
        max = max << 1;
        PriorityQ.PQnode apqnode1[] = new PriorityQ.PQnode[max + 1];
        System.arraycopy(nodes, 0, apqnode1, 0, nodes.length);
        for(int j = nodes.length; j < apqnode1.length; j++)
            apqnode1[j] = new PriorityQ.PQnode();

        nodes = apqnode1;
        if(nodes != null) goto _L4; else goto _L3
_L3:
        nodes = apqnode;
        i = 0x7fffffff;
_L5:
        return i;
_L4:
        PriorityQ.PQhandleElem apqhandleelem1[] = new PriorityQ.PQhandleElem[max + 1];
        System.arraycopy(handles, 0, apqhandleelem1, 0, handles.length);
        for(int k = handles.length; k < apqhandleelem1.length; k++)
            apqhandleelem1[k] = new PriorityQ.PQhandleElem();

        handles = apqhandleelem1;
        if(handles != null)
            break; /* Loop/switch isn't completed */
        handles = apqhandleelem;
        i = 0x7fffffff;
        if(true) goto _L5; else goto _L2
_L2:
        int l;
        if(freeList == 0)
        {
            l = i;
        } else
        {
            l = freeList;
            freeList = handles[l].node;
        }
        nodes[i].handle = l;
        handles[l].node = i;
        handles[l].key = obj;
        if(initialized)
            FloatUp(i);
        i = l;
        if(!$assertionsDisabled)
        {
            i = l;
            if(l == 0x7fffffff)
                throw new AssertionError();
        }
        if(true) goto _L5; else goto _L6
_L6:
    }

    boolean pqIsEmpty()
    {
        boolean flag;
        if(size == 0)
            flag = true;
        else
            flag = false;
        return flag;
    }

    Object pqMinimum()
    {
        return handles[nodes[1].handle].key;
    }

    static final boolean $assertionsDisabled;
    int freeList;
    PriorityQ.PQhandleElem handles[];
    boolean initialized;
    PriorityQ.Leq leq;
    int max;
    PriorityQ.PQnode nodes[];
    int size;

    static 
    {
        boolean flag;
        if(!processing/opengl/tess/PriorityQHeap.desiredAssertionStatus())
            flag = true;
        else
            flag = false;
        $assertionsDisabled = flag;
    }
}
