// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.app;

import android.app.Activity;

class ActivityCompatApi23
{
    public static interface RequestPermissionsRequestCodeValidator
    {

        public abstract void validateRequestPermissionsRequestCode(int i);
    }


    ActivityCompatApi23()
    {
    }

    public static void requestPermissions(Activity activity, String as[], int i)
    {
        if(activity instanceof RequestPermissionsRequestCodeValidator)
            ((RequestPermissionsRequestCodeValidator)activity).validateRequestPermissionsRequestCode(i);
        activity.requestPermissions(as, i);
    }

    public static boolean shouldShowRequestPermissionRationale(Activity activity, String s)
    {
        return activity.shouldShowRequestPermissionRationale(s);
    }
}
