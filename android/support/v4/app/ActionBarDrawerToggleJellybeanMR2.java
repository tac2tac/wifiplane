// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

class ActionBarDrawerToggleJellybeanMR2
{

    ActionBarDrawerToggleJellybeanMR2()
    {
    }

    public static Drawable getThemeUpIndicator(Activity activity)
    {
        Object obj = activity.getActionBar();
        if(obj != null)
            activity = ((ActionBar) (obj)).getThemedContext();
        obj = activity.obtainStyledAttributes(null, THEME_ATTRS, 0x10102ce, 0);
        activity = ((TypedArray) (obj)).getDrawable(0);
        ((TypedArray) (obj)).recycle();
        return activity;
    }

    public static Object setActionBarDescription(Object obj, Activity activity, int i)
    {
        activity = activity.getActionBar();
        if(activity != null)
            activity.setHomeActionContentDescription(i);
        return obj;
    }

    public static Object setActionBarUpIndicator(Object obj, Activity activity, Drawable drawable, int i)
    {
        activity = activity.getActionBar();
        if(activity != null)
        {
            activity.setHomeAsUpIndicator(drawable);
            activity.setHomeActionContentDescription(i);
        }
        return obj;
    }

    private static final String TAG = "ActionBarDrawerToggleImplJellybeanMR2";
    private static final int THEME_ATTRS[] = {
        0x101030b
    };

}
