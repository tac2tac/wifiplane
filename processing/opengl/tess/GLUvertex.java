// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.opengl.tess;


// Referenced classes of package processing.opengl.tess:
//            GLUhalfEdge

class GLUvertex
{

    GLUvertex()
    {
        coords = new double[3];
    }

    public GLUhalfEdge anEdge;
    public double coords[];
    public Object data;
    public GLUvertex next;
    public int pqHandle;
    public GLUvertex prev;
    public double s;
    public double t;
}
