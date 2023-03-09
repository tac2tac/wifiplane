// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package ketai.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import processing.core.PApplet;

public class SensorData
{

    SensorData(SensorEvent sensorevent)
    {
        sensorType = sensorevent.sensor.getType();
        accuracy = sensorevent.accuracy;
        timestamp = sensorevent.timestamp;
        values = new float[sensorevent.values.length];
        PApplet.arrayCopy(sensorevent.values, values);
    }

    public int accuracy;
    public int sensorType;
    public long timestamp;
    public float values[];
}
