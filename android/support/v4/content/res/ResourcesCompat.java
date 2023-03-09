// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

// Referenced classes of package android.support.v4.content.res:
//            ResourcesCompatApi23, ResourcesCompatApi21, ResourcesCompatIcsMr1

public final class ResourcesCompat
{

    private ResourcesCompat()
    {
    }

    public static int getColor(Resources resources, int i, android.content.res.Resources.Theme theme)
        throws android.content.res.Resources.NotFoundException
    {
        if(android.os.Build.VERSION.SDK_INT >= 23)
            i = ResourcesCompatApi23.getColor(resources, i, theme);
        else
            i = resources.getColor(i);
        return i;
    }

    public static ColorStateList getColorStateList(Resources resources, int i, android.content.res.Resources.Theme theme)
        throws android.content.res.Resources.NotFoundException
    {
        if(android.os.Build.VERSION.SDK_INT >= 23)
            resources = ResourcesCompatApi23.getColorStateList(resources, i, theme);
        else
            resources = resources.getColorStateList(i);
        return resources;
    }

    public static Drawable getDrawable(Resources resources, int i, android.content.res.Resources.Theme theme)
        throws android.content.res.Resources.NotFoundException
    {
        if(android.os.Build.VERSION.SDK_INT >= 21)
            resources = ResourcesCompatApi21.getDrawable(resources, i, theme);
        else
            resources = resources.getDrawable(i);
        return resources;
    }

    public static Drawable getDrawableForDensity(Resources resources, int i, int j, android.content.res.Resources.Theme theme)
        throws android.content.res.Resources.NotFoundException
    {
        if(android.os.Build.VERSION.SDK_INT >= 21)
            resources = ResourcesCompatApi21.getDrawableForDensity(resources, i, j, theme);
        else
        if(android.os.Build.VERSION.SDK_INT >= 15)
            resources = ResourcesCompatIcsMr1.getDrawableForDensity(resources, i, j);
        else
            resources = resources.getDrawable(i);
        return resources;
    }
}
