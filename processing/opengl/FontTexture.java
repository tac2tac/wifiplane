// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.opengl;

import java.util.Arrays;
import java.util.HashMap;
import processing.core.*;

// Referenced classes of package processing.opengl:
//            PGraphicsOpenGL, Texture, PGL

class FontTexture
    implements PConstants
{
    class TextureInfo
    {

        void updateTex()
        {
            textures[texIndex].setNative(pixels, crop[0] - 1, (crop[1] + crop[3]) - 1, crop[2] + 2, -crop[3] + 2);
        }

        void updateUV()
        {
            width = textures[texIndex].glWidth;
            height = textures[texIndex].glHeight;
            u0 = (float)crop[0] / (float)width;
            u1 = u0 + (float)crop[2] / (float)width;
            v0 = (float)(crop[1] + crop[3]) / (float)height;
            v1 = v0 - (float)crop[3] / (float)height;
        }

        int crop[];
        int height;
        int pixels[];
        int texIndex;
        final FontTexture this$0;
        float u0;
        float u1;
        float v0;
        float v1;
        int width;

        TextureInfo(int i, int j, int k, int l, int i1, int ai[])
        {
            this$0 = FontTexture.this;
            super();
            texIndex = i;
            crop = new int[4];
            crop[0] = j + 1;
            crop[1] = (k + 1 + i1) - 2;
            crop[2] = l - 2;
            crop[3] = -i1 + 2;
            pixels = ai;
            updateUV();
            updateTex();
        }
    }


    public FontTexture(PGraphicsOpenGL pgraphicsopengl, PFont pfont, boolean flag)
    {
        textures = null;
        images = null;
        pgl = pgraphicsopengl.pgl;
        is3D = flag;
        initTexture(pgraphicsopengl, pfont);
    }

    public void addAllGlyphsToTexture(PGraphicsOpenGL pgraphicsopengl, PFont pfont)
    {
        for(int i = 0; i < pfont.getGlyphCount(); i++)
            addToTexture(pgraphicsopengl, i, pfont.getGlyph(i));

    }

    public boolean addTexture(PGraphicsOpenGL pgraphicsopengl)
    {
        int i = maxSize;
        int j;
        boolean flag;
        Texture texture;
        if(-1 < lastTex && textures[lastTex].glHeight < maxSize)
        {
            j = PApplet.min(textures[lastTex].glHeight * 2, maxSize);
            flag = true;
        } else
        {
            j = minSize;
            flag = false;
        }
        if(is3D)
            texture = new Texture(pgraphicsopengl, i, j, new Texture.Parameters(2, 4, false));
        else
            texture = new Texture(pgraphicsopengl, i, j, new Texture.Parameters(2, 3, false));
        if(textures == null)
        {
            textures = new Texture[1];
            textures[0] = texture;
            images = new PImage[1];
            images[0] = pgraphicsopengl.wrapTexture(texture);
            lastTex = 0;
        } else
        if(flag)
        {
            texture.put(textures[lastTex]);
            textures[lastTex] = texture;
            pgraphicsopengl.setCache(images[lastTex], texture);
            images[lastTex].width = texture.width;
            images[lastTex].height = texture.height;
        } else
        {
            lastTex = textures.length;
            Object aobj[] = new Texture[lastTex + 1];
            PApplet.arrayCopy(textures, ((Object) (aobj)), textures.length);
            aobj[lastTex] = texture;
            textures = ((Texture []) (aobj));
            aobj = new PImage[textures.length];
            PApplet.arrayCopy(images, ((Object) (aobj)), images.length);
            aobj[lastTex] = pgraphicsopengl.wrapTexture(texture);
            images = ((PImage []) (aobj));
        }
        texture.bind();
        return flag;
    }

    public TextureInfo addToTexture(PGraphicsOpenGL pgraphicsopengl, processing.core.PFont.Glyph glyph)
    {
        int i = glyphTexinfos.length;
        if(i == 0)
            glyphTexinfos = new TextureInfo[1];
        addToTexture(pgraphicsopengl, i, glyph);
        return glyphTexinfos[i];
    }

    protected void addToTexture(PGraphicsOpenGL pgraphicsopengl, int i, processing.core.PFont.Glyph glyph)
    {
        int j = glyph.width + 1 + 1;
        int k = glyph.height + 1 + 1;
        int ai[] = new int[j * k];
        int l = 0;
        int j1 = 0;
        if(PGL.BIG_ENDIAN)
        {
            Arrays.fill(ai, 0, j, -256);
            int l1 = 0;
            for(int j2 = j; l1 < glyph.height; j2 = l)
            {
                ai[j2] = -256;
                l = 0;
                j2++;
                while(l < glyph.width) 
                {
                    ai[j2] = glyph.image.pixels[j1] | 0xffffff00;
                    l++;
                    j2++;
                    j1++;
                }
                l = j2 + 1;
                ai[j2] = -256;
                l1++;
            }

            Arrays.fill(ai, (k - 1) * j, k * j, -256);
        } else
        {
            Arrays.fill(ai, 0, j, 0xffffff);
            int i2 = 0;
            int k2 = j;
            int k1 = l;
            while(i2 < glyph.height) 
            {
                ai[k2] = 0xffffff;
                int i1 = 0;
                k2++;
                while(i1 < glyph.width) 
                {
                    ai[k2] = glyph.image.pixels[k1] << 24 | 0xffffff;
                    i1++;
                    k2++;
                    k1++;
                }
                i1 = k2 + 1;
                ai[k2] = 0xffffff;
                i2++;
                k2 = i1;
            }
            Arrays.fill(ai, (k - 1) * j, k * j, 0xffffff);
        }
        if(offsetX + j > textures[lastTex].glWidth)
        {
            offsetX = 0;
            offsetY = offsetY + lineHeight;
        }
        lineHeight = Math.max(lineHeight, k);
        if(offsetY + lineHeight > textures[lastTex].glHeight)
            if(addTexture(pgraphicsopengl))
            {
                updateGlyphsTexCoords();
            } else
            {
                offsetX = 0;
                offsetY = 0;
                lineHeight = 0;
            }
        ai = new TextureInfo(lastTex, offsetX, offsetY, j, k, ai);
        offsetX = offsetX + j;
        if(i == glyphTexinfos.length)
        {
            pgraphicsopengl = new TextureInfo[glyphTexinfos.length + 1];
            System.arraycopy(glyphTexinfos, 0, pgraphicsopengl, 0, glyphTexinfos.length);
            glyphTexinfos = pgraphicsopengl;
        }
        glyphTexinfos[i] = ai;
        texinfoMap.put(glyph, ai);
    }

    protected void allocate()
    {
    }

    public void begin()
    {
    }

    public boolean contextIsOutdated()
    {
        boolean flag = false;
        int i = 0;
        boolean flag1 = false;
        for(; i < textures.length; i++)
            if(textures[i].contextIsOutdated())
                flag1 = true;

        if(flag1)
        {
            for(int j = ((flag) ? 1 : 0); j < textures.length; j++)
                textures[j].dispose();

        }
        return flag1;
    }

    protected void dispose()
    {
        for(int i = 0; i < textures.length; i++)
            textures[i].dispose();

    }

    public void end()
    {
        for(int i = 0; i < textures.length; i++)
            pgl.disableTexturing(textures[i].glTarget);

    }

    public TextureInfo getTexInfo(processing.core.PFont.Glyph glyph)
    {
        return (TextureInfo)texinfoMap.get(glyph);
    }

    public PImage getTexture(TextureInfo textureinfo)
    {
        return images[textureinfo.texIndex];
    }

    protected void initTexture(PGraphicsOpenGL pgraphicsopengl, PFont pfont)
    {
        lastTex = -1;
        int i = PGL.nextPowerOfTwo(pfont.getSize());
        minSize = PApplet.min(PGraphicsOpenGL.maxTextureSize, PApplet.max(PGL.MIN_FONT_TEX_SIZE, i));
        maxSize = PApplet.min(PGraphicsOpenGL.maxTextureSize, PApplet.max(PGL.MAX_FONT_TEX_SIZE, i * 2));
        if(maxSize < i)
            PGraphics.showWarning("The font size is too large to be properly displayed with OpenGL");
        addTexture(pgraphicsopengl);
        offsetX = 0;
        offsetY = 0;
        lineHeight = 0;
        texinfoMap = new HashMap();
        glyphTexinfos = new TextureInfo[pfont.getGlyphCount()];
        addAllGlyphsToTexture(pgraphicsopengl, pfont);
    }

    public void updateGlyphsTexCoords()
    {
        for(int i = 0; i < glyphTexinfos.length; i++)
        {
            TextureInfo textureinfo = glyphTexinfos[i];
            if(textureinfo != null && textureinfo.texIndex == lastTex)
                textureinfo.updateUV();
        }

    }

    protected TextureInfo glyphTexinfos[];
    protected PImage images[];
    protected boolean is3D;
    protected int lastTex;
    protected int lineHeight;
    protected int maxSize;
    protected int minSize;
    protected int offsetX;
    protected int offsetY;
    protected PGL pgl;
    protected HashMap texinfoMap;
    protected Texture textures[];
}
