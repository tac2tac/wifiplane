// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.*;
import java.lang.annotation.Annotation;

// Referenced classes of package android.support.v4.app:
//            Fragment, FragmentManager, FragmentTransaction, FragmentHostCallback

public class DialogFragment extends Fragment
    implements android.content.DialogInterface.OnCancelListener, android.content.DialogInterface.OnDismissListener
{
    private static interface DialogStyle
        extends Annotation
    {
    }


    public DialogFragment()
    {
        mStyle = 0;
        mTheme = 0;
        mCancelable = true;
        mShowsDialog = true;
        mBackStackId = -1;
    }

    public void dismiss()
    {
        dismissInternal(false);
    }

    public void dismissAllowingStateLoss()
    {
        dismissInternal(true);
    }

    void dismissInternal(boolean flag)
    {
        if(!mDismissed)
        {
            mDismissed = true;
            mShownByMe = false;
            if(mDialog != null)
            {
                mDialog.dismiss();
                mDialog = null;
            }
            mViewDestroyed = true;
            if(mBackStackId >= 0)
            {
                getFragmentManager().popBackStack(mBackStackId, 1);
                mBackStackId = -1;
            } else
            {
                FragmentTransaction fragmenttransaction = getFragmentManager().beginTransaction();
                fragmenttransaction.remove(this);
                if(flag)
                    fragmenttransaction.commitAllowingStateLoss();
                else
                    fragmenttransaction.commit();
            }
        }
    }

    public Dialog getDialog()
    {
        return mDialog;
    }

    public LayoutInflater getLayoutInflater(Bundle bundle)
    {
        if(!mShowsDialog)
        {
            bundle = super.getLayoutInflater(bundle);
        } else
        {
            mDialog = onCreateDialog(bundle);
            if(mDialog != null)
            {
                setupDialog(mDialog, mStyle);
                bundle = (LayoutInflater)mDialog.getContext().getSystemService("layout_inflater");
            } else
            {
                bundle = (LayoutInflater)mHost.getContext().getSystemService("layout_inflater");
            }
        }
        return bundle;
    }

    public boolean getShowsDialog()
    {
        return mShowsDialog;
    }

    public int getTheme()
    {
        return mTheme;
    }

    public boolean isCancelable()
    {
        return mCancelable;
    }

    public void onActivityCreated(Bundle bundle)
    {
        super.onActivityCreated(bundle);
        if(mShowsDialog) goto _L2; else goto _L1
_L1:
        return;
_L2:
        View view = getView();
        if(view != null)
        {
            if(view.getParent() != null)
                throw new IllegalStateException("DialogFragment can not be attached to a container view");
            mDialog.setContentView(view);
        }
        mDialog.setOwnerActivity(getActivity());
        mDialog.setCancelable(mCancelable);
        mDialog.setOnCancelListener(this);
        mDialog.setOnDismissListener(this);
        if(bundle != null)
        {
            bundle = bundle.getBundle("android:savedDialogState");
            if(bundle != null)
                mDialog.onRestoreInstanceState(bundle);
        }
        if(true) goto _L1; else goto _L3
_L3:
    }

    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        if(!mShownByMe)
            mDismissed = false;
    }

    public void onCancel(DialogInterface dialoginterface)
    {
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        boolean flag;
        if(mContainerId == 0)
            flag = true;
        else
            flag = false;
        mShowsDialog = flag;
        if(bundle != null)
        {
            mStyle = bundle.getInt("android:style", 0);
            mTheme = bundle.getInt("android:theme", 0);
            mCancelable = bundle.getBoolean("android:cancelable", true);
            mShowsDialog = bundle.getBoolean("android:showsDialog", mShowsDialog);
            mBackStackId = bundle.getInt("android:backStackId", -1);
        }
    }

    public Dialog onCreateDialog(Bundle bundle)
    {
        return new Dialog(getActivity(), getTheme());
    }

    public void onDestroyView()
    {
        super.onDestroyView();
        if(mDialog != null)
        {
            mViewDestroyed = true;
            mDialog.dismiss();
            mDialog = null;
        }
    }

    public void onDetach()
    {
        super.onDetach();
        if(!mShownByMe && !mDismissed)
            mDismissed = true;
    }

    public void onDismiss(DialogInterface dialoginterface)
    {
        if(!mViewDestroyed)
            dismissInternal(true);
    }

    public void onSaveInstanceState(Bundle bundle)
    {
        super.onSaveInstanceState(bundle);
        if(mDialog != null)
        {
            Bundle bundle1 = mDialog.onSaveInstanceState();
            if(bundle1 != null)
                bundle.putBundle("android:savedDialogState", bundle1);
        }
        if(mStyle != 0)
            bundle.putInt("android:style", mStyle);
        if(mTheme != 0)
            bundle.putInt("android:theme", mTheme);
        if(!mCancelable)
            bundle.putBoolean("android:cancelable", mCancelable);
        if(!mShowsDialog)
            bundle.putBoolean("android:showsDialog", mShowsDialog);
        if(mBackStackId != -1)
            bundle.putInt("android:backStackId", mBackStackId);
    }

    public void onStart()
    {
        super.onStart();
        if(mDialog != null)
        {
            mViewDestroyed = false;
            mDialog.show();
        }
    }

    public void onStop()
    {
        super.onStop();
        if(mDialog != null)
            mDialog.hide();
    }

    public void setCancelable(boolean flag)
    {
        mCancelable = flag;
        if(mDialog != null)
            mDialog.setCancelable(flag);
    }

    public void setShowsDialog(boolean flag)
    {
        mShowsDialog = flag;
    }

    public void setStyle(int i, int j)
    {
        mStyle = i;
        if(mStyle == 2 || mStyle == 3)
            mTheme = 0x1030059;
        if(j != 0)
            mTheme = j;
    }

    public void setupDialog(Dialog dialog, int i)
    {
        i;
        JVM INSTR tableswitch 1 3: default 28
    //                   1 38
    //                   2 38
    //                   3 29;
           goto _L1 _L2 _L2 _L3
_L1:
        return;
_L3:
        dialog.getWindow().addFlags(24);
_L2:
        dialog.requestWindowFeature(1);
        if(true) goto _L1; else goto _L4
_L4:
    }

    public int show(FragmentTransaction fragmenttransaction, String s)
    {
        mDismissed = false;
        mShownByMe = true;
        fragmenttransaction.add(this, s);
        mViewDestroyed = false;
        mBackStackId = fragmenttransaction.commit();
        return mBackStackId;
    }

    public void show(FragmentManager fragmentmanager, String s)
    {
        mDismissed = false;
        mShownByMe = true;
        fragmentmanager = fragmentmanager.beginTransaction();
        fragmentmanager.add(this, s);
        fragmentmanager.commit();
    }

    private static final String SAVED_BACK_STACK_ID = "android:backStackId";
    private static final String SAVED_CANCELABLE = "android:cancelable";
    private static final String SAVED_DIALOG_STATE_TAG = "android:savedDialogState";
    private static final String SAVED_SHOWS_DIALOG = "android:showsDialog";
    private static final String SAVED_STYLE = "android:style";
    private static final String SAVED_THEME = "android:theme";
    public static final int STYLE_NORMAL = 0;
    public static final int STYLE_NO_FRAME = 2;
    public static final int STYLE_NO_INPUT = 3;
    public static final int STYLE_NO_TITLE = 1;
    int mBackStackId;
    boolean mCancelable;
    Dialog mDialog;
    boolean mDismissed;
    boolean mShownByMe;
    boolean mShowsDialog;
    int mStyle;
    int mTheme;
    boolean mViewDestroyed;
}
