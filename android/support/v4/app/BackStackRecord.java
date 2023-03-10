// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.app;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.LogWriter;
import android.util.Log;
import android.util.SparseArray;
import android.view.*;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;

// Referenced classes of package android.support.v4.app:
//            FragmentTransaction, FragmentManagerImpl, FragmentHostCallback, FragmentContainer, 
//            Fragment, SharedElementCallback, FragmentTransitionCompat21

final class BackStackRecord extends FragmentTransaction
    implements FragmentManager.BackStackEntry, Runnable
{
    static final class Op
    {

        int cmd;
        int enterAnim;
        int exitAnim;
        Fragment fragment;
        Op next;
        int popEnterAnim;
        int popExitAnim;
        Op prev;
        ArrayList removed;

        Op()
        {
        }
    }

    public class TransitionState
    {

        public FragmentTransitionCompat21.EpicenterView enteringEpicenterView;
        public ArrayList hiddenFragmentViews;
        public ArrayMap nameOverrides;
        public View nonExistentView;
        final BackStackRecord this$0;

        public TransitionState()
        {
            this$0 = BackStackRecord.this;
            super();
            nameOverrides = new ArrayMap();
            hiddenFragmentViews = new ArrayList();
            enteringEpicenterView = new FragmentTransitionCompat21.EpicenterView();
        }
    }


    public BackStackRecord(FragmentManagerImpl fragmentmanagerimpl)
    {
        mAllowAddToBackStack = true;
        mIndex = -1;
        mManager = fragmentmanagerimpl;
    }

    private TransitionState beginTransition(SparseArray sparsearray, SparseArray sparsearray1, boolean flag)
    {
        ensureFragmentsAreInitialized(sparsearray1);
        TransitionState transitionstate = new TransitionState();
        transitionstate.nonExistentView = new View(mManager.mHost.getContext());
        boolean flag1 = false;
        for(int i = 0; i < sparsearray.size(); i++)
            if(configureTransitions(sparsearray.keyAt(i), transitionstate, flag, sparsearray, sparsearray1))
                flag1 = true;

        int j = 0;
        boolean flag2;
        for(flag2 = flag1; j < sparsearray1.size(); flag2 = flag1)
        {
            int k = sparsearray1.keyAt(j);
            flag1 = flag2;
            if(sparsearray.get(k) == null)
            {
                flag1 = flag2;
                if(configureTransitions(k, transitionstate, flag, sparsearray, sparsearray1))
                    flag1 = true;
            }
            j++;
        }

        sparsearray = transitionstate;
        if(!flag2)
            sparsearray = null;
        return sparsearray;
    }

    private void calculateFragments(SparseArray sparsearray, SparseArray sparsearray1)
    {
        if(mManager.mContainer.onHasView()) goto _L2; else goto _L1
_L1:
        return;
_L2:
        Op op = mHead;
_L12:
        if(op == null) goto _L1; else goto _L3
_L3:
        op.cmd;
        JVM INSTR tableswitch 1 7: default 68
    //                   1 76
    //                   2 89
    //                   3 218
    //                   4 230
    //                   5 242
    //                   6 255
    //                   7 267;
           goto _L4 _L5 _L6 _L7 _L8 _L9 _L10 _L11
_L4:
        break; /* Loop/switch isn't completed */
_L11:
        break MISSING_BLOCK_LABEL_267;
_L13:
        op = op.next;
          goto _L12
_L5:
        setLastIn(sparsearray, sparsearray1, op.fragment);
          goto _L13
_L6:
        Fragment fragment = op.fragment;
        if(mManager.mAdded == null)
            break MISSING_BLOCK_LABEL_205;
        int i = 0;
        while(i < mManager.mAdded.size()) 
        {
label0:
            {
                Fragment fragment1 = (Fragment)mManager.mAdded.get(i);
                Fragment fragment2;
                if(fragment != null)
                {
                    fragment2 = fragment;
                    if(fragment1.mContainerId != fragment.mContainerId)
                        break label0;
                }
                if(fragment1 == fragment)
                {
                    fragment2 = null;
                    sparsearray1.remove(fragment1.mContainerId);
                } else
                {
                    setFirstOut(sparsearray, sparsearray1, fragment1);
                    fragment2 = fragment;
                }
            }
            i++;
            fragment = fragment2;
        }
        setLastIn(sparsearray, sparsearray1, op.fragment);
          goto _L13
_L7:
        setFirstOut(sparsearray, sparsearray1, op.fragment);
          goto _L13
_L8:
        setFirstOut(sparsearray, sparsearray1, op.fragment);
          goto _L13
_L9:
        setLastIn(sparsearray, sparsearray1, op.fragment);
          goto _L13
_L10:
        setFirstOut(sparsearray, sparsearray1, op.fragment);
          goto _L13
        setLastIn(sparsearray, sparsearray1, op.fragment);
          goto _L13
    }

    private void callSharedElementEnd(TransitionState transitionstate, Fragment fragment, Fragment fragment1, boolean flag, ArrayMap arraymap)
    {
        if(flag)
            transitionstate = fragment1.mEnterTransitionCallback;
        else
            transitionstate = fragment.mEnterTransitionCallback;
        if(transitionstate != null)
            transitionstate.onSharedElementEnd(new ArrayList(arraymap.keySet()), new ArrayList(arraymap.values()), null);
    }

    private static Object captureExitingViews(Object obj, Fragment fragment, ArrayList arraylist, ArrayMap arraymap, View view)
    {
        Object obj1 = obj;
        if(obj != null)
            obj1 = FragmentTransitionCompat21.captureExitingViews(obj, fragment.getView(), arraylist, arraymap, view);
        return obj1;
    }

    private boolean configureTransitions(int i, TransitionState transitionstate, boolean flag, SparseArray sparsearray, SparseArray sparsearray1)
    {
        ViewGroup viewgroup = (ViewGroup)mManager.mContainer.onFindViewById(i);
        if(viewgroup == null)
        {
            flag = false;
        } else
        {
            final Object inFragment = (Fragment)sparsearray1.get(i);
            Object obj = (Fragment)sparsearray.get(i);
            Object obj1 = getEnterTransition(((Fragment) (inFragment)), flag);
            Object obj2 = getSharedElementTransition(((Fragment) (inFragment)), ((Fragment) (obj)), flag);
            Object obj3 = getExitTransition(((Fragment) (obj)), flag);
            sparsearray = null;
            ArrayList arraylist1 = new ArrayList();
            sparsearray1 = ((SparseArray) (obj2));
            if(obj2 != null)
            {
                ArrayMap arraymap = remapSharedElements(transitionstate, ((Fragment) (obj)), flag);
                if(arraymap.isEmpty())
                {
                    sparsearray1 = null;
                    sparsearray = null;
                } else
                {
                    if(flag)
                        sparsearray = ((Fragment) (obj)).mEnterTransitionCallback;
                    else
                        sparsearray = ((Fragment) (inFragment)).mEnterTransitionCallback;
                    if(sparsearray != null)
                        sparsearray.onSharedElementStart(new ArrayList(arraymap.keySet()), new ArrayList(arraymap.values()), null);
                    prepareSharedElementTransition(transitionstate, viewgroup, obj2, ((Fragment) (inFragment)), ((Fragment) (obj)), flag, arraylist1);
                    sparsearray1 = ((SparseArray) (obj2));
                    sparsearray = arraymap;
                }
            }
            if(obj1 == null && sparsearray1 == null && obj3 == null)
            {
                flag = false;
            } else
            {
                ArrayList arraylist = new ArrayList();
                Object obj4 = captureExitingViews(obj3, ((Fragment) (obj)), arraylist, sparsearray, transitionstate.nonExistentView);
                if(mSharedElementTargetNames != null && sparsearray != null)
                {
                    obj3 = (View)sparsearray.get(mSharedElementTargetNames.get(0));
                    if(obj3 != null)
                    {
                        if(obj4 != null)
                            FragmentTransitionCompat21.setEpicenter(obj4, ((View) (obj3)));
                        if(sparsearray1 != null)
                            FragmentTransitionCompat21.setEpicenter(sparsearray1, ((View) (obj3)));
                    }
                }
                obj = new FragmentTransitionCompat21.ViewRetriever() {

                    public View getView()
                    {
                        return inFragment.getView();
                    }

                    final BackStackRecord this$0;
                    final Fragment val$inFragment;

            
            {
                this$0 = BackStackRecord.this;
                inFragment = fragment;
                super();
            }
                }
;
                ArrayList arraylist2 = new ArrayList();
                obj3 = new ArrayMap();
                boolean flag1 = true;
                if(inFragment != null)
                    if(flag)
                        flag1 = ((Fragment) (inFragment)).getAllowReturnTransitionOverlap();
                    else
                        flag1 = ((Fragment) (inFragment)).getAllowEnterTransitionOverlap();
                inFragment = FragmentTransitionCompat21.mergeTransitions(obj1, obj4, sparsearray1, flag1);
                if(inFragment != null)
                {
                    FragmentTransitionCompat21.addTransitionTargets(obj1, sparsearray1, viewgroup, ((FragmentTransitionCompat21.ViewRetriever) (obj)), transitionstate.nonExistentView, transitionstate.enteringEpicenterView, transitionstate.nameOverrides, arraylist2, sparsearray, ((java.util.Map) (obj3)), arraylist1);
                    excludeHiddenFragmentsAfterEnter(viewgroup, transitionstate, i, inFragment);
                    FragmentTransitionCompat21.excludeTarget(inFragment, transitionstate.nonExistentView, true);
                    excludeHiddenFragments(transitionstate, i, inFragment);
                    FragmentTransitionCompat21.beginDelayedTransition(viewgroup, inFragment);
                    FragmentTransitionCompat21.cleanupTransitions(viewgroup, transitionstate.nonExistentView, obj1, arraylist2, obj4, arraylist, sparsearray1, arraylist1, inFragment, transitionstate.hiddenFragmentViews, ((java.util.Map) (obj3)));
                }
                if(inFragment != null)
                    flag = true;
                else
                    flag = false;
            }
        }
        return flag;
    }

    private void doAddOp(int i, Fragment fragment, String s, int j)
    {
        fragment.mFragmentManager = mManager;
        if(s != null)
        {
            if(fragment.mTag != null && !s.equals(fragment.mTag))
                throw new IllegalStateException((new StringBuilder()).append("Can't change tag of fragment ").append(fragment).append(": was ").append(fragment.mTag).append(" now ").append(s).toString());
            fragment.mTag = s;
        }
        if(i != 0)
        {
            if(fragment.mFragmentId != 0 && fragment.mFragmentId != i)
                throw new IllegalStateException((new StringBuilder()).append("Can't change container ID of fragment ").append(fragment).append(": was ").append(fragment.mFragmentId).append(" now ").append(i).toString());
            fragment.mFragmentId = i;
            fragment.mContainerId = i;
        }
        s = new Op();
        s.cmd = j;
        s.fragment = fragment;
        addOp(s);
    }

    private void ensureFragmentsAreInitialized(SparseArray sparsearray)
    {
        int i = sparsearray.size();
        for(int j = 0; j < i; j++)
        {
            Fragment fragment = (Fragment)sparsearray.valueAt(j);
            if(fragment.mState < 1)
            {
                mManager.makeActive(fragment);
                mManager.moveToState(fragment, 1, 0, 0, false);
            }
        }

    }

    private void excludeHiddenFragments(TransitionState transitionstate, int i, Object obj)
    {
        if(mManager.mAdded != null)
        {
            int j = 0;
            while(j < mManager.mAdded.size()) 
            {
                Fragment fragment = (Fragment)mManager.mAdded.get(j);
                if(fragment.mView != null && fragment.mContainer != null && fragment.mContainerId == i)
                    if(fragment.mHidden)
                    {
                        if(!transitionstate.hiddenFragmentViews.contains(fragment.mView))
                        {
                            FragmentTransitionCompat21.excludeTarget(obj, fragment.mView, true);
                            transitionstate.hiddenFragmentViews.add(fragment.mView);
                        }
                    } else
                    {
                        FragmentTransitionCompat21.excludeTarget(obj, fragment.mView, false);
                        transitionstate.hiddenFragmentViews.remove(fragment.mView);
                    }
                j++;
            }
        }
    }

    private void excludeHiddenFragmentsAfterEnter(final View sceneRoot, final TransitionState state, final int containerId, final Object transition)
    {
        sceneRoot.getViewTreeObserver().addOnPreDrawListener(new android.view.ViewTreeObserver.OnPreDrawListener() {

            public boolean onPreDraw()
            {
                sceneRoot.getViewTreeObserver().removeOnPreDrawListener(this);
                excludeHiddenFragments(state, containerId, transition);
                return true;
            }

            final BackStackRecord this$0;
            final int val$containerId;
            final View val$sceneRoot;
            final TransitionState val$state;
            final Object val$transition;

            
            {
                this$0 = BackStackRecord.this;
                sceneRoot = view;
                state = transitionstate;
                containerId = i;
                transition = obj;
                super();
            }
        }
);
    }

    private static Object getEnterTransition(Fragment fragment, boolean flag)
    {
        if(fragment == null)
        {
            fragment = null;
        } else
        {
            if(flag)
                fragment = ((Fragment) (fragment.getReenterTransition()));
            else
                fragment = ((Fragment) (fragment.getEnterTransition()));
            fragment = ((Fragment) (FragmentTransitionCompat21.cloneTransition(fragment)));
        }
        return fragment;
    }

    private static Object getExitTransition(Fragment fragment, boolean flag)
    {
        if(fragment == null)
        {
            fragment = null;
        } else
        {
            if(flag)
                fragment = ((Fragment) (fragment.getReturnTransition()));
            else
                fragment = ((Fragment) (fragment.getExitTransition()));
            fragment = ((Fragment) (FragmentTransitionCompat21.cloneTransition(fragment)));
        }
        return fragment;
    }

    private static Object getSharedElementTransition(Fragment fragment, Fragment fragment1, boolean flag)
    {
        if(fragment == null || fragment1 == null)
        {
            fragment = null;
        } else
        {
            if(flag)
                fragment = ((Fragment) (fragment1.getSharedElementReturnTransition()));
            else
                fragment = ((Fragment) (fragment.getSharedElementEnterTransition()));
            fragment = ((Fragment) (FragmentTransitionCompat21.wrapSharedElementTransition(fragment)));
        }
        return fragment;
    }

    private ArrayMap mapEnteringSharedElements(TransitionState transitionstate, Fragment fragment, boolean flag)
    {
        ArrayMap arraymap = new ArrayMap();
        fragment = fragment.getView();
        transitionstate = arraymap;
        if(fragment != null)
        {
            transitionstate = arraymap;
            if(mSharedElementSourceNames != null)
            {
                FragmentTransitionCompat21.findNamedViews(arraymap, fragment);
                if(flag)
                {
                    transitionstate = remapNames(mSharedElementSourceNames, mSharedElementTargetNames, arraymap);
                } else
                {
                    arraymap.retainAll(mSharedElementTargetNames);
                    transitionstate = arraymap;
                }
            }
        }
        return transitionstate;
    }

    private ArrayMap mapSharedElementsIn(TransitionState transitionstate, boolean flag, Fragment fragment)
    {
        ArrayMap arraymap = mapEnteringSharedElements(transitionstate, fragment, flag);
        if(flag)
        {
            if(fragment.mExitTransitionCallback != null)
                fragment.mExitTransitionCallback.onMapSharedElements(mSharedElementTargetNames, arraymap);
            setBackNameOverrides(transitionstate, arraymap, true);
        } else
        {
            if(fragment.mEnterTransitionCallback != null)
                fragment.mEnterTransitionCallback.onMapSharedElements(mSharedElementTargetNames, arraymap);
            setNameOverrides(transitionstate, arraymap, true);
        }
        return arraymap;
    }

    private void prepareSharedElementTransition(final TransitionState state, final View sceneRoot, final Object sharedElementTransition, final Fragment inFragment, final Fragment outFragment, final boolean isBack, final ArrayList sharedElementTargets)
    {
        sceneRoot.getViewTreeObserver().addOnPreDrawListener(new android.view.ViewTreeObserver.OnPreDrawListener() {

            public boolean onPreDraw()
            {
                sceneRoot.getViewTreeObserver().removeOnPreDrawListener(this);
                if(sharedElementTransition != null)
                {
                    FragmentTransitionCompat21.removeTargets(sharedElementTransition, sharedElementTargets);
                    sharedElementTargets.clear();
                    ArrayMap arraymap = mapSharedElementsIn(state, isBack, inFragment);
                    FragmentTransitionCompat21.setSharedElementTargets(sharedElementTransition, state.nonExistentView, arraymap, sharedElementTargets);
                    setEpicenterIn(arraymap, state);
                    callSharedElementEnd(state, inFragment, outFragment, isBack, arraymap);
                }
                return true;
            }

            final BackStackRecord this$0;
            final Fragment val$inFragment;
            final boolean val$isBack;
            final Fragment val$outFragment;
            final View val$sceneRoot;
            final ArrayList val$sharedElementTargets;
            final Object val$sharedElementTransition;
            final TransitionState val$state;

            
            {
                this$0 = BackStackRecord.this;
                sceneRoot = view;
                sharedElementTransition = obj;
                sharedElementTargets = arraylist;
                state = transitionstate;
                isBack = flag;
                inFragment = fragment;
                outFragment = fragment1;
                super();
            }
        }
);
    }

    private static ArrayMap remapNames(ArrayList arraylist, ArrayList arraylist1, ArrayMap arraymap)
    {
        if(arraymap.isEmpty())
        {
            arraylist = arraymap;
        } else
        {
            ArrayMap arraymap1 = new ArrayMap();
            int i = arraylist.size();
            for(int j = 0; j < i; j++)
            {
                View view = (View)arraymap.get(arraylist.get(j));
                if(view != null)
                    arraymap1.put(arraylist1.get(j), view);
            }

            arraylist = arraymap1;
        }
        return arraylist;
    }

    private ArrayMap remapSharedElements(TransitionState transitionstate, Fragment fragment, boolean flag)
    {
        ArrayMap arraymap = new ArrayMap();
        ArrayMap arraymap1 = arraymap;
        if(mSharedElementSourceNames != null)
        {
            FragmentTransitionCompat21.findNamedViews(arraymap, fragment.getView());
            if(flag)
            {
                arraymap.retainAll(mSharedElementTargetNames);
                arraymap1 = arraymap;
            } else
            {
                arraymap1 = remapNames(mSharedElementSourceNames, mSharedElementTargetNames, arraymap);
            }
        }
        if(flag)
        {
            if(fragment.mEnterTransitionCallback != null)
                fragment.mEnterTransitionCallback.onMapSharedElements(mSharedElementTargetNames, arraymap1);
            setBackNameOverrides(transitionstate, arraymap1, false);
        } else
        {
            if(fragment.mExitTransitionCallback != null)
                fragment.mExitTransitionCallback.onMapSharedElements(mSharedElementTargetNames, arraymap1);
            setNameOverrides(transitionstate, arraymap1, false);
        }
        return arraymap1;
    }

    private void setBackNameOverrides(TransitionState transitionstate, ArrayMap arraymap, boolean flag)
    {
        int i;
        int j;
        if(mSharedElementTargetNames == null)
            i = 0;
        else
            i = mSharedElementTargetNames.size();
        j = 0;
        while(j < i) 
        {
            String s = (String)mSharedElementSourceNames.get(j);
            Object obj = (View)arraymap.get((String)mSharedElementTargetNames.get(j));
            if(obj != null)
            {
                obj = FragmentTransitionCompat21.getTransitionName(((View) (obj)));
                if(flag)
                    setNameOverride(transitionstate.nameOverrides, s, ((String) (obj)));
                else
                    setNameOverride(transitionstate.nameOverrides, ((String) (obj)), s);
            }
            j++;
        }
    }

    private void setEpicenterIn(ArrayMap arraymap, TransitionState transitionstate)
    {
        if(mSharedElementTargetNames != null && !arraymap.isEmpty())
        {
            arraymap = (View)arraymap.get(mSharedElementTargetNames.get(0));
            if(arraymap != null)
                transitionstate.enteringEpicenterView.epicenter = arraymap;
        }
    }

    private static void setFirstOut(SparseArray sparsearray, SparseArray sparsearray1, Fragment fragment)
    {
        if(fragment != null)
        {
            int i = fragment.mContainerId;
            if(i != 0 && !fragment.isHidden())
            {
                if(fragment.isAdded() && fragment.getView() != null && sparsearray.get(i) == null)
                    sparsearray.put(i, fragment);
                if(sparsearray1.get(i) == fragment)
                    sparsearray1.remove(i);
            }
        }
    }

    private void setLastIn(SparseArray sparsearray, SparseArray sparsearray1, Fragment fragment)
    {
        if(fragment != null)
        {
            int i = fragment.mContainerId;
            if(i != 0)
            {
                if(!fragment.isAdded())
                    sparsearray1.put(i, fragment);
                if(sparsearray.get(i) == fragment)
                    sparsearray.remove(i);
            }
        }
    }

    private static void setNameOverride(ArrayMap arraymap, String s, String s1)
    {
        if(s == null || s1 == null) goto _L2; else goto _L1
_L1:
        int i = 0;
_L5:
        if(i >= arraymap.size())
            break MISSING_BLOCK_LABEL_44;
        if(!s.equals(arraymap.valueAt(i))) goto _L4; else goto _L3
_L3:
        arraymap.setValueAt(i, s1);
_L2:
        return;
_L4:
        i++;
          goto _L5
        arraymap.put(s, s1);
          goto _L2
    }

    private void setNameOverrides(TransitionState transitionstate, ArrayMap arraymap, boolean flag)
    {
        int i = arraymap.size();
        int j = 0;
        while(j < i) 
        {
            String s = (String)arraymap.keyAt(j);
            String s1 = FragmentTransitionCompat21.getTransitionName((View)arraymap.valueAt(j));
            if(flag)
                setNameOverride(transitionstate.nameOverrides, s, s1);
            else
                setNameOverride(transitionstate.nameOverrides, s1, s);
            j++;
        }
    }

    private static void setNameOverrides(TransitionState transitionstate, ArrayList arraylist, ArrayList arraylist1)
    {
        if(arraylist != null)
        {
            for(int i = 0; i < arraylist.size(); i++)
            {
                String s = (String)arraylist.get(i);
                String s1 = (String)arraylist1.get(i);
                setNameOverride(transitionstate.nameOverrides, s, s1);
            }

        }
    }

    public FragmentTransaction add(int i, Fragment fragment)
    {
        doAddOp(i, fragment, null, 1);
        return this;
    }

    public FragmentTransaction add(int i, Fragment fragment, String s)
    {
        doAddOp(i, fragment, s, 1);
        return this;
    }

    public FragmentTransaction add(Fragment fragment, String s)
    {
        doAddOp(0, fragment, s, 1);
        return this;
    }

    void addOp(Op op)
    {
        if(mHead == null)
        {
            mTail = op;
            mHead = op;
        } else
        {
            op.prev = mTail;
            mTail.next = op;
            mTail = op;
        }
        op.enterAnim = mEnterAnim;
        op.exitAnim = mExitAnim;
        op.popEnterAnim = mPopEnterAnim;
        op.popExitAnim = mPopExitAnim;
        mNumOp = mNumOp + 1;
    }

    public FragmentTransaction addSharedElement(View view, String s)
    {
        if(SUPPORTS_TRANSITIONS)
        {
            view = FragmentTransitionCompat21.getTransitionName(view);
            if(view == null)
                throw new IllegalArgumentException("Unique transitionNames are required for all sharedElements");
            if(mSharedElementSourceNames == null)
            {
                mSharedElementSourceNames = new ArrayList();
                mSharedElementTargetNames = new ArrayList();
            }
            mSharedElementSourceNames.add(view);
            mSharedElementTargetNames.add(s);
        }
        return this;
    }

    public FragmentTransaction addToBackStack(String s)
    {
        if(!mAllowAddToBackStack)
        {
            throw new IllegalStateException("This FragmentTransaction is not allowed to be added to the back stack.");
        } else
        {
            mAddToBackStack = true;
            mName = s;
            return this;
        }
    }

    public FragmentTransaction attach(Fragment fragment)
    {
        Op op = new Op();
        op.cmd = 7;
        op.fragment = fragment;
        addOp(op);
        return this;
    }

    void bumpBackStackNesting(int i)
    {
        if(mAddToBackStack)
        {
            if(FragmentManagerImpl.DEBUG)
                Log.v("FragmentManager", (new StringBuilder()).append("Bump nesting in ").append(this).append(" by ").append(i).toString());
            Op op = mHead;
            while(op != null) 
            {
                if(op.fragment != null)
                {
                    Fragment fragment = op.fragment;
                    fragment.mBackStackNesting = fragment.mBackStackNesting + i;
                    if(FragmentManagerImpl.DEBUG)
                        Log.v("FragmentManager", (new StringBuilder()).append("Bump nesting of ").append(op.fragment).append(" to ").append(op.fragment.mBackStackNesting).toString());
                }
                if(op.removed != null)
                {
                    for(int j = op.removed.size() - 1; j >= 0; j--)
                    {
                        Fragment fragment1 = (Fragment)op.removed.get(j);
                        fragment1.mBackStackNesting = fragment1.mBackStackNesting + i;
                        if(FragmentManagerImpl.DEBUG)
                            Log.v("FragmentManager", (new StringBuilder()).append("Bump nesting of ").append(fragment1).append(" to ").append(fragment1.mBackStackNesting).toString());
                    }

                }
                op = op.next;
            }
        }
    }

    public void calculateBackFragments(SparseArray sparsearray, SparseArray sparsearray1)
    {
        if(mManager.mContainer.onHasView()) goto _L2; else goto _L1
_L1:
        return;
_L2:
        Op op = mTail;
_L12:
        if(op == null) goto _L1; else goto _L3
_L3:
        op.cmd;
        JVM INSTR tableswitch 1 7: default 68
    //                   1 76
    //                   2 88
    //                   3 147
    //                   4 160
    //                   5 173
    //                   6 185
    //                   7 198;
           goto _L4 _L5 _L6 _L7 _L8 _L9 _L10 _L11
_L4:
        break; /* Loop/switch isn't completed */
_L11:
        break MISSING_BLOCK_LABEL_198;
_L13:
        op = op.prev;
          goto _L12
_L5:
        setFirstOut(sparsearray, sparsearray1, op.fragment);
          goto _L13
_L6:
        if(op.removed != null)
        {
            for(int i = op.removed.size() - 1; i >= 0; i--)
                setLastIn(sparsearray, sparsearray1, (Fragment)op.removed.get(i));

        }
        setFirstOut(sparsearray, sparsearray1, op.fragment);
          goto _L13
_L7:
        setLastIn(sparsearray, sparsearray1, op.fragment);
          goto _L13
_L8:
        setLastIn(sparsearray, sparsearray1, op.fragment);
          goto _L13
_L9:
        setFirstOut(sparsearray, sparsearray1, op.fragment);
          goto _L13
_L10:
        setLastIn(sparsearray, sparsearray1, op.fragment);
          goto _L13
        setFirstOut(sparsearray, sparsearray1, op.fragment);
          goto _L13
    }

    public int commit()
    {
        return commitInternal(false);
    }

    public int commitAllowingStateLoss()
    {
        return commitInternal(true);
    }

    int commitInternal(boolean flag)
    {
        if(mCommitted)
            throw new IllegalStateException("commit already called");
        if(FragmentManagerImpl.DEBUG)
        {
            Log.v("FragmentManager", (new StringBuilder()).append("Commit: ").append(this).toString());
            dump("  ", null, new PrintWriter(new LogWriter("FragmentManager")), null);
        }
        mCommitted = true;
        if(mAddToBackStack)
            mIndex = mManager.allocBackStackIndex(this);
        else
            mIndex = -1;
        mManager.enqueueAction(this, flag);
        return mIndex;
    }

    public FragmentTransaction detach(Fragment fragment)
    {
        Op op = new Op();
        op.cmd = 6;
        op.fragment = fragment;
        addOp(op);
        return this;
    }

    public FragmentTransaction disallowAddToBackStack()
    {
        if(mAddToBackStack)
        {
            throw new IllegalStateException("This transaction is already being added to the back stack");
        } else
        {
            mAllowAddToBackStack = false;
            return this;
        }
    }

    public void dump(String s, FileDescriptor filedescriptor, PrintWriter printwriter, String as[])
    {
        dump(s, printwriter, true);
    }

    public void dump(String s, PrintWriter printwriter, boolean flag)
    {
        String s1;
        Op op;
        int i;
        if(flag)
        {
            printwriter.print(s);
            printwriter.print("mName=");
            printwriter.print(mName);
            printwriter.print(" mIndex=");
            printwriter.print(mIndex);
            printwriter.print(" mCommitted=");
            printwriter.println(mCommitted);
            if(mTransition != 0)
            {
                printwriter.print(s);
                printwriter.print("mTransition=#");
                printwriter.print(Integer.toHexString(mTransition));
                printwriter.print(" mTransitionStyle=#");
                printwriter.println(Integer.toHexString(mTransitionStyle));
            }
            if(mEnterAnim != 0 || mExitAnim != 0)
            {
                printwriter.print(s);
                printwriter.print("mEnterAnim=#");
                printwriter.print(Integer.toHexString(mEnterAnim));
                printwriter.print(" mExitAnim=#");
                printwriter.println(Integer.toHexString(mExitAnim));
            }
            if(mPopEnterAnim != 0 || mPopExitAnim != 0)
            {
                printwriter.print(s);
                printwriter.print("mPopEnterAnim=#");
                printwriter.print(Integer.toHexString(mPopEnterAnim));
                printwriter.print(" mPopExitAnim=#");
                printwriter.println(Integer.toHexString(mPopExitAnim));
            }
            if(mBreadCrumbTitleRes != 0 || mBreadCrumbTitleText != null)
            {
                printwriter.print(s);
                printwriter.print("mBreadCrumbTitleRes=#");
                printwriter.print(Integer.toHexString(mBreadCrumbTitleRes));
                printwriter.print(" mBreadCrumbTitleText=");
                printwriter.println(mBreadCrumbTitleText);
            }
            if(mBreadCrumbShortTitleRes != 0 || mBreadCrumbShortTitleText != null)
            {
                printwriter.print(s);
                printwriter.print("mBreadCrumbShortTitleRes=#");
                printwriter.print(Integer.toHexString(mBreadCrumbShortTitleRes));
                printwriter.print(" mBreadCrumbShortTitleText=");
                printwriter.println(mBreadCrumbShortTitleText);
            }
        }
        if(mHead == null)
            break MISSING_BLOCK_LABEL_817;
        printwriter.print(s);
        printwriter.println("Operations:");
        s1 = (new StringBuilder()).append(s).append("    ").toString();
        op = mHead;
        i = 0;
_L13:
        if(op == null) goto _L2; else goto _L1
_L1:
        op.cmd;
        JVM INSTR tableswitch 0 7: default 424
    //                   0 699
    //                   1 707
    //                   2 715
    //                   3 723
    //                   4 731
    //                   5 739
    //                   6 747
    //                   7 755;
           goto _L3 _L4 _L5 _L6 _L7 _L8 _L9 _L10 _L11
_L11:
        break MISSING_BLOCK_LABEL_755;
_L3:
        String s2 = (new StringBuilder()).append("cmd=").append(op.cmd).toString();
_L12:
        printwriter.print(s);
        printwriter.print("  Op #");
        printwriter.print(i);
        printwriter.print(": ");
        printwriter.print(s2);
        printwriter.print(" ");
        printwriter.println(op.fragment);
        if(flag)
        {
            if(op.enterAnim != 0 || op.exitAnim != 0)
            {
                printwriter.print(s);
                printwriter.print("enterAnim=#");
                printwriter.print(Integer.toHexString(op.enterAnim));
                printwriter.print(" exitAnim=#");
                printwriter.println(Integer.toHexString(op.exitAnim));
            }
            if(op.popEnterAnim != 0 || op.popExitAnim != 0)
            {
                printwriter.print(s);
                printwriter.print("popEnterAnim=#");
                printwriter.print(Integer.toHexString(op.popEnterAnim));
                printwriter.print(" popExitAnim=#");
                printwriter.println(Integer.toHexString(op.popExitAnim));
            }
        }
        if(op.removed != null && op.removed.size() > 0)
        {
            int j = 0;
            while(j < op.removed.size()) 
            {
                printwriter.print(s1);
                if(op.removed.size() == 1)
                {
                    printwriter.print("Removed: ");
                } else
                {
                    if(j == 0)
                        printwriter.println("Removed:");
                    printwriter.print(s1);
                    printwriter.print("  #");
                    printwriter.print(j);
                    printwriter.print(": ");
                }
                printwriter.println(op.removed.get(j));
                j++;
            }
        }
        break MISSING_BLOCK_LABEL_804;
_L4:
        s2 = "NULL";
          goto _L12
_L5:
        s2 = "ADD";
          goto _L12
_L6:
        s2 = "REPLACE";
          goto _L12
_L7:
        s2 = "REMOVE";
          goto _L12
_L8:
        s2 = "HIDE";
          goto _L12
_L9:
        s2 = "SHOW";
          goto _L12
_L10:
        s2 = "DETACH";
          goto _L12
        s2 = "ATTACH";
          goto _L12
        op = op.next;
        i++;
          goto _L13
_L2:
    }

    public CharSequence getBreadCrumbShortTitle()
    {
        CharSequence charsequence;
        if(mBreadCrumbShortTitleRes != 0)
            charsequence = mManager.mHost.getContext().getText(mBreadCrumbShortTitleRes);
        else
            charsequence = mBreadCrumbShortTitleText;
        return charsequence;
    }

    public int getBreadCrumbShortTitleRes()
    {
        return mBreadCrumbShortTitleRes;
    }

    public CharSequence getBreadCrumbTitle()
    {
        CharSequence charsequence;
        if(mBreadCrumbTitleRes != 0)
            charsequence = mManager.mHost.getContext().getText(mBreadCrumbTitleRes);
        else
            charsequence = mBreadCrumbTitleText;
        return charsequence;
    }

    public int getBreadCrumbTitleRes()
    {
        return mBreadCrumbTitleRes;
    }

    public int getId()
    {
        return mIndex;
    }

    public String getName()
    {
        return mName;
    }

    public int getTransition()
    {
        return mTransition;
    }

    public int getTransitionStyle()
    {
        return mTransitionStyle;
    }

    public FragmentTransaction hide(Fragment fragment)
    {
        Op op = new Op();
        op.cmd = 4;
        op.fragment = fragment;
        addOp(op);
        return this;
    }

    public boolean isAddToBackStackAllowed()
    {
        return mAllowAddToBackStack;
    }

    public boolean isEmpty()
    {
        boolean flag;
        if(mNumOp == 0)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public TransitionState popFromBackStack(boolean flag, TransitionState transitionstate, SparseArray sparsearray, SparseArray sparsearray1)
    {
        TransitionState transitionstate1;
        if(FragmentManagerImpl.DEBUG)
        {
            Log.v("FragmentManager", (new StringBuilder()).append("popFromBackStack: ").append(this).toString());
            dump("  ", null, new PrintWriter(new LogWriter("FragmentManager")), null);
        }
        transitionstate1 = transitionstate;
        if(!SUPPORTS_TRANSITIONS) goto _L2; else goto _L1
_L1:
        if(transitionstate != null) goto _L4; else goto _L3
_L3:
        if(sparsearray.size() != 0) goto _L6; else goto _L5
_L5:
        transitionstate1 = transitionstate;
        if(sparsearray1.size() == 0) goto _L2; else goto _L6
_L6:
        transitionstate1 = beginTransition(sparsearray, sparsearray1, true);
_L2:
        int i;
        int j;
        int k;
        int l;
        bumpBackStackNesting(-1);
        if(transitionstate1 != null)
            i = 0;
        else
            i = mTransitionStyle;
        if(transitionstate1 != null)
            j = 0;
        else
            j = mTransition;
        transitionstate = mTail;
_L17:
        if(transitionstate == null) goto _L8; else goto _L7
_L7:
        if(transitionstate1 != null)
            k = 0;
        else
            k = ((Op) (transitionstate)).popEnterAnim;
        if(transitionstate1 != null)
            l = 0;
        else
            l = ((Op) (transitionstate)).popExitAnim;
        ((Op) (transitionstate)).cmd;
        JVM INSTR tableswitch 1 7: default 192
    //                   1 284
    //                   2 318
    //                   3 404
    //                   4 427
    //                   5 456
    //                   6 485
    //                   7 514;
           goto _L9 _L10 _L11 _L12 _L13 _L14 _L15 _L16
_L9:
        throw new IllegalArgumentException((new StringBuilder()).append("Unknown cmd: ").append(((Op) (transitionstate)).cmd).toString());
_L4:
        transitionstate1 = transitionstate;
        if(!flag)
        {
            setNameOverrides(transitionstate, mSharedElementTargetNames, mSharedElementSourceNames);
            transitionstate1 = transitionstate;
        }
          goto _L2
_L10:
        sparsearray = ((Op) (transitionstate)).fragment;
        sparsearray.mNextAnim = l;
        mManager.removeFragment(sparsearray, FragmentManagerImpl.reverseTransit(j), i);
_L18:
        transitionstate = ((Op) (transitionstate)).prev;
          goto _L17
_L11:
        sparsearray = ((Op) (transitionstate)).fragment;
        if(sparsearray != null)
        {
            sparsearray.mNextAnim = l;
            mManager.removeFragment(sparsearray, FragmentManagerImpl.reverseTransit(j), i);
        }
        if(((Op) (transitionstate)).removed != null)
        {
            l = 0;
            while(l < ((Op) (transitionstate)).removed.size()) 
            {
                sparsearray = (Fragment)((Op) (transitionstate)).removed.get(l);
                sparsearray.mNextAnim = k;
                mManager.addFragment(sparsearray, false);
                l++;
            }
        }
          goto _L18
_L12:
        sparsearray = ((Op) (transitionstate)).fragment;
        sparsearray.mNextAnim = k;
        mManager.addFragment(sparsearray, false);
          goto _L18
_L13:
        sparsearray = ((Op) (transitionstate)).fragment;
        sparsearray.mNextAnim = k;
        mManager.showFragment(sparsearray, FragmentManagerImpl.reverseTransit(j), i);
          goto _L18
_L14:
        sparsearray = ((Op) (transitionstate)).fragment;
        sparsearray.mNextAnim = l;
        mManager.hideFragment(sparsearray, FragmentManagerImpl.reverseTransit(j), i);
          goto _L18
_L15:
        sparsearray = ((Op) (transitionstate)).fragment;
        sparsearray.mNextAnim = k;
        mManager.attachFragment(sparsearray, FragmentManagerImpl.reverseTransit(j), i);
          goto _L18
_L16:
        sparsearray = ((Op) (transitionstate)).fragment;
        sparsearray.mNextAnim = k;
        mManager.detachFragment(sparsearray, FragmentManagerImpl.reverseTransit(j), i);
          goto _L18
_L8:
        if(flag)
        {
            mManager.moveToState(mManager.mCurState, FragmentManagerImpl.reverseTransit(j), i, true);
            transitionstate1 = null;
        }
        if(mIndex >= 0)
        {
            mManager.freeBackStackIndex(mIndex);
            mIndex = -1;
        }
        return transitionstate1;
    }

    public FragmentTransaction remove(Fragment fragment)
    {
        Op op = new Op();
        op.cmd = 3;
        op.fragment = fragment;
        addOp(op);
        return this;
    }

    public FragmentTransaction replace(int i, Fragment fragment)
    {
        return replace(i, fragment, null);
    }

    public FragmentTransaction replace(int i, Fragment fragment, String s)
    {
        if(i == 0)
        {
            throw new IllegalArgumentException("Must use non-zero containerViewId");
        } else
        {
            doAddOp(i, fragment, s, 2);
            return this;
        }
    }

    public void run()
    {
        int i;
        int j;
        Op op;
        int k;
        int l;
        if(FragmentManagerImpl.DEBUG)
            Log.v("FragmentManager", (new StringBuilder()).append("Run: ").append(this).toString());
        if(mAddToBackStack && mIndex < 0)
            throw new IllegalStateException("addToBackStack() called after commit()");
        bumpBackStackNesting(1);
        Object obj = null;
        if(SUPPORTS_TRANSITIONS)
        {
            obj = new SparseArray();
            SparseArray sparsearray = new SparseArray();
            calculateFragments(((SparseArray) (obj)), sparsearray);
            obj = beginTransition(((SparseArray) (obj)), sparsearray, false);
        }
        if(obj != null)
            i = 0;
        else
            i = mTransitionStyle;
        if(obj != null)
            j = 0;
        else
            j = mTransition;
        op = mHead;
_L10:
        if(op == null)
            break MISSING_BLOCK_LABEL_705;
        if(obj != null)
            k = 0;
        else
            k = op.enterAnim;
        if(obj != null)
            l = 0;
        else
            l = op.exitAnim;
        op.cmd;
        JVM INSTR tableswitch 1 7: default 184
    //                   1 253
    //                   2 284
    //                   3 575
    //                   4 601
    //                   5 627
    //                   6 653
    //                   7 679;
           goto _L1 _L2 _L3 _L4 _L5 _L6 _L7 _L8
_L8:
        break MISSING_BLOCK_LABEL_679;
_L3:
        break; /* Loop/switch isn't completed */
_L1:
        throw new IllegalArgumentException((new StringBuilder()).append("Unknown cmd: ").append(op.cmd).toString());
_L2:
        Fragment fragment = op.fragment;
        fragment.mNextAnim = k;
        mManager.addFragment(fragment, false);
_L11:
        op = op.next;
        if(true) goto _L10; else goto _L9
_L9:
        Fragment fragment1 = op.fragment;
        int i1 = fragment1.mContainerId;
        Fragment fragment7 = fragment1;
        if(mManager.mAdded != null)
        {
            int j1 = mManager.mAdded.size() - 1;
            do
            {
                fragment7 = fragment1;
                if(j1 < 0)
                    break;
                Fragment fragment8 = (Fragment)mManager.mAdded.get(j1);
                if(FragmentManagerImpl.DEBUG)
                    Log.v("FragmentManager", (new StringBuilder()).append("OP_REPLACE: adding=").append(fragment1).append(" old=").append(fragment8).toString());
                fragment7 = fragment1;
                if(fragment8.mContainerId == i1)
                    if(fragment8 == fragment1)
                    {
                        fragment7 = null;
                        op.fragment = null;
                    } else
                    {
                        if(op.removed == null)
                            op.removed = new ArrayList();
                        op.removed.add(fragment8);
                        fragment8.mNextAnim = l;
                        if(mAddToBackStack)
                        {
                            fragment8.mBackStackNesting = fragment8.mBackStackNesting + 1;
                            if(FragmentManagerImpl.DEBUG)
                                Log.v("FragmentManager", (new StringBuilder()).append("Bump nesting of ").append(fragment8).append(" to ").append(fragment8.mBackStackNesting).toString());
                        }
                        mManager.removeFragment(fragment8, j, i);
                        fragment7 = fragment1;
                    }
                j1--;
                fragment1 = fragment7;
            } while(true);
        }
        if(fragment7 != null)
        {
            fragment7.mNextAnim = k;
            mManager.addFragment(fragment7, false);
        }
          goto _L11
_L4:
        Fragment fragment2 = op.fragment;
        fragment2.mNextAnim = l;
        mManager.removeFragment(fragment2, j, i);
          goto _L11
_L5:
        Fragment fragment3 = op.fragment;
        fragment3.mNextAnim = l;
        mManager.hideFragment(fragment3, j, i);
          goto _L11
_L6:
        Fragment fragment4 = op.fragment;
        fragment4.mNextAnim = k;
        mManager.showFragment(fragment4, j, i);
          goto _L11
_L7:
        Fragment fragment5 = op.fragment;
        fragment5.mNextAnim = l;
        mManager.detachFragment(fragment5, j, i);
          goto _L11
        Fragment fragment6 = op.fragment;
        fragment6.mNextAnim = k;
        mManager.attachFragment(fragment6, j, i);
          goto _L11
        mManager.moveToState(mManager.mCurState, j, i, true);
        if(mAddToBackStack)
            mManager.addBackStackState(this);
        return;
    }

    public FragmentTransaction setBreadCrumbShortTitle(int i)
    {
        mBreadCrumbShortTitleRes = i;
        mBreadCrumbShortTitleText = null;
        return this;
    }

    public FragmentTransaction setBreadCrumbShortTitle(CharSequence charsequence)
    {
        mBreadCrumbShortTitleRes = 0;
        mBreadCrumbShortTitleText = charsequence;
        return this;
    }

    public FragmentTransaction setBreadCrumbTitle(int i)
    {
        mBreadCrumbTitleRes = i;
        mBreadCrumbTitleText = null;
        return this;
    }

    public FragmentTransaction setBreadCrumbTitle(CharSequence charsequence)
    {
        mBreadCrumbTitleRes = 0;
        mBreadCrumbTitleText = charsequence;
        return this;
    }

    public FragmentTransaction setCustomAnimations(int i, int j)
    {
        return setCustomAnimations(i, j, 0, 0);
    }

    public FragmentTransaction setCustomAnimations(int i, int j, int k, int l)
    {
        mEnterAnim = i;
        mExitAnim = j;
        mPopEnterAnim = k;
        mPopExitAnim = l;
        return this;
    }

    public FragmentTransaction setTransition(int i)
    {
        mTransition = i;
        return this;
    }

    public FragmentTransaction setTransitionStyle(int i)
    {
        mTransitionStyle = i;
        return this;
    }

    public FragmentTransaction show(Fragment fragment)
    {
        Op op = new Op();
        op.cmd = 5;
        op.fragment = fragment;
        addOp(op);
        return this;
    }

    public String toString()
    {
        StringBuilder stringbuilder = new StringBuilder(128);
        stringbuilder.append("BackStackEntry{");
        stringbuilder.append(Integer.toHexString(System.identityHashCode(this)));
        if(mIndex >= 0)
        {
            stringbuilder.append(" #");
            stringbuilder.append(mIndex);
        }
        if(mName != null)
        {
            stringbuilder.append(" ");
            stringbuilder.append(mName);
        }
        stringbuilder.append("}");
        return stringbuilder.toString();
    }

    static final int OP_ADD = 1;
    static final int OP_ATTACH = 7;
    static final int OP_DETACH = 6;
    static final int OP_HIDE = 4;
    static final int OP_NULL = 0;
    static final int OP_REMOVE = 3;
    static final int OP_REPLACE = 2;
    static final int OP_SHOW = 5;
    static final boolean SUPPORTS_TRANSITIONS;
    static final String TAG = "FragmentManager";
    boolean mAddToBackStack;
    boolean mAllowAddToBackStack;
    int mBreadCrumbShortTitleRes;
    CharSequence mBreadCrumbShortTitleText;
    int mBreadCrumbTitleRes;
    CharSequence mBreadCrumbTitleText;
    boolean mCommitted;
    int mEnterAnim;
    int mExitAnim;
    Op mHead;
    int mIndex;
    final FragmentManagerImpl mManager;
    String mName;
    int mNumOp;
    int mPopEnterAnim;
    int mPopExitAnim;
    ArrayList mSharedElementSourceNames;
    ArrayList mSharedElementTargetNames;
    Op mTail;
    int mTransition;
    int mTransitionStyle;

    static 
    {
        boolean flag;
        if(android.os.Build.VERSION.SDK_INT >= 21)
            flag = true;
        else
            flag = false;
        SUPPORTS_TRANSITIONS = flag;
    }




}
