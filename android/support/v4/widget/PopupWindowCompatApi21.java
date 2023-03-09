// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.widget;

import android.util.Log;
import android.widget.PopupWindow;
import java.lang.reflect.Field;

class PopupWindowCompatApi21
{

    PopupWindowCompatApi21()
    {
    }

    static boolean getOverlapAnchor(PopupWindow popupwindow)
    {
        if(sOverlapAnchorField == null) goto _L2; else goto _L1
_L1:
        boolean flag = ((Boolean)sOverlapAnchorField.get(popupwindow)).booleanValue();
_L4:
        return flag;
        popupwindow;
        Log.i("PopupWindowCompatApi21", "Could not get overlap anchor field in PopupWindow", popupwindow);
_L2:
        flag = false;
        if(true) goto _L4; else goto _L3
_L3:
    }

    static void setOverlapAnchor(PopupWindow popupwindow, boolean flag)
    {
        if(sOverlapAnchorField == null)
            break MISSING_BLOCK_LABEL_17;
        sOverlapAnchorField.set(popupwindow, Boolean.valueOf(flag));
_L1:
        return;
        popupwindow;
        Log.i("PopupWindowCompatApi21", "Could not set overlap anchor field in PopupWindow", popupwindow);
          goto _L1
    }

    private static final String TAG = "PopupWindowCompatApi21";
    private static Field sOverlapAnchorField;

    static 
    {
        sOverlapAnchorField = android/widget/PopupWindow.getDeclaredField("mOverlapAnchor");
        sOverlapAnchorField.setAccessible(true);
_L1:
        return;
        NoSuchFieldException nosuchfieldexception;
        nosuchfieldexception;
        Log.i("PopupWindowCompatApi21", "Could not fetch mOverlapAnchor field from PopupWindow", nosuchfieldexception);
          goto _L1
    }
}
