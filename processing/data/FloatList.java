// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.data;

import java.util.*;
import processing.core.PApplet;

// Referenced classes of package processing.data:
//            IntList, Sort

public class FloatList
    implements Iterable
{

    public FloatList()
    {
        data = new float[10];
    }

    public FloatList(int i)
    {
        data = new float[i];
    }

    public FloatList(Iterable iterable)
    {
        this(10);
        for(iterable = iterable.iterator(); iterable.hasNext(); append(((Float)iterable.next()).floatValue()));
    }

    public FloatList(float af[])
    {
        count = af.length;
        data = new float[count];
        System.arraycopy(af, 0, data, 0, count);
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

    public void add(int i, float f)
    {
        float af[] = data;
        af[i] = af[i] + f;
    }

    public void append(float f)
    {
        if(count == data.length)
            data = PApplet.expand(data);
        float af[] = data;
        int i = count;
        count = i + 1;
        af[i] = f;
    }

    public void append(FloatList floatlist)
    {
        floatlist = floatlist.values();
        int i = floatlist.length;
        for(int j = 0; j < i; j++)
            append(floatlist[j]);

    }

    public void append(float af[])
    {
        int i = af.length;
        for(int j = 0; j < i; j++)
            append(af[j]);

    }

    public float[] array()
    {
        return array(null);
    }

    public float[] array(float af[])
    {
        float af1[];
label0:
        {
            if(af != null)
            {
                af1 = af;
                if(af.length == count)
                    break label0;
            }
            af1 = new float[count];
        }
        System.arraycopy(data, 0, af1, 0, count);
        return af1;
    }

    public void clear()
    {
        count = 0;
    }

    public FloatList copy()
    {
        FloatList floatlist = new FloatList(data);
        floatlist.count = count;
        return floatlist;
    }

    public void div(int i, float f)
    {
        float af[] = data;
        af[i] = af[i] / f;
    }

    public float get(int i)
    {
        return data[i];
    }

    public FloatList getPercent()
    {
        boolean flag = false;
        double d = 0.0D;
        float af[] = array();
        int i = af.length;
        for(int j = 0; j < i; j++)
            d += af[j];

        FloatList floatlist = new FloatList(count);
        for(int k = ((flag) ? 1 : 0); k < count; k++)
            floatlist.set(k, (float)((double)data[k] / d));

        return floatlist;
    }

    public FloatList getSubset(int i)
    {
        return getSubset(i, count - i);
    }

    public FloatList getSubset(int i, int j)
    {
        float af[] = new float[j];
        System.arraycopy(data, i, af, 0, j);
        return new FloatList(af);
    }

    public boolean hasValue(float f)
    {
        boolean flag = false;
        if(!Float.isNaN(f)) goto _L2; else goto _L1
_L1:
        int i = 0;
_L7:
        boolean flag1 = flag;
        if(i >= count) goto _L4; else goto _L3
_L3:
        if(!Float.isNaN(data[i])) goto _L6; else goto _L5
_L5:
        flag1 = true;
_L4:
        return flag1;
_L6:
        i++;
          goto _L7
_L2:
        i = 0;
_L8:
        flag1 = flag;
        if(i < count)
        {
label0:
            {
                if(data[i] != f)
                    break label0;
                flag1 = true;
            }
        }
          goto _L4
        i++;
          goto _L8
    }

    public int index(float f)
    {
        int i = 0;
_L3:
        if(i >= count)
            break MISSING_BLOCK_LABEL_29;
        if(data[i] != f) goto _L2; else goto _L1
_L1:
        return i;
_L2:
        i++;
          goto _L3
        i = -1;
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
            float af[] = new float[count + ai.length];
            System.arraycopy(data, 0, af, 0, Math.min(count, i));
            System.arraycopy(ai, 0, af, i, ai.length);
            System.arraycopy(data, i, af, ai.length + i, count - i);
            count = count + ai.length;
            data = af;
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

            public Float next()
            {
                float af[] = data;
                int i = index + 1;
                index = i;
                return Float.valueOf(af[i]);
            }

            public volatile Object next()
            {
                return next();
            }

            public void remove()
            {
                FloatList.this.remove(index);
            }

            int index;
            final FloatList this$0;

            
            {
                this$0 = FloatList.this;
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

    public float max()
    {
        checkMinMax("max");
        int i = maxIndex();
        float f;
        if(i == -1)
            f = (0.0F / 0.0F);
        else
            f = data[i];
        return f;
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
            if(data[j] == data[j])
            {
                float f = data[j];
                int i = j;
                k = j + 1;
                j = i;
                i = k;
                do
                {
                    k = j;
                    if(i >= count)
                        break;
                    float f1 = data[i];
                    k = j;
                    float f2 = f;
                    if(!Float.isNaN(f1))
                    {
                        k = j;
                        f2 = f;
                        if(f1 > f)
                        {
                            f2 = data[i];
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

    public float min()
    {
        checkMinMax("min");
        int i = minIndex();
        float f;
        if(i == -1)
            f = (0.0F / 0.0F);
        else
            f = data[i];
        return f;
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
            if(data[j] == data[j])
            {
                float f = data[j];
                int i = j;
                j++;
                do
                {
                    k = i;
                    if(j >= count)
                        break;
                    float f1 = data[j];
                    k = i;
                    float f2 = f;
                    if(!Float.isNaN(f1))
                    {
                        k = i;
                        f2 = f;
                        if(f1 < f)
                        {
                            f2 = data[j];
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

    public void mult(int i, float f)
    {
        float af[] = data;
        af[i] = af[i] * f;
    }

    public float remove(int i)
    {
        if(i < 0 || i >= count)
            throw new ArrayIndexOutOfBoundsException(i);
        float f = data[i];
        for(; i < count - 1; i++)
            data[i] = data[i + 1];

        count = count - 1;
        return f;
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
        int k = 0;
        int i1;
        if(Float.isNaN(i))
        {
            i = 0;
            do
            {
                i1 = i;
                if(k >= count)
                    break;
                i1 = i;
                if(!Float.isNaN(data[k]))
                {
                    data[i] = data[k];
                    i1 = i + 1;
                }
                k++;
                i = i1;
            } while(true);
        } else
        {
            int l = 0;
            do
            {
                i1 = l;
                if(j >= count)
                    break;
                i1 = l;
                if(data[j] != (float)i)
                {
                    data[l] = data[j];
                    i1 = l + 1;
                }
                j++;
                l = i1;
            } while(true);
        }
        i = count;
        count = i1;
        return i - i1;
    }

    public boolean replaceValue(float f, float f1)
    {
        boolean flag;
        int i;
        flag = false;
        if(!Float.isNaN(f))
            break MISSING_BLOCK_LABEL_57;
        i = 0;
_L5:
        boolean flag1 = flag;
        if(i >= count) goto _L2; else goto _L1
_L1:
        if(!Float.isNaN(data[i])) goto _L4; else goto _L3
_L3:
        data[i] = f1;
        flag1 = true;
_L2:
        return flag1;
_L4:
        i++;
          goto _L5
        int j = index(f);
        flag1 = flag;
        if(j != -1)
        {
            data[j] = f1;
            flag1 = true;
        }
          goto _L2
    }

    public boolean replaceValues(float f, float f1)
    {
        boolean flag = false;
        int i = 0;
        boolean flag3;
        if(Float.isNaN(f))
        {
            boolean flag1 = false;
            do
            {
                flag3 = flag1;
                if(i >= count)
                    break;
                if(Float.isNaN(data[i]))
                {
                    data[i] = f1;
                    flag1 = true;
                }
                i++;
            } while(true);
        } else
        {
            boolean flag2 = false;
            int j = ((flag) ? 1 : 0);
            do
            {
                flag3 = flag2;
                if(j >= count)
                    break;
                if(data[j] == f)
                {
                    data[j] = f1;
                    flag2 = true;
                }
                j++;
            } while(true);
        }
        return flag3;
    }

    public void resize(int i)
    {
        if(i <= data.length) goto _L2; else goto _L1
_L1:
        float af[] = new float[i];
        System.arraycopy(data, 0, af, 0, count);
        data = af;
_L4:
        count = i;
        return;
_L2:
        if(i > count)
            Arrays.fill(data, count, i, 0.0F);
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void reverse()
    {
        int i = count - 1;
        for(int j = 0; j < count / 2; j++)
        {
            float f = data[j];
            data[j] = data[i];
            data[i] = f;
            i--;
        }

    }

    public void set(int i, float f)
    {
        if(i >= count)
        {
            data = PApplet.expand(data, i + 1);
            for(int j = count; j < i; j++)
                data[j] = 0.0F;

            count = i + 1;
        }
        data[i] = f;
    }

    public void shuffle()
    {
        Random random = new Random();
        for(int i = count; i > 1;)
        {
            int j = random.nextInt(i);
            i--;
            float f = data[i];
            data[i] = data[j];
            data[j] = f;
        }

    }

    public void shuffle(PApplet papplet)
    {
        for(int i = count; i > 1;)
        {
            int j = (int)papplet.random(i);
            i--;
            float f = data[i];
            data[i] = data[j];
            data[j] = f;
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
                return data[j] - data[i];
            }

            public int size()
            {
                return count;
            }

            public void swap(int i, int j)
            {
                float f = data[i];
                data[i] = data[j];
                data[j] = f;
            }

            final FloatList this$0;

            
            {
                this$0 = FloatList.this;
                super();
            }
        }
).run();
    }

    public void sub(int i, float f)
    {
        float af[] = data;
        af[i] = af[i] - f;
    }

    public float sum()
    {
        double d = 0.0D;
        for(int i = 0; i < count; i++)
            d += data[i];

        return (float)d;
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

    public float[] values()
    {
        crop();
        return data;
    }

    int count;
    float data[];
}
