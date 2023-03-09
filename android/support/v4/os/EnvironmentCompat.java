// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.os;

import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.IOException;

// Referenced classes of package android.support.v4.os:
//            EnvironmentCompatKitKat

public final class EnvironmentCompat
{

    private EnvironmentCompat()
    {
    }

    public static String getStorageState(File file)
    {
        if(android.os.Build.VERSION.SDK_INT < 19) goto _L2; else goto _L1
_L1:
        file = EnvironmentCompatKitKat.getStorageState(file);
_L4:
        return file;
_L2:
        try
        {
            if(file.getCanonicalPath().startsWith(Environment.getExternalStorageDirectory().getCanonicalPath()))
            {
                file = Environment.getExternalStorageState();
                continue; /* Loop/switch isn't completed */
            }
        }
        // Misplaced declaration of an exception variable
        catch(File file)
        {
            Log.w("EnvironmentCompat", (new StringBuilder()).append("Failed to resolve canonical path: ").append(file).toString());
        }
        file = "unknown";
        if(true) goto _L4; else goto _L3
_L3:
    }

    public static final String MEDIA_UNKNOWN = "unknown";
    private static final String TAG = "EnvironmentCompat";
}
