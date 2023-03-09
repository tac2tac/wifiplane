// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.media;

import android.content.Intent;
import android.os.*;
import java.util.*;

// Referenced classes of package android.support.v4.media:
//            ParceledListSliceAdapterApi21, IMediaBrowserServiceCallbacksAdapterApi21

class MediaBrowserServiceCompatApi21
{
    static class MediaBrowserServiceAdaptorApi21
    {

        public IBinder onBind(Intent intent)
        {
            if("android.media.browse.MediaBrowserService".equals(intent.getAction()))
                intent = mBinder;
            else
                intent = null;
            return intent;
        }

        public void onCreate(ServiceImplApi21 serviceimplapi21)
        {
            mBinder = new ServiceBinderProxyApi21(serviceimplapi21);
        }

        ServiceBinderProxyApi21 mBinder;

        MediaBrowserServiceAdaptorApi21()
        {
        }
    }

    static class MediaBrowserServiceAdaptorApi21.ServiceBinderProxyApi21 extends IMediaBrowserServiceAdapterApi21.Stub
    {

        public void addSubscription(String s, Object obj)
        {
            mServiceImpl.addSubscription(s, new ServiceCallbacksApi21(obj));
        }

        public void connect(String s, Bundle bundle, Object obj)
        {
            mServiceImpl.connect(s, bundle, new ServiceCallbacksApi21(obj));
        }

        public void disconnect(Object obj)
        {
            mServiceImpl.disconnect(new ServiceCallbacksApi21(obj));
        }

        public void getMediaItem(String s, ResultReceiver resultreceiver)
        {
        }

        public void removeSubscription(String s, Object obj)
        {
            mServiceImpl.removeSubscription(s, new ServiceCallbacksApi21(obj));
        }

        final ServiceImplApi21 mServiceImpl;

        MediaBrowserServiceAdaptorApi21.ServiceBinderProxyApi21(ServiceImplApi21 serviceimplapi21)
        {
            mServiceImpl = serviceimplapi21;
        }
    }

    public static interface ServiceCallbacks
    {

        public abstract IBinder asBinder();

        public abstract void onConnect(String s, Object obj, Bundle bundle)
            throws RemoteException;

        public abstract void onConnectFailed()
            throws RemoteException;

        public abstract void onLoadChildren(String s, List list)
            throws RemoteException;
    }

    public static class ServiceCallbacksApi21
        implements ServiceCallbacks
    {

        public IBinder asBinder()
        {
            return mCallbacks.asBinder();
        }

        public void onConnect(String s, Object obj, Bundle bundle)
            throws RemoteException
        {
            mCallbacks.onConnect(s, obj, bundle);
        }

        public void onConnectFailed()
            throws RemoteException
        {
            mCallbacks.onConnectFailed();
        }

        public void onLoadChildren(String s, List list)
            throws RemoteException
        {
            Object obj = null;
            if(list != null)
            {
                ArrayList arraylist = new ArrayList();
                list = list.iterator();
                do
                {
                    obj = arraylist;
                    if(!list.hasNext())
                        break;
                    obj = (Parcel)list.next();
                    ((Parcel) (obj)).setDataPosition(0);
                    arraylist.add(android.media.browse.MediaBrowser.MediaItem.CREATOR.createFromParcel(((Parcel) (obj))));
                    ((Parcel) (obj)).recycle();
                } while(true);
            }
            if(android.os.Build.VERSION.SDK_INT > 23)
            {
                if(obj == null)
                    list = null;
                else
                    list = ((List) (ParceledListSliceAdapterApi21.newInstance(((List) (obj)))));
            } else
            if(obj == null)
                list = ((List) (sNullParceledListSliceObj));
            else
                list = ((List) (ParceledListSliceAdapterApi21.newInstance(((List) (obj)))));
            mCallbacks.onLoadChildren(s, list);
        }

        private static Object sNullParceledListSliceObj;
        private final IMediaBrowserServiceCallbacksAdapterApi21 mCallbacks;

        static 
        {
            android.media.browse.MediaBrowser.MediaItem mediaitem = new android.media.browse.MediaBrowser.MediaItem((new android.media.MediaDescription.Builder()).setMediaId("android.support.v4.media.MediaBrowserCompat.NULL_MEDIA_ITEM").build(), 0);
            ArrayList arraylist = new ArrayList();
            arraylist.add(mediaitem);
            sNullParceledListSliceObj = ParceledListSliceAdapterApi21.newInstance(arraylist);
        }

        ServiceCallbacksApi21(Object obj)
        {
            mCallbacks = new IMediaBrowserServiceCallbacksAdapterApi21(obj);
        }
    }

    public static interface ServiceImplApi21
    {

        public abstract void addSubscription(String s, ServiceCallbacks servicecallbacks);

        public abstract void connect(String s, Bundle bundle, ServiceCallbacks servicecallbacks);

        public abstract void disconnect(ServiceCallbacks servicecallbacks);

        public abstract void removeSubscription(String s, ServiceCallbacks servicecallbacks);
    }


    MediaBrowserServiceCompatApi21()
    {
    }

    public static Object createService()
    {
        return new MediaBrowserServiceAdaptorApi21();
    }

    public static IBinder onBind(Object obj, Intent intent)
    {
        return ((MediaBrowserServiceAdaptorApi21)obj).onBind(intent);
    }

    public static void onCreate(Object obj, ServiceImplApi21 serviceimplapi21)
    {
        ((MediaBrowserServiceAdaptorApi21)obj).onCreate(serviceimplapi21);
    }
}
