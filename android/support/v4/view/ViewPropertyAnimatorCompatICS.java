// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;

// Referenced classes of package android.support.v4.view:
//            ViewPropertyAnimatorListener

class ViewPropertyAnimatorCompatICS
{

    ViewPropertyAnimatorCompatICS()
    {
    }

    public static void alpha(View view, float f)
    {
        view.animate().alpha(f);
    }

    public static void alphaBy(View view, float f)
    {
        view.animate().alphaBy(f);
    }

    public static void cancel(View view)
    {
        view.animate().cancel();
    }

    public static long getDuration(View view)
    {
        return view.animate().getDuration();
    }

    public static long getStartDelay(View view)
    {
        return view.animate().getStartDelay();
    }

    public static void rotation(View view, float f)
    {
        view.animate().rotation(f);
    }

    public static void rotationBy(View view, float f)
    {
        view.animate().rotationBy(f);
    }

    public static void rotationX(View view, float f)
    {
        view.animate().rotationX(f);
    }

    public static void rotationXBy(View view, float f)
    {
        view.animate().rotationXBy(f);
    }

    public static void rotationY(View view, float f)
    {
        view.animate().rotationY(f);
    }

    public static void rotationYBy(View view, float f)
    {
        view.animate().rotationYBy(f);
    }

    public static void scaleX(View view, float f)
    {
        view.animate().scaleX(f);
    }

    public static void scaleXBy(View view, float f)
    {
        view.animate().scaleXBy(f);
    }

    public static void scaleY(View view, float f)
    {
        view.animate().scaleY(f);
    }

    public static void scaleYBy(View view, float f)
    {
        view.animate().scaleYBy(f);
    }

    public static void setDuration(View view, long l)
    {
        view.animate().setDuration(l);
    }

    public static void setInterpolator(View view, Interpolator interpolator)
    {
        view.animate().setInterpolator(interpolator);
    }

    public static void setListener(View view, ViewPropertyAnimatorListener viewpropertyanimatorlistener)
    {
        if(viewpropertyanimatorlistener != null)
            view.animate().setListener(new AnimatorListenerAdapter(viewpropertyanimatorlistener, view) {

                public void onAnimationCancel(Animator animator)
                {
                    listener.onAnimationCancel(view);
                }

                public void onAnimationEnd(Animator animator)
                {
                    listener.onAnimationEnd(view);
                }

                public void onAnimationStart(Animator animator)
                {
                    listener.onAnimationStart(view);
                }

                final ViewPropertyAnimatorListener val$listener;
                final View val$view;

            
            {
                listener = viewpropertyanimatorlistener;
                view = view1;
                super();
            }
            }
);
        else
            view.animate().setListener(null);
    }

    public static void setStartDelay(View view, long l)
    {
        view.animate().setStartDelay(l);
    }

    public static void start(View view)
    {
        view.animate().start();
    }

    public static void translationX(View view, float f)
    {
        view.animate().translationX(f);
    }

    public static void translationXBy(View view, float f)
    {
        view.animate().translationXBy(f);
    }

    public static void translationY(View view, float f)
    {
        view.animate().translationY(f);
    }

    public static void translationYBy(View view, float f)
    {
        view.animate().translationYBy(f);
    }

    public static void x(View view, float f)
    {
        view.animate().x(f);
    }

    public static void xBy(View view, float f)
    {
        view.animate().xBy(f);
    }

    public static void y(View view, float f)
    {
        view.animate().y(f);
    }

    public static void yBy(View view, float f)
    {
        view.animate().yBy(f);
    }
}
