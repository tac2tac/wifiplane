// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.opengl;

import java.net.URL;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;
import processing.core.*;

// Referenced classes of package processing.opengl:
//            PGL, PGraphicsOpenGL, Texture

public class PShader
    implements PConstants
{
    protected static class UniformValue
    {

        static final int FLOAT1 = 4;
        static final int FLOAT1VEC = 12;
        static final int FLOAT2 = 5;
        static final int FLOAT2VEC = 13;
        static final int FLOAT3 = 6;
        static final int FLOAT3VEC = 14;
        static final int FLOAT4 = 7;
        static final int FLOAT4VEC = 15;
        static final int INT1 = 0;
        static final int INT1VEC = 8;
        static final int INT2 = 1;
        static final int INT2VEC = 9;
        static final int INT3 = 2;
        static final int INT3VEC = 10;
        static final int INT4 = 3;
        static final int INT4VEC = 11;
        static final int MAT2 = 16;
        static final int MAT3 = 17;
        static final int MAT4 = 18;
        static final int SAMPLER2D = 19;
        int type;
        Object value;

        UniformValue(int i, Object obj)
        {
            type = i;
            value = obj;
        }
    }


    public PShader()
    {
        uniformValues = null;
        loadedAttributes = false;
        loadedUniforms = false;
        parent = null;
        pgl = null;
        context = -1;
        vertexURL = null;
        fragmentURL = null;
        vertexFilename = null;
        fragmentFilename = null;
        glProgram = 0;
        glVertex = 0;
        glFragment = 0;
        intBuffer = PGL.allocateIntBuffer(1);
        floatBuffer = PGL.allocateFloatBuffer(1);
        bound = false;
        type = -1;
    }

    public PShader(PApplet papplet)
    {
        this();
        parent = papplet;
        primaryPG = (PGraphicsOpenGL)papplet.g;
        pgl = primaryPG.pgl;
        context = pgl.createEmptyContext();
    }

    public PShader(PApplet papplet, String s, String s1)
    {
        uniformValues = null;
        loadedAttributes = false;
        loadedUniforms = false;
        parent = papplet;
        primaryPG = (PGraphicsOpenGL)papplet.g;
        pgl = primaryPG.pgl;
        vertexURL = null;
        fragmentURL = null;
        vertexFilename = s;
        fragmentFilename = s1;
        fragmentShaderSource = pgl.loadFragmentShader(s1);
        vertexShaderSource = pgl.loadVertexShader(s);
        glProgram = 0;
        glVertex = 0;
        glFragment = 0;
        intBuffer = PGL.allocateIntBuffer(1);
        floatBuffer = PGL.allocateFloatBuffer(1);
        int i = getShaderType(vertexShaderSource, -1);
        int j = getShaderType(fragmentShaderSource, -1);
        if(i == -1 && j == -1)
            type = 2;
        else
        if(i == -1)
            type = j;
        else
        if(j == -1)
            type = i;
        else
        if(j == i)
            type = i;
        else
            PGraphics.showWarning("The vertex and fragment shaders have different types");
    }

    public PShader(PApplet papplet, URL url, URL url1)
    {
        uniformValues = null;
        loadedAttributes = false;
        loadedUniforms = false;
        parent = papplet;
        primaryPG = (PGraphicsOpenGL)papplet.g;
        pgl = primaryPG.pgl;
        vertexURL = url;
        fragmentURL = url1;
        vertexFilename = null;
        fragmentFilename = null;
        fragmentShaderSource = pgl.loadFragmentShader(url1);
        vertexShaderSource = pgl.loadVertexShader(url);
        glProgram = 0;
        glVertex = 0;
        glFragment = 0;
        intBuffer = PGL.allocateIntBuffer(1);
        floatBuffer = PGL.allocateFloatBuffer(1);
        int i = getShaderType(vertexShaderSource, -1);
        int j = getShaderType(fragmentShaderSource, -1);
        if(i == -1 && j == -1)
            type = 2;
        else
        if(i == -1)
            type = j;
        else
        if(j == -1)
            type = i;
        else
        if(j == i)
            type = i;
        else
            PGraphics.showWarning("The vertex and fragment shaders have different types");
    }

    public PShader(PApplet papplet, String as[], String as1[])
    {
        uniformValues = null;
        loadedAttributes = false;
        loadedUniforms = false;
        parent = papplet;
        primaryPG = (PGraphicsOpenGL)papplet.g;
        pgl = primaryPG.pgl;
        vertexURL = null;
        fragmentURL = null;
        vertexFilename = null;
        fragmentFilename = null;
        vertexShaderSource = as;
        fragmentShaderSource = as1;
        glProgram = 0;
        glVertex = 0;
        glFragment = 0;
        intBuffer = PGL.allocateIntBuffer(1);
        floatBuffer = PGL.allocateFloatBuffer(1);
        int i = getShaderType(vertexShaderSource, -1);
        int j = getShaderType(fragmentShaderSource, -1);
        if(i == -1 && j == -1)
            type = 2;
        else
        if(i == -1)
            type = j;
        else
        if(j == -1)
            type = i;
        else
        if(j == i)
            type = i;
        else
            PGraphics.showWarning("The vertex and fragment shaders have different types");
    }

    protected static int getShaderType(String as[], int i)
    {
        boolean flag;
        int j;
        flag = false;
        j = 0;
_L1:
        int k;
        if(j >= as.length)
            break MISSING_BLOCK_LABEL_209;
        String s = as[j].trim();
        if(PApplet.match(s, pointShaderAttrRegexp) != null)
            k = ((flag) ? 1 : 0);
        else
        if(PApplet.match(s, lineShaderAttrRegexp) != null)
        {
            k = 1;
        } else
        {
            k = ((flag) ? 1 : 0);
            if(PApplet.match(s, pointShaderDefRegexp) == null)
                if(PApplet.match(s, lineShaderDefRegexp) != null)
                    k = 1;
                else
                if(PApplet.match(s, colorShaderDefRegexp) != null)
                    k = 3;
                else
                if(PApplet.match(s, lightShaderDefRegexp) != null)
                    k = 4;
                else
                if(PApplet.match(s, texShaderDefRegexp) != null)
                    k = 5;
                else
                if(PApplet.match(s, texlightShaderDefRegexp) != null)
                    k = 6;
                else
                if(PApplet.match(s, polyShaderDefRegexp) != null)
                    k = 2;
                else
                if(PApplet.match(s, triShaderAttrRegexp) != null)
                {
                    k = 2;
                } else
                {
label0:
                    {
                        if(PApplet.match(s, quadShaderAttrRegexp) == null)
                            break label0;
                        k = 2;
                    }
                }
        }
_L2:
        return k;
        j++;
          goto _L1
        k = i;
          goto _L2
    }

    protected boolean accessLightAttribs()
    {
        boolean flag;
        if(-1 < ambientLoc || -1 < specularLoc || -1 < emissiveLoc || -1 < shininessLoc)
            flag = true;
        else
            flag = false;
        return flag;
    }

    protected boolean accessNormals()
    {
        boolean flag;
        if(-1 < normalLoc)
            flag = true;
        else
            flag = false;
        return flag;
    }

    protected boolean accessTexCoords()
    {
        boolean flag;
        if(-1 < texCoordLoc)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public void bind()
    {
        init();
        if(!bound)
        {
            pgl.useProgram(glProgram);
            bound = true;
            consumeUniforms();
            bindTextures();
        }
        if(hasType())
            bindTyped();
    }

    protected void bindTextures()
    {
        if(textures != null && texUnits != null)
        {
            for(Iterator iterator = textures.keySet().iterator(); iterator.hasNext();)
            {
                int i = ((Integer)iterator.next()).intValue();
                Texture texture1 = (Texture)textures.get(Integer.valueOf(i));
                Integer integer = (Integer)texUnits.get(Integer.valueOf(i));
                if(integer != null)
                {
                    PGL pgl1 = pgl;
                    int j = PGL.TEXTURE0;
                    pgl1.activeTexture(integer.intValue() + j);
                    texture1.bind();
                } else
                {
                    throw new RuntimeException((new StringBuilder()).append("Cannot find unit for texture ").append(texture1).toString());
                }
            }

        }
    }

    protected void bindTyped()
    {
        if(currentPG == null)
        {
            setRenderer(primaryPG.getCurrentPG());
            loadAttributes();
            loadUniforms();
        }
        setCommonUniforms();
        if(-1 < vertexLoc)
            pgl.enableVertexAttribArray(vertexLoc);
        if(-1 < colorLoc)
            pgl.enableVertexAttribArray(colorLoc);
        if(-1 < texCoordLoc)
            pgl.enableVertexAttribArray(texCoordLoc);
        if(-1 < normalLoc)
            pgl.enableVertexAttribArray(normalLoc);
        if(-1 < normalMatLoc)
        {
            currentPG.updateGLNormal();
            setUniformMatrix(normalMatLoc, currentPG.glNormal);
        }
        if(-1 < ambientLoc)
            pgl.enableVertexAttribArray(ambientLoc);
        if(-1 < specularLoc)
            pgl.enableVertexAttribArray(specularLoc);
        if(-1 < emissiveLoc)
            pgl.enableVertexAttribArray(emissiveLoc);
        if(-1 < shininessLoc)
            pgl.enableVertexAttribArray(shininessLoc);
        int i = currentPG.lightCount;
        setUniformValue(lightCountLoc, i);
        if(i > 0)
        {
            setUniformVector(lightPositionLoc, currentPG.lightPosition, 4, i);
            setUniformVector(lightNormalLoc, currentPG.lightNormal, 3, i);
            setUniformVector(lightAmbientLoc, currentPG.lightAmbient, 3, i);
            setUniformVector(lightDiffuseLoc, currentPG.lightDiffuse, 3, i);
            setUniformVector(lightSpecularLoc, currentPG.lightSpecular, 3, i);
            setUniformVector(lightFalloffLoc, currentPG.lightFalloffCoefficients, 3, i);
            setUniformVector(lightSpotLoc, currentPG.lightSpotParameters, 2, i);
        }
        if(-1 < directionLoc)
            pgl.enableVertexAttribArray(directionLoc);
        if(-1 < offsetLoc)
            pgl.enableVertexAttribArray(offsetLoc);
        if(-1 < perspectiveLoc)
            if(currentPG.getHint(7) && currentPG.nonOrthoProjection())
                setUniformValue(perspectiveLoc, 1);
            else
                setUniformValue(perspectiveLoc, 0);
        if(-1 < scaleLoc)
            if(currentPG.getHint(6))
            {
                setUniformValue(scaleLoc, 1.0F, 1.0F, 1.0F);
            } else
            {
                float f = PGL.STROKE_DISPLACEMENT;
                if(currentPG.orthoProjection())
                    setUniformValue(scaleLoc, 1.0F, 1.0F, f);
                else
                    setUniformValue(scaleLoc, f, f, f);
            }
    }

    public boolean bound()
    {
        return bound;
    }

    protected boolean checkPolyType(int i)
    {
        boolean flag;
        flag = true;
        break MISSING_BLOCK_LABEL_2;
_L2:
        do
            return flag;
        while(getType() == 2 || getType() == i);
        if(i != 6)
            break; /* Loop/switch isn't completed */
        PGraphics.showWarning("Your shader needs to be of TEXLIGHT type to render this geometry properly, using default shader instead.");
_L3:
        flag = false;
        if(true) goto _L2; else goto _L1
_L1:
        if(i == 4)
            PGraphics.showWarning("Your shader needs to be of LIGHT type to render this geometry properly, using default shader instead.");
        else
        if(i == 5)
            PGraphics.showWarning("Your shader needs to be of TEXTURE type to render this geometry properly, using default shader instead.");
        else
        if(i == 3)
            PGraphics.showWarning("Your shader needs to be of COLOR type to render this geometry properly, using default shader instead.");
          goto _L3
        if(true) goto _L2; else goto _L4
_L4:
    }

    protected boolean compile()
    {
        boolean flag = true;
        boolean flag1;
        boolean flag2;
        if(hasVertexShader())
        {
            flag1 = compileVertexShader();
        } else
        {
            PGraphics.showException("Doesn't have a vertex shader");
            flag1 = true;
        }
        if(hasFragmentShader())
        {
            flag2 = compileFragmentShader();
        } else
        {
            PGraphics.showException("Doesn't have a fragment shader");
            flag2 = true;
        }
        if(flag1 && flag2)
            flag1 = flag;
        else
            flag1 = false;
        return flag1;
    }

    protected boolean compileFragmentShader()
    {
        boolean flag = false;
        pgl.shaderSource(glFragment, PApplet.join(fragmentShaderSource, "\n"));
        pgl.compileShader(glFragment);
        pgl.getShaderiv(glFragment, PGL.COMPILE_STATUS, intBuffer);
        boolean flag1;
        if(intBuffer.get(0) == 0)
            flag1 = false;
        else
            flag1 = true;
        if(!flag1)
            PGraphics.showException((new StringBuilder()).append("Cannot compile fragment shader:\n").append(pgl.getShaderInfoLog(glFragment)).toString());
        else
            flag = true;
        return flag;
    }

    protected boolean compileVertexShader()
    {
        boolean flag = false;
        pgl.shaderSource(glVertex, PApplet.join(vertexShaderSource, "\n"));
        pgl.compileShader(glVertex);
        pgl.getShaderiv(glVertex, PGL.COMPILE_STATUS, intBuffer);
        boolean flag1;
        if(intBuffer.get(0) == 0)
            flag1 = false;
        else
            flag1 = true;
        if(!flag1)
            PGraphics.showException((new StringBuilder()).append("Cannot compile vertex shader:\n").append(pgl.getShaderInfoLog(glVertex)).toString());
        else
            flag = true;
        return flag;
    }

    protected void consumeUniforms()
    {
        if(uniformValues != null && uniformValues.size() > 0)
        {
            Iterator iterator = uniformValues.keySet().iterator();
            int i = 0;
            do
            {
                if(!iterator.hasNext())
                    break;
                Integer integer = (Integer)iterator.next();
                int ai[] = (UniformValue)uniformValues.get(integer);
                if(((UniformValue) (ai)).type == 0)
                {
                    ai = (int[])((UniformValue) (ai)).value;
                    pgl.uniform1i(integer.intValue(), ai[0]);
                } else
                if(((UniformValue) (ai)).type == 1)
                {
                    ai = (int[])((UniformValue) (ai)).value;
                    pgl.uniform2i(integer.intValue(), ai[0], ai[1]);
                } else
                if(((UniformValue) (ai)).type == 2)
                {
                    ai = (int[])((UniformValue) (ai)).value;
                    pgl.uniform3i(integer.intValue(), ai[0], ai[1], ai[2]);
                } else
                if(((UniformValue) (ai)).type == 3)
                {
                    ai = (int[])((UniformValue) (ai)).value;
                    pgl.uniform4i(integer.intValue(), ai[0], ai[1], ai[2], ai[3]);
                } else
                if(((UniformValue) (ai)).type == 4)
                {
                    ai = (float[])((UniformValue) (ai)).value;
                    pgl.uniform1f(integer.intValue(), ai[0]);
                } else
                if(((UniformValue) (ai)).type == 5)
                {
                    ai = (float[])((UniformValue) (ai)).value;
                    pgl.uniform2f(integer.intValue(), ai[0], ai[1]);
                } else
                if(((UniformValue) (ai)).type == 6)
                {
                    ai = (float[])((UniformValue) (ai)).value;
                    pgl.uniform3f(integer.intValue(), ai[0], ai[1], ai[2]);
                } else
                if(((UniformValue) (ai)).type == 7)
                {
                    ai = (float[])((UniformValue) (ai)).value;
                    pgl.uniform4f(integer.intValue(), ai[0], ai[1], ai[2], ai[3]);
                } else
                if(((UniformValue) (ai)).type == 8)
                {
                    ai = (int[])((UniformValue) (ai)).value;
                    updateIntBuffer(ai);
                    pgl.uniform1iv(integer.intValue(), ai.length, intBuffer);
                } else
                if(((UniformValue) (ai)).type == 9)
                {
                    ai = (int[])((UniformValue) (ai)).value;
                    updateIntBuffer(ai);
                    pgl.uniform2iv(integer.intValue(), ai.length / 2, intBuffer);
                } else
                if(((UniformValue) (ai)).type == 10)
                {
                    ai = (int[])((UniformValue) (ai)).value;
                    updateIntBuffer(ai);
                    pgl.uniform3iv(integer.intValue(), ai.length / 3, intBuffer);
                } else
                if(((UniformValue) (ai)).type == 11)
                {
                    ai = (int[])((UniformValue) (ai)).value;
                    updateIntBuffer(ai);
                    pgl.uniform4iv(integer.intValue(), ai.length / 4, intBuffer);
                } else
                if(((UniformValue) (ai)).type == 12)
                {
                    ai = (float[])((UniformValue) (ai)).value;
                    updateFloatBuffer(ai);
                    pgl.uniform1fv(integer.intValue(), ai.length, floatBuffer);
                } else
                if(((UniformValue) (ai)).type == 13)
                {
                    ai = (float[])((UniformValue) (ai)).value;
                    updateFloatBuffer(ai);
                    pgl.uniform2fv(integer.intValue(), ai.length / 2, floatBuffer);
                } else
                if(((UniformValue) (ai)).type == 14)
                {
                    ai = (float[])((UniformValue) (ai)).value;
                    updateFloatBuffer(ai);
                    pgl.uniform3fv(integer.intValue(), ai.length / 3, floatBuffer);
                } else
                if(((UniformValue) (ai)).type == 15)
                {
                    ai = (float[])((UniformValue) (ai)).value;
                    updateFloatBuffer(ai);
                    pgl.uniform4fv(integer.intValue(), ai.length / 4, floatBuffer);
                } else
                if(((UniformValue) (ai)).type == 16)
                {
                    updateFloatBuffer((float[])((UniformValue) (ai)).value);
                    pgl.uniformMatrix2fv(integer.intValue(), 1, false, floatBuffer);
                } else
                if(((UniformValue) (ai)).type == 17)
                {
                    updateFloatBuffer((float[])((UniformValue) (ai)).value);
                    pgl.uniformMatrix3fv(integer.intValue(), 1, false, floatBuffer);
                } else
                if(((UniformValue) (ai)).type == 18)
                {
                    updateFloatBuffer((float[])((UniformValue) (ai)).value);
                    pgl.uniformMatrix4fv(integer.intValue(), 1, false, floatBuffer);
                } else
                if(((UniformValue) (ai)).type == 19)
                {
                    Object obj = (PImage)((UniformValue) (ai)).value;
                    obj = currentPG.getTexture(((PImage) (obj)));
                    if(textures == null)
                        textures = new HashMap();
                    textures.put(integer, obj);
                    if(texUnits == null)
                        texUnits = new HashMap();
                    if(texUnits.containsKey(integer))
                    {
                        i = ((Integer)texUnits.get(integer)).intValue();
                        pgl.uniform1i(integer.intValue(), i);
                    } else
                    {
                        texUnits.put(integer, Integer.valueOf(i));
                        pgl.uniform1i(integer.intValue(), i);
                    }
                    i++;
                }
            } while(true);
            uniformValues.clear();
        }
    }

    protected boolean contextIsOutdated()
    {
        boolean flag;
        if(!pgl.contextIsCurrent(context))
            flag = true;
        else
            flag = false;
        if(flag)
            dispose();
        return flag;
    }

    protected void create()
    {
        context = pgl.getCurrentContext();
        glres = new PGraphicsOpenGL.GLResourceShader(this);
    }

    protected void dispose()
    {
        if(glres != null)
        {
            glres.dispose();
            glVertex = 0;
            glFragment = 0;
            glProgram = 0;
            glres = null;
        }
    }

    protected void draw(int i, int j, int k)
    {
        pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, i);
        pgl.drawElements(PGL.TRIANGLES, j, PGL.INDEX_TYPE, PGL.SIZEOF_INDEX * k);
        pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, 0);
    }

    protected int getAttributeLoc(String s)
    {
        init();
        return pgl.getAttribLocation(glProgram, s);
    }

    protected int getLastTexUnit()
    {
        int i;
        if(texUnits == null)
            i = -1;
        else
            i = texUnits.size() - 1;
        return i;
    }

    protected int getType()
    {
        return type;
    }

    protected int getUniformLoc(String s)
    {
        init();
        return pgl.getUniformLocation(glProgram, s);
    }

    protected boolean hasFragmentShader()
    {
        boolean flag;
        if(fragmentShaderSource != null && fragmentShaderSource.length > 0)
            flag = true;
        else
            flag = false;
        return flag;
    }

    protected boolean hasType()
    {
        boolean flag;
        if(type >= 0 && type <= 6)
            flag = true;
        else
            flag = false;
        return flag;
    }

    protected boolean hasVertexShader()
    {
        boolean flag;
        if(vertexShaderSource != null && vertexShaderSource.length > 0)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public void init()
    {
        if(glProgram == 0 || contextIsOutdated())
        {
            create();
            if(compile())
            {
                pgl.attachShader(glProgram, glVertex);
                pgl.attachShader(glProgram, glFragment);
                setup();
                pgl.linkProgram(glProgram);
                validate();
            }
        }
    }

    protected boolean isLineShader()
    {
        boolean flag = true;
        if(type != 1)
            flag = false;
        return flag;
    }

    protected boolean isPointShader()
    {
        boolean flag;
        if(type == 0)
            flag = true;
        else
            flag = false;
        return flag;
    }

    protected boolean isPolyShader()
    {
        boolean flag;
        if(2 <= type && type <= 6)
            flag = true;
        else
            flag = false;
        return flag;
    }

    protected void loadAttributes()
    {
        if(!loadedAttributes)
        {
            vertexLoc = getAttributeLoc("vertex");
            if(vertexLoc == -1)
                vertexLoc = getAttributeLoc("position");
            colorLoc = getAttributeLoc("color");
            texCoordLoc = getAttributeLoc("texCoord");
            normalLoc = getAttributeLoc("normal");
            ambientLoc = getAttributeLoc("ambient");
            specularLoc = getAttributeLoc("specular");
            emissiveLoc = getAttributeLoc("emissive");
            shininessLoc = getAttributeLoc("shininess");
            directionLoc = getAttributeLoc("direction");
            offsetLoc = getAttributeLoc("offset");
            directionLoc = getAttributeLoc("direction");
            offsetLoc = getAttributeLoc("offset");
            loadedAttributes = true;
        }
    }

    protected void loadUniforms()
    {
        if(!loadedUniforms)
        {
            transformMatLoc = getUniformLoc("transform");
            if(transformMatLoc == -1)
                transformMatLoc = getUniformLoc("transformMatrix");
            modelviewMatLoc = getUniformLoc("modelview");
            if(modelviewMatLoc == -1)
                modelviewMatLoc = getUniformLoc("modelviewMatrix");
            projectionMatLoc = getUniformLoc("projection");
            if(projectionMatLoc == -1)
                projectionMatLoc = getUniformLoc("projectionMatrix");
            viewportLoc = getUniformLoc("viewport");
            ppixelsLoc = getUniformLoc("ppixels");
            normalMatLoc = getUniformLoc("normalMatrix");
            lightCountLoc = getUniformLoc("lightCount");
            lightPositionLoc = getUniformLoc("lightPosition");
            lightNormalLoc = getUniformLoc("lightNormal");
            lightAmbientLoc = getUniformLoc("lightAmbient");
            lightDiffuseLoc = getUniformLoc("lightDiffuse");
            lightSpecularLoc = getUniformLoc("lightSpecular");
            lightFalloffLoc = getUniformLoc("lightFalloff");
            lightSpotLoc = getUniformLoc("lightSpot");
            textureLoc = getUniformLoc("texture");
            if(textureLoc == -1)
                textureLoc = getUniformLoc("texMap");
            texMatrixLoc = getUniformLoc("texMatrix");
            texOffsetLoc = getUniformLoc("texOffset");
            perspectiveLoc = getUniformLoc("perspective");
            scaleLoc = getUniformLoc("scale");
            loadedUniforms = true;
        }
    }

    public void set(String s, float f)
    {
        setUniformImpl(s, 4, new float[] {
            f
        });
    }

    public void set(String s, float f, float f1)
    {
        setUniformImpl(s, 5, new float[] {
            f, f1
        });
    }

    public void set(String s, float f, float f1, float f2)
    {
        setUniformImpl(s, 6, new float[] {
            f, f1, f2
        });
    }

    public void set(String s, float f, float f1, float f2, float f3)
    {
        setUniformImpl(s, 7, new float[] {
            f, f1, f2, f3
        });
    }

    public void set(String s, int i)
    {
        setUniformImpl(s, 0, new int[] {
            i
        });
    }

    public void set(String s, int i, int j)
    {
        setUniformImpl(s, 1, new int[] {
            i, j
        });
    }

    public void set(String s, int i, int j, int k)
    {
        setUniformImpl(s, 2, new int[] {
            i, j, k
        });
    }

    public void set(String s, int i, int j, int k, int l)
    {
        setUniformImpl(s, 3, new int[] {
            i, j, k, l
        });
    }

    public void set(String s, PImage pimage)
    {
        setUniformImpl(s, 19, pimage);
    }

    public void set(String s, PMatrix2D pmatrix2d)
    {
        setUniformImpl(s, 16, new float[] {
            pmatrix2d.m00, pmatrix2d.m01, pmatrix2d.m10, pmatrix2d.m11
        });
    }

    public void set(String s, PMatrix3D pmatrix3d)
    {
        set(s, pmatrix3d, false);
    }

    public void set(String s, PMatrix3D pmatrix3d, boolean flag)
    {
        if(flag)
            setUniformImpl(s, 17, new float[] {
                pmatrix3d.m00, pmatrix3d.m01, pmatrix3d.m02, pmatrix3d.m10, pmatrix3d.m11, pmatrix3d.m12, pmatrix3d.m20, pmatrix3d.m21, pmatrix3d.m22
            });
        else
            setUniformImpl(s, 18, new float[] {
                pmatrix3d.m00, pmatrix3d.m01, pmatrix3d.m02, pmatrix3d.m03, pmatrix3d.m10, pmatrix3d.m11, pmatrix3d.m12, pmatrix3d.m13, pmatrix3d.m20, pmatrix3d.m21, 
                pmatrix3d.m22, pmatrix3d.m23, pmatrix3d.m30, pmatrix3d.m31, pmatrix3d.m32, pmatrix3d.m33
            });
    }

    public void set(String s, PVector pvector)
    {
        setUniformImpl(s, 6, new float[] {
            pvector.x, pvector.y, pvector.z
        });
    }

    public void set(String s, boolean flag)
    {
        int i = 1;
        if(!flag)
            i = 0;
        setUniformImpl(s, 0, new int[] {
            i
        });
    }

    public void set(String s, boolean flag, boolean flag1)
    {
        int i = 0;
        int j;
        if(flag)
            j = 1;
        else
            j = 0;
        if(flag1)
            i = 1;
        setUniformImpl(s, 1, new int[] {
            j, i
        });
    }

    public void set(String s, boolean flag, boolean flag1, boolean flag2)
    {
        int i = 1;
        int j;
        int k;
        if(flag)
            j = 1;
        else
            j = 0;
        if(flag1)
            k = 1;
        else
            k = 0;
        if(!flag2)
            i = 0;
        setUniformImpl(s, 2, new int[] {
            j, k, i
        });
    }

    public void set(String s, boolean flag, boolean flag1, boolean flag2, boolean flag3)
    {
        int i = 1;
        int j;
        int k;
        int l;
        if(flag)
            j = 1;
        else
            j = 0;
        if(flag1)
            k = 1;
        else
            k = 0;
        if(flag2)
            l = 1;
        else
            l = 0;
        if(!flag3)
            i = 0;
        setUniformImpl(s, 3, new int[] {
            j, k, l, i
        });
    }

    public void set(String s, float af[])
    {
        set(s, af, 1);
    }

    public void set(String s, float af[], int i)
    {
        if(i == 1)
            setUniformImpl(s, 12, af);
        else
        if(i == 2)
            setUniformImpl(s, 13, af);
        else
        if(i == 3)
            setUniformImpl(s, 14, af);
        else
        if(i == 4)
            setUniformImpl(s, 15, af);
        else
        if(4 < i)
            PGraphics.showWarning("Only up to 4 coordinates per element are supported.");
        else
            PGraphics.showWarning("Wrong number of coordinates: it is negative!");
    }

    public void set(String s, int ai[])
    {
        set(s, ai, 1);
    }

    public void set(String s, int ai[], int i)
    {
        if(i == 1)
            setUniformImpl(s, 8, ai);
        else
        if(i == 2)
            setUniformImpl(s, 9, ai);
        else
        if(i == 3)
            setUniformImpl(s, 10, ai);
        else
        if(i == 4)
            setUniformImpl(s, 11, ai);
        else
        if(4 < i)
            PGraphics.showWarning("Only up to 4 coordinates per element are supported.");
        else
            PGraphics.showWarning("Wrong number of coordinates: it is negative!");
    }

    public void set(String s, boolean aflag[])
    {
        set(s, aflag, 1);
    }

    public void set(String s, boolean aflag[], int i)
    {
        int ai[] = new int[aflag.length];
        int j = 0;
        while(j < aflag.length) 
        {
            int k;
            if(aflag[j])
                k = 1;
            else
                k = 0;
            ai[j] = k;
            j++;
        }
        set(s, ai, i);
    }

    protected void setAmbientAttribute(int i, int j, int k, int l, int i1)
    {
        setAttributeVBO(ambientLoc, i, j, k, true, l, i1);
    }

    protected void setAttributeVBO(int i, int j, int k, int l, boolean flag, int i1, int j1)
    {
        if(-1 < i)
        {
            pgl.bindBuffer(PGL.ARRAY_BUFFER, j);
            pgl.vertexAttribPointer(i, k, l, flag, i1, j1);
        }
    }

    protected void setColorAttribute(int i, int j, int k, int l, int i1)
    {
        setAttributeVBO(colorLoc, i, j, k, true, l, i1);
    }

    protected void setCommonUniforms()
    {
        if(-1 < transformMatLoc)
        {
            currentPG.updateGLProjmodelview();
            setUniformMatrix(transformMatLoc, currentPG.glProjmodelview);
        }
        if(-1 < modelviewMatLoc)
        {
            currentPG.updateGLModelview();
            setUniformMatrix(modelviewMatLoc, currentPG.glModelview);
        }
        if(-1 < projectionMatLoc)
        {
            currentPG.updateGLProjection();
            setUniformMatrix(projectionMatLoc, currentPG.glProjection);
        }
        if(-1 < viewportLoc)
        {
            float f = currentPG.viewport.get(0);
            float f1 = currentPG.viewport.get(1);
            float f2 = currentPG.viewport.get(2);
            float f3 = currentPG.viewport.get(3);
            setUniformValue(viewportLoc, f, f1, f2, f3);
        }
        if(-1 < ppixelsLoc)
        {
            ppixelsUnit = getLastTexUnit() + 1;
            setUniformValue(ppixelsLoc, ppixelsUnit);
            pgl.activeTexture(PGL.TEXTURE0 + ppixelsUnit);
            currentPG.bindFrontTexture();
        } else
        {
            ppixelsUnit = -1;
        }
    }

    protected void setEmissiveAttribute(int i, int j, int k, int l, int i1)
    {
        setAttributeVBO(emissiveLoc, i, j, k, true, l, i1);
    }

    public void setFragmentShader(String s)
    {
        fragmentFilename = s;
        fragmentShaderSource = pgl.loadFragmentShader(s);
    }

    public void setFragmentShader(URL url)
    {
        fragmentURL = url;
        fragmentShaderSource = pgl.loadFragmentShader(url);
    }

    public void setFragmentShader(String as[])
    {
        fragmentShaderSource = as;
    }

    protected void setLineAttribute(int i, int j, int k, int l, int i1)
    {
        setAttributeVBO(directionLoc, i, j, k, false, l, i1);
    }

    protected void setNormalAttribute(int i, int j, int k, int l, int i1)
    {
        setAttributeVBO(normalLoc, i, j, k, false, l, i1);
    }

    protected void setPointAttribute(int i, int j, int k, int l, int i1)
    {
        setAttributeVBO(offsetLoc, i, j, k, false, l, i1);
    }

    protected void setRenderer(PGraphicsOpenGL pgraphicsopengl)
    {
        currentPG = pgraphicsopengl;
    }

    protected void setShininessAttribute(int i, int j, int k, int l, int i1)
    {
        setAttributeVBO(shininessLoc, i, j, k, false, l, i1);
    }

    protected void setSpecularAttribute(int i, int j, int k, int l, int i1)
    {
        setAttributeVBO(specularLoc, i, j, k, true, l, i1);
    }

    protected void setTexcoordAttribute(int i, int j, int k, int l, int i1)
    {
        setAttributeVBO(texCoordLoc, i, j, k, false, l, i1);
    }

    protected void setTexture(Texture texture1)
    {
        texture = texture1;
        float f;
        float f1;
        float f2;
        float f3;
        if(texture1 != null)
        {
            float f4;
            float f5;
            float f6;
            float f7;
            if(texture1.invertedX())
            {
                f = 1.0F;
                f1 = -1F;
            } else
            {
                f = 0.0F;
                f1 = 1.0F;
            }
            if(texture1.invertedY())
            {
                f2 = -1F;
                f3 = 1.0F;
            } else
            {
                f3 = 0.0F;
                f2 = 1.0F;
            }
            f4 = texture1.maxTexcoordU();
            f5 = texture1.maxTexcoordU();
            f6 = texture1.maxTexcoordV();
            f7 = texture1.maxTexcoordV();
            setUniformValue(texOffsetLoc, 1.0F / (float)texture1.width, 1.0F / (float)texture1.height);
            if(-1 < textureLoc)
            {
                int i;
                if(-1 < ppixelsUnit)
                    i = ppixelsUnit + 1;
                else
                    i = getLastTexUnit() + 1;
                texUnit = i;
                setUniformValue(textureLoc, texUnit);
                pgl.activeTexture(PGL.TEXTURE0 + texUnit);
                texture1.bind();
            }
            f7 = f3 * f7;
            f *= f5;
            f1 *= f4;
            f3 = f2 * f6;
            f2 = f;
            f = f7;
        } else
        {
            f = 0.0F;
            f3 = 1.0F;
            f1 = 1.0F;
            f2 = 0.0F;
        }
        if(-1 < texMatrixLoc)
        {
            if(tcmat == null)
                tcmat = new float[16];
            tcmat[0] = f1;
            tcmat[4] = 0.0F;
            tcmat[8] = 0.0F;
            tcmat[12] = f2;
            tcmat[1] = 0.0F;
            tcmat[5] = f3;
            tcmat[9] = 0.0F;
            tcmat[13] = f;
            tcmat[2] = 0.0F;
            tcmat[6] = 0.0F;
            tcmat[10] = 0.0F;
            tcmat[14] = 0.0F;
            tcmat[3] = 0.0F;
            tcmat[7] = 0.0F;
            tcmat[11] = 0.0F;
            tcmat[15] = 0.0F;
            setUniformMatrix(texMatrixLoc, tcmat);
        }
    }

    protected void setType(int i)
    {
        type = i;
    }

    protected void setUniformImpl(String s, int i, Object obj)
    {
        int j = getUniformLoc(s);
        if(-1 < j)
        {
            if(uniformValues == null)
                uniformValues = new HashMap();
            uniformValues.put(Integer.valueOf(j), new UniformValue(i, obj));
        } else
        {
            PGraphics.showWarning((new StringBuilder()).append("The shader doesn't have a uniform called \"").append(s).append("\" OR the uniform was removed during compilation because it was unused.").toString());
        }
    }

    protected void setUniformMatrix(int i, float af[])
    {
        if(-1 >= i) goto _L2; else goto _L1
_L1:
        updateFloatBuffer(af);
        if(af.length != 4) goto _L4; else goto _L3
_L3:
        pgl.uniformMatrix2fv(i, 1, false, floatBuffer);
_L2:
        return;
_L4:
        if(af.length == 9)
            pgl.uniformMatrix3fv(i, 1, false, floatBuffer);
        else
        if(af.length == 16)
            pgl.uniformMatrix4fv(i, 1, false, floatBuffer);
        if(true) goto _L2; else goto _L5
_L5:
    }

    protected void setUniformTex(int i, Texture texture1)
    {
label0:
        {
            if(texUnits != null)
            {
                Integer integer = (Integer)texUnits.get(Integer.valueOf(i));
                if(integer == null)
                    break label0;
                PGL pgl1 = pgl;
                i = PGL.TEXTURE0;
                pgl1.activeTexture(integer.intValue() + i);
                texture1.bind();
            }
            return;
        }
        throw new RuntimeException((new StringBuilder()).append("Cannot find unit for texture ").append(texture1).toString());
    }

    protected void setUniformValue(int i, float f)
    {
        if(-1 < i)
            pgl.uniform1f(i, f);
    }

    protected void setUniformValue(int i, float f, float f1)
    {
        if(-1 < i)
            pgl.uniform2f(i, f, f1);
    }

    protected void setUniformValue(int i, float f, float f1, float f2)
    {
        if(-1 < i)
            pgl.uniform3f(i, f, f1, f2);
    }

    protected void setUniformValue(int i, float f, float f1, float f2, float f3)
    {
        if(-1 < i)
            pgl.uniform4f(i, f, f1, f2, f3);
    }

    protected void setUniformValue(int i, int j)
    {
        if(-1 < i)
            pgl.uniform1i(i, j);
    }

    protected void setUniformValue(int i, int j, int k)
    {
        if(-1 < i)
            pgl.uniform2i(i, j, k);
    }

    protected void setUniformValue(int i, int j, int k, int l)
    {
        if(-1 < i)
            pgl.uniform3i(i, j, k, l);
    }

    protected void setUniformValue(int i, int j, int k, int l, int i1)
    {
        if(-1 < i)
            pgl.uniform4i(i, j, k, l, i1);
    }

    protected void setUniformVector(int i, float af[], int j, int k)
    {
        if(-1 >= i) goto _L2; else goto _L1
_L1:
        updateFloatBuffer(af);
        if(j != 1) goto _L4; else goto _L3
_L3:
        pgl.uniform1fv(i, k, floatBuffer);
_L2:
        return;
_L4:
        if(j == 2)
            pgl.uniform2fv(i, k, floatBuffer);
        else
        if(j == 3)
            pgl.uniform3fv(i, k, floatBuffer);
        else
        if(j == 4)
            pgl.uniform4fv(i, k, floatBuffer);
        if(true) goto _L2; else goto _L5
_L5:
    }

    protected void setUniformVector(int i, int ai[], int j, int k)
    {
        if(-1 >= i) goto _L2; else goto _L1
_L1:
        updateIntBuffer(ai);
        if(j != 1) goto _L4; else goto _L3
_L3:
        pgl.uniform1iv(i, k, intBuffer);
_L2:
        return;
_L4:
        if(j == 2)
            pgl.uniform2iv(i, k, intBuffer);
        else
        if(j == 3)
            pgl.uniform3iv(i, k, intBuffer);
        else
        if(j == 4)
            pgl.uniform3iv(i, k, intBuffer);
        if(true) goto _L2; else goto _L5
_L5:
    }

    protected void setVertexAttribute(int i, int j, int k, int l, int i1)
    {
        setAttributeVBO(vertexLoc, i, j, k, false, l, i1);
    }

    public void setVertexShader(String s)
    {
        vertexFilename = s;
        vertexShaderSource = pgl.loadVertexShader(s);
    }

    public void setVertexShader(URL url)
    {
        vertexURL = url;
        vertexShaderSource = pgl.loadVertexShader(url);
    }

    public void setVertexShader(String as[])
    {
        vertexShaderSource = as;
    }

    protected void setup()
    {
    }

    protected boolean supportLighting()
    {
        boolean flag;
        if(-1 < lightCountLoc || -1 < lightPositionLoc || -1 < lightNormalLoc)
            flag = true;
        else
            flag = false;
        return flag;
    }

    protected boolean supportsTexturing()
    {
        boolean flag;
        if(-1 < textureLoc)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public void unbind()
    {
        if(hasType())
            unbindTyped();
        if(bound)
        {
            unbindTextures();
            pgl.useProgram(0);
            bound = false;
        }
    }

    protected void unbindTextures()
    {
        if(textures != null && texUnits != null)
        {
            for(Iterator iterator = textures.keySet().iterator(); iterator.hasNext();)
            {
                int i = ((Integer)iterator.next()).intValue();
                Texture texture1 = (Texture)textures.get(Integer.valueOf(i));
                Integer integer = (Integer)texUnits.get(Integer.valueOf(i));
                if(integer != null)
                {
                    PGL pgl1 = pgl;
                    int j = PGL.TEXTURE0;
                    pgl1.activeTexture(integer.intValue() + j);
                    texture1.unbind();
                } else
                {
                    throw new RuntimeException((new StringBuilder()).append("Cannot find unit for texture ").append(texture1).toString());
                }
            }

            pgl.activeTexture(PGL.TEXTURE0);
        }
    }

    protected void unbindTyped()
    {
        if(-1 < offsetLoc)
            pgl.disableVertexAttribArray(offsetLoc);
        if(-1 < directionLoc)
            pgl.disableVertexAttribArray(directionLoc);
        if(-1 < textureLoc && texture != null)
        {
            pgl.activeTexture(PGL.TEXTURE0 + texUnit);
            texture.unbind();
            pgl.activeTexture(PGL.TEXTURE0);
            texture = null;
        }
        if(-1 < ambientLoc)
            pgl.disableVertexAttribArray(ambientLoc);
        if(-1 < specularLoc)
            pgl.disableVertexAttribArray(specularLoc);
        if(-1 < emissiveLoc)
            pgl.disableVertexAttribArray(emissiveLoc);
        if(-1 < shininessLoc)
            pgl.disableVertexAttribArray(shininessLoc);
        if(-1 < vertexLoc)
            pgl.disableVertexAttribArray(vertexLoc);
        if(-1 < colorLoc)
            pgl.disableVertexAttribArray(colorLoc);
        if(-1 < texCoordLoc)
            pgl.disableVertexAttribArray(texCoordLoc);
        if(-1 < normalLoc)
            pgl.disableVertexAttribArray(normalLoc);
        if(-1 < ppixelsLoc)
        {
            pgl.enableFBOLayer();
            pgl.activeTexture(PGL.TEXTURE0 + ppixelsUnit);
            currentPG.unbindFrontTexture();
            pgl.activeTexture(PGL.TEXTURE0);
        }
        pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    protected void updateFloatBuffer(float af[])
    {
        floatBuffer = PGL.updateFloatBuffer(floatBuffer, af, false);
    }

    protected void updateIntBuffer(int ai[])
    {
        intBuffer = PGL.updateIntBuffer(intBuffer, ai, false);
    }

    protected void validate()
    {
        boolean flag = false;
        pgl.getProgramiv(glProgram, PGL.LINK_STATUS, intBuffer);
        boolean flag1;
        if(intBuffer.get(0) == 0)
            flag1 = false;
        else
            flag1 = true;
        if(!flag1)
            PGraphics.showException((new StringBuilder()).append("Cannot link shader program:\n").append(pgl.getProgramInfoLog(glProgram)).toString());
        pgl.validateProgram(glProgram);
        pgl.getProgramiv(glProgram, PGL.VALIDATE_STATUS, intBuffer);
        if(intBuffer.get(0) == 0)
            flag1 = flag;
        else
            flag1 = true;
        if(!flag1)
            PGraphics.showException((new StringBuilder()).append("Cannot validate shader program:\n").append(pgl.getProgramInfoLog(glProgram)).toString());
    }

    protected static final int COLOR = 3;
    protected static final int LIGHT = 4;
    protected static final int LINE = 1;
    protected static final int POINT = 0;
    protected static final int POLY = 2;
    protected static final int TEXLIGHT = 6;
    protected static final int TEXTURE = 5;
    protected static String colorShaderDefRegexp = "#define *PROCESSING_COLOR_SHADER";
    protected static String lightShaderDefRegexp = "#define *PROCESSING_LIGHT_SHADER";
    protected static String lineShaderAttrRegexp = "attribute *vec4 *direction";
    protected static String lineShaderDefRegexp = "#define *PROCESSING_LINE_SHADER";
    protected static String pointShaderAttrRegexp = "attribute *vec2 *offset";
    protected static String pointShaderDefRegexp = "#define *PROCESSING_POINT_SHADER";
    protected static String polyShaderDefRegexp = "#define *PROCESSING_POLYGON_SHADER";
    protected static String quadShaderAttrRegexp = "#define *PROCESSING_QUADS_SHADER";
    protected static String texShaderDefRegexp = "#define *PROCESSING_TEXTURE_SHADER";
    protected static String texlightShaderDefRegexp = "#define *PROCESSING_TEXLIGHT_SHADER";
    protected static String triShaderAttrRegexp = "#define *PROCESSING_TRIANGLES_SHADER";
    protected int ambientLoc;
    protected boolean bound;
    protected int colorLoc;
    protected int context;
    protected PGraphicsOpenGL currentPG;
    protected int directionLoc;
    protected int emissiveLoc;
    protected FloatBuffer floatBuffer;
    protected String fragmentFilename;
    protected String fragmentShaderSource[];
    protected URL fragmentURL;
    public int glFragment;
    public int glProgram;
    public int glVertex;
    private PGraphicsOpenGL.GLResourceShader glres;
    protected IntBuffer intBuffer;
    protected int lightAmbientLoc;
    protected int lightCountLoc;
    protected int lightDiffuseLoc;
    protected int lightFalloffLoc;
    protected int lightNormalLoc;
    protected int lightPositionLoc;
    protected int lightSpecularLoc;
    protected int lightSpotLoc;
    protected boolean loadedAttributes;
    protected boolean loadedUniforms;
    protected int modelviewMatLoc;
    protected int normalLoc;
    protected int normalMatLoc;
    protected int offsetLoc;
    protected PApplet parent;
    protected int perspectiveLoc;
    protected PGL pgl;
    protected int ppixelsLoc;
    protected int ppixelsUnit;
    protected PGraphicsOpenGL primaryPG;
    protected int projectionMatLoc;
    protected int scaleLoc;
    protected int shininessLoc;
    protected int specularLoc;
    protected float tcmat[];
    protected int texCoordLoc;
    protected int texMatrixLoc;
    protected int texOffsetLoc;
    protected int texUnit;
    protected HashMap texUnits;
    protected Texture texture;
    protected int textureLoc;
    protected HashMap textures;
    protected int transformMatLoc;
    protected int type;
    protected HashMap uniformValues;
    protected String vertexFilename;
    protected int vertexLoc;
    protected String vertexShaderSource[];
    protected URL vertexURL;
    protected int viewportLoc;

}
