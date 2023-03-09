// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.data;


public abstract class Sort
    implements Runnable
{

    public Sort()
    {
    }

    public abstract float compare(int i, int j);

    protected int partition(int i, int j)
    {
        int k = j;
        int l = i;
        int i1;
        do
        {
            do
            {
                i1 = l + 1;
                l = i1;
            } while(compare(i1, j) < 0.0F);
            do
            {
                i = k;
                if(k == 0)
                    break;
                i = k - 1;
                k = i;
            } while(compare(i, j) > 0.0F);
            swap(i1, i);
            k = i;
            l = i1;
        } while(i1 < i);
        swap(i1, i);
        return i1;
    }

    public void run()
    {
        int i = size();
        if(i > 1)
            sort(0, i - 1);
    }

    public abstract int size();

    protected void sort(int i, int j)
    {
        swap((i + j) / 2, j);
        int k = partition(i - 1, j);
        swap(k, j);
        if(k - i > 1)
            sort(i, k - 1);
        if(j - k > 1)
            sort(k + 1, j);
    }

    public abstract void swap(int i, int j);
}
