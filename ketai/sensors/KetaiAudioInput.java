// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package ketai.sensors;

import android.media.AudioRecord;
import android.os.Process;
import android.util.Log;
import java.io.PrintStream;
import java.lang.reflect.Method;
import processing.core.PApplet;

public class KetaiAudioInput
    implements Runnable
{

    public KetaiAudioInput(Object obj)
    {
        audioRecorder = null;
        samplesPerSec = 16000;
        thread = null;
        LOG_TAG = "KetaiAudioInput";
        register(obj);
    }

    public void dispose()
    {
        stop();
    }

    protected void finalize()
        throws Throwable
    {
        super.finalize();
        System.out.println("AudioCapturer finalizer");
        if(audioRecorder != null && audioRecorder.getState() == 1)
        {
            audioRecorder.stop();
            audioRecorder.release();
        }
        audioRecorder = null;
        thread = null;
    }

    public boolean isActive()
    {
        boolean flag = false;
        boolean flag1 = flag;
        if(audioRecorder != null)
        {
            flag1 = flag;
            if(audioRecorder.getRecordingState() == 3)
                flag1 = true;
        }
        return flag1;
    }

    public void onPermissionResult(boolean flag)
    {
        if(!flag)
            PApplet.println("User did not allow to record audio.  Audio recording is disabled.");
    }

    public void register(Object obj)
    {
        callbackdelegate = obj;
        if(callbackdelegate instanceof PApplet)
        {
            PApplet papplet = (PApplet)callbackdelegate;
            papplet.requestPermission("android.permission.RECORD_AUDIO", "onPermissionResult", this);
            papplet.registerMethod("dispose", this);
        }
        callbackMethod = obj.getClass().getMethod("onAudioEvent", new Class[] {
            [S
        });
        PApplet.println("Found onAudioEvent callback method...");
_L1:
        return;
        obj;
        PApplet.println("Failed to find onAudioEvent callback method...");
          goto _L1
    }

    public void run()
    {
        Process.setThreadPriority(-19);
        do
        {
            if(!isRecording || audioRecorder.getRecordingState() != 3)
                return;
            short aword0[] = new short[bufferSize];
            audioRecorder.read(aword0, 0, aword0.length);
            try
            {
                callbackMethod.invoke(callbackdelegate, new Object[] {
                    aword0
                });
            }
            catch(Exception exception)
            {
                PApplet.println((new StringBuilder("OOps... onAudioEvent() because of an error:")).append(exception.getMessage()).toString());
                exception.printStackTrace();
            }
        } while(true);
    }

    public void start()
    {
        bufferSize = AudioRecord.getMinBufferSize(samplesPerSec, 16, 2);
        if(bufferSize != -2 && bufferSize != -1)
        {
            PApplet.println((new StringBuilder("Buffer size: ")).append(bufferSize).toString());
            audioRecorder = new AudioRecord(0, samplesPerSec, 16, 2, bufferSize * 10);
            if(audioRecorder != null && audioRecorder.getState() == 1)
            {
                Log.i(LOG_TAG, "Audio Recorder created");
                PApplet.println("Audio Recorder created");
                audioRecorder.startRecording();
                isRecording = true;
                thread = new Thread(this);
                thread.start();
            } else
            {
                Log.e(LOG_TAG, "Unable to create AudioRecord instance");
                PApplet.println("Unable to create AudioRecord instance");
            }
        } else
        {
            Log.e(LOG_TAG, "Unable to get minimum buffer size");
        }
    }

    public void stop()
    {
        isRecording = false;
        if(audioRecorder != null)
        {
            if(audioRecorder.getRecordingState() == 3)
                audioRecorder.stop();
            if(audioRecorder.getState() == 1)
                audioRecorder.release();
        }
    }

    private String LOG_TAG;
    private AudioRecord audioRecorder;
    private int bufferSize;
    private Method callbackMethod;
    public Object callbackdelegate;
    private boolean isRecording;
    private int samplesPerSec;
    private Thread thread;
}
