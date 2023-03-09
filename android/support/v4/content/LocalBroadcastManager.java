// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.content;

import android.content.*;
import android.os.*;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;

public final class LocalBroadcastManager
{
    private static class BroadcastRecord
    {

        final Intent intent;
        final ArrayList receivers;

        BroadcastRecord(Intent intent1, ArrayList arraylist)
        {
            intent = intent1;
            receivers = arraylist;
        }
    }

    private static class ReceiverRecord
    {

        public String toString()
        {
            StringBuilder stringbuilder = new StringBuilder(128);
            stringbuilder.append("Receiver{");
            stringbuilder.append(receiver);
            stringbuilder.append(" filter=");
            stringbuilder.append(filter);
            stringbuilder.append("}");
            return stringbuilder.toString();
        }

        boolean broadcasting;
        final IntentFilter filter;
        final BroadcastReceiver receiver;

        ReceiverRecord(IntentFilter intentfilter, BroadcastReceiver broadcastreceiver)
        {
            filter = intentfilter;
            receiver = broadcastreceiver;
        }
    }


    private LocalBroadcastManager(Context context)
    {
        mAppContext = context;
        mHandler = new Handler(context.getMainLooper()) {

            public void handleMessage(Message message)
            {
                message.what;
                JVM INSTR tableswitch 1 1: default 24
            //                           1 30;
                   goto _L1 _L2
_L1:
                super.handleMessage(message);
_L4:
                return;
_L2:
                executePendingBroadcasts();
                if(true) goto _L4; else goto _L3
_L3:
            }

            final LocalBroadcastManager this$0;

            
            {
                this$0 = LocalBroadcastManager.this;
                super(looper);
            }
        }
;
    }

    private void executePendingBroadcasts()
    {
_L4:
        Object obj = mReceivers;
        obj;
        JVM INSTR monitorenter ;
        int i = mPendingBroadcasts.size();
        if(i > 0)
            break MISSING_BLOCK_LABEL_22;
        obj;
        JVM INSTR monitorexit ;
        return;
        BroadcastRecord abroadcastrecord[];
        abroadcastrecord = new BroadcastRecord[i];
        mPendingBroadcasts.toArray(abroadcastrecord);
        mPendingBroadcasts.clear();
        obj;
        JVM INSTR monitorexit ;
        i = 0;
_L2:
        if(i < abroadcastrecord.length)
        {
            obj = abroadcastrecord[i];
            for(int j = 0; j < ((BroadcastRecord) (obj)).receivers.size(); j++)
                ((ReceiverRecord)((BroadcastRecord) (obj)).receivers.get(j)).receiver.onReceive(mAppContext, ((BroadcastRecord) (obj)).intent);

            break MISSING_BLOCK_LABEL_109;
        }
        continue; /* Loop/switch isn't completed */
        Exception exception;
        exception;
        obj;
        JVM INSTR monitorexit ;
        throw exception;
        i++;
        if(true) goto _L2; else goto _L1
_L1:
        if(true) goto _L4; else goto _L3
_L3:
    }

    public static LocalBroadcastManager getInstance(Context context)
    {
        synchronized(mLock)
        {
            if(mInstance == null)
            {
                LocalBroadcastManager localbroadcastmanager = JVM INSTR new #2   <Class LocalBroadcastManager>;
                localbroadcastmanager.LocalBroadcastManager(context.getApplicationContext());
                mInstance = localbroadcastmanager;
            }
            context = mInstance;
        }
        return context;
        context;
        obj;
        JVM INSTR monitorexit ;
        throw context;
    }

    public void registerReceiver(BroadcastReceiver broadcastreceiver, IntentFilter intentfilter)
    {
        HashMap hashmap = mReceivers;
        hashmap;
        JVM INSTR monitorenter ;
        ReceiverRecord receiverrecord;
        Object obj;
        receiverrecord = JVM INSTR new #11  <Class LocalBroadcastManager$ReceiverRecord>;
        receiverrecord.ReceiverRecord(intentfilter, broadcastreceiver);
        obj = (ArrayList)mReceivers.get(broadcastreceiver);
        ArrayList arraylist;
        arraylist = ((ArrayList) (obj));
        if(obj != null)
            break MISSING_BLOCK_LABEL_63;
        arraylist = JVM INSTR new #56  <Class ArrayList>;
        arraylist.ArrayList(1);
        mReceivers.put(broadcastreceiver, arraylist);
        arraylist.add(intentfilter);
        int i = 0;
_L2:
        if(i >= intentfilter.countActions())
            break; /* Loop/switch isn't completed */
        obj = intentfilter.getAction(i);
        arraylist = (ArrayList)mActions.get(obj);
        broadcastreceiver = arraylist;
        if(arraylist != null)
            break MISSING_BLOCK_LABEL_132;
        broadcastreceiver = JVM INSTR new #56  <Class ArrayList>;
        broadcastreceiver.ArrayList(1);
        mActions.put(obj, broadcastreceiver);
        broadcastreceiver.add(receiverrecord);
        i++;
        if(true) goto _L2; else goto _L1
_L1:
        hashmap;
        JVM INSTR monitorexit ;
        return;
        broadcastreceiver;
        hashmap;
        JVM INSTR monitorexit ;
        throw broadcastreceiver;
    }

    public boolean sendBroadcast(Intent intent)
    {
        HashMap hashmap = mReceivers;
        hashmap;
        JVM INSTR monitorenter ;
        String s;
        String s1;
        android.net.Uri uri;
        String s2;
        java.util.Set set;
        s = intent.getAction();
        s1 = intent.resolveTypeIfNeeded(mAppContext.getContentResolver());
        uri = intent.getData();
        s2 = intent.getScheme();
        set = intent.getCategories();
        int i;
        Object obj;
        Object obj1;
        ReceiverRecord receiverrecord;
        ArrayList arraylist1;
        int j;
        if((intent.getFlags() & 8) != 0)
            i = 1;
        else
            i = 0;
        if(!i)
            break MISSING_BLOCK_LABEL_111;
        obj = JVM INSTR new #178 <Class StringBuilder>;
        ((StringBuilder) (obj)).StringBuilder();
        Log.v("LocalBroadcastManager", ((StringBuilder) (obj)).append("Resolving type ").append(s1).append(" scheme ").append(s2).append(" of intent ").append(intent).toString());
        arraylist1 = (ArrayList)mActions.get(intent.getAction());
        if(arraylist1 == null) goto _L2; else goto _L1
_L1:
        if(!i)
            break MISSING_BLOCK_LABEL_168;
        obj = JVM INSTR new #178 <Class StringBuilder>;
        ((StringBuilder) (obj)).StringBuilder();
        Log.v("LocalBroadcastManager", ((StringBuilder) (obj)).append("Action list: ").append(arraylist1).toString());
        obj1 = null;
        j = 0;
_L7:
        if(j >= arraylist1.size()) goto _L4; else goto _L3
_L3:
        receiverrecord = (ReceiverRecord)arraylist1.get(j);
        if(!i)
            break MISSING_BLOCK_LABEL_235;
        obj = JVM INSTR new #178 <Class StringBuilder>;
        ((StringBuilder) (obj)).StringBuilder();
        Log.v("LocalBroadcastManager", ((StringBuilder) (obj)).append("Matching against filter ").append(receiverrecord.filter).toString());
        if(!receiverrecord.broadcasting) goto _L6; else goto _L5
_L5:
        obj = obj1;
        if(!i)
            break MISSING_BLOCK_LABEL_264;
        Log.v("LocalBroadcastManager", "  Filter's target already added");
        obj = obj1;
_L8:
        j++;
        obj1 = obj;
          goto _L7
_L6:
        int k = receiverrecord.filter.match(s, s1, s2, uri, set, "LocalBroadcastManager");
        if(k < 0)
            break MISSING_BLOCK_LABEL_386;
        if(!i)
            break MISSING_BLOCK_LABEL_345;
        obj = JVM INSTR new #178 <Class StringBuilder>;
        ((StringBuilder) (obj)).StringBuilder();
        Log.v("LocalBroadcastManager", ((StringBuilder) (obj)).append("  Filter matched!  match=0x").append(Integer.toHexString(k)).toString());
        obj = obj1;
        if(obj1 != null)
            break MISSING_BLOCK_LABEL_364;
        obj = JVM INSTR new #56  <Class ArrayList>;
        ((ArrayList) (obj)).ArrayList();
        ((ArrayList) (obj)).add(receiverrecord);
        receiverrecord.broadcasting = true;
          goto _L8
        intent;
        hashmap;
        JVM INSTR monitorexit ;
        throw intent;
        obj = obj1;
        if(!i) goto _L8; else goto _L9
_L9:
        k;
        JVM INSTR tableswitch -4 -1: default 428
    //                   -4 477
    //                   -3 470
    //                   -2 484
    //                   -1 491;
           goto _L10 _L11 _L12 _L13 _L14
_L14:
        break MISSING_BLOCK_LABEL_491;
_L10:
        obj = "unknown reason";
_L15:
        StringBuilder stringbuilder = JVM INSTR new #178 <Class StringBuilder>;
        stringbuilder.StringBuilder();
        Log.v("LocalBroadcastManager", stringbuilder.append("  Filter did not match: ").append(((String) (obj))).toString());
        obj = obj1;
          goto _L8
_L12:
        obj = "action";
          goto _L15
_L11:
        obj = "category";
          goto _L15
_L13:
        obj = "data";
          goto _L15
        obj = "type";
          goto _L15
_L4:
        if(obj1 == null) goto _L2; else goto _L16
_L16:
        i = 0;
_L18:
        if(i >= ((ArrayList) (obj1)).size())
            break; /* Loop/switch isn't completed */
        ((ReceiverRecord)((ArrayList) (obj1)).get(i)).broadcasting = false;
        i++;
        if(true) goto _L18; else goto _L17
_L17:
        ArrayList arraylist = mPendingBroadcasts;
        BroadcastRecord broadcastrecord = JVM INSTR new #8   <Class LocalBroadcastManager$BroadcastRecord>;
        broadcastrecord.BroadcastRecord(intent, ((ArrayList) (obj1)));
        arraylist.add(broadcastrecord);
        if(!mHandler.hasMessages(1))
            mHandler.sendEmptyMessage(1);
        boolean flag = true;
        hashmap;
        JVM INSTR monitorexit ;
_L20:
        return flag;
_L2:
        hashmap;
        JVM INSTR monitorexit ;
        flag = false;
        if(true) goto _L20; else goto _L19
_L19:
          goto _L8
    }

    public void sendBroadcastSync(Intent intent)
    {
        if(sendBroadcast(intent))
            executePendingBroadcasts();
    }

    public void unregisterReceiver(BroadcastReceiver broadcastreceiver)
    {
        HashMap hashmap = mReceivers;
        hashmap;
        JVM INSTR monitorenter ;
        ArrayList arraylist = (ArrayList)mReceivers.remove(broadcastreceiver);
        if(arraylist != null) goto _L2; else goto _L1
_L1:
        hashmap;
        JVM INSTR monitorexit ;
_L9:
        return;
_L2:
        int i = 0;
_L8:
        IntentFilter intentfilter;
        if(i >= arraylist.size())
            break; /* Loop/switch isn't completed */
        intentfilter = (IntentFilter)arraylist.get(i);
        int j = 0;
_L6:
        String s;
        ArrayList arraylist1;
        if(j >= intentfilter.countActions())
            break; /* Loop/switch isn't completed */
        s = intentfilter.getAction(j);
        arraylist1 = (ArrayList)mActions.get(s);
        int k;
        if(arraylist1 == null)
            break MISSING_BLOCK_LABEL_165;
        k = 0;
_L4:
        if(k >= arraylist1.size())
            break; /* Loop/switch isn't completed */
        int l = k;
        if(((ReceiverRecord)arraylist1.get(k)).receiver != broadcastreceiver)
            break MISSING_BLOCK_LABEL_138;
        arraylist1.remove(k);
        l = k - 1;
        k = l + 1;
        if(true) goto _L4; else goto _L3
_L3:
        if(arraylist1.size() <= 0)
            mActions.remove(s);
        j++;
        if(true) goto _L6; else goto _L5
_L5:
        i++;
        if(true) goto _L8; else goto _L7
_L7:
        hashmap;
        JVM INSTR monitorexit ;
          goto _L9
        broadcastreceiver;
        hashmap;
        JVM INSTR monitorexit ;
        throw broadcastreceiver;
    }

    private static final boolean DEBUG = false;
    static final int MSG_EXEC_PENDING_BROADCASTS = 1;
    private static final String TAG = "LocalBroadcastManager";
    private static LocalBroadcastManager mInstance;
    private static final Object mLock = new Object();
    private final HashMap mActions = new HashMap();
    private final Context mAppContext;
    private final Handler mHandler;
    private final ArrayList mPendingBroadcasts = new ArrayList();
    private final HashMap mReceivers = new HashMap();


}
