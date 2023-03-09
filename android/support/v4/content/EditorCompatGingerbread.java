// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.content;


class EditorCompatGingerbread
{

    EditorCompatGingerbread()
    {
    }

    public static void apply(android.content.SharedPreferences.Editor editor)
    {
        editor.apply();
_L1:
        return;
        AbstractMethodError abstractmethoderror;
        abstractmethoderror;
        editor.commit();
          goto _L1
    }
}
