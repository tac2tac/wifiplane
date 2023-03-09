// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.app;

import android.content.Context;
import android.content.res.*;
import android.os.*;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.LogWriter;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.ViewCompat;
import android.util.*;
import android.view.*;
import android.view.animation.*;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.*;

// Referenced classes of package android.support.v4.app:
//            FragmentManager, FragmentHostCallback, Fragment, BackStackRecord, 
//            LoaderManagerImpl, SuperNotCalledException, FragmentContainer, NoSaveStateFrameLayout, 
//            FragmentManagerState, FragmentState, BackStackState, FragmentController, 
//            FragmentTransaction

final class FragmentManagerImpl extends FragmentManager
    implements LayoutInflaterFactory
{
    static class AnimateOnHWLayerIfNeededListener
        implements android.view.animation.Animation.AnimationListener
    {

        public void onAnimationEnd(Animation animation)
        {
            if(mView != null && mShouldRunOnHWLayer)
                mView.post(new Runnable() {

                    public void run()
                    {
                        ViewCompat.setLayerType(mView, 0, null);
                    }

                    final AnimateOnHWLayerIfNeededListener this$0;

            
            {
                this$0 = AnimateOnHWLayerIfNeededListener.this;
                super();
            }
                }
);
            if(mOrignalListener != null)
                mOrignalListener.onAnimationEnd(animation);
        }

        public void onAnimationRepeat(Animation animation)
        {
            if(mOrignalListener != null)
                mOrignalListener.onAnimationRepeat(animation);
        }

        public void onAnimationStart(Animation animation)
        {
            if(mView != null)
            {
                mShouldRunOnHWLayer = FragmentManagerImpl.shouldRunOnHWLayer(mView, animation);
                if(mShouldRunOnHWLayer)
                    mView.post(new Runnable() {

                        public void run()
                        {
                            ViewCompat.setLayerType(mView, 2, null);
                        }

                        final AnimateOnHWLayerIfNeededListener this$0;

            
            {
                this$0 = AnimateOnHWLayerIfNeededListener.this;
                super();
            }
                    }
);
            }
            if(mOrignalListener != null)
                mOrignalListener.onAnimationStart(animation);
        }

        private android.view.animation.Animation.AnimationListener mOrignalListener;
        private boolean mShouldRunOnHWLayer;
        private View mView;


        public AnimateOnHWLayerIfNeededListener(View view, Animation animation)
        {
            mOrignalListener = null;
            mShouldRunOnHWLayer = false;
            mView = null;
            if(view != null && animation != null)
                mView = view;
        }

        public AnimateOnHWLayerIfNeededListener(View view, Animation animation, android.view.animation.Animation.AnimationListener animationlistener)
        {
            mOrignalListener = null;
            mShouldRunOnHWLayer = false;
            mView = null;
            if(view != null && animation != null)
            {
                mOrignalListener = animationlistener;
                mView = view;
            }
        }
    }

    static class FragmentTag
    {

        public static final int Fragment[] = {
            0x1010003, 0x10100d0, 0x10100d1
        };
        public static final int Fragment_id = 1;
        public static final int Fragment_name = 0;
        public static final int Fragment_tag = 2;


        FragmentTag()
        {
        }
    }


    FragmentManagerImpl()
    {
        mCurState = 0;
        mStateBundle = null;
        mStateArray = null;
        mExecCommit = new Runnable() {

            public void run()
            {
                execPendingActions();
            }

            final FragmentManagerImpl this$0;

            
            {
                this$0 = FragmentManagerImpl.this;
                super();
            }
        }
;
    }

    private void checkStateLoss()
    {
        if(mStateSaved)
            throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
        if(mNoTransactionsBecause != null)
            throw new IllegalStateException((new StringBuilder()).append("Can not perform this action inside of ").append(mNoTransactionsBecause).toString());
        else
            return;
    }

    static Animation makeFadeAnimation(Context context, float f, float f1)
    {
        context = new AlphaAnimation(f, f1);
        context.setInterpolator(DECELERATE_CUBIC);
        context.setDuration(220L);
        return context;
    }

    static Animation makeOpenCloseAnimation(Context context, float f, float f1, float f2, float f3)
    {
        context = new AnimationSet(false);
        Object obj = new ScaleAnimation(f, f1, f, f1, 1, 0.5F, 1, 0.5F);
        ((ScaleAnimation) (obj)).setInterpolator(DECELERATE_QUINT);
        ((ScaleAnimation) (obj)).setDuration(220L);
        context.addAnimation(((Animation) (obj)));
        obj = new AlphaAnimation(f2, f3);
        ((AlphaAnimation) (obj)).setInterpolator(DECELERATE_CUBIC);
        ((AlphaAnimation) (obj)).setDuration(220L);
        context.addAnimation(((Animation) (obj)));
        return context;
    }

    static boolean modifiesAlpha(Animation animation)
    {
        boolean flag = true;
        if(!(animation instanceof AlphaAnimation)) goto _L2; else goto _L1
_L1:
        boolean flag1 = flag;
_L4:
        return flag1;
_L2:
        if(animation instanceof AnimationSet)
        {
            animation = ((AnimationSet)animation).getAnimations();
            for(int i = 0; i < animation.size(); i++)
            {
                flag1 = flag;
                if(animation.get(i) instanceof AlphaAnimation)
                    continue; /* Loop/switch isn't completed */
            }

        }
        flag1 = false;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public static int reverseTransit(int i)
    {
        boolean flag = false;
        i;
        JVM INSTR lookupswitch 3: default 36
    //                   4097: 40
    //                   4099: 54
    //                   8194: 47;
           goto _L1 _L2 _L3 _L4
_L1:
        i = ((flag) ? 1 : 0);
_L6:
        return i;
_L2:
        i = 8194;
        continue; /* Loop/switch isn't completed */
_L4:
        i = 4097;
        continue; /* Loop/switch isn't completed */
_L3:
        i = 4099;
        if(true) goto _L6; else goto _L5
_L5:
    }

    private void setHWLayerAnimListenerIfAlpha(View view, Animation animation)
    {
_L2:
        return;
        if(view == null || animation == null || !shouldRunOnHWLayer(view, animation)) goto _L2; else goto _L1
_L1:
        android.view.animation.Animation.AnimationListener animationlistener = null;
        android.view.animation.Animation.AnimationListener animationlistener1;
        if(sAnimationListenerField == null)
        {
            sAnimationListenerField = android/view/animation/Animation.getDeclaredField("mListener");
            sAnimationListenerField.setAccessible(true);
        }
        animationlistener1 = (android.view.animation.Animation.AnimationListener)sAnimationListenerField.get(animation);
        animationlistener = animationlistener1;
_L4:
        animation.setAnimationListener(new AnimateOnHWLayerIfNeededListener(view, animation, animationlistener));
        if(true) goto _L2; else goto _L3
_L3:
        Object obj;
        obj;
        Log.e("FragmentManager", "No field with the name mListener is found in Animation class", ((Throwable) (obj)));
          goto _L4
        obj;
        Log.e("FragmentManager", "Cannot access Animation's mListener field", ((Throwable) (obj)));
          goto _L4
    }

    static boolean shouldRunOnHWLayer(View view, Animation animation)
    {
        boolean flag;
        if(android.os.Build.VERSION.SDK_INT >= 19 && ViewCompat.getLayerType(view) == 0 && ViewCompat.hasOverlappingRendering(view) && modifiesAlpha(animation))
            flag = true;
        else
            flag = false;
        return flag;
    }

    private void throwException(RuntimeException runtimeexception)
    {
        Log.e("FragmentManager", runtimeexception.getMessage());
        Log.e("FragmentManager", "Activity state:");
        Object obj = new PrintWriter(new LogWriter("FragmentManager"));
        if(mHost != null)
            try
            {
                mHost.onDump("  ", null, ((PrintWriter) (obj)), new String[0]);
            }
            // Misplaced declaration of an exception variable
            catch(Object obj)
            {
                Log.e("FragmentManager", "Failed dumping state", ((Throwable) (obj)));
            }
        else
            try
            {
                dump("  ", null, ((PrintWriter) (obj)), new String[0]);
            }
            catch(Exception exception)
            {
                Log.e("FragmentManager", "Failed dumping state", exception);
            }
        throw runtimeexception;
    }

    public static int transitToStyleIndex(int i, boolean flag)
    {
        byte byte0 = -1;
        i;
        JVM INSTR lookupswitch 3: default 36
    //                   4097: 40
    //                   4099: 68
    //                   8194: 54;
           goto _L1 _L2 _L3 _L4
_L1:
        i = byte0;
_L6:
        return i;
_L2:
        if(flag)
            i = 1;
        else
            i = 2;
        continue; /* Loop/switch isn't completed */
_L4:
        if(flag)
            i = 3;
        else
            i = 4;
        continue; /* Loop/switch isn't completed */
_L3:
        if(flag)
            i = 5;
        else
            i = 6;
        if(true) goto _L6; else goto _L5
_L5:
    }

    void addBackStackState(BackStackRecord backstackrecord)
    {
        if(mBackStack == null)
            mBackStack = new ArrayList();
        mBackStack.add(backstackrecord);
        reportBackStackChanged();
    }

    public void addFragment(Fragment fragment, boolean flag)
    {
        if(mAdded == null)
            mAdded = new ArrayList();
        if(DEBUG)
            Log.v("FragmentManager", (new StringBuilder()).append("add: ").append(fragment).toString());
        makeActive(fragment);
        if(!fragment.mDetached)
        {
            if(mAdded.contains(fragment))
                throw new IllegalStateException((new StringBuilder()).append("Fragment already added: ").append(fragment).toString());
            mAdded.add(fragment);
            fragment.mAdded = true;
            fragment.mRemoving = false;
            if(fragment.mHasMenu && fragment.mMenuVisible)
                mNeedMenuInvalidate = true;
            if(flag)
                moveToState(fragment);
        }
    }

    public void addOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener onbackstackchangedlistener)
    {
        if(mBackStackChangeListeners == null)
            mBackStackChangeListeners = new ArrayList();
        mBackStackChangeListeners.add(onbackstackchangedlistener);
    }

    public int allocBackStackIndex(BackStackRecord backstackrecord)
    {
        this;
        JVM INSTR monitorenter ;
        if(mAvailBackStackIndices != null && mAvailBackStackIndices.size() > 0) goto _L2; else goto _L1
_L1:
        int i;
        if(mBackStackIndices == null)
        {
            ArrayList arraylist = JVM INSTR new #338 <Class ArrayList>;
            arraylist.ArrayList();
            mBackStackIndices = arraylist;
        }
        i = mBackStackIndices.size();
        if(DEBUG)
        {
            StringBuilder stringbuilder = JVM INSTR new #167 <Class StringBuilder>;
            stringbuilder.StringBuilder();
            Log.v("FragmentManager", stringbuilder.append("Setting back stack index ").append(i).append(" to ").append(backstackrecord).toString());
        }
        mBackStackIndices.add(backstackrecord);
        this;
        JVM INSTR monitorexit ;
_L4:
        return i;
_L2:
        i = ((Integer)mAvailBackStackIndices.remove(mAvailBackStackIndices.size() - 1)).intValue();
        if(DEBUG)
        {
            StringBuilder stringbuilder1 = JVM INSTR new #167 <Class StringBuilder>;
            stringbuilder1.StringBuilder();
            Log.v("FragmentManager", stringbuilder1.append("Adding back stack index ").append(i).append(" with ").append(backstackrecord).toString());
        }
        mBackStackIndices.set(i, backstackrecord);
        this;
        JVM INSTR monitorexit ;
        if(true) goto _L4; else goto _L3
_L3:
        backstackrecord;
        this;
        JVM INSTR monitorexit ;
        throw backstackrecord;
    }

    public void attachController(FragmentHostCallback fragmenthostcallback, FragmentContainer fragmentcontainer, Fragment fragment)
    {
        if(mHost != null)
        {
            throw new IllegalStateException("Already attached");
        } else
        {
            mHost = fragmenthostcallback;
            mContainer = fragmentcontainer;
            mParent = fragment;
            return;
        }
    }

    public void attachFragment(Fragment fragment, int i, int j)
    {
        if(DEBUG)
            Log.v("FragmentManager", (new StringBuilder()).append("attach: ").append(fragment).toString());
        if(fragment.mDetached)
        {
            fragment.mDetached = false;
            if(!fragment.mAdded)
            {
                if(mAdded == null)
                    mAdded = new ArrayList();
                if(mAdded.contains(fragment))
                    throw new IllegalStateException((new StringBuilder()).append("Fragment already added: ").append(fragment).toString());
                if(DEBUG)
                    Log.v("FragmentManager", (new StringBuilder()).append("add from attach: ").append(fragment).toString());
                mAdded.add(fragment);
                fragment.mAdded = true;
                if(fragment.mHasMenu && fragment.mMenuVisible)
                    mNeedMenuInvalidate = true;
                moveToState(fragment, mCurState, i, j, false);
            }
        }
    }

    public FragmentTransaction beginTransaction()
    {
        return new BackStackRecord(this);
    }

    public void detachFragment(Fragment fragment, int i, int j)
    {
        if(DEBUG)
            Log.v("FragmentManager", (new StringBuilder()).append("detach: ").append(fragment).toString());
        if(!fragment.mDetached)
        {
            fragment.mDetached = true;
            if(fragment.mAdded)
            {
                if(mAdded != null)
                {
                    if(DEBUG)
                        Log.v("FragmentManager", (new StringBuilder()).append("remove from detach: ").append(fragment).toString());
                    mAdded.remove(fragment);
                }
                if(fragment.mHasMenu && fragment.mMenuVisible)
                    mNeedMenuInvalidate = true;
                fragment.mAdded = false;
                moveToState(fragment, 1, i, j, false);
            }
        }
    }

    public void dispatchActivityCreated()
    {
        mStateSaved = false;
        moveToState(2, false);
    }

    public void dispatchConfigurationChanged(Configuration configuration)
    {
        if(mAdded != null)
        {
            for(int i = 0; i < mAdded.size(); i++)
            {
                Fragment fragment = (Fragment)mAdded.get(i);
                if(fragment != null)
                    fragment.performConfigurationChanged(configuration);
            }

        }
    }

    public boolean dispatchContextItemSelected(MenuItem menuitem)
    {
        int i;
        if(mAdded == null)
            break MISSING_BLOCK_LABEL_56;
        i = 0;
_L3:
        Fragment fragment;
        if(i >= mAdded.size())
            break MISSING_BLOCK_LABEL_56;
        fragment = (Fragment)mAdded.get(i);
        if(fragment == null || !fragment.performContextItemSelected(menuitem)) goto _L2; else goto _L1
_L1:
        boolean flag = true;
_L4:
        return flag;
_L2:
        i++;
          goto _L3
        flag = false;
          goto _L4
    }

    public void dispatchCreate()
    {
        mStateSaved = false;
        moveToState(1, false);
    }

    public boolean dispatchCreateOptionsMenu(Menu menu, MenuInflater menuinflater)
    {
        boolean flag = false;
        boolean flag1 = false;
        ArrayList arraylist = null;
        ArrayList arraylist1 = null;
        if(mAdded != null)
        {
            int i = 0;
            do
            {
                arraylist = arraylist1;
                flag = flag1;
                if(i >= mAdded.size())
                    break;
                Fragment fragment = (Fragment)mAdded.get(i);
                arraylist = arraylist1;
                flag = flag1;
                if(fragment != null)
                {
                    arraylist = arraylist1;
                    flag = flag1;
                    if(fragment.performCreateOptionsMenu(menu, menuinflater))
                    {
                        flag = true;
                        arraylist = arraylist1;
                        if(arraylist1 == null)
                            arraylist = new ArrayList();
                        arraylist.add(fragment);
                    }
                }
                i++;
                arraylist1 = arraylist;
                flag1 = flag;
            } while(true);
        }
        if(mCreatedMenus != null)
        {
            for(int j = 0; j < mCreatedMenus.size(); j++)
            {
                menu = (Fragment)mCreatedMenus.get(j);
                if(arraylist == null || !arraylist.contains(menu))
                    menu.onDestroyOptionsMenu();
            }

        }
        mCreatedMenus = arraylist;
        return flag;
    }

    public void dispatchDestroy()
    {
        mDestroyed = true;
        execPendingActions();
        moveToState(0, false);
        mHost = null;
        mContainer = null;
        mParent = null;
    }

    public void dispatchDestroyView()
    {
        moveToState(1, false);
    }

    public void dispatchLowMemory()
    {
        if(mAdded != null)
        {
            for(int i = 0; i < mAdded.size(); i++)
            {
                Fragment fragment = (Fragment)mAdded.get(i);
                if(fragment != null)
                    fragment.performLowMemory();
            }

        }
    }

    public boolean dispatchOptionsItemSelected(MenuItem menuitem)
    {
        int i;
        if(mAdded == null)
            break MISSING_BLOCK_LABEL_56;
        i = 0;
_L3:
        Fragment fragment;
        if(i >= mAdded.size())
            break MISSING_BLOCK_LABEL_56;
        fragment = (Fragment)mAdded.get(i);
        if(fragment == null || !fragment.performOptionsItemSelected(menuitem)) goto _L2; else goto _L1
_L1:
        boolean flag = true;
_L4:
        return flag;
_L2:
        i++;
          goto _L3
        flag = false;
          goto _L4
    }

    public void dispatchOptionsMenuClosed(Menu menu)
    {
        if(mAdded != null)
        {
            for(int i = 0; i < mAdded.size(); i++)
            {
                Fragment fragment = (Fragment)mAdded.get(i);
                if(fragment != null)
                    fragment.performOptionsMenuClosed(menu);
            }

        }
    }

    public void dispatchPause()
    {
        moveToState(4, false);
    }

    public boolean dispatchPrepareOptionsMenu(Menu menu)
    {
        boolean flag = false;
        boolean flag1 = false;
        if(mAdded != null)
        {
            int i = 0;
            do
            {
                flag = flag1;
                if(i >= mAdded.size())
                    break;
                Fragment fragment = (Fragment)mAdded.get(i);
                flag = flag1;
                if(fragment != null)
                {
                    flag = flag1;
                    if(fragment.performPrepareOptionsMenu(menu))
                        flag = true;
                }
                i++;
                flag1 = flag;
            } while(true);
        }
        return flag;
    }

    public void dispatchReallyStop()
    {
        moveToState(2, false);
    }

    public void dispatchResume()
    {
        mStateSaved = false;
        moveToState(5, false);
    }

    public void dispatchStart()
    {
        mStateSaved = false;
        moveToState(4, false);
    }

    public void dispatchStop()
    {
        mStateSaved = true;
        moveToState(3, false);
    }

    public void dump(String s, FileDescriptor filedescriptor, PrintWriter printwriter, String as[])
    {
        String s1 = (new StringBuilder()).append(s).append("    ").toString();
        if(mActive != null)
        {
            int i = mActive.size();
            if(i > 0)
            {
                printwriter.print(s);
                printwriter.print("Active Fragments in ");
                printwriter.print(Integer.toHexString(System.identityHashCode(this)));
                printwriter.println(":");
                for(int k1 = 0; k1 < i; k1++)
                {
                    Fragment fragment = (Fragment)mActive.get(k1);
                    printwriter.print(s);
                    printwriter.print("  #");
                    printwriter.print(k1);
                    printwriter.print(": ");
                    printwriter.println(fragment);
                    if(fragment != null)
                        fragment.dump(s1, filedescriptor, printwriter, as);
                }

            }
        }
        if(mAdded != null)
        {
            int j = mAdded.size();
            if(j > 0)
            {
                printwriter.print(s);
                printwriter.println("Added Fragments:");
                for(int l1 = 0; l1 < j; l1++)
                {
                    Fragment fragment1 = (Fragment)mAdded.get(l1);
                    printwriter.print(s);
                    printwriter.print("  #");
                    printwriter.print(l1);
                    printwriter.print(": ");
                    printwriter.println(fragment1.toString());
                }

            }
        }
        if(mCreatedMenus != null)
        {
            int k = mCreatedMenus.size();
            if(k > 0)
            {
                printwriter.print(s);
                printwriter.println("Fragments Created Menus:");
                for(int i2 = 0; i2 < k; i2++)
                {
                    Fragment fragment2 = (Fragment)mCreatedMenus.get(i2);
                    printwriter.print(s);
                    printwriter.print("  #");
                    printwriter.print(i2);
                    printwriter.print(": ");
                    printwriter.println(fragment2.toString());
                }

            }
        }
        if(mBackStack != null)
        {
            int l = mBackStack.size();
            if(l > 0)
            {
                printwriter.print(s);
                printwriter.println("Back Stack:");
                for(int j2 = 0; j2 < l; j2++)
                {
                    BackStackRecord backstackrecord = (BackStackRecord)mBackStack.get(j2);
                    printwriter.print(s);
                    printwriter.print("  #");
                    printwriter.print(j2);
                    printwriter.print(": ");
                    printwriter.println(backstackrecord.toString());
                    backstackrecord.dump(s1, filedescriptor, printwriter, as);
                }

            }
        }
        this;
        JVM INSTR monitorenter ;
        if(mBackStackIndices == null) goto _L2; else goto _L1
_L1:
        int i1 = mBackStackIndices.size();
        if(i1 <= 0) goto _L2; else goto _L3
_L3:
        printwriter.print(s);
        printwriter.println("Back Stack Indices:");
        int k2 = 0;
_L4:
        if(k2 >= i1)
            break; /* Loop/switch isn't completed */
        filedescriptor = (BackStackRecord)mBackStackIndices.get(k2);
        printwriter.print(s);
        printwriter.print("  #");
        printwriter.print(k2);
        printwriter.print(": ");
        printwriter.println(filedescriptor);
        k2++;
        if(true) goto _L4; else goto _L2
_L2:
        if(mAvailBackStackIndices != null && mAvailBackStackIndices.size() > 0)
        {
            printwriter.print(s);
            printwriter.print("mAvailBackStackIndices: ");
            printwriter.println(Arrays.toString(mAvailBackStackIndices.toArray()));
        }
        this;
        JVM INSTR monitorexit ;
        if(mPendingActions != null)
        {
            int j1 = mPendingActions.size();
            if(j1 > 0)
            {
                printwriter.print(s);
                printwriter.println("Pending Actions:");
                for(int l2 = 0; l2 < j1; l2++)
                {
                    filedescriptor = (Runnable)mPendingActions.get(l2);
                    printwriter.print(s);
                    printwriter.print("  #");
                    printwriter.print(l2);
                    printwriter.print(": ");
                    printwriter.println(filedescriptor);
                }

            }
        }
        break MISSING_BLOCK_LABEL_688;
        s;
        this;
        JVM INSTR monitorexit ;
        throw s;
        printwriter.print(s);
        printwriter.println("FragmentManager misc state:");
        printwriter.print(s);
        printwriter.print("  mHost=");
        printwriter.println(mHost);
        printwriter.print(s);
        printwriter.print("  mContainer=");
        printwriter.println(mContainer);
        if(mParent != null)
        {
            printwriter.print(s);
            printwriter.print("  mParent=");
            printwriter.println(mParent);
        }
        printwriter.print(s);
        printwriter.print("  mCurState=");
        printwriter.print(mCurState);
        printwriter.print(" mStateSaved=");
        printwriter.print(mStateSaved);
        printwriter.print(" mDestroyed=");
        printwriter.println(mDestroyed);
        if(mNeedMenuInvalidate)
        {
            printwriter.print(s);
            printwriter.print("  mNeedMenuInvalidate=");
            printwriter.println(mNeedMenuInvalidate);
        }
        if(mNoTransactionsBecause != null)
        {
            printwriter.print(s);
            printwriter.print("  mNoTransactionsBecause=");
            printwriter.println(mNoTransactionsBecause);
        }
        if(mAvailIndices != null && mAvailIndices.size() > 0)
        {
            printwriter.print(s);
            printwriter.print("  mAvailIndices: ");
            printwriter.println(Arrays.toString(mAvailIndices.toArray()));
        }
        return;
    }

    public void enqueueAction(Runnable runnable, boolean flag)
    {
        if(!flag)
            checkStateLoss();
        this;
        JVM INSTR monitorenter ;
        if(mDestroyed || mHost == null)
        {
            runnable = JVM INSTR new #158 <Class IllegalStateException>;
            runnable.IllegalStateException("Activity has been destroyed");
            throw runnable;
        }
        break MISSING_BLOCK_LABEL_42;
        runnable;
        this;
        JVM INSTR monitorexit ;
        throw runnable;
        if(mPendingActions == null)
        {
            ArrayList arraylist = JVM INSTR new #338 <Class ArrayList>;
            arraylist.ArrayList();
            mPendingActions = arraylist;
        }
        mPendingActions.add(runnable);
        if(mPendingActions.size() == 1)
        {
            mHost.getHandler().removeCallbacks(mExecCommit);
            mHost.getHandler().post(mExecCommit);
        }
        this;
        JVM INSTR monitorexit ;
    }

    public boolean execPendingActions()
    {
        if(mExecutingActions)
            throw new IllegalStateException("Recursive entry to executePendingTransactions");
        if(Looper.myLooper() != mHost.getHandler().getLooper())
            throw new IllegalStateException("Must be called from main thread of process");
        boolean flag = false;
_L2:
        this;
        JVM INSTR monitorenter ;
        if(mPendingActions != null && mPendingActions.size() != 0)
            break MISSING_BLOCK_LABEL_143;
        this;
        JVM INSTR monitorexit ;
        int i;
        if(!mHavePendingDeferredStart)
            break MISSING_BLOCK_LABEL_276;
        i = 0;
        for(int j = 0; j < mActive.size();)
        {
            Fragment fragment = (Fragment)mActive.get(j);
            int l = i;
            if(fragment != null)
            {
                l = i;
                if(fragment.mLoaderManager != null)
                    l = i | fragment.mLoaderManager.hasRunningLoaders();
            }
            j++;
            i = l;
        }

        break; /* Loop/switch isn't completed */
        int k;
        k = mPendingActions.size();
        if(mTmpActions == null || mTmpActions.length < k)
            mTmpActions = new Runnable[k];
        mPendingActions.toArray(mTmpActions);
        mPendingActions.clear();
        mHost.getHandler().removeCallbacks(mExecCommit);
        this;
        JVM INSTR monitorexit ;
        mExecutingActions = true;
        for(i = 0; i < k; i++)
        {
            mTmpActions[i].run();
            mTmpActions[i] = null;
        }

        break MISSING_BLOCK_LABEL_253;
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
        mExecutingActions = false;
        flag = true;
        if(true) goto _L2; else goto _L1
_L1:
        if(i == 0)
        {
            mHavePendingDeferredStart = false;
            startPendingDeferredFragments();
        }
        return flag;
    }

    public boolean executePendingTransactions()
    {
        return execPendingActions();
    }

    public Fragment findFragmentById(int i)
    {
        if(mAdded == null) goto _L2; else goto _L1
_L1:
        int j = mAdded.size() - 1;
_L6:
        if(j < 0) goto _L2; else goto _L3
_L3:
        Fragment fragment = (Fragment)mAdded.get(j);
        if(fragment == null || fragment.mFragmentId != i) goto _L5; else goto _L4
_L4:
        return fragment;
_L5:
        j--;
          goto _L6
_L2:
        if(mActive == null)
            break MISSING_BLOCK_LABEL_110;
        j = mActive.size() - 1;
_L10:
        if(j < 0) goto _L8; else goto _L7
_L7:
        Fragment fragment1;
        fragment1 = (Fragment)mActive.get(j);
        if(fragment1 == null)
            continue; /* Loop/switch isn't completed */
        fragment = fragment1;
        if(fragment1.mFragmentId == i) goto _L4; else goto _L9
_L9:
        j--;
          goto _L10
_L8:
        fragment = null;
          goto _L4
    }

    public Fragment findFragmentByTag(String s)
    {
        if(mAdded == null || s == null) goto _L2; else goto _L1
_L1:
        int i = mAdded.size() - 1;
_L6:
        if(i < 0) goto _L2; else goto _L3
_L3:
        Fragment fragment = (Fragment)mAdded.get(i);
        if(fragment == null || !s.equals(fragment.mTag)) goto _L5; else goto _L4
_L4:
        return fragment;
_L5:
        i--;
          goto _L6
_L2:
        if(mActive == null || s == null)
            break MISSING_BLOCK_LABEL_124;
        i = mActive.size() - 1;
_L10:
        if(i < 0) goto _L8; else goto _L7
_L7:
        Fragment fragment1;
        fragment1 = (Fragment)mActive.get(i);
        if(fragment1 == null)
            continue; /* Loop/switch isn't completed */
        fragment = fragment1;
        if(s.equals(fragment1.mTag)) goto _L4; else goto _L9
_L9:
        i--;
          goto _L10
_L8:
        fragment = null;
          goto _L4
    }

    public Fragment findFragmentByWho(String s)
    {
        int i;
        if(mActive == null || s == null)
            break MISSING_BLOCK_LABEL_61;
        i = mActive.size() - 1;
_L3:
        if(i < 0) goto _L2; else goto _L1
_L1:
        Fragment fragment = (Fragment)mActive.get(i);
        if(fragment == null)
            continue; /* Loop/switch isn't completed */
        fragment = fragment.findFragmentByWho(s);
        if(fragment == null)
            continue; /* Loop/switch isn't completed */
        s = fragment;
_L4:
        return s;
        i--;
          goto _L3
_L2:
        s = null;
          goto _L4
    }

    public void freeBackStackIndex(int i)
    {
        this;
        JVM INSTR monitorenter ;
        mBackStackIndices.set(i, null);
        if(mAvailBackStackIndices == null)
        {
            ArrayList arraylist = JVM INSTR new #338 <Class ArrayList>;
            arraylist.ArrayList();
            mAvailBackStackIndices = arraylist;
        }
        if(DEBUG)
        {
            StringBuilder stringbuilder = JVM INSTR new #167 <Class StringBuilder>;
            stringbuilder.StringBuilder();
            Log.v("FragmentManager", stringbuilder.append("Freeing back stack index ").append(i).toString());
        }
        mAvailBackStackIndices.add(Integer.valueOf(i));
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
    }

    public FragmentManager.BackStackEntry getBackStackEntryAt(int i)
    {
        return (FragmentManager.BackStackEntry)mBackStack.get(i);
    }

    public int getBackStackEntryCount()
    {
        int i;
        if(mBackStack != null)
            i = mBackStack.size();
        else
            i = 0;
        return i;
    }

    public Fragment getFragment(Bundle bundle, String s)
    {
        int i = bundle.getInt(s, -1);
        if(i != -1) goto _L2; else goto _L1
_L1:
        bundle = null;
_L4:
        return bundle;
_L2:
        if(i >= mActive.size())
            throwException(new IllegalStateException((new StringBuilder()).append("Fragment no longer exists for key ").append(s).append(": index ").append(i).toString()));
        Fragment fragment = (Fragment)mActive.get(i);
        bundle = fragment;
        if(fragment == null)
        {
            throwException(new IllegalStateException((new StringBuilder()).append("Fragment no longer exists for key ").append(s).append(": index ").append(i).toString()));
            bundle = fragment;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public List getFragments()
    {
        return mActive;
    }

    LayoutInflaterFactory getLayoutInflaterFactory()
    {
        return this;
    }

    public void hideFragment(Fragment fragment, int i, int j)
    {
        if(DEBUG)
            Log.v("FragmentManager", (new StringBuilder()).append("hide: ").append(fragment).toString());
        if(!fragment.mHidden)
        {
            fragment.mHidden = true;
            if(fragment.mView != null)
            {
                Animation animation = loadAnimation(fragment, i, false, j);
                if(animation != null)
                {
                    setHWLayerAnimListenerIfAlpha(fragment.mView, animation);
                    fragment.mView.startAnimation(animation);
                }
                fragment.mView.setVisibility(8);
            }
            if(fragment.mAdded && fragment.mHasMenu && fragment.mMenuVisible)
                mNeedMenuInvalidate = true;
            fragment.onHiddenChanged(true);
        }
    }

    public boolean isDestroyed()
    {
        return mDestroyed;
    }

    Animation loadAnimation(Fragment fragment, int i, boolean flag, int j)
    {
        Animation animation = fragment.onCreateAnimation(i, flag, fragment.mNextAnim);
        if(animation == null) goto _L2; else goto _L1
_L1:
        fragment = animation;
_L4:
        return fragment;
_L2:
        if(fragment.mNextAnim != 0)
        {
            fragment = AnimationUtils.loadAnimation(mHost.getContext(), fragment.mNextAnim);
            if(fragment != null)
                continue; /* Loop/switch isn't completed */
        }
        if(i == 0)
        {
            fragment = null;
            continue; /* Loop/switch isn't completed */
        }
        i = transitToStyleIndex(i, flag);
        if(i >= 0)
            break; /* Loop/switch isn't completed */
        fragment = null;
        if(true) goto _L4; else goto _L3
_L3:
        switch(i)
        {
        default:
            i = j;
            if(j == 0)
            {
                i = j;
                if(mHost.onHasWindowAnimations())
                    i = mHost.onGetWindowAnimations();
            }
            if(i == 0)
                fragment = null;
            else
                fragment = null;
            break;

        case 1: // '\001'
            fragment = makeOpenCloseAnimation(mHost.getContext(), 1.125F, 1.0F, 0.0F, 1.0F);
            break;

        case 2: // '\002'
            fragment = makeOpenCloseAnimation(mHost.getContext(), 1.0F, 0.975F, 1.0F, 0.0F);
            break;

        case 3: // '\003'
            fragment = makeOpenCloseAnimation(mHost.getContext(), 0.975F, 1.0F, 0.0F, 1.0F);
            break;

        case 4: // '\004'
            fragment = makeOpenCloseAnimation(mHost.getContext(), 1.0F, 1.075F, 1.0F, 0.0F);
            break;

        case 5: // '\005'
            fragment = makeFadeAnimation(mHost.getContext(), 0.0F, 1.0F);
            break;

        case 6: // '\006'
            fragment = makeFadeAnimation(mHost.getContext(), 1.0F, 0.0F);
            break;
        }
        if(true) goto _L4; else goto _L5
_L5:
    }

    void makeActive(Fragment fragment)
    {
        if(fragment.mIndex < 0) goto _L2; else goto _L1
_L1:
        return;
_L2:
        if(mAvailIndices == null || mAvailIndices.size() <= 0)
        {
            if(mActive == null)
                mActive = new ArrayList();
            fragment.setIndex(mActive.size(), mParent);
            mActive.add(fragment);
        } else
        {
            fragment.setIndex(((Integer)mAvailIndices.remove(mAvailIndices.size() - 1)).intValue(), mParent);
            mActive.set(fragment.mIndex, fragment);
        }
        if(DEBUG)
            Log.v("FragmentManager", (new StringBuilder()).append("Allocated fragment index ").append(fragment).toString());
        if(true) goto _L1; else goto _L3
_L3:
    }

    void makeInactive(Fragment fragment)
    {
        if(fragment.mIndex >= 0)
        {
            if(DEBUG)
                Log.v("FragmentManager", (new StringBuilder()).append("Freeing fragment index ").append(fragment).toString());
            mActive.set(fragment.mIndex, null);
            if(mAvailIndices == null)
                mAvailIndices = new ArrayList();
            mAvailIndices.add(Integer.valueOf(fragment.mIndex));
            mHost.inactivateFragment(fragment.mWho);
            fragment.initState();
        }
    }

    void moveToState(int i, int j, int k, boolean flag)
    {
        if(mHost == null && i != 0)
            throw new IllegalStateException("No host");
        if(flag || mCurState != i) goto _L2; else goto _L1
_L1:
        return;
_L2:
        mCurState = i;
        if(mActive != null)
        {
            boolean flag1 = false;
            for(int l = 0; l < mActive.size();)
            {
                Fragment fragment = (Fragment)mActive.get(l);
                boolean flag2 = flag1;
                if(fragment != null)
                {
                    moveToState(fragment, i, j, k, false);
                    flag2 = flag1;
                    if(fragment.mLoaderManager != null)
                        flag2 = flag1 | fragment.mLoaderManager.hasRunningLoaders();
                }
                l++;
                flag1 = flag2;
            }

            if(!flag1)
                startPendingDeferredFragments();
            if(mNeedMenuInvalidate && mHost != null && mCurState == 5)
            {
                mHost.onSupportInvalidateOptionsMenu();
                mNeedMenuInvalidate = false;
            }
        }
        if(true) goto _L1; else goto _L3
_L3:
    }

    void moveToState(int i, boolean flag)
    {
        moveToState(i, 0, 0, flag);
    }

    void moveToState(Fragment fragment)
    {
        moveToState(fragment, mCurState, 0, 0, false);
    }

    void moveToState(Fragment fragment, int i, int j, int k, boolean flag)
    {
        int l;
label0:
        {
            if(fragment.mAdded)
            {
                l = i;
                if(!fragment.mDetached)
                    break label0;
            }
            l = i;
            if(i > 1)
                l = 1;
        }
        int j1 = l;
        if(fragment.mRemoving)
        {
            j1 = l;
            if(l > fragment.mState)
                j1 = fragment.mState;
        }
        i = j1;
        if(fragment.mDeferStart)
        {
            i = j1;
            if(fragment.mState < 4)
            {
                i = j1;
                if(j1 > 3)
                    i = 3;
            }
        }
        if(fragment.mState >= i) goto _L2; else goto _L1
_L1:
        if(!fragment.mFromLayout || fragment.mInLayout) goto _L4; else goto _L3
_L3:
        return;
_L4:
        int i1;
        int k1;
        int l1;
        if(fragment.mAnimatingAway != null)
        {
            fragment.mAnimatingAway = null;
            moveToState(fragment, fragment.mStateAfterAnimating, 0, 0, true);
        }
        k1 = i;
        l1 = i;
        i1 = i;
        fragment.mState;
        JVM INSTR tableswitch 0 4: default 184
    //                   0 261
    //                   1 645
    //                   2 979
    //                   3 979
    //                   4 1029;
           goto _L5 _L6 _L7 _L8 _L8 _L9
_L6:
        break; /* Loop/switch isn't completed */
_L5:
        k1 = i;
_L11:
        if(fragment.mState != k1)
        {
            Log.w("FragmentManager", (new StringBuilder()).append("moveToState: Fragment state for ").append(fragment).append(" not updated inline; ").append("expected state ").append(k1).append(" found ").append(fragment.mState).toString());
            fragment.mState = k1;
        }
        if(true) goto _L3; else goto _L10
_L10:
        if(DEBUG)
            Log.v("FragmentManager", (new StringBuilder()).append("moveto CREATED: ").append(fragment).toString());
        i1 = i;
        if(fragment.mSavedFragmentState != null)
        {
            fragment.mSavedFragmentState.setClassLoader(mHost.getContext().getClassLoader());
            fragment.mSavedViewState = fragment.mSavedFragmentState.getSparseParcelableArray("android:view_state");
            fragment.mTarget = getFragment(fragment.mSavedFragmentState, "android:target_state");
            if(fragment.mTarget != null)
                fragment.mTargetRequestCode = fragment.mSavedFragmentState.getInt("android:target_req_state", 0);
            fragment.mUserVisibleHint = fragment.mSavedFragmentState.getBoolean("android:user_visible_hint", true);
            i1 = i;
            if(!fragment.mUserVisibleHint)
            {
                fragment.mDeferStart = true;
                i1 = i;
                if(i > 3)
                    i1 = 3;
            }
        }
        fragment.mHost = mHost;
        fragment.mParentFragment = mParent;
        FragmentManagerImpl fragmentmanagerimpl;
        if(mParent != null)
            fragmentmanagerimpl = mParent.mChildFragmentManager;
        else
            fragmentmanagerimpl = mHost.getFragmentManagerImpl();
        fragment.mFragmentManager = fragmentmanagerimpl;
        fragment.mCalled = false;
        fragment.onAttach(mHost.getContext());
        if(!fragment.mCalled)
            throw new SuperNotCalledException((new StringBuilder()).append("Fragment ").append(fragment).append(" did not call through to super.onAttach()").toString());
        if(fragment.mParentFragment == null)
            mHost.onAttachFragment(fragment);
        if(!fragment.mRetaining)
            fragment.performCreate(fragment.mSavedFragmentState);
        fragment.mRetaining = false;
        k1 = i1;
        if(fragment.mFromLayout)
        {
            fragment.mView = fragment.performCreateView(fragment.getLayoutInflater(fragment.mSavedFragmentState), null, fragment.mSavedFragmentState);
            if(fragment.mView != null)
            {
                fragment.mInnerView = fragment.mView;
                ViewGroup viewgroup;
                Object obj;
                if(android.os.Build.VERSION.SDK_INT >= 11)
                    ViewCompat.setSaveFromParentEnabled(fragment.mView, false);
                else
                    fragment.mView = NoSaveStateFrameLayout.wrap(fragment.mView);
                if(fragment.mHidden)
                    fragment.mView.setVisibility(8);
                fragment.onViewCreated(fragment.mView, fragment.mSavedFragmentState);
                k1 = i1;
            } else
            {
                fragment.mInnerView = null;
                k1 = i1;
            }
        }
_L7:
        l1 = k1;
        if(k1 > 1)
        {
            if(DEBUG)
                Log.v("FragmentManager", (new StringBuilder()).append("moveto ACTIVITY_CREATED: ").append(fragment).toString());
            if(!fragment.mFromLayout)
            {
                viewgroup = null;
                if(fragment.mContainerId != 0)
                {
                    obj = (ViewGroup)mContainer.onFindViewById(fragment.mContainerId);
                    viewgroup = ((ViewGroup) (obj));
                    if(obj == null)
                    {
                        viewgroup = ((ViewGroup) (obj));
                        if(!fragment.mRestored)
                        {
                            throwException(new IllegalArgumentException((new StringBuilder()).append("No view found for id 0x").append(Integer.toHexString(fragment.mContainerId)).append(" (").append(fragment.getResources().getResourceName(fragment.mContainerId)).append(") for fragment ").append(fragment).toString()));
                            viewgroup = ((ViewGroup) (obj));
                        }
                    }
                }
                fragment.mContainer = viewgroup;
                fragment.mView = fragment.performCreateView(fragment.getLayoutInflater(fragment.mSavedFragmentState), viewgroup, fragment.mSavedFragmentState);
                if(fragment.mView != null)
                {
                    fragment.mInnerView = fragment.mView;
                    if(android.os.Build.VERSION.SDK_INT >= 11)
                        ViewCompat.setSaveFromParentEnabled(fragment.mView, false);
                    else
                        fragment.mView = NoSaveStateFrameLayout.wrap(fragment.mView);
                    if(viewgroup != null)
                    {
                        obj = loadAnimation(fragment, j, true, k);
                        if(obj != null)
                        {
                            setHWLayerAnimListenerIfAlpha(fragment.mView, ((Animation) (obj)));
                            fragment.mView.startAnimation(((Animation) (obj)));
                        }
                        viewgroup.addView(fragment.mView);
                    }
                    if(fragment.mHidden)
                        fragment.mView.setVisibility(8);
                    fragment.onViewCreated(fragment.mView, fragment.mSavedFragmentState);
                } else
                {
                    fragment.mInnerView = null;
                }
            }
            fragment.performActivityCreated(fragment.mSavedFragmentState);
            if(fragment.mView != null)
                fragment.restoreViewState(fragment.mSavedFragmentState);
            fragment.mSavedFragmentState = null;
            l1 = k1;
        }
_L8:
        i1 = l1;
        if(l1 > 3)
        {
            if(DEBUG)
                Log.v("FragmentManager", (new StringBuilder()).append("moveto STARTED: ").append(fragment).toString());
            fragment.performStart();
            i1 = l1;
        }
_L9:
        k1 = i1;
        if(i1 > 4)
        {
            if(DEBUG)
                Log.v("FragmentManager", (new StringBuilder()).append("moveto RESUMED: ").append(fragment).toString());
            fragment.performResume();
            fragment.mSavedFragmentState = null;
            fragment.mSavedViewState = null;
            k1 = i1;
        }
          goto _L11
_L2:
        k1 = i;
        if(fragment.mState <= i) goto _L11; else goto _L12
_L12:
        View view;
        switch(fragment.mState)
        {
        default:
            k1 = i;
            continue; /* Loop/switch isn't completed */

        case 1: // '\001'
            break MISSING_BLOCK_LABEL_1194;

        case 5: // '\005'
            if(i < 5)
            {
                if(DEBUG)
                    Log.v("FragmentManager", (new StringBuilder()).append("movefrom RESUMED: ").append(fragment).toString());
                fragment.performPause();
            }
            // fall through

        case 4: // '\004'
            if(i < 4)
            {
                if(DEBUG)
                    Log.v("FragmentManager", (new StringBuilder()).append("movefrom STARTED: ").append(fragment).toString());
                fragment.performStop();
            }
            // fall through

        case 3: // '\003'
            if(i < 3)
            {
                if(DEBUG)
                    Log.v("FragmentManager", (new StringBuilder()).append("movefrom STOPPED: ").append(fragment).toString());
                fragment.performReallyStop();
            }
            break;

        case 2: // '\002'
            break;
        }
        break MISSING_BLOCK_LABEL_1373;
_L13:
        k1 = i;
        if(i < 1)
        {
            if(mDestroyed && fragment.mAnimatingAway != null)
            {
                view = fragment.mAnimatingAway;
                fragment.mAnimatingAway = null;
                view.clearAnimation();
            }
            Animation animation;
            Object obj1;
            if(fragment.mAnimatingAway != null)
            {
                fragment.mStateAfterAnimating = i;
                k1 = 1;
            } else
            {
                if(DEBUG)
                    Log.v("FragmentManager", (new StringBuilder()).append("movefrom CREATED: ").append(fragment).toString());
                if(!fragment.mRetaining)
                    fragment.performDestroy();
                else
                    fragment.mState = 0;
                fragment.mCalled = false;
                fragment.onDetach();
                if(!fragment.mCalled)
                    throw new SuperNotCalledException((new StringBuilder()).append("Fragment ").append(fragment).append(" did not call through to super.onDetach()").toString());
                k1 = i;
                if(!flag)
                    if(!fragment.mRetaining)
                    {
                        makeInactive(fragment);
                        k1 = i;
                    } else
                    {
                        fragment.mHost = null;
                        fragment.mParentFragment = null;
                        fragment.mFragmentManager = null;
                        fragment.mChildFragmentManager = null;
                        k1 = i;
                    }
            }
        }
        continue; /* Loop/switch isn't completed */
        if(i < 2)
        {
            if(DEBUG)
                Log.v("FragmentManager", (new StringBuilder()).append("movefrom ACTIVITY_CREATED: ").append(fragment).toString());
            if(fragment.mView != null && mHost.onShouldSaveFragmentState(fragment) && fragment.mSavedViewState == null)
                saveFragmentViewState(fragment);
            fragment.performDestroyView();
            if(fragment.mView != null && fragment.mContainer != null)
            {
                obj1 = null;
                animation = obj1;
                if(mCurState > 0)
                {
                    animation = obj1;
                    if(!mDestroyed)
                        animation = loadAnimation(fragment, j, false, k);
                }
                if(animation != null)
                {
                    fragment.mAnimatingAway = fragment.mView;
                    fragment.mStateAfterAnimating = i;
                    animation.setAnimationListener(new AnimateOnHWLayerIfNeededListener(animation, fragment) {

                        public void onAnimationEnd(Animation animation1)
                        {
                            super.onAnimationEnd(animation1);
                            if(fragment.mAnimatingAway != null)
                            {
                                fragment.mAnimatingAway = null;
                                moveToState(fragment, fragment.mStateAfterAnimating, 0, 0, false);
                            }
                        }

                        final FragmentManagerImpl this$0;
                        final Fragment val$fragment;

            
            {
                this$0 = FragmentManagerImpl.this;
                fragment = fragment1;
                super(final_view, animation);
            }
                    }
);
                    fragment.mView.startAnimation(animation);
                }
                fragment.mContainer.removeView(fragment.mView);
            }
            fragment.mContainer = null;
            fragment.mView = null;
            fragment.mInnerView = null;
        }
          goto _L13
        if(true) goto _L11; else goto _L14
_L14:
    }

    public void noteStateNotSaved()
    {
        mStateSaved = false;
    }

    public View onCreateView(View view, String s, Context context, AttributeSet attributeset)
    {
        Object obj = null;
        if("fragment".equals(s)) goto _L2; else goto _L1
_L1:
        s = obj;
_L4:
        return s;
_L2:
        s = attributeset.getAttributeValue(null, "class");
        TypedArray typedarray = context.obtainStyledAttributes(attributeset, FragmentTag.Fragment);
        String s1 = s;
        if(s == null)
            s1 = typedarray.getString(0);
        int i = typedarray.getResourceId(1, -1);
        String s2 = typedarray.getString(2);
        typedarray.recycle();
        s = obj;
        if(!Fragment.isSupportFragmentClass(mHost.getContext(), s1))
            continue; /* Loop/switch isn't completed */
        int j;
        if(view != null)
            j = view.getId();
        else
            j = 0;
        if(j == -1 && i == -1 && s2 == null)
            throw new IllegalArgumentException((new StringBuilder()).append(attributeset.getPositionDescription()).append(": Must specify unique android:id, android:tag, or have a parent with an id for ").append(s1).toString());
        if(i != -1)
            s = findFragmentById(i);
        else
            s = null;
        view = s;
        if(s == null)
        {
            view = s;
            if(s2 != null)
                view = findFragmentByTag(s2);
        }
        s = view;
        if(view == null)
        {
            s = view;
            if(j != -1)
                s = findFragmentById(j);
        }
        if(DEBUG)
            Log.v("FragmentManager", (new StringBuilder()).append("onCreateView: id=0x").append(Integer.toHexString(i)).append(" fname=").append(s1).append(" existing=").append(s).toString());
        if(s == null)
        {
            view = Fragment.instantiate(context, s1);
            view.mFromLayout = true;
            int k;
            if(i != 0)
                k = i;
            else
                k = j;
            view.mFragmentId = k;
            view.mContainerId = j;
            view.mTag = s2;
            view.mInLayout = true;
            view.mFragmentManager = this;
            view.mHost = mHost;
            view.onInflate(mHost.getContext(), attributeset, ((Fragment) (view)).mSavedFragmentState);
            addFragment(view, true);
        } else
        {
            if(((Fragment) (s)).mInLayout)
                throw new IllegalArgumentException((new StringBuilder()).append(attributeset.getPositionDescription()).append(": Duplicate id 0x").append(Integer.toHexString(i)).append(", tag ").append(s2).append(", or parent id 0x").append(Integer.toHexString(j)).append(" with another fragment for ").append(s1).toString());
            s.mInLayout = true;
            s.mHost = mHost;
            view = s;
            if(!((Fragment) (s)).mRetaining)
            {
                s.onInflate(mHost.getContext(), attributeset, ((Fragment) (s)).mSavedFragmentState);
                view = s;
            }
        }
        if(mCurState < 1 && ((Fragment) (view)).mFromLayout)
            moveToState(view, 1, 0, 0, false);
        else
            moveToState(view);
        if(((Fragment) (view)).mView == null)
            throw new IllegalStateException((new StringBuilder()).append("Fragment ").append(s1).append(" did not create a view.").toString());
        if(i != 0)
            ((Fragment) (view)).mView.setId(i);
        if(((Fragment) (view)).mView.getTag() == null)
            ((Fragment) (view)).mView.setTag(s2);
        s = ((Fragment) (view)).mView;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void performPendingDeferredStart(Fragment fragment)
    {
        if(fragment.mDeferStart)
            if(mExecutingActions)
            {
                mHavePendingDeferredStart = true;
            } else
            {
                fragment.mDeferStart = false;
                moveToState(fragment, mCurState, 0, 0, false);
            }
    }

    public void popBackStack()
    {
        enqueueAction(new Runnable() {

            public void run()
            {
                popBackStackState(mHost.getHandler(), null, -1, 0);
            }

            final FragmentManagerImpl this$0;

            
            {
                this$0 = FragmentManagerImpl.this;
                super();
            }
        }
, false);
    }

    public void popBackStack(final int id, final int flags)
    {
        if(id < 0)
        {
            throw new IllegalArgumentException((new StringBuilder()).append("Bad id: ").append(id).toString());
        } else
        {
            enqueueAction(new Runnable() {

                public void run()
                {
                    popBackStackState(mHost.getHandler(), null, id, flags);
                }

                final FragmentManagerImpl this$0;
                final int val$flags;
                final int val$id;

            
            {
                this$0 = FragmentManagerImpl.this;
                id = i;
                flags = j;
                super();
            }
            }
, false);
            return;
        }
    }

    public void popBackStack(final String name, final int flags)
    {
        enqueueAction(new Runnable() {

            public void run()
            {
                popBackStackState(mHost.getHandler(), name, -1, flags);
            }

            final FragmentManagerImpl this$0;
            final int val$flags;
            final String val$name;

            
            {
                this$0 = FragmentManagerImpl.this;
                name = s;
                flags = i;
                super();
            }
        }
, false);
    }

    public boolean popBackStackImmediate()
    {
        checkStateLoss();
        executePendingTransactions();
        return popBackStackState(mHost.getHandler(), null, -1, 0);
    }

    public boolean popBackStackImmediate(int i, int j)
    {
        checkStateLoss();
        executePendingTransactions();
        if(i < 0)
            throw new IllegalArgumentException((new StringBuilder()).append("Bad id: ").append(i).toString());
        else
            return popBackStackState(mHost.getHandler(), null, i, j);
    }

    public boolean popBackStackImmediate(String s, int i)
    {
        checkStateLoss();
        executePendingTransactions();
        return popBackStackState(mHost.getHandler(), s, -1, i);
    }

    boolean popBackStackState(Handler handler, String s, int i, int j)
    {
        if(mBackStack != null) goto _L2; else goto _L1
_L1:
        boolean flag = false;
_L5:
        return flag;
_L2:
        if(s != null || i >= 0 || (j & 1) != 0) goto _L4; else goto _L3
_L3:
label0:
        {
            i = mBackStack.size() - 1;
            if(i >= 0)
                break label0;
            flag = false;
        }
          goto _L5
        BackStackRecord backstackrecord = (BackStackRecord)mBackStack.remove(i);
        handler = new SparseArray();
        s = new SparseArray();
        backstackrecord.calculateBackFragments(handler, s);
        backstackrecord.popFromBackStack(true, null, handler, s);
        reportBackStackChanged();
_L10:
        flag = true;
          goto _L5
_L4:
        int k;
        int l;
        k = -1;
        if(s == null && i < 0)
            break MISSING_BLOCK_LABEL_268;
        l = mBackStack.size() - 1;
_L9:
label1:
        {
            if(l < 0)
                break label1;
            handler = (BackStackRecord)mBackStack.get(l);
        }
          goto _L6
_L8:
        if(l >= 0)
            break MISSING_BLOCK_LABEL_188;
        flag = false;
          goto _L5
_L6:
        if(s != null && s.equals(handler.getName()) || i >= 0 && i == ((BackStackRecord) (handler)).mIndex) goto _L8; else goto _L7
_L7:
        l--;
          goto _L9
        k = l;
        if((j & 1) != 0)
        {
            j = l - 1;
            do
            {
                k = j;
                if(j < 0)
                    break;
                handler = (BackStackRecord)mBackStack.get(j);
                if(s == null || !s.equals(handler.getName()))
                {
                    k = j;
                    if(i < 0)
                        break;
                    k = j;
                    if(i != ((BackStackRecord) (handler)).mIndex)
                        break;
                }
                j--;
            } while(true);
        }
label2:
        {
            if(k != mBackStack.size() - 1)
                break label2;
            flag = false;
        }
          goto _L5
        ArrayList arraylist = new ArrayList();
        for(i = mBackStack.size() - 1; i > k; i--)
            arraylist.add(mBackStack.remove(i));

        j = arraylist.size() - 1;
        s = new SparseArray();
        SparseArray sparsearray = new SparseArray();
        for(i = 0; i <= j; i++)
            ((BackStackRecord)arraylist.get(i)).calculateBackFragments(s, sparsearray);

        handler = null;
        i = 0;
        while(i <= j) 
        {
            if(DEBUG)
                Log.v("FragmentManager", (new StringBuilder()).append("Popping back stack state: ").append(arraylist.get(i)).toString());
            BackStackRecord backstackrecord1 = (BackStackRecord)arraylist.get(i);
            boolean flag1;
            if(i == j)
                flag1 = true;
            else
                flag1 = false;
            handler = backstackrecord1.popFromBackStack(flag1, handler, s, sparsearray);
            i++;
        }
        reportBackStackChanged();
          goto _L10
    }

    public void putFragment(Bundle bundle, String s, Fragment fragment)
    {
        if(fragment.mIndex < 0)
            throwException(new IllegalStateException((new StringBuilder()).append("Fragment ").append(fragment).append(" is not currently in the FragmentManager").toString()));
        bundle.putInt(s, fragment.mIndex);
    }

    public void removeFragment(Fragment fragment, int i, int j)
    {
        if(DEBUG)
            Log.v("FragmentManager", (new StringBuilder()).append("remove: ").append(fragment).append(" nesting=").append(fragment.mBackStackNesting).toString());
        int k;
        if(!fragment.isInBackStack())
            k = 1;
        else
            k = 0;
        if(!fragment.mDetached || k != 0)
        {
            if(mAdded != null)
                mAdded.remove(fragment);
            if(fragment.mHasMenu && fragment.mMenuVisible)
                mNeedMenuInvalidate = true;
            fragment.mAdded = false;
            fragment.mRemoving = true;
            if(k != 0)
                k = 0;
            else
                k = 1;
            moveToState(fragment, k, i, j, false);
        }
    }

    public void removeOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener onbackstackchangedlistener)
    {
        if(mBackStackChangeListeners != null)
            mBackStackChangeListeners.remove(onbackstackchangedlistener);
    }

    void reportBackStackChanged()
    {
        if(mBackStackChangeListeners != null)
        {
            for(int i = 0; i < mBackStackChangeListeners.size(); i++)
                ((FragmentManager.OnBackStackChangedListener)mBackStackChangeListeners.get(i)).onBackStackChanged();

        }
    }

    void restoreAllState(Parcelable parcelable, List list)
    {
        if(parcelable != null) goto _L2; else goto _L1
_L1:
        return;
_L2:
        parcelable = (FragmentManagerState)parcelable;
        if(((FragmentManagerState) (parcelable)).mActive != null)
        {
            if(list != null)
            {
                for(int i = 0; i < list.size(); i++)
                {
                    Fragment fragment = (Fragment)list.get(i);
                    if(DEBUG)
                        Log.v("FragmentManager", (new StringBuilder()).append("restoreAllState: re-attaching retained ").append(fragment).toString());
                    FragmentState fragmentstate = ((FragmentManagerState) (parcelable)).mActive[fragment.mIndex];
                    fragmentstate.mInstance = fragment;
                    fragment.mSavedViewState = null;
                    fragment.mBackStackNesting = 0;
                    fragment.mInLayout = false;
                    fragment.mAdded = false;
                    fragment.mTarget = null;
                    if(fragmentstate.mSavedFragmentState != null)
                    {
                        fragmentstate.mSavedFragmentState.setClassLoader(mHost.getContext().getClassLoader());
                        fragment.mSavedViewState = fragmentstate.mSavedFragmentState.getSparseParcelableArray("android:view_state");
                        fragment.mSavedFragmentState = fragmentstate.mSavedFragmentState;
                    }
                }

            }
            mActive = new ArrayList(((FragmentManagerState) (parcelable)).mActive.length);
            if(mAvailIndices != null)
                mAvailIndices.clear();
            int j = 0;
            while(j < ((FragmentManagerState) (parcelable)).mActive.length) 
            {
                FragmentState fragmentstate1 = ((FragmentManagerState) (parcelable)).mActive[j];
                if(fragmentstate1 != null)
                {
                    Fragment fragment1 = fragmentstate1.instantiate(mHost, mParent);
                    if(DEBUG)
                        Log.v("FragmentManager", (new StringBuilder()).append("restoreAllState: active #").append(j).append(": ").append(fragment1).toString());
                    mActive.add(fragment1);
                    fragmentstate1.mInstance = null;
                } else
                {
                    mActive.add(null);
                    if(mAvailIndices == null)
                        mAvailIndices = new ArrayList();
                    if(DEBUG)
                        Log.v("FragmentManager", (new StringBuilder()).append("restoreAllState: avail #").append(j).toString());
                    mAvailIndices.add(Integer.valueOf(j));
                }
                j++;
            }
            if(list != null)
            {
                int k = 0;
                while(k < list.size()) 
                {
                    Fragment fragment2 = (Fragment)list.get(k);
                    if(fragment2.mTargetIndex >= 0)
                        if(fragment2.mTargetIndex < mActive.size())
                        {
                            fragment2.mTarget = (Fragment)mActive.get(fragment2.mTargetIndex);
                        } else
                        {
                            Log.w("FragmentManager", (new StringBuilder()).append("Re-attaching retained fragment ").append(fragment2).append(" target no longer exists: ").append(fragment2.mTargetIndex).toString());
                            fragment2.mTarget = null;
                        }
                    k++;
                }
            }
            if(((FragmentManagerState) (parcelable)).mAdded != null)
            {
                mAdded = new ArrayList(((FragmentManagerState) (parcelable)).mAdded.length);
                for(int l = 0; l < ((FragmentManagerState) (parcelable)).mAdded.length; l++)
                {
                    list = (Fragment)mActive.get(((FragmentManagerState) (parcelable)).mAdded[l]);
                    if(list == null)
                        throwException(new IllegalStateException((new StringBuilder()).append("No instantiated fragment for index #").append(((FragmentManagerState) (parcelable)).mAdded[l]).toString()));
                    list.mAdded = true;
                    if(DEBUG)
                        Log.v("FragmentManager", (new StringBuilder()).append("restoreAllState: added #").append(l).append(": ").append(list).toString());
                    if(mAdded.contains(list))
                        throw new IllegalStateException("Already added!");
                    mAdded.add(list);
                }

            } else
            {
                mAdded = null;
            }
            if(((FragmentManagerState) (parcelable)).mBackStack != null)
            {
                mBackStack = new ArrayList(((FragmentManagerState) (parcelable)).mBackStack.length);
                int i1 = 0;
                while(i1 < ((FragmentManagerState) (parcelable)).mBackStack.length) 
                {
                    list = ((FragmentManagerState) (parcelable)).mBackStack[i1].instantiate(this);
                    if(DEBUG)
                    {
                        Log.v("FragmentManager", (new StringBuilder()).append("restoreAllState: back stack #").append(i1).append(" (index ").append(((BackStackRecord) (list)).mIndex).append("): ").append(list).toString());
                        list.dump("  ", new PrintWriter(new LogWriter("FragmentManager")), false);
                    }
                    mBackStack.add(list);
                    if(((BackStackRecord) (list)).mIndex >= 0)
                        setBackStackIndex(((BackStackRecord) (list)).mIndex, list);
                    i1++;
                }
            } else
            {
                mBackStack = null;
            }
        }
        if(true) goto _L1; else goto _L3
_L3:
    }

    ArrayList retainNonConfig()
    {
        ArrayList arraylist = null;
        ArrayList arraylist1 = null;
        if(mActive != null)
        {
            int i = 0;
            do
            {
                arraylist = arraylist1;
                if(i >= mActive.size())
                    break;
                Fragment fragment = (Fragment)mActive.get(i);
                ArrayList arraylist2 = arraylist1;
                if(fragment != null)
                {
                    arraylist2 = arraylist1;
                    if(fragment.mRetainInstance)
                    {
                        arraylist = arraylist1;
                        if(arraylist1 == null)
                            arraylist = new ArrayList();
                        arraylist.add(fragment);
                        fragment.mRetaining = true;
                        int j;
                        if(fragment.mTarget != null)
                            j = fragment.mTarget.mIndex;
                        else
                            j = -1;
                        fragment.mTargetIndex = j;
                        arraylist2 = arraylist;
                        if(DEBUG)
                        {
                            Log.v("FragmentManager", (new StringBuilder()).append("retainNonConfig: keeping retained ").append(fragment).toString());
                            arraylist2 = arraylist;
                        }
                    }
                }
                i++;
                arraylist1 = arraylist2;
            } while(true);
        }
        return arraylist;
    }

    Parcelable saveAllState()
    {
        Object obj;
        Object obj1;
        obj = null;
        execPendingActions();
        if(HONEYCOMB)
            mStateSaved = true;
        obj1 = obj;
        if(mActive == null) goto _L2; else goto _L1
_L1:
        if(mActive.size() > 0) goto _L4; else goto _L3
_L3:
        obj1 = obj;
_L2:
        return ((Parcelable) (obj1));
_L4:
        int i = mActive.size();
        FragmentState afragmentstate[] = new FragmentState[i];
        boolean flag = false;
        int l = 0;
        while(l < i) 
        {
            obj1 = (Fragment)mActive.get(l);
            if(obj1 == null)
                continue;
            if(((Fragment) (obj1)).mIndex < 0)
                throwException(new IllegalStateException((new StringBuilder()).append("Failure saving state: active ").append(obj1).append(" has cleared index: ").append(((Fragment) (obj1)).mIndex).toString()));
            boolean flag1 = true;
            FragmentState fragmentstate = new FragmentState(((Fragment) (obj1)));
            afragmentstate[l] = fragmentstate;
            if(((Fragment) (obj1)).mState > 0 && fragmentstate.mSavedFragmentState == null)
            {
                fragmentstate.mSavedFragmentState = saveFragmentBasicState(((Fragment) (obj1)));
                if(((Fragment) (obj1)).mTarget != null)
                {
                    if(((Fragment) (obj1)).mTarget.mIndex < 0)
                        throwException(new IllegalStateException((new StringBuilder()).append("Failure saving state: ").append(obj1).append(" has target not in fragment manager: ").append(((Fragment) (obj1)).mTarget).toString()));
                    if(fragmentstate.mSavedFragmentState == null)
                        fragmentstate.mSavedFragmentState = new Bundle();
                    putFragment(fragmentstate.mSavedFragmentState, "android:target_state", ((Fragment) (obj1)).mTarget);
                    if(((Fragment) (obj1)).mTargetRequestCode != 0)
                        fragmentstate.mSavedFragmentState.putInt("android:target_req_state", ((Fragment) (obj1)).mTargetRequestCode);
                }
            } else
            {
                fragmentstate.mSavedFragmentState = ((Fragment) (obj1)).mSavedFragmentState;
            }
            flag = flag1;
            if(DEBUG)
            {
                Log.v("FragmentManager", (new StringBuilder()).append("Saved state of ").append(obj1).append(": ").append(fragmentstate.mSavedFragmentState).toString());
                flag = flag1;
            }
            l++;
        }
        if(!flag)
        {
            obj1 = obj;
            if(DEBUG)
            {
                Log.v("FragmentManager", "saveAllState: no fragments!");
                obj1 = obj;
            }
        } else
        {
            Object aobj[] = null;
            FragmentManagerState fragmentmanagerstate = null;
            obj1 = ((Object) (aobj));
            if(mAdded != null)
            {
                int j = mAdded.size();
                obj1 = ((Object) (aobj));
                if(j > 0)
                {
                    aobj = new int[j];
                    int i1 = 0;
                    do
                    {
                        obj1 = ((Object) (aobj));
                        if(i1 >= j)
                            break;
                        aobj[i1] = ((Fragment)mAdded.get(i1)).mIndex;
                        if(aobj[i1] < 0)
                            throwException(new IllegalStateException((new StringBuilder()).append("Failure saving state: active ").append(mAdded.get(i1)).append(" has cleared index: ").append(aobj[i1]).toString()));
                        if(DEBUG)
                            Log.v("FragmentManager", (new StringBuilder()).append("saveAllState: adding fragment #").append(i1).append(": ").append(mAdded.get(i1)).toString());
                        i1++;
                    } while(true);
                }
            }
            aobj = fragmentmanagerstate;
            if(mBackStack != null)
            {
                int k = mBackStack.size();
                aobj = fragmentmanagerstate;
                if(k > 0)
                {
                    BackStackState abackstackstate[] = new BackStackState[k];
                    int j1 = 0;
                    do
                    {
                        aobj = abackstackstate;
                        if(j1 >= k)
                            break;
                        abackstackstate[j1] = new BackStackState((BackStackRecord)mBackStack.get(j1));
                        if(DEBUG)
                            Log.v("FragmentManager", (new StringBuilder()).append("saveAllState: adding back stack #").append(j1).append(": ").append(mBackStack.get(j1)).toString());
                        j1++;
                    } while(true);
                }
            }
            abackstackstate = new FragmentManagerState();
            abackstackstate.mActive = afragmentstate;
            abackstackstate.mAdded = ((int []) (obj1));
            abackstackstate.mBackStack = ((BackStackState []) (aobj));
            obj1 = abackstackstate;
        }
        if(true) goto _L2; else goto _L5
_L5:
    }

    Bundle saveFragmentBasicState(Fragment fragment)
    {
        Bundle bundle = null;
        if(mStateBundle == null)
            mStateBundle = new Bundle();
        fragment.performSaveInstanceState(mStateBundle);
        if(!mStateBundle.isEmpty())
        {
            bundle = mStateBundle;
            mStateBundle = null;
        }
        if(fragment.mView != null)
            saveFragmentViewState(fragment);
        Bundle bundle1 = bundle;
        if(fragment.mSavedViewState != null)
        {
            bundle1 = bundle;
            if(bundle == null)
                bundle1 = new Bundle();
            bundle1.putSparseParcelableArray("android:view_state", fragment.mSavedViewState);
        }
        bundle = bundle1;
        if(!fragment.mUserVisibleHint)
        {
            bundle = bundle1;
            if(bundle1 == null)
                bundle = new Bundle();
            bundle.putBoolean("android:user_visible_hint", fragment.mUserVisibleHint);
        }
        return bundle;
    }

    public Fragment.SavedState saveFragmentInstanceState(Fragment fragment)
    {
        Object obj = null;
        if(fragment.mIndex < 0)
            throwException(new IllegalStateException((new StringBuilder()).append("Fragment ").append(fragment).append(" is not currently in the FragmentManager").toString()));
        Fragment.SavedState savedstate = obj;
        if(fragment.mState > 0)
        {
            fragment = saveFragmentBasicState(fragment);
            savedstate = obj;
            if(fragment != null)
                savedstate = new Fragment.SavedState(fragment);
        }
        return savedstate;
    }

    void saveFragmentViewState(Fragment fragment)
    {
        if(fragment.mInnerView != null) goto _L2; else goto _L1
_L1:
        return;
_L2:
        if(mStateArray == null)
            mStateArray = new SparseArray();
        else
            mStateArray.clear();
        fragment.mInnerView.saveHierarchyState(mStateArray);
        if(mStateArray.size() > 0)
        {
            fragment.mSavedViewState = mStateArray;
            mStateArray = null;
        }
        if(true) goto _L1; else goto _L3
_L3:
    }

    public void setBackStackIndex(int i, BackStackRecord backstackrecord)
    {
        this;
        JVM INSTR monitorenter ;
        int j;
        if(mBackStackIndices == null)
        {
            ArrayList arraylist = JVM INSTR new #338 <Class ArrayList>;
            arraylist.ArrayList();
            mBackStackIndices = arraylist;
        }
        j = mBackStackIndices.size();
        int k = j;
        if(i >= j) goto _L2; else goto _L1
_L1:
        if(DEBUG)
        {
            StringBuilder stringbuilder = JVM INSTR new #167 <Class StringBuilder>;
            stringbuilder.StringBuilder();
            Log.v("FragmentManager", stringbuilder.append("Setting back stack index ").append(i).append(" to ").append(backstackrecord).toString());
        }
        mBackStackIndices.set(i, backstackrecord);
_L4:
        this;
        JVM INSTR monitorexit ;
        return;
_L2:
        if(k >= i)
            break; /* Loop/switch isn't completed */
        mBackStackIndices.add(null);
        if(mAvailBackStackIndices == null)
        {
            ArrayList arraylist1 = JVM INSTR new #338 <Class ArrayList>;
            arraylist1.ArrayList();
            mAvailBackStackIndices = arraylist1;
        }
        if(DEBUG)
        {
            StringBuilder stringbuilder1 = JVM INSTR new #167 <Class StringBuilder>;
            stringbuilder1.StringBuilder();
            Log.v("FragmentManager", stringbuilder1.append("Adding available back stack index ").append(k).toString());
        }
        mAvailBackStackIndices.add(Integer.valueOf(k));
        k++;
        if(true) goto _L2; else goto _L3
_L3:
        if(DEBUG)
        {
            StringBuilder stringbuilder2 = JVM INSTR new #167 <Class StringBuilder>;
            stringbuilder2.StringBuilder();
            Log.v("FragmentManager", stringbuilder2.append("Adding back stack index ").append(i).append(" with ").append(backstackrecord).toString());
        }
        mBackStackIndices.add(backstackrecord);
          goto _L4
        backstackrecord;
        this;
        JVM INSTR monitorexit ;
        throw backstackrecord;
    }

    public void showFragment(Fragment fragment, int i, int j)
    {
        if(DEBUG)
            Log.v("FragmentManager", (new StringBuilder()).append("show: ").append(fragment).toString());
        if(fragment.mHidden)
        {
            fragment.mHidden = false;
            if(fragment.mView != null)
            {
                Animation animation = loadAnimation(fragment, i, true, j);
                if(animation != null)
                {
                    setHWLayerAnimListenerIfAlpha(fragment.mView, animation);
                    fragment.mView.startAnimation(animation);
                }
                fragment.mView.setVisibility(0);
            }
            if(fragment.mAdded && fragment.mHasMenu && fragment.mMenuVisible)
                mNeedMenuInvalidate = true;
            fragment.onHiddenChanged(false);
        }
    }

    void startPendingDeferredFragments()
    {
        if(mActive != null)
        {
            int i = 0;
            while(i < mActive.size()) 
            {
                Fragment fragment = (Fragment)mActive.get(i);
                if(fragment != null)
                    performPendingDeferredStart(fragment);
                i++;
            }
        }
    }

    public String toString()
    {
        StringBuilder stringbuilder = new StringBuilder(128);
        stringbuilder.append("FragmentManager{");
        stringbuilder.append(Integer.toHexString(System.identityHashCode(this)));
        stringbuilder.append(" in ");
        if(mParent != null)
            DebugUtils.buildShortClassTag(mParent, stringbuilder);
        else
            DebugUtils.buildShortClassTag(mHost, stringbuilder);
        stringbuilder.append("}}");
        return stringbuilder.toString();
    }

    static final Interpolator ACCELERATE_CUBIC = new AccelerateInterpolator(1.5F);
    static final Interpolator ACCELERATE_QUINT = new AccelerateInterpolator(2.5F);
    static final int ANIM_DUR = 220;
    public static final int ANIM_STYLE_CLOSE_ENTER = 3;
    public static final int ANIM_STYLE_CLOSE_EXIT = 4;
    public static final int ANIM_STYLE_FADE_ENTER = 5;
    public static final int ANIM_STYLE_FADE_EXIT = 6;
    public static final int ANIM_STYLE_OPEN_ENTER = 1;
    public static final int ANIM_STYLE_OPEN_EXIT = 2;
    static boolean DEBUG = false;
    static final Interpolator DECELERATE_CUBIC = new DecelerateInterpolator(1.5F);
    static final Interpolator DECELERATE_QUINT = new DecelerateInterpolator(2.5F);
    static final boolean HONEYCOMB;
    static final String TAG = "FragmentManager";
    static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
    static final String TARGET_STATE_TAG = "android:target_state";
    static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
    static final String VIEW_STATE_TAG = "android:view_state";
    static Field sAnimationListenerField = null;
    ArrayList mActive;
    ArrayList mAdded;
    ArrayList mAvailBackStackIndices;
    ArrayList mAvailIndices;
    ArrayList mBackStack;
    ArrayList mBackStackChangeListeners;
    ArrayList mBackStackIndices;
    FragmentContainer mContainer;
    FragmentController mController;
    ArrayList mCreatedMenus;
    int mCurState;
    boolean mDestroyed;
    Runnable mExecCommit;
    boolean mExecutingActions;
    boolean mHavePendingDeferredStart;
    FragmentHostCallback mHost;
    boolean mNeedMenuInvalidate;
    String mNoTransactionsBecause;
    Fragment mParent;
    ArrayList mPendingActions;
    SparseArray mStateArray;
    Bundle mStateBundle;
    boolean mStateSaved;
    Runnable mTmpActions[];

    static 
    {
        boolean flag = false;
        DEBUG = false;
        if(android.os.Build.VERSION.SDK_INT >= 11)
            flag = true;
        HONEYCOMB = flag;
    }
}
