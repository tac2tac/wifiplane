// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.graphics.drawable;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

// Referenced classes of package android.support.v4.graphics.drawable:
//            DrawableWrapperDonut

class DrawableWrapperHoneycomb extends DrawableWrapperDonut
{
    private static class DrawableWrapperStateHoneycomb extends DrawableWrapperDonut.DrawableWrapperState
    {

        public Drawable newDrawable(Resources resources)
        {
            return new DrawableWrapperHoneycomb(this, resources);
        }

        DrawableWrapperStateHoneycomb(DrawableWrapperDonut.DrawableWrapperState drawablewrapperstate, Resources resources)
        {
            super(drawablewrapperstate, resources);
        }
    }


    DrawableWrapperHoneycomb(Drawable drawable)
    {
        super(drawable);
    }

    DrawableWrapperHoneycomb(DrawableWrapperDonut.DrawableWrapperState drawablewrapperstate, Resources resources)
    {
        super(drawablewrapperstate, resources);
    }

    public void jumpToCurrentState()
    {
        mDrawable.jumpToCurrentState();
    }

    DrawableWrapperDonut.DrawableWrapperState mutateConstantState()
    {
        return new DrawableWrapperStateHoneycomb(mState, null);
    }
}
