// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.net;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

class ConnectivityManagerCompatGingerbread
{

    ConnectivityManagerCompatGingerbread()
    {
    }

    public static boolean isActiveNetworkMetered(ConnectivityManager connectivitymanager)
    {
        boolean flag;
        flag = true;
        connectivitymanager = connectivitymanager.getActiveNetworkInfo();
        if(connectivitymanager != null) goto _L2; else goto _L1
_L1:
        boolean flag1 = flag;
_L4:
        return flag1;
_L2:
        flag1 = flag;
        switch(connectivitymanager.getType())
        {
        default:
            flag1 = flag;
            break;

        case 1: // '\001'
            flag1 = false;
            break;

        case 0: // '\0'
        case 2: // '\002'
        case 3: // '\003'
        case 4: // '\004'
        case 5: // '\005'
        case 6: // '\006'
            break;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }
}
