// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.util;

import java.lang.reflect.Array;
import java.util.*;

// Referenced classes of package android.support.v4.util:
//            ContainerHelpers

abstract class MapCollections
{
    final class ArrayIterator
        implements Iterator
    {

        public boolean hasNext()
        {
            boolean flag;
            if(mIndex < mSize)
                flag = true;
            else
                flag = false;
            return flag;
        }

        public Object next()
        {
            Object obj = colGetEntry(mIndex, mOffset);
            mIndex = mIndex + 1;
            mCanRemove = true;
            return obj;
        }

        public void remove()
        {
            if(!mCanRemove)
            {
                throw new IllegalStateException();
            } else
            {
                mIndex = mIndex - 1;
                mSize = mSize - 1;
                mCanRemove = false;
                colRemoveAt(mIndex);
                return;
            }
        }

        boolean mCanRemove;
        int mIndex;
        final int mOffset;
        int mSize;
        final MapCollections this$0;

        ArrayIterator(int i)
        {
            this$0 = MapCollections.this;
            super();
            mCanRemove = false;
            mOffset = i;
            mSize = colGetSize();
        }
    }

    final class EntrySet
        implements Set
    {

        public volatile boolean add(Object obj)
        {
            return add((java.util.Map.Entry)obj);
        }

        public boolean add(java.util.Map.Entry entry)
        {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection collection)
        {
            int i = colGetSize();
            java.util.Map.Entry entry;
            for(collection = collection.iterator(); collection.hasNext(); colPut(entry.getKey(), entry.getValue()))
                entry = (java.util.Map.Entry)collection.next();

            boolean flag;
            if(i != colGetSize())
                flag = true;
            else
                flag = false;
            return flag;
        }

        public void clear()
        {
            colClear();
        }

        public boolean contains(Object obj)
        {
            boolean flag = false;
            if(obj instanceof java.util.Map.Entry) goto _L2; else goto _L1
_L1:
            return flag;
_L2:
            obj = (java.util.Map.Entry)obj;
            int i = colIndexOfKey(((java.util.Map.Entry) (obj)).getKey());
            if(i >= 0)
                flag = ContainerHelpers.equal(colGetEntry(i, 1), ((java.util.Map.Entry) (obj)).getValue());
            if(true) goto _L1; else goto _L3
_L3:
        }

        public boolean containsAll(Collection collection)
        {
            collection = collection.iterator();
_L4:
            if(!collection.hasNext()) goto _L2; else goto _L1
_L1:
            if(contains(collection.next())) goto _L4; else goto _L3
_L3:
            boolean flag = false;
_L6:
            return flag;
_L2:
            flag = true;
            if(true) goto _L6; else goto _L5
_L5:
        }

        public boolean equals(Object obj)
        {
            return MapCollections.equalsSetHelper(this, obj);
        }

        public int hashCode()
        {
            int i = 0;
            int j = colGetSize() - 1;
            while(j >= 0) 
            {
                Object obj = colGetEntry(j, 0);
                Object obj1 = colGetEntry(j, 1);
                int k;
                int l;
                if(obj == null)
                    k = 0;
                else
                    k = obj.hashCode();
                if(obj1 == null)
                    l = 0;
                else
                    l = obj1.hashCode();
                i += l ^ k;
                j--;
            }
            return i;
        }

        public boolean isEmpty()
        {
            boolean flag;
            if(colGetSize() == 0)
                flag = true;
            else
                flag = false;
            return flag;
        }

        public Iterator iterator()
        {
            return new MapIterator();
        }

        public boolean remove(Object obj)
        {
            throw new UnsupportedOperationException();
        }

        public boolean removeAll(Collection collection)
        {
            throw new UnsupportedOperationException();
        }

        public boolean retainAll(Collection collection)
        {
            throw new UnsupportedOperationException();
        }

        public int size()
        {
            return colGetSize();
        }

        public Object[] toArray()
        {
            throw new UnsupportedOperationException();
        }

        public Object[] toArray(Object aobj[])
        {
            throw new UnsupportedOperationException();
        }

        final MapCollections this$0;

        EntrySet()
        {
            this$0 = MapCollections.this;
            super();
        }
    }

    final class KeySet
        implements Set
    {

        public boolean add(Object obj)
        {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection collection)
        {
            throw new UnsupportedOperationException();
        }

        public void clear()
        {
            colClear();
        }

        public boolean contains(Object obj)
        {
            boolean flag;
            if(colIndexOfKey(obj) >= 0)
                flag = true;
            else
                flag = false;
            return flag;
        }

        public boolean containsAll(Collection collection)
        {
            return MapCollections.containsAllHelper(colGetMap(), collection);
        }

        public boolean equals(Object obj)
        {
            return MapCollections.equalsSetHelper(this, obj);
        }

        public int hashCode()
        {
            int i = 0;
            int j = colGetSize() - 1;
            while(j >= 0) 
            {
                Object obj = colGetEntry(j, 0);
                int k;
                if(obj == null)
                    k = 0;
                else
                    k = obj.hashCode();
                i += k;
                j--;
            }
            return i;
        }

        public boolean isEmpty()
        {
            boolean flag;
            if(colGetSize() == 0)
                flag = true;
            else
                flag = false;
            return flag;
        }

        public Iterator iterator()
        {
            return new ArrayIterator(0);
        }

        public boolean remove(Object obj)
        {
            int i = colIndexOfKey(obj);
            boolean flag;
            if(i >= 0)
            {
                colRemoveAt(i);
                flag = true;
            } else
            {
                flag = false;
            }
            return flag;
        }

        public boolean removeAll(Collection collection)
        {
            return MapCollections.removeAllHelper(colGetMap(), collection);
        }

        public boolean retainAll(Collection collection)
        {
            return MapCollections.retainAllHelper(colGetMap(), collection);
        }

        public int size()
        {
            return colGetSize();
        }

        public Object[] toArray()
        {
            return toArrayHelper(0);
        }

        public Object[] toArray(Object aobj[])
        {
            return toArrayHelper(aobj, 0);
        }

        final MapCollections this$0;

        KeySet()
        {
            this$0 = MapCollections.this;
            super();
        }
    }

    final class MapIterator
        implements Iterator, java.util.Map.Entry
    {

        public final boolean equals(Object obj)
        {
            boolean flag;
            boolean flag1;
            flag = true;
            flag1 = false;
            if(!mEntryValid)
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            if(obj instanceof java.util.Map.Entry) goto _L2; else goto _L1
_L1:
            flag = flag1;
_L4:
            return flag;
_L2:
            obj = (java.util.Map.Entry)obj;
            if(!ContainerHelpers.equal(((java.util.Map.Entry) (obj)).getKey(), colGetEntry(mIndex, 0)) || !ContainerHelpers.equal(((java.util.Map.Entry) (obj)).getValue(), colGetEntry(mIndex, 1)))
                flag = false;
            if(true) goto _L4; else goto _L3
_L3:
        }

        public Object getKey()
        {
            if(!mEntryValid)
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            else
                return colGetEntry(mIndex, 0);
        }

        public Object getValue()
        {
            if(!mEntryValid)
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            else
                return colGetEntry(mIndex, 1);
        }

        public boolean hasNext()
        {
            boolean flag;
            if(mIndex < mEnd)
                flag = true;
            else
                flag = false;
            return flag;
        }

        public final int hashCode()
        {
            int i = 0;
            if(!mEntryValid)
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            Object obj = colGetEntry(mIndex, 0);
            Object obj1 = colGetEntry(mIndex, 1);
            int j;
            if(obj == null)
                j = 0;
            else
                j = obj.hashCode();
            if(obj1 != null)
                i = obj1.hashCode();
            return i ^ j;
        }

        public volatile Object next()
        {
            return next();
        }

        public java.util.Map.Entry next()
        {
            mIndex = mIndex + 1;
            mEntryValid = true;
            return this;
        }

        public void remove()
        {
            if(!mEntryValid)
            {
                throw new IllegalStateException();
            } else
            {
                colRemoveAt(mIndex);
                mIndex = mIndex - 1;
                mEnd = mEnd - 1;
                mEntryValid = false;
                return;
            }
        }

        public Object setValue(Object obj)
        {
            if(!mEntryValid)
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            else
                return colSetValue(mIndex, obj);
        }

        public final String toString()
        {
            return (new StringBuilder()).append(getKey()).append("=").append(getValue()).toString();
        }

        int mEnd;
        boolean mEntryValid;
        int mIndex;
        final MapCollections this$0;

        MapIterator()
        {
            this$0 = MapCollections.this;
            super();
            mEntryValid = false;
            mEnd = colGetSize() - 1;
            mIndex = -1;
        }
    }

    final class ValuesCollection
        implements Collection
    {

        public boolean add(Object obj)
        {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection collection)
        {
            throw new UnsupportedOperationException();
        }

        public void clear()
        {
            colClear();
        }

        public boolean contains(Object obj)
        {
            boolean flag;
            if(colIndexOfValue(obj) >= 0)
                flag = true;
            else
                flag = false;
            return flag;
        }

        public boolean containsAll(Collection collection)
        {
            collection = collection.iterator();
_L4:
            if(!collection.hasNext()) goto _L2; else goto _L1
_L1:
            if(contains(collection.next())) goto _L4; else goto _L3
_L3:
            boolean flag = false;
_L6:
            return flag;
_L2:
            flag = true;
            if(true) goto _L6; else goto _L5
_L5:
        }

        public boolean isEmpty()
        {
            boolean flag;
            if(colGetSize() == 0)
                flag = true;
            else
                flag = false;
            return flag;
        }

        public Iterator iterator()
        {
            return new ArrayIterator(1);
        }

        public boolean remove(Object obj)
        {
            int i = colIndexOfValue(obj);
            boolean flag;
            if(i >= 0)
            {
                colRemoveAt(i);
                flag = true;
            } else
            {
                flag = false;
            }
            return flag;
        }

        public boolean removeAll(Collection collection)
        {
            int i = colGetSize();
            boolean flag = false;
            int k;
            for(int j = 0; j < i; i = k)
            {
                k = i;
                int l = j;
                if(collection.contains(colGetEntry(j, 1)))
                {
                    colRemoveAt(j);
                    l = j - 1;
                    k = i - 1;
                    flag = true;
                }
                j = l + 1;
            }

            return flag;
        }

        public boolean retainAll(Collection collection)
        {
            int i = colGetSize();
            boolean flag = false;
            int k;
            for(int j = 0; j < i; i = k)
            {
                k = i;
                int l = j;
                if(!collection.contains(colGetEntry(j, 1)))
                {
                    colRemoveAt(j);
                    l = j - 1;
                    k = i - 1;
                    flag = true;
                }
                j = l + 1;
            }

            return flag;
        }

        public int size()
        {
            return colGetSize();
        }

        public Object[] toArray()
        {
            return toArrayHelper(1);
        }

        public Object[] toArray(Object aobj[])
        {
            return toArrayHelper(aobj, 1);
        }

        final MapCollections this$0;

        ValuesCollection()
        {
            this$0 = MapCollections.this;
            super();
        }
    }


    MapCollections()
    {
    }

    public static boolean containsAllHelper(Map map, Collection collection)
    {
        collection = collection.iterator();
_L4:
        if(!collection.hasNext()) goto _L2; else goto _L1
_L1:
        if(map.containsKey(collection.next())) goto _L4; else goto _L3
_L3:
        boolean flag = false;
_L6:
        return flag;
_L2:
        flag = true;
        if(true) goto _L6; else goto _L5
_L5:
    }

    public static boolean equalsSetHelper(Set set, Object obj)
    {
        boolean flag;
        boolean flag1;
        flag = true;
        flag1 = false;
        if(set != obj) goto _L2; else goto _L1
_L1:
        boolean flag2 = true;
_L4:
        return flag2;
_L2:
        flag2 = flag1;
        if(!(obj instanceof Set))
            continue; /* Loop/switch isn't completed */
        obj = (Set)obj;
        if(set.size() != ((Set) (obj)).size())
            break MISSING_BLOCK_LABEL_65;
        flag2 = set.containsAll(((Collection) (obj)));
        if(flag2)
        {
            flag2 = flag;
            continue; /* Loop/switch isn't completed */
        }
        flag2 = false;
        continue; /* Loop/switch isn't completed */
        set;
        flag2 = flag1;
        continue; /* Loop/switch isn't completed */
        set;
        flag2 = flag1;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public static boolean removeAllHelper(Map map, Collection collection)
    {
        int i = map.size();
        for(collection = collection.iterator(); collection.hasNext(); map.remove(collection.next()));
        boolean flag;
        if(i != map.size())
            flag = true;
        else
            flag = false;
        return flag;
    }

    public static boolean retainAllHelper(Map map, Collection collection)
    {
        int i = map.size();
        Iterator iterator = map.keySet().iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            if(!collection.contains(iterator.next()))
                iterator.remove();
        } while(true);
        boolean flag;
        if(i != map.size())
            flag = true;
        else
            flag = false;
        return flag;
    }

    protected abstract void colClear();

    protected abstract Object colGetEntry(int i, int j);

    protected abstract Map colGetMap();

    protected abstract int colGetSize();

    protected abstract int colIndexOfKey(Object obj);

    protected abstract int colIndexOfValue(Object obj);

    protected abstract void colPut(Object obj, Object obj1);

    protected abstract void colRemoveAt(int i);

    protected abstract Object colSetValue(int i, Object obj);

    public Set getEntrySet()
    {
        if(mEntrySet == null)
            mEntrySet = new EntrySet();
        return mEntrySet;
    }

    public Set getKeySet()
    {
        if(mKeySet == null)
            mKeySet = new KeySet();
        return mKeySet;
    }

    public Collection getValues()
    {
        if(mValues == null)
            mValues = new ValuesCollection();
        return mValues;
    }

    public Object[] toArrayHelper(int i)
    {
        int j = colGetSize();
        Object aobj[] = new Object[j];
        for(int k = 0; k < j; k++)
            aobj[k] = colGetEntry(k, i);

        return aobj;
    }

    public Object[] toArrayHelper(Object aobj[], int i)
    {
        int j = colGetSize();
        Object aobj1[] = aobj;
        if(aobj.length < j)
            aobj1 = (Object[])Array.newInstance(((Object) (aobj)).getClass().getComponentType(), j);
        for(int k = 0; k < j; k++)
            aobj1[k] = colGetEntry(k, i);

        if(aobj1.length > j)
            aobj1[j] = null;
        return aobj1;
    }

    EntrySet mEntrySet;
    KeySet mKeySet;
    ValuesCollection mValues;
}
