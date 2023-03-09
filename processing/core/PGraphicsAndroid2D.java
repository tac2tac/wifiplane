// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.core;

import android.app.Activity;
import android.app.ActivityManager;
import android.graphics.*;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.zip.GZIPInputStream;
import processing.data.XML;

// Referenced classes of package processing.core:
//            PGraphics, PMatrix3D, PApplet, PMatrix2D, 
//            PImage, PShapeSVG, PFont, PMatrix, 
//            PShape

public class PGraphicsAndroid2D extends PGraphics
{

    public PGraphicsAndroid2D()
    {
        transform = new float[9];
        path = new Path();
        rect = new RectF();
        fillPaint = new Paint();
        fillPaint.setStyle(android.graphics.Paint.Style.FILL);
        strokePaint = new Paint();
        strokePaint.setStyle(android.graphics.Paint.Style.STROKE);
        tintPaint = new Paint(2);
    }

    protected void allocate()
    {
        if(bitmap != null)
            bitmap.recycle();
        bitmap = Bitmap.createBitmap(width, height, android.graphics.Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    public void applyMatrix(float f, float f1, float f2, float f3, float f4, float f5)
    {
        Matrix matrix = new Matrix();
        matrix.setValues(new float[] {
            f, f1, f2, f3, f4, f5, 0.0F, 0.0F, 1.0F
        });
        canvas.concat(matrix);
    }

    public void applyMatrix(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8, float f9, float f10, float f11, float f12, float f13, 
            float f14, float f15)
    {
        showVariationWarning("applyMatrix");
    }

    protected void arcImpl(float f, float f1, float f2, float f3, float f4, float f5, int i)
    {
        if(f5 - f4 < 6.283185F) goto _L2; else goto _L1
_L1:
        ellipseImpl(f, f1, f2, f3);
_L4:
        return;
_L2:
        float f6 = 57.29578F * f4;
        f4 = 57.29578F * f5;
        for(f5 = f6; f5 < 0.0F;)
        {
            f5 += 360F;
            f4 += 360F;
        }

        if(f5 > f4)
        {
            f6 = f5;
        } else
        {
            f6 = f4;
            f4 = f5;
        }
        f5 = f6 - f4;
        rect.set(f, f1, f + f2, f1 + f3);
        if(i == 0)
        {
            if(fill)
                canvas.drawArc(rect, f4, f5, true, fillPaint);
            if(stroke)
                canvas.drawArc(rect, f4, f5, false, strokePaint);
        } else
        if(i == 1)
        {
            if(fill)
            {
                canvas.drawArc(rect, f4, f5, false, fillPaint);
                canvas.drawArc(rect, f4, f5, false, strokePaint);
            }
            if(stroke)
                canvas.drawArc(rect, f4, f5, false, strokePaint);
        } else
        if(i == 2)
        {
            f3 = f4 + f5;
            float f7 = rect.width() / 2.0F;
            f2 = rect.height() / 2.0F;
            float f8 = rect.centerX();
            f6 = rect.centerY();
            f = (float)((double)f7 * Math.cos(Math.toRadians(f4))) + f8;
            f1 = (float)((double)f2 * Math.sin(Math.toRadians(f4))) + f6;
            f8 = (float)((double)f7 * Math.cos(Math.toRadians(f3))) + f8;
            double d = f2;
            f2 = f6 + (float)(Math.sin(Math.toRadians(f3)) * d);
            if(fill)
            {
                canvas.drawArc(rect, f4, f5, false, fillPaint);
                canvas.drawArc(rect, f4, f5, false, strokePaint);
                canvas.drawLine(f, f1, f8, f2, strokePaint);
            }
            if(stroke)
            {
                canvas.drawArc(rect, f4, f5, false, strokePaint);
                canvas.drawLine(f, f1, f8, f2, strokePaint);
            }
        } else
        if(i == 3)
        {
            if(fill)
                canvas.drawArc(rect, f4, f5, true, fillPaint);
            if(stroke)
                canvas.drawArc(rect, f4, f5, true, strokePaint);
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void backgroundImpl()
    {
        canvas.drawColor(backgroundColor);
    }

    public void beginDraw()
    {
        checkSettings();
        resetMatrix();
        vertexCount = 0;
    }

    public void beginRaw(PGraphics pgraphics)
    {
        showMethodWarning("beginRaw");
    }

    public void beginShape(int i)
    {
        shape = i;
        vertexCount = 0;
        curveVertexCount = 0;
    }

    protected void beginTextScreenMode()
    {
        loadPixels();
    }

    public void bezierDetail(int i)
    {
    }

    public void bezierVertex(float f, float f1, float f2, float f3, float f4, float f5)
    {
        bezierVertexCheck();
        path.cubicTo(f, f1, f2, f3, f4, f5);
    }

    public void bezierVertex(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8)
    {
        showDepthWarningXYZ("bezierVertex");
    }

    public void box(float f, float f1, float f2)
    {
        showMethodWarning("box");
    }

    public void breakShape()
    {
        breakShape = true;
    }

    public void copy(int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1)
    {
        rect.set(i, j, i + k, j + l);
        Rect rect1 = new Rect(i1, j1, i1 + k1, j1 + l1);
        canvas.drawBitmap(bitmap, rect1, rect, null);
    }

    public void curveDetail(int i)
    {
    }

    public void curveVertex(float f, float f1, float f2)
    {
        showDepthWarningXYZ("curveVertex");
    }

    protected void curveVertexCheck()
    {
        super.curveVertexCheck();
        if(curveCoordX == null)
        {
            curveCoordX = new float[4];
            curveCoordY = new float[4];
            curveDrawX = new float[4];
            curveDrawY = new float[4];
        }
    }

    protected void curveVertexSegment(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7)
    {
        curveCoordX[0] = f;
        curveCoordY[0] = f1;
        curveCoordX[1] = f2;
        curveCoordY[1] = f3;
        curveCoordX[2] = f4;
        curveCoordY[2] = f5;
        curveCoordX[3] = f6;
        curveCoordY[3] = f7;
        curveToBezierMatrix.mult(curveCoordX, curveDrawX);
        curveToBezierMatrix.mult(curveCoordY, curveDrawY);
        if(vertexCount == 0)
        {
            path.moveTo(curveDrawX[0], curveDrawY[0]);
            vertexCount = 1;
        }
        path.cubicTo(curveDrawX[1], curveDrawY[1], curveDrawX[2], curveDrawY[2], curveDrawX[3], curveDrawY[3]);
    }

    public void dispose()
    {
        if(bitmap != null)
            bitmap.recycle();
    }

    protected void drawPath()
    {
        if(fill)
            canvas.drawPath(path, fillPaint);
        if(stroke)
            canvas.drawPath(path, strokePaint);
    }

    protected void ellipseImpl(float f, float f1, float f2, float f3)
    {
        rect.set(f, f1, f + f2, f1 + f3);
        if(fill)
            canvas.drawOval(rect, fillPaint);
        if(stroke)
            canvas.drawOval(rect, strokePaint);
    }

    public void endDraw()
    {
        Canvas canvas1 = null;
        if(!primaryGraphics) goto _L2; else goto _L1
_L1:
        SurfaceHolder surfaceholder;
        surfaceholder = parent.getSurfaceHolder();
        if(surfaceholder == null)
            break MISSING_BLOCK_LABEL_77;
        Canvas canvas2 = surfaceholder.lockCanvas(null);
        if(canvas2 == null)
            break MISSING_BLOCK_LABEL_66;
        canvas1 = canvas2;
        Bitmap bitmap = this.bitmap;
        canvas1 = canvas2;
        Matrix matrix = JVM INSTR new #111 <Class Matrix>;
        canvas1 = canvas2;
        matrix.Matrix();
        canvas1 = canvas2;
        canvas2.drawBitmap(bitmap, matrix, null);
        if(canvas2 != null)
            surfaceholder.unlockCanvasAndPost(canvas2);
_L4:
        setModified();
        super.updatePixels();
        return;
        Exception exception;
        exception;
        if(canvas1 != null)
            surfaceholder.unlockCanvasAndPost(canvas1);
        throw exception;
_L2:
        loadPixels();
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void endRaw()
    {
        showMethodWarning("endRaw");
    }

    public void endShape(int i)
    {
        if(shape != 3 || !stroke || vertexCount <= 0) goto _L2; else goto _L1
_L1:
        Matrix matrix = getMatrixImp();
        if(strokeWeight == 1.0F && matrix.isIdentity())
        {
            if(screenPoint == null)
                screenPoint = new float[2];
            for(i = 0; i < vertexCount; i++)
            {
                screenPoint[0] = vertices[i][0];
                screenPoint[1] = vertices[i][1];
                matrix.mapPoints(screenPoint);
                set(PApplet.round(screenPoint[0]), PApplet.round(screenPoint[1]), strokeColor);
                float f = vertices[i][0];
                float f2 = vertices[i][1];
                set(PApplet.round(screenX(f, f2)), PApplet.round(screenY(f, f2)), strokeColor);
            }

        } else
        {
            float f1 = strokeWeight / 2.0F;
            strokePaint.setStyle(android.graphics.Paint.Style.FILL);
            for(i = 0; i < vertexCount; i++)
            {
                float f3 = vertices[i][0];
                float f4 = vertices[i][1];
                rect.set(f3 - f1, f4 - f1, f3 + f1, f4 + f1);
                canvas.drawOval(rect, strokePaint);
            }

            strokePaint.setStyle(android.graphics.Paint.Style.STROKE);
        }
_L4:
        shape = 0;
        return;
_L2:
        if(shape == 20 && !path.isEmpty())
        {
            if(i == 2)
                path.close();
            drawPath();
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected void endTextScreenMode()
    {
        updatePixels();
    }

    protected void fillFromCalc()
    {
        super.fillFromCalc();
        fillPaint.setColor(fillColor);
        fillPaint.setShader(null);
    }

    public int get(int i, int j)
    {
        if(i < 0 || j < 0 || i >= width || j >= height)
            i = 0;
        else
            i = bitmap.getPixel(i, j);
        return i;
    }

    public PImage get()
    {
        return get(0, 0, width, height);
    }

    public PMatrix2D getMatrix(PMatrix2D pmatrix2d)
    {
        if(pmatrix2d == null)
            pmatrix2d = new PMatrix2D();
        getMatrixImp().getValues(transform);
        pmatrix2d.set(transform[0], transform[1], transform[2], transform[3], transform[4], transform[5]);
        return pmatrix2d;
    }

    public PMatrix3D getMatrix(PMatrix3D pmatrix3d)
    {
        showVariationWarning("getMatrix");
        return pmatrix3d;
    }

    public PMatrix getMatrix()
    {
        return getMatrix((PMatrix2D)null);
    }

    protected Matrix getMatrixImp()
    {
        return parent.getSurfaceView().getMatrix();
    }

    protected void imageImpl(PImage pimage, float f, float f1, float f2, float f3, int i, int j, 
            int k, int l)
    {
        Object obj;
        Object obj1;
        if(pimage.bitmap != null && pimage.bitmap.isRecycled())
            pimage.bitmap = null;
        if(pimage.bitmap == null && pimage.format == 4)
        {
            pimage.bitmap = Bitmap.createBitmap(pimage.width, pimage.height, android.graphics.Bitmap.Config.ARGB_8888);
            int ai[] = new int[pimage.pixels.length];
            for(int i1 = 0; i1 < ai.length; i1++)
                ai[i1] = pimage.pixels[i1] << 24 | 0xffffff;

            pimage.bitmap.setPixels(ai, 0, pimage.width, 0, 0, pimage.width, pimage.height);
            pimage.modified = false;
        }
        if(pimage.bitmap == null || pimage.width != pimage.bitmap.getWidth() || pimage.height != pimage.bitmap.getHeight())
        {
            if(pimage.bitmap != null)
                pimage.bitmap.recycle();
            pimage.bitmap = Bitmap.createBitmap(pimage.width, pimage.height, android.graphics.Bitmap.Config.ARGB_8888);
            pimage.modified = true;
        }
        if(pimage.modified)
        {
            if(!pimage.bitmap.isMutable())
            {
                pimage.bitmap.recycle();
                pimage.bitmap = Bitmap.createBitmap(pimage.width, pimage.height, android.graphics.Bitmap.Config.ARGB_8888);
            }
            if(pimage.pixels != null)
                pimage.bitmap.setPixels(pimage.pixels, 0, pimage.width, 0, 0, pimage.width, pimage.height);
            pimage.modified = false;
        }
        Canvas canvas1;
        Bitmap bitmap;
        Rect rect1;
        if(imageImplSrcRect == null)
        {
            imageImplSrcRect = new Rect(i, j, k, l);
            imageImplDstRect = new RectF(f, f1, f2, f3);
        } else
        {
            imageImplSrcRect.set(i, j, k, l);
            imageImplDstRect.set(f, f1, f2, f3);
        }
        canvas1 = canvas;
        bitmap = pimage.bitmap;
        rect1 = imageImplSrcRect;
        obj1 = imageImplDstRect;
        if(tint)
            obj = tintPaint;
        else
            obj = null;
        canvas1.drawBitmap(bitmap, rect1, ((RectF) (obj1)), ((Paint) (obj)));
        obj1 = new android.app.ActivityManager.MemoryInfo();
        obj = parent.getActivity();
        if(obj != null) goto _L2; else goto _L1
_L1:
        return;
_L2:
        ((ActivityManager)((Activity) (obj)).getSystemService("activity")).getMemoryInfo(((android.app.ActivityManager.MemoryInfo) (obj1)));
        if(((android.app.ActivityManager.MemoryInfo) (obj1)).lowMemory)
        {
            pimage.bitmap.recycle();
            pimage.bitmap = null;
        }
        if(true) goto _L1; else goto _L3
_L3:
    }

    public void line(float f, float f1, float f2, float f3)
    {
        if(stroke)
            canvas.drawLine(f, f1, f2, f3, strokePaint);
    }

    public void loadPixels()
    {
        if(pixels == null || pixels.length != width * height)
            pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
    }

    public PShape loadShape(String s)
    {
        String s1 = PApplet.getExtension(s);
        if(s1.equals("svg"))
            s = new PShapeSVG(parent.loadXML(s));
        else
        if(s1.equals("svgz"))
        {
            try
            {
                GZIPInputStream gzipinputstream = JVM INSTR new #509 <Class GZIPInputStream>;
                gzipinputstream.GZIPInputStream(parent.createInput(s));
                XML xml = JVM INSTR new #518 <Class XML>;
                xml.XML(gzipinputstream);
                s = JVM INSTR new #498 <Class PShapeSVG>;
                s.PShapeSVG(xml);
            }
            // Misplaced declaration of an exception variable
            catch(String s)
            {
                s.printStackTrace();
                s = null;
            }
        } else
        {
            PGraphics.showWarning("Unsupported format");
            s = null;
        }
        return s;
    }

    public void mask(PImage pimage)
    {
        showMethodWarning("mask");
    }

    public void mask(int ai[])
    {
        showMethodWarning("mask");
    }

    public void noSmooth()
    {
        super.noSmooth();
        strokePaint.setAntiAlias(false);
        fillPaint.setAntiAlias(false);
    }

    public void point(float f, float f1)
    {
        beginShape(3);
        vertex(f, f1);
        endShape();
    }

    public void popMatrix()
    {
        canvas.restore();
    }

    public void printMatrix()
    {
        getMatrix((PMatrix2D)null).print();
    }

    public void pushMatrix()
    {
        canvas.save(1);
    }

    public void quad(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7)
    {
        path.reset();
        path.moveTo(f, f1);
        path.lineTo(f2, f3);
        path.lineTo(f4, f5);
        path.lineTo(f6, f7);
        path.close();
        drawPath();
    }

    public void quadraticVertex(float f, float f1, float f2, float f3)
    {
        bezierVertexCheck();
        path.quadTo(f, f1, f2, f3);
    }

    public void quadraticVertex(float f, float f1, float f2, float f3, float f4, float f5)
    {
        showDepthWarningXYZ("quadVertex");
    }

    protected void rectImpl(float f, float f1, float f2, float f3)
    {
        if(fill)
            canvas.drawRect(f, f1, f2, f3, fillPaint);
        if(stroke)
            canvas.drawRect(f, f1, f2, f3, strokePaint);
    }

    public void requestDraw()
    {
        parent.handleDraw();
    }

    public void resetMatrix()
    {
        canvas.setMatrix(new Matrix());
    }

    public void resize(int i, int j)
    {
        showMethodWarning("resize");
    }

    public void rotate(float f)
    {
        canvas.rotate(57.29578F * f);
    }

    public void rotate(float f, float f1, float f2, float f3)
    {
        showVariationWarning("rotate");
    }

    public void rotateX(float f)
    {
        showDepthWarning("rotateX");
    }

    public void rotateY(float f)
    {
        showDepthWarning("rotateY");
    }

    public void rotateZ(float f)
    {
        showDepthWarning("rotateZ");
    }

    public void scale(float f)
    {
        canvas.scale(f, f);
    }

    public void scale(float f, float f1)
    {
        canvas.scale(f, f1);
    }

    public void scale(float f, float f1, float f2)
    {
        showDepthWarningXYZ("scale");
    }

    public float screenX(float f, float f1)
    {
        if(screenPoint == null)
            screenPoint = new float[2];
        screenPoint[0] = f;
        screenPoint[1] = f1;
        getMatrixImp().mapPoints(screenPoint);
        return screenPoint[0];
    }

    public float screenX(float f, float f1, float f2)
    {
        showDepthWarningXYZ("screenX");
        return 0.0F;
    }

    public float screenY(float f, float f1)
    {
        if(screenPoint == null)
            screenPoint = new float[2];
        screenPoint[0] = f;
        screenPoint[1] = f1;
        getMatrixImp().mapPoints(screenPoint);
        return screenPoint[1];
    }

    public float screenY(float f, float f1, float f2)
    {
        showDepthWarningXYZ("screenY");
        return 0.0F;
    }

    public float screenZ(float f, float f1, float f2)
    {
        showDepthWarningXYZ("screenZ");
        return 0.0F;
    }

    public void set(int i, int j, int k)
    {
        if(i >= 0 && j >= 0 && i < width && j < height)
            bitmap.setPixel(i, j, k);
    }

    public void set(int i, int j, PImage pimage)
    {
        if(pimage.format == 4)
            throw new RuntimeException("set() not available for ALPHA images");
        if(pimage.bitmap == null)
        {
            pimage.bitmap = Bitmap.createBitmap(pimage.width, pimage.height, android.graphics.Bitmap.Config.ARGB_8888);
            pimage.modified = true;
        }
        if(pimage.width != pimage.bitmap.getWidth() || pimage.height != pimage.bitmap.getHeight())
        {
            pimage.bitmap.recycle();
            pimage.bitmap = Bitmap.createBitmap(pimage.width, pimage.height, android.graphics.Bitmap.Config.ARGB_8888);
            pimage.modified = true;
        }
        if(pimage.modified)
        {
            if(!pimage.bitmap.isMutable())
            {
                pimage.bitmap.recycle();
                pimage.bitmap = Bitmap.createBitmap(pimage.width, pimage.height, android.graphics.Bitmap.Config.ARGB_8888);
            }
            pimage.bitmap.setPixels(pimage.pixels, 0, pimage.width, 0, 0, pimage.width, pimage.height);
            pimage.modified = false;
        }
        canvas.save(1);
        canvas.setMatrix(null);
        canvas.drawBitmap(pimage.bitmap, i, j, null);
        canvas.restore();
    }

    public void setMatrix(PMatrix2D pmatrix2d)
    {
        Matrix matrix = new Matrix();
        matrix.setValues(new float[] {
            pmatrix2d.m00, pmatrix2d.m01, pmatrix2d.m02, pmatrix2d.m10, pmatrix2d.m11, pmatrix2d.m12, 0.0F, 0.0F, 1.0F
        });
        canvas.setMatrix(matrix);
    }

    public void setMatrix(PMatrix3D pmatrix3d)
    {
        showVariationWarning("setMatrix");
    }

    public void setSize(int i, int j)
    {
        width = i;
        height = j;
        width1 = width - 1;
        height1 = height - 1;
        allocate();
        reapplySettings();
    }

    public void shearX(float f)
    {
        canvas.skew((float)Math.tan(f), 0.0F);
    }

    public void shearY(float f)
    {
        canvas.skew(0.0F, (float)Math.tan(f));
    }

    public void smooth(int i)
    {
        super.smooth(i);
        strokePaint.setAntiAlias(true);
        fillPaint.setAntiAlias(true);
    }

    public void sphere(float f)
    {
        showMethodWarning("sphere");
    }

    public void strokeCap(int i)
    {
        super.strokeCap(i);
        if(strokeCap == 2)
            strokePaint.setStrokeCap(android.graphics.Paint.Cap.ROUND);
        else
        if(strokeCap == 4)
            strokePaint.setStrokeCap(android.graphics.Paint.Cap.SQUARE);
        else
            strokePaint.setStrokeCap(android.graphics.Paint.Cap.BUTT);
    }

    protected void strokeFromCalc()
    {
        super.strokeFromCalc();
        strokePaint.setColor(strokeColor);
        strokePaint.setShader(null);
    }

    public void strokeJoin(int i)
    {
        super.strokeJoin(i);
        if(strokeJoin == 8)
            strokePaint.setStrokeJoin(android.graphics.Paint.Join.MITER);
        else
        if(strokeJoin == 2)
            strokePaint.setStrokeJoin(android.graphics.Paint.Join.ROUND);
        else
            strokePaint.setStrokeJoin(android.graphics.Paint.Join.BEVEL);
    }

    public void strokeWeight(float f)
    {
        super.strokeWeight(f);
        strokePaint.setStrokeWidth(f);
    }

    public void textFont(PFont pfont)
    {
        super.textFont(pfont);
        fillPaint.setTypeface((Typeface)pfont.getNative());
    }

    protected void textLineImpl(char ac[], int i, int j, float f, float f1)
    {
        if((Typeface)textFont.getNative() == null)
        {
            showWarning("Inefficient font rendering: use createFont() with a TTF/OTF instead of loadFont().");
            super.textLineImpl(ac, i, j, f, f1);
        } else
        {
            fillPaint.setAntiAlias(textFont.smooth);
            canvas.drawText(ac, i, j - i, f, f1, fillPaint);
            ac = fillPaint;
            boolean flag;
            if(smooth > 0)
                flag = true;
            else
                flag = false;
            ac.setAntiAlias(flag);
        }
    }

    protected boolean textModeCheck(int i)
    {
        boolean flag;
        if(i == 4)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public void textSize(float f)
    {
        if(textFont == null)
            defaultFontOrDeath("textSize", f);
        if((Typeface)textFont.getNative() != null)
            fillPaint.setTextSize(f);
        textSize = f;
        textLeading = (textAscent() + textDescent()) * 1.275F;
    }

    protected float textWidthImpl(char ac[], int i, int j)
    {
        float f;
        if((Typeface)textFont.getNative() == null)
            f = super.textWidthImpl(ac, i, j);
        else
            f = fillPaint.measureText(ac, i, j - i);
        return f;
    }

    public void texture(PImage pimage)
    {
        showMethodWarning("texture");
    }

    protected void tintFromCalc()
    {
        super.tintFromCalc();
        tintPaint.setColorFilter(new PorterDuffColorFilter(tintColor, android.graphics.PorterDuff.Mode.MULTIPLY));
    }

    public void translate(float f, float f1)
    {
        canvas.translate(f, f1);
    }

    public void triangle(float f, float f1, float f2, float f3, float f4, float f5)
    {
        path.reset();
        path.moveTo(f, f1);
        path.lineTo(f2, f3);
        path.lineTo(f4, f5);
        path.close();
        drawPath();
    }

    public void updatePixels()
    {
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
    }

    public void updatePixels(int i, int j, int k, int l)
    {
        if(i != 0 || j != 0 || k != width || l != height)
            showVariationWarning("updatePixels(x, y, w, h)");
        updatePixels();
    }

    public void vertex(float f, float f1)
    {
        if(shape != 20) goto _L2; else goto _L1
_L1:
        if(vertexCount == 0)
        {
            path.reset();
            path.moveTo(f, f1);
            vertexCount = 1;
        } else
        if(breakShape)
        {
            path.moveTo(f, f1);
            breakShape = false;
        } else
        {
            path.lineTo(f, f1);
        }
_L4:
        return;
_L2:
        curveVertexCount = 0;
        if(vertexCount == vertices.length)
        {
            int i = vertexCount;
            float af[][] = new float[i << 1][37];
            System.arraycopy(vertices, 0, af, 0, vertexCount);
            vertices = af;
        }
        vertices[vertexCount][0] = f;
        vertices[vertexCount][1] = f1;
        vertexCount = vertexCount + 1;
        switch(shape)
        {
        case 5: // '\005'
            if(vertexCount % 2 == 0)
            {
                line(vertices[vertexCount - 2][0], vertices[vertexCount - 2][1], f, f1);
                vertexCount = 0;
            }
            break;

        case 9: // '\t'
            if(vertexCount % 3 == 0)
            {
                triangle(vertices[vertexCount - 3][0], vertices[vertexCount - 3][1], vertices[vertexCount - 2][0], vertices[vertexCount - 2][1], f, f1);
                vertexCount = 0;
            }
            break;

        case 10: // '\n'
            if(vertexCount >= 3)
                triangle(vertices[vertexCount - 2][0], vertices[vertexCount - 2][1], f, f1, vertices[vertexCount - 3][0], vertices[vertexCount - 3][1]);
            break;

        case 11: // '\013'
            if(vertexCount >= 3)
                triangle(vertices[0][0], vertices[0][1], vertices[vertexCount - 2][0], vertices[vertexCount - 2][1], f, f1);
            break;

        case 16: // '\020'
        case 17: // '\021'
            if(vertexCount % 4 == 0)
            {
                quad(vertices[vertexCount - 4][0], vertices[vertexCount - 4][1], vertices[vertexCount - 3][0], vertices[vertexCount - 3][1], vertices[vertexCount - 2][0], vertices[vertexCount - 2][1], f, f1);
                vertexCount = 0;
            }
            break;

        case 18: // '\022'
            if(vertexCount >= 4 && vertexCount % 2 == 0)
                quad(vertices[vertexCount - 4][0], vertices[vertexCount - 4][1], vertices[vertexCount - 2][0], vertices[vertexCount - 2][1], f, f1, vertices[vertexCount - 3][0], vertices[vertexCount - 3][1]);
            break;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void vertex(float f, float f1, float f2)
    {
        showDepthWarningXYZ("vertex");
    }

    public void vertex(float f, float f1, float f2, float f3)
    {
        showVariationWarning("vertex(x, y, u, v)");
    }

    public void vertex(float f, float f1, float f2, float f3, float f4)
    {
        showDepthWarningXYZ("vertex");
    }

    static int getset[] = new int[1];
    boolean breakShape;
    public Canvas canvas;
    float curveCoordX[];
    float curveCoordY[];
    float curveDrawX[];
    float curveDrawY[];
    Paint fillPaint;
    RectF imageImplDstRect;
    Rect imageImplSrcRect;
    Path path;
    RectF rect;
    float screenPoint[];
    Paint strokePaint;
    Paint tintPaint;
    float transform[];

}
