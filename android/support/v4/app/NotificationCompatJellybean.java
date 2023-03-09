// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.widget.RemoteViews;
import java.lang.reflect.Field;
import java.util.*;

// Referenced classes of package android.support.v4.app:
//            NotificationBuilderWithBuilderAccessor, BundleUtil, RemoteInputCompatJellybean, NotificationBuilderWithActions

class NotificationCompatJellybean
{
    public static class Builder
        implements NotificationBuilderWithBuilderAccessor, NotificationBuilderWithActions
    {

        public void addAction(NotificationCompatBase.Action action)
        {
            mActionExtrasList.add(NotificationCompatJellybean.writeActionAndGetExtras(b, action));
        }

        public Notification build()
        {
            Notification notification = b.build();
            Bundle bundle = NotificationCompatJellybean.getExtras(notification);
            Bundle bundle1 = new Bundle(mExtras);
            Object obj = mExtras.keySet().iterator();
            do
            {
                if(!((Iterator) (obj)).hasNext())
                    break;
                String s = (String)((Iterator) (obj)).next();
                if(bundle.containsKey(s))
                    bundle1.remove(s);
            } while(true);
            bundle.putAll(bundle1);
            obj = NotificationCompatJellybean.buildActionExtrasMap(mActionExtrasList);
            if(obj != null)
                NotificationCompatJellybean.getExtras(notification).putSparseParcelableArray("android.support.actionExtras", ((SparseArray) (obj)));
            return notification;
        }

        public android.app.Notification.Builder getBuilder()
        {
            return b;
        }

        private android.app.Notification.Builder b;
        private List mActionExtrasList;
        private final Bundle mExtras = new Bundle();

        public Builder(Context context, Notification notification, CharSequence charsequence, CharSequence charsequence1, CharSequence charsequence2, RemoteViews remoteviews, int i, 
                PendingIntent pendingintent, PendingIntent pendingintent1, Bitmap bitmap, int j, int k, boolean flag, boolean flag1, 
                int l, CharSequence charsequence3, boolean flag2, Bundle bundle, String s, boolean flag3, String s1)
        {
            mActionExtrasList = new ArrayList();
            context = (new android.app.Notification.Builder(context)).setWhen(notification.when).setSmallIcon(notification.icon, notification.iconLevel).setContent(notification.contentView).setTicker(notification.tickerText, remoteviews).setSound(notification.sound, notification.audioStreamType).setVibrate(notification.vibrate).setLights(notification.ledARGB, notification.ledOnMS, notification.ledOffMS);
            boolean flag4;
            if((notification.flags & 2) != 0)
                flag4 = true;
            else
                flag4 = false;
            context = context.setOngoing(flag4);
            if((notification.flags & 8) != 0)
                flag4 = true;
            else
                flag4 = false;
            context = context.setOnlyAlertOnce(flag4);
            if((notification.flags & 0x10) != 0)
                flag4 = true;
            else
                flag4 = false;
            context = context.setAutoCancel(flag4).setDefaults(notification.defaults).setContentTitle(charsequence).setContentText(charsequence1).setSubText(charsequence3).setContentInfo(charsequence2).setContentIntent(pendingintent).setDeleteIntent(notification.deleteIntent);
            if((notification.flags & 0x80) != 0)
                flag4 = true;
            else
                flag4 = false;
            b = context.setFullScreenIntent(pendingintent1, flag4).setLargeIcon(bitmap).setNumber(i).setUsesChronometer(flag1).setPriority(l).setProgress(j, k, flag);
            if(bundle != null)
                mExtras.putAll(bundle);
            if(flag2)
                mExtras.putBoolean("android.support.localOnly", true);
            if(s != null)
            {
                mExtras.putString("android.support.groupKey", s);
                if(flag3)
                    mExtras.putBoolean("android.support.isGroupSummary", true);
                else
                    mExtras.putBoolean("android.support.useSideChannel", true);
            }
            if(s1 != null)
                mExtras.putString("android.support.sortKey", s1);
        }
    }


    NotificationCompatJellybean()
    {
    }

    public static void addBigPictureStyle(NotificationBuilderWithBuilderAccessor notificationbuilderwithbuilderaccessor, CharSequence charsequence, boolean flag, CharSequence charsequence1, Bitmap bitmap, Bitmap bitmap1, boolean flag1)
    {
        notificationbuilderwithbuilderaccessor = (new android.app.Notification.BigPictureStyle(notificationbuilderwithbuilderaccessor.getBuilder())).setBigContentTitle(charsequence).bigPicture(bitmap);
        if(flag1)
            notificationbuilderwithbuilderaccessor.bigLargeIcon(bitmap1);
        if(flag)
            notificationbuilderwithbuilderaccessor.setSummaryText(charsequence1);
    }

    public static void addBigTextStyle(NotificationBuilderWithBuilderAccessor notificationbuilderwithbuilderaccessor, CharSequence charsequence, boolean flag, CharSequence charsequence1, CharSequence charsequence2)
    {
        notificationbuilderwithbuilderaccessor = (new android.app.Notification.BigTextStyle(notificationbuilderwithbuilderaccessor.getBuilder())).setBigContentTitle(charsequence).bigText(charsequence2);
        if(flag)
            notificationbuilderwithbuilderaccessor.setSummaryText(charsequence1);
    }

    public static void addInboxStyle(NotificationBuilderWithBuilderAccessor notificationbuilderwithbuilderaccessor, CharSequence charsequence, boolean flag, CharSequence charsequence1, ArrayList arraylist)
    {
        notificationbuilderwithbuilderaccessor = (new android.app.Notification.InboxStyle(notificationbuilderwithbuilderaccessor.getBuilder())).setBigContentTitle(charsequence);
        if(flag)
            notificationbuilderwithbuilderaccessor.setSummaryText(charsequence1);
        for(charsequence = arraylist.iterator(); charsequence.hasNext(); notificationbuilderwithbuilderaccessor.addLine((CharSequence)charsequence.next()));
    }

    public static SparseArray buildActionExtrasMap(List list)
    {
        SparseArray sparsearray = null;
        int i = 0;
        for(int j = list.size(); i < j;)
        {
            Bundle bundle = (Bundle)list.get(i);
            SparseArray sparsearray1 = sparsearray;
            if(bundle != null)
            {
                sparsearray1 = sparsearray;
                if(sparsearray == null)
                    sparsearray1 = new SparseArray();
                sparsearray1.put(i, bundle);
            }
            i++;
            sparsearray = sparsearray1;
        }

        return sparsearray;
    }

    private static boolean ensureActionReflectionReadyLocked()
    {
        boolean flag;
        boolean flag1;
        flag = false;
        flag1 = true;
        if(!sActionsAccessFailed) goto _L2; else goto _L1
_L1:
        flag1 = flag;
_L4:
        return flag1;
_L2:
        try
        {
            if(sActionsField == null)
            {
                sActionClass = Class.forName("android.app.Notification$Action");
                sActionIconField = sActionClass.getDeclaredField("icon");
                sActionTitleField = sActionClass.getDeclaredField("title");
                sActionIntentField = sActionClass.getDeclaredField("actionIntent");
                sActionsField = android/app/Notification.getDeclaredField("actions");
                sActionsField.setAccessible(true);
            }
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            Log.e("NotificationCompat", "Unable to access notification actions", classnotfoundexception);
            sActionsAccessFailed = true;
        }
        catch(NoSuchFieldException nosuchfieldexception)
        {
            Log.e("NotificationCompat", "Unable to access notification actions", nosuchfieldexception);
            sActionsAccessFailed = true;
        }
        if(sActionsAccessFailed)
            flag1 = false;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public static NotificationCompatBase.Action getAction(Notification notification, int i, NotificationCompatBase.Action.Factory factory, RemoteInputCompatBase.RemoteInput.Factory factory1)
    {
        Object obj = sActionsLock;
        obj;
        JVM INSTR monitorenter ;
        Object obj1 = getActionObjectsLocked(notification)[i];
        Object obj2 = null;
        Object obj3 = getExtras(notification);
        notification = obj2;
        if(obj3 == null)
            break MISSING_BLOCK_LABEL_60;
        obj3 = ((Bundle) (obj3)).getSparseParcelableArray("android.support.actionExtras");
        notification = obj2;
        if(obj3 == null)
            break MISSING_BLOCK_LABEL_60;
        notification = (Bundle)((SparseArray) (obj3)).get(i);
        notification = readAction(factory, factory1, sActionIconField.getInt(obj1), (CharSequence)sActionTitleField.get(obj1), (PendingIntent)sActionIntentField.get(obj1), notification);
        obj;
        JVM INSTR monitorexit ;
_L2:
        return notification;
        notification;
        Log.e("NotificationCompat", "Unable to access notification actions", notification);
        sActionsAccessFailed = true;
        obj;
        JVM INSTR monitorexit ;
        notification = null;
        if(true) goto _L2; else goto _L1
_L1:
        notification;
        obj;
        JVM INSTR monitorexit ;
        throw notification;
    }

    public static int getActionCount(Notification notification)
    {
        Object obj = sActionsLock;
        obj;
        JVM INSTR monitorenter ;
        notification = ((Notification) (getActionObjectsLocked(notification)));
        if(notification == null)
            break MISSING_BLOCK_LABEL_22;
        int i = notification.length;
_L1:
        obj;
        JVM INSTR monitorexit ;
        return i;
        i = 0;
          goto _L1
        notification;
        obj;
        JVM INSTR monitorexit ;
        throw notification;
    }

    private static NotificationCompatBase.Action getActionFromBundle(Bundle bundle, NotificationCompatBase.Action.Factory factory, RemoteInputCompatBase.RemoteInput.Factory factory1)
    {
        return factory.build(bundle.getInt("icon"), bundle.getCharSequence("title"), (PendingIntent)bundle.getParcelable("actionIntent"), bundle.getBundle("extras"), RemoteInputCompatJellybean.fromBundleArray(BundleUtil.getBundleArrayFromBundle(bundle, "remoteInputs"), factory1));
    }

    private static Object[] getActionObjectsLocked(Notification notification)
    {
        Object obj = sActionsLock;
        obj;
        JVM INSTR monitorenter ;
        if(ensureActionReflectionReadyLocked()) goto _L2; else goto _L1
_L1:
        notification = null;
_L4:
        return notification;
_L2:
        notification = ((Notification) ((Object[])sActionsField.get(notification)));
        obj;
        JVM INSTR monitorexit ;
        continue; /* Loop/switch isn't completed */
        notification;
        obj;
        JVM INSTR monitorexit ;
        throw notification;
        notification;
        Log.e("NotificationCompat", "Unable to access notification actions", notification);
        sActionsAccessFailed = true;
        obj;
        JVM INSTR monitorexit ;
        notification = null;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public static NotificationCompatBase.Action[] getActionsFromParcelableArrayList(ArrayList arraylist, NotificationCompatBase.Action.Factory factory, RemoteInputCompatBase.RemoteInput.Factory factory1)
    {
        if(arraylist != null) goto _L2; else goto _L1
_L1:
        NotificationCompatBase.Action aaction[] = null;
_L4:
        return aaction;
_L2:
        NotificationCompatBase.Action aaction1[] = factory.newArray(arraylist.size());
        int i = 0;
        do
        {
            aaction = aaction1;
            if(i >= aaction1.length)
                continue;
            aaction1[i] = getActionFromBundle((Bundle)arraylist.get(i), factory, factory1);
            i++;
        } while(true);
        if(true) goto _L4; else goto _L3
_L3:
    }

    private static Bundle getBundleForAction(NotificationCompatBase.Action action)
    {
        Bundle bundle = new Bundle();
        bundle.putInt("icon", action.getIcon());
        bundle.putCharSequence("title", action.getTitle());
        bundle.putParcelable("actionIntent", action.getActionIntent());
        bundle.putBundle("extras", action.getExtras());
        bundle.putParcelableArray("remoteInputs", RemoteInputCompatJellybean.toBundleArray(action.getRemoteInputs()));
        return bundle;
    }

    public static Bundle getExtras(Notification notification)
    {
        Object obj = sExtrasLock;
        obj;
        JVM INSTR monitorenter ;
        if(!sExtrasFieldAccessFailed) goto _L2; else goto _L1
_L1:
        notification = null;
_L3:
        return notification;
_L2:
        Object obj1;
        if(sExtrasField != null)
            break MISSING_BLOCK_LABEL_73;
        obj1 = android/app/Notification.getDeclaredField("extras");
        if(android/os/Bundle.isAssignableFrom(((Field) (obj1)).getType()))
            break MISSING_BLOCK_LABEL_64;
        Log.e("NotificationCompat", "Notification.extras field is not of type Bundle");
        sExtrasFieldAccessFailed = true;
        obj;
        JVM INSTR monitorexit ;
        notification = null;
          goto _L3
        ((Field) (obj1)).setAccessible(true);
        sExtrasField = ((Field) (obj1));
        Bundle bundle = (Bundle)sExtrasField.get(notification);
        obj1 = bundle;
        if(bundle != null)
            break MISSING_BLOCK_LABEL_106;
        obj1 = JVM INSTR new #159 <Class Bundle>;
        ((Bundle) (obj1)).Bundle();
        sExtrasField.set(notification, obj1);
        obj;
        JVM INSTR monitorexit ;
        notification = ((Notification) (obj1));
          goto _L3
        notification;
        obj;
        JVM INSTR monitorexit ;
        throw notification;
        notification;
        Log.e("NotificationCompat", "Unable to access notification extras", notification);
_L4:
        sExtrasFieldAccessFailed = true;
        obj;
        JVM INSTR monitorexit ;
        notification = null;
          goto _L3
        notification;
        Log.e("NotificationCompat", "Unable to access notification extras", notification);
          goto _L4
    }

    public static String getGroup(Notification notification)
    {
        return getExtras(notification).getString("android.support.groupKey");
    }

    public static boolean getLocalOnly(Notification notification)
    {
        return getExtras(notification).getBoolean("android.support.localOnly");
    }

    public static ArrayList getParcelableArrayListForActions(NotificationCompatBase.Action aaction[])
    {
        if(aaction != null) goto _L2; else goto _L1
_L1:
        ArrayList arraylist = null;
_L4:
        return arraylist;
_L2:
        ArrayList arraylist1 = new ArrayList(aaction.length);
        int i = aaction.length;
        int j = 0;
        do
        {
            arraylist = arraylist1;
            if(j >= i)
                continue;
            arraylist1.add(getBundleForAction(aaction[j]));
            j++;
        } while(true);
        if(true) goto _L4; else goto _L3
_L3:
    }

    public static String getSortKey(Notification notification)
    {
        return getExtras(notification).getString("android.support.sortKey");
    }

    public static boolean isGroupSummary(Notification notification)
    {
        return getExtras(notification).getBoolean("android.support.isGroupSummary");
    }

    public static NotificationCompatBase.Action readAction(NotificationCompatBase.Action.Factory factory, RemoteInputCompatBase.RemoteInput.Factory factory1, int i, CharSequence charsequence, PendingIntent pendingintent, Bundle bundle)
    {
        RemoteInputCompatBase.RemoteInput aremoteinput[] = null;
        if(bundle != null)
            aremoteinput = RemoteInputCompatJellybean.fromBundleArray(BundleUtil.getBundleArrayFromBundle(bundle, "android.support.remoteInputs"), factory1);
        return factory.build(i, charsequence, pendingintent, bundle, aremoteinput);
    }

    public static Bundle writeActionAndGetExtras(android.app.Notification.Builder builder, NotificationCompatBase.Action action)
    {
        builder.addAction(action.getIcon(), action.getTitle(), action.getActionIntent());
        builder = new Bundle(action.getExtras());
        if(action.getRemoteInputs() != null)
            builder.putParcelableArray("android.support.remoteInputs", RemoteInputCompatJellybean.toBundleArray(action.getRemoteInputs()));
        return builder;
    }

    static final String EXTRA_ACTION_EXTRAS = "android.support.actionExtras";
    static final String EXTRA_GROUP_KEY = "android.support.groupKey";
    static final String EXTRA_GROUP_SUMMARY = "android.support.isGroupSummary";
    static final String EXTRA_LOCAL_ONLY = "android.support.localOnly";
    static final String EXTRA_REMOTE_INPUTS = "android.support.remoteInputs";
    static final String EXTRA_SORT_KEY = "android.support.sortKey";
    static final String EXTRA_USE_SIDE_CHANNEL = "android.support.useSideChannel";
    private static final String KEY_ACTION_INTENT = "actionIntent";
    private static final String KEY_EXTRAS = "extras";
    private static final String KEY_ICON = "icon";
    private static final String KEY_REMOTE_INPUTS = "remoteInputs";
    private static final String KEY_TITLE = "title";
    public static final String TAG = "NotificationCompat";
    private static Class sActionClass;
    private static Field sActionIconField;
    private static Field sActionIntentField;
    private static Field sActionTitleField;
    private static boolean sActionsAccessFailed;
    private static Field sActionsField;
    private static final Object sActionsLock = new Object();
    private static Field sExtrasField;
    private static boolean sExtrasFieldAccessFailed;
    private static final Object sExtrasLock = new Object();

}
