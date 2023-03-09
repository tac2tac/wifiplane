// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.net;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

// Referenced classes of package android.support.v4.net:
//            ConnectivityManagerCompatGingerbread, ConnectivityManagerCompatHoneycombMR2, ConnectivityManagerCompatJellyBean

public final class ConnectivityManagerCompat
{
    static class BaseConnectivityManagerCompatImpl
        implements ConnectivityManagerCompatImpl
    {

        public boolean isActiveNetworkMetered(ConnectivityManager connectivitymanager)
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
                break;
            }
            if(true) goto _L4; else goto _L3
_L3:
        }

        BaseConnectivityManagerCompatImpl()
        {
        }
    }

    static interface ConnectivityManagerCompatImpl
    {

        public abstract boolean isActiveNetworkMetered(ConnectivityManager connectivitymanager);
    }

    static class GingerbreadConnectivityManagerCompatImpl
        implements ConnectivityManagerCompatImpl
    {

        public boolean isActiveNetworkMetered(ConnectivityManager connectivitymanager)
        {
            return ConnectivityManagerCompatGingerbread.isActiveNetworkMetered(connectivitymanager);
        }

        GingerbreadConnectivityManagerCompatImpl()
        {
        }
    }

    static class HoneycombMR2ConnectivityManagerCompatImpl
        implements ConnectivityManagerCompatImpl
    {

        public boolean isActiveNetworkMetered(ConnectivityManager connectivitymanager)
        {
            return ConnectivityManagerCompatHoneycombMR2.isActiveNetworkMetered(connectivitymanager);
        }

        HoneycombMR2ConnectivityManagerCompatImpl()
        {
        }
    }

    static class JellyBeanConnectivityManagerCompatImpl
        implements ConnectivityManagerCompatImpl
    {

        public boolean isActiveNetworkMetered(ConnectivityManager connectivitymanager)
        {
            return ConnectivityManagerCompatJellyBean.isActiveNetworkMetered(connectivitymanager);
        }

        JellyBeanConnectivityManagerCompatImpl()
        {
        }
    }


    private ConnectivityManagerCompat()
    {
    }

    public static NetworkInfo getNetworkInfoFromBroadcast(ConnectivityManager connectivitymanager, Intent intent)
    {
        intent = (NetworkInfo)intent.getParcelableExtra("networkInfo");
        if(intent != null)
            connectivitymanager = connectivitymanager.getNetworkInfo(intent.getType());
        else
            connectivitymanager = null;
        return connectivitymanager;
    }

    public static boolean isActiveNetworkMetered(ConnectivityManager connectivitymanager)
    {
        return IMPL.isActiveNetworkMetered(connectivitymanager);
    }

    private static final ConnectivityManagerCompatImpl IMPL;

    static 
    {
        if(android.os.Build.VERSION.SDK_INT >= 16)
            IMPL = new JellyBeanConnectivityManagerCompatImpl();
        else
        if(android.os.Build.VERSION.SDK_INT >= 13)
            IMPL = new HoneycombMR2ConnectivityManagerCompatImpl();
        else
        if(android.os.Build.VERSION.SDK_INT >= 8)
            IMPL = new GingerbreadConnectivityManagerCompatImpl();
        else
            IMPL = new BaseConnectivityManagerCompatImpl();
    }
}
