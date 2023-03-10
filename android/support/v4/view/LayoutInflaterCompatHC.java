// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import java.lang.reflect.Field;

// Referenced classes of package android.support.v4.view:
//            LayoutInflaterFactory

class LayoutInflaterCompatHC
{
    static class FactoryWrapperHC extends LayoutInflaterCompatBase.FactoryWrapper
        implements android.view.LayoutInflater.Factory2
    {

        public View onCreateView(View view, String s, Context context, AttributeSet attributeset)
        {
            return mDelegateFactory.onCreateView(view, s, context, attributeset);
        }

        FactoryWrapperHC(LayoutInflaterFactory layoutinflaterfactory)
        {
            super(layoutinflaterfactory);
        }
    }


    LayoutInflaterCompatHC()
    {
    }

    static void forceSetFactory2(LayoutInflater layoutinflater, android.view.LayoutInflater.Factory2 factory2)
    {
        if(!sCheckedField)
        {
            try
            {
                sLayoutInflaterFactory2Field = android/view/LayoutInflater.getDeclaredField("mFactory2");
                sLayoutInflaterFactory2Field.setAccessible(true);
            }
            catch(NoSuchFieldException nosuchfieldexception)
            {
                Log.e("LayoutInflaterCompatHC", (new StringBuilder()).append("forceSetFactory2 Could not find field 'mFactory2' on class ").append(android/view/LayoutInflater.getName()).append("; inflation may have unexpected results.").toString(), nosuchfieldexception);
            }
            sCheckedField = true;
        }
        if(sLayoutInflaterFactory2Field == null)
            break MISSING_BLOCK_LABEL_41;
        sLayoutInflaterFactory2Field.set(layoutinflater, factory2);
_L1:
        return;
        factory2;
        Log.e("LayoutInflaterCompatHC", (new StringBuilder()).append("forceSetFactory2 could not set the Factory2 on LayoutInflater ").append(layoutinflater).append("; inflation may have unexpected results.").toString(), factory2);
          goto _L1
    }

    static void setFactory(LayoutInflater layoutinflater, LayoutInflaterFactory layoutinflaterfactory)
    {
        android.view.LayoutInflater.Factory factory;
        if(layoutinflaterfactory != null)
            layoutinflaterfactory = new FactoryWrapperHC(layoutinflaterfactory);
        else
            layoutinflaterfactory = null;
        layoutinflater.setFactory2(layoutinflaterfactory);
        factory = layoutinflater.getFactory();
        if(factory instanceof android.view.LayoutInflater.Factory2)
            forceSetFactory2(layoutinflater, (android.view.LayoutInflater.Factory2)factory);
        else
            forceSetFactory2(layoutinflater, layoutinflaterfactory);
    }

    private static final String TAG = "LayoutInflaterCompatHC";
    private static boolean sCheckedField;
    private static Field sLayoutInflaterFactory2Field;
}
