// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.graphics.drawable;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

// Referenced classes of package android.support.v4.graphics.drawable:
//            DrawableWrapperDonut

class DrawableWrapperEclair extends DrawableWrapperDonut
{
    private static class DrawableWrapperStateEclair extends DrawableWrapperDonut.DrawableWrapperState
    {

        public Drawable newDrawable(Resources resources)
        {
            return new DrawableWrapperEclair(this, resources);
        }

        DrawableWrapperStateEclair(DrawableWrapperDonut.DrawableWrapperState drawablewrapperstate, Resources resources)
        {
            super(drawablewrapperstate, resources);
        }
    }


    DrawableWrapperEclair(Drawable drawable)
    {
        super(drawable);
    }

    DrawableWrapperEclair(DrawableWrapperDonut.DrawableWrapperState drawablewrapperstate, Resources resources)
    {
        super(drawablewrapperstate, resources);
    }

    DrawableWrapperDonut.DrawableWrapperState mutateConstantState()
    {
        return new DrawableWrapperStateEclair(mState, null);
    }

    protected Drawable newDrawableFromState(android.graphics.drawable.Drawable.ConstantState constantstate, Resources resources)
    {
        return constantstate.newDrawable(resources);
    }
}
