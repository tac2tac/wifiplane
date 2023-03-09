// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.media.session;

import android.content.*;
import android.content.pm.*;
import android.view.KeyEvent;
import java.util.List;

// Referenced classes of package android.support.v4.media.session:
//            MediaSessionCompat, MediaControllerCompat

public class MediaButtonReceiver extends BroadcastReceiver
{

    public MediaButtonReceiver()
    {
    }

    public static KeyEvent handleIntent(MediaSessionCompat mediasessioncompat, Intent intent)
    {
        if(mediasessioncompat == null || intent == null || !"android.intent.action.MEDIA_BUTTON".equals(intent.getAction()) || !intent.hasExtra("android.intent.extra.KEY_EVENT"))
        {
            mediasessioncompat = null;
        } else
        {
            intent = (KeyEvent)intent.getParcelableExtra("android.intent.extra.KEY_EVENT");
            mediasessioncompat.getController().dispatchMediaButtonEvent(intent);
            mediasessioncompat = intent;
        }
        return mediasessioncompat;
    }

    public void onReceive(Context context, Intent intent)
    {
        Intent intent1 = new Intent("android.intent.action.MEDIA_BUTTON");
        intent1.setPackage(context.getPackageName());
        PackageManager packagemanager = context.getPackageManager();
        List list = packagemanager.queryIntentServices(intent1, 0);
        Object obj = list;
        if(list.isEmpty())
        {
            intent1.setAction("android.media.browse.MediaBrowserService");
            obj = packagemanager.queryIntentServices(intent1, 0);
        }
        if(((List) (obj)).isEmpty())
            throw new IllegalStateException("Could not find any Service that handles android.intent.action.MEDIA_BUTTON or a media browser service implementation");
        if(((List) (obj)).size() != 1)
        {
            throw new IllegalStateException((new StringBuilder()).append("Expected 1 Service that handles ").append(intent1.getAction()).append(", found ").append(((List) (obj)).size()).toString());
        } else
        {
            obj = (ResolveInfo)((List) (obj)).get(0);
            intent.setComponent(new ComponentName(((ResolveInfo) (obj)).serviceInfo.packageName, ((ResolveInfo) (obj)).serviceInfo.name));
            context.startService(intent);
            return;
        }
    }
}
