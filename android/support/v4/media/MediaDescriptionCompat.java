// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.media;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.*;
import android.text.TextUtils;

// Referenced classes of package android.support.v4.media:
//            MediaDescriptionCompatApi21, MediaDescriptionCompatApi23

public final class MediaDescriptionCompat
    implements Parcelable
{
    public static final class Builder
    {

        public MediaDescriptionCompat build()
        {
            return new MediaDescriptionCompat(mMediaId, mTitle, mSubtitle, mDescription, mIcon, mIconUri, mExtras, mMediaUri);
        }

        public Builder setDescription(CharSequence charsequence)
        {
            mDescription = charsequence;
            return this;
        }

        public Builder setExtras(Bundle bundle)
        {
            mExtras = bundle;
            return this;
        }

        public Builder setIconBitmap(Bitmap bitmap)
        {
            mIcon = bitmap;
            return this;
        }

        public Builder setIconUri(Uri uri)
        {
            mIconUri = uri;
            return this;
        }

        public Builder setMediaId(String s)
        {
            mMediaId = s;
            return this;
        }

        public Builder setMediaUri(Uri uri)
        {
            mMediaUri = uri;
            return this;
        }

        public Builder setSubtitle(CharSequence charsequence)
        {
            mSubtitle = charsequence;
            return this;
        }

        public Builder setTitle(CharSequence charsequence)
        {
            mTitle = charsequence;
            return this;
        }

        private CharSequence mDescription;
        private Bundle mExtras;
        private Bitmap mIcon;
        private Uri mIconUri;
        private String mMediaId;
        private Uri mMediaUri;
        private CharSequence mSubtitle;
        private CharSequence mTitle;

        public Builder()
        {
        }
    }


    private MediaDescriptionCompat(Parcel parcel)
    {
        mMediaId = parcel.readString();
        mTitle = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        mSubtitle = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        mDescription = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        mIcon = (Bitmap)parcel.readParcelable(null);
        mIconUri = (Uri)parcel.readParcelable(null);
        mExtras = parcel.readBundle();
        mMediaUri = (Uri)parcel.readParcelable(null);
    }


    private MediaDescriptionCompat(String s, CharSequence charsequence, CharSequence charsequence1, CharSequence charsequence2, Bitmap bitmap, Uri uri, Bundle bundle, 
            Uri uri1)
    {
        mMediaId = s;
        mTitle = charsequence;
        mSubtitle = charsequence1;
        mDescription = charsequence2;
        mIcon = bitmap;
        mIconUri = uri;
        mExtras = bundle;
        mMediaUri = uri1;
    }


    public static MediaDescriptionCompat fromMediaDescription(Object obj)
    {
        Object obj1;
        Object obj2;
        obj1 = null;
        obj2 = obj1;
        if(obj == null) goto _L2; else goto _L1
_L1:
        if(android.os.Build.VERSION.SDK_INT >= 21) goto _L4; else goto _L3
_L3:
        obj2 = obj1;
_L2:
        return ((MediaDescriptionCompat) (obj2));
_L4:
        Builder builder;
        builder = new Builder();
        builder.setMediaId(MediaDescriptionCompatApi21.getMediaId(obj));
        builder.setTitle(MediaDescriptionCompatApi21.getTitle(obj));
        builder.setSubtitle(MediaDescriptionCompatApi21.getSubtitle(obj));
        builder.setDescription(MediaDescriptionCompatApi21.getDescription(obj));
        builder.setIconBitmap(MediaDescriptionCompatApi21.getIconBitmap(obj));
        builder.setIconUri(MediaDescriptionCompatApi21.getIconUri(obj));
        Bundle bundle1 = MediaDescriptionCompatApi21.getExtras(obj);
        Bundle bundle;
        if(bundle1 == null)
            obj2 = null;
        else
            obj2 = (Uri)bundle1.getParcelable("android.support.v4.media.description.MEDIA_URI");
        bundle = bundle1;
        if(obj2 != null)
            if(bundle1.containsKey("android.support.v4.media.description.NULL_BUNDLE_FLAG") && bundle1.size() == 2)
            {
                bundle = null;
            } else
            {
                bundle1.remove("android.support.v4.media.description.MEDIA_URI");
                bundle1.remove("android.support.v4.media.description.NULL_BUNDLE_FLAG");
                bundle = bundle1;
            }
        builder.setExtras(bundle);
        if(obj2 == null)
            break; /* Loop/switch isn't completed */
        builder.setMediaUri(((Uri) (obj2)));
_L7:
        obj2 = builder.build();
        obj2.mDescriptionObj = obj;
        if(true) goto _L2; else goto _L5
_L5:
        if(android.os.Build.VERSION.SDK_INT < 23) goto _L7; else goto _L6
_L6:
        builder.setMediaUri(MediaDescriptionCompatApi23.getMediaUri(obj));
          goto _L7
    }

    public int describeContents()
    {
        return 0;
    }

    public CharSequence getDescription()
    {
        return mDescription;
    }

    public Bundle getExtras()
    {
        return mExtras;
    }

    public Bitmap getIconBitmap()
    {
        return mIcon;
    }

    public Uri getIconUri()
    {
        return mIconUri;
    }

    public Object getMediaDescription()
    {
        Object obj;
        if(mDescriptionObj != null || android.os.Build.VERSION.SDK_INT < 21)
        {
            obj = mDescriptionObj;
        } else
        {
            Object obj1 = MediaDescriptionCompatApi21.Builder.newInstance();
            MediaDescriptionCompatApi21.Builder.setMediaId(obj1, mMediaId);
            MediaDescriptionCompatApi21.Builder.setTitle(obj1, mTitle);
            MediaDescriptionCompatApi21.Builder.setSubtitle(obj1, mSubtitle);
            MediaDescriptionCompatApi21.Builder.setDescription(obj1, mDescription);
            MediaDescriptionCompatApi21.Builder.setIconBitmap(obj1, mIcon);
            MediaDescriptionCompatApi21.Builder.setIconUri(obj1, mIconUri);
            Bundle bundle = mExtras;
            obj = bundle;
            if(android.os.Build.VERSION.SDK_INT < 23)
            {
                obj = bundle;
                if(mMediaUri != null)
                {
                    obj = bundle;
                    if(bundle == null)
                    {
                        obj = new Bundle();
                        ((Bundle) (obj)).putBoolean("android.support.v4.media.description.NULL_BUNDLE_FLAG", true);
                    }
                    ((Bundle) (obj)).putParcelable("android.support.v4.media.description.MEDIA_URI", mMediaUri);
                }
            }
            MediaDescriptionCompatApi21.Builder.setExtras(obj1, ((Bundle) (obj)));
            if(android.os.Build.VERSION.SDK_INT >= 23)
                MediaDescriptionCompatApi23.Builder.setMediaUri(obj1, mMediaUri);
            mDescriptionObj = MediaDescriptionCompatApi21.Builder.build(obj1);
            obj = mDescriptionObj;
        }
        return obj;
    }

    public String getMediaId()
    {
        return mMediaId;
    }

    public Uri getMediaUri()
    {
        return mMediaUri;
    }

    public CharSequence getSubtitle()
    {
        return mSubtitle;
    }

    public CharSequence getTitle()
    {
        return mTitle;
    }

    public String toString()
    {
        return (new StringBuilder()).append(mTitle).append(", ").append(mSubtitle).append(", ").append(mDescription).toString();
    }

    public void writeToParcel(Parcel parcel, int i)
    {
        if(android.os.Build.VERSION.SDK_INT < 21)
        {
            parcel.writeString(mMediaId);
            TextUtils.writeToParcel(mTitle, parcel, i);
            TextUtils.writeToParcel(mSubtitle, parcel, i);
            TextUtils.writeToParcel(mDescription, parcel, i);
            parcel.writeParcelable(mIcon, i);
            parcel.writeParcelable(mIconUri, i);
            parcel.writeBundle(mExtras);
            parcel.writeParcelable(mMediaUri, i);
        } else
        {
            MediaDescriptionCompatApi21.writeToParcel(getMediaDescription(), parcel, i);
        }
    }

    public static final android.os.Parcelable.Creator CREATOR = new android.os.Parcelable.Creator() {

        public MediaDescriptionCompat createFromParcel(Parcel parcel)
        {
            if(android.os.Build.VERSION.SDK_INT < 21)
                parcel = new MediaDescriptionCompat(parcel);
            else
                parcel = MediaDescriptionCompat.fromMediaDescription(MediaDescriptionCompatApi21.fromParcel(parcel));
            return parcel;
        }

        public volatile Object createFromParcel(Parcel parcel)
        {
            return createFromParcel(parcel);
        }

        public MediaDescriptionCompat[] newArray(int i)
        {
            return new MediaDescriptionCompat[i];
        }

        public volatile Object[] newArray(int i)
        {
            return newArray(i);
        }

    }
;
    public static final String DESCRIPTION_KEY_MEDIA_URI = "android.support.v4.media.description.MEDIA_URI";
    public static final String DESCRIPTION_KEY_NULL_BUNDLE_FLAG = "android.support.v4.media.description.NULL_BUNDLE_FLAG";
    private final CharSequence mDescription;
    private Object mDescriptionObj;
    private final Bundle mExtras;
    private final Bitmap mIcon;
    private final Uri mIconUri;
    private final String mMediaId;
    private final Uri mMediaUri;
    private final CharSequence mSubtitle;
    private final CharSequence mTitle;

}
