// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package ketai.net.nfc.record;

import android.nfc.NdefRecord;
import java.io.UnsupportedEncodingException;
import processing.core.PApplet;

// Referenced classes of package ketai.net.nfc.record:
//            ParsedNdefRecord

public class TextRecord
    implements ParsedNdefRecord
{

    private TextRecord(String s, String s1)
    {
        mLanguageCode = s;
        mText = s1;
    }

    public static boolean isText(NdefRecord ndefrecord)
    {
        parse(ndefrecord);
        PApplet.println("TextRecord.isText is true!");
        boolean flag = true;
_L2:
        return flag;
        ndefrecord;
        flag = false;
        if(true) goto _L2; else goto _L1
_L1:
    }

    public static TextRecord parse(NdefRecord ndefrecord)
    {
        byte abyte0[];
        int i;
        String s;
        String s1;
        try
        {
            abyte0 = ndefrecord.getPayload();
            ndefrecord = JVM INSTR new #47  <Class StringBuilder>;
            ndefrecord.StringBuilder("TextRecord parsed and NdefRecord with a payload of ");
            PApplet.println(ndefrecord.append(abyte0.length).append(" bytes.").toString());
        }
        // Misplaced declaration of an exception variable
        catch(NdefRecord ndefrecord)
        {
            throw new IllegalArgumentException(ndefrecord);
        }
        // Misplaced declaration of an exception variable
        catch(NdefRecord ndefrecord)
        {
            throw new IllegalArgumentException((new StringBuilder("Error parsing as a TextRecord: ")).append(ndefrecord.getMessage()).toString());
        }
        if((abyte0[0] & 0x80) == 0)
            ndefrecord = "UTF-8";
        else
            ndefrecord = "UTF-16";
        i = abyte0[0] & 0x3f;
        s = JVM INSTR new #68  <Class String>;
        s.String(abyte0, 1, i, "US-ASCII");
        s1 = JVM INSTR new #68  <Class String>;
        s1.String(abyte0, i + 1, abyte0.length - i - 1, ndefrecord);
        ndefrecord = JVM INSTR new #47  <Class StringBuilder>;
        ndefrecord.StringBuilder("TextRecord parsing: ");
        PApplet.println(ndefrecord.append(abyte0).toString());
        ndefrecord = JVM INSTR new #47  <Class StringBuilder>;
        ndefrecord.StringBuilder("\t parsed text:");
        PApplet.println(ndefrecord.append(s1).toString());
        return new TextRecord(s, s1);
    }

    public String getLanguageCode()
    {
        return mLanguageCode;
    }

    public String getTag()
    {
        return getText();
    }

    public String getText()
    {
        return mText;
    }

    private final String mLanguageCode;
    private final String mText;
}
