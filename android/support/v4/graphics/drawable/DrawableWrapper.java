// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.graphics.drawable;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;

public interface DrawableWrapper
{

    public abstract Drawable getWrappedDrawable();

    public abstract void setCompatTint(int i);

    public abstract void setCompatTintList(ColorStateList colorstatelist);

    public abstract void setCompatTintMode(android.graphics.PorterDuff.Mode mode);

    public abstract void setWrappedDrawable(Drawable drawable);
}
