// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.widget;

import android.view.View;
import android.widget.ListView;

// Referenced classes of package android.support.v4.widget:
//            AutoScrollHelper, ListViewCompat

public class ListViewAutoScrollHelper extends AutoScrollHelper
{

    public ListViewAutoScrollHelper(ListView listview)
    {
        super(listview);
        mTarget = listview;
    }

    public boolean canTargetScrollHorizontally(int i)
    {
        return false;
    }

    public boolean canTargetScrollVertically(int i)
    {
        boolean flag;
        ListView listview;
        int j;
        flag = false;
        listview = mTarget;
        j = listview.getCount();
        if(j != 0) goto _L2; else goto _L1
_L1:
        boolean flag1 = flag;
_L8:
        return flag1;
_L2:
        int k;
        int l;
        k = listview.getChildCount();
        l = listview.getFirstVisiblePosition();
        if(i <= 0) goto _L4; else goto _L3
_L3:
        if(l + k >= j)
        {
            flag1 = flag;
            if(listview.getChildAt(k - 1).getBottom() <= listview.getHeight())
                continue; /* Loop/switch isn't completed */
        }
_L6:
        flag1 = true;
        continue; /* Loop/switch isn't completed */
_L4:
        flag1 = flag;
        if(i >= 0)
            continue; /* Loop/switch isn't completed */
        if(l > 0 || listview.getChildAt(0).getTop() < 0) goto _L6; else goto _L5
_L5:
        flag1 = flag;
        if(true) goto _L8; else goto _L7
_L7:
    }

    public void scrollTargetBy(int i, int j)
    {
        ListViewCompat.scrollListBy(mTarget, j);
    }

    private final ListView mTarget;
}
