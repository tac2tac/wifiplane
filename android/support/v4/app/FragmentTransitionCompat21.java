// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.app;

import android.graphics.Rect;
import android.transition.*;
import android.view.*;
import java.util.*;

class FragmentTransitionCompat21
{
    public static class EpicenterView
    {

        public View epicenter;

        public EpicenterView()
        {
        }
    }

    public static interface ViewRetriever
    {

        public abstract View getView();
    }


    FragmentTransitionCompat21()
    {
    }

    public static void addTargets(Object obj, ArrayList arraylist)
    {
        obj = (Transition)obj;
        if(obj instanceof TransitionSet)
        {
            obj = (TransitionSet)obj;
            int i = ((TransitionSet) (obj)).getTransitionCount();
            for(int k = 0; k < i; k++)
                addTargets(((TransitionSet) (obj)).getTransitionAt(k), arraylist);

        } else
        if(!hasSimpleTarget(((Transition) (obj))) && isNullOrEmpty(((Transition) (obj)).getTargets()))
        {
            int j = arraylist.size();
            for(int l = 0; l < j; l++)
                ((Transition) (obj)).addTarget((View)arraylist.get(l));

        }
    }

    public static void addTransitionTargets(Object obj, Object obj1, View view, ViewRetriever viewretriever, View view1, EpicenterView epicenterview, Map map, ArrayList arraylist, 
            Map map1, Map map2, ArrayList arraylist1)
    {
        if(obj != null || obj1 != null)
        {
            obj = (Transition)obj;
            if(obj != null)
                ((Transition) (obj)).addTarget(view1);
            if(obj1 != null)
                setSharedElementTargets(obj1, view1, map1, arraylist1);
            if(viewretriever != null)
                view.getViewTreeObserver().addOnPreDrawListener(new android.view.ViewTreeObserver.OnPreDrawListener(view, ((Transition) (obj)), view1, viewretriever, map, map2, arraylist) {

                    public boolean onPreDraw()
                    {
                        container.getViewTreeObserver().removeOnPreDrawListener(this);
                        if(enterTransition != null)
                            enterTransition.removeTarget(nonExistentView);
                        View view2 = inFragment.getView();
                        if(view2 != null)
                        {
                            if(!nameOverrides.isEmpty())
                            {
                                FragmentTransitionCompat21.findNamedViews(renamedViews, view2);
                                renamedViews.keySet().retainAll(nameOverrides.values());
                                Iterator iterator = nameOverrides.entrySet().iterator();
                                do
                                {
                                    if(!iterator.hasNext())
                                        break;
                                    java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
                                    Object obj2 = (String)entry.getValue();
                                    obj2 = (View)renamedViews.get(obj2);
                                    if(obj2 != null)
                                        ((View) (obj2)).setTransitionName((String)entry.getKey());
                                } while(true);
                            }
                            if(enterTransition != null)
                            {
                                FragmentTransitionCompat21.captureTransitioningViews(enteringViews, view2);
                                enteringViews.removeAll(renamedViews.values());
                                enteringViews.add(nonExistentView);
                                FragmentTransitionCompat21.addTargets(enterTransition, enteringViews);
                            }
                        }
                        return true;
                    }

                    final View val$container;
                    final Transition val$enterTransition;
                    final ArrayList val$enteringViews;
                    final ViewRetriever val$inFragment;
                    final Map val$nameOverrides;
                    final View val$nonExistentView;
                    final Map val$renamedViews;

            
            {
                container = view;
                enterTransition = transition;
                nonExistentView = view1;
                inFragment = viewretriever;
                nameOverrides = map;
                renamedViews = map1;
                enteringViews = arraylist;
                super();
            }
                }
);
            setSharedElementEpicenter(((Transition) (obj)), epicenterview);
        }
    }

    public static void beginDelayedTransition(ViewGroup viewgroup, Object obj)
    {
        TransitionManager.beginDelayedTransition(viewgroup, (Transition)obj);
    }

    private static void bfsAddViewChildren(List list, View view)
    {
        int i = list.size();
        if(!containedBeforeIndex(list, view, i))
        {
            list.add(view);
            int j = i;
            while(j < list.size()) 
            {
                view = (View)list.get(j);
                if(view instanceof ViewGroup)
                {
                    view = (ViewGroup)view;
                    int k = view.getChildCount();
                    for(int l = 0; l < k; l++)
                    {
                        View view1 = view.getChildAt(l);
                        if(!containedBeforeIndex(list, view1, i))
                            list.add(view1);
                    }

                }
                j++;
            }
        }
    }

    public static Object captureExitingViews(Object obj, View view, ArrayList arraylist, Map map, View view1)
    {
        Object obj1 = obj;
        if(obj != null)
        {
            captureTransitioningViews(arraylist, view);
            if(map != null)
                arraylist.removeAll(map.values());
            if(arraylist.isEmpty())
            {
                obj1 = null;
            } else
            {
                arraylist.add(view1);
                addTargets((Transition)obj, arraylist);
                obj1 = obj;
            }
        }
        return obj1;
    }

    private static void captureTransitioningViews(ArrayList arraylist, View view)
    {
        if(view.getVisibility() == 0)
            if(view instanceof ViewGroup)
            {
                view = (ViewGroup)view;
                if(view.isTransitionGroup())
                {
                    arraylist.add(view);
                } else
                {
                    int i = view.getChildCount();
                    int j = 0;
                    while(j < i) 
                    {
                        captureTransitioningViews(arraylist, view.getChildAt(j));
                        j++;
                    }
                }
            } else
            {
                arraylist.add(view);
            }
    }

    public static void cleanupTransitions(View view, View view1, Object obj, ArrayList arraylist, Object obj1, ArrayList arraylist1, Object obj2, ArrayList arraylist2, 
            Object obj3, ArrayList arraylist3, Map map)
    {
        obj = (Transition)obj;
        obj1 = (Transition)obj1;
        obj2 = (Transition)obj2;
        obj3 = (Transition)obj3;
        if(obj3 != null)
            view.getViewTreeObserver().addOnPreDrawListener(new android.view.ViewTreeObserver.OnPreDrawListener(view, ((Transition) (obj)), arraylist, ((Transition) (obj1)), arraylist1, ((Transition) (obj2)), arraylist2, map, arraylist3, ((Transition) (obj3)), view1) {

                public boolean onPreDraw()
                {
                    sceneRoot.getViewTreeObserver().removeOnPreDrawListener(this);
                    if(enterTransition != null)
                        FragmentTransitionCompat21.removeTargets(enterTransition, enteringViews);
                    if(exitTransition != null)
                        FragmentTransitionCompat21.removeTargets(exitTransition, exitingViews);
                    if(sharedElementTransition != null)
                        FragmentTransitionCompat21.removeTargets(sharedElementTransition, sharedElementTargets);
                    java.util.Map.Entry entry;
                    for(Iterator iterator = renamedViews.entrySet().iterator(); iterator.hasNext(); ((View)entry.getValue()).setTransitionName((String)entry.getKey()))
                        entry = (java.util.Map.Entry)iterator.next();

                    int i = hiddenViews.size();
                    for(int j = 0; j < i; j++)
                        overallTransition.excludeTarget((View)hiddenViews.get(j), false);

                    overallTransition.excludeTarget(nonExistentView, false);
                    return true;
                }

                final Transition val$enterTransition;
                final ArrayList val$enteringViews;
                final Transition val$exitTransition;
                final ArrayList val$exitingViews;
                final ArrayList val$hiddenViews;
                final View val$nonExistentView;
                final Transition val$overallTransition;
                final Map val$renamedViews;
                final View val$sceneRoot;
                final ArrayList val$sharedElementTargets;
                final Transition val$sharedElementTransition;

            
            {
                sceneRoot = view;
                enterTransition = transition;
                enteringViews = arraylist;
                exitTransition = transition1;
                exitingViews = arraylist1;
                sharedElementTransition = transition2;
                sharedElementTargets = arraylist2;
                renamedViews = map;
                hiddenViews = arraylist3;
                overallTransition = transition3;
                nonExistentView = view1;
                super();
            }
            }
);
    }

    public static Object cloneTransition(Object obj)
    {
        Object obj1 = obj;
        if(obj != null)
            obj1 = ((Transition)obj).clone();
        return obj1;
    }

    private static boolean containedBeforeIndex(List list, View view, int i)
    {
        int j = 0;
_L3:
        if(j >= i)
            break MISSING_BLOCK_LABEL_30;
        if(list.get(j) != view) goto _L2; else goto _L1
_L1:
        boolean flag = true;
_L4:
        return flag;
_L2:
        j++;
          goto _L3
        flag = false;
          goto _L4
    }

    public static void excludeTarget(Object obj, View view, boolean flag)
    {
        ((Transition)obj).excludeTarget(view, flag);
    }

    public static void findNamedViews(Map map, View view)
    {
        if(view.getVisibility() == 0)
        {
            String s = view.getTransitionName();
            if(s != null)
                map.put(s, view);
            if(view instanceof ViewGroup)
            {
                view = (ViewGroup)view;
                int i = view.getChildCount();
                for(int j = 0; j < i; j++)
                    findNamedViews(map, view.getChildAt(j));

            }
        }
    }

    private static Rect getBoundsOnScreen(View view)
    {
        Rect rect = new Rect();
        int ai[] = new int[2];
        view.getLocationOnScreen(ai);
        rect.set(ai[0], ai[1], ai[0] + view.getWidth(), ai[1] + view.getHeight());
        return rect;
    }

    public static String getTransitionName(View view)
    {
        return view.getTransitionName();
    }

    private static boolean hasSimpleTarget(Transition transition)
    {
        boolean flag;
        if(!isNullOrEmpty(transition.getTargetIds()) || !isNullOrEmpty(transition.getTargetNames()) || !isNullOrEmpty(transition.getTargetTypes()))
            flag = true;
        else
            flag = false;
        return flag;
    }

    private static boolean isNullOrEmpty(List list)
    {
        boolean flag;
        if(list == null || list.isEmpty())
            flag = true;
        else
            flag = false;
        return flag;
    }

    public static Object mergeTransitions(Object obj, Object obj1, Object obj2, boolean flag)
    {
        Transition transition;
        boolean flag2;
        boolean flag1 = true;
        transition = (Transition)obj;
        obj = (Transition)obj1;
        obj2 = (Transition)obj2;
        flag2 = flag1;
        if(transition != null)
        {
            flag2 = flag1;
            if(obj != null)
                flag2 = flag;
        }
        if(!flag2) goto _L2; else goto _L1
_L1:
        obj1 = new TransitionSet();
        if(transition != null)
            ((TransitionSet) (obj1)).addTransition(transition);
        if(obj != null)
            ((TransitionSet) (obj1)).addTransition(((Transition) (obj)));
        if(obj2 != null)
            ((TransitionSet) (obj1)).addTransition(((Transition) (obj2)));
        obj = obj1;
_L4:
        return obj;
_L2:
        obj1 = null;
        if(obj == null || transition == null)
            break; /* Loop/switch isn't completed */
        obj = (new TransitionSet()).addTransition(((Transition) (obj))).addTransition(transition).setOrdering(1);
_L5:
        if(obj2 != null)
        {
            obj1 = new TransitionSet();
            if(obj != null)
                ((TransitionSet) (obj1)).addTransition(((Transition) (obj)));
            ((TransitionSet) (obj1)).addTransition(((Transition) (obj2)));
            obj = obj1;
        }
        if(true) goto _L4; else goto _L3
_L3:
        if(obj == null) goto _L6; else goto _L5
_L6:
        obj = obj1;
        if(transition != null)
            obj = transition;
          goto _L5
    }

    public static void removeTargets(Object obj, ArrayList arraylist)
    {
        obj = (Transition)obj;
        if(obj instanceof TransitionSet)
        {
            obj = (TransitionSet)obj;
            int i = ((TransitionSet) (obj)).getTransitionCount();
            for(int j = 0; j < i; j++)
                removeTargets(((TransitionSet) (obj)).getTransitionAt(j), arraylist);

        } else
        if(!hasSimpleTarget(((Transition) (obj))))
        {
            List list = ((Transition) (obj)).getTargets();
            if(list != null && list.size() == arraylist.size() && list.containsAll(arraylist))
            {
                for(int k = arraylist.size() - 1; k >= 0; k--)
                    ((Transition) (obj)).removeTarget((View)arraylist.get(k));

            }
        }
    }

    public static void setEpicenter(Object obj, View view)
    {
        ((Transition)obj).setEpicenterCallback(new android.transition.Transition.EpicenterCallback(getBoundsOnScreen(view)) {

            public Rect onGetEpicenter(Transition transition)
            {
                return epicenter;
            }

            final Rect val$epicenter;

            
            {
                epicenter = rect;
                super();
            }
        }
);
    }

    private static void setSharedElementEpicenter(Transition transition, EpicenterView epicenterview)
    {
        if(transition != null)
            transition.setEpicenterCallback(new android.transition.Transition.EpicenterCallback(epicenterview) {

                public Rect onGetEpicenter(Transition transition1)
                {
                    if(mEpicenter == null && epicenterView.epicenter != null)
                        mEpicenter = FragmentTransitionCompat21.getBoundsOnScreen(epicenterView.epicenter);
                    return mEpicenter;
                }

                private Rect mEpicenter;
                final EpicenterView val$epicenterView;

            
            {
                epicenterView = epicenterview;
                super();
            }
            }
);
    }

    public static void setSharedElementTargets(Object obj, View view, Map map, ArrayList arraylist)
    {
        obj = (TransitionSet)obj;
        arraylist.clear();
        arraylist.addAll(map.values());
        map = ((TransitionSet) (obj)).getTargets();
        map.clear();
        int i = arraylist.size();
        for(int j = 0; j < i; j++)
            bfsAddViewChildren(map, (View)arraylist.get(j));

        arraylist.add(view);
        addTargets(obj, arraylist);
    }

    public static Object wrapSharedElementTransition(Object obj)
    {
        Object obj1 = null;
        if(obj != null) goto _L2; else goto _L1
_L1:
        obj = obj1;
_L4:
        return obj;
_L2:
        Transition transition = (Transition)obj;
        obj = obj1;
        if(transition != null)
        {
            obj = new TransitionSet();
            ((TransitionSet) (obj)).addTransition(transition);
        }
        if(true) goto _L4; else goto _L3
_L3:
    }


}
