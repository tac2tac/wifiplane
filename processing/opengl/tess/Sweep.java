// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.opengl.tess;


// Referenced classes of package processing.opengl.tess:
//            ActiveRegion, GLUtessellatorImpl, Dict, GLUhalfEdge, 
//            Geom, Mesh, GLUvertex, PriorityQ, 
//            GLUface, GLUmesh

class Sweep
{

    private Sweep()
    {
    }

    static ActiveRegion AddRegionBelow(GLUtessellatorImpl glutessellatorimpl, ActiveRegion activeregion, GLUhalfEdge gluhalfedge)
    {
        ActiveRegion activeregion1 = new ActiveRegion();
        if(activeregion1 == null)
            throw new RuntimeException();
        activeregion1.eUp = gluhalfedge;
        activeregion1.nodeUp = Dict.dictInsertBefore(glutessellatorimpl.dict, activeregion.nodeUp, activeregion1);
        if(activeregion1.nodeUp == null)
        {
            throw new RuntimeException();
        } else
        {
            activeregion1.fixUpperEdge = false;
            activeregion1.sentinel = false;
            activeregion1.dirty = false;
            gluhalfedge.activeRegion = activeregion1;
            return activeregion1;
        }
    }

    static void AddRightEdges(GLUtessellatorImpl glutessellatorimpl, ActiveRegion activeregion, GLUhalfEdge gluhalfedge, GLUhalfEdge gluhalfedge1, GLUhalfEdge gluhalfedge2, boolean flag)
    {
        GLUhalfEdge gluhalfedge3;
        do
        {
            if(!$assertionsDisabled && !Geom.VertLeq(gluhalfedge.Org, gluhalfedge.Sym.Org))
                throw new AssertionError();
            AddRegionBelow(glutessellatorimpl, activeregion, gluhalfedge.Sym);
            gluhalfedge3 = gluhalfedge.Onext;
            gluhalfedge = gluhalfedge3;
        } while(gluhalfedge3 != gluhalfedge1);
        gluhalfedge = gluhalfedge2;
        if(gluhalfedge2 == null)
            gluhalfedge = RegionBelow(activeregion).eUp.Sym.Onext;
        boolean flag1 = true;
        do
        {
            gluhalfedge1 = RegionBelow(activeregion);
            gluhalfedge2 = ((ActiveRegion) (gluhalfedge1)).eUp.Sym;
            if(gluhalfedge2.Org != gluhalfedge.Org)
            {
                activeregion.dirty = true;
                if(!$assertionsDisabled && activeregion.windingNumber - gluhalfedge2.winding != ((ActiveRegion) (gluhalfedge1)).windingNumber)
                    throw new AssertionError();
                break;
            }
            if(gluhalfedge2.Onext != gluhalfedge)
            {
                if(!Mesh.__gl_meshSplice(gluhalfedge2.Sym.Lnext, gluhalfedge2))
                    throw new RuntimeException();
                if(!Mesh.__gl_meshSplice(gluhalfedge.Sym.Lnext, gluhalfedge2))
                    throw new RuntimeException();
            }
            gluhalfedge1.windingNumber = activeregion.windingNumber - gluhalfedge2.winding;
            gluhalfedge1.inside = IsWindingInside(glutessellatorimpl, ((ActiveRegion) (gluhalfedge1)).windingNumber);
            activeregion.dirty = true;
            if(!flag1 && CheckForRightSplice(glutessellatorimpl, activeregion))
            {
                AddWinding(gluhalfedge2, gluhalfedge);
                DeleteRegion(glutessellatorimpl, activeregion);
                if(!Mesh.__gl_meshDelete(gluhalfedge))
                    throw new RuntimeException();
            }
            flag1 = false;
            gluhalfedge = gluhalfedge2;
            activeregion = gluhalfedge1;
        } while(true);
        if(flag)
            WalkDirtyRegions(glutessellatorimpl, activeregion);
    }

    static void AddSentinel(GLUtessellatorImpl glutessellatorimpl, double d)
    {
        ActiveRegion activeregion = new ActiveRegion();
        if(activeregion == null)
            throw new RuntimeException();
        GLUhalfEdge gluhalfedge = Mesh.__gl_meshMakeEdge(glutessellatorimpl.mesh);
        if(gluhalfedge == null)
            throw new RuntimeException();
        gluhalfedge.Org.s = 3.9999999999999999E+150D;
        gluhalfedge.Org.t = d;
        gluhalfedge.Sym.Org.s = -3.9999999999999999E+150D;
        gluhalfedge.Sym.Org.t = d;
        glutessellatorimpl.event = gluhalfedge.Sym.Org;
        activeregion.eUp = gluhalfedge;
        activeregion.windingNumber = 0;
        activeregion.inside = false;
        activeregion.fixUpperEdge = false;
        activeregion.sentinel = true;
        activeregion.dirty = false;
        activeregion.nodeUp = Dict.dictInsert(glutessellatorimpl.dict, activeregion);
        if(activeregion.nodeUp == null)
            throw new RuntimeException();
        else
            return;
    }

    private static void AddWinding(GLUhalfEdge gluhalfedge, GLUhalfEdge gluhalfedge1)
    {
        gluhalfedge.winding = gluhalfedge.winding + gluhalfedge1.winding;
        gluhalfedge = gluhalfedge.Sym;
        gluhalfedge.winding = gluhalfedge.winding + gluhalfedge1.Sym.winding;
    }

    static void CallCombine(GLUtessellatorImpl glutessellatorimpl, GLUvertex gluvertex, Object aobj[], float af[], boolean flag)
    {
        double d = gluvertex.coords[0];
        double d1 = gluvertex.coords[1];
        double d2 = gluvertex.coords[2];
        Object aobj1[] = new Object[1];
        glutessellatorimpl.callCombineOrCombineData(new double[] {
            d, d1, d2
        }, aobj, af, aobj1);
        gluvertex.data = aobj1[0];
        if(gluvertex.data != null) goto _L2; else goto _L1
_L1:
        if(flag) goto _L4; else goto _L3
_L3:
        gluvertex.data = aobj[0];
_L2:
        return;
_L4:
        if(!glutessellatorimpl.fatalError)
        {
            glutessellatorimpl.callErrorOrErrorData(0x1873c);
            glutessellatorimpl.fatalError = true;
        }
        if(true) goto _L2; else goto _L5
_L5:
    }

    static boolean CheckForIntersect(GLUtessellatorImpl glutessellatorimpl, ActiveRegion activeregion)
    {
        ActiveRegion activeregion1;
        GLUhalfEdge gluhalfedge;
        GLUhalfEdge gluhalfedge1;
        Object obj;
        GLUvertex gluvertex;
        GLUvertex gluvertex1;
        GLUvertex gluvertex2;
        GLUvertex gluvertex3;
        activeregion1 = RegionBelow(activeregion);
        gluhalfedge = activeregion.eUp;
        gluhalfedge1 = activeregion1.eUp;
        obj = gluhalfedge.Org;
        gluvertex = gluhalfedge1.Org;
        gluvertex1 = gluhalfedge.Sym.Org;
        gluvertex2 = gluhalfedge1.Sym.Org;
        gluvertex3 = new GLUvertex();
        if(!$assertionsDisabled && Geom.VertEq(gluvertex2, gluvertex1))
            throw new AssertionError();
        if(!$assertionsDisabled && Geom.EdgeSign(gluvertex1, glutessellatorimpl.event, ((GLUvertex) (obj))) > 0.0D)
            throw new AssertionError();
        if(!$assertionsDisabled && Geom.EdgeSign(gluvertex2, glutessellatorimpl.event, gluvertex) < 0.0D)
            throw new AssertionError();
        if(!$assertionsDisabled && (obj == glutessellatorimpl.event || gluvertex == glutessellatorimpl.event))
            throw new AssertionError();
        if(!$assertionsDisabled && (activeregion.fixUpperEdge || activeregion1.fixUpperEdge))
            throw new AssertionError();
        if(obj != gluvertex) goto _L2; else goto _L1
_L1:
        boolean flag = false;
_L4:
        return flag;
_L2:
        if(Math.min(((GLUvertex) (obj)).t, gluvertex1.t) > Math.max(gluvertex.t, gluvertex2.t))
        {
            flag = false;
            continue; /* Loop/switch isn't completed */
        }
        if(Geom.VertLeq(((GLUvertex) (obj)), gluvertex))
        {
            if(Geom.EdgeSign(gluvertex2, ((GLUvertex) (obj)), gluvertex) > 0.0D)
            {
                flag = false;
                continue; /* Loop/switch isn't completed */
            }
        } else
        if(Geom.EdgeSign(gluvertex1, gluvertex, ((GLUvertex) (obj))) < 0.0D)
        {
            flag = false;
            continue; /* Loop/switch isn't completed */
        }
        DebugEvent(glutessellatorimpl);
        Geom.EdgeIntersect(gluvertex1, ((GLUvertex) (obj)), gluvertex2, gluvertex, gluvertex3);
        if(!$assertionsDisabled && Math.min(((GLUvertex) (obj)).t, gluvertex1.t) > gluvertex3.t)
            throw new AssertionError();
        if(!$assertionsDisabled && gluvertex3.t > Math.max(gluvertex.t, gluvertex2.t))
            throw new AssertionError();
        if(!$assertionsDisabled && Math.min(gluvertex2.s, gluvertex1.s) > gluvertex3.s)
            throw new AssertionError();
        if(!$assertionsDisabled && gluvertex3.s > Math.max(gluvertex.s, ((GLUvertex) (obj)).s))
            throw new AssertionError();
        if(Geom.VertLeq(gluvertex3, glutessellatorimpl.event))
        {
            gluvertex3.s = glutessellatorimpl.event.s;
            gluvertex3.t = glutessellatorimpl.event.t;
        }
        Object obj1;
        if(Geom.VertLeq(((GLUvertex) (obj)), gluvertex))
            obj1 = obj;
        else
            obj1 = gluvertex;
        if(Geom.VertLeq(((GLUvertex) (obj1)), gluvertex3))
        {
            gluvertex3.s = ((GLUvertex) (obj1)).s;
            gluvertex3.t = ((GLUvertex) (obj1)).t;
        }
        if(Geom.VertEq(gluvertex3, ((GLUvertex) (obj))) || Geom.VertEq(gluvertex3, gluvertex))
        {
            CheckForRightSplice(glutessellatorimpl, activeregion);
            flag = false;
        } else
        if(!Geom.VertEq(gluvertex1, glutessellatorimpl.event) && Geom.EdgeSign(gluvertex1, glutessellatorimpl.event, gluvertex3) >= 0.0D || !Geom.VertEq(gluvertex2, glutessellatorimpl.event) && Geom.EdgeSign(gluvertex2, glutessellatorimpl.event, gluvertex3) <= 0.0D)
        {
            if(gluvertex2 == glutessellatorimpl.event)
            {
                if(Mesh.__gl_meshSplitEdge(gluhalfedge.Sym) == null)
                    throw new RuntimeException();
                if(!Mesh.__gl_meshSplice(gluhalfedge1.Sym, gluhalfedge))
                    throw new RuntimeException();
                ActiveRegion activeregion2 = TopLeftRegion(activeregion);
                if(activeregion2 == null)
                    throw new RuntimeException();
                activeregion = RegionBelow(activeregion2).eUp;
                FinishLeftRegions(glutessellatorimpl, RegionBelow(activeregion2), activeregion1);
                AddRightEdges(glutessellatorimpl, activeregion2, ((GLUhalfEdge) (activeregion)).Sym.Lnext, activeregion, activeregion, true);
                flag = true;
            } else
            if(gluvertex1 == glutessellatorimpl.event)
            {
                if(Mesh.__gl_meshSplitEdge(gluhalfedge1.Sym) == null)
                    throw new RuntimeException();
                if(!Mesh.__gl_meshSplice(gluhalfedge.Lnext, gluhalfedge1.Sym.Lnext))
                    throw new RuntimeException();
                ActiveRegion activeregion3 = TopRightRegion(activeregion);
                obj = RegionBelow(activeregion3).eUp.Sym.Onext;
                activeregion.eUp = gluhalfedge1.Sym.Lnext;
                AddRightEdges(glutessellatorimpl, activeregion3, FinishLeftRegions(glutessellatorimpl, activeregion, null).Onext, gluhalfedge.Sym.Onext, ((GLUhalfEdge) (obj)), true);
                flag = true;
            } else
            {
                if(Geom.EdgeSign(gluvertex1, glutessellatorimpl.event, gluvertex3) >= 0.0D)
                {
                    ActiveRegion activeregion4 = RegionAbove(activeregion);
                    activeregion.dirty = true;
                    activeregion4.dirty = true;
                    if(Mesh.__gl_meshSplitEdge(gluhalfedge.Sym) == null)
                        throw new RuntimeException();
                    gluhalfedge.Org.s = glutessellatorimpl.event.s;
                    gluhalfedge.Org.t = glutessellatorimpl.event.t;
                }
                if(Geom.EdgeSign(gluvertex2, glutessellatorimpl.event, gluvertex3) <= 0.0D)
                {
                    activeregion1.dirty = true;
                    activeregion.dirty = true;
                    if(Mesh.__gl_meshSplitEdge(gluhalfedge1.Sym) == null)
                        throw new RuntimeException();
                    gluhalfedge1.Org.s = glutessellatorimpl.event.s;
                    gluhalfedge1.Org.t = glutessellatorimpl.event.t;
                }
                flag = false;
            }
        } else
        {
            if(Mesh.__gl_meshSplitEdge(gluhalfedge.Sym) == null)
                throw new RuntimeException();
            if(Mesh.__gl_meshSplitEdge(gluhalfedge1.Sym) == null)
                throw new RuntimeException();
            if(!Mesh.__gl_meshSplice(gluhalfedge1.Sym.Lnext, gluhalfedge))
                throw new RuntimeException();
            gluhalfedge.Org.s = gluvertex3.s;
            gluhalfedge.Org.t = gluvertex3.t;
            gluhalfedge.Org.pqHandle = glutessellatorimpl.pq.pqInsert(gluhalfedge.Org);
            if((long)gluhalfedge.Org.pqHandle == 0x7fffffffffffffffL)
            {
                glutessellatorimpl.pq.pqDeletePriorityQ();
                glutessellatorimpl.pq = null;
                throw new RuntimeException();
            }
            GetIntersectData(glutessellatorimpl, gluhalfedge.Org, ((GLUvertex) (obj)), gluvertex1, gluvertex, gluvertex2);
            glutessellatorimpl = RegionAbove(activeregion);
            activeregion1.dirty = true;
            activeregion.dirty = true;
            glutessellatorimpl.dirty = true;
            flag = false;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    static boolean CheckForLeftSplice(GLUtessellatorImpl glutessellatorimpl, ActiveRegion activeregion)
    {
        boolean flag;
        Object obj;
        GLUhalfEdge gluhalfedge;
        flag = false;
        obj = RegionBelow(activeregion);
        gluhalfedge = activeregion.eUp;
        glutessellatorimpl = ((ActiveRegion) (obj)).eUp;
        if(!$assertionsDisabled && Geom.VertEq(gluhalfedge.Sym.Org, ((GLUhalfEdge) (glutessellatorimpl)).Sym.Org))
            throw new AssertionError();
        if(!Geom.VertLeq(gluhalfedge.Sym.Org, ((GLUhalfEdge) (glutessellatorimpl)).Sym.Org)) goto _L2; else goto _L1
_L1:
        if(Geom.EdgeSign(gluhalfedge.Sym.Org, ((GLUhalfEdge) (glutessellatorimpl)).Sym.Org, gluhalfedge.Org) >= 0.0D) goto _L4; else goto _L3
_L3:
        return flag;
_L4:
        obj = RegionAbove(activeregion);
        activeregion.dirty = true;
        obj.dirty = true;
        gluhalfedge = Mesh.__gl_meshSplitEdge(gluhalfedge);
        if(gluhalfedge == null)
            throw new RuntimeException();
        if(!Mesh.__gl_meshSplice(((GLUhalfEdge) (glutessellatorimpl)).Sym, gluhalfedge))
            throw new RuntimeException();
        gluhalfedge.Lface.inside = activeregion.inside;
_L6:
        flag = true;
        if(true) goto _L3; else goto _L2
_L2:
        if(Geom.EdgeSign(((GLUhalfEdge) (glutessellatorimpl)).Sym.Org, gluhalfedge.Sym.Org, ((GLUhalfEdge) (glutessellatorimpl)).Org) > 0.0D) goto _L3; else goto _L5
_L5:
        obj.dirty = true;
        activeregion.dirty = true;
        obj = Mesh.__gl_meshSplitEdge(glutessellatorimpl);
        if(obj == null)
            throw new RuntimeException();
        if(!Mesh.__gl_meshSplice(gluhalfedge.Lnext, ((GLUhalfEdge) (glutessellatorimpl)).Sym))
            throw new RuntimeException();
        ((GLUhalfEdge) (obj)).Sym.Lface.inside = activeregion.inside;
          goto _L6
    }

    static boolean CheckForRightSplice(GLUtessellatorImpl glutessellatorimpl, ActiveRegion activeregion)
    {
        boolean flag;
        ActiveRegion activeregion1;
        GLUhalfEdge gluhalfedge;
        GLUhalfEdge gluhalfedge1;
        flag = false;
        activeregion1 = RegionBelow(activeregion);
        gluhalfedge = activeregion.eUp;
        gluhalfedge1 = activeregion1.eUp;
        if(!Geom.VertLeq(gluhalfedge.Org, gluhalfedge1.Org)) goto _L2; else goto _L1
_L1:
        if(Geom.EdgeSign(gluhalfedge1.Sym.Org, gluhalfedge.Org, gluhalfedge1.Org) <= 0.0D) goto _L4; else goto _L3
_L3:
        return flag;
_L4:
        if(Geom.VertEq(gluhalfedge.Org, gluhalfedge1.Org))
            break; /* Loop/switch isn't completed */
        if(Mesh.__gl_meshSplitEdge(gluhalfedge1.Sym) == null)
            throw new RuntimeException();
        if(!Mesh.__gl_meshSplice(gluhalfedge, gluhalfedge1.Sym.Lnext))
            throw new RuntimeException();
        activeregion1.dirty = true;
        activeregion.dirty = true;
_L7:
        flag = true;
        if(true) goto _L3; else goto _L5
_L5:
        if(gluhalfedge.Org == gluhalfedge1.Org) goto _L7; else goto _L6
_L6:
        glutessellatorimpl.pq.pqDelete(gluhalfedge.Org.pqHandle);
        SpliceMergeVertices(glutessellatorimpl, gluhalfedge1.Sym.Lnext, gluhalfedge);
          goto _L7
_L2:
        if(Geom.EdgeSign(gluhalfedge.Sym.Org, gluhalfedge1.Org, gluhalfedge.Org) < 0.0D) goto _L3; else goto _L8
_L8:
        glutessellatorimpl = RegionAbove(activeregion);
        activeregion.dirty = true;
        glutessellatorimpl.dirty = true;
        if(Mesh.__gl_meshSplitEdge(gluhalfedge.Sym) == null)
            throw new RuntimeException();
        if(!Mesh.__gl_meshSplice(gluhalfedge1.Sym.Lnext, gluhalfedge))
            throw new RuntimeException();
          goto _L7
    }

    static void ComputeWinding(GLUtessellatorImpl glutessellatorimpl, ActiveRegion activeregion)
    {
        activeregion.windingNumber = RegionAbove(activeregion).windingNumber + activeregion.eUp.winding;
        activeregion.inside = IsWindingInside(glutessellatorimpl, activeregion.windingNumber);
    }

    static void ConnectLeftDegenerate(GLUtessellatorImpl glutessellatorimpl, ActiveRegion activeregion, GLUvertex gluvertex)
    {
        GLUhalfEdge gluhalfedge = activeregion.eUp;
        if(Geom.VertEq(gluhalfedge.Org, gluvertex))
        {
            if(!$assertionsDisabled)
                throw new AssertionError();
            SpliceMergeVertices(glutessellatorimpl, gluhalfedge, gluvertex.anEdge);
        } else
        if(!Geom.VertEq(gluhalfedge.Sym.Org, gluvertex))
        {
            if(Mesh.__gl_meshSplitEdge(gluhalfedge.Sym) == null)
                throw new RuntimeException();
            if(activeregion.fixUpperEdge)
            {
                if(!Mesh.__gl_meshDelete(gluhalfedge.Onext))
                    throw new RuntimeException();
                activeregion.fixUpperEdge = false;
            }
            if(!Mesh.__gl_meshSplice(gluvertex.anEdge, gluhalfedge))
                throw new RuntimeException();
            SweepEvent(glutessellatorimpl, gluvertex);
        } else
        {
            if(!$assertionsDisabled)
                throw new AssertionError();
            ActiveRegion activeregion1 = TopRightRegion(activeregion);
            ActiveRegion activeregion2 = RegionBelow(activeregion1);
            GLUhalfEdge gluhalfedge2 = activeregion2.eUp.Sym;
            GLUhalfEdge gluhalfedge1 = gluhalfedge2.Onext;
            activeregion = gluhalfedge2;
            if(activeregion2.fixUpperEdge)
            {
                if(!$assertionsDisabled && gluhalfedge1 == gluhalfedge2)
                    throw new AssertionError();
                DeleteRegion(glutessellatorimpl, activeregion2);
                if(!Mesh.__gl_meshDelete(gluhalfedge2))
                    throw new RuntimeException();
                activeregion = gluhalfedge1.Sym.Lnext;
            }
            if(!Mesh.__gl_meshSplice(gluvertex.anEdge, activeregion))
                throw new RuntimeException();
            if(!Geom.EdgeGoesLeft(gluhalfedge1))
                gluvertex = null;
            else
                gluvertex = gluhalfedge1;
            AddRightEdges(glutessellatorimpl, activeregion1, ((GLUhalfEdge) (activeregion)).Onext, gluhalfedge1, gluvertex, true);
        }
    }

    static void ConnectLeftVertex(GLUtessellatorImpl glutessellatorimpl, GLUvertex gluvertex)
    {
        ActiveRegion activeregion = new ActiveRegion();
        activeregion.eUp = gluvertex.anEdge.Sym;
        ActiveRegion activeregion1 = (ActiveRegion)Dict.dictKey(Dict.dictSearch(glutessellatorimpl.dict, activeregion));
        activeregion = RegionBelow(activeregion1);
        GLUhalfEdge gluhalfedge = activeregion1.eUp;
        GLUhalfEdge gluhalfedge1 = activeregion.eUp;
        if(Geom.EdgeSign(gluhalfedge.Sym.Org, gluvertex, gluhalfedge.Org) == 0.0D)
        {
            ConnectLeftDegenerate(glutessellatorimpl, activeregion1, gluvertex);
        } else
        {
            if(Geom.VertLeq(gluhalfedge1.Sym.Org, gluhalfedge.Sym.Org))
                activeregion = activeregion1;
            if(activeregion1.inside || activeregion.fixUpperEdge)
            {
                if(activeregion == activeregion1)
                {
                    gluhalfedge1 = Mesh.__gl_meshConnect(gluvertex.anEdge.Sym, gluhalfedge.Lnext);
                    gluhalfedge = gluhalfedge1;
                    if(gluhalfedge1 == null)
                        throw new RuntimeException();
                } else
                {
                    gluhalfedge = Mesh.__gl_meshConnect(gluhalfedge1.Sym.Onext.Sym, gluvertex.anEdge);
                    if(gluhalfedge == null)
                        throw new RuntimeException();
                    gluhalfedge = gluhalfedge.Sym;
                }
                if(activeregion.fixUpperEdge)
                {
                    if(!FixUpperEdge(activeregion, gluhalfedge))
                        throw new RuntimeException();
                } else
                {
                    ComputeWinding(glutessellatorimpl, AddRegionBelow(glutessellatorimpl, activeregion1, gluhalfedge));
                }
                SweepEvent(glutessellatorimpl, gluvertex);
            } else
            {
                AddRightEdges(glutessellatorimpl, activeregion1, gluvertex.anEdge, gluvertex.anEdge, null, true);
            }
        }
    }

    static void ConnectRightVertex(GLUtessellatorImpl glutessellatorimpl, ActiveRegion activeregion, GLUhalfEdge gluhalfedge)
    {
        GLUhalfEdge gluhalfedge1 = gluhalfedge.Onext;
        ActiveRegion activeregion1 = RegionBelow(activeregion);
        GLUhalfEdge gluhalfedge2 = activeregion.eUp;
        GLUhalfEdge gluhalfedge3 = activeregion1.eUp;
        if(gluhalfedge2.Sym.Org != gluhalfedge3.Sym.Org)
            CheckForIntersect(glutessellatorimpl, activeregion);
        boolean flag;
        GLUhalfEdge gluhalfedge4;
        if(Geom.VertEq(gluhalfedge2.Org, glutessellatorimpl.event))
        {
            if(!Mesh.__gl_meshSplice(gluhalfedge1.Sym.Lnext, gluhalfedge2))
                throw new RuntimeException();
            activeregion = TopLeftRegion(activeregion);
            if(activeregion == null)
                throw new RuntimeException();
            gluhalfedge1 = RegionBelow(activeregion).eUp;
            FinishLeftRegions(glutessellatorimpl, RegionBelow(activeregion), activeregion1);
            flag = true;
        } else
        {
            flag = false;
        }
        gluhalfedge4 = gluhalfedge;
        if(Geom.VertEq(gluhalfedge3.Org, glutessellatorimpl.event))
        {
            if(!Mesh.__gl_meshSplice(gluhalfedge, gluhalfedge3.Sym.Lnext))
                throw new RuntimeException();
            gluhalfedge4 = FinishLeftRegions(glutessellatorimpl, activeregion1, null);
            flag = true;
        }
        if(flag)
        {
            AddRightEdges(glutessellatorimpl, activeregion, gluhalfedge4.Onext, gluhalfedge1, gluhalfedge1, true);
        } else
        {
            if(Geom.VertLeq(gluhalfedge3.Org, gluhalfedge2.Org))
                gluhalfedge = gluhalfedge3.Sym.Lnext;
            else
                gluhalfedge = gluhalfedge2;
            gluhalfedge = Mesh.__gl_meshConnect(gluhalfedge4.Onext.Sym, gluhalfedge);
            if(gluhalfedge == null)
                throw new RuntimeException();
            AddRightEdges(glutessellatorimpl, activeregion, gluhalfedge, gluhalfedge.Onext, gluhalfedge.Onext, false);
            gluhalfedge.Sym.activeRegion.fixUpperEdge = true;
            WalkDirtyRegions(glutessellatorimpl, activeregion);
        }
    }

    private static void DebugEvent(GLUtessellatorImpl glutessellatorimpl)
    {
    }

    static void DeleteRegion(GLUtessellatorImpl glutessellatorimpl, ActiveRegion activeregion)
    {
        if(activeregion.fixUpperEdge && !$assertionsDisabled && activeregion.eUp.winding != 0)
        {
            throw new AssertionError();
        } else
        {
            activeregion.eUp.activeRegion = null;
            Dict.dictDelete(glutessellatorimpl.dict, activeregion.nodeUp);
            return;
        }
    }

    static void DoneEdgeDict(GLUtessellatorImpl glutessellatorimpl)
    {
        int i = 0;
        do
        {
            ActiveRegion activeregion = (ActiveRegion)Dict.dictKey(Dict.dictMin(glutessellatorimpl.dict));
            if(activeregion != null)
            {
                int j = i;
                if(!activeregion.sentinel)
                {
                    if(!$assertionsDisabled && !activeregion.fixUpperEdge)
                        throw new AssertionError();
                    j = i;
                    if(!$assertionsDisabled)
                    {
                        j = ++i;
                        if(i != 1)
                            throw new AssertionError();
                    }
                }
                if(!$assertionsDisabled && activeregion.windingNumber != 0)
                    throw new AssertionError();
                DeleteRegion(glutessellatorimpl, activeregion);
                i = j;
            } else
            {
                Dict.dictDeleteDict(glutessellatorimpl.dict);
                return;
            }
        } while(true);
    }

    static void DonePriorityQ(GLUtessellatorImpl glutessellatorimpl)
    {
        glutessellatorimpl.pq.pqDeletePriorityQ();
    }

    static boolean EdgeLeq(GLUtessellatorImpl glutessellatorimpl, ActiveRegion activeregion, ActiveRegion activeregion1)
    {
        boolean flag;
        flag = true;
        glutessellatorimpl = glutessellatorimpl.event;
        activeregion = activeregion.eUp;
        activeregion1 = activeregion1.eUp;
        if(((GLUhalfEdge) (activeregion)).Sym.Org != glutessellatorimpl) goto _L2; else goto _L1
_L1:
        if(((GLUhalfEdge) (activeregion1)).Sym.Org != glutessellatorimpl) goto _L4; else goto _L3
_L3:
        if(!Geom.VertLeq(((GLUhalfEdge) (activeregion)).Org, ((GLUhalfEdge) (activeregion1)).Org)) goto _L6; else goto _L5
_L5:
        if(Geom.EdgeSign(((GLUhalfEdge) (activeregion1)).Sym.Org, ((GLUhalfEdge) (activeregion)).Org, ((GLUhalfEdge) (activeregion1)).Org) > 0.0D)
            flag = false;
_L8:
        return flag;
_L6:
        if(Geom.EdgeSign(((GLUhalfEdge) (activeregion)).Sym.Org, ((GLUhalfEdge) (activeregion1)).Org, ((GLUhalfEdge) (activeregion)).Org) < 0.0D)
            flag = false;
        continue; /* Loop/switch isn't completed */
_L4:
        if(Geom.EdgeSign(((GLUhalfEdge) (activeregion1)).Sym.Org, glutessellatorimpl, ((GLUhalfEdge) (activeregion1)).Org) > 0.0D)
            flag = false;
        continue; /* Loop/switch isn't completed */
_L2:
        if(((GLUhalfEdge) (activeregion1)).Sym.Org == glutessellatorimpl)
        {
            if(Geom.EdgeSign(((GLUhalfEdge) (activeregion)).Sym.Org, glutessellatorimpl, ((GLUhalfEdge) (activeregion)).Org) < 0.0D)
                flag = false;
        } else
        if(Geom.EdgeEval(((GLUhalfEdge) (activeregion)).Sym.Org, glutessellatorimpl, ((GLUhalfEdge) (activeregion)).Org) < Geom.EdgeEval(((GLUhalfEdge) (activeregion1)).Sym.Org, glutessellatorimpl, ((GLUhalfEdge) (activeregion1)).Org))
            flag = false;
        if(true) goto _L8; else goto _L7
_L7:
    }

    static GLUhalfEdge FinishLeftRegions(GLUtessellatorImpl glutessellatorimpl, ActiveRegion activeregion, ActiveRegion activeregion1)
    {
        GLUhalfEdge gluhalfedge = activeregion.eUp;
        do
        {
            ActiveRegion activeregion2;
            GLUhalfEdge gluhalfedge2;
label0:
            {
                GLUhalfEdge gluhalfedge1;
label1:
                {
                    if(activeregion != activeregion1)
                    {
                        activeregion.fixUpperEdge = false;
                        activeregion2 = RegionBelow(activeregion);
                        gluhalfedge1 = activeregion2.eUp;
                        gluhalfedge2 = gluhalfedge1;
                        if(gluhalfedge1.Org == gluhalfedge.Org)
                            break label0;
                        if(activeregion2.fixUpperEdge)
                            break label1;
                        FinishRegion(glutessellatorimpl, activeregion);
                    }
                    return gluhalfedge;
                }
                gluhalfedge1 = Mesh.__gl_meshConnect(gluhalfedge.Onext.Sym, gluhalfedge1.Sym);
                if(gluhalfedge1 == null)
                    throw new RuntimeException();
                gluhalfedge2 = gluhalfedge1;
                if(!FixUpperEdge(activeregion2, gluhalfedge1))
                    throw new RuntimeException();
            }
            if(gluhalfedge.Onext != gluhalfedge2)
            {
                if(!Mesh.__gl_meshSplice(gluhalfedge2.Sym.Lnext, gluhalfedge2))
                    throw new RuntimeException();
                if(!Mesh.__gl_meshSplice(gluhalfedge, gluhalfedge2))
                    throw new RuntimeException();
            }
            FinishRegion(glutessellatorimpl, activeregion);
            gluhalfedge = activeregion2.eUp;
            activeregion = activeregion2;
        } while(true);
    }

    static void FinishRegion(GLUtessellatorImpl glutessellatorimpl, ActiveRegion activeregion)
    {
        GLUhalfEdge gluhalfedge = activeregion.eUp;
        GLUface gluface = gluhalfedge.Lface;
        gluface.inside = activeregion.inside;
        gluface.anEdge = gluhalfedge;
        DeleteRegion(glutessellatorimpl, activeregion);
    }

    static boolean FixUpperEdge(ActiveRegion activeregion, GLUhalfEdge gluhalfedge)
    {
        boolean flag = false;
        if(!$assertionsDisabled && !activeregion.fixUpperEdge)
            throw new AssertionError();
        if(Mesh.__gl_meshDelete(activeregion.eUp))
        {
            activeregion.fixUpperEdge = false;
            activeregion.eUp = gluhalfedge;
            gluhalfedge.activeRegion = activeregion;
            flag = true;
        }
        return flag;
    }

    static void GetIntersectData(GLUtessellatorImpl glutessellatorimpl, GLUvertex gluvertex, GLUvertex gluvertex1, GLUvertex gluvertex2, GLUvertex gluvertex3, GLUvertex gluvertex4)
    {
        float af[] = new float[4];
        float af1[] = new float[2];
        float af2[] = new float[2];
        Object obj = gluvertex1.data;
        Object obj1 = gluvertex2.data;
        Object obj2 = gluvertex3.data;
        Object obj3 = gluvertex4.data;
        double ad[] = gluvertex.coords;
        double ad1[] = gluvertex.coords;
        gluvertex.coords[2] = 0.0D;
        ad1[1] = 0.0D;
        ad[0] = 0.0D;
        VertexWeights(gluvertex, gluvertex1, gluvertex2, af1);
        VertexWeights(gluvertex, gluvertex3, gluvertex4, af2);
        System.arraycopy(af1, 0, af, 0, 2);
        System.arraycopy(af2, 0, af, 2, 2);
        CallCombine(glutessellatorimpl, gluvertex, new Object[] {
            obj, obj1, obj2, obj3
        }, af, true);
    }

    static void InitEdgeDict(GLUtessellatorImpl glutessellatorimpl)
    {
        glutessellatorimpl.dict = Dict.dictNewDict(glutessellatorimpl, new Dict.DictLeq(glutessellatorimpl) {

            public boolean leq(Object obj, Object obj1, Object obj2)
            {
                return Sweep.EdgeLeq(tess, (ActiveRegion)obj1, (ActiveRegion)obj2);
            }

            final GLUtessellatorImpl val$tess;

            
            {
                tess = glutessellatorimpl;
                super();
            }
        }
);
        if(glutessellatorimpl.dict == null)
        {
            throw new RuntimeException();
        } else
        {
            AddSentinel(glutessellatorimpl, -3.9999999999999999E+150D);
            AddSentinel(glutessellatorimpl, 3.9999999999999999E+150D);
            return;
        }
    }

    static boolean InitPriorityQ(GLUtessellatorImpl glutessellatorimpl)
    {
        boolean flag;
        PriorityQ priorityq;
        flag = false;
        priorityq = PriorityQ.pqNewPriorityQ(new PriorityQ.Leq() {

            public boolean leq(Object obj, Object obj1)
            {
                return Geom.VertLeq((GLUvertex)obj, (GLUvertex)obj1);
            }

        }
);
        glutessellatorimpl.pq = priorityq;
        if(priorityq != null) goto _L2; else goto _L1
_L1:
        return flag;
_L2:
        GLUvertex gluvertex;
        GLUvertex gluvertex1;
        gluvertex = glutessellatorimpl.mesh.vHead;
        gluvertex1 = gluvertex.next;
_L4:
label0:
        {
            if(gluvertex1 != gluvertex)
            {
                gluvertex1.pqHandle = priorityq.pqInsert(gluvertex1);
                if((long)gluvertex1.pqHandle != 0x7fffffffffffffffL)
                    break label0;
            }
            if(gluvertex1 != gluvertex || !priorityq.pqInit())
            {
                glutessellatorimpl.pq.pqDeletePriorityQ();
                glutessellatorimpl.pq = null;
            } else
            {
                flag = true;
            }
        }
        if(true) goto _L1; else goto _L3
_L3:
        gluvertex1 = gluvertex1.next;
          goto _L4
    }

    static boolean IsWindingInside(GLUtessellatorImpl glutessellatorimpl, int i)
    {
        boolean flag;
        boolean flag1;
        flag = true;
        flag1 = false;
        glutessellatorimpl.windingRule;
        JVM INSTR tableswitch 100130 100134: default 44
    //                   100130 52
    //                   100131 67
    //                   100132 78
    //                   100133 89
    //                   100134 100;
           goto _L1 _L2 _L3 _L4 _L5 _L6
_L1:
        throw new InternalError();
_L2:
        if((i & 1) != 0)
            flag1 = flag;
        else
            flag1 = false;
_L8:
        return flag1;
_L3:
        flag1 = flag;
        if(i == 0)
            flag1 = false;
        continue; /* Loop/switch isn't completed */
_L4:
        flag1 = flag;
        if(i <= 0)
            flag1 = false;
        continue; /* Loop/switch isn't completed */
_L5:
        flag1 = flag;
        if(i >= 0)
            flag1 = false;
        continue; /* Loop/switch isn't completed */
_L6:
        if(i >= 2 || i <= -2)
            flag1 = true;
        if(true) goto _L8; else goto _L7
_L7:
    }

    private static ActiveRegion RegionAbove(ActiveRegion activeregion)
    {
        return (ActiveRegion)Dict.dictKey(Dict.dictSucc(activeregion.nodeUp));
    }

    private static ActiveRegion RegionBelow(ActiveRegion activeregion)
    {
        return (ActiveRegion)Dict.dictKey(Dict.dictPred(activeregion.nodeUp));
    }

    static void RemoveDegenerateEdges(GLUtessellatorImpl glutessellatorimpl)
    {
        GLUhalfEdge gluhalfedge = glutessellatorimpl.mesh.eHead;
        GLUhalfEdge gluhalfedge1 = gluhalfedge.next;
        do
        {
label0:
            {
label1:
                {
label2:
                    {
label3:
                        {
                            if(gluhalfedge1 == gluhalfedge)
                                break label1;
                            GLUhalfEdge gluhalfedge2 = gluhalfedge1.next;
                            GLUhalfEdge gluhalfedge3 = gluhalfedge1.Lnext;
                            GLUhalfEdge gluhalfedge4;
                            if(Geom.VertEq(gluhalfedge1.Org, gluhalfedge1.Sym.Org) && gluhalfedge1.Lnext.Lnext != gluhalfedge1)
                            {
                                SpliceMergeVertices(glutessellatorimpl, gluhalfedge3, gluhalfedge1);
                                if(!Mesh.__gl_meshDelete(gluhalfedge1))
                                    throw new RuntimeException();
                                gluhalfedge4 = gluhalfedge3.Lnext;
                            } else
                            {
                                gluhalfedge4 = gluhalfedge3;
                                gluhalfedge3 = gluhalfedge1;
                            }
                            if(gluhalfedge4.Lnext != gluhalfedge3)
                                break label0;
                            gluhalfedge1 = gluhalfedge2;
                            if(gluhalfedge4 == gluhalfedge3)
                                break label2;
                            if(gluhalfedge4 != gluhalfedge2)
                            {
                                gluhalfedge1 = gluhalfedge2;
                                if(gluhalfedge4 != gluhalfedge2.Sym)
                                    break label3;
                            }
                            gluhalfedge1 = gluhalfedge2.next;
                        }
                        if(!Mesh.__gl_meshDelete(gluhalfedge4))
                            throw new RuntimeException();
                    }
label4:
                    {
                        gluhalfedge2 = gluhalfedge1;
                        if(gluhalfedge3 != gluhalfedge2)
                        {
                            gluhalfedge1 = gluhalfedge2;
                            if(gluhalfedge3 != gluhalfedge2.Sym)
                                break label4;
                        }
                        gluhalfedge1 = gluhalfedge2.next;
                    }
                    if(!Mesh.__gl_meshDelete(gluhalfedge3))
                        throw new RuntimeException();
                    continue;
                }
                return;
            }
            gluhalfedge1 = gluhalfedge2;
        } while(true);
    }

    static boolean RemoveDegenerateFaces(GLUmesh glumesh)
    {
        Object obj = glumesh.fHead.next;
_L3:
        if(obj == glumesh.fHead) goto _L2; else goto _L1
_L1:
        GLUface gluface;
        boolean flag;
        gluface = ((GLUface) (obj)).next;
        obj = ((GLUface) (obj)).anEdge;
        if(!$assertionsDisabled && ((GLUhalfEdge) (obj)).Lnext == obj)
            throw new AssertionError();
        if(((GLUhalfEdge) (obj)).Lnext.Lnext != obj)
            continue; /* Loop/switch isn't completed */
        AddWinding(((GLUhalfEdge) (obj)).Onext, ((GLUhalfEdge) (obj)));
        if(Mesh.__gl_meshDelete(((GLUhalfEdge) (obj))))
            continue; /* Loop/switch isn't completed */
        flag = false;
_L4:
        return flag;
        obj = gluface;
          goto _L3
_L2:
        flag = true;
          goto _L4
    }

    static void SpliceMergeVertices(GLUtessellatorImpl glutessellatorimpl, GLUhalfEdge gluhalfedge, GLUhalfEdge gluhalfedge1)
    {
        Object aobj[] = new Object[4];
        aobj[0] = gluhalfedge.Org.data;
        aobj[1] = gluhalfedge1.Org.data;
        CallCombine(glutessellatorimpl, gluhalfedge.Org, aobj, new float[] {
            0.5F, 0.5F, 0.0F, 0.0F
        }, false);
        if(!Mesh.__gl_meshSplice(gluhalfedge, gluhalfedge1))
            throw new RuntimeException();
        else
            return;
    }

    static void SweepEvent(GLUtessellatorImpl glutessellatorimpl, GLUvertex gluvertex)
    {
        Object obj;
        glutessellatorimpl.event = gluvertex;
        DebugEvent(glutessellatorimpl);
        obj = gluvertex.anEdge;
_L4:
        if(((GLUhalfEdge) (obj)).activeRegion != null) goto _L2; else goto _L1
_L1:
        GLUhalfEdge gluhalfedge;
        gluhalfedge = ((GLUhalfEdge) (obj)).Onext;
        obj = gluhalfedge;
        if(gluhalfedge != gluvertex.anEdge) goto _L4; else goto _L3
_L3:
        ConnectLeftVertex(glutessellatorimpl, gluvertex);
_L6:
        return;
_L2:
        obj = TopLeftRegion(((GLUhalfEdge) (obj)).activeRegion);
        if(obj == null)
            throw new RuntimeException();
        Object obj1 = RegionBelow(((ActiveRegion) (obj)));
        gluvertex = ((ActiveRegion) (obj1)).eUp;
        obj1 = FinishLeftRegions(glutessellatorimpl, ((ActiveRegion) (obj1)), null);
        if(((GLUhalfEdge) (obj1)).Onext == gluvertex)
            ConnectRightVertex(glutessellatorimpl, ((ActiveRegion) (obj)), ((GLUhalfEdge) (obj1)));
        else
            AddRightEdges(glutessellatorimpl, ((ActiveRegion) (obj)), ((GLUhalfEdge) (obj1)).Onext, gluvertex, gluvertex, true);
        if(true) goto _L6; else goto _L5
_L5:
    }

    static ActiveRegion TopLeftRegion(ActiveRegion activeregion)
    {
        GLUvertex gluvertex = activeregion.eUp.Org;
        ActiveRegion activeregion1;
        do
        {
            activeregion1 = RegionAbove(activeregion);
            activeregion = activeregion1;
        } while(activeregion1.eUp.Org == gluvertex);
        activeregion = activeregion1;
        if(activeregion1.fixUpperEdge)
        {
            activeregion = Mesh.__gl_meshConnect(RegionBelow(activeregion1).eUp.Sym, activeregion1.eUp.Lnext);
            if(activeregion == null)
                activeregion = null;
            else
            if(!FixUpperEdge(activeregion1, activeregion))
                activeregion = null;
            else
                activeregion = RegionAbove(activeregion1);
        }
        return activeregion;
    }

    static ActiveRegion TopRightRegion(ActiveRegion activeregion)
    {
        GLUvertex gluvertex = activeregion.eUp.Sym.Org;
        ActiveRegion activeregion1;
        do
        {
            activeregion1 = RegionAbove(activeregion);
            activeregion = activeregion1;
        } while(activeregion1.eUp.Sym.Org == gluvertex);
        return activeregion1;
    }

    static void VertexWeights(GLUvertex gluvertex, GLUvertex gluvertex1, GLUvertex gluvertex2, float af[])
    {
        double d = Geom.VertL1dist(gluvertex1, gluvertex);
        double d1 = Geom.VertL1dist(gluvertex2, gluvertex);
        af[0] = (float)((0.5D * d1) / (d + d1));
        af[1] = (float)((0.5D * d) / (d + d1));
        double ad[] = gluvertex.coords;
        ad[0] = ad[0] + ((double)af[0] * gluvertex1.coords[0] + (double)af[1] * gluvertex2.coords[0]);
        ad = gluvertex.coords;
        ad[1] = ad[1] + ((double)af[0] * gluvertex1.coords[1] + (double)af[1] * gluvertex2.coords[1]);
        gluvertex = gluvertex.coords;
        gluvertex[2] = gluvertex[2] + ((double)af[0] * gluvertex1.coords[2] + (double)af[1] * gluvertex2.coords[2]);
    }

    static void WalkDirtyRegions(GLUtessellatorImpl glutessellatorimpl, ActiveRegion activeregion)
    {
_L7:
        ActiveRegion activeregion1;
        ActiveRegion activeregion2;
        for(activeregion1 = RegionBelow(activeregion); activeregion1.dirty; activeregion1 = activeregion2)
        {
            activeregion2 = RegionBelow(activeregion1);
            activeregion = activeregion1;
        }

        if(activeregion.dirty) goto _L2; else goto _L1
_L1:
        ActiveRegion activeregion3 = RegionAbove(activeregion);
        if(activeregion3 == null) goto _L4; else goto _L3
_L3:
        activeregion1 = activeregion3;
        if(activeregion3.dirty) goto _L5; else goto _L4
_L4:
        return;
_L2:
        activeregion3 = activeregion;
        activeregion = activeregion1;
        activeregion1 = activeregion3;
_L5:
        ActiveRegion activeregion4;
        activeregion1.dirty = false;
        GLUhalfEdge gluhalfedge = activeregion1.eUp;
        GLUhalfEdge gluhalfedge1 = activeregion.eUp;
        if(gluhalfedge.Sym.Org == gluhalfedge1.Sym.Org || !CheckForLeftSplice(glutessellatorimpl, activeregion1))
            break MISSING_BLOCK_LABEL_365;
        if(activeregion.fixUpperEdge)
        {
            DeleteRegion(glutessellatorimpl, activeregion);
            if(!Mesh.__gl_meshDelete(gluhalfedge1))
                throw new RuntimeException();
            activeregion3 = RegionBelow(activeregion1);
            gluhalfedge1 = activeregion3.eUp;
            activeregion4 = activeregion1;
        } else
        {
            if(!activeregion1.fixUpperEdge)
                break MISSING_BLOCK_LABEL_365;
            DeleteRegion(glutessellatorimpl, activeregion1);
            if(!Mesh.__gl_meshDelete(gluhalfedge))
                throw new RuntimeException();
            activeregion4 = RegionAbove(activeregion);
            gluhalfedge = activeregion4.eUp;
            activeregion3 = activeregion;
        }
_L8:
        if(gluhalfedge.Org == gluhalfedge1.Org)
            break; /* Loop/switch isn't completed */
        if(gluhalfedge.Sym.Org == gluhalfedge1.Sym.Org || activeregion4.fixUpperEdge || activeregion3.fixUpperEdge || gluhalfedge.Sym.Org != glutessellatorimpl.event && gluhalfedge1.Sym.Org != glutessellatorimpl.event)
            break MISSING_BLOCK_LABEL_345;
        if(CheckForIntersect(glutessellatorimpl, activeregion4)) goto _L4; else goto _L6
_L6:
        activeregion1 = activeregion3;
        activeregion = activeregion4;
        if(gluhalfedge.Org == gluhalfedge1.Org)
        {
            activeregion1 = activeregion3;
            activeregion = activeregion4;
            if(gluhalfedge.Sym.Org == gluhalfedge1.Sym.Org)
            {
                AddWinding(gluhalfedge1, gluhalfedge);
                DeleteRegion(glutessellatorimpl, activeregion4);
                if(!Mesh.__gl_meshDelete(gluhalfedge))
                    throw new RuntimeException();
                break MISSING_BLOCK_LABEL_355;
            }
        }
          goto _L7
        CheckForRightSplice(glutessellatorimpl, activeregion4);
          goto _L6
        activeregion = RegionAbove(activeregion3);
        activeregion1 = activeregion3;
          goto _L7
        activeregion3 = activeregion;
        activeregion4 = activeregion1;
          goto _L8
    }

    public static boolean __gl_computeInterior(GLUtessellatorImpl glutessellatorimpl)
    {
        glutessellatorimpl.fatalError = false;
        RemoveDegenerateEdges(glutessellatorimpl);
        if(InitPriorityQ(glutessellatorimpl)) goto _L2; else goto _L1
_L1:
        boolean flag = false;
_L4:
        return flag;
_L2:
        InitEdgeDict(glutessellatorimpl);
        do
        {
label0:
            {
                GLUvertex gluvertex = (GLUvertex)glutessellatorimpl.pq.pqExtractMin();
                if(gluvertex == null)
                    break label0;
                do
                {
label1:
                    {
                        GLUvertex gluvertex1 = (GLUvertex)glutessellatorimpl.pq.pqMinimum();
                        if(gluvertex1 != null && Geom.VertEq(gluvertex1, gluvertex))
                            break label1;
                        SweepEvent(glutessellatorimpl, gluvertex);
                    }
                    if(true)
                        break;
                    GLUvertex gluvertex2 = (GLUvertex)glutessellatorimpl.pq.pqExtractMin();
                    SpliceMergeVertices(glutessellatorimpl, gluvertex.anEdge, gluvertex2.anEdge);
                } while(true);
            }
        } while(true);
        glutessellatorimpl.event = ((ActiveRegion)Dict.dictKey(Dict.dictMin(glutessellatorimpl.dict))).eUp.Org;
        DebugEvent(glutessellatorimpl);
        DoneEdgeDict(glutessellatorimpl);
        DonePriorityQ(glutessellatorimpl);
        if(!RemoveDegenerateFaces(glutessellatorimpl.mesh))
        {
            flag = false;
        } else
        {
            Mesh.__gl_meshCheckMesh(glutessellatorimpl.mesh);
            flag = true;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    static final boolean $assertionsDisabled;
    private static final double SENTINEL_COORD = 3.9999999999999999E+150D;
    private static final boolean TOLERANCE_NONZERO = false;

    static 
    {
        boolean flag;
        if(!processing/opengl/tess/Sweep.desiredAssertionStatus())
            flag = true;
        else
            flag = false;
        $assertionsDisabled = flag;
    }
}
