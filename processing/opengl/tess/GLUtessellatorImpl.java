// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.opengl.tess;


// Referenced classes of package processing.opengl.tess:
//            PGLUtessellator, PGLUtessellatorCallbackAdapter, CachedVertex, Mesh, 
//            GLUhalfEdge, GLUvertex, PGLUtessellatorCallback, Render, 
//            Normal, Sweep, TessMono, Dict, 
//            GLUface, GLUmesh, PriorityQ

public class GLUtessellatorImpl
    implements PGLUtessellator
{

    private GLUtessellatorImpl()
    {
        int i = 0;
        super();
        normal = new double[3];
        sUnit = new double[3];
        tUnit = new double[3];
        cache = new CachedVertex[100];
        state = 0;
        normal[0] = 0.0D;
        normal[1] = 0.0D;
        normal[2] = 0.0D;
        relTolerance = 0.0D;
        windingRule = 0x18722;
        flagBoundary = false;
        boundaryOnly = false;
        callBegin = NULL_CB;
        callEdgeFlag = NULL_CB;
        callVertex = NULL_CB;
        callEnd = NULL_CB;
        callError = NULL_CB;
        callCombine = NULL_CB;
        callBeginData = NULL_CB;
        callEdgeFlagData = NULL_CB;
        callVertexData = NULL_CB;
        callEndData = NULL_CB;
        callErrorData = NULL_CB;
        callCombineData = NULL_CB;
        polygonData = null;
        for(; i < cache.length; i++)
            cache[i] = new CachedVertex();

    }

    private boolean addVertex(double ad[], Object obj)
    {
        GLUhalfEdge gluhalfedge = lastEdge;
        if(gluhalfedge != null) goto _L2; else goto _L1
_L1:
        GLUhalfEdge gluhalfedge1 = Mesh.__gl_meshMakeEdge(mesh);
        if(gluhalfedge1 != null) goto _L4; else goto _L3
_L3:
        boolean flag = false;
_L7:
        return flag;
_L4:
        gluhalfedge = gluhalfedge1;
        if(!Mesh.__gl_meshSplice(gluhalfedge1, gluhalfedge1.Sym))
        {
            flag = false;
            continue; /* Loop/switch isn't completed */
        }
          goto _L5
_L2:
        if(Mesh.__gl_meshSplitEdge(gluhalfedge) == null)
        {
            flag = false;
            continue; /* Loop/switch isn't completed */
        }
        gluhalfedge = gluhalfedge.Lnext;
_L5:
        gluhalfedge.Org.data = obj;
        gluhalfedge.Org.coords[0] = ad[0];
        gluhalfedge.Org.coords[1] = ad[1];
        gluhalfedge.Org.coords[2] = ad[2];
        gluhalfedge.winding = 1;
        gluhalfedge.Sym.winding = -1;
        lastEdge = gluhalfedge;
        flag = true;
        if(true) goto _L7; else goto _L6
_L6:
    }

    private void cacheVertex(double ad[], Object obj)
    {
        if(cache[cacheCount] == null)
            cache[cacheCount] = new CachedVertex();
        CachedVertex cachedvertex = cache[cacheCount];
        cachedvertex.data = obj;
        cachedvertex.coords[0] = ad[0];
        cachedvertex.coords[1] = ad[1];
        cachedvertex.coords[2] = ad[2];
        cacheCount = cacheCount + 1;
    }

    private boolean flushCache()
    {
        boolean flag;
        CachedVertex acachedvertex[];
        flag = false;
        acachedvertex = cache;
        mesh = Mesh.__gl_meshNewMesh();
        if(mesh != null) goto _L2; else goto _L1
_L1:
        boolean flag1 = flag;
_L4:
        return flag1;
_L2:
        for(int i = 0; i < cacheCount; i++)
        {
            CachedVertex cachedvertex = acachedvertex[i];
            flag1 = flag;
            if(!addVertex(cachedvertex.coords, cachedvertex.data))
                continue; /* Loop/switch isn't completed */
        }

        cacheCount = 0;
        flushCacheOnNextVertex = false;
        flag1 = true;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public static PGLUtessellator gluNewTess()
    {
        return new GLUtessellatorImpl();
    }

    private void gotoState(int i)
    {
        do
        {
            if(state == i)
                break;
            if(state < i)
            {
                if(state == 0)
                {
                    callErrorOrErrorData(0x18737);
                    gluTessBeginPolygon(null);
                } else
                if(state == 1)
                {
                    callErrorOrErrorData(0x18738);
                    gluTessBeginContour();
                }
            } else
            if(state == 2)
            {
                callErrorOrErrorData(0x1873a);
                gluTessEndContour();
            } else
            if(state == 1)
            {
                callErrorOrErrorData(0x18739);
                makeDormant();
            }
        } while(true);
    }

    private void makeDormant()
    {
        if(mesh != null)
            Mesh.__gl_meshDeleteMesh(mesh);
        state = 0;
        lastEdge = null;
        mesh = null;
    }

    private void requireState(int i)
    {
        if(state != i)
            gotoState(i);
    }

    void callBeginOrBeginData(int i)
    {
        if(callBeginData != NULL_CB)
            callBeginData.beginData(i, polygonData);
        else
            callBegin.begin(i);
    }

    void callCombineOrCombineData(double ad[], Object aobj[], float af[], Object aobj1[])
    {
        if(callCombineData != NULL_CB)
            callCombineData.combineData(ad, aobj, af, aobj1, polygonData);
        else
            callCombine.combine(ad, aobj, af, aobj1);
    }

    void callEdgeFlagOrEdgeFlagData(boolean flag)
    {
        if(callEdgeFlagData != NULL_CB)
            callEdgeFlagData.edgeFlagData(flag, polygonData);
        else
            callEdgeFlag.edgeFlag(flag);
    }

    void callEndOrEndData()
    {
        if(callEndData != NULL_CB)
            callEndData.endData(polygonData);
        else
            callEnd.end();
    }

    void callErrorOrErrorData(int i)
    {
        if(callErrorData != NULL_CB)
            callErrorData.errorData(i, polygonData);
        else
            callError.error(i);
    }

    void callVertexOrVertexData(Object obj)
    {
        if(callVertexData != NULL_CB)
            callVertexData.vertexData(obj, polygonData);
        else
            callVertex.vertex(obj);
    }

    public void gluBeginPolygon()
    {
        gluTessBeginPolygon(null);
        gluTessBeginContour();
    }

    public void gluDeleteTess()
    {
        requireState(0);
    }

    public void gluEndPolygon()
    {
        gluTessEndContour();
        gluTessEndPolygon();
    }

    public void gluGetTessProperty(int i, double ad[], int j)
    {
        double d = 1.0D;
        i;
        JVM INSTR lookupswitch 4: default 48
    //                   100140: 102
    //                   100141: 176
    //                   100142: 60
    //                   100149: 226;
           goto _L1 _L2 _L3 _L4 _L5
_L1:
        ad[j] = 0.0D;
        callErrorOrErrorData(0x18a24);
_L7:
        return;
_L4:
        if(!$assertionsDisabled && (0.0D > relTolerance || relTolerance > 1.0D))
            throw new AssertionError();
        ad[j] = relTolerance;
        continue; /* Loop/switch isn't completed */
_L2:
        if(!$assertionsDisabled && windingRule != 0x18722 && windingRule != 0x18723 && windingRule != 0x18724 && windingRule != 0x18725 && windingRule != 0x18726)
            throw new AssertionError();
        ad[j] = windingRule;
        continue; /* Loop/switch isn't completed */
_L3:
        if(!$assertionsDisabled && !boundaryOnly && boundaryOnly)
            throw new AssertionError();
        if(!boundaryOnly)
            d = 0.0D;
        ad[j] = d;
        continue; /* Loop/switch isn't completed */
_L5:
        if(!avoidDegenerateTris)
            d = 0.0D;
        ad[j] = d;
        if(true) goto _L7; else goto _L6
_L6:
    }

    public void gluNextContour(int i)
    {
        gluTessEndContour();
        gluTessBeginContour();
    }

    public void gluTessBeginContour()
    {
        requireState(1);
        state = 2;
        lastEdge = null;
        if(cacheCount > 0)
            flushCacheOnNextVertex = true;
    }

    public void gluTessBeginPolygon(Object obj)
    {
        requireState(0);
        state = 1;
        cacheCount = 0;
        flushCacheOnNextVertex = false;
        mesh = null;
        polygonData = obj;
    }

    public void gluTessCallback(int i, PGLUtessellatorCallback pglutessellatorcallback)
    {
        boolean flag = true;
        i;
        JVM INSTR tableswitch 100100 100111: default 64
    //                   100100 72
    //                   100101 198
    //                   100102 240
    //                   100103 282
    //                   100104 114
    //                   100105 324
    //                   100106 93
    //                   100107 219
    //                   100108 261
    //                   100109 303
    //                   100110 154
    //                   100111 345;
           goto _L1 _L2 _L3 _L4 _L5 _L6 _L7 _L8 _L9 _L10 _L11 _L12 _L13
_L1:
        callErrorOrErrorData(0x18a24);
_L15:
        return;
_L2:
        PGLUtessellatorCallback pglutessellatorcallback1 = pglutessellatorcallback;
        if(pglutessellatorcallback == null)
            pglutessellatorcallback1 = NULL_CB;
        callBegin = pglutessellatorcallback1;
        continue; /* Loop/switch isn't completed */
_L8:
        PGLUtessellatorCallback pglutessellatorcallback2 = pglutessellatorcallback;
        if(pglutessellatorcallback == null)
            pglutessellatorcallback2 = NULL_CB;
        callBeginData = pglutessellatorcallback2;
        continue; /* Loop/switch isn't completed */
_L6:
        PGLUtessellatorCallback pglutessellatorcallback3;
        if(pglutessellatorcallback == null)
            pglutessellatorcallback3 = NULL_CB;
        else
            pglutessellatorcallback3 = pglutessellatorcallback;
        callEdgeFlag = pglutessellatorcallback3;
        if(pglutessellatorcallback != null)
            flag = true;
        else
            flag = false;
        flagBoundary = flag;
        continue; /* Loop/switch isn't completed */
_L12:
        PGLUtessellatorCallback pglutessellatorcallback4;
        if(pglutessellatorcallback == null)
            pglutessellatorcallback4 = NULL_CB;
        else
            pglutessellatorcallback4 = pglutessellatorcallback;
        callBegin = pglutessellatorcallback4;
        callEdgeFlagData = pglutessellatorcallback4;
        if(pglutessellatorcallback == null)
            flag = false;
        flagBoundary = flag;
        continue; /* Loop/switch isn't completed */
_L3:
        PGLUtessellatorCallback pglutessellatorcallback5 = pglutessellatorcallback;
        if(pglutessellatorcallback == null)
            pglutessellatorcallback5 = NULL_CB;
        callVertex = pglutessellatorcallback5;
        continue; /* Loop/switch isn't completed */
_L9:
        PGLUtessellatorCallback pglutessellatorcallback6 = pglutessellatorcallback;
        if(pglutessellatorcallback == null)
            pglutessellatorcallback6 = NULL_CB;
        callVertexData = pglutessellatorcallback6;
        continue; /* Loop/switch isn't completed */
_L4:
        PGLUtessellatorCallback pglutessellatorcallback7 = pglutessellatorcallback;
        if(pglutessellatorcallback == null)
            pglutessellatorcallback7 = NULL_CB;
        callEnd = pglutessellatorcallback7;
        continue; /* Loop/switch isn't completed */
_L10:
        PGLUtessellatorCallback pglutessellatorcallback8 = pglutessellatorcallback;
        if(pglutessellatorcallback == null)
            pglutessellatorcallback8 = NULL_CB;
        callEndData = pglutessellatorcallback8;
        continue; /* Loop/switch isn't completed */
_L5:
        PGLUtessellatorCallback pglutessellatorcallback9 = pglutessellatorcallback;
        if(pglutessellatorcallback == null)
            pglutessellatorcallback9 = NULL_CB;
        callError = pglutessellatorcallback9;
        continue; /* Loop/switch isn't completed */
_L11:
        PGLUtessellatorCallback pglutessellatorcallback10 = pglutessellatorcallback;
        if(pglutessellatorcallback == null)
            pglutessellatorcallback10 = NULL_CB;
        callErrorData = pglutessellatorcallback10;
        continue; /* Loop/switch isn't completed */
_L7:
        PGLUtessellatorCallback pglutessellatorcallback11 = pglutessellatorcallback;
        if(pglutessellatorcallback == null)
            pglutessellatorcallback11 = NULL_CB;
        callCombine = pglutessellatorcallback11;
        continue; /* Loop/switch isn't completed */
_L13:
        PGLUtessellatorCallback pglutessellatorcallback12 = pglutessellatorcallback;
        if(pglutessellatorcallback == null)
            pglutessellatorcallback12 = NULL_CB;
        callCombineData = pglutessellatorcallback12;
        if(true) goto _L15; else goto _L14
_L14:
    }

    public void gluTessEndContour()
    {
        requireState(2);
        state = 1;
    }

    public void gluTessEndPolygon()
    {
        requireState(1);
        state = 0;
        if(mesh != null) goto _L2; else goto _L1
_L1:
        if(flagBoundary || !Render.__gl_renderCache(this)) goto _L4; else goto _L3
_L3:
        polygonData = null;
_L5:
        return;
_L4:
        if(!flushCache())
        {
            RuntimeException runtimeexception = JVM INSTR new #298 <Class RuntimeException>;
            runtimeexception.RuntimeException();
            throw runtimeexception;
        }
          goto _L2
        Exception exception;
        exception;
        exception.printStackTrace();
        callErrorOrErrorData(0x18a26);
          goto _L5
_L2:
        Object obj;
        boolean flag;
        Normal.__gl_projectPolygon(this);
        if(!Sweep.__gl_computeInterior(this))
        {
            RuntimeException runtimeexception1 = JVM INSTR new #298 <Class RuntimeException>;
            runtimeexception1.RuntimeException();
            throw runtimeexception1;
        }
        obj = mesh;
        if(fatalError)
            break MISSING_BLOCK_LABEL_238;
        if(!boundaryOnly)
            break MISSING_BLOCK_LABEL_130;
        flag = TessMono.__gl_meshSetWindingNumber(((GLUmesh) (obj)), 1, true);
_L6:
        if(flag)
            break MISSING_BLOCK_LABEL_142;
        obj = JVM INSTR new #298 <Class RuntimeException>;
        ((RuntimeException) (obj)).RuntimeException();
        throw obj;
        flag = TessMono.__gl_meshTessellateInterior(((GLUmesh) (obj)), avoidDegenerateTris);
          goto _L6
        Mesh.__gl_meshCheckMesh(((GLUmesh) (obj)));
        if(callBegin != NULL_CB || callEnd != NULL_CB || callVertex != NULL_CB || callEdgeFlag != NULL_CB || callBeginData != NULL_CB || callEndData != NULL_CB || callVertexData != NULL_CB || callEdgeFlagData != NULL_CB)
        {
            if(!boundaryOnly)
                break MISSING_BLOCK_LABEL_250;
            Render.__gl_renderBoundary(this, ((GLUmesh) (obj)));
        }
_L7:
        Mesh.__gl_meshDeleteMesh(((GLUmesh) (obj)));
        polygonData = null;
          goto _L5
        Render.__gl_renderMesh(this, ((GLUmesh) (obj)));
          goto _L7
    }

    public void gluTessNormal(double d, double d1, double d2)
    {
        normal[0] = d;
        normal[1] = d1;
        normal[2] = d2;
    }

    public void gluTessProperty(int i, double d)
    {
        boolean flag;
        boolean flag1;
        flag = true;
        flag1 = true;
        i;
        JVM INSTR lookupswitch 4: default 48
    //                   100140: 86
    //                   100141: 132
    //                   100142: 56
    //                   100149: 161;
           goto _L1 _L2 _L3 _L4 _L5
_L1:
        callErrorOrErrorData(0x18a24);
_L15:
        return;
_L4:
        if(d >= 0.0D && d <= 1.0D) goto _L7; else goto _L6
_L6:
        callErrorOrErrorData(0x18a25);
          goto _L8
_L7:
        relTolerance = d;
          goto _L8
_L2:
        i = (int)d;
        if((double)i != d) goto _L6; else goto _L9
_L9:
        i;
        JVM INSTR tableswitch 100130 100134: default 132
    //                   100130 147
    //                   100131 147
    //                   100132 147
    //                   100133 147
    //                   100134 147;
           goto _L3 _L10 _L10 _L10 _L10 _L10
        if(true) goto _L8; else goto _L11
_L8:
        if(true) goto _L12; else goto _L11
_L12:
        if(true) goto _L13; else goto _L11
_L13:
        if(true)
            continue; /* Loop/switch isn't completed */
_L11:
_L3:
        if(d == 0.0D)
            flag1 = false;
        boundaryOnly = flag1;
        continue; /* Loop/switch isn't completed */
_L10:
        windingRule = i;
        continue; /* Loop/switch isn't completed */
_L5:
        boolean flag2;
        if(d != 0.0D)
            flag2 = flag;
        else
            flag2 = false;
        avoidDegenerateTris = flag2;
        if(true) goto _L15; else goto _L14
_L14:
    }

    public void gluTessVertex(double ad[], int i, Object obj)
    {
        boolean flag;
        double ad1[];
        flag = false;
        ad1 = new double[3];
        requireState(2);
        if(!flushCacheOnNextVertex) goto _L2; else goto _L1
_L1:
        if(flushCache()) goto _L4; else goto _L3
_L3:
        callErrorOrErrorData(0x18a26);
_L6:
        return;
_L4:
        lastEdge = null;
_L2:
        for(int j = 0; j < 3; j++)
        {
            double d = ad[j + i];
            double d1 = d;
            if(d < -9.9999999999999998E+149D)
            {
                d1 = -9.9999999999999998E+149D;
                flag = true;
            }
            d = d1;
            if(d1 > 9.9999999999999998E+149D)
            {
                d = 9.9999999999999998E+149D;
                flag = true;
            }
            ad1[j] = d;
        }

        if(flag)
            callErrorOrErrorData(0x1873b);
        if(mesh == null)
        {
            if(cacheCount < 100)
            {
                cacheVertex(ad1, obj);
                continue; /* Loop/switch isn't completed */
            }
            if(!flushCache())
            {
                callErrorOrErrorData(0x18a26);
                continue; /* Loop/switch isn't completed */
            }
        }
        if(!addVertex(ad1, obj))
            callErrorOrErrorData(0x18a26);
        if(true) goto _L6; else goto _L5
_L5:
    }

    static final boolean $assertionsDisabled;
    private static final double GLU_TESS_DEFAULT_TOLERANCE = 0D;
    private static PGLUtessellatorCallback NULL_CB = new PGLUtessellatorCallbackAdapter();
    public static final int TESS_MAX_CACHE = 100;
    boolean avoidDegenerateTris;
    boolean boundaryOnly;
    CachedVertex cache[];
    int cacheCount;
    private PGLUtessellatorCallback callBegin;
    private PGLUtessellatorCallback callBeginData;
    private PGLUtessellatorCallback callCombine;
    private PGLUtessellatorCallback callCombineData;
    private PGLUtessellatorCallback callEdgeFlag;
    private PGLUtessellatorCallback callEdgeFlagData;
    private PGLUtessellatorCallback callEnd;
    private PGLUtessellatorCallback callEndData;
    private PGLUtessellatorCallback callError;
    private PGLUtessellatorCallback callErrorData;
    private PGLUtessellatorCallback callVertex;
    private PGLUtessellatorCallback callVertexData;
    Dict dict;
    GLUvertex event;
    boolean fatalError;
    boolean flagBoundary;
    private boolean flushCacheOnNextVertex;
    private GLUhalfEdge lastEdge;
    GLUface lonelyTriList;
    GLUmesh mesh;
    double normal[];
    private Object polygonData;
    PriorityQ pq;
    private double relTolerance;
    double sUnit[];
    private int state;
    double tUnit[];
    int windingRule;

    static 
    {
        boolean flag;
        if(!processing/opengl/tess/GLUtessellatorImpl.desiredAssertionStatus())
            flag = true;
        else
            flag = false;
        $assertionsDisabled = flag;
    }
}
