// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.media.session;

import android.app.PendingIntent;
import android.content.*;
import android.content.pm.*;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.Uri;
import android.os.*;
import android.support.v4.media.*;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import java.lang.annotation.Annotation;
import java.util.*;

// Referenced classes of package android.support.v4.media.session:
//            MediaControllerCompat, PlaybackStateCompat, MediaSessionCompatApi23, MediaSessionCompatApi21, 
//            MediaSessionCompatApi22, MediaSessionCompatApi14, IMediaControllerCallback, MediaSessionCompatApi18, 
//            MediaSessionCompatApi8, MediaSessionCompatApi19, ParcelableVolumeInfo

public class MediaSessionCompat
{
    public static abstract class Callback
    {

        public void onCommand(String s, Bundle bundle, ResultReceiver resultreceiver)
        {
        }

        public void onCustomAction(String s, Bundle bundle)
        {
        }

        public void onFastForward()
        {
        }

        public boolean onMediaButtonEvent(Intent intent)
        {
            return false;
        }

        public void onPause()
        {
        }

        public void onPlay()
        {
        }

        public void onPlayFromMediaId(String s, Bundle bundle)
        {
        }

        public void onPlayFromSearch(String s, Bundle bundle)
        {
        }

        public void onPlayFromUri(Uri uri, Bundle bundle)
        {
        }

        public void onRewind()
        {
        }

        public void onSeekTo(long l)
        {
        }

        public void onSetRating(RatingCompat ratingcompat)
        {
        }

        public void onSkipToNext()
        {
        }

        public void onSkipToPrevious()
        {
        }

        public void onSkipToQueueItem(long l)
        {
        }

        public void onStop()
        {
        }

        final Object mCallbackObj;

        public Callback()
        {
            if(android.os.Build.VERSION.SDK_INT >= 23)
                mCallbackObj = MediaSessionCompatApi23.createCallback(new StubApi23());
            else
            if(android.os.Build.VERSION.SDK_INT >= 21)
                mCallbackObj = MediaSessionCompatApi21.createCallback(new StubApi21());
            else
                mCallbackObj = null;
        }
    }

    private class Callback.StubApi21
        implements MediaSessionCompatApi21.Callback
    {

        public void onCommand(String s, Bundle bundle, ResultReceiver resultreceiver)
        {
            Callback.this.onCommand(s, bundle, resultreceiver);
        }

        public void onCustomAction(String s, Bundle bundle)
        {
            if(s.equals("android.support.v4.media.session.action.PLAY_FROM_URI"))
            {
                s = (Uri)bundle.getParcelable("android.support.v4.media.session.action.ARGUMENT_URI");
                bundle = (Bundle)bundle.getParcelable("android.support.v4.media.session.action.ARGUMENT_EXTRAS");
                onPlayFromUri(s, bundle);
            } else
            {
                Callback.this.onCustomAction(s, bundle);
            }
        }

        public void onFastForward()
        {
            Callback.this.onFastForward();
        }

        public boolean onMediaButtonEvent(Intent intent)
        {
            return Callback.this.onMediaButtonEvent(intent);
        }

        public void onPause()
        {
            Callback.this.onPause();
        }

        public void onPlay()
        {
            Callback.this.onPlay();
        }

        public void onPlayFromMediaId(String s, Bundle bundle)
        {
            Callback.this.onPlayFromMediaId(s, bundle);
        }

        public void onPlayFromSearch(String s, Bundle bundle)
        {
            Callback.this.onPlayFromSearch(s, bundle);
        }

        public void onRewind()
        {
            Callback.this.onRewind();
        }

        public void onSeekTo(long l)
        {
            Callback.this.onSeekTo(l);
        }

        public void onSetRating(Object obj)
        {
            Callback.this.onSetRating(RatingCompat.fromRating(obj));
        }

        public void onSkipToNext()
        {
            Callback.this.onSkipToNext();
        }

        public void onSkipToPrevious()
        {
            Callback.this.onSkipToPrevious();
        }

        public void onSkipToQueueItem(long l)
        {
            Callback.this.onSkipToQueueItem(l);
        }

        public void onStop()
        {
            Callback.this.onStop();
        }

        final Callback this$0;

        private Callback.StubApi21()
        {
            this$0 = Callback.this;
            super();
        }

    }

    private class Callback.StubApi23 extends Callback.StubApi21
        implements MediaSessionCompatApi23.Callback
    {

        public void onPlayFromUri(Uri uri, Bundle bundle)
        {
            Callback.this.onPlayFromUri(uri, bundle);
        }

        final Callback this$0;

        private Callback.StubApi23()
        {
            this$0 = Callback.this;
            super();
        }

    }

    static interface MediaSessionImpl
    {

        public abstract Object getMediaSession();

        public abstract Object getRemoteControlClient();

        public abstract Token getSessionToken();

        public abstract boolean isActive();

        public abstract void release();

        public abstract void sendSessionEvent(String s, Bundle bundle);

        public abstract void setActive(boolean flag);

        public abstract void setCallback(Callback callback, Handler handler);

        public abstract void setExtras(Bundle bundle);

        public abstract void setFlags(int i);

        public abstract void setMediaButtonReceiver(PendingIntent pendingintent);

        public abstract void setMetadata(MediaMetadataCompat mediametadatacompat);

        public abstract void setPlaybackState(PlaybackStateCompat playbackstatecompat);

        public abstract void setPlaybackToLocal(int i);

        public abstract void setPlaybackToRemote(VolumeProviderCompat volumeprovidercompat);

        public abstract void setQueue(List list);

        public abstract void setQueueTitle(CharSequence charsequence);

        public abstract void setRatingType(int i);

        public abstract void setSessionActivity(PendingIntent pendingintent);
    }

    static class MediaSessionImplApi21
        implements MediaSessionImpl
    {

        public Object getMediaSession()
        {
            return mSessionObj;
        }

        public Object getRemoteControlClient()
        {
            return null;
        }

        public Token getSessionToken()
        {
            return mToken;
        }

        public boolean isActive()
        {
            return MediaSessionCompatApi21.isActive(mSessionObj);
        }

        public void release()
        {
            MediaSessionCompatApi21.release(mSessionObj);
        }

        public void sendSessionEvent(String s, Bundle bundle)
        {
            MediaSessionCompatApi21.sendSessionEvent(mSessionObj, s, bundle);
        }

        public void setActive(boolean flag)
        {
            MediaSessionCompatApi21.setActive(mSessionObj, flag);
        }

        public void setCallback(Callback callback, Handler handler)
        {
            Object obj = mSessionObj;
            if(callback == null)
                callback = null;
            else
                callback = ((Callback) (callback.mCallbackObj));
            MediaSessionCompatApi21.setCallback(obj, callback, handler);
        }

        public void setExtras(Bundle bundle)
        {
            MediaSessionCompatApi21.setExtras(mSessionObj, bundle);
        }

        public void setFlags(int i)
        {
            MediaSessionCompatApi21.setFlags(mSessionObj, i);
        }

        public void setMediaButtonReceiver(PendingIntent pendingintent)
        {
            mMediaButtonIntent = pendingintent;
            MediaSessionCompatApi21.setMediaButtonReceiver(mSessionObj, pendingintent);
        }

        public void setMetadata(MediaMetadataCompat mediametadatacompat)
        {
            Object obj = mSessionObj;
            if(mediametadatacompat == null)
                mediametadatacompat = null;
            else
                mediametadatacompat = ((MediaMetadataCompat) (mediametadatacompat.getMediaMetadata()));
            MediaSessionCompatApi21.setMetadata(obj, mediametadatacompat);
        }

        public void setPlaybackState(PlaybackStateCompat playbackstatecompat)
        {
            Object obj = mSessionObj;
            if(playbackstatecompat == null)
                playbackstatecompat = null;
            else
                playbackstatecompat = ((PlaybackStateCompat) (playbackstatecompat.getPlaybackState()));
            MediaSessionCompatApi21.setPlaybackState(obj, playbackstatecompat);
        }

        public void setPlaybackToLocal(int i)
        {
            MediaSessionCompatApi21.setPlaybackToLocal(mSessionObj, i);
        }

        public void setPlaybackToRemote(VolumeProviderCompat volumeprovidercompat)
        {
            MediaSessionCompatApi21.setPlaybackToRemote(mSessionObj, volumeprovidercompat.getVolumeProvider());
        }

        public void setQueue(List list)
        {
            ArrayList arraylist = null;
            if(list != null)
            {
                ArrayList arraylist1 = new ArrayList();
                list = list.iterator();
                do
                {
                    arraylist = arraylist1;
                    if(!list.hasNext())
                        break;
                    arraylist1.add(((QueueItem)list.next()).getQueueItem());
                } while(true);
            }
            MediaSessionCompatApi21.setQueue(mSessionObj, arraylist);
        }

        public void setQueueTitle(CharSequence charsequence)
        {
            MediaSessionCompatApi21.setQueueTitle(mSessionObj, charsequence);
        }

        public void setRatingType(int i)
        {
            if(android.os.Build.VERSION.SDK_INT >= 22)
                MediaSessionCompatApi22.setRatingType(mSessionObj, i);
        }

        public void setSessionActivity(PendingIntent pendingintent)
        {
            MediaSessionCompatApi21.setSessionActivity(mSessionObj, pendingintent);
        }

        private PendingIntent mMediaButtonIntent;
        private final Object mSessionObj;
        private final Token mToken;

        public MediaSessionImplApi21(Context context, String s)
        {
            mSessionObj = MediaSessionCompatApi21.createSession(context, s);
            mToken = new Token(MediaSessionCompatApi21.getSessionToken(mSessionObj));
        }

        public MediaSessionImplApi21(Object obj)
        {
            mSessionObj = MediaSessionCompatApi21.verifySession(obj);
            mToken = new Token(MediaSessionCompatApi21.getSessionToken(mSessionObj));
        }
    }

    static class MediaSessionImplBase
        implements MediaSessionImpl
    {

        private void adjustVolume(int i, int j)
        {
            if(mVolumeType == 2)
            {
                if(mVolumeProvider != null)
                    mVolumeProvider.onAdjustVolume(i);
            } else
            {
                mAudioManager.adjustStreamVolume(mLocalStream, i, j);
            }
        }

        private MediaMetadataCompat cloneMetadataIfNeeded(MediaMetadataCompat mediametadatacompat)
        {
            if(mediametadatacompat != null) goto _L2; else goto _L1
_L1:
            Object obj = null;
_L4:
            return ((MediaMetadataCompat) (obj));
_L2:
            if(!mediametadatacompat.containsKey("android.media.metadata.ART"))
            {
                obj = mediametadatacompat;
                if(!mediametadatacompat.containsKey("android.media.metadata.ALBUM_ART"))
                    continue; /* Loop/switch isn't completed */
            }
            obj = new android.support.v4.media.MediaMetadataCompat.Builder(mediametadatacompat);
            Bitmap bitmap = mediametadatacompat.getBitmap("android.media.metadata.ART");
            if(bitmap != null)
                ((android.support.v4.media.MediaMetadataCompat.Builder) (obj)).putBitmap("android.media.metadata.ART", bitmap.copy(bitmap.getConfig(), false));
            mediametadatacompat = mediametadatacompat.getBitmap("android.media.metadata.ALBUM_ART");
            if(mediametadatacompat != null)
                ((android.support.v4.media.MediaMetadataCompat.Builder) (obj)).putBitmap("android.media.metadata.ALBUM_ART", mediametadatacompat.copy(mediametadatacompat.getConfig(), false));
            obj = ((android.support.v4.media.MediaMetadataCompat.Builder) (obj)).build();
            if(true) goto _L4; else goto _L3
_L3:
        }

        private PlaybackStateCompat getStateWithUpdatedPosition()
        {
            long l = -1L;
            Object obj = mLock;
            obj;
            JVM INSTR monitorenter ;
            PlaybackStateCompat playbackstatecompat = mState;
            long l1 = l;
            if(mMetadata == null)
                break MISSING_BLOCK_LABEL_55;
            l1 = l;
            if(mMetadata.containsKey("android.media.metadata.DURATION"))
                l1 = mMetadata.getLong("android.media.metadata.DURATION");
            obj;
            JVM INSTR monitorexit ;
            Object obj1;
            obj1 = null;
            obj = obj1;
            if(playbackstatecompat == null) goto _L2; else goto _L1
_L1:
            if(playbackstatecompat.getState() == 3 || playbackstatecompat.getState() == 4) goto _L4; else goto _L3
_L3:
            obj = obj1;
            if(playbackstatecompat.getState() != 5) goto _L2; else goto _L4
_L4:
            long l2;
            l = playbackstatecompat.getLastPositionUpdateTime();
            l2 = SystemClock.elapsedRealtime();
            obj = obj1;
            if(l <= 0L) goto _L2; else goto _L5
_L5:
            l = (long)(playbackstatecompat.getPlaybackSpeed() * (float)(l2 - l)) + playbackstatecompat.getPosition();
            if(l1 < 0L || l <= l1) goto _L7; else goto _L6
_L6:
            obj = new PlaybackStateCompat.Builder(playbackstatecompat);
            ((PlaybackStateCompat.Builder) (obj)).setState(playbackstatecompat.getState(), l1, playbackstatecompat.getPlaybackSpeed(), l2);
            obj = ((PlaybackStateCompat.Builder) (obj)).build();
_L2:
            if(obj == null)
                obj = playbackstatecompat;
            return ((PlaybackStateCompat) (obj));
            Exception exception;
            exception;
            obj;
            JVM INSTR monitorexit ;
            throw exception;
_L7:
            l1 = l;
            if(l < 0L)
                l1 = 0L;
            if(true) goto _L6; else goto _L8
_L8:
        }

        private void postToHandler(int i)
        {
            postToHandler(i, null);
        }

        private void postToHandler(int i, Object obj)
        {
            postToHandler(i, obj, null);
        }

        private void postToHandler(int i, Object obj, Bundle bundle)
        {
            synchronized(mLock)
            {
                if(mHandler != null)
                    mHandler.post(i, obj, bundle);
            }
            return;
            obj;
            obj1;
            JVM INSTR monitorexit ;
            throw obj;
        }

        private void sendEvent(String s, Bundle bundle)
        {
            int i = mControllerCallbacks.beginBroadcast() - 1;
            while(i >= 0) 
            {
                IMediaControllerCallback imediacontrollercallback = (IMediaControllerCallback)mControllerCallbacks.getBroadcastItem(i);
                try
                {
                    imediacontrollercallback.onEvent(s, bundle);
                }
                catch(RemoteException remoteexception) { }
                i--;
            }
            mControllerCallbacks.finishBroadcast();
        }

        private void sendMetadata(MediaMetadataCompat mediametadatacompat)
        {
            int i = mControllerCallbacks.beginBroadcast() - 1;
            while(i >= 0) 
            {
                IMediaControllerCallback imediacontrollercallback = (IMediaControllerCallback)mControllerCallbacks.getBroadcastItem(i);
                try
                {
                    imediacontrollercallback.onMetadataChanged(mediametadatacompat);
                }
                catch(RemoteException remoteexception) { }
                i--;
            }
            mControllerCallbacks.finishBroadcast();
        }

        private void sendQueue(List list)
        {
            int i = mControllerCallbacks.beginBroadcast() - 1;
            while(i >= 0) 
            {
                IMediaControllerCallback imediacontrollercallback = (IMediaControllerCallback)mControllerCallbacks.getBroadcastItem(i);
                try
                {
                    imediacontrollercallback.onQueueChanged(list);
                }
                catch(RemoteException remoteexception) { }
                i--;
            }
            mControllerCallbacks.finishBroadcast();
        }

        private void sendQueueTitle(CharSequence charsequence)
        {
            int i = mControllerCallbacks.beginBroadcast() - 1;
            while(i >= 0) 
            {
                IMediaControllerCallback imediacontrollercallback = (IMediaControllerCallback)mControllerCallbacks.getBroadcastItem(i);
                try
                {
                    imediacontrollercallback.onQueueTitleChanged(charsequence);
                }
                catch(RemoteException remoteexception) { }
                i--;
            }
            mControllerCallbacks.finishBroadcast();
        }

        private void sendSessionDestroyed()
        {
            int i = mControllerCallbacks.beginBroadcast() - 1;
            while(i >= 0) 
            {
                IMediaControllerCallback imediacontrollercallback = (IMediaControllerCallback)mControllerCallbacks.getBroadcastItem(i);
                try
                {
                    imediacontrollercallback.onSessionDestroyed();
                }
                catch(RemoteException remoteexception) { }
                i--;
            }
            mControllerCallbacks.finishBroadcast();
            mControllerCallbacks.kill();
        }

        private void sendState(PlaybackStateCompat playbackstatecompat)
        {
            int i = mControllerCallbacks.beginBroadcast() - 1;
            while(i >= 0) 
            {
                IMediaControllerCallback imediacontrollercallback = (IMediaControllerCallback)mControllerCallbacks.getBroadcastItem(i);
                try
                {
                    imediacontrollercallback.onPlaybackStateChanged(playbackstatecompat);
                }
                catch(RemoteException remoteexception) { }
                i--;
            }
            mControllerCallbacks.finishBroadcast();
        }

        private void sendVolumeInfoChanged(ParcelableVolumeInfo parcelablevolumeinfo)
        {
            int i = mControllerCallbacks.beginBroadcast() - 1;
            while(i >= 0) 
            {
                IMediaControllerCallback imediacontrollercallback = (IMediaControllerCallback)mControllerCallbacks.getBroadcastItem(i);
                try
                {
                    imediacontrollercallback.onVolumeInfoChanged(parcelablevolumeinfo);
                }
                catch(RemoteException remoteexception) { }
                i--;
            }
            mControllerCallbacks.finishBroadcast();
        }

        private void setVolumeTo(int i, int j)
        {
            if(mVolumeType == 2)
            {
                if(mVolumeProvider != null)
                    mVolumeProvider.onSetVolumeTo(i);
            } else
            {
                mAudioManager.setStreamVolume(mLocalStream, i, j);
            }
        }

        private boolean update()
        {
            boolean flag = false;
            boolean flag1;
            if(mIsActive)
            {
                if(android.os.Build.VERSION.SDK_INT >= 8)
                    if(!mIsMbrRegistered && (mFlags & 1) != 0)
                    {
                        if(android.os.Build.VERSION.SDK_INT >= 18)
                            MediaSessionCompatApi18.registerMediaButtonEventReceiver(mContext, mMediaButtonEventReceiver, mComponentName);
                        else
                            MediaSessionCompatApi8.registerMediaButtonEventReceiver(mContext, mComponentName);
                        mIsMbrRegistered = true;
                    } else
                    if(mIsMbrRegistered && (mFlags & 1) == 0)
                    {
                        if(android.os.Build.VERSION.SDK_INT >= 18)
                            MediaSessionCompatApi18.unregisterMediaButtonEventReceiver(mContext, mMediaButtonEventReceiver, mComponentName);
                        else
                            MediaSessionCompatApi8.unregisterMediaButtonEventReceiver(mContext, mComponentName);
                        mIsMbrRegistered = false;
                    }
                flag1 = flag;
                if(android.os.Build.VERSION.SDK_INT >= 14)
                    if(!mIsRccRegistered && (mFlags & 2) != 0)
                    {
                        MediaSessionCompatApi14.registerRemoteControlClient(mContext, mRccObj);
                        mIsRccRegistered = true;
                        flag1 = true;
                    } else
                    {
                        flag1 = flag;
                        if(mIsRccRegistered)
                        {
                            flag1 = flag;
                            if((mFlags & 2) == 0)
                            {
                                MediaSessionCompatApi14.setState(mRccObj, 0);
                                MediaSessionCompatApi14.unregisterRemoteControlClient(mContext, mRccObj);
                                mIsRccRegistered = false;
                                flag1 = flag;
                            }
                        }
                    }
            } else
            {
                if(mIsMbrRegistered)
                {
                    if(android.os.Build.VERSION.SDK_INT >= 18)
                        MediaSessionCompatApi18.unregisterMediaButtonEventReceiver(mContext, mMediaButtonEventReceiver, mComponentName);
                    else
                        MediaSessionCompatApi8.unregisterMediaButtonEventReceiver(mContext, mComponentName);
                    mIsMbrRegistered = false;
                }
                flag1 = flag;
                if(mIsRccRegistered)
                {
                    MediaSessionCompatApi14.setState(mRccObj, 0);
                    MediaSessionCompatApi14.unregisterRemoteControlClient(mContext, mRccObj);
                    mIsRccRegistered = false;
                    flag1 = flag;
                }
            }
            return flag1;
        }

        public Object getMediaSession()
        {
            return null;
        }

        public Object getRemoteControlClient()
        {
            return mRccObj;
        }

        public Token getSessionToken()
        {
            return mToken;
        }

        public boolean isActive()
        {
            return mIsActive;
        }

        public void release()
        {
            mIsActive = false;
            mDestroyed = true;
            update();
            sendSessionDestroyed();
        }

        public void sendSessionEvent(String s, Bundle bundle)
        {
            sendEvent(s, bundle);
        }

        public void setActive(boolean flag)
        {
            if(flag != mIsActive) goto _L2; else goto _L1
_L1:
            return;
_L2:
            mIsActive = flag;
            if(update())
            {
                setMetadata(mMetadata);
                setPlaybackState(mState);
            }
            if(true) goto _L1; else goto _L3
_L3:
        }

        public void setCallback(Callback callback, Handler handler)
        {
            mCallback = callback;
            if(callback != null) goto _L2; else goto _L1
_L1:
            if(android.os.Build.VERSION.SDK_INT >= 18)
                MediaSessionCompatApi18.setOnPlaybackPositionUpdateListener(mRccObj, null);
            if(android.os.Build.VERSION.SDK_INT >= 19)
                MediaSessionCompatApi19.setOnMetadataUpdateListener(mRccObj, null);
_L4:
            return;
_L2:
            callback = handler;
            if(handler == null)
                callback = new Handler();
            synchronized(mLock)
            {
                MessageHandler messagehandler = JVM INSTR new #21  <Class MediaSessionCompat$MediaSessionImplBase$MessageHandler>;
                messagehandler.this. MessageHandler(callback.getLooper());
                mHandler = messagehandler;
            }
            callback = new MediaSessionCompatApi19.Callback() {

                public void onSeekTo(long l)
                {
                    postToHandler(11, Long.valueOf(l));
                }

                public void onSetRating(Object obj)
                {
                    postToHandler(12, RatingCompat.fromRating(obj));
                }

                final MediaSessionImplBase this$0;

            
            {
                this$0 = MediaSessionImplBase.this;
                super();
            }
            }
;
            if(android.os.Build.VERSION.SDK_INT >= 18)
            {
                handler = ((Handler) (MediaSessionCompatApi18.createPlaybackPositionUpdateListener(callback)));
                MediaSessionCompatApi18.setOnPlaybackPositionUpdateListener(mRccObj, handler);
            }
            if(android.os.Build.VERSION.SDK_INT >= 19)
            {
                callback = ((Callback) (MediaSessionCompatApi19.createMetadataUpdateListener(callback)));
                MediaSessionCompatApi19.setOnMetadataUpdateListener(mRccObj, callback);
            }
            if(true) goto _L4; else goto _L3
_L3:
            callback;
            handler;
            JVM INSTR monitorexit ;
            throw callback;
        }

        public void setExtras(Bundle bundle)
        {
            mExtras = bundle;
        }

        public void setFlags(int i)
        {
            synchronized(mLock)
            {
                mFlags = i;
            }
            update();
            return;
            exception;
            obj;
            JVM INSTR monitorexit ;
            throw exception;
        }

        public void setMediaButtonReceiver(PendingIntent pendingintent)
        {
        }

        public void setMetadata(MediaMetadataCompat mediametadatacompat)
        {
            Object obj;
            Object obj1;
            obj = null;
            obj1 = null;
            obj3 = mediametadatacompat;
            if(android.os.Build.VERSION.SDK_INT >= 14)
            {
                obj3 = mediametadatacompat;
                if(mediametadatacompat != null)
                    obj3 = cloneMetadataIfNeeded(mediametadatacompat);
            }
            synchronized(mLock)
            {
                mMetadata = ((MediaMetadataCompat) (obj3));
            }
            sendMetadata(((MediaMetadataCompat) (obj3)));
            if(mIsActive) goto _L2; else goto _L1
_L1:
            return;
            obj3;
            mediametadatacompat;
            JVM INSTR monitorexit ;
            throw obj3;
_L2:
            if(android.os.Build.VERSION.SDK_INT >= 19)
            {
                obj = mRccObj;
                long l;
                if(obj3 == null)
                    mediametadatacompat = obj1;
                else
                    mediametadatacompat = ((MediaMetadataCompat) (obj3)).getBundle();
                if(mState == null)
                    l = 0L;
                else
                    l = mState.getActions();
                MediaSessionCompatApi19.setMetadata(obj, mediametadatacompat, l);
            } else
            if(android.os.Build.VERSION.SDK_INT >= 14)
            {
                Object obj2 = mRccObj;
                if(obj3 == null)
                    mediametadatacompat = ((MediaMetadataCompat) (obj));
                else
                    mediametadatacompat = ((MediaMetadataCompat) (obj3)).getBundle();
                MediaSessionCompatApi14.setMetadata(obj2, mediametadatacompat);
            }
            if(true) goto _L1; else goto _L3
_L3:
        }

        public void setPlaybackState(PlaybackStateCompat playbackstatecompat)
        {
            synchronized(mLock)
            {
                mState = playbackstatecompat;
            }
            sendState(playbackstatecompat);
            if(mIsActive) goto _L2; else goto _L1
_L1:
            return;
            playbackstatecompat;
            obj;
            JVM INSTR monitorexit ;
            throw playbackstatecompat;
_L2:
            if(playbackstatecompat == null)
            {
                if(android.os.Build.VERSION.SDK_INT >= 14)
                {
                    MediaSessionCompatApi14.setState(mRccObj, 0);
                    MediaSessionCompatApi14.setTransportControlFlags(mRccObj, 0L);
                }
                continue; /* Loop/switch isn't completed */
            }
            if(android.os.Build.VERSION.SDK_INT < 18) goto _L4; else goto _L3
_L3:
            MediaSessionCompatApi18.setState(mRccObj, playbackstatecompat.getState(), playbackstatecompat.getPosition(), playbackstatecompat.getPlaybackSpeed(), playbackstatecompat.getLastPositionUpdateTime());
_L6:
            if(android.os.Build.VERSION.SDK_INT < 19)
                break; /* Loop/switch isn't completed */
            MediaSessionCompatApi19.setTransportControlFlags(mRccObj, playbackstatecompat.getActions());
            continue; /* Loop/switch isn't completed */
_L4:
            if(android.os.Build.VERSION.SDK_INT >= 14)
                MediaSessionCompatApi14.setState(mRccObj, playbackstatecompat.getState());
            if(true) goto _L6; else goto _L5
_L5:
            if(android.os.Build.VERSION.SDK_INT >= 18)
                MediaSessionCompatApi18.setTransportControlFlags(mRccObj, playbackstatecompat.getActions());
            else
            if(android.os.Build.VERSION.SDK_INT >= 14)
                MediaSessionCompatApi14.setTransportControlFlags(mRccObj, playbackstatecompat.getActions());
            if(true) goto _L1; else goto _L7
_L7:
        }

        public void setPlaybackToLocal(int i)
        {
            if(mVolumeProvider != null)
                mVolumeProvider.setCallback(null);
            mVolumeType = 1;
            sendVolumeInfoChanged(new ParcelableVolumeInfo(mVolumeType, mLocalStream, 2, mAudioManager.getStreamMaxVolume(mLocalStream), mAudioManager.getStreamVolume(mLocalStream)));
        }

        public void setPlaybackToRemote(VolumeProviderCompat volumeprovidercompat)
        {
            if(volumeprovidercompat == null)
                throw new IllegalArgumentException("volumeProvider may not be null");
            if(mVolumeProvider != null)
                mVolumeProvider.setCallback(null);
            mVolumeType = 2;
            mVolumeProvider = volumeprovidercompat;
            sendVolumeInfoChanged(new ParcelableVolumeInfo(mVolumeType, mLocalStream, mVolumeProvider.getVolumeControl(), mVolumeProvider.getMaxVolume(), mVolumeProvider.getCurrentVolume()));
            volumeprovidercompat.setCallback(mVolumeCallback);
        }

        public void setQueue(List list)
        {
            mQueue = list;
            sendQueue(list);
        }

        public void setQueueTitle(CharSequence charsequence)
        {
            mQueueTitle = charsequence;
            sendQueueTitle(charsequence);
        }

        public void setRatingType(int i)
        {
            mRatingType = i;
        }

        public void setSessionActivity(PendingIntent pendingintent)
        {
            synchronized(mLock)
            {
                mSessionActivity = pendingintent;
            }
            return;
            pendingintent;
            obj;
            JVM INSTR monitorexit ;
            throw pendingintent;
        }

        private final AudioManager mAudioManager;
        private volatile Callback mCallback;
        private final ComponentName mComponentName;
        private final Context mContext;
        private final RemoteCallbackList mControllerCallbacks = new RemoteCallbackList();
        private boolean mDestroyed;
        private Bundle mExtras;
        private int mFlags;
        private MessageHandler mHandler;
        private boolean mIsActive;
        private boolean mIsMbrRegistered;
        private boolean mIsRccRegistered;
        private int mLocalStream;
        private final Object mLock = new Object();
        private final PendingIntent mMediaButtonEventReceiver;
        private MediaMetadataCompat mMetadata;
        private final String mPackageName;
        private List mQueue;
        private CharSequence mQueueTitle;
        private int mRatingType;
        private final Object mRccObj;
        private PendingIntent mSessionActivity;
        private PlaybackStateCompat mState;
        private final MediaSessionStub mStub = new MediaSessionStub();
        private final String mTag;
        private final Token mToken;
        private android.support.v4.media.VolumeProviderCompat.Callback mVolumeCallback;
        private VolumeProviderCompat mVolumeProvider;
        private int mVolumeType;


























        public MediaSessionImplBase(Context context, String s, ComponentName componentname, PendingIntent pendingintent)
        {
            mDestroyed = false;
            mIsActive = false;
            mIsRccRegistered = false;
            mIsMbrRegistered = false;
            mVolumeCallback = new _cls1();
            if(componentname == null)
                throw new IllegalArgumentException("MediaButtonReceiver component may not be null.");
            mContext = context;
            mPackageName = context.getPackageName();
            mAudioManager = (AudioManager)context.getSystemService("audio");
            mTag = s;
            mComponentName = componentname;
            mMediaButtonEventReceiver = pendingintent;
            mToken = new Token(mStub);
            mRatingType = 0;
            mVolumeType = 1;
            mLocalStream = 3;
            if(android.os.Build.VERSION.SDK_INT >= 14)
                mRccObj = MediaSessionCompatApi14.createRemoteControlClient(pendingintent);
            else
                mRccObj = null;
        }
    }

    private static final class MediaSessionImplBase.Command
    {

        public final String command;
        public final Bundle extras;
        public final ResultReceiver stub;

        public MediaSessionImplBase.Command(String s, Bundle bundle, ResultReceiver resultreceiver)
        {
            command = s;
            extras = bundle;
            stub = resultreceiver;
        }
    }

    class MediaSessionImplBase.MediaSessionStub extends IMediaSession.Stub
    {

        public void adjustVolume(int i, int j, String s)
        {
            MediaSessionImplBase.this.adjustVolume(i, j);
        }

        public void fastForward()
            throws RemoteException
        {
            postToHandler(9);
        }

        public Bundle getExtras()
        {
            Bundle bundle;
            synchronized(mLock)
            {
                bundle = mExtras;
            }
            return bundle;
            exception;
            obj;
            JVM INSTR monitorexit ;
            throw exception;
        }

        public long getFlags()
        {
            long l;
            synchronized(mLock)
            {
                l = mFlags;
            }
            return l;
            exception;
            obj;
            JVM INSTR monitorexit ;
            throw exception;
        }

        public PendingIntent getLaunchPendingIntent()
        {
            PendingIntent pendingintent;
            synchronized(mLock)
            {
                pendingintent = mSessionActivity;
            }
            return pendingintent;
            exception;
            obj;
            JVM INSTR monitorexit ;
            throw exception;
        }

        public MediaMetadataCompat getMetadata()
        {
            return mMetadata;
        }

        public String getPackageName()
        {
            return mPackageName;
        }

        public PlaybackStateCompat getPlaybackState()
        {
            return getStateWithUpdatedPosition();
        }

        public List getQueue()
        {
            List list;
            synchronized(mLock)
            {
                list = mQueue;
            }
            return list;
            exception;
            obj;
            JVM INSTR monitorexit ;
            throw exception;
        }

        public CharSequence getQueueTitle()
        {
            return mQueueTitle;
        }

        public int getRatingType()
        {
            return mRatingType;
        }

        public String getTag()
        {
            return mTag;
        }

        public ParcelableVolumeInfo getVolumeAttributes()
        {
            Object obj = mLock;
            obj;
            JVM INSTR monitorenter ;
            int i;
            int j;
            VolumeProviderCompat volumeprovidercompat;
            i = mVolumeType;
            j = mLocalStream;
            volumeprovidercompat = mVolumeProvider;
            if(i != 2) goto _L2; else goto _L1
_L1:
            int k;
            int l;
            int i1;
            k = volumeprovidercompat.getVolumeControl();
            l = volumeprovidercompat.getMaxVolume();
            i1 = volumeprovidercompat.getCurrentVolume();
_L3:
            obj;
            JVM INSTR monitorexit ;
            return new ParcelableVolumeInfo(i, j, k, l, i1);
_L2:
            k = 2;
            l = mAudioManager.getStreamMaxVolume(j);
            i1 = mAudioManager.getStreamVolume(j);
              goto _L3
            Exception exception;
            exception;
            obj;
            JVM INSTR monitorexit ;
            throw exception;
        }

        public boolean isTransportControlEnabled()
        {
            boolean flag;
            if((mFlags & 2) != 0)
                flag = true;
            else
                flag = false;
            return flag;
        }

        public void next()
            throws RemoteException
        {
            postToHandler(7);
        }

        public void pause()
            throws RemoteException
        {
            postToHandler(5);
        }

        public void play()
            throws RemoteException
        {
            postToHandler(1);
        }

        public void playFromMediaId(String s, Bundle bundle)
            throws RemoteException
        {
            postToHandler(2, s, bundle);
        }

        public void playFromSearch(String s, Bundle bundle)
            throws RemoteException
        {
            postToHandler(3, s, bundle);
        }

        public void playFromUri(Uri uri, Bundle bundle)
            throws RemoteException
        {
            postToHandler(18, uri, bundle);
        }

        public void previous()
            throws RemoteException
        {
            postToHandler(8);
        }

        public void rate(RatingCompat ratingcompat)
            throws RemoteException
        {
            postToHandler(12, ratingcompat);
        }

        public void registerCallbackListener(IMediaControllerCallback imediacontrollercallback)
        {
            if(!mDestroyed) goto _L2; else goto _L1
_L1:
            imediacontrollercallback.onSessionDestroyed();
_L4:
            return;
_L2:
            mControllerCallbacks.register(imediacontrollercallback);
            continue; /* Loop/switch isn't completed */
            imediacontrollercallback;
            if(true) goto _L4; else goto _L3
_L3:
        }

        public void rewind()
            throws RemoteException
        {
            postToHandler(10);
        }

        public void seekTo(long l)
            throws RemoteException
        {
            postToHandler(11, Long.valueOf(l));
        }

        public void sendCommand(String s, Bundle bundle, ResultReceiverWrapper resultreceiverwrapper)
        {
            postToHandler(15, new Command(s, bundle, resultreceiverwrapper.mResultReceiver));
        }

        public void sendCustomAction(String s, Bundle bundle)
            throws RemoteException
        {
            postToHandler(13, s, bundle);
        }

        public boolean sendMediaButton(KeyEvent keyevent)
        {
            boolean flag;
            if((mFlags & 1) != 0)
                flag = true;
            else
                flag = false;
            if(flag)
                postToHandler(14, keyevent);
            return flag;
        }

        public void setVolumeTo(int i, int j, String s)
        {
            MediaSessionImplBase.this.setVolumeTo(i, j);
        }

        public void skipToQueueItem(long l)
        {
            postToHandler(4, Long.valueOf(l));
        }

        public void stop()
            throws RemoteException
        {
            postToHandler(6);
        }

        public void unregisterCallbackListener(IMediaControllerCallback imediacontrollercallback)
        {
            mControllerCallbacks.unregister(imediacontrollercallback);
        }

        final MediaSessionImplBase this$0;

        MediaSessionImplBase.MediaSessionStub()
        {
            this$0 = MediaSessionImplBase.this;
            super();
        }
    }

    private class MediaSessionImplBase.MessageHandler extends Handler
    {

        private void onMediaButtonEvent(KeyEvent keyevent, Callback callback)
        {
            boolean flag = true;
            if(keyevent != null && keyevent.getAction() == 0) goto _L2; else goto _L1
_L1:
            return;
_L2:
            long l;
            if(mState == null)
                l = 0L;
            else
                l = mState.getActions();
            switch(keyevent.getKeyCode())
            {
            case 79: // 'O'
            case 85: // 'U'
                boolean flag1;
                boolean flag2;
                if(mState != null && mState.getState() == 3)
                    flag1 = true;
                else
                    flag1 = false;
                if((516L & l) != 0L)
                    flag2 = true;
                else
                    flag2 = false;
                if((514L & l) == 0L)
                    flag = false;
                if(flag1 && flag)
                    callback.onPause();
                else
                if(!flag1 && flag2)
                    callback.onPlay();
                break;

            case 126: // '~'
                if((4L & l) != 0L)
                    callback.onPlay();
                break;

            case 127: // '\177'
                if((2L & l) != 0L)
                    callback.onPause();
                break;

            case 87: // 'W'
                if((32L & l) != 0L)
                    callback.onSkipToNext();
                break;

            case 88: // 'X'
                if((16L & l) != 0L)
                    callback.onSkipToPrevious();
                break;

            case 86: // 'V'
                if((1L & l) != 0L)
                    callback.onStop();
                break;

            case 90: // 'Z'
                if((64L & l) != 0L)
                    callback.onFastForward();
                break;

            case 89: // 'Y'
                if((8L & l) != 0L)
                    callback.onRewind();
                break;
            }
            if(true) goto _L1; else goto _L3
_L3:
        }

        public void handleMessage(Message message)
        {
            Callback callback = mCallback;
            if(callback != null) goto _L2; else goto _L1
_L1:
            return;
_L2:
            switch(message.what)
            {
            case 1: // '\001'
                callback.onPlay();
                break;

            case 2: // '\002'
                callback.onPlayFromMediaId((String)message.obj, message.getData());
                break;

            case 3: // '\003'
                callback.onPlayFromSearch((String)message.obj, message.getData());
                break;

            case 18: // '\022'
                callback.onPlayFromUri((Uri)message.obj, message.getData());
                break;

            case 4: // '\004'
                callback.onSkipToQueueItem(((Long)message.obj).longValue());
                break;

            case 5: // '\005'
                callback.onPause();
                break;

            case 6: // '\006'
                callback.onStop();
                break;

            case 7: // '\007'
                callback.onSkipToNext();
                break;

            case 8: // '\b'
                callback.onSkipToPrevious();
                break;

            case 9: // '\t'
                callback.onFastForward();
                break;

            case 10: // '\n'
                callback.onRewind();
                break;

            case 11: // '\013'
                callback.onSeekTo(((Long)message.obj).longValue());
                break;

            case 12: // '\f'
                callback.onSetRating((RatingCompat)message.obj);
                break;

            case 13: // '\r'
                callback.onCustomAction((String)message.obj, message.getData());
                break;

            case 14: // '\016'
                message = (KeyEvent)message.obj;
                Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
                intent.putExtra("android.intent.extra.KEY_EVENT", message);
                if(!callback.onMediaButtonEvent(intent))
                    onMediaButtonEvent(message, callback);
                break;

            case 15: // '\017'
                message = (MediaSessionImplBase.Command)message.obj;
                callback.onCommand(((MediaSessionImplBase.Command) (message)).command, ((MediaSessionImplBase.Command) (message)).extras, ((MediaSessionImplBase.Command) (message)).stub);
                break;

            case 16: // '\020'
                adjustVolume(((Integer)message.obj).intValue(), 0);
                break;

            case 17: // '\021'
                setVolumeTo(((Integer)message.obj).intValue(), 0);
                break;
            }
            if(true) goto _L1; else goto _L3
_L3:
        }

        public void post(int i)
        {
            post(i, null);
        }

        public void post(int i, Object obj)
        {
            obtainMessage(i, obj).sendToTarget();
        }

        public void post(int i, Object obj, int j)
        {
            obtainMessage(i, j, 0, obj).sendToTarget();
        }

        public void post(int i, Object obj, Bundle bundle)
        {
            obj = obtainMessage(i, obj);
            ((Message) (obj)).setData(bundle);
            ((Message) (obj)).sendToTarget();
        }

        private static final int KEYCODE_MEDIA_PAUSE = 127;
        private static final int KEYCODE_MEDIA_PLAY = 126;
        private static final int MSG_ADJUST_VOLUME = 16;
        private static final int MSG_COMMAND = 15;
        private static final int MSG_CUSTOM_ACTION = 13;
        private static final int MSG_FAST_FORWARD = 9;
        private static final int MSG_MEDIA_BUTTON = 14;
        private static final int MSG_NEXT = 7;
        private static final int MSG_PAUSE = 5;
        private static final int MSG_PLAY = 1;
        private static final int MSG_PLAY_MEDIA_ID = 2;
        private static final int MSG_PLAY_SEARCH = 3;
        private static final int MSG_PLAY_URI = 18;
        private static final int MSG_PREVIOUS = 8;
        private static final int MSG_RATE = 12;
        private static final int MSG_REWIND = 10;
        private static final int MSG_SEEK_TO = 11;
        private static final int MSG_SET_VOLUME = 17;
        private static final int MSG_SKIP_TO_ITEM = 4;
        private static final int MSG_STOP = 6;
        final MediaSessionImplBase this$0;

        public MediaSessionImplBase.MessageHandler(Looper looper)
        {
            this$0 = MediaSessionImplBase.this;
            super(looper);
        }
    }

    public static interface OnActiveChangeListener
    {

        public abstract void onActiveChanged();
    }

    public static final class QueueItem
        implements Parcelable
    {

        public static QueueItem obtain(Object obj)
        {
            return new QueueItem(obj, MediaDescriptionCompat.fromMediaDescription(MediaSessionCompatApi21.QueueItem.getDescription(obj)), MediaSessionCompatApi21.QueueItem.getQueueId(obj));
        }

        public int describeContents()
        {
            return 0;
        }

        public MediaDescriptionCompat getDescription()
        {
            return mDescription;
        }

        public long getQueueId()
        {
            return mId;
        }

        public Object getQueueItem()
        {
            Object obj;
            if(mItem != null || android.os.Build.VERSION.SDK_INT < 21)
            {
                obj = mItem;
            } else
            {
                mItem = MediaSessionCompatApi21.QueueItem.createItem(mDescription.getMediaDescription(), mId);
                obj = mItem;
            }
            return obj;
        }

        public String toString()
        {
            return (new StringBuilder()).append("MediaSession.QueueItem {Description=").append(mDescription).append(", Id=").append(mId).append(" }").toString();
        }

        public void writeToParcel(Parcel parcel, int i)
        {
            mDescription.writeToParcel(parcel, i);
            parcel.writeLong(mId);
        }

        public static final android.os.Parcelable.Creator CREATOR = new android.os.Parcelable.Creator() {

            public QueueItem createFromParcel(Parcel parcel)
            {
                return new QueueItem(parcel);
            }

            public volatile Object createFromParcel(Parcel parcel)
            {
                return createFromParcel(parcel);
            }

            public QueueItem[] newArray(int i)
            {
                return new QueueItem[i];
            }

            public volatile Object[] newArray(int i)
            {
                return newArray(i);
            }

        }
;
        public static final int UNKNOWN_ID = -1;
        private final MediaDescriptionCompat mDescription;
        private final long mId;
        private Object mItem;


        private QueueItem(Parcel parcel)
        {
            mDescription = (MediaDescriptionCompat)MediaDescriptionCompat.CREATOR.createFromParcel(parcel);
            mId = parcel.readLong();
        }


        public QueueItem(MediaDescriptionCompat mediadescriptioncompat, long l)
        {
            this(null, mediadescriptioncompat, l);
        }

        private QueueItem(Object obj, MediaDescriptionCompat mediadescriptioncompat, long l)
        {
            if(mediadescriptioncompat == null)
                throw new IllegalArgumentException("Description cannot be null.");
            if(l == -1L)
            {
                throw new IllegalArgumentException("Id cannot be QueueItem.UNKNOWN_ID");
            } else
            {
                mDescription = mediadescriptioncompat;
                mId = l;
                mItem = obj;
                return;
            }
        }
    }

    static final class ResultReceiverWrapper
        implements Parcelable
    {

        public int describeContents()
        {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i)
        {
            mResultReceiver.writeToParcel(parcel, i);
        }

        public static final android.os.Parcelable.Creator CREATOR = new android.os.Parcelable.Creator() {

            public ResultReceiverWrapper createFromParcel(Parcel parcel)
            {
                return new ResultReceiverWrapper(parcel);
            }

            public volatile Object createFromParcel(Parcel parcel)
            {
                return createFromParcel(parcel);
            }

            public ResultReceiverWrapper[] newArray(int i)
            {
                return new ResultReceiverWrapper[i];
            }

            public volatile Object[] newArray(int i)
            {
                return newArray(i);
            }

        }
;
        private ResultReceiver mResultReceiver;



        ResultReceiverWrapper(Parcel parcel)
        {
            mResultReceiver = (ResultReceiver)ResultReceiver.CREATOR.createFromParcel(parcel);
        }

        public ResultReceiverWrapper(ResultReceiver resultreceiver)
        {
            mResultReceiver = resultreceiver;
        }
    }

    public static interface SessionFlags
        extends Annotation
    {
    }

    public static final class Token
        implements Parcelable
    {

        public static Token fromToken(Object obj)
        {
            if(obj == null || android.os.Build.VERSION.SDK_INT < 21)
                obj = null;
            else
                obj = new Token(MediaSessionCompatApi21.verifyToken(obj));
            return ((Token) (obj));
        }

        public int describeContents()
        {
            return 0;
        }

        public Object getToken()
        {
            return mInner;
        }

        public void writeToParcel(Parcel parcel, int i)
        {
            if(android.os.Build.VERSION.SDK_INT >= 21)
                parcel.writeParcelable((Parcelable)mInner, i);
            else
                parcel.writeStrongBinder((IBinder)mInner);
        }

        public static final android.os.Parcelable.Creator CREATOR = new android.os.Parcelable.Creator() {

            public Token createFromParcel(Parcel parcel)
            {
                if(android.os.Build.VERSION.SDK_INT >= 21)
                    parcel = parcel.readParcelable(null);
                else
                    parcel = parcel.readStrongBinder();
                return new Token(parcel);
            }

            public volatile Object createFromParcel(Parcel parcel)
            {
                return createFromParcel(parcel);
            }

            public Token[] newArray(int i)
            {
                return new Token[i];
            }

            public volatile Object[] newArray(int i)
            {
                return newArray(i);
            }

        }
;
        private final Object mInner;


        Token(Object obj)
        {
            mInner = obj;
        }
    }


    private MediaSessionCompat(Context context, MediaSessionImpl mediasessionimpl)
    {
        mActiveListeners = new ArrayList();
        mImpl = mediasessionimpl;
        mController = new MediaControllerCompat(context, this);
    }

    public MediaSessionCompat(Context context, String s)
    {
        this(context, s, null, null);
    }

    public MediaSessionCompat(Context context, String s, ComponentName componentname, PendingIntent pendingintent)
    {
        mActiveListeners = new ArrayList();
        if(context == null)
            throw new IllegalArgumentException("context must not be null");
        if(TextUtils.isEmpty(s))
            throw new IllegalArgumentException("tag must not be null or empty");
        Object obj = componentname;
        if(componentname == null)
        {
            obj = new Intent("android.intent.action.MEDIA_BUTTON");
            ((Intent) (obj)).setPackage(context.getPackageName());
            List list = context.getPackageManager().queryBroadcastReceivers(((Intent) (obj)), 0);
            if(list.size() == 1)
            {
                componentname = (ResolveInfo)list.get(0);
                obj = new ComponentName(((ResolveInfo) (componentname)).activityInfo.packageName, ((ResolveInfo) (componentname)).activityInfo.name);
            } else
            {
                obj = componentname;
                if(list.size() > 1)
                {
                    Log.w("MediaSessionCompat", "More than one BroadcastReceiver that handles android.intent.action.MEDIA_BUTTON was found, using null. Provide a specific ComponentName to use as this session's media button receiver");
                    obj = componentname;
                }
            }
        }
        componentname = pendingintent;
        if(obj != null)
        {
            componentname = pendingintent;
            if(pendingintent == null)
            {
                componentname = new Intent("android.intent.action.MEDIA_BUTTON");
                componentname.setComponent(((ComponentName) (obj)));
                componentname = PendingIntent.getBroadcast(context, 0, componentname, 0);
            }
        }
        if(android.os.Build.VERSION.SDK_INT >= 21)
        {
            mImpl = new MediaSessionImplApi21(context, s);
            mImpl.setMediaButtonReceiver(componentname);
        } else
        {
            mImpl = new MediaSessionImplBase(context, s, ((ComponentName) (obj)), componentname);
        }
        mController = new MediaControllerCompat(context, this);
    }

    public static MediaSessionCompat obtain(Context context, Object obj)
    {
        return new MediaSessionCompat(context, new MediaSessionImplApi21(obj));
    }

    public void addOnActiveChangeListener(OnActiveChangeListener onactivechangelistener)
    {
        if(onactivechangelistener == null)
        {
            throw new IllegalArgumentException("Listener may not be null");
        } else
        {
            mActiveListeners.add(onactivechangelistener);
            return;
        }
    }

    public MediaControllerCompat getController()
    {
        return mController;
    }

    public Object getMediaSession()
    {
        return mImpl.getMediaSession();
    }

    public Object getRemoteControlClient()
    {
        return mImpl.getRemoteControlClient();
    }

    public Token getSessionToken()
    {
        return mImpl.getSessionToken();
    }

    public boolean isActive()
    {
        return mImpl.isActive();
    }

    public void release()
    {
        mImpl.release();
    }

    public void removeOnActiveChangeListener(OnActiveChangeListener onactivechangelistener)
    {
        if(onactivechangelistener == null)
        {
            throw new IllegalArgumentException("Listener may not be null");
        } else
        {
            mActiveListeners.remove(onactivechangelistener);
            return;
        }
    }

    public void sendSessionEvent(String s, Bundle bundle)
    {
        if(TextUtils.isEmpty(s))
        {
            throw new IllegalArgumentException("event cannot be null or empty");
        } else
        {
            mImpl.sendSessionEvent(s, bundle);
            return;
        }
    }

    public void setActive(boolean flag)
    {
        mImpl.setActive(flag);
        for(Iterator iterator = mActiveListeners.iterator(); iterator.hasNext(); ((OnActiveChangeListener)iterator.next()).onActiveChanged());
    }

    public void setCallback(Callback callback)
    {
        setCallback(callback, null);
    }

    public void setCallback(Callback callback, Handler handler)
    {
        MediaSessionImpl mediasessionimpl = mImpl;
        if(handler == null)
            handler = new Handler();
        mediasessionimpl.setCallback(callback, handler);
    }

    public void setExtras(Bundle bundle)
    {
        mImpl.setExtras(bundle);
    }

    public void setFlags(int i)
    {
        mImpl.setFlags(i);
    }

    public void setMediaButtonReceiver(PendingIntent pendingintent)
    {
        mImpl.setMediaButtonReceiver(pendingintent);
    }

    public void setMetadata(MediaMetadataCompat mediametadatacompat)
    {
        mImpl.setMetadata(mediametadatacompat);
    }

    public void setPlaybackState(PlaybackStateCompat playbackstatecompat)
    {
        mImpl.setPlaybackState(playbackstatecompat);
    }

    public void setPlaybackToLocal(int i)
    {
        mImpl.setPlaybackToLocal(i);
    }

    public void setPlaybackToRemote(VolumeProviderCompat volumeprovidercompat)
    {
        if(volumeprovidercompat == null)
        {
            throw new IllegalArgumentException("volumeProvider may not be null!");
        } else
        {
            mImpl.setPlaybackToRemote(volumeprovidercompat);
            return;
        }
    }

    public void setQueue(List list)
    {
        mImpl.setQueue(list);
    }

    public void setQueueTitle(CharSequence charsequence)
    {
        mImpl.setQueueTitle(charsequence);
    }

    public void setRatingType(int i)
    {
        mImpl.setRatingType(i);
    }

    public void setSessionActivity(PendingIntent pendingintent)
    {
        mImpl.setSessionActivity(pendingintent);
    }

    public static final String ACTION_ARGUMENT_EXTRAS = "android.support.v4.media.session.action.ARGUMENT_EXTRAS";
    public static final String ACTION_ARGUMENT_URI = "android.support.v4.media.session.action.ARGUMENT_URI";
    public static final String ACTION_PLAY_FROM_URI = "android.support.v4.media.session.action.PLAY_FROM_URI";
    public static final int FLAG_HANDLES_MEDIA_BUTTONS = 1;
    public static final int FLAG_HANDLES_TRANSPORT_CONTROLS = 2;
    private static final String TAG = "MediaSessionCompat";
    private final ArrayList mActiveListeners;
    private final MediaControllerCompat mController;
    private final MediaSessionImpl mImpl;

    // Unreferenced inner class android/support/v4/media/session/MediaSessionCompat$MediaSessionImplBase$1

/* anonymous class */
    class MediaSessionImplBase._cls1 extends android.support.v4.media.VolumeProviderCompat.Callback
    {

        public void onVolumeChanged(VolumeProviderCompat volumeprovidercompat)
        {
            if(mVolumeProvider == volumeprovidercompat)
            {
                volumeprovidercompat = new ParcelableVolumeInfo(mVolumeType, mLocalStream, volumeprovidercompat.getVolumeControl(), volumeprovidercompat.getMaxVolume(), volumeprovidercompat.getCurrentVolume());
                sendVolumeInfoChanged(volumeprovidercompat);
            }
        }

        final MediaSessionImplBase this$0;

            
            {
                this$0 = MediaSessionImplBase.this;
                super();
            }
    }

}
