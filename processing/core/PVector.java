// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.core;

import java.io.Serializable;

// Referenced classes of package processing.core:
//            PApplet

public class PVector
    implements Serializable
{

    public PVector()
    {
    }

    public PVector(float f, float f1)
    {
        x = f;
        y = f1;
        z = 0.0F;
    }

    public PVector(float f, float f1, float f2)
    {
        x = f;
        y = f1;
        z = f2;
    }

    public static PVector add(PVector pvector, PVector pvector1)
    {
        return add(pvector, pvector1, ((PVector) (null)));
    }

    public static PVector add(PVector pvector, PVector pvector1, PVector pvector2)
    {
        if(pvector2 == null)
            pvector2 = new PVector(pvector.x + pvector1.x, pvector.y + pvector1.y, pvector.z + pvector1.z);
        else
            pvector2.set(pvector.x + pvector1.x, pvector.y + pvector1.y, pvector.z + pvector1.z);
        return pvector2;
    }

    public static float angleBetween(PVector pvector, PVector pvector1)
    {
        float f = 0.0F;
        if(pvector.x != 0.0F || pvector.y != 0.0F) goto _L2; else goto _L1
_L1:
        float f1 = f;
_L4:
        return f1;
_L2:
        if(pvector1.x == 0.0F)
        {
            f1 = f;
            if(pvector1.y == 0.0F)
                continue; /* Loop/switch isn't completed */
        }
        double d = (double)(pvector.x * pvector1.x + pvector.y * pvector1.y + pvector.z * pvector1.z) / (Math.sqrt(pvector.x * pvector.x + pvector.y * pvector.y + pvector.z * pvector.z) * Math.sqrt(pvector1.x * pvector1.x + pvector1.y * pvector1.y + pvector1.z * pvector1.z));
        if(d <= -1D)
        {
            f1 = 3.141593F;
        } else
        {
            f1 = f;
            if(d < 1.0D)
                f1 = (float)Math.acos(d);
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public static PVector cross(PVector pvector, PVector pvector1, PVector pvector2)
    {
        float f = pvector.y * pvector1.z - pvector1.y * pvector.z;
        float f1 = pvector.z * pvector1.x - pvector1.z * pvector.x;
        float f2 = pvector.x * pvector1.y - pvector1.x * pvector.y;
        if(pvector2 == null)
            pvector2 = new PVector(f, f1, f2);
        else
            pvector2.set(f, f1, f2);
        return pvector2;
    }

    public static float dist(PVector pvector, PVector pvector1)
    {
        float f = pvector.x - pvector1.x;
        float f1 = pvector.y - pvector1.y;
        float f2 = pvector.z - pvector1.z;
        return (float)Math.sqrt(f * f + f1 * f1 + f2 * f2);
    }

    public static PVector div(PVector pvector, float f)
    {
        return div(pvector, f, null);
    }

    public static PVector div(PVector pvector, float f, PVector pvector1)
    {
        if(pvector1 == null)
            pvector1 = new PVector(pvector.x / f, pvector.y / f, pvector.z / f);
        else
            pvector1.set(pvector.x / f, pvector.y / f, pvector.z / f);
        return pvector1;
    }

    public static float dot(PVector pvector, PVector pvector1)
    {
        return pvector.x * pvector1.x + pvector.y * pvector1.y + pvector.z * pvector1.z;
    }

    public static PVector fromAngle(float f)
    {
        return fromAngle(f, null);
    }

    public static PVector fromAngle(float f, PVector pvector)
    {
        if(pvector == null)
            pvector = new PVector((float)Math.cos(f), (float)Math.sin(f), 0.0F);
        else
            pvector.set((float)Math.cos(f), (float)Math.sin(f), 0.0F);
        return pvector;
    }

    public static PVector lerp(PVector pvector, PVector pvector1, float f)
    {
        pvector = pvector.get();
        pvector.lerp(pvector1, f);
        return pvector;
    }

    public static PVector mult(PVector pvector, float f)
    {
        return mult(pvector, f, null);
    }

    public static PVector mult(PVector pvector, float f, PVector pvector1)
    {
        if(pvector1 == null)
            pvector1 = new PVector(pvector.x * f, pvector.y * f, pvector.z * f);
        else
            pvector1.set(pvector.x * f, pvector.y * f, pvector.z * f);
        return pvector1;
    }

    public static PVector random2D()
    {
        return random2D(null, null);
    }

    public static PVector random2D(PApplet papplet)
    {
        return random2D(null, papplet);
    }

    public static PVector random2D(PVector pvector)
    {
        return random2D(pvector, null);
    }

    public static PVector random2D(PVector pvector, PApplet papplet)
    {
        if(papplet == null)
            pvector = fromAngle((float)(Math.random() * 3.1415926535897931D * 2D), pvector);
        else
            pvector = fromAngle(papplet.random(6.283185F), pvector);
        return pvector;
    }

    public static PVector random3D()
    {
        return random3D(null, null);
    }

    public static PVector random3D(PApplet papplet)
    {
        return random3D(null, papplet);
    }

    public static PVector random3D(PVector pvector)
    {
        return random3D(pvector, null);
    }

    public static PVector random3D(PVector pvector, PApplet papplet)
    {
        float f;
        float f1;
        float f2;
        if(papplet == null)
        {
            f = (float)(Math.random() * 3.1415926535897931D * 2D);
            f1 = (float)(Math.random() * 2D - 1.0D);
        } else
        {
            f = papplet.random(6.283185F);
            f1 = papplet.random(-1F, 1.0F);
        }
        f2 = (float)(Math.sqrt(1.0F - f1 * f1) * Math.cos(f));
        f = (float)(Math.sqrt(1.0F - f1 * f1) * Math.sin(f));
        if(pvector == null)
            pvector = new PVector(f2, f, f1);
        else
            pvector.set(f2, f, f1);
        return pvector;
    }

    public static PVector sub(PVector pvector, PVector pvector1)
    {
        return sub(pvector, pvector1, ((PVector) (null)));
    }

    public static PVector sub(PVector pvector, PVector pvector1, PVector pvector2)
    {
        if(pvector2 == null)
            pvector2 = new PVector(pvector.x - pvector1.x, pvector.y - pvector1.y, pvector.z - pvector1.z);
        else
            pvector2.set(pvector.x - pvector1.x, pvector.y - pvector1.y, pvector.z - pvector1.z);
        return pvector2;
    }

    public void add(float f, float f1, float f2)
    {
        x = x + f;
        y = y + f1;
        z = z + f2;
    }

    public void add(PVector pvector)
    {
        x = x + pvector.x;
        y = y + pvector.y;
        z = z + pvector.z;
    }

    public float[] array()
    {
        if(array == null)
            array = new float[3];
        array[0] = x;
        array[1] = y;
        array[2] = z;
        return array;
    }

    public PVector cross(PVector pvector)
    {
        return cross(pvector, null);
    }

    public PVector cross(PVector pvector, PVector pvector1)
    {
        float f = y * pvector.z - pvector.y * z;
        float f1 = z * pvector.x - pvector.z * x;
        float f2 = x * pvector.y - pvector.x * y;
        if(pvector1 == null)
            pvector1 = new PVector(f, f1, f2);
        else
            pvector1.set(f, f1, f2);
        return pvector1;
    }

    public float dist(PVector pvector)
    {
        float f = x - pvector.x;
        float f1 = y - pvector.y;
        float f2 = z - pvector.z;
        return (float)Math.sqrt(f * f + f1 * f1 + f2 * f2);
    }

    public void div(float f)
    {
        x = x / f;
        y = y / f;
        z = z / f;
    }

    public float dot(float f, float f1, float f2)
    {
        return x * f + y * f1 + z * f2;
    }

    public float dot(PVector pvector)
    {
        return x * pvector.x + y * pvector.y + z * pvector.z;
    }

    public boolean equals(Object obj)
    {
        boolean flag = false;
        if(obj instanceof PVector) goto _L2; else goto _L1
_L1:
        boolean flag1 = flag;
_L4:
        return flag1;
_L2:
        obj = (PVector)obj;
        flag1 = flag;
        if(x == ((PVector) (obj)).x)
        {
            flag1 = flag;
            if(y == ((PVector) (obj)).y)
            {
                flag1 = flag;
                if(z == ((PVector) (obj)).z)
                    flag1 = true;
            }
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public PVector get()
    {
        return new PVector(x, y, z);
    }

    public float[] get(float af[])
    {
        if(af != null) goto _L2; else goto _L1
_L1:
        float af1[];
        af1 = new float[3];
        af1[0] = x;
        af1[1] = y;
        af1[2] = z;
_L4:
        return af1;
_L2:
        if(af.length >= 2)
        {
            af[0] = x;
            af[1] = y;
        }
        af1 = af;
        if(af.length >= 3)
        {
            af[2] = z;
            af1 = af;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public int hashCode()
    {
        return ((Float.floatToIntBits(x) + 31) * 31 + Float.floatToIntBits(y)) * 31 + Float.floatToIntBits(z);
    }

    public float heading()
    {
        return (float)Math.atan2(-y, x) * -1F;
    }

    public float heading2D()
    {
        return heading();
    }

    public void lerp(float f, float f1, float f2, float f3)
    {
        x = PApplet.lerp(x, f, f3);
        y = PApplet.lerp(y, f1, f3);
        z = PApplet.lerp(z, f2, f3);
    }

    public void lerp(PVector pvector, float f)
    {
        x = PApplet.lerp(x, pvector.x, f);
        y = PApplet.lerp(y, pvector.y, f);
        z = PApplet.lerp(z, pvector.z, f);
    }

    public void limit(float f)
    {
        if(magSq() > f * f)
        {
            normalize();
            mult(f);
        }
    }

    public float mag()
    {
        return (float)Math.sqrt(x * x + y * y + z * z);
    }

    public float magSq()
    {
        return x * x + y * y + z * z;
    }

    public void mult(float f)
    {
        x = x * f;
        y = y * f;
        z = z * f;
    }

    public PVector normalize(PVector pvector)
    {
        PVector pvector1 = pvector;
        if(pvector == null)
            pvector1 = new PVector();
        float f = mag();
        if(f > 0.0F)
            pvector1.set(x / f, y / f, z / f);
        else
            pvector1.set(x, y, z);
        return pvector1;
    }

    public void normalize()
    {
        float f = mag();
        if(f != 0.0F && f != 1.0F)
            div(f);
    }

    public void rotate(float f)
    {
        float f1 = x;
        x = x * PApplet.cos(f) - y * PApplet.sin(f);
        y = f1 * PApplet.sin(f) + y * PApplet.cos(f);
    }

    public void set(float f, float f1)
    {
        x = f;
        y = f1;
    }

    public void set(float f, float f1, float f2)
    {
        x = f;
        y = f1;
        z = f2;
    }

    public void set(PVector pvector)
    {
        x = pvector.x;
        y = pvector.y;
        z = pvector.z;
    }

    public void set(float af[])
    {
        if(af.length >= 2)
        {
            x = af[0];
            y = af[1];
        }
        if(af.length >= 3)
            z = af[2];
    }

    public PVector setMag(PVector pvector, float f)
    {
        pvector = normalize(pvector);
        pvector.mult(f);
        return pvector;
    }

    public void setMag(float f)
    {
        normalize();
        mult(f);
    }

    public void sub(float f, float f1, float f2)
    {
        x = x - f;
        y = y - f1;
        z = z - f2;
    }

    public void sub(PVector pvector)
    {
        x = x - pvector.x;
        y = y - pvector.y;
        z = z - pvector.z;
    }

    public String toString()
    {
        return (new StringBuilder()).append("[ ").append(x).append(", ").append(y).append(", ").append(z).append(" ]").toString();
    }

    private static final long serialVersionUID = 0xa2c552ee3eb6428aL;
    protected transient float array[];
    public float x;
    public float y;
    public float z;
}
