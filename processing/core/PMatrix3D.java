// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.core;

import java.io.PrintStream;

// Referenced classes of package processing.core:
//            PMatrix, PMatrix2D, PVector, PApplet

public final class PMatrix3D
    implements PMatrix
{

    public PMatrix3D()
    {
        reset();
    }

    public PMatrix3D(float f, float f1, float f2, float f3, float f4, float f5)
    {
        set(f, f1, f2, 0.0F, f3, f4, f5, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
    }

    public PMatrix3D(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8, float f9, float f10, float f11, float f12, float f13, 
            float f14, float f15)
    {
        set(f, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15);
    }

    public PMatrix3D(PMatrix pmatrix)
    {
        set(pmatrix);
    }

    private static final float abs(float f)
    {
        float f1 = f;
        if(f < 0.0F)
            f1 = -f;
        return f1;
    }

    private static final float cos(float f)
    {
        return (float)Math.cos(f);
    }

    private float determinant3x3(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8)
    {
        return (f4 * f8 - f5 * f7) * f + (f5 * f6 - f3 * f8) * f1 + (f3 * f7 - f4 * f6) * f2;
    }

    private static final float max(float f, float f1)
    {
        if(f <= f1)
            f = f1;
        return f;
    }

    private static final float sin(float f)
    {
        return (float)Math.sin(f);
    }

    public void apply(float f, float f1, float f2, float f3, float f4, float f5)
    {
        apply(f, f1, 0.0F, f2, f3, f4, 0.0F, f5, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
    }

    public void apply(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8, float f9, float f10, float f11, float f12, float f13, 
            float f14, float f15)
    {
        float f16 = m00;
        float f17 = m01;
        float f18 = m02;
        float f19 = m03;
        float f20 = m00;
        float f21 = m01;
        float f22 = m02;
        float f23 = m03;
        float f24 = m00;
        float f25 = m01;
        float f26 = m02;
        float f27 = m03;
        float f28 = m00;
        float f29 = m01;
        float f30 = m02;
        float f31 = m03;
        float f32 = m10;
        float f33 = m11;
        float f34 = m12;
        float f35 = m13;
        float f36 = m10;
        float f37 = m11;
        float f38 = m12;
        float f39 = m13;
        float f40 = m10;
        float f41 = m11;
        float f42 = m12;
        float f43 = m13;
        float f44 = m10;
        float f45 = m11;
        float f46 = m12;
        float f47 = m13;
        float f48 = m20;
        float f49 = m21;
        float f50 = m22;
        float f51 = m23;
        float f52 = m20;
        float f53 = m21;
        float f54 = m22;
        float f55 = m23;
        float f56 = m20;
        float f57 = m21;
        float f58 = m22;
        float f59 = m23;
        float f60 = m20;
        float f61 = m21;
        float f62 = m22;
        float f63 = m23;
        float f64 = m30;
        float f65 = m31;
        float f66 = m32;
        float f67 = m33;
        float f68 = m30;
        float f69 = m31;
        float f70 = m32;
        float f71 = m33;
        float f72 = m30;
        float f73 = m31;
        float f74 = m32;
        float f75 = m33;
        float f76 = m30;
        float f77 = m31;
        float f78 = m32;
        float f79 = m33;
        m00 = f16 * f + f17 * f4 + f18 * f8 + f19 * f12;
        m01 = f20 * f1 + f21 * f5 + f22 * f9 + f23 * f13;
        m02 = f24 * f2 + f25 * f6 + f26 * f10 + f27 * f14;
        m03 = f28 * f3 + f29 * f7 + f30 * f11 + f31 * f15;
        m10 = f32 * f + f33 * f4 + f34 * f8 + f35 * f12;
        m11 = f36 * f1 + f37 * f5 + f38 * f9 + f39 * f13;
        m12 = f40 * f2 + f41 * f6 + f42 * f10 + f43 * f14;
        m13 = f44 * f3 + f45 * f7 + f46 * f11 + f47 * f15;
        m20 = f48 * f + f49 * f4 + f50 * f8 + f51 * f12;
        m21 = f52 * f1 + f53 * f5 + f54 * f9 + f55 * f13;
        m22 = f56 * f2 + f57 * f6 + f58 * f10 + f59 * f14;
        m23 = f60 * f3 + f61 * f7 + f62 * f11 + f63 * f15;
        m30 = f64 * f + f65 * f4 + f66 * f8 + f67 * f12;
        m31 = f68 * f1 + f69 * f5 + f70 * f9 + f71 * f13;
        m32 = f72 * f2 + f73 * f6 + f74 * f10 + f75 * f14;
        m33 = f76 * f3 + f77 * f7 + f78 * f11 + f79 * f15;
    }

    public void apply(PMatrix2D pmatrix2d)
    {
        apply(pmatrix2d.m00, pmatrix2d.m01, 0.0F, pmatrix2d.m02, pmatrix2d.m10, pmatrix2d.m11, 0.0F, pmatrix2d.m12, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
    }

    public void apply(PMatrix3D pmatrix3d)
    {
        apply(pmatrix3d.m00, pmatrix3d.m01, pmatrix3d.m02, pmatrix3d.m03, pmatrix3d.m10, pmatrix3d.m11, pmatrix3d.m12, pmatrix3d.m13, pmatrix3d.m20, pmatrix3d.m21, pmatrix3d.m22, pmatrix3d.m23, pmatrix3d.m30, pmatrix3d.m31, pmatrix3d.m32, pmatrix3d.m33);
    }

    public void apply(PMatrix pmatrix)
    {
        if(!(pmatrix instanceof PMatrix2D)) goto _L2; else goto _L1
_L1:
        apply((PMatrix2D)pmatrix);
_L4:
        return;
_L2:
        if(pmatrix instanceof PMatrix3D)
            apply((PMatrix3D)pmatrix);
        if(true) goto _L4; else goto _L3
_L3:
    }

    public float determinant()
    {
        return ((m00 * ((m11 * m22 * m33 + m12 * m23 * m31 + m13 * m21 * m32) - m13 * m22 * m31 - m11 * m23 * m32 - m12 * m21 * m33) - m01 * ((m10 * m22 * m33 + m12 * m23 * m30 + m13 * m20 * m32) - m13 * m22 * m30 - m10 * m23 * m32 - m12 * m20 * m33)) + m02 * ((m10 * m21 * m33 + m11 * m23 * m30 + m13 * m20 * m31) - m13 * m21 * m30 - m10 * m23 * m31 - m11 * m20 * m33)) - m03 * ((m10 * m21 * m32 + m11 * m22 * m30 + m12 * m20 * m31) - m12 * m21 * m30 - m10 * m22 * m31 - m11 * m20 * m32);
    }

    public PMatrix3D get()
    {
        PMatrix3D pmatrix3d = new PMatrix3D();
        pmatrix3d.set(this);
        return pmatrix3d;
    }

    public volatile PMatrix get()
    {
        return get();
    }

    public float[] get(float af[])
    {
        float af1[];
label0:
        {
            if(af != null)
            {
                af1 = af;
                if(af.length == 16)
                    break label0;
            }
            af1 = new float[16];
        }
        af1[0] = m00;
        af1[1] = m01;
        af1[2] = m02;
        af1[3] = m03;
        af1[4] = m10;
        af1[5] = m11;
        af1[6] = m12;
        af1[7] = m13;
        af1[8] = m20;
        af1[9] = m21;
        af1[10] = m22;
        af1[11] = m23;
        af1[12] = m30;
        af1[13] = m31;
        af1[14] = m32;
        af1[15] = m33;
        return af1;
    }

    protected boolean invApply(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8, float f9, float f10, float f11, float f12, float f13, 
            float f14, float f15)
    {
        if(inverseCopy == null)
            inverseCopy = new PMatrix3D();
        inverseCopy.set(f, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15);
        boolean flag;
        if(!inverseCopy.invert())
        {
            flag = false;
        } else
        {
            preApply(inverseCopy);
            flag = true;
        }
        return flag;
    }

    protected void invRotate(float f, float f1, float f2, float f3)
    {
        float f4 = cos(-f);
        float f5 = sin(-f);
        f = 1.0F - f4;
        preApply(f * f1 * f1 + f4, f * f1 * f2 - f5 * f3, f * f1 * f3 + f5 * f2, 0.0F, f * f1 * f2 + f5 * f3, f * f2 * f2 + f4, f * f2 * f3 - f5 * f1, 0.0F, f * f1 * f3 - f5 * f2, f5 * f1 + f * f2 * f3, f * f3 * f3 + f4, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
    }

    protected void invRotateX(float f)
    {
        float f1 = cos(-f);
        f = sin(-f);
        preApply(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, f1, -f, 0.0F, 0.0F, f, f1, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
    }

    protected void invRotateY(float f)
    {
        float f1 = cos(-f);
        f = sin(-f);
        preApply(f1, 0.0F, f, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, -f, 0.0F, f1, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
    }

    protected void invRotateZ(float f)
    {
        float f1 = cos(-f);
        f = sin(-f);
        preApply(f1, -f, 0.0F, 0.0F, f, f1, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
    }

    protected void invScale(float f, float f1, float f2)
    {
        preApply(1.0F / f, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F / f1, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F / f2, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
    }

    protected void invTranslate(float f, float f1, float f2)
    {
        preApply(1.0F, 0.0F, 0.0F, -f, 0.0F, 1.0F, 0.0F, -f1, 0.0F, 0.0F, 1.0F, -f2, 0.0F, 0.0F, 0.0F, 1.0F);
    }

    public boolean invert()
    {
        float f = determinant();
        boolean flag;
        if(f == 0.0F)
        {
            flag = false;
        } else
        {
            float f1 = determinant3x3(m11, m12, m13, m21, m22, m23, m31, m32, m33);
            float f2 = -determinant3x3(m10, m12, m13, m20, m22, m23, m30, m32, m33);
            float f3 = determinant3x3(m10, m11, m13, m20, m21, m23, m30, m31, m33);
            float f4 = -determinant3x3(m10, m11, m12, m20, m21, m22, m30, m31, m32);
            float f5 = -determinant3x3(m01, m02, m03, m21, m22, m23, m31, m32, m33);
            float f6 = determinant3x3(m00, m02, m03, m20, m22, m23, m30, m32, m33);
            float f7 = -determinant3x3(m00, m01, m03, m20, m21, m23, m30, m31, m33);
            float f8 = determinant3x3(m00, m01, m02, m20, m21, m22, m30, m31, m32);
            float f9 = determinant3x3(m01, m02, m03, m11, m12, m13, m31, m32, m33);
            float f10 = -determinant3x3(m00, m02, m03, m10, m12, m13, m30, m32, m33);
            float f11 = determinant3x3(m00, m01, m03, m10, m11, m13, m30, m31, m33);
            float f12 = -determinant3x3(m00, m01, m02, m10, m11, m12, m30, m31, m32);
            float f13 = -determinant3x3(m01, m02, m03, m11, m12, m13, m21, m22, m23);
            float f14 = determinant3x3(m00, m02, m03, m10, m12, m13, m20, m22, m23);
            float f15 = -determinant3x3(m00, m01, m03, m10, m11, m13, m20, m21, m23);
            float f16 = determinant3x3(m00, m01, m02, m10, m11, m12, m20, m21, m22);
            m00 = f1 / f;
            m01 = f5 / f;
            m02 = f9 / f;
            m03 = f13 / f;
            m10 = f2 / f;
            m11 = f6 / f;
            m12 = f10 / f;
            m13 = f14 / f;
            m20 = f3 / f;
            m21 = f7 / f;
            m22 = f11 / f;
            m23 = f15 / f;
            m30 = f4 / f;
            m31 = f8 / f;
            m32 = f12 / f;
            m33 = f16 / f;
            flag = true;
        }
        return flag;
    }

    public PVector mult(PVector pvector, PVector pvector1)
    {
        PVector pvector2 = pvector1;
        if(pvector1 == null)
            pvector2 = new PVector();
        pvector2.set(m00 * pvector.x + m01 * pvector.y + m02 * pvector.z + m03, m10 * pvector.x + m11 * pvector.y + m12 * pvector.z + m13, m20 * pvector.x + m21 * pvector.y + m22 * pvector.z + m23);
        return pvector2;
    }

    public float[] mult(float af[], float af1[])
    {
        float af2[];
label0:
        {
            if(af1 != null)
            {
                af2 = af1;
                if(af1.length >= 3)
                    break label0;
            }
            af2 = new float[3];
        }
        if(af == af2)
            throw new RuntimeException("The source and target vectors used in PMatrix3D.mult() cannot be identical.");
        if(af2.length != 3) goto _L2; else goto _L1
_L1:
        af2[0] = m00 * af[0] + m01 * af[1] + m02 * af[2] + m03;
        af2[1] = m10 * af[0] + m11 * af[1] + m12 * af[2] + m13;
        af2[2] = m20 * af[0] + m21 * af[1] + m22 * af[2] + m23;
_L4:
        return af2;
_L2:
        if(af2.length > 3)
        {
            af2[0] = m00 * af[0] + m01 * af[1] + m02 * af[2] + m03 * af[3];
            af2[1] = m10 * af[0] + m11 * af[1] + m12 * af[2] + m13 * af[3];
            af2[2] = m20 * af[0] + m21 * af[1] + m22 * af[2] + m23 * af[3];
            af2[3] = m30 * af[0] + m31 * af[1] + m32 * af[2] + m33 * af[3];
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public float multW(float f, float f1, float f2)
    {
        return m30 * f + m31 * f1 + m32 * f2 + m33;
    }

    public float multW(float f, float f1, float f2, float f3)
    {
        return m30 * f + m31 * f1 + m32 * f2 + m33 * f3;
    }

    public float multX(float f, float f1)
    {
        return m00 * f + m01 * f1 + m03;
    }

    public float multX(float f, float f1, float f2)
    {
        return m00 * f + m01 * f1 + m02 * f2 + m03;
    }

    public float multX(float f, float f1, float f2, float f3)
    {
        return m00 * f + m01 * f1 + m02 * f2 + m03 * f3;
    }

    public float multY(float f, float f1)
    {
        return m10 * f + m11 * f1 + m13;
    }

    public float multY(float f, float f1, float f2)
    {
        return m10 * f + m11 * f1 + m12 * f2 + m13;
    }

    public float multY(float f, float f1, float f2, float f3)
    {
        return m10 * f + m11 * f1 + m12 * f2 + m13 * f3;
    }

    public float multZ(float f, float f1, float f2)
    {
        return m20 * f + m21 * f1 + m22 * f2 + m23;
    }

    public float multZ(float f, float f1, float f2, float f3)
    {
        return m20 * f + m21 * f1 + m22 * f2 + m23 * f3;
    }

    public void preApply(float f, float f1, float f2, float f3, float f4, float f5)
    {
        preApply(f, f1, 0.0F, f2, f3, f4, 0.0F, f5, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
    }

    public void preApply(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8, float f9, float f10, float f11, float f12, float f13, 
            float f14, float f15)
    {
        float f16 = m00;
        float f17 = m10;
        float f18 = m20;
        float f19 = m30;
        float f20 = m01;
        float f21 = m11;
        float f22 = m21;
        float f23 = m31;
        float f24 = m02;
        float f25 = m12;
        float f26 = m22;
        float f27 = m32;
        float f28 = m03;
        float f29 = m13;
        float f30 = m23;
        float f31 = m33;
        float f32 = m00;
        float f33 = m10;
        float f34 = m20;
        float f35 = m30;
        float f36 = m01;
        float f37 = m11;
        float f38 = m21;
        float f39 = m31;
        float f40 = m02;
        float f41 = m12;
        float f42 = m22;
        float f43 = m32;
        float f44 = m03;
        float f45 = m13;
        float f46 = m23;
        float f47 = m33;
        float f48 = m00;
        float f49 = m10;
        float f50 = m20;
        float f51 = m30;
        float f52 = m01;
        float f53 = m11;
        float f54 = m21;
        float f55 = m31;
        float f56 = m02;
        float f57 = m12;
        float f58 = m22;
        float f59 = m32;
        float f60 = m03;
        float f61 = m13;
        float f62 = m23;
        float f63 = m33;
        float f64 = m00;
        float f65 = m10;
        float f66 = m20;
        float f67 = m30;
        float f68 = m01;
        float f69 = m11;
        float f70 = m21;
        float f71 = m31;
        float f72 = m02;
        float f73 = m12;
        float f74 = m22;
        float f75 = m32;
        float f76 = m03;
        float f77 = m13;
        float f78 = m23;
        float f79 = m33;
        m00 = f16 * f + f17 * f1 + f18 * f2 + f19 * f3;
        m01 = f20 * f + f21 * f1 + f22 * f2 + f23 * f3;
        m02 = f24 * f + f25 * f1 + f26 * f2 + f27 * f3;
        m03 = f28 * f + f29 * f1 + f30 * f2 + f31 * f3;
        m10 = f32 * f4 + f33 * f5 + f34 * f6 + f35 * f7;
        m11 = f36 * f4 + f37 * f5 + f38 * f6 + f39 * f7;
        m12 = f40 * f4 + f41 * f5 + f42 * f6 + f43 * f7;
        m13 = f44 * f4 + f45 * f5 + f46 * f6 + f47 * f7;
        m20 = f48 * f8 + f49 * f9 + f50 * f10 + f51 * f11;
        m21 = f52 * f8 + f53 * f9 + f54 * f10 + f55 * f11;
        m22 = f56 * f8 + f57 * f9 + f58 * f10 + f59 * f11;
        m23 = f60 * f8 + f61 * f9 + f62 * f10 + f63 * f11;
        m30 = f64 * f12 + f65 * f13 + f66 * f14 + f67 * f15;
        m31 = f68 * f12 + f69 * f13 + f70 * f14 + f71 * f15;
        m32 = f72 * f12 + f73 * f13 + f74 * f14 + f75 * f15;
        m33 = f76 * f12 + f77 * f13 + f78 * f14 + f79 * f15;
    }

    public void preApply(PMatrix2D pmatrix2d)
    {
        preApply(pmatrix2d.m00, pmatrix2d.m01, 0.0F, pmatrix2d.m02, pmatrix2d.m10, pmatrix2d.m11, 0.0F, pmatrix2d.m12, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
    }

    public void preApply(PMatrix3D pmatrix3d)
    {
        preApply(pmatrix3d.m00, pmatrix3d.m01, pmatrix3d.m02, pmatrix3d.m03, pmatrix3d.m10, pmatrix3d.m11, pmatrix3d.m12, pmatrix3d.m13, pmatrix3d.m20, pmatrix3d.m21, pmatrix3d.m22, pmatrix3d.m23, pmatrix3d.m30, pmatrix3d.m31, pmatrix3d.m32, pmatrix3d.m33);
    }

    public void print()
    {
        int i;
        int k;
        i = (int)Math.abs(max(max(max(max(abs(m00), abs(m01)), max(abs(m02), abs(m03))), max(max(abs(m10), abs(m11)), max(abs(m12), abs(m13)))), max(max(max(abs(m20), abs(m21)), max(abs(m22), abs(m23))), max(max(abs(m30), abs(m31)), max(abs(m32), abs(m33))))));
        k = 1;
        if(Float.isNaN(i)) goto _L2; else goto _L1
_L1:
        int l = i;
        if(!Float.isInfinite(i)) goto _L3; else goto _L2
_L2:
        l = 5;
_L5:
        System.out.println((new StringBuilder()).append(PApplet.nfs(m00, l, 4)).append(" ").append(PApplet.nfs(m01, l, 4)).append(" ").append(PApplet.nfs(m02, l, 4)).append(" ").append(PApplet.nfs(m03, l, 4)).toString());
        System.out.println((new StringBuilder()).append(PApplet.nfs(m10, l, 4)).append(" ").append(PApplet.nfs(m11, l, 4)).append(" ").append(PApplet.nfs(m12, l, 4)).append(" ").append(PApplet.nfs(m13, l, 4)).toString());
        System.out.println((new StringBuilder()).append(PApplet.nfs(m20, l, 4)).append(" ").append(PApplet.nfs(m21, l, 4)).append(" ").append(PApplet.nfs(m22, l, 4)).append(" ").append(PApplet.nfs(m23, l, 4)).toString());
        System.out.println((new StringBuilder()).append(PApplet.nfs(m30, l, 4)).append(" ").append(PApplet.nfs(m31, l, 4)).append(" ").append(PApplet.nfs(m32, l, 4)).append(" ").append(PApplet.nfs(m33, l, 4)).toString());
        System.out.println();
        return;
_L3:
        do
        {
            int j = l / 10;
            l = k;
            if(j == 0)
                continue;
            k++;
            l = j;
        } while(true);
        if(true) goto _L5; else goto _L4
_L4:
    }

    public void reset()
    {
        set(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
    }

    public void rotate(float f)
    {
        rotateZ(f);
    }

    public void rotate(float f, float f1, float f2, float f3)
    {
        float f4 = f1 * f1 + f2 * f2 + f3 * f3;
        if(f4 >= 0.0001F)
        {
            float f5 = f1;
            float f6 = f2;
            float f7 = f3;
            if(Math.abs(f4 - 1.0F) > 0.0001F)
            {
                f7 = PApplet.sqrt(f4);
                f5 = f1 / f7;
                f6 = f2 / f7;
                f7 = f3 / f7;
            }
            f1 = cos(f);
            f2 = sin(f);
            f = 1.0F - f1;
            apply(f * f5 * f5 + f1, f * f5 * f6 - f2 * f7, f * f5 * f7 + f2 * f6, 0.0F, f * f5 * f6 + f2 * f7, f * f6 * f6 + f1, f * f6 * f7 - f2 * f5, 0.0F, f * f5 * f7 - f2 * f6, f2 * f5 + f * f6 * f7, f * f7 * f7 + f1, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
        }
    }

    public void rotateX(float f)
    {
        float f1 = cos(f);
        f = sin(f);
        apply(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, f1, -f, 0.0F, 0.0F, f, f1, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
    }

    public void rotateY(float f)
    {
        float f1 = cos(f);
        f = sin(f);
        apply(f1, 0.0F, f, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, -f, 0.0F, f1, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
    }

    public void rotateZ(float f)
    {
        float f1 = cos(f);
        f = sin(f);
        apply(f1, -f, 0.0F, 0.0F, f, f1, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
    }

    public void scale(float f)
    {
        scale(f, f, f);
    }

    public void scale(float f, float f1)
    {
        scale(f, f1, 1.0F);
    }

    public void scale(float f, float f1, float f2)
    {
        m00 = m00 * f;
        m01 = m01 * f1;
        m02 = m02 * f2;
        m10 = m10 * f;
        m11 = m11 * f1;
        m12 = m12 * f2;
        m20 = m20 * f;
        m21 = m21 * f1;
        m22 = m22 * f2;
        m30 = m30 * f;
        m31 = m31 * f1;
        m32 = m32 * f2;
    }

    public void set(float f, float f1, float f2, float f3, float f4, float f5)
    {
        set(f, f1, 0.0F, f2, f3, f4, 0.0F, f5, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
    }

    public void set(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8, float f9, float f10, float f11, float f12, float f13, 
            float f14, float f15)
    {
        m00 = f;
        m01 = f1;
        m02 = f2;
        m03 = f3;
        m10 = f4;
        m11 = f5;
        m12 = f6;
        m13 = f7;
        m20 = f8;
        m21 = f9;
        m22 = f10;
        m23 = f11;
        m30 = f12;
        m31 = f13;
        m32 = f14;
        m33 = f15;
    }

    public void set(PMatrix pmatrix)
    {
        if(pmatrix instanceof PMatrix3D)
        {
            pmatrix = (PMatrix3D)pmatrix;
            set(((PMatrix3D) (pmatrix)).m00, ((PMatrix3D) (pmatrix)).m01, ((PMatrix3D) (pmatrix)).m02, ((PMatrix3D) (pmatrix)).m03, ((PMatrix3D) (pmatrix)).m10, ((PMatrix3D) (pmatrix)).m11, ((PMatrix3D) (pmatrix)).m12, ((PMatrix3D) (pmatrix)).m13, ((PMatrix3D) (pmatrix)).m20, ((PMatrix3D) (pmatrix)).m21, ((PMatrix3D) (pmatrix)).m22, ((PMatrix3D) (pmatrix)).m23, ((PMatrix3D) (pmatrix)).m30, ((PMatrix3D) (pmatrix)).m31, ((PMatrix3D) (pmatrix)).m32, ((PMatrix3D) (pmatrix)).m33);
        } else
        {
            pmatrix = (PMatrix2D)pmatrix;
            set(((PMatrix2D) (pmatrix)).m00, ((PMatrix2D) (pmatrix)).m01, 0.0F, ((PMatrix2D) (pmatrix)).m02, ((PMatrix2D) (pmatrix)).m10, ((PMatrix2D) (pmatrix)).m11, 0.0F, ((PMatrix2D) (pmatrix)).m12, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
        }
    }

    public void set(float af[])
    {
        if(af.length != 6) goto _L2; else goto _L1
_L1:
        set(af[0], af[1], af[2], af[3], af[4], af[5]);
_L4:
        return;
_L2:
        if(af.length == 16)
        {
            m00 = af[0];
            m01 = af[1];
            m02 = af[2];
            m03 = af[3];
            m10 = af[4];
            m11 = af[5];
            m12 = af[6];
            m13 = af[7];
            m20 = af[8];
            m21 = af[9];
            m22 = af[10];
            m23 = af[11];
            m30 = af[12];
            m31 = af[13];
            m32 = af[14];
            m33 = af[15];
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void shearX(float f)
    {
        apply(1.0F, (float)Math.tan(f), 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
    }

    public void shearY(float f)
    {
        apply(1.0F, 0.0F, 0.0F, 0.0F, (float)Math.tan(f), 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
    }

    public void translate(float f, float f1)
    {
        translate(f, f1, 0.0F);
    }

    public void translate(float f, float f1, float f2)
    {
        m03 = m03 + (m00 * f + m01 * f1 + m02 * f2);
        m13 = m13 + (m10 * f + m11 * f1 + m12 * f2);
        m23 = m23 + (m20 * f + m21 * f1 + m22 * f2);
        m33 = m33 + (m30 * f + m31 * f1 + m32 * f2);
    }

    public void transpose()
    {
        float f = m01;
        m01 = m10;
        m10 = f;
        f = m02;
        m02 = m20;
        m20 = f;
        f = m03;
        m03 = m30;
        m30 = f;
        f = m12;
        m12 = m21;
        m21 = f;
        f = m13;
        m13 = m31;
        m31 = f;
        f = m23;
        m23 = m32;
        m32 = f;
    }

    protected PMatrix3D inverseCopy;
    public float m00;
    public float m01;
    public float m02;
    public float m03;
    public float m10;
    public float m11;
    public float m12;
    public float m13;
    public float m20;
    public float m21;
    public float m22;
    public float m23;
    public float m30;
    public float m31;
    public float m32;
    public float m33;
}
