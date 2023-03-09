// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package ketai.net;

import oscP5.OscMessage;

public class KetaiOSCMessage extends OscMessage
{

    public KetaiOSCMessage(byte abyte0[])
    {
        super("");
        parseMessage(abyte0);
    }

    public boolean isValid()
    {
        return isValid;
    }
}
