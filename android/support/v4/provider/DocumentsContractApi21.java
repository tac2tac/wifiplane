// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.util.Log;
import java.util.ArrayList;

class DocumentsContractApi21
{

    DocumentsContractApi21()
    {
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

    public static Uri createDirectory(Context context, Uri uri, String s)
    {
        return createFile(context, uri, "vnd.android.document/directory", s);
    }

    public static Uri createFile(Context context, Uri uri, String s, String s1)
    {
        return DocumentsContract.createDocument(context.getContentResolver(), uri, s, s1);
    }

    public static Uri[] listFiles(Context context, Uri uri)
    {
        ContentResolver contentresolver;
        Object obj;
        ArrayList arraylist;
        Object obj1;
        contentresolver = context.getContentResolver();
        obj = DocumentsContract.buildChildDocumentsUriUsingTree(uri, DocumentsContract.getDocumentId(uri));
        arraylist = new ArrayList();
        obj1 = null;
        context = null;
        obj = contentresolver.query(((Uri) (obj)), new String[] {
            "document_id"
        }, null, null, null);
_L2:
        context = ((Context) (obj));
        obj1 = obj;
        if(!((Cursor) (obj)).moveToNext())
            break; /* Loop/switch isn't completed */
        context = ((Context) (obj));
        obj1 = obj;
        arraylist.add(DocumentsContract.buildDocumentUriUsingTree(uri, ((Cursor) (obj)).getString(0)));
        if(true) goto _L2; else goto _L1
        uri;
        obj1 = context;
        obj = JVM INSTR new #86  <Class StringBuilder>;
        obj1 = context;
        ((StringBuilder) (obj)).StringBuilder();
        obj1 = context;
        Log.w("DocumentFile", ((StringBuilder) (obj)).append("Failed query: ").append(uri).toString());
        closeQuietly(context);
_L4:
        return (Uri[])arraylist.toArray(new Uri[arraylist.size()]);
_L1:
        closeQuietly(((AutoCloseable) (obj)));
        if(true) goto _L4; else goto _L3
_L3:
        context;
        closeQuietly(((AutoCloseable) (obj1)));
        throw context;
    }

    public static Uri prepareTreeUri(Uri uri)
    {
        return DocumentsContract.buildDocumentUriUsingTree(uri, DocumentsContract.getTreeDocumentId(uri));
    }

    public static Uri renameTo(Context context, Uri uri, String s)
    {
        return DocumentsContract.renameDocument(context.getContentResolver(), uri, s);
    }

    private static final String TAG = "DocumentFile";
}
