// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.core;

import android.graphics.Color;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.WeakHashMap;
import java.util.concurrent.*;
import processing.opengl.PGL;
import processing.opengl.PShader;

// Referenced classes of package processing.core:
//            PImage, PConstants, PStyle, PMatrix3D, 
//            PApplet, PMatrix2D, PShape, PFont, 
//            PMatrix

public class PGraphics extends PImage
    implements PConstants
{
    protected static class AsyncImageSaver
    {

        public void dispose()
        {
            saveExecutor.shutdown();
            saveExecutor.awaitTermination(5000L, TimeUnit.SECONDS);
_L2:
            return;
            InterruptedException interruptedexception;
            interruptedexception;
            if(true) goto _L2; else goto _L1
_L1:
        }

        public PImage getAvailableTarget(int i, int j, int k)
        {
            if(targetsCreated >= TARGET_COUNT || !targetPool.isEmpty()) goto _L2; else goto _L1
_L1:
            Object obj;
            obj = JVM INSTR new #100 <Class PImage>;
            ((PImage) (obj)).PImage(i, j);
            targetsCreated = targetsCreated + 1;
_L5:
            obj.format = k;
_L6:
            return ((PImage) (obj));
_L2:
            PImage pimage = (PImage)targetPool.take();
            if(pimage.width != i) goto _L4; else goto _L3
_L3:
            obj = pimage;
            if(pimage.height == j) goto _L5; else goto _L4
_L4:
            pimage.width = i;
            pimage.height = j;
            pimage.pixels = new int[i * j];
            obj = pimage;
              goto _L5
            InterruptedException interruptedexception;
            interruptedexception;
            interruptedexception = null;
              goto _L6
        }

        public boolean hasAvailableTarget()
        {
            boolean flag;
            if(targetsCreated < TARGET_COUNT || targetPool.isEmpty())
                flag = true;
            else
                flag = false;
            return flag;
        }

        public void returnUnusedTarget(PImage pimage)
        {
            targetPool.offer(pimage);
        }

        public void saveTargetAsync(PGraphics pgraphics, PImage pimage, String s)
        {
            pimage.parent = pgraphics.parent;
            if(pimage.parent.frameCount - 1 == lastFrameCount && TARGET_COUNT > 1)
            {
                long l = avgNanos / (long)Math.max(1, TARGET_COUNT - 1);
                long l1 = System.nanoTime();
                l1 = PApplet.round((float)((l + lastTime) - l1) / 1000000F);
                ExecutorService executorservice;
                Runnable runnable;
                if(l1 > 0L)
                    try
                    {
                        Thread.sleep(l1);
                    }
                    catch(InterruptedException interruptedexception) { }
            }
            lastFrameCount = pimage.parent.frameCount;
            lastTime = System.nanoTime();
            executorservice = saveExecutor;
            runnable = JVM INSTR new #9   <Class PGraphics$AsyncImageSaver$1>;
            pimage.s. _cls1();
            executorservice.submit(runnable);
_L2:
            return;
            pgraphics;
            if(true) goto _L2; else goto _L1
_L1:
        }

        static final int TARGET_COUNT = Math.max(1, Runtime.getRuntime().availableProcessors() - 1);
        static final int TIME_AVG_FACTOR = 32;
        volatile long avgNanos;
        int lastFrameCount;
        long lastTime;
        ExecutorService saveExecutor;
        BlockingQueue targetPool;
        int targetsCreated;


        public AsyncImageSaver()
        {
            targetPool = new ArrayBlockingQueue(TARGET_COUNT);
            saveExecutor = Executors.newFixedThreadPool(TARGET_COUNT);
            targetsCreated = 0;
            avgNanos = 0L;
            lastTime = 0L;
            lastFrameCount = 0;
        }
    }


    public PGraphics()
    {
        hints = new boolean[12];
        cacheMap = new WeakHashMap();
        fillColor = -1;
        strokeColor = 0xff000000;
        strokeWeight = 1.0F;
        strokeJoin = 8;
        strokeCap = 2;
        imageMode = 0;
        textAlign = 21;
        textAlignY = 0;
        textMode = 4;
        styleStack = new PStyle[64];
        backgroundColor = 0xffcccccc;
        cacheHsbValue = new float[3];
        vertices = new float[512][37];
        bezierInited = false;
        bezierDetail = 20;
        bezierBasisMatrix = new PMatrix3D(-1F, 3F, -3F, 1.0F, 3F, -6F, 3F, 0.0F, -3F, 3F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        curveInited = false;
        curveDetail = 20;
        curveTightness = 0.0F;
        textBuffer = new char[8192];
        textWidthBuffer = new char[8192];
        edge = true;
        textureMode = 2;
        textureImage = null;
        sphereDetailU = 0;
        sphereDetailV = 0;
    }

    public static int lerpColor(int i, int j, float f, int k)
    {
        int l = 0;
        if(k != 1) goto _L2; else goto _L1
_L1:
        float f1 = i >> 24 & 0xff;
        float f2 = i >> 16 & 0xff;
        float f3 = i >> 8 & 0xff;
        float f4 = i & 0xff;
        float f5 = j >> 24 & 0xff;
        float f7 = j >> 16 & 0xff;
        float f8 = j >> 8 & 0xff;
        float f9 = j & 0xff;
        l = (int)(f1 + (f5 - f1) * f) << 24 | (int)(f2 + (f7 - f2) * f) << 16 | (int)((f8 - f3) * f + f3) << 8 | (int)((f9 - f4) * f + f4);
_L4:
        return l;
_L2:
        if(k == 3)
        {
            if(lerpColorHSB1 == null)
            {
                lerpColorHSB1 = new float[3];
                lerpColorHSB2 = new float[3];
                lerpColorHSB3 = new float[3];
            }
            float f6 = i >> 24 & 0xff;
            k = (int)(f6 + ((float)(j >> 24 & 0xff) - f6) * f);
            Color.RGBToHSV(i >> 16 & 0xff, i >> 8 & 0xff, i & 0xff, lerpColorHSB1);
            Color.RGBToHSV(j >> 16 & 0xff, j >> 8 & 0xff, j & 0xff, lerpColorHSB2);
            lerpColorHSB3[0] = PApplet.lerp(lerpColorHSB1[0], lerpColorHSB2[0], f);
            lerpColorHSB3[1] = PApplet.lerp(lerpColorHSB1[1], lerpColorHSB2[1], f);
            lerpColorHSB3[2] = PApplet.lerp(lerpColorHSB1[2], lerpColorHSB2[2], f);
            l = Color.HSVToColor(k << 24, lerpColorHSB3);
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public static void showDepthWarning(String s)
    {
        showWarning((new StringBuilder()).append(s).append("() can only be used with a renderer that supports 3D, such as P3D or OPENGL.").toString());
    }

    public static void showDepthWarningXYZ(String s)
    {
        showWarning((new StringBuilder()).append(s).append("() with x, y, and z coordinates can only be used with a renderer that supports 3D, such as P3D or OPENGL. Use a version without a z-coordinate instead.").toString());
    }

    public static void showException(String s)
    {
        throw new RuntimeException(s);
    }

    public static void showMethodWarning(String s)
    {
        showWarning((new StringBuilder()).append(s).append("() is not available with this renderer.").toString());
    }

    public static void showMissingWarning(String s)
    {
        showWarning((new StringBuilder()).append(s).append("(), or this particular variation of it, is not available with this renderer.").toString());
    }

    public static void showVariationWarning(String s)
    {
        showWarning((new StringBuilder()).append(s).append(" is not available with this renderer.").toString());
    }

    public static void showWarning(String s)
    {
        if(warnings == null)
            warnings = new HashMap();
        if(!warnings.containsKey(s))
        {
            System.err.println(s);
            warnings.put(s, new Object());
        }
    }

    public static transient void showWarning(String s, Object aobj[])
    {
        showWarning(String.format(s, aobj));
    }

    private void smoothWarning(String s)
    {
        showWarning("%s() can only be used before beginDraw()", new Object[] {
            s
        });
    }

    private boolean textSentence(char ac[], int i, int j, float f, float f1)
    {
        int i1;
        float f2;
        int j1;
        int k = i;
        i1 = i;
        f2 = 0.0F;
        j1 = i;
        i = k;
_L11:
        if(i > j)
            break MISSING_BLOCK_LABEL_200;
        if(ac[i] != ' ' && i != j) goto _L2; else goto _L1
_L1:
        float f3 = textWidthImpl(ac, i1, i);
        if(f2 + f3 <= f) goto _L4; else goto _L3
_L3:
        if(f2 == 0.0F) goto _L6; else goto _L5
_L5:
        textSentenceBreak(j1, i1);
        do
        {
            i = i1;
            if(i1 >= j)
                break;
            i = i1;
            if(ac[i1] != ' ')
                break;
            i1++;
        } while(true);
          goto _L7
_L6:
        int l = i - 1;
        if(l != i1) goto _L9; else goto _L8
_L8:
        boolean flag = false;
_L12:
        return flag;
_L9:
        i = l;
        if(textWidthImpl(ac, i1, l) > f) goto _L6; else goto _L10
_L10:
        textSentenceBreak(j1, l);
        i = l;
_L7:
        i1 = i;
        j1 = i;
        f2 = 0.0F;
          goto _L11
_L4:
        if(i == j)
        {
            textSentenceBreak(j1, i);
            i++;
        } else
        {
            f2 += f3 + f1;
            i1 = i + 1;
            i++;
        }
          goto _L11
_L2:
        i++;
          goto _L11
        flag = true;
          goto _L12
    }

    private void textSentenceBreak(int i, int j)
    {
        if(textBreakCount == textBreakStart.length)
        {
            textBreakStart = PApplet.expand(textBreakStart);
            textBreakStop = PApplet.expand(textBreakStop);
        }
        textBreakStart[textBreakCount] = i;
        textBreakStop[textBreakCount] = j;
        textBreakCount = textBreakCount + 1;
    }

    protected void allocate()
    {
    }

    public final float alpha(int i)
    {
        float f = i >> 24 & 0xff;
        if(colorModeA != 255F)
            f = (f / 255F) * colorModeA;
        return f;
    }

    public void ambient(float f)
    {
        colorCalc(f);
        ambientFromCalc();
    }

    public void ambient(float f, float f1, float f2)
    {
        colorCalc(f, f1, f2);
        ambientFromCalc();
    }

    public void ambient(int i)
    {
        colorCalc(i);
        ambientFromCalc();
    }

    protected void ambientFromCalc()
    {
        ambientColor = calcColor;
        ambientR = calcR;
        ambientG = calcG;
        ambientB = calcB;
        setAmbient = true;
    }

    public void ambientLight(float f, float f1, float f2)
    {
        showMethodWarning("ambientLight");
    }

    public void ambientLight(float f, float f1, float f2, float f3, float f4, float f5)
    {
        showMethodWarning("ambientLight");
    }

    public void applyMatrix(float f, float f1, float f2, float f3, float f4, float f5)
    {
        showMissingWarning("applyMatrix");
    }

    public void applyMatrix(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8, float f9, float f10, float f11, float f12, float f13, 
            float f14, float f15)
    {
        showMissingWarning("applyMatrix");
    }

    public void applyMatrix(PMatrix2D pmatrix2d)
    {
        applyMatrix(pmatrix2d.m00, pmatrix2d.m01, pmatrix2d.m02, pmatrix2d.m10, pmatrix2d.m11, pmatrix2d.m12);
    }

    public void applyMatrix(PMatrix3D pmatrix3d)
    {
        applyMatrix(pmatrix3d.m00, pmatrix3d.m01, pmatrix3d.m02, pmatrix3d.m03, pmatrix3d.m10, pmatrix3d.m11, pmatrix3d.m12, pmatrix3d.m13, pmatrix3d.m20, pmatrix3d.m21, pmatrix3d.m22, pmatrix3d.m23, pmatrix3d.m30, pmatrix3d.m31, pmatrix3d.m32, pmatrix3d.m33);
    }

    public void applyMatrix(PMatrix pmatrix)
    {
        if(!(pmatrix instanceof PMatrix2D)) goto _L2; else goto _L1
_L1:
        applyMatrix((PMatrix2D)pmatrix);
_L4:
        return;
_L2:
        if(pmatrix instanceof PMatrix3D)
            applyMatrix((PMatrix3D)pmatrix);
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void arc(float f, float f1, float f2, float f3, float f4, float f5)
    {
        arc(f, f1, f2, f3, f4, f5, 0);
    }

    public void arc(float f, float f1, float f2, float f3, float f4, float f5, int i)
    {
        if(ellipseMode == 1)
        {
            f3 -= f1;
            f2 -= f;
        } else
        if(ellipseMode == 2)
        {
            f6 = f3 * 2.0F;
            float f7 = f2 * 2.0F;
            f1 -= f3;
            f -= f2;
            f2 = f7;
            f3 = f6;
        } else
        if(ellipseMode == 3)
        {
            f6 = f2 / 2.0F;
            float f8 = f3 / 2.0F;
            f1 -= f8;
            f -= f6;
        }
        if(!Float.isInfinite(f4) && !Float.isInfinite(f5) && f5 > f4)
        {
            float f6 = f5;
            f5 = f4;
            for(f4 = f6; f5 < 0.0F; f4 += 6.283185F)
                f5 += 6.283185F;

            float f9 = f5;
            f6 = f4;
            if(f4 - f5 > 6.283185F)
            {
                f9 = 0.0F;
                f6 = 6.283185F;
            }
            arcImpl(f, f1, f2, f3, f9, f6, i);
        }
    }

    protected void arcImpl(float f, float f1, float f2, float f3, float f4, float f5, int i)
    {
        showMissingWarning("arc");
    }

    public transient void attrib(String s, float af[])
    {
        showMissingWarning("attrib");
    }

    public transient void attrib(String s, int ai[])
    {
        showMissingWarning("attrib");
    }

    public transient void attrib(String s, boolean aflag[])
    {
        showMissingWarning("attrib");
    }

    public void attribColor(String s, int i)
    {
        showMissingWarning("attrib");
    }

    public void attribNormal(String s, float f, float f1, float f2)
    {
        showMissingWarning("attrib");
    }

    public void attribPosition(String s, float f, float f1, float f2)
    {
        showMissingWarning("attrib");
    }

    public void background(float f)
    {
        colorCalc(f);
        backgroundFromCalc();
    }

    public void background(float f, float f1)
    {
        if(format == 1)
        {
            background(f);
        } else
        {
            colorCalc(f, f1);
            backgroundFromCalc();
        }
    }

    public void background(float f, float f1, float f2)
    {
        colorCalc(f, f1, f2);
        backgroundFromCalc();
    }

    public void background(float f, float f1, float f2, float f3)
    {
        colorCalc(f, f1, f2, f3);
        backgroundFromCalc();
    }

    public void background(int i)
    {
        colorCalc(i);
        backgroundFromCalc();
    }

    public void background(int i, float f)
    {
        colorCalc(i, f);
        backgroundFromCalc();
    }

    public void background(PImage pimage)
    {
        if(pimage.width != width || pimage.height != height)
            throw new RuntimeException("background image must be the same size as your application");
        if(pimage.format != 1 && pimage.format != 2)
        {
            throw new RuntimeException("background images should be RGB or ARGB");
        } else
        {
            backgroundColor = 0;
            backgroundImpl(pimage);
            return;
        }
    }

    protected void backgroundFromCalc()
    {
        backgroundR = calcR;
        backgroundG = calcG;
        backgroundB = calcB;
        float f;
        int i;
        boolean flag;
        if(format == 1)
            f = colorModeA;
        else
            f = calcA;
        backgroundA = f;
        backgroundRi = calcRi;
        backgroundGi = calcGi;
        backgroundBi = calcBi;
        if(format == 1)
            i = 255;
        else
            i = calcAi;
        backgroundAi = i;
        if(format == 1)
            flag = false;
        else
            flag = calcAlpha;
        backgroundAlpha = flag;
        backgroundColor = calcColor;
        backgroundImpl();
    }

    protected void backgroundImpl()
    {
        pushStyle();
        pushMatrix();
        resetMatrix();
        fill(backgroundColor);
        rect(0.0F, 0.0F, width, height);
        popMatrix();
        popStyle();
    }

    protected void backgroundImpl(PImage pimage)
    {
        set(0, 0, pimage);
    }

    public void beginCamera()
    {
        showMethodWarning("beginCamera");
    }

    public void beginContour()
    {
        showMissingWarning("beginContour");
    }

    public void beginDraw()
    {
    }

    public PGL beginPGL()
    {
        showMethodWarning("beginPGL");
        return null;
    }

    public void beginRaw(PGraphics pgraphics)
    {
        raw = pgraphics;
        pgraphics.beginDraw();
    }

    public void beginShape()
    {
        beginShape(20);
    }

    public void beginShape(int i)
    {
        shape = i;
    }

    public void bezier(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7)
    {
        beginShape();
        vertex(f, f1);
        bezierVertex(f2, f3, f4, f5, f6, f7);
        endShape();
    }

    public void bezier(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8, float f9, float f10, float f11)
    {
        beginShape();
        vertex(f, f1, f2);
        bezierVertex(f3, f4, f5, f6, f7, f8, f9, f10, f11);
        endShape();
    }

    public void bezierDetail(int i)
    {
        bezierDetail = i;
        if(bezierDrawMatrix == null)
            bezierDrawMatrix = new PMatrix3D();
        splineForward(i, bezierDrawMatrix);
        bezierDrawMatrix.apply(bezierBasisMatrix);
    }

    protected void bezierInit()
    {
        bezierDetail(bezierDetail);
        bezierInited = true;
    }

    protected void bezierInitCheck()
    {
        if(!bezierInited)
            bezierInit();
    }

    public float bezierPoint(float f, float f1, float f2, float f3, float f4)
    {
        float f5 = 1.0F - f4;
        return f5 * (3F * f2 * f4 * f4) + (f * f5 * f5 * f5 + 3F * f1 * f4 * f5 * f5) + f3 * f4 * f4 * f4;
    }

    public float bezierTangent(float f, float f1, float f2, float f3, float f4)
    {
        return 3F * f4 * f4 * (((-f + 3F * f1) - 3F * f2) + f3) + 6F * f4 * ((f - 2.0F * f1) + f2) + (-f + f1) * 3F;
    }

    public void bezierVertex(float f, float f1, float f2, float f3, float f4, float f5)
    {
        bezierInitCheck();
        bezierVertexCheck();
        PMatrix3D pmatrix3d = bezierDrawMatrix;
        float af[] = vertices[vertexCount - 1];
        float f6 = af[0];
        float f7 = af[1];
        float f8 = pmatrix3d.m10 * f6 + pmatrix3d.m11 * f + pmatrix3d.m12 * f2 + pmatrix3d.m13 * f4;
        float f9 = pmatrix3d.m20 * f6 + pmatrix3d.m21 * f + pmatrix3d.m22 * f2 + pmatrix3d.m23 * f4;
        float f10 = pmatrix3d.m30;
        float f11 = pmatrix3d.m31;
        float f12 = pmatrix3d.m32;
        float f13 = pmatrix3d.m33;
        float f14 = pmatrix3d.m10;
        float f15 = pmatrix3d.m11;
        float f16 = pmatrix3d.m12;
        float f17 = pmatrix3d.m13 * f5 + (f14 * f7 + f15 * f1 + f16 * f3);
        f16 = pmatrix3d.m20 * f7 + pmatrix3d.m21 * f1 + pmatrix3d.m22 * f3 + pmatrix3d.m23 * f5;
        float f18 = pmatrix3d.m30;
        float f19 = pmatrix3d.m31;
        float f20 = pmatrix3d.m32;
        float f21 = pmatrix3d.m33;
        int i = 0;
        f14 = f6;
        f15 = f7;
        for(; i < bezierDetail; i++)
        {
            f14 += f8;
            f8 += f9;
            f9 += f10 * f6 + f11 * f + f12 * f2 + f13 * f4;
            f15 += f17;
            f17 += f16;
            f16 += f18 * f7 + f19 * f1 + f20 * f3 + f21 * f5;
            vertex(f14, f15);
        }

    }

    public void bezierVertex(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8)
    {
        bezierInitCheck();
        bezierVertexCheck();
        PMatrix3D pmatrix3d = bezierDrawMatrix;
        float af[] = vertices[vertexCount - 1];
        float f9 = af[0];
        float f10 = af[1];
        float f11 = af[2];
        float f12 = pmatrix3d.m10 * f9 + pmatrix3d.m11 * f + pmatrix3d.m12 * f3 + pmatrix3d.m13 * f6;
        float f13 = pmatrix3d.m20 * f9 + pmatrix3d.m21 * f + pmatrix3d.m22 * f3 + pmatrix3d.m23 * f6;
        float f14 = pmatrix3d.m30;
        float f15 = pmatrix3d.m31;
        float f16 = pmatrix3d.m32;
        float f17 = pmatrix3d.m33;
        float f18 = pmatrix3d.m10 * f10 + pmatrix3d.m11 * f1 + pmatrix3d.m12 * f4 + pmatrix3d.m13 * f7;
        float f19 = pmatrix3d.m20 * f10 + pmatrix3d.m21 * f1 + pmatrix3d.m22 * f4 + pmatrix3d.m23 * f7;
        float f20 = pmatrix3d.m30;
        float f21 = pmatrix3d.m31;
        float f22 = pmatrix3d.m32;
        float f23 = pmatrix3d.m33;
        float f24 = pmatrix3d.m10;
        float f25 = pmatrix3d.m11;
        float f26 = pmatrix3d.m12;
        float f27 = pmatrix3d.m13 * f8 + (f24 * f11 + f25 * f2 + f26 * f5);
        float f28 = pmatrix3d.m20 * f11 + pmatrix3d.m21 * f2 + pmatrix3d.m22 * f5 + pmatrix3d.m23 * f8;
        float f29 = pmatrix3d.m30;
        float f30 = pmatrix3d.m31;
        float f31 = pmatrix3d.m32;
        float f32 = pmatrix3d.m33;
        int i = 0;
        f25 = f9;
        f24 = f10;
        f26 = f11;
        for(; i < bezierDetail; i++)
        {
            f25 += f12;
            f12 += f13;
            f13 += f14 * f9 + f15 * f + f16 * f3 + f17 * f6;
            f24 += f18;
            f18 += f19;
            f19 += f20 * f10 + f21 * f1 + f22 * f4 + f23 * f7;
            f26 += f27;
            f27 += f28;
            f28 += f29 * f11 + f30 * f2 + f31 * f5 + f32 * f8;
            vertex(f25, f24, f26);
        }

    }

    protected void bezierVertexCheck()
    {
        bezierVertexCheck(shape, vertexCount);
    }

    protected void bezierVertexCheck(int i, int j)
    {
        if(i == 0 || i != 20)
            throw new RuntimeException("beginShape() or beginShape(POLYGON) must be used before bezierVertex() or quadraticVertex()");
        if(j == 0)
            throw new RuntimeException("vertex() must be used at least oncebefore bezierVertex() or quadraticVertex()");
        else
            return;
    }

    public void blendMode(int i)
    {
        blendMode = i;
        blendModeImpl();
    }

    protected void blendModeImpl()
    {
        if(blendMode != 1)
            showMissingWarning("blendMode");
    }

    public final float blue(int i)
    {
        float f = i & 0xff;
        if(!colorModeDefault)
            f = (f / 255F) * colorModeZ;
        return f;
    }

    public void box(float f)
    {
        box(f, f, f);
    }

    public void box(float f, float f1, float f2)
    {
        float f3 = -f / 2.0F;
        f /= 2.0F;
        float f4 = -f1 / 2.0F;
        f1 /= 2.0F;
        float f5 = -f2 / 2.0F;
        f2 /= 2.0F;
        beginShape(17);
        normal(0.0F, 0.0F, 1.0F);
        vertex(f3, f4, f5);
        vertex(f, f4, f5);
        vertex(f, f1, f5);
        vertex(f3, f1, f5);
        normal(1.0F, 0.0F, 0.0F);
        vertex(f, f4, f5);
        vertex(f, f4, f2);
        vertex(f, f1, f2);
        vertex(f, f1, f5);
        normal(0.0F, 0.0F, -1F);
        vertex(f, f4, f2);
        vertex(f3, f4, f2);
        vertex(f3, f1, f2);
        vertex(f, f1, f2);
        normal(-1F, 0.0F, 0.0F);
        vertex(f3, f4, f2);
        vertex(f3, f4, f5);
        vertex(f3, f1, f5);
        vertex(f3, f1, f2);
        normal(0.0F, 1.0F, 0.0F);
        vertex(f3, f4, f2);
        vertex(f, f4, f2);
        vertex(f, f4, f5);
        vertex(f3, f4, f5);
        normal(0.0F, -1F, 0.0F);
        vertex(f3, f1, f5);
        vertex(f, f1, f5);
        vertex(f, f1, f2);
        vertex(f3, f1, f2);
        endShape();
    }

    public void breakShape()
    {
        showWarning("This renderer cannot currently handle concave shapes, or shapes with holes.");
    }

    public final float brightness(int i)
    {
        if(i != cacheHsbKey)
        {
            Color.RGBToHSV(i >> 16 & 0xff, i >> 8 & 0xff, i & 0xff, cacheHsbValue);
            cacheHsbKey = i;
        }
        return cacheHsbValue[2] * colorModeZ;
    }

    public void camera()
    {
        showMissingWarning("camera");
    }

    public void camera(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8)
    {
        showMissingWarning("camera");
    }

    public boolean canDraw()
    {
        return true;
    }

    protected void checkSettings()
    {
        if(!settingsInited)
            defaultSettings();
    }

    public void clear()
    {
        background(0.0F, 0.0F, 0.0F, 0.0F);
    }

    public void clip(float f, float f1, float f2, float f3)
    {
        if(imageMode != 0) goto _L2; else goto _L1
_L1:
        float f4 = f;
        float f5 = f2;
        if(f2 < 0.0F)
        {
            f4 = f + f2;
            f5 = -f2;
        }
        f2 = f1;
        f = f3;
        if(f3 < 0.0F)
        {
            f2 = f1 + f3;
            f = -f3;
        }
        clipImpl(f4, f2, f4 + f5, f2 + f);
_L4:
        return;
_L2:
        if(imageMode == 1)
        {
            float f6;
            if(f2 >= f)
            {
                float f7 = f;
                f = f2;
                f2 = f7;
            }
            if(f3 < f1)
            {
                f6 = f3;
                f3 = f1;
            } else
            {
                f6 = f1;
            }
            clipImpl(f2, f6, f, f3);
        } else
        if(imageMode == 3)
        {
            f6 = f2;
            if(f2 < 0.0F)
                f6 = -f2;
            f2 = f3;
            if(f3 < 0.0F)
                f2 = -f3;
            f -= f6 / 2.0F;
            f1 -= f2 / 2.0F;
            clipImpl(f, f1, f + f6, f1 + f2);
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected void clipImpl(float f, float f1, float f2, float f3)
    {
        showMissingWarning("clip");
    }

    public final int color(float f)
    {
        colorCalc(f);
        return calcColor;
    }

    public final int color(float f, float f1)
    {
        colorCalc(f, f1);
        return calcColor;
    }

    public final int color(float f, float f1, float f2)
    {
        colorCalc(f, f1, f2);
        return calcColor;
    }

    public final int color(float f, float f1, float f2, float f3)
    {
        colorCalc(f, f1, f2, f3);
        return calcColor;
    }

    public final int color(int i)
    {
        if((i & 0xff000000) != 0 || (float)i > colorModeX) goto _L2; else goto _L1
_L1:
        if(!colorModeDefault) goto _L4; else goto _L3
_L3:
        int j;
        if(i > 255)
        {
            j = 255;
        } else
        {
            j = i;
            if(i < 0)
                j = 0;
        }
        i = j << 16 | 0xff000000 | j << 8 | j;
_L5:
        return i;
_L4:
        colorCalc(i);
_L6:
        i = calcColor;
        if(true) goto _L5; else goto _L2
_L2:
        colorCalcARGB(i, colorModeA);
          goto _L6
    }

    public final int color(int i, float f)
    {
        if((0xff000000 & i) == 0 && (float)i <= colorModeX)
            colorCalc(i, f);
        else
            colorCalcARGB(i, f);
        return calcColor;
    }

    public final int color(int i, int j)
    {
        if(colorModeDefault)
        {
            int k;
            if(i > 255)
            {
                k = 255;
            } else
            {
                k = i;
                if(i < 0)
                    k = 0;
            }
            if(j > 255)
            {
                i = 255;
            } else
            {
                i = j;
                if(j < 0)
                    i = 0;
            }
            i = (i & 0xff) << 24 | k << 16 | k << 8 | k;
        } else
        {
            colorCalc(i, j);
            i = calcColor;
        }
        return i;
    }

    public final int color(int i, int j, int k)
    {
        if(colorModeDefault)
        {
            int l;
            if(i > 255)
            {
                l = 255;
            } else
            {
                l = i;
                if(i < 0)
                    l = 0;
            }
            if(j > 255)
            {
                i = 255;
            } else
            {
                i = j;
                if(j < 0)
                    i = 0;
            }
            if(k > 255)
            {
                j = 255;
            } else
            {
                j = k;
                if(k < 0)
                    j = 0;
            }
            i = 0xff000000 | l << 16 | i << 8 | j;
        } else
        {
            colorCalc(i, j, k);
            i = calcColor;
        }
        return i;
    }

    public final int color(int i, int j, int k, int l)
    {
        if(colorModeDefault)
        {
            int i1;
            if(l > 255)
            {
                i1 = 255;
            } else
            {
                i1 = l;
                if(l < 0)
                    i1 = 0;
            }
            if(i > 255)
            {
                l = 255;
            } else
            {
                l = i;
                if(i < 0)
                    l = 0;
            }
            if(j > 255)
            {
                i = 255;
            } else
            {
                i = j;
                if(j < 0)
                    i = 0;
            }
            if(k > 255)
            {
                j = 255;
            } else
            {
                j = k;
                if(k < 0)
                    j = 0;
            }
            i = i1 << 24 | l << 16 | i << 8 | j;
        } else
        {
            colorCalc(i, j, k, l);
            i = calcColor;
        }
        return i;
    }

    protected void colorCalc(float f)
    {
        colorCalc(f, colorModeA);
    }

    protected void colorCalc(float f, float f1)
    {
        float f2 = 0.0F;
        float f3 = f;
        if(f > colorModeX)
            f3 = colorModeX;
        boolean flag;
        if(f1 > colorModeA)
            f = colorModeA;
        else
            f = f1;
        if(f3 < 0.0F)
            f1 = 0.0F;
        else
            f1 = f3;
        if(f < 0.0F)
            f = f2;
        if(colorModeScale)
            f1 /= colorModeX;
        calcR = f1;
        calcG = calcR;
        calcB = calcR;
        f1 = f;
        if(colorModeScale)
            f1 = f / colorModeA;
        calcA = f1;
        calcRi = (int)(calcR * 255F);
        calcGi = (int)(calcG * 255F);
        calcBi = (int)(calcB * 255F);
        calcAi = (int)(calcA * 255F);
        calcColor = calcAi << 24 | calcRi << 16 | calcGi << 8 | calcBi;
        if(calcAi != 255)
            flag = true;
        else
            flag = false;
        calcAlpha = flag;
    }

    protected void colorCalc(float f, float f1, float f2)
    {
        colorCalc(f, f1, f2, colorModeA);
    }

    protected void colorCalc(float f, float f1, float f2, float f3)
    {
        float f4 = f;
        if(f > colorModeX)
            f4 = colorModeX;
        float f5 = f1;
        if(f1 > colorModeY)
            f5 = colorModeY;
        f = f2;
        if(f2 > colorModeZ)
            f = colorModeZ;
        boolean flag;
        if(f3 > colorModeA)
            f1 = colorModeA;
        else
            f1 = f3;
        f2 = f4;
        if(f4 < 0.0F)
            f2 = 0.0F;
        f3 = f5;
        if(f5 < 0.0F)
            f3 = 0.0F;
        f4 = f;
        if(f < 0.0F)
            f4 = 0.0F;
        f = f1;
        if(f1 < 0.0F)
            f = 0.0F;
        colorMode;
        JVM INSTR tableswitch 1 3: default 144
    //                   1 248
    //                   2 144
    //                   3 325;
           goto _L1 _L2 _L1 _L3
_L1:
        calcRi = (int)(calcR * 255F);
        calcGi = (int)(calcG * 255F);
        calcBi = (int)(calcB * 255F);
        calcAi = (int)(calcA * 255F);
        calcColor = calcAi << 24 | calcRi << 16 | calcGi << 8 | calcBi;
        if(calcAi != 255)
            flag = true;
        else
            flag = false;
        calcAlpha = flag;
        return;
_L2:
        if(colorModeScale)
        {
            calcR = f2 / colorModeX;
            calcG = f3 / colorModeY;
            calcB = f4 / colorModeZ;
            calcA = f / colorModeA;
        } else
        {
            calcR = f2;
            calcG = f3;
            calcB = f4;
            calcA = f;
        }
          goto _L1
_L3:
        f5 = f2 / colorModeX;
        f3 /= colorModeY;
        f2 = f4 / colorModeZ;
        f1 = f;
        if(colorModeScale)
            f1 = f / colorModeA;
        calcA = f1;
        if(f3 != 0.0F) goto _L5; else goto _L4
_L4:
        calcB = f2;
        calcG = f2;
        calcR = f2;
          goto _L1
_L5:
        f1 = (f5 - (float)(int)f5) * 6F;
        f5 = f1 - (float)(int)f1;
        f4 = (1.0F - f3) * f2;
        f = (1.0F - f3 * f5) * f2;
        f3 = (1.0F - (1.0F - f5) * f3) * f2;
        switch((int)f1)
        {
        case 0: // '\0'
            calcR = f2;
            calcG = f3;
            calcB = f4;
            break;

        case 1: // '\001'
            calcR = f;
            calcG = f2;
            calcB = f4;
            break;

        case 2: // '\002'
            calcR = f4;
            calcG = f2;
            calcB = f3;
            break;

        case 3: // '\003'
            calcR = f4;
            calcG = f;
            calcB = f2;
            break;

        case 4: // '\004'
            calcR = f3;
            calcG = f4;
            calcB = f2;
            break;

        case 5: // '\005'
            calcR = f2;
            calcG = f4;
            calcB = f;
            break;
        }
        if(true) goto _L1; else goto _L6
_L6:
    }

    protected void colorCalc(int i)
    {
        if((0xff000000 & i) == 0 && (float)i <= colorModeX)
            colorCalc(i);
        else
            colorCalcARGB(i, colorModeA);
    }

    protected void colorCalc(int i, float f)
    {
        if((0xff000000 & i) == 0 && (float)i <= colorModeX)
            colorCalc(i, f);
        else
            colorCalcARGB(i, f);
    }

    protected void colorCalcARGB(int i, float f)
    {
        boolean flag;
        if(f == colorModeA)
        {
            calcAi = i >> 24 & 0xff;
            calcColor = i;
        } else
        {
            calcAi = (int)((float)(i >> 24 & 0xff) * (f / colorModeA));
            calcColor = calcAi << 24 | 0xffffff & i;
        }
        calcRi = i >> 16 & 0xff;
        calcGi = i >> 8 & 0xff;
        calcBi = i & 0xff;
        calcA = (float)calcAi / 255F;
        calcR = (float)calcRi / 255F;
        calcG = (float)calcGi / 255F;
        calcB = (float)calcBi / 255F;
        if(calcAi != 255)
            flag = true;
        else
            flag = false;
        calcAlpha = flag;
    }

    public void colorMode(int i)
    {
        colorMode(i, colorModeX, colorModeY, colorModeZ, colorModeA);
    }

    public void colorMode(int i, float f)
    {
        colorMode(i, f, f, f, f);
    }

    public void colorMode(int i, float f, float f1, float f2)
    {
        colorMode(i, f, f1, f2, colorModeA);
    }

    public void colorMode(int i, float f, float f1, float f2, float f3)
    {
        boolean flag = true;
        colorMode = i;
        colorModeX = f;
        colorModeY = f1;
        colorModeZ = f2;
        colorModeA = f3;
        boolean flag1;
        if(f3 != 1.0F || f != f1 || f1 != f2 || f2 != f3)
            flag1 = true;
        else
            flag1 = false;
        colorModeScale = flag1;
        if(colorMode == 1 && colorModeA == 255F && colorModeX == 255F && colorModeY == 255F && colorModeZ == 255F)
            flag1 = flag;
        else
            flag1 = false;
        colorModeDefault = flag1;
    }

    public PShape createShape()
    {
        return createShape(3);
    }

    public PShape createShape(int i)
    {
        if(i == 0 || i == 2 || i == 3)
            return createShapeFamily(i);
        else
            throw new IllegalArgumentException("Only GROUP, PShape.PATH, and PShape.GEOMETRY work with createShape()");
    }

    public transient PShape createShape(int i, float af[])
    {
        int j = af.length;
        if(i == 2)
        {
            if(is3D() && j != 2 && j != 3)
                throw new IllegalArgumentException("Use createShape(POINT, x, y) or createShape(POINT, x, y, z)");
            if(j != 2)
                throw new IllegalArgumentException("Use createShape(POINT, x, y)");
            af = createShapePrimitive(i, af);
        } else
        if(i == 4)
        {
            if(is3D() && j != 4 && j != 6)
                throw new IllegalArgumentException("Use createShape(LINE, x1, y1, x2, y2) or createShape(LINE, x1, y1, z1, x2, y2, z1)");
            if(j != 4)
                throw new IllegalArgumentException("Use createShape(LINE, x1, y1, x2, y2)");
            af = createShapePrimitive(i, af);
        } else
        if(i == 8)
        {
            if(j != 6)
                throw new IllegalArgumentException("Use createShape(TRIANGLE, x1, y1, x2, y2, x3, y3)");
            af = createShapePrimitive(i, af);
        } else
        if(i == 16)
        {
            if(j != 8)
                throw new IllegalArgumentException("Use createShape(QUAD, x1, y1, x2, y2, x3, y3, x4, y4)");
            af = createShapePrimitive(i, af);
        } else
        if(i == 30)
        {
            if(j != 4 && j != 5 && j != 8 && j != 9)
                throw new IllegalArgumentException("Wrong number of parameters for createShape(RECT), see the reference");
            af = createShapePrimitive(i, af);
        } else
        if(i == 31)
        {
            if(j != 4 && j != 5)
                throw new IllegalArgumentException("Use createShape(ELLIPSE, x, y, w, h) or createShape(ELLIPSE, x, y, w, h, mode)");
            af = createShapePrimitive(i, af);
        } else
        if(i == 32)
        {
            if(j != 6 && j != 7)
                throw new IllegalArgumentException("Use createShape(ARC, x, y, w, h, start, stop)");
            af = createShapePrimitive(i, af);
        } else
        if(i == 41)
        {
            if(!is3D())
                throw new IllegalArgumentException("createShape(BOX) is not supported in 2D");
            if(j != 1 && j != 3)
                throw new IllegalArgumentException("Use createShape(BOX, size) or createShape(BOX, width, height, depth)");
            af = createShapePrimitive(i, af);
        } else
        if(i == 40)
        {
            if(!is3D())
                throw new IllegalArgumentException("createShape(SPHERE) is not supported in 2D");
            if(j != 1)
                throw new IllegalArgumentException("Use createShape(SPHERE, radius)");
            af = createShapePrimitive(i, af);
        } else
        {
            throw new IllegalArgumentException("Unknown shape type passed to createShape()");
        }
        return af;
    }

    protected PShape createShapeFamily(int i)
    {
        return new PShape(this, i);
    }

    protected transient PShape createShapePrimitive(int i, float af[])
    {
        return new PShape(this, i, af);
    }

    public void curve(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7)
    {
        beginShape();
        curveVertex(f, f1);
        curveVertex(f2, f3);
        curveVertex(f4, f5);
        curveVertex(f6, f7);
        endShape();
    }

    public void curve(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8, float f9, float f10, float f11)
    {
        beginShape();
        curveVertex(f, f1, f2);
        curveVertex(f3, f4, f5);
        curveVertex(f6, f7, f8);
        curveVertex(f9, f10, f11);
        endShape();
    }

    public void curveDetail(int i)
    {
        curveDetail = i;
        curveInit();
    }

    protected void curveInit()
    {
        if(curveDrawMatrix == null)
        {
            curveBasisMatrix = new PMatrix3D();
            curveDrawMatrix = new PMatrix3D();
            curveInited = true;
        }
        float f = curveTightness;
        curveBasisMatrix.set((f - 1.0F) / 2.0F, (3F + f) / 2.0F, (-3F - f) / 2.0F, (1.0F - f) / 2.0F, 1.0F - f, (-5F - f) / 2.0F, 2.0F + f, (f - 1.0F) / 2.0F, (f - 1.0F) / 2.0F, 0.0F, (1.0F - f) / 2.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F);
        splineForward(curveDetail, curveDrawMatrix);
        if(bezierBasisInverse == null)
        {
            bezierBasisInverse = bezierBasisMatrix.get();
            bezierBasisInverse.invert();
            curveToBezierMatrix = new PMatrix3D();
        }
        curveToBezierMatrix.set(curveBasisMatrix);
        curveToBezierMatrix.preApply(bezierBasisInverse);
        curveDrawMatrix.apply(curveBasisMatrix);
    }

    protected void curveInitCheck()
    {
        if(!curveInited)
            curveInit();
    }

    public float curvePoint(float f, float f1, float f2, float f3, float f4)
    {
        curveInitCheck();
        float f5 = f4 * f4;
        float f6 = f4 * f5;
        PMatrix3D pmatrix3d = curveBasisMatrix;
        float f7 = pmatrix3d.m00;
        float f8 = pmatrix3d.m10;
        float f9 = pmatrix3d.m20;
        float f10 = pmatrix3d.m30;
        float f11 = pmatrix3d.m01;
        float f12 = pmatrix3d.m11;
        float f13 = pmatrix3d.m21;
        float f14 = pmatrix3d.m31;
        float f15 = pmatrix3d.m02;
        float f16 = pmatrix3d.m12;
        float f17 = pmatrix3d.m22;
        float f18 = pmatrix3d.m32;
        float f19 = pmatrix3d.m03;
        return (f5 * pmatrix3d.m13 + f6 * f19 + pmatrix3d.m23 * f4 + pmatrix3d.m33) * f3 + ((f7 * f6 + f8 * f5 + f9 * f4 + f10) * f + (f11 * f6 + f12 * f5 + f13 * f4 + f14) * f1 + (f15 * f6 + f16 * f5 + f17 * f4 + f18) * f2);
    }

    public float curveTangent(float f, float f1, float f2, float f3, float f4)
    {
        curveInitCheck();
        float f5 = f4 * f4 * 3F;
        f4 = 2.0F * f4;
        PMatrix3D pmatrix3d = curveBasisMatrix;
        float f6 = pmatrix3d.m00;
        float f7 = pmatrix3d.m10;
        float f8 = pmatrix3d.m20;
        float f9 = pmatrix3d.m01;
        float f10 = pmatrix3d.m11;
        float f11 = pmatrix3d.m21;
        float f12 = pmatrix3d.m02;
        float f13 = pmatrix3d.m12;
        float f14 = pmatrix3d.m22;
        return (f5 * pmatrix3d.m03 + f4 * pmatrix3d.m13 + pmatrix3d.m23) * f3 + ((f6 * f5 + f7 * f4 + f8) * f + (f9 * f5 + f10 * f4 + f11) * f1 + (f12 * f5 + f13 * f4 + f14) * f2);
    }

    public void curveTightness(float f)
    {
        curveTightness = f;
        curveInit();
    }

    public void curveVertex(float f, float f1)
    {
        curveVertexCheck();
        float af[] = curveVertices[curveVertexCount];
        af[0] = f;
        af[1] = f1;
        curveVertexCount = curveVertexCount + 1;
        if(curveVertexCount > 3)
            curveVertexSegment(curveVertices[curveVertexCount - 4][0], curveVertices[curveVertexCount - 4][1], curveVertices[curveVertexCount - 3][0], curveVertices[curveVertexCount - 3][1], curveVertices[curveVertexCount - 2][0], curveVertices[curveVertexCount - 2][1], curveVertices[curveVertexCount - 1][0], curveVertices[curveVertexCount - 1][1]);
    }

    public void curveVertex(float f, float f1, float f2)
    {
        curveVertexCheck();
        float af[] = curveVertices[curveVertexCount];
        af[0] = f;
        af[1] = f1;
        af[2] = f2;
        curveVertexCount = curveVertexCount + 1;
        if(curveVertexCount > 3)
            curveVertexSegment(curveVertices[curveVertexCount - 4][0], curveVertices[curveVertexCount - 4][1], curveVertices[curveVertexCount - 4][2], curveVertices[curveVertexCount - 3][0], curveVertices[curveVertexCount - 3][1], curveVertices[curveVertexCount - 3][2], curveVertices[curveVertexCount - 2][0], curveVertices[curveVertexCount - 2][1], curveVertices[curveVertexCount - 2][2], curveVertices[curveVertexCount - 1][0], curveVertices[curveVertexCount - 1][1], curveVertices[curveVertexCount - 1][2]);
    }

    protected void curveVertexCheck()
    {
        curveVertexCheck(shape);
    }

    protected void curveVertexCheck(int i)
    {
        if(i != 20)
            throw new RuntimeException("You must use beginShape() or beginShape(POLYGON) before curveVertex()");
        if(curveVertices == null)
            curveVertices = new float[128][3];
        if(curveVertexCount == curveVertices.length)
        {
            i = curveVertexCount;
            float af[][] = new float[i << 1][3];
            System.arraycopy(curveVertices, 0, af, 0, curveVertexCount);
            curveVertices = af;
        }
        curveInitCheck();
    }

    protected void curveVertexSegment(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7)
    {
        PMatrix3D pmatrix3d = curveDrawMatrix;
        float f8 = pmatrix3d.m10 * f + pmatrix3d.m11 * f2 + pmatrix3d.m12 * f4 + pmatrix3d.m13 * f6;
        float f9 = pmatrix3d.m20 * f + pmatrix3d.m21 * f2 + pmatrix3d.m22 * f4 + pmatrix3d.m23 * f6;
        float f10 = pmatrix3d.m30;
        float f11 = pmatrix3d.m31;
        float f12 = pmatrix3d.m32;
        float f13 = pmatrix3d.m33;
        float f14 = pmatrix3d.m10;
        float f15 = pmatrix3d.m11;
        float f16 = pmatrix3d.m12;
        f16 = pmatrix3d.m13 * f7 + (f14 * f1 + f15 * f3 + f16 * f5);
        float f17 = pmatrix3d.m20 * f1 + pmatrix3d.m21 * f3 + pmatrix3d.m22 * f5 + pmatrix3d.m23 * f7;
        float f18 = pmatrix3d.m30;
        float f19 = pmatrix3d.m31;
        float f20 = pmatrix3d.m32;
        float f21 = pmatrix3d.m33;
        int i = curveVertexCount;
        vertex(f2, f3);
        int j = 0;
        f15 = f3;
        f14 = f2;
        for(; j < curveDetail; j++)
        {
            f14 += f8;
            f8 += f9;
            f9 += f10 * f + f11 * f2 + f12 * f4 + f13 * f6;
            f15 += f16;
            f16 += f17;
            f17 += f18 * f1 + f19 * f3 + f20 * f5 + f21 * f7;
            vertex(f14, f15);
        }

        curveVertexCount = i;
    }

    protected void curveVertexSegment(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8, float f9, float f10, float f11)
    {
        PMatrix3D pmatrix3d = curveDrawMatrix;
        float f12 = pmatrix3d.m10 * f + pmatrix3d.m11 * f3 + pmatrix3d.m12 * f6 + pmatrix3d.m13 * f9;
        float f13 = pmatrix3d.m20 * f + pmatrix3d.m21 * f3 + pmatrix3d.m22 * f6 + pmatrix3d.m23 * f9;
        float f14 = pmatrix3d.m30;
        float f15 = pmatrix3d.m31;
        float f16 = pmatrix3d.m32;
        float f17 = pmatrix3d.m33;
        float f18 = pmatrix3d.m10 * f1 + pmatrix3d.m11 * f4 + pmatrix3d.m12 * f7 + pmatrix3d.m13 * f10;
        float f19 = pmatrix3d.m20 * f1 + pmatrix3d.m21 * f4 + pmatrix3d.m22 * f7 + pmatrix3d.m23 * f10;
        float f20 = pmatrix3d.m30;
        float f21 = pmatrix3d.m31;
        float f22 = pmatrix3d.m32;
        float f23 = pmatrix3d.m33;
        int i = curveVertexCount;
        float f24 = pmatrix3d.m10;
        float f25 = pmatrix3d.m11;
        float f26 = pmatrix3d.m12;
        float f27 = pmatrix3d.m13 * f11 + (f24 * f2 + f25 * f5 + f26 * f8);
        float f28 = pmatrix3d.m20 * f2 + pmatrix3d.m21 * f5 + pmatrix3d.m22 * f8 + pmatrix3d.m23 * f11;
        float f29 = pmatrix3d.m30;
        float f30 = pmatrix3d.m31;
        float f31 = pmatrix3d.m32;
        float f32 = pmatrix3d.m33;
        vertex(f3, f4, f5);
        int j = 0;
        f24 = f5;
        f26 = f4;
        f25 = f3;
        for(; j < curveDetail; j++)
        {
            f25 += f12;
            f12 += f13;
            f13 += f14 * f + f15 * f3 + f16 * f6 + f17 * f9;
            f26 += f18;
            f18 += f19;
            f19 += f20 * f1 + f21 * f4 + f22 * f7 + f23 * f10;
            f24 += f27;
            f27 += f28;
            f28 += f29 * f2 + f30 * f5 + f31 * f8 + f32 * f11;
            vertex(f25, f26, f24);
        }

        curveVertexCount = i;
    }

    protected void defaultFontOrDeath(String s)
    {
        defaultFontOrDeath(s, 12F);
    }

    protected void defaultFontOrDeath(String s, float f)
    {
        if(parent != null)
        {
            textFont = parent.createDefaultFont(f);
            return;
        } else
        {
            throw new RuntimeException((new StringBuilder()).append("Use textFont() before ").append(s).append("()").toString());
        }
    }

    protected void defaultSettings()
    {
        smooth();
        colorMode(1, 255F);
        fill(255);
        stroke(0);
        strokeWeight(1.0F);
        strokeJoin(8);
        strokeCap(2);
        shape = 0;
        rectMode(0);
        ellipseMode(3);
        autoNormal = true;
        textFont = null;
        textSize = 12F;
        textLeading = 14F;
        textAlign = 21;
        textMode = 4;
        if(primaryGraphics)
            background(backgroundColor);
        blendMode(1);
        settingsInited = true;
    }

    public void directionalLight(float f, float f1, float f2, float f3, float f4, float f5)
    {
        showMethodWarning("directionalLight");
    }

    public boolean displayable()
    {
        return true;
    }

    public void dispose()
    {
        parent = null;
    }

    public void edge(boolean flag)
    {
        edge = flag;
    }

    public void ellipse(float f, float f1, float f2, float f3)
    {
        float f5;
        float f9;
        if(ellipseMode == 1)
        {
            float f4 = f3 - f1;
            f2 -= f;
            f3 = f;
            f = f4;
        } else
        if(ellipseMode == 2)
        {
            float f10 = f3 * 2.0F;
            float f6 = f2 * 2.0F;
            f1 -= f3;
            f3 = f - f2;
            f = f10;
            f2 = f6;
        } else
        if(ellipseMode == 3)
        {
            float f7 = f2 / 2.0F;
            float f11 = f3 / 2.0F;
            f1 -= f11;
            f7 = f - f7;
            f = f3;
            f3 = f7;
        } else
        {
            float f8 = f;
            f = f3;
            f3 = f8;
        }
        f9 = f2;
        f5 = f3;
        if(f2 < 0.0F)
        {
            f5 = f3 + f2;
            f9 = -f2;
        }
        f3 = f;
        f2 = f1;
        if(f < 0.0F)
        {
            f2 = f1 + f;
            f3 = -f;
        }
        ellipseImpl(f5, f2, f9, f3);
    }

    protected void ellipseImpl(float f, float f1, float f2, float f3)
    {
    }

    public void ellipseMode(int i)
    {
        ellipseMode = i;
    }

    public void emissive(float f)
    {
        colorCalc(f);
        emissiveFromCalc();
    }

    public void emissive(float f, float f1, float f2)
    {
        colorCalc(f, f1, f2);
        emissiveFromCalc();
    }

    public void emissive(int i)
    {
        colorCalc(i);
        emissiveFromCalc();
    }

    protected void emissiveFromCalc()
    {
        emissiveColor = calcColor;
        emissiveR = calcR;
        emissiveG = calcG;
        emissiveB = calcB;
    }

    public void endCamera()
    {
        showMethodWarning("endCamera");
    }

    public void endContour()
    {
        showMissingWarning("endContour");
    }

    public void endDraw()
    {
    }

    public void endPGL()
    {
        showMethodWarning("endPGL");
    }

    public void endRaw()
    {
        if(raw != null)
        {
            flush();
            raw.endDraw();
            raw.dispose();
            raw = null;
        }
    }

    public void endShape()
    {
        endShape(1);
    }

    public void endShape(int i)
    {
    }

    public void fill(float f)
    {
        colorCalc(f);
        fillFromCalc();
    }

    public void fill(float f, float f1)
    {
        colorCalc(f, f1);
        fillFromCalc();
    }

    public void fill(float f, float f1, float f2)
    {
        colorCalc(f, f1, f2);
        fillFromCalc();
    }

    public void fill(float f, float f1, float f2, float f3)
    {
        colorCalc(f, f1, f2, f3);
        fillFromCalc();
    }

    public void fill(int i)
    {
        colorCalc(i);
        fillFromCalc();
    }

    public void fill(int i, float f)
    {
        colorCalc(i, f);
        fillFromCalc();
    }

    protected void fillFromCalc()
    {
        fill = true;
        fillR = calcR;
        fillG = calcG;
        fillB = calcB;
        fillA = calcA;
        fillRi = calcRi;
        fillGi = calcGi;
        fillBi = calcBi;
        fillAi = calcAi;
        fillColor = calcColor;
        fillAlpha = calcAlpha;
    }

    public void filter(PShader pshader)
    {
        showMissingWarning("filter");
    }

    public void flush()
    {
    }

    public void frustum(float f, float f1, float f2, float f3, float f4, float f5)
    {
        showMethodWarning("frustum");
    }

    public Object getCache(PImage pimage)
    {
        return cacheMap.get(pimage);
    }

    public PMatrix2D getMatrix(PMatrix2D pmatrix2d)
    {
        showMissingWarning("getMatrix");
        return null;
    }

    public PMatrix3D getMatrix(PMatrix3D pmatrix3d)
    {
        showMissingWarning("getMatrix");
        return null;
    }

    public PMatrix getMatrix()
    {
        showMissingWarning("getMatrix");
        return null;
    }

    public PGraphics getRaw()
    {
        return raw;
    }

    public PShader getShader(int i)
    {
        showMissingWarning("getShader");
        return null;
    }

    public PStyle getStyle()
    {
        return getStyle(null);
    }

    public PStyle getStyle(PStyle pstyle)
    {
        PStyle pstyle1 = pstyle;
        if(pstyle == null)
            pstyle1 = new PStyle();
        pstyle1.imageMode = imageMode;
        pstyle1.rectMode = rectMode;
        pstyle1.ellipseMode = ellipseMode;
        pstyle1.shapeMode = shapeMode;
        pstyle1.blendMode = blendMode;
        pstyle1.colorMode = colorMode;
        pstyle1.colorModeX = colorModeX;
        pstyle1.colorModeY = colorModeY;
        pstyle1.colorModeZ = colorModeZ;
        pstyle1.colorModeA = colorModeA;
        pstyle1.tint = tint;
        pstyle1.tintColor = tintColor;
        pstyle1.fill = fill;
        pstyle1.fillColor = fillColor;
        pstyle1.stroke = stroke;
        pstyle1.strokeColor = strokeColor;
        pstyle1.strokeWeight = strokeWeight;
        pstyle1.strokeCap = strokeCap;
        pstyle1.strokeJoin = strokeJoin;
        pstyle1.ambientR = ambientR;
        pstyle1.ambientG = ambientG;
        pstyle1.ambientB = ambientB;
        pstyle1.specularR = specularR;
        pstyle1.specularG = specularG;
        pstyle1.specularB = specularB;
        pstyle1.emissiveR = emissiveR;
        pstyle1.emissiveG = emissiveG;
        pstyle1.emissiveB = emissiveB;
        pstyle1.shininess = shininess;
        pstyle1.textFont = textFont;
        pstyle1.textAlign = textAlign;
        pstyle1.textAlignY = textAlignY;
        pstyle1.textMode = textMode;
        pstyle1.textSize = textSize;
        pstyle1.textLeading = textLeading;
        return pstyle1;
    }

    public final float green(int i)
    {
        float f = i >> 8 & 0xff;
        if(!colorModeDefault)
            f = (f / 255F) * colorModeY;
        return f;
    }

    protected void handleTextSize(float f)
    {
        textSize = f;
        textLeading = (textAscent() + textDescent()) * 1.275F;
    }

    public boolean haveRaw()
    {
        boolean flag;
        if(raw != null)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public void hint(int i)
    {
        if(i > 0)
            hints[i] = true;
        else
            hints[-i] = false;
    }

    public final float hue(int i)
    {
        if(i != cacheHsbKey)
        {
            Color.RGBToHSV(i >> 16 & 0xff, i >> 8 & 0xff, i & 0xff, cacheHsbValue);
            cacheHsbKey = i;
        }
        return (cacheHsbValue[0] / 360F) * colorModeX;
    }

    public void image(PImage pimage, float f, float f1)
    {
_L2:
        return;
        if(pimage.width == -1 || pimage.height == -1 || pimage.width == 0 || pimage.height == 0) goto _L2; else goto _L1
_L1:
        if(imageMode == 0 || imageMode == 1)
            imageImpl(pimage, f, f1, f + (float)pimage.width, f1 + (float)pimage.height, 0, 0, pimage.width, pimage.height);
        else
        if(imageMode == 3)
        {
            f -= pimage.width / 2;
            f1 -= pimage.height / 2;
            imageImpl(pimage, f, f1, f + (float)pimage.width, f1 + (float)pimage.height, 0, 0, pimage.width, pimage.height);
        }
        if(true) goto _L2; else goto _L3
_L3:
    }

    public void image(PImage pimage, float f, float f1, float f2, float f3)
    {
        image(pimage, f, f1, f2, f3, 0, 0, pimage.width, pimage.height);
    }

    public void image(PImage pimage, float f, float f1, float f2, float f3, int i, int j, 
            int k, int l)
    {
        if(pimage.width != -1 && pimage.height != -1) goto _L2; else goto _L1
_L1:
        return;
_L2:
        if(imageMode == 0)
        {
            if(f2 < 0.0F)
            {
                f += f2;
                f2 = -f2;
            }
            if(f3 < 0.0F)
            {
                f1 += f3;
                f3 = -f3;
            }
            imageImpl(pimage, f, f1, f + f2, f1 + f3, i, j, k, l);
        } else
        if(imageMode == 1)
        {
            float f4;
            if(f2 >= f)
            {
                float f5 = f2;
                f2 = f;
                f = f5;
            }
            if(f3 < f1)
            {
                f4 = f3;
            } else
            {
                f4 = f1;
                f1 = f3;
            }
            imageImpl(pimage, f2, f4, f, f1, i, j, k, l);
        } else
        if(imageMode == 3)
        {
            f4 = f2;
            if(f2 < 0.0F)
                f4 = -f2;
            f2 = f3;
            if(f3 < 0.0F)
                f2 = -f3;
            f -= f4 / 2.0F;
            f1 -= f2 / 2.0F;
            imageImpl(pimage, f, f1, f + f4, f1 + f2, i, j, k, l);
        }
        if(true) goto _L1; else goto _L3
_L3:
    }

    protected void imageImpl(PImage pimage, float f, float f1, float f2, float f3, int i, int j, 
            int k, int l)
    {
        boolean flag = stroke;
        boolean flag1 = fill;
        int i1 = textureMode;
        stroke = false;
        fill = true;
        textureMode = 2;
        float f4 = fillR;
        float f5 = fillG;
        float f6 = fillB;
        float f7 = fillA;
        if(tint)
        {
            fillR = tintR;
            fillG = tintG;
            fillB = tintB;
            fillA = tintA;
        } else
        {
            fillR = 1.0F;
            fillG = 1.0F;
            fillB = 1.0F;
            fillA = 1.0F;
        }
        beginShape(17);
        texture(pimage);
        vertex(f, f1, i, j);
        vertex(f, f3, i, l);
        vertex(f2, f3, k, l);
        vertex(f2, f1, k, j);
        endShape();
        stroke = flag;
        fill = flag1;
        textureMode = i1;
        fillR = f4;
        fillG = f5;
        fillB = f6;
        fillA = f7;
    }

    public void imageMode(int i)
    {
        if(i == 0 || i == 1 || i == 3)
        {
            imageMode = i;
            return;
        } else
        {
            throw new RuntimeException("imageMode() only works with CORNER, CORNERS, or CENTER");
        }
    }

    public boolean is2D()
    {
        return true;
    }

    public boolean is3D()
    {
        return false;
    }

    public boolean isGL()
    {
        return false;
    }

    public int lerpColor(int i, int j, float f)
    {
        return lerpColor(i, j, f, colorMode);
    }

    public void lightFalloff(float f, float f1, float f2)
    {
        showMethodWarning("lightFalloff");
    }

    public void lightSpecular(float f, float f1, float f2)
    {
        showMethodWarning("lightSpecular");
    }

    public void lights()
    {
        showMethodWarning("lights");
    }

    public void line(float f, float f1, float f2, float f3)
    {
        beginShape(5);
        vertex(f, f1);
        vertex(f2, f3);
        endShape();
    }

    public void line(float f, float f1, float f2, float f3, float f4, float f5)
    {
        beginShape(5);
        vertex(f, f1, f2);
        vertex(f3, f4, f5);
        endShape();
    }

    public PShader loadShader(String s)
    {
        showMissingWarning("loadShader");
        return null;
    }

    public PShader loadShader(String s, String s1)
    {
        showMissingWarning("loadShader");
        return null;
    }

    public PShape loadShape(String s)
    {
        showMissingWarning("loadShape");
        return null;
    }

    public float modelX(float f, float f1, float f2)
    {
        showMissingWarning("modelX");
        return 0.0F;
    }

    public float modelY(float f, float f1, float f2)
    {
        showMissingWarning("modelY");
        return 0.0F;
    }

    public float modelZ(float f, float f1, float f2)
    {
        showMissingWarning("modelZ");
        return 0.0F;
    }

    public void noClip()
    {
        showMissingWarning("noClip");
    }

    public void noFill()
    {
        fill = false;
    }

    public void noLights()
    {
        showMethodWarning("noLights");
    }

    public void noSmooth()
    {
        smooth(0);
    }

    public void noStroke()
    {
        stroke = false;
    }

    public void noTexture()
    {
        textureImage = null;
    }

    public void noTint()
    {
        tint = false;
    }

    public void normal(float f, float f1, float f2)
    {
        normalX = f;
        normalY = f1;
        normalZ = f2;
        if(shape == 0) goto _L2; else goto _L1
_L1:
        if(normalMode != 0) goto _L4; else goto _L3
_L3:
        normalMode = 1;
_L2:
        return;
_L4:
        if(normalMode == 1)
            normalMode = 2;
        if(true) goto _L2; else goto _L5
_L5:
    }

    public void ortho()
    {
        showMissingWarning("ortho");
    }

    public void ortho(float f, float f1, float f2, float f3)
    {
        showMissingWarning("ortho");
    }

    public void ortho(float f, float f1, float f2, float f3, float f4, float f5)
    {
        showMissingWarning("ortho");
    }

    public void perspective()
    {
        showMissingWarning("perspective");
    }

    public void perspective(float f, float f1, float f2, float f3)
    {
        showMissingWarning("perspective");
    }

    public void point(float f, float f1)
    {
        beginShape(3);
        vertex(f, f1);
        endShape();
    }

    public void point(float f, float f1, float f2)
    {
        beginShape(3);
        vertex(f, f1, f2);
        endShape();
    }

    public void pointLight(float f, float f1, float f2, float f3, float f4, float f5)
    {
        showMethodWarning("pointLight");
    }

    public void popMatrix()
    {
        showMethodWarning("popMatrix");
    }

    public void popStyle()
    {
        if(styleStackDepth == 0)
        {
            throw new RuntimeException("Too many popStyle() without enough pushStyle()");
        } else
        {
            styleStackDepth = styleStackDepth - 1;
            style(styleStack[styleStackDepth]);
            return;
        }
    }

    public void printCamera()
    {
        showMethodWarning("printCamera");
    }

    public void printMatrix()
    {
        showMethodWarning("printMatrix");
    }

    public void printProjection()
    {
        showMethodWarning("printCamera");
    }

    protected void processImageBeforeAsyncSave(PImage pimage)
    {
    }

    public void pushMatrix()
    {
        showMethodWarning("pushMatrix");
    }

    public void pushStyle()
    {
        if(styleStackDepth == styleStack.length)
            styleStack = (PStyle[])PApplet.expand(styleStack);
        if(styleStack[styleStackDepth] == null)
            styleStack[styleStackDepth] = new PStyle();
        PStyle apstyle[] = styleStack;
        int i = styleStackDepth;
        styleStackDepth = i + 1;
        getStyle(apstyle[i]);
    }

    public void quad(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7)
    {
        beginShape(17);
        vertex(f, f1);
        vertex(f2, f3);
        vertex(f4, f5);
        vertex(f6, f7);
        endShape();
    }

    public void quadraticVertex(float f, float f1, float f2, float f3)
    {
        bezierVertexCheck();
        float af[] = vertices[vertexCount - 1];
        float f4 = af[0];
        float f5 = af[1];
        bezierVertex(f4 + ((f - f4) * 2.0F) / 3F, ((f1 - f5) * 2.0F) / 3F + f5, f2 + ((f - f2) * 2.0F) / 3F, f3 + ((f1 - f3) * 2.0F) / 3F, f2, f3);
    }

    public void quadraticVertex(float f, float f1, float f2, float f3, float f4, float f5)
    {
        bezierVertexCheck();
        float af[] = vertices[vertexCount - 1];
        float f6 = af[0];
        float f7 = af[1];
        float f8 = af[2];
        bezierVertex(f6 + ((f - f6) * 2.0F) / 3F, f7 + ((f1 - f7) * 2.0F) / 3F, ((f2 - f8) * 2.0F) / 3F + f8, f3 + ((f - f3) * 2.0F) / 3F, f4 + ((f1 - f4) * 2.0F) / 3F, f5 + ((f2 - f5) * 2.0F) / 3F, f3, f4, f5);
    }

    protected void reapplySettings()
    {
        if(settingsInited)
        {
            colorMode(colorMode, colorModeX, colorModeY, colorModeZ);
            if(fill)
                fill(fillColor);
            else
                noFill();
            if(stroke)
            {
                stroke(strokeColor);
                strokeWeight(strokeWeight);
                strokeCap(strokeCap);
                strokeJoin(strokeJoin);
            } else
            {
                noStroke();
            }
            if(tint)
                tint(tintColor);
            else
                noTint();
            if(textFont != null)
            {
                float f = textLeading;
                textFont(textFont, textSize);
                textLeading(f);
            }
            textMode(textMode);
            textAlign(textAlign, textAlignY);
            background(backgroundColor);
            blendMode(blendMode);
        }
    }

    public void rect(float f, float f1, float f2, float f3)
    {
        rectMode;
        JVM INSTR tableswitch 0 3: default 36
    //                   0 98
    //                   1 81
    //                   2 119
    //                   3 151;
           goto _L1 _L2 _L3 _L4 _L5
_L5:
        break MISSING_BLOCK_LABEL_151;
_L1:
        float f4 = f3;
        float f7 = f1;
        f3 = f2;
        f2 = f;
        f1 = f4;
        f = f7;
_L6:
        float f5;
        float f8;
        if(f2 <= f3)
        {
            float f6 = f2;
            f2 = f3;
            f3 = f6;
        }
        if(f > f1)
        {
            f5 = f;
        } else
        {
            f5 = f1;
            f1 = f;
        }
        rectImpl(f3, f1, f2, f5);
        return;
_L3:
        f5 = f3;
        f3 = f2;
        f2 = f;
        f = f1;
        f1 = f5;
          goto _L6
_L2:
        f5 = f3 + f1;
        f3 = f2 + f;
        f2 = f;
        f = f1;
        f1 = f5;
          goto _L6
_L4:
        f5 = f + f2;
        f8 = f1 + f3;
        f1 -= f3;
        f2 = f - f2;
        f = f1;
        f1 = f8;
        f3 = f5;
          goto _L6
        f5 = f2 / 2.0F;
        f8 = f3 / 2.0F;
        f2 = f1 + f8;
        f3 = f + f5;
        f1 -= f8;
        f5 = f - f5;
        f = f1;
        f1 = f2;
        f2 = f5;
          goto _L6
    }

    public void rect(float f, float f1, float f2, float f3, float f4)
    {
        rect(f, f1, f2, f3, f4, f4, f4, f4);
    }

    public void rect(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7)
    {
        rectMode;
        JVM INSTR tableswitch 0 3: default 36
    //                   0 164
    //                   1 143
    //                   2 189
    //                   3 223;
           goto _L1 _L2 _L3 _L4 _L5
_L5:
        break MISSING_BLOCK_LABEL_223;
_L1:
        float f8 = f3;
        float f11 = f2;
        f2 = f1;
        f3 = f;
        f1 = f11;
        f = f8;
_L6:
        float f9;
        float f12;
        float f13;
        if(f3 <= f1)
        {
            float f10 = f3;
            f3 = f1;
            f1 = f10;
        }
        if(f2 > f)
        {
            f9 = f2;
            f2 = f;
        } else
        {
            f9 = f;
        }
        f = PApplet.min((f3 - f1) / 2.0F, (f9 - f2) / 2.0F);
        if(f4 > f)
            f4 = f;
        if(f5 > f)
            f5 = f;
        if(f6 > f)
            f6 = f;
        if(f7 <= f)
            f = f7;
        rectImpl(f1, f2, f3, f9, f4, f5, f6, f);
        return;
_L3:
        f9 = f2;
        f2 = f1;
        f12 = f;
        f = f3;
        f1 = f9;
        f3 = f12;
          goto _L6
_L2:
        f12 = f3 + f1;
        f9 = f2 + f;
        f2 = f1;
        f3 = f;
        f = f12;
        f1 = f9;
          goto _L6
_L4:
        f12 = f + f2;
        f13 = f1 + f3;
        f9 = f1 - f3;
        f3 = f - f2;
        f = f13;
        f1 = f12;
        f2 = f9;
          goto _L6
        f12 = f2 / 2.0F;
        f2 = f3 / 2.0F;
        f9 = f1 + f2;
        f3 = f + f12;
        f2 = f1 - f2;
        f12 = f - f12;
        f = f9;
        f1 = f3;
        f3 = f12;
          goto _L6
    }

    protected void rectImpl(float f, float f1, float f2, float f3)
    {
        quad(f, f1, f2, f1, f2, f3, f, f3);
    }

    protected void rectImpl(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7)
    {
        beginShape();
        if(f5 != 0.0F)
        {
            vertex(f2 - f5, f1);
            quadraticVertex(f2, f1, f2, f1 + f5);
        } else
        {
            vertex(f2, f1);
        }
        if(f6 != 0.0F)
        {
            vertex(f2, f3 - f6);
            quadraticVertex(f2, f3, f2 - f6, f3);
        } else
        {
            vertex(f2, f3);
        }
        if(f7 != 0.0F)
        {
            vertex(f + f7, f3);
            quadraticVertex(f, f3, f, f3 - f7);
        } else
        {
            vertex(f, f3);
        }
        if(f4 != 0.0F)
        {
            vertex(f, f1 + f4);
            quadraticVertex(f, f1, f + f4, f1);
        } else
        {
            vertex(f, f1);
        }
        endShape(2);
    }

    public void rectMode(int i)
    {
        rectMode = i;
    }

    public final float red(int i)
    {
        float f = i >> 16 & 0xff;
        if(!colorModeDefault)
            f = (f / 255F) * colorModeX;
        return f;
    }

    public void removeCache(PImage pimage)
    {
        cacheMap.remove(pimage);
    }

    public void requestDraw()
    {
    }

    public void requestFocus()
    {
    }

    public void resetMatrix()
    {
        showMethodWarning("resetMatrix");
    }

    public void resetShader()
    {
        showMissingWarning("resetShader");
    }

    public void resetShader(int i)
    {
        showMissingWarning("resetShader");
    }

    public void rotate(float f)
    {
        showMissingWarning("rotate");
    }

    public void rotate(float f, float f1, float f2, float f3)
    {
        showMissingWarning("rotate");
    }

    public void rotateX(float f)
    {
        showMethodWarning("rotateX");
    }

    public void rotateY(float f)
    {
        showMethodWarning("rotateY");
    }

    public void rotateZ(float f)
    {
        showMethodWarning("rotateZ");
    }

    public final float saturation(int i)
    {
        if(i != cacheHsbKey)
        {
            Color.RGBToHSV(i >> 16 & 0xff, i >> 8 & 0xff, i & 0xff, cacheHsbValue);
            cacheHsbKey = i;
        }
        return cacheHsbValue[1] * colorModeY;
    }

    public boolean save(String s)
    {
        boolean flag = false;
        if(!hints[11]) goto _L2; else goto _L1
_L1:
        flag = super.save(s);
_L4:
        return flag;
_L2:
        if(asyncImageSaver == null)
            asyncImageSaver = new AsyncImageSaver();
        if(!loaded)
            loadPixels();
        PImage pimage = asyncImageSaver.getAvailableTarget(pixelWidth, pixelHeight, format);
        if(pimage != null)
        {
            int i = PApplet.min(pixels.length, pimage.pixels.length);
            System.arraycopy(pixels, 0, pimage.pixels, 0, i);
            asyncImageSaver.saveTargetAsync(this, pimage, s);
            flag = true;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void scale(float f)
    {
        showMissingWarning("scale");
    }

    public void scale(float f, float f1)
    {
        showMissingWarning("scale");
    }

    public void scale(float f, float f1, float f2)
    {
        showMissingWarning("scale");
    }

    public float screenX(float f, float f1)
    {
        showMissingWarning("screenX");
        return 0.0F;
    }

    public float screenX(float f, float f1, float f2)
    {
        showMissingWarning("screenX");
        return 0.0F;
    }

    public float screenY(float f, float f1)
    {
        showMissingWarning("screenY");
        return 0.0F;
    }

    public float screenY(float f, float f1, float f2)
    {
        showMissingWarning("screenY");
        return 0.0F;
    }

    public float screenZ(float f, float f1, float f2)
    {
        showMissingWarning("screenZ");
        return 0.0F;
    }

    public void setCache(PImage pimage, Object obj)
    {
        cacheMap.put(pimage, obj);
    }

    public void setFrameRate(float f)
    {
    }

    public void setMatrix(PMatrix2D pmatrix2d)
    {
        showMissingWarning("setMatrix");
    }

    public void setMatrix(PMatrix3D pmatrix3d)
    {
        showMissingWarning("setMatrix");
    }

    public void setMatrix(PMatrix pmatrix)
    {
        if(!(pmatrix instanceof PMatrix2D)) goto _L2; else goto _L1
_L1:
        setMatrix((PMatrix2D)pmatrix);
_L4:
        return;
_L2:
        if(pmatrix instanceof PMatrix3D)
            setMatrix((PMatrix3D)pmatrix);
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void setParent(PApplet papplet)
    {
        parent = papplet;
    }

    public void setPath(String s)
    {
        path = s;
    }

    public void setPrimary(boolean flag)
    {
        primaryGraphics = flag;
        if(primaryGraphics)
            format = 1;
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

    public void shader(PShader pshader)
    {
        showMissingWarning("shader");
    }

    public void shader(PShader pshader, int i)
    {
        showMissingWarning("shader");
    }

    public void shape(PShape pshape)
    {
        if(pshape.isVisible())
        {
            if(shapeMode == 3)
            {
                pushMatrix();
                translate(-pshape.getWidth() / 2.0F, -pshape.getHeight() / 2.0F);
            }
            pshape.draw(this);
            if(shapeMode == 3)
                popMatrix();
        }
    }

    public void shape(PShape pshape, float f, float f1)
    {
        if(!pshape.isVisible()) goto _L2; else goto _L1
_L1:
        pushMatrix();
        if(shapeMode != 3) goto _L4; else goto _L3
_L3:
        translate(f - pshape.getWidth() / 2.0F, f1 - pshape.getHeight() / 2.0F);
_L6:
        pshape.draw(this);
        popMatrix();
_L2:
        return;
_L4:
        if(shapeMode == 0 || shapeMode == 1)
            translate(f, f1);
        if(true) goto _L6; else goto _L5
_L5:
    }

    protected void shape(PShape pshape, float f, float f1, float f2)
    {
        showMissingWarning("shape");
    }

    public void shape(PShape pshape, float f, float f1, float f2, float f3)
    {
        if(!pshape.isVisible()) goto _L2; else goto _L1
_L1:
        pushMatrix();
        if(shapeMode != 3) goto _L4; else goto _L3
_L3:
        translate(f - f2 / 2.0F, f1 - f3 / 2.0F);
        scale(f2 / pshape.getWidth(), f3 / pshape.getHeight());
_L6:
        pshape.draw(this);
        popMatrix();
_L2:
        return;
_L4:
        if(shapeMode == 0)
        {
            translate(f, f1);
            scale(f2 / pshape.getWidth(), f3 / pshape.getHeight());
        } else
        if(shapeMode == 1)
        {
            translate(f, f1);
            scale((f2 - f) / pshape.getWidth(), (f3 - f1) / pshape.getHeight());
        }
        if(true) goto _L6; else goto _L5
_L5:
    }

    protected void shape(PShape pshape, float f, float f1, float f2, float f3, float f4, float f5)
    {
        showMissingWarning("shape");
    }

    public void shapeMode(int i)
    {
        shapeMode = i;
    }

    public void shearX(float f)
    {
        showMissingWarning("shearX");
    }

    public void shearY(float f)
    {
        showMissingWarning("shearY");
    }

    public void shininess(float f)
    {
        shininess = f;
    }

    public void smooth()
    {
        smooth(1);
    }

    public void smooth(int i)
    {
        if(!primaryGraphics) goto _L2; else goto _L1
_L1:
        parent.smooth(i);
_L4:
        return;
_L2:
        if(settingsInited)
        {
            if(smooth != i)
                smoothWarning("smooth");
        } else
        {
            smooth = i;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void specular(float f)
    {
        colorCalc(f);
        specularFromCalc();
    }

    public void specular(float f, float f1, float f2)
    {
        colorCalc(f, f1, f2);
        specularFromCalc();
    }

    public void specular(int i)
    {
        colorCalc(i);
        specularFromCalc();
    }

    protected void specularFromCalc()
    {
        specularColor = calcColor;
        specularR = calcR;
        specularG = calcG;
        specularB = calcB;
    }

    public void sphere(float f)
    {
        boolean flag = false;
        if(sphereDetailU < 3 || sphereDetailV < 2)
            sphereDetail(30);
        edge(false);
        beginShape(10);
        for(int i = 0; i < sphereDetailU; i++)
        {
            normal(0.0F, -1F, 0.0F);
            vertex(0.0F, -f, 0.0F);
            normal(sphereX[i], sphereY[i], sphereZ[i]);
            vertex(sphereX[i] * f, sphereY[i] * f, sphereZ[i] * f);
        }

        normal(0.0F, -f, 0.0F);
        vertex(0.0F, -f, 0.0F);
        normal(sphereX[0], sphereY[0], sphereZ[0]);
        vertex(sphereX[0] * f, sphereY[0] * f, sphereZ[0] * f);
        endShape();
        int k = 2;
        int j;
        int i1;
        for(j = 0; k < sphereDetailV; j = i1)
        {
            i1 = sphereDetailU + j;
            beginShape(10);
            int k1 = 0;
            int l1 = i1;
            for(int i2 = j; k1 < sphereDetailU; i2++)
            {
                normal(sphereX[i2], sphereY[i2], sphereZ[i2]);
                vertex(f * sphereX[i2], f * sphereY[i2], sphereZ[i2] * f);
                normal(sphereX[l1], sphereY[l1], sphereZ[l1]);
                vertex(f * sphereX[l1], f * sphereY[l1], sphereZ[l1] * f);
                k1++;
                l1++;
            }

            normal(sphereX[j], sphereY[j], sphereZ[j]);
            vertex(sphereX[j] * f, sphereY[j] * f, sphereZ[j] * f);
            normal(sphereX[i1], sphereY[i1], sphereZ[i1]);
            vertex(sphereX[i1] * f, sphereY[i1] * f, sphereZ[i1] * f);
            endShape();
            k++;
        }

        beginShape(10);
        for(int j1 = ((flag) ? 1 : 0); j1 < sphereDetailU; j1++)
        {
            int l = j + j1;
            normal(sphereX[l], sphereY[l], sphereZ[l]);
            vertex(sphereX[l] * f, sphereY[l] * f, sphereZ[l] * f);
            normal(0.0F, 1.0F, 0.0F);
            vertex(0.0F, f, 0.0F);
        }

        normal(sphereX[j], sphereY[j], sphereZ[j]);
        vertex(sphereX[j] * f, sphereY[j] * f, sphereZ[j] * f);
        normal(0.0F, 1.0F, 0.0F);
        vertex(0.0F, f, 0.0F);
        endShape();
        edge(true);
    }

    public void sphereDetail(int i)
    {
        sphereDetail(i, i);
    }

    public void sphereDetail(int i, int j)
    {
        int k = i;
        if(i < 3)
            k = 3;
        int l = j;
        if(j < 2)
            l = 2;
        if(k != sphereDetailU || l != sphereDetailV)
        {
            float f = 720F / (float)k;
            float af[] = new float[k];
            float af1[] = new float[k];
            for(i = 0; i < k; i++)
            {
                af[i] = cosLUT[(int)((float)i * f) % 720];
                af1[i] = sinLUT[(int)((float)i * f) % 720];
            }

            i = (l - 1) * k + 2;
            sphereX = new float[i];
            sphereY = new float[i];
            sphereZ = new float[i];
            float f1 = 360F / (float)l;
            j = 1;
            f = f1;
            i = 0;
            while(j < l) 
            {
                float f2 = sinLUT[(int)f % 720];
                float f3 = cosLUT[(int)f % 720];
                for(int i1 = 0; i1 < k;)
                {
                    sphereX[i] = af[i1] * f2;
                    sphereY[i] = f3;
                    sphereZ[i] = af1[i1] * f2;
                    i1++;
                    i++;
                }

                j++;
                f += f1;
            }
            sphereDetailU = k;
            sphereDetailV = l;
        }
    }

    protected void splineForward(int i, PMatrix3D pmatrix3d)
    {
        float f = 1.0F / (float)i;
        float f1 = f * f;
        float f2 = f1 * f;
        pmatrix3d.set(0.0F, 0.0F, 0.0F, 1.0F, f2, f1, f, 0.0F, 6F * f2, 2.0F * f1, 0.0F, 0.0F, 6F * f2, 0.0F, 0.0F, 0.0F);
    }

    public void spotLight(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8, float f9, float f10)
    {
        showMethodWarning("spotLight");
    }

    public void stroke(float f)
    {
        colorCalc(f);
        strokeFromCalc();
    }

    public void stroke(float f, float f1)
    {
        colorCalc(f, f1);
        strokeFromCalc();
    }

    public void stroke(float f, float f1, float f2)
    {
        colorCalc(f, f1, f2);
        strokeFromCalc();
    }

    public void stroke(float f, float f1, float f2, float f3)
    {
        colorCalc(f, f1, f2, f3);
        strokeFromCalc();
    }

    public void stroke(int i)
    {
        colorCalc(i);
        strokeFromCalc();
    }

    public void stroke(int i, float f)
    {
        colorCalc(i, f);
        strokeFromCalc();
    }

    public void strokeCap(int i)
    {
        strokeCap = i;
    }

    protected void strokeFromCalc()
    {
        stroke = true;
        strokeR = calcR;
        strokeG = calcG;
        strokeB = calcB;
        strokeA = calcA;
        strokeRi = calcRi;
        strokeGi = calcGi;
        strokeBi = calcBi;
        strokeAi = calcAi;
        strokeColor = calcColor;
        strokeAlpha = calcAlpha;
    }

    public void strokeJoin(int i)
    {
        strokeJoin = i;
    }

    public void strokeWeight(float f)
    {
        strokeWeight = f;
    }

    public void style(PStyle pstyle)
    {
        imageMode(pstyle.imageMode);
        rectMode(pstyle.rectMode);
        ellipseMode(pstyle.ellipseMode);
        shapeMode(pstyle.shapeMode);
        blendMode(pstyle.blendMode);
        if(pstyle.tint)
            tint(pstyle.tintColor);
        else
            noTint();
        if(pstyle.fill)
            fill(pstyle.fillColor);
        else
            noFill();
        if(pstyle.stroke)
            stroke(pstyle.strokeColor);
        else
            noStroke();
        strokeWeight(pstyle.strokeWeight);
        strokeCap(pstyle.strokeCap);
        strokeJoin(pstyle.strokeJoin);
        colorMode(1, 1.0F);
        ambient(pstyle.ambientR, pstyle.ambientG, pstyle.ambientB);
        emissive(pstyle.emissiveR, pstyle.emissiveG, pstyle.emissiveB);
        specular(pstyle.specularR, pstyle.specularG, pstyle.specularB);
        shininess(pstyle.shininess);
        colorMode(pstyle.colorMode, pstyle.colorModeX, pstyle.colorModeY, pstyle.colorModeZ, pstyle.colorModeA);
        if(pstyle.textFont != null)
        {
            textFont(pstyle.textFont, pstyle.textSize);
            textLeading(pstyle.textLeading);
        }
        textAlign(pstyle.textAlign, pstyle.textAlignY);
        textMode(pstyle.textMode);
    }

    public void text(char c, float f, float f1)
    {
        if(textFont == null)
            defaultFontOrDeath("text");
        if(textAlignY != 3) goto _L2; else goto _L1
_L1:
        f1 += textAscent() / 2.0F;
_L4:
        textBuffer[0] = c;
        textLineAlignImpl(textBuffer, 0, 1, f, f1);
        return;
_L2:
        if(textAlignY == 101)
            f1 += textAscent();
        else
        if(textAlignY == 102)
            f1 -= textDescent();
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void text(char c, float f, float f1, float f2)
    {
        if(f2 != 0.0F)
            translate(0.0F, 0.0F, f2);
        text(c, f, f1);
        if(f2 != 0.0F)
            translate(0.0F, 0.0F, -f2);
    }

    public void text(float f, float f1, float f2)
    {
        text(PApplet.nfs(f, 0, 3), f1, f2);
    }

    public void text(float f, float f1, float f2, float f3)
    {
        text(PApplet.nfs(f, 0, 3), f1, f2, f3);
    }

    public void text(int i, float f, float f1)
    {
        text(String.valueOf(i), f, f1);
    }

    public void text(int i, float f, float f1, float f2)
    {
        text(String.valueOf(i), f, f1, f2);
    }

    public void text(String s, float f, float f1)
    {
        boolean flag;
        int j;
        float f2;
        flag = false;
        if(textFont == null)
            defaultFontOrDeath("text");
        j = s.length();
        if(j > textBuffer.length)
            textBuffer = new char[j + 10];
        s.getChars(0, j, textBuffer, 0);
        f2 = 0.0F;
        for(int k = 0; k < j;)
        {
            float f3 = f2;
            if(textBuffer[k] == '\n')
                f3 = f2 + textLeading;
            k++;
            f2 = f3;
        }

        if(textAlignY != 3) goto _L2; else goto _L1
_L1:
        float f4 = f1 + (textAscent() - f2) / 2.0F;
_L4:
        int l;
        int i1;
        i1 = 0;
        f1 = f4;
        for(l = ((flag) ? 1 : 0); l < j;)
        {
            int i = i1;
            f4 = f1;
            if(textBuffer[l] == '\n')
            {
                textLineAlignImpl(textBuffer, i1, l, f, f1);
                i = l + 1;
                f4 = f1 + textLeading;
            }
            l++;
            i1 = i;
            f1 = f4;
        }

        break; /* Loop/switch isn't completed */
_L2:
        if(textAlignY == 101)
        {
            f4 = f1 + textAscent();
        } else
        {
            f4 = f1;
            if(textAlignY == 102)
                f4 = f1 - (f2 + textDescent());
        }
        if(true) goto _L4; else goto _L3
_L3:
        if(i1 < j)
            textLineAlignImpl(textBuffer, i1, l, f, f1);
        return;
    }

    public void text(String s, float f, float f1, float f2)
    {
        if(f2 != 0.0F)
            translate(0.0F, 0.0F, f2);
        text(s, f, f1);
        if(f2 != 0.0F)
            translate(0.0F, 0.0F, -f2);
    }

    public void text(String s, float f, float f1, float f2, float f3)
    {
        if(textFont == null)
            defaultFontOrDeath("text");
        rectMode;
        JVM INSTR tableswitch 0 3: default 48
    //                   0 373
    //                   1 48
    //                   2 396
    //                   3 431;
           goto _L1 _L2 _L1 _L3 _L4
_L1:
        float f4 = f3;
        float f7 = f1;
        f3 = f;
        f1 = f4;
        f = f7;
_L8:
        float f5;
        float f8;
        int i;
        int j;
        int k;
        int l;
        if(f2 >= f3)
        {
            float f6 = f3;
            f3 = f2;
            f2 = f6;
        }
        if(f1 < f)
        {
            f5 = f;
        } else
        {
            f5 = f1;
            f1 = f;
        }
        f = f3 - f2;
        f8 = textWidth(' ');
        if(textBreakStart == null)
        {
            textBreakStart = new int[20];
            textBreakStop = new int[20];
        }
        textBreakCount = 0;
        i = s.length();
        if(i + 1 > textBuffer.length)
            textBuffer = new char[i + 1];
        s.getChars(0, i, textBuffer, 0);
        textBuffer[i] = (char)10;
        j = 0;
        k = 0;
_L9:
        if(k >= i + 1) goto _L6; else goto _L5
_L5:
        l = j;
        if(textBuffer[k] != '\n')
            break MISSING_BLOCK_LABEL_484;
        if(textSentence(textBuffer, j, k, f, f8)) goto _L7; else goto _L6
_L6:
        if(textAlign == 3)
            f = f2 + f / 2.0F;
        else
        if(textAlign == 22)
            f = f3;
        else
            f = f2;
        f2 = f5 - f1;
        k = PApplet.floor((f2 - (textAscent() + textDescent())) / textLeading);
        l = Math.min(textBreakCount, k + 1);
        if(textAlignY == 3)
        {
            f3 = textAscent();
            f5 = textLeading;
            f8 = l - 1;
            f1 = textAscent() + f1 + (f2 - (f3 + f5 * f8)) / 2.0F;
            for(k = 0; k < l; k++)
            {
                textLineAlignImpl(textBuffer, textBreakStart[k], textBreakStop[k], f, f1);
                f1 += textLeading;
            }

        } else
        if(textAlignY == 102)
        {
            f1 = f5 - textDescent() - textLeading * (float)(l - 1);
            for(k = 0; k < l; k++)
            {
                textLineAlignImpl(textBuffer, textBreakStart[k], textBreakStop[k], f, f1);
                f1 += textLeading;
            }

        } else
        {
            f1 += textAscent();
            for(k = 0; k < l; k++)
            {
                textLineAlignImpl(textBuffer, textBreakStart[k], textBreakStop[k], f, f1);
                f1 += textLeading;
            }

        }
        break MISSING_BLOCK_LABEL_638;
_L2:
        f2 += f;
        f5 = f3 + f1;
        f3 = f;
        f = f1;
        f1 = f5;
          goto _L8
_L3:
        f5 = f + f2;
        f8 = f1 + f3;
        f1 -= f3;
        f3 = f - f2;
        f = f1;
        f1 = f8;
        f2 = f5;
          goto _L8
_L4:
        f5 = f2 / 2.0F;
        f8 = f3 / 2.0F;
        f2 = f + f5;
        f3 = f1 + f8;
        f1 -= f8;
        f5 = f - f5;
        f = f1;
        f1 = f3;
        f3 = f5;
          goto _L8
_L7:
        l = k + 1;
        k++;
        j = l;
          goto _L9
    }

    public void textAlign(int i)
    {
        textAlign(i, 0);
    }

    public void textAlign(int i, int j)
    {
        textAlign = i;
        textAlignY = j;
    }

    public float textAscent()
    {
        if(textFont == null)
            defaultFontOrDeath("textAscent");
        return textFont.ascent() * textSize;
    }

    protected void textCharImpl(char c, float f, float f1)
    {
        PFont.Glyph glyph = textFont.getGlyph(c);
        if(glyph != null && textMode == 4)
        {
            float f2 = (float)glyph.height / (float)textFont.size;
            float f3 = (float)glyph.width / (float)textFont.size;
            float f4 = (float)glyph.leftExtent / (float)textFont.size;
            float f5 = (float)glyph.topExtent / (float)textFont.size;
            f = f4 * textSize + f;
            f5 = f1 - f5 * textSize;
            f1 = textSize;
            f4 = textSize;
            textCharModelImpl(glyph.image, f, f5, f3 * f1 + f, f5 + f2 * f4, glyph.width, glyph.height);
        }
    }

    protected void textCharModelImpl(PImage pimage, float f, float f1, float f2, float f3, int i, int j)
    {
        boolean flag = tint;
        int k = tintColor;
        tint(fillColor);
        imageImpl(pimage, f, f1, f2, f3, 0, 0, i, j);
        if(flag)
            tint(k);
        else
            noTint();
    }

    public float textDescent()
    {
        if(textFont == null)
            defaultFontOrDeath("textDescent");
        return textFont.descent() * textSize;
    }

    public void textFont(PFont pfont)
    {
        if(pfont == null)
        {
            throw new RuntimeException("A null PFont was passed to textFont()");
        } else
        {
            textFontImpl(pfont, pfont.getDefaultSize());
            return;
        }
    }

    public void textFont(PFont pfont, float f)
    {
        if(pfont == null)
            throw new RuntimeException("A null PFont was passed to textFont()");
        float f1 = f;
        if(f <= 0.0F)
        {
            System.err.println((new StringBuilder()).append("textFont: ignoring size ").append(f).append(" px:the text size must be larger than zero").toString());
            f1 = textSize;
        }
        textFontImpl(pfont, f1);
    }

    protected void textFontImpl(PFont pfont, float f)
    {
        textFont = pfont;
        handleTextSize(f);
    }

    public void textLeading(float f)
    {
        textLeading = f;
    }

    protected void textLineAlignImpl(char ac[], int i, int j, float f, float f1)
    {
        if(textAlign != 3) goto _L2; else goto _L1
_L1:
        f -= textWidthImpl(ac, i, j) / 2.0F;
_L4:
        textLineImpl(ac, i, j, f, f1);
        return;
_L2:
        if(textAlign == 22)
            f -= textWidthImpl(ac, i, j);
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected void textLineImpl(char ac[], int i, int j, float f, float f1)
    {
        for(; i < j; i++)
        {
            textCharImpl(ac[i], f, f1);
            f += textWidth(ac[i]);
        }

    }

    public void textMode(int i)
    {
        if(i != 21 && i != 22) goto _L2; else goto _L1
_L1:
        showWarning("Since Processing beta, textMode() is now textAlign().");
_L4:
        return;
_L2:
        String s;
        if(i == 256)
            showWarning("textMode(SCREEN) has been removed from Processing 2.0.");
        if(textModeCheck(i))
        {
            textMode = i;
            continue; /* Loop/switch isn't completed */
        }
        s = String.valueOf(i);
        switch(i)
        {
        default:
            break;

        case 4: // '\004'
            break; /* Loop/switch isn't completed */

        case 5: // '\005'
            break;
        }
        break MISSING_BLOCK_LABEL_115;
_L5:
        showWarning((new StringBuilder()).append("textMode(").append(s).append(") is not supported by this renderer.").toString());
        if(true) goto _L4; else goto _L3
_L3:
        s = "MODEL";
          goto _L5
        s = "SHAPE";
          goto _L5
    }

    protected boolean textModeCheck(int i)
    {
        return true;
    }

    public void textSize(float f)
    {
        if(f <= 0.0F)
        {
            System.err.println((new StringBuilder()).append("textSize(").append(f).append(") ignored: the text size must be larger than zero").toString());
        } else
        {
            if(textFont == null)
                defaultFontOrDeath("textSize", f);
            textSizeImpl(f);
        }
    }

    protected void textSizeImpl(float f)
    {
        handleTextSize(f);
    }

    public float textWidth(char c)
    {
        textWidthBuffer[0] = c;
        return textWidthImpl(textWidthBuffer, 0, 1);
    }

    public float textWidth(String s)
    {
        int i = 0;
        if(textFont == null)
            defaultFontOrDeath("textWidth");
        int j = s.length();
        if(j > textWidthBuffer.length)
            textWidthBuffer = new char[j + 10];
        s.getChars(0, j, textWidthBuffer, 0);
        float f = 0.0F;
        int k;
        for(k = 0; k < j;)
        {
            int l = i;
            float f1 = f;
            if(textWidthBuffer[k] == '\n')
            {
                f1 = Math.max(f, textWidthImpl(textWidthBuffer, i, k));
                l = k + 1;
            }
            k++;
            i = l;
            f = f1;
        }

        float f2 = f;
        if(i < j)
            f2 = Math.max(f, textWidthImpl(textWidthBuffer, i, k));
        return f2;
    }

    protected float textWidthImpl(char ac[], int i, int j)
    {
        float f = 0.0F;
        for(; i < j; i++)
            f += textFont.width(ac[i]) * textSize;

        return f;
    }

    public void texture(PImage pimage)
    {
        textureImage = pimage;
    }

    public void textureMode(int i)
    {
        textureMode = i;
    }

    public void textureWrap(int i)
    {
        showMissingWarning("textureWrap");
    }

    public void tint(float f)
    {
        colorCalc(f);
        tintFromCalc();
    }

    public void tint(float f, float f1)
    {
        colorCalc(f, f1);
        tintFromCalc();
    }

    public void tint(float f, float f1, float f2)
    {
        colorCalc(f, f1, f2);
        tintFromCalc();
    }

    public void tint(float f, float f1, float f2, float f3)
    {
        colorCalc(f, f1, f2, f3);
        tintFromCalc();
    }

    public void tint(int i)
    {
        colorCalc(i);
        tintFromCalc();
    }

    public void tint(int i, float f)
    {
        colorCalc(i, f);
        tintFromCalc();
    }

    protected void tintFromCalc()
    {
        tint = true;
        tintR = calcR;
        tintG = calcG;
        tintB = calcB;
        tintA = calcA;
        tintRi = calcRi;
        tintGi = calcGi;
        tintBi = calcBi;
        tintAi = calcAi;
        tintColor = calcColor;
        tintAlpha = calcAlpha;
    }

    public void translate(float f, float f1)
    {
        showMissingWarning("translate");
    }

    public void translate(float f, float f1, float f2)
    {
        showMissingWarning("translate");
    }

    public void triangle(float f, float f1, float f2, float f3, float f4, float f5)
    {
        beginShape(9);
        vertex(f, f1);
        vertex(f2, f3);
        vertex(f4, f5);
        endShape();
    }

    public void vertex(float f, float f1)
    {
        vertexCheck();
        float af[] = vertices[vertexCount];
        curveVertexCount = 0;
        af[0] = f;
        af[1] = f1;
        af[2] = 0.0F;
        boolean flag;
        if(edge)
            f = 1.0F;
        else
            f = 0.0F;
        af[12] = f;
        if(textureImage != null)
            flag = true;
        else
            flag = false;
        if(fill || flag)
            if(!flag)
            {
                af[3] = fillR;
                af[4] = fillG;
                af[5] = fillB;
                af[6] = fillA;
            } else
            if(tint)
            {
                af[3] = tintR;
                af[4] = tintG;
                af[5] = tintB;
                af[6] = tintA;
            } else
            {
                af[3] = 1.0F;
                af[4] = 1.0F;
                af[5] = 1.0F;
                af[6] = 1.0F;
            }
        if(stroke)
        {
            af[13] = strokeR;
            af[14] = strokeG;
            af[15] = strokeB;
            af[16] = strokeA;
            af[17] = strokeWeight;
        }
        af[7] = textureU;
        af[8] = textureV;
        if(autoNormal)
        {
            f = normalX * normalX + normalY * normalY + normalZ * normalZ;
            if(f < 0.0001F)
            {
                af[36] = 0.0F;
            } else
            {
                if(Math.abs(f - 1.0F) > 0.0001F)
                {
                    f = PApplet.sqrt(f);
                    normalX = normalX / f;
                    normalY = normalY / f;
                    normalZ = normalZ / f;
                }
                af[36] = 1.0F;
            }
        } else
        {
            af[36] = 1.0F;
        }
        af[9] = normalX;
        af[10] = normalY;
        af[11] = normalZ;
        vertexCount = vertexCount + 1;
    }

    public void vertex(float f, float f1, float f2)
    {
        float af[];
        vertexCheck();
        af = vertices[vertexCount];
        if(shape != 20 || vertexCount <= 0) goto _L2; else goto _L1
_L1:
        float af1[] = vertices[vertexCount - 1];
        if(Math.abs(af1[0] - f) >= 0.0001F || Math.abs(af1[1] - f1) >= 0.0001F || Math.abs(af1[2] - f2) >= 0.0001F) goto _L2; else goto _L3
_L3:
        return;
_L2:
        curveVertexCount = 0;
        af[0] = f;
        af[1] = f1;
        af[2] = f2;
        boolean flag;
        if(edge)
            f = 1.0F;
        else
            f = 0.0F;
        af[12] = f;
        if(textureImage != null)
            flag = true;
        else
            flag = false;
        if(fill || flag)
            if(!flag)
            {
                af[3] = fillR;
                af[4] = fillG;
                af[5] = fillB;
                af[6] = fillA;
            } else
            if(tint)
            {
                af[3] = tintR;
                af[4] = tintG;
                af[5] = tintB;
                af[6] = tintA;
            } else
            {
                af[3] = 1.0F;
                af[4] = 1.0F;
                af[5] = 1.0F;
                af[6] = 1.0F;
            }
        if(stroke)
        {
            af[13] = strokeR;
            af[14] = strokeG;
            af[15] = strokeB;
            af[16] = strokeA;
            af[17] = strokeWeight;
        }
        af[7] = textureU;
        af[8] = textureV;
        if(autoNormal)
        {
            f = normalX * normalX + normalY * normalY + normalZ * normalZ;
            if(f < 0.0001F)
            {
                af[36] = 0.0F;
            } else
            {
                if(Math.abs(f - 1.0F) > 0.0001F)
                {
                    f = PApplet.sqrt(f);
                    normalX = normalX / f;
                    normalY = normalY / f;
                    normalZ = normalZ / f;
                }
                af[36] = 1.0F;
            }
        } else
        {
            af[36] = 1.0F;
        }
        af[9] = normalX;
        af[10] = normalY;
        af[11] = normalZ;
        vertexCount = vertexCount + 1;
        if(true) goto _L3; else goto _L4
_L4:
    }

    public void vertex(float f, float f1, float f2, float f3)
    {
        vertexTexture(f2, f3);
        vertex(f, f1);
    }

    public void vertex(float f, float f1, float f2, float f3, float f4)
    {
        vertexTexture(f3, f4);
        vertex(f, f1, f2);
    }

    public void vertex(float af[])
    {
        vertexCheck();
        curveVertexCount = 0;
        System.arraycopy(af, 0, vertices[vertexCount], 0, 37);
        vertexCount = vertexCount + 1;
    }

    protected void vertexCheck()
    {
        if(vertexCount == vertices.length)
        {
            int i = vertexCount;
            float af[][] = new float[i << 1][37];
            System.arraycopy(vertices, 0, af, 0, vertexCount);
            vertices = af;
        }
    }

    protected void vertexTexture(float f, float f1)
    {
        if(textureImage == null)
            throw new RuntimeException("You must first call texture() before using u and v coordinates with vertex()");
        float f2 = f;
        float f3 = f1;
        if(textureMode == 2)
        {
            f2 = f / (float)textureImage.width;
            f3 = f1 / (float)textureImage.height;
        }
        textureU = f2;
        textureV = f3;
    }

    public static final int A = 6;
    public static final int AB = 27;
    public static final int AG = 26;
    public static final int AR = 25;
    public static final int B = 5;
    public static final int BEEN_LIT = 35;
    public static final int DA = 6;
    public static final int DB = 5;
    protected static final int DEFAULT_STROKE_CAP = 2;
    protected static final int DEFAULT_STROKE_JOIN = 8;
    protected static final float DEFAULT_STROKE_WEIGHT = 1F;
    static final int DEFAULT_VERTICES = 512;
    public static final int DG = 4;
    public static final int DR = 3;
    public static final int EB = 34;
    public static final int EDGE = 12;
    public static final int EG = 33;
    public static final int ER = 32;
    public static final int G = 4;
    public static final int HAS_NORMAL = 36;
    static final int MATRIX_STACK_DEPTH = 32;
    protected static final int NORMAL_MODE_AUTO = 0;
    protected static final int NORMAL_MODE_SHAPE = 1;
    protected static final int NORMAL_MODE_VERTEX = 2;
    public static final int NX = 9;
    public static final int NY = 10;
    public static final int NZ = 11;
    public static final int R = 3;
    public static final int SA = 16;
    public static final int SB = 15;
    public static final int SG = 14;
    public static final int SHINE = 31;
    protected static final int SINCOS_LENGTH = 720;
    protected static final float SINCOS_PRECISION = 0.5F;
    public static final int SPB = 30;
    public static final int SPG = 29;
    public static final int SPR = 28;
    public static final int SR = 13;
    static final int STYLE_STACK_DEPTH = 64;
    public static final int SW = 17;
    public static final int TX = 18;
    public static final int TY = 19;
    public static final int TZ = 20;
    public static final int U = 7;
    public static final int V = 8;
    public static final int VERTEX_FIELD_COUNT = 37;
    public static final int VW = 24;
    public static final int VX = 21;
    public static final int VY = 22;
    public static final int VZ = 23;
    protected static AsyncImageSaver asyncImageSaver;
    protected static final float cosLUT[];
    static float lerpColorHSB1[];
    static float lerpColorHSB2[];
    static float lerpColorHSB3[];
    protected static final float sinLUT[];
    protected static HashMap warnings;
    public float ambientB;
    public int ambientColor;
    public float ambientG;
    public float ambientR;
    protected boolean autoNormal;
    protected float backgroundA;
    protected int backgroundAi;
    protected boolean backgroundAlpha;
    protected float backgroundB;
    protected int backgroundBi;
    public int backgroundColor;
    protected float backgroundG;
    protected int backgroundGi;
    protected float backgroundR;
    protected int backgroundRi;
    protected PMatrix3D bezierBasisInverse;
    protected PMatrix3D bezierBasisMatrix;
    public int bezierDetail;
    protected PMatrix3D bezierDrawMatrix;
    protected boolean bezierInited;
    protected int blendMode;
    int cacheHsbKey;
    float cacheHsbValue[];
    protected WeakHashMap cacheMap;
    protected float calcA;
    protected int calcAi;
    protected boolean calcAlpha;
    protected float calcB;
    protected int calcBi;
    protected int calcColor;
    protected float calcG;
    protected int calcGi;
    protected float calcR;
    protected int calcRi;
    public int colorMode;
    public float colorModeA;
    boolean colorModeDefault;
    boolean colorModeScale;
    public float colorModeX;
    public float colorModeY;
    public float colorModeZ;
    protected PMatrix3D curveBasisMatrix;
    public int curveDetail;
    protected PMatrix3D curveDrawMatrix;
    protected boolean curveInited;
    public float curveTightness;
    protected PMatrix3D curveToBezierMatrix;
    protected int curveVertexCount;
    protected float curveVertices[][];
    public boolean edge;
    public int ellipseMode;
    public float emissiveB;
    public int emissiveColor;
    public float emissiveG;
    public float emissiveR;
    public boolean fill;
    protected float fillA;
    protected int fillAi;
    protected boolean fillAlpha;
    protected float fillB;
    protected int fillBi;
    public int fillColor;
    protected float fillG;
    protected int fillGi;
    protected float fillR;
    protected int fillRi;
    protected int height1;
    protected boolean hints[];
    public int imageMode;
    protected int normalMode;
    public float normalX;
    public float normalY;
    public float normalZ;
    protected String path;
    public int pixelCount;
    protected boolean primaryGraphics;
    protected PGraphics raw;
    public int rectMode;
    public boolean setAmbient;
    protected boolean settingsInited;
    protected int shape;
    public int shapeMode;
    public float shininess;
    public int smooth;
    public float specularB;
    public int specularColor;
    public float specularG;
    public float specularR;
    public int sphereDetailU;
    public int sphereDetailV;
    protected float sphereX[];
    protected float sphereY[];
    protected float sphereZ[];
    public boolean stroke;
    protected float strokeA;
    protected int strokeAi;
    protected boolean strokeAlpha;
    protected float strokeB;
    protected int strokeBi;
    public int strokeCap;
    public int strokeColor;
    protected float strokeG;
    protected int strokeGi;
    public int strokeJoin;
    protected float strokeR;
    protected int strokeRi;
    public float strokeWeight;
    PStyle styleStack[];
    int styleStackDepth;
    public int textAlign;
    public int textAlignY;
    protected int textBreakCount;
    protected int textBreakStart[];
    protected int textBreakStop[];
    protected char textBuffer[];
    public PFont textFont;
    public float textLeading;
    public int textMode;
    public float textSize;
    protected char textWidthBuffer[];
    public PImage textureImage;
    public int textureMode;
    public float textureU;
    public float textureV;
    public boolean tint;
    protected float tintA;
    protected int tintAi;
    protected boolean tintAlpha;
    protected float tintB;
    protected int tintBi;
    public int tintColor;
    protected float tintG;
    protected int tintGi;
    protected float tintR;
    protected int tintRi;
    protected int vertexCount;
    protected float vertices[][];
    protected int width1;

    static 
    {
        sinLUT = new float[720];
        cosLUT = new float[720];
        for(int i = 0; i < 720; i++)
        {
            sinLUT[i] = (float)Math.sin((float)i * 0.01745329F * 0.5F);
            cosLUT[i] = (float)Math.cos((float)i * 0.01745329F * 0.5F);
        }

    }

    // Unreferenced inner class processing/core/PGraphics$AsyncImageSaver$1

/* anonymous class */
    class AsyncImageSaver._cls1
        implements Runnable
    {

        public void run()
        {
            long l;
            l = System.nanoTime();
            renderer.processImageBeforeAsyncSave(target);
            target.save(filename);
            l = System.nanoTime() - l;
            AsyncImageSaver asyncimagesaver = AsyncImageSaver.this;
            asyncimagesaver;
            JVM INSTR monitorenter ;
            if(avgNanos != 0L) goto _L2; else goto _L1
_L1:
            avgNanos = l;
_L3:
            targetPool.offer(target);
            return;
_L2:
            if(l >= avgNanos)
                break MISSING_BLOCK_LABEL_146;
            avgNanos = (l + avgNanos * 31L) / 32L;
              goto _L3
            Exception exception1;
            exception1;
            asyncimagesaver;
            JVM INSTR monitorexit ;
            throw exception1;
            Exception exception;
            exception;
            targetPool.offer(target);
            throw exception;
            avgNanos = l;
              goto _L3
        }

        final AsyncImageSaver this$0;
        final String val$filename;
        final PGraphics val$renderer;
        final PImage val$target;

            
            {
                this$0 = final_asyncimagesaver;
                renderer = pgraphics;
                target = pimage;
                filename = String.this;
                super();
            }
    }

}
