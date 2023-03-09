// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package ketai.sensors;

import processing.core.PApplet;

public class SensorQueue
{

    public SensorQueue()
    {
        queue = new Object[10];
    }

    void add(Object obj)
    {
        this;
        JVM INSTR monitorenter ;
        Object aobj[];
        int i;
        if(count == queue.length)
            queue = (Object[])PApplet.expand(((Object) (queue)));
        aobj = queue;
        i = count;
        count = i + 1;
        aobj[i] = obj;
        this;
        JVM INSTR monitorexit ;
        return;
        obj;
        throw obj;
    }

    boolean available()
    {
        this;
        JVM INSTR monitorenter ;
        int i = count;
        boolean flag;
        if(i != 0)
            flag = true;
        else
            flag = false;
        this;
        JVM INSTR monitorexit ;
        return flag;
        Exception exception;
        exception;
        throw exception;
    }

    Object remove()
    {
        this;
        JVM INSTR monitorenter ;
        if(offset == count)
        {
            RuntimeException runtimeexception = JVM INSTR new #35  <Class RuntimeException>;
            runtimeexception.RuntimeException("Sensor queue is empty.");
            throw runtimeexception;
        }
        break MISSING_BLOCK_LABEL_30;
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
        Object aobj[];
        int i;
        aobj = queue;
        i = offset;
        offset = i + 1;
        Object obj = aobj[i];
        if(offset == count)
        {
            offset = 0;
            count = 0;
        }
        this;
        JVM INSTR monitorexit ;
        return obj;
    }

    private int count;
    private int offset;
    private Object queue[];
}
