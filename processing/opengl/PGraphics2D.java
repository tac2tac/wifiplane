// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.opengl;

import processing.core.*;

// Referenced classes of package processing.opengl:
//            PGraphicsOpenGL, PShapeOpenGL

public class PGraphics2D extends PGraphicsOpenGL
{

    public PGraphics2D()
    {
    }

    protected static boolean isSupportedExtension(String s)
    {
        boolean flag;
        if(s.equals("svg") || s.equals("svgz"))
            flag = true;
        else
            flag = false;
        return flag;
    }

    protected static PShape loadShapeImpl(PGraphics pgraphics, String s, String s1)
    {
        if(s1.equals("svg") || s1.equals("svgz"))
        {
            s = new PShapeSVG(pgraphics.parent.loadXML(s));
            pgraphics = PShapeOpenGL.createShape((PGraphicsOpenGL)pgraphics, s);
        } else
        {
            pgraphics = null;
        }
        return pgraphics;
    }

    public void ambientLight(float f, float f1, float f2)
    {
        showMethodWarning("ambientLight");
    }

    public void ambientLight(float f, float f1, float f2, float f3, float f4, float f5)
    {
        showMethodWarning("ambientLight");
    }

    public void applyMatrix(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8, float f9, float f10, float f11, float f12, float f13, 
            float f14, float f15)
    {
        showVariationWarning("applyMatrix");
    }

    public void applyMatrix(PMatrix3D pmatrix3d)
    {
        showVariationWarning("applyMatrix");
    }

    protected void begin2D()
    {
        pushProjection();
        defaultPerspective();
        pushMatrix();
        defaultCamera();
    }

    public void beginCamera()
    {
        showMethodWarning("beginCamera");
    }

    public void bezierVertex(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8)
    {
        showDepthWarningXYZ("bezierVertex");
    }

    public void box(float f, float f1, float f2)
    {
        showMethodWarning("box");
    }

    public void camera()
    {
        showMethodWarning("camera");
    }

    public void camera(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8)
    {
        showMethodWarning("camera");
    }

    public void curveVertex(float f, float f1, float f2)
    {
        showDepthWarningXYZ("curveVertex");
    }

    protected void defaultCamera()
    {
        eyeDist = 1.0F;
        resetMatrix();
    }

    protected void defaultPerspective()
    {
        super.ortho(0.0F, width, -height, 0.0F, -1F, 1.0F);
    }

    public void directionalLight(float f, float f1, float f2, float f3, float f4, float f5)
    {
        showMethodWarning("directionalLight");
    }

    protected void end2D()
    {
        popMatrix();
        popProjection();
    }

    public void endCamera()
    {
        showMethodWarning("endCamera");
    }

    public void frustum(float f, float f1, float f2, float f3, float f4, float f5)
    {
        showMethodWarning("frustum");
    }

    public PMatrix3D getMatrix(PMatrix3D pmatrix3d)
    {
        showVariationWarning("getMatrix");
        return pmatrix3d;
    }

    public void hint(int i)
    {
        if(i == 7)
            showWarning("Strokes cannot be perspective-corrected in 2D.");
        else
            super.hint(i);
    }

    public boolean is2D()
    {
        return true;
    }

    public boolean is3D()
    {
        return false;
    }

    public void lightFalloff(float f, float f1, float f2)
    {
        showMethodWarning("lightFalloff");
    }

    public void lightSpecular(float f, float f1, float f2)
    {
        showMethodWarning("lightSpecular");
    }

    public void lights()
    {
        showMethodWarning("lights");
    }

    public void noLights()
    {
        showMethodWarning("noLights");
    }

    public void ortho()
    {
        showMethodWarning("ortho");
    }

    public void ortho(float f, float f1, float f2, float f3)
    {
        showMethodWarning("ortho");
    }

    public void ortho(float f, float f1, float f2, float f3, float f4, float f5)
    {
        showMethodWarning("ortho");
    }

    public void perspective()
    {
        showMethodWarning("perspective");
    }

    public void perspective(float f, float f1, float f2, float f3)
    {
        showMethodWarning("perspective");
    }

    public void pointLight(float f, float f1, float f2, float f3, float f4, float f5)
    {
        showMethodWarning("pointLight");
    }

    public void quadraticVertex(float f, float f1, float f2, float f3, float f4, float f5)
    {
        showDepthWarningXYZ("quadVertex");
    }

    public void rotate(float f, float f1, float f2, float f3)
    {
        showVariationWarning("rotate");
    }

    public void rotateX(float f)
    {
        showDepthWarning("rotateX");
    }

    public void rotateY(float f)
    {
        showDepthWarning("rotateY");
    }

    public void rotateZ(float f)
    {
        showDepthWarning("rotateZ");
    }

    public void scale(float f, float f1, float f2)
    {
        showDepthWarningXYZ("scale");
    }

    public float screenX(float f, float f1, float f2)
    {
        showDepthWarningXYZ("screenX");
        return 0.0F;
    }

    public float screenY(float f, float f1, float f2)
    {
        showDepthWarningXYZ("screenY");
        return 0.0F;
    }

    public float screenZ(float f, float f1, float f2)
    {
        showDepthWarningXYZ("screenZ");
        return 0.0F;
    }

    public void setMatrix(PMatrix3D pmatrix3d)
    {
        showVariationWarning("setMatrix");
    }

    public void shape(PShape pshape)
    {
        if(pshape.is2D())
            super.shape(pshape);
        else
            showWarning("The shape object is not 2D, cannot be displayed with this renderer");
    }

    public void shape(PShape pshape, float f, float f1)
    {
        if(pshape.is2D())
            super.shape(pshape, f, f1);
        else
            showWarning("The shape object is not 2D, cannot be displayed with this renderer");
    }

    public void shape(PShape pshape, float f, float f1, float f2)
    {
        showDepthWarningXYZ("shape");
    }

    public void shape(PShape pshape, float f, float f1, float f2, float f3)
    {
        if(pshape.is2D())
            super.shape(pshape, f, f1, f2, f3);
        else
            showWarning("The shape object is not 2D, cannot be displayed with this renderer");
    }

    public void shape(PShape pshape, float f, float f1, float f2, float f3, float f4, float f5)
    {
        showDepthWarningXYZ("shape");
    }

    public void sphere(float f)
    {
        showMethodWarning("sphere");
    }

    public void spotLight(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8, float f9, float f10)
    {
        showMethodWarning("spotLight");
    }

    public void translate(float f, float f1, float f2)
    {
        showDepthWarningXYZ("translate");
    }

    public void vertex(float f, float f1, float f2)
    {
        showDepthWarningXYZ("vertex");
    }

    public void vertex(float f, float f1, float f2, float f3, float f4)
    {
        showDepthWarningXYZ("vertex");
    }
}
