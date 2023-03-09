// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.media;

import android.os.*;

class IMediaBrowserServiceAdapterApi21
{
    static abstract class Stub extends Binder
        implements IInterface
    {

        public abstract void addSubscription(String s, Object obj);

        public IBinder asBinder()
        {
            return this;
        }

        public abstract void connect(String s, Bundle bundle, Object obj);

        public abstract void disconnect(Object obj);

        public abstract void getMediaItem(String s, ResultReceiver resultreceiver);

        public boolean onTransact(int i, Parcel parcel, Parcel parcel1, int j)
            throws RemoteException
        {
            boolean flag = true;
            i;
            JVM INSTR lookupswitch 6: default 64
        //                       1: 87
        //                       2: 141
        //                       3: 161
        //                       4: 185
        //                       5: 209
        //                       1598968902: 78;
               goto _L1 _L2 _L3 _L4 _L5 _L6 _L7
_L1:
            flag = super.onTransact(i, parcel, parcel1, j);
_L9:
            return flag;
_L7:
            parcel1.writeString("android.service.media.IMediaBrowserService");
            continue; /* Loop/switch isn't completed */
_L2:
            parcel.enforceInterface("android.service.media.IMediaBrowserService");
            String s = parcel.readString();
            if(parcel.readInt() != 0)
                parcel1 = (Bundle)Bundle.CREATOR.createFromParcel(parcel);
            else
                parcel1 = null;
            connect(s, parcel1, IMediaBrowserServiceCallbacksAdapterApi21.Stub.asInterface(parcel.readStrongBinder()));
            continue; /* Loop/switch isn't completed */
_L3:
            parcel.enforceInterface("android.service.media.IMediaBrowserService");
            disconnect(IMediaBrowserServiceCallbacksAdapterApi21.Stub.asInterface(parcel.readStrongBinder()));
            continue; /* Loop/switch isn't completed */
_L4:
            parcel.enforceInterface("android.service.media.IMediaBrowserService");
            addSubscription(parcel.readString(), IMediaBrowserServiceCallbacksAdapterApi21.Stub.asInterface(parcel.readStrongBinder()));
            continue; /* Loop/switch isn't completed */
_L5:
            parcel.enforceInterface("android.service.media.IMediaBrowserService");
            removeSubscription(parcel.readString(), IMediaBrowserServiceCallbacksAdapterApi21.Stub.asInterface(parcel.readStrongBinder()));
            continue; /* Loop/switch isn't completed */
_L6:
            parcel.enforceInterface("android.service.media.IMediaBrowserService");
            parcel1 = parcel.readString();
            if(parcel.readInt() != 0)
                parcel = (ResultReceiver)ResultReceiver.CREATOR.createFromParcel(parcel);
            else
                parcel = null;
            getMediaItem(parcel1, parcel);
            if(true) goto _L9; else goto _L8
_L8:
        }

        public abstract void removeSubscription(String s, Object obj);

        private static final String DESCRIPTOR = "android.service.media.IMediaBrowserService";
        private static final int TRANSACTION_addSubscription = 3;
        private static final int TRANSACTION_connect = 1;
        private static final int TRANSACTION_disconnect = 2;
        private static final int TRANSACTION_getMediaItem = 5;
        private static final int TRANSACTION_removeSubscription = 4;

        public Stub()
        {
            attachInterface(this, "android.service.media.IMediaBrowserService");
        }
    }


    IMediaBrowserServiceAdapterApi21()
    {
    }
}
