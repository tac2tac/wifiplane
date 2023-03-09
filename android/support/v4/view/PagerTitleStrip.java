// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.view;

import android.content.Context;
import android.content.res.*;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.TextView;
import java.lang.ref.WeakReference;

// Referenced classes of package android.support.v4.view:
//            ViewPager, PagerAdapter, ViewCompat, PagerTitleStripIcs

public class PagerTitleStrip extends ViewGroup
    implements ViewPager.Decor
{
    private class PageListener extends DataSetObserver
        implements ViewPager.OnPageChangeListener, ViewPager.OnAdapterChangeListener
    {

        public void onAdapterChanged(PagerAdapter pageradapter, PagerAdapter pageradapter1)
        {
            updateAdapter(pageradapter, pageradapter1);
        }

        public void onChanged()
        {
            float f = 0.0F;
            updateText(mPager.getCurrentItem(), mPager.getAdapter());
            if(mLastKnownPositionOffset >= 0.0F)
                f = mLastKnownPositionOffset;
            updateTextPositions(mPager.getCurrentItem(), f, true);
        }

        public void onPageScrollStateChanged(int i)
        {
            mScrollState = i;
        }

        public void onPageScrolled(int i, float f, int j)
        {
            j = i;
            if(f > 0.5F)
                j = i + 1;
            updateTextPositions(j, f, false);
        }

        public void onPageSelected(int i)
        {
            float f = 0.0F;
            if(mScrollState == 0)
            {
                updateText(mPager.getCurrentItem(), mPager.getAdapter());
                if(mLastKnownPositionOffset >= 0.0F)
                    f = mLastKnownPositionOffset;
                updateTextPositions(mPager.getCurrentItem(), f, true);
            }
        }

        private int mScrollState;
        final PagerTitleStrip this$0;

        private PageListener()
        {
            this$0 = PagerTitleStrip.this;
            super();
        }

    }

    static interface PagerTitleStripImpl
    {

        public abstract void setSingleLineAllCaps(TextView textview);
    }

    static class PagerTitleStripImplBase
        implements PagerTitleStripImpl
    {

        public void setSingleLineAllCaps(TextView textview)
        {
            textview.setSingleLine();
        }

        PagerTitleStripImplBase()
        {
        }
    }

    static class PagerTitleStripImplIcs
        implements PagerTitleStripImpl
    {

        public void setSingleLineAllCaps(TextView textview)
        {
            PagerTitleStripIcs.setSingleLineAllCaps(textview);
        }

        PagerTitleStripImplIcs()
        {
        }
    }


    public PagerTitleStrip(Context context)
    {
        this(context, null);
    }

    public PagerTitleStrip(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        mLastKnownCurrentPage = -1;
        mLastKnownPositionOffset = -1F;
        mPageListener = new PageListener();
        TextView textview = new TextView(context);
        mPrevText = textview;
        addView(textview);
        textview = new TextView(context);
        mCurrText = textview;
        addView(textview);
        textview = new TextView(context);
        mNextText = textview;
        addView(textview);
        attributeset = context.obtainStyledAttributes(attributeset, ATTRS);
        int i = attributeset.getResourceId(0, 0);
        if(i != 0)
        {
            mPrevText.setTextAppearance(context, i);
            mCurrText.setTextAppearance(context, i);
            mNextText.setTextAppearance(context, i);
        }
        int j = attributeset.getDimensionPixelSize(1, 0);
        if(j != 0)
            setTextSize(0, j);
        if(attributeset.hasValue(2))
        {
            int k = attributeset.getColor(2, 0);
            mPrevText.setTextColor(k);
            mCurrText.setTextColor(k);
            mNextText.setTextColor(k);
        }
        mGravity = attributeset.getInteger(3, 80);
        attributeset.recycle();
        mTextColor = mCurrText.getTextColors().getDefaultColor();
        setNonPrimaryAlpha(0.6F);
        mPrevText.setEllipsize(android.text.TextUtils.TruncateAt.END);
        mCurrText.setEllipsize(android.text.TextUtils.TruncateAt.END);
        mNextText.setEllipsize(android.text.TextUtils.TruncateAt.END);
        boolean flag = false;
        if(i != 0)
        {
            attributeset = context.obtainStyledAttributes(i, TEXT_ATTRS);
            flag = attributeset.getBoolean(0, false);
            attributeset.recycle();
        }
        if(flag)
        {
            setSingleLineAllCaps(mPrevText);
            setSingleLineAllCaps(mCurrText);
            setSingleLineAllCaps(mNextText);
        } else
        {
            mPrevText.setSingleLine();
            mCurrText.setSingleLine();
            mNextText.setSingleLine();
        }
        mScaledTextSpacing = (int)(16F * context.getResources().getDisplayMetrics().density);
    }

    private static void setSingleLineAllCaps(TextView textview)
    {
        IMPL.setSingleLineAllCaps(textview);
    }

    int getMinHeight()
    {
        int i = 0;
        Drawable drawable = getBackground();
        if(drawable != null)
            i = drawable.getIntrinsicHeight();
        return i;
    }

    public int getTextSpacing()
    {
        return mScaledTextSpacing;
    }

    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        Object obj = getParent();
        if(!(obj instanceof ViewPager))
            throw new IllegalStateException("PagerTitleStrip must be a direct child of a ViewPager.");
        obj = (ViewPager)obj;
        PagerAdapter pageradapter = ((ViewPager) (obj)).getAdapter();
        ((ViewPager) (obj)).setInternalPageChangeListener(mPageListener);
        ((ViewPager) (obj)).setOnAdapterChangeListener(mPageListener);
        mPager = ((ViewPager) (obj));
        if(mWatchingAdapter != null)
            obj = (PagerAdapter)mWatchingAdapter.get();
        else
            obj = null;
        updateAdapter(((PagerAdapter) (obj)), pageradapter);
    }

    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        if(mPager != null)
        {
            updateAdapter(mPager.getAdapter(), null);
            mPager.setInternalPageChangeListener(null);
            mPager.setOnAdapterChangeListener(null);
            mPager = null;
        }
    }

    protected void onLayout(boolean flag, int i, int j, int k, int l)
    {
        float f = 0.0F;
        if(mPager != null)
        {
            if(mLastKnownPositionOffset >= 0.0F)
                f = mLastKnownPositionOffset;
            updateTextPositions(mLastKnownCurrentPage, f, true);
        }
    }

    protected void onMeasure(int i, int j)
    {
        if(android.view.View.MeasureSpec.getMode(i) != 0x40000000)
            throw new IllegalStateException("Must measure with an exact width");
        int k = getPaddingTop() + getPaddingBottom();
        int l = getChildMeasureSpec(j, k, -2);
        int i1 = android.view.View.MeasureSpec.getSize(i);
        i = getChildMeasureSpec(i, (int)((float)i1 * 0.2F), -2);
        mPrevText.measure(i, l);
        mCurrText.measure(i, l);
        mNextText.measure(i, l);
        if(android.view.View.MeasureSpec.getMode(j) == 0x40000000)
        {
            i = android.view.View.MeasureSpec.getSize(j);
        } else
        {
            i = mCurrText.getMeasuredHeight();
            i = Math.max(getMinHeight(), i + k);
        }
        setMeasuredDimension(i1, ViewCompat.resolveSizeAndState(i, j, ViewCompat.getMeasuredState(mCurrText) << 16));
    }

    public void requestLayout()
    {
        if(!mUpdatingText)
            super.requestLayout();
    }

    public void setGravity(int i)
    {
        mGravity = i;
        requestLayout();
    }

    public void setNonPrimaryAlpha(float f)
    {
        mNonPrimaryAlpha = (int)(255F * f) & 0xff;
        int i = mNonPrimaryAlpha << 24 | mTextColor & 0xffffff;
        mPrevText.setTextColor(i);
        mNextText.setTextColor(i);
    }

    public void setTextColor(int i)
    {
        mTextColor = i;
        mCurrText.setTextColor(i);
        i = mNonPrimaryAlpha << 24 | mTextColor & 0xffffff;
        mPrevText.setTextColor(i);
        mNextText.setTextColor(i);
    }

    public void setTextSize(int i, float f)
    {
        mPrevText.setTextSize(i, f);
        mCurrText.setTextSize(i, f);
        mNextText.setTextSize(i, f);
    }

    public void setTextSpacing(int i)
    {
        mScaledTextSpacing = i;
        requestLayout();
    }

    void updateAdapter(PagerAdapter pageradapter, PagerAdapter pageradapter1)
    {
        if(pageradapter != null)
        {
            pageradapter.unregisterDataSetObserver(mPageListener);
            mWatchingAdapter = null;
        }
        if(pageradapter1 != null)
        {
            pageradapter1.registerDataSetObserver(mPageListener);
            mWatchingAdapter = new WeakReference(pageradapter1);
        }
        if(mPager != null)
        {
            mLastKnownCurrentPage = -1;
            mLastKnownPositionOffset = -1F;
            updateText(mPager.getCurrentItem(), pageradapter1);
            requestLayout();
        }
    }

    void updateText(int i, PagerAdapter pageradapter)
    {
        int j;
        TextView textview;
        CharSequence charsequence;
        int k;
        if(pageradapter != null)
            j = pageradapter.getCount();
        else
            j = 0;
        mUpdatingText = true;
        textview = null;
        charsequence = textview;
        if(i >= 1)
        {
            charsequence = textview;
            if(pageradapter != null)
                charsequence = pageradapter.getPageTitle(i - 1);
        }
        mPrevText.setText(charsequence);
        textview = mCurrText;
        if(pageradapter != null && i < j)
            charsequence = pageradapter.getPageTitle(i);
        else
            charsequence = null;
        textview.setText(charsequence);
        textview = null;
        charsequence = textview;
        if(i + 1 < j)
        {
            charsequence = textview;
            if(pageradapter != null)
                charsequence = pageradapter.getPageTitle(i + 1);
        }
        mNextText.setText(charsequence);
        j = android.view.View.MeasureSpec.makeMeasureSpec(Math.max(0, (int)((float)(getWidth() - getPaddingLeft() - getPaddingRight()) * 0.8F)), 0x80000000);
        k = android.view.View.MeasureSpec.makeMeasureSpec(Math.max(0, getHeight() - getPaddingTop() - getPaddingBottom()), 0x80000000);
        mPrevText.measure(j, k);
        mCurrText.measure(j, k);
        mNextText.measure(j, k);
        mLastKnownCurrentPage = i;
        if(!mUpdatingPositions)
            updateTextPositions(i, mLastKnownPositionOffset, false);
        mUpdatingText = false;
    }

    void updateTextPositions(int i, float f, boolean flag)
    {
        if(i == mLastKnownCurrentPage) goto _L2; else goto _L1
_L1:
        updateText(i, mPager.getAdapter());
_L7:
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
        int l2;
        int i3;
        int j3;
        mUpdatingPositions = true;
        j = mPrevText.getMeasuredWidth();
        k = mCurrText.getMeasuredWidth();
        l = mNextText.getMeasuredWidth();
        i1 = k / 2;
        j1 = getWidth();
        k1 = getHeight();
        l1 = getPaddingLeft();
        i2 = getPaddingRight();
        i = getPaddingTop();
        j2 = getPaddingBottom();
        k2 = i2 + i1;
        float f1 = f + 0.5F;
        float f2 = f1;
        if(f1 > 1.0F)
            f2 = f1 - 1.0F;
        i1 = j1 - k2 - (int)((float)(j1 - (l1 + i1) - k2) * f2) - k / 2;
        k = i1 + k;
        l2 = mPrevText.getBaseline();
        i3 = mCurrText.getBaseline();
        k2 = mNextText.getBaseline();
        j3 = Math.max(Math.max(l2, i3), k2);
        l2 = j3 - l2;
        i3 = j3 - i3;
        k2 = j3 - k2;
        int k3 = mPrevText.getMeasuredHeight();
        j3 = mCurrText.getMeasuredHeight();
        int l3 = mNextText.getMeasuredHeight();
        j3 = Math.max(Math.max(l2 + k3, i3 + j3), k2 + l3);
        mGravity & 0x70;
        JVM INSTR lookupswitch 2: default 304
    //                   16: 458
    //                   80: 491;
           goto _L3 _L4 _L5
_L5:
        break MISSING_BLOCK_LABEL_491;
_L3:
        k1 = i + l2;
        j2 = i + i3;
        i += k2;
_L8:
        mCurrText.layout(i1, j2, k, mCurrText.getMeasuredHeight() + j2);
        j2 = Math.min(l1, i1 - mScaledTextSpacing - j);
        mPrevText.layout(j2, k1, j2 + j, mPrevText.getMeasuredHeight() + k1);
        j2 = Math.max(j1 - i2 - l, mScaledTextSpacing + k);
        mNextText.layout(j2, i, j2 + l, mNextText.getMeasuredHeight() + i);
        mLastKnownPositionOffset = f;
        mUpdatingPositions = false;
_L6:
        return;
_L2:
        if(flag || f != mLastKnownPositionOffset) goto _L7; else goto _L6
_L4:
        i = (k1 - i - j2 - j3) / 2;
        k1 = i + l2;
        j2 = i + i3;
        i += k2;
          goto _L8
        i = k1 - j2 - j3;
        k1 = i + l2;
        j2 = i + i3;
        i += k2;
          goto _L8
    }

    private static final int ATTRS[] = {
        0x1010034, 0x1010095, 0x1010098, 0x10100af
    };
    private static final PagerTitleStripImpl IMPL;
    private static final float SIDE_ALPHA = 0.6F;
    private static final String TAG = "PagerTitleStrip";
    private static final int TEXT_ATTRS[] = {
        0x101038c
    };
    private static final int TEXT_SPACING = 16;
    TextView mCurrText;
    private int mGravity;
    private int mLastKnownCurrentPage;
    private float mLastKnownPositionOffset;
    TextView mNextText;
    private int mNonPrimaryAlpha;
    private final PageListener mPageListener;
    ViewPager mPager;
    TextView mPrevText;
    private int mScaledTextSpacing;
    int mTextColor;
    private boolean mUpdatingPositions;
    private boolean mUpdatingText;
    private WeakReference mWatchingAdapter;

    static 
    {
        if(android.os.Build.VERSION.SDK_INT >= 14)
            IMPL = new PagerTitleStripImplIcs();
        else
            IMPL = new PagerTitleStripImplBase();
    }

}
