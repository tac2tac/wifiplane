// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.graphics.drawable;

import android.graphics.drawable.Drawable;

// Referenced classes of package android.support.v4.graphics.drawable:
//            DrawableWrapperEclair

class DrawableCompatEclair
{

    DrawableCompatEclair()
    {
    }

    public static Drawable wrapForTinting(Drawable drawable)
    {
        Object obj = drawable;
        if(!(drawable instanceof DrawableWrapperEclair))
            obj = new DrawableWrapperEclair(drawable);
        return ((Drawable) (obj));
    }
}
