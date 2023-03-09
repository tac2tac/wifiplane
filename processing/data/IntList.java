// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.data;

import java.util.*;
import processing.core.PApplet;

// Referenced classes of package processing.data:
//            FloatList, Sort

public class IntList
    implements Iterable
{

    public IntList()
    {
        data = new int[10];
    }

    public IntList(int i)
    {
        data = new int[i];
    }

    public IntList(Iterable iterable)
    {
        this(10);
        for(iterable = iterable.iterator(); iterable.hasNext(); append(((Integer)iterable.next()).intValue()));
    }

    public IntList(int ai[])
    {
        count = ai.length;
        data = new int[count];
        System.arraycopy(ai, 0, data, 0, count);
    }

    private void checkMinMax(String s)
    {
        if(count == 0)
            throw new RuntimeException(String.format("Cannot use %s() on an empty %s.", new Object[] {
                s, getClass().getSimpleName()
            }));
        else
            return;
    }

    private void crop()
    {
        if(count != data.length)
            data = PApplet.subset(data, 0, count);
    }

    public static IntList fromRange(int i)
    {
        return fromRange(0, i);
    }

    public static IntList fromRange(int i, int j)
    {
        int k = j - i;
        IntList intlist = new IntList(k);
        for(j = 0; j < k; j++)
            intlist.set(j, i + j);

        return intlist;
    }

    public void add(int i, int j)
    {
        int ai[] = data;
        ai[i] = ai[i] + j;
    }

    public void append(int i)
    {
        if(count == data.length)
            data = PApplet.expand(data);
        int ai[] = data;
        int j = count;
        count = j + 1;
        ai[j] = i;
    }

    public void append(IntList intlist)
    {
        intlist = intlist.values();
        int i = intlist.length;
        for(int j = 0; j < i; j++)
            append(intlist[j]);

    }

    public void append(int ai[])
    {
        int i = ai.length;
        for(int j = 0; j < i; j++)
            append(ai[j]);

    }

    public int[] array()
    {
        return array(null);
    }

    public int[] array(int ai[])
    {
        int ai1[];
label0:
        {
            if(ai != null)
            {
                ai1 = ai;
                if(ai.length == count)
                    break label0;
            }
            ai1 = new int[count];
        }
        System.arraycopy(data, 0, ai1, 0, count);
        return ai1;
    }

    public void clear()
    {
        count = 0;
    }

    public IntList copy()
    {
        IntList intlist = new IntList(data);
        intlist.count = count;
        return intlist;
    }

    public void div(int i, int j)
    {
        int ai[] = data;
        ai[i] = ai[i] / j;
    }

    public int get(int i)
    {
        return data[i];
    }

    public FloatList getPercent()
    {
        boolean flag = false;
        double d = 0.0D;
        int ai[] = array();
        int i = ai.length;
        for(int j = 0; j < i; j++)
            d += (float)ai[j];

        FloatList floatlist = new FloatList(count);
        for(int k = ((flag) ? 1 : 0); k < count; k++)
            floatlist.set(k, (float)((double)data[k] / d));

        return floatlist;
    }

    public IntList getSubset(int i)
    {
        return getSubset(i, count - i);
    }

    public IntList getSubset(int i, int j)
    {
        int ai[] = new int[j];
        System.arraycopy(data, i, ai, 0, j);
        return new IntList(ai);
    }

    public boolean hasValue(int i)
    {
        boolean flag = false;
        int j = 0;
        do
        {
label0:
            {
                boolean flag1 = flag;
                if(j < count)
                {
                    if(data[j] != i)
                        break label0;
                    flag1 = true;
                }
                return flag1;
            }
            j++;
        } while(true);
    }

    public void increment(int i)
    {
        if(count <= i)
            resize(i + 1);
        int ai[] = data;
        ai[i] = ai[i] + 1;
    }

    public int index(int i)
    {
        int j = 0;
_L3:
        if(j >= count)
            break MISSING_BLOCK_LABEL_28;
        if(data[j] != i) goto _L2; else goto _L1
_L1:
        return j;
_L2:
        j++;
          goto _L3
        j = -1;
          goto _L1
    }

    public void insert(int i, IntList intlist)
    {
        insert(i, intlist.values());
    }

    public void insert(int i, int ai[])
    {
        if(i < 0)
            throw new IllegalArgumentException((new StringBuilder()).append("insert() index cannot be negative: it was ").append(i).toString());
        if(i >= data.length)
        {
            throw new IllegalArgumentException((new StringBuilder()).append("insert() index ").append(i).append(" is past the end of this list").toString());
        } else
        {
            int ai1[] = new int[count + ai.length];
            System.arraycopy(data, 0, ai1, 0, Math.min(count, i));
            System.arraycopy(ai, 0, ai1, i, ai.length);
            System.arraycopy(data, i, ai1, ai.length + i, count - i);
            count = count + ai.length;
            data = ai1;
            return;
        }
    }

    public Iterator iterator()
    {
        return new Iterator() {

            public boolean hasNext()
            {
                boolean flag;
                if(index + 1 < count)
                    flag = true;
                else
                    flag = false;
                return flag;
            }

            public Integer next()
            {
                int ai[] = data;
                int i = index + 1;
                index = i;
                return Integer.valueOf(ai[i]);
            }

            public volatile Object next()
            {
                return next();
            }

            public void remove()
            {
                IntList.this.remove(index);
            }

            int index;
            final IntList this$0;

            
            {
                this$0 = IntList.this;
                super();
                index = -1;
            }
        }
;
    }

    public String join(String s)
    {
        if(count == 0)
        {
            s = "";
        } else
        {
            StringBuilder stringbuilder = new StringBuilder();
            stringbuilder.append(data[0]);
            for(int i = 1; i < count; i++)
            {
                stringbuilder.append(s);
                stringbuilder.append(data[i]);
            }

            s = stringbuilder.toString();
        }
        return s;
    }

    public int max()
    {
        checkMinMax("max");
        int i = data[0];
        for(int j = 1; j < count;)
        {
            int k = i;
            if(data[j] > i)
                k = data[j];
            j++;
            i = k;
        }

        return i;
    }

    public int maxIndex()
    {
        int i = 0;
        checkMinMax("maxIndex");
        int j = data[0];
        for(int k = 1; k < count;)
        {
            int l = j;
            if(data[k] > j)
            {
                l = data[k];
                i = k;
            }
            k++;
            j = l;
        }

        return i;
    }

    public int min()
    {
        checkMinMax("min");
        int i = data[0];
        for(int j = 1; j < count;)
        {
            int k = i;
            if(data[j] < i)
                k = data[j];
            j++;
            i = k;
        }

        return i;
    }

    public int minIndex()
    {
        int i = 0;
        checkMinMax("minIndex");
        int j = data[0];
        for(int k = 1; k < count;)
        {
            int l = j;
            if(data[k] < j)
            {
                l = data[k];
                i = k;
            }
            k++;
            j = l;
        }

        return i;
    }

    public void mult(int i, int j)
    {
        int ai[] = data;
        ai[i] = ai[i] * j;
    }

    public int remove(int i)
    {
        if(i < 0 || i >= count)
            throw new ArrayIndexOutOfBoundsException(i);
        int j = data[i];
        for(; i < count - 1; i++)
            data[i] = data[i + 1];

        count = count - 1;
        return j;
    }

    public int removeValue(int i)
    {
        i = index(i);
        if(i != -1)
            remove(i);
        else
            i = -1;
        return i;
    }

    public int removeValues(int i)
    {
        int j = 0;
        int k;
        int l;
        for(k = 0; j < count; k = l)
        {
            l = k;
            if(data[j] != i)
            {
                data[k] = data[j];
                l = k + 1;
            }
            j++;
        }

        i = count;
        count = k;
        return i - k;
    }

    public void resize(int i)
    {
        if(i <= data.length) goto _L2; else goto _L1
_L1:
        int ai[] = new int[i];
        System.arraycopy(data, 0, ai, 0, count);
        data = ai;
_L4:
        count = i;
        return;
_L2:
        if(i > count)
            Arrays.fill(data, count, i, 0);
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void reverse()
    {
        int i = count - 1;
        for(int j = 0; j < count / 2; j++)
        {
            int k = data[j];
            data[j] = data[i];
            data[i] = k;
            i--;
        }

    }

    public void set(int i, int j)
    {
        if(i >= count)
        {
            data = PApplet.expand(data, i + 1);
            for(int k = count; k < i; k++)
                data[k] = 0;

            count = i + 1;
        }
        data[i] = j;
    }

    public void shuffle()
    {
        Random random = new Random();
        for(int i = count; i > 1;)
        {
            int j = random.nextInt(i);
            i--;
            int k = data[i];
            data[i] = data[j];
            data[j] = k;
        }

    }

    public void shuffle(PApplet papplet)
    {
        for(int i = count; i > 1;)
        {
            int j = (int)papplet.random(i);
            i--;
            int k = data[i];
            data[i] = data[j];
            data[j] = k;
        }

    }

    public int size()
    {
        return count;
    }

    public void sort()
    {
        Arrays.sort(data, 0, count);
    }

    public void sortReverse()
    {
        (new Sort() {

            public float compare(int i, int j)
            {
                return (float)(data[j] - data[i]);
            }

            public int size()
            {
                return count;
            }

            public void swap(int i, int j)
            {
                int k = data[i];
                data[i] = data[j];
                data[j] = k;
            }

            final IntList this$0;

            
            {
                this$0 = IntList.this;
                super();
            }
        }
).run();
    }

    public void sub(int i, int j)
    {
        int ai[] = data;
        ai[i] = ai[i] - j;
    }

    public int sum()
    {
        int i = 0;
        int j = 0;
        for(; i < count; i++)
            j += data[i];

        return j;
    }

    public String toString()
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append((new StringBuilder()).append(getClass().getSimpleName()).append(" size=").append(size()).append(" [ ").toString());
        for(int i = 0; i < size(); i++)
        {
            if(i != 0)
                stringbuilder.append(", ");
            stringbuilder.append((new StringBuilder()).append(i).append(": ").append(data[i]).toString());
        }

        stringbuilder.append(" ]");
        return stringbuilder.toString();
    }

    public int[] values()
    {
        crop();
        return data;
    }

    protected int count;
    protected int data[];
}
