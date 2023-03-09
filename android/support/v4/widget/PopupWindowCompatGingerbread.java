// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.widget;

import android.widget.PopupWindow;
import java.lang.reflect.Method;

class PopupWindowCompatGingerbread
{

    PopupWindowCompatGingerbread()
    {
    }

    static int getWindowLayoutType(PopupWindow popupwindow)
    {
        if(!sGetWindowLayoutTypeMethodAttempted)
        {
            int i;
            try
            {
                sGetWindowLayoutTypeMethod = android/widget/PopupWindow.getDeclaredMethod("getWindowLayoutType", new Class[0]);
                sGetWindowLayoutTypeMethod.setAccessible(true);
            }
            catch(Exception exception) { }
            sGetWindowLayoutTypeMethodAttempted = true;
        }
        if(sGetWindowLayoutTypeMethod == null) goto _L2; else goto _L1
_L1:
        i = ((Integer)sGetWindowLayoutTypeMethod.invoke(popupwindow, new Object[0])).intValue();
_L4:
        return i;
        popupwindow;
_L2:
        i = 0;
        if(true) goto _L4; else goto _L3
_L3:
    }

    static void setWindowLayoutType(PopupWindow popupwindow, int i)
    {
        if(!sSetWindowLayoutTypeMethodAttempted)
        {
            try
            {
                sSetWindowLayoutTypeMethod = android/widget/PopupWindow.getDeclaredMethod("setWindowLayoutType", new Class[] {
                    Integer.TYPE
                });
                sSetWindowLayoutTypeMethod.setAccessible(true);
            }
            catch(Exception exception) { }
            sSetWindowLayoutTypeMethodAttempted = true;
        }
        if(sSetWindowLayoutTypeMethod == null)
            break MISSING_BLOCK_LABEL_62;
        sSetWindowLayoutTypeMethod.invoke(popupwindow, new Object[] {
            Integer.valueOf(i)
        });
_L2:
        return;
        popupwindow;
        if(true) goto _L2; else goto _L1
_L1:
    }

    private static Method sGetWindowLayoutTypeMethod;
    private static boolean sGetWindowLayoutTypeMethodAttempted;
    private static Method sSetWindowLayoutTypeMethod;
    private static boolean sSetWindowLayoutTypeMethodAttempted;
}
