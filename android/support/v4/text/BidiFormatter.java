// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.text;

import java.util.Locale;

// Referenced classes of package android.support.v4.text:
//            TextDirectionHeuristicsCompat, TextUtilsCompat, TextDirectionHeuristicCompat

public final class BidiFormatter
{
    public static final class Builder
    {

        private static BidiFormatter getDefaultInstanceFromContext(boolean flag)
        {
            BidiFormatter bidiformatter;
            if(flag)
                bidiformatter = BidiFormatter.DEFAULT_RTL_INSTANCE;
            else
                bidiformatter = BidiFormatter.DEFAULT_LTR_INSTANCE;
            return bidiformatter;
        }

        private void initialize(boolean flag)
        {
            mIsRtlContext = flag;
            mTextDirectionHeuristicCompat = BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC;
            mFlags = 2;
        }

        public BidiFormatter build()
        {
            BidiFormatter bidiformatter;
            if(mFlags == 2 && mTextDirectionHeuristicCompat == BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC)
                bidiformatter = getDefaultInstanceFromContext(mIsRtlContext);
            else
                bidiformatter = new BidiFormatter(mIsRtlContext, mFlags, mTextDirectionHeuristicCompat);
            return bidiformatter;
        }

        public Builder setTextDirectionHeuristic(TextDirectionHeuristicCompat textdirectionheuristiccompat)
        {
            mTextDirectionHeuristicCompat = textdirectionheuristiccompat;
            return this;
        }

        public Builder stereoReset(boolean flag)
        {
            if(flag)
                mFlags = mFlags | 2;
            else
                mFlags = mFlags & -3;
            return this;
        }

        private int mFlags;
        private boolean mIsRtlContext;
        private TextDirectionHeuristicCompat mTextDirectionHeuristicCompat;

        public Builder()
        {
            initialize(BidiFormatter.isRtlLocale(Locale.getDefault()));
        }

        public Builder(Locale locale)
        {
            initialize(BidiFormatter.isRtlLocale(locale));
        }

        public Builder(boolean flag)
        {
            initialize(flag);
        }
    }

    private static class DirectionalityEstimator
    {

        private static byte getCachedDirectionality(char c)
        {
            byte byte2;
            if(c < '\u0700')
            {
                byte byte0 = DIR_TYPE_CACHE[c];
                byte2 = byte0;
            } else
            {
                byte byte1 = Character.getDirectionality(c);
                byte2 = byte1;
            }
            return byte2;
        }

        private byte skipEntityBackward()
        {
            int i = charIndex;
_L2:
            byte byte1;
            if(charIndex <= 0)
                break; /* Loop/switch isn't completed */
            String s = text;
            int j = charIndex - 1;
            charIndex = j;
            lastChar = s.charAt(j);
            if(lastChar != '&')
                continue; /* Loop/switch isn't completed */
            j = 12;
            byte1 = j;
_L3:
            return byte1;
            if(lastChar != ';') goto _L2; else goto _L1
_L1:
            charIndex = i;
            lastChar = (char)59;
            byte byte0 = 13;
            byte1 = byte0;
              goto _L3
            if(true) goto _L2; else goto _L4
_L4:
        }

        private byte skipEntityForward()
        {
            int i;
            do
            {
                if(charIndex >= length)
                    break;
                String s = text;
                i = charIndex;
                charIndex = i + 1;
                i = s.charAt(i);
                lastChar = (char)i;
            } while(i != 59);
            return 12;
        }

        private byte skipTagBackward()
        {
            int i = charIndex;
_L8:
            if(charIndex <= 0) goto _L2; else goto _L1
_L1:
            String s = text;
            int j = charIndex - 1;
            charIndex = j;
            lastChar = s.charAt(j);
            if(lastChar != '<') goto _L4; else goto _L3
_L3:
            byte byte0;
            i = 12;
            byte0 = i;
_L6:
            return byte0;
_L4:
            if(lastChar != '>')
                break; /* Loop/switch isn't completed */
_L2:
            charIndex = i;
            lastChar = (char)62;
            i = 13;
            byte0 = i;
            if(true) goto _L6; else goto _L5
_L5:
            if(lastChar != '"' && lastChar != '\'')
                break;
            char c = lastChar;
            int k;
            do
            {
                if(charIndex <= 0)
                    break;
                String s1 = text;
                k = charIndex - 1;
                charIndex = k;
                k = s1.charAt(k);
                lastChar = (char)k;
            } while(k != c);
            if(true) goto _L8; else goto _L7
_L7:
        }

        private byte skipTagForward()
        {
            int i = charIndex;
            do
            {
                do
                {
                    String s1;
                    char c;
                    byte byte0;
                    int k;
                    if(charIndex < length)
                    {
                        String s = text;
                        int j = charIndex;
                        charIndex = j + 1;
                        lastChar = s.charAt(j);
                        if(lastChar != '>')
                            continue;
                        i = 12;
                        byte0 = i;
                    } else
                    {
                        charIndex = i;
                        lastChar = (char)60;
                        i = 13;
                        byte0 = i;
                    }
                    return byte0;
                } while(lastChar != '"' && lastChar != '\'');
                c = lastChar;
                do
                {
                    if(charIndex >= length)
                        break;
                    s1 = text;
                    k = charIndex;
                    charIndex = k + 1;
                    k = s1.charAt(k);
                    lastChar = (char)k;
                } while(k != c);
            } while(true);
        }

        byte dirTypeBackward()
        {
            lastChar = text.charAt(charIndex - 1);
            if(!Character.isLowSurrogate(lastChar)) goto _L2; else goto _L1
_L1:
            byte byte2;
            int i = Character.codePointBefore(text, charIndex);
            charIndex = charIndex - Character.charCount(i);
            i = Character.getDirectionality(i);
            byte2 = i;
_L4:
            return byte2;
_L2:
            charIndex = charIndex - 1;
            byte byte0 = getCachedDirectionality(lastChar);
            byte2 = byte0;
            if(isHtml)
                if(lastChar == '>')
                {
                    byte0 = skipTagBackward();
                    byte2 = byte0;
                } else
                {
                    byte2 = byte0;
                    if(lastChar == ';')
                    {
                        byte byte1 = skipEntityBackward();
                        byte2 = byte1;
                    }
                }
            if(true) goto _L4; else goto _L3
_L3:
        }

        byte dirTypeForward()
        {
            lastChar = text.charAt(charIndex);
            if(!Character.isHighSurrogate(lastChar)) goto _L2; else goto _L1
_L1:
            byte byte2;
            int i = Character.codePointAt(text, charIndex);
            charIndex = charIndex + Character.charCount(i);
            i = Character.getDirectionality(i);
            byte2 = i;
_L4:
            return byte2;
_L2:
            charIndex = charIndex + 1;
            byte byte0 = getCachedDirectionality(lastChar);
            byte2 = byte0;
            if(isHtml)
                if(lastChar == '<')
                {
                    byte0 = skipTagForward();
                    byte2 = byte0;
                } else
                {
                    byte2 = byte0;
                    if(lastChar == '&')
                    {
                        byte byte1 = skipEntityForward();
                        byte2 = byte1;
                    }
                }
            if(true) goto _L4; else goto _L3
_L3:
        }

        int getEntryDir()
        {
            int i;
            byte byte0;
            int j;
            charIndex = 0;
            i = 0;
            byte0 = 0;
            j = 0;
_L6:
            if(charIndex >= length || j != 0) goto _L2; else goto _L1
_L1:
            dirTypeForward();
            JVM INSTR tableswitch 0 18: default 120
        //                       0 149
        //                       1 164
        //                       2 164
        //                       3 120
        //                       4 120
        //                       5 120
        //                       6 120
        //                       7 120
        //                       8 120
        //                       9 11
        //                       10 120
        //                       11 120
        //                       12 120
        //                       13 120
        //                       14 125
        //                       15 125
        //                       16 133
        //                       17 133
        //                       18 141;
               goto _L3 _L4 _L5 _L5 _L3 _L3 _L3 _L3 _L3 _L3 _L6 _L3 _L3 _L3 _L3 _L7 _L7 _L8 _L8 _L9
_L3:
            j = i;
              goto _L6
_L7:
            i++;
            byte0 = -1;
              goto _L6
_L8:
            i++;
            byte0 = 1;
              goto _L6
_L9:
            i--;
            byte0 = 0;
              goto _L6
_L4:
            if(i != 0) goto _L11; else goto _L10
_L10:
            byte byte1 = -1;
_L14:
            return byte1;
_L11:
            j = i;
              goto _L6
_L5:
            if(i != 0) goto _L13; else goto _L12
_L12:
            byte1 = 1;
              goto _L14
_L13:
            j = i;
              goto _L6
_L2:
            if(j != 0) goto _L16; else goto _L15
_L15:
            byte1 = 0;
              goto _L14
_L16:
            byte1 = byte0;
            if(byte0 != 0) goto _L14; else goto _L17
_L17:
            if(charIndex <= 0) goto _L19; else goto _L18
_L18:
            dirTypeBackward();
            JVM INSTR tableswitch 14 18: default 240
        //                       14 243
        //                       15 243
        //                       16 260
        //                       17 260
        //                       18 277;
               goto _L17 _L20 _L20 _L21 _L21 _L22
_L20:
            if(j != i) goto _L24; else goto _L23
_L23:
            byte1 = -1;
              goto _L14
_L24:
            i--;
              goto _L17
_L21:
            if(j != i) goto _L26; else goto _L25
_L25:
            byte1 = 1;
              goto _L14
_L26:
            i--;
              goto _L17
_L22:
            i++;
              goto _L17
_L19:
            byte1 = 0;
              goto _L14
        }

        int getExitDir()
        {
            byte byte0;
            int i;
            int j;
            byte0 = -1;
            charIndex = length;
            i = 0;
            j = 0;
_L4:
            if(charIndex <= 0)
                break MISSING_BLOCK_LABEL_200;
            dirTypeBackward();
            JVM INSTR tableswitch 0 18: default 116
        //                       0 125
        //                       1 158
        //                       2 158
        //                       3 116
        //                       4 116
        //                       5 116
        //                       6 116
        //                       7 116
        //                       8 116
        //                       9 14
        //                       10 116
        //                       11 116
        //                       12 116
        //                       13 116
        //                       14 144
        //                       15 144
        //                       16 177
        //                       17 177
        //                       18 194;
               goto _L1 _L2 _L3 _L3 _L1 _L1 _L1 _L1 _L1 _L1 _L4 _L1 _L1 _L1 _L1 _L5 _L5 _L6 _L6 _L7
_L1:
            if(j == 0)
                j = i;
              goto _L4
_L2:
            if(i != 0) goto _L9; else goto _L8
_L8:
            byte byte1 = byte0;
_L11:
            return byte1;
_L9:
            if(j == 0)
                j = i;
              goto _L4
_L5:
            byte1 = byte0;
            if(j == i) goto _L11; else goto _L10
_L10:
            i--;
              goto _L4
_L3:
label0:
            {
                if(i != 0)
                    break label0;
                byte1 = 1;
            }
              goto _L11
            if(j == 0)
                j = i;
              goto _L4
_L6:
label1:
            {
                if(j != i)
                    break label1;
                byte1 = 1;
            }
              goto _L11
            i--;
              goto _L4
_L7:
            i++;
              goto _L4
            byte1 = 0;
              goto _L11
        }

        private static final byte DIR_TYPE_CACHE[];
        private static final int DIR_TYPE_CACHE_SIZE = 1792;
        private int charIndex;
        private final boolean isHtml;
        private char lastChar;
        private final int length;
        private final String text;

        static 
        {
            DIR_TYPE_CACHE = new byte[1792];
            for(int i = 0; i < 1792; i++)
                DIR_TYPE_CACHE[i] = Character.getDirectionality(i);

        }

        DirectionalityEstimator(String s, boolean flag)
        {
            text = s;
            isHtml = flag;
            length = s.length();
        }
    }


    private BidiFormatter(boolean flag, int i, TextDirectionHeuristicCompat textdirectionheuristiccompat)
    {
        mIsRtlContext = flag;
        mFlags = i;
        mDefaultTextDirectionHeuristicCompat = textdirectionheuristiccompat;
    }


    private static int getEntryDir(String s)
    {
        return (new DirectionalityEstimator(s, false)).getEntryDir();
    }

    private static int getExitDir(String s)
    {
        return (new DirectionalityEstimator(s, false)).getExitDir();
    }

    public static BidiFormatter getInstance()
    {
        return (new Builder()).build();
    }

    public static BidiFormatter getInstance(Locale locale)
    {
        return (new Builder(locale)).build();
    }

    public static BidiFormatter getInstance(boolean flag)
    {
        return (new Builder(flag)).build();
    }

    private static boolean isRtlLocale(Locale locale)
    {
        boolean flag = true;
        if(TextUtilsCompat.getLayoutDirectionFromLocale(locale) != 1)
            flag = false;
        return flag;
    }

    private String markAfter(String s, TextDirectionHeuristicCompat textdirectionheuristiccompat)
    {
        boolean flag = textdirectionheuristiccompat.isRtl(s, 0, s.length());
        if(!mIsRtlContext && (flag || getExitDir(s) == 1))
            s = LRM_STRING;
        else
        if(mIsRtlContext && (!flag || getExitDir(s) == -1))
            s = RLM_STRING;
        else
            s = "";
        return s;
    }

    private String markBefore(String s, TextDirectionHeuristicCompat textdirectionheuristiccompat)
    {
        boolean flag = textdirectionheuristiccompat.isRtl(s, 0, s.length());
        if(!mIsRtlContext && (flag || getEntryDir(s) == 1))
            s = LRM_STRING;
        else
        if(mIsRtlContext && (!flag || getEntryDir(s) == -1))
            s = RLM_STRING;
        else
            s = "";
        return s;
    }

    public boolean getStereoReset()
    {
        boolean flag;
        if((mFlags & 2) != 0)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public boolean isRtl(String s)
    {
        return mDefaultTextDirectionHeuristicCompat.isRtl(s, 0, s.length());
    }

    public boolean isRtlContext()
    {
        return mIsRtlContext;
    }

    public String unicodeWrap(String s)
    {
        return unicodeWrap(s, mDefaultTextDirectionHeuristicCompat, true);
    }

    public String unicodeWrap(String s, TextDirectionHeuristicCompat textdirectionheuristiccompat)
    {
        return unicodeWrap(s, textdirectionheuristiccompat, true);
    }

    public String unicodeWrap(String s, TextDirectionHeuristicCompat textdirectionheuristiccompat, boolean flag)
    {
        if(s == null)
        {
            s = null;
        } else
        {
            boolean flag1 = textdirectionheuristiccompat.isRtl(s, 0, s.length());
            StringBuilder stringbuilder = new StringBuilder();
            if(getStereoReset() && flag)
            {
                char c;
                if(flag1)
                    textdirectionheuristiccompat = TextDirectionHeuristicsCompat.RTL;
                else
                    textdirectionheuristiccompat = TextDirectionHeuristicsCompat.LTR;
                stringbuilder.append(markBefore(s, textdirectionheuristiccompat));
            }
            if(flag1 != mIsRtlContext)
            {
                char c2;
                if(flag1)
                {
                    c = '\u202B';
                    c2 = c;
                } else
                {
                    char c1 = '\u202A';
                    c2 = c1;
                }
                stringbuilder.append(c2);
                stringbuilder.append(s);
                stringbuilder.append('\u202C');
            } else
            {
                stringbuilder.append(s);
            }
            if(flag)
            {
                if(flag1)
                    textdirectionheuristiccompat = TextDirectionHeuristicsCompat.RTL;
                else
                    textdirectionheuristiccompat = TextDirectionHeuristicsCompat.LTR;
                stringbuilder.append(markAfter(s, textdirectionheuristiccompat));
            }
            s = stringbuilder.toString();
        }
        return s;
    }

    public String unicodeWrap(String s, boolean flag)
    {
        return unicodeWrap(s, mDefaultTextDirectionHeuristicCompat, flag);
    }

    private static final int DEFAULT_FLAGS = 2;
    private static final BidiFormatter DEFAULT_LTR_INSTANCE;
    private static final BidiFormatter DEFAULT_RTL_INSTANCE;
    private static TextDirectionHeuristicCompat DEFAULT_TEXT_DIRECTION_HEURISTIC;
    private static final int DIR_LTR = -1;
    private static final int DIR_RTL = 1;
    private static final int DIR_UNKNOWN = 0;
    private static final String EMPTY_STRING = "";
    private static final int FLAG_STEREO_RESET = 2;
    private static final char LRE = 8234;
    private static final char LRM = 8206;
    private static final String LRM_STRING = Character.toString('\u200E');
    private static final char PDF = 8236;
    private static final char RLE = 8235;
    private static final char RLM = 8207;
    private static final String RLM_STRING = Character.toString('\u200F');
    private final TextDirectionHeuristicCompat mDefaultTextDirectionHeuristicCompat;
    private final int mFlags;
    private final boolean mIsRtlContext;

    static 
    {
        DEFAULT_TEXT_DIRECTION_HEURISTIC = TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR;
        DEFAULT_LTR_INSTANCE = new BidiFormatter(false, 2, DEFAULT_TEXT_DIRECTION_HEURISTIC);
        DEFAULT_RTL_INSTANCE = new BidiFormatter(true, 2, DEFAULT_TEXT_DIRECTION_HEURISTIC);
    }




}
