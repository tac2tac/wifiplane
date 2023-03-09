// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.data;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import processing.core.PApplet;

// Referenced classes of package processing.data:
//            JSONTokener, JSONObject, FloatList, IntList, 
//            StringList

public class JSONArray
{

    public JSONArray()
    {
        myArrayList = new ArrayList();
    }

    public JSONArray(Reader reader)
    {
        this(new JSONTokener(reader));
    }

    protected JSONArray(Object obj)
    {
        this();
        if(obj.getClass().isArray())
        {
            int i = Array.getLength(obj);
            for(int j = 0; j < i; j++)
                append(JSONObject.wrap(Array.get(obj, j)));

        } else
        {
            throw new RuntimeException("JSONArray initial value should be a string or collection or array.");
        }
    }

    public JSONArray(FloatList floatlist)
    {
        myArrayList = new ArrayList();
        floatlist = floatlist.values();
        int i = floatlist.length;
        for(int j = 0; j < i; j++)
        {
            float f = floatlist[j];
            myArrayList.add(new Float(f));
        }

    }

    public JSONArray(IntList intlist)
    {
        myArrayList = new ArrayList();
        intlist = intlist.values();
        int i = intlist.length;
        for(int j = 0; j < i; j++)
        {
            int k = intlist[j];
            myArrayList.add(new Integer(k));
        }

    }

    protected JSONArray(JSONTokener jsontokener)
    {
        this();
        if(jsontokener.nextClean() != '[')
            throw new RuntimeException("A JSONArray text must start with '['");
        if(jsontokener.nextClean() == ']') goto _L2; else goto _L1
_L1:
        jsontokener.back();
_L7:
        if(jsontokener.nextClean() == ',')
        {
            jsontokener.back();
            myArrayList.add(JSONObject.NULL);
        } else
        {
            jsontokener.back();
            myArrayList.add(jsontokener.nextValue());
        }
        jsontokener.nextClean();
        JVM INSTR lookupswitch 3: default 100
    //                   44: 129
    //                   59: 129
    //                   93: 138;
           goto _L3 _L4 _L4 _L2
_L3:
        throw new RuntimeException("Expected a ',' or ']'");
_L4:
        if(jsontokener.nextClean() != ']') goto _L5; else goto _L2
_L2:
        return;
_L5:
        jsontokener.back();
        if(true) goto _L7; else goto _L6
_L6:
    }

    public JSONArray(StringList stringlist)
    {
        myArrayList = new ArrayList();
        stringlist = stringlist.values();
        int i = stringlist.length;
        for(int j = 0; j < i; j++)
        {
            Object obj = stringlist[j];
            myArrayList.add(obj);
        }

    }

    private Object get(int i)
    {
        Object obj = opt(i);
        if(obj == null)
            throw new RuntimeException((new StringBuilder()).append("JSONArray[").append(i).append("] not found.").toString());
        else
            return obj;
    }

    private Object opt(int i)
    {
        Object obj;
        if(i < 0 || i >= size())
            obj = null;
        else
            obj = myArrayList.get(i);
        return obj;
    }

    public static JSONArray parse(String s)
    {
        JSONArray jsonarray;
        jsonarray = JVM INSTR new #2   <Class JSONArray>;
        JSONTokener jsontokener = JVM INSTR new #20  <Class JSONTokener>;
        jsontokener.JSONTokener(s);
        jsonarray.JSONArray(jsontokener);
        s = jsonarray;
_L2:
        return s;
        s;
        s = null;
        if(true) goto _L2; else goto _L1
_L1:
    }

    private JSONArray set(int i, Object obj)
    {
        JSONObject.testValidity(obj);
        if(i < 0)
            throw new RuntimeException((new StringBuilder()).append("JSONArray[").append(i).append("] not found.").toString());
        if(i < size())
        {
            myArrayList.set(i, obj);
        } else
        {
            for(; i != size(); append(JSONObject.NULL));
            append(obj);
        }
        return this;
    }

    public JSONArray append(double d)
    {
        Double double1 = new Double(d);
        JSONObject.testValidity(double1);
        append(double1);
        return this;
    }

    public JSONArray append(float f)
    {
        return append(f);
    }

    public JSONArray append(int i)
    {
        append(new Integer(i));
        return this;
    }

    public JSONArray append(long l)
    {
        append(new Long(l));
        return this;
    }

    protected JSONArray append(Object obj)
    {
        myArrayList.add(obj);
        return this;
    }

    public JSONArray append(String s)
    {
        append(s);
        return this;
    }

    public JSONArray append(JSONArray jsonarray)
    {
        myArrayList.add(jsonarray);
        return this;
    }

    public JSONArray append(JSONObject jsonobject)
    {
        myArrayList.add(jsonobject);
        return this;
    }

    public JSONArray append(boolean flag)
    {
        Boolean boolean1;
        if(flag)
            boolean1 = Boolean.TRUE;
        else
            boolean1 = Boolean.FALSE;
        append(boolean1);
        return this;
    }

    public String format(int i)
    {
        Object obj = new StringWriter();
        synchronized(((StringWriter) (obj)).getBuffer())
        {
            obj = write(((Writer) (obj)), i, 0).toString();
        }
        return ((String) (obj));
        exception;
        stringbuffer;
        JVM INSTR monitorexit ;
        throw exception;
    }

    public boolean getBoolean(int i)
    {
        Object obj = get(i);
        boolean flag;
        if(obj.equals(Boolean.FALSE) || (obj instanceof String) && ((String)obj).equalsIgnoreCase("false"))
            flag = false;
        else
        if(obj.equals(Boolean.TRUE) || (obj instanceof String) && ((String)obj).equalsIgnoreCase("true"))
            flag = true;
        else
            throw new RuntimeException((new StringBuilder()).append("JSONArray[").append(i).append("] is not a boolean.").toString());
        return flag;
    }

    public boolean getBoolean(int i, boolean flag)
    {
        boolean flag1 = getBoolean(i);
        flag = flag1;
_L2:
        return flag;
        Exception exception;
        exception;
        if(true) goto _L2; else goto _L1
_L1:
    }

    public boolean[] getBooleanArray()
    {
        boolean aflag[] = new boolean[size()];
        for(int i = 0; i < size(); i++)
            aflag[i] = getBoolean(i);

        return aflag;
    }

    public double getDouble(int i)
    {
        Object obj = get(i);
        if(!(obj instanceof Number)) goto _L2; else goto _L1
_L1:
        double d = ((Number)obj).doubleValue();
_L4:
        return d;
_L2:
        try
        {
            d = Double.parseDouble((String)obj);
        }
        catch(Exception exception)
        {
            throw new RuntimeException((new StringBuilder()).append("JSONArray[").append(i).append("] is not a number.").toString());
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public double getDouble(int i, double d)
    {
        double d1 = getDouble(i);
        d = d1;
_L2:
        return d;
        Exception exception;
        exception;
        if(true) goto _L2; else goto _L1
_L1:
    }

    public double[] getDoubleArray()
    {
        double ad[] = new double[size()];
        for(int i = 0; i < size(); i++)
            ad[i] = getDouble(i);

        return ad;
    }

    public float getFloat(int i)
    {
        return (float)getDouble(i);
    }

    public float getFloat(int i, float f)
    {
        float f1 = getFloat(i);
        f = f1;
_L2:
        return f;
        Exception exception;
        exception;
        if(true) goto _L2; else goto _L1
_L1:
    }

    public float[] getFloatArray()
    {
        float af[] = new float[size()];
        for(int i = 0; i < size(); i++)
            af[i] = getFloat(i);

        return af;
    }

    public int getInt(int i)
    {
        Object obj = get(i);
        if(!(obj instanceof Number)) goto _L2; else goto _L1
_L1:
        int j = ((Number)obj).intValue();
        i = j;
_L4:
        return i;
_L2:
        int k;
        try
        {
            k = Integer.parseInt((String)obj);
        }
        catch(Exception exception)
        {
            throw new RuntimeException((new StringBuilder()).append("JSONArray[").append(i).append("] is not a number.").toString());
        }
        i = k;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public int getInt(int i, int j)
    {
        i = getInt(i);
        j = i;
_L2:
        return j;
        Exception exception;
        exception;
        if(true) goto _L2; else goto _L1
_L1:
    }

    public int[] getIntArray()
    {
        int ai[] = new int[size()];
        for(int i = 0; i < size(); i++)
            ai[i] = getInt(i);

        return ai;
    }

    public JSONArray getJSONArray(int i)
    {
        Object obj = get(i);
        if(obj instanceof JSONArray)
            return (JSONArray)obj;
        else
            throw new RuntimeException((new StringBuilder()).append("JSONArray[").append(i).append("] is not a JSONArray.").toString());
    }

    public JSONArray getJSONArray(int i, JSONArray jsonarray)
    {
        JSONArray jsonarray1 = getJSONArray(i);
        jsonarray = jsonarray1;
_L2:
        return jsonarray;
        Exception exception;
        exception;
        if(true) goto _L2; else goto _L1
_L1:
    }

    public JSONObject getJSONObject(int i)
    {
        Object obj = get(i);
        if(obj instanceof JSONObject)
            return (JSONObject)obj;
        else
            throw new RuntimeException((new StringBuilder()).append("JSONArray[").append(i).append("] is not a JSONObject.").toString());
    }

    public JSONObject getJSONObject(int i, JSONObject jsonobject)
    {
        JSONObject jsonobject1 = getJSONObject(i);
        jsonobject = jsonobject1;
_L2:
        return jsonobject;
        Exception exception;
        exception;
        if(true) goto _L2; else goto _L1
_L1:
    }

    public long getLong(int i)
    {
        Object obj = get(i);
        if(!(obj instanceof Number)) goto _L2; else goto _L1
_L1:
        long l = ((Number)obj).longValue();
_L4:
        return l;
_L2:
        try
        {
            l = Long.parseLong((String)obj);
        }
        catch(Exception exception)
        {
            throw new RuntimeException((new StringBuilder()).append("JSONArray[").append(i).append("] is not a number.").toString());
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public long getLong(int i, long l)
    {
        long l1 = getLong(i);
        l = l1;
_L2:
        return l;
        Exception exception;
        exception;
        if(true) goto _L2; else goto _L1
_L1:
    }

    public long[] getLongArray()
    {
        long al[] = new long[size()];
        for(int i = 0; i < size(); i++)
            al[i] = getLong(i);

        return al;
    }

    public String getString(int i)
    {
        Object obj = get(i);
        if(obj instanceof String)
            return (String)obj;
        else
            throw new RuntimeException((new StringBuilder()).append("JSONArray[").append(i).append("] not a string.").toString());
    }

    public String getString(int i, String s)
    {
        Object obj = opt(i);
        if(!JSONObject.NULL.equals(obj))
            s = obj.toString();
        return s;
    }

    public String[] getStringArray()
    {
        String as[] = new String[size()];
        for(int i = 0; i < size(); i++)
            as[i] = getString(i);

        return as;
    }

    public boolean isNull(int i)
    {
        return JSONObject.NULL.equals(opt(i));
    }

    public String join(String s)
    {
        int i = size();
        StringBuffer stringbuffer = new StringBuffer();
        for(int j = 0; j < i; j++)
        {
            if(j > 0)
                stringbuffer.append(s);
            stringbuffer.append(JSONObject.valueToString(myArrayList.get(j)));
        }

        return stringbuffer.toString();
    }

    public Object remove(int i)
    {
        Object obj = opt(i);
        myArrayList.remove(i);
        return obj;
    }

    public boolean save(File file, String s)
    {
        return save(PApplet.createWriter(file));
    }

    protected boolean save(OutputStream outputstream)
    {
        return save(PApplet.createWriter(outputstream));
    }

    public boolean save(PrintWriter printwriter)
    {
        printwriter.print(format(2));
        printwriter.flush();
        return true;
    }

    public JSONArray setBoolean(int i, boolean flag)
    {
        Boolean boolean1;
        if(flag)
            boolean1 = Boolean.TRUE;
        else
            boolean1 = Boolean.FALSE;
        return set(i, boolean1);
    }

    public JSONArray setDouble(int i, double d)
    {
        return set(i, new Double(d));
    }

    public JSONArray setFloat(int i, float f)
    {
        return setDouble(i, f);
    }

    public JSONArray setInt(int i, int j)
    {
        set(i, new Integer(j));
        return this;
    }

    public JSONArray setJSONArray(int i, JSONArray jsonarray)
    {
        set(i, jsonarray);
        return this;
    }

    public JSONArray setJSONObject(int i, JSONObject jsonobject)
    {
        set(i, jsonobject);
        return this;
    }

    public JSONArray setLong(int i, long l)
    {
        return set(i, new Long(l));
    }

    public JSONArray setString(int i, String s)
    {
        set(i, s);
        return this;
    }

    public int size()
    {
        return myArrayList.size();
    }

    public String toString()
    {
        String s;
        try
        {
            s = format(2);
        }
        catch(Exception exception)
        {
            exception = null;
        }
        return s;
    }

    protected Writer write(Writer writer)
    {
        return write(writer, -1, 0);
    }

    protected Writer write(Writer writer, int i, int j)
    {
        int k = 0;
        int l;
        int i1;
        int j1;
        boolean flag;
        try
        {
            l = size();
            writer.write(91);
        }
        // Misplaced declaration of an exception variable
        catch(Writer writer)
        {
            throw new RuntimeException(writer);
        }
        if(i == -1)
            i1 = 0;
        else
            i1 = i;
        if(l != 1) goto _L2; else goto _L1
_L1:
        JSONObject.writeValue(writer, myArrayList.get(0), i1, j);
_L4:
        writer.write(93);
        return writer;
_L2:
        if(l == 0) goto _L4; else goto _L3
_L3:
        j1 = j + i1;
        flag = false;
_L6:
        if(k >= l)
            break; /* Loop/switch isn't completed */
        if(!flag)
            break MISSING_BLOCK_LABEL_91;
        writer.write(44);
        if(i == -1)
            break MISSING_BLOCK_LABEL_102;
        writer.write(10);
        JSONObject.indent(writer, j1);
        JSONObject.writeValue(writer, myArrayList.get(k), i1, j1);
        k++;
        flag = true;
        if(true) goto _L6; else goto _L5
_L5:
        if(i == -1)
            break MISSING_BLOCK_LABEL_146;
        writer.write(10);
        JSONObject.indent(writer, j);
          goto _L4
    }

    private final ArrayList myArrayList;
}
