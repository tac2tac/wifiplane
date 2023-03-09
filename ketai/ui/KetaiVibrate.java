// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package ketai.ui;

import android.app.Activity;
import android.os.Vibrator;
import processing.core.PApplet;

public class KetaiVibrate
{

    public KetaiVibrate(PApplet papplet)
    {
        parent = papplet;
        vibe = (Vibrator)parent.getActivity().getSystemService("vibrator");
    }

    public boolean hasVibrator()
    {
        return vibe.hasVibrator();
    }

    public void stop()
    {
        vibe.cancel();
    }

    public void vibrate()
    {
        long al[] = new long[2];
        al[1] = 0x7fffffffffffffffL;
        vibe.vibrate(al, 0);
    }

    public void vibrate(long l)
    {
        vibe.vibrate(l);
    }

    public void vibrate(long al[], int i)
    {
        vibe.vibrate(al, i);
    }

    private PApplet parent;
    private Vibrator vibe;
}
