// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.core;

import android.graphics.*;
import java.io.PrintStream;
import java.util.HashMap;
import processing.data.XML;

// Referenced classes of package processing.core:
//            PShape, PApplet, PGraphics, PMatrix2D, 
//            PGraphicsAndroid2D

public class PShapeSVG extends PShape
{
    public class Font extends PShapeSVG
    {

        public void drawChar(PGraphics pgraphics, char c, float f, float f1, float f2)
        {
            pgraphics.pushMatrix();
            f2 /= face.unitsPerEm;
            pgraphics.translate(f, f1);
            pgraphics.scale(f2, -f2);
            FontGlyph fontglyph = (FontGlyph)unicodeGlyphs.get(new Character(c));
            if(fontglyph != null)
                pgraphics.shape(fontglyph);
            pgraphics.popMatrix();
        }

        protected void drawShape()
        {
        }

        public void drawString(PGraphics pgraphics, String s, float f, float f1, float f2)
        {
            pgraphics.pushMatrix();
            f2 /= face.unitsPerEm;
            pgraphics.translate(f, f1);
            pgraphics.scale(f2, -f2);
            char ac[] = s.toCharArray();
            int i = 0;
            while(i < ac.length) 
            {
                s = (FontGlyph)unicodeGlyphs.get(new Character(ac[i]));
                if(s != null)
                {
                    s.draw(pgraphics);
                    pgraphics.translate(((FontGlyph) (s)).horizAdvX, 0.0F);
                } else
                {
                    System.err.println((new StringBuilder()).append("'").append(ac[i]).append("' not available.").toString());
                }
                i++;
            }
            pgraphics.popMatrix();
        }

        public float textWidth(String s, float f)
        {
            char ac[] = s.toCharArray();
            float f1 = 0.0F;
            for(int i = 0; i < ac.length;)
            {
                s = (FontGlyph)unicodeGlyphs.get(new Character(ac[i]));
                float f2 = f1;
                if(s != null)
                    f2 = f1 + (float)((FontGlyph) (s)).horizAdvX / (float)face.unitsPerEm;
                i++;
                f1 = f2;
            }

            return f1 * f;
        }

        public FontFace face;
        public int glyphCount;
        public FontGlyph glyphs[];
        int horizAdvX;
        public FontGlyph missingGlyph;
        public HashMap namedGlyphs;
        final PShapeSVG this$0;
        public HashMap unicodeGlyphs;

        public Font(PShapeSVG pshapesvg1, XML xml)
        {
            int i = 0;
            this$0 = PShapeSVG.this;
            super(pshapesvg1, xml, false);
            pshapesvg = xml.getChildren();
            horizAdvX = xml.getInt("horiz-adv-x", 0);
            namedGlyphs = new HashMap();
            unicodeGlyphs = new HashMap();
            glyphCount = 0;
            glyphs = new FontGlyph[PShapeSVG.this.length];
            while(i < PShapeSVG.this.length) 
            {
                xml = PShapeSVG.this[i].getName();
                pshapesvg1 = PShapeSVG.this[i];
                if(xml != null)
                    if(xml.equals("glyph"))
                    {
                        pshapesvg1 = new FontGlyph(this, pshapesvg1, this);
                        if(pshapesvg1.isLegit())
                        {
                            if(((FontGlyph) (pshapesvg1)).name != null)
                                namedGlyphs.put(((FontGlyph) (pshapesvg1)).name, pshapesvg1);
                            if(((FontGlyph) (pshapesvg1)).unicode != 0)
                                unicodeGlyphs.put(new Character(((FontGlyph) (pshapesvg1)).unicode), pshapesvg1);
                        }
                        xml = glyphs;
                        int j = glyphCount;
                        glyphCount = j + 1;
                        xml[j] = pshapesvg1;
                    } else
                    if(xml.equals("missing-glyph"))
                        missingGlyph = new FontGlyph(this, pshapesvg1, this);
                    else
                    if(xml.equals("font-face"))
                        face = new FontFace(this, pshapesvg1);
                    else
                        System.err.println((new StringBuilder()).append("Ignoring ").append(xml).append(" inside <font>").toString());
                i++;
            }
        }
    }

    class FontFace extends PShapeSVG
    {

        protected void drawShape()
        {
        }

        int ascent;
        int bbox[];
        int descent;
        String fontFamily;
        String fontStretch;
        int fontWeight;
        int horizOriginX;
        int horizOriginY;
        int panose1[];
        final PShapeSVG this$0;
        int underlinePosition;
        int underlineThickness;
        int unitsPerEm;
        int vertAdvY;
        int vertOriginX;
        int vertOriginY;

        public FontFace(PShapeSVG pshapesvg1, XML xml)
        {
            this$0 = PShapeSVG.this;
            super(pshapesvg1, xml, true);
            unitsPerEm = xml.getInt("units-per-em", 1000);
        }
    }

    public class FontGlyph extends PShapeSVG
    {

        protected boolean isLegit()
        {
            boolean flag;
            if(vertexCount != 0)
                flag = true;
            else
                flag = false;
            return flag;
        }

        int horizAdvX;
        public String name;
        final PShapeSVG this$0;
        char unicode;

        public FontGlyph(PShapeSVG pshapesvg1, XML xml, Font font)
        {
            this$0 = PShapeSVG.this;
            super(pshapesvg1, xml, true);
            parsePath();
            name = xml.getString("glyph-name");
            pshapesvg = xml.getString("unicode");
            unicode = (char)0;
            if(PShapeSVG.this != null)
                if(length() == 1)
                    unicode = charAt(0);
                else
                    System.err.println((new StringBuilder()).append("unicode for ").append(name).append(" is more than one char: ").append(PShapeSVG.this).toString());
            if(xml.hasAttribute("horiz-adv-x"))
                horizAdvX = xml.getInt("horiz-adv-x");
            else
                horizAdvX = font.horizAdvX;
        }
    }

    static class Gradient extends PShapeSVG
    {

        int color[];
        int count;
        float offset[];
        Matrix transform;

        public Gradient(PShapeSVG pshapesvg, XML xml)
        {
            super(pshapesvg, xml, true);
            XML axml[] = xml.getChildren();
            offset = new float[axml.length];
            color = new int[axml.length];
            for(int i = 0; i < axml.length; i++)
            {
                Object obj = axml[i];
                if(!((XML) (obj)).getName().equals("stop"))
                    continue;
                xml = ((XML) (obj)).getString("offset");
                float f = 1.0F;
                pshapesvg = xml;
                if(xml.endsWith("%"))
                {
                    f = 100F;
                    pshapesvg = xml.substring(0, xml.length() - 1);
                }
                offset[count] = PApplet.parseFloat(pshapesvg) / f;
                xml = parseStyleAttributes(((XML) (obj)).getString("style"));
                pshapesvg = (String)xml.get("stop-color");
                if(pshapesvg == null)
                    pshapesvg = "#000000";
                obj = (String)xml.get("stop-opacity");
                xml = ((XML) (obj));
                if(obj == null)
                    xml = "1";
                int j = (int)(PApplet.parseFloat(xml) * 255F);
                color[count] = j << 24 | Integer.parseInt(pshapesvg.substring(1), 16);
                count = count + 1;
            }

            offset = PApplet.subset(offset, 0, count);
            color = PApplet.subset(color, 0, count);
        }
    }

    class LinearGradient extends Gradient
    {

        final PShapeSVG this$0;
        float x1;
        float x2;
        float y1;
        float y2;

        public LinearGradient(PShapeSVG pshapesvg1, XML xml)
        {
            this$0 = PShapeSVG.this;
            super(pshapesvg1, xml);
            x1 = getFloatWithUnit(xml, "x1");
            y1 = getFloatWithUnit(xml, "y1");
            x2 = getFloatWithUnit(xml, "x2");
            y2 = getFloatWithUnit(xml, "y2");
            pshapesvg = xml.getString("gradientTransform");
            if(PShapeSVG.this != null)
            {
                pshapesvg = parseTransform(PShapeSVG.this).get(null);
                transform = new Matrix();
                transform.setValues(new float[] {
                    PShapeSVG.this[0], PShapeSVG.this[1], PShapeSVG.this[2], PShapeSVG.this[3], PShapeSVG.this[4], PShapeSVG.this[5], 0.0F, 0.0F, 1.0F
                });
                pshapesvg = new float[2];
                PShapeSVG.this[0] = x1;
                PShapeSVG.this[1] = y1;
                pshapesvg1 = new float[2];
                pshapesvg1[0] = x2;
                pshapesvg1[1] = y2;
                transform.mapPoints(PShapeSVG.this);
                transform.mapPoints(pshapesvg1);
                x1 = PShapeSVG.this[0];
                y1 = PShapeSVG.this[1];
                x2 = pshapesvg1[0];
                y2 = pshapesvg1[1];
            }
        }
    }

    class RadialGradient extends Gradient
    {

        float cx;
        float cy;
        float r;
        final PShapeSVG this$0;

        public RadialGradient(PShapeSVG pshapesvg1, XML xml)
        {
            this$0 = PShapeSVG.this;
            super(pshapesvg1, xml);
            cx = getFloatWithUnit(xml, "cx");
            cy = getFloatWithUnit(xml, "cy");
            r = getFloatWithUnit(xml, "r");
            pshapesvg = xml.getString("gradientTransform");
            if(PShapeSVG.this != null)
            {
                pshapesvg = parseTransform(PShapeSVG.this).get(null);
                transform = new Matrix();
                transform.setValues(new float[] {
                    PShapeSVG.this[0], PShapeSVG.this[1], PShapeSVG.this[2], PShapeSVG.this[3], PShapeSVG.this[4], PShapeSVG.this[5], 0.0F, 0.0F, 1.0F
                });
                pshapesvg = new float[2];
                PShapeSVG.this[0] = cx;
                PShapeSVG.this[1] = cy;
                pshapesvg1 = new float[2];
                pshapesvg1[0] = cx + r;
                pshapesvg1[1] = cy;
                transform.mapPoints(PShapeSVG.this);
                transform.mapPoints(pshapesvg1);
                cx = PShapeSVG.this[0];
                cy = PShapeSVG.this[1];
                r = pshapesvg1[0] - PShapeSVG.this[0];
            }
        }
    }


    protected PShapeSVG(PShapeSVG pshapesvg, XML xml, boolean flag)
    {
        boolean flag1;
        flag1 = true;
        super();
        parent = pshapesvg;
        if(pshapesvg == null)
        {
            stroke = false;
            strokeColor = 0xff000000;
            strokeWeight = 1.0F;
            strokeCap = 1;
            strokeJoin = 8;
            strokeGradient = null;
            strokeGradientPaint = null;
            strokeName = null;
            fill = true;
            fillColor = 0xff000000;
            fillGradient = null;
            fillGradientPaint = null;
            fillName = null;
            strokeOpacity = 1.0F;
            fillOpacity = 1.0F;
            opacity = 1.0F;
        } else
        {
            stroke = pshapesvg.stroke;
            strokeColor = pshapesvg.strokeColor;
            strokeWeight = pshapesvg.strokeWeight;
            strokeCap = pshapesvg.strokeCap;
            strokeJoin = pshapesvg.strokeJoin;
            strokeGradient = pshapesvg.strokeGradient;
            strokeGradientPaint = pshapesvg.strokeGradientPaint;
            strokeName = pshapesvg.strokeName;
            fill = pshapesvg.fill;
            fillColor = pshapesvg.fillColor;
            fillGradient = pshapesvg.fillGradient;
            fillGradientPaint = pshapesvg.fillGradientPaint;
            fillName = pshapesvg.fillName;
            opacity = pshapesvg.opacity;
        }
        element = xml;
        name = xml.getString("id");
        if(name == null) goto _L2; else goto _L1
_L1:
        pshapesvg = PApplet.match(name, "_x([A-Za-z0-9]{2})_");
        if(pshapesvg != null) goto _L3; else goto _L2
_L2:
        char c;
        if(xml.getString("display", "inline").equals("none"))
            flag1 = false;
        visible = flag1;
        pshapesvg = xml.getString("transform");
        if(pshapesvg != null)
            matrix = parseTransform(pshapesvg);
        if(flag)
        {
            parseColors(xml);
            parseChildren(xml);
        }
        return;
_L3:
        c = (char)PApplet.unhex(pshapesvg[1]);
        name = name.replace(pshapesvg[0], (new StringBuilder()).append("").append(c).toString());
        if(true) goto _L1; else goto _L4
_L4:
    }

    public PShapeSVG(XML xml)
    {
        String s;
        this(null, xml, true);
        if(!xml.getName().equals("svg"))
            throw new RuntimeException((new StringBuilder()).append("root is not <svg>, it's <").append(xml.getName()).append(">").toString());
        s = xml.getString("viewBox");
        if(s != null)
        {
            int ai[] = PApplet.parseInt(PApplet.splitTokens(s));
            width = ai[2];
            height = ai[3];
        }
        ai = xml.getString("width");
        xml = xml.getString("height");
        if(ai == null) goto _L2; else goto _L1
_L1:
        width = parseUnitSize(ai);
        height = parseUnitSize(xml);
_L4:
        return;
_L2:
        if(width == 0.0F || height == 0.0F)
        {
            PGraphics.showWarning("The width and/or height is not readable in the <svg> tag of this file.");
            width = 1.0F;
            height = 1.0F;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected static float getFloatWithUnit(XML xml, String s)
    {
        xml = xml.getString(s);
        float f;
        if(xml == null)
            f = 0.0F;
        else
            f = parseUnitSize(xml);
        return f;
    }

    private void parsePathCode(int i)
    {
        if(vertexCodeCount == vertexCodes.length)
            vertexCodes = PApplet.expand(vertexCodes);
        int ai[] = vertexCodes;
        int j = vertexCodeCount;
        vertexCodeCount = j + 1;
        ai[j] = i;
    }

    private void parsePathCurveto(float f, float f1, float f2, float f3, float f4, float f5)
    {
        parsePathCode(1);
        parsePathVertex(f, f1);
        parsePathVertex(f2, f3);
        parsePathVertex(f4, f5);
    }

    private void parsePathLineto(float f, float f1)
    {
        parsePathCode(0);
        parsePathVertex(f, f1);
    }

    private void parsePathMoveto(float f, float f1)
    {
        if(vertexCount > 0)
            parsePathCode(4);
        parsePathCode(0);
        parsePathVertex(f, f1);
    }

    private void parsePathQuadto(float f, float f1, float f2, float f3)
    {
        parsePathCode(2);
        parsePathVertex(f, f1);
        parsePathVertex(f2, f3);
    }

    private void parsePathVertex(float f, float f1)
    {
        if(vertexCount == vertices.length)
        {
            int i = vertexCount;
            float af[][] = new float[i << 1][2];
            System.arraycopy(vertices, 0, af, 0, vertexCount);
            vertices = af;
        }
        vertices[vertexCount][0] = f;
        vertices[vertexCount][1] = f1;
        vertexCount = vertexCount + 1;
    }

    protected static int parseRGB(String s)
    {
        s = PApplet.parseInt(PApplet.splitTokens(s.substring(s.indexOf('(') + 1, s.indexOf(')')), ", "));
        int i = s[0];
        int j = s[1];
        return s[2] | (i << 16 | j << 8);
    }

    protected static PMatrix2D parseSingleTransform(String s)
    {
        String as[] = PApplet.match(s, "[,\\s]*(\\w+)\\((.*)\\)");
        if(as != null) goto _L2; else goto _L1
_L1:
        System.err.println((new StringBuilder()).append("Could not parse transform ").append(s).toString());
        s = null;
_L4:
        return s;
_L2:
        float af[] = PApplet.parseFloat(PApplet.splitTokens(as[2], ", "));
        if(as[1].equals("matrix"))
        {
            s = new PMatrix2D(af[0], af[2], af[4], af[1], af[3], af[5]);
            continue; /* Loop/switch isn't completed */
        }
        if(as[1].equals("translate"))
        {
            float f = af[0];
            float f3;
            if(af.length == 2)
                f3 = af[1];
            else
                f3 = af[0];
            s = new PMatrix2D(1.0F, 0.0F, f, 0.0F, 1.0F, f3);
            continue; /* Loop/switch isn't completed */
        }
        if(as[1].equals("scale"))
        {
            float f1 = af[0];
            float f4;
            if(af.length == 2)
                f4 = af[1];
            else
                f4 = af[0];
            s = new PMatrix2D(f1, 0.0F, 0.0F, 0.0F, f4, 0.0F);
            continue; /* Loop/switch isn't completed */
        }
        if(as[1].equals("rotate"))
        {
            float f2 = af[0];
            if(af.length == 1)
            {
                float f5 = PApplet.cos(f2);
                f2 = PApplet.sin(f2);
                s = new PMatrix2D(f5, -f2, 0.0F, f2, f5, 0.0F);
                continue; /* Loop/switch isn't completed */
            }
            if(af.length == 3)
            {
                s = new PMatrix2D(0.0F, 1.0F, af[1], 1.0F, 0.0F, af[2]);
                s.rotate(af[0]);
                s.translate(-af[1], -af[2]);
                continue; /* Loop/switch isn't completed */
            }
        } else
        {
            if(as[1].equals("skewX"))
            {
                s = new PMatrix2D(1.0F, 0.0F, 1.0F, PApplet.tan(af[0]), 0.0F, 0.0F);
                continue; /* Loop/switch isn't completed */
            }
            if(as[1].equals("skewY"))
            {
                s = new PMatrix2D(1.0F, 0.0F, 1.0F, 0.0F, PApplet.tan(af[0]), 0.0F);
                continue; /* Loop/switch isn't completed */
            }
        }
        s = null;
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected static HashMap parseStyleAttributes(String s)
    {
        HashMap hashmap = new HashMap();
        String as[] = s.split(";");
        for(int i = 0; i < as.length; i++)
        {
            s = as[i].split(":");
            hashmap.put(s[0], s[1]);
        }

        return hashmap;
    }

    protected static PMatrix2D parseTransform(String s)
    {
        String s1 = s.trim();
        s = null;
        int i = 0;
        do
        {
            int j = s1.indexOf(')', i);
            if(j != -1)
            {
                PMatrix2D pmatrix2d = parseSingleTransform(s1.substring(i, j + 1));
                if(s == null)
                    s = pmatrix2d;
                else
                    s.apply(pmatrix2d);
                i = j + 1;
            } else
            {
                return s;
            }
        } while(true);
    }

    protected static float parseUnitSize(String s)
    {
        int i = s.length() - 2;
        float f;
        if(s.endsWith("pt"))
            f = PApplet.parseFloat(s.substring(0, i)) * 1.25F;
        else
        if(s.endsWith("pc"))
            f = PApplet.parseFloat(s.substring(0, i)) * 15F;
        else
        if(s.endsWith("mm"))
            f = PApplet.parseFloat(s.substring(0, i)) * 3.543307F;
        else
        if(s.endsWith("cm"))
            f = PApplet.parseFloat(s.substring(0, i)) * 35.43307F;
        else
        if(s.endsWith("in"))
            f = PApplet.parseFloat(s.substring(0, i)) * 90F;
        else
        if(s.endsWith("px"))
            f = PApplet.parseFloat(s.substring(0, i));
        else
            f = PApplet.parseFloat(s);
        return f;
    }

    protected Shader calcGradientPaint(Gradient gradient)
    {
        int ai[] = new int[gradient.count];
        int i = (int)(opacity * 255F);
        for(int j = 0; j < gradient.count; j++)
            ai[j] = gradient.color[j] & 0xffffff | i << 24;

        if(gradient instanceof LinearGradient)
        {
            gradient = (LinearGradient)gradient;
            gradient = new android.graphics.LinearGradient(((LinearGradient) (gradient)).x1, ((LinearGradient) (gradient)).y1, ((LinearGradient) (gradient)).x2, ((LinearGradient) (gradient)).y2, ai, ((LinearGradient) (gradient)).offset, android.graphics.Shader.TileMode.CLAMP);
        } else
        if(gradient instanceof RadialGradient)
        {
            gradient = (RadialGradient)gradient;
            gradient = new android.graphics.RadialGradient(((RadialGradient) (gradient)).cx, ((RadialGradient) (gradient)).cy, ((RadialGradient) (gradient)).r, ai, ((RadialGradient) (gradient)).offset, android.graphics.Shader.TileMode.CLAMP);
        } else
        {
            gradient = null;
        }
        return gradient;
    }

    public PShape getChild(String s)
    {
        PShape pshape = super.getChild(s);
        PShape pshape1 = pshape;
        if(pshape == null)
            pshape1 = super.getChild(s.replace(' ', '_'));
        if(pshape1 != null)
        {
            pshape1.width = width;
            pshape1.height = height;
        }
        return pshape1;
    }

    protected PShape parseChild(XML xml)
    {
        Object obj;
        String s;
        obj = null;
        s = xml.getName();
        if(s != null) goto _L2; else goto _L1
_L1:
        xml = obj;
_L4:
        return xml;
_L2:
        if(s.equals("g"))
            xml = new PShapeSVG(this, xml, true);
        else
        if(s.equals("defs"))
            xml = new PShapeSVG(this, xml, true);
        else
        if(s.equals("line"))
        {
            xml = new PShapeSVG(this, xml, true);
            xml.parseLine();
        } else
        if(s.equals("circle"))
        {
            xml = new PShapeSVG(this, xml, true);
            xml.parseEllipse(true);
        } else
        if(s.equals("ellipse"))
        {
            xml = new PShapeSVG(this, xml, true);
            xml.parseEllipse(false);
        } else
        if(s.equals("rect"))
        {
            xml = new PShapeSVG(this, xml, true);
            xml.parseRect();
        } else
        if(s.equals("polygon"))
        {
            xml = new PShapeSVG(this, xml, true);
            xml.parsePoly(true);
        } else
        if(s.equals("polyline"))
        {
            xml = new PShapeSVG(this, xml, true);
            xml.parsePoly(false);
        } else
        if(s.equals("path"))
        {
            xml = new PShapeSVG(this, xml, true);
            xml.parsePath();
        } else
        if(s.equals("radialGradient"))
            xml = new RadialGradient(this, xml);
        else
        if(s.equals("linearGradient"))
            xml = new LinearGradient(this, xml);
        else
        if(s.equals("font"))
        {
            xml = new Font(this, xml);
        } else
        {
            xml = obj;
            if(!s.equals("metadata"))
                if(s.equals("text"))
                {
                    PGraphics.showWarning("Text and fonts in SVG files are not currently supported, convert text to outlines instead.");
                    xml = obj;
                } else
                if(s.equals("filter"))
                {
                    PGraphics.showWarning("Filters are not supported.");
                    xml = obj;
                } else
                if(s.equals("mask"))
                {
                    PGraphics.showWarning("Masks are not supported.");
                    xml = obj;
                } else
                if(s.equals("pattern"))
                {
                    PGraphics.showWarning("Patterns are not supported.");
                    xml = obj;
                } else
                {
                    xml = obj;
                    if(!s.equals("stop"))
                    {
                        xml = obj;
                        if(!s.equals("sodipodi:namedview"))
                        {
                            PGraphics.showWarning((new StringBuilder()).append("Ignoring <").append(s).append("> tag.").toString());
                            xml = obj;
                        }
                    }
                }
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected void parseChildren(XML xml)
    {
        XML axml[] = xml.getChildren();
        children = new PShape[axml.length];
        childCount = 0;
        int i = axml.length;
        for(int j = 0; j < i; j++)
        {
            xml = parseChild(axml[j]);
            if(xml != null)
                addChild(xml);
        }

        children = (PShape[])PApplet.subset(children, 0, childCount);
    }

    protected void parseColors(XML xml)
    {
        if(xml.hasAttribute("opacity"))
            setOpacity(xml.getString("opacity"));
        if(xml.hasAttribute("stroke"))
            setColor(xml.getString("stroke"), false);
        if(xml.hasAttribute("stroke-opacity"))
            setStrokeOpacity(xml.getString("stroke-opacity"));
        if(xml.hasAttribute("stroke-width"))
            setStrokeWeight(xml.getString("stroke-width"));
        if(xml.hasAttribute("stroke-linejoin"))
            setStrokeJoin(xml.getString("stroke-linejoin"));
        if(xml.hasAttribute("stroke-linecap"))
            setStrokeCap(xml.getString("stroke-linecap"));
        if(xml.hasAttribute("fill"))
            setColor(xml.getString("fill"), true);
        if(xml.hasAttribute("fill-opacity"))
            setFillOpacity(xml.getString("fill-opacity"));
        if(xml.hasAttribute("style"))
        {
            String as[] = PApplet.splitTokens(xml.getString("style"), ";");
            int i = 0;
            while(i < as.length) 
            {
                xml = PApplet.splitTokens(as[i], ":");
                xml[0] = PApplet.trim(xml[0]);
                if(xml[0].equals("fill"))
                    setColor(xml[1], true);
                else
                if(xml[0].equals("fill-opacity"))
                    setFillOpacity(xml[1]);
                else
                if(xml[0].equals("stroke"))
                    setColor(xml[1], false);
                else
                if(xml[0].equals("stroke-width"))
                    setStrokeWeight(xml[1]);
                else
                if(xml[0].equals("stroke-linecap"))
                    setStrokeCap(xml[1]);
                else
                if(xml[0].equals("stroke-linejoin"))
                    setStrokeJoin(xml[1]);
                else
                if(xml[0].equals("stroke-opacity"))
                    setStrokeOpacity(xml[1]);
                else
                if(xml[0].equals("opacity"))
                    setOpacity(xml[1]);
                i++;
            }
        }
    }

    protected void parseEllipse(boolean flag)
    {
        kind = 31;
        family = 1;
        params = new float[4];
        params[0] = getFloatWithUnit(element, "cx");
        params[1] = getFloatWithUnit(element, "cy");
        float f;
        float f1;
        float af[];
        if(flag)
        {
            f = getFloatWithUnit(element, "r");
            f1 = f;
        } else
        {
            f1 = getFloatWithUnit(element, "rx");
            f = getFloatWithUnit(element, "ry");
        }
        af = params;
        af[0] = af[0] - f1;
        af = params;
        af[1] = af[1] - f;
        params[2] = f1 * 2.0F;
        params[3] = f * 2.0F;
    }

    protected void parseLine()
    {
        kind = 4;
        family = 1;
        params = (new float[] {
            getFloatWithUnit(element, "x1"), getFloatWithUnit(element, "y1"), getFloatWithUnit(element, "x2"), getFloatWithUnit(element, "y2")
        });
    }

    protected void parsePath()
    {
        String s;
        family = 2;
        kind = 0;
        s = element.getString("d");
        if(s != null && PApplet.trim(s).length() != 0) goto _L2; else goto _L1
_L1:
        return;
_L2:
        String as[];
        int i;
        int k;
        int l;
        float f;
        float f1;
        float f2;
        float f3;
        char ac[] = s.toCharArray();
        StringBuffer stringbuffer = new StringBuffer();
        boolean flag = false;
        for(int j = 0; j < ac.length;)
        {
            char c = ac[j];
            boolean flag1 = false;
            if(c == 'M' || c == 'm' || c == 'L' || c == 'l' || c == 'H' || c == 'h' || c == 'V' || c == 'v' || c == 'C' || c == 'c' || c == 'S' || c == 's' || c == 'Q' || c == 'q' || c == 'T' || c == 't' || c == 'Z' || c == 'z' || c == ',')
            {
                boolean flag2 = true;
                flag1 = flag2;
                if(j != 0)
                {
                    stringbuffer.append("|");
                    flag1 = flag2;
                }
            }
            if(c == 'Z' || c == 'z')
                flag1 = false;
            if(c == '-' && !flag && (j == 0 || ac[j - 1] != 'e'))
                stringbuffer.append("|");
            if(c != ',')
                stringbuffer.append(c);
            if(flag1 && c != ',' && c != '-')
                stringbuffer.append("|");
            j++;
            flag = flag1;
        }

        as = PApplet.splitTokens(stringbuffer.toString(), "| \t\n\r\f\240");
        l = as.length;
        vertices = new float[l][2];
        vertexCodes = new int[as.length];
        f = 0.0F;
        f1 = 0.0F;
        k = 0;
        i = 0;
        l = 0;
        f2 = 0.0F;
        f3 = 0.0F;
_L22:
        if(l >= as.length) goto _L1; else goto _L3
_L3:
        int i1;
        int j1 = as[l].charAt(0);
        String s1;
        String s2;
        if((j1 >= 48 && j1 <= 57 || j1 == 45) && i != 0)
        {
            j1 = i;
            i1 = l - 1;
            l = j1;
        } else
        {
            i = j1;
            i1 = l;
            l = j1;
        }
        i;
        JVM INSTR lookupswitch 18: default 592
    //                   67: 1122
    //                   72: 964
    //                   76: 860
    //                   77: 724
    //                   81: 1724
    //                   83: 1354
    //                   84: 1894
    //                   86: 1043
    //                   90: 2194
    //                   99: 1225
    //                   104: 1002
    //                   108: 909
    //                   109: 804
    //                   113: 1799
    //                   115: 1529
    //                   116: 2039
    //                   118: 1081
    //                   122: 2194;
           goto _L4 _L5 _L6 _L7 _L8 _L9 _L10 _L11 _L12 _L13 _L14 _L15 _L16 _L17 _L18 _L19 _L20 _L21 _L13
_L13:
        break MISSING_BLOCK_LABEL_2194;
_L4:
        s1 = PApplet.join(PApplet.subset(as, 0, i1), ",");
        s2 = PApplet.join(PApplet.subset(as, i1), ",");
        System.err.println((new StringBuilder()).append("parsed: ").append(s1).toString());
        System.err.println((new StringBuilder()).append("unparsed: ").append(s2).toString());
        int k1;
        float f4;
        float f5;
        float f6;
        float f7;
        float f8;
        float f9;
        if(as[i1].equals("a") || as[i1].equals("A"))
            throw new RuntimeException("Sorry, elliptical arc support for SVG files is not yet implemented (See issue 130 for updates)");
        else
            throw new RuntimeException((new StringBuilder()).append("shape command not handled: ").append(as[i1]).toString());
_L8:
        f2 = PApplet.parseFloat(as[i1 + 1]);
        f = PApplet.parseFloat(as[i1 + 2]);
        parsePathMoveto(f2, f);
        k1 = 76;
        i = i1 + 3;
        f1 = f2;
        l = k;
        f3 = f;
        k = k1;
_L23:
        i1 = l;
        l = k;
        k1 = i;
        k = i1;
        i = l;
        l = k1;
          goto _L22
_L17:
        f2 += PApplet.parseFloat(as[i1 + 1]);
        f3 += PApplet.parseFloat(as[i1 + 2]);
        parsePathMoveto(f2, f3);
        k1 = 108;
        i = i1 + 3;
        l = k;
        k = k1;
          goto _L23
_L7:
        f2 = PApplet.parseFloat(as[i1 + 1]);
        f3 = PApplet.parseFloat(as[i1 + 2]);
        parsePathLineto(f2, f3);
        i1 += 3;
        i = l;
        l = k;
        k = i;
        i = i1;
          goto _L23
_L16:
        f2 += PApplet.parseFloat(as[i1 + 1]);
        f3 += PApplet.parseFloat(as[i1 + 2]);
        parsePathLineto(f2, f3);
        i1 += 3;
        i = l;
        l = k;
        k = i;
        i = i1;
          goto _L23
_L6:
        f2 = PApplet.parseFloat(as[i1 + 1]);
        parsePathLineto(f2, f3);
        i1 += 2;
        i = k;
        k = l;
        l = i;
        i = i1;
          goto _L23
_L15:
        f2 += PApplet.parseFloat(as[i1 + 1]);
        parsePathLineto(f2, f3);
        i1 += 2;
        i = k;
        k = l;
        l = i;
        i = i1;
          goto _L23
_L12:
        f3 = PApplet.parseFloat(as[i1 + 1]);
        parsePathLineto(f2, f3);
        i1 += 2;
        i = l;
        l = k;
        k = i;
        i = i1;
          goto _L23
_L21:
        f3 += PApplet.parseFloat(as[i1 + 1]);
        parsePathLineto(f2, f3);
        i1 += 2;
        i = l;
        l = k;
        k = i;
        i = i1;
          goto _L23
_L5:
        f4 = PApplet.parseFloat(as[i1 + 1]);
        f5 = PApplet.parseFloat(as[i1 + 2]);
        f6 = PApplet.parseFloat(as[i1 + 3]);
        f7 = PApplet.parseFloat(as[i1 + 4]);
        f2 = PApplet.parseFloat(as[i1 + 5]);
        f3 = PApplet.parseFloat(as[i1 + 6]);
        parsePathCurveto(f4, f5, f6, f7, f2, f3);
        i = i1 + 7;
        i1 = 1;
        k = l;
        l = i1;
          goto _L23
_L14:
        f8 = PApplet.parseFloat(as[i1 + 1]);
        f5 = PApplet.parseFloat(as[i1 + 2]);
        f9 = PApplet.parseFloat(as[i1 + 3]);
        f6 = PApplet.parseFloat(as[i1 + 4]);
        f7 = f2 + PApplet.parseFloat(as[i1 + 5]);
        f4 = f3 + PApplet.parseFloat(as[i1 + 6]);
        parsePathCurveto(f2 + f8, f3 + f5, f2 + f9, f3 + f6, f7, f4);
        i = i1 + 7;
        i1 = 1;
        k = l;
        f2 = f7;
        f3 = f4;
        l = i1;
          goto _L23
_L10:
        if(k != 0)
        {
            f7 = vertices[vertexCount - 2][0];
            f3 = vertices[vertexCount - 2][1];
            f2 = vertices[vertexCount - 1][0];
            f4 = vertices[vertexCount - 1][1];
            f2 += f2 - f7;
            f3 = f4 + (f4 - f3);
        }
        f6 = PApplet.parseFloat(as[i1 + 1]);
        f5 = PApplet.parseFloat(as[i1 + 2]);
        f7 = PApplet.parseFloat(as[i1 + 3]);
        f4 = PApplet.parseFloat(as[i1 + 4]);
        parsePathCurveto(f2, f3, f6, f5, f7, f4);
        i = i1 + 5;
        i1 = 1;
        k = l;
        f2 = f7;
        f3 = f4;
        l = i1;
          goto _L23
_L19:
        if(k == 0)
        {
            f7 = f3;
            f4 = f2;
        } else
        {
            f6 = vertices[vertexCount - 2][0];
            f5 = vertices[vertexCount - 2][1];
            f4 = vertices[vertexCount - 1][0];
            f7 = vertices[vertexCount - 1][1];
            f4 += f4 - f6;
            f7 += f7 - f5;
        }
        f9 = PApplet.parseFloat(as[i1 + 1]);
        f8 = PApplet.parseFloat(as[i1 + 2]);
        f6 = f2 + PApplet.parseFloat(as[i1 + 3]);
        f5 = f3 + PApplet.parseFloat(as[i1 + 4]);
        parsePathCurveto(f4, f7, f2 + f9, f3 + f8, f6, f5);
        i = i1 + 5;
        i1 = 1;
        k = l;
        f2 = f6;
        f3 = f5;
        l = i1;
          goto _L23
_L9:
        f7 = PApplet.parseFloat(as[i1 + 1]);
        f4 = PApplet.parseFloat(as[i1 + 2]);
        f2 = PApplet.parseFloat(as[i1 + 3]);
        f3 = PApplet.parseFloat(as[i1 + 4]);
        parsePathQuadto(f7, f4, f2, f3);
        i = i1 + 5;
        i1 = 1;
        k = l;
        l = i1;
          goto _L23
_L18:
        f6 = PApplet.parseFloat(as[i1 + 1]);
        f5 = PApplet.parseFloat(as[i1 + 2]);
        f7 = f2 + PApplet.parseFloat(as[i1 + 3]);
        f4 = f3 + PApplet.parseFloat(as[i1 + 4]);
        parsePathQuadto(f6 + f2, f5 + f3, f7, f4);
        i = i1 + 5;
        i1 = 1;
        k = l;
        f2 = f7;
        f3 = f4;
        l = i1;
          goto _L23
_L11:
        if(k != 0)
        {
            f2 = vertices[vertexCount - 2][0];
            f4 = vertices[vertexCount - 2][1];
            f7 = vertices[vertexCount - 1][0];
            f3 = vertices[vertexCount - 1][1];
            f2 = f7 + (f7 - f2);
            f3 += f3 - f4;
        }
        f4 = PApplet.parseFloat(as[i1 + 1]);
        f7 = PApplet.parseFloat(as[i1 + 2]);
        parsePathQuadto(f2, f3, f4, f7);
        i = i1 + 3;
        k = l;
        f3 = f7;
        f2 = f4;
        l = 1;
          goto _L23
_L20:
        if(k == 0)
        {
            f4 = f3;
            f7 = f2;
        } else
        {
            f7 = vertices[vertexCount - 2][0];
            f5 = vertices[vertexCount - 2][1];
            f6 = vertices[vertexCount - 1][0];
            f4 = vertices[vertexCount - 1][1];
            f7 = f6 + (f6 - f7);
            f4 = (f4 - f5) + f4;
        }
        f2 += PApplet.parseFloat(as[i1 + 1]);
        f3 += PApplet.parseFloat(as[i1 + 2]);
        parsePathQuadto(f7, f4, f2, f3);
        i = i1 + 3;
        i1 = 1;
        k = l;
        l = i1;
          goto _L23
        close = true;
        i1++;
        f4 = f1;
        i = k;
        k = l;
        f3 = f;
        f2 = f1;
        f1 = f4;
        l = i;
        i = i1;
          goto _L23
    }

    protected void parsePoly(boolean flag)
    {
        family = 2;
        close = flag;
        String s = element.getString("points");
        if(s != null)
        {
            String as1[] = PApplet.splitTokens(s);
            vertexCount = as1.length;
            int i = vertexCount;
            vertices = new float[i][2];
            for(int j = 0; j < vertexCount; j++)
            {
                String as[] = PApplet.split(as1[j], ',');
                vertices[j][0] = Float.valueOf(as[0]).floatValue();
                vertices[j][1] = Float.valueOf(as[1]).floatValue();
            }

        }
    }

    protected void parseRect()
    {
        kind = 30;
        family = 1;
        params = (new float[] {
            getFloatWithUnit(element, "x"), getFloatWithUnit(element, "y"), getFloatWithUnit(element, "width"), getFloatWithUnit(element, "height")
        });
    }

    public void print()
    {
        PApplet.println(element.toString());
    }

    void setColor(String s, boolean flag)
    {
        Gradient gradient = null;
        boolean flag1 = true;
        boolean flag2 = false;
        int i = 0xff000000 & fillColor;
        String s1;
        if(s.equals("none"))
        {
            s1 = "";
            flag1 = false;
            s = null;
            i = ((flag2) ? 1 : 0);
        } else
        if(s.equals("black"))
        {
            s1 = "";
            s = null;
        } else
        if(s.equals("white"))
        {
            i |= 0xffffff;
            s1 = "";
            s = null;
        } else
        if(s.startsWith("#"))
        {
            s1 = s;
            if(s.length() == 4)
                s1 = s.replaceAll("^#(.)(.)(.)$", "#$1$1$2$2$3$3");
            i |= Integer.parseInt(s1.substring(1), 16) & 0xffffff;
            s1 = "";
            s = null;
        } else
        if(s.startsWith("rgb"))
        {
            i |= parseRGB(s);
            s1 = "";
            s = null;
        } else
        if(s.startsWith("url(#"))
        {
            s1 = s.substring(5, s.length() - 1);
            s = findChild(s1);
            if(s instanceof Gradient)
            {
                gradient = (Gradient)s;
                s = calcGradientPaint(gradient);
                i = ((flag2) ? 1 : 0);
            } else
            {
                System.err.println((new StringBuilder()).append("url ").append(s1).append(" refers to unexpected data: ").append(s).toString());
                s = null;
                i = ((flag2) ? 1 : 0);
            }
        } else
        {
            s1 = "";
            s = null;
            i = ((flag2) ? 1 : 0);
        }
        if(flag)
        {
            fill = flag1;
            fillColor = i;
            fillName = s1;
            fillGradient = gradient;
            fillGradientPaint = s;
        } else
        {
            stroke = flag1;
            strokeColor = i;
            strokeName = s1;
            strokeGradient = gradient;
            strokeGradientPaint = s;
        }
    }

    void setFillOpacity(String s)
    {
        fillOpacity = PApplet.parseFloat(s);
        fillColor = (int)(fillOpacity * 255F) << 24 | fillColor & 0xffffff;
    }

    void setOpacity(String s)
    {
        opacity = PApplet.parseFloat(s);
        strokeColor = (int)(opacity * 255F) << 24 | strokeColor & 0xffffff;
        fillColor = (int)(opacity * 255F) << 24 | fillColor & 0xffffff;
    }

    void setStrokeCap(String s)
    {
        if(!s.equals("inherit")) goto _L2; else goto _L1
_L1:
        return;
_L2:
        if(s.equals("butt"))
            strokeCap = 1;
        else
        if(s.equals("round"))
            strokeCap = 2;
        else
        if(s.equals("square"))
            strokeCap = 4;
        if(true) goto _L1; else goto _L3
_L3:
    }

    void setStrokeJoin(String s)
    {
        if(!s.equals("inherit")) goto _L2; else goto _L1
_L1:
        return;
_L2:
        if(s.equals("miter"))
            strokeJoin = 8;
        else
        if(s.equals("round"))
            strokeJoin = 2;
        else
        if(s.equals("bevel"))
            strokeJoin = 32;
        if(true) goto _L1; else goto _L3
_L3:
    }

    void setStrokeOpacity(String s)
    {
        strokeOpacity = PApplet.parseFloat(s);
        strokeColor = (int)(strokeOpacity * 255F) << 24 | strokeColor & 0xffffff;
    }

    void setStrokeWeight(String s)
    {
        strokeWeight = parseUnitSize(s);
    }

    protected void styles(PGraphics pgraphics)
    {
        super.styles(pgraphics);
        if(pgraphics instanceof PGraphicsAndroid2D)
        {
            pgraphics = (PGraphicsAndroid2D)pgraphics;
            if(strokeGradient != null)
                ((PGraphicsAndroid2D) (pgraphics)).strokePaint.setShader(strokeGradientPaint);
            if(fillGradient != null)
                ((PGraphicsAndroid2D) (pgraphics)).fillPaint.setShader(fillGradientPaint);
        }
    }

    XML element;
    Gradient fillGradient;
    Shader fillGradientPaint;
    String fillName;
    float fillOpacity;
    float opacity;
    Gradient strokeGradient;
    Shader strokeGradientPaint;
    String strokeName;
    float strokeOpacity;
}
