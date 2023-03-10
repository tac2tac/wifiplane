// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.app;

import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class BundleCompatDonut
{

    BundleCompatDonut()
    {
    }

    public static IBinder getBinder(Bundle bundle, String s)
    {
        if(!sGetIBinderMethodFetched)
        {
            try
            {
                sGetIBinderMethod = android/os/Bundle.getMethod("getIBinder", new Class[] {
                    java/lang/String
                });
                sGetIBinderMethod.setAccessible(true);
            }
            catch(NoSuchMethodException nosuchmethodexception)
            {
                Log.i("BundleCompatDonut", "Failed to retrieve getIBinder method", nosuchmethodexception);
            }
            sGetIBinderMethodFetched = true;
        }
        if(sGetIBinderMethod == null) goto _L2; else goto _L1
_L1:
        bundle = (IBinder)sGetIBinderMethod.invoke(bundle, new Object[] {
            s
        });
_L3:
        return bundle;
        bundle;
_L4:
        Log.i("BundleCompatDonut", "Failed to invoke getIBinder via reflection", bundle);
        sGetIBinderMethod = null;
_L2:
        bundle = null;
          goto _L3
        bundle;
          goto _L4
        bundle;
          goto _L4
    }

    public static void putBinder(Bundle bundle, String s, IBinder ibinder)
    {
        if(!sPutIBinderMethodFetched)
        {
            try
            {
                sPutIBinderMethod = android/os/Bundle.getMethod("putIBinder", new Class[] {
                    java/lang/String, android/os/IBinder
                });
                sPutIBinderMethod.setAccessible(true);
            }
            catch(NoSuchMethodException nosuchmethodexception)
            {
                Log.i("BundleCompatDonut", "Failed to retrieve putIBinder method", nosuchmethodexception);
            }
            sPutIBinderMethodFetched = true;
        }
        if(sPutIBinderMethod == null)
            break MISSING_BLOCK_LABEL_67;
        sPutIBinderMethod.invoke(bundle, new Object[] {
            s, ibinder
        });
_L1:
        return;
        bundle;
_L2:
        Log.i("BundleCompatDonut", "Failed to invoke putIBinder via reflection", bundle);
        sPutIBinderMethod = null;
          goto _L1
        bundle;
          goto _L2
        bundle;
          goto _L2
    }

    private static final String TAG = "BundleCompatDonut";
    private static Method sGetIBinderMethod;
    private static boolean sGetIBinderMethodFetched;
    private static Method sPutIBinderMethod;
    private static boolean sPutIBinderMethodFetched;
}
