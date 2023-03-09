// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.core;

import java.util.HashMap;
import java.util.Map;

// Referenced classes of package processing.core:
//            PConstants, PGraphics, PApplet, PMatrix, 
//            PMatrix2D, PMatrix3D, PVector, PImage

public class PShape
    implements PConstants
{

    public PShape()
    {
        visible = true;
        openShape = false;
        openContour = false;
        style = true;
        is3D = false;
        perVertexStyles = false;
        family = 0;
    }

    public PShape(int i)
    {
        visible = true;
        openShape = false;
        openContour = false;
        style = true;
        is3D = false;
        perVertexStyles = false;
        family = i;
    }

    public PShape(PGraphics pgraphics, int i)
    {
        visible = true;
        openShape = false;
        openContour = false;
        style = true;
        is3D = false;
        perVertexStyles = false;
        g = pgraphics;
        family = i;
        textureMode = pgraphics.textureMode;
        colorMode(pgraphics.colorMode, pgraphics.colorModeX, pgraphics.colorModeY, pgraphics.colorModeZ, pgraphics.colorModeA);
        fill = pgraphics.fill;
        fillColor = pgraphics.fillColor;
        stroke = pgraphics.stroke;
        strokeColor = pgraphics.strokeColor;
        strokeWeight = pgraphics.strokeWeight;
        strokeCap = pgraphics.strokeCap;
        strokeJoin = pgraphics.strokeJoin;
        tint = pgraphics.tint;
        tintColor = pgraphics.tintColor;
        setAmbient = pgraphics.setAmbient;
        ambientColor = pgraphics.ambientColor;
        specularColor = pgraphics.specularColor;
        emissiveColor = pgraphics.emissiveColor;
        shininess = pgraphics.shininess;
        sphereDetailU = pgraphics.sphereDetailU;
        sphereDetailV = pgraphics.sphereDetailV;
        rectMode = 0;
        ellipseMode = 0;
        if(i != 0);
    }

    public transient PShape(PGraphics pgraphics, int i, float af[])
    {
        this(pgraphics, 1);
        setKind(i);
        setParams(af);
    }

    protected static void copyGeometry(PShape pshape, PShape pshape1)
    {
        pshape1.beginShape(pshape.getKind());
        copyMatrix(pshape, pshape1);
        copyStyles(pshape, pshape1);
        copyImage(pshape, pshape1);
        if(pshape.style)
        {
            for(int i = 0; i < pshape.vertexCount; i++)
            {
                float af[] = pshape.vertices[i];
                pshape1.fill((int)(af[6] * 255F) << 24 | (int)(af[3] * 255F) << 16 | (int)(af[4] * 255F) << 8 | (int)(af[5] * 255F));
                if(0.0F < PApplet.dist(af[9], af[10], af[11], 0.0F, 0.0F, 0.0F))
                    pshape1.normal(af[9], af[10], af[11]);
                pshape1.vertex(af[0], af[1], af[2], af[7], af[8]);
            }

        } else
        {
            int j = 0;
            while(j < pshape.vertexCount) 
            {
                float af1[] = pshape.vertices[j];
                if(af1[2] == 0.0F)
                    pshape1.vertex(af1[0], af1[1]);
                else
                    pshape1.vertex(af1[0], af1[1], af1[2]);
                j++;
            }
        }
        pshape1.endShape();
    }

    protected static void copyGroup(PApplet papplet, PShape pshape, PShape pshape1)
    {
        copyMatrix(pshape, pshape1);
        copyStyles(pshape, pshape1);
        copyImage(pshape, pshape1);
        for(int i = 0; i < pshape.childCount; i++)
            pshape1.addChild(createShape(papplet, pshape.children[i]));

    }

    protected static void copyImage(PShape pshape, PShape pshape1)
    {
        if(pshape.image != null)
            pshape1.texture(pshape.image);
    }

    protected static void copyMatrix(PShape pshape, PShape pshape1)
    {
        if(pshape.matrix != null)
            pshape1.applyMatrix(pshape.matrix);
    }

    protected static void copyPath(PShape pshape, PShape pshape1)
    {
        copyMatrix(pshape, pshape1);
        copyStyles(pshape, pshape1);
        copyImage(pshape, pshape1);
        pshape1.close = pshape.close;
        pshape1.setPath(pshape.vertexCount, pshape.vertices, pshape.vertexCodeCount, pshape.vertexCodes);
    }

    protected static void copyPrimitive(PShape pshape, PShape pshape1)
    {
        copyMatrix(pshape, pshape1);
        copyStyles(pshape, pshape1);
        copyImage(pshape, pshape1);
    }

    protected static void copyStyles(PShape pshape, PShape pshape1)
    {
        if(pshape.stroke)
        {
            pshape1.stroke = true;
            pshape1.strokeColor = pshape.strokeColor;
            pshape1.strokeWeight = pshape.strokeWeight;
            pshape1.strokeCap = pshape.strokeCap;
            pshape1.strokeJoin = pshape.strokeJoin;
        } else
        {
            pshape1.stroke = false;
        }
        if(pshape.fill)
        {
            pshape1.fill = true;
            pshape1.fillColor = pshape.fillColor;
        } else
        {
            pshape1.fill = false;
        }
    }

    protected static PShape createShape(PApplet papplet, PShape pshape)
    {
        PShape pshape1 = null;
        if(pshape.family != 0) goto _L2; else goto _L1
_L1:
        pshape1 = papplet.createShape(0);
        copyGroup(papplet, pshape, pshape1);
_L4:
        pshape1.setName(pshape.name);
        return pshape1;
_L2:
        if(pshape.family == 1)
        {
            pshape1 = papplet.createShape(pshape.kind, pshape.params);
            copyPrimitive(pshape, pshape1);
        } else
        if(pshape.family == 3)
        {
            pshape1 = papplet.createShape(pshape.kind);
            copyGeometry(pshape, pshape1);
        } else
        if(pshape.family == 2)
        {
            pshape1 = papplet.createShape(2);
            copyPath(pshape, pshape1);
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void addChild(PShape pshape)
    {
        if(children == null)
            children = new PShape[1];
        if(childCount == children.length)
            children = (PShape[])PApplet.expand(children);
        PShape apshape[] = children;
        int i = childCount;
        childCount = i + 1;
        apshape[i] = pshape;
        pshape.parent = this;
        if(pshape.getName() != null)
            addName(pshape.getName(), pshape);
    }

    public void addChild(PShape pshape, int i)
    {
        if(i < childCount)
        {
            if(childCount == children.length)
                children = (PShape[])PApplet.expand(children);
            for(int j = childCount - 1; j >= i; j--)
                children[j + 1] = children[j];

            childCount = childCount + 1;
            children[i] = pshape;
            pshape.parent = this;
            if(pshape.getName() != null)
                addName(pshape.getName(), pshape);
        }
    }

    public void addName(String s, PShape pshape)
    {
        if(parent != null)
        {
            parent.addName(s, pshape);
        } else
        {
            if(nameTable == null)
                nameTable = new HashMap();
            nameTable.put(s, pshape);
        }
    }

    public void ambient(float f)
    {
        if(!openShape)
        {
            PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
                "ambient()"
            });
        } else
        {
            setAmbient = true;
            colorCalc(f);
            ambientColor = calcColor;
        }
    }

    public void ambient(float f, float f1, float f2)
    {
        if(!openShape)
        {
            PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
                "ambient()"
            });
        } else
        {
            setAmbient = true;
            colorCalc(f, f1, f2);
            ambientColor = calcColor;
        }
    }

    public void ambient(int i)
    {
        if(!openShape)
        {
            PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
                "ambient()"
            });
        } else
        {
            setAmbient = true;
            colorCalc(i);
            ambientColor = calcColor;
        }
    }

    public void applyMatrix(float f, float f1, float f2, float f3, float f4, float f5)
    {
        checkMatrix(2);
        matrix.apply(f, f1, f2, f3, f4, f5);
    }

    public void applyMatrix(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8, float f9, float f10, float f11, float f12, float f13, 
            float f14, float f15)
    {
        checkMatrix(3);
        matrix.apply(f, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15);
    }

    public void applyMatrix(PMatrix2D pmatrix2d)
    {
        applyMatrix(pmatrix2d.m00, pmatrix2d.m01, 0.0F, pmatrix2d.m02, pmatrix2d.m10, pmatrix2d.m11, 0.0F, pmatrix2d.m12, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
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

    public transient void attrib(String s, float af[])
    {
    }

    public transient void attrib(String s, int ai[])
    {
    }

    public transient void attrib(String s, boolean aflag[])
    {
    }

    public void attribColor(String s, int i)
    {
    }

    public void attribNormal(String s, float f, float f1, float f2)
    {
    }

    public void attribPosition(String s, float f, float f1, float f2)
    {
    }

    public void beginContour()
    {
        if(!openShape)
            PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
                "beginContour()"
            });
        else
        if(family == 0)
            PGraphics.showWarning("Cannot begin contour in GROUP shapes");
        else
        if(openContour)
        {
            PGraphics.showWarning("Already called beginContour().");
        } else
        {
            openContour = true;
            beginContourImpl();
        }
    }

    protected void beginContourImpl()
    {
        if(vertexCodes != null) goto _L2; else goto _L1
_L1:
        vertexCodes = new int[10];
_L4:
        int ai[] = vertexCodes;
        int i = vertexCodeCount;
        vertexCodeCount = i + 1;
        ai[i] = 4;
        return;
_L2:
        if(vertexCodes.length == vertexCodeCount)
            vertexCodes = PApplet.expand(vertexCodes);
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void beginShape()
    {
        beginShape(20);
    }

    public void beginShape(int i)
    {
        kind = i;
        openShape = true;
    }

    public void bezierDetail(int i)
    {
    }

    public void bezierVertex(float f, float f1, float f2, float f3, float f4, float f5)
    {
        if(vertices != null) goto _L2; else goto _L1
_L1:
        vertices = new float[10][];
_L4:
        Object obj = vertices;
        int i = vertexCount;
        vertexCount = i + 1;
        obj[i] = (new float[] {
            f, f1
        });
        obj = vertices;
        i = vertexCount;
        vertexCount = i + 1;
        obj[i] = (new float[] {
            f2, f3
        });
        obj = vertices;
        i = vertexCount;
        vertexCount = i + 1;
        obj[i] = (new float[] {
            f4, f5
        });
        if(vertexCodes.length == vertexCodeCount)
            vertexCodes = PApplet.expand(vertexCodes);
        obj = vertexCodes;
        i = vertexCodeCount;
        vertexCodeCount = i + 1;
        obj[i] = 1;
        if(f4 > width)
            width = f4;
        if(f5 > height)
            height = f5;
        return;
_L2:
        if(vertexCount + 2 >= vertices.length)
            vertices = (float[][])PApplet.expand(vertices);
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void bezierVertex(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8)
    {
    }

    protected void checkMatrix(int i)
    {
        if(matrix != null) goto _L2; else goto _L1
_L1:
        if(i == 2)
            matrix = new PMatrix2D();
        else
            matrix = new PMatrix3D();
_L4:
        return;
_L2:
        if(i == 3 && (matrix instanceof PMatrix2D))
            matrix = new PMatrix3D(matrix);
        if(true) goto _L4; else goto _L3
_L3:
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
        f5 = f;
        if(f < 0.0F)
            f5 = 0.0F;
        f = f1;
        if(f1 < 0.0F)
            f = 0.0F;
        colorMode;
        JVM INSTR tableswitch 1 3: default 144
    //                   1 244
    //                   2 144
    //                   3 321;
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
            calcB = f5 / colorModeZ;
            calcA = f / colorModeA;
        } else
        {
            calcR = f2;
            calcG = f3;
            calcB = f5;
            calcA = f;
        }
          goto _L1
_L3:
        f4 = f2 / colorModeX;
        f3 /= colorModeY;
        f2 = f5 / colorModeZ;
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
        f = (f4 - (float)(int)f4) * 6F;
        f4 = f - (float)(int)f;
        f1 = (1.0F - f3) * f2;
        f5 = (1.0F - f3 * f4) * f2;
        f3 = (1.0F - (1.0F - f4) * f3) * f2;
        switch((int)f)
        {
        case 0: // '\0'
            calcR = f2;
            calcG = f3;
            calcB = f1;
            break;

        case 1: // '\001'
            calcR = f5;
            calcG = f2;
            calcB = f1;
            break;

        case 2: // '\002'
            calcR = f1;
            calcG = f2;
            calcB = f3;
            break;

        case 3: // '\003'
            calcR = f1;
            calcG = f5;
            calcB = f2;
            break;

        case 4: // '\004'
            calcR = f3;
            calcG = f1;
            calcB = f2;
            break;

        case 5: // '\005'
            calcR = f2;
            calcG = f1;
            calcB = f5;
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

    public boolean contains(float f, float f1)
    {
        boolean flag;
        if(family == 2)
        {
            int i = vertexCount - 1;
            int j = 0;
            flag = false;
            while(j < vertexCount) 
            {
                int k;
                boolean flag1;
                boolean flag2;
                if(vertices[j][1] > f1)
                    k = 1;
                else
                    k = 0;
                if(vertices[i][1] > f1)
                    flag1 = true;
                else
                    flag1 = false;
                flag2 = flag;
                if(k != flag1)
                {
                    flag2 = flag;
                    if(f < ((vertices[i][0] - vertices[j][0]) * (f1 - vertices[j][1])) / (vertices[i][1] - vertices[j][1]) + vertices[j][0])
                        if(!flag)
                            flag2 = true;
                        else
                            flag2 = false;
                }
                k = j + 1;
                i = j;
                j = k;
                flag = flag2;
            }
        } else
        {
            throw new IllegalArgumentException("The contains() method is only implemented for paths.");
        }
        return flag;
    }

    protected void crop()
    {
        if(children.length != childCount)
            children = (PShape[])PApplet.subset(children, 0, childCount);
    }

    public void curveDetail(int i)
    {
    }

    public void curveTightness(float f)
    {
    }

    public void curveVertex(float f, float f1)
    {
    }

    public void curveVertex(float f, float f1, float f2)
    {
    }

    public void disableStyle()
    {
        int i = 0;
        style = false;
        for(; i < childCount; i++)
            children[i].disableStyle();

    }

    public void draw(PGraphics pgraphics)
    {
        if(visible)
        {
            pre(pgraphics);
            drawImpl(pgraphics);
            post(pgraphics);
        }
    }

    protected void drawGeometry(PGraphics pgraphics)
    {
        int i = 0;
        pgraphics.beginShape(kind);
        if(style)
        {
            for(; i < vertexCount; i++)
                pgraphics.vertex(vertices[i]);

        } else
        {
            i = 0;
            while(i < vertexCount) 
            {
                float af[] = vertices[i];
                if(af[2] == 0.0F)
                    pgraphics.vertex(af[0], af[1]);
                else
                    pgraphics.vertex(af[0], af[1], af[2]);
                i++;
            }
        }
        if(close)
            i = 2;
        else
            i = 1;
        pgraphics.endShape(i);
    }

    protected void drawGroup(PGraphics pgraphics)
    {
        for(int i = 0; i < childCount; i++)
            children[i].draw(pgraphics);

    }

    protected void drawImpl(PGraphics pgraphics)
    {
        if(family != 0) goto _L2; else goto _L1
_L1:
        drawGroup(pgraphics);
_L4:
        return;
_L2:
        if(family == 1)
            drawPrimitive(pgraphics);
        else
        if(family == 3)
            drawPath(pgraphics);
        else
        if(family == 2)
            drawPath(pgraphics);
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected void drawPath(PGraphics pgraphics)
    {
        if(vertices != null) goto _L2; else goto _L1
_L1:
        return;
_L2:
        int i;
        i = 0;
        pgraphics.beginShape();
        if(vertexCodeCount != 0) goto _L4; else goto _L3
_L3:
        int k1;
        if(vertices[0].length == 2)
        {
            int k = 0;
            do
            {
                k1 = i;
                if(k >= vertexCount)
                    break;
                pgraphics.vertex(vertices[k][0], vertices[k][1]);
                k++;
            } while(true);
        } else
        {
            int l = 0;
            do
            {
                k1 = i;
                if(l >= vertexCount)
                    break;
                pgraphics.vertex(vertices[l][0], vertices[l][1], vertices[l][2]);
                l++;
            } while(true);
        }
          goto _L5
_L4:
        if(vertices[0].length != 2) goto _L7; else goto _L6
_L6:
        int i1;
        i = 0;
        i1 = 0;
        k1 = 0;
_L15:
        if(i >= vertexCodeCount) goto _L5; else goto _L8
_L8:
        vertexCodes[i];
        JVM INSTR tableswitch 0 4: default 184
    //                   0 210
    //                   1 304
    //                   2 245
    //                   3 383
    //                   4 418;
           goto _L9 _L10 _L11 _L12 _L13 _L14
_L14:
        break MISSING_BLOCK_LABEL_418;
_L9:
        int l1 = i1;
        i1 = k1;
        k1 = l1;
_L16:
        i++;
        int i2 = k1;
        k1 = i1;
        i1 = i2;
          goto _L15
_L10:
        pgraphics.vertex(vertices[i1][0], vertices[i1][1]);
        int j2 = i1 + 1;
        i1 = k1;
        k1 = j2;
          goto _L16
_L12:
        pgraphics.quadraticVertex(vertices[i1 + 0][0], vertices[i1 + 0][1], vertices[i1 + 1][0], vertices[i1 + 1][1]);
        int k2 = i1 + 2;
        i1 = k1;
        k1 = k2;
          goto _L16
_L11:
        pgraphics.bezierVertex(vertices[i1 + 0][0], vertices[i1 + 0][1], vertices[i1 + 1][0], vertices[i1 + 1][1], vertices[i1 + 2][0], vertices[i1 + 2][1]);
        int l2 = i1 + 3;
        i1 = k1;
        k1 = l2;
          goto _L16
_L13:
        pgraphics.curveVertex(vertices[i1][0], vertices[i1][1]);
        int i3 = i1 + 1;
        i1 = k1;
        k1 = i3;
          goto _L16
        if(k1 != 0)
            pgraphics.endContour();
        pgraphics.beginContour();
        k1 = i1;
        i1 = 1;
          goto _L16
_L5:
        if(k1 != 0)
            pgraphics.endContour();
        int j;
        int j1;
        int j3;
        if(close)
            k1 = 2;
        else
            k1 = 1;
        pgraphics.endShape(k1);
        if(true) goto _L1; else goto _L7
_L7:
        j = 0;
        j1 = 0;
        k1 = 0;
_L24:
        if(j >= vertexCodeCount) goto _L5; else goto _L17
_L17:
        vertexCodes[j];
        JVM INSTR tableswitch 0 4: default 524
    //                   0 550
    //                   1 672
    //                   2 593
    //                   3 781
    //                   4 824;
           goto _L18 _L19 _L20 _L21 _L22 _L23
_L23:
        break MISSING_BLOCK_LABEL_824;
_L18:
        j3 = j1;
        j1 = k1;
        k1 = j3;
_L25:
        j++;
        j3 = j1;
        j1 = k1;
        k1 = j3;
          goto _L24
_L19:
        pgraphics.vertex(vertices[j1][0], vertices[j1][1], vertices[j1][2]);
        j3 = j1 + 1;
        j1 = k1;
        k1 = j3;
          goto _L25
_L21:
        pgraphics.quadraticVertex(vertices[j1 + 0][0], vertices[j1 + 0][1], vertices[j1 + 0][2], vertices[j1 + 1][0], vertices[j1 + 1][1], vertices[j1 + 0][2]);
        j3 = j1 + 2;
        j1 = k1;
        k1 = j3;
          goto _L25
_L20:
        pgraphics.bezierVertex(vertices[j1 + 0][0], vertices[j1 + 0][1], vertices[j1 + 0][2], vertices[j1 + 1][0], vertices[j1 + 1][1], vertices[j1 + 1][2], vertices[j1 + 2][0], vertices[j1 + 2][1], vertices[j1 + 2][2]);
        j3 = j1 + 3;
        j1 = k1;
        k1 = j3;
          goto _L25
_L22:
        pgraphics.curveVertex(vertices[j1][0], vertices[j1][1], vertices[j1][2]);
        j3 = j1 + 1;
        j1 = k1;
        k1 = j3;
          goto _L25
        if(k1 != 0)
            pgraphics.endContour();
        pgraphics.beginContour();
        k1 = j1;
        j1 = 1;
          goto _L25
    }

    protected void drawPrimitive(PGraphics pgraphics)
    {
        if(kind != 2) goto _L2; else goto _L1
_L1:
        pgraphics.point(params[0], params[1]);
_L4:
        return;
_L2:
        if(kind == 4)
        {
            if(params.length == 4)
                pgraphics.line(params[0], params[1], params[2], params[3]);
            else
                pgraphics.line(params[0], params[1], params[2], params[3], params[4], params[5]);
        } else
        if(kind == 8)
            pgraphics.triangle(params[0], params[1], params[2], params[3], params[4], params[5]);
        else
        if(kind == 16)
            pgraphics.quad(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7]);
        else
        if(kind == 30)
        {
            if(image != null)
            {
                pgraphics.imageMode(0);
                pgraphics.image(image, params[0], params[1], params[2], params[3]);
            } else
            {
                if(params.length != 5)
                    pgraphics.rectMode(0);
                else
                    pgraphics.rectMode((int)params[4]);
                pgraphics.rect(params[0], params[1], params[2], params[3]);
            }
        } else
        if(kind == 31)
        {
            pgraphics.ellipseMode(0);
            pgraphics.ellipse(params[0], params[1], params[2], params[3]);
        } else
        if(kind == 32)
        {
            pgraphics.ellipseMode(0);
            pgraphics.arc(params[0], params[1], params[2], params[3], params[4], params[5]);
        } else
        if(kind == 41)
        {
            if(params.length == 1)
                pgraphics.box(params[0]);
            else
                pgraphics.box(params[0], params[1], params[2]);
        } else
        if(kind == 40)
            pgraphics.sphere(params[0]);
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void emissive(float f)
    {
        if(!openShape)
        {
            PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
                "emissive()"
            });
        } else
        {
            colorCalc(f);
            emissiveColor = calcColor;
        }
    }

    public void emissive(float f, float f1, float f2)
    {
        if(!openShape)
        {
            PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
                "emissive()"
            });
        } else
        {
            colorCalc(f, f1, f2);
            emissiveColor = calcColor;
        }
    }

    public void emissive(int i)
    {
        if(!openShape)
        {
            PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
                "emissive()"
            });
        } else
        {
            colorCalc(i);
            emissiveColor = calcColor;
        }
    }

    public void enableStyle()
    {
        style = true;
        for(int i = 0; i < childCount; i++)
            children[i].enableStyle();

    }

    public void endContour()
    {
        if(!openShape)
            PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
                "endContour()"
            });
        else
        if(family == 0)
            PGraphics.showWarning("Cannot end contour in GROUP shapes");
        else
        if(!openContour)
        {
            PGraphics.showWarning("Need to call beginContour() first.");
        } else
        {
            endContourImpl();
            openContour = false;
        }
    }

    protected void endContourImpl()
    {
    }

    public void endShape()
    {
        endShape(1);
    }

    public void endShape(int i)
    {
        if(family == 0)
            PGraphics.showWarning("Cannot end GROUP shape");
        else
        if(!openShape)
        {
            PGraphics.showWarning("Need to call beginShape() first");
        } else
        {
            boolean flag;
            if(i == 2)
                flag = true;
            else
                flag = false;
            close = flag;
            openShape = false;
        }
    }

    public void fill(float f)
    {
        if(openShape) goto _L2; else goto _L1
_L1:
        PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
            "fill()"
        });
_L4:
        return;
_L2:
        fill = true;
        colorCalc(f);
        fillColor = calcColor;
        if(!setAmbient)
            ambientColor = fillColor;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void fill(float f, float f1)
    {
        if(openShape) goto _L2; else goto _L1
_L1:
        PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
            "fill()"
        });
_L4:
        return;
_L2:
        fill = true;
        colorCalc(f, f1);
        fillColor = calcColor;
        if(!setAmbient)
        {
            ambient(fillColor);
            setAmbient = false;
        }
        if(!setAmbient)
            ambientColor = fillColor;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void fill(float f, float f1, float f2)
    {
        if(openShape) goto _L2; else goto _L1
_L1:
        PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
            "fill()"
        });
_L4:
        return;
_L2:
        fill = true;
        colorCalc(f, f1, f2);
        fillColor = calcColor;
        if(!setAmbient)
            ambientColor = fillColor;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void fill(float f, float f1, float f2, float f3)
    {
        if(openShape) goto _L2; else goto _L1
_L1:
        PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
            "fill()"
        });
_L4:
        return;
_L2:
        fill = true;
        colorCalc(f, f1, f2, f3);
        fillColor = calcColor;
        if(!setAmbient)
            ambientColor = fillColor;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void fill(int i)
    {
        if(openShape) goto _L2; else goto _L1
_L1:
        PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
            "fill()"
        });
_L4:
        return;
_L2:
        fill = true;
        colorCalc(i);
        fillColor = calcColor;
        if(!setAmbient)
            ambientColor = fillColor;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void fill(int i, float f)
    {
        if(openShape) goto _L2; else goto _L1
_L1:
        PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
            "fill()"
        });
_L4:
        return;
_L2:
        fill = true;
        colorCalc(i, f);
        fillColor = calcColor;
        if(!setAmbient)
            ambientColor = fillColor;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public PShape findChild(String s)
    {
        if(parent == null)
            s = getChild(s);
        else
            s = parent.findChild(s);
        return s;
    }

    public int getAmbient(int i)
    {
        if(vertices == null || i >= vertices.length)
        {
            PGraphics.showWarning((new StringBuilder()).append("%1$s vertex index does not exist (").append(i).append(")").toString(), new Object[] {
                "getAmbient()"
            });
            i = ambientColor;
        } else
        {
            i = (int)(vertices[i][25] * 255F) << 16 | 0xff000000 | (int)(vertices[i][26] * 255F) << 8 | (int)(vertices[i][27] * 255F);
        }
        return i;
    }

    public PShape getChild(int i)
    {
        crop();
        return children[i];
    }

    public PShape getChild(String s)
    {
        if(name == null || !name.equals(s)) goto _L2; else goto _L1
_L1:
        s = this;
_L4:
        return s;
_L2:
        if(nameTable != null)
        {
            PShape pshape = (PShape)nameTable.get(s);
            if(pshape != null)
            {
                s = pshape;
                continue; /* Loop/switch isn't completed */
            }
        }
        int i = 0;
        do
        {
            if(i >= childCount)
                break;
            PShape pshape1 = children[i].getChild(s);
            if(pshape1 != null)
            {
                s = pshape1;
                continue; /* Loop/switch isn't completed */
            }
            i++;
        } while(true);
        s = null;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public int getChildCount()
    {
        return childCount;
    }

    public int getChildIndex(PShape pshape)
    {
        int i = 0;
_L3:
        if(i >= childCount)
            break MISSING_BLOCK_LABEL_28;
        if(children[i] != pshape) goto _L2; else goto _L1
_L1:
        return i;
_L2:
        i++;
          goto _L3
        i = -1;
          goto _L1
    }

    public PShape[] getChildren()
    {
        crop();
        return children;
    }

    public float getDepth()
    {
        return depth;
    }

    public int getEmissive(int i)
    {
        if(vertices == null || i >= vertices.length)
        {
            PGraphics.showWarning((new StringBuilder()).append("%1$s vertex index does not exist (").append(i).append(")").toString(), new Object[] {
                "getEmissive()"
            });
            i = emissiveColor;
        } else
        {
            i = (int)(vertices[i][32] * 255F) << 16 | 0xff000000 | (int)(vertices[i][33] * 255F) << 8 | (int)(vertices[i][34] * 255F);
        }
        return i;
    }

    public int getFamily()
    {
        return family;
    }

    public int getFill(int i)
    {
        int j = 0;
        if(vertices != null && i < vertices.length) goto _L2; else goto _L1
_L1:
        PGraphics.showWarning((new StringBuilder()).append("%1$s vertex index does not exist (").append(i).append(")").toString(), new Object[] {
            "getFill()"
        });
        j = fillColor;
_L4:
        return j;
_L2:
        if(image == null)
            j = (int)(vertices[i][6] * 255F) << 24 | (int)(vertices[i][3] * 255F) << 16 | (int)(vertices[i][4] * 255F) << 8 | (int)(vertices[i][5] * 255F);
        if(true) goto _L4; else goto _L3
_L3:
    }

    public float getHeight()
    {
        return height;
    }

    public int getKind()
    {
        return kind;
    }

    public String getName()
    {
        return name;
    }

    public PVector getNormal(int i)
    {
        return getNormal(i, null);
    }

    public PVector getNormal(int i, PVector pvector)
    {
        PVector pvector1 = pvector;
        if(pvector == null)
            pvector1 = new PVector();
        pvector1.x = vertices[i][9];
        pvector1.y = vertices[i][10];
        pvector1.z = vertices[i][11];
        return pvector1;
    }

    public float getNormalX(int i)
    {
        return vertices[i][9];
    }

    public float getNormalY(int i)
    {
        return vertices[i][10];
    }

    public float getNormalZ(int i)
    {
        return vertices[i][11];
    }

    public float getParam(int i)
    {
        return params[i];
    }

    public float[] getParams()
    {
        return getParams(null);
    }

    public float[] getParams(float af[])
    {
        float af1[];
label0:
        {
            if(af != null)
            {
                af1 = af;
                if(af.length == params.length)
                    break label0;
            }
            af1 = new float[params.length];
        }
        PApplet.arrayCopy(params, af1);
        return af1;
    }

    public PShape getParent()
    {
        return parent;
    }

    public float getShininess(int i)
    {
        float f;
        if(vertices == null || i >= vertices.length)
        {
            PGraphics.showWarning((new StringBuilder()).append("%1$s vertex index does not exist (").append(i).append(")").toString(), new Object[] {
                "getShininess()"
            });
            f = shininess;
        } else
        {
            f = vertices[i][31];
        }
        return f;
    }

    public int getSpecular(int i)
    {
        if(vertices == null || i >= vertices.length)
        {
            PGraphics.showWarning((new StringBuilder()).append("%1$s vertex index does not exist (").append(i).append(")").toString(), new Object[] {
                "getSpecular()"
            });
            i = specularColor;
        } else
        {
            i = (int)(vertices[i][28] * 255F) << 16 | 0xff000000 | (int)(vertices[i][29] * 255F) << 8 | (int)(vertices[i][30] * 255F);
        }
        return i;
    }

    public int getStroke(int i)
    {
        if(vertices == null || i >= vertices.length)
        {
            PGraphics.showWarning((new StringBuilder()).append("%1$s vertex index does not exist (").append(i).append(")").toString(), new Object[] {
                "getStroke()"
            });
            i = strokeColor;
        } else
        {
            i = (int)(vertices[i][16] * 255F) << 24 | (int)(vertices[i][13] * 255F) << 16 | (int)(vertices[i][14] * 255F) << 8 | (int)(vertices[i][15] * 255F);
        }
        return i;
    }

    public float getStrokeWeight(int i)
    {
        float f;
        if(vertices == null || i >= vertices.length)
        {
            PGraphics.showWarning((new StringBuilder()).append("%1$s vertex index does not exist (").append(i).append(")").toString(), new Object[] {
                "getStrokeWeight()"
            });
            f = strokeWeight;
        } else
        {
            f = vertices[i][17];
        }
        return f;
    }

    public PShape getTessellation()
    {
        return null;
    }

    public float getTextureU(int i)
    {
        return vertices[i][7];
    }

    public float getTextureV(int i)
    {
        return vertices[i][8];
    }

    public int getTint(int i)
    {
        int j = 0;
        if(vertices != null && i < vertices.length) goto _L2; else goto _L1
_L1:
        PGraphics.showWarning((new StringBuilder()).append("%1$s vertex index does not exist (").append(i).append(")").toString(), new Object[] {
            "getTint()"
        });
        j = tintColor;
_L4:
        return j;
_L2:
        if(image != null)
            j = (int)(vertices[i][6] * 255F) << 24 | (int)(vertices[i][3] * 255F) << 16 | (int)(vertices[i][4] * 255F) << 8 | (int)(vertices[i][5] * 255F);
        if(true) goto _L4; else goto _L3
_L3:
    }

    public PVector getVertex(int i)
    {
        return getVertex(i, null);
    }

    public PVector getVertex(int i, PVector pvector)
    {
        PVector pvector1 = pvector;
        if(pvector == null)
            pvector1 = new PVector();
        pvector = vertices[i];
        pvector1.x = pvector[0];
        pvector1.y = pvector[1];
        if(pvector.length > 2)
            pvector1.z = pvector[2];
        else
            pvector1.z = 0.0F;
        return pvector1;
    }

    public int getVertexCode(int i)
    {
        return vertexCodes[i];
    }

    public int getVertexCodeCount()
    {
        return vertexCodeCount;
    }

    public int[] getVertexCodes()
    {
        int ai[];
        if(vertexCodes == null)
        {
            ai = null;
        } else
        {
            if(vertexCodes.length != vertexCodeCount)
                vertexCodes = PApplet.subset(vertexCodes, 0, vertexCodeCount);
            ai = vertexCodes;
        }
        return ai;
    }

    public int getVertexCount()
    {
        if(family == 0 || family == 1)
            PGraphics.showWarning("getVertexCount() only works with PATH or GEOMETRY shapes");
        return vertexCount;
    }

    public float getVertexX(int i)
    {
        return vertices[i][0];
    }

    public float getVertexY(int i)
    {
        return vertices[i][1];
    }

    public float getVertexZ(int i)
    {
        return vertices[i][2];
    }

    public float getWidth()
    {
        return width;
    }

    public boolean is2D()
    {
        boolean flag;
        if(!is3D)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public boolean is3D()
    {
        return is3D;
    }

    public boolean isClosed()
    {
        return close;
    }

    public boolean isVisible()
    {
        return visible;
    }

    public void noFill()
    {
        if(openShape) goto _L2; else goto _L1
_L1:
        PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
            "noFill()"
        });
_L4:
        return;
_L2:
        fill = false;
        fillColor = 0;
        if(!setAmbient)
            ambientColor = fillColor;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void noStroke()
    {
        if(!openShape)
            PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
                "noStroke()"
            });
        else
            stroke = false;
    }

    public void noTexture()
    {
        if(!openShape)
            PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
                "noTexture()"
            });
        else
            image = null;
    }

    public void noTint()
    {
        if(!openShape)
            PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
                "noTint()"
            });
        else
            tint = false;
    }

    public void normal(float f, float f1, float f2)
    {
    }

    protected void post(PGraphics pgraphics)
    {
        if(matrix != null)
            pgraphics.popMatrix();
        if(style)
            pgraphics.popStyle();
    }

    protected void pre(PGraphics pgraphics)
    {
        if(matrix != null)
        {
            pgraphics.pushMatrix();
            pgraphics.applyMatrix(matrix);
        }
        if(style)
        {
            pgraphics.pushStyle();
            styles(pgraphics);
        }
    }

    public void quadraticVertex(float f, float f1, float f2, float f3)
    {
        if(vertices != null) goto _L2; else goto _L1
_L1:
        vertices = new float[10][];
_L4:
        Object obj = vertices;
        int i = vertexCount;
        vertexCount = i + 1;
        obj[i] = (new float[] {
            f, f1
        });
        obj = vertices;
        i = vertexCount;
        vertexCount = i + 1;
        obj[i] = (new float[] {
            f2, f3
        });
        if(vertexCodes.length == vertexCodeCount)
            vertexCodes = PApplet.expand(vertexCodes);
        obj = vertexCodes;
        i = vertexCodeCount;
        vertexCodeCount = i + 1;
        obj[i] = 2;
        if(f2 > width)
            width = f2;
        if(f3 > height)
            height = f3;
        return;
_L2:
        if(vertexCount + 1 >= vertices.length)
            vertices = (float[][])PApplet.expand(vertices);
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void quadraticVertex(float f, float f1, float f2, float f3, float f4, float f5)
    {
    }

    public void removeChild(int i)
    {
        if(i < childCount)
        {
            PShape pshape = children[i];
            for(; i < childCount - 1; i++)
                children[i] = children[i + 1];

            childCount = childCount - 1;
            if(pshape.getName() != null && nameTable != null)
                nameTable.remove(pshape.getName());
        }
    }

    public void resetMatrix()
    {
        checkMatrix(2);
        matrix.reset();
    }

    public void rotate(float f)
    {
        checkMatrix(2);
        matrix.rotate(f);
    }

    public void rotate(float f, float f1, float f2, float f3)
    {
        checkMatrix(3);
        float f4 = f1 * f1 + f2 * f2 + f3 * f3;
        float f5 = f1;
        float f6 = f2;
        float f7 = f3;
        if(Math.abs(f4 - 1.0F) > 0.0001F)
        {
            f7 = PApplet.sqrt(f4);
            f5 = f1 / f7;
            f6 = f2 / f7;
            f7 = f3 / f7;
        }
        matrix.rotate(f, f5, f6, f7);
    }

    public void rotateX(float f)
    {
        rotate(f, 1.0F, 0.0F, 0.0F);
    }

    public void rotateY(float f)
    {
        rotate(f, 0.0F, 1.0F, 0.0F);
    }

    public void rotateZ(float f)
    {
        rotate(f, 0.0F, 0.0F, 1.0F);
    }

    public void scale(float f)
    {
        checkMatrix(2);
        matrix.scale(f);
    }

    public void scale(float f, float f1)
    {
        checkMatrix(2);
        matrix.scale(f, f1);
    }

    public void scale(float f, float f1, float f2)
    {
        checkMatrix(3);
        matrix.scale(f, f1, f2);
    }

    public void set3D(boolean flag)
    {
        is3D = flag;
    }

    public void setAmbient(int i)
    {
        int j = 0;
        if(!openShape) goto _L2; else goto _L1
_L1:
        PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
            "setAmbient()"
        });
_L4:
        return;
_L2:
        ambientColor = i;
        if(vertices != null)
            while(j < vertices.length) 
            {
                setAmbient(j, i);
                j++;
            }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void setAmbient(int i, int j)
    {
        if(openShape)
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setAmbient()"
            });
        else
        if(vertices == null || i >= vertices.length)
        {
            PGraphics.showWarning((new StringBuilder()).append("%1$s vertex index does not exist (").append(i).append(")").toString(), new Object[] {
                "setAmbient()"
            });
        } else
        {
            vertices[i][25] = (float)(j >> 16 & 0xff) / 255F;
            vertices[i][26] = (float)(j >> 8 & 0xff) / 255F;
            vertices[i][27] = (float)(j >> 0 & 0xff) / 255F;
        }
    }

    public transient void setAttrib(String s, int i, float af[])
    {
    }

    public transient void setAttrib(String s, int i, int ai[])
    {
    }

    public transient void setAttrib(String s, int i, boolean aflag[])
    {
    }

    public void setEmissive(int i)
    {
        int j = 0;
        if(!openShape) goto _L2; else goto _L1
_L1:
        PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
            "setEmissive()"
        });
_L4:
        return;
_L2:
        emissiveColor = i;
        if(vertices != null)
            while(j < vertices.length) 
            {
                setEmissive(j, i);
                j++;
            }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void setEmissive(int i, int j)
    {
        if(openShape)
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setEmissive()"
            });
        else
        if(vertices == null || i >= vertices.length)
        {
            PGraphics.showWarning((new StringBuilder()).append("%1$s vertex index does not exist (").append(i).append(")").toString(), new Object[] {
                "setEmissive()"
            });
        } else
        {
            vertices[i][32] = (float)(j >> 16 & 0xff) / 255F;
            vertices[i][33] = (float)(j >> 8 & 0xff) / 255F;
            vertices[i][34] = (float)(j >> 0 & 0xff) / 255F;
        }
    }

    public void setFamily(int i)
    {
        family = i;
    }

    public void setFill(int i)
    {
        int j = 0;
        if(!openShape) goto _L2; else goto _L1
_L1:
        PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
            "setFill()"
        });
_L4:
        return;
_L2:
        fillColor = i;
        if(vertices != null)
            while(j < vertices.length) 
            {
                setFill(j, i);
                j++;
            }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void setFill(int i, int j)
    {
        if(!openShape) goto _L2; else goto _L1
_L1:
        PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
            "setFill()"
        });
_L4:
        return;
_L2:
        if(vertices == null || i >= vertices.length)
            PGraphics.showWarning((new StringBuilder()).append("%1$s vertex index does not exist (").append(i).append(")").toString(), new Object[] {
                "getFill()"
            });
        else
        if(image == null)
        {
            vertices[i][6] = (float)(j >> 24 & 0xff) / 255F;
            vertices[i][3] = (float)(j >> 16 & 0xff) / 255F;
            vertices[i][4] = (float)(j >> 8 & 0xff) / 255F;
            vertices[i][5] = (float)(j >> 0 & 0xff) / 255F;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void setFill(boolean flag)
    {
        if(openShape)
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setFill()"
            });
        else
            fill = flag;
    }

    public void setKind(int i)
    {
        kind = i;
    }

    public void setName(String s)
    {
        name = s;
    }

    public void setNormal(int i, float f, float f1, float f2)
    {
        if(openShape)
        {
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setNormal()"
            });
        } else
        {
            vertices[i][9] = f;
            vertices[i][10] = f1;
            vertices[i][11] = f2;
        }
    }

    protected void setParams(float af[])
    {
        if(params == null)
            params = new float[af.length];
        if(af.length != params.length)
            PGraphics.showWarning("Wrong number of parameters");
        else
            PApplet.arrayCopy(af, params);
    }

    public void setPath(int i, float af[][])
    {
        setPath(i, af, 0, null);
    }

    protected void setPath(int i, float af[][], int j, int ai[])
    {
_L2:
        return;
        if(af == null || af.length < i || j > 0 && (ai == null || ai.length < j)) goto _L2; else goto _L1
_L1:
        int k = af[0].length;
        vertexCount = i;
        i = vertexCount;
        vertices = new float[i][k];
        for(i = 0; i < vertexCount; i++)
            PApplet.arrayCopy(af[i], vertices[i]);

        vertexCodeCount = j;
        if(vertexCodeCount > 0)
        {
            vertexCodes = new int[vertexCodeCount];
            PApplet.arrayCopy(ai, vertexCodes, vertexCodeCount);
        }
        if(true) goto _L2; else goto _L3
_L3:
    }

    public void setShininess(float f)
    {
        int i = 0;
        if(!openShape) goto _L2; else goto _L1
_L1:
        PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
            "setShininess()"
        });
_L4:
        return;
_L2:
        shininess = f;
        if(vertices != null)
            while(i < vertices.length) 
            {
                setShininess(i, f);
                i++;
            }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void setShininess(int i, float f)
    {
        if(openShape)
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setShininess()"
            });
        else
        if(vertices == null || i >= vertices.length)
            PGraphics.showWarning((new StringBuilder()).append("%1$s vertex index does not exist (").append(i).append(")").toString(), new Object[] {
                "setShininess()"
            });
        else
            vertices[i][31] = f;
    }

    public void setSpecular(int i)
    {
        int j = 0;
        if(!openShape) goto _L2; else goto _L1
_L1:
        PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
            "setSpecular()"
        });
_L4:
        return;
_L2:
        specularColor = i;
        if(vertices != null)
            while(j < vertices.length) 
            {
                setSpecular(j, i);
                j++;
            }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void setSpecular(int i, int j)
    {
        if(openShape)
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setSpecular()"
            });
        else
        if(vertices == null || i >= vertices.length)
        {
            PGraphics.showWarning((new StringBuilder()).append("%1$s vertex index does not exist (").append(i).append(")").toString(), new Object[] {
                "setSpecular()"
            });
        } else
        {
            vertices[i][28] = (float)(j >> 16 & 0xff) / 255F;
            vertices[i][29] = (float)(j >> 8 & 0xff) / 255F;
            vertices[i][30] = (float)(j >> 0 & 0xff) / 255F;
        }
    }

    public void setStroke(int i)
    {
        int j = 0;
        if(!openShape) goto _L2; else goto _L1
_L1:
        PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
            "setStroke()"
        });
_L4:
        return;
_L2:
        strokeColor = i;
        if(vertices != null)
            while(j < vertices.length) 
            {
                setStroke(j, i);
                j++;
            }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void setStroke(int i, int j)
    {
        if(openShape)
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setStroke()"
            });
        else
        if(vertices == null || i >= vertices.length)
        {
            PGraphics.showWarning((new StringBuilder()).append("%1$s vertex index does not exist (").append(i).append(")").toString(), new Object[] {
                "setStroke()"
            });
        } else
        {
            vertices[i][16] = (float)(j >> 24 & 0xff) / 255F;
            vertices[i][13] = (float)(j >> 16 & 0xff) / 255F;
            vertices[i][14] = (float)(j >> 8 & 0xff) / 255F;
            vertices[i][15] = (float)(j >> 0 & 0xff) / 255F;
        }
    }

    public void setStroke(boolean flag)
    {
        if(openShape)
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setStroke()"
            });
        else
            stroke = flag;
    }

    public void setStrokeCap(int i)
    {
        if(openShape)
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setStrokeCap()"
            });
        else
            strokeCap = i;
    }

    public void setStrokeJoin(int i)
    {
        if(openShape)
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setStrokeJoin()"
            });
        else
            strokeJoin = i;
    }

    public void setStrokeWeight(float f)
    {
        int i = 0;
        if(!openShape) goto _L2; else goto _L1
_L1:
        PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
            "setStrokeWeight()"
        });
_L4:
        return;
_L2:
        strokeWeight = f;
        if(vertices != null)
            while(i < vertices.length) 
            {
                setStrokeWeight(i, f);
                i++;
            }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void setStrokeWeight(int i, float f)
    {
        if(openShape)
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setStrokeWeight()"
            });
        else
        if(vertices == null || i >= vertices.length)
            PGraphics.showWarning((new StringBuilder()).append("%1$s vertex index does not exist (").append(i).append(")").toString(), new Object[] {
                "setStrokeWeight()"
            });
        else
            vertices[i][17] = f;
    }

    public void setTexture(PImage pimage)
    {
        if(openShape)
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setTexture()"
            });
        else
            image = pimage;
    }

    public void setTextureMode(int i)
    {
        if(openShape)
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setTextureMode()"
            });
        else
            textureMode = i;
    }

    public void setTextureUV(int i, float f, float f1)
    {
        if(openShape)
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setTextureUV()"
            });
        else
        if(vertices == null || i >= vertices.length)
        {
            PGraphics.showWarning((new StringBuilder()).append("%1$s vertex index does not exist (").append(i).append(")").toString(), new Object[] {
                "setTextureUV()"
            });
        } else
        {
            vertices[i][7] = f;
            vertices[i][8] = f1;
        }
    }

    public void setTint(int i)
    {
        int j = 0;
        if(!openShape) goto _L2; else goto _L1
_L1:
        PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
            "setTint()"
        });
_L4:
        return;
_L2:
        tintColor = i;
        if(vertices != null)
            while(j < vertices.length) 
            {
                setFill(j, i);
                j++;
            }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void setTint(int i, int j)
    {
        if(!openShape) goto _L2; else goto _L1
_L1:
        PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
            "setTint()"
        });
_L4:
        return;
_L2:
        if(vertices == null || i >= vertices.length)
            PGraphics.showWarning((new StringBuilder()).append("%1$s vertex index does not exist (").append(i).append(")").toString(), new Object[] {
                "setTint()"
            });
        else
        if(image != null)
        {
            vertices[i][6] = (float)(j >> 24 & 0xff) / 255F;
            vertices[i][3] = (float)(j >> 16 & 0xff) / 255F;
            vertices[i][4] = (float)(j >> 8 & 0xff) / 255F;
            vertices[i][5] = (float)(j >> 0 & 0xff) / 255F;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void setTint(boolean flag)
    {
        if(openShape)
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setTint()"
            });
        else
            tint = flag;
    }

    public void setVertex(int i, float f, float f1)
    {
        if(openShape)
        {
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setVertex()"
            });
        } else
        {
            vertices[i][0] = f;
            vertices[i][1] = f1;
        }
    }

    public void setVertex(int i, float f, float f1, float f2)
    {
        if(openShape)
        {
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setVertex()"
            });
        } else
        {
            vertices[i][0] = f;
            vertices[i][1] = f1;
            vertices[i][2] = f2;
        }
    }

    public void setVertex(int i, PVector pvector)
    {
        if(!openShape) goto _L2; else goto _L1
_L1:
        PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
            "setVertex()"
        });
_L4:
        return;
_L2:
        vertices[i][0] = pvector.x;
        vertices[i][1] = pvector.y;
        if(vertices[i].length <= 2)
            break; /* Loop/switch isn't completed */
        vertices[i][2] = pvector.z;
        if(true) goto _L4; else goto _L3
_L3:
        if(pvector.z == 0.0F || pvector.z != pvector.z) goto _L4; else goto _L5
_L5:
        throw new IllegalArgumentException("Cannot set a z-coordinate on a 2D shape");
    }

    public void setVisible(boolean flag)
    {
        visible = flag;
    }

    public void shininess(float f)
    {
        if(!openShape)
            PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
                "shininess()"
            });
        else
            shininess = f;
    }

    protected void solid(boolean flag)
    {
    }

    public void specular(float f)
    {
        if(!openShape)
        {
            PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
                "specular()"
            });
        } else
        {
            colorCalc(f);
            specularColor = calcColor;
        }
    }

    public void specular(float f, float f1, float f2)
    {
        if(!openShape)
        {
            PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
                "specular()"
            });
        } else
        {
            colorCalc(f, f1, f2);
            specularColor = calcColor;
        }
    }

    public void specular(int i)
    {
        if(!openShape)
        {
            PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
                "specular()"
            });
        } else
        {
            colorCalc(i);
            specularColor = calcColor;
        }
    }

    public void stroke(float f)
    {
        if(!openShape)
        {
            PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
                "stroke()"
            });
        } else
        {
            stroke = true;
            colorCalc(f);
            strokeColor = calcColor;
        }
    }

    public void stroke(float f, float f1)
    {
        if(!openShape)
        {
            PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
                "stroke()"
            });
        } else
        {
            stroke = true;
            colorCalc(f, f1);
            strokeColor = calcColor;
        }
    }

    public void stroke(float f, float f1, float f2)
    {
        if(!openShape)
        {
            PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
                "stroke()"
            });
        } else
        {
            stroke = true;
            colorCalc(f, f1, f2);
            strokeColor = calcColor;
        }
    }

    public void stroke(float f, float f1, float f2, float f3)
    {
        if(!openShape)
        {
            PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
                "stroke()"
            });
        } else
        {
            stroke = true;
            colorCalc(f, f1, f2, f3);
            strokeColor = calcColor;
        }
    }

    public void stroke(int i)
    {
        if(!openShape)
        {
            PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
                "stroke()"
            });
        } else
        {
            stroke = true;
            colorCalc(i);
            strokeColor = calcColor;
        }
    }

    public void stroke(int i, float f)
    {
        if(!openShape)
        {
            PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
                "stroke()"
            });
        } else
        {
            stroke = true;
            colorCalc(i, f);
            strokeColor = calcColor;
        }
    }

    public void strokeCap(int i)
    {
        if(!openShape)
            PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
                "strokeCap()"
            });
        else
            strokeCap = i;
    }

    public void strokeJoin(int i)
    {
        if(!openShape)
            PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
                "strokeJoin()"
            });
        else
            strokeJoin = i;
    }

    public void strokeWeight(float f)
    {
        if(!openShape)
            PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
                "strokeWeight()"
            });
        else
            strokeWeight = f;
    }

    protected void styles(PGraphics pgraphics)
    {
        if(stroke)
        {
            pgraphics.stroke(strokeColor);
            pgraphics.strokeWeight(strokeWeight);
            pgraphics.strokeCap(strokeCap);
            pgraphics.strokeJoin(strokeJoin);
        } else
        {
            pgraphics.noStroke();
        }
        if(fill)
            pgraphics.fill(fillColor);
        else
            pgraphics.noFill();
    }

    public void texture(PImage pimage)
    {
        if(!openShape)
            PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
                "texture()"
            });
        else
            image = pimage;
    }

    public void textureMode(int i)
    {
        if(!openShape)
            PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
                "textureMode()"
            });
        else
            textureMode = i;
    }

    public void tint(float f)
    {
        if(!openShape)
        {
            PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
                "tint()"
            });
        } else
        {
            tint = true;
            colorCalc(f);
            tintColor = calcColor;
        }
    }

    public void tint(float f, float f1)
    {
        if(!openShape)
        {
            PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
                "tint()"
            });
        } else
        {
            tint = true;
            colorCalc(f, f1);
            tintColor = calcColor;
        }
    }

    public void tint(float f, float f1, float f2)
    {
        if(!openShape)
        {
            PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
                "tint()"
            });
        } else
        {
            tint = true;
            colorCalc(f, f1, f2);
            tintColor = calcColor;
        }
    }

    public void tint(float f, float f1, float f2, float f3)
    {
        if(!openShape)
        {
            PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
                "tint()"
            });
        } else
        {
            tint = true;
            colorCalc(f, f1, f2, f3);
            tintColor = calcColor;
        }
    }

    public void tint(int i)
    {
        if(!openShape)
        {
            PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
                "tint()"
            });
        } else
        {
            tint = true;
            colorCalc(i);
            tintColor = calcColor;
        }
    }

    public void tint(int i, float f)
    {
        if(!openShape)
        {
            PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
                "tint()"
            });
        } else
        {
            tint = true;
            colorCalc(i, f);
            tintColor = calcColor;
        }
    }

    public void translate(float f, float f1)
    {
        checkMatrix(2);
        matrix.translate(f, f1);
    }

    public void translate(float f, float f1, float f2)
    {
        checkMatrix(3);
        matrix.translate(f, f1, f2);
    }

    public void vertex(float f, float f1)
    {
        Object obj;
        int i;
        if(vertices == null)
            vertices = new float[10][2];
        else
        if(vertices.length == vertexCount)
            vertices = (float[][])PApplet.expand(vertices);
        obj = vertices;
        i = vertexCount;
        vertexCount = i + 1;
        obj[i] = (new float[] {
            f, f1
        });
        if(vertexCodes == null)
            vertexCodes = new int[10];
        else
        if(vertexCodes.length == vertexCodeCount)
            vertexCodes = PApplet.expand(vertexCodes);
        obj = vertexCodes;
        i = vertexCodeCount;
        vertexCodeCount = i + 1;
        obj[i] = 0;
        if(f > width)
            width = f;
        if(f1 > height)
            height = f1;
    }

    public void vertex(float f, float f1, float f2)
    {
        vertex(f, f1);
    }

    public void vertex(float f, float f1, float f2, float f3)
    {
    }

    public void vertex(float f, float f1, float f2, float f3, float f4)
    {
    }

    public static final int GEOMETRY = 3;
    public static final String INSIDE_BEGIN_END_ERROR = "%1$s can only be called outside beginShape() and endShape()";
    public static final String NOT_A_SIMPLE_VERTEX = "%1$s can not be called on quadratic or bezier vertices";
    public static final String NO_SUCH_VERTEX_ERROR = "%1$s vertex index does not exist";
    public static final String NO_VERTICES_ERROR = "getVertexCount() only works with PATH or GEOMETRY shapes";
    public static final String OUTSIDE_BEGIN_END_ERROR = "%1$s can only be called between beginShape() and endShape()";
    public static final int PATH = 2;
    public static final int PRIMITIVE = 1;
    protected int ambientColor;
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
    protected int childCount;
    protected PShape children[];
    protected boolean close;
    public int colorMode;
    public float colorModeA;
    boolean colorModeDefault;
    boolean colorModeScale;
    public float colorModeX;
    public float colorModeY;
    public float colorModeZ;
    public float depth;
    protected int ellipseMode;
    protected int emissiveColor;
    protected int family;
    protected boolean fill;
    protected int fillColor;
    PGraphics g;
    public float height;
    protected PImage image;
    protected boolean is3D;
    protected int kind;
    protected PMatrix matrix;
    protected String name;
    protected Map nameTable;
    protected boolean openContour;
    protected boolean openShape;
    protected float params[];
    protected PShape parent;
    protected boolean perVertexStyles;
    protected int rectMode;
    protected boolean setAmbient;
    protected float shininess;
    protected int specularColor;
    protected int sphereDetailU;
    protected int sphereDetailV;
    protected boolean stroke;
    protected int strokeCap;
    protected int strokeColor;
    protected int strokeJoin;
    protected float strokeWeight;
    protected boolean style;
    protected int textureMode;
    protected boolean tint;
    protected int tintColor;
    protected int vertexCodeCount;
    protected int vertexCodes[];
    protected int vertexCount;
    protected float vertices[][];
    protected boolean visible;
    public float width;
}
