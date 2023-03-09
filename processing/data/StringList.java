// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.data;

import java.io.PrintStream;
import java.util.*;
import processing.core.PApplet;

// Referenced classes of package processing.data:
//            IntDict, Sort

public class StringList
    implements Iterable
{

    public StringList()
    {
        this(10);
    }

    public StringList(int i)
    {
        data = new String[i];
    }

    public StringList(Iterable iterable)
    {
        this(10);
        for(iterable = iterable.iterator(); iterable.hasNext(); append((String)iterable.next()));
    }

    public transient StringList(Object aobj[])
    {
        int i = 0;
        super();
        count = aobj.length;
        data = new String[count];
        int j = aobj.length;
        int k = 0;
        for(; i < j; i++)
        {
            Object obj = aobj[i];
            if(obj != null)
                data[k] = obj.toString();
            k++;
        }

    }

    public StringList(String as[])
    {
        count = as.length;
        data = new String[count];
        System.arraycopy(as, 0, data, 0, count);
    }

    private void crop()
    {
        if(count != data.length)
            data = PApplet.subset(data, 0, count);
    }

    private void sortImpl(final boolean reverse)
    {
        (new Sort() {

            public float compare(int i, int j)
            {
                float f = data[i].compareToIgnoreCase(data[j]);
                float f1 = f;
                if(reverse)
                    f1 = -f;
                return f1;
            }

            public int size()
            {
                return count;
            }

            public void swap(int i, int j)
            {
                String s = data[i];
                data[i] = data[j];
                data[j] = s;
            }

            final StringList this$0;
            final boolean val$reverse;

            
            {
                this$0 = StringList.this;
                reverse = flag;
                super();
            }
        }
).run();
    }

    public void append(String s)
    {
        if(count == data.length)
            data = PApplet.expand(data);
        String as[] = data;
        int i = count;
        count = i + 1;
        as[i] = s;
    }

    public void append(StringList stringlist)
    {
        stringlist = stringlist.values();
        int i = stringlist.length;
        for(int j = 0; j < i; j++)
            append(stringlist[j]);

    }

    public void append(String as[])
    {
        int i = as.length;
        for(int j = 0; j < i; j++)
            append(as[j]);

    }

    public void appendUnique(String s)
    {
        if(!hasValue(s))
            append(s);
    }

    public String[] array()
    {
        return array(null);
    }

    public String[] array(String as[])
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
        System.arraycopy(data, 0, as1, 0, count);
        return as1;
    }

    public void clear()
    {
        count = 0;
    }

    public StringList copy()
    {
        StringList stringlist = new StringList(data);
        stringlist.count = count;
        return stringlist;
    }

    public String get(int i)
    {
        if(i >= count)
            throw new ArrayIndexOutOfBoundsException(i);
        else
            return data[i];
    }

    public IntDict getOrder()
    {
        IntDict intdict = new IntDict();
        for(int i = 0; i < count; i++)
            intdict.set(data[i], i);

        return intdict;
    }

    public StringList getSubset(int i)
    {
        return getSubset(i, count - i);
    }

    public StringList getSubset(int i, int j)
    {
        String as[] = new String[j];
        System.arraycopy(data, i, as, 0, j);
        return new StringList(as);
    }

    public IntDict getTally()
    {
        IntDict intdict = new IntDict();
        for(int i = 0; i < count; i++)
            intdict.increment(data[i]);

        return intdict;
    }

    public String[] getUnique()
    {
        return getTally().keyArray();
    }

    public boolean hasValue(String s)
    {
        boolean flag = false;
        if(s != null) goto _L2; else goto _L1
_L1:
        int i = 0;
_L7:
        boolean flag1 = flag;
        if(i >= count) goto _L4; else goto _L3
_L3:
        if(data[i] != null) goto _L6; else goto _L5
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
                if(!s.equals(data[i]))
                    break label0;
                flag1 = true;
            }
        }
          goto _L4
        i++;
          goto _L8
    }

    public int index(String s)
    {
        int i;
        boolean flag;
        i = 0;
        flag = false;
        if(s != null) goto _L2; else goto _L1
_L1:
        i = ((flag) ? 1 : 0);
_L7:
        if(i >= count) goto _L4; else goto _L3
_L3:
        if(data[i] != null) goto _L6; else goto _L5
_L5:
        return i;
_L6:
        i++;
          goto _L7
_L9:
        i++;
_L2:
        if(i >= count) goto _L4; else goto _L8
_L8:
        if(!s.equals(data[i])) goto _L9; else goto _L5
_L4:
        i = -1;
          goto _L5
    }

    public void insert(int i, String s)
    {
        insert(i, new String[] {
            s
        });
    }

    public void insert(int i, StringList stringlist)
    {
        insert(i, stringlist.values());
    }

    public void insert(int i, String as[])
    {
        if(i < 0)
            throw new IllegalArgumentException((new StringBuilder()).append("insert() index cannot be negative: it was ").append(i).toString());
        if(i >= data.length)
        {
            throw new IllegalArgumentException((new StringBuilder()).append("insert() index ").append(i).append(" is past the end of this list").toString());
        } else
        {
            String as1[] = new String[count + as.length];
            System.arraycopy(data, 0, as1, 0, Math.min(count, i));
            System.arraycopy(as, 0, as1, i, as.length);
            System.arraycopy(data, i, as1, as.length + i, count - i);
            count = count + as.length;
            data = as1;
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

            public volatile Object next()
            {
                return next();
            }

            public String next()
            {
                String as[] = data;
                int i = index + 1;
                index = i;
                return as[i];
            }

            public void remove()
            {
                StringList.this.remove(index);
            }

            int index;
            final StringList this$0;

            
            {
                this$0 = StringList.this;
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

    public void lower()
    {
        for(int i = 0; i < count; i++)
            if(data[i] != null)
                data[i] = data[i].toLowerCase();

    }

    public String pop()
    {
        if(count == 0)
        {
            throw new RuntimeException("Can't call pop() on an empty list");
        } else
        {
            String s = get(count - 1);
            String as[] = data;
            int i = count - 1;
            count = i;
            as[i] = null;
            return s;
        }
    }

    public void print()
    {
        for(int i = 0; i < size(); i++)
            System.out.format("[%d] %s%n", new Object[] {
                Integer.valueOf(i), data[i]
            });

    }

    public void push(String s)
    {
        append(s);
    }

    public String remove(int i)
    {
        if(i < 0 || i >= count)
            throw new ArrayIndexOutOfBoundsException(i);
        String s = data[i];
        for(; i < count - 1; i++)
            data[i] = data[i + 1];

        count = count - 1;
        return s;
    }

    public int removeValue(String s)
    {
        if(s != null) goto _L2; else goto _L1
_L1:
        int i = 0;
_L5:
        if(i >= count)
            break MISSING_BLOCK_LABEL_57;
        if(data[i] != null) goto _L4; else goto _L3
_L3:
        remove(i);
_L6:
        return i;
_L4:
        i++;
          goto _L5
_L2:
        i = index(s);
        if(i == -1)
            break MISSING_BLOCK_LABEL_57;
        remove(i);
          goto _L6
        i = -1;
          goto _L6
    }

    public int removeValues(String s)
    {
        int i = 0;
        int j = 0;
        if(s == null)
        {
            int l = 0;
            do
            {
                i = l;
                if(j >= count)
                    break;
                i = l;
                if(data[j] != null)
                {
                    data[l] = data[j];
                    i = l + 1;
                }
                j++;
                l = i;
            } while(true);
        } else
        {
            int i1 = 0;
            int k = i;
            do
            {
                i = i1;
                if(k >= count)
                    break;
                i = i1;
                if(!s.equals(data[k]))
                {
                    data[i1] = data[k];
                    i = i1 + 1;
                }
                k++;
                i1 = i;
            } while(true);
        }
        int j1 = count;
        count = i;
        return j1 - i;
    }

    public int replaceValue(String s, String s1)
    {
        int i;
        boolean flag;
        i = 0;
        flag = false;
        if(s != null) goto _L2; else goto _L1
_L1:
        i = ((flag) ? 1 : 0);
_L5:
        if(i >= count)
            break MISSING_BLOCK_LABEL_78;
        if(data[i] != null) goto _L4; else goto _L3
_L3:
        data[i] = s1;
_L8:
        return i;
_L4:
        i++;
          goto _L5
_L7:
        i++;
_L2:
        if(i >= count)
            break MISSING_BLOCK_LABEL_78;
        if(!s.equals(data[i])) goto _L7; else goto _L6
_L6:
        data[i] = s1;
          goto _L8
        i = -1;
          goto _L8
    }

    public int replaceValues(String s, String s1)
    {
        int i = 0;
        int j = 0;
        if(s == null)
        {
            int l = 0;
            do
            {
                i = l;
                if(j >= count)
                    break;
                i = l;
                if(data[j] == null)
                {
                    data[j] = s1;
                    i = l + 1;
                }
                j++;
                l = i;
            } while(true);
        } else
        {
            int i1 = 0;
            int k = i;
            do
            {
                i = i1;
                if(k >= count)
                    break;
                i = i1;
                if(s.equals(data[k]))
                {
                    data[k] = s1;
                    i = i1 + 1;
                }
                k++;
                i1 = i;
            } while(true);
        }
        return i;
    }

    public void resize(int i)
    {
        if(i <= data.length) goto _L2; else goto _L1
_L1:
        String as[] = new String[i];
        System.arraycopy(data, 0, as, 0, count);
        data = as;
_L4:
        count = i;
        return;
_L2:
        if(i > count)
            Arrays.fill(data, count, i, Integer.valueOf(0));
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void reverse()
    {
        int i = count - 1;
        for(int j = 0; j < count / 2; j++)
        {
            String s = data[j];
            data[j] = data[i];
            data[i] = s;
            i--;
        }

    }

    public void set(int i, String s)
    {
        if(i >= count)
        {
            data = PApplet.expand(data, i + 1);
            for(int j = count; j < i; j++)
                data[j] = null;

            count = i + 1;
        }
        data[i] = s;
    }

    public void shuffle()
    {
        Random random = new Random();
        for(int i = count; i > 1;)
        {
            int j = random.nextInt(i);
            i--;
            String s = data[i];
            data[i] = data[j];
            data[j] = s;
        }

    }

    public void shuffle(PApplet papplet)
    {
        for(int i = count; i > 1;)
        {
            int j = (int)papplet.random(i);
            i--;
            String s = data[i];
            data[i] = data[j];
            data[j] = s;
        }

    }

    public int size()
    {
        return count;
    }

    public void sort()
    {
        sortImpl(false);
    }

    public void sortReverse()
    {
        sortImpl(true);
    }

    public String toString()
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append((new StringBuilder()).append(getClass().getSimpleName()).append(" size=").append(size()).append(" [ ").toString());
        for(int i = 0; i < size(); i++)
        {
            if(i != 0)
                stringbuilder.append(", ");
            stringbuilder.append((new StringBuilder()).append(i).append(": \"").append(data[i]).append("\"").toString());
        }

        stringbuilder.append(" ]");
        return stringbuilder.toString();
    }

    public void upper()
    {
        for(int i = 0; i < count; i++)
            if(data[i] != null)
                data[i] = data[i].toUpperCase();

    }

    public String[] values()
    {
        crop();
        return data;
    }

    int count;
    String data[];
}
