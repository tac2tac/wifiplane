// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.util;

import java.io.PrintWriter;

public final class TimeUtils
{

    private TimeUtils()
    {
    }

    private static int accumField(int i, int j, boolean flag, int k)
    {
        if(i > 99 || flag && k >= 3)
            i = j + 3;
        else
        if(i > 9 || flag && k >= 2)
            i = j + 2;
        else
        if(flag || i > 0)
            i = j + 1;
        else
            i = 0;
        return i;
    }

    public static void formatDuration(long l, long l1, PrintWriter printwriter)
    {
        if(l == 0L)
            printwriter.print("--");
        else
            formatDuration(l - l1, printwriter, 0);
    }

    public static void formatDuration(long l, PrintWriter printwriter)
    {
        formatDuration(l, printwriter, 0);
    }

    public static void formatDuration(long l, PrintWriter printwriter, int i)
    {
        synchronized(sFormatSync)
        {
            i = formatDurationLocked(l, i);
            String s = JVM INSTR new #49  <Class String>;
            s.String(sFormatStr, 0, i);
            printwriter.print(s);
        }
        return;
        printwriter;
        obj;
        JVM INSTR monitorexit ;
        throw printwriter;
    }

    public static void formatDuration(long l, StringBuilder stringbuilder)
    {
        synchronized(sFormatSync)
        {
            int i = formatDurationLocked(l, 0);
            stringbuilder.append(sFormatStr, 0, i);
        }
        return;
        stringbuilder;
        obj;
        JVM INSTR monitorexit ;
        throw stringbuilder;
    }

    private static int formatDurationLocked(long l, int i)
    {
        if(sFormatStr.length < i)
            sFormatStr = new char[i];
        char ac[] = sFormatStr;
        if(l == 0L)
        {
            while(i - 1 < 0) 
                ac[0] = (char)32;
            ac[0] = (char)48;
            i = 1;
        } else
        {
            int j;
            int k;
            int i1;
            int j1;
            int k1;
            int l1;
            int i2;
            int j2;
            boolean flag;
            if(l > 0L)
            {
                j = 43;
            } else
            {
                j = 45;
                l = -l;
            }
            k = (int)(l % 1000L);
            i1 = (int)Math.floor(l / 1000L);
            j1 = 0;
            k1 = 0;
            l1 = 0;
            i2 = i1;
            if(i1 > 0x15180)
            {
                j1 = i1 / 0x15180;
                i2 = i1 - 0x15180 * j1;
            }
            i1 = i2;
            if(i2 > 3600)
            {
                k1 = i2 / 3600;
                i1 = i2 - k1 * 3600;
            }
            i2 = i1;
            if(i1 > 60)
            {
                l1 = i1 / 60;
                i2 = i1 - l1 * 60;
            }
            j2 = 0;
            flag = false;
            if(i != 0)
            {
                i1 = accumField(j1, 1, false, 0);
                boolean flag1;
                int k2;
                if(i1 > 0)
                    flag1 = true;
                else
                    flag1 = false;
                i1 += accumField(k1, 1, flag1, 2);
                if(i1 > 0)
                    flag1 = true;
                else
                    flag1 = false;
                i1 += accumField(l1, 1, flag1, 2);
                if(i1 > 0)
                    flag1 = true;
                else
                    flag1 = false;
                k2 = i1 + accumField(i2, 1, flag1, 2);
                if(k2 > 0)
                    i1 = 3;
                else
                    i1 = 0;
                k2 += accumField(k, 2, true, i1) + 1;
                i1 = ((flag) ? 1 : 0);
                do
                {
                    j2 = i1;
                    if(k2 >= i)
                        break;
                    ac[i1] = (char)32;
                    i1++;
                    k2++;
                } while(true);
            }
            ac[j2] = (char)j;
            j = j2 + 1;
            boolean flag2;
            if(i != 0)
                i = 1;
            else
                i = 0;
            j1 = printField(ac, j1, 'd', j, false, 0);
            if(j1 != j)
                flag2 = true;
            else
                flag2 = false;
            if(i != 0)
                i1 = 2;
            else
                i1 = 0;
            k1 = printField(ac, k1, 'h', j1, flag2, i1);
            if(k1 != j)
                flag2 = true;
            else
                flag2 = false;
            if(i != 0)
                i1 = 2;
            else
                i1 = 0;
            l1 = printField(ac, l1, 'm', k1, flag2, i1);
            if(l1 != j)
                flag2 = true;
            else
                flag2 = false;
            if(i != 0)
                i1 = 2;
            else
                i1 = 0;
            i1 = printField(ac, i2, 's', l1, flag2, i1);
            if(i != 0 && i1 != j)
                i = 3;
            else
                i = 0;
            i = printField(ac, k, 'm', i1, true, i);
            ac[i] = (char)115;
            i++;
        }
        return i;
    }

    private static int printField(char ac[], int i, char c, int j, boolean flag, int k)
    {
        int l;
label0:
        {
            int i1;
label1:
            {
                if(!flag)
                {
                    l = j;
                    if(i <= 0)
                        break label0;
                }
                if(!flag || k < 3)
                {
                    l = i;
                    i1 = j;
                    if(i <= 99)
                        break label1;
                }
                l = i / 100;
                ac[j] = (char)(l + 48);
                i1 = j + 1;
                l = i - l * 100;
            }
label2:
            {
                if((!flag || k < 2) && l <= 9)
                {
                    k = l;
                    i = i1;
                    if(j == i1)
                        break label2;
                }
                j = l / 10;
                ac[i1] = (char)(j + 48);
                i = i1 + 1;
                k = l - j * 10;
            }
            ac[i] = (char)(k + 48);
            i++;
            ac[i] = c;
            l = i + 1;
        }
        return l;
    }

    public static final int HUNDRED_DAY_FIELD_LEN = 19;
    private static final int SECONDS_PER_DAY = 0x15180;
    private static final int SECONDS_PER_HOUR = 3600;
    private static final int SECONDS_PER_MINUTE = 60;
    private static char sFormatStr[] = new char[24];
    private static final Object sFormatSync = new Object();

}
