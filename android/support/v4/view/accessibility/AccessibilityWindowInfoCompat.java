// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.view.accessibility;

import android.graphics.Rect;

// Referenced classes of package android.support.v4.view.accessibility:
//            AccessibilityNodeInfoCompat, AccessibilityWindowInfoCompatApi21

public class AccessibilityWindowInfoCompat
{
    private static class AccessibilityWindowInfoApi21Impl extends AccessibilityWindowInfoStubImpl
    {

        public void getBoundsInScreen(Object obj, Rect rect)
        {
            AccessibilityWindowInfoCompatApi21.getBoundsInScreen(obj, rect);
        }

        public Object getChild(Object obj, int i)
        {
            return AccessibilityWindowInfoCompatApi21.getChild(obj, i);
        }

        public int getChildCount(Object obj)
        {
            return AccessibilityWindowInfoCompatApi21.getChildCount(obj);
        }

        public int getId(Object obj)
        {
            return AccessibilityWindowInfoCompatApi21.getId(obj);
        }

        public int getLayer(Object obj)
        {
            return AccessibilityWindowInfoCompatApi21.getLayer(obj);
        }

        public Object getParent(Object obj)
        {
            return AccessibilityWindowInfoCompatApi21.getParent(obj);
        }

        public Object getRoot(Object obj)
        {
            return AccessibilityWindowInfoCompatApi21.getRoot(obj);
        }

        public int getType(Object obj)
        {
            return AccessibilityWindowInfoCompatApi21.getType(obj);
        }

        public boolean isAccessibilityFocused(Object obj)
        {
            return AccessibilityWindowInfoCompatApi21.isAccessibilityFocused(obj);
        }

        public boolean isActive(Object obj)
        {
            return AccessibilityWindowInfoCompatApi21.isActive(obj);
        }

        public boolean isFocused(Object obj)
        {
            return AccessibilityWindowInfoCompatApi21.isFocused(obj);
        }

        public Object obtain()
        {
            return AccessibilityWindowInfoCompatApi21.obtain();
        }

        public Object obtain(Object obj)
        {
            return AccessibilityWindowInfoCompatApi21.obtain(obj);
        }

        public void recycle(Object obj)
        {
            AccessibilityWindowInfoCompatApi21.recycle(obj);
        }

        private AccessibilityWindowInfoApi21Impl()
        {
        }

    }

    private static interface AccessibilityWindowInfoImpl
    {

        public abstract void getBoundsInScreen(Object obj, Rect rect);

        public abstract Object getChild(Object obj, int i);

        public abstract int getChildCount(Object obj);

        public abstract int getId(Object obj);

        public abstract int getLayer(Object obj);

        public abstract Object getParent(Object obj);

        public abstract Object getRoot(Object obj);

        public abstract int getType(Object obj);

        public abstract boolean isAccessibilityFocused(Object obj);

        public abstract boolean isActive(Object obj);

        public abstract boolean isFocused(Object obj);

        public abstract Object obtain();

        public abstract Object obtain(Object obj);

        public abstract void recycle(Object obj);
    }

    private static class AccessibilityWindowInfoStubImpl
        implements AccessibilityWindowInfoImpl
    {

        public void getBoundsInScreen(Object obj, Rect rect)
        {
        }

        public Object getChild(Object obj, int i)
        {
            return null;
        }

        public int getChildCount(Object obj)
        {
            return 0;
        }

        public int getId(Object obj)
        {
            return -1;
        }

        public int getLayer(Object obj)
        {
            return -1;
        }

        public Object getParent(Object obj)
        {
            return null;
        }

        public Object getRoot(Object obj)
        {
            return null;
        }

        public int getType(Object obj)
        {
            return -1;
        }

        public boolean isAccessibilityFocused(Object obj)
        {
            return true;
        }

        public boolean isActive(Object obj)
        {
            return true;
        }

        public boolean isFocused(Object obj)
        {
            return true;
        }

        public Object obtain()
        {
            return null;
        }

        public Object obtain(Object obj)
        {
            return null;
        }

        public void recycle(Object obj)
        {
        }

        private AccessibilityWindowInfoStubImpl()
        {
        }

    }


    private AccessibilityWindowInfoCompat(Object obj)
    {
        mInfo = obj;
    }

    public static AccessibilityWindowInfoCompat obtain()
    {
        return wrapNonNullInstance(IMPL.obtain());
    }

    public static AccessibilityWindowInfoCompat obtain(AccessibilityWindowInfoCompat accessibilitywindowinfocompat)
    {
        return wrapNonNullInstance(IMPL.obtain(accessibilitywindowinfocompat.mInfo));
    }

    private static String typeToString(int i)
    {
        i;
        JVM INSTR tableswitch 1 4: default 32
    //                   1 37
    //                   2 43
    //                   3 49
    //                   4 55;
           goto _L1 _L2 _L3 _L4 _L5
_L1:
        String s = "<UNKNOWN>";
_L7:
        return s;
_L2:
        s = "TYPE_APPLICATION";
        continue; /* Loop/switch isn't completed */
_L3:
        s = "TYPE_INPUT_METHOD";
        continue; /* Loop/switch isn't completed */
_L4:
        s = "TYPE_SYSTEM";
        continue; /* Loop/switch isn't completed */
_L5:
        s = "TYPE_ACCESSIBILITY_OVERLAY";
        if(true) goto _L7; else goto _L6
_L6:
    }

    static AccessibilityWindowInfoCompat wrapNonNullInstance(Object obj)
    {
        if(obj != null)
            obj = new AccessibilityWindowInfoCompat(obj);
        else
            obj = null;
        return ((AccessibilityWindowInfoCompat) (obj));
    }

    public boolean equals(Object obj)
    {
        boolean flag = true;
        if(this != obj) goto _L2; else goto _L1
_L1:
        return flag;
_L2:
        if(obj == null)
            flag = false;
        else
        if(getClass() != obj.getClass())
        {
            flag = false;
        } else
        {
            obj = (AccessibilityWindowInfoCompat)obj;
            if(mInfo == null)
            {
                if(((AccessibilityWindowInfoCompat) (obj)).mInfo != null)
                    flag = false;
            } else
            if(!mInfo.equals(((AccessibilityWindowInfoCompat) (obj)).mInfo))
                flag = false;
        }
        if(true) goto _L1; else goto _L3
_L3:
    }

    public void getBoundsInScreen(Rect rect)
    {
        IMPL.getBoundsInScreen(mInfo, rect);
    }

    public AccessibilityWindowInfoCompat getChild(int i)
    {
        return wrapNonNullInstance(IMPL.getChild(mInfo, i));
    }

    public int getChildCount()
    {
        return IMPL.getChildCount(mInfo);
    }

    public int getId()
    {
        return IMPL.getId(mInfo);
    }

    public int getLayer()
    {
        return IMPL.getLayer(mInfo);
    }

    public AccessibilityWindowInfoCompat getParent()
    {
        return wrapNonNullInstance(IMPL.getParent(mInfo));
    }

    public AccessibilityNodeInfoCompat getRoot()
    {
        return AccessibilityNodeInfoCompat.wrapNonNullInstance(IMPL.getRoot(mInfo));
    }

    public int getType()
    {
        return IMPL.getType(mInfo);
    }

    public int hashCode()
    {
        int i;
        if(mInfo == null)
            i = 0;
        else
            i = mInfo.hashCode();
        return i;
    }

    public boolean isAccessibilityFocused()
    {
        return IMPL.isAccessibilityFocused(mInfo);
    }

    public boolean isActive()
    {
        return IMPL.isActive(mInfo);
    }

    public boolean isFocused()
    {
        return IMPL.isFocused(mInfo);
    }

    public void recycle()
    {
        IMPL.recycle(mInfo);
    }

    public String toString()
    {
        boolean flag = true;
        StringBuilder stringbuilder = new StringBuilder();
        Object obj = new Rect();
        getBoundsInScreen(((Rect) (obj)));
        stringbuilder.append("AccessibilityWindowInfo[");
        stringbuilder.append("id=").append(getId());
        stringbuilder.append(", type=").append(typeToString(getType()));
        stringbuilder.append(", layer=").append(getLayer());
        stringbuilder.append(", bounds=").append(obj);
        stringbuilder.append(", focused=").append(isFocused());
        stringbuilder.append(", active=").append(isActive());
        obj = stringbuilder.append(", hasParent=");
        boolean flag1;
        if(getParent() != null)
            flag1 = true;
        else
            flag1 = false;
        ((StringBuilder) (obj)).append(flag1);
        obj = stringbuilder.append(", hasChildren=");
        if(getChildCount() > 0)
            flag1 = flag;
        else
            flag1 = false;
        ((StringBuilder) (obj)).append(flag1);
        stringbuilder.append(']');
        return stringbuilder.toString();
    }

    private static final AccessibilityWindowInfoImpl IMPL;
    public static final int TYPE_ACCESSIBILITY_OVERLAY = 4;
    public static final int TYPE_APPLICATION = 1;
    public static final int TYPE_INPUT_METHOD = 2;
    public static final int TYPE_SYSTEM = 3;
    private static final int UNDEFINED = -1;
    private Object mInfo;

    static 
    {
        if(android.os.Build.VERSION.SDK_INT >= 21)
            IMPL = new AccessibilityWindowInfoApi21Impl();
        else
            IMPL = new AccessibilityWindowInfoStubImpl();
    }
}
