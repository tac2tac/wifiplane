// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.util;


public class DebugUtils
{

    public DebugUtils()
    {
    }

    public static void buildShortClassTag(Object obj, StringBuilder stringbuilder)
    {
        if(obj != null) goto _L2; else goto _L1
_L1:
        stringbuilder.append("null");
_L4:
        return;
_L2:
        String s1;
label0:
        {
            String s = obj.getClass().getSimpleName();
            if(s != null)
            {
                s1 = s;
                if(s.length() > 0)
                    break label0;
            }
            s = obj.getClass().getName();
            int i = s.lastIndexOf('.');
            s1 = s;
            if(i > 0)
                s1 = s.substring(i + 1);
        }
        stringbuilder.append(s1);
        stringbuilder.append('{');
        stringbuilder.append(Integer.toHexString(System.identityHashCode(obj)));
        if(true) goto _L4; else goto _L3
_L3:
    }
}
