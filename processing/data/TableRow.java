// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.data;


public interface TableRow
{

    public abstract int getColumnCount();

    public abstract int getColumnType(int i);

    public abstract int getColumnType(String s);

    public abstract int[] getColumnTypes();

    public abstract double getDouble(int i);

    public abstract double getDouble(String s);

    public abstract float getFloat(int i);

    public abstract float getFloat(String s);

    public abstract int getInt(int i);

    public abstract int getInt(String s);

    public abstract long getLong(int i);

    public abstract long getLong(String s);

    public abstract String getString(int i);

    public abstract String getString(String s);

    public abstract void setDouble(int i, double d);

    public abstract void setDouble(String s, double d);

    public abstract void setFloat(int i, float f);

    public abstract void setFloat(String s, float f);

    public abstract void setInt(int i, int j);

    public abstract void setInt(String s, int i);

    public abstract void setLong(int i, long l);

    public abstract void setLong(String s, long l);

    public abstract void setString(int i, String s);

    public abstract void setString(String s, String s1);
}
