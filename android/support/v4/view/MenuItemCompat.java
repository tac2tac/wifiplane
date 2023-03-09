// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.view;

import android.support.v4.internal.view.SupportMenuItem;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

// Referenced classes of package android.support.v4.view:
//            ActionProvider, MenuItemCompatHoneycomb, MenuItemCompatIcs

public final class MenuItemCompat
{
    static class BaseMenuVersionImpl
        implements MenuVersionImpl
    {

        public boolean collapseActionView(MenuItem menuitem)
        {
            return false;
        }

        public boolean expandActionView(MenuItem menuitem)
        {
            return false;
        }

        public View getActionView(MenuItem menuitem)
        {
            return null;
        }

        public boolean isActionViewExpanded(MenuItem menuitem)
        {
            return false;
        }

        public MenuItem setActionView(MenuItem menuitem, int i)
        {
            return menuitem;
        }

        public MenuItem setActionView(MenuItem menuitem, View view)
        {
            return menuitem;
        }

        public MenuItem setOnActionExpandListener(MenuItem menuitem, OnActionExpandListener onactionexpandlistener)
        {
            return menuitem;
        }

        public void setShowAsAction(MenuItem menuitem, int i)
        {
        }

        BaseMenuVersionImpl()
        {
        }
    }

    static class HoneycombMenuVersionImpl
        implements MenuVersionImpl
    {

        public boolean collapseActionView(MenuItem menuitem)
        {
            return false;
        }

        public boolean expandActionView(MenuItem menuitem)
        {
            return false;
        }

        public View getActionView(MenuItem menuitem)
        {
            return MenuItemCompatHoneycomb.getActionView(menuitem);
        }

        public boolean isActionViewExpanded(MenuItem menuitem)
        {
            return false;
        }

        public MenuItem setActionView(MenuItem menuitem, int i)
        {
            return MenuItemCompatHoneycomb.setActionView(menuitem, i);
        }

        public MenuItem setActionView(MenuItem menuitem, View view)
        {
            return MenuItemCompatHoneycomb.setActionView(menuitem, view);
        }

        public MenuItem setOnActionExpandListener(MenuItem menuitem, OnActionExpandListener onactionexpandlistener)
        {
            return menuitem;
        }

        public void setShowAsAction(MenuItem menuitem, int i)
        {
            MenuItemCompatHoneycomb.setShowAsAction(menuitem, i);
        }

        HoneycombMenuVersionImpl()
        {
        }
    }

    static class IcsMenuVersionImpl extends HoneycombMenuVersionImpl
    {

        public boolean collapseActionView(MenuItem menuitem)
        {
            return MenuItemCompatIcs.collapseActionView(menuitem);
        }

        public boolean expandActionView(MenuItem menuitem)
        {
            return MenuItemCompatIcs.expandActionView(menuitem);
        }

        public boolean isActionViewExpanded(MenuItem menuitem)
        {
            return MenuItemCompatIcs.isActionViewExpanded(menuitem);
        }

        public MenuItem setOnActionExpandListener(MenuItem menuitem, OnActionExpandListener onactionexpandlistener)
        {
            if(onactionexpandlistener == null)
                menuitem = MenuItemCompatIcs.setOnActionExpandListener(menuitem, null);
            else
                menuitem = MenuItemCompatIcs.setOnActionExpandListener(menuitem, onactionexpandlistener. new MenuItemCompatIcs.SupportActionExpandProxy() {

                    public boolean onMenuItemActionCollapse(MenuItem menuitem)
                    {
                        return listener.onMenuItemActionCollapse(menuitem);
                    }

                    public boolean onMenuItemActionExpand(MenuItem menuitem)
                    {
                        return listener.onMenuItemActionExpand(menuitem);
                    }

                    final IcsMenuVersionImpl this$0;
                    final OnActionExpandListener val$listener;

            
            {
                this$0 = final_icsmenuversionimpl;
                listener = OnActionExpandListener.this;
                super();
            }
                }
);
            return menuitem;
        }

        IcsMenuVersionImpl()
        {
        }
    }

    static interface MenuVersionImpl
    {

        public abstract boolean collapseActionView(MenuItem menuitem);

        public abstract boolean expandActionView(MenuItem menuitem);

        public abstract View getActionView(MenuItem menuitem);

        public abstract boolean isActionViewExpanded(MenuItem menuitem);

        public abstract MenuItem setActionView(MenuItem menuitem, int i);

        public abstract MenuItem setActionView(MenuItem menuitem, View view);

        public abstract MenuItem setOnActionExpandListener(MenuItem menuitem, OnActionExpandListener onactionexpandlistener);

        public abstract void setShowAsAction(MenuItem menuitem, int i);
    }

    public static interface OnActionExpandListener
    {

        public abstract boolean onMenuItemActionCollapse(MenuItem menuitem);

        public abstract boolean onMenuItemActionExpand(MenuItem menuitem);
    }


    private MenuItemCompat()
    {
    }

    public static boolean collapseActionView(MenuItem menuitem)
    {
        boolean flag;
        if(menuitem instanceof SupportMenuItem)
            flag = ((SupportMenuItem)menuitem).collapseActionView();
        else
            flag = IMPL.collapseActionView(menuitem);
        return flag;
    }

    public static boolean expandActionView(MenuItem menuitem)
    {
        boolean flag;
        if(menuitem instanceof SupportMenuItem)
            flag = ((SupportMenuItem)menuitem).expandActionView();
        else
            flag = IMPL.expandActionView(menuitem);
        return flag;
    }

    public static ActionProvider getActionProvider(MenuItem menuitem)
    {
        if(menuitem instanceof SupportMenuItem)
        {
            menuitem = ((SupportMenuItem)menuitem).getSupportActionProvider();
        } else
        {
            Log.w("MenuItemCompat", "getActionProvider: item does not implement SupportMenuItem; returning null");
            menuitem = null;
        }
        return menuitem;
    }

    public static View getActionView(MenuItem menuitem)
    {
        if(menuitem instanceof SupportMenuItem)
            menuitem = ((SupportMenuItem)menuitem).getActionView();
        else
            menuitem = IMPL.getActionView(menuitem);
        return menuitem;
    }

    public static boolean isActionViewExpanded(MenuItem menuitem)
    {
        boolean flag;
        if(menuitem instanceof SupportMenuItem)
            flag = ((SupportMenuItem)menuitem).isActionViewExpanded();
        else
            flag = IMPL.isActionViewExpanded(menuitem);
        return flag;
    }

    public static MenuItem setActionProvider(MenuItem menuitem, ActionProvider actionprovider)
    {
        if(menuitem instanceof SupportMenuItem)
            menuitem = ((SupportMenuItem)menuitem).setSupportActionProvider(actionprovider);
        else
            Log.w("MenuItemCompat", "setActionProvider: item does not implement SupportMenuItem; ignoring");
        return menuitem;
    }

    public static MenuItem setActionView(MenuItem menuitem, int i)
    {
        if(menuitem instanceof SupportMenuItem)
            menuitem = ((SupportMenuItem)menuitem).setActionView(i);
        else
            menuitem = IMPL.setActionView(menuitem, i);
        return menuitem;
    }

    public static MenuItem setActionView(MenuItem menuitem, View view)
    {
        if(menuitem instanceof SupportMenuItem)
            menuitem = ((SupportMenuItem)menuitem).setActionView(view);
        else
            menuitem = IMPL.setActionView(menuitem, view);
        return menuitem;
    }

    public static MenuItem setOnActionExpandListener(MenuItem menuitem, OnActionExpandListener onactionexpandlistener)
    {
        if(menuitem instanceof SupportMenuItem)
            menuitem = ((SupportMenuItem)menuitem).setSupportOnActionExpandListener(onactionexpandlistener);
        else
            menuitem = IMPL.setOnActionExpandListener(menuitem, onactionexpandlistener);
        return menuitem;
    }

    public static void setShowAsAction(MenuItem menuitem, int i)
    {
        if(menuitem instanceof SupportMenuItem)
            ((SupportMenuItem)menuitem).setShowAsAction(i);
        else
            IMPL.setShowAsAction(menuitem, i);
    }

    static final MenuVersionImpl IMPL;
    public static final int SHOW_AS_ACTION_ALWAYS = 2;
    public static final int SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW = 8;
    public static final int SHOW_AS_ACTION_IF_ROOM = 1;
    public static final int SHOW_AS_ACTION_NEVER = 0;
    public static final int SHOW_AS_ACTION_WITH_TEXT = 4;
    private static final String TAG = "MenuItemCompat";

    static 
    {
        int i = android.os.Build.VERSION.SDK_INT;
        if(i >= 14)
            IMPL = new IcsMenuVersionImpl();
        else
        if(i >= 11)
            IMPL = new HoneycombMenuVersionImpl();
        else
            IMPL = new BaseMenuVersionImpl();
    }
}
