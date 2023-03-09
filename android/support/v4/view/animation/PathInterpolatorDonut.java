// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.view.animation;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.view.animation.Interpolator;

class PathInterpolatorDonut
    implements Interpolator
{

    public PathInterpolatorDonut(float f, float f1)
    {
        this(createQuad(f, f1));
    }

    public PathInterpolatorDonut(float f, float f1, float f2, float f3)
    {
        this(createCubic(f, f1, f2, f3));
    }

    public PathInterpolatorDonut(Path path)
    {
        path = new PathMeasure(path, false);
        float f = path.getLength();
        int i = (int)(f / 0.002F) + 1;
        mX = new float[i];
        mY = new float[i];
        float af[] = new float[2];
        for(int j = 0; j < i; j++)
        {
            path.getPosTan(((float)j * f) / (float)(i - 1), af, null);
            mX[j] = af[0];
            mY[j] = af[1];
        }

    }

    private static Path createCubic(float f, float f1, float f2, float f3)
    {
        Path path = new Path();
        path.moveTo(0.0F, 0.0F);
        path.cubicTo(f, f1, f2, f3, 1.0F, 1.0F);
        return path;
    }

    private static Path createQuad(float f, float f1)
    {
        Path path = new Path();
        path.moveTo(0.0F, 0.0F);
        path.quadTo(f, f1, 1.0F, 1.0F);
        return path;
    }

    public float getInterpolation(float f)
    {
        float f1 = 0.0F;
        if(f <= 0.0F)
            f = f1;
        else
        if(f >= 1.0F)
        {
            f = 1.0F;
        } else
        {
            int i = 0;
            int j;
            for(j = mX.length - 1; j - i > 1;)
            {
                int k = (i + j) / 2;
                if(f < mX[k])
                    j = k;
                else
                    i = k;
            }

            float f2 = mX[j] - mX[i];
            if(f2 == 0.0F)
            {
                f = mY[i];
            } else
            {
                f2 = (f - mX[i]) / f2;
                f = mY[i];
                f = (mY[j] - f) * f2 + f;
            }
        }
        return f;
    }

    private static final float PRECISION = 0.002F;
    private final float mX[];
    private final float mY[];
}
