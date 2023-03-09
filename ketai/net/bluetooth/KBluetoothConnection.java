// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package ketai.net.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import processing.core.PApplet;

// Referenced classes of package ketai.net.bluetooth:
//            KetaiBluetooth

public class KBluetoothConnection extends Thread
{

    public KBluetoothConnection(KetaiBluetooth ketaibluetooth, BluetoothSocket bluetoothsocket)
    {
        Object obj;
        isConnected = false;
        address = "";
        PApplet.println((new StringBuilder("create Connection thread to ")).append(bluetoothsocket.getRemoteDevice().getName()).toString());
        btm = ketaibluetooth;
        mmSocket = bluetoothsocket;
        ketaibluetooth = null;
        obj = null;
        address = bluetoothsocket.getRemoteDevice().getAddress();
        InputStream inputstream = bluetoothsocket.getInputStream();
        ketaibluetooth = inputstream;
        bluetoothsocket = bluetoothsocket.getOutputStream();
        ketaibluetooth = inputstream;
_L2:
        mmInStream = ketaibluetooth;
        mmOutStream = bluetoothsocket;
        isConnected = true;
        return;
        bluetoothsocket;
        PApplet.println((new StringBuilder("temp sockets not created: ")).append(bluetoothsocket.getMessage()).toString());
        bluetoothsocket = obj;
        if(true) goto _L2; else goto _L1
_L1:
    }

    public void cancel()
    {
        mmSocket.close();
_L1:
        return;
        IOException ioexception;
        ioexception;
        PApplet.println((new StringBuilder("close() of connect socket failed")).append(ioexception.getMessage()).toString());
          goto _L1
    }

    public void disconnect()
    {
        PApplet.println((new StringBuilder("Disconnecting device : ")).append(address).toString());
        if(mmInStream != null)
        {
            StringBuilder stringbuilder1;
            try
            {
                StringBuilder stringbuilder = JVM INSTR new #31  <Class StringBuilder>;
                stringbuilder.StringBuilder("Closing input stream for ");
                PApplet.println(stringbuilder.append(address).toString());
                mmInStream.close();
            }
            catch(IOException ioexception)
            {
                PApplet.println((new StringBuilder("Error closing input stream for ")).append(address).append(" : ").append(ioexception.getMessage()).toString());
            }
            mmInStream = null;
        }
        if(mmOutStream != null)
        {
            try
            {
                stringbuilder1 = JVM INSTR new #31  <Class StringBuilder>;
                stringbuilder1.StringBuilder("Closing output stream for ");
                PApplet.println(stringbuilder1.append(address).toString());
                mmOutStream.close();
            }
            catch(IOException ioexception1)
            {
                PApplet.println((new StringBuilder("Error closing output stream for ")).append(address).append(" : ").append(ioexception1.getMessage()).toString());
            }
            mmOutStream = null;
        }
        if(mmSocket != null)
        {
            try
            {
                stringbuilder1 = JVM INSTR new #31  <Class StringBuilder>;
                stringbuilder1.StringBuilder("Closing socket for ");
                PApplet.println(stringbuilder1.append(address).toString());
                mmSocket.close();
            }
            catch(IOException ioexception2)
            {
                PApplet.println((new StringBuilder("Error closing Socket for ")).append(address).append(" : ").append(ioexception2.getMessage()).toString());
            }
            mmSocket = null;
        }
    }

    public String getAddress()
    {
        return address;
    }

    public String getDeviceName()
    {
        String s;
        if(mmSocket == null)
            s = "";
        else
            s = mmSocket.getRemoteDevice().getName();
        return s;
    }

    public boolean isConnected()
    {
        return isConnected;
    }

    public void run()
    {
        byte abyte0[];
        PApplet.println((new StringBuilder("BEGIN mConnectedThread to ")).append(address).toString());
        abyte0 = new byte[1024];
_L1:
        byte abyte1[];
        InterruptedException interruptedexception;
        StringBuilder stringbuilder;
        Method method;
        IllegalAccessException illegalaccessexception;
        try
        {
            abyte1 = Arrays.copyOfRange(abyte0, 0, mmInStream.read(abyte0));
            method = btm.onBluetoothDataEventMethod;
        }
        catch(IOException ioexception)
        {
            btm.removeConnection(this);
            PApplet.println((new StringBuilder(String.valueOf(getAddress()))).append(" disconnected").append(ioexception.getMessage()).toString());
            isConnected = false;
            return;
        }
        if(method == null)
            break MISSING_BLOCK_LABEL_87;
        btm.onBluetoothDataEventMethod.invoke(btm.parent, new Object[] {
            address, abyte1
        });
_L2:
        try
        {
            Thread.sleep(5L);
        }
        // Misplaced declaration of an exception variable
        catch(InterruptedException interruptedexception) { }
          goto _L1
        illegalaccessexception;
        stringbuilder = JVM INSTR new #31  <Class StringBuilder>;
        stringbuilder.StringBuilder("Error in reading connection data.:");
        PApplet.println(stringbuilder.append(illegalaccessexception.getMessage()).toString());
          goto _L2
        InvocationTargetException invocationtargetexception;
        invocationtargetexception;
        StringBuilder stringbuilder1 = JVM INSTR new #31  <Class StringBuilder>;
        stringbuilder1.StringBuilder("Error in reading connection data.:");
        PApplet.println(stringbuilder1.append(invocationtargetexception.getMessage()).toString());
          goto _L2
    }

    public void write(byte abyte0[])
    {
        mmOutStream.write(abyte0);
_L1:
        return;
        abyte0;
        PApplet.println((new StringBuilder(String.valueOf(getAddress()))).append(": Exception during write").append(abyte0.getMessage()).toString());
        btm.removeConnection(this);
          goto _L1
    }

    private String address;
    private KetaiBluetooth btm;
    private boolean isConnected;
    private InputStream mmInStream;
    private OutputStream mmOutStream;
    private BluetoothSocket mmSocket;
}
