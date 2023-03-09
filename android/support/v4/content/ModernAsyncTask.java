// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.content;

import android.os.*;
import android.util.Log;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

abstract class ModernAsyncTask
{
    private static class AsyncTaskResult
    {

        final Object mData[];
        final ModernAsyncTask mTask;

        transient AsyncTaskResult(ModernAsyncTask modernasynctask, Object aobj[])
        {
            mTask = modernasynctask;
            mData = aobj;
        }
    }

    private static class InternalHandler extends Handler
    {

        public void handleMessage(Message message)
        {
            AsyncTaskResult asynctaskresult = (AsyncTaskResult)message.obj;
            message.what;
            JVM INSTR tableswitch 1 2: default 36
        //                       1 37
        //                       2 53;
               goto _L1 _L2 _L3
_L1:
            return;
_L2:
            asynctaskresult.mTask.finish(asynctaskresult.mData[0]);
            continue; /* Loop/switch isn't completed */
_L3:
            asynctaskresult.mTask.onProgressUpdate(asynctaskresult.mData);
            if(true) goto _L1; else goto _L4
_L4:
        }

        public InternalHandler()
        {
            super(Looper.getMainLooper());
        }
    }

    public static final class Status extends Enum
    {

        public static Status valueOf(String s)
        {
            return (Status)Enum.valueOf(android/support/v4/content/ModernAsyncTask$Status, s);
        }

        public static Status[] values()
        {
            return (Status[])$VALUES.clone();
        }

        private static final Status $VALUES[];
        public static final Status FINISHED;
        public static final Status PENDING;
        public static final Status RUNNING;

        static 
        {
            PENDING = new Status("PENDING", 0);
            RUNNING = new Status("RUNNING", 1);
            FINISHED = new Status("FINISHED", 2);
            $VALUES = (new Status[] {
                PENDING, RUNNING, FINISHED
            });
        }

        private Status(String s, int i)
        {
            super(s, i);
        }
    }

    private static abstract class WorkerRunnable
        implements Callable
    {

        Object mParams[];

        private WorkerRunnable()
        {
        }

    }


    public ModernAsyncTask()
    {
        mStatus = Status.PENDING;
        mFuture = new FutureTask(mWorker) {

            protected void done()
            {
                Object obj = get();
                postResultIfNotInvoked(obj);
_L1:
                return;
                Object obj1;
                obj1;
                Log.w("AsyncTask", ((Throwable) (obj1)));
                  goto _L1
                obj1;
                throw new RuntimeException("An error occurred while executing doInBackground()", ((ExecutionException) (obj1)).getCause());
                obj1;
                postResultIfNotInvoked(null);
                  goto _L1
                obj1;
                throw new RuntimeException("An error occurred while executing doInBackground()", ((Throwable) (obj1)));
            }

            final ModernAsyncTask this$0;

            
            {
                this$0 = ModernAsyncTask.this;
                super(callable);
            }
        }
;
    }

    public static void execute(Runnable runnable)
    {
        sDefaultExecutor.execute(runnable);
    }

    private void finish(Object obj)
    {
        if(isCancelled())
            onCancelled(obj);
        else
            onPostExecute(obj);
        mStatus = Status.FINISHED;
    }

    private static Handler getHandler()
    {
        android/support/v4/content/ModernAsyncTask;
        JVM INSTR monitorenter ;
        InternalHandler internalhandler1;
        if(sHandler == null)
        {
            InternalHandler internalhandler = JVM INSTR new #18  <Class ModernAsyncTask$InternalHandler>;
            internalhandler.InternalHandler();
            sHandler = internalhandler;
        }
        internalhandler1 = sHandler;
        android/support/v4/content/ModernAsyncTask;
        JVM INSTR monitorexit ;
        return internalhandler1;
        Exception exception;
        exception;
        android/support/v4/content/ModernAsyncTask;
        JVM INSTR monitorexit ;
        throw exception;
    }

    private Object postResult(Object obj)
    {
        getHandler().obtainMessage(1, new AsyncTaskResult(this, new Object[] {
            obj
        })).sendToTarget();
        return obj;
    }

    private void postResultIfNotInvoked(Object obj)
    {
        if(!mTaskInvoked.get())
            postResult(obj);
    }

    public static void setDefaultExecutor(Executor executor)
    {
        sDefaultExecutor = executor;
    }

    public final boolean cancel(boolean flag)
    {
        return mFuture.cancel(flag);
    }

    protected transient abstract Object doInBackground(Object aobj[]);

    public final transient ModernAsyncTask execute(Object aobj[])
    {
        return executeOnExecutor(sDefaultExecutor, aobj);
    }

    public final transient ModernAsyncTask executeOnExecutor(Executor executor, Object aobj[])
    {
        if(mStatus == Status.PENDING) goto _L2; else goto _L1
_L1:
        static class _cls4
        {

            static final int $SwitchMap$android$support$v4$content$ModernAsyncTask$Status[];

            static 
            {
                $SwitchMap$android$support$v4$content$ModernAsyncTask$Status = new int[Status.values().length];
                NoSuchFieldError nosuchfielderror;
                try
                {
                    $SwitchMap$android$support$v4$content$ModernAsyncTask$Status[Status.RUNNING.ordinal()] = 1;
                }
                catch(NoSuchFieldError nosuchfielderror1) { }
                $SwitchMap$android$support$v4$content$ModernAsyncTask$Status[Status.FINISHED.ordinal()] = 2;
_L2:
                return;
                nosuchfielderror;
                if(true) goto _L2; else goto _L1
_L1:
            }
        }

        _cls4..SwitchMap.android.support.v4.content.ModernAsyncTask.Status[mStatus.ordinal()];
        JVM INSTR tableswitch 1 2: default 44
    //                   1 75
    //                   2 85;
           goto _L2 _L3 _L4
_L2:
        mStatus = Status.RUNNING;
        onPreExecute();
        mWorker.mParams = aobj;
        executor.execute(mFuture);
        return this;
_L3:
        throw new IllegalStateException("Cannot execute task: the task is already running.");
_L4:
        throw new IllegalStateException("Cannot execute task: the task has already been executed (a task can be executed only once)");
    }

    public final Object get()
        throws InterruptedException, ExecutionException
    {
        return mFuture.get();
    }

    public final Object get(long l, TimeUnit timeunit)
        throws InterruptedException, ExecutionException, TimeoutException
    {
        return mFuture.get(l, timeunit);
    }

    public final Status getStatus()
    {
        return mStatus;
    }

    public final boolean isCancelled()
    {
        return mFuture.isCancelled();
    }

    protected void onCancelled()
    {
    }

    protected void onCancelled(Object obj)
    {
        onCancelled();
    }

    protected void onPostExecute(Object obj)
    {
    }

    protected void onPreExecute()
    {
    }

    protected transient void onProgressUpdate(Object aobj[])
    {
    }

    protected final transient void publishProgress(Object aobj[])
    {
        if(!isCancelled())
            getHandler().obtainMessage(2, new AsyncTaskResult(this, aobj)).sendToTarget();
    }

    private static final int CORE_POOL_SIZE = 5;
    private static final int KEEP_ALIVE = 1;
    private static final String LOG_TAG = "AsyncTask";
    private static final int MAXIMUM_POOL_SIZE = 128;
    private static final int MESSAGE_POST_PROGRESS = 2;
    private static final int MESSAGE_POST_RESULT = 1;
    public static final Executor THREAD_POOL_EXECUTOR;
    private static volatile Executor sDefaultExecutor;
    private static InternalHandler sHandler;
    private static final BlockingQueue sPoolWorkQueue;
    private static final ThreadFactory sThreadFactory;
    private final FutureTask mFuture;
    private volatile Status mStatus;
    private final AtomicBoolean mTaskInvoked = new AtomicBoolean();
    private final WorkerRunnable mWorker = new WorkerRunnable() {

        public Object call()
            throws Exception
        {
            mTaskInvoked.set(true);
            Process.setThreadPriority(10);
            return postResult(doInBackground(mParams));
        }

        final ModernAsyncTask this$0;

            
            {
                this$0 = ModernAsyncTask.this;
                super();
            }
    }
;

    static 
    {
        sThreadFactory = new ThreadFactory() {

            public Thread newThread(Runnable runnable)
            {
                return new Thread(runnable, (new StringBuilder()).append("ModernAsyncTask #").append(mCount.getAndIncrement()).toString());
            }

            private final AtomicInteger mCount = new AtomicInteger(1);

        }
;
        sPoolWorkQueue = new LinkedBlockingQueue(10);
        THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(5, 128, 1L, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);
        sDefaultExecutor = THREAD_POOL_EXECUTOR;
    }




}
