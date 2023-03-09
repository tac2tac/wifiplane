// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.data;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import processing.core.PApplet;

// Referenced classes of package processing.data:
//            JSONTokener, FloatDict, IntDict, StringDict, 
//            JSONArray

public class JSONObject
{
    private static final class Null
    {

        protected final Object clone()
        {
            return this;
        }

        public boolean equals(Object obj)
        {
            boolean flag;
            if(obj == null || obj == this)
                flag = true;
            else
                flag = false;
            return flag;
        }

        public int hashCode()
        {
            return super.hashCode();
        }

        public String toString()
        {
            return "null";
        }

        private Null()
        {
        }

    }


    public JSONObject()
    {
        map = new HashMap();
    }

    public JSONObject(Reader reader)
    {
        this(new JSONTokener(reader));
    }

    protected JSONObject(Object obj)
    {
        this();
        populateMap(obj);
    }

    protected JSONObject(HashMap hashmap)
    {
        map = new HashMap();
        if(hashmap != null)
        {
            Iterator iterator = hashmap.entrySet().iterator();
            do
            {
                if(!iterator.hasNext())
                    break;
                java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
                Object obj = entry.getValue();
                if(obj != null)
                    hashmap.put((String)entry.getKey(), wrap(obj));
            } while(true);
        }
    }

    public JSONObject(FloatDict floatdict)
    {
        map = new HashMap();
        for(int i = 0; i < floatdict.size(); i++)
            setFloat(floatdict.key(i), floatdict.value(i));

    }

    public JSONObject(IntDict intdict)
    {
        map = new HashMap();
        for(int i = 0; i < intdict.size(); i++)
            setInt(intdict.key(i), intdict.value(i));

    }

    protected JSONObject(JSONTokener jsontokener)
    {
        this();
        if(jsontokener.nextClean() != '{')
            throw new RuntimeException("A JSONObject text must begin with '{'");
          goto _L1
_L11:
        jsontokener.back();
_L1:
        jsontokener.nextClean();
        JVM INSTR lookupswitch 2: default 56
    //                   0: 150
    //                   125: 185;
           goto _L2 _L3 _L4
_L2:
        String s;
        char c;
        jsontokener.back();
        s = jsontokener.nextValue().toString();
        c = jsontokener.nextClean();
        if(c != '=') goto _L6; else goto _L5
_L5:
        if(jsontokener.next() != '>')
            jsontokener.back();
_L10:
        putOnce(s, jsontokener.nextValue());
        jsontokener.nextClean();
        JVM INSTR lookupswitch 3: default 140
    //                   44: 176
    //                   59: 176
    //                   125: 185;
           goto _L7 _L8 _L8 _L4
_L7:
        throw new RuntimeException("Expected a ',' or '}'");
_L3:
        throw new RuntimeException("A JSONObject text must end with '}'");
_L6:
        if(c == ':') goto _L10; else goto _L9
_L9:
        throw new RuntimeException("Expected a ':' after a key");
_L8:
        if(jsontokener.nextClean() != '}') goto _L11; else goto _L4
_L4:
    }

    public JSONObject(StringDict stringdict)
    {
        map = new HashMap();
        for(int i = 0; i < stringdict.size(); i++)
            setString(stringdict.key(i), stringdict.value(i));

    }

    protected static String doubleToString(double d)
    {
        if(!Double.isInfinite(d) && !Double.isNaN(d)) goto _L2; else goto _L1
_L1:
        String s = "null";
_L4:
        return s;
_L2:
        String s1 = Double.toString(d);
        s = s1;
        if(s1.indexOf('.') > 0)
        {
            s = s1;
            if(s1.indexOf('e') < 0)
            {
                s = s1;
                if(s1.indexOf('E') < 0)
                {
                    for(; s1.endsWith("0"); s1 = s1.substring(0, s1.length() - 1));
                    s = s1;
                    if(s1.endsWith("."))
                        s = s1.substring(0, s1.length() - 1);
                }
            }
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    private Object get(String s)
    {
        if(s == null)
            throw new RuntimeException("Null key.");
        Object obj = opt(s);
        if(obj == null)
            throw new RuntimeException((new StringBuilder()).append("JSONObject[").append(quote(s)).append("] not found.").toString());
        else
            return obj;
    }

    static final void indent(Writer writer, int i)
        throws IOException
    {
        for(int j = 0; j < i; j++)
            writer.write(32);

    }

    private static String numberToString(Number number)
    {
        if(number == null)
            throw new RuntimeException("Null pointer");
        testValidity(number);
        String s = number.toString();
        number = s;
        if(s.indexOf('.') > 0)
        {
            number = s;
            if(s.indexOf('e') < 0)
            {
                number = s;
                if(s.indexOf('E') < 0)
                {
                    for(; s.endsWith("0"); s = s.substring(0, s.length() - 1));
                    number = s;
                    if(s.endsWith("."))
                        number = s.substring(0, s.length() - 1);
                }
            }
        }
        return number;
    }

    private Object opt(String s)
    {
        if(s == null)
            s = null;
        else
            s = ((String) (map.get(s)));
        return s;
    }

    public static JSONObject parse(String s)
    {
        return new JSONObject(new JSONTokener(s));
    }

    private void populateMap(Object obj)
    {
        Object obj1;
        String s;
        boolean flag = false;
        obj1 = obj.getClass();
        int i;
        Method amethod[];
        Object obj2;
        if(((Class) (obj1)).getClassLoader() != null)
            i = 1;
        else
            i = 0;
        if(i != 0)
        {
            amethod = ((Class) (obj1)).getMethods();
            i = ((flag) ? 1 : 0);
        } else
        {
            amethod = ((Class) (obj1)).getDeclaredMethods();
            i = ((flag) ? 1 : 0);
        }
_L15:
        if(i >= amethod.length)
            break MISSING_BLOCK_LABEL_281;
        obj2 = amethod[i];
        if(!Modifier.isPublic(((Method) (obj2)).getModifiers())) goto _L2; else goto _L1
_L1:
        s = ((Method) (obj2)).getName();
        obj1 = "";
        if(!s.startsWith("get")) goto _L4; else goto _L3
_L3:
        if(!"getClass".equals(s) && !"getDeclaringClass".equals(s)) goto _L6; else goto _L5
_L5:
        obj1 = "";
_L10:
        if(((String) (obj1)).length() <= 0 || !Character.isUpperCase(((String) (obj1)).charAt(0)) || ((Method) (obj2)).getParameterTypes().length != 0) goto _L2; else goto _L7
_L7:
        if(((String) (obj1)).length() != 1) goto _L9; else goto _L8
_L8:
        obj1 = ((String) (obj1)).toLowerCase();
_L13:
        obj2 = ((Method) (obj2)).invoke(obj, (Object[])null);
        if(obj2 != null)
            try
            {
                map.put(obj1, wrap(obj2));
            }
            catch(Exception exception) { }
_L2:
        i++;
        continue; /* Loop/switch isn't completed */
_L6:
        obj1 = s.substring(3);
          goto _L10
_L4:
        if(!s.startsWith("is")) goto _L10; else goto _L11
_L11:
        obj1 = s.substring(2);
          goto _L10
_L9:
        if(Character.isUpperCase(((String) (obj1)).charAt(1))) goto _L13; else goto _L12
_L12:
        StringBuilder stringbuilder = JVM INSTR new #210 <Class StringBuilder>;
        stringbuilder.StringBuilder();
        obj1 = stringbuilder.append(((String) (obj1)).substring(0, 1).toLowerCase()).append(((String) (obj1)).substring(1)).toString();
          goto _L13
        return;
        if(true) goto _L15; else goto _L14
_L14:
    }

    private JSONObject put(String s, Object obj)
    {
        if(s == null)
            throw new RuntimeException("Null key.");
        if(obj != null)
        {
            testValidity(obj);
            String s1 = (String)keyPool.get(s);
            if(s1 == null)
            {
                if(keyPool.size() >= 100)
                    keyPool = new HashMap(100);
                keyPool.put(s, s);
            } else
            {
                s = s1;
            }
            map.put(s, obj);
        } else
        {
            remove(s);
        }
        return this;
    }

    private JSONObject putOnce(String s, Object obj)
    {
        if(s != null && obj != null)
        {
            if(opt(s) != null)
                throw new RuntimeException((new StringBuilder()).append("Duplicate key \"").append(s).append("\"").toString());
            put(s, obj);
        }
        return this;
    }

    protected static Writer quote(String s, Writer writer)
        throws IOException
    {
        if(s != null && s.length() != 0) goto _L2; else goto _L1
_L1:
        writer.write("\"\"");
_L4:
        return writer;
_L2:
label0:
        {
label1:
            {
label2:
                {
label3:
                    {
label4:
                        {
label5:
                            {
label6:
                                {
                                    int i = s.length();
                                    writer.write(34);
                                    int j = 0;
                                    char c = '\0';
label7:
                                    do
                                    {
                                        {
                                            if(j >= i)
                                                break label0;
                                            char c1 = s.charAt(j);
                                            switch(c1)
                                            {
                                            default:
                                                if(c1 < ' ' || c1 >= '\200' && c1 < '\240' || c1 >= '\u2000' && c1 < '\u2100')
                                                {
                                                    writer.write("\\u");
                                                    String s1 = Integer.toHexString(c1);
                                                    writer.write("0000", 0, 4 - s1.length());
                                                    writer.write(s1);
                                                } else
                                                {
                                                    writer.write(c1);
                                                }
                                                break;

                                            case 8: // '\b'
                                                break label5;

                                            case 9: // '\t'
                                                break label4;

                                            case 10: // '\n'
                                                break label3;

                                            case 12: // '\f'
                                                break label2;

                                            case 13: // '\r'
                                                break label1;

                                            case 34: // '"'
                                            case 92: // '\\'
                                                break label7;

                                            case 47: // '/'
                                                break label6;
                                            }
                                        }
                                        j++;
                                        c = c1;
                                    } while(true);
                                    writer.write(92);
                                    writer.write(c1);
                                    break MISSING_BLOCK_LABEL_198;
                                }
                                if(c == '<')
                                    writer.write(92);
                                writer.write(c1);
                                break MISSING_BLOCK_LABEL_198;
                            }
                            writer.write("\\b");
                            break MISSING_BLOCK_LABEL_198;
                        }
                        writer.write("\\t");
                        break MISSING_BLOCK_LABEL_198;
                    }
                    writer.write("\\n");
                    break MISSING_BLOCK_LABEL_198;
                }
                writer.write("\\f");
                break MISSING_BLOCK_LABEL_198;
            }
            writer.write("\\r");
            break MISSING_BLOCK_LABEL_198;
        }
        writer.write(34);
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected static String quote(String s)
    {
        StringWriter stringwriter = new StringWriter();
        if(true) goto _L2; else goto _L1
_L1:
        stringbuffer;
        JVM INSTR monitorenter ;
_L2:
        synchronized(stringwriter.getBuffer())
        {
            s = quote(s, ((Writer) (stringwriter))).toString();
        }
_L4:
        return s;
        s;
        s = "";
        stringbuffer;
        JVM INSTR monitorexit ;
        if(true) goto _L4; else goto _L3
_L3:
        s;
        stringbuffer;
        JVM INSTR monitorexit ;
        throw s;
    }

    protected static Object stringToValue(String s)
    {
        if(!s.equals("")) goto _L2; else goto _L1
_L1:
        Object obj = s;
_L4:
        return obj;
_L2:
        if(s.equalsIgnoreCase("true"))
        {
            obj = Boolean.TRUE;
            continue; /* Loop/switch isn't completed */
        }
        if(s.equalsIgnoreCase("false"))
        {
            obj = Boolean.FALSE;
            continue; /* Loop/switch isn't completed */
        }
        if(s.equalsIgnoreCase("null"))
        {
            obj = NULL;
            continue; /* Loop/switch isn't completed */
        }
        char c = s.charAt(0);
        if((c < '0' || c > '9') && c != '.' && c != '-')
        {
            obj = s;
            if(c != '+')
                continue; /* Loop/switch isn't completed */
        }
        Double double1;
        if(s.indexOf('.') <= -1 && s.indexOf('e') <= -1 && s.indexOf('E') <= -1)
            break MISSING_BLOCK_LABEL_160;
        double1 = Double.valueOf(s);
        obj = s;
        if(double1.isInfinite())
            continue; /* Loop/switch isn't completed */
        obj = s;
        if(!double1.isNaN())
            obj = double1;
        continue; /* Loop/switch isn't completed */
        try
        {
            obj = JVM INSTR new #392 <Class Long>;
            ((Long) (obj)).Long(s);
            if(((Long) (obj)).longValue() == (long)((Long) (obj)).intValue())
                obj = new Integer(((Long) (obj)).intValue());
        }
        catch(Exception exception)
        {
            exception = s;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected static void testValidity(Object obj)
    {
        if(obj != null)
            if(obj instanceof Double)
            {
                if(((Double)obj).isInfinite() || ((Double)obj).isNaN())
                    throw new RuntimeException("JSON does not allow non-finite numbers.");
            } else
            if((obj instanceof Float) && (((Float)obj).isInfinite() || ((Float)obj).isNaN()))
                throw new RuntimeException("JSON does not allow non-finite numbers.");
    }

    protected static String valueToString(Object obj)
    {
        if(obj == null || obj.equals(null))
            obj = "null";
        else
        if(obj instanceof Number)
            obj = numberToString((Number)obj);
        else
        if((obj instanceof Boolean) || (obj instanceof JSONObject) || (obj instanceof JSONArray))
            obj = obj.toString();
        else
        if(obj instanceof Map)
            obj = (new JSONObject((Map)obj)).toString();
        else
        if(obj instanceof Collection)
            obj = (new JSONArray((Collection)obj)).toString();
        else
        if(obj.getClass().isArray())
            obj = (new JSONArray(obj)).toString();
        else
            obj = quote(obj.toString());
        return ((String) (obj));
    }

    protected static Object wrap(Object obj)
    {
label0:
        {
            {
                if(obj != null)
                    break label0;
                Object obj1;
                try
                {
                    obj1 = NULL;
                }
                // Misplaced declaration of an exception variable
                catch(Object obj)
                {
                    obj1 = null;
                }
            }
            return obj1;
        }
        obj1 = obj;
        if(obj instanceof JSONObject)
            break MISSING_BLOCK_LABEL_8;
        obj1 = obj;
        if(obj instanceof JSONArray)
            break MISSING_BLOCK_LABEL_8;
        obj1 = obj;
        if(NULL.equals(obj))
            break MISSING_BLOCK_LABEL_8;
        obj1 = obj;
        if(obj instanceof Byte)
            break MISSING_BLOCK_LABEL_8;
        obj1 = obj;
        if(obj instanceof Character)
            break MISSING_BLOCK_LABEL_8;
        obj1 = obj;
        if(obj instanceof Short)
            break MISSING_BLOCK_LABEL_8;
        obj1 = obj;
        if(obj instanceof Integer)
            break MISSING_BLOCK_LABEL_8;
        obj1 = obj;
        if(obj instanceof Long)
            break MISSING_BLOCK_LABEL_8;
        obj1 = obj;
        if(obj instanceof Boolean)
            break MISSING_BLOCK_LABEL_8;
        obj1 = obj;
        if(obj instanceof Float)
            break MISSING_BLOCK_LABEL_8;
        obj1 = obj;
        if(obj instanceof Double)
            break MISSING_BLOCK_LABEL_8;
        obj1 = obj;
label1:
        {
            if(obj instanceof String)
                break MISSING_BLOCK_LABEL_8;
            if(!(obj instanceof Collection))
                break label1;
            obj1 = new JSONArray((Collection)obj);
        }
        break MISSING_BLOCK_LABEL_8;
label2:
        {
            if(!obj.getClass().isArray())
                break label2;
            obj1 = new JSONArray(obj);
        }
        break MISSING_BLOCK_LABEL_8;
label3:
        {
            if(!(obj instanceof Map))
                break label3;
            obj1 = new JSONObject((Map)obj);
        }
        break MISSING_BLOCK_LABEL_8;
        obj1 = obj.getClass().getPackage();
        if(obj1 == null)
            break MISSING_BLOCK_LABEL_242;
        obj1 = ((Package) (obj1)).getName();
_L2:
        if(!((String) (obj1)).startsWith("java.") && !((String) (obj1)).startsWith("javax.") && obj.getClass().getClassLoader() != null)
            break MISSING_BLOCK_LABEL_249;
        obj1 = obj.toString();
        break MISSING_BLOCK_LABEL_8;
        obj1 = "";
        if(true) goto _L2; else goto _L1
_L1:
        obj1 = new JSONObject(obj);
        break MISSING_BLOCK_LABEL_8;
    }

    static final Writer writeValue(Writer writer, Object obj, int i, int j)
        throws IOException
    {
        if(obj == null || obj.equals(null))
            writer.write("null");
        else
        if(obj instanceof JSONObject)
            ((JSONObject)obj).write(writer, i, j);
        else
        if(obj instanceof JSONArray)
            ((JSONArray)obj).write(writer, i, j);
        else
        if(obj instanceof Map)
            (new JSONObject((Map)obj)).write(writer, i, j);
        else
        if(obj instanceof Collection)
            (new JSONArray((Collection)obj)).write(writer, i, j);
        else
        if(obj.getClass().isArray())
            (new JSONArray(obj)).write(writer, i, j);
        else
        if(obj instanceof Number)
            writer.write(numberToString((Number)obj));
        else
        if(obj instanceof Boolean)
            writer.write(obj.toString());
        else
            quote(obj.toString(), writer);
        return writer;
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

    public boolean getBoolean(String s)
    {
        Object obj = get(s);
        boolean flag;
        if(obj.equals(Boolean.FALSE) || (obj instanceof String) && ((String)obj).equalsIgnoreCase("false"))
            flag = false;
        else
        if(obj.equals(Boolean.TRUE) || (obj instanceof String) && ((String)obj).equalsIgnoreCase("true"))
            flag = true;
        else
            throw new RuntimeException((new StringBuilder()).append("JSONObject[").append(quote(s)).append("] is not a Boolean.").toString());
        return flag;
    }

    public boolean getBoolean(String s, boolean flag)
    {
        boolean flag1 = getBoolean(s);
        flag = flag1;
_L2:
        return flag;
        s;
        if(true) goto _L2; else goto _L1
_L1:
    }

    public double getDouble(String s)
    {
        Object obj = get(s);
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
            throw new RuntimeException((new StringBuilder()).append("JSONObject[").append(quote(s)).append("] is not a number.").toString());
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public double getDouble(String s, double d)
    {
        double d1 = getDouble(s);
        d = d1;
_L2:
        return d;
        s;
        if(true) goto _L2; else goto _L1
_L1:
    }

    public float getFloat(String s)
    {
        return (float)getDouble(s);
    }

    public float getFloat(String s, float f)
    {
        float f1 = getFloat(s);
        f = f1;
_L2:
        return f;
        s;
        if(true) goto _L2; else goto _L1
_L1:
    }

    public int getInt(String s)
    {
        Object obj = get(s);
        if(!(obj instanceof Number)) goto _L2; else goto _L1
_L1:
        int i = ((Number)obj).intValue();
_L4:
        return i;
_L2:
        try
        {
            i = Integer.parseInt((String)obj);
        }
        catch(Exception exception)
        {
            throw new RuntimeException((new StringBuilder()).append("JSONObject[").append(quote(s)).append("] is not an int.").toString());
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public int getInt(String s, int i)
    {
        int j = getInt(s);
        i = j;
_L2:
        return i;
        s;
        if(true) goto _L2; else goto _L1
_L1:
    }

    public JSONArray getJSONArray(String s)
    {
        Object obj = get(s);
        if(obj instanceof JSONArray)
            return (JSONArray)obj;
        else
            throw new RuntimeException((new StringBuilder()).append("JSONObject[").append(quote(s)).append("] is not a JSONArray.").toString());
    }

    public JSONObject getJSONObject(String s)
    {
        Object obj = get(s);
        if(obj instanceof JSONObject)
            return (JSONObject)obj;
        else
            throw new RuntimeException((new StringBuilder()).append("JSONObject[").append(quote(s)).append("] is not a JSONObject.").toString());
    }

    public long getLong(String s)
    {
        Object obj = get(s);
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
            throw new RuntimeException((new StringBuilder()).append("JSONObject[").append(quote(s)).append("] is not a long.").toString(), exception);
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public long getLong(String s, long l)
    {
        long l1 = getLong(s);
        l = l1;
_L2:
        return l;
        s;
        if(true) goto _L2; else goto _L1
_L1:
    }

    public String getString(String s)
    {
        Object obj = get(s);
        if(obj instanceof String)
            return (String)obj;
        else
            throw new RuntimeException((new StringBuilder()).append("JSONObject[").append(quote(s)).append("] not a string.").toString());
    }

    public String getString(String s, String s1)
    {
        s = ((String) (opt(s)));
        if(!NULL.equals(s))
            s1 = s.toString();
        return s1;
    }

    public boolean hasKey(String s)
    {
        return map.containsKey(s);
    }

    public boolean isNull(String s)
    {
        return NULL.equals(opt(s));
    }

    public Iterator keyIterator()
    {
        return map.keySet().iterator();
    }

    public Set keys()
    {
        return map.keySet();
    }

    public Object remove(String s)
    {
        return map.remove(s);
    }

    public boolean save(File file, String s)
    {
        return write(PApplet.createWriter(file));
    }

    public JSONObject setBoolean(String s, boolean flag)
    {
        Boolean boolean1;
        if(flag)
            boolean1 = Boolean.TRUE;
        else
            boolean1 = Boolean.FALSE;
        put(s, boolean1);
        return this;
    }

    public JSONObject setDouble(String s, double d)
    {
        put(s, new Double(d));
        return this;
    }

    public JSONObject setFloat(String s, float f)
    {
        put(s, new Double(f));
        return this;
    }

    public JSONObject setInt(String s, int i)
    {
        put(s, new Integer(i));
        return this;
    }

    public JSONObject setJSONArray(String s, JSONArray jsonarray)
    {
        return put(s, jsonarray);
    }

    public JSONObject setJSONObject(String s, JSONObject jsonobject)
    {
        return put(s, jsonobject);
    }

    public JSONObject setLong(String s, long l)
    {
        put(s, new Long(l));
        return this;
    }

    public JSONObject setString(String s, String s1)
    {
        return put(s, s1);
    }

    public int size()
    {
        return map.size();
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

    protected Writer write(Writer writer, int i, int j)
    {
        boolean flag = false;
        int k;
        Iterator iterator;
        int l;
        Object obj;
        try
        {
            k = size();
            iterator = keyIterator();
            writer.write(123);
        }
        // Misplaced declaration of an exception variable
        catch(Writer writer)
        {
            throw new RuntimeException(writer);
        }
        if(i == -1)
            l = 0;
        else
            l = i;
        if(k != 1) goto _L2; else goto _L1
_L1:
        obj = iterator.next();
        writer.write(quote(obj.toString()));
        writer.write(58);
        if(l <= 0)
            break MISSING_BLOCK_LABEL_73;
        writer.write(32);
        writeValue(writer, map.get(obj), l, j);
_L4:
        writer.write(125);
        return writer;
_L2:
        if(k == 0) goto _L4; else goto _L3
_L3:
        k = j + l;
_L6:
        if(!iterator.hasNext())
            break; /* Loop/switch isn't completed */
        obj = iterator.next();
        if(!flag)
            break MISSING_BLOCK_LABEL_145;
        writer.write(44);
        if(i == -1)
            break MISSING_BLOCK_LABEL_156;
        writer.write(10);
        indent(writer, k);
        writer.write(quote(obj.toString()));
        writer.write(58);
        if(l <= 0)
            break MISSING_BLOCK_LABEL_191;
        writer.write(32);
        writeValue(writer, map.get(obj), l, k);
        flag = true;
        if(true) goto _L6; else goto _L5
_L5:
        if(i == -1)
            break MISSING_BLOCK_LABEL_226;
        writer.write(10);
        indent(writer, j);
          goto _L4
    }

    public boolean write(PrintWriter printwriter)
    {
        printwriter.print(format(2));
        printwriter.flush();
        return true;
    }

    public static final Object NULL = new Null();
    private static HashMap keyPool = new HashMap(100);
    private static final int keyPoolSize = 100;
    private final HashMap map;

}
