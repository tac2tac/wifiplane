// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.media.session;

import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.RemoteControlClient;
import android.os.Bundle;

class MediaSessionCompatApi14
{

    MediaSessionCompatApi14()
    {
    }

    static void buildOldMetadata(Bundle bundle, android.media.RemoteControlClient.MetadataEditor metadataeditor)
    {
        if(bundle != null) goto _L2; else goto _L1
_L1:
        return;
_L2:
        if(!bundle.containsKey("android.media.metadata.ART"))
            break; /* Loop/switch isn't completed */
        metadataeditor.putBitmap(100, (Bitmap)bundle.getParcelable("android.media.metadata.ART"));
_L5:
        if(bundle.containsKey("android.media.metadata.ALBUM"))
            metadataeditor.putString(1, bundle.getString("android.media.metadata.ALBUM"));
        if(bundle.containsKey("android.media.metadata.ALBUM_ARTIST"))
            metadataeditor.putString(13, bundle.getString("android.media.metadata.ALBUM_ARTIST"));
        if(bundle.containsKey("android.media.metadata.ARTIST"))
            metadataeditor.putString(2, bundle.getString("android.media.metadata.ARTIST"));
        if(bundle.containsKey("android.media.metadata.AUTHOR"))
            metadataeditor.putString(3, bundle.getString("android.media.metadata.AUTHOR"));
        if(bundle.containsKey("android.media.metadata.COMPILATION"))
            metadataeditor.putString(15, bundle.getString("android.media.metadata.COMPILATION"));
        if(bundle.containsKey("android.media.metadata.COMPOSER"))
            metadataeditor.putString(4, bundle.getString("android.media.metadata.COMPOSER"));
        if(bundle.containsKey("android.media.metadata.DATE"))
            metadataeditor.putString(5, bundle.getString("android.media.metadata.DATE"));
        if(bundle.containsKey("android.media.metadata.DISC_NUMBER"))
            metadataeditor.putLong(14, bundle.getLong("android.media.metadata.DISC_NUMBER"));
        if(bundle.containsKey("android.media.metadata.DURATION"))
            metadataeditor.putLong(9, bundle.getLong("android.media.metadata.DURATION"));
        if(bundle.containsKey("android.media.metadata.GENRE"))
            metadataeditor.putString(6, bundle.getString("android.media.metadata.GENRE"));
        if(bundle.containsKey("android.media.metadata.TITLE"))
            metadataeditor.putString(7, bundle.getString("android.media.metadata.TITLE"));
        if(bundle.containsKey("android.media.metadata.TRACK_NUMBER"))
            metadataeditor.putLong(0, bundle.getLong("android.media.metadata.TRACK_NUMBER"));
        if(bundle.containsKey("android.media.metadata.WRITER"))
            metadataeditor.putString(11, bundle.getString("android.media.metadata.WRITER"));
        if(true) goto _L1; else goto _L3
_L3:
        if(!bundle.containsKey("android.media.metadata.ALBUM_ART")) goto _L5; else goto _L4
_L4:
        metadataeditor.putBitmap(100, (Bitmap)bundle.getParcelable("android.media.metadata.ALBUM_ART"));
          goto _L5
    }

    public static Object createRemoteControlClient(PendingIntent pendingintent)
    {
        return new RemoteControlClient(pendingintent);
    }

    static int getRccStateFromState(int i)
    {
        i;
        JVM INSTR tableswitch 0 11: default 64
    //                   0 85
    //                   1 117
    //                   2 90
    //                   3 95
    //                   4 80
    //                   5 100
    //                   6 68
    //                   7 74
    //                   8 68
    //                   9 105
    //                   10 111
    //                   11 111;
           goto _L1 _L2 _L3 _L4 _L5 _L6 _L7 _L8 _L9 _L8 _L10 _L11 _L11
_L1:
        i = -1;
_L13:
        return i;
_L8:
        i = 8;
        continue; /* Loop/switch isn't completed */
_L9:
        i = 9;
        continue; /* Loop/switch isn't completed */
_L6:
        i = 4;
        continue; /* Loop/switch isn't completed */
_L2:
        i = 0;
        continue; /* Loop/switch isn't completed */
_L4:
        i = 2;
        continue; /* Loop/switch isn't completed */
_L5:
        i = 3;
        continue; /* Loop/switch isn't completed */
_L7:
        i = 5;
        continue; /* Loop/switch isn't completed */
_L10:
        i = 7;
        continue; /* Loop/switch isn't completed */
_L11:
        i = 6;
        continue; /* Loop/switch isn't completed */
_L3:
        i = 1;
        if(true) goto _L13; else goto _L12
_L12:
    }

    static int getRccTransportControlFlagsFromActions(long l)
    {
        int i = 0;
        if((1L & l) != 0L)
            i = 0 | 0x20;
        int j = i;
        if((2L & l) != 0L)
            j = i | 0x10;
        i = j;
        if((4L & l) != 0L)
            i = j | 4;
        j = i;
        if((8L & l) != 0L)
            j = i | 2;
        i = j;
        if((16L & l) != 0L)
            i = j | 1;
        j = i;
        if((32L & l) != 0L)
            j = i | 0x80;
        i = j;
        if((64L & l) != 0L)
            i = j | 0x40;
        j = i;
        if((512L & l) != 0L)
            j = i | 8;
        return j;
    }

    public static void registerRemoteControlClient(Context context, Object obj)
    {
        ((AudioManager)context.getSystemService("audio")).registerRemoteControlClient((RemoteControlClient)obj);
    }

    public static void setMetadata(Object obj, Bundle bundle)
    {
        obj = ((RemoteControlClient)obj).editMetadata(true);
        buildOldMetadata(bundle, ((android.media.RemoteControlClient.MetadataEditor) (obj)));
        ((android.media.RemoteControlClient.MetadataEditor) (obj)).apply();
    }

    public static void setState(Object obj, int i)
    {
        ((RemoteControlClient)obj).setPlaybackState(getRccStateFromState(i));
    }

    public static void setTransportControlFlags(Object obj, long l)
    {
        ((RemoteControlClient)obj).setTransportControlFlags(getRccTransportControlFlagsFromActions(l));
    }

    public static void unregisterRemoteControlClient(Context context, Object obj)
    {
        ((AudioManager)context.getSystemService("audio")).unregisterRemoteControlClient((RemoteControlClient)obj);
    }

    private static final long ACTION_FAST_FORWARD = 64L;
    private static final long ACTION_PAUSE = 2L;
    private static final long ACTION_PLAY = 4L;
    private static final long ACTION_PLAY_PAUSE = 512L;
    private static final long ACTION_REWIND = 8L;
    private static final long ACTION_SKIP_TO_NEXT = 32L;
    private static final long ACTION_SKIP_TO_PREVIOUS = 16L;
    private static final long ACTION_STOP = 1L;
    private static final String METADATA_KEY_ALBUM = "android.media.metadata.ALBUM";
    private static final String METADATA_KEY_ALBUM_ART = "android.media.metadata.ALBUM_ART";
    private static final String METADATA_KEY_ALBUM_ARTIST = "android.media.metadata.ALBUM_ARTIST";
    private static final String METADATA_KEY_ART = "android.media.metadata.ART";
    private static final String METADATA_KEY_ARTIST = "android.media.metadata.ARTIST";
    private static final String METADATA_KEY_AUTHOR = "android.media.metadata.AUTHOR";
    private static final String METADATA_KEY_COMPILATION = "android.media.metadata.COMPILATION";
    private static final String METADATA_KEY_COMPOSER = "android.media.metadata.COMPOSER";
    private static final String METADATA_KEY_DATE = "android.media.metadata.DATE";
    private static final String METADATA_KEY_DISC_NUMBER = "android.media.metadata.DISC_NUMBER";
    private static final String METADATA_KEY_DURATION = "android.media.metadata.DURATION";
    private static final String METADATA_KEY_GENRE = "android.media.metadata.GENRE";
    private static final String METADATA_KEY_TITLE = "android.media.metadata.TITLE";
    private static final String METADATA_KEY_TRACK_NUMBER = "android.media.metadata.TRACK_NUMBER";
    private static final String METADATA_KEY_WRITER = "android.media.metadata.WRITER";
    static final int RCC_PLAYSTATE_NONE = 0;
    static final int STATE_BUFFERING = 6;
    static final int STATE_CONNECTING = 8;
    static final int STATE_ERROR = 7;
    static final int STATE_FAST_FORWARDING = 4;
    static final int STATE_NONE = 0;
    static final int STATE_PAUSED = 2;
    static final int STATE_PLAYING = 3;
    static final int STATE_REWINDING = 5;
    static final int STATE_SKIPPING_TO_NEXT = 10;
    static final int STATE_SKIPPING_TO_PREVIOUS = 9;
    static final int STATE_SKIPPING_TO_QUEUE_ITEM = 11;
    static final int STATE_STOPPED = 1;
}
