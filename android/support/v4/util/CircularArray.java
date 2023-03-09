// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.util;


public final class CircularArray
{

    public CircularArray()
    {
        this(8);
    }

    public CircularArray(int i)
    {
        if(i < 1)
            throw new IllegalArgumentException("capacity must be >= 1");
        if(i > 0x40000000)
            throw new IllegalArgumentException("capacity must be <= 2^30");
        if(Integer.bitCount(i) != 1)
            i = Integer.highestOneBit(i - 1) << 1;
        mCapacityBitmask = i - 1;
        mElements = (Object[])new Object[i];
    }

    private void doubleCapacity()
    {
        int i = mElements.length;
        int j = i - mHead;
        int k = i << 1;
        if(k < 0)
        {
            throw new RuntimeException("Max array capacity exceeded");
        } else
        {
            Object aobj[] = new Object[k];
            System.arraycopy(((Object) (mElements)), mHead, ((Object) (aobj)), 0, j);
            System.arraycopy(((Object) (mElements)), 0, ((Object) (aobj)), j, mHead);
            mElements = (Object[])aobj;
            mHead = 0;
            mTail = i;
            mCapacityBitmask = k - 1;
            return;
        }
    }

    public void addFirst(Object obj)
    {
        mHead = mHead - 1 & mCapacityBitmask;
        mElements[mHead] = obj;
        if(mHead == mTail)
            doubleCapacity();
    }

    public void addLast(Object obj)
    {
        mElements[mTail] = obj;
        mTail = mTail + 1 & mCapacityBitmask;
        if(mTail == mHead)
            doubleCapacity();
    }

    public void clear()
    {
        removeFromStart(size());
    }

    public Object get(int i)
    {
        if(i < 0 || i >= size())
            throw new ArrayIndexOutOfBoundsException();
        else
            return mElements[mHead + i & mCapacityBitmask];
    }

    public Object getFirst()
    {
        if(mHead == mTail)
            throw new ArrayIndexOutOfBoundsException();
        else
            return mElements[mHead];
    }

    public Object getLast()
    {
        if(mHead == mTail)
            throw new ArrayIndexOutOfBoundsException();
        else
            return mElements[mTail - 1 & mCapacityBitmask];
    }

    public boolean isEmpty()
    {
        boolean flag;
        if(mHead == mTail)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public Object popFirst()
    {
        if(mHead == mTail)
        {
            throw new ArrayIndexOutOfBoundsException();
        } else
        {
            Object obj = mElements[mHead];
            mElements[mHead] = null;
            mHead = mHead + 1 & mCapacityBitmask;
            return obj;
        }
    }

    public Object popLast()
    {
        if(mHead == mTail)
        {
            throw new ArrayIndexOutOfBoundsException();
        } else
        {
            int i = mTail - 1 & mCapacityBitmask;
            Object obj = mElements[i];
            mElements[i] = null;
            mTail = i;
            return obj;
        }
    }

    public void removeFromEnd(int i)
    {
        if(i > 0) goto _L2; else goto _L1
_L1:
        return;
_L2:
        if(i > size())
            throw new ArrayIndexOutOfBoundsException();
        int j = 0;
        if(i < mTail)
            j = mTail - i;
        for(int l = j; l < mTail; l++)
            mElements[l] = null;

        j = mTail - j;
        i -= j;
        mTail = mTail - j;
        if(i > 0)
        {
            mTail = mElements.length;
            int k = mTail - i;
            for(i = k; i < mTail; i++)
                mElements[i] = null;

            mTail = k;
        }
        if(true) goto _L1; else goto _L3
_L3:
    }

    public void removeFromStart(int i)
    {
        if(i > 0) goto _L2; else goto _L1
_L1:
        return;
_L2:
        if(i > size())
            throw new ArrayIndexOutOfBoundsException();
        int j = mElements.length;
        int k = j;
        if(i < j - mHead)
            k = mHead + i;
        for(j = mHead; j < k; j++)
            mElements[j] = null;

        j = k - mHead;
        k = i - j;
        mHead = mHead + j & mCapacityBitmask;
        if(k > 0)
        {
            for(i = 0; i < k; i++)
                mElements[i] = null;

            mHead = k;
        }
        if(true) goto _L1; else goto _L3
_L3:
    }

    public int size()
    {
        return mTail - mHead & mCapacityBitmask;
    }

    private int mCapacityBitmask;
    private Object mElements[];
    private int mHead;
    private int mTail;
}
