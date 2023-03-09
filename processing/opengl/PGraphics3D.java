// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.opengl;

import processing.core.*;

// Referenced classes of package processing.opengl:
//            PGraphicsOpenGL, PShapeOpenGL

public class PGraphics3D extends PGraphicsOpenGL
{

    public PGraphics3D()
    {
    }

    protected static boolean isSupportedExtension(String s)
    {
        return s.equals("obj");
    }

    protected static PShape loadShapeImpl(PGraphics pgraphics, String s, String s1)
    {
        if(s1.equals("obj"))
        {
            s = new PShapeOBJ(pgraphics.parent, s);
            int i = pgraphics.textureMode;
            pgraphics.textureMode = 1;
            s = PShapeOpenGL.createShape((PGraphicsOpenGL)pgraphics, s);
            pgraphics.textureMode = i;
            pgraphics = s;
        } else
        {
            pgraphics = null;
        }
        return pgraphics;
    }

    protected void begin2D()
    {
        pushProjection();
        ortho((float)(-width) / 2.0F, (float)width / 2.0F, (float)(-height) / 2.0F, (float)height / 2.0F);
        pushMatrix();
        float f = (float)width / 2.0F;
        float f1 = (float)height / 2.0F;
        modelview.reset();
        modelview.translate(-f, -f1);
        modelviewInv.set(modelview);
        modelviewInv.invert();
        camera.set(modelview);
        cameraInv.set(modelviewInv);
        updateProjmodelview();
    }

    protected void defaultCamera()
    {
        camera();
    }

    protected void defaultPerspective()
    {
        perspective();
    }

    protected void end2D()
    {
        popMatrix();
        popProjection();
    }

    public boolean is2D()
    {
        return false;
    }

    public boolean is3D()
    {
        return true;
    }
}
