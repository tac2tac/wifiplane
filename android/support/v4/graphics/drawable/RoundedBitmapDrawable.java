// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.graphics.drawable;

import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

public abstract class RoundedBitmapDrawable extends Drawable
{

    RoundedBitmapDrawable(Resources resources, Bitmap bitmap)
    {
        mTargetDensity = 160;
        mGravity = 119;
        mApplyGravity = true;
        if(resources != null)
            mTargetDensity = resources.getDisplayMetrics().densityDpi;
        mBitmap = bitmap;
        if(mBitmap != null)
        {
            computeBitmapSize();
            mBitmapShader = new BitmapShader(mBitmap, android.graphics.Shader.TileMode.CLAMP, android.graphics.Shader.TileMode.CLAMP);
        } else
        {
            mBitmapHeight = -1;
            mBitmapWidth = -1;
            mBitmapShader = null;
        }
    }

    private void computeBitmapSize()
    {
        mBitmapWidth = mBitmap.getScaledWidth(mTargetDensity);
        mBitmapHeight = mBitmap.getScaledHeight(mTargetDensity);
    }

    private static boolean isGreaterThanZero(float f)
    {
        boolean flag;
        if(f > 0.05F)
            flag = true;
        else
            flag = false;
        return flag;
    }

    private void updateCircularCornerRadius()
    {
        mCornerRadius = Math.min(mBitmapHeight, mBitmapWidth) / 2;
    }

    public void draw(Canvas canvas)
    {
        Bitmap bitmap = mBitmap;
        if(bitmap != null)
        {
            updateDstRect();
            if(mPaint.getShader() == null)
                canvas.drawBitmap(bitmap, null, mDstRect, mPaint);
            else
                canvas.drawRoundRect(mDstRectF, mCornerRadius, mCornerRadius, mPaint);
        }
    }

    public int getAlpha()
    {
        return mPaint.getAlpha();
    }

    public final Bitmap getBitmap()
    {
        return mBitmap;
    }

    public ColorFilter getColorFilter()
    {
        return mPaint.getColorFilter();
    }

    public float getCornerRadius()
    {
        return mCornerRadius;
    }

    public int getGravity()
    {
        return mGravity;
    }

    public int getIntrinsicHeight()
    {
        return mBitmapHeight;
    }

    public int getIntrinsicWidth()
    {
        return mBitmapWidth;
    }

    public int getOpacity()
    {
        byte byte0;
        byte byte1;
        byte0 = -3;
        byte1 = byte0;
        if(mGravity != 119) goto _L2; else goto _L1
_L1:
        if(!mIsCircular) goto _L4; else goto _L3
_L3:
        byte1 = byte0;
_L2:
        return byte1;
_L4:
        Bitmap bitmap = mBitmap;
        byte1 = byte0;
        if(bitmap != null)
        {
            byte1 = byte0;
            if(!bitmap.hasAlpha())
            {
                byte1 = byte0;
                if(mPaint.getAlpha() >= 255)
                {
                    byte1 = byte0;
                    if(!isGreaterThanZero(mCornerRadius))
                        byte1 = -1;
                }
            }
        }
        if(true) goto _L2; else goto _L5
_L5:
    }

    public final Paint getPaint()
    {
        return mPaint;
    }

    void gravityCompatApply(int i, int j, int k, Rect rect, Rect rect1)
    {
        throw new UnsupportedOperationException();
    }

    public boolean hasAntiAlias()
    {
        return mPaint.isAntiAlias();
    }

    public boolean hasMipMap()
    {
        throw new UnsupportedOperationException();
    }

    public boolean isCircular()
    {
        return mIsCircular;
    }

    protected void onBoundsChange(Rect rect)
    {
        super.onBoundsChange(rect);
        if(mIsCircular)
            updateCircularCornerRadius();
        mApplyGravity = true;
    }

    public void setAlpha(int i)
    {
        if(i != mPaint.getAlpha())
        {
            mPaint.setAlpha(i);
            invalidateSelf();
        }
    }

    public void setAntiAlias(boolean flag)
    {
        mPaint.setAntiAlias(flag);
        invalidateSelf();
    }

    public void setCircular(boolean flag)
    {
        mIsCircular = flag;
        mApplyGravity = true;
        if(flag)
        {
            updateCircularCornerRadius();
            mPaint.setShader(mBitmapShader);
            invalidateSelf();
        } else
        {
            setCornerRadius(0.0F);
        }
    }

    public void setColorFilter(ColorFilter colorfilter)
    {
        mPaint.setColorFilter(colorfilter);
        invalidateSelf();
    }

    public void setCornerRadius(float f)
    {
        if(mCornerRadius != f)
        {
            mIsCircular = false;
            if(isGreaterThanZero(f))
                mPaint.setShader(mBitmapShader);
            else
                mPaint.setShader(null);
            mCornerRadius = f;
            invalidateSelf();
        }
    }

    public void setDither(boolean flag)
    {
        mPaint.setDither(flag);
        invalidateSelf();
    }

    public void setFilterBitmap(boolean flag)
    {
        mPaint.setFilterBitmap(flag);
        invalidateSelf();
    }

    public void setGravity(int i)
    {
        if(mGravity != i)
        {
            mGravity = i;
            mApplyGravity = true;
            invalidateSelf();
        }
    }

    public void setMipMap(boolean flag)
    {
        throw new UnsupportedOperationException();
    }

    public void setTargetDensity(int i)
    {
        if(mTargetDensity != i)
        {
            int j = i;
            if(i == 0)
                j = 160;
            mTargetDensity = j;
            if(mBitmap != null)
                computeBitmapSize();
            invalidateSelf();
        }
    }

    public void setTargetDensity(Canvas canvas)
    {
        setTargetDensity(canvas.getDensity());
    }

    public void setTargetDensity(DisplayMetrics displaymetrics)
    {
        setTargetDensity(displaymetrics.densityDpi);
    }

    void updateDstRect()
    {
        if(mApplyGravity)
        {
            if(mIsCircular)
            {
                int i = Math.min(mBitmapWidth, mBitmapHeight);
                gravityCompatApply(mGravity, i, i, getBounds(), mDstRect);
                int j = Math.min(mDstRect.width(), mDstRect.height());
                int k = Math.max(0, (mDstRect.width() - j) / 2);
                i = Math.max(0, (mDstRect.height() - j) / 2);
                mDstRect.inset(k, i);
                mCornerRadius = 0.5F * (float)j;
            } else
            {
                gravityCompatApply(mGravity, mBitmapWidth, mBitmapHeight, getBounds(), mDstRect);
            }
            mDstRectF.set(mDstRect);
            if(mBitmapShader != null)
            {
                mShaderMatrix.setTranslate(mDstRectF.left, mDstRectF.top);
                mShaderMatrix.preScale(mDstRectF.width() / (float)mBitmap.getWidth(), mDstRectF.height() / (float)mBitmap.getHeight());
                mBitmapShader.setLocalMatrix(mShaderMatrix);
                mPaint.setShader(mBitmapShader);
            }
            mApplyGravity = false;
        }
    }

    private static final int DEFAULT_PAINT_FLAGS = 3;
    private boolean mApplyGravity;
    final Bitmap mBitmap;
    private int mBitmapHeight;
    private final BitmapShader mBitmapShader;
    private int mBitmapWidth;
    private float mCornerRadius;
    final Rect mDstRect = new Rect();
    private final RectF mDstRectF = new RectF();
    private int mGravity;
    private boolean mIsCircular;
    private final Paint mPaint = new Paint(3);
    private final Matrix mShaderMatrix = new Matrix();
    private int mTargetDensity;
}
