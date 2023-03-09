// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.core;

import android.graphics.Bitmap;
import java.io.*;
import java.util.HashMap;

// Referenced classes of package processing.core:
//            PConstants, PApplet

public class PImage
    implements PConstants, Cloneable
{

    public PImage()
    {
        pixelDensity = 1;
        loaded = false;
        format = 2;
    }

    public PImage(int i, int j)
    {
        pixelDensity = 1;
        loaded = false;
        init(i, j, 1);
    }

    public PImage(int i, int j, int k)
    {
        pixelDensity = 1;
        loaded = false;
        init(i, j, k);
    }

    public PImage(Object obj)
    {
        pixelDensity = 1;
        loaded = false;
        obj = (Bitmap)obj;
        bitmap = ((Bitmap) (obj));
        width = ((Bitmap) (obj)).getWidth();
        height = ((Bitmap) (obj)).getHeight();
        pixels = null;
        int i;
        if(((Bitmap) (obj)).hasAlpha())
            i = 2;
        else
            i = 1;
        format = i;
        pixelDensity = 1;
        pixelWidth = width;
        pixelHeight = height;
    }

    public static int blendColor(int i, int j, int k)
    {
        int l = j;
        k;
        JVM INSTR lookupswitch 15: default 132
    //                   0: 134
    //                   1: 136
    //                   2: 145
    //                   4: 154
    //                   8: 163
    //                   16: 172
    //                   32: 181
    //                   64: 190
    //                   128: 199
    //                   256: 208
    //                   512: 235
    //                   1024: 217
    //                   2048: 226
    //                   4096: 244
    //                   8192: 253;
           goto _L1 _L2 _L3 _L4 _L5 _L6 _L7 _L8 _L9 _L10 _L11 _L12 _L13 _L14 _L15 _L16
_L2:
        break; /* Loop/switch isn't completed */
_L1:
        l = 0;
_L18:
        return l;
_L3:
        l = blend_blend(i, j);
        continue; /* Loop/switch isn't completed */
_L4:
        l = blend_add_pin(i, j);
        continue; /* Loop/switch isn't completed */
_L5:
        l = blend_sub_pin(i, j);
        continue; /* Loop/switch isn't completed */
_L6:
        l = blend_lightest(i, j);
        continue; /* Loop/switch isn't completed */
_L7:
        l = blend_darkest(i, j);
        continue; /* Loop/switch isn't completed */
_L8:
        l = blend_difference(i, j);
        continue; /* Loop/switch isn't completed */
_L9:
        l = blend_exclusion(i, j);
        continue; /* Loop/switch isn't completed */
_L10:
        l = blend_multiply(i, j);
        continue; /* Loop/switch isn't completed */
_L11:
        l = blend_screen(i, j);
        continue; /* Loop/switch isn't completed */
_L13:
        l = blend_hard_light(i, j);
        continue; /* Loop/switch isn't completed */
_L14:
        l = blend_soft_light(i, j);
        continue; /* Loop/switch isn't completed */
_L12:
        l = blend_overlay(i, j);
        continue; /* Loop/switch isn't completed */
_L15:
        l = blend_dodge(i, j);
        continue; /* Loop/switch isn't completed */
_L16:
        l = blend_burn(i, j);
        if(true) goto _L18; else goto _L17
_L17:
    }

    private static int blend_add_pin(int i, int j)
    {
        int k = j >>> 24;
        int l;
        int i1;
        int j1;
        if(k >= 127)
            l = 1;
        else
            l = 0;
        i1 = l + k;
        l = (i & 0xff00ff) + ((j & 0xff00ff) * i1 >>> 8 & 0xff00ff);
        j1 = min(k + (i >>> 24), 255);
        k = min(0xffff0000 & l, 0xff0000);
        return min((i1 * (j & 0xff00) >>> 8) + (i & 0xff00) & 0xffff00, 65280) | (j1 << 24 | k) | min(0xffff & l, 255);
    }

    private static int blend_blend(int i, int j)
    {
        int k = j >>> 24;
        int l;
        int i1;
        if(k >= 127)
            l = 1;
        else
            l = 0;
        l += k;
        i1 = 256 - l;
        return l * (j & 0xff00) + i1 * (i & 0xff00) >>> 8 & 0xff00 | (min(k + (i >>> 24), 255) << 24 | (i & 0xff00ff) * i1 + (j & 0xff00ff) * l >>> 8 & 0xff00ff);
    }

    private static int blend_burn(int i, int j)
    {
        int k = 0xff0000;
        int l = j >>> 24;
        int i1;
        int j1;
        int k1;
        int l1;
        int i2;
        if(l >= 127)
            i1 = 1;
        else
            i1 = 0;
        j1 = l + i1;
        k1 = 256 - j1;
        i1 = (0xff0000 - (i & 0xff0000)) / ((j & 0xff) + 1);
        l1 = (65280 - (i & 0xff00) << 8) / ((j & 0xff) + 1);
        i2 = (255 - (i & 0xff) << 8) / ((j & 0xff) + 1);
        if(i1 > 65280)
            j = k;
        else
            j = 0xff0000 & i1 << 8;
        i1 = i2;
        if(i2 > 255)
            i1 = 255;
        if(l1 > 65280)
            k = 65280;
        else
            k = l1 & 0xff00;
        return (65280 - k) * j1 + (i & 0xff00) * k1 >>> 8 & 0xff00 | (min(l + (i >>> 24), 255) << 24 | (0xff00ff - j - i1) * j1 + (i & 0xff00ff) * k1 >>> 8 & 0xff00ff);
    }

    private static int blend_darkest(int i, int j)
    {
        int k = j >>> 24;
        int l;
        int i1;
        int j1;
        int k1;
        if(k >= 127)
            l = 1;
        else
            l = 0;
        i1 = l + k;
        l = 256 - i1;
        j1 = min(j & 0xff0000, 0xff0000 & i);
        k1 = min(j & 0xff, i & 0xff);
        return i1 * min(j & 0xff00, i & 0xff00) + l * (i & 0xff00) >>> 8 & 0xff00 | (min(k + (i >>> 24), 255) << 24 | (j1 | k1) * i1 + (i & 0xff00ff) * l >>> 8 & 0xff00ff);
    }

    private static int blend_difference(int i, int j)
    {
        int k = j >>> 24;
        int l;
        int i1;
        int j1;
        int k1;
        int l1;
        if(k >= 127)
            l = 1;
        else
            l = 0;
        i1 = k + l;
        j1 = 256 - i1;
        l = (i & 0xff0000) - (0xff0000 & j);
        k1 = (i & 0xff) - (j & 0xff);
        l1 = (i & 0xff00) - (j & 0xff00);
        j = l;
        if(l < 0)
            j = -l;
        l = k1;
        if(k1 < 0)
            l = -k1;
        k1 = l1;
        if(l1 < 0)
            k1 = -l1;
        return k1 * i1 + (i & 0xff00) * j1 >>> 8 & 0xff00 | ((l | j) * i1 + (i & 0xff00ff) * j1 >>> 8 & 0xff00ff | min((i >>> 24) + k, 255) << 24);
    }

    private static int blend_dodge(int i, int j)
    {
        int k = 0xff0000;
        int l = j >>> 24;
        int i1;
        int j1;
        int k1;
        int l1;
        int i2;
        if(l >= 127)
            i1 = 1;
        else
            i1 = 0;
        j1 = l + i1;
        k1 = 256 - j1;
        i1 = (i & 0xff0000) / (256 - ((j & 0xff0000) >> 16));
        l1 = ((i & 0xff00) << 8) / (256 - ((j & 0xff00) >> 8));
        i2 = ((i & 0xff) << 8) / (256 - (j & 0xff));
        if(i1 > 65280)
            j = k;
        else
            j = 0xff0000 & i1 << 8;
        i1 = i2;
        if(i2 > 255)
            i1 = 255;
        if(l1 > 65280)
            k = 65280;
        else
            k = l1 & 0xff00;
        return k * j1 + (i & 0xff00) * k1 >>> 8 & 0xff00 | (min(l + (i >>> 24), 255) << 24 | (j | i1) * j1 + (i & 0xff00ff) * k1 >>> 8 & 0xff00ff);
    }

    private static int blend_exclusion(int i, int j)
    {
        int k = j >>> 24;
        int l;
        int i1;
        int j1;
        int k1;
        int l1;
        int i2;
        int j2;
        int k2;
        int l2;
        char c;
        if(k >= 127)
            l = 1;
        else
            l = 0;
        i1 = k + l;
        j1 = 256 - i1;
        k1 = i & 0xff00ff;
        l1 = i & 0xff00;
        i2 = j & 0xff00;
        j2 = (0xff0000 & i) >> 16;
        k2 = i & 0xff;
        if(j2 >= 127)
            l = 1;
        else
            l = 0;
        if(k2 >= 127)
            l2 = 1;
        else
            l2 = 0;
        if(l1 >= 32512)
            c = '\u0100';
        else
            c = '\0';
        return ((l1 + i2) - ((c + l1) * i2 >>> 15 & 0x1ff00)) * i1 + j1 * l1 >>> 8 & 0xff00 | (min(k + (i >>> 24), 255) << 24 | ((k1 + (0xff00ff & j)) - (0x1ff01ff & ((l2 + k2) * (j & 0xff) | (j & 0xff0000) * (l + j2)) >>> 7)) * i1 + k1 * j1 >>> 8 & 0xff00ff);
    }

    private static int blend_hard_light(int i, int j)
    {
        int k = j >>> 24;
        int l;
        int i1;
        int j1;
        int k1;
        int l1;
        int i2;
        int j2;
        int k2;
        if(k >= 127)
            l = 1;
        else
            l = 0;
        i1 = k + l;
        j1 = 256 - i1;
        k1 = i & 0xff0000;
        l = i & 0xff00;
        l1 = i & 0xff;
        i2 = j & 0xff0000;
        j2 = j & 0xff00;
        k2 = j & 0xff;
        if(i2 < 0x800000)
            j = ((k1 >>> 16) + 1) * i2 >>> 7;
        else
            j = 0xff0000 - ((256 - (k1 >>> 16)) * (0xff0000 - i2) >>> 7);
        if(j2 < 32768)
            l = (l + 256) * j2 >>> 15;
        else
            l = 65280 - ((0x10000 - l) * (65280 - j2) >>> 15);
        if(k2 < 128)
            l1 = (l1 + 1) * k2 >>> 7;
        else
            l1 = 65280 - ((256 - l1) * (255 - k2) << 1) >>> 8;
        return ((l1 | j) & 0xff00ff) * i1 + (i & 0xff00ff) * j1 >>> 8 & 0xff00ff | min(k + (i >>> 24), 255) << 24 | (l & 0xff00) * i1 + (i & 0xff00) * j1 >>> 8 & 0xff00;
    }

    private static int blend_lightest(int i, int j)
    {
        int k = j >>> 24;
        int l;
        int i1;
        int j1;
        int k1;
        if(k >= 127)
            l = 1;
        else
            l = 0;
        i1 = l + k;
        j1 = 256 - i1;
        l = max(j & 0xff0000, 0xff0000 & i);
        k1 = max(j & 0xff, i & 0xff);
        return i1 * max(j & 0xff00, i & 0xff00) + j1 * (i & 0xff00) >>> 8 & 0xff00 | (min(k + (i >>> 24), 255) << 24 | (l | k1) * i1 + (i & 0xff00ff) * j1 >>> 8 & 0xff00ff);
    }

    private static int blend_multiply(int i, int j)
    {
        int k = j >>> 24;
        int l;
        int i1;
        int j1;
        if(k >= 127)
            l = 1;
        else
            l = 0;
        i1 = l + k;
        l = 256 - i1;
        j1 = i & 0xff00;
        return i1 * ((j & 0xff00) * (j1 + 256) >>> 16 & 0xff00) + l * j1 >>> 8 & 0xff00 | (min(k + (i >>> 24), 255) << 24 | (((((i & 0xff0000) >> 16) + 1) * (0xff0000 & j) | ((i & 0xff) + 1) * (j & 0xff)) >>> 8 & 0xff00ff) * i1 + (i & 0xff00ff) * l >>> 8 & 0xff00ff);
    }

    private static int blend_overlay(int i, int j)
    {
        int k = j >>> 24;
        int l;
        int i1;
        int j1;
        int k1;
        int l1;
        int i2;
        int j2;
        int k2;
        if(k >= 127)
            l = 1;
        else
            l = 0;
        i1 = k + l;
        j1 = 256 - i1;
        k1 = i & 0xff0000;
        l1 = i & 0xff00;
        i2 = i & 0xff;
        j2 = j & 0xff0000;
        l = j & 0xff00;
        k2 = j & 0xff;
        if(k1 < 0x800000)
            j = k1 * ((j2 >>> 16) + 1) >>> 7;
        else
            j = 0xff0000 - ((0xff0000 - k1) * (256 - (j2 >>> 16)) >>> 7);
        if(l1 < 32768)
            l = (l + 256) * l1 >>> 15;
        else
            l = 65280 - ((0x10000 - l) * (65280 - l1) >>> 15);
        if(i2 < 128)
            i2 = (k2 + 1) * i2 >>> 7;
        else
            i2 = 65280 - ((256 - k2) * (255 - i2) << 1) >>> 8;
        return ((i2 | j) & 0xff00ff) * i1 + (i & 0xff00ff) * j1 >>> 8 & 0xff00ff | min(k + (i >>> 24), 255) << 24 | (l & 0xff00) * i1 + (i & 0xff00) * j1 >>> 8 & 0xff00;
    }

    private static int blend_screen(int i, int j)
    {
        int k = j >>> 24;
        int l;
        int i1;
        int j1;
        int k1;
        int l1;
        if(k >= 127)
            l = 1;
        else
            l = 0;
        i1 = l + k;
        j1 = 256 - i1;
        k1 = i & 0xff00ff;
        l1 = i & 0xff00;
        l = j & 0xff00;
        return i1 * ((l1 + l) - ((l1 + 256) * l >>> 16 & 0xff00)) + j1 * l1 >>> 8 & 0xff00 | (min(k + (i >>> 24), 255) << 24 | ((k1 + (j & 0xff00ff)) - (((((i & 0xff0000) >> 16) + 1) * (0xff0000 & j) | ((i & 0xff) + 1) * (j & 0xff)) >>> 8 & 0xff00ff)) * i1 + k1 * j1 >>> 8 & 0xff00ff);
    }

    private static int blend_soft_light(int i, int j)
    {
        int k = j >>> 24;
        int l;
        int i1;
        int j1;
        int k1;
        int l1;
        int i2;
        int j2;
        int k2;
        int l2;
        int i3;
        if(k >= 127)
            l = 1;
        else
            l = 0;
        l = k + l;
        i1 = 256 - l;
        j1 = i & 0xff0000;
        k1 = i & 0xff00;
        l1 = i & 0xff;
        i2 = j & 0xff;
        j2 = j & 0xff;
        k2 = j & 0xff;
        if((float)i2 < 7F)
            j = 1;
        else
            j = 0;
        l2 = (j1 >> 16) + j;
        if((float)j2 < 7F)
            j = 1;
        else
            j = 0;
        i3 = (k1 >> 8) + j;
        if((float)k2 < 7F)
            j = 1;
        else
            j = 0;
        j += l1;
        return (((l1 * k2 << 9) + j * 255 * (j + 1)) - (j * (k2 * j) << 1) >>> 16 | ((j1 * i2 >> 7) + l2 * 255 * (l2 + 1)) - (i2 * l2 * l2 << 1) & 0xff0000) * l + (0xff00ff & i) * i1 >>> 8 & 0xff00ff | min(k + (i >>> 24), 255) << 24 | (0xff00 & i) * i1 + l * (((k1 * j2 << 1) + i3 * 255 * (i3 + 1)) - (j2 * i3 * i3 << 1) >>> 8 & 0xff00) >>> 8 & 0xff00;
    }

    private static int blend_sub_pin(int i, int j)
    {
        int k = j >>> 24;
        int l;
        int i1;
        int j1;
        if(k >= 127)
            l = 1;
        else
            l = 0;
        i1 = l + k;
        l = (0xff00ff & j) * i1 >>> 8;
        j1 = min(k + (i >>> 24), 255);
        k = max((i & 0xff0000) - (0xff0000 & l), 0);
        return max((i & 0xff00) - (i1 * (j & 0xff00) >>> 8 & 0xff00), 0) | (j1 << 24 | k) | max((i & 0xff) - (l & 0xff), 0);
    }

    private void blit_resize(PImage pimage, int i, int j, int k, int l, int ai[], int i1, 
            int j1, int k1, int l1, int i2, int j2, int k2)
    {
        int l2;
        int i3;
        l2 = i;
        if(i < 0)
            l2 = 0;
        i = j;
        if(j < 0)
            i = 0;
        j = k;
        if(k > pimage.pixelWidth)
            j = pimage.pixelWidth;
        k = l;
        if(l > pimage.pixelHeight)
            k = pimage.pixelHeight;
        i3 = j - l2;
        j = k - i;
        k = i2 - k1;
        l = j2 - l1;
        if(k > 0 && l > 0 && i3 > 0 && j > 0 && k1 < i1 && l1 < j1 && l2 < pimage.pixelWidth && i < pimage.pixelHeight) goto _L2; else goto _L1
_L1:
        return;
_L2:
        i2 = (int)(((float)i3 / (float)k) * 32768F);
        j2 = (int)(((float)j / (float)l) * 32768F);
        if(k1 < 0)
            j = -k1 * i2;
        else
            j = 32768 * l2;
        srcXOffset = j;
        if(l1 < 0)
            i = -l1 * j2;
        else
            i = 32768 * i;
        srcYOffset = i;
        j = k;
        i = k1;
        if(k1 < 0)
        {
            i = 0;
            j = k + k1;
        }
        if(l1 < 0)
        {
            k = l + l1;
            l1 = 0;
        } else
        {
            k = l;
        }
        l = min(j, i1 - i);
        j1 = min(k, j1 - l1);
        i = l1 * i1 + i;
        srcBuffer = pimage.pixels;
        iw = pimage.pixelWidth;
        iw1 = pimage.pixelWidth - 1;
        ih1 = pimage.pixelHeight - 1;
        switch(k2)
        {
        default:
            break;

        case 0: // '\0'
            k = 0;
            j = i;
            i = k;
            do
            {
                if(i >= j1)
                    continue; /* Loop/switch isn't completed */
                filter_new_scanline();
                for(k = 0; k < l; k++)
                {
                    ai[j + k] = filter_bilinear();
                    sX = sX + i2;
                }

                j += i1;
                srcYOffset = srcYOffset + j2;
                i++;
            } while(true);

        case 1: // '\001'
            j = 0;
            while(j < j1) 
            {
                filter_new_scanline();
                for(k = 0; k < l; k++)
                {
                    ai[i + k] = blend_blend(ai[i + k], filter_bilinear());
                    sX = sX + i2;
                }

                i += i1;
                srcYOffset = srcYOffset + j2;
                j++;
            }
            continue; /* Loop/switch isn't completed */

        case 2: // '\002'
            k = 0;
            j = i;
            i = k;
            while(i < j1) 
            {
                filter_new_scanline();
                for(k = 0; k < l; k++)
                {
                    ai[j + k] = blend_add_pin(ai[j + k], filter_bilinear());
                    sX = sX + i2;
                }

                j += i1;
                srcYOffset = srcYOffset + j2;
                i++;
            }
            continue; /* Loop/switch isn't completed */

        case 4: // '\004'
            j = 0;
            while(j < j1) 
            {
                filter_new_scanline();
                for(k = 0; k < l; k++)
                {
                    ai[i + k] = blend_sub_pin(ai[i + k], filter_bilinear());
                    sX = sX + i2;
                }

                i += i1;
                srcYOffset = srcYOffset + j2;
                j++;
            }
            continue; /* Loop/switch isn't completed */

        case 8: // '\b'
            k = 0;
            j = i;
            i = k;
            while(i < j1) 
            {
                filter_new_scanline();
                for(k = 0; k < l; k++)
                {
                    ai[j + k] = blend_lightest(ai[j + k], filter_bilinear());
                    sX = sX + i2;
                }

                j += i1;
                srcYOffset = srcYOffset + j2;
                i++;
            }
            continue; /* Loop/switch isn't completed */

        case 16: // '\020'
            k = 0;
            j = i;
            i = k;
            while(i < j1) 
            {
                filter_new_scanline();
                for(k = 0; k < l; k++)
                {
                    ai[j + k] = blend_darkest(ai[j + k], filter_bilinear());
                    sX = sX + i2;
                }

                j += i1;
                srcYOffset = srcYOffset + j2;
                i++;
            }
            continue; /* Loop/switch isn't completed */

        case 32: // ' '
            j = 0;
            while(j < j1) 
            {
                filter_new_scanline();
                for(k = 0; k < l; k++)
                {
                    ai[i + k] = blend_difference(ai[i + k], filter_bilinear());
                    sX = sX + i2;
                }

                i += i1;
                srcYOffset = srcYOffset + j2;
                j++;
            }
            continue; /* Loop/switch isn't completed */

        case 64: // '@'
            k = 0;
            j = i;
            i = k;
            while(i < j1) 
            {
                filter_new_scanline();
                for(k = 0; k < l; k++)
                {
                    ai[j + k] = blend_exclusion(ai[j + k], filter_bilinear());
                    sX = sX + i2;
                }

                j += i1;
                srcYOffset = srcYOffset + j2;
                i++;
            }
            continue; /* Loop/switch isn't completed */

        case 128: 
            j = 0;
            while(j < j1) 
            {
                filter_new_scanline();
                for(k = 0; k < l; k++)
                {
                    ai[i + k] = blend_multiply(ai[i + k], filter_bilinear());
                    sX = sX + i2;
                }

                i += i1;
                srcYOffset = srcYOffset + j2;
                j++;
            }
            continue; /* Loop/switch isn't completed */

        case 256: 
            k = 0;
            j = i;
            i = k;
            while(i < j1) 
            {
                filter_new_scanline();
                for(k = 0; k < l; k++)
                {
                    ai[j + k] = blend_screen(ai[j + k], filter_bilinear());
                    sX = sX + i2;
                }

                j += i1;
                srcYOffset = srcYOffset + j2;
                i++;
            }
            continue; /* Loop/switch isn't completed */

        case 512: 
            k = 0;
            j = i;
            i = k;
            while(i < j1) 
            {
                filter_new_scanline();
                for(k = 0; k < l; k++)
                {
                    ai[j + k] = blend_overlay(ai[j + k], filter_bilinear());
                    sX = sX + i2;
                }

                j += i1;
                srcYOffset = srcYOffset + j2;
                i++;
            }
            continue; /* Loop/switch isn't completed */

        case 1024: 
            j = 0;
            while(j < j1) 
            {
                filter_new_scanline();
                for(k = 0; k < l; k++)
                {
                    ai[i + k] = blend_hard_light(ai[i + k], filter_bilinear());
                    sX = sX + i2;
                }

                i += i1;
                srcYOffset = srcYOffset + j2;
                j++;
            }
            continue; /* Loop/switch isn't completed */

        case 2048: 
            j = 0;
            while(j < j1) 
            {
                filter_new_scanline();
                for(k = 0; k < l; k++)
                {
                    ai[i + k] = blend_soft_light(ai[i + k], filter_bilinear());
                    sX = sX + i2;
                }

                i += i1;
                srcYOffset = srcYOffset + j2;
                j++;
            }
            continue; /* Loop/switch isn't completed */

        case 4096: 
            k = 0;
            j = i;
            i = k;
            while(i < j1) 
            {
                filter_new_scanline();
                for(k = 0; k < l; k++)
                {
                    ai[j + k] = blend_dodge(ai[j + k], filter_bilinear());
                    sX = sX + i2;
                }

                j += i1;
                srcYOffset = srcYOffset + j2;
                i++;
            }
            continue; /* Loop/switch isn't completed */

        case 8192: 
            k = 0;
            j = i;
            i = k;
            while(i < j1) 
            {
                filter_new_scanline();
                for(k = 0; k < l; k++)
                {
                    ai[j + k] = blend_burn(ai[j + k], filter_bilinear());
                    sX = sX + i2;
                }

                j += i1;
                srcYOffset = srcYOffset + j2;
                i++;
            }
            break;
        }
        if(true) goto _L1; else goto _L3
_L3:
    }

    private int filter_bilinear()
    {
        fracU = sX & 0x7fff;
        ifU = (32767 - fracU) + 1;
        ul = ifU * ifV >> 15;
        ll = ifU - ul;
        ur = ifV - ul;
        lr = 32768 - ul - ll - ur;
        u1 = sX >> 15;
        u2 = min(u1 + 1, iw1);
        cUL = srcBuffer[v1 + u1];
        cUR = srcBuffer[v1 + u2];
        cLL = srcBuffer[v2 + u1];
        cLR = srcBuffer[v2 + u2];
        r = ul * ((cUL & 0xff0000) >> 16) + ll * ((cLL & 0xff0000) >> 16) + ur * ((cUR & 0xff0000) >> 16) + lr * ((cLR & 0xff0000) >> 16) << 1 & 0xff0000;
        g = ul * (cUL & 0xff00) + ll * (cLL & 0xff00) + ur * (cUR & 0xff00) + lr * (cLR & 0xff00) >>> 15 & 0xff00;
        b = ul * (cUL & 0xff) + ll * (cLL & 0xff) + ur * (cUR & 0xff) + lr * (cLR & 0xff) >>> 15;
        a = ul * ((cUL & 0xff000000) >>> 24) + ll * ((cLL & 0xff000000) >>> 24) + ur * ((cUR & 0xff000000) >>> 24) + lr * ((cLR & 0xff000000) >>> 24) << 9 & 0xff000000;
        return a | r | g | b;
    }

    private void filter_new_scanline()
    {
        sX = srcXOffset;
        fracV = srcYOffset & 0x7fff;
        ifV = (32767 - fracV) + 1;
        v1 = (srcYOffset >> 15) * iw;
        v2 = min((srcYOffset >> 15) + 1, ih1) * iw;
    }

    private boolean intersect(int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1)
    {
        int i2 = (k - i) + 1;
        k = (l - j) + 1;
        k1 = (k1 - i1) + 1;
        l = (l1 - j1) + 1;
        boolean flag;
        if(i1 < i)
        {
            i1 = k1 + (i1 - i);
            i = i1;
            if(i1 > i2)
                i = i2;
        } else
        {
            i1 = (i2 + i) - i1;
            i = i1;
            if(k1 <= i1)
                i = k1;
        }
        if(j1 < j)
        {
            l += j1 - j;
            j = l;
            if(l > k)
                j = k;
        } else
        {
            k = (k + j) - j1;
            j = k;
            if(l <= k)
                j = l;
        }
        if(i > 0 && j > 0)
            flag = true;
        else
            flag = false;
        return flag;
    }

    protected static PImage loadTIFF(byte abyte0[])
    {
        boolean flag = false;
        if(abyte0[42] == abyte0[102] && abyte0[43] == abyte0[103]) goto _L2; else goto _L1
_L1:
        PImage pimage;
        System.err.println("Error: Processing can only read its own TIFF files.");
        pimage = null;
_L9:
        return pimage;
_L2:
        int j;
        int l;
        int i1;
        int k1;
        j = abyte0[30];
        l = abyte0[31] & 0xff | (j & 0xff) << 8;
        j = abyte0[42];
        i1 = abyte0[43] & 0xff | (j & 0xff) << 8;
        byte byte0 = abyte0[114];
        j = abyte0[115];
        k1 = abyte0[116];
        k1 = abyte0[117] & 0xff | ((byte0 & 0xff) << 24 | (j & 0xff) << 16 | (k1 & 0xff) << 8);
        if(k1 != l * i1 * 3)
        {
            System.err.println((new StringBuilder()).append("Error: Processing can only read its own TIFF files. (").append(l).append(", ").append(i1).append(")").toString());
            pimage = null;
            continue; /* Loop/switch isn't completed */
        }
        j = 0;
_L5:
        if(j >= TIFF_HEADER.length)
            break; /* Loop/switch isn't completed */
          goto _L3
_L7:
        j++;
        if(true) goto _L5; else goto _L4
_L4:
        break; /* Loop/switch isn't completed */
_L3:
        if(j == 30 || j == 31 || j == 42 || j == 43 || j == 102 || j == 103 || j == 114 || j == 115 || j == 116 || j == 117 || abyte0[j] == TIFF_HEADER[j]) goto _L7; else goto _L6
_L6:
        System.err.println((new StringBuilder()).append("Error: Processing can only read its own TIFF files. (").append(j).append(")").toString());
        pimage = null;
        if(true) goto _L9; else goto _L8
_L8:
        PImage pimage1 = new PImage(l, i1, 1);
        l = 768;
        i1 = k1 / 3;
        int k = ((flag) ? 1 : 0);
        do
        {
            pimage = pimage1;
            if(k >= i1)
                continue;
            int ai[] = pimage1.pixels;
            int j1 = l + 1;
            byte byte1 = abyte0[l];
            int i = j1 + 1;
            j1 = abyte0[j1];
            l = i + 1;
            ai[k] = abyte0[i] & 0xff | ((j1 & 0xff) << 8 | ((byte1 & 0xff) << 16 | 0xff000000));
            k++;
        } while(true);
        if(true) goto _L9; else goto _L10
_L10:
    }

    private static int max(int i, int j)
    {
        if(i <= j)
            i = j;
        return i;
    }

    private static int min(int i, int j)
    {
        if(i >= j)
            i = j;
        return i;
    }

    public void blend(int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1, int i2)
    {
        blend(this, i, j, k, l, i1, j1, k1, l1, i2);
    }

    public void blend(PImage pimage, int i, int j, int k, int l, int i1, int j1, 
            int k1, int l1, int i2)
    {
        int j2 = i + k;
        int k2 = j + l;
        k1 = i1 + k1;
        l1 = j1 + l1;
        loadPixels();
        if(pimage == this)
        {
            if(intersect(i, j, j2, k2, i1, j1, k1, l1))
                blit_resize(get(i, j, k, l), 0, 0, k, l, pixels, pixelWidth, pixelHeight, i1, j1, k1, l1, i2);
            else
                blit_resize(pimage, i, j, j2, k2, pixels, pixelWidth, pixelHeight, i1, j1, k1, l1, i2);
        } else
        {
            pimage.loadPixels();
            blit_resize(pimage, i, j, j2, k2, pixels, pixelWidth, pixelHeight, i1, j1, k1, l1, i2);
        }
        updatePixels();
    }

    protected void blurARGB(float f)
    {
        int ai[];
        int ai1[];
        int ai2[];
        int ai3[];
        int k;
        int l;
        int i = pixels.length;
        ai = new int[i];
        ai1 = new int[i];
        ai2 = new int[i];
        ai3 = new int[i];
        k = 0;
        buildBlurKernel(f);
        l = 0;
_L9:
        if(l >= height) goto _L2; else goto _L1
_L1:
        int i1 = 0;
_L7:
        if(i1 >= width) goto _L4; else goto _L3
_L3:
        int j1;
        int k1;
        j1 = 0;
        k1 = i1 - blurRadius;
        if(k1 >= 0) goto _L6; else goto _L5
_L5:
        int j;
        j = -k1;
        k1 = 0;
_L10:
        int l1;
        int i2;
        int j2;
        int k2;
        l1 = 0;
        i2 = 0;
        j2 = 0;
        k2 = 0;
_L11:
        if(j < blurKernelSize && k1 < width)
            break MISSING_BLOCK_LABEL_186;
        j = k + i1;
        ai3[j] = j1 / k2;
        ai[j] = j2 / k2;
        ai1[j] = i2 / k2;
        ai2[j] = l1 / k2;
        i1++;
          goto _L7
_L6:
        if(k1 < width) goto _L8; else goto _L4
_L4:
        k += width;
        l++;
          goto _L9
_L8:
        j = 0;
          goto _L10
        int l2 = pixels[k1 + k];
        int ai4[] = blurMult[j];
        j1 += ai4[(0xff000000 & l2) >>> 24];
        j2 += ai4[(0xff0000 & l2) >> 16];
        i2 += ai4[(0xff00 & l2) >> 8];
        l1 += ai4[l2 & 0xff];
        k2 += blurKernel[j];
        k1++;
        j++;
          goto _L11
_L2:
        l1 = 0;
        j = -blurRadius;
        k2 = j * width;
        i2 = 0;
_L18:
        if(i2 >= height)
            break MISSING_BLOCK_LABEL_567;
        k1 = 0;
_L16:
        if(k1 >= width) goto _L13; else goto _L12
_L12:
        int j3 = 0;
        if(j >= 0) goto _L15; else goto _L14
_L14:
        k = -j;
        l = k;
        i1 = k1;
_L19:
        int i3;
        int k3;
        i3 = 0;
        k3 = 0;
        j1 = 0;
        j2 = 0;
_L20:
        if(k < blurKernelSize && l < height)
            break MISSING_BLOCK_LABEL_477;
        pixels[k1 + l1] = j3 / j2 << 24 | j1 / j2 << 16 | k3 / j2 << 8 | i3 / j2;
        k1++;
          goto _L16
_L15:
        if(j < height) goto _L17; else goto _L13
_L13:
        l1 += width;
        k2 += width;
        j++;
        i2++;
          goto _L18
_L17:
        k = 0;
        i1 = k1 + k2;
        l = j;
          goto _L19
        int ai5[] = blurMult[k];
        j3 += ai5[ai3[i1]];
        j1 += ai5[ai[i1]];
        k3 += ai5[ai1[i1]];
        i3 += ai5[ai2[i1]];
        j2 += blurKernel[k];
        l++;
        i1 += width;
        k++;
          goto _L20
          goto _L7
    }

    protected void blurAlpha(float f)
    {
        int ai[];
        int i;
        int j;
        ai = new int[pixels.length];
        buildBlurKernel(f);
        i = 0;
        j = 0;
_L9:
        if(i >= height) goto _L2; else goto _L1
_L1:
        int k = 0;
_L7:
        if(k >= width) goto _L4; else goto _L3
_L3:
        int l = k - blurRadius;
        if(l >= 0) goto _L6; else goto _L5
_L5:
        int i1;
        i1 = -l;
        l = 0;
_L10:
        int j1;
        int k1;
        j1 = 0;
        k1 = 0;
_L11:
        if(i1 < blurKernelSize && l < width)
            break MISSING_BLOCK_LABEL_132;
        ai[j + k] = j1 / k1;
        k++;
          goto _L7
_L6:
        if(l < width) goto _L8; else goto _L4
_L4:
        j += width;
        i++;
          goto _L9
_L8:
        i1 = 0;
          goto _L10
        int l1 = pixels[l + j];
        j1 += blurMult[i1][l1 & 0xff];
        k1 += blurKernel[i1];
        l++;
        i1++;
          goto _L11
_L2:
        int i2;
        i1 = -blurRadius;
        k1 = width * i1;
        i2 = 0;
        j1 = 0;
_L18:
        if(i2 >= height)
            break MISSING_BLOCK_LABEL_384;
        l = 0;
_L16:
        if(l >= width) goto _L13; else goto _L12
_L12:
        if(i1 >= 0) goto _L15; else goto _L14
_L14:
        i = -i1;
        j = i;
        k = l;
_L19:
        int j2;
        int k2;
        j2 = 0;
        k2 = 0;
_L20:
        if(i < blurKernelSize && j < height)
            break MISSING_BLOCK_LABEL_339;
        pixels[l + j1] = j2 / k2;
        l++;
          goto _L16
_L15:
        if(i1 < height) goto _L17; else goto _L13
_L13:
        j1 += width;
        k1 += width;
        i1++;
        i2++;
          goto _L18
_L17:
        j = i1;
        k = l + k1;
        i = 0;
          goto _L19
        j2 += blurMult[i][ai[k]];
        k2 += blurKernel[i];
        j++;
        k += width;
        i++;
          goto _L20
          goto _L7
    }

    protected void blurRGB(float f)
    {
        int ai[];
        int ai1[];
        int ai2[];
        int i;
        int j;
        ai = new int[pixels.length];
        ai1 = new int[pixels.length];
        ai2 = new int[pixels.length];
        i = 0;
        buildBlurKernel(f);
        j = 0;
_L9:
        if(j >= height) goto _L2; else goto _L1
_L1:
        int k = 0;
_L7:
        if(k >= width) goto _L4; else goto _L3
_L3:
        int l;
        int i1;
        l = 0;
        i1 = k - blurRadius;
        if(i1 >= 0) goto _L6; else goto _L5
_L5:
        int j1;
        j1 = -i1;
        i1 = 0;
_L10:
        int k1;
        int l1;
        int i2;
        k1 = 0;
        l1 = 0;
        i2 = 0;
_L11:
        if(j1 < blurKernelSize && i1 < width)
            break MISSING_BLOCK_LABEL_180;
        j1 = i + k;
        ai[j1] = l1 / i2;
        ai1[j1] = k1 / i2;
        ai2[j1] = l / i2;
        k++;
          goto _L7
_L6:
        if(i1 < width) goto _L8; else goto _L4
_L4:
        i += width;
        j++;
          goto _L9
_L8:
        j1 = 0;
          goto _L10
        int j2 = pixels[i1 + i];
        int ai3[] = blurMult[j1];
        l1 += ai3[(0xff0000 & j2) >> 16];
        k1 += ai3[(0xff00 & j2) >> 8];
        l += ai3[j2 & 0xff];
        i2 += blurKernel[j1];
        i1++;
        j1++;
          goto _L11
_L2:
        l1 = 0;
        j1 = -blurRadius;
        i2 = j1 * width;
        l = 0;
_L18:
        if(l >= height)
            break MISSING_BLOCK_LABEL_530;
        i1 = 0;
_L16:
        if(i1 >= width) goto _L13; else goto _L12
_L12:
        int l2 = 0;
        if(j1 >= 0) goto _L15; else goto _L14
_L14:
        j = -j1;
        i = j;
        k = i1;
_L19:
        int k2;
        int i3;
        i3 = 0;
        k2 = 0;
        k1 = 0;
_L20:
        if(j < blurKernelSize && i < height)
            break MISSING_BLOCK_LABEL_454;
        pixels[i1 + l1] = 0xff000000 | k2 / k1 << 16 | i3 / k1 << 8 | l2 / k1;
        i1++;
          goto _L16
_L15:
        if(j1 < height) goto _L17; else goto _L13
_L13:
        l1 += width;
        i2 += width;
        j1++;
        l++;
          goto _L18
_L17:
        j = 0;
        k = i1 + i2;
        i = j1;
          goto _L19
        int ai4[] = blurMult[j];
        k2 += ai4[ai[k]];
        i3 += ai4[ai1[k]];
        l2 += ai4[ai2[k]];
        k1 += blurKernel[j];
        i++;
        k += width;
        j++;
          goto _L20
          goto _L7
    }

    protected void buildBlurKernel(float f)
    {
        int i;
        int k;
        i = 248;
        k = (int)(3.5F * f);
        if(k >= 1) goto _L2; else goto _L1
_L1:
        i = 1;
_L8:
        if(blurRadius == i) goto _L4; else goto _L3
_L3:
        int i1;
        blurRadius = i;
        blurKernelSize = blurRadius + 1 << 1;
        blurKernel = new int[blurKernelSize];
        k = blurKernelSize;
        blurMult = new int[k][256];
        k = i - 1;
        i1 = 1;
_L6:
        if(i1 >= i)
            break; /* Loop/switch isn't completed */
        int ai[] = blurKernel;
        int ai2[] = blurKernel;
        int j1 = k * k;
        ai2[k] = j1;
        ai[i + i1] = j1;
        ai = blurMult[i + i1];
        ai2 = blurMult[k];
        for(int k1 = 0; k1 < 256; k1++)
        {
            int l1 = j1 * k1;
            ai2[k1] = l1;
            ai[k1] = l1;
        }

        i1++;
        k--;
        continue; /* Loop/switch isn't completed */
_L2:
        if(k < 248)
            i = k;
        continue; /* Loop/switch isn't completed */
        if(true) goto _L6; else goto _L5
_L5:
        int ai1[] = blurKernel;
        int l = i * i;
        ai1[i] = l;
        ai1 = blurMult[i];
        for(int j = 0; j < 256; j++)
            ai1[j] = l * j;

_L4:
        return;
        if(true) goto _L8; else goto _L7
_L7:
    }

    protected void checkAlpha()
    {
        if(pixels != null) goto _L2; else goto _L1
_L1:
        return;
_L2:
        int i = 0;
        do
        {
            if(i < pixels.length)
            {
label0:
                {
                    if((pixels[i] & 0xff000000) == 0xff000000)
                        break label0;
                    format = 2;
                }
            }
            if(true)
                continue;
            i++;
        } while(true);
        if(true) goto _L1; else goto _L3
_L3:
    }

    public Object clone()
        throws CloneNotSupportedException
    {
        return get();
    }

    public void copy(int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1)
    {
        blend(this, i, j, k, l, i1, j1, k1, l1, 0);
    }

    public void copy(PImage pimage, int i, int j, int k, int l, int i1, int j1, 
            int k1, int l1)
    {
        blend(pimage, i, j, k, l, i1, j1, k1, l1, 0);
    }

    protected void dilate(boolean flag)
    {
        int i;
        boolean flag1;
        int k;
        int ai[];
        i = 0;
        flag1 = false;
        k = pixels.length;
        ai = new int[k];
        if(flag) goto _L2; else goto _L1
_L1:
        i = ((flag1) ? 1 : 0);
        while(i < k) 
        {
            int l = i + width;
            int j = i;
            while(j < l) 
            {
                int i1 = pixels[j];
                int j1 = j - 1;
                int k1 = j + 1;
                int i2 = j - width;
                int j2 = width + j;
                if(j1 < i)
                    j1 = j;
                int k2 = k1;
                if(k1 >= l)
                    k2 = j;
                k1 = i2;
                if(i2 < 0)
                    k1 = 0;
                i2 = j2;
                if(j2 >= k)
                    i2 = j;
                j2 = pixels[k1];
                j1 = pixels[j1];
                i2 = pixels[i2];
                int l2 = pixels[k2];
                k2 = (i1 >> 16 & 0xff) * 77 + (i1 >> 8 & 0xff) * 151 + (i1 & 0xff) * 28;
                int i3 = (j1 >> 16 & 0xff) * 77 + (j1 >> 8 & 0xff) * 151 + (j1 & 0xff) * 28;
                k1 = (l2 & 0xff) * 28 + ((l2 >> 16 & 0xff) * 77 + (l2 >> 8 & 0xff) * 151);
                int j3 = (j2 >> 16 & 0xff) * 77 + (j2 >> 8 & 0xff) * 151 + (j2 & 0xff) * 28;
                if(i3 > k2)
                {
                    k2 = j1;
                    j1 = i3;
                } else
                {
                    j1 = k2;
                    k2 = i1;
                }
                if(k1 > j1)
                {
                    j1 = l2;
                    k2 = k1;
                } else
                {
                    int l1 = k2;
                    k2 = j1;
                    j1 = l1;
                }
                if(j3 > k2)
                {
                    k2 = j3;
                    j1 = j2;
                }
                if((i2 >> 16 & 0xff) * 77 + (i2 >> 8 & 0xff) * 151 + (i2 & 0xff) * 28 > k2)
                    k2 = i2;
                else
                    k2 = j1;
                ai[j] = k2;
                j++;
            }
            i = j;
        }
          goto _L3
_L5:
        i = j;
_L2:
        if(i >= k) goto _L3; else goto _L4
_L4:
        l = i + width;
        j = i;
        while(j < l) 
        {
            i1 = pixels[j];
            j1 = j - 1;
            k1 = j + 1;
            i2 = j - width;
            j2 = width + j;
            if(j1 < i)
                j1 = j;
            k2 = k1;
            if(k1 >= l)
                k2 = j;
            k1 = i2;
            if(i2 < 0)
                k1 = 0;
            i2 = j2;
            if(j2 >= k)
                i2 = j;
            j2 = pixels[k1];
            i3 = pixels[j1];
            i2 = pixels[i2];
            l2 = pixels[k2];
            k2 = (i1 >> 16 & 0xff) * 77 + (i1 >> 8 & 0xff) * 151 + (i1 & 0xff) * 28;
            j1 = (i3 >> 16 & 0xff) * 77 + (i3 >> 8 & 0xff) * 151 + (i3 & 0xff) * 28;
            k1 = (l2 & 0xff) * 28 + ((l2 >> 16 & 0xff) * 77 + (l2 >> 8 & 0xff) * 151);
            j3 = (j2 >> 16 & 0xff) * 77 + (j2 >> 8 & 0xff) * 151 + (j2 & 0xff) * 28;
            if(j1 < k2)
            {
                k2 = i3;
            } else
            {
                j1 = k2;
                k2 = i1;
            }
            if(k1 < j1)
            {
                j1 = l2;
                k2 = k1;
            } else
            {
                k1 = j1;
                j1 = k2;
                k2 = k1;
            }
            if(j3 < k2)
            {
                k2 = j3;
                j1 = j2;
            }
            if((i2 >> 16 & 0xff) * 77 + (i2 >> 8 & 0xff) * 151 + (i2 & 0xff) * 28 < k2)
                k2 = i2;
            else
                k2 = j1;
            ai[j] = k2;
            j++;
        }
        if(true) goto _L5; else goto _L3
_L3:
        System.arraycopy(ai, 0, pixels, 0, k);
        return;
    }

    public void filter(int i)
    {
        boolean flag;
        int j;
        int k;
        boolean flag1;
        flag = false;
        j = 0;
        k = 0;
        flag1 = false;
        loadPixels();
        i;
        JVM INSTR tableswitch 1 18: default 100
    //                   1 312
    //                   2 100
    //                   3 100
    //                   4 100
    //                   5 100
    //                   6 100
    //                   7 100
    //                   8 100
    //                   9 100
    //                   10 100
    //                   11 105
    //                   12 115
    //                   13 268
    //                   14 100
    //                   15 301
    //                   16 355
    //                   17 367
    //                   18 375;
           goto _L1 _L2 _L1 _L1 _L1 _L1 _L1 _L1 _L1 _L1 _L1 _L3 _L4 _L5 _L1 _L6 _L7 _L8 _L9
_L1:
        updatePixels();
        return;
_L3:
        filter(11, 1.0F);
        continue; /* Loop/switch isn't completed */
_L4:
        i = ((flag) ? 1 : 0);
        if(format == 4)
        {
            for(i = ((flag1) ? 1 : 0); i < pixels.length; i++)
            {
                j = 255 - pixels[i];
                pixels[i] = j | (j << 16 | 0xff000000 | j << 8);
            }

            format = 1;
        } else
        {
            while(i < pixels.length) 
            {
                k = pixels[i];
                j = (k >> 16 & 0xff) * 77 + (k >> 8 & 0xff) * 151 + (k & 0xff) * 28 >> 8;
                pixels[i] = k & 0xff000000 | j << 16 | j << 8 | j;
                i++;
            }
        }
        continue; /* Loop/switch isn't completed */
_L5:
        while(j < pixels.length) 
        {
            int ai[] = pixels;
            ai[j] = ai[j] ^ 0xffffff;
            j++;
        }
        continue; /* Loop/switch isn't completed */
_L6:
        throw new RuntimeException("Use filter(POSTERIZE, int levels) instead of filter(POSTERIZE)");
_L2:
        for(; k < pixels.length; k++)
        {
            int ai1[] = pixels;
            ai1[k] = ai1[k] | 0xff000000;
        }

        format = 1;
        continue; /* Loop/switch isn't completed */
_L7:
        filter(16, 0.5F);
        continue; /* Loop/switch isn't completed */
_L8:
        dilate(true);
        continue; /* Loop/switch isn't completed */
_L9:
        dilate(false);
        if(true) goto _L1; else goto _L10
_L10:
    }

    public void filter(int i, float f)
    {
        boolean flag;
        flag = false;
        loadPixels();
        i;
        JVM INSTR tableswitch 11 18: default 52
    //                   11 57
    //                   12 97
    //                   13 108
    //                   14 119
    //                   15 130
    //                   16 301
    //                   17 407
    //                   18 418;
           goto _L1 _L2 _L3 _L4 _L5 _L6 _L7 _L8 _L9
_L1:
        break; /* Loop/switch isn't completed */
_L9:
        break MISSING_BLOCK_LABEL_418;
_L10:
        updatePixels();
        return;
_L2:
        if(format == 4)
            blurAlpha(f);
        else
        if(format == 2)
            blurARGB(f);
        else
            blurRGB(f);
        continue; /* Loop/switch isn't completed */
_L3:
        throw new RuntimeException("Use filter(GRAY) instead of filter(GRAY, param)");
_L4:
        throw new RuntimeException("Use filter(INVERT) instead of filter(INVERT, param)");
_L5:
        throw new RuntimeException("Use filter(OPAQUE) instead of filter(OPAQUE, param)");
_L6:
        int l = (int)f;
        if(l < 2 || l > 255)
            throw new RuntimeException("Levels must be between 2 and 255 for filter(POSTERIZE, levels)");
        int j1 = l - 1;
        i = ((flag) ? 1 : 0);
        while(i < pixels.length) 
        {
            int j = pixels[i];
            int l1 = pixels[i];
            int i2 = pixels[i];
            j = (((j >> 16 & 0xff) * l >> 8) * 255) / j1;
            l1 = (((l1 >> 8 & 0xff) * l >> 8) * 255) / j1;
            i2 = (((i2 & 0xff) * l >> 8) * 255) / j1;
            pixels[i] = j << 16 | pixels[i] & 0xff000000 | l1 << 8 | i2;
            i++;
        }
        if(true) goto _L10; else goto _L7
_L7:
        int i1 = (int)(255F * f);
        i = 0;
        while(i < pixels.length) 
        {
            int k = Math.max((pixels[i] & 0xff0000) >> 16, Math.max((pixels[i] & 0xff00) >> 8, pixels[i] & 0xff));
            int ai[] = pixels;
            int k1 = pixels[i];
            if(k < i1)
                k = 0;
            else
                k = 0xffffff;
            ai[i] = k | k1 & 0xff000000;
            i++;
        }
        if(true) goto _L10; else goto _L8
_L8:
        throw new RuntimeException("Use filter(ERODE) instead of filter(ERODE, param)");
        throw new RuntimeException("Use filter(DILATE) instead of filter(DILATE, param)");
    }

    public int get(int i, int j)
    {
        boolean flag;
        int k;
        flag = false;
        k = ((flag) ? 1 : 0);
        if(i < 0) goto _L2; else goto _L1
_L1:
        k = ((flag) ? 1 : 0);
        if(j < 0) goto _L2; else goto _L3
_L3:
        k = ((flag) ? 1 : 0);
        if(i >= width) goto _L2; else goto _L4
_L4:
        if(j < height) goto _L6; else goto _L5
_L5:
        k = ((flag) ? 1 : 0);
_L2:
        return k;
_L6:
        if(pixels != null)
            break; /* Loop/switch isn't completed */
        k = bitmap.getPixel(i, j);
        if(true) goto _L2; else goto _L7
_L7:
        switch(format)
        {
        case 3: // '\003'
        default:
            k = ((flag) ? 1 : 0);
            break;

        case 1: // '\001'
            k = pixels[width * j + i] | 0xff000000;
            break;

        case 2: // '\002'
            k = pixels[width * j + i];
            break;

        case 4: // '\004'
            k = pixels[width * j + i] << 24 | 0xffffff;
            break;
        }
        if(true) goto _L2; else goto _L8
_L8:
    }

    public PImage get()
    {
        return get(0, 0, width, height);
    }

    public PImage get(int i, int j, int k, int l)
    {
        int i1 = 0;
        int k1;
        int j2;
        int k2;
        int l2;
        int i3;
        int j3;
        PImage pimage;
        if(i < 0)
        {
            int j1 = k + i;
            j2 = -i;
            k2 = 1;
            l2 = 0;
            i = j1;
        } else
        {
            k2 = 0;
            j2 = 0;
            int i2 = k;
            l2 = i;
            i = i2;
        }
        if(j < 0)
        {
            i3 = -j;
            k2 = l + j;
            j3 = 0;
            j = 1;
        } else
        {
            i3 = 0;
            int l1 = l;
            j3 = j;
            j = k2;
            k2 = l1;
        }
        k1 = j;
        j = i;
        if(l2 + i > width)
        {
            j = width - l2;
            k1 = 1;
        }
        if(j3 + k2 > height)
        {
            i = height - j3;
            k2 = 1;
        } else
        {
            i = k2;
            k2 = k1;
        }
        if(j < 0)
            j = 0;
        if(i < 0)
            i = i1;
        i1 = format;
        k1 = i1;
        if(k2 != 0)
        {
            k1 = i1;
            if(format == 1)
                k1 = 2;
        }
        pimage = new PImage(k, l, k1);
        pimage.parent = parent;
        if(j > 0 && i > 0)
            getImpl(l2, j3, j, i, pimage, j2, i3);
        return pimage;
    }

    protected void getImpl(int i, int j, int k, int l, PImage pimage, int i1, int j1)
    {
        if(pixels == null)
        {
            bitmap.getPixels(pimage.pixels, pimage.width * j1 + i1, pimage.width, i, j, k, l);
        } else
        {
            i = width * j + i;
            j = pimage.width * j1 + i1;
            j1 = 0;
            i1 = i;
            i = j1;
            while(i < l) 
            {
                System.arraycopy(pixels, i1, pimage.pixels, j, k);
                i1 += width;
                j += pimage.width;
                i++;
            }
        }
    }

    public int getModifiedX1()
    {
        return mx1;
    }

    public int getModifiedX2()
    {
        return mx2;
    }

    public int getModifiedY1()
    {
        return my1;
    }

    public int getModifiedY2()
    {
        return my2;
    }

    public Object getNative()
    {
        return bitmap;
    }

    public void init(int i, int j, int k)
    {
        width = i;
        height = j;
        pixels = new int[i * j];
        format = k;
        pixelWidth = pixelDensity * i;
        pixelHeight = pixelDensity * j;
        pixels = new int[pixelWidth * pixelHeight];
    }

    public boolean isLoaded()
    {
        return loaded;
    }

    public boolean isModified()
    {
        return modified;
    }

    public void loadPixels()
    {
        if(pixels == null || pixels.length != width * height)
            pixels = new int[width * height];
        if(bitmap != null)
            if(modified)
            {
                if(!bitmap.isMutable())
                    bitmap = bitmap.copy(android.graphics.Bitmap.Config.ARGB_8888, true);
                bitmap.setPixels(pixels, 0, width, mx1, my1, mx2 - mx1, my2 - my1);
                modified = false;
            } else
            {
                bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            }
        setLoaded();
    }

    public void mask(PImage pimage)
    {
        if(pimage.pixels == null)
        {
            pimage.loadPixels();
            mask(pimage.pixels);
            pimage.pixels = null;
        } else
        {
            mask(pimage.pixels);
        }
    }

    public void mask(int ai[])
    {
        loadPixels();
        if(ai.length != pixels.length)
            throw new RuntimeException("The PImage used with mask() must be the same size as the applet.");
        for(int i = 0; i < pixels.length; i++)
            pixels[i] = (ai[i] & 0xff) << 24 | pixels[i] & 0xffffff;

        format = 2;
        updatePixels();
    }

    public void resize(int i, int j)
    {
        if(i <= 0 && j <= 0)
            throw new IllegalArgumentException("width or height must be > 0 for resize");
        if(i != 0) goto _L2; else goto _L1
_L1:
        int k;
        int l;
        k = (int)(((float)j / (float)height) * (float)width);
        l = j;
_L4:
        bitmap = Bitmap.createScaledBitmap(bitmap, k, l, true);
        width = k;
        height = l;
        updatePixels();
        return;
_L2:
        k = i;
        l = j;
        if(j == 0)
        {
            l = (int)(((float)i / (float)width) * (float)height);
            k = i;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public boolean save(String s)
    {
        boolean flag;
        boolean flag1;
        Object obj;
        flag = false;
        loadPixels();
        flag1 = flag;
        obj = s;
        BufferedOutputStream bufferedoutputstream = JVM INSTR new #475 <Class BufferedOutputStream>;
        flag1 = flag;
        obj = s;
        bufferedoutputstream.BufferedOutputStream(parent.createOutput(s), 16384);
        flag1 = flag;
        obj = s;
        String s1 = s.toLowerCase();
        flag1 = flag;
        obj = s;
        String s2 = s1.substring(s1.lastIndexOf('.') + 1);
        flag1 = flag;
        obj = s;
        if(s2.equals("jpg")) goto _L2; else goto _L1
_L1:
        flag1 = flag;
        obj = s;
        if(!s2.equals("jpeg")) goto _L3; else goto _L2
_L2:
        flag1 = flag;
        obj = s;
        flag = Bitmap.createBitmap(pixels, width, height, android.graphics.Bitmap.Config.ARGB_8888).compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, bufferedoutputstream);
_L5:
        flag1 = flag;
        obj = s;
        bufferedoutputstream.flush();
        flag1 = flag;
        obj = s;
        Object obj1;
        try
        {
            bufferedoutputstream.close();
        }
        // Misplaced declaration of an exception variable
        catch(String s)
        {
            s.printStackTrace();
            flag = flag1;
            s = ((String) (obj));
        }
        if(!flag)
            System.err.println((new StringBuilder()).append("Could not write the image to ").append(s).toString());
        return flag;
_L3:
        flag1 = flag;
        obj = s;
        if(!s2.equals("png"))
            break MISSING_BLOCK_LABEL_240;
        flag1 = flag;
        obj = s;
        flag = Bitmap.createBitmap(pixels, width, height, android.graphics.Bitmap.Config.ARGB_8888).compress(android.graphics.Bitmap.CompressFormat.PNG, 100, bufferedoutputstream);
        continue; /* Loop/switch isn't completed */
        flag1 = flag;
        obj = s;
        if(!s2.equals("tga"))
            break MISSING_BLOCK_LABEL_271;
        flag1 = flag;
        obj = s;
        flag = saveTGA(bufferedoutputstream);
        continue; /* Loop/switch isn't completed */
        obj1 = s;
        flag1 = flag;
        obj = s;
        if(s2.equals("tif"))
            break MISSING_BLOCK_LABEL_351;
        obj1 = s;
        flag1 = flag;
        obj = s;
        if(s2.equals("tiff"))
            break MISSING_BLOCK_LABEL_351;
        flag1 = flag;
        obj = s;
        obj1 = JVM INSTR new #293 <Class StringBuilder>;
        flag1 = flag;
        obj = s;
        ((StringBuilder) (obj1)).StringBuilder();
        flag1 = flag;
        obj = s;
        obj1 = ((StringBuilder) (obj1)).append(s).append(".tif").toString();
        flag1 = flag;
        obj = obj1;
        flag = saveTIFF(bufferedoutputstream);
        s = ((String) (obj1));
        if(true) goto _L5; else goto _L4
_L4:
    }

    protected boolean saveTGA(OutputStream outputstream)
    {
        boolean flag;
        byte abyte0[];
        int i;
        int j;
        int k;
        int l;
        int i1;
        flag = true;
        abyte0 = new byte[18];
        if(format == 4)
        {
            abyte0[2] = (byte)11;
            abyte0[16] = (byte)8;
            abyte0[17] = (byte)40;
        } else
        if(format == 1)
        {
            abyte0[2] = (byte)10;
            abyte0[16] = (byte)24;
            abyte0[17] = (byte)32;
        } else
        if(format == 2)
        {
            abyte0[2] = (byte)10;
            abyte0[16] = (byte)32;
            abyte0[17] = (byte)40;
        } else
        {
            throw new RuntimeException("Image format not recognized inside save()");
        }
        abyte0[12] = (byte)(byte)(width & 0xff);
        abyte0[13] = (byte)(byte)(width >> 8);
        abyte0[14] = (byte)(byte)(height & 0xff);
        abyte0[15] = (byte)(byte)(height >> 8);
        outputstream.write(abyte0);
        i = height * width;
        abyte0 = new int[128];
        if(format != 4)
            break MISSING_BLOCK_LABEL_890;
        j = 0;
_L9:
        if(j >= i) goto _L2; else goto _L1
_L1:
        k = pixels[j] & 0xff;
        abyte0[0] = k;
        l = 1;
_L10:
        if(j + l >= i) goto _L4; else goto _L3
_L3:
        if(k == (pixels[j + l] & 0xff) && l != 128) goto _L6; else goto _L5
_L5:
        if(l > 1)
            i1 = 1;
        else
            i1 = 0;
_L35:
        if(i1 == 0) goto _L8; else goto _L7
_L7:
        outputstream.write(l - 1 | 0x80);
        outputstream.write(k);
        k = l;
_L14:
        j = k + j;
          goto _L9
_L6:
        l++;
          goto _L10
_L8:
        i1 = 1;
_L12:
        l = i1;
        if(j + i1 >= i)
            break MISSING_BLOCK_LABEL_405;
        int j1 = pixels[j + i1] & 0xff;
        if((k == j1 || i1 >= 128) && i1 >= 3)
            break; /* Loop/switch isn't completed */
        abyte0[i1] = j1;
        i1++;
        k = j1;
        if(true) goto _L12; else goto _L11
_L11:
        l = i1;
        if(k == j1)
            l = i1 - 2;
        outputstream.write(l - 1);
        i1 = 0;
_L15:
        k = l;
        if(i1 >= l) goto _L14; else goto _L13
_L13:
        outputstream.write(abyte0[i1]);
        i1++;
          goto _L15
_L25:
        if(j >= i) goto _L2; else goto _L16
_L16:
        k = pixels[j];
        abyte0[0] = k;
        l = 1;
_L26:
        if(j + l >= i) goto _L18; else goto _L17
_L17:
        if(k == pixels[j + l] && l != 128) goto _L20; else goto _L19
_L19:
        if(l > 1)
            i1 = 1;
        else
            i1 = 0;
_L34:
        if(i1 == 0) goto _L22; else goto _L21
_L21:
        outputstream.write(l - 1 | 0x80);
        outputstream.write(k & 0xff);
        outputstream.write(k >> 8 & 0xff);
        outputstream.write(k >> 16 & 0xff);
        if(format != 2) goto _L24; else goto _L23
_L23:
        outputstream.write(k >>> 24 & 0xff);
        i1 = l;
_L30:
        j = i1 + j;
          goto _L25
_L20:
        l++;
          goto _L26
_L22:
        i1 = 1;
_L28:
        l = i1;
        if(j + i1 >= i)
            break MISSING_BLOCK_LABEL_702;
        if((k == pixels[j + i1] || i1 >= 128) && i1 >= 3)
            break; /* Loop/switch isn't completed */
        k = pixels[j + i1];
        abyte0[i1] = k;
        i1++;
        if(true) goto _L28; else goto _L27
_L27:
        l = i1;
        if(k == pixels[j + i1])
            l = i1 - 2;
        outputstream.write(l - 1);
        if(format != 2)
            break MISSING_BLOCK_LABEL_793;
        k = 0;
_L31:
        i1 = l;
        if(k >= l) goto _L30; else goto _L29
_L29:
        i1 = abyte0[k];
        outputstream.write(i1 & 0xff);
        outputstream.write(i1 >> 8 & 0xff);
        outputstream.write(i1 >> 16 & 0xff);
        outputstream.write(i1 >>> 24 & 0xff);
        k++;
          goto _L31
        k = 0;
_L33:
        i1 = l;
        if(k >= l) goto _L30; else goto _L32
_L32:
        i1 = abyte0[k];
        outputstream.write(i1 & 0xff);
        outputstream.write(i1 >> 8 & 0xff);
        outputstream.write(i1 >> 16 & 0xff);
        k++;
          goto _L33
_L2:
        try
        {
            outputstream.flush();
        }
        // Misplaced declaration of an exception variable
        catch(OutputStream outputstream)
        {
            outputstream.printStackTrace();
            flag = false;
        }
        return flag;
_L24:
        i1 = l;
          goto _L30
_L18:
        i1 = 0;
          goto _L34
_L4:
        i1 = 0;
          goto _L35
        j = 0;
          goto _L25
    }

    protected boolean saveTIFF(OutputStream outputstream)
    {
        boolean flag = false;
        byte abyte0[];
        int i;
        abyte0 = new byte[768];
        System.arraycopy(TIFF_HEADER, 0, abyte0, 0, TIFF_HEADER.length);
        abyte0[30] = (byte)(width >> 8 & 0xff);
        abyte0[31] = (byte)(width & 0xff);
        i = (byte)(height >> 8 & 0xff);
        abyte0[102] = (byte)i;
        abyte0[42] = (byte)i;
        i = (byte)(height & 0xff);
        abyte0[103] = (byte)i;
        abyte0[43] = (byte)i;
        i = width * height * 3;
        abyte0[114] = (byte)(i >> 24 & 0xff);
        abyte0[115] = (byte)(i >> 16 & 0xff);
        abyte0[116] = (byte)(i >> 8 & 0xff);
        abyte0[117] = (byte)(i & 0xff);
        outputstream.write(abyte0);
        i = 0;
_L2:
        if(i >= pixels.length)
            break; /* Loop/switch isn't completed */
        outputstream.write(pixels[i] >> 16 & 0xff);
        outputstream.write(pixels[i] >> 8 & 0xff);
        outputstream.write(pixels[i] & 0xff);
        i++;
        if(true) goto _L2; else goto _L1
_L1:
        outputstream.flush();
        flag = true;
_L4:
        return flag;
        outputstream;
        outputstream.printStackTrace();
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void set(int i, int j, int k)
    {
        if(pixels != null) goto _L2; else goto _L1
_L1:
        bitmap.setPixel(i, j, k);
_L4:
        return;
_L2:
        if(i >= 0 && j >= 0 && i < width && j < height)
        {
            pixels[width * j + i] = k;
            updatePixelsImpl(i, j, 1, 1);
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void set(int i, int j, PImage pimage)
    {
        if(pimage.format == 4)
            throw new IllegalArgumentException("set() not available for ALPHA images");
        int k = pimage.width;
        int l = pimage.height;
        int i1;
        int j1;
        int l1;
        if(i < 0)
        {
            i1 = 0 - i;
            i = k + i;
            j1 = 0;
        } else
        {
            i1 = 0;
            j1 = i;
            i = k;
        }
        if(j < 0)
        {
            int k1 = 0 - j;
            j = l + j;
            k = 0;
            l = k1;
        } else
        {
            boolean flag = false;
            k = j;
            j = l;
            l = ((flag) ? 1 : 0);
        }
        l1 = i;
        if(j1 + i > width)
            l1 = width - j1;
        if(k + j > height)
            i = height - k;
        else
            i = j;
        if(l1 > 0 && i > 0)
            setImpl(pimage, i1, l, l1, i, j1, k);
    }

    protected void setImpl(PImage pimage, int i, int j, int k, int l, int i1, int j1)
    {
        if(pimage.pixels == null)
            pimage.loadPixels();
        if(pixels == null)
        {
            if(!bitmap.isMutable())
                bitmap = bitmap.copy(android.graphics.Bitmap.Config.ARGB_8888, true);
            int k1 = pimage.width;
            bitmap.setPixels(pimage.pixels, k1 * j + i, pimage.width, i1, j1, k, l);
        } else
        {
            int j2 = pimage.width;
            int l1 = width;
            i = j2 * j + i;
            j2 = l1 * j1 + i1;
            for(int i2 = j; i2 < j + l; i2++)
            {
                System.arraycopy(pimage.pixels, i, pixels, j2, k);
                i += pimage.width;
                j2 += width;
            }

            updatePixelsImpl(i1, j1, k, l);
        }
    }

    public void setLoaded()
    {
        loaded = true;
    }

    public void setLoaded(boolean flag)
    {
        loaded = flag;
    }

    public void setModified()
    {
        modified = true;
    }

    public void setModified(boolean flag)
    {
        modified = flag;
    }

    public void updatePixels()
    {
        updatePixelsImpl(0, 0, width, height);
    }

    public void updatePixels(int i, int j, int k, int l)
    {
        updatePixelsImpl(i, j, k, l);
    }

    protected void updatePixelsImpl(int i, int j, int k, int l)
    {
        k = i + k;
        l = j + l;
        if(modified) goto _L2; else goto _L1
_L1:
        mx1 = PApplet.max(0, i);
        mx2 = PApplet.min(width, k);
        my1 = PApplet.max(0, j);
        my2 = PApplet.min(height, l);
        modified = true;
_L4:
        return;
_L2:
        if(i < mx1)
            mx1 = PApplet.max(0, i);
        if(i > mx2)
            mx2 = PApplet.min(width, i);
        if(j < my1)
            my1 = PApplet.max(0, j);
        if(j > my2)
            my2 = PApplet.min(height, j);
        if(k < mx1)
            mx1 = PApplet.max(0, k);
        if(k > mx2)
            mx2 = PApplet.min(width, k);
        if(l < my1)
            my1 = PApplet.max(0, l);
        if(l > my2)
            my2 = PApplet.min(height, l);
        if(true) goto _L4; else goto _L3
_L3:
    }

    public static final int ALPHA_MASK = 0xff000000;
    public static final int BLUE_MASK = 255;
    private static final int GN_MASK = 65280;
    public static final int GREEN_MASK = 65280;
    static final int PRECISIONB = 15;
    static final int PRECISIONF = 32768;
    static final int PREC_ALPHA_SHIFT = 9;
    static final int PREC_MAXVAL = 32767;
    static final int PREC_RED_SHIFT = 1;
    private static final int RB_MASK = 0xff00ff;
    public static final int RED_MASK = 0xff0000;
    static final String TIFF_ERROR = "Error: Processing can only read its own TIFF files.";
    static byte TIFF_HEADER[] = {
        77, 77, 0, 42, 0, 0, 0, 8, 0, 9, 
        0, -2, 0, 4, 0, 0, 0, 1, 0, 0, 
        0, 0, 1, 0, 0, 3, 0, 0, 0, 1, 
        0, 0, 0, 0, 1, 1, 0, 3, 0, 0, 
        0, 1, 0, 0, 0, 0, 1, 2, 0, 3, 
        0, 0, 0, 3, 0, 0, 0, 122, 1, 6, 
        0, 3, 0, 0, 0, 1, 0, 2, 0, 0, 
        1, 17, 0, 4, 0, 0, 0, 1, 0, 0, 
        3, 0, 1, 21, 0, 3, 0, 0, 0, 1, 
        0, 3, 0, 0, 1, 22, 0, 3, 0, 0, 
        0, 1, 0, 0, 0, 0, 1, 23, 0, 4, 
        0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 8, 0, 8, 0, 8
    };
    private int a;
    private int b;
    protected Bitmap bitmap;
    private int blurKernel[];
    private int blurKernelSize;
    private int blurMult[][];
    private int blurRadius;
    private int cLL;
    private int cLR;
    private int cUL;
    private int cUR;
    protected HashMap cacheMap;
    public int format;
    private int fracU;
    private int fracV;
    private int g;
    public int height;
    private int ifU;
    private int ifV;
    private int ih1;
    private int iw;
    private int iw1;
    private int ll;
    public boolean loaded;
    private int lr;
    protected boolean modified;
    protected int mx1;
    protected int mx2;
    protected int my1;
    protected int my2;
    protected HashMap paramMap;
    public PApplet parent;
    public int pixelDensity;
    public int pixelHeight;
    public int pixelWidth;
    public int pixels[];
    private int r;
    private int sX;
    private int sY;
    protected String saveImageFormats[];
    private int srcBuffer[];
    private int srcXOffset;
    private int srcYOffset;
    private int u1;
    private int u2;
    private int ul;
    private int ur;
    private int v1;
    private int v2;
    public int width;

}
