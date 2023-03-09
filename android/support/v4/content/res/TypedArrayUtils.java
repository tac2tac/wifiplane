// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.content.res;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

public class TypedArrayUtils
{

    public TypedArrayUtils()
    {
    }

    public static boolean getBoolean(TypedArray typedarray, int i, int j, boolean flag)
    {
        return typedarray.getBoolean(i, typedarray.getBoolean(j, flag));
    }

    public static Drawable getDrawable(TypedArray typedarray, int i, int j)
    {
        Drawable drawable = typedarray.getDrawable(i);
        Drawable drawable1 = drawable;
        if(drawable == null)
            drawable1 = typedarray.getDrawable(j);
        return drawable1;
    }

    public static int getInt(TypedArray typedarray, int i, int j, int k)
    {
        return typedarray.getInt(i, typedarray.getInt(j, k));
    }

    public static int getResourceId(TypedArray typedarray, int i, int j, int k)
    {
        return typedarray.getResourceId(i, typedarray.getResourceId(j, k));
    }

    public static String getString(TypedArray typedarray, int i, int j)
    {
        String s = typedarray.getString(i);
        String s1 = s;
        if(s == null)
            s1 = typedarray.getString(j);
        return s1;
    }

    public static CharSequence[] getTextArray(TypedArray typedarray, int i, int j)
    {
        CharSequence acharsequence[] = typedarray.getTextArray(i);
        CharSequence acharsequence1[] = acharsequence;
        if(acharsequence == null)
            acharsequence1 = typedarray.getTextArray(j);
        return acharsequence1;
    }
}
