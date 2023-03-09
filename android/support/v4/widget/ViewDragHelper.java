// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.widget;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.*;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.view.animation.Interpolator;
import java.util.Arrays;

// Referenced classes of package android.support.v4.widget:
//            ScrollerCompat

public class ViewDragHelper
{
    public static abstract class Callback
    {

        public int clampViewPositionHorizontal(View view, int i, int j)
        {
            return 0;
        }

        public int clampViewPositionVertical(View view, int i, int j)
        {
            return 0;
        }

        public int getOrderedChildIndex(int i)
        {
            return i;
        }

        public int getViewHorizontalDragRange(View view)
        {
            return 0;
        }

        public int getViewVerticalDragRange(View view)
        {
            return 0;
        }

        public void onEdgeDragStarted(int i, int j)
        {
        }

        public boolean onEdgeLock(int i)
        {
            return false;
        }

        public void onEdgeTouched(int i, int j)
        {
        }

        public void onViewCaptured(View view, int i)
        {
        }

        public void onViewDragStateChanged(int i)
        {
        }

        public void onViewPositionChanged(View view, int i, int j, int k, int l)
        {
        }

        public void onViewReleased(View view, float f, float f1)
        {
        }

        public abstract boolean tryCaptureView(View view, int i);

        public Callback()
        {
        }
    }


    private ViewDragHelper(Context context, ViewGroup viewgroup, Callback callback)
    {
        mActivePointerId = -1;
        if(viewgroup == null)
            throw new IllegalArgumentException("Parent view may not be null");
        if(callback == null)
        {
            throw new IllegalArgumentException("Callback may not be null");
        } else
        {
            mParentView = viewgroup;
            mCallback = callback;
            viewgroup = ViewConfiguration.get(context);
            mEdgeSize = (int)(20F * context.getResources().getDisplayMetrics().density + 0.5F);
            mTouchSlop = viewgroup.getScaledTouchSlop();
            mMaxVelocity = viewgroup.getScaledMaximumFlingVelocity();
            mMinVelocity = viewgroup.getScaledMinimumFlingVelocity();
            mScroller = ScrollerCompat.create(context, sInterpolator);
            return;
        }
    }

    private boolean checkNewEdgeDrag(float f, float f1, int i, int j)
    {
        boolean flag;
        boolean flag1;
        flag = false;
        f = Math.abs(f);
        f1 = Math.abs(f1);
        flag1 = flag;
        if((mInitialEdgesTouched[i] & j) != j) goto _L2; else goto _L1
_L1:
        flag1 = flag;
        if((mTrackingEdges & j) == 0) goto _L2; else goto _L3
_L3:
        flag1 = flag;
        if((mEdgeDragsLocked[i] & j) == j) goto _L2; else goto _L4
_L4:
        flag1 = flag;
        if((mEdgeDragsInProgress[i] & j) == j) goto _L2; else goto _L5
_L5:
        if(f > (float)mTouchSlop || f1 > (float)mTouchSlop) goto _L7; else goto _L6
_L6:
        flag1 = flag;
_L2:
        return flag1;
_L7:
        if(f < 0.5F * f1 && mCallback.onEdgeLock(j))
        {
            int ai[] = mEdgeDragsLocked;
            ai[i] = ai[i] | j;
            flag1 = flag;
        } else
        {
            flag1 = flag;
            if((mEdgeDragsInProgress[i] & j) == 0)
            {
                flag1 = flag;
                if(f > (float)mTouchSlop)
                    flag1 = true;
            }
        }
        if(true) goto _L2; else goto _L8
_L8:
    }

    private boolean checkTouchSlop(View view, float f, float f1)
    {
        boolean flag = true;
        if(view != null) goto _L2; else goto _L1
_L1:
        flag = false;
_L4:
        return flag;
_L2:
        boolean flag1;
        boolean flag2;
        if(mCallback.getViewHorizontalDragRange(view) > 0)
            flag1 = true;
        else
            flag1 = false;
        if(mCallback.getViewVerticalDragRange(view) > 0)
            flag2 = true;
        else
            flag2 = false;
        if(flag1 && flag2)
        {
            if(f * f + f1 * f1 <= (float)(mTouchSlop * mTouchSlop))
                flag = false;
        } else
        if(flag1)
        {
            if(Math.abs(f) <= (float)mTouchSlop)
                flag = false;
        } else
        if(flag2)
        {
            if(Math.abs(f1) <= (float)mTouchSlop)
                flag = false;
        } else
        {
            flag = false;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    private float clampMag(float f, float f1, float f2)
    {
        float f3 = Math.abs(f);
        if(f3 >= f1) goto _L2; else goto _L1
_L1:
        f1 = 0.0F;
_L4:
        return f1;
_L2:
        if(f3 > f2)
        {
            f1 = f2;
            if(f <= 0.0F)
                f1 = -f2;
        } else
        {
            f1 = f;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    private int clampMag(int i, int j, int k)
    {
        int l = Math.abs(i);
        if(l >= j) goto _L2; else goto _L1
_L1:
        j = 0;
_L4:
        return j;
_L2:
        if(l > k)
        {
            j = k;
            if(i <= 0)
                j = -k;
        } else
        {
            j = i;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    private void clearMotionHistory()
    {
        if(mInitialMotionX != null)
        {
            Arrays.fill(mInitialMotionX, 0.0F);
            Arrays.fill(mInitialMotionY, 0.0F);
            Arrays.fill(mLastMotionX, 0.0F);
            Arrays.fill(mLastMotionY, 0.0F);
            Arrays.fill(mInitialEdgesTouched, 0);
            Arrays.fill(mEdgeDragsInProgress, 0);
            Arrays.fill(mEdgeDragsLocked, 0);
            mPointersDown = 0;
        }
    }

    private void clearMotionHistory(int i)
    {
        if(mInitialMotionX != null)
        {
            mInitialMotionX[i] = 0.0F;
            mInitialMotionY[i] = 0.0F;
            mLastMotionX[i] = 0.0F;
            mLastMotionY[i] = 0.0F;
            mInitialEdgesTouched[i] = 0;
            mEdgeDragsInProgress[i] = 0;
            mEdgeDragsLocked[i] = 0;
            mPointersDown = mPointersDown & ~(1 << i);
        }
    }

    private int computeAxisDuration(int i, int j, int k)
    {
        if(i == 0)
        {
            i = 0;
        } else
        {
            int l = mParentView.getWidth();
            int i1 = l / 2;
            float f = Math.min(1.0F, (float)Math.abs(i) / (float)l);
            float f1 = i1;
            float f2 = i1;
            f = distanceInfluenceForSnapDuration(f);
            j = Math.abs(j);
            if(j > 0)
                i = Math.round(1000F * Math.abs((f1 + f2 * f) / (float)j)) * 4;
            else
                i = (int)(((float)Math.abs(i) / (float)k + 1.0F) * 256F);
            i = Math.min(i, 600);
        }
        return i;
    }

    private int computeSettleDuration(View view, int i, int j, int k, int l)
    {
        int i1 = clampMag(k, (int)mMinVelocity, (int)mMaxVelocity);
        k = clampMag(l, (int)mMinVelocity, (int)mMaxVelocity);
        int j1 = Math.abs(i);
        int k1 = Math.abs(j);
        int l1 = Math.abs(i1);
        int i2 = Math.abs(k);
        l = l1 + i2;
        int j2 = j1 + k1;
        float f;
        float f1;
        if(i1 != 0)
            f = (float)l1 / (float)l;
        else
            f = (float)j1 / (float)j2;
        if(k != 0)
            f1 = (float)i2 / (float)l;
        else
            f1 = (float)k1 / (float)j2;
        i = computeAxisDuration(i, i1, mCallback.getViewHorizontalDragRange(view));
        j = computeAxisDuration(j, k, mCallback.getViewVerticalDragRange(view));
        return (int)((float)i * f + (float)j * f1);
    }

    public static ViewDragHelper create(ViewGroup viewgroup, float f, Callback callback)
    {
        viewgroup = create(viewgroup, callback);
        viewgroup.mTouchSlop = (int)((float)((ViewDragHelper) (viewgroup)).mTouchSlop * (1.0F / f));
        return viewgroup;
    }

    public static ViewDragHelper create(ViewGroup viewgroup, Callback callback)
    {
        return new ViewDragHelper(viewgroup.getContext(), viewgroup, callback);
    }

    private void dispatchViewReleased(float f, float f1)
    {
        mReleaseInProgress = true;
        mCallback.onViewReleased(mCapturedView, f, f1);
        mReleaseInProgress = false;
        if(mDragState == 1)
            setDragState(0);
    }

    private float distanceInfluenceForSnapDuration(float f)
    {
        return (float)Math.sin((float)((double)(f - 0.5F) * 0.4712389167638204D));
    }

    private void dragTo(int i, int j, int k, int l)
    {
        int i1 = i;
        int j1 = j;
        int k1 = mCapturedView.getLeft();
        int l1 = mCapturedView.getTop();
        if(k != 0)
        {
            i1 = mCallback.clampViewPositionHorizontal(mCapturedView, i, k);
            ViewCompat.offsetLeftAndRight(mCapturedView, i1 - k1);
        }
        if(l != 0)
        {
            j1 = mCallback.clampViewPositionVertical(mCapturedView, j, l);
            ViewCompat.offsetTopAndBottom(mCapturedView, j1 - l1);
        }
        if(k != 0 || l != 0)
            mCallback.onViewPositionChanged(mCapturedView, i1, j1, i1 - k1, j1 - l1);
    }

    private void ensureMotionHistorySizeForId(int i)
    {
        if(mInitialMotionX == null || mInitialMotionX.length <= i)
        {
            float af[] = new float[i + 1];
            float af1[] = new float[i + 1];
            float af2[] = new float[i + 1];
            float af3[] = new float[i + 1];
            int ai[] = new int[i + 1];
            int ai1[] = new int[i + 1];
            int ai2[] = new int[i + 1];
            if(mInitialMotionX != null)
            {
                System.arraycopy(mInitialMotionX, 0, af, 0, mInitialMotionX.length);
                System.arraycopy(mInitialMotionY, 0, af1, 0, mInitialMotionY.length);
                System.arraycopy(mLastMotionX, 0, af2, 0, mLastMotionX.length);
                System.arraycopy(mLastMotionY, 0, af3, 0, mLastMotionY.length);
                System.arraycopy(mInitialEdgesTouched, 0, ai, 0, mInitialEdgesTouched.length);
                System.arraycopy(mEdgeDragsInProgress, 0, ai1, 0, mEdgeDragsInProgress.length);
                System.arraycopy(mEdgeDragsLocked, 0, ai2, 0, mEdgeDragsLocked.length);
            }
            mInitialMotionX = af;
            mInitialMotionY = af1;
            mLastMotionX = af2;
            mLastMotionY = af3;
            mInitialEdgesTouched = ai;
            mEdgeDragsInProgress = ai1;
            mEdgeDragsLocked = ai2;
        }
    }

    private boolean forceSettleCapturedViewAt(int i, int j, int k, int l)
    {
        boolean flag = false;
        int i1 = mCapturedView.getLeft();
        int j1 = mCapturedView.getTop();
        i -= i1;
        j -= j1;
        if(i == 0 && j == 0)
        {
            mScroller.abortAnimation();
            setDragState(0);
        } else
        {
            k = computeSettleDuration(mCapturedView, i, j, k, l);
            mScroller.startScroll(i1, j1, i, j, k);
            setDragState(2);
            flag = true;
        }
        return flag;
    }

    private int getEdgesTouched(int i, int j)
    {
        int k = 0;
        if(i < mParentView.getLeft() + mEdgeSize)
            k = false | true;
        int l = k;
        if(j < mParentView.getTop() + mEdgeSize)
            l = k | 4;
        k = l;
        if(i > mParentView.getRight() - mEdgeSize)
            k = l | 2;
        i = k;
        if(j > mParentView.getBottom() - mEdgeSize)
            i = k | 8;
        return i;
    }

    private boolean isValidPointerForActionMove(int i)
    {
        boolean flag;
        if(!isPointerDown(i))
        {
            Log.e("ViewDragHelper", (new StringBuilder()).append("Ignoring pointerId=").append(i).append(" because ACTION_DOWN was not received ").append("for this pointer before ACTION_MOVE. It likely happened because ").append(" ViewDragHelper did not receive all the events in the event stream.").toString());
            flag = false;
        } else
        {
            flag = true;
        }
        return flag;
    }

    private void releaseViewForPointerUp()
    {
        mVelocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
        dispatchViewReleased(clampMag(VelocityTrackerCompat.getXVelocity(mVelocityTracker, mActivePointerId), mMinVelocity, mMaxVelocity), clampMag(VelocityTrackerCompat.getYVelocity(mVelocityTracker, mActivePointerId), mMinVelocity, mMaxVelocity));
    }

    private void reportNewEdgeDrags(float f, float f1, int i)
    {
        int j = 0;
        if(checkNewEdgeDrag(f, f1, i, 1))
            j = false | true;
        int k = j;
        if(checkNewEdgeDrag(f1, f, i, 4))
            k = j | 4;
        j = k;
        if(checkNewEdgeDrag(f, f1, i, 2))
            j = k | 2;
        k = j;
        if(checkNewEdgeDrag(f1, f, i, 8))
            k = j | 8;
        if(k != 0)
        {
            int ai[] = mEdgeDragsInProgress;
            ai[i] = ai[i] | k;
            mCallback.onEdgeDragStarted(k, i);
        }
    }

    private void saveInitialMotion(float f, float f1, int i)
    {
        ensureMotionHistorySizeForId(i);
        float af[] = mInitialMotionX;
        mLastMotionX[i] = f;
        af[i] = f;
        af = mInitialMotionY;
        mLastMotionY[i] = f1;
        af[i] = f1;
        mInitialEdgesTouched[i] = getEdgesTouched((int)f, (int)f1);
        mPointersDown = mPointersDown | 1 << i;
    }

    private void saveLastMotion(MotionEvent motionevent)
    {
        int i = MotionEventCompat.getPointerCount(motionevent);
        for(int j = 0; j < i; j++)
        {
            int k = MotionEventCompat.getPointerId(motionevent, j);
            float f = MotionEventCompat.getX(motionevent, j);
            float f1 = MotionEventCompat.getY(motionevent, j);
            mLastMotionX[k] = f;
            mLastMotionY[k] = f1;
        }

    }

    public void abort()
    {
        cancel();
        if(mDragState == 2)
        {
            int i = mScroller.getCurrX();
            int j = mScroller.getCurrY();
            mScroller.abortAnimation();
            int k = mScroller.getCurrX();
            int l = mScroller.getCurrY();
            mCallback.onViewPositionChanged(mCapturedView, k, l, k - i, l - j);
        }
        setDragState(0);
    }

    protected boolean canScroll(View view, boolean flag, int i, int j, int k, int l)
    {
        ViewGroup viewgroup;
        int i1;
        int j1;
        int k1;
        if(!(view instanceof ViewGroup))
            break MISSING_BLOCK_LABEL_145;
        viewgroup = (ViewGroup)view;
        i1 = view.getScrollX();
        j1 = view.getScrollY();
        k1 = viewgroup.getChildCount() - 1;
_L3:
        View view1;
        if(k1 < 0)
            break MISSING_BLOCK_LABEL_145;
        view1 = viewgroup.getChildAt(k1);
        if(k + i1 < view1.getLeft() || k + i1 >= view1.getRight() || l + j1 < view1.getTop() || l + j1 >= view1.getBottom() || !canScroll(view1, true, i, j, (k + i1) - view1.getLeft(), (l + j1) - view1.getTop())) goto _L2; else goto _L1
_L1:
        flag = true;
_L4:
        return flag;
_L2:
        k1--;
          goto _L3
        if(flag && (ViewCompat.canScrollHorizontally(view, -i) || ViewCompat.canScrollVertically(view, -j)))
            flag = true;
        else
            flag = false;
          goto _L4
    }

    public void cancel()
    {
        mActivePointerId = -1;
        clearMotionHistory();
        if(mVelocityTracker != null)
        {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    public void captureChildView(View view, int i)
    {
        if(view.getParent() != mParentView)
        {
            throw new IllegalArgumentException((new StringBuilder()).append("captureChildView: parameter must be a descendant of the ViewDragHelper's tracked parent view (").append(mParentView).append(")").toString());
        } else
        {
            mCapturedView = view;
            mActivePointerId = i;
            mCallback.onViewCaptured(view, i);
            setDragState(1);
            return;
        }
    }

    public boolean checkTouchSlop(int i)
    {
        int j;
        int k;
        j = mInitialMotionX.length;
        k = 0;
_L3:
        if(k >= j)
            break MISSING_BLOCK_LABEL_34;
        if(!checkTouchSlop(i, k)) goto _L2; else goto _L1
_L1:
        boolean flag = true;
_L4:
        return flag;
_L2:
        k++;
          goto _L3
        flag = false;
          goto _L4
    }

    public boolean checkTouchSlop(int i, int j)
    {
        boolean flag = true;
        if(isPointerDown(j)) goto _L2; else goto _L1
_L1:
        flag = false;
_L4:
        return flag;
_L2:
        boolean flag1;
        float f;
        float f1;
        if((i & 1) == 1)
            flag1 = true;
        else
            flag1 = false;
        if((i & 2) == 2)
            i = 1;
        else
            i = 0;
        f = mLastMotionX[j] - mInitialMotionX[j];
        f1 = mLastMotionY[j] - mInitialMotionY[j];
        if(flag1 && i != 0)
        {
            if(f * f + f1 * f1 <= (float)(mTouchSlop * mTouchSlop))
                flag = false;
        } else
        if(flag1)
        {
            if(Math.abs(f) <= (float)mTouchSlop)
                flag = false;
        } else
        if(i != 0)
        {
            if(Math.abs(f1) <= (float)mTouchSlop)
                flag = false;
        } else
        {
            flag = false;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public boolean continueSettling(boolean flag)
    {
        if(mDragState == 2)
        {
            boolean flag1 = mScroller.computeScrollOffset();
            int i = mScroller.getCurrX();
            int j = mScroller.getCurrY();
            int k = i - mCapturedView.getLeft();
            int l = j - mCapturedView.getTop();
            if(k != 0)
                ViewCompat.offsetLeftAndRight(mCapturedView, k);
            if(l != 0)
                ViewCompat.offsetTopAndBottom(mCapturedView, l);
            if(k != 0 || l != 0)
                mCallback.onViewPositionChanged(mCapturedView, i, j, k, l);
            boolean flag2 = flag1;
            if(flag1)
            {
                flag2 = flag1;
                if(i == mScroller.getFinalX())
                {
                    flag2 = flag1;
                    if(j == mScroller.getFinalY())
                    {
                        mScroller.abortAnimation();
                        flag2 = false;
                    }
                }
            }
            if(!flag2)
                if(flag)
                    mParentView.post(mSetIdleRunnable);
                else
                    setDragState(0);
        }
        if(mDragState == 2)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public View findTopChildUnder(int i, int j)
    {
        int k = mParentView.getChildCount() - 1;
_L3:
        View view;
        if(k < 0)
            break MISSING_BLOCK_LABEL_76;
        view = mParentView.getChildAt(mCallback.getOrderedChildIndex(k));
        if(i < view.getLeft() || i >= view.getRight() || j < view.getTop() || j >= view.getBottom()) goto _L2; else goto _L1
_L1:
        return view;
_L2:
        k--;
          goto _L3
        view = null;
          goto _L1
    }

    public void flingCapturedView(int i, int j, int k, int l)
    {
        if(!mReleaseInProgress)
        {
            throw new IllegalStateException("Cannot flingCapturedView outside of a call to Callback#onViewReleased");
        } else
        {
            mScroller.fling(mCapturedView.getLeft(), mCapturedView.getTop(), (int)VelocityTrackerCompat.getXVelocity(mVelocityTracker, mActivePointerId), (int)VelocityTrackerCompat.getYVelocity(mVelocityTracker, mActivePointerId), i, k, j, l);
            setDragState(2);
            return;
        }
    }

    public int getActivePointerId()
    {
        return mActivePointerId;
    }

    public View getCapturedView()
    {
        return mCapturedView;
    }

    public int getEdgeSize()
    {
        return mEdgeSize;
    }

    public float getMinVelocity()
    {
        return mMinVelocity;
    }

    public int getTouchSlop()
    {
        return mTouchSlop;
    }

    public int getViewDragState()
    {
        return mDragState;
    }

    public boolean isCapturedViewUnder(int i, int j)
    {
        return isViewUnder(mCapturedView, i, j);
    }

    public boolean isEdgeTouched(int i)
    {
        int j;
        int k;
        j = mInitialEdgesTouched.length;
        k = 0;
_L3:
        if(k >= j)
            break MISSING_BLOCK_LABEL_34;
        if(!isEdgeTouched(i, k)) goto _L2; else goto _L1
_L1:
        boolean flag = true;
_L4:
        return flag;
_L2:
        k++;
          goto _L3
        flag = false;
          goto _L4
    }

    public boolean isEdgeTouched(int i, int j)
    {
        boolean flag;
        if(isPointerDown(j) && (mInitialEdgesTouched[j] & i) != 0)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public boolean isPointerDown(int i)
    {
        boolean flag = true;
        if((mPointersDown & 1 << i) == 0)
            flag = false;
        return flag;
    }

    public boolean isViewUnder(View view, int i, int j)
    {
        boolean flag = false;
        if(view != null) goto _L2; else goto _L1
_L1:
        boolean flag1 = flag;
_L4:
        return flag1;
_L2:
        flag1 = flag;
        if(i >= view.getLeft())
        {
            flag1 = flag;
            if(i < view.getRight())
            {
                flag1 = flag;
                if(j >= view.getTop())
                {
                    flag1 = flag;
                    if(j < view.getBottom())
                        flag1 = true;
                }
            }
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void processTouchEvent(MotionEvent motionevent)
    {
        int i;
        int j;
        i = MotionEventCompat.getActionMasked(motionevent);
        j = MotionEventCompat.getActionIndex(motionevent);
        if(i == 0)
            cancel();
        if(mVelocityTracker == null)
            mVelocityTracker = VelocityTracker.obtain();
        mVelocityTracker.addMovement(motionevent);
        i;
        JVM INSTR tableswitch 0 6: default 84
    //                   0 85
    //                   1 643
    //                   2 274
    //                   3 662
    //                   4 84
    //                   5 163
    //                   6 513;
           goto _L1 _L2 _L3 _L4 _L5 _L1 _L6 _L7
_L1:
        return;
_L2:
        float f = motionevent.getX();
        float f4 = motionevent.getY();
        i = MotionEventCompat.getPointerId(motionevent, 0);
        motionevent = findTopChildUnder((int)f, (int)f4);
        saveInitialMotion(f, f4, i);
        tryCaptureViewForDrag(motionevent, i);
        j = mInitialEdgesTouched[i];
        if((mTrackingEdges & j) != 0)
            mCallback.onEdgeTouched(mTrackingEdges & j, i);
        continue; /* Loop/switch isn't completed */
_L6:
        i = MotionEventCompat.getPointerId(motionevent, j);
        float f5 = MotionEventCompat.getX(motionevent, j);
        float f1 = MotionEventCompat.getY(motionevent, j);
        saveInitialMotion(f5, f1, i);
        if(mDragState == 0)
        {
            tryCaptureViewForDrag(findTopChildUnder((int)f5, (int)f1), i);
            j = mInitialEdgesTouched[i];
            if((mTrackingEdges & j) != 0)
                mCallback.onEdgeTouched(mTrackingEdges & j, i);
        } else
        if(isCapturedViewUnder((int)f5, (int)f1))
            tryCaptureViewForDrag(mCapturedView, i);
        continue; /* Loop/switch isn't completed */
_L4:
        if(mDragState == 1)
        {
            if(isValidPointerForActionMove(mActivePointerId))
            {
                j = MotionEventCompat.findPointerIndex(motionevent, mActivePointerId);
                float f6 = MotionEventCompat.getX(motionevent, j);
                float f2 = MotionEventCompat.getY(motionevent, j);
                i = (int)(f6 - mLastMotionX[mActivePointerId]);
                j = (int)(f2 - mLastMotionY[mActivePointerId]);
                dragTo(mCapturedView.getLeft() + i, mCapturedView.getTop() + j, i, j);
                saveLastMotion(motionevent);
            }
            continue; /* Loop/switch isn't completed */
        }
        i = MotionEventCompat.getPointerCount(motionevent);
        j = 0;
_L12:
        if(j >= i) goto _L9; else goto _L8
_L8:
        int k = MotionEventCompat.getPointerId(motionevent, j);
        if(isValidPointerForActionMove(k)) goto _L11; else goto _L10
_L10:
        j++;
          goto _L12
_L11:
        float f3;
        float f7;
        float f8;
        float f9;
        f7 = MotionEventCompat.getX(motionevent, j);
        f8 = MotionEventCompat.getY(motionevent, j);
        f3 = f7 - mInitialMotionX[k];
        f9 = f8 - mInitialMotionY[k];
        reportNewEdgeDrags(f3, f9, k);
        if(mDragState != 1) goto _L13; else goto _L9
_L9:
        saveLastMotion(motionevent);
        continue; /* Loop/switch isn't completed */
_L13:
        View view = findTopChildUnder((int)f7, (int)f8);
        if(!checkTouchSlop(view, f3, f9) || !tryCaptureViewForDrag(view, k)) goto _L10; else goto _L9
_L7:
        int l;
        int i1;
        l = MotionEventCompat.getPointerId(motionevent, j);
        if(mDragState != 1 || l != mActivePointerId)
            break MISSING_BLOCK_LABEL_634;
        k = -1;
        i1 = MotionEventCompat.getPointerCount(motionevent);
        j = 0;
_L15:
        i = k;
        if(j >= i1)
            break MISSING_BLOCK_LABEL_625;
        i = MotionEventCompat.getPointerId(motionevent, j);
        if(i != mActivePointerId)
            break; /* Loop/switch isn't completed */
_L17:
        j++;
        if(true) goto _L15; else goto _L14
_L14:
        f3 = MotionEventCompat.getX(motionevent, j);
        f7 = MotionEventCompat.getY(motionevent, j);
        if(findTopChildUnder((int)f3, (int)f7) != mCapturedView || !tryCaptureViewForDrag(mCapturedView, i)) goto _L17; else goto _L16
_L16:
        i = mActivePointerId;
        if(i == -1)
            releaseViewForPointerUp();
        clearMotionHistory(l);
        continue; /* Loop/switch isn't completed */
_L3:
        if(mDragState == 1)
            releaseViewForPointerUp();
        cancel();
        continue; /* Loop/switch isn't completed */
_L5:
        if(mDragState == 1)
            dispatchViewReleased(0.0F, 0.0F);
        cancel();
        if(true) goto _L1; else goto _L18
_L18:
    }

    void setDragState(int i)
    {
        mParentView.removeCallbacks(mSetIdleRunnable);
        if(mDragState != i)
        {
            mDragState = i;
            mCallback.onViewDragStateChanged(i);
            if(mDragState == 0)
                mCapturedView = null;
        }
    }

    public void setEdgeTrackingEnabled(int i)
    {
        mTrackingEdges = i;
    }

    public void setMinVelocity(float f)
    {
        mMinVelocity = f;
    }

    public boolean settleCapturedViewAt(int i, int j)
    {
        if(!mReleaseInProgress)
            throw new IllegalStateException("Cannot settleCapturedViewAt outside of a call to Callback#onViewReleased");
        else
            return forceSettleCapturedViewAt(i, j, (int)VelocityTrackerCompat.getXVelocity(mVelocityTracker, mActivePointerId), (int)VelocityTrackerCompat.getYVelocity(mVelocityTracker, mActivePointerId));
    }

    public boolean shouldInterceptTouchEvent(MotionEvent motionevent)
    {
        int i;
        int k;
        i = MotionEventCompat.getActionMasked(motionevent);
        k = MotionEventCompat.getActionIndex(motionevent);
        if(i == 0)
            cancel();
        if(mVelocityTracker == null)
            mVelocityTracker = VelocityTracker.obtain();
        mVelocityTracker.addMovement(motionevent);
        i;
        JVM INSTR tableswitch 0 6: default 84
    //                   0 98
    //                   1 601
    //                   2 298
    //                   3 601
    //                   4 84
    //                   5 192
    //                   6 589;
           goto _L1 _L2 _L3 _L4 _L3 _L1 _L5 _L6
_L1:
        break; /* Loop/switch isn't completed */
_L3:
        break MISSING_BLOCK_LABEL_601;
_L7:
        int j;
        boolean flag;
        float f;
        float f1;
        int l;
        int i1;
        float f2;
        float f3;
        View view;
        int j1;
        int k1;
        int l1;
        int i2;
        int j2;
        int k2;
        if(mDragState == 1)
            flag = true;
        else
            flag = false;
        return flag;
_L2:
        f = motionevent.getX();
        f1 = motionevent.getY();
        k = MotionEventCompat.getPointerId(motionevent, 0);
        saveInitialMotion(f, f1, k);
        motionevent = findTopChildUnder((int)f, (int)f1);
        if(motionevent == mCapturedView && mDragState == 2)
            tryCaptureViewForDrag(motionevent, k);
        j = mInitialEdgesTouched[k];
        if((mTrackingEdges & j) != 0)
            mCallback.onEdgeTouched(mTrackingEdges & j, k);
          goto _L7
_L5:
        j = MotionEventCompat.getPointerId(motionevent, k);
        f = MotionEventCompat.getX(motionevent, k);
        f1 = MotionEventCompat.getY(motionevent, k);
        saveInitialMotion(f, f1, j);
        if(mDragState == 0)
        {
            k = mInitialEdgesTouched[j];
            if((mTrackingEdges & k) != 0)
                mCallback.onEdgeTouched(mTrackingEdges & k, j);
        } else
        if(mDragState == 2)
        {
            motionevent = findTopChildUnder((int)f, (int)f1);
            if(motionevent == mCapturedView)
                tryCaptureViewForDrag(motionevent, j);
        }
          goto _L7
_L4:
        if(mInitialMotionX == null || mInitialMotionY == null) goto _L7; else goto _L8
_L8:
        l = MotionEventCompat.getPointerCount(motionevent);
        k = 0;
_L13:
        if(k >= l) goto _L10; else goto _L9
_L9:
        i1 = MotionEventCompat.getPointerId(motionevent, k);
        if(isValidPointerForActionMove(i1)) goto _L12; else goto _L11
_L11:
        k++;
          goto _L13
_L12:
        f1 = MotionEventCompat.getX(motionevent, k);
        f2 = MotionEventCompat.getY(motionevent, k);
        f3 = f1 - mInitialMotionX[i1];
        f = f2 - mInitialMotionY[i1];
        view = findTopChildUnder((int)f1, (int)f2);
        if(view != null && checkTouchSlop(view, f3, f))
            j = 1;
        else
            j = 0;
        if(!j) goto _L15; else goto _L14
_L14:
        j1 = view.getLeft();
        k1 = (int)f3;
        k1 = mCallback.clampViewPositionHorizontal(view, j1 + k1, (int)f3);
        l1 = view.getTop();
        i2 = (int)f;
        j2 = mCallback.clampViewPositionVertical(view, l1 + i2, (int)f);
        k2 = mCallback.getViewHorizontalDragRange(view);
        i2 = mCallback.getViewVerticalDragRange(view);
        if(k2 != 0 && (k2 <= 0 || k1 != j1) || i2 != 0 && (i2 <= 0 || j2 != l1)) goto _L15; else goto _L10
_L10:
        saveLastMotion(motionevent);
          goto _L7
_L15:
        reportNewEdgeDrags(f3, f, i1);
        if(mDragState != 1 && (!j || !tryCaptureViewForDrag(view, i1))) goto _L11; else goto _L10
_L6:
        clearMotionHistory(MotionEventCompat.getPointerId(motionevent, k));
          goto _L7
        cancel();
          goto _L7
    }

    public boolean smoothSlideViewTo(View view, int i, int j)
    {
        mCapturedView = view;
        mActivePointerId = -1;
        boolean flag = forceSettleCapturedViewAt(i, j, 0, 0);
        if(!flag && mDragState == 0 && mCapturedView != null)
            mCapturedView = null;
        return flag;
    }

    boolean tryCaptureViewForDrag(View view, int i)
    {
        boolean flag = true;
        if(view != mCapturedView || mActivePointerId != i)
            if(view != null && mCallback.tryCaptureView(view, i))
            {
                mActivePointerId = i;
                captureChildView(view, i);
            } else
            {
                flag = false;
            }
        return flag;
    }

    private static final int BASE_SETTLE_DURATION = 256;
    public static final int DIRECTION_ALL = 3;
    public static final int DIRECTION_HORIZONTAL = 1;
    public static final int DIRECTION_VERTICAL = 2;
    public static final int EDGE_ALL = 15;
    public static final int EDGE_BOTTOM = 8;
    public static final int EDGE_LEFT = 1;
    public static final int EDGE_RIGHT = 2;
    private static final int EDGE_SIZE = 20;
    public static final int EDGE_TOP = 4;
    public static final int INVALID_POINTER = -1;
    private static final int MAX_SETTLE_DURATION = 600;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    private static final String TAG = "ViewDragHelper";
    private static final Interpolator sInterpolator = new Interpolator() {

        public float getInterpolation(float f)
        {
            f--;
            return f * f * f * f * f + 1.0F;
        }

    }
;
    private int mActivePointerId;
    private final Callback mCallback;
    private View mCapturedView;
    private int mDragState;
    private int mEdgeDragsInProgress[];
    private int mEdgeDragsLocked[];
    private int mEdgeSize;
    private int mInitialEdgesTouched[];
    private float mInitialMotionX[];
    private float mInitialMotionY[];
    private float mLastMotionX[];
    private float mLastMotionY[];
    private float mMaxVelocity;
    private float mMinVelocity;
    private final ViewGroup mParentView;
    private int mPointersDown;
    private boolean mReleaseInProgress;
    private ScrollerCompat mScroller;
    private final Runnable mSetIdleRunnable = new Runnable() {

        public void run()
        {
            setDragState(0);
        }

        final ViewDragHelper this$0;

            
            {
                this$0 = ViewDragHelper.this;
                super();
            }
    }
;
    private int mTouchSlop;
    private int mTrackingEdges;
    private VelocityTracker mVelocityTracker;

}
