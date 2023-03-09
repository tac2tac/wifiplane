// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.opengl.tess;


// Referenced classes of package processing.opengl.tess:
//            GLUmesh, GLUface, Mesh, GLUhalfEdge, 
//            Geom

class TessMono
{

    TessMono()
    {
    }

    public static void __gl_meshDiscardExterior(GLUmesh glumesh)
    {
        GLUface gluface1;
        for(GLUface gluface = glumesh.fHead.next; gluface != glumesh.fHead; gluface = gluface1)
        {
            gluface1 = gluface.next;
            if(!gluface.inside)
                Mesh.__gl_meshZapFace(gluface);
        }

    }

    public static boolean __gl_meshSetWindingNumber(GLUmesh glumesh, int i, boolean flag)
    {
        GLUhalfEdge gluhalfedge = glumesh.eHead.next;
_L3:
        if(gluhalfedge == glumesh.eHead) goto _L2; else goto _L1
_L1:
        GLUhalfEdge gluhalfedge1 = gluhalfedge.next;
        if(gluhalfedge.Sym.Lface.inside != gluhalfedge.Lface.inside)
        {
            int j;
            if(gluhalfedge.Lface.inside)
                j = i;
            else
                j = -i;
            gluhalfedge.winding = j;
        } else
        {
            if(flag)
                continue; /* Loop/switch isn't completed */
            gluhalfedge.winding = 0;
        }
_L5:
        gluhalfedge = gluhalfedge1;
          goto _L3
        if(Mesh.__gl_meshDelete(gluhalfedge)) goto _L5; else goto _L4
_L4:
        flag = false;
_L7:
        return flag;
_L2:
        flag = true;
        if(true) goto _L7; else goto _L6
_L6:
    }

    public static boolean __gl_meshTessellateInterior(GLUmesh glumesh, boolean flag)
    {
        GLUface gluface = glumesh.fHead.next;
_L3:
        GLUface gluface1;
        if(gluface == glumesh.fHead)
            break MISSING_BLOCK_LABEL_45;
        gluface1 = gluface.next;
        if(!gluface.inside || __gl_meshTessellateMonoRegion(gluface, flag)) goto _L2; else goto _L1
_L1:
        flag = false;
_L4:
        return flag;
_L2:
        gluface = gluface1;
          goto _L3
        flag = true;
          goto _L4
    }

    static boolean __gl_meshTessellateMonoRegion(GLUface gluface, boolean flag)
    {
        boolean flag1;
        Object obj;
        boolean flag2;
label0:
        {
            flag1 = false;
            GLUhalfEdge gluhalfedge = gluface.anEdge;
            gluface = gluhalfedge;
            if($assertionsDisabled)
                break label0;
            if(gluhalfedge.Lnext != gluhalfedge)
            {
                gluface = gluhalfedge;
                if(gluhalfedge.Lnext.Lnext != gluhalfedge)
                    break label0;
            }
            throw new AssertionError();
        }
        do
        {
            obj = gluface;
            if(!Geom.VertLeq(((GLUhalfEdge) (gluface)).Sym.Org, ((GLUhalfEdge) (gluface)).Org))
                break;
            gluface = ((GLUhalfEdge) (gluface)).Onext.Sym;
        } while(true);
        for(; Geom.VertLeq(((GLUhalfEdge) (obj)).Org, ((GLUhalfEdge) (obj)).Sym.Org); obj = ((GLUhalfEdge) (obj)).Lnext);
        GLUhalfEdge gluhalfedge1 = ((GLUhalfEdge) (obj)).Onext.Sym;
        gluface = ((GLUface) (obj));
        flag2 = false;
        obj = gluhalfedge1;
_L24:
        Object obj1;
        GLUface gluface1;
        obj1 = obj;
        gluface1 = gluface;
        if(((GLUhalfEdge) (gluface)).Lnext == obj) goto _L2; else goto _L1
_L1:
        boolean flag3;
        GLUhalfEdge gluhalfedge2;
        GLUface gluface2;
        flag3 = flag2;
        gluhalfedge2 = ((GLUhalfEdge) (obj));
        gluface2 = gluface;
        if(!flag) goto _L4; else goto _L3
_L3:
        flag3 = flag2;
        gluhalfedge2 = ((GLUhalfEdge) (obj));
        gluface2 = gluface;
        if(flag2) goto _L4; else goto _L5
_L5:
        if(Geom.EdgeCos(((GLUhalfEdge) (obj)).Lnext.Org, ((GLUhalfEdge) (obj)).Org, ((GLUhalfEdge) (obj)).Lnext.Lnext.Org) > -0.99999000000000005D) goto _L7; else goto _L6
_L6:
        obj1 = obj;
_L11:
        obj = ((GLUhalfEdge) (obj1)).Onext.Sym;
        obj1 = obj;
        gluface1 = gluface;
        if(((GLUhalfEdge) (gluface)).Lnext == obj) goto _L9; else goto _L8
_L8:
        obj1 = obj;
        if(Geom.EdgeCos(((GLUhalfEdge) (obj)).Lnext.Org, ((GLUhalfEdge) (obj)).Org, ((GLUhalfEdge) (obj)).Lnext.Lnext.Org) <= -0.99999000000000005D) goto _L11; else goto _L10
_L10:
        flag2 = true;
        gluface1 = gluface;
        obj1 = obj;
_L16:
        flag3 = flag2;
        gluhalfedge2 = ((GLUhalfEdge) (obj1));
        gluface2 = gluface1;
        if(((GLUhalfEdge) (gluface1)).Lnext != obj1)
            break; /* Loop/switch isn't completed */
_L2:
        gluface = ((GLUface) (obj1));
        if(!$assertionsDisabled)
        {
            gluface = ((GLUface) (obj1));
            if(((GLUhalfEdge) (obj1)).Lnext == gluface1)
                throw new AssertionError();
        }
          goto _L12
_L7:
        obj1 = obj;
        gluface1 = gluface;
        if(Geom.EdgeCos(((GLUhalfEdge) (gluface)).Onext.Sym.Org, ((GLUhalfEdge) (gluface)).Org, ((GLUhalfEdge) (gluface)).Onext.Sym.Onext.Sym.Org) > -0.99999000000000005D)
            continue; /* Loop/switch isn't completed */
        obj1 = gluface;
_L15:
        gluface = ((GLUhalfEdge) (obj1)).Lnext;
        obj1 = obj;
        gluface1 = gluface;
        if(((GLUhalfEdge) (gluface)).Lnext == obj) goto _L9; else goto _L13
_L13:
        obj1 = gluface;
        if(Geom.EdgeCos(((GLUhalfEdge) (gluface)).Onext.Sym.Org, ((GLUhalfEdge) (gluface)).Org, ((GLUhalfEdge) (gluface)).Onext.Sym.Onext.Sym.Org) <= -0.99999000000000005D) goto _L15; else goto _L14
_L14:
        gluface1 = gluface;
        obj1 = obj;
_L9:
        flag2 = true;
        if(true) goto _L16; else goto _L4
_L4:
        flag2 = flag3;
        gluface = gluface2;
        if(!Geom.VertLeq(((GLUhalfEdge) (gluface2)).Sym.Org, gluhalfedge2.Org)) goto _L18; else goto _L17
_L17:
        flag2 = flag3;
_L23:
        if(gluhalfedge2.Lnext == gluface2 || !Geom.EdgeGoesLeft(gluhalfedge2.Lnext) && Geom.EdgeSign(gluhalfedge2.Org, gluhalfedge2.Sym.Org, gluhalfedge2.Lnext.Sym.Org) > 0.0D) goto _L20; else goto _L19
_L19:
        gluface = Mesh.__gl_meshConnect(gluhalfedge2.Lnext, gluhalfedge2);
        if(gluface != null) goto _L22; else goto _L21
_L21:
        flag = flag1;
_L27:
        return flag;
_L22:
        gluhalfedge2 = ((GLUhalfEdge) (gluface)).Sym;
        flag2 = false;
          goto _L23
_L20:
        obj = gluhalfedge2.Onext.Sym;
        gluface = gluface2;
          goto _L24
_L26:
        gluface = ((GLUhalfEdge) (gluface)).Sym;
        flag2 = false;
_L18:
        if(gluhalfedge2.Lnext == gluface || !Geom.EdgeGoesRight(((GLUhalfEdge) (gluface)).Onext.Sym) && Geom.EdgeSign(((GLUhalfEdge) (gluface)).Sym.Org, ((GLUhalfEdge) (gluface)).Org, ((GLUhalfEdge) (gluface)).Onext.Sym.Org) < 0.0D)
            break MISSING_BLOCK_LABEL_644;
        gluface = Mesh.__gl_meshConnect(gluface, ((GLUhalfEdge) (gluface)).Onext.Sym);
        if(gluface != null) goto _L26; else goto _L25
_L25:
        flag = flag1;
          goto _L27
        gluface = ((GLUhalfEdge) (gluface)).Lnext;
        obj = gluhalfedge2;
          goto _L24
_L29:
        gluface = ((GLUhalfEdge) (gluface)).Sym;
_L12:
        if(((GLUhalfEdge) (gluface)).Lnext.Lnext == gluface1)
            break MISSING_BLOCK_LABEL_690;
        gluface = Mesh.__gl_meshConnect(((GLUhalfEdge) (gluface)).Lnext, gluface);
        if(gluface != null) goto _L29; else goto _L28
_L28:
        flag = flag1;
          goto _L27
        flag = true;
          goto _L27
    }

    static final boolean $assertionsDisabled;

    static 
    {
        boolean flag;
        if(!processing/opengl/tess/TessMono.desiredAssertionStatus())
            flag = true;
        else
            flag = false;
        $assertionsDisabled = flag;
    }
}
