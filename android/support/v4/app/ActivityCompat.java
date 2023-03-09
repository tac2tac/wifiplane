// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.*;
import android.support.v4.content.ContextCompat;
import android.view.View;
import java.util.List;
import java.util.Map;

// Referenced classes of package android.support.v4.app:
//            ActivityCompatJB, ActivityCompat21, ActivityCompatHoneycomb, ActivityCompatApi23, 
//            ActivityCompat22, SharedElementCallback

public class ActivityCompat extends ContextCompat
{
    public static interface OnRequestPermissionsResultCallback
    {

        public abstract void onRequestPermissionsResult(int i, String as[], int ai[]);
    }

    private static class SharedElementCallback21Impl extends ActivityCompat21.SharedElementCallback21
    {

        public Parcelable onCaptureSharedElementSnapshot(View view, Matrix matrix, RectF rectf)
        {
            return mCallback.onCaptureSharedElementSnapshot(view, matrix, rectf);
        }

        public View onCreateSnapshotView(Context context, Parcelable parcelable)
        {
            return mCallback.onCreateSnapshotView(context, parcelable);
        }

        public void onMapSharedElements(List list, Map map)
        {
            mCallback.onMapSharedElements(list, map);
        }

        public void onRejectSharedElements(List list)
        {
            mCallback.onRejectSharedElements(list);
        }

        public void onSharedElementEnd(List list, List list1, List list2)
        {
            mCallback.onSharedElementEnd(list, list1, list2);
        }

        public void onSharedElementStart(List list, List list1, List list2)
        {
            mCallback.onSharedElementStart(list, list1, list2);
        }

        private SharedElementCallback mCallback;

        public SharedElementCallback21Impl(SharedElementCallback sharedelementcallback)
        {
            mCallback = sharedelementcallback;
        }
    }


    public ActivityCompat()
    {
    }

    private static ActivityCompat21.SharedElementCallback21 createCallback(SharedElementCallback sharedelementcallback)
    {
        SharedElementCallback21Impl sharedelementcallback21impl = null;
        if(sharedelementcallback != null)
            sharedelementcallback21impl = new SharedElementCallback21Impl(sharedelementcallback);
        return sharedelementcallback21impl;
    }

    public static void finishAffinity(Activity activity)
    {
        if(android.os.Build.VERSION.SDK_INT >= 16)
            ActivityCompatJB.finishAffinity(activity);
        else
            activity.finish();
    }

    public static void finishAfterTransition(Activity activity)
    {
        if(android.os.Build.VERSION.SDK_INT >= 21)
            ActivityCompat21.finishAfterTransition(activity);
        else
            activity.finish();
    }

    public static boolean invalidateOptionsMenu(Activity activity)
    {
        boolean flag;
        if(android.os.Build.VERSION.SDK_INT >= 11)
        {
            ActivityCompatHoneycomb.invalidateOptionsMenu(activity);
            flag = true;
        } else
        {
            flag = false;
        }
        return flag;
    }

    public static void postponeEnterTransition(Activity activity)
    {
        if(android.os.Build.VERSION.SDK_INT >= 21)
            ActivityCompat21.postponeEnterTransition(activity);
    }

    public static void requestPermissions(Activity activity, String as[], int i)
    {
        if(android.os.Build.VERSION.SDK_INT < 23) goto _L2; else goto _L1
_L1:
        ActivityCompatApi23.requestPermissions(activity, as, i);
_L4:
        return;
_L2:
        if(activity instanceof OnRequestPermissionsResultCallback)
            (new Handler(Looper.getMainLooper())).post(new Runnable(as, activity, i) {

                public void run()
                {
                    int ai[] = new int[permissions.length];
                    PackageManager packagemanager = activity.getPackageManager();
                    String s = activity.getPackageName();
                    int j = permissions.length;
                    for(int k = 0; k < j; k++)
                        ai[k] = packagemanager.checkPermission(permissions[k], s);

                    ((OnRequestPermissionsResultCallback)activity).onRequestPermissionsResult(requestCode, permissions, ai);
                }

                final Activity val$activity;
                final String val$permissions[];
                final int val$requestCode;

            
            {
                permissions = as;
                activity = activity1;
                requestCode = i;
                super();
            }
            }
);
        if(true) goto _L4; else goto _L3
_L3:
    }

    public static void setEnterSharedElementCallback(Activity activity, SharedElementCallback sharedelementcallback)
    {
        if(android.os.Build.VERSION.SDK_INT >= 21)
            ActivityCompat21.setEnterSharedElementCallback(activity, createCallback(sharedelementcallback));
    }

    public static void setExitSharedElementCallback(Activity activity, SharedElementCallback sharedelementcallback)
    {
        if(android.os.Build.VERSION.SDK_INT >= 21)
            ActivityCompat21.setExitSharedElementCallback(activity, createCallback(sharedelementcallback));
    }

    public static boolean shouldShowRequestPermissionRationale(Activity activity, String s)
    {
        boolean flag;
        if(android.os.Build.VERSION.SDK_INT >= 23)
            flag = ActivityCompatApi23.shouldShowRequestPermissionRationale(activity, s);
        else
            flag = false;
        return flag;
    }

    public static void startActivity(Activity activity, Intent intent, Bundle bundle)
    {
        if(android.os.Build.VERSION.SDK_INT >= 16)
            ActivityCompatJB.startActivity(activity, intent, bundle);
        else
            activity.startActivity(intent);
    }

    public static void startActivityForResult(Activity activity, Intent intent, int i, Bundle bundle)
    {
        if(android.os.Build.VERSION.SDK_INT >= 16)
            ActivityCompatJB.startActivityForResult(activity, intent, i, bundle);
        else
            activity.startActivityForResult(intent, i);
    }

    public static void startPostponedEnterTransition(Activity activity)
    {
        if(android.os.Build.VERSION.SDK_INT >= 21)
            ActivityCompat21.startPostponedEnterTransition(activity);
    }

    public Uri getReferrer(Activity activity)
    {
        if(android.os.Build.VERSION.SDK_INT < 22) goto _L2; else goto _L1
_L1:
        activity = ActivityCompat22.getReferrer(activity);
_L4:
        return activity;
_L2:
        Intent intent = activity.getIntent();
        Uri uri = (Uri)intent.getParcelableExtra("android.intent.extra.REFERRER");
        activity = uri;
        if(uri == null)
        {
            activity = intent.getStringExtra("android.intent.extra.REFERRER_NAME");
            if(activity != null)
                activity = Uri.parse(activity);
            else
                activity = null;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }
}
