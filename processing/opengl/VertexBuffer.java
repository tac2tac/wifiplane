// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.opengl;


// Referenced classes of package processing.opengl:
//            PGraphicsOpenGL, PGL

public class VertexBuffer
{

    VertexBuffer(PGraphicsOpenGL pgraphicsopengl, int i, int j, int k)
    {
        this(pgraphicsopengl, i, j, k, false);
    }

    VertexBuffer(PGraphicsOpenGL pgraphicsopengl, int i, int j, int k, boolean flag)
    {
        pgl = pgraphicsopengl.pgl;
        context = pgl.createEmptyContext();
        target = i;
        ncoords = j;
        elementSize = k;
        index = flag;
        create();
        init();
    }

    protected boolean contextIsOutdated()
    {
        boolean flag;
        if(!pgl.contextIsCurrent(context))
            flag = true;
        else
            flag = false;
        if(flag)
            dispose();
        return flag;
    }

    protected void create()
    {
        context = pgl.getCurrentContext();
        glres = new PGraphicsOpenGL.GLResourceVertexBuffer(this);
    }

    protected void dispose()
    {
        if(glres != null)
        {
            glres.dispose();
            glId = 0;
            glres = null;
        }
    }

    protected void init()
    {
        int i;
        if(index)
            i = ncoords * 512 * elementSize;
        else
            i = ncoords * 256 * elementSize;
        pgl.bindBuffer(target, glId);
        pgl.bufferData(target, i, null, PGL.STATIC_DRAW);
    }

    protected static final int INIT_INDEX_BUFFER_SIZE = 512;
    protected static final int INIT_VERTEX_BUFFER_SIZE = 256;
    protected int context;
    int elementSize;
    public int glId;
    private PGraphicsOpenGL.GLResourceVertexBuffer glres;
    boolean index;
    int ncoords;
    protected PGL pgl;
    int target;
}
