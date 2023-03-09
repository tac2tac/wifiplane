// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.opengl.tess;


// Referenced classes of package processing.opengl.tess:
//            GLUvertex, GLUhalfEdge

class Geom
{

    private Geom()
    {
    }

    static double EdgeCos(GLUvertex gluvertex, GLUvertex gluvertex1, GLUvertex gluvertex2)
    {
        double d = gluvertex1.s - gluvertex.s;
        double d1 = gluvertex1.t - gluvertex.t;
        double d2 = gluvertex2.s - gluvertex.s;
        double d3 = gluvertex2.t - gluvertex.t;
        double d4 = d * d2 + d1 * d3;
        d = Math.sqrt(d * d + d1 * d1) * Math.sqrt(d2 * d2 + d3 * d3);
        d2 = d4;
        if(d > 0.0D)
            d2 = d4 / d;
        return d2;
    }

    static double EdgeEval(GLUvertex gluvertex, GLUvertex gluvertex1, GLUvertex gluvertex2)
    {
        double d = 0.0D;
        if(!$assertionsDisabled && (!VertLeq(gluvertex, gluvertex1) || !VertLeq(gluvertex1, gluvertex2)))
            throw new AssertionError();
        double d1 = gluvertex1.s - gluvertex.s;
        double d2 = gluvertex2.s - gluvertex1.s;
        if(d1 + d2 > 0.0D)
            if(d1 < d2)
            {
                d = gluvertex1.t;
                double d3 = gluvertex.t;
                double d5 = gluvertex.t;
                double d7 = gluvertex2.t;
                d = (d - d3) + (d1 / (d2 + d1)) * (d5 - d7);
            } else
            {
                double d8 = gluvertex1.t;
                d = gluvertex2.t;
                double d4 = gluvertex2.t;
                double d6 = gluvertex.t;
                d = (d8 - d) + (d2 / (d1 + d2)) * (d4 - d6);
            }
        return d;
    }

    static boolean EdgeGoesLeft(GLUhalfEdge gluhalfedge)
    {
        return VertLeq(gluhalfedge.Sym.Org, gluhalfedge.Org);
    }

    static boolean EdgeGoesRight(GLUhalfEdge gluhalfedge)
    {
        return VertLeq(gluhalfedge.Org, gluhalfedge.Sym.Org);
    }

    static void EdgeIntersect(GLUvertex gluvertex, GLUvertex gluvertex1, GLUvertex gluvertex2, GLUvertex gluvertex3, GLUvertex gluvertex4)
    {
        GLUvertex gluvertex5;
        if(VertLeq(gluvertex, gluvertex1))
        {
            GLUvertex gluvertex7 = gluvertex;
            gluvertex = gluvertex1;
            gluvertex1 = gluvertex7;
        }
        if(VertLeq(gluvertex2, gluvertex3))
        {
            GLUvertex gluvertex6 = gluvertex2;
            gluvertex2 = gluvertex3;
            gluvertex3 = gluvertex6;
        }
        if(!VertLeq(gluvertex1, gluvertex3))
        {
            gluvertex5 = gluvertex2;
            gluvertex2 = gluvertex1;
        } else
        {
            gluvertex5 = gluvertex;
            gluvertex = gluvertex2;
            gluvertex2 = gluvertex3;
            gluvertex3 = gluvertex1;
        }
        if(!VertLeq(gluvertex2, gluvertex5))
            gluvertex4.s = (gluvertex2.s + gluvertex5.s) / 2D;
        else
        if(VertLeq(gluvertex5, gluvertex))
        {
            double d = EdgeEval(gluvertex3, gluvertex2, gluvertex5);
            double d4 = EdgeEval(gluvertex2, gluvertex5, gluvertex);
            double d8 = d;
            double d12 = d4;
            if(d + d4 < 0.0D)
            {
                d8 = -d;
                d12 = -d4;
            }
            gluvertex4.s = Interpolate(d8, gluvertex2.s, d12, gluvertex5.s);
        } else
        {
            double d1 = EdgeSign(gluvertex3, gluvertex2, gluvertex5);
            double d5 = -EdgeSign(gluvertex3, gluvertex, gluvertex5);
            double d9 = d1;
            double d13 = d5;
            if(d1 + d5 < 0.0D)
            {
                d9 = -d1;
                d13 = -d5;
            }
            gluvertex4.s = Interpolate(d9, gluvertex2.s, d13, gluvertex.s);
        }
        if(!TransLeq(gluvertex3, gluvertex5))
        {
            gluvertex1 = gluvertex5;
        } else
        {
            gluvertex1 = gluvertex3;
            gluvertex3 = gluvertex5;
        }
        if(TransLeq(gluvertex2, gluvertex))
        {
            gluvertex5 = gluvertex;
            gluvertex = gluvertex2;
            gluvertex2 = gluvertex5;
        }
        if(!TransLeq(gluvertex1, gluvertex))
        {
            gluvertex5 = gluvertex3;
            gluvertex3 = gluvertex2;
        } else
        {
            gluvertex5 = gluvertex1;
            gluvertex1 = gluvertex;
            gluvertex = gluvertex5;
            gluvertex5 = gluvertex2;
        }
        if(!TransLeq(gluvertex1, gluvertex3))
            gluvertex4.t = (gluvertex1.t + gluvertex3.t) / 2D;
        else
        if(TransLeq(gluvertex3, gluvertex5))
        {
            double d2 = TransEval(gluvertex, gluvertex1, gluvertex3);
            double d6 = TransEval(gluvertex1, gluvertex3, gluvertex5);
            double d10 = d2;
            double d14 = d6;
            if(d2 + d6 < 0.0D)
            {
                d10 = -d2;
                d14 = -d6;
            }
            gluvertex4.t = Interpolate(d10, gluvertex1.t, d14, gluvertex3.t);
        } else
        {
            double d3 = TransSign(gluvertex, gluvertex1, gluvertex3);
            double d7 = -TransSign(gluvertex, gluvertex5, gluvertex3);
            double d11 = d3;
            double d15 = d7;
            if(d3 + d7 < 0.0D)
            {
                d11 = -d3;
                d15 = -d7;
            }
            gluvertex4.t = Interpolate(d11, gluvertex1.t, d15, gluvertex5.t);
        }
    }

    static double EdgeSign(GLUvertex gluvertex, GLUvertex gluvertex1, GLUvertex gluvertex2)
    {
        double d = 0.0D;
        if(!$assertionsDisabled && (!VertLeq(gluvertex, gluvertex1) || !VertLeq(gluvertex1, gluvertex2)))
            throw new AssertionError();
        double d1 = gluvertex1.s - gluvertex.s;
        double d2 = gluvertex2.s - gluvertex1.s;
        if(d1 + d2 > 0.0D)
            d = (gluvertex1.t - gluvertex2.t) * d1 + (gluvertex1.t - gluvertex.t) * d2;
        return d;
    }

    static double Interpolate(double d, double d1, double d2, double d3)
    {
        double d4 = d;
        if(d < 0.0D)
            d4 = 0.0D;
        d = d2;
        if(d2 < 0.0D)
            d = 0.0D;
        if(d4 <= d)
        {
            if(d == 0.0D)
                d = (d1 + d3) / 2D;
            else
                d = (d3 - d1) * (d4 / (d4 + d)) + d1;
        } else
        {
            d = (d1 - d3) * (d / (d4 + d)) + d3;
        }
        return d;
    }

    static double TransEval(GLUvertex gluvertex, GLUvertex gluvertex1, GLUvertex gluvertex2)
    {
        double d = 0.0D;
        if(!$assertionsDisabled && (!TransLeq(gluvertex, gluvertex1) || !TransLeq(gluvertex1, gluvertex2)))
            throw new AssertionError();
        double d1 = gluvertex1.t - gluvertex.t;
        double d2 = gluvertex2.t - gluvertex1.t;
        if(d1 + d2 > 0.0D)
            if(d1 < d2)
            {
                double d3 = gluvertex1.s;
                double d5 = gluvertex.s;
                double d7 = gluvertex.s;
                d = gluvertex2.s;
                d = (d3 - d5) + (d1 / (d2 + d1)) * (d7 - d);
            } else
            {
                double d6 = gluvertex1.s;
                double d4 = gluvertex2.s;
                d = gluvertex2.s;
                double d8 = gluvertex.s;
                d = (d6 - d4) + (d2 / (d1 + d2)) * (d - d8);
            }
        return d;
    }

    static boolean TransLeq(GLUvertex gluvertex, GLUvertex gluvertex1)
    {
        boolean flag;
        if(gluvertex.t < gluvertex1.t || gluvertex.t == gluvertex1.t && gluvertex.s <= gluvertex1.s)
            flag = true;
        else
            flag = false;
        return flag;
    }

    static double TransSign(GLUvertex gluvertex, GLUvertex gluvertex1, GLUvertex gluvertex2)
    {
        double d = 0.0D;
        if(!$assertionsDisabled && (!TransLeq(gluvertex, gluvertex1) || !TransLeq(gluvertex1, gluvertex2)))
            throw new AssertionError();
        double d1 = gluvertex1.t - gluvertex.t;
        double d2 = gluvertex2.t - gluvertex1.t;
        if(d1 + d2 > 0.0D)
            d = (gluvertex1.s - gluvertex2.s) * d1 + (gluvertex1.s - gluvertex.s) * d2;
        return d;
    }

    static boolean VertCCW(GLUvertex gluvertex, GLUvertex gluvertex1, GLUvertex gluvertex2)
    {
        boolean flag;
        if(gluvertex.s * (gluvertex1.t - gluvertex2.t) + gluvertex1.s * (gluvertex2.t - gluvertex.t) + gluvertex2.s * (gluvertex.t - gluvertex1.t) >= 0.0D)
            flag = true;
        else
            flag = false;
        return flag;
    }

    static boolean VertEq(GLUvertex gluvertex, GLUvertex gluvertex1)
    {
        boolean flag;
        if(gluvertex.s == gluvertex1.s && gluvertex.t == gluvertex1.t)
            flag = true;
        else
            flag = false;
        return flag;
    }

    static double VertL1dist(GLUvertex gluvertex, GLUvertex gluvertex1)
    {
        return Math.abs(gluvertex.s - gluvertex1.s) + Math.abs(gluvertex.t - gluvertex1.t);
    }

    static boolean VertLeq(GLUvertex gluvertex, GLUvertex gluvertex1)
    {
        boolean flag;
        if(gluvertex.s < gluvertex1.s || gluvertex.s == gluvertex1.s && gluvertex.t <= gluvertex1.t)
            flag = true;
        else
            flag = false;
        return flag;
    }

    static final boolean $assertionsDisabled;
    static final double EPSILON = 1.0000000000000001E-005D;
    static final double ONE_MINUS_EPSILON = 0.99999000000000005D;

    static 
    {
        boolean flag;
        if(!processing/opengl/tess/Geom.desiredAssertionStatus())
            flag = true;
        else
            flag = false;
        $assertionsDisabled = flag;
    }
}
