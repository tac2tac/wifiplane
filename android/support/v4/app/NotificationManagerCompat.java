// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.util.Log;
import java.util.*;

// Referenced classes of package android.support.v4.app:
//            NotificationCompat, INotificationSideChannel, NotificationManagerCompatEclair

public final class NotificationManagerCompat
{
    private static class CancelTask
        implements Task
    {

        public void send(INotificationSideChannel inotificationsidechannel)
            throws RemoteException
        {
            if(all)
                inotificationsidechannel.cancelAll(packageName);
            else
                inotificationsidechannel.cancel(packageName, id, tag);
        }

        public String toString()
        {
            StringBuilder stringbuilder = new StringBuilder("CancelTask[");
            stringbuilder.append("packageName:").append(packageName);
            stringbuilder.append(", id:").append(id);
            stringbuilder.append(", tag:").append(tag);
            stringbuilder.append(", all:").append(all);
            stringbuilder.append("]");
            return stringbuilder.toString();
        }

        final boolean all;
        final int id;
        final String packageName;
        final String tag;

        public CancelTask(String s)
        {
            packageName = s;
            id = 0;
            tag = null;
            all = true;
        }

        public CancelTask(String s, int i, String s1)
        {
            packageName = s;
            id = i;
            tag = s1;
            all = false;
        }
    }

    static interface Impl
    {

        public abstract void cancelNotification(NotificationManager notificationmanager, String s, int i);

        public abstract int getSideChannelBindFlags();

        public abstract void postNotification(NotificationManager notificationmanager, String s, int i, Notification notification);
    }

    static class ImplBase
        implements Impl
    {

        public void cancelNotification(NotificationManager notificationmanager, String s, int i)
        {
            notificationmanager.cancel(i);
        }

        public int getSideChannelBindFlags()
        {
            return 1;
        }

        public void postNotification(NotificationManager notificationmanager, String s, int i, Notification notification)
        {
            notificationmanager.notify(i, notification);
        }

        ImplBase()
        {
        }
    }

    static class ImplEclair extends ImplBase
    {

        public void cancelNotification(NotificationManager notificationmanager, String s, int i)
        {
            NotificationManagerCompatEclair.cancelNotification(notificationmanager, s, i);
        }

        public void postNotification(NotificationManager notificationmanager, String s, int i, Notification notification)
        {
            NotificationManagerCompatEclair.postNotification(notificationmanager, s, i, notification);
        }

        ImplEclair()
        {
        }
    }

    static class ImplIceCreamSandwich extends ImplEclair
    {

        public int getSideChannelBindFlags()
        {
            return 33;
        }

        ImplIceCreamSandwich()
        {
        }
    }

    private static class NotifyTask
        implements Task
    {

        public void send(INotificationSideChannel inotificationsidechannel)
            throws RemoteException
        {
            inotificationsidechannel.notify(packageName, id, tag, notif);
        }

        public String toString()
        {
            StringBuilder stringbuilder = new StringBuilder("NotifyTask[");
            stringbuilder.append("packageName:").append(packageName);
            stringbuilder.append(", id:").append(id);
            stringbuilder.append(", tag:").append(tag);
            stringbuilder.append("]");
            return stringbuilder.toString();
        }

        final int id;
        final Notification notif;
        final String packageName;
        final String tag;

        public NotifyTask(String s, int i, String s1, Notification notification)
        {
            packageName = s;
            id = i;
            tag = s1;
            notif = notification;
        }
    }

    private static class ServiceConnectedEvent
    {

        final ComponentName componentName;
        final IBinder iBinder;

        public ServiceConnectedEvent(ComponentName componentname, IBinder ibinder)
        {
            componentName = componentname;
            iBinder = ibinder;
        }
    }

    private static class SideChannelManager
        implements android.os.Handler.Callback, ServiceConnection
    {

        private boolean ensureServiceBound(ListenerRecord listenerrecord)
        {
            boolean flag;
            if(listenerrecord.bound)
            {
                flag = true;
            } else
            {
                Intent intent = (new Intent("android.support.BIND_NOTIFICATION_SIDE_CHANNEL")).setComponent(listenerrecord.componentName);
                listenerrecord.bound = mContext.bindService(intent, this, NotificationManagerCompat.SIDE_CHANNEL_BIND_FLAGS);
                if(listenerrecord.bound)
                {
                    listenerrecord.retryCount = 0;
                } else
                {
                    Log.w("NotifManCompat", (new StringBuilder()).append("Unable to bind to listener ").append(listenerrecord.componentName).toString());
                    mContext.unbindService(this);
                }
                flag = listenerrecord.bound;
            }
            return flag;
        }

        private void ensureServiceUnbound(ListenerRecord listenerrecord)
        {
            if(listenerrecord.bound)
            {
                mContext.unbindService(this);
                listenerrecord.bound = false;
            }
            listenerrecord.service = null;
        }

        private void handleQueueTask(Task task)
        {
            updateListenerMap();
            ListenerRecord listenerrecord;
            for(Iterator iterator = mRecordMap.values().iterator(); iterator.hasNext(); processListenerQueue(listenerrecord))
            {
                listenerrecord = (ListenerRecord)iterator.next();
                listenerrecord.taskQueue.add(task);
            }

        }

        private void handleRetryListenerQueue(ComponentName componentname)
        {
            componentname = (ListenerRecord)mRecordMap.get(componentname);
            if(componentname != null)
                processListenerQueue(componentname);
        }

        private void handleServiceConnected(ComponentName componentname, IBinder ibinder)
        {
            componentname = (ListenerRecord)mRecordMap.get(componentname);
            if(componentname != null)
            {
                componentname.service = INotificationSideChannel.Stub.asInterface(ibinder);
                componentname.retryCount = 0;
                processListenerQueue(componentname);
            }
        }

        private void handleServiceDisconnected(ComponentName componentname)
        {
            componentname = (ListenerRecord)mRecordMap.get(componentname);
            if(componentname != null)
                ensureServiceUnbound(componentname);
        }

        private void processListenerQueue(ListenerRecord listenerrecord)
        {
            if(Log.isLoggable("NotifManCompat", 3))
                Log.d("NotifManCompat", (new StringBuilder()).append("Processing component ").append(listenerrecord.componentName).append(", ").append(listenerrecord.taskQueue.size()).append(" queued tasks").toString());
            if(!listenerrecord.taskQueue.isEmpty()) goto _L2; else goto _L1
_L1:
            return;
_L2:
            Task task;
            if(!ensureServiceBound(listenerrecord) || listenerrecord.service == null)
            {
                scheduleListenerRetry(listenerrecord);
                continue; /* Loop/switch isn't completed */
            }
_L5:
            task = (Task)listenerrecord.taskQueue.peek();
            if(task != null) goto _L4; else goto _L3
_L4:
            if(Log.isLoggable("NotifManCompat", 3))
            {
                StringBuilder stringbuilder = JVM INSTR new #116 <Class StringBuilder>;
                stringbuilder.StringBuilder();
                Log.d("NotifManCompat", stringbuilder.append("Sending task ").append(task).toString());
            }
            task.send(listenerrecord.service);
            listenerrecord.taskQueue.remove();
              goto _L5
_L3:
            if(!listenerrecord.taskQueue.isEmpty())
                scheduleListenerRetry(listenerrecord);
            if(true) goto _L1; else goto _L6
_L6:
            Object obj;
            obj;
            if(Log.isLoggable("NotifManCompat", 3))
                Log.d("NotifManCompat", (new StringBuilder()).append("Remote service has died: ").append(listenerrecord.componentName).toString());
              goto _L3
            obj;
            Log.w("NotifManCompat", (new StringBuilder()).append("RemoteException communicating with ").append(listenerrecord.componentName).toString(), ((Throwable) (obj)));
              goto _L3
        }

        private void scheduleListenerRetry(ListenerRecord listenerrecord)
        {
            if(!mHandler.hasMessages(3, listenerrecord.componentName))
            {
                listenerrecord.retryCount = listenerrecord.retryCount + 1;
                if(listenerrecord.retryCount > 6)
                {
                    Log.w("NotifManCompat", (new StringBuilder()).append("Giving up on delivering ").append(listenerrecord.taskQueue.size()).append(" tasks to ").append(listenerrecord.componentName).append(" after ").append(listenerrecord.retryCount).append(" retries").toString());
                    listenerrecord.taskQueue.clear();
                } else
                {
                    int i = (1 << listenerrecord.retryCount - 1) * 1000;
                    if(Log.isLoggable("NotifManCompat", 3))
                        Log.d("NotifManCompat", (new StringBuilder()).append("Scheduling retry for ").append(i).append(" ms").toString());
                    listenerrecord = mHandler.obtainMessage(3, listenerrecord.componentName);
                    mHandler.sendMessageDelayed(listenerrecord, i);
                }
            }
        }

        private void updateListenerMap()
        {
            Object obj = NotificationManagerCompat.getEnabledListenerPackages(mContext);
            if(!((Set) (obj)).equals(mCachedEnabledPackages))
            {
                mCachedEnabledPackages = ((Set) (obj));
                Object obj1 = mContext.getPackageManager().queryIntentServices((new Intent()).setAction("android.support.BIND_NOTIFICATION_SIDE_CHANNEL"), 4);
                HashSet hashset = new HashSet();
                Iterator iterator = ((List) (obj1)).iterator();
                do
                {
                    if(!iterator.hasNext())
                        break;
                    ResolveInfo resolveinfo = (ResolveInfo)iterator.next();
                    if(((Set) (obj)).contains(resolveinfo.serviceInfo.packageName))
                    {
                        obj1 = new ComponentName(resolveinfo.serviceInfo.packageName, resolveinfo.serviceInfo.name);
                        if(resolveinfo.serviceInfo.permission != null)
                            Log.w("NotifManCompat", (new StringBuilder()).append("Permission present on component ").append(obj1).append(", not adding listener record.").toString());
                        else
                            hashset.add(obj1);
                    }
                } while(true);
                obj1 = hashset.iterator();
                do
                {
                    if(!((Iterator) (obj1)).hasNext())
                        break;
                    obj = (ComponentName)((Iterator) (obj1)).next();
                    if(!mRecordMap.containsKey(obj))
                    {
                        if(Log.isLoggable("NotifManCompat", 3))
                            Log.d("NotifManCompat", (new StringBuilder()).append("Adding listener record for ").append(obj).toString());
                        mRecordMap.put(obj, new ListenerRecord(((ComponentName) (obj))));
                    }
                } while(true);
                obj = mRecordMap.entrySet().iterator();
                while(((Iterator) (obj)).hasNext()) 
                {
                    java.util.Map.Entry entry = (java.util.Map.Entry)((Iterator) (obj)).next();
                    if(!hashset.contains(entry.getKey()))
                    {
                        if(Log.isLoggable("NotifManCompat", 3))
                            Log.d("NotifManCompat", (new StringBuilder()).append("Removing listener record for ").append(entry.getKey()).toString());
                        ensureServiceUnbound((ListenerRecord)entry.getValue());
                        ((Iterator) (obj)).remove();
                    }
                }
            }
        }

        public boolean handleMessage(Message message)
        {
            message.what;
            JVM INSTR tableswitch 0 3: default 36
        //                       0 40
        //                       1 56
        //                       2 81
        //                       3 97;
               goto _L1 _L2 _L3 _L4 _L5
_L1:
            boolean flag = false;
_L7:
            return flag;
_L2:
            handleQueueTask((Task)message.obj);
            flag = true;
            continue; /* Loop/switch isn't completed */
_L3:
            message = (ServiceConnectedEvent)message.obj;
            handleServiceConnected(((ServiceConnectedEvent) (message)).componentName, ((ServiceConnectedEvent) (message)).iBinder);
            flag = true;
            continue; /* Loop/switch isn't completed */
_L4:
            handleServiceDisconnected((ComponentName)message.obj);
            flag = true;
            continue; /* Loop/switch isn't completed */
_L5:
            handleRetryListenerQueue((ComponentName)message.obj);
            flag = true;
            if(true) goto _L7; else goto _L6
_L6:
        }

        public void onServiceConnected(ComponentName componentname, IBinder ibinder)
        {
            if(Log.isLoggable("NotifManCompat", 3))
                Log.d("NotifManCompat", (new StringBuilder()).append("Connected to service ").append(componentname).toString());
            mHandler.obtainMessage(1, new ServiceConnectedEvent(componentname, ibinder)).sendToTarget();
        }

        public void onServiceDisconnected(ComponentName componentname)
        {
            if(Log.isLoggable("NotifManCompat", 3))
                Log.d("NotifManCompat", (new StringBuilder()).append("Disconnected from service ").append(componentname).toString());
            mHandler.obtainMessage(2, componentname).sendToTarget();
        }

        public void queueTask(Task task)
        {
            mHandler.obtainMessage(0, task).sendToTarget();
        }

        private static final String KEY_BINDER = "binder";
        private static final int MSG_QUEUE_TASK = 0;
        private static final int MSG_RETRY_LISTENER_QUEUE = 3;
        private static final int MSG_SERVICE_CONNECTED = 1;
        private static final int MSG_SERVICE_DISCONNECTED = 2;
        private Set mCachedEnabledPackages;
        private final Context mContext;
        private final Handler mHandler;
        private final HandlerThread mHandlerThread = new HandlerThread("NotificationManagerCompat");
        private final Map mRecordMap = new HashMap();

        public SideChannelManager(Context context)
        {
            mCachedEnabledPackages = new HashSet();
            mContext = context;
            mHandlerThread.start();
            mHandler = new Handler(mHandlerThread.getLooper(), this);
        }
    }

    private static class SideChannelManager.ListenerRecord
    {

        public boolean bound;
        public final ComponentName componentName;
        public int retryCount;
        public INotificationSideChannel service;
        public LinkedList taskQueue;

        public SideChannelManager.ListenerRecord(ComponentName componentname)
        {
            bound = false;
            taskQueue = new LinkedList();
            retryCount = 0;
            componentName = componentname;
        }
    }

    private static interface Task
    {

        public abstract void send(INotificationSideChannel inotificationsidechannel)
            throws RemoteException;
    }


    private NotificationManagerCompat(Context context)
    {
        mContext = context;
        mNotificationManager = (NotificationManager)mContext.getSystemService("notification");
    }

    public static NotificationManagerCompat from(Context context)
    {
        return new NotificationManagerCompat(context);
    }

    public static Set getEnabledListenerPackages(Context context)
    {
        context = android.provider.Settings.Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
        if(context != null && !context.equals(sEnabledNotificationListeners))
        {
            String as[] = context.split(":");
            HashSet hashset = new HashSet(as.length);
            int i = as.length;
            for(int j = 0; j < i; j++)
            {
                ComponentName componentname = ComponentName.unflattenFromString(as[j]);
                if(componentname != null)
                    hashset.add(componentname.getPackageName());
            }

            synchronized(sEnabledNotificationListenersLock)
            {
                sEnabledNotificationListenerPackages = hashset;
                sEnabledNotificationListeners = context;
            }
        }
        return sEnabledNotificationListenerPackages;
        context;
        obj;
        JVM INSTR monitorexit ;
        throw context;
    }

    private void pushSideChannelQueue(Task task)
    {
        synchronized(sLock)
        {
            if(sSideChannelManager == null)
            {
                SideChannelManager sidechannelmanager = JVM INSTR new #27  <Class NotificationManagerCompat$SideChannelManager>;
                sidechannelmanager.SideChannelManager(mContext.getApplicationContext());
                sSideChannelManager = sidechannelmanager;
            }
        }
        sSideChannelManager.queueTask(task);
        return;
        task;
        obj;
        JVM INSTR monitorexit ;
        throw task;
    }

    private static boolean useSideChannelForNotification(Notification notification)
    {
        notification = NotificationCompat.getExtras(notification);
        boolean flag;
        if(notification != null && notification.getBoolean("android.support.useSideChannel"))
            flag = true;
        else
            flag = false;
        return flag;
    }

    public void cancel(int i)
    {
        cancel(null, i);
    }

    public void cancel(String s, int i)
    {
        IMPL.cancelNotification(mNotificationManager, s, i);
        if(android.os.Build.VERSION.SDK_INT <= 19)
            pushSideChannelQueue(new CancelTask(mContext.getPackageName(), i, s));
    }

    public void cancelAll()
    {
        mNotificationManager.cancelAll();
        if(android.os.Build.VERSION.SDK_INT <= 19)
            pushSideChannelQueue(new CancelTask(mContext.getPackageName()));
    }

    public void notify(int i, Notification notification)
    {
        notify(null, i, notification);
    }

    public void notify(String s, int i, Notification notification)
    {
        if(useSideChannelForNotification(notification))
        {
            pushSideChannelQueue(new NotifyTask(mContext.getPackageName(), i, s, notification));
            IMPL.cancelNotification(mNotificationManager, s, i);
        } else
        {
            IMPL.postNotification(mNotificationManager, s, i, notification);
        }
    }

    public static final String ACTION_BIND_SIDE_CHANNEL = "android.support.BIND_NOTIFICATION_SIDE_CHANNEL";
    public static final String EXTRA_USE_SIDE_CHANNEL = "android.support.useSideChannel";
    private static final Impl IMPL;
    static final int MAX_SIDE_CHANNEL_SDK_VERSION = 19;
    private static final String SETTING_ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final int SIDE_CHANNEL_BIND_FLAGS;
    private static final int SIDE_CHANNEL_RETRY_BASE_INTERVAL_MS = 1000;
    private static final int SIDE_CHANNEL_RETRY_MAX_COUNT = 6;
    private static final String TAG = "NotifManCompat";
    private static Set sEnabledNotificationListenerPackages = new HashSet();
    private static String sEnabledNotificationListeners;
    private static final Object sEnabledNotificationListenersLock = new Object();
    private static final Object sLock = new Object();
    private static SideChannelManager sSideChannelManager;
    private final Context mContext;
    private final NotificationManager mNotificationManager;

    static 
    {
        if(android.os.Build.VERSION.SDK_INT >= 14)
            IMPL = new ImplIceCreamSandwich();
        else
        if(android.os.Build.VERSION.SDK_INT >= 5)
            IMPL = new ImplEclair();
        else
            IMPL = new ImplBase();
        SIDE_CHANNEL_BIND_FLAGS = IMPL.getSideChannelBindFlags();
    }

}
