// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package ketai.net.nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.*;
import android.nfc.tech.*;
import android.util.Log;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;
import ketai.net.nfc.record.ParsedNdefRecord;
import processing.core.PApplet;

// Referenced classes of package ketai.net.nfc:
//            NdefMessageParser

public class KetaiNFC
    implements android.nfc.NfcAdapter.CreateNdefMessageCallback, android.nfc.NfcAdapter.OnNdefPushCompleteCallback
{

    public KetaiNFC(PApplet papplet)
    {
        Log.d("KetaiNFC", "KetaiNFC instantiated...");
        parent = papplet;
        findParentIntentions();
        initAdapter();
        papplet = new IntentFilter("android.nfc.action.NDEF_DISCOVERED");
        Object obj = new IntentFilter("android.nfc.action.TECH_DISCOVERED");
        Object obj1 = new IntentFilter("android.nfc.action.TAG_DISCOVERED");
        String s;
        try
        {
            papplet.addDataType("*/*");
            ((IntentFilter) (obj1)).addDataType("*/*");
            ((IntentFilter) (obj)).addDataType("*/*");
        }
        // Misplaced declaration of an exception variable
        catch(PApplet papplet)
        {
            throw new RuntimeException("fail", papplet);
        }
        mFilters = (new IntentFilter[] {
            papplet, obj1, obj
        });
        obj = android/nfc/tech/NfcA.getName();
        s = android/nfc/tech/MifareUltralight.getName();
        obj1 = android/nfc/tech/NfcF.getName();
        papplet = android/nfc/tech/NdefFormatable.getName();
        mTechLists = (new String[][] {
            new String[] {
                obj
            }, new String[] {
                s
            }, new String[] {
                obj1
            }, new String[] {
                papplet
            }
        });
        parent.registerMethod("resume", this);
        parent.registerMethod("pause", this);
        parent.registerMethod("onNewIntent", this);
    }

    private void findParentIntentions()
    {
        NoSuchMethodException nosuchmethodexception;
        try
        {
            onNFCEventMethod_String = parent.getClass().getMethod("onNFCEvent", new Class[] {
                java/lang/String
            });
            Log.d("KetaiNFC", "Found onNFCEvent(String) callback...");
        }
        catch(NoSuchMethodException nosuchmethodexception3) { }
        try
        {
            onNFCWriteMethod = parent.getClass().getMethod("onNFCWrite", new Class[] {
                Boolean.TYPE, java/lang/String
            });
            Log.d("KetaiNFC", "Found onNFCWrite callback...");
        }
        catch(NoSuchMethodException nosuchmethodexception2) { }
        try
        {
            onNFCEventMethod_URI = parent.getClass().getMethod("onNFCEvent", new Class[] {
                java/net/URI
            });
            Log.d("KetaiNFC", "Found onNFCEvent(URI) callback...");
        }
        catch(NoSuchMethodException nosuchmethodexception1) { }
        onNFCEventMethod_bArray = parent.getClass().getMethod("onNFCEvent", new Class[] {
            [B
        });
        Log.d("KetaiNFC", "Found onNFCEvent(byte[]) callback...");
_L2:
        return;
        nosuchmethodexception;
        if(true) goto _L2; else goto _L1
_L1:
    }

    private void initAdapter()
    {
        mAdapter = NfcAdapter.getDefaultAdapter(parent.getContext());
        if(mAdapter == null)
            Log.i("KetaiNFC", "Failed to get NFC adapter...");
        else
            mAdapter.setNdefPushMessageCallback(this, parent.getActivity(), new Activity[0]);
        p = PendingIntent.getActivity(parent.getActivity(), 0, (new Intent(parent.getActivity(), parent.getClass())).addFlags(0x20000000), 0);
    }

    public static NdefRecord newTextRecord(String s, Locale locale, boolean flag)
    {
        byte abyte0[] = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));
        int i;
        if(flag)
            locale = Charset.forName("UTF-8");
        else
            locale = Charset.forName("UTF-16");
        s = s.getBytes(locale);
        if(flag)
            i = 0;
        else
            i = 128;
        i = (char)(abyte0.length + i);
        locale = new byte[abyte0.length + 1 + s.length];
        locale[0] = (byte)i;
        System.arraycopy(abyte0, 0, locale, 1, abyte0.length);
        System.arraycopy(s, 0, locale, abyte0.length + 1, s.length);
        return new NdefRecord((short)1, NdefRecord.RTD_TEXT, new byte[0], locale);
    }

    private void writeNFCString(Tag tag1)
    {
        tag = NdefFormatable.get(tag1);
        ndefTag = null;
        if(tag != null) goto _L2; else goto _L1
_L1:
        PApplet.println("Tag does not support writing (via NdefFormattable). Trying NDEF write...");
        ndefTag = Ndef.get(tag1);
        if(ndefTag == null) goto _L4; else goto _L3
_L3:
        if(!ndefTag.isWritable()) goto _L6; else goto _L5
_L5:
        PApplet.println("KetaiNFC: Tag is NDEF writable.");
        Log.i("KetaiNFC", "NDEFTag is writable");
_L2:
        if(tag != null && messageToWrite != null)
            parent.getActivity().runOnUiThread(new Runnable() {

                public void run()
                {
                    tag.connect();
                    PApplet.println(messageToWrite.toByteArray());
                    if(!tag.isConnected()) goto _L2; else goto _L1
_L1:
                    tag.format(messageToWrite);
                    messageToWrite = null;
_L3:
                    tag.close();
_L4:
                    return;
_L2:
                    PApplet.println("Failed to connect to tag.");
                      goto _L3
                    Object obj;
                    obj;
                    ((FormatException) (obj)).printStackTrace();
                      goto _L4
                    obj;
                    ((IOException) (obj)).printStackTrace();
                    String s = ((IOException) (obj)).getMessage();
                    PApplet.println((new StringBuilder("Failed to write to tag.  Error: ")).append(s).toString());
                    if(onNFCWriteMethod != null)
                        try
                        {
                            onNFCWriteMethod.invoke(parent, new Object[] {
                                Boolean.valueOf(false), s
                            });
                        }
                        catch(Exception exception)
                        {
                            PApplet.println((new StringBuilder("Failed to write nfc tag because of an error:")).append(exception.getMessage()).toString());
                            exception.printStackTrace();
                        }
                      goto _L4
                }

                final KetaiNFC this$0;

            
            {
                this$0 = KetaiNFC.this;
                super();
            }
            }
);
        else
        if(ndefTag != null)
            parent.getActivity().runOnUiThread(new Runnable() {

                public void run()
                {
                    PApplet.println(messageToWrite.toByteArray());
                    ndefTag.connect();
                    if(!ndefTag.isConnected()) goto _L2; else goto _L1
_L1:
                    ndefTag.writeNdefMessage(messageToWrite);
                    messageToWrite = null;
_L3:
                    Method method;
                    ndefTag.close();
                    method = onNFCWriteMethod;
                    if(method == null)
                        break MISSING_BLOCK_LABEL_117;
                    onNFCWriteMethod.invoke(parent, new Object[] {
                        Boolean.valueOf(true), ""
                    });
_L4:
                    return;
_L2:
                    Object obj;
                    PApplet.println("Failed to connect to Tag for an NDef write.");
                      goto _L3
                    obj;
                    try
                    {
                        StringBuilder stringbuilder = JVM INSTR new #100 <Class StringBuilder>;
                        stringbuilder.StringBuilder("Failed to notify sketch of an NFC write Tag operation: ");
                        PApplet.println(stringbuilder.append(((Exception) (obj)).getMessage()).toString());
                    }
                    // Misplaced declaration of an exception variable
                    catch(Object obj)
                    {
                        ((FormatException) (obj)).printStackTrace();
                    }
                    catch(IOException ioexception)
                    {
                        ioexception.printStackTrace();
                        String s = ioexception.getMessage();
                        PApplet.println((new StringBuilder("Failed to write to tag.  Error: ")).append(s).toString());
                        if(onNFCWriteMethod != null)
                            try
                            {
                                onNFCWriteMethod.invoke(parent, new Object[] {
                                    Boolean.valueOf(false), s
                                });
                            }
                            catch(Exception exception)
                            {
                                PApplet.println((new StringBuilder("Failed to write nfc tag & subsequently notify sketch, error:")).append(exception.getMessage()).toString());
                                exception.printStackTrace();
                            }
                    }
                      goto _L4
                }

                final KetaiNFC this$0;

            
            {
                this$0 = KetaiNFC.this;
                super();
            }
            }
);
_L4:
        return;
_L6:
        PApplet.println("KetaiNFC: Tag is NOT writable");
        Log.i("KetaiNFC", "Tag is NOT writable");
        if(onNFCWriteMethod == null) goto _L2; else goto _L7
_L7:
        try
        {
            onNFCWriteMethod.invoke(parent, new Object[] {
                Boolean.valueOf(false), "Tag is NOT writable"
            });
        }
        // Misplaced declaration of an exception variable
        catch(Tag tag1)
        {
            PApplet.println((new StringBuilder(" onNFCWriteEvent()  error:")).append(tag1.getMessage()).toString());
            tag1.printStackTrace();
        }
          goto _L4
    }

    public void beam(String s)
    {
        byte abyte0[] = Locale.US.getLanguage().getBytes(Charset.forName("UTF-8"));
        s = s.getBytes(Charset.forName("UTF-8"));
        char c = (char)(abyte0.length + 0);
        byte abyte1[] = new byte[abyte0.length + 1 + s.length];
        abyte1[0] = (byte)c;
        System.arraycopy(abyte0, 0, abyte1, 1, abyte0.length);
        System.arraycopy(s, 0, abyte1, abyte0.length + 1, s.length);
        messageToBeam = new NdefMessage(new NdefRecord[] {
            new NdefRecord((short)1, NdefRecord.RTD_TEXT, new byte[0], abyte1)
        });
    }

    public void cancelWrite()
    {
        messageToWrite = null;
    }

    public NdefMessage createNdefMessage(NfcEvent nfcevent)
    {
        if(messageToBeam == null)
            beam("");
        PApplet.println((new StringBuilder("createNdefMessage callback called for beam, returning: ")).append(messageToBeam.toString()).toString());
        return messageToBeam;
    }

    public void handleIntent(Intent intent)
    {
        if(mAdapter != null) goto _L2; else goto _L1
_L1:
        return;
_L2:
        String s1;
        android.os.Parcelable aparcelable[];
        Log.i("KetaiNFC", "processing intent...");
        PApplet.println((new StringBuilder("KetaiNFC: Processing intent: ")).append(intent.getAction()).toString());
        String s = intent.getAction();
        s1 = "";
        if(!"android.nfc.action.TAG_DISCOVERED".equals(s) && !"android.nfc.action.NDEF_DISCOVERED".equals(s) && !"android.nfc.action.TECH_DISCOVERED".equals(s))
            continue; /* Loop/switch isn't completed */
        aparcelable = intent.getParcelableArrayExtra("android.nfc.extra.NDEF_MESSAGES");
        intent = intent.getParcelableExtra("android.nfc.extra.TAG");
        if(intent == null || intent.getClass() != android/nfc/Tag) goto _L4; else goto _L3
_L3:
        Object obj;
        String as[];
        int i;
        int j;
        obj = (Tag)intent;
        PApplet.println((new StringBuilder("Found Tag object: ")).append(((Tag) (obj)).toString()).toString());
        intent = "";
        as = ((Tag) (obj)).getTechList();
        i = as.length;
        j = 0;
_L9:
        if(j < i) goto _L6; else goto _L5
_L5:
        PApplet.println((new StringBuilder("Supported Tag tech: ")).append(intent).append("\n").toString());
        intent = ((Intent) (obj));
_L10:
        Object aobj[];
        if(aparcelable == null)
            break MISSING_BLOCK_LABEL_298;
        aobj = new NdefMessage[aparcelable.length];
        j = 0;
_L11:
        if(j < aparcelable.length) goto _L8; else goto _L7
_L7:
        if(intent != null && messageToWrite != null)
        {
            PApplet.println("Attempting to write to Tag");
            writeNFCString(intent);
            continue; /* Loop/switch isn't completed */
        }
        break MISSING_BLOCK_LABEL_342;
_L6:
        String s2 = as[j];
        intent = (new StringBuilder(String.valueOf(intent))).append(s2).append("\n").toString();
        j++;
          goto _L9
_L4:
        intent = null;
          goto _L10
_L8:
        aobj[j] = (NdefMessage)aparcelable[j];
        j++;
          goto _L11
        aobj = new byte[0];
        NdefMessage ndefmessage = new NdefMessage(new NdefRecord[] {
            new NdefRecord((short)5, ((byte []) (aobj)), ((byte []) (aobj)), ((byte []) (aobj)))
        });
        aobj = new NdefMessage[1];
        aobj[0] = ndefmessage;
          goto _L7
        if(aobj == null || aobj.length == 0)
            continue; /* Loop/switch isn't completed */
        intent = new String(((NdefMessage) (aobj[0])).toByteArray());
        PApplet.println((new StringBuilder("got nfc message:")).append(intent).toString());
        aobj = NdefMessageParser.parse(((NdefMessage) (aobj[0])));
        i = ((List) (aobj)).size();
        j = 0;
        intent = s1;
_L13:
label0:
        {
            if(j < i)
                break label0;
            if(onNFCEventMethod_String != null)
                try
                {
                    onNFCEventMethod_String.invoke(parent, new Object[] {
                        intent
                    });
                }
                // Misplaced declaration of an exception variable
                catch(Intent intent)
                {
                    PApplet.println((new StringBuilder("Disabling onNFCEvent() because of an error:")).append(intent.getMessage()).toString());
                    intent.printStackTrace();
                }
        }
        if(true) goto _L1; else goto _L12
_L12:
        ParsedNdefRecord parsedndefrecord = (ParsedNdefRecord)((List) (aobj)).get(j);
        intent = (new StringBuilder(String.valueOf(intent))).append(parsedndefrecord.getTag()).toString();
        j++;
          goto _L13
        if(true) goto _L1; else goto _L14
_L14:
    }

    public void onNdefPushComplete(NfcEvent nfcevent)
    {
        PApplet.println("Completed a beam! clearing out pending message.");
        messageToBeam = null;
    }

    public void onNewIntent(Intent intent)
    {
        PApplet.println("----------------------> KetaiNFC.onNewIntent()");
        handleIntent(intent);
    }

    public void pause()
    {
        Log.i("KetaiNFC", "pausing...");
        PApplet.println("KetaiNFC Pausing...");
        if(mAdapter != null)
            mAdapter.disableForegroundDispatch(parent.getActivity());
    }

    public void resume()
    {
        Log.i("KetaiNFC", "resuming...");
        PApplet.println("KetaiNFC: resuming...");
        if(mAdapter == null) goto _L2; else goto _L1
_L1:
        mAdapter.enableForegroundDispatch(parent.getActivity(), p, mFilters, mTechLists);
_L4:
        return;
_L2:
        PApplet.println("mAdapter was null in onResume()");
        mAdapter = NfcAdapter.getDefaultAdapter(parent.getActivity());
        if(mAdapter != null)
            mAdapter.enableForegroundDispatch(parent.getActivity(), p, mFilters, mTechLists);
        if(true) goto _L4; else goto _L3
_L3:
    }

    public void write(String s)
    {
        byte abyte0[] = Locale.US.getLanguage().getBytes(Charset.forName("UTF-8"));
        s = s.getBytes(Charset.forName("UTF-8"));
        char c = (char)(abyte0.length + 0);
        byte abyte1[] = new byte[abyte0.length + 1 + s.length];
        abyte1[0] = (byte)c;
        System.arraycopy(abyte0, 0, abyte1, 1, abyte0.length);
        System.arraycopy(s, 0, abyte1, abyte0.length + 1, s.length);
        messageToWrite = new NdefMessage(new NdefRecord[] {
            new NdefRecord((short)1, NdefRecord.RTD_TEXT, new byte[0], abyte1)
        });
    }

    public void write(URI uri)
    {
        messageToWrite = new NdefMessage(new NdefRecord[] {
            new NdefRecord((short)3, uri.toString().getBytes(Charset.forName("UTF-8")), new byte[0], new byte[0])
        });
    }

    public void write(byte abyte0[])
    {
        PApplet.println("NFC tag byte writing not yet implemented...");
    }

    private NfcAdapter mAdapter;
    private IntentFilter mFilters[];
    private String mTechLists[][];
    private NdefMessage messageToBeam;
    private NdefMessage messageToWrite;
    Ndef ndefTag;
    private Method onNFCEventMethod_String;
    private Method onNFCEventMethod_URI;
    private Method onNFCEventMethod_bArray;
    private Method onNFCWriteMethod;
    private PendingIntent p;
    private PApplet parent;
    NdefFormatable tag;




}
