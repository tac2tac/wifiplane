// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package ketai.cv.facedetector;

import android.graphics.PointF;
import processing.core.PApplet;
import processing.core.PVector;

public class KetaiSimpleFace
{

    public KetaiSimpleFace(android.media.FaceDetector.Face face)
    {
        PointF pointf = new PointF();
        face.getMidPoint(pointf);
        location = new PVector(pointf.x, pointf.y);
        distance = face.eyesDistance();
    }

    public KetaiSimpleFace(android.media.FaceDetector.Face face, int i, int j)
    {
        PointF pointf = new PointF();
        face.getMidPoint(pointf);
        location = new PVector(pointf.y, PApplet.map(pointf.x, 0.0F, j, j, 0.0F));
        distance = face.eyesDistance();
    }

    public float confidence;
    public float distance;
    public PVector location;
}
