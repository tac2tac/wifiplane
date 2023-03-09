// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.view.animation;

import android.view.animation.Interpolator;

abstract class LookupTableInterpolator
    implements Interpolator
{

    public LookupTableInterpolator(float af[])
    {
        mValues = af;
        mStepSize = 1.0F / (float)(mValues.length - 1);
    }

    public float getInterpolation(float f)
    {
        float f1 = 1.0F;
        if(f >= 1.0F)
            f = f1;
        else
        if(f <= 0.0F)
        {
            f = 0.0F;
        } else
        {
            int i = Math.min((int)((float)(mValues.length - 1) * f), mValues.length - 2);
            f = (f - (float)i * mStepSize) / mStepSize;
            f = mValues[i] + (mValues[i + 1] - mValues[i]) * f;
        }
        return f;
    }

    private final float mStepSize;
    private final float mValues[];
}
