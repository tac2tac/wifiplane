// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.view;

import android.view.View;
import android.view.ViewParent;

// Referenced classes of package android.support.v4.view:
//            ViewParentCompat, ViewCompat

public class NestedScrollingChildHelper
{

    public NestedScrollingChildHelper(View view)
    {
        mView = view;
    }

    public boolean dispatchNestedFling(float f, float f1, boolean flag)
    {
        if(isNestedScrollingEnabled() && mNestedScrollingParent != null)
            flag = ViewParentCompat.onNestedFling(mNestedScrollingParent, mView, f, f1, flag);
        else
            flag = false;
        return flag;
    }

    public boolean dispatchNestedPreFling(float f, float f1)
    {
        boolean flag;
        if(isNestedScrollingEnabled() && mNestedScrollingParent != null)
            flag = ViewParentCompat.onNestedPreFling(mNestedScrollingParent, mView, f, f1);
        else
            flag = false;
        return flag;
    }

    public boolean dispatchNestedPreScroll(int i, int j, int ai[], int ai1[])
    {
        boolean flag;
        boolean flag1;
        flag = false;
        flag1 = flag;
        if(!isNestedScrollingEnabled()) goto _L2; else goto _L1
_L1:
        flag1 = flag;
        if(mNestedScrollingParent == null) goto _L2; else goto _L3
_L3:
        if(i == 0 && j == 0) goto _L5; else goto _L4
_L4:
        int ai2[];
        int k = 0;
        int l = 0;
        if(ai1 != null)
        {
            mView.getLocationInWindow(ai1);
            k = ai1[0];
            l = ai1[1];
        }
        ai2 = ai;
        if(ai == null)
        {
            if(mTempNestedScrollConsumed == null)
                mTempNestedScrollConsumed = new int[2];
            ai2 = mTempNestedScrollConsumed;
        }
        ai2[0] = 0;
        ai2[1] = 0;
        ViewParentCompat.onNestedPreScroll(mNestedScrollingParent, mView, i, j, ai2);
        if(ai1 != null)
        {
            mView.getLocationInWindow(ai1);
            ai1[0] = ai1[0] - k;
            ai1[1] = ai1[1] - l;
        }
        if(ai2[0] != 0) goto _L7; else goto _L6
_L6:
        flag1 = flag;
        if(ai2[1] == 0) goto _L2; else goto _L7
_L7:
        flag1 = true;
_L2:
        return flag1;
_L5:
        flag1 = flag;
        if(ai1 != null)
        {
            ai1[0] = 0;
            ai1[1] = 0;
            flag1 = flag;
        }
        if(true) goto _L2; else goto _L8
_L8:
    }

    public boolean dispatchNestedScroll(int i, int j, int k, int l, int ai[])
    {
        if(!isNestedScrollingEnabled() || mNestedScrollingParent == null) goto _L2; else goto _L1
_L1:
        if(i == 0 && j == 0 && k == 0 && l == 0) goto _L4; else goto _L3
_L3:
        boolean flag;
        int i1 = 0;
        int j1 = 0;
        if(ai != null)
        {
            mView.getLocationInWindow(ai);
            i1 = ai[0];
            j1 = ai[1];
        }
        ViewParentCompat.onNestedScroll(mNestedScrollingParent, mView, i, j, k, l);
        if(ai != null)
        {
            mView.getLocationInWindow(ai);
            ai[0] = ai[0] - i1;
            ai[1] = ai[1] - j1;
        }
        flag = true;
_L6:
        return flag;
_L4:
        if(ai != null)
        {
            ai[0] = 0;
            ai[1] = 0;
        }
_L2:
        flag = false;
        if(true) goto _L6; else goto _L5
_L5:
    }

    public boolean hasNestedScrollingParent()
    {
        boolean flag;
        if(mNestedScrollingParent != null)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public boolean isNestedScrollingEnabled()
    {
        return mIsNestedScrollingEnabled;
    }

    public void onDetachedFromWindow()
    {
        ViewCompat.stopNestedScroll(mView);
    }

    public void onStopNestedScroll(View view)
    {
        ViewCompat.stopNestedScroll(mView);
    }

    public void setNestedScrollingEnabled(boolean flag)
    {
        if(mIsNestedScrollingEnabled)
            ViewCompat.stopNestedScroll(mView);
        mIsNestedScrollingEnabled = flag;
    }

    public boolean startNestedScroll(int i)
    {
        boolean flag = true;
        if(!hasNestedScrollingParent()) goto _L2; else goto _L1
_L1:
        return flag;
_L2:
        if(isNestedScrollingEnabled())
        {
            ViewParent viewparent = mView.getParent();
            View view = mView;
            for(; viewparent != null; viewparent = viewparent.getParent())
            {
                if(ViewParentCompat.onStartNestedScroll(viewparent, view, mView, i))
                {
                    mNestedScrollingParent = viewparent;
                    ViewParentCompat.onNestedScrollAccepted(viewparent, view, mView, i);
                    continue; /* Loop/switch isn't completed */
                }
                if(viewparent instanceof View)
                    view = (View)viewparent;
            }

        }
        flag = false;
        if(true) goto _L1; else goto _L3
_L3:
    }

    public void stopNestedScroll()
    {
        if(mNestedScrollingParent != null)
        {
            ViewParentCompat.onStopNestedScroll(mNestedScrollingParent, mView);
            mNestedScrollingParent = null;
        }
    }

    private boolean mIsNestedScrollingEnabled;
    private ViewParent mNestedScrollingParent;
    private int mTempNestedScrollConsumed[];
    private final View mView;
}
