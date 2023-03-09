// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.opengl.tess;


// Referenced classes of package processing.opengl.tess:
//            GLUface, GLUvertex, ActiveRegion

class GLUhalfEdge
{

    public GLUhalfEdge(boolean flag)
    {
        first = flag;
    }

    public GLUface Lface;
    public GLUhalfEdge Lnext;
    public GLUhalfEdge Onext;
    public GLUvertex Org;
    public GLUhalfEdge Sym;
    public ActiveRegion activeRegion;
    public boolean first;
    public GLUhalfEdge next;
    public int winding;
}
