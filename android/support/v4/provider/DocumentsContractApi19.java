// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.util.Log;

class DocumentsContractApi19
{

    DocumentsContractApi19()
    {
    }

    public static boolean canRead(Context context, Uri uri)
    {
        boolean flag;
        flag = false;
        break MISSING_BLOCK_LABEL_2;
        if(context.checkCallingOrSelfUriPermission(uri, 1) == 0 && !TextUtils.isEmpty(getRawType(context, uri)))
            flag = true;
        return flag;
    }

    public static boolean canWrite(Context context, Uri uri)
    {
        boolean flag = false;
        if(context.checkCallingOrSelfUriPermission(uri, 2) == 0) goto _L2; else goto _L1
_L1:
        boolean flag1 = flag;
_L4:
        return flag1;
_L2:
        String s = getRawType(context, uri);
        int i = queryForInt(context, uri, "flags", 0);
        flag1 = flag;
        if(!TextUtils.isEmpty(s))
            if((i & 4) != 0)
                flag1 = true;
            else
            if("vnd.android.document/directory".equals(s) && (i & 8) != 0)
            {
                flag1 = true;
            } else
            {
                flag1 = flag;
                if(!TextUtils.isEmpty(s))
                {
                    flag1 = flag;
                    if((i & 2) != 0)
                        flag1 = true;
                }
            }
        if(true) goto _L4; else goto _L3
_L3:
    }

    private static void closeQuietly(AutoCloseable autocloseable)
    {
        if(autocloseable == null)
            break MISSING_BLOCK_LABEL_10;
        autocloseable.close();
_L2:
        return;
        autocloseable;
        throw autocloseable;
        autocloseable;
        if(true) goto _L2; else goto _L1
_L1:
    }

    public static boolean delete(Context context, Uri uri)
    {
        return DocumentsContract.deleteDocument(context.getContentResolver(), uri);
    }

    public static boolean exists(Context context, Uri uri)
    {
        Object obj;
        Object obj1;
        obj = context.getContentResolver();
        obj1 = null;
        context = null;
        uri = ((ContentResolver) (obj)).query(uri, new String[] {
            "document_id"
        }, null, null, null);
        context = uri;
        obj1 = uri;
        int i = uri.getCount();
        boolean flag;
        if(i > 0)
            flag = true;
        else
            flag = false;
        closeQuietly(uri);
_L2:
        return flag;
        uri;
        obj1 = context;
        obj = JVM INSTR new #87  <Class StringBuilder>;
        obj1 = context;
        ((StringBuilder) (obj)).StringBuilder();
        obj1 = context;
        Log.w("DocumentFile", ((StringBuilder) (obj)).append("Failed query: ").append(uri).toString());
        closeQuietly(context);
        flag = false;
        if(true) goto _L2; else goto _L1
_L1:
        context;
        closeQuietly(((AutoCloseable) (obj1)));
        throw context;
    }

    public static String getName(Context context, Uri uri)
    {
        return queryForString(context, uri, "_display_name", null);
    }

    private static String getRawType(Context context, Uri uri)
    {
        return queryForString(context, uri, "mime_type", null);
    }

    public static String getType(Context context, Uri uri)
    {
        uri = getRawType(context, uri);
        context = uri;
        if("vnd.android.document/directory".equals(uri))
            context = null;
        return context;
    }

    public static boolean isDirectory(Context context, Uri uri)
    {
        return "vnd.android.document/directory".equals(getRawType(context, uri));
    }

    public static boolean isDocumentUri(Context context, Uri uri)
    {
        return DocumentsContract.isDocumentUri(context, uri);
    }

    public static boolean isFile(Context context, Uri uri)
    {
        context = getRawType(context, uri);
        boolean flag;
        if("vnd.android.document/directory".equals(context) || TextUtils.isEmpty(context))
            flag = false;
        else
            flag = true;
        return flag;
    }

    public static long lastModified(Context context, Uri uri)
    {
        return queryForLong(context, uri, "last_modified", 0L);
    }

    public static long length(Context context, Uri uri)
    {
        return queryForLong(context, uri, "_size", 0L);
    }

    private static int queryForInt(Context context, Uri uri, String s, int i)
    {
        return (int)queryForLong(context, uri, s, i);
    }

    private static long queryForLong(Context context, Uri uri, String s, long l)
    {
        ContentResolver contentresolver;
        Object obj;
        contentresolver = context.getContentResolver();
        obj = null;
        context = null;
        uri = contentresolver.query(uri, new String[] {
            s
        }, null, null, null);
        context = uri;
        obj = uri;
        if(!uri.moveToFirst()) goto _L2; else goto _L1
_L1:
        context = uri;
        obj = uri;
        if(uri.isNull(0)) goto _L2; else goto _L3
_L3:
        context = uri;
        obj = uri;
        long l1 = uri.getLong(0);
        l = l1;
        closeQuietly(uri);
_L4:
        return l;
_L2:
        closeQuietly(uri);
          goto _L4
        uri;
        obj = context;
        s = JVM INSTR new #87  <Class StringBuilder>;
        obj = context;
        s.StringBuilder();
        obj = context;
        Log.w("DocumentFile", s.append("Failed query: ").append(uri).toString());
        closeQuietly(context);
          goto _L4
        context;
        closeQuietly(((AutoCloseable) (obj)));
        throw context;
    }

    private static String queryForString(Context context, Uri uri, String s, String s1)
    {
        ContentResolver contentresolver;
        Uri uri1;
        contentresolver = context.getContentResolver();
        context = null;
        uri1 = null;
        uri = contentresolver.query(uri, new String[] {
            s
        }, null, null, null);
        uri1 = uri;
        context = uri;
        if(!uri.moveToFirst()) goto _L2; else goto _L1
_L1:
        uri1 = uri;
        context = uri;
        if(uri.isNull(0)) goto _L2; else goto _L3
_L3:
        uri1 = uri;
        context = uri;
        s = uri.getString(0);
        context = s;
        closeQuietly(uri);
_L4:
        return context;
_L2:
        closeQuietly(uri);
        context = s1;
          goto _L4
        s;
        context = uri1;
        uri = JVM INSTR new #87  <Class StringBuilder>;
        context = uri1;
        uri.StringBuilder();
        context = uri1;
        Log.w("DocumentFile", uri.append("Failed query: ").append(s).toString());
        closeQuietly(uri1);
        context = s1;
          goto _L4
        uri;
        closeQuietly(context);
        throw uri;
    }

    private static final String TAG = "DocumentFile";
}
