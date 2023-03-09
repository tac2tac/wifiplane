// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.media;

import android.os.*;
import android.service.media.MediaBrowserService;
import android.util.Log;
import java.lang.reflect.Field;

// Referenced classes of package android.support.v4.media:
//            MediaBrowserServiceCompatApi21

class MediaBrowserServiceCompatApi23 extends MediaBrowserServiceCompatApi21
{
    public static interface ItemCallback
    {

        public abstract void onItemLoaded(int i, Bundle bundle, Parcel parcel);
    }

    static class MediaBrowserServiceAdaptorApi23 extends MediaBrowserServiceCompatApi21.MediaBrowserServiceAdaptorApi21
    {

        public void onCreate(ServiceImplApi23 serviceimplapi23)
        {
            mBinder = new ServiceBinderProxyApi23(serviceimplapi23);
        }

        MediaBrowserServiceAdaptorApi23()
        {
        }
    }

    private static class MediaBrowserServiceAdaptorApi23.ServiceBinderProxyApi23 extends MediaBrowserServiceCompatApi21.MediaBrowserServiceAdaptorApi21.ServiceBinderProxyApi21
    {

        public void getMediaItem(String s, ResultReceiver resultreceiver)
        {
            final String KEY_MEDIA_ITEM = (String)android/service/media/MediaBrowserService.getDeclaredField("KEY_MEDIA_ITEM").get(null);
            mServiceImpl.getMediaItem(s, resultreceiver. new ItemCallback() {

                public void onItemLoaded(int i, Bundle bundle, Parcel parcel)
                {
                    if(parcel != null)
                    {
                        parcel.setDataPosition(0);
                        android.media.browse.MediaBrowser.MediaItem mediaitem = (android.media.browse.MediaBrowser.MediaItem)android.media.browse.MediaBrowser.MediaItem.CREATOR.createFromParcel(parcel);
                        bundle.putParcelable(KEY_MEDIA_ITEM, mediaitem);
                        parcel.recycle();
                    }
                    receiver.send(i, bundle);
                }

                final MediaBrowserServiceAdaptorApi23.ServiceBinderProxyApi23 this$0;
                final String val$KEY_MEDIA_ITEM;
                final ResultReceiver val$receiver;

            
            {
                this$0 = final_servicebinderproxyapi23;
                KEY_MEDIA_ITEM = s;
                receiver = ResultReceiver.this;
                super();
            }
            }
);
_L2:
            return;
            s;
_L3:
            Log.i("MediaBrowserServiceCompatApi21", "Failed to get KEY_MEDIA_ITEM via reflection", s);
            if(true) goto _L2; else goto _L1
_L1:
            s;
              goto _L3
        }

        ServiceImplApi23 mServiceImpl;

        MediaBrowserServiceAdaptorApi23.ServiceBinderProxyApi23(ServiceImplApi23 serviceimplapi23)
        {
            super(serviceimplapi23);
            mServiceImpl = serviceimplapi23;
        }
    }

    public static interface ServiceImplApi23
        extends MediaBrowserServiceCompatApi21.ServiceImplApi21
    {

        public abstract void getMediaItem(String s, ItemCallback itemcallback);
    }


    MediaBrowserServiceCompatApi23()
    {
    }

    public static Object createService()
    {
        return new MediaBrowserServiceAdaptorApi23();
    }

    public static void onCreate(Object obj, ServiceImplApi23 serviceimplapi23)
    {
        ((MediaBrowserServiceAdaptorApi23)obj).onCreate(serviceimplapi23);
    }

    private static final String TAG = "MediaBrowserServiceCompatApi21";
}
