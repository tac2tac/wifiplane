// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.event;


public class Event
{

    public Event(Object obj, long l, int i, int j)
    {
        nativeObject = obj;
        millis = l;
        action = i;
        modifiers = j;
    }

    public int getAction()
    {
        return action;
    }

    public int getFlavor()
    {
        return flavor;
    }

    public long getMillis()
    {
        return millis;
    }

    public int getModifiers()
    {
        return modifiers;
    }

    public Object getNative()
    {
        return nativeObject;
    }

    public boolean isAltDown()
    {
        boolean flag;
        if((modifiers & 8) != 0)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public boolean isControlDown()
    {
        boolean flag;
        if((modifiers & 2) != 0)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public boolean isMetaDown()
    {
        boolean flag;
        if((modifiers & 4) != 0)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public boolean isShiftDown()
    {
        boolean flag;
        if((modifiers & 1) != 0)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public static final int ALT = 8;
    public static final int CTRL = 2;
    public static final int KEY = 1;
    public static final int META = 4;
    public static final int MOUSE = 2;
    public static final int SHIFT = 1;
    public static final int TOUCH = 3;
    protected int action;
    protected int flavor;
    protected long millis;
    protected int modifiers;
    protected Object nativeObject;
}
