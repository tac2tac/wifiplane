// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.util;


public class Pair
{

    public Pair(Object obj, Object obj1)
    {
        first = obj;
        second = obj1;
    }

    public static Pair create(Object obj, Object obj1)
    {
        return new Pair(obj, obj1);
    }

    private static boolean objectsEqual(Object obj, Object obj1)
    {
        boolean flag;
        if(obj == obj1 || obj != null && obj.equals(obj1))
            flag = true;
        else
            flag = false;
        return flag;
    }

    public boolean equals(Object obj)
    {
        boolean flag = false;
        if(obj instanceof Pair) goto _L2; else goto _L1
_L1:
        boolean flag1 = flag;
_L4:
        return flag1;
_L2:
        obj = (Pair)obj;
        flag1 = flag;
        if(objectsEqual(((Pair) (obj)).first, first))
        {
            flag1 = flag;
            if(objectsEqual(((Pair) (obj)).second, second))
                flag1 = true;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public int hashCode()
    {
        int i = 0;
        int j;
        if(first == null)
            j = 0;
        else
            j = first.hashCode();
        if(second != null)
            i = second.hashCode();
        return j ^ i;
    }

    public final Object first;
    public final Object second;
}
