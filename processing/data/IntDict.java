// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.data;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import processing.core.PApplet;

// Referenced classes of package processing.data:
//            FloatDict, Sort

public class IntDict
{

    public IntDict()
    {
        indices = new HashMap();
        count = 0;
        keys = new String[10];
        values = new int[10];
    }

    public IntDict(int i)
    {
        indices = new HashMap();
        count = 0;
        keys = new String[i];
        values = new int[i];
    }

    public IntDict(BufferedReader bufferedreader)
    {
        indices = new HashMap();
        bufferedreader = PApplet.loadStrings(bufferedreader);
        keys = new String[bufferedreader.length];
        values = new int[bufferedreader.length];
        for(int i = 0; i < bufferedreader.length; i++)
        {
            String as[] = PApplet.split(bufferedreader[i], '\t');
            if(as.length == 2)
            {
                keys[count] = as[0];
                values[count] = PApplet.parseInt(as[1]);
                count = count + 1;
            }
        }

    }

    public IntDict(String as[], int ai[])
    {
        indices = new HashMap();
        if(as.length != ai.length)
            throw new IllegalArgumentException("key and value arrays must be the same length");
        keys = as;
        values = ai;
        count = as.length;
        for(int i = 0; i < count; i++)
            indices.put(as[i], Integer.valueOf(i));

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

    public void add(String s, int i)
    {
        int j = index(s);
        if(j == -1)
        {
            create(s, i);
        } else
        {
            s = values;
            s[j] = s[j] + i;
        }
    }

    public void clear()
    {
        count = 0;
        indices = new HashMap();
    }

    public IntDict copy()
    {
        int i = 0;
        IntDict intdict = new IntDict(count);
        System.arraycopy(keys, 0, intdict.keys, 0, count);
        System.arraycopy(values, 0, intdict.values, 0, count);
        for(; i < count; i++)
            intdict.indices.put(keys[i], Integer.valueOf(i));

        intdict.count = count;
        return intdict;
    }

    protected void create(String s, int i)
    {
        if(count == keys.length)
        {
            keys = PApplet.expand(keys);
            values = PApplet.expand(values);
        }
        indices.put(s, new Integer(count));
        keys[count] = s;
        values[count] = i;
        count = count + 1;
    }

    public void div(String s, int i)
    {
        int j = index(s);
        if(j != -1)
        {
            s = values;
            s[j] = s[j] / i;
        }
    }

    public int get(String s)
    {
        int i = index(s);
        if(i == -1)
            i = 0;
        else
            i = values[i];
        return i;
    }

    public FloatDict getPercent()
    {
        boolean flag = false;
        double d = 0.0D;
        int ai[] = valueArray();
        int i = ai.length;
        for(int j = 0; j < i; j++)
            d += ai[j];

        FloatDict floatdict = new FloatDict();
        for(int k = ((flag) ? 1 : 0); k < size(); k++)
        {
            double d1 = (double)value(k) / d;
            floatdict.set(key(k), (float)d1);
        }

        return floatdict;
    }

    public boolean hasKey(String s)
    {
        boolean flag;
        if(index(s) != -1)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public void increment(String s)
    {
        add(s, 1);
    }

    public int index(String s)
    {
        s = (Integer)indices.get(s);
        int i;
        if(s == null)
            i = -1;
        else
            i = s.intValue();
        return i;
    }

    public String key(int i)
    {
        return keys[i];
    }

    public String[] keyArray()
    {
        return keyArray(null);
    }

    public String[] keyArray(String as[])
    {
        String as1[];
label0:
        {
            if(as != null)
            {
                as1 = as;
                if(as.length == count)
                    break label0;
            }
            as1 = new String[count];
        }
        System.arraycopy(keys, 0, as1, 0, count);
        return as1;
    }

    public Iterator keyIterator()
    {
        return new Iterator() {

            public boolean hasNext()
            {
                boolean flag;
                if(index + 1 < size())
                    flag = true;
                else
                    flag = false;
                return flag;
            }

            public volatile Object next()
            {
                return next();
            }

            public String next()
            {
                IntDict intdict = IntDict.this;
                int i = index + 1;
                index = i;
                return intdict.key(i);
            }

            public void remove()
            {
                removeIndex(index);
            }

            int index;
            final IntDict this$0;

            
            {
                this$0 = IntDict.this;
                super();
                index = -1;
            }
        }
;
    }

    public Iterable keys()
    {
        return new Iterable() {

            public Iterator iterator()
            {
                return keyIterator();
            }

            final IntDict this$0;

            
            {
                this$0 = IntDict.this;
                super();
            }
        }
;
    }

    public int maxIndex()
    {
        int i = 0;
        checkMinMax("maxIndex");
        int j = values[0];
        for(int k = 1; k < count;)
        {
            int l = j;
            if(values[k] > j)
            {
                l = values[k];
                i = k;
            }
            k++;
            j = l;
        }

        return i;
    }

    public String maxKey()
    {
        checkMinMax("maxKey");
        return keys[maxIndex()];
    }

    public int maxValue()
    {
        checkMinMax("maxValue");
        return values[maxIndex()];
    }

    public int minIndex()
    {
        int i = 0;
        checkMinMax("minIndex");
        int j = values[0];
        for(int k = 1; k < count;)
        {
            int l = j;
            if(values[k] < j)
            {
                l = values[k];
                i = k;
            }
            k++;
            j = l;
        }

        return i;
    }

    public String minKey()
    {
        checkMinMax("minKey");
        return keys[minIndex()];
    }

    public int minValue()
    {
        checkMinMax("minValue");
        return values[minIndex()];
    }

    public void mult(String s, int i)
    {
        int j = index(s);
        if(j != -1)
        {
            s = values;
            s[j] = s[j] * i;
        }
    }

    public int remove(String s)
    {
        int i = index(s);
        if(i != -1)
            removeIndex(i);
        return i;
    }

    public String removeIndex(int i)
    {
        if(i < 0 || i >= count)
            throw new ArrayIndexOutOfBoundsException(i);
        String s = keys[i];
        indices.remove(keys[i]);
        for(; i < count - 1; i++)
        {
            keys[i] = keys[i + 1];
            values[i] = values[i + 1];
            indices.put(keys[i], Integer.valueOf(i));
        }

        count = count - 1;
        keys[count] = null;
        values[count] = 0;
        return s;
    }

    public void set(String s, int i)
    {
        int j = index(s);
        if(j == -1)
            create(s, i);
        else
            values[j] = i;
    }

    public int size()
    {
        return count;
    }

    protected void sortImpl(final boolean useKeys, final boolean reverse)
    {
        (new Sort() {

            public float compare(int i, int j)
            {
                if(!useKeys) goto _L2; else goto _L1
_L1:
                int k;
                int i1;
                k = keys[i].compareToIgnoreCase(keys[j]);
                i1 = k;
                if(k != 0) goto _L4; else goto _L3
_L3:
                float f = values[i] - values[j];
_L6:
                return f;
_L2:
                int l = values[i] - values[j];
                i1 = l;
                if(l == 0)
                    i1 = keys[i].compareToIgnoreCase(keys[j]);
_L4:
                if(reverse)
                    f = -i1;
                else
                    f = i1;
                if(true) goto _L6; else goto _L5
_L5:
            }

            public int size()
            {
                return count;
            }

            public void swap(int i, int j)
            {
                IntDict.this.swap(i, j);
            }

            final IntDict this$0;
            final boolean val$reverse;
            final boolean val$useKeys;

            
            {
                this$0 = IntDict.this;
                useKeys = flag;
                reverse = flag1;
                super();
            }
        }
).run();
    }

    public void sortKeys()
    {
        sortImpl(true, false);
    }

    public void sortKeysReverse()
    {
        sortImpl(true, true);
    }

    public void sortValues()
    {
        sortImpl(false, false);
    }

    public void sortValuesReverse()
    {
        sortImpl(false, true);
    }

    public void sub(String s, int i)
    {
        add(s, -i);
    }

    protected void swap(int i, int j)
    {
        String s = keys[i];
        int k = values[i];
        keys[i] = keys[j];
        values[i] = values[j];
        keys[j] = s;
        values[j] = k;
        indices.put(keys[i], new Integer(i));
        indices.put(keys[j], new Integer(j));
    }

    public String toString()
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append((new StringBuilder()).append(getClass().getSimpleName()).append(" size=").append(size()).append(" { ").toString());
        for(int i = 0; i < size(); i++)
        {
            if(i != 0)
                stringbuilder.append(", ");
            stringbuilder.append((new StringBuilder()).append("\"").append(keys[i]).append("\": ").append(values[i]).toString());
        }

        stringbuilder.append(" }");
        return stringbuilder.toString();
    }

    public int value(int i)
    {
        return values[i];
    }

    public int[] valueArray()
    {
        return valueArray(null);
    }

    public int[] valueArray(int ai[])
    {
        int ai1[];
label0:
        {
            if(ai != null)
            {
                ai1 = ai;
                if(ai.length == size())
                    break label0;
            }
            ai1 = new int[count];
        }
        System.arraycopy(values, 0, ai1, 0, count);
        return ai1;
    }

    public Iterator valueIterator()
    {
        return new Iterator() {

            public boolean hasNext()
            {
                boolean flag;
                if(index + 1 < size())
                    flag = true;
                else
                    flag = false;
                return flag;
            }

            public Integer next()
            {
                IntDict intdict = IntDict.this;
                int i = index + 1;
                index = i;
                return Integer.valueOf(intdict.value(i));
            }

            public volatile Object next()
            {
                return next();
            }

            public void remove()
            {
                removeIndex(index);
            }

            int index;
            final IntDict this$0;

            
            {
                this$0 = IntDict.this;
                super();
                index = -1;
            }
        }
;
    }

    public Iterable values()
    {
        return new Iterable() {

            public Iterator iterator()
            {
                return valueIterator();
            }

            final IntDict this$0;

            
            {
                this$0 = IntDict.this;
                super();
            }
        }
;
    }

    public void write(PrintWriter printwriter)
    {
        for(int i = 0; i < count; i++)
            printwriter.println((new StringBuilder()).append(keys[i]).append("\t").append(values[i]).toString());

        printwriter.flush();
    }

    protected int count;
    private HashMap indices;
    protected String keys[];
    protected int values[];
}
