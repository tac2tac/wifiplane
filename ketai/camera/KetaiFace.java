// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package ketai.camera;

import android.graphics.Point;
import android.graphics.Rect;
import processing.core.PApplet;
import processing.core.PVector;

public class KetaiFace extends android.hardware.Camera.Face
{

    public KetaiFace(android.hardware.Camera.Face face, int i, int j)
    {
        leftEye = new PVector(PApplet.map(face.leftEye.x, -1000F, 1000F, 0.0F, i), PApplet.map(face.leftEye.y, -1000F, 1000F, 0.0F, j));
        rightEye = new PVector(PApplet.map(face.rightEye.x, -1000F, 1000F, 0.0F, i), PApplet.map(face.rightEye.y, -1000F, 1000F, 0.0F, j));
        mouth = new PVector(PApplet.map(face.mouth.x, -1000F, 1000F, 0.0F, i), PApplet.map(face.mouth.y, -1000F, 1000F, 0.0F, j));
        id = face.id;
        score = face.score;
        center = new PVector(PApplet.map(face.rect.exactCenterX(), -1000F, 1000F, 0.0F, i), PApplet.map(face.rect.exactCenterY(), -1000F, 1000F, 0.0F, j));
        width = face.rect.width();
        height = face.rect.height();
    }

    public PVector center;
    public int height;
    public int id;
    public PVector leftEye;
    public PVector mouth;
    public PVector rightEye;
    public int score;
    public int width;
}
