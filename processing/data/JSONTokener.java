// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.data;

import java.io.*;

// Referenced classes of package processing.data:
//            JSONObject, JSONArray

class JSONTokener
{

    public JSONTokener(InputStream inputstream)
    {
        this(((Reader) (new InputStreamReader(inputstream))));
    }

    public JSONTokener(Reader reader1)
    {
        if(!reader1.markSupported())
            reader1 = new BufferedReader(reader1);
        reader = reader1;
        eof = false;
        usePrevious = false;
        previous = (char)0;
        index = 0L;
        character = 1L;
        line = 1L;
    }

    public JSONTokener(String s)
    {
        this(((Reader) (new StringReader(s))));
    }

    public static int dehexchar(char c)
    {
        if(c >= '0' && c <= '9')
            c -= 48;
        else
        if(c >= 'A' && c <= 'F')
            c -= 55;
        else
        if(c >= 'a' && c <= 'f')
            c -= 87;
        else
            c = '\uFFFF';
        return c;
    }

    public void back()
    {
        if(usePrevious || index <= 0L)
        {
            throw new RuntimeException("Stepping back two steps is not supported");
        } else
        {
            index = index - 1L;
            character = character - 1L;
            usePrevious = true;
            eof = false;
            return;
        }
    }

    public boolean end()
    {
        boolean flag;
        if(eof && !usePrevious)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public boolean more()
    {
        next();
        boolean flag;
        if(end())
        {
            flag = false;
        } else
        {
            back();
            flag = true;
        }
        return flag;
    }

    public char next()
    {
        long l = 0L;
        int i = 0;
        if(usePrevious)
        {
            usePrevious = false;
            i = previous;
        } else
        {
            int j;
            try
            {
                j = reader.read();
            }
            catch(IOException ioexception)
            {
                throw new RuntimeException(ioexception);
            }
            if(j <= 0)
                eof = true;
            else
                i = j;
        }
        index = index + 1L;
        if(previous == '\r')
        {
            line = line + 1L;
            if(i != 10)
                l = 1L;
            character = l;
        } else
        if(i == 10)
        {
            line = 1L + line;
            character = 0L;
        } else
        {
            character = character + 1L;
        }
        previous = (char)i;
        return previous;
    }

    public char next(char c)
    {
        char c1 = next();
        if(c1 != c)
            throw new RuntimeException((new StringBuilder()).append("Expected '").append(c).append("' and instead saw '").append(c1).append("'").toString());
        else
            return c1;
    }

    public String next(int i)
    {
        Object obj;
        if(i == 0)
        {
            obj = "";
        } else
        {
            obj = new char[i];
            for(int j = 0; j < i; j++)
            {
                obj[j] = next();
                if(end())
                    throw new RuntimeException("Substring bounds error");
            }

            obj = new String(((char []) (obj)));
        }
        return ((String) (obj));
    }

    public char nextClean()
    {
        char c;
        do
            c = next();
        while(c != 0 && c <= ' ');
        return c;
    }

    public String nextString(char c)
    {
        StringBuffer stringbuffer = new StringBuffer();
        do
        {
            char c1 = next();
            switch(c1)
            {
            default:
                if(c1 == c)
                    return stringbuffer.toString();
                break;

            case 0: // '\0'
            case 10: // '\n'
            case 13: // '\r'
                throw new RuntimeException("Unterminated string");

            case 92: // '\\'
                c1 = next();
                switch(c1)
                {
                default:
                    throw new RuntimeException("Illegal escape.");

                case 98: // 'b'
                    stringbuffer.append('\b');
                    break;

                case 116: // 't'
                    stringbuffer.append('\t');
                    break;

                case 110: // 'n'
                    stringbuffer.append('\n');
                    break;

                case 102: // 'f'
                    stringbuffer.append('\f');
                    break;

                case 114: // 'r'
                    stringbuffer.append('\r');
                    break;

                case 117: // 'u'
                    stringbuffer.append((char)Integer.parseInt(next(4), 16));
                    break;

                case 34: // '"'
                case 39: // '\''
                case 47: // '/'
                case 92: // '\\'
                    stringbuffer.append(c1);
                    break;
                }
                continue;
            }
            stringbuffer.append(c1);
        } while(true);
    }

    public String nextTo(char c)
    {
        StringBuffer stringbuffer = new StringBuffer();
        do
        {
            char c1 = next();
            if(c1 == c || c1 == 0 || c1 == '\n' || c1 == '\r')
            {
                if(c1 != 0)
                    back();
                return stringbuffer.toString().trim();
            }
            stringbuffer.append(c1);
        } while(true);
    }

    public String nextTo(String s)
    {
        StringBuffer stringbuffer = new StringBuffer();
        do
        {
            char c = next();
            if(s.indexOf(c) >= 0 || c == 0 || c == '\n' || c == '\r')
            {
                if(c != 0)
                    back();
                return stringbuffer.toString().trim();
            }
            stringbuffer.append(c);
        } while(true);
    }

    public Object nextValue()
    {
        char c = nextClean();
        c;
        JVM INSTR lookupswitch 4: default 48
    //                   34: 87
    //                   39: 87
    //                   91: 111
    //                   123: 95;
           goto _L1 _L2 _L2 _L3 _L4
_L1:
        Object obj = new StringBuffer();
        char c1;
        for(; c >= ' ' && ",:]}/\\\"[{;=#".indexOf(c) < 0; c = c1)
        {
            ((StringBuffer) (obj)).append(c);
            c1 = next();
        }

        back();
        obj = ((StringBuffer) (obj)).toString().trim();
        if("".equals(obj))
            throw new RuntimeException("Missing value");
        obj = JSONObject.stringToValue(((String) (obj)));
          goto _L5
_L2:
        obj = nextString(c);
_L7:
        return obj;
_L4:
        back();
        obj = new JSONObject(this);
        continue; /* Loop/switch isn't completed */
_L3:
        back();
        obj = new JSONArray(this);
        continue; /* Loop/switch isn't completed */
_L5:
        if(true) goto _L7; else goto _L6
_L6:
    }

    public char skipTo(char c)
    {
        long l;
        long l1;
        long l2;
        l = index;
        l1 = character;
        l2 = line;
        reader.mark(0xf4240);
_L2:
        char c1 = next();
        if(c1 != 0)
            continue; /* Loop/switch isn't completed */
        try
        {
            reader.reset();
            index = l;
            character = l1;
            line = l2;
        }
        catch(IOException ioexception)
        {
            throw new RuntimeException(ioexception);
        }
        return c1;
        if(c1 != c) goto _L2; else goto _L1
_L1:
        back();
        break MISSING_BLOCK_LABEL_61;
        if(true) goto _L2; else goto _L3
_L3:
    }

    public String toString()
    {
        return (new StringBuilder()).append(" at ").append(index).append(" [character ").append(character).append(" line ").append(line).append("]").toString();
    }

    private long character;
    private boolean eof;
    private long index;
    private long line;
    private char previous;
    private Reader reader;
    private boolean usePrevious;
}
