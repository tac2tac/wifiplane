// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.media;

import android.os.Bundle;
import java.util.List;

public class MediaBrowserCompatUtils
{

    public MediaBrowserCompatUtils()
    {
    }

    public static List applyOptions(List list, Bundle bundle)
    {
        int i = bundle.getInt("android.media.browse.extra.PAGE", -1);
        int k = bundle.getInt("android.media.browse.extra.PAGE_SIZE", -1);
        if(i != -1 || k != -1)
        {
            int l = k * (i - 1);
            int i1 = l + k;
            if(i < 1 || k < 1 || l >= list.size())
            {
                list = null;
            } else
            {
                int j = i1;
                if(i1 > list.size())
                    j = list.size();
                list = list.subList(l, j);
            }
        }
        return list;
    }

    public static boolean areSameOptions(Bundle bundle, Bundle bundle1)
    {
        boolean flag = true;
        if(bundle != bundle1) goto _L2; else goto _L1
_L1:
        return flag;
_L2:
        if(bundle == null)
        {
            if(bundle1.getInt("android.media.browse.extra.PAGE", -1) != -1 || bundle1.getInt("android.media.browse.extra.PAGE_SIZE", -1) != -1)
                flag = false;
        } else
        if(bundle1 == null)
        {
            if(bundle.getInt("android.media.browse.extra.PAGE", -1) != -1 || bundle.getInt("android.media.browse.extra.PAGE_SIZE", -1) != -1)
                flag = false;
        } else
        if(bundle.getInt("android.media.browse.extra.PAGE", -1) != bundle1.getInt("android.media.browse.extra.PAGE", -1) || bundle.getInt("android.media.browse.extra.PAGE_SIZE", -1) != bundle1.getInt("android.media.browse.extra.PAGE_SIZE", -1))
            flag = false;
        if(true) goto _L1; else goto _L3
_L3:
    }

    public static boolean hasDuplicatedItems(Bundle bundle, Bundle bundle1)
    {
        boolean flag;
        int i;
        int k;
        int l;
        flag = true;
        int j;
        if(bundle == null)
            i = -1;
        else
            i = bundle.getInt("android.media.browse.extra.PAGE", -1);
        if(bundle1 == null)
            j = -1;
        else
            j = bundle1.getInt("android.media.browse.extra.PAGE", -1);
        if(bundle == null)
            k = -1;
        else
            k = bundle.getInt("android.media.browse.extra.PAGE_SIZE", -1);
        if(bundle1 == null)
            l = -1;
        else
            l = bundle1.getInt("android.media.browse.extra.PAGE_SIZE", -1);
        if(i == -1 || k == -1)
        {
            i = 0;
            k = 0x7fffffff;
        } else
        {
            i = k * (i - 1);
            k = (i + k) - 1;
        }
        if(j == -1 || l == -1)
        {
            j = 0;
            l = 0x7fffffff;
        } else
        {
            j = l * (j - 1);
            l = (j + l) - 1;
        }
        break MISSING_BLOCK_LABEL_65;
        if((i > j || j > k) && (i > l || l > k))
            flag = false;
        return flag;
    }
}
