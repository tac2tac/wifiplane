// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

// Referenced classes of package android.support.v4.graphics.drawable:
//            DrawableWrapperLollipop

class DrawableCompatLollipop
{

    DrawableCompatLollipop()
    {
    }

    public static void applyTheme(Drawable drawable, android.content.res.Resources.Theme theme)
    {
        drawable.applyTheme(theme);
    }

    public static boolean canApplyTheme(Drawable drawable)
    {
        return drawable.canApplyTheme();
    }

    public static ColorFilter getColorFilter(Drawable drawable)
    {
        return drawable.getColorFilter();
    }

    public static void inflate(Drawable drawable, Resources resources, XmlPullParser xmlpullparser, AttributeSet attributeset, android.content.res.Resources.Theme theme)
        throws IOException, XmlPullParserException
    {
        drawable.inflate(resources, xmlpullparser, attributeset, theme);
    }

    public static void setHotspot(Drawable drawable, float f, float f1)
    {
        drawable.setHotspot(f, f1);
    }

    public static void setHotspotBounds(Drawable drawable, int i, int j, int k, int l)
    {
        drawable.setHotspotBounds(i, j, k, l);
    }

    public static void setTint(Drawable drawable, int i)
    {
        drawable.setTint(i);
    }

    public static void setTintList(Drawable drawable, ColorStateList colorstatelist)
    {
        drawable.setTintList(colorstatelist);
    }

    public static void setTintMode(Drawable drawable, android.graphics.PorterDuff.Mode mode)
    {
        drawable.setTintMode(mode);
    }

    public static Drawable wrapForTinting(Drawable drawable)
    {
        Object obj = drawable;
        if(!(drawable instanceof DrawableWrapperLollipop))
            obj = new DrawableWrapperLollipop(drawable);
        return ((Drawable) (obj));
    }
}
