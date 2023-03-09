// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.core;

import java.io.*;
import java.util.*;

// Referenced classes of package processing.core:
//            PShape, PApplet, PVector, PImage

public class PShapeOBJ extends PShape
{
    protected static class OBJFace
    {

        int matIdx;
        String name;
        ArrayList normIdx;
        ArrayList texIdx;
        ArrayList vertIdx;

        OBJFace()
        {
            vertIdx = new ArrayList();
            texIdx = new ArrayList();
            normIdx = new ArrayList();
            matIdx = -1;
            name = "";
        }
    }

    protected static class OBJMaterial
    {

        float d;
        PVector ka;
        PVector kd;
        PImage kdMap;
        PVector ks;
        String name;
        float ns;

        OBJMaterial()
        {
            this("default");
        }

        OBJMaterial(String s)
        {
            name = s;
            ka = new PVector(0.5F, 0.5F, 0.5F);
            kd = new PVector(0.5F, 0.5F, 0.5F);
            ks = new PVector(0.5F, 0.5F, 0.5F);
            d = 1.0F;
            ns = 0.0F;
            kdMap = null;
        }
    }


    public PShapeOBJ(PApplet papplet, BufferedReader bufferedreader)
    {
        this(papplet, bufferedreader, "");
    }

    public PShapeOBJ(PApplet papplet, BufferedReader bufferedreader, String s)
    {
        ArrayList arraylist = new ArrayList();
        ArrayList arraylist1 = new ArrayList();
        ArrayList arraylist2 = new ArrayList();
        ArrayList arraylist3 = new ArrayList();
        ArrayList arraylist4 = new ArrayList();
        parseOBJ(papplet, s, bufferedreader, arraylist, arraylist1, arraylist2, arraylist3, arraylist4);
        family = 0;
        addChildren(arraylist, arraylist1, arraylist2, arraylist3, arraylist4);
    }

    public PShapeOBJ(PApplet papplet, String s)
    {
        this(papplet, papplet.createReader(s), getBasePath(papplet, s));
    }

    protected PShapeOBJ(OBJFace objface, OBJMaterial objmaterial, ArrayList arraylist, ArrayList arraylist1, ArrayList arraylist2)
    {
        PVector pvector1;
        family = 3;
        int i;
        Object obj;
        PVector pvector;
        int j;
        PVector pvector2;
        if(objface.vertIdx.size() == 3)
            kind = 9;
        else
        if(objface.vertIdx.size() == 4)
            kind = 17;
        else
            kind = 20;
        stroke = false;
        fill = true;
        fillColor = rgbaValue(objmaterial.kd);
        ambientColor = rgbaValue(objmaterial.ka);
        specularColor = rgbaValue(objmaterial.ks);
        shininess = objmaterial.ns;
        if(objmaterial.kdMap != null)
            tintColor = rgbaValue(objmaterial.kd, objmaterial.d);
        vertexCount = objface.vertIdx.size();
        i = vertexCount;
        vertices = new float[i][12];
        i = 0;
        if(i >= objface.vertIdx.size())
            break; /* Loop/switch isn't completed */
        obj = null;
        pvector = (PVector)arraylist.get(((Integer)objface.vertIdx.get(i)).intValue() - 1);
        if(i >= objface.normIdx.size())
            break MISSING_BLOCK_LABEL_519;
        j = ((Integer)objface.normIdx.get(i)).intValue() - 1;
        if(-1 >= j)
            break MISSING_BLOCK_LABEL_519;
        pvector1 = (PVector)arraylist1.get(j);
_L4:
        pvector2 = obj;
        if(i < objface.texIdx.size())
        {
            j = ((Integer)objface.texIdx.get(i)).intValue() - 1;
            pvector2 = obj;
            if(-1 < j)
                pvector2 = (PVector)arraylist2.get(j);
        }
        vertices[i][0] = pvector.x;
        vertices[i][1] = pvector.y;
        vertices[i][2] = pvector.z;
        vertices[i][3] = objmaterial.kd.x;
        vertices[i][4] = objmaterial.kd.y;
        vertices[i][5] = objmaterial.kd.z;
        vertices[i][6] = 1.0F;
        if(pvector1 != null)
        {
            vertices[i][9] = pvector1.x;
            vertices[i][10] = pvector1.y;
            vertices[i][11] = pvector1.z;
        }
        if(pvector2 != null)
        {
            vertices[i][7] = pvector2.x;
            vertices[i][8] = pvector2.y;
        }
        if(objmaterial != null && objmaterial.kdMap != null)
            image = objmaterial.kdMap;
        i++;
        if(true) goto _L2; else goto _L1
_L2:
        break MISSING_BLOCK_LABEL_131;
_L1:
        return;
        pvector1 = null;
        if(true) goto _L4; else goto _L3
_L3:
    }

    protected static OBJMaterial addMaterial(String s, ArrayList arraylist, Map map)
    {
        OBJMaterial objmaterial = new OBJMaterial(s);
        map.put(s, Integer.valueOf(arraylist.size()));
        arraylist.add(objmaterial);
        return objmaterial;
    }

    protected static String getBasePath(PApplet papplet, String s)
    {
        File file = new File(papplet.dataPath(s));
        File file1 = file;
        if(!file.exists())
            file1 = papplet.sketchFile(s);
        papplet = file1.getAbsolutePath();
        return papplet.substring(0, papplet.lastIndexOf(File.separator));
    }

    protected static void parseMTL(PApplet papplet, String s, String s1, BufferedReader bufferedreader, ArrayList arraylist, Map map)
    {
        Object obj = null;
_L2:
        Object obj1 = bufferedreader.readLine();
        if(obj1 == null)
            break MISSING_BLOCK_LABEL_239;
        Object obj2;
        obj2 = ((String) (obj1)).trim().split("\\s+");
        if(obj2.length <= 0)
            continue; /* Loop/switch isn't completed */
        if(obj2[0].equals("newmtl"))
        {
            obj = addMaterial(obj2[1], arraylist, map);
            continue; /* Loop/switch isn't completed */
        }
        obj1 = obj;
        if(obj != null)
            break MISSING_BLOCK_LABEL_106;
        obj = JVM INSTR new #228 <Class StringBuilder>;
        ((StringBuilder) (obj)).StringBuilder();
        obj1 = addMaterial(((StringBuilder) (obj)).append("material").append(arraylist.size()).toString(), arraylist, map);
        if(!obj2[0].equals("map_Kd") || obj2.length <= 1)
            break MISSING_BLOCK_LABEL_299;
        obj2 = obj2[1];
        obj = obj2;
        if(((String) (obj2)).indexOf(File.separator) != -1)
            break MISSING_BLOCK_LABEL_192;
        obj = obj2;
        if(!s1.equals(""))
        {
            obj = JVM INSTR new #228 <Class StringBuilder>;
            ((StringBuilder) (obj)).StringBuilder();
            obj = ((StringBuilder) (obj)).append(s1).append(File.separator).append(((String) (obj2))).toString();
        }
        obj2 = JVM INSTR new #170 <Class File>;
        ((File) (obj2)).File(papplet.dataPath(((String) (obj))));
        if(!((File) (obj2)).exists())
            break MISSING_BLOCK_LABEL_240;
        obj1.kdMap = papplet.loadImage(((String) (obj)));
        obj = obj1;
        continue; /* Loop/switch isn't completed */
        papplet;
        papplet.printStackTrace();
        return;
        obj2 = System.err;
        StringBuilder stringbuilder = JVM INSTR new #228 <Class StringBuilder>;
        stringbuilder.StringBuilder();
        ((PrintStream) (obj2)).println(stringbuilder.append("The texture map \"").append(((String) (obj))).append("\" in the materials definition file \"").append(s).append("\" is missing or inaccessible, make sure the URL is valid or that the file has been added to your sketch and is readable.").toString());
        obj = obj1;
        continue; /* Loop/switch isn't completed */
        if(!obj2[0].equals("Ka") || obj2.length <= 3)
            break MISSING_BLOCK_LABEL_380;
        ((OBJMaterial) (obj1)).ka.x = Float.valueOf(obj2[1]).floatValue();
        ((OBJMaterial) (obj1)).ka.y = Float.valueOf(obj2[2]).floatValue();
        ((OBJMaterial) (obj1)).ka.z = Float.valueOf(obj2[3]).floatValue();
        obj = obj1;
        continue; /* Loop/switch isn't completed */
        if(!obj2[0].equals("Kd") || obj2.length <= 3)
            break MISSING_BLOCK_LABEL_461;
        ((OBJMaterial) (obj1)).kd.x = Float.valueOf(obj2[1]).floatValue();
        ((OBJMaterial) (obj1)).kd.y = Float.valueOf(obj2[2]).floatValue();
        ((OBJMaterial) (obj1)).kd.z = Float.valueOf(obj2[3]).floatValue();
        obj = obj1;
        continue; /* Loop/switch isn't completed */
        if(!obj2[0].equals("Ks") || obj2.length <= 3)
            break MISSING_BLOCK_LABEL_542;
        ((OBJMaterial) (obj1)).ks.x = Float.valueOf(obj2[1]).floatValue();
        ((OBJMaterial) (obj1)).ks.y = Float.valueOf(obj2[2]).floatValue();
        ((OBJMaterial) (obj1)).ks.z = Float.valueOf(obj2[3]).floatValue();
        obj = obj1;
        continue; /* Loop/switch isn't completed */
        if(!obj2[0].equals("d") && !obj2[0].equals("Tr") || obj2.length <= 1)
            break MISSING_BLOCK_LABEL_597;
        obj1.d = Float.valueOf(obj2[1]).floatValue();
        obj = obj1;
        continue; /* Loop/switch isn't completed */
        obj = obj1;
        if(!obj2[0].equals("Ns"))
            continue; /* Loop/switch isn't completed */
        obj = obj1;
        if(obj2.length <= 1)
            continue; /* Loop/switch isn't completed */
        obj1.ns = Float.valueOf(obj2[1]).floatValue();
        obj = obj1;
        if(true) goto _L2; else goto _L1
_L1:
    }

    protected static void parseOBJ(PApplet papplet, String s, BufferedReader bufferedreader, ArrayList arraylist, ArrayList arraylist1, ArrayList arraylist2, ArrayList arraylist3, ArrayList arraylist4)
    {
        HashMap hashmap;
        int i;
        int j;
        String s1;
        boolean flag;
        boolean flag1;
        hashmap = new HashMap();
        i = -1;
        j = 0;
        s1 = "object";
        flag = false;
        flag1 = false;
_L4:
        Object obj = bufferedreader.readLine();
        if(obj == null) goto _L2; else goto _L1
_L1:
        obj = ((String) (obj)).trim();
        if(((String) (obj)).equals("") || ((String) (obj)).indexOf('#') == 0) goto _L4; else goto _L3
_L3:
        Object obj1;
        String s2;
        if(!((String) (obj)).contains("\\"))
            break MISSING_BLOCK_LABEL_132;
        obj1 = ((String) (obj)).split("\\\\")[0];
        s2 = bufferedreader.readLine();
        obj = obj1;
        if(s2 == null) goto _L3; else goto _L5
_L5:
        obj = JVM INSTR new #228 <Class StringBuilder>;
        ((StringBuilder) (obj)).StringBuilder();
        obj = ((StringBuilder) (obj)).append(((String) (obj1))).append(s2).toString();
          goto _L3
        obj = ((String) (obj)).split("\\s+");
        if(obj.length <= 0) goto _L7; else goto _L6
_L6:
        if(!obj[0].equals("v")) goto _L9; else goto _L8
_L8:
        obj1 = JVM INSTR new #128 <Class PVector>;
        ((PVector) (obj1)).PVector(Float.valueOf(obj[1]).floatValue(), Float.valueOf(obj[2]).floatValue(), Float.valueOf(obj[3]).floatValue());
        arraylist2.add(obj1);
        int k = j;
        flag1 = flag;
        flag = true;
        j = i;
        i = k;
_L10:
        boolean flag7 = flag1;
        flag1 = flag;
        int l = j;
        j = i;
        flag = flag7;
        i = l;
          goto _L4
_L9:
        if(!obj[0].equals("vn"))
            break MISSING_BLOCK_LABEL_341;
        obj1 = JVM INSTR new #128 <Class PVector>;
        ((PVector) (obj1)).PVector(Float.valueOf(obj[1]).floatValue(), Float.valueOf(obj[2]).floatValue(), Float.valueOf(obj[3]).floatValue());
        arraylist3.add(obj1);
        int i1 = j;
        boolean flag8 = true;
        flag = flag1;
        j = i;
        i = i1;
        flag1 = flag8;
          goto _L10
        if(!obj[0].equals("vt"))
            break MISSING_BLOCK_LABEL_420;
        obj1 = JVM INSTR new #128 <Class PVector>;
        ((PVector) (obj1)).PVector(Float.valueOf(obj[1]).floatValue(), 1.0F - Float.valueOf(obj[2]).floatValue());
        arraylist4.add(obj1);
        boolean flag9 = true;
        boolean flag2 = flag;
        flag = flag1;
        j = i;
        i = ((flag9) ? 1 : 0);
        flag1 = flag2;
          goto _L10
        if(!obj[0].equals("o"))
            break MISSING_BLOCK_LABEL_460;
        int l1 = j;
        boolean flag3 = flag;
        flag = flag1;
        j = i;
        i = l1;
        flag1 = flag3;
          goto _L10
        if(!obj[0].equals("mtllib")) goto _L12; else goto _L11
_L11:
        if(obj[1] == null) goto _L7; else goto _L13
_L13:
        obj1 = obj[1];
        obj = obj1;
        if(((String) (obj1)).indexOf(File.separator) != -1)
            break MISSING_BLOCK_LABEL_547;
        obj = obj1;
        if(!s.equals(""))
        {
            obj = JVM INSTR new #228 <Class StringBuilder>;
            ((StringBuilder) (obj)).StringBuilder();
            obj = ((StringBuilder) (obj)).append(s).append(File.separator).append(((String) (obj1))).toString();
        }
        obj1 = papplet.createReader(((String) (obj)));
        if(obj1 == null)
            break MISSING_BLOCK_LABEL_578;
        parseMTL(papplet, ((String) (obj)), s, ((BufferedReader) (obj1)), arraylist1, hashmap);
        ((BufferedReader) (obj1)).close();
        boolean flag4 = flag1;
        int i2 = i;
        i = j;
        flag1 = flag;
        flag = flag4;
        j = i2;
          goto _L10
_L12:
        if(!obj[0].equals("g"))
            break MISSING_BLOCK_LABEL_665;
        int j1;
        boolean flag10;
        if(1 < obj.length)
            s1 = obj[1];
        else
            s1 = "";
        flag10 = flag1;
        j1 = i;
        i = j;
        flag1 = flag;
        flag = flag10;
        j = j1;
          goto _L10
        if(!obj[0].equals("usemtl")) goto _L15; else goto _L14
_L14:
        if(obj[1] == null) goto _L7; else goto _L16
_L16:
        obj = obj[1];
        if(!hashmap.containsKey(obj))
            break MISSING_BLOCK_LABEL_747;
        i = ((Integer)hashmap.get(obj)).intValue();
_L17:
        int j2 = j;
        boolean flag5 = flag1;
        j = i;
        i = j2;
        flag1 = flag;
        flag = flag5;
          goto _L10
        i = -1;
          goto _L17
_L15:
        if(!obj[0].equals("f")) goto _L7; else goto _L18
_L18:
        obj1 = JVM INSTR new #6   <Class PShapeOBJ$OBJFace>;
        ((OBJFace) (obj1)).OBJFace();
        obj1.matIdx = i;
        obj1.name = s1;
        int k1 = 1;
_L25:
        if(k1 >= obj.length) goto _L20; else goto _L19
_L19:
        s2 = obj[k1];
        if(s2.indexOf("/") <= 0) goto _L22; else goto _L21
_L21:
        s2 = s2.split("/");
        if(s2.length <= 2) goto _L24; else goto _L23
_L23:
        if(s2[0].length() <= 0 || !flag1)
            break MISSING_BLOCK_LABEL_867;
        ((OBJFace) (obj1)).vertIdx.add(Integer.valueOf(s2[0]));
        if(s2[1].length() <= 0 || j == 0)
            break MISSING_BLOCK_LABEL_898;
        ((OBJFace) (obj1)).texIdx.add(Integer.valueOf(s2[1]));
        if(s2[2].length() <= 0 || !flag)
            break MISSING_BLOCK_LABEL_929;
        ((OBJFace) (obj1)).normIdx.add(Integer.valueOf(s2[2]));
_L31:
        k1++;
          goto _L25
_L24:
        if(s2.length <= 1) goto _L27; else goto _L26
_L26:
        if(s2[0].length() <= 0 || !flag1) goto _L29; else goto _L28
_L28:
        ((OBJFace) (obj1)).vertIdx.add(Integer.valueOf(s2[0]));
_L29:
        if(s2[1].length() <= 0) goto _L31; else goto _L30
_L30:
        if(j == 0) goto _L33; else goto _L32
_L32:
        ((OBJFace) (obj1)).texIdx.add(Integer.valueOf(s2[1]));
          goto _L31
        papplet;
        papplet.printStackTrace();
_L38:
        return;
_L33:
        if(!flag) goto _L31; else goto _L34
_L34:
        ((OBJFace) (obj1)).normIdx.add(Integer.valueOf(s2[1]));
          goto _L31
_L27:
        if(s2.length <= 0 || s2[0].length() <= 0 || !flag1) goto _L31; else goto _L35
_L35:
        ((OBJFace) (obj1)).vertIdx.add(Integer.valueOf(s2[0]));
          goto _L31
_L22:
        if(s2.length() <= 0 || !flag1) goto _L31; else goto _L36
_L36:
        ((OBJFace) (obj1)).vertIdx.add(Integer.valueOf(s2));
          goto _L31
_L20:
        arraylist.add(obj1);
_L7:
        int k2 = j;
        boolean flag6 = flag;
        flag = flag1;
        j = i;
        i = k2;
        flag1 = flag6;
          goto _L10
_L2:
        if(arraylist1.size() != 0) goto _L38; else goto _L37
_L37:
        papplet = JVM INSTR new #9   <Class PShapeOBJ$OBJMaterial>;
        papplet.OBJMaterial();
        arraylist1.add(papplet);
          goto _L38
    }

    protected static int rgbaValue(PVector pvector)
    {
        return 0xff000000 | (int)(pvector.x * 255F) << 16 | (int)(pvector.y * 255F) << 8 | (int)(pvector.z * 255F);
    }

    protected static int rgbaValue(PVector pvector, float f)
    {
        return (int)(f * 255F) << 24 | (int)(pvector.x * 255F) << 16 | (int)(pvector.y * 255F) << 8 | (int)(pvector.z * 255F);
    }

    protected void addChildren(ArrayList arraylist, ArrayList arraylist1, ArrayList arraylist2, ArrayList arraylist3, ArrayList arraylist4)
    {
        OBJMaterial objmaterial = null;
        int i = 0;
        int j = -1;
        for(; i < arraylist.size(); i++)
        {
            OBJFace objface = (OBJFace)arraylist.get(i);
            if(j != objface.matIdx || objface.matIdx == -1)
            {
                j = PApplet.max(0, objface.matIdx);
                objmaterial = (OBJMaterial)arraylist1.get(j);
            }
            addChild(new PShapeOBJ(objface, objmaterial, arraylist2, arraylist3, arraylist4));
        }

    }
}
