// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.*;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.*;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.*;
import android.view.accessibility.AccessibilityEvent;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package android.support.v4.widget:
//            DrawerLayoutImpl, ViewDragHelper, DrawerLayoutCompatApi21

public class DrawerLayout extends ViewGroup
    implements DrawerLayoutImpl
{
    class AccessibilityDelegate extends AccessibilityDelegateCompat
    {

        private void addChildrenForAccessibility(AccessibilityNodeInfoCompat accessibilitynodeinfocompat, ViewGroup viewgroup)
        {
            int i = viewgroup.getChildCount();
            for(int j = 0; j < i; j++)
            {
                View view = viewgroup.getChildAt(j);
                if(DrawerLayout.includeChildForAccessibility(view))
                    accessibilitynodeinfocompat.addChild(view);
            }

        }

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
        }

        public boolean dispatchPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityevent)
        {
            boolean flag;
            if(accessibilityevent.getEventType() == 32)
            {
                view = accessibilityevent.getText();
                accessibilityevent = findVisibleDrawer();
                if(accessibilityevent != null)
                {
                    int i = getDrawerViewAbsoluteGravity(accessibilityevent);
                    accessibilityevent = getDrawerTitle(i);
                    if(accessibilityevent != null)
                        view.add(accessibilityevent);
                }
                flag = true;
            } else
            {
                flag = super.dispatchPopulateAccessibilityEvent(view, accessibilityevent);
            }
            return flag;
        }

        public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityevent)
        {
            super.onInitializeAccessibilityEvent(view, accessibilityevent);
            accessibilityevent.setClassName(android/support/v4/widget/DrawerLayout.getName());
        }

        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilitynodeinfocompat)
        {
            if(DrawerLayout.CAN_HIDE_DESCENDANTS)
            {
                super.onInitializeAccessibilityNodeInfo(view, accessibilitynodeinfocompat);
            } else
            {
                AccessibilityNodeInfoCompat accessibilitynodeinfocompat1 = AccessibilityNodeInfoCompat.obtain(accessibilitynodeinfocompat);
                super.onInitializeAccessibilityNodeInfo(view, accessibilitynodeinfocompat1);
                accessibilitynodeinfocompat.setSource(view);
                android.view.ViewParent viewparent = ViewCompat.getParentForAccessibility(view);
                if(viewparent instanceof View)
                    accessibilitynodeinfocompat.setParent((View)viewparent);
                copyNodeInfoNoChildren(accessibilitynodeinfocompat, accessibilitynodeinfocompat1);
                accessibilitynodeinfocompat1.recycle();
                addChildrenForAccessibility(accessibilitynodeinfocompat, (ViewGroup)view);
            }
            accessibilitynodeinfocompat.setClassName(android/support/v4/widget/DrawerLayout.getName());
            accessibilitynodeinfocompat.setFocusable(false);
            accessibilitynodeinfocompat.setFocused(false);
            accessibilitynodeinfocompat.removeAction(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_FOCUS);
            accessibilitynodeinfocompat.removeAction(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLEAR_FOCUS);
        }

        public boolean onRequestSendAccessibilityEvent(ViewGroup viewgroup, View view, AccessibilityEvent accessibilityevent)
        {
            boolean flag;
            if(DrawerLayout.CAN_HIDE_DESCENDANTS || DrawerLayout.includeChildForAccessibility(view))
                flag = super.onRequestSendAccessibilityEvent(viewgroup, view, accessibilityevent);
            else
                flag = false;
            return flag;
        }

        private final Rect mTmpRect = new Rect();
        final DrawerLayout this$0;

        AccessibilityDelegate()
        {
            this$0 = DrawerLayout.this;
            super();
        }
    }

    final class ChildAccessibilityDelegate extends AccessibilityDelegateCompat
    {

        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilitynodeinfocompat)
        {
            super.onInitializeAccessibilityNodeInfo(view, accessibilitynodeinfocompat);
            if(!DrawerLayout.includeChildForAccessibility(view))
                accessibilitynodeinfocompat.setParent(null);
        }

        final DrawerLayout this$0;

        ChildAccessibilityDelegate()
        {
            this$0 = DrawerLayout.this;
            super();
        }
    }

    static interface DrawerLayoutCompatImpl
    {

        public abstract void applyMarginInsets(android.view.ViewGroup.MarginLayoutParams marginlayoutparams, Object obj, int i);

        public abstract void configureApplyInsets(View view);

        public abstract void dispatchChildInsets(View view, Object obj, int i);

        public abstract Drawable getDefaultStatusBarBackground(Context context);

        public abstract int getTopInset(Object obj);
    }

    static class DrawerLayoutCompatImplApi21
        implements DrawerLayoutCompatImpl
    {

        public void applyMarginInsets(android.view.ViewGroup.MarginLayoutParams marginlayoutparams, Object obj, int i)
        {
            DrawerLayoutCompatApi21.applyMarginInsets(marginlayoutparams, obj, i);
        }

        public void configureApplyInsets(View view)
        {
            DrawerLayoutCompatApi21.configureApplyInsets(view);
        }

        public void dispatchChildInsets(View view, Object obj, int i)
        {
            DrawerLayoutCompatApi21.dispatchChildInsets(view, obj, i);
        }

        public Drawable getDefaultStatusBarBackground(Context context)
        {
            return DrawerLayoutCompatApi21.getDefaultStatusBarBackground(context);
        }

        public int getTopInset(Object obj)
        {
            return DrawerLayoutCompatApi21.getTopInset(obj);
        }

        DrawerLayoutCompatImplApi21()
        {
        }
    }

    static class DrawerLayoutCompatImplBase
        implements DrawerLayoutCompatImpl
    {

        public void applyMarginInsets(android.view.ViewGroup.MarginLayoutParams marginlayoutparams, Object obj, int i)
        {
        }

        public void configureApplyInsets(View view)
        {
        }

        public void dispatchChildInsets(View view, Object obj, int i)
        {
        }

        public Drawable getDefaultStatusBarBackground(Context context)
        {
            return null;
        }

        public int getTopInset(Object obj)
        {
            return 0;
        }

        DrawerLayoutCompatImplBase()
        {
        }
    }

    public static interface DrawerListener
    {

        public abstract void onDrawerClosed(View view);

        public abstract void onDrawerOpened(View view);

        public abstract void onDrawerSlide(View view, float f);

        public abstract void onDrawerStateChanged(int i);
    }

    private static interface EdgeGravity
        extends Annotation
    {
    }

    public static class LayoutParams extends android.view.ViewGroup.MarginLayoutParams
    {

        private static final int FLAG_IS_CLOSING = 4;
        private static final int FLAG_IS_OPENED = 1;
        private static final int FLAG_IS_OPENING = 2;
        public int gravity;
        private boolean isPeeking;
        private float onScreen;
        private int openState;



/*
        static float access$002(LayoutParams layoutparams, float f)
        {
            layoutparams.onScreen = f;
            return f;
        }

*/



/*
        static int access$102(LayoutParams layoutparams, int i)
        {
            layoutparams.openState = i;
            return i;
        }

*/


/*
        static int access$176(LayoutParams layoutparams, int i)
        {
            i = layoutparams.openState | i;
            layoutparams.openState = i;
            return i;
        }

*/



/*
        static boolean access$202(LayoutParams layoutparams, boolean flag)
        {
            layoutparams.isPeeking = flag;
            return flag;
        }

*/

        public LayoutParams(int i, int j)
        {
            super(i, j);
            gravity = 0;
        }

        public LayoutParams(int i, int j, int k)
        {
            this(i, j);
            gravity = k;
        }

        public LayoutParams(Context context, AttributeSet attributeset)
        {
            super(context, attributeset);
            gravity = 0;
            context = context.obtainStyledAttributes(attributeset, DrawerLayout.LAYOUT_ATTRS);
            gravity = context.getInt(0, 0);
            context.recycle();
        }

        public LayoutParams(LayoutParams layoutparams)
        {
            super(layoutparams);
            gravity = 0;
            gravity = layoutparams.gravity;
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams layoutparams)
        {
            super(layoutparams);
            gravity = 0;
        }

        public LayoutParams(android.view.ViewGroup.MarginLayoutParams marginlayoutparams)
        {
            super(marginlayoutparams);
            gravity = 0;
        }
    }

    private static interface LockMode
        extends Annotation
    {
    }

    protected static class SavedState extends android.view.View.BaseSavedState
    {

        public void writeToParcel(Parcel parcel, int i)
        {
            super.writeToParcel(parcel, i);
            parcel.writeInt(openDrawerGravity);
            parcel.writeInt(lockModeLeft);
            parcel.writeInt(lockModeRight);
            parcel.writeInt(lockModeStart);
            parcel.writeInt(lockModeEnd);
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
        int lockModeEnd;
        int lockModeLeft;
        int lockModeRight;
        int lockModeStart;
        int openDrawerGravity;


        public SavedState(Parcel parcel)
        {
            super(parcel);
            openDrawerGravity = 0;
            openDrawerGravity = parcel.readInt();
            lockModeLeft = parcel.readInt();
            lockModeRight = parcel.readInt();
            lockModeStart = parcel.readInt();
            lockModeEnd = parcel.readInt();
        }

        public SavedState(Parcelable parcelable)
        {
            super(parcelable);
            openDrawerGravity = 0;
        }
    }

    public static abstract class SimpleDrawerListener
        implements DrawerListener
    {

        public void onDrawerClosed(View view)
        {
        }

        public void onDrawerOpened(View view)
        {
        }

        public void onDrawerSlide(View view, float f)
        {
        }

        public void onDrawerStateChanged(int i)
        {
        }

        public SimpleDrawerListener()
        {
        }
    }

    private static interface State
        extends Annotation
    {
    }

    private class ViewDragCallback extends ViewDragHelper.Callback
    {

        private void closeOtherDrawer()
        {
            byte byte0 = 3;
            if(mAbsGravity == 3)
                byte0 = 5;
            View view = findDrawerWithGravity(byte0);
            if(view != null)
                closeDrawer(view);
        }

        private void peekDrawer()
        {
            int i = 0;
            int j = mDragger.getEdgeSize();
            boolean flag;
            View view;
            if(mAbsGravity == 3)
                flag = true;
            else
                flag = false;
            if(flag)
            {
                view = findDrawerWithGravity(3);
                if(view != null)
                    i = -view.getWidth();
                i += j;
            } else
            {
                view = findDrawerWithGravity(5);
                i = getWidth() - j;
            }
            if(view != null && (flag && view.getLeft() < i || !flag && view.getLeft() > i) && getDrawerLockMode(view) == 0)
            {
                LayoutParams layoutparams = (LayoutParams)view.getLayoutParams();
                mDragger.smoothSlideViewTo(view, i, view.getTop());
                layoutparams.isPeeking = true;
                invalidate();
                closeOtherDrawer();
                cancelChildViewTouch();
            }
        }

        public int clampViewPositionHorizontal(View view, int i, int j)
        {
            if(checkDrawerViewAbsoluteGravity(view, 3))
            {
                i = Math.max(-view.getWidth(), Math.min(i, 0));
            } else
            {
                j = getWidth();
                i = Math.max(j - view.getWidth(), Math.min(i, j));
            }
            return i;
        }

        public int clampViewPositionVertical(View view, int i, int j)
        {
            return view.getTop();
        }

        public int getViewHorizontalDragRange(View view)
        {
            int i;
            if(isDrawerView(view))
                i = view.getWidth();
            else
                i = 0;
            return i;
        }

        public void onEdgeDragStarted(int i, int j)
        {
            View view;
            if((i & 1) == 1)
                view = findDrawerWithGravity(3);
            else
                view = findDrawerWithGravity(5);
            if(view != null && getDrawerLockMode(view) == 0)
                mDragger.captureChildView(view, j);
        }

        public boolean onEdgeLock(int i)
        {
            return false;
        }

        public void onEdgeTouched(int i, int j)
        {
            postDelayed(mPeekRunnable, 160L);
        }

        public void onViewCaptured(View view, int i)
        {
            ((LayoutParams)view.getLayoutParams()).isPeeking = false;
            closeOtherDrawer();
        }

        public void onViewDragStateChanged(int i)
        {
            updateDrawerState(mAbsGravity, i, mDragger.getCapturedView());
        }

        public void onViewPositionChanged(View view, int i, int j, int k, int l)
        {
            j = view.getWidth();
            float f;
            if(checkDrawerViewAbsoluteGravity(view, 3))
                f = (float)(j + i) / (float)j;
            else
                f = (float)(getWidth() - i) / (float)j;
            setDrawerViewOffset(view, f);
            if(f == 0.0F)
                i = 4;
            else
                i = 0;
            view.setVisibility(i);
            invalidate();
        }

        public void onViewReleased(View view, float f, float f1)
        {
            int i;
            f1 = getDrawerViewOffset(view);
            i = view.getWidth();
            if(!checkDrawerViewAbsoluteGravity(view, 3)) goto _L2; else goto _L1
_L1:
            int j;
            if(f > 0.0F || f == 0.0F && f1 > 0.5F)
                j = 0;
            else
                j = -i;
_L4:
            mDragger.settleCapturedViewAt(j, view.getTop());
            invalidate();
            return;
_L2:
            j = getWidth();
            if(f < 0.0F || f == 0.0F && f1 > 0.5F)
                j -= i;
            if(true) goto _L4; else goto _L3
_L3:
        }

        public void removeCallbacks()
        {
            DrawerLayout.this.removeCallbacks(mPeekRunnable);
        }

        public void setDragger(ViewDragHelper viewdraghelper)
        {
            mDragger = viewdraghelper;
        }

        public boolean tryCaptureView(View view, int i)
        {
            boolean flag;
            if(isDrawerView(view) && checkDrawerViewAbsoluteGravity(view, mAbsGravity) && getDrawerLockMode(view) == 0)
                flag = true;
            else
                flag = false;
            return flag;
        }

        private final int mAbsGravity;
        private ViewDragHelper mDragger;
        private final Runnable mPeekRunnable = new _cls1();
        final DrawerLayout this$0;


        public ViewDragCallback(int i)
        {
            this$0 = DrawerLayout.this;
            super();
            mAbsGravity = i;
        }
    }


    public DrawerLayout(Context context)
    {
        this(context, null);
    }

    public DrawerLayout(Context context, AttributeSet attributeset)
    {
        this(context, attributeset, 0);
    }

    public DrawerLayout(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        mChildAccessibilityDelegate = new ChildAccessibilityDelegate();
        mScrimColor = 0x99000000;
        mScrimPaint = new Paint();
        mFirstLayout = true;
        mLockModeLeft = 3;
        mLockModeRight = 3;
        mLockModeStart = 3;
        mLockModeEnd = 3;
        mShadowStart = null;
        mShadowEnd = null;
        mShadowLeft = null;
        mShadowRight = null;
        setDescendantFocusability(0x40000);
        float f = getResources().getDisplayMetrics().density;
        mMinDrawerMargin = (int)(64F * f + 0.5F);
        float f1 = 400F * f;
        mLeftCallback = new ViewDragCallback(3);
        mRightCallback = new ViewDragCallback(5);
        mLeftDragger = ViewDragHelper.create(this, 1.0F, mLeftCallback);
        mLeftDragger.setEdgeTrackingEnabled(1);
        mLeftDragger.setMinVelocity(f1);
        mLeftCallback.setDragger(mLeftDragger);
        mRightDragger = ViewDragHelper.create(this, 1.0F, mRightCallback);
        mRightDragger.setEdgeTrackingEnabled(2);
        mRightDragger.setMinVelocity(f1);
        mRightCallback.setDragger(mRightDragger);
        setFocusableInTouchMode(true);
        ViewCompat.setImportantForAccessibility(this, 1);
        ViewCompat.setAccessibilityDelegate(this, new AccessibilityDelegate());
        ViewGroupCompat.setMotionEventSplittingEnabled(this, false);
        if(ViewCompat.getFitsSystemWindows(this))
        {
            IMPL.configureApplyInsets(this);
            mStatusBarBackground = IMPL.getDefaultStatusBarBackground(context);
        }
        mDrawerElevation = 10F * f;
        mNonDrawerViews = new ArrayList();
    }

    private View findVisibleDrawer()
    {
        int i;
        int j;
        i = getChildCount();
        j = 0;
_L3:
        View view;
        if(j >= i)
            break MISSING_BLOCK_LABEL_42;
        view = getChildAt(j);
        if(!isDrawerView(view) || !isDrawerVisible(view)) goto _L2; else goto _L1
_L1:
        return view;
_L2:
        j++;
          goto _L3
        view = null;
          goto _L1
    }

    static String gravityToString(int i)
    {
        String s;
        if((i & 3) == 3)
            s = "LEFT";
        else
        if((i & 5) == 5)
            s = "RIGHT";
        else
            s = Integer.toHexString(i);
        return s;
    }

    private static boolean hasOpaqueBackground(View view)
    {
        boolean flag = false;
        view = view.getBackground();
        boolean flag1 = flag;
        if(view != null)
        {
            flag1 = flag;
            if(view.getOpacity() == -1)
                flag1 = true;
        }
        return flag1;
    }

    private boolean hasPeekingDrawer()
    {
        int i;
        int j;
        i = getChildCount();
        j = 0;
_L3:
        if(j >= i)
            break MISSING_BLOCK_LABEL_39;
        if(!((LayoutParams)getChildAt(j).getLayoutParams()).isPeeking) goto _L2; else goto _L1
_L1:
        boolean flag = true;
_L4:
        return flag;
_L2:
        j++;
          goto _L3
        flag = false;
          goto _L4
    }

    private boolean hasVisibleDrawer()
    {
        boolean flag;
        if(findVisibleDrawer() != null)
            flag = true;
        else
            flag = false;
        return flag;
    }

    private static boolean includeChildForAccessibility(View view)
    {
        boolean flag;
        if(ViewCompat.getImportantForAccessibility(view) != 4 && ViewCompat.getImportantForAccessibility(view) != 2)
            flag = true;
        else
            flag = false;
        return flag;
    }

    private boolean mirror(Drawable drawable, int i)
    {
        boolean flag;
        if(drawable == null || !DrawableCompat.isAutoMirrored(drawable))
        {
            flag = false;
        } else
        {
            DrawableCompat.setLayoutDirection(drawable, i);
            flag = true;
        }
        return flag;
    }

    private Drawable resolveLeftShadow()
    {
        int i = ViewCompat.getLayoutDirection(this);
        if(i != 0) goto _L2; else goto _L1
_L1:
        if(mShadowStart == null) goto _L4; else goto _L3
_L3:
        Drawable drawable;
        mirror(mShadowStart, i);
        drawable = mShadowStart;
_L6:
        return drawable;
_L2:
        if(mShadowEnd != null)
        {
            mirror(mShadowEnd, i);
            drawable = mShadowEnd;
            continue; /* Loop/switch isn't completed */
        }
_L4:
        drawable = mShadowLeft;
        if(true) goto _L6; else goto _L5
_L5:
    }

    private Drawable resolveRightShadow()
    {
        int i = ViewCompat.getLayoutDirection(this);
        if(i != 0) goto _L2; else goto _L1
_L1:
        if(mShadowEnd == null) goto _L4; else goto _L3
_L3:
        Drawable drawable;
        mirror(mShadowEnd, i);
        drawable = mShadowEnd;
_L6:
        return drawable;
_L2:
        if(mShadowStart != null)
        {
            mirror(mShadowStart, i);
            drawable = mShadowStart;
            continue; /* Loop/switch isn't completed */
        }
_L4:
        drawable = mShadowRight;
        if(true) goto _L6; else goto _L5
_L5:
    }

    private void resolveShadowDrawables()
    {
        if(!SET_DRAWER_SHADOW_FROM_ELEVATION)
        {
            mShadowLeftResolved = resolveLeftShadow();
            mShadowRightResolved = resolveRightShadow();
        }
    }

    private void updateChildrenImportantForAccessibility(View view, boolean flag)
    {
        int i = getChildCount();
        int j = 0;
        while(j < i) 
        {
            View view1 = getChildAt(j);
            if(!flag && !isDrawerView(view1) || flag && view1 == view)
                ViewCompat.setImportantForAccessibility(view1, 1);
            else
                ViewCompat.setImportantForAccessibility(view1, 4);
            j++;
        }
    }

    public void addDrawerListener(DrawerListener drawerlistener)
    {
        if(drawerlistener != null)
        {
            if(mListeners == null)
                mListeners = new ArrayList();
            mListeners.add(drawerlistener);
        }
    }

    public void addFocusables(ArrayList arraylist, int i, int j)
    {
        if(getDescendantFocusability() != 0x60000)
        {
            int k = getChildCount();
            boolean flag = false;
            int i1 = 0;
            while(i1 < k) 
            {
                View view = getChildAt(i1);
                if(isDrawerView(view))
                {
                    if(isDrawerOpen(view))
                    {
                        flag = true;
                        view.addFocusables(arraylist, i, j);
                    }
                } else
                {
                    mNonDrawerViews.add(view);
                }
                i1++;
            }
            if(!flag)
            {
                int l = mNonDrawerViews.size();
                for(int j1 = 0; j1 < l; j1++)
                {
                    View view1 = (View)mNonDrawerViews.get(j1);
                    if(view1.getVisibility() == 0)
                        view1.addFocusables(arraylist, i, j);
                }

            }
            mNonDrawerViews.clear();
        }
    }

    public void addView(View view, int i, android.view.ViewGroup.LayoutParams layoutparams)
    {
        super.addView(view, i, layoutparams);
        if(findOpenDrawer() != null || isDrawerView(view))
            ViewCompat.setImportantForAccessibility(view, 4);
        else
            ViewCompat.setImportantForAccessibility(view, 1);
        if(!CAN_HIDE_DESCENDANTS)
            ViewCompat.setAccessibilityDelegate(view, mChildAccessibilityDelegate);
    }

    void cancelChildViewTouch()
    {
        if(!mChildrenCanceledTouch)
        {
            long l = SystemClock.uptimeMillis();
            MotionEvent motionevent = MotionEvent.obtain(l, l, 3, 0.0F, 0.0F, 0);
            int i = getChildCount();
            for(int j = 0; j < i; j++)
                getChildAt(j).dispatchTouchEvent(motionevent);

            motionevent.recycle();
            mChildrenCanceledTouch = true;
        }
    }

    boolean checkDrawerViewAbsoluteGravity(View view, int i)
    {
        boolean flag;
        if((getDrawerViewAbsoluteGravity(view) & i) == i)
            flag = true;
        else
            flag = false;
        return flag;
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

    public void closeDrawer(int i)
    {
        View view = findDrawerWithGravity(i);
        if(view == null)
        {
            throw new IllegalArgumentException((new StringBuilder()).append("No drawer view found with gravity ").append(gravityToString(i)).toString());
        } else
        {
            closeDrawer(view);
            return;
        }
    }

    public void closeDrawer(View view)
    {
        if(!isDrawerView(view))
            throw new IllegalArgumentException((new StringBuilder()).append("View ").append(view).append(" is not a sliding drawer").toString());
        LayoutParams layoutparams = (LayoutParams)view.getLayoutParams();
        if(mFirstLayout)
        {
            layoutparams.onScreen = 0.0F;
            layoutparams.openState = 0;
        } else
        {
            view = 
// JavaClassFileOutputException: get_constant: invalid tag

    public void closeDrawers()
    {
        closeDrawers(false);
    }

    void closeDrawers(boolean flag)
    {
        boolean flag1 = false;
        int i = getChildCount();
        int j = 0;
        while(j < i) 
        {
            View view = getChildAt(j);
            LayoutParams layoutparams = (LayoutParams)view.getLayoutParams();
            int k = ((flag1) ? 1 : 0);
            if(isDrawerView(view))
                if(flag && !layoutparams.isPeeking)
                {
                    k = ((flag1) ? 1 : 0);
                } else
                {
                    k = view.getWidth();
                    if(checkDrawerViewAbsoluteGravity(view, 3))
                        flag1 |= mLeftDragger.smoothSlideViewTo(view, -k, view.getTop());
                    else
                        flag1 |= mRightDragger.smoothSlideViewTo(view, getWidth(), view.getTop());
                    layoutparams.isPeeking = false;
                    k = ((flag1) ? 1 : 0);
                }
            j++;
            flag1 = k;
        }
        mLeftCallback.removeCallbacks();
        mRightCallback.removeCallbacks();
        if(flag1)
            invalidate();
    }

    public void computeScroll()
    {
        int i = getChildCount();
        float f = 0.0F;
        for(int j = 0; j < i; j++)
            f = Math.max(f, ((LayoutParams)getChildAt(j).getLayoutParams()).onScreen);

        mScrimOpacity = f;
        if(mLeftDragger.continueSettling(true) | mRightDragger.continueSettling(true))
            ViewCompat.postInvalidateOnAnimation(this);
    }

    void dispatchOnDrawerClosed(View view)
    {
        LayoutParams layoutparams = (LayoutParams)view.getLayoutParams();
        if((layoutparams.openState & 1) == 1)
        {
            layoutparams.openState = 0;
            if(mListeners != null)
            {
                for(int i = mListeners.size() - 1; i >= 0; i--)
                    ((DrawerListener)mListeners.get(i)).onDrawerClosed(view);

            }
            updateChildrenImportantForAccessibility(view, false);
            if(hasWindowFocus())
            {
                view = getRootView();
                if(view != null)
                    view.sendAccessibilityEvent(32);
            }
        }
    }

    void dispatchOnDrawerOpened(View view)
    {
        LayoutParams layoutparams = (LayoutParams)view.getLayoutParams();
        if((layoutparams.openState & 1) == 0)
        {
            layoutparams.openState = 1;
            if(mListeners != null)
            {
                for(int i = mListeners.size() - 1; i >= 0; i--)
                    ((DrawerListener)mListeners.get(i)).onDrawerOpened(view);

            }
            updateChildrenImportantForAccessibility(view, true);
            if(hasWindowFocus())
                sendAccessibilityEvent(32);
            view.requestFocus();
        }
    }

    void dispatchOnDrawerSlide(View view, float f)
    {
        if(mListeners != null)
        {
            for(int i = mListeners.size() - 1; i >= 0; i--)
                ((DrawerListener)mListeners.get(i)).onDrawerSlide(view, f);

        }
    }

    protected boolean drawChild(Canvas canvas, View view, long l)
    {
        boolean flag;
        int j;
        int j3;
        boolean flag2;
        int i = getHeight();
        flag = isContentView(view);
        j = 0;
        boolean flag1 = false;
        int i2 = getWidth();
        int i3 = canvas.save();
        j3 = i2;
        if(flag)
        {
            int i4 = getChildCount();
            j3 = 0;
            j = ((flag1) ? 1 : 0);
            while(j3 < i4) 
            {
                View view1 = getChildAt(j3);
                int j1 = j;
                int j4 = i2;
                if(view1 != view)
                {
                    j1 = j;
                    j4 = i2;
                    if(view1.getVisibility() == 0)
                    {
                        j1 = j;
                        j4 = i2;
                        if(hasOpaqueBackground(view1))
                        {
                            j1 = j;
                            j4 = i2;
                            if(isDrawerView(view1))
                                if(view1.getHeight() < i)
                                {
                                    j4 = i2;
                                    j1 = j;
                                } else
                                if(checkDrawerViewAbsoluteGravity(view1, 3))
                                {
                                    int k4 = view1.getRight();
                                    j1 = j;
                                    j4 = i2;
                                    if(k4 > j)
                                    {
                                        j1 = k4;
                                        j4 = i2;
                                    }
                                } else
                                {
                                    int l4 = view1.getLeft();
                                    j1 = j;
                                    j4 = i2;
                                    if(l4 < i2)
                                    {
                                        j4 = l4;
                                        j1 = j;
                                    }
                                }
                        }
                    }
                }
                j3++;
                j = j1;
                i2 = j4;
            }
            canvas.clipRect(j, 0, i2, getHeight());
            j3 = i2;
        }
        flag2 = drawChild(canvas, view, l);
        canvas.restoreToCount(i3);
        if(mScrimOpacity <= 0.0F || !flag) goto _L2; else goto _L1
_L1:
        int k1 = (int)((float)((mScrimColor & 0xff000000) >>> 24) * mScrimOpacity);
        int j2 = mScrimColor;
        mScrimPaint.setColor(k1 << 24 | j2 & 0xffffff);
        canvas.drawRect(j, 0.0F, j3, getHeight(), mScrimPaint);
_L4:
        return flag2;
_L2:
        if(mShadowLeftResolved != null && checkDrawerViewAbsoluteGravity(view, 3))
        {
            int k3 = mShadowLeftResolved.getIntrinsicWidth();
            int k = view.getRight();
            int k2 = mLeftDragger.getEdgeSize();
            float f = Math.max(0.0F, Math.min((float)k / (float)k2, 1.0F));
            mShadowLeftResolved.setBounds(k, view.getTop(), k + k3, view.getBottom());
            mShadowLeftResolved.setAlpha((int)(255F * f));
            mShadowLeftResolved.draw(canvas);
        } else
        if(mShadowRightResolved != null && checkDrawerViewAbsoluteGravity(view, 5))
        {
            int l1 = mShadowRightResolved.getIntrinsicWidth();
            int l2 = view.getLeft();
            int l3 = getWidth();
            int i1 = mRightDragger.getEdgeSize();
            float f1 = Math.max(0.0F, Math.min((float)(l3 - l2) / (float)i1, 1.0F));
            mShadowRightResolved.setBounds(l2 - l1, view.getTop(), l2, view.getBottom());
            mShadowRightResolved.setAlpha((int)(255F * f1));
            mShadowRightResolved.draw(canvas);
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    View findDrawerWithGravity(int i)
    {
        int j;
        int k;
        j = GravityCompat.getAbsoluteGravity(i, ViewCompat.getLayoutDirection(this));
        k = getChildCount();
        i = 0;
_L3:
        View view;
        if(i >= k)
            break MISSING_BLOCK_LABEL_53;
        view = getChildAt(i);
        if((getDrawerViewAbsoluteGravity(view) & 7) != (j & 7)) goto _L2; else goto _L1
_L1:
        return view;
_L2:
        i++;
          goto _L3
        view = null;
          goto _L1
    }

    View findOpenDrawer()
    {
        int i;
        int j;
        i = getChildCount();
        j = 0;
_L3:
        View view;
        if(j >= i)
            break MISSING_BLOCK_LABEL_42;
        view = getChildAt(j);
        if((((LayoutParams)view.getLayoutParams()).openState & 1) != 1) goto _L2; else goto _L1
_L1:
        return view;
_L2:
        j++;
          goto _L3
        view = null;
          goto _L1
    }

    protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams()
    {
        return new LayoutParams(-1, -1);
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeset)
    {
        return new LayoutParams(getContext(), attributeset);
    }

    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams layoutparams)
    {
        if(layoutparams instanceof LayoutParams)
            layoutparams = new LayoutParams((LayoutParams)layoutparams);
        else
        if(layoutparams instanceof android.view.ViewGroup.MarginLayoutParams)
            layoutparams = new LayoutParams((android.view.ViewGroup.MarginLayoutParams)layoutparams);
        else
            layoutparams = new LayoutParams(layoutparams);
        return layoutparams;
    }

    public float getDrawerElevation()
    {
        float f;
        if(SET_DRAWER_SHADOW_FROM_ELEVATION)
            f = mDrawerElevation;
        else
            f = 0.0F;
        return f;
    }

    public int getDrawerLockMode(int i)
    {
        int j = ViewCompat.getLayoutDirection(this);
        i;
        JVM INSTR lookupswitch 4: default 48
    //                   3: 52
    //                   5: 93
    //                   8388611: 134
    //                   8388613: 175;
           goto _L1 _L2 _L3 _L4 _L5
_L1:
        i = 0;
_L7:
        return i;
_L2:
        if(mLockModeLeft != 3)
        {
            i = mLockModeLeft;
        } else
        {
            if(j == 0)
                i = mLockModeStart;
            else
                i = mLockModeEnd;
            if(i == 3)
                break; /* Loop/switch isn't completed */
        }
        continue; /* Loop/switch isn't completed */
_L3:
        if(mLockModeRight != 3)
        {
            i = mLockModeRight;
        } else
        {
            if(j == 0)
                i = mLockModeEnd;
            else
                i = mLockModeStart;
            if(i == 3)
                break; /* Loop/switch isn't completed */
        }
        continue; /* Loop/switch isn't completed */
_L4:
        if(mLockModeStart != 3)
        {
            i = mLockModeStart;
        } else
        {
            if(j == 0)
                i = mLockModeLeft;
            else
                i = mLockModeRight;
            if(i == 3)
                break; /* Loop/switch isn't completed */
        }
        continue; /* Loop/switch isn't completed */
_L5:
        if(mLockModeEnd == 3)
            break; /* Loop/switch isn't completed */
        i = mLockModeEnd;
        if(true) goto _L7; else goto _L6
_L6:
        if(j == 0)
            i = mLockModeRight;
        else
            i = mLockModeLeft;
        if(i == 3) goto _L1; else goto _L7
    }

    public int getDrawerLockMode(View view)
    {
        if(!isDrawerView(view))
            throw new IllegalArgumentException((new StringBuilder()).append("View ").append(view).append(" is not a drawer").toString());
        else
            return getDrawerLockMode(((LayoutParams)view.getLayoutParams()).gravity);
    }

    public CharSequence getDrawerTitle(int i)
    {
        i = GravityCompat.getAbsoluteGravity(i, ViewCompat.getLayoutDirection(this));
        CharSequence charsequence;
        if(i == 3)
            charsequence = mTitleLeft;
        else
        if(i == 5)
            charsequence = mTitleRight;
        else
            charsequence = null;
        return charsequence;
    }

    int getDrawerViewAbsoluteGravity(View view)
    {
        return GravityCompat.getAbsoluteGravity(((LayoutParams)view.getLayoutParams()).gravity, ViewCompat.getLayoutDirection(this));
    }

    float getDrawerViewOffset(View view)
    {
        return ((LayoutParams)view.getLayoutParams()).onScreen;
    }

    public Drawable getStatusBarBackgroundDrawable()
    {
        return mStatusBarBackground;
    }

    boolean isContentView(View view)
    {
        boolean flag;
        if(((LayoutParams)view.getLayoutParams()).gravity == 0)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public boolean isDrawerOpen(int i)
    {
        View view = findDrawerWithGravity(i);
        boolean flag;
        if(view != null)
            flag = isDrawerOpen(view);
        else
            flag = false;
        return flag;
    }

    public boolean isDrawerOpen(View view)
    {
        boolean flag = true;
        if(!isDrawerView(view))
            throw new IllegalArgumentException((new StringBuilder()).append("View ").append(view).append(" is not a drawer").toString());
        if((((LayoutParams)view.getLayoutParams()).openState & 1) != 1)
            flag = false;
        return flag;
    }

    boolean isDrawerView(View view)
    {
        int i = GravityCompat.getAbsoluteGravity(((LayoutParams)view.getLayoutParams()).gravity, ViewCompat.getLayoutDirection(view));
        boolean flag;
        if((i & 3) != 0)
            flag = true;
        else
        if((i & 5) != 0)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public boolean isDrawerVisible(int i)
    {
        View view = findDrawerWithGravity(i);
        boolean flag;
        if(view != null)
            flag = isDrawerVisible(view);
        else
            flag = false;
        return flag;
    }

    public boolean isDrawerVisible(View view)
    {
        if(!isDrawerView(view))
            throw new IllegalArgumentException((new StringBuilder()).append("View ").append(view).append(" is not a drawer").toString());
        boolean flag;
        if(((LayoutParams)view.getLayoutParams()).onScreen > 0.0F)
            flag = true;
        else
            flag = false;
        return flag;
    }

    void moveDrawerToOffset(View view, float f)
    {
        float f1 = getDrawerViewOffset(view);
        int i = view.getWidth();
        int j = (int)((float)i * f1);
        j = (int)((float)i * f) - j;
        if(!checkDrawerViewAbsoluteGravity(view, 3))
            j = -j;
        view.offsetLeftAndRight(j);
        setDrawerViewOffset(view, f);
    }

    protected void onAttachedToWindow()
    {
        onAttachedToWindow();
        mFirstLayout = true;
    }

    protected void onDetachedFromWindow()
    {
        onDetachedFromWindow();
        mFirstLayout = true;
    }

    public void onDraw(Canvas canvas)
    {
        onDraw(canvas);
        if(mDrawStatusBarBackground && mStatusBarBackground != null)
        {
            int i = IMPL.getTopInset(mLastInsets);
            if(i > 0)
            {
                mStatusBarBackground.setBounds(0, 0, getWidth(), i);
                mStatusBarBackground.draw(canvas);
            }
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionevent)
    {
        boolean flag;
        int i;
        boolean flag2;
        boolean flag3;
        boolean flag4;
        boolean flag5;
        flag = false;
        i = MotionEventCompat.getActionMasked(motionevent);
        flag2 = mLeftDragger.shouldInterceptTouchEvent(motionevent);
        flag3 = mRightDragger.shouldInterceptTouchEvent(motionevent);
        flag4 = false;
        flag5 = false;
        i;
        JVM INSTR tableswitch 0 3: default 64
    //                   0 97
    //                   1 214
    //                   2 180
    //                   3 214;
           goto _L1 _L2 _L3 _L4 _L3
_L1:
        boolean flag1 = flag5;
_L6:
        if(flag2 | flag3 || flag1 || hasPeekingDrawer() || mChildrenCanceledTouch)
            flag = true;
        return flag;
_L2:
        float f = motionevent.getX();
        float f1 = motionevent.getY();
        mInitialMotionX = f;
        mInitialMotionY = f1;
        flag1 = flag4;
        if(mScrimOpacity > 0.0F)
        {
            motionevent = mLeftDragger.findTopChildUnder((int)f, (int)f1);
            flag1 = flag4;
            if(motionevent != null)
            {
                flag1 = flag4;
                if(isContentView(motionevent))
                    flag1 = true;
            }
        }
        mDisallowInterceptRequested = false;
        mChildrenCanceledTouch = false;
        continue; /* Loop/switch isn't completed */
_L4:
        flag1 = flag5;
        if(mLeftDragger.checkTouchSlop(3))
        {
            mLeftCallback.removeCallbacks();
            mRightCallback.removeCallbacks();
            flag1 = flag5;
        }
        continue; /* Loop/switch isn't completed */
_L3:
        closeDrawers(true);
        mDisallowInterceptRequested = false;
        mChildrenCanceledTouch = false;
        flag1 = flag5;
        if(true) goto _L6; else goto _L5
_L5:
    }

    public boolean onKeyDown(int i, KeyEvent keyevent)
    {
        boolean flag;
        if(i == 4 && hasVisibleDrawer())
        {
            KeyEventCompat.startTracking(keyevent);
            flag = true;
        } else
        {
            flag = onKeyDown(i, keyevent);
        }
        return flag;
    }

    public boolean onKeyUp(int i, KeyEvent keyevent)
    {
        boolean flag;
        if(i == 4)
        {
            keyevent = findVisibleDrawer();
            if(keyevent != null && getDrawerLockMode(keyevent) == 0)
                closeDrawers();
            if(keyevent != null)
                flag = true;
            else
                flag = false;
        } else
        {
            flag = onKeyUp(i, keyevent);
        }
        return flag;
    }

    protected void onLayout(boolean flag, int i, int j, int k, int l)
    {
        int i1;
        int j1;
        mInLayout = true;
        i1 = k - i;
        j1 = getChildCount();
        k = 0;
_L2:
        View view;
        if(k >= j1)
            break MISSING_BLOCK_LABEL_446;
        view = getChildAt(k);
        if(view.getVisibility() != 8)
            break; /* Loop/switch isn't completed */
_L3:
        k++;
        if(true) goto _L2; else goto _L1
_L1:
        LayoutParams layoutparams;
label0:
        {
            layoutparams = (LayoutParams)view.getLayoutParams();
            if(!isContentView(view))
                break label0;
            view.layout(layoutparams.leftMargin, layoutparams.topMargin, layoutparams.leftMargin + view.getMeasuredWidth(), layoutparams.topMargin + view.getMeasuredHeight());
        }
          goto _L3
        int k1;
        int l1;
        int i2;
        k1 = view.getMeasuredWidth();
        l1 = view.getMeasuredHeight();
        float f;
        boolean flag1;
        if(checkDrawerViewAbsoluteGravity(view, 3))
        {
            i2 = -k1 + (int)((float)k1 * layoutparams.onScreen);
            f = (float)(k1 + i2) / (float)k1;
        } else
        {
            i2 = i1 - (int)((float)k1 * layoutparams.onScreen);
            f = (float)(i1 - i2) / (float)k1;
        }
        if(f != layoutparams.onScreen)
            flag1 = true;
        else
            flag1 = false;
        layoutparams.gravity & 0x70;
        JVM INSTR lookupswitch 2: default 212
    //                   16: 356
    //                   80: 316;
           goto _L4 _L5 _L6
_L5:
        break MISSING_BLOCK_LABEL_356;
_L4:
        view.layout(i2, layoutparams.topMargin, i2 + k1, layoutparams.topMargin + l1);
_L7:
        if(flag1)
            setDrawerViewOffset(view, f);
        int j2;
        int k2;
        if(layoutparams.onScreen > 0.0F)
            i = 0;
        else
            i = 4;
        if(view.getVisibility() != i)
            view.setVisibility(i);
          goto _L3
_L6:
        i = l - j;
        view.layout(i2, i - layoutparams.bottomMargin - view.getMeasuredHeight(), i2 + k1, i - layoutparams.bottomMargin);
          goto _L7
        j2 = l - j;
        k2 = (j2 - l1) / 2;
        if(k2 < layoutparams.topMargin)
        {
            i = layoutparams.topMargin;
        } else
        {
            i = k2;
            if(k2 + l1 > j2 - layoutparams.bottomMargin)
                i = j2 - layoutparams.bottomMargin - l1;
        }
        view.layout(i2, i, i2 + k1, i + l1);
          goto _L7
        mInLayout = false;
        mFirstLayout = false;
        return;
          goto _L3
    }

    protected void onMeasure(int i, int j)
    {
        int k;
        int l;
        int i1;
        int j1;
        int k1;
        int l1;
        int i2;
label0:
        {
            k = android.view.View.MeasureSpec.getMode(i);
            l = android.view.View.MeasureSpec.getMode(j);
            i1 = android.view.View.MeasureSpec.getSize(i);
            j1 = android.view.View.MeasureSpec.getSize(j);
            if(k == 0x40000000)
            {
                k1 = j1;
                l1 = i1;
                if(l == 0x40000000)
                    break label0;
            }
            if(!isInEditMode())
                break MISSING_BLOCK_LABEL_187;
            int j2;
            if(k != 0x80000000 && k == 0)
                i1 = 300;
            if(l == 0x80000000)
            {
                l1 = i1;
                k1 = j1;
            } else
            {
                k1 = j1;
                l1 = i1;
                if(l == 0)
                {
                    k1 = 300;
                    l1 = i1;
                }
            }
        }
        setMeasuredDimension(l1, k1);
        if(mLastInsets != null && ViewCompat.getFitsSystemWindows(this))
            l = 1;
        else
            l = 0;
        i2 = ViewCompat.getLayoutDirection(this);
        j1 = 0;
        i1 = 0;
        j2 = getChildCount();
        k = 0;
        while(k < j2) 
        {
            View view = getChildAt(k);
            if(view.getVisibility() != 8)
            {
                LayoutParams layoutparams = (LayoutParams)view.getLayoutParams();
                if(l != 0)
                {
                    int k2 = GravityCompat.getAbsoluteGravity(layoutparams.gravity, i2);
                    if(ViewCompat.getFitsSystemWindows(view))
                        IMPL.dispatchChildInsets(view, mLastInsets, k2);
                    else
                        IMPL.applyMarginInsets(layoutparams, mLastInsets, k2);
                }
                if(isContentView(view))
                    view.measure(android.view.View.MeasureSpec.makeMeasureSpec(l1 - layoutparams.leftMargin - layoutparams.rightMargin, 0x40000000), android.view.View.MeasureSpec.makeMeasureSpec(k1 - layoutparams.topMargin - layoutparams.bottomMargin, 0x40000000));
                else
                if(isDrawerView(view))
                {
                    if(SET_DRAWER_SHADOW_FROM_ELEVATION && ViewCompat.getElevation(view) != mDrawerElevation)
                        ViewCompat.setElevation(view, mDrawerElevation);
                    int l2 = getDrawerViewAbsoluteGravity(view) & 7;
                    boolean flag;
                    if(l2 == 3)
                        flag = true;
                    else
                        flag = false;
                    if(flag && j1 != 0 || !flag && i1 != 0)
                        throw new IllegalStateException((new StringBuilder()).append("Child drawer has absolute gravity ").append(gravityToString(l2)).append(" but this ").append("DrawerLayout").append(" already has a ").append("drawer view along that edge").toString());
                    if(flag)
                        j1 = 1;
                    else
                        i1 = 1;
                    view.measure(getChildMeasureSpec(i, mMinDrawerMargin + layoutparams.leftMargin + layoutparams.rightMargin, layoutparams.width), getChildMeasureSpec(j, layoutparams.topMargin + layoutparams.bottomMargin, layoutparams.height));
                } else
                {
                    throw new IllegalStateException((new StringBuilder()).append("Child ").append(view).append(" at index ").append(k).append(" does not have a valid layout_gravity - must be Gravity.LEFT, ").append("Gravity.RIGHT or Gravity.NO_GRAVITY").toString());
                }
            }
            k++;
        }
        break MISSING_BLOCK_LABEL_587;
        throw new IllegalArgumentException("DrawerLayout must be measured with MeasureSpec.EXACTLY.");
    }

    protected void onRestoreInstanceState(Parcelable parcelable)
    {
        parcelable = (SavedState)parcelable;
        onRestoreInstanceState(parcelable.getSuperState());
        if(((SavedState) (parcelable)).openDrawerGravity != 0)
        {
            View view = findDrawerWithGravity(((SavedState) (parcelable)).openDrawerGravity);
            if(view != null)
                openDrawer(view);
        }
        if(((SavedState) (parcelable)).lockModeLeft != 3)
            setDrawerLockMode(((SavedState) (parcelable)).lockModeLeft, 3);
        if(((SavedState) (parcelable)).lockModeRight != 3)
            setDrawerLockMode(((SavedState) (parcelable)).lockModeRight, 5);
        if(((SavedState) (parcelable)).lockModeStart != 3)
            setDrawerLockMode(((SavedState) (parcelable)).lockModeStart, 0x800003);
        if(((SavedState) (parcelable)).lockModeEnd != 3)
            setDrawerLockMode(((SavedState) (parcelable)).lockModeEnd, 0x800005);
    }

    public void onRtlPropertiesChanged(int i)
    {
        resolveShadowDrawables();
    }

    protected Parcelable onSaveInstanceState()
    {
        SavedState savedstate = new SavedState(onSaveInstanceState());
        int i = getChildCount();
        int j = 0;
        do
        {
label0:
            {
                if(j < i)
                {
                    LayoutParams layoutparams = (LayoutParams)getChildAt(j).getLayoutParams();
                    boolean flag;
                    boolean flag1;
                    if(layoutparams.openState == 1)
                        flag = true;
                    else
                        flag = false;
                    if(layoutparams.openState == 2)
                        flag1 = true;
                    else
                        flag1 = false;
                    if(!flag && !flag1)
                        break label0;
                    savedstate.openDrawerGravity = layoutparams.gravity;
                }
                savedstate.lockModeLeft = mLockModeLeft;
                savedstate.lockModeRight = mLockModeRight;
                savedstate.lockModeStart = mLockModeStart;
                savedstate.lockModeEnd = mLockModeEnd;
                return savedstate;
            }
            j++;
        } while(true);
    }

    public boolean onTouchEvent(MotionEvent motionevent)
    {
        mLeftDragger.processTouchEvent(motionevent);
        mRightDragger.processTouchEvent(motionevent);
        motionevent.getAction() & 0xff;
        JVM INSTR tableswitch 0 3: default 56
    //                   0 58
    //                   1 91
    //                   2 56
    //                   3 225;
           goto _L1 _L2 _L3 _L1 _L4
_L1:
        return true;
_L2:
        float f = motionevent.getX();
        float f2 = motionevent.getY();
        mInitialMotionX = f;
        mInitialMotionY = f2;
        mDisallowInterceptRequested = false;
        mChildrenCanceledTouch = false;
        continue; /* Loop/switch isn't completed */
_L3:
        float f1 = motionevent.getX();
        float f3 = motionevent.getY();
        boolean flag = true;
        motionevent = mLeftDragger.findTopChildUnder((int)f1, (int)f3);
        boolean flag1 = flag;
        if(motionevent != null)
        {
            flag1 = flag;
            if(isContentView(motionevent))
            {
                f1 -= mInitialMotionX;
                f3 -= mInitialMotionY;
                int i = mLeftDragger.getTouchSlop();
                flag1 = flag;
                if(f1 * f1 + f3 * f3 < (float)(i * i))
                {
                    motionevent = findOpenDrawer();
                    flag1 = flag;
                    if(motionevent != null)
                        if(getDrawerLockMode(motionevent) == 2)
                            flag1 = true;
                        else
                            flag1 = false;
                }
            }
        }
        closeDrawers(flag1);
        mDisallowInterceptRequested = false;
        continue; /* Loop/switch isn't completed */
_L4:
        closeDrawers(true);
        mDisallowInterceptRequested = false;
        mChildrenCanceledTouch = false;
        if(true) goto _L1; else goto _L5
_L5:
    }

    public void openDrawer(int i)
    {
        View view = findDrawerWithGravity(i);
        if(view == null)
        {
            throw new IllegalArgumentException((new StringBuilder()).append("No drawer view found with gravity ").append(gravityToString(i)).toString());
        } else
        {
            openDrawer(view);
            return;
        }
    }

    public void openDrawer(View view)
    {
        if(!isDrawerView(view))
            throw new IllegalArgumentException((new StringBuilder()).append("View ").append(view).append(" is not a sliding drawer").toString());
        LayoutParams layoutparams = (LayoutParams)view.getLayoutParams();
        if(mFirstLayout)
        {
            layoutparams.onScreen = 1.0F;
            layoutparams.openState = 1;
            updateChildrenImportantForAccessibility(view, true);
        } else
        {
            view = 
// JavaClassFileOutputException: get_constant: invalid tag

    public void removeDrawerListener(DrawerListener drawerlistener)
    {
        if(drawerlistener != null && mListeners != null)
            mListeners.remove(drawerlistener);
    }

    public void requestDisallowInterceptTouchEvent(boolean flag)
    {
        requestDisallowInterceptTouchEvent(flag);
        mDisallowInterceptRequested = flag;
        if(flag)
            closeDrawers(true);
    }

    public void requestLayout()
    {
        if(!mInLayout)
            requestLayout();
    }

    public void setChildInsets(Object obj, boolean flag)
    {
        mLastInsets = obj;
        mDrawStatusBarBackground = flag;
        if(!flag && getBackground() == null)
            flag = true;
        else
            flag = false;
        setWillNotDraw(flag);
        requestLayout();
    }

    public void setDrawerElevation(float f)
    {
        mDrawerElevation = f;
        for(int i = 0; i < getChildCount(); i++)
        {
            View view = getChildAt(i);
            if(isDrawerView(view))
                ViewCompat.setElevation(view, mDrawerElevation);
        }

    }

    public void setDrawerListener(DrawerListener drawerlistener)
    {
        if(mListener != null)
            removeDrawerListener(mListener);
        if(drawerlistener != null)
            addDrawerListener(drawerlistener);
        mListener = drawerlistener;
    }

    public void setDrawerLockMode(int i)
    {
        setDrawerLockMode(i, 3);
        setDrawerLockMode(i, 5);
    }

    public void setDrawerLockMode(int i, int j)
    {
        int k = GravityCompat.getAbsoluteGravity(j, ViewCompat.getLayoutDirection(this));
        j;
        JVM INSTR lookupswitch 4: default 52
    //                   3: 97
    //                   5: 105
    //                   8388611: 113
    //                   8388613: 121;
           goto _L1 _L2 _L3 _L4 _L5
_L1:
        if(i != 0)
        {
            ViewDragHelper viewdraghelper;
            if(k == 3)
                viewdraghelper = mLeftDragger;
            else
                viewdraghelper = mRightDragger;
            viewdraghelper.cancel();
        }
        i;
        JVM INSTR tableswitch 1 2: default 96
    //                   1 159
    //                   2 138;
           goto _L6 _L7 _L8
_L6:
        break; /* Loop/switch isn't completed */
_L7:
        break MISSING_BLOCK_LABEL_159;
_L9:
        return;
_L2:
        mLockModeLeft = i;
          goto _L1
_L3:
        mLockModeRight = i;
          goto _L1
_L4:
        mLockModeStart = i;
          goto _L1
_L5:
        mLockModeEnd = i;
          goto _L1
_L8:
        View view = findDrawerWithGravity(k);
        if(view != null)
            openDrawer(view);
          goto _L9
        View view1 = findDrawerWithGravity(k);
        if(view1 != null)
            closeDrawer(view1);
          goto _L9
    }

    public void setDrawerLockMode(int i, View view)
    {
        if(!isDrawerView(view))
        {
            throw new IllegalArgumentException((new StringBuilder()).append("View ").append(view).append(" is not a ").append("drawer with appropriate layout_gravity").toString());
        } else
        {
            setDrawerLockMode(i, ((LayoutParams)view.getLayoutParams()).gravity);
            return;
        }
    }

    public void setDrawerShadow(int i, int j)
    {
        setDrawerShadow(getResources().getDrawable(i), j);
    }

    public void setDrawerShadow(Drawable drawable, int i)
    {
        if(!SET_DRAWER_SHADOW_FROM_ELEVATION) goto _L2; else goto _L1
_L1:
        return;
_L2:
        if((i & 0x800003) != 0x800003)
            break; /* Loop/switch isn't completed */
        mShadowStart = drawable;
_L4:
        resolveShadowDrawables();
        invalidate();
        if(true) goto _L1; else goto _L3
_L3:
        if((i & 0x800005) == 0x800005)
        {
            mShadowEnd = drawable;
        } else
        {
            if((i & 3) != 3)
                continue; /* Loop/switch isn't completed */
            mShadowLeft = drawable;
        }
          goto _L4
        if((i & 5) != 5) goto _L1; else goto _L5
_L5:
        mShadowRight = drawable;
          goto _L4
    }

    public void setDrawerTitle(int i, CharSequence charsequence)
    {
        i = GravityCompat.getAbsoluteGravity(i, ViewCompat.getLayoutDirection(this));
        if(i != 3) goto _L2; else goto _L1
_L1:
        mTitleLeft = charsequence;
_L4:
        return;
_L2:
        if(i == 5)
            mTitleRight = charsequence;
        if(true) goto _L4; else goto _L3
_L3:
    }

    void setDrawerViewOffset(View view, float f)
    {
        LayoutParams layoutparams = (LayoutParams)view.getLayoutParams();
        if(f != layoutparams.onScreen)
        {
            layoutparams.onScreen = f;
            dispatchOnDrawerSlide(view, f);
        }
    }

    public void setScrimColor(int i)
    {
        mScrimColor = i;
        invalidate();
    }

    public void setStatusBarBackground(int i)
    {
        Drawable drawable;
        if(i != 0)
            drawable = ContextCompat.getDrawable(getContext(), i);
        else
            drawable = null;
        mStatusBarBackground = drawable;
        invalidate();
    }

    public void setStatusBarBackground(Drawable drawable)
    {
        mStatusBarBackground = drawable;
        invalidate();
    }

    public void setStatusBarBackgroundColor(int i)
    {
        mStatusBarBackground = new ColorDrawable(i);
        invalidate();
    }

    void updateDrawerState(int i, int j, View view)
    {
        LayoutParams layoutparams;
        i = mLeftDragger.getViewDragState();
        int k = mRightDragger.getViewDragState();
        if(i == 1 || k == 1)
            i = 1;
        else
        if(i == 2 || k == 2)
            i = 2;
        else
            i = 0;
        if(view == null || j != 0) goto _L2; else goto _L1
_L1:
        layoutparams = (LayoutParams)view.getLayoutParams();
        if(layoutparams.onScreen != 0.0F) goto _L4; else goto _L3
_L3:
        dispatchOnDrawerClosed(view);
_L2:
        if(i != mDrawerState)
        {
            mDrawerState = i;
            if(mListeners != null)
                for(j = mListeners.size() - 1; j >= 0; j--)
                    ((DrawerListener)mListeners.get(j)).onDrawerStateChanged(i);

        }
        break; /* Loop/switch isn't completed */
_L4:
        if(layoutparams.onScreen == 1.0F)
            dispatchOnDrawerOpened(view);
        if(true) goto _L2; else goto _L5
_L5:
    }

    private static final boolean ALLOW_EDGE_LOCK = false;
    private static final boolean CAN_HIDE_DESCENDANTS;
    private static final boolean CHILDREN_DISALLOW_INTERCEPT = true;
    private static final int DEFAULT_SCRIM_COLOR = 0x99000000;
    private static final int DRAWER_ELEVATION = 10;
    static final DrawerLayoutCompatImpl IMPL;
    private static final int LAYOUT_ATTRS[] = {
        0x10100b3
    };
    public static final int LOCK_MODE_LOCKED_CLOSED = 1;
    public static final int LOCK_MODE_LOCKED_OPEN = 2;
    public static final int LOCK_MODE_UNDEFINED = 3;
    public static final int LOCK_MODE_UNLOCKED = 0;
    private static final int MIN_DRAWER_MARGIN = 64;
    private static final int MIN_FLING_VELOCITY = 400;
    private static final int PEEK_DELAY = 160;
    private static final boolean SET_DRAWER_SHADOW_FROM_ELEVATION;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    private static final String TAG = "DrawerLayout";
    private static final float TOUCH_SLOP_SENSITIVITY = 1F;
    private final ChildAccessibilityDelegate mChildAccessibilityDelegate;
    private boolean mChildrenCanceledTouch;
    private boolean mDisallowInterceptRequested;
    private boolean mDrawStatusBarBackground;
    private float mDrawerElevation;
    private int mDrawerState;
    private boolean mFirstLayout;
    private boolean mInLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private Object mLastInsets;
    private final ViewDragCallback mLeftCallback;
    private final ViewDragHelper mLeftDragger;
    private DrawerListener mListener;
    private List mListeners;
    private int mLockModeEnd;
    private int mLockModeLeft;
    private int mLockModeRight;
    private int mLockModeStart;
    private int mMinDrawerMargin;
    private final ArrayList mNonDrawerViews;
    private final ViewDragCallback mRightCallback;
    private final ViewDragHelper mRightDragger;
    private int mScrimColor;
    private float mScrimOpacity;
    private Paint mScrimPaint;
    private Drawable mShadowEnd;
    private Drawable mShadowLeft;
    private Drawable mShadowLeftResolved;
    private Drawable mShadowRight;
    private Drawable mShadowRightResolved;
    private Drawable mShadowStart;
    private Drawable mStatusBarBackground;
    private CharSequence mTitleLeft;
    private CharSequence mTitleRight;

    static 
    {
        boolean flag = true;
        boolean flag1;
        if(android.os.Build.VERSION.SDK_INT >= 19)
            flag1 = true;
        else
            flag1 = false;
        CAN_HIDE_DESCENDANTS = flag1;
        if(android.os.Build.VERSION.SDK_INT >= 21)
            flag1 = flag;
        else
            flag1 = false;
        SET_DRAWER_SHADOW_FROM_ELEVATION = flag1;
        if(android.os.Build.VERSION.SDK_INT >= 21)
            IMPL = new DrawerLayoutCompatImplApi21();
        else
            IMPL = new DrawerLayoutCompatImplBase();
    }





    // Unreferenced inner class android/support/v4/widget/DrawerLayout$ViewDragCallback$1

/* anonymous class */
    class ViewDragCallback._cls1
        implements Runnable
    {

        public void run()
        {
            peekDrawer();
        }

        final ViewDragCallback this$1;

            
            {
                this$1 = ViewDragCallback.this;
                super();
            }
    }

}
