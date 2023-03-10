// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.view.View;

// Referenced classes of package android.support.v4.app:
//            ActivityOptionsCompatJB, ActivityOptionsCompat21

public class ActivityOptionsCompat
{
    private static class ActivityOptionsImpl21 extends ActivityOptionsCompat
    {

        public Bundle toBundle()
        {
            return mImpl.toBundle();
        }

        public void update(ActivityOptionsCompat activityoptionscompat)
        {
            if(activityoptionscompat instanceof ActivityOptionsImpl21)
            {
                activityoptionscompat = (ActivityOptionsImpl21)activityoptionscompat;
                mImpl.update(((ActivityOptionsImpl21) (activityoptionscompat)).mImpl);
            }
        }

        private final ActivityOptionsCompat21 mImpl;

        ActivityOptionsImpl21(ActivityOptionsCompat21 activityoptionscompat21)
        {
            mImpl = activityoptionscompat21;
        }
    }

    private static class ActivityOptionsImplJB extends ActivityOptionsCompat
    {

        public Bundle toBundle()
        {
            return mImpl.toBundle();
        }

        public void update(ActivityOptionsCompat activityoptionscompat)
        {
            if(activityoptionscompat instanceof ActivityOptionsImplJB)
            {
                activityoptionscompat = (ActivityOptionsImplJB)activityoptionscompat;
                mImpl.update(((ActivityOptionsImplJB) (activityoptionscompat)).mImpl);
            }
        }

        private final ActivityOptionsCompatJB mImpl;

        ActivityOptionsImplJB(ActivityOptionsCompatJB activityoptionscompatjb)
        {
            mImpl = activityoptionscompatjb;
        }
    }


    protected ActivityOptionsCompat()
    {
    }

    public static ActivityOptionsCompat makeCustomAnimation(Context context, int i, int j)
    {
        if(android.os.Build.VERSION.SDK_INT >= 16)
            context = new ActivityOptionsImplJB(ActivityOptionsCompatJB.makeCustomAnimation(context, i, j));
        else
            context = new ActivityOptionsCompat();
        return context;
    }

    public static ActivityOptionsCompat makeScaleUpAnimation(View view, int i, int j, int k, int l)
    {
        if(android.os.Build.VERSION.SDK_INT >= 16)
            view = new ActivityOptionsImplJB(ActivityOptionsCompatJB.makeScaleUpAnimation(view, i, j, k, l));
        else
            view = new ActivityOptionsCompat();
        return view;
    }

    public static ActivityOptionsCompat makeSceneTransitionAnimation(Activity activity, View view, String s)
    {
        if(android.os.Build.VERSION.SDK_INT >= 21)
            activity = new ActivityOptionsImpl21(ActivityOptionsCompat21.makeSceneTransitionAnimation(activity, view, s));
        else
            activity = new ActivityOptionsCompat();
        return activity;
    }

    public static transient ActivityOptionsCompat makeSceneTransitionAnimation(Activity activity, Pair apair[])
    {
        if(android.os.Build.VERSION.SDK_INT >= 21)
        {
            View aview[] = null;
            String as[] = null;
            if(apair != null)
            {
                View aview1[] = new View[apair.length];
                String as1[] = new String[apair.length];
                int i = 0;
                do
                {
                    as = as1;
                    aview = aview1;
                    if(i >= apair.length)
                        break;
                    aview1[i] = (View)apair[i].first;
                    as1[i] = (String)apair[i].second;
                    i++;
                } while(true);
            }
            activity = new ActivityOptionsImpl21(ActivityOptionsCompat21.makeSceneTransitionAnimation(activity, aview, as));
        } else
        {
            activity = new ActivityOptionsCompat();
        }
        return activity;
    }

    public static ActivityOptionsCompat makeThumbnailScaleUpAnimation(View view, Bitmap bitmap, int i, int j)
    {
        if(android.os.Build.VERSION.SDK_INT >= 16)
            view = new ActivityOptionsImplJB(ActivityOptionsCompatJB.makeThumbnailScaleUpAnimation(view, bitmap, i, j));
        else
            view = new ActivityOptionsCompat();
        return view;
    }

    public Bundle toBundle()
    {
        return null;
    }

    public void update(ActivityOptionsCompat activityoptionscompat)
    {
    }
}
