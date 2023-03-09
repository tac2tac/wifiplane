// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.content;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Process;
import android.support.v4.app.AppOpsManagerCompat;
import java.lang.annotation.Annotation;

public final class PermissionChecker
{
    public static interface PermissionResult
        extends Annotation
    {
    }


    private PermissionChecker()
    {
    }

    public static int checkCallingOrSelfPermission(Context context, String s)
    {
        String s1;
        if(Binder.getCallingPid() == Process.myPid())
            s1 = context.getPackageName();
        else
            s1 = null;
        return checkPermission(context, s, Binder.getCallingPid(), Binder.getCallingUid(), s1);
    }

    public static int checkCallingPermission(Context context, String s, String s1)
    {
        int i;
        if(Binder.getCallingPid() == Process.myPid())
            i = -1;
        else
            i = checkPermission(context, s, Binder.getCallingPid(), Binder.getCallingUid(), s1);
        return i;
    }

    public static int checkPermission(Context context, String s, int i, int j, String s1)
    {
        byte byte0 = -1;
        if(context.checkPermission(s, i, j) != -1) goto _L2; else goto _L1
_L1:
        i = byte0;
_L4:
        return i;
_L2:
        String s2 = AppOpsManagerCompat.permissionToOp(s);
        if(s2 == null)
        {
            i = 0;
            continue; /* Loop/switch isn't completed */
        }
        s = s1;
        if(s1 == null)
        {
            s = context.getPackageManager().getPackagesForUid(j);
            i = byte0;
            if(s == null)
                continue; /* Loop/switch isn't completed */
            i = byte0;
            if(s.length <= 0)
                continue; /* Loop/switch isn't completed */
            s = s[0];
        }
        if(AppOpsManagerCompat.noteProxyOp(context, s2, s) != 0)
            i = -2;
        else
            i = 0;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public static int checkSelfPermission(Context context, String s)
    {
        return checkPermission(context, s, Process.myPid(), Process.myUid(), context.getPackageName());
    }

    public static final int PERMISSION_DENIED = -1;
    public static final int PERMISSION_DENIED_APP_OP = -2;
    public static final int PERMISSION_GRANTED = 0;
}
