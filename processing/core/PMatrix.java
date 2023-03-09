// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.core;


// Referenced classes of package processing.core:
//            PMatrix2D, PMatrix3D, PVector

public interface PMatrix
{

    public abstract void apply(float f, float f1, float f2, float f3, float f4, float f5);

    public abstract void apply(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8, float f9, float f10, float f11, float f12, float f13, 
            float f14, float f15);

    public abstract void apply(PMatrix2D pmatrix2d);

    public abstract void apply(PMatrix3D pmatrix3d);

    public abstract void apply(PMatrix pmatrix);

    public abstract float determinant();

    public abstract PMatrix get();

    public abstract float[] get(float af[]);

    public abstract boolean invert();

    public abstract PVector mult(PVector pvector, PVector pvector1);

    public abstract float[] mult(float af[], float af1[]);

    public abstract void preApply(float f, float f1, float f2, float f3, float f4, float f5);

    public abstract void preApply(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8, float f9, float f10, float f11, float f12, float f13, 
            float f14, float f15);

    public abstract void preApply(PMatrix2D pmatrix2d);

    public abstract void preApply(PMatrix3D pmatrix3d);

    public abstract void reset();

    public abstract void rotate(float f);

    public abstract void rotate(float f, float f1, float f2, float f3);

    public abstract void rotateX(float f);

    public abstract void rotateY(float f);

    public abstract void rotateZ(float f);

    public abstract void scale(float f);

    public abstract void scale(float f, float f1);

    public abstract void scale(float f, float f1, float f2);

    public abstract void set(float f, float f1, float f2, float f3, float f4, float f5);

    public abstract void set(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8, float f9, float f10, float f11, float f12, float f13, 
            float f14, float f15);

    public abstract void set(PMatrix pmatrix);

    public abstract void set(float af[]);

    public abstract void shearX(float f);

    public abstract void shearY(float f);

    public abstract void translate(float f, float f1);

    public abstract void translate(float f, float f1, float f2);

    public abstract void transpose();
}
