// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.event;


// Referenced classes of package processing.event:
//            Event

public class KeyEvent extends Event
{

    public KeyEvent(Object obj, long l, int i, int j, char c, int k)
    {
        super(obj, l, i, j);
        flavor = 1;
        key = c;
        keyCode = k;
    }

    public char getKey()
    {
        return key;
    }

    public int getKeyCode()
    {
        return keyCode;
    }

    public static final int PRESS = 1;
    public static final int RELEASE = 2;
    public static final int TYPE = 3;
    char key;
    int keyCode;
}
