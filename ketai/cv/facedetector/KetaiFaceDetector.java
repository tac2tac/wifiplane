// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package ketai.cv.facedetector;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.FaceDetector;
import java.util.ArrayList;
import ketai.camera.KetaiCamera;
import processing.core.PImage;

// Referenced classes of package ketai.cv.facedetector:
//            KetaiSimpleFace

public class KetaiFaceDetector
{

    public KetaiFaceDetector()
    {
    }

    public static KetaiSimpleFace[] findFaces(KetaiCamera ketaicamera)
    {
        return findFaces(ketaicamera, 5);
    }

    public static KetaiSimpleFace[] findFaces(KetaiCamera ketaicamera, int i)
    {
        ArrayList arraylist;
        boolean flag;
        int j;
        Object obj;
        arraylist = new ArrayList();
        flag = false;
        j = 0;
        ketaicamera.loadPixels();
        obj = Bitmap.createBitmap(ketaicamera.pixels, ketaicamera.width, ketaicamera.height, android.graphics.Bitmap.Config.RGB_565);
        if(!ketaicamera.requestedPortraitImage) goto _L2; else goto _L1
_L1:
        Bitmap bitmap;
        Matrix matrix = new Matrix();
        matrix.postRotate(90F);
        obj = Bitmap.createScaledBitmap(((Bitmap) (obj)), ketaicamera.width, ketaicamera.height, true);
        bitmap = Bitmap.createBitmap(((Bitmap) (obj)), 0, 0, ((Bitmap) (obj)).getWidth(), ((Bitmap) (obj)).getHeight(), matrix, true);
        if(bitmap == null) goto _L4; else goto _L3
_L3:
        android.media.FaceDetector.Face aface[];
        obj = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), i);
        aface = new android.media.FaceDetector.Face[i];
        j = ((FaceDetector) (obj)).findFaces(bitmap, aface);
        i = 0;
_L8:
        if(i < j) goto _L5; else goto _L4
_L4:
        ketaicamera = new KetaiSimpleFace[j];
        i = 0;
_L9:
        if(i < j) goto _L7; else goto _L6
_L6:
        return ketaicamera;
_L5:
        arraylist.add(new KetaiSimpleFace(aface[i], ketaicamera.width, ketaicamera.height));
        i++;
          goto _L8
_L7:
        ketaicamera[i] = (KetaiSimpleFace)arraylist.get(i);
        i++;
          goto _L9
_L2:
        j = ((flag) ? 1 : 0);
        if(obj == null) goto _L11; else goto _L10
_L10:
        FaceDetector facedetector = new FaceDetector(ketaicamera.width, ketaicamera.height, i);
        ketaicamera = new android.media.FaceDetector.Face[i];
        j = facedetector.findFaces(((Bitmap) (obj)), ketaicamera);
        i = 0;
_L14:
        if(i < j)
            break MISSING_BLOCK_LABEL_297;
_L11:
        KetaiSimpleFace aketaisimpleface[];
        aketaisimpleface = new KetaiSimpleFace[j];
        i = 0;
_L13:
        ketaicamera = aketaisimpleface;
        if(i >= j) goto _L6; else goto _L12
_L12:
        aketaisimpleface[i] = (KetaiSimpleFace)arraylist.get(i);
        i++;
          goto _L13
          goto _L6
        arraylist.add(new KetaiSimpleFace(ketaicamera[i]));
        i++;
          goto _L14
    }

    public static KetaiSimpleFace[] findFaces(PImage pimage, int i)
    {
        ArrayList arraylist;
        int j;
        Bitmap bitmap;
        arraylist = new ArrayList();
        j = 0;
        pimage.loadPixels();
        bitmap = Bitmap.createBitmap(pimage.pixels, pimage.width, pimage.height, android.graphics.Bitmap.Config.RGB_565);
        if(bitmap == null) goto _L2; else goto _L1
_L1:
        android.media.FaceDetector.Face aface[];
        pimage = new FaceDetector(pimage.width, pimage.height, i);
        aface = new android.media.FaceDetector.Face[i];
        j = pimage.findFaces(bitmap, aface);
        i = 0;
_L4:
        if(i < j) goto _L3; else goto _L2
_L2:
        pimage = new KetaiSimpleFace[j];
        i = 0;
_L5:
        if(i >= j)
            return pimage;
        break MISSING_BLOCK_LABEL_114;
_L3:
        arraylist.add(new KetaiSimpleFace(aface[i]));
        i++;
          goto _L4
        pimage[i] = (KetaiSimpleFace)arraylist.get(i);
        i++;
          goto _L5
    }
}
