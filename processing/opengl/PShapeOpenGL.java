// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.opengl;

import java.nio.*;
import java.util.*;
import processing.core.*;

// Referenced classes of package processing.opengl:
//            PGL, PGraphicsOpenGL, VertexBuffer, PShader, 
//            Texture

public class PShapeOpenGL extends PShape
{

    PShapeOpenGL()
    {
        glUsage = PGL.STATIC_DRAW;
        needBufferInit = false;
        solid = true;
        breakShape = false;
        shapeCreated = false;
    }

    public PShapeOpenGL(PGraphicsOpenGL pgraphicsopengl, int i)
    {
        glUsage = PGL.STATIC_DRAW;
        needBufferInit = false;
        solid = true;
        breakShape = false;
        shapeCreated = false;
        pg = pgraphicsopengl;
        family = i;
        pgl = pgraphicsopengl.pgl;
        context = pgl.createEmptyContext();
        bufPolyVertex = null;
        bufPolyColor = null;
        bufPolyNormal = null;
        bufPolyTexcoord = null;
        bufPolyAmbient = null;
        bufPolySpecular = null;
        bufPolyEmissive = null;
        bufPolyShininess = null;
        bufPolyIndex = null;
        bufLineVertex = null;
        bufLineColor = null;
        bufLineAttrib = null;
        bufLineIndex = null;
        bufPointVertex = null;
        bufPointColor = null;
        bufPointAttrib = null;
        bufPointIndex = null;
        tessellator = pgraphicsopengl.tessellator;
        root = this;
        parent = null;
        tessellated = false;
        if(i == 3 || i == 1 || i == 2)
        {
            polyAttribs = PGraphicsOpenGL.newAttributeMap();
            inGeo = PGraphicsOpenGL.newInGeometry(pgraphicsopengl, polyAttribs, 1);
        }
        textureMode = pgraphicsopengl.textureMode;
        colorMode(pgraphicsopengl.colorMode, pgraphicsopengl.colorModeX, pgraphicsopengl.colorModeY, pgraphicsopengl.colorModeZ, pgraphicsopengl.colorModeA);
        fill = pgraphicsopengl.fill;
        fillColor = pgraphicsopengl.fillColor;
        stroke = pgraphicsopengl.stroke;
        strokeColor = pgraphicsopengl.strokeColor;
        strokeWeight = pgraphicsopengl.strokeWeight;
        strokeCap = pgraphicsopengl.strokeCap;
        strokeJoin = pgraphicsopengl.strokeJoin;
        tint = pgraphicsopengl.tint;
        tintColor = pgraphicsopengl.tintColor;
        setAmbient = pgraphicsopengl.setAmbient;
        ambientColor = pgraphicsopengl.ambientColor;
        specularColor = pgraphicsopengl.specularColor;
        emissiveColor = pgraphicsopengl.emissiveColor;
        shininess = pgraphicsopengl.shininess;
        sphereDetailU = pgraphicsopengl.sphereDetailU;
        sphereDetailV = pgraphicsopengl.sphereDetailV;
        bezierDetail = pgraphicsopengl.bezierDetail;
        curveDetail = pgraphicsopengl.curveDetail;
        curveTightness = pgraphicsopengl.curveTightness;
        rectMode = pgraphicsopengl.rectMode;
        ellipseMode = pgraphicsopengl.ellipseMode;
        normalY = 0.0F;
        normalX = 0.0F;
        normalZ = 1.0F;
        normalMode = 0;
        breakShape = false;
        if(i == 0)
            shapeCreated = true;
        perVertexStyles = true;
    }

    public transient PShapeOpenGL(PGraphicsOpenGL pgraphicsopengl, int i, float af[])
    {
        this(pgraphicsopengl, 1);
        setKind(i);
        setParams(af);
    }

    public static void copyGroup(PGraphicsOpenGL pgraphicsopengl, PShape pshape, PShape pshape1)
    {
        copyMatrix(pshape, pshape1);
        copyStyles(pshape, pshape1);
        copyImage(pshape, pshape1);
        for(int i = 0; i < pshape.getChildCount(); i++)
            pshape1.addChild(createShape(pgraphicsopengl, pshape.getChild(i)));

    }

    public static PShapeOpenGL createShape(PGraphicsOpenGL pgraphicsopengl, PShape pshape)
    {
        PShapeOpenGL pshapeopengl = null;
        if(pshape.getFamily() != 0) goto _L2; else goto _L1
_L1:
        pshapeopengl = (PShapeOpenGL)pgraphicsopengl.createShapeFamily(0);
        copyGroup(pgraphicsopengl, pshape, pshapeopengl);
_L4:
        pshapeopengl.setName(pshape.getName());
        pshapeopengl.width = pshape.width;
        pshapeopengl.height = pshape.height;
        pshapeopengl.depth = pshape.depth;
        return pshapeopengl;
_L2:
        if(pshape.getFamily() == 1)
        {
            pshapeopengl = (PShapeOpenGL)pgraphicsopengl.createShapePrimitive(pshape.getKind(), pshape.getParams());
            PShape.copyPrimitive(pshape, pshapeopengl);
        } else
        if(pshape.getFamily() == 3)
        {
            pshapeopengl = (PShapeOpenGL)pgraphicsopengl.createShapeFamily(3);
            PShape.copyGeometry(pshape, pshapeopengl);
        } else
        if(pshape.getFamily() == 2)
        {
            pshapeopengl = (PShapeOpenGL)pgraphicsopengl.createShapeFamily(2);
            PShape.copyPath(pshape, pshapeopengl);
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void addChild(PShape pshape)
    {
        if(!(pshape instanceof PShapeOpenGL)) goto _L2; else goto _L1
_L1:
        if(family != 0) goto _L4; else goto _L3
_L3:
        PShapeOpenGL pshapeopengl;
        pshapeopengl = (PShapeOpenGL)pshape;
        super.addChild(pshapeopengl);
        pshapeopengl.updateRoot(root);
        markForTessellation();
        if(pshapeopengl.family != 0) goto _L6; else goto _L5
_L5:
        if(pshapeopengl.textures != null)
            for(pshape = pshapeopengl.textures.iterator(); pshape.hasNext(); addTexture((PImage)pshape.next()));
        else
            untexChild(true);
        if(pshapeopengl.strokedTexture)
            strokedTexture(true);
_L8:
        return;
_L6:
        if(pshapeopengl.image != null)
        {
            addTexture(pshapeopengl.image);
            if(pshapeopengl.stroke)
                strokedTexture(true);
        } else
        {
            untexChild(true);
        }
        continue; /* Loop/switch isn't completed */
_L4:
        PGraphics.showWarning("Cannot add child shape to non-group shape.");
        continue; /* Loop/switch isn't completed */
_L2:
        PGraphics.showWarning("Shape must be OpenGL to be added to the group.");
        if(true) goto _L8; else goto _L7
_L7:
    }

    public void addChild(PShape pshape, int i)
    {
        if(!(pshape instanceof PShapeOpenGL)) goto _L2; else goto _L1
_L1:
        if(family != 0) goto _L4; else goto _L3
_L3:
        PShapeOpenGL pshapeopengl;
        pshapeopengl = (PShapeOpenGL)pshape;
        super.addChild(pshapeopengl, i);
        pshapeopengl.updateRoot(root);
        markForTessellation();
        if(pshapeopengl.family != 0) goto _L6; else goto _L5
_L5:
        if(pshapeopengl.textures != null)
            for(pshape = pshapeopengl.textures.iterator(); pshape.hasNext(); addTexture((PImage)pshape.next()));
        else
            untexChild(true);
        if(pshapeopengl.strokedTexture)
            strokedTexture(true);
_L8:
        return;
_L6:
        if(pshapeopengl.image != null)
        {
            addTexture(pshapeopengl.image);
            if(pshapeopengl.stroke)
                strokedTexture(true);
        } else
        {
            untexChild(true);
        }
        continue; /* Loop/switch isn't completed */
_L4:
        PGraphics.showWarning("Cannot add child shape to non-group shape.");
        continue; /* Loop/switch isn't completed */
_L2:
        PGraphics.showWarning("Shape must be OpenGL to be added to the group.");
        if(true) goto _L8; else goto _L7
_L7:
    }

    protected void addTexture(PImage pimage)
    {
        if(textures == null)
            textures = new HashSet();
        textures.add(pimage);
        if(parent != null)
            ((PShapeOpenGL)parent).addTexture(pimage);
    }

    protected void aggregate()
    {
        if(root == this && parent == null)
        {
            polyIndexOffset = 0;
            polyVertexOffset = 0;
            polyVertexAbs = 0;
            polyVertexRel = 0;
            lineIndexOffset = 0;
            lineVertexOffset = 0;
            lineVertexAbs = 0;
            lineVertexRel = 0;
            pointIndexOffset = 0;
            pointVertexOffset = 0;
            pointVertexAbs = 0;
            pointVertexRel = 0;
            aggregateImpl();
        }
    }

    protected void aggregateImpl()
    {
        boolean flag = true;
        if(family == 0)
        {
            hasPolys = false;
            hasLines = false;
            hasPoints = false;
            for(int i = 0; i < childCount; i++)
            {
                PShapeOpenGL pshapeopengl = (PShapeOpenGL)children[i];
                pshapeopengl.aggregateImpl();
                hasPolys = hasPolys | pshapeopengl.hasPolys;
                hasLines = hasLines | pshapeopengl.hasLines;
                boolean flag1 = hasPoints;
                hasPoints = pshapeopengl.hasPoints | flag1;
            }

        } else
        {
            boolean flag2;
            if(-1 < firstPolyIndexCache && -1 < lastPolyIndexCache)
                flag2 = true;
            else
                flag2 = false;
            hasPolys = flag2;
            if(-1 < firstLineIndexCache && -1 < lastLineIndexCache)
                flag2 = true;
            else
                flag2 = false;
            hasLines = flag2;
            if(-1 < firstPointIndexCache && -1 < lastPointIndexCache)
                flag2 = flag;
            else
                flag2 = false;
            hasPoints = flag2;
        }
        if(hasPolys)
            updatePolyIndexCache();
        if(is3D())
        {
            if(hasLines)
                updateLineIndexCache();
            if(hasPoints)
                updatePointIndexCache();
        }
        if(matrix != null)
        {
            if(hasPolys)
                tessGeo.applyMatrixOnPolyGeometry(matrix, firstPolyVertex, lastPolyVertex);
            if(is3D())
            {
                if(hasLines)
                    tessGeo.applyMatrixOnLineGeometry(matrix, firstLineVertex, lastLineVertex);
                if(hasPoints)
                    tessGeo.applyMatrixOnPointGeometry(matrix, firstPointVertex, lastPointVertex);
            }
        }
    }

    public void applyMatrix(float f, float f1, float f2, float f3, float f4, float f5)
    {
        transform(3, new float[] {
            f, f1, f2, f3, f4, f5
        });
    }

    public void applyMatrix(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8, float f9, float f10, float f11, float f12, float f13, 
            float f14, float f15)
    {
        transform(3, new float[] {
            f, f1, f2, f3, f4, f5, f6, f7, f8, f9, 
            f10, f11, f12, f13, f14, f15
        });
    }

    public void applyMatrix(PMatrix2D pmatrix2d)
    {
        transform(3, new float[] {
            pmatrix2d.m00, pmatrix2d.m01, pmatrix2d.m02, pmatrix2d.m10, pmatrix2d.m11, pmatrix2d.m12
        });
    }

    protected void applyMatrixImpl(PMatrix pmatrix)
    {
        if(hasPolys)
        {
            tessGeo.applyMatrixOnPolyGeometry(pmatrix, firstPolyVertex, lastPolyVertex);
            root.setModifiedPolyVertices(firstPolyVertex, lastPolyVertex);
            root.setModifiedPolyNormals(firstPolyVertex, lastPolyVertex);
            Iterator iterator = polyAttribs.values().iterator();
            do
            {
                if(!iterator.hasNext())
                    break;
                PGraphicsOpenGL.VertexAttribute vertexattribute = (PGraphicsOpenGL.VertexAttribute)iterator.next();
                if(vertexattribute.isPosition() || vertexattribute.isNormal())
                    root.setModifiedPolyAttrib(vertexattribute, firstPolyVertex, lastPolyVertex);
            } while(true);
        }
        if(is3D())
        {
            if(hasLines)
            {
                tessGeo.applyMatrixOnLineGeometry(pmatrix, firstLineVertex, lastLineVertex);
                root.setModifiedLineVertices(firstLineVertex, lastLineVertex);
                root.setModifiedLineAttributes(firstLineVertex, lastLineVertex);
            }
            if(hasPoints)
            {
                tessGeo.applyMatrixOnPointGeometry(pmatrix, firstPointVertex, lastPointVertex);
                root.setModifiedPointVertices(firstPointVertex, lastPointVertex);
                root.setModifiedPointAttributes(firstPointVertex, lastPointVertex);
            }
        }
    }

    public transient void attrib(String s, float af[])
    {
        s = attribImpl(s, 3, PGL.FLOAT, af.length);
        if(s != null)
            s.set(af);
    }

    public transient void attrib(String s, int ai[])
    {
        s = attribImpl(s, 3, PGL.INT, ai.length);
        if(s != null)
            s.set(ai);
    }

    public transient void attrib(String s, boolean aflag[])
    {
        s = attribImpl(s, 3, PGL.BOOL, aflag.length);
        if(s != null)
            s.set(aflag);
    }

    public void attribColor(String s, int i)
    {
        s = attribImpl(s, 2, PGL.INT, 1);
        if(s != null)
            s.set(new int[] {
                i
            });
    }

    protected PGraphicsOpenGL.VertexAttribute attribImpl(String s, int i, int j, int k)
    {
        if(4 >= k) goto _L2; else goto _L1
_L1:
        PGraphics.showWarning("Vertex attributes cannot have more than 4 values");
        s = null;
_L4:
        return s;
_L2:
        PGraphicsOpenGL.VertexAttribute vertexattribute = (PGraphicsOpenGL.VertexAttribute)polyAttribs.get(s);
        PGraphicsOpenGL.VertexAttribute vertexattribute1 = vertexattribute;
        if(vertexattribute == null)
        {
            vertexattribute1 = new PGraphicsOpenGL.VertexAttribute(pg, s, i, j, k);
            polyAttribs.put(s, vertexattribute1);
            inGeo.initAttrib(vertexattribute1);
        }
        if(vertexattribute1.kind != i)
        {
            PGraphics.showWarning("The attribute kind cannot be changed after creation");
            s = null;
        } else
        if(vertexattribute1.type != j)
        {
            PGraphics.showWarning("The attribute type cannot be changed after creation");
            s = null;
        } else
        {
            s = vertexattribute1;
            if(vertexattribute1.size != k)
            {
                PGraphics.showWarning("New value for vertex attribute has wrong number of values");
                s = null;
            }
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void attribNormal(String s, float f, float f1, float f2)
    {
        s = attribImpl(s, 1, PGL.FLOAT, 3);
        if(s != null)
            s.set(f, f1, f2);
    }

    public void attribPosition(String s, float f, float f1, float f2)
    {
        s = attribImpl(s, 0, PGL.FLOAT, 3);
        if(s != null)
            s.set(f, f1, f2);
    }

    protected void beginContourImpl()
    {
        breakShape = true;
    }

    public void bezierDetail(int i)
    {
        bezierDetail = i;
        if(inGeo.codeCount > 0)
            markForTessellation();
    }

    public void bezierVertex(float f, float f1, float f2, float f3, float f4, float f5)
    {
        bezierVertexImpl(f, f1, 0.0F, f2, f3, 0.0F, f4, f5, 0.0F);
    }

    public void bezierVertex(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8)
    {
        bezierVertexImpl(f, f1, f2, f3, f4, f5, f6, f7, f8);
    }

    protected void bezierVertexImpl(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8)
    {
        inGeo.setMaterial(fillColor, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininess);
        inGeo.setNormal(normalX, normalY, normalZ);
        inGeo.addBezierVertex(f, f1, f2, f3, f4, f5, f6, f7, f8, vertexBreak());
    }

    protected void collectPolyAttribs()
    {
        PGraphicsOpenGL.AttributeMap attributemap = root.polyAttribs;
        if(family == 0)
        {
            for(int i = 0; i < childCount; i++)
                ((PShapeOpenGL)children[i]).collectPolyAttribs();

        } else
        {
            for(int j = 0; j < polyAttribs.size(); j++)
            {
                PGraphicsOpenGL.VertexAttribute vertexattribute = polyAttribs.get(j);
                tessGeo.initAttrib(vertexattribute);
                if(attributemap.containsKey(vertexattribute.name))
                {
                    if(((PGraphicsOpenGL.VertexAttribute)attributemap.get(vertexattribute.name)).diff(vertexattribute))
                        throw new RuntimeException("Children shapes cannot have different attributes with same name");
                } else
                {
                    attributemap.put(vertexattribute.name, vertexattribute);
                }
            }

        }
    }

    public boolean contains(float f, float f1)
    {
        boolean flag;
        if(family == 2)
        {
            int i = inGeo.vertexCount - 1;
            int j = 0;
            flag = false;
            while(j < inGeo.vertexCount) 
            {
                int k;
                boolean flag1;
                boolean flag2;
                if(inGeo.vertices[j * 3 + 1] > f1)
                    k = 1;
                else
                    k = 0;
                if(inGeo.vertices[i * 3 + 1] > f1)
                    flag1 = true;
                else
                    flag1 = false;
                flag2 = flag;
                if(k != flag1)
                {
                    flag2 = flag;
                    if(f < ((inGeo.vertices[i * 3] - inGeo.vertices[j * 3]) * (f1 - inGeo.vertices[j * 3 + 1])) / (inGeo.vertices[i * 3 + 1] - inGeo.vertices[j * 3 + 1]) + inGeo.vertices[j * 3])
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

    protected boolean contextIsOutdated()
    {
        boolean flag;
        if(!pgl.contextIsCurrent(context))
            flag = true;
        else
            flag = false;
        if(flag)
        {
            bufPolyVertex.dispose();
            bufPolyColor.dispose();
            bufPolyNormal.dispose();
            bufPolyTexcoord.dispose();
            bufPolyAmbient.dispose();
            bufPolySpecular.dispose();
            bufPolyEmissive.dispose();
            bufPolyShininess.dispose();
            for(Iterator iterator = polyAttribs.values().iterator(); iterator.hasNext(); ((PGraphicsOpenGL.VertexAttribute)iterator.next()).buf.dispose());
            bufPolyIndex.dispose();
            bufLineVertex.dispose();
            bufLineColor.dispose();
            bufLineAttrib.dispose();
            bufLineIndex.dispose();
            bufPointVertex.dispose();
            bufPointColor.dispose();
            bufPointAttrib.dispose();
            bufPointIndex.dispose();
        }
        return flag;
    }

    protected void copyLineAttributes(int i, int j)
    {
        tessGeo.updateLineDirectionsBuffer(i, j);
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufLineAttrib.glId);
        tessGeo.lineDirectionsBuffer.position(i * 4);
        pgl.bufferSubData(PGL.ARRAY_BUFFER, i * 4 * PGL.SIZEOF_FLOAT, j * 4 * PGL.SIZEOF_FLOAT, tessGeo.lineDirectionsBuffer);
        tessGeo.lineDirectionsBuffer.rewind();
        pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    protected void copyLineColors(int i, int j)
    {
        tessGeo.updateLineColorsBuffer(i, j);
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufLineColor.glId);
        tessGeo.lineColorsBuffer.position(i);
        pgl.bufferSubData(PGL.ARRAY_BUFFER, PGL.SIZEOF_INT * i, PGL.SIZEOF_INT * j, tessGeo.lineColorsBuffer);
        tessGeo.lineColorsBuffer.rewind();
        pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    protected void copyLineVertices(int i, int j)
    {
        tessGeo.updateLineVerticesBuffer(i, j);
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufLineVertex.glId);
        tessGeo.lineVerticesBuffer.position(i * 4);
        pgl.bufferSubData(PGL.ARRAY_BUFFER, i * 4 * PGL.SIZEOF_FLOAT, j * 4 * PGL.SIZEOF_FLOAT, tessGeo.lineVerticesBuffer);
        tessGeo.lineVerticesBuffer.rewind();
        pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    protected void copyPointAttributes(int i, int j)
    {
        tessGeo.updatePointOffsetsBuffer(i, j);
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPointAttrib.glId);
        tessGeo.pointOffsetsBuffer.position(i * 2);
        pgl.bufferSubData(PGL.ARRAY_BUFFER, i * 2 * PGL.SIZEOF_FLOAT, j * 2 * PGL.SIZEOF_FLOAT, tessGeo.pointOffsetsBuffer);
        tessGeo.pointOffsetsBuffer.rewind();
        pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    protected void copyPointColors(int i, int j)
    {
        tessGeo.updatePointColorsBuffer(i, j);
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPointColor.glId);
        tessGeo.pointColorsBuffer.position(i);
        pgl.bufferSubData(PGL.ARRAY_BUFFER, PGL.SIZEOF_INT * i, PGL.SIZEOF_INT * j, tessGeo.pointColorsBuffer);
        tessGeo.pointColorsBuffer.rewind();
        pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    protected void copyPointVertices(int i, int j)
    {
        tessGeo.updatePointVerticesBuffer(i, j);
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPointVertex.glId);
        tessGeo.pointVerticesBuffer.position(i * 4);
        pgl.bufferSubData(PGL.ARRAY_BUFFER, i * 4 * PGL.SIZEOF_FLOAT, j * 4 * PGL.SIZEOF_FLOAT, tessGeo.pointVerticesBuffer);
        tessGeo.pointVerticesBuffer.rewind();
        pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    protected void copyPolyAmbient(int i, int j)
    {
        tessGeo.updatePolyAmbientBuffer(i, j);
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPolyAmbient.glId);
        tessGeo.polyAmbientBuffer.position(i);
        pgl.bufferSubData(PGL.ARRAY_BUFFER, PGL.SIZEOF_INT * i, PGL.SIZEOF_INT * j, tessGeo.polyAmbientBuffer);
        tessGeo.polyAmbientBuffer.rewind();
        pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    protected void copyPolyAttrib(PGraphicsOpenGL.VertexAttribute vertexattribute, int i, int j)
    {
        tessGeo.updateAttribBuffer(vertexattribute.name, i, j);
        pgl.bindBuffer(PGL.ARRAY_BUFFER, vertexattribute.buf.glId);
        Buffer buffer = (Buffer)tessGeo.polyAttribBuffers.get(vertexattribute.name);
        buffer.position(vertexattribute.size * i);
        pgl.bufferSubData(PGL.ARRAY_BUFFER, vertexattribute.sizeInBytes(i), vertexattribute.sizeInBytes(j), buffer);
        buffer.rewind();
        pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    protected void copyPolyColors(int i, int j)
    {
        tessGeo.updatePolyColorsBuffer(i, j);
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPolyColor.glId);
        tessGeo.polyColorsBuffer.position(i);
        pgl.bufferSubData(PGL.ARRAY_BUFFER, PGL.SIZEOF_INT * i, PGL.SIZEOF_INT * j, tessGeo.polyColorsBuffer);
        tessGeo.polyColorsBuffer.rewind();
        pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    protected void copyPolyEmissive(int i, int j)
    {
        tessGeo.updatePolyEmissiveBuffer(i, j);
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPolyEmissive.glId);
        tessGeo.polyEmissiveBuffer.position(i);
        pgl.bufferSubData(PGL.ARRAY_BUFFER, PGL.SIZEOF_INT * i, PGL.SIZEOF_INT * j, tessGeo.polyEmissiveBuffer);
        tessGeo.polyEmissiveBuffer.rewind();
        pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    protected void copyPolyNormals(int i, int j)
    {
        tessGeo.updatePolyNormalsBuffer(i, j);
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPolyNormal.glId);
        tessGeo.polyNormalsBuffer.position(i * 3);
        pgl.bufferSubData(PGL.ARRAY_BUFFER, i * 3 * PGL.SIZEOF_FLOAT, j * 3 * PGL.SIZEOF_FLOAT, tessGeo.polyNormalsBuffer);
        tessGeo.polyNormalsBuffer.rewind();
        pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    protected void copyPolyShininess(int i, int j)
    {
        tessGeo.updatePolyShininessBuffer(i, j);
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPolyShininess.glId);
        tessGeo.polyShininessBuffer.position(i);
        pgl.bufferSubData(PGL.ARRAY_BUFFER, PGL.SIZEOF_FLOAT * i, PGL.SIZEOF_FLOAT * j, tessGeo.polyShininessBuffer);
        tessGeo.polyShininessBuffer.rewind();
        pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    protected void copyPolySpecular(int i, int j)
    {
        tessGeo.updatePolySpecularBuffer(i, j);
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPolySpecular.glId);
        tessGeo.polySpecularBuffer.position(i);
        pgl.bufferSubData(PGL.ARRAY_BUFFER, PGL.SIZEOF_INT * i, PGL.SIZEOF_INT * j, tessGeo.polySpecularBuffer);
        tessGeo.polySpecularBuffer.rewind();
        pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    protected void copyPolyTexCoords(int i, int j)
    {
        tessGeo.updatePolyTexCoordsBuffer(i, j);
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPolyTexcoord.glId);
        tessGeo.polyTexCoordsBuffer.position(i * 2);
        pgl.bufferSubData(PGL.ARRAY_BUFFER, i * 2 * PGL.SIZEOF_FLOAT, j * 2 * PGL.SIZEOF_FLOAT, tessGeo.polyTexCoordsBuffer);
        tessGeo.polyTexCoordsBuffer.rewind();
        pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    protected void copyPolyVertices(int i, int j)
    {
        tessGeo.updatePolyVerticesBuffer(i, j);
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPolyVertex.glId);
        tessGeo.polyVerticesBuffer.position(i * 4);
        pgl.bufferSubData(PGL.ARRAY_BUFFER, i * 4 * PGL.SIZEOF_FLOAT, j * 4 * PGL.SIZEOF_FLOAT, tessGeo.polyVerticesBuffer);
        tessGeo.polyVerticesBuffer.rewind();
        pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    public void curveDetail(int i)
    {
        curveDetail = i;
        if(inGeo.codeCount > 0)
            markForTessellation();
    }

    public void curveTightness(float f)
    {
        curveTightness = f;
        if(inGeo.codeCount > 0)
            markForTessellation();
    }

    public void curveVertex(float f, float f1)
    {
        curveVertexImpl(f, f1, 0.0F);
    }

    public void curveVertex(float f, float f1, float f2)
    {
        curveVertexImpl(f, f1, f2);
    }

    protected void curveVertexImpl(float f, float f1, float f2)
    {
        inGeo.setMaterial(fillColor, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininess);
        inGeo.setNormal(normalX, normalY, normalZ);
        inGeo.addCurveVertex(f, f1, f2, vertexBreak());
    }

    public void disableStyle()
    {
        if(openShape)
        {
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "disableStyle()"
            });
        } else
        {
            savedStroke = stroke;
            savedStrokeColor = strokeColor;
            savedStrokeWeight = strokeWeight;
            savedStrokeCap = strokeCap;
            savedStrokeJoin = strokeJoin;
            savedFill = fill;
            savedFillColor = fillColor;
            savedTint = tint;
            savedTintColor = tintColor;
            savedAmbientColor = ambientColor;
            savedSpecularColor = specularColor;
            savedEmissiveColor = emissiveColor;
            savedShininess = shininess;
            savedTextureMode = textureMode;
            super.disableStyle();
        }
    }

    public void draw(PGraphics pgraphics)
    {
        if(pgraphics instanceof PGraphicsOpenGL)
        {
            PGraphicsOpenGL pgraphicsopengl = (PGraphicsOpenGL)pgraphics;
            if(visible)
            {
                pre(pgraphicsopengl);
                updateTessellation();
                updateGeometry();
                if(family == 0)
                {
                    if(fragmentedGroup(pgraphicsopengl))
                    {
                        for(int i = 0; i < childCount; i++)
                            ((PShapeOpenGL)children[i]).draw(((PGraphics) (pgraphicsopengl)));

                    } else
                    {
                        if(textures != null && textures.size() == 1)
                            pgraphics = (PImage)textures.toArray()[0];
                        else
                            pgraphics = null;
                        render(pgraphicsopengl, pgraphics);
                    }
                } else
                {
                    render(pgraphicsopengl, image);
                }
                post(pgraphicsopengl);
            }
        } else
        {
            super.draw(pgraphics);
        }
    }

    protected void drawGeometry(PGraphics pgraphics)
    {
        vertexCount = inGeo.vertexCount;
        vertices = inGeo.getVertexData();
        super.drawGeometry(pgraphics);
        vertexCount = 0;
        vertices = (float[][])null;
    }

    public void enableStyle()
    {
        if(savedStroke)
        {
            setStroke(true);
            setStroke(savedStrokeColor);
            setStrokeWeight(savedStrokeWeight);
            setStrokeCap(savedStrokeCap);
            setStrokeJoin(savedStrokeJoin);
        } else
        {
            setStroke(false);
        }
        if(savedFill)
        {
            setFill(true);
            setFill(savedFillColor);
        } else
        {
            setFill(false);
        }
        if(savedTint)
        {
            setTint(true);
            setTint(savedTintColor);
        }
        setAmbient(savedAmbientColor);
        setSpecular(savedSpecularColor);
        setEmissive(savedEmissiveColor);
        setShininess(savedShininess);
        if(image != null)
            setTextureMode(savedTextureMode);
        super.enableStyle();
    }

    protected void endContourImpl()
    {
    }

    public void endShape(int i)
    {
        super.endShape(i);
        inGeo.trim();
        boolean flag;
        if(i == 2)
            flag = true;
        else
            flag = false;
        close = flag;
        markForTessellation();
        shapeCreated = true;
    }

    protected boolean fragmentedGroup(PGraphicsOpenGL pgraphicsopengl)
    {
label0:
        {
            boolean flag = true;
            boolean flag1 = flag;
            if(pgraphicsopengl.getHint(6))
                break label0;
            if(textures != null)
            {
                flag1 = flag;
                if(1 < textures.size())
                    break label0;
                flag1 = flag;
                if(untexChild)
                    break label0;
            }
            if(strokedTexture)
                flag1 = flag;
            else
                flag1 = false;
        }
        return flag1;
    }

    public int getAmbient(int i)
    {
        if(family != 0)
            i = PGL.nativeToJavaARGB(inGeo.ambient[i]);
        else
            i = 0;
        return i;
    }

    public float getDepth()
    {
        PVector pvector = new PVector((1.0F / 0.0F), (1.0F / 0.0F), (1.0F / 0.0F));
        PVector pvector1 = new PVector((-1.0F / 0.0F), (-1.0F / 0.0F), (-1.0F / 0.0F));
        if(shapeCreated)
        {
            getVertexMin(pvector);
            getVertexMax(pvector1);
        }
        depth = pvector1.z - pvector.z;
        return depth;
    }

    public int getEmissive(int i)
    {
        if(family == 0)
            i = PGL.nativeToJavaARGB(inGeo.emissive[i]);
        else
            i = 0;
        return i;
    }

    public int getFill(int i)
    {
        if(family != 0 && image == null)
            i = PGL.nativeToJavaARGB(inGeo.colors[i]);
        else
            i = 0;
        return i;
    }

    public float getHeight()
    {
        PVector pvector = new PVector((1.0F / 0.0F), (1.0F / 0.0F), (1.0F / 0.0F));
        PVector pvector1 = new PVector((-1.0F / 0.0F), (-1.0F / 0.0F), (-1.0F / 0.0F));
        if(shapeCreated)
        {
            getVertexMin(pvector);
            getVertexMax(pvector1);
        }
        height = pvector1.y - pvector.y;
        return height;
    }

    public PVector getNormal(int i, PVector pvector)
    {
        PVector pvector1 = pvector;
        if(pvector == null)
            pvector1 = new PVector();
        pvector1.x = inGeo.normals[i * 3 + 0];
        pvector1.y = inGeo.normals[i * 3 + 1];
        pvector1.z = inGeo.normals[i * 3 + 2];
        return pvector1;
    }

    public float getNormalX(int i)
    {
        return inGeo.normals[i * 3 + 0];
    }

    public float getNormalY(int i)
    {
        return inGeo.normals[i * 3 + 1];
    }

    public float getNormalZ(int i)
    {
        return inGeo.normals[i * 3 + 2];
    }

    public float getShininess(int i)
    {
        float f;
        if(family == 0)
            f = inGeo.shininess[i];
        else
            f = 0.0F;
        return f;
    }

    public int getSpecular(int i)
    {
        if(family == 0)
            i = PGL.nativeToJavaARGB(inGeo.specular[i]);
        else
            i = 0;
        return i;
    }

    public int getStroke(int i)
    {
        if(family != 0)
            i = PGL.nativeToJavaARGB(inGeo.strokeColors[i]);
        else
            i = 0;
        return i;
    }

    public float getStrokeWeight(int i)
    {
        float f;
        if(family != 0)
            f = inGeo.strokeWeights[i];
        else
            f = 0.0F;
        return f;
    }

    public PShape getTessellation()
    {
        updateTessellation();
        float af[] = tessGeo.polyVertices;
        float af1[] = tessGeo.polyNormals;
        int ai[] = tessGeo.polyColors;
        float af2[] = tessGeo.polyTexCoords;
        short aword0[] = tessGeo.polyIndices;
        PShape pshape = pg.createShapeFamily(3);
        pshape.set3D(is3D);
        pshape.beginShape(9);
        pshape.noStroke();
        PGraphicsOpenGL.IndexCache indexcache = tessGeo.polyIndexCache;
        int i = firstPolyIndexCache;
        do
        {
            if(i > lastPolyIndexCache)
                break;
            int j = indexcache.indexOffset[i];
            int k = indexcache.indexCount[i];
            int l = indexcache.vertexOffset[i];
            int i1 = j / 3;
            while(i1 < (j + k) / 3) 
            {
                int j1 = l + aword0[i1 * 3 + 0];
                int k1 = l + aword0[i1 * 3 + 1];
                int l1 = l + aword0[i1 * 3 + 2];
                if(is3D())
                {
                    float f = af[j1 * 4 + 0];
                    float f1 = af[j1 * 4 + 1];
                    float f3 = af[j1 * 4 + 2];
                    float f5 = af[k1 * 4 + 0];
                    float f6 = af[k1 * 4 + 1];
                    float f7 = af[k1 * 4 + 2];
                    float f9 = af[l1 * 4 + 0];
                    float f11 = af[l1 * 4 + 1];
                    float f12 = af[l1 * 4 + 2];
                    float f13 = af1[j1 * 3 + 0];
                    float f15 = af1[j1 * 3 + 1];
                    float f16 = af1[j1 * 3 + 2];
                    float f18 = af1[k1 * 3 + 0];
                    float f19 = af1[k1 * 3 + 1];
                    float f20 = af1[k1 * 3 + 2];
                    float f21 = af1[l1 * 3 + 0];
                    float f22 = af1[l1 * 3 + 1];
                    float f23 = af1[l1 * 3 + 2];
                    int i2 = PGL.nativeToJavaARGB(ai[j1]);
                    int k2 = PGL.nativeToJavaARGB(ai[k1]);
                    int i3 = PGL.nativeToJavaARGB(ai[l1]);
                    pshape.fill(i2);
                    pshape.normal(f13, f15, f16);
                    pshape.vertex(f, f1, f3, af2[j1 * 2 + 0], af2[j1 * 2 + 1]);
                    pshape.fill(k2);
                    pshape.normal(f18, f19, f20);
                    pshape.vertex(f5, f6, f7, af2[k1 * 2 + 0], af2[k1 * 2 + 1]);
                    pshape.fill(i3);
                    pshape.normal(f21, f22, f23);
                    pshape.vertex(f9, f11, f12, af2[l1 * 2 + 0], af2[l1 * 2 + 1]);
                } else
                if(is2D())
                {
                    float f2 = af[j1 * 4 + 0];
                    float f4 = af[j1 * 4 + 1];
                    float f14 = af[k1 * 4 + 0];
                    float f10 = af[k1 * 4 + 1];
                    float f8 = af[l1 * 4 + 0];
                    float f17 = af[l1 * 4 + 1];
                    int l2 = PGL.nativeToJavaARGB(ai[j1]);
                    int j3 = PGL.nativeToJavaARGB(ai[k1]);
                    int j2 = PGL.nativeToJavaARGB(ai[l1]);
                    pshape.fill(l2);
                    pshape.vertex(f2, f4, af2[j1 * 2 + 0], af2[j1 * 2 + 1]);
                    pshape.fill(j3);
                    pshape.vertex(f14, f10, af2[k1 * 2 + 0], af2[k1 * 2 + 1]);
                    pshape.fill(j2);
                    pshape.vertex(f8, f17, af2[l1 * 2 + 0], af2[l1 * 2 + 1]);
                }
                i1++;
            }
            i++;
        } while(true);
        pshape.endShape();
        return pshape;
    }

    public float[] getTessellation(int i, int j)
    {
        updateTessellation();
        if(i != 9) goto _L2; else goto _L1
_L1:
        if(j != 0) goto _L4; else goto _L3
_L3:
        float af[];
        if(is3D())
            root.setModifiedPolyVertices(firstPolyVertex, lastPolyVertex);
        else
        if(is2D())
        {
            i = lastPolyVertex + 1;
            if(-1 < firstLineVertex)
                i = firstLineVertex;
            if(-1 < firstPointVertex)
                i = firstPointVertex;
            root.setModifiedPolyVertices(firstPolyVertex, i - 1);
        }
        af = tessGeo.polyVertices;
_L7:
        return af;
_L4:
        if(j == 1)
        {
            if(is3D())
                root.setModifiedPolyNormals(firstPolyVertex, lastPolyVertex);
            else
            if(is2D())
            {
                i = lastPolyVertex + 1;
                if(-1 < firstLineVertex)
                    i = firstLineVertex;
                if(-1 < firstPointVertex)
                    i = firstPointVertex;
                root.setModifiedPolyNormals(firstPolyVertex, i - 1);
            }
            af = tessGeo.polyNormals;
            continue; /* Loop/switch isn't completed */
        }
        if(j == 2)
        {
            if(is3D())
                root.setModifiedPolyTexCoords(firstPolyVertex, lastPolyVertex);
            else
            if(is2D())
            {
                i = lastPolyVertex + 1;
                if(-1 < firstLineVertex)
                    i = firstLineVertex;
                if(-1 < firstPointVertex)
                    i = firstPointVertex;
                root.setModifiedPolyTexCoords(firstPolyVertex, i - 1);
            }
            af = tessGeo.polyTexCoords;
            continue; /* Loop/switch isn't completed */
        }
          goto _L5
_L2:
        if(i == 5)
        {
            if(j == 0)
            {
                if(is3D())
                    root.setModifiedLineVertices(firstLineVertex, lastLineVertex);
                else
                if(is2D())
                    root.setModifiedPolyVertices(firstLineVertex, lastLineVertex);
                af = tessGeo.lineVertices;
                continue; /* Loop/switch isn't completed */
            }
            if(j == 3)
            {
                if(is2D())
                    root.setModifiedLineAttributes(firstLineVertex, lastLineVertex);
                af = tessGeo.lineDirections;
                continue; /* Loop/switch isn't completed */
            }
        } else
        if(i == 3)
        {
            if(j == 0)
            {
                if(is3D())
                    root.setModifiedPointVertices(firstPointVertex, lastPointVertex);
                else
                if(is2D())
                    root.setModifiedPolyVertices(firstPointVertex, lastPointVertex);
                af = tessGeo.pointVertices;
                continue; /* Loop/switch isn't completed */
            }
            if(j == 4)
            {
                if(is2D())
                    root.setModifiedPointAttributes(firstPointVertex, lastPointVertex);
                af = tessGeo.pointOffsets;
                continue; /* Loop/switch isn't completed */
            }
        }
_L5:
        af = null;
        if(true) goto _L7; else goto _L6
_L6:
    }

    public float getTextureU(int i)
    {
        return inGeo.texcoords[i * 2 + 0];
    }

    public float getTextureV(int i)
    {
        return inGeo.texcoords[i * 2 + 1];
    }

    public int getTint(int i)
    {
        if(family != 0 && image != null)
            i = PGL.nativeToJavaARGB(inGeo.colors[i]);
        else
            i = 0;
        return i;
    }

    public PVector getVertex(int i, PVector pvector)
    {
        PVector pvector1 = pvector;
        if(pvector == null)
            pvector1 = new PVector();
        pvector1.x = inGeo.vertices[i * 3 + 0];
        pvector1.y = inGeo.vertices[i * 3 + 1];
        pvector1.z = inGeo.vertices[i * 3 + 2];
        return pvector1;
    }

    public int getVertexCode(int i)
    {
        return inGeo.codes[i];
    }

    public int getVertexCodeCount()
    {
        int i;
        if(family == 0)
        {
            i = 0;
        } else
        {
            if(family == 1 || family == 2)
                updateTessellation();
            i = inGeo.codeCount;
        }
        return i;
    }

    public int[] getVertexCodes()
    {
        int ai[] = null;
        if(family != 0) goto _L2; else goto _L1
_L1:
        return ai;
_L2:
        if(family == 1 || family == 2)
            updateTessellation();
        if(inGeo.codes != null)
            ai = inGeo.codes;
        if(true) goto _L1; else goto _L3
_L3:
    }

    public int getVertexCount()
    {
        int i;
        if(family == 0)
        {
            i = 0;
        } else
        {
            if(family == 1 || family == 2)
                updateTessellation();
            i = inGeo.vertexCount;
        }
        return i;
    }

    protected void getVertexMax(PVector pvector)
    {
        updateTessellation();
        if(family == 0)
        {
            for(int i = 0; i < childCount; i++)
                ((PShapeOpenGL)children[i]).getVertexMax(pvector);

        } else
        {
            if(hasPolys)
                tessGeo.getPolyVertexMax(pvector, firstPolyVertex, lastPolyVertex);
            if(is3D())
            {
                if(hasLines)
                    tessGeo.getLineVertexMax(pvector, firstLineVertex, lastLineVertex);
                if(hasPoints)
                    tessGeo.getPointVertexMax(pvector, firstPointVertex, lastPointVertex);
            }
        }
    }

    protected void getVertexMin(PVector pvector)
    {
        updateTessellation();
        if(family == 0)
        {
            for(int i = 0; i < childCount; i++)
                ((PShapeOpenGL)children[i]).getVertexMin(pvector);

        } else
        {
            if(hasPolys)
                tessGeo.getPolyVertexMin(pvector, firstPolyVertex, lastPolyVertex);
            if(is3D())
            {
                if(hasLines)
                    tessGeo.getLineVertexMin(pvector, firstLineVertex, lastLineVertex);
                if(hasPoints)
                    tessGeo.getPointVertexMin(pvector, firstPointVertex, lastPointVertex);
            }
        }
    }

    protected int getVertexSum(PVector pvector, int i)
    {
        updateTessellation();
        if(family == 0)
        {
            int j = 0;
            int l = i;
            do
            {
                i = l;
                if(j >= childCount)
                    break;
                l += ((PShapeOpenGL)children[j]).getVertexSum(pvector, l);
                j++;
            } while(true);
        } else
        {
            int i1 = i;
            if(hasPolys)
                i1 = i + tessGeo.getPolyVertexSum(pvector, firstPolyVertex, lastPolyVertex);
            i = i1;
            if(is3D())
            {
                int k = i1;
                if(hasLines)
                    k = i1 + tessGeo.getLineVertexSum(pvector, firstLineVertex, lastLineVertex);
                i = k;
                if(hasPoints)
                    i = k + tessGeo.getPointVertexSum(pvector, firstPointVertex, lastPointVertex);
            }
        }
        return i;
    }

    public float getVertexX(int i)
    {
        return inGeo.vertices[i * 3 + 0];
    }

    public float getVertexY(int i)
    {
        return inGeo.vertices[i * 3 + 1];
    }

    public float getVertexZ(int i)
    {
        return inGeo.vertices[i * 3 + 2];
    }

    public float getWidth()
    {
        PVector pvector = new PVector((1.0F / 0.0F), (1.0F / 0.0F), (1.0F / 0.0F));
        PVector pvector1 = new PVector((-1.0F / 0.0F), (-1.0F / 0.0F), (-1.0F / 0.0F));
        if(shapeCreated)
        {
            getVertexMin(pvector);
            getVertexMax(pvector1);
        }
        width = pvector1.x - pvector.x;
        return width;
    }

    protected boolean hasStrokedTexture()
    {
        boolean flag;
        if(family == 0)
            flag = strokedTexture;
        else
        if(image != null && stroke)
            flag = true;
        else
            flag = false;
        return flag;
    }

    protected boolean hasTexture()
    {
        boolean flag = true;
        if(family != 0) goto _L2; else goto _L1
_L1:
        if(textures == null || textures.size() <= 0)
            flag = false;
_L4:
        return flag;
_L2:
        if(image == null)
            flag = false;
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected boolean hasTexture(PImage pimage)
    {
        boolean flag = true;
        if(family != 0) goto _L2; else goto _L1
_L1:
        if(textures == null || !textures.contains(pimage))
            flag = false;
_L4:
        return flag;
_L2:
        if(image != pimage)
            flag = false;
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected void initBuffers()
    {
        boolean flag = contextIsOutdated();
        context = pgl.getCurrentContext();
        if(hasPolys && (needBufferInit || flag))
            initPolyBuffers();
        if(hasLines && (needBufferInit || flag))
            initLineBuffers();
        if(hasPoints && (needBufferInit || flag))
            initPointBuffers();
        needBufferInit = false;
    }

    protected void initLineBuffers()
    {
        int i = tessGeo.lineVertexCount;
        int j = PGL.SIZEOF_FLOAT * i;
        int k = PGL.SIZEOF_INT;
        tessGeo.updateLineVerticesBuffer();
        if(bufLineVertex == null)
            bufLineVertex = new VertexBuffer(pg, PGL.ARRAY_BUFFER, 4, PGL.SIZEOF_FLOAT);
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufLineVertex.glId);
        pgl.bufferData(PGL.ARRAY_BUFFER, j * 4, tessGeo.lineVerticesBuffer, glUsage);
        tessGeo.updateLineColorsBuffer();
        if(bufLineColor == null)
            bufLineColor = new VertexBuffer(pg, PGL.ARRAY_BUFFER, 1, PGL.SIZEOF_INT);
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufLineColor.glId);
        pgl.bufferData(PGL.ARRAY_BUFFER, i * k, tessGeo.lineColorsBuffer, glUsage);
        tessGeo.updateLineDirectionsBuffer();
        if(bufLineAttrib == null)
            bufLineAttrib = new VertexBuffer(pg, PGL.ARRAY_BUFFER, 4, PGL.SIZEOF_FLOAT);
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufLineAttrib.glId);
        pgl.bufferData(PGL.ARRAY_BUFFER, j * 4, tessGeo.lineDirectionsBuffer, glUsage);
        pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
        tessGeo.updateLineIndicesBuffer();
        if(bufLineIndex == null)
            bufLineIndex = new VertexBuffer(pg, PGL.ELEMENT_ARRAY_BUFFER, 1, PGL.SIZEOF_INDEX, true);
        pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, bufLineIndex.glId);
        pgl.bufferData(PGL.ELEMENT_ARRAY_BUFFER, tessGeo.lineIndexCount * PGL.SIZEOF_INDEX, tessGeo.lineIndicesBuffer, glUsage);
        pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, 0);
    }

    protected void initModified()
    {
        modified = false;
        modifiedPolyVertices = false;
        modifiedPolyColors = false;
        modifiedPolyNormals = false;
        modifiedPolyTexCoords = false;
        modifiedPolyAmbient = false;
        modifiedPolySpecular = false;
        modifiedPolyEmissive = false;
        modifiedPolyShininess = false;
        modifiedLineVertices = false;
        modifiedLineColors = false;
        modifiedLineAttributes = false;
        modifiedPointVertices = false;
        modifiedPointColors = false;
        modifiedPointAttributes = false;
        firstModifiedPolyVertex = 0x7fffffff;
        lastModifiedPolyVertex = 0x80000000;
        firstModifiedPolyColor = 0x7fffffff;
        lastModifiedPolyColor = 0x80000000;
        firstModifiedPolyNormal = 0x7fffffff;
        lastModifiedPolyNormal = 0x80000000;
        firstModifiedPolyTexcoord = 0x7fffffff;
        lastModifiedPolyTexcoord = 0x80000000;
        firstModifiedPolyAmbient = 0x7fffffff;
        lastModifiedPolyAmbient = 0x80000000;
        firstModifiedPolySpecular = 0x7fffffff;
        lastModifiedPolySpecular = 0x80000000;
        firstModifiedPolyEmissive = 0x7fffffff;
        lastModifiedPolyEmissive = 0x80000000;
        firstModifiedPolyShininess = 0x7fffffff;
        lastModifiedPolyShininess = 0x80000000;
        firstModifiedLineVertex = 0x7fffffff;
        lastModifiedLineVertex = 0x80000000;
        firstModifiedLineColor = 0x7fffffff;
        lastModifiedLineColor = 0x80000000;
        firstModifiedLineAttribute = 0x7fffffff;
        lastModifiedLineAttribute = 0x80000000;
        firstModifiedPointVertex = 0x7fffffff;
        lastModifiedPointVertex = 0x80000000;
        firstModifiedPointColor = 0x7fffffff;
        lastModifiedPointColor = 0x80000000;
        firstModifiedPointAttribute = 0x7fffffff;
        lastModifiedPointAttribute = 0x80000000;
    }

    protected void initPointBuffers()
    {
        int i = tessGeo.pointVertexCount;
        int j = PGL.SIZEOF_FLOAT * i;
        int k = PGL.SIZEOF_INT;
        tessGeo.updatePointVerticesBuffer();
        if(bufPointVertex == null)
            bufPointVertex = new VertexBuffer(pg, PGL.ARRAY_BUFFER, 4, PGL.SIZEOF_FLOAT);
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPointVertex.glId);
        pgl.bufferData(PGL.ARRAY_BUFFER, j * 4, tessGeo.pointVerticesBuffer, glUsage);
        tessGeo.updatePointColorsBuffer();
        if(bufPointColor == null)
            bufPointColor = new VertexBuffer(pg, PGL.ARRAY_BUFFER, 1, PGL.SIZEOF_INT);
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPointColor.glId);
        pgl.bufferData(PGL.ARRAY_BUFFER, i * k, tessGeo.pointColorsBuffer, glUsage);
        tessGeo.updatePointOffsetsBuffer();
        if(bufPointAttrib == null)
            bufPointAttrib = new VertexBuffer(pg, PGL.ARRAY_BUFFER, 2, PGL.SIZEOF_FLOAT);
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPointAttrib.glId);
        pgl.bufferData(PGL.ARRAY_BUFFER, j * 2, tessGeo.pointOffsetsBuffer, glUsage);
        pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
        tessGeo.updatePointIndicesBuffer();
        if(bufPointIndex == null)
            bufPointIndex = new VertexBuffer(pg, PGL.ELEMENT_ARRAY_BUFFER, 1, PGL.SIZEOF_INDEX, true);
        pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, bufPointIndex.glId);
        pgl.bufferData(PGL.ELEMENT_ARRAY_BUFFER, tessGeo.pointIndexCount * PGL.SIZEOF_INDEX, tessGeo.pointIndicesBuffer, glUsage);
        pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, 0);
    }

    protected void initPolyBuffers()
    {
        int i = tessGeo.polyVertexCount;
        int j = PGL.SIZEOF_FLOAT * i;
        int k = PGL.SIZEOF_INT * i;
        tessGeo.updatePolyVerticesBuffer();
        if(bufPolyVertex == null)
            bufPolyVertex = new VertexBuffer(pg, PGL.ARRAY_BUFFER, 4, PGL.SIZEOF_FLOAT);
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPolyVertex.glId);
        pgl.bufferData(PGL.ARRAY_BUFFER, j * 4, tessGeo.polyVerticesBuffer, glUsage);
        tessGeo.updatePolyColorsBuffer();
        if(bufPolyColor == null)
            bufPolyColor = new VertexBuffer(pg, PGL.ARRAY_BUFFER, 1, PGL.SIZEOF_INT);
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPolyColor.glId);
        pgl.bufferData(PGL.ARRAY_BUFFER, k, tessGeo.polyColorsBuffer, glUsage);
        tessGeo.updatePolyNormalsBuffer();
        if(bufPolyNormal == null)
            bufPolyNormal = new VertexBuffer(pg, PGL.ARRAY_BUFFER, 3, PGL.SIZEOF_FLOAT);
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPolyNormal.glId);
        pgl.bufferData(PGL.ARRAY_BUFFER, j * 3, tessGeo.polyNormalsBuffer, glUsage);
        tessGeo.updatePolyTexCoordsBuffer();
        if(bufPolyTexcoord == null)
            bufPolyTexcoord = new VertexBuffer(pg, PGL.ARRAY_BUFFER, 2, PGL.SIZEOF_FLOAT);
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPolyTexcoord.glId);
        pgl.bufferData(PGL.ARRAY_BUFFER, j * 2, tessGeo.polyTexCoordsBuffer, glUsage);
        tessGeo.updatePolyAmbientBuffer();
        if(bufPolyAmbient == null)
            bufPolyAmbient = new VertexBuffer(pg, PGL.ARRAY_BUFFER, 1, PGL.SIZEOF_INT);
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPolyAmbient.glId);
        pgl.bufferData(PGL.ARRAY_BUFFER, k, tessGeo.polyAmbientBuffer, glUsage);
        tessGeo.updatePolySpecularBuffer();
        if(bufPolySpecular == null)
            bufPolySpecular = new VertexBuffer(pg, PGL.ARRAY_BUFFER, 1, PGL.SIZEOF_INT);
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPolySpecular.glId);
        pgl.bufferData(PGL.ARRAY_BUFFER, k, tessGeo.polySpecularBuffer, glUsage);
        tessGeo.updatePolyEmissiveBuffer();
        if(bufPolyEmissive == null)
            bufPolyEmissive = new VertexBuffer(pg, PGL.ARRAY_BUFFER, 1, PGL.SIZEOF_INT);
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPolyEmissive.glId);
        pgl.bufferData(PGL.ARRAY_BUFFER, k, tessGeo.polyEmissiveBuffer, glUsage);
        tessGeo.updatePolyShininessBuffer();
        if(bufPolyShininess == null)
            bufPolyShininess = new VertexBuffer(pg, PGL.ARRAY_BUFFER, 1, PGL.SIZEOF_FLOAT);
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPolyShininess.glId);
        pgl.bufferData(PGL.ARRAY_BUFFER, j, tessGeo.polyShininessBuffer, glUsage);
        String s;
        PGraphicsOpenGL.VertexAttribute vertexattribute;
        for(Iterator iterator = polyAttribs.keySet().iterator(); iterator.hasNext(); pgl.bufferData(PGL.ARRAY_BUFFER, vertexattribute.sizeInBytes(i), (Buffer)tessGeo.polyAttribBuffers.get(s), glUsage))
        {
            s = (String)iterator.next();
            vertexattribute = (PGraphicsOpenGL.VertexAttribute)polyAttribs.get(s);
            tessGeo.updateAttribBuffer(vertexattribute.name);
            if(!vertexattribute.bufferCreated())
                vertexattribute.createBuffer(pgl);
            pgl.bindBuffer(PGL.ARRAY_BUFFER, vertexattribute.buf.glId);
        }

        pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
        tessGeo.updatePolyIndicesBuffer();
        if(bufPolyIndex == null)
            bufPolyIndex = new VertexBuffer(pg, PGL.ELEMENT_ARRAY_BUFFER, 1, PGL.SIZEOF_INDEX, true);
        pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, bufPolyIndex.glId);
        pgl.bufferData(PGL.ELEMENT_ARRAY_BUFFER, tessGeo.polyIndexCount * PGL.SIZEOF_INDEX, tessGeo.polyIndicesBuffer, glUsage);
        pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, 0);
    }

    protected void markForTessellation()
    {
        root.tessellated = false;
        tessellated = false;
    }

    public void normal(float f, float f1, float f2)
    {
        if(openShape) goto _L2; else goto _L1
_L1:
        PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
            "normal()"
        });
_L4:
        return;
_L2:
        if(family == 0)
        {
            PGraphics.showWarning("Cannot set normal in GROUP shape");
        } else
        {
            normalX = f;
            normalY = f1;
            normalZ = f2;
            if(normalMode == 0)
                normalMode = 1;
            else
            if(normalMode == 1)
                normalMode = 2;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected PMatrix popTransform()
    {
        PMatrix pmatrix;
        if(transformStack == null || transformStack.size() == 0)
            pmatrix = null;
        else
            pmatrix = (PMatrix)transformStack.pop();
        return pmatrix;
    }

    protected void post(PGraphics pgraphics)
    {
        if(!(pgraphics instanceof PGraphicsOpenGL))
            super.post(pgraphics);
    }

    protected void pre(PGraphics pgraphics)
    {
        if(pgraphics instanceof PGraphicsOpenGL)
        {
            if(!style)
                styles(pgraphics);
        } else
        {
            super.pre(pgraphics);
        }
    }

    protected void pushTransform()
    {
        if(transformStack == null)
            transformStack = new Stack();
        Object obj;
        if(transform instanceof PMatrix2D)
            obj = new PMatrix2D();
        else
            obj = new PMatrix3D();
        ((PMatrix) (obj)).set(transform);
        transformStack.push(obj);
    }

    public void quadraticVertex(float f, float f1, float f2, float f3)
    {
        quadraticVertexImpl(f, f1, 0.0F, f2, f3, 0.0F);
    }

    public void quadraticVertex(float f, float f1, float f2, float f3, float f4, float f5)
    {
        quadraticVertexImpl(f, f1, f2, f3, f4, f5);
    }

    protected void quadraticVertexImpl(float f, float f1, float f2, float f3, float f4, float f5)
    {
        inGeo.setMaterial(fillColor, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininess);
        inGeo.setNormal(normalX, normalY, normalZ);
        inGeo.addQuadraticVertex(f, f1, f2, f3, f4, f5, vertexBreak());
    }

    protected void rawLines(PGraphicsOpenGL pgraphicsopengl)
    {
        PGraphics pgraphics = pgraphicsopengl.getRaw();
        pgraphics.colorMode(1);
        pgraphics.noFill();
        pgraphics.strokeCap(strokeCap);
        pgraphics.strokeJoin(strokeJoin);
        pgraphics.beginShape(5);
        float af[] = tessGeo.lineVertices;
        int ai[] = tessGeo.lineColors;
        float af1[] = tessGeo.lineDirections;
        short aword0[] = tessGeo.lineIndices;
        PGraphicsOpenGL.IndexCache indexcache = tessGeo.lineIndexCache;
        int i = firstLineIndexCache;
        do
        {
            if(i > lastLineIndexCache)
                break;
            int j = indexcache.indexOffset[i];
            int k = indexcache.indexCount[i];
            int l = indexcache.vertexOffset[i];
            int i1 = j / 6;
            while(i1 < (j + k) / 6) 
            {
                int j1 = aword0[i1 * 6 + 0] + l;
                int k1 = aword0[i1 * 6 + 5] + l;
                float f = 2.0F * af1[j1 * 4 + 3];
                float f1 = 2.0F * af1[k1 * 4 + 3];
                if(!PGraphicsOpenGL.zero(f))
                {
                    float af2[] = new float[4];
                    float[] _tmp = af2;
                    af2[0] = 0.0F;
                    af2[1] = 0.0F;
                    af2[2] = 0.0F;
                    af2[3] = 0.0F;
                    float af3[] = new float[4];
                    float[] _tmp1 = af3;
                    af3[0] = 0.0F;
                    af3[1] = 0.0F;
                    af3[2] = 0.0F;
                    af3[3] = 0.0F;
                    float af4[] = new float[4];
                    float[] _tmp2 = af4;
                    af4[0] = 0.0F;
                    af4[1] = 0.0F;
                    af4[2] = 0.0F;
                    af4[3] = 0.0F;
                    float af5[] = new float[4];
                    float[] _tmp3 = af5;
                    af5[0] = 0.0F;
                    af5[1] = 0.0F;
                    af5[2] = 0.0F;
                    af5[3] = 0.0F;
                    int l1 = PGL.nativeToJavaARGB(ai[j1]);
                    int i2 = PGL.nativeToJavaARGB(ai[k1]);
                    PApplet.arrayCopy(af, j1 * 4, af2, 0, 4);
                    PApplet.arrayCopy(af, k1 * 4, af3, 0, 4);
                    pgraphicsopengl.modelview.mult(af2, af4);
                    pgraphicsopengl.modelview.mult(af3, af5);
                    if(pgraphics.is3D())
                    {
                        pgraphics.strokeWeight(f);
                        pgraphics.stroke(l1);
                        pgraphics.vertex(af4[0], af4[1], af4[2]);
                        pgraphics.strokeWeight(f1);
                        pgraphics.stroke(i2);
                        pgraphics.vertex(af5[0], af5[1], af5[2]);
                    } else
                    if(pgraphics.is2D())
                    {
                        float f2 = pgraphicsopengl.screenXImpl(af4[0], af4[1], af4[2], af4[3]);
                        float f3 = pgraphicsopengl.screenYImpl(af4[0], af4[1], af4[2], af4[3]);
                        float f4 = pgraphicsopengl.screenXImpl(af5[0], af5[1], af5[2], af5[3]);
                        float f5 = pgraphicsopengl.screenYImpl(af5[0], af5[1], af5[2], af5[3]);
                        pgraphics.strokeWeight(f);
                        pgraphics.stroke(l1);
                        pgraphics.vertex(f2, f3);
                        pgraphics.strokeWeight(f1);
                        pgraphics.stroke(i2);
                        pgraphics.vertex(f4, f5);
                    }
                }
                i1++;
            }
            i++;
        } while(true);
        pgraphics.endShape();
    }

    protected void rawPoints(PGraphicsOpenGL pgraphicsopengl)
    {
        PGraphics pgraphics = pgraphicsopengl.getRaw();
        pgraphics.colorMode(1);
        pgraphics.noFill();
        pgraphics.strokeCap(strokeCap);
        pgraphics.beginShape(3);
        float af[] = tessGeo.pointVertices;
        int ai[] = tessGeo.pointColors;
        float af1[] = tessGeo.pointOffsets;
        short aword0[] = tessGeo.pointIndices;
        PGraphicsOpenGL.IndexCache indexcache = tessGeo.pointIndexCache;
        int i = 0;
        do
        {
            if(i >= indexcache.size)
                break;
            int j = indexcache.indexOffset[i];
            int k = indexcache.indexCount[i];
            int l = indexcache.vertexOffset[i];
            int i1 = j;
            while(i1 < (j + k) / 3) 
            {
                float f = af1[i1 * 2 + 2];
                int j1;
                int k1;
                int l1;
                float af2[];
                float af3[];
                if(0.0F < f)
                {
                    f /= 0.5F;
                    j1 = PApplet.min(200, PApplet.max(20, (int)((6.283185F * f) / 10F))) + 1;
                } else
                {
                    f = -f / 0.5F;
                    j1 = 5;
                }
                k1 = aword0[i1 * 3] + l;
                l1 = PGL.nativeToJavaARGB(ai[k1]);
                af2 = new float[4];
                float[] _tmp = af2;
                af2[0] = 0.0F;
                af2[1] = 0.0F;
                af2[2] = 0.0F;
                af2[3] = 0.0F;
                af3 = new float[4];
                float[] _tmp1 = af3;
                af3[0] = 0.0F;
                af3[1] = 0.0F;
                af3[2] = 0.0F;
                af3[3] = 0.0F;
                PApplet.arrayCopy(af, k1 * 4, af3, 0, 4);
                pgraphicsopengl.modelview.mult(af3, af2);
                if(pgraphics.is3D())
                {
                    pgraphics.strokeWeight(f);
                    pgraphics.stroke(l1);
                    pgraphics.vertex(af2[0], af2[1], af2[2]);
                } else
                if(pgraphics.is2D())
                {
                    float f1 = pgraphicsopengl.screenXImpl(af2[0], af2[1], af2[2], af2[3]);
                    float f2 = pgraphicsopengl.screenYImpl(af2[0], af2[1], af2[2], af2[3]);
                    pgraphics.strokeWeight(f);
                    pgraphics.stroke(l1);
                    pgraphics.vertex(f1, f2);
                }
                i1 += j1;
            }
            i++;
        } while(true);
        pgraphics.endShape();
    }

    protected void rawPolys(PGraphicsOpenGL pgraphicsopengl, PImage pimage)
    {
        PGraphics pgraphics = pgraphicsopengl.getRaw();
        pgraphics.colorMode(1);
        pgraphics.noStroke();
        pgraphics.beginShape(9);
        float af[] = tessGeo.polyVertices;
        int ai[] = tessGeo.polyColors;
        float af1[] = tessGeo.polyTexCoords;
        short aword0[] = tessGeo.polyIndices;
        PGraphicsOpenGL.IndexCache indexcache = tessGeo.polyIndexCache;
        int i = firstPolyIndexCache;
        do
        {
            if(i > lastPolyIndexCache)
                break;
            int j = indexcache.indexOffset[i];
            int k = indexcache.indexCount[i];
            int l = indexcache.vertexOffset[i];
            int i1 = j / 3;
            while(i1 < (j + k) / 3) 
            {
                int j1 = l + aword0[i1 * 3 + 0];
                int k1 = l + aword0[i1 * 3 + 1];
                int l1 = l + aword0[i1 * 3 + 2];
                float af2[] = new float[4];
                float[] _tmp = af2;
                af2[0] = 0.0F;
                af2[1] = 0.0F;
                af2[2] = 0.0F;
                af2[3] = 0.0F;
                float af3[] = new float[4];
                float[] _tmp1 = af3;
                af3[0] = 0.0F;
                af3[1] = 0.0F;
                af3[2] = 0.0F;
                af3[3] = 0.0F;
                float af4[] = new float[4];
                float[] _tmp2 = af4;
                af4[0] = 0.0F;
                af4[1] = 0.0F;
                af4[2] = 0.0F;
                af4[3] = 0.0F;
                float af5[] = new float[4];
                float[] _tmp3 = af5;
                af5[0] = 0.0F;
                af5[1] = 0.0F;
                af5[2] = 0.0F;
                af5[3] = 0.0F;
                float af6[] = new float[4];
                float[] _tmp4 = af6;
                af6[0] = 0.0F;
                af6[1] = 0.0F;
                af6[2] = 0.0F;
                af6[3] = 0.0F;
                float af7[] = new float[4];
                float[] _tmp5 = af7;
                af7[0] = 0.0F;
                af7[1] = 0.0F;
                af7[2] = 0.0F;
                af7[3] = 0.0F;
                int i2 = PGL.nativeToJavaARGB(ai[j1]);
                int j2 = PGL.nativeToJavaARGB(ai[k1]);
                int k2 = PGL.nativeToJavaARGB(ai[l1]);
                PApplet.arrayCopy(af, j1 * 4, af2, 0, 4);
                PApplet.arrayCopy(af, k1 * 4, af3, 0, 4);
                PApplet.arrayCopy(af, l1 * 4, af4, 0, 4);
                pgraphicsopengl.modelview.mult(af2, af5);
                pgraphicsopengl.modelview.mult(af3, af6);
                pgraphicsopengl.modelview.mult(af4, af7);
                if(pimage != null)
                {
                    pgraphics.texture(pimage);
                    if(pgraphics.is3D())
                    {
                        pgraphics.fill(i2);
                        pgraphics.vertex(af5[0], af5[1], af5[2], af1[j1 * 2 + 0], af1[j1 * 2 + 1]);
                        pgraphics.fill(j2);
                        pgraphics.vertex(af6[0], af6[1], af6[2], af1[k1 * 2 + 0], af1[k1 * 2 + 1]);
                        pgraphics.fill(k2);
                        pgraphics.vertex(af7[0], af7[1], af7[2], af1[l1 * 2 + 0], af1[l1 * 2 + 1]);
                    } else
                    if(pgraphics.is2D())
                    {
                        float f = pgraphicsopengl.screenXImpl(af5[0], af5[1], af5[2], af5[3]);
                        float f2 = pgraphicsopengl.screenYImpl(af5[0], af5[1], af5[2], af5[3]);
                        float f4 = pgraphicsopengl.screenXImpl(af6[0], af6[1], af6[2], af6[3]);
                        float f6 = pgraphicsopengl.screenYImpl(af6[0], af6[1], af6[2], af6[3]);
                        float f8 = pgraphicsopengl.screenXImpl(af7[0], af7[1], af7[2], af7[3]);
                        float f10 = pgraphicsopengl.screenYImpl(af7[0], af7[1], af7[2], af7[3]);
                        pgraphics.fill(i2);
                        pgraphics.vertex(f, f2, af1[j1 * 2 + 0], af1[j1 * 2 + 1]);
                        pgraphics.fill(j2);
                        pgraphics.vertex(f4, f6, af1[k1 * 2 + 0], af1[k1 * 2 + 1]);
                        pgraphics.fill(j2);
                        pgraphics.vertex(f8, f10, af1[l1 * 2 + 0], af1[l1 * 2 + 1]);
                    }
                } else
                if(pgraphics.is3D())
                {
                    pgraphics.fill(i2);
                    pgraphics.vertex(af5[0], af5[1], af5[2]);
                    pgraphics.fill(j2);
                    pgraphics.vertex(af6[0], af6[1], af6[2]);
                    pgraphics.fill(k2);
                    pgraphics.vertex(af7[0], af7[1], af7[2]);
                } else
                if(pgraphics.is2D())
                {
                    float f7 = pgraphicsopengl.screenXImpl(af5[0], af5[1], af5[2], af5[3]);
                    float f9 = pgraphicsopengl.screenYImpl(af5[0], af5[1], af5[2], af5[3]);
                    float f5 = pgraphicsopengl.screenXImpl(af6[0], af6[1], af6[2], af6[3]);
                    float f11 = pgraphicsopengl.screenYImpl(af6[0], af6[1], af6[2], af6[3]);
                    float f1 = pgraphicsopengl.screenXImpl(af7[0], af7[1], af7[2], af7[3]);
                    float f3 = pgraphicsopengl.screenYImpl(af7[0], af7[1], af7[2], af7[3]);
                    pgraphics.fill(i2);
                    pgraphics.vertex(f7, f9);
                    pgraphics.fill(j2);
                    pgraphics.vertex(f5, f11);
                    pgraphics.fill(k2);
                    pgraphics.vertex(f1, f3);
                }
                i1++;
            }
            i++;
        } while(true);
        pgraphics.endShape();
    }

    public void removeChild(int i)
    {
        super.removeChild(i);
        strokedTexture(false);
        untexChild(false);
        markForTessellation();
    }

    protected void removeTexture(PImage pimage, PShapeOpenGL pshapeopengl)
    {
        boolean flag = false;
        if(textures != null && textures.contains(pimage)) goto _L2; else goto _L1
_L1:
        return;
_L2:
        int i = 0;
_L5:
        boolean flag1;
        PShapeOpenGL pshapeopengl1;
        flag1 = flag;
        if(i >= childCount)
            break; /* Loop/switch isn't completed */
        pshapeopengl1 = (PShapeOpenGL)children[i];
          goto _L3
_L7:
        i++;
        if(true) goto _L5; else goto _L4
_L3:
        if(pshapeopengl1 == pshapeopengl || !pshapeopengl1.hasTexture(pimage)) goto _L7; else goto _L6
_L6:
        flag1 = true;
_L4:
        if(!flag1)
        {
            textures.remove(pimage);
            if(textures.size() == 0)
                textures = null;
        }
        if(parent != null)
            ((PShapeOpenGL)parent).removeTexture(pimage, this);
        if(true) goto _L1; else goto _L8
_L8:
    }

    protected void render(PGraphicsOpenGL pgraphicsopengl, PImage pimage)
    {
        if(root == null)
            throw new RuntimeException("Error rendering PShapeOpenGL, root shape is null");
        if(hasPolys)
        {
            renderPolys(pgraphicsopengl, pimage);
            if(pgraphicsopengl.haveRaw())
                rawPolys(pgraphicsopengl, pimage);
        }
        if(is3D())
        {
            if(hasLines)
            {
                renderLines(pgraphicsopengl);
                if(pgraphicsopengl.haveRaw())
                    rawLines(pgraphicsopengl);
            }
            if(hasPoints)
            {
                renderPoints(pgraphicsopengl);
                if(pgraphicsopengl.haveRaw())
                    rawPoints(pgraphicsopengl);
            }
        }
    }

    protected void renderLines(PGraphicsOpenGL pgraphicsopengl)
    {
        PShader pshader = pgraphicsopengl.getLineShader();
        pshader.bind();
        pgraphicsopengl = tessGeo.lineIndexCache;
        for(int i = firstLineIndexCache; i <= lastLineIndexCache; i++)
        {
            int j = ((PGraphicsOpenGL.IndexCache) (pgraphicsopengl)).indexOffset[i];
            int k = ((PGraphicsOpenGL.IndexCache) (pgraphicsopengl)).indexCount[i];
            int l = ((PGraphicsOpenGL.IndexCache) (pgraphicsopengl)).vertexOffset[i];
            pshader.setVertexAttribute(root.bufLineVertex.glId, 4, PGL.FLOAT, 0, l * 4 * PGL.SIZEOF_FLOAT);
            pshader.setColorAttribute(root.bufLineColor.glId, 4, PGL.UNSIGNED_BYTE, 0, l * 4 * PGL.SIZEOF_BYTE);
            pshader.setLineAttribute(root.bufLineAttrib.glId, 4, PGL.FLOAT, 0, l * 4 * PGL.SIZEOF_FLOAT);
            pshader.draw(root.bufLineIndex.glId, k, j);
        }

        pshader.unbind();
    }

    protected void renderPoints(PGraphicsOpenGL pgraphicsopengl)
    {
        PShader pshader = pgraphicsopengl.getPointShader();
        pshader.bind();
        pgraphicsopengl = tessGeo.pointIndexCache;
        for(int i = firstPointIndexCache; i <= lastPointIndexCache; i++)
        {
            int j = ((PGraphicsOpenGL.IndexCache) (pgraphicsopengl)).indexOffset[i];
            int k = ((PGraphicsOpenGL.IndexCache) (pgraphicsopengl)).indexCount[i];
            int l = ((PGraphicsOpenGL.IndexCache) (pgraphicsopengl)).vertexOffset[i];
            pshader.setVertexAttribute(root.bufPointVertex.glId, 4, PGL.FLOAT, 0, l * 4 * PGL.SIZEOF_FLOAT);
            pshader.setColorAttribute(root.bufPointColor.glId, 4, PGL.UNSIGNED_BYTE, 0, l * 4 * PGL.SIZEOF_BYTE);
            pshader.setPointAttribute(root.bufPointAttrib.glId, 2, PGL.FLOAT, 0, l * 2 * PGL.SIZEOF_FLOAT);
            pshader.draw(root.bufPointIndex.glId, k, j);
        }

        pshader.unbind();
    }

    protected void renderPolys(PGraphicsOpenGL pgraphicsopengl, PImage pimage)
    {
        boolean flag;
        int i;
        boolean flag3;
        PImage pimage1;
        int j;
        int k;
        boolean flag1;
        boolean flag2;
        PGraphicsOpenGL.IndexCache indexcache;
        boolean flag4;
        Object obj;
        int l;
        PGraphicsOpenGL.VertexAttribute vertexattribute;
        if(pgraphicsopengl.polyShader != null)
            flag = true;
        else
            flag = false;
        if(flag)
            flag1 = pgraphicsopengl.polyShader.accessNormals();
        else
            flag1 = false;
        if(flag)
            flag2 = pgraphicsopengl.polyShader.accessTexCoords();
        else
            flag2 = false;
        if(pimage != null)
            pimage = pgraphicsopengl.getTexture(pimage);
        else
            pimage = null;
        indexcache = tessGeo.polyIndexCache;
        i = firstPolyIndexCache;
        flag3 = false;
        pimage1 = null;
        flag = false;
        if(i > lastPolyIndexCache) goto _L2; else goto _L1
_L1:
        if(is3D() || pimage != null && (firstLineIndexCache == -1 || i < firstLineIndexCache) && (firstPointIndexCache == -1 || i < firstPointIndexCache))
        {
            if(flag3)
                break MISSING_BLOCK_LABEL_781;
            flag4 = pgraphicsopengl.lights;
            boolean flag5;
            if(pimage != null)
                flag5 = true;
            else
                flag5 = false;
            obj = pgraphicsopengl.getPolyShader(flag4, flag5);
            ((PShader) (obj)).bind();
            flag3 = flag;
            flag = true;
            pimage1 = pimage;
            pimage = ((PImage) (obj));
        } else
        {
            if(flag)
                break MISSING_BLOCK_LABEL_781;
            PImage pimage2 = pimage;
            if(pimage != null)
            {
                pimage.unbind();
                pimage2 = null;
            }
            if(pimage1 != null && pimage1.bound())
                pimage1.unbind();
            pimage = pgraphicsopengl.getPolyShader(pgraphicsopengl.lights, false);
            pimage.bind();
            flag3 = true;
            flag = false;
            pimage1 = pimage2;
        }
_L4:
        j = indexcache.indexOffset[i];
        k = indexcache.indexCount[i];
        l = indexcache.vertexOffset[i];
        pimage.setVertexAttribute(root.bufPolyVertex.glId, 4, PGL.FLOAT, 0, l * 4 * PGL.SIZEOF_FLOAT);
        pimage.setColorAttribute(root.bufPolyColor.glId, 4, PGL.UNSIGNED_BYTE, 0, l * 4 * PGL.SIZEOF_BYTE);
        if(pgraphicsopengl.lights)
        {
            pimage.setNormalAttribute(root.bufPolyNormal.glId, 3, PGL.FLOAT, 0, l * 3 * PGL.SIZEOF_FLOAT);
            pimage.setAmbientAttribute(root.bufPolyAmbient.glId, 4, PGL.UNSIGNED_BYTE, 0, l * 4 * PGL.SIZEOF_BYTE);
            pimage.setSpecularAttribute(root.bufPolySpecular.glId, 4, PGL.UNSIGNED_BYTE, 0, l * 4 * PGL.SIZEOF_BYTE);
            pimage.setEmissiveAttribute(root.bufPolyEmissive.glId, 4, PGL.UNSIGNED_BYTE, 0, l * 4 * PGL.SIZEOF_BYTE);
            pimage.setShininessAttribute(root.bufPolyShininess.glId, 1, PGL.FLOAT, 0, PGL.SIZEOF_FLOAT * l);
        }
        if(pgraphicsopengl.lights || flag1)
            pimage.setNormalAttribute(root.bufPolyNormal.glId, 3, PGL.FLOAT, 0, l * 3 * PGL.SIZEOF_FLOAT);
        if(pimage1 != null || flag2)
        {
            pimage.setTexcoordAttribute(root.bufPolyTexcoord.glId, 2, PGL.FLOAT, 0, l * 2 * PGL.SIZEOF_FLOAT);
            pimage.setTexture(pimage1);
        }
        obj = polyAttribs.values().iterator();
        do
        {
            if(!((Iterator) (obj)).hasNext())
                break;
            vertexattribute = (PGraphicsOpenGL.VertexAttribute)((Iterator) (obj)).next();
            if(vertexattribute.active(pimage))
            {
                vertexattribute.bind(pgl);
                pimage.setAttributeVBO(vertexattribute.glLoc, vertexattribute.buf.glId, vertexattribute.tessSize, vertexattribute.type, vertexattribute.isColor(), 0, vertexattribute.sizeInBytes(l));
            }
        } while(true);
        pimage.draw(root.bufPolyIndex.glId, k, j);
        int i1 = i + 1;
        i = ((flag3) ? 1 : 0);
        PImage pimage3 = pimage1;
        flag3 = flag;
        pimage1 = pimage;
        flag = i;
        pimage = pimage3;
        i = i1;
        break MISSING_BLOCK_LABEL_68;
_L2:
        pimage = polyAttribs.values().iterator();
        do
        {
            if(!pimage.hasNext())
                break;
            pgraphicsopengl = (PGraphicsOpenGL.VertexAttribute)pimage.next();
            if(pgraphicsopengl.active(pimage1))
                pgraphicsopengl.unbind(pgl);
        } while(true);
        if(pimage1 != null && pimage1.bound())
            pimage1.unbind();
        return;
        boolean flag6 = flag3;
        PImage pimage4 = pimage;
        pimage = pimage1;
        flag3 = flag;
        flag = flag6;
        pimage1 = pimage4;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void resetMatrix()
    {
        if(shapeCreated && matrix != null && transformStack != null)
        {
            if(family == 0)
                updateTessellation();
            if(tessellated)
            {
                PMatrix pmatrix = popTransform();
                while(pmatrix != null) 
                {
                    if(pmatrix.invert())
                        applyMatrixImpl(pmatrix);
                    else
                        PGraphics.showWarning("Transformation applied on the shape cannot be inverted");
                    pmatrix = popTransform();
                }
            }
            matrix.reset();
            transformStack.clear();
        }
    }

    protected void restoreBezierVertexSettings()
    {
        if(savedBezierDetail != bezierDetail)
            pg.bezierDetail(savedBezierDetail);
    }

    protected void restoreCurveVertexSettings()
    {
        if(savedCurveDetail != curveDetail)
            pg.curveDetail(savedCurveDetail);
        if(savedCurveTightness != curveTightness)
            pg.curveTightness(savedCurveTightness);
    }

    public void rotate(float f)
    {
        transform(1, new float[] {
            f
        });
    }

    public void rotate(float f, float f1, float f2, float f3)
    {
        transform(1, new float[] {
            f, f1, f2, f3
        });
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
        transform(1, new float[] {
            f
        });
    }

    protected void saveBezierVertexSettings()
    {
        savedBezierDetail = pg.bezierDetail;
        if(pg.bezierDetail != bezierDetail)
            pg.bezierDetail(bezierDetail);
    }

    protected void saveCurveVertexSettings()
    {
        savedCurveDetail = pg.curveDetail;
        savedCurveTightness = pg.curveTightness;
        if(pg.curveDetail != curveDetail)
            pg.curveDetail(curveDetail);
        if(pg.curveTightness != curveTightness)
            pg.curveTightness(curveTightness);
    }

    public void scale(float f)
    {
        if(is3D)
            transform(2, new float[] {
                f, f, f
            });
        else
            transform(2, new float[] {
                f, f
            });
    }

    public void scale(float f, float f1)
    {
        if(is3D)
            transform(2, new float[] {
                f, f1, 1.0F
            });
        else
            transform(2, new float[] {
                f, f1
            });
    }

    public void scale(float f, float f1, float f2)
    {
        transform(2, new float[] {
            f, f1, f2
        });
    }

    protected void scaleTextureUV(float f, float f1)
    {
        if(!PGraphicsOpenGL.same(f, 1.0F) || !PGraphicsOpenGL.same(f1, 1.0F))
        {
            for(int i = 0; i < inGeo.vertexCount; i++)
            {
                f1 = inGeo.texcoords[i * 2 + 0];
                float f2 = inGeo.texcoords[i * 2 + 1];
                inGeo.texcoords[i * 2 + 0] = PApplet.min(1.0F, f1 * f);
                inGeo.texcoords[i * 2 + 1] = PApplet.min(1.0F, f2 * f);
            }

            if(shapeCreated && tessellated && hasPolys)
            {
                int j;
                int k;
                if(is3D())
                    j = lastPolyVertex + 1;
                else
                if(is2D())
                {
                    j = lastPolyVertex + 1;
                    if(-1 < firstLineVertex)
                        j = firstLineVertex;
                    if(-1 < firstPointVertex)
                        j = firstPointVertex;
                } else
                {
                    j = 0;
                }
                for(k = firstLineVertex; k < j; k++)
                {
                    f1 = tessGeo.polyTexCoords[k * 2 + 0];
                    float f3 = tessGeo.polyTexCoords[k * 2 + 1];
                    tessGeo.polyTexCoords[k * 2 + 0] = PApplet.min(1.0F, f1 * f);
                    tessGeo.polyTexCoords[k * 2 + 1] = PApplet.min(1.0F, f3 * f);
                }

                root.setModifiedPolyTexCoords(firstPolyVertex, j - 1);
            }
        }
    }

    public void setAmbient(int i)
    {
        if(openShape)
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setAmbient()"
            });
        else
        if(family == 0)
        {
            int j = 0;
            while(j < childCount) 
            {
                ((PShapeOpenGL)children[j]).setAmbient(i);
                j++;
            }
        } else
        {
            setAmbientImpl(i);
        }
    }

    public void setAmbient(int i, int j)
    {
        if(openShape)
        {
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setAmbient()"
            });
        } else
        {
            inGeo.ambient[i] = PGL.javaToNativeARGB(j);
            markForTessellation();
            setAmbient = true;
        }
    }

    protected void setAmbientImpl(int i)
    {
        if(ambientColor != i) goto _L2; else goto _L1
_L1:
        return;
_L2:
        ambientColor = i;
        Arrays.fill(inGeo.ambient, 0, inGeo.vertexCount, PGL.javaToNativeARGB(ambientColor));
        if(shapeCreated && tessellated && hasPolys)
        {
            if(!is3D())
                break; /* Loop/switch isn't completed */
            Arrays.fill(tessGeo.polyAmbient, firstPolyVertex, lastPolyVertex + 1, PGL.javaToNativeARGB(ambientColor));
            root.setModifiedPolyAmbient(firstPolyVertex, lastPolyVertex);
        }
_L5:
        setAmbient = true;
        if(true) goto _L1; else goto _L3
_L3:
        if(!is2D()) goto _L5; else goto _L4
_L4:
        i = lastPolyVertex + 1;
        if(-1 < firstLineVertex)
            i = firstLineVertex;
        if(-1 < firstPointVertex)
            i = firstPointVertex;
        Arrays.fill(tessGeo.polyAmbient, firstPolyVertex, i, PGL.javaToNativeARGB(ambientColor));
        root.setModifiedPolyColors(firstPolyVertex, i - 1);
          goto _L5
    }

    public transient void setAttrib(String s, int i, float af[])
    {
        int j = 0;
        if(openShape)
        {
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setNormal()"
            });
        } else
        {
            PGraphicsOpenGL.VertexAttribute vertexattribute = (PGraphicsOpenGL.VertexAttribute)polyAttribs.get(s);
            s = (float[])inGeo.fattribs.get(s);
            for(; j < af.length; j++)
                s[vertexattribute.size * i + 0] = af[j];

            markForTessellation();
        }
    }

    public transient void setAttrib(String s, int i, int ai[])
    {
        int j = 0;
        if(openShape)
        {
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setNormal()"
            });
        } else
        {
            PGraphicsOpenGL.VertexAttribute vertexattribute = (PGraphicsOpenGL.VertexAttribute)polyAttribs.get(s);
            s = (int[])inGeo.iattribs.get(s);
            for(; j < ai.length; j++)
                s[vertexattribute.size * i + 0] = ai[j];

            markForTessellation();
        }
    }

    public transient void setAttrib(String s, int i, boolean aflag[])
    {
        if(openShape)
        {
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setNormal()"
            });
        } else
        {
            PGraphicsOpenGL.VertexAttribute vertexattribute = (PGraphicsOpenGL.VertexAttribute)polyAttribs.get(s);
            s = (byte[])inGeo.battribs.get(s);
            int j = 0;
            while(j < aflag.length) 
            {
                int k = vertexattribute.size;
                int l;
                if(aflag[j])
                    l = 1;
                else
                    l = 0;
                s[k * i + 0] = (byte)l;
                j++;
            }
            markForTessellation();
        }
    }

    public void setEmissive(int i)
    {
        if(openShape)
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setEmissive()"
            });
        else
        if(family == 0)
        {
            int j = 0;
            while(j < childCount) 
            {
                ((PShapeOpenGL)children[j]).setEmissive(i);
                j++;
            }
        } else
        {
            setEmissiveImpl(i);
        }
    }

    public void setEmissive(int i, int j)
    {
        if(openShape)
        {
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setEmissive()"
            });
        } else
        {
            inGeo.emissive[i] = PGL.javaToNativeARGB(j);
            markForTessellation();
        }
    }

    protected void setEmissiveImpl(int i)
    {
        if(emissiveColor != i) goto _L2; else goto _L1
_L1:
        return;
_L2:
        emissiveColor = i;
        Arrays.fill(inGeo.emissive, 0, inGeo.vertexCount, PGL.javaToNativeARGB(emissiveColor));
        if(shapeCreated && tessellated && tessGeo.polyVertexCount > 0)
            if(is3D())
            {
                Arrays.fill(tessGeo.polyEmissive, firstPolyVertex, lastPolyVertex + 1, PGL.javaToNativeARGB(emissiveColor));
                root.setModifiedPolyEmissive(firstPolyVertex, lastPolyVertex);
            } else
            if(is2D())
            {
                i = lastPolyVertex + 1;
                if(-1 < firstLineVertex)
                    i = firstLineVertex;
                if(-1 < firstPointVertex)
                    i = firstPointVertex;
                Arrays.fill(tessGeo.polyEmissive, firstPolyVertex, i, PGL.javaToNativeARGB(emissiveColor));
                root.setModifiedPolyColors(firstPolyVertex, i - 1);
            }
        if(true) goto _L1; else goto _L3
_L3:
    }

    public void setFill(int i)
    {
        if(openShape)
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setFill()"
            });
        else
        if(family == 0)
        {
            int j = 0;
            while(j < childCount) 
            {
                ((PShapeOpenGL)children[j]).setFill(i);
                j++;
            }
        } else
        {
            setFillImpl(i);
        }
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
        if(image == null)
        {
            inGeo.colors[i] = PGL.javaToNativeARGB(j);
            markForTessellation();
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void setFill(boolean flag)
    {
        if(openShape)
        {
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setFill()"
            });
        } else
        {
            if(family == 0)
            {
                for(int i = 0; i < childCount; i++)
                    ((PShapeOpenGL)children[i]).setFill(flag);

            } else
            if(fill != flag)
                markForTessellation();
            fill = flag;
        }
    }

    protected void setFillImpl(int i)
    {
        if(fillColor != i) goto _L2; else goto _L1
_L1:
        return;
_L2:
        fillColor = i;
        if(image == null)
        {
            Arrays.fill(inGeo.colors, 0, inGeo.vertexCount, PGL.javaToNativeARGB(fillColor));
            if(shapeCreated && tessellated && hasPolys)
            {
                if(!is3D())
                    break; /* Loop/switch isn't completed */
                Arrays.fill(tessGeo.polyColors, firstPolyVertex, lastPolyVertex + 1, PGL.javaToNativeARGB(fillColor));
                root.setModifiedPolyColors(firstPolyVertex, lastPolyVertex);
            }
        }
_L5:
        if(!setAmbient)
        {
            setAmbientImpl(i);
            setAmbient = false;
        }
        if(true) goto _L1; else goto _L3
_L3:
        if(!is2D()) goto _L5; else goto _L4
_L4:
        int j = lastPolyVertex + 1;
        if(-1 < firstLineVertex)
            j = firstLineVertex;
        if(-1 < firstPointVertex)
            j = firstPointVertex;
        Arrays.fill(tessGeo.polyColors, firstPolyVertex, j, PGL.javaToNativeARGB(fillColor));
        root.setModifiedPolyColors(firstPolyVertex, j - 1);
          goto _L5
    }

    protected void setFirstStrokeVertex(int i, int j)
    {
        if(i == firstLineIndexCache && firstLineVertex == -1)
        {
            lastLineVertex = j;
            firstLineVertex = j;
        }
        if(i == firstPointIndexCache && firstPointVertex == -1)
        {
            lastPointVertex = j;
            firstPointVertex = j;
        }
    }

    protected void setLastStrokeVertex(int i)
    {
        if(-1 < lastLineVertex)
            lastLineVertex = i;
        if(-1 < lastPointVertex)
            lastPointVertex = lastPointVertex + i;
    }

    protected void setModifiedLineAttributes(int i, int j)
    {
        if(i < firstModifiedLineAttribute)
            firstModifiedLineAttribute = i;
        if(j > lastModifiedLineAttribute)
            lastModifiedLineAttribute = j;
        modifiedLineAttributes = true;
        modified = true;
    }

    protected void setModifiedLineColors(int i, int j)
    {
        if(i < firstModifiedLineColor)
            firstModifiedLineColor = i;
        if(j > lastModifiedLineColor)
            lastModifiedLineColor = j;
        modifiedLineColors = true;
        modified = true;
    }

    protected void setModifiedLineVertices(int i, int j)
    {
        if(i < firstModifiedLineVertex)
            firstModifiedLineVertex = i;
        if(j > lastModifiedLineVertex)
            lastModifiedLineVertex = j;
        modifiedLineVertices = true;
        modified = true;
    }

    protected void setModifiedPointAttributes(int i, int j)
    {
        if(i < firstModifiedPointAttribute)
            firstModifiedPointAttribute = i;
        if(j > lastModifiedPointAttribute)
            lastModifiedPointAttribute = j;
        modifiedPointAttributes = true;
        modified = true;
    }

    protected void setModifiedPointColors(int i, int j)
    {
        if(i < firstModifiedPointColor)
            firstModifiedPointColor = i;
        if(j > lastModifiedPointColor)
            lastModifiedPointColor = j;
        modifiedPointColors = true;
        modified = true;
    }

    protected void setModifiedPointVertices(int i, int j)
    {
        if(i < firstModifiedPointVertex)
            firstModifiedPointVertex = i;
        if(j > lastModifiedPointVertex)
            lastModifiedPointVertex = j;
        modifiedPointVertices = true;
        modified = true;
    }

    protected void setModifiedPolyAmbient(int i, int j)
    {
        if(i < firstModifiedPolyAmbient)
            firstModifiedPolyAmbient = i;
        if(j > lastModifiedPolyAmbient)
            lastModifiedPolyAmbient = j;
        modifiedPolyAmbient = true;
        modified = true;
    }

    protected void setModifiedPolyAttrib(PGraphicsOpenGL.VertexAttribute vertexattribute, int i, int j)
    {
        if(i < vertexattribute.firstModified)
            vertexattribute.firstModified = i;
        if(j > vertexattribute.lastModified)
            vertexattribute.lastModified = j;
        vertexattribute.modified = true;
        modified = true;
    }

    protected void setModifiedPolyColors(int i, int j)
    {
        if(i < firstModifiedPolyColor)
            firstModifiedPolyColor = i;
        if(j > lastModifiedPolyColor)
            lastModifiedPolyColor = j;
        modifiedPolyColors = true;
        modified = true;
    }

    protected void setModifiedPolyEmissive(int i, int j)
    {
        if(i < firstModifiedPolyEmissive)
            firstModifiedPolyEmissive = i;
        if(j > lastModifiedPolyEmissive)
            lastModifiedPolyEmissive = j;
        modifiedPolyEmissive = true;
        modified = true;
    }

    protected void setModifiedPolyNormals(int i, int j)
    {
        if(i < firstModifiedPolyNormal)
            firstModifiedPolyNormal = i;
        if(j > lastModifiedPolyNormal)
            lastModifiedPolyNormal = j;
        modifiedPolyNormals = true;
        modified = true;
    }

    protected void setModifiedPolyShininess(int i, int j)
    {
        if(i < firstModifiedPolyShininess)
            firstModifiedPolyShininess = i;
        if(j > lastModifiedPolyShininess)
            lastModifiedPolyShininess = j;
        modifiedPolyShininess = true;
        modified = true;
    }

    protected void setModifiedPolySpecular(int i, int j)
    {
        if(i < firstModifiedPolySpecular)
            firstModifiedPolySpecular = i;
        if(j > lastModifiedPolySpecular)
            lastModifiedPolySpecular = j;
        modifiedPolySpecular = true;
        modified = true;
    }

    protected void setModifiedPolyTexCoords(int i, int j)
    {
        if(i < firstModifiedPolyTexcoord)
            firstModifiedPolyTexcoord = i;
        if(j > lastModifiedPolyTexcoord)
            lastModifiedPolyTexcoord = j;
        modifiedPolyTexCoords = true;
        modified = true;
    }

    protected void setModifiedPolyVertices(int i, int j)
    {
        if(i < firstModifiedPolyVertex)
            firstModifiedPolyVertex = i;
        if(j > lastModifiedPolyVertex)
            lastModifiedPolyVertex = j;
        modifiedPolyVertices = true;
        modified = true;
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
            inGeo.normals[i * 3 + 0] = f;
            inGeo.normals[i * 3 + 1] = f1;
            inGeo.normals[i * 3 + 2] = f2;
            markForTessellation();
        }
    }

    public void setParams(float af[])
    {
        if(family != 1)
        {
            PGraphics.showWarning("Parameters can only be set to PRIMITIVE shapes");
        } else
        {
            super.setParams(af);
            markForTessellation();
            shapeCreated = true;
        }
    }

    public void setPath(int i, float af[][], int j, int ai[])
    {
        if(family != 2)
        {
            PGraphics.showWarning("Vertex coordinates and codes can only be set to PATH shapes");
        } else
        {
            super.setPath(i, af, j, ai);
            markForTessellation();
            shapeCreated = true;
        }
    }

    public void setShininess(float f)
    {
        if(openShape)
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setShininess()"
            });
        else
        if(family == 0)
        {
            int i = 0;
            while(i < childCount) 
            {
                ((PShapeOpenGL)children[i]).setShininess(f);
                i++;
            }
        } else
        {
            setShininessImpl(f);
        }
    }

    public void setShininess(int i, float f)
    {
        if(openShape)
        {
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setShininess()"
            });
        } else
        {
            inGeo.shininess[i] = f;
            markForTessellation();
        }
    }

    protected void setShininessImpl(float f)
    {
        if(!PGraphicsOpenGL.same(shininess, f)) goto _L2; else goto _L1
_L1:
        return;
_L2:
        shininess = f;
        Arrays.fill(inGeo.shininess, 0, inGeo.vertexCount, f);
        if(shapeCreated && tessellated && hasPolys)
            if(is3D())
            {
                Arrays.fill(tessGeo.polyShininess, firstPolyVertex, lastPolyVertex + 1, f);
                root.setModifiedPolyShininess(firstPolyVertex, lastPolyVertex);
            } else
            if(is2D())
            {
                int i = lastPolyVertex + 1;
                if(-1 < firstLineVertex)
                    i = firstLineVertex;
                if(-1 < firstPointVertex)
                    i = firstPointVertex;
                Arrays.fill(tessGeo.polyShininess, firstPolyVertex, i, f);
                root.setModifiedPolyColors(firstPolyVertex, i - 1);
            }
        if(true) goto _L1; else goto _L3
_L3:
    }

    public void setSpecular(int i)
    {
        if(openShape)
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setSpecular()"
            });
        else
        if(family == 0)
        {
            int j = 0;
            while(j < childCount) 
            {
                ((PShapeOpenGL)children[j]).setSpecular(i);
                j++;
            }
        } else
        {
            setSpecularImpl(i);
        }
    }

    public void setSpecular(int i, int j)
    {
        if(openShape)
        {
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setSpecular()"
            });
        } else
        {
            inGeo.specular[i] = PGL.javaToNativeARGB(j);
            markForTessellation();
        }
    }

    protected void setSpecularImpl(int i)
    {
        if(specularColor != i) goto _L2; else goto _L1
_L1:
        return;
_L2:
        specularColor = i;
        Arrays.fill(inGeo.specular, 0, inGeo.vertexCount, PGL.javaToNativeARGB(specularColor));
        if(shapeCreated && tessellated && hasPolys)
            if(is3D())
            {
                Arrays.fill(tessGeo.polySpecular, firstPolyVertex, lastPolyVertex + 1, PGL.javaToNativeARGB(specularColor));
                root.setModifiedPolySpecular(firstPolyVertex, lastPolyVertex);
            } else
            if(is2D())
            {
                i = lastPolyVertex + 1;
                if(-1 < firstLineVertex)
                    i = firstLineVertex;
                if(-1 < firstPointVertex)
                    i = firstPointVertex;
                Arrays.fill(tessGeo.polySpecular, firstPolyVertex, i, PGL.javaToNativeARGB(specularColor));
                root.setModifiedPolyColors(firstPolyVertex, i - 1);
            }
        if(true) goto _L1; else goto _L3
_L3:
    }

    public void setStroke(int i)
    {
        if(openShape)
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setStroke()"
            });
        else
        if(family == 0)
        {
            int j = 0;
            while(j < childCount) 
            {
                ((PShapeOpenGL)children[j]).setStroke(i);
                j++;
            }
        } else
        {
            setStrokeImpl(i);
        }
    }

    public void setStroke(int i, int j)
    {
        if(openShape)
        {
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setStroke()"
            });
        } else
        {
            inGeo.strokeColors[i] = PGL.javaToNativeARGB(j);
            markForTessellation();
        }
    }

    public void setStroke(boolean flag)
    {
        if(openShape)
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setStroke()"
            });
        else
        if(family == 0)
        {
            for(int i = 0; i < childCount; i++)
                ((PShapeOpenGL)children[i]).setStroke(flag);

            stroke = flag;
        } else
        {
            setStrokeImpl(flag);
        }
    }

    public void setStrokeCap(int i)
    {
        if(openShape)
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setStrokeCap()"
            });
        else
        if(family == 0)
        {
            int j = 0;
            while(j < childCount) 
            {
                ((PShapeOpenGL)children[j]).setStrokeCap(i);
                j++;
            }
        } else
        {
            if(is2D() && strokeCap != i)
                markForTessellation();
            strokeCap = i;
        }
    }

    protected void setStrokeImpl(int i)
    {
        if(strokeColor != i) goto _L2; else goto _L1
_L1:
        return;
_L2:
        strokeColor = i;
        Arrays.fill(inGeo.strokeColors, 0, inGeo.vertexCount, PGL.javaToNativeARGB(strokeColor));
        if(!shapeCreated || !tessellated || !hasLines && !hasPoints)
            continue; /* Loop/switch isn't completed */
        if(hasLines)
            if(is3D())
            {
                Arrays.fill(tessGeo.lineColors, firstLineVertex, lastLineVertex + 1, PGL.javaToNativeARGB(strokeColor));
                root.setModifiedLineColors(firstLineVertex, lastLineVertex);
            } else
            if(is2D())
            {
                Arrays.fill(tessGeo.polyColors, firstLineVertex, lastLineVertex + 1, PGL.javaToNativeARGB(strokeColor));
                root.setModifiedPolyColors(firstLineVertex, lastLineVertex);
            }
        if(hasPoints)
            if(is3D())
            {
                Arrays.fill(tessGeo.pointColors, firstPointVertex, lastPointVertex + 1, PGL.javaToNativeARGB(strokeColor));
                root.setModifiedPointColors(firstPointVertex, lastPointVertex);
            } else
            if(is2D())
            {
                Arrays.fill(tessGeo.polyColors, firstPointVertex, lastPointVertex + 1, PGL.javaToNativeARGB(strokeColor));
                root.setModifiedPolyColors(firstPointVertex, lastPointVertex);
            }
        continue; /* Loop/switch isn't completed */
        if(true) goto _L1; else goto _L3
_L3:
    }

    protected void setStrokeImpl(boolean flag)
    {
        if(stroke != flag)
        {
            if(flag)
            {
                int i = strokeColor;
                strokeColor = strokeColor + 1;
                setStrokeImpl(i);
            }
            markForTessellation();
            if(is2D() && parent != null)
            {
                PShapeOpenGL pshapeopengl = (PShapeOpenGL)parent;
                boolean flag1;
                if(flag && image != null)
                    flag1 = true;
                else
                    flag1 = false;
                pshapeopengl.strokedTexture(flag1);
            }
            stroke = flag;
        }
    }

    public void setStrokeJoin(int i)
    {
        if(openShape)
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setStrokeJoin()"
            });
        else
        if(family == 0)
        {
            int j = 0;
            while(j < childCount) 
            {
                ((PShapeOpenGL)children[j]).setStrokeJoin(i);
                j++;
            }
        } else
        {
            if(is2D() && strokeJoin != i)
                markForTessellation();
            strokeJoin = i;
        }
    }

    public void setStrokeWeight(float f)
    {
        if(openShape)
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setStrokeWeight()"
            });
        else
        if(family == 0)
        {
            int i = 0;
            while(i < childCount) 
            {
                ((PShapeOpenGL)children[i]).setStrokeWeight(f);
                i++;
            }
        } else
        {
            setStrokeWeightImpl(f);
        }
    }

    public void setStrokeWeight(int i, float f)
    {
        if(openShape)
        {
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setStrokeWeight()"
            });
        } else
        {
            inGeo.strokeWeights[i] = f;
            markForTessellation();
        }
    }

    protected void setStrokeWeightImpl(float f)
    {
        if(!PGraphicsOpenGL.same(strokeWeight, f)) goto _L2; else goto _L1
_L1:
        return;
_L2:
        float f1 = strokeWeight;
        strokeWeight = f;
        Arrays.fill(inGeo.strokeWeights, 0, inGeo.vertexCount, strokeWeight);
        if(!shapeCreated || !tessellated || !hasLines && !hasPoints)
            continue; /* Loop/switch isn't completed */
        f /= f1;
        if(hasLines)
            if(is3D())
            {
                for(int i = firstLineVertex; i <= lastLineVertex; i++)
                {
                    float af[] = tessGeo.lineDirections;
                    int k = i * 4 + 3;
                    af[k] = af[k] * f;
                }

                root.setModifiedLineAttributes(firstLineVertex, lastLineVertex);
            } else
            if(is2D())
                markForTessellation();
        if(!hasPoints)
            continue; /* Loop/switch isn't completed */
        if(is3D())
        {
            for(int j = firstPointVertex; j <= lastPointVertex; j++)
            {
                float af1[] = tessGeo.pointOffsets;
                int l = j * 2 + 0;
                af1[l] = af1[l] * f;
                af1 = tessGeo.pointOffsets;
                l = j * 2 + 1;
                af1[l] = af1[l] * f;
            }

            root.setModifiedPointAttributes(firstPointVertex, lastPointVertex);
            continue; /* Loop/switch isn't completed */
        }
        if(is2D())
            markForTessellation();
        if(true) goto _L1; else goto _L3
_L3:
    }

    public void setTexture(PImage pimage)
    {
        if(openShape)
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setTexture()"
            });
        else
        if(family == 0)
        {
            int i = 0;
            while(i < childCount) 
            {
                ((PShapeOpenGL)children[i]).setTexture(pimage);
                i++;
            }
        } else
        {
            setTextureImpl(pimage);
        }
    }

    protected void setTextureImpl(PImage pimage)
    {
        float f = 1.0F;
        PImage pimage1 = image;
        image = pimage;
        if(textureMode == 2 && pimage1 != image)
        {
            float f1;
            float f2;
            float f3;
            if(image != null)
            {
                f1 = 1.0F / (float)image.width;
                f = 1.0F / (float)image.height;
            } else
            {
                f1 = 1.0F;
            }
            f2 = f;
            f3 = f1;
            if(pimage1 != null)
            {
                f3 = f1 * (float)pimage1.width;
                f2 = f * (float)pimage1.height;
            }
            scaleTextureUV(f3, f2);
        }
        if(pimage1 != pimage && parent != null)
            ((PShapeOpenGL)parent).removeTexture(pimage1, this);
        if(parent != null)
        {
            ((PShapeOpenGL)parent).addTexture(image);
            if(is2D() && stroke)
                ((PShapeOpenGL)parent).strokedTexture(true);
        }
    }

    public void setTextureMode(int i)
    {
        if(openShape)
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setTextureMode()"
            });
        else
        if(family == 0)
        {
            int j = 0;
            while(j < childCount) 
            {
                ((PShapeOpenGL)children[j]).setTextureMode(i);
                j++;
            }
        } else
        {
            setTextureModeImpl(i);
        }
    }

    protected void setTextureModeImpl(int i)
    {
        if(textureMode != i) goto _L2; else goto _L1
_L1:
        return;
_L2:
        textureMode = i;
        if(image != null)
        {
            float f = image.width;
            float f1 = image.height;
            float f2 = f1;
            float f3 = f;
            if(textureMode == 1)
            {
                f3 = 1.0F / f;
                f2 = 1.0F / f1;
            }
            scaleTextureUV(f3, f2);
        }
        if(true) goto _L1; else goto _L3
_L3:
    }

    public void setTextureUV(int i, float f, float f1)
    {
        if(openShape)
        {
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setTextureUV()"
            });
        } else
        {
            float f2 = f;
            float f3 = f1;
            if(textureMode == 2)
            {
                f2 = f;
                f3 = f1;
                if(image != null)
                {
                    f2 = f / (float)image.width;
                    f3 = f1 / (float)image.height;
                }
            }
            inGeo.texcoords[i * 2 + 0] = f2;
            inGeo.texcoords[i * 2 + 1] = f3;
            markForTessellation();
        }
    }

    public void setTint(int i)
    {
        if(openShape)
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setTint()"
            });
        else
        if(family == 0)
        {
            int j = 0;
            while(j < childCount) 
            {
                ((PShapeOpenGL)children[j]).setTint(i);
                j++;
            }
        } else
        {
            setTintImpl(i);
        }
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
        if(image != null)
        {
            inGeo.colors[i] = PGL.javaToNativeARGB(j);
            markForTessellation();
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void setTint(boolean flag)
    {
        if(openShape)
        {
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setTint()"
            });
        } else
        {
            if(family == 0)
            {
                for(int i = 0; i < childCount; i++)
                    ((PShapeOpenGL)children[i]).setTint(fill);

            } else
            if(tint && !flag)
                setTintImpl(-1);
            tint = flag;
        }
    }

    protected void setTintImpl(int i)
    {
        if(tintColor != i) goto _L2; else goto _L1
_L1:
        return;
_L2:
        tintColor = i;
        if(image != null)
        {
            Arrays.fill(inGeo.colors, 0, inGeo.vertexCount, PGL.javaToNativeARGB(tintColor));
            if(shapeCreated && tessellated && hasPolys)
                if(is3D())
                {
                    Arrays.fill(tessGeo.polyColors, firstPolyVertex, lastPolyVertex + 1, PGL.javaToNativeARGB(tintColor));
                    root.setModifiedPolyColors(firstPolyVertex, lastPolyVertex);
                } else
                if(is2D())
                {
                    i = lastPolyVertex + 1;
                    if(-1 < firstLineVertex)
                        i = firstLineVertex;
                    if(-1 < firstPointVertex)
                        i = firstPointVertex;
                    Arrays.fill(tessGeo.polyColors, firstPolyVertex, i, PGL.javaToNativeARGB(tintColor));
                    root.setModifiedPolyColors(firstPolyVertex, i - 1);
                }
        }
        if(true) goto _L1; else goto _L3
_L3:
    }

    public void setVertex(int i, float f, float f1)
    {
        setVertex(i, f, f1, 0.0F);
    }

    public void setVertex(int i, float f, float f1, float f2)
    {
        if(!openShape) goto _L2; else goto _L1
_L1:
        PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
            "setVertex()"
        });
_L4:
        return;
_L2:
        if(family != 2)
            break; /* Loop/switch isn't completed */
        if(vertexCodes != null && vertexCodeCount > 0 && vertexCodes[i] != 0)
        {
            PGraphics.showWarning("%1$s can not be called on quadratic or bezier vertices", new Object[] {
                "setVertex()"
            });
            continue; /* Loop/switch isn't completed */
        }
        vertices[i][0] = f;
        vertices[i][1] = f1;
        if(is3D)
            vertices[i][2] = f2;
_L5:
        markForTessellation();
        if(true) goto _L4; else goto _L3
_L3:
        inGeo.vertices[i * 3 + 0] = f;
        inGeo.vertices[i * 3 + 1] = f1;
        inGeo.vertices[i * 3 + 2] = f2;
          goto _L5
        if(true) goto _L4; else goto _L6
_L6:
    }

    public void setVertex(int i, PVector pvector)
    {
        if(openShape)
        {
            PGraphics.showWarning("%1$s can only be called outside beginShape() and endShape()", new Object[] {
                "setVertex()"
            });
        } else
        {
            inGeo.vertices[i * 3 + 0] = pvector.x;
            inGeo.vertices[i * 3 + 1] = pvector.y;
            inGeo.vertices[i * 3 + 2] = pvector.z;
            markForTessellation();
        }
    }

    public void solid(boolean flag)
    {
        if(family == 0)
        {
            for(int i = 0; i < childCount; i++)
                ((PShapeOpenGL)children[i]).solid(flag);

        } else
        {
            solid = flag;
        }
    }

    protected boolean startStrokedTex(int i)
    {
        boolean flag;
        if(image != null && (i == firstLineIndexCache || i == firstPointIndexCache))
            flag = true;
        else
            flag = false;
        return flag;
    }

    protected void strokedTexture(boolean flag)
    {
        strokedTexture(flag, null);
    }

    protected void strokedTexture(boolean flag, PShapeOpenGL pshapeopengl)
    {
        if(strokedTexture != flag) goto _L2; else goto _L1
_L1:
        return;
_L2:
        if(!flag)
            break; /* Loop/switch isn't completed */
        strokedTexture = true;
_L5:
        if(parent != null)
            ((PShapeOpenGL)parent).strokedTexture(flag, this);
        if(true) goto _L1; else goto _L3
_L3:
        int i;
        strokedTexture = false;
        i = 0;
_L7:
        if(i >= childCount) goto _L5; else goto _L4
_L4:
        PShapeOpenGL pshapeopengl1 = (PShapeOpenGL)children[i];
          goto _L6
_L9:
        i++;
          goto _L7
_L6:
        if(pshapeopengl1 == pshapeopengl || !pshapeopengl1.hasStrokedTexture()) goto _L9; else goto _L8
_L8:
        strokedTexture = true;
          goto _L5
    }

    protected void styles(PGraphics pgraphics)
    {
        if(pgraphics instanceof PGraphicsOpenGL)
        {
            if(pgraphics.stroke)
            {
                setStroke(true);
                setStroke(pgraphics.strokeColor);
                setStrokeWeight(pgraphics.strokeWeight);
                setStrokeCap(pgraphics.strokeCap);
                setStrokeJoin(pgraphics.strokeJoin);
            } else
            {
                setStroke(false);
            }
            if(pgraphics.fill)
            {
                setFill(true);
                setFill(pgraphics.fillColor);
            } else
            {
                setFill(false);
            }
            if(pgraphics.tint)
            {
                setTint(true);
                setTint(pgraphics.tintColor);
            }
            setAmbient(pgraphics.ambientColor);
            setSpecular(pgraphics.specularColor);
            setEmissive(pgraphics.emissiveColor);
            setShininess(pgraphics.shininess);
            if(image != null)
                setTextureMode(pgraphics.textureMode);
        } else
        {
            super.styles(pgraphics);
        }
    }

    protected void tessellate()
    {
        if(root == this && parent == null)
        {
            if(polyAttribs == null)
            {
                polyAttribs = PGraphicsOpenGL.newAttributeMap();
                collectPolyAttribs();
            }
            if(tessGeo == null)
                tessGeo = PGraphicsOpenGL.newTessGeometry(pg, polyAttribs, 1);
            tessGeo.clear();
            for(int i = 0; i < polyAttribs.size(); i++)
            {
                PGraphicsOpenGL.VertexAttribute vertexattribute = polyAttribs.get(i);
                tessGeo.initAttrib(vertexattribute);
            }

            tessellateImpl();
            tessGeo.trim();
        }
    }

    protected void tessellateArc()
    {
        float f = 0.0F;
        float f1 = 0.0F;
        float f2 = 0.0F;
        float f3 = 0.0F;
        float f4 = 0.0F;
        float f5 = 0.0F;
        int i = ellipseMode;
        boolean flag = false;
        int j = ((flag) ? 1 : 0);
        if(6 <= params.length)
        {
            float f6 = params[0];
            float f7 = params[1];
            float f9 = params[2];
            float f14 = params[3];
            float f15 = params[4];
            float f16 = params[5];
            f3 = f14;
            f2 = f9;
            f1 = f7;
            f = f6;
            f5 = f16;
            f4 = f15;
            j = ((flag) ? 1 : 0);
            if(params.length == 7)
            {
                j = (int)params[6];
                f4 = f15;
                f5 = f16;
                f = f6;
                f1 = f7;
                f2 = f9;
                f3 = f14;
            }
        }
        if(i == 1)
        {
            float f17 = f3 - f1;
            float f10 = f2 - f;
            f3 = f;
            f2 = f1;
            f1 = f10;
            f = f17;
        } else
        if(i == 2)
        {
            float f11 = f3 * 2.0F;
            float f18 = f2 * 2.0F;
            f3 = f1 - f3;
            float f8 = f - f2;
            f = f11;
            f1 = f18;
            f2 = f3;
            f3 = f8;
        } else
        if(i == 3)
        {
            float f12 = f2 / 2.0F;
            float f19 = f3 / 2.0F;
            f19 = f1 - f19;
            f12 = f - f12;
            f = f3;
            f1 = f2;
            f2 = f19;
            f3 = f12;
        } else
        {
            float f13 = f2;
            f2 = f1;
            float f20 = f;
            f = f3;
            f1 = f13;
            f3 = f20;
        }
        if(!Float.isInfinite(f4) && !Float.isInfinite(f5) && f5 > f4)
        {
            for(; f4 < 0.0F; f4 = 6.283185F + f4)
                f5 += 6.283185F;

            if(f5 - f4 > 6.283185F)
                f5 = 6.283185F + f4;
            inGeo.setMaterial(fillColor, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininess);
            inGeo.setNormal(normalX, normalY, normalZ);
            inGeo.addArc(f3, f2, f1, f, f4, f5, fill, stroke, j);
            tessellator.tessellateTriangleFan();
        }
    }

    protected void tessellateBox()
    {
        float f;
        float f2;
        float f3;
        if(params.length == 1)
        {
            f = params[0];
            float f1 = f;
            f2 = f;
            f3 = f;
            f = f1;
        } else
        if(params.length == 3)
        {
            f3 = params[0];
            f2 = params[1];
            f = params[2];
        } else
        {
            f = 0.0F;
            f2 = 0.0F;
            f3 = 0.0F;
        }
        inGeo.setMaterial(fillColor, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininess);
        inGeo.addBox(f3, f2, f, fill, stroke);
        tessellator.tessellateQuads();
    }

    protected void tessellateEllipse()
    {
        int i = ellipseMode;
        float f;
        float f1;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        float f7;
        if(4 <= params.length)
        {
            f = params[0];
            f1 = params[1];
            f2 = params[2];
            f3 = params[3];
        } else
        {
            f3 = 0.0F;
            f2 = 0.0F;
            f1 = 0.0F;
            f = 0.0F;
        }
        if(i != 1) goto _L2; else goto _L1
_L1:
        f4 = f2 - f;
        f5 = f3 - f1;
        f6 = f;
        f7 = f1;
_L4:
        if(f4 < 0.0F)
        {
            f3 = -f4;
            f6 += f4;
            f4 = f3;
        }
        if(f5 < 0.0F)
        {
            f3 = -f5;
            f7 += f5;
            f5 = f3;
        }
        inGeo.setMaterial(fillColor, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininess);
        inGeo.setNormal(normalX, normalY, normalZ);
        inGeo.addEllipse(f6, f7, f4, f5, fill, stroke);
        tessellator.tessellateTriangleFan();
        return;
_L2:
        if(i == 2)
        {
            f6 = f - f2;
            f7 = f1 - f3;
            f4 = f2 * 2.0F;
            f5 = f3 * 2.0F;
        } else
        {
            f5 = f3;
            f7 = f1;
            f4 = f2;
            f6 = f;
            if(i == 3)
            {
                f6 = f - f2 / 2.0F;
                f7 = f1 - f3 / 2.0F;
                f5 = f3;
                f4 = f2;
            }
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected void tessellateImpl()
    {
        boolean flag;
        flag = false;
        tessGeo = root.tessGeo;
        firstPolyIndexCache = -1;
        lastPolyIndexCache = -1;
        firstLineIndexCache = -1;
        lastLineIndexCache = -1;
        firstPointIndexCache = -1;
        lastPointIndexCache = -1;
        if(family != 0) goto _L2; else goto _L1
_L1:
        if(polyAttribs == null)
        {
            polyAttribs = PGraphicsOpenGL.newAttributeMap();
            collectPolyAttribs();
        }
        for(int i = 0; i < childCount; i++)
            ((PShapeOpenGL)children[i]).tessellateImpl();

          goto _L3
_L2:
        if(!shapeCreated) goto _L3; else goto _L4
_L4:
        inGeo.clearEdges();
        tessellator.setInGeometry(inGeo);
        tessellator.setTessGeometry(tessGeo);
        PGraphicsOpenGL.Tessellator tessellator1 = tessellator;
        boolean flag1;
        if(fill || image != null)
            flag1 = true;
        else
            flag1 = false;
        tessellator1.setFill(flag1);
        tessellator.setTexCache(null, null);
        tessellator.setStroke(stroke);
        tessellator.setStrokeColor(strokeColor);
        tessellator.setStrokeWeight(strokeWeight);
        tessellator.setStrokeCap(strokeCap);
        tessellator.setStrokeJoin(strokeJoin);
        tessellator.setRenderer(pg);
        tessellator.setTransform(matrix);
        tessellator.set3D(is3D());
        if(family != 3) goto _L6; else goto _L5
_L5:
        if(kind != 3) goto _L8; else goto _L7
_L7:
        tessellator.tessellatePoints();
_L10:
        if(image != null && parent != null)
            ((PShapeOpenGL)parent).addTexture(image);
        firstPolyIndexCache = tessellator.firstPolyIndexCache;
        lastPolyIndexCache = tessellator.lastPolyIndexCache;
        firstLineIndexCache = tessellator.firstLineIndexCache;
        lastLineIndexCache = tessellator.lastLineIndexCache;
        firstPointIndexCache = tessellator.firstPointIndexCache;
        lastPointIndexCache = tessellator.lastPointIndexCache;
_L3:
        lastPolyVertex = -1;
        firstPolyVertex = -1;
        lastLineVertex = -1;
        firstLineVertex = -1;
        lastPointVertex = -1;
        firstPointVertex = -1;
        tessellated = true;
        return;
_L8:
        if(kind == 5)
            tessellator.tessellateLines();
        else
        if(kind == 50)
            tessellator.tessellateLineStrip();
        else
        if(kind == 51)
            tessellator.tessellateLineLoop();
        else
        if(kind == 8 || kind == 9)
        {
            if(stroke)
                inGeo.addTrianglesEdges();
            if(normalMode == 0)
                inGeo.calcTrianglesNormals();
            tessellator.tessellateTriangles();
        } else
        if(kind == 11)
        {
            if(stroke)
                inGeo.addTriangleFanEdges();
            if(normalMode == 0)
                inGeo.calcTriangleFanNormals();
            tessellator.tessellateTriangleFan();
        } else
        if(kind == 10)
        {
            if(stroke)
                inGeo.addTriangleStripEdges();
            if(normalMode == 0)
                inGeo.calcTriangleStripNormals();
            tessellator.tessellateTriangleStrip();
        } else
        if(kind == 16 || kind == 17)
        {
            if(stroke)
                inGeo.addQuadsEdges();
            if(normalMode == 0)
                inGeo.calcQuadsNormals();
            tessellator.tessellateQuads();
        } else
        if(kind == 18)
        {
            if(stroke)
                inGeo.addQuadStripEdges();
            if(normalMode == 0)
                inGeo.calcQuadStripNormals();
            tessellator.tessellateQuadStrip();
        } else
        if(kind == 20)
        {
            boolean flag3 = inGeo.hasBezierVertex();
            boolean flag4 = inGeo.hasQuadraticVertex();
            boolean flag5 = inGeo.hasCurveVertex();
            if(flag3 || flag4)
                saveBezierVertexSettings();
            if(flag5)
            {
                saveCurveVertexSettings();
                tessellator.resetCurveVertexCount();
            }
            PGraphicsOpenGL.Tessellator tessellator2 = tessellator;
            boolean flag6 = solid;
            boolean flag7 = close;
            boolean flag2 = flag;
            if(normalMode == 0)
                flag2 = true;
            tessellator2.tessellatePolygon(flag6, flag7, flag2);
            if(flag3 || flag4)
                restoreBezierVertexSettings();
            if(flag5)
                restoreCurveVertexSettings();
        }
        continue; /* Loop/switch isn't completed */
_L6:
        if(family == 1)
        {
            inGeo.clear();
            if(kind == 2)
                tessellatePoint();
            else
            if(kind == 4)
                tessellateLine();
            else
            if(kind == 8)
                tessellateTriangle();
            else
            if(kind == 16)
                tessellateQuad();
            else
            if(kind == 30)
                tessellateRect();
            else
            if(kind == 31)
                tessellateEllipse();
            else
            if(kind == 32)
                tessellateArc();
            else
            if(kind == 41)
                tessellateBox();
            else
            if(kind == 40)
                tessellateSphere();
        } else
        if(family == 2)
        {
            inGeo.clear();
            tessellatePath();
        }
        if(true) goto _L10; else goto _L9
_L9:
    }

    protected void tessellateLine()
    {
        float f;
        float f1;
        float f2;
        float f3;
        float f4;
        float f5;
        if(params.length == 4)
        {
            f = params[0];
            f1 = params[1];
            f2 = params[2];
            f3 = params[3];
            f4 = 0.0F;
            f5 = 0.0F;
        } else
        if(params.length == 6)
        {
            f = params[0];
            f1 = params[1];
            f5 = params[2];
            f2 = params[3];
            f3 = params[4];
            f4 = params[5];
        } else
        {
            f4 = 0.0F;
            f3 = 0.0F;
            f2 = 0.0F;
            f5 = 0.0F;
            f1 = 0.0F;
            f = 0.0F;
        }
        inGeo.setMaterial(fillColor, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininess);
        inGeo.setNormal(normalX, normalY, normalZ);
        inGeo.addLine(f, f1, f5, f2, f3, f4, fill, stroke);
        tessellator.tessellateLines();
    }

    protected void tessellatePath()
    {
        if(vertices != null) goto _L2; else goto _L1
_L1:
        return;
_L2:
        int k;
        boolean flag;
        boolean flag2;
        int l;
        inGeo.setMaterial(fillColor, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininess);
        if(vertexCodeCount == 0)
        {
            if(vertices[0].length == 2)
            {
                for(int i = 0; i < vertexCount; i++)
                    inGeo.addVertex(vertices[i][0], vertices[i][1], 0, false);

            } else
            {
                for(int j = 0; j < vertexCount; j++)
                    inGeo.addVertex(vertices[j][0], vertices[j][1], vertices[j][2], 0, false);

            }
            break MISSING_BLOCK_LABEL_770;
        }
        flag = true;
        flag2 = true;
        if(vertices[0].length != 2)
            break MISSING_BLOCK_LABEL_439;
        l = 0;
        k = 0;
_L10:
        if(l >= vertexCodeCount)
            break MISSING_BLOCK_LABEL_770;
        vertexCodes[l];
        JVM INSTR tableswitch 0 4: default 224
    //                   0 230
    //                   1 322
    //                   2 264
    //                   3 401
    //                   4 434;
           goto _L3 _L4 _L5 _L6 _L7 _L8
_L8:
        break MISSING_BLOCK_LABEL_434;
_L3:
        break; /* Loop/switch isn't completed */
_L4:
        break; /* Loop/switch isn't completed */
_L11:
        l++;
        if(true) goto _L10; else goto _L9
_L9:
        inGeo.addVertex(vertices[k][0], vertices[k][1], 0, flag2);
        flag2 = false;
        k++;
          goto _L11
_L6:
        inGeo.addQuadraticVertex(vertices[k + 0][0], vertices[k + 0][1], 0.0F, vertices[k + 1][0], vertices[k + 1][1], 0.0F, flag2);
        flag2 = false;
        k += 2;
          goto _L11
_L5:
        inGeo.addBezierVertex(vertices[k + 0][0], vertices[k + 0][1], 0.0F, vertices[k + 1][0], vertices[k + 1][1], 0.0F, vertices[k + 2][0], vertices[k + 2][1], 0.0F, flag2);
        flag2 = false;
        k += 3;
          goto _L11
_L7:
        inGeo.addCurveVertex(vertices[k][0], vertices[k][1], 0.0F, flag2);
        flag2 = false;
        k++;
          goto _L11
        flag2 = true;
          goto _L11
        l = 0;
        k = 0;
        flag2 = flag;
_L19:
        if(l >= vertexCodeCount)
            break MISSING_BLOCK_LABEL_770;
        vertexCodes[l];
        JVM INSTR tableswitch 0 4: default 496
    //                   0 502
    //                   1 619
    //                   2 543
    //                   3 725
    //                   4 765;
           goto _L12 _L13 _L14 _L15 _L16 _L17
_L17:
        break MISSING_BLOCK_LABEL_765;
_L12:
        break; /* Loop/switch isn't completed */
_L13:
        break; /* Loop/switch isn't completed */
_L20:
        l++;
        if(true) goto _L19; else goto _L18
_L18:
        inGeo.addVertex(vertices[k][0], vertices[k][1], vertices[k][2], flag2);
        flag2 = false;
        k++;
          goto _L20
_L15:
        inGeo.addQuadraticVertex(vertices[k + 0][0], vertices[k + 0][1], vertices[k + 0][2], vertices[k + 1][0], vertices[k + 1][1], vertices[k + 0][2], flag2);
        flag2 = false;
        k += 2;
          goto _L20
_L14:
        inGeo.addBezierVertex(vertices[k + 0][0], vertices[k + 0][1], vertices[k + 0][2], vertices[k + 1][0], vertices[k + 1][1], vertices[k + 1][2], vertices[k + 2][0], vertices[k + 2][1], vertices[k + 2][2], flag2);
        flag2 = false;
        k += 3;
          goto _L20
_L16:
        inGeo.addCurveVertex(vertices[k][0], vertices[k][1], vertices[k][2], flag2);
        flag2 = false;
        k++;
          goto _L20
        flag2 = true;
          goto _L20
        boolean flag4 = inGeo.hasBezierVertex();
        boolean flag1 = inGeo.hasQuadraticVertex();
        boolean flag3 = inGeo.hasCurveVertex();
        if(flag4 || flag1)
            saveBezierVertexSettings();
        if(flag3)
        {
            saveCurveVertexSettings();
            tessellator.resetCurveVertexCount();
        }
        tessellator.tessellatePolygon(true, close, true);
        if(flag4 || flag1)
            restoreBezierVertexSettings();
        if(flag3)
            restoreCurveVertexSettings();
        if(true) goto _L1; else goto _L21
_L21:
    }

    protected void tessellatePoint()
    {
        float f;
        float f1;
        float f2;
        if(params.length == 2)
        {
            f = params[0];
            f1 = params[1];
            f2 = 0.0F;
        } else
        if(params.length == 3)
        {
            f = params[0];
            f1 = params[1];
            f2 = params[2];
        } else
        {
            f2 = 0.0F;
            f1 = 0.0F;
            f = 0.0F;
        }
        inGeo.setMaterial(fillColor, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininess);
        inGeo.setNormal(normalX, normalY, normalZ);
        inGeo.addPoint(f, f1, f2, fill, stroke);
        tessellator.tessellatePoints();
    }

    protected void tessellateQuad()
    {
        float f = 0.0F;
        float f1 = 0.0F;
        float f2 = 0.0F;
        float f3;
        float f4;
        float f5;
        float f6;
        float f7;
        if(params.length == 8)
        {
            f3 = params[0];
            f4 = params[1];
            f5 = params[2];
            f6 = params[3];
            f7 = params[4];
            f = params[5];
            f1 = params[6];
            f2 = params[7];
        } else
        {
            f7 = 0.0F;
            f6 = 0.0F;
            f5 = 0.0F;
            f4 = 0.0F;
            f3 = 0.0F;
        }
        inGeo.setMaterial(fillColor, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininess);
        inGeo.setNormal(normalX, normalY, normalZ);
        inGeo.addQuad(f3, f4, 0.0F, f5, f6, 0.0F, f7, f, 0.0F, f1, f2, 0.0F, stroke);
        tessellator.tessellateQuads();
    }

    protected void tessellateRect()
    {
        float f;
        float f1;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        float f7;
        int i;
        f = 0.0F;
        f1 = 0.0F;
        f2 = 0.0F;
        f3 = 0.0F;
        f4 = 0.0F;
        f5 = 0.0F;
        f6 = 0.0F;
        f7 = 0.0F;
        i = rectMode;
        if(params.length != 4 && params.length != 5) goto _L2; else goto _L1
_L1:
        float f8;
        float f9;
        float f10;
        float f11;
        f8 = params[0];
        f9 = params[1];
        f10 = params[2];
        f11 = params[3];
        f3 = f11;
        f2 = f10;
        f1 = f9;
        f = f8;
        if(params.length != 5) goto _L4; else goto _L3
_L3:
        boolean flag;
        f4 = params[4];
        f6 = params[4];
        f5 = params[4];
        f3 = params[4];
        flag = true;
        f = f8;
        f1 = f9;
        f2 = f10;
        f8 = f6;
        f9 = f5;
        f10 = f3;
        f3 = f11;
_L12:
        i;
        JVM INSTR tableswitch 0 3: default 192
    //                   0 465
    //                   1 454
    //                   2 490
    //                   3 516;
           goto _L5 _L6 _L7 _L8 _L9
_L5:
        float f12 = f1;
        f1 = f;
        f = f12;
_L10:
        float f13;
        if(f1 <= f2)
        {
            float f14 = f2;
            f2 = f1;
            f1 = f14;
        }
        if(f > f3)
        {
            f13 = f3;
            f3 = f;
        } else
        {
            f13 = f;
        }
        f = PApplet.min((f1 - f2) / 2.0F, (f3 - f13) / 2.0F);
        if(f4 > f)
            f4 = f;
        if(f8 > f)
            f8 = f;
        if(f9 > f)
            f9 = f;
        if(f10 <= f)
            f = f10;
        inGeo.setMaterial(fillColor, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininess);
        inGeo.setNormal(normalX, normalY, normalZ);
        if(flag)
        {
            saveBezierVertexSettings();
            inGeo.addRect(f2, f13, f1, f3, f4, f8, f9, f, stroke);
            tessellator.tessellatePolygon(true, true, true);
            restoreBezierVertexSettings();
        } else
        {
            inGeo.addRect(f2, f13, f1, f3, stroke);
            tessellator.tessellateQuads();
        }
        return;
_L2:
        if(params.length == 8)
        {
            f = params[0];
            f1 = params[1];
            f2 = params[2];
            f3 = params[3];
            f4 = params[4];
            f8 = params[5];
            f9 = params[6];
            f10 = params[7];
            flag = true;
            continue; /* Loop/switch isn't completed */
        }
          goto _L4
_L7:
        f13 = f1;
        f1 = f;
        f = f13;
          goto _L10
_L6:
        f2 += f;
        f13 = f3 + f1;
        f3 = f1;
        f1 = f;
        f = f3;
        f3 = f13;
          goto _L10
_L8:
        f13 = f1 - f3;
        f3 = f1 + f3;
        f1 = f - f2;
        f2 = f + f2;
        f = f13;
          goto _L10
_L9:
        f13 = f2 / 2.0F;
        f5 = f3 / 2.0F;
        f2 = f + f13;
        f3 = f1 + f5;
        f13 = f - f13;
        f = f1 - f5;
        f1 = f13;
          goto _L10
_L4:
        flag = false;
        f10 = f7;
        f9 = f6;
        f8 = f5;
        if(true) goto _L12; else goto _L11
_L11:
    }

    protected void tessellateSphere()
    {
        float f;
        int i;
        int j;
        f = 0.0F;
        i = sphereDetailU;
        j = sphereDetailV;
        if(1 > params.length) goto _L2; else goto _L1
_L1:
        float f1 = params[0];
        if(params.length != 2) goto _L4; else goto _L3
_L3:
        j = (int)params[1];
        i = j;
        f = f1;
_L2:
        if(i < 3 || j < 2)
        {
            j = 30;
            i = 30;
        }
        int k = pg.sphereDetailU;
        int l = pg.sphereDetailV;
        if(pg.sphereDetailU != i || pg.sphereDetailV != j)
            pg.sphereDetail(i, j);
        inGeo.setMaterial(fillColor, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininess);
        int ai[] = inGeo.addSphere(f, i, j, fill, stroke);
        tessellator.tessellateTriangles(ai);
        if(k > 0 && k != i || l > 0 && l != j)
            pg.sphereDetail(k, l);
        return;
_L4:
        f = f1;
        if(params.length == 3)
        {
            i = (int)params[1];
            j = (int)params[2];
            f = f1;
        }
        if(true) goto _L2; else goto _L5
_L5:
    }

    protected void tessellateTriangle()
    {
        float f = 0.0F;
        float f1;
        float f2;
        float f3;
        float f4;
        float f5;
        if(params.length == 6)
        {
            f1 = params[0];
            f2 = params[1];
            f3 = params[2];
            f4 = params[3];
            f5 = params[4];
            f = params[5];
        } else
        {
            f5 = 0.0F;
            f4 = 0.0F;
            f3 = 0.0F;
            f2 = 0.0F;
            f1 = 0.0F;
        }
        inGeo.setMaterial(fillColor, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininess);
        inGeo.setNormal(normalX, normalY, normalZ);
        inGeo.addTriangle(f1, f2, 0.0F, f3, f4, 0.0F, f5, f, 0.0F, fill, stroke);
        tessellator.tessellateTriangles();
    }

    protected transient void transform(int i, float af[])
    {
        int j;
        if(is3D)
            j = 3;
        else
            j = 2;
        checkMatrix(j);
        if(transform == null)
        {
            if(j == 2)
                transform = new PMatrix2D();
            else
                transform = new PMatrix3D();
        } else
        {
            transform.reset();
        }
        j = af.length;
        if(i == 1)
        {
            if(af.length == 1)
                j = 2;
            else
                j = 3;
        } else
        if(i == 3)
            if(af.length == 6)
                j = 2;
            else
                j = 3;
        i;
        JVM INSTR tableswitch 0 3: default 84
    //                   0 175
    //                   1 219
    //                   2 263
    //                   3 307;
           goto _L1 _L2 _L3 _L4 _L5
_L1:
        matrix.apply(transform);
        pushTransform();
        if(tessellated)
            applyMatrixImpl(transform);
        return;
_L2:
        if(j == 3)
            transform.translate(af[0], af[1], af[2]);
        else
            transform.translate(af[0], af[1]);
        continue; /* Loop/switch isn't completed */
_L3:
        if(j == 3)
            transform.rotate(af[0], af[1], af[2], af[3]);
        else
            transform.rotate(af[0]);
        continue; /* Loop/switch isn't completed */
_L4:
        if(j == 3)
            transform.scale(af[0], af[1], af[2]);
        else
            transform.scale(af[0], af[1]);
        continue; /* Loop/switch isn't completed */
_L5:
        if(j == 3)
            transform.set(af[0], af[1], af[2], af[3], af[4], af[5], af[6], af[7], af[8], af[9], af[10], af[11], af[12], af[13], af[14], af[15]);
        else
            transform.set(af[0], af[1], af[2], af[3], af[4], af[5]);
        if(true) goto _L1; else goto _L6
_L6:
    }

    public void translate(float f, float f1)
    {
        if(is3D)
            transform(0, new float[] {
                f, f1, 0.0F
            });
        else
            transform(0, new float[] {
                f, f1
            });
    }

    public void translate(float f, float f1, float f2)
    {
        transform(0, new float[] {
            f, f1, f2
        });
    }

    protected void untexChild(boolean flag)
    {
        untexChild(flag, null);
    }

    protected void untexChild(boolean flag, PShapeOpenGL pshapeopengl)
    {
        if(untexChild != flag) goto _L2; else goto _L1
_L1:
        return;
_L2:
        if(!flag)
            break; /* Loop/switch isn't completed */
        untexChild = true;
_L5:
        if(parent != null)
            ((PShapeOpenGL)parent).untexChild(flag, this);
        if(true) goto _L1; else goto _L3
_L3:
        int i;
        untexChild = false;
        i = 0;
_L7:
        if(i >= childCount) goto _L5; else goto _L4
_L4:
        PShapeOpenGL pshapeopengl1 = (PShapeOpenGL)children[i];
          goto _L6
_L9:
        i++;
          goto _L7
_L6:
        if(pshapeopengl1 == pshapeopengl || pshapeopengl1.hasTexture()) goto _L9; else goto _L8
_L8:
        untexChild = true;
          goto _L5
    }

    protected void updateGeometry()
    {
        root.initBuffers();
        if(root.modified)
            root.updateGeometryImpl();
    }

    protected void updateGeometryImpl()
    {
        if(modifiedPolyVertices)
        {
            int i = firstModifiedPolyVertex;
            copyPolyVertices(i, (lastModifiedPolyVertex - i) + 1);
            modifiedPolyVertices = false;
            firstModifiedPolyVertex = 0x7fffffff;
            lastModifiedPolyVertex = 0x80000000;
        }
        if(modifiedPolyColors)
        {
            int j = firstModifiedPolyColor;
            copyPolyColors(j, (lastModifiedPolyColor - j) + 1);
            modifiedPolyColors = false;
            firstModifiedPolyColor = 0x7fffffff;
            lastModifiedPolyColor = 0x80000000;
        }
        if(modifiedPolyNormals)
        {
            int k = firstModifiedPolyNormal;
            copyPolyNormals(k, (lastModifiedPolyNormal - k) + 1);
            modifiedPolyNormals = false;
            firstModifiedPolyNormal = 0x7fffffff;
            lastModifiedPolyNormal = 0x80000000;
        }
        if(modifiedPolyTexCoords)
        {
            int l = firstModifiedPolyTexcoord;
            copyPolyTexCoords(l, (lastModifiedPolyTexcoord - l) + 1);
            modifiedPolyTexCoords = false;
            firstModifiedPolyTexcoord = 0x7fffffff;
            lastModifiedPolyTexcoord = 0x80000000;
        }
        if(modifiedPolyAmbient)
        {
            int i1 = firstModifiedPolyAmbient;
            copyPolyAmbient(i1, (lastModifiedPolyAmbient - i1) + 1);
            modifiedPolyAmbient = false;
            firstModifiedPolyAmbient = 0x7fffffff;
            lastModifiedPolyAmbient = 0x80000000;
        }
        if(modifiedPolySpecular)
        {
            int j1 = firstModifiedPolySpecular;
            copyPolySpecular(j1, (lastModifiedPolySpecular - j1) + 1);
            modifiedPolySpecular = false;
            firstModifiedPolySpecular = 0x7fffffff;
            lastModifiedPolySpecular = 0x80000000;
        }
        if(modifiedPolyEmissive)
        {
            int k1 = firstModifiedPolyEmissive;
            copyPolyEmissive(k1, (lastModifiedPolyEmissive - k1) + 1);
            modifiedPolyEmissive = false;
            firstModifiedPolyEmissive = 0x7fffffff;
            lastModifiedPolyEmissive = 0x80000000;
        }
        if(modifiedPolyShininess)
        {
            int l1 = firstModifiedPolyShininess;
            copyPolyShininess(l1, (lastModifiedPolyShininess - l1) + 1);
            modifiedPolyShininess = false;
            firstModifiedPolyShininess = 0x7fffffff;
            lastModifiedPolyShininess = 0x80000000;
        }
        Iterator iterator = polyAttribs.keySet().iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            Object obj = (String)iterator.next();
            obj = (PGraphicsOpenGL.VertexAttribute)polyAttribs.get(obj);
            if(((PGraphicsOpenGL.VertexAttribute) (obj)).modified)
            {
                int i2 = firstModifiedPolyVertex;
                copyPolyAttrib(((PGraphicsOpenGL.VertexAttribute) (obj)), i2, (lastModifiedPolyVertex - i2) + 1);
                obj.modified = false;
                obj.firstModified = 0x7fffffff;
                obj.lastModified = 0x80000000;
            }
        } while(true);
        if(modifiedLineVertices)
        {
            int j2 = firstModifiedLineVertex;
            copyLineVertices(j2, (lastModifiedLineVertex - j2) + 1);
            modifiedLineVertices = false;
            firstModifiedLineVertex = 0x7fffffff;
            lastModifiedLineVertex = 0x80000000;
        }
        if(modifiedLineColors)
        {
            int k2 = firstModifiedLineColor;
            copyLineColors(k2, (lastModifiedLineColor - k2) + 1);
            modifiedLineColors = false;
            firstModifiedLineColor = 0x7fffffff;
            lastModifiedLineColor = 0x80000000;
        }
        if(modifiedLineAttributes)
        {
            int l2 = firstModifiedLineAttribute;
            copyLineAttributes(l2, (lastModifiedLineAttribute - l2) + 1);
            modifiedLineAttributes = false;
            firstModifiedLineAttribute = 0x7fffffff;
            lastModifiedLineAttribute = 0x80000000;
        }
        if(modifiedPointVertices)
        {
            int i3 = firstModifiedPointVertex;
            copyPointVertices(i3, (lastModifiedPointVertex - i3) + 1);
            modifiedPointVertices = false;
            firstModifiedPointVertex = 0x7fffffff;
            lastModifiedPointVertex = 0x80000000;
        }
        if(modifiedPointColors)
        {
            int j3 = firstModifiedPointColor;
            copyPointColors(j3, (lastModifiedPointColor - j3) + 1);
            modifiedPointColors = false;
            firstModifiedPointColor = 0x7fffffff;
            lastModifiedPointColor = 0x80000000;
        }
        if(modifiedPointAttributes)
        {
            int k3 = firstModifiedPointAttribute;
            copyPointAttributes(k3, (lastModifiedPointAttribute - k3) + 1);
            modifiedPointAttributes = false;
            firstModifiedPointAttribute = 0x7fffffff;
            lastModifiedPointAttribute = 0x80000000;
        }
        modified = false;
    }

    protected void updateLineIndexCache()
    {
        PGraphicsOpenGL.IndexCache indexcache = tessGeo.lineIndexCache;
        if(family == 0)
        {
            lastLineIndexCache = -1;
            firstLineIndexCache = -1;
            int i = 0;
            int k = -1;
            for(; i < childCount; i++)
            {
                PShapeOpenGL pshapeopengl = (PShapeOpenGL)children[i];
                int i1 = pshapeopengl.firstLineIndexCache;
                int j1;
                int l1;
                if(-1 < i1)
                    j1 = (pshapeopengl.lastLineIndexCache - i1) + 1;
                else
                    j1 = -1;
                l1 = i1;
                while(l1 < i1 + j1) 
                {
                    if(k == -1)
                    {
                        k = indexcache.addNew(l1);
                        firstLineIndexCache = k;
                    } else
                    if(indexcache.vertexOffset[k] == indexcache.vertexOffset[l1])
                        indexcache.incCounts(k, indexcache.indexCount[l1], indexcache.vertexCount[l1]);
                    else
                        k = indexcache.addNew(l1);
                    l1++;
                }
                if(-1 < pshapeopengl.firstLineVertex)
                {
                    if(firstLineVertex == -1)
                        firstLineVertex = 0x7fffffff;
                    firstLineVertex = PApplet.min(firstLineVertex, pshapeopengl.firstLineVertex);
                }
                if(-1 < pshapeopengl.lastLineVertex)
                    lastLineVertex = PApplet.max(lastLineVertex, pshapeopengl.lastLineVertex);
            }

            lastLineIndexCache = k;
        } else
        {
            int l = indexcache.vertexOffset[firstLineIndexCache];
            lastLineVertex = l;
            firstLineVertex = l;
            l = firstLineIndexCache;
            while(l <= lastLineIndexCache) 
            {
                int i2 = indexcache.indexOffset[l];
                int k1 = indexcache.indexCount[l];
                int j = indexcache.vertexCount[l];
                PShapeOpenGL pshapeopengl1;
                if(PGL.MAX_VERTEX_INDEX1 <= root.lineVertexRel + j)
                {
                    root.lineVertexRel = 0;
                    root.lineVertexOffset = root.lineVertexAbs;
                    indexcache.indexOffset[l] = root.lineIndexOffset;
                } else
                {
                    tessGeo.incLineIndices(i2, (i2 + k1) - 1, root.lineVertexRel);
                }
                indexcache.vertexOffset[l] = root.lineVertexOffset;
                pshapeopengl1 = root;
                pshapeopengl1.lineIndexOffset = k1 + pshapeopengl1.lineIndexOffset;
                pshapeopengl1 = root;
                pshapeopengl1.lineVertexAbs = pshapeopengl1.lineVertexAbs + j;
                pshapeopengl1 = root;
                pshapeopengl1.lineVertexRel = pshapeopengl1.lineVertexRel + j;
                lastLineVertex = lastLineVertex + j;
                l++;
            }
            lastLineVertex = lastLineVertex - 1;
        }
    }

    protected void updatePointIndexCache()
    {
        PGraphicsOpenGL.IndexCache indexcache = tessGeo.pointIndexCache;
        if(family == 0)
        {
            lastPointIndexCache = -1;
            firstPointIndexCache = -1;
            int i = 0;
            int k = -1;
            for(; i < childCount; i++)
            {
                PShapeOpenGL pshapeopengl = (PShapeOpenGL)children[i];
                int i1 = pshapeopengl.firstPointIndexCache;
                int j1;
                int l1;
                if(-1 < i1)
                    j1 = (pshapeopengl.lastPointIndexCache - i1) + 1;
                else
                    j1 = -1;
                l1 = i1;
                while(l1 < i1 + j1) 
                {
                    if(k == -1)
                    {
                        k = indexcache.addNew(l1);
                        firstPointIndexCache = k;
                    } else
                    if(indexcache.vertexOffset[k] == indexcache.vertexOffset[l1])
                        indexcache.incCounts(k, indexcache.indexCount[l1], indexcache.vertexCount[l1]);
                    else
                        k = indexcache.addNew(l1);
                    l1++;
                }
                if(-1 < pshapeopengl.firstPointVertex)
                {
                    if(firstPointVertex == -1)
                        firstPointVertex = 0x7fffffff;
                    firstPointVertex = PApplet.min(firstPointVertex, pshapeopengl.firstPointVertex);
                }
                if(-1 < pshapeopengl.lastPointVertex)
                    lastPointVertex = PApplet.max(lastPointVertex, pshapeopengl.lastPointVertex);
            }

            lastPointIndexCache = k;
        } else
        {
            int l = indexcache.vertexOffset[firstPointIndexCache];
            lastPointVertex = l;
            firstPointVertex = l;
            l = firstPointIndexCache;
            while(l <= lastPointIndexCache) 
            {
                int j = indexcache.indexOffset[l];
                int i2 = indexcache.indexCount[l];
                int k1 = indexcache.vertexCount[l];
                PShapeOpenGL pshapeopengl1;
                if(PGL.MAX_VERTEX_INDEX1 <= root.pointVertexRel + k1)
                {
                    root.pointVertexRel = 0;
                    root.pointVertexOffset = root.pointVertexAbs;
                    indexcache.indexOffset[l] = root.pointIndexOffset;
                } else
                {
                    tessGeo.incPointIndices(j, (j + i2) - 1, root.pointVertexRel);
                }
                indexcache.vertexOffset[l] = root.pointVertexOffset;
                pshapeopengl1 = root;
                pshapeopengl1.pointIndexOffset = i2 + pshapeopengl1.pointIndexOffset;
                pshapeopengl1 = root;
                pshapeopengl1.pointVertexAbs = pshapeopengl1.pointVertexAbs + k1;
                pshapeopengl1 = root;
                pshapeopengl1.pointVertexRel = pshapeopengl1.pointVertexRel + k1;
                lastPointVertex = lastPointVertex + k1;
                l++;
            }
            lastPointVertex = lastPointVertex - 1;
        }
    }

    protected void updatePolyIndexCache()
    {
        PGraphicsOpenGL.IndexCache indexcache = tessGeo.polyIndexCache;
        if(family != 0) goto _L2; else goto _L1
_L1:
        lastPolyIndexCache = -1;
        firstPolyIndexCache = -1;
        int i = 0;
        int k = -1;
        for(; i < childCount; i++)
        {
            PShapeOpenGL pshapeopengl = (PShapeOpenGL)children[i];
            int i1 = pshapeopengl.firstPolyIndexCache;
            int j1;
            int l1;
            if(-1 < i1)
                j1 = (pshapeopengl.lastPolyIndexCache - i1) + 1;
            else
                j1 = -1;
            l1 = i1;
            while(l1 < i1 + j1) 
            {
                if(k == -1)
                {
                    k = indexcache.addNew(l1);
                    firstPolyIndexCache = k;
                } else
                if(indexcache.vertexOffset[k] == indexcache.vertexOffset[l1])
                    indexcache.incCounts(k, indexcache.indexCount[l1], indexcache.vertexCount[l1]);
                else
                    k = indexcache.addNew(l1);
                l1++;
            }
            if(-1 < pshapeopengl.firstPolyVertex)
            {
                if(firstPolyVertex == -1)
                    firstPolyVertex = 0x7fffffff;
                firstPolyVertex = PApplet.min(firstPolyVertex, pshapeopengl.firstPolyVertex);
            }
            if(-1 < pshapeopengl.lastPolyVertex)
                lastPolyVertex = PApplet.max(lastPolyVertex, pshapeopengl.lastPolyVertex);
        }

        lastPolyIndexCache = k;
_L4:
        return;
_L2:
        int l = indexcache.vertexOffset[firstPolyIndexCache];
        lastPolyVertex = l;
        firstPolyVertex = l;
        l = firstPolyIndexCache;
        while(l <= lastPolyIndexCache) 
        {
            int j = indexcache.indexOffset[l];
            int k1 = indexcache.indexCount[l];
            int i2 = indexcache.vertexCount[l];
            PShapeOpenGL pshapeopengl1;
            if(PGL.MAX_VERTEX_INDEX1 <= root.polyVertexRel + i2 || is2D() && startStrokedTex(l))
            {
                root.polyVertexRel = 0;
                root.polyVertexOffset = root.polyVertexAbs;
                indexcache.indexOffset[l] = root.polyIndexOffset;
            } else
            {
                tessGeo.incPolyIndices(j, (j + k1) - 1, root.polyVertexRel);
            }
            indexcache.vertexOffset[l] = root.polyVertexOffset;
            if(is2D())
                setFirstStrokeVertex(l, lastPolyVertex);
            pshapeopengl1 = root;
            pshapeopengl1.polyIndexOffset = k1 + pshapeopengl1.polyIndexOffset;
            pshapeopengl1 = root;
            pshapeopengl1.polyVertexAbs = pshapeopengl1.polyVertexAbs + i2;
            pshapeopengl1 = root;
            pshapeopengl1.polyVertexRel = pshapeopengl1.polyVertexRel + i2;
            lastPolyVertex = lastPolyVertex + i2;
            l++;
        }
        lastPolyVertex = lastPolyVertex - 1;
        if(is2D())
            setLastStrokeVertex(lastPolyVertex);
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected void updateRoot(PShape pshape)
    {
        root = (PShapeOpenGL)pshape;
        if(family == 0)
        {
            for(int i = 0; i < childCount; i++)
                ((PShapeOpenGL)children[i]).updateRoot(pshape);

        }
    }

    protected void updateTessellation()
    {
        if(!root.tessellated)
        {
            root.tessellate();
            root.aggregate();
            root.initModified();
            root.needBufferInit = true;
        }
    }

    public void vertex(float f, float f1)
    {
        vertexImpl(f, f1, 0.0F, 0.0F, 0.0F);
        if(image != null)
            PGraphics.showWarning("No uv texture coordinates supplied with vertex() call");
    }

    public void vertex(float f, float f1, float f2)
    {
        vertexImpl(f, f1, f2, 0.0F, 0.0F);
        if(image != null)
            PGraphics.showWarning("No uv texture coordinates supplied with vertex() call");
    }

    public void vertex(float f, float f1, float f2, float f3)
    {
        vertexImpl(f, f1, 0.0F, f2, f3);
    }

    public void vertex(float f, float f1, float f2, float f3, float f4)
    {
        vertexImpl(f, f1, f2, f3, f4);
    }

    protected boolean vertexBreak()
    {
        boolean flag = false;
        if(breakShape)
        {
            breakShape = false;
            flag = true;
        }
        return flag;
    }

    protected void vertexImpl(float f, float f1, float f2, float f3, float f4)
    {
        if(openShape) goto _L2; else goto _L1
_L1:
        PGraphics.showWarning("%1$s can only be called between beginShape() and endShape()", new Object[] {
            "vertex()"
        });
_L4:
        return;
_L2:
        int j;
        if(family == 0)
        {
            PGraphics.showWarning("Cannot add vertices to GROUP shape");
            continue; /* Loop/switch isn't completed */
        }
        int i;
        float f5;
        if(image != null)
            i = 1;
        else
            i = 0;
        j = 0;
        if(fill || i != 0)
        {
            if(i != 0)
                break; /* Loop/switch isn't completed */
            j = fillColor;
        }
_L5:
        if(textureMode == 2 && image != null)
        {
            f3 /= image.width;
            f4 /= image.height;
        }
        i = 0;
        f5 = 0.0F;
        if(stroke)
        {
            i = strokeColor;
            f5 = strokeWeight;
        }
        inGeo.addVertex(f, f1, f2, j, normalX, normalY, normalZ, f3, f4, i, f5, ambientColor, specularColor, emissiveColor, shininess, 0, vertexBreak());
        markForTessellation();
        if(true) goto _L4; else goto _L3
_L3:
        if(tint)
            j = tintColor;
        else
            j = -1;
          goto _L5
        if(true) goto _L4; else goto _L6
_L6:
    }

    public static final int DIRECTION = 3;
    protected static final int MATRIX = 3;
    public static final int NORMAL = 1;
    protected static final int NORMAL_MODE_AUTO = 0;
    protected static final int NORMAL_MODE_SHAPE = 1;
    protected static final int NORMAL_MODE_VERTEX = 2;
    public static final int OFFSET = 4;
    public static final int POSITION = 0;
    protected static final int ROTATE = 1;
    protected static final int SCALE = 2;
    public static final int TEXCOORD = 2;
    protected static final int TRANSLATE = 0;
    protected int bezierDetail;
    protected boolean breakShape;
    protected VertexBuffer bufLineAttrib;
    protected VertexBuffer bufLineColor;
    protected VertexBuffer bufLineIndex;
    protected VertexBuffer bufLineVertex;
    protected VertexBuffer bufPointAttrib;
    protected VertexBuffer bufPointColor;
    protected VertexBuffer bufPointIndex;
    protected VertexBuffer bufPointVertex;
    protected VertexBuffer bufPolyAmbient;
    protected VertexBuffer bufPolyColor;
    protected VertexBuffer bufPolyEmissive;
    protected VertexBuffer bufPolyIndex;
    protected VertexBuffer bufPolyNormal;
    protected VertexBuffer bufPolyShininess;
    protected VertexBuffer bufPolySpecular;
    protected VertexBuffer bufPolyTexcoord;
    protected VertexBuffer bufPolyVertex;
    protected int context;
    protected int curveDetail;
    protected float curveTightness;
    protected int firstLineIndexCache;
    protected int firstLineVertex;
    protected int firstModifiedLineAttribute;
    protected int firstModifiedLineColor;
    protected int firstModifiedLineVertex;
    protected int firstModifiedPointAttribute;
    protected int firstModifiedPointColor;
    protected int firstModifiedPointVertex;
    protected int firstModifiedPolyAmbient;
    protected int firstModifiedPolyColor;
    protected int firstModifiedPolyEmissive;
    protected int firstModifiedPolyNormal;
    protected int firstModifiedPolyShininess;
    protected int firstModifiedPolySpecular;
    protected int firstModifiedPolyTexcoord;
    protected int firstModifiedPolyVertex;
    protected int firstPointIndexCache;
    protected int firstPointVertex;
    protected int firstPolyIndexCache;
    protected int firstPolyVertex;
    public int glUsage;
    protected boolean hasLines;
    protected boolean hasPoints;
    protected boolean hasPolys;
    protected PGraphicsOpenGL.InGeometry inGeo;
    protected int lastLineIndexCache;
    protected int lastLineVertex;
    protected int lastModifiedLineAttribute;
    protected int lastModifiedLineColor;
    protected int lastModifiedLineVertex;
    protected int lastModifiedPointAttribute;
    protected int lastModifiedPointColor;
    protected int lastModifiedPointVertex;
    protected int lastModifiedPolyAmbient;
    protected int lastModifiedPolyColor;
    protected int lastModifiedPolyEmissive;
    protected int lastModifiedPolyNormal;
    protected int lastModifiedPolyShininess;
    protected int lastModifiedPolySpecular;
    protected int lastModifiedPolyTexcoord;
    protected int lastModifiedPolyVertex;
    protected int lastPointIndexCache;
    protected int lastPointVertex;
    protected int lastPolyIndexCache;
    protected int lastPolyVertex;
    protected int lineIndCopyOffset;
    protected int lineIndexOffset;
    protected int lineVertCopyOffset;
    protected int lineVertexAbs;
    protected int lineVertexOffset;
    protected int lineVertexRel;
    protected boolean modified;
    protected boolean modifiedLineAttributes;
    protected boolean modifiedLineColors;
    protected boolean modifiedLineVertices;
    protected boolean modifiedPointAttributes;
    protected boolean modifiedPointColors;
    protected boolean modifiedPointVertices;
    protected boolean modifiedPolyAmbient;
    protected boolean modifiedPolyColors;
    protected boolean modifiedPolyEmissive;
    protected boolean modifiedPolyNormals;
    protected boolean modifiedPolyShininess;
    protected boolean modifiedPolySpecular;
    protected boolean modifiedPolyTexCoords;
    protected boolean modifiedPolyVertices;
    protected boolean needBufferInit;
    protected int normalMode;
    protected float normalX;
    protected float normalY;
    protected float normalZ;
    protected PGraphicsOpenGL pg;
    protected PGL pgl;
    protected int pointIndCopyOffset;
    protected int pointIndexOffset;
    protected int pointVertCopyOffset;
    protected int pointVertexAbs;
    protected int pointVertexOffset;
    protected int pointVertexRel;
    protected PGraphicsOpenGL.AttributeMap polyAttribs;
    protected int polyIndCopyOffset;
    protected int polyIndexOffset;
    protected int polyVertCopyOffset;
    protected int polyVertexAbs;
    protected int polyVertexOffset;
    protected int polyVertexRel;
    protected PShapeOpenGL root;
    protected int savedAmbientColor;
    protected int savedBezierDetail;
    protected int savedCurveDetail;
    protected float savedCurveTightness;
    protected int savedEmissiveColor;
    protected boolean savedFill;
    protected int savedFillColor;
    protected float savedShininess;
    protected int savedSpecularColor;
    protected boolean savedStroke;
    protected int savedStrokeCap;
    protected int savedStrokeColor;
    protected int savedStrokeJoin;
    protected float savedStrokeWeight;
    protected int savedTextureMode;
    protected boolean savedTint;
    protected int savedTintColor;
    protected boolean shapeCreated;
    protected boolean solid;
    protected boolean strokedTexture;
    protected PGraphicsOpenGL.TessGeometry tessGeo;
    protected boolean tessellated;
    protected PGraphicsOpenGL.Tessellator tessellator;
    protected HashSet textures;
    protected PMatrix transform;
    protected Stack transformStack;
    protected boolean untexChild;
}
