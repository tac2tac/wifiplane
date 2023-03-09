// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.opengl.tess;


// Referenced classes of package processing.opengl.tess:
//            GLUhalfEdge

class GLUface
{

    GLUface()
    {
    }

    public GLUhalfEdge anEdge;
    public Object data;
    public boolean inside;
    public boolean marked;
    public GLUface next;
    public GLUface prev;
    public GLUface trail;
}
