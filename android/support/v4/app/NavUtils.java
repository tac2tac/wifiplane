// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.app;

import android.app.Activity;
import android.content.*;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.util.Log;

// Referenced classes of package android.support.v4.app:
//            NavUtilsJB

public final class NavUtils
{
    static interface NavUtilsImpl
    {

        public abstract Intent getParentActivityIntent(Activity activity);

        public abstract String getParentActivityName(Context context, ActivityInfo activityinfo);

        public abstract void navigateUpTo(Activity activity, Intent intent);

        public abstract boolean shouldUpRecreateTask(Activity activity, Intent intent);
    }

    static class NavUtilsImplBase
        implements NavUtilsImpl
    {

        public Intent getParentActivityIntent(Activity activity)
        {
            Object obj;
            String s;
            obj = null;
            s = NavUtils.getParentActivityName(activity);
            if(s != null) goto _L2; else goto _L1
_L1:
            activity = obj;
_L4:
            return activity;
_L2:
            ComponentName componentname = new ComponentName(activity, s);
            if(NavUtils.getParentActivityName(activity, componentname) == null)
            {
                activity = IntentCompat.makeMainActivity(componentname);
                continue; /* Loop/switch isn't completed */
            }
            try
            {
                activity = JVM INSTR new #38  <Class Intent>;
                activity.Intent();
                activity = activity.setComponent(componentname);
            }
            // Misplaced declaration of an exception variable
            catch(Activity activity)
            {
                Log.e("NavUtils", (new StringBuilder()).append("getParentActivityIntent: bad parentActivityName '").append(s).append("' in manifest").toString());
                activity = obj;
            }
            if(true) goto _L4; else goto _L3
_L3:
        }

        public String getParentActivityName(Context context, ActivityInfo activityinfo)
        {
            if(activityinfo.metaData != null) goto _L2; else goto _L1
_L1:
            activityinfo = null;
_L4:
            return activityinfo;
_L2:
            String s = activityinfo.metaData.getString("android.support.PARENT_ACTIVITY");
            if(s == null)
            {
                activityinfo = null;
            } else
            {
                activityinfo = s;
                if(s.charAt(0) == '.')
                    activityinfo = (new StringBuilder()).append(context.getPackageName()).append(s).toString();
            }
            if(true) goto _L4; else goto _L3
_L3:
        }

        public void navigateUpTo(Activity activity, Intent intent)
        {
            intent.addFlags(0x4000000);
            activity.startActivity(intent);
            activity.finish();
        }

        public boolean shouldUpRecreateTask(Activity activity, Intent intent)
        {
            activity = activity.getIntent().getAction();
            boolean flag;
            if(activity != null && !activity.equals("android.intent.action.MAIN"))
                flag = true;
            else
                flag = false;
            return flag;
        }

        NavUtilsImplBase()
        {
        }
    }

    static class NavUtilsImplJB extends NavUtilsImplBase
    {

        public Intent getParentActivityIntent(Activity activity)
        {
            Intent intent = NavUtilsJB.getParentActivityIntent(activity);
            Intent intent1 = intent;
            if(intent == null)
                intent1 = superGetParentActivityIntent(activity);
            return intent1;
        }

        public String getParentActivityName(Context context, ActivityInfo activityinfo)
        {
            String s = NavUtilsJB.getParentActivityName(activityinfo);
            String s1 = s;
            if(s == null)
                s1 = super.getParentActivityName(context, activityinfo);
            return s1;
        }

        public void navigateUpTo(Activity activity, Intent intent)
        {
            NavUtilsJB.navigateUpTo(activity, intent);
        }

        public boolean shouldUpRecreateTask(Activity activity, Intent intent)
        {
            return NavUtilsJB.shouldUpRecreateTask(activity, intent);
        }

        Intent superGetParentActivityIntent(Activity activity)
        {
            return super.getParentActivityIntent(activity);
        }

        NavUtilsImplJB()
        {
        }
    }


    private NavUtils()
    {
    }

    public static Intent getParentActivityIntent(Activity activity)
    {
        return IMPL.getParentActivityIntent(activity);
    }

    public static Intent getParentActivityIntent(Context context, ComponentName componentname)
        throws android.content.pm.PackageManager.NameNotFoundException
    {
        String s = getParentActivityName(context, componentname);
        if(s == null)
        {
            context = null;
        } else
        {
            componentname = new ComponentName(componentname.getPackageName(), s);
            if(getParentActivityName(context, componentname) == null)
                context = IntentCompat.makeMainActivity(componentname);
            else
                context = (new Intent()).setComponent(componentname);
        }
        return context;
    }

    public static Intent getParentActivityIntent(Context context, Class class1)
        throws android.content.pm.PackageManager.NameNotFoundException
    {
        class1 = getParentActivityName(context, new ComponentName(context, class1));
        if(class1 == null)
        {
            context = null;
        } else
        {
            class1 = new ComponentName(context, class1);
            if(getParentActivityName(context, class1) == null)
                context = IntentCompat.makeMainActivity(class1);
            else
                context = (new Intent()).setComponent(class1);
        }
        return context;
    }

    public static String getParentActivityName(Activity activity)
    {
        try
        {
            activity = getParentActivityName(((Context) (activity)), activity.getComponentName());
        }
        // Misplaced declaration of an exception variable
        catch(Activity activity)
        {
            throw new IllegalArgumentException(activity);
        }
        return activity;
    }

    public static String getParentActivityName(Context context, ComponentName componentname)
        throws android.content.pm.PackageManager.NameNotFoundException
    {
        componentname = context.getPackageManager().getActivityInfo(componentname, 128);
        return IMPL.getParentActivityName(context, componentname);
    }

    public static void navigateUpFromSameTask(Activity activity)
    {
        Intent intent = getParentActivityIntent(activity);
        if(intent == null)
        {
            throw new IllegalArgumentException((new StringBuilder()).append("Activity ").append(activity.getClass().getSimpleName()).append(" does not have a parent activity name specified.").append(" (Did you forget to add the android.support.PARENT_ACTIVITY <meta-data> ").append(" element in your manifest?)").toString());
        } else
        {
            navigateUpTo(activity, intent);
            return;
        }
    }

    public static void navigateUpTo(Activity activity, Intent intent)
    {
        IMPL.navigateUpTo(activity, intent);
    }

    public static boolean shouldUpRecreateTask(Activity activity, Intent intent)
    {
        return IMPL.shouldUpRecreateTask(activity, intent);
    }

    private static final NavUtilsImpl IMPL;
    public static final String PARENT_ACTIVITY = "android.support.PARENT_ACTIVITY";
    private static final String TAG = "NavUtils";

    static 
    {
        if(android.os.Build.VERSION.SDK_INT >= 16)
            IMPL = new NavUtilsImplJB();
        else
            IMPL = new NavUtilsImplBase();
    }
}
