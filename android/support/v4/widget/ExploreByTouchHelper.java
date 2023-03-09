// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.*;
import android.support.v4.view.accessibility.*;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import java.util.*;

public abstract class ExploreByTouchHelper extends AccessibilityDelegateCompat
{
    private class ExploreByTouchNodeProvider extends AccessibilityNodeProviderCompat
    {

        public AccessibilityNodeInfoCompat createAccessibilityNodeInfo(int i)
        {
            return createNode(i);
        }

        public boolean performAction(int i, int j, Bundle bundle)
        {
            return ExploreByTouchHelper.this.performAction(i, j, bundle);
        }

        final ExploreByTouchHelper this$0;

        private ExploreByTouchNodeProvider()
        {
            this$0 = ExploreByTouchHelper.this;
            super();
        }

    }


    public ExploreByTouchHelper(View view)
    {
        mFocusedVirtualViewId = 0x80000000;
        mHoveredVirtualViewId = 0x80000000;
        if(view == null)
        {
            throw new IllegalArgumentException("View may not be null");
        } else
        {
            mView = view;
            mManager = (AccessibilityManager)view.getContext().getSystemService("accessibility");
            return;
        }
    }

    private boolean clearAccessibilityFocus(int i)
    {
        boolean flag;
        if(isAccessibilityFocused(i))
        {
            mFocusedVirtualViewId = 0x80000000;
            mView.invalidate();
            sendEventForVirtualView(i, 0x10000);
            flag = true;
        } else
        {
            flag = false;
        }
        return flag;
    }

    private AccessibilityEvent createEvent(int i, int j)
    {
        i;
        JVM INSTR tableswitch -1 -1: default 20
    //                   -1 29;
           goto _L1 _L2
_L1:
        AccessibilityEvent accessibilityevent = createEventForChild(i, j);
_L4:
        return accessibilityevent;
_L2:
        accessibilityevent = createEventForHost(j);
        if(true) goto _L4; else goto _L3
_L3:
    }

    private AccessibilityEvent createEventForChild(int i, int j)
    {
        AccessibilityEvent accessibilityevent = AccessibilityEvent.obtain(j);
        accessibilityevent.setEnabled(true);
        accessibilityevent.setClassName(DEFAULT_CLASS_NAME);
        onPopulateEventForVirtualView(i, accessibilityevent);
        if(accessibilityevent.getText().isEmpty() && accessibilityevent.getContentDescription() == null)
        {
            throw new RuntimeException("Callbacks must add text or a content description in populateEventForVirtualViewId()");
        } else
        {
            accessibilityevent.setPackageName(mView.getContext().getPackageName());
            AccessibilityEventCompat.asRecord(accessibilityevent).setSource(mView, i);
            return accessibilityevent;
        }
    }

    private AccessibilityEvent createEventForHost(int i)
    {
        AccessibilityEvent accessibilityevent = AccessibilityEvent.obtain(i);
        ViewCompat.onInitializeAccessibilityEvent(mView, accessibilityevent);
        return accessibilityevent;
    }

    private AccessibilityNodeInfoCompat createNode(int i)
    {
        i;
        JVM INSTR tableswitch -1 -1: default 20
    //                   -1 28;
           goto _L1 _L2
_L1:
        AccessibilityNodeInfoCompat accessibilitynodeinfocompat = createNodeForChild(i);
_L4:
        return accessibilitynodeinfocompat;
_L2:
        accessibilitynodeinfocompat = createNodeForHost();
        if(true) goto _L4; else goto _L3
_L3:
    }

    private AccessibilityNodeInfoCompat createNodeForChild(int i)
    {
        AccessibilityNodeInfoCompat accessibilitynodeinfocompat = AccessibilityNodeInfoCompat.obtain();
        accessibilitynodeinfocompat.setEnabled(true);
        accessibilitynodeinfocompat.setClassName(DEFAULT_CLASS_NAME);
        onPopulateNodeForVirtualView(i, accessibilitynodeinfocompat);
        if(accessibilitynodeinfocompat.getText() == null && accessibilitynodeinfocompat.getContentDescription() == null)
            throw new RuntimeException("Callbacks must add text or a content description in populateNodeForVirtualViewId()");
        accessibilitynodeinfocompat.getBoundsInParent(mTempParentRect);
        if(mTempParentRect.isEmpty())
            throw new RuntimeException("Callbacks must set parent bounds in populateNodeForVirtualViewId()");
        int j = accessibilitynodeinfocompat.getActions();
        if((j & 0x40) != 0)
            throw new RuntimeException("Callbacks must not add ACTION_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
        if((j & 0x80) != 0)
            throw new RuntimeException("Callbacks must not add ACTION_CLEAR_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
        accessibilitynodeinfocompat.setPackageName(mView.getContext().getPackageName());
        accessibilitynodeinfocompat.setSource(mView, i);
        accessibilitynodeinfocompat.setParent(mView);
        if(mFocusedVirtualViewId == i)
        {
            accessibilitynodeinfocompat.setAccessibilityFocused(true);
            accessibilitynodeinfocompat.addAction(128);
        } else
        {
            accessibilitynodeinfocompat.setAccessibilityFocused(false);
            accessibilitynodeinfocompat.addAction(64);
        }
        if(intersectVisibleToUser(mTempParentRect))
        {
            accessibilitynodeinfocompat.setVisibleToUser(true);
            accessibilitynodeinfocompat.setBoundsInParent(mTempParentRect);
        }
        mView.getLocationOnScreen(mTempGlobalRect);
        i = mTempGlobalRect[0];
        j = mTempGlobalRect[1];
        mTempScreenRect.set(mTempParentRect);
        mTempScreenRect.offset(i, j);
        accessibilitynodeinfocompat.setBoundsInScreen(mTempScreenRect);
        return accessibilitynodeinfocompat;
    }

    private AccessibilityNodeInfoCompat createNodeForHost()
    {
        AccessibilityNodeInfoCompat accessibilitynodeinfocompat = AccessibilityNodeInfoCompat.obtain(mView);
        ViewCompat.onInitializeAccessibilityNodeInfo(mView, accessibilitynodeinfocompat);
        onPopulateNodeForHost(accessibilitynodeinfocompat);
        Object obj = new LinkedList();
        getVisibleVirtualViews(((List) (obj)));
        Integer integer;
        for(obj = ((LinkedList) (obj)).iterator(); ((Iterator) (obj)).hasNext(); accessibilitynodeinfocompat.addChild(mView, integer.intValue()))
            integer = (Integer)((Iterator) (obj)).next();

        return accessibilitynodeinfocompat;
    }

    private boolean intersectVisibleToUser(Rect rect)
    {
        boolean flag;
        boolean flag1;
        flag = false;
        flag1 = flag;
        if(rect == null) goto _L2; else goto _L1
_L1:
        if(!rect.isEmpty()) goto _L4; else goto _L3
_L3:
        flag1 = flag;
_L2:
        return flag1;
_L4:
        flag1 = flag;
        if(mView.getWindowVisibility() != 0)
            continue; /* Loop/switch isn't completed */
        Object obj;
        for(obj = mView.getParent(); obj instanceof View; obj = ((View) (obj)).getParent())
        {
            obj = (View)obj;
            flag1 = flag;
            if(ViewCompat.getAlpha(((View) (obj))) <= 0.0F)
                continue; /* Loop/switch isn't completed */
            flag1 = flag;
            if(((View) (obj)).getVisibility() != 0)
                continue; /* Loop/switch isn't completed */
        }

        flag1 = flag;
        if(obj != null)
        {
            flag1 = flag;
            if(mView.getLocalVisibleRect(mTempVisibleRect))
                flag1 = rect.intersect(mTempVisibleRect);
        }
        if(true) goto _L2; else goto _L5
_L5:
    }

    private boolean isAccessibilityFocused(int i)
    {
        boolean flag;
        if(mFocusedVirtualViewId == i)
            flag = true;
        else
            flag = false;
        return flag;
    }

    private boolean manageFocusForChild(int i, int j, Bundle bundle)
    {
        j;
        JVM INSTR lookupswitch 2: default 28
    //                   64: 34
    //                   128: 44;
           goto _L1 _L2 _L3
_L1:
        boolean flag = false;
_L5:
        return flag;
_L2:
        flag = requestAccessibilityFocus(i);
        continue; /* Loop/switch isn't completed */
_L3:
        flag = clearAccessibilityFocus(i);
        if(true) goto _L5; else goto _L4
_L4:
    }

    private boolean performAction(int i, int j, Bundle bundle)
    {
        i;
        JVM INSTR tableswitch -1 -1: default 20
    //                   -1 32;
           goto _L1 _L2
_L1:
        boolean flag = performActionForChild(i, j, bundle);
_L4:
        return flag;
_L2:
        flag = performActionForHost(j, bundle);
        if(true) goto _L4; else goto _L3
_L3:
    }

    private boolean performActionForChild(int i, int j, Bundle bundle)
    {
        j;
        JVM INSTR lookupswitch 2: default 28
    //                   64: 40
    //                   128: 40;
           goto _L1 _L2 _L2
_L1:
        boolean flag = onPerformActionForVirtualView(i, j, bundle);
_L4:
        return flag;
_L2:
        flag = manageFocusForChild(i, j, bundle);
        if(true) goto _L4; else goto _L3
_L3:
    }

    private boolean performActionForHost(int i, Bundle bundle)
    {
        return ViewCompat.performAccessibilityAction(mView, i, bundle);
    }

    private boolean requestAccessibilityFocus(int i)
    {
        boolean flag;
        boolean flag1;
        flag = false;
        flag1 = flag;
        if(!mManager.isEnabled()) goto _L2; else goto _L1
_L1:
        if(AccessibilityManagerCompat.isTouchExplorationEnabled(mManager)) goto _L4; else goto _L3
_L3:
        flag1 = flag;
_L2:
        return flag1;
_L4:
        flag1 = flag;
        if(!isAccessibilityFocused(i))
        {
            if(mFocusedVirtualViewId != 0x80000000)
                sendEventForVirtualView(mFocusedVirtualViewId, 0x10000);
            mFocusedVirtualViewId = i;
            mView.invalidate();
            sendEventForVirtualView(i, 32768);
            flag1 = true;
        }
        if(true) goto _L2; else goto _L5
_L5:
    }

    private void updateHoveredVirtualView(int i)
    {
        if(mHoveredVirtualViewId != i)
        {
            int j = mHoveredVirtualViewId;
            mHoveredVirtualViewId = i;
            sendEventForVirtualView(i, 128);
            sendEventForVirtualView(j, 256);
        }
    }

    public boolean dispatchHoverEvent(MotionEvent motionevent)
    {
        boolean flag;
        boolean flag1;
        boolean flag2;
        flag = true;
        flag1 = false;
        flag2 = flag1;
        if(!mManager.isEnabled()) goto _L2; else goto _L1
_L1:
        if(AccessibilityManagerCompat.isTouchExplorationEnabled(mManager)) goto _L4; else goto _L3
_L3:
        flag2 = flag1;
_L2:
        return flag2;
_L4:
        switch(motionevent.getAction())
        {
        case 8: // '\b'
        default:
            flag2 = flag1;
            break;

        case 7: // '\007'
        case 9: // '\t'
            int i = getVirtualViewAt(motionevent.getX(), motionevent.getY());
            updateHoveredVirtualView(i);
            if(i != 0x80000000)
                flag2 = flag;
            else
                flag2 = false;
            break;

        case 10: // '\n'
            flag2 = flag1;
            if(mFocusedVirtualViewId != 0x80000000)
            {
                updateHoveredVirtualView(0x80000000);
                flag2 = true;
            }
            break;
        }
        if(true) goto _L2; else goto _L5
_L5:
    }

    public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View view)
    {
        if(mNodeProvider == null)
            mNodeProvider = new ExploreByTouchNodeProvider();
        return mNodeProvider;
    }

    public int getFocusedVirtualView()
    {
        return mFocusedVirtualViewId;
    }

    protected abstract int getVirtualViewAt(float f, float f1);

    protected abstract void getVisibleVirtualViews(List list);

    public void invalidateRoot()
    {
        invalidateVirtualView(-1);
    }

    public void invalidateVirtualView(int i)
    {
        sendEventForVirtualView(i, 2048);
    }

    protected abstract boolean onPerformActionForVirtualView(int i, int j, Bundle bundle);

    protected abstract void onPopulateEventForVirtualView(int i, AccessibilityEvent accessibilityevent);

    public void onPopulateNodeForHost(AccessibilityNodeInfoCompat accessibilitynodeinfocompat)
    {
    }

    protected abstract void onPopulateNodeForVirtualView(int i, AccessibilityNodeInfoCompat accessibilitynodeinfocompat);

    public boolean sendEventForVirtualView(int i, int j)
    {
        boolean flag;
        boolean flag1;
        flag = false;
        flag1 = flag;
        if(i == 0x80000000) goto _L2; else goto _L1
_L1:
        if(mManager.isEnabled()) goto _L4; else goto _L3
_L3:
        flag1 = flag;
_L2:
        return flag1;
_L4:
        android.view.ViewParent viewparent = mView.getParent();
        flag1 = flag;
        if(viewparent != null)
        {
            AccessibilityEvent accessibilityevent = createEvent(i, j);
            flag1 = ViewParentCompat.requestSendAccessibilityEvent(viewparent, mView, accessibilityevent);
        }
        if(true) goto _L2; else goto _L5
_L5:
    }

    private static final String DEFAULT_CLASS_NAME = android/view/View.getName();
    public static final int HOST_ID = -1;
    public static final int INVALID_ID = 0x80000000;
    private int mFocusedVirtualViewId;
    private int mHoveredVirtualViewId;
    private final AccessibilityManager mManager;
    private ExploreByTouchNodeProvider mNodeProvider;
    private final int mTempGlobalRect[] = new int[2];
    private final Rect mTempParentRect = new Rect();
    private final Rect mTempScreenRect = new Rect();
    private final Rect mTempVisibleRect = new Rect();
    private final View mView;



}
