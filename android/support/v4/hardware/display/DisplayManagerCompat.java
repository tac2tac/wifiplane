// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.hardware.display;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;
import java.util.WeakHashMap;

// Referenced classes of package android.support.v4.hardware.display:
//            DisplayManagerJellybeanMr1

public abstract class DisplayManagerCompat
{
    private static class JellybeanMr1Impl extends DisplayManagerCompat
    {

        public Display getDisplay(int i)
        {
            return DisplayManagerJellybeanMr1.getDisplay(mDisplayManagerObj, i);
        }

        public Display[] getDisplays()
        {
            return DisplayManagerJellybeanMr1.getDisplays(mDisplayManagerObj);
        }

        public Display[] getDisplays(String s)
        {
            return DisplayManagerJellybeanMr1.getDisplays(mDisplayManagerObj, s);
        }

        private final Object mDisplayManagerObj;

        public JellybeanMr1Impl(Context context)
        {
            mDisplayManagerObj = DisplayManagerJellybeanMr1.getDisplayManager(context);
        }
    }

    private static class LegacyImpl extends DisplayManagerCompat
    {

        public Display getDisplay(int i)
        {
            Display display = mWindowManager.getDefaultDisplay();
            if(display.getDisplayId() != i)
                display = null;
            return display;
        }

        public Display[] getDisplays()
        {
            return (new Display[] {
                mWindowManager.getDefaultDisplay()
            });
        }

        public Display[] getDisplays(String s)
        {
            if(s == null)
                s = getDisplays();
            else
                s = new Display[0];
            return s;
        }

        private final WindowManager mWindowManager;

        public LegacyImpl(Context context)
        {
            mWindowManager = (WindowManager)context.getSystemService("window");
        }
    }


    DisplayManagerCompat()
    {
    }

    public static DisplayManagerCompat getInstance(Context context)
    {
        WeakHashMap weakhashmap = sInstances;
        weakhashmap;
        JVM INSTR monitorenter ;
        DisplayManagerCompat displaymanagercompat = (DisplayManagerCompat)sInstances.get(context);
        Object obj = displaymanagercompat;
        if(displaymanagercompat != null) goto _L2; else goto _L1
_L1:
        if(android.os.Build.VERSION.SDK_INT < 17)
            break MISSING_BLOCK_LABEL_53;
        obj = JVM INSTR new #6   <Class DisplayManagerCompat$JellybeanMr1Impl>;
        ((JellybeanMr1Impl) (obj)).JellybeanMr1Impl(context);
_L3:
        sInstances.put(context, obj);
_L2:
        weakhashmap;
        JVM INSTR monitorexit ;
        return ((DisplayManagerCompat) (obj));
        obj = new LegacyImpl(context);
          goto _L3
        context;
        weakhashmap;
        JVM INSTR monitorexit ;
        throw context;
    }

    public abstract Display getDisplay(int i);

    public abstract Display[] getDisplays();

    public abstract Display[] getDisplays(String s);

    public static final String DISPLAY_CATEGORY_PRESENTATION = "android.hardware.display.category.PRESENTATION";
    private static final WeakHashMap sInstances = new WeakHashMap();

}
