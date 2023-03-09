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

public class FloatDict
{

    public FloatDict()
    {
        indices = new HashMap();
        count = 0;
        keys = new String[10];
        values = new float[10];
    }

    public FloatDict(int i)
    {
        indices = new HashMap();
        count = 0;
        keys = new String[i];
        values = new float[i];
    }

    public FloatDict(BufferedReader bufferedreader)
    {
        indices = new HashMap();
        String as[] = PApplet.loadStrings(bufferedreader);
        keys = new String[as.length];
        values = new float[as.length];
        for(int i = 0; i < as.length; i++)
        {
            bufferedreader = PApplet.split(as[i], '\t');
            if(bufferedreader.length == 2)
            {
                keys[count] = bufferedreader[0];
                values[count] = PApplet.parseFloat(bufferedreader[1]);
                count = count + 1;
            }
        }

    }

    public FloatDict(String as[], float af[])
    {
        indices = new HashMap();
        if(as.length != af.length)
            throw new IllegalArgumentException("key and value arrays must be the same length");
        keys = as;
        values = af;
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

    public void add(String s, float f)
    {
        int i = index(s);
        if(i == -1)
        {
            create(s, f);
        } else
        {
            s = values;
            s[i] = s[i] + f;
        }
    }

    public void clear()
    {
        count = 0;
        indices = new HashMap();
    }

    public FloatDict copy()
    {
        int i = 0;
        FloatDict floatdict = new FloatDict(count);
        System.arraycopy(keys, 0, floatdict.keys, 0, count);
        System.arraycopy(values, 0, floatdict.values, 0, count);
        for(; i < count; i++)
            floatdict.indices.put(keys[i], Integer.valueOf(i));

        floatdict.count = count;
        return floatdict;
    }

    protected void create(String s, float f)
    {
        if(count == keys.length)
        {
            keys = PApplet.expand(keys);
            values = PApplet.expand(values);
        }
        indices.put(s, new Integer(count));
        keys[count] = s;
        values[count] = f;
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

    public void div(String s, float f)
    {
        int i = index(s);
        if(i != -1)
        {
            s = values;
            s[i] = s[i] / f;
        }
    }

    public float get(String s)
    {
        int i = index(s);
        float f;
        if(i == -1)
            f = 0.0F;
        else
            f = values[i];
        return f;
    }

    public FloatDict getPercent()
    {
        boolean flag = false;
        double d = 0.0D;
        float af[] = valueArray();
        int i = af.length;
        for(int j = 0; j < i; j++)
            d += af[j];

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
                        FloatDict floatdict = _fld0;
                        int i = index + 1;
                        index = i;
                        return floatdict.key(i);
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

            final FloatDict this$0;

            
            {
                this$0 = FloatDict.this;
                super();
            }
        }
;
    }

    public int maxIndex()
    {
        checkMinMax("maxIndex");
        byte byte0 = -1;
        int j = 0;
        int k;
        do
        {
            k = byte0;
            if(j >= count)
                break;
            if(values[j] == values[j])
            {
                float f = values[j];
                int i = j;
                j++;
                do
                {
                    k = i;
                    if(j >= count)
                        break;
                    float f1 = values[j];
                    k = i;
                    float f2 = f;
                    if(!Float.isNaN(f1))
                    {
                        k = i;
                        f2 = f;
                        if(f1 > f)
                        {
                            f2 = values[j];
                            k = j;
                        }
                    }
                    j++;
                    i = k;
                    f = f2;
                } while(true);
                break;
            }
            j++;
        } while(true);
        return k;
    }

    public String maxKey()
    {
        checkMinMax("maxKey");
        return keys[maxIndex()];
    }

    public float maxValue()
    {
        checkMinMax("maxValue");
        return values[maxIndex()];
    }

    public int minIndex()
    {
        checkMinMax("minIndex");
        byte byte0 = -1;
        int j = 0;
        int k;
        do
        {
            k = byte0;
            if(j >= count)
                break;
            if(values[j] == values[j])
            {
                float f = values[j];
                int i = j;
                k = j + 1;
                j = i;
                i = k;
                do
                {
                    k = j;
                    if(i >= count)
                        break;
                    float f1 = values[i];
                    k = j;
                    float f2 = f;
                    if(!Float.isNaN(f1))
                    {
                        k = j;
                        f2 = f;
                        if(f1 < f)
                        {
                            f2 = values[i];
                            k = i;
                        }
                    }
                    i++;
                    j = k;
                    f = f2;
                } while(true);
                break;
            }
            j++;
        } while(true);
        return k;
    }

    public String minKey()
    {
        checkMinMax("minKey");
        return keys[minIndex()];
    }

    public float minValue()
    {
        checkMinMax("minValue");
        return values[minIndex()];
    }

    public void mult(String s, float f)
    {
        int i = index(s);
        if(i != -1)
        {
            s = values;
            s[i] = s[i] * f;
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
        values[count] = 0.0F;
        return s;
    }

    public void set(String s, float f)
    {
        int i = index(s);
        if(i == -1)
            create(s, f);
        else
            values[i] = f;
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
                float f;
                float f1;
                f = keys[i].compareToIgnoreCase(keys[j]);
                f1 = f;
                if(f != 0.0F) goto _L4; else goto _L3
_L3:
                f = values[i] - values[j];
_L6:
                return f;
_L2:
                f = values[i] - values[j];
                f1 = f;
                if(f == 0.0F)
                    f1 = keys[i].compareToIgnoreCase(keys[j]);
_L4:
                f = f1;
                if(reverse)
                    f = -f1;
                if(true) goto _L6; else goto _L5
_L5:
            }

            public int size()
            {
                return count;
            }

            public void swap(int i, int j)
            {
                FloatDict.this.swap(i, j);
            }

            final FloatDict this$0;
            final boolean val$reverse;
            final boolean val$useKeys;

            
            {
                this$0 = FloatDict.this;
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

    public void sub(String s, float f)
    {
        add(s, -f);
    }

    protected void swap(int i, int j)
    {
        String s = keys[i];
        float f = values[i];
        keys[i] = keys[j];
        values[i] = values[j];
        keys[j] = s;
        values[j] = f;
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

    public float value(int i)
    {
        return values[i];
    }

    public float[] valueArray()
    {
        return valueArray(null);
    }

    public float[] valueArray(float af[])
    {
        float af1[];
label0:
        {
            if(af != null)
            {
                af1 = af;
                if(af.length == size())
                    break label0;
            }
            af1 = new float[count];
        }
        System.arraycopy(values, 0, af1, 0, count);
        return af1;
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

                    public Float next()
                    {
                        FloatDict floatdict = _fld0;
                        int i = index + 1;
                        index = i;
                        return Float.valueOf(floatdict.value(i));
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
                    final _cls2 this$1;

            
            {
                this$1 = _cls2.this;
                super();
                index = -1;
            }
                }
;
            }

            final FloatDict this$0;

            
            {
                this$0 = FloatDict.this;
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
    protected float values[];
}
