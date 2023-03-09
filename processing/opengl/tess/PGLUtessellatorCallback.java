// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.opengl.tess;


public interface PGLUtessellatorCallback
{

    public abstract void begin(int i);

    public abstract void beginData(int i, Object obj);

    public abstract void combine(double ad[], Object aobj[], float af[], Object aobj1[]);

    public abstract void combineData(double ad[], Object aobj[], float af[], Object aobj1[], Object obj);

    public abstract void edgeFlag(boolean flag);

    public abstract void edgeFlagData(boolean flag, Object obj);

    public abstract void end();

    public abstract void endData(Object obj);

    public abstract void error(int i);

    public abstract void errorData(int i, Object obj);

    public abstract void vertex(Object obj);

    public abstract void vertexData(Object obj, Object obj1);
}
