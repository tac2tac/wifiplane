// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.data;

import java.io.*;
import java.lang.reflect.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import processing.core.PApplet;

// Referenced classes of package processing.data:
//            TableRow, XML, FloatDict, FloatList, 
//            IntDict, IntList, StringList, StringDict, 
//            Sort

public class Table
{
    class HashMapBlows
    {

        private void writeln(PrintWriter printwriter)
            throws IOException
        {
            for(Iterator iterator = indexToData.iterator(); iterator.hasNext(); printwriter.println((String)iterator.next()));
            printwriter.flush();
            printwriter.close();
        }

        int index(String s)
        {
            Integer integer = (Integer)dataToIndex.get(s);
            int i;
            if(integer != null)
            {
                i = integer.intValue();
            } else
            {
                i = dataToIndex.size();
                dataToIndex.put(s, Integer.valueOf(i));
                indexToData.add(s);
            }
            return i;
        }

        String key(int i)
        {
            return (String)indexToData.get(i);
        }

        void read(DataInputStream datainputstream)
            throws IOException
        {
            int i = datainputstream.readInt();
            dataToIndex = new HashMap(i);
            for(int j = 0; j < i; j++)
            {
                String s = datainputstream.readUTF();
                dataToIndex.put(s, Integer.valueOf(j));
                indexToData.add(s);
            }

        }

        int size()
        {
            return dataToIndex.size();
        }

        void write(DataOutputStream dataoutputstream)
            throws IOException
        {
            dataoutputstream.writeInt(size());
            for(Iterator iterator = indexToData.iterator(); iterator.hasNext(); dataoutputstream.writeUTF((String)iterator.next()));
        }

        HashMap dataToIndex;
        ArrayList indexToData;
        final Table this$0;


        HashMapBlows()
        {
            this$0 = Table.this;
            super();
            dataToIndex = new HashMap();
            indexToData = new ArrayList();
        }

        HashMapBlows(DataInputStream datainputstream)
            throws IOException
        {
            this$0 = Table.this;
            super();
            dataToIndex = new HashMap();
            indexToData = new ArrayList();
            read(datainputstream);
        }
    }

    static class RowIndexIterator
        implements Iterator
    {

        public boolean hasNext()
        {
            boolean flag;
            if(index + 1 < indices.length)
                flag = true;
            else
                flag = false;
            return flag;
        }

        public volatile Object next()
        {
            return next();
        }

        public TableRow next()
        {
            RowPointer rowpointer = rp;
            int ai[] = indices;
            int i = index + 1;
            index = i;
            rowpointer.setRow(ai[i]);
            return rp;
        }

        public void remove()
        {
            table.removeRow(indices[index]);
        }

        public void reset()
        {
            index = -1;
        }

        int index;
        int indices[];
        RowPointer rp;
        Table table;

        public RowIndexIterator(Table table1, int ai[])
        {
            table = table1;
            indices = ai;
            index = -1;
            rp = new RowPointer(table1, -1);
        }
    }

    static class RowIterator
        implements Iterator
    {

        public boolean hasNext()
        {
            boolean flag;
            if(row + 1 < table.getRowCount())
                flag = true;
            else
                flag = false;
            return flag;
        }

        public volatile Object next()
        {
            return next();
        }

        public TableRow next()
        {
            RowPointer rowpointer = rp;
            int i = row + 1;
            row = i;
            rowpointer.setRow(i);
            return rp;
        }

        public void remove()
        {
            table.removeRow(row);
        }

        public void reset()
        {
            row = -1;
        }

        int row;
        RowPointer rp;
        Table table;

        public RowIterator(Table table1)
        {
            table = table1;
            row = -1;
            rp = new RowPointer(table1, row);
        }
    }

    static class RowPointer
        implements TableRow
    {

        public int getColumnCount()
        {
            return table.getColumnCount();
        }

        public int getColumnType(int i)
        {
            return table.getColumnType(i);
        }

        public int getColumnType(String s)
        {
            return table.getColumnType(s);
        }

        public int[] getColumnTypes()
        {
            return table.getColumnTypes();
        }

        public double getDouble(int i)
        {
            return table.getDouble(row, i);
        }

        public double getDouble(String s)
        {
            return table.getDouble(row, s);
        }

        public float getFloat(int i)
        {
            return table.getFloat(row, i);
        }

        public float getFloat(String s)
        {
            return table.getFloat(row, s);
        }

        public int getInt(int i)
        {
            return table.getInt(row, i);
        }

        public int getInt(String s)
        {
            return table.getInt(row, s);
        }

        public long getLong(int i)
        {
            return table.getLong(row, i);
        }

        public long getLong(String s)
        {
            return table.getLong(row, s);
        }

        public String getString(int i)
        {
            return table.getString(row, i);
        }

        public String getString(String s)
        {
            return table.getString(row, s);
        }

        public void setDouble(int i, double d)
        {
            table.setDouble(row, i, d);
        }

        public void setDouble(String s, double d)
        {
            table.setDouble(row, s, d);
        }

        public void setFloat(int i, float f)
        {
            table.setFloat(row, i, f);
        }

        public void setFloat(String s, float f)
        {
            table.setFloat(row, s, f);
        }

        public void setInt(int i, int j)
        {
            table.setInt(row, i, j);
        }

        public void setInt(String s, int i)
        {
            table.setInt(row, s, i);
        }

        public void setLong(int i, long l)
        {
            table.setLong(row, i, l);
        }

        public void setLong(String s, long l)
        {
            table.setLong(row, s, l);
        }

        public void setRow(int i)
        {
            row = i;
        }

        public void setString(int i, String s)
        {
            table.setString(row, i, s);
        }

        public void setString(String s, String s1)
        {
            table.setString(row, s, s1);
        }

        int row;
        Table table;

        public RowPointer(Table table1, int i)
        {
            table = table1;
            row = i;
        }
    }


    public Table()
    {
        missingString = null;
        missingInt = 0;
        missingLong = 0L;
        missingFloat = (0.0F / 0.0F);
        missingDouble = (0.0D / 0.0D);
        missingCategory = -1;
        init();
    }

    public Table(File file)
        throws IOException
    {
        this(file, null);
    }

    public Table(File file, String s)
        throws IOException
    {
        missingString = null;
        missingInt = 0;
        missingLong = 0L;
        missingFloat = (0.0F / 0.0F);
        missingDouble = (0.0D / 0.0D);
        missingCategory = -1;
        init();
        parse(PApplet.createInput(file), extensionOptions(true, file.getName(), s));
    }

    public Table(InputStream inputstream)
        throws IOException
    {
        this(inputstream, null);
    }

    public Table(InputStream inputstream, String s)
        throws IOException
    {
        missingString = null;
        missingInt = 0;
        missingLong = 0L;
        missingFloat = (0.0F / 0.0F);
        missingDouble = (0.0D / 0.0D);
        missingCategory = -1;
        init();
        parse(inputstream, s);
    }

    public Table(Iterable iterable)
    {
        missingString = null;
        missingInt = 0;
        missingLong = 0L;
        missingFloat = (0.0F / 0.0F);
        missingDouble = (0.0D / 0.0D);
        missingCategory = -1;
        for(Iterator iterator = iterable.iterator(); iterator.hasNext(); addRow(iterable))
        {
            iterable = (TableRow)iterator.next();
            setColumnTypes(iterable.getColumnTypes());
        }

    }

    public Table(ResultSet resultset)
    {
        int i;
        int j;
        missingString = null;
        missingInt = 0;
        missingLong = 0L;
        missingFloat = (0.0F / 0.0F);
        missingDouble = (0.0D / 0.0D);
        missingCategory = -1;
        init();
        ResultSetMetaData resultsetmetadata;
        try
        {
            resultsetmetadata = resultset.getMetaData();
            i = resultsetmetadata.getColumnCount();
            setColumnCount(i);
        }
        // Misplaced declaration of an exception variable
        catch(ResultSet resultset)
        {
            throw new RuntimeException(resultset);
        }
        j = 0;
        if(j >= i)
            break MISSING_BLOCK_LABEL_224;
        setColumnTitle(j, resultsetmetadata.getColumnName(j + 1));
        resultsetmetadata.getColumnType(j + 1);
        JVM INSTR tableswitch -6 8: default 168
    //                   -6 174
    //                   -5 194
    //                   -4 168
    //                   -3 168
    //                   -2 168
    //                   -1 168
    //                   0 168
    //                   1 168
    //                   2 168
    //                   3 214
    //                   4 174
    //                   5 174
    //                   6 204
    //                   7 214
    //                   8 214;
           goto _L1 _L2 _L3 _L1 _L1 _L1 _L1 _L1 _L1 _L1 _L4 _L2 _L2 _L5 _L4 _L4
_L4:
        break MISSING_BLOCK_LABEL_214;
_L1:
        break; /* Loop/switch isn't completed */
_L2:
        break; /* Loop/switch isn't completed */
_L9:
        j++;
        if(true) goto _L7; else goto _L6
_L7:
        break MISSING_BLOCK_LABEL_63;
_L6:
        setColumnType(j, 1);
        continue; /* Loop/switch isn't completed */
_L3:
        setColumnType(j, 2);
        continue; /* Loop/switch isn't completed */
_L5:
        setColumnType(j, 3);
        continue; /* Loop/switch isn't completed */
        setColumnType(j, 4);
        if(true) goto _L9; else goto _L8
_L8:
        j = 0;
_L20:
        if(!resultset.next())
            break; /* Loop/switch isn't completed */
        int k = 0;
_L17:
        if(k >= i)
            break MISSING_BLOCK_LABEL_438;
        columnTypes[k];
        JVM INSTR tableswitch 0 4: default 288
    //                   0 330
    //                   1 354
    //                   2 375
    //                   3 396
    //                   4 417;
           goto _L10 _L11 _L12 _L13 _L14 _L15
_L15:
        break MISSING_BLOCK_LABEL_417;
_L12:
        break; /* Loop/switch isn't completed */
_L10:
        IllegalArgumentException illegalargumentexception = JVM INSTR new #217 <Class IllegalArgumentException>;
        resultset = JVM INSTR new #219 <Class StringBuilder>;
        resultset.StringBuilder();
        illegalargumentexception.IllegalArgumentException(resultset.append("column type ").append(columnTypes[k]).append(" not supported.").toString());
        throw illegalargumentexception;
_L11:
        setString(j, k, resultset.getString(k + 1));
_L18:
        k++;
        if(true) goto _L17; else goto _L16
_L16:
        setInt(j, k, resultset.getInt(k + 1));
          goto _L18
_L13:
        setLong(j, k, resultset.getLong(k + 1));
          goto _L18
_L14:
        setFloat(j, k, resultset.getFloat(k + 1));
          goto _L18
        setDouble(j, k, resultset.getDouble(k + 1));
          goto _L18
        j++;
        if(true) goto _L20; else goto _L19
_L19:
    }

    public static String extensionOptions(boolean flag, String s, String s1)
    {
        String s2;
        s2 = PApplet.checkExtension(s);
        s = s1;
        if(s2 == null) goto _L2; else goto _L1
_L1:
        int j;
        String as[];
        int i;
        if(flag)
            as = loadExtensions;
        else
            as = saveExtensions;
        i = as.length;
        j = 0;
_L7:
        s = s1;
        if(j >= i) goto _L2; else goto _L3
_L3:
        if(!s2.equals(as[j])) goto _L5; else goto _L4
_L4:
        if(s1 == null)
            s = s2;
        else
            s = (new StringBuilder()).append(s2).append(",").append(s1).toString();
_L2:
        return s;
_L5:
        j++;
        if(true) goto _L7; else goto _L6
_L6:
    }

    protected static int nextComma(char ac[], int i)
    {
        boolean flag = false;
_L5:
        if(i >= ac.length) goto _L2; else goto _L1
_L1:
        if(flag || ac[i] != ',') goto _L4; else goto _L3
_L3:
        return i;
_L4:
        boolean flag1 = flag;
        if(ac[i] == '"')
            if(!flag)
                flag1 = true;
            else
                flag1 = false;
        i++;
        flag = flag1;
          goto _L5
_L2:
        i = ac.length;
          goto _L3
    }

    private void odsAppendNotNull(XML xml, StringBuffer stringbuffer)
    {
        xml = xml.getContent();
        if(xml != null)
            stringbuffer.append(xml);
    }

    private InputStream odsFindContentXML(InputStream inputstream)
    {
        inputstream = new ZipInputStream(inputstream);
_L4:
        ZipEntry zipentry = inputstream.getNextEntry();
        if(zipentry == null) goto _L2; else goto _L1
_L1:
        boolean flag = zipentry.getName().equals("content.xml");
        if(!flag) goto _L4; else goto _L3
_L3:
        return inputstream;
        inputstream;
        inputstream.printStackTrace();
_L2:
        inputstream = null;
        if(true) goto _L3; else goto _L5
_L5:
    }

    private void odsParseSheet(XML xml)
    {
        XML axml[] = xml.getChildren("table:table-row");
        int i = axml.length;
        int j = 0;
        int l;
        for(int k = 0; j < i; k += l)
        {
            xml = axml[j];
            l = xml.getInt("table:number-rows-repeated", 1);
            int i1 = 0;
            XML axml1[] = xml.getChildren();
            int j1 = 0;
            int l1 = axml1.length;
            int k3;
            for(int i2 = 0; i2 < l1; i2 = k3)
            {
                XML xml1 = axml1[i2];
                int j2 = xml1.getInt("table:number-columns-repeated", 1);
                String s = xml1.getString("office:value");
                xml = s;
                if(s == null)
                {
                    xml = s;
                    if(xml1.getChildCount() != 0)
                    {
                        xml = xml1.getChildren("text:p");
                        if(xml.length != 1)
                        {
                            i1 = xml.length;
                            for(j1 = 0; j1 < i1; j1++)
                            {
                                XML xml3 = xml[j1];
                                System.err.println(xml3.toString());
                            }

                            throw new RuntimeException("found more than one text:p element");
                        }
                        XML xml4 = xml[0];
                        xml = xml4.getContent();
                        int k2;
                        int i3;
                        if(xml == null)
                        {
                            XML axml2[] = xml4.getChildren();
                            xml = new StringBuffer();
                            int l3 = axml2.length;
                            int j3 = 0;
                            while(j3 < l3) 
                            {
                                XML xml2 = axml2[j3];
                                String s1 = xml2.getName();
                                if(s1 == null)
                                    odsAppendNotNull(xml2, xml);
                                else
                                if(s1.equals("text:s"))
                                {
                                    int i4 = xml2.getInt("text:c", 1);
                                    int l2 = 0;
                                    while(l2 < i4) 
                                    {
                                        xml.append(' ');
                                        l2++;
                                    }
                                } else
                                if(s1.equals("text:span"))
                                    odsAppendNotNull(xml2, xml);
                                else
                                if(s1.equals("text:a"))
                                {
                                    xml.append(xml2.getString("xlink:href"));
                                } else
                                {
                                    odsAppendNotNull(xml2, xml);
                                    System.err.println((new StringBuilder()).append(getClass().getName()).append(": don't understand: ").append(xml2).toString());
                                }
                                j3++;
                            }
                            xml = xml.toString();
                        }
                    }
                }
                k2 = i1;
                i3 = 0;
                i1 = j1;
                j1 = k2;
                for(; i3 < j2; i3++)
                {
                    if(xml != null)
                        setString(k, i1, xml);
                    i1++;
                    if(xml != null)
                        j1 = 1;
                }

                k3 = i2 + 1;
                i2 = j1;
                j1 = i1;
                i1 = i2;
            }

            if(i1 != 0 && l > 1)
            {
                xml = getStringRow(k);
                for(int k1 = 1; k1 < l; k1++)
                    addRow(xml);

            }
            j++;
        }

    }

    protected static String[] splitLineCSV(String s)
    {
        char ac[];
        int i1;
        int l1;
        boolean flag = false;
        ac = s.toCharArray();
        int j = 0;
        boolean flag1 = false;
        i1 = 1;
        while(j < ac.length) 
        {
            int j1;
            boolean flag2;
            if(!flag1 && ac[j] == ',')
            {
                j1 = i1 + 1;
                flag2 = flag1;
            } else
            {
                flag2 = flag1;
                j1 = i1;
                if(ac[j] == '"')
                    if(!flag1)
                    {
                        flag2 = true;
                        j1 = i1;
                    } else
                    {
                        flag2 = false;
                        j1 = i1;
                    }
            }
            j++;
            flag1 = flag2;
            i1 = j1;
        }
        s = new String[i1];
        i1 = 0;
        l1 = ((flag) ? 1 : 0);
_L3:
        int k = i1;
        if(l1 >= ac.length) goto _L2; else goto _L1
_L1:
        int i2 = nextComma(ac, l1);
        int l;
        int k1;
        if(ac[l1] == '"' && ac[i2 - 1] == '"')
        {
            l = i2 - 1;
            l1++;
        } else
        {
            l = i2;
        }
        k1 = l1;
        int i;
        for(k = l1; k < l; k = i + 1)
        {
            i = k;
            if(ac[k] == '"')
                i = k + 1;
            if(i != k1)
                ac[k1] = ac[i];
            k1++;
        }

        s[i1] = new String(ac, l1, k1 - l1);
        i1++;
        l1 = i2 + 1;
        if(true) goto _L3; else goto _L2
_L2:
        for(; k < s.length; k++)
            s[k] = "";

        return s;
    }

    public void addColumn()
    {
        addColumn(null, 0);
    }

    public void addColumn(String s)
    {
        addColumn(s, 0);
    }

    public void addColumn(String s, int i)
    {
        insertColumn(columns.length, s, i);
    }

    public TableRow addRow()
    {
        setRowCount(rowCount + 1);
        return new RowPointer(this, rowCount - 1);
    }

    public TableRow addRow(TableRow tablerow)
    {
        int i;
        int j;
        i = rowCount;
        ensureBounds(i, tablerow.getColumnCount() - 1);
        j = 0;
_L8:
        if(j >= columns.length)
            break MISSING_BLOCK_LABEL_166;
        columnTypes[j];
        JVM INSTR tableswitch 0 5: default 72
    //                   0 150
    //                   1 83
    //                   2 102
    //                   3 118
    //                   4 134
    //                   5 83;
           goto _L1 _L2 _L3 _L4 _L5 _L6 _L3
_L2:
        break MISSING_BLOCK_LABEL_150;
_L4:
        break; /* Loop/switch isn't completed */
_L1:
        throw new RuntimeException("no types");
_L3:
        setInt(i, j, tablerow.getInt(j));
_L9:
        j++;
        if(true) goto _L8; else goto _L7
_L7:
        setLong(i, j, tablerow.getLong(j));
          goto _L9
_L5:
        setFloat(i, j, tablerow.getFloat(j));
          goto _L9
_L6:
        setDouble(i, j, tablerow.getDouble(j));
          goto _L9
        setString(i, j, tablerow.getString(j));
          goto _L9
        return new RowPointer(this, i);
    }

    public TableRow addRow(Object aobj[])
    {
        setRow(getRowCount(), aobj);
        return new RowPointer(this, rowCount - 1);
    }

    protected void checkBounds(int i, int j)
    {
        checkRow(i);
        checkColumn(j);
    }

    protected void checkColumn(int i)
    {
        if(i < 0 || i >= columns.length)
            throw new ArrayIndexOutOfBoundsException((new StringBuilder()).append("Column ").append(i).append(" does not exist.").toString());
        else
            return;
    }

    public int checkColumnIndex(String s)
    {
        int i = getColumnIndex(s, false);
        if(i == -1)
        {
            addColumn(s);
            i = getColumnCount() - 1;
        }
        return i;
    }

    protected void checkRow(int i)
    {
        if(i < 0 || i >= rowCount)
            throw new ArrayIndexOutOfBoundsException((new StringBuilder()).append("Row ").append(i).append(" does not exist.").toString());
        else
            return;
    }

    public void clearRows()
    {
        setRowCount(0);
    }

    protected void convertBasic(BufferedReader bufferedreader, boolean flag, File file)
        throws IOException
    {
        DataOutputStream dataoutputstream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file), 16384));
        dataoutputstream.writeInt(0);
        dataoutputstream.writeInt(getColumnCount());
        if(columnTitles != null)
        {
            dataoutputstream.writeBoolean(true);
            String as[] = columnTitles;
            int i = as.length;
            for(int k = 0; k < i; k++)
                dataoutputstream.writeUTF(as[k]);

        } else
        {
            dataoutputstream.writeBoolean(false);
        }
        int ai[] = columnTypes;
        int j = ai.length;
        for(int l = 0; l < j; l++)
            dataoutputstream.writeInt(ai[l]);

        j = 0;
        int i1 = -1;
        do
        {
            Object obj = bufferedreader.readLine();
            if(obj == null)
                break;
            if(flag)
                obj = PApplet.split(((String) (obj)), '\t');
            else
                obj = splitLineCSV(((String) (obj)));
            convertRow(dataoutputstream, ((String []) (obj)));
            if(++j % 10000 == 0 && j < rowCount)
            {
                int j1 = (j * 100) / rowCount;
                if(j1 != i1)
                {
                    System.out.println((new StringBuilder()).append(j1).append("%").toString());
                    i1 = j1;
                }
            }
        } while(true);
        bufferedreader = columnCategories;
        int k1 = bufferedreader.length;
        j = 0;
        i1 = 0;
        while(j < k1) 
        {
            HashMapBlows hashmapblows = bufferedreader[j];
            if(hashmapblows == null)
            {
                dataoutputstream.writeInt(0);
            } else
            {
                hashmapblows.write(dataoutputstream);
                hashmapblows.writeln(PApplet.createWriter(new File((new StringBuilder()).append(columnTitles[i1]).append(".categories").toString())));
            }
            i1++;
            j++;
        }
        dataoutputstream.flush();
        dataoutputstream.close();
        bufferedreader = new RandomAccessFile(file, "rw");
        bufferedreader.writeInt(rowCount);
        bufferedreader.close();
    }

    protected void convertRow(DataOutputStream dataoutputstream, String as[])
        throws IOException
    {
        int i;
        if(as.length > getColumnCount())
            throw new IllegalArgumentException((new StringBuilder()).append("Row with too many columns: ").append(PApplet.join(as, ",")).toString());
        i = 0;
_L9:
        if(i >= as.length)
            break MISSING_BLOCK_LABEL_217;
        columnTypes[i];
        JVM INSTR tableswitch 0 5: default 96
    //                   0 102
    //                   1 112
    //                   2 129
    //                   3 155
    //                   4 172
    //                   5 198;
           goto _L1 _L2 _L3 _L4 _L5 _L6 _L7
_L7:
        break MISSING_BLOCK_LABEL_198;
_L1:
        break; /* Loop/switch isn't completed */
_L2:
        break; /* Loop/switch isn't completed */
_L10:
        i++;
        if(true) goto _L9; else goto _L8
_L8:
        dataoutputstream.writeUTF(as[i]);
          goto _L10
_L3:
        dataoutputstream.writeInt(PApplet.parseInt(as[i], missingInt));
          goto _L10
_L4:
        try
        {
            dataoutputstream.writeLong(Long.parseLong(as[i]));
        }
        catch(NumberFormatException numberformatexception)
        {
            dataoutputstream.writeLong(missingLong);
        }
          goto _L10
_L5:
        dataoutputstream.writeFloat(PApplet.parseFloat(as[i], missingFloat));
          goto _L10
_L6:
        try
        {
            dataoutputstream.writeDouble(Double.parseDouble(as[i]));
        }
        catch(NumberFormatException numberformatexception1)
        {
            dataoutputstream.writeDouble(missingDouble);
        }
          goto _L10
        dataoutputstream.writeInt(columnCategories[i].index(as[i]));
          goto _L10
        i = as.length;
_L19:
        if(i >= getColumnCount())
            break MISSING_BLOCK_LABEL_343;
        columnTypes[i];
        JVM INSTR tableswitch 0 5: default 272
    //                   0 278
    //                   1 288
    //                   2 299
    //                   3 310
    //                   4 321
    //                   5 332;
           goto _L11 _L12 _L13 _L14 _L15 _L16 _L17
_L17:
        break MISSING_BLOCK_LABEL_332;
_L11:
        break; /* Loop/switch isn't completed */
_L12:
        break; /* Loop/switch isn't completed */
_L20:
        i++;
        if(true) goto _L19; else goto _L18
_L18:
        dataoutputstream.writeUTF("");
          goto _L20
_L13:
        dataoutputstream.writeInt(missingInt);
          goto _L20
_L14:
        dataoutputstream.writeLong(missingLong);
          goto _L20
_L15:
        dataoutputstream.writeFloat(missingFloat);
          goto _L20
_L16:
        dataoutputstream.writeDouble(missingDouble);
          goto _L20
        dataoutputstream.writeInt(missingCategory);
          goto _L20
    }

    protected Table createSubset(int ai[])
    {
        Table table;
        int i;
        table = new Table();
        table.setColumnTitles(columnTitles);
        table.columnTypes = columnTypes;
        table.setRowCount(ai.length);
        i = 0;
_L11:
        int j;
        int k;
        if(i >= ai.length)
            break; /* Loop/switch isn't completed */
        j = ai[i];
        k = 0;
_L8:
        if(k >= columns.length)
            break MISSING_BLOCK_LABEL_192;
        columnTypes[k];
        JVM INSTR tableswitch 0 4: default 96
    //                   0 102
    //                   1 120
    //                   2 138
    //                   3 156
    //                   4 174;
           goto _L1 _L2 _L3 _L4 _L5 _L6
_L6:
        break MISSING_BLOCK_LABEL_174;
_L1:
        break; /* Loop/switch isn't completed */
_L2:
        break; /* Loop/switch isn't completed */
_L9:
        k++;
        if(true) goto _L8; else goto _L7
_L7:
        table.setString(i, k, getString(j, k));
          goto _L9
_L3:
        table.setInt(i, k, getInt(j, k));
          goto _L9
_L4:
        table.setLong(i, k, getLong(j, k));
          goto _L9
_L5:
        table.setFloat(i, k, getFloat(j, k));
          goto _L9
        table.setDouble(i, k, getDouble(j, k));
          goto _L9
        i++;
        if(true) goto _L11; else goto _L10
_L10:
        return table;
    }

    protected void ensureBounds(int i, int j)
    {
        ensureRow(i);
        ensureColumn(j);
    }

    protected void ensureColumn(int i)
    {
        if(i >= columns.length)
            setColumnCount(i + 1);
    }

    protected void ensureRow(int i)
    {
        if(i >= rowCount)
            setRowCount(i + 1);
    }

    public TableRow findRow(String s, int i)
    {
        i = findRowIndex(s, i);
        if(i == -1)
            s = null;
        else
            s = new RowPointer(this, i);
        return s;
    }

    public TableRow findRow(String s, String s1)
    {
        return findRow(s, getColumnIndex(s1));
    }

    public int findRowIndex(String s, int i)
    {
        boolean flag;
        int j;
        boolean flag1;
        flag = false;
        j = 0;
        flag1 = false;
        checkColumn(i);
        if(columnTypes[i] != 0) goto _L2; else goto _L1
_L1:
        String as[];
        as = (String[])columns[i];
        i = ((flag) ? 1 : 0);
        if(s != null) goto _L4; else goto _L3
_L3:
        i = ((flag1) ? 1 : 0);
_L7:
        if(i >= rowCount)
            break MISSING_BLOCK_LABEL_148;
        if(as[i] != null) goto _L6; else goto _L5
_L5:
        return i;
_L6:
        i++;
          goto _L7
_L8:
        i++;
_L4:
        if(i >= rowCount)
            break MISSING_BLOCK_LABEL_148;
        if(as[i] == null || !as[i].equals(s)) goto _L8; else goto _L5
_L10:
        j++;
_L2:
        String s1;
        if(j >= rowCount)
            break MISSING_BLOCK_LABEL_148;
        s1 = getString(j, i);
        if(s1 != null)
            continue; /* Loop/switch isn't completed */
        if(s != null) goto _L10; else goto _L9
_L9:
        i = j;
          goto _L5
        if(!s1.equals(s)) goto _L10; else goto _L11
_L11:
        i = j;
          goto _L5
        i = -1;
          goto _L5
    }

    public int findRowIndex(String s, String s1)
    {
        return findRowIndex(s, getColumnIndex(s1));
    }

    public int[] findRowIndices(String s, int i)
    {
        int ai[] = new int[rowCount];
        checkColumn(i);
        int i1;
        if(columnTypes[i] == 0)
        {
            String as[] = (String[])columns[i];
            if(s == null)
            {
                int j = 0;
                i = 0;
                do
                {
                    i1 = i;
                    if(j >= rowCount)
                        break;
                    i1 = i;
                    if(as[j] == null)
                    {
                        ai[i] = j;
                        i1 = i + 1;
                    }
                    j++;
                    i = i1;
                } while(true);
            } else
            {
                int k = 0;
                i = 0;
                do
                {
                    i1 = i;
                    if(k >= rowCount)
                        break;
                    i1 = i;
                    if(as[k] != null)
                    {
                        i1 = i;
                        if(as[k].equals(s))
                        {
                            ai[i] = k;
                            i1 = i + 1;
                        }
                    }
                    k++;
                    i = i1;
                } while(true);
            }
        } else
        {
            int j1 = 0;
            int l = 0;
            while(j1 < rowCount) 
            {
                String s1 = getString(j1, i);
                if(s1 == null)
                {
                    i1 = l;
                    if(s == null)
                    {
                        ai[l] = j1;
                        i1 = l + 1;
                    }
                } else
                {
                    i1 = l;
                    if(s1.equals(s))
                    {
                        ai[l] = j1;
                        i1 = l + 1;
                    }
                }
                j1++;
                l = i1;
            }
            i1 = l;
        }
        return PApplet.subset(ai, 0, i1);
    }

    public int[] findRowIndices(String s, String s1)
    {
        return findRowIndices(s, getColumnIndex(s1));
    }

    public Iterator findRowIterator(String s, int i)
    {
        return new RowIndexIterator(this, findRowIndices(s, i));
    }

    public Iterator findRowIterator(String s, String s1)
    {
        return findRowIterator(s, getColumnIndex(s1));
    }

    public Iterable findRows(final String value, final int column)
    {
        return new Iterable() {

            public Iterator iterator()
            {
                return findRowIterator(value, column);
            }

            final Table this$0;
            final int val$column;
            final String val$value;

            
            {
                this$0 = Table.this;
                value = s;
                column = i;
                super();
            }
        }
;
    }

    public Iterable findRows(String s, String s1)
    {
        return findRows(s, getColumnIndex(s1));
    }

    public int getColumnCount()
    {
        return columns.length;
    }

    public int getColumnIndex(String s)
    {
        return getColumnIndex(s, true);
    }

    protected int getColumnIndex(String s, boolean flag)
    {
        int i;
        if(columnTitles == null)
        {
            if(flag)
                throw new IllegalArgumentException("This table has no header, so no column titles are set.");
            i = -1;
        } else
        {
            if(columnIndices == null)
            {
                columnIndices = new HashMap();
                for(i = 0; i < columns.length; i++)
                    columnIndices.put(columnTitles[i], Integer.valueOf(i));

            }
            Integer integer = (Integer)columnIndices.get(s);
            if(integer == null)
            {
                if(flag)
                    throw new IllegalArgumentException((new StringBuilder()).append("This table has no column named '").append(s).append("'").toString());
                i = -1;
            } else
            {
                i = integer.intValue();
            }
        }
        return i;
    }

    public String getColumnTitle(int i)
    {
        String s;
        if(columnTitles == null)
            s = null;
        else
            s = columnTitles[i];
        return s;
    }

    public String[] getColumnTitles()
    {
        return columnTitles;
    }

    public int getColumnType(int i)
    {
        return columnTypes[i];
    }

    public int getColumnType(String s)
    {
        return getColumnType(getColumnIndex(s));
    }

    public int[] getColumnTypes()
    {
        return columnTypes;
    }

    public double getDouble(int i, int j)
    {
        checkBounds(i, j);
        double d;
        if(columnTypes[j] == 4)
        {
            d = ((double[])columns[j])[i];
        } else
        {
            String s = getString(i, j);
            if(s == null || s.equals(missingString))
                d = missingDouble;
            else
                try
                {
                    d = Double.parseDouble(s);
                }
                catch(NumberFormatException numberformatexception)
                {
                    d = missingDouble;
                }
        }
        return d;
    }

    public double getDouble(int i, String s)
    {
        return getDouble(i, getColumnIndex(s));
    }

    public double[] getDoubleColumn(int i)
    {
        double ad[] = new double[rowCount];
        for(int j = 0; j < rowCount; j++)
            ad[j] = getDouble(j, i);

        return ad;
    }

    public double[] getDoubleColumn(String s)
    {
        int i = getColumnIndex(s);
        if(i == -1)
            s = null;
        else
            s = getDoubleColumn(i);
        return s;
    }

    public double[] getDoubleRow(int i)
    {
        double ad[] = new double[columns.length];
        for(int j = 0; j < columns.length; j++)
            ad[j] = getDouble(i, j);

        return ad;
    }

    public float getFloat(int i, int j)
    {
        checkBounds(i, j);
        float f;
        if(columnTypes[j] == 3)
        {
            f = ((float[])columns[j])[i];
        } else
        {
            String s = getString(i, j);
            if(s == null || s.equals(missingString))
                f = missingFloat;
            else
                f = PApplet.parseFloat(s, missingFloat);
        }
        return f;
    }

    public float getFloat(int i, String s)
    {
        return getFloat(i, getColumnIndex(s));
    }

    public float[] getFloatColumn(int i)
    {
        float af[] = new float[rowCount];
        for(int j = 0; j < rowCount; j++)
            af[j] = getFloat(j, i);

        return af;
    }

    public float[] getFloatColumn(String s)
    {
        int i = getColumnIndex(s);
        if(i == -1)
            s = null;
        else
            s = getFloatColumn(i);
        return s;
    }

    public FloatDict getFloatDict(int i, int j)
    {
        return new FloatDict(getStringColumn(i), getFloatColumn(j));
    }

    public FloatDict getFloatDict(String s, String s1)
    {
        return new FloatDict(getStringColumn(s), getFloatColumn(s1));
    }

    public FloatList getFloatList(int i)
    {
        return new FloatList(getFloatColumn(i));
    }

    public FloatList getFloatList(String s)
    {
        return new FloatList(getFloatColumn(s));
    }

    public float[] getFloatRow(int i)
    {
        float af[] = new float[columns.length];
        for(int j = 0; j < columns.length; j++)
            af[j] = getFloat(i, j);

        return af;
    }

    public int getInt(int i, int j)
    {
        checkBounds(i, j);
        if(columnTypes[j] == 1 || columnTypes[j] == 5)
        {
            i = ((int[])columns[j])[i];
        } else
        {
            String s = getString(i, j);
            if(s == null || s.equals(missingString))
                i = missingInt;
            else
                i = PApplet.parseInt(s, missingInt);
        }
        return i;
    }

    public int getInt(int i, String s)
    {
        return getInt(i, getColumnIndex(s));
    }

    public int[] getIntColumn(int i)
    {
        int ai[] = new int[rowCount];
        for(int j = 0; j < rowCount; j++)
            ai[j] = getInt(j, i);

        return ai;
    }

    public int[] getIntColumn(String s)
    {
        int i = getColumnIndex(s);
        if(i == -1)
            s = null;
        else
            s = getIntColumn(i);
        return s;
    }

    public IntDict getIntDict(int i, int j)
    {
        return new IntDict(getStringColumn(i), getIntColumn(j));
    }

    public IntDict getIntDict(String s, String s1)
    {
        return new IntDict(getStringColumn(s), getIntColumn(s1));
    }

    public IntList getIntList(int i)
    {
        return new IntList(getIntColumn(i));
    }

    public IntList getIntList(String s)
    {
        return new IntList(getIntColumn(s));
    }

    public int[] getIntRow(int i)
    {
        int ai[] = new int[columns.length];
        for(int j = 0; j < columns.length; j++)
            ai[j] = getInt(i, j);

        return ai;
    }

    public long getLong(int i, int j)
    {
        checkBounds(i, j);
        long l;
        if(columnTypes[j] == 2)
        {
            l = ((long[])columns[j])[i];
        } else
        {
            String s = getString(i, j);
            if(s == null || s.equals(missingString))
                l = missingLong;
            else
                try
                {
                    l = Long.parseLong(s);
                }
                catch(NumberFormatException numberformatexception)
                {
                    l = missingLong;
                }
        }
        return l;
    }

    public long getLong(int i, String s)
    {
        return getLong(i, getColumnIndex(s));
    }

    public long[] getLongColumn(int i)
    {
        long al[] = new long[rowCount];
        for(int j = 0; j < rowCount; j++)
            al[j] = getLong(j, i);

        return al;
    }

    public long[] getLongColumn(String s)
    {
        int i = getColumnIndex(s);
        if(i == -1)
            s = null;
        else
            s = getLongColumn(i);
        return s;
    }

    public long[] getLongRow(int i)
    {
        long al[] = new long[columns.length];
        for(int j = 0; j < columns.length; j++)
            al[j] = getLong(i, j);

        return al;
    }

    protected float getMaxFloat()
    {
        float f = -3.402823E+038F;
        boolean flag = false;
        int i = 0;
        do
        {
            if(i >= getRowCount())
                break;
            int j = 0;
            while(j < getColumnCount()) 
            {
                float f1 = getFloat(i, j);
                if(!Float.isNaN(f1))
                    if(!flag)
                    {
                        flag = true;
                        f = f1;
                    } else
                    if(f1 > f)
                        f = f1;
                j++;
            }
            i++;
        } while(true);
        if(!flag)
            f = missingFloat;
        return f;
    }

    public IntDict getOrder(int i)
    {
        return (new StringList(getStringColumn(i))).getOrder();
    }

    public IntDict getOrder(String s)
    {
        return getOrder(getColumnIndex(s));
    }

    public TableRow getRow(int i)
    {
        return new RowPointer(this, i);
    }

    public int getRowCount()
    {
        return rowCount;
    }

    public String getString(int i, int j)
    {
        checkBounds(i, j);
        String s;
        if(columnTypes[j] == 0)
            s = ((String[])columns[j])[i];
        else
        if(columnTypes[j] == 5)
        {
            i = getInt(i, j);
            if(i == missingCategory)
                s = missingString;
            else
                s = columnCategories[j].key(i);
        } else
        {
            s = String.valueOf(Array.get(columns[j], i));
        }
        return s;
    }

    public String getString(int i, String s)
    {
        return getString(i, getColumnIndex(s));
    }

    public String[] getStringColumn(int i)
    {
        String as[] = new String[rowCount];
        for(int j = 0; j < rowCount; j++)
            as[j] = getString(j, i);

        return as;
    }

    public String[] getStringColumn(String s)
    {
        int i = getColumnIndex(s);
        if(i == -1)
            s = null;
        else
            s = getStringColumn(i);
        return s;
    }

    public StringDict getStringDict(int i, int j)
    {
        return new StringDict(getStringColumn(i), getStringColumn(j));
    }

    public StringDict getStringDict(String s, String s1)
    {
        return new StringDict(getStringColumn(s), getStringColumn(s1));
    }

    public StringList getStringList(int i)
    {
        return new StringList(getStringColumn(i));
    }

    public StringList getStringList(String s)
    {
        return new StringList(getStringColumn(s));
    }

    public String[] getStringRow(int i)
    {
        String as[] = new String[columns.length];
        for(int j = 0; j < columns.length; j++)
            as[j] = getString(i, j);

        return as;
    }

    public IntDict getTally(int i)
    {
        return (new StringList(getStringColumn(i))).getTally();
    }

    public IntDict getTally(String s)
    {
        return getTally(getColumnIndex(s));
    }

    public String[] getUnique(int i)
    {
        return (new StringList(getStringColumn(i))).getUnique();
    }

    public String[] getUnique(String s)
    {
        return getUnique(getColumnIndex(s));
    }

    public boolean hasColumnTitles()
    {
        boolean flag;
        if(columnTitles != null)
            flag = true;
        else
            flag = false;
        return flag;
    }

    protected void init()
    {
        columns = new Object[0];
        columnTypes = new int[0];
        columnCategories = new HashMapBlows[0];
    }

    public void insertColumn(int i)
    {
        insertColumn(i, null, 0);
    }

    public void insertColumn(int i, String s)
    {
        insertColumn(i, s, 0);
    }

    public void insertColumn(int i, String s, int j)
    {
        if(s != null && columnTitles == null)
            columnTitles = new String[columns.length];
        if(columnTitles != null)
        {
            columnTitles = PApplet.splice(columnTitles, s, i);
            columnIndices = null;
        }
        columnTypes = PApplet.splice(columnTypes, j, i);
        s = new HashMapBlows[columns.length + 1];
        for(int k = 0; k < i; k++)
            s[k] = columnCategories[k];

        s[i] = new HashMapBlows();
        for(int l = i; l < columns.length; l++)
            s[l + 1] = columnCategories[l];

        columnCategories = s;
        s = ((String) (new Object[columns.length + 1]));
        System.arraycopy(((Object) (columns)), 0, s, 0, i);
        System.arraycopy(((Object) (columns)), i, s, i + 1, columns.length - i);
        columns = s;
        j;
        JVM INSTR tableswitch 0 5: default 232
    //                   0 293
    //                   1 233
    //                   2 248
    //                   3 263
    //                   4 278
    //                   5 309;
           goto _L1 _L2 _L3 _L4 _L5 _L6 _L7
_L1:
        return;
_L3:
        columns[i] = new int[rowCount];
        continue; /* Loop/switch isn't completed */
_L4:
        columns[i] = new long[rowCount];
        continue; /* Loop/switch isn't completed */
_L5:
        columns[i] = new float[rowCount];
        continue; /* Loop/switch isn't completed */
_L6:
        columns[i] = new double[rowCount];
        continue; /* Loop/switch isn't completed */
_L2:
        columns[i] = new String[rowCount];
        continue; /* Loop/switch isn't completed */
_L7:
        columns[i] = new int[rowCount];
        if(true) goto _L1; else goto _L8
_L8:
    }

    public void insertRow(int i, Object aobj[])
    {
        int j = 0;
_L8:
        if(j >= columns.length)
            break MISSING_BLOCK_LABEL_343;
        columnTypes[j];
        JVM INSTR tableswitch 0 5: default 56
    //                   0 286
    //                   1 62
    //                   2 118
    //                   3 174
    //                   4 230
    //                   5 62;
           goto _L1 _L2 _L3 _L4 _L5 _L6 _L3
_L2:
        break MISSING_BLOCK_LABEL_286;
_L1:
        break; /* Loop/switch isn't completed */
_L3:
        break; /* Loop/switch isn't completed */
_L9:
        j++;
        if(true) goto _L8; else goto _L7
_L7:
        int ai[] = new int[rowCount + 1];
        System.arraycopy(columns[j], 0, ai, 0, i);
        System.arraycopy(columns[j], i, ai, i + 1, rowCount - i);
        columns[j] = ai;
          goto _L9
_L4:
        long al[] = new long[rowCount + 1];
        System.arraycopy(columns[j], 0, al, 0, i);
        System.arraycopy(columns[j], i, al, i + 1, rowCount - i);
        columns[j] = al;
          goto _L9
_L5:
        float af[] = new float[rowCount + 1];
        System.arraycopy(columns[j], 0, af, 0, i);
        System.arraycopy(columns[j], i, af, i + 1, rowCount - i);
        columns[j] = af;
          goto _L9
_L6:
        double ad[] = new double[rowCount + 1];
        System.arraycopy(columns[j], 0, ad, 0, i);
        System.arraycopy(columns[j], i, ad, i + 1, rowCount - i);
        columns[j] = ad;
          goto _L9
        String as[] = new String[rowCount + 1];
        System.arraycopy(columns[j], 0, as, 0, i);
        System.arraycopy(columns[j], i, as, i + 1, rowCount - i);
        columns[j] = as;
          goto _L9
        setRow(i, aobj);
        rowCount = rowCount + 1;
        return;
    }

    public int lastRowIndex()
    {
        return getRowCount() - 1;
    }

    protected void loadBinary(InputStream inputstream)
        throws IOException
    {
        DataInputStream datainputstream;
        int i;
        int j;
        int k;
        datainputstream = new DataInputStream(new BufferedInputStream(inputstream));
        i = datainputstream.readInt();
        if(i != 0x9007ab1e)
            throw new IOException((new StringBuilder()).append("Not a compatible binary table (magic was ").append(PApplet.hex(i)).append(")").toString());
        j = datainputstream.readInt();
        setRowCount(j);
        k = datainputstream.readInt();
        setColumnCount(k);
        if(datainputstream.readBoolean())
        {
            columnTitles = new String[getColumnCount()];
            for(i = 0; i < k; i++)
                setColumnTitle(i, datainputstream.readUTF());

        }
        i = 0;
_L9:
        int l;
        if(i >= k)
            break MISSING_BLOCK_LABEL_303;
        l = datainputstream.readInt();
        columnTypes[i] = l;
        l;
        JVM INSTR tableswitch 0 5: default 192
    //                   0 276
    //                   1 221
    //                   2 237
    //                   3 250
    //                   4 263
    //                   5 290;
           goto _L1 _L2 _L3 _L4 _L5 _L6 _L7
_L7:
        break MISSING_BLOCK_LABEL_290;
_L4:
        break; /* Loop/switch isn't completed */
_L1:
        throw new IllegalArgumentException((new StringBuilder()).append(l).append(" is not a valid column type.").toString());
_L3:
        columns[i] = new int[j];
_L10:
        i++;
        if(true) goto _L9; else goto _L8
_L8:
        columns[i] = new long[j];
          goto _L10
_L5:
        columns[i] = new float[j];
          goto _L10
_L6:
        columns[i] = new double[j];
          goto _L10
_L2:
        columns[i] = new String[j];
          goto _L10
        columns[i] = new int[j];
          goto _L10
        for(i = 0; i < k; i++)
            if(columnTypes[i] == 5)
                columnCategories[i] = new HashMapBlows(datainputstream);

        if(datainputstream.readBoolean())
            missingString = datainputstream.readUTF();
        else
            missingString = null;
        missingInt = datainputstream.readInt();
        missingLong = datainputstream.readLong();
        missingFloat = datainputstream.readFloat();
        missingDouble = datainputstream.readDouble();
        missingCategory = datainputstream.readInt();
        i = 0;
_L22:
        if(i >= j)
            break; /* Loop/switch isn't completed */
        l = 0;
_L19:
        if(l >= k)
            break MISSING_BLOCK_LABEL_567;
        columnTypes[l];
        JVM INSTR tableswitch 0 5: default 460
    //                   0 474
    //                   1 497
    //                   2 511
    //                   3 525
    //                   4 539
    //                   5 553;
           goto _L11 _L12 _L13 _L14 _L15 _L16 _L17
_L17:
        break MISSING_BLOCK_LABEL_553;
_L11:
        break; /* Loop/switch isn't completed */
_L12:
        break; /* Loop/switch isn't completed */
_L20:
        l++;
        if(true) goto _L19; else goto _L18
_L18:
        if(datainputstream.readBoolean())
            inputstream = datainputstream.readUTF();
        else
            inputstream = null;
        setString(i, l, inputstream);
          goto _L20
_L13:
        setInt(i, l, datainputstream.readInt());
          goto _L20
_L14:
        setLong(i, l, datainputstream.readLong());
          goto _L20
_L15:
        setFloat(i, l, datainputstream.readFloat());
          goto _L20
_L16:
        setDouble(i, l, datainputstream.readDouble());
          goto _L20
        setInt(i, l, datainputstream.readInt());
          goto _L20
        i++;
        if(true) goto _L22; else goto _L21
_L21:
        datainputstream.close();
        return;
    }

    public TableRow matchRow(String s, int i)
    {
        i = matchRowIndex(s, i);
        if(i == -1)
            s = null;
        else
            s = new RowPointer(this, i);
        return s;
    }

    public TableRow matchRow(String s, String s1)
    {
        return matchRow(s, getColumnIndex(s1));
    }

    public int matchRowIndex(String s, int i)
    {
        int j;
        boolean flag;
        j = 0;
        flag = false;
        checkColumn(i);
        if(columnTypes[i] != 0) goto _L2; else goto _L1
_L1:
        String as[];
        as = (String[])columns[i];
        j = ((flag) ? 1 : 0);
_L7:
        if(j >= rowCount) goto _L4; else goto _L3
_L3:
        if(as[j] == null || PApplet.match(as[j], s) == null) goto _L6; else goto _L5
_L5:
        return j;
_L6:
        j++;
          goto _L7
_L9:
        j++;
_L2:
        if(j >= rowCount) goto _L4; else goto _L8
_L8:
        String s1 = getString(j, i);
        if(s1 == null || PApplet.match(s1, s) == null) goto _L9; else goto _L5
_L4:
        j = -1;
          goto _L5
    }

    public int matchRowIndex(String s, String s1)
    {
        return matchRowIndex(s, getColumnIndex(s1));
    }

    public int[] matchRowIndices(String s, int i)
    {
        int ai[] = new int[rowCount];
        checkColumn(i);
        int l;
        if(columnTypes[i] == 0)
        {
            String as[] = (String[])columns[i];
            int j = 0;
            i = 0;
            do
            {
                l = i;
                if(j >= rowCount)
                    break;
                l = i;
                if(as[j] != null)
                {
                    l = i;
                    if(PApplet.match(as[j], s) != null)
                    {
                        ai[i] = j;
                        l = i + 1;
                    }
                }
                j++;
                i = l;
            } while(true);
        } else
        {
            l = 0;
            int k;
            int i1;
            for(k = 0; l < rowCount; k = i1)
            {
                String s1 = getString(l, i);
                i1 = k;
                if(s1 != null)
                {
                    i1 = k;
                    if(PApplet.match(s1, s) != null)
                    {
                        ai[k] = l;
                        i1 = k + 1;
                    }
                }
                l++;
            }

            l = k;
        }
        return PApplet.subset(ai, 0, l);
    }

    public int[] matchRowIndices(String s, String s1)
    {
        return matchRowIndices(s, getColumnIndex(s1));
    }

    public Iterator matchRowIterator(String s, int i)
    {
        return new RowIndexIterator(this, matchRowIndices(s, i));
    }

    public Iterator matchRowIterator(String s, String s1)
    {
        return matchRowIterator(s, getColumnIndex(s1));
    }

    public Iterable matchRows(final String regexp, final int column)
    {
        return new Iterable() {

            public Iterator iterator()
            {
                return matchRowIterator(regexp, column);
            }

            final Table this$0;
            final int val$column;
            final String val$regexp;

            
            {
                this$0 = Table.this;
                regexp = s;
                column = i;
                super();
            }
        }
;
    }

    public Iterable matchRows(String s, String s1)
    {
        return matchRows(s, getColumnIndex(s1));
    }

    protected void odsParse(InputStream inputstream, String s)
    {
        boolean flag = false;
        XML axml[];
        int i;
        inputstream = odsFindContentXML(inputstream);
        XML xml = JVM INSTR new #291 <Class XML>;
        xml.XML(inputstream);
        axml = xml.getChildren("office:body/office:spreadsheet/table:table");
        i = axml.length;
        int j = 0;
_L2:
        boolean flag1;
        flag1 = flag;
        if(j >= i)
            break MISSING_BLOCK_LABEL_85;
        inputstream = axml[j];
        if(s == null)
            break MISSING_BLOCK_LABEL_71;
        if(!s.equals(inputstream.getString("table:name")))
            break MISSING_BLOCK_LABEL_113;
        odsParseSheet(inputstream);
        flag = true;
        flag1 = true;
        if(s != null)
            break MISSING_BLOCK_LABEL_113;
        if(!flag1)
        {
            if(s != null)
                break; /* Loop/switch isn't completed */
            StringBuilder stringbuilder;
            try
            {
                inputstream = JVM INSTR new #208 <Class RuntimeException>;
                inputstream.RuntimeException("No worksheets found in the ODS file.");
                throw inputstream;
            }
            // Misplaced declaration of an exception variable
            catch(InputStream inputstream)
            {
                inputstream.printStackTrace();
            }
            // Misplaced declaration of an exception variable
            catch(InputStream inputstream)
            {
                inputstream.printStackTrace();
            }
            // Misplaced declaration of an exception variable
            catch(InputStream inputstream)
            {
                inputstream.printStackTrace();
            }
            // Misplaced declaration of an exception variable
            catch(InputStream inputstream)
            {
                inputstream.printStackTrace();
            }
        }
        return;
        j++;
        if(true) goto _L2; else goto _L1
_L1:
        inputstream = JVM INSTR new #208 <Class RuntimeException>;
        stringbuilder = JVM INSTR new #219 <Class StringBuilder>;
        stringbuilder.StringBuilder();
        inputstream.RuntimeException(stringbuilder.append("No worksheet named ").append(s).append(" found in the ODS file.").toString());
        throw inputstream;
    }

    protected void parse(InputStream inputstream, String s)
        throws IOException
    {
        String s1;
        String s2;
        String as[];
        int i;
        int j;
        boolean flag;
        boolean flag1;
        boolean flag2;
        s1 = null;
        s2 = null;
        if(s == null)
            break MISSING_BLOCK_LABEL_249;
        as = PApplet.splitTokens(s, " ,");
        i = as.length;
        j = 0;
        flag = false;
        s = null;
        flag1 = false;
        flag2 = false;
_L1:
        boolean flag3;
        String s3;
        boolean flag4;
        boolean flag5;
        s1 = s2;
        flag3 = flag;
        s3 = s;
        flag4 = flag1;
        flag5 = flag2;
        if(j >= i)
            break MISSING_BLOCK_LABEL_261;
        s3 = as[j];
        if(s3.equals("tsv"))
            s = "tsv";
        else
        if(s3.equals("csv"))
            s = "csv";
        else
        if(s3.equals("ods"))
            s = "ods";
        else
        if(s3.equals("newlines"))
        {
            s = "csv";
            flag2 = true;
        } else
        if(s3.equals("bin"))
        {
            s = "bin";
            flag = true;
        } else
        if(s3.equals("header"))
        {
            flag1 = true;
        } else
        {
            if(!s3.startsWith("worksheet="))
                continue; /* Loop/switch isn't completed */
            s2 = s3.substring("worksheet=".length());
        }
_L3:
        j++;
          goto _L1
        if(s3.startsWith("dictionary=")) goto _L3; else goto _L2
_L2:
        throw new IllegalArgumentException((new StringBuilder()).append("'").append(s3).append("' is not a valid option for loading a Table").toString());
        flag3 = false;
        s3 = null;
        flag4 = false;
        flag5 = false;
        if(s3 == null)
            throw new IllegalArgumentException("No extension specified for this Table");
        if(!flag3) goto _L5; else goto _L4
_L4:
        loadBinary(inputstream);
_L7:
        return;
_L5:
        if(s3.equals("ods"))
        {
            odsParse(inputstream, s1);
        } else
        {
            inputstream = PApplet.createReader(inputstream);
            if(flag5)
                parseAwfulCSV(inputstream, flag4);
            else
            if("tsv".equals(s3))
                parseBasic(inputstream, flag4, true);
            else
            if("csv".equals(s3))
                parseBasic(inputstream, flag4, false);
        }
        if(true) goto _L7; else goto _L6
_L6:
    }

    protected void parseAwfulCSV(BufferedReader bufferedreader, boolean flag)
        throws IOException
    {
        char ac[] = new char[100];
        int i = 0;
        boolean flag1 = false;
        int j = 0;
        int k = 0;
        boolean flag2 = flag;
        do
        {
            int l = bufferedreader.read();
            if(l == -1)
                break;
            if(flag1)
            {
                if(l == 34)
                {
                    bufferedreader.mark(1);
                    if(bufferedreader.read() == 34)
                    {
                        if(j == ac.length)
                            ac = PApplet.expand(ac);
                        ac[j] = (char)34;
                        j++;
                    } else
                    {
                        bufferedreader.reset();
                        flag1 = false;
                    }
                } else
                {
                    if(j == ac.length)
                        ac = PApplet.expand(ac);
                    ac[j] = (char)l;
                    j++;
                }
            } else
            if(l == 34)
                flag1 = true;
            else
            if(l == 13 || l == 10)
            {
                if(l == 13)
                {
                    bufferedreader.mark(1);
                    if(bufferedreader.read() != 10)
                        bufferedreader.reset();
                }
                setString(i, k, new String(ac, 0, j));
                flag = flag2;
                if(i == 0)
                {
                    flag = flag2;
                    if(flag2)
                    {
                        removeTitleRow();
                        flag = false;
                    }
                }
                i++;
                j = 0;
                k = 0;
                flag2 = flag;
            } else
            if(l == 44)
            {
                setString(i, k, new String(ac, 0, j));
                k++;
                ensureColumn(k);
                j = 0;
            } else
            {
                if(j == ac.length)
                    ac = PApplet.expand(ac);
                ac[j] = (char)l;
                j++;
            }
        } while(true);
        if(j > 0)
            setString(i, k, new String(ac, 0, j));
    }

    protected void parseBasic(BufferedReader bufferedreader, boolean flag, boolean flag1)
        throws IOException
    {
        int i;
        if(rowCount == 0)
            setRowCount(10);
        i = 0;
_L6:
        int j;
        Object obj;
        boolean flag2;
        int k;
        j = i;
        try
        {
            obj = bufferedreader.readLine();
        }
        // Misplaced declaration of an exception variable
        catch(BufferedReader bufferedreader)
        {
            throw new RuntimeException((new StringBuilder()).append("Error reading table on line ").append(j).toString(), bufferedreader);
        }
        if(obj == null)
            break MISSING_BLOCK_LABEL_256;
        j = i;
        if(i != getRowCount())
            break MISSING_BLOCK_LABEL_56;
        j = i;
        setRowCount(i << 1);
        if(i != 0 || !flag) goto _L2; else goto _L1
_L1:
        if(!flag1) goto _L4; else goto _L3
_L3:
        j = i;
        obj = PApplet.split(((String) (obj)), '\t');
_L7:
        j = i;
        setColumnTitles(((String []) (obj)));
        flag2 = false;
        k = i;
_L10:
        i = k;
        flag = flag2;
        if(k % 10000 != 0) goto _L6; else goto _L5
_L5:
        j = k;
        Thread.sleep(10L);
        i = k;
        flag = flag2;
          goto _L6
        obj;
        j = k;
        ((InterruptedException) (obj)).printStackTrace();
        i = k;
        flag = flag2;
          goto _L6
_L4:
        j = i;
        obj = splitLineCSV(((String) (obj)));
          goto _L7
_L2:
        if(!flag1) goto _L9; else goto _L8
_L8:
        j = i;
        obj = PApplet.split(((String) (obj)), '\t');
_L11:
        j = i;
        setRow(i, ((Object []) (obj)));
        k = i + 1;
        flag2 = flag;
          goto _L10
_L9:
        j = i;
        obj = splitLineCSV(((String) (obj)));
          goto _L11
        if(i != getRowCount())
            setRowCount(i);
        return;
          goto _L7
    }

    public void parseInto(Object obj, String s)
    {
        Field afield[];
        Object obj2;
        afield = null;
        obj2 = null;
        s = obj.getClass().getDeclaredField(s);
        Object obj3;
        boolean flag;
        obj3 = s.getType();
        flag = ((Class) (obj3)).isArray();
        if(flag) goto _L2; else goto _L1
_L1:
        obj3 = s;
_L6:
        Class class1 = afield.getEnclosingClass();
        if(class1 != null) goto _L4; else goto _L3
_L3:
        s = afield.getDeclaredConstructor(new Class[0]);
_L7:
        if(!s.isAccessible())
            s.setAccessible(true);
_L8:
        Object obj4;
        afield = afield.getDeclaredFields();
        obj4 = new ArrayList();
        int i = afield.length;
        for(int j = 0; j < i; j++)
        {
            Field field = afield[j];
            if(getColumnIndex(field.getName(), false) != -1)
            {
                if(!field.isAccessible())
                    field.setAccessible(true);
                ((ArrayList) (obj4)).add(field);
            }
        }

          goto _L5
_L2:
        obj3 = ((Class) (obj3)).getComponentType();
        obj2 = Array.newInstance(((Class) (obj3)), getRowCount());
        afield = ((Field []) (obj3));
          goto _L1
        obj2;
        obj3 = null;
        s = null;
_L27:
        ((NoSuchFieldException) (obj2)).printStackTrace();
        obj2 = null;
        afield = ((Field []) (obj3));
        obj3 = s;
          goto _L6
        obj2;
        obj3 = null;
        s = null;
_L26:
        ((SecurityException) (obj2)).printStackTrace();
        obj2 = null;
        afield = ((Field []) (obj3));
        obj3 = s;
          goto _L6
_L4:
        s = afield.getDeclaredConstructor(new Class[] {
            class1
        });
          goto _L7
        obj4;
        s = null;
_L25:
        ((SecurityException) (obj4)).printStackTrace();
          goto _L8
        obj4;
        s = null;
_L24:
        ((NoSuchMethodException) (obj4)).printStackTrace();
          goto _L8
_L5:
        Iterator iterator = rows().iterator();
        int k = 0;
_L23:
        TableRow tablerow;
        if(!iterator.hasNext())
            break MISSING_BLOCK_LABEL_658;
        tablerow = (TableRow)iterator.next();
        if(class1 != null) goto _L10; else goto _L9
_L9:
        Object obj1 = s.newInstance(new Object[0]);
_L14:
        Iterator iterator1 = ((ArrayList) (obj4)).iterator();
_L13:
        Field field1;
        String s1;
        if(!iterator1.hasNext())
            break MISSING_BLOCK_LABEL_644;
        field1 = (Field)iterator1.next();
        s1 = field1.getName();
        if(field1.getType() != java/lang/String) goto _L12; else goto _L11
_L11:
        field1.set(obj1, tablerow.getString(s1));
          goto _L13
        try
        {
            if(!((Field) (obj3)).isAccessible())
                ((Field) (obj3)).setAccessible(true);
            ((Field) (obj3)).set(obj, obj2);
        }
        // Misplaced declaration of an exception variable
        catch(Object obj)
        {
            ((InstantiationException) (obj)).printStackTrace();
        }
        // Misplaced declaration of an exception variable
        catch(Object obj)
        {
            ((IllegalAccessException) (obj)).printStackTrace();
        }
        // Misplaced declaration of an exception variable
        catch(Object obj)
        {
            ((IllegalArgumentException) (obj)).printStackTrace();
        }
        // Misplaced declaration of an exception variable
        catch(Object obj)
        {
            ((InvocationTargetException) (obj)).printStackTrace();
        }
        return;
_L10:
        obj1 = s.newInstance(new Object[] {
            obj
        });
          goto _L14
_L12:
label0:
        {
            if(field1.getType() != Integer.TYPE)
                break label0;
            field1.setInt(obj1, tablerow.getInt(s1));
        }
          goto _L13
label1:
        {
            if(field1.getType() != Long.TYPE)
                break label1;
            field1.setLong(obj1, tablerow.getLong(s1));
        }
          goto _L13
label2:
        {
            if(field1.getType() != Float.TYPE)
                break label2;
            field1.setFloat(obj1, tablerow.getFloat(s1));
        }
          goto _L13
label3:
        {
            if(field1.getType() != Double.TYPE)
                break label3;
            field1.setDouble(obj1, tablerow.getDouble(s1));
        }
          goto _L13
        if(field1.getType() != Boolean.TYPE) goto _L16; else goto _L15
_L15:
        s1 = tablerow.getString(s1);
        if(s1 == null) goto _L13; else goto _L17
_L17:
        if(!s1.toLowerCase().equals("true") && !s1.equals("1")) goto _L13; else goto _L18
_L18:
        field1.setBoolean(obj1, true);
          goto _L13
_L16:
        if(field1.getType() != Character.TYPE) goto _L13; else goto _L19
_L19:
        s1 = tablerow.getString(s1);
        if(s1 == null) goto _L13; else goto _L20
_L20:
        if(s1.length() <= 0) goto _L13; else goto _L21
_L21:
        field1.setChar(obj1, s1.charAt(0));
          goto _L13
        Array.set(obj2, k, obj1);
        k++;
        if(true) goto _L23; else goto _L22
_L22:
        obj4;
          goto _L24
        obj4;
          goto _L25
        obj2;
        obj3 = null;
          goto _L26
        obj2;
          goto _L26
        obj2;
        obj3 = null;
          goto _L27
        obj2;
          goto _L27
    }

    public void removeColumn(int i)
    {
        int j = 0;
        int k = columns.length - 1;
        Object aobj[] = new Object[k];
        HashMapBlows ahashmapblows[] = new HashMapBlows[k];
        for(int l = 0; l < i; l++)
        {
            aobj[l] = columns[l];
            ahashmapblows[l] = columnCategories[l];
        }

        for(int i1 = i; i1 < k; i1++)
        {
            aobj[i1] = columns[i1 + 1];
            ahashmapblows[i1] = columnCategories[i1 + 1];
        }

        columns = aobj;
        columnCategories = ahashmapblows;
        if(columnTitles != null)
        {
            String as[] = new String[k];
            int j1;
            do
            {
                j1 = i;
                if(j >= i)
                    break;
                as[j] = columnTitles[j];
                j++;
            } while(true);
            for(; j1 < k; j1++)
                as[j1] = columnTitles[j1 + 1];

            columnTitles = as;
            columnIndices = null;
        }
    }

    public void removeColumn(String s)
    {
        removeColumn(getColumnIndex(s));
    }

    public void removeRow(int i)
    {
        int j = 0;
_L8:
        if(j >= columns.length)
            break MISSING_BLOCK_LABEL_333;
        columnTypes[j];
        JVM INSTR tableswitch 0 5: default 56
    //                   0 278
    //                   1 62
    //                   2 116
    //                   3 170
    //                   4 224
    //                   5 62;
           goto _L1 _L2 _L3 _L4 _L5 _L6 _L3
_L2:
        break MISSING_BLOCK_LABEL_278;
_L1:
        break; /* Loop/switch isn't completed */
_L3:
        break; /* Loop/switch isn't completed */
_L9:
        j++;
        if(true) goto _L8; else goto _L7
_L7:
        int ai[] = new int[rowCount - 1];
        System.arraycopy(columns[j], 0, ai, 0, i);
        System.arraycopy(columns[j], i + 1, ai, i, rowCount - i - 1);
        columns[j] = ai;
          goto _L9
_L4:
        long al[] = new long[rowCount - 1];
        System.arraycopy(columns[j], 0, al, 0, i);
        System.arraycopy(columns[j], i + 1, al, i, rowCount - i - 1);
        columns[j] = al;
          goto _L9
_L5:
        float af[] = new float[rowCount - 1];
        System.arraycopy(columns[j], 0, af, 0, i);
        System.arraycopy(columns[j], i + 1, af, i, rowCount - i - 1);
        columns[j] = af;
          goto _L9
_L6:
        double ad[] = new double[rowCount - 1];
        System.arraycopy(columns[j], 0, ad, 0, i);
        System.arraycopy(columns[j], i + 1, ad, i, rowCount - i - 1);
        columns[j] = ad;
          goto _L9
        String as[] = new String[rowCount - 1];
        System.arraycopy(columns[j], 0, as, 0, i);
        System.arraycopy(columns[j], i + 1, as, i, rowCount - i - 1);
        columns[j] = as;
          goto _L9
        rowCount = rowCount - 1;
        return;
    }

    public String[] removeTitleRow()
    {
        String as[] = getStringRow(0);
        removeRow(0);
        setColumnTitles(as);
        return as;
    }

    public void removeTokens(String s)
    {
        for(int i = 0; i < getColumnCount(); i++)
            removeTokens(s, i);

    }

    public void removeTokens(String s, int i)
    {
        for(int j = 0; j < rowCount; j++)
        {
            String s1 = getString(j, i);
            if(s1 == null)
                continue;
            char ac[] = s1.toCharArray();
            int k = 0;
            int l;
            int i1;
            for(l = 0; k < ac.length; l = i1)
            {
                i1 = l;
                if(s.indexOf(ac[k]) == -1)
                {
                    if(l != k)
                        ac[l] = ac[k];
                    i1 = l + 1;
                }
                k++;
            }

            if(l != ac.length)
                setString(j, i, new String(ac, 0, l));
        }

    }

    public void removeTokens(String s, String s1)
    {
        removeTokens(s, getColumnIndex(s1));
    }

    public void replace(String s, String s1)
    {
        for(int i = 0; i < columns.length; i++)
            replace(s, s1, i);

    }

    public void replace(String s, String s1, int i)
    {
        if(columnTypes[i] == 0)
        {
            String as[] = (String[])columns[i];
            for(i = 0; i < rowCount; i++)
                if(as[i].equals(s))
                    as[i] = s1;

        }
    }

    public void replace(String s, String s1, String s2)
    {
        replace(s, s1, getColumnIndex(s2));
    }

    public void replaceAll(String s, String s1)
    {
        for(int i = 0; i < columns.length; i++)
            replaceAll(s, s1, i);

    }

    public void replaceAll(String s, String s1, int i)
    {
        checkColumn(i);
        if(columnTypes[i] == 0)
        {
            String as[] = (String[])columns[i];
            for(i = 0; i < rowCount; i++)
                if(as[i] != null)
                    as[i] = as[i].replaceAll(s, s1);

        } else
        {
            throw new IllegalArgumentException("replaceAll() can only be used on String columns");
        }
    }

    public void replaceAll(String s, String s1, String s2)
    {
        replaceAll(s, s1, getColumnIndex(s2));
    }

    public Iterable rows()
    {
        return new Iterable() {

            public Iterator iterator()
            {
                if(rowIterator == null)
                    rowIterator = new RowIterator(Table.this);
                else
                    rowIterator.reset();
                return rowIterator;
            }

            final Table this$0;

            
            {
                this$0 = Table.this;
                super();
            }
        }
;
    }

    public Iterable rows(final int indices[])
    {
        return new Iterable() {

            public Iterator iterator()
            {
                return new RowIndexIterator(Table.this, indices);
            }

            final Table this$0;
            final int val$indices[];

            
            {
                this$0 = Table.this;
                indices = ai;
                super();
            }
        }
;
    }

    public boolean save(File file, String s)
        throws IOException
    {
        return save(PApplet.createOutput(file), extensionOptions(false, file.getName(), s));
    }

    public boolean save(OutputStream outputstream, String s)
    {
        boolean flag;
        PrintWriter printwriter;
        int j;
        flag = false;
        printwriter = PApplet.createWriter(outputstream);
        if(s == null)
            throw new IllegalArgumentException("No extension specified for saving this Table");
        s = PApplet.splitTokens(s, ", ");
        s = s[s.length - 1];
        String as[] = saveExtensions;
        int i = as.length;
        j = 0;
        do
        {
            if(j >= i)
                break MISSING_BLOCK_LABEL_208;
            if(s.equals(as[j]))
            {
                j = 1;
                break MISSING_BLOCK_LABEL_73;
            }
            j++;
        } while(true);
_L7:
        if(j == 0)
            throw new IllegalArgumentException((new StringBuilder()).append("'").append(s).append("' not available for Table").toString());
        if(s.equals("csv"))
        {
            writeCSV(printwriter);
        } else
        {
label0:
            {
                if(!s.equals("tsv"))
                    break label0;
                writeTSV(printwriter);
            }
        }
_L3:
        printwriter.flush();
        printwriter.close();
        flag = true;
_L5:
        return flag;
        if(!s.equals("html")) goto _L2; else goto _L1
_L1:
        writeHTML(printwriter);
          goto _L3
_L2:
        if(!s.equals("bin")) goto _L3; else goto _L4
_L4:
        saveBinary(outputstream);
          goto _L3
        outputstream;
        outputstream.printStackTrace();
          goto _L5
        j = 0;
        if(true) goto _L7; else goto _L6
_L6:
    }

    protected void saveBinary(OutputStream outputstream)
        throws IOException
    {
        int i1;
        TableRow tablerow;
        outputstream = new DataOutputStream(new BufferedOutputStream(outputstream));
        outputstream.writeInt(0x9007ab1e);
        outputstream.writeInt(getRowCount());
        outputstream.writeInt(getColumnCount());
        if(columnTitles != null)
        {
            outputstream.writeBoolean(true);
            String as[] = columnTitles;
            int i = as.length;
            for(int j = 0; j < i; j++)
                outputstream.writeUTF(as[j]);

        } else
        {
            outputstream.writeBoolean(false);
        }
        for(int k = 0; k < getColumnCount(); k++)
            outputstream.writeInt(columnTypes[k]);

        for(int l = 0; l < getColumnCount(); l++)
            if(columnTypes[l] == 5)
                columnCategories[l].write(outputstream);

        Iterator iterator;
        if(missingString == null)
        {
            outputstream.writeBoolean(false);
        } else
        {
            outputstream.writeBoolean(true);
            outputstream.writeUTF(missingString);
        }
        outputstream.writeInt(missingInt);
        outputstream.writeLong(missingLong);
        outputstream.writeFloat(missingFloat);
        outputstream.writeDouble(missingDouble);
        outputstream.writeInt(missingCategory);
        iterator = rows().iterator();
        if(!iterator.hasNext())
            break MISSING_BLOCK_LABEL_446;
        tablerow = (TableRow)iterator.next();
        i1 = 0;
_L9:
        if(i1 >= getColumnCount())
            break MISSING_BLOCK_LABEL_218;
        columnTypes[i1];
        JVM INSTR tableswitch 0 5: default 296
    //                   0 318
    //                   1 356
    //                   2 372
    //                   3 388
    //                   4 404
    //                   5 420;
           goto _L1 _L2 _L3 _L4 _L5 _L6 _L7
_L2:
        break; /* Loop/switch isn't completed */
_L1:
        break; /* Loop/switch isn't completed */
_L7:
        break MISSING_BLOCK_LABEL_420;
_L10:
        i1++;
        if(true) goto _L9; else goto _L8
_L8:
        String s = tablerow.getString(i1);
        if(s == null)
        {
            outputstream.writeBoolean(false);
        } else
        {
            outputstream.writeBoolean(true);
            outputstream.writeUTF(s);
        }
          goto _L10
_L3:
        outputstream.writeInt(tablerow.getInt(i1));
          goto _L10
_L4:
        outputstream.writeLong(tablerow.getLong(i1));
          goto _L10
_L5:
        outputstream.writeFloat(tablerow.getFloat(i1));
          goto _L10
_L6:
        outputstream.writeDouble(tablerow.getDouble(i1));
          goto _L10
        outputstream.writeInt(columnCategories[i1].index(tablerow.getString(i1)));
          goto _L10
        outputstream.flush();
        outputstream.close();
        return;
    }

    public void setColumnCount(int i)
    {
        int j = columns.length;
        if(j != i)
        {
            columns = (Object[])PApplet.expand(((Object) (columns)), i);
            for(; j < i; j++)
                columns[j] = new String[rowCount];

            if(columnTitles != null)
                columnTitles = PApplet.expand(columnTitles, i);
            columnTypes = PApplet.expand(columnTypes, i);
            columnCategories = (HashMapBlows[])PApplet.expand(columnCategories, i);
        }
    }

    public void setColumnTitle(int i, String s)
    {
        ensureColumn(i);
        if(columnTitles == null)
            columnTitles = new String[getColumnCount()];
        columnTitles[i] = s;
        columnIndices = null;
    }

    public void setColumnTitles(String as[])
    {
        if(as != null)
            ensureColumn(as.length - 1);
        columnTitles = as;
        columnIndices = null;
    }

    public void setColumnType(int i, int j)
    {
        boolean flag;
        boolean flag1;
        boolean flag2;
        boolean flag3;
        boolean flag4;
        int k;
        flag = false;
        flag1 = false;
        flag2 = false;
        flag3 = false;
        flag4 = false;
        k = 0;
        j;
        JVM INSTR tableswitch 0 5: default 56
    //                   0 323
    //                   1 67
    //                   2 125
    //                   3 195
    //                   4 252
    //                   5 383;
           goto _L1 _L2 _L3 _L4 _L5 _L6 _L7
_L1:
        throw new IllegalArgumentException("That's not a valid column type.");
_L3:
        int ai[] = new int[rowCount];
        for(; k < rowCount; k++)
            ai[k] = PApplet.parseInt(getString(k, i), missingInt);

        columns[i] = ai;
_L9:
        columnTypes[i] = j;
        return;
_L4:
        long al[] = new long[rowCount];
        int l = ((flag) ? 1 : 0);
        while(l < rowCount) 
        {
            String s = getString(l, i);
            try
            {
                al[l] = Long.parseLong(s);
            }
            catch(NumberFormatException numberformatexception)
            {
                al[l] = missingLong;
            }
            l++;
        }
        columns[i] = al;
        continue; /* Loop/switch isn't completed */
_L5:
        float af[] = new float[rowCount];
        for(int i1 = ((flag1) ? 1 : 0); i1 < rowCount; i1++)
            af[i1] = PApplet.parseFloat(getString(i1, i), missingFloat);

        columns[i] = af;
        continue; /* Loop/switch isn't completed */
_L6:
        double ad[] = new double[rowCount];
        int j1 = ((flag2) ? 1 : 0);
        while(j1 < rowCount) 
        {
            String s1 = getString(j1, i);
            try
            {
                ad[j1] = Double.parseDouble(s1);
            }
            catch(NumberFormatException numberformatexception1)
            {
                ad[j1] = missingDouble;
            }
            j1++;
        }
        columns[i] = ad;
        continue; /* Loop/switch isn't completed */
_L2:
        if(columnTypes[i] != 0)
        {
            String as[] = new String[rowCount];
            for(int k1 = ((flag3) ? 1 : 0); k1 < rowCount; k1++)
                as[k1] = getString(k1, i);

            columns[i] = as;
        }
        continue; /* Loop/switch isn't completed */
_L7:
        int ai1[] = new int[rowCount];
        HashMapBlows hashmapblows = new HashMapBlows();
        for(int l1 = ((flag4) ? 1 : 0); l1 < rowCount; l1++)
            ai1[l1] = hashmapblows.index(getString(l1, i));

        columnCategories[i] = hashmapblows;
        columns[i] = ai1;
        if(true) goto _L9; else goto _L8
_L8:
    }

    public void setColumnType(int i, String s)
    {
        int j;
        if(s.equals("String"))
            j = 0;
        else
        if(s.equals("int"))
            j = 1;
        else
        if(s.equals("long"))
            j = 2;
        else
        if(s.equals("float"))
            j = 3;
        else
        if(s.equals("double"))
            j = 4;
        else
        if(s.equals("category"))
            j = 5;
        else
            throw new IllegalArgumentException((new StringBuilder()).append("'").append(s).append("' is not a valid column type.").toString());
        setColumnType(i, j);
    }

    public void setColumnType(String s, int i)
    {
        setColumnType(checkColumnIndex(s), i);
    }

    public void setColumnType(String s, String s1)
    {
        setColumnType(checkColumnIndex(s), s1);
    }

    public void setColumnTypes(Table table)
    {
        boolean flag = false;
        boolean flag1 = false;
        final int col;
        int i;
        final String typeNames[];
        if(table.hasColumnTitles())
        {
            col = table.getColumnIndex("title", true);
            i = table.getColumnIndex("type", true);
        } else
        {
            i = 1;
            col = 0;
        }
        setColumnTitles(table.getStringColumn(col));
        typeNames = table.getStringColumn(i);
        if(table.getColumnCount() > 1)
        {
            col = ((flag) ? 1 : 0);
            if(getRowCount() > 1000)
            {
                ExecutorService executorservice = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() / 2);
                for(col = ((flag1) ? 1 : 0); col < table.getRowCount(); col++)
                    executorservice.execute(new Runnable() {

                        public void run()
                        {
                            setColumnType(col, typeNames[col]);
                        }

                        final Table this$0;
                        final int val$col;
                        final String val$typeNames[];

            
            {
                this$0 = Table.this;
                col = i;
                typeNames = as;
                super();
            }
                    }
);

                executorservice.shutdown();
                for(; !executorservice.isTerminated(); Thread.yield());
            } else
            {
                for(; col < table.getRowCount(); col++)
                    setColumnType(col, typeNames[col]);

            }
        }
    }

    public void setColumnTypes(int ai[])
    {
        for(int i = 0; i < ai.length; i++)
            setColumnType(i, ai[i]);

    }

    public void setDouble(int i, int j, double d)
    {
        if(columnTypes[j] == 0)
        {
            setString(i, j, String.valueOf(d));
        } else
        {
            ensureBounds(i, j);
            if(columnTypes[j] != 4)
                throw new IllegalArgumentException((new StringBuilder()).append("Column ").append(j).append(" is not a 'double' column.").toString());
            ((double[])columns[j])[i] = d;
        }
    }

    public void setDouble(int i, String s, double d)
    {
        setDouble(i, getColumnIndex(s), d);
    }

    public void setFloat(int i, int j, float f)
    {
        if(columnTypes[j] == 0)
        {
            setString(i, j, String.valueOf(f));
        } else
        {
            ensureBounds(i, j);
            if(columnTypes[j] != 3)
                throw new IllegalArgumentException((new StringBuilder()).append("Column ").append(j).append(" is not a float column.").toString());
            ((float[])columns[j])[i] = f;
        }
    }

    public void setFloat(int i, String s, float f)
    {
        setFloat(i, getColumnIndex(s), f);
    }

    public void setInt(int i, int j, int k)
    {
        if(columnTypes[j] == 0)
        {
            setString(i, j, String.valueOf(k));
        } else
        {
            ensureBounds(i, j);
            if(columnTypes[j] != 1 && columnTypes[j] != 5)
                throw new IllegalArgumentException((new StringBuilder()).append("Column ").append(j).append(" is not an int column.").toString());
            ((int[])columns[j])[i] = k;
        }
    }

    public void setInt(int i, String s, int j)
    {
        setInt(i, getColumnIndex(s), j);
    }

    public void setLong(int i, int j, long l)
    {
        if(columnTypes[j] == 0)
        {
            setString(i, j, String.valueOf(l));
        } else
        {
            ensureBounds(i, j);
            if(columnTypes[j] != 2)
                throw new IllegalArgumentException((new StringBuilder()).append("Column ").append(j).append(" is not a 'long' column.").toString());
            ((long[])columns[j])[i] = l;
        }
    }

    public void setLong(int i, String s, long l)
    {
        setLong(i, getColumnIndex(s), l);
    }

    public void setMissingDouble(double d)
    {
        missingDouble = d;
    }

    public void setMissingFloat(float f)
    {
        missingFloat = f;
    }

    public void setMissingInt(int i)
    {
        missingInt = i;
    }

    public void setMissingLong(long l)
    {
        missingLong = l;
    }

    public void setMissingString(String s)
    {
        missingString = s;
    }

    public void setRow(int i, Object aobj[])
    {
        ensureBounds(i, aobj.length - 1);
        for(int j = 0; j < aobj.length; j++)
            setRowCol(i, j, aobj[j]);

    }

    protected void setRowCol(int i, int j, Object obj)
    {
        columnTypes[j];
        JVM INSTR tableswitch 0 5: default 44
    //                   0 55
    //                   1 87
    //                   2 152
    //                   3 225
    //                   4 290
    //                   5 363;
           goto _L1 _L2 _L3 _L4 _L5 _L6 _L7
_L1:
        throw new IllegalArgumentException("That's not a valid column type.");
_L2:
        String as[] = (String[])columns[j];
        if(obj == null)
            as[i] = null;
        else
            as[i] = String.valueOf(obj);
_L9:
        return;
_L3:
        int ai[] = (int[])columns[j];
        if(obj == null)
            ai[i] = missingInt;
        else
        if(obj instanceof Integer)
            ai[i] = ((Integer)obj).intValue();
        else
            ai[i] = PApplet.parseInt(String.valueOf(obj), missingInt);
        continue; /* Loop/switch isn't completed */
_L4:
        long al[] = (long[])columns[j];
        if(obj == null)
            al[i] = missingLong;
        else
        if(obj instanceof Long)
            al[i] = ((Long)obj).longValue();
        else
            try
            {
                al[i] = Long.parseLong(String.valueOf(obj));
            }
            // Misplaced declaration of an exception variable
            catch(Object obj)
            {
                al[i] = missingLong;
            }
        continue; /* Loop/switch isn't completed */
_L5:
        float af[] = (float[])columns[j];
        if(obj == null)
            af[i] = missingFloat;
        else
        if(obj instanceof Float)
            af[i] = ((Float)obj).floatValue();
        else
            af[i] = PApplet.parseFloat(String.valueOf(obj), missingFloat);
        continue; /* Loop/switch isn't completed */
_L6:
        double ad[] = (double[])columns[j];
        if(obj == null)
            ad[i] = missingDouble;
        else
        if(obj instanceof Double)
            ad[i] = ((Double)obj).doubleValue();
        else
            try
            {
                ad[i] = Double.parseDouble(String.valueOf(obj));
            }
            // Misplaced declaration of an exception variable
            catch(Object obj)
            {
                ad[i] = missingDouble;
            }
        continue; /* Loop/switch isn't completed */
_L7:
        int ai1[] = (int[])columns[j];
        if(obj == null)
            ai1[i] = missingCategory;
        else
            ai1[i] = columnCategories[j].index(String.valueOf(obj));
        if(true) goto _L9; else goto _L8
_L8:
    }

    public void setRowCount(int i)
    {
        long l;
        int j;
        if(i == rowCount)
            break MISSING_BLOCK_LABEL_332;
        if(i > 0xf4240)
            System.out.print((new StringBuilder()).append("Note: setting maximum row count to ").append(PApplet.nfc(i)).toString());
        l = System.currentTimeMillis();
        j = 0;
_L8:
        if(j >= columns.length)
            break MISSING_BLOCK_LABEL_281;
        columnTypes[j];
        JVM INSTR tableswitch 0 5: default 108
    //                   0 223
    //                   1 127
    //                   2 151
    //                   3 175
    //                   4 199
    //                   5 247;
           goto _L1 _L2 _L3 _L4 _L5 _L6 _L7
_L1:
        if(i > 0xf4240)
            try
            {
                Thread.sleep(10L);
            }
            catch(InterruptedException interruptedexception)
            {
                interruptedexception.printStackTrace();
            }
        j++;
        if(true) goto _L8; else goto _L3
_L3:
        columns[j] = PApplet.expand((int[])columns[j], i);
          goto _L1
_L4:
        columns[j] = PApplet.expand((long[])columns[j], i);
          goto _L1
_L5:
        columns[j] = PApplet.expand((float[])columns[j], i);
          goto _L1
_L6:
        columns[j] = PApplet.expand((double[])columns[j], i);
          goto _L1
_L2:
        columns[j] = PApplet.expand((String[])columns[j], i);
          goto _L1
_L7:
        columns[j] = PApplet.expand((int[])columns[j], i);
          goto _L1
        if(i > 0xf4240)
        {
            int k = (int)(System.currentTimeMillis() - l);
            System.out.println((new StringBuilder()).append(" (resize took ").append(PApplet.nfc(k)).append(" ms)").toString());
        }
        rowCount = i;
        return;
    }

    public void setString(int i, int j, String s)
    {
        ensureBounds(i, j);
        if(columnTypes[j] != 0)
        {
            throw new IllegalArgumentException((new StringBuilder()).append("Column ").append(j).append(" is not a String column.").toString());
        } else
        {
            ((String[])columns[j])[i] = s;
            return;
        }
    }

    public void setString(int i, String s, String s1)
    {
        setString(i, checkColumnIndex(s), s1);
    }

    public void setTableType(String s)
    {
        for(int i = 0; i < getColumnCount(); i++)
            setColumnType(i, s);

    }

    public void sort(int i)
    {
        sort(i, false);
    }

    protected void sort(final int column, final boolean reverse)
    {
        final int order[];
        order = IntList.fromRange(getRowCount()).array();
        (new Sort() {

            public float compare(int j1, int k1)
            {
                int l1;
                if(reverse)
                    l1 = order[k1];
                else
                    l1 = order[j1];
                if(reverse)
                    j1 = order[j1];
                else
                    j1 = order[k1];
                getColumnType(column);
                JVM INSTR tableswitch 0 5: default 76
            //                           0 256
            //                           1 133
            //                           2 164
            //                           3 195
            //                           4 225
            //                           5 289;
                   goto _L1 _L2 _L3 _L4 _L5 _L6 _L7
_L1:
                throw new IllegalArgumentException((new StringBuilder()).append("Invalid column type: ").append(getColumnType(column)).toString());
_L3:
                float f = getInt(l1, column) - getInt(j1, column);
_L9:
                return f;
_L4:
                f = getLong(l1, column) - getLong(j1, column);
                continue; /* Loop/switch isn't completed */
_L5:
                f = getFloat(l1, column) - getFloat(j1, column);
                continue; /* Loop/switch isn't completed */
_L6:
                f = (float)(getDouble(l1, column) - getDouble(j1, column));
                continue; /* Loop/switch isn't completed */
_L2:
                f = getString(l1, column).compareToIgnoreCase(getString(j1, column));
                continue; /* Loop/switch isn't completed */
_L7:
                f = getInt(l1, column) - getInt(j1, column);
                if(true) goto _L9; else goto _L8
_L8:
            }

            public int size()
            {
                return getRowCount();
            }

            public void swap(int j1, int k1)
            {
                int l1 = order[j1];
                order[j1] = order[k1];
                order[k1] = l1;
            }

            final Table this$0;
            final int val$column;
            final int val$order[];
            final boolean val$reverse;

            
            {
                this$0 = Table.this;
                reverse = flag;
                order = ai;
                column = i;
                super();
            }
        }
).run();
        column = 0;
_L8:
        if(column >= getColumnCount())
            break MISSING_BLOCK_LABEL_387;
        getColumnType(column);
        JVM INSTR tableswitch 0 5: default 80
    //                   0 326
    //                   1 86
    //                   2 146
    //                   3 206
    //                   4 266
    //                   5 86;
           goto _L1 _L2 _L3 _L4 _L5 _L6 _L3
_L2:
        break MISSING_BLOCK_LABEL_326;
_L1:
        break; /* Loop/switch isn't completed */
_L3:
        break; /* Loop/switch isn't completed */
_L9:
        column++;
        if(true) goto _L8; else goto _L7
_L7:
        int ai[] = (int[])columns[column];
        int ai1[] = new int[rowCount];
        for(int i = 0; i < getRowCount(); i++)
            ai1[i] = ai[order[i]];

        columns[column] = ai1;
          goto _L9
_L4:
        long al1[] = (long[])columns[column];
        long al[] = new long[rowCount];
        for(int j = 0; j < getRowCount(); j++)
            al[j] = al1[order[j]];

        columns[column] = al;
          goto _L9
_L5:
        float af1[] = (float[])columns[column];
        float af[] = new float[rowCount];
        for(int k = 0; k < getRowCount(); k++)
            af[k] = af1[order[k]];

        columns[column] = af;
          goto _L9
_L6:
        double ad1[] = (double[])columns[column];
        double ad[] = new double[rowCount];
        for(int l = 0; l < getRowCount(); l++)
            ad[l] = ad1[order[l]];

        columns[column] = ad;
          goto _L9
        String as1[] = (String[])columns[column];
        String as[] = new String[rowCount];
        for(int i1 = 0; i1 < getRowCount(); i1++)
            as[i1] = as1[order[i1]];

        columns[column] = as;
          goto _L9
    }

    public void sort(String s)
    {
        sort(getColumnIndex(s), false);
    }

    public void sortReverse(int i)
    {
        sort(i, true);
    }

    public void sortReverse(String s)
    {
        sort(getColumnIndex(s), true);
    }

    public void trim()
    {
        for(int i = 0; i < getColumnCount(); i++)
            trim(i);

    }

    public void trim(int i)
    {
        if(columnTypes[i] == 0)
        {
            String as[] = (String[])columns[i];
            for(i = 0; i < rowCount; i++)
                if(as[i] != null)
                    as[i] = PApplet.trim(as[i]);

        }
    }

    public void trim(String s)
    {
        trim(getColumnIndex(s));
    }

    public Table typedParse(InputStream inputstream, String s)
        throws IOException
    {
        Table table = new Table();
        table.setColumnTypes(this);
        table.parse(inputstream, s);
        return table;
    }

    protected void writeCSV(PrintWriter printwriter)
    {
        if(columnTitles != null)
        {
            for(int i = 0; i < columns.length; i++)
            {
                if(i != 0)
                    printwriter.print(',');
                if(columnTitles[i] != null)
                    writeEntryCSV(printwriter, columnTitles[i]);
            }

            printwriter.println();
        }
        for(int j = 0; j < rowCount; j++)
        {
            for(int k = 0; k < getColumnCount(); k++)
            {
                if(k != 0)
                    printwriter.print(',');
                String s = getString(j, k);
                if(s != null)
                    writeEntryCSV(printwriter, s);
            }

            printwriter.println();
        }

        printwriter.flush();
    }

    protected void writeEntryCSV(PrintWriter printwriter, String s)
    {
        int i = 0;
        if(s != null)
            if(s.indexOf('"') != -1)
            {
                s = s.toCharArray();
                printwriter.print('"');
                while(i < s.length) 
                {
                    if(s[i] == '"')
                        printwriter.print("\"\"");
                    else
                        printwriter.print(s[i]);
                    i++;
                }
                printwriter.print('"');
            } else
            if(s.indexOf(',') != -1 || s.indexOf('\n') != -1 || s.indexOf('\r') != -1)
            {
                printwriter.print('"');
                printwriter.print(s);
                printwriter.print('"');
            } else
            if(s.length() > 0 && (s.charAt(0) == ' ' || s.charAt(s.length() - 1) == ' '))
            {
                printwriter.print('"');
                printwriter.print(s);
                printwriter.print('"');
            } else
            {
                printwriter.print(s);
            }
    }

    protected void writeEntryHTML(PrintWriter printwriter, String s)
    {
        s = s.toCharArray();
        int i = s.length;
        int j = 0;
        while(j < i) 
        {
            char c = s[j];
            if(c == '<')
                printwriter.print("&lt;");
            else
            if(c == '>')
                printwriter.print("&gt;");
            else
            if(c == '&')
                printwriter.print("&amp;");
            else
            if(c == '\'')
                printwriter.print("&apos;");
            else
            if(c == '"')
                printwriter.print("&quot;");
            else
                printwriter.print(c);
            j++;
        }
    }

    protected void writeHTML(PrintWriter printwriter)
    {
        printwriter.println("<html>");
        printwriter.println("<head>");
        printwriter.println("  <meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\" />");
        printwriter.println("</head>");
        printwriter.println("<body>");
        printwriter.println("  <table>");
        for(int i = 0; i < getRowCount(); i++)
        {
            printwriter.println("    <tr>");
            for(int j = 0; j < getColumnCount(); j++)
            {
                String s = getString(i, j);
                printwriter.print("      <td>");
                writeEntryHTML(printwriter, s);
                printwriter.println("      </td>");
            }

            printwriter.println("    </tr>");
        }

        printwriter.println("  </table>");
        printwriter.println("</body>");
        printwriter.println("</hmtl>");
        printwriter.flush();
    }

    protected void writeTSV(PrintWriter printwriter)
    {
        if(columnTitles != null)
        {
            for(int i = 0; i < columns.length; i++)
            {
                if(i != 0)
                    printwriter.print('\t');
                if(columnTitles[i] != null)
                    printwriter.print(columnTitles[i]);
            }

            printwriter.println();
        }
        for(int j = 0; j < rowCount; j++)
        {
            for(int k = 0; k < getColumnCount(); k++)
            {
                if(k != 0)
                    printwriter.print('\t');
                String s = getString(j, k);
                if(s != null)
                    printwriter.print(s);
            }

            printwriter.println();
        }

        printwriter.flush();
    }

    public static final int CATEGORY = 5;
    public static final int DOUBLE = 4;
    public static final int FLOAT = 3;
    public static final int INT = 1;
    public static final int LONG = 2;
    public static final int STRING = 0;
    static final String loadExtensions[] = {
        "csv", "tsv", "ods", "bin"
    };
    static final String saveExtensions[] = {
        "csv", "tsv", "html", "bin"
    };
    HashMapBlows columnCategories[];
    HashMap columnIndices;
    String columnTitles[];
    int columnTypes[];
    protected Object columns[];
    protected int expandIncrement;
    protected int missingCategory;
    protected double missingDouble;
    protected float missingFloat;
    protected int missingInt;
    protected long missingLong;
    protected String missingString;
    protected int rowCount;
    protected RowIterator rowIterator;

}
