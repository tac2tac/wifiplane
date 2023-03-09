// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.test.wifiplane;

import hypermedia.net.UDP;
import ketai.sensors.KetaiSensor;
import ketai.ui.KetaiVibrate;
import processing.core.PApplet;

public class wifiplane extends PApplet
{

    public wifiplane()
    {
        app_start = 1;
        DC_UPDATE = 1;
        P_ID = (byte)1;
        dc_count = 0;
        lock = 0;
        gas = 0;
        rssi = 0;
        vcc = 0;
        l_speed = 0;
        r_speed = 0;
        vib_count = 0;
        rst_count = 0;
        exprt_flag = 0;
        diff_power = 2.2F;
        remotPort = 6000;
        localPort = 2390;
        offsetl = 0;
        offsetr = 0;
        remotIp = "192.168.43.255";
    }

    public static void main(String args[])
    {
        String args1[] = new String[4];
        args1[0] = "--present";
        args1[1] = "--window-color=#666666";
        args1[2] = "--stop-color=#cccccc";
        args1[3] = "wifiplane";
        if(args != null)
            PApplet.main(concat(args1, args));
        else
            PApplet.main(args1);
    }

    public void draw()
    {
        background(125F, 255F, 200F);
        fill(255);
        stroke(163);
        rect(0.0F, 0.0F, width / 4, height / 4);
        rect((width * 3) / 4, 0.0F, width / 4, height / 4);
        rect(0.0F, height / 4, width / 4, height / 4);
        rect((width * 3) / 4, height / 4, width / 4, height / 4);
        rect(0.0F, (height * 7) / 8, width, height / 8);
        fill(color(255, 100, 60));
        rect(width / 4, 0.0F, width / 2, (height * 7) / 8);
        fill(color(100, 150, 255));
        rect(width / 4, 0.0F, width / 2, (height * 7) / 8 - (gas * 7 * height) / 1016);
        textSize(height / 12);
        textAlign(3, 3);
        fill(color(50, 100, 255));
        text("+", width / 8, height / 8 - 10);
        text("-", width / 8, (height * 3) / 8 - 10);
        text("+", (width * 3) / 4 + width / 8, height / 8 - 10);
        text("-", (width * 3) / 4 + width / 8, (height * 3) / 8 - 10);
        fill(0);
        text((gas * 100) / 127, width / 2, height / 2);
        text(offsetl, width / 8, height / 4 - 10);
        text(offsetr, (width * 3) / 4 + width / 8, height / 4 - 10);
        if(exprt_flag == 0)
            text("BG", width / 8, height / 2 + height / 6);
        else
        if(exprt_flag == 1)
            text("EX", width / 8, height / 2 + height / 6);
        if(lock == 0)
            text("LOCKED", width / 2, (height * 7) / 8 + height / 16);
        else
        if(lock == 1)
            text("ACTIVATED", width / 2, (height * 7) / 8 + height / 16);
        textSize(height / 14);
        fill(255);
        if(rssi == 0)
            text((new StringBuilder("-")).append(Character.toString('\u221E')).append("dBm").toString(), width / 2, (height * 3) / 4);
        else
            text((new StringBuilder("-")).append(rssi).append("dBm").toString(), width / 2, (height * 3) / 4);
        text((new StringBuilder(String.valueOf(vcc / 10))).append(".").append(vcc % 10).append("V").toString(), width / 2, (height * 3) / 4 + height / 12);
        fill(0);
        textSize(height / 30);
        textAlign(3, 3);
        text("Instructables", width / 2, height / 20);
        text("WiFi Plane App", width / 2, (height * 2) / 20);
        text("By Ravi Butani", width / 2, (height * 3) / 20);
        textSize(height / 12);
        delay(1);
        dc_count = dc_count + 1;
        if(dc_count >= DC_UPDATE)
        {
            rst_count = rst_count + 1;
            if(rst_count >= 200)
            {
                vcc = 0;
                rssi = 0;
            }
            dc_count = 0;
            Object obj;
            if(accelerometerX > 1.5F)
                accelerometerX = accelerometerX - 1.5F;
            else
            if(accelerometerX < -1.5F)
                accelerometerX = accelerometerX + 1.5F;
            else
                accelerometerX = 0.0F;
            l_speed = (int)((float)gas + (float)offsetl + accelerometerX * diff_power);
            r_speed = (int)(((float)gas + (float)offsetr) - accelerometerX * diff_power);
            if(l_speed >= 127)
                l_speed = 127;
            else
            if(l_speed <= 1)
                l_speed = 1;
            if(r_speed >= 127)
                r_speed = 127;
            else
            if(r_speed <= 1)
                r_speed = 1;
            obj = new byte[3];
            Object _tmp = obj;
            obj[0] = 49;
            obj[1] = 50;
            obj[2] = 51;
            obj[0] = P_ID;
            if(lock == 1)
            {
                obj[1] = (byte)l_speed;
                obj[2] = (byte)r_speed;
                vib_count = vib_count + 1;
                if(vcc < 35 && vib_count < 5)
                    vibe.vibrate(1000L);
                if(vib_count >= 40)
                    vib_count = 0;
            } else
            if(lock == 0)
            {
                obj[1] = (byte)1;
                obj[2] = (byte)1;
            }
            println(obj[1]);
            println(obj[2]);
            obj = new String(((byte []) (obj)));
            udp.send(((String) (obj)), remotIp, remotPort);
            println("msgsend");
        }
    }

    public void mouseDragged()
    {
        if(mouseY < (height * 7) / 8 && mouseX > width / 4 && mouseX < (width * 3) / 4 && lock == 1)
            gas = 127 - (int)(((float)mouseY / (float)((height * 7) / 8)) * 127F);
    }

    public void mousePressed()
    {
        if(mouseX >= width / 4 || mouseY >= height / 4) goto _L2; else goto _L1
_L1:
        offsetl = offsetl + 1;
_L4:
        return;
_L2:
        if(mouseX < width / 4 && mouseY < height / 2)
            offsetl = offsetl - 1;
        else
        if(mouseX > (width * 3) / 4 && mouseY < height / 4)
            offsetr = offsetr + 1;
        else
        if(mouseX > (width * 3) / 4 && mouseY < height / 2)
            offsetr = offsetr - 1;
        else
        if(mouseX < width / 4 && mouseY < (height * 3) / 4)
        {
            if(exprt_flag == 0)
            {
                exprt_flag = 1;
                diff_power = 3.9F;
            } else
            {
                exprt_flag = 0;
                diff_power = 2.2F;
            }
        } else
        if(mouseY > (height * 7) / 8)
        {
            gas = 0;
            if(lock == 0)
                lock = 1;
            else
                lock = 0;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void onAccelerometerEvent(float f, float f1, float f2)
    {
        accelerometerX = f;
        accelerometerY = f1;
        accelerometerZ = f2;
    }

    public void receive(byte abyte0[], String s, int i)
    {
        rst_count = 0;
        rssi = abyte0[1];
        vcc = abyte0[2] + 3;
    }

    public void settings()
    {
        size(displayWidth, displayHeight);
    }

    public void setup()
    {
        orientation(1);
        udp = new UDP(this, localPort);
        udp.listen(true);
        sensor = new KetaiSensor(this);
        vibe = new KetaiVibrate(this);
        sensor.start();
    }

    int DC_UPDATE;
    byte P_ID;
    float accelerometerX;
    float accelerometerY;
    float accelerometerZ;
    int app_start;
    int dc_count;
    float diff_power;
    int exprt_flag;
    int gas;
    int l_speed;
    int localPort;
    int lock;
    int offsetl;
    int offsetr;
    int r_speed;
    String remotIp;
    int remotPort;
    int rssi;
    int rst_count;
    KetaiSensor sensor;
    UDP udp;
    int vcc;
    int vib_count;
    KetaiVibrate vibe;
}
