// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.view;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ViewCompatEclairMr1
{

    ViewCompatEclairMr1()
    {
    }

    public static boolean isOpaque(View view)
    {
        return view.isOpaque();
    }

    public static void setChildrenDrawingOrderEnabled(ViewGroup viewgroup, boolean flag)
    {
        if(sChildrenDrawingOrderMethod == null)
        {
            try
            {
                sChildrenDrawingOrderMethod = android/view/ViewGroup.getDeclaredMethod("setChildrenDrawingOrderEnabled", new Class[] {
                    Boolean.TYPE
                });
            }
            catch(NoSuchMethodException nosuchmethodexception)
            {
                Log.e("ViewCompat", "Unable to find childrenDrawingOrderEnabled", nosuchmethodexception);
            }
            sChildrenDrawingOrderMethod.setAccessible(true);
        }
        sChildrenDrawingOrderMethod.invoke(viewgroup, new Object[] {
            Boolean.valueOf(flag)
        });
_L1:
        return;
        viewgroup;
        Log.e("ViewCompat", "Unable to invoke childrenDrawingOrderEnabled", viewgroup);
          goto _L1
        viewgroup;
        Log.e("ViewCompat", "Unable to invoke childrenDrawingOrderEnabled", viewgroup);
          goto _L1
        viewgroup;
        Log.e("ViewCompat", "Unable to invoke childrenDrawingOrderEnabled", viewgroup);
          goto _L1
    }

    public static final String TAG = "ViewCompat";
    private static Method sChildrenDrawingOrderMethod;
}
