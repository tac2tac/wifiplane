// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.widget;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.CompoundButton;
import java.lang.reflect.Field;

// Referenced classes of package android.support.v4.widget:
//            TintableCompoundButton

class CompoundButtonCompatDonut
{

    CompoundButtonCompatDonut()
    {
    }

    static Drawable getButtonDrawable(CompoundButton compoundbutton)
    {
        if(!sButtonDrawableFieldFetched)
        {
            try
            {
                sButtonDrawableField = android/widget/CompoundButton.getDeclaredField("mButtonDrawable");
                sButtonDrawableField.setAccessible(true);
            }
            catch(NoSuchFieldException nosuchfieldexception)
            {
                Log.i("CompoundButtonCompatDonut", "Failed to retrieve mButtonDrawable field", nosuchfieldexception);
            }
            sButtonDrawableFieldFetched = true;
        }
        if(sButtonDrawableField == null) goto _L2; else goto _L1
_L1:
        compoundbutton = (Drawable)sButtonDrawableField.get(compoundbutton);
_L4:
        return compoundbutton;
        compoundbutton;
        Log.i("CompoundButtonCompatDonut", "Failed to get button drawable via reflection", compoundbutton);
        sButtonDrawableField = null;
_L2:
        compoundbutton = null;
        if(true) goto _L4; else goto _L3
_L3:
    }

    static ColorStateList getButtonTintList(CompoundButton compoundbutton)
    {
        if(compoundbutton instanceof TintableCompoundButton)
            compoundbutton = ((TintableCompoundButton)compoundbutton).getSupportButtonTintList();
        else
            compoundbutton = null;
        return compoundbutton;
    }

    static android.graphics.PorterDuff.Mode getButtonTintMode(CompoundButton compoundbutton)
    {
        if(compoundbutton instanceof TintableCompoundButton)
            compoundbutton = ((TintableCompoundButton)compoundbutton).getSupportButtonTintMode();
        else
            compoundbutton = null;
        return compoundbutton;
    }

    static void setButtonTintList(CompoundButton compoundbutton, ColorStateList colorstatelist)
    {
        if(compoundbutton instanceof TintableCompoundButton)
            ((TintableCompoundButton)compoundbutton).setSupportButtonTintList(colorstatelist);
    }

    static void setButtonTintMode(CompoundButton compoundbutton, android.graphics.PorterDuff.Mode mode)
    {
        if(compoundbutton instanceof TintableCompoundButton)
            ((TintableCompoundButton)compoundbutton).setSupportButtonTintMode(mode);
    }

    private static final String TAG = "CompoundButtonCompatDonut";
    private static Field sButtonDrawableField;
    private static boolean sButtonDrawableFieldFetched;
}
