// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.util;

import java.util.Map;

// Referenced classes of package android.support.v4.util:
//            ContainerHelpers, ArrayMap

public class SimpleArrayMap
{

    public SimpleArrayMap()
    {
        mHashes = ContainerHelpers.EMPTY_INTS;
        mArray = ContainerHelpers.EMPTY_OBJECTS;
        mSize = 0;
    }

    public SimpleArrayMap(int i)
    {
        if(i == 0)
        {
            mHashes = ContainerHelpers.EMPTY_INTS;
            mArray = ContainerHelpers.EMPTY_OBJECTS;
        } else
        {
            allocArrays(i);
        }
        mSize = 0;
    }

    public SimpleArrayMap(SimpleArrayMap simplearraymap)
    {
        this();
        if(simplearraymap != null)
            putAll(simplearraymap);
    }

    private void allocArrays(int i)
    {
        if(i != 8) goto _L2; else goto _L1
_L1:
        android/support/v4/util/ArrayMap;
        JVM INSTR monitorenter ;
        if(mTwiceBaseCache == null) goto _L4; else goto _L3
_L3:
        Object aobj[];
        aobj = mTwiceBaseCache;
        mArray = aobj;
        mTwiceBaseCache = (Object[])aobj[0];
        mHashes = (int[])aobj[1];
        aobj[1] = null;
        aobj[0] = null;
        mTwiceBaseCacheSize--;
        android/support/v4/util/ArrayMap;
        JVM INSTR monitorexit ;
_L5:
        return;
_L4:
        android/support/v4/util/ArrayMap;
        JVM INSTR monitorexit ;
_L7:
        mHashes = new int[i];
        mArray = new Object[i << 1];
          goto _L5
        Exception exception;
        exception;
        android/support/v4/util/ArrayMap;
        JVM INSTR monitorexit ;
        throw exception;
_L2:
        if(i != 4) goto _L7; else goto _L6
_L6:
        android/support/v4/util/ArrayMap;
        JVM INSTR monitorenter ;
        if(mBaseCache == null)
            break MISSING_BLOCK_LABEL_162;
        exception = ((Exception) (mBaseCache));
        mArray = exception;
        mBaseCache = (Object[])exception[0];
        mHashes = (int[])exception[1];
        exception[1] = null;
        exception[0] = null;
        mBaseCacheSize--;
        android/support/v4/util/ArrayMap;
        JVM INSTR monitorexit ;
          goto _L5
        exception;
        android/support/v4/util/ArrayMap;
        JVM INSTR monitorexit ;
        throw exception;
        android/support/v4/util/ArrayMap;
        JVM INSTR monitorexit ;
          goto _L7
    }

    private static void freeArrays(int ai[], Object aobj[], int i)
    {
        if(ai.length != 8) goto _L2; else goto _L1
_L1:
        android/support/v4/util/ArrayMap;
        JVM INSTR monitorenter ;
        if(mTwiceBaseCacheSize >= 10)
            break MISSING_BLOCK_LABEL_61;
        aobj[0] = ((Object) (mTwiceBaseCache));
        aobj[1] = ai;
        for(i = (i << 1) - 1; i >= 2; i--)
            aobj[i] = null;

        mTwiceBaseCache = aobj;
        mTwiceBaseCacheSize++;
        android/support/v4/util/ArrayMap;
        JVM INSTR monitorexit ;
_L4:
        return;
        ai;
        android/support/v4/util/ArrayMap;
        JVM INSTR monitorexit ;
        throw ai;
_L2:
        if(ai.length != 4) goto _L4; else goto _L3
_L3:
        android/support/v4/util/ArrayMap;
        JVM INSTR monitorenter ;
        if(mBaseCacheSize >= 10)
            break MISSING_BLOCK_LABEL_131;
        aobj[0] = ((Object) (mBaseCache));
        aobj[1] = ai;
        for(i = (i << 1) - 1; i >= 2; i--)
            aobj[i] = null;

        mBaseCache = aobj;
        mBaseCacheSize++;
        android/support/v4/util/ArrayMap;
        JVM INSTR monitorexit ;
          goto _L4
        ai;
        android/support/v4/util/ArrayMap;
        JVM INSTR monitorexit ;
        throw ai;
    }

    public void clear()
    {
        if(mSize != 0)
        {
            freeArrays(mHashes, mArray, mSize);
            mHashes = ContainerHelpers.EMPTY_INTS;
            mArray = ContainerHelpers.EMPTY_OBJECTS;
            mSize = 0;
        }
    }

    public boolean containsKey(Object obj)
    {
        boolean flag;
        if(indexOfKey(obj) >= 0)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public boolean containsValue(Object obj)
    {
        boolean flag;
        if(indexOfValue(obj) >= 0)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public void ensureCapacity(int i)
    {
        if(mHashes.length < i)
        {
            int ai[] = mHashes;
            Object aobj[] = mArray;
            allocArrays(i);
            if(mSize > 0)
            {
                System.arraycopy(ai, 0, mHashes, 0, mSize);
                System.arraycopy(((Object) (aobj)), 0, ((Object) (mArray)), 0, mSize << 1);
            }
            freeArrays(ai, aobj, mSize);
        }
    }

    public boolean equals(Object obj)
    {
        boolean flag = true;
        if(this != obj) goto _L2; else goto _L1
_L1:
        boolean flag1 = flag;
_L6:
        return flag1;
_L2:
        Map map;
        int i;
        if(!(obj instanceof Map))
            break MISSING_BLOCK_LABEL_143;
        map = (Map)obj;
        if(size() != map.size())
        {
            flag1 = false;
            continue; /* Loop/switch isn't completed */
        }
        i = 0;
_L4:
        flag1 = flag;
        Object obj1;
        Object obj2;
        try
        {
            if(i >= mSize)
                continue; /* Loop/switch isn't completed */
            obj = keyAt(i);
            obj1 = valueAt(i);
            obj2 = map.get(obj);
        }
        // Misplaced declaration of an exception variable
        catch(Object obj)
        {
            flag1 = false;
            continue; /* Loop/switch isn't completed */
        }
        // Misplaced declaration of an exception variable
        catch(Object obj)
        {
            flag1 = false;
            continue; /* Loop/switch isn't completed */
        }
        if(obj1 != null)
            break MISSING_BLOCK_LABEL_108;
        if(obj2 != null)
            break MISSING_BLOCK_LABEL_103;
        if(map.containsKey(obj))
            break MISSING_BLOCK_LABEL_125;
        flag1 = false;
        continue; /* Loop/switch isn't completed */
        flag1 = obj1.equals(obj2);
        if(!flag1)
        {
            flag1 = false;
            continue; /* Loop/switch isn't completed */
        }
        i++;
        if(true) goto _L4; else goto _L3
_L3:
        flag1 = false;
        if(true) goto _L6; else goto _L5
_L5:
    }

    public Object get(Object obj)
    {
        int i = indexOfKey(obj);
        if(i >= 0)
            obj = mArray[(i << 1) + 1];
        else
            obj = null;
        return obj;
    }

    public int hashCode()
    {
        int ai[] = mHashes;
        Object aobj[] = mArray;
        int i = 0;
        int j = 0;
        int k = 1;
        int l = mSize;
        while(j < l) 
        {
            Object obj = aobj[k];
            int i1 = ai[j];
            int j1;
            if(obj == null)
                j1 = 0;
            else
                j1 = obj.hashCode();
            i += j1 ^ i1;
            j++;
            k += 2;
        }
        return i;
    }

    int indexOf(Object obj, int i)
    {
        int j = mSize;
        if(j != 0) goto _L2; else goto _L1
_L1:
        int k = -1;
_L4:
        return k;
_L2:
        int l = ContainerHelpers.binarySearch(mHashes, j, i);
        k = l;
        if(l < 0)
            continue; /* Loop/switch isn't completed */
        k = l;
        if(obj.equals(mArray[l << 1]))
            continue; /* Loop/switch isn't completed */
        for(k = l + 1; k < j && mHashes[k] == i; k++)
            if(obj.equals(mArray[k << 1]))
                continue; /* Loop/switch isn't completed */

        l--;
        do
        {
            if(l < 0 || mHashes[l] != i)
                break;
            if(obj.equals(mArray[l << 1]))
            {
                k = l;
                continue; /* Loop/switch isn't completed */
            }
            l--;
        } while(true);
        k = ~k;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public int indexOfKey(Object obj)
    {
        int i;
        if(obj == null)
            i = indexOfNull();
        else
            i = indexOf(obj, obj.hashCode());
        return i;
    }

    int indexOfNull()
    {
        int i = mSize;
        if(i != 0) goto _L2; else goto _L1
_L1:
        int j = -1;
_L4:
        return j;
_L2:
        int k = ContainerHelpers.binarySearch(mHashes, i, 0);
        j = k;
        if(k < 0)
            continue; /* Loop/switch isn't completed */
        j = k;
        if(mArray[k << 1] == null)
            continue; /* Loop/switch isn't completed */
        for(j = k + 1; j < i && mHashes[j] == 0; j++)
            if(mArray[j << 1] == null)
                continue; /* Loop/switch isn't completed */

        k--;
        do
        {
            if(k < 0 || mHashes[k] != 0)
                break;
            if(mArray[k << 1] == null)
            {
                j = k;
                continue; /* Loop/switch isn't completed */
            }
            k--;
        } while(true);
        j = ~j;
        if(true) goto _L4; else goto _L3
_L3:
    }

    int indexOfValue(Object obj)
    {
        int i;
        Object aobj[];
        i = mSize * 2;
        aobj = mArray;
        if(obj != null) goto _L2; else goto _L1
_L1:
        int j = 1;
_L5:
        if(j >= i)
            break MISSING_BLOCK_LABEL_82;
        if(aobj[j] != null) goto _L4; else goto _L3
_L3:
        j >>= 1;
_L6:
        return j;
_L4:
        j += 2;
          goto _L5
_L2:
        j = 1;
_L7:
label0:
        {
            if(j >= i)
                break MISSING_BLOCK_LABEL_82;
            if(!obj.equals(aobj[j]))
                break label0;
            j >>= 1;
        }
          goto _L6
        j += 2;
          goto _L7
        j = -1;
          goto _L6
    }

    public boolean isEmpty()
    {
        boolean flag;
        if(mSize <= 0)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public Object keyAt(int i)
    {
        return mArray[i << 1];
    }

    public Object put(Object obj, Object obj1)
    {
        byte byte0 = 8;
        int i;
        int j;
        if(obj == null)
        {
            i = 0;
            j = indexOfNull();
        } else
        {
            i = obj.hashCode();
            j = indexOf(obj, i);
        }
        if(j >= 0)
        {
            j = (j << 1) + 1;
            obj = mArray[j];
            mArray[j] = obj1;
        } else
        {
            int l = ~j;
            if(mSize >= mHashes.length)
            {
                int k;
                int ai[];
                Object aobj[];
                if(mSize >= 8)
                {
                    k = mSize + (mSize >> 1);
                } else
                {
                    k = byte0;
                    if(mSize < 4)
                        k = 4;
                }
                ai = mHashes;
                aobj = mArray;
                allocArrays(k);
                if(mHashes.length > 0)
                {
                    System.arraycopy(ai, 0, mHashes, 0, ai.length);
                    System.arraycopy(((Object) (aobj)), 0, ((Object) (mArray)), 0, aobj.length);
                }
                freeArrays(ai, aobj, mSize);
            }
            if(l < mSize)
            {
                System.arraycopy(mHashes, l, mHashes, l + 1, mSize - l);
                System.arraycopy(((Object) (mArray)), l << 1, ((Object) (mArray)), l + 1 << 1, mSize - l << 1);
            }
            mHashes[l] = i;
            mArray[l << 1] = obj;
            mArray[(l << 1) + 1] = obj1;
            mSize = mSize + 1;
            obj = null;
        }
        return obj;
    }

    public void putAll(SimpleArrayMap simplearraymap)
    {
        int i = simplearraymap.mSize;
        ensureCapacity(mSize + i);
        if(mSize == 0)
        {
            if(i > 0)
            {
                System.arraycopy(simplearraymap.mHashes, 0, mHashes, 0, i);
                System.arraycopy(((Object) (simplearraymap.mArray)), 0, ((Object) (mArray)), 0, i << 1);
                mSize = i;
            }
        } else
        {
            int j = 0;
            while(j < i) 
            {
                put(simplearraymap.keyAt(j), simplearraymap.valueAt(j));
                j++;
            }
        }
    }

    public Object remove(Object obj)
    {
        int i = indexOfKey(obj);
        if(i >= 0)
            obj = removeAt(i);
        else
            obj = null;
        return obj;
    }

    public Object removeAt(int i)
    {
        int j;
        Object obj;
        j = 8;
        obj = mArray[(i << 1) + 1];
        if(mSize > 1) goto _L2; else goto _L1
_L1:
        freeArrays(mHashes, mArray, mSize);
        mHashes = ContainerHelpers.EMPTY_INTS;
        mArray = ContainerHelpers.EMPTY_OBJECTS;
        mSize = 0;
_L4:
        return obj;
_L2:
        if(mHashes.length > 8 && mSize < mHashes.length / 3)
        {
            if(mSize > 8)
                j = mSize + (mSize >> 1);
            int ai[] = mHashes;
            Object aobj[] = mArray;
            allocArrays(j);
            mSize = mSize - 1;
            if(i > 0)
            {
                System.arraycopy(ai, 0, mHashes, 0, i);
                System.arraycopy(((Object) (aobj)), 0, ((Object) (mArray)), 0, i << 1);
            }
            if(i < mSize)
            {
                System.arraycopy(ai, i + 1, mHashes, i, mSize - i);
                System.arraycopy(((Object) (aobj)), i + 1 << 1, ((Object) (mArray)), i << 1, mSize - i << 1);
            }
        } else
        {
            mSize = mSize - 1;
            if(i < mSize)
            {
                System.arraycopy(mHashes, i + 1, mHashes, i, mSize - i);
                System.arraycopy(((Object) (mArray)), i + 1 << 1, ((Object) (mArray)), i << 1, mSize - i << 1);
            }
            mArray[mSize << 1] = null;
            mArray[(mSize << 1) + 1] = null;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public Object setValueAt(int i, Object obj)
    {
        i = (i << 1) + 1;
        Object obj1 = mArray[i];
        mArray[i] = obj;
        return obj1;
    }

    public int size()
    {
        return mSize;
    }

    public String toString()
    {
        Object obj;
        if(isEmpty())
        {
            obj = "{}";
        } else
        {
            obj = new StringBuilder(mSize * 28);
            ((StringBuilder) (obj)).append('{');
            int i = 0;
            while(i < mSize) 
            {
                if(i > 0)
                    ((StringBuilder) (obj)).append(", ");
                Object obj1 = keyAt(i);
                if(obj1 != this)
                    ((StringBuilder) (obj)).append(obj1);
                else
                    ((StringBuilder) (obj)).append("(this Map)");
                ((StringBuilder) (obj)).append('=');
                obj1 = valueAt(i);
                if(obj1 != this)
                    ((StringBuilder) (obj)).append(obj1);
                else
                    ((StringBuilder) (obj)).append("(this Map)");
                i++;
            }
            ((StringBuilder) (obj)).append('}');
            obj = ((StringBuilder) (obj)).toString();
        }
        return ((String) (obj));
    }

    public Object valueAt(int i)
    {
        return mArray[(i << 1) + 1];
    }

    private static final int BASE_SIZE = 4;
    private static final int CACHE_SIZE = 10;
    private static final boolean DEBUG = false;
    private static final String TAG = "ArrayMap";
    static Object mBaseCache[];
    static int mBaseCacheSize;
    static Object mTwiceBaseCache[];
    static int mTwiceBaseCacheSize;
    Object mArray[];
    int mHashes[];
    int mSize;
}
