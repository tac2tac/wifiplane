// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;

// Referenced classes of package android.support.v4.app:
//            BackStackRecord, Fragment, FragmentManagerImpl

final class BackStackState
    implements Parcelable
{

    public BackStackState(Parcel parcel)
    {
        mOps = parcel.createIntArray();
        mTransition = parcel.readInt();
        mTransitionStyle = parcel.readInt();
        mName = parcel.readString();
        mIndex = parcel.readInt();
        mBreadCrumbTitleRes = parcel.readInt();
        mBreadCrumbTitleText = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        mBreadCrumbShortTitleRes = parcel.readInt();
        mBreadCrumbShortTitleText = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        mSharedElementSourceNames = parcel.createStringArrayList();
        mSharedElementTargetNames = parcel.createStringArrayList();
    }

    public BackStackState(BackStackRecord backstackrecord)
    {
        int i = 0;
        for(BackStackRecord.Op op = backstackrecord.mHead; op != null;)
        {
            int l = i;
            if(op.removed != null)
                l = i + op.removed.size();
            op = op.next;
            i = l;
        }

        mOps = new int[backstackrecord.mNumOp * 7 + i];
        if(!backstackrecord.mAddToBackStack)
            throw new IllegalStateException("Not on back stack");
        BackStackRecord.Op op1 = backstackrecord.mHead;
        int i1 = 0;
        while(op1 != null) 
        {
            int ai[] = mOps;
            int j1 = i1 + 1;
            ai[i1] = op1.cmd;
            ai = mOps;
            int j = j1 + 1;
            int l1;
            if(op1.fragment != null)
                i1 = op1.fragment.mIndex;
            else
                i1 = -1;
            ai[j1] = i1;
            ai = mOps;
            j1 = j + 1;
            ai[j] = op1.enterAnim;
            ai = mOps;
            i1 = j1 + 1;
            ai[j1] = op1.exitAnim;
            ai = mOps;
            j = i1 + 1;
            ai[i1] = op1.popEnterAnim;
            ai = mOps;
            l1 = j + 1;
            ai[j] = op1.popExitAnim;
            if(op1.removed != null)
            {
                int k1 = op1.removed.size();
                mOps[l1] = k1;
                int k = 0;
                for(i1 = l1 + 1; k < k1; i1++)
                {
                    mOps[i1] = ((Fragment)op1.removed.get(k)).mIndex;
                    k++;
                }

            } else
            {
                int ai1[] = mOps;
                i1 = l1 + 1;
                ai1[l1] = 0;
            }
            op1 = op1.next;
        }
        mTransition = backstackrecord.mTransition;
        mTransitionStyle = backstackrecord.mTransitionStyle;
        mName = backstackrecord.mName;
        mIndex = backstackrecord.mIndex;
        mBreadCrumbTitleRes = backstackrecord.mBreadCrumbTitleRes;
        mBreadCrumbTitleText = backstackrecord.mBreadCrumbTitleText;
        mBreadCrumbShortTitleRes = backstackrecord.mBreadCrumbShortTitleRes;
        mBreadCrumbShortTitleText = backstackrecord.mBreadCrumbShortTitleText;
        mSharedElementSourceNames = backstackrecord.mSharedElementSourceNames;
        mSharedElementTargetNames = backstackrecord.mSharedElementTargetNames;
    }

    public int describeContents()
    {
        return 0;
    }

    public BackStackRecord instantiate(FragmentManagerImpl fragmentmanagerimpl)
    {
        BackStackRecord backstackrecord = new BackStackRecord(fragmentmanagerimpl);
        int i = 0;
        for(int j = 0; i < mOps.length; j++)
        {
            BackStackRecord.Op op = new BackStackRecord.Op();
            int ai[] = mOps;
            int k = i + 1;
            op.cmd = ai[i];
            if(FragmentManagerImpl.DEBUG)
                Log.v("FragmentManager", (new StringBuilder()).append("Instantiate ").append(backstackrecord).append(" op #").append(j).append(" base fragment #").append(mOps[k]).toString());
            ai = mOps;
            i = k + 1;
            k = ai[k];
            int l;
            int j1;
            if(k >= 0)
                op.fragment = (Fragment)fragmentmanagerimpl.mActive.get(k);
            else
                op.fragment = null;
            ai = mOps;
            k = i + 1;
            op.enterAnim = ai[i];
            ai = mOps;
            i = k + 1;
            op.exitAnim = ai[k];
            ai = mOps;
            k = i + 1;
            op.popEnterAnim = ai[i];
            ai = mOps;
            l = k + 1;
            op.popExitAnim = ai[k];
            ai = mOps;
            i = l + 1;
            j1 = ai[l];
            k = i;
            if(j1 > 0)
            {
                op.removed = new ArrayList(j1);
                int i1 = 0;
                do
                {
                    k = i;
                    if(i1 >= j1)
                        break;
                    if(FragmentManagerImpl.DEBUG)
                        Log.v("FragmentManager", (new StringBuilder()).append("Instantiate ").append(backstackrecord).append(" set remove fragment #").append(mOps[i]).toString());
                    Fragment fragment = (Fragment)fragmentmanagerimpl.mActive.get(mOps[i]);
                    op.removed.add(fragment);
                    i1++;
                    i++;
                } while(true);
            }
            i = k;
            backstackrecord.addOp(op);
        }

        backstackrecord.mTransition = mTransition;
        backstackrecord.mTransitionStyle = mTransitionStyle;
        backstackrecord.mName = mName;
        backstackrecord.mIndex = mIndex;
        backstackrecord.mAddToBackStack = true;
        backstackrecord.mBreadCrumbTitleRes = mBreadCrumbTitleRes;
        backstackrecord.mBreadCrumbTitleText = mBreadCrumbTitleText;
        backstackrecord.mBreadCrumbShortTitleRes = mBreadCrumbShortTitleRes;
        backstackrecord.mBreadCrumbShortTitleText = mBreadCrumbShortTitleText;
        backstackrecord.mSharedElementSourceNames = mSharedElementSourceNames;
        backstackrecord.mSharedElementTargetNames = mSharedElementTargetNames;
        backstackrecord.bumpBackStackNesting(1);
        return backstackrecord;
    }

    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeIntArray(mOps);
        parcel.writeInt(mTransition);
        parcel.writeInt(mTransitionStyle);
        parcel.writeString(mName);
        parcel.writeInt(mIndex);
        parcel.writeInt(mBreadCrumbTitleRes);
        TextUtils.writeToParcel(mBreadCrumbTitleText, parcel, 0);
        parcel.writeInt(mBreadCrumbShortTitleRes);
        TextUtils.writeToParcel(mBreadCrumbShortTitleText, parcel, 0);
        parcel.writeStringList(mSharedElementSourceNames);
        parcel.writeStringList(mSharedElementTargetNames);
    }

    public static final android.os.Parcelable.Creator CREATOR = new android.os.Parcelable.Creator() {

        public BackStackState createFromParcel(Parcel parcel)
        {
            return new BackStackState(parcel);
        }

        public volatile Object createFromParcel(Parcel parcel)
        {
            return createFromParcel(parcel);
        }

        public BackStackState[] newArray(int i)
        {
            return new BackStackState[i];
        }

        public volatile Object[] newArray(int i)
        {
            return newArray(i);
        }

    }
;
    final int mBreadCrumbShortTitleRes;
    final CharSequence mBreadCrumbShortTitleText;
    final int mBreadCrumbTitleRes;
    final CharSequence mBreadCrumbTitleText;
    final int mIndex;
    final String mName;
    final int mOps[];
    final ArrayList mSharedElementSourceNames;
    final ArrayList mSharedElementTargetNames;
    final int mTransition;
    final int mTransitionStyle;

}
