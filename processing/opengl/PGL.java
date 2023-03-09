// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.opengl;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.nio.*;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import processing.core.PApplet;
import processing.core.PGraphics;

// Referenced classes of package processing.opengl:
//            PGraphicsOpenGL, Texture

public abstract class PGL
{
    protected static interface FontOutline
    {

        public abstract int currentSegment(float af[]);

        public abstract boolean isDone();

        public abstract void next();
    }

    protected static interface Tessellator
    {

        public abstract void addVertex(double ad[]);

        public abstract void beginContour();

        public abstract void beginPolygon();

        public abstract void endContour();

        public abstract void endPolygon();

        public abstract void setWindingRule(int i);
    }

    protected static interface TessellatorCallback
    {

        public abstract void begin(int i);

        public abstract void combine(double ad[], Object aobj[], float af[], Object aobj1[]);

        public abstract void end();

        public abstract void error(int i);

        public abstract void vertex(Object obj);
    }


    public PGL()
    {
        fboLayerEnabled = false;
        fboLayerCreated = false;
        fboLayerEnabledReq = false;
        fboLayerDisableReq = false;
        fbolayerResetReq = false;
        usingFrontTex = false;
        needSepFrontTex = false;
        loadedTex2DShader = false;
        loadedTexRectShader = false;
        activeTexUnit = 0;
        targetFps = 60F;
        currentFps = 60F;
        setFps = false;
        geomCount = 0;
        clearColor = false;
        presentMode = false;
        showStopButton = true;
        stopButtonWidth = 28;
        stopButtonHeight = 12;
        stopButtonX = 21;
        closeButtonY = 21;
    }

    public PGL(PGraphicsOpenGL pgraphicsopengl)
    {
        fboLayerEnabled = false;
        fboLayerCreated = false;
        fboLayerEnabledReq = false;
        fboLayerDisableReq = false;
        fbolayerResetReq = false;
        usingFrontTex = false;
        needSepFrontTex = false;
        loadedTex2DShader = false;
        loadedTexRectShader = false;
        activeTexUnit = 0;
        targetFps = 60F;
        currentFps = 60F;
        setFps = false;
        geomCount = 0;
        clearColor = false;
        presentMode = false;
        showStopButton = true;
        stopButtonWidth = 28;
        stopButtonHeight = 12;
        stopButtonX = 21;
        closeButtonY = 21;
        graphics = pgraphicsopengl;
        if(glColorTex == null)
        {
            glColorFbo = allocateIntBuffer(1);
            glColorTex = allocateIntBuffer(2);
            glDepthStencil = allocateIntBuffer(1);
            glDepth = allocateIntBuffer(1);
            glStencil = allocateIntBuffer(1);
            glMultiFbo = allocateIntBuffer(1);
            glMultiColor = allocateIntBuffer(1);
            glMultiDepthStencil = allocateIntBuffer(1);
            glMultiDepth = allocateIntBuffer(1);
            glMultiStencil = allocateIntBuffer(1);
        }
        byteBuffer = allocateByteBuffer(1);
        intBuffer = allocateIntBuffer(1);
        viewBuffer = allocateIntBuffer(4);
    }

    protected static ByteBuffer allocateByteBuffer(int i)
    {
        ByteBuffer bytebuffer;
        if(USE_DIRECT_BUFFERS)
            bytebuffer = allocateDirectByteBuffer(i);
        else
            bytebuffer = ByteBuffer.allocate(i);
        return bytebuffer;
    }

    protected static ByteBuffer allocateByteBuffer(byte abyte0[])
    {
        if(USE_DIRECT_BUFFERS)
        {
            ByteBuffer bytebuffer = allocateDirectByteBuffer(abyte0.length);
            bytebuffer.put(abyte0);
            bytebuffer.position(0);
            abyte0 = bytebuffer;
        } else
        {
            abyte0 = ByteBuffer.wrap(abyte0);
        }
        return abyte0;
    }

    protected static ByteBuffer allocateDirectByteBuffer(int i)
    {
        return ByteBuffer.allocateDirect(PApplet.max(MIN_DIRECT_BUFFER_SIZE, i) * SIZEOF_BYTE).order(ByteOrder.nativeOrder());
    }

    protected static FloatBuffer allocateDirectFloatBuffer(int i)
    {
        return ByteBuffer.allocateDirect(PApplet.max(MIN_DIRECT_BUFFER_SIZE, i) * SIZEOF_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    protected static IntBuffer allocateDirectIntBuffer(int i)
    {
        return ByteBuffer.allocateDirect(PApplet.max(MIN_DIRECT_BUFFER_SIZE, i) * SIZEOF_INT).order(ByteOrder.nativeOrder()).asIntBuffer();
    }

    protected static ShortBuffer allocateDirectShortBuffer(int i)
    {
        return ByteBuffer.allocateDirect(PApplet.max(MIN_DIRECT_BUFFER_SIZE, i) * SIZEOF_SHORT).order(ByteOrder.nativeOrder()).asShortBuffer();
    }

    protected static FloatBuffer allocateFloatBuffer(int i)
    {
        FloatBuffer floatbuffer;
        if(USE_DIRECT_BUFFERS)
            floatbuffer = allocateDirectFloatBuffer(i);
        else
            floatbuffer = FloatBuffer.allocate(i);
        return floatbuffer;
    }

    protected static FloatBuffer allocateFloatBuffer(float af[])
    {
        if(USE_DIRECT_BUFFERS)
        {
            FloatBuffer floatbuffer = allocateDirectFloatBuffer(af.length);
            floatbuffer.put(af);
            floatbuffer.position(0);
            af = floatbuffer;
        } else
        {
            af = FloatBuffer.wrap(af);
        }
        return af;
    }

    protected static IntBuffer allocateIntBuffer(int i)
    {
        IntBuffer intbuffer;
        if(USE_DIRECT_BUFFERS)
            intbuffer = allocateDirectIntBuffer(i);
        else
            intbuffer = IntBuffer.allocate(i);
        return intbuffer;
    }

    protected static IntBuffer allocateIntBuffer(int ai[])
    {
        if(USE_DIRECT_BUFFERS)
        {
            IntBuffer intbuffer = allocateDirectIntBuffer(ai.length);
            intbuffer.put(ai);
            intbuffer.position(0);
            ai = intbuffer;
        } else
        {
            ai = IntBuffer.wrap(ai);
        }
        return ai;
    }

    protected static ShortBuffer allocateShortBuffer(int i)
    {
        ShortBuffer shortbuffer;
        if(USE_DIRECT_BUFFERS)
            shortbuffer = allocateDirectShortBuffer(i);
        else
            shortbuffer = ShortBuffer.allocate(i);
        return shortbuffer;
    }

    protected static ShortBuffer allocateShortBuffer(short aword0[])
    {
        if(USE_DIRECT_BUFFERS)
        {
            ShortBuffer shortbuffer = allocateDirectShortBuffer(aword0.length);
            shortbuffer.put(aword0);
            shortbuffer.position(0);
            aword0 = shortbuffer;
        } else
        {
            aword0 = ShortBuffer.wrap(aword0);
        }
        return aword0;
    }

    protected static boolean containsVersionDirective(String as[])
    {
        boolean flag = false;
        int i = 0;
        do
        {
label0:
            {
                boolean flag1 = flag;
                if(i < as.length)
                {
                    if(!as[i].contains("#version"))
                        break label0;
                    flag1 = true;
                }
                return flag1;
            }
            i++;
        } while(true);
    }

    private void createDepthAndStencilBuffer(boolean flag, int i, int j, boolean flag1)
    {
        if(!flag1 || i != 24 || j != 8) goto _L2; else goto _L1
_L1:
        IntBuffer intbuffer;
        if(flag)
            intbuffer = glMultiDepthStencil;
        else
            intbuffer = glDepthStencil;
        genRenderbuffers(1, intbuffer);
        bindRenderbuffer(RENDERBUFFER, intbuffer.get(0));
        if(flag)
            renderbufferStorageMultisample(RENDERBUFFER, numSamples, DEPTH24_STENCIL8, fboWidth, fboHeight);
        else
            renderbufferStorage(RENDERBUFFER, DEPTH24_STENCIL8, fboWidth, fboHeight);
        framebufferRenderbuffer(FRAMEBUFFER, DEPTH_ATTACHMENT, RENDERBUFFER, intbuffer.get(0));
        framebufferRenderbuffer(FRAMEBUFFER, STENCIL_ATTACHMENT, RENDERBUFFER, intbuffer.get(0));
_L7:
        return;
_L2:
        IntBuffer intbuffer1;
        if(i > 0)
        {
            int k = DEPTH_COMPONENT16;
            if(i == 32)
                k = DEPTH_COMPONENT32;
            else
            if(i == 24)
                k = DEPTH_COMPONENT24;
            else
            if(i == 16)
                k = DEPTH_COMPONENT16;
            if(flag)
                intbuffer1 = glMultiDepth;
            else
                intbuffer1 = glDepth;
            genRenderbuffers(1, intbuffer1);
            bindRenderbuffer(RENDERBUFFER, intbuffer1.get(0));
            if(flag)
                renderbufferStorageMultisample(RENDERBUFFER, numSamples, k, fboWidth, fboHeight);
            else
                renderbufferStorage(RENDERBUFFER, k, fboWidth, fboHeight);
            framebufferRenderbuffer(FRAMEBUFFER, DEPTH_ATTACHMENT, RENDERBUFFER, intbuffer1.get(0));
        }
        if(j <= 0) goto _L4; else goto _L3
_L3:
        i = STENCIL_INDEX1;
        if(j != 8) goto _L6; else goto _L5
_L5:
        i = STENCIL_INDEX8;
_L8:
        if(flag)
            intbuffer1 = glMultiStencil;
        else
            intbuffer1 = glStencil;
        genRenderbuffers(1, intbuffer1);
        bindRenderbuffer(RENDERBUFFER, intbuffer1.get(0));
        if(flag)
            renderbufferStorageMultisample(RENDERBUFFER, numSamples, i, fboWidth, fboHeight);
        else
            renderbufferStorage(RENDERBUFFER, i, fboWidth, fboHeight);
        framebufferRenderbuffer(FRAMEBUFFER, STENCIL_ATTACHMENT, RENDERBUFFER, intbuffer1.get(0));
_L4:
        if(true) goto _L7; else goto _L6
_L6:
        if(j == 4)
            i = STENCIL_INDEX4;
        else
        if(j == 1)
            i = STENCIL_INDEX1;
          goto _L8
    }

    private void createFBOLayer()
    {
        float f = getPixelScale();
        boolean flag;
        boolean flag1;
        int k;
        int l;
        if(hasNpotTexSupport())
        {
            fboWidth = (int)((float)graphics.width * f);
            fboHeight = (int)(f * (float)graphics.height);
        } else
        {
            fboWidth = nextPowerOfTwo((int)((float)graphics.width * f));
            fboHeight = nextPowerOfTwo((int)(f * (float)graphics.height));
        }
        if(hasFboMultisampleSupport())
        {
            int i = maxSamples();
            numSamples = PApplet.min(reqNumSamples, i);
        } else
        {
            numSamples = 1;
        }
        if(1 < numSamples)
            flag = true;
        else
            flag = false;
        flag1 = hasPackedDepthStencilSupport();
        k = PApplet.min(REQUESTED_DEPTH_BITS, getDepthBits());
        l = PApplet.min(REQUESTED_STENCIL_BITS, getStencilBits());
        genTextures(2, glColorTex);
        for(int i1 = 0; i1 < 2; i1++)
        {
            bindTexture(TEXTURE_2D, glColorTex.get(i1));
            texParameteri(TEXTURE_2D, TEXTURE_MIN_FILTER, NEAREST);
            texParameteri(TEXTURE_2D, TEXTURE_MAG_FILTER, NEAREST);
            texParameteri(TEXTURE_2D, TEXTURE_WRAP_S, CLAMP_TO_EDGE);
            texParameteri(TEXTURE_2D, TEXTURE_WRAP_T, CLAMP_TO_EDGE);
            texImage2D(TEXTURE_2D, 0, RGBA, fboWidth, fboHeight, 0, RGBA, UNSIGNED_BYTE, null);
            initTexture(TEXTURE_2D, RGBA, fboWidth, fboHeight, graphics.backgroundColor);
        }

        bindTexture(TEXTURE_2D, 0);
        backTex = 0;
        frontTex = 1;
        genFramebuffers(1, glColorFbo);
        bindFramebufferImpl(FRAMEBUFFER, glColorFbo.get(0));
        framebufferTexture2D(FRAMEBUFFER, COLOR_ATTACHMENT0, TEXTURE_2D, glColorTex.get(backTex), 0);
        if(!flag || graphics.getHint(10))
            createDepthAndStencilBuffer(false, k, l, flag1);
        if(flag)
        {
            genFramebuffers(1, glMultiFbo);
            bindFramebufferImpl(FRAMEBUFFER, glMultiFbo.get(0));
            genRenderbuffers(1, glMultiColor);
            bindRenderbuffer(RENDERBUFFER, glMultiColor.get(0));
            renderbufferStorageMultisample(RENDERBUFFER, numSamples, RGBA8, fboWidth, fboHeight);
            framebufferRenderbuffer(FRAMEBUFFER, COLOR_ATTACHMENT0, RENDERBUFFER, glMultiColor.get(0));
            createDepthAndStencilBuffer(true, k, l, flag1);
        }
        validateFramebuffer();
        clearDepth(1.0F);
        clearStencil(0);
        int j = graphics.backgroundColor;
        f = (float)(j >> 24 & 0xff) / 255F;
        clearColor((float)(j >> 16 & 0xff) / 255F, (float)(j >> 8 & 0xff) / 255F, (float)(j & 0xff) / 255F, f);
        clear(DEPTH_BUFFER_BIT | STENCIL_BUFFER_BIT | COLOR_BUFFER_BIT);
        bindFramebufferImpl(FRAMEBUFFER, 0);
        initFBOLayer();
        fboLayerCreated = true;
    }

    protected static void fillByteBuffer(ByteBuffer bytebuffer, int i, int j, byte byte0)
    {
        j -= i;
        byte abyte0[] = new byte[j];
        Arrays.fill(abyte0, 0, j, byte0);
        bytebuffer.position(i);
        bytebuffer.put(abyte0, 0, j);
        bytebuffer.rewind();
    }

    protected static void fillFloatBuffer(FloatBuffer floatbuffer, int i, int j, float f)
    {
        j -= i;
        float af[] = new float[j];
        Arrays.fill(af, 0, j, f);
        floatbuffer.position(i);
        floatbuffer.put(af, 0, j);
        floatbuffer.rewind();
    }

    protected static void fillIntBuffer(IntBuffer intbuffer, int i, int j, int k)
    {
        j -= i;
        int ai[] = new int[j];
        Arrays.fill(ai, 0, j, k);
        intbuffer.position(i);
        intbuffer.put(ai, 0, j);
        intbuffer.rewind();
    }

    protected static void fillShortBuffer(ShortBuffer shortbuffer, int i, int j, short word0)
    {
        j -= i;
        short aword0[] = new short[j];
        Arrays.fill(aword0, 0, j, word0);
        shortbuffer.position(i);
        shortbuffer.put(aword0, 0, j);
        shortbuffer.rewind();
    }

    protected static void getByteArray(ByteBuffer bytebuffer, byte abyte0[])
    {
        if(!bytebuffer.hasArray() || bytebuffer.array() != abyte0)
        {
            bytebuffer.position(0);
            bytebuffer.get(abyte0);
            bytebuffer.rewind();
        }
    }

    protected static void getFloatArray(FloatBuffer floatbuffer, float af[])
    {
        if(!floatbuffer.hasArray() || floatbuffer.array() != af)
        {
            floatbuffer.position(0);
            floatbuffer.get(af);
            floatbuffer.rewind();
        }
    }

    protected static void getIntArray(IntBuffer intbuffer, int ai[])
    {
        if(!intbuffer.hasArray() || intbuffer.array() != ai)
        {
            intbuffer.position(0);
            intbuffer.get(ai);
            intbuffer.rewind();
        }
    }

    protected static void getShortArray(ShortBuffer shortbuffer, short aword0[])
    {
        if(!shortbuffer.hasArray() || shortbuffer.array() != aword0)
        {
            shortbuffer.position(0);
            shortbuffer.get(aword0);
            shortbuffer.rewind();
        }
    }

    protected static boolean isPowerOfTwo(int i)
    {
        boolean flag;
        if((i - 1 & i) == 0)
            flag = true;
        else
            flag = false;
        return flag;
    }

    protected static int javaToNativeARGB(int i)
    {
        if(BIG_ENDIAN)
        {
            i = i >>> 24 | i << 8;
        } else
        {
            int j = 0xff00ff & i;
            i = j >> 16 | (0xff00ff00 & i | j << 16);
        }
        return i;
    }

    protected static void javaToNativeARGB(int ai[], int i, int j)
    {
        boolean flag = false;
        int k = (j - 1) * i;
        int i1 = 0;
        for(int k1 = 0; k1 < j / 2; k1++)
        {
            int i2 = 0;
            while(i2 < i) 
            {
                int j2 = ai[k];
                int k2 = ai[i1];
                if(BIG_ENDIAN)
                {
                    ai[i1] = j2 << 8 | j2 >>> 24;
                    ai[k] = k2 >>> 24 | k2 << 8;
                } else
                {
                    int l2 = k2 & 0xff00ff;
                    int i3 = j2 & 0xff00ff;
                    ai[i1] = j2 & 0xff00ff00 | i3 << 16 | i3 >> 16;
                    ai[k] = k2 & 0xff00ff00 | l2 << 16 | l2 >> 16;
                }
                i2++;
                k++;
                i1++;
            }
            k -= i * 2;
        }

        if(j % 2 == 1)
        {
            int l = (j / 2) * i;
            j = ((flag) ? 1 : 0);
            while(j < i) 
            {
                int l1 = ai[l];
                if(BIG_ENDIAN)
                {
                    ai[l] = l1 << 8 | l1 >>> 24;
                } else
                {
                    int j1 = l1 & 0xff00ff;
                    ai[l] = l1 & 0xff00ff00 | j1 << 16 | j1 >> 16;
                }
                l++;
                j++;
            }
        }
    }

    protected static int javaToNativeRGB(int i)
    {
        if(BIG_ENDIAN)
        {
            i = i << 8 | 0xff;
        } else
        {
            int j = 0xff00ff & i;
            i = j >> 16 | (0xff000000 | j << 16 | 0xff00 & i);
        }
        return i;
    }

    protected static void javaToNativeRGB(int ai[], int i, int j)
    {
        boolean flag = false;
        int k = (j - 1) * i;
        int i1 = 0;
        for(int k1 = 0; k1 < j / 2; k1++)
        {
            int i2 = 0;
            while(i2 < i) 
            {
                int j2 = ai[k];
                int k2 = ai[i1];
                if(BIG_ENDIAN)
                {
                    ai[i1] = j2 << 8 | 0xff;
                    ai[k] = k2 << 8 | 0xff;
                } else
                {
                    int l2 = k2 & 0xff00ff;
                    int i3 = j2 & 0xff00ff;
                    ai[i1] = j2 & 0xff00 | (i3 << 16 | 0xff000000) | i3 >> 16;
                    ai[k] = l2 << 16 | 0xff000000 | k2 & 0xff00 | l2 >> 16;
                }
                i2++;
                k++;
                i1++;
            }
            k -= i * 2;
        }

        if(j % 2 == 1)
        {
            int l = (j / 2) * i;
            j = ((flag) ? 1 : 0);
            while(j < i) 
            {
                int l1 = ai[l];
                if(BIG_ENDIAN)
                {
                    ai[l] = l1 << 8 | 0xff;
                } else
                {
                    int j1 = l1 & 0xff00ff;
                    ai[l] = l1 & 0xff00 | (j1 << 16 | 0xff000000) | j1 >> 16;
                }
                l++;
                j++;
            }
        }
    }

    protected static int nativeToJavaARGB(int i)
    {
        if(BIG_ENDIAN)
        {
            i = i >>> 8 | i << 24;
        } else
        {
            int j = 0xff00ff & i;
            i = j >> 16 | (0xff00ff00 & i | j << 16);
        }
        return i;
    }

    protected static void nativeToJavaARGB(int ai[], int i, int j)
    {
        boolean flag = false;
        int k = (j - 1) * i;
        int i1 = 0;
        for(int k1 = 0; k1 < j / 2; k1++)
        {
            int i2 = 0;
            while(i2 < i) 
            {
                int j2 = ai[k];
                int k2 = ai[i1];
                if(BIG_ENDIAN)
                {
                    ai[i1] = j2 << 24 | j2 >>> 8;
                    ai[k] = k2 >>> 8 | k2 << 24;
                } else
                {
                    int l2 = k2 & 0xff00ff;
                    int i3 = j2 & 0xff00ff;
                    ai[i1] = j2 & 0xff00ff00 | i3 << 16 | i3 >> 16;
                    ai[k] = k2 & 0xff00ff00 | l2 << 16 | l2 >> 16;
                }
                i2++;
                k++;
                i1++;
            }
            k -= i * 2;
        }

        if(j % 2 == 1)
        {
            int l = (j / 2) * i;
            j = ((flag) ? 1 : 0);
            while(j < i) 
            {
                int l1 = ai[l];
                if(BIG_ENDIAN)
                {
                    ai[l] = l1 << 24 | l1 >>> 8;
                } else
                {
                    int j1 = l1 & 0xff00ff;
                    ai[l] = l1 & 0xff00ff00 | j1 << 16 | j1 >> 16;
                }
                l++;
                j++;
            }
        }
    }

    protected static int nativeToJavaRGB(int i)
    {
        if(BIG_ENDIAN)
        {
            i = i >>> 8 | 0xff000000;
        } else
        {
            int j = 0xff00ff & i;
            i = j >> 16 | (j << 16 | 0xff000000 | 0xff00 & i);
        }
        return i;
    }

    protected static void nativeToJavaRGB(int ai[], int i, int j)
    {
        boolean flag = false;
        int k = (j - 1) * i;
        int i1 = 0;
        for(int k1 = 0; k1 < j / 2; k1++)
        {
            int i2 = 0;
            while(i2 < i) 
            {
                int j2 = ai[k];
                int k2 = ai[i1];
                if(BIG_ENDIAN)
                {
                    ai[i1] = j2 >>> 8 | 0xff000000;
                    ai[k] = k2 >>> 8 | 0xff000000;
                } else
                {
                    int l2 = k2 & 0xff00ff;
                    int i3 = j2 & 0xff00ff;
                    ai[i1] = j2 & 0xff00 | (i3 << 16 | 0xff000000) | i3 >> 16;
                    ai[k] = l2 << 16 | 0xff000000 | k2 & 0xff00 | l2 >> 16;
                }
                i2++;
                k++;
                i1++;
            }
            k -= i * 2;
        }

        if(j % 2 == 1)
        {
            int l = (j / 2) * i;
            j = ((flag) ? 1 : 0);
            while(j < i) 
            {
                int j1 = ai[l];
                if(BIG_ENDIAN)
                {
                    ai[l] = j1 >>> 8 | 0xff000000;
                } else
                {
                    int l1 = j1 & 0xff00ff;
                    ai[l] = j1 & 0xff00 | (l1 << 16 | 0xff000000) | l1 >> 16;
                }
                l++;
                j++;
            }
        }
    }

    protected static int nextPowerOfTwo(int i)
    {
        int j;
        for(j = 1; j < i; j <<= 1);
        return j;
    }

    protected static String[] preprocessFragmentSource(String as[], int i)
    {
        if(!containsVersionDirective(as))
            if(i < 130)
            {
                as = preprocessShaderSource(as, new Pattern[0], new String[0], 1);
                as[0] = (new StringBuilder()).append("#version ").append(i).toString();
            } else
            {
                as = preprocessShaderSource(as, new Pattern[] {
                    Pattern.compile(String.format("(?<![0-9A-Z_a-z])(%s)(?![0-9A-Z_a-z]|\\s*\\()", new Object[] {
                        "varying|attribute"
                    })), Pattern.compile(String.format("(?<![0-9A-Z_a-z])(%s)(?![0-9A-Z_a-z]|\\s*\\()", new Object[] {
                        "texture"
                    })), Pattern.compile(String.format("(?<![0-9A-Z_a-z])(%s)(?=\\s*\\()", new Object[] {
                        "textureRect|texture2D|texture3D|textureCube"
                    })), Pattern.compile(String.format("(?<![0-9A-Z_a-z])(%s)(?![0-9A-Z_a-z]|\\s*\\()", new Object[] {
                        "gl_FragColor"
                    }))
                }, new String[] {
                    "in", "texMap", "texture", "_fragColor"
                }, 2);
                as[0] = (new StringBuilder()).append("#version ").append(i).toString();
                as[1] = "out vec4 _fragColor;";
            }
        return as;
    }

    protected static String[] preprocessShaderSource(String as[], Pattern apattern[], String as1[], int i)
    {
        String as2[] = new String[as.length + i];
        for(int j = 0; j < as.length; j++)
        {
            String s = as[j];
            String s1 = s;
            if(s.contains("#version"))
                s1 = "";
            for(int k = 0; k < apattern.length; k++)
                s1 = apattern[k].matcher(s1).replaceAll(as1[k]);

            as2[j + i] = s1;
        }

        return as2;
    }

    protected static String[] preprocessVertexSource(String as[], int i)
    {
        if(!containsVersionDirective(as))
            if(i < 130)
            {
                as = preprocessShaderSource(as, new Pattern[0], new String[0], 1);
                as[0] = (new StringBuilder()).append("#version ").append(i).toString();
            } else
            {
                as = preprocessShaderSource(as, new Pattern[] {
                    Pattern.compile(String.format("(?<![0-9A-Z_a-z])(%s)(?![0-9A-Z_a-z]|\\s*\\()", new Object[] {
                        "varying"
                    })), Pattern.compile(String.format("(?<![0-9A-Z_a-z])(%s)(?![0-9A-Z_a-z]|\\s*\\()", new Object[] {
                        "attribute"
                    })), Pattern.compile(String.format("(?<![0-9A-Z_a-z])(%s)(?![0-9A-Z_a-z]|\\s*\\()", new Object[] {
                        "texture"
                    })), Pattern.compile(String.format("(?<![0-9A-Z_a-z])(%s)(?=\\s*\\()", new Object[] {
                        "textureRect|texture2D|texture3D|textureCube"
                    }))
                }, new String[] {
                    "out", "in", "texMap", "texture"
                }, 1);
                as[0] = (new StringBuilder()).append("#version ").append(i).toString();
            }
        return as;
    }

    protected static void putByteArray(ByteBuffer bytebuffer, byte abyte0[])
    {
        if(!bytebuffer.hasArray() || bytebuffer.array() != abyte0)
        {
            bytebuffer.position(0);
            bytebuffer.put(abyte0);
            bytebuffer.rewind();
        }
    }

    protected static void putFloatArray(FloatBuffer floatbuffer, float af[])
    {
        if(!floatbuffer.hasArray() || floatbuffer.array() != af)
        {
            floatbuffer.position(0);
            floatbuffer.put(af);
            floatbuffer.rewind();
        }
    }

    protected static void putIntArray(IntBuffer intbuffer, int ai[])
    {
        if(!intbuffer.hasArray() || intbuffer.array() != ai)
        {
            intbuffer.position(0);
            intbuffer.put(ai);
            intbuffer.rewind();
        }
    }

    protected static void putShortArray(ShortBuffer shortbuffer, short aword0[])
    {
        if(!shortbuffer.hasArray() || shortbuffer.array() != aword0)
        {
            shortbuffer.position(0);
            shortbuffer.put(aword0);
            shortbuffer.rewind();
        }
    }

    protected static int qualityToSamples(int i)
    {
        boolean flag = true;
        if(i <= 1)
            i = ((flag) ? 1 : 0);
        else
            i = (i / 2) * 2;
        return i;
    }

    public static int smoothToSamples(int i)
    {
        if(i != 0) goto _L2; else goto _L1
_L1:
        int j = 1;
_L4:
        return j;
_L2:
        j = i;
        if(i == 1)
            j = 2;
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected static ByteBuffer updateByteBuffer(ByteBuffer bytebuffer, byte abyte0[], boolean flag)
    {
        if(!USE_DIRECT_BUFFERS) goto _L2; else goto _L1
_L1:
        ByteBuffer bytebuffer1;
label0:
        {
            if(bytebuffer != null)
            {
                bytebuffer1 = bytebuffer;
                if(bytebuffer.capacity() >= abyte0.length)
                    break label0;
            }
            bytebuffer1 = allocateDirectByteBuffer(abyte0.length);
        }
        bytebuffer1.position(0);
        bytebuffer1.put(abyte0);
        bytebuffer1.rewind();
_L4:
        return bytebuffer1;
_L2:
label1:
        {
            if(flag)
            {
                bytebuffer1 = ByteBuffer.wrap(abyte0);
                continue; /* Loop/switch isn't completed */
            }
            if(bytebuffer != null)
            {
                bytebuffer1 = bytebuffer;
                if(bytebuffer.capacity() >= abyte0.length)
                    break label1;
            }
            bytebuffer1 = ByteBuffer.allocate(abyte0.length);
        }
        bytebuffer1.position(0);
        bytebuffer1.put(abyte0);
        bytebuffer1.rewind();
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected static void updateByteBuffer(ByteBuffer bytebuffer, byte abyte0[], int i, int j)
    {
        if(USE_DIRECT_BUFFERS || bytebuffer.hasArray() && bytebuffer.array() != abyte0)
        {
            bytebuffer.position(i);
            bytebuffer.put(abyte0, i, j);
            bytebuffer.rewind();
        }
    }

    protected static FloatBuffer updateFloatBuffer(FloatBuffer floatbuffer, float af[], boolean flag)
    {
        if(!USE_DIRECT_BUFFERS) goto _L2; else goto _L1
_L1:
        FloatBuffer floatbuffer1;
label0:
        {
            if(floatbuffer != null)
            {
                floatbuffer1 = floatbuffer;
                if(floatbuffer.capacity() >= af.length)
                    break label0;
            }
            floatbuffer1 = allocateDirectFloatBuffer(af.length);
        }
        floatbuffer1.position(0);
        floatbuffer1.put(af);
        floatbuffer1.rewind();
_L4:
        return floatbuffer1;
_L2:
label1:
        {
            if(flag)
            {
                floatbuffer1 = FloatBuffer.wrap(af);
                continue; /* Loop/switch isn't completed */
            }
            if(floatbuffer != null)
            {
                floatbuffer1 = floatbuffer;
                if(floatbuffer.capacity() >= af.length)
                    break label1;
            }
            floatbuffer1 = FloatBuffer.allocate(af.length);
        }
        floatbuffer1.position(0);
        floatbuffer1.put(af);
        floatbuffer1.rewind();
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected static void updateFloatBuffer(FloatBuffer floatbuffer, float af[], int i, int j)
    {
        if(USE_DIRECT_BUFFERS || floatbuffer.hasArray() && floatbuffer.array() != af)
        {
            floatbuffer.position(i);
            floatbuffer.put(af, i, j);
            floatbuffer.rewind();
        }
    }

    protected static IntBuffer updateIntBuffer(IntBuffer intbuffer, int ai[], boolean flag)
    {
        if(!USE_DIRECT_BUFFERS) goto _L2; else goto _L1
_L1:
        IntBuffer intbuffer1;
label0:
        {
            if(intbuffer != null)
            {
                intbuffer1 = intbuffer;
                if(intbuffer.capacity() >= ai.length)
                    break label0;
            }
            intbuffer1 = allocateDirectIntBuffer(ai.length);
        }
        intbuffer1.position(0);
        intbuffer1.put(ai);
        intbuffer1.rewind();
_L4:
        return intbuffer1;
_L2:
label1:
        {
            if(flag)
            {
                intbuffer1 = IntBuffer.wrap(ai);
                continue; /* Loop/switch isn't completed */
            }
            if(intbuffer != null)
            {
                intbuffer1 = intbuffer;
                if(intbuffer.capacity() >= ai.length)
                    break label1;
            }
            intbuffer1 = IntBuffer.allocate(ai.length);
        }
        intbuffer1.position(0);
        intbuffer1.put(ai);
        intbuffer1.rewind();
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected static void updateIntBuffer(IntBuffer intbuffer, int ai[], int i, int j)
    {
        if(USE_DIRECT_BUFFERS || intbuffer.hasArray() && intbuffer.array() != ai)
        {
            intbuffer.position(i);
            intbuffer.put(ai, i, j);
            intbuffer.rewind();
        }
    }

    protected static ShortBuffer updateShortBuffer(ShortBuffer shortbuffer, short aword0[], boolean flag)
    {
        if(!USE_DIRECT_BUFFERS) goto _L2; else goto _L1
_L1:
        ShortBuffer shortbuffer1;
label0:
        {
            if(shortbuffer != null)
            {
                shortbuffer1 = shortbuffer;
                if(shortbuffer.capacity() >= aword0.length)
                    break label0;
            }
            shortbuffer1 = allocateDirectShortBuffer(aword0.length);
        }
        shortbuffer1.position(0);
        shortbuffer1.put(aword0);
        shortbuffer1.rewind();
_L4:
        return shortbuffer1;
_L2:
label1:
        {
            if(flag)
            {
                shortbuffer1 = ShortBuffer.wrap(aword0);
                continue; /* Loop/switch isn't completed */
            }
            if(shortbuffer != null)
            {
                shortbuffer1 = shortbuffer;
                if(shortbuffer.capacity() >= aword0.length)
                    break label1;
            }
            shortbuffer1 = ShortBuffer.allocate(aword0.length);
        }
        shortbuffer1.position(0);
        shortbuffer1.put(aword0);
        shortbuffer1.rewind();
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected static void updateShortBuffer(ShortBuffer shortbuffer, short aword0[], int i, int j)
    {
        if(USE_DIRECT_BUFFERS || shortbuffer.hasArray() && shortbuffer.array() != aword0)
        {
            shortbuffer.position(i);
            shortbuffer.put(aword0, i, j);
            shortbuffer.rewind();
        }
    }

    public void activeTexture(int i)
    {
        activeTexUnit = i - TEXTURE0;
        activeTextureImpl(i);
    }

    protected abstract void activeTextureImpl(int i);

    public abstract void attachShader(int i, int j);

    protected void beginGL()
    {
    }

    protected void beginRender()
    {
        if(sketch == null)
            sketch = graphics.parent;
        pgeomCount = geomCount;
        geomCount = 0;
        pclearColor = clearColor;
        clearColor = false;
        if(SINGLE_BUFFERED && sketch.frameCount == 1)
            restoreFirstFrame();
        if(fboLayerEnabledReq)
        {
            fboLayerEnabled = true;
            fboLayerEnabledReq = false;
        }
        if(!fboLayerEnabled) goto _L2; else goto _L1
_L1:
        if(fbolayerResetReq)
        {
            destroyFBOLayer();
            fbolayerResetReq = false;
        }
        if(!fboLayerCreated)
            createFBOLayer();
        bindFramebufferImpl(FRAMEBUFFER, glColorFbo.get(0));
        framebufferTexture2D(FRAMEBUFFER, COLOR_ATTACHMENT0, TEXTURE_2D, glColorTex.get(backTex), 0);
        if(1 < numSamples)
            bindFramebufferImpl(FRAMEBUFFER, glMultiFbo.get(0));
        if(sketch.frameCount != 0) goto _L4; else goto _L3
_L3:
        int i = graphics.backgroundColor;
        float f = (float)(i >> 24 & 0xff) / 255F;
        clearColor((float)(i >> 16 & 0xff) / 255F, (float)(i >> 8 & 0xff) / 255F, (float)(i & 0xff) / 255F, f);
        clear(COLOR_BUFFER_BIT);
_L2:
        return;
_L4:
        if(!pclearColor || !sketch.isLooping())
        {
            int j = 0;
            int k = 0;
            if(presentMode)
            {
                j = (int)presentX;
                k = (int)presentY;
            }
            float f1 = getPixelScale();
            drawTexture(TEXTURE_2D, glColorTex.get(frontTex), fboWidth, fboHeight, j, k, graphics.width, graphics.height, 0, 0, (int)((float)graphics.width * f1), (int)(f1 * (float)graphics.height), 0, 0, graphics.width, graphics.height);
        }
        if(true) goto _L2; else goto _L5
_L5:
    }

    public abstract void bindAttribLocation(int i, int j, String s);

    public abstract void bindBuffer(int i, int j);

    public void bindFramebuffer(int i, int j)
    {
        graphics.beginBindFramebuffer(i, j);
        bindFramebufferImpl(i, j);
        graphics.endBindFramebuffer(i, j);
    }

    protected abstract void bindFramebufferImpl(int i, int j);

    protected void bindFrontTexture()
    {
        usingFrontTex = true;
        if(!texturingIsEnabled(TEXTURE_2D))
            enableTexturing(TEXTURE_2D);
        bindTexture(TEXTURE_2D, glColorTex.get(frontTex));
    }

    public abstract void bindRenderbuffer(int i, int j);

    public void bindTexture(int i, int j)
    {
        bindTextureImpl(i, j);
        if(boundTextures == null)
        {
            maxTexUnits = getMaxTexUnits();
            int k = maxTexUnits;
            boundTextures = new int[k][2];
        }
        if(maxTexUnits <= activeTexUnit)
            throw new RuntimeException("Number of texture units not supported by this hardware (or driver) Read http://wiki.processing.org/w/OpenGL_Issues for help.");
        if(i != TEXTURE_2D) goto _L2; else goto _L1
_L1:
        boundTextures[activeTexUnit][0] = j;
_L4:
        return;
_L2:
        if(i == TEXTURE_RECTANGLE)
            boundTextures[activeTexUnit][1] = j;
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected abstract void bindTextureImpl(int i, int j);

    public abstract void blendColor(float f, float f1, float f2, float f3);

    public abstract void blendEquation(int i);

    public abstract void blendEquationSeparate(int i, int j);

    public abstract void blendFunc(int i, int j);

    public abstract void blendFuncSeparate(int i, int j, int k, int l);

    public abstract void blitFramebuffer(int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1, int i2, int j2);

    public abstract void bufferData(int i, int j, Buffer buffer, int k);

    public abstract void bufferSubData(int i, int j, int k, Buffer buffer);

    protected abstract boolean canDraw();

    public abstract int checkFramebufferStatus(int i);

    public abstract void clear(int i);

    protected void clearBackground(float f, float f1, float f2, float f3, boolean flag)
    {
        if(flag)
        {
            clearDepth(1.0F);
            clear(DEPTH_BUFFER_BIT);
        }
        clearColor(f, f1, f2, f3);
        clear(COLOR_BUFFER_BIT);
        if(sketch.frameCount > 0)
            clearColor = true;
    }

    public abstract void clearColor(float f, float f1, float f2, float f3);

    public abstract void clearDepth(float f);

    public abstract void clearStencil(int i);

    public abstract int clientWaitSync(long l, int i, long l1);

    public abstract void colorMask(boolean flag, boolean flag1, boolean flag2, boolean flag3);

    public abstract void compileShader(int i);

    protected boolean compiled(int i)
    {
        boolean flag = false;
        intBuffer.rewind();
        getShaderiv(i, COMPILE_STATUS, intBuffer);
        if(intBuffer.get(0) != 0)
            flag = true;
        return flag;
    }

    public abstract void compressedTexImage2D(int i, int j, int k, int l, int i1, int j1, int k1, 
            Buffer buffer);

    public abstract void compressedTexSubImage2D(int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1, Buffer buffer);

    protected boolean contextIsCurrent(int i)
    {
        boolean flag;
        if(i == -1 || i == glContext)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public abstract void copyTexImage2D(int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1);

    public abstract void copyTexSubImage2D(int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1);

    protected void copyToTexture(int i, int j, int k, int l, int i1, int j1, int k1, 
            IntBuffer intbuffer)
    {
        activeTexture(TEXTURE0);
        boolean flag;
        if(!texturingIsEnabled(i))
        {
            enableTexturing(i);
            flag = true;
        } else
        {
            flag = false;
        }
        bindTexture(i, k);
        texSubImage2D(i, 0, l, i1, j1, k1, j, UNSIGNED_BYTE, intbuffer);
        bindTexture(i, 0);
        if(flag)
            disableTexturing(i);
    }

    protected void copyToTexture(int i, int j, int k, int l, int i1, int j1, int k1, 
            int ai[])
    {
        copyToTexture(i, j, k, l, i1, j1, k1, IntBuffer.wrap(ai));
    }

    protected int createEmptyContext()
    {
        return -1;
    }

    protected abstract FontOutline createFontOutline(char c, Object obj);

    public abstract int createProgram();

    protected int createProgram(int i, int j)
    {
        int k = createProgram();
        int l = k;
        if(k != 0)
        {
            attachShader(k, i);
            attachShader(k, j);
            linkProgram(k);
            l = k;
            if(!linked(k))
            {
                System.err.println("Could not link program: ");
                System.err.println(getProgramInfoLog(k));
                deleteProgram(k);
                l = 0;
            }
        }
        return l;
    }

    public abstract int createShader(int i);

    protected int createShader(int i, String s)
    {
        int j = createShader(i);
        int k = j;
        if(j != 0)
        {
            shaderSource(j, s);
            compileShader(j);
            k = j;
            if(!compiled(j))
            {
                System.err.println((new StringBuilder()).append("Could not compile shader ").append(i).append(":").toString());
                System.err.println(getShaderInfoLog(j));
                deleteShader(j);
                k = 0;
            }
        }
        return k;
    }

    protected abstract Tessellator createTessellator(TessellatorCallback tessellatorcallback);

    public abstract void cullFace(int i);

    public abstract void deleteBuffers(int i, IntBuffer intbuffer);

    public abstract void deleteFramebuffers(int i, IntBuffer intbuffer);

    public abstract void deleteProgram(int i);

    public abstract void deleteRenderbuffers(int i, IntBuffer intbuffer);

    public abstract void deleteShader(int i);

    public abstract void deleteSync(long l);

    public abstract void deleteTextures(int i, IntBuffer intbuffer);

    public abstract void depthFunc(int i);

    public abstract void depthMask(boolean flag);

    public abstract void depthRangef(float f, float f1);

    protected void destroyFBOLayer()
    {
        if(threadIsCurrent() && fboLayerCreated)
        {
            deleteFramebuffers(1, glColorFbo);
            deleteTextures(2, glColorTex);
            deleteRenderbuffers(1, glDepthStencil);
            deleteRenderbuffers(1, glDepth);
            deleteRenderbuffers(1, glStencil);
            deleteFramebuffers(1, glMultiFbo);
            deleteRenderbuffers(1, glMultiColor);
            deleteRenderbuffers(1, glMultiDepthStencil);
            deleteRenderbuffers(1, glMultiDepth);
            deleteRenderbuffers(1, glMultiStencil);
        }
        fboLayerCreated = false;
    }

    public abstract void detachShader(int i, int j);

    public abstract void disable(int i);

    public void disableFBOLayer()
    {
        fboLayerDisableReq = true;
    }

    protected void disableTexturing(int i)
    {
        if(i != TEXTURE_2D) goto _L2; else goto _L1
_L1:
        texturingTargets[0] = false;
_L4:
        return;
_L2:
        if(i == TEXTURE_RECTANGLE)
            texturingTargets[1] = false;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public abstract void disableVertexAttribArray(int i);

    public void dispose()
    {
        graphics = null;
        sketch = null;
        destroyFBOLayer();
    }

    public void drawArrays(int i, int j, int k)
    {
        geomCount = geomCount + k;
        drawArraysImpl(i, j, k);
    }

    public abstract void drawArraysImpl(int i, int j, int k);

    public abstract void drawBuffer(int i);

    public void drawElements(int i, int j, int k, int l)
    {
        geomCount = geomCount + j;
        drawElementsImpl(i, j, k, l);
    }

    public abstract void drawElementsImpl(int i, int j, int k, int l);

    public void drawTexture(int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1)
    {
        drawTexture(i, j, k, l, 0, 0, k, l, 1, i1, j1, k1, l1, i1, j1, k1, l1);
    }

    public void drawTexture(int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1, int i2, int j2, int k2, int l2, int i3, int j3, 
            int k3, int l3)
    {
        drawTexture(i, j, k, l, i1, j1, k1, l1, (int)getPixelScale(), i2, j2, k2, l2, i3, j3, k3, l3);
    }

    public void drawTexture(int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1, int i2, int j2, int k2, int l2, int i3, int j3, 
            int k3, int l3, int i4)
    {
        if(i != TEXTURE_2D) goto _L2; else goto _L1
_L1:
        drawTexture2D(j, k, l, i1, j1, k1, l1, i2, j2, k2, l2, i3, j3, k3, l3, i4);
_L4:
        return;
_L2:
        if(i == TEXTURE_RECTANGLE)
            drawTextureRect(j, k, l, i1, j1, k1, l1, i2, j2, k2, l2, i3, j3, k3, l3, i4);
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected void drawTexture2D(int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1, int i2, int j2, int k2, int l2, int i3, int j3, 
            int k3, int l3)
    {
        PGL pgl = initTex2DShader();
        if(pgl.tex2DShaderProgram > 0)
        {
            boolean flag = getDepthTest();
            disable(DEPTH_TEST);
            boolean flag1 = getDepthWriteMask();
            depthMask(false);
            viewBuffer.rewind();
            getIntegerv(VIEWPORT, viewBuffer);
            viewportImpl(l1 * l, l1 * i1, l1 * j1, l1 * k1);
            useProgram(pgl.tex2DShaderProgram);
            enableVertexAttribArray(pgl.tex2DVertLoc);
            enableVertexAttribArray(pgl.tex2DTCoordLoc);
            texCoords[0] = (2.0F * (float)i3) / (float)j1 - 1.0F;
            texCoords[1] = (2.0F * (float)j3) / (float)k1 - 1.0F;
            texCoords[2] = (float)i2 / (float)j;
            texCoords[3] = (float)j2 / (float)k;
            texCoords[4] = (2.0F * (float)k3) / (float)j1 - 1.0F;
            texCoords[5] = (2.0F * (float)j3) / (float)k1 - 1.0F;
            texCoords[6] = (float)k2 / (float)j;
            texCoords[7] = (float)j2 / (float)k;
            texCoords[8] = (2.0F * (float)i3) / (float)j1 - 1.0F;
            texCoords[9] = (2.0F * (float)l3) / (float)k1 - 1.0F;
            texCoords[10] = (float)i2 / (float)j;
            texCoords[11] = (float)l2 / (float)k;
            texCoords[12] = (2.0F * (float)k3) / (float)j1 - 1.0F;
            texCoords[13] = (2.0F * (float)l3) / (float)k1 - 1.0F;
            texCoords[14] = (float)k2 / (float)j;
            texCoords[15] = (float)l2 / (float)k;
            texData.rewind();
            texData.put(texCoords);
            activeTexture(TEXTURE0);
            if(!texturingIsEnabled(TEXTURE_2D))
            {
                enableTexturing(TEXTURE_2D);
                j = 1;
            } else
            {
                j = 0;
            }
            bindTexture(TEXTURE_2D, i);
            uniform1i(pgl.tex2DSamplerLoc, 0);
            texData.position(0);
            bindBuffer(ARRAY_BUFFER, pgl.tex2DGeoVBO);
            bufferData(ARRAY_BUFFER, SIZEOF_FLOAT * 16, texData, STATIC_DRAW);
            vertexAttribPointer(pgl.tex2DVertLoc, 2, FLOAT, false, SIZEOF_FLOAT * 4, 0);
            vertexAttribPointer(pgl.tex2DTCoordLoc, 2, FLOAT, false, SIZEOF_FLOAT * 4, SIZEOF_FLOAT * 2);
            drawArrays(TRIANGLE_STRIP, 0, 4);
            bindBuffer(ARRAY_BUFFER, 0);
            bindTexture(TEXTURE_2D, 0);
            if(j != 0)
                disableTexturing(TEXTURE_2D);
            disableVertexAttribArray(pgl.tex2DVertLoc);
            disableVertexAttribArray(pgl.tex2DTCoordLoc);
            useProgram(0);
            if(flag)
                enable(DEPTH_TEST);
            else
                disable(DEPTH_TEST);
            depthMask(flag1);
            viewportImpl(viewBuffer.get(0), viewBuffer.get(1), viewBuffer.get(2), viewBuffer.get(3));
        }
    }

    protected void drawTextureRect(int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1, int i2, int j2, int k2, int l2, int i3, int j3, 
            int k3, int l3)
    {
        PGL pgl = initTexRectShader();
        if(texData == null)
            texData = allocateDirectFloatBuffer(texCoords.length);
        if(pgl.texRectShaderProgram > 0)
        {
            boolean flag = getDepthTest();
            disable(DEPTH_TEST);
            boolean flag1 = getDepthWriteMask();
            depthMask(false);
            viewBuffer.rewind();
            getIntegerv(VIEWPORT, viewBuffer);
            viewportImpl(l1 * l, l1 * i1, l1 * j1, l1 * k1);
            useProgram(pgl.texRectShaderProgram);
            enableVertexAttribArray(pgl.texRectVertLoc);
            enableVertexAttribArray(pgl.texRectTCoordLoc);
            texCoords[0] = (2.0F * (float)i3) / (float)j1 - 1.0F;
            texCoords[1] = (2.0F * (float)j3) / (float)k1 - 1.0F;
            texCoords[2] = i2;
            texCoords[3] = j2;
            texCoords[4] = (2.0F * (float)k3) / (float)j1 - 1.0F;
            texCoords[5] = (2.0F * (float)j3) / (float)k1 - 1.0F;
            texCoords[6] = k2;
            texCoords[7] = j2;
            texCoords[8] = (2.0F * (float)i3) / (float)j1 - 1.0F;
            texCoords[9] = (2.0F * (float)l3) / (float)k1 - 1.0F;
            texCoords[10] = i2;
            texCoords[11] = l2;
            texCoords[12] = (2.0F * (float)k3) / (float)j1 - 1.0F;
            texCoords[13] = (2.0F * (float)l3) / (float)k1 - 1.0F;
            texCoords[14] = k2;
            texCoords[15] = l2;
            texData.rewind();
            texData.put(texCoords);
            activeTexture(TEXTURE0);
            if(!texturingIsEnabled(TEXTURE_RECTANGLE))
            {
                enableTexturing(TEXTURE_RECTANGLE);
                j = 1;
            } else
            {
                j = 0;
            }
            bindTexture(TEXTURE_RECTANGLE, i);
            uniform1i(pgl.texRectSamplerLoc, 0);
            texData.position(0);
            bindBuffer(ARRAY_BUFFER, pgl.texRectGeoVBO);
            bufferData(ARRAY_BUFFER, SIZEOF_FLOAT * 16, texData, STATIC_DRAW);
            vertexAttribPointer(pgl.texRectVertLoc, 2, FLOAT, false, SIZEOF_FLOAT * 4, 0);
            vertexAttribPointer(pgl.texRectTCoordLoc, 2, FLOAT, false, SIZEOF_FLOAT * 4, SIZEOF_FLOAT * 2);
            drawArrays(TRIANGLE_STRIP, 0, 4);
            bindBuffer(ARRAY_BUFFER, 0);
            bindTexture(TEXTURE_RECTANGLE, 0);
            if(j != 0)
                disableTexturing(TEXTURE_RECTANGLE);
            disableVertexAttribArray(pgl.texRectVertLoc);
            disableVertexAttribArray(pgl.texRectTCoordLoc);
            useProgram(0);
            if(flag)
                enable(DEPTH_TEST);
            else
                disable(DEPTH_TEST);
            depthMask(flag1);
            viewportImpl(viewBuffer.get(0), viewBuffer.get(1), viewBuffer.get(2), viewBuffer.get(3));
        }
    }

    public abstract void enable(int i);

    public void enableFBOLayer()
    {
        fboLayerEnabledReq = true;
    }

    protected void enableTexturing(int i)
    {
        if(i != TEXTURE_2D) goto _L2; else goto _L1
_L1:
        texturingTargets[0] = true;
_L4:
        return;
_L2:
        if(i == TEXTURE_RECTANGLE)
            texturingTargets[1] = true;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public abstract void enableVertexAttribArray(int i);

    protected void endGL()
    {
    }

    protected void endRender(int i)
    {
        if(!fboLayerEnabled) goto _L2; else goto _L1
_L1:
        syncBackTexture();
        bindFramebufferImpl(FRAMEBUFFER, 0);
        float f8;
        int j2;
        if(presentMode)
        {
            float f = (float)(i >> 24 & 0xff) / 255F;
            float f2 = (float)(i >> 16 & 0xff) / 255F;
            float f4 = (float)(i >> 8 & 0xff) / 255F;
            float f6 = (float)(i & 0xff) / 255F;
            clearDepth(1.0F);
            clearColor(f2, f4, f6, f);
            clear(COLOR_BUFFER_BIT | DEPTH_BUFFER_BIT);
            if(showStopButton)
            {
                if(closeButtonTex == null)
                {
                    closeButtonTex = allocateIntBuffer(1);
                    genTextures(1, closeButtonTex);
                    bindTexture(TEXTURE_2D, closeButtonTex.get(0));
                    texParameteri(TEXTURE_2D, TEXTURE_MIN_FILTER, NEAREST);
                    texParameteri(TEXTURE_2D, TEXTURE_MAG_FILTER, NEAREST);
                    texParameteri(TEXTURE_2D, TEXTURE_WRAP_S, CLAMP_TO_EDGE);
                    texParameteri(TEXTURE_2D, TEXTURE_WRAP_T, CLAMP_TO_EDGE);
                    texImage2D(TEXTURE_2D, 0, RGBA, stopButtonWidth, stopButtonHeight, 0, RGBA, UNSIGNED_BYTE, null);
                    int ai[] = new int[closeButtonPix.length];
                    PApplet.arrayCopy(closeButtonPix, ai);
                    float f7 = (float)(stopButtonColor >> 24 & 0xff) / 255F;
                    float f3 = (float)(stopButtonColor >> 16 & 0xff) / 255F;
                    float f5 = (float)(stopButtonColor >> 8 & 0xff) / 255F;
                    float f1 = (float)(stopButtonColor >> 0 & 0xff) / 255F;
                    for(i = 0; i < ai.length; i++)
                    {
                        int j = closeButtonPix[i];
                        int l = (int)((float)(j >> 24 & 0xff) * f7);
                        int j1 = (int)((float)(j >> 16 & 0xff) * f3);
                        int l1 = (int)((float)(j >> 8 & 0xff) * f5);
                        ai[i] = javaToNativeARGB((int)((float)(j >> 0 & 0xff) * f1) | (l << 24 | j1 << 16 | l1 << 8));
                    }

                    IntBuffer intbuffer = allocateIntBuffer(ai);
                    copyToTexture(TEXTURE_2D, RGBA, closeButtonTex.get(0), 0, 0, stopButtonWidth, stopButtonHeight, intbuffer);
                    bindTexture(TEXTURE_2D, 0);
                }
                int k2 = TEXTURE_2D;
                int l2 = closeButtonTex.get(0);
                int i3 = stopButtonWidth;
                int j3 = stopButtonHeight;
                int k1 = stopButtonX;
                int i2 = stopButtonWidth;
                int k3 = closeButtonY;
                int l3 = stopButtonHeight;
                int i1 = stopButtonHeight;
                i = stopButtonWidth;
                int i4 = stopButtonX;
                int j4 = closeButtonY;
                int k = stopButtonX;
                int k4 = stopButtonWidth;
                int l4 = closeButtonY;
                drawTexture(k2, l2, i3, j3, 0, 0, i2 + k1, l3 + k3, 0, i1, i, 0, i4, j4, k4 + k, stopButtonHeight + l4);
            }
        } else
        {
            clearDepth(1.0F);
            clearColor(0.0F, 0.0F, 0.0F, 0.0F);
            clear(COLOR_BUFFER_BIT | DEPTH_BUFFER_BIT);
        }
        disable(BLEND);
        j2 = 0;
        i = 0;
        if(presentMode)
        {
            j2 = (int)presentX;
            i = (int)presentY;
        }
        f8 = getPixelScale();
        drawTexture(TEXTURE_2D, glColorTex.get(backTex), fboWidth, fboHeight, j2, i, graphics.width, graphics.height, 0, 0, (int)((float)graphics.width * f8), (int)(f8 * (float)graphics.height), 0, 0, graphics.width, graphics.height);
        i = frontTex;
        frontTex = backTex;
        backTex = i;
        if(fboLayerDisableReq)
        {
            fboLayerEnabled = false;
            fboLayerDisableReq = false;
        }
_L4:
        return;
_L2:
        if(SINGLE_BUFFERED && sketch.frameCount == 0)
            saveFirstFrame();
        if(!clearColor && sketch.frameCount > 0 || !sketch.isLooping())
        {
            enableFBOLayer();
            if(SINGLE_BUFFERED)
                createFBOLayer();
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public abstract String errorString(int i);

    public abstract long fenceSync(int i, int j);

    public abstract void finish();

    public abstract void flush();

    public abstract void framebufferRenderbuffer(int i, int j, int k, int l);

    public abstract void framebufferTexture2D(int i, int j, int k, int l, int i1);

    public abstract void frontFace(int i);

    public abstract void genBuffers(int i, IntBuffer intbuffer);

    public abstract void genFramebuffers(int i, IntBuffer intbuffer);

    public abstract void genRenderbuffers(int i, IntBuffer intbuffer);

    public abstract void genTextures(int i, IntBuffer intbuffer);

    public abstract void generateMipmap(int i);

    public abstract String getActiveAttrib(int i, int j, IntBuffer intbuffer, IntBuffer intbuffer1);

    public abstract String getActiveUniform(int i, int j, IntBuffer intbuffer, IntBuffer intbuffer1);

    public abstract void getAttachedShaders(int i, int j, IntBuffer intbuffer, IntBuffer intbuffer1);

    public abstract int getAttribLocation(int i, String s);

    public abstract void getBooleanv(int i, IntBuffer intbuffer);

    public abstract void getBufferParameteriv(int i, int j, IntBuffer intbuffer);

    protected int getColorValue(int i, int j)
    {
        if(colorBuffer == null)
            colorBuffer = IntBuffer.allocate(1);
        colorBuffer.rewind();
        readPixels(i, graphics.height - j - 1, 1, 1, RGBA, UNSIGNED_BYTE, colorBuffer);
        return colorBuffer.get();
    }

    protected int getCurrentContext()
    {
        return glContext;
    }

    protected int getDefaultDrawBuffer()
    {
        int i;
        if(fboLayerEnabled)
            i = COLOR_ATTACHMENT0;
        else
            i = BACK;
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

    protected abstract int getDepthBits();

    protected boolean getDepthTest()
    {
        boolean flag = false;
        intBuffer.rewind();
        getBooleanv(DEPTH_TEST, intBuffer);
        if(intBuffer.get(0) != 0)
            flag = true;
        return flag;
    }

    protected float getDepthValue(int i, int j)
    {
        if(depthBuffer == null)
            depthBuffer = FloatBuffer.allocate(1);
        depthBuffer.rewind();
        readPixels(i, graphics.height - j - 1, 1, 1, DEPTH_COMPONENT, FLOAT, depthBuffer);
        return depthBuffer.get(0);
    }

    protected boolean getDepthWriteMask()
    {
        boolean flag = false;
        intBuffer.rewind();
        getBooleanv(DEPTH_WRITEMASK, intBuffer);
        if(intBuffer.get(0) != 0)
            flag = true;
        return flag;
    }

    protected abstract Object getDerivedFont(Object obj, float f);

    protected int getDrawFramebuffer()
    {
        int i = 0;
        if(fboLayerEnabled)
            if(1 < numSamples)
                i = glMultiFbo.get(0);
            else
                i = glColorFbo.get(0);
        return i;
    }

    public abstract int getError();

    public abstract void getFloatv(int i, FloatBuffer floatbuffer);

    protected abstract int getFontAscent(Object obj);

    protected abstract int getFontDescent(Object obj);

    public abstract void getFramebufferAttachmentParameteriv(int i, int j, int k, IntBuffer intbuffer);

    protected abstract void getGL(PGL pgl);

    protected abstract int getGLSLVersion();

    protected int[] getGLVersion()
    {
        String s = getString(VERSION).trim().toLowerCase();
        int i = s.indexOf("opengl es");
        String s1 = s;
        if(i >= 0)
            s1 = s.substring("opengl es".length() + i).trim();
        int ai[] = new int[3];
        int[] _tmp = ai;
        ai[0] = 0;
        ai[1] = 0;
        ai[2] = 0;
        String as[] = s1.split(" ");
        i = 0;
        do
        {
label0:
            {
                if(i < as.length)
                {
                    if(as[i].indexOf(".") <= 0)
                        break label0;
                    as = as[i].split("\\.");
                    try
                    {
                        ai[0] = Integer.parseInt(as[0]);
                    }
                    catch(NumberFormatException numberformatexception2) { }
                    if(1 < as.length)
                        try
                        {
                            ai[1] = Integer.parseInt(as[1]);
                        }
                        catch(NumberFormatException numberformatexception1) { }
                    if(2 < as.length)
                        try
                        {
                            ai[2] = Integer.parseInt(as[2]);
                        }
                        catch(NumberFormatException numberformatexception) { }
                }
                return ai;
            }
            i++;
        } while(true);
    }

    public abstract void getIntegerv(int i, IntBuffer intbuffer);

    protected int getMaxTexUnits()
    {
        intBuffer.rewind();
        getIntegerv(MAX_TEXTURE_IMAGE_UNITS, intBuffer);
        return intBuffer.get(0);
    }

    public abstract Object getNative();

    protected abstract float getPixelScale();

    public abstract String getProgramInfoLog(int i);

    public abstract void getProgramiv(int i, int j, IntBuffer intbuffer);

    protected int getReadFramebuffer()
    {
        int i = 0;
        if(fboLayerEnabled)
            i = glColorFbo.get(0);
        return i;
    }

    public abstract void getRenderbufferParameteriv(int i, int j, IntBuffer intbuffer);

    public abstract String getShaderInfoLog(int i);

    public abstract void getShaderPrecisionFormat(int i, int j, IntBuffer intbuffer, IntBuffer intbuffer1);

    public abstract String getShaderSource(int i);

    public abstract void getShaderiv(int i, int j, IntBuffer intbuffer);

    protected abstract int getStencilBits();

    protected byte getStencilValue(int i, int j)
    {
        if(stencilBuffer == null)
            stencilBuffer = ByteBuffer.allocate(1);
        stencilBuffer.rewind();
        readPixels(i, graphics.height - j - 1, 1, 1, STENCIL_INDEX, UNSIGNED_BYTE, stencilBuffer);
        return stencilBuffer.get(0);
    }

    public abstract String getString(int i);

    public abstract void getTexParameterfv(int i, int j, FloatBuffer floatbuffer);

    public abstract void getTexParameteriv(int i, int j, IntBuffer intbuffer);

    protected abstract int getTextWidth(Object obj, char ac[], int i, int j);

    public abstract int getUniformLocation(int i, String s);

    public abstract void getUniformfv(int i, int j, FloatBuffer floatbuffer);

    public abstract void getUniformiv(int i, int j, IntBuffer intbuffer);

    public abstract void getVertexAttribPointerv(int i, int j, ByteBuffer bytebuffer);

    public abstract void getVertexAttribfv(int i, int j, FloatBuffer floatbuffer);

    public abstract void getVertexAttribiv(int i, int j, IntBuffer intbuffer);

    protected boolean hasAnisoSamplingSupport()
    {
        boolean flag = true;
        boolean flag1 = flag;
        if(getGLVersion()[0] < 3)
            if(-1 < getString(EXTENSIONS).indexOf("_texture_filter_anisotropic"))
                flag1 = flag;
            else
                flag1 = false;
        return flag1;
    }

    protected boolean hasAutoMipmapGenSupport()
    {
        boolean flag = true;
        boolean flag1 = flag;
        if(getGLVersion()[0] < 3)
            if(-1 < getString(EXTENSIONS).indexOf("_generate_mipmap"))
                flag1 = flag;
            else
                flag1 = false;
        return flag1;
    }

    protected boolean hasDrawBuffer()
    {
        boolean flag;
        int ai[];
        flag = true;
        ai = getGLVersion();
        if(!isES()) goto _L2; else goto _L1
_L1:
        if(ai[0] < 3)
            flag = false;
_L4:
        return flag;
_L2:
        if(ai[0] < 2)
            flag = false;
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected boolean hasFBOs()
    {
        boolean flag = true;
        boolean flag1 = flag;
        if(getGLVersion()[0] < 2)
        {
            String s = getString(EXTENSIONS);
            if(s.indexOf("_framebuffer_object") != -1 && s.indexOf("_vertex_shader") != -1 && s.indexOf("_shader_objects") != -1 && s.indexOf("_shading_language") != -1)
                flag1 = flag;
            else
                flag1 = false;
        }
        return flag1;
    }

    protected boolean hasFboMultisampleSupport()
    {
        boolean flag = true;
        boolean flag1 = flag;
        if(getGLVersion()[0] < 3)
            if(-1 < getString(EXTENSIONS).indexOf("_framebuffer_multisample"))
                flag1 = flag;
            else
                flag1 = false;
        return flag1;
    }

    protected boolean hasNpotTexSupport()
    {
        boolean flag;
        boolean flag1;
        flag = true;
        flag1 = flag;
        if(getGLVersion()[0] >= 3) goto _L2; else goto _L1
_L1:
        String s = getString(EXTENSIONS);
        if(!isES()) goto _L4; else goto _L3
_L3:
        if(-1 < s.indexOf("_texture_npot"))
            flag1 = flag;
        else
            flag1 = false;
_L2:
        return flag1;
_L4:
        flag1 = flag;
        if(-1 >= s.indexOf("_texture_non_power_of_two"))
            flag1 = false;
        if(true) goto _L2; else goto _L5
_L5:
    }

    protected boolean hasPBOs()
    {
        boolean flag;
        boolean flag1;
        int ai[];
        flag = true;
        flag1 = false;
        ai = getGLVersion();
        if(!isES()) goto _L2; else goto _L1
_L1:
        if(ai[0] < 3)
            flag = false;
_L4:
        return flag;
_L2:
        if(ai[0] <= 2)
        {
            flag = flag1;
            if(ai[0] != 2)
                continue; /* Loop/switch isn't completed */
            flag = flag1;
            if(ai[1] < 1)
                continue; /* Loop/switch isn't completed */
        }
        flag = true;
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected boolean hasPackedDepthStencilSupport()
    {
        boolean flag = true;
        boolean flag1 = flag;
        if(getGLVersion()[0] < 3)
            if(-1 < getString(EXTENSIONS).indexOf("_packed_depth_stencil"))
                flag1 = flag;
            else
                flag1 = false;
        return flag1;
    }

    protected boolean hasReadBuffer()
    {
        boolean flag;
        int ai[];
        flag = true;
        ai = getGLVersion();
        if(!isES()) goto _L2; else goto _L1
_L1:
        if(ai[0] < 3)
            flag = false;
_L4:
        return flag;
_L2:
        if(ai[0] < 2)
            flag = false;
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected boolean hasShaders()
    {
        boolean flag = true;
        boolean flag1 = flag;
        if(getGLVersion()[0] < 2)
        {
            String s = getString(EXTENSIONS);
            if(s.indexOf("_fragment_shader") != -1 && s.indexOf("_vertex_shader") != -1 && s.indexOf("_shader_objects") != -1 && s.indexOf("_shading_language") != -1)
                flag1 = flag;
            else
                flag1 = false;
        }
        return flag1;
    }

    protected boolean hasSynchronization()
    {
        boolean flag;
        boolean flag1;
        int ai[];
        flag = true;
        flag1 = false;
        ai = getGLVersion();
        if(!isES()) goto _L2; else goto _L1
_L1:
        if(ai[0] < 3)
            flag = false;
_L4:
        return flag;
_L2:
        if(ai[0] <= 3)
        {
            flag = flag1;
            if(ai[0] != 3)
                continue; /* Loop/switch isn't completed */
            flag = flag1;
            if(ai[1] < 2)
                continue; /* Loop/switch isn't completed */
        }
        flag = true;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public abstract void hint(int i, int j);

    protected abstract void initFBOLayer();

    public void initPresentMode(float f, float f1, int i)
    {
        boolean flag = true;
        presentMode = true;
        if(i == 0)
            flag = false;
        showStopButton = flag;
        stopButtonColor = i;
        presentX = f;
        presentY = f1;
        enableFBOLayer();
    }

    protected abstract void initSurface(int i);

    protected PGL initTex2DShader()
    {
        PGL pgl;
        if(primaryPGL)
            pgl = this;
        else
            pgl = graphics.getPrimaryPGL();
        if(!pgl.loadedTex2DShader || pgl.tex2DShaderContext != pgl.glContext)
        {
            String s = PApplet.join(preprocessVertexSource(texVertShaderSource, getGLSLVersion()), "\n");
            String s1 = PApplet.join(preprocessFragmentSource(tex2DFragShaderSource, getGLSLVersion()), "\n");
            pgl.tex2DVertShader = createShader(VERTEX_SHADER, s);
            pgl.tex2DFragShader = createShader(FRAGMENT_SHADER, s1);
            if(pgl.tex2DVertShader > 0 && pgl.tex2DFragShader > 0)
                pgl.tex2DShaderProgram = createProgram(pgl.tex2DVertShader, pgl.tex2DFragShader);
            if(pgl.tex2DShaderProgram > 0)
            {
                pgl.tex2DVertLoc = getAttribLocation(pgl.tex2DShaderProgram, "position");
                pgl.tex2DTCoordLoc = getAttribLocation(pgl.tex2DShaderProgram, "texCoord");
                pgl.tex2DSamplerLoc = getUniformLocation(pgl.tex2DShaderProgram, "texMap");
            }
            pgl.loadedTex2DShader = true;
            pgl.tex2DShaderContext = pgl.glContext;
            genBuffers(1, intBuffer);
            pgl.tex2DGeoVBO = intBuffer.get(0);
            bindBuffer(ARRAY_BUFFER, pgl.tex2DGeoVBO);
            bufferData(ARRAY_BUFFER, SIZEOF_FLOAT * 16, null, STATIC_DRAW);
        }
        if(texData == null)
            texData = allocateDirectFloatBuffer(texCoords.length);
        return pgl;
    }

    protected PGL initTexRectShader()
    {
        PGL pgl;
        if(primaryPGL)
            pgl = this;
        else
            pgl = graphics.getPrimaryPGL();
        if(!pgl.loadedTexRectShader || pgl.texRectShaderContext != pgl.glContext)
        {
            String s = PApplet.join(preprocessVertexSource(texVertShaderSource, getGLSLVersion()), "\n");
            String s1 = PApplet.join(preprocessFragmentSource(texRectFragShaderSource, getGLSLVersion()), "\n");
            pgl.texRectVertShader = createShader(VERTEX_SHADER, s);
            pgl.texRectFragShader = createShader(FRAGMENT_SHADER, s1);
            if(pgl.texRectVertShader > 0 && pgl.texRectFragShader > 0)
                pgl.texRectShaderProgram = createProgram(pgl.texRectVertShader, pgl.texRectFragShader);
            if(pgl.texRectShaderProgram > 0)
            {
                pgl.texRectVertLoc = getAttribLocation(pgl.texRectShaderProgram, "position");
                pgl.texRectTCoordLoc = getAttribLocation(pgl.texRectShaderProgram, "texCoord");
                pgl.texRectSamplerLoc = getUniformLocation(pgl.texRectShaderProgram, "texMap");
            }
            pgl.loadedTexRectShader = true;
            pgl.texRectShaderContext = pgl.glContext;
            genBuffers(1, intBuffer);
            pgl.texRectGeoVBO = intBuffer.get(0);
            bindBuffer(ARRAY_BUFFER, pgl.texRectGeoVBO);
            bufferData(ARRAY_BUFFER, SIZEOF_FLOAT * 16, null, STATIC_DRAW);
        }
        return pgl;
    }

    protected void initTexture(int i, int j, int k, int l)
    {
        initTexture(i, j, k, l, 0);
    }

    protected void initTexture(int i, int j, int k, int l, int i1)
    {
        int ai[] = new int[256];
        Arrays.fill(ai, javaToNativeARGB(i1));
        IntBuffer intbuffer = allocateDirectIntBuffer(256);
        intbuffer.put(ai);
        intbuffer.rewind();
        for(i1 = 0; i1 < l; i1 += 16)
        {
            int j1 = PApplet.min(16, l - i1);
            for(int k1 = 0; k1 < k; k1 += 16)
                texSubImage2D(i, 0, k1, i1, PApplet.min(16, k - k1), j1, j, UNSIGNED_BYTE, intbuffer);

        }

    }

    public boolean insideStopButton(float f, float f1)
    {
        boolean flag = false;
        if(showStopButton) goto _L2; else goto _L1
_L1:
        boolean flag1 = flag;
_L4:
        return flag1;
_L2:
        flag1 = flag;
        if((float)stopButtonX < f)
        {
            flag1 = flag;
            if(f < (float)(stopButtonX + stopButtonWidth))
            {
                flag1 = flag;
                if((float)(-(closeButtonY + stopButtonHeight)) < f1)
                {
                    flag1 = flag;
                    if(f1 < (float)(-closeButtonY))
                        flag1 = true;
                }
            }
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public abstract void isBuffer(int i);

    protected boolean isES()
    {
        return getString(VERSION).trim().toLowerCase().contains("opengl es");
    }

    public abstract boolean isEnabled(int i);

    protected boolean isFBOBacked()
    {
        return fboLayerEnabled;
    }

    public abstract boolean isFramebuffer(int i);

    protected boolean isMultisampled()
    {
        boolean flag = true;
        if(1 >= numSamples)
            flag = false;
        return flag;
    }

    public abstract boolean isProgram(int i);

    public abstract boolean isRenderbuffer(int i);

    public abstract boolean isShader(int i);

    public abstract boolean isTexture(int i);

    public abstract void lineWidth(float f);

    public abstract void linkProgram(int i);

    protected boolean linked(int i)
    {
        boolean flag = false;
        intBuffer.rewind();
        getProgramiv(i, LINK_STATUS, intBuffer);
        if(intBuffer.get(0) != 0)
            flag = true;
        return flag;
    }

    protected String[] loadFragmentShader(String s)
    {
        return sketch.loadStrings(s);
    }

    protected String[] loadFragmentShader(String s, int i)
    {
        return loadFragmentShader(s);
    }

    protected String[] loadFragmentShader(URL url)
    {
        String as[] = PApplet.loadStrings(url.openStream());
        url = as;
_L2:
        return url;
        IOException ioexception;
        ioexception;
        PGraphics.showException((new StringBuilder()).append("Cannot load fragment shader ").append(url.getFile()).toString());
        url = null;
        if(true) goto _L2; else goto _L1
_L1:
    }

    protected String[] loadFragmentShader(URL url, int i)
    {
        return loadFragmentShader(url);
    }

    protected String[] loadVertexShader(String s)
    {
        return sketch.loadStrings(s);
    }

    protected String[] loadVertexShader(String s, int i)
    {
        return loadVertexShader(s);
    }

    protected String[] loadVertexShader(URL url)
    {
        String as[] = PApplet.loadStrings(url.openStream());
        url = as;
_L2:
        return url;
        IOException ioexception;
        ioexception;
        PGraphics.showException((new StringBuilder()).append("Cannot load vertex shader ").append(url.getFile()).toString());
        url = null;
        if(true) goto _L2; else goto _L1
_L1:
    }

    protected String[] loadVertexShader(URL url, int i)
    {
        return loadVertexShader(url);
    }

    public abstract ByteBuffer mapBuffer(int i, int j);

    public abstract ByteBuffer mapBufferRange(int i, int j, int k, int l);

    protected int maxSamples()
    {
        intBuffer.rewind();
        getIntegerv(MAX_SAMPLES, intBuffer);
        return intBuffer.get(0);
    }

    public abstract void pixelStorei(int i, int j);

    public abstract void polygonOffset(float f, float f1);

    public boolean presentMode()
    {
        return presentMode;
    }

    public float presentX()
    {
        return presentX;
    }

    public float presentY()
    {
        return presentY;
    }

    public abstract void readBuffer(int i);

    public void readPixels(int i, int j, int k, int l, int i1, int j1, long l1)
    {
        boolean flag = false;
        boolean flag1;
        boolean flag2;
        if(isMultisampled() || graphics.offscreenMultisample)
            flag1 = true;
        else
            flag1 = false;
        flag2 = graphics.getHint(10);
        if(i1 == STENCIL_INDEX || i1 == DEPTH_COMPONENT || i1 == DEPTH_STENCIL)
            flag = true;
        if(flag1 && flag && !flag2)
        {
            PGraphics.showWarning("Reading depth and stencil values from this multisampled buffer is not enabled. You can enable it by calling hint(ENABLE_DEPTH_READING) once. If your sketch becomes too slow, disable multisampling with noSmooth() instead.");
        } else
        {
            graphics.beginReadPixels();
            readPixelsImpl(i, j, k, l, i1, j1, l1);
            graphics.endReadPixels();
        }
    }

    public void readPixels(int i, int j, int k, int l, int i1, int j1, Buffer buffer)
    {
        boolean flag = false;
        boolean flag1;
        boolean flag2;
        if(isMultisampled() || graphics.offscreenMultisample)
            flag1 = true;
        else
            flag1 = false;
        flag2 = graphics.getHint(10);
        if(i1 == STENCIL_INDEX || i1 == DEPTH_COMPONENT || i1 == DEPTH_STENCIL)
            flag = true;
        if(flag1 && flag && !flag2)
        {
            PGraphics.showWarning("Reading depth and stencil values from this multisampled buffer is not enabled. You can enable it by calling hint(ENABLE_DEPTH_READING) once. If your sketch becomes too slow, disable multisampling with noSmooth() instead.");
        } else
        {
            graphics.beginReadPixels();
            readPixelsImpl(i, j, k, l, i1, j1, buffer);
            graphics.endReadPixels();
        }
    }

    protected abstract void readPixelsImpl(int i, int j, int k, int l, int i1, int j1, long l1);

    protected abstract void readPixelsImpl(int i, int j, int k, int l, int i1, int j1, Buffer buffer);

    protected abstract void registerListeners();

    protected abstract void reinitSurface();

    public abstract void releaseShaderCompiler();

    public abstract void renderbufferStorage(int i, int j, int k, int l);

    public abstract void renderbufferStorageMultisample(int i, int j, int k, int l, int i1);

    protected abstract void requestDraw();

    public void requestFBOLayer()
    {
        enableFBOLayer();
    }

    protected abstract void requestFocus();

    public void resetFBOLayer()
    {
        fbolayerResetReq = true;
    }

    protected void restoreFirstFrame()
    {
        if(firstFrame != null)
        {
            IntBuffer intbuffer = allocateIntBuffer(1);
            genTextures(1, intbuffer);
            float f = getPixelScale();
            int i;
            int j;
            if(hasNpotTexSupport())
            {
                i = (int)((float)graphics.width * f);
                j = (int)((float)graphics.height * f);
            } else
            {
                i = nextPowerOfTwo((int)((float)graphics.width * f));
                j = nextPowerOfTwo((int)((float)graphics.height * f));
            }
            bindTexture(TEXTURE_2D, intbuffer.get(0));
            texParameteri(TEXTURE_2D, TEXTURE_MIN_FILTER, NEAREST);
            texParameteri(TEXTURE_2D, TEXTURE_MAG_FILTER, NEAREST);
            texParameteri(TEXTURE_2D, TEXTURE_WRAP_S, CLAMP_TO_EDGE);
            texParameteri(TEXTURE_2D, TEXTURE_WRAP_T, CLAMP_TO_EDGE);
            texImage2D(TEXTURE_2D, 0, RGBA, i, j, 0, RGBA, UNSIGNED_BYTE, null);
            texSubImage2D(TEXTURE_2D, 0, 0, 0, graphics.width, graphics.height, RGBA, UNSIGNED_BYTE, firstFrame);
            drawTexture(TEXTURE_2D, intbuffer.get(0), i, j, 0, 0, graphics.width, graphics.height, 0, 0, (int)((float)graphics.width * f), (int)((float)graphics.height * f), 0, 0, graphics.width, graphics.height);
            deleteTextures(1, intbuffer);
            firstFrame.clear();
            firstFrame = null;
        }
    }

    public abstract void sampleCoverage(float f, boolean flag);

    protected void saveFirstFrame()
    {
        firstFrame = allocateDirectIntBuffer(graphics.width * graphics.height);
        if(hasReadBuffer())
            readBuffer(BACK);
        readPixelsImpl(0, 0, graphics.width, graphics.height, RGBA, UNSIGNED_BYTE, firstFrame);
    }

    public abstract void scissor(int i, int j, int k, int l);

    protected abstract void setFrameRate(float f);

    public void setPrimary(boolean flag)
    {
        primaryPGL = flag;
    }

    public void setThread(Thread thread)
    {
        glThread = thread;
    }

    public abstract void shaderBinary(int i, IntBuffer intbuffer, int j, Buffer buffer, int k);

    public abstract void shaderSource(int i, String s);

    public abstract void stencilFunc(int i, int j, int k);

    public abstract void stencilFuncSeparate(int i, int j, int k, int l);

    public abstract void stencilMask(int i);

    public abstract void stencilMaskSeparate(int i, int j);

    public abstract void stencilOp(int i, int j, int k);

    public abstract void stencilOpSeparate(int i, int j, int k, int l);

    protected abstract void swapBuffers();

    protected void syncBackTexture()
    {
        if(usingFrontTex)
            needSepFrontTex = true;
        if(1 < numSamples)
        {
            bindFramebufferImpl(READ_FRAMEBUFFER, glMultiFbo.get(0));
            bindFramebufferImpl(DRAW_FRAMEBUFFER, glColorFbo.get(0));
            int i = COLOR_BUFFER_BIT;
            int j = i;
            if(graphics.getHint(10))
                j = i | (DEPTH_BUFFER_BIT | STENCIL_BUFFER_BIT);
            blitFramebuffer(0, 0, fboWidth, fboHeight, 0, 0, fboWidth, fboHeight, j, NEAREST);
        }
    }

    protected String tessError(int i)
    {
        return "";
    }

    public abstract void texImage2D(int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1, Buffer buffer);

    public abstract void texParameterf(int i, int j, float f);

    public abstract void texParameterfv(int i, int j, FloatBuffer floatbuffer);

    public abstract void texParameteri(int i, int j, int k);

    public abstract void texParameteriv(int i, int j, IntBuffer intbuffer);

    public abstract void texSubImage2D(int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1, Buffer buffer);

    protected boolean textureIsBound(int i, int j)
    {
        boolean flag;
        boolean flag1;
        boolean flag2;
        flag = true;
        flag1 = true;
        flag2 = false;
        if(boundTextures != null) goto _L2; else goto _L1
_L1:
        flag1 = flag2;
_L4:
        return flag1;
_L2:
        if(i == TEXTURE_2D)
        {
            if(boundTextures[activeTexUnit][0] != j)
                flag1 = false;
        } else
        {
            flag1 = flag2;
            if(i == TEXTURE_RECTANGLE)
                if(boundTextures[activeTexUnit][1] == j)
                    flag1 = flag;
                else
                    flag1 = false;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected boolean texturingIsEnabled(int i)
    {
        boolean flag = false;
        if(i != TEXTURE_2D) goto _L2; else goto _L1
_L1:
        flag = texturingTargets[0];
_L4:
        return flag;
_L2:
        if(i == TEXTURE_RECTANGLE)
            flag = texturingTargets[1];
        if(true) goto _L4; else goto _L3
_L3:
    }

    public boolean threadIsCurrent()
    {
        boolean flag;
        if(Thread.currentThread() == glThread)
            flag = true;
        else
            flag = false;
        return flag;
    }

    protected void unbindFrontTexture()
    {
        if(textureIsBound(TEXTURE_2D, glColorTex.get(frontTex)))
            if(!texturingIsEnabled(TEXTURE_2D))
            {
                enableTexturing(TEXTURE_2D);
                bindTexture(TEXTURE_2D, 0);
                disableTexturing(TEXTURE_2D);
            } else
            {
                bindTexture(TEXTURE_2D, 0);
            }
    }

    public abstract void uniform1f(int i, float f);

    public abstract void uniform1fv(int i, int j, FloatBuffer floatbuffer);

    public abstract void uniform1i(int i, int j);

    public abstract void uniform1iv(int i, int j, IntBuffer intbuffer);

    public abstract void uniform2f(int i, float f, float f1);

    public abstract void uniform2fv(int i, int j, FloatBuffer floatbuffer);

    public abstract void uniform2i(int i, int j, int k);

    public abstract void uniform2iv(int i, int j, IntBuffer intbuffer);

    public abstract void uniform3f(int i, float f, float f1, float f2);

    public abstract void uniform3fv(int i, int j, FloatBuffer floatbuffer);

    public abstract void uniform3i(int i, int j, int k, int l);

    public abstract void uniform3iv(int i, int j, IntBuffer intbuffer);

    public abstract void uniform4f(int i, float f, float f1, float f2, float f3);

    public abstract void uniform4fv(int i, int j, FloatBuffer floatbuffer);

    public abstract void uniform4i(int i, int j, int k, int l, int i1);

    public abstract void uniform4iv(int i, int j, IntBuffer intbuffer);

    public abstract void uniformMatrix2fv(int i, int j, boolean flag, FloatBuffer floatbuffer);

    public abstract void uniformMatrix3fv(int i, int j, boolean flag, FloatBuffer floatbuffer);

    public abstract void uniformMatrix4fv(int i, int j, boolean flag, FloatBuffer floatbuffer);

    public abstract void unmapBuffer(int i);

    public abstract void useProgram(int i);

    protected boolean validateFramebuffer()
    {
        boolean flag;
        int i;
        flag = true;
        i = checkFramebufferStatus(FRAMEBUFFER);
        if(i != FRAMEBUFFER_COMPLETE) goto _L2; else goto _L1
_L1:
        return flag;
_L2:
        if(i != FRAMEBUFFER_INCOMPLETE_ATTACHMENT)
            break; /* Loop/switch isn't completed */
        System.err.println(String.format("Framebuffer error (%1$s), rendering will probably not work as expected Read http://wiki.processing.org/w/OpenGL_Issues for help.", new Object[] {
            "incomplete attachment"
        }));
_L4:
        flag = false;
        if(true) goto _L1; else goto _L3
_L3:
        if(i == FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT)
            System.err.println(String.format("Framebuffer error (%1$s), rendering will probably not work as expected Read http://wiki.processing.org/w/OpenGL_Issues for help.", new Object[] {
                "incomplete missing attachment"
            }));
        else
        if(i == FRAMEBUFFER_INCOMPLETE_DIMENSIONS)
            System.err.println(String.format("Framebuffer error (%1$s), rendering will probably not work as expected Read http://wiki.processing.org/w/OpenGL_Issues for help.", new Object[] {
                "incomplete dimensions"
            }));
        else
        if(i == FRAMEBUFFER_INCOMPLETE_FORMATS)
            System.err.println(String.format("Framebuffer error (%1$s), rendering will probably not work as expected Read http://wiki.processing.org/w/OpenGL_Issues for help.", new Object[] {
                "incomplete formats"
            }));
        else
        if(i == FRAMEBUFFER_UNSUPPORTED)
            System.err.println(String.format("Framebuffer error (%1$s), rendering will probably not work as expected Read http://wiki.processing.org/w/OpenGL_Issues for help.", new Object[] {
                "framebuffer unsupported"
            }));
        else
            System.err.println(String.format("Framebuffer error (%1$s), rendering will probably not work as expected Read http://wiki.processing.org/w/OpenGL_Issues for help.", new Object[] {
                "unknown error"
            }));
          goto _L4
        if(true) goto _L1; else goto _L5
_L5:
    }

    public abstract void validateProgram(int i);

    public abstract void vertexAttrib1f(int i, float f);

    public abstract void vertexAttrib1fv(int i, FloatBuffer floatbuffer);

    public abstract void vertexAttrib2f(int i, float f, float f1);

    public abstract void vertexAttrib2fv(int i, FloatBuffer floatbuffer);

    public abstract void vertexAttrib3f(int i, float f, float f1, float f2);

    public abstract void vertexAttrib3fv(int i, FloatBuffer floatbuffer);

    public abstract void vertexAttrib4f(int i, float f, float f1, float f2, float f3);

    public abstract void vertexAttrib4fv(int i, FloatBuffer floatbuffer);

    public abstract void vertexAttribPointer(int i, int j, int k, boolean flag, int l, int i1);

    public abstract void viewport(int i, int j, int k, int l);

    protected abstract void viewportImpl(int i, int j, int k, int l);

    protected Texture wrapBackTexture(Texture texture)
    {
        if(texture == null)
        {
            texture = new Texture(graphics);
            texture.init(graphics.width, graphics.height, glColorTex.get(backTex), TEXTURE_2D, RGBA, fboWidth, fboHeight, NEAREST, NEAREST, CLAMP_TO_EDGE, CLAMP_TO_EDGE);
            texture.invertedY(true);
            texture.colorBuffer(true);
            graphics.setCache(graphics, texture);
        } else
        {
            texture.glName = glColorTex.get(backTex);
        }
        return texture;
    }

    protected Texture wrapFrontTexture(Texture texture)
    {
        if(texture == null)
        {
            texture = new Texture(graphics);
            texture.init(graphics.width, graphics.height, glColorTex.get(frontTex), TEXTURE_2D, RGBA, fboWidth, fboHeight, NEAREST, NEAREST, CLAMP_TO_EDGE, CLAMP_TO_EDGE);
            texture.invertedY(true);
            texture.colorBuffer(true);
        } else
        {
            texture.glName = glColorTex.get(frontTex);
        }
        return texture;
    }

    public static int ALIASED_LINE_WIDTH_RANGE = 0;
    public static int ALIASED_POINT_SIZE_RANGE = 0;
    public static int ALPHA = 0;
    public static int ALPHA8 = 0;
    public static int ALREADY_SIGNALED = 0;
    public static int ALWAYS = 0;
    public static int ARRAY_BUFFER = 0;
    public static int BACK = 0;
    protected static boolean BIG_ENDIAN = false;
    public static int BLEND = 0;
    public static int BOOL = 0;
    public static int BOOL_VEC2 = 0;
    public static int BOOL_VEC3 = 0;
    public static int BOOL_VEC4 = 0;
    public static int BUFFER_SIZE = 0;
    public static int BUFFER_USAGE = 0;
    public static int BYTE = 0;
    public static int CCW = 0;
    public static int CLAMP_TO_EDGE = 0;
    public static int COLOR_ATTACHMENT0 = 0;
    public static int COLOR_ATTACHMENT1 = 0;
    public static int COLOR_ATTACHMENT2 = 0;
    public static int COLOR_ATTACHMENT3 = 0;
    public static int COLOR_BUFFER_BIT = 0;
    public static int COMPILE_STATUS = 0;
    public static int COMPRESSED_TEXTURE_FORMATS = 0;
    public static int CONDITION_SATISFIED = 0;
    public static int CONSTANT_ALPHA = 0;
    public static int CONSTANT_COLOR = 0;
    public static int CULL_FACE = 0;
    public static int CURRENT_VERTEX_ATTRIB = 0;
    public static int CW = 0;
    public static int DECR = 0;
    public static int DECR_WRAP = 0;
    protected static int DEFAULT_IN_EDGES = 0;
    protected static int DEFAULT_IN_TEXTURES = 0;
    protected static int DEFAULT_IN_VERTICES = 0;
    protected static int DEFAULT_TESS_INDICES = 0;
    protected static int DEFAULT_TESS_VERTICES = 0;
    public static int DELETE_STATUS = 0;
    public static int DEPTH24_STENCIL8 = 0;
    public static int DEPTH_ATTACHMENT = 0;
    public static int DEPTH_BITS = 0;
    public static int DEPTH_BUFFER_BIT = 0;
    public static int DEPTH_COMPONENT = 0;
    public static int DEPTH_COMPONENT16 = 0;
    public static int DEPTH_COMPONENT24 = 0;
    public static int DEPTH_COMPONENT32 = 0;
    protected static final String DEPTH_READING_NOT_ENABLED_ERROR = "Reading depth and stencil values from this multisampled buffer is not enabled. You can enable it by calling hint(ENABLE_DEPTH_READING) once. If your sketch becomes too slow, disable multisampling with noSmooth() instead.";
    public static int DEPTH_STENCIL = 0;
    public static int DEPTH_TEST = 0;
    public static int DEPTH_WRITEMASK = 0;
    public static int DITHER = 0;
    public static int DONT_CARE = 0;
    public static int DRAW_FRAMEBUFFER = 0;
    public static int DST_ALPHA = 0;
    public static int DST_COLOR = 0;
    public static int DYNAMIC_DRAW = 0;
    public static int ELEMENT_ARRAY_BUFFER = 0;
    public static int EQUAL = 0;
    public static int EXTENSIONS = 0;
    public static int FALSE = 0;
    public static int FASTEST = 0;
    public static int FLOAT = 0;
    protected static float FLOAT_EPS = 0F;
    public static int FLOAT_MAT2 = 0;
    public static int FLOAT_MAT3 = 0;
    public static int FLOAT_MAT4 = 0;
    public static int FLOAT_VEC2 = 0;
    public static int FLOAT_VEC3 = 0;
    public static int FLOAT_VEC4 = 0;
    protected static int FLUSH_VERTEX_COUNT = 0;
    public static int FRAGMENT_SHADER = 0;
    public static int FRAMEBUFFER = 0;
    public static int FRAMEBUFFER_ATTACHMENT_OBJECT_NAME = 0;
    public static int FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE = 0;
    public static int FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE = 0;
    public static int FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL = 0;
    public static int FRAMEBUFFER_COMPLETE = 0;
    public static final String FRAMEBUFFER_ERROR = "Framebuffer error (%1$s), rendering will probably not work as expected Read http://wiki.processing.org/w/OpenGL_Issues for help.";
    public static int FRAMEBUFFER_INCOMPLETE_ATTACHMENT = 0;
    public static int FRAMEBUFFER_INCOMPLETE_DIMENSIONS = 0;
    public static int FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER = 0;
    public static int FRAMEBUFFER_INCOMPLETE_FORMATS = 0;
    public static int FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = 0;
    public static int FRAMEBUFFER_INCOMPLETE_READ_BUFFER = 0;
    public static int FRAMEBUFFER_UNSUPPORTED = 0;
    public static int FRONT = 0;
    public static int FRONT_AND_BACK = 0;
    public static int FUNC_ADD = 0;
    public static int FUNC_MAX = 0;
    public static int FUNC_MIN = 0;
    public static int FUNC_REVERSE_SUBTRACT = 0;
    public static int FUNC_SUBTRACT = 0;
    public static int GENERATE_MIPMAP_HINT = 0;
    public static int GEQUAL = 0;
    protected static final String GLSL_FN_REGEX = "(?<![0-9A-Z_a-z])(%s)(?=\\s*\\()";
    protected static final String GLSL_ID_REGEX = "(?<![0-9A-Z_a-z])(%s)(?![0-9A-Z_a-z]|\\s*\\()";
    public static int GREATER = 0;
    public static int HIGH_FLOAT = 0;
    public static int HIGH_INT = 0;
    public static int INCR = 0;
    public static int INCR_WRAP = 0;
    protected static int INDEX_TYPE = 0;
    public static int INFO_LOG_LENGTH = 0;
    public static int INT = 0;
    public static int INT_VEC2 = 0;
    public static int INT_VEC3 = 0;
    public static int INT_VEC4 = 0;
    public static int INVERT = 0;
    public static int KEEP = 0;
    public static int LEQUAL = 0;
    public static int LESS = 0;
    public static int LINEAR = 0;
    public static int LINEAR_MIPMAP_LINEAR = 0;
    public static int LINEAR_MIPMAP_NEAREST = 0;
    public static int LINES = 0;
    public static int LINE_LOOP = 0;
    public static int LINE_SMOOTH = 0;
    public static int LINE_STRIP = 0;
    public static int LINK_STATUS = 0;
    public static int LOW_FLOAT = 0;
    public static int LOW_INT = 0;
    public static int LUMINANCE = 0;
    public static int LUMINANCE_ALPHA = 0;
    protected static int MAX_CAPS_JOINS_LENGTH = 0;
    public static int MAX_COMBINED_TEXTURE_IMAGE_UNITS = 0;
    protected static int MAX_FONT_TEX_SIZE = 0;
    protected static int MAX_LIGHTS = 0;
    public static int MAX_SAMPLES = 0;
    public static int MAX_TEXTURE_IMAGE_UNITS = 0;
    public static int MAX_TEXTURE_MAX_ANISOTROPY = 0;
    public static int MAX_TEXTURE_SIZE = 0;
    public static int MAX_VERTEX_ATTRIBS = 0;
    protected static int MAX_VERTEX_INDEX = 0;
    protected static int MAX_VERTEX_INDEX1 = 0;
    public static int MAX_VERTEX_TEXTURE_IMAGE_UNITS = 0;
    public static int MEDIUM_FLOAT = 0;
    public static int MEDIUM_INT = 0;
    protected static int MIN_ARRAYCOPY_SIZE = 0;
    protected static float MIN_CAPS_JOINS_WEIGHT = 0F;
    protected static int MIN_DIRECT_BUFFER_SIZE = 0;
    protected static int MIN_FONT_TEX_SIZE = 0;
    protected static boolean MIPMAPS_ENABLED = false;
    public static final String MISSING_FBO_ERROR = "Framebuffer objects are not supported by this hardware (or driver) Read http://wiki.processing.org/w/OpenGL_Issues for help.";
    public static final String MISSING_GLFUNC_ERROR = "GL function %1$s is not available on this hardware (or driver) Read http://wiki.processing.org/w/OpenGL_Issues for help.";
    public static final String MISSING_GLSL_ERROR = "GLSL shaders are not supported by this hardware (or driver) Read http://wiki.processing.org/w/OpenGL_Issues for help.";
    public static int MULTISAMPLE = 0;
    public static int NEAREST = 0;
    public static int NEVER = 0;
    public static int NICEST = 0;
    public static final String NONPRIMARY_ERROR = "The renderer is trying to call a PGL function that can only be called on a primary PGL. This is most likely due to a bug in the renderer's code, please report it with an issue on Processing's github page https://github.com/processing/processing/issues?state=open if using any of the built-in OpenGL renderers. If you are using a contributed library, contact the library's developers.";
    public static int NOTEQUAL = 0;
    public static int NUM_COMPRESSED_TEXTURE_FORMATS = 0;
    public static int ONE = 0;
    public static int ONE_MINUS_CONSTANT_ALPHA = 0;
    public static int ONE_MINUS_CONSTANT_COLOR = 0;
    public static int ONE_MINUS_DST_COLOR = 0;
    public static int ONE_MINUS_SRC_ALPHA = 0;
    public static int ONE_MINUS_SRC_COLOR = 0;
    public static int PACK_ALIGNMENT = 0;
    public static int PIXEL_PACK_BUFFER = 0;
    public static int POINTS = 0;
    public static int POLYGON_OFFSET_FILL = 0;
    public static int POLYGON_SMOOTH = 0;
    public static int READ_FRAMEBUFFER = 0;
    public static int READ_ONLY = 0;
    public static int READ_WRITE = 0;
    public static int RENDERBUFFER = 0;
    public static int RENDERBUFFER_ALPHA_SIZE = 0;
    public static int RENDERBUFFER_BLUE_SIZE = 0;
    public static int RENDERBUFFER_DEPTH_SIZE = 0;
    public static int RENDERBUFFER_GREEN_SIZE = 0;
    public static int RENDERBUFFER_HEIGHT = 0;
    public static int RENDERBUFFER_INTERNAL_FORMAT = 0;
    public static int RENDERBUFFER_RED_SIZE = 0;
    public static int RENDERBUFFER_STENCIL_SIZE = 0;
    public static int RENDERBUFFER_WIDTH = 0;
    public static int RENDERER = 0;
    public static int REPEAT = 0;
    public static int REPLACE = 0;
    public static int REQUESTED_ALPHA_BITS = 0;
    public static int REQUESTED_DEPTH_BITS = 0;
    public static int REQUESTED_STENCIL_BITS = 0;
    public static int RGB = 0;
    public static int RGB565 = 0;
    public static int RGB5_A1 = 0;
    public static int RGB8 = 0;
    public static int RGBA = 0;
    public static int RGBA4 = 0;
    public static int RGBA8 = 0;
    public static int SAMPLER_2D = 0;
    public static int SAMPLER_CUBE = 0;
    public static int SAMPLES = 0;
    public static int SAMPLE_ALPHA_TO_COVERAGE = 0;
    public static int SAMPLE_COVERAGE = 0;
    public static int SCISSOR_TEST = 0;
    protected static int SEG_CLOSE = 0;
    protected static int SEG_CUBICTO = 0;
    protected static int SEG_LINETO = 0;
    protected static int SEG_MOVETO = 0;
    protected static int SEG_QUADTO = 0;
    protected static final String SHADER_PREPROCESSOR_DIRECTIVE = "#ifdef GL_ES\nprecision mediump float;\nprecision mediump int;\n#endif\n";
    public static int SHADER_SOURCE_LENGTH = 0;
    public static int SHADER_TYPE = 0;
    public static int SHADING_LANGUAGE_VERSION = 0;
    protected static boolean SHAPE_TEXT_SUPPORTED = false;
    public static int SHORT = 0;
    protected static boolean SINGLE_BUFFERED = false;
    protected static int SIZEOF_BYTE = 0;
    protected static int SIZEOF_FLOAT = 0;
    protected static int SIZEOF_INDEX = 0;
    protected static int SIZEOF_INT = 0;
    protected static int SIZEOF_SHORT = 0;
    public static int SRC_ALPHA = 0;
    public static int SRC_ALPHA_SATURATE = 0;
    public static int SRC_COLOR = 0;
    public static int STATIC_DRAW = 0;
    public static int STENCIL_ATTACHMENT = 0;
    public static int STENCIL_BITS = 0;
    public static int STENCIL_BUFFER_BIT = 0;
    public static int STENCIL_INDEX = 0;
    public static int STENCIL_INDEX1 = 0;
    public static int STENCIL_INDEX4 = 0;
    public static int STENCIL_INDEX8 = 0;
    public static int STENCIL_TEST = 0;
    public static int STREAM_DRAW = 0;
    public static int STREAM_READ = 0;
    protected static float STROKE_DISPLACEMENT = 0F;
    public static int SYNC_GPU_COMMANDS_COMPLETE = 0;
    public static int TESS_WINDING_NONZERO = 0;
    public static int TESS_WINDING_ODD = 0;
    public static int TEXTURE0 = 0;
    public static int TEXTURE1 = 0;
    public static int TEXTURE2 = 0;
    public static int TEXTURE3 = 0;
    public static int TEXTURE_2D = 0;
    public static int TEXTURE_BINDING_2D = 0;
    public static int TEXTURE_BINDING_RECTANGLE = 0;
    public static int TEXTURE_CUBE_MAP = 0;
    public static int TEXTURE_CUBE_MAP_NEGATIVE_X = 0;
    public static int TEXTURE_CUBE_MAP_NEGATIVE_Y = 0;
    public static int TEXTURE_CUBE_MAP_NEGATIVE_Z = 0;
    public static int TEXTURE_CUBE_MAP_POSITIVE_X = 0;
    public static int TEXTURE_CUBE_MAP_POSITIVE_Y = 0;
    public static int TEXTURE_CUBE_MAP_POSITIVE_Z = 0;
    public static int TEXTURE_MAG_FILTER = 0;
    public static int TEXTURE_MAX_ANISOTROPY = 0;
    public static int TEXTURE_MIN_FILTER = 0;
    public static int TEXTURE_RECTANGLE = 0;
    public static int TEXTURE_WRAP_R = 0;
    public static int TEXTURE_WRAP_S = 0;
    public static int TEXTURE_WRAP_T = 0;
    public static final String TEXUNIT_ERROR = "Number of texture units not supported by this hardware (or driver) Read http://wiki.processing.org/w/OpenGL_Issues for help.";
    public static int TRIANGLES = 0;
    public static int TRIANGLE_FAN = 0;
    public static int TRIANGLE_STRIP = 0;
    public static int TRUE = 0;
    public static int UNPACK_ALIGNMENT = 0;
    public static int UNSIGNED_BYTE = 0;
    public static int UNSIGNED_INT = 0;
    public static int UNSIGNED_SHORT = 0;
    public static int UNSIGNED_SHORT_4_4_4_4 = 0;
    public static int UNSIGNED_SHORT_5_5_5_1 = 0;
    public static int UNSIGNED_SHORT_5_6_5 = 0;
    public static final String UNSUPPORTED_GLPROF_ERROR = "Unsupported OpenGL profile.";
    protected static boolean USE_DIRECT_BUFFERS = false;
    public static int VALIDATE_STATUS = 0;
    public static int VENDOR = 0;
    public static int VERSION = 0;
    public static int VERTEX_ATTRIB_ARRAY_BUFFER_BINDING = 0;
    public static int VERTEX_ATTRIB_ARRAY_ENABLED = 0;
    public static int VERTEX_ATTRIB_ARRAY_NORMALIZED = 0;
    public static int VERTEX_ATTRIB_ARRAY_POINTER = 0;
    public static int VERTEX_ATTRIB_ARRAY_SIZE = 0;
    public static int VERTEX_ATTRIB_ARRAY_STRIDE = 0;
    public static int VERTEX_ATTRIB_ARRAY_TYPE = 0;
    public static int VERTEX_SHADER = 0;
    public static int VIEWPORT = 0;
    public static final String WIKI = " Read http://wiki.processing.org/w/OpenGL_Issues for help.";
    public static int WRITE_ONLY;
    public static int ZERO;
    protected static int closeButtonPix[] = {
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, -1, -1, 
        -1, 0, 0, 0, -1, -1, -1, -1, -1, 0, 
        0, 0, -1, -1, -1, 0, 0, 0, -1, -1, 
        -1, -1, 0, 0, 0, -1, 0, 0, 0, -1, 
        0, 0, 0, 0, -1, 0, 0, 0, 0, -1, 
        -1, 0, -1, -1, 0, 0, -1, -1, 0, -1, 
        -1, 0, 0, -1, 0, 0, 0, 0, 0, 0, 
        0, 0, -1, 0, 0, 0, 0, -1, 0, 0, 
        0, -1, 0, 0, -1, 0, 0, 0, -1, 0, 
        0, 0, -1, -1, -1, 0, 0, 0, 0, 0, 
        -1, 0, 0, 0, 0, -1, 0, 0, 0, -1, 
        0, 0, -1, 0, 0, 0, -1, 0, 0, 0, 
        0, 0, 0, -1, 0, 0, 0, 0, -1, 0, 
        0, 0, 0, -1, 0, 0, 0, -1, 0, 0, 
        -1, 0, 0, 0, -1, 0, 0, -1, 0, 0, 
        0, -1, 0, 0, 0, 0, -1, 0, 0, 0, 
        0, -1, -1, 0, -1, -1, 0, 0, -1, -1, 
        0, -1, -1, 0, 0, 0, -1, -1, -1, 0, 
        0, 0, 0, 0, -1, -1, -1, 0, 0, 0, 
        -1, -1, -1, 0, 0, 0, -1, -1, -1, -1, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -1, 0, 0, 0, 0, 0
    };
    protected static String tex2DFragShaderSource[] = {
        "#ifdef GL_ES\nprecision mediump float;\nprecision mediump int;\n#endif\n", "uniform sampler2D texMap;", "varying vec2 vertTexCoord;", "void main() {", "  gl_FragColor = texture2D(texMap, vertTexCoord.st);", "}"
    };
    protected static String texRectFragShaderSource[] = {
        "#ifdef GL_ES\nprecision mediump float;\nprecision mediump int;\n#endif\n", "uniform sampler2DRect texMap;", "varying vec2 vertTexCoord;", "void main() {", "  gl_FragColor = texture2DRect(texMap, vertTexCoord.st);", "}"
    };
    protected static String texVertShaderSource[] = {
        "attribute vec2 position;", "attribute vec2 texCoord;", "varying vec2 vertTexCoord;", "void main() {", "  gl_Position = vec4(position, 0, 1);", "  vertTexCoord = texCoord;", "}"
    };
    protected int activeTexUnit;
    protected int backTex;
    protected int boundTextures[][];
    protected ByteBuffer byteBuffer;
    protected boolean clearColor;
    protected IntBuffer closeButtonTex;
    protected int closeButtonY;
    protected IntBuffer colorBuffer;
    protected float currentFps;
    protected FloatBuffer depthBuffer;
    protected int fboHeight;
    protected boolean fboLayerCreated;
    protected boolean fboLayerDisableReq;
    protected boolean fboLayerEnabled;
    protected boolean fboLayerEnabledReq;
    protected int fboWidth;
    protected boolean fbolayerResetReq;
    protected IntBuffer firstFrame;
    protected int frontTex;
    protected int geomCount;
    protected IntBuffer glColorFbo;
    protected IntBuffer glColorTex;
    protected int glContext;
    protected IntBuffer glDepth;
    protected IntBuffer glDepthStencil;
    protected IntBuffer glMultiColor;
    protected IntBuffer glMultiDepth;
    protected IntBuffer glMultiDepthStencil;
    protected IntBuffer glMultiFbo;
    protected IntBuffer glMultiStencil;
    protected IntBuffer glStencil;
    protected Thread glThread;
    protected PGraphicsOpenGL graphics;
    protected IntBuffer intBuffer;
    protected boolean loadedTex2DShader;
    protected boolean loadedTexRectShader;
    protected int maxTexUnits;
    protected boolean needSepFrontTex;
    protected int numSamples;
    protected boolean pclearColor;
    protected int pgeomCount;
    protected boolean presentMode;
    public float presentX;
    public float presentY;
    public boolean primaryPGL;
    public int reqNumSamples;
    protected boolean setFps;
    protected boolean showStopButton;
    protected PApplet sketch;
    protected ByteBuffer stencilBuffer;
    protected int stopButtonColor;
    protected int stopButtonHeight;
    protected int stopButtonWidth;
    protected int stopButtonX;
    protected float targetFps;
    protected int tex2DFragShader;
    protected int tex2DGeoVBO;
    protected int tex2DSamplerLoc;
    protected int tex2DShaderContext;
    protected int tex2DShaderProgram;
    protected int tex2DTCoordLoc;
    protected int tex2DVertLoc;
    protected int tex2DVertShader;
    protected float texCoords[] = {
        -1F, -1F, 0.0F, 0.0F, 1.0F, -1F, 1.0F, 0.0F, -1F, 1.0F, 
        0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F
    };
    protected FloatBuffer texData;
    protected int texRectFragShader;
    protected int texRectGeoVBO;
    protected int texRectSamplerLoc;
    protected int texRectShaderContext;
    protected int texRectShaderProgram;
    protected int texRectTCoordLoc;
    protected int texRectVertLoc;
    protected int texRectVertShader;
    protected boolean texturingTargets[] = {
        0, 0
    };
    protected boolean usingFrontTex;
    protected IntBuffer viewBuffer;

    static 
    {
        boolean flag = true;
        REQUESTED_DEPTH_BITS = 24;
        REQUESTED_STENCIL_BITS = 8;
        REQUESTED_ALPHA_BITS = 8;
        USE_DIRECT_BUFFERS = true;
        MIN_DIRECT_BUFFER_SIZE = 1;
        MIPMAPS_ENABLED = true;
        DEFAULT_IN_VERTICES = 64;
        DEFAULT_IN_EDGES = 128;
        DEFAULT_IN_TEXTURES = 64;
        DEFAULT_TESS_VERTICES = 64;
        DEFAULT_TESS_INDICES = 128;
        MAX_LIGHTS = 8;
        MAX_VERTEX_INDEX = 32767;
        MAX_VERTEX_INDEX1 = MAX_VERTEX_INDEX + 1;
        FLUSH_VERTEX_COUNT = MAX_VERTEX_INDEX1;
        MIN_FONT_TEX_SIZE = 256;
        MAX_FONT_TEX_SIZE = 1024;
        MIN_CAPS_JOINS_WEIGHT = 2.0F;
        MAX_CAPS_JOINS_LENGTH = 5000;
        MIN_ARRAYCOPY_SIZE = 2;
        STROKE_DISPLACEMENT = 0.999F;
        SINGLE_BUFFERED = false;
        SIZEOF_SHORT = 2;
        SIZEOF_INT = 4;
        SIZEOF_FLOAT = 4;
        SIZEOF_BYTE = 1;
        SIZEOF_INDEX = SIZEOF_SHORT;
        INDEX_TYPE = 5123;
        FLOAT_EPS = 1.401298E-045F;
        float f = 1.0F;
        float f1;
        do
        {
            f1 = f / 2.0F;
            f = f1;
        } while((double)(float)(1.0D + (double)f1 / 2D) != 1.0D);
        FLOAT_EPS = f1;
        if(ByteOrder.nativeOrder() != ByteOrder.BIG_ENDIAN)
            flag = false;
        BIG_ENDIAN = flag;
    }
}
