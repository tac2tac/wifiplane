// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.opengl.tess;


// Referenced classes of package processing.opengl.tess:
//            GLUtessellatorImpl, PGLUtessellator, PGLUtessellatorCallback

public class PGLU
{

    public PGLU()
    {
    }

    public static String gluErrorString(int i)
    {
        String s;
        if(i == 0)
            s = "no error";
        else
        if(i >= 1280 && i <= 1286)
            s = glErrorStrings[i - 1280];
        else
        if(i == 32817)
            s = "table too large";
        else
        if(i >= 0x18a24 && i <= 0x18a28)
            s = gluErrorStrings[i - 0x18a24];
        else
        if(i >= 0x18737 && i <= 0x1873e)
            s = gluTessErrors[i - 0x18736];
        else
            s = (new StringBuilder()).append("error (").append(i).append(")").toString();
        return s;
    }

    public static final PGLUtessellator gluNewTess()
    {
        return GLUtessellatorImpl.gluNewTess();
    }

    public static final void gluTessBeginContour(PGLUtessellator pglutessellator)
    {
        ((GLUtessellatorImpl)pglutessellator).gluTessBeginContour();
    }

    public static final void gluTessBeginPolygon(PGLUtessellator pglutessellator, Object obj)
    {
        ((GLUtessellatorImpl)pglutessellator).gluTessBeginPolygon(obj);
    }

    public static final void gluTessCallback(PGLUtessellator pglutessellator, int i, PGLUtessellatorCallback pglutessellatorcallback)
    {
        ((GLUtessellatorImpl)pglutessellator).gluTessCallback(i, pglutessellatorcallback);
    }

    public static final void gluTessEndContour(PGLUtessellator pglutessellator)
    {
        ((GLUtessellatorImpl)pglutessellator).gluTessEndContour();
    }

    public static final void gluTessEndPolygon(PGLUtessellator pglutessellator)
    {
        ((GLUtessellatorImpl)pglutessellator).gluTessEndPolygon();
    }

    public static final void gluTessProperty(PGLUtessellator pglutessellator, int i, double d)
    {
        ((GLUtessellatorImpl)pglutessellator).gluTessProperty(i, d);
    }

    public static final void gluTessVertex(PGLUtessellator pglutessellator, double ad[], int i, Object obj)
    {
        ((GLUtessellatorImpl)pglutessellator).gluTessVertex(ad, i, obj);
    }

    public static final int GLU_BEGIN = 0x18704;
    public static final int GLU_CCW = 0x18719;
    public static final int GLU_CW = 0x18718;
    public static final int GLU_EDGE_FLAG = 0x18708;
    public static final int GLU_END = 0x18706;
    public static final int GLU_ERROR = 0x18707;
    public static final int GLU_EXTERIOR = 0x1871b;
    public static final int GLU_FALSE = 0;
    public static final int GLU_FILL = 0x186ac;
    public static final int GLU_FLAT = 0x186a1;
    public static final int GLU_INSIDE = 0x186b5;
    public static final int GLU_INTERIOR = 0x1871a;
    public static final int GLU_INVALID_ENUM = 0x18a24;
    public static final int GLU_INVALID_OPERATION = 0x18a28;
    public static final int GLU_INVALID_VALUE = 0x18a25;
    public static final int GLU_LINE = 0x186ab;
    public static final int GLU_NONE = 0x186a2;
    public static final int GLU_OUTSIDE = 0x186b4;
    public static final int GLU_OUT_OF_MEMORY = 0x18a26;
    public static final int GLU_POINT = 0x186aa;
    public static final int GLU_SILHOUETTE = 0x186ad;
    public static final int GLU_SMOOTH = 0x186a0;
    public static final int GLU_TESS_AVOID_DEGENERATE_TRIANGLES = 0x18735;
    public static final int GLU_TESS_BEGIN = 0x18704;
    public static final int GLU_TESS_BEGIN_DATA = 0x1870a;
    public static final int GLU_TESS_BOUNDARY_ONLY = 0x1872d;
    public static final int GLU_TESS_COMBINE = 0x18709;
    public static final int GLU_TESS_COMBINE_DATA = 0x1870f;
    public static final int GLU_TESS_COORD_TOO_LARGE = 0x1873b;
    public static final int GLU_TESS_EDGE_FLAG = 0x18708;
    public static final int GLU_TESS_EDGE_FLAG_DATA = 0x1870e;
    public static final int GLU_TESS_END = 0x18706;
    public static final int GLU_TESS_END_DATA = 0x1870c;
    public static final int GLU_TESS_ERROR = 0x18707;
    public static final int GLU_TESS_ERROR1 = 0x18737;
    public static final int GLU_TESS_ERROR2 = 0x18738;
    public static final int GLU_TESS_ERROR3 = 0x18739;
    public static final int GLU_TESS_ERROR4 = 0x1873a;
    public static final int GLU_TESS_ERROR5 = 0x1873b;
    public static final int GLU_TESS_ERROR6 = 0x1873c;
    public static final int GLU_TESS_ERROR7 = 0x1873d;
    public static final int GLU_TESS_ERROR8 = 0x1873e;
    public static final int GLU_TESS_ERROR_DATA = 0x1870d;
    public static final double GLU_TESS_MAX_COORD = 9.9999999999999998E+149D;
    public static final int GLU_TESS_MISSING_BEGIN_CONTOUR = 0x18738;
    public static final int GLU_TESS_MISSING_BEGIN_POLYGON = 0x18737;
    public static final int GLU_TESS_MISSING_END_CONTOUR = 0x1873a;
    public static final int GLU_TESS_MISSING_END_POLYGON = 0x18739;
    public static final int GLU_TESS_NEED_COMBINE_CALLBACK = 0x1873c;
    public static final int GLU_TESS_TOLERANCE = 0x1872e;
    public static final int GLU_TESS_VERTEX = 0x18705;
    public static final int GLU_TESS_VERTEX_DATA = 0x1870b;
    public static final int GLU_TESS_WINDING_ABS_GEQ_TWO = 0x18726;
    public static final int GLU_TESS_WINDING_NEGATIVE = 0x18725;
    public static final int GLU_TESS_WINDING_NONZERO = 0x18723;
    public static final int GLU_TESS_WINDING_ODD = 0x18722;
    public static final int GLU_TESS_WINDING_POSITIVE = 0x18724;
    public static final int GLU_TESS_WINDING_RULE = 0x1872c;
    public static final int GLU_TRUE = 1;
    public static final int GLU_UNKNOWN = 0x1871c;
    public static final int GLU_VERTEX = 0x18705;
    private static String glErrorStrings[] = {
        "invalid enumerant", "invalid value", "invalid operation", "stack overflow", "stack underflow", "out of memory", "invalid framebuffer operation"
    };
    private static String gluErrorStrings[] = {
        "invalid enumerant", "invalid value", "out of memory", "", "invalid operation"
    };
    private static String gluTessErrors[] = {
        " ", "gluTessBeginPolygon() must precede a gluTessEndPolygon", "gluTessBeginContour() must precede a gluTessEndContour()", "gluTessEndPolygon() must follow a gluTessBeginPolygon()", "gluTessEndContour() must follow a gluTessBeginContour()", "a coordinate is too large", "need combine callback"
    };

}
