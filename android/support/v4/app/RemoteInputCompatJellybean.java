// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.app;

import android.content.*;
import android.os.Bundle;

class RemoteInputCompatJellybean
{

    RemoteInputCompatJellybean()
    {
    }

    static void addResultsToIntent(RemoteInputCompatBase.RemoteInput aremoteinput[], Intent intent, Bundle bundle)
    {
        Bundle bundle1 = new Bundle();
        int i = aremoteinput.length;
        for(int j = 0; j < i; j++)
        {
            RemoteInputCompatBase.RemoteInput remoteinput = aremoteinput[j];
            Object obj = bundle.get(remoteinput.getResultKey());
            if(obj instanceof CharSequence)
                bundle1.putCharSequence(remoteinput.getResultKey(), (CharSequence)obj);
        }

        aremoteinput = new Intent();
        aremoteinput.putExtra("android.remoteinput.resultsData", bundle1);
        intent.setClipData(ClipData.newIntent("android.remoteinput.results", aremoteinput));
    }

    static RemoteInputCompatBase.RemoteInput fromBundle(Bundle bundle, RemoteInputCompatBase.RemoteInput.Factory factory)
    {
        return factory.build(bundle.getString("resultKey"), bundle.getCharSequence("label"), bundle.getCharSequenceArray("choices"), bundle.getBoolean("allowFreeFormInput"), bundle.getBundle("extras"));
    }

    static RemoteInputCompatBase.RemoteInput[] fromBundleArray(Bundle abundle[], RemoteInputCompatBase.RemoteInput.Factory factory)
    {
        if(abundle != null) goto _L2; else goto _L1
_L1:
        RemoteInputCompatBase.RemoteInput aremoteinput[] = null;
_L4:
        return aremoteinput;
_L2:
        RemoteInputCompatBase.RemoteInput aremoteinput1[] = factory.newArray(abundle.length);
        int i = 0;
        do
        {
            aremoteinput = aremoteinput1;
            if(i >= abundle.length)
                continue;
            aremoteinput1[i] = fromBundle(abundle[i], factory);
            i++;
        } while(true);
        if(true) goto _L4; else goto _L3
_L3:
    }

    static Bundle getResultsFromIntent(Intent intent)
    {
        Object obj;
        ClipData clipdata;
        obj = null;
        clipdata = intent.getClipData();
        if(clipdata != null) goto _L2; else goto _L1
_L1:
        intent = obj;
_L4:
        return intent;
_L2:
        ClipDescription clipdescription = clipdata.getDescription();
        intent = obj;
        if(clipdescription.hasMimeType("text/vnd.android.intent"))
        {
            intent = obj;
            if(clipdescription.getLabel().equals("android.remoteinput.results"))
                intent = (Bundle)clipdata.getItemAt(0).getIntent().getExtras().getParcelable("android.remoteinput.resultsData");
        }
        if(true) goto _L4; else goto _L3
_L3:
    }

    static Bundle toBundle(RemoteInputCompatBase.RemoteInput remoteinput)
    {
        Bundle bundle = new Bundle();
        bundle.putString("resultKey", remoteinput.getResultKey());
        bundle.putCharSequence("label", remoteinput.getLabel());
        bundle.putCharSequenceArray("choices", remoteinput.getChoices());
        bundle.putBoolean("allowFreeFormInput", remoteinput.getAllowFreeFormInput());
        bundle.putBundle("extras", remoteinput.getExtras());
        return bundle;
    }

    static Bundle[] toBundleArray(RemoteInputCompatBase.RemoteInput aremoteinput[])
    {
        if(aremoteinput != null) goto _L2; else goto _L1
_L1:
        Bundle abundle[] = null;
_L4:
        return abundle;
_L2:
        Bundle abundle1[] = new Bundle[aremoteinput.length];
        int i = 0;
        do
        {
            abundle = abundle1;
            if(i >= aremoteinput.length)
                continue;
            abundle1[i] = toBundle(aremoteinput[i]);
            i++;
        } while(true);
        if(true) goto _L4; else goto _L3
_L3:
    }

    public static final String EXTRA_RESULTS_DATA = "android.remoteinput.resultsData";
    private static final String KEY_ALLOW_FREE_FORM_INPUT = "allowFreeFormInput";
    private static final String KEY_CHOICES = "choices";
    private static final String KEY_EXTRAS = "extras";
    private static final String KEY_LABEL = "label";
    private static final String KEY_RESULT_KEY = "resultKey";
    public static final String RESULTS_CLIP_LABEL = "android.remoteinput.results";
}
