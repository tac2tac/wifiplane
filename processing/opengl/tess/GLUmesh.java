// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.opengl.tess;


// Referenced classes of package processing.opengl.tess:
//            GLUvertex, GLUface, GLUhalfEdge

class GLUmesh
{

    GLUmesh()
    {
        vHead = new GLUvertex();
        fHead = new GLUface();
        eHead = new GLUhalfEdge(true);
        eHeadSym = new GLUhalfEdge(false);
    }

    GLUhalfEdge eHead;
    GLUhalfEdge eHeadSym;
    GLUface fHead;
    GLUvertex vHead;
}
