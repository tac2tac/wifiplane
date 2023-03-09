// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.content;

import android.content.*;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import java.io.*;
import java.util.*;
import org.xmlpull.v1.XmlPullParserException;

public class FileProvider extends ContentProvider
{
    static interface PathStrategy
    {

        public abstract File getFileForUri(Uri uri);

        public abstract Uri getUriForFile(File file);
    }

    static class SimplePathStrategy
        implements PathStrategy
    {

        public void addRoot(String s, File file)
        {
            if(TextUtils.isEmpty(s))
                throw new IllegalArgumentException("Name must not be empty");
            File file1;
            try
            {
                file1 = file.getCanonicalFile();
            }
            // Misplaced declaration of an exception variable
            catch(String s)
            {
                throw new IllegalArgumentException((new StringBuilder()).append("Failed to resolve canonical path for ").append(file).toString(), s);
            }
            mRoots.put(s, file1);
        }

        public File getFileForUri(Uri uri)
        {
            Object obj = uri.getEncodedPath();
            int i = ((String) (obj)).indexOf('/', 1);
            Object obj1 = Uri.decode(((String) (obj)).substring(1, i));
            obj = Uri.decode(((String) (obj)).substring(i + 1));
            obj1 = (File)mRoots.get(obj1);
            if(obj1 == null)
                throw new IllegalArgumentException((new StringBuilder()).append("Unable to find configured root for ").append(uri).toString());
            uri = new File(((File) (obj1)), ((String) (obj)));
            try
            {
                obj = uri.getCanonicalFile();
            }
            catch(IOException ioexception)
            {
                throw new IllegalArgumentException((new StringBuilder()).append("Failed to resolve canonical path for ").append(uri).toString());
            }
            if(!((File) (obj)).getPath().startsWith(((File) (obj1)).getPath()))
                throw new SecurityException("Resolved path jumped beyond configured root");
            else
                return ((File) (obj));
        }

        public Uri getUriForFile(File file)
        {
            String s;
            Iterator iterator;
            try
            {
                s = file.getCanonicalPath();
            }
            catch(IOException ioexception)
            {
                throw new IllegalArgumentException((new StringBuilder()).append("Failed to resolve canonical path for ").append(file).toString());
            }
            file = null;
            iterator = mRoots.entrySet().iterator();
            do
            {
                if(!iterator.hasNext())
                    break;
                java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
                String s2 = ((File)entry.getValue()).getPath();
                if(s.startsWith(s2) && (file == null || s2.length() > ((File)file.getValue()).getPath().length()))
                    file = entry;
            } while(true);
            if(file == null)
                throw new IllegalArgumentException((new StringBuilder()).append("Failed to find configured root that contains ").append(s).toString());
            String s1 = ((File)file.getValue()).getPath();
            if(s1.endsWith("/"))
                s1 = s.substring(s1.length());
            else
                s1 = s.substring(s1.length() + 1);
            file = (new StringBuilder()).append(Uri.encode((String)file.getKey())).append('/').append(Uri.encode(s1, "/")).toString();
            return (new android.net.Uri.Builder()).scheme("content").authority(mAuthority).encodedPath(file).build();
        }

        private final String mAuthority;
        private final HashMap mRoots = new HashMap();

        public SimplePathStrategy(String s)
        {
            mAuthority = s;
        }
    }


    public FileProvider()
    {
    }

    private static transient File buildPath(File file, String as[])
    {
        int i = as.length;
        for(int j = 0; j < i; j++)
        {
            String s = as[j];
            if(s != null)
                file = new File(file, s);
        }

        return file;
    }

    private static Object[] copyOf(Object aobj[], int i)
    {
        Object aobj1[] = new Object[i];
        System.arraycopy(((Object) (aobj)), 0, ((Object) (aobj1)), 0, i);
        return aobj1;
    }

    private static String[] copyOf(String as[], int i)
    {
        String as1[] = new String[i];
        System.arraycopy(as, 0, as1, 0, i);
        return as1;
    }

    private static PathStrategy getPathStrategy(Context context, String s)
    {
        HashMap hashmap = sCache;
        hashmap;
        JVM INSTR monitorenter ;
        PathStrategy pathstrategy = (PathStrategy)sCache.get(s);
        PathStrategy pathstrategy1;
        pathstrategy1 = pathstrategy;
        if(pathstrategy != null)
            break MISSING_BLOCK_LABEL_41;
        pathstrategy1 = parsePathStrategy(context, s);
        sCache.put(s, pathstrategy1);
        hashmap;
        JVM INSTR monitorexit ;
        return pathstrategy1;
        s;
        context = JVM INSTR new #104 <Class IllegalArgumentException>;
        context.IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", s);
        throw context;
        context;
        hashmap;
        JVM INSTR monitorexit ;
        throw context;
        s;
        context = JVM INSTR new #104 <Class IllegalArgumentException>;
        context.IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", s);
        throw context;
    }

    public static Uri getUriForFile(Context context, String s, File file)
    {
        return getPathStrategy(context, s).getUriForFile(file);
    }

    private static int modeToMode(String s)
    {
        int i;
        if("r".equals(s))
            i = 0x10000000;
        else
        if("w".equals(s) || "wt".equals(s))
            i = 0x2c000000;
        else
        if("wa".equals(s))
            i = 0x2a000000;
        else
        if("rw".equals(s))
            i = 0x38000000;
        else
        if("rwt".equals(s))
            i = 0x3c000000;
        else
            throw new IllegalArgumentException((new StringBuilder()).append("Invalid mode: ").append(s).toString());
        return i;
    }

    private static PathStrategy parsePathStrategy(Context context, String s)
        throws IOException, XmlPullParserException
    {
        SimplePathStrategy simplepathstrategy;
        XmlResourceParser xmlresourceparser;
        simplepathstrategy = new SimplePathStrategy(s);
        xmlresourceparser = context.getPackageManager().resolveContentProvider(s, 128).loadXmlMetaData(context.getPackageManager(), "android.support.FILE_PROVIDER_PATHS");
        if(xmlresourceparser == null)
            throw new IllegalArgumentException("Missing android.support.FILE_PROVIDER_PATHS meta-data");
_L7:
        int i = xmlresourceparser.next();
        if(i == 1) goto _L2; else goto _L1
_L1:
        String s1;
        String s2;
        String s3;
        if(i != 2)
            continue; /* Loop/switch isn't completed */
        s1 = xmlresourceparser.getName();
        s2 = xmlresourceparser.getAttributeValue(null, "name");
        s3 = xmlresourceparser.getAttributeValue(null, "path");
        s = null;
        if(!"root-path".equals(s1)) goto _L4; else goto _L3
_L3:
        s = buildPath(DEVICE_ROOT, new String[] {
            s3
        });
_L5:
        if(s != null)
            simplepathstrategy.addRoot(s2, s);
        continue; /* Loop/switch isn't completed */
_L4:
        if("files-path".equals(s1))
            s = buildPath(context.getFilesDir(), new String[] {
                s3
            });
        else
        if("cache-path".equals(s1))
            s = buildPath(context.getCacheDir(), new String[] {
                s3
            });
        else
        if("external-path".equals(s1))
            s = buildPath(Environment.getExternalStorageDirectory(), new String[] {
                s3
            });
        if(true) goto _L5; else goto _L2
_L2:
        return simplepathstrategy;
        if(true) goto _L7; else goto _L6
_L6:
    }

    public void attachInfo(Context context, ProviderInfo providerinfo)
    {
        super.attachInfo(context, providerinfo);
        if(providerinfo.exported)
            throw new SecurityException("Provider must not be exported");
        if(!providerinfo.grantUriPermissions)
        {
            throw new SecurityException("Provider must grant uri permissions");
        } else
        {
            mStrategy = getPathStrategy(context, providerinfo.authority);
            return;
        }
    }

    public int delete(Uri uri, String s, String as[])
    {
        int i;
        if(mStrategy.getFileForUri(uri).delete())
            i = 1;
        else
            i = 0;
        return i;
    }

    public String getType(Uri uri)
    {
        int i;
        uri = mStrategy.getFileForUri(uri);
        i = uri.getName().lastIndexOf('.');
        if(i < 0) goto _L2; else goto _L1
_L1:
        uri = uri.getName().substring(i + 1);
        uri = MimeTypeMap.getSingleton().getMimeTypeFromExtension(uri);
        if(uri == null) goto _L2; else goto _L3
_L3:
        return uri;
_L2:
        uri = "application/octet-stream";
        if(true) goto _L3; else goto _L4
_L4:
    }

    public Uri insert(Uri uri, ContentValues contentvalues)
    {
        throw new UnsupportedOperationException("No external inserts");
    }

    public boolean onCreate()
    {
        return true;
    }

    public ParcelFileDescriptor openFile(Uri uri, String s)
        throws FileNotFoundException
    {
        return ParcelFileDescriptor.open(mStrategy.getFileForUri(uri), modeToMode(s));
    }

    public Cursor query(Uri uri, String as[], String s, String as1[], String s1)
    {
        s = mStrategy.getFileForUri(uri);
        uri = as;
        if(as == null)
            uri = COLUMNS;
        as1 = new String[uri.length];
        as = ((String []) (new Object[uri.length]));
        int i = uri.length;
        int j = 0;
        int k = 0;
        while(j < i) 
        {
            s1 = uri[j];
            if("_display_name".equals(s1))
            {
                as1[k] = "_display_name";
                int l = k + 1;
                as[k] = s.getName();
                k = l;
            } else
            if("_size".equals(s1))
            {
                as1[k] = "_size";
                int i1 = k + 1;
                as[k] = Long.valueOf(s.length());
                k = i1;
            }
            j++;
        }
        uri = copyOf(as1, k);
        as = ((String []) (copyOf(as, k)));
        uri = new MatrixCursor(uri, 1);
        uri.addRow(as);
        return uri;
    }

    public int update(Uri uri, ContentValues contentvalues, String s, String as[])
    {
        throw new UnsupportedOperationException("No external updates");
    }

    private static final String ATTR_NAME = "name";
    private static final String ATTR_PATH = "path";
    private static final String COLUMNS[] = {
        "_display_name", "_size"
    };
    private static final File DEVICE_ROOT = new File("/");
    private static final String META_DATA_FILE_PROVIDER_PATHS = "android.support.FILE_PROVIDER_PATHS";
    private static final String TAG_CACHE_PATH = "cache-path";
    private static final String TAG_EXTERNAL = "external-path";
    private static final String TAG_FILES_PATH = "files-path";
    private static final String TAG_ROOT_PATH = "root-path";
    private static HashMap sCache = new HashMap();
    private PathStrategy mStrategy;

}
