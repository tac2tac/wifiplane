// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.widget;

import android.content.Context;
import android.database.*;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

// Referenced classes of package android.support.v4.widget:
//            CursorFilter

public abstract class CursorAdapter extends BaseAdapter
    implements Filterable, CursorFilter.CursorFilterClient
{
    private class ChangeObserver extends ContentObserver
    {

        public boolean deliverSelfNotifications()
        {
            return true;
        }

        public void onChange(boolean flag)
        {
            onContentChanged();
        }

        final CursorAdapter this$0;

        public ChangeObserver()
        {
            this$0 = CursorAdapter.this;
            super(new Handler());
        }
    }

    private class MyDataSetObserver extends DataSetObserver
    {

        public void onChanged()
        {
            mDataValid = true;
            notifyDataSetChanged();
        }

        public void onInvalidated()
        {
            mDataValid = false;
            notifyDataSetInvalidated();
        }

        final CursorAdapter this$0;

        private MyDataSetObserver()
        {
            this$0 = CursorAdapter.this;
            super();
        }

    }


    public CursorAdapter(Context context, Cursor cursor)
    {
        init(context, cursor, 1);
    }

    public CursorAdapter(Context context, Cursor cursor, int i)
    {
        init(context, cursor, i);
    }

    public CursorAdapter(Context context, Cursor cursor, boolean flag)
    {
        int i;
        if(flag)
            i = 1;
        else
            i = 2;
        init(context, cursor, i);
    }

    public abstract void bindView(View view, Context context, Cursor cursor);

    public void changeCursor(Cursor cursor)
    {
        cursor = swapCursor(cursor);
        if(cursor != null)
            cursor.close();
    }

    public CharSequence convertToString(Cursor cursor)
    {
        if(cursor == null)
            cursor = "";
        else
            cursor = cursor.toString();
        return cursor;
    }

    public int getCount()
    {
        int i;
        if(mDataValid && mCursor != null)
            i = mCursor.getCount();
        else
            i = 0;
        return i;
    }

    public Cursor getCursor()
    {
        return mCursor;
    }

    public View getDropDownView(int i, View view, ViewGroup viewgroup)
    {
        if(mDataValid)
        {
            mCursor.moveToPosition(i);
            if(view == null)
                view = newDropDownView(mContext, mCursor, viewgroup);
            bindView(view, mContext, mCursor);
        } else
        {
            view = null;
        }
        return view;
    }

    public Filter getFilter()
    {
        if(mCursorFilter == null)
            mCursorFilter = new CursorFilter(this);
        return mCursorFilter;
    }

    public FilterQueryProvider getFilterQueryProvider()
    {
        return mFilterQueryProvider;
    }

    public Object getItem(int i)
    {
        Cursor cursor;
        if(mDataValid && mCursor != null)
        {
            mCursor.moveToPosition(i);
            cursor = mCursor;
        } else
        {
            cursor = null;
        }
        return cursor;
    }

    public long getItemId(int i)
    {
        long l = 0L;
        long l1 = l;
        if(mDataValid)
        {
            l1 = l;
            if(mCursor != null)
            {
                l1 = l;
                if(mCursor.moveToPosition(i))
                    l1 = mCursor.getLong(mRowIDColumn);
            }
        }
        return l1;
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        if(!mDataValid)
            throw new IllegalStateException("this should only be called when the cursor is valid");
        if(!mCursor.moveToPosition(i))
            throw new IllegalStateException((new StringBuilder()).append("couldn't move cursor to position ").append(i).toString());
        if(view == null)
            view = newView(mContext, mCursor, viewgroup);
        bindView(view, mContext, mCursor);
        return view;
    }

    public boolean hasStableIds()
    {
        return true;
    }

    void init(Context context, Cursor cursor, int i)
    {
        boolean flag = true;
        int j;
        if((i & 1) == 1)
        {
            i |= 2;
            mAutoRequery = true;
        } else
        {
            mAutoRequery = false;
        }
        if(cursor == null)
            flag = false;
        mCursor = cursor;
        mDataValid = flag;
        mContext = context;
        if(flag)
            j = cursor.getColumnIndexOrThrow("_id");
        else
            j = -1;
        mRowIDColumn = j;
        if((i & 2) == 2)
        {
            mChangeObserver = new ChangeObserver();
            mDataSetObserver = new MyDataSetObserver();
        } else
        {
            mChangeObserver = null;
            mDataSetObserver = null;
        }
        if(flag)
        {
            if(mChangeObserver != null)
                cursor.registerContentObserver(mChangeObserver);
            if(mDataSetObserver != null)
                cursor.registerDataSetObserver(mDataSetObserver);
        }
    }

    protected void init(Context context, Cursor cursor, boolean flag)
    {
        int i;
        if(flag)
            i = 1;
        else
            i = 2;
        init(context, cursor, i);
    }

    public View newDropDownView(Context context, Cursor cursor, ViewGroup viewgroup)
    {
        return newView(context, cursor, viewgroup);
    }

    public abstract View newView(Context context, Cursor cursor, ViewGroup viewgroup);

    protected void onContentChanged()
    {
        if(mAutoRequery && mCursor != null && !mCursor.isClosed())
            mDataValid = mCursor.requery();
    }

    public Cursor runQueryOnBackgroundThread(CharSequence charsequence)
    {
        if(mFilterQueryProvider != null)
            charsequence = mFilterQueryProvider.runQuery(charsequence);
        else
            charsequence = mCursor;
        return charsequence;
    }

    public void setFilterQueryProvider(FilterQueryProvider filterqueryprovider)
    {
        mFilterQueryProvider = filterqueryprovider;
    }

    public Cursor swapCursor(Cursor cursor)
    {
        if(cursor == mCursor)
        {
            cursor = null;
        } else
        {
            Cursor cursor1 = mCursor;
            if(cursor1 != null)
            {
                if(mChangeObserver != null)
                    cursor1.unregisterContentObserver(mChangeObserver);
                if(mDataSetObserver != null)
                    cursor1.unregisterDataSetObserver(mDataSetObserver);
            }
            mCursor = cursor;
            if(cursor != null)
            {
                if(mChangeObserver != null)
                    cursor.registerContentObserver(mChangeObserver);
                if(mDataSetObserver != null)
                    cursor.registerDataSetObserver(mDataSetObserver);
                mRowIDColumn = cursor.getColumnIndexOrThrow("_id");
                mDataValid = true;
                notifyDataSetChanged();
                cursor = cursor1;
            } else
            {
                mRowIDColumn = -1;
                mDataValid = false;
                notifyDataSetInvalidated();
                cursor = cursor1;
            }
        }
        return cursor;
    }

    public static final int FLAG_AUTO_REQUERY = 1;
    public static final int FLAG_REGISTER_CONTENT_OBSERVER = 2;
    protected boolean mAutoRequery;
    protected ChangeObserver mChangeObserver;
    protected Context mContext;
    protected Cursor mCursor;
    protected CursorFilter mCursorFilter;
    protected DataSetObserver mDataSetObserver;
    protected boolean mDataValid;
    protected FilterQueryProvider mFilterQueryProvider;
    protected int mRowIDColumn;
}
