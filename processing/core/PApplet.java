// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.core;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.data.StringList;
import processing.data.Table;
import processing.data.XML;
import processing.event.Event;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import processing.event.TouchEvent;
import processing.opengl.PGL;
import processing.opengl.PGLES;
import processing.opengl.PGraphics2D;
import processing.opengl.PGraphics3D;
import processing.opengl.PGraphicsOpenGL;
import processing.opengl.PShader;

// Referenced classes of package processing.core:
//            PConstants, PImage, PGraphics, PFont, 
//            PGraphicsAndroid2D, PMatrix2D, PMatrix3D, PMatrix, 
//            PShape, PStyle

public class PApplet extends Fragment
    implements PConstants, Runnable
{
    class AsyncImageLoader extends Thread
    {

        public void run()
        {
            while(requestImageCount == requestImageMax) 
                try
                {
                    Thread.sleep(10L);
                }
                catch(InterruptedException interruptedexception) { }
            Object obj = PApplet.this;
            obj.requestImageCount = ((PApplet) (obj)).requestImageCount + 1;
            obj = loadImage(filename);
            if(obj == null)
            {
                vessel.width = -1;
                vessel.height = -1;
            } else
            {
                vessel.width = ((PImage) (obj)).width;
                vessel.height = ((PImage) (obj)).height;
                vessel.format = ((PImage) (obj)).format;
                vessel.pixels = ((PImage) (obj)).pixels;
                vessel.bitmap = ((PImage) (obj)).bitmap;
            }
            obj = PApplet.this;
            obj.requestImageCount = ((PApplet) (obj)).requestImageCount - 1;
        }

        String filename;
        final PApplet this$0;
        PImage vessel;

        public AsyncImageLoader(String s, PImage pimage)
        {
            this$0 = PApplet.this;
            super();
            filename = s;
            vessel = pimage;
        }
    }

    class InternalEventQueue
    {

        void add(Event event)
        {
            this;
            JVM INSTR monitorenter ;
            Event aevent[];
            int i;
            if(count == queue.length)
                queue = (Event[])PApplet.expand(queue);
            aevent = queue;
            i = count;
            count = i + 1;
            aevent[i] = event;
            this;
            JVM INSTR monitorexit ;
            return;
            event;
            throw event;
        }

        boolean available()
        {
            this;
            JVM INSTR monitorenter ;
            int i = count;
            boolean flag;
            if(i != 0)
                flag = true;
            else
                flag = false;
            this;
            JVM INSTR monitorexit ;
            return flag;
            Exception exception;
            exception;
            throw exception;
        }

        Event remove()
        {
            this;
            JVM INSTR monitorenter ;
            if(offset == count)
            {
                RuntimeException runtimeexception = JVM INSTR new #43  <Class RuntimeException>;
                runtimeexception.RuntimeException("Nothing left on the event queue.");
                throw runtimeexception;
            }
            break MISSING_BLOCK_LABEL_30;
            Exception exception;
            exception;
            this;
            JVM INSTR monitorexit ;
            throw exception;
            Event aevent[];
            int i;
            aevent = queue;
            i = offset;
            offset = i + 1;
            Event event = aevent[i];
            if(offset == count)
            {
                offset = 0;
                count = 0;
            }
            this;
            JVM INSTR monitorexit ;
            return event;
        }

        protected int count;
        protected int offset;
        protected Event queue[];
        final PApplet this$0;

        InternalEventQueue()
        {
            this$0 = PApplet.this;
            super();
            queue = new Event[10];
        }
    }

    class RegisteredMethods
    {

        void add(Object obj, Method method1)
        {
            if(findIndex(obj) == -1)
            {
                if(objects == null)
                {
                    objects = new Object[5];
                    methods = new Method[5];
                } else
                if(count == objects.length)
                {
                    objects = (Object[])PApplet.expand(((Object) (objects)));
                    methods = (Method[])PApplet.expand(methods);
                }
                objects[count] = obj;
                methods[count] = method1;
                count = count + 1;
            } else
            {
                die((new StringBuilder()).append(method1.getName()).append("() already added for this instance of ").append(obj.getClass().getName()).toString());
            }
        }

        protected int findIndex(Object obj)
        {
            int i = 0;
_L3:
            if(i >= count)
                break MISSING_BLOCK_LABEL_28;
            if(objects[i] != obj) goto _L2; else goto _L1
_L1:
            return i;
_L2:
            i++;
              goto _L3
            i = -1;
              goto _L1
        }

        void handle()
        {
            handle(emptyArgs);
        }

        void handle(Object aobj[])
        {
            int i = 0;
            do
            {
                if(i >= count)
                    break;
                try
                {
                    methods[i].invoke(objects[i], aobj);
                }
                catch(Exception exception)
                {
                    Object obj = exception;
                    if(exception instanceof InvocationTargetException)
                        obj = ((InvocationTargetException)exception).getCause();
                    if(obj instanceof RuntimeException)
                        throw (RuntimeException)obj;
                    ((Throwable) (obj)).printStackTrace();
                }
                i++;
            } while(true);
        }

        public void remove(Object obj)
        {
            int i = findIndex(obj);
            if(i != -1)
            {
                for(count = count - 1; i < count; i++)
                {
                    objects[i] = objects[i + 1];
                    methods[i] = methods[i + 1];
                }

                objects[count] = null;
                methods[count] = null;
            }
        }

        int count;
        Object emptyArgs[];
        Method methods[];
        Object objects[];
        final PApplet this$0;

        RegisteredMethods()
        {
            this$0 = PApplet.this;
            super();
            emptyArgs = new Object[0];
        }
    }

    public static class RendererChangeException extends RuntimeException
    {

        public RendererChangeException()
        {
        }
    }

    public class SketchSurfaceView extends SurfaceView
        implements android.view.SurfaceHolder.Callback
    {

        public boolean onKeyDown(int i, android.view.KeyEvent keyevent)
        {
            surfaceKeyDown(i, keyevent);
            return super.onKeyDown(i, keyevent);
        }

        public boolean onKeyUp(int i, android.view.KeyEvent keyevent)
        {
            surfaceKeyUp(i, keyevent);
            return super.onKeyUp(i, keyevent);
        }

        public boolean onTouchEvent(MotionEvent motionevent)
        {
            if(fullScreen && PApplet.SDK < 19)
                surfaceView.setSystemUiVisibility(2);
            return surfaceTouchEvent(motionevent);
        }

        public void onWindowFocusChanged(boolean flag)
        {
            super.onWindowFocusChanged(flag);
            surfaceWindowFocusChanged(flag);
        }

        public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k)
        {
            PApplet.this.surfaceChanged = true;
            displayHeight = j;
            displayHeight = k;
            g.setSize(sketchWidth(), sketchHeight());
        }

        public void surfaceCreated(SurfaceHolder surfaceholder)
        {
        }

        public void surfaceDestroyed(SurfaceHolder surfaceholder)
        {
        }

        PGraphicsAndroid2D g2;
        SurfaceHolder surfaceHolder;
        final PApplet this$0;

        public SketchSurfaceView(Context context, int i, int j, Class class1)
        {
            this$0 = PApplet.this;
            super(context);
            surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);
            if(class1.equals(processing/core/PGraphicsAndroid2D))
                g2 = new PGraphicsAndroid2D();
            else
                try
                {
                    g2 = (PGraphicsAndroid2D)class1.getConstructor(new Class[0]).newInstance(new Object[0]);
                }
                // Misplaced declaration of an exception variable
                catch(PApplet papplet)
                {
                    throw new RuntimeException("Error: Failed to initialize custom Android2D renderer", PApplet.this);
                }
            g2.setParent(PApplet.this);
            g2.setPrimary(true);
            g = g2;
            setFocusable(true);
            setFocusableInTouchMode(true);
            requestFocus();
        }
    }

    public class SketchSurfaceViewGL extends GLSurfaceView
    {

        public PGraphics getGraphics()
        {
            return g3;
        }

        public boolean onKeyDown(int i, android.view.KeyEvent keyevent)
        {
            surfaceKeyDown(i, keyevent);
            return super.onKeyDown(i, keyevent);
        }

        public boolean onKeyUp(int i, android.view.KeyEvent keyevent)
        {
            surfaceKeyUp(i, keyevent);
            return super.onKeyUp(i, keyevent);
        }

        public boolean onTouchEvent(MotionEvent motionevent)
        {
            if(fullScreen && PApplet.SDK < 19)
                surfaceView.setSystemUiVisibility(2);
            return surfaceTouchEvent(motionevent);
        }

        public void onWindowFocusChanged(boolean flag)
        {
            super.onWindowFocusChanged(flag);
            surfaceWindowFocusChanged(flag);
        }

        public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k)
        {
            super.surfaceChanged(surfaceholder, i, j, k);
            PApplet.this.surfaceChanged = true;
        }

        public void surfaceCreated(SurfaceHolder surfaceholder)
        {
            super.surfaceCreated(surfaceholder);
        }

        public void surfaceDestroyed(SurfaceHolder surfaceholder)
        {
            super.surfaceDestroyed(surfaceholder);
        }

        PGraphicsOpenGL g3;
        SurfaceHolder surfaceHolder;
        final PApplet this$0;

        public SketchSurfaceViewGL(Context context, int i, int j, Class class1)
        {
            this$0 = PApplet.this;
            super(context);
            if(((ActivityManager)activity.getSystemService("activity")).getDeviceConfigurationInfo().reqGlEsVersion >= 0x20000)
                i = 1;
            else
                i = 0;
            if(i == 0)
                throw new RuntimeException("OpenGL ES 2.0 is not supported by this device.");
            surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(2);
            if(class1.equals(processing/opengl/PGraphics2D))
                g3 = new PGraphics2D();
            else
            if(class1.equals(processing/opengl/PGraphics3D))
                g3 = new PGraphics3D();
            else
                try
                {
                    g3 = (PGraphicsOpenGL)class1.getConstructor(new Class[0]).newInstance(new Object[0]);
                }
                // Misplaced declaration of an exception variable
                catch(PApplet papplet)
                {
                    throw new RuntimeException("Error: Failed to initialize custom OpenGL renderer", PApplet.this);
                }
            g3.setParent(PApplet.this);
            g3.setPrimary(true);
            setEGLContextClientVersion(2);
            i = sketchQuality();
            if(1 < i)
                setEGLConfigChooser(((PGLES)g3.pgl).getConfigChooser(i));
            setRenderer(((PGLES)g3.pgl).getRenderer());
            setRenderMode(0);
            g = g3;
            setFocusable(true);
            setFocusableInTouchMode(true);
            requestFocus();
        }
    }


    public PApplet()
    {
        width = 100;
        height = 100;
        focused = false;
        windowFocused = false;
        viewFocused = false;
        millisOffset = System.currentTimeMillis();
        frameRate = 10F;
        frameRateLastNanos = 0L;
        frameRateTarget = 60F;
        frameRatePeriod = 0xfe502aL;
        external = false;
        renderer = "processing.core.PGraphicsAndroid2D";
        smooth = 1;
        fullScreen = false;
        windowColor = 0xffdddddd;
        registerMap = new HashMap();
        requestedNoLoop = false;
        eventQueue = new InternalEventQueue();
        perlin_octaves = 4;
        perlin_amp_falloff = 0.5F;
        requestImageMax = 4;
    }

    public static final float abs(float f)
    {
        float f1 = f;
        if(f < 0.0F)
            f1 = -f;
        return f1;
    }

    public static final int abs(int i)
    {
        int j = i;
        if(i < 0)
            j = -i;
        return j;
    }

    public static final float acos(float f)
    {
        return (float)Math.acos(f);
    }

    public static Object append(Object obj, Object obj1)
    {
        int i = Array.getLength(obj);
        obj = expand(obj, i + 1);
        Array.set(obj, i, obj1);
        return obj;
    }

    public static byte[] append(byte abyte0[], byte byte0)
    {
        abyte0 = expand(abyte0, abyte0.length + 1);
        abyte0[abyte0.length - 1] = byte0;
        return abyte0;
    }

    public static char[] append(char ac[], char c)
    {
        ac = expand(ac, ac.length + 1);
        ac[ac.length - 1] = c;
        return ac;
    }

    public static float[] append(float af[], float f)
    {
        af = expand(af, af.length + 1);
        af[af.length - 1] = f;
        return af;
    }

    public static int[] append(int ai[], int i)
    {
        ai = expand(ai, ai.length + 1);
        ai[ai.length - 1] = i;
        return ai;
    }

    public static String[] append(String as[], String s)
    {
        as = expand(as, as.length + 1);
        as[as.length - 1] = s;
        return as;
    }

    public static void arrayCopy(Object obj, int i, Object obj1, int j, int k)
    {
        System.arraycopy(obj, i, obj1, j, k);
    }

    public static void arrayCopy(Object obj, Object obj1)
    {
        System.arraycopy(obj, 0, obj1, 0, Array.getLength(obj));
    }

    public static void arrayCopy(Object obj, Object obj1, int i)
    {
        System.arraycopy(obj, 0, obj1, 0, i);
    }

    public static final float asin(float f)
    {
        return (float)Math.asin(f);
    }

    public static final float atan(float f)
    {
        return (float)Math.atan(f);
    }

    public static final float atan2(float f, float f1)
    {
        return (float)Math.atan2(f, f1);
    }

    public static final String binary(byte byte0)
    {
        return binary(byte0, 8);
    }

    public static final String binary(char c)
    {
        return binary(c, 16);
    }

    public static final String binary(int i)
    {
        return binary(i, 32);
    }

    public static final String binary(int i, int j)
    {
        String s;
        s = Integer.toBinaryString(i);
        i = j;
        if(j > 32)
            i = 32;
        j = s.length();
        if(j <= i) goto _L2; else goto _L1
_L1:
        s = s.substring(j - i);
_L4:
        return s;
_L2:
        if(j < i)
            s = (new StringBuilder()).append("00000000000000000000000000000000".substring(32 - (i - j))).append(s).toString();
        if(true) goto _L4; else goto _L3
_L3:
    }

    public static int blendColor(int i, int j, int k)
    {
        return PImage.blendColor(i, j, k);
    }

    public static final int ceil(float f)
    {
        return (int)Math.ceil(f);
    }

    public static String checkExtension(String s)
    {
        String s1 = s;
        if(s.toLowerCase().endsWith(".gz"))
            s1 = s.substring(0, s.length() - 3);
        int i = s1.lastIndexOf('.');
        if(i != -1)
            s = s1.substring(i + 1).toLowerCase();
        else
            s = null;
        return s;
    }

    public static Object concat(Object obj, Object obj1)
    {
        Object obj2 = obj.getClass().getComponentType();
        int i = Array.getLength(obj);
        int j = Array.getLength(obj1);
        obj2 = Array.newInstance(((Class) (obj2)), i + j);
        System.arraycopy(obj, 0, obj2, 0, i);
        System.arraycopy(obj1, 0, obj2, i, j);
        return obj2;
    }

    public static byte[] concat(byte abyte0[], byte abyte1[])
    {
        byte abyte2[] = new byte[abyte0.length + abyte1.length];
        System.arraycopy(abyte0, 0, abyte2, 0, abyte0.length);
        System.arraycopy(abyte1, 0, abyte2, abyte0.length, abyte1.length);
        return abyte2;
    }

    public static char[] concat(char ac[], char ac1[])
    {
        char ac2[] = new char[ac.length + ac1.length];
        System.arraycopy(ac, 0, ac2, 0, ac.length);
        System.arraycopy(ac1, 0, ac2, ac.length, ac1.length);
        return ac2;
    }

    public static float[] concat(float af[], float af1[])
    {
        float af2[] = new float[af.length + af1.length];
        System.arraycopy(af, 0, af2, 0, af.length);
        System.arraycopy(af1, 0, af2, af.length, af1.length);
        return af2;
    }

    public static int[] concat(int ai[], int ai1[])
    {
        int ai2[] = new int[ai.length + ai1.length];
        System.arraycopy(ai, 0, ai2, 0, ai.length);
        System.arraycopy(ai1, 0, ai2, ai.length, ai1.length);
        return ai2;
    }

    public static String[] concat(String as[], String as1[])
    {
        String as2[] = new String[as.length + as1.length];
        System.arraycopy(as, 0, as2, 0, as.length);
        System.arraycopy(as1, 0, as2, as.length, as1.length);
        return as2;
    }

    public static boolean[] concat(boolean aflag[], boolean aflag1[])
    {
        boolean aflag2[] = new boolean[aflag.length + aflag1.length];
        System.arraycopy(aflag, 0, aflag2, 0, aflag.length);
        System.arraycopy(aflag1, 0, aflag2, aflag.length, aflag1.length);
        return aflag2;
    }

    public static final float constrain(float f, float f1, float f2)
    {
        if(f >= f1)
            if(f > f2)
                f1 = f2;
            else
                f1 = f;
        return f1;
    }

    public static final int constrain(int i, int j, int k)
    {
        if(i >= j)
            if(i > k)
                j = k;
            else
                j = i;
        return j;
    }

    public static final float cos(float f)
    {
        return (float)Math.cos(f);
    }

    public static InputStream createInput(File file)
    {
        if(file == null)
            throw new IllegalArgumentException("File passed to createInput() was null");
        FileInputStream fileinputstream;
        fileinputstream = JVM INSTR new #538 <Class FileInputStream>;
        fileinputstream.FileInputStream(file);
        if(!file.getName().toLowerCase().endsWith(".gz")) goto _L2; else goto _L1
_L1:
        GZIPInputStream gzipinputstream;
        gzipinputstream = JVM INSTR new #548 <Class GZIPInputStream>;
        gzipinputstream.GZIPInputStream(fileinputstream);
        file = gzipinputstream;
_L4:
        return file;
_L2:
        file = fileinputstream;
        continue; /* Loop/switch isn't completed */
        IOException ioexception;
        ioexception;
        System.err.println((new StringBuilder()).append("Could not createInput() for ").append(file).toString());
        ioexception.printStackTrace();
        file = null;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public static OutputStream createOutput(File file)
    {
        FileOutputStream fileoutputstream;
        fileoutputstream = JVM INSTR new #572 <Class FileOutputStream>;
        fileoutputstream.FileOutputStream(file);
        if(!file.getName().toLowerCase().endsWith(".gz")) goto _L2; else goto _L1
_L1:
        file = JVM INSTR new #575 <Class GZIPOutputStream>;
        file.GZIPOutputStream(fileoutputstream);
_L4:
        return file;
_L2:
        file = fileoutputstream;
        continue; /* Loop/switch isn't completed */
        file;
        file.printStackTrace();
        file = null;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public static void createPath(File file)
    {
        String s = file.getParent();
        if(s == null)
            break MISSING_BLOCK_LABEL_30;
        File file1 = JVM INSTR new #543 <Class File>;
        file1.File(s);
        if(!file1.exists())
            file1.mkdirs();
_L1:
        return;
        SecurityException securityexception;
        securityexception;
        System.err.println((new StringBuilder()).append("You don't have permissions to create ").append(file.getAbsolutePath()).toString());
          goto _L1
    }

    public static void createPath(String s)
    {
        createPath(new File(s));
    }

    public static BufferedReader createReader(File file)
    {
        FileInputStream fileinputstream;
        Object obj;
        fileinputstream = JVM INSTR new #538 <Class FileInputStream>;
        fileinputstream.FileInputStream(file);
        if(!file.getName().toLowerCase().endsWith(".gz"))
            break MISSING_BLOCK_LABEL_92;
        obj = JVM INSTR new #548 <Class GZIPInputStream>;
        ((GZIPInputStream) (obj)).GZIPInputStream(fileinputstream);
_L1:
        obj = createReader(((InputStream) (obj)));
        return ((BufferedReader) (obj));
        obj;
        if(file == null)
        {
            throw new RuntimeException("File passed to createReader() was null");
        } else
        {
            ((Exception) (obj)).printStackTrace();
            throw new RuntimeException((new StringBuilder()).append("Couldn't create a reader for ").append(file.getAbsolutePath()).toString());
        }
        obj = fileinputstream;
          goto _L1
    }

    public static BufferedReader createReader(InputStream inputstream)
    {
        InputStreamReader inputstreamreader;
        inputstreamreader = JVM INSTR new #618 <Class InputStreamReader>;
        inputstreamreader.InputStreamReader(inputstream, "UTF-8");
        inputstream = inputstreamreader;
_L2:
        return new BufferedReader(inputstream);
        inputstream;
        inputstream = null;
        if(true) goto _L2; else goto _L1
_L1:
    }

    public static PrintWriter createWriter(File file)
    {
        FileOutputStream fileoutputstream;
        Object obj;
        fileoutputstream = JVM INSTR new #572 <Class FileOutputStream>;
        fileoutputstream.FileOutputStream(file);
        if(!file.getName().toLowerCase().endsWith(".gz"))
            break MISSING_BLOCK_LABEL_92;
        obj = JVM INSTR new #575 <Class GZIPOutputStream>;
        ((GZIPOutputStream) (obj)).GZIPOutputStream(fileoutputstream);
_L1:
        obj = createWriter(((OutputStream) (obj)));
        return ((PrintWriter) (obj));
        obj;
        if(file == null)
        {
            throw new RuntimeException("File passed to createWriter() was null");
        } else
        {
            ((Exception) (obj)).printStackTrace();
            throw new RuntimeException((new StringBuilder()).append("Couldn't create a writer for ").append(file.getAbsolutePath()).toString());
        }
        obj = fileoutputstream;
          goto _L1
    }

    public static PrintWriter createWriter(OutputStream outputstream)
    {
        try
        {
            BufferedOutputStream bufferedoutputstream = JVM INSTR new #639 <Class BufferedOutputStream>;
            bufferedoutputstream.BufferedOutputStream(outputstream, 8192);
            OutputStreamWriter outputstreamwriter = JVM INSTR new #644 <Class OutputStreamWriter>;
            outputstreamwriter.OutputStreamWriter(bufferedoutputstream, "UTF-8");
            outputstream = JVM INSTR new #649 <Class PrintWriter>;
            outputstream.PrintWriter(outputstreamwriter);
        }
        // Misplaced declaration of an exception variable
        catch(OutputStream outputstream)
        {
            outputstream = null;
        }
        return outputstream;
    }

    public static int day()
    {
        return Calendar.getInstance().get(5);
    }

    public static final float degrees(float f)
    {
        return 57.29578F * f;
    }

    public static final float dist(float f, float f1, float f2, float f3)
    {
        return sqrt(sq(f2 - f) + sq(f3 - f1));
    }

    public static final float dist(float f, float f1, float f2, float f3, float f4, float f5)
    {
        return sqrt(sq(f3 - f) + sq(f4 - f1) + sq(f5 - f2));
    }

    public static Process exec(String as[])
    {
        Process process;
        try
        {
            process = Runtime.getRuntime().exec(as);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            throw new RuntimeException((new StringBuilder()).append("Could not open ").append(join(as, ' ')).toString());
        }
        return process;
    }

    public static final float exp(float f)
    {
        return (float)Math.exp(f);
    }

    public static Object expand(Object obj)
    {
        return expand(obj, Array.getLength(obj) << 1);
    }

    public static Object expand(Object obj, int i)
    {
        Object obj1 = Array.newInstance(obj.getClass().getComponentType(), i);
        System.arraycopy(obj, 0, obj1, 0, Math.min(Array.getLength(obj), i));
        return obj1;
    }

    public static byte[] expand(byte abyte0[])
    {
        return expand(abyte0, abyte0.length << 1);
    }

    public static byte[] expand(byte abyte0[], int i)
    {
        byte abyte1[] = new byte[i];
        System.arraycopy(abyte0, 0, abyte1, 0, Math.min(i, abyte0.length));
        return abyte1;
    }

    public static char[] expand(char ac[])
    {
        return expand(ac, ac.length << 1);
    }

    public static char[] expand(char ac[], int i)
    {
        char ac1[] = new char[i];
        System.arraycopy(ac, 0, ac1, 0, Math.min(i, ac.length));
        return ac1;
    }

    public static float[] expand(float af[])
    {
        return expand(af, af.length << 1);
    }

    public static float[] expand(float af[], int i)
    {
        float af1[] = new float[i];
        System.arraycopy(af, 0, af1, 0, Math.min(i, af.length));
        return af1;
    }

    public static int[] expand(int ai[])
    {
        return expand(ai, ai.length << 1);
    }

    public static int[] expand(int ai[], int i)
    {
        int ai1[] = new int[i];
        System.arraycopy(ai, 0, ai1, 0, Math.min(i, ai.length));
        return ai1;
    }

    public static String[] expand(String as[])
    {
        return expand(as, as.length << 1);
    }

    public static String[] expand(String as[], int i)
    {
        String as1[] = new String[i];
        System.arraycopy(as, 0, as1, 0, Math.min(i, as.length));
        return as1;
    }

    public static PImage[] expand(PImage apimage[])
    {
        return expand(apimage, apimage.length << 1);
    }

    public static PImage[] expand(PImage apimage[], int i)
    {
        PImage apimage1[] = new PImage[i];
        System.arraycopy(apimage, 0, apimage1, 0, Math.min(i, apimage.length));
        return apimage1;
    }

    public static boolean[] expand(boolean aflag[])
    {
        return expand(aflag, aflag.length << 1);
    }

    public static boolean[] expand(boolean aflag[], int i)
    {
        boolean aflag1[] = new boolean[i];
        System.arraycopy(aflag, 0, aflag1, 0, Math.min(i, aflag.length));
        return aflag1;
    }

    public static final int floor(float f)
    {
        return (int)Math.floor(f);
    }

    public static String getExtension(String s)
    {
        String s1 = s.toLowerCase();
        int i = s.lastIndexOf('.');
        if(i != -1);
        s1 = s1.substring(i + 1);
        i = s1.indexOf('?');
        s = s1;
        if(i != -1)
            s = s1.substring(0, i);
        return s;
    }

    public static final String hex(byte byte0)
    {
        return hex(byte0, 2);
    }

    public static final String hex(char c)
    {
        return hex(c, 4);
    }

    public static final String hex(int i)
    {
        return hex(i, 8);
    }

    public static final String hex(int i, int j)
    {
        String s;
        s = Integer.toHexString(i).toUpperCase();
        i = j;
        if(j > 8)
            i = 8;
        j = s.length();
        if(j <= i) goto _L2; else goto _L1
_L1:
        s = s.substring(j - i);
_L4:
        return s;
_L2:
        if(j < i)
            s = (new StringBuilder()).append("00000000".substring(8 - (i - j))).append(s).toString();
        if(true) goto _L4; else goto _L3
_L3:
    }

    public static int hour()
    {
        return Calendar.getInstance().get(11);
    }

    public static String join(String as[], char c)
    {
        return join(as, String.valueOf(c));
    }

    public static String join(String as[], String s)
    {
        StringBuffer stringbuffer = new StringBuffer();
        for(int i = 0; i < as.length; i++)
        {
            if(i != 0)
                stringbuffer.append(s);
            stringbuffer.append(as[i]);
        }

        return stringbuffer.toString();
    }

    public static final float lerp(float f, float f1, float f2)
    {
        return (f1 - f) * f2 + f;
    }

    public static int lerpColor(int i, int j, float f, int k)
    {
        return PGraphics.lerpColor(i, j, f, k);
    }

    public static byte[] loadBytes(File file)
    {
        return loadBytes(createInput(file));
    }

    public static byte[] loadBytes(InputStream inputstream)
    {
        BufferedInputStream bufferedinputstream;
        int i;
        bufferedinputstream = JVM INSTR new #758 <Class BufferedInputStream>;
        bufferedinputstream.BufferedInputStream(inputstream);
        inputstream = JVM INSTR new #761 <Class ByteArrayOutputStream>;
        inputstream.ByteArrayOutputStream();
        i = bufferedinputstream.read();
_L2:
        if(i == -1)
            break; /* Loop/switch isn't completed */
        inputstream.write(i);
        i = bufferedinputstream.read();
        if(true) goto _L2; else goto _L1
_L1:
        try
        {
            inputstream = inputstream.toByteArray();
        }
        // Misplaced declaration of an exception variable
        catch(InputStream inputstream)
        {
            inputstream.printStackTrace();
            inputstream = null;
        }
        return inputstream;
    }

    public static JSONArray loadJSONArray(File file)
    {
        return new JSONArray(createReader(file));
    }

    public static JSONObject loadJSONObject(File file)
    {
        return new JSONObject(createReader(file));
    }

    public static String[] loadStrings(BufferedReader bufferedreader)
    {
        Object obj = new String[100];
        int i = 0;
_L2:
        String s = bufferedreader.readLine();
        if(s == null)
            break; /* Loop/switch isn't completed */
        String as[];
        if(i != obj.length)
            break MISSING_BLOCK_LABEL_43;
        as = new String[i << 1];
        System.arraycopy(obj, 0, as, 0, i);
        obj = as;
        obj[i] = s;
        i++;
        if(true) goto _L2; else goto _L1
_L1:
        bufferedreader.close();
        if(i != obj.length) goto _L4; else goto _L3
_L3:
        return ((String []) (obj));
_L4:
        bufferedreader = new String[i];
        System.arraycopy(obj, 0, bufferedreader, 0, i);
        obj = bufferedreader;
        continue; /* Loop/switch isn't completed */
        bufferedreader;
        bufferedreader.printStackTrace();
        obj = null;
        if(true) goto _L3; else goto _L5
_L5:
    }

    public static String[] loadStrings(File file)
    {
        file = createInput(file);
        if(file != null)
            file = loadStrings(((InputStream) (file)));
        else
            file = null;
        return file;
    }

    public static String[] loadStrings(InputStream inputstream)
    {
        BufferedReader bufferedreader;
        bufferedreader = JVM INSTR new #625 <Class BufferedReader>;
        InputStreamReader inputstreamreader = JVM INSTR new #618 <Class InputStreamReader>;
        inputstreamreader.InputStreamReader(inputstream, "UTF-8");
        bufferedreader.BufferedReader(inputstreamreader);
        inputstream = new String[100];
        int i = 0;
_L2:
        String s = bufferedreader.readLine();
        if(s == null)
            break; /* Loop/switch isn't completed */
        String as[];
        if(i != inputstream.length)
            break MISSING_BLOCK_LABEL_63;
        as = new String[i << 1];
        System.arraycopy(inputstream, 0, as, 0, i);
        inputstream = as;
        inputstream[i] = s;
        i++;
        if(true) goto _L2; else goto _L1
_L1:
        bufferedreader.close();
        if(i != inputstream.length) goto _L4; else goto _L3
_L3:
        return inputstream;
_L4:
        as = new String[i];
        System.arraycopy(inputstream, 0, as, 0, i);
        inputstream = as;
        continue; /* Loop/switch isn't completed */
        inputstream;
        inputstream.printStackTrace();
        inputstream = null;
        if(true) goto _L3; else goto _L5
_L5:
    }

    public static final float log(float f)
    {
        return (float)Math.log(f);
    }

    public static final float mag(float f, float f1)
    {
        return (float)Math.sqrt(f * f + f1 * f1);
    }

    public static final float mag(float f, float f1, float f2)
    {
        return (float)Math.sqrt(f * f + f1 * f1 + f2 * f2);
    }

    public static void main(String args[])
    {
    }

    public static final float map(float f, float f1, float f2, float f3, float f4)
    {
        return (f4 - f3) * ((f - f1) / (f2 - f1)) + f3;
    }

    public static String[] match(String s, String s1)
    {
        Matcher matcher = matchPattern(s1).matcher(s);
        if(matcher.find())
        {
            int i = matcher.groupCount() + 1;
            s1 = new String[i];
            int j = 0;
            do
            {
                s = s1;
                if(j >= i)
                    break;
                s1[j] = matcher.group(j);
                j++;
            } while(true);
        } else
        {
            s = null;
        }
        return s;
    }

    public static String[][] matchAll(String s, String s1)
    {
        ArrayList arraylist;
        int i;
        s = matchPattern(s1).matcher(s);
        arraylist = new ArrayList();
        i = s.groupCount() + 1;
        for(; s.find(); arraylist.add(s1))
        {
            s1 = new String[i];
            for(int j = 0; j < i; j++)
                s1[j] = s.group(j);

        }

        if(!arraylist.isEmpty()) goto _L2; else goto _L1
_L1:
        s = (String[][])null;
_L4:
        return s;
_L2:
        s1 = new String[arraylist.size()][i];
        int k = 0;
        do
        {
            s = s1;
            if(k >= s1.length)
                continue;
            s1[k] = (String[])arraylist.get(k);
            k++;
        } while(true);
        if(true) goto _L4; else goto _L3
_L3:
    }

    static Pattern matchPattern(String s)
    {
        Pattern pattern = null;
        Pattern pattern1;
        if(matchPatterns == null)
            matchPatterns = new HashMap();
        else
            pattern = (Pattern)matchPatterns.get(s);
        pattern1 = pattern;
        if(pattern == null)
        {
            if(matchPatterns.size() == 10)
                matchPatterns.clear();
            pattern1 = Pattern.compile(s, 40);
            matchPatterns.put(s, pattern1);
        }
        return pattern1;
    }

    public static final float max(float f, float f1)
    {
        if(f <= f1)
            f = f1;
        return f;
    }

    public static final float max(float f, float f1, float f2)
    {
        if(f <= f1) goto _L2; else goto _L1
_L1:
        float f3;
        f3 = f2;
        if(f > f2)
            f3 = f;
_L4:
        return f3;
_L2:
        f3 = f2;
        if(f1 > f2)
            f3 = f1;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public static final float max(float af[])
    {
        if(af.length == 0)
            throw new ArrayIndexOutOfBoundsException("Cannot use min() or max() on an empty array.");
        float f = af[0];
        for(int i = 1; i < af.length;)
        {
            float f1 = f;
            if(af[i] > f)
                f1 = af[i];
            i++;
            f = f1;
        }

        return f;
    }

    public static final int max(int i, int j)
    {
        if(i <= j)
            i = j;
        return i;
    }

    public static final int max(int i, int j, int k)
    {
        if(i <= j) goto _L2; else goto _L1
_L1:
        int l;
        l = k;
        if(i > k)
            l = i;
_L4:
        return l;
_L2:
        l = k;
        if(j > k)
            l = j;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public static final int max(int ai[])
    {
        if(ai.length == 0)
            throw new ArrayIndexOutOfBoundsException("Cannot use min() or max() on an empty array.");
        int i = ai[0];
        for(int j = 1; j < ai.length;)
        {
            int k = i;
            if(ai[j] > i)
                k = ai[j];
            j++;
            i = k;
        }

        return i;
    }

    public static final float min(float f, float f1)
    {
        if(f >= f1)
            f = f1;
        return f;
    }

    public static final float min(float f, float f1, float f2)
    {
        if(f >= f1) goto _L2; else goto _L1
_L1:
        float f3;
        f3 = f2;
        if(f < f2)
            f3 = f;
_L4:
        return f3;
_L2:
        f3 = f2;
        if(f1 < f2)
            f3 = f1;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public static final float min(float af[])
    {
        if(af.length == 0)
            throw new ArrayIndexOutOfBoundsException("Cannot use min() or max() on an empty array.");
        float f = af[0];
        for(int i = 1; i < af.length;)
        {
            float f1 = f;
            if(af[i] < f)
                f1 = af[i];
            i++;
            f = f1;
        }

        return f;
    }

    public static final int min(int i, int j)
    {
        if(i >= j)
            i = j;
        return i;
    }

    public static final int min(int i, int j, int k)
    {
        if(i >= j) goto _L2; else goto _L1
_L1:
        int l;
        l = k;
        if(i < k)
            l = i;
_L4:
        return l;
_L2:
        l = k;
        if(j < k)
            l = j;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public static final int min(int ai[])
    {
        if(ai.length == 0)
            throw new ArrayIndexOutOfBoundsException("Cannot use min() or max() on an empty array.");
        int i = ai[0];
        for(int j = 1; j < ai.length;)
        {
            int k = i;
            if(ai[j] < i)
                k = ai[j];
            j++;
            i = k;
        }

        return i;
    }

    public static int minute()
    {
        return Calendar.getInstance().get(12);
    }

    public static int month()
    {
        return Calendar.getInstance().get(2) + 1;
    }

    public static String nf(float f, int i, int j)
    {
        String s;
        if(float_nf != null && float_nf_left == i && float_nf_right == j && !float_nf_commas)
        {
            s = float_nf.format(f);
        } else
        {
            float_nf = NumberFormat.getInstance();
            float_nf.setGroupingUsed(false);
            float_nf_commas = false;
            if(i != 0)
                float_nf.setMinimumIntegerDigits(i);
            if(j != 0)
            {
                float_nf.setMinimumFractionDigits(j);
                float_nf.setMaximumFractionDigits(j);
            }
            float_nf_left = i;
            float_nf_right = j;
            s = float_nf.format(f);
        }
        return s;
    }

    public static String nf(int i, int j)
    {
        String s;
        if(int_nf != null && int_nf_digits == j && !int_nf_commas)
        {
            s = int_nf.format(i);
        } else
        {
            int_nf = NumberFormat.getInstance();
            int_nf.setGroupingUsed(false);
            int_nf_commas = false;
            int_nf.setMinimumIntegerDigits(j);
            int_nf_digits = j;
            s = int_nf.format(i);
        }
        return s;
    }

    public static String[] nf(float af[], int i, int j)
    {
        String as[] = new String[af.length];
        for(int k = 0; k < as.length; k++)
            as[k] = nf(af[k], i, j);

        return as;
    }

    public static String[] nf(int ai[], int i)
    {
        String as[] = new String[ai.length];
        for(int j = 0; j < as.length; j++)
            as[j] = nf(ai[j], i);

        return as;
    }

    public static String nfc(float f, int i)
    {
        String s;
        if(float_nf != null && float_nf_left == 0 && float_nf_right == i && float_nf_commas)
        {
            s = float_nf.format(f);
        } else
        {
            float_nf = NumberFormat.getInstance();
            float_nf.setGroupingUsed(true);
            float_nf_commas = true;
            if(i != 0)
            {
                float_nf.setMinimumFractionDigits(i);
                float_nf.setMaximumFractionDigits(i);
            }
            float_nf_left = 0;
            float_nf_right = i;
            s = float_nf.format(f);
        }
        return s;
    }

    public static String nfc(int i)
    {
        String s;
        if(int_nf != null && int_nf_digits == 0 && int_nf_commas)
        {
            s = int_nf.format(i);
        } else
        {
            int_nf = NumberFormat.getInstance();
            int_nf.setGroupingUsed(true);
            int_nf_commas = true;
            int_nf.setMinimumIntegerDigits(0);
            int_nf_digits = 0;
            s = int_nf.format(i);
        }
        return s;
    }

    public static String[] nfc(float af[], int i)
    {
        String as[] = new String[af.length];
        for(int j = 0; j < as.length; j++)
            as[j] = nfc(af[j], i);

        return as;
    }

    public static String[] nfc(int ai[])
    {
        String as[] = new String[ai.length];
        for(int i = 0; i < as.length; i++)
            as[i] = nfc(ai[i]);

        return as;
    }

    public static String nfp(float f, int i, int j)
    {
        String s;
        if(f < 0.0F)
            s = nf(f, i, j);
        else
            s = (new StringBuilder()).append('+').append(nf(f, i, j)).toString();
        return s;
    }

    public static String nfp(int i, int j)
    {
        String s;
        if(i < 0)
            s = nf(i, j);
        else
            s = (new StringBuilder()).append('+').append(nf(i, j)).toString();
        return s;
    }

    public static String[] nfp(float af[], int i, int j)
    {
        String as[] = new String[af.length];
        for(int k = 0; k < as.length; k++)
            as[k] = nfp(af[k], i, j);

        return as;
    }

    public static String[] nfp(int ai[], int i)
    {
        String as[] = new String[ai.length];
        for(int j = 0; j < as.length; j++)
            as[j] = nfp(ai[j], i);

        return as;
    }

    public static String nfs(float f, int i, int j)
    {
        String s;
        if(f < 0.0F)
            s = nf(f, i, j);
        else
            s = (new StringBuilder()).append(' ').append(nf(f, i, j)).toString();
        return s;
    }

    public static String nfs(int i, int j)
    {
        String s;
        if(i < 0)
            s = nf(i, j);
        else
            s = (new StringBuilder()).append(' ').append(nf(i, j)).toString();
        return s;
    }

    public static String[] nfs(float af[], int i, int j)
    {
        String as[] = new String[af.length];
        for(int k = 0; k < as.length; k++)
            as[k] = nfs(af[k], i, j);

        return as;
    }

    public static String[] nfs(int ai[], int i)
    {
        String as[] = new String[ai.length];
        for(int j = 0; j < as.length; j++)
            as[j] = nfs(ai[j], i);

        return as;
    }

    private float noise_fsc(float f)
    {
        return 0.5F * (1.0F - perlin_cosTable[(int)((float)perlin_PI * f) % perlin_TWOPI]);
    }

    public static final float norm(float f, float f1, float f2)
    {
        return (f - f1) / (f2 - f1);
    }

    public static Process open(String as[])
    {
        return exec(as);
    }

    public static void open(String s)
    {
        open(new String[] {
            s
        });
    }

    public static final boolean parseBoolean(int i)
    {
        boolean flag;
        if(i != 0)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public static final boolean parseBoolean(String s)
    {
        return (new Boolean(s)).booleanValue();
    }

    public static final boolean[] parseBoolean(byte abyte0[])
    {
        boolean aflag[] = new boolean[abyte0.length];
        int i = 0;
        while(i < abyte0.length) 
        {
            boolean flag;
            if(abyte0[i] != 0)
                flag = true;
            else
                flag = false;
            aflag[i] = flag;
            i++;
        }
        return aflag;
    }

    public static final boolean[] parseBoolean(int ai[])
    {
        boolean aflag[] = new boolean[ai.length];
        int i = 0;
        while(i < ai.length) 
        {
            boolean flag;
            if(ai[i] != 0)
                flag = true;
            else
                flag = false;
            aflag[i] = flag;
            i++;
        }
        return aflag;
    }

    public static final boolean[] parseBoolean(String as[])
    {
        boolean aflag[] = new boolean[as.length];
        for(int i = 0; i < as.length; i++)
            aflag[i] = (new Boolean(as[i])).booleanValue();

        return aflag;
    }

    public static final byte parseByte(char c)
    {
        return (byte)c;
    }

    public static final byte parseByte(float f)
    {
        return (byte)(int)f;
    }

    public static final byte parseByte(int i)
    {
        return (byte)i;
    }

    public static final byte parseByte(boolean flag)
    {
        byte byte0;
        if(flag)
        {
            boolean flag1 = true;
            byte0 = flag1;
        } else
        {
            boolean flag2 = false;
            byte0 = flag2;
        }
        return byte0;
    }

    public static final byte[] parseByte(char ac[])
    {
        byte abyte0[] = new byte[ac.length];
        for(int i = 0; i < ac.length; i++)
            abyte0[i] = (byte)ac[i];

        return abyte0;
    }

    public static final byte[] parseByte(float af[])
    {
        byte abyte0[] = new byte[af.length];
        for(int i = 0; i < af.length; i++)
            abyte0[i] = (byte)(int)af[i];

        return abyte0;
    }

    public static final byte[] parseByte(int ai[])
    {
        byte abyte0[] = new byte[ai.length];
        for(int i = 0; i < ai.length; i++)
            abyte0[i] = (byte)ai[i];

        return abyte0;
    }

    public static final byte[] parseByte(boolean aflag[])
    {
        byte abyte0[] = new byte[aflag.length];
        int i = 0;
        while(i < aflag.length) 
        {
            int j;
            if(aflag[i])
                j = 1;
            else
                j = 0;
            abyte0[i] = (byte)j;
            i++;
        }
        return abyte0;
    }

    public static final float[] parseByte(byte abyte0[])
    {
        float af[] = new float[abyte0.length];
        for(int i = 0; i < abyte0.length; i++)
            af[i] = abyte0[i];

        return af;
    }

    public static final char parseChar(byte byte0)
    {
        return (char)(byte0 & 0xff);
    }

    public static final char parseChar(int i)
    {
        return (char)i;
    }

    public static final char[] parseChar(byte abyte0[])
    {
        char ac[] = new char[abyte0.length];
        for(int i = 0; i < abyte0.length; i++)
            ac[i] = (char)(abyte0[i] & 0xff);

        return ac;
    }

    public static final char[] parseChar(int ai[])
    {
        char ac[] = new char[ai.length];
        for(int i = 0; i < ai.length; i++)
            ac[i] = (char)ai[i];

        return ac;
    }

    public static final float parseFloat(int i)
    {
        return (float)i;
    }

    public static final float parseFloat(String s)
    {
        return parseFloat(s, (0.0F / 0.0F));
    }

    public static final float parseFloat(String s, float f)
    {
        float f1;
        Float float1 = JVM INSTR new #992 <Class Float>;
        float1.Float(s);
        f1 = float1.floatValue();
        f = f1;
_L2:
        return f;
        s;
        if(true) goto _L2; else goto _L1
_L1:
    }

    public static final float[] parseFloat(int ai[])
    {
        float af[] = new float[ai.length];
        for(int i = 0; i < ai.length; i++)
            af[i] = ai[i];

        return af;
    }

    public static final float[] parseFloat(String as[])
    {
        return parseFloat(as, (0.0F / 0.0F));
    }

    public static final float[] parseFloat(String as[], float f)
    {
        float af[] = new float[as.length];
        int i = 0;
        while(i < as.length) 
        {
            try
            {
                Float float1 = JVM INSTR new #992 <Class Float>;
                float1.Float(as[i]);
                af[i] = float1.floatValue();
            }
            catch(NumberFormatException numberformatexception)
            {
                af[i] = f;
            }
            i++;
        }
        return af;
    }

    public static final int parseInt(byte byte0)
    {
        return byte0 & 0xff;
    }

    public static final int parseInt(char c)
    {
        return c;
    }

    public static final int parseInt(float f)
    {
        return (int)f;
    }

    public static final int parseInt(String s)
    {
        return parseInt(s, 0);
    }

    public static final int parseInt(String s, int i)
    {
        int j = s.indexOf('.');
        if(j != -1) goto _L2; else goto _L1
_L1:
        j = Integer.parseInt(s);
        i = j;
_L4:
        return i;
_L2:
        j = Integer.parseInt(s.substring(0, j));
        i = j;
        continue; /* Loop/switch isn't completed */
        s;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public static final int parseInt(boolean flag)
    {
        int i;
        if(flag)
            i = 1;
        else
            i = 0;
        return i;
    }

    public static final int[] parseInt(byte abyte0[])
    {
        int ai[] = new int[abyte0.length];
        for(int i = 0; i < abyte0.length; i++)
            ai[i] = abyte0[i] & 0xff;

        return ai;
    }

    public static final int[] parseInt(char ac[])
    {
        int ai[] = new int[ac.length];
        for(int i = 0; i < ac.length; i++)
            ai[i] = ac[i];

        return ai;
    }

    public static int[] parseInt(float af[])
    {
        int ai[] = new int[af.length];
        for(int i = 0; i < af.length; i++)
            ai[i] = (int)af[i];

        return ai;
    }

    public static int[] parseInt(String as[])
    {
        return parseInt(as, 0);
    }

    public static int[] parseInt(String as[], int i)
    {
        int ai[] = new int[as.length];
        int j = 0;
        while(j < as.length) 
        {
            try
            {
                ai[j] = Integer.parseInt(as[j]);
            }
            catch(NumberFormatException numberformatexception)
            {
                ai[j] = i;
            }
            j++;
        }
        return ai;
    }

    public static final int[] parseInt(boolean aflag[])
    {
        int ai[] = new int[aflag.length];
        int i = 0;
        while(i < aflag.length) 
        {
            int j;
            if(aflag[i])
                j = 1;
            else
                j = 0;
            ai[i] = j;
            i++;
        }
        return ai;
    }

    public static final float pow(float f, float f1)
    {
        return (float)Math.pow(f, f1);
    }

    public static void print(byte byte0)
    {
        System.out.print(byte0);
        System.out.flush();
    }

    public static void print(char c)
    {
        System.out.print(c);
        System.out.flush();
    }

    public static void print(float f)
    {
        System.out.print(f);
        System.out.flush();
    }

    public static void print(int i)
    {
        System.out.print(i);
        System.out.flush();
    }

    public static void print(String s)
    {
        System.out.print(s);
        System.out.flush();
    }

    public static void print(boolean flag)
    {
        System.out.print(flag);
        System.out.flush();
    }

    public static transient void print(Object aobj[])
    {
        StringBuilder stringbuilder = new StringBuilder();
        int i = aobj.length;
        int j = 0;
        while(j < i) 
        {
            Object obj = aobj[j];
            if(stringbuilder.length() != 0)
                stringbuilder.append(" ");
            if(obj == null)
                stringbuilder.append("null");
            else
                stringbuilder.append(obj.toString());
            j++;
        }
        System.out.print(stringbuilder.toString());
    }

    public static void printArray(Object obj)
    {
        boolean flag;
        boolean flag1;
        boolean flag2;
        boolean flag3;
        boolean flag4;
        boolean flag5;
        boolean flag6;
        int i;
        flag = false;
        flag1 = false;
        flag2 = false;
        flag3 = false;
        flag4 = false;
        flag5 = false;
        flag6 = false;
        i = 0;
        if(obj != null) goto _L2; else goto _L1
_L1:
        System.out.println("null");
_L4:
        System.out.flush();
        return;
_L2:
        String s = obj.getClass().getName();
        if(s.charAt(0) == '[')
            switch(s.charAt(1))
            {
            default:
                System.out.println(obj);
                break;

            case 91: // '['
                System.out.println(obj);
                break;

            case 76: // 'L'
                obj = ((Object) ((Object[])obj));
                while(i < obj.length) 
                {
                    if(obj[i] instanceof String)
                        System.out.println((new StringBuilder()).append("[").append(i).append("] \"").append(obj[i]).append("\"").toString());
                    else
                        System.out.println((new StringBuilder()).append("[").append(i).append("] ").append(obj[i]).toString());
                    i++;
                }
                break;

            case 90: // 'Z'
                obj = (boolean[])obj;
                int j = ((flag) ? 1 : 0);
                while(j < obj.length) 
                {
                    System.out.println((new StringBuilder()).append("[").append(j).append("] ").append(obj[j]).toString());
                    j++;
                }
                break;

            case 66: // 'B'
                obj = (byte[])obj;
                int k = ((flag1) ? 1 : 0);
                while(k < obj.length) 
                {
                    System.out.println((new StringBuilder()).append("[").append(k).append("] ").append(obj[k]).toString());
                    k++;
                }
                break;

            case 67: // 'C'
                obj = (char[])obj;
                int l = ((flag2) ? 1 : 0);
                while(l < obj.length) 
                {
                    System.out.println((new StringBuilder()).append("[").append(l).append("] '").append(obj[l]).append("'").toString());
                    l++;
                }
                break;

            case 73: // 'I'
                obj = (int[])obj;
                int i1 = ((flag3) ? 1 : 0);
                while(i1 < obj.length) 
                {
                    System.out.println((new StringBuilder()).append("[").append(i1).append("] ").append(obj[i1]).toString());
                    i1++;
                }
                break;

            case 74: // 'J'
                obj = (long[])obj;
                int j1 = ((flag4) ? 1 : 0);
                while(j1 < obj.length) 
                {
                    System.out.println((new StringBuilder()).append("[").append(j1).append("] ").append(obj[j1]).toString());
                    j1++;
                }
                break;

            case 70: // 'F'
                obj = (float[])obj;
                int k1 = ((flag5) ? 1 : 0);
                while(k1 < obj.length) 
                {
                    System.out.println((new StringBuilder()).append("[").append(k1).append("] ").append(obj[k1]).toString());
                    k1++;
                }
                break;

            case 68: // 'D'
                obj = (double[])obj;
                int l1 = ((flag6) ? 1 : 0);
                while(l1 < obj.length) 
                {
                    System.out.println((new StringBuilder()).append("[").append(l1).append("] ").append(obj[l1]).toString());
                    l1++;
                }
                break;
            }
        else
            System.out.println(obj);
        if(true) goto _L4; else goto _L3
_L3:
    }

    public static void println()
    {
        System.out.println();
    }

    public static void println(byte byte0)
    {
        print(byte0);
        System.out.println();
    }

    public static void println(char c)
    {
        print(c);
        System.out.println();
    }

    public static void println(float f)
    {
        print(f);
        System.out.println();
    }

    public static void println(int i)
    {
        print(i);
        System.out.println();
    }

    public static void println(Object obj)
    {
        boolean flag;
        boolean flag1;
        boolean flag2;
        boolean flag3;
        boolean flag4;
        int i;
        flag = false;
        flag1 = false;
        flag2 = false;
        flag3 = false;
        flag4 = false;
        i = 0;
        if(obj != null) goto _L2; else goto _L1
_L1:
        System.out.println("null");
_L4:
        return;
_L2:
        String s = obj.getClass().getName();
        if(s.charAt(0) == '[')
            switch(s.charAt(1))
            {
            default:
                System.out.println(obj);
                break;

            case 91: // '['
                System.out.println(obj);
                break;

            case 76: // 'L'
                obj = ((Object) ((Object[])obj));
                while(i < obj.length) 
                {
                    if(obj[i] instanceof String)
                        System.out.println((new StringBuilder()).append("[").append(i).append("] \"").append(obj[i]).append("\"").toString());
                    else
                        System.out.println((new StringBuilder()).append("[").append(i).append("] ").append(obj[i]).toString());
                    i++;
                }
                break;

            case 90: // 'Z'
                obj = (boolean[])obj;
                int j = ((flag) ? 1 : 0);
                while(j < obj.length) 
                {
                    System.out.println((new StringBuilder()).append("[").append(j).append("] ").append(obj[j]).toString());
                    j++;
                }
                break;

            case 66: // 'B'
                obj = (byte[])obj;
                int k = ((flag1) ? 1 : 0);
                while(k < obj.length) 
                {
                    System.out.println((new StringBuilder()).append("[").append(k).append("] ").append(obj[k]).toString());
                    k++;
                }
                break;

            case 67: // 'C'
                obj = (char[])obj;
                int l = ((flag2) ? 1 : 0);
                while(l < obj.length) 
                {
                    System.out.println((new StringBuilder()).append("[").append(l).append("] '").append(obj[l]).append("'").toString());
                    l++;
                }
                break;

            case 73: // 'I'
                obj = (int[])obj;
                int i1 = ((flag3) ? 1 : 0);
                while(i1 < obj.length) 
                {
                    System.out.println((new StringBuilder()).append("[").append(i1).append("] ").append(obj[i1]).toString());
                    i1++;
                }
                break;

            case 70: // 'F'
                obj = (float[])obj;
                int j1 = ((flag4) ? 1 : 0);
                while(j1 < obj.length) 
                {
                    System.out.println((new StringBuilder()).append("[").append(j1).append("] ").append(obj[j1]).toString());
                    j1++;
                }
                break;
            }
        else
            System.out.println(obj);
        if(true) goto _L4; else goto _L3
_L3:
    }

    public static void println(String s)
    {
        print(s);
        System.out.println();
    }

    public static void println(boolean flag)
    {
        print(flag);
        System.out.println();
    }

    public static transient void println(Object aobj[])
    {
        print(aobj);
        println();
    }

    public static final float radians(float f)
    {
        return 0.01745329F * f;
    }

    private void registerNoArgs(String s, Object obj)
    {
        Object obj1;
        RegisteredMethods registeredmethods;
        obj1 = (RegisteredMethods)registerMap.get(s);
        registeredmethods = ((RegisteredMethods) (obj1));
        if(obj1 == null)
        {
            registeredmethods = new RegisteredMethods();
            registerMap.put(s, registeredmethods);
        }
        obj1 = obj.getClass();
        registeredmethods.add(obj, ((Class) (obj1)).getMethod(s, new Class[0]));
_L1:
        return;
        Object obj2;
        obj2;
        die((new StringBuilder()).append("There is no public ").append(s).append("() method in the class ").append(obj.getClass().getName()).toString());
          goto _L1
        obj2;
        die((new StringBuilder()).append("Could not register ").append(s).append(" + () for ").append(obj).toString(), ((Exception) (obj2)));
          goto _L1
    }

    private void registerWithArgs(String s, Object obj, Class aclass[])
    {
        Object obj1;
        RegisteredMethods registeredmethods;
        obj1 = (RegisteredMethods)registerMap.get(s);
        registeredmethods = ((RegisteredMethods) (obj1));
        if(obj1 == null)
        {
            registeredmethods = new RegisteredMethods();
            registerMap.put(s, registeredmethods);
        }
        obj1 = obj.getClass();
        registeredmethods.add(obj, ((Class) (obj1)).getMethod(s, aclass));
_L1:
        return;
        aclass;
        die((new StringBuilder()).append("There is no public ").append(s).append("() method in the class ").append(obj.getClass().getName()).toString());
          goto _L1
        aclass;
        die((new StringBuilder()).append("Could not register ").append(s).append(" + () for ").append(obj).toString(), aclass);
          goto _L1
    }

    public static Object reverse(Object obj)
    {
        Object obj1 = obj.getClass().getComponentType();
        int i = Array.getLength(obj);
        obj1 = Array.newInstance(((Class) (obj1)), i);
        for(int j = 0; j < i; j++)
            Array.set(obj1, j, Array.get(obj, i - 1 - j));

        return obj1;
    }

    public static byte[] reverse(byte abyte0[])
    {
        byte abyte1[] = new byte[abyte0.length];
        int i = abyte0.length;
        for(int j = 0; j < abyte0.length; j++)
            abyte1[j] = abyte0[i - 1 - j];

        return abyte1;
    }

    public static char[] reverse(char ac[])
    {
        char ac1[] = new char[ac.length];
        int i = ac.length;
        for(int j = 0; j < ac.length; j++)
            ac1[j] = ac[i - 1 - j];

        return ac1;
    }

    public static float[] reverse(float af[])
    {
        float af1[] = new float[af.length];
        int i = af.length;
        for(int j = 0; j < af.length; j++)
            af1[j] = af[i - 1 - j];

        return af1;
    }

    public static int[] reverse(int ai[])
    {
        int ai1[] = new int[ai.length];
        int i = ai.length;
        for(int j = 0; j < ai.length; j++)
            ai1[j] = ai[i - 1 - j];

        return ai1;
    }

    public static String[] reverse(String as[])
    {
        String as1[] = new String[as.length];
        int i = as.length;
        for(int j = 0; j < as.length; j++)
            as1[j] = as[i - 1 - j];

        return as1;
    }

    public static boolean[] reverse(boolean aflag[])
    {
        boolean aflag1[] = new boolean[aflag.length];
        int i = aflag.length;
        for(int j = 0; j < aflag.length; j++)
            aflag1[j] = aflag[i - 1 - j];

        return aflag1;
    }

    public static final int round(float f)
    {
        return Math.round(f);
    }

    public static void saveBytes(File file, byte abyte0[])
    {
        FileOutputStream fileoutputstream;
        Object obj;
        createPath(file.getAbsolutePath());
        fileoutputstream = JVM INSTR new #572 <Class FileOutputStream>;
        fileoutputstream.FileOutputStream(file);
        if(!file.getName().toLowerCase().endsWith(".gz"))
            break MISSING_BLOCK_LABEL_85;
        obj = JVM INSTR new #575 <Class GZIPOutputStream>;
        ((GZIPOutputStream) (obj)).GZIPOutputStream(fileoutputstream);
_L2:
        saveBytes(((OutputStream) (obj)), abyte0);
        ((OutputStream) (obj)).close();
_L1:
        return;
        abyte0;
        System.err.println((new StringBuilder()).append("error saving bytes to ").append(file).toString());
        abyte0.printStackTrace();
          goto _L1
        obj = fileoutputstream;
          goto _L2
    }

    public static void saveBytes(OutputStream outputstream, byte abyte0[])
    {
        outputstream.write(abyte0);
        outputstream.flush();
_L1:
        return;
        outputstream;
        outputstream.printStackTrace();
          goto _L1
    }

    public static boolean saveStream(File file, InputStream inputstream)
    {
        File file1;
        boolean flag;
        File file2;
        file1 = null;
        flag = false;
        file2 = file1;
        Object obj = file.getParentFile();
        file2 = file1;
        createPath(file);
        file2 = file1;
        file1 = File.createTempFile(file.getName(), null, ((File) (obj)));
        file2 = file1;
        obj = JVM INSTR new #758 <Class BufferedInputStream>;
        file2 = file1;
        ((BufferedInputStream) (obj)).BufferedInputStream(inputstream, 16384);
        file2 = file1;
        FileOutputStream fileoutputstream = JVM INSTR new #572 <Class FileOutputStream>;
        file2 = file1;
        fileoutputstream.FileOutputStream(file1);
        file2 = file1;
        inputstream = JVM INSTR new #639 <Class BufferedOutputStream>;
        file2 = file1;
        inputstream.BufferedOutputStream(fileoutputstream);
        file2 = file1;
        byte abyte0[] = new byte[8192];
_L2:
        file2 = file1;
        int i = ((BufferedInputStream) (obj)).read(abyte0);
        if(i == -1)
            break; /* Loop/switch isn't completed */
        file2 = file1;
        inputstream.write(abyte0, 0, i);
        if(true) goto _L2; else goto _L1
_L4:
        return flag;
_L1:
        file2 = file1;
        inputstream.flush();
        file2 = file1;
        inputstream.close();
        file2 = file1;
        if(!file.exists())
            break MISSING_BLOCK_LABEL_235;
        file2 = file1;
        if(file.delete())
            break MISSING_BLOCK_LABEL_235;
        file2 = file1;
        obj = System.err;
        file2 = file1;
        inputstream = JVM INSTR new #462 <Class StringBuilder>;
        file2 = file1;
        inputstream.StringBuilder();
        file2 = file1;
        ((PrintStream) (obj)).println(inputstream.append("Could not replace ").append(file.getAbsolutePath()).append(".").toString());
        file2 = file1;
        if(file1.renameTo(file))
            break MISSING_BLOCK_LABEL_294;
        file2 = file1;
        file = System.err;
        file2 = file1;
        inputstream = JVM INSTR new #462 <Class StringBuilder>;
        file2 = file1;
        inputstream.StringBuilder();
        file2 = file1;
        try
        {
            file.println(inputstream.append("Could not rename temporary file ").append(file1.getAbsolutePath()).toString());
        }
        // Misplaced declaration of an exception variable
        catch(File file)
        {
            if(file2 != null)
                file2.delete();
            file.printStackTrace();
        }
        continue; /* Loop/switch isn't completed */
        flag = true;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public static void saveStrings(File file, String as[])
    {
        FileOutputStream fileoutputstream;
        String s = file.getAbsolutePath();
        createPath(s);
        fileoutputstream = JVM INSTR new #572 <Class FileOutputStream>;
        fileoutputstream.FileOutputStream(s);
        if(!file.getName().toLowerCase().endsWith(".gz"))
            break MISSING_BLOCK_LABEL_61;
        file = JVM INSTR new #575 <Class GZIPOutputStream>;
        file.GZIPOutputStream(fileoutputstream);
_L2:
        saveStrings(((OutputStream) (file)), as);
        file.close();
_L1:
        return;
        file;
        file.printStackTrace();
          goto _L1
        file = fileoutputstream;
          goto _L2
    }

    public static void saveStrings(OutputStream outputstream, String as[])
    {
        OutputStreamWriter outputstreamwriter = JVM INSTR new #644 <Class OutputStreamWriter>;
        outputstreamwriter.OutputStreamWriter(outputstream, "UTF-8");
        outputstream = JVM INSTR new #649 <Class PrintWriter>;
        outputstream.PrintWriter(outputstreamwriter);
        int i = 0;
_L2:
        if(i >= as.length)
            break; /* Loop/switch isn't completed */
        outputstream.println(as[i]);
        i++;
        if(true) goto _L2; else goto _L1
_L1:
        outputstream.flush();
_L4:
        return;
        outputstream;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public static int second()
    {
        return Calendar.getInstance().get(13);
    }

    private void setFullScreenVisibility()
    {
        if(fullScreen)
        {
            char c;
            if(SDK < 19)
                c = '\002';
            else
                c = '\u1706';
            surfaceView.setSystemUiVisibility(c);
        }
    }

    public static Object shorten(Object obj)
    {
        return subset(obj, 0, Array.getLength(obj) - 1);
    }

    public static byte[] shorten(byte abyte0[])
    {
        return subset(abyte0, 0, abyte0.length - 1);
    }

    public static char[] shorten(char ac[])
    {
        return subset(ac, 0, ac.length - 1);
    }

    public static float[] shorten(float af[])
    {
        return subset(af, 0, af.length - 1);
    }

    public static int[] shorten(int ai[])
    {
        return subset(ai, 0, ai.length - 1);
    }

    public static String[] shorten(String as[])
    {
        return subset(as, 0, as.length - 1);
    }

    public static boolean[] shorten(boolean aflag[])
    {
        return subset(aflag, 0, aflag.length - 1);
    }

    public static void showDepthWarning(String s)
    {
        PGraphics.showDepthWarning(s);
    }

    public static void showDepthWarningXYZ(String s)
    {
        PGraphics.showDepthWarningXYZ(s);
    }

    public static void showMethodWarning(String s)
    {
        PGraphics.showMethodWarning(s);
    }

    public static void showMissingWarning(String s)
    {
        PGraphics.showMissingWarning(s);
    }

    public static void showVariationWarning(String s)
    {
        PGraphics.showVariationWarning(s);
    }

    public static final float sin(float f)
    {
        return (float)Math.sin(f);
    }

    private void smoothWarning(String s)
    {
        String s1;
        if(external)
            s1 = "setup";
        else
            s1 = "settings";
        PGraphics.showWarning("%s() can only be used inside %s()", new Object[] {
            s, s1
        });
    }

    public static byte[] sort(byte abyte0[])
    {
        return sort(abyte0, abyte0.length);
    }

    public static byte[] sort(byte abyte0[], int i)
    {
        byte abyte1[] = new byte[abyte0.length];
        System.arraycopy(abyte0, 0, abyte1, 0, abyte0.length);
        Arrays.sort(abyte1, 0, i);
        return abyte1;
    }

    public static char[] sort(char ac[])
    {
        return sort(ac, ac.length);
    }

    public static char[] sort(char ac[], int i)
    {
        char ac1[] = new char[ac.length];
        System.arraycopy(ac, 0, ac1, 0, ac.length);
        Arrays.sort(ac1, 0, i);
        return ac1;
    }

    public static float[] sort(float af[])
    {
        return sort(af, af.length);
    }

    public static float[] sort(float af[], int i)
    {
        float af1[] = new float[af.length];
        System.arraycopy(af, 0, af1, 0, af.length);
        Arrays.sort(af1, 0, i);
        return af1;
    }

    public static int[] sort(int ai[])
    {
        return sort(ai, ai.length);
    }

    public static int[] sort(int ai[], int i)
    {
        int ai1[] = new int[ai.length];
        System.arraycopy(ai, 0, ai1, 0, ai.length);
        Arrays.sort(ai1, 0, i);
        return ai1;
    }

    public static String[] sort(String as[])
    {
        return sort(as, as.length);
    }

    public static String[] sort(String as[], int i)
    {
        String as1[] = new String[as.length];
        System.arraycopy(as, 0, as1, 0, as.length);
        Arrays.sort(as1, 0, i);
        return as1;
    }

    public static final Object splice(Object obj, Object obj1, int i)
    {
        int j = Array.getLength(obj);
        if(obj1.getClass().getName().charAt(0) == '[')
        {
            int k = Array.getLength(obj1);
            Object aobj[] = new Object[j + k];
            System.arraycopy(obj, 0, ((Object) (aobj)), 0, i);
            System.arraycopy(obj1, 0, ((Object) (aobj)), i, k);
            System.arraycopy(obj, i, ((Object) (aobj)), k + i, j - i);
            obj = ((Object) (aobj));
        } else
        {
            Object aobj1[] = new Object[j + 1];
            System.arraycopy(obj, 0, ((Object) (aobj1)), 0, i);
            Array.set(((Object) (aobj1)), i, obj1);
            System.arraycopy(obj, i, ((Object) (aobj1)), i + 1, j - i);
            obj = ((Object) (aobj1));
        }
        return obj;
    }

    public static final byte[] splice(byte abyte0[], byte byte0, int i)
    {
        byte abyte1[] = new byte[abyte0.length + 1];
        System.arraycopy(abyte0, 0, abyte1, 0, i);
        abyte1[i] = byte0;
        System.arraycopy(abyte0, i, abyte1, i + 1, abyte0.length - i);
        return abyte1;
    }

    public static final byte[] splice(byte abyte0[], byte abyte1[], int i)
    {
        byte abyte2[] = new byte[abyte0.length + abyte1.length];
        System.arraycopy(abyte0, 0, abyte2, 0, i);
        System.arraycopy(abyte1, 0, abyte2, i, abyte1.length);
        System.arraycopy(abyte0, i, abyte2, abyte1.length + i, abyte0.length - i);
        return abyte2;
    }

    public static final char[] splice(char ac[], char c, int i)
    {
        char ac1[] = new char[ac.length + 1];
        System.arraycopy(ac, 0, ac1, 0, i);
        ac1[i] = c;
        System.arraycopy(ac, i, ac1, i + 1, ac.length - i);
        return ac1;
    }

    public static final char[] splice(char ac[], char ac1[], int i)
    {
        char ac2[] = new char[ac.length + ac1.length];
        System.arraycopy(ac, 0, ac2, 0, i);
        System.arraycopy(ac1, 0, ac2, i, ac1.length);
        System.arraycopy(ac, i, ac2, ac1.length + i, ac.length - i);
        return ac2;
    }

    public static final float[] splice(float af[], float f, int i)
    {
        float af1[] = new float[af.length + 1];
        System.arraycopy(af, 0, af1, 0, i);
        af1[i] = f;
        System.arraycopy(af, i, af1, i + 1, af.length - i);
        return af1;
    }

    public static final float[] splice(float af[], float af1[], int i)
    {
        float af2[] = new float[af.length + af1.length];
        System.arraycopy(af, 0, af2, 0, i);
        System.arraycopy(af1, 0, af2, i, af1.length);
        System.arraycopy(af, i, af2, af1.length + i, af.length - i);
        return af2;
    }

    public static final int[] splice(int ai[], int i, int j)
    {
        int ai1[] = new int[ai.length + 1];
        System.arraycopy(ai, 0, ai1, 0, j);
        ai1[j] = i;
        System.arraycopy(ai, j, ai1, j + 1, ai.length - j);
        return ai1;
    }

    public static final int[] splice(int ai[], int ai1[], int i)
    {
        int ai2[] = new int[ai.length + ai1.length];
        System.arraycopy(ai, 0, ai2, 0, i);
        System.arraycopy(ai1, 0, ai2, i, ai1.length);
        System.arraycopy(ai, i, ai2, ai1.length + i, ai.length - i);
        return ai2;
    }

    public static final String[] splice(String as[], String s, int i)
    {
        String as1[] = new String[as.length + 1];
        System.arraycopy(as, 0, as1, 0, i);
        as1[i] = s;
        System.arraycopy(as, i, as1, i + 1, as.length - i);
        return as1;
    }

    public static final String[] splice(String as[], String as1[], int i)
    {
        String as2[] = new String[as.length + as1.length];
        System.arraycopy(as, 0, as2, 0, i);
        System.arraycopy(as1, 0, as2, i, as1.length);
        System.arraycopy(as, i, as2, as1.length + i, as.length - i);
        return as2;
    }

    public static final boolean[] splice(boolean aflag[], boolean flag, int i)
    {
        boolean aflag1[] = new boolean[aflag.length + 1];
        System.arraycopy(aflag, 0, aflag1, 0, i);
        aflag1[i] = flag;
        System.arraycopy(aflag, i, aflag1, i + 1, aflag.length - i);
        return aflag1;
    }

    public static final boolean[] splice(boolean aflag[], boolean aflag1[], int i)
    {
        boolean aflag2[] = new boolean[aflag.length + aflag1.length];
        System.arraycopy(aflag, 0, aflag2, 0, i);
        System.arraycopy(aflag1, 0, aflag2, i, aflag1.length);
        System.arraycopy(aflag, i, aflag2, aflag1.length + i, aflag.length - i);
        return aflag2;
    }

    public static String[] split(String s, char c)
    {
        boolean flag = false;
        if(s == null)
        {
            s = null;
        } else
        {
            char ac[] = s.toCharArray();
            int j = 0;
            int l;
            int j1;
            for(l = 0; j < ac.length; l = j1)
            {
                j1 = l;
                if(ac[j] == c)
                    j1 = l + 1;
                j++;
            }

            if(l == 0)
            {
                ac = new String[1];
                ac[0] = new String(s);
                s = ac;
            } else
            {
                s = new String[l + 1];
                int l1 = 0;
                int k = 0;
                for(int i1 = ((flag) ? 1 : 0); i1 < ac.length;)
                {
                    int i = l1;
                    int k1 = k;
                    if(ac[i1] == c)
                    {
                        s[k] = new String(ac, l1, i1 - l1);
                        i = i1 + 1;
                        k1 = k + 1;
                    }
                    i1++;
                    l1 = i;
                    k = k1;
                }

                s[k] = new String(ac, l1, ac.length - l1);
            }
        }
        return s;
    }

    public static String[] split(String s, String s1)
    {
        ArrayList arraylist = new ArrayList();
        int i = 0;
        do
        {
            int j = s.indexOf(s1, i);
            if(j != -1)
            {
                arraylist.add(s.substring(i, j));
                i = s1.length() + j;
            } else
            {
                arraylist.add(s.substring(i));
                s = new String[arraylist.size()];
                arraylist.toArray(s);
                return s;
            }
        } while(true);
    }

    public static String[] splitTokens(String s)
    {
        return splitTokens(s, " \t\n\r\f\240");
    }

    public static String[] splitTokens(String s, String s1)
    {
        s = new StringTokenizer(s, s1);
        s1 = new String[s.countTokens()];
        for(int i = 0; s.hasMoreTokens(); i++)
            s1[i] = s.nextToken();

        return s1;
    }

    public static final float sq(float f)
    {
        return f * f;
    }

    public static final float sqrt(float f)
    {
        return (float)Math.sqrt(f);
    }

    public static final String str(byte byte0)
    {
        return String.valueOf(byte0);
    }

    public static final String str(char c)
    {
        return String.valueOf(c);
    }

    public static final String str(float f)
    {
        return String.valueOf(f);
    }

    public static final String str(int i)
    {
        return String.valueOf(i);
    }

    public static final String str(boolean flag)
    {
        return String.valueOf(flag);
    }

    public static final String[] str(byte abyte0[])
    {
        String as[] = new String[abyte0.length];
        for(int i = 0; i < abyte0.length; i++)
            as[i] = String.valueOf(abyte0[i]);

        return as;
    }

    public static final String[] str(char ac[])
    {
        String as[] = new String[ac.length];
        for(int i = 0; i < ac.length; i++)
            as[i] = String.valueOf(ac[i]);

        return as;
    }

    public static final String[] str(float af[])
    {
        String as[] = new String[af.length];
        for(int i = 0; i < af.length; i++)
            as[i] = String.valueOf(af[i]);

        return as;
    }

    public static final String[] str(int ai[])
    {
        String as[] = new String[ai.length];
        for(int i = 0; i < ai.length; i++)
            as[i] = String.valueOf(ai[i]);

        return as;
    }

    public static final String[] str(boolean aflag[])
    {
        String as[] = new String[aflag.length];
        for(int i = 0; i < aflag.length; i++)
            as[i] = String.valueOf(aflag[i]);

        return as;
    }

    public static Object subset(Object obj, int i)
    {
        return subset(obj, i, Array.getLength(obj) - i);
    }

    public static Object subset(Object obj, int i, int j)
    {
        Object obj1 = Array.newInstance(obj.getClass().getComponentType(), j);
        System.arraycopy(obj, i, obj1, 0, j);
        return obj1;
    }

    public static byte[] subset(byte abyte0[], int i)
    {
        return subset(abyte0, i, abyte0.length - i);
    }

    public static byte[] subset(byte abyte0[], int i, int j)
    {
        byte abyte1[] = new byte[j];
        System.arraycopy(abyte0, i, abyte1, 0, j);
        return abyte1;
    }

    public static char[] subset(char ac[], int i)
    {
        return subset(ac, i, ac.length - i);
    }

    public static char[] subset(char ac[], int i, int j)
    {
        char ac1[] = new char[j];
        System.arraycopy(ac, i, ac1, 0, j);
        return ac1;
    }

    public static float[] subset(float af[], int i)
    {
        return subset(af, i, af.length - i);
    }

    public static float[] subset(float af[], int i, int j)
    {
        float af1[] = new float[j];
        System.arraycopy(af, i, af1, 0, j);
        return af1;
    }

    public static int[] subset(int ai[], int i)
    {
        return subset(ai, i, ai.length - i);
    }

    public static int[] subset(int ai[], int i, int j)
    {
        int ai1[] = new int[j];
        System.arraycopy(ai, i, ai1, 0, j);
        return ai1;
    }

    public static String[] subset(String as[], int i)
    {
        return subset(as, i, as.length - i);
    }

    public static String[] subset(String as[], int i, int j)
    {
        String as1[] = new String[j];
        System.arraycopy(as, i, as1, 0, j);
        return as1;
    }

    public static boolean[] subset(boolean aflag[], int i)
    {
        return subset(aflag, i, aflag.length - i);
    }

    public static boolean[] subset(boolean aflag[], int i, int j)
    {
        boolean aflag1[] = new boolean[j];
        System.arraycopy(aflag, i, aflag1, 0, j);
        return aflag1;
    }

    public static final float tan(float f)
    {
        return (float)Math.tan(f);
    }

    private void tellPDE(String s)
    {
        Log.i(activity.getComponentName().getPackageName(), (new StringBuilder()).append("PROCESSING ").append(s).toString());
    }

    public static String trim(String s)
    {
        return s.replace('\240', ' ').trim();
    }

    public static String[] trim(String as[])
    {
        String as1[] = new String[as.length];
        for(int i = 0; i < as.length; i++)
            if(as[i] != null)
                as1[i] = as[i].replace('\240', ' ').trim();

        return as1;
    }

    public static final int unbinary(String s)
    {
        return Integer.parseInt(s, 2);
    }

    public static final int unhex(String s)
    {
        return (int)Long.parseLong(s, 16);
    }

    public static String urlDecode(String s)
    {
        try
        {
            s = URLDecoder.decode(s, "UTF-8");
        }
        // Misplaced declaration of an exception variable
        catch(String s)
        {
            s = null;
        }
        return s;
    }

    public static String urlEncode(String s)
    {
        try
        {
            s = URLEncoder.encode(s, "UTF-8");
        }
        // Misplaced declaration of an exception variable
        catch(String s)
        {
            s = null;
        }
        return s;
    }

    public static int year()
    {
        return Calendar.getInstance().get(1);
    }

    public final float alpha(int i)
    {
        return g.alpha(i);
    }

    public void ambient(float f)
    {
        g.ambient(f);
    }

    public void ambient(float f, float f1, float f2)
    {
        g.ambient(f, f1, f2);
    }

    public void ambient(int i)
    {
        g.ambient(i);
    }

    public void ambientLight(float f, float f1, float f2)
    {
        g.ambientLight(f, f1, f2);
    }

    public void ambientLight(float f, float f1, float f2, float f3, float f4, float f5)
    {
        g.ambientLight(f, f1, f2, f3, f4, f5);
    }

    public void applyMatrix(float f, float f1, float f2, float f3, float f4, float f5)
    {
        g.applyMatrix(f, f1, f2, f3, f4, f5);
    }

    public void applyMatrix(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8, float f9, float f10, float f11, float f12, float f13, 
            float f14, float f15)
    {
        g.applyMatrix(f, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15);
    }

    public void applyMatrix(PMatrix2D pmatrix2d)
    {
        g.applyMatrix(pmatrix2d);
    }

    public void applyMatrix(PMatrix3D pmatrix3d)
    {
        g.applyMatrix(pmatrix3d);
    }

    public void applyMatrix(PMatrix pmatrix)
    {
        g.applyMatrix(pmatrix);
    }

    public void arc(float f, float f1, float f2, float f3, float f4, float f5)
    {
        g.arc(f, f1, f2, f3, f4, f5);
    }

    public void arc(float f, float f1, float f2, float f3, float f4, float f5, int i)
    {
        g.arc(f, f1, f2, f3, f4, f5, i);
    }

    public transient void attrib(String s, float af[])
    {
        g.attrib(s, af);
    }

    public transient void attrib(String s, int ai[])
    {
        g.attrib(s, ai);
    }

    public transient void attrib(String s, boolean aflag[])
    {
        g.attrib(s, aflag);
    }

    public void attribColor(String s, int i)
    {
        g.attribColor(s, i);
    }

    public void attribNormal(String s, float f, float f1, float f2)
    {
        g.attribNormal(s, f, f1, f2);
    }

    public void attribPosition(String s, float f, float f1, float f2)
    {
        g.attribPosition(s, f, f1, f2);
    }

    public void background(float f)
    {
        g.background(f);
    }

    public void background(float f, float f1)
    {
        g.background(f, f1);
    }

    public void background(float f, float f1, float f2)
    {
        g.background(f, f1, f2);
    }

    public void background(float f, float f1, float f2, float f3)
    {
        g.background(f, f1, f2, f3);
    }

    public void background(int i)
    {
        g.background(i);
    }

    public void background(int i, float f)
    {
        g.background(i, f);
    }

    public void background(PImage pimage)
    {
        g.background(pimage);
    }

    public void beginCamera()
    {
        g.beginCamera();
    }

    public void beginContour()
    {
        g.beginContour();
    }

    public PGL beginPGL()
    {
        return g.beginPGL();
    }

    public void beginShape()
    {
        g.beginShape();
    }

    public void beginShape(int i)
    {
        g.beginShape(i);
    }

    public void bezier(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7)
    {
        g.bezier(f, f1, f2, f3, f4, f5, f6, f7);
    }

    public void bezier(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8, float f9, float f10, float f11)
    {
        g.bezier(f, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11);
    }

    public void bezierDetail(int i)
    {
        g.bezierDetail(i);
    }

    public float bezierPoint(float f, float f1, float f2, float f3, float f4)
    {
        return g.bezierPoint(f, f1, f2, f3, f4);
    }

    public float bezierTangent(float f, float f1, float f2, float f3, float f4)
    {
        return g.bezierTangent(f, f1, f2, f3, f4);
    }

    public void bezierVertex(float f, float f1, float f2, float f3, float f4, float f5)
    {
        g.bezierVertex(f, f1, f2, f3, f4, f5);
    }

    public void bezierVertex(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8)
    {
        g.bezierVertex(f, f1, f2, f3, f4, f5, f6, f7, f8);
    }

    public void blend(int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1, int i2)
    {
        g.blend(i, j, k, l, i1, j1, k1, l1, i2);
    }

    public void blend(PImage pimage, int i, int j, int k, int l, int i1, int j1, 
            int k1, int l1, int i2)
    {
        g.blend(pimage, i, j, k, l, i1, j1, k1, l1, i2);
    }

    public void blendMode(int i)
    {
        g.blendMode(i);
    }

    public final float blue(int i)
    {
        return g.blue(i);
    }

    public void box(float f)
    {
        g.box(f);
    }

    public void box(float f, float f1, float f2)
    {
        g.box(f, f1, f2);
    }

    public void breakShape()
    {
        g.breakShape();
    }

    public final float brightness(int i)
    {
        return g.brightness(i);
    }

    public void camera()
    {
        g.camera();
    }

    public void camera(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8)
    {
        g.camera(f, f1, f2, f3, f4, f5, f6, f7, f8);
    }

    public boolean canDraw()
    {
        boolean flag;
        if(g != null && surfaceReady && !paused && (looping || redraw))
            flag = true;
        else
            flag = false;
        return flag;
    }

    public boolean checkPermission(String s)
    {
        boolean flag;
        if(ContextCompat.checkSelfPermission(activity, s) == 0)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public void clear()
    {
        g.clear();
    }

    public void clip(float f, float f1, float f2, float f3)
    {
        g.clip(f, f1, f2, f3);
    }

    public final int color(float f)
    {
        char c = '\377';
        int i;
        if(g == null)
        {
            i = (int)f;
            if(i > 255)
                i = c;
            else
            if(i < 0)
                i = 0;
            i |= 0xff000000 | i << 16 | i << 8;
        } else
        {
            i = g.color(f);
        }
        return i;
    }

    public final int color(float f, float f1)
    {
        int k;
label0:
        {
            if(g != null)
                break label0;
            int i = (int)f;
            int j = (int)f1;
            if(i > 255)
            {
                k = 255;
            } else
            {
                k = i;
                if(i < 0)
                    k = 0;
            }
            if(j > 255 || j >= 0);
            k |= 0xff000000 | k << 16 | k << 8;
        }
_L2:
        return k;
        k = g.color(f, f1);
        if(true) goto _L2; else goto _L1
_L1:
    }

    public final int color(float f, float f1, float f2)
    {
        int i;
        if(g == null)
        {
            float f3;
            if(f > 255F)
            {
                f3 = 255F;
            } else
            {
                f3 = f;
                if(f < 0.0F)
                    f3 = 0.0F;
            }
            if(f1 > 255F)
            {
                f = 255F;
            } else
            {
                f = f1;
                if(f1 < 0.0F)
                    f = 0.0F;
            }
            if(f2 > 255F)
            {
                f1 = 255F;
            } else
            {
                f1 = f2;
                if(f2 < 0.0F)
                    f1 = 0.0F;
            }
            i = 0xff000000 | (int)f3 << 16 | (int)f << 8 | (int)f1;
        } else
        {
            i = g.color(f, f1, f2);
        }
        return i;
    }

    public final int color(float f, float f1, float f2, float f3)
    {
        int i;
        if(g == null)
        {
            float f4;
            if(f3 > 255F)
            {
                f4 = 255F;
            } else
            {
                f4 = f3;
                if(f3 < 0.0F)
                    f4 = 0.0F;
            }
            if(f > 255F)
            {
                f3 = 255F;
            } else
            {
                f3 = f;
                if(f < 0.0F)
                    f3 = 0.0F;
            }
            if(f1 > 255F)
            {
                f = 255F;
            } else
            {
                f = f1;
                if(f1 < 0.0F)
                    f = 0.0F;
            }
            if(f2 > 255F)
            {
                f1 = 255F;
            } else
            {
                f1 = f2;
                if(f2 < 0.0F)
                    f1 = 0.0F;
            }
            i = (int)f4 << 24 | (int)f3 << 16 | (int)f << 8 | (int)f1;
        } else
        {
            i = g.color(f, f1, f2, f3);
        }
        return i;
    }

    public final int color(int i)
    {
        if(g == null)
        {
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
            i = 0xff000000 | j << 16 | j << 8 | j;
        } else
        {
            i = g.color(i);
        }
        return i;
    }

    public final int color(int i, int j)
    {
        if(g == null)
        {
            int k;
            if(j > 255)
            {
                k = 255;
            } else
            {
                k = j;
                if(j < 0)
                    k = 0;
            }
            if(i > 255)
                i = k << 24 | 0xffffff & i;
            else
                i = k << 24 | i << 16 | i << 8 | i;
        } else
        {
            i = g.color(i, j);
        }
        return i;
    }

    public final int color(int i, int j, int k)
    {
        if(g == null)
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
            i = g.color(i, j, k);
        }
        return i;
    }

    public final int color(int i, int j, int k, int l)
    {
        if(g == null)
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
            i = g.color(i, j, k, l);
        }
        return i;
    }

    public void colorMode(int i)
    {
        g.colorMode(i);
    }

    public void colorMode(int i, float f)
    {
        g.colorMode(i, f);
    }

    public void colorMode(int i, float f, float f1, float f2)
    {
        g.colorMode(i, f, f1, f2);
    }

    public void colorMode(int i, float f, float f1, float f2, float f3)
    {
        g.colorMode(i, f, f1, f2, f3);
    }

    public void copy(int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1)
    {
        g.copy(i, j, k, l, i1, j1, k1, l1);
    }

    public void copy(PImage pimage, int i, int j, int k, int l, int i1, int j1, 
            int k1, int l1)
    {
        g.copy(pimage, i, j, k, l, i1, j1, k1, l1);
    }

    protected PFont createDefaultFont(float f)
    {
        return createFont("SansSerif", f, true, null);
    }

    public PFont createFont(String s, float f)
    {
        return createFont(s, f, true, null);
    }

    public PFont createFont(String s, float f, boolean flag)
    {
        return createFont(s, f, flag, null);
    }

    public PFont createFont(String s, float f, boolean flag, char ac[])
    {
        String s1 = s.toLowerCase();
        if(s1.endsWith(".otf") || s1.endsWith(".ttf"))
            s = Typeface.createFromAsset(activity.getAssets(), s);
        else
            s = (Typeface)PFont.findNative(s);
        return new PFont(s, round(f), flag, ac);
    }

    public PGraphics createGraphics(int i, int j)
    {
        return createGraphics(i, j, "processing.core.PGraphicsAndroid2D");
    }

    public PGraphics createGraphics(int i, int j, String s)
    {
        if(!s.equals("processing.core.PGraphicsAndroid2D")) goto _L2; else goto _L1
_L1:
        s = new PGraphicsAndroid2D();
_L4:
        s.setParent(this);
        s.setPrimary(false);
        s.setSize(i, j);
        return s;
_L2:
        if(s.equals("processing.opengl.PGraphics2D"))
        {
            if(!g.isGL())
                throw new RuntimeException("createGraphics() with P2D requires size() to use P2D or P3D");
            s = new PGraphics2D();
            continue; /* Loop/switch isn't completed */
        }
        if(s.equals("processing.opengl.PGraphics3D"))
        {
            if(!g.isGL())
                throw new RuntimeException("createGraphics() with P3D or OPENGL requires size() to use P2D or P3D");
            s = new PGraphics3D();
            continue; /* Loop/switch isn't completed */
        }
        try
        {
            s = getClass().getClassLoader().loadClass(s);
        }
        // Misplaced declaration of an exception variable
        catch(String s)
        {
            throw new RuntimeException("Missing renderer class");
        }
        if(s != null)
        {
            try
            {
                s = s.getConstructor(new Class[0]);
            }
            // Misplaced declaration of an exception variable
            catch(String s)
            {
                throw new RuntimeException("Missing renderer constructor");
            }
            if(s != null)
                try
                {
                    s = (PGraphics)s.newInstance(new Object[0]);
                    continue; /* Loop/switch isn't completed */
                }
                // Misplaced declaration of an exception variable
                catch(String s)
                {
                    s.printStackTrace();
                    throw new RuntimeException(s.getMessage());
                }
                // Misplaced declaration of an exception variable
                catch(String s)
                {
                    s.printStackTrace();
                    throw new RuntimeException(s.getMessage());
                }
                // Misplaced declaration of an exception variable
                catch(String s)
                {
                    s.printStackTrace();
                    throw new RuntimeException(s.getMessage());
                }
                // Misplaced declaration of an exception variable
                catch(String s)
                {
                    s.printStackTrace();
                    s = null;
                    continue; /* Loop/switch isn't completed */
                }
                // Misplaced declaration of an exception variable
                catch(String s)
                {
                    s.printStackTrace();
                }
        }
        s = null;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public PImage createImage(int i, int j, int k)
    {
        PImage pimage = new PImage(i, j, k);
        pimage.parent = this;
        return pimage;
    }

    public InputStream createInput(String s)
    {
        InputStream inputstream = createInputRaw(s);
        if(inputstream != null && s.toLowerCase().endsWith(".gz"))
            try
            {
                s = JVM INSTR new #548 <Class GZIPInputStream>;
                s.GZIPInputStream(inputstream);
            }
            // Misplaced declaration of an exception variable
            catch(String s)
            {
                s.printStackTrace();
                s = null;
            }
        else
            s = inputstream;
        return s;
    }

    public InputStream createInputRaw(String s)
    {
        Object obj;
        if(s == null)
        {
            obj = null;
        } else
        {
label0:
            {
                if(s.length() != 0)
                    break label0;
                obj = null;
            }
        }
_L1:
        return ((InputStream) (obj));
        if(s.indexOf(":") == -1)
            break MISSING_BLOCK_LABEL_83;
        obj = JVM INSTR new #1755 <Class URL>;
        ((URL) (obj)).URL(s);
        obj = (HttpURLConnection)((URL) (obj)).openConnection();
        ((HttpURLConnection) (obj)).setRequestMethod("GET");
        ((HttpURLConnection) (obj)).setDoInput(true);
        ((HttpURLConnection) (obj)).connect();
        obj = ((HttpURLConnection) (obj)).getInputStream();
          goto _L1
        s;
        s.printStackTrace();
        obj = null;
          goto _L1
        obj;
_L8:
        obj = activity.getAssets();
        Object obj2 = ((AssetManager) (obj)).open(s);
        obj = obj2;
        if(obj2 != null) goto _L1; else goto _L2
_L2:
        obj = new File(s);
        if(!((File) (obj)).exists()) goto _L4; else goto _L3
_L3:
        obj2 = JVM INSTR new #538 <Class FileInputStream>;
        ((FileInputStream) (obj2)).FileInputStream(((File) (obj)));
        obj = obj2;
        if(obj2 != null) goto _L1; else goto _L4
_L4:
        obj = new File(sketchPath(s));
        if(!((File) (obj)).exists()) goto _L6; else goto _L5
_L5:
        obj2 = JVM INSTR new #538 <Class FileInputStream>;
        ((FileInputStream) (obj2)).FileInputStream(((File) (obj)));
        obj = obj2;
        if(obj2 != null) goto _L1; else goto _L6
_L6:
        s = activity.openFileInput(s);
        obj = s;
        if(s != null) goto _L1; else goto _L7
_L7:
        obj = null;
          goto _L1
        Object obj1;
        obj1;
          goto _L2
        s;
          goto _L7
        obj1;
          goto _L6
        obj1;
          goto _L4
        obj1;
          goto _L8
    }

    public OutputStream createOutput(String s)
    {
        File file;
        file = JVM INSTR new #543 <Class File>;
        file.File(s);
        Object obj = file;
        if(!file.isAbsolute())
        {
            obj = JVM INSTR new #543 <Class File>;
            ((File) (obj)).File(sketchPath(s));
        }
        s = JVM INSTR new #572 <Class FileOutputStream>;
        s.FileOutputStream(((File) (obj)));
        if(!((File) (obj)).getName().toLowerCase().endsWith(".gz"))
            break MISSING_BLOCK_LABEL_67;
        obj = JVM INSTR new #575 <Class GZIPOutputStream>;
        ((GZIPOutputStream) (obj)).GZIPOutputStream(s);
        s = ((String) (obj));
_L2:
        return s;
        s;
        s.printStackTrace();
        s = null;
        if(true) goto _L2; else goto _L1
_L1:
    }

    public BufferedReader createReader(String s)
    {
        Object obj = null;
        Object obj1 = createInput(s);
        if(obj1 != null) goto _L2; else goto _L1
_L1:
        obj1 = System.err;
        StringBuilder stringbuilder = JVM INSTR new #462 <Class StringBuilder>;
        stringbuilder.StringBuilder();
        ((PrintStream) (obj1)).println(stringbuilder.append(s).append(" does not exist or could not be read").toString());
        s = obj;
_L4:
        return s;
_L2:
        obj1 = createReader(((InputStream) (obj1)));
        s = ((String) (obj1));
        continue; /* Loop/switch isn't completed */
        Exception exception;
        exception;
        if(s == null)
        {
            System.err.println("Filename passed to reader() was null");
            s = obj;
        } else
        {
            System.err.println((new StringBuilder()).append("Couldn't create a reader for ").append(s).toString());
            s = obj;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public PShape createShape()
    {
        return g.createShape();
    }

    public PShape createShape(int i)
    {
        return g.createShape(i);
    }

    public transient PShape createShape(int i, float af[])
    {
        return g.createShape(i, af);
    }

    public Table createTable()
    {
        return new Table();
    }

    public PrintWriter createWriter(String s)
    {
        return createWriter(saveFile(s));
    }

    public XML createXML(String s)
    {
        XML xml;
        xml = JVM INSTR new #1824 <Class XML>;
        xml.XML(s);
        s = xml;
_L2:
        return s;
        s;
        s.printStackTrace();
        s = null;
        if(true) goto _L2; else goto _L1
_L1:
    }

    public void curve(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7)
    {
        g.curve(f, f1, f2, f3, f4, f5, f6, f7);
    }

    public void curve(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8, float f9, float f10, float f11)
    {
        g.curve(f, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11);
    }

    public void curveDetail(int i)
    {
        g.curveDetail(i);
    }

    public float curvePoint(float f, float f1, float f2, float f3, float f4)
    {
        return g.curvePoint(f, f1, f2, f3, f4);
    }

    public float curveTangent(float f, float f1, float f2, float f3, float f4)
    {
        return g.curveTangent(f, f1, f2, f3, f4);
    }

    public void curveTightness(float f)
    {
        g.curveTightness(f);
    }

    public void curveVertex(float f, float f1)
    {
        g.curveVertex(f, f1);
    }

    public void curveVertex(float f, float f1, float f2)
    {
        g.curveVertex(f, f1, f2);
    }

    public File dataFile(String s)
    {
        return new File(dataPath(s));
    }

    public String dataPath(String s)
    {
        if(!(new File(s)).isAbsolute())
            s = (new StringBuilder()).append(sketchPath).append(File.separator).append("data").append(File.separator).append(s).toString();
        return s;
    }

    public void delay(int i)
    {
        long l = i;
        Thread.sleep(l);
_L2:
        return;
        InterruptedException interruptedexception;
        interruptedexception;
        if(true) goto _L2; else goto _L1
_L1:
    }

    protected void dequeueEvents()
    {
        do
        {
            if(!eventQueue.available())
                break;
            Event event = eventQueue.remove();
            switch(event.getFlavor())
            {
            case 1: // '\001'
                handleKeyEvent((KeyEvent)event);
                break;

            case 2: // '\002'
                handleMouseEvent((MouseEvent)event);
                break;
            }
        } while(true);
    }

    public void destroy()
    {
        exit();
    }

    public void die(String s)
    {
        stop();
        throw new RuntimeException(s);
    }

    public void die(String s, Exception exception)
    {
        if(exception != null)
            exception.printStackTrace();
        die(s);
    }

    public void directionalLight(float f, float f1, float f2, float f3, float f4, float f5)
    {
        g.directionalLight(f, f1, f2, f3, f4, f5);
    }

    public boolean displayable()
    {
        return g.displayable();
    }

    public final void dispose()
    {
        finished = true;
        if(thread != null)
        {
            thread = null;
            if(g != null)
                g.dispose();
            if(surfaceView != null)
            {
                surfaceView.getHolder().getSurface().release();
                surfaceView = null;
                activity = null;
            }
            handleMethods("dispose");
        }
    }

    public void draw()
    {
        finished = true;
    }

    public void edge(boolean flag)
    {
        g.edge(flag);
    }

    public void ellipse(float f, float f1, float f2, float f3)
    {
        g.ellipse(f, f1, f2, f3);
    }

    public void ellipseMode(int i)
    {
        g.ellipseMode(i);
    }

    public void emissive(float f)
    {
        g.emissive(f);
    }

    public void emissive(float f, float f1, float f2)
    {
        g.emissive(f, f1, f2);
    }

    public void emissive(int i)
    {
        g.emissive(i);
    }

    public void endCamera()
    {
        g.endCamera();
    }

    public void endContour()
    {
        g.endContour();
    }

    public void endPGL()
    {
        g.endPGL();
    }

    public void endShape()
    {
        g.endShape();
    }

    public void endShape(int i)
    {
        g.endShape(i);
    }

    public void exit()
    {
        if(thread != null) goto _L2; else goto _L1
_L1:
        exit2();
_L4:
        return;
_L2:
        if(looping)
        {
            finished = true;
            exitCalled = true;
        } else
        if(!looping)
        {
            dispose();
            exit2();
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    void exit2()
    {
        System.exit(0);
_L2:
        return;
        SecurityException securityexception;
        securityexception;
        if(true) goto _L2; else goto _L1
_L1:
    }

    public void fill(float f)
    {
        g.fill(f);
    }

    public void fill(float f, float f1)
    {
        g.fill(f, f1);
    }

    public void fill(float f, float f1, float f2)
    {
        g.fill(f, f1, f2);
    }

    public void fill(float f, float f1, float f2, float f3)
    {
        g.fill(f, f1, f2, f3);
    }

    public void fill(int i)
    {
        g.fill(i);
    }

    public void fill(int i, float f)
    {
        g.fill(i, f);
    }

    public void filter(int i)
    {
        g.filter(i);
    }

    public void filter(int i, float f)
    {
        g.filter(i, f);
    }

    public void filter(PShader pshader)
    {
        g.filter(pshader);
    }

    public void flush()
    {
        g.flush();
    }

    public void focusGained()
    {
    }

    public void focusLost()
    {
    }

    public void frameRate(float f)
    {
        frameRateTarget = f;
        frameRatePeriod = (long)(1000000000D / (double)frameRateTarget);
        g.setFrameRate(f);
    }

    public void frustum(float f, float f1, float f2, float f3, float f4, float f5)
    {
        g.frustum(f, f1, f2, f3, f4, f5);
    }

    public void fullScreen()
    {
        if(!fullScreen && insideSettings("fullScreen", new Object[0]))
            fullScreen = true;
    }

    public void fullScreen(int i)
    {
        if(!fullScreen && insideSettings("fullScreen", new Object[] {
    Integer.valueOf(i)
}))
            fullScreen = true;
    }

    public void fullScreen(String s)
    {
        if((!fullScreen || !s.equals(renderer)) && insideSettings("fullScreen", new Object[] {
    s
}))
        {
            fullScreen = true;
            renderer = s;
        }
    }

    public void fullScreen(String s, int i)
    {
        if((!fullScreen || !s.equals(renderer)) && insideSettings("fullScreen", new Object[] {
    s, Integer.valueOf(i)
}))
        {
            fullScreen = true;
            renderer = s;
        }
    }

    public int get(int i, int j)
    {
        return g.get(i, j);
    }

    public PImage get()
    {
        return g.get();
    }

    public PImage get(int i, int j, int k, int l)
    {
        return g.get(i, j, k, l);
    }

    public Object getCache(PImage pimage)
    {
        return g.getCache(pimage);
    }

    public PMatrix2D getMatrix(PMatrix2D pmatrix2d)
    {
        return g.getMatrix(pmatrix2d);
    }

    public PMatrix3D getMatrix(PMatrix3D pmatrix3d)
    {
        return g.getMatrix(pmatrix3d);
    }

    public PMatrix getMatrix()
    {
        return g.getMatrix();
    }

    public Object getNative()
    {
        return g.getNative();
    }

    public PShader getShader(int i)
    {
        return g.getShader(i);
    }

    public SurfaceHolder getSurfaceHolder()
    {
        SurfaceHolder surfaceholder;
        if(surfaceView != null)
            surfaceholder = surfaceView.getHolder();
        else
            surfaceholder = null;
        return surfaceholder;
    }

    public SurfaceView getSurfaceView()
    {
        return surfaceView;
    }

    public final float green(int i)
    {
        return g.green(i);
    }

    public void handleDraw()
    {
        if(surfaceChanged)
        {
            int i = surfaceView.getWidth();
            int j = surfaceView.getHeight();
            if(i != width || j != height)
            {
                width = i;
                height = j;
                g.setSize(width, height);
            }
            surfaceChanged = false;
            surfaceReady = true;
        }
        if(!canDraw()) goto _L2; else goto _L1
_L1:
        g.beginDraw();
        if(!requestedNoLoop) goto _L4; else goto _L3
_L3:
        looping = false;
        requestedNoLoop = false;
        g.endDraw();
_L2:
        return;
_L4:
        long l = System.nanoTime();
        if(frameCount != 0) goto _L6; else goto _L5
_L5:
        setup();
_L8:
        g.endDraw();
        if(frameCount != 0)
            handleMethods("post");
        frameRateLastNanos = l;
        frameCount = frameCount + 1;
        continue; /* Loop/switch isn't completed */
_L6:
        frameRate = ((float)(1000000D / ((double)(l - frameRateLastNanos) / 1000000D)) / 1000F) * 0.1F + frameRate * 0.9F;
        if(frameCount != 0)
            handleMethods("pre");
        pmouseX = dmouseX;
        pmouseY = dmouseY;
        draw();
        dmouseX = mouseX;
        dmouseY = mouseY;
        dequeueEvents();
        handleMethods("draw");
        redraw = false;
        if(true) goto _L8; else goto _L7
_L7:
        RendererChangeException rendererchangeexception;
        rendererchangeexception;
        if(true) goto _L2; else goto _L9
_L9:
    }

    protected void handleKeyEvent(KeyEvent keyevent)
    {
        key = keyevent.getKey();
        keyCode = keyevent.getKeyCode();
        keyevent.getAction();
        JVM INSTR tableswitch 1 2: default 44
    //                   1 60
    //                   2 73;
           goto _L1 _L2 _L3
_L1:
        handleMethods("keyEvent", new Object[] {
            keyevent
        });
        return;
_L2:
        keyPressed = true;
        keyPressed(keyevent);
        continue; /* Loop/switch isn't completed */
_L3:
        keyPressed = false;
        keyReleased(keyevent);
        if(true) goto _L1; else goto _L4
_L4:
    }

    protected void handleMethods(String s)
    {
        s = (RegisteredMethods)registerMap.get(s);
        if(s != null)
            s.handle();
    }

    protected void handleMethods(String s, Object aobj[])
    {
        s = (RegisteredMethods)registerMap.get(s);
        if(s != null)
            s.handle(aobj);
    }

    protected void handleMouseEvent(MouseEvent mouseevent)
    {
        if(mouseevent.getAction() == 4 || mouseevent.getAction() == 5)
        {
            pmouseX = emouseX;
            pmouseY = emouseY;
            mouseX = mouseevent.getX();
            mouseY = mouseevent.getY();
        }
        if(mouseevent.getAction() == 1)
        {
            mouseX = mouseevent.getX();
            mouseY = mouseevent.getY();
            pmouseX = mouseX;
            pmouseY = mouseY;
            dmouseX = mouseX;
            dmouseY = mouseY;
        }
        mouseevent.getAction();
        JVM INSTR tableswitch 1 2: default 132
    //                   1 249
    //                   2 257;
           goto _L1 _L2 _L3
_L1:
        handleMethods("mouseEvent", new Object[] {
            mouseevent
        });
        mouseevent.getAction();
        JVM INSTR tableswitch 1 7: default 192
    //                   1 265
    //                   2 273
    //                   3 281
    //                   4 289
    //                   5 297
    //                   6 305
    //                   7 313;
           goto _L4 _L5 _L6 _L7 _L8 _L9 _L10 _L11
_L4:
        break; /* Loop/switch isn't completed */
_L11:
        break MISSING_BLOCK_LABEL_313;
_L12:
        if(mouseevent.getAction() == 4 || mouseevent.getAction() == 5)
        {
            emouseX = mouseX;
            emouseY = mouseY;
        }
        if(mouseevent.getAction() == 1)
        {
            emouseX = mouseX;
            emouseY = mouseY;
        }
        return;
_L2:
        mousePressed = true;
          goto _L1
_L3:
        mousePressed = false;
          goto _L1
_L5:
        mousePressed(mouseevent);
          goto _L12
_L6:
        mouseReleased(mouseevent);
          goto _L12
_L7:
        mouseClicked(mouseevent);
          goto _L12
_L8:
        mouseDragged(mouseevent);
          goto _L12
_L9:
        mouseMoved(mouseevent);
          goto _L12
_L10:
        mouseEntered(mouseevent);
          goto _L12
        mouseExited(mouseevent);
          goto _L12
    }

    void handleSettings()
    {
        insideSettings = true;
        settings();
        insideSettings = false;
    }

    public void hint(int i)
    {
        g.hint(i);
    }

    public final float hue(int i)
    {
        return g.hue(i);
    }

    public void image(PImage pimage, float f, float f1)
    {
        g.image(pimage, f, f1);
    }

    public void image(PImage pimage, float f, float f1, float f2, float f3)
    {
        g.image(pimage, f, f1, f2, f3);
    }

    public void image(PImage pimage, float f, float f1, float f2, float f3, int i, int j, 
            int k, int l)
    {
        g.image(pimage, f, f1, f2, f3, i, j, k, l);
    }

    public void imageMode(int i)
    {
        g.imageMode(i);
    }

    protected String insertFrame(String s)
    {
        int i = s.indexOf('#');
        int j = s.lastIndexOf('#');
        String s1 = s;
        if(i != -1)
        {
            s1 = s;
            if(j - i > 0)
            {
                s1 = s.substring(0, i);
                s = s.substring(j + 1);
                s1 = (new StringBuilder()).append(s1).append(nf(frameCount, (j - i) + 1)).append(s).toString();
            }
        }
        return s1;
    }

    transient boolean insideSettings(String s, Object aobj[])
    {
        if(insideSettings)
            return true;
        String s1 = (new StringBuilder()).append("https://processing.org/reference/").append(s).append("_.html").toString();
        if(!external)
        {
            aobj = new StringList(aobj);
            System.err.println((new StringBuilder()).append("When not using the PDE, ").append(s).append("() can only be used inside settings().").toString());
            System.err.println((new StringBuilder()).append("Remove the ").append(s).append("() method from setup(), and add the following:").toString());
            System.err.println("public void settings() {");
            System.err.println((new StringBuilder()).append("  ").append(s).append("(").append(((StringList) (aobj)).join(", ")).append(");").toString());
            System.err.println("}");
        }
        throw new IllegalStateException((new StringBuilder()).append(s).append("() cannot be used here, see ").append(s1).toString());
    }

    public boolean isGL()
    {
        return g.isGL();
    }

    public boolean isLooping()
    {
        return looping;
    }

    public void keyPressed()
    {
    }

    public void keyPressed(KeyEvent keyevent)
    {
        keyPressed();
    }

    public void keyReleased()
    {
    }

    public void keyReleased(KeyEvent keyevent)
    {
        keyReleased();
    }

    public void keyTyped()
    {
    }

    public void keyTyped(KeyEvent keyevent)
    {
        keyTyped();
    }

    public int lerpColor(int i, int j, float f)
    {
        return g.lerpColor(i, j, f);
    }

    public void lightFalloff(float f, float f1, float f2)
    {
        g.lightFalloff(f, f1, f2);
    }

    public void lightSpecular(float f, float f1, float f2)
    {
        g.lightSpecular(f, f1, f2);
    }

    public void lights()
    {
        g.lights();
    }

    public void line(float f, float f1, float f2, float f3)
    {
        g.line(f, f1, f2, f3);
    }

    public void line(float f, float f1, float f2, float f3, float f4, float f5)
    {
        g.line(f, f1, f2, f3, f4, f5);
    }

    public void link(String s)
    {
        link(s, null);
    }

    public void link(String s, String s1)
    {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(s)));
    }

    public byte[] loadBytes(String s)
    {
        InputStream inputstream = createInput(s);
        if(inputstream != null)
        {
            s = loadBytes(inputstream);
        } else
        {
            System.err.println((new StringBuilder()).append("The file \"").append(s).append("\" is missing or inaccessible, make sure the URL is valid or that the file has been added to your sketch and is readable.").toString());
            s = null;
        }
        return s;
    }

    public PFont loadFont(String s)
    {
        PFont pfont;
        InputStream inputstream = createInput(s);
        pfont = JVM INSTR new #1643 <Class PFont>;
        pfont.PFont(inputstream);
        s = pfont;
_L2:
        return s;
        Exception exception;
        exception;
        die((new StringBuilder()).append("Could not load font ").append(s).append(". Make sure that the font has been copied to the data folder of your sketch.").toString(), exception);
        s = null;
        if(true) goto _L2; else goto _L1
_L1:
    }

    public PImage loadImage(String s)
    {
        InputStream inputstream = createInput(s);
        if(inputstream != null) goto _L2; else goto _L1
_L1:
        System.err.println((new StringBuilder()).append("Could not find the image ").append(s).append(".").toString());
        s = null;
_L4:
        return s;
_L2:
        s = BitmapFactory.decodeStream(inputstream);
        try
        {
            inputstream.close();
        }
        catch(IOException ioexception) { }
        s = new PImage(s);
        s.parent = this;
        if(true) goto _L4; else goto _L3
_L3:
        s;
        try
        {
            inputstream.close();
        }
        catch(IOException ioexception1) { }
        throw s;
    }

    public JSONArray loadJSONArray(String s)
    {
        return new JSONArray(createReader(s));
    }

    public JSONObject loadJSONObject(String s)
    {
        return new JSONObject(createReader(s));
    }

    public void loadPixels()
    {
        g.loadPixels();
        pixels = g.pixels;
    }

    public PShader loadShader(String s)
    {
        return g.loadShader(s);
    }

    public PShader loadShader(String s, String s1)
    {
        return g.loadShader(s, s1);
    }

    public PShape loadShape(String s)
    {
        return g.loadShape(s);
    }

    public String[] loadStrings(String s)
    {
        InputStream inputstream = createInput(s);
        if(inputstream != null)
        {
            s = loadStrings(inputstream);
        } else
        {
            System.err.println((new StringBuilder()).append("The file \"").append(s).append("\" is missing or inaccessible, make sure the URL is valid or that the file has been added to your sketch and is readable.").toString());
            s = null;
        }
        return s;
    }

    public Table loadTable(String s)
    {
        return loadTable(s, null);
    }

    public Table loadTable(String s, String s1)
    {
        String s2 = checkExtension(s);
        Object obj = s1;
        if(s2 == null) goto _L2; else goto _L1
_L1:
        if(s2.equals("csv")) goto _L4; else goto _L3
_L3:
        obj = s1;
        if(!s2.equals("tsv")) goto _L2; else goto _L4
_L4:
        if(s1 != null) goto _L6; else goto _L5
_L5:
        obj = s2;
_L2:
        s1 = JVM INSTR new #1812 <Class Table>;
        s1.Table(createInput(s), ((String) (obj)));
        s = s1;
_L7:
        return s;
_L6:
        obj = JVM INSTR new #462 <Class StringBuilder>;
        ((StringBuilder) (obj)).StringBuilder();
        obj = ((StringBuilder) (obj)).append(s2).append(",").append(s1).toString();
          goto _L2
        s;
        s.printStackTrace();
        s = null;
          goto _L7
    }

    public XML loadXML(String s)
    {
        return loadXML(s, null);
    }

    public XML loadXML(String s, String s1)
    {
        XML xml;
        xml = JVM INSTR new #1824 <Class XML>;
        xml.XML(createInput(s), s1);
        s = xml;
_L2:
        return s;
        s;
        s.printStackTrace();
        s = null;
        if(true) goto _L2; else goto _L1
_L1:
    }

    public void loop()
    {
        this;
        JVM INSTR monitorenter ;
        if(!looping)
            looping = true;
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        throw exception;
    }

    public void mask(PImage pimage)
    {
        g.mask(pimage);
    }

    public void mask(int ai[])
    {
        g.mask(ai);
    }

    public void method(String s)
    {
        getClass().getMethod(s, new Class[0]).invoke(this, new Object[0]);
_L1:
        return;
        s;
        s.printStackTrace();
          goto _L1
        s;
        s.printStackTrace();
          goto _L1
        s;
        s.getTargetException().printStackTrace();
          goto _L1
        NoSuchMethodException nosuchmethodexception;
        nosuchmethodexception;
        System.err.println((new StringBuilder()).append("There is no public ").append(s).append("() method in the class ").append(getClass().getName()).toString());
          goto _L1
        s;
        s.printStackTrace();
          goto _L1
    }

    public int millis()
    {
        return (int)(System.currentTimeMillis() - millisOffset);
    }

    public float modelX(float f, float f1, float f2)
    {
        return g.modelX(f, f1, f2);
    }

    public float modelY(float f, float f1, float f2)
    {
        return g.modelY(f, f1, f2);
    }

    public float modelZ(float f, float f1, float f2)
    {
        return g.modelZ(f, f1, f2);
    }

    public void mouseClicked()
    {
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
        mouseClicked();
    }

    public void mouseDragged()
    {
    }

    public void mouseDragged(MouseEvent mouseevent)
    {
        mouseDragged();
    }

    public void mouseEntered()
    {
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
        mouseEntered();
    }

    public void mouseExited()
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
        mouseExited();
    }

    public void mouseMoved()
    {
    }

    public void mouseMoved(MouseEvent mouseevent)
    {
        mouseMoved();
    }

    public void mousePressed()
    {
    }

    public void mousePressed(MouseEvent mouseevent)
    {
        mousePressed();
    }

    public void mouseReleased()
    {
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
        mouseReleased();
    }

    protected void nativeKeyEvent(android.view.KeyEvent keyevent)
    {
        int i;
        int l;
label0:
        {
            i = 1;
            int j = (char)keyevent.getUnicodeChar();
            if(j != 0)
            {
                l = j;
                if(j != '\uFFFF')
                    break label0;
            }
            j = 65535;
            l = j;
        }
        int k = keyevent.getKeyCode();
        int i1 = keyevent.getAction();
        if(i1 != 0)
            if(i1 == 1)
                i = 2;
            else
                i = 0;
        postEvent(new KeyEvent(keyevent, keyevent.getEventTime(), i, 0, l, k));
    }

    protected void nativeMotionEvent(MotionEvent motionevent)
    {
        int i1;
        int i = motionevent.getMetaState();
        int j = 0;
        if((i & 1) != 0)
            j = 1;
        i1 = j;
        if((i & 0x1000) != 0)
            i1 = j | 2;
        j = i1;
        if((0x10000 & i) != 0)
            j = i1 | 4;
        i1 = j;
        if((i & 2) != 0)
            i1 = j | 8;
        motionevent.getAction();
        JVM INSTR tableswitch 0 2: default 92
    //                   0 93
    //                   1 188
    //                   2 137;
           goto _L1 _L2 _L3 _L4
_L1:
        return;
_L2:
        motionPointerId = motionevent.getPointerId(0);
        postEvent(new MouseEvent(motionevent, motionevent.getEventTime(), 1, i1, (int)motionevent.getX(), (int)motionevent.getY(), 21, 1));
        continue; /* Loop/switch isn't completed */
_L4:
        int k = motionevent.findPointerIndex(motionPointerId);
        if(k != -1)
            postEvent(new MouseEvent(motionevent, motionevent.getEventTime(), 4, i1, (int)motionevent.getX(k), (int)motionevent.getY(k), 21, 1));
        continue; /* Loop/switch isn't completed */
_L3:
        int l = motionevent.findPointerIndex(motionPointerId);
        if(l != -1)
            postEvent(new MouseEvent(motionevent, motionevent.getEventTime(), 2, i1, (int)motionevent.getX(l), (int)motionevent.getY(l), 21, 1));
        if(true) goto _L1; else goto _L5
_L5:
    }

    public void noClip()
    {
        g.noClip();
    }

    public void noFill()
    {
        g.noFill();
    }

    public void noLights()
    {
        g.noLights();
    }

    public void noLoop()
    {
        this;
        JVM INSTR monitorenter ;
        if(looping)
        {
            if(!(g instanceof PGraphicsOpenGL))
                break MISSING_BLOCK_LABEL_27;
            requestedNoLoop = true;
        }
_L1:
        this;
        JVM INSTR monitorexit ;
        return;
        looping = false;
          goto _L1
        Exception exception;
        exception;
        throw exception;
    }

    public void noSmooth()
    {
        if(!insideSettings) goto _L2; else goto _L1
_L1:
        smooth = 0;
_L4:
        return;
_L2:
        if(smooth != 0)
            smoothWarning("noSmooth");
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void noStroke()
    {
        g.noStroke();
    }

    public void noTexture()
    {
        g.noTexture();
    }

    public void noTint()
    {
        g.noTint();
    }

    public float noise(float f)
    {
        return noise(f, 0.0F, 0.0F);
    }

    public float noise(float f, float f1)
    {
        return noise(f, f1, 0.0F);
    }

    public float noise(float f, float f1, float f2)
    {
        if(perlin == null)
        {
            if(perlinRandom == null)
                perlinRandom = new Random();
            perlin = new float[4096];
            for(int i = 0; i < 4096; i++)
                perlin[i] = perlinRandom.nextFloat();

            perlin_cosTable = PGraphics.cosLUT;
            perlin_PI = 720;
            perlin_TWOPI = 720;
            perlin_PI = perlin_PI >> 1;
        }
        float f3 = f;
        if(f < 0.0F)
            f3 = -f;
        f = f1;
        if(f1 < 0.0F)
            f = -f1;
        f1 = f2;
        if(f2 < 0.0F)
            f1 = -f2;
        int j = (int)f3;
        int k = (int)f;
        int l = (int)f1;
        float f4 = f3 - (float)j;
        f2 = f - (float)k;
        float f6 = f1 - (float)l;
        float f8 = 0.0F;
        f3 = 0.5F;
        int i1 = 0;
        f = f4;
        f1 = f2;
        f2 = f6;
        for(; i1 < perlin_octaves; i1++)
        {
            int j1 = (k << 4) + j + (l << 8);
            float f7 = noise_fsc(f);
            float f5 = noise_fsc(f1);
            float f9 = perlin[j1 & 0xfff];
            f9 += (perlin[j1 + 1 & 0xfff] - f9) * f7;
            float f10 = perlin[j1 + 16 & 0xfff];
            f9 += ((f10 + (perlin[j1 + 16 + 1 & 0xfff] - f10) * f7) - f9) * f5;
            j1 += 256;
            f10 = perlin[j1 & 0xfff];
            f10 += (perlin[j1 + 1 & 0xfff] - f10) * f7;
            float f11 = perlin[j1 + 16 & 0xfff];
            f8 += ((((((perlin[j1 + 16 + 1 & 0xfff] - f11) * f7 + f11) - f10) * f5 + f10) - f9) * noise_fsc(f2) + f9) * f3;
            f3 *= perlin_amp_falloff;
            int k1 = j << 1;
            f9 = f * 2.0F;
            int l1 = k << 1;
            f7 = f1 * 2.0F;
            j1 = l << 1;
            f5 = f2 * 2.0F;
            f = f9;
            j = k1;
            if(f9 >= 1.0F)
            {
                j = k1 + 1;
                f = f9 - 1.0F;
            }
            f1 = f7;
            k = l1;
            if(f7 >= 1.0F)
            {
                k = l1 + 1;
                f1 = f7 - 1.0F;
            }
            f2 = f5;
            l = j1;
            if(f5 >= 1.0F)
            {
                l = j1 + 1;
                f2 = f5 - 1.0F;
            }
        }

        return f8;
    }

    public void noiseDetail(int i)
    {
        if(i > 0)
            perlin_octaves = i;
    }

    public void noiseDetail(int i, float f)
    {
        if(i > 0)
            perlin_octaves = i;
        if(f > 0.0F)
            perlin_amp_falloff = f;
    }

    public void noiseSeed(long l)
    {
        if(perlinRandom == null)
            perlinRandom = new Random();
        perlinRandom.setSeed(l);
        perlin = null;
    }

    public void normal(float f, float f1, float f2)
    {
        g.normal(f, f1, f2);
    }

    public void onBackPressed()
    {
        exit();
    }

    public void onConfigurationChanged(Configuration configuration)
    {
        super.onConfigurationChanged(configuration);
    }

    public View onCreateView(LayoutInflater layoutinflater, ViewGroup viewgroup, Bundle bundle)
    {
        activity = getActivity();
        layoutinflater = new Point();
        viewgroup = ((WindowManager)activity.getSystemService("window")).getDefaultDisplay();
        if(SDK >= 17)
            viewgroup.getRealSize(layoutinflater);
        else
        if(SDK >= 14)
            try
            {
                layoutinflater.x = ((Integer)android/view/Display.getMethod("getRawWidth", new Class[0]).invoke(viewgroup, new Object[0])).intValue();
                layoutinflater.y = ((Integer)android/view/Display.getMethod("getRawHeight", new Class[0]).invoke(viewgroup, new Object[0])).intValue();
            }
            // Misplaced declaration of an exception variable
            catch(Bundle bundle)
            {
                viewgroup.getSize(layoutinflater);
            }
        displayWidth = ((Point) (layoutinflater)).x;
        displayHeight = ((Point) (layoutinflater)).y;
        handleSettings();
        if(fullScreen)
        {
            width = displayWidth;
            height = displayHeight;
        }
        layoutinflater = sketchRenderer();
        try
        {
            viewgroup = Class.forName(layoutinflater);
        }
        // Misplaced declaration of an exception variable
        catch(ViewGroup viewgroup)
        {
            throw new RuntimeException(String.format("Error: Could not resolve renderer class name: %s", new Object[] {
                layoutinflater
            }), viewgroup);
        }
        if(layoutinflater.equals("processing.core.PGraphicsAndroid2D"))
            surfaceView = new SketchSurfaceView(activity, width, height, viewgroup);
        else
        if(processing/opengl/PGraphicsOpenGL.isAssignableFrom(viewgroup))
            surfaceView = new SketchSurfaceViewGL(activity, width, height, viewgroup);
        else
            throw new RuntimeException(String.format("Error: Unsupported renderer class: %s", new Object[] {
                layoutinflater
            }));
        setFullScreenVisibility();
        if(smooth == 0)
            g.noSmooth();
        else
            g.smooth(smooth);
        if(width == displayWidth && height == displayHeight)
        {
            layoutinflater = surfaceView;
        } else
        {
            layoutinflater = new RelativeLayout(activity);
            bundle = new android.widget.RelativeLayout.LayoutParams(-2, -2);
            bundle.addRule(13);
            viewgroup = new LinearLayout(activity);
            viewgroup.addView(surfaceView, sketchWidth(), sketchHeight());
            layoutinflater.addView(viewgroup, bundle);
            layoutinflater.setBackgroundColor(sketchWindowColor());
        }
        finished = false;
        looping = true;
        redraw = true;
        sketchPath = activity.getFilesDir().getAbsolutePath();
        handler = new Handler();
        start();
        return layoutinflater;
    }

    public void onDestroy()
    {
        super.onDestroy();
        dispose();
    }

    public void onPause()
    {
        super.onPause();
        setFullScreenVisibility();
        paused = true;
        handleMethods("pause");
        pause();
    }

    public void onPermissionsGranted()
    {
    }

    public void onResume()
    {
        super.onResume();
        setFullScreenVisibility();
        paused = false;
        handleMethods("resume");
        resume();
    }

    public void onStart()
    {
        tellPDE("onStart");
        super.onStart();
    }

    public void onStop()
    {
        tellPDE("onStop");
        super.onStop();
    }

    public void orientation(int i)
    {
        if(i != 1) goto _L2; else goto _L1
_L1:
        activity.setRequestedOrientation(1);
_L4:
        return;
_L2:
        if(i == 2)
            activity.setRequestedOrientation(0);
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void ortho()
    {
        g.ortho();
    }

    public void ortho(float f, float f1, float f2, float f3)
    {
        g.ortho(f, f1, f2, f3);
    }

    public void ortho(float f, float f1, float f2, float f3, float f4, float f5)
    {
        g.ortho(f, f1, f2, f3, f4, f5);
    }

    public JSONArray parseJSONArray(String s)
    {
        return new JSONArray(new StringReader(s));
    }

    public JSONObject parseJSONObject(String s)
    {
        return new JSONObject(new StringReader(s));
    }

    public XML parseXML(String s)
    {
        return parseXML(s, null);
    }

    public XML parseXML(String s, String s1)
    {
        try
        {
            s = XML.parse(s, s1);
        }
        // Misplaced declaration of an exception variable
        catch(String s)
        {
            s.printStackTrace();
            s = null;
        }
        return s;
    }

    public void pause()
    {
    }

    public void perspective()
    {
        g.perspective();
    }

    public void perspective(float f, float f1, float f2, float f3)
    {
        g.perspective(f, f1, f2, f3);
    }

    public void point(float f, float f1)
    {
        g.point(f, f1);
    }

    public void point(float f, float f1, float f2)
    {
        g.point(f, f1, f2);
    }

    public void pointLight(float f, float f1, float f2, float f3, float f4, float f5)
    {
        g.pointLight(f, f1, f2, f3, f4, f5);
    }

    public void popMatrix()
    {
        g.popMatrix();
    }

    public void popStyle()
    {
        g.popStyle();
    }

    public void postEvent(Event event)
    {
        eventQueue.add(event);
        if(!looping)
            dequeueEvents();
    }

    public void printCamera()
    {
        g.printCamera();
    }

    public void printMatrix()
    {
        g.printMatrix();
    }

    public void printProjection()
    {
        g.printProjection();
    }

    public void pushMatrix()
    {
        g.pushMatrix();
    }

    public void pushStyle()
    {
        g.pushStyle();
    }

    public void quad(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7)
    {
        g.quad(f, f1, f2, f3, f4, f5, f6, f7);
    }

    public void quadraticVertex(float f, float f1, float f2, float f3)
    {
        g.quadraticVertex(f, f1, f2, f3);
    }

    public void quadraticVertex(float f, float f1, float f2, float f3, float f4, float f5)
    {
        g.quadraticVertex(f, f1, f2, f3, f4, f5);
    }

    public final float random(float f)
    {
        float f1 = 0.0F;
        if(f == 0.0F)
        {
            f = f1;
        } else
        {
            if(internalRandom == null)
                internalRandom = new Random();
            float f2;
            do
                f2 = internalRandom.nextFloat() * f;
            while(f2 == f);
            f = f2;
        }
        return f;
    }

    public final float random(float f, float f1)
    {
        if(f < f1)
            f += random(f1 - f);
        return f;
    }

    public final void randomSeed(long l)
    {
        if(internalRandom == null)
            internalRandom = new Random();
        internalRandom.setSeed(l);
    }

    public void rect(float f, float f1, float f2, float f3)
    {
        g.rect(f, f1, f2, f3);
    }

    public void rect(float f, float f1, float f2, float f3, float f4)
    {
        g.rect(f, f1, f2, f3, f4);
    }

    public void rect(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7)
    {
        g.rect(f, f1, f2, f3, f4, f5, f6, f7);
    }

    public void rectMode(int i)
    {
        g.rectMode(i);
    }

    public final float red(int i)
    {
        return g.red(i);
    }

    public void redraw()
    {
        this;
        JVM INSTR monitorenter ;
        if(!looping)
            redraw = true;
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        throw exception;
    }

    public void registerDispose(Object obj)
    {
        registerNoArgs("dispose", obj);
    }

    public void registerDraw(Object obj)
    {
        registerNoArgs("draw", obj);
    }

    public void registerMethod(String s, Object obj)
    {
        if(s.equals("mouseEvent"))
            registerWithArgs("mouseEvent", obj, new Class[] {
                processing/event/MouseEvent
            });
        else
        if(s.equals("keyEvent"))
            registerWithArgs("keyEvent", obj, new Class[] {
                processing/event/KeyEvent
            });
        else
        if(s.equals("touchEvent"))
            registerWithArgs("touchEvent", obj, new Class[] {
                processing/event/TouchEvent
            });
        else
            registerNoArgs(s, obj);
    }

    public void registerPost(Object obj)
    {
        registerNoArgs("post", obj);
    }

    public void registerPre(Object obj)
    {
        registerNoArgs("pre", obj);
    }

    public void registerSize(Object obj)
    {
        System.err.println("The registerSize() command is no longer supported.");
    }

    public void removeCache(PImage pimage)
    {
        g.removeCache(pimage);
    }

    public PImage requestImage(String s)
    {
        PImage pimage = createImage(0, 0, 2);
        (new AsyncImageLoader(s, pimage)).start();
        return pimage;
    }

    public void resetMatrix()
    {
        g.resetMatrix();
    }

    public void resetShader()
    {
        g.resetShader();
    }

    public void resetShader(int i)
    {
        g.resetShader(i);
    }

    public void resume()
    {
    }

    public void rotate(float f)
    {
        g.rotate(f);
    }

    public void rotate(float f, float f1, float f2, float f3)
    {
        g.rotate(f, f1, f2, f3);
    }

    public void rotateX(float f)
    {
        g.rotateX(f);
    }

    public void rotateY(float f)
    {
        g.rotateY(f);
    }

    public void rotateZ(float f)
    {
        g.rotateZ(f);
    }

    public void run()
    {
        long l = System.nanoTime();
        long l1 = 0L;
        while(Thread.currentThread() == thread && !finished) 
        {
            while(paused) 
                try
                {
                    Thread.sleep(100L);
                }
                catch(InterruptedException interruptedexception) { }
            if(g != null)
                g.requestDraw();
            long l2 = System.nanoTime();
            l1 = frameRatePeriod - (l2 - l) - l1;
            if(l1 > 0L)
            {
                try
                {
                    Thread.sleep(l1 / 0xf4240L, (int)(l1 % 0xf4240L));
                }
                catch(InterruptedException interruptedexception1) { }
                l1 = System.nanoTime() - l2 - l1;
            } else
            {
                l1 = 0L;
            }
            l = System.nanoTime();
        }
        if(!paused)
        {
            stop();
            if(exitCalled)
                exit2();
        }
    }

    public final float saturation(int i)
    {
        return g.saturation(i);
    }

    public void save(String s)
    {
        g.save(savePath(s));
    }

    public void saveBytes(String s, byte abyte0[])
    {
        saveBytes(saveFile(s), abyte0);
    }

    public File saveFile(String s)
    {
        return new File(savePath(s));
    }

    public void saveFrame()
    {
        PGraphics pgraphics = g;
        StringBuilder stringbuilder = JVM INSTR new #462 <Class StringBuilder>;
        stringbuilder.StringBuilder();
        pgraphics.save(savePath(stringbuilder.append("screen-").append(nf(frameCount, 4)).append(".tif").toString()));
_L1:
        return;
        SecurityException securityexception;
        securityexception;
        System.err.println("Can't use saveFrame() when running in a browser, unless using a signed applet.");
          goto _L1
    }

    public void saveFrame(String s)
    {
        g.save(savePath(insertFrame(s)));
_L1:
        return;
        s;
        System.err.println("Can't use saveFrame() when running in a browser, unless using a signed applet.");
          goto _L1
    }

    public boolean saveJSONArray(JSONArray jsonarray, String s)
    {
        return saveJSONArray(jsonarray, s, null);
    }

    public boolean saveJSONArray(JSONArray jsonarray, String s, String s1)
    {
        return jsonarray.save(saveFile(s), s1);
    }

    public boolean saveJSONObject(JSONObject jsonobject, String s)
    {
        return saveJSONObject(jsonobject, s, null);
    }

    public boolean saveJSONObject(JSONObject jsonobject, String s, String s1)
    {
        return jsonobject.save(saveFile(s), s1);
    }

    public String savePath(String s)
    {
        if(s == null)
        {
            s = null;
        } else
        {
            s = sketchPath(s);
            createPath(s);
        }
        return s;
    }

    public boolean saveStream(File file, String s)
    {
        return saveStream(file, createInputRaw(s));
    }

    public boolean saveStream(String s, InputStream inputstream)
    {
        return saveStream(saveFile(s), inputstream);
    }

    public boolean saveStream(String s, String s1)
    {
        return saveStream(saveFile(s), s1);
    }

    public void saveStrings(String s, String as[])
    {
        saveStrings(saveFile(s), as);
    }

    public boolean saveTable(Table table, String s)
    {
        return saveTable(table, s, null);
    }

    public boolean saveTable(Table table, String s, String s1)
    {
        table.save(saveFile(s), s1);
        boolean flag = true;
_L2:
        return flag;
        table;
        table.printStackTrace();
        flag = false;
        if(true) goto _L2; else goto _L1
_L1:
    }

    public boolean saveXML(XML xml, String s)
    {
        return saveXML(xml, s, null);
    }

    public boolean saveXML(XML xml, String s, String s1)
    {
        return xml.save(saveFile(s), s1);
    }

    public void scale(float f)
    {
        g.scale(f);
    }

    public void scale(float f, float f1)
    {
        g.scale(f, f1);
    }

    public void scale(float f, float f1, float f2)
    {
        g.scale(f, f1, f2);
    }

    public float screenX(float f, float f1)
    {
        return g.screenX(f, f1);
    }

    public float screenX(float f, float f1, float f2)
    {
        return g.screenX(f, f1, f2);
    }

    public float screenY(float f, float f1)
    {
        return g.screenY(f, f1);
    }

    public float screenY(float f, float f1, float f2)
    {
        return g.screenY(f, f1, f2);
    }

    public float screenZ(float f, float f1, float f2)
    {
        return g.screenZ(f, f1, f2);
    }

    public void set(int i, int j, int k)
    {
        g.set(i, j, k);
    }

    public void set(int i, int j, PImage pimage)
    {
        g.set(i, j, pimage);
    }

    public void setCache(PImage pimage, Object obj)
    {
        g.setCache(pimage, obj);
    }

    public void setMatrix(PMatrix2D pmatrix2d)
    {
        g.setMatrix(pmatrix2d);
    }

    public void setMatrix(PMatrix3D pmatrix3d)
    {
        g.setMatrix(pmatrix3d);
    }

    public void setMatrix(PMatrix pmatrix)
    {
        g.setMatrix(pmatrix);
    }

    public void settings()
    {
    }

    public void setup()
    {
    }

    public void shader(PShader pshader)
    {
        g.shader(pshader);
    }

    public void shader(PShader pshader, int i)
    {
        g.shader(pshader, i);
    }

    public void shape(PShape pshape)
    {
        g.shape(pshape);
    }

    public void shape(PShape pshape, float f, float f1)
    {
        g.shape(pshape, f, f1);
    }

    public void shape(PShape pshape, float f, float f1, float f2, float f3)
    {
        g.shape(pshape, f, f1, f2, f3);
    }

    public void shapeMode(int i)
    {
        g.shapeMode(i);
    }

    public void shearX(float f)
    {
        g.shearX(f);
    }

    public void shearY(float f)
    {
        g.shearY(f);
    }

    public void shininess(float f)
    {
        g.shininess(f);
    }

    public void size(int i, int j)
    {
        if((i != width || j != height) && insideSettings("size", new Object[] {
    Integer.valueOf(i), Integer.valueOf(j)
}))
        {
            width = i;
            height = j;
        }
    }

    public void size(int i, int j, String s)
    {
        if((i != width || j != height || !renderer.equals(s)) && insideSettings("size", new Object[] {
    Integer.valueOf(i), Integer.valueOf(j), s
}))
        {
            width = i;
            height = j;
            renderer = s;
        }
    }

    public void size(int i, int j, String s, String s1)
    {
        if((i != width || j != height || !renderer.equals(s)) && insideSettings("size", new Object[] {
    Integer.valueOf(i), Integer.valueOf(j), s, s1
}))
        {
            width = i;
            height = j;
            renderer = s;
        }
    }

    public File sketchFile(String s)
    {
        return new File(sketchPath(s));
    }

    public final int sketchHeight()
    {
        int i;
        if(fullScreen)
            i = displayHeight;
        else
            i = height;
        return i;
    }

    public String sketchPath(String s)
    {
        if(sketchPath != null) goto _L2; else goto _L1
_L1:
        Object obj = s;
_L4:
        return ((String) (obj));
_L2:
        boolean flag;
        obj = JVM INSTR new #543 <Class File>;
        ((File) (obj)).File(s);
        flag = ((File) (obj)).isAbsolute();
        obj = s;
        if(flag)
            continue; /* Loop/switch isn't completed */
_L5:
        obj = activity.getFileStreamPath(s).getAbsolutePath();
        if(true) goto _L4; else goto _L3
_L3:
        Exception exception;
        exception;
          goto _L5
    }

    public int sketchQuality()
    {
        return 1;
    }

    public final String sketchRenderer()
    {
        return renderer;
    }

    public final int sketchWidth()
    {
        int i;
        if(fullScreen)
            i = displayWidth;
        else
            i = width;
        return i;
    }

    public final int sketchWindowColor()
    {
        return windowColor;
    }

    public void smooth()
    {
        smooth(1);
    }

    public void smooth(int i)
    {
        if(!insideSettings) goto _L2; else goto _L1
_L1:
        smooth = i;
_L4:
        return;
_L2:
        if(smooth != i)
            smoothWarning("smooth");
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void specular(float f)
    {
        g.specular(f);
    }

    public void specular(float f, float f1, float f2)
    {
        g.specular(f, f1, f2);
    }

    public void specular(int i)
    {
        g.specular(i);
    }

    public void sphere(float f)
    {
        g.sphere(f);
    }

    public void sphereDetail(int i)
    {
        g.sphereDetail(i);
    }

    public void sphereDetail(int i, int j)
    {
        g.sphereDetail(i, j);
    }

    public void spotLight(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8, float f9, float f10)
    {
        g.spotLight(f, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10);
    }

    public void start()
    {
        finished = false;
        paused = false;
        if(thread == null)
        {
            thread = new Thread(this, "Animation Thread");
            thread.start();
        }
    }

    public void stop()
    {
        paused = true;
    }

    public void stroke(float f)
    {
        g.stroke(f);
    }

    public void stroke(float f, float f1)
    {
        g.stroke(f, f1);
    }

    public void stroke(float f, float f1, float f2)
    {
        g.stroke(f, f1, f2);
    }

    public void stroke(float f, float f1, float f2, float f3)
    {
        g.stroke(f, f1, f2, f3);
    }

    public void stroke(int i)
    {
        g.stroke(i);
    }

    public void stroke(int i, float f)
    {
        g.stroke(i, f);
    }

    public void strokeCap(int i)
    {
        g.strokeCap(i);
    }

    public void strokeJoin(int i)
    {
        g.strokeJoin(i);
    }

    public void strokeWeight(float f)
    {
        g.strokeWeight(f);
    }

    public void style(PStyle pstyle)
    {
        g.style(pstyle);
    }

    public void surfaceKeyDown(int i, android.view.KeyEvent keyevent)
    {
        nativeKeyEvent(keyevent);
    }

    public void surfaceKeyUp(int i, android.view.KeyEvent keyevent)
    {
        nativeKeyEvent(keyevent);
    }

    public boolean surfaceTouchEvent(MotionEvent motionevent)
    {
        nativeMotionEvent(motionevent);
        return true;
    }

    public void surfaceWindowFocusChanged(boolean flag)
    {
        focused = flag;
        if(focused)
            focusGained();
        else
            focusLost();
    }

    public void text(char c, float f, float f1)
    {
        g.text(c, f, f1);
    }

    public void text(char c, float f, float f1, float f2)
    {
        g.text(c, f, f1, f2);
    }

    public void text(float f, float f1, float f2)
    {
        g.text(f, f1, f2);
    }

    public void text(float f, float f1, float f2, float f3)
    {
        g.text(f, f1, f2, f3);
    }

    public void text(int i, float f, float f1)
    {
        g.text(i, f, f1);
    }

    public void text(int i, float f, float f1, float f2)
    {
        g.text(i, f, f1, f2);
    }

    public void text(String s, float f, float f1)
    {
        g.text(s, f, f1);
    }

    public void text(String s, float f, float f1, float f2)
    {
        g.text(s, f, f1, f2);
    }

    public void text(String s, float f, float f1, float f2, float f3)
    {
        g.text(s, f, f1, f2, f3);
    }

    public void textAlign(int i)
    {
        g.textAlign(i);
    }

    public void textAlign(int i, int j)
    {
        g.textAlign(i, j);
    }

    public float textAscent()
    {
        return g.textAscent();
    }

    public float textDescent()
    {
        return g.textDescent();
    }

    public void textFont(PFont pfont)
    {
        g.textFont(pfont);
    }

    public void textFont(PFont pfont, float f)
    {
        g.textFont(pfont, f);
    }

    public void textLeading(float f)
    {
        g.textLeading(f);
    }

    public void textMode(int i)
    {
        g.textMode(i);
    }

    public void textSize(float f)
    {
        g.textSize(f);
    }

    public float textWidth(char c)
    {
        return g.textWidth(c);
    }

    public float textWidth(String s)
    {
        return g.textWidth(s);
    }

    public void texture(PImage pimage)
    {
        g.texture(pimage);
    }

    public void textureMode(int i)
    {
        g.textureMode(i);
    }

    public void textureWrap(int i)
    {
        g.textureWrap(i);
    }

    public void thread(final String name)
    {
        (new Thread() {

            public void run()
            {
                method(name);
            }

            final PApplet this$0;
            final String val$name;

            
            {
                this$0 = PApplet.this;
                name = s;
                super();
            }
        }
).start();
    }

    public void tint(float f)
    {
        g.tint(f);
    }

    public void tint(float f, float f1)
    {
        g.tint(f, f1);
    }

    public void tint(float f, float f1, float f2)
    {
        g.tint(f, f1, f2);
    }

    public void tint(float f, float f1, float f2, float f3)
    {
        g.tint(f, f1, f2, f3);
    }

    public void tint(int i)
    {
        g.tint(i);
    }

    public void tint(int i, float f)
    {
        g.tint(i, f);
    }

    public void translate(float f, float f1)
    {
        g.translate(f, f1);
    }

    public void translate(float f, float f1, float f2)
    {
        g.translate(f, f1, f2);
    }

    public void triangle(float f, float f1, float f2, float f3, float f4, float f5)
    {
        g.triangle(f, f1, f2, f3, f4, f5);
    }

    public void unregisterDispose(Object obj)
    {
        unregisterMethod("dispose", obj);
    }

    public void unregisterDraw(Object obj)
    {
        unregisterMethod("draw", obj);
    }

    public void unregisterMethod(String s, Object obj)
    {
        RegisteredMethods registeredmethods;
        registeredmethods = (RegisteredMethods)registerMap.get(s);
        if(registeredmethods == null)
            die((new StringBuilder()).append("No registered methods with the name ").append(s).append("() were found.").toString());
        registeredmethods.remove(obj);
_L1:
        return;
        Exception exception;
        exception;
        die((new StringBuilder()).append("Could not unregister ").append(s).append("() for ").append(obj).toString(), exception);
          goto _L1
    }

    public void unregisterPost(Object obj)
    {
        unregisterMethod("post", obj);
    }

    public void unregisterPre(Object obj)
    {
        unregisterMethod("pre", obj);
    }

    public void unregisterSize(Object obj)
    {
        System.err.println("The unregisterSize() command is no longer supported.");
    }

    public void updatePixels()
    {
        g.updatePixels();
    }

    public void updatePixels(int i, int j, int k, int l)
    {
        g.updatePixels(i, j, k, l);
    }

    public void vertex(float f, float f1)
    {
        g.vertex(f, f1);
    }

    public void vertex(float f, float f1, float f2)
    {
        g.vertex(f, f1, f2);
    }

    public void vertex(float f, float f1, float f2, float f3)
    {
        g.vertex(f, f1, f2, f3);
    }

    public void vertex(float f, float f1, float f2, float f3, float f4)
    {
        g.vertex(f, f1, f2, f3, f4);
    }

    public void vertex(float af[])
    {
        g.vertex(af);
    }

    public static final String ARGS_BGCOLOR = "--bgcolor";
    public static final String ARGS_DISPLAY = "--display";
    public static final String ARGS_EDITOR_LOCATION = "--editor-location";
    public static final String ARGS_EXCLUSIVE = "--exclusive";
    public static final String ARGS_EXTERNAL = "--external";
    public static final String ARGS_HIDE_STOP = "--hide-stop";
    public static final String ARGS_LOCATION = "--location";
    public static final String ARGS_PRESENT = "--present";
    public static final String ARGS_SKETCH_FOLDER = "--sketch-path";
    public static final String ARGS_STOP_COLOR = "--stop-color";
    public static final boolean DEBUG = false;
    public static final int DEFAULT_HEIGHT = 100;
    public static final int DEFAULT_WIDTH = 100;
    static final String ERROR_MIN_MAX = "Cannot use min() or max() on an empty array.";
    public static final String EXTERNAL_MOVE = "__MOVE__";
    public static final String EXTERNAL_STOP = "__STOP__";
    public static final byte ICON_IMAGE[] = {
        71, 73, 70, 56, 57, 97, 16, 0, 16, 0, 
        -77, 0, 0, 0, 0, 0, -1, -1, -1, 12, 
        12, 13, -15, -15, -14, 45, 57, 74, 54, 80, 
        111, 47, 71, 97, 62, 88, 117, 1, 14, 27, 
        7, 41, 73, 15, 52, 85, 2, 31, 55, 4, 
        54, 94, 18, 69, 109, 37, 87, 126, -1, -1, 
        -1, 33, -7, 4, 1, 0, 0, 15, 0, 44, 
        0, 0, 0, 0, 16, 0, 16, 0, 0, 4, 
        122, -16, -107, 114, -86, -67, 83, 30, -42, 26, 
        -17, -100, -45, 56, -57, -108, 48, 40, 122, -90, 
        104, 67, -91, -51, 32, -53, 77, -78, -100, 47, 
        -86, 12, 76, -110, -20, -74, -101, 97, -93, 27, 
        40, 20, -65, 65, 48, -111, 99, -20, -112, -117, 
        -123, -47, -105, 24, 114, -112, 74, 69, 84, 25, 
        93, 88, -75, 9, 46, 2, 49, 88, -116, -67, 
        7, -19, -83, 60, 38, 3, -34, 2, 66, -95, 
        27, -98, 13, 4, -17, 55, 33, 109, 11, 11, 
        -2, -128, 121, 123, 62, 91, 120, -128, 127, 122, 
        115, 102, 2, 119, 0, -116, -113, -119, 6, 102, 
        121, -108, -126, 5, 18, 6, 4, -102, -101, -100, 
        114, 15, 17, 0, 59
    };
    static final int META_CTRL_ON = 4096;
    static final int META_META_ON = 0x10000;
    static final int PERLIN_SIZE = 4095;
    static final int PERLIN_YWRAP = 16;
    static final int PERLIN_YWRAPB = 4;
    static final int PERLIN_ZWRAP = 256;
    static final int PERLIN_ZWRAPB = 8;
    public static final int SDK;
    private static NumberFormat float_nf;
    private static boolean float_nf_commas;
    private static int float_nf_left;
    private static int float_nf_right;
    private static NumberFormat int_nf;
    private static boolean int_nf_commas;
    private static int int_nf_digits;
    protected static HashMap matchPatterns;
    private Activity activity;
    public int displayHeight;
    public int displayWidth;
    protected int dmouseX;
    protected int dmouseY;
    protected int emouseX;
    protected int emouseY;
    InternalEventQueue eventQueue;
    protected boolean exitCalled;
    boolean external;
    public boolean finished;
    public boolean focused;
    public int frameCount;
    public float frameRate;
    protected long frameRateLastNanos;
    protected long frameRatePeriod;
    protected float frameRateTarget;
    boolean fullScreen;
    public PGraphics g;
    Handler handler;
    public int height;
    boolean insideSettings;
    Random internalRandom;
    public char key;
    public int keyCode;
    public boolean keyPressed;
    protected boolean looping;
    long millisOffset;
    int motionPointerId;
    public boolean mousePressed;
    public int mouseX;
    public int mouseY;
    protected boolean paused;
    float perlin[];
    Random perlinRandom;
    int perlin_PI;
    int perlin_TWOPI;
    float perlin_amp_falloff;
    float perlin_cosTable[];
    int perlin_octaves;
    public int pixels[];
    public int pmouseX;
    public int pmouseY;
    protected boolean redraw;
    HashMap registerMap;
    String renderer;
    volatile int requestImageCount;
    public int requestImageMax;
    private boolean requestedNoLoop;
    public String sketchPath;
    int smooth;
    protected boolean surfaceChanged;
    protected boolean surfaceReady;
    protected SurfaceView surfaceView;
    Thread thread;
    protected boolean viewFocused;
    public int width;
    int windowColor;
    protected boolean windowFocused;

    static 
    {
        SDK = android.os.Build.VERSION.SDK_INT;
    }

}
