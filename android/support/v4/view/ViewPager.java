// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.*;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.support.v4.widget.EdgeEffectCompat;
import android.util.*;
import android.view.*;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import java.lang.reflect.Method;
import java.util.*;

// Referenced classes of package android.support.v4.view:
//            PagerAdapter, ViewCompat, MotionEventCompat, VelocityTrackerCompat, 
//            KeyEventCompat, ViewConfigurationCompat, AccessibilityDelegateCompat, OnApplyWindowInsetsListener, 
//            WindowInsetsCompat

public class ViewPager extends ViewGroup
{
    static interface Decor
    {
    }

    static class ItemInfo
    {

        Object object;
        float offset;
        int position;
        boolean scrolling;
        float widthFactor;

        ItemInfo()
        {
        }
    }

    public static class LayoutParams extends android.view.ViewGroup.LayoutParams
    {

        int childIndex;
        public int gravity;
        public boolean isDecor;
        boolean needsMeasure;
        int position;
        float widthFactor;

        public LayoutParams()
        {
            super(-1, -1);
            widthFactor = 0.0F;
        }

        public LayoutParams(Context context, AttributeSet attributeset)
        {
            super(context, attributeset);
            widthFactor = 0.0F;
            context = context.obtainStyledAttributes(attributeset, ViewPager.LAYOUT_ATTRS);
            gravity = context.getInteger(0, 48);
            context.recycle();
        }
    }

    class MyAccessibilityDelegate extends AccessibilityDelegateCompat
    {

        private boolean canScroll()
        {
            boolean flag = true;
            if(mAdapter == null || mAdapter.getCount() <= 1)
                flag = false;
            return flag;
        }

        public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityevent)
        {
            super.onInitializeAccessibilityEvent(view, accessibilityevent);
            accessibilityevent.setClassName(android/support/v4/view/ViewPager.getName());
            view = AccessibilityEventCompat.asRecord(accessibilityevent);
            view.setScrollable(canScroll());
            if(accessibilityevent.getEventType() == 4096 && mAdapter != null)
            {
                view.setItemCount(mAdapter.getCount());
                view.setFromIndex(mCurItem);
                view.setToIndex(mCurItem);
            }
        }

        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilitynodeinfocompat)
        {
            super.onInitializeAccessibilityNodeInfo(view, accessibilitynodeinfocompat);
            accessibilitynodeinfocompat.setClassName(android/support/v4/view/ViewPager.getName());
            accessibilitynodeinfocompat.setScrollable(canScroll());
            if(canScrollHorizontally(1))
                accessibilitynodeinfocompat.addAction(4096);
            if(canScrollHorizontally(-1))
                accessibilitynodeinfocompat.addAction(8192);
        }

        public boolean performAccessibilityAction(View view, int i, Bundle bundle)
        {
            boolean flag = true;
            if(!super.performAccessibilityAction(view, i, bundle)) goto _L2; else goto _L1
_L1:
            return flag;
_L2:
            switch(i)
            {
            default:
                flag = false;
                break;

            case 4096: 
                if(canScrollHorizontally(1))
                    setCurrentItem(mCurItem + 1);
                else
                    flag = false;
                break;

            case 8192: 
                if(canScrollHorizontally(-1))
                    setCurrentItem(mCurItem - 1);
                else
                    flag = false;
                break;
            }
            if(true) goto _L1; else goto _L3
_L3:
        }

        final ViewPager this$0;

        MyAccessibilityDelegate()
        {
            this$0 = ViewPager.this;
            super();
        }
    }

    static interface OnAdapterChangeListener
    {

        public abstract void onAdapterChanged(PagerAdapter pageradapter, PagerAdapter pageradapter1);
    }

    public static interface OnPageChangeListener
    {

        public abstract void onPageScrollStateChanged(int i);

        public abstract void onPageScrolled(int i, float f, int j);

        public abstract void onPageSelected(int i);
    }

    public static interface PageTransformer
    {

        public abstract void transformPage(View view, float f);
    }

    private class PagerObserver extends DataSetObserver
    {

        public void onChanged()
        {
            dataSetChanged();
        }

        public void onInvalidated()
        {
            dataSetChanged();
        }

        final ViewPager this$0;

        private PagerObserver()
        {
            this$0 = ViewPager.this;
            super();
        }

    }

    public static class SavedState extends android.view.View.BaseSavedState
    {

        public String toString()
        {
            return (new StringBuilder()).append("FragmentPager.SavedState{").append(Integer.toHexString(System.identityHashCode(this))).append(" position=").append(position).append("}").toString();
        }

        public void writeToParcel(Parcel parcel, int i)
        {
            super.writeToParcel(parcel, i);
            parcel.writeInt(position);
            parcel.writeParcelable(adapterState, i);
        }

        public static final android.os.Parcelable.Creator CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks() {

            public SavedState createFromParcel(Parcel parcel, ClassLoader classloader)
            {
                return new SavedState(parcel, classloader);
            }

            public volatile Object createFromParcel(Parcel parcel, ClassLoader classloader)
            {
                return createFromParcel(parcel, classloader);
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
);
        Parcelable adapterState;
        ClassLoader loader;
        int position;


        SavedState(Parcel parcel, ClassLoader classloader)
        {
            super(parcel);
            ClassLoader classloader1 = classloader;
            if(classloader == null)
                classloader1 = getClass().getClassLoader();
            position = parcel.readInt();
            adapterState = parcel.readParcelable(classloader1);
            loader = classloader1;
        }

        public SavedState(Parcelable parcelable)
        {
            super(parcelable);
        }
    }

    public static class SimpleOnPageChangeListener
        implements OnPageChangeListener
    {

        public void onPageScrollStateChanged(int i)
        {
        }

        public void onPageScrolled(int i, float f, int j)
        {
        }

        public void onPageSelected(int i)
        {
        }

        public SimpleOnPageChangeListener()
        {
        }
    }

    static class ViewPositionComparator
        implements Comparator
    {

        public int compare(View view, View view1)
        {
            view = (LayoutParams)view.getLayoutParams();
            view1 = (LayoutParams)view1.getLayoutParams();
            int i;
            if(((LayoutParams) (view)).isDecor != ((LayoutParams) (view1)).isDecor)
            {
                if(((LayoutParams) (view)).isDecor)
                    i = 1;
                else
                    i = -1;
            } else
            {
                i = ((LayoutParams) (view)).position - ((LayoutParams) (view1)).position;
            }
            return i;
        }

        public volatile int compare(Object obj, Object obj1)
        {
            return compare((View)obj, (View)obj1);
        }

        ViewPositionComparator()
        {
        }
    }


    public ViewPager(Context context)
    {
        super(context);
        mItems = new ArrayList();
        mTempItem = new ItemInfo();
        mTempRect = new Rect();
        mRestoredCurItem = -1;
        mRestoredAdapterState = null;
        mRestoredClassLoader = null;
        mFirstOffset = -3.402823E+038F;
        mLastOffset = 3.402823E+038F;
        mOffscreenPageLimit = 1;
        mActivePointerId = -1;
        mFirstLayout = true;
        mNeedCalculatePageOffsets = false;
        mEndScrollRunnable = new Runnable() {

            public void run()
            {
                setScrollState(0);
                populate();
            }

            final ViewPager this$0;

            
            {
                this$0 = ViewPager.this;
                super();
            }
        }
;
        mScrollState = 0;
        initViewPager();
    }

    public ViewPager(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        mItems = new ArrayList();
        mTempItem = new ItemInfo();
        mTempRect = new Rect();
        mRestoredCurItem = -1;
        mRestoredAdapterState = null;
        mRestoredClassLoader = null;
        mFirstOffset = -3.402823E+038F;
        mLastOffset = 3.402823E+038F;
        mOffscreenPageLimit = 1;
        mActivePointerId = -1;
        mFirstLayout = true;
        mNeedCalculatePageOffsets = false;
        mEndScrollRunnable = new _cls3();
        mScrollState = 0;
        initViewPager();
    }

    private void calculatePageOffsets(ItemInfo iteminfo, int i, ItemInfo iteminfo1)
    {
        int j = mAdapter.getCount();
        int k = getClientWidth();
        float f;
        if(k > 0)
            f = (float)mPageMargin / (float)k;
        else
            f = 0.0F;
        if(iteminfo1 != null)
        {
            k = iteminfo1.position;
            if(k < iteminfo.position)
            {
                int l = 0;
                float f1 = iteminfo1.offset + iteminfo1.widthFactor + f;
                int k1;
                for(k++; k <= iteminfo.position && l < mItems.size(); k = k1 + 1)
                {
                    iteminfo1 = (ItemInfo)mItems.get(l);
                    float f4;
                    do
                    {
                        f4 = f1;
                        k1 = k;
                        if(k <= iteminfo1.position)
                            break;
                        f4 = f1;
                        k1 = k;
                        if(l >= mItems.size() - 1)
                            break;
                        l++;
                        iteminfo1 = (ItemInfo)mItems.get(l);
                    } while(true);
                    for(; k1 < iteminfo1.position; k1++)
                        f4 += mAdapter.getPageWidth(k1) + f;

                    iteminfo1.offset = f4;
                    f1 = f4 + (iteminfo1.widthFactor + f);
                }

            } else
            if(k > iteminfo.position)
            {
                int i1 = mItems.size() - 1;
                float f2 = iteminfo1.offset;
                int l1;
                for(k--; k >= iteminfo.position && i1 >= 0; k = l1 - 1)
                {
                    iteminfo1 = (ItemInfo)mItems.get(i1);
                    float f5;
                    do
                    {
                        f5 = f2;
                        l1 = k;
                        if(k >= iteminfo1.position)
                            break;
                        f5 = f2;
                        l1 = k;
                        if(i1 <= 0)
                            break;
                        i1--;
                        iteminfo1 = (ItemInfo)mItems.get(i1);
                    } while(true);
                    for(; l1 > iteminfo1.position; l1--)
                        f5 -= mAdapter.getPageWidth(l1) + f;

                    f2 = f5 - (iteminfo1.widthFactor + f);
                    iteminfo1.offset = f2;
                }

            }
        }
        int i2 = mItems.size();
        float f6 = iteminfo.offset;
        k = iteminfo.position - 1;
        int j1;
        float f3;
        if(iteminfo.position == 0)
            f3 = iteminfo.offset;
        else
            f3 = -3.402823E+038F;
        mFirstOffset = f3;
        if(iteminfo.position == j - 1)
            f3 = (iteminfo.offset + iteminfo.widthFactor) - 1.0F;
        else
            f3 = 3.402823E+038F;
        mLastOffset = f3;
        j1 = i - 1;
        f3 = f6;
        while(j1 >= 0) 
        {
            for(iteminfo1 = (ItemInfo)mItems.get(j1); k > iteminfo1.position; k--)
                f3 -= mAdapter.getPageWidth(k) + f;

            f3 -= iteminfo1.widthFactor + f;
            iteminfo1.offset = f3;
            if(iteminfo1.position == 0)
                mFirstOffset = f3;
            j1--;
            k--;
        }
        f3 = iteminfo.offset + iteminfo.widthFactor + f;
        j1 = iteminfo.position + 1;
        k = i + 1;
        for(i = j1; k < i2; i++)
        {
            for(iteminfo = (ItemInfo)mItems.get(k); i < iteminfo.position; i++)
                f3 += mAdapter.getPageWidth(i) + f;

            if(iteminfo.position == j - 1)
                mLastOffset = (iteminfo.widthFactor + f3) - 1.0F;
            iteminfo.offset = f3;
            f3 += iteminfo.widthFactor + f;
            k++;
        }

        mNeedCalculatePageOffsets = false;
    }

    private void completeScroll(boolean flag)
    {
        int i = 1;
        int j;
        if(mScrollState == 2)
            j = 1;
        else
            j = 0;
        if(j != 0)
        {
            setScrollingCacheEnabled(false);
            boolean flag1;
            ItemInfo iteminfo;
            if(mScroller.isFinished())
                i = 0;
            if(i != 0)
            {
                mScroller.abortAnimation();
                int k = getScrollX();
                int l = getScrollY();
                i = mScroller.getCurrX();
                int i1 = mScroller.getCurrY();
                if(k != i || l != i1)
                {
                    scrollTo(i, i1);
                    if(i != k)
                        pageScrolled(i);
                }
            }
        }
        mPopulatePending = false;
        flag1 = false;
        i = j;
        for(j = ((flag1) ? 1 : 0); j < mItems.size(); j++)
        {
            iteminfo = (ItemInfo)mItems.get(j);
            if(iteminfo.scrolling)
            {
                i = 1;
                iteminfo.scrolling = false;
            }
        }

        if(i != 0)
            if(flag)
                ViewCompat.postOnAnimation(this, mEndScrollRunnable);
            else
                mEndScrollRunnable.run();
    }

    private int determineTargetPage(int i, float f, int j, int k)
    {
        if(Math.abs(k) > mFlingDistance && Math.abs(j) > mMinimumVelocity)
        {
            if(j <= 0)
                i++;
        } else
        {
            float f1;
            if(i >= mCurItem)
                f1 = 0.4F;
            else
                f1 = 0.6F;
            i = (int)((float)i + f + f1);
        }
        j = i;
        if(mItems.size() > 0)
        {
            ItemInfo iteminfo = (ItemInfo)mItems.get(0);
            ItemInfo iteminfo1 = (ItemInfo)mItems.get(mItems.size() - 1);
            j = Math.max(iteminfo.position, Math.min(i, iteminfo1.position));
        }
        return j;
    }

    private void dispatchOnPageScrolled(int i, float f, int j)
    {
        if(mOnPageChangeListener != null)
            mOnPageChangeListener.onPageScrolled(i, f, j);
        if(mOnPageChangeListeners != null)
        {
            int k = 0;
            for(int l = mOnPageChangeListeners.size(); k < l; k++)
            {
                OnPageChangeListener onpagechangelistener = (OnPageChangeListener)mOnPageChangeListeners.get(k);
                if(onpagechangelistener != null)
                    onpagechangelistener.onPageScrolled(i, f, j);
            }

        }
        if(mInternalPageChangeListener != null)
            mInternalPageChangeListener.onPageScrolled(i, f, j);
    }

    private void dispatchOnPageSelected(int i)
    {
        if(mOnPageChangeListener != null)
            mOnPageChangeListener.onPageSelected(i);
        if(mOnPageChangeListeners != null)
        {
            int j = 0;
            for(int k = mOnPageChangeListeners.size(); j < k; j++)
            {
                OnPageChangeListener onpagechangelistener = (OnPageChangeListener)mOnPageChangeListeners.get(j);
                if(onpagechangelistener != null)
                    onpagechangelistener.onPageSelected(i);
            }

        }
        if(mInternalPageChangeListener != null)
            mInternalPageChangeListener.onPageSelected(i);
    }

    private void dispatchOnScrollStateChanged(int i)
    {
        if(mOnPageChangeListener != null)
            mOnPageChangeListener.onPageScrollStateChanged(i);
        if(mOnPageChangeListeners != null)
        {
            int j = 0;
            for(int k = mOnPageChangeListeners.size(); j < k; j++)
            {
                OnPageChangeListener onpagechangelistener = (OnPageChangeListener)mOnPageChangeListeners.get(j);
                if(onpagechangelistener != null)
                    onpagechangelistener.onPageScrollStateChanged(i);
            }

        }
        if(mInternalPageChangeListener != null)
            mInternalPageChangeListener.onPageScrollStateChanged(i);
    }

    private void enableLayers(boolean flag)
    {
        int i = getChildCount();
        int j = 0;
        while(j < i) 
        {
            byte byte0;
            if(flag)
                byte0 = 2;
            else
                byte0 = 0;
            ViewCompat.setLayerType(getChildAt(j), byte0, null);
            j++;
        }
    }

    private void endDrag()
    {
        mIsBeingDragged = false;
        mIsUnableToDrag = false;
        if(mVelocityTracker != null)
        {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private Rect getChildRectInPagerCoordinates(Rect rect, View view)
    {
        Rect rect1 = rect;
        if(rect == null)
            rect1 = new Rect();
        if(view == null)
        {
            rect1.set(0, 0, 0, 0);
        } else
        {
            rect1.left = view.getLeft();
            rect1.right = view.getRight();
            rect1.top = view.getTop();
            rect1.bottom = view.getBottom();
            rect = view.getParent();
            while((rect instanceof ViewGroup) && rect != this) 
            {
                rect = (ViewGroup)rect;
                rect1.left = rect1.left + rect.getLeft();
                rect1.right = rect1.right + rect.getRight();
                rect1.top = rect1.top + rect.getTop();
                rect1.bottom = rect1.bottom + rect.getBottom();
                rect = rect.getParent();
            }
        }
        return rect1;
    }

    private int getClientWidth()
    {
        return getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    private ItemInfo infoForCurrentScrollPosition()
    {
        float f = 0.0F;
        int i = getClientWidth();
        float f1;
        int j;
        float f2;
        float f3;
        boolean flag;
        ItemInfo iteminfo;
        if(i > 0)
            f1 = (float)getScrollX() / (float)i;
        else
            f1 = 0.0F;
        if(i > 0)
            f = (float)mPageMargin / (float)i;
        j = -1;
        f2 = 0.0F;
        f3 = 0.0F;
        flag = true;
        iteminfo = null;
        i = 0;
        do
        {
            int k;
            ItemInfo iteminfo2;
label0:
            {
                ItemInfo iteminfo1;
label1:
                {
                    iteminfo1 = iteminfo;
                    if(i >= mItems.size())
                        break label1;
                    iteminfo1 = (ItemInfo)mItems.get(i);
                    k = i;
                    iteminfo2 = iteminfo1;
                    if(!flag)
                    {
                        k = i;
                        iteminfo2 = iteminfo1;
                        if(iteminfo1.position != j + 1)
                        {
                            iteminfo2 = mTempItem;
                            iteminfo2.offset = f2 + f3 + f;
                            iteminfo2.position = j + 1;
                            iteminfo2.widthFactor = mAdapter.getPageWidth(iteminfo2.position);
                            k = i - 1;
                        }
                    }
                    f2 = iteminfo2.offset;
                    f3 = iteminfo2.widthFactor;
                    if(!flag)
                    {
                        iteminfo1 = iteminfo;
                        if(f1 < f2)
                            break label1;
                    }
                    if(f1 >= f3 + f2 + f && k != mItems.size() - 1)
                        break label0;
                    iteminfo1 = iteminfo2;
                }
                return iteminfo1;
            }
            flag = false;
            j = iteminfo2.position;
            f3 = iteminfo2.widthFactor;
            i = k + 1;
            iteminfo = iteminfo2;
        } while(true);
    }

    private boolean isGutterDrag(float f, float f1)
    {
        boolean flag;
        if(f < (float)mGutterSize && f1 > 0.0F || f > (float)(getWidth() - mGutterSize) && f1 < 0.0F)
            flag = true;
        else
            flag = false;
        return flag;
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
            mLastMotionX = MotionEventCompat.getX(motionevent, i);
            mActivePointerId = MotionEventCompat.getPointerId(motionevent, i);
            if(mVelocityTracker != null)
                mVelocityTracker.clear();
        }
    }

    private boolean pageScrolled(int i)
    {
        boolean flag = false;
        if(mItems.size() == 0)
        {
            mCalledSuper = false;
            onPageScrolled(0, 0.0F, 0);
            if(!mCalledSuper)
                throw new IllegalStateException("onPageScrolled did not call superclass implementation");
        } else
        {
            ItemInfo iteminfo = infoForCurrentScrollPosition();
            int j = getClientWidth();
            int k = mPageMargin;
            float f = (float)mPageMargin / (float)j;
            int l = iteminfo.position;
            f = ((float)i / (float)j - iteminfo.offset) / (iteminfo.widthFactor + f);
            i = (int)((float)(j + k) * f);
            mCalledSuper = false;
            onPageScrolled(l, f, i);
            if(!mCalledSuper)
                throw new IllegalStateException("onPageScrolled did not call superclass implementation");
            flag = true;
        }
        return flag;
    }

    private boolean performDrag(float f)
    {
        boolean flag;
        boolean flag1;
        boolean flag2;
        float f1;
        float f2;
        int i;
        boolean flag3;
        boolean flag4;
        flag = false;
        flag1 = false;
        flag2 = false;
        f1 = mLastMotionX;
        mLastMotionX = f;
        f2 = (float)getScrollX() + (f1 - f);
        i = getClientWidth();
        f = (float)i * mFirstOffset;
        f1 = (float)i * mLastOffset;
        flag3 = true;
        flag4 = true;
        ItemInfo iteminfo = (ItemInfo)mItems.get(0);
        ItemInfo iteminfo1 = (ItemInfo)mItems.get(mItems.size() - 1);
        if(iteminfo.position != 0)
        {
            flag3 = false;
            f = iteminfo.offset * (float)i;
        }
        if(iteminfo1.position != mAdapter.getCount() - 1)
        {
            flag4 = false;
            f1 = iteminfo1.offset * (float)i;
        }
        if(f2 >= f) goto _L2; else goto _L1
_L1:
        if(flag3)
            flag2 = mLeftEdge.onPull(Math.abs(f - f2) / (float)i);
_L4:
        mLastMotionX = mLastMotionX + (f - (float)(int)f);
        scrollTo((int)f, getScrollY());
        pageScrolled((int)f);
        return flag2;
_L2:
        flag2 = flag;
        f = f2;
        if(f2 > f1)
        {
            flag2 = flag1;
            if(flag4)
                flag2 = mRightEdge.onPull(Math.abs(f2 - f1) / (float)i);
            f = f1;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    private void recomputeScrollPosition(int i, int j, int k, int l)
    {
        if(j <= 0 || mItems.isEmpty()) goto _L2; else goto _L1
_L1:
        if(!mScroller.isFinished())
        {
            mScroller.setFinalX(getCurrentItem() * getClientWidth());
        } else
        {
            int i1 = getPaddingLeft();
            int j1 = getPaddingRight();
            int k1 = getPaddingLeft();
            int l1 = getPaddingRight();
            float f = (float)getScrollX() / (float)((j - k1 - l1) + l);
            scrollTo((int)((float)((i - i1 - j1) + k) * f), getScrollY());
        }
_L4:
        return;
_L2:
        ItemInfo iteminfo = infoForPosition(mCurItem);
        float f1;
        if(iteminfo != null)
            f1 = Math.min(iteminfo.offset, mLastOffset);
        else
            f1 = 0.0F;
        i = (int)((float)(i - getPaddingLeft() - getPaddingRight()) * f1);
        if(i != getScrollX())
        {
            completeScroll(false);
            scrollTo(i, getScrollY());
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    private void removeNonDecorViews()
    {
        int j;
        for(int i = 0; i < getChildCount(); i = j + 1)
        {
            j = i;
            if(!((LayoutParams)getChildAt(i).getLayoutParams()).isDecor)
            {
                removeViewAt(i);
                j = i - 1;
            }
        }

    }

    private void requestParentDisallowInterceptTouchEvent(boolean flag)
    {
        ViewParent viewparent = getParent();
        if(viewparent != null)
            viewparent.requestDisallowInterceptTouchEvent(flag);
    }

    private boolean resetTouch()
    {
        mActivePointerId = -1;
        endDrag();
        return mLeftEdge.onRelease() | mRightEdge.onRelease();
    }

    private void scrollToItem(int i, boolean flag, int j, boolean flag1)
    {
        ItemInfo iteminfo = infoForPosition(i);
        int k = 0;
        if(iteminfo != null)
            k = (int)((float)getClientWidth() * Math.max(mFirstOffset, Math.min(iteminfo.offset, mLastOffset)));
        if(flag)
        {
            smoothScrollTo(k, 0, j);
            if(flag1)
                dispatchOnPageSelected(i);
        } else
        {
            if(flag1)
                dispatchOnPageSelected(i);
            completeScroll(false);
            scrollTo(k, 0);
            pageScrolled(k);
        }
    }

    private void setScrollState(int i)
    {
        if(mScrollState != i)
        {
            mScrollState = i;
            if(mPageTransformer != null)
            {
                boolean flag;
                if(i != 0)
                    flag = true;
                else
                    flag = false;
                enableLayers(flag);
            }
            dispatchOnScrollStateChanged(i);
        }
    }

    private void setScrollingCacheEnabled(boolean flag)
    {
        if(mScrollingCacheEnabled != flag)
            mScrollingCacheEnabled = flag;
    }

    private void sortChildDrawingOrder()
    {
        if(mDrawingOrder != 0)
        {
            int i;
            if(mDrawingOrderedChildren == null)
                mDrawingOrderedChildren = new ArrayList();
            else
                mDrawingOrderedChildren.clear();
            i = getChildCount();
            for(int j = 0; j < i; j++)
            {
                View view = getChildAt(j);
                mDrawingOrderedChildren.add(view);
            }

            Collections.sort(mDrawingOrderedChildren, sPositionComparator);
        }
    }

    public void addFocusables(ArrayList arraylist, int i, int j)
    {
        int k;
        int l;
        k = arraylist.size();
        l = getDescendantFocusability();
        if(l != 0x60000)
        {
            for(int i1 = 0; i1 < getChildCount(); i1++)
            {
                View view = getChildAt(i1);
                if(view.getVisibility() == 0)
                {
                    ItemInfo iteminfo = infoForChild(view);
                    if(iteminfo != null && iteminfo.position == mCurItem)
                        view.addFocusables(arraylist, i, j);
                }
            }

        }
        break MISSING_BLOCK_LABEL_87;
        if((l != 0x40000 || k == arraylist.size()) && isFocusable() && ((j & 1) != 1 || !isInTouchMode() || isFocusableInTouchMode()) && arraylist != null)
            arraylist.add(this);
        return;
    }

    ItemInfo addNewItem(int i, int j)
    {
        ItemInfo iteminfo = new ItemInfo();
        iteminfo.position = i;
        iteminfo.object = mAdapter.instantiateItem(this, i);
        iteminfo.widthFactor = mAdapter.getPageWidth(i);
        if(j < 0 || j >= mItems.size())
            mItems.add(iteminfo);
        else
            mItems.add(j, iteminfo);
        return iteminfo;
    }

    public void addOnPageChangeListener(OnPageChangeListener onpagechangelistener)
    {
        if(mOnPageChangeListeners == null)
            mOnPageChangeListeners = new ArrayList();
        mOnPageChangeListeners.add(onpagechangelistener);
    }

    public void addTouchables(ArrayList arraylist)
    {
        for(int i = 0; i < getChildCount(); i++)
        {
            View view = getChildAt(i);
            if(view.getVisibility() != 0)
                continue;
            ItemInfo iteminfo = infoForChild(view);
            if(iteminfo != null && iteminfo.position == mCurItem)
                view.addTouchables(arraylist);
        }

    }

    public void addView(View view, int i, android.view.ViewGroup.LayoutParams layoutparams)
    {
        android.view.ViewGroup.LayoutParams layoutparams1 = layoutparams;
        if(!checkLayoutParams(layoutparams))
            layoutparams1 = generateLayoutParams(layoutparams);
        layoutparams = (LayoutParams)layoutparams1;
        layoutparams.isDecor = ((LayoutParams) (layoutparams)).isDecor | (view instanceof Decor);
        if(mInLayout)
        {
            if(layoutparams != null && ((LayoutParams) (layoutparams)).isDecor)
                throw new IllegalStateException("Cannot add pager decor view during layout");
            layoutparams.needsMeasure = true;
            addViewInLayout(view, i, layoutparams1);
        } else
        {
            super.addView(view, i, layoutparams1);
        }
    }

    public boolean arrowScroll(int i)
    {
        View view = findFocus();
        if(view != this) goto _L2; else goto _L1
_L1:
        Object obj = null;
_L4:
        boolean flag = false;
        view = FocusFinder.getInstance().findNextFocus(this, ((View) (obj)), i);
        if(view != null && view != obj)
        {
            if(i == 17)
            {
                int j = getChildRectInPagerCoordinates(mTempRect, view).left;
                int l = getChildRectInPagerCoordinates(mTempRect, ((View) (obj))).left;
                boolean flag1;
                StringBuilder stringbuilder;
                if(obj != null && j >= l)
                    flag = pageLeft();
                else
                    flag = view.requestFocus();
            } else
            if(i == 66)
            {
                int i1 = getChildRectInPagerCoordinates(mTempRect, view).left;
                int k = getChildRectInPagerCoordinates(mTempRect, ((View) (obj))).left;
                if(obj != null && i1 <= k)
                    flag = pageRight();
                else
                    flag = view.requestFocus();
            }
        } else
        if(i == 17 || i == 1)
            flag = pageLeft();
        else
        if(i == 66 || i == 2)
            flag = pageRight();
        if(flag)
            playSoundEffect(SoundEffectConstants.getContantForFocusDirection(i));
        return flag;
_L2:
        obj = view;
        if(view == null) goto _L4; else goto _L3
_L3:
        l = 0;
        obj = view.getParent();
_L5:
label0:
        {
            flag1 = l;
            if(obj instanceof ViewGroup)
            {
                if(obj != this)
                    break label0;
                flag1 = true;
            }
            obj = view;
            if(!flag1)
            {
                stringbuilder = new StringBuilder();
                stringbuilder.append(view.getClass().getSimpleName());
                for(obj = view.getParent(); obj instanceof ViewGroup; obj = ((ViewParent) (obj)).getParent())
                    stringbuilder.append(" => ").append(obj.getClass().getSimpleName());

                break MISSING_BLOCK_LABEL_214;
            }
        }
          goto _L4
        obj = ((ViewParent) (obj)).getParent();
          goto _L5
        Log.e("ViewPager", (new StringBuilder()).append("arrowScroll tried to find focus based on non-child current focused view ").append(stringbuilder.toString()).toString());
        obj = null;
          goto _L4
    }

    public boolean beginFakeDrag()
    {
        boolean flag = false;
        if(!mIsBeingDragged)
        {
            mFakeDragging = true;
            setScrollState(1);
            mLastMotionX = 0.0F;
            mInitialMotionX = 0.0F;
            long l;
            MotionEvent motionevent;
            if(mVelocityTracker == null)
                mVelocityTracker = VelocityTracker.obtain();
            else
                mVelocityTracker.clear();
            l = SystemClock.uptimeMillis();
            motionevent = MotionEvent.obtain(l, l, 0, 0.0F, 0.0F, 0);
            mVelocityTracker.addMovement(motionevent);
            motionevent.recycle();
            mFakeDragBeginTime = l;
            flag = true;
        }
        return flag;
    }

    protected boolean canScroll(View view, boolean flag, int i, int j, int k)
    {
        ViewGroup viewgroup;
        int l;
        int i1;
        int j1;
        if(!(view instanceof ViewGroup))
            break MISSING_BLOCK_LABEL_143;
        viewgroup = (ViewGroup)view;
        l = view.getScrollX();
        i1 = view.getScrollY();
        j1 = viewgroup.getChildCount() - 1;
_L3:
        View view1;
        if(j1 < 0)
            break MISSING_BLOCK_LABEL_143;
        view1 = viewgroup.getChildAt(j1);
        if(j + l < view1.getLeft() || j + l >= view1.getRight() || k + i1 < view1.getTop() || k + i1 >= view1.getBottom() || !canScroll(view1, true, i, (j + l) - view1.getLeft(), (k + i1) - view1.getTop())) goto _L2; else goto _L1
_L1:
        flag = true;
_L4:
        return flag;
_L2:
        j1--;
          goto _L3
        if(flag && ViewCompat.canScrollHorizontally(view, -i))
            flag = true;
        else
            flag = false;
          goto _L4
    }

    public boolean canScrollHorizontally(int i)
    {
        boolean flag;
        boolean flag1;
        boolean flag2;
        flag = true;
        flag1 = true;
        flag2 = false;
        if(mAdapter != null) goto _L2; else goto _L1
_L1:
        flag1 = flag2;
_L4:
        return flag1;
_L2:
        int j = getClientWidth();
        int k = getScrollX();
        if(i < 0)
        {
            if(k <= (int)((float)j * mFirstOffset))
                flag1 = false;
        } else
        {
            flag1 = flag2;
            if(i > 0)
                if(k < (int)((float)j * mLastOffset))
                    flag1 = flag;
                else
                    flag1 = false;
        }
        if(true) goto _L4; else goto _L3
_L3:
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

    public void clearOnPageChangeListeners()
    {
        if(mOnPageChangeListeners != null)
            mOnPageChangeListeners.clear();
    }

    public void computeScroll()
    {
        mIsScrollStarted = true;
        if(!mScroller.isFinished() && mScroller.computeScrollOffset())
        {
            int i = getScrollX();
            int j = getScrollY();
            int k = mScroller.getCurrX();
            int l = mScroller.getCurrY();
            if(i != k || j != l)
            {
                scrollTo(k, l);
                if(!pageScrolled(k))
                {
                    mScroller.abortAnimation();
                    scrollTo(0, l);
                }
            }
            ViewCompat.postInvalidateOnAnimation(this);
        } else
        {
            completeScroll(true);
        }
    }

    void dataSetChanged()
    {
        int i = mAdapter.getCount();
        mExpectedAdapterCount = i;
        boolean flag;
        int k;
        boolean flag1;
        int i1;
        if(mItems.size() < mOffscreenPageLimit * 2 + 1 && mItems.size() < i)
            flag = true;
        else
            flag = false;
        k = mCurItem;
        flag1 = false;
        i1 = 0;
        while(i1 < mItems.size()) 
        {
            ItemInfo iteminfo = (ItemInfo)mItems.get(i1);
            int j1 = mAdapter.getItemPosition(iteminfo.object);
            int k1;
            int l1;
            int i2;
            if(j1 == -1)
            {
                k1 = k;
                l1 = ((flag1) ? 1 : 0);
                i2 = i1;
            } else
            if(j1 == -2)
            {
                mItems.remove(i1);
                j1 = i1 - 1;
                i1 = ((flag1) ? 1 : 0);
                if(!flag1)
                {
                    mAdapter.startUpdate(this);
                    i1 = 1;
                }
                mAdapter.destroyItem(this, iteminfo.position, iteminfo.object);
                flag = true;
                i2 = j1;
                l1 = i1;
                k1 = k;
                if(mCurItem == iteminfo.position)
                {
                    k1 = Math.max(0, Math.min(mCurItem, i - 1));
                    flag = true;
                    i2 = j1;
                    l1 = i1;
                }
            } else
            {
                i2 = i1;
                l1 = ((flag1) ? 1 : 0);
                k1 = k;
                if(iteminfo.position != j1)
                {
                    if(iteminfo.position == mCurItem)
                        k = j1;
                    iteminfo.position = j1;
                    flag = true;
                    i2 = i1;
                    l1 = ((flag1) ? 1 : 0);
                    k1 = k;
                }
            }
            i1 = i2 + 1;
            flag1 = l1;
            k = k1;
        }
        if(flag1)
            mAdapter.finishUpdate(this);
        Collections.sort(mItems, COMPARATOR);
        if(flag)
        {
            int l = getChildCount();
            for(int j = 0; j < l; j++)
            {
                LayoutParams layoutparams = (LayoutParams)getChildAt(j).getLayoutParams();
                if(!layoutparams.isDecor)
                    layoutparams.widthFactor = 0.0F;
            }

            setCurrentItemInternal(k, false, true);
            requestLayout();
        }
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

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityevent)
    {
        if(accessibilityevent.getEventType() != 4096) goto _L2; else goto _L1
_L1:
        boolean flag = super.dispatchPopulateAccessibilityEvent(accessibilityevent);
_L4:
        return flag;
_L2:
        int i = getChildCount();
        for(int j = 0; j < i; j++)
        {
            View view = getChildAt(j);
            if(view.getVisibility() != 0)
                continue;
            ItemInfo iteminfo = infoForChild(view);
            if(iteminfo == null || iteminfo.position != mCurItem || !view.dispatchPopulateAccessibilityEvent(accessibilityevent))
                continue;
            flag = true;
            continue; /* Loop/switch isn't completed */
        }

        flag = false;
        if(true) goto _L4; else goto _L3
_L3:
    }

    float distanceInfluenceForSnapDuration(float f)
    {
        return (float)Math.sin((float)((double)(f - 0.5F) * 0.4712389167638204D));
    }

    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        int i = 0;
        int j = 0;
        int k = ViewCompat.getOverScrollMode(this);
        if(k == 0 || k == 1 && mAdapter != null && mAdapter.getCount() > 1)
        {
            if(!mLeftEdge.isFinished())
            {
                i = canvas.save();
                j = getHeight() - getPaddingTop() - getPaddingBottom();
                int l = getWidth();
                canvas.rotate(270F);
                canvas.translate(-j + getPaddingTop(), mFirstOffset * (float)l);
                mLeftEdge.setSize(j, l);
                j = false | mLeftEdge.draw(canvas);
                canvas.restoreToCount(i);
            }
            i = j;
            if(!mRightEdge.isFinished())
            {
                int i1 = canvas.save();
                int j1 = getWidth();
                int k1 = getHeight();
                int l1 = getPaddingTop();
                i = getPaddingBottom();
                canvas.rotate(90F);
                canvas.translate(-getPaddingTop(), -(mLastOffset + 1.0F) * (float)j1);
                mRightEdge.setSize(k1 - l1 - i, j1);
                i = j | mRightEdge.draw(canvas);
                canvas.restoreToCount(i1);
            }
        } else
        {
            mLeftEdge.finish();
            mRightEdge.finish();
        }
        if(i != 0)
            ViewCompat.postInvalidateOnAnimation(this);
    }

    protected void drawableStateChanged()
    {
        super.drawableStateChanged();
        Drawable drawable = mMarginDrawable;
        if(drawable != null && drawable.isStateful())
            drawable.setState(getDrawableState());
    }

    public void endFakeDrag()
    {
        if(!mFakeDragging)
            throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        if(mAdapter != null)
        {
            Object obj = mVelocityTracker;
            ((VelocityTracker) (obj)).computeCurrentVelocity(1000, mMaximumVelocity);
            int i = (int)VelocityTrackerCompat.getXVelocity(((VelocityTracker) (obj)), mActivePointerId);
            mPopulatePending = true;
            int j = getClientWidth();
            int k = getScrollX();
            obj = infoForCurrentScrollPosition();
            setCurrentItemInternal(determineTargetPage(((ItemInfo) (obj)).position, ((float)k / (float)j - ((ItemInfo) (obj)).offset) / ((ItemInfo) (obj)).widthFactor, i, (int)(mLastMotionX - mInitialMotionX)), true, true, i);
        }
        endDrag();
        mFakeDragging = false;
    }

    public boolean executeKeyEvent(KeyEvent keyevent)
    {
        boolean flag;
        boolean flag1;
        flag = false;
        flag1 = flag;
        if(keyevent.getAction() != 0) goto _L2; else goto _L1
_L1:
        keyevent.getKeyCode();
        JVM INSTR lookupswitch 3: default 48
    //                   21: 52
    //                   22: 62
    //                   61: 72;
           goto _L3 _L4 _L5 _L6
_L3:
        flag1 = flag;
_L2:
        return flag1;
_L4:
        flag1 = arrowScroll(17);
        continue; /* Loop/switch isn't completed */
_L5:
        flag1 = arrowScroll(66);
        continue; /* Loop/switch isn't completed */
_L6:
        flag1 = flag;
        if(android.os.Build.VERSION.SDK_INT >= 11)
            if(KeyEventCompat.hasNoModifiers(keyevent))
            {
                flag1 = arrowScroll(2);
            } else
            {
                flag1 = flag;
                if(KeyEventCompat.hasModifiers(keyevent, 1))
                    flag1 = arrowScroll(1);
            }
        if(true) goto _L2; else goto _L7
_L7:
    }

    public void fakeDragBy(float f)
    {
        if(!mFakeDragging)
            throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        if(mAdapter != null) goto _L2; else goto _L1
_L1:
        return;
_L2:
        float f1;
        float f2;
        mLastMotionX = mLastMotionX + f;
        f1 = (float)getScrollX() - f;
        int i = getClientWidth();
        f = (float)i * mFirstOffset;
        f2 = (float)i * mLastOffset;
        ItemInfo iteminfo = (ItemInfo)mItems.get(0);
        ItemInfo iteminfo1 = (ItemInfo)mItems.get(mItems.size() - 1);
        if(iteminfo.position != 0)
            f = iteminfo.offset * (float)i;
        if(iteminfo1.position != mAdapter.getCount() - 1)
            f2 = iteminfo1.offset * (float)i;
        if(f1 >= f)
            break; /* Loop/switch isn't completed */
_L4:
        mLastMotionX = mLastMotionX + (f - (float)(int)f);
        scrollTo((int)f, getScrollY());
        pageScrolled((int)f);
        long l = SystemClock.uptimeMillis();
        MotionEvent motionevent = MotionEvent.obtain(mFakeDragBeginTime, l, 2, mLastMotionX, 0.0F, 0);
        mVelocityTracker.addMovement(motionevent);
        motionevent.recycle();
        if(true) goto _L1; else goto _L3
_L3:
        f = f1;
        if(f1 > f2)
            f = f2;
          goto _L4
        if(true) goto _L1; else goto _L5
_L5:
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
        return generateDefaultLayoutParams();
    }

    public PagerAdapter getAdapter()
    {
        return mAdapter;
    }

    protected int getChildDrawingOrder(int i, int j)
    {
        if(mDrawingOrder == 2)
            j = i - 1 - j;
        return ((LayoutParams)((View)mDrawingOrderedChildren.get(j)).getLayoutParams()).childIndex;
    }

    public int getCurrentItem()
    {
        return mCurItem;
    }

    public int getOffscreenPageLimit()
    {
        return mOffscreenPageLimit;
    }

    public int getPageMargin()
    {
        return mPageMargin;
    }

    ItemInfo infoForAnyChild(View view)
    {
_L3:
        ViewParent viewparent;
        viewparent = view.getParent();
        if(viewparent == this)
            break MISSING_BLOCK_LABEL_33;
        if(viewparent != null && (viewparent instanceof View)) goto _L2; else goto _L1
_L1:
        view = null;
_L4:
        return view;
_L2:
        view = (View)viewparent;
          goto _L3
        view = infoForChild(view);
          goto _L4
    }

    ItemInfo infoForChild(View view)
    {
        int i = 0;
_L3:
        ItemInfo iteminfo;
        if(i >= mItems.size())
            break MISSING_BLOCK_LABEL_50;
        iteminfo = (ItemInfo)mItems.get(i);
        if(!mAdapter.isViewFromObject(view, iteminfo.object)) goto _L2; else goto _L1
_L1:
        view = iteminfo;
_L4:
        return view;
_L2:
        i++;
          goto _L3
        view = null;
          goto _L4
    }

    ItemInfo infoForPosition(int i)
    {
        int j = 0;
_L3:
        ItemInfo iteminfo;
        if(j >= mItems.size())
            break MISSING_BLOCK_LABEL_41;
        iteminfo = (ItemInfo)mItems.get(j);
        if(iteminfo.position != i) goto _L2; else goto _L1
_L1:
        return iteminfo;
_L2:
        j++;
          goto _L3
        iteminfo = null;
          goto _L1
    }

    void initViewPager()
    {
        setWillNotDraw(false);
        setDescendantFocusability(0x40000);
        setFocusable(true);
        Context context = getContext();
        mScroller = new Scroller(context, sInterpolator);
        ViewConfiguration viewconfiguration = ViewConfiguration.get(context);
        float f = context.getResources().getDisplayMetrics().density;
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(viewconfiguration);
        mMinimumVelocity = (int)(400F * f);
        mMaximumVelocity = viewconfiguration.getScaledMaximumFlingVelocity();
        mLeftEdge = new EdgeEffectCompat(context);
        mRightEdge = new EdgeEffectCompat(context);
        mFlingDistance = (int)(25F * f);
        mCloseEnough = (int)(2.0F * f);
        mDefaultGutterSize = (int)(16F * f);
        ViewCompat.setAccessibilityDelegate(this, new MyAccessibilityDelegate());
        if(ViewCompat.getImportantForAccessibility(this) == 0)
            ViewCompat.setImportantForAccessibility(this, 1);
        ViewCompat.setOnApplyWindowInsetsListener(this, new OnApplyWindowInsetsListener() {

            public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowinsetscompat)
            {
                view = ViewCompat.onApplyWindowInsets(view, windowinsetscompat);
                if(!view.isConsumed())
                {
                    windowinsetscompat = mTempRect;
                    windowinsetscompat.left = view.getSystemWindowInsetLeft();
                    windowinsetscompat.top = view.getSystemWindowInsetTop();
                    windowinsetscompat.right = view.getSystemWindowInsetRight();
                    windowinsetscompat.bottom = view.getSystemWindowInsetBottom();
                    int i = 0;
                    for(int j = getChildCount(); i < j; i++)
                    {
                        WindowInsetsCompat windowinsetscompat1 = ViewCompat.dispatchApplyWindowInsets(getChildAt(i), view);
                        windowinsetscompat.left = Math.min(windowinsetscompat1.getSystemWindowInsetLeft(), ((Rect) (windowinsetscompat)).left);
                        windowinsetscompat.top = Math.min(windowinsetscompat1.getSystemWindowInsetTop(), ((Rect) (windowinsetscompat)).top);
                        windowinsetscompat.right = Math.min(windowinsetscompat1.getSystemWindowInsetRight(), ((Rect) (windowinsetscompat)).right);
                        windowinsetscompat.bottom = Math.min(windowinsetscompat1.getSystemWindowInsetBottom(), ((Rect) (windowinsetscompat)).bottom);
                    }

                    view = view.replaceSystemWindowInsets(((Rect) (windowinsetscompat)).left, ((Rect) (windowinsetscompat)).top, ((Rect) (windowinsetscompat)).right, ((Rect) (windowinsetscompat)).bottom);
                }
                return view;
            }

            private final Rect mTempRect = new Rect();
            final ViewPager this$0;

            
            {
                this$0 = ViewPager.this;
                super();
            }
        }
);
    }

    public boolean isFakeDragging()
    {
        return mFakeDragging;
    }

    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        mFirstLayout = true;
    }

    protected void onDetachedFromWindow()
    {
        removeCallbacks(mEndScrollRunnable);
        if(mScroller != null && !mScroller.isFinished())
            mScroller.abortAnimation();
        super.onDetachedFromWindow();
    }

    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if(mPageMargin <= 0 || mMarginDrawable == null || mItems.size() <= 0 || mAdapter == null) goto _L2; else goto _L1
_L1:
        int i;
        int j;
        float f;
        int k;
        Object obj;
        float f1;
        int l;
        int i1;
        int j1;
        i = getScrollX();
        j = getWidth();
        f = (float)mPageMargin / (float)j;
        k = 0;
        obj = (ItemInfo)mItems.get(0);
        f1 = ((ItemInfo) (obj)).offset;
        l = mItems.size();
        i1 = ((ItemInfo) (obj)).position;
        j1 = ((ItemInfo)mItems.get(l - 1)).position;
_L6:
        if(i1 >= j1) goto _L2; else goto _L3
_L3:
        for(; i1 > ((ItemInfo) (obj)).position && k < l; obj = (ItemInfo)((ArrayList) (obj)).get(k))
        {
            obj = mItems;
            k++;
        }

        float f2;
        if(i1 == ((ItemInfo) (obj)).position)
        {
            f2 = (((ItemInfo) (obj)).offset + ((ItemInfo) (obj)).widthFactor) * (float)j;
            f1 = ((ItemInfo) (obj)).offset + ((ItemInfo) (obj)).widthFactor + f;
        } else
        {
            float f3 = mAdapter.getPageWidth(i1);
            f2 = (f1 + f3) * (float)j;
            f1 += f3 + f;
        }
        if((float)mPageMargin + f2 > (float)i)
        {
            mMarginDrawable.setBounds((int)f2, mTopPageBounds, (int)((float)mPageMargin + f2 + 0.5F), mBottomPageBounds);
            mMarginDrawable.draw(canvas);
        }
        if(f2 <= (float)(i + j)) goto _L4; else goto _L2
_L2:
        return;
_L4:
        i1++;
        if(true) goto _L6; else goto _L5
_L5:
    }

    public boolean onInterceptTouchEvent(MotionEvent motionevent)
    {
        int i;
        boolean flag;
        i = motionevent.getAction() & 0xff;
        if(i == 3 || i == 1)
        {
            resetTouch();
            flag = false;
        } else
        {
label0:
            {
                if(i == 0)
                    break label0;
                if(mIsBeingDragged)
                {
                    flag = true;
                } else
                {
                    if(!mIsUnableToDrag)
                        break label0;
                    flag = false;
                }
            }
        }
_L5:
        return flag;
        i;
        JVM INSTR lookupswitch 3: default 92
    //                   0: 371
    //                   2: 122
    //                   6: 514;
           goto _L1 _L2 _L3 _L4
_L1:
        break; /* Loop/switch isn't completed */
_L4:
        break MISSING_BLOCK_LABEL_514;
_L7:
        if(mVelocityTracker == null)
            mVelocityTracker = VelocityTracker.obtain();
        mVelocityTracker.addMovement(motionevent);
        flag = mIsBeingDragged;
          goto _L5
_L3:
        i = mActivePointerId;
        if(i == -1) goto _L7; else goto _L6
_L6:
        float f;
        float f1;
        float f2;
        float f5;
        float f6;
label1:
        {
            i = MotionEventCompat.findPointerIndex(motionevent, i);
            f = MotionEventCompat.getX(motionevent, i);
            f1 = f - mLastMotionX;
            f2 = Math.abs(f1);
            f5 = MotionEventCompat.getY(motionevent, i);
            f6 = Math.abs(f5 - mInitialMotionY);
            if(f1 == 0.0F || isGutterDrag(mLastMotionX, f1) || !canScroll(this, false, (int)f1, (int)f, (int)f5))
                break label1;
            mLastMotionX = f;
            mLastMotionY = f5;
            mIsUnableToDrag = true;
            flag = false;
        }
          goto _L5
        if(f2 > (float)mTouchSlop && 0.5F * f2 > f6)
        {
            mIsBeingDragged = true;
            requestParentDisallowInterceptTouchEvent(true);
            setScrollState(1);
            float f3;
            if(f1 > 0.0F)
                f3 = mInitialMotionX + (float)mTouchSlop;
            else
                f3 = mInitialMotionX - (float)mTouchSlop;
            mLastMotionX = f3;
            mLastMotionY = f5;
            setScrollingCacheEnabled(true);
        } else
        if(f6 > (float)mTouchSlop)
            mIsUnableToDrag = true;
        if(mIsBeingDragged && performDrag(f))
            ViewCompat.postInvalidateOnAnimation(this);
          goto _L7
_L2:
        float f4 = motionevent.getX();
        mInitialMotionX = f4;
        mLastMotionX = f4;
        f4 = motionevent.getY();
        mInitialMotionY = f4;
        mLastMotionY = f4;
        mActivePointerId = MotionEventCompat.getPointerId(motionevent, 0);
        mIsUnableToDrag = false;
        mIsScrollStarted = true;
        mScroller.computeScrollOffset();
        if(mScrollState == 2 && Math.abs(mScroller.getFinalX() - mScroller.getCurrX()) > mCloseEnough)
        {
            mScroller.abortAnimation();
            mPopulatePending = false;
            populate();
            mIsBeingDragged = true;
            requestParentDisallowInterceptTouchEvent(true);
            setScrollState(1);
        } else
        {
            completeScroll(false);
            mIsBeingDragged = false;
        }
          goto _L7
        onSecondaryPointerUp(motionevent);
          goto _L7
    }

    protected void onLayout(boolean flag, int i, int j, int k, int l)
    {
        int i1;
        int j1;
        int k1;
        int l1;
        int j2;
        int k2;
        int l2;
        i1 = getChildCount();
        j1 = k - i;
        k1 = l - j;
        j = getPaddingLeft();
        i = getPaddingTop();
        l1 = getPaddingRight();
        l = getPaddingBottom();
        j2 = getScrollX();
        k2 = 0;
        l2 = 0;
_L17:
        if(l2 >= i1) goto _L2; else goto _L1
_L1:
        View view;
        int i3;
        int j3;
        int k3;
        int i4;
        view = getChildAt(l2);
        i3 = k2;
        j3 = l;
        k3 = j;
        i4 = l1;
        k = i;
        if(view.getVisibility() == 8) goto _L4; else goto _L3
_L3:
        LayoutParams layoutparams;
        layoutparams = (LayoutParams)view.getLayoutParams();
        i3 = k2;
        j3 = l;
        k3 = j;
        i4 = l1;
        k = i;
        if(!layoutparams.isDecor) goto _L4; else goto _L5
_L5:
        k = layoutparams.gravity;
        i4 = layoutparams.gravity;
        k & 7;
        JVM INSTR tableswitch 1 5: default 184
    //                   1 316
    //                   2 184
    //                   3 301
    //                   4 184
    //                   5 338;
           goto _L6 _L7 _L6 _L8 _L6 _L9
_L6:
        k = j;
        k3 = j;
_L14:
        i4 & 0x70;
        JVM INSTR lookupswitch 3: default 228
    //                   16: 380
    //                   48: 367
    //                   80: 398;
           goto _L10 _L11 _L12 _L13
_L10:
        j = i;
_L15:
        k += j2;
        view.layout(k, j, view.getMeasuredWidth() + k, view.getMeasuredHeight() + j);
        i3 = k2 + 1;
        k = i;
        i4 = l1;
        j3 = l;
_L4:
        l2++;
        k2 = i3;
        l = j3;
        j = k3;
        l1 = i4;
        i = k;
        continue; /* Loop/switch isn't completed */
_L8:
        k = j;
        k3 = j + view.getMeasuredWidth();
          goto _L14
_L7:
        k = Math.max((j1 - view.getMeasuredWidth()) / 2, j);
        k3 = j;
          goto _L14
_L9:
        k = j1 - l1 - view.getMeasuredWidth();
        l1 += view.getMeasuredWidth();
        k3 = j;
          goto _L14
_L12:
        j = i;
        i += view.getMeasuredHeight();
          goto _L15
_L11:
        j = Math.max((k1 - view.getMeasuredHeight()) / 2, i);
          goto _L15
_L13:
        j = k1 - l - view.getMeasuredHeight();
        l += view.getMeasuredHeight();
          goto _L15
_L2:
        int l3 = j1 - j - l1;
        for(k = 0; k < i1; k++)
        {
            View view1 = getChildAt(k);
            if(view1.getVisibility() == 8)
                continue;
            LayoutParams layoutparams1 = (LayoutParams)view1.getLayoutParams();
            if(layoutparams1.isDecor)
                continue;
            ItemInfo iteminfo = infoForChild(view1);
            if(iteminfo == null)
                continue;
            int i2 = j + (int)((float)l3 * iteminfo.offset);
            if(layoutparams1.needsMeasure)
            {
                layoutparams1.needsMeasure = false;
                view1.measure(android.view.View.MeasureSpec.makeMeasureSpec((int)((float)l3 * layoutparams1.widthFactor), 0x40000000), android.view.View.MeasureSpec.makeMeasureSpec(k1 - i - l, 0x40000000));
            }
            view1.layout(i2, i, view1.getMeasuredWidth() + i2, view1.getMeasuredHeight() + i);
        }

        mTopPageBounds = i;
        mBottomPageBounds = k1 - l;
        mDecorChildCount = k2;
        if(mFirstLayout)
            scrollToItem(mCurItem, false, 0, false);
        mFirstLayout = false;
        return;
        if(true) goto _L17; else goto _L16
_L16:
    }

    protected void onMeasure(int i, int j)
    {
        setMeasuredDimension(getDefaultSize(0, i), getDefaultSize(0, j));
        i = getMeasuredWidth();
        mGutterSize = Math.min(i / 10, mDefaultGutterSize);
        i = i - getPaddingLeft() - getPaddingRight();
        j = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        int k = getChildCount();
        int l = 0;
        do
        {
            if(l < k)
            {
                View view = getChildAt(l);
                int i1 = j;
                int k1 = i;
                if(view.getVisibility() != 8)
                {
                    LayoutParams layoutparams = (LayoutParams)view.getLayoutParams();
                    i1 = j;
                    k1 = i;
                    if(layoutparams != null)
                    {
                        i1 = j;
                        k1 = i;
                        if(layoutparams.isDecor)
                        {
                            k1 = layoutparams.gravity & 7;
                            int l1 = layoutparams.gravity & 0x70;
                            int i2 = 0x80000000;
                            i1 = 0x80000000;
                            boolean flag;
                            boolean flag1;
                            int j2;
                            int k2;
                            if(l1 == 48 || l1 == 80)
                                flag = true;
                            else
                                flag = false;
                            if(k1 == 3 || k1 == 5)
                                flag1 = true;
                            else
                                flag1 = false;
                            if(flag)
                            {
                                k1 = 0x40000000;
                            } else
                            {
                                k1 = i2;
                                if(flag1)
                                {
                                    i1 = 0x40000000;
                                    k1 = i2;
                                }
                            }
                            j2 = i;
                            i2 = j;
                            k2 = k1;
                            k1 = j2;
                            if(layoutparams.width != -2)
                            {
                                int l2 = 0x40000000;
                                k2 = l2;
                                k1 = j2;
                                if(layoutparams.width != -1)
                                {
                                    k1 = layoutparams.width;
                                    k2 = l2;
                                }
                            }
                            j2 = i2;
                            if(layoutparams.height != -2)
                            {
                                int i3 = 0x40000000;
                                i1 = i3;
                                j2 = i2;
                                if(layoutparams.height != -1)
                                {
                                    j2 = layoutparams.height;
                                    i1 = i3;
                                }
                            }
                            view.measure(android.view.View.MeasureSpec.makeMeasureSpec(k1, k2), android.view.View.MeasureSpec.makeMeasureSpec(j2, i1));
                            if(flag)
                            {
                                i1 = j - view.getMeasuredHeight();
                                k1 = i;
                            } else
                            {
                                i1 = j;
                                k1 = i;
                                if(flag1)
                                {
                                    k1 = i - view.getMeasuredWidth();
                                    i1 = j;
                                }
                            }
                        }
                    }
                }
                l++;
                j = i1;
                i = k1;
                continue;
            }
            mChildWidthMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(i, 0x40000000);
            mChildHeightMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(j, 0x40000000);
            mInLayout = true;
            populate();
            mInLayout = false;
            int j1 = getChildCount();
            for(j = 0; j < j1; j++)
            {
                View view1 = getChildAt(j);
                if(view1.getVisibility() == 8)
                    continue;
                LayoutParams layoutparams1 = (LayoutParams)view1.getLayoutParams();
                if(layoutparams1 == null || !layoutparams1.isDecor)
                    view1.measure(android.view.View.MeasureSpec.makeMeasureSpec((int)((float)i * layoutparams1.widthFactor), 0x40000000), mChildHeightMeasureSpec);
            }

            return;
        } while(true);
    }

    protected void onPageScrolled(int i, float f, int j)
    {
        int k;
        int l;
        int j1;
        int k1;
        int l1;
        int i2;
        if(mDecorChildCount <= 0)
            break MISSING_BLOCK_LABEL_247;
        k = getScrollX();
        l = getPaddingLeft();
        j1 = getPaddingRight();
        k1 = getWidth();
        l1 = getChildCount();
        i2 = 0;
_L2:
        View view;
        LayoutParams layoutparams;
        int j2;
        int k2;
        if(i2 >= l1)
            break MISSING_BLOCK_LABEL_247;
        view = getChildAt(i2);
        layoutparams = (LayoutParams)view.getLayoutParams();
        if(layoutparams.isDecor)
            break; /* Loop/switch isn't completed */
        j2 = j1;
        k2 = l;
_L7:
        i2++;
        l = k2;
        j1 = j2;
        if(true) goto _L2; else goto _L1
_L1:
        layoutparams.gravity & 7;
        JVM INSTR tableswitch 1 5: default 136
    //                   1 201
    //                   2 136
    //                   3 184
    //                   4 136
    //                   5 221;
           goto _L3 _L4 _L3 _L5 _L3 _L6
_L6:
        break MISSING_BLOCK_LABEL_221;
_L3:
        j2 = l;
_L8:
        int l2 = (j2 + k) - view.getLeft();
        k2 = l;
        j2 = j1;
        if(l2 != 0)
        {
            view.offsetLeftAndRight(l2);
            k2 = l;
            j2 = j1;
        }
          goto _L7
_L5:
        j2 = l;
        l += view.getWidth();
          goto _L8
_L4:
        j2 = Math.max((k1 - view.getMeasuredWidth()) / 2, l);
          goto _L8
        j2 = k1 - j1 - view.getMeasuredWidth();
        j1 += view.getMeasuredWidth();
          goto _L8
        dispatchOnPageScrolled(i, f, j);
        if(mPageTransformer != null)
        {
            int i1 = getScrollX();
            j = getChildCount();
            i = 0;
            while(i < j) 
            {
                View view1 = getChildAt(i);
                if(!((LayoutParams)view1.getLayoutParams()).isDecor)
                {
                    f = (float)(view1.getLeft() - i1) / (float)getClientWidth();
                    mPageTransformer.transformPage(view1, f);
                }
                i++;
            }
        }
        mCalledSuper = true;
        return;
          goto _L7
    }

    protected boolean onRequestFocusInDescendants(int i, Rect rect)
    {
        int k;
        byte byte0;
        boolean flag;
        int j = getChildCount();
        View view;
        ItemInfo iteminfo;
        if((i & 2) != 0)
        {
            k = 0;
            byte0 = 1;
        } else
        {
            k = j - 1;
            byte0 = -1;
            j = -1;
        }
_L3:
        if(k == j) goto _L2; else goto _L1
_L1:
        view = getChildAt(k);
        if(view.getVisibility() != 0)
            continue; /* Loop/switch isn't completed */
        iteminfo = infoForChild(view);
        if(iteminfo == null || iteminfo.position != mCurItem || !view.requestFocus(i, rect))
            continue; /* Loop/switch isn't completed */
        flag = true;
_L4:
        return flag;
        k += byte0;
          goto _L3
_L2:
        flag = false;
          goto _L4
    }

    public void onRestoreInstanceState(Parcelable parcelable)
    {
        if(!(parcelable instanceof SavedState))
        {
            super.onRestoreInstanceState(parcelable);
        } else
        {
            parcelable = (SavedState)parcelable;
            super.onRestoreInstanceState(parcelable.getSuperState());
            if(mAdapter != null)
            {
                mAdapter.restoreState(((SavedState) (parcelable)).adapterState, ((SavedState) (parcelable)).loader);
                setCurrentItemInternal(((SavedState) (parcelable)).position, false, true);
            } else
            {
                mRestoredCurItem = ((SavedState) (parcelable)).position;
                mRestoredAdapterState = ((SavedState) (parcelable)).adapterState;
                mRestoredClassLoader = ((SavedState) (parcelable)).loader;
            }
        }
    }

    public Parcelable onSaveInstanceState()
    {
        SavedState savedstate = new SavedState(super.onSaveInstanceState());
        savedstate.position = mCurItem;
        if(mAdapter != null)
            savedstate.adapterState = mAdapter.saveState();
        return savedstate;
    }

    protected void onSizeChanged(int i, int j, int k, int l)
    {
        super.onSizeChanged(i, j, k, l);
        if(i != k)
            recomputeScrollPosition(i, k, mPageMargin, mPageMargin);
    }

    public boolean onTouchEvent(MotionEvent motionevent)
    {
        if(!mFakeDragging) goto _L2; else goto _L1
_L1:
        boolean flag = true;
_L12:
        return flag;
_L2:
        int i;
        boolean flag1;
        if(motionevent.getAction() == 0 && motionevent.getEdgeFlags() != 0)
        {
            flag = false;
            continue; /* Loop/switch isn't completed */
        }
        if(mAdapter == null || mAdapter.getCount() == 0)
        {
            flag = false;
            continue; /* Loop/switch isn't completed */
        }
        if(mVelocityTracker == null)
            mVelocityTracker = VelocityTracker.obtain();
        mVelocityTracker.addMovement(motionevent);
        i = motionevent.getAction();
        flag1 = false;
        flag = flag1;
        i & 0xff;
        JVM INSTR tableswitch 0 6: default 132
    //                   0 148
    //                   1 423
    //                   2 215
    //                   3 549
    //                   4 135
    //                   5 578
    //                   6 607;
           goto _L3 _L4 _L5 _L6 _L7 _L8 _L9 _L10
_L4:
        break; /* Loop/switch isn't completed */
_L10:
        break MISSING_BLOCK_LABEL_607;
_L8:
        break; /* Loop/switch isn't completed */
_L3:
        flag = flag1;
_L13:
        if(flag)
            ViewCompat.postInvalidateOnAnimation(this);
        flag = true;
        if(true) goto _L12; else goto _L11
_L11:
        mScroller.abortAnimation();
        mPopulatePending = false;
        populate();
        float f = motionevent.getX();
        mInitialMotionX = f;
        mLastMotionX = f;
        f = motionevent.getY();
        mInitialMotionY = f;
        mLastMotionY = f;
        mActivePointerId = MotionEventCompat.getPointerId(motionevent, 0);
        flag = flag1;
          goto _L13
_L6:
label0:
        {
            if(mIsBeingDragged)
                break MISSING_BLOCK_LABEL_376;
            i = MotionEventCompat.findPointerIndex(motionevent, mActivePointerId);
            if(i != -1)
                break label0;
            flag = resetTouch();
        }
          goto _L13
        float f3 = MotionEventCompat.getX(motionevent, i);
        float f1 = Math.abs(f3 - mLastMotionX);
        float f4 = MotionEventCompat.getY(motionevent, i);
        float f5 = Math.abs(f4 - mLastMotionY);
        if(f1 > (float)mTouchSlop && f1 > f5)
        {
            mIsBeingDragged = true;
            requestParentDisallowInterceptTouchEvent(true);
            float f2;
            ViewParent viewparent;
            if(f3 - mInitialMotionX > 0.0F)
                f2 = mInitialMotionX + (float)mTouchSlop;
            else
                f2 = mInitialMotionX - (float)mTouchSlop;
            mLastMotionX = f2;
            mLastMotionY = f4;
            setScrollState(1);
            setScrollingCacheEnabled(true);
            viewparent = getParent();
            if(viewparent != null)
                viewparent.requestDisallowInterceptTouchEvent(true);
        }
        flag = flag1;
        if(mIsBeingDragged)
            flag = false | performDrag(MotionEventCompat.getX(motionevent, MotionEventCompat.findPointerIndex(motionevent, mActivePointerId)));
          goto _L13
_L5:
        flag = flag1;
        if(mIsBeingDragged)
        {
            Object obj = mVelocityTracker;
            ((VelocityTracker) (obj)).computeCurrentVelocity(1000, mMaximumVelocity);
            int l = (int)VelocityTrackerCompat.getXVelocity(((VelocityTracker) (obj)), mActivePointerId);
            mPopulatePending = true;
            int i1 = getClientWidth();
            int j = getScrollX();
            obj = infoForCurrentScrollPosition();
            setCurrentItemInternal(determineTargetPage(((ItemInfo) (obj)).position, ((float)j / (float)i1 - ((ItemInfo) (obj)).offset) / ((ItemInfo) (obj)).widthFactor, l, (int)(MotionEventCompat.getX(motionevent, MotionEventCompat.findPointerIndex(motionevent, mActivePointerId)) - mInitialMotionX)), true, true, l);
            flag = resetTouch();
        }
          goto _L13
_L7:
        flag = flag1;
        if(mIsBeingDragged)
        {
            scrollToItem(mCurItem, true, 0, false);
            flag = resetTouch();
        }
          goto _L13
_L9:
        int k = MotionEventCompat.getActionIndex(motionevent);
        mLastMotionX = MotionEventCompat.getX(motionevent, k);
        mActivePointerId = MotionEventCompat.getPointerId(motionevent, k);
        flag = flag1;
          goto _L13
        onSecondaryPointerUp(motionevent);
        mLastMotionX = MotionEventCompat.getX(motionevent, MotionEventCompat.findPointerIndex(motionevent, mActivePointerId));
        flag = flag1;
          goto _L13
    }

    boolean pageLeft()
    {
        boolean flag = true;
        if(mCurItem > 0)
            setCurrentItem(mCurItem - 1, true);
        else
            flag = false;
        return flag;
    }

    boolean pageRight()
    {
        boolean flag = true;
        if(mAdapter != null && mCurItem < mAdapter.getCount() - 1)
            setCurrentItem(mCurItem + 1, true);
        else
            flag = false;
        return flag;
    }

    void populate()
    {
        populate(mCurItem);
    }

    void populate(int i)
    {
        Object obj;
        obj = null;
        if(mCurItem != i)
        {
            obj = infoForPosition(mCurItem);
            mCurItem = i;
        }
        if(mAdapter != null) goto _L2; else goto _L1
_L1:
        sortChildDrawingOrder();
_L4:
        return;
_L2:
        if(!mPopulatePending)
            break; /* Loop/switch isn't completed */
        sortChildDrawingOrder();
        if(true) goto _L4; else goto _L3
_L3:
        if(getWindowToken() == null) goto _L4; else goto _L5
_L5:
        int j;
        int k;
        int l;
        ItemInfo iteminfo1;
        mAdapter.startUpdate(this);
        i = mOffscreenPageLimit;
        j = Math.max(0, mCurItem - i);
        k = mAdapter.getCount();
        l = Math.min(k - 1, mCurItem + i);
        if(k != mExpectedAdapterCount)
        {
            String s;
            try
            {
                s = getResources().getResourceName(getId());
            }
            catch(android.content.res.Resources.NotFoundException notfoundexception)
            {
                notfoundexception = Integer.toHexString(getId());
            }
            throw new IllegalStateException((new StringBuilder()).append("The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: ").append(mExpectedAdapterCount).append(", found: ").append(k).append(" Pager id: ").append(s).append(" Pager class: ").append(getClass()).append(" Problematic adapter: ").append(mAdapter.getClass()).toString());
        }
        iteminfo1 = null;
        i = 0;
_L21:
        Object obj1 = iteminfo1;
        if(i >= mItems.size()) goto _L7; else goto _L6
_L6:
        ItemInfo iteminfo2 = (ItemInfo)mItems.get(i);
        if(iteminfo2.position < mCurItem) goto _L9; else goto _L8
_L8:
        obj1 = iteminfo1;
        if(iteminfo2.position == mCurItem)
            obj1 = iteminfo2;
_L7:
        iteminfo2 = ((ItemInfo) (obj1));
        if(obj1 == null)
        {
            iteminfo2 = ((ItemInfo) (obj1));
            if(k > 0)
                iteminfo2 = addNewItem(mCurItem, i);
        }
        if(iteminfo2 == null) goto _L11; else goto _L10
_L10:
        float f;
        int i1;
        int j1;
        float f1;
        int k1;
        int l1;
        int i2;
        f = 0.0F;
        i1 = i - 1;
        if(i1 >= 0)
            obj1 = (ItemInfo)mItems.get(i1);
        else
            obj1 = null;
        j1 = getClientWidth();
        if(j1 <= 0)
            f1 = 0.0F;
        else
            f1 = (2.0F - iteminfo2.widthFactor) + (float)getPaddingLeft() / (float)j1;
        k1 = mCurItem - 1;
        iteminfo1 = ((ItemInfo) (obj1));
        l1 = i;
_L22:
        if(k1 < 0) goto _L13; else goto _L12
_L12:
        if(f < f1 || k1 >= j) goto _L15; else goto _L14
_L14:
        if(iteminfo1 != null) goto _L16; else goto _L13
_L13:
        f = iteminfo2.widthFactor;
        k1 = l1 + 1;
        if(f >= 2.0F) goto _L18; else goto _L17
_L17:
        float f2;
        if(k1 < mItems.size())
            obj1 = (ItemInfo)mItems.get(k1);
        else
            obj1 = null;
        if(j1 <= 0)
            f1 = 0.0F;
        else
            f1 = (float)getPaddingRight() / (float)j1 + 2.0F;
        i2 = mCurItem + 1;
        iteminfo1 = ((ItemInfo) (obj1));
_L24:
        if(i2 >= k) goto _L18; else goto _L19
_L19:
        if(f < f1 || i2 <= l)
            break MISSING_BLOCK_LABEL_1068;
        if(iteminfo1 != null) goto _L20; else goto _L18
_L18:
        calculatePageOffsets(iteminfo2, l1, ((ItemInfo) (obj)));
_L11:
        obj = mAdapter;
        i = mCurItem;
        float f3;
        if(iteminfo2 != null)
            obj1 = iteminfo2.object;
        else
            obj1 = null;
        ((PagerAdapter) (obj)).setPrimaryItem(this, i, obj1);
        mAdapter.finishUpdate(this);
        i2 = getChildCount();
        for(i = 0; i < i2; i++)
        {
            obj = getChildAt(i);
            obj1 = (LayoutParams)((View) (obj)).getLayoutParams();
            obj1.childIndex = i;
            if(!((LayoutParams) (obj1)).isDecor && ((LayoutParams) (obj1)).widthFactor == 0.0F)
            {
                obj = infoForChild(((View) (obj)));
                if(obj != null)
                {
                    obj1.widthFactor = ((ItemInfo) (obj)).widthFactor;
                    obj1.position = ((ItemInfo) (obj)).position;
                }
            }
        }

        break MISSING_BLOCK_LABEL_1195;
_L9:
        i++;
          goto _L21
_L16:
        i = l1;
        f2 = f;
        obj1 = iteminfo1;
        i2 = i1;
        if(k1 == iteminfo1.position)
        {
            i = l1;
            f2 = f;
            obj1 = iteminfo1;
            i2 = i1;
            if(!iteminfo1.scrolling)
            {
                mItems.remove(i1);
                mAdapter.destroyItem(this, k1, iteminfo1.object);
                i2 = i1 - 1;
                i = l1 - 1;
                if(i2 >= 0)
                {
                    obj1 = (ItemInfo)mItems.get(i2);
                    f2 = f;
                } else
                {
                    obj1 = null;
                    f2 = f;
                }
            }
        }
_L23:
        k1--;
        l1 = i;
        f = f2;
        iteminfo1 = ((ItemInfo) (obj1));
        i1 = i2;
          goto _L22
_L15:
        if(iteminfo1 != null && k1 == iteminfo1.position)
        {
            f2 = f + iteminfo1.widthFactor;
            i2 = i1 - 1;
            if(i2 >= 0)
                obj1 = (ItemInfo)mItems.get(i2);
            else
                obj1 = null;
            i = l1;
        } else
        {
            f2 = f + addNewItem(k1, i1 + 1).widthFactor;
            i = l1 + 1;
            if(i1 >= 0)
                obj1 = (ItemInfo)mItems.get(i1);
            else
                obj1 = null;
            i2 = i1;
        }
          goto _L23
_L20:
        f3 = f;
        obj1 = iteminfo1;
        i = k1;
        if(i2 == iteminfo1.position)
        {
            f3 = f;
            obj1 = iteminfo1;
            i = k1;
            if(!iteminfo1.scrolling)
            {
                mItems.remove(k1);
                mAdapter.destroyItem(this, i2, iteminfo1.object);
                if(k1 < mItems.size())
                {
                    obj1 = (ItemInfo)mItems.get(k1);
                    i = k1;
                    f3 = f;
                } else
                {
                    obj1 = null;
                    f3 = f;
                    i = k1;
                }
            }
        }
_L25:
        i2++;
        f = f3;
        iteminfo1 = ((ItemInfo) (obj1));
        k1 = i;
          goto _L24
        if(iteminfo1 != null && i2 == iteminfo1.position)
        {
            f3 = f + iteminfo1.widthFactor;
            i = k1 + 1;
            if(i < mItems.size())
                obj1 = (ItemInfo)mItems.get(i);
            else
                obj1 = null;
        } else
        {
            obj1 = addNewItem(i2, k1);
            i = k1 + 1;
            f3 = f + ((ItemInfo) (obj1)).widthFactor;
            if(i < mItems.size())
                obj1 = (ItemInfo)mItems.get(i);
            else
                obj1 = null;
        }
          goto _L25
        sortChildDrawingOrder();
        if(!hasFocus()) goto _L4; else goto _L26
_L26:
        Object obj2 = findFocus();
        ItemInfo iteminfo;
        if(obj2 != null)
            obj2 = infoForAnyChild(((View) (obj2)));
        else
            obj2 = null;
        if(obj2 != null && ((ItemInfo) (obj2)).position == mCurItem) goto _L4; else goto _L27
_L27:
        i = 0;
_L30:
        if(i >= getChildCount()) goto _L4; else goto _L28
_L28:
        obj2 = getChildAt(i);
        iteminfo = infoForChild(((View) (obj2)));
        if(iteminfo != null && iteminfo.position == mCurItem && ((View) (obj2)).requestFocus(2)) goto _L4; else goto _L29
_L29:
        i++;
          goto _L30
    }

    public void removeOnPageChangeListener(OnPageChangeListener onpagechangelistener)
    {
        if(mOnPageChangeListeners != null)
            mOnPageChangeListeners.remove(onpagechangelistener);
    }

    public void removeView(View view)
    {
        if(mInLayout)
            removeViewInLayout(view);
        else
            super.removeView(view);
    }

    public void setAdapter(PagerAdapter pageradapter)
    {
        if(mAdapter != null)
        {
            mAdapter.setViewPagerObserver(null);
            mAdapter.startUpdate(this);
            for(int i = 0; i < mItems.size(); i++)
            {
                ItemInfo iteminfo = (ItemInfo)mItems.get(i);
                mAdapter.destroyItem(this, iteminfo.position, iteminfo.object);
            }

            mAdapter.finishUpdate(this);
            mItems.clear();
            removeNonDecorViews();
            mCurItem = 0;
            scrollTo(0, 0);
        }
        PagerAdapter pageradapter1 = mAdapter;
        mAdapter = pageradapter;
        mExpectedAdapterCount = 0;
        if(mAdapter != null)
        {
            if(mObserver == null)
                mObserver = new PagerObserver();
            mAdapter.setViewPagerObserver(mObserver);
            mPopulatePending = false;
            boolean flag = mFirstLayout;
            mFirstLayout = true;
            mExpectedAdapterCount = mAdapter.getCount();
            if(mRestoredCurItem >= 0)
            {
                mAdapter.restoreState(mRestoredAdapterState, mRestoredClassLoader);
                setCurrentItemInternal(mRestoredCurItem, false, true);
                mRestoredCurItem = -1;
                mRestoredAdapterState = null;
                mRestoredClassLoader = null;
            } else
            if(!flag)
                populate();
            else
                requestLayout();
        }
        if(mAdapterChangeListener != null && pageradapter1 != pageradapter)
            mAdapterChangeListener.onAdapterChanged(pageradapter1, pageradapter);
    }

    void setChildrenDrawingOrderEnabledCompat(boolean flag)
    {
        if(android.os.Build.VERSION.SDK_INT < 7)
            break MISSING_BLOCK_LABEL_57;
        if(mSetChildrenDrawingOrderEnabled == null)
            try
            {
                mSetChildrenDrawingOrderEnabled = android/view/ViewGroup.getDeclaredMethod("setChildrenDrawingOrderEnabled", new Class[] {
                    Boolean.TYPE
                });
            }
            catch(NoSuchMethodException nosuchmethodexception)
            {
                Log.e("ViewPager", "Can't find setChildrenDrawingOrderEnabled", nosuchmethodexception);
            }
        mSetChildrenDrawingOrderEnabled.invoke(this, new Object[] {
            Boolean.valueOf(flag)
        });
_L1:
        return;
        Exception exception;
        exception;
        Log.e("ViewPager", "Error changing children drawing order", exception);
          goto _L1
    }

    public void setCurrentItem(int i)
    {
        mPopulatePending = false;
        boolean flag;
        if(!mFirstLayout)
            flag = true;
        else
            flag = false;
        setCurrentItemInternal(i, flag, false);
    }

    public void setCurrentItem(int i, boolean flag)
    {
        mPopulatePending = false;
        setCurrentItemInternal(i, flag, false);
    }

    void setCurrentItemInternal(int i, boolean flag, boolean flag1)
    {
        setCurrentItemInternal(i, flag, flag1, 0);
    }

    void setCurrentItemInternal(int i, boolean flag, boolean flag1, int j)
    {
        boolean flag2 = true;
        if(mAdapter != null && mAdapter.getCount() > 0) goto _L2; else goto _L1
_L1:
        setScrollingCacheEnabled(false);
_L8:
        return;
_L2:
        if(!flag1 && mCurItem == i && mItems.size() != 0)
        {
            setScrollingCacheEnabled(false);
            continue; /* Loop/switch isn't completed */
        }
        if(i >= 0) goto _L4; else goto _L3
_L3:
        int k = 0;
_L6:
        i = mOffscreenPageLimit;
        if(k > mCurItem + i || k < mCurItem - i)
            for(i = 0; i < mItems.size(); i++)
                ((ItemInfo)mItems.get(i)).scrolling = true;

        break; /* Loop/switch isn't completed */
_L4:
        k = i;
        if(i >= mAdapter.getCount())
            k = mAdapter.getCount() - 1;
        if(true) goto _L6; else goto _L5
_L5:
        if(mCurItem != k)
            flag1 = flag2;
        else
            flag1 = false;
        if(mFirstLayout)
        {
            mCurItem = k;
            if(flag1)
                dispatchOnPageSelected(k);
            requestLayout();
        } else
        {
            populate(k);
            scrollToItem(k, flag, j, flag1);
        }
        if(true) goto _L8; else goto _L7
_L7:
    }

    OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener onpagechangelistener)
    {
        OnPageChangeListener onpagechangelistener1 = mInternalPageChangeListener;
        mInternalPageChangeListener = onpagechangelistener;
        return onpagechangelistener1;
    }

    public void setOffscreenPageLimit(int i)
    {
        int j = i;
        if(i < 1)
        {
            Log.w("ViewPager", (new StringBuilder()).append("Requested offscreen page limit ").append(i).append(" too small; defaulting to ").append(1).toString());
            j = 1;
        }
        if(j != mOffscreenPageLimit)
        {
            mOffscreenPageLimit = j;
            populate();
        }
    }

    void setOnAdapterChangeListener(OnAdapterChangeListener onadapterchangelistener)
    {
        mAdapterChangeListener = onadapterchangelistener;
    }

    public void setOnPageChangeListener(OnPageChangeListener onpagechangelistener)
    {
        mOnPageChangeListener = onpagechangelistener;
    }

    public void setPageMargin(int i)
    {
        int j = mPageMargin;
        mPageMargin = i;
        int k = getWidth();
        recomputeScrollPosition(k, k, i, j);
        requestLayout();
    }

    public void setPageMarginDrawable(int i)
    {
        setPageMarginDrawable(getContext().getResources().getDrawable(i));
    }

    public void setPageMarginDrawable(Drawable drawable)
    {
        mMarginDrawable = drawable;
        if(drawable != null)
            refreshDrawableState();
        boolean flag;
        if(drawable == null)
            flag = true;
        else
            flag = false;
        setWillNotDraw(flag);
        invalidate();
    }

    public void setPageTransformer(boolean flag, PageTransformer pagetransformer)
    {
        int i = 1;
        if(android.os.Build.VERSION.SDK_INT >= 11)
        {
            boolean flag1;
            boolean flag2;
            boolean flag3;
            if(pagetransformer != null)
                flag1 = true;
            else
                flag1 = false;
            if(mPageTransformer != null)
                flag2 = true;
            else
                flag2 = false;
            if(flag1 != flag2)
                flag3 = true;
            else
                flag3 = false;
            mPageTransformer = pagetransformer;
            setChildrenDrawingOrderEnabledCompat(flag1);
            if(flag1)
            {
                if(flag)
                    i = 2;
                mDrawingOrder = i;
            } else
            {
                mDrawingOrder = 0;
            }
            if(flag3)
                populate();
        }
    }

    void smoothScrollTo(int i, int j)
    {
        smoothScrollTo(i, j, 0);
    }

    void smoothScrollTo(int i, int j, int k)
    {
        if(getChildCount() == 0)
        {
            setScrollingCacheEnabled(false);
        } else
        {
            int l;
            int i1;
            int j1;
            if(mScroller != null && !mScroller.isFinished())
                l = 1;
            else
                l = 0;
            if(l != 0)
            {
                if(mIsScrollStarted)
                    l = mScroller.getCurrX();
                else
                    l = mScroller.getStartX();
                mScroller.abortAnimation();
                setScrollingCacheEnabled(false);
            } else
            {
                l = getScrollX();
            }
            i1 = getScrollY();
            j1 = i - l;
            j -= i1;
            if(j1 == 0 && j == 0)
            {
                completeScroll(false);
                populate();
                setScrollState(0);
            } else
            {
                setScrollingCacheEnabled(true);
                setScrollState(2);
                i = getClientWidth();
                int k1 = i / 2;
                float f = Math.min(1.0F, (1.0F * (float)Math.abs(j1)) / (float)i);
                float f1 = k1;
                float f3 = k1;
                f = distanceInfluenceForSnapDuration(f);
                k = Math.abs(k);
                if(k > 0)
                {
                    i = Math.round(1000F * Math.abs((f1 + f3 * f) / (float)k)) * 4;
                } else
                {
                    float f4 = i;
                    float f2 = mAdapter.getPageWidth(mCurItem);
                    i = (int)((1.0F + (float)Math.abs(j1) / ((float)mPageMargin + f4 * f2)) * 100F);
                }
                i = Math.min(i, 600);
                mIsScrollStarted = false;
                mScroller.startScroll(l, i1, j1, j, i);
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }
    }

    protected boolean verifyDrawable(Drawable drawable)
    {
        boolean flag;
        if(super.verifyDrawable(drawable) || drawable == mMarginDrawable)
            flag = true;
        else
            flag = false;
        return flag;
    }

    private static final int CLOSE_ENOUGH = 2;
    private static final Comparator COMPARATOR = new Comparator() {

        public int compare(ItemInfo iteminfo, ItemInfo iteminfo1)
        {
            return iteminfo.position - iteminfo1.position;
        }

        public volatile int compare(Object obj, Object obj1)
        {
            return compare((ItemInfo)obj, (ItemInfo)obj1);
        }

    }
;
    private static final boolean DEBUG = false;
    private static final int DEFAULT_GUTTER_SIZE = 16;
    private static final int DEFAULT_OFFSCREEN_PAGES = 1;
    private static final int DRAW_ORDER_DEFAULT = 0;
    private static final int DRAW_ORDER_FORWARD = 1;
    private static final int DRAW_ORDER_REVERSE = 2;
    private static final int INVALID_POINTER = -1;
    private static final int LAYOUT_ATTRS[] = {
        0x10100b3
    };
    private static final int MAX_SETTLE_DURATION = 600;
    private static final int MIN_DISTANCE_FOR_FLING = 25;
    private static final int MIN_FLING_VELOCITY = 400;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    private static final String TAG = "ViewPager";
    private static final boolean USE_CACHE = false;
    private static final Interpolator sInterpolator = new Interpolator() {

        public float getInterpolation(float f)
        {
            f--;
            return f * f * f * f * f + 1.0F;
        }

    }
;
    private static final ViewPositionComparator sPositionComparator = new ViewPositionComparator();
    private int mActivePointerId;
    private PagerAdapter mAdapter;
    private OnAdapterChangeListener mAdapterChangeListener;
    private int mBottomPageBounds;
    private boolean mCalledSuper;
    private int mChildHeightMeasureSpec;
    private int mChildWidthMeasureSpec;
    private int mCloseEnough;
    private int mCurItem;
    private int mDecorChildCount;
    private int mDefaultGutterSize;
    private int mDrawingOrder;
    private ArrayList mDrawingOrderedChildren;
    private final Runnable mEndScrollRunnable;
    private int mExpectedAdapterCount;
    private long mFakeDragBeginTime;
    private boolean mFakeDragging;
    private boolean mFirstLayout;
    private float mFirstOffset;
    private int mFlingDistance;
    private int mGutterSize;
    private boolean mInLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private OnPageChangeListener mInternalPageChangeListener;
    private boolean mIsBeingDragged;
    private boolean mIsScrollStarted;
    private boolean mIsUnableToDrag;
    private final ArrayList mItems;
    private float mLastMotionX;
    private float mLastMotionY;
    private float mLastOffset;
    private EdgeEffectCompat mLeftEdge;
    private Drawable mMarginDrawable;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private boolean mNeedCalculatePageOffsets;
    private PagerObserver mObserver;
    private int mOffscreenPageLimit;
    private OnPageChangeListener mOnPageChangeListener;
    private List mOnPageChangeListeners;
    private int mPageMargin;
    private PageTransformer mPageTransformer;
    private boolean mPopulatePending;
    private Parcelable mRestoredAdapterState;
    private ClassLoader mRestoredClassLoader;
    private int mRestoredCurItem;
    private EdgeEffectCompat mRightEdge;
    private int mScrollState;
    private Scroller mScroller;
    private boolean mScrollingCacheEnabled;
    private Method mSetChildrenDrawingOrderEnabled;
    private final ItemInfo mTempItem;
    private final Rect mTempRect;
    private int mTopPageBounds;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;





}
