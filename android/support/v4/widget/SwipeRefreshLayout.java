// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.view.*;
import android.util.*;
import android.view.*;
import android.view.animation.*;
import android.widget.AbsListView;

// Referenced classes of package android.support.v4.widget:
//            CircleImageView, MaterialProgressDrawable

public class SwipeRefreshLayout extends ViewGroup
    implements NestedScrollingParent, NestedScrollingChild
{
    public static interface OnRefreshListener
    {

        public abstract void onRefresh();
    }


    public SwipeRefreshLayout(Context context)
    {
        this(context, null);
    }

    public SwipeRefreshLayout(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        mRefreshing = false;
        mTotalDragDistance = -1F;
        mParentScrollConsumed = new int[2];
        mParentOffsetInWindow = new int[2];
        mOriginalOffsetCalculated = false;
        mActivePointerId = -1;
        mCircleViewIndex = -1;
        mAnimateToCorrectPosition = new Animation() {

            public void applyTransformation(float f, Transformation transformation)
            {
                int i;
                int j;
                int k;
                if(!mUsingCustomStart)
                    i = (int)(mSpinnerFinalOffset - (float)Math.abs(mOriginalOffsetTop));
                else
                    i = (int)mSpinnerFinalOffset;
                j = mFrom;
                k = (int)((float)(i - mFrom) * f);
                i = mCircleView.getTop();
                setTargetOffsetTopAndBottom((j + k) - i, false);
                mProgress.setArrowScale(1.0F - f);
            }

            final SwipeRefreshLayout this$0;

            
            {
                this$0 = SwipeRefreshLayout.this;
                super();
            }
        }
;
        mAnimateToStartPosition = new Animation() {

            public void applyTransformation(float f, Transformation transformation)
            {
                moveToStart(f);
            }

            final SwipeRefreshLayout this$0;

            
            {
                this$0 = SwipeRefreshLayout.this;
                super();
            }
        }
;
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMediumAnimationDuration = getResources().getInteger(0x10e0001);
        setWillNotDraw(false);
        mDecelerateInterpolator = new DecelerateInterpolator(2.0F);
        context = context.obtainStyledAttributes(attributeset, LAYOUT_ATTRS);
        setEnabled(context.getBoolean(0, true));
        context.recycle();
        context = getResources().getDisplayMetrics();
        mCircleWidth = (int)(((DisplayMetrics) (context)).density * 40F);
        mCircleHeight = (int)(((DisplayMetrics) (context)).density * 40F);
        createProgressView();
        ViewCompat.setChildrenDrawingOrderEnabled(this, true);
        mSpinnerFinalOffset = 64F * ((DisplayMetrics) (context)).density;
        mTotalDragDistance = mSpinnerFinalOffset;
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
    }

    private void animateOffsetToCorrectPosition(int i, android.view.animation.Animation.AnimationListener animationlistener)
    {
        mFrom = i;
        mAnimateToCorrectPosition.reset();
        mAnimateToCorrectPosition.setDuration(200L);
        mAnimateToCorrectPosition.setInterpolator(mDecelerateInterpolator);
        if(animationlistener != null)
            mCircleView.setAnimationListener(animationlistener);
        mCircleView.clearAnimation();
        mCircleView.startAnimation(mAnimateToCorrectPosition);
    }

    private void animateOffsetToStartPosition(int i, android.view.animation.Animation.AnimationListener animationlistener)
    {
        if(mScale)
        {
            startScaleDownReturnToStartAnimation(i, animationlistener);
        } else
        {
            mFrom = i;
            mAnimateToStartPosition.reset();
            mAnimateToStartPosition.setDuration(200L);
            mAnimateToStartPosition.setInterpolator(mDecelerateInterpolator);
            if(animationlistener != null)
                mCircleView.setAnimationListener(animationlistener);
            mCircleView.clearAnimation();
            mCircleView.startAnimation(mAnimateToStartPosition);
        }
    }

    private void createProgressView()
    {
        mCircleView = new CircleImageView(getContext(), 0xfffafafa, 20F);
        mProgress = new MaterialProgressDrawable(getContext(), this);
        mProgress.setBackgroundColor(0xfffafafa);
        mCircleView.setImageDrawable(mProgress);
        mCircleView.setVisibility(8);
        addView(mCircleView);
    }

    private void ensureTarget()
    {
        if(mTarget != null) goto _L2; else goto _L1
_L1:
        int i = 0;
_L7:
        if(i >= getChildCount()) goto _L2; else goto _L3
_L3:
        View view = getChildAt(i);
        if(view.equals(mCircleView)) goto _L5; else goto _L4
_L4:
        mTarget = view;
_L2:
        return;
_L5:
        i++;
        if(true) goto _L7; else goto _L6
_L6:
    }

    private void finishSpinner(float f)
    {
        if(f > mTotalDragDistance)
        {
            setRefreshing(true, true);
        } else
        {
            mRefreshing = false;
            mProgress.setStartEndTrim(0.0F, 0.0F);
            android.view.animation.Animation.AnimationListener animationlistener = null;
            if(!mScale)
                animationlistener = new android.view.animation.Animation.AnimationListener() {

                    public void onAnimationEnd(Animation animation)
                    {
                        if(!mScale)
                            startScaleDownAnimation(null);
                    }

                    public void onAnimationRepeat(Animation animation)
                    {
                    }

                    public void onAnimationStart(Animation animation)
                    {
                    }

                    final SwipeRefreshLayout this$0;

            
            {
                this$0 = SwipeRefreshLayout.this;
                super();
            }
                }
;
            animateOffsetToStartPosition(mCurrentTargetOffsetTop, animationlistener);
            mProgress.showArrow(false);
        }
    }

    private float getMotionEventY(MotionEvent motionevent, int i)
    {
        i = MotionEventCompat.findPointerIndex(motionevent, i);
        float f;
        if(i < 0)
            f = -1F;
        else
            f = MotionEventCompat.getY(motionevent, i);
        return f;
    }

    private boolean isAlphaUsedForScale()
    {
        boolean flag;
        if(android.os.Build.VERSION.SDK_INT < 11)
            flag = true;
        else
            flag = false;
        return flag;
    }

    private boolean isAnimationRunning(Animation animation)
    {
        boolean flag;
        if(animation != null && animation.hasStarted() && !animation.hasEnded())
            flag = true;
        else
            flag = false;
        return flag;
    }

    private void moveSpinner(float f)
    {
        mProgress.showArrow(true);
        float f1 = Math.min(1.0F, Math.abs(f / mTotalDragDistance));
        float f2 = ((float)Math.max((double)f1 - 0.40000000000000002D, 0.0D) * 5F) / 3F;
        float f3 = Math.abs(f);
        float f4 = mTotalDragDistance;
        float f5;
        int i;
        int j;
        if(mUsingCustomStart)
            f5 = mSpinnerFinalOffset - (float)mOriginalOffsetTop;
        else
            f5 = mSpinnerFinalOffset;
        f3 = Math.max(0.0F, Math.min(f3 - f4, 2.0F * f5) / f5);
        f3 = (float)((double)(f3 / 4F) - Math.pow(f3 / 4F, 2D)) * 2.0F;
        i = mOriginalOffsetTop;
        j = (int)(f5 * f1 + f5 * f3 * 2.0F);
        if(mCircleView.getVisibility() != 0)
            mCircleView.setVisibility(0);
        if(!mScale)
        {
            ViewCompat.setScaleX(mCircleView, 1.0F);
            ViewCompat.setScaleY(mCircleView, 1.0F);
        }
        if(mScale)
            setAnimationProgress(Math.min(1.0F, f / mTotalDragDistance));
        if(f >= mTotalDragDistance) goto _L2; else goto _L1
_L1:
        if(mProgress.getAlpha() > 76 && !isAnimationRunning(mAlphaStartAnimation))
            startProgressAlphaStartAnimation();
_L4:
        mProgress.setStartEndTrim(0.0F, Math.min(0.8F, f2 * 0.8F));
        mProgress.setArrowScale(Math.min(1.0F, f2));
        mProgress.setProgressRotation((-0.25F + 0.4F * f2 + 2.0F * f3) * 0.5F);
        setTargetOffsetTopAndBottom((i + j) - mCurrentTargetOffsetTop, true);
        return;
_L2:
        if(mProgress.getAlpha() < 255 && !isAnimationRunning(mAlphaMaxAnimation))
            startProgressAlphaMaxAnimation();
        if(true) goto _L4; else goto _L3
_L3:
    }

    private void moveToStart(float f)
    {
        setTargetOffsetTopAndBottom((mFrom + (int)((float)(mOriginalOffsetTop - mFrom) * f)) - mCircleView.getTop(), false);
    }

    private void onSecondaryPointerUp(MotionEvent motionevent)
    {
        int i = MotionEventCompat.getActionIndex(motionevent);
        if(MotionEventCompat.getPointerId(motionevent, i) == mActivePointerId)
        {
            if(i == 0)
                i = 1;
            else
                i = 0;
            mActivePointerId = MotionEventCompat.getPointerId(motionevent, i);
        }
    }

    private void reset()
    {
        mCircleView.clearAnimation();
        mProgress.stop();
        mCircleView.setVisibility(8);
        setColorViewAlpha(255);
        if(mScale)
            setAnimationProgress(0.0F);
        else
            setTargetOffsetTopAndBottom(mOriginalOffsetTop - mCurrentTargetOffsetTop, true);
        mCurrentTargetOffsetTop = mCircleView.getTop();
    }

    private void setAnimationProgress(float f)
    {
        if(isAlphaUsedForScale())
        {
            setColorViewAlpha((int)(255F * f));
        } else
        {
            ViewCompat.setScaleX(mCircleView, f);
            ViewCompat.setScaleY(mCircleView, f);
        }
    }

    private void setColorViewAlpha(int i)
    {
        mCircleView.getBackground().setAlpha(i);
        mProgress.setAlpha(i);
    }

    private void setRefreshing(boolean flag, boolean flag1)
    {
        if(mRefreshing != flag)
        {
            mNotify = flag1;
            ensureTarget();
            mRefreshing = flag;
            if(mRefreshing)
                animateOffsetToCorrectPosition(mCurrentTargetOffsetTop, mRefreshListener);
            else
                startScaleDownAnimation(mRefreshListener);
        }
    }

    private void setTargetOffsetTopAndBottom(int i, boolean flag)
    {
        mCircleView.bringToFront();
        mCircleView.offsetTopAndBottom(i);
        mCurrentTargetOffsetTop = mCircleView.getTop();
        if(flag && android.os.Build.VERSION.SDK_INT < 11)
            invalidate();
    }

    private Animation startAlphaAnimation(final int startingAlpha, final int endingAlpha)
    {
        Object obj;
        if(mScale && isAlphaUsedForScale())
        {
            obj = null;
        } else
        {
            obj = new Animation() {

                public void applyTransformation(float f, Transformation transformation)
                {
                    mProgress.setAlpha((int)((float)startingAlpha + (float)(endingAlpha - startingAlpha) * f));
                }

                final SwipeRefreshLayout this$0;
                final int val$endingAlpha;
                final int val$startingAlpha;

            
            {
                this$0 = SwipeRefreshLayout.this;
                startingAlpha = i;
                endingAlpha = j;
                super();
            }
            }
;
            ((Animation) (obj)).setDuration(300L);
            mCircleView.setAnimationListener(null);
            mCircleView.clearAnimation();
            mCircleView.startAnimation(((Animation) (obj)));
        }
        return ((Animation) (obj));
    }

    private void startProgressAlphaMaxAnimation()
    {
        mAlphaMaxAnimation = startAlphaAnimation(mProgress.getAlpha(), 255);
    }

    private void startProgressAlphaStartAnimation()
    {
        mAlphaStartAnimation = startAlphaAnimation(mProgress.getAlpha(), 76);
    }

    private void startScaleDownAnimation(android.view.animation.Animation.AnimationListener animationlistener)
    {
        mScaleDownAnimation = new Animation() {

            public void applyTransformation(float f, Transformation transformation)
            {
                setAnimationProgress(1.0F - f);
            }

            final SwipeRefreshLayout this$0;

            
            {
                this$0 = SwipeRefreshLayout.this;
                super();
            }
        }
;
        mScaleDownAnimation.setDuration(150L);
        mCircleView.setAnimationListener(animationlistener);
        mCircleView.clearAnimation();
        mCircleView.startAnimation(mScaleDownAnimation);
    }

    private void startScaleDownReturnToStartAnimation(int i, android.view.animation.Animation.AnimationListener animationlistener)
    {
        mFrom = i;
        if(isAlphaUsedForScale())
            mStartingScale = mProgress.getAlpha();
        else
            mStartingScale = ViewCompat.getScaleX(mCircleView);
        mScaleDownToStartAnimation = new Animation() {

            public void applyTransformation(float f, Transformation transformation)
            {
                float f1 = mStartingScale;
                float f2 = -mStartingScale;
                setAnimationProgress(f1 + f2 * f);
                moveToStart(f);
            }

            final SwipeRefreshLayout this$0;

            
            {
                this$0 = SwipeRefreshLayout.this;
                super();
            }
        }
;
        mScaleDownToStartAnimation.setDuration(150L);
        if(animationlistener != null)
            mCircleView.setAnimationListener(animationlistener);
        mCircleView.clearAnimation();
        mCircleView.startAnimation(mScaleDownToStartAnimation);
    }

    private void startScaleUpAnimation(android.view.animation.Animation.AnimationListener animationlistener)
    {
        mCircleView.setVisibility(0);
        if(android.os.Build.VERSION.SDK_INT >= 11)
            mProgress.setAlpha(255);
        mScaleAnimation = new Animation() {

            public void applyTransformation(float f, Transformation transformation)
            {
                setAnimationProgress(f);
            }

            final SwipeRefreshLayout this$0;

            
            {
                this$0 = SwipeRefreshLayout.this;
                super();
            }
        }
;
        mScaleAnimation.setDuration(mMediumAnimationDuration);
        if(animationlistener != null)
            mCircleView.setAnimationListener(animationlistener);
        mCircleView.clearAnimation();
        mCircleView.startAnimation(mScaleAnimation);
    }

    public boolean canChildScrollUp()
    {
        boolean flag;
        boolean flag1;
        flag = true;
        flag1 = false;
        if(android.os.Build.VERSION.SDK_INT >= 14) goto _L2; else goto _L1
_L1:
        if(!(mTarget instanceof AbsListView)) goto _L4; else goto _L3
_L3:
        AbsListView abslistview = (AbsListView)mTarget;
        if(abslistview.getChildCount() <= 0) goto _L6; else goto _L5
_L5:
        flag1 = flag;
        if(abslistview.getFirstVisiblePosition() > 0) goto _L8; else goto _L7
_L7:
        if(abslistview.getChildAt(0).getTop() >= abslistview.getPaddingTop()) goto _L6; else goto _L9
_L9:
        flag1 = flag;
_L8:
        return flag1;
_L6:
        flag1 = false;
        continue; /* Loop/switch isn't completed */
_L4:
        if(ViewCompat.canScrollVertically(mTarget, -1) || mTarget.getScrollY() > 0)
            flag1 = true;
        continue; /* Loop/switch isn't completed */
_L2:
        flag1 = ViewCompat.canScrollVertically(mTarget, -1);
        if(true) goto _L8; else goto _L10
_L10:
    }

    public boolean dispatchNestedFling(float f, float f1, boolean flag)
    {
        return mNestedScrollingChildHelper.dispatchNestedFling(f, f1, flag);
    }

    public boolean dispatchNestedPreFling(float f, float f1)
    {
        return mNestedScrollingChildHelper.dispatchNestedPreFling(f, f1);
    }

    public boolean dispatchNestedPreScroll(int i, int j, int ai[], int ai1[])
    {
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(i, j, ai, ai1);
    }

    public boolean dispatchNestedScroll(int i, int j, int k, int l, int ai[])
    {
        return mNestedScrollingChildHelper.dispatchNestedScroll(i, j, k, l, ai);
    }

    protected int getChildDrawingOrder(int i, int j)
    {
        if(mCircleViewIndex >= 0) goto _L2; else goto _L1
_L1:
        i = j;
_L4:
        return i;
_L2:
        if(j == i - 1)
        {
            i = mCircleViewIndex;
        } else
        {
            i = j;
            if(j >= mCircleViewIndex)
                i = j + 1;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public int getNestedScrollAxes()
    {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    public int getProgressCircleDiameter()
    {
        int i;
        if(mCircleView != null)
            i = mCircleView.getMeasuredHeight();
        else
            i = 0;
        return i;
    }

    public boolean hasNestedScrollingParent()
    {
        return mNestedScrollingChildHelper.hasNestedScrollingParent();
    }

    public boolean isNestedScrollingEnabled()
    {
        return mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    public boolean isRefreshing()
    {
        return mRefreshing;
    }

    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        reset();
    }

    public boolean onInterceptTouchEvent(MotionEvent motionevent)
    {
        boolean flag;
        int i;
        boolean flag1;
        flag = false;
        ensureTarget();
        i = MotionEventCompat.getActionMasked(motionevent);
        if(mReturningToStart && i == 0)
            mReturningToStart = false;
        flag1 = flag;
        if(!isEnabled()) goto _L2; else goto _L1
_L1:
        flag1 = flag;
        if(mReturningToStart) goto _L2; else goto _L3
_L3:
        flag1 = flag;
        if(canChildScrollUp()) goto _L2; else goto _L4
_L4:
        flag1 = flag;
        if(mRefreshing) goto _L2; else goto _L5
_L5:
        if(!mNestedScrollInProgress) goto _L7; else goto _L6
_L6:
        flag1 = flag;
_L2:
        return flag1;
_L7:
        i;
        JVM INSTR tableswitch 0 6: default 124
    //                   0 133
    //                   1 303
    //                   2 195
    //                   3 303
    //                   4 124
    //                   5 124
    //                   6 295;
           goto _L8 _L9 _L10 _L11 _L10 _L8 _L8 _L12
_L8:
        break; /* Loop/switch isn't completed */
_L10:
        break MISSING_BLOCK_LABEL_303;
_L14:
        flag1 = mIsBeingDragged;
          goto _L2
_L9:
        float f;
        setTargetOffsetTopAndBottom(mOriginalOffsetTop - mCircleView.getTop(), true);
        mActivePointerId = MotionEventCompat.getPointerId(motionevent, 0);
        mIsBeingDragged = false;
        f = getMotionEventY(motionevent, mActivePointerId);
        flag1 = flag;
        if(f == -1F) goto _L2; else goto _L13
_L13:
        mInitialDownY = f;
          goto _L14
_L11:
label0:
        {
            if(mActivePointerId != -1)
                break label0;
            Log.e(LOG_TAG, "Got ACTION_MOVE event but don't have an active pointer id.");
            flag1 = flag;
        }
          goto _L2
        f = getMotionEventY(motionevent, mActivePointerId);
        flag1 = flag;
        if(f == -1F) goto _L2; else goto _L15
_L15:
        if(f - mInitialDownY > (float)mTouchSlop && !mIsBeingDragged)
        {
            mInitialMotionY = mInitialDownY + (float)mTouchSlop;
            mIsBeingDragged = true;
            mProgress.setAlpha(76);
        }
          goto _L14
_L12:
        onSecondaryPointerUp(motionevent);
          goto _L14
        mIsBeingDragged = false;
        mActivePointerId = -1;
          goto _L14
    }

    protected void onLayout(boolean flag, int i, int j, int k, int l)
    {
        i = getMeasuredWidth();
        l = getMeasuredHeight();
        if(getChildCount() != 0) goto _L2; else goto _L1
_L1:
        return;
_L2:
        if(mTarget == null)
            ensureTarget();
        if(mTarget != null)
        {
            View view = mTarget;
            k = getPaddingLeft();
            j = getPaddingTop();
            view.layout(k, j, k + (i - getPaddingLeft() - getPaddingRight()), j + (l - getPaddingTop() - getPaddingBottom()));
            k = mCircleView.getMeasuredWidth();
            j = mCircleView.getMeasuredHeight();
            mCircleView.layout(i / 2 - k / 2, mCurrentTargetOffsetTop, i / 2 + k / 2, mCurrentTargetOffsetTop + j);
        }
        if(true) goto _L1; else goto _L3
_L3:
    }

    public void onMeasure(int i, int j)
    {
        super.onMeasure(i, j);
        if(mTarget == null)
            ensureTarget();
        if(mTarget != null) goto _L2; else goto _L1
_L1:
        return;
_L2:
        mTarget.measure(android.view.View.MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), 0x40000000), android.view.View.MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), 0x40000000));
        mCircleView.measure(android.view.View.MeasureSpec.makeMeasureSpec(mCircleWidth, 0x40000000), android.view.View.MeasureSpec.makeMeasureSpec(mCircleHeight, 0x40000000));
        if(!mUsingCustomStart && !mOriginalOffsetCalculated)
        {
            mOriginalOffsetCalculated = true;
            i = -mCircleView.getMeasuredHeight();
            mOriginalOffsetTop = i;
            mCurrentTargetOffsetTop = i;
        }
        mCircleViewIndex = -1;
        i = 0;
        do
        {
            if(i < getChildCount())
            {
label0:
                {
                    if(getChildAt(i) != mCircleView)
                        break label0;
                    mCircleViewIndex = i;
                }
            }
            if(true)
                continue;
            i++;
        } while(true);
        if(true) goto _L1; else goto _L3
_L3:
    }

    public boolean onNestedFling(View view, float f, float f1, boolean flag)
    {
        return dispatchNestedFling(f, f1, flag);
    }

    public boolean onNestedPreFling(View view, float f, float f1)
    {
        return dispatchNestedPreFling(f, f1);
    }

    public void onNestedPreScroll(View view, int i, int j, int ai[])
    {
        if(j > 0 && mTotalUnconsumed > 0.0F)
        {
            if((float)j > mTotalUnconsumed)
            {
                ai[1] = j - (int)mTotalUnconsumed;
                mTotalUnconsumed = 0.0F;
            } else
            {
                mTotalUnconsumed = mTotalUnconsumed - (float)j;
                ai[1] = j;
            }
            moveSpinner(mTotalUnconsumed);
        }
        if(mUsingCustomStart && j > 0 && mTotalUnconsumed == 0.0F && Math.abs(j - ai[1]) > 0)
            mCircleView.setVisibility(8);
        view = mParentScrollConsumed;
        if(dispatchNestedPreScroll(i - ai[0], j - ai[1], view, null))
        {
            ai[0] = ai[0] + view[0];
            ai[1] = ai[1] + view[1];
        }
    }

    public void onNestedScroll(View view, int i, int j, int k, int l)
    {
        dispatchNestedScroll(i, j, k, l, mParentOffsetInWindow);
        i = l + mParentOffsetInWindow[1];
        if(i < 0)
        {
            mTotalUnconsumed = mTotalUnconsumed + (float)Math.abs(i);
            moveSpinner(mTotalUnconsumed);
        }
    }

    public void onNestedScrollAccepted(View view, View view1, int i)
    {
        mNestedScrollingParentHelper.onNestedScrollAccepted(view, view1, i);
        startNestedScroll(i & 2);
        mTotalUnconsumed = 0.0F;
        mNestedScrollInProgress = true;
    }

    public boolean onStartNestedScroll(View view, View view1, int i)
    {
        boolean flag;
        if(isEnabled() && canChildScrollUp() && !mReturningToStart && !mRefreshing && (i & 2) != 0)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public void onStopNestedScroll(View view)
    {
        mNestedScrollingParentHelper.onStopNestedScroll(view);
        mNestedScrollInProgress = false;
        if(mTotalUnconsumed > 0.0F)
        {
            finishSpinner(mTotalUnconsumed);
            mTotalUnconsumed = 0.0F;
        }
        stopNestedScroll();
    }

    public boolean onTouchEvent(MotionEvent motionevent)
    {
        boolean flag;
        int i;
        boolean flag1;
        flag = false;
        i = MotionEventCompat.getActionMasked(motionevent);
        if(mReturningToStart && i == 0)
            mReturningToStart = false;
        flag1 = flag;
        if(!isEnabled()) goto _L2; else goto _L1
_L1:
        flag1 = flag;
        if(mReturningToStart) goto _L2; else goto _L3
_L3:
        flag1 = flag;
        if(canChildScrollUp()) goto _L2; else goto _L4
_L4:
        if(!mNestedScrollInProgress) goto _L6; else goto _L5
_L5:
        flag1 = flag;
_L2:
        return flag1;
_L6:
        flag1 = flag;
        i;
        JVM INSTR tableswitch 0 6: default 112
    //                   0 118
    //                   1 250
    //                   2 135
    //                   3 63
    //                   4 112
    //                   5 205
    //                   6 242;
           goto _L7 _L8 _L9 _L10 _L11 _L7 _L12 _L13
_L7:
        break; /* Loop/switch isn't completed */
_L11:
        continue; /* Loop/switch isn't completed */
_L14:
        flag1 = true;
        continue; /* Loop/switch isn't completed */
_L8:
        mActivePointerId = MotionEventCompat.getPointerId(motionevent, 0);
        mIsBeingDragged = false;
        continue; /* Loop/switch isn't completed */
_L10:
        int j = MotionEventCompat.findPointerIndex(motionevent, mActivePointerId);
        if(j < 0)
        {
            Log.e(LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
            flag1 = flag;
            continue; /* Loop/switch isn't completed */
        }
        float f = (MotionEventCompat.getY(motionevent, j) - mInitialMotionY) * 0.5F;
        if(mIsBeingDragged)
        {
            flag1 = flag;
            if(f <= 0.0F)
                continue; /* Loop/switch isn't completed */
            moveSpinner(f);
        }
        continue; /* Loop/switch isn't completed */
_L12:
        int k = MotionEventCompat.getActionIndex(motionevent);
        if(k < 0)
        {
            Log.e(LOG_TAG, "Got ACTION_POINTER_DOWN event but have an invalid action index.");
            flag1 = flag;
            continue; /* Loop/switch isn't completed */
        }
        mActivePointerId = MotionEventCompat.getPointerId(motionevent, k);
        continue; /* Loop/switch isn't completed */
_L13:
        onSecondaryPointerUp(motionevent);
        if(true) goto _L14; else goto _L9
_L9:
        int l = MotionEventCompat.findPointerIndex(motionevent, mActivePointerId);
        if(l < 0)
        {
            Log.e(LOG_TAG, "Got ACTION_UP event but don't have an active pointer id.");
            flag1 = flag;
        } else
        {
            float f2 = MotionEventCompat.getY(motionevent, l);
            float f1 = mInitialMotionY;
            mIsBeingDragged = false;
            finishSpinner((f2 - f1) * 0.5F);
            mActivePointerId = -1;
            flag1 = flag;
        }
        if(true) goto _L2; else goto _L15
_L15:
    }

    public void requestDisallowInterceptTouchEvent(boolean flag)
    {
        if((android.os.Build.VERSION.SDK_INT >= 21 || !(mTarget instanceof AbsListView)) && (mTarget == null || ViewCompat.isNestedScrollingEnabled(mTarget)))
            super.requestDisallowInterceptTouchEvent(flag);
    }

    public transient void setColorScheme(int ai[])
    {
        setColorSchemeResources(ai);
    }

    public transient void setColorSchemeColors(int ai[])
    {
        ensureTarget();
        mProgress.setColorSchemeColors(ai);
    }

    public transient void setColorSchemeResources(int ai[])
    {
        Resources resources = getResources();
        int ai1[] = new int[ai.length];
        for(int i = 0; i < ai.length; i++)
            ai1[i] = resources.getColor(ai[i]);

        setColorSchemeColors(ai1);
    }

    public void setDistanceToTriggerSync(int i)
    {
        mTotalDragDistance = i;
    }

    public void setNestedScrollingEnabled(boolean flag)
    {
        mNestedScrollingChildHelper.setNestedScrollingEnabled(flag);
    }

    public void setOnRefreshListener(OnRefreshListener onrefreshlistener)
    {
        mListener = onrefreshlistener;
    }

    public void setProgressBackgroundColor(int i)
    {
        setProgressBackgroundColorSchemeResource(i);
    }

    public void setProgressBackgroundColorSchemeColor(int i)
    {
        mCircleView.setBackgroundColor(i);
        mProgress.setBackgroundColor(i);
    }

    public void setProgressBackgroundColorSchemeResource(int i)
    {
        setProgressBackgroundColorSchemeColor(getResources().getColor(i));
    }

    public void setProgressViewEndTarget(boolean flag, int i)
    {
        mSpinnerFinalOffset = i;
        mScale = flag;
        mCircleView.invalidate();
    }

    public void setProgressViewOffset(boolean flag, int i, int j)
    {
        mScale = flag;
        mCircleView.setVisibility(8);
        mCurrentTargetOffsetTop = i;
        mOriginalOffsetTop = i;
        mSpinnerFinalOffset = j;
        mUsingCustomStart = true;
        mCircleView.invalidate();
    }

    public void setRefreshing(boolean flag)
    {
        if(flag && mRefreshing != flag)
        {
            mRefreshing = flag;
            int i;
            if(!mUsingCustomStart)
                i = (int)(mSpinnerFinalOffset + (float)mOriginalOffsetTop);
            else
                i = (int)mSpinnerFinalOffset;
            setTargetOffsetTopAndBottom(i - mCurrentTargetOffsetTop, true);
            mNotify = false;
            startScaleUpAnimation(mRefreshListener);
        } else
        {
            setRefreshing(flag, false);
        }
    }

    public void setSize(int i)
    {
        if(i == 0 || i == 1)
        {
            DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
            if(i == 0)
            {
                int j = (int)(56F * displaymetrics.density);
                mCircleWidth = j;
                mCircleHeight = j;
            } else
            {
                int k = (int)(40F * displaymetrics.density);
                mCircleWidth = k;
                mCircleHeight = k;
            }
            mCircleView.setImageDrawable(null);
            mProgress.updateSizes(i);
            mCircleView.setImageDrawable(mProgress);
        }
    }

    public boolean startNestedScroll(int i)
    {
        return mNestedScrollingChildHelper.startNestedScroll(i);
    }

    public void stopNestedScroll()
    {
        mNestedScrollingChildHelper.stopNestedScroll();
    }

    private static final int ALPHA_ANIMATION_DURATION = 300;
    private static final int ANIMATE_TO_START_DURATION = 200;
    private static final int ANIMATE_TO_TRIGGER_DURATION = 200;
    private static final int CIRCLE_BG_LIGHT = 0xfffafafa;
    private static final int CIRCLE_DIAMETER = 40;
    private static final int CIRCLE_DIAMETER_LARGE = 56;
    private static final float DECELERATE_INTERPOLATION_FACTOR = 2F;
    public static final int DEFAULT = 1;
    private static final int DEFAULT_CIRCLE_TARGET = 64;
    private static final float DRAG_RATE = 0.5F;
    private static final int INVALID_POINTER = -1;
    public static final int LARGE = 0;
    private static final int LAYOUT_ATTRS[] = {
        0x101000e
    };
    private static final String LOG_TAG = android/support/v4/widget/SwipeRefreshLayout.getSimpleName();
    private static final int MAX_ALPHA = 255;
    private static final float MAX_PROGRESS_ANGLE = 0.8F;
    private static final int SCALE_DOWN_DURATION = 150;
    private static final int STARTING_PROGRESS_ALPHA = 76;
    private int mActivePointerId;
    private Animation mAlphaMaxAnimation;
    private Animation mAlphaStartAnimation;
    private final Animation mAnimateToCorrectPosition;
    private final Animation mAnimateToStartPosition;
    private int mCircleHeight;
    private CircleImageView mCircleView;
    private int mCircleViewIndex;
    private int mCircleWidth;
    private int mCurrentTargetOffsetTop;
    private final DecelerateInterpolator mDecelerateInterpolator;
    protected int mFrom;
    private float mInitialDownY;
    private float mInitialMotionY;
    private boolean mIsBeingDragged;
    private OnRefreshListener mListener;
    private int mMediumAnimationDuration;
    private boolean mNestedScrollInProgress;
    private final NestedScrollingChildHelper mNestedScrollingChildHelper;
    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
    private boolean mNotify;
    private boolean mOriginalOffsetCalculated;
    protected int mOriginalOffsetTop;
    private final int mParentOffsetInWindow[];
    private final int mParentScrollConsumed[];
    private MaterialProgressDrawable mProgress;
    private android.view.animation.Animation.AnimationListener mRefreshListener = new android.view.animation.Animation.AnimationListener() {

        public void onAnimationEnd(Animation animation)
        {
            if(mRefreshing)
            {
                mProgress.setAlpha(255);
                mProgress.start();
                if(mNotify && mListener != null)
                    mListener.onRefresh();
                mCurrentTargetOffsetTop = mCircleView.getTop();
            } else
            {
                reset();
            }
        }

        public void onAnimationRepeat(Animation animation)
        {
        }

        public void onAnimationStart(Animation animation)
        {
        }

        final SwipeRefreshLayout this$0;

            
            {
                this$0 = SwipeRefreshLayout.this;
                super();
            }
    }
;
    private boolean mRefreshing;
    private boolean mReturningToStart;
    private boolean mScale;
    private Animation mScaleAnimation;
    private Animation mScaleDownAnimation;
    private Animation mScaleDownToStartAnimation;
    private float mSpinnerFinalOffset;
    private float mStartingScale;
    private View mTarget;
    private float mTotalDragDistance;
    private float mTotalUnconsumed;
    private int mTouchSlop;
    private boolean mUsingCustomStart;












/*
    static int access$402(SwipeRefreshLayout swiperefreshlayout, int i)
    {
        swiperefreshlayout.mCurrentTargetOffsetTop = i;
        return i;
    }

*/





}
