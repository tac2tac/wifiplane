// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.opengl;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.*;
import processing.core.*;

// Referenced classes of package processing.opengl:
//            PGraphicsOpenGL, PGL, FrameBuffer

public class Texture
    implements PConstants
{
    protected class BufferData
    {

        void dispose()
        {
            disposeBufferMethod.invoke(bufferSource, new Object[] {
                natBuf
            });
            natBuf = null;
            rgbBuf = null;
_L1:
            return;
            Exception exception;
            exception;
            exception.printStackTrace();
              goto _L1
        }

        int h;
        Object natBuf;
        IntBuffer rgbBuf;
        final Texture this$0;
        int w;

        BufferData(Object obj, IntBuffer intbuffer, int i, int j)
        {
            this$0 = Texture.this;
            super();
            natBuf = obj;
            rgbBuf = intbuffer;
            w = i;
            h = j;
        }
    }

    public static class Parameters
    {

        public void set(int i)
        {
            format = i;
        }

        public void set(int i, int j)
        {
            format = i;
            sampling = j;
        }

        public void set(int i, int j, boolean flag)
        {
            format = i;
            sampling = j;
            mipmaps = flag;
        }

        public void set(Parameters parameters)
        {
            target = parameters.target;
            format = parameters.format;
            sampling = parameters.sampling;
            mipmaps = parameters.mipmaps;
            wrapU = parameters.wrapU;
            wrapV = parameters.wrapV;
        }

        public int format;
        public boolean mipmaps;
        public int sampling;
        public int target;
        public int wrapU;
        public int wrapV;

        public Parameters()
        {
            target = 0;
            format = 2;
            sampling = 4;
            mipmaps = true;
            wrapU = 0;
            wrapV = 0;
        }

        public Parameters(int i)
        {
            target = 0;
            format = i;
            sampling = 4;
            mipmaps = true;
            wrapU = 0;
            wrapV = 0;
        }

        public Parameters(int i, int j)
        {
            target = 0;
            format = i;
            sampling = j;
            mipmaps = true;
            wrapU = 0;
            wrapV = 0;
        }

        public Parameters(int i, int j, boolean flag)
        {
            target = 0;
            format = i;
            mipmaps = flag;
            if(j == 5 && !flag)
                sampling = 4;
            else
                sampling = j;
            wrapU = 0;
            wrapV = 0;
        }

        public Parameters(int i, int j, boolean flag, int k)
        {
            target = 0;
            format = i;
            mipmaps = flag;
            if(j == 5 && !flag)
                sampling = 4;
            else
                sampling = j;
            wrapU = k;
            wrapV = k;
        }

        public Parameters(Parameters parameters)
        {
            set(parameters);
        }
    }


    public Texture(PGraphicsOpenGL pgraphicsopengl)
    {
        rgbaPixels = null;
        pixelBuffer = null;
        edgePixels = null;
        edgeBuffer = null;
        tempFbo = null;
        pixBufUpdateCount = 0;
        rgbaPixUpdateCount = 0;
        bufferCache = null;
        usedBuffers = null;
        pg = pgraphicsopengl;
        pgl = pgraphicsopengl.pgl;
        context = pgl.createEmptyContext();
        colorBuffer = false;
        glName = 0;
    }

    public Texture(PGraphicsOpenGL pgraphicsopengl, int i, int j)
    {
        this(pgraphicsopengl, i, j, new Parameters());
    }

    public Texture(PGraphicsOpenGL pgraphicsopengl, int i, int j, Object obj)
    {
        rgbaPixels = null;
        pixelBuffer = null;
        edgePixels = null;
        edgeBuffer = null;
        tempFbo = null;
        pixBufUpdateCount = 0;
        rgbaPixUpdateCount = 0;
        bufferCache = null;
        usedBuffers = null;
        pg = pgraphicsopengl;
        pgl = pgraphicsopengl.pgl;
        context = pgl.createEmptyContext();
        colorBuffer = false;
        glName = 0;
        init(i, j, (Parameters)obj);
    }

    protected void allocate()
    {
        dispose();
        boolean flag;
        if(!pgl.texturingIsEnabled(glTarget))
        {
            pgl.enableTexturing(glTarget);
            flag = true;
        } else
        {
            flag = false;
        }
        context = pgl.getCurrentContext();
        glres = new PGraphicsOpenGL.GLResourceTexture(this);
        pgl.bindTexture(glTarget, glName);
        pgl.texParameteri(glTarget, PGL.TEXTURE_MIN_FILTER, glMinFilter);
        pgl.texParameteri(glTarget, PGL.TEXTURE_MAG_FILTER, glMagFilter);
        pgl.texParameteri(glTarget, PGL.TEXTURE_WRAP_S, glWrapS);
        pgl.texParameteri(glTarget, PGL.TEXTURE_WRAP_T, glWrapT);
        if(PGraphicsOpenGL.anisoSamplingSupported)
            pgl.texParameterf(glTarget, PGL.TEXTURE_MAX_ANISOTROPY, PGraphicsOpenGL.maxAnisoAmount);
        pgl.texImage2D(glTarget, 0, glFormat, glWidth, glHeight, 0, PGL.RGBA, PGL.UNSIGNED_BYTE, null);
        pgl.initTexture(glTarget, PGL.RGBA, width, height);
        pgl.bindTexture(glTarget, 0);
        if(flag)
            pgl.disableTexturing(glTarget);
        bound = false;
    }

    public boolean available()
    {
        boolean flag;
        if(glName > 0)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public void bind()
    {
        if(!pgl.texturingIsEnabled(glTarget))
            pgl.enableTexturing(glTarget);
        pgl.bindTexture(glTarget, glName);
        bound = true;
    }

    public boolean bound()
    {
        return bound;
    }

    protected boolean bufferUpdate()
    {
        boolean flag = false;
        BufferData bufferdata;
        try
        {
            bufferdata = (BufferData)bufferCache.remove(0);
        }
        catch(NoSuchElementException nosuchelementexception)
        {
            PGraphics.showWarning("Don't have pixel data to copy to texture");
            nosuchelementexception = null;
        }
        if(bufferdata != null)
        {
            if(bufferdata.w != width || bufferdata.h != height)
                init(bufferdata.w, bufferdata.h);
            bufferdata.rgbBuf.rewind();
            setNative(bufferdata.rgbBuf, 0, 0, width, height);
            if(usedBuffers == null)
                usedBuffers = new LinkedList();
            usedBuffers.add(bufferdata);
            flag = true;
        }
        return flag;
    }

    public void colorBuffer(boolean flag)
    {
        colorBuffer = flag;
    }

    public boolean colorBuffer()
    {
        return colorBuffer;
    }

    protected boolean contextIsOutdated()
    {
        boolean flag;
        if(!pgl.contextIsCurrent(context))
            flag = true;
        else
            flag = false;
        if(flag)
            dispose();
        return flag;
    }

    protected void convertToARGB(int ai[])
    {
        if(PGL.BIG_ENDIAN)
        {
            int i = 0;
            int k = 0;
            int i1 = 0;
            for(; i < height; i++)
            {
                for(int k1 = 0; k1 < width;)
                {
                    int i2 = ai[k];
                    ai[i1] = i2 << 24 & 0xff000000 | i2 >>> 8;
                    k1++;
                    i1++;
                    k++;
                }

            }

        } else
        {
            int j = 0;
            int l = 0;
            int j1 = 0;
            for(; j < height; j++)
            {
                for(int l1 = 0; l1 < width;)
                {
                    int j2 = ai[l];
                    ai[j1] = j2 & 0xff00ff00 | ((j2 & 0xff) << 16 | (0xff0000 & j2) >> 16);
                    l1++;
                    j1++;
                    l++;
                }

            }

        }
    }

    protected void convertToRGBA(int ai[], int i, int j, int k)
    {
        int l;
        boolean flag;
        boolean flag1;
        boolean flag2;
        k = 0;
        l = 0;
        flag = false;
        flag1 = false;
        flag2 = false;
        j = 0;
        if(!PGL.BIG_ENDIAN) goto _L2; else goto _L1
_L1:
        i;
        JVM INSTR tableswitch 1 4: default 56
    //                   1 92
    //                   2 125
    //                   3 56
    //                   4 67;
           goto _L3 _L4 _L5 _L3 _L6
_L3:
        rgbaPixUpdateCount = rgbaPixUpdateCount + 1;
        return;
_L6:
        while(j < ai.length) 
        {
            rgbaPixels[j] = ai[j] | 0xffffff00;
            j++;
        }
        continue; /* Loop/switch isn't completed */
_L4:
        while(k < ai.length) 
        {
            i = ai[k];
            rgbaPixels[k] = i << 8 | 0xff;
            k++;
        }
        continue; /* Loop/switch isn't completed */
_L5:
        while(l < ai.length) 
        {
            i = ai[l];
            rgbaPixels[l] = i >> 24 & 0xff | i << 8;
            l++;
        }
        if(true) goto _L3; else goto _L2
_L2:
        j = ((flag) ? 1 : 0);
        k = ((flag1) ? 1 : 0);
        int i1 = ((flag2) ? 1 : 0);
        switch(i)
        {
        case 1: // '\001'
            while(j < ai.length) 
            {
                i = ai[j];
                rgbaPixels[j] = i & 0xff00 | (0xff000000 | (i & 0xff) << 16 | (i & 0xff0000) >> 16);
                j++;
            }
            break;

        case 4: // '\004'
            while(k < ai.length) 
            {
                rgbaPixels[k] = ai[k] << 24 | 0xffffff;
                k++;
            }
            break;

        case 2: // '\002'
            for(; i1 < ai.length; i1++)
            {
                i = ai[i1];
                rgbaPixels[i1] = i & 0xff00ff00 | ((i & 0xff) << 16 | (i & 0xff0000) >> 16);
            }

            break;
        }
        if(true) goto _L3; else goto _L7
_L7:
    }

    public void copyBufferFromSource(Object obj, ByteBuffer bytebuffer, int i, int j)
    {
        if(bufferCache == null)
            bufferCache = new LinkedList();
        if(bufferCache.size() + 1 <= 3)
            bufferCache.add(new BufferData(obj, bytebuffer.asIntBuffer(), i, j));
        else
            try
            {
                LinkedList linkedlist = usedBuffers;
                BufferData bufferdata = JVM INSTR new #8   <Class Texture$BufferData>;
                bufferdata.this. BufferData(obj, bytebuffer.asIntBuffer(), i, j);
                linkedlist.add(bufferdata);
            }
            // Misplaced declaration of an exception variable
            catch(Object obj)
            {
                ((Exception) (obj)).printStackTrace();
            }
    }

    protected void copyObject(Texture texture)
    {
        dispose();
        width = texture.width;
        height = texture.height;
        glName = texture.glName;
        glTarget = texture.glTarget;
        glFormat = texture.glFormat;
        glMinFilter = texture.glMinFilter;
        glMagFilter = texture.glMagFilter;
        glWidth = texture.glWidth;
        glHeight = texture.glHeight;
        usingMipmaps = texture.usingMipmaps;
        usingRepeat = texture.usingRepeat;
        maxTexcoordU = texture.maxTexcoordU;
        maxTexcoordV = texture.maxTexcoordV;
        invertedX = texture.invertedX;
        invertedY = texture.invertedY;
    }

    protected void copyTexture(int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1, boolean flag)
    {
        if(tempFbo == null)
            tempFbo = new FrameBuffer(pg, glWidth, glHeight);
        tempFbo.setColorBuffer(this);
        tempFbo.disableDepthTest();
        pg.pushFramebuffer();
        pg.setFramebuffer(tempFbo);
        pg.pushStyle();
        pg.blendMode(0);
        if(flag)
            pgl.drawTexture(i, j, k, l, 0, 0, tempFbo.width, tempFbo.height, i1, j1, k1, l1, 0, 0, width, height);
        else
            pgl.drawTexture(i, j, k, l, 0, 0, tempFbo.width, tempFbo.height, i1, j1, k1, l1, i1, j1, k1, l1);
        pgl.flush();
        pg.popStyle();
        pg.popFramebuffer();
        updateTexels(i1, j1, k1, l1);
    }

    protected void copyTexture(Texture texture, int i, int j, int k, int l, boolean flag)
    {
        if(texture == null)
            throw new RuntimeException("Source texture is null");
        if(tempFbo == null)
            tempFbo = new FrameBuffer(pg, glWidth, glHeight);
        tempFbo.setColorBuffer(this);
        tempFbo.disableDepthTest();
        pg.pushFramebuffer();
        pg.setFramebuffer(tempFbo);
        pg.pushStyle();
        pg.blendMode(0);
        if(flag)
            pgl.drawTexture(texture.glTarget, texture.glName, texture.glWidth, texture.glHeight, 0, 0, tempFbo.width, tempFbo.height, 1, i, j, i + k, j + l, 0, 0, width, height);
        else
            pgl.drawTexture(texture.glTarget, texture.glName, texture.glWidth, texture.glHeight, 0, 0, tempFbo.width, tempFbo.height, 1, i, j, i + k, j + l, i, j, i + k, j + l);
        pgl.flush();
        pg.popStyle();
        pg.popFramebuffer();
        updateTexels(i, j, k, l);
    }

    public int currentSampling()
    {
        if(glMagFilter != PGL.NEAREST || glMinFilter != PGL.NEAREST) goto _L2; else goto _L1
_L1:
        int i = 2;
_L4:
        return i;
_L2:
        if(glMagFilter == PGL.NEAREST)
        {
            int j = glMinFilter;
            if(PGL.MIPMAPS_ENABLED)
                i = PGL.LINEAR_MIPMAP_NEAREST;
            else
                i = PGL.LINEAR;
            if(j == i)
            {
                i = 3;
                continue; /* Loop/switch isn't completed */
            }
        }
        if(glMagFilter == PGL.LINEAR)
        {
            int k = glMinFilter;
            if(PGL.MIPMAPS_ENABLED)
                i = PGL.LINEAR_MIPMAP_NEAREST;
            else
                i = PGL.LINEAR;
            if(k == i)
            {
                i = 4;
                continue; /* Loop/switch isn't completed */
            }
        }
        if(glMagFilter == PGL.LINEAR && glMinFilter == PGL.LINEAR_MIPMAP_LINEAR)
            i = 5;
        else
            i = -1;
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected void dispose()
    {
        if(glres != null)
        {
            glres.dispose();
            glres = null;
            glName = 0;
        }
    }

    public void disposeSourceBuffer()
    {
        if(usedBuffers != null)
            while(usedBuffers.size() > 0) 
            {
                BufferData bufferdata;
                try
                {
                    bufferdata = (BufferData)usedBuffers.remove(0);
                }
                catch(NoSuchElementException nosuchelementexception)
                {
                    PGraphics.showWarning("Cannot remove used buffer");
                    nosuchelementexception = null;
                }
                if(bufferdata != null)
                    bufferdata.dispose();
            }
    }

    protected void fillEdges(int i, int j, int k, int l)
    {
        if((width < glWidth || height < glHeight) && (i + k == width || j + l == height))
        {
            if(i + k == width)
            {
                int i1 = glWidth - width;
                edgePixels = new int[l * i1];
                for(int k1 = 0; k1 < l; k1++)
                {
                    int i2 = rgbaPixels[k1 * k + (k - 1)];
                    Arrays.fill(edgePixels, k1 * i1, (k1 + 1) * i1, i2);
                }

                edgeBuffer = PGL.updateIntBuffer(edgeBuffer, edgePixels, true);
                pgl.texSubImage2D(glTarget, 0, width, j, i1, l, PGL.RGBA, PGL.UNSIGNED_BYTE, edgeBuffer);
            }
            if(j + l == height)
            {
                int j1 = glHeight - height;
                edgePixels = new int[j1 * k];
                for(int l1 = 0; l1 < j1; l1++)
                    System.arraycopy(rgbaPixels, (l - 1) * k, edgePixels, l1 * k, k);

                edgeBuffer = PGL.updateIntBuffer(edgeBuffer, edgePixels, true);
                pgl.texSubImage2D(glTarget, 0, i, height, k, j1, PGL.RGBA, PGL.UNSIGNED_BYTE, edgeBuffer);
            }
            if(i + k == width && j + l == height)
            {
                i = glWidth - width;
                j = glHeight - height;
                k = rgbaPixels[k * l - 1];
                edgePixels = new int[j * i];
                Arrays.fill(edgePixels, 0, j * i, k);
                edgeBuffer = PGL.updateIntBuffer(edgeBuffer, edgePixels, true);
                pgl.texSubImage2D(glTarget, 0, width, height, i, j, PGL.RGBA, PGL.UNSIGNED_BYTE, edgeBuffer);
            }
        }
    }

    protected void flipArrayOnX(int ai[], int i)
    {
        int j = (width - 1) * i;
        int k = 0;
        for(int l = 0; l < width / 2; l++)
        {
            for(int i1 = 0; i1 < height; i1++)
            {
                int j1 = width;
                int l1 = width;
                int i2 = k + i * i1 * j1;
                l1 = i * i1 * l1 + j;
                for(int k1 = 0; k1 < i; k1++)
                {
                    int j2 = ai[i2];
                    ai[i2] = ai[l1];
                    ai[l1] = j2;
                    i2++;
                    l1++;
                }

            }

            k += i;
            j -= i;
        }

    }

    protected void flipArrayOnY(int ai[], int i)
    {
        int j = (height - 1) * i * width;
        int k = 0;
        for(int l = 0; l < height / 2; l++)
        {
            for(int i1 = 0; i1 < width * i;)
            {
                int j1 = ai[k];
                ai[k] = ai[j];
                ai[j] = j1;
                i1++;
                j++;
                k++;
            }

            j -= width * i * 2;
        }

    }

    public void get(int ai[])
    {
        if(ai == null)
            throw new RuntimeException("Trying to copy texture to null pixels array");
        if(ai.length != width * height)
            throw new RuntimeException("Trying to copy texture to pixels array of wrong size");
        if(tempFbo == null)
            tempFbo = new FrameBuffer(pg, glWidth, glHeight);
        tempFbo.setColorBuffer(this);
        pg.pushFramebuffer();
        pg.setFramebuffer(tempFbo);
        tempFbo.readPixels();
        pg.popFramebuffer();
        tempFbo.getPixels(ai);
        convertToARGB(ai);
        if(invertedX)
            flipArrayOnX(ai, 1);
        if(invertedY)
            flipArrayOnY(ai, 1);
    }

    public void getBufferPixels(int ai[])
    {
        Object obj = null;
        if(usedBuffers == null || usedBuffers.size() <= 0) goto _L2; else goto _L1
_L1:
        BufferData bufferdata = (BufferData)usedBuffers.getLast();
_L4:
        if(bufferdata != null)
        {
            if(bufferdata.w != width || bufferdata.h != height)
                init(bufferdata.w, bufferdata.h);
            bufferdata.rgbBuf.rewind();
            bufferdata.rgbBuf.get(ai);
            convertToARGB(ai);
            if(usedBuffers == null)
                usedBuffers = new LinkedList();
            for(; bufferCache.size() > 0; usedBuffers.add(ai))
                ai = (BufferData)bufferCache.remove(0);

        }
        break; /* Loop/switch isn't completed */
_L2:
        bufferdata = obj;
        if(bufferCache != null)
        {
            bufferdata = obj;
            if(bufferCache.size() > 0)
                bufferdata = (BufferData)bufferCache.getLast();
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public int getModifiedX1()
    {
        return mx1;
    }

    public int getModifiedX2()
    {
        return mx2;
    }

    public int getModifiedY1()
    {
        return my1;
    }

    public int getModifiedY2()
    {
        return my2;
    }

    public Parameters getParameters()
    {
        Parameters parameters = new Parameters();
        if(glTarget == PGL.TEXTURE_2D)
            parameters.target = 0;
        if(glFormat == PGL.RGB)
            parameters.format = 1;
        else
        if(glFormat == PGL.RGBA)
            parameters.format = 2;
        else
        if(glFormat == PGL.ALPHA)
            parameters.format = 4;
        if(glMagFilter == PGL.NEAREST && glMinFilter == PGL.NEAREST)
        {
            parameters.sampling = 2;
            parameters.mipmaps = false;
        } else
        if(glMagFilter == PGL.NEAREST && glMinFilter == PGL.LINEAR)
        {
            parameters.sampling = 3;
            parameters.mipmaps = false;
        } else
        if(glMagFilter == PGL.NEAREST && glMinFilter == PGL.LINEAR_MIPMAP_NEAREST)
        {
            parameters.sampling = 3;
            parameters.mipmaps = true;
        } else
        if(glMagFilter == PGL.LINEAR && glMinFilter == PGL.LINEAR)
        {
            parameters.sampling = 4;
            parameters.mipmaps = false;
        } else
        if(glMagFilter == PGL.LINEAR && glMinFilter == PGL.LINEAR_MIPMAP_NEAREST)
        {
            parameters.sampling = 4;
            parameters.mipmaps = true;
        } else
        if(glMagFilter == PGL.LINEAR && glMinFilter == PGL.LINEAR_MIPMAP_LINEAR)
        {
            parameters.sampling = 5;
            parameters.mipmaps = true;
        }
        if(glWrapS == PGL.CLAMP_TO_EDGE)
            parameters.wrapU = 0;
        else
        if(glWrapS == PGL.REPEAT)
            parameters.wrapU = 1;
        if(glWrapT == PGL.CLAMP_TO_EDGE)
            parameters.wrapV = 0;
        else
        if(glWrapT == PGL.REPEAT)
            parameters.wrapV = 1;
        return parameters;
    }

    protected void getSourceMethods()
    {
        try
        {
            disposeBufferMethod = bufferSource.getClass().getMethod("disposeBuffer", new Class[] {
                java/lang/Object
            });
            return;
        }
        catch(Exception exception)
        {
            throw new RuntimeException("Provided source object doesn't have a disposeBuffer method.");
        }
    }

    public boolean hasBufferSource()
    {
        boolean flag;
        if(bufferSource != null)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public boolean hasBuffers()
    {
        boolean flag;
        if(bufferSource != null && bufferCache != null && bufferCache.size() > 0)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public void init(int i, int j)
    {
        Parameters parameters;
        if(glName > 0)
            parameters = getParameters();
        else
            parameters = new Parameters();
        init(i, j, parameters);
    }

    public void init(int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1, int i2, int j2, int k2)
    {
label0:
        {
            boolean flag = false;
            width = i;
            height = j;
            glName = k;
            glTarget = l;
            glFormat = i1;
            glWidth = j1;
            glHeight = k1;
            glMinFilter = l1;
            glMagFilter = i2;
            glWrapS = j2;
            glWrapT = k2;
            maxTexcoordU = (float)i / (float)j1;
            maxTexcoordV = (float)j / (float)k1;
            boolean flag1;
            if(l1 == PGL.LINEAR_MIPMAP_NEAREST || l1 == PGL.LINEAR_MIPMAP_LINEAR)
                flag1 = true;
            else
                flag1 = false;
            usingMipmaps = flag1;
            if(j2 != PGL.REPEAT)
            {
                flag1 = flag;
                if(k2 != PGL.REPEAT)
                    break label0;
            }
            flag1 = true;
        }
        usingRepeat = flag1;
    }

    public void init(int i, int j, Parameters parameters)
    {
        setParameters(parameters);
        setSize(i, j);
        allocate();
    }

    public void invertedX(boolean flag)
    {
        invertedX = flag;
    }

    public boolean invertedX()
    {
        return invertedX;
    }

    public void invertedY(boolean flag)
    {
        invertedY = flag;
    }

    public boolean invertedY()
    {
        return invertedY;
    }

    public boolean isModified()
    {
        return modified;
    }

    protected void loadPixels(int i)
    {
        if(rgbaPixels == null || rgbaPixels.length < i)
            rgbaPixels = new int[i];
    }

    protected void manualMipmap()
    {
    }

    public float maxTexcoordU()
    {
        return maxTexcoordU;
    }

    public float maxTexcoordV()
    {
        return maxTexcoordV;
    }

    public void put(int i, int j, int k, int l, int i1, int j1)
    {
        copyTexture(i, j, k, l, 0, 0, i1, j1, false);
    }

    public void put(int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1, int i2, int j2)
    {
        copyTexture(i, j, k, l, k1, l1, i2, j2, false);
    }

    public void put(Texture texture)
    {
        copyTexture(texture, 0, 0, texture.width, texture.height, false);
    }

    public void put(Texture texture, int i, int j, int k, int l)
    {
        copyTexture(texture, i, j, k, l, false);
    }

    protected void releasePixelBuffer()
    {
        double d = (double)Runtime.getRuntime().freeMemory() / 1000000D;
        if(pixBufUpdateCount < 10 || d < 5D)
            pixelBuffer = null;
    }

    protected void releaseRGBAPixels()
    {
        double d = (double)Runtime.getRuntime().freeMemory() / 1000000D;
        if(rgbaPixUpdateCount < 10 || d < 5D)
            rgbaPixels = null;
    }

    public void resize(int i, int j)
    {
        dispose();
        Texture texture = new Texture(pg, i, j, getParameters());
        texture.set(this);
        copyObject(texture);
        tempFbo = null;
    }

    public void set(int i, int j, int k, int l, int i1, int j1)
    {
        copyTexture(i, j, k, l, 0, 0, i1, j1, true);
    }

    public void set(int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1, int i2, int j2)
    {
        copyTexture(i, j, k, l, k1, l1, i2, j2, true);
    }

    public void set(Texture texture)
    {
        copyTexture(texture, 0, 0, texture.width, texture.height, true);
    }

    public void set(Texture texture, int i, int j, int k, int l)
    {
        copyTexture(texture, i, j, k, l, true);
    }

    public void set(int ai[])
    {
        set(ai, 0, 0, width, height, 2);
    }

    public void set(int ai[], int i)
    {
        set(ai, 0, 0, width, height, i);
    }

    public void set(int ai[], int i, int j, int k, int l)
    {
        set(ai, i, j, k, l, 2);
    }

    public void set(int ai[], int i, int j, int k, int l, int i1)
    {
        if(ai != null) goto _L2; else goto _L1
_L1:
        PGraphics.showWarning("The pixels array is null.");
_L4:
        return;
_L2:
        if(ai.length < k * l)
            PGraphics.showWarning((new StringBuilder()).append("The pixel array has a length of ").append(ai.length).append(", but it should be at least ").append(k * l).toString());
        else
        if(ai.length != 0 && k != 0 && l != 0)
        {
            boolean flag;
            if(!pgl.texturingIsEnabled(glTarget))
            {
                pgl.enableTexturing(glTarget);
                flag = true;
            } else
            {
                flag = false;
            }
            pgl.bindTexture(glTarget, glName);
            loadPixels(k * l);
            convertToRGBA(ai, i1, k, l);
            updatePixelBuffer(rgbaPixels);
            pgl.texSubImage2D(glTarget, 0, i, j, k, l, PGL.RGBA, PGL.UNSIGNED_BYTE, pixelBuffer);
            fillEdges(i, j, k, l);
            if(usingMipmaps)
                if(PGraphicsOpenGL.autoMipmapGenSupported)
                    pgl.generateMipmap(glTarget);
                else
                    manualMipmap();
            pgl.bindTexture(glTarget, 0);
            if(flag)
                pgl.disableTexturing(glTarget);
            releasePixelBuffer();
            releaseRGBAPixels();
            updateTexels(i, j, k, l);
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void setBufferSource(Object obj)
    {
        bufferSource = obj;
        getSourceMethods();
    }

    public void setModified()
    {
        modified = true;
    }

    public void setModified(boolean flag)
    {
        modified = flag;
    }

    public void setNative(IntBuffer intbuffer, int i, int j, int k, int l)
    {
        if(intbuffer != null) goto _L2; else goto _L1
_L1:
        PGraphics.showWarning("The pixel buffer is null.");
_L4:
        return;
_L2:
        if(intbuffer.capacity() < k * l)
            PGraphics.showWarning((new StringBuilder()).append("The pixel bufer has a length of ").append(intbuffer.capacity()).append(", but it should be at least ").append(k * l).toString());
        else
        if(intbuffer.capacity() != 0)
        {
            boolean flag;
            if(!pgl.texturingIsEnabled(glTarget))
            {
                pgl.enableTexturing(glTarget);
                flag = true;
            } else
            {
                flag = false;
            }
            pgl.bindTexture(glTarget, glName);
            pgl.texSubImage2D(glTarget, 0, i, j, k, l, PGL.RGBA, PGL.UNSIGNED_BYTE, intbuffer);
            fillEdges(i, j, k, l);
            if(usingMipmaps)
                if(PGraphicsOpenGL.autoMipmapGenSupported)
                    pgl.generateMipmap(glTarget);
                else
                    manualMipmap();
            pgl.bindTexture(glTarget, 0);
            if(flag)
                pgl.disableTexturing(glTarget);
            updateTexels(i, j, k, l);
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void setNative(int ai[])
    {
        setNative(ai, 0, 0, width, height);
    }

    public void setNative(int ai[], int i, int j, int k, int l)
    {
        updatePixelBuffer(ai);
        setNative(pixelBuffer, i, j, k, l);
        releasePixelBuffer();
    }

    protected void setParameters(Parameters parameters)
    {
        boolean flag = true;
        if(parameters.target != 0) goto _L2; else goto _L1
_L1:
        boolean flag3;
        glTarget = PGL.TEXTURE_2D;
        boolean flag1;
        boolean flag2;
        if(parameters.format == 1)
            glFormat = PGL.RGB;
        else
        if(parameters.format == 2)
            glFormat = PGL.RGBA;
        else
        if(parameters.format == 4)
            glFormat = PGL.ALPHA;
        else
            throw new RuntimeException("Unknown texture format");
        if(parameters.mipmaps && PGL.MIPMAPS_ENABLED)
            flag1 = true;
        else
            flag1 = false;
        flag2 = flag1;
        if(flag1)
        {
            flag2 = flag1;
            if(!PGraphicsOpenGL.autoMipmapGenSupported)
            {
                PGraphics.showWarning("Mipmaps were requested but automatic mipmap generation is not supported and manual generation still not implemented, so mipmaps will be disabled.");
                flag2 = false;
            }
        }
        if(parameters.sampling == 2)
        {
            glMagFilter = PGL.NEAREST;
            glMinFilter = PGL.NEAREST;
        } else
        if(parameters.sampling == 3)
        {
            glMagFilter = PGL.NEAREST;
            int i;
            if(flag2)
                i = PGL.LINEAR_MIPMAP_NEAREST;
            else
                i = PGL.LINEAR;
            glMinFilter = i;
        } else
        if(parameters.sampling == 4)
        {
            glMagFilter = PGL.LINEAR;
            int j;
            if(flag2)
                j = PGL.LINEAR_MIPMAP_NEAREST;
            else
                j = PGL.LINEAR;
            glMinFilter = j;
        } else
        if(parameters.sampling == 5)
        {
            glMagFilter = PGL.LINEAR;
            int k;
            if(flag2)
                k = PGL.LINEAR_MIPMAP_LINEAR;
            else
                k = PGL.LINEAR;
            glMinFilter = k;
        } else
        {
            throw new RuntimeException("Unknown texture filtering mode");
        }
_L4:
        if(parameters.wrapU == 0)
            glWrapS = PGL.CLAMP_TO_EDGE;
        else
        if(parameters.wrapU == 1)
            glWrapS = PGL.REPEAT;
        else
            throw new RuntimeException("Unknown wrapping mode");
        if(parameters.wrapV == 0)
        {
            glWrapT = PGL.CLAMP_TO_EDGE;
        } else
        {
label0:
            {
                if(parameters.wrapV != 1)
                    break label0;
                glWrapT = PGL.REPEAT;
            }
        }
        if(glMinFilter == PGL.LINEAR_MIPMAP_NEAREST || glMinFilter == PGL.LINEAR_MIPMAP_LINEAR)
            flag3 = true;
        else
            flag3 = false;
        usingMipmaps = flag3;
        flag3 = flag;
        if(glWrapS != PGL.REPEAT)
            if(glWrapT == PGL.REPEAT)
                flag3 = flag;
            else
                flag3 = false;
        usingRepeat = flag3;
        invertedX = false;
        invertedY = false;
        return;
_L2:
        throw new RuntimeException("Unknown texture target");
        throw new RuntimeException("Unknown wrapping mode");
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected void setSize(int i, int j)
    {
        width = i;
        height = j;
        if(PGraphicsOpenGL.npotTexSupported)
        {
            glWidth = i;
            glHeight = j;
        } else
        {
            glWidth = PGL.nextPowerOfTwo(i);
            glHeight = PGL.nextPowerOfTwo(j);
        }
        if(glWidth > PGraphicsOpenGL.maxTextureSize || glHeight > PGraphicsOpenGL.maxTextureSize)
        {
            glHeight = 0;
            glWidth = 0;
            throw new RuntimeException((new StringBuilder()).append("Image width and height cannot be larger than ").append(PGraphicsOpenGL.maxTextureSize).append(" with this graphics card.").toString());
        } else
        {
            maxTexcoordU = (float)width / (float)glWidth;
            maxTexcoordV = (float)height / (float)glHeight;
            return;
        }
    }

    public void unbind()
    {
        if(pgl.textureIsBound(glTarget, glName))
            if(!pgl.texturingIsEnabled(glTarget))
            {
                pgl.enableTexturing(glTarget);
                pgl.bindTexture(glTarget, 0);
                pgl.disableTexturing(glTarget);
            } else
            {
                pgl.bindTexture(glTarget, 0);
            }
        bound = false;
    }

    protected void updatePixelBuffer(int ai[])
    {
        pixelBuffer = PGL.updateIntBuffer(pixelBuffer, ai, true);
        pixBufUpdateCount = pixBufUpdateCount + 1;
    }

    public void updateTexels()
    {
        updateTexelsImpl(0, 0, width, height);
    }

    public void updateTexels(int i, int j, int k, int l)
    {
        updateTexelsImpl(i, j, k, l);
    }

    protected void updateTexelsImpl(int i, int j, int k, int l)
    {
        k = i + k;
        l = j + l;
        if(modified) goto _L2; else goto _L1
_L1:
        mx1 = PApplet.max(0, i);
        mx2 = PApplet.min(width - 1, k);
        my1 = PApplet.max(0, j);
        my2 = PApplet.min(height - 1, l);
        modified = true;
_L4:
        return;
_L2:
        if(i < mx1)
            mx1 = PApplet.max(0, i);
        if(i > mx2)
            mx2 = PApplet.min(width - 1, i);
        if(j < my1)
            my1 = PApplet.max(0, j);
        if(j > my2)
            my2 = j;
        if(k < mx1)
            mx1 = PApplet.max(0, k);
        if(k > mx2)
            mx2 = PApplet.min(width - 1, k);
        if(l < my1)
            my1 = PApplet.max(0, l);
        if(l > my2)
            my2 = PApplet.min(height - 1, l);
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void usingMipmaps(boolean flag, int i)
    {
        int j = glMagFilter;
        int k = glMinFilter;
        if(flag)
        {
            if(i == 2)
            {
                glMagFilter = PGL.NEAREST;
                glMinFilter = PGL.NEAREST;
                usingMipmaps = false;
            } else
            if(i == 3)
            {
                glMagFilter = PGL.NEAREST;
                if(PGL.MIPMAPS_ENABLED)
                    i = PGL.LINEAR_MIPMAP_NEAREST;
                else
                    i = PGL.LINEAR;
                glMinFilter = i;
                usingMipmaps = true;
            } else
            if(i == 4)
            {
                glMagFilter = PGL.LINEAR;
                if(PGL.MIPMAPS_ENABLED)
                    i = PGL.LINEAR_MIPMAP_NEAREST;
                else
                    i = PGL.LINEAR;
                glMinFilter = i;
                usingMipmaps = true;
            } else
            if(i == 5)
            {
                glMagFilter = PGL.LINEAR;
                if(PGL.MIPMAPS_ENABLED)
                    i = PGL.LINEAR_MIPMAP_LINEAR;
                else
                    i = PGL.LINEAR;
                glMinFilter = i;
                usingMipmaps = true;
            } else
            {
                throw new RuntimeException("Unknown texture filtering mode");
            }
        } else
        {
            usingMipmaps = false;
            if(i == 2)
            {
                glMagFilter = PGL.NEAREST;
                glMinFilter = PGL.NEAREST;
            } else
            if(i == 3)
            {
                glMagFilter = PGL.NEAREST;
                glMinFilter = PGL.LINEAR;
            } else
            if(i == 4 || i == 5)
            {
                glMagFilter = PGL.LINEAR;
                glMinFilter = PGL.LINEAR;
            } else
            {
                throw new RuntimeException("Unknown texture filtering mode");
            }
        }
        if(j != glMagFilter || k != glMinFilter)
        {
            bind();
            pgl.texParameteri(glTarget, PGL.TEXTURE_MIN_FILTER, glMinFilter);
            pgl.texParameteri(glTarget, PGL.TEXTURE_MAG_FILTER, glMagFilter);
            if(usingMipmaps)
                if(PGraphicsOpenGL.autoMipmapGenSupported)
                    pgl.generateMipmap(glTarget);
                else
                    manualMipmap();
            unbind();
        }
    }

    public boolean usingMipmaps()
    {
        return usingMipmaps;
    }

    public void usingRepeat(boolean flag)
    {
        if(flag)
        {
            glWrapS = PGL.REPEAT;
            glWrapT = PGL.REPEAT;
            usingRepeat = true;
        } else
        {
            glWrapS = PGL.CLAMP_TO_EDGE;
            glWrapT = PGL.CLAMP_TO_EDGE;
            usingRepeat = false;
        }
        bind();
        pgl.texParameteri(glTarget, PGL.TEXTURE_WRAP_S, glWrapS);
        pgl.texParameteri(glTarget, PGL.TEXTURE_WRAP_T, glWrapT);
        unbind();
    }

    public boolean usingRepeat()
    {
        return usingRepeat;
    }

    protected static final int BILINEAR = 4;
    protected static final int LINEAR = 3;
    public static final int MAX_BUFFER_CACHE_SIZE = 3;
    protected static final int MAX_UPDATES = 10;
    protected static final int MIN_MEMORY = 5;
    protected static final int POINT = 2;
    protected static final int TEX2D = 0;
    protected static final int TEXRECT = 1;
    protected static final int TRILINEAR = 5;
    protected boolean bound;
    protected LinkedList bufferCache;
    protected Object bufferSource;
    protected boolean colorBuffer;
    protected int context;
    protected Method disposeBufferMethod;
    protected IntBuffer edgeBuffer;
    protected int edgePixels[];
    public int glFormat;
    public int glHeight;
    public int glMagFilter;
    public int glMinFilter;
    public int glName;
    public int glTarget;
    public int glWidth;
    public int glWrapS;
    public int glWrapT;
    private PGraphicsOpenGL.GLResourceTexture glres;
    public int height;
    protected boolean invertedX;
    protected boolean invertedY;
    protected float maxTexcoordU;
    protected float maxTexcoordV;
    protected boolean modified;
    protected int mx1;
    protected int mx2;
    protected int my1;
    protected int my2;
    protected PGraphicsOpenGL pg;
    protected PGL pgl;
    protected int pixBufUpdateCount;
    protected IntBuffer pixelBuffer;
    protected int rgbaPixUpdateCount;
    protected int rgbaPixels[];
    protected FrameBuffer tempFbo;
    protected LinkedList usedBuffers;
    protected boolean usingMipmaps;
    protected boolean usingRepeat;
    public int width;
}
