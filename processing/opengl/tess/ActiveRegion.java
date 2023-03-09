// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.opengl.tess;


// Referenced classes of package processing.opengl.tess:
//            GLUhalfEdge, DictNode

class ActiveRegion
{

    ActiveRegion()
    {
    }

    boolean dirty;
    GLUhalfEdge eUp;
    boolean fixUpperEdge;
    boolean inside;
    DictNode nodeUp;
    boolean sentinel;
    int windingNumber;
}
