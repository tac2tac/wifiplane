// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.media;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.*;
import android.support.v4.app.BundleCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.*;

// Referenced classes of package android.support.v4.media:
//            MediaBrowserCompatUtils, MediaBrowserServiceCompatApi21, MediaBrowserServiceCompatApi23, MediaBrowserCompat

public abstract class MediaBrowserServiceCompat extends Service
{
    public static final class BrowserRoot
    {

        public Bundle getExtras()
        {
            return mExtras;
        }

        public String getRootId()
        {
            return mRootId;
        }

        public static final String EXTRA_OFFLINE = "android.service.media.extra.OFFLINE";
        public static final String EXTRA_RECENT = "android.service.media.extra.RECENT";
        public static final String EXTRA_SUGGESTED = "android.service.media.extra.SUGGESTED";
        private final Bundle mExtras;
        private final String mRootId;

        public BrowserRoot(String s, Bundle bundle)
        {
            if(s == null)
            {
                throw new IllegalArgumentException("The root id in BrowserRoot cannot be null. Use null for BrowserRoot instead.");
            } else
            {
                mRootId = s;
                mExtras = bundle;
                return;
            }
        }
    }

    private class ConnectionRecord
    {

        ServiceCallbacks callbacks;
        String pkg;
        BrowserRoot root;
        Bundle rootHints;
        HashMap subscriptions;
        final MediaBrowserServiceCompat this$0;

        private ConnectionRecord()
        {
            this$0 = MediaBrowserServiceCompat.this;
            super();
            subscriptions = new HashMap();
        }

    }

    static interface MediaBrowserServiceImpl
    {

        public abstract IBinder onBind(Intent intent);

        public abstract void onCreate();
    }

    class MediaBrowserServiceImplApi21
        implements MediaBrowserServiceImpl
    {

        public IBinder onBind(Intent intent)
        {
            return MediaBrowserServiceCompatApi21.onBind(mServiceObj, intent);
        }

        public void onCreate()
        {
            mServiceObj = MediaBrowserServiceCompatApi21.createService();
            MediaBrowserServiceCompatApi21.onCreate(mServiceObj, new ServiceImplApi21());
        }

        private Object mServiceObj;
        final MediaBrowserServiceCompat this$0;

        MediaBrowserServiceImplApi21()
        {
            this$0 = MediaBrowserServiceCompat.this;
            super();
        }
    }

    class MediaBrowserServiceImplApi23
        implements MediaBrowserServiceImpl
    {

        public IBinder onBind(Intent intent)
        {
            return MediaBrowserServiceCompatApi23.onBind(mServiceObj, intent);
        }

        public void onCreate()
        {
            mServiceObj = MediaBrowserServiceCompatApi23.createService();
            MediaBrowserServiceCompatApi23.onCreate(mServiceObj, new ServiceImplApi23());
        }

        private Object mServiceObj;
        final MediaBrowserServiceCompat this$0;

        MediaBrowserServiceImplApi23()
        {
            this$0 = MediaBrowserServiceCompat.this;
            super();
        }
    }

    class MediaBrowserServiceImplBase
        implements MediaBrowserServiceImpl
    {

        public IBinder onBind(Intent intent)
        {
            if("android.media.browse.MediaBrowserService".equals(intent.getAction()))
                intent = mMessenger.getBinder();
            else
                intent = null;
            return intent;
        }

        public void onCreate()
        {
            mMessenger = new Messenger(mHandler);
        }

        private Messenger mMessenger;
        final MediaBrowserServiceCompat this$0;

        MediaBrowserServiceImplBase()
        {
            this$0 = MediaBrowserServiceCompat.this;
            super();
        }
    }

    public static class Result
    {

        public void detach()
        {
            if(mDetachCalled)
                throw new IllegalStateException((new StringBuilder()).append("detach() called when detach() had already been called for: ").append(mDebug).toString());
            if(mSendResultCalled)
            {
                throw new IllegalStateException((new StringBuilder()).append("detach() called when sendResult() had already been called for: ").append(mDebug).toString());
            } else
            {
                mDetachCalled = true;
                return;
            }
        }

        boolean isDone()
        {
            boolean flag;
            if(mDetachCalled || mSendResultCalled)
                flag = true;
            else
                flag = false;
            return flag;
        }

        void onResultSent(Object obj, int i)
        {
        }

        public void sendResult(Object obj)
        {
            if(mSendResultCalled)
            {
                throw new IllegalStateException((new StringBuilder()).append("sendResult() called twice for: ").append(mDebug).toString());
            } else
            {
                mSendResultCalled = true;
                onResultSent(obj, mFlags);
                return;
            }
        }

        void setFlags(int i)
        {
            mFlags = i;
        }

        private Object mDebug;
        private boolean mDetachCalled;
        private int mFlags;
        private boolean mSendResultCalled;

        Result(Object obj)
        {
            mDebug = obj;
        }
    }

    private static interface ResultFlags
        extends Annotation
    {
    }

    private static interface ServiceCallbacks
    {

        public abstract IBinder asBinder();

        public abstract void onConnect(String s, android.support.v4.media.session.MediaSessionCompat.Token token, Bundle bundle)
            throws RemoteException;

        public abstract void onConnectFailed()
            throws RemoteException;

        public abstract void onLoadChildren(String s, List list, Bundle bundle)
            throws RemoteException;
    }

    private class ServiceCallbacksApi21
        implements ServiceCallbacks
    {

        public IBinder asBinder()
        {
            return mCallbacks.asBinder();
        }

        public void onConnect(String s, android.support.v4.media.session.MediaSessionCompat.Token token, Bundle bundle)
            throws RemoteException
        {
            Bundle bundle1 = bundle;
            if(bundle == null)
                bundle1 = new Bundle();
            mMessenger = new Messenger(mHandler);
            BundleCompat.putBinder(bundle1, "extra_messenger", mMessenger.getBinder());
            bundle1.putInt("extra_service_version", 1);
            mCallbacks.onConnect(s, token.getToken(), bundle1);
        }

        public void onConnectFailed()
            throws RemoteException
        {
            mCallbacks.onConnectFailed();
        }

        public void onLoadChildren(String s, List list, Bundle bundle)
            throws RemoteException
        {
            bundle = null;
            if(list != null)
            {
                ArrayList arraylist = new ArrayList();
                list = list.iterator();
                do
                {
                    bundle = arraylist;
                    if(!list.hasNext())
                        break;
                    MediaBrowserCompat.MediaItem mediaitem = (MediaBrowserCompat.MediaItem)list.next();
                    bundle = Parcel.obtain();
                    mediaitem.writeToParcel(bundle, 0);
                    arraylist.add(bundle);
                } while(true);
            }
            mCallbacks.onLoadChildren(s, bundle);
        }

        final MediaBrowserServiceCompatApi21.ServiceCallbacks mCallbacks;
        Messenger mMessenger;
        final MediaBrowserServiceCompat this$0;

        ServiceCallbacksApi21(MediaBrowserServiceCompatApi21.ServiceCallbacks servicecallbacks)
        {
            this$0 = MediaBrowserServiceCompat.this;
            super();
            mCallbacks = servicecallbacks;
        }
    }

    private class ServiceCallbacksCompat
        implements ServiceCallbacks
    {

        private void sendRequest(int i, Bundle bundle)
            throws RemoteException
        {
            Message message = Message.obtain();
            message.what = i;
            message.arg1 = 1;
            message.setData(bundle);
            mCallbacks.send(message);
        }

        public IBinder asBinder()
        {
            return mCallbacks.getBinder();
        }

        public void onConnect(String s, android.support.v4.media.session.MediaSessionCompat.Token token, Bundle bundle)
            throws RemoteException
        {
            Bundle bundle1 = bundle;
            if(bundle == null)
                bundle1 = new Bundle();
            bundle1.putInt("extra_service_version", 1);
            bundle = new Bundle();
            bundle.putString("data_media_item_id", s);
            bundle.putParcelable("data_media_session_token", token);
            bundle.putBundle("data_root_hints", bundle1);
            sendRequest(1, bundle);
        }

        public void onConnectFailed()
            throws RemoteException
        {
            sendRequest(2, null);
        }

        public void onLoadChildren(String s, List list, Bundle bundle)
            throws RemoteException
        {
            Bundle bundle1 = new Bundle();
            bundle1.putString("data_media_item_id", s);
            bundle1.putBundle("data_options", bundle);
            if(list != null)
            {
                if(list instanceof ArrayList)
                    s = (ArrayList)list;
                else
                    s = new ArrayList(list);
                bundle1.putParcelableArrayList("data_media_item_list", s);
            }
            sendRequest(3, bundle1);
        }

        final Messenger mCallbacks;
        final MediaBrowserServiceCompat this$0;

        ServiceCallbacksCompat(Messenger messenger)
        {
            this$0 = MediaBrowserServiceCompat.this;
            super();
            mCallbacks = messenger;
        }
    }

    private final class ServiceHandler extends Handler
    {

        public ServiceImpl getServiceImpl()
        {
            return mServiceImpl;
        }

        public void handleMessage(Message message)
        {
            Bundle bundle = message.getData();
            message.what;
            JVM INSTR tableswitch 1 6: default 48
        //                       1 95
        //                       2 138
        //                       3 163
        //                       4 200
        //                       5 237
        //                       6 262;
               goto _L1 _L2 _L3 _L4 _L5 _L6 _L7
_L1:
            Log.w("MediaBrowserServiceCompat", (new StringBuilder()).append("Unhandled message: ").append(message).append("\n  Service version: ").append(1).append("\n  Client version: ").append(message.arg1).toString());
_L9:
            return;
_L2:
            mServiceImpl.connect(bundle.getString("data_package_name"), bundle.getInt("data_calling_uid"), bundle.getBundle("data_root_hints"), new ServiceCallbacksCompat(message.replyTo));
            continue; /* Loop/switch isn't completed */
_L3:
            mServiceImpl.disconnect(new ServiceCallbacksCompat(message.replyTo));
            continue; /* Loop/switch isn't completed */
_L4:
            mServiceImpl.addSubscription(bundle.getString("data_media_item_id"), bundle.getBundle("data_options"), new ServiceCallbacksCompat(message.replyTo));
            continue; /* Loop/switch isn't completed */
_L5:
            mServiceImpl.removeSubscription(bundle.getString("data_media_item_id"), bundle.getBundle("data_options"), new ServiceCallbacksCompat(message.replyTo));
            continue; /* Loop/switch isn't completed */
_L6:
            mServiceImpl.getMediaItem(bundle.getString("data_media_item_id"), (ResultReceiver)bundle.getParcelable("data_result_receiver"));
            continue; /* Loop/switch isn't completed */
_L7:
            mServiceImpl.registerCallbacks(new ServiceCallbacksCompat(message.replyTo));
            if(true) goto _L9; else goto _L8
_L8:
        }

        public void postOrRun(Runnable runnable)
        {
            if(Thread.currentThread() == getLooper().getThread())
                runnable.run();
            else
                post(runnable);
        }

        public boolean sendMessageAtTime(Message message, long l)
        {
            Bundle bundle = message.getData();
            bundle.setClassLoader(android/support/v4/media/MediaBrowserCompat.getClassLoader());
            bundle.putInt("data_calling_uid", Binder.getCallingUid());
            return super.sendMessageAtTime(message, l);
        }

        private final ServiceImpl mServiceImpl;
        final MediaBrowserServiceCompat this$0;

        private ServiceHandler()
        {
            this$0 = MediaBrowserServiceCompat.this;
            super();
            mServiceImpl = new ServiceImpl();
        }

    }

    private class ServiceImpl
    {

        public void addSubscription(final String id, Bundle bundle, final ServiceCallbacks callbacks)
        {
            mHandler.postOrRun(bundle. new Runnable() {

                public void run()
                {
                    Object obj = callbacks.asBinder();
                    obj = (ConnectionRecord)mConnections.get(obj);
                    if(obj == null)
                        Log.w("MediaBrowserServiceCompat", (new StringBuilder()).append("addSubscription for callback that isn't registered id=").append(id).toString());
                    else
                        addSubscription(id, ((ConnectionRecord) (obj)), options);
                }

                final ServiceImpl this$1;
                final ServiceCallbacks val$callbacks;
                final String val$id;
                final Bundle val$options;

            
            {
                this$1 = final_serviceimpl;
                callbacks = servicecallbacks;
                id = s;
                options = Bundle.this;
                super();
            }
            }
);
        }

        public void connect(final String pkg, int i, final Bundle rootHints, final ServiceCallbacks callbacks)
        {
            if(!isValidPackage(pkg, i))
            {
                throw new IllegalArgumentException((new StringBuilder()).append("Package/uid mismatch: uid=").append(i).append(" package=").append(pkg).toString());
            } else
            {
                mHandler.postOrRun(i. new Runnable() {

                    public void run()
                    {
                        Object obj;
                        ConnectionRecord connectionrecord;
                        obj = callbacks.asBinder();
                        mConnections.remove(obj);
                        connectionrecord = new ConnectionRecord();
                        connectionrecord.pkg = pkg;
                        connectionrecord.rootHints = rootHints;
                        connectionrecord.callbacks = callbacks;
                        connectionrecord.root = onGetRoot(pkg, uid, rootHints);
                        if(connectionrecord.root != null)
                            break MISSING_BLOCK_LABEL_183;
                        Log.i("MediaBrowserServiceCompat", (new StringBuilder()).append("No root for client ").append(pkg).append(" from service ").append(getClass().getName()).toString());
                        callbacks.onConnectFailed();
_L1:
                        return;
                        obj;
                        Log.w("MediaBrowserServiceCompat", (new StringBuilder()).append("Calling onConnectFailed() failed. Ignoring. pkg=").append(pkg).toString());
                          goto _L1
                        try
                        {
                            mConnections.put(obj, connectionrecord);
                            if(mSession != null)
                                callbacks.onConnect(connectionrecord.root.getRootId(), mSession, connectionrecord.root.getExtras());
                        }
                        catch(RemoteException remoteexception)
                        {
                            Log.w("MediaBrowserServiceCompat", (new StringBuilder()).append("Calling onConnect() failed. Dropping client. pkg=").append(pkg).toString());
                            mConnections.remove(obj);
                        }
                          goto _L1
                    }

                    final ServiceImpl this$1;
                    final ServiceCallbacks val$callbacks;
                    final String val$pkg;
                    final Bundle val$rootHints;
                    final int val$uid;

            
            {
                this$1 = final_serviceimpl;
                callbacks = servicecallbacks;
                pkg = s;
                rootHints = bundle;
                uid = I.this;
                super();
            }
                }
);
                return;
            }
        }

        public void disconnect(ServiceCallbacks servicecallbacks)
        {
            mHandler.postOrRun(servicecallbacks. new Runnable() {

                public void run()
                {
                    IBinder ibinder = callbacks.asBinder();
                    if((ConnectionRecord)mConnections.remove(ibinder) == null);
                }

                final ServiceImpl this$1;
                final ServiceCallbacks val$callbacks;

            
            {
                this$1 = final_serviceimpl;
                callbacks = ServiceCallbacks.this;
                super();
            }
            }
);
        }

        public void getMediaItem(final String mediaId, ResultReceiver resultreceiver)
        {
            if(!TextUtils.isEmpty(mediaId) && resultreceiver != null)
                mHandler.postOrRun(resultreceiver. new Runnable() {

                    public void run()
                    {
                        performLoadItem(mediaId, receiver);
                    }

                    final ServiceImpl this$1;
                    final String val$mediaId;
                    final ResultReceiver val$receiver;

            
            {
                this$1 = final_serviceimpl;
                mediaId = s;
                receiver = ResultReceiver.this;
                super();
            }
                }
);
        }

        public void registerCallbacks(ServiceCallbacks servicecallbacks)
        {
            mHandler.postOrRun(servicecallbacks. new Runnable() {

                public void run()
                {
                    IBinder ibinder = callbacks.asBinder();
                    mConnections.remove(ibinder);
                    ConnectionRecord connectionrecord = new ConnectionRecord();
                    connectionrecord.callbacks = callbacks;
                    mConnections.put(ibinder, connectionrecord);
                }

                final ServiceImpl this$1;
                final ServiceCallbacks val$callbacks;

            
            {
                this$1 = final_serviceimpl;
                callbacks = ServiceCallbacks.this;
                super();
            }
            }
);
        }

        public void removeSubscription(final String id, Bundle bundle, final ServiceCallbacks callbacks)
        {
            mHandler.postOrRun(bundle. new Runnable() {

                public void run()
                {
                    Object obj;
                    obj = callbacks.asBinder();
                    obj = (ConnectionRecord)mConnections.get(obj);
                    if(obj != null) goto _L2; else goto _L1
_L1:
                    Log.w("MediaBrowserServiceCompat", (new StringBuilder()).append("removeSubscription for callback that isn't registered id=").append(id).toString());
_L4:
                    return;
_L2:
                    if(!removeSubscription(id, ((ConnectionRecord) (obj)), options))
                        Log.w("MediaBrowserServiceCompat", (new StringBuilder()).append("removeSubscription called for ").append(id).append(" which is not subscribed").toString());
                    if(true) goto _L4; else goto _L3
_L3:
                }

                final ServiceImpl this$1;
                final ServiceCallbacks val$callbacks;
                final String val$id;
                final Bundle val$options;

            
            {
                this$1 = final_serviceimpl;
                callbacks = servicecallbacks;
                id = s;
                options = Bundle.this;
                super();
            }
            }
);
        }

        final MediaBrowserServiceCompat this$0;

        private ServiceImpl()
        {
            this$0 = MediaBrowserServiceCompat.this;
            super();
        }

    }

    private class ServiceImplApi21
        implements MediaBrowserServiceCompatApi21.ServiceImplApi21
    {

        public void addSubscription(String s, MediaBrowserServiceCompatApi21.ServiceCallbacks servicecallbacks)
        {
            mServiceImpl.addSubscription(s, null, new ServiceCallbacksApi21(servicecallbacks));
        }

        public void connect(String s, Bundle bundle, MediaBrowserServiceCompatApi21.ServiceCallbacks servicecallbacks)
        {
            mServiceImpl.connect(s, Binder.getCallingUid(), bundle, new ServiceCallbacksApi21(servicecallbacks));
        }

        public void disconnect(MediaBrowserServiceCompatApi21.ServiceCallbacks servicecallbacks)
        {
            mServiceImpl.disconnect(new ServiceCallbacksApi21(servicecallbacks));
        }

        public void removeSubscription(String s, MediaBrowserServiceCompatApi21.ServiceCallbacks servicecallbacks)
        {
            mServiceImpl.removeSubscription(s, null, new ServiceCallbacksApi21(servicecallbacks));
        }

        final ServiceImpl mServiceImpl;
        final MediaBrowserServiceCompat this$0;

        ServiceImplApi21()
        {
            this$0 = MediaBrowserServiceCompat.this;
            super();
            mServiceImpl = mHandler.getServiceImpl();
        }
    }

    private class ServiceImplApi23 extends ServiceImplApi21
        implements MediaBrowserServiceCompatApi23.ServiceImplApi23
    {

        public void getMediaItem(String s, MediaBrowserServiceCompatApi23.ItemCallback itemcallback)
        {
            itemcallback = mHandler. new ResultReceiver(itemcallback) {

                protected void onReceiveResult(int i, Bundle bundle)
                {
                    MediaBrowserCompat.MediaItem mediaitem = (MediaBrowserCompat.MediaItem)bundle.getParcelable("media_item");
                    Parcel parcel = null;
                    if(mediaitem != null)
                    {
                        parcel = Parcel.obtain();
                        mediaitem.writeToParcel(parcel, 0);
                    }
                    cb.onItemLoaded(i, bundle, parcel);
                }

                final ServiceImplApi23 this$1;
                final MediaBrowserServiceCompatApi23.ItemCallback val$cb;

            
            {
                this$1 = final_serviceimplapi23;
                cb = itemcallback;
                super(Handler.this);
            }
            }
;
            mServiceImpl.getMediaItem(s, itemcallback);
        }

        final MediaBrowserServiceCompat this$0;

        private ServiceImplApi23()
        {
            this$0 = MediaBrowserServiceCompat.this;
            super();
        }

    }


    public MediaBrowserServiceCompat()
    {
    }

    private void addSubscription(String s, ConnectionRecord connectionrecord, Bundle bundle)
    {
        Object obj;
        Object obj1;
        obj = (List)connectionrecord.subscriptions.get(s);
        obj1 = obj;
        if(obj == null)
            obj1 = new ArrayList();
        obj = ((List) (obj1)).iterator();
_L4:
        if(!((Iterator) (obj)).hasNext()) goto _L2; else goto _L1
_L1:
        if(!MediaBrowserCompatUtils.areSameOptions(bundle, (Bundle)((Iterator) (obj)).next())) goto _L4; else goto _L3
_L3:
        return;
_L2:
        ((List) (obj1)).add(bundle);
        connectionrecord.subscriptions.put(s, obj1);
        performLoadChildren(s, connectionrecord, bundle);
        if(true) goto _L3; else goto _L5
_L5:
    }

    private List applyOptions(List list, Bundle bundle)
    {
        int i = bundle.getInt("android.media.browse.extra.PAGE", -1);
        int j = bundle.getInt("android.media.browse.extra.PAGE_SIZE", -1);
        if(i != -1 || j != -1)
        {
            int l = j * (i - 1);
            int i1 = l + j;
            if(i < 1 || j < 1 || l >= list.size())
            {
                list = Collections.emptyList();
            } else
            {
                int k = i1;
                if(i1 > list.size())
                    k = list.size();
                list = list.subList(l, k);
            }
        }
        return list;
    }

    private boolean isValidPackage(String s, int i)
    {
        boolean flag = false;
        if(s != null) goto _L2; else goto _L1
_L1:
        boolean flag1 = flag;
_L4:
        return flag1;
_L2:
        String as[] = getPackageManager().getPackagesForUid(i);
        int j = as.length;
        i = 0;
        do
        {
            flag1 = flag;
            if(i < j)
            {
label0:
                {
                    if(!as[i].equals(s))
                        break label0;
                    flag1 = true;
                }
            }
            if(true)
                continue;
            i++;
        } while(true);
        if(true) goto _L4; else goto _L3
_L3:
    }

    private void notifyChildrenChangedInternal(final String parentId, final Bundle options)
    {
        if(parentId == null)
        {
            throw new IllegalArgumentException("parentId cannot be null in notifyChildrenChanged");
        } else
        {
            mHandler.post(new Runnable() {

                public void run()
                {
                    Iterator iterator = mConnections.keySet().iterator();
label0:
                    do
                    {
                        if(!iterator.hasNext())
                            break;
                        Object obj = (IBinder)iterator.next();
                        obj = (ConnectionRecord)mConnections.get(obj);
                        Object obj1 = (List)((ConnectionRecord) (obj)).subscriptions.get(parentId);
                        if(obj1 == null)
                            continue;
                        Iterator iterator1 = ((List) (obj1)).iterator();
                        do
                        {
                            if(!iterator1.hasNext())
                                continue label0;
                            obj1 = (Bundle)iterator1.next();
                        } while(!MediaBrowserCompatUtils.hasDuplicatedItems(options, ((Bundle) (obj1))));
                        performLoadChildren(parentId, ((ConnectionRecord) (obj)), ((Bundle) (obj1)));
                    } while(true);
                }

                final MediaBrowserServiceCompat this$0;
                final Bundle val$options;
                final String val$parentId;

            
            {
                this$0 = MediaBrowserServiceCompat.this;
                parentId = s;
                options = bundle;
                super();
            }
            }
);
            return;
        }
    }

    private void performLoadChildren(final String final_obj, final ConnectionRecord connection, Bundle bundle)
    {
        Result result = new Result(bundle) {

            volatile void onResultSent(Object obj, int i)
            {
                onResultSent((List)obj, i);
            }

            void onResultSent(List list, int i)
            {
                if(mConnections.get(connection.callbacks.asBinder()) == connection)
                {
                    if((i & 1) != 0)
                        list = MediaBrowserCompatUtils.applyOptions(list, options);
                    try
                    {
                        connection.callbacks.onLoadChildren(parentId, list, options);
                    }
                    // Misplaced declaration of an exception variable
                    catch(List list)
                    {
                        Log.w("MediaBrowserServiceCompat", (new StringBuilder()).append("Calling onLoadChildren() failed for id=").append(parentId).append(" package=").append(connection.pkg).toString());
                    }
                }
            }

            final MediaBrowserServiceCompat this$0;
            final ConnectionRecord val$connection;
            final Bundle val$options;
            final String val$parentId;

            
            {
                this$0 = MediaBrowserServiceCompat.this;
                connection = connectionrecord;
                parentId = s;
                options = bundle;
                super(final_obj);
            }
        }
;
        if(bundle == null)
            onLoadChildren(final_obj, result);
        else
            onLoadChildren(final_obj, result, bundle);
        if(!result.isDone())
            throw new IllegalStateException((new StringBuilder()).append("onLoadChildren must call detach() or sendResult() before returning for package=").append(connection.pkg).append(" id=").append(final_obj).toString());
        else
            return;
    }

    private void performLoadItem(final String final_obj, ResultReceiver resultreceiver)
    {
        resultreceiver = new Result(resultreceiver) {

            void onResultSent(MediaBrowserCompat.MediaItem mediaitem, int i)
            {
                Bundle bundle = new Bundle();
                bundle.putParcelable("media_item", mediaitem);
                receiver.send(0, bundle);
            }

            volatile void onResultSent(Object obj, int i)
            {
                onResultSent((MediaBrowserCompat.MediaItem)obj, i);
            }

            final MediaBrowserServiceCompat this$0;
            final ResultReceiver val$receiver;

            
            {
                this$0 = MediaBrowserServiceCompat.this;
                receiver = resultreceiver;
                super(final_obj);
            }
        }
;
        onLoadItem(final_obj, resultreceiver);
        if(!resultreceiver.isDone())
            throw new IllegalStateException((new StringBuilder()).append("onLoadItem must call detach() or sendResult() before returning for id=").append(final_obj).toString());
        else
            return;
    }

    private boolean removeSubscription(String s, ConnectionRecord connectionrecord, Bundle bundle)
    {
        boolean flag = false;
        boolean flag1 = false;
        List list = (List)connectionrecord.subscriptions.get(s);
        if(list != null)
        {
            Iterator iterator = list.iterator();
            boolean flag2;
            do
            {
                flag2 = flag1;
                if(!iterator.hasNext())
                    break;
                Bundle bundle1 = (Bundle)iterator.next();
                if(!MediaBrowserCompatUtils.areSameOptions(bundle, bundle1))
                    continue;
                flag2 = true;
                list.remove(bundle1);
                break;
            } while(true);
            flag = flag2;
            if(list.size() == 0)
            {
                connectionrecord.subscriptions.remove(s);
                flag = flag2;
            }
        }
        return flag;
    }

    public void dump(FileDescriptor filedescriptor, PrintWriter printwriter, String as[])
    {
    }

    public android.support.v4.media.session.MediaSessionCompat.Token getSessionToken()
    {
        return mSession;
    }

    public void notifyChildrenChanged(String s)
    {
        notifyChildrenChangedInternal(s, null);
    }

    public void notifyChildrenChanged(String s, Bundle bundle)
    {
        if(bundle == null)
        {
            throw new IllegalArgumentException("options cannot be null in notifyChildrenChanged");
        } else
        {
            notifyChildrenChangedInternal(s, bundle);
            return;
        }
    }

    public IBinder onBind(Intent intent)
    {
        return mImpl.onBind(intent);
    }

    public void onCreate()
    {
        super.onCreate();
        if(android.os.Build.VERSION.SDK_INT >= 23)
            mImpl = new MediaBrowserServiceImplApi23();
        else
        if(android.os.Build.VERSION.SDK_INT >= 21)
            mImpl = new MediaBrowserServiceImplApi21();
        else
            mImpl = new MediaBrowserServiceImplBase();
        mImpl.onCreate();
    }

    public abstract BrowserRoot onGetRoot(String s, int i, Bundle bundle);

    public abstract void onLoadChildren(String s, Result result);

    public void onLoadChildren(String s, Result result, Bundle bundle)
    {
        result.setFlags(1);
        onLoadChildren(s, result);
    }

    public void onLoadItem(String s, Result result)
    {
        result.sendResult(null);
    }

    public void setSessionToken(final android.support.v4.media.session.MediaSessionCompat.Token token)
    {
        if(token == null)
            throw new IllegalArgumentException("Session token may not be null.");
        if(mSession != null)
        {
            throw new IllegalStateException("The session token has already been set.");
        } else
        {
            mSession = token;
            mHandler.post(new Runnable() {

                public void run()
                {
                    for(Iterator iterator = mConnections.keySet().iterator(); iterator.hasNext();)
                    {
                        IBinder ibinder = (IBinder)iterator.next();
                        ConnectionRecord connectionrecord = (ConnectionRecord)mConnections.get(ibinder);
                        try
                        {
                            connectionrecord.callbacks.onConnect(connectionrecord.root.getRootId(), token, connectionrecord.root.getExtras());
                        }
                        catch(RemoteException remoteexception)
                        {
                            Log.w("MediaBrowserServiceCompat", (new StringBuilder()).append("Connection for ").append(connectionrecord.pkg).append(" is no longer valid.").toString());
                            mConnections.remove(ibinder);
                        }
                    }

                }

                final MediaBrowserServiceCompat this$0;
                final android.support.v4.media.session.MediaSessionCompat.Token val$token;

            
            {
                this$0 = MediaBrowserServiceCompat.this;
                token = token1;
                super();
            }
            }
);
            return;
        }
    }

    private static final boolean DBG = false;
    public static final String KEY_MEDIA_ITEM = "media_item";
    private static final int RESULT_FLAG_OPTION_NOT_HANDLED = 1;
    public static final String SERVICE_INTERFACE = "android.media.browse.MediaBrowserService";
    private static final String TAG = "MediaBrowserServiceCompat";
    private final ArrayMap mConnections = new ArrayMap();
    private final ServiceHandler mHandler = new ServiceHandler();
    private MediaBrowserServiceImpl mImpl;
    android.support.v4.media.session.MediaSessionCompat.Token mSession;







}
