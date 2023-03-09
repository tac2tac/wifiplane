// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package ketai.sensors;

import android.content.Context;
import android.hardware.*;
import java.lang.reflect.Method;
import java.util.*;
import processing.core.PApplet;

// Referenced classes of package ketai.sensors:
//            SensorQueue, SensorData

public class KetaiSensor
    implements SensorEventListener
{

    public KetaiSensor(PApplet papplet)
    {
        sensorManager = null;
        isRegistered = false;
        samplingRate = 2;
        parent = papplet;
        findParentIntentions();
        useSimulator = false;
        sensorManager = (SensorManager)parent.getContext().getSystemService("sensor");
        eventQueue = new SensorQueue();
        timeOfLastUpdate = 0L;
        delayInterval = 0L;
        parent.registerMethod("dispose", this);
        parent.registerMethod("post", this);
    }

    private void dequeueEvents()
    {
        do
        {
            if(!eventQueue.available())
                return;
            handleSensorEvent((SensorData)eventQueue.remove());
        } while(true);
    }

    private void findObjectIntentions(Object obj)
    {
        callbackdelegate = obj;
        try
        {
            onSensorEventMethod = obj.getClass().getMethod("onSensorEvent", new Class[] {
                android/hardware/SensorEvent
            });
        }
        catch(NoSuchMethodException nosuchmethodexception29) { }
        try
        {
            onAccelerometerEventMethod = obj.getClass().getMethod("onAccelerometerEvent", new Class[] {
                Float.TYPE, Float.TYPE, Float.TYPE, Long.TYPE, Integer.TYPE
            });
            accelerometerSensorEnabled = true;
            StringBuilder stringbuilder = JVM INSTR new #195 <Class StringBuilder>;
            stringbuilder.StringBuilder("Found onAccelerometerEvent\tMethod...in ");
            PApplet.println(stringbuilder.append(obj.getClass()).toString());
        }
        catch(NoSuchMethodException nosuchmethodexception28) { }
        try
        {
            onAccelerometerEventMethodSimple = obj.getClass().getMethod("onAccelerometerEvent", new Class[] {
                Float.TYPE, Float.TYPE, Float.TYPE
            });
            accelerometerSensorEnabled = true;
            PApplet.println("Found onAccelerometerEventMethod(simple)...");
        }
        catch(NoSuchMethodException nosuchmethodexception27) { }
        try
        {
            onOrientationSensorEventMethod = obj.getClass().getMethod("onOrientationEvent", new Class[] {
                Float.TYPE, Float.TYPE, Float.TYPE, Long.TYPE, Integer.TYPE
            });
            orientationSensorEnabled = true;
            PApplet.println("Found onOrientationEventMethod...");
        }
        catch(NoSuchMethodException nosuchmethodexception26) { }
        try
        {
            onOrientationSensorEventMethodSimple = obj.getClass().getMethod("onOrientationEvent", new Class[] {
                Float.TYPE, Float.TYPE, Float.TYPE
            });
            orientationSensorEnabled = true;
            PApplet.println("Found onOrientationEventMethod(simple)...");
        }
        catch(NoSuchMethodException nosuchmethodexception25) { }
        try
        {
            onMagneticFieldSensorEventMethod = obj.getClass().getMethod("onMagneticFieldEvent", new Class[] {
                Float.TYPE, Float.TYPE, Float.TYPE, Long.TYPE, Integer.TYPE
            });
            magneticFieldSensorEnabled = true;
            PApplet.println("Found onMagneticFieldEventMethod...");
        }
        catch(NoSuchMethodException nosuchmethodexception24) { }
        try
        {
            onMagneticFieldSensorEventMethodSimple = obj.getClass().getMethod("onMagneticFieldEvent", new Class[] {
                Float.TYPE, Float.TYPE, Float.TYPE
            });
            magneticFieldSensorEnabled = true;
            PApplet.println("Found onMagneticFieldEventMethod(simple)...");
        }
        catch(NoSuchMethodException nosuchmethodexception23) { }
        try
        {
            onGyroscopeSensorEventMethod = obj.getClass().getMethod("onGyroscopeEvent", new Class[] {
                Float.TYPE, Float.TYPE, Float.TYPE, Long.TYPE, Integer.TYPE
            });
            gyroscopeSensorEnabled = true;
            PApplet.println("Found onGyroscopeEventMethod...");
        }
        catch(NoSuchMethodException nosuchmethodexception22) { }
        try
        {
            onGyroscopeSensorEventMethodSimple = obj.getClass().getMethod("onGyroscopeEvent", new Class[] {
                Float.TYPE, Float.TYPE, Float.TYPE
            });
            gyroscopeSensorEnabled = true;
            PApplet.println("Found onGyroscopeEventMethod(simple)...");
        }
        catch(NoSuchMethodException nosuchmethodexception21) { }
        try
        {
            onGravitySensorEventMethod = obj.getClass().getMethod("onGravityEvent", new Class[] {
                Float.TYPE, Float.TYPE, Float.TYPE, Long.TYPE, Integer.TYPE
            });
            gravitySensorEnabled = true;
            PApplet.println("Found onGravityEvenMethod...");
        }
        catch(NoSuchMethodException nosuchmethodexception20) { }
        try
        {
            onGravitySensorEventMethodSimple = obj.getClass().getMethod("onGravityEvent", new Class[] {
                Float.TYPE, Float.TYPE, Float.TYPE
            });
            gravitySensorEnabled = true;
            PApplet.println("Found onGravityEvenMethod(simple)...");
        }
        catch(NoSuchMethodException nosuchmethodexception19) { }
        try
        {
            onProximitySensorEventMethod = obj.getClass().getMethod("onProximityEvent", new Class[] {
                Float.TYPE, Long.TYPE, Integer.TYPE
            });
            proximitySensorEnabled = true;
            PApplet.println("Found onLightEventMethod...");
        }
        catch(NoSuchMethodException nosuchmethodexception18) { }
        try
        {
            onProximitySensorEventMethodSimple = obj.getClass().getMethod("onProximityEvent", new Class[] {
                Float.TYPE
            });
            proximitySensorEnabled = true;
            PApplet.println("Found onProximityEventMethod(simple)...");
        }
        catch(NoSuchMethodException nosuchmethodexception17) { }
        try
        {
            onLightSensorEventMethod = obj.getClass().getMethod("onLightEvent", new Class[] {
                Float.TYPE, Long.TYPE, Integer.TYPE
            });
            lightSensorEnabled = true;
            PApplet.println("Found onLightEventMethod...");
        }
        catch(NoSuchMethodException nosuchmethodexception16) { }
        try
        {
            onLightSensorEventMethodSimple = obj.getClass().getMethod("onLightEvent", new Class[] {
                Float.TYPE
            });
            lightSensorEnabled = true;
            PApplet.println("Found onLightEventMethod(simple)...");
        }
        catch(NoSuchMethodException nosuchmethodexception15) { }
        try
        {
            onPressureSensorEventMethod = obj.getClass().getMethod("onPressureEvent", new Class[] {
                Float.TYPE, Long.TYPE, Integer.TYPE
            });
            pressureSensorEnabled = true;
            PApplet.println("Found onPressureEventMethod...");
        }
        catch(NoSuchMethodException nosuchmethodexception14) { }
        try
        {
            onPressureSensorEventMethodSimple = obj.getClass().getMethod("onPressureEvent", new Class[] {
                Float.TYPE
            });
            pressureSensorEnabled = true;
            PApplet.println("Found onPressureEventMethod(simple)...");
        }
        catch(NoSuchMethodException nosuchmethodexception13) { }
        try
        {
            onTemperatureSensorEventMethod = obj.getClass().getMethod("onTemperatureEvent", new Class[] {
                Float.TYPE, Long.TYPE, Integer.TYPE
            });
            temperatureSensorEnabled = true;
            PApplet.println("Found onTemperatureEventMethod...");
        }
        catch(NoSuchMethodException nosuchmethodexception12) { }
        try
        {
            onTemperatureSensorEventMethodSimple = obj.getClass().getMethod("onTemperatureEvent", new Class[] {
                Float.TYPE
            });
            temperatureSensorEnabled = true;
            PApplet.println("Found onTemperatureEventMethod(simple)...");
        }
        catch(NoSuchMethodException nosuchmethodexception11) { }
        try
        {
            onLinearAccelerationSensorEventMethod = obj.getClass().getMethod("onLinearAccelerationEvent", new Class[] {
                Float.TYPE, Float.TYPE, Float.TYPE, Long.TYPE, Integer.TYPE
            });
            linearAccelerationSensorEnabled = true;
            PApplet.println("Found onLinearAccelerationEventMethod...");
        }
        catch(NoSuchMethodException nosuchmethodexception10) { }
        try
        {
            onLinearAccelerationSensorEventMethodSimple = obj.getClass().getMethod("onLinearAccelerationEvent", new Class[] {
                Float.TYPE, Float.TYPE, Float.TYPE
            });
            linearAccelerationSensorEnabled = true;
            PApplet.println("Found onLinearAccelerationEventMethod(simple)...");
        }
        catch(NoSuchMethodException nosuchmethodexception9) { }
        try
        {
            onRotationVectorSensorEventMethod = obj.getClass().getMethod("onRotationVectorEvent", new Class[] {
                Float.TYPE, Float.TYPE, Float.TYPE, Long.TYPE, Integer.TYPE
            });
            rotationVectorSensorEnabled = true;
            PApplet.println("Found onRotationVectorEvenMethod...");
        }
        catch(NoSuchMethodException nosuchmethodexception8) { }
        try
        {
            onRotationVectorSensorEventMethodSimple = obj.getClass().getMethod("onRotationVectorEvent", new Class[] {
                Float.TYPE, Float.TYPE, Float.TYPE
            });
            rotationVectorSensorEnabled = true;
            PApplet.println("Found onRotationVectorEventMethod(simple)...");
        }
        catch(NoSuchMethodException nosuchmethodexception7) { }
        try
        {
            onAmbientTemperatureEventMethod = obj.getClass().getMethod("onAmibentTemperatureEvent", new Class[] {
                Float.TYPE
            });
            ambientTemperatureSensorEnabled = true;
            PApplet.println("Found onAmbientTemperatureEvent callback...");
        }
        catch(NoSuchMethodException nosuchmethodexception6) { }
        try
        {
            onRelativeHumidityEventMethod = obj.getClass().getMethod("onRelativeHumidityEvent", new Class[] {
                Float.TYPE
            });
            relativeHumiditySensorEnabled = true;
            PApplet.println("Found onRelativeHumidityEventMethod...");
        }
        catch(NoSuchMethodException nosuchmethodexception5) { }
        try
        {
            onStepDetectorEventMethod = obj.getClass().getMethod("onStepDetectorEvent", new Class[0]);
            stepDetectorSensorEnabled = true;
            PApplet.println("Found onStepDetectorEvent...");
        }
        catch(NoSuchMethodException nosuchmethodexception4) { }
        try
        {
            onStepCounterEventMethod = obj.getClass().getMethod("onStepCounterEvent", new Class[] {
                Float.TYPE
            });
            stepCounterSensorEnabled = true;
            PApplet.println("Found onStepCounterEvent...");
        }
        catch(NoSuchMethodException nosuchmethodexception3) { }
        try
        {
            onSignificantMotionEventMethod = obj.getClass().getMethod("onSignificantMotionEvent", new Class[0]);
            significantMotionSensorEnabled = true;
            PApplet.println("Found onSignificantMotionEvent...");
        }
        catch(NoSuchMethodException nosuchmethodexception2) { }
        try
        {
            onHeartRateEventMethod = obj.getClass().getMethod("onHeartRateEvent", new Class[] {
                Float.TYPE
            });
            heartRateSensorEnabled = true;
            PApplet.println("Found onHeartRateEvent...");
        }
        catch(NoSuchMethodException nosuchmethodexception1) { }
        try
        {
            onGeomagneticRotationVectorEventMethod = obj.getClass().getMethod("onGeomagneticRotationVectorEvent", new Class[] {
                Float.TYPE, Float.TYPE, Float.TYPE
            });
            geomagneticRotationVectorSensorEnabled = true;
            PApplet.println("Found onGeomagneticRotationVectorEvent...");
        }
        catch(NoSuchMethodException nosuchmethodexception) { }
        onGameRotationEventMethod = obj.getClass().getMethod("onGameRotationEvent", new Class[] {
            Float.TYPE, Float.TYPE, Float.TYPE
        });
        gameRotationSensorEnabled = true;
        PApplet.println("Found onGameRotationEvent...");
_L2:
        return;
        obj;
        if(true) goto _L2; else goto _L1
_L1:
    }

    private void findParentIntentions()
    {
        findObjectIntentions(parent);
    }

    public static void getRotationMatrixFromVector(float af[], float af1[])
    {
        SensorManager.getRotationMatrixFromVector(af, af1);
    }

    private void handleSensorEvent(SensorData sensordata)
    {
        if(sensordata.sensorType != 1 || !accelerometerSensorEnabled) goto _L2; else goto _L1
_L1:
        accelerometerData = (float[])sensordata.values.clone();
        if(onAccelerometerEventMethod == null) goto _L4; else goto _L3
_L3:
        onAccelerometerEventMethod.invoke(callbackdelegate, new Object[] {
            Float.valueOf(sensordata.values[0]), Float.valueOf(sensordata.values[1]), Float.valueOf(sensordata.values[2]), Long.valueOf(sensordata.timestamp), Integer.valueOf(sensordata.accuracy)
        });
_L6:
        return;
        Exception exception;
        exception;
        PApplet.println((new StringBuilder("Error onAccelerometerEvent():")).append(exception.getMessage()).toString());
        exception.printStackTrace();
_L4:
        if(onAccelerometerEventMethodSimple == null) goto _L2; else goto _L5
_L5:
        onAccelerometerEventMethodSimple.invoke(callbackdelegate, new Object[] {
            Float.valueOf(sensordata.values[0]), Float.valueOf(sensordata.values[1]), Float.valueOf(sensordata.values[2])
        });
          goto _L6
        exception;
        PApplet.println((new StringBuilder("Error onAccelerometerEvent() [simple]:")).append(exception.getMessage()).toString());
        exception.printStackTrace();
_L2:
        if(sensordata.sensorType != 9 || !gravitySensorEnabled)
            break MISSING_BLOCK_LABEL_443;
        if(onGravitySensorEventMethod == null)
            break MISSING_BLOCK_LABEL_353;
        onGravitySensorEventMethod.invoke(callbackdelegate, new Object[] {
            Float.valueOf(sensordata.values[0]), Float.valueOf(sensordata.values[1]), Float.valueOf(sensordata.values[2]), Long.valueOf(sensordata.timestamp), Integer.valueOf(sensordata.accuracy)
        });
          goto _L6
        exception;
        PApplet.println((new StringBuilder("Error onGravityEvent():")).append(exception.getMessage()).toString());
        exception.printStackTrace();
        if(onGravitySensorEventMethodSimple == null)
            break MISSING_BLOCK_LABEL_443;
        onGravitySensorEventMethodSimple.invoke(callbackdelegate, new Object[] {
            Float.valueOf(sensordata.values[0]), Float.valueOf(sensordata.values[1]), Float.valueOf(sensordata.values[2])
        });
          goto _L6
        exception;
        PApplet.println((new StringBuilder("Error onGravityEvent()[simple]:")).append(exception.getMessage()).toString());
        exception.printStackTrace();
        if(sensordata.sensorType != 3 || !orientationSensorEnabled)
            break MISSING_BLOCK_LABEL_658;
        if(onOrientationSensorEventMethod == null)
            break MISSING_BLOCK_LABEL_568;
        onOrientationSensorEventMethod.invoke(callbackdelegate, new Object[] {
            Float.valueOf(sensordata.values[0]), Float.valueOf(sensordata.values[1]), Float.valueOf(sensordata.values[2]), Long.valueOf(sensordata.timestamp), Integer.valueOf(sensordata.accuracy)
        });
          goto _L6
        exception;
        PApplet.println((new StringBuilder("Error onOrientationEvent():")).append(exception.getMessage()).toString());
        exception.printStackTrace();
        if(onOrientationSensorEventMethodSimple == null)
            break MISSING_BLOCK_LABEL_658;
        onOrientationSensorEventMethodSimple.invoke(callbackdelegate, new Object[] {
            Float.valueOf(sensordata.values[0]), Float.valueOf(sensordata.values[1]), Float.valueOf(sensordata.values[2])
        });
          goto _L6
        exception;
        PApplet.println((new StringBuilder("Error onOrientationEvent()[simple] :")).append(exception.getMessage()).toString());
        exception.printStackTrace();
        if(sensordata.sensorType != 2 || !magneticFieldSensorEnabled)
            break MISSING_BLOCK_LABEL_887;
        magnetometerData = (float[])sensordata.values.clone();
        if(onMagneticFieldSensorEventMethod == null)
            break MISSING_BLOCK_LABEL_797;
        onMagneticFieldSensorEventMethod.invoke(callbackdelegate, new Object[] {
            Float.valueOf(sensordata.values[0]), Float.valueOf(sensordata.values[1]), Float.valueOf(sensordata.values[2]), Long.valueOf(sensordata.timestamp), Integer.valueOf(sensordata.accuracy)
        });
          goto _L6
        exception;
        PApplet.println((new StringBuilder("Error onMagneticFieldEvent():")).append(exception.getMessage()).toString());
        exception.printStackTrace();
        if(onMagneticFieldSensorEventMethodSimple == null)
            break MISSING_BLOCK_LABEL_887;
        onMagneticFieldSensorEventMethodSimple.invoke(callbackdelegate, new Object[] {
            Float.valueOf(sensordata.values[0]), Float.valueOf(sensordata.values[1]), Float.valueOf(sensordata.values[2])
        });
          goto _L6
        exception;
        PApplet.println((new StringBuilder("Error onMagneticFieldEvent()[simple]:")).append(exception.getMessage()).toString());
        exception.printStackTrace();
        if(sensordata.sensorType != 4 || !gyroscopeSensorEnabled)
            break MISSING_BLOCK_LABEL_1102;
        if(onGyroscopeSensorEventMethod == null)
            break MISSING_BLOCK_LABEL_1012;
        onGyroscopeSensorEventMethod.invoke(callbackdelegate, new Object[] {
            Float.valueOf(sensordata.values[0]), Float.valueOf(sensordata.values[1]), Float.valueOf(sensordata.values[2]), Long.valueOf(sensordata.timestamp), Integer.valueOf(sensordata.accuracy)
        });
          goto _L6
        exception;
        PApplet.println((new StringBuilder("Error onGyroscopeEvent():")).append(exception.getMessage()).toString());
        exception.printStackTrace();
        if(onGyroscopeSensorEventMethodSimple == null)
            break MISSING_BLOCK_LABEL_1102;
        onGyroscopeSensorEventMethodSimple.invoke(callbackdelegate, new Object[] {
            Float.valueOf(sensordata.values[0]), Float.valueOf(sensordata.values[1]), Float.valueOf(sensordata.values[2])
        });
          goto _L6
        exception;
        PApplet.println((new StringBuilder("Error onGyroscopeEvent()[simple]:")).append(exception.getMessage()).toString());
        exception.printStackTrace();
        if(sensordata.sensorType != 5 || !lightSensorEnabled)
            break MISSING_BLOCK_LABEL_1269;
        if(onLightSensorEventMethod == null)
            break MISSING_BLOCK_LABEL_1203;
        onLightSensorEventMethod.invoke(callbackdelegate, new Object[] {
            Float.valueOf(sensordata.values[0]), Long.valueOf(sensordata.timestamp), Integer.valueOf(sensordata.accuracy)
        });
          goto _L6
        exception;
        PApplet.println((new StringBuilder("Error onLightEvent():")).append(exception.getMessage()).toString());
        exception.printStackTrace();
        if(onLightSensorEventMethodSimple == null)
            break MISSING_BLOCK_LABEL_1269;
        onLightSensorEventMethodSimple.invoke(callbackdelegate, new Object[] {
            Float.valueOf(sensordata.values[0])
        });
          goto _L6
        exception;
        PApplet.println((new StringBuilder("Error onLightEvent()[simple]r:")).append(exception.getMessage()).toString());
        exception.printStackTrace();
        if(sensordata.sensorType != 8 || !proximitySensorEnabled)
            break MISSING_BLOCK_LABEL_1437;
        if(onProximitySensorEventMethod == null)
            break MISSING_BLOCK_LABEL_1371;
        onProximitySensorEventMethod.invoke(callbackdelegate, new Object[] {
            Float.valueOf(sensordata.values[0]), Long.valueOf(sensordata.timestamp), Integer.valueOf(sensordata.accuracy)
        });
          goto _L6
        exception;
        PApplet.println((new StringBuilder("Error onProximityEvent():")).append(exception.getMessage()).toString());
        exception.printStackTrace();
        if(onProximitySensorEventMethodSimple == null)
            break MISSING_BLOCK_LABEL_1437;
        onProximitySensorEventMethodSimple.invoke(callbackdelegate, new Object[] {
            Float.valueOf(sensordata.values[0])
        });
          goto _L6
        exception;
        PApplet.println((new StringBuilder("Error onProximityEvent()[simple]:")).append(exception.getMessage()).toString());
        exception.printStackTrace();
        if(sensordata.sensorType != 6 || !pressureSensorEnabled)
            break MISSING_BLOCK_LABEL_1605;
        if(onPressureSensorEventMethod == null)
            break MISSING_BLOCK_LABEL_1539;
        onPressureSensorEventMethod.invoke(callbackdelegate, new Object[] {
            Float.valueOf(sensordata.values[0]), Long.valueOf(sensordata.timestamp), Integer.valueOf(sensordata.accuracy)
        });
          goto _L6
        exception;
        PApplet.println((new StringBuilder("Error onPressureEvent()r:")).append(exception.getMessage()).toString());
        exception.printStackTrace();
        if(onPressureSensorEventMethodSimple == null)
            break MISSING_BLOCK_LABEL_1605;
        onPressureSensorEventMethodSimple.invoke(callbackdelegate, new Object[] {
            Float.valueOf(sensordata.values[0])
        });
          goto _L6
        exception;
        PApplet.println((new StringBuilder("Error onPressureEvent()[simple]:")).append(exception.getMessage()).toString());
        exception.printStackTrace();
        if(sensordata.sensorType != 7 || !temperatureSensorEnabled)
            break MISSING_BLOCK_LABEL_1773;
        if(onTemperatureSensorEventMethod == null)
            break MISSING_BLOCK_LABEL_1707;
        onTemperatureSensorEventMethod.invoke(callbackdelegate, new Object[] {
            Float.valueOf(sensordata.values[0]), Long.valueOf(sensordata.timestamp), Integer.valueOf(sensordata.accuracy)
        });
          goto _L6
        exception;
        PApplet.println((new StringBuilder("Error onTemperatureEvent():")).append(exception.getMessage()).toString());
        exception.printStackTrace();
        if(onTemperatureSensorEventMethodSimple == null)
            break MISSING_BLOCK_LABEL_1773;
        onTemperatureSensorEventMethodSimple.invoke(callbackdelegate, new Object[] {
            Float.valueOf(sensordata.values[0])
        });
          goto _L6
        exception;
        PApplet.println((new StringBuilder("Error onTemperatureEvent()[simple]:")).append(exception.getMessage()).toString());
        exception.printStackTrace();
        if(sensordata.sensorType != 10 || !linearAccelerationSensorEnabled)
            break MISSING_BLOCK_LABEL_1989;
        if(onLinearAccelerationSensorEventMethod == null)
            break MISSING_BLOCK_LABEL_1899;
        onLinearAccelerationSensorEventMethod.invoke(callbackdelegate, new Object[] {
            Float.valueOf(sensordata.values[0]), Float.valueOf(sensordata.values[1]), Float.valueOf(sensordata.values[2]), Long.valueOf(sensordata.timestamp), Integer.valueOf(sensordata.accuracy)
        });
          goto _L6
        exception;
        PApplet.println((new StringBuilder("Error onLinearAccelerationEvent():")).append(exception.getMessage()).toString());
        exception.printStackTrace();
        if(onLinearAccelerationSensorEventMethodSimple == null)
            break MISSING_BLOCK_LABEL_1989;
        onLinearAccelerationSensorEventMethodSimple.invoke(callbackdelegate, new Object[] {
            Float.valueOf(sensordata.values[0]), Float.valueOf(sensordata.values[1]), Float.valueOf(sensordata.values[2])
        });
          goto _L6
        exception;
        PApplet.println((new StringBuilder("Error onLinearAccelerationEvent()[simple]:")).append(exception.getMessage()).toString());
        exception.printStackTrace();
        if(sensordata.sensorType != 11 || !rotationVectorSensorEnabled)
            break MISSING_BLOCK_LABEL_2205;
        if(onRotationVectorSensorEventMethod == null)
            break MISSING_BLOCK_LABEL_2115;
        onRotationVectorSensorEventMethod.invoke(callbackdelegate, new Object[] {
            Float.valueOf(sensordata.values[0]), Float.valueOf(sensordata.values[1]), Float.valueOf(sensordata.values[2]), Long.valueOf(sensordata.timestamp), Integer.valueOf(sensordata.accuracy)
        });
          goto _L6
        exception;
        PApplet.println((new StringBuilder("Error onRotationVectorEvent():")).append(exception.getMessage()).toString());
        exception.printStackTrace();
        if(onRotationVectorSensorEventMethodSimple == null)
            break MISSING_BLOCK_LABEL_2205;
        onRotationVectorSensorEventMethodSimple.invoke(callbackdelegate, new Object[] {
            Float.valueOf(sensordata.values[0]), Float.valueOf(sensordata.values[1]), Float.valueOf(sensordata.values[2])
        });
          goto _L6
        exception;
        PApplet.println((new StringBuilder("Error onRotationVectorEvent()[simple]:")).append(exception.getMessage()).toString());
        exception.printStackTrace();
        if(sensordata.sensorType != 13 || !ambientTemperatureSensorEnabled || onAmbientTemperatureEventMethod == null)
            break MISSING_BLOCK_LABEL_2287;
        onAmbientTemperatureEventMethod.invoke(callbackdelegate, new Object[] {
            Float.valueOf(sensordata.values[0])
        });
          goto _L6
        exception;
        PApplet.println((new StringBuilder("Error onAmbientTemperatureEvent():")).append(exception.getMessage()).toString());
        exception.printStackTrace();
        if(sensordata.sensorType != 12 || !relativeHumiditySensorEnabled || onRelativeHumidityEventMethod == null)
            break MISSING_BLOCK_LABEL_2369;
        onRelativeHumidityEventMethod.invoke(callbackdelegate, new Object[] {
            Float.valueOf(sensordata.values[0])
        });
          goto _L6
        exception;
        PApplet.println((new StringBuilder("Error onRelativeHumidityEventMethod():")).append(exception.getMessage()).toString());
        exception.printStackTrace();
        if(sensordata.sensorType != 18 || !stepDetectorSensorEnabled || onStepDetectorEventMethod == null)
            break MISSING_BLOCK_LABEL_2439;
        onStepDetectorEventMethod.invoke(callbackdelegate, new Object[0]);
          goto _L6
        exception;
        PApplet.println((new StringBuilder("Error onStepDetectorEventMethod():")).append(exception.getMessage()).toString());
        exception.printStackTrace();
        if(sensordata.sensorType != 19 || !stepCounterSensorEnabled || onStepCounterEventMethod == null)
            break MISSING_BLOCK_LABEL_2521;
        onStepCounterEventMethod.invoke(callbackdelegate, new Object[] {
            Float.valueOf(sensordata.values[0])
        });
          goto _L6
        exception;
        PApplet.println((new StringBuilder("Error onStepCounterEventMethod():")).append(exception.getMessage()).toString());
        exception.printStackTrace();
        if(sensordata.sensorType != 17 || !significantMotionSensorEnabled || onSignificantMotionEventMethod == null)
            break MISSING_BLOCK_LABEL_2601;
        PApplet.println("significant motion data: ");
        PApplet.println(sensordata);
        onSignificantMotionEventMethod.invoke(callbackdelegate, new Object[0]);
          goto _L6
        exception;
        PApplet.println((new StringBuilder("Error onSignificantMotionEventMethod():")).append(exception.getMessage()).toString());
        exception.printStackTrace();
        if(sensordata.sensorType != 20 || !geomagneticRotationVectorSensorEnabled || onGeomagneticRotationVectorEventMethod == null)
            break MISSING_BLOCK_LABEL_2707;
        onGeomagneticRotationVectorEventMethod.invoke(callbackdelegate, new Object[] {
            Float.valueOf(sensordata.values[0]), Float.valueOf(sensordata.values[1]), Float.valueOf(sensordata.values[2])
        });
          goto _L6
        exception;
        PApplet.println((new StringBuilder("Error onGeomagneticRotationVectorEventMethod():")).append(exception.getMessage()).toString());
        exception.printStackTrace();
        if(sensordata.sensorType == 15 && gameRotationSensorEnabled && onGameRotationEventMethod != null)
            try
            {
                onGameRotationEventMethod.invoke(callbackdelegate, new Object[] {
                    Float.valueOf(sensordata.values[0]), Float.valueOf(sensordata.values[1]), Float.valueOf(sensordata.values[2])
                });
            }
            // Misplaced declaration of an exception variable
            catch(SensorData sensordata)
            {
                PApplet.println((new StringBuilder("Error onGameRotationEventMethod():")).append(sensordata.getMessage()).toString());
                sensordata.printStackTrace();
            }
          goto _L6
    }

    private boolean isSensorSupported(int i)
    {
        Iterator iterator = sensorManager.getSensorList(-1).iterator();
_L2:
        boolean flag;
        if(iterator.hasNext())
            continue; /* Loop/switch isn't completed */
        flag = false;
_L3:
        return flag;
        if(i != ((Sensor)iterator.next()).getType()) goto _L2; else goto _L1
_L1:
        flag = true;
          goto _L3
        if(true) goto _L2; else goto _L4
_L4:
    }

    public static boolean remapCoordinateSystem(float af[], int i, int j, float af1[])
    {
        return SensorManager.remapCoordinateSystem(af, i, j, af1);
    }

    public void disableAccelerometer()
    {
        accelerometerSensorEnabled = true;
    }

    public void disableAmibentTemperature()
    {
        ambientTemperatureSensorEnabled = false;
    }

    public void disableGameRotationSensor()
    {
        gameRotationSensorEnabled = false;
    }

    public void disableGeomagneticRotationVectorSensor()
    {
        geomagneticRotationVectorSensorEnabled = false;
    }

    public void disableGyroscope()
    {
        gyroscopeSensorEnabled = false;
    }

    public void disableHeartRateSensor()
    {
        heartRateSensorEnabled = false;
    }

    public void disableLight()
    {
        lightSensorEnabled = true;
    }

    public void disableMagneticField()
    {
        magneticFieldSensorEnabled = true;
    }

    public void disableOrientation()
    {
        orientationSensorEnabled = false;
    }

    public void disablePressure()
    {
        pressureSensorEnabled = true;
    }

    public void disableProximity()
    {
        proximitySensorEnabled = false;
    }

    public void disableRelativeHumiditySensor()
    {
        relativeHumiditySensorEnabled = false;
    }

    public void disableRotationVector()
    {
        rotationVectorSensorEnabled = false;
    }

    public void disableSignificantMotionSensor()
    {
        significantMotionSensorEnabled = false;
    }

    public void disableStepCounterSensor()
    {
        stepCounterSensorEnabled = false;
    }

    public void disableStepDetectorSensor()
    {
        stepDetectorSensorEnabled = false;
    }

    public void disableTemperature()
    {
        temperatureSensorEnabled = false;
    }

    public void disablelinearAcceleration()
    {
        linearAccelerationSensorEnabled = false;
    }

    public void dispose()
    {
        stop();
    }

    public void enableAccelerometer()
    {
        accelerometerSensorEnabled = true;
    }

    public void enableAllSensors()
    {
        stepDetectorSensorEnabled = true;
        stepCounterSensorEnabled = true;
        significantMotionSensorEnabled = true;
        heartRateSensorEnabled = true;
        geomagneticRotationVectorSensorEnabled = true;
        gameRotationSensorEnabled = true;
        relativeHumiditySensorEnabled = true;
        ambientTemperatureSensorEnabled = true;
        rotationVectorSensorEnabled = true;
        linearAccelerationSensorEnabled = true;
        gyroscopeSensorEnabled = true;
        temperatureSensorEnabled = true;
        pressureSensorEnabled = true;
        lightSensorEnabled = true;
        proximitySensorEnabled = true;
        orientationSensorEnabled = true;
        magneticFieldSensorEnabled = true;
        accelerometerSensorEnabled = true;
    }

    public void enableAmibentTemperature()
    {
        ambientTemperatureSensorEnabled = true;
    }

    public void enableGameRotationSensor()
    {
        gameRotationSensorEnabled = true;
    }

    public void enableGeomagneticRotationVectorSensor()
    {
        geomagneticRotationVectorSensorEnabled = true;
    }

    public void enableGyroscope()
    {
        gyroscopeSensorEnabled = true;
    }

    public void enableHeartRateSensor()
    {
        heartRateSensorEnabled = true;
    }

    public void enableLight()
    {
        lightSensorEnabled = true;
    }

    public void enableLinearAcceleration()
    {
        linearAccelerationSensorEnabled = true;
    }

    public void enableMagenticField()
    {
        magneticFieldSensorEnabled = true;
    }

    public void enableOrientation()
    {
        orientationSensorEnabled = true;
    }

    public void enablePressure()
    {
        pressureSensorEnabled = true;
    }

    public void enableProximity()
    {
        proximitySensorEnabled = true;
    }

    public void enableRelativeHumiditySensor()
    {
        relativeHumiditySensorEnabled = true;
    }

    public void enableRotationVector()
    {
        rotationVectorSensorEnabled = true;
    }

    public void enableSignificantMotionSensor()
    {
        significantMotionSensorEnabled = true;
    }

    public void enableStepCounterSensor()
    {
        stepCounterSensorEnabled = true;
    }

    public void enableStepDetectorSensor()
    {
        stepDetectorSensorEnabled = true;
    }

    public void enableTemperature()
    {
        temperatureSensorEnabled = true;
    }

    public float[] getOrientation()
    {
        float af[];
        if(!isStarted() || !accelerometerSensorEnabled || !magneticFieldSensorEnabled)
        {
            PApplet.println("Cannot compute orientation until sensor service is started and accelerometer and magnetometer must also be enabled.");
            af = (float[])zeroes.clone();
        } else
        if(accelerometerData != null && magnetometerData != null)
        {
            if(rotationMat == null)
                rotationMat = new float[16];
            if(inclinationMat == null)
                inclinationMat = new float[9];
            if(orientationVec == null)
                orientationVec = new float[3];
            if(SensorManager.getRotationMatrix(rotationMat, inclinationMat, accelerometerData, magnetometerData))
            {
                SensorManager.getOrientation(rotationMat, orientationVec);
                af = (float[])orientationVec.clone();
            } else
            {
                af = (float[])zeroes.clone();
            }
        } else
        {
            af = (float[])zeroes.clone();
        }
        return af;
    }

    public float[] getOrientation(float af[])
    {
        float af1[] = af;
        if(af == null)
            af1 = new float[3];
        if(!isStarted() || !accelerometerSensorEnabled || !magneticFieldSensorEnabled)
        {
            PApplet.println("Cannot compute orientation until sensor service is started and accelerometer and magnetometer must also be enabled.");
            PApplet.arrayCopy(zeroes, af1);
        }
        if(accelerometerData != null && magnetometerData != null)
        {
            if(rotationMat == null)
                rotationMat = new float[16];
            if(inclinationMat == null)
                inclinationMat = new float[9];
            if(SensorManager.getRotationMatrix(rotationMat, inclinationMat, accelerometerData, magnetometerData))
                SensorManager.getOrientation(rotationMat, af1);
            else
                PApplet.arrayCopy(zeroes, af1);
        } else
        {
            PApplet.arrayCopy(zeroes, af1);
        }
        return af1;
    }

    public void getQuaternionFromVector(float af[], float af1[])
    {
        SensorManager.getQuaternionFromVector(af, af1);
    }

    public String getServiceDescription()
    {
        return "Android Sensors.";
    }

    public int getStatus()
    {
        return 0;
    }

    public boolean isAccelerometerAvailable()
    {
        return isSensorSupported(1);
    }

    public boolean isAmbientTemperatureAvailable()
    {
        return isSensorSupported(13);
    }

    public boolean isGameRotationAvailable()
    {
        return isSensorSupported(15);
    }

    public boolean isGeomagneticRotationVectorAvailable()
    {
        return isSensorSupported(20);
    }

    public boolean isGyroscopeAvailable()
    {
        return isSensorSupported(4);
    }

    public boolean isLightAvailable()
    {
        return isSensorSupported(5);
    }

    public boolean isLinearAccelerationAvailable()
    {
        return isSensorSupported(10);
    }

    public boolean isMagenticFieldAvailable()
    {
        return isSensorSupported(2);
    }

    public boolean isOrientationAvailable()
    {
        return isSensorSupported(3);
    }

    public boolean isPressureAvailable()
    {
        return isSensorSupported(6);
    }

    public boolean isProximityAvailable()
    {
        return isSensorSupported(8);
    }

    public boolean isRelativeHumidityAvailable()
    {
        return isSensorSupported(12);
    }

    public boolean isRotationVectorAvailable()
    {
        return isSensorSupported(11);
    }

    public boolean isSignificantMotionAvailable()
    {
        return isSensorSupported(17);
    }

    public boolean isStarted()
    {
        return isRegistered;
    }

    public boolean isStepCounterAvailable()
    {
        return isSensorSupported(19);
    }

    public boolean isStepDetectorAvailable()
    {
        return isSensorSupported(18);
    }

    public boolean isTemperatureAvailable()
    {
        return isSensorSupported(7);
    }

    public Collection list()
    {
        Vector vector = new Vector();
        Iterator iterator = sensorManager.getSensorList(-1).iterator();
        do
        {
            if(!iterator.hasNext())
                return vector;
            Sensor sensor = (Sensor)iterator.next();
            vector.add(sensor.getName());
            PApplet.println((new StringBuilder("\tKetaiSensor sensor: ")).append(sensor.getName()).append(":").append(sensor.getType()).toString());
        } while(true);
    }

    public void onAccuracyChanged(Sensor sensor, int i)
    {
    }

    public void onSensorChanged(SensorEvent sensorevent)
    {
        long l = (new Date()).getTime();
        if(l >= timeOfLastUpdate + delayInterval) goto _L2; else goto _L1
_L1:
        return;
_L2:
        if(onSensorEventMethod != null)
            try
            {
                onSensorEventMethod.invoke(callbackdelegate, new Object[] {
                    sensorevent
                });
                continue; /* Loop/switch isn't completed */
            }
            catch(Exception exception)
            {
                PApplet.println((new StringBuilder("Disabling onSensorEvent() because of an error:")).append(exception.getMessage()).toString());
                exception.printStackTrace();
                onSensorEventMethod = null;
            }
        eventQueue.add(new SensorData(sensorevent));
        timeOfLastUpdate = l;
        if(true) goto _L1; else goto _L3
_L3:
    }

    public void post()
    {
        dequeueEvents();
    }

    public void register(Object obj)
    {
        PApplet.println((new StringBuilder("KetaiSensor delegating Events to class: ")).append(obj.getClass()).toString());
        findObjectIntentions(obj);
    }

    public void setDelayInterval(long l)
    {
        delayInterval = l;
    }

    public void setSamplingRate(int i)
    {
        samplingRate = i;
    }

    public void start()
    {
        PApplet.println("KetaiSensor: start()...");
        if(accelerometerSensorEnabled)
        {
            Sensor sensor = sensorManager.getDefaultSensor(1);
            if(sensor != null)
                sensorManager.registerListener(this, sensor, samplingRate);
        }
        if(magneticFieldSensorEnabled)
        {
            Sensor sensor1 = sensorManager.getDefaultSensor(2);
            if(sensor1 != null)
                sensorManager.registerListener(this, sensor1, samplingRate);
        }
        if(pressureSensorEnabled)
        {
            Sensor sensor2 = sensorManager.getDefaultSensor(6);
            if(sensor2 != null)
                sensorManager.registerListener(this, sensor2, samplingRate);
        }
        if(orientationSensorEnabled)
        {
            Sensor sensor3 = sensorManager.getDefaultSensor(3);
            if(sensor3 != null)
                sensorManager.registerListener(this, sensor3, samplingRate);
        }
        if(proximitySensorEnabled)
        {
            Sensor sensor4 = sensorManager.getDefaultSensor(8);
            if(sensor4 != null)
                sensorManager.registerListener(this, sensor4, samplingRate);
        }
        if(temperatureSensorEnabled)
        {
            Sensor sensor5 = sensorManager.getDefaultSensor(7);
            if(sensor5 != null)
                sensorManager.registerListener(this, sensor5, samplingRate);
        }
        if(gyroscopeSensorEnabled)
        {
            Sensor sensor6 = sensorManager.getDefaultSensor(4);
            if(sensor6 != null)
                sensorManager.registerListener(this, sensor6, samplingRate);
        }
        if(rotationVectorSensorEnabled)
        {
            Sensor sensor7 = sensorManager.getDefaultSensor(11);
            if(sensor7 != null)
                sensorManager.registerListener(this, sensor7, samplingRate);
        }
        if(linearAccelerationSensorEnabled)
        {
            Sensor sensor8 = sensorManager.getDefaultSensor(10);
            if(sensor8 != null)
                sensorManager.registerListener(this, sensor8, samplingRate);
        }
        if(lightSensorEnabled)
        {
            Sensor sensor9 = sensorManager.getDefaultSensor(5);
            if(sensor9 != null)
                sensorManager.registerListener(this, sensor9, samplingRate);
        }
        if(gravitySensorEnabled)
        {
            Sensor sensor10 = sensorManager.getDefaultSensor(9);
            if(sensor10 != null)
                sensorManager.registerListener(this, sensor10, samplingRate);
        }
        if(ambientTemperatureSensorEnabled)
        {
            Sensor sensor11 = sensorManager.getDefaultSensor(13);
            if(sensor11 != null)
                sensorManager.registerListener(this, sensor11, samplingRate);
        }
        if(relativeHumiditySensorEnabled)
        {
            Sensor sensor12 = sensorManager.getDefaultSensor(12);
            if(sensor12 != null)
                sensorManager.registerListener(this, sensor12, samplingRate);
        }
        if(stepDetectorSensorEnabled)
        {
            Sensor sensor13 = sensorManager.getDefaultSensor(18);
            if(sensor13 != null)
                sensorManager.registerListener(this, sensor13, samplingRate);
        }
        if(stepCounterSensorEnabled)
        {
            Sensor sensor14 = sensorManager.getDefaultSensor(19);
            if(sensor14 != null)
                sensorManager.registerListener(this, sensor14, samplingRate);
        }
        if(significantMotionSensorEnabled)
        {
            Sensor sensor15 = sensorManager.getDefaultSensor(17);
            if(sensor15 != null)
                sensorManager.registerListener(this, sensor15, samplingRate);
        }
        if(geomagneticRotationVectorSensorEnabled)
        {
            Sensor sensor16 = sensorManager.getDefaultSensor(20);
            if(sensor16 != null)
                sensorManager.registerListener(this, sensor16, samplingRate);
        }
        if(gameRotationSensorEnabled)
        {
            Sensor sensor17 = sensorManager.getDefaultSensor(15);
            if(sensor17 != null)
                sensorManager.registerListener(this, sensor17, samplingRate);
        }
        isRegistered = true;
    }

    public void startService()
    {
        start();
    }

    public void stop()
    {
        PApplet.println("KetaiSensor: Stop()....");
        sensorManager.unregisterListener(this);
        isRegistered = false;
    }

    public void stopService()
    {
        stop();
    }

    public void useSimulator(boolean flag)
    {
        useSimulator = flag;
    }

    public boolean usingSimulator()
    {
        return useSimulator;
    }

    static final String SERVICE_DESCRIPTION = "Android Sensors.";
    float accelerometerData[];
    private boolean accelerometerSensorEnabled;
    private boolean ambientTemperatureSensorEnabled;
    public Object callbackdelegate;
    private long delayInterval;
    private SensorQueue eventQueue;
    private boolean gameRotationSensorEnabled;
    private boolean geomagneticRotationVectorSensorEnabled;
    private boolean gravitySensorEnabled;
    private boolean gyroscopeSensorEnabled;
    private boolean heartRateSensorEnabled;
    private float inclinationMat[];
    private boolean isRegistered;
    private boolean lightSensorEnabled;
    private boolean linearAccelerationSensorEnabled;
    private boolean magneticFieldSensorEnabled;
    float magnetometerData[];
    private Method onAccelerometerEventMethod;
    private Method onAccelerometerEventMethodSimple;
    private Method onAmbientTemperatureEventMethod;
    private Method onGameRotationEventMethod;
    private Method onGeomagneticRotationVectorEventMethod;
    private Method onGravitySensorEventMethod;
    private Method onGravitySensorEventMethodSimple;
    private Method onGyroscopeSensorEventMethod;
    private Method onGyroscopeSensorEventMethodSimple;
    private Method onHeartRateEventMethod;
    private Method onLightSensorEventMethod;
    private Method onLightSensorEventMethodSimple;
    private Method onLinearAccelerationSensorEventMethod;
    private Method onLinearAccelerationSensorEventMethodSimple;
    private Method onMagneticFieldSensorEventMethod;
    private Method onMagneticFieldSensorEventMethodSimple;
    private Method onOrientationSensorEventMethod;
    private Method onOrientationSensorEventMethodSimple;
    private Method onPressureSensorEventMethod;
    private Method onPressureSensorEventMethodSimple;
    private Method onProximitySensorEventMethod;
    private Method onProximitySensorEventMethodSimple;
    private Method onRelativeHumidityEventMethod;
    private Method onRotationVectorSensorEventMethod;
    private Method onRotationVectorSensorEventMethodSimple;
    private Method onSensorEventMethod;
    private Method onSignificantMotionEventMethod;
    private Method onStepCounterEventMethod;
    private Method onStepDetectorEventMethod;
    private Method onTemperatureSensorEventMethod;
    private Method onTemperatureSensorEventMethodSimple;
    private boolean orientationSensorEnabled;
    private float orientationVec[];
    private PApplet parent;
    private boolean pressureSensorEnabled;
    private boolean proximitySensorEnabled;
    private boolean relativeHumiditySensorEnabled;
    private float rotationMat[];
    private boolean rotationVectorSensorEnabled;
    private int samplingRate;
    private SensorManager sensorManager;
    private boolean significantMotionSensorEnabled;
    private boolean stepCounterSensorEnabled;
    private boolean stepDetectorSensorEnabled;
    private boolean temperatureSensorEnabled;
    private long timeOfLastUpdate;
    private boolean useSimulator;
    private float zeroes[] = {
        0.0F, 0.0F, 0.0F
    };
}
