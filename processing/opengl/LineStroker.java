// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.opengl;

import processing.core.PMatrix2D;

// Referenced classes of package processing.opengl:
//            LinePath

public class LineStroker
{

    public LineStroker()
    {
        offset = new int[2];
        reverse = new int[100];
        miter = new int[2];
        joinSegment = false;
    }

    public LineStroker(LineStroker linestroker, int i, int j, int k, int l, PMatrix2D pmatrix2d)
    {
        offset = new int[2];
        reverse = new int[100];
        miter = new int[2];
        joinSegment = false;
        setOutput(linestroker);
        setParameters(i, j, k, l, pmatrix2d);
    }

    private void computeMiter(int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1, int ai[])
    {
        long l2 = i;
        long l3 = j;
        long l4 = k;
        long l5 = l;
        long l6 = i1;
        long l7 = j1;
        long l8 = k1;
        long l9 = l1;
        l4 -= l2;
        long l10 = l5 - l3;
        long l11 = l9 - l7;
        l5 = l4 * l11 - (l8 - l6) * l10 >> 16;
        if(l5 == 0L)
        {
            ai[0] = i;
            ai[1] = j;
        } else
        {
            l8 = l6 * (l9 - l3) + ((l3 - l7) * l8 - l2 * l11) >> 16;
            ai[0] = (int)(l2 + (l4 * l8) / l5);
            ai[1] = (int)(l3 + (l8 * l10) / l5);
        }
    }

    private void computeOffset(int i, int j, int k, int l, int ai[])
    {
        long l1;
        long l2;
        l1 = (long)k - (long)i;
        l2 = (long)l - (long)j;
        if(m00 <= 0 || m00 != m11) goto _L2; else goto _L1
_L1:
        boolean flag;
        boolean flag1;
        if(m01 == 0)
            flag = true;
        else
            flag = false;
        if(m10 == 0)
            flag1 = true;
        else
            flag1 = false;
        if(!(flag & flag1)) goto _L2; else goto _L3
_L3:
        long l3 = LinePath.hypot(l1, l2);
        if(l3 == 0L)
        {
            i = 0;
            j = 0;
        } else
        {
            j = (int)((scaledLineWidth2 * l2) / l3);
            i = (int)(-(l1 * scaledLineWidth2) / l3);
        }
_L5:
        ai[0] = j;
        ai[1] = i;
        return;
_L2:
        double d = k - i;
        double d1 = l - j;
        double d2;
        double d3;
        double d4;
        double d5;
        double d6;
        if((double)m00 * (double)m11 - (double)m01 * (double)m10 > 0.0D)
            i = 1;
        else
            i = -1;
        d2 = LinePath.hypot((double)m00 * d1 - (double)m10 * d, (double)m01 * d1 - (double)m11 * d);
        d3 = (double)(i * lineWidth2) / (d2 * 65536D);
        d4 = m00_2_m01_2;
        d5 = m00_m10_m01_m11;
        d2 = m00_m10_m01_m11;
        d6 = m10_2_m11_2;
        j = (int)((d4 * d1 - d5 * d) * d3);
        i = (int)((d1 * d2 - d * d6) * d3);
        if(true) goto _L5; else goto _L4
_L4:
    }

    private int computeRoundJoin(int i, int j, int k, int l, int i1, int j1, int k1, 
            boolean flag, int ai[])
    {
        int l1 = 0;
        if(k1 == 0)
            flag = side(i, j, k, l, i1, j1);
        else
        if(k1 == 1)
            flag = true;
        else
            flag = false;
        k1 = 0;
        while(k1 < numPenSegments) 
        {
            if(side(i + pen_dx[k1], j + pen_dy[k1], k, l, i1, j1) != flag)
                penIncluded[k1] = true;
            else
                penIncluded[k1] = false;
            k1++;
        }
        k1 = -1;
        int i2 = -1;
        for(int j2 = 0; j2 < numPenSegments;)
        {
            int l2 = k1;
            if(penIncluded[j2])
            {
                l2 = k1;
                if(!penIncluded[((numPenSegments + j2) - 1) % numPenSegments])
                    l2 = j2;
            }
            k1 = i2;
            if(penIncluded[j2])
            {
                k1 = i2;
                if(!penIncluded[(j2 + 1) % numPenSegments])
                    k1 = j2;
            }
            j2++;
            i2 = k1;
            k1 = l2;
        }

        int k2;
        int i3;
        long l3;
        long l4;
        long l5;
        long l6;
        if(i2 < k1)
            k2 = i2 + numPenSegments;
        else
            k2 = i2;
        i2 = l1;
        if(k1 == -1) goto _L2; else goto _L1
_L1:
        i2 = l1;
        if(k2 == -1) goto _L2; else goto _L3
_L3:
        l3 = (pen_dx[k1] + i) - k;
        l4 = (pen_dy[k1] + j) - l;
        l5 = (pen_dx[k1] + i) - i1;
        l6 = (pen_dy[k1] + j) - j1;
        if(l3 * l3 + l4 * l4 > l5 * l5 + l6 * l6)
            l = 1;
        else
            l = 0;
        if(l != 0)
            k = k2;
        else
            k = k1;
        if(l != 0)
            i1 = -1;
        else
            i1 = 1;
        j1 = 0;
_L7:
        i2 = k % numPenSegments;
        i3 = pen_dx[i2];
        l1 = pen_dy[i2];
        i2 = j1 + 1;
        ai[j1] = i3 + i;
        j1 = i2 + 1;
        ai[i2] = l1 + j;
        if(l != 0)
            i2 = k1;
        else
            i2 = k2;
        if(k != i2) goto _L5; else goto _L4
_L4:
        i2 = j1;
_L2:
        return i2 / 2;
_L5:
        k += i1;
        if(true) goto _L7; else goto _L6
_L6:
    }

    private void drawMiter(int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1, int i2, int j2, int k2, boolean flag)
    {
_L2:
        return;
        if(i2 == k1 && j2 == l1 || i == k && j == l || k == i1 && l == j1) goto _L2; else goto _L1
_L1:
        int l2 = k1;
        int i3 = l1;
        int j3 = i2;
        int k3 = j2;
        if(flag)
        {
            l2 = -k1;
            i3 = -l1;
            j3 = -i2;
            k3 = -j2;
        }
        computeMiter(i + l2, j + i3, k + l2, l + i3, k + j3, l + k3, i1 + j3, j1 + k3, miter);
        long l3 = (long)miter[0] - (long)k;
        long l4 = (long)miter[1] - (long)l;
        long l5 = (long)m00 * l4 - (long)m10 * l3 >> 16;
        l3 = l4 * (long)m01 - l3 * (long)m11 >> 16;
        if(l3 * l3 + l5 * l5 < miterLimitSq)
            emitLineTo(miter[0], miter[1], k2, flag);
        if(true) goto _L2; else goto _L3
_L3:
    }

    private void drawRoundJoin(int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1, boolean flag, boolean flag1, long l2)
    {
        if((k != 0 || l != 0) && (i1 != 0 || j1 != 0)) goto _L2; else goto _L1
_L1:
        return;
_L2:
        long l3 = (long)k - (long)i1;
        long l4 = (long)l - (long)j1;
        if(l3 * l3 + l4 * l4 >= l2)
        {
            int i2 = k;
            int j2 = l;
            int k2 = i1;
            int i3 = j1;
            if(flag1)
            {
                i2 = -k;
                j2 = -l;
                k2 = -i1;
                i3 = -j1;
            }
            j = computeRoundJoin(i, j, i + i2, j + j2, i + k2, j + i3, k1, flag, join);
            i = 0;
            while(i < j) 
            {
                emitLineTo(join[i * 2], join[i * 2 + 1], l1, flag1);
                i++;
            }
        }
        if(true) goto _L1; else goto _L3
_L3:
    }

    private void emitClose()
    {
        output.close();
    }

    private void emitLineTo(int i, int j, int k)
    {
        output.lineTo(i, j, k);
    }

    private void emitLineTo(int i, int j, int k, boolean flag)
    {
        if(flag)
        {
            ensureCapacity(rindex + 3);
            int ai[] = reverse;
            int l = rindex;
            rindex = l + 1;
            ai[l] = i;
            ai = reverse;
            i = rindex;
            rindex = i + 1;
            ai[i] = j;
            ai = reverse;
            i = rindex;
            rindex = i + 1;
            ai[i] = k;
        } else
        {
            emitLineTo(i, j, k);
        }
    }

    private void emitMoveTo(int i, int j, int k)
    {
        output.moveTo(i, j, k);
    }

    private void ensureCapacity(int i)
    {
        if(reverse.length < i)
        {
            int ai[] = new int[Math.max(i, (reverse.length * 6) / 5)];
            System.arraycopy(reverse, 0, ai, 0, rindex);
            reverse = ai;
        }
    }

    private void finish()
    {
        if(capStyle != 1) goto _L2; else goto _L1
_L1:
        drawRoundJoin(x0, y0, omx, omy, -omx, -omy, 1, color0, false, false, 0x5f5e100L);
_L4:
        for(int i = rindex - 3; i >= 0; i -= 3)
            emitLineTo(reverse[i], reverse[i + 1], reverse[i + 2]);

        break; /* Loop/switch isn't completed */
_L2:
        if(capStyle == 2)
        {
            long l = px0 - x0;
            long l2 = py0 - y0;
            long l4 = lineLength(l, l2);
            if(0L < l4)
            {
                l4 = ((long)lineWidth2 * 0x10000L) / l4;
                int j = x0 - (int)(l * l4 >> 16);
                int i1 = y0 - (int)(l2 * l4 >> 16);
                emitLineTo(omx + j, omy + i1, color0);
                emitLineTo(j - omx, i1 - omy, color0);
            }
        }
        if(true) goto _L4; else goto _L3
_L3:
        rindex = 0;
        if(capStyle != 1) goto _L6; else goto _L5
_L5:
        drawRoundJoin(sx0, sy0, -mx0, -my0, mx0, my0, 1, scolor0, false, false, 0x5f5e100L);
_L8:
        emitClose();
        joinSegment = false;
        return;
_L6:
        if(capStyle == 2)
        {
            long l3 = sx1 - sx0;
            long l1 = sy1 - sy0;
            long l5 = lineLength(l3, l1);
            if(0L < l5)
            {
                l5 = ((long)lineWidth2 * 0x10000L) / l5;
                int j1 = sx0 - (int)(l3 * l5 >> 16);
                int k = sy0 - (int)(l1 * l5 >> 16);
                emitLineTo(j1 - mx0, k - my0, scolor0);
                emitLineTo(j1 + mx0, k + my0, scolor0);
            }
        }
        if(true) goto _L8; else goto _L7
_L7:
    }

    private boolean isCCW(int i, int j, int k, int l, int i1, int j1)
    {
        boolean flag;
        if((long)(k - i) * (long)(j1 - l) < (long)(l - j) * (long)(i1 - k))
            flag = true;
        else
            flag = false;
        return flag;
    }

    private void lineToImpl(int i, int j, int k, boolean flag)
    {
        computeOffset(x0, y0, i, j, offset);
        int l = offset[0];
        int i1 = offset[1];
        if(!started)
        {
            emitMoveTo(x0 + l, y0 + i1, color0);
            sx1 = i;
            sy1 = j;
            mx0 = l;
            my0 = i1;
            started = true;
        } else
        {
            boolean flag1 = isCCW(px0, py0, x0, y0, i, j);
            int j1;
            int k1;
            int l1;
            if(flag)
            {
                if(joinStyle == 0)
                    drawMiter(px0, py0, x0, y0, i, j, omx, omy, l, i1, color0, flag1);
                else
                if(joinStyle == 1)
                    drawRoundJoin(x0, y0, omx, omy, l, i1, 0, color0, false, flag1, 0x5f5e100L);
            } else
            {
                drawRoundJoin(x0, y0, omx, omy, l, i1, 0, color0, false, flag1, 0x3b9aca00L);
            }
            j1 = x0;
            k1 = y0;
            l1 = color0;
            if(!flag1)
                flag = true;
            else
                flag = false;
            emitLineTo(j1, k1, l1, flag);
        }
        emitLineTo(x0 + l, y0 + i1, color0, false);
        emitLineTo(i + l, j + i1, k, false);
        emitLineTo(x0 - l, y0 - i1, color0, true);
        emitLineTo(i - l, j - i1, k, true);
        omx = l;
        omy = i1;
        px0 = x0;
        py0 = y0;
        pcolor0 = color0;
        x0 = i;
        y0 = j;
        color0 = k;
        prev = 1;
    }

    private boolean side(int i, int j, int k, int l, int i1, int j1)
    {
        long l1 = i;
        long l2 = j;
        long l3 = k;
        long l4 = l;
        long l5 = i1;
        long l6 = j1;
        boolean flag;
        if(l1 * (l4 - l6) + l2 * (l5 - l3) + (l3 * l6 - l5 * l4) > 0L)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public void close()
    {
        if(lineToOrigin)
            lineToOrigin = false;
        if(started) goto _L2; else goto _L1
_L1:
        finish();
_L4:
        return;
_L2:
        computeOffset(x0, y0, sx0, sy0, offset);
        int i = offset[0];
        int j = offset[1];
        boolean flag = isCCW(px0, py0, x0, y0, sx0, sy0);
        if(joinSegment)
        {
            if(joinStyle == 0)
                drawMiter(px0, py0, x0, y0, sx0, sy0, omx, omy, i, j, pcolor0, flag);
            else
            if(joinStyle == 1)
                drawRoundJoin(x0, y0, omx, omy, i, j, 0, color0, false, flag, 0x5f5e100L);
        } else
        {
            drawRoundJoin(x0, y0, omx, omy, i, j, 0, color0, false, flag, 0x3b9aca00L);
        }
        emitLineTo(x0 + i, y0 + j, color0);
        emitLineTo(sx0 + i, sy0 + j, scolor0);
        flag = isCCW(x0, y0, sx0, sy0, sx1, sy1);
        if(!flag)
            if(joinStyle == 0)
                drawMiter(x0, y0, sx0, sy0, sx1, sy1, i, j, mx0, my0, color0, false);
            else
            if(joinStyle == 1)
                drawRoundJoin(sx0, sy0, i, j, mx0, my0, 0, scolor0, false, false, 0x5f5e100L);
        emitLineTo(sx0 + mx0, sy0 + my0, scolor0);
        emitLineTo(sx0 - mx0, sy0 - my0, scolor0);
        if(flag)
            if(joinStyle == 0)
                drawMiter(x0, y0, sx0, sy0, sx1, sy1, -i, -j, -mx0, -my0, color0, false);
            else
            if(joinStyle == 1)
                drawRoundJoin(sx0, sy0, -i, -j, -mx0, -my0, 0, scolor0, true, false, 0x5f5e100L);
        emitLineTo(sx0 - i, sy0 - j, scolor0);
        emitLineTo(x0 - i, y0 - j, color0);
        for(j = rindex - 3; j >= 0; j -= 3)
            emitLineTo(reverse[j], reverse[j + 1], reverse[j + 2]);

        x0 = sx0;
        y0 = sy0;
        rindex = 0;
        started = false;
        joinSegment = false;
        prev = 2;
        emitClose();
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void end()
    {
        if(lineToOrigin)
        {
            lineToImpl(sx0, sy0, scolor0, joinToOrigin);
            lineToOrigin = false;
        }
        if(prev == 1)
            finish();
        output.end();
        joinSegment = false;
        prev = 0;
    }

    public void lineJoin()
    {
        joinSegment = true;
    }

    long lineLength(long l, long l1)
    {
        long l2 = (long)m00 * (long)m11 - (long)m01 * (long)m10 >> 16;
        return (long)(int)LinePath.hypot(((long)m00 * l1 - (long)m10 * l) / l2, ((long)m01 * l1 - (long)m11 * l) / l2);
    }

    public void lineTo(int i, int j, int k)
    {
        if(!lineToOrigin) goto _L2; else goto _L1
_L1:
        if(i != sx0 || j != sy0) goto _L4; else goto _L3
_L3:
        return;
_L4:
        lineToImpl(sx0, sy0, scolor0, joinToOrigin);
        lineToOrigin = false;
_L6:
        lineToImpl(i, j, k, joinSegment);
        joinSegment = false;
        continue; /* Loop/switch isn't completed */
_L2:
        if(i == x0 && j == y0)
            continue; /* Loop/switch isn't completed */
        if(i != sx0 || j != sy0) goto _L6; else goto _L5
_L5:
        lineToOrigin = true;
        joinToOrigin = joinSegment;
        joinSegment = false;
        if(true) goto _L3; else goto _L7
_L7:
    }

    public void moveTo(int i, int j, int k)
    {
        if(lineToOrigin)
        {
            lineToImpl(sx0, sy0, scolor0, joinToOrigin);
            lineToOrigin = false;
        }
        if(prev == 1)
            finish();
        x0 = i;
        sx0 = i;
        y0 = j;
        sy0 = j;
        color0 = k;
        scolor0 = k;
        rindex = 0;
        started = false;
        joinSegment = false;
        prev = 0;
    }

    public void setOutput(LineStroker linestroker)
    {
        output = linestroker;
    }

    public void setParameters(int i, int j, int k, int l, PMatrix2D pmatrix2d)
    {
        m00 = LinePath.FloatToS15_16(pmatrix2d.m00);
        m01 = LinePath.FloatToS15_16(pmatrix2d.m01);
        m10 = LinePath.FloatToS15_16(pmatrix2d.m10);
        m11 = LinePath.FloatToS15_16(pmatrix2d.m11);
        lineWidth2 = i >> 1;
        scaledLineWidth2 = (long)m00 * (long)lineWidth2 >> 16;
        capStyle = j;
        joinStyle = k;
        m00_2_m01_2 = (double)m00 * (double)m00 + (double)m01 * (double)m01;
        m10_2_m11_2 = (double)m10 * (double)m10 + (double)m11 * (double)m11;
        m00_m10_m01_m11 = (double)m00 * (double)m10 + (double)m01 * (double)m11;
        double d = (double)m00 / 65536D;
        double d1 = (double)m01 / 65536D;
        double d2 = (double)m10 / 65536D;
        double d3 = (double)m11 / 65536D;
        if(k == 0)
        {
            double d4 = (d * d3 - d1 * d2) * (((double)l / 65536D) * ((double)lineWidth2 / 65536D));
            miterLimitSq = (long)(d4 * d4 * 65536D * 65536D);
        }
        numPenSegments = (int)((3.14159F * (float)i) / 65536F);
        if(pen_dx == null || pen_dx.length < numPenSegments)
        {
            pen_dx = new int[numPenSegments];
            pen_dy = new int[numPenSegments];
            penIncluded = new boolean[numPenSegments];
            join = new int[numPenSegments * 2];
        }
        for(j = 0; j < numPenSegments; j++)
        {
            double d6 = (double)i / 2D;
            double d7 = ((double)(j * 2) * 3.1415926535897931D) / (double)numPenSegments;
            double d5 = Math.cos(d7);
            d7 = Math.sin(d7);
            pen_dx[j] = (int)((d * d5 + d1 * d7) * d6);
            pen_dy[j] = (int)(d6 * (d7 * d3 + d5 * d2));
        }

        prev = 2;
        rindex = 0;
        started = false;
        lineToOrigin = false;
    }

    private static final long ROUND_JOIN_INTERNAL_THRESHOLD = 0x3b9aca00L;
    private static final long ROUND_JOIN_THRESHOLD = 0x5f5e100L;
    private int capStyle;
    private int color0;
    private int join[];
    boolean joinSegment;
    private int joinStyle;
    private boolean joinToOrigin;
    private boolean lineToOrigin;
    private int lineWidth2;
    private int m00;
    private double m00_2_m01_2;
    private double m00_m10_m01_m11;
    private int m01;
    private int m10;
    private double m10_2_m11_2;
    private int m11;
    private int miter[];
    private long miterLimitSq;
    private int mx0;
    private int my0;
    private int numPenSegments;
    private int offset[];
    private int omx;
    private int omy;
    private LineStroker output;
    private int pcolor0;
    private boolean penIncluded[];
    private int pen_dx[];
    private int pen_dy[];
    private int prev;
    private int px0;
    private int py0;
    private int reverse[];
    private int rindex;
    private long scaledLineWidth2;
    private int scolor0;
    private boolean started;
    private int sx0;
    private int sx1;
    private int sy0;
    private int sy1;
    private int x0;
    private int y0;
}
