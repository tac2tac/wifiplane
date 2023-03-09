// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.opengl.tess;


// Referenced classes of package processing.opengl.tess:
//            GLUtessellatorImpl, GLUmesh, GLUface, GLUhalfEdge, 
//            GLUvertex

class Normal
{

    private Normal()
    {
    }

    static void CheckOrientation(GLUtessellatorImpl glutessellatorimpl)
    {
        GLUface gluface = glutessellatorimpl.mesh.fHead;
        GLUvertex gluvertex = glutessellatorimpl.mesh.vHead;
        double d = 0.0D;
        GLUface gluface1 = gluface.next;
        while(gluface1 != gluface) 
        {
            GLUhalfEdge gluhalfedge = gluface1.anEdge;
            GLUhalfEdge gluhalfedge1 = gluhalfedge;
            double d1 = d;
            if(gluhalfedge.winding > 0)
                do
                {
                    d = d1 + (gluhalfedge1.Org.s - gluhalfedge1.Sym.Org.s) * (gluhalfedge1.Org.t + gluhalfedge1.Sym.Org.t);
                    gluhalfedge = gluhalfedge1.Lnext;
                    gluhalfedge1 = gluhalfedge;
                    d1 = d;
                } while(gluhalfedge != gluface1.anEdge);
            gluface1 = gluface1.next;
        }
        if(d < 0.0D)
        {
            for(GLUvertex gluvertex1 = gluvertex.next; gluvertex1 != gluvertex; gluvertex1 = gluvertex1.next)
                gluvertex1.t = -gluvertex1.t;

            glutessellatorimpl.tUnit[0] = -glutessellatorimpl.tUnit[0];
            glutessellatorimpl.tUnit[1] = -glutessellatorimpl.tUnit[1];
            glutessellatorimpl.tUnit[2] = -glutessellatorimpl.tUnit[2];
        }
    }

    static void ComputeNormal(GLUtessellatorImpl glutessellatorimpl, double ad[])
    {
        GLUvertex gluvertex;
        double ad1[];
        double ad2[];
        GLUvertex agluvertex[];
        GLUvertex agluvertex1[];
        double ad3[];
        double ad4[];
        double ad5[];
        int k;
        gluvertex = glutessellatorimpl.mesh.vHead;
        ad1 = new double[3];
        ad2 = new double[3];
        agluvertex = new GLUvertex[3];
        agluvertex1 = new GLUvertex[3];
        ad3 = new double[3];
        ad4 = new double[3];
        ad5 = new double[3];
        ad1[2] = -2E+150D;
        ad1[1] = -2E+150D;
        ad1[0] = -2E+150D;
        ad2[2] = 2E+150D;
        ad2[1] = 2E+150D;
        ad2[0] = 2E+150D;
        for(glutessellatorimpl = gluvertex.next; glutessellatorimpl != gluvertex; glutessellatorimpl = ((GLUvertex) (glutessellatorimpl)).next)
        {
            for(int i = 0; i < 3; i++)
            {
                double d = ((GLUvertex) (glutessellatorimpl)).coords[i];
                if(d < ad2[i])
                {
                    ad2[i] = d;
                    agluvertex[i] = glutessellatorimpl;
                }
                if(d > ad1[i])
                {
                    ad1[i] = d;
                    agluvertex1[i] = glutessellatorimpl;
                }
            }

        }

        int j = 0;
        if(ad1[1] - ad2[1] > ad1[0] - ad2[0])
            j = 1;
        k = j;
        if(ad1[2] - ad2[2] > ad1[j] - ad2[j])
            k = 2;
        if(ad2[k] < ad1[k]) goto _L2; else goto _L1
_L1:
        ad[0] = 0.0D;
        ad[1] = 0.0D;
        ad[2] = 1.0D;
_L4:
        return;
_L2:
        double d1 = 0.0D;
        glutessellatorimpl = agluvertex[k];
        GLUvertex gluvertex1 = agluvertex1[k];
        ad3[0] = ((GLUvertex) (glutessellatorimpl)).coords[0] - gluvertex1.coords[0];
        ad3[1] = ((GLUvertex) (glutessellatorimpl)).coords[1] - gluvertex1.coords[1];
        ad3[2] = ((GLUvertex) (glutessellatorimpl)).coords[2] - gluvertex1.coords[2];
        for(glutessellatorimpl = gluvertex.next; glutessellatorimpl != gluvertex; glutessellatorimpl = ((GLUvertex) (glutessellatorimpl)).next)
        {
            ad4[0] = ((GLUvertex) (glutessellatorimpl)).coords[0] - gluvertex1.coords[0];
            ad4[1] = ((GLUvertex) (glutessellatorimpl)).coords[1] - gluvertex1.coords[1];
            ad4[2] = ((GLUvertex) (glutessellatorimpl)).coords[2] - gluvertex1.coords[2];
            ad5[0] = ad3[1] * ad4[2] - ad3[2] * ad4[1];
            ad5[1] = ad3[2] * ad4[0] - ad3[0] * ad4[2];
            ad5[2] = ad3[0] * ad4[1] - ad3[1] * ad4[0];
            double d2 = ad5[0] * ad5[0] + ad5[1] * ad5[1] + ad5[2] * ad5[2];
            if(d2 > d1)
            {
                ad[0] = ad5[0];
                ad[1] = ad5[1];
                ad[2] = ad5[2];
                d1 = d2;
            }
        }

        if(d1 <= 0.0D)
        {
            ad[2] = 0.0D;
            ad[1] = 0.0D;
            ad[0] = 0.0D;
            ad[LongAxis(ad3)] = 1.0D;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    private static double Dot(double ad[], double ad1[])
    {
        return ad[0] * ad1[0] + ad[1] * ad1[1] + ad[2] * ad1[2];
    }

    static int LongAxis(double ad[])
    {
        int i = 1;
        int j;
        if(Math.abs(ad[1]) <= Math.abs(ad[0]))
            i = 0;
        j = i;
        if(Math.abs(ad[2]) > Math.abs(ad[i]))
            j = 2;
        return j;
    }

    static void Normalize(double ad[])
    {
        double d = ad[0] * ad[0] + ad[1] * ad[1] + ad[2] * ad[2];
        if(!$assertionsDisabled && d <= 0.0D)
        {
            throw new AssertionError();
        } else
        {
            d = Math.sqrt(d);
            ad[0] = ad[0] / d;
            ad[1] = ad[1] / d;
            ad[2] = ad[2] / d;
            return;
        }
    }

    public static void __gl_projectPolygon(GLUtessellatorImpl glutessellatorimpl)
    {
        boolean flag = true;
        GLUvertex gluvertex = glutessellatorimpl.mesh.vHead;
        double ad[] = new double[3];
        ad[0] = glutessellatorimpl.normal[0];
        ad[1] = glutessellatorimpl.normal[1];
        ad[2] = glutessellatorimpl.normal[2];
        double ad1[];
        double ad2[];
        int i;
        double d;
        if(ad[0] == 0.0D && ad[1] == 0.0D && ad[2] == 0.0D)
            ComputeNormal(glutessellatorimpl, ad);
        else
            flag = false;
        ad1 = glutessellatorimpl.sUnit;
        ad2 = glutessellatorimpl.tUnit;
        i = LongAxis(ad);
        ad1[i] = 0.0D;
        ad1[(i + 1) % 3] = S_UNIT_X;
        ad1[(i + 2) % 3] = S_UNIT_Y;
        ad2[i] = 0.0D;
        if(ad[i] > 0.0D)
            d = -S_UNIT_Y;
        else
            d = S_UNIT_Y;
        ad2[(i + 1) % 3] = d;
        if(ad[i] > 0.0D)
            d = S_UNIT_X;
        else
            d = -S_UNIT_X;
        ad2[(i + 2) % 3] = d;
        for(GLUvertex gluvertex1 = gluvertex.next; gluvertex1 != gluvertex; gluvertex1 = gluvertex1.next)
        {
            gluvertex1.s = Dot(gluvertex1.coords, ad1);
            gluvertex1.t = Dot(gluvertex1.coords, ad2);
        }

        if(flag)
            CheckOrientation(glutessellatorimpl);
    }

    static final boolean $assertionsDisabled;
    static boolean SLANTED_SWEEP;
    static double S_UNIT_X;
    static double S_UNIT_Y;
    private static final boolean TRUE_PROJECT = false;

    static 
    {
        boolean flag;
        if(!processing/opengl/tess/Normal.desiredAssertionStatus())
            flag = true;
        else
            flag = false;
        $assertionsDisabled = flag;
        SLANTED_SWEEP = false;
        if(SLANTED_SWEEP)
        {
            S_UNIT_X = 0.50941539564955385D;
            S_UNIT_Y = 0.86052074622010633D;
        } else
        {
            S_UNIT_X = 1.0D;
            S_UNIT_Y = 0.0D;
        }
    }
}
