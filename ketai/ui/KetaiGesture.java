// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package ketai.ui;

import android.app.Activity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import java.lang.reflect.Method;
import java.util.HashMap;
import processing.core.PApplet;
import processing.core.PVector;
import processing.event.TouchEvent;

public class KetaiGesture
    implements android.view.GestureDetector.OnGestureListener, android.view.GestureDetector.OnDoubleTapListener
{

    public KetaiGesture(PApplet papplet)
    {
        cursors = new HashMap();
        pcursors = new HashMap();
        parent = papplet;
        me = this;
        parent.getActivity().runOnUiThread(new Runnable() {

            public void run()
            {
                gestures = new GestureDetector(parent.getActivity(), me);
            }

            final KetaiGesture this$0;

            
            {
                this$0 = KetaiGesture.this;
                super();
            }
        }
);
        parent.registerMethod("touchEvent", this);
        findParentIntentions();
    }

    private void analyse()
    {
        this;
        JVM INSTR monitorenter ;
        if(cursors.size() <= 1 || pcursors.size() <= 1) goto _L2; else goto _L1
_L1:
        PVector pvector;
        PVector pvector1;
        PVector pvector2;
        Object obj;
        pvector = (PVector)cursors.get(Integer.valueOf(0));
        pvector1 = (PVector)pcursors.get(Integer.valueOf(0));
        pvector2 = (PVector)cursors.get(Integer.valueOf(1));
        obj = (PVector)pcursors.get(Integer.valueOf(1));
        if(pvector != null && pvector2 != null && pvector1 != null && obj != null) goto _L3; else goto _L2
_L2:
        this;
        JVM INSTR monitorexit ;
        return;
_L3:
        float f;
        float f1;
        float f2;
        float f3;
        float f4;
        float f5;
        f = (pvector.x + pvector2.x) / 2.0F;
        f1 = (pvector.y + pvector2.y) / 2.0F;
        f2 = PApplet.dist(pvector1.x, pvector1.y, ((PVector) (obj)).x, ((PVector) (obj)).y);
        f3 = PApplet.dist(pvector.x, pvector.y, pvector2.x, pvector2.y);
        f4 = PApplet.atan2(PVector.sub(pvector1, ((PVector) (obj))).y, PVector.sub(pvector1, ((PVector) (obj))).x);
        f5 = PApplet.atan2(PVector.sub(pvector, pvector2).y, PVector.sub(pvector, pvector2).x);
        obj = onPinchMethod;
        if(obj == null)
            break MISSING_BLOCK_LABEL_273;
        Exception exception1;
        try
        {
            onPinchMethod.invoke(parent, new Object[] {
                Float.valueOf(f), Float.valueOf(f1), Float.valueOf(f3 - f2)
            });
        }
        catch(Exception exception2) { }
        obj = onRotateMethod;
        if(obj == null)
            continue; /* Loop/switch isn't completed */
        try
        {
            onRotateMethod.invoke(parent, new Object[] {
                Float.valueOf(f), Float.valueOf(f1), Float.valueOf(f5 - f4)
            });
        }
        catch(Exception exception) { }
        if(true) goto _L2; else goto _L4
_L4:
        exception1;
        throw exception1;
    }

    private void findParentIntentions()
    {
        Exception exception;
        try
        {
            onTapMethod = parent.getClass().getMethod("onTap", new Class[] {
                Float.TYPE, Float.TYPE
            });
        }
        catch(Exception exception6) { }
        try
        {
            onDoubleTapMethod = parent.getClass().getMethod("onDoubleTap", new Class[] {
                Float.TYPE, Float.TYPE
            });
        }
        catch(Exception exception5) { }
        try
        {
            onFlickMethod = parent.getClass().getMethod("onFlick", new Class[] {
                Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE
            });
        }
        catch(Exception exception4) { }
        try
        {
            onScrollMethod = parent.getClass().getMethod("onScroll", new Class[] {
                Integer.TYPE, Integer.TYPE
            });
        }
        catch(Exception exception3) { }
        try
        {
            onLongPressMethod = parent.getClass().getMethod("onLongPress", new Class[] {
                Float.TYPE, Float.TYPE
            });
        }
        catch(Exception exception2) { }
        try
        {
            onPinchMethod = parent.getClass().getMethod("onPinch", new Class[] {
                Float.TYPE, Float.TYPE, Float.TYPE
            });
        }
        catch(Exception exception1) { }
        onRotateMethod = parent.getClass().getMethod("onRotate", new Class[] {
            Float.TYPE, Float.TYPE, Float.TYPE
        });
_L2:
        return;
        exception;
        if(true) goto _L2; else goto _L1
_L1:
    }

    public boolean onDoubleTap(MotionEvent motionevent)
    {
        if(onDoubleTapMethod != null)
            try
            {
                onDoubleTapMethod.invoke(parent, new Object[] {
                    Float.valueOf(motionevent.getX()), Float.valueOf(motionevent.getY())
                });
            }
            // Misplaced declaration of an exception variable
            catch(MotionEvent motionevent) { }
        return true;
    }

    public boolean onDoubleTapEvent(MotionEvent motionevent)
    {
        return false;
    }

    public boolean onDown(MotionEvent motionevent)
    {
        return true;
    }

    public boolean onFling(MotionEvent motionevent, MotionEvent motionevent1, float f, float f1)
    {
        if(onFlickMethod != null)
            try
            {
                PVector pvector = JVM INSTR new #88  <Class PVector>;
                pvector.PVector(f, f1);
                onFlickMethod.invoke(parent, new Object[] {
                    Float.valueOf(motionevent1.getX()), Float.valueOf(motionevent1.getY()), Float.valueOf(motionevent.getX()), Float.valueOf(motionevent.getY()), Float.valueOf(pvector.mag())
                });
            }
            // Misplaced declaration of an exception variable
            catch(MotionEvent motionevent) { }
        return true;
    }

    public void onLongPress(MotionEvent motionevent)
    {
        if(onLongPressMethod == null)
            break MISSING_BLOCK_LABEL_43;
        onLongPressMethod.invoke(parent, new Object[] {
            Float.valueOf(motionevent.getX()), Float.valueOf(motionevent.getY())
        });
_L2:
        return;
        motionevent;
        if(true) goto _L2; else goto _L1
_L1:
    }

    public boolean onScroll(MotionEvent motionevent, MotionEvent motionevent1, float f, float f1)
    {
        return true;
    }

    public void onShowPress(MotionEvent motionevent)
    {
    }

    public boolean onSingleTapConfirmed(MotionEvent motionevent)
    {
        return false;
    }

    public boolean onSingleTapUp(MotionEvent motionevent)
    {
        if(onTapMethod != null)
            try
            {
                onTapMethod.invoke(parent, new Object[] {
                    Float.valueOf(motionevent.getX()), Float.valueOf(motionevent.getY())
                });
            }
            // Misplaced declaration of an exception variable
            catch(MotionEvent motionevent) { }
        return true;
    }

    public boolean surfaceTouchEvent(MotionEvent motionevent)
    {
        int i;
        int k;
        float f;
        float f2;
        i = motionevent.getAction() & 0xff;
        k = motionevent.getAction() >> 8;
        f = motionevent.getX(k);
        f2 = motionevent.getY(k);
        k = motionevent.getPointerId(k);
        if(i != 0 && i != 5) goto _L2; else goto _L1
_L1:
        cursors.put(Integer.valueOf(k), new PVector(f, f2));
_L4:
        analyse();
        parent.getActivity().onTouchEvent(motionevent);
        return gestures.onTouchEvent(motionevent);
_L2:
        if(i != 1 && i != 6)
            break; /* Loop/switch isn't completed */
        if(cursors.containsKey(Integer.valueOf(k)))
            cursors.remove(Integer.valueOf(k));
        if(pcursors.containsKey(Integer.valueOf(k)))
            pcursors.remove(Integer.valueOf(k));
        if(true) goto _L4; else goto _L3
_L3:
        if(i == 2)
        {
            int l = motionevent.getPointerCount();
            int j = 0;
            while(j < l) 
            {
                int i1 = motionevent.getPointerId(j);
                float f3 = motionevent.getX(j);
                float f1 = motionevent.getY(j);
                if(cursors.containsKey(Integer.valueOf(i1)))
                    pcursors.put(Integer.valueOf(i1), (PVector)cursors.get(Integer.valueOf(i1)));
                else
                    pcursors.put(Integer.valueOf(i1), new PVector(f3, f1));
                cursors.put(Integer.valueOf(i1), new PVector(f3, f1));
                j++;
            }
        }
        if(true) goto _L4; else goto _L5
_L5:
    }

    public void touchEvent(TouchEvent touchevent)
    {
        PApplet.println("motionEvent called inside kgesture");
        if(touchevent.getNative() instanceof MotionEvent)
        {
            PApplet.println("KGesture got a MotionEvent!");
            surfaceTouchEvent((MotionEvent)touchevent.getNative());
        }
    }

    HashMap cursors;
    GestureDetector gestures;
    KetaiGesture me;
    Method onDoubleTapMethod;
    Method onFlickMethod;
    Method onLongPressMethod;
    Method onPinchMethod;
    Method onRotateMethod;
    Method onScrollMethod;
    Method onTapMethod;
    PApplet parent;
    HashMap pcursors;
}
