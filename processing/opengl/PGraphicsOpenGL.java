// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.opengl;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.nio.*;
import java.util.*;
import processing.core.*;

// Referenced classes of package processing.opengl:
//            PGL, Texture, FrameBuffer, VertexBuffer, 
//            PGLES, PShapeOpenGL, PShader, FontTexture, 
//            PGraphics2D, PGraphics3D, LinePath

public class PGraphicsOpenGL extends PGraphics
{
    protected static class AttributeMap extends HashMap
    {

        public VertexAttribute get(int i)
        {
            return (VertexAttribute)get(names.get(i));
        }

        public volatile Object put(Object obj, Object obj1)
        {
            return put((String)obj, (VertexAttribute)obj1);
        }

        public VertexAttribute put(String s, VertexAttribute vertexattribute)
        {
            VertexAttribute vertexattribute1 = (VertexAttribute)put(s, vertexattribute);
            names.add(s);
            if(vertexattribute.kind == 2)
                numComp = numComp + 4;
            else
                numComp = numComp + vertexattribute.size;
            return vertexattribute1;
        }

        public ArrayList names;
        public int numComp;

        protected AttributeMap()
        {
            names = new ArrayList();
            numComp = 0;
        }
    }

    protected static class DepthSorter
    {

        static float dot(float f, float f1, float f2, float f3, float f4, float f5)
        {
            return f * f3 + f1 * f4 + f2 * f5;
        }

        static void fetchTriCoords(float af[], int i, int ai[], int ai1[], float af1[], short aword0[])
        {
            int j = ai[ai1[i]];
            int k = (aword0[i * 3 + 0] + j) * 3;
            int l = (aword0[i * 3 + 1] + j) * 3;
            i = (j + aword0[i * 3 + 2]) * 3;
            af[0] = af1[k + 0];
            af[1] = af1[k + 1];
            af[2] = af1[k + 2];
            af[3] = af1[l + 0];
            af[4] = af1[l + 1];
            af[5] = af1[l + 2];
            af[6] = af1[i + 0];
            af[7] = af1[i + 1];
            af[8] = af1[i + 2];
        }

        static void rotateRight(int ai[], int i, int j)
        {
            if(i != j)
            {
                int k = ai[j];
                System.arraycopy(ai, i, ai, i + 1, j - i);
                ai[i] = k;
            }
        }

        static int side(float af[], float af1[], float f)
        {
            float f1 = af[3] - af[0];
            float f2 = af[6] - af[0];
            float f3 = af[4] - af[1];
            float f4 = af[7] - af[1];
            float f5 = af[5] - af[2];
            float f6 = af[8] - af[2];
            float f7 = f3 * f6 - f5 * f4;
            f6 = f5 * f2 - f6 * f1;
            f4 = f1 * f4 - f2 * f3;
            f3 = 1.0F / (float)Math.sqrt(f7 * f7 + f6 * f6 + f4 * f4);
            f2 = f7 * f3;
            f1 = f6 * f3;
            f3 = f4 * f3;
            f4 = -dot(f2, f1, f3, af[0], af[1], af[2]);
            f6 = dot(f2, f1, f3, af[0], af[1], af[2] + 100F * f) + f4;
            f = dot(f2, f1, f3, af1[0], af1[1], af1[2]) + f4;
            f7 = dot(f2, f1, f3, af1[3], af1[4], af1[5]) + f4;
            f2 = dot(f2, f1, f3, af1[6], af1[7], af1[8]) + f4;
            f4 = PApplet.abs(f);
            f5 = PApplet.abs(f7);
            f1 = PApplet.abs(f2);
            f3 = PApplet.max(f4, f5, f1) * 0.1F;
            if(f4 < f3)
                f = 0.0F;
            f4 = f * f6;
            int i;
            boolean flag;
            if(f5 < f3)
                f = 0.0F;
            else
                f = f7;
            f7 = f * f6;
            f = f2;
            if(f1 < f3)
                f = 0.0F;
            f *= f6;
            if(f4 >= 0.0F && f7 >= 0.0F && f >= 0.0F)
                i = 1;
            else
                i = 0;
            if(f4 <= 0.0F && f7 <= 0.0F && f <= 0.0F)
                flag = true;
            else
                flag = false;
            if(i != 0)
                i = 1;
            else
            if(flag)
                i = -1;
            else
                i = 0;
            return i;
        }

        static void sortByMinZ(int i, int j, int ai[], float af[])
        {
            swap(ai, i, (i + j) / 2);
            float f = af[ai[i]];
            int k = i + 1;
            int l;
            int i1;
            for(l = i; k <= j; l = i1)
            {
                i1 = l;
                if(af[ai[k]] < f)
                {
                    i1 = l + 1;
                    swap(ai, i1, k);
                }
                k++;
            }

            swap(ai, i, l);
            if(i < l - 1)
                sortByMinZ(i, l - 1, ai, af);
            if(l + 1 < j)
                sortByMinZ(l + 1, j, ai, af);
        }

        static void swap(int ai[], int i, int j)
        {
            int k = ai[i];
            ai[i] = ai[j];
            ai[j] = k;
        }

        void checkIndexBuffers(int i)
        {
            if(triangleIndices.length < i)
            {
                i = (i / 4 + 1) * 5;
                triangleIndices = new int[i];
                texMap = new int[i];
                voffsetMap = new int[i];
                minXBuffer = new float[i];
                minYBuffer = new float[i];
                minZBuffer = new float[i];
                maxXBuffer = new float[i];
                maxYBuffer = new float[i];
                maxZBuffer = new float[i];
            }
        }

        void checkVertexBuffer(int i)
        {
            i *= 3;
            if(screenVertices.length < i)
                screenVertices = new float[(i / 4 + 1) * 5];
        }

        void sort(TessGeometry tessgeometry)
        {
            int i;
            int ai[];
            int ai1[];
            int ai2[];
            int k1;
            short aword0[];
            i = tessgeometry.polyIndexCount / 3;
            checkIndexBuffers(i);
            ai = triangleIndices;
            ai1 = texMap;
            ai2 = voffsetMap;
            for(int j = 0; j < i; j++)
                ai[j] = j;

            TexCache texcache = pg.texCache;
            IndexCache indexcache = tessgeometry.polyIndexCache;
            for(int k = 0; k < texcache.size; k++)
            {
                int l1 = texcache.firstCache[k];
                int j2 = texcache.lastCache[k];
                int l3;
                int k4;
                for(int l2 = l1; l2 <= j2; l2++)
                {
                    int j5;
                    if(l2 == l1)
                        l3 = texcache.firstIndex[k];
                    else
                        l3 = indexcache.indexOffset[l2];
                    if(l2 == j2)
                        k4 = (texcache.lastIndex[k] - l3) + 1;
                    else
                        k4 = (indexcache.indexOffset[l2] + indexcache.indexCount[l2]) - l3;
                    for(j5 = l3 / 3; j5 < (l3 + k4) / 3; j5++)
                    {
                        ai1[j5] = k;
                        ai2[j5] = l2;
                    }

                }

            }

            int i3 = tessgeometry.polyVertexCount;
            checkVertexBuffer(i3);
            float af[] = screenVertices;
            float af1[] = tessgeometry.polyVertices;
            PMatrix3D pmatrix3d = pg.projection;
            for(int l = 0; l < i3; l++)
            {
                float f = af1[l * 4 + 0];
                float f1 = af1[l * 4 + 1];
                float f3 = af1[l * 4 + 2];
                float f5 = af1[l * 4 + 3];
                float f7 = pmatrix3d.m00 * f + pmatrix3d.m01 * f1 + pmatrix3d.m02 * f3 + pmatrix3d.m03 * f5;
                float f9 = pmatrix3d.m10;
                float f11 = pmatrix3d.m11;
                float f18 = pmatrix3d.m12;
                f11 = pmatrix3d.m13 * f5 + (f9 * f + f11 * f1 + f18 * f3);
                f9 = pmatrix3d.m20 * f + pmatrix3d.m21 * f1 + pmatrix3d.m22 * f3 + pmatrix3d.m23 * f5;
                f = f * pmatrix3d.m30 + f1 * pmatrix3d.m31 + pmatrix3d.m32 * f3 + pmatrix3d.m33 * f5;
                f3 = f9;
                f1 = f11;
                f5 = f7;
                if(PGraphicsOpenGL.nonZero(f))
                {
                    f5 = f7 / f;
                    f1 = f11 / f;
                    f3 = f9 / f;
                }
                af[l * 3 + 0] = f5;
                af[l * 3 + 1] = f1;
                af[l * 3 + 2] = -f3;
            }

            af1 = screenVertices;
            af = tessgeometry.polyIndexCache.vertexOffset;
            aword0 = tessgeometry.polyIndices;
            float af2[] = triA;
            float af3[] = triB;
            for(int i1 = 0; i1 < i; i1++)
            {
                fetchTriCoords(af2, i1, af, ai2, af1, aword0);
                minXBuffer[i1] = PApplet.min(af2[0], af2[3], af2[6]);
                maxXBuffer[i1] = PApplet.max(af2[0], af2[3], af2[6]);
                minYBuffer[i1] = PApplet.min(af2[1], af2[4], af2[7]);
                maxYBuffer[i1] = PApplet.max(af2[1], af2[4], af2[7]);
                minZBuffer[i1] = PApplet.min(af2[2], af2[5], af2[8]);
                maxZBuffer[i1] = PApplet.max(af2[2], af2[5], af2[8]);
            }

            sortByMinZ(0, i - 1, ai, minZBuffer);
            BitSet bitset = marked;
            tessgeometry = swapped;
            bitset.clear();
            for(int l4 = 0; l4 < i; l4++)
            {
                int i4 = l4 + 1;
                int j1 = 0;
                tessgeometry.clear();
                int j3 = ai[l4];
                float f2 = minXBuffer[j3];
                float f4 = maxXBuffer[j3];
                float f8 = minYBuffer[j3];
                float f6 = maxYBuffer[j3];
                float f10 = maxZBuffer[j3];
                fetchTriCoords(af2, j3, af, ai2, af1, aword0);
                while(j1 == 0 && i4 < i) 
                {
                    int k5 = ai[i4];
                    float f13;
                    if(f10 <= minZBuffer[k5] && !bitset.get(k5))
                    {
                        float f12 = f8;
                        j1 = j3;
                        j3 = 1;
                        f8 = f2;
                        f2 = f12;
                    } else
                    if(f4 <= minXBuffer[k5] || f6 <= minYBuffer[k5] || f2 >= maxXBuffer[k5] || f8 >= maxYBuffer[k5])
                    {
                        float f14 = f8;
                        f8 = f2;
                        k5 = i4 + 1;
                        i4 = j1;
                        f2 = f14;
                        j1 = j3;
                        j3 = i4;
                        i4 = k5;
                    } else
                    {
                        fetchTriCoords(af3, k5, af, ai2, af1, aword0);
                        if(side(af3, af2, -1F) > 0)
                        {
                            float f15 = f2;
                            k5 = i4 + 1;
                            i4 = j1;
                            f2 = f8;
                            f8 = f15;
                            j1 = j3;
                            j3 = i4;
                            i4 = k5;
                        } else
                        if(side(af2, af3, 1.0F) > 0)
                        {
                            float f16 = f2;
                            k5 = i4 + 1;
                            i4 = j1;
                            f2 = f8;
                            f8 = f16;
                            j1 = j3;
                            j3 = i4;
                            i4 = k5;
                        } else
                        if(!tessgeometry.get(k5))
                        {
                            tessgeometry.set(j3);
                            bitset.set(k5);
                            rotateRight(ai, l4, i4);
                            System.arraycopy(af3, 0, af2, 0, 9);
                            f8 = minXBuffer[k5];
                            f4 = maxXBuffer[k5];
                            f2 = minYBuffer[k5];
                            f6 = maxYBuffer[k5];
                            f10 = maxZBuffer[k5];
                            i4 = l4 + 1;
                            j3 = j1;
                            j1 = k5;
                        } else
                        {
                            float f17 = f2;
                            int l5 = i4 + 1;
                            i4 = j1;
                            f2 = f8;
                            f8 = f17;
                            j1 = j3;
                            j3 = i4;
                            i4 = l5;
                        }
                    }
                    f13 = f8;
                    k5 = j3;
                    f8 = f2;
                    f2 = f13;
                    j3 = j1;
                    j1 = k5;
                }
            }

            k1 = 0;
_L3:
            if(k1 >= i) goto _L2; else goto _L1
_L1:
            int i2;
            int k2;
            int k3;
            int j4;
            int i6;
            int j6;
            int k6;
            k3 = ai[k1];
            if(k1 == k3)
                continue; /* Loop/switch isn't completed */
            k2 = aword0[k1 * 3 + 0];
            j6 = aword0[k1 * 3 + 1];
            k6 = aword0[k1 * 3 + 2];
            i2 = ai1[k1];
            i6 = ai2[k1];
            j4 = k1;
_L4:
            int i5;
            ai[j4] = j4;
            aword0[j4 * 3 + 0] = aword0[k3 * 3 + 0];
            aword0[j4 * 3 + 1] = aword0[k3 * 3 + 1];
            aword0[j4 * 3 + 2] = aword0[k3 * 3 + 2];
            ai1[j4] = ai1[k3];
            ai2[j4] = ai2[k3];
            i5 = ai[k3];
            if(i5 != k1)
                break MISSING_BLOCK_LABEL_1547;
            ai[k3] = k3;
            aword0[k3 * 3 + 0] = (short)k2;
            aword0[k3 * 3 + 1] = (short)j6;
            aword0[k3 * 3 + 2] = (short)k6;
            ai1[k3] = i2;
            ai2[k3] = i6;
            k1++;
              goto _L3
_L2:
            return;
            j4 = k3;
            k3 = i5;
              goto _L4
        }

        static final int W = 3;
        static final int X = 0;
        static final int X0 = 0;
        static final int X1 = 3;
        static final int X2 = 6;
        static final int Y = 1;
        static final int Y0 = 1;
        static final int Y1 = 4;
        static final int Y2 = 7;
        static final int Z = 2;
        static final int Z0 = 2;
        static final int Z1 = 5;
        static final int Z2 = 8;
        BitSet marked;
        float maxXBuffer[];
        float maxYBuffer[];
        float maxZBuffer[];
        float minXBuffer[];
        float minYBuffer[];
        float minZBuffer[];
        PGraphicsOpenGL pg;
        float screenVertices[];
        BitSet swapped;
        int texMap[];
        float triA[];
        float triB[];
        int triangleIndices[];
        int voffsetMap[];

        DepthSorter(PGraphicsOpenGL pgraphicsopengl)
        {
            triangleIndices = new int[0];
            texMap = new int[0];
            voffsetMap = new int[0];
            minXBuffer = new float[0];
            minYBuffer = new float[0];
            minZBuffer = new float[0];
            maxXBuffer = new float[0];
            maxYBuffer = new float[0];
            maxZBuffer = new float[0];
            screenVertices = new float[0];
            triA = new float[9];
            triB = new float[9];
            marked = new BitSet();
            swapped = new BitSet();
            pg = pgraphicsopengl;
        }
    }

    protected static class GLResourceFrameBuffer extends WeakReference
    {

        private void disposeNative()
        {
            if(pgl != null)
            {
                if(glFbo != 0)
                {
                    PGraphicsOpenGL.intBuffer.put(0, glFbo);
                    pgl.deleteFramebuffers(1, PGraphicsOpenGL.intBuffer);
                    glFbo = 0;
                }
                if(glDepth != 0)
                {
                    PGraphicsOpenGL.intBuffer.put(0, glDepth);
                    pgl.deleteRenderbuffers(1, PGraphicsOpenGL.intBuffer);
                    glDepth = 0;
                }
                if(glStencil != 0)
                {
                    PGraphicsOpenGL.intBuffer.put(0, glStencil);
                    pgl.deleteRenderbuffers(1, PGraphicsOpenGL.intBuffer);
                    glStencil = 0;
                }
                if(glDepthStencil != 0)
                {
                    PGraphicsOpenGL.intBuffer.put(0, glDepthStencil);
                    pgl.deleteRenderbuffers(1, PGraphicsOpenGL.intBuffer);
                    glDepthStencil = 0;
                }
                if(glMultisample != 0)
                {
                    PGraphicsOpenGL.intBuffer.put(0, glMultisample);
                    pgl.deleteRenderbuffers(1, PGraphicsOpenGL.intBuffer);
                    glMultisample = 0;
                }
                pgl = null;
            }
        }

        static void drainRefQueueBounded()
        {
            ReferenceQueue referencequeue = referenceQueue();
            int i = 0;
            do
            {
                GLResourceFrameBuffer glresourceframebuffer;
label0:
                {
                    if(i < 10)
                    {
                        glresourceframebuffer = (GLResourceFrameBuffer)referencequeue.poll();
                        if(glresourceframebuffer != null)
                            break label0;
                    }
                    return;
                }
                glresourceframebuffer.dispose();
                i++;
            } while(true);
        }

        static ReferenceQueue referenceQueue()
        {
            return refQueue;
        }

        void dispose()
        {
            refList.remove(this);
            disposeNative();
        }

        public boolean equals(Object obj)
        {
            obj = (GLResourceFrameBuffer)obj;
            boolean flag;
            if(((GLResourceFrameBuffer) (obj)).glFbo == glFbo && ((GLResourceFrameBuffer) (obj)).glDepth == glDepth && ((GLResourceFrameBuffer) (obj)).glStencil == glStencil && ((GLResourceFrameBuffer) (obj)).glDepthStencil == glDepthStencil && ((GLResourceFrameBuffer) (obj)).glMultisample == glMultisample && ((GLResourceFrameBuffer) (obj)).context == context)
                flag = true;
            else
                flag = false;
            return flag;
        }

        public int hashCode()
        {
            return (((((glFbo + 527) * 31 + glDepth) * 31 + glStencil) * 31 + glDepthStencil) * 31 + glMultisample) * 31 + context;
        }

        private static List refList = new ArrayList();
        private static ReferenceQueue refQueue = new ReferenceQueue();
        private int context;
        int glDepth;
        int glDepthStencil;
        int glFbo;
        int glMultisample;
        int glStencil;
        private PGL pgl;


        public GLResourceFrameBuffer(FrameBuffer framebuffer)
        {
            super(framebuffer, refQueue);
            drainRefQueueBounded();
            pgl = framebuffer.pg.getPrimaryPGL();
            if(framebuffer.screenFb) goto _L2; else goto _L1
_L1:
            pgl.genFramebuffers(1, PGraphicsOpenGL.intBuffer);
            framebuffer.glFbo = PGraphicsOpenGL.intBuffer.get(0);
            if(framebuffer.multisample)
            {
                pgl.genRenderbuffers(1, PGraphicsOpenGL.intBuffer);
                framebuffer.glMultisample = PGraphicsOpenGL.intBuffer.get(0);
            }
            if(!framebuffer.packedDepthStencil) goto _L4; else goto _L3
_L3:
            pgl.genRenderbuffers(1, PGraphicsOpenGL.intBuffer);
            framebuffer.glDepthStencil = PGraphicsOpenGL.intBuffer.get(0);
_L6:
            glFbo = framebuffer.glFbo;
            glDepth = framebuffer.glDepth;
            glStencil = framebuffer.glStencil;
            glDepthStencil = framebuffer.glDepthStencil;
            glMultisample = framebuffer.glMultisample;
_L2:
            context = framebuffer.context;
            refList.add(this);
            return;
_L4:
            if(framebuffer.depthBits > 0)
            {
                pgl.genRenderbuffers(1, PGraphicsOpenGL.intBuffer);
                framebuffer.glDepth = PGraphicsOpenGL.intBuffer.get(0);
            }
            if(framebuffer.stencilBits > 0)
            {
                pgl.genRenderbuffers(1, PGraphicsOpenGL.intBuffer);
                framebuffer.glStencil = PGraphicsOpenGL.intBuffer.get(0);
            }
            if(true) goto _L6; else goto _L5
_L5:
        }
    }

    protected static class GLResourceShader extends WeakReference
    {

        private void disposeNative()
        {
            if(pgl != null)
            {
                if(glFragment != 0)
                {
                    pgl.deleteShader(glFragment);
                    glFragment = 0;
                }
                if(glVertex != 0)
                {
                    pgl.deleteShader(glVertex);
                    glVertex = 0;
                }
                if(glProgram != 0)
                {
                    pgl.deleteProgram(glProgram);
                    glProgram = 0;
                }
                pgl = null;
            }
        }

        static void drainRefQueueBounded()
        {
            ReferenceQueue referencequeue = referenceQueue();
            int i = 0;
            do
            {
                GLResourceShader glresourceshader;
label0:
                {
                    if(i < 10)
                    {
                        glresourceshader = (GLResourceShader)referencequeue.poll();
                        if(glresourceshader != null)
                            break label0;
                    }
                    return;
                }
                glresourceshader.dispose();
                i++;
            } while(true);
        }

        static ReferenceQueue referenceQueue()
        {
            return refQueue;
        }

        void dispose()
        {
            refList.remove(this);
            disposeNative();
        }

        public boolean equals(Object obj)
        {
            obj = (GLResourceShader)obj;
            boolean flag;
            if(((GLResourceShader) (obj)).glProgram == glProgram && ((GLResourceShader) (obj)).glVertex == glVertex && ((GLResourceShader) (obj)).glFragment == glFragment && ((GLResourceShader) (obj)).context == context)
                flag = true;
            else
                flag = false;
            return flag;
        }

        public int hashCode()
        {
            return (((glProgram + 527) * 31 + glVertex) * 31 + glFragment) * 31 + context;
        }

        private static List refList = new ArrayList();
        private static ReferenceQueue refQueue = new ReferenceQueue();
        private int context;
        int glFragment;
        int glProgram;
        int glVertex;
        private PGL pgl;


        public GLResourceShader(PShader pshader)
        {
            super(pshader, refQueue);
            drainRefQueueBounded();
            pgl = pshader.pgl.graphics.getPrimaryPGL();
            pshader.glProgram = pgl.createProgram();
            pshader.glVertex = pgl.createShader(PGL.VERTEX_SHADER);
            pshader.glFragment = pgl.createShader(PGL.FRAGMENT_SHADER);
            glProgram = pshader.glProgram;
            glVertex = pshader.glVertex;
            glFragment = pshader.glFragment;
            context = pshader.context;
            refList.add(this);
        }
    }

    protected static class GLResourceTexture extends WeakReference
    {

        private void disposeNative()
        {
            if(pgl != null)
            {
                if(glName != 0)
                {
                    PGraphicsOpenGL.intBuffer.put(0, glName);
                    pgl.deleteTextures(1, PGraphicsOpenGL.intBuffer);
                    glName = 0;
                }
                pgl = null;
            }
        }

        static void drainRefQueueBounded()
        {
            ReferenceQueue referencequeue = referenceQueue();
            int i = 0;
            do
            {
                GLResourceTexture glresourcetexture;
label0:
                {
                    if(i < 10)
                    {
                        glresourcetexture = (GLResourceTexture)referencequeue.poll();
                        if(glresourcetexture != null)
                            break label0;
                    }
                    return;
                }
                glresourcetexture.dispose();
                i++;
            } while(true);
        }

        static ReferenceQueue referenceQueue()
        {
            return refQueue;
        }

        void dispose()
        {
            refList.remove(this);
            disposeNative();
        }

        public boolean equals(Object obj)
        {
            obj = (GLResourceTexture)obj;
            boolean flag;
            if(((GLResourceTexture) (obj)).glName == glName && ((GLResourceTexture) (obj)).context == context)
                flag = true;
            else
                flag = false;
            return flag;
        }

        public int hashCode()
        {
            return (glName + 527) * 31 + context;
        }

        private static List refList = new ArrayList();
        private static ReferenceQueue refQueue = new ReferenceQueue();
        private int context;
        int glName;
        private PGL pgl;


        public GLResourceTexture(Texture texture1)
        {
            super(texture1, refQueue);
            drainRefQueueBounded();
            pgl = texture1.pg.getPrimaryPGL();
            pgl.genTextures(1, PGraphicsOpenGL.intBuffer);
            texture1.glName = PGraphicsOpenGL.intBuffer.get(0);
            glName = texture1.glName;
            context = texture1.context;
            refList.add(this);
        }
    }

    protected static class GLResourceVertexBuffer extends WeakReference
    {

        private void disposeNative()
        {
            if(pgl != null)
            {
                if(glId != 0)
                {
                    PGraphicsOpenGL.intBuffer.put(0, glId);
                    pgl.deleteBuffers(1, PGraphicsOpenGL.intBuffer);
                    glId = 0;
                }
                pgl = null;
            }
        }

        static void drainRefQueueBounded()
        {
            ReferenceQueue referencequeue = referenceQueue();
            int i = 0;
            do
            {
                GLResourceVertexBuffer glresourcevertexbuffer;
label0:
                {
                    if(i < 10)
                    {
                        glresourcevertexbuffer = (GLResourceVertexBuffer)referencequeue.poll();
                        if(glresourcevertexbuffer != null)
                            break label0;
                    }
                    return;
                }
                glresourcevertexbuffer.dispose();
                i++;
            } while(true);
        }

        static ReferenceQueue referenceQueue()
        {
            return refQueue;
        }

        void dispose()
        {
            refList.remove(this);
            disposeNative();
        }

        public boolean equals(Object obj)
        {
            obj = (GLResourceVertexBuffer)obj;
            boolean flag;
            if(((GLResourceVertexBuffer) (obj)).glId == glId && ((GLResourceVertexBuffer) (obj)).context == context)
                flag = true;
            else
                flag = false;
            return flag;
        }

        public int hashCode()
        {
            return (glId + 527) * 31 + context;
        }

        private static List refList = new ArrayList();
        private static ReferenceQueue refQueue = new ReferenceQueue();
        private int context;
        int glId;
        private PGL pgl;


        public GLResourceVertexBuffer(VertexBuffer vertexbuffer)
        {
            super(vertexbuffer, refQueue);
            drainRefQueueBounded();
            pgl = vertexbuffer.pgl.graphics.getPrimaryPGL();
            pgl.genBuffers(1, PGraphicsOpenGL.intBuffer);
            vertexbuffer.glId = PGraphicsOpenGL.intBuffer.get(0);
            glId = vertexbuffer.glId;
            context = vertexbuffer.context;
            refList.add(this);
        }
    }

    protected static class InGeometry
    {

        void addArc(float f, float f1, float f2, float f3, float f4, float f5, boolean flag, 
                boolean flag1, int i)
        {
            float f6 = f2 / 2.0F;
            float f7 = f3 / 2.0F;
            float f8 = f + f6;
            float f9 = f1 + f7;
            int j = (int)(0.5F + (f4 / 6.283185F) * 720F);
            int k = (int)(0.5F + (f5 / 6.283185F) * 720F);
            int l = PApplet.constrain(k - j, 0, 720);
            boolean flag2;
            int i1;
            int j1;
            int k1;
            if(l == 720)
                flag2 = true;
            else
                flag2 = false;
            i1 = l;
            j1 = k;
            if(flag2)
            {
                i1 = l;
                j1 = k;
                if(i == 2)
                {
                    i1 = l - 1;
                    j1 = k - 1;
                }
            }
            l = j % 720;
            k = l;
            if(l < 0)
                k = l + 720;
            l = j1 % 720;
            j1 = l;
            if(l < 0)
                j1 = l + 720;
            if(i == 2 || i == 1)
            {
                float f10 = trimAttribs[k];
                f4 = trimAttribs[j1];
                f5 = trimCodes[k];
                j1 = addVertex((f10 + f4) * 0.5F * f6 + f8, (trimCodes[j1] + f5) * 0.5F * f7 + f9, 0, true);
            } else
            {
                j1 = addVertex(f8, f9, 0, true);
            }
            k1 = PApplet.max(1, 720 / PApplet.min(200, PApplet.max(20, (int)((PApplet.dist(pg.screenX(f, f1), pg.screenY(f, f1), pg.screenX(f + f2, f1 + f3), pg.screenY(f + f2, f1 + f3)) * 6.283185F) / 10F))));
            j = -k1;
            l = j1;
            do
            {
                int l1 = PApplet.min(j + k1, i1);
                int i2 = k + l1;
                j = i2;
                if(i2 >= 720)
                    j = i2 - 720;
                f1 = trimAttribs[j];
                f = trimCodes[j];
                boolean flag3;
                if(l1 == 0 && !flag)
                    flag3 = true;
                else
                    flag3 = false;
                i2 = addVertex(f1 * f6 + f8, f9 + f * f7, 0, flag3);
                if(flag1)
                    if(i == 2 || i == 3)
                    {
                        if(l1 == 0)
                            flag3 = true;
                        else
                            flag3 = false;
                        addEdge(l, i2, flag3, false);
                    } else
                    if(l1 > 0)
                    {
                        boolean flag4;
                        boolean flag5;
                        if(l1 == PApplet.min(k1, i1))
                            flag4 = true;
                        else
                            flag4 = false;
                        if(l1 == i1 && !flag2)
                            flag5 = true;
                        else
                            flag5 = false;
                        addEdge(l, i2, flag4, flag5);
                    }
                if(l1 >= i1)
                {
                    if(flag1)
                        if(i == 2 || i == 3)
                        {
                            addEdge(i2, j1, false, false);
                            closeEdge(i2, j1);
                        } else
                        if(flag2)
                            closeEdge(l, i2);
                    return;
                }
                j = l1;
                l = i2;
            } while(true);
        }

        public void addBezierVertex(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
                float f7, float f8, boolean flag)
        {
            addVertex(f, f1, f2, 1, flag);
            addVertex(f3, f4, f5, -1, false);
            addVertex(f6, f7, f8, -1, false);
        }

        void addBox(float f, float f1, float f2, boolean flag, boolean flag1)
        {
            int i;
            int j;
            int k;
            int l;
            float f3;
            float f4;
            float f5;
            if(f1 > 0.0F)
                i = 1;
            else
                i = 0;
            if(f2 > 0.0F)
                j = 1;
            else
                j = 0;
            if(i != j)
                i = 1;
            else
                i = 0;
            if(f > 0.0F)
                j = 1;
            else
                j = 0;
            if(f2 > 0.0F)
                k = 1;
            else
                k = 0;
            if(j != k)
                k = 1;
            else
                k = 0;
            if(f > 0.0F)
                j = 1;
            else
                j = 0;
            if(f1 > 0.0F)
                l = 1;
            else
                l = 0;
            if(j != l)
                l = 1;
            else
                l = 0;
            if(i != 0)
                j = -1;
            else
                j = 1;
            if(k != 0)
                i = -1;
            else
                i = 1;
            if(l != 0)
                k = -1;
            else
                k = 1;
            f3 = -f / 2.0F;
            f /= 2.0F;
            f4 = -f1 / 2.0F;
            f1 /= 2.0F;
            f5 = -f2 / 2.0F;
            f2 /= 2.0F;
            if(flag || flag1)
            {
                setNormal(0.0F, 0.0F, -k);
                l = addVertex(f3, f4, f5, 0.0F, 0.0F, 0, true);
                int i1 = addVertex(f3, f1, f5, 0.0F, 1.0F, 0, false);
                int j1 = addVertex(f, f1, f5, 1.0F, 1.0F, 0, false);
                int k1 = addVertex(f, f4, f5, 1.0F, 0.0F, 0, false);
                if(flag1)
                {
                    addEdge(l, i1, true, false);
                    addEdge(i1, j1, false, false);
                    addEdge(j1, k1, false, false);
                    addEdge(k1, l, false, false);
                    closeEdge(k1, l);
                }
                setNormal(0.0F, 0.0F, k);
                k = addVertex(f3, f1, f2, 1.0F, 1.0F, 0, false);
                i1 = addVertex(f3, f4, f2, 1.0F, 0.0F, 0, false);
                j1 = addVertex(f, f4, f2, 0.0F, 0.0F, 0, false);
                l = addVertex(f, f1, f2, 0.0F, 1.0F, 0, false);
                if(flag1)
                {
                    addEdge(k, i1, true, false);
                    addEdge(i1, j1, false, false);
                    addEdge(j1, l, false, false);
                    addEdge(l, k, false, false);
                    closeEdge(l, k);
                }
                setNormal(j, 0.0F, 0.0F);
                k = addVertex(f, f4, f5, 0.0F, 0.0F, 0, false);
                i1 = addVertex(f, f1, f5, 0.0F, 1.0F, 0, false);
                l = addVertex(f, f1, f2, 1.0F, 1.0F, 0, false);
                j1 = addVertex(f, f4, f2, 1.0F, 0.0F, 0, false);
                if(flag1)
                {
                    addEdge(k, i1, true, false);
                    addEdge(i1, l, false, false);
                    addEdge(l, j1, false, false);
                    addEdge(j1, k, false, false);
                    closeEdge(j1, k);
                }
                setNormal(-j, 0.0F, 0.0F);
                l = addVertex(f3, f1, f5, 1.0F, 1.0F, 0, false);
                i1 = addVertex(f3, f4, f5, 1.0F, 0.0F, 0, false);
                k = addVertex(f3, f4, f2, 0.0F, 0.0F, 0, false);
                j = addVertex(f3, f1, f2, 0.0F, 1.0F, 0, false);
                if(flag1)
                {
                    addEdge(l, i1, true, false);
                    addEdge(i1, k, false, false);
                    addEdge(k, j, false, false);
                    addEdge(j, l, false, false);
                    closeEdge(j, l);
                }
                setNormal(0.0F, -i, 0.0F);
                l = addVertex(f, f4, f5, 1.0F, 1.0F, 0, false);
                i1 = addVertex(f, f4, f2, 1.0F, 0.0F, 0, false);
                k = addVertex(f3, f4, f2, 0.0F, 0.0F, 0, false);
                j = addVertex(f3, f4, f5, 0.0F, 1.0F, 0, false);
                if(flag1)
                {
                    addEdge(l, i1, true, false);
                    addEdge(i1, k, false, false);
                    addEdge(k, j, false, false);
                    addEdge(j, l, false, false);
                    closeEdge(j, l);
                }
                setNormal(0.0F, i, 0.0F);
                l = addVertex(f3, f1, f5, 0.0F, 0.0F, 0, false);
                j = addVertex(f3, f1, f2, 0.0F, 1.0F, 0, false);
                i = addVertex(f, f1, f2, 1.0F, 1.0F, 0, false);
                k = addVertex(f, f1, f5, 1.0F, 0.0F, 0, false);
                if(flag1)
                {
                    addEdge(l, j, true, false);
                    addEdge(j, i, false, false);
                    addEdge(i, k, false, false);
                    addEdge(k, l, false, false);
                    closeEdge(k, l);
                }
            }
        }

        public void addCurveVertex(float f, float f1, float f2, boolean flag)
        {
            addVertex(f, f1, f2, 3, flag);
        }

        int addEdge(int i, int j, boolean flag, boolean flag1)
        {
            boolean flag2 = true;
            edgeCheck();
            int ai[] = edges[edgeCount];
            ai[0] = i;
            ai[1] = j;
            if(flag)
                i = 1;
            else
                i = 0;
            if(flag1)
                j = ((flag2) ? 1 : 0);
            else
                j = 0;
            ai[2] = j * 2 + i;
            edgeCount = edgeCount + 1;
            return edgeCount - 1;
        }

        void addEllipse(float f, float f1, float f2, float f3, boolean flag, boolean flag1)
        {
            float f4 = f2 / 2.0F;
            float f5 = f3 / 2.0F;
            float f6 = f + f4;
            float f7 = f1 + f5;
            int i = PApplet.min(200, PApplet.max(20, (int)((PApplet.dist(pg.screenX(f, f1), pg.screenY(f, f1), pg.screenX(f + f2, f1 + f3), pg.screenY(f + f2, f1 + f3)) * 6.283185F) / 10F)));
            f1 = 720F / (float)i;
            if(flag)
                addVertex(f6, f7, 0, true);
            int j = 0;
            int k = 0;
            f = 0.0F;
            int l = 0;
            int i1 = 0;
            while(k < i) 
            {
                f2 = trimAttribs[(int)f];
                f3 = trimCodes[(int)f];
                boolean flag2;
                if(k == 0 && !flag)
                    flag2 = true;
                else
                    flag2 = false;
                j = addVertex(f6 + f2 * f4, f7 + f3 * f5, 0, flag2);
                f = (f + f1) % 720F;
                if(k > 0)
                {
                    if(flag1)
                    {
                        if(k == 1)
                            flag2 = true;
                        else
                            flag2 = false;
                        addEdge(l, j, flag2, false);
                    }
                } else
                {
                    i1 = j;
                }
                k++;
                l = j;
            }
            addVertex(trimAttribs[0] * f4 + f6, trimCodes[0] * f5 + f7, 0, false);
            if(flag1)
            {
                addEdge(j, i1, false, false);
                closeEdge(j, i1);
            }
        }

        void addLine(float f, float f1, float f2, float f3, float f4, float f5, boolean flag, 
                boolean flag1)
        {
            int i = addVertex(f, f1, f2, 0, true);
            int j = addVertex(f3, f4, f5, 0, false);
            if(flag1)
                addEdge(i, j, true, true);
        }

        void addPoint(float f, float f1, float f2, boolean flag, boolean flag1)
        {
            addVertex(f, f1, f2, 0, true);
        }

        void addQuad(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
                float f7, float f8, float f9, float f10, float f11, boolean flag)
        {
            int i = addVertex(f, f1, f2, 0.0F, 0.0F, 0, true);
            int j = addVertex(f3, f4, f5, 1.0F, 0.0F, 0, false);
            int k = addVertex(f6, f7, f8, 1.0F, 1.0F, 0, false);
            int l = addVertex(f9, f10, f11, 0.0F, 1.0F, 0, false);
            if(flag)
            {
                addEdge(i, j, true, false);
                addEdge(j, k, false, false);
                addEdge(k, l, false, false);
                addEdge(l, i, false, false);
                closeEdge(l, i);
            }
        }

        void addQuadStripEdges()
        {
            for(int i = 1; i < vertexCount / 2; i++)
            {
                int j = (i - 1) * 2;
                int k = (i - 1) * 2 + 1;
                int l = i * 2 + 1;
                int i1 = i * 2;
                addEdge(j, k, true, false);
                addEdge(k, l, false, false);
                addEdge(l, i1, false, false);
                addEdge(i1, j, false, true);
                closeEdge(i1, j);
            }

        }

        public void addQuadraticVertex(float f, float f1, float f2, float f3, float f4, float f5, boolean flag)
        {
            addVertex(f, f1, f2, 2, flag);
            addVertex(f3, f4, f5, -1, false);
        }

        void addQuadsEdges()
        {
            for(int i = 0; i < vertexCount / 4; i++)
            {
                int j = i * 4 + 0;
                int k = i * 4 + 1;
                int l = i * 4 + 2;
                int i1 = i * 4 + 3;
                addEdge(j, k, true, false);
                addEdge(k, l, false, false);
                addEdge(l, i1, false, false);
                addEdge(i1, j, false, false);
                closeEdge(i1, j);
            }

        }

        void addRect(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
                float f7, boolean flag)
        {
            if(PGraphicsOpenGL.nonZero(f5))
            {
                addVertex(f2 - f5, f1, 0, true);
                addQuadraticVertex(f2, f1, 0.0F, f2, f1 + f5, 0.0F, false);
            } else
            {
                addVertex(f2, f1, 0, true);
            }
            if(PGraphicsOpenGL.nonZero(f6))
            {
                addVertex(f2, f3 - f6, 0, false);
                addQuadraticVertex(f2, f3, 0.0F, f2 - f6, f3, 0.0F, false);
            } else
            {
                addVertex(f2, f3, 0, false);
            }
            if(PGraphicsOpenGL.nonZero(f7))
            {
                addVertex(f + f7, f3, 0, false);
                addQuadraticVertex(f, f3, 0.0F, f, f3 - f7, 0.0F, false);
            } else
            {
                addVertex(f, f3, 0, false);
            }
            if(PGraphicsOpenGL.nonZero(f4))
            {
                addVertex(f, f1 + f4, 0, false);
                addQuadraticVertex(f, f1, 0.0F, f + f4, f1, 0.0F, false);
            } else
            {
                addVertex(f, f1, 0, false);
            }
        }

        void addRect(float f, float f1, float f2, float f3, boolean flag)
        {
            addQuad(f, f1, 0.0F, f2, f1, 0.0F, f2, f3, 0.0F, f, f3, 0.0F, flag);
        }

        int[] addSphere(float f, int i, int j, boolean flag, boolean flag1)
        {
            int ai[] = new int[i * 3 + (i * 6 + 3) * (j - 2) + i * 3];
            float f1 = 1.0F / (float)i;
            float f2 = 1.0F / (float)j;
            float f3 = 1.0F;
            for(int k = 0; k < i; k++)
            {
                setNormal(0.0F, 1.0F, 0.0F);
                addVertex(0.0F, f, 0.0F, f3, 1.0F, 0, true);
                f3 -= f1;
            }

            f3 = 1.0F;
            float f4 = 1.0F - f2;
            for(int l = 0; l < i; l++)
            {
                setNormal(((trimIntAttrib) (pg)).trimIntAttrib[l], ((InGeometry) (pg)).trimBoolAttrib[l], ((InGeometry) (pg)).expandColors[l]);
                addVertex(f * ((trimIntAttrib) (pg)).trimIntAttrib[l], f * ((InGeometry) (pg)).trimBoolAttrib[l], f * ((InGeometry) (pg)).expandColors[l], f3, f4, 0, false);
                f3 -= f1;
            }

            setNormal(((trimIntAttrib) (pg)).trimIntAttrib[0], ((InGeometry) (pg)).trimBoolAttrib[0], ((InGeometry) (pg)).expandColors[0]);
            addVertex(f * ((trimIntAttrib) (pg)).trimIntAttrib[0], f * ((InGeometry) (pg)).trimBoolAttrib[0], f * ((InGeometry) (pg)).expandColors[0], f3, f4, 0, false);
            int l1 = i + i + 1;
            for(int i1 = 0; i1 < i; i1++)
            {
                int j2 = i + i1;
                int l2 = (i + i1) - i;
                ai[i1 * 3 + 0] = j2;
                ai[i1 * 3 + 1] = l2;
                ai[i1 * 3 + 2] = j2 + 1;
                addEdge(l2, j2, true, true);
                addEdge(j2, j2 + 1, true, true);
            }

            byte byte0 = 2;
            int k2 = i;
            int i3 = 0 + i * 3;
            int l3 = 0;
            int j1 = l1;
            l1 = byte0;
            f3 = f4;
            while(l1 < j) 
            {
                l3 += i;
                float f6 = 1.0F;
                float f5 = f3 - f2;
                k2 = 0;
                f3 = f6;
                for(; k2 < i; k2++)
                {
                    int j3 = l3 + k2;
                    setNormal(((trimIntAttrib) (pg)).trimIntAttrib[j3], ((InGeometry) (pg)).trimBoolAttrib[j3], ((InGeometry) (pg)).expandColors[j3]);
                    addVertex(((trimIntAttrib) (pg)).trimIntAttrib[j3] * f, ((InGeometry) (pg)).trimBoolAttrib[j3] * f, f * ((InGeometry) (pg)).expandColors[j3], f3, f5, 0, false);
                    f3 -= f1;
                }

                int k3 = j1 + i;
                setNormal(((trimIntAttrib) (pg)).trimIntAttrib[l3], ((InGeometry) (pg)).trimBoolAttrib[l3], ((InGeometry) (pg)).expandColors[l3]);
                addVertex(f * ((trimIntAttrib) (pg)).trimIntAttrib[l3], f * ((InGeometry) (pg)).trimBoolAttrib[l3], f * ((InGeometry) (pg)).expandColors[l3], f3, f5, 0, false);
                for(k2 = 0; k2 < i; k2++)
                {
                    int i4 = j1 + k2;
                    int j4 = (j1 + k2) - i - 1;
                    ai[k2 * 6 + i3 + 0] = i4;
                    ai[k2 * 6 + i3 + 1] = j4;
                    ai[k2 * 6 + i3 + 2] = j4 + 1;
                    ai[k2 * 6 + i3 + 3] = i4;
                    ai[k2 * 6 + i3 + 4] = j4 + 1;
                    ai[k2 * 6 + i3 + 5] = i4 + 1;
                    addEdge(j4, i4, true, true);
                    addEdge(i4, i4 + 1, true, true);
                    addEdge(j4 + 1, i4, true, true);
                }

                i3 = i * 6 + i3;
                ai[i3 + 0] = k3;
                ai[i3 + 1] = k3 - i;
                ai[i3 + 2] = k3 - 1;
                l1++;
                i3 += 3;
                k3++;
                f3 = f5;
                k2 = j1;
                j1 = k3;
            }
            f3 = 1.0F;
            for(j = 0; j < i; j++)
            {
                setNormal(0.0F, -1F, 0.0F);
                addVertex(0.0F, -f, 0.0F, f3, 0.0F, 0, false);
                f3 -= f1;
            }

            for(j = 0; j < i; j++)
            {
                int i2 = k2 + j;
                int k1 = k2 + j + i + 1;
                ai[j * 3 + i3 + 0] = k1;
                ai[j * 3 + i3 + 1] = i2;
                ai[j * 3 + i3 + 2] = i2 + 1;
                addEdge(i2, i2 + 1, true, true);
                addEdge(i2, k1, true, true);
            }

            return ai;
        }

        void addTriangle(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
                float f7, float f8, boolean flag, boolean flag1)
        {
            int i = addVertex(f, f1, f2, 0, true);
            int j = addVertex(f3, f4, f5, 0, false);
            int k = addVertex(f6, f7, f8, 0, false);
            if(flag1)
            {
                addEdge(i, j, true, false);
                addEdge(j, k, false, false);
                addEdge(k, i, false, false);
                closeEdge(k, i);
            }
        }

        void addTriangleFanEdges()
        {
            for(int i = 1; i < vertexCount - 1; i++)
            {
                int j = i + 1;
                addEdge(0, i, true, false);
                addEdge(i, j, false, false);
                addEdge(j, 0, false, false);
                closeEdge(j, 0);
            }

        }

        void addTriangleStripEdges()
        {
            int i = 1;
            while(i < vertexCount - 1) 
            {
                int j;
                int k;
                if(i % 2 == 0)
                {
                    j = i - 1;
                    k = i + 1;
                } else
                {
                    j = i + 1;
                    k = i - 1;
                }
                addEdge(i, j, true, false);
                addEdge(j, k, false, false);
                addEdge(k, i, false, false);
                closeEdge(k, i);
                i++;
            }
        }

        void addTrianglesEdges()
        {
            for(int i = 0; i < vertexCount / 3; i++)
            {
                int j = i * 3 + 0;
                int k = i * 3 + 1;
                int l = i * 3 + 2;
                addEdge(j, k, true, false);
                addEdge(k, l, false, false);
                addEdge(l, j, false, false);
                closeEdge(l, j);
            }

        }

        int addVertex(float f, float f1, float f2, float f3, float f4, int i, boolean flag)
        {
            return addVertex(f, f1, f2, fillColor, normalX, normalY, normalZ, f3, f4, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininessFactor, i, flag);
        }

        int addVertex(float f, float f1, float f2, float f3, float f4, boolean flag)
        {
            return addVertex(f, f1, f2, fillColor, normalX, normalY, normalZ, f3, f4, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininessFactor, 0, flag);
        }

        int addVertex(float f, float f1, float f2, float f3, int i, boolean flag)
        {
            return addVertex(f, f1, 0.0F, fillColor, normalX, normalY, normalZ, f2, f3, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininessFactor, i, flag);
        }

        int addVertex(float f, float f1, float f2, float f3, boolean flag)
        {
            return addVertex(f, f1, 0.0F, fillColor, normalX, normalY, normalZ, f2, f3, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininessFactor, 0, flag);
        }

        int addVertex(float f, float f1, float f2, int i, float f3, float f4, float f5, 
                float f6, float f7, int j, float f8, int k, int l, int i1, 
                float f9, int j1, boolean flag)
        {
            vertexCheck();
            int k1 = vertexCount * 3;
            float af[] = vertices;
            int l1 = k1 + 1;
            af[k1] = f;
            vertices[l1] = f1;
            vertices[l1 + 1] = f2;
            colors[vertexCount] = PGL.javaToNativeARGB(i);
            i = vertexCount * 3;
            af = normals;
            l1 = i + 1;
            af[i] = f3;
            normals[l1] = f4;
            normals[l1 + 1] = f5;
            i = vertexCount * 2;
            texcoords[i] = f6;
            texcoords[i + 1] = f7;
            strokeColors[vertexCount] = PGL.javaToNativeARGB(j);
            strokeWeights[vertexCount] = f8;
            ambient[vertexCount] = PGL.javaToNativeARGB(k);
            specular[vertexCount] = PGL.javaToNativeARGB(l);
            emissive[vertexCount] = PGL.javaToNativeARGB(i1);
            shininess[vertexCount] = f9;
            Iterator iterator = attribs.keySet().iterator();
            do
            {
                if(!iterator.hasNext())
                    break;
                String s = (String)iterator.next();
                VertexAttribute vertexattribute = (VertexAttribute)attribs.get(s);
                i = vertexattribute.size * vertexCount;
                if(vertexattribute.type == PGL.FLOAT)
                    vertexattribute.add((float[])fattribs.get(s), i);
                else
                if(vertexattribute.type == PGL.INT)
                    vertexattribute.add((int[])iattribs.get(s), i);
                else
                if(vertexattribute.type == PGL.BOOL)
                    vertexattribute.add((byte[])battribs.get(s), i);
            } while(true);
            if(flag || j1 == 0 && codes != null || j1 == 1 || j1 == 2 || j1 == 3)
            {
                if(codes == null)
                {
                    codes = new int[PApplet.max(PGL.DEFAULT_IN_VERTICES, vertexCount)];
                    Arrays.fill(codes, 0, vertexCount, 0);
                    codeCount = vertexCount;
                }
                if(flag)
                {
                    codeCheck();
                    codes[codeCount] = 4;
                    codeCount = codeCount + 1;
                }
                if(j1 != -1)
                {
                    codeCheck();
                    codes[codeCount] = j1;
                    codeCount = codeCount + 1;
                }
            }
            vertexCount = vertexCount + 1;
            return vertexCount - 1;
        }

        int addVertex(float f, float f1, float f2, int i, boolean flag)
        {
            return addVertex(f, f1, f2, fillColor, normalX, normalY, normalZ, 0.0F, 0.0F, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininessFactor, i, flag);
        }

        int addVertex(float f, float f1, float f2, boolean flag)
        {
            return addVertex(f, f1, f2, fillColor, normalX, normalY, normalZ, 0.0F, 0.0F, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininessFactor, 0, flag);
        }

        int addVertex(float f, float f1, int i, boolean flag)
        {
            return addVertex(f, f1, 0.0F, fillColor, normalX, normalY, normalZ, 0.0F, 0.0F, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininessFactor, i, flag);
        }

        int addVertex(float f, float f1, boolean flag)
        {
            return addVertex(f, f1, 0.0F, fillColor, normalX, normalY, normalZ, 0.0F, 0.0F, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininessFactor, 0, flag);
        }

        void allocate()
        {
            vertices = new float[PGL.DEFAULT_IN_VERTICES * 3];
            colors = new int[PGL.DEFAULT_IN_VERTICES];
            normals = new float[PGL.DEFAULT_IN_VERTICES * 3];
            texcoords = new float[PGL.DEFAULT_IN_VERTICES * 2];
            strokeColors = new int[PGL.DEFAULT_IN_VERTICES];
            strokeWeights = new float[PGL.DEFAULT_IN_VERTICES];
            ambient = new int[PGL.DEFAULT_IN_VERTICES];
            specular = new int[PGL.DEFAULT_IN_VERTICES];
            emissive = new int[PGL.DEFAULT_IN_VERTICES];
            shininess = new float[PGL.DEFAULT_IN_VERTICES];
            int i = PGL.DEFAULT_IN_EDGES;
            edges = new int[i][3];
            fattribs = new HashMap();
            iattribs = new HashMap();
            battribs = new HashMap();
            clear();
        }

        void calcQuadStripNormals()
        {
            for(int i = 1; i < vertexCount / 2; i++)
            {
                int j = (i - 1) * 2 + 1;
                int k = i * 2;
                calcTriangleNormal((i - 1) * 2, j, k);
                calcTriangleNormal(k, j, i * 2 + 1);
            }

        }

        void calcQuadsNormals()
        {
            for(int i = 0; i < vertexCount / 4; i++)
            {
                int j = i * 4 + 0;
                int k = i * 4 + 2;
                calcTriangleNormal(j, i * 4 + 1, k);
                calcTriangleNormal(k, i * 4 + 3, j);
            }

        }

        void calcTriangleFanNormals()
        {
            for(int i = 1; i < vertexCount - 1; i++)
                calcTriangleNormal(0, i, i + 1);

        }

        void calcTriangleNormal(int i, int j, int k)
        {
            int l = i * 3;
            float af[] = vertices;
            int i1 = l + 1;
            float f = af[l];
            float f1 = vertices[i1];
            float f2 = vertices[i1 + 1];
            i1 = j * 3;
            af = vertices;
            l = i1 + 1;
            float f3 = af[i1];
            float f4 = vertices[l];
            float f5 = vertices[l + 1];
            l = k * 3;
            af = vertices;
            i1 = l + 1;
            float f6 = af[l];
            float f7 = vertices[i1];
            float f8 = vertices[i1 + 1];
            f6 -= f3;
            f7 -= f4;
            f8 -= f5;
            f -= f3;
            f4 = f1 - f4;
            f2 -= f5;
            f5 = f7 * f2 - f4 * f8;
            f2 = f8 * f - f2 * f6;
            f7 = f4 * f6 - f * f7;
            f6 = PApplet.sqrt(f5 * f5 + f2 * f2 + f7 * f7);
            f5 /= f6;
            f2 /= f6;
            f6 = f7 / f6;
            i *= 3;
            af = normals;
            l = i + 1;
            af[i] = f5;
            normals[l] = f2;
            normals[l + 1] = f6;
            j *= 3;
            af = normals;
            i = j + 1;
            af[j] = f5;
            normals[i] = f2;
            normals[i + 1] = f6;
            j = k * 3;
            af = normals;
            i = j + 1;
            af[j] = f5;
            normals[i] = f2;
            normals[i + 1] = f6;
        }

        void calcTriangleStripNormals()
        {
            int i = 1;
            while(i < vertexCount - 1) 
            {
                int j;
                int k;
                if(i % 2 == 1)
                {
                    j = i - 1;
                    k = i + 1;
                } else
                {
                    j = i + 1;
                    k = i - 1;
                }
                calcTriangleNormal(j, i, k);
                i++;
            }
        }

        void calcTrianglesNormals()
        {
            for(int i = 0; i < vertexCount / 3; i++)
                calcTriangleNormal(i * 3 + 0, i * 3 + 1, i * 3 + 2);

        }

        void clear()
        {
            vertexCount = 0;
            codeCount = 0;
            edgeCount = 0;
        }

        void clearEdges()
        {
            edgeCount = 0;
        }

        int closeEdge(int i, int j)
        {
            edgeCheck();
            int ai[] = edges[edgeCount];
            ai[0] = i;
            ai[1] = j;
            ai[2] = -1;
            edgeCount = edgeCount + 1;
            return edgeCount - 1;
        }

        void codeCheck()
        {
            if(codeCount == codes.length)
                expandCodes(codeCount << 1);
        }

        void edgeCheck()
        {
            if(edgeCount == edges.length)
                expandEdges(edgeCount << 1);
        }

        void expandAmbient(int i)
        {
            int ai[] = new int[i];
            PApplet.arrayCopy(ambient, 0, ai, 0, vertexCount);
            ambient = ai;
        }

        void expandAttribs(int i)
        {
            Iterator iterator = attribs.keySet().iterator();
            do
            {
                if(!iterator.hasNext())
                    break;
                Object obj = (String)iterator.next();
                obj = (VertexAttribute)attribs.get(obj);
                if(((VertexAttribute) (obj)).type == PGL.FLOAT)
                    expandFloatAttrib(((VertexAttribute) (obj)), i);
                else
                if(((VertexAttribute) (obj)).type == PGL.INT)
                    expandIntAttrib(((VertexAttribute) (obj)), i);
                else
                if(((VertexAttribute) (obj)).type == PGL.BOOL)
                    expandBoolAttrib(((VertexAttribute) (obj)), i);
            } while(true);
        }

        void expandBoolAttrib(VertexAttribute vertexattribute, int i)
        {
            byte abyte0[] = (byte[])battribs.get(vertexattribute.name);
            byte abyte1[] = new byte[vertexattribute.size * i];
            PApplet.arrayCopy(abyte0, 0, abyte1, 0, vertexattribute.size * vertexCount);
            battribs.put(vertexattribute.name, abyte1);
        }

        void expandCodes(int i)
        {
            int ai[] = new int[i];
            PApplet.arrayCopy(codes, 0, ai, 0, codeCount);
            codes = ai;
        }

        void expandColors(int i)
        {
            int ai[] = new int[i];
            PApplet.arrayCopy(colors, 0, ai, 0, vertexCount);
            colors = ai;
        }

        void expandEdges(int i)
        {
            int ai[][] = new int[i][3];
            PApplet.arrayCopy(edges, 0, ai, 0, edgeCount);
            edges = ai;
        }

        void expandEmissive(int i)
        {
            int ai[] = new int[i];
            PApplet.arrayCopy(emissive, 0, ai, 0, vertexCount);
            emissive = ai;
        }

        void expandFloatAttrib(VertexAttribute vertexattribute, int i)
        {
            float af[] = (float[])fattribs.get(vertexattribute.name);
            float af1[] = new float[vertexattribute.size * i];
            PApplet.arrayCopy(af, 0, af1, 0, vertexattribute.size * vertexCount);
            fattribs.put(vertexattribute.name, af1);
        }

        void expandIntAttrib(VertexAttribute vertexattribute, int i)
        {
            int ai[] = (int[])iattribs.get(vertexattribute.name);
            int ai1[] = new int[vertexattribute.size * i];
            PApplet.arrayCopy(ai, 0, ai1, 0, vertexattribute.size * vertexCount);
            iattribs.put(vertexattribute.name, ai1);
        }

        void expandNormals(int i)
        {
            float af[] = new float[i * 3];
            PApplet.arrayCopy(normals, 0, af, 0, vertexCount * 3);
            normals = af;
        }

        void expandShininess(int i)
        {
            float af[] = new float[i];
            PApplet.arrayCopy(shininess, 0, af, 0, vertexCount);
            shininess = af;
        }

        void expandSpecular(int i)
        {
            int ai[] = new int[i];
            PApplet.arrayCopy(specular, 0, ai, 0, vertexCount);
            specular = ai;
        }

        void expandStrokeColors(int i)
        {
            int ai[] = new int[i];
            PApplet.arrayCopy(strokeColors, 0, ai, 0, vertexCount);
            strokeColors = ai;
        }

        void expandStrokeWeights(int i)
        {
            float af[] = new float[i];
            PApplet.arrayCopy(strokeWeights, 0, af, 0, vertexCount);
            strokeWeights = af;
        }

        void expandTexCoords(int i)
        {
            float af[] = new float[i * 2];
            PApplet.arrayCopy(texcoords, 0, af, 0, vertexCount * 2);
            texcoords = af;
        }

        void expandVertices(int i)
        {
            float af[] = new float[i * 3];
            PApplet.arrayCopy(vertices, 0, af, 0, vertexCount * 3);
            vertices = af;
        }

        double[] getAttribVector(int i)
        {
            double ad[];
            int j;
            int k;
            ad = new double[attribs.numComp];
            j = 0;
            k = 0;
_L2:
            VertexAttribute vertexattribute;
            Object obj;
            int l;
            int i1;
            if(j >= attribs.size())
                break; /* Loop/switch isn't completed */
            vertexattribute = attribs.get(j);
            obj = vertexattribute.name;
            l = vertexattribute.size * i;
            if(vertexattribute.isColor())
            {
                i1 = ((int[])iattribs.get(obj))[l];
                l = k + 1;
                ad[k] = i1 >> 24 & 0xff;
                k = l + 1;
                ad[l] = i1 >> 16 & 0xff;
                l = k + 1;
                ad[k] = i1 >> 8 & 0xff;
                ad[l] = i1 >> 0 & 0xff;
                i1 = l + 1;
            } else
            if(vertexattribute.isFloat())
            {
                obj = (float[])fattribs.get(obj);
                for(i1 = 0; i1 < vertexattribute.size;)
                {
                    ad[k] = obj[l];
                    i1++;
                    l++;
                    k++;
                }

                i1 = k;
            } else
            {
label0:
                {
                    if(!vertexattribute.isInt())
                        break label0;
                    obj = (int[])iattribs.get(obj);
                    for(i1 = 0; i1 < vertexattribute.size;)
                    {
                        ad[k] = obj[l];
                        i1++;
                        l++;
                        k++;
                    }

                    i1 = k;
                }
            }
_L3:
            j++;
            k = i1;
            if(true) goto _L2; else goto _L1
            i1 = k;
            if(vertexattribute.isBool())
            {
                byte abyte0[] = (byte[])battribs.get(obj);
                for(i1 = 0; i1 < vertexattribute.size;)
                {
                    ad[k] = abyte0[l];
                    i1++;
                    l++;
                    k++;
                }

                break MISSING_BLOCK_LABEL_357;
            }
              goto _L3
_L1:
            return ad;
            i1 = k;
              goto _L3
        }

        float getLastVertexX()
        {
            return vertices[(vertexCount - 1) * 3 + 0];
        }

        float getLastVertexY()
        {
            return vertices[(vertexCount - 1) * 3 + 1];
        }

        float getLastVertexZ()
        {
            return vertices[(vertexCount - 1) * 3 + 2];
        }

        int getNumEdgeClosures()
        {
            int i = 0;
            int j;
            int k;
            for(j = 0; i < edgeCount; j = k)
            {
                k = j;
                if(edges[i][2] == -1)
                    k = j + 1;
                i++;
            }

            return j;
        }

        int getNumEdgeIndices(boolean flag)
        {
            int i = 0;
            int j = edgeCount;
            int l;
            int i1;
            if(flag)
            {
                int k = 0;
label0:
                do
                {
                    int ai[];
label1:
                    {
                        l = k;
                        i1 = j;
                        if(i >= edgeCount)
                            break label0;
                        ai = edges[i];
                        if(ai[2] != 0)
                        {
                            i1 = k;
                            if(ai[2] != 1)
                                break label1;
                        }
                        i1 = k + 1;
                    }
                    k = i1;
                    l = j;
                    if(ai[2] == -1)
                    {
                        k = i1 + 1;
                        l = j - 1;
                    }
                    i++;
                    j = l;
                } while(true);
            } else
            {
                i1 = j - getNumEdgeClosures();
                l = 0;
            }
            return (i1 + l) * 6;
        }

        int getNumEdgeVertices(boolean flag)
        {
            int i = 0;
            int j = edgeCount;
            int l;
            int i1;
            if(flag)
            {
                int k = 0;
label0:
                do
                {
                    int ai[];
label1:
                    {
                        l = k;
                        i1 = j;
                        if(i >= edgeCount)
                            break label0;
                        ai = edges[i];
                        if(ai[2] != 0)
                        {
                            i1 = k;
                            if(ai[2] != 1)
                                break label1;
                        }
                        i1 = k + 3;
                    }
                    k = i1;
                    l = j;
                    if(ai[2] == -1)
                    {
                        k = i1 + 5;
                        l = j - 1;
                    }
                    i++;
                    j = l;
                } while(true);
            } else
            {
                i1 = j - getNumEdgeClosures();
                l = 0;
            }
            return i1 * 4 + l;
        }

        float[][] getVertexData()
        {
            int i = vertexCount;
            float af[][] = new float[i][37];
            for(int j = 0; j < vertexCount; j++)
            {
                float af1[] = af[j];
                af1[0] = vertices[j * 3 + 0];
                af1[1] = vertices[j * 3 + 1];
                af1[2] = vertices[j * 3 + 2];
                af1[3] = (float)(colors[j] >> 16 & 0xff) / 255F;
                af1[4] = (float)(colors[j] >> 8 & 0xff) / 255F;
                af1[5] = (float)(colors[j] >> 0 & 0xff) / 255F;
                af1[6] = (float)(colors[j] >> 24 & 0xff) / 255F;
                af1[7] = texcoords[j * 2 + 0];
                af1[8] = texcoords[j * 2 + 1];
                af1[9] = normals[j * 3 + 0];
                af1[10] = normals[j * 3 + 1];
                af1[11] = normals[j * 3 + 2];
                af1[13] = (float)(strokeColors[j] >> 16 & 0xff) / 255F;
                af1[14] = (float)(strokeColors[j] >> 8 & 0xff) / 255F;
                af1[15] = (float)(strokeColors[j] >> 0 & 0xff) / 255F;
                af1[16] = (float)(strokeColors[j] >> 24 & 0xff) / 255F;
                af1[17] = strokeWeights[j];
            }

            return af;
        }

        void getVertexMax(PVector pvector)
        {
            for(int i = 0; i < vertexCount; i++)
            {
                int j = i * 4;
                float f = pvector.x;
                float af[] = vertices;
                int k = j + 1;
                pvector.x = PApplet.max(f, af[j]);
                pvector.y = PApplet.max(pvector.y, vertices[k]);
                pvector.z = PApplet.max(pvector.z, vertices[k + 1]);
            }

        }

        void getVertexMin(PVector pvector)
        {
            for(int i = 0; i < vertexCount; i++)
            {
                int j = i * 4;
                float f = pvector.x;
                float af[] = vertices;
                int k = j + 1;
                pvector.x = PApplet.min(f, af[j]);
                pvector.y = PApplet.min(pvector.y, vertices[k]);
                pvector.z = PApplet.min(pvector.z, vertices[k + 1]);
            }

        }

        int getVertexSum(PVector pvector)
        {
            for(int i = 0; i < vertexCount; i++)
            {
                int j = i * 4;
                float f = pvector.x;
                float af[] = vertices;
                int k = j + 1;
                pvector.x = af[j] + f;
                pvector.y = pvector.y + vertices[k];
                pvector.z = pvector.z + vertices[k + 1];
            }

            return vertexCount;
        }

        float getVertexX(int i)
        {
            return vertices[i * 3 + 0];
        }

        float getVertexY(int i)
        {
            return vertices[i * 3 + 1];
        }

        float getVertexZ(int i)
        {
            return vertices[i * 3 + 2];
        }

        boolean hasBezierVertex()
        {
            boolean flag = false;
            int i = 0;
            do
            {
label0:
                {
                    boolean flag1 = flag;
                    if(i < codeCount)
                    {
                        if(codes[i] != 1)
                            break label0;
                        flag1 = true;
                    }
                    return flag1;
                }
                i++;
            } while(true);
        }

        boolean hasCurveVertex()
        {
            boolean flag = false;
            int i = 0;
            do
            {
label0:
                {
                    boolean flag1 = flag;
                    if(i < codeCount)
                    {
                        if(codes[i] != 3)
                            break label0;
                        flag1 = true;
                    }
                    return flag1;
                }
                i++;
            } while(true);
        }

        boolean hasQuadraticVertex()
        {
            boolean flag = false;
            int i = 0;
            do
            {
label0:
                {
                    boolean flag1 = flag;
                    if(i < codeCount)
                    {
                        if(codes[i] != 2)
                            break label0;
                        flag1 = true;
                    }
                    return flag1;
                }
                i++;
            } while(true);
        }

        void initAttrib(VertexAttribute vertexattribute)
        {
            if(vertexattribute.type != PGL.FLOAT) goto _L2; else goto _L1
_L1:
            float af[] = new float[vertexattribute.size * PGL.DEFAULT_IN_VERTICES];
            fattribs.put(vertexattribute.name, af);
_L4:
            return;
_L2:
            if(vertexattribute.type == PGL.INT)
            {
                int ai[] = new int[vertexattribute.size * PGL.DEFAULT_IN_VERTICES];
                iattribs.put(vertexattribute.name, ai);
            } else
            if(vertexattribute.type == PGL.BOOL)
            {
                byte abyte0[] = new byte[vertexattribute.size * PGL.DEFAULT_IN_VERTICES];
                battribs.put(vertexattribute.name, abyte0);
            }
            if(true) goto _L4; else goto _L3
_L3:
        }

        void setMaterial(int i, int j, float f, int k, int l, int i1, float f1)
        {
            fillColor = i;
            strokeColor = j;
            strokeWeight = f;
            ambientColor = k;
            specularColor = l;
            emissiveColor = i1;
            shininessFactor = f1;
        }

        void setNormal(float f, float f1, float f2)
        {
            normalX = f;
            normalY = f1;
            normalZ = f2;
        }

        void trim()
        {
            if(vertexCount > 0 && vertexCount < vertices.length / 3)
            {
                trimVertices();
                trimColors();
                trimNormals();
                trimTexCoords();
                trimStrokeColors();
                trimStrokeWeights();
                trimAmbient();
                trimSpecular();
                trimEmissive();
                trimShininess();
                trimAttribs();
            }
            if(codeCount > 0 && codeCount < codes.length)
                trimCodes();
            if(edgeCount > 0 && edgeCount < edges.length)
                trimEdges();
        }

        void trimAmbient()
        {
            int ai[] = new int[vertexCount];
            PApplet.arrayCopy(ambient, 0, ai, 0, vertexCount);
            ambient = ai;
        }

        void trimAttribs()
        {
            Iterator iterator = attribs.keySet().iterator();
            do
            {
                if(!iterator.hasNext())
                    break;
                Object obj = (String)iterator.next();
                obj = (VertexAttribute)attribs.get(obj);
                if(((VertexAttribute) (obj)).type == PGL.FLOAT)
                    trimFloatAttrib(((VertexAttribute) (obj)));
                else
                if(((VertexAttribute) (obj)).type == PGL.INT)
                    trimIntAttrib(((VertexAttribute) (obj)));
                else
                if(((VertexAttribute) (obj)).type == PGL.BOOL)
                    trimBoolAttrib(((VertexAttribute) (obj)));
            } while(true);
        }

        void trimBoolAttrib(VertexAttribute vertexattribute)
        {
            byte abyte0[] = (byte[])battribs.get(vertexattribute.name);
            byte abyte1[] = new byte[vertexattribute.size * vertexCount];
            PApplet.arrayCopy(abyte0, 0, abyte1, 0, vertexattribute.size * vertexCount);
            battribs.put(vertexattribute.name, abyte1);
        }

        void trimCodes()
        {
            int ai[] = new int[codeCount];
            PApplet.arrayCopy(codes, 0, ai, 0, codeCount);
            codes = ai;
        }

        void trimColors()
        {
            int ai[] = new int[vertexCount];
            PApplet.arrayCopy(colors, 0, ai, 0, vertexCount);
            colors = ai;
        }

        void trimEdges()
        {
            int i = edgeCount;
            int ai[][] = new int[i][3];
            PApplet.arrayCopy(edges, 0, ai, 0, edgeCount);
            edges = ai;
        }

        void trimEmissive()
        {
            int ai[] = new int[vertexCount];
            PApplet.arrayCopy(emissive, 0, ai, 0, vertexCount);
            emissive = ai;
        }

        void trimFloatAttrib(VertexAttribute vertexattribute)
        {
            float af[] = (float[])fattribs.get(vertexattribute.name);
            float af1[] = new float[vertexattribute.size * vertexCount];
            PApplet.arrayCopy(af, 0, af1, 0, vertexattribute.size * vertexCount);
            fattribs.put(vertexattribute.name, af1);
        }

        void trimIntAttrib(VertexAttribute vertexattribute)
        {
            int ai[] = (int[])iattribs.get(vertexattribute.name);
            int ai1[] = new int[vertexattribute.size * vertexCount];
            PApplet.arrayCopy(ai, 0, ai1, 0, vertexattribute.size * vertexCount);
            iattribs.put(vertexattribute.name, ai1);
        }

        void trimNormals()
        {
            float af[] = new float[vertexCount * 3];
            PApplet.arrayCopy(normals, 0, af, 0, vertexCount * 3);
            normals = af;
        }

        void trimShininess()
        {
            float af[] = new float[vertexCount];
            PApplet.arrayCopy(shininess, 0, af, 0, vertexCount);
            shininess = af;
        }

        void trimSpecular()
        {
            int ai[] = new int[vertexCount];
            PApplet.arrayCopy(specular, 0, ai, 0, vertexCount);
            specular = ai;
        }

        void trimStrokeColors()
        {
            int ai[] = new int[vertexCount];
            PApplet.arrayCopy(strokeColors, 0, ai, 0, vertexCount);
            strokeColors = ai;
        }

        void trimStrokeWeights()
        {
            float af[] = new float[vertexCount];
            PApplet.arrayCopy(strokeWeights, 0, af, 0, vertexCount);
            strokeWeights = af;
        }

        void trimTexCoords()
        {
            float af[] = new float[vertexCount * 2];
            PApplet.arrayCopy(texcoords, 0, af, 0, vertexCount * 2);
            texcoords = af;
        }

        void trimVertices()
        {
            float af[] = new float[vertexCount * 3];
            PApplet.arrayCopy(vertices, 0, af, 0, vertexCount * 3);
            vertices = af;
        }

        void vertexCheck()
        {
            if(vertexCount == vertices.length / 3)
            {
                int i = vertexCount << 1;
                expandVertices(i);
                expandColors(i);
                expandNormals(i);
                expandTexCoords(i);
                expandStrokeColors(i);
                expandStrokeWeights(i);
                expandAmbient(i);
                expandSpecular(i);
                expandEmissive(i);
                expandShininess(i);
                expandAttribs(i);
            }
        }

        int ambient[];
        int ambientColor;
        AttributeMap attribs;
        HashMap battribs;
        int codeCount;
        int codes[];
        int colors[];
        int edgeCount;
        int edges[][];
        int emissive[];
        int emissiveColor;
        HashMap fattribs;
        int fillColor;
        HashMap iattribs;
        float normalX;
        float normalY;
        float normalZ;
        float normals[];
        PGraphicsOpenGL pg;
        int renderMode;
        float shininess[];
        float shininessFactor;
        int specular[];
        int specularColor;
        int strokeColor;
        int strokeColors[];
        float strokeWeight;
        float strokeWeights[];
        float texcoords[];
        int vertexCount;
        float vertices[];

        InGeometry(PGraphicsOpenGL pgraphicsopengl, AttributeMap attributemap, int i)
        {
            pg = pgraphicsopengl;
            attribs = attributemap;
            renderMode = i;
            allocate();
        }
    }

    protected static class IndexCache
    {

        int addNew()
        {
            arrayCheck();
            init(size);
            size = size + 1;
            return size - 1;
        }

        int addNew(int i)
        {
            arrayCheck();
            indexCount[size] = indexCount[i];
            indexOffset[size] = indexOffset[i];
            vertexCount[size] = vertexCount[i];
            vertexOffset[size] = vertexOffset[i];
            size = size + 1;
            return size - 1;
        }

        void allocate()
        {
            size = 0;
            indexCount = new int[2];
            indexOffset = new int[2];
            vertexCount = new int[2];
            vertexOffset = new int[2];
            counter = null;
        }

        void arrayCheck()
        {
            if(size == indexCount.length)
            {
                int i = size << 1;
                expandIndexCount(i);
                expandIndexOffset(i);
                expandVertexCount(i);
                expandVertexOffset(i);
            }
        }

        void clear()
        {
            size = 0;
        }

        void expandIndexCount(int i)
        {
            int ai[] = new int[i];
            PApplet.arrayCopy(indexCount, 0, ai, 0, size);
            indexCount = ai;
        }

        void expandIndexOffset(int i)
        {
            int ai[] = new int[i];
            PApplet.arrayCopy(indexOffset, 0, ai, 0, size);
            indexOffset = ai;
        }

        void expandVertexCount(int i)
        {
            int ai[] = new int[i];
            PApplet.arrayCopy(vertexCount, 0, ai, 0, size);
            vertexCount = ai;
        }

        void expandVertexOffset(int i)
        {
            int ai[] = new int[i];
            PApplet.arrayCopy(vertexOffset, 0, ai, 0, size);
            vertexOffset = ai;
        }

        int getLast()
        {
            if(size == 0)
            {
                arrayCheck();
                init(0);
                size = 1;
            }
            return size - 1;
        }

        void incCounts(int i, int j, int k)
        {
            int ai[] = indexCount;
            ai[i] = ai[i] + j;
            ai = vertexCount;
            ai[i] = ai[i] + k;
            if(counter != null)
            {
                int ai1[] = counter;
                ai1[0] = ai1[0] + j;
                ai1 = counter;
                ai1[1] = ai1[1] + k;
            }
        }

        void init(int i)
        {
            if(i > 0)
            {
                indexOffset[i] = indexOffset[i - 1] + indexCount[i - 1];
                vertexOffset[i] = vertexOffset[i - 1] + vertexCount[i - 1];
            } else
            {
                indexOffset[i] = 0;
                vertexOffset[i] = 0;
            }
            indexCount[i] = 0;
            vertexCount[i] = 0;
        }

        void setCounter(int ai[])
        {
            counter = ai;
        }

        int counter[];
        int indexCount[];
        int indexOffset[];
        int size;
        int vertexCount[];
        int vertexOffset[];

        IndexCache()
        {
            allocate();
        }
    }

    protected static class TessGeometry
    {

        private void copyFewAttribs(InGeometry ingeometry, int i, int j, int k)
        {
label0:
            for(int l = 0; l < k; l++)
            {
                int i1 = i + l;
                int j1 = firstPolyVertex + l;
                j = i1 * 2;
                float f = ingeometry.texcoords[j];
                float f1 = ingeometry.texcoords[j + 1];
                polyColors[j1] = ingeometry.colors[i1];
                j = j1 * 2;
                polyTexCoords[j] = f;
                polyTexCoords[j + 1] = f1;
                polyAmbient[j1] = ingeometry.ambient[i1];
                polySpecular[j1] = ingeometry.specular[i1];
                polyEmissive[j1] = ingeometry.emissive[i1];
                polyShininess[j1] = ingeometry.shininess[i1];
                Iterator iterator = polyAttribs.keySet().iterator();
label1:
                do
                {
                    Object obj;
                    VertexAttribute vertexattribute;
                    int k1;
                    do
                    {
                        if(!iterator.hasNext())
                            continue label0;
                        obj = (String)iterator.next();
                        vertexattribute = (VertexAttribute)polyAttribs.get(obj);
                        if(!vertexattribute.isPosition() && !vertexattribute.isNormal())
                        {
                            k1 = vertexattribute.size * i1;
                            j = vertexattribute.size * j1;
                            if(vertexattribute.isFloat())
                            {
                                float af[] = (float[])ingeometry.fattribs.get(obj);
                                obj = (float[])fpolyAttribs.get(obj);
                                int l1 = 0;
                                while(l1 < vertexattribute.size) 
                                {
                                    obj[j] = af[k1];
                                    l1++;
                                    j++;
                                    k1++;
                                }
                            } else
                            {
                                if(!vertexattribute.isInt())
                                    continue;
                                int ai[] = (int[])ingeometry.iattribs.get(obj);
                                obj = (int[])ipolyAttribs.get(obj);
                                int i2 = 0;
                                while(i2 < vertexattribute.size) 
                                {
                                    obj[j] = ai[k1];
                                    i2++;
                                    j++;
                                    k1++;
                                }
                            }
                        }
                        continue label1;
                    } while(!vertexattribute.isBool());
                    byte abyte1[] = (byte[])ingeometry.battribs.get(obj);
                    byte abyte0[] = (byte[])bpolyAttribs.get(obj);
                    int j2 = 0;
                    while(j2 < vertexattribute.size) 
                    {
                        abyte0[j] = abyte1[k1];
                        j2++;
                        j++;
                        k1++;
                    }
                } while(true);
            }

        }

        private void copyFewCoords(InGeometry ingeometry, int i, int j, int k)
        {
label0:
            for(j = 0; j < k; j++)
            {
                int l = i + j;
                int i1 = firstPolyVertex + j;
                int j1 = l * 3;
                float af[] = ingeometry.vertices;
                int j2 = j1 + 1;
                float f = af[j1];
                float f1 = ingeometry.vertices[j2];
                float f2 = ingeometry.vertices[j2 + 1];
                j1 = l * 3;
                af = ingeometry.normals;
                j2 = j1 + 1;
                float f4 = af[j1];
                float f6 = ingeometry.normals[j2];
                float f8 = ingeometry.normals[j2 + 1];
                j2 = i1 * 4;
                af = polyVertices;
                j1 = j2 + 1;
                af[j2] = f;
                af = polyVertices;
                j2 = j1 + 1;
                af[j1] = f1;
                polyVertices[j2] = f2;
                polyVertices[j2 + 1] = 1.0F;
                j2 = i1 * 3;
                af = polyNormals;
                j1 = j2 + 1;
                af[j2] = f4;
                polyNormals[j1] = f6;
                polyNormals[j1 + 1] = f8;
                Iterator iterator = polyAttribs.keySet().iterator();
                do
                {
                    if(!iterator.hasNext())
                        continue label0;
                    String s = (String)iterator.next();
                    VertexAttribute vertexattribute = (VertexAttribute)polyAttribs.get(s);
                    if(!vertexattribute.isColor() && !vertexattribute.isOther())
                    {
                        float af2[] = (float[])ingeometry.fattribs.get(s);
                        int k1 = l * 3;
                        int k2 = k1 + 1;
                        float f7 = af2[k1];
                        float f3 = af2[k2];
                        float f5 = af2[k2 + 1];
                        float af1[] = (float[])fpolyAttribs.get(s);
                        if(vertexattribute.isPosition())
                        {
                            int l2 = i1 * 4;
                            int l1 = l2 + 1;
                            af1[l2] = f7;
                            l2 = l1 + 1;
                            af1[l1] = f3;
                            af1[l2] = f5;
                            af1[l2 + 1] = 1.0F;
                        } else
                        {
                            int i3 = i1 * 3;
                            int i2 = i3 + 1;
                            af1[i3] = f7;
                            af1[i2] = f3;
                            af1[i2 + 1] = f5;
                        }
                    }
                } while(true);
            }

        }

        private void copyManyAttribs(InGeometry ingeometry, int i, int j, int k)
        {
            Iterator iterator;
            PApplet.arrayCopy(ingeometry.colors, i, polyColors, firstPolyVertex, k);
            PApplet.arrayCopy(ingeometry.texcoords, i * 2, polyTexCoords, firstPolyVertex * 2, k * 2);
            PApplet.arrayCopy(ingeometry.ambient, i, polyAmbient, firstPolyVertex, k);
            PApplet.arrayCopy(ingeometry.specular, i, polySpecular, firstPolyVertex, k);
            PApplet.arrayCopy(ingeometry.emissive, i, polyEmissive, firstPolyVertex, k);
            PApplet.arrayCopy(ingeometry.shininess, i, polyShininess, firstPolyVertex, k);
            iterator = polyAttribs.keySet().iterator();
_L3:
            if(!iterator.hasNext()) goto _L2; else goto _L1
_L1:
            Object obj = (String)iterator.next();
            VertexAttribute vertexattribute = (VertexAttribute)polyAttribs.get(obj);
            if(!vertexattribute.isPosition() && !vertexattribute.isNormal())
            {
                Object obj1;
                if(vertexattribute.isFloat())
                {
                    obj1 = ingeometry.fattribs.get(obj);
                    obj = fpolyAttribs.get(obj);
                } else
                if(vertexattribute.isInt())
                {
                    obj1 = ingeometry.iattribs.get(obj);
                    obj = ipolyAttribs.get(obj);
                } else
                if(vertexattribute.isBool())
                {
                    obj1 = ingeometry.battribs.get(obj);
                    obj = bpolyAttribs.get(obj);
                } else
                {
                    obj = null;
                    obj1 = null;
                }
                PApplet.arrayCopy(obj1, vertexattribute.size * i, obj, vertexattribute.tessSize * firstPolyVertex, vertexattribute.size * k);
            }
            if(true) goto _L3; else goto _L2
_L2:
        }

        private void copyManyCoords(InGeometry ingeometry, int i, int j, int k)
        {
label0:
            for(j = 0; j < k; j++)
            {
                int l = i + j;
                int i1 = firstPolyVertex + j;
                PApplet.arrayCopy(ingeometry.vertices, l * 3, polyVertices, i1 * 4, 3);
                polyVertices[i1 * 4 + 3] = 1.0F;
                Iterator iterator = polyAttribs.keySet().iterator();
                do
                {
                    if(!iterator.hasNext())
                        continue label0;
                    String s = (String)iterator.next();
                    if(((VertexAttribute)polyAttribs.get(s)).isPosition())
                    {
                        float af1[] = (float[])ingeometry.fattribs.get(s);
                        float af[] = (float[])fpolyAttribs.get(s);
                        PApplet.arrayCopy(af1, l * 3, af, i1 * 4, 3);
                        af[i1 * 4 + 3] = 1.0F;
                    }
                } while(true);
            }

            PApplet.arrayCopy(ingeometry.normals, i * 3, polyNormals, firstPolyVertex * 3, k * 3);
            Iterator iterator1 = polyAttribs.keySet().iterator();
            do
            {
                if(!iterator1.hasNext())
                    break;
                String s1 = (String)iterator1.next();
                if(((VertexAttribute)polyAttribs.get(s1)).isNormal())
                    PApplet.arrayCopy((float[])ingeometry.fattribs.get(s1), i * 3, (float[])fpolyAttribs.get(s1), firstPolyVertex * 3, k * 3);
            } while(true);
        }

        private void modelviewCoords(InGeometry ingeometry, int i, int j, int k, boolean flag)
        {
            PMatrix3D pmatrix3d = pg.modelview;
            PMatrix3D pmatrix3d1 = pg.modelviewInv;
label0:
            for(j = 0; j < k; j++)
            {
                int l = i + j;
                int i1 = firstPolyVertex + j;
                int j1 = l * 3;
                float af[] = ingeometry.vertices;
                int l2 = j1 + 1;
                float f = af[j1];
                float f1 = ingeometry.vertices[l2];
                float f2 = ingeometry.vertices[l2 + 1];
                j1 = l * 3;
                af = ingeometry.normals;
                l2 = j1 + 1;
                float f3 = af[j1];
                float f5 = ingeometry.normals[l2];
                float f6 = ingeometry.normals[l2 + 1];
                l2 = i1 * 4;
                Iterator iterator;
                if(flag)
                {
                    af = polyVertices;
                    j1 = l2 + 1;
                    af[l2] = PApplet.ceil(pmatrix3d.m00 * f + pmatrix3d.m01 * f1 + pmatrix3d.m02 * f2 + pmatrix3d.m03);
                    af = polyVertices;
                    l2 = j1 + 1;
                    af[j1] = PApplet.ceil(pmatrix3d.m10 * f + pmatrix3d.m11 * f1 + pmatrix3d.m12 * f2 + pmatrix3d.m13);
                } else
                {
                    float af1[] = polyVertices;
                    int i2 = l2 + 1;
                    af1[l2] = pmatrix3d.m00 * f + pmatrix3d.m01 * f1 + pmatrix3d.m02 * f2 + pmatrix3d.m03;
                    af1 = polyVertices;
                    l2 = i2 + 1;
                    af1[i2] = pmatrix3d.m10 * f + pmatrix3d.m11 * f1 + pmatrix3d.m12 * f2 + pmatrix3d.m13;
                }
                polyVertices[l2] = pmatrix3d.m20 * f + pmatrix3d.m21 * f1 + pmatrix3d.m22 * f2 + pmatrix3d.m23;
                polyVertices[l2 + 1] = f * pmatrix3d.m30 + f1 * pmatrix3d.m31 + pmatrix3d.m32 * f2 + pmatrix3d.m33;
                j1 = i1 * 3;
                af = polyNormals;
                l2 = j1 + 1;
                af[j1] = pmatrix3d1.m00 * f3 + pmatrix3d1.m10 * f5 + pmatrix3d1.m20 * f6;
                polyNormals[l2] = pmatrix3d1.m01 * f3 + pmatrix3d1.m11 * f5 + pmatrix3d1.m21 * f6;
                polyNormals[l2 + 1] = pmatrix3d1.m02 * f3 + pmatrix3d1.m12 * f5 + pmatrix3d1.m22 * f6;
                iterator = polyAttribs.keySet().iterator();
                do
                {
                    if(!iterator.hasNext())
                        continue label0;
                    String s = (String)iterator.next();
                    VertexAttribute vertexattribute = (VertexAttribute)polyAttribs.get(s);
                    if(!vertexattribute.isColor() && !vertexattribute.isOther())
                    {
                        float af3[] = (float[])ingeometry.fattribs.get(s);
                        int k1 = l * 3;
                        l2 = k1 + 1;
                        f1 = af3[k1];
                        f2 = af3[l2];
                        f = af3[l2 + 1];
                        float af2[] = (float[])fpolyAttribs.get(s);
                        if(vertexattribute.isPosition())
                        {
                            l2 = i1 * 4;
                            if(flag)
                            {
                                int l1 = l2 + 1;
                                af2[l2] = PApplet.ceil(pmatrix3d.m00 * f1 + pmatrix3d.m01 * f2 + pmatrix3d.m02 * f + pmatrix3d.m03);
                                l2 = l1 + 1;
                                af2[l1] = PApplet.ceil(pmatrix3d.m10 * f1 + pmatrix3d.m11 * f2 + pmatrix3d.m12 * f + pmatrix3d.m13);
                            } else
                            {
                                int j2 = l2 + 1;
                                af2[l2] = pmatrix3d.m00 * f1 + pmatrix3d.m01 * f2 + pmatrix3d.m02 * f + pmatrix3d.m03;
                                l2 = j2 + 1;
                                af2[j2] = pmatrix3d.m10 * f1 + pmatrix3d.m11 * f2 + pmatrix3d.m12 * f + pmatrix3d.m13;
                            }
                            af2[l2] = pmatrix3d.m20 * f1 + pmatrix3d.m21 * f2 + pmatrix3d.m22 * f + pmatrix3d.m23;
                            af2[l2 + 1] = pmatrix3d.m30 * f1 + pmatrix3d.m31 * f2 + f * pmatrix3d.m32 + pmatrix3d.m33;
                        } else
                        {
                            int k2 = i1 * 3;
                            int i3 = k2 + 1;
                            af2[k2] = pmatrix3d1.m00 * f1 + pmatrix3d1.m10 * f2 + pmatrix3d1.m20 * f;
                            af2[i3] = pmatrix3d1.m01 * f1 + pmatrix3d1.m11 * f2 + pmatrix3d1.m21 * f;
                            float f4 = pmatrix3d1.m02;
                            float f7 = pmatrix3d1.m12;
                            af2[i3 + 1] = f * pmatrix3d1.m22 + (f1 * f4 + f2 * f7);
                        }
                    }
                } while(true);
            }

        }

        void addPolyVertex(float f, float f1, float f2, int i, float f3, float f4, float f5, 
                float f6, float f7, int j, int k, int l, float f8, boolean flag)
        {
            polyVertexCheck();
            setPolyVertex(polyVertexCount - 1, f, f1, f2, i, f3, f4, f5, f6, f7, j, k, l, f8, flag);
        }

        void addPolyVertex(InGeometry ingeometry, int i, boolean flag)
        {
            addPolyVertices(ingeometry, i, i, flag);
        }

        void addPolyVertex(double ad[], boolean flag)
        {
            int i = (int)ad[3];
            int k = (int)ad[4];
            int l = (int)ad[5];
            int i1 = (int)ad[6];
            int k1 = (int)ad[12];
            int l1 = (int)ad[13];
            int i2 = (int)ad[14];
            int k2 = (int)ad[15];
            int l2 = (int)ad[16];
            int l4 = (int)ad[17];
            int i5 = (int)ad[18];
            int j5 = (int)ad[19];
            int j7 = (int)ad[20];
            int l7 = (int)ad[21];
            int j8 = (int)ad[22];
            int l8 = (int)ad[23];
            addPolyVertex((float)ad[0], (float)ad[1], (float)ad[2], i << 24 | k << 16 | l << 8 | i1, (float)ad[7], (float)ad[8], (float)ad[9], (float)ad[10], (float)ad[11], k1 << 24 | l1 << 16 | i2 << 8 | k2, l2 << 24 | l4 << 16 | i5 << 8 | j5, j7 << 24 | l7 << 16 | j8 << 8 | l8, (float)ad[24], flag);
            if(25 < ad.length)
            {
                PMatrix3D pmatrix3d = pg.modelview;
                PMatrix3D pmatrix3d1 = pg.modelviewInv;
                int k7 = polyVertexCount;
                int k8 = 25;
                int j = 0;
                while(j < polyAttribs.size()) 
                {
                    Object obj = polyAttribs.get(j);
                    Object obj1 = ((VertexAttribute) (obj)).name;
                    int i8 = ((VertexAttribute) (obj)).tessSize * (k7 - 1);
                    if(((VertexAttribute) (obj)).isColor())
                    {
                        int i3 = (int)ad[k8 + 0];
                        int j2 = (int)ad[k8 + 1];
                        int k5 = (int)ad[k8 + 2];
                        int j1 = (int)ad[k8 + 3];
                        ((int[])ipolyAttribs.get(obj1))[i8] = i3 << 24 | j2 << 16 | k5 << 8 | j1;
                        k8 += 4;
                    } else
                    if(((VertexAttribute) (obj)).isPosition())
                    {
                        obj = (float[])fpolyAttribs.get(obj1);
                        int l5 = k8 + 1;
                        float f = (float)ad[k8];
                        int j3 = l5 + 1;
                        float f2 = (float)ad[l5];
                        float f4 = (float)ad[j3];
                        if(renderMode == 0 && pg.flushMode == 1)
                        {
                            if(flag)
                            {
                                int i6 = i8 + 1;
                                obj[i8] = PApplet.ceil(pmatrix3d.m00 * f + pmatrix3d.m01 * f2 + pmatrix3d.m02 * f4 + pmatrix3d.m03);
                                k8 = i6 + 1;
                                obj[i6] = PApplet.ceil(pmatrix3d.m10 * f + pmatrix3d.m11 * f2 + pmatrix3d.m12 * f4 + pmatrix3d.m13);
                            } else
                            {
                                int j6 = i8 + 1;
                                obj[i8] = pmatrix3d.m00 * f + pmatrix3d.m01 * f2 + pmatrix3d.m02 * f4 + pmatrix3d.m03;
                                k8 = j6 + 1;
                                obj[j6] = pmatrix3d.m10 * f + pmatrix3d.m11 * f2 + pmatrix3d.m12 * f4 + pmatrix3d.m13;
                            }
                            obj[k8] = pmatrix3d.m20 * f + pmatrix3d.m21 * f2 + pmatrix3d.m22 * f4 + pmatrix3d.m23;
                            obj[k8 + 1] = pmatrix3d.m30 * f + pmatrix3d.m31 * f2 + pmatrix3d.m32 * f4 + pmatrix3d.m33;
                        } else
                        {
                            k8 = i8 + 1;
                            obj[i8] = f;
                            i8 = k8 + 1;
                            obj[k8] = f2;
                            obj[i8] = f4;
                            obj[i8 + 1] = 1.0F;
                        }
                        k8 = j3 + 1;
                    } else
                    if(((VertexAttribute) (obj)).isNormal())
                    {
                        obj = (float[])fpolyAttribs.get(obj1);
                        float f5 = (float)ad[k8 + 0];
                        float f3 = (float)ad[k8 + 1];
                        float f1 = (float)ad[k8 + 2];
                        if(renderMode == 0 && pg.flushMode == 1)
                        {
                            int k3 = i8 + 1;
                            obj[i8] = pmatrix3d1.m00 * f5 + pmatrix3d1.m10 * f3 + pmatrix3d1.m20 * f1;
                            obj[k3] = pmatrix3d1.m01 * f5 + pmatrix3d1.m11 * f3 + pmatrix3d1.m21 * f1;
                            obj[k3 + 1] = f5 * pmatrix3d1.m02 + f3 * pmatrix3d1.m12 + pmatrix3d1.m22 * f1;
                        } else
                        {
                            int l3 = i8 + 1;
                            obj[i8] = f5;
                            obj[l3] = f3;
                            obj[l3 + 1] = f1;
                        }
                        k8 += 3;
                    } else
                    {
                        if(((VertexAttribute) (obj)).isFloat())
                        {
                            obj1 = (float[])fpolyAttribs.get(obj1);
                            int k6 = 0;
                            int i4 = i8;
                            i8 = k8;
                            do
                            {
                                k8 = i8;
                                if(k6 >= ((VertexAttribute) (obj)).size)
                                    break;
                                obj1[i4] = (float)ad[i8];
                                k6++;
                                i8++;
                                i4++;
                            } while(true);
                        } else
                        if(((VertexAttribute) (obj)).isInt())
                        {
                            obj1 = (int[])ipolyAttribs.get(obj1);
                            int l6 = 0;
                            int j4 = i8;
                            i8 = k8;
                            do
                            {
                                k8 = i8;
                                if(l6 >= ((VertexAttribute) (obj)).size)
                                    break;
                                obj1[j4] = (int)ad[i8];
                                l6++;
                                i8++;
                                j4++;
                            } while(true);
                        } else
                        if(((VertexAttribute) (obj)).isBool())
                        {
                            byte abyte0[] = (byte[])bpolyAttribs.get(obj1);
                            int i7 = 0;
                            int k4 = i8;
                            i8 = k8;
                            do
                            {
                                k8 = i8;
                                if(i7 >= ((VertexAttribute) (obj)).size)
                                    break;
                                abyte0[k4] = (byte)(int)ad[i8];
                                i7++;
                                i8++;
                                k4++;
                            } while(true);
                        }
                        k8 += ((VertexAttribute) (obj)).size;
                    }
                    j++;
                }
            }
        }

        void addPolyVertices(InGeometry ingeometry, int i, int j, boolean flag)
        {
            j = (j - i) + 1;
            polyVertexCheck(j);
            if(renderMode == 0 && pg.flushMode == 1)
                modelviewCoords(ingeometry, i, 0, j, flag);
            else
            if(j <= PGL.MIN_ARRAYCOPY_SIZE)
                copyFewCoords(ingeometry, i, 0, j);
            else
                copyManyCoords(ingeometry, i, 0, j);
            if(j <= PGL.MIN_ARRAYCOPY_SIZE)
                copyFewAttribs(ingeometry, i, 0, j);
            else
                copyManyAttribs(ingeometry, i, 0, j);
        }

        void addPolyVertices(InGeometry ingeometry, boolean flag)
        {
            addPolyVertices(ingeometry, 0, ingeometry.vertexCount - 1, flag);
        }

        void allocate()
        {
            polyVertices = new float[PGL.DEFAULT_TESS_VERTICES * 4];
            polyColors = new int[PGL.DEFAULT_TESS_VERTICES];
            polyNormals = new float[PGL.DEFAULT_TESS_VERTICES * 3];
            polyTexCoords = new float[PGL.DEFAULT_TESS_VERTICES * 2];
            polyAmbient = new int[PGL.DEFAULT_TESS_VERTICES];
            polySpecular = new int[PGL.DEFAULT_TESS_VERTICES];
            polyEmissive = new int[PGL.DEFAULT_TESS_VERTICES];
            polyShininess = new float[PGL.DEFAULT_TESS_VERTICES];
            polyIndices = new short[PGL.DEFAULT_TESS_VERTICES];
            lineVertices = new float[PGL.DEFAULT_TESS_VERTICES * 4];
            lineColors = new int[PGL.DEFAULT_TESS_VERTICES];
            lineDirections = new float[PGL.DEFAULT_TESS_VERTICES * 4];
            lineIndices = new short[PGL.DEFAULT_TESS_VERTICES];
            pointVertices = new float[PGL.DEFAULT_TESS_VERTICES * 4];
            pointColors = new int[PGL.DEFAULT_TESS_VERTICES];
            pointOffsets = new float[PGL.DEFAULT_TESS_VERTICES * 2];
            pointIndices = new short[PGL.DEFAULT_TESS_VERTICES];
            polyVerticesBuffer = PGL.allocateFloatBuffer(polyVertices);
            polyColorsBuffer = PGL.allocateIntBuffer(polyColors);
            polyNormalsBuffer = PGL.allocateFloatBuffer(polyNormals);
            polyTexCoordsBuffer = PGL.allocateFloatBuffer(polyTexCoords);
            polyAmbientBuffer = PGL.allocateIntBuffer(polyAmbient);
            polySpecularBuffer = PGL.allocateIntBuffer(polySpecular);
            polyEmissiveBuffer = PGL.allocateIntBuffer(polyEmissive);
            polyShininessBuffer = PGL.allocateFloatBuffer(polyShininess);
            polyIndicesBuffer = PGL.allocateShortBuffer(polyIndices);
            lineVerticesBuffer = PGL.allocateFloatBuffer(lineVertices);
            lineColorsBuffer = PGL.allocateIntBuffer(lineColors);
            lineDirectionsBuffer = PGL.allocateFloatBuffer(lineDirections);
            lineIndicesBuffer = PGL.allocateShortBuffer(lineIndices);
            pointVerticesBuffer = PGL.allocateFloatBuffer(pointVertices);
            pointColorsBuffer = PGL.allocateIntBuffer(pointColors);
            pointOffsetsBuffer = PGL.allocateFloatBuffer(pointOffsets);
            pointIndicesBuffer = PGL.allocateShortBuffer(pointIndices);
            clear();
        }

        void applyMatrixOnLineGeometry(PMatrix2D pmatrix2d, int i, int j)
        {
            if(i < j)
            {
                float f = PGraphicsOpenGL.matrixScale(pmatrix2d);
                for(; i <= j; i++)
                {
                    int k = i * 4;
                    float f1 = lineVertices[k];
                    float f2 = lineVertices[k + 1];
                    k = i * 4;
                    float f3 = lineDirections[k];
                    float f4 = lineDirections[k + 1];
                    k = i * 4;
                    lineVertices[k] = pmatrix2d.m00 * f1 + pmatrix2d.m01 * f2 + pmatrix2d.m02;
                    lineVertices[k + 1] = f1 * pmatrix2d.m10 + f2 * pmatrix2d.m11 + pmatrix2d.m12;
                    int l = i * 4;
                    float af[] = lineDirections;
                    k = l + 1;
                    af[l] = pmatrix2d.m00 * f3 + pmatrix2d.m01 * f4;
                    lineDirections[k] = pmatrix2d.m10 * f3 + pmatrix2d.m11 * f4;
                    af = lineDirections;
                    k += 2;
                    af[k] = af[k] * f;
                }

            }
        }

        void applyMatrixOnLineGeometry(PMatrix3D pmatrix3d, int i, int j)
        {
            if(i < j)
            {
                float f = PGraphicsOpenGL.matrixScale(pmatrix3d);
                for(; i <= j; i++)
                {
                    int k = i * 4;
                    float af[] = lineVertices;
                    int l = k + 1;
                    float f1 = af[k];
                    af = lineVertices;
                    k = l + 1;
                    float f2 = af[l];
                    float f3 = lineVertices[k];
                    float f4 = lineVertices[k + 1];
                    k = i * 4;
                    af = lineDirections;
                    l = k + 1;
                    float f5 = af[k];
                    float f6 = lineDirections[l];
                    float f7 = lineDirections[l + 1];
                    k = i * 4;
                    af = lineVertices;
                    l = k + 1;
                    af[k] = pmatrix3d.m00 * f1 + pmatrix3d.m01 * f2 + pmatrix3d.m02 * f3 + pmatrix3d.m03 * f4;
                    af = lineVertices;
                    k = l + 1;
                    af[l] = pmatrix3d.m10 * f1 + pmatrix3d.m11 * f2 + pmatrix3d.m12 * f3 + pmatrix3d.m13 * f4;
                    lineVertices[k] = pmatrix3d.m20 * f1 + pmatrix3d.m21 * f2 + pmatrix3d.m22 * f3 + pmatrix3d.m23 * f4;
                    lineVertices[k + 1] = f1 * pmatrix3d.m30 + f2 * pmatrix3d.m31 + pmatrix3d.m32 * f3 + pmatrix3d.m33 * f4;
                    l = i * 4;
                    af = lineDirections;
                    k = l + 1;
                    af[l] = pmatrix3d.m00 * f5 + pmatrix3d.m01 * f6 + pmatrix3d.m02 * f7;
                    af = lineDirections;
                    l = k + 1;
                    af[k] = pmatrix3d.m10 * f5 + pmatrix3d.m11 * f6 + pmatrix3d.m12 * f7;
                    af = lineDirections;
                    k = l + 1;
                    af[l] = pmatrix3d.m20 * f5 + pmatrix3d.m21 * f6 + pmatrix3d.m22 * f7;
                    af = lineDirections;
                    af[k] = af[k] * f;
                }

            }
        }

        void applyMatrixOnLineGeometry(PMatrix pmatrix, int i, int j)
        {
            if(!(pmatrix instanceof PMatrix2D)) goto _L2; else goto _L1
_L1:
            applyMatrixOnLineGeometry((PMatrix2D)pmatrix, i, j);
_L4:
            return;
_L2:
            if(pmatrix instanceof PMatrix3D)
                applyMatrixOnLineGeometry((PMatrix3D)pmatrix, i, j);
            if(true) goto _L4; else goto _L3
_L3:
        }

        void applyMatrixOnPointGeometry(PMatrix2D pmatrix2d, int i, int j)
        {
            if(i < j)
            {
                float f = PGraphicsOpenGL.matrixScale(pmatrix2d);
                for(; i <= j; i++)
                {
                    int k = i * 4;
                    float f1 = pointVertices[k];
                    float f2 = pointVertices[k + 1];
                    k = i * 4;
                    pointVertices[k] = pmatrix2d.m00 * f1 + pmatrix2d.m01 * f2 + pmatrix2d.m02;
                    pointVertices[k + 1] = f1 * pmatrix2d.m10 + f2 * pmatrix2d.m11 + pmatrix2d.m12;
                    int l = i * 2;
                    float af[] = pointOffsets;
                    k = l + 1;
                    af[l] = af[l] * f;
                    af = pointOffsets;
                    af[k] = af[k] * f;
                }

            }
        }

        void applyMatrixOnPointGeometry(PMatrix3D pmatrix3d, int i, int j)
        {
            if(i < j)
            {
                float f = PGraphicsOpenGL.matrixScale(pmatrix3d);
                for(; i <= j; i++)
                {
                    int k = i * 4;
                    float af[] = pointVertices;
                    int l = k + 1;
                    float f1 = af[k];
                    af = pointVertices;
                    k = l + 1;
                    float f2 = af[l];
                    float f3 = pointVertices[k];
                    float f4 = pointVertices[k + 1];
                    k = i * 4;
                    af = pointVertices;
                    l = k + 1;
                    af[k] = pmatrix3d.m00 * f1 + pmatrix3d.m01 * f2 + pmatrix3d.m02 * f3 + pmatrix3d.m03 * f4;
                    af = pointVertices;
                    k = l + 1;
                    af[l] = pmatrix3d.m10 * f1 + pmatrix3d.m11 * f2 + pmatrix3d.m12 * f3 + pmatrix3d.m13 * f4;
                    pointVertices[k] = pmatrix3d.m20 * f1 + pmatrix3d.m21 * f2 + pmatrix3d.m22 * f3 + pmatrix3d.m23 * f4;
                    pointVertices[k + 1] = f1 * pmatrix3d.m30 + f2 * pmatrix3d.m31 + pmatrix3d.m32 * f3 + pmatrix3d.m33 * f4;
                    k = i * 2;
                    af = pointOffsets;
                    l = k + 1;
                    af[k] = af[k] * f;
                    af = pointOffsets;
                    af[l] = af[l] * f;
                }

            }
        }

        void applyMatrixOnPointGeometry(PMatrix pmatrix, int i, int j)
        {
            if(!(pmatrix instanceof PMatrix2D)) goto _L2; else goto _L1
_L1:
            applyMatrixOnPointGeometry((PMatrix2D)pmatrix, i, j);
_L4:
            return;
_L2:
            if(pmatrix instanceof PMatrix3D)
                applyMatrixOnPointGeometry((PMatrix3D)pmatrix, i, j);
            if(true) goto _L4; else goto _L3
_L3:
        }

        void applyMatrixOnPolyGeometry(PMatrix2D pmatrix2d, int i, int j)
        {
            if(i < j)
label0:
                for(; i <= j; i++)
                {
                    int k = i * 4;
                    float f = polyVertices[k];
                    float f3 = polyVertices[k + 1];
                    k = i * 3;
                    float f6 = polyNormals[k];
                    float f7 = polyNormals[k + 1];
                    k = i * 4;
                    polyVertices[k] = pmatrix2d.m00 * f + pmatrix2d.m01 * f3 + pmatrix2d.m02;
                    polyVertices[k + 1] = f * pmatrix2d.m10 + f3 * pmatrix2d.m11 + pmatrix2d.m12;
                    k = i * 3;
                    polyNormals[k] = pmatrix2d.m00 * f6 + pmatrix2d.m01 * f7;
                    polyNormals[k + 1] = pmatrix2d.m10 * f6 + pmatrix2d.m11 * f7;
                    Iterator iterator = polyAttribs.keySet().iterator();
                    do
                    {
                        if(!iterator.hasNext())
                            continue label0;
                        String s = (String)iterator.next();
                        VertexAttribute vertexattribute = (VertexAttribute)polyAttribs.get(s);
                        if(!vertexattribute.isColor() && !vertexattribute.isOther())
                        {
                            float af[] = (float[])fpolyAttribs.get(s);
                            if(vertexattribute.isPosition())
                            {
                                int l = i * 4;
                                float f4 = af[l];
                                float f1 = af[l + 1];
                                l = i * 4;
                                af[l] = pmatrix2d.m00 * f4 + pmatrix2d.m01 * f1 + pmatrix2d.m02;
                                af[l + 1] = f4 * pmatrix2d.m10 + f1 * pmatrix2d.m11 + pmatrix2d.m12;
                            } else
                            {
                                int i1 = i * 3;
                                float f2 = af[i1];
                                float f5 = af[i1 + 1];
                                i1 = i * 3;
                                af[i1] = pmatrix2d.m00 * f2 + pmatrix2d.m01 * f5;
                                af[i1 + 1] = f2 * pmatrix2d.m10 + f5 * pmatrix2d.m11;
                            }
                        }
                    } while(true);
                }

        }

        void applyMatrixOnPolyGeometry(PMatrix3D pmatrix3d, int i, int j)
        {
            if(i < j)
label0:
                for(; i <= j; i++)
                {
                    int k = i * 4;
                    float af[] = polyVertices;
                    int j1 = k + 1;
                    float f = af[k];
                    af = polyVertices;
                    k = j1 + 1;
                    float f2 = af[j1];
                    float f3 = polyVertices[k];
                    float f6 = polyVertices[k + 1];
                    j1 = i * 3;
                    af = polyNormals;
                    k = j1 + 1;
                    float f9 = af[j1];
                    float f12 = polyNormals[k];
                    float f13 = polyNormals[k + 1];
                    k = i * 4;
                    af = polyVertices;
                    j1 = k + 1;
                    af[k] = pmatrix3d.m00 * f + pmatrix3d.m01 * f2 + pmatrix3d.m02 * f3 + pmatrix3d.m03 * f6;
                    af = polyVertices;
                    k = j1 + 1;
                    af[j1] = pmatrix3d.m10 * f + pmatrix3d.m11 * f2 + pmatrix3d.m12 * f3 + pmatrix3d.m13 * f6;
                    polyVertices[k] = pmatrix3d.m20 * f + pmatrix3d.m21 * f2 + pmatrix3d.m22 * f3 + pmatrix3d.m23 * f6;
                    polyVertices[k + 1] = f * pmatrix3d.m30 + f2 * pmatrix3d.m31 + pmatrix3d.m32 * f3 + pmatrix3d.m33 * f6;
                    j1 = i * 3;
                    af = polyNormals;
                    k = j1 + 1;
                    af[j1] = pmatrix3d.m00 * f9 + pmatrix3d.m01 * f12 + pmatrix3d.m02 * f13;
                    polyNormals[k] = pmatrix3d.m10 * f9 + pmatrix3d.m11 * f12 + pmatrix3d.m12 * f13;
                    polyNormals[k + 1] = pmatrix3d.m20 * f9 + pmatrix3d.m21 * f12 + pmatrix3d.m22 * f13;
                    Iterator iterator = polyAttribs.keySet().iterator();
                    do
                    {
                        if(!iterator.hasNext())
                            continue label0;
                        String s = (String)iterator.next();
                        VertexAttribute vertexattribute = (VertexAttribute)polyAttribs.get(s);
                        if(!vertexattribute.isColor() && !vertexattribute.isOther())
                        {
                            float af1[] = (float[])fpolyAttribs.get(s);
                            if(vertexattribute.isPosition())
                            {
                                int l = i * 4;
                                int k1 = l + 1;
                                float f10 = af1[l];
                                l = k1 + 1;
                                float f4 = af1[k1];
                                float f7 = af1[l];
                                float f1 = af1[l + 1];
                                l = i * 4;
                                k1 = l + 1;
                                af1[l] = pmatrix3d.m00 * f10 + pmatrix3d.m01 * f4 + pmatrix3d.m02 * f7 + pmatrix3d.m03 * f1;
                                l = k1 + 1;
                                af1[k1] = pmatrix3d.m10 * f10 + pmatrix3d.m11 * f4 + pmatrix3d.m12 * f7 + pmatrix3d.m13 * f1;
                                af1[l] = pmatrix3d.m20 * f10 + pmatrix3d.m21 * f4 + pmatrix3d.m22 * f7 + pmatrix3d.m23 * f1;
                                af1[l + 1] = f10 * pmatrix3d.m30 + f4 * pmatrix3d.m31 + pmatrix3d.m32 * f7 + pmatrix3d.m33 * f1;
                            } else
                            {
                                int i1 = i * 3;
                                int l1 = i1 + 1;
                                float f11 = af1[i1];
                                float f5 = af1[l1];
                                float f8 = af1[l1 + 1];
                                i1 = i * 3;
                                l1 = i1 + 1;
                                af1[i1] = pmatrix3d.m00 * f11 + pmatrix3d.m01 * f5 + pmatrix3d.m02 * f8;
                                af1[l1] = pmatrix3d.m10 * f11 + pmatrix3d.m11 * f5 + pmatrix3d.m12 * f8;
                                af1[l1 + 1] = f11 * pmatrix3d.m20 + f5 * pmatrix3d.m21 + pmatrix3d.m22 * f8;
                            }
                        }
                    } while(true);
                }

        }

        void applyMatrixOnPolyGeometry(PMatrix pmatrix, int i, int j)
        {
            if(!(pmatrix instanceof PMatrix2D)) goto _L2; else goto _L1
_L1:
            applyMatrixOnPolyGeometry((PMatrix2D)pmatrix, i, j);
_L4:
            return;
_L2:
            if(pmatrix instanceof PMatrix3D)
                applyMatrixOnPolyGeometry((PMatrix3D)pmatrix, i, j);
            if(true) goto _L4; else goto _L3
_L3:
        }

        void calcPolyNormal(int i, int j, int k)
        {
            int l = i * 4;
            float af[] = polyVertices;
            int i1 = l + 1;
            float f = af[l];
            float f1 = polyVertices[i1];
            float f2 = polyVertices[i1 + 1];
            i1 = j * 4;
            af = polyVertices;
            l = i1 + 1;
            float f3 = af[i1];
            float f4 = polyVertices[l];
            float f5 = polyVertices[l + 1];
            l = k * 4;
            af = polyVertices;
            i1 = l + 1;
            float f6 = af[l];
            float f7 = polyVertices[i1];
            float f8 = polyVertices[i1 + 1];
            f6 -= f3;
            f7 -= f4;
            f8 -= f5;
            f3 = f - f3;
            f1 -= f4;
            f2 -= f5;
            f5 = f7 * f2 - f1 * f8;
            f2 = f8 * f3 - f2 * f6;
            f6 = f1 * f6 - f3 * f7;
            f7 = PApplet.sqrt(f5 * f5 + f2 * f2 + f6 * f6);
            f5 /= f7;
            f2 /= f7;
            f6 /= f7;
            i *= 3;
            af = polyNormals;
            i1 = i + 1;
            af[i] = f5;
            polyNormals[i1] = f2;
            polyNormals[i1 + 1] = f6;
            j *= 3;
            af = polyNormals;
            i = j + 1;
            af[j] = f5;
            polyNormals[i] = f2;
            polyNormals[i + 1] = f6;
            i = k * 3;
            af = polyNormals;
            j = i + 1;
            af[i] = f5;
            polyNormals[j] = f2;
            polyNormals[j + 1] = f6;
        }

        void clear()
        {
            polyVertexCount = 0;
            lastPolyVertex = 0;
            firstPolyVertex = 0;
            polyIndexCount = 0;
            lastPolyIndex = 0;
            firstPolyIndex = 0;
            lineVertexCount = 0;
            lastLineVertex = 0;
            firstLineVertex = 0;
            lineIndexCount = 0;
            lastLineIndex = 0;
            firstLineIndex = 0;
            pointVertexCount = 0;
            lastPointVertex = 0;
            firstPointVertex = 0;
            pointIndexCount = 0;
            lastPointIndex = 0;
            firstPointIndex = 0;
            polyIndexCache.clear();
            lineIndexCache.clear();
            pointIndexCache.clear();
        }

        void expandAttributes(int i)
        {
            Iterator iterator = polyAttribs.keySet().iterator();
            do
            {
                if(!iterator.hasNext())
                    break;
                Object obj = (String)iterator.next();
                obj = (VertexAttribute)polyAttribs.get(obj);
                if(((VertexAttribute) (obj)).type == PGL.FLOAT)
                    expandFloatAttribute(((VertexAttribute) (obj)), i);
                else
                if(((VertexAttribute) (obj)).type == PGL.INT)
                    expandIntAttribute(((VertexAttribute) (obj)), i);
                else
                if(((VertexAttribute) (obj)).type == PGL.BOOL)
                    expandBoolAttribute(((VertexAttribute) (obj)), i);
            } while(true);
        }

        void expandBoolAttribute(VertexAttribute vertexattribute, int i)
        {
            byte abyte0[] = (byte[])bpolyAttribs.get(vertexattribute.name);
            byte abyte1[] = new byte[vertexattribute.tessSize * i];
            PApplet.arrayCopy(abyte0, 0, abyte1, 0, vertexattribute.tessSize * polyVertexCount);
            bpolyAttribs.put(vertexattribute.name, abyte1);
            polyAttribBuffers.put(vertexattribute.name, PGL.allocateByteBuffer(abyte1));
        }

        void expandFloatAttribute(VertexAttribute vertexattribute, int i)
        {
            float af[] = (float[])fpolyAttribs.get(vertexattribute.name);
            float af1[] = new float[vertexattribute.tessSize * i];
            PApplet.arrayCopy(af, 0, af1, 0, vertexattribute.tessSize * polyVertexCount);
            fpolyAttribs.put(vertexattribute.name, af1);
            polyAttribBuffers.put(vertexattribute.name, PGL.allocateFloatBuffer(af1));
        }

        void expandIntAttribute(VertexAttribute vertexattribute, int i)
        {
            int ai[] = (int[])ipolyAttribs.get(vertexattribute.name);
            int ai1[] = new int[vertexattribute.tessSize * i];
            PApplet.arrayCopy(ai, 0, ai1, 0, vertexattribute.tessSize * polyVertexCount);
            ipolyAttribs.put(vertexattribute.name, ai1);
            polyAttribBuffers.put(vertexattribute.name, PGL.allocateIntBuffer(ai1));
        }

        void expandLineColors(int i)
        {
            int ai[] = new int[i];
            PApplet.arrayCopy(lineColors, 0, ai, 0, lineVertexCount);
            lineColors = ai;
            lineColorsBuffer = PGL.allocateIntBuffer(lineColors);
        }

        void expandLineDirections(int i)
        {
            float af[] = new float[i * 4];
            PApplet.arrayCopy(lineDirections, 0, af, 0, lineVertexCount * 4);
            lineDirections = af;
            lineDirectionsBuffer = PGL.allocateFloatBuffer(lineDirections);
        }

        void expandLineIndices(int i)
        {
            short aword0[] = new short[i];
            PApplet.arrayCopy(lineIndices, 0, aword0, 0, lineIndexCount);
            lineIndices = aword0;
            lineIndicesBuffer = PGL.allocateShortBuffer(lineIndices);
        }

        void expandLineVertices(int i)
        {
            float af[] = new float[i * 4];
            PApplet.arrayCopy(lineVertices, 0, af, 0, lineVertexCount * 4);
            lineVertices = af;
            lineVerticesBuffer = PGL.allocateFloatBuffer(lineVertices);
        }

        void expandPointColors(int i)
        {
            int ai[] = new int[i];
            PApplet.arrayCopy(pointColors, 0, ai, 0, pointVertexCount);
            pointColors = ai;
            pointColorsBuffer = PGL.allocateIntBuffer(pointColors);
        }

        void expandPointIndices(int i)
        {
            short aword0[] = new short[i];
            PApplet.arrayCopy(pointIndices, 0, aword0, 0, pointIndexCount);
            pointIndices = aword0;
            pointIndicesBuffer = PGL.allocateShortBuffer(pointIndices);
        }

        void expandPointOffsets(int i)
        {
            float af[] = new float[i * 2];
            PApplet.arrayCopy(pointOffsets, 0, af, 0, pointVertexCount * 2);
            pointOffsets = af;
            pointOffsetsBuffer = PGL.allocateFloatBuffer(pointOffsets);
        }

        void expandPointVertices(int i)
        {
            float af[] = new float[i * 4];
            PApplet.arrayCopy(pointVertices, 0, af, 0, pointVertexCount * 4);
            pointVertices = af;
            pointVerticesBuffer = PGL.allocateFloatBuffer(pointVertices);
        }

        void expandPolyAmbient(int i)
        {
            int ai[] = new int[i];
            PApplet.arrayCopy(polyAmbient, 0, ai, 0, polyVertexCount);
            polyAmbient = ai;
            polyAmbientBuffer = PGL.allocateIntBuffer(polyAmbient);
        }

        void expandPolyColors(int i)
        {
            int ai[] = new int[i];
            PApplet.arrayCopy(polyColors, 0, ai, 0, polyVertexCount);
            polyColors = ai;
            polyColorsBuffer = PGL.allocateIntBuffer(polyColors);
        }

        void expandPolyEmissive(int i)
        {
            int ai[] = new int[i];
            PApplet.arrayCopy(polyEmissive, 0, ai, 0, polyVertexCount);
            polyEmissive = ai;
            polyEmissiveBuffer = PGL.allocateIntBuffer(polyEmissive);
        }

        void expandPolyIndices(int i)
        {
            short aword0[] = new short[i];
            PApplet.arrayCopy(polyIndices, 0, aword0, 0, polyIndexCount);
            polyIndices = aword0;
            polyIndicesBuffer = PGL.allocateShortBuffer(polyIndices);
        }

        void expandPolyNormals(int i)
        {
            float af[] = new float[i * 3];
            PApplet.arrayCopy(polyNormals, 0, af, 0, polyVertexCount * 3);
            polyNormals = af;
            polyNormalsBuffer = PGL.allocateFloatBuffer(polyNormals);
        }

        void expandPolyShininess(int i)
        {
            float af[] = new float[i];
            PApplet.arrayCopy(polyShininess, 0, af, 0, polyVertexCount);
            polyShininess = af;
            polyShininessBuffer = PGL.allocateFloatBuffer(polyShininess);
        }

        void expandPolySpecular(int i)
        {
            int ai[] = new int[i];
            PApplet.arrayCopy(polySpecular, 0, ai, 0, polyVertexCount);
            polySpecular = ai;
            polySpecularBuffer = PGL.allocateIntBuffer(polySpecular);
        }

        void expandPolyTexCoords(int i)
        {
            float af[] = new float[i * 2];
            PApplet.arrayCopy(polyTexCoords, 0, af, 0, polyVertexCount * 2);
            polyTexCoords = af;
            polyTexCoordsBuffer = PGL.allocateFloatBuffer(polyTexCoords);
        }

        void expandPolyVertices(int i)
        {
            float af[] = new float[i * 4];
            PApplet.arrayCopy(polyVertices, 0, af, 0, polyVertexCount * 4);
            polyVertices = af;
            polyVerticesBuffer = PGL.allocateFloatBuffer(polyVertices);
        }

        void getLineVertexMax(PVector pvector, int i, int j)
        {
            for(; i <= j; i++)
            {
                int k = i * 4;
                float f = pvector.x;
                float af[] = lineVertices;
                int l = k + 1;
                pvector.x = PApplet.max(f, af[k]);
                pvector.y = PApplet.max(pvector.y, lineVertices[l]);
                pvector.z = PApplet.max(pvector.z, lineVertices[l + 1]);
            }

        }

        void getLineVertexMin(PVector pvector, int i, int j)
        {
            for(; i <= j; i++)
            {
                int k = i * 4;
                float f = pvector.x;
                float af[] = lineVertices;
                int l = k + 1;
                pvector.x = PApplet.min(f, af[k]);
                pvector.y = PApplet.min(pvector.y, lineVertices[l]);
                pvector.z = PApplet.min(pvector.z, lineVertices[l + 1]);
            }

        }

        int getLineVertexSum(PVector pvector, int i, int j)
        {
            for(int k = i; k <= j; k++)
            {
                int l = k * 4;
                float f = pvector.x;
                float af[] = lineVertices;
                int i1 = l + 1;
                pvector.x = af[l] + f;
                pvector.y = pvector.y + lineVertices[i1];
                pvector.z = pvector.z + lineVertices[i1 + 1];
            }

            return (j - i) + 1;
        }

        void getPointVertexMax(PVector pvector, int i, int j)
        {
            for(; i <= j; i++)
            {
                int k = i * 4;
                float f = pvector.x;
                float af[] = pointVertices;
                int l = k + 1;
                pvector.x = PApplet.max(f, af[k]);
                pvector.y = PApplet.max(pvector.y, pointVertices[l]);
                pvector.z = PApplet.max(pvector.z, pointVertices[l + 1]);
            }

        }

        void getPointVertexMin(PVector pvector, int i, int j)
        {
            for(; i <= j; i++)
            {
                int k = i * 4;
                float f = pvector.x;
                float af[] = pointVertices;
                int l = k + 1;
                pvector.x = PApplet.min(f, af[k]);
                pvector.y = PApplet.min(pvector.y, pointVertices[l]);
                pvector.z = PApplet.min(pvector.z, pointVertices[l + 1]);
            }

        }

        int getPointVertexSum(PVector pvector, int i, int j)
        {
            for(int k = i; k <= j; k++)
            {
                int l = k * 4;
                float f = pvector.x;
                float af[] = pointVertices;
                int i1 = l + 1;
                pvector.x = af[l] + f;
                pvector.y = pvector.y + pointVertices[i1];
                pvector.z = pvector.z + pointVertices[i1 + 1];
            }

            return (j - i) + 1;
        }

        void getPolyVertexMax(PVector pvector, int i, int j)
        {
            for(; i <= j; i++)
            {
                int k = i * 4;
                float f = pvector.x;
                float af[] = polyVertices;
                int l = k + 1;
                pvector.x = PApplet.max(f, af[k]);
                pvector.y = PApplet.max(pvector.y, polyVertices[l]);
                pvector.z = PApplet.max(pvector.z, polyVertices[l + 1]);
            }

        }

        void getPolyVertexMin(PVector pvector, int i, int j)
        {
            for(; i <= j; i++)
            {
                int k = i * 4;
                float f = pvector.x;
                float af[] = polyVertices;
                int l = k + 1;
                pvector.x = PApplet.min(f, af[k]);
                pvector.y = PApplet.min(pvector.y, polyVertices[l]);
                pvector.z = PApplet.min(pvector.z, polyVertices[l + 1]);
            }

        }

        int getPolyVertexSum(PVector pvector, int i, int j)
        {
            for(int k = i; k <= j; k++)
            {
                int l = k * 4;
                float f = pvector.x;
                float af[] = polyVertices;
                int i1 = l + 1;
                pvector.x = af[l] + f;
                pvector.y = pvector.y + polyVertices[i1];
                pvector.z = pvector.z + polyVertices[i1 + 1];
            }

            return (j - i) + 1;
        }

        void incLineIndices(int i, int j, int k)
        {
            for(; i <= j; i++)
            {
                short aword0[] = lineIndices;
                aword0[i] = (short)(aword0[i] + k);
            }

        }

        void incPointIndices(int i, int j, int k)
        {
            for(; i <= j; i++)
            {
                short aword0[] = pointIndices;
                aword0[i] = (short)(aword0[i] + k);
            }

        }

        void incPolyIndices(int i, int j, int k)
        {
            for(; i <= j; i++)
            {
                short aword0[] = polyIndices;
                aword0[i] = (short)(aword0[i] + k);
            }

        }

        void initAttrib(VertexAttribute vertexattribute)
        {
            if(vertexattribute.type != PGL.FLOAT || fpolyAttribs.containsKey(vertexattribute.name)) goto _L2; else goto _L1
_L1:
            float af[] = new float[vertexattribute.tessSize * PGL.DEFAULT_TESS_VERTICES];
            fpolyAttribs.put(vertexattribute.name, af);
            polyAttribBuffers.put(vertexattribute.name, PGL.allocateFloatBuffer(af));
_L4:
            return;
_L2:
            if(vertexattribute.type == PGL.INT && !ipolyAttribs.containsKey(vertexattribute.name))
            {
                int ai[] = new int[vertexattribute.tessSize * PGL.DEFAULT_TESS_VERTICES];
                ipolyAttribs.put(vertexattribute.name, ai);
                polyAttribBuffers.put(vertexattribute.name, PGL.allocateIntBuffer(ai));
            } else
            if(vertexattribute.type == PGL.BOOL && !bpolyAttribs.containsKey(vertexattribute.name))
            {
                byte abyte0[] = new byte[vertexattribute.tessSize * PGL.DEFAULT_TESS_VERTICES];
                bpolyAttribs.put(vertexattribute.name, abyte0);
                polyAttribBuffers.put(vertexattribute.name, PGL.allocateByteBuffer(abyte0));
            }
            if(true) goto _L4; else goto _L3
_L3:
        }

        boolean isFull()
        {
            boolean flag;
            if(PGL.FLUSH_VERTEX_COUNT <= polyVertexCount || PGL.FLUSH_VERTEX_COUNT <= lineVertexCount || PGL.FLUSH_VERTEX_COUNT <= pointVertexCount)
                flag = true;
            else
                flag = false;
            return flag;
        }

        void lineIndexCheck(int i)
        {
            int j = lineIndices.length;
            if(lineIndexCount + i > j)
                expandLineIndices(PGraphicsOpenGL.expandArraySize(j, lineIndexCount + i));
            firstLineIndex = lineIndexCount;
            lineIndexCount = lineIndexCount + i;
            lastLineIndex = lineIndexCount - 1;
        }

        void lineVertexCheck(int i)
        {
            int j = lineVertices.length / 4;
            if(lineVertexCount + i > j)
            {
                j = PGraphicsOpenGL.expandArraySize(j, lineVertexCount + i);
                expandLineVertices(j);
                expandLineColors(j);
                expandLineDirections(j);
            }
            firstLineVertex = lineVertexCount;
            lineVertexCount = lineVertexCount + i;
            lastLineVertex = lineVertexCount - 1;
        }

        void pointIndexCheck(int i)
        {
            int j = pointIndices.length;
            if(pointIndexCount + i > j)
                expandPointIndices(PGraphicsOpenGL.expandArraySize(j, pointIndexCount + i));
            firstPointIndex = pointIndexCount;
            pointIndexCount = pointIndexCount + i;
            lastPointIndex = pointIndexCount - 1;
        }

        void pointVertexCheck(int i)
        {
            int j = pointVertices.length / 4;
            if(pointVertexCount + i > j)
            {
                j = PGraphicsOpenGL.expandArraySize(j, pointVertexCount + i);
                expandPointVertices(j);
                expandPointColors(j);
                expandPointOffsets(j);
            }
            firstPointVertex = pointVertexCount;
            pointVertexCount = pointVertexCount + i;
            lastPointVertex = pointVertexCount - 1;
        }

        void polyIndexCheck()
        {
            if(polyIndexCount == polyIndices.length)
                expandPolyIndices(polyIndexCount << 1);
            firstPolyIndex = polyIndexCount;
            polyIndexCount = polyIndexCount + 1;
            lastPolyIndex = polyIndexCount - 1;
        }

        void polyIndexCheck(int i)
        {
            int j = polyIndices.length;
            if(polyIndexCount + i > j)
                expandPolyIndices(PGraphicsOpenGL.expandArraySize(j, polyIndexCount + i));
            firstPolyIndex = polyIndexCount;
            polyIndexCount = polyIndexCount + i;
            lastPolyIndex = polyIndexCount - 1;
        }

        void polyVertexCheck()
        {
            if(polyVertexCount == polyVertices.length / 4)
            {
                int i = polyVertexCount << 1;
                expandPolyVertices(i);
                expandPolyColors(i);
                expandPolyNormals(i);
                expandPolyTexCoords(i);
                expandPolyAmbient(i);
                expandPolySpecular(i);
                expandPolyEmissive(i);
                expandPolyShininess(i);
                expandAttributes(i);
            }
            firstPolyVertex = polyVertexCount;
            polyVertexCount = polyVertexCount + 1;
            lastPolyVertex = polyVertexCount - 1;
        }

        void polyVertexCheck(int i)
        {
            int j = polyVertices.length / 4;
            if(polyVertexCount + i > j)
            {
                j = PGraphicsOpenGL.expandArraySize(j, polyVertexCount + i);
                expandPolyVertices(j);
                expandPolyColors(j);
                expandPolyNormals(j);
                expandPolyTexCoords(j);
                expandPolyAmbient(j);
                expandPolySpecular(j);
                expandPolyEmissive(j);
                expandPolyShininess(j);
                expandAttributes(j);
            }
            firstPolyVertex = polyVertexCount;
            polyVertexCount = polyVertexCount + i;
            lastPolyVertex = polyVertexCount - 1;
        }

        void setLineVertex(int i, float af[], int j, int k)
        {
            int l = j * 3;
            j = l + 1;
            float f = af[l];
            float f1 = af[j];
            float f2 = af[j + 1];
            if(renderMode == 0 && pg.flushMode == 1)
            {
                af = pg.modelview;
                int i1 = i * 4;
                float af1[] = lineVertices;
                j = i1 + 1;
                af1[i1] = ((PMatrix3D) (af)).m00 * f + ((PMatrix3D) (af)).m01 * f1 + ((PMatrix3D) (af)).m02 * f2 + ((PMatrix3D) (af)).m03;
                af1 = lineVertices;
                i1 = j + 1;
                af1[j] = ((PMatrix3D) (af)).m10 * f + ((PMatrix3D) (af)).m11 * f1 + ((PMatrix3D) (af)).m12 * f2 + ((PMatrix3D) (af)).m13;
                lineVertices[i1] = ((PMatrix3D) (af)).m20 * f + ((PMatrix3D) (af)).m21 * f1 + ((PMatrix3D) (af)).m22 * f2 + ((PMatrix3D) (af)).m23;
                lineVertices[i1 + 1] = f * ((PMatrix3D) (af)).m30 + f1 * ((PMatrix3D) (af)).m31 + ((PMatrix3D) (af)).m32 * f2 + ((PMatrix3D) (af)).m33;
            } else
            {
                int j1 = i * 4;
                af = lineVertices;
                j = j1 + 1;
                af[j1] = f;
                af = lineVertices;
                j1 = j + 1;
                af[j] = f1;
                lineVertices[j1] = f2;
                lineVertices[j1 + 1] = 1.0F;
            }
            lineColors[i] = k;
            j = i * 4;
            af = lineDirections;
            i = j + 1;
            af[j] = 0.0F;
            af = lineDirections;
            j = i + 1;
            af[i] = 0.0F;
            lineDirections[j] = 0.0F;
            lineDirections[j + 1] = 0.0F;
        }

        void setLineVertex(int i, float af[], int j, int k, int l, float f)
        {
            int i1 = j * 3;
            j = i1 + 1;
            float f1 = af[i1];
            float f2 = af[j];
            float f3 = af[j + 1];
            k *= 3;
            j = k + 1;
            float f4 = af[k];
            float f5 = af[j];
            float f6 = af[j + 1];
            f4 -= f1;
            f5 -= f2;
            f6 -= f3;
            if(renderMode == 0 && pg.flushMode == 1)
            {
                af = pg.modelview;
                k = i * 4;
                float af1[] = lineVertices;
                j = k + 1;
                af1[k] = ((PMatrix3D) (af)).m00 * f1 + ((PMatrix3D) (af)).m01 * f2 + ((PMatrix3D) (af)).m02 * f3 + ((PMatrix3D) (af)).m03;
                af1 = lineVertices;
                k = j + 1;
                af1[j] = ((PMatrix3D) (af)).m10 * f1 + ((PMatrix3D) (af)).m11 * f2 + ((PMatrix3D) (af)).m12 * f3 + ((PMatrix3D) (af)).m13;
                lineVertices[k] = ((PMatrix3D) (af)).m20 * f1 + ((PMatrix3D) (af)).m21 * f2 + ((PMatrix3D) (af)).m22 * f3 + ((PMatrix3D) (af)).m23;
                lineVertices[k + 1] = f1 * ((PMatrix3D) (af)).m30 + f2 * ((PMatrix3D) (af)).m31 + ((PMatrix3D) (af)).m32 * f3 + ((PMatrix3D) (af)).m33;
                k = i * 4;
                af1 = lineDirections;
                j = k + 1;
                af1[k] = ((PMatrix3D) (af)).m00 * f4 + ((PMatrix3D) (af)).m01 * f5 + ((PMatrix3D) (af)).m02 * f6;
                lineDirections[j] = ((PMatrix3D) (af)).m10 * f4 + ((PMatrix3D) (af)).m11 * f5 + ((PMatrix3D) (af)).m12 * f6;
                lineDirections[j + 1] = ((PMatrix3D) (af)).m20 * f4 + ((PMatrix3D) (af)).m21 * f5 + ((PMatrix3D) (af)).m22 * f6;
            } else
            {
                k = i * 4;
                af = lineVertices;
                j = k + 1;
                af[k] = f1;
                af = lineVertices;
                k = j + 1;
                af[j] = f2;
                lineVertices[k] = f3;
                lineVertices[k + 1] = 1.0F;
                j = i * 4;
                af = lineDirections;
                k = j + 1;
                af[j] = f4;
                lineDirections[k] = f5;
                lineDirections[k + 1] = f6;
            }
            lineColors[i] = l;
            lineDirections[i * 4 + 3] = f;
        }

        void setPointVertex(int i, InGeometry ingeometry, int j)
        {
            int k = j * 3;
            float af[] = ingeometry.vertices;
            int j1 = k + 1;
            float f = af[k];
            float f1 = ingeometry.vertices[j1];
            float f2 = ingeometry.vertices[j1 + 1];
            if(renderMode == 0 && pg.flushMode == 1)
            {
                PMatrix3D pmatrix3d = pg.modelview;
                int l = i * 4;
                float af2[] = pointVertices;
                int k1 = l + 1;
                af2[l] = pmatrix3d.m00 * f + pmatrix3d.m01 * f1 + pmatrix3d.m02 * f2 + pmatrix3d.m03;
                af2 = pointVertices;
                l = k1 + 1;
                af2[k1] = pmatrix3d.m10 * f + pmatrix3d.m11 * f1 + pmatrix3d.m12 * f2 + pmatrix3d.m13;
                pointVertices[l] = pmatrix3d.m20 * f + pmatrix3d.m21 * f1 + pmatrix3d.m22 * f2 + pmatrix3d.m23;
                pointVertices[l + 1] = f * pmatrix3d.m30 + f1 * pmatrix3d.m31 + pmatrix3d.m32 * f2 + pmatrix3d.m33;
            } else
            {
                int i1 = i * 4;
                float af1[] = pointVertices;
                int l1 = i1 + 1;
                af1[i1] = f;
                af1 = pointVertices;
                i1 = l1 + 1;
                af1[l1] = f1;
                pointVertices[i1] = f2;
                pointVertices[i1 + 1] = 1.0F;
            }
            pointColors[i] = ingeometry.strokeColors[j];
        }

        void setPolyVertex(int i, float f, float f1, float f2, int j, float f3, float f4, 
                float f5, float f6, float f7, int k, int l, int i1, float f8, 
                boolean flag)
        {
            if(renderMode == 0 && pg.flushMode == 1)
            {
                Object obj = pg.modelview;
                PMatrix3D pmatrix3d = pg.modelviewInv;
                int j1 = i * 4;
                float af2[];
                int i2;
                float f9;
                float f10;
                float f11;
                if(flag)
                {
                    float af1[] = polyVertices;
                    int l1 = j1 + 1;
                    af1[j1] = PApplet.ceil(((PMatrix3D) (obj)).m00 * f + ((PMatrix3D) (obj)).m01 * f1 + ((PMatrix3D) (obj)).m02 * f2 + ((PMatrix3D) (obj)).m03);
                    af1 = polyVertices;
                    j1 = l1 + 1;
                    af1[l1] = PApplet.ceil(((PMatrix3D) (obj)).m10 * f + ((PMatrix3D) (obj)).m11 * f1 + ((PMatrix3D) (obj)).m12 * f2 + ((PMatrix3D) (obj)).m13);
                } else
                {
                    float af3[] = polyVertices;
                    int j2 = j1 + 1;
                    af3[j1] = ((PMatrix3D) (obj)).m00 * f + ((PMatrix3D) (obj)).m01 * f1 + ((PMatrix3D) (obj)).m02 * f2 + ((PMatrix3D) (obj)).m03;
                    af3 = polyVertices;
                    j1 = j2 + 1;
                    af3[j2] = ((PMatrix3D) (obj)).m10 * f + ((PMatrix3D) (obj)).m11 * f1 + ((PMatrix3D) (obj)).m12 * f2 + ((PMatrix3D) (obj)).m13;
                }
                polyVertices[j1] = ((PMatrix3D) (obj)).m20 * f + ((PMatrix3D) (obj)).m21 * f1 + ((PMatrix3D) (obj)).m22 * f2 + ((PMatrix3D) (obj)).m23;
                af2 = polyVertices;
                f9 = ((PMatrix3D) (obj)).m30;
                f10 = ((PMatrix3D) (obj)).m31;
                f11 = ((PMatrix3D) (obj)).m32;
                af2[j1 + 1] = ((PMatrix3D) (obj)).m33 + (f9 * f + f10 * f1 + f11 * f2);
                j1 = i * 3;
                obj = polyNormals;
                i2 = j1 + 1;
                obj[j1] = pmatrix3d.m00 * f3 + pmatrix3d.m10 * f4 + pmatrix3d.m20 * f5;
                polyNormals[i2] = pmatrix3d.m01 * f3 + pmatrix3d.m11 * f4 + pmatrix3d.m21 * f5;
                obj = polyNormals;
                f1 = pmatrix3d.m02;
                f = pmatrix3d.m12;
                obj[i2 + 1] = pmatrix3d.m22 * f5 + (f1 * f3 + f * f4);
            } else
            {
                int k2 = i * 4;
                float af[] = polyVertices;
                int k1 = k2 + 1;
                af[k2] = f;
                af = polyVertices;
                k2 = k1 + 1;
                af[k1] = f1;
                polyVertices[k2] = f2;
                polyVertices[k2 + 1] = 1.0F;
                k2 = i * 3;
                af = polyNormals;
                k1 = k2 + 1;
                af[k2] = f3;
                polyNormals[k1] = f4;
                polyNormals[k1 + 1] = f5;
            }
            polyColors[i] = j;
            j = i * 2;
            polyTexCoords[j] = f6;
            polyTexCoords[j + 1] = f7;
            polyAmbient[i] = k;
            polySpecular[i] = l;
            polyEmissive[i] = i1;
            polyShininess[i] = f8;
        }

        void setPolyVertex(int i, float f, float f1, float f2, int j, boolean flag)
        {
            setPolyVertex(i, f, f1, f2, j, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F, flag);
        }

        void trim()
        {
            if(polyVertexCount > 0 && polyVertexCount < polyVertices.length / 4)
            {
                trimPolyVertices();
                trimPolyColors();
                trimPolyNormals();
                trimPolyTexCoords();
                trimPolyAmbient();
                trimPolySpecular();
                trimPolyEmissive();
                trimPolyShininess();
                trimPolyAttributes();
            }
            if(polyIndexCount > 0 && polyIndexCount < polyIndices.length)
                trimPolyIndices();
            if(lineVertexCount > 0 && lineVertexCount < lineVertices.length / 4)
            {
                trimLineVertices();
                trimLineColors();
                trimLineDirections();
            }
            if(lineIndexCount > 0 && lineIndexCount < lineIndices.length)
                trimLineIndices();
            if(pointVertexCount > 0 && pointVertexCount < pointVertices.length / 4)
            {
                trimPointVertices();
                trimPointColors();
                trimPointOffsets();
            }
            if(pointIndexCount > 0 && pointIndexCount < pointIndices.length)
                trimPointIndices();
        }

        void trimBoolAttribute(VertexAttribute vertexattribute)
        {
            byte abyte0[] = (byte[])bpolyAttribs.get(vertexattribute.name);
            byte abyte1[] = new byte[vertexattribute.tessSize * polyVertexCount];
            PApplet.arrayCopy(abyte0, 0, abyte1, 0, vertexattribute.tessSize * polyVertexCount);
            bpolyAttribs.put(vertexattribute.name, abyte1);
            polyAttribBuffers.put(vertexattribute.name, PGL.allocateByteBuffer(abyte1));
        }

        void trimFloatAttribute(VertexAttribute vertexattribute)
        {
            float af[] = (float[])fpolyAttribs.get(vertexattribute.name);
            float af1[] = new float[vertexattribute.tessSize * polyVertexCount];
            PApplet.arrayCopy(af, 0, af1, 0, vertexattribute.tessSize * polyVertexCount);
            fpolyAttribs.put(vertexattribute.name, af1);
            polyAttribBuffers.put(vertexattribute.name, PGL.allocateFloatBuffer(af1));
        }

        void trimIntAttribute(VertexAttribute vertexattribute)
        {
            int ai[] = (int[])ipolyAttribs.get(vertexattribute.name);
            int ai1[] = new int[vertexattribute.tessSize * polyVertexCount];
            PApplet.arrayCopy(ai, 0, ai1, 0, vertexattribute.tessSize * polyVertexCount);
            ipolyAttribs.put(vertexattribute.name, ai1);
            polyAttribBuffers.put(vertexattribute.name, PGL.allocateIntBuffer(ai1));
        }

        void trimLineColors()
        {
            int ai[] = new int[lineVertexCount];
            PApplet.arrayCopy(lineColors, 0, ai, 0, lineVertexCount);
            lineColors = ai;
            lineColorsBuffer = PGL.allocateIntBuffer(lineColors);
        }

        void trimLineDirections()
        {
            float af[] = new float[lineVertexCount * 4];
            PApplet.arrayCopy(lineDirections, 0, af, 0, lineVertexCount * 4);
            lineDirections = af;
            lineDirectionsBuffer = PGL.allocateFloatBuffer(lineDirections);
        }

        void trimLineIndices()
        {
            short aword0[] = new short[lineIndexCount];
            PApplet.arrayCopy(lineIndices, 0, aword0, 0, lineIndexCount);
            lineIndices = aword0;
            lineIndicesBuffer = PGL.allocateShortBuffer(lineIndices);
        }

        void trimLineVertices()
        {
            float af[] = new float[lineVertexCount * 4];
            PApplet.arrayCopy(lineVertices, 0, af, 0, lineVertexCount * 4);
            lineVertices = af;
            lineVerticesBuffer = PGL.allocateFloatBuffer(lineVertices);
        }

        void trimPointColors()
        {
            int ai[] = new int[pointVertexCount];
            PApplet.arrayCopy(pointColors, 0, ai, 0, pointVertexCount);
            pointColors = ai;
            pointColorsBuffer = PGL.allocateIntBuffer(pointColors);
        }

        void trimPointIndices()
        {
            short aword0[] = new short[pointIndexCount];
            PApplet.arrayCopy(pointIndices, 0, aword0, 0, pointIndexCount);
            pointIndices = aword0;
            pointIndicesBuffer = PGL.allocateShortBuffer(pointIndices);
        }

        void trimPointOffsets()
        {
            float af[] = new float[pointVertexCount * 2];
            PApplet.arrayCopy(pointOffsets, 0, af, 0, pointVertexCount * 2);
            pointOffsets = af;
            pointOffsetsBuffer = PGL.allocateFloatBuffer(pointOffsets);
        }

        void trimPointVertices()
        {
            float af[] = new float[pointVertexCount * 4];
            PApplet.arrayCopy(pointVertices, 0, af, 0, pointVertexCount * 4);
            pointVertices = af;
            pointVerticesBuffer = PGL.allocateFloatBuffer(pointVertices);
        }

        void trimPolyAmbient()
        {
            int ai[] = new int[polyVertexCount];
            PApplet.arrayCopy(polyAmbient, 0, ai, 0, polyVertexCount);
            polyAmbient = ai;
            polyAmbientBuffer = PGL.allocateIntBuffer(polyAmbient);
        }

        void trimPolyAttributes()
        {
            Iterator iterator = polyAttribs.keySet().iterator();
            do
            {
                if(!iterator.hasNext())
                    break;
                Object obj = (String)iterator.next();
                obj = (VertexAttribute)polyAttribs.get(obj);
                if(((VertexAttribute) (obj)).type == PGL.FLOAT)
                    trimFloatAttribute(((VertexAttribute) (obj)));
                else
                if(((VertexAttribute) (obj)).type == PGL.INT)
                    trimIntAttribute(((VertexAttribute) (obj)));
                else
                if(((VertexAttribute) (obj)).type == PGL.BOOL)
                    trimBoolAttribute(((VertexAttribute) (obj)));
            } while(true);
        }

        void trimPolyColors()
        {
            int ai[] = new int[polyVertexCount];
            PApplet.arrayCopy(polyColors, 0, ai, 0, polyVertexCount);
            polyColors = ai;
            polyColorsBuffer = PGL.allocateIntBuffer(polyColors);
        }

        void trimPolyEmissive()
        {
            int ai[] = new int[polyVertexCount];
            PApplet.arrayCopy(polyEmissive, 0, ai, 0, polyVertexCount);
            polyEmissive = ai;
            polyEmissiveBuffer = PGL.allocateIntBuffer(polyEmissive);
        }

        void trimPolyIndices()
        {
            short aword0[] = new short[polyIndexCount];
            PApplet.arrayCopy(polyIndices, 0, aword0, 0, polyIndexCount);
            polyIndices = aword0;
            polyIndicesBuffer = PGL.allocateShortBuffer(polyIndices);
        }

        void trimPolyNormals()
        {
            float af[] = new float[polyVertexCount * 3];
            PApplet.arrayCopy(polyNormals, 0, af, 0, polyVertexCount * 3);
            polyNormals = af;
            polyNormalsBuffer = PGL.allocateFloatBuffer(polyNormals);
        }

        void trimPolyShininess()
        {
            float af[] = new float[polyVertexCount];
            PApplet.arrayCopy(polyShininess, 0, af, 0, polyVertexCount);
            polyShininess = af;
            polyShininessBuffer = PGL.allocateFloatBuffer(polyShininess);
        }

        void trimPolySpecular()
        {
            int ai[] = new int[polyVertexCount];
            PApplet.arrayCopy(polySpecular, 0, ai, 0, polyVertexCount);
            polySpecular = ai;
            polySpecularBuffer = PGL.allocateIntBuffer(polySpecular);
        }

        void trimPolyTexCoords()
        {
            float af[] = new float[polyVertexCount * 2];
            PApplet.arrayCopy(polyTexCoords, 0, af, 0, polyVertexCount * 2);
            polyTexCoords = af;
            polyTexCoordsBuffer = PGL.allocateFloatBuffer(polyTexCoords);
        }

        void trimPolyVertices()
        {
            float af[] = new float[polyVertexCount * 4];
            PApplet.arrayCopy(polyVertices, 0, af, 0, polyVertexCount * 4);
            polyVertices = af;
            polyVerticesBuffer = PGL.allocateFloatBuffer(polyVertices);
        }

        protected void updateAttribBuffer(String s)
        {
            updateAttribBuffer(s, 0, polyVertexCount);
        }

        protected void updateAttribBuffer(String s, int i, int j)
        {
            VertexAttribute vertexattribute = (VertexAttribute)polyAttribs.get(s);
            if(vertexattribute.type != PGL.FLOAT) goto _L2; else goto _L1
_L1:
            PGL.updateFloatBuffer((FloatBuffer)polyAttribBuffers.get(s), (float[])fpolyAttribs.get(s), vertexattribute.tessSize * i, vertexattribute.tessSize * j);
_L4:
            return;
_L2:
            if(vertexattribute.type == PGL.INT)
                PGL.updateIntBuffer((IntBuffer)polyAttribBuffers.get(s), (int[])ipolyAttribs.get(s), vertexattribute.tessSize * i, vertexattribute.tessSize * j);
            else
            if(vertexattribute.type == PGL.BOOL)
                PGL.updateByteBuffer((ByteBuffer)polyAttribBuffers.get(s), (byte[])bpolyAttribs.get(s), vertexattribute.tessSize * i, vertexattribute.tessSize * j);
            if(true) goto _L4; else goto _L3
_L3:
        }

        protected void updateLineColorsBuffer()
        {
            updateLineColorsBuffer(0, lineVertexCount);
        }

        protected void updateLineColorsBuffer(int i, int j)
        {
            PGL.updateIntBuffer(lineColorsBuffer, lineColors, i, j);
        }

        protected void updateLineDirectionsBuffer()
        {
            updateLineDirectionsBuffer(0, lineVertexCount);
        }

        protected void updateLineDirectionsBuffer(int i, int j)
        {
            PGL.updateFloatBuffer(lineDirectionsBuffer, lineDirections, i * 4, j * 4);
        }

        protected void updateLineIndicesBuffer()
        {
            updateLineIndicesBuffer(0, lineIndexCount);
        }

        protected void updateLineIndicesBuffer(int i, int j)
        {
            PGL.updateShortBuffer(lineIndicesBuffer, lineIndices, i, j);
        }

        protected void updateLineVerticesBuffer()
        {
            updateLineVerticesBuffer(0, lineVertexCount);
        }

        protected void updateLineVerticesBuffer(int i, int j)
        {
            PGL.updateFloatBuffer(lineVerticesBuffer, lineVertices, i * 4, j * 4);
        }

        protected void updatePointColorsBuffer()
        {
            updatePointColorsBuffer(0, pointVertexCount);
        }

        protected void updatePointColorsBuffer(int i, int j)
        {
            PGL.updateIntBuffer(pointColorsBuffer, pointColors, i, j);
        }

        protected void updatePointIndicesBuffer()
        {
            updatePointIndicesBuffer(0, pointIndexCount);
        }

        protected void updatePointIndicesBuffer(int i, int j)
        {
            PGL.updateShortBuffer(pointIndicesBuffer, pointIndices, i, j);
        }

        protected void updatePointOffsetsBuffer()
        {
            updatePointOffsetsBuffer(0, pointVertexCount);
        }

        protected void updatePointOffsetsBuffer(int i, int j)
        {
            PGL.updateFloatBuffer(pointOffsetsBuffer, pointOffsets, i * 2, j * 2);
        }

        protected void updatePointVerticesBuffer()
        {
            updatePointVerticesBuffer(0, pointVertexCount);
        }

        protected void updatePointVerticesBuffer(int i, int j)
        {
            PGL.updateFloatBuffer(pointVerticesBuffer, pointVertices, i * 4, j * 4);
        }

        protected void updatePolyAmbientBuffer()
        {
            updatePolyAmbientBuffer(0, polyVertexCount);
        }

        protected void updatePolyAmbientBuffer(int i, int j)
        {
            PGL.updateIntBuffer(polyAmbientBuffer, polyAmbient, i, j);
        }

        protected void updatePolyColorsBuffer()
        {
            updatePolyColorsBuffer(0, polyVertexCount);
        }

        protected void updatePolyColorsBuffer(int i, int j)
        {
            PGL.updateIntBuffer(polyColorsBuffer, polyColors, i, j);
        }

        protected void updatePolyEmissiveBuffer()
        {
            updatePolyEmissiveBuffer(0, polyVertexCount);
        }

        protected void updatePolyEmissiveBuffer(int i, int j)
        {
            PGL.updateIntBuffer(polyEmissiveBuffer, polyEmissive, i, j);
        }

        protected void updatePolyIndicesBuffer()
        {
            updatePolyIndicesBuffer(0, polyIndexCount);
        }

        protected void updatePolyIndicesBuffer(int i, int j)
        {
            PGL.updateShortBuffer(polyIndicesBuffer, polyIndices, i, j);
        }

        protected void updatePolyNormalsBuffer()
        {
            updatePolyNormalsBuffer(0, polyVertexCount);
        }

        protected void updatePolyNormalsBuffer(int i, int j)
        {
            PGL.updateFloatBuffer(polyNormalsBuffer, polyNormals, i * 3, j * 3);
        }

        protected void updatePolyShininessBuffer()
        {
            updatePolyShininessBuffer(0, polyVertexCount);
        }

        protected void updatePolyShininessBuffer(int i, int j)
        {
            PGL.updateFloatBuffer(polyShininessBuffer, polyShininess, i, j);
        }

        protected void updatePolySpecularBuffer()
        {
            updatePolySpecularBuffer(0, polyVertexCount);
        }

        protected void updatePolySpecularBuffer(int i, int j)
        {
            PGL.updateIntBuffer(polySpecularBuffer, polySpecular, i, j);
        }

        protected void updatePolyTexCoordsBuffer()
        {
            updatePolyTexCoordsBuffer(0, polyVertexCount);
        }

        protected void updatePolyTexCoordsBuffer(int i, int j)
        {
            PGL.updateFloatBuffer(polyTexCoordsBuffer, polyTexCoords, i * 2, j * 2);
        }

        protected void updatePolyVerticesBuffer()
        {
            updatePolyVerticesBuffer(0, polyVertexCount);
        }

        protected void updatePolyVerticesBuffer(int i, int j)
        {
            PGL.updateFloatBuffer(polyVerticesBuffer, polyVertices, i * 4, j * 4);
        }

        HashMap bpolyAttribs;
        int firstLineIndex;
        int firstLineVertex;
        int firstPointIndex;
        int firstPointVertex;
        int firstPolyIndex;
        int firstPolyVertex;
        HashMap fpolyAttribs;
        HashMap ipolyAttribs;
        int lastLineIndex;
        int lastLineVertex;
        int lastPointIndex;
        int lastPointVertex;
        int lastPolyIndex;
        int lastPolyVertex;
        int lineColors[];
        IntBuffer lineColorsBuffer;
        float lineDirections[];
        FloatBuffer lineDirectionsBuffer;
        IndexCache lineIndexCache;
        int lineIndexCount;
        short lineIndices[];
        ShortBuffer lineIndicesBuffer;
        int lineVertexCount;
        float lineVertices[];
        FloatBuffer lineVerticesBuffer;
        PGraphicsOpenGL pg;
        int pointColors[];
        IntBuffer pointColorsBuffer;
        IndexCache pointIndexCache;
        int pointIndexCount;
        short pointIndices[];
        ShortBuffer pointIndicesBuffer;
        float pointOffsets[];
        FloatBuffer pointOffsetsBuffer;
        int pointVertexCount;
        float pointVertices[];
        FloatBuffer pointVerticesBuffer;
        int polyAmbient[];
        IntBuffer polyAmbientBuffer;
        HashMap polyAttribBuffers;
        AttributeMap polyAttribs;
        int polyColors[];
        IntBuffer polyColorsBuffer;
        int polyEmissive[];
        IntBuffer polyEmissiveBuffer;
        IndexCache polyIndexCache;
        int polyIndexCount;
        short polyIndices[];
        ShortBuffer polyIndicesBuffer;
        float polyNormals[];
        FloatBuffer polyNormalsBuffer;
        float polyShininess[];
        FloatBuffer polyShininessBuffer;
        int polySpecular[];
        IntBuffer polySpecularBuffer;
        float polyTexCoords[];
        FloatBuffer polyTexCoordsBuffer;
        int polyVertexCount;
        float polyVertices[];
        FloatBuffer polyVerticesBuffer;
        int renderMode;

        TessGeometry(PGraphicsOpenGL pgraphicsopengl, AttributeMap attributemap, int i)
        {
            polyAttribBuffers = new HashMap();
            polyIndexCache = new IndexCache();
            lineIndexCache = new IndexCache();
            pointIndexCache = new IndexCache();
            fpolyAttribs = new HashMap();
            ipolyAttribs = new HashMap();
            bpolyAttribs = new HashMap();
            pg = pgraphicsopengl;
            polyAttribs = attributemap;
            renderMode = i;
            allocate();
        }
    }

    protected static class Tessellator
    {

        int addBevel3D(int i, int j, int k, int l, int i1, short aword0[], boolean flag)
        {
            IndexCache indexcache = tess.lineIndexCache;
            int j1 = indexcache.vertexCount[i1];
            boolean flag1;
            int k1;
            int l1;
            int i2;
            float f;
            TessGeometry tessgeometry;
            int j2;
            int k2;
            if(PGL.MAX_VERTEX_INDEX1 <= j1 + 3)
            {
                i1 = indexcache.addNew();
                flag1 = true;
                j1 = 0;
            } else
            {
                flag1 = false;
            }
            k1 = indexcache.indexOffset[i1] + indexcache.indexCount[i1];
            l1 = indexcache.vertexOffset[i1] + indexcache.vertexCount[i1];
            if(flag)
                i2 = strokeColor;
            else
                i2 = strokeColors[i];
            if(flag)
                f = strokeWeight;
            else
                f = strokeWeights[i];
            f *= transformScale();
            tessgeometry = tess;
            j2 = l1 + 1;
            tessgeometry.setLineVertex(l1, strokeVertices, i, i2);
            tessgeometry = tess;
            k2 = j2 + 1;
            tessgeometry.setLineVertex(j2, strokeVertices, i, j, i2, f / 2.0F);
            tessgeometry = tess;
            l1 = k2 + 1;
            tessgeometry.setLineVertex(k2, strokeVertices, i, j, i2, -f / 2.0F);
            j = 0;
            i = j;
            if(flag1)
            {
                i = j;
                if(-1 < k)
                {
                    i = j;
                    if(-1 < l)
                    {
                        short aword1[];
                        if(flag)
                            i = strokeColor;
                        else
                            i = strokeColors[l];
                        if(flag)
                            f = strokeWeight;
                        else
                            f = strokeWeights[l];
                        f *= transformScale();
                        tess.setLineVertex(l1, strokeVertices, l, k, i, -f / 2.0F);
                        tess.setLineVertex(l1 + 1, strokeVertices, l, k, i, f / 2.0F);
                        aword0[0] = (short)(j1 + 3);
                        aword0[1] = (short)(j1 + 4);
                        i = 2;
                    }
                }
            }
            aword1 = tess.lineIndices;
            j = k1 + 1;
            aword1[k1] = (short)(j1 + 0);
            aword1 = tess.lineIndices;
            k = j + 1;
            aword1[j] = aword0[0];
            aword1 = tess.lineIndices;
            j = k + 1;
            aword1[k] = (short)(j1 + 1);
            aword1 = tess.lineIndices;
            k = j + 1;
            aword1[j] = (short)(j1 + 0);
            tess.lineIndices[k] = (short)(j1 + 2);
            tess.lineIndices[k + 1] = aword0[1];
            indexcache.incCounts(i1, 6, i + 3);
            return i1;
        }

        void addBezierVertex(int i)
        {
            pg.0.addPolyVertices = i;
            pg.bezierInitCheck();
            pg.bezierVertexCheck(20, i);
            PMatrix3D pmatrix3d = 
// JavaClassFileOutputException: get_constant: invalid tag

        void addCurveInitialVertex(int i, float f, float f1, float f2)
        {
            if(fill)
            {
                double ad[] = collectVertexAttributes(i);
                ad[0] = f;
                ad[1] = f1;
                ad[2] = f2;
                gluTess.addVertex(ad);
            }
            if(stroke)
                addStrokeVertex(f, f1, f2, in.strokeColors[i], strokeWeight);
        }

        void addCurveVertex(int i)
        {
            pg.curveVertexCheck(20);
            float af[] = 
// JavaClassFileOutputException: get_constant: invalid tag

        void addCurveVertexSegment(int i, float f, float f1, float f2, float f3, float f4, float f5, 
                float f6, float f7, float f8, float f9, float f10, float f11)
        {
            int j = 0;
            float f12 = 0.0F;
            if(stroke)
            {
                j = in.strokeColors[i];
                f12 = in.strokeWeights[i];
            }
            double ad[];
            PMatrix3D pmatrix3d;
            float f13;
            float f14;
            float f15;
            float f16;
            float f17;
            float f18;
            float f19;
            float f20;
            float f21;
            float f22;
            float f23;
            float f24;
            float f25;
            float f26;
            float f27;
            float f28;
            float f29;
            float f30;
            float f31;
            float f32;
            float f33;
            float f34;
            float f36;
            float f37;
            float f39;
            float f41;
            float f42;
            float f43;
            float f44;
            float f45;
            float f46;
            float f47;
            float f48;
            if(fill)
                ad = collectVertexAttributes(i);
            else
                ad = null;
            pmatrix3d = ((TessGeometry) (pg)).lineVertexCount;
            f13 = pmatrix3d.m10;
            f14 = pmatrix3d.m11;
            f15 = pmatrix3d.m12;
            f16 = pmatrix3d.m13;
            f17 = pmatrix3d.m20;
            f18 = pmatrix3d.m21;
            f19 = pmatrix3d.m22;
            f20 = pmatrix3d.m23;
            f21 = pmatrix3d.m30;
            f22 = pmatrix3d.m31;
            f23 = pmatrix3d.m32;
            f24 = pmatrix3d.m33;
            f25 = pmatrix3d.m10;
            f26 = pmatrix3d.m11;
            f27 = pmatrix3d.m12;
            f28 = pmatrix3d.m13;
            f29 = pmatrix3d.m20 * f1 + pmatrix3d.m21 * f4 + pmatrix3d.m22 * f7 + pmatrix3d.m23 * f10;
            f30 = pmatrix3d.m30;
            f31 = pmatrix3d.m31;
            f32 = pmatrix3d.m32;
            f33 = pmatrix3d.m33;
            f34 = pmatrix3d.m10;
            f36 = pmatrix3d.m11;
            f37 = pmatrix3d.m12;
            f39 = pmatrix3d.m13;
            f41 = pmatrix3d.m20;
            f42 = pmatrix3d.m21;
            f43 = pmatrix3d.m22;
            f44 = pmatrix3d.m23;
            f45 = pmatrix3d.m30;
            f46 = pmatrix3d.m31;
            f47 = pmatrix3d.m32;
            f48 = pmatrix3d.m33;
            f13 = f13 * f + f14 * f3 + f15 * f6 + f16 * f9;
            f27 = f25 * f1 + f26 * f4 + f27 * f7 + f28 * f10;
            i = 0;
            f44 = f41 * f2 + f42 * f5 + f43 * f8 + f44 * f11;
            f36 = f39 * f11 + (f34 * f2 + f36 * f5 + f37 * f8);
            f18 = f17 * f + f18 * f3 + f19 * f6 + f20 * f9;
            f17 = f5;
            f19 = f4;
            f20 = f3;
            while(i < pg.curveDetail) 
            {
                f20 += f13;
                f19 += f27;
                f17 += f36;
                if(fill)
                {
                    double ad1[] = Arrays.copyOf(ad, ad.length);
                    ad1[0] = f20;
                    ad1[1] = f19;
                    ad1[2] = f17;
                    gluTess.addVertex(ad1);
                }
                if(stroke)
                    addStrokeVertex(f20, f19, f17, j, f12);
                i++;
                float f38 = f18 + (f21 * f + f22 * f3 + f23 * f6 + f24 * f9);
                float f35 = f44 + (f45 * f2 + f46 * f5 + f47 * f8 + f48 * f11);
                float f40 = f29 + (f30 * f1 + f31 * f4 + f32 * f7 + f33 * f10);
                f36 += f44;
                f13 += f18;
                f27 += f29;
                f44 = f35;
                f18 = f38;
                f29 = f40;
            }
        }

        void addDupIndex(int i)
        {
            int j;
            j = 0;
            if(dupIndices == null)
                dupIndices = new int[16];
            if(dupIndices.length == dupCount)
            {
                int ai[] = new int[dupCount << 1];
                PApplet.arrayCopy(dupIndices, 0, ai, 0, dupCount);
                dupIndices = ai;
            }
            if(i >= dupIndices[0]) goto _L2; else goto _L1
_L1:
            for(j = dupCount; j > 0; j--)
                dupIndices[j] = dupIndices[j - 1];

            dupIndices[0] = i;
            dupCount = dupCount + 1;
_L4:
            return;
_L2:
            if(dupIndices[dupCount - 1] < i)
            {
                dupIndices[dupCount] = i;
                dupCount = dupCount + 1;
                continue; /* Loop/switch isn't completed */
            }
            do
            {
                if(j >= dupCount - 1 || dupIndices[j] == i)
                    continue; /* Loop/switch isn't completed */
                if(dupIndices[j] >= i || i >= dupIndices[j + 1])
                    j++;
                else
                    break;
            } while(true);
            for(int k = dupCount; k > j + 1; k--)
                dupIndices[k] = dupIndices[k - 1];

            dupIndices[j + 1] = i;
            dupCount = dupCount + 1;
            if(true) goto _L4; else goto _L3
_L3:
        }

        int addLineSegment2D(int i, int j, int k, boolean flag, boolean flag1)
        {
            IndexCache indexcache = tess.polyIndexCache;
            int l = indexcache.vertexCount[k];
            int i1;
            int j1;
            int k1;
            float f;
            float f1;
            float f3;
            float f5;
            float f6;
            float f7;
            float f8;
            float f9;
            float f10;
            float f11;
            TessGeometry tessgeometry;
            short aword0[];
            int l1;
            if(PGL.MAX_VERTEX_INDEX1 <= l + 4)
            {
                i1 = indexcache.addNew();
                l = 0;
            } else
            {
                i1 = k;
            }
            j1 = indexcache.indexOffset[i1] + indexcache.indexCount[i1];
            k = indexcache.vertexOffset[i1];
            k1 = indexcache.vertexCount[i1] + k;
            if(flag)
                k = strokeColor;
            else
                k = strokeColors[i];
            if(flag)
                f = strokeWeight;
            else
                f = strokeWeights[i];
            if(subPixelStroke(f))
                flag1 = false;
            f1 = strokeVertices[i * 3 + 0];
            f3 = strokeVertices[i * 3 + 1];
            f5 = strokeVertices[j * 3 + 0];
            f6 = strokeVertices[j * 3 + 1];
            f7 = f5 - f1;
            f8 = f6 - f3;
            f9 = PApplet.sqrt(f7 * f7 + f8 * f8);
            if(PGraphicsOpenGL.nonZero(f9))
            {
                f10 = -f8 / f9;
                f11 = f7 / f9;
                f7 /= f9;
                float f12 = PApplet.min(0.75F, f / 2.0F);
                f9 = f8 / f9;
                f8 = PApplet.min(0.75F, f / 2.0F);
                f7 = f12 * f7;
                f8 = f9 * f8;
            } else
            {
                f7 = 0.0F;
                f11 = 0.0F;
                f10 = 0.0F;
                f8 = 0.0F;
            }
            f9 = (f10 * f) / 2.0F;
            f = (f * f11) / 2.0F;
            tessgeometry = tess;
            l1 = k1 + 1;
            tessgeometry.setPolyVertex(k1, (f1 + f9) - f7, (f3 + f) - f8, 0.0F, k, flag1);
            aword0 = tess.polyIndices;
            k1 = j1 + 1;
            aword0[j1] = (short)(l + 0);
            aword0 = tess;
            i = l1 + 1;
            aword0.setPolyVertex(l1, f1 - f9 - f7, f3 - f - f8, 0.0F, k, flag1);
            aword0 = tess.polyIndices;
            j1 = k1 + 1;
            aword0[k1] = (short)(l + 1);
            if(flag1)
            {
                float f14 = tess.polyVertices[(i - 2) * 4 + 0];
                float f15 = tess.polyVertices[(i - 2) * 4 + 1];
                float f13 = tess.polyVertices[(i - 1) * 4 + 0];
                float f16 = tess.polyVertices[(i - 1) * 4 + 1];
                if(PGraphicsOpenGL.same(f14, f13) && PGraphicsOpenGL.same(f15, f16))
                {
                    unclampLine2D(i - 2, (f1 + f9) - f7, (f3 + f) - f8);
                    unclampLine2D(i - 1, f1 - f9 - f7, f3 - f - f8);
                }
            }
            if(!flag)
            {
                k = strokeColors[j];
                f9 = strokeWeights[j];
                f = (f10 * f9) / 2.0F;
                f11 = (f11 * f9) / 2.0F;
                float f2;
                float f4;
                TessGeometry tessgeometry1;
                short aword1[];
                if(subPixelStroke(f9))
                {
                    flag1 = false;
                    f10 = f11;
                    f11 = f;
                    f = f10;
                } else
                {
                    f10 = f;
                    f = f11;
                    f11 = f10;
                }
            } else
            {
                f11 = f9;
            }
            tessgeometry1 = tess;
            j = i + 1;
            tessgeometry1.setPolyVertex(i, (f5 - f11) + f7, (f6 - f) + f8, 0.0F, k, flag1);
            aword1 = tess.polyIndices;
            i = j1 + 1;
            aword1[j1] = (short)(l + 2);
            aword1 = tess.polyIndices;
            j1 = i + 1;
            aword1[i] = (short)(l + 2);
            aword1 = tess.polyIndices;
            i = j1 + 1;
            aword1[j1] = (short)(l + 0);
            aword1 = tess;
            j1 = j + 1;
            aword1.setPolyVertex(j, f5 + f11 + f7, f6 + f + f8, 0.0F, k, flag1);
            tess.polyIndices[i] = (short)(l + 3);
            if(flag1)
            {
                f9 = tess.polyVertices[(j1 - 2) * 4 + 0];
                f10 = tess.polyVertices[(j1 - 2) * 4 + 1];
                f2 = tess.polyVertices[(j1 - 1) * 4 + 0];
                f4 = tess.polyVertices[(j1 - 1) * 4 + 1];
                if(PGraphicsOpenGL.same(f9, f2) && PGraphicsOpenGL.same(f10, f4))
                {
                    unclampLine2D(j1 - 2, (f5 - f11) + f7, (f6 - f) + f8);
                    unclampLine2D(j1 - 1, f5 + f11 + f7, f6 + f + f8);
                }
            }
            indexcache.incCounts(i1, 6, 4);
            return i1;
        }

        int addLineSegment3D(int i, int j, int k, int l, int i1, short aword0[], boolean flag)
        {
            IndexCache indexcache = tess.lineIndexCache;
            int j1 = indexcache.vertexCount[i1];
            int k1;
            int l1;
            int i2;
            int j2;
            float f;
            TessGeometry tessgeometry;
            short aword1[];
            int k2;
            int l2;
            if(aword0 != null && -1 < aword0[0] && -1 < aword0[1])
                k1 = 1;
            else
                k1 = 0;
            l1 = PGL.MAX_VERTEX_INDEX1;
            if(k1 != 0)
                k1 = 1;
            else
                k1 = 0;
            if(l1 <= k1 + (j1 + 4))
            {
                i1 = indexcache.addNew();
                j1 = 1;
                k1 = 0;
            } else
            {
                boolean flag1 = false;
                k1 = j1;
                j1 = ((flag1) ? 1 : 0);
            }
            i2 = indexcache.indexOffset[i1] + indexcache.indexCount[i1];
            l1 = indexcache.vertexOffset[i1];
            j2 = indexcache.vertexCount[i1] + l1;
            if(flag)
                l1 = strokeColor;
            else
                l1 = strokeColors[i];
            if(flag)
                f = strokeWeight;
            else
                f = strokeWeights[i];
            f *= transformScale();
            tessgeometry = tess;
            k2 = j2 + 1;
            tessgeometry.setLineVertex(j2, strokeVertices, i, j, l1, f / 2.0F);
            aword1 = tess.lineIndices;
            j2 = i2 + 1;
            aword1[i2] = (short)(k1 + 0);
            aword1 = tess;
            l2 = k2 + 1;
            aword1.setLineVertex(k2, strokeVertices, i, j, l1, -f / 2.0F);
            aword1 = tess.lineIndices;
            k2 = j2 + 1;
            aword1[j2] = (short)(k1 + 1);
            if(flag)
                i2 = strokeColor;
            else
                i2 = strokeColors[j];
            if(flag)
                f = strokeWeight;
            else
                f = strokeWeights[j];
            f *= transformScale();
            aword1 = tess;
            j2 = l2 + 1;
            aword1.setLineVertex(l2, strokeVertices, j, i, i2, -f / 2.0F);
            aword1 = tess.lineIndices;
            l2 = k2 + 1;
            aword1[k2] = (short)(k1 + 2);
            aword1 = tess.lineIndices;
            k2 = l2 + 1;
            aword1[l2] = (short)(k1 + 2);
            aword1 = tess.lineIndices;
            l2 = k2 + 1;
            aword1[k2] = (short)(k1 + 1);
            aword1 = tess;
            k2 = j2 + 1;
            aword1.setLineVertex(j2, strokeVertices, j, i, i2, f / 2.0F);
            aword1 = tess.lineIndices;
            j = l2 + 1;
            aword1[l2] = (short)(k1 + 3);
            indexcache.incCounts(i1, 6, 4);
            if(aword0 != null)
            {
                if(-1 < aword0[0] && -1 < aword0[1])
                    if(j1 != 0)
                    {
                        if(-1 < k && -1 < l)
                        {
                            TessGeometry tessgeometry1;
                            short aword2[];
                            if(flag)
                                i = strokeColor;
                            else
                                i = strokeColors[k];
                            if(flag)
                                f = strokeWeight;
                            else
                                f = strokeWeights[k];
                            f *= transformScale();
                            tessgeometry1 = tess;
                            j1 = k2 + 1;
                            tessgeometry1.setLineVertex(k2, strokeVertices, l, i);
                            tess.setLineVertex(j1, strokeVertices, l, k, i, -f / 2.0F);
                            tess.setLineVertex(j1 + 1, strokeVertices, l, k, i, f / 2.0F);
                            aword2 = tess.lineIndices;
                            i = j + 1;
                            aword2[j] = (short)(k1 + 4);
                            aword2 = tess.lineIndices;
                            j = i + 1;
                            aword2[i] = (short)(k1 + 5);
                            aword2 = tess.lineIndices;
                            i = j + 1;
                            aword2[j] = (short)(k1 + 0);
                            aword2 = tess.lineIndices;
                            j = i + 1;
                            aword2[i] = (short)(k1 + 4);
                            tess.lineIndices[j] = (short)(k1 + 6);
                            tess.lineIndices[j + 1] = (short)(k1 + 1);
                            indexcache.incCounts(i1, 6, 3);
                        }
                    } else
                    {
                        tess.setLineVertex(k2, strokeVertices, i, l1);
                        short aword3[] = tess.lineIndices;
                        i = j + 1;
                        aword3[j] = (short)(k1 + 4);
                        aword3 = tess.lineIndices;
                        j = i + 1;
                        aword3[i] = aword0[0];
                        aword3 = tess.lineIndices;
                        i = j + 1;
                        aword3[j] = (short)(k1 + 0);
                        aword3 = tess.lineIndices;
                        j = i + 1;
                        aword3[i] = (short)(k1 + 4);
                        tess.lineIndices[j] = aword0[1];
                        tess.lineIndices[j + 1] = (short)(k1 + 1);
                        indexcache.incCounts(i1, 6, 1);
                    }
                aword0[0] = (short)(k1 + 2);
                aword0[1] = (short)(k1 + 3);
            }
            return i1;
        }

        void addQuadraticVertex(int i)
        {
            pg.0.addPolyVertices = i;
            pg.bezierInitCheck();
            pg.bezierVertexCheck(20, i);
            PMatrix3D pmatrix3d = 
// JavaClassFileOutputException: get_constant: invalid tag

        void addStrokeVertex(float f, float f1, float f2, int i, float f3)
        {
            int j = pathVertexCount;
            if(beginPath + 1 < j)
            {
                InGeometry ingeometry = in;
                float af[];
                boolean flag;
                int k;
                if(beginPath == j - 2)
                    flag = true;
                else
                    flag = false;
                ingeometry.addEdge(j - 2, j - 1, flag, false);
            }
            if(pathVertexCount == pathVertices.length / 3)
            {
                k = pathVertexCount << 1;
                af = new float[k * 3];
                PApplet.arrayCopy(pathVertices, 0, af, 0, pathVertexCount * 3);
                pathVertices = af;
                af = new int[k];
                PApplet.arrayCopy(pathColors, 0, af, 0, pathVertexCount);
                pathColors = af;
                af = new float[k];
                PApplet.arrayCopy(pathWeights, 0, af, 0, pathVertexCount);
                pathWeights = af;
            }
            pathVertices[j * 3 + 0] = f;
            pathVertices[j * 3 + 1] = f1;
            pathVertices[j * 3 + 2] = f2;
            pathColors[j] = i;
            pathWeights[j] = f3;
            pathVertexCount = pathVertexCount + 1;
        }

        void addVertex(int i)
        {
            pg.0.addPolyVertices = i;
            float f = in.vertices[i * 3 + 0];
            float f1 = in.vertices[i * 3 + 1];
            float f2 = in.vertices[i * 3 + 2];
            if(fill)
            {
                double ad[] = collectVertexAttributes(i);
                ad[0] = f;
                ad[1] = f1;
                ad[2] = f2;
                gluTess.addVertex(ad);
            }
            if(stroke)
                addStrokeVertex(f, f1, f2, in.strokeColors[i], in.strokeWeights[i]);
        }

        void beginNoTex()
        {
            newTexImage = null;
            setFirstTexIndex(tess.polyIndexCount, tess.polyIndexCache.size - 1);
        }

        void beginPolygonStroke()
        {
            pathVertexCount = 0;
            if(pathVertices == null)
            {
                pathVertices = new float[PGL.DEFAULT_IN_VERTICES * 3];
                pathColors = new int[PGL.DEFAULT_IN_VERTICES];
                pathWeights = new float[PGL.DEFAULT_IN_VERTICES];
            }
        }

        void beginStrokePath()
        {
            beginPath = pathVertexCount;
        }

        void beginTex()
        {
            setFirstTexIndex(tess.polyIndexCount, tess.polyIndexCache.size - 1);
        }

        boolean clamp2D()
        {
            boolean flag;
            if(is2D && tess.renderMode == 0 && PGraphicsOpenGL.zero(pg.modelview.m01) && PGraphicsOpenGL.zero(pg.modelview.m10))
                flag = true;
            else
                flag = false;
            return flag;
        }

        boolean clampEdges2D()
        {
            boolean flag = clamp2D();
            if(!flag) goto _L2; else goto _L1
_L1:
            int i = 0;
_L7:
            boolean flag1 = flag;
            if(i > in.edgeCount - 1) goto _L4; else goto _L3
_L3:
            int ai[] = in.edges[i];
            if(ai[2] != -1) goto _L6; else goto _L5
_L5:
            i++;
              goto _L7
_L6:
            int j = ai[0];
            int k = ai[1];
            flag1 = segmentIsAxisAligned(strokeVertices, j, k);
            flag = flag1;
            if(flag1) goto _L5; else goto _L4
_L4:
            return flag1;
_L2:
            flag1 = flag;
            if(true) goto _L4; else goto _L8
_L8:
        }

        boolean clampLineLoop2D(int i)
        {
            boolean flag;
            boolean flag1;
            flag = clamp2D();
            flag1 = flag;
            if(!flag) goto _L2; else goto _L1
_L1:
            int j;
            j = 0;
            flag1 = flag;
_L6:
            if(j >= i) goto _L2; else goto _L3
_L3:
            flag1 = segmentIsAxisAligned(0, j + 1);
            if(flag1) goto _L4; else goto _L2
_L2:
            return flag1;
_L4:
            j++;
            if(true) goto _L6; else goto _L5
_L5:
        }

        boolean clampLinePath()
        {
            boolean flag;
            if(clamp2D() && strokeCap == 4 && strokeJoin == 32 && !subPixelStroke(strokeWeight))
                flag = true;
            else
                flag = false;
            return flag;
        }

        boolean clampLineStrip2D(int i)
        {
            boolean flag;
            boolean flag1;
            flag = clamp2D();
            flag1 = flag;
            if(!flag) goto _L2; else goto _L1
_L1:
            int j;
            j = 0;
            flag1 = flag;
_L6:
            if(j >= i) goto _L2; else goto _L3
_L3:
            flag1 = segmentIsAxisAligned(0, j + 1);
            if(flag1) goto _L4; else goto _L2
_L2:
            return flag1;
_L4:
            j++;
            if(true) goto _L6; else goto _L5
_L5:
        }

        boolean clampLines2D(int i)
        {
            boolean flag;
            boolean flag1;
            flag = clamp2D();
            flag1 = flag;
            if(!flag) goto _L2; else goto _L1
_L1:
            int j;
            j = 0;
            flag1 = flag;
_L6:
            if(j >= i) goto _L2; else goto _L3
_L3:
            flag1 = segmentIsAxisAligned(j * 2 + 0, j * 2 + 1);
            if(flag1) goto _L4; else goto _L2
_L2:
            return flag1;
_L4:
            j++;
            if(true) goto _L6; else goto _L5
_L5:
        }

        boolean clampPolygon()
        {
            return false;
        }

        boolean clampQuadStrip(int i)
        {
            boolean flag;
            boolean flag1;
            flag = clamp2D();
            flag1 = flag;
            if(!flag) goto _L2; else goto _L1
_L1:
            int j;
            j = 1;
            flag1 = flag;
_L6:
            if(j >= i + 1) goto _L2; else goto _L3
_L3:
            int k = (j - 1) * 2 + 1;
            int l = j * 2 + 1;
            if(segmentIsAxisAligned((j - 1) * 2, k) && segmentIsAxisAligned(k, l) && segmentIsAxisAligned(l, j * 2))
                flag1 = true;
            else
                flag1 = false;
            if(flag1) goto _L4; else goto _L2
_L2:
            return flag1;
_L4:
            j++;
            if(true) goto _L6; else goto _L5
_L5:
        }

        boolean clampQuads(int i)
        {
            boolean flag;
            boolean flag1;
            flag = clamp2D();
            flag1 = flag;
            if(!flag) goto _L2; else goto _L1
_L1:
            int j;
            j = 0;
            flag1 = flag;
_L6:
            if(j >= i) goto _L2; else goto _L3
_L3:
            int k = j * 4 + 1;
            int l = j * 4 + 2;
            if(segmentIsAxisAligned(j * 4 + 0, k) && segmentIsAxisAligned(k, l) && segmentIsAxisAligned(l, j * 4 + 3))
                flag1 = true;
            else
                flag1 = false;
            if(flag1) goto _L4; else goto _L2
_L2:
            return flag1;
_L4:
            j++;
            if(true) goto _L6; else goto _L5
_L5:
        }

        boolean clampSquarePoints2D()
        {
            return clamp2D();
        }

        boolean clampTriangleFan()
        {
            boolean flag = clamp2D();
            if(!flag) goto _L2; else goto _L1
_L1:
            int i = 1;
_L6:
            if(i >= in.vertexCount - 1) goto _L2; else goto _L3
_L3:
            int j = i + 1;
            int k;
            int l;
            if(segmentIsAxisAligned(0, i))
                k = 1;
            else
                k = 0;
            l = k;
            if(segmentIsAxisAligned(0, j))
                l = k + 1;
            k = l;
            if(segmentIsAxisAligned(i, j))
                k = l + 1;
            if(1 < k)
                flag = true;
            else
                flag = false;
            if(flag) goto _L4; else goto _L2
_L2:
            return flag;
_L4:
            i++;
            if(true) goto _L6; else goto _L5
_L5:
        }

        boolean clampTriangleStrip()
        {
            boolean flag = clamp2D();
            if(!flag) goto _L2; else goto _L1
_L1:
            int i = 1;
_L6:
            if(i >= in.vertexCount - 1) goto _L2; else goto _L3
_L3:
            int j;
            int k;
            int l;
            int i1;
            if(i % 2 == 0)
            {
                j = i - 1;
                k = i + 1;
            } else
            {
                j = i + 1;
                k = i - 1;
            }
            if(segmentIsAxisAligned(i, j))
                l = 1;
            else
                l = 0;
            i1 = l;
            if(segmentIsAxisAligned(i, k))
                i1 = l + 1;
            l = i1;
            if(segmentIsAxisAligned(j, k))
                l = i1 + 1;
            if(1 < l)
                flag = true;
            else
                flag = false;
            if(flag) goto _L4; else goto _L2
_L2:
            return flag;
_L4:
            i++;
            if(true) goto _L6; else goto _L5
_L5:
        }

        boolean clampTriangles()
        {
            boolean flag;
            boolean flag1;
            flag = clamp2D();
            flag1 = flag;
            if(!flag) goto _L2; else goto _L1
_L1:
            int i;
            int j;
            i = in.vertexCount / 3;
            j = 0;
            flag1 = flag;
_L6:
            if(j >= i) goto _L2; else goto _L3
_L3:
            int k = j * 3 + 0;
            int l = j * 3 + 1;
            int i1 = j * 3 + 2;
            int j1;
            int k1;
            if(segmentIsAxisAligned(k, l))
                j1 = 1;
            else
                j1 = 0;
            k1 = j1;
            if(segmentIsAxisAligned(k, i1))
                k1 = j1 + 1;
            j1 = k1;
            if(segmentIsAxisAligned(l, i1))
                j1 = k1 + 1;
            if(1 < j1)
                flag1 = true;
            else
                flag1 = false;
            if(flag1) goto _L4; else goto _L2
_L2:
            return flag1;
_L4:
            j++;
            if(true) goto _L6; else goto _L5
_L5:
        }

        boolean clampTriangles(int ai[])
        {
            boolean flag;
            boolean flag1;
            flag = clamp2D();
            flag1 = flag;
            if(!flag) goto _L2; else goto _L1
_L1:
            int i;
            int j;
            i = ai.length;
            j = 0;
            flag1 = flag;
_L6:
            if(j >= i) goto _L2; else goto _L3
_L3:
            int k = ai[j * 3 + 0];
            int l = ai[j * 3 + 1];
            int i1 = ai[j * 3 + 2];
            int j1;
            int k1;
            if(segmentIsAxisAligned(k, l))
                j1 = 1;
            else
                j1 = 0;
            k1 = j1;
            if(segmentIsAxisAligned(k, i1))
                k1 = j1 + 1;
            j1 = k1;
            if(segmentIsAxisAligned(l, i1))
                j1 = k1 + 1;
            if(1 < j1)
                flag1 = true;
            else
                flag1 = false;
            if(flag1) goto _L4; else goto _L2
_L2:
            return flag1;
_L4:
            j++;
            if(true) goto _L6; else goto _L5
_L5:
        }

        double[] collectVertexAttributes(int i)
        {
            double ad[] = in.getAttribVector(i);
            double ad1[] = new double[ad.length + 25];
            int j = in.colors[i];
            ad1[3] = j >> 24 & 0xff;
            ad1[4] = j >> 16 & 0xff;
            ad1[5] = j >> 8 & 0xff;
            ad1[6] = j >> 0 & 0xff;
            ad1[7] = in.normals[i * 3 + 0];
            ad1[8] = in.normals[i * 3 + 1];
            ad1[9] = in.normals[i * 3 + 2];
            ad1[10] = in.texcoords[i * 2 + 0];
            ad1[11] = in.texcoords[i * 2 + 1];
            j = in.ambient[i];
            ad1[12] = j >> 24 & 0xff;
            ad1[13] = j >> 16 & 0xff;
            ad1[14] = j >> 8 & 0xff;
            ad1[15] = j >> 0 & 0xff;
            j = in.specular[i];
            ad1[16] = j >> 24 & 0xff;
            ad1[17] = j >> 16 & 0xff;
            ad1[18] = j >> 8 & 0xff;
            ad1[19] = j >> 0 & 0xff;
            j = in.emissive[i];
            ad1[20] = j >> 24 & 0xff;
            ad1[21] = j >> 16 & 0xff;
            ad1[22] = j >> 8 & 0xff;
            ad1[23] = j >> 0 & 0xff;
            ad1[24] = in.shininess[i];
            System.arraycopy(ad, 0, ad1, 25, ad.length);
            return ad1;
        }

        int dupIndexPos(int i)
        {
            int j = 0;
_L3:
            if(j >= dupCount)
                break MISSING_BLOCK_LABEL_28;
            if(dupIndices[j] != i) goto _L2; else goto _L1
_L1:
            return j;
_L2:
            j++;
              goto _L3
            j = 0;
              goto _L1
        }

        void endNoTex()
        {
            setLastTexIndex(tess.lastPolyIndex, tess.polyIndexCache.size - 1);
        }

        void endPolygonStroke()
        {
        }

        void endStrokePath(boolean flag)
        {
            boolean flag1 = true;
            int i = pathVertexCount;
            if(beginPath + 1 < i)
            {
                boolean flag2;
                boolean flag3;
                if(beginPath == i - 2)
                    flag2 = true;
                else
                    flag2 = false;
                flag3 = flag1;
                if(!flag2)
                    if(!flag)
                        flag3 = flag1;
                    else
                        flag3 = false;
                in.addEdge(i - 2, i - 1, flag2, flag3);
                if(!flag3)
                {
                    in.addEdge(i - 1, beginPath, false, false);
                    in.closeEdge(i - 1, beginPath);
                }
            }
        }

        void endTex()
        {
            setLastTexIndex(tess.lastPolyIndex, tess.polyIndexCache.size - 1);
        }

        void expandRawIndices(int i)
        {
            int ai[] = new int[i];
            PApplet.arrayCopy(rawIndices, 0, ai, 0, rawSize);
            rawIndices = ai;
        }

        void initGluTess()
        {
            if(gluTess == null)
            {
                callback = new TessellatorCallback(tess.polyAttribs);
                gluTess = pg.pgl.createTessellator(callback);
            }
        }

        boolean noCapsJoins()
        {
            boolean flag;
            if(tess.renderMode == 0 && transformScale() * strokeWeight < PGL.MIN_CAPS_JOINS_WEIGHT)
                flag = true;
            else
                flag = false;
            return flag;
        }

        boolean noCapsJoins(int i)
        {
            boolean flag;
            flag = true;
            break MISSING_BLOCK_LABEL_2;
            if(accurate2DStrokes && PGL.MAX_CAPS_JOINS_LENGTH > i)
                flag = noCapsJoins();
            return flag;
        }

        void resetCurveVertexCount()
        {
            pg.0.addPolyVertices = <returnValue>;
        }

        boolean segmentIsAxisAligned(int i, int j)
        {
            boolean flag;
            if(PGraphicsOpenGL.zero(in.vertices[i * 3 + 0] - in.vertices[j * 3 + 0]) || PGraphicsOpenGL.zero(in.vertices[i * 3 + 1] - in.vertices[j * 3 + 1]))
                flag = true;
            else
                flag = false;
            return flag;
        }

        boolean segmentIsAxisAligned(float af[], int i, int j)
        {
            boolean flag;
            if(PGraphicsOpenGL.zero(af[i * 3 + 0] - af[j * 3 + 0]) || PGraphicsOpenGL.zero(af[i * 3 + 1] - af[j * 3 + 1]))
                flag = true;
            else
                flag = false;
            return flag;
        }

        void set3D(boolean flag)
        {
            if(flag)
            {
                is2D = false;
                is3D = true;
            } else
            {
                is2D = true;
                is3D = false;
            }
        }

        void setAccurate2DStrokes(boolean flag)
        {
            accurate2DStrokes = flag;
        }

        void setFill(boolean flag)
        {
            fill = flag;
        }

        void setFirstTexIndex(int i, int j)
        {
            if(texCache != null)
            {
                firstTexIndex = i;
                firstTexCache = PApplet.max(0, j);
            }
        }

        void setInGeometry(InGeometry ingeometry)
        {
            in = ingeometry;
            firstPolyIndexCache = -1;
            lastPolyIndexCache = -1;
            firstLineIndexCache = -1;
            lastLineIndexCache = -1;
            firstPointIndexCache = -1;
            lastPointIndexCache = -1;
        }

        void setLastTexIndex(int i, int j)
        {
            if(texCache != null)
            {
                if(prevTexImage != newTexImage || texCache.size == 0)
                    texCache.addTexture(newTexImage, firstTexIndex, firstTexCache, i, j);
                else
                    texCache.setLastIndex(i, j);
                prevTexImage = newTexImage;
            }
        }

        void setRawSize(int i)
        {
            int j = rawIndices.length;
            if(j < i)
                expandRawIndices(PGraphicsOpenGL.expandArraySize(j, i));
            rawSize = i;
        }

        protected void setRenderer(PGraphicsOpenGL pgraphicsopengl)
        {
            pg = pgraphicsopengl;
        }

        void setStroke(boolean flag)
        {
            stroke = flag;
        }

        void setStrokeCap(int i)
        {
            strokeCap = i;
        }

        void setStrokeColor(int i)
        {
            strokeColor = PGL.javaToNativeARGB(i);
        }

        void setStrokeJoin(int i)
        {
            strokeJoin = i;
        }

        void setStrokeWeight(float f)
        {
            strokeWeight = f;
        }

        void setTessGeometry(TessGeometry tessgeometry)
        {
            tess = tessgeometry;
        }

        void setTexCache(TexCache texcache, PImage pimage)
        {
            texCache = texcache;
            newTexImage = pimage;
        }

        void setTransform(PMatrix pmatrix)
        {
            transform = pmatrix;
            transformScale = -1F;
        }

        void splitRawIndices(boolean flag)
        {
            tess.polyIndexCheck(rawSize);
            int i = tess.firstPolyIndex;
            int j = 0;
            int k = 0;
            dupCount = 0;
            IndexCache indexcache = tess.polyIndexCache;
            int l;
            int i1;
            int j1;
            int k1;
            int l1;
            int i2;
            if(in.renderMode == 1)
                l = indexcache.addNew();
            else
                l = indexcache.getLast();
            firstPolyIndexCache = l;
            i1 = rawSize / 3;
            j1 = 0;
            k1 = -1;
            l1 = 0;
            i2 = 0;
            while(j1 < i1) 
            {
                int j2 = l;
                if(l == -1)
                    j2 = indexcache.addNew();
                int k2 = rawIndices[j1 * 3 + 0];
                int l2 = rawIndices[j1 * 3 + 1];
                int i3 = rawIndices[j1 * 3 + 2];
                l = k2 - k;
                int j3 = l2 - k;
                int k3 = i3 - k;
                int l3 = indexcache.vertexCount[j2];
                if(l < 0)
                    addDupIndex(l);
                else
                    l += l3;
                if(j3 < 0)
                    addDupIndex(j3);
                else
                    j3 += l3;
                if(k3 < 0)
                    addDupIndex(k3);
                else
                    k3 += l3;
                tess.polyIndices[j1 * 3 + i + 0] = (short)l;
                tess.polyIndices[j1 * 3 + i + 1] = (short)j3;
                tess.polyIndices[j1 * 3 + i + 2] = (short)k3;
                l3 = j1 * 3 + 2;
                l1 = PApplet.max(l1, PApplet.max(k2, l2, i3));
                i2 = PApplet.min(i2, PApplet.min(k2, l2, i3));
                j3 = PApplet.max(k1, PApplet.max(l, j3, k3));
                if(PGL.MAX_VERTEX_INDEX1 - 3 <= dupCount + j3 && dupCount + j3 < PGL.MAX_VERTEX_INDEX1 || j1 == i1 - 1)
                {
                    k1 = 0;
                    if(dupCount > 0)
                    {
                        for(l = j; l <= l3; l++)
                        {
                            i2 = tess.polyIndices[i + l];
                            if(i2 < 0)
                                tess.polyIndices[i + l] = (short)(dupIndexPos(i2) + (j3 + 1));
                        }

                        l = k1;
                        if(k <= l1)
                        {
                            tess.addPolyVertices(in, k, l1, flag);
                            l = (l1 - k) + 1;
                        }
                        k1 = 0;
                        do
                        {
                            i2 = l;
                            if(k1 >= dupCount)
                                break;
                            tess.addPolyVertex(in, dupIndices[k1] + k, flag);
                            k1++;
                        } while(true);
                    } else
                    {
                        tess.addPolyVertices(in, i2, l1, flag);
                        i2 = (l1 - i2) + 1;
                    }
                    indexcache.incCounts(j2, (l3 - j) + 1, i2 + dupCount);
                    lastPolyIndexCache = j2;
                    j2 = -1;
                    k1 = -1;
                    k = l1 + 1;
                    if(dupIndices != null)
                        Arrays.fill(dupIndices, 0, dupCount, 0);
                    dupCount = 0;
                    j = l3 + 1;
                    l = k;
                } else
                {
                    k1 = j3;
                    l = i2;
                }
                j1++;
                i2 = l;
                l = j2;
            }
        }

        boolean subPixelStroke(float f)
        {
            f = transformScale() * f;
            boolean flag;
            if(PApplet.abs(f - (float)(int)f) > 0.0F)
                flag = true;
            else
                flag = false;
            return flag;
        }

        void tessellateEdges()
        {
            if(stroke && in.edgeCount != 0) goto _L2; else goto _L1
_L1:
            return;
_L2:
            strokeVertices = in.vertices;
            strokeColors = in.strokeColors;
            strokeWeights = in.strokeWeights;
            if(is3D)
                tessellateEdges3D();
            else
            if(is2D)
            {
                beginNoTex();
                tessellateEdges2D();
                endNoTex();
            }
            if(true) goto _L1; else goto _L3
_L3:
        }

        void tessellateEdges2D()
        {
            int i = in.getNumEdgeVertices(false);
            if(!noCapsJoins(i)) goto _L2; else goto _L1
_L1:
            int j = in.getNumEdgeIndices(false);
            tess.polyVertexCheck(i);
            tess.polyIndexCheck(j);
            boolean flag;
            boolean flag1;
            if(in.renderMode == 1)
                j = tess.polyIndexCache.addNew();
            else
                j = tess.polyIndexCache.getLast();
            firstLineIndexCache = j;
            if(firstPolyIndexCache == -1)
                firstPolyIndexCache = j;
            flag = clampEdges2D();
            flag1 = false;
            i = j;
            j = ((flag1) ? 1 : 0);
            while(j <= in.edgeCount - 1) 
            {
                int ai[] = in.edges[j];
                if(ai[2] != -1)
                    i = addLineSegment2D(ai[0], ai[1], i, false, flag);
                j++;
            }
            lastPolyIndexCache = i;
            lastLineIndexCache = i;
_L13:
            return;
_L2:
            int k;
            LinePath linepath;
            linepath = new LinePath(1);
            k = 0;
_L10:
            int l;
            int ai1[];
            if(k > in.edgeCount - 1)
                break MISSING_BLOCK_LABEL_544;
            ai1 = in.edges[k];
            l = ai1[0];
            i = ai1[1];
            ai1[2];
            JVM INSTR tableswitch -1 3: default 264
        //                       -1 536
        //                       0 270
        //                       1 304
        //                       2 372
        //                       3 437;
               goto _L3 _L4 _L5 _L6 _L7 _L8
_L4:
            break MISSING_BLOCK_LABEL_536;
_L3:
            break; /* Loop/switch isn't completed */
_L5:
            break; /* Loop/switch isn't completed */
_L11:
            k++;
            if(true) goto _L10; else goto _L9
_L9:
            linepath.lineTo(strokeVertices[i * 3 + 0], strokeVertices[i * 3 + 1], strokeColors[i]);
              goto _L11
_L6:
            linepath.moveTo(strokeVertices[l * 3 + 0], strokeVertices[l * 3 + 1], strokeColors[l]);
            linepath.lineTo(strokeVertices[i * 3 + 0], strokeVertices[i * 3 + 1], strokeColors[i]);
              goto _L11
_L7:
            linepath.lineTo(strokeVertices[i * 3 + 0], strokeVertices[i * 3 + 1], strokeColors[i]);
            linepath.moveTo(strokeVertices[i * 3 + 0], strokeVertices[i * 3 + 1], strokeColors[i]);
              goto _L11
_L8:
            linepath.moveTo(strokeVertices[l * 3 + 0], strokeVertices[l * 3 + 1], strokeColors[l]);
            linepath.lineTo(strokeVertices[i * 3 + 0], strokeVertices[i * 3 + 1], strokeColors[i]);
            linepath.moveTo(strokeVertices[i * 3 + 0], strokeVertices[i * 3 + 1], strokeColors[i]);
              goto _L11
            linepath.closePath();
              goto _L11
            tessellateLinePath(linepath);
            if(true) goto _L13; else goto _L12
_L12:
        }

        void tessellateEdges3D()
        {
            boolean flag;
            int i;
            int k;
            int l;
            int i1;
            int j1;
            short aword0[];
            int k1;
            int l1;
            int ai[];
            int i2;
            int j2;
            int k2;
            if(!noCapsJoins())
                flag = true;
            else
                flag = false;
            i = in.getNumEdgeVertices(flag);
            k = in.getNumEdgeIndices(flag);
            l = tess.lineVertexCount;
            i1 = tess.lineIndexCount;
            tess.lineVertexCheck(i);
            tess.lineIndexCheck(k);
            if(in.renderMode == 1)
                k = tess.lineIndexCache.addNew();
            else
                k = tess.lineIndexCache.getLast();
            firstLineIndexCache = k;
            j1 = 0;
            aword0 = new short[2];
            short[] _tmp = aword0;
            aword0[0] = -1;
            aword0[1] = -1;
            k1 = -1;
            l1 = -1;
            ai = new int[2];
            int[] _tmp1 = ai;
            ai[0] = 0;
            ai[1] = 0;
            tess.lineIndexCache.setCounter(ai);
            i2 = 0;
            j2 = 0;
            k2 = k;
            while(i2 <= in.edgeCount - 1) 
            {
                int ai1[] = in.edges[i2];
                int j = ai1[0];
                int l2 = ai1[1];
                if(flag)
                {
                    if(ai1[2] == -1)
                        k = addBevel3D(j2, j1, k1, l1, k2, aword0, false);
                    else
                        k = addLineSegment3D(j, l2, k1, l1, k2, aword0, false);
                } else
                {
                    k = k2;
                    if(ai1[2] != -1)
                        k = addLineSegment3D(j, l2, k1, l1, k2, null, false);
                }
                if(ai1[2] == 1)
                {
                    j1 = l2;
                    j2 = j;
                }
                if(ai1[2] == 2 || ai1[2] == 3 || ai1[2] == -1)
                {
                    aword0[1] = (short)-1;
                    aword0[0] = (short)-1;
                    j = -1;
                    l2 = -1;
                } else
                {
                    k2 = l2;
                    l2 = j;
                    j = k2;
                }
                i2++;
                k1 = l2;
                l1 = j;
                k2 = k;
            }
            tess.lineIndexCache.setCounter(null);
            tess.lineIndexCount = ai[0] + i1;
            tess.lineVertexCount = ai[1] + l;
            lastLineIndexCache = k2;
        }

        void tessellateLineLoop()
        {
            int i = in.vertexCount;
            if(!stroke || 2 > i) goto _L2; else goto _L1
_L1:
            strokeVertices = in.vertices;
            strokeColors = in.strokeColors;
            strokeWeights = in.strokeWeights;
            updateTex();
            if(!is3D) goto _L4; else goto _L3
_L3:
            tessellateLineLoop3D(i);
_L2:
            return;
_L4:
            if(is2D)
            {
                beginNoTex();
                tessellateLineLoop2D(i);
                endNoTex();
            }
            if(true) goto _L2; else goto _L5
_L5:
        }

        void tessellateLineLoop2D(int i)
        {
            int j = 0;
            int k = i * 4;
            if(noCapsJoins(k))
            {
                tess.polyVertexCheck(k);
                tess.polyIndexCheck(i * 2 * 3);
                boolean flag;
                boolean flag1;
                int j1;
                if(in.renderMode == 1)
                    j = tess.polyIndexCache.addNew();
                else
                    j = tess.polyIndexCache.getLast();
                firstLineIndexCache = j;
                if(firstPolyIndexCache == -1)
                    firstPolyIndexCache = j;
                flag = clampLineLoop2D(i);
                flag1 = false;
                j1 = 0;
                k = j;
                for(j = ((flag1) ? 1 : 0); j < i - 1;)
                {
                    int i1 = j + 1;
                    k = addLineSegment2D(j1, i1, k, false, flag);
                    j++;
                    j1 = i1;
                }

                i = addLineSegment2D(0, in.vertexCount - 1, k, false, flag);
                lastPolyIndexCache = i;
                lastLineIndexCache = i;
            } else
            {
                LinePath linepath = new LinePath(1);
                linepath.moveTo(in.vertices[0], in.vertices[1], in.strokeColors[0]);
                for(; j < i - 1; j++)
                {
                    int l = j + 1;
                    linepath.lineTo(in.vertices[l * 3 + 0], in.vertices[l * 3 + 1], in.strokeColors[l]);
                }

                linepath.closePath();
                tessellateLinePath(linepath);
            }
        }

        void tessellateLineLoop3D(int i)
        {
            int j;
            int k;
            int l;
            int i1;
            short aword0[];
            int ai[];
            int j1;
            boolean flag;
            int k1;
            int l1;
            if(noCapsJoins())
                j = 0;
            else
                j = i;
            k = tess.lineVertexCount;
            l = tess.lineIndexCount;
            tess.lineVertexCheck(i * 4 + j * 3);
            tess.lineIndexCheck(i * 2 * 3 + j * 2 * 3);
            if(in.renderMode == 1)
                i1 = tess.lineIndexCache.addNew();
            else
                i1 = tess.lineIndexCache.getLast();
            firstLineIndexCache = i1;
            aword0 = new short[2];
            short[] _tmp = aword0;
            aword0[0] = -1;
            aword0[1] = -1;
            ai = new int[2];
            int[] _tmp1 = ai;
            ai[0] = 0;
            ai[1] = 0;
            tess.lineIndexCache.setCounter(ai);
            j1 = 0;
            flag = false;
            k1 = -1;
            l1 = i1;
            i1 = ((flag) ? 1 : 0);
            while(j1 < i - 1) 
            {
                k1 = j1 + 1;
                if(j > 0)
                    i1 = addLineSegment3D(i1, k1, k1 - 2, k1 - 1, l1, aword0, false);
                else
                    i1 = addLineSegment3D(i1, k1, k1 - 2, k1 - 1, l1, null, false);
                j1++;
                l1 = i1;
                i1 = k1;
            }
            i1 = addLineSegment3D(in.vertexCount - 1, 0, k1 - 2, k1 - 1, l1, aword0, false);
            i = i1;
            if(j > 0)
                i = addBevel3D(0, 1, in.vertexCount - 1, 0, i1, aword0, false);
            tess.lineIndexCache.setCounter(null);
            tess.lineIndexCount = ai[0] + l;
            tess.lineVertexCount = ai[1] + k;
            lastLineIndexCache = i;
        }

        public void tessellateLinePath(LinePath linepath)
        {
            initGluTess();
            boolean flag = clampLinePath();
            Object obj = callback;
            boolean flag1;
            int i;
            int j;
            if(in.renderMode == 1)
                flag1 = true;
            else
                flag1 = false;
            ((TessellatorCallback) (obj)).init(flag1, true, false, flag);
            if(strokeCap == 2)
                i = 1;
            else
            if(strokeCap == 4)
                i = 2;
            else
                i = 0;
            if(strokeJoin == 2)
                j = 1;
            else
            if(strokeJoin == 32)
                j = 2;
            else
                j = 0;
            obj = LinePath.createStrokedPath(linepath, strokeWeight, i, j);
            gluTess.beginPolygon();
            linepath = new float[6];
            obj = ((LinePath) (obj)).getPathIterator();
            ((LinePath.PathIterator) (obj)).getWindingRule();
            JVM INSTR tableswitch 0 1: default 116
        //                       0 210
        //                       1 225;
               goto _L1 _L2 _L3
_L1:
            if(((LinePath.PathIterator) (obj)).isDone()) goto _L5; else goto _L4
_L4:
            ((LinePath.PathIterator) (obj)).currentSegment(linepath);
            JVM INSTR tableswitch 0 2: default 156
        //                       0 240
        //                       1 249
        //                       2 426;
               goto _L6 _L7 _L8 _L9
_L6:
            ((LinePath.PathIterator) (obj)).next();
            continue; /* Loop/switch isn't completed */
_L2:
            gluTess.setWindingRule(PGL.TESS_WINDING_ODD);
            continue; /* Loop/switch isn't completed */
_L3:
            gluTess.setWindingRule(PGL.TESS_WINDING_NONZERO);
            continue; /* Loop/switch isn't completed */
_L7:
            gluTess.beginContour();
_L8:
            double d = linepath[0];
            double d1 = linepath[1];
            double d2 = linepath[2];
            double d3 = linepath[3];
            double d4 = linepath[4];
            double d5 = linepath[5];
            gluTess.addVertex(new double[] {
                d, d1, 0.0D, d2, d3, d4, d5, 0.0D, 0.0D, 1.0D, 
                0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 
                0.0D, 0.0D, 0.0D, 0.0D, 0.0D
            });
            continue; /* Loop/switch isn't completed */
_L9:
            gluTess.endContour();
            if(true) goto _L6; else goto _L5
_L5:
            gluTess.endPolygon();
            return;
            if(true) goto _L1; else goto _L10
_L10:
        }

        void tessellateLineStrip()
        {
            int i = in.vertexCount;
            if(!stroke || 2 > i) goto _L2; else goto _L1
_L1:
            strokeVertices = in.vertices;
            strokeColors = in.strokeColors;
            strokeWeights = in.strokeWeights;
            updateTex();
            i--;
            if(!is3D) goto _L4; else goto _L3
_L3:
            tessellateLineStrip3D(i);
_L2:
            return;
_L4:
            if(is2D)
            {
                beginNoTex();
                tessellateLineStrip2D(i);
                endNoTex();
            }
            if(true) goto _L2; else goto _L5
_L5:
        }

        void tessellateLineStrip2D(int i)
        {
            int j = 0;
            int k = i * 4;
            if(noCapsJoins(k))
            {
                tess.polyVertexCheck(k);
                tess.polyIndexCheck(i * 2 * 3);
                boolean flag;
                boolean flag1;
                int j1;
                if(in.renderMode == 1)
                    j = tess.polyIndexCache.addNew();
                else
                    j = tess.polyIndexCache.getLast();
                firstLineIndexCache = j;
                if(firstPolyIndexCache == -1)
                    firstPolyIndexCache = j;
                flag = clampLineStrip2D(i);
                flag1 = false;
                k = 0;
                j1 = j;
                for(j = ((flag1) ? 1 : 0); j < i;)
                {
                    int i1 = j + 1;
                    j1 = addLineSegment2D(k, i1, j1, false, flag);
                    j++;
                    k = i1;
                }

                lastPolyIndexCache = j1;
                lastLineIndexCache = j1;
            } else
            {
                LinePath linepath = new LinePath(1);
                linepath.moveTo(in.vertices[0], in.vertices[1], in.strokeColors[0]);
                for(; j < i; j++)
                {
                    int l = j + 1;
                    linepath.lineTo(in.vertices[l * 3 + 0], in.vertices[l * 3 + 1], in.strokeColors[l]);
                }

                tessellateLinePath(linepath);
            }
        }

        void tessellateLineStrip3D(int i)
        {
            int j;
            int k;
            int l;
            int i1;
            int ai[];
            int j1;
            boolean flag;
            int l1;
            if(noCapsJoins())
                j = 0;
            else
                j = i - 1;
            k = tess.lineVertexCount;
            l = tess.lineIndexCount;
            tess.lineVertexCheck(i * 4 + j * 3);
            tess.lineIndexCheck(i * 2 * 3 + j * 2 * 3);
            if(in.renderMode == 1)
                i1 = tess.lineIndexCache.addNew();
            else
                i1 = tess.lineIndexCache.getLast();
            firstLineIndexCache = i1;
            ai = new int[2];
            int[] _tmp = ai;
            ai[0] = 0;
            ai[1] = 0;
            tess.lineIndexCache.setCounter(ai);
            j1 = 0;
            flag = false;
            l1 = i1;
            i1 = ((flag) ? 1 : 0);
            while(j1 < i) 
            {
                int k1 = j1 + 1;
                if(j > 0)
                    i1 = addLineSegment3D(i1, k1, k1 - 2, k1 - 1, l1, new short[] {
                        -1, -1
                    }, false);
                else
                    i1 = addLineSegment3D(i1, k1, k1 - 2, k1 - 1, l1, null, false);
                j1++;
                l1 = i1;
                i1 = k1;
            }
            tess.lineIndexCache.setCounter(null);
            tess.lineIndexCount = ai[0] + l;
            tess.lineVertexCount = ai[1] + k;
            lastLineIndexCache = l1;
        }

        void tessellateLines()
        {
            int i = in.vertexCount;
            if(!stroke || 2 > i) goto _L2; else goto _L1
_L1:
            strokeVertices = in.vertices;
            strokeColors = in.strokeColors;
            strokeWeights = in.strokeWeights;
            updateTex();
            i /= 2;
            if(!is3D) goto _L4; else goto _L3
_L3:
            tessellateLines3D(i);
_L2:
            return;
_L4:
            if(is2D)
            {
                beginNoTex();
                tessellateLines2D(i);
                endNoTex();
            }
            if(true) goto _L2; else goto _L5
_L5:
        }

        void tessellateLines2D(int i)
        {
            int j = 0;
            int k = i * 4;
            if(noCapsJoins(k))
            {
                tess.polyVertexCheck(k);
                tess.polyIndexCheck(i * 2 * 3);
                boolean flag;
                boolean flag1;
                if(in.renderMode == 1)
                    j = tess.polyIndexCache.addNew();
                else
                    j = tess.polyIndexCache.getLast();
                firstLineIndexCache = j;
                if(firstPolyIndexCache == -1)
                    firstPolyIndexCache = j;
                flag = clampLines2D(i);
                flag1 = false;
                k = j;
                for(j = ((flag1) ? 1 : 0); j < i; j++)
                    k = addLineSegment2D(j * 2 + 0, j * 2 + 1, k, false, flag);

                lastPolyIndexCache = k;
                lastLineIndexCache = k;
            } else
            {
                LinePath linepath = new LinePath(1);
                for(; j < i; j++)
                {
                    int i1 = j * 2 + 0;
                    int l = j * 2 + 1;
                    linepath.moveTo(in.vertices[i1 * 3 + 0], in.vertices[i1 * 3 + 1], in.strokeColors[i1]);
                    linepath.lineTo(in.vertices[l * 3 + 0], in.vertices[l * 3 + 1], in.strokeColors[l]);
                }

                tessellateLinePath(linepath);
            }
        }

        void tessellateLines3D(int i)
        {
            int j = tess.lineVertexCount;
            int k = tess.lineIndexCount;
            tess.lineVertexCheck(i * 4);
            tess.lineIndexCheck(i * 2 * 3);
            int l;
            int ai[];
            boolean flag;
            int j1;
            if(in.renderMode == 1)
                l = tess.lineIndexCache.addNew();
            else
                l = tess.lineIndexCache.getLast();
            firstLineIndexCache = l;
            ai = new int[2];
            int[] _tmp = ai;
            ai[0] = 0;
            ai[1] = 0;
            tess.lineIndexCache.setCounter(ai);
            flag = false;
            j1 = l;
            for(l = ((flag) ? 1 : 0); l < i; l++)
            {
                int i1 = l * 2 + 0;
                int k1 = l * 2 + 1;
                j1 = addLineSegment3D(i1, k1, i1 - 2, k1 - 1, j1, null, false);
            }

            tess.lineIndexCache.setCounter(null);
            tess.lineIndexCount = ai[0] + k;
            tess.lineVertexCount = ai[1] + j;
            lastLineIndexCache = j1;
        }

        void tessellatePoints()
        {
            if(strokeCap == 2)
                tessellateRoundPoints();
            else
                tessellateSquarePoints();
        }

        void tessellatePolygon(boolean flag, boolean flag1, boolean flag2)
        {
            beginTex();
            if(3 <= in.vertexCount)
            {
                firstPolyIndexCache = -1;
                initGluTess();
                boolean flag3 = clampPolygon();
                TessellatorCallback tessellatorcallback = callback;
                boolean flag4;
                int i;
                if(in.renderMode == 1)
                    flag4 = true;
                else
                    flag4 = false;
                tessellatorcallback.init(flag4, false, flag2, flag3);
                if(fill)
                {
                    gluTess.beginPolygon();
                    int ai[];
                    if(flag)
                        gluTess.setWindingRule(PGL.TESS_WINDING_NONZERO);
                    else
                        gluTess.setWindingRule(PGL.TESS_WINDING_ODD);
                    gluTess.beginContour();
                }
                if(stroke)
                {
                    beginPolygonStroke();
                    beginStrokePath();
                }
                i = 0;
                for(int j = 0; j < in.vertexCount;)
                {
                    int k;
                    boolean flag5;
                    if(in.codes != null && i < in.codeCount)
                    {
                        ai = in.codes;
                        k = i + 1;
                        int l = ai[i];
                        if(l == 4 && k < in.codeCount)
                        {
                            l = in.codes[k];
                            i = k + 1;
                            flag5 = true;
                            k = l;
                        } else
                        {
                            flag5 = false;
                            i = k;
                            k = l;
                        }
                    } else
                    {
                        flag5 = false;
                        k = 0;
                    }
                    if(flag5)
                    {
                        if(stroke)
                        {
                            endStrokePath(flag1);
                            beginStrokePath();
                        }
                        if(fill)
                        {
                            gluTess.endContour();
                            gluTess.beginContour();
                        }
                    }
                    if(k == 1)
                    {
                        addBezierVertex(j);
                        j += 3;
                    } else
                    if(k == 2)
                    {
                        addQuadraticVertex(j);
                        j += 2;
                    } else
                    if(k == 3)
                    {
                        addCurveVertex(j);
                        j++;
                    } else
                    {
                        addVertex(j);
                        j++;
                    }
                }

                if(stroke)
                {
                    endStrokePath(flag1);
                    endPolygonStroke();
                }
                if(fill)
                {
                    gluTess.endContour();
                    gluTess.endPolygon();
                }
            }
            endTex();
            if(stroke)
                tessellateStrokePath();
        }

        void tessellateQuadStrip()
        {
            int i = 1;
            beginTex();
            int j = in.vertexCount / 2 - 1;
            if(fill && 1 <= j)
            {
                setRawSize(j * 6);
                int k = 0;
                boolean flag = clampQuadStrip(j);
                for(; i < j + 1; i++)
                {
                    int l = (i - 1) * 2 + 1;
                    int i1 = i * 2;
                    int ai[] = rawIndices;
                    int j1 = k + 1;
                    ai[k] = (i - 1) * 2;
                    ai = rawIndices;
                    k = j1 + 1;
                    ai[j1] = l;
                    ai = rawIndices;
                    j1 = k + 1;
                    ai[k] = i1;
                    ai = rawIndices;
                    k = j1 + 1;
                    ai[j1] = l;
                    ai = rawIndices;
                    l = k + 1;
                    ai[k] = i * 2 + 1;
                    ai = rawIndices;
                    k = l + 1;
                    ai[l] = i1;
                }

                splitRawIndices(flag);
            }
            endTex();
            tessellateEdges();
        }

        void tessellateQuads()
        {
            int i = 0;
            beginTex();
            int j = in.vertexCount / 4;
            if(fill && 1 <= j)
            {
                setRawSize(j * 6);
                boolean flag = clampQuads(j);
                int k = 0;
                for(; i < j; i++)
                {
                    int l = i * 4 + 0;
                    int i1 = i * 4 + 2;
                    int ai[] = rawIndices;
                    int j1 = k + 1;
                    ai[k] = l;
                    ai = rawIndices;
                    k = j1 + 1;
                    ai[j1] = i * 4 + 1;
                    ai = rawIndices;
                    j1 = k + 1;
                    ai[k] = i1;
                    ai = rawIndices;
                    k = j1 + 1;
                    ai[j1] = i1;
                    ai = rawIndices;
                    i1 = k + 1;
                    ai[k] = i * 4 + 3;
                    ai = rawIndices;
                    k = i1 + 1;
                    ai[i1] = l;
                }

                splitRawIndices(flag);
            }
            endTex();
            tessellateEdges();
        }

        void tessellateRoundPoints()
        {
            int i = in.vertexCount;
            if(!stroke || 1 > i) goto _L2; else goto _L1
_L1:
            int j;
            int k;
            j = PApplet.min(200, PApplet.max(20, (int)((6.283185F * strokeWeight) / 10F))) + 1;
            if(PGL.MAX_VERTEX_INDEX1 <= j)
                throw new RuntimeException("Error in point tessellation.");
            updateTex();
            k = j * i;
            i *= (j - 1) * 3;
            if(!is3D) goto _L4; else goto _L3
_L3:
            tessellateRoundPoints3D(k, i, j);
_L2:
            return;
_L4:
            if(is2D)
            {
                beginNoTex();
                tessellateRoundPoints2D(k, i, j);
                endNoTex();
            }
            if(true) goto _L2; else goto _L5
_L5:
        }

        void tessellateRoundPoints2D(int i, int j, int k)
        {
            int l = k - 1;
            tess.polyVertexCheck(i);
            tess.polyIndexCheck(j);
            int i1 = tess.firstPolyVertex;
            j = tess.firstPolyIndex;
            IndexCache indexcache = tess.polyIndexCache;
            if(in.renderMode == 1)
                i = indexcache.addNew();
            else
                i = indexcache.getLast();
            firstPointIndexCache = i;
            if(firstPolyIndexCache == -1)
                firstPolyIndexCache = i;
            for(int j1 = 0; j1 < in.vertexCount;)
            {
                int k1 = indexcache.vertexCount[i];
                if(PGL.MAX_VERTEX_INDEX1 <= k1 + k)
                {
                    i = indexcache.addNew();
                    k1 = 0;
                }
                float f = in.vertices[j1 * 3 + 0];
                float f1 = in.vertices[j1 * 3 + 1];
                int l1 = in.strokeColors[j1];
                float f2 = 720F / (float)l;
                tess.setPolyVertex(i1, f, f1, 0.0F, l1, false);
                int j2 = 0;
                i1++;
                float f3 = 0.0F;
                for(; j2 < l; j2++)
                {
                    tess.setPolyVertex(i1, f + 0.5F * setLastIndex[(int)f3] * strokeWeight, f1 + 0.5F * expandArraySize[(int)f3] * strokeWeight, 0.0F, l1, false);
                    i1++;
                    f3 = (f3 + f2) % 720F;
                }

                for(j2 = 1; j2 < k - 1; j2++)
                {
                    short aword0[] = tess.polyIndices;
                    int i2 = j + 1;
                    aword0[j] = (short)(k1 + 0);
                    aword0 = tess.polyIndices;
                    int k2 = i2 + 1;
                    aword0[i2] = (short)(k1 + j2);
                    aword0 = tess.polyIndices;
                    j = k2 + 1;
                    aword0[k2] = (short)(k1 + j2 + 1);
                }

                short aword1[] = tess.polyIndices;
                j2 = j + 1;
                aword1[j] = (short)(k1 + 0);
                aword1 = tess.polyIndices;
                j = j2 + 1;
                aword1[j2] = (short)(k1 + 1);
                tess.polyIndices[j] = (short)((k1 + k) - 1);
                indexcache.incCounts(i, (k - 1) * 3, k);
                j1++;
                j++;
            }

            lastPolyIndexCache = i;
            lastPointIndexCache = i;
        }

        void tessellateRoundPoints3D(int i, int j, int k)
        {
            int l = k - 1;
            tess.pointVertexCheck(i);
            tess.pointIndexCheck(j);
            int i1 = tess.firstPointVertex;
            j = tess.firstPointVertex;
            int j1 = tess.firstPointIndex;
            IndexCache indexcache = tess.pointIndexCache;
            int l1;
            boolean flag;
            if(in.renderMode == 1)
                i = indexcache.addNew();
            else
                i = indexcache.getLast();
            firstPointIndexCache = i;
            l1 = i;
            flag = false;
            i = j1;
            for(int k1 = ((flag) ? 1 : 0); k1 < in.vertexCount; k1++)
            {
                int j2 = indexcache.vertexCount[l1];
                int i2 = j2;
                if(PGL.MAX_VERTEX_INDEX1 <= j2 + k)
                {
                    l1 = indexcache.addNew();
                    i2 = 0;
                }
                for(j2 = 0; j2 < k;)
                {
                    tess.setPointVertex(i1, in, k1);
                    j2++;
                    i1++;
                }

                tess.pointOffsets[j * 2 + 0] = 0.0F;
                tess.pointOffsets[j * 2 + 1] = 0.0F;
                float f = 720F / (float)l;
                j++;
                float f1 = 0.0F;
                for(j2 = 0; j2 < l;)
                {
                    tess.pointOffsets[j * 2 + 0] = 0.5F * setLastIndex[(int)f1] * transformScale() * strokeWeight;
                    tess.pointOffsets[j * 2 + 1] = 0.5F * expandArraySize[(int)f1] * transformScale() * strokeWeight;
                    f1 = (f1 + f) % 720F;
                    j2++;
                    j++;
                }

                boolean flag1 = true;
                j2 = i;
                for(i = ((flag1) ? 1 : 0); i < k - 1; i++)
                {
                    short aword0[] = tess.pointIndices;
                    int k2 = j2 + 1;
                    aword0[j2] = (short)(i2 + 0);
                    aword0 = tess.pointIndices;
                    int l2 = k2 + 1;
                    aword0[k2] = (short)(i2 + i);
                    aword0 = tess.pointIndices;
                    j2 = l2 + 1;
                    aword0[l2] = (short)(i2 + i + 1);
                }

                short aword1[] = tess.pointIndices;
                i = j2 + 1;
                aword1[j2] = (short)(i2 + 0);
                aword1 = tess.pointIndices;
                j2 = i + 1;
                aword1[i] = (short)(i2 + 1);
                aword1 = tess.pointIndices;
                i = j2 + 1;
                aword1[j2] = (short)((i2 + k) - 1);
                indexcache.incCounts(l1, (k - 1) * 3, k);
            }

            lastPointIndexCache = l1;
        }

        void tessellateSquarePoints()
        {
            int i = in.vertexCount;
            if(!stroke || 1 > i) goto _L2; else goto _L1
_L1:
            int j;
            updateTex();
            j = i * 5;
            i *= 12;
            if(!is3D) goto _L4; else goto _L3
_L3:
            tessellateSquarePoints3D(j, i);
_L2:
            return;
_L4:
            if(is2D)
            {
                beginNoTex();
                tessellateSquarePoints2D(j, i);
                endNoTex();
            }
            if(true) goto _L2; else goto _L5
_L5:
        }

        void tessellateSquarePoints2D(int i, int j)
        {
            tess.polyVertexCheck(i);
            tess.polyIndexCheck(j);
            boolean flag = clampSquarePoints2D();
            int k = tess.firstPolyVertex;
            j = tess.firstPolyIndex;
            IndexCache indexcache = tess.polyIndexCache;
            if(in.renderMode == 1)
                i = indexcache.addNew();
            else
                i = indexcache.getLast();
            firstPointIndexCache = i;
            if(firstPolyIndexCache == -1)
                firstPolyIndexCache = i;
            for(int l = 0; l < in.vertexCount;)
            {
                int i1 = indexcache.vertexCount[i];
                if(PGL.MAX_VERTEX_INDEX1 <= i1 + 5)
                {
                    i = indexcache.addNew();
                    i1 = 0;
                }
                float f = in.vertices[l * 3 + 0];
                float f1 = in.vertices[l * 3 + 1];
                int j1 = in.strokeColors[l];
                tess.setPolyVertex(k, f, f1, 0.0F, j1, flag);
                k++;
                for(int l1 = 0; l1 < 4; l1++)
                {
                    tess.setPolyVertex(k, f + 0.5F * PGraphicsOpenGL.QUAD_POINT_SIGNS[l1][0] * strokeWeight, f1 + 0.5F * PGraphicsOpenGL.QUAD_POINT_SIGNS[l1][1] * strokeWeight, 0.0F, j1, flag);
                    k++;
                }

                for(int i2 = 1; i2 < 4; i2++)
                {
                    short aword0[] = tess.polyIndices;
                    int k2 = j + 1;
                    aword0[j] = (short)(i1 + 0);
                    aword0 = tess.polyIndices;
                    int k1 = k2 + 1;
                    aword0[k2] = (short)(i1 + i2);
                    aword0 = tess.polyIndices;
                    j = k1 + 1;
                    aword0[k1] = (short)(i1 + i2 + 1);
                }

                short aword1[] = tess.polyIndices;
                int j2 = j + 1;
                aword1[j] = (short)(i1 + 0);
                aword1 = tess.polyIndices;
                j = j2 + 1;
                aword1[j2] = (short)(i1 + 1);
                tess.polyIndices[j] = (short)((i1 + 5) - 1);
                indexcache.incCounts(i, 12, 5);
                l++;
                j++;
            }

            lastPolyIndexCache = i;
            lastPointIndexCache = i;
        }

        void tessellateSquarePoints3D(int i, int j)
        {
            tess.pointVertexCheck(i);
            tess.pointIndexCheck(j);
            int k = tess.firstPointVertex;
            j = tess.firstPointVertex;
            int l = tess.firstPointIndex;
            IndexCache indexcache = tess.pointIndexCache;
            int j1;
            boolean flag;
            if(in.renderMode == 1)
                i = indexcache.addNew();
            else
                i = indexcache.getLast();
            firstPointIndexCache = i;
            j1 = i;
            flag = false;
            i = l;
            for(int i1 = ((flag) ? 1 : 0); i1 < in.vertexCount; i1++)
            {
                int l1 = indexcache.vertexCount[j1];
                int k1 = l1;
                if(PGL.MAX_VERTEX_INDEX1 <= l1 + 5)
                {
                    j1 = indexcache.addNew();
                    k1 = 0;
                }
                for(l1 = 0; l1 < 5;)
                {
                    tess.setPointVertex(k, in, i1);
                    l1++;
                    k++;
                }

                tess.pointOffsets[j * 2 + 0] = 0.0F;
                tess.pointOffsets[j * 2 + 1] = 0.0F;
                j++;
                for(l1 = 0; l1 < 4;)
                {
                    tess.pointOffsets[j * 2 + 0] = 0.5F * PGraphicsOpenGL.QUAD_POINT_SIGNS[l1][0] * transformScale() * strokeWeight;
                    tess.pointOffsets[j * 2 + 1] = 0.5F * PGraphicsOpenGL.QUAD_POINT_SIGNS[l1][1] * transformScale() * strokeWeight;
                    l1++;
                    j++;
                }

                boolean flag1 = true;
                l1 = i;
                for(i = ((flag1) ? 1 : 0); i < 4; i++)
                {
                    short aword0[] = tess.pointIndices;
                    int i2 = l1 + 1;
                    aword0[l1] = (short)(k1 + 0);
                    aword0 = tess.pointIndices;
                    int j2 = i2 + 1;
                    aword0[i2] = (short)(k1 + i);
                    aword0 = tess.pointIndices;
                    l1 = j2 + 1;
                    aword0[j2] = (short)(k1 + i + 1);
                }

                short aword1[] = tess.pointIndices;
                i = l1 + 1;
                aword1[l1] = (short)(k1 + 0);
                aword1 = tess.pointIndices;
                l1 = i + 1;
                aword1[i] = (short)(k1 + 1);
                aword1 = tess.pointIndices;
                i = l1 + 1;
                aword1[l1] = (short)((k1 + 5) - 1);
                indexcache.incCounts(j1, 12, 5);
            }

            lastPointIndexCache = j1;
        }

        void tessellateStrokePath()
        {
            if(in.edgeCount != 0) goto _L2; else goto _L1
_L1:
            return;
_L2:
            strokeVertices = pathVertices;
            strokeColors = pathColors;
            strokeWeights = pathWeights;
            if(is3D)
                tessellateEdges3D();
            else
            if(is2D)
            {
                beginNoTex();
                tessellateEdges2D();
                endNoTex();
            }
            if(true) goto _L1; else goto _L3
_L3:
        }

        void tessellateTriangleFan()
        {
            beginTex();
            int i = in.vertexCount;
            if(fill && 3 <= i)
            {
                setRawSize((i - 2) * 3);
                boolean flag = clampTriangleFan();
                i = 1;
                int j = 0;
                for(; i < in.vertexCount - 1; i++)
                {
                    int ai[] = rawIndices;
                    int k = j + 1;
                    ai[j] = 0;
                    ai = rawIndices;
                    int l = k + 1;
                    ai[k] = i;
                    ai = rawIndices;
                    j = l + 1;
                    ai[l] = i + 1;
                }

                splitRawIndices(flag);
            }
            endTex();
            tessellateEdges();
        }

        void tessellateTriangleStrip()
        {
            beginTex();
            int i = in.vertexCount;
            if(fill && 3 <= i)
            {
                setRawSize((i - 2) * 3);
                i = 0;
                boolean flag = clampTriangleStrip();
                int j = 1;
                while(j < in.vertexCount - 1) 
                {
                    int ai[] = rawIndices;
                    int k = i + 1;
                    ai[i] = j;
                    if(j % 2 == 0)
                    {
                        int ai1[] = rawIndices;
                        int l = k + 1;
                        ai1[k] = j - 1;
                        ai1 = rawIndices;
                        i = l + 1;
                        ai1[l] = j + 1;
                    } else
                    {
                        int ai2[] = rawIndices;
                        int i1 = k + 1;
                        ai2[k] = j + 1;
                        ai2 = rawIndices;
                        i = i1 + 1;
                        ai2[i1] = j - 1;
                    }
                    j++;
                }
                splitRawIndices(flag);
            }
            endTex();
            tessellateEdges();
        }

        void tessellateTriangles()
        {
            int i = 0;
            beginTex();
            int j = in.vertexCount / 3;
            if(fill && 1 <= j)
            {
                setRawSize(j * 3);
                boolean flag = clampTriangles();
                for(int k = 0; i < j * 3; k++)
                {
                    rawIndices[k] = i;
                    i++;
                }

                splitRawIndices(flag);
            }
            endTex();
            tessellateEdges();
        }

        void tessellateTriangles(int ai[])
        {
            beginTex();
            int i = in.vertexCount;
            if(fill && 3 <= i)
            {
                int j = ai.length;
                setRawSize(j);
                PApplet.arrayCopy(ai, rawIndices, j);
                splitRawIndices(clampTriangles(ai));
            }
            endTex();
            tessellateEdges();
        }

        float transformScale()
        {
            float f;
            if(-1F < transformScale)
            {
                f = transformScale;
            } else
            {
                f = PGraphicsOpenGL.matrixScale(transform);
                transformScale = f;
            }
            return f;
        }

        void unclampLine2D(int i, float f, float f1)
        {
            PMatrix3D pmatrix3d = pg.modelview;
            int j = i * 4;
            float af[] = tess.polyVertices;
            i = j + 1;
            af[j] = pmatrix3d.m00 * f + pmatrix3d.m01 * f1 + pmatrix3d.m03;
            af = tess.polyVertices;
            float f2 = pmatrix3d.m10;
            float f3 = pmatrix3d.m11;
            af[i] = pmatrix3d.m13 + (f2 * f + f3 * f1);
        }

        void updateTex()
        {
            beginTex();
            endTex();
        }

        boolean accurate2DStrokes;
        int beginPath;
        TessellatorCallback callback;
        int dupCount;
        int dupIndices[];
        boolean fill;
        int firstLineIndexCache;
        int firstPointIndexCache;
        int firstPolyIndexCache;
        int firstTexCache;
        int firstTexIndex;
        PGL.Tessellator gluTess;
        InGeometry in;
        boolean is2D;
        boolean is3D;
        int lastLineIndexCache;
        int lastPointIndexCache;
        int lastPolyIndexCache;
        PImage newTexImage;
        int pathColors[];
        int pathVertexCount;
        float pathVertices[];
        float pathWeights[];
        protected PGraphicsOpenGL pg;
        PImage prevTexImage;
        int rawIndices[];
        int rawSize;
        boolean stroke;
        int strokeCap;
        int strokeColor;
        int strokeColors[];
        int strokeJoin;
        float strokeVertices[];
        float strokeWeight;
        float strokeWeights[];
        TessGeometry tess;
        TexCache texCache;
        PMatrix transform;
        float transformScale;

        public Tessellator()
        {
            rawIndices = new int[512];
            accurate2DStrokes = true;
            transform = null;
            is2D = false;
            is3D = true;
        }
    }

    protected class Tessellator.TessellatorCallback
        implements PGL.TessellatorCallback
    {

        private void normalize(double ad[], int i)
        {
            double d = Math.sqrt(ad[i] * ad[i] + ad[i + 1] * ad[i + 1] + ad[i + 2] * ad[i + 2]);
            if(0.0D < d)
            {
                ad[i] = ad[i] / d;
                int j = i + 1;
                ad[j] = ad[j] / d;
                i += 2;
                ad[i] = ad[i] / d;
            }
        }

        protected void addIndex(int i)
        {
            tess.polyIndexCheck();
            tess.polyIndices[tess.polyIndexCount - 1] = (short)(vertFirst + i);
        }

        public void begin(int i)
        {
            cacheIndex = cache.getLast();
            if(firstPolyIndexCache == -1)
                firstPolyIndexCache = cacheIndex;
            if(strokeTess && firstLineIndexCache == -1)
                firstLineIndexCache = cacheIndex;
            vertFirst = cache.vertexCount[cacheIndex];
            vertOffset = cache.vertexOffset[cacheIndex];
            vertCount = 0;
            if(i != PGL.TRIANGLE_FAN) goto _L2; else goto _L1
_L1:
            primitive = 11;
_L4:
            return;
_L2:
            if(i == PGL.TRIANGLE_STRIP)
                primitive = 10;
            else
            if(i == PGL.TRIANGLES)
                primitive = 9;
            if(true) goto _L4; else goto _L3
_L3:
        }

        protected void calcTriNormal(int i, int j, int k)
        {
            tess.calcPolyNormal(vertFirst + vertOffset + i, vertFirst + vertOffset + j, vertFirst + vertOffset + k);
        }

        public void combine(double ad[], Object aobj[], float af[], Object aobj1[])
        {
            int i = ((double[])aobj[0]).length;
            double ad1[] = new double[i];
            ad1[0] = ad[0];
            ad1[1] = ad[1];
            ad1[2] = ad[2];
            for(int j = 3; j < i; j++)
            {
                ad1[j] = 0.0D;
                for(int l = 0; l < 4; l++)
                {
                    ad = (double[])aobj[l];
                    if(ad != null)
                        ad1[j] = ad1[j] + (double)af[l] * ad[j];
                }

            }

            normalize(ad1, 7);
            if(25 < i)
            {
                int k = 25;
                int i1 = 0;
                while(i1 < attribs.size()) 
                {
                    ad = attribs.get(i1);
                    if(ad.isNormal())
                    {
                        normalize(ad1, k);
                        k += 3;
                    } else
                    {
                        k += ((VertexAttribute) (ad)).size;
                    }
                    i1++;
                }
            }
            aobj1[0] = ad1;
        }

        public void end()
        {
            boolean flag;
            int i;
            int j;
            int k;
            flag = true;
            i = 1;
            j = 0;
            k = 0;
            if(PGL.MAX_VERTEX_INDEX1 <= vertFirst + vertCount)
            {
                cacheIndex = cache.addNew();
                vertFirst = cache.vertexCount[cacheIndex];
                vertOffset = cache.vertexOffset[cacheIndex];
            }
            primitive;
            JVM INSTR tableswitch 9 11: default 96
        //                       9 322
        //                       10 208
        //                       11 145;
               goto _L1 _L2 _L3 _L4
_L1:
            i = k;
_L9:
            cache.incCounts(cacheIndex, i, vertCount);
            lastPolyIndexCache = cacheIndex;
            if(strokeTess)
                lastLineIndexCache = cacheIndex;
            return;
_L4:
            j = vertCount;
            for(; i < vertCount - 1; i++)
            {
                addIndex(0);
                addIndex(i);
                addIndex(i + 1);
                if(calcNormals)
                    calcTriNormal(0, i, i + 1);
            }

            i = (j - 2) * 3;
            continue; /* Loop/switch isn't completed */
_L3:
            k = (vertCount - 2) * 3;
            j = ((flag) ? 1 : 0);
_L6:
            i = k;
            if(j >= vertCount - 1)
                continue; /* Loop/switch isn't completed */
            if(j % 2 != 0)
                break; /* Loop/switch isn't completed */
            addIndex(j + 1);
            addIndex(j);
            addIndex(j - 1);
            if(calcNormals)
                calcTriNormal(j + 1, j, j - 1);
_L7:
            j++;
            if(true) goto _L6; else goto _L5
_L5:
            addIndex(j - 1);
            addIndex(j);
            addIndex(j + 1);
            if(calcNormals)
                calcTriNormal(j - 1, j, j + 1);
              goto _L7
            if(true) goto _L6; else goto _L2
_L2:
            int l = vertCount;
            for(i = 0; i < vertCount; i++)
                addIndex(i);

            if(calcNormals)
                for(i = j; i < vertCount / 3; i++)
                    calcTriNormal(i * 3 + 0, i * 3 + 1, i * 3 + 2);

            i = l;
            if(true) goto _L9; else goto _L8
_L8:
        }

        public void error(int i)
        {
            PGraphics.showWarning("Tessellation Error: %1$s", new Object[] {
                pg.pgl.tessError(i)
            });
        }

        public void init(boolean flag, boolean flag1, boolean flag2, boolean flag3)
        {
            strokeTess = flag1;
            calcNormals = flag2;
            clampXY = flag3;
            cache = tess.polyIndexCache;
            if(flag)
                cache.addNew();
        }

        public void vertex(Object obj)
        {
            if(obj instanceof double[])
            {
                obj = (double[])obj;
                if(obj.length < 25)
                    throw new RuntimeException("TessCallback vertex() data is too small");
                if(vertCount < PGL.MAX_VERTEX_INDEX1)
                {
                    tess.addPolyVertex(((double []) (obj)), clampXY);
                    vertCount = vertCount + 1;
                    return;
                } else
                {
                    throw new RuntimeException("The tessellator is generating too many vertices, reduce complexity of shape.");
                }
            } else
            {
                throw new RuntimeException("TessCallback vertex() data not understood");
            }
        }

        AttributeMap attribs;
        IndexCache cache;
        int cacheIndex;
        boolean calcNormals;
        boolean clampXY;
        int primitive;
        boolean strokeTess;
        final Tessellator this$0;
        int vertCount;
        int vertFirst;
        int vertOffset;

        public Tessellator.TessellatorCallback(AttributeMap attributemap)
        {
            this$0 = Tessellator.this;
            super();
            attribs = attributemap;
        }
    }

    protected static class TexCache
    {

        void addTexture(PImage pimage, int i, int j, int k, int l)
        {
            arrayCheck();
            textures[size] = pimage;
            firstIndex[size] = i;
            lastIndex[size] = k;
            firstCache[size] = j;
            lastCache[size] = l;
            boolean flag = hasTextures;
            if(pimage != null)
                i = 1;
            else
                i = 0;
            hasTextures = i | flag;
            size = size + 1;
        }

        void allocate()
        {
            textures = new PImage[PGL.DEFAULT_IN_TEXTURES];
            firstIndex = new int[PGL.DEFAULT_IN_TEXTURES];
            lastIndex = new int[PGL.DEFAULT_IN_TEXTURES];
            firstCache = new int[PGL.DEFAULT_IN_TEXTURES];
            lastCache = new int[PGL.DEFAULT_IN_TEXTURES];
            size = 0;
            hasTextures = false;
        }

        void arrayCheck()
        {
            if(size == textures.length)
            {
                int i = size << 1;
                expandTextures(i);
                expandFirstIndex(i);
                expandLastIndex(i);
                expandFirstCache(i);
                expandLastCache(i);
            }
        }

        void clear()
        {
            Arrays.fill(textures, 0, size, null);
            size = 0;
            hasTextures = false;
        }

        boolean containsTexture(PImage pimage)
        {
            boolean flag = false;
            int i = 0;
            do
            {
label0:
                {
                    boolean flag1 = flag;
                    if(i < size)
                    {
                        if(textures[i] != pimage)
                            break label0;
                        flag1 = true;
                    }
                    return flag1;
                }
                i++;
            } while(true);
        }

        void expandFirstCache(int i)
        {
            int ai[] = new int[i];
            PApplet.arrayCopy(firstCache, 0, ai, 0, size);
            firstCache = ai;
        }

        void expandFirstIndex(int i)
        {
            int ai[] = new int[i];
            PApplet.arrayCopy(firstIndex, 0, ai, 0, size);
            firstIndex = ai;
        }

        void expandLastCache(int i)
        {
            int ai[] = new int[i];
            PApplet.arrayCopy(lastCache, 0, ai, 0, size);
            lastCache = ai;
        }

        void expandLastIndex(int i)
        {
            int ai[] = new int[i];
            PApplet.arrayCopy(lastIndex, 0, ai, 0, size);
            lastIndex = ai;
        }

        void expandTextures(int i)
        {
            PImage apimage[] = new PImage[i];
            PApplet.arrayCopy(textures, 0, apimage, 0, size);
            textures = apimage;
        }

        Texture getTexture(int i)
        {
            PImage pimage = textures[i];
            Texture texture1 = null;
            if(pimage != null)
                texture1 = pg.getTexture(pimage);
            return texture1;
        }

        PImage getTextureImage(int i)
        {
            return textures[i];
        }

        void setLastIndex(int i, int j)
        {
            lastIndex[size - 1] = i;
            lastCache[size - 1] = j;
        }

        int firstCache[];
        int firstIndex[];
        boolean hasTextures;
        int lastCache[];
        int lastIndex[];
        PGraphicsOpenGL pg;
        int size;
        PImage textures[];

        TexCache(PGraphicsOpenGL pgraphicsopengl)
        {
            pg = pgraphicsopengl;
            allocate();
        }
    }

    protected static class VertexAttribute
    {

        boolean active(PShader pshader)
        {
            if(active && glLoc == -1)
            {
                glLoc = pshader.getAttributeLoc(name);
                if(glLoc == -1)
                    active = false;
            }
            return active;
        }

        void add(byte abyte0[], int i)
        {
            PApplet.arrayCopy(bvalues, 0, abyte0, i, size);
        }

        void add(float af[], int i)
        {
            PApplet.arrayCopy(fvalues, 0, af, i, size);
        }

        void add(int ai[], int i)
        {
            PApplet.arrayCopy(ivalues, 0, ai, i, size);
        }

        void bind(PGL pgl1)
        {
            pgl1.enableVertexAttribArray(glLoc);
        }

        boolean bufferCreated()
        {
            boolean flag;
            if(buf != null && buf.glId > 0)
                flag = true;
            else
                flag = false;
            return flag;
        }

        void createBuffer(PGL pgl1)
        {
            buf = new VertexBuffer(pg, PGL.ARRAY_BUFFER, size, elementSize, false);
        }

        void deleteBuffer(PGL pgl1)
        {
            if(buf.glId != 0)
            {
                PGraphicsOpenGL.intBuffer.put(0, buf.glId);
                if(pgl1.threadIsCurrent())
                    pgl1.deleteBuffers(1, PGraphicsOpenGL.intBuffer);
            }
        }

        public boolean diff(VertexAttribute vertexattribute)
        {
            boolean flag;
            if(!name.equals(vertexattribute.name) || kind != vertexattribute.kind || type != vertexattribute.type || size != vertexattribute.size || tessSize != vertexattribute.tessSize || elementSize != vertexattribute.elementSize)
                flag = true;
            else
                flag = false;
            return flag;
        }

        boolean isBool()
        {
            boolean flag;
            if(type == PGL.BOOL)
                flag = true;
            else
                flag = false;
            return flag;
        }

        boolean isColor()
        {
            boolean flag;
            if(kind == 2)
                flag = true;
            else
                flag = false;
            return flag;
        }

        boolean isFloat()
        {
            boolean flag;
            if(type == PGL.FLOAT)
                flag = true;
            else
                flag = false;
            return flag;
        }

        boolean isInt()
        {
            boolean flag;
            if(type == PGL.INT)
                flag = true;
            else
                flag = false;
            return flag;
        }

        boolean isNormal()
        {
            boolean flag = true;
            if(kind != 1)
                flag = false;
            return flag;
        }

        boolean isOther()
        {
            boolean flag;
            if(kind == 3)
                flag = true;
            else
                flag = false;
            return flag;
        }

        boolean isPosition()
        {
            boolean flag;
            if(kind == 0)
                flag = true;
            else
                flag = false;
            return flag;
        }

        void set(float f, float f1, float f2)
        {
            fvalues[0] = f;
            fvalues[1] = f1;
            fvalues[2] = f2;
        }

        void set(int i)
        {
            ivalues[0] = i;
        }

        void set(float af[])
        {
            PApplet.arrayCopy(af, 0, fvalues, 0, size);
        }

        void set(int ai[])
        {
            PApplet.arrayCopy(ai, 0, ivalues, 0, size);
        }

        void set(boolean aflag[])
        {
            int i = 0;
            while(i < aflag.length) 
            {
                byte abyte0[] = bvalues;
                int j;
                if(aflag[i])
                    j = 1;
                else
                    j = 0;
                abyte0[i] = (byte)j;
                i++;
            }
        }

        int sizeInBytes(int i)
        {
            return tessSize * i * elementSize;
        }

        void unbind(PGL pgl1)
        {
            pgl1.disableVertexAttribArray(glLoc);
        }

        static final int COLOR = 2;
        static final int NORMAL = 1;
        static final int OTHER = 3;
        static final int POSITION = 0;
        boolean active;
        VertexBuffer buf;
        byte bvalues[];
        int elementSize;
        int firstModified;
        float fvalues[];
        int glLoc;
        int ivalues[];
        int kind;
        int lastModified;
        boolean modified;
        String name;
        PGraphicsOpenGL pg;
        int size;
        int tessSize;
        int type;

        VertexAttribute(PGraphicsOpenGL pgraphicsopengl, String s, int i, int j, int k)
        {
            pg = pgraphicsopengl;
            name = s;
            kind = i;
            type = j;
            size = k;
            if(i == 0)
                tessSize = 4;
            else
                tessSize = k;
            if(j != PGL.FLOAT) goto _L2; else goto _L1
_L1:
            elementSize = PGL.SIZEOF_FLOAT;
            fvalues = new float[k];
_L4:
            buf = null;
            glLoc = -1;
            modified = false;
            firstModified = 0x7fffffff;
            lastModified = 0x80000000;
            active = true;
            return;
_L2:
            if(j == PGL.INT)
            {
                elementSize = PGL.SIZEOF_INT;
                ivalues = new int[k];
            } else
            if(j == PGL.BOOL)
            {
                elementSize = PGL.SIZEOF_INT;
                bvalues = new byte[k];
            }
            if(true) goto _L4; else goto _L3
_L3:
        }
    }


    public PGraphicsOpenGL()
    {
        flushMode = 1;
        polyBuffersCreated = false;
        lineBuffersCreated = false;
        pointBuffersCreated = false;
        modelviewStack = new float[32][16];
        modelviewInvStack = new float[32][16];
        cameraStack = new float[32][16];
        cameraInvStack = new float[32][16];
        projectionStack = new float[32][16];
        lightCount = 0;
        textureWrap = 0;
        textureSampling = 5;
        clip = false;
        texture = null;
        ptexture = null;
        filterTexture = null;
        drawing = false;
        smoothDisabled = false;
        smoothCallCount = 0;
        lastSmoothCall = -10;
        lastBlendMode = -1;
        pixelsOp = 0;
        openContour = false;
        breakShape = false;
        defaultEdges = false;
        pgl = createPGL(this);
        if(intBuffer == null)
        {
            intBuffer = PGL.allocateIntBuffer(2);
            floatBuffer = PGL.allocateFloatBuffer(2);
        }
        viewport = PGL.allocateIntBuffer(4);
        polyAttribs = newAttributeMap();
        inGeo = newInGeometry(this, polyAttribs, 0);
        tessGeo = newTessGeometry(this, polyAttribs, 0);
        texCache = newTexCache(this);
        projection = new PMatrix3D();
        camera = new PMatrix3D();
        cameraInv = new PMatrix3D();
        modelview = new PMatrix3D();
        modelviewInv = new PMatrix3D();
        projmodelview = new PMatrix3D();
        lightType = new int[PGL.MAX_LIGHTS];
        lightPosition = new float[PGL.MAX_LIGHTS * 4];
        lightNormal = new float[PGL.MAX_LIGHTS * 3];
        lightAmbient = new float[PGL.MAX_LIGHTS * 3];
        lightDiffuse = new float[PGL.MAX_LIGHTS * 3];
        lightSpecular = new float[PGL.MAX_LIGHTS * 3];
        lightFalloffCoefficients = new float[PGL.MAX_LIGHTS * 3];
        lightSpotParameters = new float[PGL.MAX_LIGHTS * 2];
        currentLightSpecular = new float[3];
        initialized = false;
    }

    protected static void completeAllPixelTransfers()
    {
        ongoingPixelTransfersIterable.addAll(ongoingPixelTransfers);
        for(Iterator iterator = ongoingPixelTransfersIterable.iterator(); iterator.hasNext(); ((AsyncPixelReader)iterator.next()).completeAllTransfers());
        ongoingPixelTransfersIterable.clear();
    }

    protected static void completeFinishedPixelTransfers()
    {
        ongoingPixelTransfersIterable.addAll(ongoingPixelTransfers);
        for(Iterator iterator = ongoingPixelTransfersIterable.iterator(); iterator.hasNext();)
        {
            AsyncPixelReader asyncpixelreader = (AsyncPixelReader)iterator.next();
            if(!asyncpixelreader.calledThisFrame)
                asyncpixelreader.completeFinishedTransfers();
            asyncpixelreader.calledThisFrame = false;
        }

        ongoingPixelTransfersIterable.clear();
    }

    protected static boolean diff(float f, float f1)
    {
        boolean flag;
        if(PGL.FLOAT_EPS <= Math.abs(f - f1))
            flag = true;
        else
            flag = false;
        return flag;
    }

    protected static int expandArraySize(int i, int j)
    {
        for(; i < j; i <<= 1);
        return i;
    }

    private static void invRotate(PMatrix3D pmatrix3d, float f, float f1, float f2, float f3)
    {
        float f4 = PApplet.cos(-f);
        f = PApplet.sin(-f);
        float f5 = 1.0F - f4;
        pmatrix3d.preApply(f5 * f1 * f1 + f4, f5 * f1 * f2 - f * f3, f5 * f1 * f3 + f * f2, 0.0F, f5 * f1 * f2 + f * f3, f5 * f2 * f2 + f4, f5 * f2 * f3 - f * f1, 0.0F, f5 * f1 * f3 - f * f2, f * f1 + f5 * f2 * f3, f5 * f3 * f3 + f4, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
    }

    protected static void invScale(PMatrix3D pmatrix3d, float f, float f1, float f2)
    {
        pmatrix3d.preApply(1.0F / f, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F / f1, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F / f2, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
    }

    protected static void invTranslate(PMatrix3D pmatrix3d, float f, float f1, float f2)
    {
        pmatrix3d.preApply(1.0F, 0.0F, 0.0F, -f, 0.0F, 1.0F, 0.0F, -f1, 0.0F, 0.0F, 1.0F, -f2, 0.0F, 0.0F, 0.0F, 1.0F);
    }

    protected static float matrixScale(PMatrix pmatrix)
    {
        float f;
        float f1;
        f = 1.0F;
        f1 = f;
        if(pmatrix == null) goto _L2; else goto _L1
_L1:
        if(!(pmatrix instanceof PMatrix2D)) goto _L4; else goto _L3
_L3:
        pmatrix = (PMatrix2D)pmatrix;
        f1 = (float)Math.sqrt(Math.abs(((PMatrix2D) (pmatrix)).m00 * ((PMatrix2D) (pmatrix)).m11 - ((PMatrix2D) (pmatrix)).m01 * ((PMatrix2D) (pmatrix)).m10));
_L2:
        return f1;
_L4:
        f1 = f;
        if(pmatrix instanceof PMatrix3D)
        {
            pmatrix = (PMatrix3D)pmatrix;
            f1 = (float)Math.pow(Math.abs(((PMatrix3D) (pmatrix)).m00 * (((PMatrix3D) (pmatrix)).m11 * ((PMatrix3D) (pmatrix)).m22 - ((PMatrix3D) (pmatrix)).m12 * ((PMatrix3D) (pmatrix)).m21) + ((PMatrix3D) (pmatrix)).m01 * (((PMatrix3D) (pmatrix)).m12 * ((PMatrix3D) (pmatrix)).m20 - ((PMatrix3D) (pmatrix)).m10 * ((PMatrix3D) (pmatrix)).m22) + ((PMatrix3D) (pmatrix)).m02 * (((PMatrix3D) (pmatrix)).m10 * ((PMatrix3D) (pmatrix)).m21 - ((PMatrix3D) (pmatrix)).m11 * ((PMatrix3D) (pmatrix)).m20)), 0.3333333432674408D);
        }
        if(true) goto _L2; else goto _L5
_L5:
    }

    protected static AttributeMap newAttributeMap()
    {
        return new AttributeMap();
    }

    protected static InGeometry newInGeometry(PGraphicsOpenGL pgraphicsopengl, AttributeMap attributemap, int i)
    {
        return new InGeometry(pgraphicsopengl, attributemap, i);
    }

    protected static TessGeometry newTessGeometry(PGraphicsOpenGL pgraphicsopengl, AttributeMap attributemap, int i)
    {
        return new TessGeometry(pgraphicsopengl, attributemap, i);
    }

    protected static TexCache newTexCache(PGraphicsOpenGL pgraphicsopengl)
    {
        return new TexCache(pgraphicsopengl);
    }

    protected static boolean nonZero(float f)
    {
        boolean flag;
        if(PGL.FLOAT_EPS <= Math.abs(f))
            flag = true;
        else
            flag = false;
        return flag;
    }

    protected static boolean same(float f, float f1)
    {
        boolean flag;
        if(Math.abs(f - f1) < PGL.FLOAT_EPS)
            flag = true;
        else
            flag = false;
        return flag;
    }

    protected static boolean zero(float f)
    {
        boolean flag;
        if(Math.abs(f) < PGL.FLOAT_EPS)
            flag = true;
        else
            flag = false;
        return flag;
    }

    protected Texture addTexture(PImage pimage)
    {
        return addTexture(pimage, new Texture.Parameters(2, textureSampling, getHint(-8), textureWrap));
    }

    protected Texture addTexture(PImage pimage, Texture.Parameters parameters)
    {
        if(pimage.width == 0 || pimage.height == 0)
        {
            pimage = null;
        } else
        {
            if(pimage.parent == null)
                pimage.parent = parent;
            parameters = new Texture(this, pimage.pixelWidth, pimage.pixelHeight, parameters);
            setCache(pimage, parameters);
            pimage = parameters;
        }
        return pimage;
    }

    protected void allocatePixels()
    {
        updatePixelSize();
        if(pixels == null || pixels.length != pixelWidth * pixelHeight)
        {
            pixels = new int[pixelWidth * pixelHeight];
            pixelBuffer = PGL.allocateIntBuffer(pixels);
            loaded = false;
        }
    }

    public void ambientLight(float f, float f1, float f2)
    {
        ambientLight(f, f1, f2, 0.0F, 0.0F, 0.0F);
    }

    public void ambientLight(float f, float f1, float f2, float f3, float f4, float f5)
    {
        enableLighting();
        if(lightCount == PGL.MAX_LIGHTS)
        {
            throw new RuntimeException((new StringBuilder()).append("can only create ").append(PGL.MAX_LIGHTS).append(" lights").toString());
        } else
        {
            lightType[lightCount] = 0;
            lightPosition(lightCount, f3, f4, f5, false);
            lightNormal(lightCount, 0.0F, 0.0F, 0.0F);
            lightAmbient(lightCount, f, f1, f2);
            noLightDiffuse(lightCount);
            noLightSpecular(lightCount);
            noLightSpot(lightCount);
            lightFalloff(lightCount, currentLightFalloffConstant, currentLightFalloffLinear, currentLightFalloffQuadratic);
            lightCount = lightCount + 1;
            return;
        }
    }

    public void applyMatrix(float f, float f1, float f2, float f3, float f4, float f5)
    {
        applyMatrixImpl(f, f1, 0.0F, f2, f3, f4, 0.0F, f5, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
    }

    public void applyMatrix(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8, float f9, float f10, float f11, float f12, float f13, 
            float f14, float f15)
    {
        applyMatrixImpl(f, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15);
    }

    public void applyMatrix(PMatrix2D pmatrix2d)
    {
        applyMatrixImpl(pmatrix2d.m00, pmatrix2d.m01, 0.0F, pmatrix2d.m02, pmatrix2d.m10, pmatrix2d.m11, 0.0F, pmatrix2d.m12, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
    }

    public void applyMatrix(PMatrix3D pmatrix3d)
    {
        applyMatrixImpl(pmatrix3d.m00, pmatrix3d.m01, pmatrix3d.m02, pmatrix3d.m03, pmatrix3d.m10, pmatrix3d.m11, pmatrix3d.m12, pmatrix3d.m13, pmatrix3d.m20, pmatrix3d.m21, pmatrix3d.m22, pmatrix3d.m23, pmatrix3d.m30, pmatrix3d.m31, pmatrix3d.m32, pmatrix3d.m33);
    }

    protected void applyMatrixImpl(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8, float f9, float f10, float f11, float f12, float f13, 
            float f14, float f15)
    {
        modelview.apply(f, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15);
        modelviewInv.set(modelview);
        modelviewInv.invert();
        projmodelview.apply(f, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15);
    }

    public void applyProjection(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8, float f9, float f10, float f11, float f12, float f13, 
            float f14, float f15)
    {
        flush();
        projection.apply(f, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15);
        updateProjmodelview();
    }

    public void applyProjection(PMatrix3D pmatrix3d)
    {
        flush();
        projection.apply(pmatrix3d);
        updateProjmodelview();
    }

    protected void arcImpl(float f, float f1, float f2, float f3, float f4, float f5, int i)
    {
        beginShape(11);
        defaultEdges = false;
        normalMode = 1;
        inGeo.setMaterial(fillColor, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininess);
        inGeo.setNormal(normalX, normalY, normalZ);
        inGeo.addArc(f, f1, f2, f3, f4, f5, fill, stroke, i);
        endShape();
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

    protected VertexAttribute attribImpl(String s, int i, int j, int k)
    {
        if(4 >= k) goto _L2; else goto _L1
_L1:
        PGraphics.showWarning("Vertex attributes cannot have more than 4 values");
        s = null;
_L4:
        return s;
_L2:
        VertexAttribute vertexattribute = (VertexAttribute)polyAttribs.get(s);
        VertexAttribute vertexattribute1 = vertexattribute;
        if(vertexattribute == null)
        {
            vertexattribute1 = new VertexAttribute(this, s, i, j, k);
            polyAttribs.put(s, vertexattribute1);
            inGeo.initAttrib(vertexattribute1);
            tessGeo.initAttrib(vertexattribute1);
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

    protected void backgroundImpl()
    {
        flush();
        PGL pgl1 = pgl;
        float f = backgroundR;
        float f1 = backgroundG;
        float f2 = backgroundB;
        float f3 = backgroundA;
        boolean flag;
        if(!hints[5])
            flag = true;
        else
            flag = false;
        pgl1.clearBackground(f, f1, f2, f3, flag);
        loaded = false;
    }

    protected void backgroundImpl(PImage pimage)
    {
        backgroundImpl();
        set(0, 0, pimage);
        backgroundA = 1.0F;
        loaded = false;
    }

    protected void begin2D()
    {
    }

    protected void beginBindFramebuffer(int i, int j)
    {
    }

    public void beginCamera()
    {
        if(manipulatingCamera)
        {
            throw new RuntimeException("beginCamera() cannot be called again before endCamera()");
        } else
        {
            manipulatingCamera = true;
            return;
        }
    }

    public void beginContour()
    {
        if(openContour)
        {
            PGraphics.showWarning("Already called beginContour()");
        } else
        {
            openContour = true;
            breakShape = true;
        }
    }

    public void beginDraw()
    {
        if(primaryGraphics)
        {
            if(!initialized)
                initPrimary();
            setCurrentPG(this);
        } else
        {
            pgl.getGL(getPrimaryPGL());
            getPrimaryPG().setCurrentPG(this);
        }
        if(pgl.threadIsCurrent()) goto _L2; else goto _L1
_L1:
        PGraphics.showWarning("You are trying to draw outside OpenGL's animation thread.\nPlace all drawing commands in the draw() function, or inside\nyour own functions as long as they are called from draw(),\nbut not in event handling functions such as keyPressed()\nor mousePressed().");
_L4:
        return;
_L2:
        report("top beginDraw()");
        if(checkGLThread() && !drawing)
        {
            if(!primaryGraphics && getPrimaryPG().texCache.containsTexture(this))
                getPrimaryPG().flush();
            if(!glParamsRead)
                getGLParameters();
            setViewport();
            if(primaryGraphics)
                beginOnscreenDraw();
            else
                beginOffscreenDraw();
            checkSettings();
            drawing = true;
            report("bot beginDraw()");
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected void beginOffscreenDraw()
    {
        if(!initialized)
        {
            initOffscreen();
        } else
        {
            FrameBuffer framebuffer1 = offscreenFramebuffer;
            FrameBuffer framebuffer3 = multisampleFramebuffer;
            boolean flag;
            boolean flag1;
            if(framebuffer1 != null && framebuffer1.contextIsOutdated())
                flag = true;
            else
                flag = false;
            if(framebuffer3 != null && framebuffer3.contextIsOutdated())
                flag1 = true;
            else
                flag1 = false;
            if(flag || flag1)
            {
                restartPGL();
                initOffscreen();
            } else
            {
                swapOffscreenTextures();
            }
        }
        pushFramebuffer();
        if(offscreenMultisample)
        {
            FrameBuffer framebuffer = multisampleFramebuffer;
            if(framebuffer != null)
                setFramebuffer(framebuffer);
        } else
        {
            FrameBuffer framebuffer2 = offscreenFramebuffer;
            if(framebuffer2 != null)
                setFramebuffer(framebuffer2);
        }
        drawPTexture();
        if(clip)
        {
            pgl.enable(PGL.SCISSOR_TEST);
            pgl.scissor(clipRect[0], clipRect[1], clipRect[2], clipRect[3]);
        } else
        {
            pgl.disable(PGL.SCISSOR_TEST);
        }
    }

    protected void beginOnscreenDraw()
    {
        updatePixelSize();
        pgl.beginRender();
        if(drawFramebuffer == null)
            drawFramebuffer = new FrameBuffer(this, pixelWidth, pixelHeight, true);
        drawFramebuffer.setFBO(pgl.getDrawFramebuffer());
        if(readFramebuffer == null)
            readFramebuffer = new FrameBuffer(this, pixelWidth, pixelHeight, true);
        readFramebuffer.setFBO(pgl.getReadFramebuffer());
        if(currentFramebuffer == null)
            setFramebuffer(drawFramebuffer);
        if(pgl.isFBOBacked())
        {
            texture = pgl.wrapBackTexture(texture);
            ptexture = pgl.wrapFrontTexture(ptexture);
        }
    }

    public PGL beginPGL()
    {
        flush();
        pgl.beginGL();
        return pgl;
    }

    protected void beginPixelsOp(int i)
    {
        if(!primaryGraphics) goto _L2; else goto _L1
_L1:
        if(i != 1) goto _L4; else goto _L3
_L3:
        FrameBuffer framebuffer;
        if(pgl.isFBOBacked() && pgl.isMultisampled())
        {
            pgl.syncBackTexture();
            framebuffer = readFramebuffer;
        } else
        {
            framebuffer = drawFramebuffer;
        }
_L10:
        if(framebuffer != null && framebuffer != getCurrentFB())
        {
            pushFramebuffer();
            setFramebuffer(framebuffer);
            pixOpChangedFB = true;
        }
        if(i != 1) goto _L6; else goto _L5
_L5:
        if(readBufferSupported)
            pgl.readBuffer(getCurrentFB().getDefaultDrawBuffer());
_L8:
        pixelsOp = i;
        return;
_L4:
        if(i != 2)
            break; /* Loop/switch isn't completed */
        framebuffer = drawFramebuffer;
        continue; /* Loop/switch isn't completed */
_L2:
        FrameBuffer framebuffer1 = offscreenFramebuffer;
        FrameBuffer framebuffer2 = multisampleFramebuffer;
        if(i == 1)
        {
            framebuffer = framebuffer1;
            if(offscreenMultisample)
            {
                int j = PGL.COLOR_BUFFER_BIT;
                int k = j;
                if(hints[10])
                    k = j | (PGL.DEPTH_BUFFER_BIT | PGL.STENCIL_BUFFER_BIT);
                framebuffer = framebuffer1;
                if(framebuffer1 != null)
                {
                    framebuffer = framebuffer1;
                    if(framebuffer2 != null)
                    {
                        framebuffer2.copy(framebuffer1, k);
                        framebuffer = framebuffer1;
                    }
                }
            }
        } else
        {
            if(i != 2)
                break; /* Loop/switch isn't completed */
            if(offscreenMultisample)
                framebuffer = framebuffer2;
            else
                framebuffer = framebuffer1;
        }
        continue; /* Loop/switch isn't completed */
_L6:
        if(i == 2 && drawBufferSupported)
            pgl.drawBuffer(getCurrentFB().getDefaultDrawBuffer());
        if(true) goto _L8; else goto _L7
_L7:
        framebuffer = null;
        if(true) goto _L10; else goto _L9
_L9:
    }

    protected void beginReadPixels()
    {
        beginPixelsOp(1);
    }

    public void beginShape(int i)
    {
        shape = i;
        inGeo.clear();
        curveVertexCount = 0;
        breakShape = false;
        defaultEdges = true;
        super.noTexture();
        normalMode = 0;
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
        bezierVertexCheck(shape, inGeo.vertexCount);
        inGeo.setMaterial(fillColor, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininess);
        inGeo.setNormal(normalX, normalY, normalZ);
        inGeo.addBezierVertex(f, f1, f2, f3, f4, f5, f6, f7, f8, vertexBreak());
    }

    protected void bindFrontTexture()
    {
        if(primaryGraphics)
        {
            pgl.bindFrontTexture();
        } else
        {
            if(ptexture == null)
                createPTexture();
            ptexture.bind();
        }
    }

    protected void blendModeImpl()
    {
        if(blendMode != lastBlendMode)
            flush();
        pgl.enable(PGL.BLEND);
        if(blendMode != 0) goto _L2; else goto _L1
_L1:
        if(blendEqSupported)
            pgl.blendEquation(PGL.FUNC_ADD);
        pgl.blendFunc(PGL.ONE, PGL.ZERO);
_L4:
        lastBlendMode = blendMode;
        return;
_L2:
        if(blendMode == 1)
        {
            if(blendEqSupported)
                pgl.blendEquationSeparate(PGL.FUNC_ADD, PGL.FUNC_ADD);
            pgl.blendFuncSeparate(PGL.SRC_ALPHA, PGL.ONE_MINUS_SRC_ALPHA, PGL.ONE, PGL.ONE);
        } else
        if(blendMode == 2)
        {
            if(blendEqSupported)
                pgl.blendEquationSeparate(PGL.FUNC_ADD, PGL.FUNC_ADD);
            pgl.blendFuncSeparate(PGL.SRC_ALPHA, PGL.ONE, PGL.ONE, PGL.ONE);
        } else
        if(blendMode == 4)
        {
            if(blendEqSupported)
            {
                pgl.blendEquationSeparate(PGL.FUNC_REVERSE_SUBTRACT, PGL.FUNC_ADD);
                pgl.blendFuncSeparate(PGL.SRC_ALPHA, PGL.ONE, PGL.ONE, PGL.ONE);
            } else
            {
                PGraphics.showWarning("blendMode(%1$s) is not supported by this hardware (or driver)", new Object[] {
                    "SUBTRACT"
                });
            }
        } else
        if(blendMode == 8)
        {
            if(blendEqSupported)
            {
                pgl.blendEquationSeparate(PGL.FUNC_MAX, PGL.FUNC_ADD);
                pgl.blendFuncSeparate(PGL.ONE, PGL.ONE, PGL.ONE, PGL.ONE);
            } else
            {
                PGraphics.showWarning("blendMode(%1$s) is not supported by this hardware (or driver)", new Object[] {
                    "LIGHTEST"
                });
            }
        } else
        if(blendMode == 16)
        {
            if(blendEqSupported)
            {
                pgl.blendEquationSeparate(PGL.FUNC_MIN, PGL.FUNC_ADD);
                pgl.blendFuncSeparate(PGL.ONE, PGL.ONE, PGL.ONE, PGL.ONE);
            } else
            {
                PGraphics.showWarning("blendMode(%1$s) is not supported by this hardware (or driver)", new Object[] {
                    "DARKEST"
                });
            }
        } else
        if(blendMode == 64)
        {
            if(blendEqSupported)
                pgl.blendEquationSeparate(PGL.FUNC_ADD, PGL.FUNC_ADD);
            pgl.blendFuncSeparate(PGL.ONE_MINUS_DST_COLOR, PGL.ONE_MINUS_SRC_COLOR, PGL.ONE, PGL.ONE);
        } else
        if(blendMode == 128)
        {
            if(blendEqSupported)
                pgl.blendEquationSeparate(PGL.FUNC_ADD, PGL.FUNC_ADD);
            pgl.blendFuncSeparate(PGL.ZERO, PGL.SRC_COLOR, PGL.ONE, PGL.ONE);
        } else
        if(blendMode == 256)
        {
            if(blendEqSupported)
                pgl.blendEquationSeparate(PGL.FUNC_ADD, PGL.FUNC_ADD);
            pgl.blendFuncSeparate(PGL.ONE_MINUS_DST_COLOR, PGL.ONE, PGL.ONE, PGL.ONE);
        } else
        if(blendMode == 32)
            PGraphics.showWarning("blendMode(%1$s) is not supported by this renderer", new Object[] {
                "DIFFERENCE"
            });
        else
        if(blendMode == 512)
            PGraphics.showWarning("blendMode(%1$s) is not supported by this renderer", new Object[] {
                "OVERLAY"
            });
        else
        if(blendMode == 1024)
            PGraphics.showWarning("blendMode(%1$s) is not supported by this renderer", new Object[] {
                "HARD_LIGHT"
            });
        else
        if(blendMode == 2048)
            PGraphics.showWarning("blendMode(%1$s) is not supported by this renderer", new Object[] {
                "SOFT_LIGHT"
            });
        else
        if(blendMode == 4096)
            PGraphics.showWarning("blendMode(%1$s) is not supported by this renderer", new Object[] {
                "DODGE"
            });
        else
        if(blendMode == 8192)
            PGraphics.showWarning("blendMode(%1$s) is not supported by this renderer", new Object[] {
                "BURN"
            });
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void box(float f, float f1, float f2)
    {
        beginShape(17);
        defaultEdges = false;
        normalMode = 2;
        inGeo.setMaterial(fillColor, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininess);
        inGeo.addBox(f, f1, f2, fill, stroke);
        endShape();
    }

    public void camera()
    {
        camera(cameraX, cameraY, cameraZ, cameraX, cameraY, 0.0F, 0.0F, 1.0F, 0.0F);
    }

    public void camera(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8)
    {
        float f9 = f - f3;
        float f10 = f1 - f4;
        float f11 = f2 - f5;
        eyeDist = PApplet.sqrt(f9 * f9 + f10 * f10 + f11 * f11);
        f5 = f9;
        f4 = f10;
        f3 = f11;
        if(nonZero(eyeDist))
        {
            f5 = f9 / eyeDist;
            f4 = f10 / eyeDist;
            f3 = f11 / eyeDist;
        }
        f10 = f7 * f3 - f8 * f4;
        f11 = f8 * f5 + -f6 * f3;
        f9 = f6 * f4 - f7 * f5;
        float f12 = f4 * f9 - f3 * f11;
        float f13 = -f5 * f9 + f3 * f10;
        float f14 = f5 * f11 - f4 * f10;
        float f15 = PApplet.sqrt(f10 * f10 + f11 * f11 + f9 * f9);
        f8 = f10;
        f7 = f11;
        f6 = f9;
        if(nonZero(f15))
        {
            f8 = f10 / f15;
            f7 = f11 / f15;
            f6 = f9 / f15;
        }
        f15 = PApplet.sqrt(f12 * f12 + f13 * f13 + f14 * f14);
        f9 = f12;
        f11 = f13;
        f10 = f14;
        if(nonZero(f15))
        {
            f9 = f12 / f15;
            f11 = f13 / f15;
            f10 = f14 / f15;
        }
        modelview.set(f8, f7, f6, 0.0F, f9, f11, f10, 0.0F, f5, f4, f3, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
        f = -f;
        f1 = -f1;
        f2 = -f2;
        modelview.translate(f, f1, f2);
        modelviewInv.set(modelview);
        modelviewInv.invert();
        camera.set(modelview);
        cameraInv.set(modelviewInv);
        updateProjmodelview();
    }

    public boolean canDraw()
    {
        return pgl.canDraw();
    }

    protected boolean checkGLThread()
    {
        boolean flag;
        if(pgl.threadIsCurrent())
        {
            flag = true;
        } else
        {
            PGraphics.showWarning("Cannot run the OpenGL renderer outside the main thread, change your code\nso the drawing calls are all inside the main thread, \nor use the default renderer instead.");
            flag = false;
        }
        return flag;
    }

    protected void checkSettings()
    {
        super.checkSettings();
        setGLSettings();
    }

    protected void checkTexture(Texture texture1)
    {
        if(!texture1.colorBuffer() && (texture1.usingMipmaps == hints[8] || texture1.currentSampling() != textureSampling))
            if(hints[8])
                texture1.usingMipmaps(false, textureSampling);
            else
                texture1.usingMipmaps(true, textureSampling);
        if(texture1.usingRepeat && textureWrap == 0 || !texture1.usingRepeat && textureWrap == 1)
            if(textureWrap == 0)
                texture1.usingRepeat(false);
            else
                texture1.usingRepeat(true);
    }

    protected void clipImpl(float f, float f1, float f2, float f3)
    {
        flush();
        pgl.enable(PGL.SCISSOR_TEST);
        f3 -= f1;
        clipRect[0] = (int)f;
        clipRect[1] = (int)((float)height - f1 - f3);
        clipRect[2] = (int)(f2 - f);
        clipRect[3] = (int)f3;
        pgl.scissor(clipRect[0], clipRect[1], clipRect[2], clipRect[3]);
        clip = true;
    }

    public void copy(int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1)
    {
        if(primaryGraphics)
            pgl.enableFBOLayer();
        loadTexture();
        if(filterTexture == null || filterTexture.contextIsOutdated())
        {
            filterTexture = new Texture(this, texture.width, texture.height, texture.getParameters());
            filterTexture.invertedY(true);
            filterImage = wrapTexture(filterTexture);
        }
        filterTexture.put(texture, i, height - (j + l), k, height - j);
        copy(filterImage, i, j, k, l, i1, j1, k1, l1);
    }

    public void copy(PImage pimage, int i, int j, int k, int l, int i1, int j1, 
            int k1, int l1)
    {
        boolean flag;
        boolean flag1;
        boolean flag2;
        if(!drawing)
        {
            beginDraw();
            flag = true;
        } else
        {
            flag = false;
        }
        flush();
        pimage = getTexture(pimage);
        flag1 = pimage.invertedX();
        flag2 = pimage.invertedY();
        if(flag1)
        {
            k1 = i1 + k1;
        } else
        {
            int j2 = i1 + k1;
            k1 = i1;
            i1 = j2;
        }
        if(flag2)
        {
            int i2 = height - (j1 + l1);
            l1 = height - j1;
            j1 = ((Texture) (pimage)).height - (j + l);
            j = ((Texture) (pimage)).height - j;
            l = i2;
        } else
        {
            int k2 = height - j1;
            l1 = height - (j1 + l1);
            l = j + l;
            j1 = j;
            j = l;
            l = k2;
        }
        pgl.drawTexture(((Texture) (pimage)).glTarget, ((Texture) (pimage)).glName, ((Texture) (pimage)).glWidth, ((Texture) (pimage)).glHeight, 0, 0, width, height, i, j1, i + k, j, k1, l, i1, l1);
        if(flag)
            endDraw();
    }

    protected void createLineBuffers()
    {
        if(!lineBuffersCreated || lineBufferContextIsOutdated())
        {
            lineBuffersContext = pgl.getCurrentContext();
            bufLineVertex = new VertexBuffer(this, PGL.ARRAY_BUFFER, 3, PGL.SIZEOF_FLOAT);
            bufLineColor = new VertexBuffer(this, PGL.ARRAY_BUFFER, 1, PGL.SIZEOF_INT);
            bufLineAttrib = new VertexBuffer(this, PGL.ARRAY_BUFFER, 4, PGL.SIZEOF_FLOAT);
            pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
            bufLineIndex = new VertexBuffer(this, PGL.ELEMENT_ARRAY_BUFFER, 1, PGL.SIZEOF_INDEX, true);
            pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, 0);
            lineBuffersCreated = true;
        }
    }

    protected PGL createPGL(PGraphicsOpenGL pgraphicsopengl)
    {
        return new PGLES(pgraphicsopengl);
    }

    protected void createPTexture()
    {
        updatePixelSize();
        if(texture != null)
        {
            ptexture = new Texture(this, pixelWidth, pixelHeight, texture.getParameters());
            ptexture.invertedY(true);
            ptexture.colorBuffer(true);
        }
    }

    protected void createPointBuffers()
    {
        if(!pointBuffersCreated || pointBuffersContextIsOutdated())
        {
            pointBuffersContext = pgl.getCurrentContext();
            bufPointVertex = new VertexBuffer(this, PGL.ARRAY_BUFFER, 3, PGL.SIZEOF_FLOAT);
            bufPointColor = new VertexBuffer(this, PGL.ARRAY_BUFFER, 1, PGL.SIZEOF_INT);
            bufPointAttrib = new VertexBuffer(this, PGL.ARRAY_BUFFER, 2, PGL.SIZEOF_FLOAT);
            pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
            bufPointIndex = new VertexBuffer(this, PGL.ELEMENT_ARRAY_BUFFER, 1, PGL.SIZEOF_INDEX, true);
            pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, 0);
            pointBuffersCreated = true;
        }
    }

    protected void createPolyBuffers()
    {
        if(!polyBuffersCreated || polyBuffersContextIsOutdated())
        {
            polyBuffersContext = pgl.getCurrentContext();
            bufPolyVertex = new VertexBuffer(this, PGL.ARRAY_BUFFER, 3, PGL.SIZEOF_FLOAT);
            bufPolyColor = new VertexBuffer(this, PGL.ARRAY_BUFFER, 1, PGL.SIZEOF_INT);
            bufPolyNormal = new VertexBuffer(this, PGL.ARRAY_BUFFER, 3, PGL.SIZEOF_FLOAT);
            bufPolyTexcoord = new VertexBuffer(this, PGL.ARRAY_BUFFER, 2, PGL.SIZEOF_FLOAT);
            bufPolyAmbient = new VertexBuffer(this, PGL.ARRAY_BUFFER, 1, PGL.SIZEOF_INT);
            bufPolySpecular = new VertexBuffer(this, PGL.ARRAY_BUFFER, 1, PGL.SIZEOF_INT);
            bufPolyEmissive = new VertexBuffer(this, PGL.ARRAY_BUFFER, 1, PGL.SIZEOF_INT);
            bufPolyShininess = new VertexBuffer(this, PGL.ARRAY_BUFFER, 1, PGL.SIZEOF_FLOAT);
            pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
            bufPolyIndex = new VertexBuffer(this, PGL.ELEMENT_ARRAY_BUFFER, 1, PGL.SIZEOF_INDEX, true);
            pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, 0);
            polyBuffersCreated = true;
        }
        Iterator iterator = polyAttribs.keySet().iterator();
        boolean flag = false;
        do
        {
            if(!iterator.hasNext())
                break;
            Object obj = (String)iterator.next();
            obj = (VertexAttribute)polyAttribs.get(obj);
            if(!((VertexAttribute) (obj)).bufferCreated() || polyBuffersContextIsOutdated())
            {
                ((VertexAttribute) (obj)).createBuffer(pgl);
                flag = true;
            }
        } while(true);
        if(flag)
            pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    protected PShape createShapeFamily(int i)
    {
        PShapeOpenGL pshapeopengl = new PShapeOpenGL(this, i);
        if(is3D())
            pshapeopengl.set3D(true);
        return pshapeopengl;
    }

    protected transient PShape createShapePrimitive(int i, float af[])
    {
        af = new PShapeOpenGL(this, i, af);
        if(is3D())
            af.set3D(true);
        return af;
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
        curveVertexCheck(shape);
        inGeo.setMaterial(fillColor, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininess);
        inGeo.setNormal(normalX, normalY, normalZ);
        inGeo.addCurveVertex(f, f1, f2, vertexBreak());
    }

    protected void defaultCamera()
    {
        camera();
    }

    protected void defaultPerspective()
    {
        perspective();
    }

    protected void defaultSettings()
    {
        super.defaultSettings();
        manipulatingCamera = false;
        textureMode(2);
        ambient(255);
        specular(125);
        emissive(0);
        shininess(1.0F);
        setAmbient = false;
    }

    protected void deleteSurfaceTextures()
    {
        if(texture != null)
            texture.dispose();
        if(ptexture != null)
            ptexture.dispose();
        if(filterTexture != null)
            filterTexture.dispose();
    }

    public void directionalLight(float f, float f1, float f2, float f3, float f4, float f5)
    {
        enableLighting();
        if(lightCount == PGL.MAX_LIGHTS)
        {
            throw new RuntimeException((new StringBuilder()).append("can only create ").append(PGL.MAX_LIGHTS).append(" lights").toString());
        } else
        {
            lightType[lightCount] = 1;
            lightPosition(lightCount, 0.0F, 0.0F, 0.0F, true);
            lightNormal(lightCount, f3, f4, f5);
            noLightAmbient(lightCount);
            lightDiffuse(lightCount, f, f1, f2);
            lightSpecular(lightCount, currentLightSpecular[0], currentLightSpecular[1], currentLightSpecular[2]);
            noLightSpot(lightCount);
            noLightFalloff(lightCount);
            lightCount = lightCount + 1;
            return;
        }
    }

    protected void disableLighting()
    {
        flush();
        lights = false;
    }

    public void dispose()
    {
        if(asyncPixelReader != null)
        {
            asyncPixelReader.dispose();
            asyncPixelReader = null;
        }
        if(!primaryGraphics)
        {
            deleteSurfaceTextures();
            FrameBuffer framebuffer = offscreenFramebuffer;
            FrameBuffer framebuffer1 = multisampleFramebuffer;
            if(framebuffer != null)
                framebuffer.dispose();
            if(framebuffer1 != null)
                framebuffer1.dispose();
        }
        pgl.dispose();
        super.dispose();
    }

    protected void drawPTexture()
    {
        if(ptexture != null)
        {
            pgl.disable(PGL.BLEND);
            pgl.drawTexture(ptexture.glTarget, ptexture.glName, ptexture.glWidth, ptexture.glHeight, 0, 0, width, height);
            pgl.enable(PGL.BLEND);
        }
    }

    protected void drawPixels(int i, int j, int k, int l)
    {
        boolean flag;
        int i1;
        int j1;
        flag = false;
        i1 = (int)pgl.getPixelScale();
        j1 = i1 * k * i1 * l;
        if(nativePixels == null || nativePixels.length < j1)
        {
            nativePixels = new int[j1];
            nativePixelBuffer = PGL.allocateIntBuffer(nativePixels);
        }
        if(i > 0 || j > 0) goto _L2; else goto _L1
_L1:
        if(k >= width && l >= height) goto _L3; else goto _L2
_L2:
        int l1 = width;
        int j2;
        j1 = i1 * j;
        l1 = i1 * (l1 * j + i);
        j2 = 0;
_L5:
        if(j1 >= (j + l) * i1)
            break; /* Loop/switch isn't completed */
        System.arraycopy(pixels, l1, nativePixels, j2, i1 * k);
        l1 += width * i1;
        j2 += i1 * k;
        j1++;
        if(true) goto _L5; else goto _L4
_L3:
        PApplet.arrayCopy(pixels, 0, nativePixels, 0, j1);
_L4:
        PGL.javaToNativeARGB(nativePixels, i1 * k, i1 * l);
_L7:
        boolean flag1;
label0:
        {
            PGL.putIntArray(nativePixelBuffer, nativePixels);
            if(primaryGraphics && !pgl.isFBOBacked())
                loadTextureImpl(2, false);
            if(!primaryGraphics || pgl.isFBOBacked() && (!pgl.isFBOBacked() || !pgl.isMultisampled()))
            {
                flag1 = flag;
                if(!offscreenMultisample)
                    break label0;
            }
            flag1 = true;
        }
        if(texture != null)
            if(flag1)
            {
                int i2 = PApplet.min(texture.glWidth - i1 * i, i1 * k);
                int k1 = PApplet.min(texture.glHeight - i1 * j, i1 * l);
                pgl.copyToTexture(texture.glTarget, texture.glFormat, texture.glName, i1 * i, i1 * j, i2, k1, nativePixelBuffer);
                beginPixelsOp(2);
                drawTexture(i, j, k, l);
                endPixelsOp();
            } else
            {
                pgl.copyToTexture(texture.glTarget, texture.glFormat, texture.glName, i1 * i, (height - (j + l)) * i1, i1 * k, i1 * l, nativePixelBuffer);
            }
        return;
        ArrayIndexOutOfBoundsException arrayindexoutofboundsexception;
        arrayindexoutofboundsexception;
        if(true) goto _L7; else goto _L6
_L6:
    }

    protected void drawTexture()
    {
        if(texture != null)
        {
            pgl.disable(PGL.BLEND);
            pgl.drawTexture(texture.glTarget, texture.glName, texture.glWidth, texture.glHeight, 0, 0, width, height);
            pgl.enable(PGL.BLEND);
        }
    }

    protected void drawTexture(int i, int j, int k, int l)
    {
        if(texture != null)
        {
            pgl.disable(PGL.BLEND);
            pgl.drawTexture(texture.glTarget, texture.glName, texture.glWidth, texture.glHeight, 0, 0, width, height, i, j, i + k, j + l, i, height - (j + l), i + k, height - j);
            pgl.enable(PGL.BLEND);
        }
    }

    public void ellipseImpl(float f, float f1, float f2, float f3)
    {
        beginShape(11);
        defaultEdges = false;
        normalMode = 1;
        inGeo.setMaterial(fillColor, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininess);
        inGeo.setNormal(normalX, normalY, normalZ);
        inGeo.addEllipse(f, f1, f2, f3, fill, stroke);
        endShape();
    }

    protected void enableLighting()
    {
        flush();
        lights = true;
    }

    protected void end2D()
    {
    }

    protected void endBindFramebuffer(int i, int j)
    {
        FrameBuffer framebuffer = getCurrentFB();
        if(j == 0 && framebuffer != null && framebuffer.glFbo != 0)
            framebuffer.bind();
    }

    public void endCamera()
    {
        if(!manipulatingCamera)
        {
            throw new RuntimeException("Cannot call endCamera() without first calling beginCamera()");
        } else
        {
            camera.set(modelview);
            cameraInv.set(modelviewInv);
            manipulatingCamera = false;
            return;
        }
    }

    public void endContour()
    {
        if(!openContour)
            PGraphics.showWarning("Need to call beginContour() first");
        else
            openContour = false;
    }

    public void endDraw()
    {
        report("top endDraw()");
        if(drawing)
        {
            flush();
            if(primaryGraphics)
                endOnscreenDraw();
            else
                endOffscreenDraw();
            if(primaryGraphics)
                setCurrentPG(null);
            else
                getPrimaryPG().setCurrentPG();
            drawing = false;
            report("bot endDraw()");
        }
    }

    protected void endOffscreenDraw()
    {
        if(offscreenMultisample)
        {
            FrameBuffer framebuffer = offscreenFramebuffer;
            FrameBuffer framebuffer1 = multisampleFramebuffer;
            if(framebuffer != null && framebuffer1 != null)
                framebuffer1.copyColor(framebuffer);
        }
        popFramebuffer();
        if(backgroundA == 1.0F)
        {
            pgl.colorMask(false, false, false, true);
            pgl.clearColor(0.0F, 0.0F, 0.0F, backgroundA);
            pgl.clear(PGL.COLOR_BUFFER_BIT);
            pgl.colorMask(true, true, true, true);
        }
        if(texture != null)
            texture.updateTexels();
        getPrimaryPG().restoreGL();
    }

    protected void endOnscreenDraw()
    {
        pgl.endRender(parent.sketchWindowColor());
    }

    public void endPGL()
    {
        pgl.endGL();
        restoreGL();
    }

    protected void endPixelsOp()
    {
        if(pixOpChangedFB)
        {
            popFramebuffer();
            pixOpChangedFB = false;
        }
        if(readBufferSupported)
            pgl.readBuffer(getCurrentFB().getDefaultReadBuffer());
        if(drawBufferSupported)
            pgl.drawBuffer(getCurrentFB().getDefaultDrawBuffer());
        pixelsOp = 0;
    }

    protected void endReadPixels()
    {
        endPixelsOp();
    }

    public void endShape(int i)
    {
        tessellate(i);
        if(flushMode == 0 || flushMode == 1 && tessGeo.isFull())
            flush();
        else
            loaded = false;
    }

    protected void endShape(int ai[])
    {
        if(shape != 8 && shape != 9)
            throw new RuntimeException("Indices and edges can only be set for TRIANGLE shapes");
        tessellate(ai);
        if(flushMode == 0 || flushMode == 1 && tessGeo.isFull())
            flush();
        else
            loaded = false;
    }

    protected void fillFromCalc()
    {
        super.fillFromCalc();
        if(!setAmbient)
        {
            ambientFromCalc();
            setAmbient = false;
        }
    }

    public void filter(int i)
    {
        PImage pimage = get();
        pimage.filter(i);
        set(0, 0, pimage);
    }

    public void filter(int i, float f)
    {
        PImage pimage = get();
        pimage.filter(i, f);
        set(0, 0, pimage);
    }

    public void filter(PShader pshader)
    {
        if(pshader.isPolyShader()) goto _L2; else goto _L1
_L1:
        PGraphics.showWarning("Your shader cannot be used as a filter because is of type POINT or LINES");
_L4:
        return;
_L2:
        boolean flag;
        boolean flag1;
        int i;
        boolean flag2;
        int j;
        PShader pshader1;
        if(primaryGraphics)
        {
            pgl.enableFBOLayer();
            flag = false;
        } else
        if(!drawing)
        {
            beginDraw();
            flag = true;
        } else
        {
            flag = false;
        }
        loadTexture();
        if(filterTexture == null || filterTexture.contextIsOutdated())
        {
            filterTexture = new Texture(this, texture.width, texture.height, texture.getParameters());
            filterTexture.invertedY(true);
            filterImage = wrapTexture(filterTexture);
        }
        filterTexture.set(texture);
        pgl.depthMask(false);
        pgl.disable(PGL.DEPTH_TEST);
        begin2D();
        flag1 = lights;
        lights = false;
        i = textureMode;
        textureMode = 1;
        flag2 = stroke;
        stroke = false;
        j = blendMode;
        blendMode(0);
        pshader1 = polyShader;
        polyShader = pshader;
        beginShape(17);
        texture(filterImage);
        vertex(0.0F, 0.0F, 0.0F, 0.0F);
        vertex(width, 0.0F, 1.0F, 0.0F);
        vertex(width, height, 1.0F, 1.0F);
        vertex(0.0F, height, 0.0F, 1.0F);
        endShape();
        end2D();
        polyShader = pshader1;
        stroke = flag2;
        lights = flag1;
        textureMode = i;
        blendMode(j);
        if(!hints[2])
            pgl.enable(PGL.DEPTH_TEST);
        if(!hints[5])
            pgl.depthMask(true);
        if(flag)
            endDraw();
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void flush()
    {
        PMatrix3D pmatrix3d = null;
        boolean flag;
        boolean flag1;
        boolean flag2;
        boolean flag3;
        if(tessGeo.polyVertexCount > 0 && tessGeo.polyIndexCount > 0)
            flag = true;
        else
            flag = false;
        if(tessGeo.lineVertexCount > 0 && tessGeo.lineIndexCount > 0)
            flag1 = true;
        else
            flag1 = false;
        if(tessGeo.pointVertexCount > 0 && tessGeo.pointIndexCount > 0)
            flag2 = true;
        else
            flag2 = false;
        if(modified && pixels != null)
            flag3 = true;
        else
            flag3 = false;
        if(flag3)
            flushPixels();
        if(flag2 || flag1 || flag)
        {
            PMatrix3D pmatrix3d1;
            if(flushMode == 1)
            {
                pmatrix3d1 = modelview;
                pmatrix3d = modelviewInv;
                PMatrix3D pmatrix3d2 = identity;
                modelviewInv = pmatrix3d2;
                modelview = pmatrix3d2;
                projmodelview.set(projection);
            } else
            {
                pmatrix3d1 = null;
            }
            if(flag && !isDepthSortingEnabled)
            {
                flushPolys();
                if(raw != null)
                    rawPolys();
            }
            if(is3D())
            {
                if(flag1)
                {
                    flushLines();
                    if(raw != null)
                        rawLines();
                }
                if(flag2)
                {
                    flushPoints();
                    if(raw != null)
                        rawPoints();
                }
            }
            if(flag && isDepthSortingEnabled)
            {
                flushSortedPolys();
                if(raw != null)
                    rawSortedPolys();
            }
            if(flushMode == 1)
            {
                modelview = pmatrix3d1;
                modelviewInv = pmatrix3d;
                updateProjmodelview();
            }
            loaded = false;
        }
        tessGeo.clear();
        texCache.clear();
    }

    protected void flushLines()
    {
        updateLineBuffers();
        PShader pshader = getLineShader();
        pshader.bind();
        IndexCache indexcache = tessGeo.lineIndexCache;
        for(int i = 0; i < indexcache.size; i++)
        {
            int j = indexcache.indexOffset[i];
            int k = indexcache.indexCount[i];
            int l = indexcache.vertexOffset[i];
            pshader.setVertexAttribute(bufLineVertex.glId, 4, PGL.FLOAT, 0, l * 4 * PGL.SIZEOF_FLOAT);
            pshader.setColorAttribute(bufLineColor.glId, 4, PGL.UNSIGNED_BYTE, 0, l * 4 * PGL.SIZEOF_BYTE);
            pshader.setLineAttribute(bufLineAttrib.glId, 4, PGL.FLOAT, 0, l * 4 * PGL.SIZEOF_FLOAT);
            pshader.draw(bufLineIndex.glId, k, j);
        }

        pshader.unbind();
        unbindLineBuffers();
    }

    protected void flushPixels()
    {
        drawPixels(mx1, my1, mx2 - mx1, my2 - my1);
        modified = false;
    }

    protected void flushPoints()
    {
        updatePointBuffers();
        PShader pshader = getPointShader();
        pshader.bind();
        IndexCache indexcache = tessGeo.pointIndexCache;
        for(int i = 0; i < indexcache.size; i++)
        {
            int j = indexcache.indexOffset[i];
            int k = indexcache.indexCount[i];
            int l = indexcache.vertexOffset[i];
            pshader.setVertexAttribute(bufPointVertex.glId, 4, PGL.FLOAT, 0, l * 4 * PGL.SIZEOF_FLOAT);
            pshader.setColorAttribute(bufPointColor.glId, 4, PGL.UNSIGNED_BYTE, 0, l * 4 * PGL.SIZEOF_BYTE);
            pshader.setPointAttribute(bufPointAttrib.glId, 2, PGL.FLOAT, 0, l * 2 * PGL.SIZEOF_FLOAT);
            pshader.draw(bufPointIndex.glId, k, j);
        }

        pshader.unbind();
        unbindPointBuffers();
    }

    protected void flushPolys()
    {
        int i;
        boolean flag;
        boolean flag1;
        if(polyShader != null)
            i = 1;
        else
            i = 0;
        if(i != 0)
            flag = polyShader.accessNormals();
        else
            flag = false;
        if(i != 0)
            flag1 = polyShader.accessTexCoords();
        else
            flag1 = false;
        updatePolyBuffers(lights, texCache.hasTextures, flag, flag1);
        for(i = 0; i < texCache.size; i++)
        {
            Texture texture1 = texCache.getTexture(i);
            boolean flag2 = lights;
            boolean flag3;
            PShader pshader;
            int j;
            int k;
            IndexCache indexcache;
            if(texture1 != null)
                flag3 = true;
            else
                flag3 = false;
            pshader = getPolyShader(flag2, flag3);
            pshader.bind();
            j = texCache.firstCache[i];
            k = texCache.lastCache[i];
            indexcache = tessGeo.polyIndexCache;
            for(int l = j; l <= k; l++)
            {
                int i1;
                int j1;
                int k1;
                Iterator iterator;
                if(l == j)
                    i1 = texCache.firstIndex[i];
                else
                    i1 = indexcache.indexOffset[l];
                if(l == k)
                    j1 = (texCache.lastIndex[i] - i1) + 1;
                else
                    j1 = (indexcache.indexOffset[l] + indexcache.indexCount[l]) - i1;
                k1 = indexcache.vertexOffset[l];
                pshader.setVertexAttribute(bufPolyVertex.glId, 4, PGL.FLOAT, 0, k1 * 4 * PGL.SIZEOF_FLOAT);
                pshader.setColorAttribute(bufPolyColor.glId, 4, PGL.UNSIGNED_BYTE, 0, k1 * 4 * PGL.SIZEOF_BYTE);
                if(lights)
                {
                    pshader.setNormalAttribute(bufPolyNormal.glId, 3, PGL.FLOAT, 0, k1 * 3 * PGL.SIZEOF_FLOAT);
                    pshader.setAmbientAttribute(bufPolyAmbient.glId, 4, PGL.UNSIGNED_BYTE, 0, k1 * 4 * PGL.SIZEOF_BYTE);
                    pshader.setSpecularAttribute(bufPolySpecular.glId, 4, PGL.UNSIGNED_BYTE, 0, k1 * 4 * PGL.SIZEOF_BYTE);
                    pshader.setEmissiveAttribute(bufPolyEmissive.glId, 4, PGL.UNSIGNED_BYTE, 0, k1 * 4 * PGL.SIZEOF_BYTE);
                    pshader.setShininessAttribute(bufPolyShininess.glId, 1, PGL.FLOAT, 0, PGL.SIZEOF_FLOAT * k1);
                }
                if(lights || flag)
                    pshader.setNormalAttribute(bufPolyNormal.glId, 3, PGL.FLOAT, 0, k1 * 3 * PGL.SIZEOF_FLOAT);
                if(texture1 != null || flag1)
                {
                    pshader.setTexcoordAttribute(bufPolyTexcoord.glId, 2, PGL.FLOAT, 0, k1 * 2 * PGL.SIZEOF_FLOAT);
                    pshader.setTexture(texture1);
                }
                iterator = polyAttribs.values().iterator();
                do
                {
                    if(!iterator.hasNext())
                        break;
                    VertexAttribute vertexattribute1 = (VertexAttribute)iterator.next();
                    if(vertexattribute1.active(pshader))
                    {
                        vertexattribute1.bind(pgl);
                        pshader.setAttributeVBO(vertexattribute1.glLoc, vertexattribute1.buf.glId, vertexattribute1.tessSize, vertexattribute1.type, vertexattribute1.isColor(), 0, vertexattribute1.sizeInBytes(k1));
                    }
                } while(true);
                pshader.draw(bufPolyIndex.glId, j1, i1);
            }

            Iterator iterator1 = polyAttribs.values().iterator();
            do
            {
                if(!iterator1.hasNext())
                    break;
                VertexAttribute vertexattribute = (VertexAttribute)iterator1.next();
                if(vertexattribute.active(pshader))
                    vertexattribute.unbind(pgl);
            } while(true);
            pshader.unbind();
        }

        unbindPolyBuffers();
    }

    protected void flushSortedPolys()
    {
        int i;
        boolean flag;
        boolean flag1;
        int j;
        int ai[];
        int ai1[];
        int ai2[];
        if(polyShader != null)
            i = 1;
        else
            i = 0;
        if(i != 0)
            flag = polyShader.accessNormals();
        else
            flag = false;
        if(i != 0)
            flag1 = polyShader.accessTexCoords();
        else
            flag1 = false;
        sorter.sort(tessGeo);
        j = tessGeo.polyIndexCount / 3;
        ai = sorter.texMap;
        ai1 = sorter.voffsetMap;
        ai2 = tessGeo.polyIndexCache.vertexOffset;
        updatePolyBuffers(lights, texCache.hasTextures, flag, flag1);
        int i1;
        for(i = 0; i < j; i = i1)
        {
            int k = ai[i];
            int l = ai1[i];
            for(i1 = i; ++i1 < j && k == ai[i1] && l == ai1[i1];);
            Object obj = texCache.getTexture(k);
            k = ai2[l];
            boolean flag2 = lights;
            boolean flag3;
            PShader pshader;
            Iterator iterator;
            if(obj != null)
                flag3 = true;
            else
                flag3 = false;
            pshader = getPolyShader(flag2, flag3);
            pshader.bind();
            pshader.setVertexAttribute(bufPolyVertex.glId, 4, PGL.FLOAT, 0, k * 4 * PGL.SIZEOF_FLOAT);
            pshader.setColorAttribute(bufPolyColor.glId, 4, PGL.UNSIGNED_BYTE, 0, k * 4 * PGL.SIZEOF_BYTE);
            if(lights)
            {
                pshader.setNormalAttribute(bufPolyNormal.glId, 3, PGL.FLOAT, 0, k * 3 * PGL.SIZEOF_FLOAT);
                pshader.setAmbientAttribute(bufPolyAmbient.glId, 4, PGL.UNSIGNED_BYTE, 0, k * 4 * PGL.SIZEOF_BYTE);
                pshader.setSpecularAttribute(bufPolySpecular.glId, 4, PGL.UNSIGNED_BYTE, 0, k * 4 * PGL.SIZEOF_BYTE);
                pshader.setEmissiveAttribute(bufPolyEmissive.glId, 4, PGL.UNSIGNED_BYTE, 0, k * 4 * PGL.SIZEOF_BYTE);
                pshader.setShininessAttribute(bufPolyShininess.glId, 1, PGL.FLOAT, 0, PGL.SIZEOF_FLOAT * k);
            }
            if(lights || flag)
                pshader.setNormalAttribute(bufPolyNormal.glId, 3, PGL.FLOAT, 0, k * 3 * PGL.SIZEOF_FLOAT);
            if(obj != null || flag1)
            {
                pshader.setTexcoordAttribute(bufPolyTexcoord.glId, 2, PGL.FLOAT, 0, k * 2 * PGL.SIZEOF_FLOAT);
                pshader.setTexture(((Texture) (obj)));
            }
            iterator = polyAttribs.values().iterator();
            do
            {
                if(!iterator.hasNext())
                    break;
                obj = (VertexAttribute)iterator.next();
                if(((VertexAttribute) (obj)).active(pshader))
                {
                    ((VertexAttribute) (obj)).bind(pgl);
                    pshader.setAttributeVBO(((VertexAttribute) (obj)).glLoc, ((VertexAttribute) (obj)).buf.glId, ((VertexAttribute) (obj)).tessSize, ((VertexAttribute) (obj)).type, ((VertexAttribute) (obj)).isColor(), 0, ((VertexAttribute) (obj)).sizeInBytes(k));
                }
            } while(true);
            pshader.draw(bufPolyIndex.glId, (i1 - i) * 3, i * 3);
            obj = polyAttribs.values().iterator();
            do
            {
                if(!((Iterator) (obj)).hasNext())
                    break;
                VertexAttribute vertexattribute = (VertexAttribute)((Iterator) (obj)).next();
                if(vertexattribute.active(pshader))
                    vertexattribute.unbind(pgl);
            } while(true);
            pshader.unbind();
        }

        unbindPolyBuffers();
    }

    public void frustum(float f, float f1, float f2, float f3, float f4, float f5)
    {
        flush();
        float f6 = 2.0F * f4;
        float f7 = f1 - f;
        float f8 = f3 - f2;
        float f9 = f5 - f4;
        projection.set(f6 / f7, 0.0F, (f1 + f) / f7, 0.0F, 0.0F, -f6 / f8, (f3 + f2) / f8, 0.0F, 0.0F, 0.0F, -(f5 + f4) / f9, -(f6 * f5) / f9, 0.0F, 0.0F, -1F, 0.0F);
        updateProjmodelview();
    }

    public int get(int i, int j)
    {
        loadPixels();
        return super.get(i, j);
    }

    public Object getCache(PImage pimage)
    {
        Object obj = getPrimaryPG().cacheMap.get(pimage);
        pimage = ((PImage) (obj));
        if(obj != null)
        {
            pimage = ((PImage) (obj));
            if(obj.getClass() == java/lang/ref/WeakReference)
                pimage = ((PImage) (((WeakReference)obj).get()));
        }
        return pimage;
    }

    protected FrameBuffer getCurrentFB()
    {
        return getPrimaryPG().currentFramebuffer;
    }

    protected PGraphicsOpenGL getCurrentPG()
    {
        return currentPG;
    }

    protected FontTexture getFontTexture(PFont pfont)
    {
        return (FontTexture)getPrimaryPG().fontMap.get(pfont);
    }

    public FrameBuffer getFrameBuffer()
    {
        return getFrameBuffer(false);
    }

    public FrameBuffer getFrameBuffer(boolean flag)
    {
        FrameBuffer framebuffer;
        if(flag)
            framebuffer = multisampleFramebuffer;
        else
            framebuffer = offscreenFramebuffer;
        return framebuffer;
    }

    protected void getGLParameters()
    {
        OPENGL_VENDOR = pgl.getString(PGL.VENDOR);
        OPENGL_RENDERER = pgl.getString(PGL.RENDERER);
        OPENGL_VERSION = pgl.getString(PGL.VERSION);
        OPENGL_EXTENSIONS = pgl.getString(PGL.EXTENSIONS);
        GLSL_VERSION = pgl.getString(PGL.SHADING_LANGUAGE_VERSION);
        npotTexSupported = pgl.hasNpotTexSupport();
        autoMipmapGenSupported = pgl.hasAutoMipmapGenSupport();
        fboMultisampleSupported = pgl.hasFboMultisampleSupport();
        packedDepthStencilSupported = pgl.hasPackedDepthStencilSupport();
        anisoSamplingSupported = pgl.hasAnisoSamplingSupport();
        readBufferSupported = pgl.hasReadBuffer();
        drawBufferSupported = pgl.hasDrawBuffer();
        try
        {
            pgl.blendEquation(PGL.FUNC_ADD);
            blendEqSupported = true;
        }
        catch(Exception exception)
        {
            blendEqSupported = false;
        }
        depthBits = pgl.getDepthBits();
        stencilBits = pgl.getStencilBits();
        pgl.getIntegerv(PGL.MAX_TEXTURE_SIZE, intBuffer);
        maxTextureSize = intBuffer.get(0);
        if(!OPENGL_RENDERER.equals("VideoCore IV HW"))
        {
            pgl.getIntegerv(PGL.MAX_SAMPLES, intBuffer);
            maxSamples = intBuffer.get(0);
        }
        if(anisoSamplingSupported)
        {
            pgl.getFloatv(PGL.MAX_TEXTURE_MAX_ANISOTROPY, floatBuffer);
            maxAnisoAmount = floatBuffer.get(0);
        }
        if(OPENGL_RENDERER.equals("VideoCore IV HW") || OPENGL_RENDERER.equals("Gallium 0.4 on VC4"))
        {
            defLightShaderVertURL = processing/opengl/PGraphicsOpenGL.getResource("/processing/opengl/shaders/LightVert-vc4.glsl");
            defTexlightShaderVertURL = processing/opengl/PGraphicsOpenGL.getResource("/processing/opengl/shaders/TexLightVert-vc4.glsl");
        }
        glParamsRead = true;
    }

    protected boolean getHint(int i)
    {
        boolean flag;
        if(i > 0)
            flag = hints[i];
        else
        if(!hints[-i])
            flag = true;
        else
            flag = false;
        return flag;
    }

    protected void getImpl(int i, int j, int k, int l, PImage pimage, int i1, int j1)
    {
        loadPixels();
        super.getImpl(i, j, k, l, pimage, i1, j1);
    }

    protected PShader getLineShader()
    {
        PGraphicsOpenGL pgraphicsopengl = getPrimaryPG();
        PShader pshader;
        if(lineShader == null)
        {
            if(pgraphicsopengl.defLineShader == null)
            {
                String as[] = pgl.loadVertexShader(defLineShaderVertURL);
                String as1[] = pgl.loadFragmentShader(defLineShaderFragURL);
                pgraphicsopengl.defLineShader = new PShader(parent, as, as1);
            }
            pshader = pgraphicsopengl.defLineShader;
        } else
        {
            pshader = lineShader;
        }
        pshader.setRenderer(this);
        pshader.loadAttributes();
        pshader.loadUniforms();
        return pshader;
    }

    public PMatrix3D getMatrix(PMatrix3D pmatrix3d)
    {
        PMatrix3D pmatrix3d1 = pmatrix3d;
        if(pmatrix3d == null)
            pmatrix3d1 = new PMatrix3D();
        pmatrix3d1.set(modelview);
        return pmatrix3d1;
    }

    public PMatrix getMatrix()
    {
        return modelview.get();
    }

    protected PShader getPointShader()
    {
        PGraphicsOpenGL pgraphicsopengl = getPrimaryPG();
        PShader pshader;
        if(pointShader == null)
        {
            if(pgraphicsopengl.defPointShader == null)
            {
                String as[] = pgl.loadVertexShader(defPointShaderVertURL);
                String as1[] = pgl.loadFragmentShader(defPointShaderFragURL);
                pgraphicsopengl.defPointShader = new PShader(parent, as, as1);
            }
            pshader = pgraphicsopengl.defPointShader;
        } else
        {
            pshader = pointShader;
        }
        pshader.setRenderer(this);
        pshader.loadAttributes();
        pshader.loadUniforms();
        return pshader;
    }

    protected PShader getPolyShader(boolean flag, boolean flag1)
    {
        Object obj = getPrimaryPG();
        boolean flag2;
        if(polyShader == null)
            flag2 = true;
        else
            flag2 = false;
        if(polyShader != null)
        {
            polyShader.setRenderer(this);
            polyShader.loadAttributes();
            polyShader.loadUniforms();
        }
        if(flag)
        {
            if(flag1)
            {
                if(flag2 || !polyShader.checkPolyType(6))
                {
                    if(((PGraphicsOpenGL) (obj)).defTexlightShader == null)
                    {
                        String as[] = pgl.loadVertexShader(defTexlightShaderVertURL);
                        String as4[] = pgl.loadFragmentShader(defTexlightShaderFragURL);
                        obj.defTexlightShader = new PShader(parent, as, as4);
                    }
                    obj = ((PGraphicsOpenGL) (obj)).defTexlightShader;
                } else
                {
                    obj = polyShader;
                }
            } else
            if(flag2 || !polyShader.checkPolyType(4))
            {
                if(((PGraphicsOpenGL) (obj)).defLightShader == null)
                {
                    String as1[] = pgl.loadVertexShader(defLightShaderVertURL);
                    String as5[] = pgl.loadFragmentShader(defLightShaderFragURL);
                    obj.defLightShader = new PShader(parent, as1, as5);
                }
                obj = ((PGraphicsOpenGL) (obj)).defLightShader;
            } else
            {
                obj = polyShader;
            }
        } else
        {
            boolean flag3 = flag2;
            if(polyShader != null)
            {
                flag3 = flag2;
                if(polyShader.accessLightAttribs())
                {
                    PGraphics.showWarning("The provided shader needs light attributes (ambient, diffuse, etc.), but the current scene is unlit, so the default shader will be used instead");
                    flag3 = true;
                }
            }
            if(flag1)
            {
                if(flag3 || !polyShader.checkPolyType(5))
                {
                    if(((PGraphicsOpenGL) (obj)).defTextureShader == null)
                    {
                        String as6[] = pgl.loadVertexShader(defTextureShaderVertURL);
                        String as2[] = pgl.loadFragmentShader(defTextureShaderFragURL);
                        obj.defTextureShader = new PShader(parent, as6, as2);
                    }
                    obj = ((PGraphicsOpenGL) (obj)).defTextureShader;
                } else
                {
                    obj = polyShader;
                }
            } else
            if(flag3 || !polyShader.checkPolyType(3))
            {
                if(((PGraphicsOpenGL) (obj)).defColorShader == null)
                {
                    String as3[] = pgl.loadVertexShader(defColorShaderVertURL);
                    String as7[] = pgl.loadFragmentShader(defColorShaderFragURL);
                    obj.defColorShader = new PShader(parent, as3, as7);
                }
                obj = ((PGraphicsOpenGL) (obj)).defColorShader;
            } else
            {
                obj = polyShader;
            }
        }
        if(obj != polyShader)
        {
            ((PShader) (obj)).setRenderer(this);
            ((PShader) (obj)).loadAttributes();
            ((PShader) (obj)).loadUniforms();
        }
        return ((PShader) (obj));
    }

    protected PGraphicsOpenGL getPrimaryPG()
    {
        PGraphicsOpenGL pgraphicsopengl;
        if(primaryGraphics)
            pgraphicsopengl = this;
        else
            pgraphicsopengl = (PGraphicsOpenGL)parent.g;
        return pgraphicsopengl;
    }

    protected PGL getPrimaryPGL()
    {
        PGL pgl1;
        if(primaryGraphics)
            pgl1 = pgl;
        else
            pgl1 = ((PGraphicsOpenGL)parent.g).pgl;
        return pgl1;
    }

    public Texture getTexture()
    {
        return getTexture(true);
    }

    public Texture getTexture(PImage pimage)
    {
        Texture texture1 = (Texture)initCache(pimage);
        if(texture1 == null)
        {
            pimage = null;
        } else
        {
            if(pimage.isModified())
            {
                if(pimage.width != texture1.width || pimage.height != texture1.height)
                    texture1.init(pimage.width, pimage.height);
                updateTexture(pimage, texture1);
            }
            if(texture1.hasBuffers())
                texture1.bufferUpdate();
            checkTexture(texture1);
            pimage = texture1;
        }
        return pimage;
    }

    public Texture getTexture(boolean flag)
    {
        if(flag)
            loadTexture();
        return texture;
    }

    protected void handleTextSize(float f)
    {
        Object obj = textFont.getNative();
        if(obj != null)
        {
            obj = pgl.getDerivedFont(obj, f);
            textFont.setNative(obj);
        }
        super.handleTextSize(f);
    }

    public void hint(int i)
    {
        boolean flag;
        flag = hints[PApplet.abs(i)];
        super.hint(i);
        if(flag != hints[PApplet.abs(i)]) goto _L2; else goto _L1
_L1:
        return;
_L2:
        if(i == 2)
        {
            flush();
            pgl.disable(PGL.DEPTH_TEST);
        } else
        if(i == -2)
        {
            flush();
            pgl.enable(PGL.DEPTH_TEST);
        } else
        if(i == 5)
        {
            flush();
            pgl.depthMask(false);
        } else
        if(i == -5)
        {
            flush();
            pgl.depthMask(true);
        } else
        if(i == -6)
        {
            flush();
            setFlushMode(1);
        } else
        if(i == 6)
        {
            if(is2D())
            {
                PGraphics.showWarning("Optimized strokes can only be disabled in 3D");
            } else
            {
                flush();
                setFlushMode(0);
            }
        } else
        if(i == -7)
        {
            if(tessGeo.lineVertexCount > 0 && tessGeo.lineIndexCount > 0)
                flush();
        } else
        if(i == 7)
        {
            if(tessGeo.lineVertexCount > 0 && tessGeo.lineIndexCount > 0)
                flush();
        } else
        if(i == 3)
        {
            if(is3D())
            {
                flush();
                if(sorter == null)
                    sorter = new DepthSorter(this);
                isDepthSortingEnabled = true;
            } else
            {
                PGraphics.showWarning("Depth sorting can only be enabled in 3D");
            }
        } else
        if(i == -3)
        {
            if(is3D())
            {
                flush();
                isDepthSortingEnabled = false;
            }
        } else
        if(i == 10)
            restartPGL();
        else
        if(i == -10)
            restartPGL();
        if(true) goto _L1; else goto _L3
_L3:
    }

    protected Object initCache(PImage pimage)
    {
        if(checkGLThread()) goto _L2; else goto _L1
_L1:
        Texture texture1 = null;
_L4:
        return texture1;
_L2:
        Texture texture2 = (Texture)getCache(pimage);
        if(texture2 != null)
        {
            texture1 = texture2;
            if(!texture2.contextIsOutdated())
                continue; /* Loop/switch isn't completed */
        }
        texture2 = addTexture(pimage);
        texture1 = texture2;
        if(texture2 != null)
        {
            pimage.loadPixels();
            texture2.set(pimage.pixels, pimage.format);
            pimage.setModified();
            texture1 = texture2;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected void initOffscreen()
    {
        loadTextureImpl(textureSampling, false);
        FrameBuffer framebuffer = offscreenFramebuffer;
        FrameBuffer framebuffer1 = multisampleFramebuffer;
        if(framebuffer != null)
            framebuffer.dispose();
        if(framebuffer1 != null)
            framebuffer1.dispose();
        boolean flag;
        if(depthBits == 24 && stencilBits == 8 && packedDepthStencilSupported)
            flag = true;
        else
            flag = false;
        if(fboMultisampleSupported && 1 < PGL.smoothToSamples(smooth))
        {
            framebuffer1 = new FrameBuffer(this, texture.glWidth, texture.glHeight, PGL.smoothToSamples(smooth), 0, depthBits, stencilBits, flag, false);
            framebuffer1.clear();
            multisampleFramebuffer = framebuffer1;
            offscreenMultisample = true;
            if(hints[10])
                framebuffer1 = new FrameBuffer(this, texture.glWidth, texture.glHeight, 1, 1, depthBits, stencilBits, flag, false);
            else
                framebuffer1 = new FrameBuffer(this, texture.glWidth, texture.glHeight, 1, 1, 0, 0, false, false);
        } else
        {
            smooth = 0;
            framebuffer1 = new FrameBuffer(this, texture.glWidth, texture.glHeight, 1, 1, depthBits, stencilBits, flag, false);
            offscreenMultisample = false;
        }
        framebuffer1.setColorBuffer(texture);
        framebuffer1.clear();
        offscreenFramebuffer = framebuffer1;
        initialized = true;
    }

    protected void initPrimary()
    {
        pgl.initSurface(smooth);
        if(texture != null)
        {
            removeCache(this);
            texture = null;
            ptexture = null;
        }
        initialized = true;
    }

    public boolean isGL()
    {
        return true;
    }

    protected void lightAmbient(int i, float f, float f1, float f2)
    {
        colorCalc(f, f1, f2);
        lightAmbient[i * 3 + 0] = calcR;
        lightAmbient[i * 3 + 1] = calcG;
        lightAmbient[i * 3 + 2] = calcB;
    }

    protected void lightDiffuse(int i, float f, float f1, float f2)
    {
        colorCalc(f, f1, f2);
        lightDiffuse[i * 3 + 0] = calcR;
        lightDiffuse[i * 3 + 1] = calcG;
        lightDiffuse[i * 3 + 2] = calcB;
    }

    public void lightFalloff(float f, float f1, float f2)
    {
        currentLightFalloffConstant = f;
        currentLightFalloffLinear = f1;
        currentLightFalloffQuadratic = f2;
    }

    protected void lightFalloff(int i, float f, float f1, float f2)
    {
        lightFalloffCoefficients[i * 3 + 0] = f;
        lightFalloffCoefficients[i * 3 + 1] = f1;
        lightFalloffCoefficients[i * 3 + 2] = f2;
    }

    protected void lightNormal(int i, float f, float f1, float f2)
    {
        float f3 = modelviewInv.m00 * f + modelviewInv.m10 * f1 + modelviewInv.m20 * f2;
        float f4 = modelviewInv.m01 * f + modelviewInv.m11 * f1 + modelviewInv.m21 * f2;
        f = modelviewInv.m02 * f + modelviewInv.m12 * f1 + modelviewInv.m22 * f2;
        f1 = PApplet.dist(0.0F, 0.0F, 0.0F, f3, f4, f);
        if(0.0F < f1)
        {
            f1 = 1.0F / f1;
            lightNormal[i * 3 + 0] = f3 * f1;
            lightNormal[i * 3 + 1] = f1 * f4;
            lightNormal[i * 3 + 2] = f1 * f;
        } else
        {
            lightNormal[i * 3 + 0] = 0.0F;
            lightNormal[i * 3 + 1] = 0.0F;
            lightNormal[i * 3 + 2] = 0.0F;
        }
    }

    protected void lightPosition(int i, float f, float f1, float f2, boolean flag)
    {
        lightPosition[i * 4 + 0] = modelview.m00 * f + modelview.m01 * f1 + modelview.m02 * f2 + modelview.m03;
        lightPosition[i * 4 + 1] = modelview.m10 * f + modelview.m11 * f1 + modelview.m12 * f2 + modelview.m13;
        lightPosition[i * 4 + 2] = modelview.m20 * f + modelview.m21 * f1 + modelview.m22 * f2 + modelview.m23;
        float af[] = lightPosition;
        if(flag)
            f = 0.0F;
        else
            f = 1.0F;
        af[i * 4 + 3] = f;
    }

    public void lightSpecular(float f, float f1, float f2)
    {
        colorCalc(f, f1, f2);
        currentLightSpecular[0] = calcR;
        currentLightSpecular[1] = calcG;
        currentLightSpecular[2] = calcB;
    }

    protected void lightSpecular(int i, float f, float f1, float f2)
    {
        lightSpecular[i * 3 + 0] = f;
        lightSpecular[i * 3 + 1] = f1;
        lightSpecular[i * 3 + 2] = f2;
    }

    protected void lightSpot(int i, float f, float f1)
    {
        lightSpotParameters[i * 2 + 0] = Math.max(0.0F, PApplet.cos(f));
        lightSpotParameters[i * 2 + 1] = f1;
    }

    public void lights()
    {
        enableLighting();
        lightCount = 0;
        int i = colorMode;
        colorMode = 1;
        lightFalloff(1.0F, 0.0F, 0.0F);
        lightSpecular(0.0F, 0.0F, 0.0F);
        ambientLight(colorModeX * 0.5F, colorModeY * 0.5F, colorModeZ * 0.5F);
        directionalLight(colorModeX * 0.5F, colorModeY * 0.5F, 0.5F * colorModeZ, 0.0F, 0.0F, -1F);
        colorMode = i;
    }

    public void line(float f, float f1, float f2, float f3)
    {
        lineImpl(f, f1, 0.0F, f2, f3, 0.0F);
    }

    public void line(float f, float f1, float f2, float f3, float f4, float f5)
    {
        lineImpl(f, f1, f2, f3, f4, f5);
    }

    protected boolean lineBufferContextIsOutdated()
    {
        boolean flag;
        if(!pgl.contextIsCurrent(lineBuffersContext))
            flag = true;
        else
            flag = false;
        return flag;
    }

    protected void lineImpl(float f, float f1, float f2, float f3, float f4, float f5)
    {
        beginShape(5);
        defaultEdges = false;
        normalMode = 1;
        inGeo.setMaterial(fillColor, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininess);
        inGeo.setNormal(normalX, normalY, normalZ);
        inGeo.addLine(f, f1, f2, f3, f4, f5, fill, stroke);
        endShape();
    }

    public void loadPixels()
    {
        if(!primaryGraphics || !sized) goto _L2; else goto _L1
_L1:
        return;
_L2:
        boolean flag = false;
        if(!drawing)
        {
            beginDraw();
            flag = true;
        }
        if(!loaded)
            flush();
        allocatePixels();
        if(!loaded)
            readPixels();
        loaded = true;
        if(flag)
            endDraw();
        if(true) goto _L1; else goto _L3
_L3:
    }

    public PShader loadShader(String s)
    {
        if(s == null || s.equals(""))
        {
            PGraphics.showWarning("The fragment shader is missing, cannot create shader object");
            s = null;
        } else
        {
            int i = PShader.getShaderType(parent.loadStrings(s), 2);
            PShader pshader = new PShader(parent);
            pshader.setType(i);
            pshader.setFragmentShader(s);
            if(i == 0)
            {
                pshader.setVertexShader(pgl.loadVertexShader(defPointShaderVertURL));
                s = pshader;
            } else
            if(i == 1)
            {
                pshader.setVertexShader(pgl.loadVertexShader(defLineShaderVertURL));
                s = pshader;
            } else
            if(i == 6)
            {
                pshader.setVertexShader(pgl.loadVertexShader(defTexlightShaderVertURL));
                s = pshader;
            } else
            if(i == 4)
            {
                pshader.setVertexShader(pgl.loadVertexShader(defLightShaderVertURL));
                s = pshader;
            } else
            if(i == 5)
            {
                pshader.setVertexShader(pgl.loadVertexShader(defTextureShaderVertURL));
                s = pshader;
            } else
            if(i == 3)
            {
                pshader.setVertexShader(pgl.loadVertexShader(defColorShaderVertURL));
                s = pshader;
            } else
            {
                pshader.setVertexShader(pgl.loadVertexShader(defTextureShaderVertURL));
                s = pshader;
            }
        }
        return s;
    }

    public PShader loadShader(String s, String s1)
    {
        Object obj = null;
        if(s == null || s.equals(""))
        {
            PGraphics.showWarning("The fragment shader is missing, cannot create shader object");
            s = obj;
        } else
        if(s1 == null || s1.equals(""))
        {
            PGraphics.showWarning("The vertex shader is missing, cannot create shader object");
            s = obj;
        } else
        {
            s = new PShader(parent, s1, s);
        }
        return s;
    }

    public PShape loadShape(String s)
    {
        String s1 = PApplet.getExtension(s);
        if(PGraphics2D.isSupportedExtension(s1))
            s = PGraphics2D.loadShapeImpl(this, s, s1);
        else
        if(PGraphics3D.isSupportedExtension(s1))
        {
            s = PGraphics3D.loadShapeImpl(this, s, s1);
        } else
        {
            PGraphics.showWarning("Unsupported shape format");
            s = null;
        }
        return s;
    }

    public void loadTexture()
    {
        boolean flag;
        FrameBuffer framebuffer;
        FrameBuffer framebuffer1;
        IndexOutOfBoundsException indexoutofboundsexception;
        if(!drawing)
        {
            beginDraw();
            flag = true;
        } else
        {
            flag = false;
        }
        flush();
        if(!primaryGraphics) goto _L2; else goto _L1
_L1:
        updatePixelSize();
        if(!pgl.isFBOBacked()) goto _L4; else goto _L3
_L3:
        pgl.syncBackTexture();
_L6:
        if(flag)
            endDraw();
        return;
_L4:
        loadTextureImpl(2, false);
        if(nativePixels == null || nativePixels.length < pixelWidth * pixelHeight)
        {
            nativePixels = new int[pixelWidth * pixelHeight];
            nativePixelBuffer = PGL.allocateIntBuffer(nativePixels);
        }
        beginPixelsOp(1);
        try
        {
            pgl.readPixelsImpl(0, 0, pixelWidth, pixelHeight, PGL.RGBA, PGL.UNSIGNED_BYTE, nativePixelBuffer);
        }
        // Misplaced declaration of an exception variable
        catch(IndexOutOfBoundsException indexoutofboundsexception) { }
        endPixelsOp();
        if(texture != null)
            texture.setNative(nativePixelBuffer, 0, 0, pixelWidth, pixelHeight);
        continue; /* Loop/switch isn't completed */
_L2:
        if(offscreenMultisample)
        {
            framebuffer = offscreenFramebuffer;
            framebuffer1 = multisampleFramebuffer;
            if(framebuffer != null && framebuffer1 != null)
                framebuffer1.copyColor(framebuffer);
        }
        if(true) goto _L6; else goto _L5
_L5:
    }

    protected void loadTextureImpl(int i, boolean flag)
    {
        updatePixelSize();
        break MISSING_BLOCK_LABEL_4;
        if(pixelWidth != 0 && pixelHeight != 0 && (texture == null || texture.contextIsOutdated()))
        {
            Texture.Parameters parameters = new Texture.Parameters(2, i, flag);
            texture = new Texture(this, pixelWidth, pixelHeight, parameters);
            texture.invertedY(true);
            texture.colorBuffer(true);
            setCache(this, texture);
        }
        return;
    }

    public void mask(PImage pimage)
    {
        updatePixelSize();
        if(pimage.width != pixelWidth || pimage.height != pixelHeight)
            throw new RuntimeException("The PImage used with mask() must be the same size as the applet.");
        PGraphicsOpenGL pgraphicsopengl = getPrimaryPG();
        if(pgraphicsopengl.maskShader == null)
            pgraphicsopengl.maskShader = new PShader(parent, defTextureShaderVertURL, maskShaderFragURL);
        pgraphicsopengl.maskShader.set("mask", pimage);
        filter(pgraphicsopengl.maskShader);
    }

    public float modelX(float f, float f1, float f2)
    {
        float f3 = modelview.m00;
        float f4 = modelview.m01;
        float f5 = modelview.m02;
        f4 = modelview.m03 + (f3 * f + f4 * f1 + f5 * f2);
        f5 = modelview.m10;
        f3 = modelview.m11;
        float f6 = modelview.m12;
        f5 = modelview.m13 + (f5 * f + f3 * f1 + f6 * f2);
        float f7 = modelview.m20;
        f6 = modelview.m21;
        f3 = modelview.m22;
        f3 = modelview.m23 + (f7 * f + f6 * f1 + f3 * f2);
        f7 = modelview.m30;
        f6 = modelview.m31;
        float f8 = modelview.m32;
        f = modelview.m33 + (f7 * f + f6 * f1 + f8 * f2);
        f1 = cameraInv.m00 * f4 + cameraInv.m01 * f5 + cameraInv.m02 * f3 + cameraInv.m03 * f;
        f2 = f4 * cameraInv.m30 + f5 * cameraInv.m31 + cameraInv.m32 * f3 + cameraInv.m33 * f;
        f = f1;
        if(nonZero(f2))
            f = f1 / f2;
        return f;
    }

    public float modelY(float f, float f1, float f2)
    {
        float f3 = modelview.m00;
        float f4 = modelview.m01;
        float f5 = modelview.m02;
        f4 = modelview.m03 + (f3 * f + f4 * f1 + f5 * f2);
        f5 = modelview.m10;
        f3 = modelview.m11;
        float f6 = modelview.m12;
        f3 = modelview.m13 + (f5 * f + f3 * f1 + f6 * f2);
        float f7 = modelview.m20;
        f6 = modelview.m21;
        f5 = modelview.m22;
        f5 = modelview.m23 + (f7 * f + f6 * f1 + f5 * f2);
        float f8 = modelview.m30;
        f7 = modelview.m31;
        f6 = modelview.m32;
        f = modelview.m33 + (f8 * f + f7 * f1 + f6 * f2);
        f1 = cameraInv.m10 * f4 + cameraInv.m11 * f3 + cameraInv.m12 * f5 + cameraInv.m13 * f;
        f2 = f4 * cameraInv.m30 + f3 * cameraInv.m31 + cameraInv.m32 * f5 + cameraInv.m33 * f;
        f = f1;
        if(nonZero(f2))
            f = f1 / f2;
        return f;
    }

    public float modelZ(float f, float f1, float f2)
    {
        float f3 = modelview.m00;
        float f4 = modelview.m01;
        float f5 = modelview.m02;
        f4 = modelview.m03 + (f3 * f + f4 * f1 + f5 * f2);
        f5 = modelview.m10;
        f3 = modelview.m11;
        float f6 = modelview.m12;
        f5 = modelview.m13 + (f5 * f + f3 * f1 + f6 * f2);
        float f7 = modelview.m20;
        f6 = modelview.m21;
        f3 = modelview.m22;
        f3 = modelview.m23 + (f7 * f + f6 * f1 + f3 * f2);
        f6 = modelview.m30;
        float f8 = modelview.m31;
        f7 = modelview.m32;
        f = modelview.m33 + (f6 * f + f8 * f1 + f7 * f2);
        f1 = cameraInv.m20 * f4 + cameraInv.m21 * f5 + cameraInv.m22 * f3 + cameraInv.m23 * f;
        f2 = f4 * cameraInv.m30 + f5 * cameraInv.m31 + cameraInv.m32 * f3 + cameraInv.m33 * f;
        f = f1;
        if(nonZero(f2))
            f = f1 / f2;
        return f;
    }

    public void noClip()
    {
        if(clip)
        {
            flush();
            pgl.disable(PGL.SCISSOR_TEST);
            clip = false;
        }
    }

    protected void noLightAmbient(int i)
    {
        lightAmbient[i * 3 + 0] = 0.0F;
        lightAmbient[i * 3 + 1] = 0.0F;
        lightAmbient[i * 3 + 2] = 0.0F;
    }

    protected void noLightDiffuse(int i)
    {
        lightDiffuse[i * 3 + 0] = 0.0F;
        lightDiffuse[i * 3 + 1] = 0.0F;
        lightDiffuse[i * 3 + 2] = 0.0F;
    }

    protected void noLightFalloff(int i)
    {
        lightFalloffCoefficients[i * 3 + 0] = 1.0F;
        lightFalloffCoefficients[i * 3 + 1] = 0.0F;
        lightFalloffCoefficients[i * 3 + 2] = 0.0F;
    }

    protected void noLightSpecular(int i)
    {
        lightSpecular[i * 3 + 0] = 0.0F;
        lightSpecular[i * 3 + 1] = 0.0F;
        lightSpecular[i * 3 + 2] = 0.0F;
    }

    protected void noLightSpot(int i)
    {
        lightSpotParameters[i * 2 + 0] = 0.0F;
        lightSpotParameters[i * 2 + 1] = 0.0F;
    }

    public void noLights()
    {
        disableLighting();
        lightCount = 0;
    }

    protected boolean nonOrthoProjection()
    {
        boolean flag;
        if(nonZero(projection.m01) || nonZero(projection.m02) || nonZero(projection.m10) || nonZero(projection.m12) || nonZero(projection.m20) || nonZero(projection.m21) || nonZero(projection.m30) || nonZero(projection.m31) || nonZero(projection.m32) || diff(projection.m33, 1.0F))
            flag = true;
        else
            flag = false;
        return flag;
    }

    public void ortho()
    {
        ortho((float)(-width) / 2.0F, (float)width / 2.0F, (float)(-height) / 2.0F, (float)height / 2.0F, 0.0F, 10F * eyeDist);
    }

    public void ortho(float f, float f1, float f2, float f3)
    {
        ortho(f, f1, f2, f3, 0.0F, eyeDist * 10F);
    }

    public void ortho(float f, float f1, float f2, float f3, float f4, float f5)
    {
        float f6 = f1 - f;
        float f7 = f3 - f2;
        float f8 = f5 - f4;
        flush();
        float f9 = 2.0F / f6;
        float f10 = 2.0F / f7;
        float f11 = -2F / f8;
        f = -(f1 + f) / f6;
        f1 = -(f3 + f2) / f7;
        f2 = -(f5 + f4) / f8;
        projection.set(f9, 0.0F, 0.0F, f, 0.0F, -f10, 0.0F, f1, 0.0F, 0.0F, f11, f2, 0.0F, 0.0F, 0.0F, 1.0F);
        updateProjmodelview();
    }

    protected boolean orthoProjection()
    {
        boolean flag;
        if(zero(projection.m01) && zero(projection.m02) && zero(projection.m10) && zero(projection.m12) && zero(projection.m20) && zero(projection.m21) && zero(projection.m30) && zero(projection.m31) && zero(projection.m32) && same(projection.m33, 1.0F))
            flag = true;
        else
            flag = false;
        return flag;
    }

    public void perspective()
    {
        perspective(cameraFOV, cameraAspect, cameraNear, cameraFar);
    }

    public void perspective(float f, float f1, float f2, float f3)
    {
        f = f2 * (float)Math.tan(f / 2.0F);
        float f4 = -f;
        frustum(f4 * f1, f * f1, f4, f, f2, f3);
    }

    public void point(float f, float f1)
    {
        pointImpl(f, f1, 0.0F);
    }

    public void point(float f, float f1, float f2)
    {
        pointImpl(f, f1, f2);
    }

    protected boolean pointBuffersContextIsOutdated()
    {
        boolean flag;
        if(!pgl.contextIsCurrent(pointBuffersContext))
            flag = true;
        else
            flag = false;
        return flag;
    }

    protected void pointImpl(float f, float f1, float f2)
    {
        beginShape(3);
        defaultEdges = false;
        normalMode = 1;
        inGeo.setMaterial(fillColor, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininess);
        inGeo.setNormal(normalX, normalY, normalZ);
        inGeo.addPoint(f, f1, f2, fill, stroke);
        endShape();
    }

    public void pointLight(float f, float f1, float f2, float f3, float f4, float f5)
    {
        enableLighting();
        if(lightCount == PGL.MAX_LIGHTS)
        {
            throw new RuntimeException((new StringBuilder()).append("can only create ").append(PGL.MAX_LIGHTS).append(" lights").toString());
        } else
        {
            lightType[lightCount] = 2;
            lightPosition(lightCount, f3, f4, f5, false);
            lightNormal(lightCount, 0.0F, 0.0F, 0.0F);
            noLightAmbient(lightCount);
            lightDiffuse(lightCount, f, f1, f2);
            lightSpecular(lightCount, currentLightSpecular[0], currentLightSpecular[1], currentLightSpecular[2]);
            noLightSpot(lightCount);
            lightFalloff(lightCount, currentLightFalloffConstant, currentLightFalloffLinear, currentLightFalloffQuadratic);
            lightCount = lightCount + 1;
            return;
        }
    }

    protected boolean polyBuffersContextIsOutdated()
    {
        boolean flag;
        if(!pgl.contextIsCurrent(polyBuffersContext))
            flag = true;
        else
            flag = false;
        return flag;
    }

    protected void popFramebuffer()
    {
        PGraphicsOpenGL pgraphicsopengl = getPrimaryPG();
        if(pgraphicsopengl.fbStackDepth == 0)
            throw new RuntimeException("popFramebuffer call is unbalanced.");
        pgraphicsopengl.fbStackDepth = pgraphicsopengl.fbStackDepth - 1;
        FrameBuffer framebuffer = pgraphicsopengl.fbStack[pgraphicsopengl.fbStackDepth];
        if(pgraphicsopengl.currentFramebuffer != framebuffer)
        {
            pgraphicsopengl.currentFramebuffer.finish();
            pgraphicsopengl.currentFramebuffer = framebuffer;
            if(pgraphicsopengl.currentFramebuffer != null)
                pgraphicsopengl.currentFramebuffer.bind();
        }
    }

    public void popMatrix()
    {
        if(modelviewStackDepth == 0)
        {
            throw new RuntimeException("Too many calls to popMatrix(), and not enough to pushMatrix().");
        } else
        {
            modelviewStackDepth = modelviewStackDepth - 1;
            modelview.set(modelviewStack[modelviewStackDepth]);
            modelviewInv.set(modelviewInvStack[modelviewStackDepth]);
            camera.set(cameraStack[modelviewStackDepth]);
            cameraInv.set(cameraInvStack[modelviewStackDepth]);
            updateProjmodelview();
            return;
        }
    }

    public void popProjection()
    {
        flush();
        if(projectionStackDepth == 0)
        {
            throw new RuntimeException("Too many calls to popMatrix(), and not enough to pushMatrix().");
        } else
        {
            projectionStackDepth = projectionStackDepth - 1;
            projection.set(projectionStack[projectionStackDepth]);
            updateProjmodelview();
            return;
        }
    }

    public void popStyle()
    {
        boolean flag = setAmbient;
        super.popStyle();
        if(!flag)
            setAmbient = false;
    }

    public void printCamera()
    {
        camera.print();
    }

    public void printMatrix()
    {
        modelview.print();
    }

    public void printProjection()
    {
        projection.print();
    }

    protected void processImageBeforeAsyncSave(PImage pimage)
    {
        if(pimage.format != -1) goto _L2; else goto _L1
_L1:
        PGL.nativeToJavaARGB(pimage.pixels, pimage.width, pimage.height);
        pimage.format = 2;
_L4:
        return;
_L2:
        if(pimage.format == -2)
        {
            PGL.nativeToJavaRGB(pimage.pixels, pimage.width, pimage.height);
            pimage.format = 1;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected void pushFramebuffer()
    {
        PGraphicsOpenGL pgraphicsopengl = getPrimaryPG();
        if(pgraphicsopengl.fbStackDepth == 16)
        {
            throw new RuntimeException("Too many pushFramebuffer calls");
        } else
        {
            pgraphicsopengl.fbStack[pgraphicsopengl.fbStackDepth] = pgraphicsopengl.currentFramebuffer;
            pgraphicsopengl.fbStackDepth = pgraphicsopengl.fbStackDepth + 1;
            return;
        }
    }

    public void pushMatrix()
    {
        if(modelviewStackDepth == 32)
        {
            throw new RuntimeException("Too many calls to pushMatrix().");
        } else
        {
            modelview.get(modelviewStack[modelviewStackDepth]);
            modelviewInv.get(modelviewInvStack[modelviewStackDepth]);
            camera.get(cameraStack[modelviewStackDepth]);
            cameraInv.get(cameraInvStack[modelviewStackDepth]);
            modelviewStackDepth = modelviewStackDepth + 1;
            return;
        }
    }

    public void pushProjection()
    {
        if(projectionStackDepth == 32)
        {
            throw new RuntimeException("Too many calls to pushMatrix().");
        } else
        {
            projection.get(projectionStack[projectionStackDepth]);
            projectionStackDepth = projectionStackDepth + 1;
            return;
        }
    }

    public void quad(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7)
    {
        beginShape(17);
        defaultEdges = false;
        normalMode = 1;
        inGeo.setMaterial(fillColor, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininess);
        inGeo.setNormal(normalX, normalY, normalZ);
        inGeo.addQuad(f, f1, 0.0F, f2, f3, 0.0F, f4, f5, 0.0F, f6, f7, 0.0F, stroke);
        endShape();
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
        bezierVertexCheck(shape, inGeo.vertexCount);
        inGeo.setMaterial(fillColor, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininess);
        inGeo.setNormal(normalX, normalY, normalZ);
        inGeo.addQuadraticVertex(f, f1, f2, f3, f4, f5, vertexBreak());
    }

    void rawLines()
    {
        raw.colorMode(1);
        raw.noFill();
        raw.strokeCap(strokeCap);
        raw.strokeJoin(strokeJoin);
        raw.beginShape(5);
        float af[] = tessGeo.lineVertices;
        int ai[] = tessGeo.lineColors;
        float af1[] = tessGeo.lineDirections;
        short aword0[] = tessGeo.lineIndices;
        IndexCache indexcache = tessGeo.lineIndexCache;
        int i = 0;
        do
        {
            if(i >= indexcache.size)
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
                if(!zero(f))
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
                    int l1 = PGL.nativeToJavaARGB(ai[j1]);
                    int i2 = PGL.nativeToJavaARGB(ai[k1]);
                    if(flushMode == 0)
                    {
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
                        PApplet.arrayCopy(af, j1 * 4, af4, 0, 4);
                        PApplet.arrayCopy(af, k1 * 4, af5, 0, 4);
                        modelview.mult(af4, af2);
                        modelview.mult(af5, af3);
                    } else
                    {
                        PApplet.arrayCopy(af, j1 * 4, af2, 0, 4);
                        PApplet.arrayCopy(af, k1 * 4, af3, 0, 4);
                    }
                    if(raw.is3D())
                    {
                        raw.strokeWeight(f);
                        raw.stroke(l1);
                        raw.vertex(af2[0], af2[1], af2[2]);
                        raw.strokeWeight(f1);
                        raw.stroke(i2);
                        raw.vertex(af3[0], af3[1], af3[2]);
                    } else
                    if(raw.is2D())
                    {
                        float f2 = screenXImpl(af2[0], af2[1], af2[2], af2[3]);
                        float f3 = screenYImpl(af2[0], af2[1], af2[2], af2[3]);
                        float f4 = screenXImpl(af3[0], af3[1], af3[2], af3[3]);
                        float f5 = screenYImpl(af3[0], af3[1], af3[2], af3[3]);
                        raw.strokeWeight(f);
                        raw.stroke(l1);
                        raw.vertex(f2, f3);
                        raw.strokeWeight(f1);
                        raw.stroke(i2);
                        raw.vertex(f4, f5);
                    }
                }
                i1++;
            }
            i++;
        } while(true);
        raw.endShape();
    }

    void rawPoints()
    {
        raw.colorMode(1);
        raw.noFill();
        raw.strokeCap(strokeCap);
        raw.beginShape(3);
        float af[] = tessGeo.pointVertices;
        int ai[] = tessGeo.pointColors;
        float af1[] = tessGeo.pointOffsets;
        short aword0[] = tessGeo.pointIndices;
        IndexCache indexcache = tessGeo.pointIndexCache;
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
                if(flushMode == 0)
                {
                    float af3[] = new float[4];
                    float[] _tmp1 = af3;
                    af3[0] = 0.0F;
                    af3[1] = 0.0F;
                    af3[2] = 0.0F;
                    af3[3] = 0.0F;
                    PApplet.arrayCopy(af, k1 * 4, af3, 0, 4);
                    modelview.mult(af3, af2);
                } else
                {
                    PApplet.arrayCopy(af, k1 * 4, af2, 0, 4);
                }
                if(raw.is3D())
                {
                    raw.strokeWeight(f);
                    raw.stroke(l1);
                    raw.vertex(af2[0], af2[1], af2[2]);
                } else
                if(raw.is2D())
                {
                    float f1 = screenXImpl(af2[0], af2[1], af2[2], af2[3]);
                    float f2 = screenYImpl(af2[0], af2[1], af2[2], af2[3]);
                    raw.strokeWeight(f);
                    raw.stroke(l1);
                    raw.vertex(f1, f2);
                }
                i1 += j1;
            }
            i++;
        } while(true);
        raw.endShape();
    }

    void rawPolys()
    {
        raw.colorMode(1);
        raw.noStroke();
        raw.beginShape(9);
        float af[] = tessGeo.polyVertices;
        int ai[] = tessGeo.polyColors;
        float af1[] = tessGeo.polyTexCoords;
        short aword0[] = tessGeo.polyIndices;
label0:
        for(int i = 0; i < texCache.size; i++)
        {
            PImage pimage = texCache.getTextureImage(i);
            int j = texCache.firstCache[i];
            int k = texCache.lastCache[i];
            IndexCache indexcache = tessGeo.polyIndexCache;
            int l = j;
            do
            {
                if(l > k)
                    continue label0;
                int i1;
                int j1;
                int k1;
                int l1;
                if(l == j)
                    i1 = texCache.firstIndex[i];
                else
                    i1 = indexcache.indexOffset[l];
                if(l == k)
                    j1 = (texCache.lastIndex[i] - i1) + 1;
                else
                    j1 = (indexcache.indexOffset[l] + indexcache.indexCount[l]) - i1;
                k1 = indexcache.vertexOffset[l];
                l1 = i1 / 3;
                while(l1 < (i1 + j1) / 3) 
                {
                    int i2 = k1 + aword0[l1 * 3 + 0];
                    int j2 = k1 + aword0[l1 * 3 + 1];
                    int k2 = k1 + aword0[l1 * 3 + 2];
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
                    int l2 = PGL.nativeToJavaARGB(ai[i2]);
                    int i3 = PGL.nativeToJavaARGB(ai[j2]);
                    int j3 = PGL.nativeToJavaARGB(ai[k2]);
                    if(flushMode == 0)
                    {
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
                        PApplet.arrayCopy(af, i2 * 4, af5, 0, 4);
                        PApplet.arrayCopy(af, j2 * 4, af6, 0, 4);
                        PApplet.arrayCopy(af, k2 * 4, af7, 0, 4);
                        modelview.mult(af5, af2);
                        modelview.mult(af6, af3);
                        modelview.mult(af7, af4);
                    } else
                    {
                        PApplet.arrayCopy(af, i2 * 4, af2, 0, 4);
                        PApplet.arrayCopy(af, j2 * 4, af3, 0, 4);
                        PApplet.arrayCopy(af, k2 * 4, af4, 0, 4);
                    }
                    if(pimage != null)
                    {
                        raw.texture(pimage);
                        if(raw.is3D())
                        {
                            raw.fill(l2);
                            raw.vertex(af2[0], af2[1], af2[2], af1[i2 * 2 + 0], af1[i2 * 2 + 1]);
                            raw.fill(i3);
                            raw.vertex(af3[0], af3[1], af3[2], af1[j2 * 2 + 0], af1[j2 * 2 + 1]);
                            raw.fill(j3);
                            raw.vertex(af4[0], af4[1], af4[2], af1[k2 * 2 + 0], af1[k2 * 2 + 1]);
                        } else
                        if(raw.is2D())
                        {
                            float f = screenXImpl(af2[0], af2[1], af2[2], af2[3]);
                            float f2 = screenYImpl(af2[0], af2[1], af2[2], af2[3]);
                            float f4 = screenXImpl(af3[0], af3[1], af3[2], af3[3]);
                            float f6 = screenYImpl(af3[0], af3[1], af3[2], af3[3]);
                            float f8 = screenXImpl(af4[0], af4[1], af4[2], af4[3]);
                            float f10 = screenYImpl(af4[0], af4[1], af4[2], af4[3]);
                            raw.fill(l2);
                            raw.vertex(f, f2, af1[i2 * 2 + 0], af1[i2 * 2 + 1]);
                            raw.fill(i3);
                            raw.vertex(f4, f6, af1[j2 * 2 + 0], af1[j2 * 2 + 1]);
                            raw.fill(i3);
                            raw.vertex(f8, f10, af1[k2 * 2 + 0], af1[k2 * 2 + 1]);
                        }
                    } else
                    if(raw.is3D())
                    {
                        raw.fill(l2);
                        raw.vertex(af2[0], af2[1], af2[2]);
                        raw.fill(i3);
                        raw.vertex(af3[0], af3[1], af3[2]);
                        raw.fill(j3);
                        raw.vertex(af4[0], af4[1], af4[2]);
                    } else
                    if(raw.is2D())
                    {
                        float f3 = screenXImpl(af2[0], af2[1], af2[2], af2[3]);
                        float f11 = screenYImpl(af2[0], af2[1], af2[2], af2[3]);
                        float f1 = screenXImpl(af3[0], af3[1], af3[2], af3[3]);
                        float f9 = screenYImpl(af3[0], af3[1], af3[2], af3[3]);
                        float f7 = screenXImpl(af4[0], af4[1], af4[2], af4[3]);
                        float f5 = screenYImpl(af4[0], af4[1], af4[2], af4[3]);
                        raw.fill(l2);
                        raw.vertex(f3, f11);
                        raw.fill(i3);
                        raw.vertex(f1, f9);
                        raw.fill(j3);
                        raw.vertex(f7, f5);
                    }
                    l1++;
                }
                l++;
            } while(true);
        }

        raw.endShape();
    }

    void rawSortedPolys()
    {
        raw.colorMode(1);
        raw.noStroke();
        raw.beginShape(9);
        float af[] = tessGeo.polyVertices;
        int ai[] = tessGeo.polyColors;
        float af1[] = tessGeo.polyTexCoords;
        short aword0[] = tessGeo.polyIndices;
        sorter.sort(tessGeo);
        int ai1[] = sorter.triangleIndices;
        int ai2[] = sorter.texMap;
        int ai3[] = sorter.voffsetMap;
        int ai4[] = tessGeo.polyIndexCache.vertexOffset;
        int i = 0;
        while(i < tessGeo.polyIndexCount / 3) 
        {
            int j = ai1[i];
            PImage pimage = texCache.getTextureImage(ai2[j]);
            int k = ai4[ai3[j]];
            int l = k + aword0[j * 3 + 0];
            int i1 = k + aword0[j * 3 + 1];
            int j1 = k + aword0[j * 3 + 2];
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
            j = PGL.nativeToJavaARGB(ai[l]);
            k = PGL.nativeToJavaARGB(ai[i1]);
            int k1 = PGL.nativeToJavaARGB(ai[j1]);
            if(flushMode == 0)
            {
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
                PApplet.arrayCopy(af, l * 4, af5, 0, 4);
                PApplet.arrayCopy(af, i1 * 4, af6, 0, 4);
                PApplet.arrayCopy(af, j1 * 4, af7, 0, 4);
                modelview.mult(af5, af2);
                modelview.mult(af6, af3);
                modelview.mult(af7, af4);
            } else
            {
                PApplet.arrayCopy(af, l * 4, af2, 0, 4);
                PApplet.arrayCopy(af, i1 * 4, af3, 0, 4);
                PApplet.arrayCopy(af, j1 * 4, af4, 0, 4);
            }
            if(pimage != null)
            {
                raw.texture(pimage);
                if(raw.is3D())
                {
                    raw.fill(j);
                    raw.vertex(af2[0], af2[1], af2[2], af1[l * 2 + 0], af1[l * 2 + 1]);
                    raw.fill(k);
                    raw.vertex(af3[0], af3[1], af3[2], af1[i1 * 2 + 0], af1[i1 * 2 + 1]);
                    raw.fill(k1);
                    raw.vertex(af4[0], af4[1], af4[2], af1[j1 * 2 + 0], af1[j1 * 2 + 1]);
                } else
                if(raw.is2D())
                {
                    float f = screenXImpl(af2[0], af2[1], af2[2], af2[3]);
                    float f2 = screenYImpl(af2[0], af2[1], af2[2], af2[3]);
                    float f4 = screenXImpl(af3[0], af3[1], af3[2], af3[3]);
                    float f6 = screenYImpl(af3[0], af3[1], af3[2], af3[3]);
                    float f8 = screenXImpl(af4[0], af4[1], af4[2], af4[3]);
                    float f10 = screenYImpl(af4[0], af4[1], af4[2], af4[3]);
                    raw.fill(j);
                    raw.vertex(f, f2, af1[l * 2 + 0], af1[l * 2 + 1]);
                    raw.fill(k);
                    raw.vertex(f4, f6, af1[i1 * 2 + 0], af1[i1 * 2 + 1]);
                    raw.fill(k);
                    raw.vertex(f8, f10, af1[j1 * 2 + 0], af1[j1 * 2 + 1]);
                }
            } else
            if(raw.is3D())
            {
                raw.fill(j);
                raw.vertex(af2[0], af2[1], af2[2]);
                raw.fill(k);
                raw.vertex(af3[0], af3[1], af3[2]);
                raw.fill(k1);
                raw.vertex(af4[0], af4[1], af4[2]);
            } else
            if(raw.is2D())
            {
                float f7 = screenXImpl(af2[0], af2[1], af2[2], af2[3]);
                float f3 = screenYImpl(af2[0], af2[1], af2[2], af2[3]);
                float f11 = screenXImpl(af3[0], af3[1], af3[2], af3[3]);
                float f1 = screenYImpl(af3[0], af3[1], af3[2], af3[3]);
                float f9 = screenXImpl(af4[0], af4[1], af4[2], af4[3]);
                float f5 = screenYImpl(af4[0], af4[1], af4[2], af4[3]);
                raw.fill(j);
                raw.vertex(f7, f3);
                raw.fill(k);
                raw.vertex(f11, f1);
                raw.fill(k1);
                raw.vertex(f9, f5);
            }
            i++;
        }
        raw.endShape();
    }

    protected void readPixels()
    {
        updatePixelSize();
        beginPixelsOp(1);
        ArrayIndexOutOfBoundsException arrayindexoutofboundsexception;
        try
        {
            pgl.readPixelsImpl(0, 0, pixelWidth, pixelHeight, PGL.RGBA, PGL.UNSIGNED_BYTE, pixelBuffer);
        }
        catch(IndexOutOfBoundsException indexoutofboundsexception) { }
        endPixelsOp();
        PGL.getIntArray(pixelBuffer, pixels);
        PGL.nativeToJavaARGB(pixels, pixelWidth, pixelHeight);
_L2:
        return;
        arrayindexoutofboundsexception;
        if(true) goto _L2; else goto _L1
_L1:
    }

    protected void rectImpl(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7)
    {
        beginShape(20);
        defaultEdges = false;
        normalMode = 1;
        inGeo.setMaterial(fillColor, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininess);
        inGeo.setNormal(normalX, normalY, normalZ);
        inGeo.addRect(f, f1, f2, f3, f4, f5, f6, f7, stroke);
        endShape(2);
    }

    public void removeCache(PImage pimage)
    {
        getPrimaryPG().cacheMap.remove(pimage);
    }

    protected void removeFontTexture(PFont pfont)
    {
        getPrimaryPG().fontMap.remove(pfont);
    }

    protected void report(String s)
    {
        if(!hints[4])
        {
            int i = pgl.getError();
            if(i != 0)
            {
                String s1 = pgl.errorString(i);
                PGraphics.showWarning((new StringBuilder()).append("OpenGL error ").append(i).append(" at ").append(s).append(": ").append(s1).toString());
            }
        }
    }

    public void requestDraw()
    {
        if(primaryGraphics)
            if(initialized)
            {
                if(sized)
                    pgl.reinitSurface();
                if(parent.canDraw())
                    pgl.requestDraw();
            } else
            {
                initPrimary();
            }
    }

    public void resetMatrix()
    {
        modelview.reset();
        modelviewInv.reset();
        projmodelview.set(projection);
        camera.reset();
        cameraInv.reset();
    }

    public void resetProjection()
    {
        flush();
        projection.reset();
        updateProjmodelview();
    }

    public void resetShader()
    {
        resetShader(9);
    }

    public void resetShader(int i)
    {
        flush();
        if(i == 9 || i == 17 || i == 20)
            polyShader = null;
        else
        if(i == 5)
            lineShader = null;
        else
        if(i == 3)
            pointShader = null;
        else
            PGraphics.showWarning("Unknown shader kind");
    }

    public void resize(int i, int j)
    {
        PGraphics.showMethodWarning("resize");
    }

    protected void restartPGL()
    {
        initialized = false;
    }

    protected void restoreGL()
    {
        blendMode(blendMode);
        FrameBuffer framebuffer;
        if(hints[2])
            pgl.disable(PGL.DEPTH_TEST);
        else
            pgl.enable(PGL.DEPTH_TEST);
        pgl.depthFunc(PGL.LEQUAL);
        if(smooth < 1)
        {
            pgl.disable(PGL.MULTISAMPLE);
        } else
        {
            pgl.enable(PGL.MULTISAMPLE);
            pgl.disable(PGL.POLYGON_SMOOTH);
        }
        pgl.viewport(viewport.get(0), viewport.get(1), viewport.get(2), viewport.get(3));
        if(clip)
        {
            pgl.enable(PGL.SCISSOR_TEST);
            pgl.scissor(clipRect[0], clipRect[1], clipRect[2], clipRect[3]);
        } else
        {
            pgl.disable(PGL.SCISSOR_TEST);
        }
        pgl.frontFace(PGL.CW);
        pgl.disable(PGL.CULL_FACE);
        pgl.activeTexture(PGL.TEXTURE0);
        if(hints[5])
            pgl.depthMask(false);
        else
            pgl.depthMask(true);
        framebuffer = getCurrentFB();
        if(framebuffer != null)
        {
            framebuffer.bind();
            if(drawBufferSupported)
                pgl.drawBuffer(framebuffer.getDefaultDrawBuffer());
        }
    }

    public void rotate(float f)
    {
        rotateImpl(f, 0.0F, 0.0F, 1.0F);
    }

    public void rotate(float f, float f1, float f2, float f3)
    {
        rotateImpl(f, f1, f2, f3);
    }

    protected void rotateImpl(float f, float f1, float f2, float f3)
    {
        float f4 = f1 * f1 + f2 * f2 + f3 * f3;
        if(!zero(f4))
        {
            float f5 = f1;
            float f6 = f2;
            float f7 = f3;
            if(diff(f4, 1.0F))
            {
                f7 = PApplet.sqrt(f4);
                f5 = f1 / f7;
                f6 = f2 / f7;
                f7 = f3 / f7;
            }
            modelview.rotate(f, f5, f6, f7);
            invRotate(modelviewInv, f, f5, f6, f7);
            updateProjmodelview();
        }
    }

    public void rotateX(float f)
    {
        rotateImpl(f, 1.0F, 0.0F, 0.0F);
    }

    public void rotateY(float f)
    {
        rotateImpl(f, 0.0F, 1.0F, 0.0F);
    }

    public void rotateZ(float f)
    {
        rotateImpl(f, 0.0F, 0.0F, 1.0F);
    }

    public boolean save(String s)
    {
        return saveImpl(s);
    }

    public boolean saveImpl(String s)
    {
        return super.save(s);
    }

    public void scale(float f)
    {
        scaleImpl(f, f, f);
    }

    public void scale(float f, float f1)
    {
        scaleImpl(f, f1, 1.0F);
    }

    public void scale(float f, float f1, float f2)
    {
        scaleImpl(f, f1, f2);
    }

    protected void scaleImpl(float f, float f1, float f2)
    {
        modelview.scale(f, f1, f2);
        invScale(modelviewInv, f, f1, f2);
        projmodelview.scale(f, f1, f2);
    }

    public float screenX(float f, float f1)
    {
        return screenXImpl(f, f1, 0.0F);
    }

    public float screenX(float f, float f1, float f2)
    {
        return screenXImpl(f, f1, f2);
    }

    protected float screenXImpl(float f, float f1, float f2)
    {
        return screenXImpl(modelview.m00 * f + modelview.m01 * f1 + modelview.m02 * f2 + modelview.m03, modelview.m10 * f + modelview.m11 * f1 + modelview.m12 * f2 + modelview.m13, modelview.m20 * f + modelview.m21 * f1 + modelview.m22 * f2 + modelview.m23, modelview.m30 * f + modelview.m31 * f1 + modelview.m32 * f2 + modelview.m33);
    }

    protected float screenXImpl(float f, float f1, float f2, float f3)
    {
        float f4 = projection.m00 * f + projection.m01 * f1 + projection.m02 * f2 + projection.m03 * f3;
        f1 = projection.m30 * f + projection.m31 * f1 + projection.m32 * f2 + projection.m33 * f3;
        f = f4;
        if(nonZero(f1))
            f = f4 / f1;
        return ((f + 1.0F) * (float)width) / 2.0F;
    }

    public float screenY(float f, float f1)
    {
        return screenYImpl(f, f1, 0.0F);
    }

    public float screenY(float f, float f1, float f2)
    {
        return screenYImpl(f, f1, f2);
    }

    protected float screenYImpl(float f, float f1, float f2)
    {
        return screenYImpl(modelview.m00 * f + modelview.m01 * f1 + modelview.m02 * f2 + modelview.m03, modelview.m10 * f + modelview.m11 * f1 + modelview.m12 * f2 + modelview.m13, modelview.m20 * f + modelview.m21 * f1 + modelview.m22 * f2 + modelview.m23, modelview.m30 * f + modelview.m31 * f1 + modelview.m32 * f2 + modelview.m33);
    }

    protected float screenYImpl(float f, float f1, float f2, float f3)
    {
        float f4 = projection.m10 * f + projection.m11 * f1 + projection.m12 * f2 + projection.m13 * f3;
        f1 = projection.m30 * f + projection.m31 * f1 + projection.m32 * f2 + projection.m33 * f3;
        f = f4;
        if(nonZero(f1))
            f = f4 / f1;
        f = ((f + 1.0F) * (float)height) / 2.0F;
        return (float)height - f;
    }

    public float screenZ(float f, float f1, float f2)
    {
        return screenZImpl(f, f1, f2);
    }

    protected float screenZImpl(float f, float f1, float f2)
    {
        return screenZImpl(modelview.m00 * f + modelview.m01 * f1 + modelview.m02 * f2 + modelview.m03, modelview.m10 * f + modelview.m11 * f1 + modelview.m12 * f2 + modelview.m13, modelview.m20 * f + modelview.m21 * f1 + modelview.m22 * f2 + modelview.m23, modelview.m30 * f + modelview.m31 * f1 + modelview.m32 * f2 + modelview.m33);
    }

    protected float screenZImpl(float f, float f1, float f2, float f3)
    {
        float f4 = projection.m20 * f + projection.m21 * f1 + projection.m22 * f2 + projection.m23 * f3;
        f1 = projection.m30 * f + projection.m31 * f1 + projection.m32 * f2 + projection.m33 * f3;
        f = f4;
        if(nonZero(f1))
            f = f4 / f1;
        return (f + 1.0F) / 2.0F;
    }

    public void set(int i, int j, int k)
    {
        loadPixels();
        super.set(i, j, k);
    }

    public void setCache(PImage pimage, Object obj)
    {
        if(pimage instanceof PGraphicsOpenGL)
            getPrimaryPG().cacheMap.put(pimage, new WeakReference(obj));
        else
            getPrimaryPG().cacheMap.put(pimage, obj);
    }

    protected void setCurrentPG()
    {
        currentPG = this;
    }

    protected void setCurrentPG(PGraphicsOpenGL pgraphicsopengl)
    {
        currentPG = pgraphicsopengl;
    }

    protected void setFlushMode(int i)
    {
        flushMode = i;
    }

    protected void setFontTexture(PFont pfont, FontTexture fonttexture)
    {
        getPrimaryPG().fontMap.put(pfont, fonttexture);
    }

    public void setFrameRate(float f)
    {
        pgl.setFrameRate(f);
    }

    protected void setFramebuffer(FrameBuffer framebuffer)
    {
        PGraphicsOpenGL pgraphicsopengl = getPrimaryPG();
        if(pgraphicsopengl.currentFramebuffer != framebuffer)
        {
            pgraphicsopengl.currentFramebuffer = framebuffer;
            if(pgraphicsopengl.currentFramebuffer != null)
                pgraphicsopengl.currentFramebuffer.bind();
        }
    }

    protected void setGLSettings()
    {
        inGeo.clear();
        tessGeo.clear();
        texCache.clear();
        super.noTexture();
        blendModeImpl();
        if(hints[2])
            pgl.disable(PGL.DEPTH_TEST);
        else
            pgl.enable(PGL.DEPTH_TEST);
        pgl.depthFunc(PGL.LEQUAL);
        if(hints[6])
            flushMode = 0;
        else
            flushMode = 1;
        if(!primaryGraphics);
        if(smooth < 1)
            pgl.disable(PGL.MULTISAMPLE);
        else
        if(!OPENGL_RENDERER.equals("VideoCore IV HW"))
            pgl.enable(PGL.MULTISAMPLE);
        if(!OPENGL_RENDERER.equals("VideoCore IV HW"))
            pgl.disable(PGL.POLYGON_SMOOTH);
        if(sized)
        {
            if(primaryGraphics)
                background(backgroundColor);
            else
                background(backgroundColor & 0xffffff | 0);
            defaultPerspective();
            defaultCamera();
            sized = false;
        } else
        {
            modelview.set(camera);
            modelviewInv.set(cameraInv);
            updateProjmodelview();
        }
        if(is3D())
        {
            noLights();
            lightFalloff(1.0F, 0.0F, 0.0F);
            lightSpecular(0.0F, 0.0F, 0.0F);
        }
        pgl.frontFace(PGL.CW);
        pgl.disable(PGL.CULL_FACE);
        pgl.activeTexture(PGL.TEXTURE0);
        normalY = 0.0F;
        normalX = 0.0F;
        normalZ = 1.0F;
        pgl.depthMask(true);
        pgl.clearDepth(1.0F);
        pgl.clearStencil(0);
        pgl.clear(PGL.DEPTH_BUFFER_BIT | PGL.STENCIL_BUFFER_BIT);
        if(hints[5])
            pgl.depthMask(false);
        else
            pgl.depthMask(true);
        pixelsOp = 0;
        modified = false;
        loaded = false;
    }

    protected void setImpl(PImage pimage, int i, int j, int k, int l, int i1, int j1)
    {
        updatePixelSize();
        loadPixels();
        int k1 = pimage.pixelWidth;
        int l1 = pixelWidth;
        int j2 = k1 * j + i;
        k1 = l1 * j1 + i1;
        for(int i2 = j; i2 < j + l; i2++)
        {
            System.arraycopy(pimage.pixels, j2, pixels, k1, k);
            j2 += pimage.pixelWidth;
            k1 += pixelWidth;
        }

        copy(pimage, i, j, k, l, i1, j1, k, l);
    }

    public void setMatrix(PMatrix2D pmatrix2d)
    {
        resetMatrix();
        applyMatrix(pmatrix2d);
    }

    public void setMatrix(PMatrix3D pmatrix3d)
    {
        resetMatrix();
        applyMatrix(pmatrix3d);
    }

    public void setParent(PApplet papplet)
    {
        super.setParent(papplet);
        if(pgl != null)
            pgl.sketch = papplet;
    }

    public void setPrimary(boolean flag)
    {
        super.setPrimary(flag);
        pgl.setPrimary(flag);
        format = 2;
        if(flag)
        {
            fbStack = new FrameBuffer[16];
            fontMap = new WeakHashMap();
            tessellator = new Tessellator();
        } else
        {
            tessellator = getPrimaryPG().tessellator;
        }
    }

    public void setProjection(PMatrix3D pmatrix3d)
    {
        flush();
        projection.set(pmatrix3d);
        updateProjmodelview();
    }

    public void setSize(int i, int j)
    {
        width = i;
        height = j;
        updatePixelSize();
        cameraFOV = 1.047198F;
        cameraX = (float)width / 2.0F;
        cameraY = (float)height / 2.0F;
        cameraZ = cameraY / (float)Math.tan(cameraFOV / 2.0F);
        cameraNear = cameraZ / 10F;
        cameraFar = cameraZ * 10F;
        cameraAspect = (float)width / (float)height;
        sized = true;
    }

    protected void setViewport()
    {
        viewport.put(0, 0);
        viewport.put(1, 0);
        viewport.put(2, width);
        viewport.put(3, height);
        pgl.viewport(viewport.get(0), viewport.get(1), viewport.get(2), viewport.get(3));
    }

    public void shader(PShader pshader)
    {
        flush();
        if(pshader != null)
            pshader.init();
        if(pshader.isPolyShader())
            polyShader = pshader;
        else
        if(pshader.isLineShader())
            lineShader = pshader;
        else
        if(pshader.isPointShader())
            pointShader = pshader;
        else
            PGraphics.showWarning("Unknown shader kind");
    }

    public void shader(PShader pshader, int i)
    {
        flush();
        if(pshader != null)
            pshader.init();
        if(i == 9)
            polyShader = pshader;
        else
        if(i == 5)
            lineShader = pshader;
        else
        if(i == 3)
            pointShader = pshader;
        else
            PGraphics.showWarning("Unknown shader kind");
    }

    protected void shape(PShape pshape, float f, float f1, float f2)
    {
        if(!pshape.isVisible()) goto _L2; else goto _L1
_L1:
        flush();
        pushMatrix();
        if(shapeMode != 3) goto _L4; else goto _L3
_L3:
        translate(f - pshape.getWidth() / 2.0F, f1 - pshape.getHeight() / 2.0F, f2 - pshape.getDepth() / 2.0F);
_L6:
        pshape.draw(this);
        popMatrix();
_L2:
        return;
_L4:
        if(shapeMode == 0 || shapeMode == 1)
            translate(f, f1, f2);
        if(true) goto _L6; else goto _L5
_L5:
    }

    protected void shape(PShape pshape, float f, float f1, float f2, float f3, float f4, float f5)
    {
        if(!pshape.isVisible()) goto _L2; else goto _L1
_L1:
        flush();
        pushMatrix();
        if(shapeMode != 3) goto _L4; else goto _L3
_L3:
        translate(f - f3 / 2.0F, f1 - f4 / 2.0F, f2 - f5 / 2.0F);
        scale(f3 / pshape.getWidth(), f4 / pshape.getHeight(), f5 / pshape.getDepth());
_L6:
        pshape.draw(this);
        popMatrix();
_L2:
        return;
_L4:
        if(shapeMode == 0)
        {
            translate(f, f1, f2);
            scale(f3 / pshape.getWidth(), f4 / pshape.getHeight(), f5 / pshape.getDepth());
        } else
        if(shapeMode == 1)
        {
            translate(f, f1, f2);
            scale((f3 - f) / pshape.getWidth(), (f4 - f1) / pshape.getHeight(), (f5 - f2) / pshape.getDepth());
        }
        if(true) goto _L6; else goto _L5
_L5:
    }

    public void shearX(float f)
    {
        applyMatrixImpl(1.0F, (float)Math.tan(f), 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
    }

    public void shearY(float f)
    {
        applyMatrixImpl(1.0F, 0.0F, 0.0F, 0.0F, (float)Math.tan(f), 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
    }

    public void sphere(float f)
    {
        if(sphereDetailU < 3 || sphereDetailV < 2)
            sphereDetail(30);
        beginShape(9);
        defaultEdges = false;
        normalMode = 2;
        inGeo.setMaterial(fillColor, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininess);
        endShape(inGeo.addSphere(f, sphereDetailU, sphereDetailV, fill, stroke));
    }

    public void spotLight(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8, float f9, float f10)
    {
        enableLighting();
        if(lightCount == PGL.MAX_LIGHTS)
        {
            throw new RuntimeException((new StringBuilder()).append("can only create ").append(PGL.MAX_LIGHTS).append(" lights").toString());
        } else
        {
            lightType[lightCount] = 3;
            lightPosition(lightCount, f3, f4, f5, false);
            lightNormal(lightCount, f6, f7, f8);
            noLightAmbient(lightCount);
            lightDiffuse(lightCount, f, f1, f2);
            lightSpecular(lightCount, currentLightSpecular[0], currentLightSpecular[1], currentLightSpecular[2]);
            lightSpot(lightCount, f9, f10);
            lightFalloff(lightCount, currentLightFalloffConstant, currentLightFalloffLinear, currentLightFalloffQuadratic);
            lightCount = lightCount + 1;
            return;
        }
    }

    public void strokeCap(int i)
    {
        strokeCap = i;
    }

    public void strokeJoin(int i)
    {
        strokeJoin = i;
    }

    public void strokeWeight(float f)
    {
        strokeWeight = f;
    }

    protected void swapOffscreenTextures()
    {
        FrameBuffer framebuffer = offscreenFramebuffer;
        if(texture != null && ptexture != null && framebuffer != null)
        {
            int i = texture.glName;
            texture.glName = ptexture.glName;
            ptexture.glName = i;
            framebuffer.setColorBuffer(texture);
        }
    }

    protected void tessellate(int i)
    {
        boolean flag;
        flag = false;
        tessellator.setInGeometry(inGeo);
        tessellator.setTessGeometry(tessGeo);
        Tessellator tessellator1 = tessellator;
        boolean flag1;
        if(fill || textureImage != null)
            flag1 = true;
        else
            flag1 = false;
        tessellator1.setFill(flag1);
        tessellator.setTexCache(texCache, textureImage);
        tessellator.setStroke(stroke);
        tessellator.setStrokeColor(strokeColor);
        tessellator.setStrokeWeight(strokeWeight);
        tessellator.setStrokeCap(strokeCap);
        tessellator.setStrokeJoin(strokeJoin);
        tessellator.setRenderer(this);
        tessellator.setTransform(modelview);
        tessellator.set3D(is3D());
        if(shape != 3) goto _L2; else goto _L1
_L1:
        tessellator.tessellatePoints();
_L4:
        return;
_L2:
        if(shape == 5)
            tessellator.tessellateLines();
        else
        if(shape == 50)
            tessellator.tessellateLineStrip();
        else
        if(shape == 51)
            tessellator.tessellateLineLoop();
        else
        if(shape == 8 || shape == 9)
        {
            if(stroke && defaultEdges)
                inGeo.addTrianglesEdges();
            if(normalMode == 0)
                inGeo.calcTrianglesNormals();
            tessellator.tessellateTriangles();
        } else
        if(shape == 11)
        {
            if(stroke && defaultEdges)
                inGeo.addTriangleFanEdges();
            if(normalMode == 0)
                inGeo.calcTriangleFanNormals();
            tessellator.tessellateTriangleFan();
        } else
        if(shape == 10)
        {
            if(stroke && defaultEdges)
                inGeo.addTriangleStripEdges();
            if(normalMode == 0)
                inGeo.calcTriangleStripNormals();
            tessellator.tessellateTriangleStrip();
        } else
        if(shape == 16 || shape == 17)
        {
            if(stroke && defaultEdges)
                inGeo.addQuadsEdges();
            if(normalMode == 0)
                inGeo.calcQuadsNormals();
            tessellator.tessellateQuads();
        } else
        if(shape == 18)
        {
            if(stroke && defaultEdges)
                inGeo.addQuadStripEdges();
            if(normalMode == 0)
                inGeo.calcQuadStripNormals();
            tessellator.tessellateQuadStrip();
        } else
        if(shape == 20)
        {
            Tessellator tessellator2 = tessellator;
            boolean flag2;
            if(i == 2)
                flag2 = true;
            else
                flag2 = false;
            if(normalMode == 0)
                flag = true;
            tessellator2.tessellatePolygon(true, flag2, flag);
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected void tessellate(int ai[])
    {
        tessellator.setInGeometry(inGeo);
        tessellator.setTessGeometry(tessGeo);
        Tessellator tessellator1 = tessellator;
        boolean flag;
        if(fill || textureImage != null)
            flag = true;
        else
            flag = false;
        tessellator1.setFill(flag);
        tessellator.setStroke(stroke);
        tessellator.setStrokeColor(strokeColor);
        tessellator.setStrokeWeight(strokeWeight);
        tessellator.setStrokeCap(strokeCap);
        tessellator.setStrokeJoin(strokeJoin);
        tessellator.setTexCache(texCache, textureImage);
        tessellator.setTransform(modelview);
        tessellator.set3D(is3D());
        if(stroke && defaultEdges)
            inGeo.addTrianglesEdges();
        if(normalMode == 0)
            inGeo.calcTrianglesNormals();
        tessellator.tessellateTriangles(ai);
    }

    public float textAscent()
    {
        if(textFont == null)
            defaultFontOrDeath("textAscent");
        Object obj = textFont.getNative();
        float f;
        float f1;
        if(obj != null)
            f = pgl.getFontAscent(obj);
        else
            f = 0.0F;
        f1 = f;
        if(f == 0.0F)
            f1 = super.textAscent();
        return f1;
    }

    protected void textCharImpl(char c, float f, float f1)
    {
        processing.core.PFont.Glyph glyph = textFont.getGlyph(c);
        if(glyph == null) goto _L2; else goto _L1
_L1:
        if(textMode != 4) goto _L4; else goto _L3
_L3:
        FontTexture.TextureInfo textureinfo = textTex.getTexInfo(glyph);
        FontTexture.TextureInfo textureinfo1 = textureinfo;
        if(textureinfo == null)
            textureinfo1 = textTex.addToTexture(this, glyph);
        float f2 = (float)glyph.height / (float)textFont.getSize();
        float f3 = (float)glyph.width / (float)textFont.getSize();
        float f4 = (float)glyph.leftExtent / (float)textFont.getSize();
        float f5 = (float)glyph.topExtent / (float)textFont.getSize();
        f = f4 * textSize + f;
        f1 -= f5 * textSize;
        textCharModelImpl(textureinfo1, f, f1, f + textSize * f3, f1 + textSize * f2);
_L2:
        return;
_L4:
        if(textMode == 5)
            textCharShapeImpl(c, f, f1);
        if(true) goto _L2; else goto _L5
_L5:
    }

    protected void textCharModelImpl(FontTexture.TextureInfo textureinfo, float f, float f1, float f2, float f3)
    {
        beginShape(17);
        texture(textTex.getTexture(textureinfo));
        vertex(f, f1, textureinfo.u0, textureinfo.v0);
        vertex(f2, f1, textureinfo.u1, textureinfo.v0);
        vertex(f2, f3, textureinfo.u1, textureinfo.v1);
        vertex(f, f3, textureinfo.u0, textureinfo.v1);
        endShape();
    }

    protected void textCharShapeImpl(char c, float f, float f1)
    {
        boolean flag = stroke;
        stroke = false;
        PGL.FontOutline fontoutline = pgl.createFontOutline(c, textFont.getNative());
        float af[] = new float[6];
        float f2 = 0.0F;
        float f3 = 0.0F;
        boolean flag1 = false;
        beginShape();
        while(!fontoutline.isDone()) 
        {
            int i = fontoutline.currentSegment(af);
            if(!flag1)
            {
                beginContour();
                flag1 = true;
            }
            if(i == PGL.SEG_MOVETO || i == PGL.SEG_LINETO)
            {
                vertex(af[0] + f, af[1] + f1);
                f2 = af[0];
                f3 = af[1];
            } else
            if(i == PGL.SEG_QUADTO)
            {
                for(i = 1; i < bezierDetail; i++)
                {
                    float f4 = (float)i / (float)bezierDetail;
                    float f5 = (float)((double)((af[0] - f2) * 2.0F) / 3D);
                    float f7 = af[2];
                    vertex(bezierPoint(f2, f2 + f5, (float)((double)((af[0] - af[2]) * 2.0F) / 3D) + f7, af[2], f4) + f, bezierPoint(f3, f3 + (float)((double)((af[1] - f3) * 2.0F) / 3D), af[3] + (float)((double)((af[1] - af[3]) * 2.0F) / 3D), af[3], f4) + f1);
                }

                f2 = af[2];
                f3 = af[3];
            } else
            if(i == PGL.SEG_CUBICTO)
            {
                for(i = 1; i < bezierDetail; i++)
                {
                    float f6 = (float)i / (float)bezierDetail;
                    vertex(bezierPoint(f2, af[0], af[2], af[4], f6) + f, bezierPoint(f3, af[1], af[3], af[5], f6) + f1);
                }

                f2 = af[4];
                f3 = af[5];
            } else
            if(i == PGL.SEG_CLOSE)
            {
                endContour();
                flag1 = false;
            }
            fontoutline.next();
        }
        endShape();
        stroke = flag;
    }

    public float textDescent()
    {
        if(textFont == null)
            defaultFontOrDeath("textDescent");
        Object obj = textFont.getNative();
        float f;
        float f1;
        if(obj != null)
            f = pgl.getFontDescent(obj);
        else
            f = 0.0F;
        f1 = f;
        if(f == 0.0F)
            f1 = super.textDescent();
        return f1;
    }

    protected void textLineImpl(char ac[], int i, int j, float f, float f1)
    {
        if(textMode != 4) goto _L2; else goto _L1
_L1:
        textTex = getFontTexture(textFont);
        if(textTex == null || textTex.contextIsOutdated())
        {
            textTex = new FontTexture(this, textFont, is3D());
            setFontTexture(textFont, textTex);
        }
        textTex.begin();
        int k = textureMode;
        boolean flag = stroke;
        float f2 = normalX;
        float f3 = normalY;
        float f4 = normalZ;
        boolean flag1 = tint;
        int l = tintColor;
        int i1 = blendMode;
        textureMode = 1;
        stroke = false;
        normalX = 0.0F;
        normalY = 0.0F;
        normalZ = 1.0F;
        tint = true;
        tintColor = fillColor;
        blendMode(1);
        super.textLineImpl(ac, i, j, f, f1);
        textureMode = k;
        stroke = flag;
        normalX = f2;
        normalY = f3;
        normalZ = f4;
        tint = flag1;
        tintColor = l;
        blendMode(i1);
        textTex.end();
_L4:
        return;
_L2:
        if(textMode == 5)
            super.textLineImpl(ac, i, j, f, f1);
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected boolean textModeCheck(int i)
    {
        boolean flag;
        if(i == 4 || i == 5 && PGL.SHAPE_TEXT_SUPPORTED)
            flag = true;
        else
            flag = false;
        return flag;
    }

    protected float textWidthImpl(char ac[], int i, int j)
    {
        Object obj = textFont.getNative();
        float f;
        float f1;
        if(obj != null)
            f = pgl.getTextWidth(obj, ac, i, j);
        else
            f = 0.0F;
        f1 = f;
        if(f == 0.0F)
            f1 = super.textWidthImpl(ac, i, j);
        return f1;
    }

    public void textureSampling(int i)
    {
        textureSampling = i;
    }

    public void textureWrap(int i)
    {
        textureWrap = i;
    }

    public void translate(float f, float f1)
    {
        translateImpl(f, f1, 0.0F);
    }

    public void translate(float f, float f1, float f2)
    {
        translateImpl(f, f1, f2);
    }

    protected void translateImpl(float f, float f1, float f2)
    {
        modelview.translate(f, f1, f2);
        invTranslate(modelviewInv, f, f1, f2);
        projmodelview.translate(f, f1, f2);
    }

    public void triangle(float f, float f1, float f2, float f3, float f4, float f5)
    {
        beginShape(9);
        defaultEdges = false;
        normalMode = 1;
        inGeo.setMaterial(fillColor, strokeColor, strokeWeight, ambientColor, specularColor, emissiveColor, shininess);
        inGeo.setNormal(normalX, normalY, normalZ);
        inGeo.addTriangle(f, f1, 0.0F, f2, f3, 0.0F, f4, f5, 0.0F, fill, stroke);
        endShape();
    }

    protected void unbindFrontTexture()
    {
        if(primaryGraphics)
            pgl.unbindFrontTexture();
        else
            ptexture.unbind();
    }

    protected void unbindLineBuffers()
    {
        pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
        pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, 0);
    }

    protected void unbindPointBuffers()
    {
        pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
        pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, 0);
    }

    protected void unbindPolyBuffers()
    {
        pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
        pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, 0);
    }

    public void updateDisplay()
    {
        flush();
        beginPixelsOp(2);
        drawTexture();
        endPixelsOp();
    }

    protected void updateGLModelview()
    {
        if(glModelview == null)
            glModelview = new float[16];
        glModelview[0] = modelview.m00;
        glModelview[1] = modelview.m10;
        glModelview[2] = modelview.m20;
        glModelview[3] = modelview.m30;
        glModelview[4] = modelview.m01;
        glModelview[5] = modelview.m11;
        glModelview[6] = modelview.m21;
        glModelview[7] = modelview.m31;
        glModelview[8] = modelview.m02;
        glModelview[9] = modelview.m12;
        glModelview[10] = modelview.m22;
        glModelview[11] = modelview.m32;
        glModelview[12] = modelview.m03;
        glModelview[13] = modelview.m13;
        glModelview[14] = modelview.m23;
        glModelview[15] = modelview.m33;
    }

    protected void updateGLNormal()
    {
        if(glNormal == null)
            glNormal = new float[9];
        glNormal[0] = modelviewInv.m00;
        glNormal[1] = modelviewInv.m01;
        glNormal[2] = modelviewInv.m02;
        glNormal[3] = modelviewInv.m10;
        glNormal[4] = modelviewInv.m11;
        glNormal[5] = modelviewInv.m12;
        glNormal[6] = modelviewInv.m20;
        glNormal[7] = modelviewInv.m21;
        glNormal[8] = modelviewInv.m22;
    }

    protected void updateGLProjection()
    {
        if(glProjection == null)
            glProjection = new float[16];
        glProjection[0] = projection.m00;
        glProjection[1] = projection.m10;
        glProjection[2] = projection.m20;
        glProjection[3] = projection.m30;
        glProjection[4] = projection.m01;
        glProjection[5] = projection.m11;
        glProjection[6] = projection.m21;
        glProjection[7] = projection.m31;
        glProjection[8] = projection.m02;
        glProjection[9] = projection.m12;
        glProjection[10] = projection.m22;
        glProjection[11] = projection.m32;
        glProjection[12] = projection.m03;
        glProjection[13] = projection.m13;
        glProjection[14] = projection.m23;
        glProjection[15] = projection.m33;
    }

    protected void updateGLProjmodelview()
    {
        if(glProjmodelview == null)
            glProjmodelview = new float[16];
        glProjmodelview[0] = projmodelview.m00;
        glProjmodelview[1] = projmodelview.m10;
        glProjmodelview[2] = projmodelview.m20;
        glProjmodelview[3] = projmodelview.m30;
        glProjmodelview[4] = projmodelview.m01;
        glProjmodelview[5] = projmodelview.m11;
        glProjmodelview[6] = projmodelview.m21;
        glProjmodelview[7] = projmodelview.m31;
        glProjmodelview[8] = projmodelview.m02;
        glProjmodelview[9] = projmodelview.m12;
        glProjmodelview[10] = projmodelview.m22;
        glProjmodelview[11] = projmodelview.m32;
        glProjmodelview[12] = projmodelview.m03;
        glProjmodelview[13] = projmodelview.m13;
        glProjmodelview[14] = projmodelview.m23;
        glProjmodelview[15] = projmodelview.m33;
    }

    protected void updateLineBuffers()
    {
        createLineBuffers();
        int i = tessGeo.lineVertexCount;
        int j = PGL.SIZEOF_FLOAT * i;
        int k = PGL.SIZEOF_INT;
        tessGeo.updateLineVerticesBuffer();
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufLineVertex.glId);
        pgl.bufferData(PGL.ARRAY_BUFFER, j * 4, tessGeo.lineVerticesBuffer, PGL.STATIC_DRAW);
        tessGeo.updateLineColorsBuffer();
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufLineColor.glId);
        pgl.bufferData(PGL.ARRAY_BUFFER, i * k, tessGeo.lineColorsBuffer, PGL.STATIC_DRAW);
        tessGeo.updateLineDirectionsBuffer();
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufLineAttrib.glId);
        pgl.bufferData(PGL.ARRAY_BUFFER, j * 4, tessGeo.lineDirectionsBuffer, PGL.STATIC_DRAW);
        tessGeo.updateLineIndicesBuffer();
        pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, bufLineIndex.glId);
        pgl.bufferData(PGL.ELEMENT_ARRAY_BUFFER, tessGeo.lineIndexCount * PGL.SIZEOF_INDEX, tessGeo.lineIndicesBuffer, PGL.STATIC_DRAW);
    }

    protected void updatePixelSize()
    {
        float f = pgl.getPixelScale();
        pixelWidth = (int)((float)width * f);
        pixelHeight = (int)(f * (float)height);
    }

    protected void updatePointBuffers()
    {
        createPointBuffers();
        int i = tessGeo.pointVertexCount;
        int j = PGL.SIZEOF_FLOAT * i;
        int k = PGL.SIZEOF_INT;
        tessGeo.updatePointVerticesBuffer();
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPointVertex.glId);
        pgl.bufferData(PGL.ARRAY_BUFFER, j * 4, tessGeo.pointVerticesBuffer, PGL.STATIC_DRAW);
        tessGeo.updatePointColorsBuffer();
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPointColor.glId);
        pgl.bufferData(PGL.ARRAY_BUFFER, i * k, tessGeo.pointColorsBuffer, PGL.STATIC_DRAW);
        tessGeo.updatePointOffsetsBuffer();
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPointAttrib.glId);
        pgl.bufferData(PGL.ARRAY_BUFFER, j * 2, tessGeo.pointOffsetsBuffer, PGL.STATIC_DRAW);
        tessGeo.updatePointIndicesBuffer();
        pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, bufPointIndex.glId);
        pgl.bufferData(PGL.ELEMENT_ARRAY_BUFFER, tessGeo.pointIndexCount * PGL.SIZEOF_INDEX, tessGeo.pointIndicesBuffer, PGL.STATIC_DRAW);
    }

    protected void updatePolyBuffers(boolean flag, boolean flag1, boolean flag2, boolean flag3)
    {
        createPolyBuffers();
        int i = tessGeo.polyVertexCount;
        int j = PGL.SIZEOF_FLOAT * i;
        int k = PGL.SIZEOF_INT * i;
        tessGeo.updatePolyVerticesBuffer();
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPolyVertex.glId);
        pgl.bufferData(PGL.ARRAY_BUFFER, j * 4, tessGeo.polyVerticesBuffer, PGL.STATIC_DRAW);
        tessGeo.updatePolyColorsBuffer();
        pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPolyColor.glId);
        pgl.bufferData(PGL.ARRAY_BUFFER, k, tessGeo.polyColorsBuffer, PGL.STATIC_DRAW);
        if(flag)
        {
            tessGeo.updatePolyAmbientBuffer();
            pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPolyAmbient.glId);
            pgl.bufferData(PGL.ARRAY_BUFFER, k, tessGeo.polyAmbientBuffer, PGL.STATIC_DRAW);
            tessGeo.updatePolySpecularBuffer();
            pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPolySpecular.glId);
            pgl.bufferData(PGL.ARRAY_BUFFER, k, tessGeo.polySpecularBuffer, PGL.STATIC_DRAW);
            tessGeo.updatePolyEmissiveBuffer();
            pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPolyEmissive.glId);
            pgl.bufferData(PGL.ARRAY_BUFFER, k, tessGeo.polyEmissiveBuffer, PGL.STATIC_DRAW);
            tessGeo.updatePolyShininessBuffer();
            pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPolyShininess.glId);
            pgl.bufferData(PGL.ARRAY_BUFFER, j, tessGeo.polyShininessBuffer, PGL.STATIC_DRAW);
        }
        if(flag || flag2)
        {
            tessGeo.updatePolyNormalsBuffer();
            pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPolyNormal.glId);
            pgl.bufferData(PGL.ARRAY_BUFFER, j * 3, tessGeo.polyNormalsBuffer, PGL.STATIC_DRAW);
        }
        if(flag1 || flag3)
        {
            tessGeo.updatePolyTexCoordsBuffer();
            pgl.bindBuffer(PGL.ARRAY_BUFFER, bufPolyTexcoord.glId);
            pgl.bufferData(PGL.ARRAY_BUFFER, j * 2, tessGeo.polyTexCoordsBuffer, PGL.STATIC_DRAW);
        }
        String s;
        VertexAttribute vertexattribute;
        for(Iterator iterator = polyAttribs.keySet().iterator(); iterator.hasNext(); pgl.bufferData(PGL.ARRAY_BUFFER, vertexattribute.sizeInBytes(i), (Buffer)tessGeo.polyAttribBuffers.get(s), PGL.STATIC_DRAW))
        {
            s = (String)iterator.next();
            vertexattribute = (VertexAttribute)polyAttribs.get(s);
            tessGeo.updateAttribBuffer(s);
            pgl.bindBuffer(PGL.ARRAY_BUFFER, vertexattribute.buf.glId);
        }

        tessGeo.updatePolyIndicesBuffer();
        pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, bufPolyIndex.glId);
        pgl.bufferData(PGL.ELEMENT_ARRAY_BUFFER, tessGeo.polyIndexCount * PGL.SIZEOF_INDEX, tessGeo.polyIndicesBuffer, PGL.STATIC_DRAW);
    }

    public void updateProjmodelview()
    {
        projmodelview.set(projection);
        projmodelview.apply(modelview);
    }

    public void updateTexture()
    {
        if(texture != null)
            texture.updateTexels();
    }

    public void updateTexture(int i, int j, int k, int l)
    {
        if(texture != null)
            texture.updateTexels(i, j, k, l);
    }

    protected void updateTexture(PImage pimage, Texture texture1)
    {
        if(texture1 != null && pimage.isModified())
        {
            int i = pimage.getModifiedX1();
            int j = pimage.getModifiedY1();
            int k = pimage.getModifiedX2();
            int l = pimage.getModifiedY2();
            texture1.set(pimage.pixels, i, j, k - i, l - j, pimage.format);
        }
        pimage.setModified(false);
    }

    public void vertex(float f, float f1)
    {
        vertexImpl(f, f1, 0.0F, 0.0F, 0.0F);
        if(textureImage != null)
            PGraphics.showWarning("No uv texture coordinates supplied with vertex() call");
    }

    public void vertex(float f, float f1, float f2)
    {
        vertexImpl(f, f1, f2, 0.0F, 0.0F);
        if(textureImage != null)
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
        boolean flag;
        int i;
        int j;
        float f5;
        if(textureImage != null)
            flag = true;
        else
            flag = false;
        i = 0;
        if(fill || flag)
            if(!flag)
                i = fillColor;
            else
            if(tint)
                i = tintColor;
            else
                i = -1;
        j = 0;
        f5 = 0.0F;
        if(stroke)
        {
            j = strokeColor;
            f5 = strokeWeight;
        }
        if(flag && textureMode == 2)
        {
            f3 /= textureImage.width;
            f4 /= textureImage.height;
        }
        inGeo.addVertex(f, f1, f2, i, normalX, normalY, normalZ, f3, f4, j, f5, ambientColor, specularColor, emissiveColor, shininess, 0, vertexBreak());
    }

    protected PImage wrapTexture(Texture texture1)
    {
        PImage pimage = new PImage();
        pimage.parent = parent;
        pimage.width = texture1.width;
        pimage.height = texture1.height;
        pimage.format = 2;
        setCache(pimage, texture1);
        return pimage;
    }

    static final String ALREADY_BEGAN_CONTOUR_ERROR = "Already called beginContour()";
    static final String BLEND_DRIVER_ERROR = "blendMode(%1$s) is not supported by this hardware (or driver)";
    static final String BLEND_RENDERER_ERROR = "blendMode(%1$s) is not supported by this renderer";
    protected static final int EDGE_CLOSE = -1;
    protected static final int EDGE_MIDDLE = 0;
    protected static final int EDGE_SINGLE = 3;
    protected static final int EDGE_START = 1;
    protected static final int EDGE_STOP = 2;
    protected static final int FB_STACK_DEPTH = 16;
    protected static final int FLUSH_CONTINUOUSLY = 0;
    protected static final int FLUSH_WHEN_FULL = 1;
    public static String GLSL_VERSION;
    static final String GL_THREAD_NOT_CURRENT = "You are trying to draw outside OpenGL's animation thread.\nPlace all drawing commands in the draw() function, or inside\nyour own functions as long as they are called from draw(),\nbut not in event handling functions such as keyPressed()\nor mousePressed().";
    protected static final int IMMEDIATE = 0;
    static final String INCONSISTENT_SHADER_TYPES = "The vertex and fragment shaders have different types";
    protected static final int INIT_INDEX_BUFFER_SIZE = 512;
    protected static final int INIT_VERTEX_BUFFER_SIZE = 256;
    static final String INVALID_FILTER_SHADER_ERROR = "Your shader cannot be used as a filter because is of type POINT or LINES";
    protected static final int MATRIX_STACK_DEPTH = 32;
    private static final int MAX_DRAIN_GLRES_ITERATIONS = 10;
    protected static final int MAX_POINT_ACCURACY = 200;
    protected static final int MIN_POINT_ACCURACY = 20;
    static final String MISSING_FRAGMENT_SHADER = "The fragment shader is missing, cannot create shader object";
    static final String MISSING_UV_TEXCOORDS_ERROR = "No uv texture coordinates supplied with vertex() call";
    static final String MISSING_VERTEX_SHADER = "The vertex shader is missing, cannot create shader object";
    static final String NO_BEGIN_CONTOUR_ERROR = "Need to call beginContour() first";
    static final String NO_COLOR_SHADER_ERROR = "Your shader needs to be of COLOR type to render this geometry properly, using default shader instead.";
    static final String NO_LIGHT_SHADER_ERROR = "Your shader needs to be of LIGHT type to render this geometry properly, using default shader instead.";
    static final String NO_TEXLIGHT_SHADER_ERROR = "Your shader needs to be of TEXLIGHT type to render this geometry properly, using default shader instead.";
    static final String NO_TEXTURE_SHADER_ERROR = "Your shader needs to be of TEXTURE type to render this geometry properly, using default shader instead.";
    public static String OPENGL_EXTENSIONS;
    public static String OPENGL_RENDERER;
    static final String OPENGL_THREAD_ERROR = "Cannot run the OpenGL renderer outside the main thread, change your code\nso the drawing calls are all inside the main thread, \nor use the default renderer instead.";
    public static String OPENGL_VENDOR;
    public static String OPENGL_VERSION;
    protected static final int OP_NONE = 0;
    protected static final int OP_READ = 1;
    protected static final int OP_WRITE = 2;
    protected static final float POINT_ACCURACY_FACTOR = 10F;
    protected static final float QUAD_POINT_SIGNS[][] = {
        {
            -1F, 1.0F
        }, {
            -1F, -1F
        }, {
            1.0F, -1F
        }, {
            1.0F, 1.0F
        }
    };
    protected static final int RETAINED = 1;
    static final String SHADER_NEED_LIGHT_ATTRIBS = "The provided shader needs light attributes (ambient, diffuse, etc.), but the current scene is unlit, so the default shader will be used instead";
    static final String TESSELLATION_ERROR = "Tessellation Error: %1$s";
    static final String TOO_MANY_SMOOTH_CALLS_ERROR = "The smooth/noSmooth functions are being called too often.\nThis results in screen flickering, so they will be disabled\nfor the rest of the sketch's execution";
    static final String UNKNOWN_SHADER_KIND_ERROR = "Unknown shader kind";
    static final String UNSUPPORTED_SHAPE_FORMAT_ERROR = "Unsupported shape format";
    static final String UNSUPPORTED_SMOOTH_ERROR = "Smooth is not supported by this hardware (or driver)";
    static final String UNSUPPORTED_SMOOTH_LEVEL_ERROR = "Smooth level %1$s is not available. Using %2$s instead";
    static final String WRONG_SHADER_TYPE_ERROR = "shader() called with a wrong shader";
    public static boolean anisoSamplingSupported;
    public static boolean autoMipmapGenSupported;
    public static boolean blendEqSupported;
    protected static URL defColorShaderFragURL = processing/opengl/PGraphicsOpenGL.getResource("/processing/opengl/shaders/ColorFrag.glsl");
    protected static URL defColorShaderVertURL = processing/opengl/PGraphicsOpenGL.getResource("/processing/opengl/shaders/ColorVert.glsl");
    protected static URL defLightShaderFragURL = processing/opengl/PGraphicsOpenGL.getResource("/processing/opengl/shaders/LightFrag.glsl");
    protected static URL defLightShaderVertURL = processing/opengl/PGraphicsOpenGL.getResource("/processing/opengl/shaders/LightVert.glsl");
    protected static URL defLineShaderFragURL = processing/opengl/PGraphicsOpenGL.getResource("/processing/opengl/shaders/LineFrag.glsl");
    protected static URL defLineShaderVertURL = processing/opengl/PGraphicsOpenGL.getResource("/processing/opengl/shaders/LineVert.glsl");
    protected static URL defPointShaderFragURL = processing/opengl/PGraphicsOpenGL.getResource("/processing/opengl/shaders/PointFrag.glsl");
    protected static URL defPointShaderVertURL = processing/opengl/PGraphicsOpenGL.getResource("/processing/opengl/shaders/PointVert.glsl");
    protected static URL defTexlightShaderFragURL = processing/opengl/PGraphicsOpenGL.getResource("/processing/opengl/shaders/TexLightFrag.glsl");
    protected static URL defTexlightShaderVertURL = processing/opengl/PGraphicsOpenGL.getResource("/processing/opengl/shaders/TexLightVert.glsl");
    protected static URL defTextureShaderFragURL = processing/opengl/PGraphicsOpenGL.getResource("/processing/opengl/shaders/TexFrag.glsl");
    protected static URL defTextureShaderVertURL = processing/opengl/PGraphicsOpenGL.getResource("/processing/opengl/shaders/TexVert.glsl");
    public static int depthBits;
    public static boolean drawBufferSupported;
    public static boolean fboMultisampleSupported;
    protected static FloatBuffer floatBuffer;
    protected static boolean glParamsRead = false;
    protected static PMatrix3D identity = new PMatrix3D();
    protected static IntBuffer intBuffer;
    protected static URL maskShaderFragURL = processing/opengl/PGraphicsOpenGL.getResource("/processing/opengl/shaders/MaskFrag.glsl");
    public static float maxAnisoAmount;
    public static int maxSamples;
    public static int maxTextureSize;
    public static boolean npotTexSupported;
    protected static final Set ongoingPixelTransfers = new HashSet();
    protected static final List ongoingPixelTransfersIterable = new ArrayList();
    public static boolean packedDepthStencilSupported;
    public static boolean readBufferSupported;
    public static int stencilBits;
    protected AsyncPixelReader asyncPixelReader;
    protected boolean asyncPixelReaderInitialized;
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
    public PMatrix3D camera;
    public float cameraAspect;
    public float cameraFOV;
    public float cameraFar;
    public PMatrix3D cameraInv;
    protected float cameraInvStack[][];
    public float cameraNear;
    protected float cameraStack[][];
    public float cameraX;
    public float cameraY;
    public float cameraZ;
    protected boolean clip;
    protected int clipRect[] = {
        0, 0, 0, 0
    };
    protected FrameBuffer currentFramebuffer;
    public float currentLightFalloffConstant;
    public float currentLightFalloffLinear;
    public float currentLightFalloffQuadratic;
    public float currentLightSpecular[];
    public PGraphicsOpenGL currentPG;
    protected PShader defColorShader;
    protected PShader defLightShader;
    protected PShader defLineShader;
    protected PShader defPointShader;
    protected PShader defTexlightShader;
    protected PShader defTextureShader;
    protected boolean defaultEdges;
    protected FrameBuffer drawFramebuffer;
    protected boolean drawing;
    protected float eyeDist;
    protected FrameBuffer fbStack[];
    protected int fbStackDepth;
    protected PImage filterImage;
    protected Texture filterTexture;
    protected int flushMode;
    protected WeakHashMap fontMap;
    protected float glModelview[];
    protected float glNormal[];
    protected float glProjection[];
    protected float glProjmodelview[];
    protected InGeometry inGeo;
    public boolean initialized;
    protected boolean isDepthSortingEnabled;
    protected int lastBlendMode;
    protected int lastSmoothCall;
    public float lightAmbient[];
    public int lightCount;
    public float lightDiffuse[];
    public float lightFalloffCoefficients[];
    public float lightNormal[];
    public float lightPosition[];
    public float lightSpecular[];
    public float lightSpotParameters[];
    public int lightType[];
    public boolean lights;
    protected int lineBuffersContext;
    protected boolean lineBuffersCreated;
    protected PShader lineShader;
    protected boolean manipulatingCamera;
    protected PShader maskShader;
    public PMatrix3D modelview;
    public PMatrix3D modelviewInv;
    protected float modelviewInvStack[][];
    protected float modelviewStack[][];
    protected int modelviewStackDepth;
    protected FrameBuffer multisampleFramebuffer;
    protected IntBuffer nativePixelBuffer;
    protected int nativePixels[];
    protected FrameBuffer offscreenFramebuffer;
    protected boolean offscreenMultisample;
    protected boolean openContour;
    public PGL pgl;
    protected boolean pixOpChangedFB;
    protected IntBuffer pixelBuffer;
    protected int pixelsOp;
    protected int pointBuffersContext;
    protected boolean pointBuffersCreated;
    protected PShader pointShader;
    protected AttributeMap polyAttribs;
    protected int polyBuffersContext;
    protected boolean polyBuffersCreated;
    protected PShader polyShader;
    public PMatrix3D projection;
    protected float projectionStack[][];
    protected int projectionStackDepth;
    public PMatrix3D projmodelview;
    protected Texture ptexture;
    protected FrameBuffer readFramebuffer;
    protected boolean sized;
    protected int smoothCallCount;
    protected boolean smoothDisabled;
    protected DepthSorter sorter;
    protected TessGeometry tessGeo;
    protected Tessellator tessellator;
    protected TexCache texCache;
    FontTexture textTex;
    protected Texture texture;
    protected int textureSampling;
    protected int textureWrap;
    protected IntBuffer viewport;




































/*
    static int access$3902(PGraphicsOpenGL pgraphicsopengl, int i)
    {
        pgraphicsopengl.curveVertexCount = i;
        return i;
    }

*/







/*
    static int access$4402(PGraphicsOpenGL pgraphicsopengl, int i)
    {
        pgraphicsopengl.curveVertexCount = i;
        return i;
    }

*/





/*
    static int access$4802(PGraphicsOpenGL pgraphicsopengl, int i)
    {
        pgraphicsopengl.curveVertexCount = i;
        return i;
    }

*/









/*
    static int access$5508(PGraphicsOpenGL pgraphicsopengl)
    {
        int i = pgraphicsopengl.curveVertexCount;
        pgraphicsopengl.curveVertexCount = i + 1;
        return i;
    }

*/
















/*
    static int access$6902(PGraphicsOpenGL pgraphicsopengl, int i)
    {
        pgraphicsopengl.curveVertexCount = i;
        return i;
    }

*/



}
