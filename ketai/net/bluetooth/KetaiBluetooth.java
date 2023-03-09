// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package ketai.net.bluetooth;

import android.app.Activity;
import android.bluetooth.*;
import android.content.*;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import processing.core.PApplet;

// Referenced classes of package ketai.net.bluetooth:
//            KBluetoothConnection, KBluetoothListener

public class KetaiBluetooth
{
    private class ConnectThread extends Thread
    {

        public void cancel()
        {
        }

        public void run()
        {
_L3:
            if(mmSocket == null) goto _L2; else goto _L1
_L1:
            PApplet.println((new StringBuilder("BEGIN mConnectThread SocketType:")).append(mSocketType).append(":").append(mmSocket.getRemoteDevice().getName()).toString());
            bluetoothAdapter.cancelDiscovery();
            if(mmSocket != null)
                mmSocket.connect();
            PApplet.println("KBTConnect thread connected!");
            connectDevice(mmSocket);
_L4:
            return;
_L2:
            try
            {
                Thread.sleep(5L);
            }
            catch(InterruptedException interruptedexception)
            {
                interruptedexception.printStackTrace();
            }
              goto _L3
            IOException ioexception;
            ioexception;
            try
            {
                mmSocket.close();
            }
            catch(IOException ioexception1)
            {
                PApplet.println((new StringBuilder("unable to close() ")).append(mSocketType).append(" socket during connection failure").append(ioexception1).toString());
            }
            mConnectThread = null;
              goto _L4
        }

        private String mSocketType;
        protected final BluetoothDevice mmDevice;
        private final BluetoothSocket mmSocket;
        final KetaiBluetooth this$0;

        public ConnectThread(BluetoothDevice bluetoothdevice, boolean flag)
        {
            this$0 = KetaiBluetooth.this;
            super();
            mmDevice = bluetoothdevice;
            Object obj = null;
            String s;
            if(flag)
                s = "Secure";
            else
                s = "Insecure";
            mSocketType = s;
            if(!flag)
                break MISSING_BLOCK_LABEL_57;
            try
            {
                ketaibluetooth = bluetoothdevice.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
            }
            // Misplaced declaration of an exception variable
            catch(KetaiBluetooth ketaibluetooth)
            {
                PApplet.println((new StringBuilder("Socket Type: ")).append(mSocketType).append("create() failed").append(KetaiBluetooth.this).toString());
                ketaibluetooth = obj;
            }
            mmSocket = KetaiBluetooth.this;
            return;
            ketaibluetooth = bluetoothdevice.createInsecureRfcommSocketToServiceRecord(MY_UUID_INSECURE);
            break MISSING_BLOCK_LABEL_44;
        }
    }


    public KetaiBluetooth(PApplet papplet)
    {
        isStarted = false;
        MY_UUID_SECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        MY_UUID_INSECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        NAME_SECURE = "BluetoothSecure";
        NAME_INSECURE = "BluetoothInsecure";
        parent = papplet;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null)
        {
            PApplet.println("No Bluetooth Support.");
        } else
        {
            if(!bluetoothAdapter.isEnabled())
            {
                papplet = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
                parent.getActivity().startActivityForResult(papplet, 1);
            }
            pairedDevices = new HashMap();
            discoveredDevices = new HashMap();
            currentConnections = new HashMap();
            findParentIntention();
            parent.registerMethod("dispose", this);
        }
    }

    private void findParentIntention()
    {
        onBluetoothDataEventMethod = parent.getClass().getMethod("onBluetoothDataEvent", new Class[] {
            java/lang/String, [B
        });
        PApplet.println("Found onBluetoothDataEvent method.");
_L1:
        return;
        NoSuchMethodException nosuchmethodexception;
        nosuchmethodexception;
        PApplet.println("Did not find onBluetoothDataEvent callback method.");
          goto _L1
    }

    public void broadcast(byte abyte0[])
    {
        Iterator iterator = currentConnections.entrySet().iterator();
        do
        {
            if(!iterator.hasNext())
                return;
            ((KBluetoothConnection)((java.util.Map.Entry)iterator.next()).getValue()).write(abyte0);
        } while(true);
    }

    public boolean connectDevice(BluetoothSocket bluetoothsocket)
    {
        KBluetoothConnection kbluetoothconnection = new KBluetoothConnection(this, bluetoothsocket);
        boolean flag;
        if(kbluetoothconnection.isConnected())
        {
            kbluetoothconnection.start();
            if(kbluetoothconnection != null && !currentConnections.containsKey(bluetoothsocket.getRemoteDevice().getAddress()))
                currentConnections.put(bluetoothsocket.getRemoteDevice().getAddress(), kbluetoothconnection);
            if(mConnectThread != null)
            {
                mConnectThread.cancel();
                mConnectThread = null;
            }
            flag = true;
        } else
        {
            PApplet.println((new StringBuilder("Error trying to connect to ")).append(bluetoothsocket.getRemoteDevice().getName()).append(" (").append(bluetoothsocket.getRemoteDevice().getAddress()).append(")").toString());
            mConnectThread = null;
            flag = false;
        }
        return flag;
    }

    public boolean connectDevice(String s)
    {
        if(BluetoothAdapter.checkBluetoothAddress(s)) goto _L2; else goto _L1
_L1:
        PApplet.println((new StringBuilder("Bad bluetooth hardware address! : ")).append(s).toString());
_L4:
        return false;
_L2:
        BluetoothDevice bluetoothdevice = bluetoothAdapter.getRemoteDevice(s);
        if(mConnectThread == null)
        {
            mConnectThread = new ConnectThread(bluetoothdevice, true);
            mConnectThread.start();
        } else
        if(mConnectThread.mmDevice.getAddress() != s)
        {
            mConnectThread.cancel();
            mConnectThread = new ConnectThread(bluetoothdevice, true);
            mConnectThread.start();
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public boolean connectDeviceUsingSLIP(String s)
    {
        return false;
    }

    public boolean connectToDeviceByName(String s)
    {
        String s1 = "";
        boolean flag;
        if(pairedDevices.containsKey(s))
            s1 = (String)pairedDevices.get(s);
        else
        if(discoveredDevices.containsKey(s))
            s1 = (String)discoveredDevices.get(s);
        if(s1.length() > 0 && currentConnections.containsKey(s1))
            flag = true;
        else
            flag = connectDevice(s1);
        return flag;
    }

    public boolean disconnectDevice(String s)
    {
        Set set;
        set = currentConnections.keySet();
        PApplet.println((new StringBuilder("Disconnecting device: ")).append(s).toString());
        if(set.size() <= 0) goto _L2; else goto _L1
_L1:
        Iterator iterator = set.iterator();
_L8:
        if(iterator.hasNext()) goto _L4; else goto _L3
_L3:
        PApplet.println((new StringBuilder("Did not find device (")).append(s).append(")in connected list.").toString());
_L2:
        boolean flag = false;
_L6:
        return flag;
_L4:
        String s1 = (String)iterator.next();
        PApplet.println((new StringBuilder("Comparing ")).append(s1).append(" to target ").append(s).toString());
        KBluetoothConnection kbluetoothconnection = (KBluetoothConnection)currentConnections.get(s1);
        if(!s.equals(s1) && !s1.equals(kbluetoothconnection.getAddress()))
            continue; /* Loop/switch isn't completed */
        PApplet.println((new StringBuilder("Disconnecting device (")).append(s).append(").").toString());
        kbluetoothconnection.disconnect();
        PApplet.println((new StringBuilder("Removing current connection for ")).append(kbluetoothconnection.getDeviceName()).toString());
        currentConnections.remove(kbluetoothconnection);
        set.remove(s1);
        flag = true;
        if(true) goto _L6; else goto _L5
_L5:
        if(true) goto _L8; else goto _L7
_L7:
    }

    public void discoverDevices()
    {
        discoveredDevices.clear();
        bluetoothAdapter.cancelDiscovery();
        if(bluetoothAdapter.startDiscovery())
            PApplet.println("Starting bt discovery.");
        else
            PApplet.println("BT discovery failed to start.");
    }

    public void dispose()
    {
        stop();
    }

    public String getAddress()
    {
        String s;
        if(bluetoothAdapter != null)
            s = bluetoothAdapter.getAddress();
        else
            s = "";
        return s;
    }

    public BluetoothAdapter getBluetoothAdapater()
    {
        return bluetoothAdapter;
    }

    public ArrayList getConnectedDeviceAddresses()
    {
        ArrayList arraylist;
        Object obj;
        arraylist = new ArrayList();
        obj = currentConnections.keySet();
        if(((Set) (obj)).size() <= 0) goto _L2; else goto _L1
_L1:
        obj = ((Set) (obj)).iterator();
_L5:
        if(((Iterator) (obj)).hasNext()) goto _L3; else goto _L2
_L2:
        return arraylist;
_L3:
        String s = (String)((Iterator) (obj)).next();
        arraylist.add(((KBluetoothConnection)currentConnections.get(s)).getAddress());
        if(true) goto _L5; else goto _L4
_L4:
    }

    public ArrayList getConnectedDeviceLabel()
    {
        ArrayList arraylist;
        Object obj;
        arraylist = new ArrayList();
        obj = currentConnections.keySet();
        if(((Set) (obj)).size() <= 0) goto _L2; else goto _L1
_L1:
        obj = ((Set) (obj)).iterator();
_L5:
        if(((Iterator) (obj)).hasNext()) goto _L3; else goto _L2
_L2:
        return arraylist;
_L3:
        String s = (String)((Iterator) (obj)).next();
        arraylist.add((new StringBuilder(String.valueOf(((KBluetoothConnection)currentConnections.get(s)).getDeviceName()))).append("(").append(s).append(")").toString());
        if(true) goto _L5; else goto _L4
_L4:
    }

    public ArrayList getConnectedDeviceNames()
    {
        ArrayList arraylist;
        Set set;
        arraylist = new ArrayList();
        set = currentConnections.keySet();
        if(set.size() <= 0) goto _L2; else goto _L1
_L1:
        Iterator iterator = set.iterator();
_L5:
        if(iterator.hasNext()) goto _L3; else goto _L2
_L2:
        return arraylist;
_L3:
        String s = (String)iterator.next();
        arraylist.add(((KBluetoothConnection)currentConnections.get(s)).getDeviceName());
        if(true) goto _L5; else goto _L4
_L4:
    }

    public ArrayList getDiscoveredDeviceNames()
    {
        ArrayList arraylist = new ArrayList();
        Iterator iterator = discoveredDevices.keySet().iterator();
        do
        {
            String s;
            do
            {
                if(!iterator.hasNext())
                    return arraylist;
                s = (String)iterator.next();
            } while(s == null);
            arraylist.add(s);
        } while(true);
    }

    public ArrayList getPairedDeviceNames()
    {
        ArrayList arraylist;
        Set set;
        arraylist = new ArrayList();
        pairedDevices.clear();
        set = bluetoothAdapter.getBondedDevices();
        if(set.size() <= 0) goto _L2; else goto _L1
_L1:
        Iterator iterator = set.iterator();
_L5:
        if(iterator.hasNext()) goto _L3; else goto _L2
_L2:
        return arraylist;
_L3:
        BluetoothDevice bluetoothdevice = (BluetoothDevice)iterator.next();
        pairedDevices.put(bluetoothdevice.getName(), bluetoothdevice.getAddress());
        arraylist.add(bluetoothdevice.getName());
        if(true) goto _L5; else goto _L4
_L4:
    }

    public boolean isDiscoverable()
    {
        boolean flag;
        if(bluetoothAdapter.getScanMode() == 23)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public boolean isDiscovering()
    {
        return bluetoothAdapter.isDiscovering();
    }

    public boolean isStarted()
    {
        return isStarted;
    }

    public String lookupAddressByName(String s)
    {
        if(pairedDevices.containsKey(s))
            s = (String)pairedDevices.get(s);
        else
        if(discoveredDevices.containsKey(s))
            s = (String)discoveredDevices.get(s);
        else
            s = "";
        return s;
    }

    public void makeDiscoverable()
    {
        if(bluetoothAdapter.getScanMode() != 23)
        {
            Intent intent = new Intent("android.bluetooth.adapter.action.REQUEST_DISCOVERABLE");
            intent.putExtra("android.bluetooth.adapter.extra.DISCOVERABLE_DURATION", 300);
            parent.getActivity().startActivity(intent);
        }
    }

    public void onActivityResult(int i, int j, Intent intent)
    {
        i;
        JVM INSTR tableswitch 1 1: default 20
    //                   1 21;
           goto _L1 _L2
_L1:
        return;
_L2:
        if(j == -1)
            PApplet.println("BT made available.");
        else
            PApplet.println("BT was not made available.");
        if(true) goto _L1; else goto _L3
_L3:
    }

    protected void removeConnection(KBluetoothConnection kbluetoothconnection)
    {
        PApplet.println((new StringBuilder("KBTM removing connection for ")).append(kbluetoothconnection.getAddress()).toString());
        if(currentConnections.containsKey(kbluetoothconnection.getAddress()))
        {
            kbluetoothconnection.cancel();
            currentConnections.remove(kbluetoothconnection.getAddress());
        }
    }

    public void setSLIPMode(boolean flag)
    {
    }

    public boolean start()
    {
        if(btListener != null)
        {
            stop();
            isStarted = false;
        }
        btListener = new KBluetoothListener(this, true);
        btListener.start();
        isStarted = true;
        findParentIntention();
        IntentFilter intentfilter = new IntentFilter("android.bluetooth.device.action.FOUND");
        parent.getActivity().registerReceiver(mReceiver, intentfilter);
        parent.getActivity().registerReceiver(mReceiver, new IntentFilter("android.bluetooth.adapter.action.DISCOVERY_FINISHED"));
        return isStarted;
    }

    public void stop()
    {
        if(btListener != null)
            btListener.cancel();
        if(mConnectThread != null)
            mConnectThread.cancel();
        Iterator iterator = currentConnections.keySet().iterator();
        do
        {
            if(!iterator.hasNext())
            {
                currentConnections.clear();
                btListener = null;
                mConnectThread = null;
                return;
            }
            String s = (String)iterator.next();
            ((KBluetoothConnection)currentConnections.get(s)).cancel();
        } while(true);
    }

    public String toString()
    {
        String s;
        Iterator iterator;
        s = "KBluetoothManager dump:\n--------------------\nPairedDevices:\n";
        iterator = pairedDevices.keySet().iterator();
_L5:
        if(iterator.hasNext()) goto _L2; else goto _L1
_L1:
        s = (new StringBuilder(String.valueOf(s))).append("\n\nDiscovered Devices\n").toString();
        iterator = discoveredDevices.keySet().iterator();
_L6:
        if(iterator.hasNext()) goto _L4; else goto _L3
_L3:
        s = (new StringBuilder(String.valueOf(s))).append("\n\nCurrent Connections\n").toString();
        iterator = currentConnections.keySet().iterator();
_L7:
        if(!iterator.hasNext())
            return (new StringBuilder(String.valueOf(s))).append("\n-------------------------------\n").toString();
        break MISSING_BLOCK_LABEL_249;
_L2:
        String s1 = (String)iterator.next();
        s = (new StringBuilder(String.valueOf(s))).append(s1).append("->").append((String)pairedDevices.get(s1)).append("\n").toString();
          goto _L5
_L4:
        String s2 = (String)iterator.next();
        s = (new StringBuilder(String.valueOf(s))).append(s2).append("->").append((String)discoveredDevices.get(s2)).append("\n").toString();
          goto _L6
        String s3 = (String)iterator.next();
        s = (new StringBuilder(String.valueOf(s))).append(s3).append("->").append(currentConnections.get(s3)).append("\n").toString();
          goto _L7
    }

    public void write(String s, byte abyte0[])
    {
        bluetoothAdapter.cancelDiscovery();
        break MISSING_BLOCK_LABEL_8;
        if((currentConnections.containsKey(s) || connectDevice(s)) && currentConnections.containsKey(s))
            ((KBluetoothConnection)currentConnections.get(s)).write(abyte0);
        return;
    }

    public void writeToDeviceName(String s, byte abyte0[])
    {
        String s1 = lookupAddressByName(s);
        if(s1.length() > 0)
            write(s1, abyte0);
        else
            PApplet.println((new StringBuilder("Error writing to ")).append(s).append(".  HW Address was not found.").toString());
    }

    static final int BLUETOOTH_ENABLE_REQUEST = 1;
    protected UUID MY_UUID_INSECURE;
    protected UUID MY_UUID_SECURE;
    protected String NAME_INSECURE;
    protected String NAME_SECURE;
    protected BluetoothAdapter bluetoothAdapter;
    private KBluetoothListener btListener;
    private HashMap currentConnections;
    private HashMap discoveredDevices;
    private boolean isStarted;
    private ConnectThread mConnectThread;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent)
        {
            if("android.bluetooth.device.action.FOUND".equals(intent.getAction()))
            {
                context = (BluetoothDevice)intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                if(context != null)
                {
                    discoveredDevices.put(context.getName(), context.getAddress());
                    PApplet.println((new StringBuilder("New Device Discovered: ")).append(context.getName()).toString());
                }
            }
        }

        final KetaiBluetooth this$0;

            
            {
                this$0 = KetaiBluetooth.this;
                super();
            }
    }
;
    protected Method onBluetoothDataEventMethod;
    private HashMap pairedDevices;
    protected PApplet parent;


}
