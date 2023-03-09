// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package ketai.net.bluetooth;

import android.bluetooth.*;
import java.io.IOException;
import processing.core.PApplet;

// Referenced classes of package ketai.net.bluetooth:
//            KetaiBluetooth

public class KBluetoothListener extends Thread
{

    public KBluetoothListener(KetaiBluetooth ketaibluetooth, boolean flag)
    {
        go = true;
        Object obj = null;
        String s;
        if(flag)
            s = "Secure";
        else
            s = "Insecure";
        mSocketType = s;
        btManager = ketaibluetooth;
        mAdapter = btManager.getBluetoothAdapater();
        if(!flag)
            break MISSING_BLOCK_LABEL_80;
        try
        {
            ketaibluetooth = mAdapter.listenUsingRfcommWithServiceRecord(btManager.NAME_SECURE, btManager.MY_UUID_SECURE);
        }
        // Misplaced declaration of an exception variable
        catch(KetaiBluetooth ketaibluetooth)
        {
            PApplet.println((new StringBuilder("Socket Type: ")).append(mSocketType).append("listen() failed").append(ketaibluetooth).toString());
            ketaibluetooth = obj;
        }
        mmServerSocket = ketaibluetooth;
        return;
        ketaibluetooth = mAdapter.listenUsingInsecureRfcommWithServiceRecord(btManager.NAME_INSECURE, btManager.MY_UUID_INSECURE);
        break MISSING_BLOCK_LABEL_67;
    }

    public void cancel()
    {
        PApplet.println((new StringBuilder("Socket Type")).append(mSocketType).append("cancel ").append(this).toString());
        go = false;
        if(mmServerSocket != null)
            mmServerSocket.close();
_L1:
        return;
        IOException ioexception;
        ioexception;
        PApplet.println((new StringBuilder("Socket Type")).append(mSocketType).append("close() of server failed").append(ioexception.getMessage()).toString());
          goto _L1
    }

    public void run()
    {
        PApplet.println((new StringBuilder("Socket Type: ")).append(mSocketType).append("BEGIN mAcceptThread").append(this).toString());
        PApplet.println((new StringBuilder("AcceptThread")).append(mSocketType).toString());
        if(mmServerSocket != null) goto _L2; else goto _L1
_L1:
        PApplet.println("Failed to get socket for server! bye.");
_L6:
        return;
_L5:
        BluetoothSocket bluetoothsocket = mmServerSocket.accept();
        if(bluetoothsocket == null) goto _L2; else goto _L3
_L3:
        this;
        JVM INSTR monitorenter ;
        StringBuilder stringbuilder = JVM INSTR new #65  <Class StringBuilder>;
        stringbuilder.StringBuilder("Incoming connection from: ");
        PApplet.println(stringbuilder.append(bluetoothsocket.getRemoteDevice().getName()).toString());
        btManager.connectDevice(bluetoothsocket);
        this;
        JVM INSTR monitorexit ;
_L2:
        if(go) goto _L5; else goto _L4
_L4:
        PApplet.println((new StringBuilder("END mAcceptThread, socket Type: ")).append(mSocketType).toString());
          goto _L6
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        try
        {
            throw exception;
        }
        catch(IOException ioexception)
        {
            PApplet.println((new StringBuilder("Socket Type: ")).append(mSocketType).append("accept() failed").append(ioexception.getMessage()).toString());
        }
          goto _L2
    }

    private KetaiBluetooth btManager;
    private boolean go;
    private BluetoothAdapter mAdapter;
    private String mSocketType;
    private final BluetoothServerSocket mmServerSocket;
}
