// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.data;

import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import processing.core.PApplet;

public class XML
    implements Serializable
{

    protected XML()
    {
    }

    public XML(File file)
        throws IOException, ParserConfigurationException, SAXException
    {
        this(file, ((String) (null)));
    }

    public XML(File file, String s)
        throws IOException, ParserConfigurationException, SAXException
    {
        this(((Reader) (PApplet.createReader(file))), s);
    }

    public XML(InputStream inputstream)
        throws IOException, ParserConfigurationException, SAXException
    {
        this(inputstream, ((String) (null)));
    }

    public XML(InputStream inputstream, String s)
        throws IOException, ParserConfigurationException, SAXException
    {
        this(((Reader) (PApplet.createReader(inputstream))), s);
    }

    public XML(Reader reader)
        throws IOException, ParserConfigurationException, SAXException
    {
        this(reader, ((String) (null)));
    }

    public XML(Reader reader, String s)
        throws IOException, ParserConfigurationException, SAXException
    {
        s = DocumentBuilderFactory.newInstance();
        try
        {
            s.setAttribute("http://apache.org/xml/features/nonvalidating/load-external-dtd", Boolean.valueOf(false));
        }
        catch(IllegalArgumentException illegalargumentexception) { }
        s.setExpandEntityReferences(false);
        node = s.newDocumentBuilder().parse(new InputSource(reader)).getDocumentElement();
    }

    public XML(String s)
    {
        try
        {
            node = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument().createElement(s);
            parent = null;
            return;
        }
        // Misplaced declaration of an exception variable
        catch(String s)
        {
            throw new RuntimeException(s);
        }
    }

    protected XML(XML xml, Node node1)
    {
        node = node1;
        parent = xml;
    }

    public static XML parse(String s)
        throws IOException, ParserConfigurationException, SAXException
    {
        return parse(s, null);
    }

    public static XML parse(String s, String s1)
        throws IOException, ParserConfigurationException, SAXException
    {
        return new XML(new StringReader(s), ((String) (null)));
    }

    public XML addChild(String s)
    {
        return appendChild(node.getOwnerDocument().createElement(s));
    }

    public XML addChild(XML xml)
    {
        return appendChild(node.getOwnerDocument().importNode((Node)xml.getNative(), true));
    }

    protected XML appendChild(Node node1)
    {
        node.appendChild(node1);
        node1 = new XML(this, node1);
        if(children != null)
            children = (XML[])PApplet.concat(children, new XML[] {
                node1
            });
        return node1;
    }

    protected void checkChildren()
    {
        if(children == null)
        {
            NodeList nodelist = node.getChildNodes();
            int i = nodelist.getLength();
            children = new XML[i];
            for(int j = 0; j < i; j++)
                children[j] = new XML(this, nodelist.item(j));

        }
    }

    public String format(int i)
    {
        boolean flag = false;
        Object obj = TransformerFactory.newInstance();
        int j;
        j = ((flag) ? 1 : 0);
        if(i == -1)
            break MISSING_BLOCK_LABEL_27;
        ((TransformerFactory) (obj)).setAttribute("indent-number", Integer.valueOf(i));
        j = ((flag) ? 1 : 0);
_L8:
        Transformer transformer = ((TransformerFactory) (obj)).newTransformer();
        if(i == -1) goto _L2; else goto _L1
_L1:
        if(parent != null) goto _L3; else goto _L2
_L2:
        transformer.setOutputProperty("omit-xml-declaration", "yes");
_L9:
        transformer.setOutputProperty("method", "xml");
        if(j == 0)
            break MISSING_BLOCK_LABEL_79;
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(i));
        String s;
        Object obj1;
        transformer.setOutputProperty("encoding", "UTF-8");
        transformer.setOutputProperty("indent", "yes");
        s = System.getProperty("line.separator");
        obj1 = JVM INSTR new #219 <Class StringWriter>;
        ((StringWriter) (obj1)).StringWriter();
        StreamResult streamresult = JVM INSTR new #222 <Class StreamResult>;
        streamresult.StreamResult(((java.io.Writer) (obj1)));
        obj = JVM INSTR new #227 <Class DOMSource>;
        ((DOMSource) (obj)).DOMSource(node);
        transformer.transform(((javax.xml.transform.Source) (obj)), streamresult);
        obj1 = PApplet.split(((StringWriter) (obj1)).toString(), s);
        obj = obj1;
        if(!obj1[0].startsWith("<?xml")) goto _L5; else goto _L4
_L4:
        j = obj1[0].indexOf("?>") + 2;
        if(obj1[0].length() != j) goto _L7; else goto _L6
_L6:
        obj = PApplet.subset(((String []) (obj1)), 1);
_L5:
        obj = PApplet.join(PApplet.trim(((String []) (obj))), "");
        if(i != -1)
            try
            {
label0:
                {
                    if(((String) (obj)).trim().length() != 0)
                        break label0;
                    obj1 = JVM INSTR new #283 <Class StringBuilder>;
                    ((StringBuilder) (obj1)).StringBuilder();
                    obj = ((StringBuilder) (obj1)).append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(s).append(((String) (obj))).toString();
                }
            }
            // Misplaced declaration of an exception variable
            catch(Object obj)
            {
                ((Exception) (obj)).printStackTrace();
                obj = null;
            }
_L11:
        return ((String) (obj));
        obj1;
        j = 1;
          goto _L8
_L3:
        transformer.setOutputProperty("omit-xml-declaration", "no");
          goto _L9
_L7:
        obj1[0] = obj1[0].substring(j);
        obj = obj1;
          goto _L5
        obj1 = JVM INSTR new #219 <Class StringWriter>;
        ((StringWriter) (obj1)).StringWriter();
        StreamResult streamresult1 = JVM INSTR new #222 <Class StreamResult>;
        streamresult1.StreamResult(((java.io.Writer) (obj1)));
        StreamSource streamsource = JVM INSTR new #293 <Class StreamSource>;
        StringReader stringreader = JVM INSTR new #114 <Class StringReader>;
        stringreader.StringReader(((String) (obj)));
        streamsource.StreamSource(stringreader);
        transformer.transform(streamsource, streamresult1);
        obj1 = ((StringWriter) (obj1)).toString();
        obj = obj1;
        if(((String) (obj1)).startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) goto _L11; else goto _L10
_L10:
        obj = JVM INSTR new #283 <Class StringBuilder>;
        ((StringBuilder) (obj)).StringBuilder();
        obj = ((StringBuilder) (obj)).append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(s).append(((String) (obj1))).toString();
          goto _L11
    }

    public int getAttributeCount()
    {
        return node.getAttributes().getLength();
    }

    public XML getChild(int i)
    {
        checkChildren();
        return children[i];
    }

    public XML getChild(String s)
    {
        if(s.length() > 0 && s.charAt(0) == '/')
            throw new IllegalArgumentException("getChild() should not begin with a slash");
        if(s.indexOf('/') == -1) goto _L2; else goto _L1
_L1:
        XML xml = getChildRecursive(PApplet.split(s, '/'), 0);
_L4:
        return xml;
_L2:
        int i = getChildCount();
        for(int j = 0; j < i; j++)
        {
            xml = getChild(j);
            String s1 = xml.getName();
            if(s1 != null && s1.equals(s))
                continue; /* Loop/switch isn't completed */
        }

        xml = null;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public int getChildCount()
    {
        checkChildren();
        return children.length;
    }

    protected XML getChildRecursive(String as[], int i)
    {
        if(!Character.isDigit(as[i].charAt(0))) goto _L2; else goto _L1
_L1:
        Object obj;
        obj = getChild(Integer.parseInt(as[i]));
        if(i != as.length - 1)
            obj = ((XML) (obj)).getChildRecursive(as, i + 1);
_L4:
        return ((XML) (obj));
_L2:
        int j = getChildCount();
        int k = 0;
        do
        {
            if(k >= j)
                break;
            XML xml = getChild(k);
            obj = xml.getName();
            if(obj != null && ((String) (obj)).equals(as[i]))
            {
                obj = xml;
                if(i != as.length - 1)
                    obj = xml.getChildRecursive(as, i + 1);
                continue; /* Loop/switch isn't completed */
            }
            k++;
        } while(true);
        obj = null;
        if(true) goto _L4; else goto _L3
_L3:
    }

    public XML[] getChildren()
    {
        checkChildren();
        return children;
    }

    public XML[] getChildren(String s)
    {
        if(s.length() > 0 && s.charAt(0) == '/')
            throw new IllegalArgumentException("getChildren() should not begin with a slash");
        if(s.indexOf('/') != -1)
            s = getChildrenRecursive(PApplet.split(s, '/'), 0);
        else
        if(Character.isDigit(s.charAt(0)))
        {
            XML axml[] = new XML[1];
            axml[0] = getChild(Integer.parseInt(s));
            s = axml;
        } else
        {
            int i = getChildCount();
            XML axml1[] = new XML[i];
            int j = 0;
            int k = 0;
            for(; j < i; j++)
            {
                XML xml = getChild(j);
                String s1 = xml.getName();
                if(s1 != null && s1.equals(s))
                {
                    int l = k + 1;
                    axml1[k] = xml;
                    k = l;
                }
            }

            s = (XML[])PApplet.subset(axml1, 0, k);
        }
        return s;
    }

    protected XML[] getChildrenRecursive(String as[], int i)
    {
        if(i != as.length - 1) goto _L2; else goto _L1
_L1:
        XML axml[] = getChildren(as[i]);
_L4:
        return axml;
_L2:
        XML axml1[] = getChildren(as[i]);
        XML axml2[] = new XML[0];
        int j = 0;
        do
        {
            axml = axml2;
            if(j >= axml1.length)
                continue;
            axml2 = (XML[])PApplet.concat(axml2, axml1[j].getChildrenRecursive(as, i + 1));
            j++;
        } while(true);
        if(true) goto _L4; else goto _L3
_L3:
    }

    public String getContent()
    {
        return node.getTextContent();
    }

    public String getContent(String s)
    {
        String s1 = node.getTextContent();
        if(s1 != null)
            s = s1;
        return s;
    }

    public double getDouble(String s)
    {
        return getDouble(s, 0.0D);
    }

    public double getDouble(String s, double d)
    {
        s = getString(s);
        if(s != null)
            d = Double.parseDouble(s);
        return d;
    }

    public double getDoubleContent()
    {
        return getDoubleContent(0.0D);
    }

    public double getDoubleContent(double d)
    {
        String s = node.getTextContent();
        double d1 = d;
        if(s != null)
            try
            {
                d1 = Double.parseDouble(s);
            }
            catch(NumberFormatException numberformatexception)
            {
                d1 = d;
            }
        return d1;
    }

    public float getFloat(String s)
    {
        return getFloat(s, 0.0F);
    }

    public float getFloat(String s, float f)
    {
        s = getString(s);
        if(s != null)
            f = Float.parseFloat(s);
        return f;
    }

    public float getFloatContent()
    {
        return getFloatContent(0.0F);
    }

    public float getFloatContent(float f)
    {
        return PApplet.parseFloat(node.getTextContent(), f);
    }

    public int getInt(String s)
    {
        return getInt(s, 0);
    }

    public int getInt(String s, int i)
    {
        s = getString(s);
        if(s != null)
            i = Integer.parseInt(s);
        return i;
    }

    public int getIntContent()
    {
        return getIntContent(0);
    }

    public int getIntContent(int i)
    {
        return PApplet.parseInt(node.getTextContent(), i);
    }

    public String getLocalName()
    {
        return node.getLocalName();
    }

    public long getLong(String s, long l)
    {
        s = getString(s);
        if(s != null)
            l = Long.parseLong(s);
        return l;
    }

    public long getLongContent()
    {
        return getLongContent(0L);
    }

    public long getLongContent(long l)
    {
        String s = node.getTextContent();
        long l1 = l;
        if(s != null)
            try
            {
                l1 = Long.parseLong(s);
            }
            catch(NumberFormatException numberformatexception)
            {
                l1 = l;
            }
        return l1;
    }

    public String getName()
    {
        return node.getNodeName();
    }

    protected Object getNative()
    {
        return node;
    }

    public XML getParent()
    {
        return parent;
    }

    public String getString(String s)
    {
        return getString(s, null);
    }

    public String getString(String s, String s1)
    {
        s = node.getAttributes().getNamedItem(s);
        if(s != null)
            s1 = s.getNodeValue();
        return s1;
    }

    public boolean hasAttribute(String s)
    {
        boolean flag;
        if(node.getAttributes().getNamedItem(s) != null)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public boolean hasChildren()
    {
        checkChildren();
        boolean flag;
        if(children.length > 0)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public String[] listAttributes()
    {
        NamedNodeMap namednodemap = node.getAttributes();
        String as[] = new String[namednodemap.getLength()];
        for(int i = 0; i < as.length; i++)
            as[i] = namednodemap.item(i).getNodeName();

        return as;
    }

    public String[] listChildren()
    {
        checkChildren();
        String as[] = new String[children.length];
        for(int i = 0; i < children.length; i++)
            as[i] = children[i].getName();

        return as;
    }

    public void removeChild(XML xml)
    {
        node.removeChild(xml.node);
        children = null;
    }

    public boolean save(File file, String s)
    {
        file = PApplet.createWriter(file);
        boolean flag = write(file);
        file.flush();
        file.close();
        return flag;
    }

    public void setContent(String s)
    {
        node.setTextContent(s);
    }

    public void setDouble(String s, double d)
    {
        setString(s, String.valueOf(d));
    }

    public void setDoubleContent(double d)
    {
        setContent(String.valueOf(d));
    }

    public void setFloat(String s, float f)
    {
        setString(s, String.valueOf(f));
    }

    public void setFloatContent(float f)
    {
        setContent(String.valueOf(f));
    }

    public void setInt(String s, int i)
    {
        setString(s, String.valueOf(i));
    }

    public void setIntContent(int i)
    {
        setContent(String.valueOf(i));
    }

    public void setLong(String s, long l)
    {
        setString(s, String.valueOf(l));
    }

    public void setLongContent(long l)
    {
        setContent(String.valueOf(l));
    }

    public void setName(String s)
    {
        node = node.getOwnerDocument().renameNode(node, null, s);
    }

    public void setString(String s, String s1)
    {
        ((Element)node).setAttribute(s, s1);
    }

    public String toString()
    {
        return format(-1);
    }

    public boolean write(PrintWriter printwriter)
    {
        printwriter.print(format(2));
        printwriter.flush();
        return true;
    }

    protected XML children[];
    protected Node node;
    protected XML parent;
}
