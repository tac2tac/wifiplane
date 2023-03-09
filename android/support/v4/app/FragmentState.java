// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.app;

import android.content.Context;
import android.os.*;
import android.util.Log;

// Referenced classes of package android.support.v4.app:
//            Fragment, FragmentHostCallback, FragmentManagerImpl

final class FragmentState
    implements Parcelable
{

    public FragmentState(Parcel parcel)
    {
        boolean flag = true;
        super();
        mClassName = parcel.readString();
        mIndex = parcel.readInt();
        boolean flag1;
        if(parcel.readInt() != 0)
            flag1 = true;
        else
            flag1 = false;
        mFromLayout = flag1;
        mFragmentId = parcel.readInt();
        mContainerId = parcel.readInt();
        mTag = parcel.readString();
        if(parcel.readInt() != 0)
            flag1 = true;
        else
            flag1 = false;
        mRetainInstance = flag1;
        if(parcel.readInt() != 0)
            flag1 = flag;
        else
            flag1 = false;
        mDetached = flag1;
        mArguments = parcel.readBundle();
        mSavedFragmentState = parcel.readBundle();
    }

    public FragmentState(Fragment fragment)
    {
        mClassName = fragment.getClass().getName();
        mIndex = fragment.mIndex;
        mFromLayout = fragment.mFromLayout;
        mFragmentId = fragment.mFragmentId;
        mContainerId = fragment.mContainerId;
        mTag = fragment.mTag;
        mRetainInstance = fragment.mRetainInstance;
        mDetached = fragment.mDetached;
        mArguments = fragment.mArguments;
    }

    public int describeContents()
    {
        return 0;
    }

    public Fragment instantiate(FragmentHostCallback fragmenthostcallback, Fragment fragment)
    {
        if(mInstance != null)
        {
            fragmenthostcallback = mInstance;
        } else
        {
            Context context = fragmenthostcallback.getContext();
            if(mArguments != null)
                mArguments.setClassLoader(context.getClassLoader());
            mInstance = Fragment.instantiate(context, mClassName, mArguments);
            if(mSavedFragmentState != null)
            {
                mSavedFragmentState.setClassLoader(context.getClassLoader());
                mInstance.mSavedFragmentState = mSavedFragmentState;
            }
            mInstance.setIndex(mIndex, fragment);
            mInstance.mFromLayout = mFromLayout;
            mInstance.mRestored = true;
            mInstance.mFragmentId = mFragmentId;
            mInstance.mContainerId = mContainerId;
            mInstance.mTag = mTag;
            mInstance.mRetainInstance = mRetainInstance;
            mInstance.mDetached = mDetached;
            mInstance.mFragmentManager = fragmenthostcallback.mFragmentManager;
            if(FragmentManagerImpl.DEBUG)
                Log.v("FragmentManager", (new StringBuilder()).append("Instantiated fragment ").append(mInstance).toString());
            fragmenthostcallback = mInstance;
        }
        return fragmenthostcallback;
    }

    public void writeToParcel(Parcel parcel, int i)
    {
        boolean flag = true;
        parcel.writeString(mClassName);
        parcel.writeInt(mIndex);
        if(mFromLayout)
            i = 1;
        else
            i = 0;
        parcel.writeInt(i);
        parcel.writeInt(mFragmentId);
        parcel.writeInt(mContainerId);
        parcel.writeString(mTag);
        if(mRetainInstance)
            i = 1;
        else
            i = 0;
        parcel.writeInt(i);
        if(mDetached)
            i = ((flag) ? 1 : 0);
        else
            i = 0;
        parcel.writeInt(i);
        parcel.writeBundle(mArguments);
        parcel.writeBundle(mSavedFragmentState);
    }

    public static final android.os.Parcelable.Creator CREATOR = new android.os.Parcelable.Creator() {

        public FragmentState createFromParcel(Parcel parcel)
        {
            return new FragmentState(parcel);
        }

        public volatile Object createFromParcel(Parcel parcel)
        {
            return createFromParcel(parcel);
        }

        public FragmentState[] newArray(int i)
        {
            return new FragmentState[i];
        }

        public volatile Object[] newArray(int i)
        {
            return newArray(i);
        }

    }
;
    final Bundle mArguments;
    final String mClassName;
    final int mContainerId;
    final boolean mDetached;
    final int mFragmentId;
    final boolean mFromLayout;
    final int mIndex;
    Fragment mInstance;
    final boolean mRetainInstance;
    Bundle mSavedFragmentState;
    final String mTag;

}
