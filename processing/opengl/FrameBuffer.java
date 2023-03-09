// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.opengl;

import java.nio.IntBuffer;
import processing.core.PApplet;
import processing.core.PConstants;

// Referenced classes of package processing.opengl:
//            PGraphicsOpenGL, PGL, Texture

public class FrameBuffer
    implements PConstants
{

    FrameBuffer(PGraphicsOpenGL pgraphicsopengl)
    {
        pg = pgraphicsopengl;
        pgl = pgraphicsopengl.pgl;
        context = pgl.createEmptyContext();
    }

    FrameBuffer(PGraphicsOpenGL pgraphicsopengl, int i, int j)
    {
        this(pgraphicsopengl, i, j, 1, 1, 0, 0, false, false);
    }

    FrameBuffer(PGraphicsOpenGL pgraphicsopengl, int i, int j, int k, int l, int i1, int j1, 
            boolean flag, boolean flag1)
    {
        this(pgraphicsopengl);
        glFbo = 0;
        glDepth = 0;
        glStencil = 0;
        glDepthStencil = 0;
        glMultisample = 0;
        if(flag1)
        {
            j1 = 0;
            i1 = 0;
            l = 0;
            k = 0;
        }
        width = i;
        height = j;
        if(1 < k)
        {
            multisample = true;
            nsamples = k;
        } else
        {
            multisample = false;
            nsamples = 1;
        }
        numColorBuffers = l;
        colorBufferTex = new Texture[numColorBuffers];
        for(i = 0; i < numColorBuffers; i++)
            colorBufferTex[i] = null;

        if(i1 < 1 && j1 < 1)
        {
            depthBits = 0;
            stencilBits = 0;
            packedDepthStencil = false;
        } else
        if(flag)
        {
            depthBits = 24;
            stencilBits = 8;
            packedDepthStencil = true;
        } else
        {
            depthBits = i1;
            stencilBits = j1;
            packedDepthStencil = false;
        }
        screenFb = flag1;
        allocate();
        noDepth = false;
        pixelBuffer = null;
    }

    FrameBuffer(PGraphicsOpenGL pgraphicsopengl, int i, int j, boolean flag)
    {
        this(pgraphicsopengl, i, j, 1, 1, 0, 0, false, flag);
    }

    protected void allocate()
    {
        dispose();
        context = pgl.getCurrentContext();
        glres = new PGraphicsOpenGL.GLResourceFrameBuffer(this);
        if(!screenFb) goto _L2; else goto _L1
_L1:
        glFbo = 0;
_L4:
        return;
_L2:
        if(multisample)
            initColorBufferMultisample();
        if(packedDepthStencil)
        {
            initPackedDepthStencilBuffer();
        } else
        {
            if(depthBits > 0)
                initDepthBuffer();
            if(stencilBits > 0)
                initStencilBuffer();
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void bind()
    {
        pgl.bindFramebufferImpl(PGL.FRAMEBUFFER, glFbo);
    }

    public void clear()
    {
        pg.pushFramebuffer();
        pg.setFramebuffer(this);
        pgl.clearDepth(1.0F);
        pgl.clearStencil(0);
        pgl.clearColor(0.0F, 0.0F, 0.0F, 0.0F);
        pgl.clear(PGL.DEPTH_BUFFER_BIT | PGL.STENCIL_BUFFER_BIT | PGL.COLOR_BUFFER_BIT);
        pg.popFramebuffer();
    }

    protected boolean contextIsOutdated()
    {
        int i;
        boolean flag;
        i = 0;
        flag = false;
        if(!screenFb) goto _L2; else goto _L1
_L1:
        return flag;
_L2:
        if(!pgl.contextIsCurrent(context))
            flag = true;
        else
            flag = false;
        if(flag)
        {
            dispose();
            while(i < numColorBuffers) 
            {
                colorBufferTex[i] = null;
                i++;
            }
        }
        if(true) goto _L1; else goto _L3
_L3:
    }

    public void copy(FrameBuffer framebuffer, int i)
    {
        pgl.bindFramebufferImpl(PGL.READ_FRAMEBUFFER, glFbo);
        pgl.bindFramebufferImpl(PGL.DRAW_FRAMEBUFFER, framebuffer.glFbo);
        pgl.blitFramebuffer(0, 0, width, height, 0, 0, framebuffer.width, framebuffer.height, i, PGL.NEAREST);
        pgl.bindFramebufferImpl(PGL.READ_FRAMEBUFFER, pg.getCurrentFB().glFbo);
        pgl.bindFramebufferImpl(PGL.DRAW_FRAMEBUFFER, pg.getCurrentFB().glFbo);
    }

    public void copyColor(FrameBuffer framebuffer)
    {
        copy(framebuffer, PGL.COLOR_BUFFER_BIT);
    }

    public void copyDepth(FrameBuffer framebuffer)
    {
        copy(framebuffer, PGL.DEPTH_BUFFER_BIT);
    }

    public void copyStencil(FrameBuffer framebuffer)
    {
        copy(framebuffer, PGL.STENCIL_BUFFER_BIT);
    }

    protected void createPixelBuffer()
    {
        pixelBuffer = IntBuffer.allocate(width * height);
        pixelBuffer.rewind();
    }

    public void disableDepthTest()
    {
        noDepth = true;
    }

    protected void dispose()
    {
        if(!screenFb && glres != null)
        {
            glres.dispose();
            glFbo = 0;
            glDepth = 0;
            glStencil = 0;
            glMultisample = 0;
            glDepthStencil = 0;
            glres = null;
        }
    }

    public void finish()
    {
        if(noDepth)
            if(pg.getHint(-2))
                pgl.enable(PGL.DEPTH_TEST);
            else
                pgl.disable(PGL.DEPTH_TEST);
    }

    public int getDefaultDrawBuffer()
    {
        int i;
        if(screenFb)
            i = pgl.getDefaultDrawBuffer();
        else
            i = PGL.COLOR_ATTACHMENT0;
        return i;
    }

    public int getDefaultReadBuffer()
    {
        int i;
        if(screenFb)
            i = pgl.getDefaultReadBuffer();
        else
            i = PGL.COLOR_ATTACHMENT0;
        return i;
    }

    public IntBuffer getPixelBuffer()
    {
        return pixelBuffer;
    }

    public void getPixels(int ai[])
    {
        if(pixelBuffer != null)
        {
            pixelBuffer.get(ai, 0, ai.length);
            pixelBuffer.rewind();
        }
    }

    public boolean hasDepthBuffer()
    {
        boolean flag;
        if(depthBits > 0)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public boolean hasStencilBuffer()
    {
        boolean flag;
        if(stencilBits > 0)
            flag = true;
        else
            flag = false;
        return flag;
    }

    protected void initColorBufferMultisample()
    {
        if(!screenFb)
        {
            pg.pushFramebuffer();
            pg.setFramebuffer(this);
            pgl.bindRenderbuffer(PGL.RENDERBUFFER, glMultisample);
            pgl.renderbufferStorageMultisample(PGL.RENDERBUFFER, nsamples, PGL.RGBA8, width, height);
            pgl.framebufferRenderbuffer(PGL.FRAMEBUFFER, PGL.COLOR_ATTACHMENT0, PGL.RENDERBUFFER, glMultisample);
            pg.popFramebuffer();
        }
    }

    protected void initDepthBuffer()
    {
        if(!screenFb) goto _L2; else goto _L1
_L1:
        return;
_L2:
        int i;
        if(width == 0 || height == 0)
            throw new RuntimeException("PFramebuffer: size undefined.");
        pg.pushFramebuffer();
        pg.setFramebuffer(this);
        pgl.bindRenderbuffer(PGL.RENDERBUFFER, glDepth);
        i = PGL.DEPTH_COMPONENT16;
        if(depthBits != 16) goto _L4; else goto _L3
_L3:
        i = PGL.DEPTH_COMPONENT16;
_L5:
        if(multisample)
            pgl.renderbufferStorageMultisample(PGL.RENDERBUFFER, nsamples, i, width, height);
        else
            pgl.renderbufferStorage(PGL.RENDERBUFFER, i, width, height);
        pgl.framebufferRenderbuffer(PGL.FRAMEBUFFER, PGL.DEPTH_ATTACHMENT, PGL.RENDERBUFFER, glDepth);
        pg.popFramebuffer();
        if(true) goto _L1; else goto _L4
_L4:
        if(depthBits == 24)
            i = PGL.DEPTH_COMPONENT24;
        else
        if(depthBits == 32)
            i = PGL.DEPTH_COMPONENT32;
          goto _L5
    }

    protected void initPackedDepthStencilBuffer()
    {
        if(!screenFb)
        {
            if(width == 0 || height == 0)
                throw new RuntimeException("PFramebuffer: size undefined.");
            pg.pushFramebuffer();
            pg.setFramebuffer(this);
            pgl.bindRenderbuffer(PGL.RENDERBUFFER, glDepthStencil);
            if(multisample)
                pgl.renderbufferStorageMultisample(PGL.RENDERBUFFER, nsamples, PGL.DEPTH24_STENCIL8, width, height);
            else
                pgl.renderbufferStorage(PGL.RENDERBUFFER, PGL.DEPTH24_STENCIL8, width, height);
            pgl.framebufferRenderbuffer(PGL.FRAMEBUFFER, PGL.DEPTH_ATTACHMENT, PGL.RENDERBUFFER, glDepthStencil);
            pgl.framebufferRenderbuffer(PGL.FRAMEBUFFER, PGL.STENCIL_ATTACHMENT, PGL.RENDERBUFFER, glDepthStencil);
            pg.popFramebuffer();
        }
    }

    protected void initStencilBuffer()
    {
        if(!screenFb) goto _L2; else goto _L1
_L1:
        return;
_L2:
        int i;
        if(width == 0 || height == 0)
            throw new RuntimeException("PFramebuffer: size undefined.");
        pg.pushFramebuffer();
        pg.setFramebuffer(this);
        pgl.bindRenderbuffer(PGL.RENDERBUFFER, glStencil);
        i = PGL.STENCIL_INDEX1;
        if(stencilBits != 1) goto _L4; else goto _L3
_L3:
        i = PGL.STENCIL_INDEX1;
_L5:
        if(multisample)
            pgl.renderbufferStorageMultisample(PGL.RENDERBUFFER, nsamples, i, width, height);
        else
            pgl.renderbufferStorage(PGL.RENDERBUFFER, i, width, height);
        pgl.framebufferRenderbuffer(PGL.FRAMEBUFFER, PGL.STENCIL_ATTACHMENT, PGL.RENDERBUFFER, glStencil);
        pg.popFramebuffer();
        if(true) goto _L1; else goto _L4
_L4:
        if(stencilBits == 4)
            i = PGL.STENCIL_INDEX4;
        else
        if(stencilBits == 8)
            i = PGL.STENCIL_INDEX8;
          goto _L5
    }

    public void readPixels()
    {
        if(pixelBuffer == null)
            createPixelBuffer();
        pixelBuffer.rewind();
        pgl.readPixels(0, 0, width, height, PGL.RGBA, PGL.UNSIGNED_BYTE, pixelBuffer);
    }

    public void setColorBuffer(Texture texture)
    {
        setColorBuffers(new Texture[] {
            texture
        }, 1);
    }

    public void setColorBuffers(Texture atexture[])
    {
        setColorBuffers(atexture, atexture.length);
    }

    public void setColorBuffers(Texture atexture[], int i)
    {
        if(!screenFb)
        {
            if(numColorBuffers != PApplet.min(i, atexture.length))
                throw new RuntimeException("Wrong number of textures to set the color buffers.");
            for(i = 0; i < numColorBuffers; i++)
                colorBufferTex[i] = atexture[i];

            pg.pushFramebuffer();
            pg.setFramebuffer(this);
            for(i = 0; i < numColorBuffers; i++)
                pgl.framebufferTexture2D(PGL.FRAMEBUFFER, PGL.COLOR_ATTACHMENT0 + i, PGL.TEXTURE_2D, 0, 0);

            for(i = 0; i < numColorBuffers; i++)
                pgl.framebufferTexture2D(PGL.FRAMEBUFFER, PGL.COLOR_ATTACHMENT0 + i, colorBufferTex[i].glTarget, colorBufferTex[i].glName, 0);

            pgl.validateFramebuffer();
            pg.popFramebuffer();
        }
    }

    public void setFBO(int i)
    {
        if(screenFb)
            glFbo = i;
    }

    public void swapColorBuffers()
    {
        for(int i = 0; i < numColorBuffers - 1; i++)
        {
            int k = i + 1;
            Texture texture = colorBufferTex[i];
            colorBufferTex[i] = colorBufferTex[k];
            colorBufferTex[k] = texture;
        }

        pg.pushFramebuffer();
        pg.setFramebuffer(this);
        for(int j = 0; j < numColorBuffers; j++)
            pgl.framebufferTexture2D(PGL.FRAMEBUFFER, PGL.COLOR_ATTACHMENT0 + j, colorBufferTex[j].glTarget, colorBufferTex[j].glName, 0);

        pgl.validateFramebuffer();
        pg.popFramebuffer();
    }

    protected Texture colorBufferTex[];
    protected int context;
    protected int depthBits;
    public int glDepth;
    public int glDepthStencil;
    public int glFbo;
    public int glMultisample;
    public int glStencil;
    private PGraphicsOpenGL.GLResourceFrameBuffer glres;
    public int height;
    protected boolean multisample;
    protected boolean noDepth;
    protected int nsamples;
    protected int numColorBuffers;
    protected boolean packedDepthStencil;
    protected PGraphicsOpenGL pg;
    protected PGL pgl;
    protected IntBuffer pixelBuffer;
    protected boolean screenFb;
    protected int stencilBits;
    public int width;
}
