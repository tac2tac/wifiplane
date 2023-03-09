// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.text;

import java.util.Locale;

// Referenced classes of package android.support.v4.text:
//            ICUCompat, TextUtilsCompatJellybeanMr1

public final class TextUtilsCompat
{
    private static class TextUtilsCompatImpl
    {

        private static int getLayoutDirectionFromFirstChar(Locale locale)
        {
            int i = 0;
            Character.getDirectionality(locale.getDisplayName(locale).charAt(0));
            JVM INSTR tableswitch 1 2: default 36
        //                       1 38
        //                       2 38;
               goto _L1 _L2 _L2
_L1:
            return i;
_L2:
            i = 1;
            if(true) goto _L1; else goto _L3
_L3:
        }

        public int getLayoutDirectionFromLocale(Locale locale)
        {
            if(locale == null || locale.equals(TextUtilsCompat.ROOT)) goto _L2; else goto _L1
_L1:
            String s = ICUCompat.maximizeAndGetScript(locale);
            if(s != null) goto _L4; else goto _L3
_L3:
            int i = getLayoutDirectionFromFirstChar(locale);
_L6:
            return i;
_L4:
            if(s.equalsIgnoreCase(TextUtilsCompat.ARAB_SCRIPT_SUBTAG) || s.equalsIgnoreCase(TextUtilsCompat.HEBR_SCRIPT_SUBTAG))
            {
                i = 1;
                continue; /* Loop/switch isn't completed */
            }
_L2:
            i = 0;
            if(true) goto _L6; else goto _L5
_L5:
        }

        public String htmlEncode(String s)
        {
            StringBuilder stringbuilder;
            int i;
            stringbuilder = new StringBuilder();
            i = 0;
_L8:
            char c;
            if(i >= s.length())
                break MISSING_BLOCK_LABEL_139;
            c = s.charAt(i);
            c;
            JVM INSTR lookupswitch 5: default 76
        //                       34: 129
        //                       38: 109
        //                       39: 119
        //                       60: 89
        //                       62: 99;
               goto _L1 _L2 _L3 _L4 _L5 _L6
_L2:
            break MISSING_BLOCK_LABEL_129;
_L5:
            break; /* Loop/switch isn't completed */
_L1:
            stringbuilder.append(c);
_L9:
            i++;
            if(true) goto _L8; else goto _L7
_L7:
            stringbuilder.append("&lt;");
              goto _L9
_L6:
            stringbuilder.append("&gt;");
              goto _L9
_L3:
            stringbuilder.append("&amp;");
              goto _L9
_L4:
            stringbuilder.append("&#39;");
              goto _L9
            stringbuilder.append("&quot;");
              goto _L9
            return stringbuilder.toString();
        }

        private TextUtilsCompatImpl()
        {
        }

    }

    private static class TextUtilsCompatJellybeanMr1Impl extends TextUtilsCompatImpl
    {

        public int getLayoutDirectionFromLocale(Locale locale)
        {
            return TextUtilsCompatJellybeanMr1.getLayoutDirectionFromLocale(locale);
        }

        public String htmlEncode(String s)
        {
            return TextUtilsCompatJellybeanMr1.htmlEncode(s);
        }

        private TextUtilsCompatJellybeanMr1Impl()
        {
        }

    }


    private TextUtilsCompat()
    {
    }

    public static int getLayoutDirectionFromLocale(Locale locale)
    {
        return IMPL.getLayoutDirectionFromLocale(locale);
    }

    public static String htmlEncode(String s)
    {
        return IMPL.htmlEncode(s);
    }

    private static String ARAB_SCRIPT_SUBTAG = "Arab";
    private static String HEBR_SCRIPT_SUBTAG = "Hebr";
    private static final TextUtilsCompatImpl IMPL;
    public static final Locale ROOT = new Locale("", "");

    static 
    {
        if(android.os.Build.VERSION.SDK_INT >= 17)
            IMPL = new TextUtilsCompatJellybeanMr1Impl();
        else
            IMPL = new TextUtilsCompatImpl();
    }


}
