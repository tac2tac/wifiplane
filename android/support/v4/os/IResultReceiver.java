// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.os;

import android.os.*;

public interface IResultReceiver
    extends IInterface
{
    public static abstract class Stub extends Binder
        implements IResultReceiver
    {

        public static IResultReceiver asInterface(IBinder ibinder)
        {
            if(ibinder == null)
            {
                ibinder = null;
            } else
            {
                IInterface iinterface = ibinder.queryLocalInterface("android.support.v4.os.IResultReceiver");
                if(iinterface != null && (iinterface instanceof IResultReceiver))
                    ibinder = (IResultReceiver)iinterface;
                else
                    ibinder = new Proxy(ibinder);
            }
            return ibinder;
        }

        public IBinder asBinder()
        {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel1, int j)
            throws RemoteException
        {
            boolean flag = true;
            i;
            JVM INSTR lookupswitch 2: default 32
        //                       1: 55
        //                       1598968902: 46;
               goto _L1 _L2 _L3
_L1:
            flag = super.onTransact(i, parcel, parcel1, j);
_L5:
            return flag;
_L3:
            parcel1.writeString("android.support.v4.os.IResultReceiver");
            continue; /* Loop/switch isn't completed */
_L2:
            parcel.enforceInterface("android.support.v4.os.IResultReceiver");
            i = parcel.readInt();
            if(parcel.readInt() != 0)
                parcel = (Bundle)Bundle.CREATOR.createFromParcel(parcel);
            else
                parcel = null;
            send(i, parcel);
            if(true) goto _L5; else goto _L4
_L4:
        }

        private static final String DESCRIPTOR = "android.support.v4.os.IResultReceiver";
        static final int TRANSACTION_send = 1;

        public Stub()
        {
            attachInterface(this, "android.support.v4.os.IResultReceiver");
        }
    }

    private static class Stub.Proxy
        implements IResultReceiver
    {

        public IBinder asBinder()
        {
            return mRemote;
        }

        public String getInterfaceDescriptor()
        {
            return "android.support.v4.os.IResultReceiver";
        }

        public void send(int i, Bundle bundle)
            throws RemoteException
        {
            Parcel parcel = Parcel.obtain();
            parcel.writeInterfaceToken("android.support.v4.os.IResultReceiver");
            parcel.writeInt(i);
            if(bundle == null)
                break MISSING_BLOCK_LABEL_49;
            parcel.writeInt(1);
            bundle.writeToParcel(parcel, 0);
_L1:
            mRemote.transact(1, parcel, null, 1);
            parcel.recycle();
            return;
            parcel.writeInt(0);
              goto _L1
            bundle;
            parcel.recycle();
            throw bundle;
        }

        private IBinder mRemote;

        Stub.Proxy(IBinder ibinder)
        {
            mRemote = ibinder;
        }
    }


    public abstract void send(int i, Bundle bundle)
        throws RemoteException;
}
