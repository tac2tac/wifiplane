// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.provider;

import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

// Referenced classes of package android.support.v4.provider:
//            DocumentFile

class RawDocumentFile extends DocumentFile
{

    RawDocumentFile(DocumentFile documentfile, File file)
    {
        super(documentfile);
        mFile = file;
    }

    private static boolean deleteContents(File file)
    {
        File afile[] = file.listFiles();
        boolean flag = true;
        boolean flag1 = true;
        if(afile != null)
        {
            int i = afile.length;
            int j = 0;
            do
            {
                flag = flag1;
                if(j >= i)
                    break;
                file = afile[j];
                flag = flag1;
                if(file.isDirectory())
                    flag = flag1 & deleteContents(file);
                flag1 = flag;
                if(!file.delete())
                {
                    Log.w("DocumentFile", (new StringBuilder()).append("Failed to delete ").append(file).toString());
                    flag1 = false;
                }
                j++;
            } while(true);
        }
        return flag;
    }

    private static String getTypeForName(String s)
    {
        int i = s.lastIndexOf('.');
        if(i < 0) goto _L2; else goto _L1
_L1:
        s = s.substring(i + 1).toLowerCase();
        s = MimeTypeMap.getSingleton().getMimeTypeFromExtension(s);
        if(s == null) goto _L2; else goto _L3
_L3:
        return s;
_L2:
        s = "application/octet-stream";
        if(true) goto _L3; else goto _L4
_L4:
    }

    public boolean canRead()
    {
        return mFile.canRead();
    }

    public boolean canWrite()
    {
        return mFile.canWrite();
    }

    public DocumentFile createDirectory(String s)
    {
        s = new File(mFile, s);
        if(s.isDirectory() || s.mkdir())
            s = new RawDocumentFile(this, s);
        else
            s = null;
        return s;
    }

    public DocumentFile createFile(String s, String s1)
    {
        String s2 = MimeTypeMap.getSingleton().getExtensionFromMimeType(s);
        s = s1;
        if(s2 != null)
            s = (new StringBuilder()).append(s1).append(".").append(s2).toString();
        s1 = new File(mFile, s);
        try
        {
            s1.createNewFile();
            s = JVM INSTR new #2   <Class RawDocumentFile>;
            s.RawDocumentFile(this, s1);
        }
        // Misplaced declaration of an exception variable
        catch(String s)
        {
            Log.w("DocumentFile", (new StringBuilder()).append("Failed to createFile: ").append(s).toString());
            s = null;
        }
        return s;
    }

    public boolean delete()
    {
        deleteContents(mFile);
        return mFile.delete();
    }

    public boolean exists()
    {
        return mFile.exists();
    }

    public String getName()
    {
        return mFile.getName();
    }

    public String getType()
    {
        String s;
        if(mFile.isDirectory())
            s = null;
        else
            s = getTypeForName(mFile.getName());
        return s;
    }

    public Uri getUri()
    {
        return Uri.fromFile(mFile);
    }

    public boolean isDirectory()
    {
        return mFile.isDirectory();
    }

    public boolean isFile()
    {
        return mFile.isFile();
    }

    public long lastModified()
    {
        return mFile.lastModified();
    }

    public long length()
    {
        return mFile.length();
    }

    public DocumentFile[] listFiles()
    {
        ArrayList arraylist = new ArrayList();
        File afile[] = mFile.listFiles();
        if(afile != null)
        {
            int i = afile.length;
            for(int j = 0; j < i; j++)
                arraylist.add(new RawDocumentFile(this, afile[j]));

        }
        return (DocumentFile[])arraylist.toArray(new DocumentFile[arraylist.size()]);
    }

    public boolean renameTo(String s)
    {
        s = new File(mFile.getParentFile(), s);
        boolean flag;
        if(mFile.renameTo(s))
        {
            mFile = s;
            flag = true;
        } else
        {
            flag = false;
        }
        return flag;
    }

    private File mFile;
}
