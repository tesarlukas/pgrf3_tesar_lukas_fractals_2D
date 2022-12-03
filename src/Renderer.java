import lwjglutils.OGLTexture2D;
import lwjglutils.ShaderUtils;
import models.Axes;
import models.LightPoint;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import models.Grid;
import org.lwjgl.opengl.GLUtil;
import transforms.*;

import java.io.IOException;
import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;

public class Renderer extends AbstractRenderer {
    private int basicShader;
    private int axesShader;
    private int lightSourceShader;
    private Grid grid;
    private Axes axes;
    private LightPoint lightPoint;

    private Camera camera;
    private Mat4 projection;
    private OGLTexture2D textureBase;
    private OGLTexture2D textureNormal;
    private boolean mouseButton1;
    private double ox, oy;
    private int loc_uProj;
    private int loc_uView;
    private int loc_uTransl;
    private int triangleMode;

    private Mat4Transl translMat;
    private Mat4Scale scaleMat;
    private float scaleCoef;
    private float translX;
    private float translY;
    private float translZ;

    double time;

    private Mat4Transl lightTranslMat;
    private int loc_uLightSource;
    private float lightTranslX;
    private float lightTranslY;
    private float lightTranslZ;

    private Mat4RotXYZ rotMat;
    private float rotX;
    private float rotY;
    private float rotZ;

    @Override
    public void init() {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_PRIMITIVE_RESTART);
        glPrimitiveRestartIndex(Integer.MAX_VALUE);
        width = 800;
        height = 600;

        camera = new Camera()
                .withPosition(new Vec3D(0.f, 0f, 0f))
                .withAzimuth(2.10)
                .withZenith(-0.53)
                .withFirstPerson(false)
                .withRadius(3);
        projection = new Mat4PerspRH(Math.PI / 3,  height / (float)width, 0.1f, 500.f);

        lightPoint = new LightPoint();

        basicShader = ShaderUtils.loadProgram("/shaders/Basic");
        axesShader = ShaderUtils.loadProgram("/shaders/Axes");
        lightSourceShader = ShaderUtils.loadProgram("/shaders/Light");

        glUseProgram(basicShader);
        loc_uLightSource = glGetUniformLocation(basicShader, "u_lightSource");
        glUniform3fv(loc_uLightSource, lightPoint.getPosition());

        grid = new Grid(40, 40);
        triangleMode = GL_TRIANGLES;
        axes = new Axes();

        translX = 0.f;
        translY = 0.f;
        translZ = 0.1f;
        lightTranslX = lightPoint.getPosition()[0];
        lightTranslY = lightPoint.getPosition()[1];
        lightTranslZ = lightPoint.getPosition()[2];
        scaleCoef = 1.f;

        // Proj for Grid
        glUseProgram(basicShader);
        loc_uProj = glGetUniformLocation(basicShader, "u_Proj");
        glUniformMatrix4fv(loc_uProj, false, projection.floatArray());

        // Proj for Axes
        glUseProgram(axesShader);
        loc_uProj = glGetUniformLocation(axesShader, "u_Proj");
        glUniformMatrix4fv(loc_uProj, false, projection.floatArray());

        // Proj for lightSource point
        glUseProgram(lightSourceShader);
        loc_uProj = glGetUniformLocation(lightSourceShader, "u_Proj");
        glUniformMatrix4fv(loc_uProj, false, projection.floatArray());

        try {
            textureBase = new OGLTexture2D("./textures/bricks.jpg");
            textureNormal = new OGLTexture2D("./textures/bricksn.png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // display is in a while loop
    @Override
    public void display() {
        // View for Axes
        glUseProgram(axesShader);
        loc_uView = glGetUniformLocation(axesShader, "u_View");
        glUniformMatrix4fv(loc_uView, false, camera.getViewMatrix().floatArray());
        axes.getBuffers().draw(GL_LINES, axesShader);

        // Translation for Grid
        glUseProgram(basicShader);
        loc_uTransl = glGetUniformLocation(basicShader, "u_Transl");
        translMat = new Mat4Transl(translX,translY,translZ);
        glUniformMatrix4fv(loc_uTransl, false, translMat.floatArray());

        // Scale for Grid
        int loc_uScale = glGetUniformLocation(basicShader, "u_Scale");
        scaleMat = new Mat4Scale(scaleCoef);
        glUniformMatrix4fv(loc_uScale, false, scaleMat.floatArray());

        // Rotation
        int loc_uRot = glGetUniformLocation(basicShader, "u_Rot");
        rotMat = new Mat4RotXYZ(Math.PI/180 * rotX, Math.PI/180 * rotY, Math.PI/180 * rotZ);
        glUniformMatrix4fv(loc_uRot, false, rotMat.floatArray());

        // camera position
        int loc_uViewPosition = glGetUniformLocation(basicShader, "u_ViewPosition");
        glUniform3f(loc_uViewPosition, (float)camera.getPosition().getX(),(float)camera.getPosition().getY(),(float)camera.getPosition().getZ());

        // Translation matrix for light position
        int loc_uLightSourceTransl = glGetUniformLocation(basicShader, "u_lightSourceTransl");
        lightTranslMat = new Mat4Transl(lightTranslX, lightTranslY, lightTranslZ);
        glUniformMatrix4fv(loc_uLightSourceTransl, false, lightTranslMat.floatArray());

        // time
        time = glfwGetTime();
        glUniform1f(glGetUniformLocation(basicShader, "u_time"), (float)time);

        // controlling position of the light point
        glUseProgram(lightSourceShader);
        loc_uLightSourceTransl = glGetUniformLocation(lightSourceShader, "u_Transl");
        lightTranslMat = new Mat4Transl(lightTranslX, lightTranslY, lightTranslZ);
        glUniformMatrix4fv(loc_uLightSourceTransl, false, lightTranslMat.floatArray());

        // View for LightSourcePoint
        loc_uView = glGetUniformLocation(lightSourceShader, "u_View");
        glUniformMatrix4fv(loc_uView, false, camera.getViewMatrix().floatArray());
        // draw light point
        lightPoint.getBuffers().draw(GL_TRIANGLES, lightSourceShader);

        // View for Grid
        glUseProgram(basicShader);
        loc_uView = glGetUniformLocation(basicShader, "u_View");
        glUniformMatrix4fv(loc_uView, false, camera.getViewMatrix().floatArray());

        textureBase.bind(basicShader, "textureBase", 0);
        textureNormal.bind(basicShader, "textureNormal", 1);
        grid.getBuffers().draw(triangleMode, basicShader);
    }

    private GLFWCursorPosCallback cpCallbacknew = new GLFWCursorPosCallback() {
        @Override
        public void invoke(long window, double x, double y) {
            if (mouseButton1) {
                camera = camera.addAzimuth((double) Math.PI * (ox - x) / 800)
                        .addZenith((double) Math.PI * (oy - y) / 800);
                ox = x;
                oy = y;
            }
        }
    };

    private GLFWMouseButtonCallback mbCallback = new GLFWMouseButtonCallback () {
        @Override
        public void invoke(long window, int button, int action, int mods) {
            mouseButton1 = glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == GLFW_PRESS;

            if (button==GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS){
                mouseButton1 = true;
                DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
                DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
                glfwGetCursorPos(window, xBuffer, yBuffer);
                ox = xBuffer.get(0);
                oy = yBuffer.get(0);
            }

            if (button==GLFW_MOUSE_BUTTON_1 && action == GLFW_RELEASE){
                mouseButton1 = false;
                DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
                DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
                glfwGetCursorPos(window, xBuffer, yBuffer);
                double x = xBuffer.get(0);
                double y = yBuffer.get(0);
                camera = camera.addAzimuth((double) Math.PI * (ox - x) / 800)
                        .addZenith((double) Math.PI * (oy - y) / 800);
                ox = x;
                oy = y;
            }
        }
    };

    private GLFWScrollCallback scrollCallback = new GLFWScrollCallback() {
        @Override
        public void invoke(long window, double dx, double dy) {
            if (dy < 0) {
                camera = camera.mulRadius(1.1f);
            }
            else {
                camera = camera.mulRadius(0.9f);
            }
        }
    };

    private GLFWWindowSizeCallback wsCallback = new GLFWWindowSizeCallback() {
        @Override
        public void invoke(long window, int w, int h) {
            if (w > 0 && h > 0) {
                width = w;
                height = h;

                glViewport(0,0,w,h);
                projection = new Mat4PerspRH(Math.PI / 3, h / (float)w, 0.1f, 50.f);
                // Proj for Grid
                glUseProgram(basicShader);
                loc_uProj = glGetUniformLocation(basicShader, "u_Proj");
                glUniformMatrix4fv(loc_uProj, false, projection.floatArray());

                // Proj for Axes
                glUseProgram(axesShader);
                loc_uProj = glGetUniformLocation(axesShader, "u_Proj");
                glUniformMatrix4fv(loc_uProj, false, projection.floatArray());

                if (textRenderer != null)
                    textRenderer.resize(width, height);
            }
        }
    };

    private GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                // We will detect this in our rendering loop
                glfwSetWindowShouldClose(window, true);

            if(action == GLFW_RELEASE) {
                //fill mode
                if (key == GLFW_KEY_Z) {
                    glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
                }
                //line mode
                if (key == GLFW_KEY_X) {
                    glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
                }
                //point mode
                if (key == GLFW_KEY_C) {
                    glPolygonMode(GL_FRONT_AND_BACK, GL_POINT);
                }
                //toggle between strip and triangles
                if (key == GLFW_KEY_V) {
                    if(triangleMode == GL_TRIANGLE_STRIP) {
                        triangleMode = GL_TRIANGLES;
                        grid = new Grid(40, 40);
                        System.out.println("Topology is now GL_TRIANGLES");
                    } else {
                        triangleMode = GL_TRIANGLE_STRIP;
                        grid = new Grid(40, 40, "strip");
                        System.out.println("Topology is now GL_TRIANGLE_STRIP");
                    }
                }
                if (key == GLFW_KEY_B) {
                    if (projection.getClass().getSimpleName().equals("Mat4PerspRH")) {
                        projection = new Mat4OrthoRH(4.f, 4.f, 0.1f, 100.f);

                        glUseProgram(basicShader);
                        loc_uProj = glGetUniformLocation(basicShader, "u_Proj");
                        glUniformMatrix4fv(loc_uProj, false, projection.floatArray());

                        glUseProgram(axesShader);
                        loc_uProj = glGetUniformLocation(axesShader, "u_Proj");
                        glUniformMatrix4fv(loc_uProj, false, projection.floatArray());
                        return;
                    }
                    projection = new Mat4PerspRH(Math.PI / 3, height / (float)width, 1f, 50.f);
                    // Proj for Grid
                    glUseProgram(basicShader);
                    loc_uProj = glGetUniformLocation(basicShader, "u_Proj");
                    glUniformMatrix4fv(loc_uProj, false, projection.floatArray());

                    // Proj for Axes
                    glUseProgram(axesShader);
                    loc_uProj = glGetUniformLocation(axesShader, "u_Proj");
                    glUniformMatrix4fv(loc_uProj, false, projection.floatArray());
                }

                if (key == GLFW_KEY_GRAVE_ACCENT) {
                    if (mods == GLFW_MOD_SHIFT) {
                        glUseProgram(basicShader);
                        int loc_uFunctionType = glGetUniformLocation(basicShader, "u_functionType");
                        glUniform1i(loc_uFunctionType, Functions.DEFAULT.getValue());
                        return;
                    }
                    if (mods == GLFW_MOD_ALT) {
                        glUseProgram(basicShader);
                        int loc_uLightingType = glGetUniformLocation(basicShader, "u_lightingType");
                        glUniform1i(loc_uLightingType, LightningOptions.WITHOUT.getValue());
                        return;
                    }

                    glUseProgram(basicShader);
                    int loc_uColorType = glGetUniformLocation(basicShader, "u_colorType");
                    glUniform1i(loc_uColorType, ColorOptions.DEFAULT.getValue());
                }
                if (key == GLFW_KEY_1) {
                    if (mods == GLFW_MOD_ALT) {
                        glUseProgram(basicShader);
                        int loc_uLightingType = glGetUniformLocation(basicShader, "u_lightingType");
                        glUniform1i(loc_uLightingType, LightningOptions.AMBIENT.getValue());
                        return;
                    }
                    if (mods == GLFW_MOD_SHIFT) {
                        glUseProgram(basicShader);
                        int loc_uFunctionType = glGetUniformLocation(basicShader, "u_functionType");
                        glUniform1i(loc_uFunctionType, Functions.PRESENTATION.getValue());
                        return;
                    }
                    glUseProgram(basicShader);
                    int loc_uColorType = glGetUniformLocation(basicShader, "u_colorType");
                    glUniform1i(loc_uColorType, ColorOptions.UV.getValue());
                }
                if (key == GLFW_KEY_2) {
                    if (mods == GLFW_MOD_ALT) {
                        glUseProgram(basicShader);
                        int loc_uLightingType = glGetUniformLocation(basicShader, "u_lightingType");
                        glUniform1i(loc_uLightingType, LightningOptions.DIFFUSE.getValue());
                        return;
                    }
                    if (mods == GLFW_MOD_SHIFT) {
                        glUseProgram(basicShader);
                        int loc_uFunctionType = glGetUniformLocation(basicShader, "u_functionType");
                        glUniform1i(loc_uFunctionType, Functions.INTERES.getValue());
                        return;
                    }
                    if (mods == GLFW_MOD_CONTROL) {
                        glUseProgram(basicShader);
                        int loc_uFunctionType = glGetUniformLocation(basicShader, "u_functionType");
                        glUniform1i(loc_uFunctionType, Functions.INTERES_TIME.getValue());
                        return;
                    }
                    glUseProgram(basicShader);
                    int loc_uColorType = glGetUniformLocation(basicShader, "u_colorType");
                    glUniform1i(loc_uColorType, ColorOptions.POSITION.getValue());
                }
                if(key == GLFW_KEY_3) {
                    if (mods == GLFW_MOD_ALT) {
                        glUseProgram(basicShader);
                        int loc_uLightingType = glGetUniformLocation(basicShader, "u_lightingType");
                        glUniform1i(loc_uLightingType, LightningOptions.SPECULAR.getValue());
                        return;
                    }
                    if (mods == GLFW_MOD_SHIFT) {
                        glUseProgram(basicShader);
                        int loc_uFunctionType = glGetUniformLocation(basicShader, "u_functionType");
                        glUniform1i(loc_uFunctionType, Functions.SPHERICAL.getValue());
                        return;
                    }

                    glUseProgram(basicShader);
                    int loc_uColorType = glGetUniformLocation(basicShader, "u_colorType");
                    glUniform1i(loc_uColorType, ColorOptions.DEPTH.getValue());
                }
                if(key == GLFW_KEY_4) {
                    if (mods == GLFW_MOD_ALT) {
                        glUseProgram(basicShader);
                        int loc_uLightingType = glGetUniformLocation(basicShader, "u_lightingType");
                        glUniform1i(loc_uLightingType, LightningOptions.FULL.getValue());
                        return;
                    }
                    if (mods == GLFW_MOD_SHIFT) {
                        glUseProgram(basicShader);
                        int loc_uFunctionType = glGetUniformLocation(basicShader, "u_functionType");
                        glUniform1i(loc_uFunctionType, Functions.SPHERICAL2.getValue());

                        System.out.println(Functions.SPHERICAL2.getValue());
                        return;
                    }

                    glUseProgram(basicShader);
                    int loc_uColorType = glGetUniformLocation(basicShader, "u_colorType");
                    glUniform1i(loc_uColorType, ColorOptions.TEXTURE.getValue());
                }
                if(key == GLFW_KEY_5) {
                    if (mods == GLFW_MOD_ALT) {
                        glUseProgram(basicShader);
                        int loc_uLightingType = glGetUniformLocation(basicShader, "u_lightingType");
                        glUniform1i(loc_uLightingType, LightningOptions.ATTENUATION.getValue());
                        return;
                    }
                    if (mods == GLFW_MOD_SHIFT) {
                        glUseProgram(basicShader);
                        int loc_uFunctionType = glGetUniformLocation(basicShader, "u_functionType");
                        glUniform1i(loc_uFunctionType, Functions.CYLINDR.getValue());
                        return;
                    }
                    glUseProgram(basicShader);
                    int loc_uColorType = glGetUniformLocation(basicShader, "u_colorType");
                    glUniform1i(loc_uColorType, ColorOptions.NORMAL.getValue());
                }
                if(key == GLFW_KEY_6) {
                    if (mods == GLFW_MOD_SHIFT) {
                        glUseProgram(basicShader);
                        int loc_uFunctionType = glGetUniformLocation(basicShader, "u_functionType");
                        glUniform1i(loc_uFunctionType, Functions.CYLINDR2.getValue());
                        return;
                    }
                    glUseProgram(basicShader);
                    int loc_uColorType = glGetUniformLocation(basicShader, "u_colorType");
                    glUniform1i(loc_uColorType, ColorOptions.NORMAL_TEXTURE.getValue());
                }
                if(key == GLFW_KEY_7) {
                    glUseProgram(basicShader);
                    int loc_uColorType = glGetUniformLocation(basicShader, "u_colorType");
                    glUniform1i(loc_uColorType, ColorOptions.LIGHT_DISTANCE.getValue());
                }
            }
            if (action == GLFW_REPEAT || action == GLFW_PRESS) {
                if (key == GLFW_KEY_I) {
                    if (mods == GLFW_MOD_SHIFT) {
                        translY += 0.1f;
                        return;
                    }
                    if (mods == GLFW_MOD_ALT) {
                        scaleCoef += 0.1f;
                        return;
                    }
                    if (mods == GLFW_MOD_CONTROL) {
                        rotX += 10f;
                        return;
                    }
                    translZ += 0.1f;
                }
                if (key == GLFW_KEY_K) {
                    if (mods == GLFW_MOD_SHIFT) {
                        translY -= 0.1f;
                        return;
                    }
                    if (mods == GLFW_MOD_ALT) {
                        scaleCoef -= 0.1f;
                        return;
                    }
                    if (mods == GLFW_MOD_CONTROL) {
                        rotX -= 10f;
                        return;
                    }
                    translZ -= 0.1f;
                }
                if (key == GLFW_KEY_J) {
                    if (mods == GLFW_MOD_SHIFT) {
                        rotZ += 10f;
                        return;
                    }
                    if (mods == GLFW_MOD_CONTROL) {
                        rotY += 10f;
                        return;
                    }
                    translX -= 0.1f;
                }
                if (key == GLFW_KEY_L) {
                    if (mods == GLFW_MOD_SHIFT) {
                        rotZ -= 10f;
                        return;
                    }
                    if (mods == GLFW_MOD_CONTROL) {
                        rotY -= 10f;
                        return;
                    }
                    translX += 0.1f;
                }
                // light
                if (key == GLFW_KEY_UP) {
                    if (mods == GLFW_MOD_SHIFT) {
                        lightTranslY += 0.1f;

                        lightPoint.moveForward(0.1f);
                        glUseProgram(basicShader);
                        loc_uLightSource = glGetUniformLocation(basicShader, "u_lightSource");
                        glUniform3fv(loc_uLightSource, lightPoint.getPosition());
                        return;
                    }
                    lightTranslZ += 0.1f;

                    lightPoint.moveUp(0.1f);
                    glUseProgram(basicShader);
                    loc_uLightSource = glGetUniformLocation(basicShader, "u_lightSource");
                    glUniform3fv(loc_uLightSource, lightPoint.getPosition());
                }
                if (key == GLFW_KEY_DOWN) {
                    if (mods == GLFW_MOD_SHIFT) {
                        lightTranslY -= 0.1f;

                        lightPoint.moveBackward(0.1f);
                        glUseProgram(basicShader);
                        loc_uLightSource = glGetUniformLocation(basicShader, "u_lightSource");
                        glUniform3fv(loc_uLightSource, lightPoint.getPosition());
                        return;
                    }
                    lightTranslZ -= 0.1f;

                    lightPoint.moveDown(0.1f);
                    glUseProgram(basicShader);
                    loc_uLightSource = glGetUniformLocation(basicShader, "u_lightSource");
                    glUniform3fv(loc_uLightSource, lightPoint.getPosition());
                }
                if (key == GLFW_KEY_LEFT) {
                    lightTranslX -= 0.1f;

                    lightPoint.moveLeft(0.1f);
                    glUseProgram(basicShader);
                    loc_uLightSource = glGetUniformLocation(basicShader, "u_lightSource");
                    glUniform3fv(loc_uLightSource, lightPoint.getPosition());
                }
                if (key == GLFW_KEY_RIGHT) {
                    lightTranslX += 0.1f;

                    lightPoint.moveRight(0.1f);
                    glUseProgram(basicShader);
                    loc_uLightSource = glGetUniformLocation(basicShader, "u_lightSource");
                    glUniform3fv(loc_uLightSource, lightPoint.getPosition());
                }
            }

            // camera handling
            if (key == GLFW_KEY_W) {
                camera = camera.forward(0.1f);
            }
            if (key == GLFW_KEY_S) {
                camera = camera.backward(0.1f);
            }
            if (key == GLFW_KEY_A) {
                camera = camera.left(0.1f);
            }
            if (key == GLFW_KEY_D) {
                camera = camera.right(0.1f);
            }
        }
    };

    @Override
    public GLFWKeyCallback getKeyCallback() {
        return keyCallback;
    }

    @Override
    public GLFWScrollCallback getScrollCallback() {
        return scrollCallback;
    }

    @Override
    public GLFWMouseButtonCallback getMouseCallback() {
        return mbCallback;
    }

    @Override
    public GLFWCursorPosCallback getCursorCallback() {
        return cpCallbacknew;
    }

    @Override
    public GLFWWindowSizeCallback getWsCallback() {
        return wsCallback;
    }
}
