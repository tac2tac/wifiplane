// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.media.session;

import android.app.PendingIntent;
import android.net.Uri;
import android.os.*;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import java.util.List;

// Referenced classes of package android.support.v4.media.session:
//            PlaybackStateCompat, ParcelableVolumeInfo, IMediaControllerCallback

public interface IMediaSession
    extends IInterface
{
    public static abstract class Stub extends Binder
        implements IMediaSession
    {

        public static IMediaSession asInterface(IBinder ibinder)
        {
            if(ibinder == null)
            {
                ibinder = null;
            } else
            {
                IInterface iinterface = ibinder.queryLocalInterface("android.support.v4.media.session.IMediaSession");
                if(iinterface != null && (iinterface instanceof IMediaSession))
                    ibinder = (IMediaSession)iinterface;
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
            boolean flag;
            boolean flag1;
            boolean flag2;
            flag = false;
            flag1 = false;
            flag2 = true;
            i;
            JVM INSTR lookupswitch 33: default 284
        //                       1: 307
        //                       2: 387
        //                       3: 447
        //                       4: 471
        //                       5: 495
        //                       6: 529
        //                       7: 552
        //                       8: 575
        //                       9: 616
        //                       10: 641
        //                       11: 682
        //                       12: 711
        //                       13: 740
        //                       14: 757
        //                       15: 808
        //                       16: 859
        //                       17: 931
        //                       18: 952
        //                       19: 969
        //                       20: 986
        //                       21: 1003
        //                       22: 1020
        //                       23: 1037
        //                       24: 1054
        //                       25: 1075
        //                       26: 1118
        //                       27: 1169
        //                       28: 1210
        //                       29: 1251
        //                       30: 1274
        //                       31: 1315
        //                       32: 1356
        //                       1598968902: 298;
               goto _L1 _L2 _L3 _L4 _L5 _L6 _L7 _L8 _L9 _L10 _L11 _L12 _L13 _L14 _L15 _L16 _L17 _L18 _L19 _L20 _L21 _L22 _L23 _L24 _L25 _L26 _L27 _L28 _L29 _L30 _L31 _L32 _L33 _L34
_L1:
            flag2 = super.onTransact(i, parcel, parcel1, j);
_L36:
            return flag2;
_L34:
            parcel1.writeString("android.support.v4.media.session.IMediaSession");
            continue; /* Loop/switch isn't completed */
_L2:
            parcel.enforceInterface("android.support.v4.media.session.IMediaSession");
            String s = parcel.readString();
            Bundle bundle;
            if(parcel.readInt() != 0)
                bundle = (Bundle)Bundle.CREATOR.createFromParcel(parcel);
            else
                bundle = null;
            if(parcel.readInt() != 0)
                parcel = (MediaSessionCompat.ResultReceiverWrapper)MediaSessionCompat.ResultReceiverWrapper.CREATOR.createFromParcel(parcel);
            else
                parcel = null;
            sendCommand(s, bundle, parcel);
            parcel1.writeNoException();
            continue; /* Loop/switch isn't completed */
_L3:
            parcel.enforceInterface("android.support.v4.media.session.IMediaSession");
            boolean flag3;
            if(parcel.readInt() != 0)
                parcel = (KeyEvent)KeyEvent.CREATOR.createFromParcel(parcel);
            else
                parcel = null;
            flag3 = sendMediaButton(parcel);
            parcel1.writeNoException();
            i = ((flag1) ? 1 : 0);
            if(flag3)
                i = 1;
            parcel1.writeInt(i);
            continue; /* Loop/switch isn't completed */
_L4:
            parcel.enforceInterface("android.support.v4.media.session.IMediaSession");
            registerCallbackListener(IMediaControllerCallback.Stub.asInterface(parcel.readStrongBinder()));
            parcel1.writeNoException();
            continue; /* Loop/switch isn't completed */
_L5:
            parcel.enforceInterface("android.support.v4.media.session.IMediaSession");
            unregisterCallbackListener(IMediaControllerCallback.Stub.asInterface(parcel.readStrongBinder()));
            parcel1.writeNoException();
            continue; /* Loop/switch isn't completed */
_L6:
            parcel.enforceInterface("android.support.v4.media.session.IMediaSession");
            boolean flag4 = isTransportControlEnabled();
            parcel1.writeNoException();
            i = ((flag) ? 1 : 0);
            if(flag4)
                i = 1;
            parcel1.writeInt(i);
            continue; /* Loop/switch isn't completed */
_L7:
            parcel.enforceInterface("android.support.v4.media.session.IMediaSession");
            parcel = getPackageName();
            parcel1.writeNoException();
            parcel1.writeString(parcel);
            continue; /* Loop/switch isn't completed */
_L8:
            parcel.enforceInterface("android.support.v4.media.session.IMediaSession");
            parcel = getTag();
            parcel1.writeNoException();
            parcel1.writeString(parcel);
            continue; /* Loop/switch isn't completed */
_L9:
            parcel.enforceInterface("android.support.v4.media.session.IMediaSession");
            parcel = getLaunchPendingIntent();
            parcel1.writeNoException();
            if(parcel != null)
            {
                parcel1.writeInt(1);
                parcel.writeToParcel(parcel1, 1);
            } else
            {
                parcel1.writeInt(0);
            }
            continue; /* Loop/switch isn't completed */
_L10:
            parcel.enforceInterface("android.support.v4.media.session.IMediaSession");
            long l = getFlags();
            parcel1.writeNoException();
            parcel1.writeLong(l);
            continue; /* Loop/switch isn't completed */
_L11:
            parcel.enforceInterface("android.support.v4.media.session.IMediaSession");
            parcel = getVolumeAttributes();
            parcel1.writeNoException();
            if(parcel != null)
            {
                parcel1.writeInt(1);
                parcel.writeToParcel(parcel1, 1);
            } else
            {
                parcel1.writeInt(0);
            }
            continue; /* Loop/switch isn't completed */
_L12:
            parcel.enforceInterface("android.support.v4.media.session.IMediaSession");
            adjustVolume(parcel.readInt(), parcel.readInt(), parcel.readString());
            parcel1.writeNoException();
            continue; /* Loop/switch isn't completed */
_L13:
            parcel.enforceInterface("android.support.v4.media.session.IMediaSession");
            setVolumeTo(parcel.readInt(), parcel.readInt(), parcel.readString());
            parcel1.writeNoException();
            continue; /* Loop/switch isn't completed */
_L14:
            parcel.enforceInterface("android.support.v4.media.session.IMediaSession");
            play();
            parcel1.writeNoException();
            continue; /* Loop/switch isn't completed */
_L15:
            parcel.enforceInterface("android.support.v4.media.session.IMediaSession");
            String s1 = parcel.readString();
            if(parcel.readInt() != 0)
                parcel = (Bundle)Bundle.CREATOR.createFromParcel(parcel);
            else
                parcel = null;
            playFromMediaId(s1, parcel);
            parcel1.writeNoException();
            continue; /* Loop/switch isn't completed */
_L16:
            parcel.enforceInterface("android.support.v4.media.session.IMediaSession");
            String s2 = parcel.readString();
            if(parcel.readInt() != 0)
                parcel = (Bundle)Bundle.CREATOR.createFromParcel(parcel);
            else
                parcel = null;
            playFromSearch(s2, parcel);
            parcel1.writeNoException();
            continue; /* Loop/switch isn't completed */
_L17:
            parcel.enforceInterface("android.support.v4.media.session.IMediaSession");
            Uri uri;
            if(parcel.readInt() != 0)
                uri = (Uri)Uri.CREATOR.createFromParcel(parcel);
            else
                uri = null;
            if(parcel.readInt() != 0)
                parcel = (Bundle)Bundle.CREATOR.createFromParcel(parcel);
            else
                parcel = null;
            playFromUri(uri, parcel);
            parcel1.writeNoException();
            continue; /* Loop/switch isn't completed */
_L18:
            parcel.enforceInterface("android.support.v4.media.session.IMediaSession");
            skipToQueueItem(parcel.readLong());
            parcel1.writeNoException();
            continue; /* Loop/switch isn't completed */
_L19:
            parcel.enforceInterface("android.support.v4.media.session.IMediaSession");
            pause();
            parcel1.writeNoException();
            continue; /* Loop/switch isn't completed */
_L20:
            parcel.enforceInterface("android.support.v4.media.session.IMediaSession");
            stop();
            parcel1.writeNoException();
            continue; /* Loop/switch isn't completed */
_L21:
            parcel.enforceInterface("android.support.v4.media.session.IMediaSession");
            next();
            parcel1.writeNoException();
            continue; /* Loop/switch isn't completed */
_L22:
            parcel.enforceInterface("android.support.v4.media.session.IMediaSession");
            previous();
            parcel1.writeNoException();
            continue; /* Loop/switch isn't completed */
_L23:
            parcel.enforceInterface("android.support.v4.media.session.IMediaSession");
            fastForward();
            parcel1.writeNoException();
            continue; /* Loop/switch isn't completed */
_L24:
            parcel.enforceInterface("android.support.v4.media.session.IMediaSession");
            rewind();
            parcel1.writeNoException();
            continue; /* Loop/switch isn't completed */
_L25:
            parcel.enforceInterface("android.support.v4.media.session.IMediaSession");
            seekTo(parcel.readLong());
            parcel1.writeNoException();
            continue; /* Loop/switch isn't completed */
_L26:
            parcel.enforceInterface("android.support.v4.media.session.IMediaSession");
            if(parcel.readInt() != 0)
                parcel = (RatingCompat)RatingCompat.CREATOR.createFromParcel(parcel);
            else
                parcel = null;
            rate(parcel);
            parcel1.writeNoException();
            continue; /* Loop/switch isn't completed */
_L27:
            parcel.enforceInterface("android.support.v4.media.session.IMediaSession");
            String s3 = parcel.readString();
            if(parcel.readInt() != 0)
                parcel = (Bundle)Bundle.CREATOR.createFromParcel(parcel);
            else
                parcel = null;
            sendCustomAction(s3, parcel);
            parcel1.writeNoException();
            continue; /* Loop/switch isn't completed */
_L28:
            parcel.enforceInterface("android.support.v4.media.session.IMediaSession");
            parcel = getMetadata();
            parcel1.writeNoException();
            if(parcel != null)
            {
                parcel1.writeInt(1);
                parcel.writeToParcel(parcel1, 1);
            } else
            {
                parcel1.writeInt(0);
            }
            continue; /* Loop/switch isn't completed */
_L29:
            parcel.enforceInterface("android.support.v4.media.session.IMediaSession");
            parcel = getPlaybackState();
            parcel1.writeNoException();
            if(parcel != null)
            {
                parcel1.writeInt(1);
                parcel.writeToParcel(parcel1, 1);
            } else
            {
                parcel1.writeInt(0);
            }
            continue; /* Loop/switch isn't completed */
_L30:
            parcel.enforceInterface("android.support.v4.media.session.IMediaSession");
            parcel = getQueue();
            parcel1.writeNoException();
            parcel1.writeTypedList(parcel);
            continue; /* Loop/switch isn't completed */
_L31:
            parcel.enforceInterface("android.support.v4.media.session.IMediaSession");
            parcel = getQueueTitle();
            parcel1.writeNoException();
            if(parcel != null)
            {
                parcel1.writeInt(1);
                TextUtils.writeToParcel(parcel, parcel1, 1);
            } else
            {
                parcel1.writeInt(0);
            }
            continue; /* Loop/switch isn't completed */
_L32:
            parcel.enforceInterface("android.support.v4.media.session.IMediaSession");
            parcel = getExtras();
            parcel1.writeNoException();
            if(parcel != null)
            {
                parcel1.writeInt(1);
                parcel.writeToParcel(parcel1, 1);
            } else
            {
                parcel1.writeInt(0);
            }
            continue; /* Loop/switch isn't completed */
_L33:
            parcel.enforceInterface("android.support.v4.media.session.IMediaSession");
            i = getRatingType();
            parcel1.writeNoException();
            parcel1.writeInt(i);
            if(true) goto _L36; else goto _L35
_L35:
        }

        private static final String DESCRIPTOR = "android.support.v4.media.session.IMediaSession";
        static final int TRANSACTION_adjustVolume = 11;
        static final int TRANSACTION_fastForward = 22;
        static final int TRANSACTION_getExtras = 31;
        static final int TRANSACTION_getFlags = 9;
        static final int TRANSACTION_getLaunchPendingIntent = 8;
        static final int TRANSACTION_getMetadata = 27;
        static final int TRANSACTION_getPackageName = 6;
        static final int TRANSACTION_getPlaybackState = 28;
        static final int TRANSACTION_getQueue = 29;
        static final int TRANSACTION_getQueueTitle = 30;
        static final int TRANSACTION_getRatingType = 32;
        static final int TRANSACTION_getTag = 7;
        static final int TRANSACTION_getVolumeAttributes = 10;
        static final int TRANSACTION_isTransportControlEnabled = 5;
        static final int TRANSACTION_next = 20;
        static final int TRANSACTION_pause = 18;
        static final int TRANSACTION_play = 13;
        static final int TRANSACTION_playFromMediaId = 14;
        static final int TRANSACTION_playFromSearch = 15;
        static final int TRANSACTION_playFromUri = 16;
        static final int TRANSACTION_previous = 21;
        static final int TRANSACTION_rate = 25;
        static final int TRANSACTION_registerCallbackListener = 3;
        static final int TRANSACTION_rewind = 23;
        static final int TRANSACTION_seekTo = 24;
        static final int TRANSACTION_sendCommand = 1;
        static final int TRANSACTION_sendCustomAction = 26;
        static final int TRANSACTION_sendMediaButton = 2;
        static final int TRANSACTION_setVolumeTo = 12;
        static final int TRANSACTION_skipToQueueItem = 17;
        static final int TRANSACTION_stop = 19;
        static final int TRANSACTION_unregisterCallbackListener = 4;

        public Stub()
        {
            attachInterface(this, "android.support.v4.media.session.IMediaSession");
        }
    }

    private static class Stub.Proxy
        implements IMediaSession
    {

        public void adjustVolume(int i, int j, String s)
            throws RemoteException
        {
            Parcel parcel;
            Parcel parcel1;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            parcel.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            parcel.writeInt(i);
            parcel.writeInt(j);
            parcel.writeString(s);
            mRemote.transact(11, parcel, parcel1, 0);
            parcel1.readException();
            parcel1.recycle();
            parcel.recycle();
            return;
            s;
            parcel1.recycle();
            parcel.recycle();
            throw s;
        }

        public IBinder asBinder()
        {
            return mRemote;
        }

        public void fastForward()
            throws RemoteException
        {
            Parcel parcel;
            Parcel parcel1;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            parcel.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            mRemote.transact(22, parcel, parcel1, 0);
            parcel1.readException();
            parcel1.recycle();
            parcel.recycle();
            return;
            Exception exception;
            exception;
            parcel1.recycle();
            parcel.recycle();
            throw exception;
        }

        public Bundle getExtras()
            throws RemoteException
        {
            Parcel parcel;
            Parcel parcel1;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            parcel.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            mRemote.transact(31, parcel, parcel1, 0);
            parcel1.readException();
            if(parcel1.readInt() == 0) goto _L2; else goto _L1
_L1:
            Bundle bundle = (Bundle)Bundle.CREATOR.createFromParcel(parcel1);
_L4:
            parcel1.recycle();
            parcel.recycle();
            return bundle;
_L2:
            bundle = null;
            if(true) goto _L4; else goto _L3
_L3:
            Exception exception;
            exception;
            parcel1.recycle();
            parcel.recycle();
            throw exception;
        }

        public long getFlags()
            throws RemoteException
        {
            Parcel parcel;
            Parcel parcel1;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            long l;
            parcel.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            mRemote.transact(9, parcel, parcel1, 0);
            parcel1.readException();
            l = parcel1.readLong();
            parcel1.recycle();
            parcel.recycle();
            return l;
            Exception exception;
            exception;
            parcel1.recycle();
            parcel.recycle();
            throw exception;
        }

        public String getInterfaceDescriptor()
        {
            return "android.support.v4.media.session.IMediaSession";
        }

        public PendingIntent getLaunchPendingIntent()
            throws RemoteException
        {
            Parcel parcel;
            Parcel parcel1;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            parcel.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            mRemote.transact(8, parcel, parcel1, 0);
            parcel1.readException();
            if(parcel1.readInt() == 0) goto _L2; else goto _L1
_L1:
            PendingIntent pendingintent = (PendingIntent)PendingIntent.CREATOR.createFromParcel(parcel1);
_L4:
            parcel1.recycle();
            parcel.recycle();
            return pendingintent;
_L2:
            pendingintent = null;
            if(true) goto _L4; else goto _L3
_L3:
            Exception exception;
            exception;
            parcel1.recycle();
            parcel.recycle();
            throw exception;
        }

        public MediaMetadataCompat getMetadata()
            throws RemoteException
        {
            Parcel parcel;
            Parcel parcel1;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            parcel.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            mRemote.transact(27, parcel, parcel1, 0);
            parcel1.readException();
            if(parcel1.readInt() == 0) goto _L2; else goto _L1
_L1:
            MediaMetadataCompat mediametadatacompat = (MediaMetadataCompat)MediaMetadataCompat.CREATOR.createFromParcel(parcel1);
_L4:
            parcel1.recycle();
            parcel.recycle();
            return mediametadatacompat;
_L2:
            mediametadatacompat = null;
            if(true) goto _L4; else goto _L3
_L3:
            Exception exception;
            exception;
            parcel1.recycle();
            parcel.recycle();
            throw exception;
        }

        public String getPackageName()
            throws RemoteException
        {
            Parcel parcel;
            Parcel parcel1;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            String s;
            parcel.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            mRemote.transact(6, parcel, parcel1, 0);
            parcel1.readException();
            s = parcel1.readString();
            parcel1.recycle();
            parcel.recycle();
            return s;
            Exception exception;
            exception;
            parcel1.recycle();
            parcel.recycle();
            throw exception;
        }

        public PlaybackStateCompat getPlaybackState()
            throws RemoteException
        {
            Parcel parcel;
            Parcel parcel1;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            parcel.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            mRemote.transact(28, parcel, parcel1, 0);
            parcel1.readException();
            if(parcel1.readInt() == 0) goto _L2; else goto _L1
_L1:
            PlaybackStateCompat playbackstatecompat = (PlaybackStateCompat)PlaybackStateCompat.CREATOR.createFromParcel(parcel1);
_L4:
            parcel1.recycle();
            parcel.recycle();
            return playbackstatecompat;
_L2:
            playbackstatecompat = null;
            if(true) goto _L4; else goto _L3
_L3:
            Exception exception;
            exception;
            parcel1.recycle();
            parcel.recycle();
            throw exception;
        }

        public List getQueue()
            throws RemoteException
        {
            Parcel parcel;
            Parcel parcel1;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            java.util.ArrayList arraylist;
            parcel.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            mRemote.transact(29, parcel, parcel1, 0);
            parcel1.readException();
            arraylist = parcel1.createTypedArrayList(MediaSessionCompat.QueueItem.CREATOR);
            parcel1.recycle();
            parcel.recycle();
            return arraylist;
            Exception exception;
            exception;
            parcel1.recycle();
            parcel.recycle();
            throw exception;
        }

        public CharSequence getQueueTitle()
            throws RemoteException
        {
            Parcel parcel;
            Parcel parcel1;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            parcel.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            mRemote.transact(30, parcel, parcel1, 0);
            parcel1.readException();
            if(parcel1.readInt() == 0) goto _L2; else goto _L1
_L1:
            CharSequence charsequence = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel1);
_L4:
            parcel1.recycle();
            parcel.recycle();
            return charsequence;
_L2:
            charsequence = null;
            if(true) goto _L4; else goto _L3
_L3:
            Exception exception;
            exception;
            parcel1.recycle();
            parcel.recycle();
            throw exception;
        }

        public int getRatingType()
            throws RemoteException
        {
            Parcel parcel;
            Parcel parcel1;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            int i;
            parcel.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            mRemote.transact(32, parcel, parcel1, 0);
            parcel1.readException();
            i = parcel1.readInt();
            parcel1.recycle();
            parcel.recycle();
            return i;
            Exception exception;
            exception;
            parcel1.recycle();
            parcel.recycle();
            throw exception;
        }

        public String getTag()
            throws RemoteException
        {
            Parcel parcel;
            Parcel parcel1;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            String s;
            parcel.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            mRemote.transact(7, parcel, parcel1, 0);
            parcel1.readException();
            s = parcel1.readString();
            parcel1.recycle();
            parcel.recycle();
            return s;
            Exception exception;
            exception;
            parcel1.recycle();
            parcel.recycle();
            throw exception;
        }

        public ParcelableVolumeInfo getVolumeAttributes()
            throws RemoteException
        {
            Parcel parcel;
            Parcel parcel1;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            parcel.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            mRemote.transact(10, parcel, parcel1, 0);
            parcel1.readException();
            if(parcel1.readInt() == 0) goto _L2; else goto _L1
_L1:
            ParcelableVolumeInfo parcelablevolumeinfo = (ParcelableVolumeInfo)ParcelableVolumeInfo.CREATOR.createFromParcel(parcel1);
_L4:
            parcel1.recycle();
            parcel.recycle();
            return parcelablevolumeinfo;
_L2:
            parcelablevolumeinfo = null;
            if(true) goto _L4; else goto _L3
_L3:
            Exception exception;
            exception;
            parcel1.recycle();
            parcel.recycle();
            throw exception;
        }

        public boolean isTransportControlEnabled()
            throws RemoteException
        {
            boolean flag;
            Parcel parcel;
            Parcel parcel1;
            flag = false;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            int i;
            parcel.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            mRemote.transact(5, parcel, parcel1, 0);
            parcel1.readException();
            i = parcel1.readInt();
            if(i != 0)
                flag = true;
            parcel1.recycle();
            parcel.recycle();
            return flag;
            Exception exception;
            exception;
            parcel1.recycle();
            parcel.recycle();
            throw exception;
        }

        public void next()
            throws RemoteException
        {
            Parcel parcel;
            Parcel parcel1;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            parcel.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            mRemote.transact(20, parcel, parcel1, 0);
            parcel1.readException();
            parcel1.recycle();
            parcel.recycle();
            return;
            Exception exception;
            exception;
            parcel1.recycle();
            parcel.recycle();
            throw exception;
        }

        public void pause()
            throws RemoteException
        {
            Parcel parcel;
            Parcel parcel1;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            parcel.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            mRemote.transact(18, parcel, parcel1, 0);
            parcel1.readException();
            parcel1.recycle();
            parcel.recycle();
            return;
            Exception exception;
            exception;
            parcel1.recycle();
            parcel.recycle();
            throw exception;
        }

        public void play()
            throws RemoteException
        {
            Parcel parcel;
            Parcel parcel1;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            parcel.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            mRemote.transact(13, parcel, parcel1, 0);
            parcel1.readException();
            parcel1.recycle();
            parcel.recycle();
            return;
            Exception exception;
            exception;
            parcel1.recycle();
            parcel.recycle();
            throw exception;
        }

        public void playFromMediaId(String s, Bundle bundle)
            throws RemoteException
        {
            Parcel parcel;
            Parcel parcel1;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            parcel.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            parcel.writeString(s);
            if(bundle == null)
                break MISSING_BLOCK_LABEL_66;
            parcel.writeInt(1);
            bundle.writeToParcel(parcel, 0);
_L1:
            mRemote.transact(14, parcel, parcel1, 0);
            parcel1.readException();
            parcel1.recycle();
            parcel.recycle();
            return;
            parcel.writeInt(0);
              goto _L1
            s;
            parcel1.recycle();
            parcel.recycle();
            throw s;
        }

        public void playFromSearch(String s, Bundle bundle)
            throws RemoteException
        {
            Parcel parcel;
            Parcel parcel1;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            parcel.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            parcel.writeString(s);
            if(bundle == null)
                break MISSING_BLOCK_LABEL_66;
            parcel.writeInt(1);
            bundle.writeToParcel(parcel, 0);
_L1:
            mRemote.transact(15, parcel, parcel1, 0);
            parcel1.readException();
            parcel1.recycle();
            parcel.recycle();
            return;
            parcel.writeInt(0);
              goto _L1
            s;
            parcel1.recycle();
            parcel.recycle();
            throw s;
        }

        public void playFromUri(Uri uri, Bundle bundle)
            throws RemoteException
        {
            Parcel parcel;
            Parcel parcel1;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            parcel.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            if(uri == null) goto _L2; else goto _L1
_L1:
            parcel.writeInt(1);
            uri.writeToParcel(parcel, 0);
_L3:
            if(bundle == null)
                break MISSING_BLOCK_LABEL_96;
            parcel.writeInt(1);
            bundle.writeToParcel(parcel, 0);
_L4:
            mRemote.transact(16, parcel, parcel1, 0);
            parcel1.readException();
            parcel1.recycle();
            parcel.recycle();
            return;
_L2:
            parcel.writeInt(0);
              goto _L3
            uri;
            parcel1.recycle();
            parcel.recycle();
            throw uri;
            parcel.writeInt(0);
              goto _L4
        }

        public void previous()
            throws RemoteException
        {
            Parcel parcel;
            Parcel parcel1;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            parcel.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            mRemote.transact(21, parcel, parcel1, 0);
            parcel1.readException();
            parcel1.recycle();
            parcel.recycle();
            return;
            Exception exception;
            exception;
            parcel1.recycle();
            parcel.recycle();
            throw exception;
        }

        public void rate(RatingCompat ratingcompat)
            throws RemoteException
        {
            Parcel parcel;
            Parcel parcel1;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            parcel.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            if(ratingcompat == null)
                break MISSING_BLOCK_LABEL_57;
            parcel.writeInt(1);
            ratingcompat.writeToParcel(parcel, 0);
_L1:
            mRemote.transact(25, parcel, parcel1, 0);
            parcel1.readException();
            parcel1.recycle();
            parcel.recycle();
            return;
            parcel.writeInt(0);
              goto _L1
            ratingcompat;
            parcel1.recycle();
            parcel.recycle();
            throw ratingcompat;
        }

        public void registerCallbackListener(IMediaControllerCallback imediacontrollercallback)
            throws RemoteException
        {
            Parcel parcel;
            Parcel parcel1;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            parcel.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            if(imediacontrollercallback == null)
                break MISSING_BLOCK_LABEL_57;
            imediacontrollercallback = imediacontrollercallback.asBinder();
_L1:
            parcel.writeStrongBinder(imediacontrollercallback);
            mRemote.transact(3, parcel, parcel1, 0);
            parcel1.readException();
            parcel1.recycle();
            parcel.recycle();
            return;
            imediacontrollercallback = null;
              goto _L1
            imediacontrollercallback;
            parcel1.recycle();
            parcel.recycle();
            throw imediacontrollercallback;
        }

        public void rewind()
            throws RemoteException
        {
            Parcel parcel;
            Parcel parcel1;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            parcel.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            mRemote.transact(23, parcel, parcel1, 0);
            parcel1.readException();
            parcel1.recycle();
            parcel.recycle();
            return;
            Exception exception;
            exception;
            parcel1.recycle();
            parcel.recycle();
            throw exception;
        }

        public void seekTo(long l)
            throws RemoteException
        {
            Parcel parcel;
            Parcel parcel1;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            parcel.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            parcel.writeLong(l);
            mRemote.transact(24, parcel, parcel1, 0);
            parcel1.readException();
            parcel1.recycle();
            parcel.recycle();
            return;
            Exception exception;
            exception;
            parcel1.recycle();
            parcel.recycle();
            throw exception;
        }

        public void sendCommand(String s, Bundle bundle, MediaSessionCompat.ResultReceiverWrapper resultreceiverwrapper)
            throws RemoteException
        {
            Parcel parcel;
            Parcel parcel1;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            parcel.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            parcel.writeString(s);
            if(bundle == null) goto _L2; else goto _L1
_L1:
            parcel.writeInt(1);
            bundle.writeToParcel(parcel, 0);
_L3:
            if(resultreceiverwrapper == null)
                break MISSING_BLOCK_LABEL_111;
            parcel.writeInt(1);
            resultreceiverwrapper.writeToParcel(parcel, 0);
_L4:
            mRemote.transact(1, parcel, parcel1, 0);
            parcel1.readException();
            parcel1.recycle();
            parcel.recycle();
            return;
_L2:
            parcel.writeInt(0);
              goto _L3
            s;
            parcel1.recycle();
            parcel.recycle();
            throw s;
            parcel.writeInt(0);
              goto _L4
        }

        public void sendCustomAction(String s, Bundle bundle)
            throws RemoteException
        {
            Parcel parcel;
            Parcel parcel1;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            parcel.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            parcel.writeString(s);
            if(bundle == null)
                break MISSING_BLOCK_LABEL_66;
            parcel.writeInt(1);
            bundle.writeToParcel(parcel, 0);
_L1:
            mRemote.transact(26, parcel, parcel1, 0);
            parcel1.readException();
            parcel1.recycle();
            parcel.recycle();
            return;
            parcel.writeInt(0);
              goto _L1
            s;
            parcel1.recycle();
            parcel.recycle();
            throw s;
        }

        public boolean sendMediaButton(KeyEvent keyevent)
            throws RemoteException
        {
            boolean flag;
            Parcel parcel;
            Parcel parcel1;
            flag = true;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            parcel.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            if(keyevent == null)
                break MISSING_BLOCK_LABEL_75;
            parcel.writeInt(1);
            keyevent.writeToParcel(parcel, 0);
_L1:
            int i;
            mRemote.transact(2, parcel, parcel1, 0);
            parcel1.readException();
            i = parcel1.readInt();
            if(i == 0)
                flag = false;
            parcel1.recycle();
            parcel.recycle();
            return flag;
            parcel.writeInt(0);
              goto _L1
            keyevent;
            parcel1.recycle();
            parcel.recycle();
            throw keyevent;
        }

        public void setVolumeTo(int i, int j, String s)
            throws RemoteException
        {
            Parcel parcel;
            Parcel parcel1;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            parcel.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            parcel.writeInt(i);
            parcel.writeInt(j);
            parcel.writeString(s);
            mRemote.transact(12, parcel, parcel1, 0);
            parcel1.readException();
            parcel1.recycle();
            parcel.recycle();
            return;
            s;
            parcel1.recycle();
            parcel.recycle();
            throw s;
        }

        public void skipToQueueItem(long l)
            throws RemoteException
        {
            Parcel parcel;
            Parcel parcel1;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            parcel.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            parcel.writeLong(l);
            mRemote.transact(17, parcel, parcel1, 0);
            parcel1.readException();
            parcel1.recycle();
            parcel.recycle();
            return;
            Exception exception;
            exception;
            parcel1.recycle();
            parcel.recycle();
            throw exception;
        }

        public void stop()
            throws RemoteException
        {
            Parcel parcel;
            Parcel parcel1;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            parcel.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            mRemote.transact(19, parcel, parcel1, 0);
            parcel1.readException();
            parcel1.recycle();
            parcel.recycle();
            return;
            Exception exception;
            exception;
            parcel1.recycle();
            parcel.recycle();
            throw exception;
        }

        public void unregisterCallbackListener(IMediaControllerCallback imediacontrollercallback)
            throws RemoteException
        {
            Parcel parcel;
            Parcel parcel1;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            parcel.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            if(imediacontrollercallback == null)
                break MISSING_BLOCK_LABEL_57;
            imediacontrollercallback = imediacontrollercallback.asBinder();
_L1:
            parcel.writeStrongBinder(imediacontrollercallback);
            mRemote.transact(4, parcel, parcel1, 0);
            parcel1.readException();
            parcel1.recycle();
            parcel.recycle();
            return;
            imediacontrollercallback = null;
              goto _L1
            imediacontrollercallback;
            parcel1.recycle();
            parcel.recycle();
            throw imediacontrollercallback;
        }

        private IBinder mRemote;

        Stub.Proxy(IBinder ibinder)
        {
            mRemote = ibinder;
        }
    }


    public abstract void adjustVolume(int i, int j, String s)
        throws RemoteException;

    public abstract void fastForward()
        throws RemoteException;

    public abstract Bundle getExtras()
        throws RemoteException;

    public abstract long getFlags()
        throws RemoteException;

    public abstract PendingIntent getLaunchPendingIntent()
        throws RemoteException;

    public abstract MediaMetadataCompat getMetadata()
        throws RemoteException;

    public abstract String getPackageName()
        throws RemoteException;

    public abstract PlaybackStateCompat getPlaybackState()
        throws RemoteException;

    public abstract List getQueue()
        throws RemoteException;

    public abstract CharSequence getQueueTitle()
        throws RemoteException;

    public abstract int getRatingType()
        throws RemoteException;

    public abstract String getTag()
        throws RemoteException;

    public abstract ParcelableVolumeInfo getVolumeAttributes()
        throws RemoteException;

    public abstract boolean isTransportControlEnabled()
        throws RemoteException;

    public abstract void next()
        throws RemoteException;

    public abstract void pause()
        throws RemoteException;

    public abstract void play()
        throws RemoteException;

    public abstract void playFromMediaId(String s, Bundle bundle)
        throws RemoteException;

    public abstract void playFromSearch(String s, Bundle bundle)
        throws RemoteException;

    public abstract void playFromUri(Uri uri, Bundle bundle)
        throws RemoteException;

    public abstract void previous()
        throws RemoteException;

    public abstract void rate(RatingCompat ratingcompat)
        throws RemoteException;

    public abstract void registerCallbackListener(IMediaControllerCallback imediacontrollercallback)
        throws RemoteException;

    public abstract void rewind()
        throws RemoteException;

    public abstract void seekTo(long l)
        throws RemoteException;

    public abstract void sendCommand(String s, Bundle bundle, MediaSessionCompat.ResultReceiverWrapper resultreceiverwrapper)
        throws RemoteException;

    public abstract void sendCustomAction(String s, Bundle bundle)
        throws RemoteException;

    public abstract boolean sendMediaButton(KeyEvent keyevent)
        throws RemoteException;

    public abstract void setVolumeTo(int i, int j, String s)
        throws RemoteException;

    public abstract void skipToQueueItem(long l)
        throws RemoteException;

    public abstract void stop()
        throws RemoteException;

    public abstract void unregisterCallbackListener(IMediaControllerCallback imediacontrollercallback)
        throws RemoteException;
}
