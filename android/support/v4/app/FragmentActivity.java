// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.app;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.*;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.util.SimpleArrayMap;
import android.support.v4.util.SparseArrayCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package android.support.v4.app:
//            BaseFragmentActivityHoneycomb, FragmentController, Fragment, ActivityCompat, 
//            FragmentManager, ActivityCompat21, ActivityCompatHoneycomb, LoaderManager, 
//            SharedElementCallback, FragmentHostCallback

public class FragmentActivity extends BaseFragmentActivityHoneycomb
    implements ActivityCompat.OnRequestPermissionsResultCallback, ActivityCompatApi23.RequestPermissionsRequestCodeValidator
{
    class HostCallbacks extends FragmentHostCallback
    {

        public void onAttachFragment(Fragment fragment)
        {
            FragmentActivity.this.onAttachFragment(fragment);
        }

        public void onDump(String s, FileDescriptor filedescriptor, PrintWriter printwriter, String as[])
        {
            dump(s, filedescriptor, printwriter, as);
        }

        public View onFindViewById(int i)
        {
            return findViewById(i);
        }

        public FragmentActivity onGetHost()
        {
            return FragmentActivity.this;
        }

        public volatile Object onGetHost()
        {
            return onGetHost();
        }

        public LayoutInflater onGetLayoutInflater()
        {
            return getLayoutInflater().cloneInContext(FragmentActivity.this);
        }

        public int onGetWindowAnimations()
        {
            Window window = getWindow();
            int i;
            if(window == null)
                i = 0;
            else
                i = window.getAttributes().windowAnimations;
            return i;
        }

        public boolean onHasView()
        {
            Window window = getWindow();
            boolean flag;
            if(window != null && window.peekDecorView() != null)
                flag = true;
            else
                flag = false;
            return flag;
        }

        public boolean onHasWindowAnimations()
        {
            boolean flag;
            if(getWindow() != null)
                flag = true;
            else
                flag = false;
            return flag;
        }

        public void onRequestPermissionsFromFragment(Fragment fragment, String as[], int i)
        {
            requestPermissionsFromFragment(fragment, as, i);
        }

        public boolean onShouldSaveFragmentState(Fragment fragment)
        {
            boolean flag;
            if(!isFinishing())
                flag = true;
            else
                flag = false;
            return flag;
        }

        public boolean onShouldShowRequestPermissionRationale(String s)
        {
            return ActivityCompat.shouldShowRequestPermissionRationale(FragmentActivity.this, s);
        }

        public void onStartActivityFromFragment(Fragment fragment, Intent intent, int i)
        {
            startActivityFromFragment(fragment, intent, i);
        }

        public void onStartActivityFromFragment(Fragment fragment, Intent intent, int i, Bundle bundle)
        {
            startActivityFromFragment(fragment, intent, i, bundle);
        }

        public void onSupportInvalidateOptionsMenu()
        {
            supportInvalidateOptionsMenu();
        }

        final FragmentActivity this$0;

        public HostCallbacks()
        {
            this$0 = FragmentActivity.this;
            super(FragmentActivity.this);
        }
    }

    static final class NonConfigurationInstances
    {

        Object custom;
        List fragments;
        SimpleArrayMap loaders;

        NonConfigurationInstances()
        {
        }
    }


    public FragmentActivity()
    {
    }

    private int allocateRequestIndex(Fragment fragment)
    {
        if(mPendingFragmentActivityResults.size() >= 65534)
            throw new IllegalStateException("Too many pending Fragment activity results.");
        for(; mPendingFragmentActivityResults.indexOfKey(mNextCandidateRequestIndex) >= 0; mNextCandidateRequestIndex = (mNextCandidateRequestIndex + 1) % 65534);
        int i = mNextCandidateRequestIndex;
        mPendingFragmentActivityResults.put(i, fragment.mWho);
        mNextCandidateRequestIndex = (mNextCandidateRequestIndex + 1) % 65534;
        return i;
    }

    private void dumpViewHierarchy(String s, PrintWriter printwriter, View view)
    {
        printwriter.print(s);
        if(view != null) goto _L2; else goto _L1
_L1:
        printwriter.println("null");
_L4:
        return;
_L2:
        printwriter.println(viewToString(view));
        if(view instanceof ViewGroup)
        {
            view = (ViewGroup)view;
            int i = view.getChildCount();
            if(i > 0)
            {
                s = (new StringBuilder()).append(s).append("  ").toString();
                int j = 0;
                while(j < i) 
                {
                    dumpViewHierarchy(s, printwriter, view.getChildAt(j));
                    j++;
                }
            }
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    private void requestPermissionsFromFragment(Fragment fragment, String as[], int i)
    {
        if(i == -1)
        {
            ActivityCompat.requestPermissions(this, as, i);
        } else
        {
            if((i & 0xffffff00) != 0)
                throw new IllegalArgumentException("Can only use lower 8 bits for requestCode");
            mRequestedPermissionsFromFragment = true;
            ActivityCompat.requestPermissions(this, as, (fragment.mIndex + 1 << 8) + (i & 0xff));
        }
    }

    private static String viewToString(View view)
    {
        byte byte0;
        int i;
        StringBuilder stringbuilder;
        byte0 = 70;
        i = 46;
        stringbuilder = new StringBuilder(128);
        stringbuilder.append(view.getClass().getName());
        stringbuilder.append('{');
        stringbuilder.append(Integer.toHexString(System.identityHashCode(view)));
        stringbuilder.append(' ');
        view.getVisibility();
        JVM INSTR lookupswitch 3: default 92
    //                   0: 536
    //                   4: 546
    //                   8: 556;
           goto _L1 _L2 _L3 _L4
_L1:
        stringbuilder.append('.');
_L11:
        char c;
        Object obj;
        String s;
        if(view.isFocusable())
        {
            byte byte4 = 70;
            c = byte4;
        } else
        {
            byte byte11 = 46;
            c = byte11;
        }
        stringbuilder.append(c);
        if(view.isEnabled())
        {
            byte byte5 = 69;
            c = byte5;
        } else
        {
            byte byte12 = 46;
            c = byte12;
        }
        stringbuilder.append(c);
        if(view.willNotDraw())
        {
            byte byte6 = 46;
            c = byte6;
        } else
        {
            byte byte13 = 68;
            c = byte13;
        }
        stringbuilder.append(c);
        if(view.isHorizontalScrollBarEnabled())
        {
            byte byte7 = 72;
            c = byte7;
        } else
        {
            byte byte14 = 46;
            c = byte14;
        }
        stringbuilder.append(c);
        if(view.isVerticalScrollBarEnabled())
        {
            byte byte8 = 86;
            c = byte8;
        } else
        {
            byte byte15 = 46;
            c = byte15;
        }
        stringbuilder.append(c);
        if(view.isClickable())
        {
            byte byte9 = 67;
            c = byte9;
        } else
        {
            byte byte16 = 46;
            c = byte16;
        }
        stringbuilder.append(c);
        if(view.isLongClickable())
        {
            byte byte10 = 76;
            c = byte10;
        } else
        {
            byte byte17 = 46;
            c = byte17;
        }
        stringbuilder.append(c);
        stringbuilder.append(' ');
        if(view.isFocused())
        {
            c = byte0;
        } else
        {
            byte byte2 = 46;
            c = byte2;
        }
        stringbuilder.append(c);
        if(view.isSelected())
        {
            byte byte1 = 83;
            c = byte1;
        } else
        {
            byte byte3 = 46;
            c = byte3;
        }
        stringbuilder.append(c);
        c = i;
        if(view.isPressed())
        {
            i = 80;
            c = i;
        }
        stringbuilder.append(c);
        stringbuilder.append(' ');
        stringbuilder.append(view.getLeft());
        stringbuilder.append(',');
        stringbuilder.append(view.getTop());
        stringbuilder.append('-');
        stringbuilder.append(view.getRight());
        stringbuilder.append(',');
        stringbuilder.append(view.getBottom());
        i = view.getId();
        if(i == -1) goto _L6; else goto _L5
_L5:
        stringbuilder.append(" #");
        stringbuilder.append(Integer.toHexString(i));
        obj = view.getResources();
        if(i == 0 || obj == null) goto _L6; else goto _L7
_L7:
        0xff000000 & i;
        JVM INSTR lookupswitch 2: default 456
    //                   16777216: 668
    //                   2130706432: 661;
           goto _L8 _L9 _L10
_L8:
        view = ((Resources) (obj)).getResourcePackageName(i);
_L12:
        s = ((Resources) (obj)).getResourceTypeName(i);
        obj = ((Resources) (obj)).getResourceEntryName(i);
        stringbuilder.append(" ");
        stringbuilder.append(view);
        stringbuilder.append(":");
        stringbuilder.append(s);
        stringbuilder.append("/");
        stringbuilder.append(((String) (obj)));
_L6:
        stringbuilder.append("}");
        return stringbuilder.toString();
_L2:
        stringbuilder.append('V');
          goto _L11
_L3:
        stringbuilder.append('I');
          goto _L11
_L4:
        stringbuilder.append('G');
          goto _L11
_L10:
        view = "app";
          goto _L12
_L9:
        view = "android";
          goto _L12
        view;
          goto _L6
    }

    final View dispatchFragmentsOnCreateView(View view, String s, Context context, AttributeSet attributeset)
    {
        return mFragments.onCreateView(view, s, context, attributeset);
    }

    void doReallyStop(boolean flag)
    {
        if(!mReallyStopped)
        {
            mReallyStopped = true;
            mRetaining = flag;
            mHandler.removeMessages(1);
            onReallyStop();
        }
    }

    public void dump(String s, FileDescriptor filedescriptor, PrintWriter printwriter, String as[])
    {
        if(android.os.Build.VERSION.SDK_INT < 11);
        printwriter.print(s);
        printwriter.print("Local FragmentActivity ");
        printwriter.print(Integer.toHexString(System.identityHashCode(this)));
        printwriter.println(" State:");
        String s1 = (new StringBuilder()).append(s).append("  ").toString();
        printwriter.print(s1);
        printwriter.print("mCreated=");
        printwriter.print(mCreated);
        printwriter.print("mResumed=");
        printwriter.print(mResumed);
        printwriter.print(" mStopped=");
        printwriter.print(mStopped);
        printwriter.print(" mReallyStopped=");
        printwriter.println(mReallyStopped);
        mFragments.dumpLoaders(s1, filedescriptor, printwriter, as);
        mFragments.getSupportFragmentManager().dump(s, filedescriptor, printwriter, as);
        printwriter.print(s);
        printwriter.println("View Hierarchy:");
        dumpViewHierarchy((new StringBuilder()).append(s).append("  ").toString(), printwriter, getWindow().getDecorView());
    }

    public Object getLastCustomNonConfigurationInstance()
    {
        Object obj = (NonConfigurationInstances)getLastNonConfigurationInstance();
        if(obj != null)
            obj = ((NonConfigurationInstances) (obj)).custom;
        else
            obj = null;
        return obj;
    }

    public FragmentManager getSupportFragmentManager()
    {
        return mFragments.getSupportFragmentManager();
    }

    public LoaderManager getSupportLoaderManager()
    {
        return mFragments.getSupportLoaderManager();
    }

    public final MediaControllerCompat getSupportMediaController()
    {
        return mMediaController;
    }

    protected void onActivityResult(int i, int j, Intent intent)
    {
        mFragments.noteStateNotSaved();
        int k = i >> 16;
        if(k != 0)
        {
            k--;
            String s = (String)mPendingFragmentActivityResults.get(k);
            mPendingFragmentActivityResults.remove(k);
            if(s == null)
            {
                Log.w("FragmentActivity", "Activity result delivered for unknown Fragment.");
            } else
            {
                Fragment fragment = mFragments.findFragmentByWho(s);
                if(fragment == null)
                    Log.w("FragmentActivity", (new StringBuilder()).append("Activity result no fragment exists for who: ").append(s).toString());
                else
                    fragment.onActivityResult(0xffff & i, j, intent);
            }
        } else
        {
            super.onActivityResult(i, j, intent);
        }
    }

    public void onAttachFragment(Fragment fragment)
    {
    }

    public void onBackPressed()
    {
        if(!mFragments.getSupportFragmentManager().popBackStackImmediate())
            supportFinishAfterTransition();
    }

    public void onConfigurationChanged(Configuration configuration)
    {
        super.onConfigurationChanged(configuration);
        mFragments.dispatchConfigurationChanged(configuration);
    }

    protected void onCreate(Bundle bundle)
    {
        List list = null;
        mFragments.attachHost(null);
        super.onCreate(bundle);
        NonConfigurationInstances nonconfigurationinstances = (NonConfigurationInstances)getLastNonConfigurationInstance();
        if(nonconfigurationinstances != null)
            mFragments.restoreLoaderNonConfig(nonconfigurationinstances.loaders);
        if(bundle != null)
        {
            android.os.Parcelable parcelable = bundle.getParcelable("android:support:fragments");
            FragmentController fragmentcontroller = mFragments;
            if(nonconfigurationinstances != null)
                list = nonconfigurationinstances.fragments;
            fragmentcontroller.restoreAllState(parcelable, list);
            if(bundle.containsKey("android:support:next_request_index"))
            {
                mNextCandidateRequestIndex = bundle.getInt("android:support:next_request_index");
                int ai[] = bundle.getIntArray("android:support:request_indicies");
                bundle = bundle.getStringArray("android:support:request_fragment_who");
                if(ai == null || bundle == null || ai.length != bundle.length)
                {
                    Log.w("FragmentActivity", "Invalid requestCode mapping in savedInstanceState.");
                } else
                {
                    mPendingFragmentActivityResults = new SparseArrayCompat(ai.length);
                    int i = 0;
                    while(i < ai.length) 
                    {
                        mPendingFragmentActivityResults.put(ai[i], bundle[i]);
                        i++;
                    }
                }
            }
        }
        if(mPendingFragmentActivityResults == null)
        {
            mPendingFragmentActivityResults = new SparseArrayCompat();
            mNextCandidateRequestIndex = 0;
        }
        mFragments.dispatchCreate();
    }

    public boolean onCreatePanelMenu(int i, Menu menu)
    {
        boolean flag;
        if(i == 0)
        {
            flag = super.onCreatePanelMenu(i, menu) | mFragments.dispatchCreateOptionsMenu(menu, getMenuInflater());
            if(android.os.Build.VERSION.SDK_INT < 11)
                flag = true;
        } else
        {
            flag = super.onCreatePanelMenu(i, menu);
        }
        return flag;
    }

    public volatile View onCreateView(View view, String s, Context context, AttributeSet attributeset)
    {
        return super.onCreateView(view, s, context, attributeset);
    }

    public volatile View onCreateView(String s, Context context, AttributeSet attributeset)
    {
        return super.onCreateView(s, context, attributeset);
    }

    protected void onDestroy()
    {
        super.onDestroy();
        doReallyStop(false);
        mFragments.dispatchDestroy();
        mFragments.doLoaderDestroy();
    }

    public boolean onKeyDown(int i, KeyEvent keyevent)
    {
        boolean flag;
        if(android.os.Build.VERSION.SDK_INT < 5 && i == 4 && keyevent.getRepeatCount() == 0)
        {
            onBackPressed();
            flag = true;
        } else
        {
            flag = super.onKeyDown(i, keyevent);
        }
        return flag;
    }

    public void onLowMemory()
    {
        super.onLowMemory();
        mFragments.dispatchLowMemory();
    }

    public boolean onMenuItemSelected(int i, MenuItem menuitem)
    {
        if(!super.onMenuItemSelected(i, menuitem)) goto _L2; else goto _L1
_L1:
        boolean flag = true;
_L4:
        return flag;
_L2:
        switch(i)
        {
        default:
            flag = false;
            break;

        case 0: // '\0'
            flag = mFragments.dispatchOptionsItemSelected(menuitem);
            break;

        case 6: // '\006'
            flag = mFragments.dispatchContextItemSelected(menuitem);
            break;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        mFragments.noteStateNotSaved();
    }

    public void onPanelClosed(int i, Menu menu)
    {
        i;
        JVM INSTR tableswitch 0 0: default 20
    //                   0 27;
           goto _L1 _L2
_L1:
        super.onPanelClosed(i, menu);
        return;
_L2:
        mFragments.dispatchOptionsMenuClosed(menu);
        if(true) goto _L1; else goto _L3
_L3:
    }

    protected void onPause()
    {
        super.onPause();
        mResumed = false;
        if(mHandler.hasMessages(2))
        {
            mHandler.removeMessages(2);
            onResumeFragments();
        }
        mFragments.dispatchPause();
    }

    protected void onPostResume()
    {
        super.onPostResume();
        mHandler.removeMessages(2);
        onResumeFragments();
        mFragments.execPendingActions();
    }

    protected boolean onPrepareOptionsPanel(View view, Menu menu)
    {
        return super.onPreparePanel(0, view, menu);
    }

    public boolean onPreparePanel(int i, View view, Menu menu)
    {
        boolean flag;
        if(i == 0 && menu != null)
        {
            if(mOptionsMenuInvalidated)
            {
                mOptionsMenuInvalidated = false;
                menu.clear();
                onCreatePanelMenu(i, menu);
            }
            flag = onPrepareOptionsPanel(view, menu) | mFragments.dispatchPrepareOptionsMenu(menu);
        } else
        {
            flag = super.onPreparePanel(i, view, menu);
        }
        return flag;
    }

    void onReallyStop()
    {
        mFragments.doLoaderStop(mRetaining);
        mFragments.dispatchReallyStop();
    }

    public void onRequestPermissionsResult(int i, String as[], int ai[])
    {
        int j = i >> 8 & 0xff;
        if(j != 0)
        {
            j--;
            int k = mFragments.getActiveFragmentsCount();
            if(k == 0 || j < 0 || j >= k)
            {
                Log.w("FragmentActivity", (new StringBuilder()).append("Activity result fragment index out of range: 0x").append(Integer.toHexString(i)).toString());
            } else
            {
                Fragment fragment = (Fragment)mFragments.getActiveFragments(new ArrayList(k)).get(j);
                if(fragment == null)
                    Log.w("FragmentActivity", (new StringBuilder()).append("Activity result no fragment exists for index: 0x").append(Integer.toHexString(i)).toString());
                else
                    fragment.onRequestPermissionsResult(i & 0xff, as, ai);
            }
        }
    }

    protected void onResume()
    {
        super.onResume();
        mHandler.sendEmptyMessage(2);
        mResumed = true;
        mFragments.execPendingActions();
    }

    protected void onResumeFragments()
    {
        mFragments.dispatchResume();
    }

    public Object onRetainCustomNonConfigurationInstance()
    {
        return null;
    }

    public final Object onRetainNonConfigurationInstance()
    {
        if(mStopped)
            doReallyStop(true);
        Object obj = onRetainCustomNonConfigurationInstance();
        List list = mFragments.retainNonConfig();
        SimpleArrayMap simplearraymap = mFragments.retainLoaderNonConfig();
        NonConfigurationInstances nonconfigurationinstances;
        if(list == null && simplearraymap == null && obj == null)
        {
            nonconfigurationinstances = null;
        } else
        {
            nonconfigurationinstances = new NonConfigurationInstances();
            nonconfigurationinstances.custom = obj;
            nonconfigurationinstances.fragments = list;
            nonconfigurationinstances.loaders = simplearraymap;
        }
        return nonconfigurationinstances;
    }

    protected void onSaveInstanceState(Bundle bundle)
    {
        super.onSaveInstanceState(bundle);
        android.os.Parcelable parcelable = mFragments.saveAllState();
        if(parcelable != null)
            bundle.putParcelable("android:support:fragments", parcelable);
        if(mPendingFragmentActivityResults.size() > 0)
        {
            bundle.putInt("android:support:next_request_index", mNextCandidateRequestIndex);
            int ai[] = new int[mPendingFragmentActivityResults.size()];
            String as[] = new String[mPendingFragmentActivityResults.size()];
            for(int i = 0; i < mPendingFragmentActivityResults.size(); i++)
            {
                ai[i] = mPendingFragmentActivityResults.keyAt(i);
                as[i] = (String)mPendingFragmentActivityResults.valueAt(i);
            }

            bundle.putIntArray("android:support:request_indicies", ai);
            bundle.putStringArray("android:support:request_fragment_who", as);
        }
    }

    protected void onStart()
    {
        super.onStart();
        mStopped = false;
        mReallyStopped = false;
        mHandler.removeMessages(1);
        if(!mCreated)
        {
            mCreated = true;
            mFragments.dispatchActivityCreated();
        }
        mFragments.noteStateNotSaved();
        mFragments.execPendingActions();
        mFragments.doLoaderStart();
        mFragments.dispatchStart();
        mFragments.reportLoaderStart();
    }

    public void onStateNotSaved()
    {
        mFragments.noteStateNotSaved();
    }

    protected void onStop()
    {
        super.onStop();
        mStopped = true;
        mHandler.sendEmptyMessage(1);
        mFragments.dispatchStop();
    }

    public void setEnterSharedElementCallback(SharedElementCallback sharedelementcallback)
    {
        ActivityCompat.setEnterSharedElementCallback(this, sharedelementcallback);
    }

    public void setExitSharedElementCallback(SharedElementCallback sharedelementcallback)
    {
        ActivityCompat.setExitSharedElementCallback(this, sharedelementcallback);
    }

    public final void setSupportMediaController(MediaControllerCompat mediacontrollercompat)
    {
        mMediaController = mediacontrollercompat;
        if(android.os.Build.VERSION.SDK_INT >= 21)
            ActivityCompat21.setMediaController(this, mediacontrollercompat.getMediaController());
    }

    public void startActivityForResult(Intent intent, int i)
    {
        if(!mStartedActivityFromFragment && i != -1 && (0xffff0000 & i) != 0)
        {
            throw new IllegalArgumentException("Can only use lower 16 bits for requestCode");
        } else
        {
            super.startActivityForResult(intent, i);
            return;
        }
    }

    public void startActivityFromFragment(Fragment fragment, Intent intent, int i)
    {
        startActivityFromFragment(fragment, intent, i, null);
    }

    public void startActivityFromFragment(Fragment fragment, Intent intent, int i, Bundle bundle)
    {
        mStartedActivityFromFragment = true;
        if(i != -1) goto _L2; else goto _L1
_L1:
        ActivityCompat.startActivityForResult(this, intent, -1, bundle);
        mStartedActivityFromFragment = false;
_L4:
        return;
_L2:
        if((0xffff0000 & i) == 0)
            break MISSING_BLOCK_LABEL_53;
        fragment = JVM INSTR new #165 <Class IllegalArgumentException>;
        fragment.IllegalArgumentException("Can only use lower 16 bits for requestCode");
        throw fragment;
        fragment;
        mStartedActivityFromFragment = false;
        throw fragment;
        ActivityCompat.startActivityForResult(this, intent, (allocateRequestIndex(fragment) + 1 << 16) + (0xffff & i), bundle);
        mStartedActivityFromFragment = false;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void supportFinishAfterTransition()
    {
        ActivityCompat.finishAfterTransition(this);
    }

    public void supportInvalidateOptionsMenu()
    {
        if(android.os.Build.VERSION.SDK_INT >= 11)
            ActivityCompatHoneycomb.invalidateOptionsMenu(this);
        else
            mOptionsMenuInvalidated = true;
    }

    public void supportPostponeEnterTransition()
    {
        ActivityCompat.postponeEnterTransition(this);
    }

    public void supportStartPostponedEnterTransition()
    {
        ActivityCompat.startPostponedEnterTransition(this);
    }

    public final void validateRequestPermissionsRequestCode(int i)
    {
        if(mRequestedPermissionsFromFragment)
            mRequestedPermissionsFromFragment = false;
        else
        if((i & 0xffffff00) != 0)
            throw new IllegalArgumentException("Can only use lower 8 bits for requestCode");
    }

    static final String ALLOCATED_REQUEST_INDICIES_TAG = "android:support:request_indicies";
    static final String FRAGMENTS_TAG = "android:support:fragments";
    private static final int HONEYCOMB = 11;
    static final int MAX_NUM_PENDING_FRAGMENT_ACTIVITY_RESULTS = 65534;
    static final int MSG_REALLY_STOPPED = 1;
    static final int MSG_RESUME_PENDING = 2;
    static final String NEXT_CANDIDATE_REQUEST_INDEX_TAG = "android:support:next_request_index";
    static final String REQUEST_FRAGMENT_WHO_TAG = "android:support:request_fragment_who";
    private static final String TAG = "FragmentActivity";
    boolean mCreated;
    final FragmentController mFragments = FragmentController.createController(new HostCallbacks());
    final Handler mHandler = new Handler() {

        public void handleMessage(Message message)
        {
            message.what;
            JVM INSTR tableswitch 1 2: default 28
        //                       1 34
        //                       2 55;
               goto _L1 _L2 _L3
_L1:
            super.handleMessage(message);
_L5:
            return;
_L2:
            if(mStopped)
                doReallyStop(false);
            continue; /* Loop/switch isn't completed */
_L3:
            onResumeFragments();
            mFragments.execPendingActions();
            if(true) goto _L5; else goto _L4
_L4:
        }

        final FragmentActivity this$0;

            
            {
                this$0 = FragmentActivity.this;
                super();
            }
    }
;
    MediaControllerCompat mMediaController;
    int mNextCandidateRequestIndex;
    boolean mOptionsMenuInvalidated;
    SparseArrayCompat mPendingFragmentActivityResults;
    boolean mReallyStopped;
    boolean mRequestedPermissionsFromFragment;
    boolean mResumed;
    boolean mRetaining;
    boolean mStartedActivityFromFragment;
    boolean mStopped;

}
