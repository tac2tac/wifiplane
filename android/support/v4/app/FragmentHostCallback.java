// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.SimpleArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import java.io.FileDescriptor;
import java.io.PrintWriter;

// Referenced classes of package android.support.v4.app:
//            FragmentContainer, FragmentManagerImpl, FragmentActivity, LoaderManagerImpl, 
//            Fragment

public abstract class FragmentHostCallback extends FragmentContainer
{

    FragmentHostCallback(Activity activity, Context context, Handler handler, int i)
    {
        mFragmentManager = new FragmentManagerImpl();
        mActivity = activity;
        mContext = context;
        mHandler = handler;
        mWindowAnimations = i;
    }

    public FragmentHostCallback(Context context, Handler handler, int i)
    {
        this(null, context, handler, i);
    }

    FragmentHostCallback(FragmentActivity fragmentactivity)
    {
        this(((Activity) (fragmentactivity)), ((Context) (fragmentactivity)), fragmentactivity.mHandler, 0);
    }

    void doLoaderDestroy()
    {
        if(mLoaderManager != null)
            mLoaderManager.doDestroy();
    }

    void doLoaderRetain()
    {
        if(mLoaderManager != null)
            mLoaderManager.doRetain();
    }

    void doLoaderStart()
    {
        if(!mLoadersStarted) goto _L2; else goto _L1
_L1:
        return;
_L2:
        mLoadersStarted = true;
        if(mLoaderManager == null)
            break; /* Loop/switch isn't completed */
        mLoaderManager.doStart();
_L5:
        mCheckedForLoaderManager = true;
        if(true) goto _L1; else goto _L3
_L3:
        if(mCheckedForLoaderManager) goto _L5; else goto _L4
_L4:
        mLoaderManager = getLoaderManager("(root)", mLoadersStarted, false);
        if(mLoaderManager != null && !mLoaderManager.mStarted)
            mLoaderManager.doStart();
          goto _L5
    }

    void doLoaderStop(boolean flag)
    {
        mRetainLoaders = flag;
        break MISSING_BLOCK_LABEL_5;
        if(mLoaderManager != null && mLoadersStarted)
        {
            mLoadersStarted = false;
            if(flag)
                mLoaderManager.doRetain();
            else
                mLoaderManager.doStop();
        }
        return;
    }

    void dumpLoaders(String s, FileDescriptor filedescriptor, PrintWriter printwriter, String as[])
    {
        printwriter.print(s);
        printwriter.print("mLoadersStarted=");
        printwriter.println(mLoadersStarted);
        if(mLoaderManager != null)
        {
            printwriter.print(s);
            printwriter.print("Loader Manager ");
            printwriter.print(Integer.toHexString(System.identityHashCode(mLoaderManager)));
            printwriter.println(":");
            mLoaderManager.dump((new StringBuilder()).append(s).append("  ").toString(), filedescriptor, printwriter, as);
        }
    }

    Activity getActivity()
    {
        return mActivity;
    }

    Context getContext()
    {
        return mContext;
    }

    FragmentManagerImpl getFragmentManagerImpl()
    {
        return mFragmentManager;
    }

    Handler getHandler()
    {
        return mHandler;
    }

    LoaderManagerImpl getLoaderManager(String s, boolean flag, boolean flag1)
    {
        if(mAllLoaderManagers == null)
            mAllLoaderManagers = new SimpleArrayMap();
        LoaderManagerImpl loadermanagerimpl = (LoaderManagerImpl)mAllLoaderManagers.get(s);
        if(loadermanagerimpl == null)
        {
            if(flag1)
            {
                loadermanagerimpl = new LoaderManagerImpl(s, this, flag);
                mAllLoaderManagers.put(s, loadermanagerimpl);
            }
        } else
        {
            loadermanagerimpl.updateHostController(this);
        }
        return loadermanagerimpl;
    }

    LoaderManagerImpl getLoaderManagerImpl()
    {
        LoaderManagerImpl loadermanagerimpl;
        if(mLoaderManager != null)
        {
            loadermanagerimpl = mLoaderManager;
        } else
        {
            mCheckedForLoaderManager = true;
            mLoaderManager = getLoaderManager("(root)", mLoadersStarted, true);
            loadermanagerimpl = mLoaderManager;
        }
        return loadermanagerimpl;
    }

    boolean getRetainLoaders()
    {
        return mRetainLoaders;
    }

    void inactivateFragment(String s)
    {
        if(mAllLoaderManagers != null)
        {
            LoaderManagerImpl loadermanagerimpl = (LoaderManagerImpl)mAllLoaderManagers.get(s);
            if(loadermanagerimpl != null && !loadermanagerimpl.mRetaining)
            {
                loadermanagerimpl.doDestroy();
                mAllLoaderManagers.remove(s);
            }
        }
    }

    void onAttachFragment(Fragment fragment)
    {
    }

    public void onDump(String s, FileDescriptor filedescriptor, PrintWriter printwriter, String as[])
    {
    }

    public View onFindViewById(int i)
    {
        return null;
    }

    public abstract Object onGetHost();

    public LayoutInflater onGetLayoutInflater()
    {
        return (LayoutInflater)mContext.getSystemService("layout_inflater");
    }

    public int onGetWindowAnimations()
    {
        return mWindowAnimations;
    }

    public boolean onHasView()
    {
        return true;
    }

    public boolean onHasWindowAnimations()
    {
        return true;
    }

    public void onRequestPermissionsFromFragment(Fragment fragment, String as[], int i)
    {
    }

    public boolean onShouldSaveFragmentState(Fragment fragment)
    {
        return true;
    }

    public boolean onShouldShowRequestPermissionRationale(String s)
    {
        return false;
    }

    public void onStartActivityFromFragment(Fragment fragment, Intent intent, int i)
    {
        onStartActivityFromFragment(fragment, intent, i, null);
    }

    public void onStartActivityFromFragment(Fragment fragment, Intent intent, int i, Bundle bundle)
    {
        if(i != -1)
        {
            throw new IllegalStateException("Starting activity with a requestCode requires a FragmentActivity host");
        } else
        {
            mContext.startActivity(intent);
            return;
        }
    }

    public void onSupportInvalidateOptionsMenu()
    {
    }

    void reportLoaderStart()
    {
        if(mAllLoaderManagers != null)
        {
            int i = mAllLoaderManagers.size();
            LoaderManagerImpl aloadermanagerimpl[] = new LoaderManagerImpl[i];
            for(int j = i - 1; j >= 0; j--)
                aloadermanagerimpl[j] = (LoaderManagerImpl)mAllLoaderManagers.valueAt(j);

            for(int k = 0; k < i; k++)
            {
                LoaderManagerImpl loadermanagerimpl = aloadermanagerimpl[k];
                loadermanagerimpl.finishRetain();
                loadermanagerimpl.doReportStart();
            }

        }
    }

    void restoreLoaderNonConfig(SimpleArrayMap simplearraymap)
    {
        mAllLoaderManagers = simplearraymap;
    }

    SimpleArrayMap retainLoaderNonConfig()
    {
        boolean flag = false;
        int i = 0;
        if(mAllLoaderManagers != null)
        {
            int j = mAllLoaderManagers.size();
            LoaderManagerImpl aloadermanagerimpl[] = new LoaderManagerImpl[j];
            for(int k = j - 1; k >= 0; k--)
                aloadermanagerimpl[k] = (LoaderManagerImpl)mAllLoaderManagers.valueAt(k);

            flag = false;
            boolean flag1 = i;
            i = ((flag) ? 1 : 0);
            do
            {
                flag = flag1;
                if(i >= j)
                    break;
                LoaderManagerImpl loadermanagerimpl = aloadermanagerimpl[i];
                if(loadermanagerimpl.mRetaining)
                {
                    flag1 = true;
                } else
                {
                    loadermanagerimpl.doDestroy();
                    mAllLoaderManagers.remove(loadermanagerimpl.mWho);
                }
                i++;
            } while(true);
        }
        SimpleArrayMap simplearraymap;
        if(flag)
            simplearraymap = mAllLoaderManagers;
        else
            simplearraymap = null;
        return simplearraymap;
    }

    private final Activity mActivity;
    private SimpleArrayMap mAllLoaderManagers;
    private boolean mCheckedForLoaderManager;
    final Context mContext;
    final FragmentManagerImpl mFragmentManager;
    private final Handler mHandler;
    private LoaderManagerImpl mLoaderManager;
    private boolean mLoadersStarted;
    private boolean mRetainLoaders;
    final int mWindowAnimations;
}
