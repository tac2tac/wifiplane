// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.event;


// Referenced classes of package processing.event:
//            Event

public class MouseEvent extends Event
{

    public MouseEvent(Object obj, long l, int i, int j, int k, int i1, 
            int j1, int k1)
    {
        super(obj, l, i, j);
        flavor = 2;
        x = k;
        y = i1;
        button = j1;
        clickCount = k1;
    }

    public int getButton()
    {
        return button;
    }

    public int getClickCount()
    {
        return clickCount;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public static final int CLICK = 3;
    public static final int DRAG = 4;
    public static final int ENTER = 6;
    public static final int EXIT = 7;
    public static final int MOVE = 5;
    public static final int PRESS = 1;
    public static final int RELEASE = 2;
    protected int button;
    protected int clickCount;
    protected int x;
    protected int y;
}
