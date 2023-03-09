// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.core;

import java.io.PrintStream;

// Referenced classes of package processing.core:
//            PMatrix, PMatrix3D, PVector, PApplet

public class PMatrix2D
    implements PMatrix
{

    public PMatrix2D()
    {
        reset();
    }

    public PMatrix2D(float f, float f1, float f2, float f3, float f4, float f5)
    {
        set(f, f1, f2, f3, f4, f5);
    }

    public PMatrix2D(PMatrix pmatrix)
    {
        set(pmatrix);
    }

    private final float abs(float f)
    {
        float f1 = f;
        if(f < 0.0F)
            f1 = -f;
        return f1;
    }

    private final float cos(float f)
    {
        return (float)Math.cos(f);
    }

    private final float max(float f, float f1)
    {
        if(f <= f1)
            f = f1;
        return f;
    }

    private final float sin(float f)
    {
        return (float)Math.sin(f);
    }

    private final float tan(float f)
    {
        return (float)Math.tan(f);
    }

    public void apply(float f, float f1, float f2, float f3, float f4, float f5)
    {
        float f6 = m00;
        float f7 = m01;
        m00 = f * f6 + f3 * f7;
        m01 = f1 * f6 + f4 * f7;
        m02 = f6 * f2 + f7 * f5 + m02;
        f6 = m10;
        f7 = m11;
        m10 = f * f6 + f3 * f7;
        m11 = f1 * f6 + f4 * f7;
        m12 = f6 * f2 + f7 * f5 + m12;
    }

    public void apply(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8, float f9, float f10, float f11, float f12, float f13, 
            float f14, float f15)
    {
        throw new IllegalArgumentException("Cannot use this version of apply() on a PMatrix2D.");
    }

    public void apply(PMatrix2D pmatrix2d)
    {
        apply(pmatrix2d.m00, pmatrix2d.m01, pmatrix2d.m02, pmatrix2d.m10, pmatrix2d.m11, pmatrix2d.m12);
    }

    public void apply(PMatrix3D pmatrix3d)
    {
        throw new IllegalArgumentException("Cannot use apply(PMatrix3D) on a PMatrix2D.");
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
        return m00 * m11 - m01 * m10;
    }

    public PMatrix2D get()
    {
        PMatrix2D pmatrix2d = new PMatrix2D();
        pmatrix2d.set(this);
        return pmatrix2d;
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
                if(af.length == 6)
                    break label0;
            }
            af1 = new float[6];
        }
        af1[0] = m00;
        af1[1] = m01;
        af1[2] = m02;
        af1[3] = m10;
        af1[4] = m11;
        af1[5] = m12;
        return af1;
    }

    public boolean invert()
    {
        float f = determinant();
        boolean flag;
        if(Math.abs(f) <= 1.401298E-045F)
        {
            flag = false;
        } else
        {
            float f1 = m00;
            float f2 = m01;
            float f3 = m02;
            float f4 = m10;
            float f5 = m11;
            float f6 = m12;
            m00 = f5 / f;
            m10 = -f4 / f;
            m01 = -f2 / f;
            m11 = f1 / f;
            m02 = (f2 * f6 - f5 * f3) / f;
            m12 = (f4 * f3 - f1 * f6) / f;
            flag = true;
        }
        return flag;
    }

    protected boolean isIdentity()
    {
        boolean flag;
        if(m00 == 1.0F && m01 == 0.0F && m02 == 0.0F && m10 == 0.0F && m11 == 1.0F && m12 == 0.0F)
            flag = true;
        else
            flag = false;
        return flag;
    }

    protected boolean isWarped()
    {
        boolean flag;
        if(m00 != 1.0F || m01 != 0.0F || m10 != 0.0F || m11 != 1.0F)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public PVector mult(PVector pvector, PVector pvector1)
    {
        PVector pvector2 = pvector1;
        if(pvector1 == null)
            pvector2 = new PVector();
        pvector2.x = m00 * pvector.x + m01 * pvector.y + m02;
        pvector2.y = m10 * pvector.x + m11 * pvector.y + m12;
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
                if(af1.length == 2)
                    break label0;
            }
            af2 = new float[2];
        }
        if(af == af2)
        {
            float f = m00;
            float f1 = af[0];
            float f2 = m01;
            float f3 = af[1];
            float f4 = m02;
            float f5 = m10;
            float f6 = af[0];
            float f7 = m11;
            float f8 = af[1];
            float f9 = m12;
            af2[0] = f * f1 + f2 * f3 + f4;
            af2[1] = f5 * f6 + f7 * f8 + f9;
        } else
        {
            af2[0] = m00 * af[0] + m01 * af[1] + m02;
            af2[1] = m10 * af[0] + m11 * af[1] + m12;
        }
        return af2;
    }

    public float multX(float f, float f1)
    {
        return m00 * f + m01 * f1 + m02;
    }

    public float multY(float f, float f1)
    {
        return m10 * f + m11 * f1 + m12;
    }

    public void preApply(float f, float f1, float f2, float f3, float f4, float f5)
    {
        float f6 = m02;
        float f7 = m12;
        m02 = f6 * f + f7 * f1 + f2;
        m12 = f6 * f3 + f7 * f4 + f5;
        f2 = m00;
        f5 = m10;
        m00 = f2 * f + f5 * f1;
        m10 = f2 * f3 + f5 * f4;
        f2 = m01;
        f5 = m11;
        m01 = f2 * f + f5 * f1;
        m11 = f2 * f3 + f5 * f4;
    }

    public void preApply(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8, float f9, float f10, float f11, float f12, float f13, 
            float f14, float f15)
    {
        throw new IllegalArgumentException("Cannot use this version of preApply() on a PMatrix2D.");
    }

    public void preApply(PMatrix2D pmatrix2d)
    {
        preApply(pmatrix2d.m00, pmatrix2d.m01, pmatrix2d.m02, pmatrix2d.m10, pmatrix2d.m11, pmatrix2d.m12);
    }

    public void preApply(PMatrix3D pmatrix3d)
    {
        throw new IllegalArgumentException("Cannot use preApply(PMatrix3D) on a PMatrix2D.");
    }

    public void print()
    {
        int i;
        int k;
        i = (int)abs(max(PApplet.max(abs(m00), abs(m01), abs(m02)), PApplet.max(abs(m10), abs(m11), abs(m12))));
        k = 1;
        if(Float.isNaN(i)) goto _L2; else goto _L1
_L1:
        int l = i;
        if(!Float.isInfinite(i)) goto _L3; else goto _L2
_L2:
        l = 5;
_L5:
        System.out.println((new StringBuilder()).append(PApplet.nfs(m00, l, 4)).append(" ").append(PApplet.nfs(m01, l, 4)).append(" ").append(PApplet.nfs(m02, l, 4)).toString());
        System.out.println((new StringBuilder()).append(PApplet.nfs(m10, l, 4)).append(" ").append(PApplet.nfs(m11, l, 4)).append(" ").append(PApplet.nfs(m12, l, 4)).toString());
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
        set(1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F);
    }

    public void rotate(float f)
    {
        float f1 = sin(f);
        f = cos(f);
        float f2 = m00;
        float f3 = m01;
        m00 = f * f2 + f1 * f3;
        m01 = f2 * -f1 + f3 * f;
        f3 = m10;
        f2 = m11;
        m10 = f * f3 + f1 * f2;
        m11 = -f1 * f3 + f * f2;
    }

    public void rotate(float f, float f1, float f2, float f3)
    {
        throw new IllegalArgumentException("Cannot use this version of rotate() on a PMatrix2D.");
    }

    public void rotateX(float f)
    {
        throw new IllegalArgumentException("Cannot use rotateX() on a PMatrix2D.");
    }

    public void rotateY(float f)
    {
        throw new IllegalArgumentException("Cannot use rotateY() on a PMatrix2D.");
    }

    public void rotateZ(float f)
    {
        rotate(f);
    }

    public void scale(float f)
    {
        scale(f, f);
    }

    public void scale(float f, float f1)
    {
        m00 = m00 * f;
        m01 = m01 * f1;
        m10 = m10 * f;
        m11 = m11 * f1;
    }

    public void scale(float f, float f1, float f2)
    {
        throw new IllegalArgumentException("Cannot use this version of scale() on a PMatrix2D.");
    }

    public void set(float f, float f1, float f2, float f3, float f4, float f5)
    {
        m00 = f;
        m01 = f1;
        m02 = f2;
        m10 = f3;
        m11 = f4;
        m12 = f5;
    }

    public void set(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8, float f9, float f10, float f11, float f12, float f13, 
            float f14, float f15)
    {
    }

    public void set(PMatrix3D pmatrix3d)
    {
    }

    public void set(PMatrix pmatrix)
    {
        if(pmatrix instanceof PMatrix2D)
        {
            pmatrix = (PMatrix2D)pmatrix;
            set(((PMatrix2D) (pmatrix)).m00, ((PMatrix2D) (pmatrix)).m01, ((PMatrix2D) (pmatrix)).m02, ((PMatrix2D) (pmatrix)).m10, ((PMatrix2D) (pmatrix)).m11, ((PMatrix2D) (pmatrix)).m12);
            return;
        } else
        {
            throw new IllegalArgumentException("PMatrix2D.set() only accepts PMatrix2D objects.");
        }
    }

    public void set(float af[])
    {
        m00 = af[0];
        m01 = af[1];
        m02 = af[2];
        m10 = af[3];
        m11 = af[4];
        m12 = af[5];
    }

    public void shearX(float f)
    {
        apply(1.0F, 0.0F, 1.0F, tan(f), 0.0F, 0.0F);
    }

    public void shearY(float f)
    {
        apply(1.0F, 0.0F, 1.0F, 0.0F, tan(f), 0.0F);
    }

    public void translate(float f, float f1)
    {
        m02 = m00 * f + m01 * f1 + m02;
        m12 = m10 * f + m11 * f1 + m12;
    }

    public void translate(float f, float f1, float f2)
    {
        throw new IllegalArgumentException("Cannot use translate(x, y, z) on a PMatrix2D.");
    }

    public void transpose()
    {
    }

    public float m00;
    public float m01;
    public float m02;
    public float m10;
    public float m11;
    public float m12;
}
