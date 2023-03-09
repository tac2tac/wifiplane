// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.widget;

import android.view.View;
import android.widget.ListView;

class ListViewCompatDonut
{

    ListViewCompatDonut()
    {
    }

    static void scrollListBy(ListView listview, int i)
    {
        int j = listview.getFirstVisiblePosition();
        if(j != -1) goto _L2; else goto _L1
_L1:
        return;
_L2:
        View view = listview.getChildAt(0);
        if(view != null)
            listview.setSelectionFromTop(j, view.getTop() - i);
        if(true) goto _L1; else goto _L3
_L3:
    }
}
