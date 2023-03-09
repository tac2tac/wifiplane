// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package ketai.sensors;

import android.content.Context;
import android.location.*;
import android.os.*;
import java.lang.reflect.Method;
import java.util.*;
import processing.core.PApplet;
import processing.core.PSurface;

// Referenced classes of package ketai.sensors:
//            SensorQueue, Location

public class KetaiLocation
    implements LocationListener
{

    public KetaiLocation(PApplet papplet)
    {
        locationManager = null;
        provider = "none";
        minTime = 10000L;
        minDistance = 1.0F;
        parent = papplet;
        me = this;
        locationManager = (LocationManager)parent.getSurface().getContext().getSystemService("location");
        locationQueue = new SensorQueue();
        PApplet.println((new StringBuilder("KetaiLocationManager instantiated:")).append(locationManager.toString()).toString());
        findObjectIntentions(parent);
        parent.requestPermission("android.permission.ACCESS_FINE_LOCATION", "onPermissionResult", this);
        parent.registerMethod("dispose", this);
        parent.registerMethod("post", this);
    }

    private void dequeueLocations()
    {
        do
        {
            if(!locationQueue.available())
                return;
            handleLocationEvent((ketai.sensors.Location)locationQueue.remove());
        } while(true);
    }

    private boolean determineProvider()
    {
        boolean flag = true;
        if(locationManager.isProviderEnabled("gps"))
            provider = "gps";
        else
            provider = locationManager.getBestProvider(new Criteria(), true);
        if(provider == null)
        {
            flag = false;
        } else
        {
            PApplet.println((new StringBuilder("Requesting location updates from: ")).append(provider).toString());
            Runnable runnable = new Runnable() {

                public void run()
                {
                    locationManager.requestLocationUpdates(provider, minTime, minDistance, me);
                }

                final KetaiLocation this$0;

            
            {
                this$0 = KetaiLocation.this;
                super();
            }
            }
;
            (new Handler(Looper.getMainLooper())).post(runnable);
        }
        return flag;
    }

    private void findObjectIntentions(Object obj)
    {
        callbackdelegate = obj;
        try
        {
            onLocationEventMethod1arg = callbackdelegate.getClass().getMethod("onLocationEvent", new Class[] {
                ketai/sensors/Location
            });
            PApplet.println("Found Advanced onLocationEventMethod(Location)...");
        }
        // Misplaced declaration of an exception variable
        catch(Object obj) { }
        try
        {
            onLocationEventMethod2arg = callbackdelegate.getClass().getMethod("onLocationEvent", new Class[] {
                Double.TYPE, Double.TYPE
            });
            PApplet.println("Found Advanced onLocationEventMethod(long, lat)...");
        }
        // Misplaced declaration of an exception variable
        catch(Object obj) { }
        try
        {
            onLocationEventMethod3arg = callbackdelegate.getClass().getMethod("onLocationEvent", new Class[] {
                Double.TYPE, Double.TYPE, Double.TYPE
            });
            PApplet.println("Found basic onLocationEventMethod(long,lat,alt)...");
        }
        // Misplaced declaration of an exception variable
        catch(Object obj) { }
        onLocationEventMethod4arg = callbackdelegate.getClass().getMethod("onLocationEvent", new Class[] {
            Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE
        });
        PApplet.println("Found basic onLocationEventMethod(long,lat,alt, acc)...");
_L2:
        return;
        obj;
        if(true) goto _L2; else goto _L1
_L1:
    }

    private void handleLocationEvent(ketai.sensors.Location location1)
    {
        location = location1;
        if(onLocationEventMethod2arg == null) goto _L2; else goto _L1
_L1:
        onLocationEventMethod2arg.invoke(callbackdelegate, new Object[] {
            Double.valueOf(location.getLatitude()), Double.valueOf(location.getLongitude())
        });
_L3:
        return;
        location1;
        PApplet.println((new StringBuilder("Disabling onLocationEvent() because of an error:")).append(location1.getMessage()).toString());
        location1.printStackTrace();
        onLocationEventMethod2arg = null;
_L2:
        if(onLocationEventMethod3arg == null)
            break MISSING_BLOCK_LABEL_184;
        onLocationEventMethod3arg.invoke(callbackdelegate, new Object[] {
            Double.valueOf(location.getLatitude()), Double.valueOf(location.getLongitude()), Double.valueOf(location.getAltitude())
        });
          goto _L3
        location1;
        PApplet.println((new StringBuilder("Disabling onLocationEvent() because of an error:")).append(location1.getMessage()).toString());
        location1.printStackTrace();
        onLocationEventMethod3arg = null;
        if(onLocationEventMethod4arg != null)
            try
            {
                onLocationEventMethod4arg.invoke(callbackdelegate, new Object[] {
                    Double.valueOf(location.getLatitude()), Double.valueOf(location.getLongitude()), Double.valueOf(location.getAltitude()), Float.valueOf(location.getAccuracy())
                });
            }
            // Misplaced declaration of an exception variable
            catch(ketai.sensors.Location location1)
            {
                PApplet.println((new StringBuilder("Disabling onLocationEvent() because of an error:")).append(location1.getMessage()).toString());
                location1.printStackTrace();
                onLocationEventMethod4arg = null;
            }
          goto _L3
    }

    public void dispose()
    {
        stop();
    }

    public ketai.sensors.Location getLocation()
    {
        return location;
    }

    public String getProvider()
    {
        return provider;
    }

    public boolean isStarted()
    {
        boolean flag;
        if(onLocationEventMethod4arg != null)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public Collection list()
    {
        Vector vector = new Vector();
        vector.add("Location");
        return vector;
    }

    public void onLocationChanged(Location location1)
    {
        onLocationChanged(new ketai.sensors.Location(location1));
    }

    public void onLocationChanged(ketai.sensors.Location location1)
    {
        PApplet.println((new StringBuilder("LocationChanged:")).append(location1.toString()).toString());
        locationQueue.add(new ketai.sensors.Location(location1));
        if(onLocationEventMethod1arg == null)
            break MISSING_BLOCK_LABEL_65;
        onLocationEventMethod1arg.invoke(callbackdelegate, new Object[] {
            location1
        });
_L1:
        return;
        location1;
        PApplet.println((new StringBuilder("Disabling onLocationEvent() because of an error:")).append(location1.getMessage()).toString());
        location1.printStackTrace();
        onLocationEventMethod1arg = null;
          goto _L1
    }

    public void onPermissionResult(boolean flag)
    {
        if(flag)
        {
            start();
        } else
        {
            PApplet.println("User did not grant location permission.  Location is disabled.");
            provider = "none";
        }
    }

    public void onProviderDisabled(String s)
    {
        PApplet.println((new StringBuilder("LocationManager onProviderDisabled: ")).append(s).toString());
        determineProvider();
    }

    public void onProviderEnabled(String s)
    {
        PApplet.println((new StringBuilder("LocationManager onProviderEnabled: ")).append(s).toString());
        determineProvider();
    }

    public void onStatusChanged(String s, int i, Bundle bundle)
    {
        PApplet.println((new StringBuilder("LocationManager onStatusChanged: ")).append(s).append(":").append(i).append(":").append(bundle.toString()).toString());
        determineProvider();
    }

    public void post()
    {
        dequeueLocations();
    }

    public void register(Object obj)
    {
        boolean flag = isStarted();
        if(flag)
            stop();
        findObjectIntentions(obj);
        if(flag)
            start();
    }

    public void setUpdateRate(int i, int j)
    {
        minTime = i;
        minDistance = j;
        determineProvider();
    }

    public void start()
    {
        Iterator iterator;
        PApplet.println("KetaiLocationManager: start()...");
        List list1 = locationManager.getAllProviders();
        PApplet.println("KetaiLocationManager All Provider(s) list: ");
        iterator = list1.iterator();
_L5:
        if(iterator.hasNext()) goto _L2; else goto _L1
_L1:
        if(!determineProvider())
        {
            PApplet.println("Error obtaining location provider.  Check your location settings.");
            provider = "none";
        }
        if(location != null) goto _L4; else goto _L3
_L3:
        List list2 = locationManager.getProviders(true);
        PApplet.println("KetaiLocationManager Enabled Provider(s) list: ");
        iterator = list2.iterator();
_L6:
        if(iterator.hasNext())
            break MISSING_BLOCK_LABEL_156;
        if(location == null)
            location = new ketai.sensors.Location("default");
_L4:
        onLocationChanged(location);
        return;
_L2:
        String s = (String)iterator.next();
        PApplet.println((new StringBuilder("\t")).append(s).toString());
          goto _L5
        String s1 = (String)iterator.next();
        if(location == null)
        {
            Location location1 = locationManager.getLastKnownLocation(s1);
            if(location1 != null)
            {
                location = new ketai.sensors.Location(location1);
                PApplet.println((new StringBuilder("\t")).append(s1).append(" - lastLocation for provider:").append(location.toString()).toString());
            }
        }
          goto _L6
    }

    public void stop()
    {
        PApplet.println("KetaiLocationManager: Stop()....");
        locationManager.removeUpdates(this);
    }

    static final String SERVICE_DESCRIPTION = "Android Location.";
    private Object callbackdelegate;
    private ketai.sensors.Location location;
    private LocationManager locationManager;
    private SensorQueue locationQueue;
    KetaiLocation me;
    private float minDistance;
    private long minTime;
    private Method onLocationEventMethod1arg;
    private Method onLocationEventMethod2arg;
    private Method onLocationEventMethod3arg;
    private Method onLocationEventMethod4arg;
    private PApplet parent;
    private String provider;




}
