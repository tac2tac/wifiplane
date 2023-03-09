// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.os;


// Referenced classes of package android.support.v4.os:
//            CancellationSignalCompatJellybean, OperationCanceledException

public final class CancellationSignal
{
    public static interface OnCancelListener
    {

        public abstract void onCancel();
    }


    public CancellationSignal()
    {
    }

    private void waitForCancelFinishedLocked()
    {
        while(mCancelInProgress) 
            try
            {
                wait();
            }
            catch(InterruptedException interruptedexception) { }
    }

    public void cancel()
    {
        this;
        JVM INSTR monitorenter ;
        if(!mIsCanceled) goto _L2; else goto _L1
_L1:
        this;
        JVM INSTR monitorexit ;
_L3:
        return;
_L2:
        OnCancelListener oncancellistener;
        Object obj;
        mIsCanceled = true;
        mCancelInProgress = true;
        oncancellistener = mOnCancelListener;
        obj = mCancellationSignalObj;
        this;
        JVM INSTR monitorexit ;
        if(oncancellistener == null)
            break MISSING_BLOCK_LABEL_44;
        oncancellistener.onCancel();
        if(obj == null)
            break MISSING_BLOCK_LABEL_52;
        CancellationSignalCompatJellybean.cancel(obj);
        this;
        JVM INSTR monitorenter ;
        mCancelInProgress = false;
        notifyAll();
        this;
        JVM INSTR monitorexit ;
          goto _L3
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
        exception;
        this;
        JVM INSTR monitorenter ;
        mCancelInProgress = false;
        notifyAll();
        this;
        JVM INSTR monitorexit ;
        throw exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
    }

    public Object getCancellationSignalObject()
    {
        if(android.os.Build.VERSION.SDK_INT >= 16) goto _L2; else goto _L1
_L1:
        Object obj = null;
_L4:
        return obj;
_L2:
        this;
        JVM INSTR monitorenter ;
        if(mCancellationSignalObj == null)
        {
            mCancellationSignalObj = CancellationSignalCompatJellybean.create();
            if(mIsCanceled)
                CancellationSignalCompatJellybean.cancel(mCancellationSignalObj);
        }
        obj = mCancellationSignalObj;
        this;
        JVM INSTR monitorexit ;
        if(true) goto _L4; else goto _L3
_L3:
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
    }

    public boolean isCanceled()
    {
        this;
        JVM INSTR monitorenter ;
        boolean flag = mIsCanceled;
        this;
        JVM INSTR monitorexit ;
        return flag;
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
    }

    public void setOnCancelListener(OnCancelListener oncancellistener)
    {
        this;
        JVM INSTR monitorenter ;
        waitForCancelFinishedLocked();
        if(mOnCancelListener != oncancellistener) goto _L2; else goto _L1
_L1:
        this;
        JVM INSTR monitorexit ;
_L3:
        return;
_L2:
        mOnCancelListener = oncancellistener;
        if(mIsCanceled && oncancellistener != null)
            break MISSING_BLOCK_LABEL_43;
        this;
        JVM INSTR monitorexit ;
          goto _L3
        oncancellistener;
        this;
        JVM INSTR monitorexit ;
        throw oncancellistener;
        this;
        JVM INSTR monitorexit ;
        oncancellistener.onCancel();
          goto _L3
    }

    public void throwIfCanceled()
    {
        if(isCanceled())
            throw new OperationCanceledException();
        else
            return;
    }

    private boolean mCancelInProgress;
    private Object mCancellationSignalObj;
    private boolean mIsCanceled;
    private OnCancelListener mOnCancelListener;
}
