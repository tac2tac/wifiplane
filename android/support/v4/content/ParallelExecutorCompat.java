// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.content;

import java.util.concurrent.Executor;

// Referenced classes of package android.support.v4.content:
//            ExecutorCompatHoneycomb, ModernAsyncTask

public final class ParallelExecutorCompat
{

    private ParallelExecutorCompat()
    {
    }

    public static Executor getParallelExecutor()
    {
        Executor executor;
        if(android.os.Build.VERSION.SDK_INT >= 11)
            executor = ExecutorCompatHoneycomb.getParallelExecutor();
        else
            executor = ModernAsyncTask.THREAD_POOL_EXECUTOR;
        return executor;
    }
}
