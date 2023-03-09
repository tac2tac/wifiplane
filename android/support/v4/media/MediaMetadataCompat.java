// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.media;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.*;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import java.lang.annotation.Annotation;
import java.util.Set;

// Referenced classes of package android.support.v4.media:
//            MediaMetadataCompatApi21, RatingCompat, MediaDescriptionCompat

public final class MediaMetadataCompat
    implements Parcelable
{
    public static interface BitmapKey
        extends Annotation
    {
    }

    public static final class Builder
    {

        public MediaMetadataCompat build()
        {
            return new MediaMetadataCompat(mBundle);
        }

        public Builder putBitmap(String s, Bitmap bitmap)
        {
            if(MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(s) && ((Integer)MediaMetadataCompat.METADATA_KEYS_TYPE.get(s)).intValue() != 2)
            {
                throw new IllegalArgumentException((new StringBuilder()).append("The ").append(s).append(" key cannot be used to put a Bitmap").toString());
            } else
            {
                mBundle.putParcelable(s, bitmap);
                return this;
            }
        }

        public Builder putLong(String s, long l)
        {
            if(MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(s) && ((Integer)MediaMetadataCompat.METADATA_KEYS_TYPE.get(s)).intValue() != 0)
            {
                throw new IllegalArgumentException((new StringBuilder()).append("The ").append(s).append(" key cannot be used to put a long").toString());
            } else
            {
                mBundle.putLong(s, l);
                return this;
            }
        }

        public Builder putRating(String s, RatingCompat ratingcompat)
        {
            if(MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(s) && ((Integer)MediaMetadataCompat.METADATA_KEYS_TYPE.get(s)).intValue() != 3)
                throw new IllegalArgumentException((new StringBuilder()).append("The ").append(s).append(" key cannot be used to put a Rating").toString());
            if(android.os.Build.VERSION.SDK_INT >= 21)
                mBundle.putParcelable(s, (Parcelable)ratingcompat.getRating());
            else
                mBundle.putParcelable(s, ratingcompat);
            return this;
        }

        public Builder putString(String s, String s1)
        {
            if(MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(s) && ((Integer)MediaMetadataCompat.METADATA_KEYS_TYPE.get(s)).intValue() != 1)
            {
                throw new IllegalArgumentException((new StringBuilder()).append("The ").append(s).append(" key cannot be used to put a String").toString());
            } else
            {
                mBundle.putCharSequence(s, s1);
                return this;
            }
        }

        public Builder putText(String s, CharSequence charsequence)
        {
            if(MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(s) && ((Integer)MediaMetadataCompat.METADATA_KEYS_TYPE.get(s)).intValue() != 1)
            {
                throw new IllegalArgumentException((new StringBuilder()).append("The ").append(s).append(" key cannot be used to put a CharSequence").toString());
            } else
            {
                mBundle.putCharSequence(s, charsequence);
                return this;
            }
        }

        private final Bundle mBundle;

        public Builder()
        {
            mBundle = new Bundle();
        }

        public Builder(MediaMetadataCompat mediametadatacompat)
        {
            mBundle = new Bundle(mediametadatacompat.mBundle);
        }
    }

    public static interface LongKey
        extends Annotation
    {
    }

    public static interface RatingKey
        extends Annotation
    {
    }

    public static interface TextKey
        extends Annotation
    {
    }


    private MediaMetadataCompat(Bundle bundle)
    {
        mBundle = new Bundle(bundle);
    }


    private MediaMetadataCompat(Parcel parcel)
    {
        mBundle = parcel.readBundle();
    }


    public static MediaMetadataCompat fromMediaMetadata(Object obj)
    {
        if(obj == null || android.os.Build.VERSION.SDK_INT < 21)
        {
            obj = null;
        } else
        {
            Parcel parcel = Parcel.obtain();
            MediaMetadataCompatApi21.writeToParcel(obj, parcel, 0);
            parcel.setDataPosition(0);
            MediaMetadataCompat mediametadatacompat = (MediaMetadataCompat)CREATOR.createFromParcel(parcel);
            parcel.recycle();
            mediametadatacompat.mMetadataObj = obj;
            obj = mediametadatacompat;
        }
        return ((MediaMetadataCompat) (obj));
    }

    public boolean containsKey(String s)
    {
        return mBundle.containsKey(s);
    }

    public int describeContents()
    {
        return 0;
    }

    public Bitmap getBitmap(String s)
    {
        Object obj = null;
        try
        {
            s = (Bitmap)mBundle.getParcelable(s);
        }
        // Misplaced declaration of an exception variable
        catch(String s)
        {
            Log.w("MediaMetadata", "Failed to retrieve a key as Bitmap.", s);
            s = obj;
        }
        return s;
    }

    public Bundle getBundle()
    {
        return mBundle;
    }

    public MediaDescriptionCompat getDescription()
    {
        if(mDescription == null) goto _L2; else goto _L1
_L1:
        Object obj = mDescription;
_L6:
        return ((MediaDescriptionCompat) (obj));
_L2:
        int i;
        String s = getString("android.media.metadata.MEDIA_ID");
        CharSequence acharsequence[] = new CharSequence[3];
        Object obj1 = null;
        MediaDescriptionCompat.Builder builder = null;
        obj = getText("android.media.metadata.DISPLAY_TITLE");
        if(!TextUtils.isEmpty(((CharSequence) (obj))))
        {
            acharsequence[0] = ((CharSequence) (obj));
            acharsequence[1] = getText("android.media.metadata.DISPLAY_SUBTITLE");
            acharsequence[2] = getText("android.media.metadata.DISPLAY_DESCRIPTION");
        } else
        {
            i = 0;
            int j = 0;
            while(i < acharsequence.length && j < PREFERRED_DESCRIPTION_ORDER.length) 
            {
                CharSequence charsequence = getText(PREFERRED_DESCRIPTION_ORDER[j]);
                int k = i;
                if(!TextUtils.isEmpty(charsequence))
                {
                    acharsequence[i] = charsequence;
                    k = i + 1;
                }
                j++;
                i = k;
            }
        }
        i = 0;
_L7:
        obj = obj1;
        if(i >= PREFERRED_BITMAP_ORDER.length) goto _L4; else goto _L3
_L3:
        obj = getBitmap(PREFERRED_BITMAP_ORDER[i]);
        if(obj == null) goto _L5; else goto _L4
_L4:
        i = 0;
_L8:
        obj1 = builder;
        if(i < PREFERRED_URI_ORDER.length)
        {
            obj1 = getString(PREFERRED_URI_ORDER[i]);
            if(TextUtils.isEmpty(((CharSequence) (obj1))))
                break MISSING_BLOCK_LABEL_287;
            obj1 = Uri.parse(((String) (obj1)));
        }
        builder = new MediaDescriptionCompat.Builder();
        builder.setMediaId(s);
        builder.setTitle(acharsequence[0]);
        builder.setSubtitle(acharsequence[1]);
        builder.setDescription(acharsequence[2]);
        builder.setIconBitmap(((Bitmap) (obj)));
        builder.setIconUri(((Uri) (obj1)));
        mDescription = builder.build();
        obj = mDescription;
          goto _L6
_L5:
        i++;
          goto _L7
        i++;
          goto _L8
    }

    public long getLong(String s)
    {
        return mBundle.getLong(s, 0L);
    }

    public Object getMediaMetadata()
    {
        Object obj;
        if(mMetadataObj != null || android.os.Build.VERSION.SDK_INT < 21)
        {
            obj = mMetadataObj;
        } else
        {
            obj = Parcel.obtain();
            writeToParcel(((Parcel) (obj)), 0);
            ((Parcel) (obj)).setDataPosition(0);
            mMetadataObj = MediaMetadataCompatApi21.createFromParcel(((Parcel) (obj)));
            ((Parcel) (obj)).recycle();
            obj = mMetadataObj;
        }
        return obj;
    }

    public RatingCompat getRating(String s)
    {
        Object obj = null;
        if(android.os.Build.VERSION.SDK_INT < 21) goto _L2; else goto _L1
_L1:
        s = RatingCompat.fromRating(mBundle.getParcelable(s));
_L4:
        return s;
_L2:
        try
        {
            s = (RatingCompat)mBundle.getParcelable(s);
        }
        // Misplaced declaration of an exception variable
        catch(String s)
        {
            Log.w("MediaMetadata", "Failed to retrieve a key as Rating.", s);
            s = obj;
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    public String getString(String s)
    {
        s = mBundle.getCharSequence(s);
        if(s != null)
            s = s.toString();
        else
            s = null;
        return s;
    }

    public CharSequence getText(String s)
    {
        return mBundle.getCharSequence(s);
    }

    public Set keySet()
    {
        return mBundle.keySet();
    }

    public int size()
    {
        return mBundle.size();
    }

    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeBundle(mBundle);
    }

    public static final android.os.Parcelable.Creator CREATOR = new android.os.Parcelable.Creator() {

        public MediaMetadataCompat createFromParcel(Parcel parcel)
        {
            return new MediaMetadataCompat(parcel);
        }

        public volatile Object createFromParcel(Parcel parcel)
        {
            return createFromParcel(parcel);
        }

        public MediaMetadataCompat[] newArray(int i)
        {
            return new MediaMetadataCompat[i];
        }

        public volatile Object[] newArray(int i)
        {
            return newArray(i);
        }

    }
;
    private static final ArrayMap METADATA_KEYS_TYPE;
    public static final String METADATA_KEY_ALBUM = "android.media.metadata.ALBUM";
    public static final String METADATA_KEY_ALBUM_ART = "android.media.metadata.ALBUM_ART";
    public static final String METADATA_KEY_ALBUM_ARTIST = "android.media.metadata.ALBUM_ARTIST";
    public static final String METADATA_KEY_ALBUM_ART_URI = "android.media.metadata.ALBUM_ART_URI";
    public static final String METADATA_KEY_ART = "android.media.metadata.ART";
    public static final String METADATA_KEY_ARTIST = "android.media.metadata.ARTIST";
    public static final String METADATA_KEY_ART_URI = "android.media.metadata.ART_URI";
    public static final String METADATA_KEY_AUTHOR = "android.media.metadata.AUTHOR";
    public static final String METADATA_KEY_COMPILATION = "android.media.metadata.COMPILATION";
    public static final String METADATA_KEY_COMPOSER = "android.media.metadata.COMPOSER";
    public static final String METADATA_KEY_DATE = "android.media.metadata.DATE";
    public static final String METADATA_KEY_DISC_NUMBER = "android.media.metadata.DISC_NUMBER";
    public static final String METADATA_KEY_DISPLAY_DESCRIPTION = "android.media.metadata.DISPLAY_DESCRIPTION";
    public static final String METADATA_KEY_DISPLAY_ICON = "android.media.metadata.DISPLAY_ICON";
    public static final String METADATA_KEY_DISPLAY_ICON_URI = "android.media.metadata.DISPLAY_ICON_URI";
    public static final String METADATA_KEY_DISPLAY_SUBTITLE = "android.media.metadata.DISPLAY_SUBTITLE";
    public static final String METADATA_KEY_DISPLAY_TITLE = "android.media.metadata.DISPLAY_TITLE";
    public static final String METADATA_KEY_DURATION = "android.media.metadata.DURATION";
    public static final String METADATA_KEY_GENRE = "android.media.metadata.GENRE";
    public static final String METADATA_KEY_MEDIA_ID = "android.media.metadata.MEDIA_ID";
    public static final String METADATA_KEY_NUM_TRACKS = "android.media.metadata.NUM_TRACKS";
    public static final String METADATA_KEY_RATING = "android.media.metadata.RATING";
    public static final String METADATA_KEY_TITLE = "android.media.metadata.TITLE";
    public static final String METADATA_KEY_TRACK_NUMBER = "android.media.metadata.TRACK_NUMBER";
    public static final String METADATA_KEY_USER_RATING = "android.media.metadata.USER_RATING";
    public static final String METADATA_KEY_WRITER = "android.media.metadata.WRITER";
    public static final String METADATA_KEY_YEAR = "android.media.metadata.YEAR";
    private static final int METADATA_TYPE_BITMAP = 2;
    private static final int METADATA_TYPE_LONG = 0;
    private static final int METADATA_TYPE_RATING = 3;
    private static final int METADATA_TYPE_TEXT = 1;
    private static final String PREFERRED_BITMAP_ORDER[] = {
        "android.media.metadata.DISPLAY_ICON", "android.media.metadata.ART", "android.media.metadata.ALBUM_ART"
    };
    private static final String PREFERRED_DESCRIPTION_ORDER[] = {
        "android.media.metadata.TITLE", "android.media.metadata.ARTIST", "android.media.metadata.ALBUM", "android.media.metadata.ALBUM_ARTIST", "android.media.metadata.WRITER", "android.media.metadata.AUTHOR", "android.media.metadata.COMPOSER"
    };
    private static final String PREFERRED_URI_ORDER[] = {
        "android.media.metadata.DISPLAY_ICON_URI", "android.media.metadata.ART_URI", "android.media.metadata.ALBUM_ART_URI"
    };
    private static final String TAG = "MediaMetadata";
    private final Bundle mBundle;
    private MediaDescriptionCompat mDescription;
    private Object mMetadataObj;

    static 
    {
        METADATA_KEYS_TYPE = new ArrayMap();
        METADATA_KEYS_TYPE.put("android.media.metadata.TITLE", Integer.valueOf(1));
        METADATA_KEYS_TYPE.put("android.media.metadata.ARTIST", Integer.valueOf(1));
        METADATA_KEYS_TYPE.put("android.media.metadata.DURATION", Integer.valueOf(0));
        METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM", Integer.valueOf(1));
        METADATA_KEYS_TYPE.put("android.media.metadata.AUTHOR", Integer.valueOf(1));
        METADATA_KEYS_TYPE.put("android.media.metadata.WRITER", Integer.valueOf(1));
        METADATA_KEYS_TYPE.put("android.media.metadata.COMPOSER", Integer.valueOf(1));
        METADATA_KEYS_TYPE.put("android.media.metadata.COMPILATION", Integer.valueOf(1));
        METADATA_KEYS_TYPE.put("android.media.metadata.DATE", Integer.valueOf(1));
        METADATA_KEYS_TYPE.put("android.media.metadata.YEAR", Integer.valueOf(0));
        METADATA_KEYS_TYPE.put("android.media.metadata.GENRE", Integer.valueOf(1));
        METADATA_KEYS_TYPE.put("android.media.metadata.TRACK_NUMBER", Integer.valueOf(0));
        METADATA_KEYS_TYPE.put("android.media.metadata.NUM_TRACKS", Integer.valueOf(0));
        METADATA_KEYS_TYPE.put("android.media.metadata.DISC_NUMBER", Integer.valueOf(0));
        METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM_ARTIST", Integer.valueOf(1));
        METADATA_KEYS_TYPE.put("android.media.metadata.ART", Integer.valueOf(2));
        METADATA_KEYS_TYPE.put("android.media.metadata.ART_URI", Integer.valueOf(1));
        METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM_ART", Integer.valueOf(2));
        METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM_ART_URI", Integer.valueOf(1));
        METADATA_KEYS_TYPE.put("android.media.metadata.USER_RATING", Integer.valueOf(3));
        METADATA_KEYS_TYPE.put("android.media.metadata.RATING", Integer.valueOf(3));
        METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_TITLE", Integer.valueOf(1));
        METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_SUBTITLE", Integer.valueOf(1));
        METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_DESCRIPTION", Integer.valueOf(1));
        METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_ICON", Integer.valueOf(2));
        METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_ICON_URI", Integer.valueOf(1));
        METADATA_KEYS_TYPE.put("android.media.metadata.MEDIA_ID", Integer.valueOf(1));
    }


}
