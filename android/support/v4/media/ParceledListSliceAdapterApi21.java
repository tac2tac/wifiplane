// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.media;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

class ParceledListSliceAdapterApi21
{

    ParceledListSliceAdapterApi21()
    {
    }

    static Object newInstance(List list)
    {
        Object obj = null;
        list = ((List) (sConstructor.newInstance(new Object[] {
            list
        })));
_L2:
        return list;
        list;
_L3:
        list.printStackTrace();
        list = obj;
        if(true) goto _L2; else goto _L1
_L1:
        list;
          goto _L3
        list;
          goto _L3
    }

    private static Constructor sConstructor = Class.forName("android.content.pm.ParceledListSlice").getConstructor(new Class[] {
        java/util/List
    });

    static 
    {
_L1:
        return;
        Object obj;
        obj;
_L2:
        ((ReflectiveOperationException) (obj)).printStackTrace();
          goto _L1
        obj;
          goto _L2
    }
}
