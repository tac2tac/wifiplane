// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package ketai.net.nfc;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import java.util.ArrayList;
import java.util.List;
import ketai.net.nfc.record.SmartPoster;
import ketai.net.nfc.record.TextRecord;
import ketai.net.nfc.record.UriRecord;
import processing.core.PApplet;

public class NdefMessageParser
{

    private NdefMessageParser()
    {
    }

    public static List getRecords(NdefRecord andefrecord[])
    {
        ArrayList arraylist;
        int i;
        int j;
        arraylist = new ArrayList();
        i = andefrecord.length;
        j = 0;
_L2:
        NdefRecord ndefrecord;
        if(j >= i)
            return arraylist;
        ndefrecord = andefrecord[j];
        if(!TextRecord.isText(ndefrecord))
            break; /* Loop/switch isn't completed */
        PApplet.println("NdefMessageParser.getRecords says this record is a text");
        arraylist.add(TextRecord.parse(ndefrecord));
_L3:
        j++;
        if(true) goto _L2; else goto _L1
_L1:
        if(SmartPoster.isPoster(ndefrecord))
        {
            PApplet.println("NdefMessageParser.getRecords says this record is a smart poster");
            arraylist.add(SmartPoster.parse(ndefrecord));
        } else
        if(UriRecord.isUri(ndefrecord))
        {
            PApplet.println("NdefMessageParser.getRecords says this record is a URI");
            arraylist.add(UriRecord.parse(ndefrecord));
        }
          goto _L3
        if(true) goto _L2; else goto _L4
_L4:
    }

    public static List parse(NdefMessage ndefmessage)
    {
        return getRecords(ndefmessage.getRecords());
    }
}
