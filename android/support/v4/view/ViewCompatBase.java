// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.view;

import android.content.res.ColorStateList;
import android.view.View;
import java.lang.reflect.Field;

// Referenced classes of package android.support.v4.view:
//            TintableBackgroundView

class ViewCompatBase
{

    ViewCompatBase()
    {
    }

    static ColorStateList getBackgroundTintList(View view)
    {
        if(view instanceof TintableBackgroundView)
            view = ((TintableBackgroundView)view).getSupportBackgroundTintList();
        else
            view = null;
        return view;
    }

    static android.graphics.PorterDuff.Mode getBackgroundTintMode(View view)
    {
        if(view instanceof TintableBackgroundView)
            view = ((TintableBackgroundView)view).getSupportBackgroundTintMode();
        else
            view = null;
        return view;
    }

    static int getMinimumHeight(View view)
    {
        if(!sMinHeightFieldFetched)
        {
            int i;
            try
            {
                sMinHeightField = android/view/View.getDeclaredField("mMinHeight");
                sMinHeightField.setAccessible(true);
            }
            catch(NoSuchFieldException nosuchfieldexception) { }
            sMinHeightFieldFetched = true;
        }
        if(sMinHeightField == null) goto _L2; else goto _L1
_L1:
        i = ((Integer)sMinHeightField.get(view)).intValue();
_L4:
        return i;
        view;
_L2:
        i = 0;
        if(true) goto _L4; else goto _L3
_L3:
    }

    static int getMinimumWidth(View view)
    {
        if(!sMinWidthFieldFetched)
        {
            int i;
            try
            {
                sMinWidthField = android/view/View.getDeclaredField("mMinWidth");
                sMinWidthField.setAccessible(true);
            }
            catch(NoSuchFieldException nosuchfieldexception) { }
            sMinWidthFieldFetched = true;
        }
        if(sMinWidthField == null) goto _L2; else goto _L1
_L1:
        i = ((Integer)sMinWidthField.get(view)).intValue();
_L4:
        return i;
        view;
_L2:
        i = 0;
        if(true) goto _L4; else goto _L3
_L3:
    }

    static boolean isAttachedToWindow(View view)
    {
        boolean flag;
        if(view.getWindowToken() != null)
            flag = true;
        else
            flag = false;
        return flag;
    }

    static boolean isLaidOut(View view)
    {
        boolean flag;
        if(view.getWidth() > 0 && view.getHeight() > 0)
            flag = true;
        else
            flag = false;
        return flag;
    }

    static void offsetLeftAndRight(View view, int i)
    {
        int j = view.getLeft();
        view.offsetLeftAndRight(i);
        if(i != 0)
        {
            android.view.ViewParent viewparent = view.getParent();
            if(viewparent instanceof View)
            {
                i = Math.abs(i);
                ((View)viewparent).invalidate(j - i, view.getTop(), view.getWidth() + j + i, view.getBottom());
            } else
            {
                view.invalidate();
            }
        }
    }

    static void offsetTopAndBottom(View view, int i)
    {
        int j = view.getTop();
        view.offsetTopAndBottom(i);
        if(i != 0)
        {
            android.view.ViewParent viewparent = view.getParent();
            if(viewparent instanceof View)
            {
                i = Math.abs(i);
                ((View)viewparent).invalidate(view.getLeft(), j - i, view.getRight(), view.getHeight() + j + i);
            } else
            {
                view.invalidate();
            }
        }
    }

    static void setBackgroundTintList(View view, ColorStateList colorstatelist)
    {
        if(view instanceof TintableBackgroundView)
            ((TintableBackgroundView)view).setSupportBackgroundTintList(colorstatelist);
    }

    static void setBackgroundTintMode(View view, android.graphics.PorterDuff.Mode mode)
    {
        if(view instanceof TintableBackgroundView)
            ((TintableBackgroundView)view).setSupportBackgroundTintMode(mode);
    }

    private static final String TAG = "ViewCompatBase";
    private static Field sMinHeightField;
    private static boolean sMinHeightFieldFetched;
    private static Field sMinWidthField;
    private static boolean sMinWidthFieldFetched;
}
