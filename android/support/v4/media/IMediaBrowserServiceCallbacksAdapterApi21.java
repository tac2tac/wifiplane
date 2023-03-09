// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.media;

import android.os.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class IMediaBrowserServiceCallbacksAdapterApi21
{
    static class Stub
    {

        static Object asInterface(IBinder ibinder)
        {
            Object obj = null;
            ibinder = ((IBinder) (sAsInterfaceMethod.invoke(null, new Object[] {
                ibinder
            })));
_L2:
            return ibinder;
            ibinder;
_L3:
            ibinder.printStackTrace();
            ibinder = obj;
            if(true) goto _L2; else goto _L1
_L1:
            ibinder;
              goto _L3
        }

        static Method sAsInterfaceMethod = Class.forName("android.service.media.IMediaBrowserServiceCallbacks$Stub").getMethod("asInterface", new Class[] {
            android/os/IBinder
        });

        static 
        {
_L1:
            return;
            Object obj;
            obj;
_L2:
            ((ReflectiveOperationException) (obj)).printStackTrace();
              goto _L1
            obj;
              goto _L2
        }

        Stub()
        {
        }
    }


    IMediaBrowserServiceCallbacksAdapterApi21(Object obj)
    {
        mCallbackObject = obj;
        obj = Class.forName("android.service.media.IMediaBrowserServiceCallbacks");
        Class class1 = Class.forName("android.content.pm.ParceledListSlice");
        mAsBinderMethod = ((Class) (obj)).getMethod("asBinder", new Class[0]);
        mOnConnectMethod = ((Class) (obj)).getMethod("onConnect", new Class[] {
            java/lang/String, android/media/session/MediaSession$Token, android/os/Bundle
        });
        mOnConnectFailedMethod = ((Class) (obj)).getMethod("onConnectFailed", new Class[0]);
        mOnLoadChildrenMethod = ((Class) (obj)).getMethod("onLoadChildren", new Class[] {
            java/lang/String, class1
        });
_L1:
        return;
        obj;
_L2:
        ((ReflectiveOperationException) (obj)).printStackTrace();
          goto _L1
        obj;
          goto _L2
    }

    IBinder asBinder()
    {
        Object obj = null;
        Object obj1 = (IBinder)mAsBinderMethod.invoke(mCallbackObject, new Object[0]);
_L2:
        return ((IBinder) (obj1));
        IllegalAccessException illegalaccessexception;
        illegalaccessexception;
_L3:
        illegalaccessexception.printStackTrace();
        illegalaccessexception = obj;
        if(true) goto _L2; else goto _L1
_L1:
        illegalaccessexception;
          goto _L3
    }

    void onConnect(String s, Object obj, Bundle bundle)
        throws RemoteException
    {
        mOnConnectMethod.invoke(mCallbackObject, new Object[] {
            s, obj, bundle
        });
_L1:
        return;
        s;
_L2:
        s.printStackTrace();
          goto _L1
        s;
          goto _L2
    }

    void onConnectFailed()
        throws RemoteException
    {
        mOnConnectFailedMethod.invoke(mCallbackObject, new Object[0]);
_L1:
        return;
        Object obj;
        obj;
_L2:
        ((ReflectiveOperationException) (obj)).printStackTrace();
          goto _L1
        obj;
          goto _L2
    }

    void onLoadChildren(String s, Object obj)
        throws RemoteException
    {
        mOnLoadChildrenMethod.invoke(mCallbackObject, new Object[] {
            s, obj
        });
_L1:
        return;
        s;
_L2:
        s.printStackTrace();
          goto _L1
        s;
          goto _L2
    }

    private Method mAsBinderMethod;
    Object mCallbackObject;
    private Method mOnConnectFailedMethod;
    private Method mOnConnectMethod;
    private Method mOnLoadChildrenMethod;
}
