// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.opengl;

import android.opengl.*;
import java.nio.*;
import javax.microedition.khronos.egl.*;
import javax.microedition.khronos.opengles.GL10;
import processing.core.PApplet;
import processing.opengl.tess.PGLU;
import processing.opengl.tess.PGLUtessellator;
import processing.opengl.tess.PGLUtessellatorCallbackAdapter;

// Referenced classes of package processing.opengl:
//            PGL, PGraphicsOpenGL

public class PGLES extends PGL
{
    protected class AndroidConfigChooser
        implements android.opengl.GLSurfaceView.EGLConfigChooser
    {

        public EGLConfig chooseBestConfig(EGL10 egl10, EGLDisplay egldisplay, EGLConfig aeglconfig[])
        {
            EGLConfig eglconfig = null;
            float f = 3.402823E+038F;
            int i = aeglconfig.length;
            int j = 0;
            while(j < i) 
            {
                EGLConfig eglconfig1 = aeglconfig[j];
                int k;
                int l;
                int i1;
                int j1;
                int k1;
                int l1;
                float f1;
                if((findConfigAttrib(egl10, egldisplay, eglconfig1, 12352, 0) & 4) != 0)
                    k = 1;
                else
                    k = 0;
                if(k == 0)
                    continue;
                k = findConfigAttrib(egl10, egldisplay, eglconfig1, 12325, 0);
                l = findConfigAttrib(egl10, egldisplay, eglconfig1, 12326, 0);
                i1 = findConfigAttrib(egl10, egldisplay, eglconfig1, 12324, 0);
                j1 = findConfigAttrib(egl10, egldisplay, eglconfig1, 12323, 0);
                k1 = findConfigAttrib(egl10, egldisplay, eglconfig1, 12322, 0);
                l1 = findConfigAttrib(egl10, egldisplay, eglconfig1, 12321, 0);
                f1 = 0.2F * (float)PApplet.abs(i1 - redTarget) + 0.2F * (float)PApplet.abs(j1 - greenTarget) + 0.2F * (float)PApplet.abs(k1 - blueTarget) + 0.15F * (float)PApplet.abs(l1 - alphaTarget) + 0.15F * (float)PApplet.abs(k - depthTarget) + 0.1F * (float)PApplet.abs(l - stencilTarget);
                if(f1 < f)
                {
                    redBits = i1;
                    greenBits = j1;
                    blueBits = k1;
                    alphaBits = l1;
                    depthBits = k;
                    stencilBits = l;
                    eglconfig = eglconfig1;
                    f = f1;
                }
                j++;
            }
            return eglconfig;
        }

        public EGLConfig chooseConfig(EGL10 egl10, EGLDisplay egldisplay)
        {
            EGLConfig aeglconfig[];
            if(1 < numSamples)
            {
                aeglconfig = chooseConfigWithAttribs(egl10, egldisplay, new int[] {
                    12352, 4, 12338, 1, 12337, numSamples, 12344
                });
                if(aeglconfig == null)
                {
                    aeglconfig = chooseConfigWithAttribs(egl10, egldisplay, new int[] {
                        12352, 4, 12512, 1, 12513, numSamples, 12344
                    });
                    if(aeglconfig == null)
                    {
                        aeglconfig = chooseConfigWithAttribs(egl10, egldisplay, attribsNoMSAA);
                    } else
                    {
                        PGLES.usingMultisampling = true;
                        PGLES.usingCoverageMultisampling = true;
                        PGLES.multisampleCount = numSamples;
                    }
                } else
                {
                    PGLES.usingMultisampling = true;
                    PGLES.usingCoverageMultisampling = false;
                    PGLES.multisampleCount = numSamples;
                }
            } else
            {
                aeglconfig = chooseConfigWithAttribs(egl10, egldisplay, attribsNoMSAA);
            }
            if(aeglconfig == null)
                throw new IllegalArgumentException("No EGL configs match configSpec");
            else
                return chooseBestConfig(egl10, egldisplay, aeglconfig);
        }

        protected EGLConfig[] chooseConfigWithAttribs(EGL10 egl10, EGLDisplay egldisplay, int ai[])
        {
            Object obj = null;
            int ai1[] = new int[1];
            egl10.eglChooseConfig(egldisplay, ai, null, 0, ai1);
            int i = ai1[0];
            if(i <= 0)
            {
                egl10 = obj;
            } else
            {
                EGLConfig aeglconfig[] = new EGLConfig[i];
                egl10.eglChooseConfig(egldisplay, ai, aeglconfig, i, ai1);
                egl10 = aeglconfig;
            }
            return egl10;
        }

        protected int findConfigAttrib(EGL10 egl10, EGLDisplay egldisplay, EGLConfig eglconfig, int i, int j)
        {
            if(egl10.eglGetConfigAttrib(egldisplay, eglconfig, i, tempValue))
                j = tempValue[0];
            return j;
        }

        protected String printConfig(EGL10 egl10, EGLDisplay egldisplay, EGLConfig eglconfig)
        {
            int i = findConfigAttrib(egl10, egldisplay, eglconfig, 12324, 0);
            int j = findConfigAttrib(egl10, egldisplay, eglconfig, 12323, 0);
            int k = findConfigAttrib(egl10, egldisplay, eglconfig, 12322, 0);
            int l = findConfigAttrib(egl10, egldisplay, eglconfig, 12321, 0);
            int i1 = findConfigAttrib(egl10, egldisplay, eglconfig, 12325, 0);
            int j1 = findConfigAttrib(egl10, egldisplay, eglconfig, 12326, 0);
            int k1 = findConfigAttrib(egl10, egldisplay, eglconfig, 12352, 0);
            int l1 = findConfigAttrib(egl10, egldisplay, eglconfig, 12333, 0);
            int i2 = findConfigAttrib(egl10, egldisplay, eglconfig, 12320, 0);
            int j2 = findConfigAttrib(egl10, egldisplay, eglconfig, 12422, 0);
            return (new StringBuilder()).append(String.format("EGLConfig rgba=%d%d%d%d depth=%d stencil=%d", new Object[] {
                Integer.valueOf(i), Integer.valueOf(j), Integer.valueOf(k), Integer.valueOf(l), Integer.valueOf(i1), Integer.valueOf(j1)
            })).append(" type=").append(k1).append(" native=").append(l1).append(" buffer size=").append(i2).append(" buffer surface=").append(j2).append(String.format(" caveat=0x%04x", new Object[] {
                Integer.valueOf(findConfigAttrib(egl10, egldisplay, eglconfig, 12327, 0))
            })).toString();
        }

        public int alphaBits;
        public int alphaTarget;
        protected int attribsNoMSAA[] = {
            12352, 4, 12338, 0, 12344
        };
        public int blueBits;
        public int blueTarget;
        public int depthBits;
        public int depthTarget;
        public int greenBits;
        public int greenTarget;
        public int numSamples;
        public int redBits;
        public int redTarget;
        public int stencilBits;
        public int stencilTarget;
        public int tempValue[];
        final PGLES this$0;

        public AndroidConfigChooser(int i, int j, int k, int l, int i1, int j1, 
                int k1)
        {
            this$0 = PGLES.this;
            super();
            tempValue = new int[1];
            redTarget = i;
            greenTarget = j;
            blueTarget = k;
            alphaTarget = l;
            depthTarget = i1;
            stencilTarget = j1;
            numSamples = k1;
        }
    }

    protected class AndroidContextFactory
        implements android.opengl.GLSurfaceView.EGLContextFactory
    {

        public EGLContext createContext(EGL10 egl10, EGLDisplay egldisplay, EGLConfig eglconfig)
        {
            return egl10.eglCreateContext(egldisplay, eglconfig, EGL10.EGL_NO_CONTEXT, new int[] {
                12440, 2, 12344
            });
        }

        public void destroyContext(EGL10 egl10, EGLDisplay egldisplay, EGLContext eglcontext)
        {
            egl10.eglDestroyContext(egldisplay, eglcontext);
        }

        final PGLES this$0;

        protected AndroidContextFactory()
        {
            this$0 = PGLES.this;
            super();
        }
    }

    protected class AndroidRenderer
        implements android.opengl.GLSurfaceView.Renderer
    {

        public void onDrawFrame(GL10 gl10)
        {
            gl = gl10;
            glThread = Thread.currentThread();
            sketch.handleDraw();
        }

        public void onSurfaceChanged(GL10 gl10, int i, int j)
        {
            gl = gl10;
            sketch.displayHeight = i;
            sketch.displayHeight = j;
            graphics.setSize(sketch.sketchWidth(), sketch.sketchHeight());
        }

        public void onSurfaceCreated(GL10 gl10, EGLConfig eglconfig)
        {
            gl = gl10;
            PGLES.context = ((EGL10)EGLContext.getEGL()).eglGetCurrentContext();
            glContext = PGLES.context.hashCode();
            glThread = Thread.currentThread();
            if(!hasFBOs())
                throw new RuntimeException("Framebuffer objects are not supported by this hardware (or driver) Read http://wiki.processing.org/w/OpenGL_Issues for help.");
            if(!hasShaders())
                throw new RuntimeException("GLSL shaders are not supported by this hardware (or driver) Read http://wiki.processing.org/w/OpenGL_Issues for help.");
            else
                return;
        }

        final PGLES this$0;

        public AndroidRenderer()
        {
            this$0 = PGLES.this;
            super();
        }
    }

    protected class Tessellator
        implements PGL.Tessellator
    {

        public void addVertex(double ad[])
        {
            PGLU.gluTessVertex(tess, ad, 0, ad);
        }

        public void beginContour()
        {
            PGLU.gluTessBeginContour(tess);
        }

        public void beginPolygon()
        {
            PGLU.gluTessBeginPolygon(tess, null);
        }

        public void endContour()
        {
            PGLU.gluTessEndContour(tess);
        }

        public void endPolygon()
        {
            PGLU.gluTessEndPolygon(tess);
        }

        public void setWindingRule(int i)
        {
            PGLU.gluTessProperty(tess, 0x1872c, i);
        }

        protected PGL.TessellatorCallback callback;
        protected GLUCallback gluCallback;
        protected PGLUtessellator tess;
        final PGLES this$0;

        public Tessellator(PGL.TessellatorCallback tessellatorcallback)
        {
            this$0 = PGLES.this;
            super();
            callback = tessellatorcallback;
            tess = PGLU.gluNewTess();
            gluCallback = new GLUCallback();
            PGLU.gluTessCallback(tess, 0x18704, gluCallback);
            PGLU.gluTessCallback(tess, 0x18706, gluCallback);
            PGLU.gluTessCallback(tess, 0x18705, gluCallback);
            PGLU.gluTessCallback(tess, 0x18709, gluCallback);
            PGLU.gluTessCallback(tess, 0x18707, gluCallback);
        }
    }

    protected class Tessellator.GLUCallback extends PGLUtessellatorCallbackAdapter
    {

        public void begin(int i)
        {
            callback.begin(i);
        }

        public void combine(double ad[], Object aobj[], float af[], Object aobj1[])
        {
            callback.combine(ad, aobj, af, aobj1);
        }

        public void end()
        {
            callback.end();
        }

        public void error(int i)
        {
            callback.error(i);
        }

        public void vertex(Object obj)
        {
            callback.vertex(obj);
        }

        final Tessellator this$1;

        protected Tessellator.GLUCallback()
        {
            this$1 = Tessellator.this;
            super();
        }
    }


    public PGLES(PGraphicsOpenGL pgraphicsopengl)
    {
        super(pgraphicsopengl);
        glu = new PGLU();
    }

    protected void activeTextureImpl(int i)
    {
        GLES20.glActiveTexture(i);
    }

    public void attachShader(int i, int j)
    {
        GLES20.glAttachShader(i, j);
    }

    public void bindAttribLocation(int i, int j, String s)
    {
        GLES20.glBindAttribLocation(i, j, s);
    }

    public void bindBuffer(int i, int j)
    {
        GLES20.glBindBuffer(i, j);
    }

    protected void bindFramebufferImpl(int i, int j)
    {
        GLES20.glBindFramebuffer(i, j);
    }

    public void bindRenderbuffer(int i, int j)
    {
        GLES20.glBindRenderbuffer(i, j);
    }

    protected void bindTextureImpl(int i, int j)
    {
        GLES20.glBindTexture(i, j);
    }

    public void blendColor(float f, float f1, float f2, float f3)
    {
        GLES20.glBlendColor(f, f1, f2, f3);
    }

    public void blendEquation(int i)
    {
        GLES20.glBlendEquation(i);
    }

    public void blendEquationSeparate(int i, int j)
    {
        GLES20.glBlendEquationSeparate(i, j);
    }

    public void blendFunc(int i, int j)
    {
        GLES20.glBlendFunc(i, j);
    }

    public void blendFuncSeparate(int i, int j, int k, int l)
    {
        GLES20.glBlendFuncSeparate(i, j, k, l);
    }

    public void blitFramebuffer(int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1, int i2, int j2)
    {
    }

    public void bufferData(int i, int j, Buffer buffer, int k)
    {
        GLES20.glBufferData(i, j, buffer, k);
    }

    public void bufferSubData(int i, int j, int k, Buffer buffer)
    {
        GLES20.glBufferSubData(i, j, k, buffer);
    }

    protected boolean canDraw()
    {
        return true;
    }

    public int checkFramebufferStatus(int i)
    {
        return GLES20.glCheckFramebufferStatus(i);
    }

    public void clear(int i)
    {
        int j = i;
        if(usingMultisampling)
        {
            j = i;
            if(usingCoverageMultisampling)
                j = i | 0x8000;
        }
        GLES20.glClear(j);
    }

    public void clearColor(float f, float f1, float f2, float f3)
    {
        GLES20.glClearColor(f, f1, f2, f3);
    }

    public void clearDepth(float f)
    {
        GLES20.glClearDepthf(f);
    }

    public void clearStencil(int i)
    {
        GLES20.glClearStencil(i);
    }

    public int clientWaitSync(long l, int i, long l1)
    {
        return 0;
    }

    public void colorMask(boolean flag, boolean flag1, boolean flag2, boolean flag3)
    {
        GLES20.glColorMask(flag, flag1, flag2, flag3);
    }

    public void compileShader(int i)
    {
        GLES20.glCompileShader(i);
    }

    public void compressedTexImage2D(int i, int j, int k, int l, int i1, int j1, int k1, 
            Buffer buffer)
    {
        GLES20.glCompressedTexImage2D(i, j, k, l, i1, j1, k1, buffer);
    }

    public void compressedTexSubImage2D(int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1, Buffer buffer)
    {
        GLES20.glCompressedTexSubImage2D(i, j, k, l, i1, j1, k1, l1, buffer);
    }

    public void copyTexImage2D(int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1)
    {
        GLES20.glCopyTexImage2D(i, j, k, l, i1, j1, k1, l1);
    }

    public void copyTexSubImage2D(int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1)
    {
        GLES20.glCopyTexSubImage2D(i, j, i1, j1, k, l, k1, l1);
    }

    protected PGL.FontOutline createFontOutline(char c, Object obj)
    {
        return null;
    }

    public int createProgram()
    {
        return GLES20.glCreateProgram();
    }

    public int createShader(int i)
    {
        return GLES20.glCreateShader(i);
    }

    protected volatile PGL.Tessellator createTessellator(PGL.TessellatorCallback tessellatorcallback)
    {
        return createTessellator(tessellatorcallback);
    }

    protected Tessellator createTessellator(PGL.TessellatorCallback tessellatorcallback)
    {
        return new Tessellator(tessellatorcallback);
    }

    public void cullFace(int i)
    {
        GLES20.glCullFace(i);
    }

    public void deleteBuffers(int i, IntBuffer intbuffer)
    {
        GLES20.glDeleteBuffers(i, intbuffer);
    }

    public void deleteFramebuffers(int i, IntBuffer intbuffer)
    {
        GLES20.glDeleteFramebuffers(i, intbuffer);
    }

    public void deleteProgram(int i)
    {
        GLES20.glDeleteProgram(i);
    }

    public void deleteRenderbuffers(int i, IntBuffer intbuffer)
    {
        GLES20.glDeleteRenderbuffers(i, intbuffer);
    }

    public void deleteShader(int i)
    {
        GLES20.glDeleteShader(i);
    }

    public void deleteSync(long l)
    {
    }

    public void deleteTextures(int i, IntBuffer intbuffer)
    {
        GLES20.glDeleteTextures(i, intbuffer);
    }

    public void depthFunc(int i)
    {
        GLES20.glDepthFunc(i);
    }

    public void depthMask(boolean flag)
    {
        GLES20.glDepthMask(flag);
    }

    public void depthRangef(float f, float f1)
    {
        GLES20.glDepthRangef(f, f1);
    }

    public void detachShader(int i, int j)
    {
        GLES20.glDetachShader(i, j);
    }

    public void disable(int i)
    {
        if(-1 < i)
            GLES20.glDisable(i);
    }

    public void disableVertexAttribArray(int i)
    {
        GLES20.glDisableVertexAttribArray(i);
    }

    public void drawArraysImpl(int i, int j, int k)
    {
        GLES20.glDrawArrays(i, j, k);
    }

    public void drawBuffer(int i)
    {
    }

    public void drawElementsImpl(int i, int j, int k, int l)
    {
        GLES20.glDrawElements(i, j, k, l);
    }

    public void enable(int i)
    {
        if(-1 < i)
            GLES20.glEnable(i);
    }

    public void enableVertexAttribArray(int i)
    {
        GLES20.glEnableVertexAttribArray(i);
    }

    public String errorString(int i)
    {
        return GLU.gluErrorString(i);
    }

    public long fenceSync(int i, int j)
    {
        return 0L;
    }

    public void finish()
    {
        GLES20.glFinish();
    }

    public void flush()
    {
        GLES20.glFlush();
    }

    public void framebufferRenderbuffer(int i, int j, int k, int l)
    {
        GLES20.glFramebufferRenderbuffer(i, j, k, l);
    }

    public void framebufferTexture2D(int i, int j, int k, int l, int i1)
    {
        GLES20.glFramebufferTexture2D(i, j, k, l, i1);
    }

    public void frontFace(int i)
    {
        GLES20.glFrontFace(i);
    }

    public void genBuffers(int i, IntBuffer intbuffer)
    {
        GLES20.glGenBuffers(i, intbuffer);
    }

    public void genFramebuffers(int i, IntBuffer intbuffer)
    {
        GLES20.glGenFramebuffers(i, intbuffer);
    }

    public void genRenderbuffers(int i, IntBuffer intbuffer)
    {
        GLES20.glGenRenderbuffers(i, intbuffer);
    }

    public void genTextures(int i, IntBuffer intbuffer)
    {
        GLES20.glGenTextures(i, intbuffer);
    }

    public void generateMipmap(int i)
    {
        GLES20.glGenerateMipmap(i);
    }

    public String getActiveAttrib(int i, int j, IntBuffer intbuffer, IntBuffer intbuffer1)
    {
        int ai[] = new int[3];
        int[] _tmp = ai;
        ai[0] = 0;
        ai[1] = 0;
        ai[2] = 0;
        byte abyte0[] = new byte[1024];
        GLES20.glGetActiveAttrib(i, j, 1024, ai, 0, ai, 1, ai, 2, abyte0, 0);
        intbuffer.put(ai[1]);
        intbuffer1.put(ai[2]);
        return new String(abyte0, 0, ai[0]);
    }

    public String getActiveUniform(int i, int j, IntBuffer intbuffer, IntBuffer intbuffer1)
    {
        int ai[] = new int[3];
        int[] _tmp = ai;
        ai[0] = 0;
        ai[1] = 0;
        ai[2] = 0;
        byte abyte0[] = new byte[1024];
        GLES20.glGetActiveUniform(i, j, 1024, ai, 0, ai, 1, ai, 2, abyte0, 0);
        intbuffer.put(ai[1]);
        intbuffer1.put(ai[2]);
        return new String(abyte0, 0, ai[0]);
    }

    public void getAttachedShaders(int i, int j, IntBuffer intbuffer, IntBuffer intbuffer1)
    {
        GLES20.glGetAttachedShaders(i, j, intbuffer, intbuffer1);
    }

    public int getAttribLocation(int i, String s)
    {
        return GLES20.glGetAttribLocation(i, s);
    }

    public void getBooleanv(int i, IntBuffer intbuffer)
    {
        if(-1 < i)
            GLES20.glGetBooleanv(i, intbuffer);
        else
            fillIntBuffer(intbuffer, 0, intbuffer.capacity(), 0);
    }

    public void getBufferParameteriv(int i, int j, IntBuffer intbuffer)
    {
        GLES20.glGetBufferParameteriv(i, j, intbuffer);
    }

    public AndroidConfigChooser getConfigChooser(int i)
    {
        configChooser = new AndroidConfigChooser(5, 6, 5, 4, 16, 1, i);
        return configChooser;
    }

    public AndroidConfigChooser getConfigChooser(int i, int j, int k, int l, int i1, int j1, int k1)
    {
        configChooser = new AndroidConfigChooser(i, j, k, l, i1, j1, k1);
        return configChooser;
    }

    public AndroidContextFactory getContextFactory()
    {
        return new AndroidContextFactory();
    }

    protected int getDefaultDrawBuffer()
    {
        int i;
        if(fboLayerEnabled)
            i = COLOR_ATTACHMENT0;
        else
            i = FRONT;
        return i;
    }

    protected int getDefaultReadBuffer()
    {
        int i;
        if(fboLayerEnabled)
            i = COLOR_ATTACHMENT0;
        else
            i = FRONT;
        return i;
    }

    protected int getDepthBits()
    {
        intBuffer.rewind();
        getIntegerv(DEPTH_BITS, intBuffer);
        return intBuffer.get(0);
    }

    protected Object getDerivedFont(Object obj, float f)
    {
        return null;
    }

    public int getError()
    {
        return GLES20.glGetError();
    }

    public void getFloatv(int i, FloatBuffer floatbuffer)
    {
        if(-1 < i)
            GLES20.glGetFloatv(i, floatbuffer);
        else
            fillFloatBuffer(floatbuffer, 0, floatbuffer.capacity() - 1, 0.0F);
    }

    protected int getFontAscent(Object obj)
    {
        return 0;
    }

    protected int getFontDescent(Object obj)
    {
        return 0;
    }

    public void getFramebufferAttachmentParameteriv(int i, int j, int k, IntBuffer intbuffer)
    {
        GLES20.glGetFramebufferAttachmentParameteriv(i, j, k, intbuffer);
    }

    protected void getGL(PGL pgl)
    {
        pgl = (PGLES)pgl;
        gl = ((PGLES) (pgl)).gl;
        setThread(((PGLES) (pgl)).glThread);
    }

    protected int getGLSLVersion()
    {
        return 100;
    }

    public void getIntegerv(int i, IntBuffer intbuffer)
    {
        if(-1 < i)
            GLES20.glGetIntegerv(i, intbuffer);
        else
            fillIntBuffer(intbuffer, 0, intbuffer.capacity() - 1, 0);
    }

    public GLSurfaceView getNative()
    {
        return glview;
    }

    public volatile Object getNative()
    {
        return getNative();
    }

    protected float getPixelScale()
    {
        return 1.0F;
    }

    public String getProgramInfoLog(int i)
    {
        return GLES20.glGetProgramInfoLog(i);
    }

    public void getProgramiv(int i, int j, IntBuffer intbuffer)
    {
        GLES20.glGetProgramiv(i, j, intbuffer);
    }

    public void getRenderbufferParameteriv(int i, int j, IntBuffer intbuffer)
    {
        GLES20.glGetRenderbufferParameteriv(i, j, intbuffer);
    }

    public AndroidRenderer getRenderer()
    {
        renderer = new AndroidRenderer();
        return renderer;
    }

    public String getShaderInfoLog(int i)
    {
        return GLES20.glGetShaderInfoLog(i);
    }

    public void getShaderPrecisionFormat(int i, int j, IntBuffer intbuffer, IntBuffer intbuffer1)
    {
        GLES20.glGetShaderPrecisionFormat(i, j, intbuffer, intbuffer1);
    }

    public String getShaderSource(int i)
    {
        int ai[] = new int[1];
        ai[0] = 0;
        byte abyte0[] = new byte[1024];
        GLES20.glGetShaderSource(i, 1024, ai, 0, abyte0, 0);
        return new String(abyte0, 0, ai[0]);
    }

    public void getShaderiv(int i, int j, IntBuffer intbuffer)
    {
        GLES20.glGetShaderiv(i, j, intbuffer);
    }

    protected int getStencilBits()
    {
        intBuffer.rewind();
        getIntegerv(STENCIL_BITS, intBuffer);
        return intBuffer.get(0);
    }

    public String getString(int i)
    {
        return GLES20.glGetString(i);
    }

    public void getTexParameterfv(int i, int j, FloatBuffer floatbuffer)
    {
        GLES20.glGetTexParameterfv(i, j, floatbuffer);
    }

    public void getTexParameteriv(int i, int j, IntBuffer intbuffer)
    {
        GLES20.glGetTexParameteriv(i, j, intbuffer);
    }

    protected int getTextWidth(Object obj, char ac[], int i, int j)
    {
        return 0;
    }

    public int getUniformLocation(int i, String s)
    {
        return GLES20.glGetUniformLocation(i, s);
    }

    public void getUniformfv(int i, int j, FloatBuffer floatbuffer)
    {
        GLES20.glGetUniformfv(i, j, floatbuffer);
    }

    public void getUniformiv(int i, int j, IntBuffer intbuffer)
    {
        GLES20.glGetUniformiv(i, j, intbuffer);
    }

    public void getVertexAttribPointerv(int i, int j, ByteBuffer bytebuffer)
    {
        throw new RuntimeException(String.format("GL function %1$s is not available on this hardware (or driver) Read http://wiki.processing.org/w/OpenGL_Issues for help.", new Object[] {
            "glGetVertexAttribPointerv()"
        }));
    }

    public void getVertexAttribfv(int i, int j, FloatBuffer floatbuffer)
    {
        GLES20.glGetVertexAttribfv(i, j, floatbuffer);
    }

    public void getVertexAttribiv(int i, int j, IntBuffer intbuffer)
    {
        GLES20.glGetVertexAttribiv(i, j, intbuffer);
    }

    public void hint(int i, int j)
    {
        GLES20.glHint(i, j);
    }

    protected void initFBOLayer()
    {
        if(sketch.frameCount > 0)
        {
            IntBuffer intbuffer = allocateDirectIntBuffer(fboWidth * fboHeight);
            if(hasReadBuffer())
                readBuffer(BACK);
            readPixelsImpl(0, 0, fboWidth, fboHeight, RGBA, UNSIGNED_BYTE, intbuffer);
            bindTexture(TEXTURE_2D, glColorTex.get(frontTex));
            texSubImage2D(TEXTURE_2D, 0, 0, 0, fboWidth, fboHeight, RGBA, UNSIGNED_BYTE, intbuffer);
            bindTexture(TEXTURE_2D, glColorTex.get(backTex));
            texSubImage2D(TEXTURE_2D, 0, 0, 0, fboWidth, fboHeight, RGBA, UNSIGNED_BYTE, intbuffer);
            bindTexture(TEXTURE_2D, 0);
            bindFramebufferImpl(FRAMEBUFFER, 0);
        }
    }

    protected void initSurface(int i)
    {
        glview = (GLSurfaceView)sketch.getSurfaceView();
        reqNumSamples = qualityToSamples(i);
        registerListeners();
    }

    public void isBuffer(int i)
    {
        GLES20.glIsBuffer(i);
    }

    public boolean isEnabled(int i)
    {
        return GLES20.glIsEnabled(i);
    }

    public boolean isFramebuffer(int i)
    {
        return GLES20.glIsFramebuffer(i);
    }

    public boolean isProgram(int i)
    {
        return GLES20.glIsProgram(i);
    }

    public boolean isRenderbuffer(int i)
    {
        return GLES20.glIsRenderbuffer(i);
    }

    public boolean isShader(int i)
    {
        return GLES20.glIsShader(i);
    }

    public boolean isTexture(int i)
    {
        return GLES20.glIsTexture(i);
    }

    public void lineWidth(float f)
    {
        GLES20.glLineWidth(f);
    }

    public void linkProgram(int i)
    {
        GLES20.glLinkProgram(i);
    }

    public ByteBuffer mapBuffer(int i, int j)
    {
        throw new RuntimeException(String.format("GL function %1$s is not available on this hardware (or driver) Read http://wiki.processing.org/w/OpenGL_Issues for help.", new Object[] {
            "glMapBuffer"
        }));
    }

    public ByteBuffer mapBufferRange(int i, int j, int k, int l)
    {
        throw new RuntimeException(String.format("GL function %1$s is not available on this hardware (or driver) Read http://wiki.processing.org/w/OpenGL_Issues for help.", new Object[] {
            "glMapBufferRange"
        }));
    }

    public void pixelStorei(int i, int j)
    {
        GLES20.glPixelStorei(i, j);
    }

    public void polygonOffset(float f, float f1)
    {
        GLES20.glPolygonOffset(f, f1);
    }

    public void readBuffer(int i)
    {
    }

    protected void readPixelsImpl(int i, int j, int k, int l, int i1, int j1, long l1)
    {
    }

    public void readPixelsImpl(int i, int j, int k, int l, int i1, int j1, Buffer buffer)
    {
        GLES20.glReadPixels(i, j, k, l, i1, j1, buffer);
    }

    protected void registerListeners()
    {
    }

    protected void reinitSurface()
    {
    }

    public void releaseShaderCompiler()
    {
        GLES20.glReleaseShaderCompiler();
    }

    public void renderbufferStorage(int i, int j, int k, int l)
    {
        GLES20.glRenderbufferStorage(i, j, k, l);
    }

    public void renderbufferStorageMultisample(int i, int j, int k, int l, int i1)
    {
    }

    protected void requestDraw()
    {
        if(graphics.initialized && sketch.canDraw())
            glview.requestRender();
    }

    protected void requestFocus()
    {
    }

    public void sampleCoverage(float f, boolean flag)
    {
        GLES20.glSampleCoverage(f, flag);
    }

    public void scissor(int i, int j, int k, int l)
    {
        GLES20.glScissor(i, j, k, l);
    }

    protected void setFrameRate(float f)
    {
    }

    public void shaderBinary(int i, IntBuffer intbuffer, int j, Buffer buffer, int k)
    {
        GLES20.glShaderBinary(i, intbuffer, j, buffer, k);
    }

    public void shaderSource(int i, String s)
    {
        GLES20.glShaderSource(i, s);
    }

    public void stencilFunc(int i, int j, int k)
    {
        GLES20.glStencilFunc(i, j, k);
    }

    public void stencilFuncSeparate(int i, int j, int k, int l)
    {
        GLES20.glStencilFuncSeparate(i, j, k, l);
    }

    public void stencilMask(int i)
    {
        GLES20.glStencilMask(i);
    }

    public void stencilMaskSeparate(int i, int j)
    {
        GLES20.glStencilMaskSeparate(i, j);
    }

    public void stencilOp(int i, int j, int k)
    {
        GLES20.glStencilOp(i, j, k);
    }

    public void stencilOpSeparate(int i, int j, int k, int l)
    {
        GLES20.glStencilOpSeparate(i, j, k, l);
    }

    protected void swapBuffers()
    {
    }

    protected String tessError(int i)
    {
        return PGLU.gluErrorString(i);
    }

    public void texImage2D(int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1, Buffer buffer)
    {
        GLES20.glTexImage2D(i, j, k, l, i1, j1, k1, l1, buffer);
    }

    public void texParameterf(int i, int j, float f)
    {
        gl.glTexParameterf(i, j, f);
    }

    public void texParameterfv(int i, int j, FloatBuffer floatbuffer)
    {
        GLES20.glTexParameterfv(i, j, floatbuffer);
    }

    public void texParameteri(int i, int j, int k)
    {
        GLES20.glTexParameteri(i, j, k);
    }

    public void texParameteriv(int i, int j, IntBuffer intbuffer)
    {
        GLES20.glTexParameteriv(i, j, intbuffer);
    }

    public void texSubImage2D(int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1, Buffer buffer)
    {
        GLES20.glTexSubImage2D(i, j, k, l, i1, j1, k1, l1, buffer);
    }

    public void uniform1f(int i, float f)
    {
        GLES20.glUniform1f(i, f);
    }

    public void uniform1fv(int i, int j, FloatBuffer floatbuffer)
    {
        GLES20.glUniform1fv(i, j, floatbuffer);
    }

    public void uniform1i(int i, int j)
    {
        GLES20.glUniform1i(i, j);
    }

    public void uniform1iv(int i, int j, IntBuffer intbuffer)
    {
        GLES20.glUniform1iv(i, j, intbuffer);
    }

    public void uniform2f(int i, float f, float f1)
    {
        GLES20.glUniform2f(i, f, f1);
    }

    public void uniform2fv(int i, int j, FloatBuffer floatbuffer)
    {
        GLES20.glUniform2fv(i, j, floatbuffer);
    }

    public void uniform2i(int i, int j, int k)
    {
        GLES20.glUniform2i(i, j, k);
    }

    public void uniform2iv(int i, int j, IntBuffer intbuffer)
    {
        GLES20.glUniform2iv(i, j, intbuffer);
    }

    public void uniform3f(int i, float f, float f1, float f2)
    {
        GLES20.glUniform3f(i, f, f1, f2);
    }

    public void uniform3fv(int i, int j, FloatBuffer floatbuffer)
    {
        GLES20.glUniform3fv(i, j, floatbuffer);
    }

    public void uniform3i(int i, int j, int k, int l)
    {
        GLES20.glUniform3i(i, j, k, l);
    }

    public void uniform3iv(int i, int j, IntBuffer intbuffer)
    {
        GLES20.glUniform3iv(i, j, intbuffer);
    }

    public void uniform4f(int i, float f, float f1, float f2, float f3)
    {
        GLES20.glUniform4f(i, f, f1, f2, f3);
    }

    public void uniform4fv(int i, int j, FloatBuffer floatbuffer)
    {
        GLES20.glUniform4fv(i, j, floatbuffer);
    }

    public void uniform4i(int i, int j, int k, int l, int i1)
    {
        GLES20.glUniform4i(i, j, k, l, i1);
    }

    public void uniform4iv(int i, int j, IntBuffer intbuffer)
    {
        GLES20.glUniform4iv(i, j, intbuffer);
    }

    public void uniformMatrix2fv(int i, int j, boolean flag, FloatBuffer floatbuffer)
    {
        GLES20.glUniformMatrix2fv(i, j, flag, floatbuffer);
    }

    public void uniformMatrix3fv(int i, int j, boolean flag, FloatBuffer floatbuffer)
    {
        GLES20.glUniformMatrix3fv(i, j, flag, floatbuffer);
    }

    public void uniformMatrix4fv(int i, int j, boolean flag, FloatBuffer floatbuffer)
    {
        GLES20.glUniformMatrix4fv(i, j, flag, floatbuffer);
    }

    public void unmapBuffer(int i)
    {
        throw new RuntimeException(String.format("GL function %1$s is not available on this hardware (or driver) Read http://wiki.processing.org/w/OpenGL_Issues for help.", new Object[] {
            "glUnmapBuffer"
        }));
    }

    public void useProgram(int i)
    {
        GLES20.glUseProgram(i);
    }

    public void validateProgram(int i)
    {
        GLES20.glValidateProgram(i);
    }

    public void vertexAttrib1f(int i, float f)
    {
        GLES20.glVertexAttrib1f(i, f);
    }

    public void vertexAttrib1fv(int i, FloatBuffer floatbuffer)
    {
        GLES20.glVertexAttrib1fv(i, floatbuffer);
    }

    public void vertexAttrib2f(int i, float f, float f1)
    {
        GLES20.glVertexAttrib2f(i, f, f1);
    }

    public void vertexAttrib2fv(int i, FloatBuffer floatbuffer)
    {
        GLES20.glVertexAttrib2fv(i, floatbuffer);
    }

    public void vertexAttrib3f(int i, float f, float f1, float f2)
    {
        GLES20.glVertexAttrib3f(i, f, f1, f2);
    }

    public void vertexAttrib3fv(int i, FloatBuffer floatbuffer)
    {
        GLES20.glVertexAttrib3fv(i, floatbuffer);
    }

    public void vertexAttrib4f(int i, float f, float f1, float f2, float f3)
    {
        GLES20.glVertexAttrib4f(i, f, f1, f2, f3);
    }

    public void vertexAttrib4fv(int i, FloatBuffer floatbuffer)
    {
        GLES20.glVertexAttrib4fv(i, floatbuffer);
    }

    public void vertexAttribPointer(int i, int j, int k, boolean flag, int l, int i1)
    {
        GLES20.glVertexAttribPointer(i, j, k, flag, l, i1);
    }

    public void viewport(int i, int j, int k, int l)
    {
        float f = getPixelScale();
        viewportImpl((int)f * i, (int)((float)j * f), (int)((float)k * f), (int)(f * (float)l));
    }

    protected void viewportImpl(int i, int j, int k, int l)
    {
        gl.glViewport(i, j, k, l);
    }

    protected static final int EGL_CONTEXT_CLIENT_VERSION = 12440;
    protected static final int EGL_COVERAGE_BUFFERS_NV = 12512;
    protected static final int EGL_COVERAGE_SAMPLES_NV = 12513;
    protected static final int EGL_OPENGL_ES2_BIT = 4;
    protected static final int GL_COVERAGE_BUFFER_BIT_NV = 32768;
    protected static AndroidConfigChooser configChooser;
    public static EGLContext context;
    public static GLSurfaceView glview;
    protected static int multisampleCount = 1;
    protected static AndroidRenderer renderer;
    protected static boolean usingCoverageMultisampling = false;
    protected static boolean usingMultisampling = false;
    public GL10 gl;
    public PGLU glu;

    static 
    {
        SINGLE_BUFFERED = true;
        MIN_DIRECT_BUFFER_SIZE = 1;
        INDEX_TYPE = 5123;
        MIPMAPS_ENABLED = false;
        DEFAULT_IN_VERTICES = 16;
        DEFAULT_IN_EDGES = 32;
        DEFAULT_IN_TEXTURES = 16;
        DEFAULT_TESS_VERTICES = 16;
        DEFAULT_TESS_INDICES = 32;
        MIN_FONT_TEX_SIZE = 128;
        MAX_FONT_TEX_SIZE = 512;
        MAX_CAPS_JOINS_LENGTH = 1000;
        SHAPE_TEXT_SUPPORTED = false;
        FALSE = 0;
        TRUE = 1;
        INT = 5124;
        BYTE = 5120;
        SHORT = 5122;
        FLOAT = 5126;
        BOOL = 35670;
        UNSIGNED_INT = 5125;
        UNSIGNED_BYTE = 5121;
        UNSIGNED_SHORT = 5123;
        RGB = 6407;
        RGBA = 6408;
        ALPHA = 6406;
        LUMINANCE = 6409;
        LUMINANCE_ALPHA = 6410;
        UNSIGNED_SHORT_5_6_5 = 33635;
        UNSIGNED_SHORT_4_4_4_4 = 32819;
        UNSIGNED_SHORT_5_5_5_1 = 32820;
        RGBA4 = 32854;
        RGB5_A1 = 32855;
        RGB565 = 36194;
        RGB8 = 32849;
        RGBA8 = 32856;
        ALPHA8 = -1;
        READ_ONLY = -1;
        WRITE_ONLY = 35001;
        READ_WRITE = -1;
        TESS_WINDING_NONZERO = 0x18723;
        TESS_WINDING_ODD = 0x18722;
        GENERATE_MIPMAP_HINT = 33170;
        FASTEST = 4353;
        NICEST = 4354;
        DONT_CARE = 4352;
        VENDOR = 7936;
        RENDERER = 7937;
        VERSION = 7938;
        EXTENSIONS = 7939;
        SHADING_LANGUAGE_VERSION = 35724;
        MAX_SAMPLES = -1;
        SAMPLES = 32937;
        ALIASED_LINE_WIDTH_RANGE = 33902;
        ALIASED_POINT_SIZE_RANGE = 33901;
        DEPTH_BITS = 3414;
        STENCIL_BITS = 3415;
        CCW = 2305;
        CW = 2304;
        VIEWPORT = 2978;
        ARRAY_BUFFER = 34962;
        ELEMENT_ARRAY_BUFFER = 34963;
        MAX_VERTEX_ATTRIBS = 34921;
        STATIC_DRAW = 35044;
        DYNAMIC_DRAW = 35048;
        STREAM_DRAW = 35040;
        BUFFER_SIZE = 34660;
        BUFFER_USAGE = 34661;
        POINTS = 0;
        LINE_STRIP = 3;
        LINE_LOOP = 2;
        LINES = 1;
        TRIANGLE_FAN = 6;
        TRIANGLE_STRIP = 5;
        TRIANGLES = 4;
        CULL_FACE = 2884;
        FRONT = 1028;
        BACK = 1029;
        FRONT_AND_BACK = 1032;
        POLYGON_OFFSET_FILL = 32823;
        UNPACK_ALIGNMENT = 3317;
        PACK_ALIGNMENT = 3333;
        TEXTURE_2D = 3553;
        TEXTURE_RECTANGLE = -1;
        TEXTURE_BINDING_2D = 32873;
        TEXTURE_BINDING_RECTANGLE = -1;
        MAX_TEXTURE_SIZE = 3379;
        TEXTURE_MAX_ANISOTROPY = 34046;
        MAX_TEXTURE_MAX_ANISOTROPY = 34047;
        MAX_VERTEX_TEXTURE_IMAGE_UNITS = 35660;
        MAX_TEXTURE_IMAGE_UNITS = 34930;
        MAX_COMBINED_TEXTURE_IMAGE_UNITS = 35661;
        NUM_COMPRESSED_TEXTURE_FORMATS = 34466;
        COMPRESSED_TEXTURE_FORMATS = 34467;
        NEAREST = 9728;
        LINEAR = 9729;
        LINEAR_MIPMAP_NEAREST = 9985;
        LINEAR_MIPMAP_LINEAR = 9987;
        CLAMP_TO_EDGE = 33071;
        REPEAT = 10497;
        TEXTURE0 = 33984;
        TEXTURE1 = 33985;
        TEXTURE2 = 33986;
        TEXTURE3 = 33987;
        TEXTURE_MIN_FILTER = 10241;
        TEXTURE_MAG_FILTER = 10240;
        TEXTURE_WRAP_S = 10242;
        TEXTURE_WRAP_T = 10243;
        TEXTURE_WRAP_R = 32882;
        TEXTURE_CUBE_MAP = 34067;
        TEXTURE_CUBE_MAP_POSITIVE_X = 34069;
        TEXTURE_CUBE_MAP_POSITIVE_Y = 34071;
        TEXTURE_CUBE_MAP_POSITIVE_Z = 34073;
        TEXTURE_CUBE_MAP_NEGATIVE_X = 34070;
        TEXTURE_CUBE_MAP_NEGATIVE_Y = 34072;
        TEXTURE_CUBE_MAP_NEGATIVE_Z = 34074;
        VERTEX_SHADER = 35633;
        FRAGMENT_SHADER = 35632;
        INFO_LOG_LENGTH = 35716;
        SHADER_SOURCE_LENGTH = 35720;
        COMPILE_STATUS = 35713;
        LINK_STATUS = 35714;
        VALIDATE_STATUS = 35715;
        SHADER_TYPE = 35663;
        DELETE_STATUS = 35712;
        FLOAT_VEC2 = 35664;
        FLOAT_VEC3 = 35665;
        FLOAT_VEC4 = 35666;
        FLOAT_MAT2 = 35674;
        FLOAT_MAT3 = 35675;
        FLOAT_MAT4 = 35676;
        INT_VEC2 = 35667;
        INT_VEC3 = 35668;
        INT_VEC4 = 35669;
        BOOL_VEC2 = 35671;
        BOOL_VEC3 = 35672;
        BOOL_VEC4 = 35673;
        SAMPLER_2D = 35678;
        SAMPLER_CUBE = 35680;
        LOW_FLOAT = 36336;
        MEDIUM_FLOAT = 36337;
        HIGH_FLOAT = 36338;
        LOW_INT = 36339;
        MEDIUM_INT = 36340;
        HIGH_INT = 36341;
        CURRENT_VERTEX_ATTRIB = 34342;
        VERTEX_ATTRIB_ARRAY_BUFFER_BINDING = 34975;
        VERTEX_ATTRIB_ARRAY_ENABLED = 34338;
        VERTEX_ATTRIB_ARRAY_SIZE = 34339;
        VERTEX_ATTRIB_ARRAY_STRIDE = 34340;
        VERTEX_ATTRIB_ARRAY_TYPE = 34341;
        VERTEX_ATTRIB_ARRAY_NORMALIZED = 34922;
        VERTEX_ATTRIB_ARRAY_POINTER = 34373;
        BLEND = 3042;
        ONE = 1;
        ZERO = 0;
        SRC_ALPHA = 770;
        DST_ALPHA = 772;
        ONE_MINUS_SRC_ALPHA = 771;
        ONE_MINUS_DST_COLOR = 775;
        ONE_MINUS_SRC_COLOR = 769;
        DST_COLOR = 774;
        SRC_COLOR = 768;
        SAMPLE_ALPHA_TO_COVERAGE = 32926;
        SAMPLE_COVERAGE = 32928;
        KEEP = 7680;
        REPLACE = 7681;
        INCR = 7682;
        DECR = 7683;
        INVERT = 5386;
        INCR_WRAP = 34055;
        DECR_WRAP = 34056;
        NEVER = 512;
        ALWAYS = 519;
        EQUAL = 514;
        LESS = 513;
        LEQUAL = 515;
        GREATER = 516;
        GEQUAL = 518;
        NOTEQUAL = 517;
        FUNC_ADD = 32774;
        FUNC_MIN = 32775;
        FUNC_MAX = 32776;
        FUNC_REVERSE_SUBTRACT = 32779;
        FUNC_SUBTRACT = 32778;
        DITHER = 3024;
        CONSTANT_COLOR = 32769;
        CONSTANT_ALPHA = 32771;
        ONE_MINUS_CONSTANT_COLOR = 32770;
        ONE_MINUS_CONSTANT_ALPHA = 32772;
        SRC_ALPHA_SATURATE = 776;
        SCISSOR_TEST = 3089;
        STENCIL_TEST = 2960;
        DEPTH_TEST = 2929;
        DEPTH_WRITEMASK = 2930;
        COLOR_BUFFER_BIT = 16384;
        DEPTH_BUFFER_BIT = 256;
        STENCIL_BUFFER_BIT = 1024;
        FRAMEBUFFER = 36160;
        COLOR_ATTACHMENT0 = 36064;
        COLOR_ATTACHMENT1 = -1;
        COLOR_ATTACHMENT2 = -1;
        COLOR_ATTACHMENT3 = -1;
        RENDERBUFFER = 36161;
        DEPTH_ATTACHMENT = 36096;
        STENCIL_ATTACHMENT = 36128;
        READ_FRAMEBUFFER = -1;
        DRAW_FRAMEBUFFER = -1;
        DEPTH24_STENCIL8 = 35056;
        DEPTH_COMPONENT = 6402;
        DEPTH_COMPONENT16 = 33189;
        DEPTH_COMPONENT24 = 33190;
        DEPTH_COMPONENT32 = 33191;
        STENCIL_INDEX = 6401;
        STENCIL_INDEX1 = 36166;
        STENCIL_INDEX4 = 36167;
        STENCIL_INDEX8 = 36168;
        DEPTH_STENCIL = 34041;
        FRAMEBUFFER_COMPLETE = 36053;
        FRAMEBUFFER_INCOMPLETE_ATTACHMENT = 36054;
        FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = 36055;
        FRAMEBUFFER_INCOMPLETE_DIMENSIONS = 36057;
        FRAMEBUFFER_INCOMPLETE_FORMATS = 36058;
        FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER = -1;
        FRAMEBUFFER_INCOMPLETE_READ_BUFFER = -1;
        FRAMEBUFFER_UNSUPPORTED = 36061;
        FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE = 36048;
        FRAMEBUFFER_ATTACHMENT_OBJECT_NAME = 36049;
        FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL = 36050;
        FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE = 36051;
        RENDERBUFFER_WIDTH = 36162;
        RENDERBUFFER_HEIGHT = 36163;
        RENDERBUFFER_RED_SIZE = 36176;
        RENDERBUFFER_GREEN_SIZE = 36177;
        RENDERBUFFER_BLUE_SIZE = 36178;
        RENDERBUFFER_ALPHA_SIZE = 36179;
        RENDERBUFFER_DEPTH_SIZE = 36180;
        RENDERBUFFER_STENCIL_SIZE = 36181;
        RENDERBUFFER_INTERNAL_FORMAT = 36164;
        MULTISAMPLE = -1;
        LINE_SMOOTH = -1;
        POLYGON_SMOOTH = -1;
    }
}
