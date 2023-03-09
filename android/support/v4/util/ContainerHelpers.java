// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.util;


class ContainerHelpers
{

    ContainerHelpers()
    {
    }

    static int binarySearch(int ai[], int i, int j)
    {
        int k;
label0:
        {
            k = 0;
            int l = i - 1;
            i = k;
            for(k = l; i <= k;)
            {
                int i1 = i + k >>> 1;
                int j1 = ai[i1];
                if(j1 < j)
                {
                    i = i1 + 1;
                } else
                {
                    k = i1;
                    if(j1 <= j)
                        break label0;
                    k = i1 - 1;
                }
            }

            k = ~i;
        }
        return k;
    }

    static int binarySearch(long al[], int i, long l)
    {
        int k;
label0:
        {
            boolean flag = false;
            k = i - 1;
            for(i = ((flag) ? 1 : 0); i <= k;)
            {
                int j = i + k >>> 1;
                long l1 = al[j];
                if(l1 < l)
                {
                    i = j + 1;
                } else
                {
                    k = j;
                    if(l1 <= l)
                        break label0;
                    k = j - 1;
                }
            }

            k = ~i;
        }
        return k;
    }

    public static boolean equal(Object obj, Object obj1)
    {
        boolean flag;
        if(obj == obj1 || obj != null && obj.equals(obj1))
            flag = true;
        else
            flag = false;
        return flag;
    }

    public static int idealByteArraySize(int i)
    {
        int j = 4;
        do
        {
label0:
            {
                int k = i;
                if(j < 32)
                {
                    if(i > (1 << j) - 12)
                        break label0;
                    k = (1 << j) - 12;
                }
                return k;
            }
            j++;
        } while(true);
    }

    public static int idealIntArraySize(int i)
    {
        return idealByteArraySize(i * 4) / 4;
    }

    public static int idealLongArraySize(int i)
    {
        return idealByteArraySize(i * 8) / 8;
    }

    static final int EMPTY_INTS[] = new int[0];
    static final long EMPTY_LONGS[] = new long[0];
    static final Object EMPTY_OBJECTS[] = new Object[0];

}
