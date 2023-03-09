// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package ketai.ui;

import android.app.Activity;
import android.content.DialogInterface;
import processing.core.PApplet;

public class KetaiAlertDialog
{

    public KetaiAlertDialog()
    {
    }

    public static void popup(final PApplet parent, final String title, final String message)
    {
        parent = parent.getActivity();
        parent.runOnUiThread(new Runnable() {

            public void run()
            {
                (new android.app.AlertDialog.Builder(parent)).setTitle(title).setMessage(message).setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialoginterface, int i)
                    {
                    }

                    final _cls1 this$1;

            
            {
                this$1 = _cls1.this;
                super();
            }
                }
).show();
            }

            private final String val$message;
            private final Activity val$parent;
            private final String val$title;

            
            {
                parent = activity;
                title = s;
                message = s1;
                super();
            }
        }
);
    }
}
