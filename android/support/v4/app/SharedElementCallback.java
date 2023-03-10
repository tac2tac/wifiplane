// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.app;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import java.util.List;
import java.util.Map;

public abstract class SharedElementCallback
{

    public SharedElementCallback()
    {
    }

    private static Bitmap createDrawableBitmap(Drawable drawable)
    {
        int i = drawable.getIntrinsicWidth();
        int j = drawable.getIntrinsicHeight();
        if(i <= 0 || j <= 0)
        {
            drawable = null;
        } else
        {
            float f = Math.min(1.0F, (float)MAX_IMAGE_SIZE / (float)(i * j));
            if((drawable instanceof BitmapDrawable) && f == 1.0F)
            {
                drawable = ((BitmapDrawable)drawable).getBitmap();
            } else
            {
                i = (int)((float)i * f);
                int k = (int)((float)j * f);
                Bitmap bitmap = Bitmap.createBitmap(i, k, android.graphics.Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                Rect rect = drawable.getBounds();
                j = rect.left;
                int l = rect.top;
                int i1 = rect.right;
                int j1 = rect.bottom;
                drawable.setBounds(0, 0, i, k);
                drawable.draw(canvas);
                drawable.setBounds(j, l, i1, j1);
                drawable = bitmap;
            }
        }
        return drawable;
    }

    public Parcelable onCaptureSharedElementSnapshot(View view, Matrix matrix, RectF rectf)
    {
        if(!(view instanceof ImageView)) goto _L2; else goto _L1
_L1:
        ImageView imageview;
        Object obj;
        Drawable drawable;
        imageview = (ImageView)view;
        obj = imageview.getDrawable();
        drawable = imageview.getBackground();
        if(obj == null || drawable != null) goto _L2; else goto _L3
_L3:
        obj = createDrawableBitmap(((Drawable) (obj)));
        if(obj == null) goto _L2; else goto _L4
_L4:
        matrix = new Bundle();
        matrix.putParcelable("sharedElement:snapshot:bitmap", ((Parcelable) (obj)));
        matrix.putString("sharedElement:snapshot:imageScaleType", imageview.getScaleType().toString());
        view = matrix;
        if(imageview.getScaleType() == android.widget.ImageView.ScaleType.MATRIX)
        {
            view = imageview.getImageMatrix();
            rectf = new float[9];
            view.getValues(rectf);
            matrix.putFloatArray("sharedElement:snapshot:imageMatrix", rectf);
            view = matrix;
        }
_L6:
        return view;
_L2:
        int i = Math.round(rectf.width());
        int j = Math.round(rectf.height());
        Object obj1 = null;
        Bitmap bitmap = obj1;
        if(i > 0)
        {
            bitmap = obj1;
            if(j > 0)
            {
                float f = Math.min(1.0F, (float)MAX_IMAGE_SIZE / (float)(i * j));
                i = (int)((float)i * f);
                j = (int)((float)j * f);
                if(mTempMatrix == null)
                    mTempMatrix = new Matrix();
                mTempMatrix.set(matrix);
                mTempMatrix.postTranslate(-rectf.left, -rectf.top);
                mTempMatrix.postScale(f, f);
                bitmap = Bitmap.createBitmap(i, j, android.graphics.Bitmap.Config.ARGB_8888);
                matrix = new Canvas(bitmap);
                matrix.concat(mTempMatrix);
                view.draw(matrix);
            }
        }
        view = bitmap;
        if(true) goto _L6; else goto _L5
_L5:
    }

    public View onCreateSnapshotView(Context context, Parcelable parcelable)
    {
        Object obj = null;
        if(!(parcelable instanceof Bundle)) goto _L2; else goto _L1
_L1:
        Object obj1;
        obj1 = (Bundle)parcelable;
        obj = (Bitmap)((Bundle) (obj1)).getParcelable("sharedElement:snapshot:bitmap");
        if(obj != null) goto _L4; else goto _L3
_L3:
        obj = null;
_L6:
        return ((View) (obj));
_L4:
        parcelable = new ImageView(context);
        context = parcelable;
        parcelable.setImageBitmap(((Bitmap) (obj)));
        parcelable.setScaleType(android.widget.ImageView.ScaleType.valueOf(((Bundle) (obj1)).getString("sharedElement:snapshot:imageScaleType")));
        obj = context;
        if(parcelable.getScaleType() == android.widget.ImageView.ScaleType.MATRIX)
        {
            obj = ((Bundle) (obj1)).getFloatArray("sharedElement:snapshot:imageMatrix");
            obj1 = new Matrix();
            ((Matrix) (obj1)).setValues(((float []) (obj)));
            parcelable.setImageMatrix(((Matrix) (obj1)));
            obj = context;
        }
        continue; /* Loop/switch isn't completed */
_L2:
        if(parcelable instanceof Bitmap)
        {
            parcelable = (Bitmap)parcelable;
            obj = new ImageView(context);
            ((ImageView) (obj)).setImageBitmap(parcelable);
        }
        if(true) goto _L6; else goto _L5
_L5:
    }

    public void onMapSharedElements(List list, Map map)
    {
    }

    public void onRejectSharedElements(List list)
    {
    }

    public void onSharedElementEnd(List list, List list1, List list2)
    {
    }

    public void onSharedElementStart(List list, List list1, List list2)
    {
    }

    private static final String BUNDLE_SNAPSHOT_BITMAP = "sharedElement:snapshot:bitmap";
    private static final String BUNDLE_SNAPSHOT_IMAGE_MATRIX = "sharedElement:snapshot:imageMatrix";
    private static final String BUNDLE_SNAPSHOT_IMAGE_SCALETYPE = "sharedElement:snapshot:imageScaleType";
    private static int MAX_IMAGE_SIZE = 0x100000;
    private Matrix mTempMatrix;

}
