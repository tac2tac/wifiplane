// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

abstract class BaseFragmentActivityDonut extends Activity
{

    BaseFragmentActivityDonut()
    {
    }

    abstract View dispatchFragmentsOnCreateView(View view, String s, Context context, AttributeSet attributeset);

    protected void onCreate(Bundle bundle)
    {
        if(android.os.Build.VERSION.SDK_INT < 11 && getLayoutInflater().getFactory() == null)
            getLayoutInflater().setFactory(this);
        super.onCreate(bundle);
    }

    public View onCreateView(String s, Context context, AttributeSet attributeset)
    {
        View view = dispatchFragmentsOnCreateView(null, s, context, attributeset);
        View view1 = view;
        if(view == null)
            view1 = super.onCreateView(s, context, attributeset);
        return view1;
    }
}
