// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.view.animation;

import android.graphics.Path;
import android.view.animation.Interpolator;

// Referenced classes of package android.support.v4.view.animation:
//            PathInterpolatorCompatApi21, PathInterpolatorCompatBase

public final class PathInterpolatorCompat
{

    private PathInterpolatorCompat()
    {
    }

    public static Interpolator create(float f, float f1)
    {
        Interpolator interpolator;
        if(android.os.Build.VERSION.SDK_INT >= 21)
            interpolator = PathInterpolatorCompatApi21.create(f, f1);
        else
            interpolator = PathInterpolatorCompatBase.create(f, f1);
        return interpolator;
    }

    public static Interpolator create(float f, float f1, float f2, float f3)
    {
        Interpolator interpolator;
        if(android.os.Build.VERSION.SDK_INT >= 21)
            interpolator = PathInterpolatorCompatApi21.create(f, f1, f2, f3);
        else
            interpolator = PathInterpolatorCompatBase.create(f, f1, f2, f3);
        return interpolator;
    }

    public static Interpolator create(Path path)
    {
        if(android.os.Build.VERSION.SDK_INT >= 21)
            path = PathInterpolatorCompatApi21.create(path);
        else
            path = PathInterpolatorCompatBase.create(path);
        return path;
    }
}
