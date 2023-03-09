// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.*;
import android.support.v4.view.*;
import android.support.v4.view.accessibility.*;
import android.util.*;
import android.view.*;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import java.util.List;

// Referenced classes of package android.support.v4.widget:
//            EdgeEffectCompat, ScrollerCompat

public class NestedScrollView extends FrameLayout
    implements NestedScrollingParent, NestedScrollingChild, ScrollingView
{
    static class AccessibilityDelegate extends AccessibilityDelegateCompat
    {

        public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityevent)
        {
            super.onInitializeAccessibilityEvent(view, accessibilityevent);
            view = (NestedScrollView)view;
            accessibilityevent.setClassName(android/widget/ScrollView.getName());
            accessibilityevent = AccessibilityEventCompat.asRecord(accessibilityevent);
            boolean flag;
            if(view.getScrollRange() > 0)
                flag = true;
            else
                flag = false;
            accessibilityevent.setScrollable(flag);
            accessibilityevent.setScrollX(view.getScrollX());
            accessibilityevent.setScrollY(view.getScrollY());
            accessibilityevent.setMaxScrollX(view.getScrollX());
            accessibilityevent.setMaxScrollY(view.getScrollRange());
        }

        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilitynodeinfocompat)
        {
            super.onInitializeAccessibilityNodeInfo(view, accessibilitynodeinfocompat);
            view = (NestedScrollView)view;
            accessibilitynodeinfocompat.setClassName(android/widget/ScrollView.getName());
            if(view.isEnabled())
            {
                int i = view.getScrollRange();
                if(i > 0)
                {
                    accessibilitynodeinfocompat.setScrollable(true);
                    if(view.getScrollY() > 0)
                        accessibilitynodeinfocompat.addAction(8192);
                    if(view.getScrollY() < i)
                        accessibilitynodeinfocompat.addAction(4096);
                }
            }
        }

        public boolean performAccessibilityAction(View view, int i, Bundle bundle)
        {
            boolean flag = true;
            if(!super.performAccessibilityAction(view, i, bundle)) goto _L2; else goto _L1
_L1:
            return flag;
_L2:
            view = (NestedScrollView)view;
            if(view.isEnabled())
                break; /* Loop/switch isn't completed */
            flag = false;
            if(true) goto _L1; else goto _L3
_L3:
            switch(i)
            {
            default:
                flag = false;
                break;

            case 4096: 
                int j = view.getHeight();
                int l = view.getPaddingBottom();
                i = view.getPaddingTop();
                i = Math.min(view.getScrollY() + (j - l - i), view.getScrollRange());
                if(i != view.getScrollY())
                    view.smoothScrollTo(0, i);
                else
                    flag = false;
                break;

            case 8192: 
                int k = view.getHeight();
                int i1 = view.getPaddingBottom();
                i = view.getPaddingTop();
                i = Math.max(view.getScrollY() - (k - i1 - i), 0);
                if(i != view.getScrollY())
                    view.smoothScrollTo(0, i);
                else
                    flag = false;
                break;
            }
            if(true) goto _L1; else goto _L4
_L4:
        }

        AccessibilityDelegate()
        {
        }
    }

    public static interface OnScrollChangeListener
    {

        public abstract void onScrollChange(NestedScrollView nestedscrollview, int i, int j, int k, int l);
    }

    static class SavedState extends android.view.View.BaseSavedState
    {

        public String toString()
        {
            return (new StringBuilder()).append("HorizontalScrollView.SavedState{").append(Integer.toHexString(System.identityHashCode(this))).append(" scrollPosition=").append(scrollPosition).append("}").toString();
        }

        public void writeToParcel(Parcel parcel, int i)
        {
            super.writeToParcel(parcel, i);
            parcel.writeInt(scrollPosition);
        }

        public static final android.os.Parcelable.Creator CREATOR = new android.os.Parcelable.Creator() {

            public SavedState createFromParcel(Parcel parcel)
            {
                return new SavedState(parcel);
            }

            public volatile Object createFromParcel(Parcel parcel)
            {
                return createFromParcel(parcel);
            }

            public SavedState[] newArray(int i)
            {
                return new SavedState[i];
            }

            public volatile Object[] newArray(int i)
            {
                return newArray(i);
            }

        }
;
        public int scrollPosition;


        public SavedState(Parcel parcel)
        {
            super(parcel);
            scrollPosition = parcel.readInt();
        }

        SavedState(Parcelable parcelable)
        {
            super(parcelable);
        }
    }


    public NestedScrollView(Context context)
    {
        this(context, null);
    }

    public NestedScrollView(Context context, AttributeSet attributeset)
    {
        this(context, attributeset, 0);
    }

    public NestedScrollView(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        mTempRect = new Rect();
        mIsLayoutDirty = true;
        mIsLaidOut = false;
        mChildToScrollTo = null;
        mIsBeingDragged = false;
        mSmoothScrollingEnabled = true;
        mActivePointerId = -1;
        mScrollOffset = new int[2];
        mScrollConsumed = new int[2];
        initScrollView();
        context = context.obtainStyledAttributes(attributeset, SCROLLVIEW_STYLEABLE, i, 0);
        setFillViewport(context.getBoolean(0, false));
        context.recycle();
        mParentHelper = new NestedScrollingParentHelper(this);
        mChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
        ViewCompat.setAccessibilityDelegate(this, ACCESSIBILITY_DELEGATE);
    }

    private boolean canScroll()
    {
        boolean flag = false;
        View view = getChildAt(0);
        boolean flag1 = flag;
        if(view != null)
        {
            int i = view.getHeight();
            flag1 = flag;
            if(getHeight() < getPaddingTop() + i + getPaddingBottom())
                flag1 = true;
        }
        return flag1;
    }

    private static int clamp(int i, int j, int k)
    {
        if(j < k && i >= 0) goto _L2; else goto _L1
_L1:
        int l = 0;
_L4:
        return l;
_L2:
        l = i;
        if(j + i > k)
            l = k - j;
        if(true) goto _L4; else goto _L3
_L3:
    }

    private void doScrollY(int i)
    {
        if(i != 0)
            if(mSmoothScrollingEnabled)
                smoothScrollBy(0, i);
            else
                scrollBy(0, i);
    }

    private void endDrag()
    {
        mIsBeingDragged = false;
        recycleVelocityTracker();
        stopNestedScroll();
        if(mEdgeGlowTop != null)
        {
            mEdgeGlowTop.onRelease();
            mEdgeGlowBottom.onRelease();
        }
    }

    private void ensureGlows()
    {
        if(ViewCompat.getOverScrollMode(this) != 2)
        {
            if(mEdgeGlowTop == null)
            {
                Context context = getContext();
                mEdgeGlowTop = new EdgeEffectCompat(context);
                mEdgeGlowBottom = new EdgeEffectCompat(context);
            }
        } else
        {
            mEdgeGlowTop = null;
            mEdgeGlowBottom = null;
        }
    }

    private View findFocusableViewInBounds(boolean flag, int i, int j)
    {
        java.util.ArrayList arraylist = getFocusables(2);
        View view = null;
        boolean flag1 = false;
        int k = arraylist.size();
        int l = 0;
        while(l < k) 
        {
            View view1 = (View)arraylist.get(l);
            int i1 = view1.getTop();
            int j1 = view1.getBottom();
            View view2 = view;
            boolean flag3 = flag1;
            if(i < j1)
            {
                view2 = view;
                flag3 = flag1;
                if(i1 < j)
                {
                    boolean flag4;
                    if(i < i1 && j1 < j)
                        flag4 = true;
                    else
                        flag4 = false;
                    if(view == null)
                    {
                        view2 = view1;
                        flag3 = flag4;
                    } else
                    {
                        boolean flag2;
                        if(flag && i1 < view.getTop() || !flag && j1 > view.getBottom())
                            flag2 = true;
                        else
                            flag2 = false;
                        if(flag1)
                        {
                            view2 = view;
                            flag3 = flag1;
                            if(flag4)
                            {
                                view2 = view;
                                flag3 = flag1;
                                if(flag2)
                                {
                                    view2 = view1;
                                    flag3 = flag1;
                                }
                            }
                        } else
                        if(flag4)
                        {
                            view2 = view1;
                            flag3 = true;
                        } else
                        {
                            view2 = view;
                            flag3 = flag1;
                            if(flag2)
                            {
                                view2 = view1;
                                flag3 = flag1;
                            }
                        }
                    }
                }
            }
            l++;
            view = view2;
            flag1 = flag3;
        }
        return view;
    }

    private void flingWithNestedDispatch(int i)
    {
        int j = getScrollY();
        boolean flag;
        if((j > 0 || i > 0) && (j < getScrollRange() || i < 0))
            flag = true;
        else
            flag = false;
        if(!dispatchNestedPreFling(0.0F, i))
        {
            dispatchNestedFling(0.0F, i, flag);
            if(flag)
                fling(i);
        }
    }

    private int getScrollRange()
    {
        int i = 0;
        if(getChildCount() > 0)
            i = Math.max(0, getChildAt(0).getHeight() - (getHeight() - getPaddingBottom() - getPaddingTop()));
        return i;
    }

    private float getVerticalScrollFactorCompat()
    {
        if(mVerticalScrollFactor == 0.0F)
        {
            TypedValue typedvalue = new TypedValue();
            Context context = getContext();
            if(!context.getTheme().resolveAttribute(0x101004d, typedvalue, true))
                throw new IllegalStateException("Expected theme to define listPreferredItemHeight.");
            mVerticalScrollFactor = typedvalue.getDimension(context.getResources().getDisplayMetrics());
        }
        return mVerticalScrollFactor;
    }

    private boolean inChild(int i, int j)
    {
        boolean flag = false;
        boolean flag1 = flag;
        if(getChildCount() > 0)
        {
            int k = getScrollY();
            View view = getChildAt(0);
            flag1 = flag;
            if(j >= view.getTop() - k)
            {
                flag1 = flag;
                if(j < view.getBottom() - k)
                {
                    flag1 = flag;
                    if(i >= view.getLeft())
                    {
                        flag1 = flag;
                        if(i < view.getRight())
                            flag1 = true;
                    }
                }
            }
        }
        return flag1;
    }

    private void initOrResetVelocityTracker()
    {
        if(mVelocityTracker == null)
            mVelocityTracker = VelocityTracker.obtain();
        else
            mVelocityTracker.clear();
    }

    private void initScrollView()
    {
        mScroller = ScrollerCompat.create(getContext(), null);
        setFocusable(true);
        setDescendantFocusability(0x40000);
        setWillNotDraw(false);
        ViewConfiguration viewconfiguration = ViewConfiguration.get(getContext());
        mTouchSlop = viewconfiguration.getScaledTouchSlop();
        mMinimumVelocity = viewconfiguration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = viewconfiguration.getScaledMaximumFlingVelocity();
    }

    private void initVelocityTrackerIfNotExists()
    {
        if(mVelocityTracker == null)
            mVelocityTracker = VelocityTracker.obtain();
    }

    private boolean isOffScreen(View view)
    {
        boolean flag = false;
        if(!isWithinDeltaOfScreen(view, 0, getHeight()))
            flag = true;
        return flag;
    }

    private static boolean isViewDescendantOf(View view, View view1)
    {
        boolean flag = true;
        if(view != view1) goto _L2; else goto _L1
_L1:
        return flag;
_L2:
        view = view.getParent();
        if(!(view instanceof ViewGroup) || !isViewDescendantOf((View)view, view1))
            flag = false;
        if(true) goto _L1; else goto _L3
_L3:
    }

    private boolean isWithinDeltaOfScreen(View view, int i, int j)
    {
        view.getDrawingRect(mTempRect);
        offsetDescendantRectToMyCoords(view, mTempRect);
        boolean flag;
        if(mTempRect.bottom + i >= getScrollY() && mTempRect.top - i <= getScrollY() + j)
            flag = true;
        else
            flag = false;
        return flag;
    }

    private void onSecondaryPointerUp(MotionEvent motionevent)
    {
        int i = (motionevent.getAction() & 0xff00) >> 8;
        if(MotionEventCompat.getPointerId(motionevent, i) == mActivePointerId)
        {
            if(i == 0)
                i = 1;
            else
                i = 0;
            mLastMotionY = (int)MotionEventCompat.getY(motionevent, i);
            mActivePointerId = MotionEventCompat.getPointerId(motionevent, i);
            if(mVelocityTracker != null)
                mVelocityTracker.clear();
        }
    }

    private void recycleVelocityTracker()
    {
        if(mVelocityTracker != null)
        {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private boolean scrollAndFocus(int i, int j, int k)
    {
        boolean flag = true;
        int l = getHeight();
        int i1 = getScrollY();
        l = i1 + l;
        boolean flag1;
        View view;
        Object obj;
        if(i == 33)
            flag1 = true;
        else
            flag1 = false;
        view = findFocusableViewInBounds(flag1, j, k);
        obj = view;
        if(view == null)
            obj = this;
        if(j >= i1 && k <= l)
        {
            flag1 = false;
        } else
        {
            if(flag1)
                j -= i1;
            else
                j = k - l;
            doScrollY(j);
            flag1 = flag;
        }
        if(obj != findFocus())
            ((View) (obj)).requestFocus(i);
        return flag1;
    }

    private void scrollToChild(View view)
    {
        view.getDrawingRect(mTempRect);
        offsetDescendantRectToMyCoords(view, mTempRect);
        int i = computeScrollDeltaToGetChildRectOnScreen(mTempRect);
        if(i != 0)
            scrollBy(0, i);
    }

    private boolean scrollToChildRect(Rect rect, boolean flag)
    {
        int i = computeScrollDeltaToGetChildRectOnScreen(rect);
        boolean flag1;
        if(i != 0)
            flag1 = true;
        else
            flag1 = false;
        if(flag1)
            if(flag)
                scrollBy(0, i);
            else
                smoothScrollBy(0, i);
        return flag1;
    }

    public void addView(View view)
    {
        if(getChildCount() > 0)
        {
            throw new IllegalStateException("ScrollView can host only one direct child");
        } else
        {
            super.addView(view);
            return;
        }
    }

    public void addView(View view, int i)
    {
        if(getChildCount() > 0)
        {
            throw new IllegalStateException("ScrollView can host only one direct child");
        } else
        {
            super.addView(view, i);
            return;
        }
    }

    public void addView(View view, int i, android.view.ViewGroup.LayoutParams layoutparams)
    {
        if(getChildCount() > 0)
        {
            throw new IllegalStateException("ScrollView can host only one direct child");
        } else
        {
            super.addView(view, i, layoutparams);
            return;
        }
    }

    public void addView(View view, android.view.ViewGroup.LayoutParams layoutparams)
    {
        if(getChildCount() > 0)
        {
            throw new IllegalStateException("ScrollView can host only one direct child");
        } else
        {
            super.addView(view, layoutparams);
            return;
        }
    }

    public boolean arrowScroll(int i)
    {
        boolean flag;
        View view;
        View view1;
        int j;
        flag = false;
        view = findFocus();
        view1 = view;
        if(view == this)
            view1 = null;
        view = FocusFinder.getInstance().findNextFocus(this, view1, i);
        j = getMaxScrollAmount();
        if(view == null || !isWithinDeltaOfScreen(view, j, getHeight())) goto _L2; else goto _L1
_L1:
        view.getDrawingRect(mTempRect);
        offsetDescendantRectToMyCoords(view, mTempRect);
        doScrollY(computeScrollDeltaToGetChildRectOnScreen(mTempRect));
        view.requestFocus(i);
_L6:
        if(view1 != null && view1.isFocused() && isOffScreen(view1))
        {
            i = getDescendantFocusability();
            setDescendantFocusability(0x20000);
            requestFocus();
            setDescendantFocusability(i);
        }
        flag = true;
_L4:
        return flag;
_L2:
        int l;
        int k = j;
        if(i == 33 && getScrollY() < k)
        {
            l = getScrollY();
        } else
        {
            l = k;
            if(i == 130)
            {
                l = k;
                if(getChildCount() > 0)
                {
                    int i1 = getChildAt(0).getBottom();
                    int j1 = (getScrollY() + getHeight()) - getPaddingBottom();
                    l = k;
                    if(i1 - j1 < j)
                        l = i1 - j1;
                }
            }
        }
        if(l == 0) goto _L4; else goto _L3
_L3:
        if(i == 130)
            i = l;
        else
            i = -l;
        doScrollY(i);
        if(true) goto _L6; else goto _L5
_L5:
    }

    public int computeHorizontalScrollExtent()
    {
        return super.computeHorizontalScrollExtent();
    }

    public int computeHorizontalScrollOffset()
    {
        return super.computeHorizontalScrollOffset();
    }

    public int computeHorizontalScrollRange()
    {
        return super.computeHorizontalScrollRange();
    }

    public void computeScroll()
    {
        boolean flag = true;
        if(!mScroller.computeScrollOffset()) goto _L2; else goto _L1
_L1:
        int i;
        int j;
        int k;
        int l;
        i = getScrollX();
        j = getScrollY();
        k = mScroller.getCurrX();
        l = mScroller.getCurrY();
        if(i == k && j == l) goto _L2; else goto _L3
_L3:
        int i1;
        i1 = getScrollRange();
        int j1 = ViewCompat.getOverScrollMode(this);
        boolean flag1 = flag;
        if(j1 != 0)
            if(j1 == 1 && i1 > 0)
                flag1 = flag;
            else
                flag1 = false;
        overScrollByCompat(k - i, l - j, i, j, 0, i1, 0, 0, false);
        if(!flag1) goto _L2; else goto _L4
_L4:
        ensureGlows();
        if(l > 0 || j <= 0) goto _L6; else goto _L5
_L5:
        mEdgeGlowTop.onAbsorb((int)mScroller.getCurrVelocity());
_L2:
        return;
_L6:
        if(l >= i1 && j < i1)
            mEdgeGlowBottom.onAbsorb((int)mScroller.getCurrVelocity());
        if(true) goto _L2; else goto _L7
_L7:
    }

    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect)
    {
        if(getChildCount() != 0) goto _L2; else goto _L1
_L1:
        int i = 0;
_L4:
        return i;
_L2:
        int j = getHeight();
        int k = getScrollY();
        i = k + j;
        int l = getVerticalFadingEdgeLength();
        int i1 = k;
        if(rect.top > 0)
            i1 = k + l;
        k = i;
        if(rect.bottom < getChildAt(0).getHeight())
            k = i - l;
        l = 0;
        if(rect.bottom > k && rect.top > i1)
        {
            if(rect.height() > j)
                i = 0 + (rect.top - i1);
            else
                i = 0 + (rect.bottom - k);
            i = Math.min(i, getChildAt(0).getBottom() - k);
        } else
        {
            i = l;
            if(rect.top < i1)
            {
                i = l;
                if(rect.bottom < k)
                {
                    if(rect.height() > j)
                        i = 0 - (k - rect.bottom);
                    else
                        i = 0 - (i1 - rect.top);
                    i = Math.max(i, -getScrollY());
                }
            }
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public int computeVerticalScrollExtent()
    {
        return super.computeVerticalScrollExtent();
    }

    public int computeVerticalScrollOffset()
    {
        return Math.max(0, super.computeVerticalScrollOffset());
    }

    public int computeVerticalScrollRange()
    {
        int i;
        int k;
        i = getChildCount();
        k = getHeight() - getPaddingBottom() - getPaddingTop();
        if(i != 0) goto _L2; else goto _L1
_L1:
        return k;
_L2:
        int j = getChildAt(0).getBottom();
        int l = getScrollY();
        int i1 = Math.max(0, j - k);
        if(l < 0)
        {
            k = j - l;
        } else
        {
            k = j;
            if(l > i1)
                k = j + (l - i1);
        }
        if(true) goto _L1; else goto _L3
_L3:
    }

    public boolean dispatchKeyEvent(KeyEvent keyevent)
    {
        boolean flag;
        if(super.dispatchKeyEvent(keyevent) || executeKeyEvent(keyevent))
            flag = true;
        else
            flag = false;
        return flag;
    }

    public boolean dispatchNestedFling(float f, float f1, boolean flag)
    {
        return mChildHelper.dispatchNestedFling(f, f1, flag);
    }

    public boolean dispatchNestedPreFling(float f, float f1)
    {
        return mChildHelper.dispatchNestedPreFling(f, f1);
    }

    public boolean dispatchNestedPreScroll(int i, int j, int ai[], int ai1[])
    {
        return mChildHelper.dispatchNestedPreScroll(i, j, ai, ai1);
    }

    public boolean dispatchNestedScroll(int i, int j, int k, int l, int ai[])
    {
        return mChildHelper.dispatchNestedScroll(i, j, k, l, ai);
    }

    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        if(mEdgeGlowTop != null)
        {
            int i = getScrollY();
            if(!mEdgeGlowTop.isFinished())
            {
                int j = canvas.save();
                int l = getWidth();
                int j1 = getPaddingLeft();
                int l1 = getPaddingRight();
                canvas.translate(getPaddingLeft(), Math.min(0, i));
                mEdgeGlowTop.setSize(l - j1 - l1, getHeight());
                if(mEdgeGlowTop.draw(canvas))
                    ViewCompat.postInvalidateOnAnimation(this);
                canvas.restoreToCount(j);
            }
            if(!mEdgeGlowBottom.isFinished())
            {
                int i1 = canvas.save();
                int k = getWidth() - getPaddingLeft() - getPaddingRight();
                int k1 = getHeight();
                canvas.translate(-k + getPaddingLeft(), Math.max(getScrollRange(), i) + k1);
                canvas.rotate(180F, k, 0.0F);
                mEdgeGlowBottom.setSize(k, k1);
                if(mEdgeGlowBottom.draw(canvas))
                    ViewCompat.postInvalidateOnAnimation(this);
                canvas.restoreToCount(i1);
            }
        }
    }

    public boolean executeKeyEvent(KeyEvent keyevent)
    {
        boolean flag;
        flag = false;
        mTempRect.setEmpty();
        if(canScroll()) goto _L2; else goto _L1
_L1:
        boolean flag1;
        flag1 = flag;
        if(isFocused())
        {
            flag1 = flag;
            if(keyevent.getKeyCode() != 4)
            {
                View view = findFocus();
                keyevent = view;
                if(view == this)
                    keyevent = null;
                keyevent = FocusFinder.getInstance().findNextFocus(this, keyevent, 130);
                flag1 = flag;
                if(keyevent != null)
                {
                    flag1 = flag;
                    if(keyevent != this)
                    {
                        flag1 = flag;
                        if(keyevent.requestFocus(130))
                            flag1 = true;
                    }
                }
            }
        }
_L4:
        return flag1;
_L2:
        flag = false;
        flag1 = flag;
        if(keyevent.getAction() != 0) goto _L4; else goto _L3
_L3:
        switch(keyevent.getKeyCode())
        {
        default:
            flag1 = flag;
            break;

        case 19: // '\023'
            if(!keyevent.isAltPressed())
                flag1 = arrowScroll(33);
            else
                flag1 = fullScroll(33);
            break;

        case 20: // '\024'
            if(!keyevent.isAltPressed())
                flag1 = arrowScroll(130);
            else
                flag1 = fullScroll(130);
            break;

        case 62: // '>'
            char c;
            if(keyevent.isShiftPressed())
                c = '!';
            else
                c = '\202';
            pageScroll(c);
            flag1 = flag;
            break;
        }
        if(true) goto _L4; else goto _L5
_L5:
    }

    public void fling(int i)
    {
        if(getChildCount() > 0)
        {
            int j = getHeight() - getPaddingBottom() - getPaddingTop();
            int k = getChildAt(0).getHeight();
            mScroller.fling(getScrollX(), getScrollY(), 0, i, 0, 0, 0, Math.max(0, k - j), 0, j / 2);
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public boolean fullScroll(int i)
    {
        int j;
        int k;
        if(i == 130)
            j = 1;
        else
            j = 0;
        k = getHeight();
        mTempRect.top = 0;
        mTempRect.bottom = k;
        if(j != 0)
        {
            j = getChildCount();
            if(j > 0)
            {
                View view = getChildAt(j - 1);
                mTempRect.bottom = view.getBottom() + getPaddingBottom();
                mTempRect.top = mTempRect.bottom - k;
            }
        }
        return scrollAndFocus(i, mTempRect.top, mTempRect.bottom);
    }

    protected float getBottomFadingEdgeStrength()
    {
        float f;
        if(getChildCount() == 0)
        {
            f = 0.0F;
        } else
        {
            int i = getVerticalFadingEdgeLength();
            int j = getHeight();
            int k = getPaddingBottom();
            k = getChildAt(0).getBottom() - getScrollY() - (j - k);
            if(k < i)
                f = (float)k / (float)i;
            else
                f = 1.0F;
        }
        return f;
    }

    public int getMaxScrollAmount()
    {
        return (int)(0.5F * (float)getHeight());
    }

    public int getNestedScrollAxes()
    {
        return mParentHelper.getNestedScrollAxes();
    }

    protected float getTopFadingEdgeStrength()
    {
        float f;
        if(getChildCount() == 0)
        {
            f = 0.0F;
        } else
        {
            int i = getVerticalFadingEdgeLength();
            int j = getScrollY();
            if(j < i)
                f = (float)j / (float)i;
            else
                f = 1.0F;
        }
        return f;
    }

    public boolean hasNestedScrollingParent()
    {
        return mChildHelper.hasNestedScrollingParent();
    }

    public boolean isFillViewport()
    {
        return mFillViewport;
    }

    public boolean isNestedScrollingEnabled()
    {
        return mChildHelper.isNestedScrollingEnabled();
    }

    public boolean isSmoothScrollingEnabled()
    {
        return mSmoothScrollingEnabled;
    }

    protected void measureChild(View view, int i, int j)
    {
        android.view.ViewGroup.LayoutParams layoutparams = view.getLayoutParams();
        view.measure(getChildMeasureSpec(i, getPaddingLeft() + getPaddingRight(), layoutparams.width), android.view.View.MeasureSpec.makeMeasureSpec(0, 0));
    }

    protected void measureChildWithMargins(View view, int i, int j, int k, int l)
    {
        android.view.ViewGroup.MarginLayoutParams marginlayoutparams = (android.view.ViewGroup.MarginLayoutParams)view.getLayoutParams();
        view.measure(getChildMeasureSpec(i, getPaddingLeft() + getPaddingRight() + marginlayoutparams.leftMargin + marginlayoutparams.rightMargin + j, marginlayoutparams.width), android.view.View.MeasureSpec.makeMeasureSpec(marginlayoutparams.topMargin + marginlayoutparams.bottomMargin, 0));
    }

    public void onAttachedToWindow()
    {
        mIsLaidOut = false;
    }

    public boolean onGenericMotionEvent(MotionEvent motionevent)
    {
        if((MotionEventCompat.getSource(motionevent) & 2) == 0) goto _L2; else goto _L1
_L1:
        motionevent.getAction();
        JVM INSTR tableswitch 8 8: default 32
    //                   8 36;
           goto _L2 _L3
_L2:
        boolean flag = false;
_L6:
        return flag;
_L3:
        float f;
        if(mIsBeingDragged || (f = MotionEventCompat.getAxisValue(motionevent, 9)) == 0.0F) goto _L2; else goto _L4
_L4:
        int i = (int)(getVerticalScrollFactorCompat() * f);
        int j = getScrollRange();
        int k = getScrollY();
        int l = k - i;
        if(l < 0)
        {
            i = 0;
        } else
        {
            i = l;
            if(l > j)
                i = j;
        }
        if(i == k) goto _L2; else goto _L5
_L5:
        super.scrollTo(getScrollX(), i);
        flag = true;
          goto _L6
    }

    public boolean onInterceptTouchEvent(MotionEvent motionevent)
    {
        boolean flag;
        boolean flag1;
        int i;
        flag = true;
        flag1 = false;
        i = motionevent.getAction();
        if(i != 2 || !mIsBeingDragged) goto _L2; else goto _L1
_L1:
        flag1 = flag;
_L9:
        return flag1;
_L2:
        i & 0xff;
        JVM INSTR tableswitch 0 6: default 76
    //                   0 228
    //                   1 322
    //                   2 84
    //                   3 322
    //                   4 76
    //                   5 76
    //                   6 372;
           goto _L3 _L4 _L5 _L6 _L5 _L3 _L3 _L7
_L7:
        break MISSING_BLOCK_LABEL_372;
_L3:
        break; /* Loop/switch isn't completed */
_L6:
        break; /* Loop/switch isn't completed */
_L10:
        flag1 = mIsBeingDragged;
        if(true) goto _L9; else goto _L8
_L8:
        int j = mActivePointerId;
        if(j != -1)
        {
            int i1 = MotionEventCompat.findPointerIndex(motionevent, j);
            if(i1 == -1)
            {
                Log.e("NestedScrollView", (new StringBuilder()).append("Invalid pointerId=").append(j).append(" in onInterceptTouchEvent").toString());
            } else
            {
                int k = (int)MotionEventCompat.getY(motionevent, i1);
                if(Math.abs(k - mLastMotionY) > mTouchSlop && (getNestedScrollAxes() & 2) == 0)
                {
                    mIsBeingDragged = true;
                    mLastMotionY = k;
                    initVelocityTrackerIfNotExists();
                    mVelocityTracker.addMovement(motionevent);
                    mNestedYOffset = 0;
                    motionevent = getParent();
                    if(motionevent != null)
                        motionevent.requestDisallowInterceptTouchEvent(true);
                }
            }
        }
          goto _L10
_L4:
        int l = (int)motionevent.getY();
        if(!inChild((int)motionevent.getX(), l))
        {
            mIsBeingDragged = false;
            recycleVelocityTracker();
        } else
        {
            mLastMotionY = l;
            mActivePointerId = MotionEventCompat.getPointerId(motionevent, 0);
            initOrResetVelocityTracker();
            mVelocityTracker.addMovement(motionevent);
            mScroller.computeScrollOffset();
            if(!mScroller.isFinished())
                flag1 = true;
            mIsBeingDragged = flag1;
            startNestedScroll(2);
        }
          goto _L10
_L5:
        mIsBeingDragged = false;
        mActivePointerId = -1;
        recycleVelocityTracker();
        if(mScroller.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange()))
            ViewCompat.postInvalidateOnAnimation(this);
        stopNestedScroll();
          goto _L10
        onSecondaryPointerUp(motionevent);
          goto _L10
    }

    protected void onLayout(boolean flag, int i, int j, int k, int l)
    {
        super.onLayout(flag, i, j, k, l);
        mIsLayoutDirty = false;
        if(mChildToScrollTo != null && isViewDescendantOf(mChildToScrollTo, this))
            scrollToChild(mChildToScrollTo);
        mChildToScrollTo = null;
        if(mIsLaidOut) goto _L2; else goto _L1
_L1:
        if(mSavedState != null)
        {
            scrollTo(getScrollX(), mSavedState.scrollPosition);
            mSavedState = null;
        }
        if(getChildCount() > 0)
            i = getChildAt(0).getMeasuredHeight();
        else
            i = 0;
        i = Math.max(0, i - (l - j - getPaddingBottom() - getPaddingTop()));
        if(getScrollY() <= i) goto _L4; else goto _L3
_L3:
        scrollTo(getScrollX(), i);
_L2:
        scrollTo(getScrollX(), getScrollY());
        mIsLaidOut = true;
        return;
_L4:
        if(getScrollY() < 0)
            scrollTo(getScrollX(), 0);
        if(true) goto _L2; else goto _L5
_L5:
    }

    protected void onMeasure(int i, int j)
    {
        super.onMeasure(i, j);
        break MISSING_BLOCK_LABEL_6;
        while(true) 
        {
            do
                return;
            while(!mFillViewport || android.view.View.MeasureSpec.getMode(j) == 0 || getChildCount() <= 0);
            View view = getChildAt(0);
            j = getMeasuredHeight();
            if(view.getMeasuredHeight() < j)
            {
                android.widget.FrameLayout.LayoutParams layoutparams = (android.widget.FrameLayout.LayoutParams)view.getLayoutParams();
                view.measure(getChildMeasureSpec(i, getPaddingLeft() + getPaddingRight(), layoutparams.width), android.view.View.MeasureSpec.makeMeasureSpec(j - getPaddingTop() - getPaddingBottom(), 0x40000000));
            }
        }
    }

    public boolean onNestedFling(View view, float f, float f1, boolean flag)
    {
        if(!flag)
        {
            flingWithNestedDispatch((int)f1);
            flag = true;
        } else
        {
            flag = false;
        }
        return flag;
    }

    public boolean onNestedPreFling(View view, float f, float f1)
    {
        return false;
    }

    public void onNestedPreScroll(View view, int i, int j, int ai[])
    {
    }

    public void onNestedScroll(View view, int i, int j, int k, int l)
    {
        i = getScrollY();
        scrollBy(0, l);
        i = getScrollY() - i;
        dispatchNestedScroll(0, i, 0, l - i, null);
    }

    public void onNestedScrollAccepted(View view, View view1, int i)
    {
        mParentHelper.onNestedScrollAccepted(view, view1, i);
        startNestedScroll(2);
    }

    protected void onOverScrolled(int i, int j, boolean flag, boolean flag1)
    {
        super.scrollTo(i, j);
    }

    protected boolean onRequestFocusInDescendants(int i, Rect rect)
    {
        boolean flag;
        int j;
        View view;
        flag = false;
        if(i == 2)
        {
            j = 130;
        } else
        {
            j = i;
            if(i == 1)
                j = 33;
        }
        if(rect == null)
            view = FocusFinder.getInstance().findNextFocus(this, null, j);
        else
            view = FocusFinder.getInstance().findNextFocusFromRect(this, rect, j);
        break MISSING_BLOCK_LABEL_28;
        if(view != null && !isOffScreen(view))
            flag = view.requestFocus(j, rect);
        return flag;
    }

    protected void onRestoreInstanceState(Parcelable parcelable)
    {
        parcelable = (SavedState)parcelable;
        super.onRestoreInstanceState(parcelable.getSuperState());
        mSavedState = parcelable;
        requestLayout();
    }

    protected Parcelable onSaveInstanceState()
    {
        SavedState savedstate = new SavedState(super.onSaveInstanceState());
        savedstate.scrollPosition = getScrollY();
        return savedstate;
    }

    protected void onScrollChanged(int i, int j, int k, int l)
    {
        super.onScrollChanged(i, j, k, l);
        if(mOnScrollChangeListener != null)
            mOnScrollChangeListener.onScrollChange(this, i, j, k, l);
    }

    protected void onSizeChanged(int i, int j, int k, int l)
    {
        View view;
        super.onSizeChanged(i, j, k, l);
        view = findFocus();
        break MISSING_BLOCK_LABEL_15;
        if(view != null && this != view && isWithinDeltaOfScreen(view, 0, l))
        {
            view.getDrawingRect(mTempRect);
            offsetDescendantRectToMyCoords(view, mTempRect);
            doScrollY(computeScrollDeltaToGetChildRectOnScreen(mTempRect));
        }
        return;
    }

    public boolean onStartNestedScroll(View view, View view1, int i)
    {
        boolean flag;
        if((i & 2) != 0)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public void onStopNestedScroll(View view)
    {
        mParentHelper.onStopNestedScroll(view);
        stopNestedScroll();
    }

    public boolean onTouchEvent(MotionEvent motionevent)
    {
        MotionEvent motionevent1;
        int i;
        initVelocityTrackerIfNotExists();
        motionevent1 = MotionEvent.obtain(motionevent);
        i = MotionEventCompat.getActionMasked(motionevent);
        if(i == 0)
            mNestedYOffset = 0;
        motionevent1.offsetLocation(0.0F, mNestedYOffset);
        i;
        JVM INSTR tableswitch 0 6: default 76
    //                   0 101
    //                   1 736
    //                   2 207
    //                   3 831
    //                   4 76
    //                   5 886
    //                   6 913;
           goto _L1 _L2 _L3 _L4 _L5 _L1 _L6 _L7
_L1:
        boolean flag;
        if(mVelocityTracker != null)
            mVelocityTracker.addMovement(motionevent1);
        motionevent1.recycle();
        flag = true;
_L9:
        return flag;
_L2:
        if(getChildCount() != 0)
            break; /* Loop/switch isn't completed */
        flag = false;
        if(true) goto _L9; else goto _L8
_L8:
        boolean flag1;
        if(!mScroller.isFinished())
            flag1 = true;
        else
            flag1 = false;
        mIsBeingDragged = flag1;
        if(flag1)
        {
            ViewParent viewparent = getParent();
            if(viewparent != null)
                viewparent.requestDisallowInterceptTouchEvent(true);
        }
        if(!mScroller.isFinished())
            mScroller.abortAnimation();
        mLastMotionY = (int)motionevent.getY();
        mActivePointerId = MotionEventCompat.getPointerId(motionevent, 0);
        startNestedScroll(2);
        continue; /* Loop/switch isn't completed */
_L4:
        int k;
        int l;
        int i1;
        k = MotionEventCompat.findPointerIndex(motionevent, mActivePointerId);
        if(k == -1)
        {
            Log.e("NestedScrollView", (new StringBuilder()).append("Invalid pointerId=").append(mActivePointerId).append(" in onTouchEvent").toString());
            continue; /* Loop/switch isn't completed */
        }
        l = (int)MotionEventCompat.getY(motionevent, k);
        i = mLastMotionY - l;
        i1 = i;
        if(dispatchNestedPreScroll(0, i, mScrollConsumed, mScrollOffset))
        {
            i1 = i - mScrollConsumed[1];
            motionevent1.offsetLocation(0.0F, mScrollOffset[1]);
            mNestedYOffset = mNestedYOffset + mScrollOffset[1];
        }
        i = i1;
        int j1;
        if(!mIsBeingDragged)
        {
            i = i1;
            if(Math.abs(i1) > mTouchSlop)
            {
                ViewParent viewparent1 = getParent();
                if(viewparent1 != null)
                    viewparent1.requestDisallowInterceptTouchEvent(true);
                mIsBeingDragged = true;
                int k1;
                if(i1 > 0)
                    i = i1 - mTouchSlop;
                else
                    i = i1 + mTouchSlop;
            }
        }
        if(!mIsBeingDragged)
            continue; /* Loop/switch isn't completed */
        mLastMotionY = l - mScrollOffset[1];
        j1 = getScrollY();
        l = getScrollRange();
        i1 = ViewCompat.getOverScrollMode(this);
        if(i1 == 0 || i1 == 1 && l > 0)
            i1 = 1;
        else
            i1 = 0;
        if(overScrollByCompat(0, i, 0, getScrollY(), 0, l, 0, 0, true) && !hasNestedScrollingParent())
            mVelocityTracker.clear();
        k1 = getScrollY() - j1;
        if(dispatchNestedScroll(0, k1, 0, i - k1, mScrollOffset))
        {
            mLastMotionY = mLastMotionY - mScrollOffset[1];
            motionevent1.offsetLocation(0.0F, mScrollOffset[1]);
            mNestedYOffset = mNestedYOffset + mScrollOffset[1];
            continue; /* Loop/switch isn't completed */
        }
        if(i1 == 0)
            continue; /* Loop/switch isn't completed */
        ensureGlows();
        i1 = j1 + i;
        if(i1 >= 0) goto _L11; else goto _L10
_L10:
        mEdgeGlowTop.onPull((float)i / (float)getHeight(), MotionEventCompat.getX(motionevent, k) / (float)getWidth());
        if(!mEdgeGlowBottom.isFinished())
            mEdgeGlowBottom.onRelease();
_L12:
        if(mEdgeGlowTop != null && (!mEdgeGlowTop.isFinished() || !mEdgeGlowBottom.isFinished()))
            ViewCompat.postInvalidateOnAnimation(this);
        continue; /* Loop/switch isn't completed */
_L11:
        if(i1 > l)
        {
            mEdgeGlowBottom.onPull((float)i / (float)getHeight(), 1.0F - MotionEventCompat.getX(motionevent, k) / (float)getWidth());
            if(!mEdgeGlowTop.isFinished())
                mEdgeGlowTop.onRelease();
        }
        if(true) goto _L12; else goto _L3
_L3:
        if(!mIsBeingDragged) goto _L14; else goto _L13
_L13:
        motionevent = mVelocityTracker;
        motionevent.computeCurrentVelocity(1000, mMaximumVelocity);
        i = (int)VelocityTrackerCompat.getYVelocity(motionevent, mActivePointerId);
        if(Math.abs(i) <= mMinimumVelocity) goto _L16; else goto _L15
_L15:
        flingWithNestedDispatch(-i);
_L14:
        mActivePointerId = -1;
        endDrag();
        continue; /* Loop/switch isn't completed */
_L16:
        if(mScroller.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange()))
            ViewCompat.postInvalidateOnAnimation(this);
        if(true) goto _L14; else goto _L5
_L5:
        if(mIsBeingDragged && getChildCount() > 0 && mScroller.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange()))
            ViewCompat.postInvalidateOnAnimation(this);
        mActivePointerId = -1;
        endDrag();
        continue; /* Loop/switch isn't completed */
_L6:
        int j = MotionEventCompat.getActionIndex(motionevent);
        mLastMotionY = (int)MotionEventCompat.getY(motionevent, j);
        mActivePointerId = MotionEventCompat.getPointerId(motionevent, j);
        continue; /* Loop/switch isn't completed */
_L7:
        onSecondaryPointerUp(motionevent);
        mLastMotionY = (int)MotionEventCompat.getY(motionevent, MotionEventCompat.findPointerIndex(motionevent, mActivePointerId));
        if(true) goto _L1; else goto _L17
_L17:
    }

    boolean overScrollByCompat(int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1, boolean flag)
    {
        int i2 = ViewCompat.getOverScrollMode(this);
        boolean flag1;
        boolean flag2;
        boolean flag3;
        if(computeHorizontalScrollRange() > computeHorizontalScrollExtent())
            flag1 = true;
        else
            flag1 = false;
        if(computeVerticalScrollRange() > computeVerticalScrollExtent())
            flag2 = true;
        else
            flag2 = false;
        if(i2 == 0 || i2 == 1 && flag1)
            flag1 = true;
        else
            flag1 = false;
        if(i2 == 0 || i2 == 1 && flag2)
            flag2 = true;
        else
            flag2 = false;
        k += i;
        if(!flag1)
            k1 = 0;
        l += j;
        if(!flag2)
            l1 = 0;
        j = -k1;
        i = k1 + i1;
        i1 = -l1;
        j1 = l1 + j1;
        flag = false;
        if(k > i)
        {
            flag = true;
        } else
        {
            i = k;
            if(k < j)
            {
                i = j;
                flag = true;
            }
        }
        flag3 = false;
        if(l > j1)
        {
            j = j1;
            flag3 = true;
        } else
        {
            j = l;
            if(l < i1)
            {
                j = i1;
                flag3 = true;
            }
        }
        if(flag3)
            mScroller.springBack(i, j, 0, 0, 0, getScrollRange());
        onOverScrolled(i, j, flag, flag3);
        if(flag || flag3)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public boolean pageScroll(int i)
    {
        int k;
        int j;
        View view;
        if(i == 130)
            j = 1;
        else
            j = 0;
        k = getHeight();
        if(j == 0) goto _L2; else goto _L1
_L1:
        mTempRect.top = getScrollY() + k;
        j = getChildCount();
        if(j > 0)
        {
            view = getChildAt(j - 1);
            if(mTempRect.top + k > view.getBottom())
                mTempRect.top = view.getBottom() - k;
        }
_L4:
        mTempRect.bottom = mTempRect.top + k;
        return scrollAndFocus(i, mTempRect.top, mTempRect.bottom);
_L2:
        mTempRect.top = getScrollY() - k;
        if(mTempRect.top < 0)
            mTempRect.top = 0;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void requestChildFocus(View view, View view1)
    {
        if(!mIsLayoutDirty)
            scrollToChild(view1);
        else
            mChildToScrollTo = view1;
        super.requestChildFocus(view, view1);
    }

    public boolean requestChildRectangleOnScreen(View view, Rect rect, boolean flag)
    {
        rect.offset(view.getLeft() - view.getScrollX(), view.getTop() - view.getScrollY());
        return scrollToChildRect(rect, flag);
    }

    public void requestDisallowInterceptTouchEvent(boolean flag)
    {
        if(flag)
            recycleVelocityTracker();
        super.requestDisallowInterceptTouchEvent(flag);
    }

    public void requestLayout()
    {
        mIsLayoutDirty = true;
        super.requestLayout();
    }

    public void scrollTo(int i, int j)
    {
        if(getChildCount() > 0)
        {
            View view = getChildAt(0);
            i = clamp(i, getWidth() - getPaddingRight() - getPaddingLeft(), view.getWidth());
            j = clamp(j, getHeight() - getPaddingBottom() - getPaddingTop(), view.getHeight());
            if(i != getScrollX() || j != getScrollY())
                super.scrollTo(i, j);
        }
    }

    public void setFillViewport(boolean flag)
    {
        if(flag != mFillViewport)
        {
            mFillViewport = flag;
            requestLayout();
        }
    }

    public void setNestedScrollingEnabled(boolean flag)
    {
        mChildHelper.setNestedScrollingEnabled(flag);
    }

    public void setOnScrollChangeListener(OnScrollChangeListener onscrollchangelistener)
    {
        mOnScrollChangeListener = onscrollchangelistener;
    }

    public void setSmoothScrollingEnabled(boolean flag)
    {
        mSmoothScrollingEnabled = flag;
    }

    public boolean shouldDelayChildPressedState()
    {
        return true;
    }

    public final void smoothScrollBy(int i, int j)
    {
        if(getChildCount() != 0) goto _L2; else goto _L1
_L1:
        return;
_L2:
        if(AnimationUtils.currentAnimationTimeMillis() - mLastScroll <= 250L)
            break; /* Loop/switch isn't completed */
        i = getHeight();
        int k = getPaddingBottom();
        int l = getPaddingTop();
        k = Math.max(0, getChildAt(0).getHeight() - (i - k - l));
        i = getScrollY();
        j = Math.max(0, Math.min(i + j, k));
        mScroller.startScroll(getScrollX(), i, 0, j - i);
        ViewCompat.postInvalidateOnAnimation(this);
_L4:
        mLastScroll = AnimationUtils.currentAnimationTimeMillis();
        if(true) goto _L1; else goto _L3
_L3:
        if(!mScroller.isFinished())
            mScroller.abortAnimation();
        scrollBy(i, j);
          goto _L4
        if(true) goto _L1; else goto _L5
_L5:
    }

    public final void smoothScrollTo(int i, int j)
    {
        smoothScrollBy(i - getScrollX(), j - getScrollY());
    }

    public boolean startNestedScroll(int i)
    {
        return mChildHelper.startNestedScroll(i);
    }

    public void stopNestedScroll()
    {
        mChildHelper.stopNestedScroll();
    }

    private static final AccessibilityDelegate ACCESSIBILITY_DELEGATE = new AccessibilityDelegate();
    static final int ANIMATED_SCROLL_GAP = 250;
    private static final int INVALID_POINTER = -1;
    static final float MAX_SCROLL_FACTOR = 0.5F;
    private static final int SCROLLVIEW_STYLEABLE[] = {
        0x101017a
    };
    private static final String TAG = "NestedScrollView";
    private int mActivePointerId;
    private final NestedScrollingChildHelper mChildHelper;
    private View mChildToScrollTo;
    private EdgeEffectCompat mEdgeGlowBottom;
    private EdgeEffectCompat mEdgeGlowTop;
    private boolean mFillViewport;
    private boolean mIsBeingDragged;
    private boolean mIsLaidOut;
    private boolean mIsLayoutDirty;
    private int mLastMotionY;
    private long mLastScroll;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private int mNestedYOffset;
    private OnScrollChangeListener mOnScrollChangeListener;
    private final NestedScrollingParentHelper mParentHelper;
    private SavedState mSavedState;
    private final int mScrollConsumed[];
    private final int mScrollOffset[];
    private ScrollerCompat mScroller;
    private boolean mSmoothScrollingEnabled;
    private final Rect mTempRect;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    private float mVerticalScrollFactor;


}
