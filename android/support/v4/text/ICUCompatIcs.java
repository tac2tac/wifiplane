// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.text;

import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

class ICUCompatIcs
{

    ICUCompatIcs()
    {
    }

    private static String addLikelySubtags(Locale locale)
    {
        locale = locale.toString();
        String s;
        if(sAddLikelySubtagsMethod == null)
            break MISSING_BLOCK_LABEL_32;
        s = (String)sAddLikelySubtagsMethod.invoke(null, new Object[] {
            locale
        });
        locale = s;
_L2:
        return locale;
        Object obj;
        obj;
        Log.w("ICUCompatIcs", ((Throwable) (obj)));
        continue; /* Loop/switch isn't completed */
        obj;
        Log.w("ICUCompatIcs", ((Throwable) (obj)));
        if(true) goto _L2; else goto _L1
_L1:
    }

    private static String getScript(String s)
    {
        if(sGetScriptMethod == null) goto _L2; else goto _L1
_L1:
        s = (String)sGetScriptMethod.invoke(null, new Object[] {
            s
        });
_L4:
        return s;
        s;
        Log.w("ICUCompatIcs", s);
_L2:
        s = null;
        if(true) goto _L4; else goto _L3
_L3:
        s;
        Log.w("ICUCompatIcs", s);
          goto _L2
    }

    public static String maximizeAndGetScript(Locale locale)
    {
        locale = addLikelySubtags(locale);
        if(locale != null)
            locale = getScript(locale);
        else
            locale = null;
        return locale;
    }

    private static final String TAG = "ICUCompatIcs";
    private static Method sAddLikelySubtagsMethod;
    private static Method sGetScriptMethod;

    static 
    {
        Class class1 = Class.forName("libcore.icu.ICU");
        if(class1 == null)
            break MISSING_BLOCK_LABEL_46;
        sGetScriptMethod = class1.getMethod("getScript", new Class[] {
            java/lang/String
        });
        sAddLikelySubtagsMethod = class1.getMethod("addLikelySubtags", new Class[] {
            java/lang/String
        });
_L1:
        return;
        Exception exception;
        exception;
        sGetScriptMethod = null;
        sAddLikelySubtagsMethod = null;
        Log.w("ICUCompatIcs", exception);
          goto _L1
    }
}
