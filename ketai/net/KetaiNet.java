// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package ketai.net;

import java.net.*;
import java.util.Enumeration;
import processing.core.PApplet;

public class KetaiNet
{

    public KetaiNet()
    {
    }

    public static String getIP()
    {
        Object obj;
        String s;
        String s1;
        obj = "0.0.0.0";
        s = ((String) (obj));
        s1 = ((String) (obj));
        Enumeration enumeration = NetworkInterface.getNetworkInterfaces();
_L4:
        s = ((String) (obj));
        s1 = ((String) (obj));
        if(enumeration.hasMoreElements()) goto _L2; else goto _L1
_L1:
        return ((String) (obj));
_L2:
        s = ((String) (obj));
        s1 = ((String) (obj));
        Enumeration enumeration1 = ((NetworkInterface)enumeration.nextElement()).getInetAddresses();
        String s2 = ((String) (obj));
_L6:
        obj = s2;
        s = s2;
        s1 = s2;
        if(!enumeration1.hasMoreElements()) goto _L4; else goto _L3
_L3:
        s = s2;
        s1 = s2;
        obj = (InetAddress)enumeration1.nextElement();
        s = s2;
        s1 = s2;
        if(((InetAddress) (obj)).isLoopbackAddress() || s2 != "0.0.0.0") goto _L6; else goto _L5
_L5:
        s = s2;
        s1 = s2;
        if(!((InetAddress) (obj)).getHostAddress().matches("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b")) goto _L6; else goto _L7
_L7:
        s = s2;
        s1 = s2;
        s2 = ((InetAddress) (obj)).getHostAddress();
          goto _L6
        Object obj1;
        obj1;
        PApplet.println((new StringBuilder("SocketException:")).append(((SocketException) (obj1)).toString()).toString());
        obj1 = s;
          goto _L1
        obj1;
        PApplet.println("Failed to get any network interfaces...");
        obj1 = s1;
          goto _L1
    }
}
