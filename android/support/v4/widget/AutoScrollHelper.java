// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.widget;

import android.content.res.Resources;
import android.os.SystemClock;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.*;
import android.view.animation.*;

public abstract class AutoScrollHelper
    implements android.view.View.OnTouchListener
{
    private static class ClampedScroller
    {

        private float getValueAt(long l)
        {
            float f = 0.0F;
            if(l >= mStartTime)
                if(mStopTime < 0L || l < mStopTime)
                {
                    f = AutoScrollHelper.constrain((float)(l - mStartTime) / (float)mRampUpDuration, 0.0F, 1.0F) * 0.5F;
                } else
                {
                    long l1 = mStopTime;
                    f = mStopValue;
                    float f1 = mStopValue;
                    f = AutoScrollHelper.constrain((float)(l - l1) / (float)mEffectiveRampDown, 0.0F, 1.0F) * f1 + (1.0F - f);
                }
            return f;
        }

        private float interpolateValue(float f)
        {
            return -4F * f * f + 4F * f;
        }

        public void computeScrollDelta()
        {
            if(mDeltaTime == 0L)
            {
                throw new RuntimeException("Cannot compute scroll delta before calling start()");
            } else
            {
                long l = AnimationUtils.currentAnimationTimeMillis();
                float f = interpolateValue(getValueAt(l));
                long l1 = l - mDeltaTime;
                mDeltaTime = l;
                mDeltaX = (int)((float)l1 * f * mTargetVelocityX);
                mDeltaY = (int)((float)l1 * f * mTargetVelocityY);
                return;
            }
        }

        public int getDeltaX()
        {
            return mDeltaX;
        }

        public int getDeltaY()
        {
            return mDeltaY;
        }

        public int getHorizontalDirection()
        {
            return (int)(mTargetVelocityX / Math.abs(mTargetVelocityX));
        }

        public int getVerticalDirection()
        {
            return (int)(mTargetVelocityY / Math.abs(mTargetVelocityY));
        }

        public boolean isFinished()
        {
            boolean flag;
            if(mStopTime > 0L && AnimationUtils.currentAnimationTimeMillis() > mStopTime + (long)mEffectiveRampDown)
                flag = true;
            else
                flag = false;
            return flag;
        }

        public void requestStop()
        {
            long l = AnimationUtils.currentAnimationTimeMillis();
            mEffectiveRampDown = AutoScrollHelper.constrain((int)(l - mStartTime), 0, mRampDownDuration);
            mStopValue = getValueAt(l);
            mStopTime = l;
        }

        public void setRampDownDuration(int i)
        {
            mRampDownDuration = i;
        }

        public void setRampUpDuration(int i)
        {
            mRampUpDuration = i;
        }

        public void setTargetVelocity(float f, float f1)
        {
            mTargetVelocityX = f;
            mTargetVelocityY = f1;
        }

        public void start()
        {
            mStartTime = AnimationUtils.currentAnimationTimeMillis();
            mStopTime = -1L;
            mDeltaTime = mStartTime;
            mStopValue = 0.5F;
            mDeltaX = 0;
            mDeltaY = 0;
        }

        private long mDeltaTime;
        private int mDeltaX;
        private int mDeltaY;
        private int mEffectiveRampDown;
        private int mRampDownDuration;
        private int mRampUpDuration;
        private long mStartTime;
        private long mStopTime;
        private float mStopValue;
        private float mTargetVelocityX;
        private float mTargetVelocityY;

        public ClampedScroller()
        {
            mStartTime = 0x8000000000000000L;
            mStopTime = -1L;
            mDeltaTime = 0L;
            mDeltaX = 0;
            mDeltaY = 0;
        }
    }

    private class ScrollAnimationRunnable
        implements Runnable
    {

        public void run()
        {
            if(mAnimating)
            {
                if(mNeedsReset)
                {
                    mNeedsReset = false;
                    mScroller.start();
                }
                ClampedScroller clampedscroller = mScroller;
                if(clampedscroller.isFinished() || !shouldAnimate())
                {
                    mAnimating = false;
                } else
                {
                    if(mNeedsCancel)
                    {
                        mNeedsCancel = false;
                        cancelTargetTouch();
                    }
                    clampedscroller.computeScrollDelta();
                    int i = clampedscroller.getDeltaX();
                    int j = clampedscroller.getDeltaY();
                    scrollTargetBy(i, j);
                    ViewCompat.postOnAnimation(mTarget, this);
                }
            }
        }

        final AutoScrollHelper this$0;

        private ScrollAnimationRunnable()
        {
            this$0 = AutoScrollHelper.this;
            super();
        }

    }


    public AutoScrollHelper(View view)
    {
        mTarget = view;
        view = Resources.getSystem().getDisplayMetrics();
        int i = (int)(1575F * ((DisplayMetrics) (view)).density + 0.5F);
        int j = (int)(315F * ((DisplayMetrics) (view)).density + 0.5F);
        setMaximumVelocity(i, i);
        setMinimumVelocity(j, j);
        setEdgeType(1);
        setMaximumEdges(3.402823E+038F, 3.402823E+038F);
        setRelativeEdges(0.2F, 0.2F);
        setRelativeVelocity(1.0F, 1.0F);
        setActivationDelay(DEFAULT_ACTIVATION_DELAY);
        setRampUpDuration(500);
        setRampDownDuration(500);
    }

    private void cancelTargetTouch()
    {
        long l = SystemClock.uptimeMillis();
        MotionEvent motionevent = MotionEvent.obtain(l, l, 3, 0.0F, 0.0F, 0);
        mTarget.onTouchEvent(motionevent);
        motionevent.recycle();
    }

    private float computeTargetVelocity(int i, float f, float f1, float f2)
    {
        float f3 = 0.0F;
        f = getEdgeValue(mRelativeEdges[i], f1, mMaximumEdges[i], f);
        if(f == 0.0F)
        {
            f = f3;
        } else
        {
            float f5 = mRelativeVelocity[i];
            f1 = mMinimumVelocity[i];
            float f4 = mMaximumVelocity[i];
            f2 = f5 * f2;
            if(f > 0.0F)
                f = constrain(f * f2, f1, f4);
            else
                f = -constrain(-f * f2, f1, f4);
        }
        return f;
    }

    private static float constrain(float f, float f1, float f2)
    {
        if(f <= f2)
            if(f < f1)
                f2 = f1;
            else
                f2 = f;
        return f2;
    }

    private static int constrain(int i, int j, int k)
    {
        if(i <= k)
            if(i < j)
                k = j;
            else
                k = i;
        return k;
    }

    private float constrainEdgeValue(float f, float f1)
    {
        float f2 = 0.0F;
        if(f1 != 0.0F) goto _L2; else goto _L1
_L1:
        float f3 = f2;
_L4:
        return f3;
_L2:
        switch(mEdgeType)
        {
        default:
            f3 = f2;
            break;

        case 0: // '\0'
        case 1: // '\001'
            f3 = f2;
            if(f < f1)
                if(f >= 0.0F)
                {
                    f3 = 1.0F - f / f1;
                } else
                {
                    f3 = f2;
                    if(mAnimating)
                    {
                        f3 = f2;
                        if(mEdgeType == 1)
                            f3 = 1.0F;
                    }
                }
            break;

        case 2: // '\002'
            f3 = f2;
            if(f < 0.0F)
                f3 = f / -f1;
            break;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    private float getEdgeValue(float f, float f1, float f2, float f3)
    {
        float f4;
        f4 = 0.0F;
        f = constrain(f * f1, 0.0F, f2);
        f2 = constrainEdgeValue(f3, f);
        f1 = constrainEdgeValue(f1 - f3, f) - f2;
        if(f1 >= 0.0F) goto _L2; else goto _L1
_L1:
        f = -mEdgeInterpolator.getInterpolation(-f1);
_L6:
        f = constrain(f, -1F, 1.0F);
_L4:
        return f;
_L2:
        f = f4;
        if(f1 <= 0.0F) goto _L4; else goto _L3
_L3:
        f = mEdgeInterpolator.getInterpolation(f1);
        if(true) goto _L6; else goto _L5
_L5:
    }

    private void requestStop()
    {
        if(mNeedsReset)
            mAnimating = false;
        else
            mScroller.requestStop();
    }

    private boolean shouldAnimate()
    {
        ClampedScroller clampedscroller = mScroller;
        int i = clampedscroller.getVerticalDirection();
        int j = clampedscroller.getHorizontalDirection();
        boolean flag;
        if(i != 0 && canTargetScrollVertically(i) || j != 0 && canTargetScrollHorizontally(j))
            flag = true;
        else
            flag = false;
        return flag;
    }

    private void startAnimating()
    {
        if(mRunnable == null)
            mRunnable = new ScrollAnimationRunnable();
        mAnimating = true;
        mNeedsReset = true;
        if(!mAlreadyDelayed && mActivationDelay > 0)
            ViewCompat.postOnAnimationDelayed(mTarget, mRunnable, mActivationDelay);
        else
            mRunnable.run();
        mAlreadyDelayed = true;
    }

    public abstract boolean canTargetScrollHorizontally(int i);

    public abstract boolean canTargetScrollVertically(int i);

    public boolean isEnabled()
    {
        return mEnabled;
    }

    public boolean isExclusive()
    {
        return mExclusive;
    }

    public boolean onTouch(View view, MotionEvent motionevent)
    {
        boolean flag;
        boolean flag1;
        flag = true;
        flag1 = false;
        if(mEnabled) goto _L2; else goto _L1
_L1:
        return flag1;
_L2:
        MotionEventCompat.getActionMasked(motionevent);
        JVM INSTR tableswitch 0 3: default 48
    //                   0 68
    //                   1 158
    //                   2 78
    //                   3 158;
           goto _L3 _L4 _L5 _L6 _L5
_L3:
        if(mExclusive && mAnimating)
            flag1 = flag;
        else
            flag1 = false;
        if(true) goto _L1; else goto _L4
_L4:
        mNeedsCancel = true;
        mAlreadyDelayed = false;
_L6:
        float f = computeTargetVelocity(0, motionevent.getX(), view.getWidth(), mTarget.getWidth());
        float f1 = computeTargetVelocity(1, motionevent.getY(), view.getHeight(), mTarget.getHeight());
        mScroller.setTargetVelocity(f, f1);
        if(!mAnimating && shouldAnimate())
            startAnimating();
          goto _L3
_L5:
        requestStop();
          goto _L3
    }

    public abstract void scrollTargetBy(int i, int j);

    public AutoScrollHelper setActivationDelay(int i)
    {
        mActivationDelay = i;
        return this;
    }

    public AutoScrollHelper setEdgeType(int i)
    {
        mEdgeType = i;
        return this;
    }

    public AutoScrollHelper setEnabled(boolean flag)
    {
        if(mEnabled && !flag)
            requestStop();
        mEnabled = flag;
        return this;
    }

    public AutoScrollHelper setExclusive(boolean flag)
    {
        mExclusive = flag;
        return this;
    }

    public AutoScrollHelper setMaximumEdges(float f, float f1)
    {
        mMaximumEdges[0] = f;
        mMaximumEdges[1] = f1;
        return this;
    }

    public AutoScrollHelper setMaximumVelocity(float f, float f1)
    {
        mMaximumVelocity[0] = f / 1000F;
        mMaximumVelocity[1] = f1 / 1000F;
        return this;
    }

    public AutoScrollHelper setMinimumVelocity(float f, float f1)
    {
        mMinimumVelocity[0] = f / 1000F;
        mMinimumVelocity[1] = f1 / 1000F;
        return this;
    }

    public AutoScrollHelper setRampDownDuration(int i)
    {
        mScroller.setRampDownDuration(i);
        return this;
    }

    public AutoScrollHelper setRampUpDuration(int i)
    {
        mScroller.setRampUpDuration(i);
        return this;
    }

    public AutoScrollHelper setRelativeEdges(float f, float f1)
    {
        mRelativeEdges[0] = f;
        mRelativeEdges[1] = f1;
        return this;
    }

    public AutoScrollHelper setRelativeVelocity(float f, float f1)
    {
        mRelativeVelocity[0] = f / 1000F;
        mRelativeVelocity[1] = f1 / 1000F;
        return this;
    }

    private static final int DEFAULT_ACTIVATION_DELAY = ViewConfiguration.getTapTimeout();
    private static final int DEFAULT_EDGE_TYPE = 1;
    private static final float DEFAULT_MAXIMUM_EDGE = 3.402823E+038F;
    private static final int DEFAULT_MAXIMUM_VELOCITY_DIPS = 1575;
    private static final int DEFAULT_MINIMUM_VELOCITY_DIPS = 315;
    private static final int DEFAULT_RAMP_DOWN_DURATION = 500;
    private static final int DEFAULT_RAMP_UP_DURATION = 500;
    private static final float DEFAULT_RELATIVE_EDGE = 0.2F;
    private static final float DEFAULT_RELATIVE_VELOCITY = 1F;
    public static final int EDGE_TYPE_INSIDE = 0;
    public static final int EDGE_TYPE_INSIDE_EXTEND = 1;
    public static final int EDGE_TYPE_OUTSIDE = 2;
    private static final int HORIZONTAL = 0;
    public static final float NO_MAX = 3.402823E+038F;
    public static final float NO_MIN = 0F;
    public static final float RELATIVE_UNSPECIFIED = 0F;
    private static final int VERTICAL = 1;
    private int mActivationDelay;
    private boolean mAlreadyDelayed;
    private boolean mAnimating;
    private final Interpolator mEdgeInterpolator = new AccelerateInterpolator();
    private int mEdgeType;
    private boolean mEnabled;
    private boolean mExclusive;
    private float mMaximumEdges[] = {
        3.402823E+038F, 3.402823E+038F
    };
    private float mMaximumVelocity[] = {
        3.402823E+038F, 3.402823E+038F
    };
    private float mMinimumVelocity[] = {
        0.0F, 0.0F
    };
    private boolean mNeedsCancel;
    private boolean mNeedsReset;
    private float mRelativeEdges[] = {
        0.0F, 0.0F
    };
    private float mRelativeVelocity[] = {
        0.0F, 0.0F
    };
    private Runnable mRunnable;
    private final ClampedScroller mScroller = new ClampedScroller();
    private final View mTarget;




/*
    static boolean access$102(AutoScrollHelper autoscrollhelper, boolean flag)
    {
        autoscrollhelper.mAnimating = flag;
        return flag;
    }

*/



/*
    static boolean access$202(AutoScrollHelper autoscrollhelper, boolean flag)
    {
        autoscrollhelper.mNeedsReset = flag;
        return flag;
    }

*/





/*
    static boolean access$502(AutoScrollHelper autoscrollhelper, boolean flag)
    {
        autoscrollhelper.mNeedsCancel = flag;
        return flag;
    }

*/




}
