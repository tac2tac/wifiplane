// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.content;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.*;
import android.util.Log;
import java.io.File;

// Referenced classes of package android.support.v4.content:
//            ContextCompatApi21, ContextCompatApi23, ContextCompatKitKat, ContextCompatFroyo, 
//            ContextCompatHoneycomb, ContextCompatJellybean

public class ContextCompat
{

    public ContextCompat()
    {
    }

    private static transient File buildPath(File file, String as[])
    {
        int i = as.length;
        int j = 0;
        while(j < i) 
        {
            String s = as[j];
            if(file == null)
                file = new File(s);
            else
            if(s != null)
                file = new File(file, s);
            j++;
        }
        return file;
    }

    public static int checkSelfPermission(Context context, String s)
    {
        if(s == null)
            throw new IllegalArgumentException("permission is null");
        else
            return context.checkPermission(s, Process.myPid(), Process.myUid());
    }

    private static File createFilesDir(File file)
    {
        android/support/v4/content/ContextCompat;
        JVM INSTR monitorenter ;
        Object obj = file;
        if(file.exists()) goto _L2; else goto _L1
_L1:
        obj = file;
        if(file.mkdirs()) goto _L2; else goto _L3
_L3:
        boolean flag = file.exists();
        if(!flag) goto _L5; else goto _L4
_L4:
        obj = file;
_L2:
        android/support/v4/content/ContextCompat;
        JVM INSTR monitorexit ;
        return ((File) (obj));
_L5:
        obj = JVM INSTR new #73  <Class StringBuilder>;
        ((StringBuilder) (obj)).StringBuilder();
        Log.w("ContextCompat", ((StringBuilder) (obj)).append("Unable to create files subdir ").append(file.getPath()).toString());
        obj = null;
        if(true) goto _L2; else goto _L6
_L6:
        file;
        throw file;
    }

    public static File getCodeCacheDir(Context context)
    {
        if(android.os.Build.VERSION.SDK_INT >= 21)
            context = ContextCompatApi21.getCodeCacheDir(context);
        else
            context = createFilesDir(new File(context.getApplicationInfo().dataDir, "code_cache"));
        return context;
    }

    public static final int getColor(Context context, int i)
    {
        if(android.os.Build.VERSION.SDK_INT >= 23)
            i = ContextCompatApi23.getColor(context, i);
        else
            i = context.getResources().getColor(i);
        return i;
    }

    public static final ColorStateList getColorStateList(Context context, int i)
    {
        if(android.os.Build.VERSION.SDK_INT >= 23)
            context = ContextCompatApi23.getColorStateList(context, i);
        else
            context = context.getResources().getColorStateList(i);
        return context;
    }

    public static final Drawable getDrawable(Context context, int i)
    {
        if(android.os.Build.VERSION.SDK_INT >= 21)
            context = ContextCompatApi21.getDrawable(context, i);
        else
            context = context.getResources().getDrawable(i);
        return context;
    }

    public static File[] getExternalCacheDirs(Context context)
    {
        int i = android.os.Build.VERSION.SDK_INT;
        if(i >= 19)
        {
            context = ContextCompatKitKat.getExternalCacheDirs(context);
        } else
        {
            File afile[];
            if(i >= 8)
                context = ContextCompatFroyo.getExternalCacheDir(context);
            else
                context = buildPath(Environment.getExternalStorageDirectory(), new String[] {
                    "Android", "data", context.getPackageName(), "cache"
                });
            afile = new File[1];
            afile[0] = context;
            context = afile;
        }
        return context;
    }

    public static File[] getExternalFilesDirs(Context context, String s)
    {
        int i = android.os.Build.VERSION.SDK_INT;
        if(i >= 19)
        {
            context = ContextCompatKitKat.getExternalFilesDirs(context, s);
        } else
        {
            if(i >= 8)
                context = ContextCompatFroyo.getExternalFilesDir(context, s);
            else
                context = buildPath(Environment.getExternalStorageDirectory(), new String[] {
                    "Android", "data", context.getPackageName(), "files", s
                });
            s = new File[1];
            s[0] = context;
            context = s;
        }
        return context;
    }

    public static File[] getObbDirs(Context context)
    {
        int i = android.os.Build.VERSION.SDK_INT;
        if(i >= 19)
        {
            context = ContextCompatKitKat.getObbDirs(context);
        } else
        {
            File afile[];
            if(i >= 11)
                context = ContextCompatHoneycomb.getObbDir(context);
            else
                context = buildPath(Environment.getExternalStorageDirectory(), new String[] {
                    "Android", "obb", context.getPackageName()
                });
            afile = new File[1];
            afile[0] = context;
            context = afile;
        }
        return context;
    }

    public static boolean startActivities(Context context, Intent aintent[])
    {
        return startActivities(context, aintent, null);
    }

    public static boolean startActivities(Context context, Intent aintent[], Bundle bundle)
    {
        boolean flag = true;
        int i = android.os.Build.VERSION.SDK_INT;
        if(i >= 16)
            ContextCompatJellybean.startActivities(context, aintent, bundle);
        else
        if(i >= 11)
            ContextCompatHoneycomb.startActivities(context, aintent);
        else
            flag = false;
        return flag;
    }

    public final File getNoBackupFilesDir(Context context)
    {
        if(android.os.Build.VERSION.SDK_INT >= 21)
            context = ContextCompatApi21.getNoBackupFilesDir(context);
        else
            context = createFilesDir(new File(context.getApplicationInfo().dataDir, "no_backup"));
        return context;
    }

    private static final String DIR_ANDROID = "Android";
    private static final String DIR_CACHE = "cache";
    private static final String DIR_DATA = "data";
    private static final String DIR_FILES = "files";
    private static final String DIR_OBB = "obb";
    private static final String TAG = "ContextCompat";
}
