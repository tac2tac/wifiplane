// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.util;


// Referenced classes of package android.support.v4.util:
//            ContainerHelpers

public class SparseArrayCompat
    implements Cloneable
{

    public SparseArrayCompat()
    {
        this(10);
    }

    public SparseArrayCompat(int i)
    {
        mGarbage = false;
        if(i == 0)
        {
            mKeys = ContainerHelpers.EMPTY_INTS;
            mValues = ContainerHelpers.EMPTY_OBJECTS;
        } else
        {
            i = ContainerHelpers.idealIntArraySize(i);
            mKeys = new int[i];
            mValues = new Object[i];
        }
        mSize = 0;
    }

    private void gc()
    {
        int i = mSize;
        int j = 0;
        int ai[] = mKeys;
        Object aobj[] = mValues;
        for(int k = 0; k < i;)
        {
            Object obj = aobj[k];
            int l = j;
            if(obj != DELETED)
            {
                if(k != j)
                {
                    ai[j] = ai[k];
                    aobj[j] = obj;
                    aobj[k] = null;
                }
                l = j + 1;
            }
            k++;
            j = l;
        }

        mGarbage = false;
        mSize = j;
    }

    public void append(int i, Object obj)
    {
        if(mSize != 0 && i <= mKeys[mSize - 1])
        {
            put(i, obj);
        } else
        {
            if(mGarbage && mSize >= mKeys.length)
                gc();
            int j = mSize;
            if(j >= mKeys.length)
            {
                int k = ContainerHelpers.idealIntArraySize(j + 1);
                int ai[] = new int[k];
                Object aobj[] = new Object[k];
                System.arraycopy(mKeys, 0, ai, 0, mKeys.length);
                System.arraycopy(((Object) (mValues)), 0, ((Object) (aobj)), 0, mValues.length);
                mKeys = ai;
                mValues = aobj;
            }
            mKeys[j] = i;
            mValues[j] = obj;
            mSize = j + 1;
        }
    }

    public void clear()
    {
        int i = mSize;
        Object aobj[] = mValues;
        for(int j = 0; j < i; j++)
            aobj[j] = null;

        mSize = 0;
        mGarbage = false;
    }

    public SparseArrayCompat clone()
    {
        SparseArrayCompat sparsearraycompat = null;
        SparseArrayCompat sparsearraycompat1 = (SparseArrayCompat)super.clone();
        sparsearraycompat = sparsearraycompat1;
        sparsearraycompat1.mKeys = (int[])mKeys.clone();
        sparsearraycompat = sparsearraycompat1;
        sparsearraycompat1.mValues = (Object[])((Object []) (mValues)).clone();
        sparsearraycompat = sparsearraycompat1;
_L2:
        return sparsearraycompat;
        CloneNotSupportedException clonenotsupportedexception;
        clonenotsupportedexception;
        if(true) goto _L2; else goto _L1
_L1:
    }

    public volatile Object clone()
        throws CloneNotSupportedException
    {
        return clone();
    }

    public void delete(int i)
    {
        i = ContainerHelpers.binarySearch(mKeys, mSize, i);
        if(i >= 0 && mValues[i] != DELETED)
        {
            mValues[i] = DELETED;
            mGarbage = true;
        }
    }

    public Object get(int i)
    {
        return get(i, null);
    }

    public Object get(int i, Object obj)
    {
        i = ContainerHelpers.binarySearch(mKeys, mSize, i);
        Object obj1 = obj;
        if(i >= 0)
            if(mValues[i] == DELETED)
                obj1 = obj;
            else
                obj1 = mValues[i];
        return obj1;
    }

    public int indexOfKey(int i)
    {
        if(mGarbage)
            gc();
        return ContainerHelpers.binarySearch(mKeys, mSize, i);
    }

    public int indexOfValue(Object obj)
    {
        int i;
        if(mGarbage)
            gc();
        i = 0;
_L3:
        if(i >= mSize)
            break MISSING_BLOCK_LABEL_39;
        if(mValues[i] != obj) goto _L2; else goto _L1
_L1:
        return i;
_L2:
        i++;
          goto _L3
        i = -1;
          goto _L1
    }

    public int keyAt(int i)
    {
        if(mGarbage)
            gc();
        return mKeys[i];
    }

    public void put(int i, Object obj)
    {
        int j = ContainerHelpers.binarySearch(mKeys, mSize, i);
        if(j >= 0)
        {
            mValues[j] = obj;
        } else
        {
            int l = ~j;
            if(l < mSize && mValues[l] == DELETED)
            {
                mKeys[l] = i;
                mValues[l] = obj;
            } else
            {
                int k = l;
                if(mGarbage)
                {
                    k = l;
                    if(mSize >= mKeys.length)
                    {
                        gc();
                        k = ~ContainerHelpers.binarySearch(mKeys, mSize, i);
                    }
                }
                if(mSize >= mKeys.length)
                {
                    int i1 = ContainerHelpers.idealIntArraySize(mSize + 1);
                    int ai[] = new int[i1];
                    Object aobj[] = new Object[i1];
                    System.arraycopy(mKeys, 0, ai, 0, mKeys.length);
                    System.arraycopy(((Object) (mValues)), 0, ((Object) (aobj)), 0, mValues.length);
                    mKeys = ai;
                    mValues = aobj;
                }
                if(mSize - k != 0)
                {
                    System.arraycopy(mKeys, k, mKeys, k + 1, mSize - k);
                    System.arraycopy(((Object) (mValues)), k, ((Object) (mValues)), k + 1, mSize - k);
                }
                mKeys[k] = i;
                mValues[k] = obj;
                mSize = mSize + 1;
            }
        }
    }

    public void remove(int i)
    {
        delete(i);
    }

    public void removeAt(int i)
    {
        if(mValues[i] != DELETED)
        {
            mValues[i] = DELETED;
            mGarbage = true;
        }
    }

    public void removeAtRange(int i, int j)
    {
        for(j = Math.min(mSize, i + j); i < j; i++)
            removeAt(i);

    }

    public void setValueAt(int i, Object obj)
    {
        if(mGarbage)
            gc();
        mValues[i] = obj;
    }

    public int size()
    {
        if(mGarbage)
            gc();
        return mSize;
    }

    public String toString()
    {
        Object obj;
        if(size() <= 0)
        {
            obj = "{}";
        } else
        {
            StringBuilder stringbuilder = new StringBuilder(mSize * 28);
            stringbuilder.append('{');
            int i = 0;
            while(i < mSize) 
            {
                if(i > 0)
                    stringbuilder.append(", ");
                stringbuilder.append(keyAt(i));
                stringbuilder.append('=');
                obj = valueAt(i);
                if(obj != this)
                    stringbuilder.append(obj);
                else
                    stringbuilder.append("(this Map)");
                i++;
            }
            stringbuilder.append('}');
            obj = stringbuilder.toString();
        }
        return ((String) (obj));
    }

    public Object valueAt(int i)
    {
        if(mGarbage)
            gc();
        return mValues[i];
    }

    private static final Object DELETED = new Object();
    private boolean mGarbage;
    private int mKeys[];
    private int mSize;
    private Object mValues[];

}
