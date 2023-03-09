// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package ketai.net.wifidirect;

import android.app.Activity;
import android.content.*;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.*;
import java.net.InetAddress;
import java.util.*;
import processing.core.PApplet;

public class KetaiWiFiDirect extends BroadcastReceiver
    implements android.net.wifi.p2p.WifiP2pManager.ChannelListener, android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener, android.net.wifi.p2p.WifiP2pManager.ActionListener, android.net.wifi.p2p.WifiP2pManager.PeerListListener
{

    public KetaiWiFiDirect(PApplet papplet)
    {
        isWifiP2pEnabled = false;
        retryChannel = false;
        peers = new ArrayList();
        ip = "";
        parent = papplet;
        initIntentFilter();
        parent.registerMethod("resume", this);
        parent.registerMethod("pause", this);
    }

    private void connectToConfig(WifiP2pConfig wifip2pconfig)
    {
        manager.connect(channel, wifip2pconfig, new android.net.wifi.p2p.WifiP2pManager.ActionListener() {

            public void onFailure(int i)
            {
                PApplet.println((new StringBuilder("Connect failed. Retry.")).append(i).toString());
            }

            public void onSuccess()
            {
            }

            final KetaiWiFiDirect this$0;

            
            {
                this$0 = KetaiWiFiDirect.this;
                super();
            }
        }
);
    }

    private void initIntentFilter()
    {
        intentFilter.addAction("android.net.wifi.p2p.STATE_CHANGED");
        intentFilter.addAction("android.net.wifi.p2p.PEERS_CHANGED");
        intentFilter.addAction("android.net.wifi.p2p.CONNECTION_STATE_CHANGE");
        intentFilter.addAction("android.net.wifi.p2p.THIS_DEVICE_CHANGED");
        manager = (WifiP2pManager)parent.getActivity().getSystemService("wifip2p");
        channel = manager.initialize(parent.getActivity(), parent.getActivity().getMainLooper(), this);
        parent.getActivity().registerReceiver(this, intentFilter);
    }

    public void cancelDisconnect()
    {
        if(manager != null)
            manager.cancelConnect(channel, new android.net.wifi.p2p.WifiP2pManager.ActionListener() {

                public void onFailure(int i)
                {
                    PApplet.println((new StringBuilder("Connect abort request failed. Reason Code: ")).append(i).toString());
                }

                public void onSuccess()
                {
                    PApplet.println("Aborting connection");
                }

                final KetaiWiFiDirect this$0;

            
            {
                this$0 = KetaiWiFiDirect.this;
                super();
            }
            }
);
    }

    public void connect(String s)
    {
        Object obj = null;
        Iterator iterator = peers.iterator();
        do
        {
            do
            {
                if(!iterator.hasNext())
                {
                    Object obj1 = new WifiP2pConfig();
                    if(obj != null)
                        obj1.deviceAddress = ((WifiP2pDevice) (obj)).deviceAddress;
                    else
                        obj1.deviceAddress = s;
                    manager.connect(channel, ((WifiP2pConfig) (obj1)), new android.net.wifi.p2p.WifiP2pManager.ActionListener() {

                        public void onFailure(int i)
                        {
                            PApplet.println((new StringBuilder("Failed to connect to device (")).append(i).append(")").toString());
                        }

                        public void onSuccess()
                        {
                        }

                        final KetaiWiFiDirect this$0;

            
            {
                this$0 = KetaiWiFiDirect.this;
                super();
            }
                    }
);
                    return;
                }
                obj1 = (WifiP2pDevice)iterator.next();
            } while(((WifiP2pDevice) (obj1)).deviceAddress != s && ((WifiP2pDevice) (obj1)).deviceName != s);
            obj = obj1;
        } while(true);
    }

    public void disconnect()
    {
        manager.removeGroup(channel, new android.net.wifi.p2p.WifiP2pManager.ActionListener() {

            public void onFailure(int i)
            {
                PApplet.println((new StringBuilder("Disconnect failed. Reason :")).append(i).toString());
            }

            public void onSuccess()
            {
            }

            final KetaiWiFiDirect this$0;

            
            {
                this$0 = KetaiWiFiDirect.this;
                super();
            }
        }
);
    }

    public void discover()
    {
        if(manager != null)
            manager.discoverPeers(channel, this);
    }

    public void getConnectionInfo()
    {
        manager.requestConnectionInfo(channel, this);
    }

    public String getHardwareAddress()
    {
        return ((WifiManager)parent.getActivity().getSystemService("wifi")).getConnectionInfo().getMacAddress();
    }

    public String getIPAddress()
    {
        return ip;
    }

    public ArrayList getPeerNameList()
    {
        ArrayList arraylist = new ArrayList();
        Iterator iterator = peers.iterator();
        do
        {
            if(!iterator.hasNext())
                return arraylist;
            arraylist.add(((WifiP2pDevice)iterator.next()).deviceName);
        } while(true);
    }

    public void onChannelDisconnected()
    {
        if(manager != null && !retryChannel)
        {
            PApplet.println("Channel lost. Trying again");
            retryChannel = true;
            manager.initialize(parent.getActivity(), parent.getActivity().getMainLooper(), this);
        } else
        {
            PApplet.println("Severe! Channel is probably lost premanently. Try Disable/Re-Enable P2P.");
        }
    }

    public void onConnectionInfoAvailable(WifiP2pInfo wifip2pinfo)
    {
        if(!wifip2pinfo.groupFormed)
        {
            ip = "";
        } else
        {
            ip = wifip2pinfo.groupOwnerAddress.getHostAddress();
            PApplet.println((new StringBuilder("Connection info available for :")).append(wifip2pinfo.toString()).append("--").append(wifip2pinfo.groupOwnerAddress.getHostAddress()).toString());
        }
    }

    public void onFailure(int i)
    {
        i;
        JVM INSTR tableswitch 0 2: default 28
    //                   0 48
    //                   1 70
    //                   2 92;
           goto _L1 _L2 _L3 _L4
_L1:
        PApplet.println((new StringBuilder("WifiDirect failed ")).append(i).toString());
_L6:
        return;
_L2:
        PApplet.println((new StringBuilder("WifiDirect failed ")).append(i).toString());
        continue; /* Loop/switch isn't completed */
_L3:
        PApplet.println((new StringBuilder("WifiDirect failed ")).append(i).toString());
        continue; /* Loop/switch isn't completed */
_L4:
        PApplet.println((new StringBuilder("WifiDirect failed ")).append(i).toString());
        if(true) goto _L6; else goto _L5
_L5:
    }

    public void onPeersAvailable(WifiP2pDeviceList wifip2pdevicelist)
    {
        wifip2pdevicelist = wifip2pdevicelist.getDeviceList();
        if(wifip2pdevicelist.size() <= 0) goto _L2; else goto _L1
_L1:
        peers.clear();
        wifip2pdevicelist = wifip2pdevicelist.iterator();
_L5:
        if(wifip2pdevicelist.hasNext()) goto _L4; else goto _L3
_L3:
        PApplet.println("New KetaiWifiDirect peer list received:");
        wifip2pdevicelist = peers.iterator();
_L6:
        if(wifip2pdevicelist.hasNext())
            break MISSING_BLOCK_LABEL_87;
_L2:
        return;
_L4:
        peers.add((WifiP2pDevice)wifip2pdevicelist.next());
          goto _L5
        WifiP2pDevice wifip2pdevice = (WifiP2pDevice)wifip2pdevicelist.next();
        PApplet.println((new StringBuilder("\t\t")).append(wifip2pdevice.deviceName).append(":").append(wifip2pdevice.deviceAddress).toString());
          goto _L6
    }

    public void onReceive(Context context, Intent intent)
    {
        context = intent.getAction();
        if(!"android.net.wifi.p2p.STATE_CHANGED".equals(context)) goto _L2; else goto _L1
_L1:
        int i = intent.getIntExtra("wifi_p2p_state", -1);
        if(i == 2)
            setIsWifiP2pEnabled(true);
        else
            setIsWifiP2pEnabled(false);
        PApplet.println((new StringBuilder("P2P state changed - ")).append(i).toString());
_L4:
        return;
_L2:
        if("android.net.wifi.p2p.PEERS_CHANGED".equals(context))
        {
            if(manager != null)
                manager.requestPeers(channel, this);
            PApplet.println("P2P peers changed");
        } else
        if("android.net.wifi.p2p.CONNECTION_STATE_CHANGE".equals(context))
        {
            if(manager != null && ((NetworkInfo)intent.getParcelableExtra("networkInfo")).isConnected())
                manager.requestConnectionInfo(channel, this);
        } else
        if("android.net.wifi.p2p.THIS_DEVICE_CHANGED".equals(context))
            PApplet.println((new StringBuilder("p2p device changed")).append((WifiP2pDevice)intent.getParcelableExtra("wifiP2pDevice")).toString());
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void onSuccess()
    {
        PApplet.println("WifiDirect succeeded ");
    }

    public void pause()
    {
        parent.getActivity().unregisterReceiver(this);
    }

    public void reset()
    {
        peers.clear();
        manager.cancelConnect(channel, this);
        manager.removeGroup(channel, this);
    }

    public void resume()
    {
        parent.getActivity().registerReceiver(this, intentFilter);
    }

    public void setIsWifiP2pEnabled(boolean flag)
    {
        isWifiP2pEnabled = flag;
    }

    private android.net.wifi.p2p.WifiP2pManager.Channel channel;
    private final IntentFilter intentFilter = new IntentFilter();
    private String ip;
    private boolean isWifiP2pEnabled;
    private WifiP2pManager manager;
    PApplet parent;
    private List peers;
    private boolean retryChannel;
}
