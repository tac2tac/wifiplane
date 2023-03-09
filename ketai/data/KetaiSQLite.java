// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package ketai.data;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.*;
import android.os.Environment;
import java.io.*;
import java.util.ArrayList;
import processing.core.PApplet;

public class KetaiSQLite
{
    private class OpenHelper extends SQLiteOpenHelper
    {

        public void onCreate(SQLiteDatabase sqlitedatabase)
        {
        }

        public void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j)
        {
        }

        final KetaiSQLite this$0;

        OpenHelper(Context context1)
        {
            this$0 = KetaiSQLite.this;
            super(context1, DATABASE_NAME, null, 1);
        }

        OpenHelper(Context context1, String s)
        {
            this$0 = KetaiSQLite.this;
            super(context1, s, null, 1);
        }
    }


    public KetaiSQLite(PApplet papplet)
    {
        DATABASE_NAME = "data";
        DATA_ROOT_DIRECTORY = "_data";
        context = papplet.getActivity().getApplicationContext();
        DATABASE_NAME = context.getPackageName();
        DATA_ROOT_DIRECTORY = context.getPackageName();
        PApplet.println((new StringBuilder("data path")).append(context.getDatabasePath(context.getPackageName()).getAbsolutePath()).toString());
        db = (new OpenHelper(context)).getWritableDatabase();
    }

    public KetaiSQLite(PApplet papplet, String s)
    {
        DATABASE_NAME = "data";
        DATA_ROOT_DIRECTORY = "_data";
        context = papplet.getActivity().getApplicationContext();
        DATABASE_NAME = s;
        db = (new OpenHelper(context, s)).getWritableDatabase();
    }

    public static boolean load(PApplet papplet, String s, String s1)
    {
        boolean flag = false;
        InputStream inputstream = papplet.getActivity().getApplicationContext().getAssets().open(s);
        if(inputstream != null) goto _L2; else goto _L1
_L1:
        return flag;
_L2:
        s1 = papplet.getActivity().getApplicationContext().getDatabasePath(s1).getAbsolutePath();
        papplet = JVM INSTR new #114 <Class FileOutputStream>;
        papplet.FileOutputStream(s1);
        s1 = new byte[4096];
_L3:
        int i = inputstream.read(s1);
        if(i > 0)
            break MISSING_BLOCK_LABEL_86;
        papplet.flush();
        papplet.close();
        inputstream.close();
        flag = true;
        continue; /* Loop/switch isn't completed */
        papplet.write(s1, 0, i);
          goto _L3
        papplet;
        PApplet.println((new StringBuilder("Failed to load SQLite file(not found): ")).append(s).toString());
        continue; /* Loop/switch isn't completed */
        papplet;
        PApplet.println((new StringBuilder("IO Error in copying SQLite database ")).append(s).append(": ").append(papplet.getMessage()).toString());
        if(true) goto _L1; else goto _L4
_L4:
    }

    private void writeToFile(String s, String s1, String s2)
    {
        PApplet.print(".");
        StringBuilder stringbuilder = JVM INSTR new #56  <Class StringBuilder>;
        stringbuilder.StringBuilder(String.valueOf(s1));
        s2 = stringbuilder.append("/").append(s2).append(".csv").toString();
        s1 = JVM INSTR new #164 <Class FileWriter>;
        s1.FileWriter(s2, true);
        s2 = JVM INSTR new #169 <Class BufferedWriter>;
        s2.BufferedWriter(s1);
        s2.write(s);
        s2.close();
        s1.close();
_L1:
        return;
        s;
        PApplet.println((new StringBuilder("Error exporting data. (")).append(s.getMessage()).append(") Check the sketch permissions or that the device is not connected in disk mode.").toString());
          goto _L1
    }

    public void close()
    {
        if(db != null)
            db.close();
    }

    public boolean connect()
    {
        return db.isOpen();
    }

    public void deleteAllData()
    {
        Cursor cursor1 = db.rawQuery("select name from SQLite_Master", null);
        if(!cursor1.moveToFirst()) goto _L2; else goto _L1
_L1:
        String s = cursor1.getString(0);
        if(!s.equals("android_metadata")) goto _L4; else goto _L3
_L3:
        if(cursor1.moveToNext()) goto _L1; else goto _L2
_L2:
        if(cursor1 == null)
            break MISSING_BLOCK_LABEL_65;
        if(!cursor1.isClosed())
            cursor1.close();
_L5:
        return;
_L4:
        db.delete(s, null, null);
          goto _L3
        SQLiteException sqliteexception;
        sqliteexception;
        sqliteexception.printStackTrace();
          goto _L5
    }

    public void dispose()
    {
        close();
    }

    public boolean execute(String s)
    {
        db.execSQL(s);
        boolean flag = true;
_L2:
        return flag;
        s;
        PApplet.println((new StringBuilder("Error executing sql statement: ")).append(s.getMessage()).toString());
        flag = false;
        if(true) goto _L2; else goto _L1
_L1:
    }

    public void exportData(String s)
        throws IOException
    {
        File file;
        long l = System.currentTimeMillis();
        file = new File(Environment.getExternalStorageDirectory(), (new StringBuilder(String.valueOf(DATA_ROOT_DIRECTORY))).append("/").append(String.valueOf(l)).toString());
        if(file.exists()) goto _L2; else goto _L1
_L1:
        if(!file.mkdirs()) goto _L4; else goto _L3
_L3:
        PApplet.println((new StringBuilder("success making directory: ")).append(file.getAbsolutePath()).toString());
_L2:
        int i = 0;
        Cursor cursor1 = db.rawQuery("select name from SQLite_Master", null);
        if(!cursor1.moveToFirst() || cursor1.getCount() <= 0) goto _L6; else goto _L5
_L5:
        s = "";
_L9:
        String s1 = cursor1.getString(0);
        if(!s1.equals("android_metadata")) goto _L8; else goto _L7
_L7:
        int j;
        Object obj;
        j = i;
        obj = s;
_L12:
        s = ((String) (obj));
        i = j;
        if(cursor1.moveToNext()) goto _L9; else goto _L6
_L6:
        if(cursor1 == null)
            break MISSING_BLOCK_LABEL_190;
        if(!cursor1.isClosed())
            cursor1.close();
        deleteAllData();
_L10:
        return;
_L4:
        PApplet.println("Failed making directory. Check your sketch permissions or that your device is not connected in disk mode.");
          goto _L10
_L8:
        Object obj1;
        obj = db;
        obj1 = JVM INSTR new #56  <Class StringBuilder>;
        ((StringBuilder) (obj1)).StringBuilder("SELECT * FROM ");
        obj1 = ((SQLiteDatabase) (obj)).rawQuery(((StringBuilder) (obj1)).append(s1).toString(), null);
        obj = s;
        j = i;
        if(!((Cursor) (obj1)).moveToFirst()) goto _L12; else goto _L11
_L11:
        obj = s;
_L14:
        int k = ((Cursor) (obj1)).getColumnCount();
        j = 0;
_L15:
        if(j < k)
            break MISSING_BLOCK_LABEL_389;
        s = JVM INSTR new #56  <Class StringBuilder>;
        s.StringBuilder(String.valueOf(obj));
        obj = s.append("\n").toString();
        j = i + 1;
        s = ((String) (obj));
        i = j;
        if(j <= 100)
            break MISSING_BLOCK_LABEL_353;
        if(((String) (obj)).length() > 0)
            writeToFile(((String) (obj)), file.getAbsolutePath(), s1);
        s = "";
        i = 0;
        obj = s;
        if(((Cursor) (obj1)).moveToNext()) goto _L14; else goto _L13
_L13:
        writeToFile(s, file.getAbsolutePath(), s1);
        obj = "";
        j = 0;
          goto _L12
        s = JVM INSTR new #56  <Class StringBuilder>;
        s.StringBuilder(String.valueOf(obj));
        obj = s.append(((Cursor) (obj1)).getString(j)).append("\t").toString();
        j++;
          goto _L15
        s;
        s.printStackTrace();
          goto _L10
    }

    public byte[] getBlob(int i)
    {
        byte abyte0[];
        if(i < 0)
            abyte0 = null;
        else
            abyte0 = cursor.getBlob(i);
        return abyte0;
    }

    public byte[] getBlob(String s)
    {
        return getBlob(cursor.getColumnIndex(s));
    }

    public long getDataCount()
    {
        long l;
        long l1;
        l = 0L;
        l1 = l;
        Cursor cursor1 = db.rawQuery("select name from SQLite_Master", null);
        long l2;
        l2 = l;
        l1 = l;
        if(!cursor1.moveToFirst()) goto _L2; else goto _L1
_L1:
        l1 = l;
        String s = cursor1.getString(0);
        l1 = l;
        if(!s.equals("android_metadata")) goto _L4; else goto _L3
_L3:
        l2 = l;
_L5:
        l = l2;
        l1 = l2;
        if(cursor1.moveToNext()) goto _L1; else goto _L2
_L2:
        l = l2;
        if(cursor1 == null)
            break MISSING_BLOCK_LABEL_111;
        l = l2;
        l1 = l2;
        if(cursor1.isClosed())
            break MISSING_BLOCK_LABEL_111;
        l1 = l2;
        cursor1.close();
        l = l2;
_L6:
        return l;
_L4:
        l1 = l;
        SQLiteDatabase sqlitedatabase = db;
        l1 = l;
        StringBuilder stringbuilder = JVM INSTR new #56  <Class StringBuilder>;
        l1 = l;
        stringbuilder.StringBuilder("SELECT COUNT(*) FROM ");
        l1 = l;
        sqlStatement = sqlitedatabase.compileStatement(stringbuilder.append(s).toString());
        l1 = l;
        l2 = sqlStatement.simpleQueryForLong();
        l2 = l + l2;
          goto _L5
        SQLiteException sqliteexception;
        sqliteexception;
        sqliteexception.printStackTrace();
        l = l1;
          goto _L6
    }

    public SQLiteDatabase getDb()
    {
        return db;
    }

    public double getDouble(int i)
    {
        double d;
        if(i < 0)
            d = 0.0D;
        else
            d = cursor.getDouble(i);
        return d;
    }

    public double getDouble(String s)
    {
        return getDouble(cursor.getColumnIndex(s));
    }

    public String getFieldMax(String s, String s1)
    {
        s = (new StringBuilder("SELECT MAX(")).append(s1).append(") FROM ").append(s).toString();
        sqlStatement = db.compileStatement(s);
        s1 = sqlStatement.simpleQueryForString();
        s = s1;
        if(s1 == null)
            s = "0";
        return s;
    }

    public String getFieldMin(String s, String s1)
    {
        s = (new StringBuilder("SELECT MIN(")).append(s1).append(") FROM ").append(s).toString();
        sqlStatement = db.compileStatement(s);
        s1 = sqlStatement.simpleQueryForString();
        s = s1;
        if(s1 == null)
            s = "0";
        return s;
    }

    public String[] getFields(String s)
    {
        Object obj;
        obj = (new StringBuilder("PRAGMA table_info(")).append(s).append(");").toString();
        s = new ArrayList();
        obj = db.rawQuery(((String) (obj)), null);
        boolean flag;
        if(((Cursor) (obj)).moveToFirst())
            do
            {
                s.add(((Cursor) (obj)).getString(1));
                flag = ((Cursor) (obj)).moveToNext();
            } while(flag);
_L2:
        String as[] = new String[s.size()];
        s.toArray(as);
        return as;
        SQLiteException sqliteexception;
        sqliteexception;
        sqliteexception.printStackTrace();
        if(true) goto _L2; else goto _L1
_L1:
    }

    public float getFloat(int i)
    {
        float f;
        if(i < 0)
            f = 0.0F;
        else
            f = cursor.getFloat(i);
        return f;
    }

    public float getFloat(String s)
    {
        return getFloat(cursor.getColumnIndex(s));
    }

    public int getInt(int i)
    {
        if(i < 0)
            i = 0;
        else
            i = cursor.getInt(i);
        return i;
    }

    public int getInt(String s)
    {
        return getInt(cursor.getColumnIndex(s));
    }

    public long getLong(int i)
    {
        long l;
        if(i < 0)
            l = 0L;
        else
            l = cursor.getLong(i);
        return l;
    }

    public long getLong(String s)
    {
        return getLong(cursor.getColumnIndex(s));
    }

    public String getPath()
    {
        return db.getPath();
    }

    public long getRecordCount(String s)
    {
        sqlStatement = db.compileStatement((new StringBuilder("SELECT COUNT(*) FROM ")).append(s).toString());
        return sqlStatement.simpleQueryForLong();
    }

    public String getString(int i)
    {
        String s;
        if(i < 0)
            s = null;
        else
            s = cursor.getString(i);
        return s;
    }

    public String getString(String s)
    {
        return getString(cursor.getColumnIndex(s));
    }

    public String[] getTables()
    {
        ArrayList arraylist = new ArrayList();
        Cursor cursor1 = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' ORDER BY name;", null);
        boolean flag;
        if(cursor1.moveToFirst())
            do
            {
                if(cursor1.getString(0) != "android_metadata")
                    arraylist.add(cursor1.getString(0));
                flag = cursor1.moveToNext();
            } while(flag);
_L2:
        String as[] = new String[arraylist.size()];
        arraylist.toArray(as);
        return as;
        SQLiteException sqliteexception;
        sqliteexception;
        sqliteexception.printStackTrace();
        if(true) goto _L2; else goto _L1
_L1:
    }

    public boolean next()
    {
        boolean flag;
        if(cursor == null)
            flag = false;
        else
            flag = cursor.moveToNext();
        return flag;
    }

    public boolean query(String s)
    {
        cursor = db.rawQuery(s, null);
        boolean flag = true;
_L2:
        return flag;
        s;
        PApplet.println((new StringBuilder("Error executing query: ")).append(s.getMessage()).toString());
        flag = false;
        if(true) goto _L2; else goto _L1
_L1:
    }

    public boolean tableExists(String s)
    {
        boolean flag;
        Cursor cursor1;
        flag = false;
        cursor1 = db.rawQuery("select name from SQLite_Master", null);
        if(!cursor1.moveToFirst()) goto _L2; else goto _L1
_L1:
        boolean flag1;
        PApplet.println((new StringBuilder("DataManager found this table: ")).append(cursor1.getString(0)).toString());
        if(!cursor1.getString(0).equalsIgnoreCase(s))
            continue; /* Loop/switch isn't completed */
        flag1 = true;
_L3:
        return flag1;
        if(cursor1.moveToNext()) goto _L1; else goto _L2
_L2:
        flag1 = flag;
        if(cursor1 != null)
        {
            flag1 = flag;
            if(!cursor1.isClosed())
            {
                cursor1.close();
                flag1 = flag;
            }
        }
          goto _L3
    }

    private static final int DATABASE_VERSION = 1;
    private String DATABASE_NAME;
    private String DATA_ROOT_DIRECTORY;
    private Context context;
    private Cursor cursor;
    private SQLiteDatabase db;
    private SQLiteStatement sqlStatement;

}
