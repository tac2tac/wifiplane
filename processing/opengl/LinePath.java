// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.opengl;

import processing.core.PMatrix2D;

// Referenced classes of package processing.opengl:
//            LineStroker

public class LinePath
{
    public static class PathIterator
    {

        public int currentSegment(double ad[])
        {
            byte byte0 = path.pointTypes[typeIdx];
            int i = curvecoords[byte0];
            if(i > 0)
            {
                for(int j = 0; j < i; j++)
                    ad[j] = floatCoords[pointIdx + j];

                int k = path.pointColors[colorIdx];
                ad[i + 0] = k >> 24 & 0xff;
                ad[i + 1] = k >> 16 & 0xff;
                ad[i + 2] = k >> 8 & 0xff;
                ad[i + 3] = k >> 0 & 0xff;
            }
            return byte0;
        }

        public int currentSegment(float af[])
        {
            byte byte0 = path.pointTypes[typeIdx];
            int i = curvecoords[byte0];
            if(i > 0)
            {
                System.arraycopy(floatCoords, pointIdx, af, 0, i);
                int j = path.pointColors[colorIdx];
                af[i + 0] = j >> 24 & 0xff;
                af[i + 1] = j >> 16 & 0xff;
                af[i + 2] = j >> 8 & 0xff;
                af[i + 3] = j >> 0 & 0xff;
            }
            return byte0;
        }

        public int getWindingRule()
        {
            return path.getWindingRule();
        }

        public boolean isDone()
        {
            boolean flag;
            if(typeIdx >= path.numTypes)
                flag = true;
            else
                flag = false;
            return flag;
        }

        public void next()
        {
            byte abyte0[] = path.pointTypes;
            int i = typeIdx;
            typeIdx = i + 1;
            i = abyte0[i];
            if(curvecoords[i] > 0)
            {
                int j = pointIdx;
                pointIdx = curvecoords[i] + j;
                colorIdx = colorIdx + 1;
            }
        }

        static final int curvecoords[] = {
            2, 2, 0
        };
        int colorIdx;
        float floatCoords[];
        LinePath path;
        int pointIdx;
        int typeIdx;


        PathIterator(LinePath linepath)
        {
            path = linepath;
            floatCoords = linepath.floatCoords;
            pointIdx = 0;
            colorIdx = 0;
        }
    }


    public LinePath()
    {
        this(1, 20);
    }

    public LinePath(int i)
    {
        this(i, 20);
    }

    public LinePath(int i, int j)
    {
        setWindingRule(i);
        pointTypes = new byte[j];
        floatCoords = new float[j * 2];
        pointColors = new int[j];
    }

    static int FloatToS15_16(float f)
    {
        f = 65536F * f + 0.5F;
        int i;
        if(f <= -4.294967E+009F)
            i = 0x80000000;
        else
        if(f >= 4.294967E+009F)
            i = 0x7fffffff;
        else
            i = (int)Math.floor(f);
        return i;
    }

    static float S15_16ToFloat(int i)
    {
        return (float)i / 65536F;
    }

    public static byte[] copyOf(byte abyte0[], int i)
    {
        byte abyte1[] = new byte[i];
        i = 0;
        while(i < abyte1.length) 
        {
            if(i > abyte0.length - 1)
                abyte1[i] = (byte)0;
            else
                abyte1[i] = abyte0[i];
            i++;
        }
        return abyte1;
    }

    public static float[] copyOf(float af[], int i)
    {
        float af1[] = new float[i];
        i = 0;
        while(i < af1.length) 
        {
            if(i > af.length - 1)
                af1[i] = 0.0F;
            else
                af1[i] = af[i];
            i++;
        }
        return af1;
    }

    public static int[] copyOf(int ai[], int i)
    {
        int ai1[] = new int[i];
        i = 0;
        while(i < ai1.length) 
        {
            if(i > ai.length - 1)
                ai1[i] = 0;
            else
                ai1[i] = ai[i];
            i++;
        }
        return ai1;
    }

    public static LinePath createStrokedPath(LinePath linepath, float f, int i, int j)
    {
        return createStrokedPath(linepath, f, i, j, defaultMiterlimit, null);
    }

    public static LinePath createStrokedPath(LinePath linepath, float f, int i, int j, float f1)
    {
        return createStrokedPath(linepath, f, i, j, f1, null);
    }

    public static LinePath createStrokedPath(LinePath linepath, float f, int i, int j, float f1, PMatrix2D pmatrix2d)
    {
        LinePath linepath1 = new LinePath();
        strokeTo(linepath, f, i, j, f1, pmatrix2d, new LineStroker(linepath1) {

            public void close()
            {
                dest.closePath();
            }

            public void end()
            {
            }

            public void lineJoin()
            {
            }

            public void lineTo(int k, int l, int i1)
            {
                dest.lineTo(LinePath.S15_16ToFloat(k), LinePath.S15_16ToFloat(l), i1);
            }

            public void moveTo(int k, int l, int i1)
            {
                dest.moveTo(LinePath.S15_16ToFloat(k), LinePath.S15_16ToFloat(l), i1);
            }

            final LinePath val$dest;

            
            {
                dest = linepath;
                super();
            }
        }
);
        return linepath1;
    }

    public static double hypot(double d, double d1)
    {
        return Math.sqrt(d * d + d1 * d1);
    }

    public static int hypot(int i, int j)
    {
        return (int)(lsqrt((long)i * (long)i + (long)j * (long)j) + 128L >> 8);
    }

    public static long hypot(long l, long l1)
    {
        return lsqrt(l * l + l1 * l1) + 128L >> 8;
    }

    public static int isqrt(int i)
    {
        int j = 0;
        int k = 23;
        int l = 0;
        do
        {
            l = l << 2 | i >>> 30;
            int i1 = i << 2;
            int j1 = j << 1;
            int k1 = (j1 << 1) + 1;
            j = l;
            i = j1;
            if(l >= k1)
            {
                j = l - k1;
                i = j1 + 1;
            }
            if(k == 0)
                return i;
            k--;
            l = j;
            j = i;
            i = i1;
        } while(true);
    }

    public static long lsqrt(long l)
    {
        long l1 = 0L;
        int i = 39;
        long l2 = 0L;
        do
        {
            l2 = l2 << 2 | l >>> 62;
            long l3 = l << 2;
            long l4 = l1 << 1;
            long l5 = (l4 << 1) + 1L;
            l1 = l2;
            l = l4;
            if(l2 >= l5)
            {
                l1 = l2 - l5;
                l = l4 + 1L;
            }
            if(i == 0)
                return l;
            i--;
            l2 = l1;
            l1 = l;
            l = l3;
        } while(true);
    }

    private static void pathTo(PathIterator pathiterator, LineStroker linestroker)
    {
        float af[] = new float[6];
_L2:
        if(pathiterator.isDone())
            break MISSING_BLOCK_LABEL_195;
        switch(pathiterator.currentSegment(af))
        {
        default:
            throw new InternalError("unknown flattened segment type");

        case 1: // '\001'
            break; /* Loop/switch isn't completed */

        case 2: // '\002'
            break MISSING_BLOCK_LABEL_184;

        case 0: // '\0'
            int i = (int)af[2];
            int k = (int)af[3];
            int i1 = (int)af[4];
            int k1 = (int)af[5];
            linestroker.moveTo(FloatToS15_16(af[0]), FloatToS15_16(af[1]), i << 24 | k << 16 | i1 << 8 | k1);
            break;
        }
_L3:
        pathiterator.next();
        if(true) goto _L2; else goto _L1
_L1:
        int j = (int)af[2];
        int l1 = (int)af[3];
        int j1 = (int)af[4];
        int l = (int)af[5];
        linestroker.lineJoin();
        linestroker.lineTo(FloatToS15_16(af[0]), FloatToS15_16(af[1]), j << 24 | l1 << 16 | j1 << 8 | l);
          goto _L3
        linestroker.lineJoin();
        linestroker.close();
          goto _L3
        linestroker.end();
        return;
    }

    private static void strokeTo(LinePath linepath, float f, int i, int j, float f1, PMatrix2D pmatrix2d, LineStroker linestroker)
    {
        int k = FloatToS15_16(f);
        int l = FloatToS15_16(f1);
        if(pmatrix2d == null)
            pmatrix2d = identity;
        pmatrix2d = new LineStroker(linestroker, k, i, j, l, pmatrix2d);
        pathTo(linepath.getPathIterator(), pmatrix2d);
    }

    public final void closePath()
    {
        if(numTypes == 0 || pointTypes[numTypes - 1] != 2)
        {
            needRoom(false, 0);
            byte abyte0[] = pointTypes;
            int i = numTypes;
            numTypes = i + 1;
            abyte0[i] = (byte)2;
        }
    }

    public PathIterator getPathIterator()
    {
        return new PathIterator(this);
    }

    public final int getWindingRule()
    {
        return windingRule;
    }

    public final void lineTo(float f, float f1, int i)
    {
        needRoom(true, 1);
        float af[] = pointTypes;
        int j = numTypes;
        numTypes = j + 1;
        af[j] = (byte)1;
        af = floatCoords;
        j = numCoords;
        numCoords = j + 1;
        af[j] = f;
        af = floatCoords;
        j = numCoords;
        numCoords = j + 1;
        af[j] = f1;
        pointColors[numCoords / 2 - 1] = i;
    }

    public final void moveTo(float f, float f1, int i)
    {
        if(numTypes > 0 && pointTypes[numTypes - 1] == 0)
        {
            floatCoords[numCoords - 2] = f;
            floatCoords[numCoords - 1] = f1;
            pointColors[numCoords / 2 - 1] = i;
        } else
        {
            needRoom(false, 1);
            float af[] = pointTypes;
            int j = numTypes;
            numTypes = j + 1;
            af[j] = (byte)0;
            af = floatCoords;
            j = numCoords;
            numCoords = j + 1;
            af[j] = f;
            af = floatCoords;
            j = numCoords;
            numCoords = j + 1;
            af[j] = f1;
            pointColors[numCoords / 2 - 1] = i;
        }
    }

    void needRoom(boolean flag, int i)
    {
        if(flag && numTypes == 0)
            throw new RuntimeException("missing initial moveto in path definition");
        int j = pointTypes.length;
        if(numTypes >= j)
        {
            int k;
            int l;
            if(j > 500)
                k = 500;
            else
                k = j;
            pointTypes = copyOf(pointTypes, k + j);
        }
        l = floatCoords.length;
        if(numCoords + i * 2 > l)
        {
            if(l > 1000)
                k = 1000;
            else
                k = l;
            j = k;
            if(k < i * 2)
                j = i * 2;
            floatCoords = copyOf(floatCoords, j + l);
        }
        j = pointColors.length;
        if(numCoords / 2 + i > j)
        {
            if(j > 500)
                k = 500;
            else
                k = j;
            if(k >= i)
                i = k;
            pointColors = copyOf(pointColors, j + i);
        }
    }

    public final void reset()
    {
        numCoords = 0;
        numTypes = 0;
    }

    public final void setWindingRule(int i)
    {
        if(i != 0 && i != 1)
        {
            throw new IllegalArgumentException("winding rule must be WIND_EVEN_ODD or WIND_NON_ZERO");
        } else
        {
            windingRule = i;
            return;
        }
    }

    public static final int CAP_BUTT = 0;
    public static final int CAP_ROUND = 1;
    public static final int CAP_SQUARE = 2;
    static final int EXPAND_MAX = 500;
    static final int INIT_SIZE = 20;
    public static final int JOIN_BEVEL = 2;
    public static final int JOIN_MITER = 0;
    public static final int JOIN_ROUND = 1;
    public static final byte SEG_CLOSE = 2;
    public static final byte SEG_LINETO = 1;
    public static final byte SEG_MOVETO = 0;
    public static final int WIND_EVEN_ODD = 0;
    public static final int WIND_NON_ZERO = 1;
    private static float defaultMiterlimit = 10F;
    private static PMatrix2D identity = new PMatrix2D();
    protected float floatCoords[];
    protected int numCoords;
    protected int numTypes;
    protected int pointColors[];
    protected byte pointTypes[];
    protected int windingRule;

}
