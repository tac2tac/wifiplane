// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.drawable.*;

// Referenced classes of package android.support.v4.graphics.drawable:
//            DrawableWrapperKitKat

class DrawableWrapperLollipop extends DrawableWrapperKitKat
{
    private static class DrawableWrapperStateLollipop extends DrawableWrapperDonut.DrawableWrapperState
    {

        public Drawable newDrawable(Resources resources)
        {
            return new DrawableWrapperLollipop(this, resources);
        }

        DrawableWrapperStateLollipop(DrawableWrapperDonut.DrawableWrapperState drawablewrapperstate, Resources resources)
        {
            super(drawablewrapperstate, resources);
        }
    }


    DrawableWrapperLollipop(Drawable drawable)
    {
        super(drawable);
    }

    DrawableWrapperLollipop(DrawableWrapperDonut.DrawableWrapperState drawablewrapperstate, Resources resources)
    {
        super(drawablewrapperstate, resources);
    }

    public Rect getDirtyBounds()
    {
        return mDrawable.getDirtyBounds();
    }

    public void getOutline(Outline outline)
    {
        mDrawable.getOutline(outline);
    }

    protected boolean isCompatTintEnabled()
    {
        boolean flag1;
label0:
        {
            boolean flag = false;
            flag1 = flag;
            if(android.os.Build.VERSION.SDK_INT != 21)
                break label0;
            Drawable drawable = mDrawable;
            if(!(drawable instanceof GradientDrawable) && !(drawable instanceof DrawableContainer))
            {
                flag1 = flag;
                if(!(drawable instanceof InsetDrawable))
                    break label0;
            }
            flag1 = true;
        }
        return flag1;
    }

    DrawableWrapperDonut.DrawableWrapperState mutateConstantState()
    {
        return new DrawableWrapperStateLollipop(mState, null);
    }

    public void setHotspot(float f, float f1)
    {
        mDrawable.setHotspot(f, f1);
    }

    public void setHotspotBounds(int i, int j, int k, int l)
    {
        mDrawable.setHotspotBounds(i, j, k, l);
    }

    public boolean setState(int ai[])
    {
        boolean flag;
        if(super.setState(ai))
        {
            invalidateSelf();
            flag = true;
        } else
        {
            flag = false;
        }
        return flag;
    }

    public void setTint(int i)
    {
        if(isCompatTintEnabled())
            setCompatTint(i);
        else
            mDrawable.setTint(i);
    }

    public void setTintList(ColorStateList colorstatelist)
    {
        if(isCompatTintEnabled())
            setCompatTintList(colorstatelist);
        else
            mDrawable.setTintList(colorstatelist);
    }

    public void setTintMode(android.graphics.PorterDuff.Mode mode)
    {
        if(isCompatTintEnabled())
            setCompatTintMode(mode);
        else
            mDrawable.setTintMode(mode);
    }
}
