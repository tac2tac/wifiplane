// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.*;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.*;
import android.view.*;
import android.view.accessibility.AccessibilityEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

// Referenced classes of package android.support.v4.widget:
//            ViewDragHelper

public class SlidingPaneLayout extends ViewGroup
{
    class AccessibilityDelegate extends AccessibilityDelegateCompat
    {

        private void copyNodeInfoNoChildren(AccessibilityNodeInfoCompat accessibilitynodeinfocompat, AccessibilityNodeInfoCompat accessibilitynodeinfocompat1)
        {
            Rect rect = mTmpRect;
            accessibilitynodeinfocompat1.getBoundsInParent(rect);
            accessibilitynodeinfocompat.setBoundsInParent(rect);
            accessibilitynodeinfocompat1.getBoundsInScreen(rect);
            accessibilitynodeinfocompat.setBoundsInScreen(rect);
            accessibilitynodeinfocompat.setVisibleToUser(accessibilitynodeinfocompat1.isVisibleToUser());
            accessibilitynodeinfocompat.setPackageName(accessibilitynodeinfocompat1.getPackageName());
            accessibilitynodeinfocompat.setClassName(accessibilitynodeinfocompat1.getClassName());
            accessibilitynodeinfocompat.setContentDescription(accessibilitynodeinfocompat1.getContentDescription());
            accessibilitynodeinfocompat.setEnabled(accessibilitynodeinfocompat1.isEnabled());
            accessibilitynodeinfocompat.setClickable(accessibilitynodeinfocompat1.isClickable());
            accessibilitynodeinfocompat.setFocusable(accessibilitynodeinfocompat1.isFocusable());
            accessibilitynodeinfocompat.setFocused(accessibilitynodeinfocompat1.isFocused());
            accessibilitynodeinfocompat.setAccessibilityFocused(accessibilitynodeinfocompat1.isAccessibilityFocused());
            accessibilitynodeinfocompat.setSelected(accessibilitynodeinfocompat1.isSelected());
            accessibilitynodeinfocompat.setLongClickable(accessibilitynodeinfocompat1.isLongClickable());
            accessibilitynodeinfocompat.addAction(accessibilitynodeinfocompat1.getActions());
            accessibilitynodeinfocompat.setMovementGranularities(accessibilitynodeinfocompat1.getMovementGranularities());
        }

        public boolean filter(View view)
        {
            return isDimmed(view);
        }

        public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityevent)
        {
            super.onInitializeAccessibilityEvent(view, accessibilityevent);
            accessibilityevent.setClassName(android/support/v4/widget/SlidingPaneLayout.getName());
        }

        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilitynodeinfocompat)
        {
            AccessibilityNodeInfoCompat accessibilitynodeinfocompat1 = AccessibilityNodeInfoCompat.obtain(accessibilitynodeinfocompat);
            super.onInitializeAccessibilityNodeInfo(view, accessibilitynodeinfocompat1);
            copyNodeInfoNoChildren(accessibilitynodeinfocompat, accessibilitynodeinfocompat1);
            accessibilitynodeinfocompat1.recycle();
            accessibilitynodeinfocompat.setClassName(android/support/v4/widget/SlidingPaneLayout.getName());
            accessibilitynodeinfocompat.setSource(view);
            view = ViewCompat.getParentForAccessibility(view);
            if(view instanceof View)
                accessibilitynodeinfocompat.setParent((View)view);
            int i = getChildCount();
            for(int j = 0; j < i; j++)
            {
                view = getChildAt(j);
                if(!filter(view) && view.getVisibility() == 0)
                {
                    ViewCompat.setImportantForAccessibility(view, 1);
                    accessibilitynodeinfocompat.addChild(view);
                }
            }

        }

        public boolean onRequestSendAccessibilityEvent(ViewGroup viewgroup, View view, AccessibilityEvent accessibilityevent)
        {
            boolean flag;
            if(!filter(view))
                flag = super.onRequestSendAccessibilityEvent(viewgroup, view, accessibilityevent);
            else
                flag = false;
            return flag;
        }

        private final Rect mTmpRect = new Rect();
        final SlidingPaneLayout this$0;

        AccessibilityDelegate()
        {
            this$0 = SlidingPaneLayout.this;
            super();
        }
    }

    private class DisableLayerRunnable
        implements Runnable
    {

        public void run()
        {
            if(mChildView.getParent() == SlidingPaneLayout.this)
            {
                ViewCompat.setLayerType(mChildView, 0, null);
                invalidateChildRegion(mChildView);
            }
            mPostedRunnables.remove(this);
        }

        final View mChildView;
        final SlidingPaneLayout this$0;

        DisableLayerRunnable(View view)
        {
            this$0 = SlidingPaneLayout.this;
            super();
            mChildView = view;
        }
    }

    private class DragHelperCallback extends ViewDragHelper.Callback
    {

        public int clampViewPositionHorizontal(View view, int i, int j)
        {
            view = (LayoutParams)mSlideableView.getLayoutParams();
            if(isLayoutRtlSupport())
            {
                j = getWidth() - (getPaddingRight() + ((LayoutParams) (view)).rightMargin + mSlideableView.getWidth());
                int k = mSlideRange;
                i = Math.max(Math.min(i, j), j - k);
            } else
            {
                j = getPaddingLeft() + ((LayoutParams) (view)).leftMargin;
                int l = mSlideRange;
                i = Math.min(Math.max(i, j), j + l);
            }
            return i;
        }

        public int clampViewPositionVertical(View view, int i, int j)
        {
            return view.getTop();
        }

        public int getViewHorizontalDragRange(View view)
        {
            return mSlideRange;
        }

        public void onEdgeDragStarted(int i, int j)
        {
            mDragHelper.captureChildView(mSlideableView, j);
        }

        public void onViewCaptured(View view, int i)
        {
            setAllChildrenVisible();
        }

        public void onViewDragStateChanged(int i)
        {
            if(mDragHelper.getViewDragState() == 0)
                if(mSlideOffset == 0.0F)
                {
                    updateObscuredViewsVisibility(mSlideableView);
                    dispatchOnPanelClosed(mSlideableView);
                    mPreservedOpenState = false;
                } else
                {
                    dispatchOnPanelOpened(mSlideableView);
                    mPreservedOpenState = true;
                }
        }

        public void onViewPositionChanged(View view, int i, int j, int k, int l)
        {
            onPanelDragged(i);
            invalidate();
        }

        public void onViewReleased(View view, float f, float f1)
        {
            LayoutParams layoutparams = (LayoutParams)view.getLayoutParams();
            if(!isLayoutRtlSupport()) goto _L2; else goto _L1
_L1:
            int l;
label0:
            {
                int i = getPaddingRight() + layoutparams.rightMargin;
                if(f >= 0.0F)
                {
                    l = i;
                    if(f != 0.0F)
                        break label0;
                    l = i;
                    if(mSlideOffset <= 0.5F)
                        break label0;
                }
                l = i + mSlideRange;
            }
            int j = mSlideableView.getWidth();
            l = getWidth() - l - j;
_L4:
            mDragHelper.settleCapturedViewAt(l, view.getTop());
            invalidate();
            return;
_L2:
            int k = getPaddingLeft() + layoutparams.leftMargin;
            if(f <= 0.0F)
            {
                l = k;
                if(f != 0.0F)
                    continue; /* Loop/switch isn't completed */
                l = k;
                if(mSlideOffset <= 0.5F)
                    continue; /* Loop/switch isn't completed */
            }
            l = k + mSlideRange;
            if(true) goto _L4; else goto _L3
_L3:
        }

        public boolean tryCaptureView(View view, int i)
        {
            boolean flag;
            if(mIsUnableToDrag)
                flag = false;
            else
                flag = ((LayoutParams)view.getLayoutParams()).slideable;
            return flag;
        }

        final SlidingPaneLayout this$0;

        private DragHelperCallback()
        {
            this$0 = SlidingPaneLayout.this;
            super();
        }

    }

    public static class LayoutParams extends android.view.ViewGroup.MarginLayoutParams
    {

        private static final int ATTRS[] = {
            0x1010181
        };
        Paint dimPaint;
        boolean dimWhenOffset;
        boolean slideable;
        public float weight;


        public LayoutParams()
        {
            super(-1, -1);
            weight = 0.0F;
        }

        public LayoutParams(int i, int j)
        {
            super(i, j);
            weight = 0.0F;
        }

        public LayoutParams(Context context, AttributeSet attributeset)
        {
            super(context, attributeset);
            weight = 0.0F;
            context = context.obtainStyledAttributes(attributeset, ATTRS);
            weight = context.getFloat(0, 0.0F);
            context.recycle();
        }

        public LayoutParams(LayoutParams layoutparams)
        {
            super(layoutparams);
            weight = 0.0F;
            weight = layoutparams.weight;
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams layoutparams)
        {
            super(layoutparams);
            weight = 0.0F;
        }

        public LayoutParams(android.view.ViewGroup.MarginLayoutParams marginlayoutparams)
        {
            super(marginlayoutparams);
            weight = 0.0F;
        }
    }

    public static interface PanelSlideListener
    {

        public abstract void onPanelClosed(View view);

        public abstract void onPanelOpened(View view);

        public abstract void onPanelSlide(View view, float f);
    }

    static class SavedState extends android.view.View.BaseSavedState
    {

        public void writeToParcel(Parcel parcel, int i)
        {
            super.writeToParcel(parcel, i);
            if(isOpen)
                i = 1;
            else
                i = 0;
            parcel.writeInt(i);
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
        boolean isOpen;


        private SavedState(Parcel parcel)
        {
            super(parcel);
            boolean flag;
            if(parcel.readInt() != 0)
                flag = true;
            else
                flag = false;
            isOpen = flag;
        }


        SavedState(Parcelable parcelable)
        {
            super(parcelable);
        }
    }

    public static class SimplePanelSlideListener
        implements PanelSlideListener
    {

        public void onPanelClosed(View view)
        {
        }

        public void onPanelOpened(View view)
        {
        }

        public void onPanelSlide(View view, float f)
        {
        }

        public SimplePanelSlideListener()
        {
        }
    }

    static interface SlidingPanelLayoutImpl
    {

        public abstract void invalidateChildRegion(SlidingPaneLayout slidingpanelayout, View view);
    }

    static class SlidingPanelLayoutImplBase
        implements SlidingPanelLayoutImpl
    {

        public void invalidateChildRegion(SlidingPaneLayout slidingpanelayout, View view)
        {
            ViewCompat.postInvalidateOnAnimation(slidingpanelayout, view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        }

        SlidingPanelLayoutImplBase()
        {
        }
    }

    static class SlidingPanelLayoutImplJB extends SlidingPanelLayoutImplBase
    {

        public void invalidateChildRegion(SlidingPaneLayout slidingpanelayout, View view)
        {
            if(mGetDisplayList != null && mRecreateDisplayList != null)
            {
                try
                {
                    mRecreateDisplayList.setBoolean(view, true);
                    mGetDisplayList.invoke(view, (Object[])null);
                }
                catch(Exception exception)
                {
                    Log.e("SlidingPaneLayout", "Error refreshing display list state", exception);
                }
                super.invalidateChildRegion(slidingpanelayout, view);
            } else
            {
                view.invalidate();
            }
        }

        private Method mGetDisplayList;
        private Field mRecreateDisplayList;

        SlidingPanelLayoutImplJB()
        {
            try
            {
                mGetDisplayList = android/view/View.getDeclaredMethod("getDisplayList", (Class[])null);
            }
            catch(NoSuchMethodException nosuchmethodexception)
            {
                Log.e("SlidingPaneLayout", "Couldn't fetch getDisplayList method; dimming won't work right.", nosuchmethodexception);
            }
            mRecreateDisplayList = android/view/View.getDeclaredField("mRecreateDisplayList");
            mRecreateDisplayList.setAccessible(true);
_L1:
            return;
            NoSuchFieldException nosuchfieldexception;
            nosuchfieldexception;
            Log.e("SlidingPaneLayout", "Couldn't fetch mRecreateDisplayList field; dimming will be slow.", nosuchfieldexception);
              goto _L1
        }
    }

    static class SlidingPanelLayoutImplJBMR1 extends SlidingPanelLayoutImplBase
    {

        public void invalidateChildRegion(SlidingPaneLayout slidingpanelayout, View view)
        {
            ViewCompat.setLayerPaint(view, ((LayoutParams)view.getLayoutParams()).dimPaint);
        }

        SlidingPanelLayoutImplJBMR1()
        {
        }
    }


    public SlidingPaneLayout(Context context)
    {
        this(context, null);
    }

    public SlidingPaneLayout(Context context, AttributeSet attributeset)
    {
        this(context, attributeset, 0);
    }

    public SlidingPaneLayout(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        mSliderFadeColor = 0xcccccccc;
        mFirstLayout = true;
        mTmpRect = new Rect();
        mPostedRunnables = new ArrayList();
        float f = context.getResources().getDisplayMetrics().density;
        mOverhangSize = (int)(32F * f + 0.5F);
        ViewConfiguration.get(context);
        setWillNotDraw(false);
        ViewCompat.setAccessibilityDelegate(this, new AccessibilityDelegate());
        ViewCompat.setImportantForAccessibility(this, 1);
        mDragHelper = ViewDragHelper.create(this, 0.5F, new DragHelperCallback());
        mDragHelper.setMinVelocity(400F * f);
    }

    private boolean closePane(View view, int i)
    {
        boolean flag = false;
        if(mFirstLayout || smoothSlideTo(0.0F, i))
        {
            mPreservedOpenState = false;
            flag = true;
        }
        return flag;
    }

    private void dimChildView(View view, float f, int i)
    {
        LayoutParams layoutparams = (LayoutParams)view.getLayoutParams();
        if(f <= 0.0F || i == 0) goto _L2; else goto _L1
_L1:
        int j = (int)((float)((0xff000000 & i) >>> 24) * f);
        if(layoutparams.dimPaint == null)
            layoutparams.dimPaint = new Paint();
        layoutparams.dimPaint.setColorFilter(new PorterDuffColorFilter(j << 24 | 0xffffff & i, android.graphics.PorterDuff.Mode.SRC_OVER));
        if(ViewCompat.getLayerType(view) != 2)
            ViewCompat.setLayerType(view, 2, layoutparams.dimPaint);
        invalidateChildRegion(view);
_L4:
        return;
_L2:
        if(ViewCompat.getLayerType(view) != 0)
        {
            if(layoutparams.dimPaint != null)
                layoutparams.dimPaint.setColorFilter(null);
            view = new DisableLayerRunnable(view);
            mPostedRunnables.add(view);
            ViewCompat.postOnAnimation(this, view);
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    private void invalidateChildRegion(View view)
    {
        IMPL.invalidateChildRegion(this, view);
    }

    private boolean isLayoutRtlSupport()
    {
        boolean flag = true;
        if(ViewCompat.getLayoutDirection(this) != 1)
            flag = false;
        return flag;
    }

    private void onPanelDragged(int i)
    {
        if(mSlideableView == null)
        {
            mSlideOffset = 0.0F;
        } else
        {
            boolean flag = isLayoutRtlSupport();
            LayoutParams layoutparams = (LayoutParams)mSlideableView.getLayoutParams();
            int j = mSlideableView.getWidth();
            if(flag)
                i = getWidth() - i - j;
            int k;
            if(flag)
                j = getPaddingRight();
            else
                j = getPaddingLeft();
            if(flag)
                k = layoutparams.rightMargin;
            else
                k = layoutparams.leftMargin;
            mSlideOffset = (float)(i - (j + k)) / (float)mSlideRange;
            if(mParallaxBy != 0)
                parallaxOtherViews(mSlideOffset);
            if(layoutparams.dimWhenOffset)
                dimChildView(mSlideableView, mSlideOffset, mSliderFadeColor);
            dispatchOnPanelSlide(mSlideableView);
        }
    }

    private boolean openPane(View view, int i)
    {
        boolean flag = true;
        if(mFirstLayout || smoothSlideTo(1.0F, i))
            mPreservedOpenState = true;
        else
            flag = false;
        return flag;
    }

    private void parallaxOtherViews(float f)
    {
        boolean flag;
        Object obj;
        int i;
        flag = isLayoutRtlSupport();
        obj = (LayoutParams)mSlideableView.getLayoutParams();
        if(((LayoutParams) (obj)).dimWhenOffset)
        {
label0:
            {
                int j;
                int k;
                if(flag)
                    i = ((LayoutParams) (obj)).rightMargin;
                else
                    i = ((LayoutParams) (obj)).leftMargin;
                if(i <= 0)
                {
                    i = 1;
                    break label0;
                }
            }
        }
        i = 0;
        continue;
        while(true) 
        {
            j = getChildCount();
            k = 0;
            while(k < j) 
            {
                obj = getChildAt(k);
                if(obj != mSlideableView)
                {
                    int l = (int)((1.0F - mParallaxOffset) * (float)mParallaxBy);
                    mParallaxOffset = f;
                    int i1 = l - (int)((1.0F - f) * (float)mParallaxBy);
                    l = i1;
                    if(flag)
                        l = -i1;
                    ((View) (obj)).offsetLeftAndRight(l);
                    if(i != 0)
                    {
                        float f1;
                        if(flag)
                            f1 = mParallaxOffset - 1.0F;
                        else
                            f1 = 1.0F - mParallaxOffset;
                        dimChildView(((View) (obj)), f1, mCoveredFadeColor);
                    }
                }
                k++;
            }
            return;
        }
    }

    private static boolean viewIsOpaque(View view)
    {
        boolean flag = true;
        if(!ViewCompat.isOpaque(view)) goto _L2; else goto _L1
_L1:
        return flag;
_L2:
        if(android.os.Build.VERSION.SDK_INT >= 18)
        {
            flag = false;
        } else
        {
            view = view.getBackground();
            if(view != null)
            {
                if(view.getOpacity() != -1)
                    flag = false;
            } else
            {
                flag = false;
            }
        }
        if(true) goto _L1; else goto _L3
_L3:
    }

    protected boolean canScroll(View view, boolean flag, int i, int j, int k)
    {
        if(!(view instanceof ViewGroup)) goto _L2; else goto _L1
_L1:
        ViewGroup viewgroup;
        int l;
        int i1;
        int j1;
        viewgroup = (ViewGroup)view;
        l = view.getScrollX();
        i1 = view.getScrollY();
        j1 = viewgroup.getChildCount() - 1;
_L6:
        if(j1 < 0) goto _L2; else goto _L3
_L3:
        View view1 = viewgroup.getChildAt(j1);
        if(j + l < view1.getLeft() || j + l >= view1.getRight() || k + i1 < view1.getTop() || k + i1 >= view1.getBottom() || !canScroll(view1, true, i, (j + l) - view1.getLeft(), (k + i1) - view1.getTop())) goto _L5; else goto _L4
_L4:
        flag = true;
_L7:
        return flag;
_L5:
        j1--;
          goto _L6
_L2:
label0:
        {
            if(!flag)
                break label0;
            if(!isLayoutRtlSupport())
                i = -i;
            if(!ViewCompat.canScrollHorizontally(view, i))
                break label0;
            flag = true;
        }
          goto _L7
        flag = false;
          goto _L7
    }

    public boolean canSlide()
    {
        return mCanSlide;
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams layoutparams)
    {
        boolean flag;
        if((layoutparams instanceof LayoutParams) && super.checkLayoutParams(layoutparams))
            flag = true;
        else
            flag = false;
        return flag;
    }

    public boolean closePane()
    {
        return closePane(mSlideableView, 0);
    }

    public void computeScroll()
    {
        if(mDragHelper.continueSettling(true))
            if(!mCanSlide)
                mDragHelper.abort();
            else
                ViewCompat.postInvalidateOnAnimation(this);
    }

    void dispatchOnPanelClosed(View view)
    {
        if(mPanelSlideListener != null)
            mPanelSlideListener.onPanelClosed(view);
        sendAccessibilityEvent(32);
    }

    void dispatchOnPanelOpened(View view)
    {
        if(mPanelSlideListener != null)
            mPanelSlideListener.onPanelOpened(view);
        sendAccessibilityEvent(32);
    }

    void dispatchOnPanelSlide(View view)
    {
        if(mPanelSlideListener != null)
            mPanelSlideListener.onPanelSlide(view, mSlideOffset);
    }

    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        Drawable drawable;
        View view;
        if(isLayoutRtlSupport())
            drawable = mShadowDrawableRight;
        else
            drawable = mShadowDrawableLeft;
        if(getChildCount() > 1)
            view = getChildAt(1);
        else
            view = null;
        if(view != null && drawable != null)
        {
            int i = view.getTop();
            int j = view.getBottom();
            int k = drawable.getIntrinsicWidth();
            int l;
            int i1;
            if(isLayoutRtlSupport())
            {
                l = view.getRight();
                i1 = l + k;
            } else
            {
                i1 = view.getLeft();
                l = i1 - k;
            }
            drawable.setBounds(l, i, i1, j);
            drawable.draw(canvas);
        }
    }

    protected boolean drawChild(Canvas canvas, View view, long l)
    {
        LayoutParams layoutparams = (LayoutParams)view.getLayoutParams();
        int i = canvas.save(2);
        boolean flag;
        if(mCanSlide && !layoutparams.slideable && mSlideableView != null)
        {
            canvas.getClipBounds(mTmpRect);
            if(isLayoutRtlSupport())
                mTmpRect.left = Math.max(mTmpRect.left, mSlideableView.getRight());
            else
                mTmpRect.right = Math.min(mTmpRect.right, mSlideableView.getLeft());
            canvas.clipRect(mTmpRect);
        }
        if(android.os.Build.VERSION.SDK_INT >= 11)
            flag = super.drawChild(canvas, view, l);
        else
        if(layoutparams.dimWhenOffset && mSlideOffset > 0.0F)
        {
            if(!view.isDrawingCacheEnabled())
                view.setDrawingCacheEnabled(true);
            android.graphics.Bitmap bitmap = view.getDrawingCache();
            if(bitmap != null)
            {
                canvas.drawBitmap(bitmap, view.getLeft(), view.getTop(), layoutparams.dimPaint);
                flag = false;
            } else
            {
                Log.e("SlidingPaneLayout", (new StringBuilder()).append("drawChild: child view ").append(view).append(" returned null drawing cache").toString());
                flag = super.drawChild(canvas, view, l);
            }
        } else
        {
            if(view.isDrawingCacheEnabled())
                view.setDrawingCacheEnabled(false);
            flag = super.drawChild(canvas, view, l);
        }
        canvas.restoreToCount(i);
        return flag;
    }

    protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams()
    {
        return new LayoutParams();
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeset)
    {
        return new LayoutParams(getContext(), attributeset);
    }

    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams layoutparams)
    {
        if(layoutparams instanceof android.view.ViewGroup.MarginLayoutParams)
            layoutparams = new LayoutParams((android.view.ViewGroup.MarginLayoutParams)layoutparams);
        else
            layoutparams = new LayoutParams(layoutparams);
        return layoutparams;
    }

    public int getCoveredFadeColor()
    {
        return mCoveredFadeColor;
    }

    public int getParallaxDistance()
    {
        return mParallaxBy;
    }

    public int getSliderFadeColor()
    {
        return mSliderFadeColor;
    }

    boolean isDimmed(View view)
    {
        boolean flag = false;
        if(view != null) goto _L2; else goto _L1
_L1:
        boolean flag1 = flag;
_L4:
        return flag1;
_L2:
        view = (LayoutParams)view.getLayoutParams();
        flag1 = flag;
        if(mCanSlide)
        {
            flag1 = flag;
            if(((LayoutParams) (view)).dimWhenOffset)
            {
                flag1 = flag;
                if(mSlideOffset > 0.0F)
                    flag1 = true;
            }
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public boolean isOpen()
    {
        boolean flag;
        if(!mCanSlide || mSlideOffset == 1.0F)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public boolean isSlideable()
    {
        return mCanSlide;
    }

    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        mFirstLayout = true;
    }

    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        mFirstLayout = true;
        int i = 0;
        for(int j = mPostedRunnables.size(); i < j; i++)
            ((DisableLayerRunnable)mPostedRunnables.get(i)).run();

        mPostedRunnables.clear();
    }

    public boolean onInterceptTouchEvent(MotionEvent motionevent)
    {
        int i;
        boolean flag;
        i = MotionEventCompat.getActionMasked(motionevent);
        if(!mCanSlide && i == 0 && getChildCount() > 1)
        {
            View view = getChildAt(1);
            if(view != null)
            {
                if(!mDragHelper.isViewUnder(view, (int)motionevent.getX(), (int)motionevent.getY()))
                    flag = true;
                else
                    flag = false;
                mPreservedOpenState = flag;
            }
        }
        if(mCanSlide && (!mIsUnableToDrag || i == 0)) goto _L2; else goto _L1
_L1:
        mDragHelper.cancel();
        flag = super.onInterceptTouchEvent(motionevent);
_L10:
        return flag;
_L2:
        boolean flag1;
        boolean flag2;
        if(i == 3 || i == 1)
        {
            mDragHelper.cancel();
            flag = false;
            continue; /* Loop/switch isn't completed */
        }
        flag1 = false;
        flag2 = flag1;
        i;
        JVM INSTR tableswitch 0 2: default 164
    //                   0 190
    //                   1 168
    //                   2 264;
           goto _L3 _L4 _L5 _L6
_L3:
        flag2 = flag1;
_L5:
        float f;
        float f1;
        if(mDragHelper.shouldInterceptTouchEvent(motionevent) || flag2)
            flag = true;
        else
            flag = false;
        break; /* Loop/switch isn't completed */
_L4:
        mIsUnableToDrag = false;
        f = motionevent.getX();
        f1 = motionevent.getY();
        mInitialMotionX = f;
        mInitialMotionY = f1;
        flag2 = flag1;
        if(mDragHelper.isViewUnder(mSlideableView, (int)f, (int)f1))
        {
            flag2 = flag1;
            if(isDimmed(mSlideableView))
                flag2 = true;
        }
          goto _L5
_L6:
        f1 = motionevent.getX();
        f = motionevent.getY();
        f1 = Math.abs(f1 - mInitialMotionX);
        f = Math.abs(f - mInitialMotionY);
        flag2 = flag1;
        if(f1 <= (float)mDragHelper.getTouchSlop()) goto _L5; else goto _L7
_L7:
        flag2 = flag1;
        if(f <= f1) goto _L5; else goto _L8
_L8:
        mDragHelper.cancel();
        mIsUnableToDrag = true;
        flag = false;
        if(true) goto _L10; else goto _L9
_L9:
    }

    protected void onLayout(boolean flag, int i, int j, int k, int l)
    {
        boolean flag1 = isLayoutRtlSupport();
        int i1;
        int j1;
        int k1;
        if(flag1)
            mDragHelper.setEdgeTrackingEnabled(2);
        else
            mDragHelper.setEdgeTrackingEnabled(1);
        i1 = k - i;
        if(flag1)
            i = getPaddingRight();
        else
            i = getPaddingLeft();
        if(flag1)
            k = getPaddingLeft();
        else
            k = getPaddingRight();
        j1 = getPaddingTop();
        k1 = getChildCount();
        l = i;
        if(mFirstLayout)
        {
            float f;
            boolean flag2;
            if(mCanSlide && mPreservedOpenState)
                f = 1.0F;
            else
                f = 0.0F;
            mSlideOffset = f;
        }
        flag2 = false;
        j = i;
        i = l;
        l = ((flag2) ? 1 : 0);
        while(l < k1) 
        {
            View view = getChildAt(l);
            if(view.getVisibility() != 8)
            {
                LayoutParams layoutparams = (LayoutParams)view.getLayoutParams();
                int i2 = view.getMeasuredWidth();
                int j2 = 0;
                int l1;
                if(layoutparams.slideable)
                {
                    l1 = layoutparams.leftMargin;
                    int k2 = layoutparams.rightMargin;
                    k2 = Math.min(i, i1 - k - mOverhangSize) - j - (l1 + k2);
                    mSlideRange = k2;
                    if(flag1)
                        l1 = layoutparams.rightMargin;
                    else
                        l1 = layoutparams.leftMargin;
                    if(j + l1 + k2 + i2 / 2 > i1 - k)
                        flag = true;
                    else
                        flag = false;
                    layoutparams.dimWhenOffset = flag;
                    k2 = (int)((float)k2 * mSlideOffset);
                    j += k2 + l1;
                    mSlideOffset = (float)k2 / (float)mSlideRange;
                    l1 = j2;
                } else
                if(mCanSlide && mParallaxBy != 0)
                {
                    l1 = (int)((1.0F - mSlideOffset) * (float)mParallaxBy);
                    j = i;
                } else
                {
                    j = i;
                    l1 = j2;
                }
                if(flag1)
                {
                    j2 = (i1 - j) + l1;
                    l1 = j2 - i2;
                } else
                {
                    l1 = j - l1;
                    j2 = l1 + i2;
                }
                view.layout(l1, j1, j2, j1 + view.getMeasuredHeight());
                i += view.getWidth();
            }
            l++;
        }
        if(mFirstLayout)
        {
            if(mCanSlide)
            {
                if(mParallaxBy != 0)
                    parallaxOtherViews(mSlideOffset);
                if(((LayoutParams)mSlideableView.getLayoutParams()).dimWhenOffset)
                    dimChildView(mSlideableView, mSlideOffset, mSliderFadeColor);
            } else
            {
                i = 0;
                while(i < k1) 
                {
                    dimChildView(getChildAt(i), 0.0F, mSliderFadeColor);
                    i++;
                }
            }
            updateObscuredViewsVisibility(mSlideableView);
        }
        mFirstLayout = false;
    }

    protected void onMeasure(int i, int j)
    {
        int k;
        int l;
        int i1;
        k = android.view.View.MeasureSpec.getMode(i);
        l = android.view.View.MeasureSpec.getSize(i);
        i1 = android.view.View.MeasureSpec.getMode(j);
        j = android.view.View.MeasureSpec.getSize(j);
        if(k == 0x40000000) goto _L2; else goto _L1
_L1:
        if(!isInEditMode()) goto _L4; else goto _L3
_L3:
        if(k != 0x80000000) goto _L6; else goto _L5
_L5:
        int k1;
        int l1;
        k1 = l;
        i = j;
        l1 = i1;
_L19:
        l = 0;
        j = -1;
        l1;
        JVM INSTR lookupswitch 2: default 88
    //                   -2147483648: 330
    //                   1073741824: 312;
           goto _L7 _L8 _L9
_L7:
        float f;
        boolean flag;
        int j2;
        int k2;
        int l2;
        f = 0.0F;
        flag = false;
        j2 = k1 - getPaddingLeft() - getPaddingRight();
        k = j2;
        k2 = getChildCount();
        if(k2 > 2)
            Log.e("SlidingPaneLayout", "onMeasure: More than two child views are not supported.");
        mSlideableView = null;
        l2 = 0;
_L14:
        if(l2 >= k2) goto _L11; else goto _L10
_L10:
        View view;
        LayoutParams layoutparams;
        view = getChildAt(l2);
        layoutparams = (LayoutParams)view.getLayoutParams();
        if(view.getVisibility() != 8) goto _L13; else goto _L12
_L12:
        int j3;
        boolean flag1;
        layoutparams.dimWhenOffset = false;
        j3 = k;
        i1 = l;
        flag1 = flag;
_L17:
        l2++;
        flag = flag1;
        l = i1;
        k = j3;
          goto _L14
_L6:
        l1 = i1;
        i = j;
        k1 = l;
        if(k == 0)
        {
            k1 = 300;
            l1 = i1;
            i = j;
        }
        continue; /* Loop/switch isn't completed */
_L4:
        throw new IllegalStateException("Width must have an exact value or MATCH_PARENT");
_L2:
        l1 = i1;
        i = j;
        k1 = l;
        if(i1 == 0)
            if(isInEditMode())
            {
                l1 = i1;
                i = j;
                k1 = l;
                if(i1 == 0)
                {
                    l1 = 0x80000000;
                    i = 300;
                    k1 = l;
                }
            } else
            {
                throw new IllegalStateException("Height must not be UNSPECIFIED");
            }
        continue; /* Loop/switch isn't completed */
_L9:
        j = i - getPaddingTop() - getPaddingBottom();
        l = j;
          goto _L7
_L8:
        j = i - getPaddingTop() - getPaddingBottom();
          goto _L7
_L13:
        float f1 = f;
        if(layoutparams.weight <= 0.0F) goto _L16; else goto _L15
_L15:
        f1 = f + layoutparams.weight;
        flag1 = flag;
        i1 = l;
        f = f1;
        j3 = k;
        if(layoutparams.width == 0) goto _L17; else goto _L16
_L16:
        i = layoutparams.leftMargin + layoutparams.rightMargin;
        if(layoutparams.width == -2)
            i = android.view.View.MeasureSpec.makeMeasureSpec(j2 - i, 0x80000000);
        else
        if(layoutparams.width == -1)
            i = android.view.View.MeasureSpec.makeMeasureSpec(j2 - i, 0x40000000);
        else
            i = android.view.View.MeasureSpec.makeMeasureSpec(layoutparams.width, 0x40000000);
        if(layoutparams.height == -2)
            i1 = android.view.View.MeasureSpec.makeMeasureSpec(j, 0x80000000);
        else
        if(layoutparams.height == -1)
            i1 = android.view.View.MeasureSpec.makeMeasureSpec(j, 0x40000000);
        else
            i1 = android.view.View.MeasureSpec.makeMeasureSpec(layoutparams.height, 0x40000000);
        view.measure(i, i1);
        i1 = view.getMeasuredWidth();
        j3 = view.getMeasuredHeight();
        i = l;
        if(l1 == 0x80000000)
        {
            i = l;
            if(j3 > l)
                i = Math.min(j3, j);
        }
        l = k - i1;
        if(l < 0)
            flag1 = true;
        else
            flag1 = false;
        layoutparams.slideable = flag1;
        flag |= flag1;
        flag1 = flag;
        i1 = i;
        f = f1;
        j3 = l;
        if(layoutparams.slideable)
        {
            mSlideableView = view;
            flag1 = flag;
            i1 = i;
            f = f1;
            j3 = l;
        }
          goto _L17
_L11:
        if(flag || f > 0.0F)
        {
            int i3 = j2 - mOverhangSize;
            int j1 = 0;
            while(j1 < k2) 
            {
                View view1 = getChildAt(j1);
                if(view1.getVisibility() != 8)
                {
                    LayoutParams layoutparams1 = (LayoutParams)view1.getLayoutParams();
                    if(view1.getVisibility() != 8)
                    {
                        int i2;
                        if(layoutparams1.width == 0 && layoutparams1.weight > 0.0F)
                            i = 1;
                        else
                            i = 0;
                        if(i != 0)
                            i2 = 0;
                        else
                            i2 = view1.getMeasuredWidth();
                        if(flag && view1 != mSlideableView)
                        {
                            if(layoutparams1.width < 0 && (i2 > i3 || layoutparams1.weight > 0.0F))
                            {
                                if(i != 0)
                                {
                                    if(layoutparams1.height == -2)
                                        i = android.view.View.MeasureSpec.makeMeasureSpec(j, 0x80000000);
                                    else
                                    if(layoutparams1.height == -1)
                                        i = android.view.View.MeasureSpec.makeMeasureSpec(j, 0x40000000);
                                    else
                                        i = android.view.View.MeasureSpec.makeMeasureSpec(layoutparams1.height, 0x40000000);
                                } else
                                {
                                    i = android.view.View.MeasureSpec.makeMeasureSpec(view1.getMeasuredHeight(), 0x40000000);
                                }
                                view1.measure(android.view.View.MeasureSpec.makeMeasureSpec(i3, 0x40000000), i);
                            }
                        } else
                        if(layoutparams1.weight > 0.0F)
                        {
                            if(layoutparams1.width == 0)
                            {
                                if(layoutparams1.height == -2)
                                    i = android.view.View.MeasureSpec.makeMeasureSpec(j, 0x80000000);
                                else
                                if(layoutparams1.height == -1)
                                    i = android.view.View.MeasureSpec.makeMeasureSpec(j, 0x40000000);
                                else
                                    i = android.view.View.MeasureSpec.makeMeasureSpec(layoutparams1.height, 0x40000000);
                            } else
                            {
                                i = android.view.View.MeasureSpec.makeMeasureSpec(view1.getMeasuredHeight(), 0x40000000);
                            }
                            if(flag)
                            {
                                int k3 = j2 - (layoutparams1.leftMargin + layoutparams1.rightMargin);
                                int i4 = android.view.View.MeasureSpec.makeMeasureSpec(k3, 0x40000000);
                                if(i2 != k3)
                                    view1.measure(i4, i);
                            } else
                            {
                                int l3 = Math.max(0, k);
                                view1.measure(android.view.View.MeasureSpec.makeMeasureSpec(i2 + (int)((layoutparams1.weight * (float)l3) / f), 0x40000000), i);
                            }
                        }
                    }
                }
                j1++;
            }
        }
        setMeasuredDimension(k1, getPaddingTop() + l + getPaddingBottom());
        mCanSlide = flag;
        if(mDragHelper.getViewDragState() != 0 && !flag)
            mDragHelper.abort();
        return;
        if(true) goto _L19; else goto _L18
_L18:
    }

    protected void onRestoreInstanceState(Parcelable parcelable)
    {
        parcelable = (SavedState)parcelable;
        super.onRestoreInstanceState(parcelable.getSuperState());
        if(((SavedState) (parcelable)).isOpen)
            openPane();
        else
            closePane();
        mPreservedOpenState = ((SavedState) (parcelable)).isOpen;
    }

    protected Parcelable onSaveInstanceState()
    {
        SavedState savedstate = new SavedState(super.onSaveInstanceState());
        boolean flag;
        if(isSlideable())
            flag = isOpen();
        else
            flag = mPreservedOpenState;
        savedstate.isOpen = flag;
        return savedstate;
    }

    protected void onSizeChanged(int i, int j, int k, int l)
    {
        super.onSizeChanged(i, j, k, l);
        if(i != k)
            mFirstLayout = true;
    }

    public boolean onTouchEvent(MotionEvent motionevent)
    {
        if(mCanSlide) goto _L2; else goto _L1
_L1:
        boolean flag = super.onTouchEvent(motionevent);
_L4:
        return flag;
_L2:
        mDragHelper.processTouchEvent(motionevent);
        int i = motionevent.getAction();
        boolean flag1 = true;
        switch(i & 0xff)
        {
        default:
            flag = flag1;
            break;

        case 0: // '\0'
            float f = motionevent.getX();
            float f2 = motionevent.getY();
            mInitialMotionX = f;
            mInitialMotionY = f2;
            flag = flag1;
            break;

        case 1: // '\001'
            flag = flag1;
            if(isDimmed(mSlideableView))
            {
                float f1 = motionevent.getX();
                float f4 = motionevent.getY();
                float f5 = f1 - mInitialMotionX;
                float f3 = f4 - mInitialMotionY;
                int j = mDragHelper.getTouchSlop();
                flag = flag1;
                if(f5 * f5 + f3 * f3 < (float)(j * j))
                {
                    flag = flag1;
                    if(mDragHelper.isViewUnder(mSlideableView, (int)f1, (int)f4))
                    {
                        closePane(mSlideableView, 0);
                        flag = flag1;
                    }
                }
            }
            break;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public boolean openPane()
    {
        return openPane(mSlideableView, 0);
    }

    public void requestChildFocus(View view, View view1)
    {
        super.requestChildFocus(view, view1);
        if(!isInTouchMode() && !mCanSlide)
        {
            boolean flag;
            if(view == mSlideableView)
                flag = true;
            else
                flag = false;
            mPreservedOpenState = flag;
        }
    }

    void setAllChildrenVisible()
    {
        int i = 0;
        for(int j = getChildCount(); i < j; i++)
        {
            View view = getChildAt(i);
            if(view.getVisibility() == 4)
                view.setVisibility(0);
        }

    }

    public void setCoveredFadeColor(int i)
    {
        mCoveredFadeColor = i;
    }

    public void setPanelSlideListener(PanelSlideListener panelslidelistener)
    {
        mPanelSlideListener = panelslidelistener;
    }

    public void setParallaxDistance(int i)
    {
        mParallaxBy = i;
        requestLayout();
    }

    public void setShadowDrawable(Drawable drawable)
    {
        setShadowDrawableLeft(drawable);
    }

    public void setShadowDrawableLeft(Drawable drawable)
    {
        mShadowDrawableLeft = drawable;
    }

    public void setShadowDrawableRight(Drawable drawable)
    {
        mShadowDrawableRight = drawable;
    }

    public void setShadowResource(int i)
    {
        setShadowDrawable(getResources().getDrawable(i));
    }

    public void setShadowResourceLeft(int i)
    {
        setShadowDrawableLeft(getResources().getDrawable(i));
    }

    public void setShadowResourceRight(int i)
    {
        setShadowDrawableRight(getResources().getDrawable(i));
    }

    public void setSliderFadeColor(int i)
    {
        mSliderFadeColor = i;
    }

    public void smoothSlideClosed()
    {
        closePane();
    }

    public void smoothSlideOpen()
    {
        openPane();
    }

    boolean smoothSlideTo(float f, int i)
    {
        boolean flag = false;
        if(mCanSlide) goto _L2; else goto _L1
_L1:
        return flag;
_L2:
        boolean flag1 = isLayoutRtlSupport();
        LayoutParams layoutparams = (LayoutParams)mSlideableView.getLayoutParams();
        if(flag1)
        {
            int j = getPaddingRight();
            int k = layoutparams.rightMargin;
            i = mSlideableView.getWidth();
            i = (int)((float)getWidth() - ((float)(j + k) + (float)mSlideRange * f + (float)i));
        } else
        {
            i = (int)((float)(getPaddingLeft() + layoutparams.leftMargin) + (float)mSlideRange * f);
        }
        if(mDragHelper.smoothSlideViewTo(mSlideableView, i, mSlideableView.getTop()))
        {
            setAllChildrenVisible();
            ViewCompat.postInvalidateOnAnimation(this);
            flag = true;
        }
        if(true) goto _L1; else goto _L3
_L3:
    }

    void updateObscuredViewsVisibility(View view)
    {
        boolean flag = isLayoutRtlSupport();
        int i;
        int j;
        int k;
        int l;
        int i1;
        int j1;
        int k1;
        int l1;
        int i2;
        int j2;
        int k2;
        if(flag)
            i = getWidth() - getPaddingRight();
        else
            i = getPaddingLeft();
        if(flag)
            j = getPaddingLeft();
        else
            j = getWidth() - getPaddingRight();
        k = getPaddingTop();
        l = getHeight();
        i1 = getPaddingBottom();
        if(view != null && viewIsOpaque(view))
        {
            j1 = view.getLeft();
            k1 = view.getRight();
            l1 = view.getTop();
            i2 = view.getBottom();
        } else
        {
            i2 = 0;
            l1 = 0;
            k1 = 0;
            j1 = 0;
        }
        j2 = 0;
        k2 = getChildCount();
        do
        {
            View view1;
label0:
            {
                if(j2 < k2)
                {
                    view1 = getChildAt(j2);
                    if(view1 != view)
                        break label0;
                }
                return;
            }
            int l2;
            int i3;
            int j3;
            int k3;
            if(flag)
                l2 = j;
            else
                l2 = i;
            i3 = Math.max(l2, view1.getLeft());
            j3 = Math.max(k, view1.getTop());
            if(flag)
                l2 = i;
            else
                l2 = j;
            k3 = Math.min(l2, view1.getRight());
            l2 = Math.min(l - i1, view1.getBottom());
            if(i3 >= j1 && j3 >= l1 && k3 <= k1 && l2 <= i2)
                l2 = 4;
            else
                l2 = 0;
            view1.setVisibility(l2);
            j2++;
        } while(true);
    }

    private static final int DEFAULT_FADE_COLOR = 0xcccccccc;
    private static final int DEFAULT_OVERHANG_SIZE = 32;
    static final SlidingPanelLayoutImpl IMPL;
    private static final int MIN_FLING_VELOCITY = 400;
    private static final String TAG = "SlidingPaneLayout";
    private boolean mCanSlide;
    private int mCoveredFadeColor;
    private final ViewDragHelper mDragHelper;
    private boolean mFirstLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private boolean mIsUnableToDrag;
    private final int mOverhangSize;
    private PanelSlideListener mPanelSlideListener;
    private int mParallaxBy;
    private float mParallaxOffset;
    private final ArrayList mPostedRunnables;
    private boolean mPreservedOpenState;
    private Drawable mShadowDrawableLeft;
    private Drawable mShadowDrawableRight;
    private float mSlideOffset;
    private int mSlideRange;
    private View mSlideableView;
    private int mSliderFadeColor;
    private final Rect mTmpRect;

    static 
    {
        int i = android.os.Build.VERSION.SDK_INT;
        if(i >= 17)
            IMPL = new SlidingPanelLayoutImplJBMR1();
        else
        if(i >= 16)
            IMPL = new SlidingPanelLayoutImplJB();
        else
            IMPL = new SlidingPanelLayoutImplBase();
    }








/*
    static boolean access$502(SlidingPaneLayout slidingpanelayout, boolean flag)
    {
        slidingpanelayout.mPreservedOpenState = flag;
        return flag;
    }

*/



}
