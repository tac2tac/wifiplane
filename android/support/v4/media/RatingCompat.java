// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.media;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.lang.annotation.Annotation;

// Referenced classes of package android.support.v4.media:
//            RatingCompatApi21

public final class RatingCompat
    implements Parcelable
{
    public static interface StarStyle
        extends Annotation
    {
    }

    public static interface Style
        extends Annotation
    {
    }


    private RatingCompat(int i, float f)
    {
        mRatingStyle = i;
        mRatingValue = f;
    }


    public static RatingCompat fromRating(Object obj)
    {
        Object obj1;
        RatingCompat ratingcompat;
        obj1 = null;
        ratingcompat = obj1;
        if(obj == null) goto _L2; else goto _L1
_L1:
        if(android.os.Build.VERSION.SDK_INT >= 21) goto _L4; else goto _L3
_L3:
        ratingcompat = obj1;
_L2:
        return ratingcompat;
_L4:
        int i = RatingCompatApi21.getRatingStyle(obj);
        if(!RatingCompatApi21.isRated(obj)) goto _L6; else goto _L5
_L5:
        i;
        JVM INSTR tableswitch 1 6: default 72
    //                   1 77
    //                   2 93
    //                   3 104
    //                   4 104
    //                   5 104
    //                   6 116;
           goto _L7 _L8 _L9 _L10 _L10 _L10 _L11
_L7:
        ratingcompat = obj1;
          goto _L2
_L8:
        ratingcompat = newHeartRating(RatingCompatApi21.hasHeart(obj));
_L12:
        ratingcompat.mRatingObj = obj;
          goto _L2
_L9:
        ratingcompat = newThumbRating(RatingCompatApi21.isThumbUp(obj));
          goto _L12
_L10:
        ratingcompat = newStarRating(i, RatingCompatApi21.getStarRating(obj));
          goto _L12
_L11:
        ratingcompat = newPercentageRating(RatingCompatApi21.getPercentRating(obj));
          goto _L12
_L6:
        ratingcompat = newUnratedRating(i);
          goto _L12
    }

    public static RatingCompat newHeartRating(boolean flag)
    {
        float f;
        if(flag)
            f = 1.0F;
        else
            f = 0.0F;
        return new RatingCompat(1, f);
    }

    public static RatingCompat newPercentageRating(float f)
    {
        RatingCompat ratingcompat;
        if(f < 0.0F || f > 100F)
        {
            Log.e("Rating", "Invalid percentage-based rating value");
            ratingcompat = null;
        } else
        {
            ratingcompat = new RatingCompat(6, f);
        }
        return ratingcompat;
    }

    public static RatingCompat newStarRating(int i, float f)
    {
        RatingCompat ratingcompat = null;
        i;
        JVM INSTR tableswitch 3 5: default 28
    //                   3 60
    //                   4 86
    //                   5 92;
           goto _L1 _L2 _L3 _L4
_L1:
        Log.e("Rating", (new StringBuilder()).append("Invalid rating style (").append(i).append(") for a star rating").toString());
_L5:
        return ratingcompat;
_L2:
        float f1 = 3F;
_L6:
        if(f < 0.0F || f > f1)
            Log.e("Rating", "Trying to set out of range star-based rating");
        else
            ratingcompat = new RatingCompat(i, f);
        if(true) goto _L5; else goto _L3
_L3:
        f1 = 4F;
          goto _L6
_L4:
        f1 = 5F;
          goto _L6
    }

    public static RatingCompat newThumbRating(boolean flag)
    {
        float f;
        if(flag)
            f = 1.0F;
        else
            f = 0.0F;
        return new RatingCompat(2, f);
    }

    public static RatingCompat newUnratedRating(int i)
    {
        i;
        JVM INSTR tableswitch 1 6: default 40
    //                   1 44
    //                   2 44
    //                   3 44
    //                   4 44
    //                   5 44
    //                   6 44;
           goto _L1 _L2 _L2 _L2 _L2 _L2 _L2
_L1:
        RatingCompat ratingcompat = null;
_L4:
        return ratingcompat;
_L2:
        ratingcompat = new RatingCompat(i, -1F);
        if(true) goto _L4; else goto _L3
_L3:
    }

    public int describeContents()
    {
        return mRatingStyle;
    }

    public float getPercentRating()
    {
        float f;
        if(mRatingStyle != 6 || !isRated())
            f = -1F;
        else
            f = mRatingValue;
        return f;
    }

    public Object getRating()
    {
        if(mRatingObj == null && android.os.Build.VERSION.SDK_INT >= 21) goto _L2; else goto _L1
_L1:
        Object obj = mRatingObj;
_L10:
        return obj;
_L2:
        if(!isRated()) goto _L4; else goto _L3
_L3:
        mRatingStyle;
        JVM INSTR tableswitch 1 6: default 72
    //                   1 77
    //                   2 96
    //                   3 110
    //                   4 110
    //                   5 110
    //                   6 128;
           goto _L5 _L6 _L7 _L8 _L8 _L8 _L9
_L5:
        obj = null;
          goto _L10
_L6:
        mRatingObj = RatingCompatApi21.newHeartRating(hasHeart());
_L11:
        obj = mRatingObj;
          goto _L10
_L7:
        mRatingObj = RatingCompatApi21.newThumbRating(isThumbUp());
          goto _L11
_L8:
        mRatingObj = RatingCompatApi21.newStarRating(mRatingStyle, getStarRating());
          goto _L11
_L9:
        mRatingObj = RatingCompatApi21.newPercentageRating(getPercentRating());
          goto _L5
_L4:
        mRatingObj = RatingCompatApi21.newUnratedRating(mRatingStyle);
          goto _L11
    }

    public int getRatingStyle()
    {
        return mRatingStyle;
    }

    public float getStarRating()
    {
        mRatingStyle;
        JVM INSTR tableswitch 3 5: default 32
    //                   3 37
    //                   4 37
    //                   5 37;
           goto _L1 _L2 _L2 _L2
_L1:
        float f = -1F;
_L4:
        return f;
_L2:
        if(!isRated())
            continue; /* Loop/switch isn't completed */
        f = mRatingValue;
        if(true) goto _L4; else goto _L3
_L3:
        if(true) goto _L1; else goto _L5
_L5:
    }

    public boolean hasHeart()
    {
        boolean flag;
        boolean flag1;
        flag = true;
        flag1 = false;
        if(mRatingStyle == 1) goto _L2; else goto _L1
_L1:
        flag = flag1;
_L4:
        return flag;
_L2:
        if(mRatingValue != 1.0F)
            flag = false;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public boolean isRated()
    {
        boolean flag;
        if(mRatingValue >= 0.0F)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public boolean isThumbUp()
    {
        boolean flag;
        flag = false;
        break MISSING_BLOCK_LABEL_2;
        if(mRatingStyle == 2 && mRatingValue == 1.0F)
            flag = true;
        return flag;
    }

    public String toString()
    {
        StringBuilder stringbuilder = (new StringBuilder()).append("Rating:style=").append(mRatingStyle).append(" rating=");
        String s;
        if(mRatingValue < 0.0F)
            s = "unrated";
        else
            s = String.valueOf(mRatingValue);
        return stringbuilder.append(s).toString();
    }

    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeInt(mRatingStyle);
        parcel.writeFloat(mRatingValue);
    }

    public static final android.os.Parcelable.Creator CREATOR = new android.os.Parcelable.Creator() {

        public RatingCompat createFromParcel(Parcel parcel)
        {
            return new RatingCompat(parcel.readInt(), parcel.readFloat());
        }

        public volatile Object createFromParcel(Parcel parcel)
        {
            return createFromParcel(parcel);
        }

        public RatingCompat[] newArray(int i)
        {
            return new RatingCompat[i];
        }

        public volatile Object[] newArray(int i)
        {
            return newArray(i);
        }

    }
;
    public static final int RATING_3_STARS = 3;
    public static final int RATING_4_STARS = 4;
    public static final int RATING_5_STARS = 5;
    public static final int RATING_HEART = 1;
    public static final int RATING_NONE = 0;
    private static final float RATING_NOT_RATED = -1F;
    public static final int RATING_PERCENTAGE = 6;
    public static final int RATING_THUMB_UP_DOWN = 2;
    private static final String TAG = "Rating";
    private Object mRatingObj;
    private final int mRatingStyle;
    private final float mRatingValue;

}
