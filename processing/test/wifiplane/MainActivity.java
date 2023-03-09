// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.test.wifiplane;

import android.app.*;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.Window;
import android.widget.FrameLayout;
import java.util.ArrayList;
import processing.core.PApplet;

// Referenced classes of package processing.test.wifiplane:
//            wifiplane

public class MainActivity extends Activity
{

    public MainActivity()
    {
        viewId = 4096;
    }

    public void onBackPressed()
    {
        fragment.onBackPressed();
        super.onBackPressed();
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        Object obj = getWindow();
        requestWindowFeature(1);
        ((Window) (obj)).setFlags(256, 256);
        ((Window) (obj)).setFlags(1024, 1024);
        obj = new FrameLayout(this);
        ((FrameLayout) (obj)).setId(viewId);
        setContentView(((android.view.View) (obj)), new android.view.ViewGroup.LayoutParams(-1, -1));
        if(bundle == null)
        {
            fragment = new wifiplane();
            getFragmentManager().beginTransaction().add(((FrameLayout) (obj)).getId(), fragment, "main_fragment").commit();
        } else
        {
            fragment = (PApplet)getFragmentManager().findFragmentByTag("main_fragment");
        }
    }

    public void onRequestPermissionsResult(int i, String as[], int ai[])
    {
        if(i != 1 || ai.length <= 0) goto _L2; else goto _L1
_L1:
        i = 0;
_L6:
        if(i < ai.length) goto _L4; else goto _L3
_L3:
        fragment.onPermissionsGranted();
_L2:
        return;
_L4:
        if(ai[i] != 0)
        {
            as = new android.app.AlertDialog.Builder(this);
            as.setMessage("The app cannot run without these permissions, will quit now.").setCancelable(false).setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialoginterface, int j)
                {
                    finish();
                }

                final MainActivity this$0;

            
            {
                this$0 = MainActivity.this;
                super();
            }
            }
);
            as.create().show();
        }
        i++;
        if(true) goto _L6; else goto _L5
_L5:
    }

    public void onStart()
    {
        ArrayList arraylist;
        super.onStart();
        arraylist = new ArrayList();
        if(arraylist.isEmpty()) goto _L2; else goto _L1
_L1:
        ActivityCompat.requestPermissions(this, (String[])arraylist.toArray(new String[arraylist.size()]), 1);
_L4:
        return;
_L2:
        if(false)
            fragment.onPermissionsGranted();
        if(true) goto _L4; else goto _L3
_L3:
    }

    private static final String MAIN_FRAGMENT_TAG = "main_fragment";
    private static final int REQUEST_PERMISSIONS = 1;
    PApplet fragment;
    int viewId;
}
