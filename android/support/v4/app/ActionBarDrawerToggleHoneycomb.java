// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.lang.reflect.Method;

class ActionBarDrawerToggleHoneycomb
{
    private static class SetIndicatorInfo
    {

        public Method setHomeActionContentDescription;
        public Method setHomeAsUpIndicator;
        public ImageView upIndicatorView;

        SetIndicatorInfo(Activity activity)
        {
            setHomeAsUpIndicator = android/app/ActionBar.getDeclaredMethod("setHomeAsUpIndicator", new Class[] {
                android/graphics/drawable/Drawable
            });
            setHomeActionContentDescription = android/app/ActionBar.getDeclaredMethod("setHomeActionContentDescription", new Class[] {
                Integer.TYPE
            });
_L1:
            return;
            NoSuchMethodException nosuchmethodexception;
            nosuchmethodexception;
            activity = activity.findViewById(0x102002c);
            if(activity != null)
            {
                Object obj = (ViewGroup)activity.getParent();
                if(((ViewGroup) (obj)).getChildCount() == 2)
                {
                    activity = ((ViewGroup) (obj)).getChildAt(0);
                    obj = ((ViewGroup) (obj)).getChildAt(1);
                    if(activity.getId() == 0x102002c)
                        activity = ((Activity) (obj));
                    if(activity instanceof ImageView)
                        upIndicatorView = (ImageView)activity;
                }
            }
              goto _L1
        }
    }


    ActionBarDrawerToggleHoneycomb()
    {
    }

    public static Drawable getThemeUpIndicator(Activity activity)
    {
        TypedArray typedarray = activity.obtainStyledAttributes(THEME_ATTRS);
        activity = typedarray.getDrawable(0);
        typedarray.recycle();
        return activity;
    }

    public static Object setActionBarDescription(Object obj, Activity activity, int i)
    {
        Object obj1 = obj;
        if(obj == null)
            obj1 = new SetIndicatorInfo(activity);
        obj = (SetIndicatorInfo)obj1;
        if(((SetIndicatorInfo) (obj)).setHomeAsUpIndicator != null)
            try
            {
                activity = activity.getActionBar();
                ((SetIndicatorInfo) (obj)).setHomeActionContentDescription.invoke(activity, new Object[] {
                    Integer.valueOf(i)
                });
                if(android.os.Build.VERSION.SDK_INT <= 19)
                    activity.setSubtitle(activity.getSubtitle());
            }
            // Misplaced declaration of an exception variable
            catch(Object obj)
            {
                Log.w("ActionBarDrawerToggleHoneycomb", "Couldn't set content description via JB-MR2 API", ((Throwable) (obj)));
            }
        return obj1;
    }

    public static Object setActionBarUpIndicator(Object obj, Activity activity, Drawable drawable, int i)
    {
        Object obj1 = obj;
        if(obj == null)
            obj1 = new SetIndicatorInfo(activity);
        obj = (SetIndicatorInfo)obj1;
        if(((SetIndicatorInfo) (obj)).setHomeAsUpIndicator != null)
            try
            {
                activity = activity.getActionBar();
                ((SetIndicatorInfo) (obj)).setHomeAsUpIndicator.invoke(activity, new Object[] {
                    drawable
                });
                ((SetIndicatorInfo) (obj)).setHomeActionContentDescription.invoke(activity, new Object[] {
                    Integer.valueOf(i)
                });
            }
            // Misplaced declaration of an exception variable
            catch(Object obj)
            {
                Log.w("ActionBarDrawerToggleHoneycomb", "Couldn't set home-as-up indicator via JB-MR2 API", ((Throwable) (obj)));
            }
        else
        if(((SetIndicatorInfo) (obj)).upIndicatorView != null)
            ((SetIndicatorInfo) (obj)).upIndicatorView.setImageDrawable(drawable);
        else
            Log.w("ActionBarDrawerToggleHoneycomb", "Couldn't set home-as-up indicator");
        return obj1;
    }

    private static final String TAG = "ActionBarDrawerToggleHoneycomb";
    private static final int THEME_ATTRS[] = {
        0x101030b
    };

}
