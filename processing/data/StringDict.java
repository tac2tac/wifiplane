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
//            Sort

public class StringDict
{

    public StringDict()
    {
        indices = new HashMap();
        count = 0;
        keys = new String[10];
        values = new String[10];
    }

    public StringDict(int i)
    {
        indices = new HashMap();
        count = 0;
        keys = new String[i];
        values = new String[i];
    }

    public StringDict(BufferedReader bufferedreader)
    {
        indices = new HashMap();
        String as[] = PApplet.loadStrings(bufferedreader);
        keys = new String[as.length];
        values = new String[as.length];
        for(int i = 0; i < as.length; i++)
        {
            bufferedreader = PApplet.split(as[i], '\t');
            if(bufferedreader.length == 2)
            {
                keys[count] = bufferedreader[0];
                values[count] = bufferedreader[1];
                count = count + 1;
            }
        }

    }

    public StringDict(String as[], String as1[])
    {
        indices = new HashMap();
        if(as.length != as1.length)
            throw new IllegalArgumentException("key and value arrays must be the same length");
        keys = as;
        values = as1;
        count = as.length;
        for(int i = 0; i < count; i++)
            indices.put(as[i], Integer.valueOf(i));

    }

    public void clear()
    {
        count = 0;
        indices = new HashMap();
    }

    public StringDict copy()
    {
        int i = 0;
        StringDict stringdict = new StringDict(count);
        System.arraycopy(keys, 0, stringdict.keys, 0, count);
        System.arraycopy(values, 0, stringdict.values, 0, count);
        for(; i < count; i++)
            stringdict.indices.put(keys[i], Integer.valueOf(i));

        stringdict.count = count;
        return stringdict;
    }

    protected void create(String s, String s1)
    {
        if(count == keys.length)
        {
            keys = PApplet.expand(keys);
            values = PApplet.expand(values);
        }
        indices.put(s, new Integer(count));
        keys[count] = s;
        values[count] = s1;
        count = count + 1;
    }

    protected void crop()
    {
        if(count != keys.length)
        {
            keys = PApplet.subset(keys, 0, count);
            values = PApplet.subset(values, 0, count);
        }
    }

    public String get(String s)
    {
        int i = index(s);
        if(i == -1)
            s = null;
        else
            s = values[i];
        return s;
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

    public Iterable keys()
    {
        return new Iterable() {

            public Iterator iterator()
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
                        StringDict stringdict = _fld0;
                        int i = index + 1;
                        index = i;
                        return stringdict.key(i);
                    }

                    public void remove()
                    {
                        removeIndex(index);
                    }

                    int index;
                    final _cls1 this$1;

            
            {
                this$1 = _cls1.this;
                super();
                index = -1;
            }
                }
;
            }

            final StringDict this$0;

            
            {
                this$0 = StringDict.this;
                super();
            }
        }
;
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
        indices.remove(s);
        for(; i < count - 1; i++)
        {
            keys[i] = keys[i + 1];
            values[i] = values[i + 1];
            indices.put(keys[i], Integer.valueOf(i));
        }

        count = count - 1;
        keys[count] = null;
        values[count] = null;
        return s;
    }

    public void set(String s, String s1)
    {
        int i = index(s);
        if(i == -1)
            create(s, s1);
        else
            values[i] = s1;
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
                int i1;
                float f;
                if(useKeys)
                {
                    int k = keys[i].compareToIgnoreCase(keys[j]);
                    i1 = k;
                    if(k == 0)
                        i1 = values[i].compareToIgnoreCase(values[j]);
                } else
                {
                    int l = values[i].compareToIgnoreCase(values[j]);
                    i1 = l;
                    if(l == 0)
                        i1 = keys[i].compareToIgnoreCase(keys[j]);
                }
                if(reverse)
                    f = -i1;
                else
                    f = i1;
                return f;
            }

            public int size()
            {
                return count;
            }

            public void swap(int i, int j)
            {
                StringDict.this.swap(i, j);
            }

            final StringDict this$0;
            final boolean val$reverse;
            final boolean val$useKeys;

            
            {
                this$0 = StringDict.this;
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

    protected void swap(int i, int j)
    {
        String s = keys[i];
        String s1 = values[i];
        keys[i] = keys[j];
        values[i] = values[j];
        keys[j] = s;
        values[j] = s1;
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
            stringbuilder.append((new StringBuilder()).append("\"").append(keys[i]).append("\": \"").append(values[i]).append("\"").toString());
        }

        stringbuilder.append(" }");
        return stringbuilder.toString();
    }

    public String value(int i)
    {
        return values[i];
    }

    public String[] valueArray()
    {
        return valueArray(null);
    }

    public String[] valueArray(String as[])
    {
        String as1[];
label0:
        {
            if(as != null)
            {
                as1 = as;
                if(as.length == size())
                    break label0;
            }
            as1 = new String[count];
        }
        System.arraycopy(values, 0, as1, 0, count);
        return as1;
    }

    public Iterable values()
    {
        return new Iterable() {

            public Iterator iterator()
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
                        StringDict stringdict = _fld0;
                        int i = index + 1;
                        index = i;
                        return stringdict.value(i);
                    }

                    public void remove()
                    {
                        removeIndex(index);
                    }

                    int index;
                    final _cls2 this$1;

            
            {
                this$1 = _cls2.this;
                super();
                index = -1;
            }
                }
;
            }

            final StringDict this$0;

            
            {
                this$0 = StringDict.this;
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
    protected String values[];
}
