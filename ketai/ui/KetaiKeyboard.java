// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package ketai.ui;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import processing.core.PApplet;

public class KetaiKeyboard
{

    public KetaiKeyboard()
    {
    }

    public static void hide(PApplet papplet)
    {
        ((InputMethodManager)papplet.getActivity().getSystemService("input_method")).hideSoftInputFromWindow(papplet.getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    public static void show(PApplet papplet)
    {
        ((InputMethodManager)papplet.getActivity().getSystemService("input_method")).showSoftInput(papplet.getActivity().getCurrentFocus(), 0);
    }

    public static void toggle(PApplet papplet)
    {
        ((InputMethodManager)papplet.getActivity().getSystemService("input_method")).toggleSoftInput(0, 0);
    }
}
