// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.opengl.tess;


// Referenced classes of package processing.opengl.tess:
//            GLUhalfEdge, GLUface, GLUvertex, GLUmesh

class Mesh
{

    private Mesh()
    {
    }

    static void KillEdge(GLUhalfEdge gluhalfedge)
    {
        GLUhalfEdge gluhalfedge1 = gluhalfedge;
        if(!gluhalfedge.first)
            gluhalfedge1 = gluhalfedge.Sym;
        gluhalfedge = gluhalfedge1.next;
        gluhalfedge1 = gluhalfedge1.Sym.next;
        gluhalfedge.Sym.next = gluhalfedge1;
        gluhalfedge1.Sym.next = gluhalfedge;
    }

    static void KillFace(GLUface gluface, GLUface gluface1)
    {
        GLUhalfEdge gluhalfedge = gluface.anEdge;
        GLUhalfEdge gluhalfedge1 = gluhalfedge;
        GLUhalfEdge gluhalfedge2;
        do
        {
            gluhalfedge1.Lface = gluface1;
            gluhalfedge2 = gluhalfedge1.Lnext;
            gluhalfedge1 = gluhalfedge2;
        } while(gluhalfedge2 != gluhalfedge);
        gluface1 = gluface.prev;
        gluface = gluface.next;
        gluface.prev = gluface1;
        gluface1.next = gluface;
    }

    static void KillVertex(GLUvertex gluvertex, GLUvertex gluvertex1)
    {
        GLUhalfEdge gluhalfedge = gluvertex.anEdge;
        GLUhalfEdge gluhalfedge1 = gluhalfedge;
        GLUhalfEdge gluhalfedge2;
        do
        {
            gluhalfedge1.Org = gluvertex1;
            gluhalfedge2 = gluhalfedge1.Onext;
            gluhalfedge1 = gluhalfedge2;
        } while(gluhalfedge2 != gluhalfedge);
        gluvertex1 = gluvertex.prev;
        gluvertex = gluvertex.next;
        gluvertex.prev = gluvertex1;
        gluvertex1.next = gluvertex;
    }

    static GLUhalfEdge MakeEdge(GLUhalfEdge gluhalfedge)
    {
        GLUhalfEdge gluhalfedge1 = new GLUhalfEdge(true);
        GLUhalfEdge gluhalfedge2 = new GLUhalfEdge(false);
        GLUhalfEdge gluhalfedge3 = gluhalfedge;
        if(!gluhalfedge.first)
            gluhalfedge3 = gluhalfedge.Sym;
        gluhalfedge = gluhalfedge3.Sym.next;
        gluhalfedge2.next = gluhalfedge;
        gluhalfedge.Sym.next = gluhalfedge1;
        gluhalfedge1.next = gluhalfedge3;
        gluhalfedge3.Sym.next = gluhalfedge2;
        gluhalfedge1.Sym = gluhalfedge2;
        gluhalfedge1.Onext = gluhalfedge1;
        gluhalfedge1.Lnext = gluhalfedge2;
        gluhalfedge1.Org = null;
        gluhalfedge1.Lface = null;
        gluhalfedge1.winding = 0;
        gluhalfedge1.activeRegion = null;
        gluhalfedge2.Sym = gluhalfedge1;
        gluhalfedge2.Onext = gluhalfedge2;
        gluhalfedge2.Lnext = gluhalfedge1;
        gluhalfedge2.Org = null;
        gluhalfedge2.Lface = null;
        gluhalfedge2.winding = 0;
        gluhalfedge2.activeRegion = null;
        return gluhalfedge1;
    }

    static void MakeFace(GLUface gluface, GLUhalfEdge gluhalfedge, GLUface gluface1)
    {
        if(!$assertionsDisabled && gluface == null)
            throw new AssertionError();
        Object obj = gluface1.prev;
        gluface.prev = ((GLUface) (obj));
        obj.next = gluface;
        gluface.next = gluface1;
        gluface1.prev = gluface;
        gluface.anEdge = gluhalfedge;
        gluface.data = null;
        gluface.trail = null;
        gluface.marked = false;
        gluface.inside = gluface1.inside;
        gluface1 = gluhalfedge;
        do
        {
            gluface1.Lface = gluface;
            obj = ((GLUhalfEdge) (gluface1)).Lnext;
            gluface1 = ((GLUface) (obj));
        } while(obj != gluhalfedge);
    }

    static void MakeVertex(GLUvertex gluvertex, GLUhalfEdge gluhalfedge, GLUvertex gluvertex1)
    {
        if(!$assertionsDisabled && gluvertex == null)
            throw new AssertionError();
        Object obj = gluvertex1.prev;
        gluvertex.prev = ((GLUvertex) (obj));
        obj.next = gluvertex;
        gluvertex.next = gluvertex1;
        gluvertex1.prev = gluvertex;
        gluvertex.anEdge = gluhalfedge;
        gluvertex.data = null;
        gluvertex1 = gluhalfedge;
        do
        {
            gluvertex1.Org = gluvertex;
            obj = ((GLUhalfEdge) (gluvertex1)).Onext;
            gluvertex1 = ((GLUvertex) (obj));
        } while(obj != gluhalfedge);
    }

    static void Splice(GLUhalfEdge gluhalfedge, GLUhalfEdge gluhalfedge1)
    {
        GLUhalfEdge gluhalfedge2 = gluhalfedge.Onext;
        GLUhalfEdge gluhalfedge3 = gluhalfedge1.Onext;
        gluhalfedge2.Sym.Lnext = gluhalfedge1;
        gluhalfedge3.Sym.Lnext = gluhalfedge;
        gluhalfedge.Onext = gluhalfedge3;
        gluhalfedge1.Onext = gluhalfedge2;
    }

    static GLUhalfEdge __gl_meshAddEdgeVertex(GLUhalfEdge gluhalfedge)
    {
        GLUhalfEdge gluhalfedge1 = MakeEdge(gluhalfedge);
        GLUhalfEdge gluhalfedge2 = gluhalfedge1.Sym;
        Splice(gluhalfedge1, gluhalfedge.Lnext);
        gluhalfedge1.Org = gluhalfedge.Sym.Org;
        MakeVertex(new GLUvertex(), gluhalfedge2, gluhalfedge1.Org);
        gluhalfedge = gluhalfedge.Lface;
        gluhalfedge2.Lface = gluhalfedge;
        gluhalfedge1.Lface = gluhalfedge;
        return gluhalfedge1;
    }

    public static void __gl_meshCheckMesh(GLUmesh glumesh)
    {
        Object obj = glumesh.fHead;
        Object obj1 = glumesh.vHead;
        GLUhalfEdge gluhalfedge = glumesh.eHead;
        Object obj2 = obj;
        GLUface gluface;
        do
        {
            gluface = ((GLUface) (obj2)).next;
            if(gluface == obj)
                break;
            if(!$assertionsDisabled && gluface.prev != obj2)
                throw new AssertionError();
            obj2 = gluface.anEdge;
            GLUhalfEdge gluhalfedge2;
            do
            {
                if(!$assertionsDisabled && ((GLUhalfEdge) (obj2)).Sym == obj2)
                    throw new AssertionError();
                if(!$assertionsDisabled && ((GLUhalfEdge) (obj2)).Sym.Sym != obj2)
                    throw new AssertionError();
                if(!$assertionsDisabled && ((GLUhalfEdge) (obj2)).Lnext.Onext.Sym != obj2)
                    throw new AssertionError();
                if(!$assertionsDisabled && ((GLUhalfEdge) (obj2)).Onext.Sym.Lnext != obj2)
                    throw new AssertionError();
                if(!$assertionsDisabled && ((GLUhalfEdge) (obj2)).Lface != gluface)
                    throw new AssertionError();
                gluhalfedge2 = ((GLUhalfEdge) (obj2)).Lnext;
                obj2 = gluhalfedge2;
            } while(gluhalfedge2 != gluface.anEdge);
            obj2 = gluface;
        } while(true);
        if(!$assertionsDisabled && (gluface.prev != obj2 || gluface.anEdge != null || gluface.data != null))
            throw new AssertionError();
        obj2 = obj1;
        do
        {
            obj = ((GLUvertex) (obj2)).next;
            if(obj == obj1)
                break;
            if(!$assertionsDisabled && ((GLUvertex) (obj)).prev != obj2)
                throw new AssertionError();
            obj2 = ((GLUvertex) (obj)).anEdge;
            GLUhalfEdge gluhalfedge1;
            do
            {
                if(!$assertionsDisabled && ((GLUhalfEdge) (obj2)).Sym == obj2)
                    throw new AssertionError();
                if(!$assertionsDisabled && ((GLUhalfEdge) (obj2)).Sym.Sym != obj2)
                    throw new AssertionError();
                if(!$assertionsDisabled && ((GLUhalfEdge) (obj2)).Lnext.Onext.Sym != obj2)
                    throw new AssertionError();
                if(!$assertionsDisabled && ((GLUhalfEdge) (obj2)).Onext.Sym.Lnext != obj2)
                    throw new AssertionError();
                if(!$assertionsDisabled && ((GLUhalfEdge) (obj2)).Org != obj)
                    throw new AssertionError();
                gluhalfedge1 = ((GLUhalfEdge) (obj2)).Onext;
                obj2 = gluhalfedge1;
            } while(gluhalfedge1 != ((GLUvertex) (obj)).anEdge);
            obj2 = obj;
        } while(true);
        if(!$assertionsDisabled && (((GLUvertex) (obj)).prev != obj2 || ((GLUvertex) (obj)).anEdge != null || ((GLUvertex) (obj)).data != null))
            throw new AssertionError();
        obj2 = gluhalfedge;
        do
        {
            obj1 = ((GLUhalfEdge) (obj2)).next;
            if(obj1 == gluhalfedge)
                break;
            if(!$assertionsDisabled && ((GLUhalfEdge) (obj1)).Sym.next != ((GLUhalfEdge) (obj2)).Sym)
                throw new AssertionError();
            if(!$assertionsDisabled && ((GLUhalfEdge) (obj1)).Sym == obj1)
                throw new AssertionError();
            if(!$assertionsDisabled && ((GLUhalfEdge) (obj1)).Sym.Sym != obj1)
                throw new AssertionError();
            if(!$assertionsDisabled && ((GLUhalfEdge) (obj1)).Org == null)
                throw new AssertionError();
            if(!$assertionsDisabled && ((GLUhalfEdge) (obj1)).Sym.Org == null)
                throw new AssertionError();
            if(!$assertionsDisabled && ((GLUhalfEdge) (obj1)).Lnext.Onext.Sym != obj1)
                throw new AssertionError();
            if(!$assertionsDisabled && ((GLUhalfEdge) (obj1)).Onext.Sym.Lnext != obj1)
                throw new AssertionError();
            obj2 = obj1;
        } while(true);
        if(!$assertionsDisabled && (((GLUhalfEdge) (obj1)).Sym.next != ((GLUhalfEdge) (obj2)).Sym || ((GLUhalfEdge) (obj1)).Sym != glumesh.eHeadSym || ((GLUhalfEdge) (obj1)).Sym.Sym != obj1 || ((GLUhalfEdge) (obj1)).Org != null || ((GLUhalfEdge) (obj1)).Sym.Org != null || ((GLUhalfEdge) (obj1)).Lface != null || ((GLUhalfEdge) (obj1)).Sym.Lface != null))
            throw new AssertionError();
        else
            return;
    }

    static GLUhalfEdge __gl_meshConnect(GLUhalfEdge gluhalfedge, GLUhalfEdge gluhalfedge1)
    {
        boolean flag = false;
        GLUhalfEdge gluhalfedge2 = MakeEdge(gluhalfedge);
        GLUhalfEdge gluhalfedge3 = gluhalfedge2.Sym;
        if(gluhalfedge1.Lface != gluhalfedge.Lface)
        {
            flag = true;
            KillFace(gluhalfedge1.Lface, gluhalfedge.Lface);
        }
        Splice(gluhalfedge2, gluhalfedge.Lnext);
        Splice(gluhalfedge3, gluhalfedge1);
        gluhalfedge2.Org = gluhalfedge.Sym.Org;
        gluhalfedge3.Org = gluhalfedge1.Org;
        gluhalfedge1 = gluhalfedge.Lface;
        gluhalfedge3.Lface = gluhalfedge1;
        gluhalfedge2.Lface = gluhalfedge1;
        gluhalfedge.Lface.anEdge = gluhalfedge3;
        if(!flag)
            MakeFace(new GLUface(), gluhalfedge2, gluhalfedge.Lface);
        return gluhalfedge2;
    }

    static boolean __gl_meshDelete(GLUhalfEdge gluhalfedge)
    {
        GLUhalfEdge gluhalfedge1 = gluhalfedge.Sym;
        boolean flag = false;
        if(gluhalfedge.Lface != gluhalfedge.Sym.Lface)
        {
            KillFace(gluhalfedge.Lface, gluhalfedge.Sym.Lface);
            flag = true;
        }
        if(gluhalfedge.Onext == gluhalfedge)
        {
            KillVertex(gluhalfedge.Org, null);
        } else
        {
            gluhalfedge.Sym.Lface.anEdge = gluhalfedge.Sym.Lnext;
            gluhalfedge.Org.anEdge = gluhalfedge.Onext;
            Splice(gluhalfedge, gluhalfedge.Sym.Lnext);
            if(!flag)
                MakeFace(new GLUface(), gluhalfedge, gluhalfedge.Lface);
        }
        if(gluhalfedge1.Onext == gluhalfedge1)
        {
            KillVertex(gluhalfedge1.Org, null);
            KillFace(gluhalfedge1.Lface, null);
        } else
        {
            gluhalfedge.Lface.anEdge = gluhalfedge1.Sym.Lnext;
            gluhalfedge1.Org.anEdge = gluhalfedge1.Onext;
            Splice(gluhalfedge1, gluhalfedge1.Sym.Lnext);
        }
        KillEdge(gluhalfedge);
        return true;
    }

    public static void __gl_meshDeleteMesh(GLUmesh glumesh)
    {
        for(GLUface gluface = glumesh.fHead.next; gluface != glumesh.fHead; gluface = gluface.next);
        for(GLUvertex gluvertex = glumesh.vHead.next; gluvertex != glumesh.vHead; gluvertex = gluvertex.next);
        for(GLUhalfEdge gluhalfedge = glumesh.eHead.next; gluhalfedge != glumesh.eHead; gluhalfedge = gluhalfedge.next);
    }

    static void __gl_meshDeleteMeshZap(GLUmesh glumesh)
    {
        for(GLUface gluface = glumesh.fHead; gluface.next != gluface;)
            __gl_meshZapFace(gluface.next);

        if(!$assertionsDisabled && glumesh.vHead.next != glumesh.vHead)
            throw new AssertionError();
        else
            return;
    }

    public static GLUhalfEdge __gl_meshMakeEdge(GLUmesh glumesh)
    {
        GLUvertex gluvertex = new GLUvertex();
        GLUvertex gluvertex1 = new GLUvertex();
        GLUface gluface = new GLUface();
        GLUhalfEdge gluhalfedge = MakeEdge(glumesh.eHead);
        if(gluhalfedge == null)
        {
            glumesh = null;
        } else
        {
            MakeVertex(gluvertex, gluhalfedge, glumesh.vHead);
            MakeVertex(gluvertex1, gluhalfedge.Sym, glumesh.vHead);
            MakeFace(gluface, gluhalfedge, glumesh.fHead);
            glumesh = gluhalfedge;
        }
        return glumesh;
    }

    public static GLUmesh __gl_meshNewMesh()
    {
        GLUmesh glumesh = new GLUmesh();
        GLUvertex gluvertex = glumesh.vHead;
        GLUface gluface = glumesh.fHead;
        GLUhalfEdge gluhalfedge = glumesh.eHead;
        GLUhalfEdge gluhalfedge1 = glumesh.eHeadSym;
        gluvertex.prev = gluvertex;
        gluvertex.next = gluvertex;
        gluvertex.anEdge = null;
        gluvertex.data = null;
        gluface.prev = gluface;
        gluface.next = gluface;
        gluface.anEdge = null;
        gluface.data = null;
        gluface.trail = null;
        gluface.marked = false;
        gluface.inside = false;
        gluhalfedge.next = gluhalfedge;
        gluhalfedge.Sym = gluhalfedge1;
        gluhalfedge.Onext = null;
        gluhalfedge.Lnext = null;
        gluhalfedge.Org = null;
        gluhalfedge.Lface = null;
        gluhalfedge.winding = 0;
        gluhalfedge.activeRegion = null;
        gluhalfedge1.next = gluhalfedge1;
        gluhalfedge1.Sym = gluhalfedge;
        gluhalfedge1.Onext = null;
        gluhalfedge1.Lnext = null;
        gluhalfedge1.Org = null;
        gluhalfedge1.Lface = null;
        gluhalfedge1.winding = 0;
        gluhalfedge1.activeRegion = null;
        return glumesh;
    }

    public static boolean __gl_meshSplice(GLUhalfEdge gluhalfedge, GLUhalfEdge gluhalfedge1)
    {
        boolean flag = false;
        if(gluhalfedge != gluhalfedge1) goto _L2; else goto _L1
_L1:
        return true;
_L2:
        boolean flag1;
        if(gluhalfedge1.Org != gluhalfedge.Org)
        {
            KillVertex(gluhalfedge1.Org, gluhalfedge.Org);
            flag1 = true;
        } else
        {
            flag1 = false;
        }
        if(gluhalfedge1.Lface != gluhalfedge.Lface)
        {
            KillFace(gluhalfedge1.Lface, gluhalfedge.Lface);
            flag = true;
        }
        Splice(gluhalfedge1, gluhalfedge);
        if(!flag1)
        {
            MakeVertex(new GLUvertex(), gluhalfedge1, gluhalfedge.Org);
            gluhalfedge.Org.anEdge = gluhalfedge;
        }
        if(!flag)
        {
            MakeFace(new GLUface(), gluhalfedge1, gluhalfedge.Lface);
            gluhalfedge.Lface.anEdge = gluhalfedge;
        }
        if(true) goto _L1; else goto _L3
_L3:
    }

    public static GLUhalfEdge __gl_meshSplitEdge(GLUhalfEdge gluhalfedge)
    {
        GLUhalfEdge gluhalfedge1 = __gl_meshAddEdgeVertex(gluhalfedge).Sym;
        Splice(gluhalfedge.Sym, gluhalfedge.Sym.Sym.Lnext);
        Splice(gluhalfedge.Sym, gluhalfedge1);
        gluhalfedge.Sym.Org = gluhalfedge1.Org;
        gluhalfedge1.Sym.Org.anEdge = gluhalfedge1.Sym;
        gluhalfedge1.Sym.Lface = gluhalfedge.Sym.Lface;
        gluhalfedge1.winding = gluhalfedge.winding;
        gluhalfedge1.Sym.winding = gluhalfedge.Sym.winding;
        return gluhalfedge1;
    }

    static GLUmesh __gl_meshUnion(GLUmesh glumesh, GLUmesh glumesh1)
    {
        GLUface gluface = glumesh.fHead;
        GLUvertex gluvertex = glumesh.vHead;
        GLUhalfEdge gluhalfedge = glumesh.eHead;
        GLUface gluface1 = glumesh1.fHead;
        GLUvertex gluvertex1 = glumesh1.vHead;
        glumesh1 = glumesh1.eHead;
        if(gluface1.next != gluface1)
        {
            gluface.prev.next = gluface1.next;
            gluface1.next.prev = gluface.prev;
            gluface1.prev.next = gluface;
            gluface.prev = gluface1.prev;
        }
        if(gluvertex1.next != gluvertex1)
        {
            gluvertex.prev.next = gluvertex1.next;
            gluvertex1.next.prev = gluvertex.prev;
            gluvertex1.prev.next = gluvertex;
            gluvertex.prev = gluvertex1.prev;
        }
        if(((GLUhalfEdge) (glumesh1)).next != glumesh1)
        {
            gluhalfedge.Sym.next.Sym.next = ((GLUhalfEdge) (glumesh1)).next;
            ((GLUhalfEdge) (glumesh1)).next.Sym.next = gluhalfedge.Sym.next;
            ((GLUhalfEdge) (glumesh1)).Sym.next.Sym.next = gluhalfedge;
            gluhalfedge.Sym.next = ((GLUhalfEdge) (glumesh1)).Sym.next;
        }
        return glumesh;
    }

    static void __gl_meshZapFace(GLUface gluface)
    {
        GLUhalfEdge gluhalfedge = gluface.anEdge;
        Object obj = gluhalfedge.Lnext;
        do
        {
            GLUhalfEdge gluhalfedge1 = ((GLUhalfEdge) (obj)).Lnext;
            obj.Lface = null;
            if(((GLUhalfEdge) (obj)).Sym.Lface == null)
            {
                GLUhalfEdge gluhalfedge2;
                if(((GLUhalfEdge) (obj)).Onext == obj)
                {
                    KillVertex(((GLUhalfEdge) (obj)).Org, null);
                } else
                {
                    ((GLUhalfEdge) (obj)).Org.anEdge = ((GLUhalfEdge) (obj)).Onext;
                    Splice(((GLUhalfEdge) (obj)), ((GLUhalfEdge) (obj)).Sym.Lnext);
                }
                gluhalfedge2 = ((GLUhalfEdge) (obj)).Sym;
                if(gluhalfedge2.Onext == gluhalfedge2)
                {
                    KillVertex(gluhalfedge2.Org, null);
                } else
                {
                    gluhalfedge2.Org.anEdge = gluhalfedge2.Onext;
                    Splice(gluhalfedge2, gluhalfedge2.Sym.Lnext);
                }
                KillEdge(((GLUhalfEdge) (obj)));
            }
            if(obj == gluhalfedge)
            {
                obj = gluface.prev;
                gluface = gluface.next;
                gluface.prev = ((GLUface) (obj));
                obj.next = gluface;
                return;
            }
            obj = gluhalfedge1;
        } while(true);
    }

    static final boolean $assertionsDisabled;

    static 
    {
        boolean flag;
        if(!processing/opengl/tess/Mesh.desiredAssertionStatus())
            flag = true;
        else
            flag = false;
        $assertionsDisabled = flag;
    }
}
