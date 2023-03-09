// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.core;

import android.graphics.*;
import java.io.*;
import java.util.*;

// Referenced classes of package processing.core:
//            PConstants, PApplet, PGraphics, PImage

public class PFont
    implements PConstants
{
    public class Glyph
    {

        protected void readBitmap(DataInputStream datainputstream)
            throws IOException
        {
            image = new PImage(width, height, 4);
            byte abyte0[] = new byte[width * height];
            datainputstream.readFully(abyte0);
            int i = width;
            int j = height;
            datainputstream = image.pixels;
            for(int k = 0; k < j; k++)
            {
                for(int l = 0; l < i; l++)
                    datainputstream[width * k + l] = abyte0[k * i + l] & 0xff;

            }

            fromStream = true;
        }

        protected void readHeader(DataInputStream datainputstream)
            throws IOException
        {
            value = datainputstream.readInt();
            height = datainputstream.readInt();
            width = datainputstream.readInt();
            setWidth = datainputstream.readInt();
            topExtent = datainputstream.readInt();
            leftExtent = datainputstream.readInt();
            datainputstream.readInt();
            if(value == 100 && ascent == 0)
                ascent = topExtent;
            if(value == 112 && descent == 0)
                descent = -topExtent + height;
        }

        protected void writeBitmap(DataOutputStream dataoutputstream)
            throws IOException
        {
            int ai[] = image.pixels;
            for(int i = 0; i < height; i++)
            {
                for(int j = 0; j < width; j++)
                    dataoutputstream.write(ai[width * i + j] & 0xff);

            }

        }

        protected void writeHeader(DataOutputStream dataoutputstream)
            throws IOException
        {
            dataoutputstream.writeInt(value);
            dataoutputstream.writeInt(height);
            dataoutputstream.writeInt(width);
            dataoutputstream.writeInt(setWidth);
            dataoutputstream.writeInt(topExtent);
            dataoutputstream.writeInt(leftExtent);
            dataoutputstream.writeInt(0);
        }

        public boolean fromStream;
        public int height;
        public PImage image;
        public int index;
        public int leftExtent;
        public int setWidth;
        final PFont this$0;
        public int topExtent;
        public int value;
        public int width;

        protected Glyph()
        {
            this$0 = PFont.this;
            super();
            fromStream = false;
        }

        protected Glyph(char c)
        {
            boolean flag = false;
            this$0 = PFont.this;
            super();
            fromStream = false;
            int i = size * 3;
            lazyCanvas.drawColor(-1);
            lazyPaint.setColor(0xff000000);
            lazyCanvas.drawText(String.valueOf(c), size, size * 2, lazyPaint);
            lazyBitmap.getPixels(lazySamples, 0, i, 0, 0, i, i);
            int j = 0;
            int k = 0;
            int l = 0;
            int i1 = 1000;
            int j1 = 0;
            int k1 = 1000;
            while(j < i) 
            {
                boolean flag1 = false;
                int i2 = i1;
                int j2 = k;
                i1 = ((flag1) ? 1 : 0);
                k = k1;
                k1 = l;
                while(i1 < i) 
                {
                    int l1;
                    int ai[];
                    if((lazySamples[j * i + i1] & 0xff) != 255)
                    {
                        j2 = k;
                        if(i1 < k)
                            j2 = i1;
                        k = i2;
                        if(j < i2)
                            k = j;
                        i2 = j1;
                        if(i1 > j1)
                            i2 = i1;
                        j1 = k1;
                        if(j > k1)
                            j1 = j;
                        k1 = j1;
                        boolean flag2 = true;
                        l = j2;
                        j1 = i2;
                        i2 = k;
                        j2 = k1;
                        k1 = ((flag2) ? 1 : 0);
                    } else
                    {
                        l = k1;
                        k1 = j2;
                        j2 = l;
                        l = k;
                    }
                    l1 = i1 + 1;
                    i1 = j2;
                    j2 = k1;
                    k1 = i1;
                    k = l;
                    i1 = l1;
                }
                j++;
                i1 = i2;
                i2 = k;
                k = j2;
                l = k1;
                k1 = i2;
            }
            if(k == 0)
            {
                i1 = 0;
                j1 = 0;
                j = 0;
                k1 = ((flag) ? 1 : 0);
            } else
            {
                j = k1;
                k1 = l;
            }
            value = c;
            height = (k1 - i1) + 1;
            width = (j1 - j) + 1;
            setWidth = (int)lazyPaint.measureText(String.valueOf(c));
            topExtent = size * 2 - i1;
            leftExtent = j - size;
            image = new PImage(width, height, 4);
            ai = image.pixels;
            for(k = i1; k <= k1; k++)
                for(i2 = j; i2 <= j1; i2++)
                {
                    j2 = lazySamples[k * i + i2];
                    ai[(k - i1) * width + (i2 - j)] = 255 - (j2 & 0xff);
                }


            if(value == 100 && ascent == 0)
                ascent = topExtent;
            if(value == 112 && descent == 0)
                descent = -topExtent + height;
        }

        protected Glyph(DataInputStream datainputstream)
            throws IOException
        {
            this$0 = PFont.this;
            super();
            fromStream = false;
            readHeader(datainputstream);
        }
    }


    public PFont()
    {
    }

    public PFont(Typeface typeface1, int i, boolean flag)
    {
        this(typeface1, i, flag, null);
    }

    public PFont(Typeface typeface1, int i, boolean flag, char ac[])
    {
        boolean flag1 = false;
        super();
        typeface = typeface1;
        smooth = flag;
        name = "";
        psname = "";
        size = i;
        glyphs = new Glyph[10];
        ascii = new int[128];
        Arrays.fill(ascii, -1);
        int k = i * 3;
        lazyBitmap = Bitmap.createBitmap(k, k, android.graphics.Bitmap.Config.ARGB_8888);
        lazyCanvas = new Canvas(lazyBitmap);
        lazyPaint = new Paint();
        lazyPaint.setAntiAlias(flag);
        lazyPaint.setTypeface(typeface1);
        lazyPaint.setTextSize(i);
        lazySamples = new int[k * k];
        if(ac == null)
        {
            lazy = true;
        } else
        {
            Arrays.sort(ac);
            glyphs = new Glyph[ac.length];
            glyphCount = 0;
            int l = ac.length;
            i = ((flag1) ? 1 : 0);
            while(i < l) 
            {
                typeface1 = new Glyph(ac[i]);
                if(((Glyph) (typeface1)).value < 128)
                    ascii[((Glyph) (typeface1)).value] = glyphCount;
                typeface1.index = glyphCount;
                Glyph aglyph[] = glyphs;
                int j = glyphCount;
                glyphCount = j + 1;
                aglyph[j] = typeface1;
                i++;
            }
        }
        if(ascent == 0)
        {
            new Glyph('d');
            if(ascent == 0)
                ascent = PApplet.round(lazyPaint.ascent());
        }
        if(descent == 0)
        {
            new Glyph('p');
            if(descent == 0)
                descent = PApplet.round(lazyPaint.descent());
        }
    }

    public PFont(InputStream inputstream)
        throws IOException
    {
        boolean flag = false;
        super();
        inputstream = new DataInputStream(inputstream);
        glyphCount = inputstream.readInt();
        int i = inputstream.readInt();
        size = inputstream.readInt();
        inputstream.readInt();
        ascent = inputstream.readInt();
        descent = inputstream.readInt();
        glyphs = new Glyph[glyphCount];
        ascii = new int[128];
        Arrays.fill(ascii, -1);
        for(int j = 0; j < glyphCount; j++)
        {
            Glyph glyph = new Glyph(inputstream);
            if(glyph.value < 128)
                ascii[glyph.value] = j;
            glyph.index = j;
            glyphs[j] = glyph;
        }

        if(ascent == 0 && descent == 0)
            throw new RuntimeException("Please use \"Create Font\" to re-create this font.");
        Glyph aglyph[] = glyphs;
        int l = aglyph.length;
        for(int k = ((flag) ? 1 : 0); k < l; k++)
            aglyph[k].readBitmap(inputstream);

        if(i >= 10)
        {
            name = inputstream.readUTF();
            psname = inputstream.readUTF();
        }
        if(i == 11)
            smooth = inputstream.readBoolean();
    }

    public static Object findNative(String s)
    {
        loadTypefaces();
        return typefaceMap.get(s);
    }

    public static String[] list()
    {
        loadTypefaces();
        return fontList;
    }

    public static void loadTypefaces()
    {
        if(typefaceMap == null)
        {
            typefaceMap = new HashMap();
            typefaceMap.put("Serif", Typeface.create(Typeface.SERIF, 0));
            typefaceMap.put("Serif-Bold", Typeface.create(Typeface.SERIF, 1));
            typefaceMap.put("Serif-Italic", Typeface.create(Typeface.SERIF, 2));
            typefaceMap.put("Serif-BoldItalic", Typeface.create(Typeface.SERIF, 3));
            typefaceMap.put("SansSerif", Typeface.create(Typeface.SANS_SERIF, 0));
            typefaceMap.put("SansSerif-Bold", Typeface.create(Typeface.SANS_SERIF, 1));
            typefaceMap.put("SansSerif-Italic", Typeface.create(Typeface.SANS_SERIF, 2));
            typefaceMap.put("SansSerif-BoldItalic", Typeface.create(Typeface.SANS_SERIF, 3));
            typefaceMap.put("Monospaced", Typeface.create(Typeface.MONOSPACE, 0));
            typefaceMap.put("Monospaced-Bold", Typeface.create(Typeface.MONOSPACE, 1));
            typefaceMap.put("Monospaced-Italic", Typeface.create(Typeface.MONOSPACE, 2));
            typefaceMap.put("Monospaced-BoldItalic", Typeface.create(Typeface.MONOSPACE, 3));
            fontList = new String[typefaceMap.size()];
            typefaceMap.keySet().toArray(fontList);
        }
    }

    protected void addGlyph(char c)
    {
        Glyph glyph;
        glyph = new Glyph(c);
        if(glyphCount == glyphs.length)
            glyphs = (Glyph[])PApplet.expand(glyphs);
        if(glyphCount != 0) goto _L2; else goto _L1
_L1:
        glyph.index = 0;
        glyphs[glyphCount] = glyph;
        if(glyph.value < 128)
            ascii[glyph.value] = 0;
_L4:
        glyphCount = glyphCount + 1;
        return;
_L2:
        int i;
        if(glyphs[glyphCount - 1].value < glyph.value)
        {
            glyphs[glyphCount] = glyph;
            if(glyph.value < 128)
                ascii[glyph.value] = glyphCount;
            continue; /* Loop/switch isn't completed */
        }
        i = 0;
_L5:
        if(i < glyphCount)
        {
label0:
            {
                if(glyphs[i].value <= c)
                    break label0;
                for(int j = glyphCount; j > i; j--)
                {
                    glyphs[j] = glyphs[j - 1];
                    if(glyphs[j].value < 128)
                        ascii[glyphs[j].value] = j;
                }

                glyph.index = i;
                glyphs[i] = glyph;
                if(c < '\200')
                    ascii[c] = i;
            }
        }
        if(true) goto _L4; else goto _L3
_L3:
        i++;
          goto _L5
        if(true) goto _L4; else goto _L6
_L6:
    }

    public float ascent()
    {
        return (float)ascent / (float)size;
    }

    public float descent()
    {
        return (float)descent / (float)size;
    }

    public Object getCache(PGraphics pgraphics)
    {
        if(cacheMap == null)
            pgraphics = null;
        else
            pgraphics = ((PGraphics) (cacheMap.get(pgraphics)));
        return pgraphics;
    }

    public int getDefaultSize()
    {
        return size;
    }

    public Glyph getGlyph(char c)
    {
        int i = index(c);
        Glyph glyph;
        if(i == -1)
            glyph = null;
        else
            glyph = glyphs[i];
        return glyph;
    }

    public Glyph getGlyph(int i)
    {
        return glyphs[i];
    }

    public int getGlyphCount()
    {
        return glyphCount;
    }

    public String getName()
    {
        return name;
    }

    public Object getNative()
    {
        Typeface typeface1;
        if(subsetting)
            typeface1 = null;
        else
            typeface1 = typeface;
        return typeface1;
    }

    public String getPostScriptName()
    {
        return psname;
    }

    public int getSize()
    {
        return size;
    }

    protected int index(char c)
    {
        int i;
        if(lazy)
        {
            i = indexActual(c);
            if(i == -1)
            {
                addGlyph(c);
                i = indexActual(c);
            }
        } else
        {
            i = indexActual(c);
        }
        return i;
    }

    protected int indexActual(char c)
    {
        if(glyphCount == 0)
            c = '\uFFFF';
        else
        if(c < '\200')
            c = ascii[c];
        else
            c = indexHunt(c, 0, glyphCount - 1);
        return c;
    }

    protected int indexHunt(int i, int j, int k)
    {
        int l = (j + k) / 2;
        if(i == glyphs[l].value)
            i = l;
        else
        if(j >= k)
            i = -1;
        else
        if(i < glyphs[l].value)
            i = indexHunt(i, j, l - 1);
        else
            i = indexHunt(i, l + 1, k);
        return i;
    }

    public float kern(char c, char c1)
    {
        return 0.0F;
    }

    public void removeCache(PGraphics pgraphics)
    {
        if(cacheMap != null)
            cacheMap.remove(pgraphics);
    }

    public void save(OutputStream outputstream)
        throws IOException
    {
        boolean flag = false;
        outputstream = new DataOutputStream(outputstream);
        outputstream.writeInt(glyphCount);
        if(name == null || psname == null)
        {
            name = "";
            psname = "";
        }
        outputstream.writeInt(11);
        outputstream.writeInt(size);
        outputstream.writeInt(0);
        outputstream.writeInt(ascent);
        outputstream.writeInt(descent);
        int i = 0;
        int j;
        do
        {
            j = ((flag) ? 1 : 0);
            if(i >= glyphCount)
                break;
            glyphs[i].writeHeader(outputstream);
            i++;
        } while(true);
        for(; j < glyphCount; j++)
            glyphs[j].writeBitmap(outputstream);

        outputstream.writeUTF(name);
        outputstream.writeUTF(psname);
        outputstream.writeBoolean(smooth);
        outputstream.flush();
    }

    public void setCache(PGraphics pgraphics, Object obj)
    {
        if(cacheMap == null)
            cacheMap = new HashMap();
        cacheMap.put(pgraphics, obj);
    }

    public void setNative(Object obj)
    {
        typeface = (Typeface)obj;
    }

    public void setSubsetting()
    {
        subsetting = true;
    }

    public float width(char c)
    {
        float f;
        if(c == ' ')
        {
            f = width('i');
        } else
        {
            int i = index(c);
            if(i == -1)
                f = 0.0F;
            else
                f = (float)glyphs[i].setWidth / (float)size;
        }
        return f;
    }

    public static char CHARSET[];
    static final char EXTRA_CHARS[] = {
        '\200', '\201', '\202', '\203', '\204', '\205', '\206', '\207', '\210', '\211', 
        '\212', '\213', '\214', '\215', '\216', '\217', '\220', '\221', '\222', '\223', 
        '\224', '\225', '\226', '\227', '\230', '\231', '\232', '\233', '\234', '\235', 
        '\236', '\237', '\240', '\241', '\242', '\243', '\244', '\245', '\246', '\247', 
        '\250', '\251', '\252', '\253', '\254', '\255', '\256', '\257', '\260', '\261', 
        '\264', '\265', '\266', '\267', '\270', '\272', '\273', '\277', '\300', '\301', 
        '\302', '\303', '\304', '\305', '\306', '\307', '\310', '\311', '\312', '\313', 
        '\314', '\315', '\316', '\317', '\321', '\322', '\323', '\324', '\325', '\326', 
        '\327', '\330', '\331', '\332', '\333', '\334', '\335', '\337', '\340', '\341', 
        '\342', '\343', '\344', '\345', '\346', '\347', '\350', '\351', '\352', '\353', 
        '\354', '\355', '\356', '\357', '\361', '\362', '\363', '\364', '\365', '\366', 
        '\367', '\370', '\371', '\372', '\373', '\374', '\375', '\377', '\u0102', '\u0103', 
        '\u0104', '\u0105', '\u0106', '\u0107', '\u010C', '\u010D', '\u010E', '\u010F', '\u0110', '\u0111', 
        '\u0118', '\u0119', '\u011A', '\u011B', '\u0131', '\u0139', '\u013A', '\u013D', '\u013E', '\u0141', 
        '\u0142', '\u0143', '\u0144', '\u0147', '\u0148', '\u0150', '\u0151', '\u0152', '\u0153', '\u0154', 
        '\u0155', '\u0158', '\u0159', '\u015A', '\u015B', '\u015E', '\u015F', '\u0160', '\u0161', '\u0162', 
        '\u0163', '\u0164', '\u0165', '\u016E', '\u016F', '\u0170', '\u0171', '\u0178', '\u0179', '\u017A', 
        '\u017B', '\u017C', '\u017D', '\u017E', '\u0192', '\u02C6', '\u02C7', '\u02D8', '\u02D9', '\u02DA', 
        '\u02DB', '\u02DC', '\u02DD', '\u03A9', '\u03C0', '\u2013', '\u2014', '\u2018', '\u2019', '\u201A', 
        '\u201C', '\u201D', '\u201E', '\u2020', '\u2021', '\u2022', '\u2026', '\u2030', '\u2039', '\u203A', 
        '\u2044', '\u20AC', '\u2122', '\u2202', '\u2206', '\u220F', '\u2211', '\u221A', '\u221E', '\u222B', 
        '\u2248', '\u2260', '\u2264', '\u2265', '\u25CA', '\uF8FF', '\uFB01', '\uFB02'
    };
    static String fontList[];
    static HashMap typefaceMap;
    protected static Typeface typefaces[];
    protected int ascent;
    protected int ascii[];
    protected HashMap cacheMap;
    protected int descent;
    protected int glyphCount;
    protected Glyph glyphs[];
    protected boolean lazy;
    Bitmap lazyBitmap;
    Canvas lazyCanvas;
    Paint lazyPaint;
    int lazySamples[];
    protected String name;
    protected String psname;
    protected int size;
    protected boolean smooth;
    protected boolean subsetting;
    protected Typeface typeface;
    protected boolean typefaceSearched;

    static 
    {
        boolean flag = false;
        CHARSET = new char[EXTRA_CHARS.length + 94];
        int i = 33;
        int j = 0;
        int k;
        int l;
        do
        {
            k = ((flag) ? 1 : 0);
            l = j;
            if(i > 126)
                break;
            CHARSET[j] = (char)i;
            i++;
            j++;
        } while(true);
        while(k < EXTRA_CHARS.length) 
        {
            CHARSET[l] = EXTRA_CHARS[k];
            k++;
            l++;
        }
    }
}
