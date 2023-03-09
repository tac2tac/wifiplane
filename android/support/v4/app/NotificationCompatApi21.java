// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.app;

import android.app.*;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.Iterator;

// Referenced classes of package android.support.v4.app:
//            NotificationBuilderWithBuilderAccessor, NotificationBuilderWithActions, NotificationCompatApi20

class NotificationCompatApi21
{
    public static class Builder
        implements NotificationBuilderWithBuilderAccessor, NotificationBuilderWithActions
    {

        public void addAction(NotificationCompatBase.Action action)
        {
            NotificationCompatApi20.addAction(b, action);
        }

        public Notification build()
        {
            return b.build();
        }

        public android.app.Notification.Builder getBuilder()
        {
            return b;
        }

        private android.app.Notification.Builder b;

        public Builder(Context context, Notification notification, CharSequence charsequence, CharSequence charsequence1, CharSequence charsequence2, RemoteViews remoteviews, int i, 
                PendingIntent pendingintent, PendingIntent pendingintent1, Bitmap bitmap, int j, int k, boolean flag, boolean flag1, 
                boolean flag2, int l, CharSequence charsequence3, boolean flag3, String s, ArrayList arraylist, Bundle bundle, 
                int i1, int j1, Notification notification1, String s1, boolean flag4, String s2)
        {
            context = (new android.app.Notification.Builder(context)).setWhen(notification.when).setShowWhen(flag1).setSmallIcon(notification.icon, notification.iconLevel).setContent(notification.contentView).setTicker(notification.tickerText, remoteviews).setSound(notification.sound, notification.audioStreamType).setVibrate(notification.vibrate).setLights(notification.ledARGB, notification.ledOnMS, notification.ledOffMS);
            if((notification.flags & 2) != 0)
                flag1 = true;
            else
                flag1 = false;
            context = context.setOngoing(flag1);
            if((notification.flags & 8) != 0)
                flag1 = true;
            else
                flag1 = false;
            context = context.setOnlyAlertOnce(flag1);
            if((notification.flags & 0x10) != 0)
                flag1 = true;
            else
                flag1 = false;
            context = context.setAutoCancel(flag1).setDefaults(notification.defaults).setContentTitle(charsequence).setContentText(charsequence1).setSubText(charsequence3).setContentInfo(charsequence2).setContentIntent(pendingintent).setDeleteIntent(notification.deleteIntent);
            if((notification.flags & 0x80) != 0)
                flag1 = true;
            else
                flag1 = false;
            b = context.setFullScreenIntent(pendingintent1, flag1).setLargeIcon(bitmap).setNumber(i).setUsesChronometer(flag2).setPriority(l).setProgress(j, k, flag).setLocalOnly(flag3).setExtras(bundle).setGroup(s1).setGroupSummary(flag4).setSortKey(s2).setCategory(s).setColor(i1).setVisibility(j1).setPublicVersion(notification1);
            for(notification = arraylist.iterator(); notification.hasNext(); b.addPerson(context))
                context = (String)notification.next();

        }
    }


    NotificationCompatApi21()
    {
    }

    private static RemoteInput fromCompatRemoteInput(RemoteInputCompatBase.RemoteInput remoteinput)
    {
        return (new android.app.RemoteInput.Builder(remoteinput.getResultKey())).setLabel(remoteinput.getLabel()).setChoices(remoteinput.getChoices()).setAllowFreeFormInput(remoteinput.getAllowFreeFormInput()).addExtras(remoteinput.getExtras()).build();
    }

    static Bundle getBundleForUnreadConversation(NotificationCompatBase.UnreadConversation unreadconversation)
    {
        if(unreadconversation == null)
        {
            unreadconversation = null;
        } else
        {
            Bundle bundle = new Bundle();
            Parcelable aparcelable[] = null;
            Object obj = aparcelable;
            if(unreadconversation.getParticipants() != null)
            {
                obj = aparcelable;
                if(unreadconversation.getParticipants().length > 1)
                    obj = unreadconversation.getParticipants()[0];
            }
            aparcelable = new Parcelable[unreadconversation.getMessages().length];
            for(int i = 0; i < aparcelable.length; i++)
            {
                Bundle bundle1 = new Bundle();
                bundle1.putString("text", unreadconversation.getMessages()[i]);
                bundle1.putString("author", ((String) (obj)));
                aparcelable[i] = bundle1;
            }

            bundle.putParcelableArray("messages", aparcelable);
            obj = unreadconversation.getRemoteInput();
            if(obj != null)
                bundle.putParcelable("remote_input", fromCompatRemoteInput(((RemoteInputCompatBase.RemoteInput) (obj))));
            bundle.putParcelable("on_reply", unreadconversation.getReplyPendingIntent());
            bundle.putParcelable("on_read", unreadconversation.getReadPendingIntent());
            bundle.putStringArray("participants", unreadconversation.getParticipants());
            bundle.putLong("timestamp", unreadconversation.getLatestTimestamp());
            unreadconversation = bundle;
        }
        return unreadconversation;
    }

    public static String getCategory(Notification notification)
    {
        return notification.category;
    }

    static NotificationCompatBase.UnreadConversation getUnreadConversationFromBundle(Bundle bundle, NotificationCompatBase.UnreadConversation.Factory factory, RemoteInputCompatBase.RemoteInput.Factory factory1)
    {
        Object obj;
        Object obj1;
        obj = null;
        obj1 = null;
        if(bundle != null) goto _L2; else goto _L1
_L1:
        Object obj2 = obj1;
_L5:
        return ((NotificationCompatBase.UnreadConversation) (obj2));
_L2:
        String as[];
        obj2 = bundle.getParcelableArray("messages");
        as = null;
        if(obj2 == null) goto _L4; else goto _L3
_L3:
        boolean flag;
        int i;
        as = new String[obj2.length];
        flag = true;
        i = 0;
_L6:
        boolean flag1 = flag;
        PendingIntent pendingintent;
        PendingIntent pendingintent1;
        RemoteInput remoteinput;
        String as1[];
        if(i < as.length)
            if(!(obj2[i] instanceof Bundle))
            {
                flag1 = false;
            } else
            {
label0:
                {
                    as[i] = ((Bundle)obj2[i]).getString("text");
                    if(as[i] != null)
                        break label0;
                    flag1 = false;
                }
            }
        obj2 = obj1;
        if(!flag1) goto _L5; else goto _L4
_L4:
        pendingintent = (PendingIntent)bundle.getParcelable("on_read");
        pendingintent1 = (PendingIntent)bundle.getParcelable("on_reply");
        remoteinput = (RemoteInput)bundle.getParcelable("remote_input");
        as1 = bundle.getStringArray("participants");
        obj2 = obj1;
        if(as1 != null)
        {
            obj2 = obj1;
            if(as1.length == 1)
            {
                obj2 = obj;
                if(remoteinput != null)
                    obj2 = toCompatRemoteInput(remoteinput, factory1);
                obj2 = factory.build(as, ((RemoteInputCompatBase.RemoteInput) (obj2)), pendingintent1, pendingintent, as1, bundle.getLong("timestamp"));
            }
        }
          goto _L5
        i++;
          goto _L6
    }

    private static RemoteInputCompatBase.RemoteInput toCompatRemoteInput(RemoteInput remoteinput, RemoteInputCompatBase.RemoteInput.Factory factory)
    {
        return factory.build(remoteinput.getResultKey(), remoteinput.getLabel(), remoteinput.getChoices(), remoteinput.getAllowFreeFormInput(), remoteinput.getExtras());
    }

    public static final String CATEGORY_ALARM = "alarm";
    public static final String CATEGORY_CALL = "call";
    public static final String CATEGORY_EMAIL = "email";
    public static final String CATEGORY_ERROR = "err";
    public static final String CATEGORY_EVENT = "event";
    public static final String CATEGORY_MESSAGE = "msg";
    public static final String CATEGORY_PROGRESS = "progress";
    public static final String CATEGORY_PROMO = "promo";
    public static final String CATEGORY_RECOMMENDATION = "recommendation";
    public static final String CATEGORY_SERVICE = "service";
    public static final String CATEGORY_SOCIAL = "social";
    public static final String CATEGORY_STATUS = "status";
    public static final String CATEGORY_SYSTEM = "sys";
    public static final String CATEGORY_TRANSPORT = "transport";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_MESSAGES = "messages";
    private static final String KEY_ON_READ = "on_read";
    private static final String KEY_ON_REPLY = "on_reply";
    private static final String KEY_PARTICIPANTS = "participants";
    private static final String KEY_REMOTE_INPUT = "remote_input";
    private static final String KEY_TEXT = "text";
    private static final String KEY_TIMESTAMP = "timestamp";
}
