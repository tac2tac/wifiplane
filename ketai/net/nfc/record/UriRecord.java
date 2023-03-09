// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package ketai.net.nfc.record;

import android.net.Uri;
import android.nfc.NdefRecord;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;

// Referenced classes of package ketai.net.nfc.record:
//            ParsedNdefRecord

public class UriRecord
    implements ParsedNdefRecord
{

    private UriRecord(Uri uri)
    {
        if(uri != null)
            mUri = uri;
        else
            mUri = Uri.EMPTY;
    }

    public static boolean isUri(NdefRecord ndefrecord)
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

    public static UriRecord parse(NdefRecord ndefrecord)
    {
        short word0 = ndefrecord.getTnf();
        if(word0 == 1)
            ndefrecord = parseWellKnown(ndefrecord);
        else
        if(word0 == 3)
            ndefrecord = parseAbsolute(ndefrecord);
        else
            throw new IllegalArgumentException((new StringBuilder("Unknown TNF ")).append(word0).toString());
        return ndefrecord;
    }

    private static UriRecord parseAbsolute(NdefRecord ndefrecord)
    {
        return new UriRecord(Uri.parse(new String(ndefrecord.getPayload(), Charset.forName("UTF-8"))));
    }

    private static UriRecord parseWellKnown(NdefRecord ndefrecord)
    {
        if(Arrays.equals(ndefrecord.getType(), NdefRecord.RTD_URI)) goto _L2; else goto _L1
_L1:
        ndefrecord = new UriRecord(Uri.EMPTY);
_L4:
        return ndefrecord;
_L2:
        byte abyte0[] = ndefrecord.getPayload();
        ndefrecord = (String)URI_PREFIX_MAP.get(Byte.valueOf(abyte0[0]));
        byte abyte1[] = Arrays.copyOf(ndefrecord.getBytes(), ndefrecord.getBytes(Charset.forName("UTF-8")).length + abyte0.length);
        int i = 0;
        int j = ndefrecord.getBytes(Charset.forName("UTF-8")).length;
        do
        {
label0:
            {
                if(j < ndefrecord.getBytes(Charset.forName("UTF-8")).length + abyte0.length && i < abyte0.length)
                    break label0;
                ndefrecord = new UriRecord(Uri.parse(new String(abyte1, Charset.forName("UTF-8"))));
            }
            if(true)
                continue;
            abyte1[j] = abyte0[i];
            i++;
            j++;
        } while(true);
        if(true) goto _L4; else goto _L3
_L3:
    }

    public String getTag()
    {
        return mUri.toString();
    }

    public Uri getUri()
    {
        return mUri;
    }

    public static final String RECORD_TYPE = "UriRecord";
    private static final HashMap URI_PREFIX_MAP = new HashMap() {

            
            {
                put(new Byte((byte)0), "");
                put(new Byte((byte)1), "http://www.");
                put(new Byte((byte)2), "https://www.");
                put(new Byte((byte)3), "http://");
                put(new Byte((byte)4), "https://");
                put(new Byte((byte)5), "tel:");
                put(new Byte((byte)6), "mailto:");
                put(new Byte((byte)7), "ftp://anonymous:anonymous@");
                put(new Byte((byte)8), "ftp://ftp.");
                put(new Byte((byte)9), "ftps://");
                put(new Byte((byte)10), "sftp://");
                put(new Byte((byte)11), "smb://");
                put(new Byte((byte)12), "nfs://");
                put(new Byte((byte)13), "ftp://");
                put(new Byte((byte)14), "dav://");
                put(new Byte((byte)15), "news:");
                put(new Byte((byte)16), "telnet://");
                put(new Byte((byte)17), "imap:");
                put(new Byte((byte)18), "rtsp://");
                put(new Byte((byte)19), "urn:");
                put(new Byte((byte)20), "pop:");
                put(new Byte((byte)21), "sip:");
                put(new Byte((byte)22), "sips:");
                put(new Byte((byte)23), "tftp:");
                put(new Byte((byte)24), "btspp://");
                put(new Byte((byte)25), "btl2cap://");
                put(new Byte((byte)26), "btgoep://");
                put(new Byte((byte)27), "tcpobex://");
                put(new Byte((byte)28), "irdaobex://");
                put(new Byte((byte)29), "file://");
                put(new Byte((byte)30), "urn:epc:id:");
                put(new Byte((byte)31), "urn:epc:tag:");
                put(new Byte((byte)32), "urn:epc:pat:");
                put(new Byte((byte)33), "urn:epc:raw:");
                put(new Byte((byte)34), "urn:epc:");
                put(new Byte((byte)35), "urn:nfc:");
            }
    }
;
    private final Uri mUri;

}
