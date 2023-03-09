// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package ketai.net.nfc.record;

import android.nfc.*;
import java.util.Arrays;
import java.util.NoSuchElementException;
import ketai.net.nfc.NdefMessageParser;

// Referenced classes of package ketai.net.nfc.record:
//            ParsedNdefRecord, TextRecord, UriRecord

public class SmartPoster
    implements ParsedNdefRecord
{
    private static final class RecommendedAction extends Enum
    {

        private byte getByte()
        {
            return mAction;
        }

        public static RecommendedAction valueOf(String s)
        {
            return (RecommendedAction)Enum.valueOf(ketai/net/nfc/record/SmartPoster$RecommendedAction, s);
        }

        public static RecommendedAction[] values()
        {
            RecommendedAction arecommendedaction[] = ENUM$VALUES;
            int i = arecommendedaction.length;
            RecommendedAction arecommendedaction1[] = new RecommendedAction[i];
            System.arraycopy(arecommendedaction, 0, arecommendedaction1, 0, i);
            return arecommendedaction1;
        }

        public static final RecommendedAction DO_ACTION;
        private static final RecommendedAction ENUM$VALUES[];
        public static final RecommendedAction OPEN_FOR_EDITING;
        public static final RecommendedAction SAVE_FOR_LATER;
        public static final RecommendedAction UNKNOWN;
        private final byte mAction;

        static 
        {
            UNKNOWN = new RecommendedAction("UNKNOWN", 0, (byte)-1);
            DO_ACTION = new RecommendedAction("DO_ACTION", 1, (byte)0);
            SAVE_FOR_LATER = new RecommendedAction("SAVE_FOR_LATER", 2, (byte)1);
            OPEN_FOR_EDITING = new RecommendedAction("OPEN_FOR_EDITING", 3, (byte)2);
            ENUM$VALUES = (new RecommendedAction[] {
                UNKNOWN, DO_ACTION, SAVE_FOR_LATER, OPEN_FOR_EDITING
            });
        }

        private RecommendedAction(String s, int i, byte byte0)
        {
            super(s, i);
            mAction = byte0;
        }
    }


    private SmartPoster(UriRecord urirecord, TextRecord textrecord, RecommendedAction recommendedaction, String s)
    {
        mUriRecord = urirecord;
        mTitleRecord = textrecord;
    }

    private static NdefRecord getByType(byte abyte0[], NdefRecord andefrecord[])
    {
        int i;
        int j;
        i = andefrecord.length;
        j = 0;
_L6:
        if(j < i) goto _L2; else goto _L1
_L1:
        NdefRecord ndefrecord = null;
_L4:
        return ndefrecord;
_L2:
        NdefRecord ndefrecord1;
        ndefrecord1 = andefrecord[j];
        ndefrecord = ndefrecord1;
        if(Arrays.equals(abyte0, ndefrecord1.getType())) goto _L4; else goto _L3
_L3:
        j++;
        if(true) goto _L6; else goto _L5
_L5:
    }

    private static Object getFirstIfExists(Iterable iterable, Class class1)
    {
        return null;
    }

    public static boolean isPoster(NdefRecord ndefrecord)
    {
        parse(ndefrecord);
        boolean flag = true;
_L2:
        return flag;
        ndefrecord;
        flag = false;
        if(true) goto _L2; else goto _L1
_L1:
    }

    public static SmartPoster parse(NdefRecord ndefrecord)
    {
        try
        {
            NdefMessage ndefmessage = JVM INSTR new #64  <Class NdefMessage>;
            ndefmessage.NdefMessage(ndefrecord.getPayload());
            ndefrecord = parse(ndefmessage.getRecords());
        }
        // Misplaced declaration of an exception variable
        catch(NdefRecord ndefrecord)
        {
            throw new IllegalArgumentException(ndefrecord);
        }
        return ndefrecord;
    }

    public static SmartPoster parse(NdefRecord andefrecord[])
    {
        try
        {
            andefrecord = new SmartPoster(null, (TextRecord)getFirstIfExists(NdefMessageParser.getRecords(andefrecord), ketai/net/nfc/record/TextRecord), parseRecommendedAction(andefrecord), parseType(andefrecord));
        }
        // Misplaced declaration of an exception variable
        catch(NdefRecord andefrecord[])
        {
            throw new IllegalArgumentException(andefrecord);
        }
        return andefrecord;
    }

    private static RecommendedAction parseRecommendedAction(NdefRecord andefrecord[])
    {
        andefrecord = getByType(ACTION_RECORD_TYPE, andefrecord);
        if(andefrecord == null)
        {
            andefrecord = RecommendedAction.UNKNOWN;
        } else
        {
            byte byte0 = andefrecord.getPayload()[0];
            andefrecord = RecommendedAction.UNKNOWN;
        }
        return andefrecord;
    }

    private static String parseType(NdefRecord andefrecord[])
    {
        if(getByType(TYPE_TYPE, andefrecord) != null);
        return null;
    }

    public String getTag()
    {
        return null;
    }

    public TextRecord getTitle()
    {
        return mTitleRecord;
    }

    public UriRecord getUriRecord()
    {
        return mUriRecord;
    }

    private static final byte ACTION_RECORD_TYPE[] = {
        97, 99, 116
    };
    private static final byte TYPE_TYPE[] = {
        116
    };
    private final TextRecord mTitleRecord;
    private final UriRecord mUriRecord;

}
