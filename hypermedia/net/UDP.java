// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package hypermedia.net;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import processing.core.PApplet;

public class UDP
    implements Runnable
{

    public UDP(Object obj)
    {
        this(obj, 0);
    }

    public UDP(Object obj, int i)
    {
        this(obj, i, null);
    }

    public UDP(Object obj, int i, String s)
    {
        ucSocket = null;
        mcSocket = null;
        log = false;
        listen = false;
        timeout = 0;
        size = 65507;
        group = null;
        thread = null;
        owner = null;
        receiveHandler = "receive";
        timeoutHandler = "timeout";
        header = "";
        owner = obj;
        Object obj1;
        SecurityException securityexception;
        try
        {
            if(obj instanceof PApplet)
                ((PApplet)obj).registerMethod("dispose", this);
        }
        // Misplaced declaration of an exception variable
        catch(Object obj) { }
        obj1 = InetAddress.getByName(s);
        if(s != null) goto _L2; else goto _L1
_L1:
        obj = (InetAddress)null;
_L3:
        if(((InetAddress) (obj1)).isMulticastAddress())
            break MISSING_BLOCK_LABEL_178;
        obj1 = JVM INSTR new #97  <Class DatagramSocket>;
        ((DatagramSocket) (obj1)).DatagramSocket(i, ((InetAddress) (obj)));
        ucSocket = ((DatagramSocket) (obj1));
        obj = JVM INSTR new #102 <Class StringBuffer>;
        ((StringBuffer) (obj)).StringBuffer();
        log(((StringBuffer) (obj)).append("bound socket to host:").append(address()).append(", port: ").append(port()).toString());
_L4:
        return;
_L2:
        obj = obj1;
          goto _L3
        try
        {
            obj = JVM INSTR new #130 <Class MulticastSocket>;
            ((MulticastSocket) (obj)).MulticastSocket(i);
            mcSocket = ((MulticastSocket) (obj));
            mcSocket.joinGroup(((InetAddress) (obj1)));
            group = ((InetAddress) (obj1));
            obj = JVM INSTR new #102 <Class StringBuffer>;
            ((StringBuffer) (obj)).StringBuffer();
            log(((StringBuffer) (obj)).append("bound multicast socket to host:").append(address()).append(", port: ").append(port()).append(", group:").append(group).toString());
        }
        // Misplaced declaration of an exception variable
        catch(Object obj)
        {
            error("opening socket failed!\n\t> address:" + s + ", port:" + i + " [group:" + group + "]" + "\n\t> " + ((Throwable) (obj)).getMessage());
        }
        // Misplaced declaration of an exception variable
        catch(Object obj)
        {
            error("opening socket failed!\n\t> bad arguments: " + ((Throwable) (obj)).getMessage());
        }
        // Misplaced declaration of an exception variable
        catch(SecurityException securityexception)
        {
            s = new StringBuffer();
            if(isMulticast())
                obj = "could not joined the group";
            else
                obj = "warning";
            error(s.append(((String) (obj))).append("\n\t> ").append(securityexception.getMessage()).toString());
        }
          goto _L4
    }

    private void callReceiveHandler(byte abyte0[])
        throws NoSuchMethodException
    {
        Class class1 = abyte0.getClass();
        owner.getClass().getMethod(receiveHandler, new Class[] {
            class1
        }).invoke(owner, new Object[] {
            abyte0
        });
_L1:
        return;
        abyte0;
        error(abyte0.getMessage());
          goto _L1
        abyte0;
        abyte0.printStackTrace();
          goto _L1
    }

    private void callReceiveHandler(byte abyte0[], String s, int i)
    {
        Class class1 = abyte0.getClass();
        Class class2 = s.getClass();
        Class class3 = Integer.TYPE;
        Integer integer = JVM INSTR new #202 <Class Integer>;
        integer.Integer(i);
        owner.getClass().getMethod(receiveHandler, new Class[] {
            class1, class2, class3
        }).invoke(owner, new Object[] {
            abyte0, s, integer
        });
_L2:
        return;
        abyte0;
        error(abyte0.getMessage());
        continue; /* Loop/switch isn't completed */
        abyte0;
        abyte0.printStackTrace();
        continue; /* Loop/switch isn't completed */
        abyte0;
        if(true) goto _L2; else goto _L1
_L1:
    }

    private void callTimeoutHandler()
    {
        owner.getClass().getDeclaredMethod(timeoutHandler, null).invoke(owner, null);
_L2:
        return;
        Object obj;
        obj;
        error(((Throwable) (obj)).getMessage());
        continue; /* Loop/switch isn't completed */
        obj;
        ((Throwable) (obj)).printStackTrace();
        continue; /* Loop/switch isn't completed */
        obj;
        if(true) goto _L2; else goto _L1
_L1:
    }

    private void error(String s)
    {
        System.err.println(s);
    }

    private void log(String s)
    {
        Object obj = new Date();
        if(!log && header.equals(""))
            header = "-- UDP session started at " + obj + " --\n-- " + s + " --\n";
        if(log)
        {
            obj = (new SimpleDateFormat("yy-MM-dd HH:mm:ss.S Z")).format(((Date) (obj)));
            System.out.println(header + "[" + obj + "] " + s);
            header = "";
        }
    }

    public String address()
    {
        Object obj = null;
        if(!isClosed())
        {
            if(isMulticast())
                obj = mcSocket.getLocalAddress();
            else
                obj = ucSocket.getLocalAddress();
            if(((InetAddress) (obj)).isAnyLocalAddress())
                obj = null;
            else
                obj = ((InetAddress) (obj)).getHostAddress();
        }
        return ((String) (obj));
    }

    public boolean broadcast(boolean flag)
    {
        boolean flag1;
        boolean flag2;
        flag1 = false;
        flag2 = flag1;
        if(ucSocket != null)
        {
            ucSocket.setBroadcast(flag);
            flag2 = isBroadcast();
        }
_L2:
        return flag2;
        Object obj;
        obj;
        error(((Throwable) (obj)).getMessage());
        flag2 = flag1;
        continue; /* Loop/switch isn't completed */
        obj;
        flag2 = flag1;
        if(true) goto _L2; else goto _L1
_L1:
    }

    public void close()
    {
        if(!isClosed()) goto _L2; else goto _L1
_L1:
        return;
_L2:
        int i;
        String s;
        i = port();
        s = address();
        if(!isMulticast()) goto _L4; else goto _L3
_L3:
        if(group != null)
        {
            mcSocket.leaveGroup(group);
            StringBuffer stringbuffer = JVM INSTR new #102 <Class StringBuffer>;
            stringbuffer.StringBuffer();
            log(stringbuffer.append("leave group < address:").append(group).append(" >").toString());
        }
        mcSocket.close();
        mcSocket = null;
_L6:
        log("close socket < port:" + i + ", address:" + s + " >\n");
        continue; /* Loop/switch isn't completed */
_L4:
        ucSocket.close();
        ucSocket = null;
        if(true) goto _L6; else goto _L5
_L5:
        IOException ioexception;
        ioexception;
        StringBuffer stringbuffer1 = JVM INSTR new #102 <Class StringBuffer>;
        stringbuffer1.StringBuffer();
        error(stringbuffer1.append("Error while closing the socket!\n\t> ").append(ioexception.getMessage()).toString());
        log("close socket < port:" + i + ", address:" + s + " >\n");
        continue; /* Loop/switch isn't completed */
        Object obj;
        obj;
        log("close socket < port:" + i + ", address:" + s + " >\n");
        if(true) goto _L1; else goto _L7
_L7:
        obj;
        log("close socket < port:" + i + ", address:" + s + " >\n");
        throw obj;
    }

    public void dispose()
    {
        close();
    }

    public int getBuffer()
    {
        return size;
    }

    public int getTimeToLive()
    {
        if(!isMulticast() || isClosed()) goto _L2; else goto _L1
_L1:
        int i = mcSocket.getTimeToLive();
_L4:
        return i;
        IOException ioexception;
        ioexception;
        error("could not retrieve the current time-to-live value!\n\t> " + ioexception.getMessage());
_L2:
        i = -1;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public boolean isBroadcast()
    {
        boolean flag = false;
        if(ucSocket != null) goto _L2; else goto _L1
_L1:
        return flag;
_L2:
        boolean flag1 = ucSocket.getBroadcast();
        flag = flag1;
        continue; /* Loop/switch isn't completed */
        Object obj;
        obj;
        error(((Throwable) (obj)).getMessage());
        continue; /* Loop/switch isn't completed */
        obj;
        if(true) goto _L1; else goto _L3
_L3:
    }

    public boolean isClosed()
    {
        boolean flag = true;
        if(!isMulticast()) goto _L2; else goto _L1
_L1:
        if(mcSocket != null)
            flag = mcSocket.isClosed();
_L4:
        return flag;
_L2:
        if(ucSocket != null)
            flag = ucSocket.isClosed();
        if(true) goto _L4; else goto _L3
_L3:
    }

    public boolean isJoined()
    {
        boolean flag;
        if(group != null)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public boolean isListening()
    {
        return listen;
    }

    public boolean isLoopback()
    {
        boolean flag;
        boolean flag1;
        flag = false;
        flag1 = flag;
        if(!isMulticast())
            break MISSING_BLOCK_LABEL_36;
        flag1 = flag;
        boolean flag2;
        if(isClosed())
            break MISSING_BLOCK_LABEL_36;
        flag2 = mcSocket.getLoopbackMode();
        flag1 = flag;
        if(!flag2)
            flag1 = true;
_L2:
        return flag1;
        SocketException socketexception;
        socketexception;
        error("could not get the loopback mode!\n\t> " + socketexception.getMessage());
        flag1 = flag;
        if(true) goto _L2; else goto _L1
_L1:
    }

    public boolean isMulticast()
    {
        boolean flag;
        if(mcSocket != null)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public void listen()
    {
        Object obj;
        byte abyte0[] = new byte[size];
        obj = JVM INSTR new #321 <Class DatagramPacket>;
        ((DatagramPacket) (obj)).DatagramPacket(abyte0, abyte0.length);
        if(!isMulticast()) goto _L2; else goto _L1
_L1:
        mcSocket.setSoTimeout(timeout);
        mcSocket.receive(((DatagramPacket) (obj)));
_L5:
        StringBuffer stringbuffer = JVM INSTR new #102 <Class StringBuffer>;
        stringbuffer.StringBuffer();
        log(stringbuffer.append("receive packet <- from ").append(((DatagramPacket) (obj)).getAddress()).append(", port:").append(((DatagramPacket) (obj)).getPort()).append(", length: ").append(((DatagramPacket) (obj)).getLength()).toString());
        if(((DatagramPacket) (obj)).getLength() == 0) goto _L4; else goto _L3
_L3:
        byte abyte1[];
        abyte1 = new byte[((DatagramPacket) (obj)).getLength()];
        System.arraycopy(((DatagramPacket) (obj)).getData(), 0, abyte1, 0, abyte1.length);
        callReceiveHandler(abyte1);
_L4:
        return;
_L2:
        ucSocket.setSoTimeout(timeout);
        ucSocket.receive(((DatagramPacket) (obj)));
          goto _L5
        NoSuchMethodException nosuchmethodexception;
        nosuchmethodexception;
        try
        {
            callReceiveHandler(abyte1, ((DatagramPacket) (obj)).getAddress().getHostAddress(), ((DatagramPacket) (obj)).getPort());
        }
        // Misplaced declaration of an exception variable
        catch(Object obj)
        {
            listen = false;
            thread = null;
        }
        catch(IOException ioexception)
        {
            listen = false;
            thread = null;
            if(ioexception instanceof SocketTimeoutException)
                callTimeoutHandler();
            else
            if(ucSocket != null && mcSocket != null)
                error("listen failed!\n\t> " + ioexception.getMessage());
        }
          goto _L4
    }

    public void listen(int i)
    {
        if(!isClosed()) goto _L2; else goto _L1
_L1:
        return;
_L2:
        listen = true;
        timeout = i;
        if(thread != null)
            send(new byte[0]);
        if(thread == null)
        {
            thread = new Thread(this);
            thread.start();
        }
        if(true) goto _L1; else goto _L3
_L3:
    }

    public void listen(boolean flag)
    {
        listen = flag;
        timeout = 0;
        if(flag && thread == null && !isClosed())
        {
            thread = new Thread(this);
            thread.start();
        }
        if(!flag && thread != null)
        {
            send(new byte[0]);
            thread.interrupt();
            thread = null;
        }
    }

    public void log(boolean flag)
    {
        log = flag;
    }

    public void loopback(boolean flag)
    {
        MulticastSocket multicastsocket;
        if(!isMulticast())
            break MISSING_BLOCK_LABEL_23;
        multicastsocket = mcSocket;
        if(!flag)
            flag = true;
        else
            flag = false;
        multicastsocket.setLoopbackMode(flag);
_L1:
        return;
        SocketException socketexception;
        socketexception;
        error("could not set the loopback mode!\n\t>" + socketexception.getMessage());
          goto _L1
    }

    public int port()
    {
        int i;
        if(isClosed())
            i = -1;
        else
        if(isMulticast())
            i = mcSocket.getLocalPort();
        else
            i = ucSocket.getLocalPort();
        return i;
    }

    public void run()
    {
        while(listen) 
            listen();
    }

    public boolean send(String s)
    {
        return send(s.getBytes());
    }

    public boolean send(String s, String s1)
    {
        return send(s.getBytes(), s1);
    }

    public boolean send(String s, String s1, int i)
    {
        return send(s.getBytes(), s1, i);
    }

    public boolean send(byte abyte0[])
    {
        boolean flag;
        if(isMulticast() && group == null)
        {
            flag = false;
        } else
        {
            String s;
            if(isMulticast())
                s = group.getHostAddress();
            else
                s = address();
            flag = send(abyte0, s, port());
        }
        return flag;
    }

    public boolean send(byte abyte0[], String s)
    {
        return send(abyte0, s, port());
    }

    public boolean send(byte abyte0[], String s, int i)
    {
        DatagramPacket datagrampacket;
        datagrampacket = JVM INSTR new #321 <Class DatagramPacket>;
        datagrampacket.DatagramPacket(abyte0, abyte0.length, InetAddress.getByName(s), i);
        if(!isMulticast()) goto _L2; else goto _L1
_L1:
        mcSocket.send(datagrampacket);
_L3:
        boolean flag;
        boolean flag1;
        boolean flag2;
        boolean flag3;
        flag = true;
        flag1 = true;
        flag2 = true;
        flag3 = flag1;
        abyte0 = JVM INSTR new #102 <Class StringBuffer>;
        flag3 = flag1;
        abyte0.StringBuffer();
        flag3 = flag1;
        log(abyte0.append("send packet -> address:").append(datagrampacket.getAddress()).append(", port:").append(datagrampacket.getPort()).append(", length: ").append(datagrampacket.getLength()).toString());
_L4:
        return flag2;
_L2:
        ucSocket.send(datagrampacket);
          goto _L3
        abyte0;
        flag2 = false;
_L5:
        flag3 = flag2;
        StringBuffer stringbuffer = JVM INSTR new #102 <Class StringBuffer>;
        flag3 = flag2;
        stringbuffer.StringBuffer();
        flag3 = flag2;
        error(stringbuffer.append("could not send message!\t\n> port:").append(i).append(", ip:").append(s).append(", buffer size: ").append(size).append(", packet length: ").append(datagrampacket.getLength()).append("\t\n> ").append(abyte0.getMessage()).toString());
          goto _L4
        abyte0;
        flag2 = flag3;
          goto _L4
        abyte0;
        flag2 = false;
          goto _L4
        abyte0;
        datagrampacket = null;
        flag2 = false;
          goto _L5
        abyte0;
        flag2 = flag;
          goto _L5
    }

    public boolean setBuffer(int i)
    {
        boolean flag = false;
        if(!isListening()) goto _L2; else goto _L1
_L1:
        return flag;
_L2:
        if(!isMulticast()) goto _L4; else goto _L3
_L3:
        Object obj = mcSocket;
        int j;
        if(i > 0)
            j = i;
        else
            j = 65507;
        ((DatagramSocket) (obj)).setSendBufferSize(j);
        obj = mcSocket;
        if(i > 0)
            j = i;
        else
            j = 65507;
        ((DatagramSocket) (obj)).setReceiveBufferSize(j);
_L5:
        Object obj1;
        if(i <= 0)
            i = 65507;
        size = i;
        flag = true;
        continue; /* Loop/switch isn't completed */
_L4:
        obj = ucSocket;
        if(i > 0)
            j = i;
        else
            j = 65507;
        ((DatagramSocket) (obj)).setSendBufferSize(j);
        obj = ucSocket;
        if(i > 0)
            j = i;
        else
            j = 65507;
        ((DatagramSocket) (obj)).setReceiveBufferSize(j);
          goto _L5
        obj1;
        StringBuffer stringbuffer = JVM INSTR new #102 <Class StringBuffer>;
        stringbuffer.StringBuffer();
        error(stringbuffer.append("could not set the buffer!\n> ").append(((Throwable) (obj1)).getMessage()).toString());
        continue; /* Loop/switch isn't completed */
        obj1;
        if(true) goto _L1; else goto _L6
_L6:
    }

    public void setReceiveHandler(String s)
    {
        receiveHandler = s;
    }

    public boolean setTimeToLive(int i)
    {
        if(isMulticast() && !isClosed())
            mcSocket.setTimeToLive(i);
        boolean flag = true;
_L2:
        return flag;
        Object obj;
        obj;
        error("setting the default \"Time to Live\" value failed!\n\t> " + ((Throwable) (obj)).getMessage());
_L3:
        flag = false;
        if(true) goto _L2; else goto _L1
_L1:
        obj;
        error("\"Time to Live\" value must be in the range of 0-255");
          goto _L3
    }

    public void setTimeoutHandler(String s)
    {
        timeoutHandler = s;
    }

    public static final int BUFFER_SIZE = 65507;
    InetAddress group;
    String header;
    boolean listen;
    boolean log;
    MulticastSocket mcSocket;
    Object owner;
    String receiveHandler;
    int size;
    Thread thread;
    int timeout;
    String timeoutHandler;
    DatagramSocket ucSocket;
}
