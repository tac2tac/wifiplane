// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package ketai.ui;

import android.app.Activity;
import android.view.View;
import android.view.ViewManager;
import android.widget.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import processing.core.PApplet;

public class KetaiList extends ListView
{

    public KetaiList(PApplet papplet, String s, ArrayList arraylist)
    {
        super(papplet.getActivity().getApplicationContext());
        name = "KetaiList";
        selection = "";
        title = "";
        parent = papplet;
        title = s;
        adapter = new ArrayAdapter(parent.getActivity(), 0x1090003, arraylist);
        init();
    }

    public KetaiList(PApplet papplet, String s, String as[])
    {
        super(papplet.getActivity().getApplicationContext());
        name = "KetaiList";
        selection = "";
        title = "";
        parent = papplet;
        title = s;
        adapter = new ArrayAdapter(parent.getActivity(), 0x1090003, as);
        init();
    }

    public KetaiList(PApplet papplet, ArrayList arraylist)
    {
        super(papplet.getActivity().getApplicationContext());
        name = "KetaiList";
        selection = "";
        title = "";
        parent = papplet;
        adapter = new ArrayAdapter(parent.getActivity(), 0x1090003, arraylist);
        init();
    }

    public KetaiList(PApplet papplet, String as[])
    {
        super(papplet.getActivity().getApplicationContext());
        name = "KetaiList";
        selection = "";
        title = "";
        parent = papplet;
        adapter = new ArrayAdapter(parent.getActivity(), 0x1090003, as);
        init();
    }

    private void init()
    {
        setBackgroundColor(0xffcccccc);
        setAlpha(1.0F);
        self = this;
        layout = new RelativeLayout(parent.getActivity());
        if(title != "")
        {
            TextView textview = new TextView(parent.getActivity());
            textview.setText(title);
            setHeaderDividersEnabled(true);
            addHeaderView(textview);
        }
        try
        {
            parentCallback = parent.getClass().getMethod("onKetaiListSelection", new Class[] {
                ketai/ui/KetaiList
            });
            PApplet.println("Found onKetaiListSelection...");
        }
        catch(NoSuchMethodException nosuchmethodexception) { }
        setAdapter(adapter);
        setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                selection = ((String)adapter.getItem(i)).toString();
                layout.removeAllViewsInLayout();
                try
                {
                    parentCallback.invoke(parent, new Object[] {
                        self
                    });
                }
                // Misplaced declaration of an exception variable
                catch(AdapterView adapterview) { }
                self.setVisibility(8);
                ((ViewManager)self.getParent()).removeView(self);
                parent.getActivity().runOnUiThread(new Runnable() {

                    public void run()
                    {
                        layout.removeAllViews();
                        layout.setVisibility(8);
                    }

                    final _cls2 this$1;

            
            {
                this$1 = _cls2.this;
                super();
            }
                }
);
            }

            final KetaiList this$0;


            
            {
                this$0 = KetaiList.this;
                super();
            }
        }
);
        parent.getActivity().runOnUiThread(new Runnable() {

            public void run()
            {
                parent.getActivity().addContentView(self, new android.view.ViewGroup.LayoutParams(-1, -1));
            }

            final KetaiList this$0;

            
            {
                this$0 = KetaiList.this;
                super();
            }
        }
);
    }

    public String getSelection()
    {
        return selection;
    }

    public void refresh()
    {
        if(adapter != null)
            parent.getActivity().runOnUiThread(new Runnable() {

                public void run()
                {
                    adapter.notifyDataSetChanged();
                }

                final KetaiList this$0;

            
            {
                this$0 = KetaiList.this;
                super();
            }
            }
);
    }

    private ArrayAdapter adapter;
    RelativeLayout layout;
    String name;
    private PApplet parent;
    private Method parentCallback;
    String selection;
    ListView self;
    String title;



}
