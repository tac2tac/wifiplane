// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.opengl.tess;


// Referenced classes of package processing.opengl.tess:
//            GLUface, GLUtessellatorImpl, CachedVertex, GLUhalfEdge, 
//            GLUvertex, GLUmesh

class Render
{
    private static class FaceCount
    {

        GLUhalfEdge eStart;
        renderCallBack render;
        long size;

        public FaceCount()
        {
        }

        public FaceCount(long l, GLUhalfEdge gluhalfedge, renderCallBack rendercallback)
        {
            size = l;
            eStart = gluhalfedge;
            render = rendercallback;
        }
    }

    private static class RenderFan
        implements renderCallBack
    {

        public void render(GLUtessellatorImpl glutessellatorimpl, GLUhalfEdge gluhalfedge, long l)
        {
            glutessellatorimpl.callBeginOrBeginData(6);
            glutessellatorimpl.callVertexOrVertexData(gluhalfedge.Org.data);
            glutessellatorimpl.callVertexOrVertexData(gluhalfedge.Sym.Org.data);
            for(; !Render.Marked(gluhalfedge.Lface); glutessellatorimpl.callVertexOrVertexData(gluhalfedge.Sym.Org.data))
            {
                gluhalfedge.Lface.marked = true;
                l--;
                gluhalfedge = gluhalfedge.Onext;
            }

            if(!$assertionsDisabled && l != 0L)
            {
                throw new AssertionError();
            } else
            {
                glutessellatorimpl.callEndOrEndData();
                return;
            }
        }

        static final boolean $assertionsDisabled;

        static 
        {
            boolean flag;
            if(!processing/opengl/tess/Render.desiredAssertionStatus())
                flag = true;
            else
                flag = false;
            $assertionsDisabled = flag;
        }

        private RenderFan()
        {
        }

    }

    private static class RenderStrip
        implements renderCallBack
    {

        public void render(GLUtessellatorImpl glutessellatorimpl, GLUhalfEdge gluhalfedge, long l)
        {
            glutessellatorimpl.callBeginOrBeginData(5);
            glutessellatorimpl.callVertexOrVertexData(gluhalfedge.Org.data);
            glutessellatorimpl.callVertexOrVertexData(gluhalfedge.Sym.Org.data);
            do
            {
label0:
                {
                    long l1 = l;
                    if(!Render.Marked(gluhalfedge.Lface))
                    {
                        gluhalfedge.Lface.marked = true;
                        l1 = l - 1L;
                        gluhalfedge = gluhalfedge.Lnext.Sym;
                        glutessellatorimpl.callVertexOrVertexData(gluhalfedge.Org.data);
                        if(!Render.Marked(gluhalfedge.Lface))
                            break label0;
                    }
                    if(!$assertionsDisabled && l1 != 0L)
                    {
                        throw new AssertionError();
                    } else
                    {
                        glutessellatorimpl.callEndOrEndData();
                        return;
                    }
                }
                gluhalfedge.Lface.marked = true;
                l = l1 - 1L;
                gluhalfedge = gluhalfedge.Onext;
                glutessellatorimpl.callVertexOrVertexData(gluhalfedge.Sym.Org.data);
            } while(true);
        }

        static final boolean $assertionsDisabled;

        static 
        {
            boolean flag;
            if(!processing/opengl/tess/Render.desiredAssertionStatus())
                flag = true;
            else
                flag = false;
            $assertionsDisabled = flag;
        }

        private RenderStrip()
        {
        }

    }

    private static class RenderTriangle
        implements renderCallBack
    {

        public void render(GLUtessellatorImpl glutessellatorimpl, GLUhalfEdge gluhalfedge, long l)
        {
            if(!$assertionsDisabled && l != 1L)
            {
                throw new AssertionError();
            } else
            {
                glutessellatorimpl.lonelyTriList = Render.AddToTrail(gluhalfedge.Lface, glutessellatorimpl.lonelyTriList);
                return;
            }
        }

        static final boolean $assertionsDisabled;

        static 
        {
            boolean flag;
            if(!processing/opengl/tess/Render.desiredAssertionStatus())
                flag = true;
            else
                flag = false;
            $assertionsDisabled = flag;
        }

        private RenderTriangle()
        {
        }

    }

    private static interface renderCallBack
    {

        public abstract void render(GLUtessellatorImpl glutessellatorimpl, GLUhalfEdge gluhalfedge, long l);
    }


    private Render()
    {
    }

    private static GLUface AddToTrail(GLUface gluface, GLUface gluface1)
    {
        gluface.trail = gluface1;
        gluface.marked = true;
        return gluface;
    }

    static int ComputeNormal(GLUtessellatorImpl glutessellatorimpl, double ad[], boolean flag)
    {
        CachedVertex acachedvertex[];
        int i;
        int j;
        int k;
        double d2;
        double d5;
        double d6;
        acachedvertex = glutessellatorimpl.cache;
        i = glutessellatorimpl.cacheCount;
        glutessellatorimpl = new double[3];
        j = 0;
        if(!flag)
        {
            ad[2] = 0.0D;
            ad[1] = 0.0D;
            ad[0] = 0.0D;
        }
        k = 1;
        double d = acachedvertex[1].coords[0];
        d2 = acachedvertex[0].coords[0];
        double d3 = acachedvertex[1].coords[1];
        d5 = acachedvertex[0].coords[1];
        d6 = acachedvertex[1].coords[2] - acachedvertex[0].coords[2];
        d2 = d - d2;
        d5 = d3 - d5;
_L9:
        int l;
        l = k + 1;
        k = j;
        if(l >= i) goto _L2; else goto _L1
_L1:
        double d1;
        double d4;
        double d7;
        d4 = acachedvertex[l].coords[0] - acachedvertex[0].coords[0];
        d1 = acachedvertex[l].coords[1] - acachedvertex[0].coords[1];
        d7 = acachedvertex[l].coords[2] - acachedvertex[0].coords[2];
        glutessellatorimpl[0] = d5 * d7 - d6 * d1;
        glutessellatorimpl[1] = d6 * d4 - d2 * d7;
        glutessellatorimpl[2] = d2 * d1 - d5 * d4;
        d6 = glutessellatorimpl[0] * ad[0] + glutessellatorimpl[1] * ad[1] + glutessellatorimpl[2] * ad[2];
        if(!flag)
        {
            if(d6 >= 0.0D)
            {
                ad[0] = ad[0] + glutessellatorimpl[0];
                ad[1] = ad[1] + glutessellatorimpl[1];
                ad[2] = ad[2] + glutessellatorimpl[2];
                d6 = d7;
                d5 = d1;
                d2 = d4;
                k = l;
            } else
            {
                ad[0] = ad[0] - glutessellatorimpl[0];
                ad[1] = ad[1] - glutessellatorimpl[1];
                ad[2] = ad[2] - glutessellatorimpl[2];
                d6 = d7;
                d5 = d1;
                d2 = d4;
                k = l;
            }
            continue; /* Loop/switch isn't completed */
        }
        if(d6 == 0.0D)
            break MISSING_BLOCK_LABEL_443;
        if(d6 <= 0.0D) goto _L4; else goto _L3
_L3:
        if(j >= 0) goto _L6; else goto _L5
_L5:
        k = 2;
_L2:
        return k;
_L6:
        j = 1;
        d6 = d7;
        d5 = d1;
        d2 = d4;
        k = l;
        continue; /* Loop/switch isn't completed */
_L4:
        if(j <= 0)
            break; /* Loop/switch isn't completed */
        k = 2;
        if(true) goto _L2; else goto _L7
_L7:
        j = -1;
        d6 = d7;
        d5 = d1;
        d2 = d4;
        k = l;
        continue; /* Loop/switch isn't completed */
        d6 = d7;
        d5 = d1;
        d2 = d4;
        k = l;
        if(true) goto _L9; else goto _L8
_L8:
    }

    private static void FreeTrail(GLUface gluface)
    {
        for(; gluface != null; gluface = gluface.trail)
            gluface.marked = false;

    }

    private static boolean IsEven(long l)
    {
        boolean flag;
        if((1L & l) == 0L)
            flag = true;
        else
            flag = false;
        return flag;
    }

    private static boolean Marked(GLUface gluface)
    {
        boolean flag;
        if(!gluface.inside || gluface.marked)
            flag = true;
        else
            flag = false;
        return flag;
    }

    static FaceCount MaximumFan(GLUhalfEdge gluhalfedge)
    {
        FaceCount facecount = new FaceCount(0L, null, renderFan);
        GLUface gluface = null;
        GLUhalfEdge gluhalfedge1 = gluhalfedge;
        GLUface gluface1;
        GLUhalfEdge gluhalfedge2;
        do
        {
            gluface1 = gluface;
            gluhalfedge2 = gluhalfedge;
            if(Marked(gluhalfedge1.Lface))
                break;
            gluface = AddToTrail(gluhalfedge1.Lface, gluface);
            facecount.size = facecount.size + 1L;
            gluhalfedge1 = gluhalfedge1.Onext;
        } while(true);
        for(; !Marked(gluhalfedge2.Sym.Lface); gluhalfedge2 = gluhalfedge2.Sym.Lnext)
        {
            gluface1 = AddToTrail(gluhalfedge2.Sym.Lface, gluface1);
            facecount.size = facecount.size + 1L;
        }

        facecount.eStart = gluhalfedge2;
        FreeTrail(gluface1);
        return facecount;
    }

    static FaceCount MaximumStrip(GLUhalfEdge gluhalfedge)
    {
        long l;
        FaceCount facecount;
        GLUface gluface;
        long l1;
        Object obj;
        l = 0L;
        facecount = new FaceCount(0L, null, renderStrip);
        gluface = null;
        l1 = 0L;
        obj = gluhalfedge;
_L5:
        GLUhalfEdge gluhalfedge1;
        GLUface gluface1;
        long l2;
        long l3;
        GLUhalfEdge gluhalfedge2;
        gluhalfedge1 = ((GLUhalfEdge) (obj));
        gluface1 = gluface;
        l2 = l1;
        l3 = l;
        gluhalfedge2 = gluhalfedge;
        if(Marked(((GLUhalfEdge) (obj)).Lface)) goto _L2; else goto _L1
_L1:
        gluface1 = AddToTrail(((GLUhalfEdge) (obj)).Lface, gluface);
        l2 = l1 + 1L;
        gluhalfedge1 = ((GLUhalfEdge) (obj)).Lnext.Sym;
        if(!Marked(gluhalfedge1.Lface)) goto _L4; else goto _L3
_L3:
        gluhalfedge2 = gluhalfedge;
        l3 = l;
_L2:
        obj = gluface1;
        l1 = l3;
        gluhalfedge = gluhalfedge2;
        if(!Marked(gluhalfedge2.Sym.Lface))
        {
            obj = AddToTrail(gluhalfedge2.Sym.Lface, gluface1);
            l1 = l3 + 1L;
            gluhalfedge = gluhalfedge2.Sym.Lnext;
            if(!Marked(gluhalfedge.Sym.Lface))
                break MISSING_BLOCK_LABEL_227;
        }
        facecount.size = l2 + l1;
        if(IsEven(l2))
            facecount.eStart = gluhalfedge1.Sym;
        else
        if(IsEven(l1))
        {
            facecount.eStart = gluhalfedge;
        } else
        {
            facecount.size = facecount.size - 1L;
            facecount.eStart = gluhalfedge.Onext;
        }
        FreeTrail(((GLUface) (obj)));
        return facecount;
_L4:
        gluface = AddToTrail(gluhalfedge1.Lface, gluface1);
        l1 = l2 + 1L;
        obj = gluhalfedge1.Onext;
          goto _L5
        gluface1 = AddToTrail(gluhalfedge.Sym.Lface, ((GLUface) (obj)));
        l3 = l1 + 1L;
        gluhalfedge2 = gluhalfedge.Sym.Onext.Sym;
          goto _L2
    }

    static void RenderLonelyTriangles(GLUtessellatorImpl glutessellatorimpl, GLUface gluface)
    {
        int i = -1;
        glutessellatorimpl.callBeginOrBeginData(4);
        while(gluface != null) 
        {
            GLUhalfEdge gluhalfedge = gluface.anEdge;
            int j = i;
            do
            {
                i = j;
                if(glutessellatorimpl.flagBoundary)
                {
                    int k;
                    if(!gluhalfedge.Sym.Lface.inside)
                        k = 1;
                    else
                        k = 0;
                    i = j;
                    if(j != k)
                    {
                        boolean flag;
                        GLUhalfEdge gluhalfedge1;
                        if(k != 0)
                            flag = true;
                        else
                            flag = false;
                        glutessellatorimpl.callEdgeFlagOrEdgeFlagData(flag);
                        i = k;
                    }
                }
                glutessellatorimpl.callVertexOrVertexData(gluhalfedge.Org.data);
                gluhalfedge1 = gluhalfedge.Lnext;
                j = i;
                gluhalfedge = gluhalfedge1;
            } while(gluhalfedge1 != gluface.anEdge);
            gluface = gluface.trail;
        }
        glutessellatorimpl.callEndOrEndData();
    }

    static void RenderMaximumFaceGroup(GLUtessellatorImpl glutessellatorimpl, GLUface gluface)
    {
        GLUhalfEdge gluhalfedge = gluface.anEdge;
        gluface = new FaceCount();
        new FaceCount();
        gluface.size = 1L;
        gluface.eStart = gluhalfedge;
        gluface.render = renderTriangle;
        Object obj;
        if(!glutessellatorimpl.flagBoundary)
        {
            obj = MaximumFan(gluhalfedge);
            if(((FaceCount) (obj)).size > ((FaceCount) (gluface)).size)
                gluface = ((GLUface) (obj));
            FaceCount facecount = MaximumFan(gluhalfedge.Lnext);
            obj = gluface;
            if(facecount.size > ((FaceCount) (gluface)).size)
                obj = facecount;
            facecount = MaximumFan(gluhalfedge.Onext.Sym);
            gluface = ((GLUface) (obj));
            if(facecount.size > ((FaceCount) (obj)).size)
                gluface = facecount;
            facecount = MaximumStrip(gluhalfedge);
            obj = gluface;
            if(facecount.size > ((FaceCount) (gluface)).size)
                obj = facecount;
            facecount = MaximumStrip(gluhalfedge.Lnext);
            gluface = ((GLUface) (obj));
            if(facecount.size > ((FaceCount) (obj)).size)
                gluface = facecount;
            facecount = MaximumStrip(gluhalfedge.Onext.Sym);
            obj = gluface;
            if(facecount.size > ((FaceCount) (gluface)).size)
                obj = facecount;
        } else
        {
            obj = gluface;
        }
        ((FaceCount) (obj)).render.render(glutessellatorimpl, ((FaceCount) (obj)).eStart, ((FaceCount) (obj)).size);
    }

    public static void __gl_renderBoundary(GLUtessellatorImpl glutessellatorimpl, GLUmesh glumesh)
    {
        for(GLUface gluface = glumesh.fHead.next; gluface != glumesh.fHead; gluface = gluface.next)
        {
            if(!gluface.inside)
                continue;
            glutessellatorimpl.callBeginOrBeginData(2);
            GLUhalfEdge gluhalfedge = gluface.anEdge;
            GLUhalfEdge gluhalfedge1;
            do
            {
                glutessellatorimpl.callVertexOrVertexData(gluhalfedge.Org.data);
                gluhalfedge1 = gluhalfedge.Lnext;
                gluhalfedge = gluhalfedge1;
            } while(gluhalfedge1 != gluface.anEdge);
            glutessellatorimpl.callEndOrEndData();
        }

    }

    public static boolean __gl_renderCache(GLUtessellatorImpl glutessellatorimpl)
    {
        boolean flag;
        Object aobj[];
        flag = true;
        aobj = glutessellatorimpl.cache;
        int i = glutessellatorimpl.cacheCount;
        aobj = new double[3];
        if(glutessellatorimpl.cacheCount >= 3) goto _L2; else goto _L1
_L1:
        return flag;
_L2:
        aobj[0] = glutessellatorimpl.normal[0];
        aobj[1] = glutessellatorimpl.normal[1];
        aobj[2] = glutessellatorimpl.normal[2];
        if(aobj[0] == 0.0D && aobj[1] == 0.0D && aobj[2] == 0.0D)
            ComputeNormal(glutessellatorimpl, ((double []) (aobj)), false);
        int j = ComputeNormal(glutessellatorimpl, ((double []) (aobj)), true);
        if(j == 2)
            flag = false;
        else
        if(j != 0)
            flag = false;
        if(true) goto _L1; else goto _L3
_L3:
    }

    public static void __gl_renderMesh(GLUtessellatorImpl glutessellatorimpl, GLUmesh glumesh)
    {
        glutessellatorimpl.lonelyTriList = null;
        for(GLUface gluface = glumesh.fHead.next; gluface != glumesh.fHead; gluface = gluface.next)
            gluface.marked = false;

        for(GLUface gluface1 = glumesh.fHead.next; gluface1 != glumesh.fHead; gluface1 = gluface1.next)
        {
            if(!gluface1.inside || gluface1.marked)
                continue;
            RenderMaximumFaceGroup(glutessellatorimpl, gluface1);
            if(!$assertionsDisabled && !gluface1.marked)
                throw new AssertionError();
        }

        if(glutessellatorimpl.lonelyTriList != null)
        {
            RenderLonelyTriangles(glutessellatorimpl, glutessellatorimpl.lonelyTriList);
            glutessellatorimpl.lonelyTriList = null;
        }
    }

    static final boolean $assertionsDisabled;
    private static final int SIGN_INCONSISTENT = 2;
    private static final boolean USE_OPTIMIZED_CODE_PATH = false;
    private static final RenderFan renderFan = new RenderFan();
    private static final RenderStrip renderStrip = new RenderStrip();
    private static final RenderTriangle renderTriangle = new RenderTriangle();

    static 
    {
        boolean flag;
        if(!processing/opengl/tess/Render.desiredAssertionStatus())
            flag = true;
        else
            flag = false;
        $assertionsDisabled = flag;
    }


}
