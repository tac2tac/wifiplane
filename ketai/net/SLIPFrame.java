// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package ketai.net;

import java.io.ByteArrayOutputStream;

public class SLIPFrame
{

    public SLIPFrame()
    {
    }

    public static byte[] createFrame(byte abyte0[])
    {
        ByteArrayOutputStream bytearrayoutputstream;
        int i;
        bytearrayoutputstream = new ByteArrayOutputStream(abyte0.length * 2);
        i = 0;
_L2:
        if(i >= abyte0.length)
            return bytearrayoutputstream.toByteArray();
        if(abyte0[i] != END)
            break; /* Loop/switch isn't completed */
        bytearrayoutputstream.write(ESC);
        bytearrayoutputstream.write(ESC_END);
_L3:
        i++;
        if(true) goto _L2; else goto _L1
_L1:
        if(abyte0[i] == ESC)
        {
            bytearrayoutputstream.write(ESC);
            bytearrayoutputstream.write(ESC_ESC);
        } else
        {
            bytearrayoutputstream.write(abyte0[i]);
        }
          goto _L3
        if(true) goto _L2; else goto _L4
_L4:
    }

    public static byte[] parseFrame(byte abyte0[])
    {
        ByteArrayOutputStream bytearrayoutputstream;
        int i;
        bytearrayoutputstream = new ByteArrayOutputStream(abyte0.length);
        i = 0;
_L2:
        int j;
        if(i >= abyte0.length)
            return bytearrayoutputstream.toByteArray();
        j = i;
        if(abyte0[i] == ESC)
        {
            j = i;
            if(i + 1 < abyte0.length)
            {
                j = i + 1;
                if(abyte0[j] != ESC_END)
                    break; /* Loop/switch isn't completed */
                bytearrayoutputstream.write(END);
            }
        }
_L3:
        i = j + 1;
        if(true) goto _L2; else goto _L1
_L1:
        if(abyte0[j] == ESC_ESC)
            bytearrayoutputstream.write(ESC);
        else
            bytearrayoutputstream.write(abyte0[j]);
          goto _L3
        if(true) goto _L2; else goto _L4
_L4:
    }

    public static byte END = (byte)-64;
    public static byte ESC = (byte)-37;
    public static byte ESC_END = (byte)-36;
    public static byte ESC_ESC = (byte)-35;

}
