// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.widget;

import android.util.Log;
import android.widget.TextView;
import java.lang.reflect.Field;

class TextViewCompatDonut
{

    TextViewCompatDonut()
    {
    }

    static int getMaxLines(TextView textview)
    {
        if(!sMaxModeFieldFetched)
        {
            sMaxModeField = retrieveField("mMaxMode");
            sMaxModeFieldFetched = true;
        }
        if(sMaxModeField == null || retrieveIntFromField(sMaxModeField, textview) != 1) goto _L2; else goto _L1
_L1:
        if(!sMaximumFieldFetched)
        {
            sMaximumField = retrieveField("mMaximum");
            sMaximumFieldFetched = true;
        }
        if(sMaximumField == null) goto _L2; else goto _L3
_L3:
        int i = retrieveIntFromField(sMaximumField, textview);
_L5:
        return i;
_L2:
        i = -1;
        if(true) goto _L5; else goto _L4
_L4:
    }

    static int getMinLines(TextView textview)
    {
        if(!sMinModeFieldFetched)
        {
            sMinModeField = retrieveField("mMinMode");
            sMinModeFieldFetched = true;
        }
        if(sMinModeField == null || retrieveIntFromField(sMinModeField, textview) != 1) goto _L2; else goto _L1
_L1:
        if(!sMinimumFieldFetched)
        {
            sMinimumField = retrieveField("mMinimum");
            sMinimumFieldFetched = true;
        }
        if(sMinimumField == null) goto _L2; else goto _L3
_L3:
        int i = retrieveIntFromField(sMinimumField, textview);
_L5:
        return i;
_L2:
        i = -1;
        if(true) goto _L5; else goto _L4
_L4:
    }

    private static Field retrieveField(String s)
    {
        Field field = null;
        Field field1 = android/widget/TextView.getDeclaredField(s);
        field = field1;
        field1.setAccessible(true);
        field = field1;
_L2:
        return field;
        NoSuchFieldException nosuchfieldexception;
        nosuchfieldexception;
        Log.e("TextViewCompatDonut", (new StringBuilder()).append("Could not retrieve ").append(s).append(" field.").toString());
        if(true) goto _L2; else goto _L1
_L1:
    }

    private static int retrieveIntFromField(Field field, TextView textview)
    {
        int i;
        try
        {
            i = field.getInt(textview);
        }
        // Misplaced declaration of an exception variable
        catch(TextView textview)
        {
            Log.d("TextViewCompatDonut", (new StringBuilder()).append("Could not retrieve value of ").append(field.getName()).append(" field.").toString());
            i = -1;
        }
        return i;
    }

    static void setTextAppearance(TextView textview, int i)
    {
        textview.setTextAppearance(textview.getContext(), i);
    }

    private static final int LINES = 1;
    private static final String LOG_TAG = "TextViewCompatDonut";
    private static Field sMaxModeField;
    private static boolean sMaxModeFieldFetched;
    private static Field sMaximumField;
    private static boolean sMaximumFieldFetched;
    private static Field sMinModeField;
    private static boolean sMinModeFieldFetched;
    private static Field sMinimumField;
    private static boolean sMinimumFieldFetched;
}
