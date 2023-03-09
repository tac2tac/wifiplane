// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.content;

import android.content.*;
import android.os.PowerManager;
import android.util.Log;
import android.util.SparseArray;

public abstract class WakefulBroadcastReceiver extends BroadcastReceiver
{

    public WakefulBroadcastReceiver()
    {
    }

    public static boolean completeWakefulIntent(Intent intent)
    {
        boolean flag;
        int i;
        flag = false;
        i = intent.getIntExtra("android.support.content.wakelockid", 0);
        if(i != 0) goto _L2; else goto _L1
_L1:
        return flag;
_L2:
        intent = mActiveWakeLocks;
        intent;
        JVM INSTR monitorenter ;
        android.os.PowerManager.WakeLock wakelock = (android.os.PowerManager.WakeLock)mActiveWakeLocks.get(i);
        if(wakelock == null)
            break MISSING_BLOCK_LABEL_55;
        wakelock.release();
        mActiveWakeLocks.remove(i);
        intent;
        JVM INSTR monitorexit ;
        flag = true;
        continue; /* Loop/switch isn't completed */
        StringBuilder stringbuilder = JVM INSTR new #49  <Class StringBuilder>;
        stringbuilder.StringBuilder();
        Log.w("WakefulBroadcastReceiver", stringbuilder.append("No active wake lock id #").append(i).toString());
        intent;
        JVM INSTR monitorexit ;
        flag = true;
        if(true) goto _L1; else goto _L3
_L3:
        Exception exception;
        exception;
        intent;
        JVM INSTR monitorexit ;
        throw exception;
    }

    public static ComponentName startWakefulService(Context context, Intent intent)
    {
        SparseArray sparsearray = mActiveWakeLocks;
        sparsearray;
        JVM INSTR monitorenter ;
        int i;
        i = mNextId;
        mNextId++;
        if(mNextId <= 0)
            mNextId = 1;
        intent.putExtra("android.support.content.wakelockid", i);
        intent = context.startService(intent);
        if(intent != null) goto _L2; else goto _L1
_L1:
        context = null;
        sparsearray;
        JVM INSTR monitorexit ;
_L4:
        return context;
_L2:
        context = (PowerManager)context.getSystemService("power");
        StringBuilder stringbuilder = JVM INSTR new #49  <Class StringBuilder>;
        stringbuilder.StringBuilder();
        context = context.newWakeLock(1, stringbuilder.append("wake:").append(intent.flattenToShortString()).toString());
        context.setReferenceCounted(false);
        context.acquire(60000L);
        mActiveWakeLocks.put(i, context);
        sparsearray;
        JVM INSTR monitorexit ;
        context = intent;
        if(true) goto _L4; else goto _L3
_L3:
        context;
        sparsearray;
        JVM INSTR monitorexit ;
        throw context;
    }

    private static final String EXTRA_WAKE_LOCK_ID = "android.support.content.wakelockid";
    private static final SparseArray mActiveWakeLocks = new SparseArray();
    private static int mNextId = 1;

}
