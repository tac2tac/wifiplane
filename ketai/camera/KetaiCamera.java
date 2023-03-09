// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package ketai.camera;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.opengl.GLES20;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import processing.core.PApplet;
import processing.core.PImage;

public class KetaiCamera extends PImage
{

    public KetaiCamera(PApplet papplet, int i, int j, int k)
    {
        super(i, j, 2);
        savePhotoPath = "";
        listeners = new Vector();
        SAVE_DIR = "";
        available = false;
        supportsFaceDetection = false;
        requestedPortraitImage = false;
        lastProcessedFrame = 0;
        previewcallback = new android.hardware.Camera.PreviewCallback() {

            public void onPreviewFrame(byte abyte0[], Camera camera1)
            {
                if(camera1 != null && isStarted) goto _L2; else goto _L1
_L1:
                return;
_L2:
                if(myPixels == null || myPixels.length != frameWidth * frameHeight)
                    myPixels = new int[frameWidth * frameHeight];
                decodeYUV420SP(abyte0);
                if(parent.millis() - lastProcessedFrame >= 1000 / cameraFPS)
                {
                    lastProcessedFrame = parent.millis();
                    if(onPreviewEventMethod != null && myPixels != null)
                        try
                        {
                            onPreviewEventMethod.invoke(callbackdelegate, new Object[0]);
                        }
                        // Misplaced declaration of an exception variable
                        catch(byte abyte0[])
                        {
                            PApplet.println((new StringBuilder(" onCameraPreviewEvent() had  an error:")).append(abyte0.getMessage()).toString());
                            abyte0.printStackTrace();
                        }
                    if(onPreviewEventMethodPImage != null && myPixels != null)
                        try
                        {
                            onPreviewEventMethodPImage.invoke(callbackdelegate, new Object[] {
                                self
                            });
                        }
                        // Misplaced declaration of an exception variable
                        catch(byte abyte0[])
                        {
                            PApplet.println((new StringBuilder("Disabling onCameraPreviewEvent(KetaiCamera) because of an error:")).append(abyte0.getMessage()).toString());
                            abyte0.printStackTrace();
                            onPreviewEventMethodPImage = null;
                        }
                    abyte0 = listeners.iterator();
                    while(abyte0.hasNext()) 
                    {
                        camera1 = (Method)abyte0.next();
                        try
                        {
                            camera1.invoke(callbackdelegate, new Object[] {
                                self
                            });
                        }
                        // Misplaced declaration of an exception variable
                        catch(Camera camera1)
                        {
                            PApplet.println((new StringBuilder("Disabling onCameraPreviewEvent(KetaiCamera) because of an error:")).append(camera1.getMessage()).toString());
                            camera1.printStackTrace();
                        }
                    }
                }
                if(true) goto _L1; else goto _L3
_L3:
            }

            final KetaiCamera this$0;

            
            {
                this$0 = KetaiCamera.this;
                super();
            }
        }
;
        autofocusCB = new android.hardware.Camera.AutoFocusCallback() {

            public void onAutoFocus(boolean flag, Camera camera1)
            {
                PApplet.println((new StringBuilder("Autofocus result: ")).append(flag).toString());
            }

            final KetaiCamera this$0;

            
            {
                this$0 = KetaiCamera.this;
                super();
            }
        }
;
        jpegCallback = new android.hardware.Camera.PictureCallback() {

            public void onPictureTaken(byte abyte0[], Camera camera1)
            {
                PApplet.println("pictureCallback entered...");
                if(camera1 != null) goto _L2; else goto _L1
_L1:
                return;
_L2:
                Object obj;
                obj = JVM INSTR new #40  <Class StringBuilder>;
                ((StringBuilder) (obj)).StringBuilder("Saving image: ");
                PApplet.println(((StringBuilder) (obj)).append(savePhotoPath).toString());
                obj = JVM INSTR new #58  <Class FileOutputStream>;
                ((FileOutputStream) (obj)).FileOutputStream(savePhotoPath);
                ((FileOutputStream) (obj)).write(abyte0);
                ((FileOutputStream) (obj)).close();
                if(onSavePhotoEventMethod == null || myPixels == null)
                    break MISSING_BLOCK_LABEL_125;
                abyte0 = savePhotoPath;
                if(abyte0 == null)
                    break MISSING_BLOCK_LABEL_125;
                onSavePhotoEventMethod.invoke(parent, new Object[] {
                    savePhotoPath
                });
_L3:
                camera1.startPreview();
                  goto _L1
                abyte0;
                abyte0.printStackTrace();
                  goto _L3
                abyte0;
_L5:
                abyte0.printStackTrace();
                  goto _L1
                abyte0;
                abyte0.printStackTrace();
                  goto _L3
                abyte0;
_L4:
                abyte0.printStackTrace();
                  goto _L1
                abyte0;
                  goto _L1
                abyte0;
                  goto _L1
                abyte0;
                  goto _L4
                abyte0;
                  goto _L5
            }

            final KetaiCamera this$0;

            
            {
                this$0 = KetaiCamera.this;
                super();
            }
        }
;
        myScannerCallback = new android.media.MediaScannerConnection.OnScanCompletedListener() {

            public void onScanCompleted(String s, Uri uri)
            {
                PApplet.println((new StringBuilder("Media Scanner returned: ")).append(uri.toString()).append(" => ").append(s).toString());
            }

            final KetaiCamera this$0;

            
            {
                this$0 = KetaiCamera.this;
                super();
            }
        }
;
        parent = papplet;
        parent.requestPermission("android.permission.CAMERA", "onPermissionResult", this);
        bitmap = Bitmap.createBitmap(pixels, width, height, android.graphics.Bitmap.Config.ARGB_8888);
        frameWidth = i;
        frameHeight = j;
        photoWidth = frameWidth;
        photoHeight = frameHeight;
        cameraFPS = k;
        isStarted = false;
        requestedStart = false;
        myPixels = new int[i * j];
        self = this;
        isRGBPreviewSupported = false;
        enableFlash = false;
        cameraID = 0;
        callbackdelegate = parent;
        determineObjectIntentions(papplet);
        PackageManager packagemanager = parent.getActivity().getApplicationContext().getPackageManager();
        try
        {
            papplet = packagemanager.getApplicationInfo(parent.getActivity().getApplicationContext().getPackageName(), 0);
        }
        // Misplaced declaration of an exception variable
        catch(PApplet papplet)
        {
            papplet = null;
        }
        if(papplet != null)
            papplet = packagemanager.getApplicationLabel(papplet);
        else
            papplet = "unknownApp";
        SAVE_DIR = (String)papplet;
        parent.registerMethod("resume", this);
        parent.registerMethod("pause", this);
        parent.registerMethod("dispose", this);
        read();
    }

    private void determineCameraParameters()
    {
        if(camera != null) goto _L2; else goto _L1
_L1:
        return;
_L2:
        android.hardware.Camera.Parameters parameters;
        int i;
        Object obj;
        Iterator iterator;
        PApplet.println((new StringBuilder("Requested camera parameters as (w,h,fps):")).append(frameWidth).append(",").append(frameHeight).append(",").append(cameraFPS).toString());
        parameters = camera.getParameters();
        List list1 = parameters.getSupportedPreviewSizes();
        i = 0;
        obj = null;
        iterator = list1.iterator();
_L7:
        if(iterator.hasNext()) goto _L4; else goto _L3
_L3:
        Object obj1;
        if(obj != null)
        {
            frameWidth = ((android.hardware.Camera.Size) (obj)).width;
            frameHeight = ((android.hardware.Camera.Size) (obj)).height;
        }
        parameters.setPreviewSize(frameWidth, frameHeight);
        List list2 = parameters.getSupportedPictureSizes();
        i = 0;
        obj = null;
        obj1 = list2.iterator();
_L8:
        if(((Iterator) (obj1)).hasNext()) goto _L6; else goto _L5
_L5:
        if(obj != null)
        {
            photoWidth = ((android.hardware.Camera.Size) (obj)).width;
            photoHeight = ((android.hardware.Camera.Size) (obj)).height;
        }
        parameters.setPictureSize(photoWidth, photoHeight);
        obj = parameters.getSupportedPreviewFrameRates();
        i = 0;
        obj = ((List) (obj)).iterator();
_L9:
        if(((Iterator) (obj)).hasNext())
            break MISSING_BLOCK_LABEL_646;
        PApplet.println((new StringBuilder("calculated preview FPS: ")).append(i).toString());
        parameters.setPreviewFrameRate(i);
        camera.setParameters(parameters);
        obj = camera.getParameters();
        frameHeight = ((android.hardware.Camera.Parameters) (obj)).getPreviewSize().height;
        frameWidth = ((android.hardware.Camera.Parameters) (obj)).getPreviewSize().width;
        if(cameraFPS == ((android.hardware.Camera.Parameters) (obj)).getPreviewFrameRate())
            cameraFPS = ((android.hardware.Camera.Parameters) (obj)).getPreviewFrameRate();
        PApplet.println((new StringBuilder("Calculated camera parameters as (w,h,fps):")).append(frameWidth).append(",").append(frameHeight).append(",").append(cameraFPS).toString());
        if(((android.hardware.Camera.Parameters) (obj)).getMaxNumDetectedFaces() > 0)
        {
            PApplet.println("Face detection supported!");
            supportsFaceDetection = true;
        }
        loadPixels();
        resize(frameWidth, frameHeight);
          goto _L1
_L4:
        android.hardware.Camera.Size size = (android.hardware.Camera.Size)iterator.next();
        PApplet.println((new StringBuilder("Checking supported preview size:")).append(size.width).append(",").append(size.height).toString());
        obj1 = obj;
        if(obj == null)
            obj1 = size;
        obj = obj1;
        if(i == 0)
            if(size.width == frameWidth && size.height == frameHeight)
            {
                PApplet.println("Found matching camera size");
                obj = size;
                i = 1;
            } else
            {
                int j = frameWidth;
                int k = frameHeight;
                int l = ((android.hardware.Camera.Size) (obj1)).height;
                int i1 = ((android.hardware.Camera.Size) (obj1)).width;
                int j1 = frameWidth;
                int l1 = frameHeight;
                int j2 = size.height;
                int k2 = size.width;
                k = Math.abs(j * k - l * i1);
                obj = obj1;
                if(Math.abs(j1 * l1 - j2 * k2) < k)
                    obj = size;
            }
          goto _L7
_L6:
        android.hardware.Camera.Size size1 = (android.hardware.Camera.Size)((Iterator) (obj1)).next();
        if(i == 0)
            if(size1.width == photoWidth && size1.height == photoHeight)
            {
                obj = size1;
                i = 1;
            } else
            if(photoWidth <= size1.width)
                obj = size1;
          goto _L8
        int i2 = ((Integer)((Iterator) (obj)).next()).intValue();
        PApplet.println((new StringBuilder("Supported preview FPS: ")).append(i2).toString());
        int k1 = i;
        if(i == 0)
            k1 = i2;
        i = k1;
        if(Math.abs(cameraFPS - i2) > Math.abs(cameraFPS - k1))
            i = i2;
          goto _L9
    }

    private void determineObjectIntentions(Object obj)
    {
        try
        {
            onPreviewEventMethod = obj.getClass().getMethod("onCameraPreviewEvent", new Class[0]);
            PApplet.println("Found onCameraPreviewEvent ");
        }
        catch(NoSuchMethodException nosuchmethodexception)
        {
            onPreviewEventMethod = null;
        }
        try
        {
            onPreviewEventMethodPImage = obj.getClass().getMethod("onCameraPreviewEvent", new Class[] {
                ketai/camera/KetaiCamera
            });
        }
        catch(NoSuchMethodException nosuchmethodexception1)
        {
            onPreviewEventMethodPImage = null;
        }
        try
        {
            onFaceDetectionEventMethod = obj.getClass().getMethod("onFaceDetectionEvent", new Class[] {
                [Lketai/camera/KetaiFace;
            });
        }
        catch(NoSuchMethodException nosuchmethodexception2)
        {
            onFaceDetectionEventMethod = null;
        }
        onSavePhotoEventMethod = obj.getClass().getMethod("onSavePhotoEvent", new Class[] {
            java/lang/String
        });
_L1:
        return;
        obj;
        onSavePhotoEventMethod = null;
          goto _L1
    }

    public void addToMediaLibrary(String s)
    {
        Context context = parent.getActivity().getApplicationContext();
        android.media.MediaScannerConnection.OnScanCompletedListener onscancompletedlistener = myScannerCallback;
        MediaScannerConnection.scanFile(context, new String[] {
            s
        }, null, onscancompletedlistener);
    }

    public void autoSettings()
    {
        if(camera != null) goto _L2; else goto _L1
_L1:
        return;
_L2:
        android.hardware.Camera.Parameters parameters = camera.getParameters();
        if(parameters.isAutoExposureLockSupported())
            parameters.setAutoExposureLock(false);
        if(parameters.isAutoWhiteBalanceLockSupported())
            parameters.setAutoWhiteBalanceLock(false);
        Iterator iterator = parameters.getSupportedFocusModes().iterator();
        do
        {
label0:
            {
                if(iterator.hasNext())
                    break label0;
                camera.setParameters(parameters);
                camera.autoFocus(autofocusCB);
            }
            if(true)
                continue;
            if(((String)iterator.next()).equalsIgnoreCase("continuous-picture"))
                parameters.setFocusMode("continuous-picture");
        } while(true);
        if(true) goto _L1; else goto _L3
_L3:
    }

    public void decodeYUV420SP(byte abyte0[])
    {
        int i;
        int j;
        int k;
        int l;
        i = width;
        j = height;
        k = 0;
        l = 0;
_L2:
        if(k >= height)
            return;
        int i1 = i * j + (k >> 1) * width;
        int j1 = 0;
        int k1 = 0;
        int l1 = 0;
        do
        {
label0:
            {
                if(l1 < width)
                    break label0;
                k++;
            }
            if(true)
                continue;
            int i2 = (abyte0[l] & 0xff) - 16;
            int j2 = i2;
            if(i2 < 0)
                j2 = 0;
            int k2 = i1;
            if((l1 & 1) == 0)
            {
                i2 = i1 + 1;
                k1 = (abyte0[i1] & 0xff) - 128;
                k2 = i2 + 1;
                j1 = (abyte0[i2] & 0xff) - 128;
            }
            i1 = j2 * 1192;
            j2 = i1 + k1 * 1634;
            i2 = i1 - k1 * 833 - j1 * 400;
            int l2 = i1 + j1 * 2066;
            if(j2 < 0)
            {
                i1 = 0;
            } else
            {
                i1 = j2;
                if(j2 > 0x3ffff)
                    i1 = 0x3ffff;
            }
            if(i2 < 0)
            {
                j2 = 0;
            } else
            {
                j2 = i2;
                if(i2 > 0x3ffff)
                    j2 = 0x3ffff;
            }
            if(l2 < 0)
            {
                i2 = 0;
            } else
            {
                i2 = l2;
                if(l2 > 0x3ffff)
                    i2 = 0x3ffff;
            }
            myPixels[l] = 0xff000000 | i1 << 6 & 0xff0000 | j2 >> 2 & 0xff00 | i2 >> 10 & 0xff;
            l1++;
            l++;
            i1 = k2;
        } while(true);
        if(true) goto _L2; else goto _L1
_L1:
    }

    public void disableFlash()
    {
        enableFlash = false;
        if(camera != null)
        {
            android.hardware.Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode("off");
            try
            {
                camera.setParameters(parameters);
            }
            catch(RuntimeException runtimeexception) { }
        }
    }

    public void dispose()
    {
        stop();
    }

    public String dump()
    {
        if(camera != null) goto _L2; else goto _L1
_L1:
        String s = "";
_L4:
        return s;
_L2:
        float af[];
        String s1;
        int i;
        android.hardware.Camera.Parameters parameters = camera.getParameters();
        s = (new StringBuilder(String.valueOf((new StringBuilder(String.valueOf(""))).append("Zoom: ").append(parameters.getZoom()).append("\n").toString()))).append("White Balance: ").append(parameters.getWhiteBalance()).append("\n").toString();
        if(parameters.isAutoWhiteBalanceLockSupported())
            s = (new StringBuilder(String.valueOf(s))).append("\t Lock supported, state: ").append(parameters.getAutoWhiteBalanceLock()).append("\n").toString();
        else
            s = (new StringBuilder(String.valueOf(s))).append("\t Lock NOT supported\n").toString();
        af = new float[3];
        s1 = "";
        parameters.getFocusDistances(af);
        i = 0;
_L5:
label0:
        {
            if(i < af.length)
                break label0;
            s = (new StringBuilder(String.valueOf((new StringBuilder(String.valueOf((new StringBuilder(String.valueOf((new StringBuilder(String.valueOf(s))).append("Focal Distances: ").append(s1).append(" \n").toString()))).append("Focal Depth: ").append(parameters.getFocalLength()).append("\n").toString()))).append("Focus Mode: ").append(parameters.getFocusMode()).append("\n").toString()))).append("Exposure: ").append(parameters.getExposureCompensation()).append("\n").toString();
            if(parameters.isAutoExposureLockSupported())
                s = (new StringBuilder(String.valueOf(s))).append("\t Lock supported, state: ").append(parameters.getAutoExposureLock()).append("\n").toString();
            else
                s = (new StringBuilder(String.valueOf(s))).append("\t Lock NOT supported\n").toString();
            s = (new StringBuilder(String.valueOf(s))).append("Native camera face detection support: ").append(supportsFaceDetection).toString();
        }
        if(true) goto _L4; else goto _L3
_L3:
        s1 = (new StringBuilder(String.valueOf(s1))).append(String.valueOf(af[i])).append(" ").toString();
        i++;
          goto _L5
    }

    public void enableFlash()
    {
        enableFlash = true;
        if(camera != null)
        {
            android.hardware.Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode("torch");
            try
            {
                camera.setParameters(parameters);
            }
            catch(RuntimeException runtimeexception) { }
        }
    }

    public int getCameraID()
    {
        return cameraID;
    }

    public int getNumberOfCameras()
    {
        return Camera.getNumberOfCameras();
    }

    public int getPhotoHeight()
    {
        return photoHeight;
    }

    public int getPhotoWidth()
    {
        return photoWidth;
    }

    public int getZoom()
    {
        int i;
        if(camera == null)
            i = 0;
        else
            i = camera.getParameters().getZoom();
        return i;
    }

    public boolean isFlashEnabled()
    {
        return enableFlash;
    }

    public boolean isStarted()
    {
        return isStarted;
    }

    public Collection list()
    {
        Vector vector = new Vector();
        int i = Camera.getNumberOfCameras();
        int j = 0;
        do
        {
            if(j >= i)
                return vector;
            Object obj = new android.hardware.Camera.CameraInfo();
            Camera.getCameraInfo(j, ((android.hardware.Camera.CameraInfo) (obj)));
            if(((android.hardware.Camera.CameraInfo) (obj)).facing == 0)
                obj = "backfacing";
            else
                obj = "frontfacing";
            vector.add((new StringBuilder("camera id [")).append(j).append("] facing:").append(((String) (obj))).toString());
            PApplet.println((new StringBuilder("camera id[")).append(j).append("] facing:").append(((String) (obj))).toString());
            j++;
        } while(true);
    }

    public void manualSettings()
    {
        if(camera != null) goto _L2; else goto _L1
_L1:
        return;
_L2:
        Object obj;
        obj = camera.getParameters();
        if(((android.hardware.Camera.Parameters) (obj)).isAutoExposureLockSupported())
            ((android.hardware.Camera.Parameters) (obj)).setAutoExposureLock(true);
        if(!((android.hardware.Camera.Parameters) (obj)).isAutoWhiteBalanceLockSupported()) goto _L4; else goto _L3
_L3:
        ((android.hardware.Camera.Parameters) (obj)).setAutoWhiteBalanceLock(true);
_L6:
        Iterator iterator = ((android.hardware.Camera.Parameters) (obj)).getSupportedFocusModes().iterator();
_L9:
        if(iterator.hasNext())
            break MISSING_BLOCK_LABEL_144;
        try
        {
            camera.setParameters(((android.hardware.Camera.Parameters) (obj)));
        }
        // Misplaced declaration of an exception variable
        catch(Object obj)
        {
            PApplet.println((new StringBuilder("Failed to set parameters to manual.")).append(((RuntimeException) (obj)).getMessage()).toString());
        }
          goto _L1
_L4:
        iterator = ((android.hardware.Camera.Parameters) (obj)).getSupportedWhiteBalance().iterator();
_L8:
        if(!iterator.hasNext()) goto _L6; else goto _L5
_L5:
        String s = (String)iterator.next();
        if(!s.equalsIgnoreCase("cloudy-daylight")) goto _L8; else goto _L7
_L7:
        ((android.hardware.Camera.Parameters) (obj)).setWhiteBalance(s);
          goto _L6
        if(((String)iterator.next()).equalsIgnoreCase("fixed"))
            ((android.hardware.Camera.Parameters) (obj)).setFocusMode("fixed");
          goto _L9
    }

    public void onFrameAvailable(SurfaceTexture surfacetexture)
    {
        PApplet.print(".");
    }

    public void onPermissionResult(boolean flag)
    {
        if(!flag)
            PApplet.println("User did not grant camera permission.  Camera is disabled.");
    }

    public void pause()
    {
        if(camera != null && isStarted)
        {
            isStarted = false;
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
        }
        isStarted = false;
    }

    public void read()
    {
        this;
        JVM INSTR monitorenter ;
        if(pixels.length != frameWidth * frameHeight)
            pixels = new int[frameWidth * frameHeight];
        synchronized(pixels)
        {
            System.arraycopy(myPixels, 0, pixels, 0, frameWidth * frameHeight);
            available = false;
            updatePixels();
        }
        this;
        JVM INSTR monitorexit ;
        return;
        exception1;
        ai;
        JVM INSTR monitorexit ;
        throw exception1;
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
    }

    public void register(Object obj)
    {
        callbackdelegate = obj;
        determineObjectIntentions(obj);
    }

    public void resume()
    {
        if(camera != null) goto _L2; else goto _L1
_L1:
        return;
_L2:
        camera = Camera.open(cameraID);
        if(!isStarted && requestedStart)
            start();
        if(true) goto _L1; else goto _L3
_L3:
    }

    public boolean savePhoto()
    {
        boolean flag;
        if(camera != null && isStarted())
        {
            savePhotoPath = "";
            flag = savePhoto(savePhotoPath);
        } else
        {
            flag = false;
        }
        return flag;
    }

    public boolean savePhoto(String s)
    {
        boolean flag = false;
        if(!s.startsWith(File.separator)) goto _L2; else goto _L1
_L1:
        savePhotoPath = s;
_L4:
        PApplet.println((new StringBuilder("Calculated photo path: ")).append(savePhotoPath).toString());
        s = JVM INSTR new #610 <Class FileOutputStream>;
        s.FileOutputStream(savePhotoPath);
        s.write(1);
        s.close();
        s = JVM INSTR new #600 <Class File>;
        s.File(savePhotoPath);
        if(!s.delete())
            PApplet.println("Failed to remove temp photoFile while testing permissions..oops");
        if(camera != null && isStarted())
            camera.takePicture(null, null, jpegCallback);
        flag = true;
_L3:
        return flag;
_L2:
        File file;
label0:
        {
            if(s.equalsIgnoreCase(""))
            {
                s = (new SimpleDateFormat("yyyyMMdd_HHmmss")).format(new Date());
                s = (new StringBuilder("IMG_")).append(s).append(".jpg").toString();
            }
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), SAVE_DIR);
            if(file.exists() || file.mkdirs())
                break label0;
            PApplet.println((new StringBuilder("failed to create directory to save photo: ")).append(file.getAbsolutePath()).toString());
        }
          goto _L3
        savePhotoPath = (new StringBuilder(String.valueOf(file.getAbsolutePath()))).append(File.separator).append(s).toString();
          goto _L4
        s;
        PApplet.println((new StringBuilder("Failed to save photo to ")).append(savePhotoPath).append("\n").append(s.getMessage()).toString());
          goto _L3
        s;
        PApplet.println((new StringBuilder("Failed to save photo to ")).append(savePhotoPath).append("\n").append(s.getMessage()).toString());
          goto _L3
    }

    public void setCameraID(int i)
    {
        boolean flag = isStarted;
        if(i < Camera.getNumberOfCameras() && cameraID != i)
        {
            if(flag)
                stop();
            cameraID = i;
        }
        if(flag)
            start();
    }

    public void setPhotoSize(int i, int j)
    {
        photoWidth = i;
        photoHeight = j;
        determineCameraParameters();
    }

    public void setSaveDirectory(String s)
    {
        SAVE_DIR = s;
    }

    public void setZoom(int i)
    {
        if(camera != null) goto _L2; else goto _L1
_L1:
        return;
_L2:
        android.hardware.Camera.Parameters parameters;
        int j;
        parameters = camera.getParameters();
        if(i <= parameters.getMaxZoom())
            break; /* Loop/switch isn't completed */
        j = parameters.getMaxZoom();
_L4:
        parameters.setZoom(j);
        camera.setParameters(parameters);
        if(true) goto _L1; else goto _L3
_L3:
        j = i;
        if(i < 0)
            j = 0;
          goto _L4
        if(true) goto _L1; else goto _L5
_L5:
    }

    public boolean start()
    {
        requestedStart = true;
        if(!isStarted) goto _L2; else goto _L1
_L1:
        boolean flag = true;
_L24:
        return flag;
_L2:
        Object obj = camera;
        if(obj != null)
            break MISSING_BLOCK_LABEL_36;
        camera = Camera.open(cameraID);
        Iterator iterator;
        obj = camera.getParameters();
        iterator = ((android.hardware.Camera.Parameters) (obj)).getSupportedPreviewFormats().iterator();
_L25:
        if(iterator.hasNext()) goto _L4; else goto _L3
_L3:
        Object obj1;
        if(isRGBPreviewSupported)
            ((android.hardware.Camera.Parameters) (obj)).setPreviewFormat(4);
        obj1 = JVM INSTR new #237 <Class StringBuilder>;
        ((StringBuilder) (obj1)).StringBuilder("default imageformat:");
        PApplet.println(((StringBuilder) (obj1)).append(((android.hardware.Camera.Parameters) (obj)).getPreviewFormat()).toString());
        obj1 = ((android.hardware.Camera.Parameters) (obj)).getSupportedFlashModes();
        if(obj1 == null) goto _L6; else goto _L5
_L5:
        if(((List) (obj1)).size() <= 0) goto _L6; else goto _L7
_L7:
        obj1 = ((List) (obj1)).iterator();
_L26:
        if(((Iterator) (obj1)).hasNext()) goto _L9; else goto _L8
_L8:
        if(!enableFlash) goto _L11; else goto _L10
_L10:
        ((android.hardware.Camera.Parameters) (obj)).setFlashMode("torch");
_L27:
        int i;
        obj1 = JVM INSTR new #513 <Class android.hardware.Camera$CameraInfo>;
        ((android.hardware.Camera.CameraInfo) (obj1)).android.hardware.Camera.CameraInfo();
        Camera.getCameraInfo(cameraID, ((android.hardware.Camera.CameraInfo) (obj1)));
        i = parent.getActivity().getWindowManager().getDefaultDisplay().getRotation();
        int j = 0;
        i;
        JVM INSTR tableswitch 0 3: default 232
    //                   0 753
    //                   1 759
    //                   2 766
    //                   3 774;
           goto _L12 _L13 _L14 _L15 _L16
_L12:
        i = 0;
        ((android.hardware.Camera.CameraInfo) (obj1)).orientation;
        JVM INSTR tableswitch 0 3: default 272
    //                   0 782
    //                   1 788
    //                   2 795
    //                   3 803;
           goto _L17 _L18 _L19 _L20 _L21
_L17:
        if(((android.hardware.Camera.CameraInfo) (obj1)).facing != 1) goto _L23; else goto _L22
_L22:
        requestedPortraitImage = true;
        i = (360 - (i + j) % 360) % 360;
_L28:
        camera.setDisplayOrientation(i);
        obj1 = JVM INSTR new #237 <Class StringBuilder>;
        ((StringBuilder) (obj1)).StringBuilder("Rotation reported: ");
        PApplet.println(((StringBuilder) (obj1)).append(j).toString());
        obj1 = JVM INSTR new #237 <Class StringBuilder>;
        ((StringBuilder) (obj1)).StringBuilder("camera: setting display orientation to: ");
        PApplet.println(((StringBuilder) (obj1)).append(i).append(" degrees").toString());
        camera.setDisplayOrientation(i);
        camera.setParameters(((android.hardware.Camera.Parameters) (obj)));
        camera.setPreviewCallback(previewcallback);
        determineCameraParameters();
        obj = parent.getActivity();
        obj1 = JVM INSTR new #14  <Class KetaiCamera$5>;
        ((_cls5) (obj1)).this. _cls5();
        ((Activity) (obj)).runOnUiThread(((Runnable) (obj1)));
        camera.startPreview();
_L29:
        isStarted = true;
        obj = JVM INSTR new #237 <Class StringBuilder>;
        ((StringBuilder) (obj)).StringBuilder("Using preview format: ");
        PApplet.println(((StringBuilder) (obj)).append(camera.getParameters().getPreviewFormat()).toString());
        obj = JVM INSTR new #237 <Class StringBuilder>;
        ((StringBuilder) (obj)).StringBuilder("Preview size: ");
        PApplet.println(((StringBuilder) (obj)).append(frameWidth).append("x").append(frameHeight).append(",").append(cameraFPS).toString());
        obj = JVM INSTR new #237 <Class StringBuilder>;
        ((StringBuilder) (obj)).StringBuilder("Photo size: ");
        PApplet.println(((StringBuilder) (obj)).append(photoWidth).append("x").append(photoHeight).toString());
        flag = true;
          goto _L24
        obj;
        obj1 = JVM INSTR new #237 <Class StringBuilder>;
        ((StringBuilder) (obj1)).StringBuilder("Failed to open camera for camera ID: ");
        PApplet.println(((StringBuilder) (obj1)).append(cameraID).append(":").append(((Exception) (obj)).getMessage()).toString());
        flag = false;
          goto _L24
_L4:
        obj1 = (Integer)iterator.next();
        if(((Integer) (obj1)).intValue() == 4)
            isRGBPreviewSupported = true;
        StringBuilder stringbuilder2 = JVM INSTR new #237 <Class StringBuilder>;
        stringbuilder2.StringBuilder("\t");
        PApplet.println(stringbuilder2.append(obj1).toString());
          goto _L25
        obj;
        ((RuntimeException) (obj)).printStackTrace();
        if(camera != null)
            camera.release();
        PApplet.println("Exception caught while trying to connect to camera service.  Please check your sketch permissions or that another application is not using the camera.");
        flag = false;
          goto _L24
_L9:
        String s = (String)((Iterator) (obj1)).next();
        StringBuilder stringbuilder = JVM INSTR new #237 <Class StringBuilder>;
        stringbuilder.StringBuilder("supported flashmode: ");
        PApplet.println(stringbuilder.append(s).toString());
          goto _L26
_L11:
        ((android.hardware.Camera.Parameters) (obj)).setFlashMode("off");
          goto _L27
_L6:
        PApplet.println("No flash support.");
          goto _L27
_L13:
        j = 0;
          goto _L12
_L14:
        j = 90;
          goto _L12
_L15:
        j = 180;
          goto _L12
_L16:
        j = 270;
          goto _L12
_L18:
        i = 0;
          goto _L17
_L19:
        i = 90;
          goto _L17
_L20:
        i = 180;
          goto _L17
_L21:
        i = 270;
          goto _L17
_L23:
        i = ((i - j) + 360) % 360;
          goto _L28
        NoClassDefFoundError noclassdeffounderror;
        noclassdeffounderror;
        camera.startPreview();
        StringBuilder stringbuilder1 = JVM INSTR new #237 <Class StringBuilder>;
        stringbuilder1.StringBuilder("Something bad happened trying to open the preview: ");
        PApplet.println(stringbuilder1.append(noclassdeffounderror.getMessage()).toString());
          goto _L29
    }

    public void stop()
    {
        PApplet.println("Stopping Camera...");
        requestedStart = false;
        if(camera != null && isStarted)
        {
            isStarted = false;
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
        }
    }

    String SAVE_DIR;
    private android.hardware.Camera.AutoFocusCallback autofocusCB;
    boolean available;
    public Object callbackdelegate;
    private Camera camera;
    private int cameraFPS;
    private int cameraID;
    public boolean enableFlash;
    private int frameHeight;
    private int frameWidth;
    public boolean isRGBPreviewSupported;
    public boolean isStarted;
    private android.hardware.Camera.PictureCallback jpegCallback;
    int lastProcessedFrame;
    private Vector listeners;
    SurfaceTexture mTexture;
    private int myPixels[];
    private android.media.MediaScannerConnection.OnScanCompletedListener myScannerCallback;
    protected Method onFaceDetectionEventMethod;
    protected Method onPreviewEventMethod;
    protected Method onPreviewEventMethodPImage;
    protected Method onSavePhotoEventMethod;
    private int photoHeight;
    private int photoWidth;
    android.hardware.Camera.PreviewCallback previewcallback;
    public boolean requestedPortraitImage;
    public boolean requestedStart;
    private String savePhotoPath;
    KetaiCamera self;
    boolean supportsFaceDetection;









    // Unreferenced inner class ketai/camera/KetaiCamera$5

/* anonymous class */
    class _cls5
        implements Runnable
    {

        public void run()
        {
            int ai[] = new int[1];
            GLES20.glGenTextures(1, ai, 0);
            GLES20.glBindTexture(3553, ai[0]);
            int i = ai[0];
            mTexture = new SurfaceTexture(i);
            camera.setPreviewTexture(mTexture);
_L1:
            return;
            IOException ioexception;
            ioexception;
            PApplet.println((new StringBuilder("Something bad happened trying set the texture when trying to open the preview: ")).append(ioexception.getMessage()).toString());
              goto _L1
        }

        final KetaiCamera this$0;

            
            {
                this$0 = KetaiCamera.this;
                super();
            }
    }

}
