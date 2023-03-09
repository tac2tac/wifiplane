// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.*;

// Referenced classes of package android.support.v4.view:
//            MotionEventCompat, VelocityTrackerCompat

public final class GestureDetectorCompat
{
    static interface GestureDetectorCompatImpl
    {

        public abstract boolean isLongpressEnabled();

        public abstract boolean onTouchEvent(MotionEvent motionevent);

        public abstract void setIsLongpressEnabled(boolean flag);

        public abstract void setOnDoubleTapListener(android.view.GestureDetector.OnDoubleTapListener ondoubletaplistener);
    }

    static class GestureDetectorCompatImplBase
        implements GestureDetectorCompatImpl
    {

        private void cancel()
        {
            mHandler.removeMessages(1);
            mHandler.removeMessages(2);
            mHandler.removeMessages(3);
            mVelocityTracker.recycle();
            mVelocityTracker = null;
            mIsDoubleTapping = false;
            mStillDown = false;
            mAlwaysInTapRegion = false;
            mAlwaysInBiggerTapRegion = false;
            mDeferConfirmSingleTap = false;
            if(mInLongPress)
                mInLongPress = false;
        }

        private void cancelTaps()
        {
            mHandler.removeMessages(1);
            mHandler.removeMessages(2);
            mHandler.removeMessages(3);
            mIsDoubleTapping = false;
            mAlwaysInTapRegion = false;
            mAlwaysInBiggerTapRegion = false;
            mDeferConfirmSingleTap = false;
            if(mInLongPress)
                mInLongPress = false;
        }

        private void dispatchLongPress()
        {
            mHandler.removeMessages(3);
            mDeferConfirmSingleTap = false;
            mInLongPress = true;
            mListener.onLongPress(mCurrentDownEvent);
        }

        private void init(Context context)
        {
            if(context == null)
                throw new IllegalArgumentException("Context must not be null");
            if(mListener == null)
            {
                throw new IllegalArgumentException("OnGestureListener must not be null");
            } else
            {
                mIsLongpressEnabled = true;
                context = ViewConfiguration.get(context);
                int i = context.getScaledTouchSlop();
                int j = context.getScaledDoubleTapSlop();
                mMinimumFlingVelocity = context.getScaledMinimumFlingVelocity();
                mMaximumFlingVelocity = context.getScaledMaximumFlingVelocity();
                mTouchSlopSquare = i * i;
                mDoubleTapSlopSquare = j * j;
                return;
            }
        }

        private boolean isConsideredDoubleTap(MotionEvent motionevent, MotionEvent motionevent1, MotionEvent motionevent2)
        {
            boolean flag = false;
            if(mAlwaysInBiggerTapRegion) goto _L2; else goto _L1
_L1:
            boolean flag1 = flag;
_L4:
            return flag1;
_L2:
            flag1 = flag;
            if(motionevent2.getEventTime() - motionevent1.getEventTime() <= (long)DOUBLE_TAP_TIMEOUT)
            {
                int i = (int)motionevent.getX() - (int)motionevent2.getX();
                int j = (int)motionevent.getY() - (int)motionevent2.getY();
                flag1 = flag;
                if(i * i + j * j < mDoubleTapSlopSquare)
                    flag1 = true;
            }
            if(true) goto _L4; else goto _L3
_L3:
        }

        public boolean isLongpressEnabled()
        {
            return mIsLongpressEnabled;
        }

        public boolean onTouchEvent(MotionEvent motionevent)
        {
            int k1;
            float f;
            float f2;
            int i2;
            boolean flag1;
            boolean flag2;
            boolean flag3;
            int l2;
            int i = motionevent.getAction();
            if(mVelocityTracker == null)
                mVelocityTracker = VelocityTracker.obtain();
            mVelocityTracker.addMovement(motionevent);
            boolean flag;
            int j2;
            if((i & 0xff) == 6)
                flag = true;
            else
                flag = false;
            if(flag)
                k1 = MotionEventCompat.getActionIndex(motionevent);
            else
                k1 = -1;
            f = 0.0F;
            f2 = 0.0F;
            i2 = MotionEventCompat.getPointerCount(motionevent);
            j2 = 0;
            while(j2 < i2) 
            {
                float f4;
                if(k1 == j2)
                {
                    f4 = f2;
                    f2 = f;
                } else
                {
                    f += MotionEventCompat.getX(motionevent, j2);
                    f4 = f2 + MotionEventCompat.getY(motionevent, j2);
                    f2 = f;
                }
                j2++;
                f = f2;
                f2 = f4;
            }
            int j;
            if(flag)
                j = i2 - 1;
            else
                j = i2;
            f /= j;
            f2 /= j;
            k1 = 0;
            flag1 = false;
            flag2 = false;
            flag3 = false;
            l2 = ((flag3) ? 1 : 0);
            i & 0xff;
            JVM INSTR tableswitch 0 6: default 228
        //                       0 423
        //                       1 937
        //                       2 692
        //                       3 1219
        //                       4 232
        //                       5 241
        //                       6 276;
               goto _L1 _L2 _L3 _L4 _L5 _L6 _L7 _L8
_L6:
            break; /* Loop/switch isn't completed */
_L1:
            l2 = ((flag3) ? 1 : 0);
_L17:
            return l2;
_L7:
            mLastFocusX = f;
            mDownFocusX = f;
            mLastFocusY = f2;
            mDownFocusY = f2;
            cancelTaps();
            l2 = ((flag3) ? 1 : 0);
            continue; /* Loop/switch isn't completed */
_L8:
            int k;
            mLastFocusX = f;
            mDownFocusX = f;
            mLastFocusY = f2;
            mDownFocusY = f2;
            mVelocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
            k1 = MotionEventCompat.getActionIndex(motionevent);
            k = MotionEventCompat.getPointerId(motionevent, k1);
            f = VelocityTrackerCompat.getXVelocity(mVelocityTracker, k);
            f2 = VelocityTrackerCompat.getYVelocity(mVelocityTracker, k);
            k = 0;
_L10:
            l2 = ((flag3) ? 1 : 0);
            if(k >= i2)
                continue; /* Loop/switch isn't completed */
            if(k != k1)
                break; /* Loop/switch isn't completed */
_L12:
            k++;
            int k2;
            if(true) goto _L10; else goto _L9
_L9:
            if(f * VelocityTrackerCompat.getXVelocity(mVelocityTracker, k2 = MotionEventCompat.getPointerId(motionevent, k)) + f2 * VelocityTrackerCompat.getYVelocity(mVelocityTracker, k2) >= 0.0F) goto _L12; else goto _L11
_L11:
            mVelocityTracker.clear();
            l2 = ((flag3) ? 1 : 0);
            continue; /* Loop/switch isn't completed */
_L2:
            int l = k1;
            if(mDoubleTapListener != null)
            {
                l2 = mHandler.hasMessages(3);
                if(l2 != 0)
                    mHandler.removeMessages(3);
                if(mCurrentDownEvent != null && mPreviousUpEvent != null && l2 != 0 && isConsideredDoubleTap(mCurrentDownEvent, mPreviousUpEvent, motionevent))
                {
                    mIsDoubleTapping = true;
                    l = false | mDoubleTapListener.onDoubleTap(mCurrentDownEvent) | mDoubleTapListener.onDoubleTapEvent(motionevent);
                } else
                {
                    mHandler.sendEmptyMessageDelayed(3, DOUBLE_TAP_TIMEOUT);
                    l = k1;
                }
            }
            mLastFocusX = f;
            mDownFocusX = f;
            mLastFocusY = f2;
            mDownFocusY = f2;
            if(mCurrentDownEvent != null)
                mCurrentDownEvent.recycle();
            mCurrentDownEvent = MotionEvent.obtain(motionevent);
            mAlwaysInTapRegion = true;
            mAlwaysInBiggerTapRegion = true;
            mStillDown = true;
            mInLongPress = false;
            mDeferConfirmSingleTap = false;
            if(mIsLongpressEnabled)
            {
                mHandler.removeMessages(2);
                mHandler.sendEmptyMessageAtTime(2, mCurrentDownEvent.getDownTime() + (long)TAP_TIMEOUT + (long)LONGPRESS_TIMEOUT);
            }
            mHandler.sendEmptyMessageAtTime(1, mCurrentDownEvent.getDownTime() + (long)TAP_TIMEOUT);
            l2 = l | mListener.onDown(motionevent);
            continue; /* Loop/switch isn't completed */
_L4:
            l2 = ((flag3) ? 1 : 0);
            if(mInLongPress)
                continue; /* Loop/switch isn't completed */
            float f6 = mLastFocusX - f;
            float f5 = mLastFocusY - f2;
            if(mIsDoubleTapping)
            {
                l2 = false | mDoubleTapListener.onDoubleTapEvent(motionevent);
                continue; /* Loop/switch isn't completed */
            }
            if(mAlwaysInTapRegion)
            {
                int l1 = (int)(f - mDownFocusX);
                int i1 = (int)(f2 - mDownFocusY);
                i1 = l1 * l1 + i1 * i1;
                flag3 = flag1;
                if(i1 > mTouchSlopSquare)
                {
                    flag3 = mListener.onScroll(mCurrentDownEvent, motionevent, f6, f5);
                    mLastFocusX = f;
                    mLastFocusY = f2;
                    mAlwaysInTapRegion = false;
                    mHandler.removeMessages(3);
                    mHandler.removeMessages(1);
                    mHandler.removeMessages(2);
                }
                l2 = ((flag3) ? 1 : 0);
                if(i1 > mTouchSlopSquare)
                {
                    mAlwaysInBiggerTapRegion = false;
                    l2 = ((flag3) ? 1 : 0);
                }
                continue; /* Loop/switch isn't completed */
            }
            if(Math.abs(f6) < 1.0F)
            {
                l2 = ((flag3) ? 1 : 0);
                if(Math.abs(f5) < 1.0F)
                    continue; /* Loop/switch isn't completed */
            }
            l2 = mListener.onScroll(mCurrentDownEvent, motionevent, f6, f5);
            mLastFocusX = f;
            mLastFocusY = f2;
            continue; /* Loop/switch isn't completed */
_L3:
            MotionEvent motionevent1;
            mStillDown = false;
            motionevent1 = MotionEvent.obtain(motionevent);
            if(!mIsDoubleTapping) goto _L14; else goto _L13
_L13:
            l2 = false | mDoubleTapListener.onDoubleTapEvent(motionevent);
_L15:
            if(mPreviousUpEvent != null)
                mPreviousUpEvent.recycle();
            mPreviousUpEvent = motionevent1;
            if(mVelocityTracker != null)
            {
                mVelocityTracker.recycle();
                mVelocityTracker = null;
            }
            mIsDoubleTapping = false;
            mDeferConfirmSingleTap = false;
            mHandler.removeMessages(1);
            mHandler.removeMessages(2);
            continue; /* Loop/switch isn't completed */
_L14:
            if(mInLongPress)
            {
                mHandler.removeMessages(3);
                mInLongPress = false;
                l2 = ((flag2) ? 1 : 0);
                continue; /* Loop/switch isn't completed */
            }
            if(mAlwaysInTapRegion)
            {
                flag3 = mListener.onSingleTapUp(motionevent);
                l2 = ((flag3) ? 1 : 0);
                if(mDeferConfirmSingleTap)
                {
                    l2 = ((flag3) ? 1 : 0);
                    if(mDoubleTapListener != null)
                    {
                        mDoubleTapListener.onSingleTapConfirmed(motionevent);
                        l2 = ((flag3) ? 1 : 0);
                    }
                }
                continue; /* Loop/switch isn't completed */
            }
            VelocityTracker velocitytracker = mVelocityTracker;
            int j1 = MotionEventCompat.getPointerId(motionevent, 0);
            velocitytracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
            float f3 = VelocityTrackerCompat.getYVelocity(velocitytracker, j1);
            float f1 = VelocityTrackerCompat.getXVelocity(velocitytracker, j1);
            if(Math.abs(f3) <= (float)mMinimumFlingVelocity)
            {
                l2 = ((flag2) ? 1 : 0);
                if(Math.abs(f1) <= (float)mMinimumFlingVelocity)
                    continue; /* Loop/switch isn't completed */
            }
            l2 = mListener.onFling(mCurrentDownEvent, motionevent, f1, f3);
            if(true) goto _L15; else goto _L5
_L5:
            cancel();
            l2 = ((flag3) ? 1 : 0);
            if(true) goto _L17; else goto _L16
_L16:
        }

        public void setIsLongpressEnabled(boolean flag)
        {
            mIsLongpressEnabled = flag;
        }

        public void setOnDoubleTapListener(android.view.GestureDetector.OnDoubleTapListener ondoubletaplistener)
        {
            mDoubleTapListener = ondoubletaplistener;
        }

        private static final int DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
        private static final int LONGPRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
        private static final int LONG_PRESS = 2;
        private static final int SHOW_PRESS = 1;
        private static final int TAP = 3;
        private static final int TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
        private boolean mAlwaysInBiggerTapRegion;
        private boolean mAlwaysInTapRegion;
        private MotionEvent mCurrentDownEvent;
        private boolean mDeferConfirmSingleTap;
        private android.view.GestureDetector.OnDoubleTapListener mDoubleTapListener;
        private int mDoubleTapSlopSquare;
        private float mDownFocusX;
        private float mDownFocusY;
        private final Handler mHandler;
        private boolean mInLongPress;
        private boolean mIsDoubleTapping;
        private boolean mIsLongpressEnabled;
        private float mLastFocusX;
        private float mLastFocusY;
        private final android.view.GestureDetector.OnGestureListener mListener;
        private int mMaximumFlingVelocity;
        private int mMinimumFlingVelocity;
        private MotionEvent mPreviousUpEvent;
        private boolean mStillDown;
        private int mTouchSlopSquare;
        private VelocityTracker mVelocityTracker;








/*
        static boolean access$502(GestureDetectorCompatImplBase gesturedetectorcompatimplbase, boolean flag)
        {
            gesturedetectorcompatimplbase.mDeferConfirmSingleTap = flag;
            return flag;
        }

*/

        public GestureDetectorCompatImplBase(Context context, android.view.GestureDetector.OnGestureListener ongesturelistener, Handler handler)
        {
            if(handler != null)
                mHandler = new GestureHandler(handler);
            else
                mHandler = new GestureHandler();
            mListener = ongesturelistener;
            if(ongesturelistener instanceof android.view.GestureDetector.OnDoubleTapListener)
                setOnDoubleTapListener((android.view.GestureDetector.OnDoubleTapListener)ongesturelistener);
            init(context);
        }
    }

    private class GestureDetectorCompatImplBase.GestureHandler extends Handler
    {

        public void handleMessage(Message message)
        {
            message.what;
            JVM INSTR tableswitch 1 3: default 32
        //                       1 59
        //                       2 79
        //                       3 89;
               goto _L1 _L2 _L3 _L4
_L1:
            throw new RuntimeException((new StringBuilder()).append("Unknown message ").append(message).toString());
_L2:
            mListener.onShowPress(mCurrentDownEvent);
_L6:
            return;
_L3:
            dispatchLongPress();
            continue; /* Loop/switch isn't completed */
_L4:
            if(mDoubleTapListener != null)
                if(!mStillDown)
                    mDoubleTapListener.onSingleTapConfirmed(mCurrentDownEvent);
                else
                    mDeferConfirmSingleTap = true;
            if(true) goto _L6; else goto _L5
_L5:
        }

        final GestureDetectorCompatImplBase this$0;

        GestureDetectorCompatImplBase.GestureHandler()
        {
            this$0 = GestureDetectorCompatImplBase.this;
            super();
        }

        GestureDetectorCompatImplBase.GestureHandler(Handler handler)
        {
            this$0 = GestureDetectorCompatImplBase.this;
            super(handler.getLooper());
        }
    }

    static class GestureDetectorCompatImplJellybeanMr2
        implements GestureDetectorCompatImpl
    {

        public boolean isLongpressEnabled()
        {
            return mDetector.isLongpressEnabled();
        }

        public boolean onTouchEvent(MotionEvent motionevent)
        {
            return mDetector.onTouchEvent(motionevent);
        }

        public void setIsLongpressEnabled(boolean flag)
        {
            mDetector.setIsLongpressEnabled(flag);
        }

        public void setOnDoubleTapListener(android.view.GestureDetector.OnDoubleTapListener ondoubletaplistener)
        {
            mDetector.setOnDoubleTapListener(ondoubletaplistener);
        }

        private final GestureDetector mDetector;

        public GestureDetectorCompatImplJellybeanMr2(Context context, android.view.GestureDetector.OnGestureListener ongesturelistener, Handler handler)
        {
            mDetector = new GestureDetector(context, ongesturelistener, handler);
        }
    }


    public GestureDetectorCompat(Context context, android.view.GestureDetector.OnGestureListener ongesturelistener)
    {
        this(context, ongesturelistener, null);
    }

    public GestureDetectorCompat(Context context, android.view.GestureDetector.OnGestureListener ongesturelistener, Handler handler)
    {
        if(android.os.Build.VERSION.SDK_INT > 17)
            mImpl = new GestureDetectorCompatImplJellybeanMr2(context, ongesturelistener, handler);
        else
            mImpl = new GestureDetectorCompatImplBase(context, ongesturelistener, handler);
    }

    public boolean isLongpressEnabled()
    {
        return mImpl.isLongpressEnabled();
    }

    public boolean onTouchEvent(MotionEvent motionevent)
    {
        return mImpl.onTouchEvent(motionevent);
    }

    public void setIsLongpressEnabled(boolean flag)
    {
        mImpl.setIsLongpressEnabled(flag);
    }

    public void setOnDoubleTapListener(android.view.GestureDetector.OnDoubleTapListener ondoubletaplistener)
    {
        mImpl.setOnDoubleTapListener(ondoubletaplistener);
    }

    private final GestureDetectorCompatImpl mImpl;
}
