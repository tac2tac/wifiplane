// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.util;


public final class Pools
{
    public static interface Pool
    {

        public abstract Object acquire();

        public abstract boolean release(Object obj);
    }

    public static class SimplePool
        implements Pool
    {

        private boolean isInPool(Object obj)
        {
            int i = 0;
_L3:
            if(i >= mPoolSize)
                break MISSING_BLOCK_LABEL_30;
            if(mPool[i] != obj) goto _L2; else goto _L1
_L1:
            boolean flag = true;
_L4:
            return flag;
_L2:
            i++;
              goto _L3
            flag = false;
              goto _L4
        }

        public Object acquire()
        {
            Object obj;
            if(mPoolSize > 0)
            {
                int i = mPoolSize - 1;
                obj = mPool[i];
                mPool[i] = null;
                mPoolSize = mPoolSize - 1;
            } else
            {
                obj = null;
            }
            return obj;
        }

        public boolean release(Object obj)
        {
            if(isInPool(obj))
                throw new IllegalStateException("Already in the pool!");
            boolean flag;
            if(mPoolSize < mPool.length)
            {
                mPool[mPoolSize] = obj;
                mPoolSize = mPoolSize + 1;
                flag = true;
            } else
            {
                flag = false;
            }
            return flag;
        }

        private final Object mPool[];
        private int mPoolSize;

        public SimplePool(int i)
        {
            if(i <= 0)
            {
                throw new IllegalArgumentException("The max pool size must be > 0");
            } else
            {
                mPool = new Object[i];
                return;
            }
        }
    }

    public static class SynchronizedPool extends SimplePool
    {

        public Object acquire()
        {
            Object obj1;
            synchronized(mLock)
            {
                obj1 = super.acquire();
            }
            return obj1;
            exception;
            obj;
            JVM INSTR monitorexit ;
            throw exception;
        }

        public boolean release(Object obj)
        {
            boolean flag;
            synchronized(mLock)
            {
                flag = super.release(obj);
            }
            return flag;
            obj;
            obj1;
            JVM INSTR monitorexit ;
            throw obj;
        }

        private final Object mLock = new Object();

        public SynchronizedPool(int i)
        {
            super(i);
        }
    }


    private Pools()
    {
    }
}
