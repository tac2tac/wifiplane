// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.print;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.*;
import android.net.Uri;
import android.os.*;
import android.print.*;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import java.io.*;

class PrintHelperKitkat
{
    public static interface OnPrintFinishCallback
    {

        public abstract void onFinish();
    }


    PrintHelperKitkat(Context context)
    {
        mDecodeOptions = null;
        mScaleMode = 2;
        mColorMode = 2;
        mOrientation = 1;
        mContext = context;
    }

    private Bitmap convertBitmapForColorMode(Bitmap bitmap, int i)
    {
        if(i == 1)
        {
            Bitmap bitmap1 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), android.graphics.Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap1);
            Paint paint = new Paint();
            ColorMatrix colormatrix = new ColorMatrix();
            colormatrix.setSaturation(0.0F);
            paint.setColorFilter(new ColorMatrixColorFilter(colormatrix));
            canvas.drawBitmap(bitmap, 0.0F, 0.0F, paint);
            canvas.setBitmap(null);
            bitmap = bitmap1;
        }
        return bitmap;
    }

    private Matrix getMatrix(int i, int j, RectF rectf, int k)
    {
        Matrix matrix = new Matrix();
        float f = rectf.width() / (float)i;
        if(k == 2)
            f = Math.max(f, rectf.height() / (float)j);
        else
            f = Math.min(f, rectf.height() / (float)j);
        matrix.postScale(f, f);
        matrix.postTranslate((rectf.width() - (float)i * f) / 2.0F, (rectf.height() - (float)j * f) / 2.0F);
        return matrix;
    }

    private Bitmap loadBitmap(Uri uri, android.graphics.BitmapFactory.Options options)
        throws FileNotFoundException
    {
        Uri uri1;
        if(uri == null || mContext == null)
            throw new IllegalArgumentException("bad argument to loadBitmap");
        uri1 = null;
        uri = mContext.getContentResolver().openInputStream(uri);
        uri1 = uri;
        options = BitmapFactory.decodeStream(uri, null, options);
        if(uri != null)
            try
            {
                uri.close();
            }
            // Misplaced declaration of an exception variable
            catch(Uri uri)
            {
                Log.w("PrintHelperKitkat", "close fail ", uri);
            }
        return options;
        uri;
        if(uri1 != null)
            try
            {
                uri1.close();
            }
            // Misplaced declaration of an exception variable
            catch(android.graphics.BitmapFactory.Options options)
            {
                Log.w("PrintHelperKitkat", "close fail ", options);
            }
        throw uri;
    }

    private Bitmap loadConstrainedBitmap(Uri uri, int i)
        throws FileNotFoundException
    {
        android.graphics.BitmapFactory.Options options;
        int j;
        int k;
        options = null;
        if(i <= 0 || uri == null || mContext == null)
            throw new IllegalArgumentException("bad argument to getScaledBitmap");
        android.graphics.BitmapFactory.Options options1 = new android.graphics.BitmapFactory.Options();
        options1.inJustDecodeBounds = true;
        loadBitmap(uri, options1);
        j = options1.outWidth;
        k = options1.outHeight;
        obj = options;
        if(j <= 0) goto _L2; else goto _L1
_L1:
        if(k > 0) goto _L4; else goto _L3
_L3:
        obj = options;
_L2:
        return ((Bitmap) (obj));
_L4:
        int l = Math.max(j, k);
        int i1;
        for(i1 = 1; l > i; i1 <<= 1)
            l >>>= 1;

        obj = options;
        if(i1 <= 0)
            continue; /* Loop/switch isn't completed */
        obj = options;
        if(Math.min(j, k) / i1 <= 0)
            continue; /* Loop/switch isn't completed */
        synchronized(mLock)
        {
            options = JVM INSTR new #204 <Class android.graphics.BitmapFactory$Options>;
            options.android.graphics.BitmapFactory.Options();
            mDecodeOptions = options;
            mDecodeOptions.inMutable = true;
            mDecodeOptions.inSampleSize = i1;
            options = mDecodeOptions;
        }
        obj = loadBitmap(uri, options);
        synchronized(mLock)
        {
            mDecodeOptions = null;
        }
        if(true) goto _L2; else goto _L5
_L5:
        obj;
        uri;
        JVM INSTR monitorexit ;
        throw obj;
        uri;
        obj;
        JVM INSTR monitorexit ;
        throw uri;
        Exception exception;
        exception;
        synchronized(mLock)
        {
            mDecodeOptions = null;
        }
        throw exception;
        exception1;
        uri;
        JVM INSTR monitorexit ;
        throw exception1;
    }

    public int getColorMode()
    {
        return mColorMode;
    }

    public int getOrientation()
    {
        return mOrientation;
    }

    public int getScaleMode()
    {
        return mScaleMode;
    }

    public void printBitmap(final String jobName, final Bitmap bitmap, final OnPrintFinishCallback callback)
    {
        if(bitmap != null)
        {
            final int fittingMode = mScaleMode;
            PrintManager printmanager = (PrintManager)mContext.getSystemService("print");
            Object obj = android.print.PrintAttributes.MediaSize.UNKNOWN_PORTRAIT;
            if(bitmap.getWidth() > bitmap.getHeight())
                obj = android.print.PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE;
            obj = (new android.print.PrintAttributes.Builder()).setMediaSize(((android.print.PrintAttributes.MediaSize) (obj))).setColorMode(mColorMode).build();
            printmanager.print(jobName, new PrintDocumentAdapter() {

                public void onFinish()
                {
                    if(callback != null)
                        callback.onFinish();
                }

                public void onLayout(PrintAttributes printattributes, PrintAttributes printattributes1, CancellationSignal cancellationsignal, android.print.PrintDocumentAdapter.LayoutResultCallback layoutresultcallback, Bundle bundle)
                {
                    boolean flag = true;
                    mAttributes = printattributes1;
                    cancellationsignal = (new android.print.PrintDocumentInfo.Builder(jobName)).setContentType(1).setPageCount(1).build();
                    if(printattributes1.equals(printattributes))
                        flag = false;
                    layoutresultcallback.onLayoutFinished(cancellationsignal, flag);
                }

                public void onWrite(PageRange apagerange[], ParcelFileDescriptor parcelfiledescriptor, CancellationSignal cancellationsignal, android.print.PrintDocumentAdapter.WriteResultCallback writeresultcallback)
                {
                    cancellationsignal = new PrintedPdfDocument(mContext, mAttributes);
                    apagerange = convertBitmapForColorMode(bitmap, mAttributes.getColorMode());
                    android.graphics.pdf.PdfDocument.Page page = cancellationsignal.startPage(1);
                    Object obj1 = JVM INSTR new #101 <Class RectF>;
                    ((RectF) (obj1)).RectF(page.getInfo().getContentRect());
                    obj1 = getMatrix(apagerange.getWidth(), apagerange.getHeight(), ((RectF) (obj1)), fittingMode);
                    page.getCanvas().drawBitmap(apagerange, ((Matrix) (obj1)), null);
                    cancellationsignal.finishPage(page);
                    FileOutputStream fileoutputstream = JVM INSTR new #144 <Class FileOutputStream>;
                    fileoutputstream.FileOutputStream(parcelfiledescriptor.getFileDescriptor());
                    cancellationsignal.writeTo(fileoutputstream);
                    writeresultcallback.onWriteFinished(new PageRange[] {
                        PageRange.ALL_PAGES
                    });
_L1:
                    if(cancellationsignal != null)
                        cancellationsignal.close();
                    IOException ioexception;
                    if(parcelfiledescriptor != null)
                        try
                        {
                            parcelfiledescriptor.close();
                        }
                        // Misplaced declaration of an exception variable
                        catch(ParcelFileDescriptor parcelfiledescriptor) { }
                    if(apagerange != bitmap)
                        apagerange.recycle();
                    return;
                    ioexception;
                    Log.e("PrintHelperKitkat", "Error writing printed content", ioexception);
                    writeresultcallback.onWriteFailed(null);
                      goto _L1
                    writeresultcallback;
                    if(cancellationsignal != null)
                        cancellationsignal.close();
                    if(parcelfiledescriptor != null)
                        try
                        {
                            parcelfiledescriptor.close();
                        }
                        // Misplaced declaration of an exception variable
                        catch(ParcelFileDescriptor parcelfiledescriptor) { }
                    if(apagerange != bitmap)
                        apagerange.recycle();
                    throw writeresultcallback;
                }

                private PrintAttributes mAttributes;
                final PrintHelperKitkat this$0;
                final Bitmap val$bitmap;
                final OnPrintFinishCallback val$callback;
                final int val$fittingMode;
                final String val$jobName;

            
            {
                this$0 = PrintHelperKitkat.this;
                jobName = s;
                bitmap = bitmap1;
                fittingMode = i;
                callback = onprintfinishcallback;
                super();
            }
            }
, ((PrintAttributes) (obj)));
        }
    }

    public void printBitmap(final String jobName, final Uri imageFile, final OnPrintFinishCallback callback)
        throws FileNotFoundException
    {
        PrintDocumentAdapter printdocumentadapter;
        printdocumentadapter = new PrintDocumentAdapter() {

            private void cancelLoad()
            {
                synchronized(mLock)
                {
                    if(mDecodeOptions != null)
                    {
                        mDecodeOptions.requestCancelDecode();
                        mDecodeOptions = null;
                    }
                }
                return;
                exception;
                obj;
                JVM INSTR monitorexit ;
                throw exception;
            }

            public void onFinish()
            {
                super.onFinish();
                cancelLoad();
                if(mLoadBitmap != null)
                    mLoadBitmap.cancel(true);
                if(callback != null)
                    callback.onFinish();
                if(mBitmap != null)
                {
                    mBitmap.recycle();
                    mBitmap = null;
                }
            }

            public void onLayout(final PrintAttributes oldPrintAttributes, final PrintAttributes newPrintAttributes, final CancellationSignal cancellationSignal, android.print.PrintDocumentAdapter.LayoutResultCallback layoutresultcallback, Bundle bundle)
            {
                boolean flag = true;
                mAttributes = newPrintAttributes;
                if(cancellationSignal.isCanceled())
                    layoutresultcallback.onLayoutCancelled();
                else
                if(mBitmap != null)
                {
                    cancellationSignal = (new android.print.PrintDocumentInfo.Builder(jobName)).setContentType(1).setPageCount(1).build();
                    if(newPrintAttributes.equals(oldPrintAttributes))
                        flag = false;
                    layoutresultcallback.onLayoutFinished(cancellationSignal, flag);
                } else
                {
                    mLoadBitmap = (layoutresultcallback. new AsyncTask() {

                        protected transient Bitmap doInBackground(Uri auri[])
                        {
                            try
                            {
                                auri = loadConstrainedBitmap(imageFile, 3500);
                            }
                            // Misplaced declaration of an exception variable
                            catch(Uri auri[])
                            {
                                auri = null;
                            }
                            return auri;
                        }

                        protected volatile Object doInBackground(Object aobj[])
                        {
                            return doInBackground((Uri[])aobj);
                        }

                        protected void onCancelled(Bitmap bitmap)
                        {
                            layoutResultCallback.onLayoutCancelled();
                            mLoadBitmap = null;
                        }

                        protected volatile void onCancelled(Object obj)
                        {
                            onCancelled((Bitmap)obj);
                        }

                        protected void onPostExecute(Bitmap bitmap)
                        {
                            boolean flag = true;
                            super.onPostExecute(bitmap);
                            mBitmap = bitmap;
                            if(bitmap != null)
                            {
                                bitmap = (new android.print.PrintDocumentInfo.Builder(jobName)).setContentType(1).setPageCount(1).build();
                                if(newPrintAttributes.equals(oldPrintAttributes))
                                    flag = false;
                                layoutResultCallback.onLayoutFinished(bitmap, flag);
                            } else
                            {
                                layoutResultCallback.onLayoutFailed(null);
                            }
                            mLoadBitmap = null;
                        }

                        protected volatile void onPostExecute(Object obj)
                        {
                            onPostExecute((Bitmap)obj);
                        }

                        protected void onPreExecute()
                        {
                            cancellationSignal.setOnCancelListener(new android.os.CancellationSignal.OnCancelListener() {

                                public void onCancel()
                                {
                                    cancelLoad();
                                    cancel(false);
                                }

                                final _cls1 this$2;

            
            {
                this$2 = _cls1.this;
                super();
            }
                            }
);
                        }

                        final _cls2 this$1;
                        final CancellationSignal val$cancellationSignal;
                        final android.print.PrintDocumentAdapter.LayoutResultCallback val$layoutResultCallback;
                        final PrintAttributes val$newPrintAttributes;
                        final PrintAttributes val$oldPrintAttributes;

            
            {
                this$1 = final__pcls2;
                cancellationSignal = cancellationsignal;
                newPrintAttributes = printattributes;
                oldPrintAttributes = printattributes1;
                layoutResultCallback = android.print.PrintDocumentAdapter.LayoutResultCallback.this;
                super();
            }
                    }
).execute(new Uri[0]);
                }
            }

            public void onWrite(PageRange apagerange[], ParcelFileDescriptor parcelfiledescriptor, CancellationSignal cancellationsignal, android.print.PrintDocumentAdapter.WriteResultCallback writeresultcallback)
            {
                cancellationsignal = new PrintedPdfDocument(mContext, mAttributes);
                apagerange = convertBitmapForColorMode(mBitmap, mAttributes.getColorMode());
                android.graphics.pdf.PdfDocument.Page page = cancellationsignal.startPage(1);
                Object obj = JVM INSTR new #162 <Class RectF>;
                ((RectF) (obj)).RectF(page.getInfo().getContentRect());
                obj = getMatrix(mBitmap.getWidth(), mBitmap.getHeight(), ((RectF) (obj)), fittingMode);
                page.getCanvas().drawBitmap(apagerange, ((Matrix) (obj)), null);
                cancellationsignal.finishPage(page);
                FileOutputStream fileoutputstream = JVM INSTR new #203 <Class FileOutputStream>;
                fileoutputstream.FileOutputStream(parcelfiledescriptor.getFileDescriptor());
                cancellationsignal.writeTo(fileoutputstream);
                writeresultcallback.onWriteFinished(new PageRange[] {
                    PageRange.ALL_PAGES
                });
_L1:
                if(cancellationsignal != null)
                    cancellationsignal.close();
                IOException ioexception;
                if(parcelfiledescriptor != null)
                    try
                    {
                        parcelfiledescriptor.close();
                    }
                    // Misplaced declaration of an exception variable
                    catch(ParcelFileDescriptor parcelfiledescriptor) { }
                if(apagerange != mBitmap)
                    apagerange.recycle();
                return;
                ioexception;
                Log.e("PrintHelperKitkat", "Error writing printed content", ioexception);
                writeresultcallback.onWriteFailed(null);
                  goto _L1
                writeresultcallback;
                if(cancellationsignal != null)
                    cancellationsignal.close();
                if(parcelfiledescriptor != null)
                    try
                    {
                        parcelfiledescriptor.close();
                    }
                    // Misplaced declaration of an exception variable
                    catch(ParcelFileDescriptor parcelfiledescriptor) { }
                if(apagerange != mBitmap)
                    apagerange.recycle();
                throw writeresultcallback;
            }

            private PrintAttributes mAttributes;
            Bitmap mBitmap;
            AsyncTask mLoadBitmap;
            final PrintHelperKitkat this$0;
            final OnPrintFinishCallback val$callback;
            final int val$fittingMode;
            final Uri val$imageFile;
            final String val$jobName;


            
            {
                this$0 = PrintHelperKitkat.this;
                jobName = s;
                imageFile = uri;
                callback = onprintfinishcallback;
                fittingMode = i;
                super();
                mBitmap = null;
            }
        }
;
        imageFile = (PrintManager)mContext.getSystemService("print");
        callback = new android.print.PrintAttributes.Builder();
        callback.setColorMode(mColorMode);
        if(mOrientation != 1) goto _L2; else goto _L1
_L1:
        callback.setMediaSize(android.print.PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE);
_L4:
        imageFile.print(jobName, printdocumentadapter, callback.build());
        return;
_L2:
        if(mOrientation == 2)
            callback.setMediaSize(android.print.PrintAttributes.MediaSize.UNKNOWN_PORTRAIT);
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void setColorMode(int i)
    {
        mColorMode = i;
    }

    public void setOrientation(int i)
    {
        mOrientation = i;
    }

    public void setScaleMode(int i)
    {
        mScaleMode = i;
    }

    public static final int COLOR_MODE_COLOR = 2;
    public static final int COLOR_MODE_MONOCHROME = 1;
    private static final String LOG_TAG = "PrintHelperKitkat";
    private static final int MAX_PRINT_SIZE = 3500;
    public static final int ORIENTATION_LANDSCAPE = 1;
    public static final int ORIENTATION_PORTRAIT = 2;
    public static final int SCALE_MODE_FILL = 2;
    public static final int SCALE_MODE_FIT = 1;
    int mColorMode;
    final Context mContext;
    android.graphics.BitmapFactory.Options mDecodeOptions;
    private final Object mLock = new Object();
    int mOrientation;
    int mScaleMode;




}
