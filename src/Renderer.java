import lwjglutils.OGLBuffers;
import lwjglutils.ShaderUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import transforms.*;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;

public class Renderer extends AbstractRenderer {
    private int basicShader;
    private Camera camera;
    private boolean mouseButton1;
    private double ox, oy;
    double time;
    private OGLBuffers renderTarget;

    @Override
    public void init() {
        glEnable(GL_DEPTH_TEST);
        //  glEnable(GL_PRIMITIVE_RESTART);
        //  glPrimitiveRestartIndex(Integer.MAX_VALUE);
        width = 800;
        height = 600;

        basicShader = ShaderUtils.loadProgram("/shaders/Basic");
        glUseProgram(basicShader);

        // Vertices
        float[] vertices = {
                -1.0f, -1.f, // 0
                1.0f, -1.0f, // 1
                1.0f, 1.0f, // 2
                -1.0f, 1.0f, // 3
        };

        // Indices
        int[] indices = {0, 1, 2, 0, 2, 3};

        OGLBuffers.Attrib[] attribs = new OGLBuffers.Attrib[] {
                new OGLBuffers.Attrib("inPosition", 2),
        };

        // OGLBuffers
        renderTarget = new OGLBuffers(vertices, attribs, indices);
    }

    // display is in a while loop
    @Override
    public void display() {
        // time
        time = glfwGetTime();
        glUniform1f(glGetUniformLocation(basicShader, "u_time"), (float)time);

        renderTarget.draw(GL_TRIANGLES, basicShader);
    }

    private GLFWCursorPosCallback cpCallbacknew = new GLFWCursorPosCallback() {
        @Override
        public void invoke(long window, double x, double y) {
            if (mouseButton1) {
            }
        }
    };

    private GLFWMouseButtonCallback mbCallback = new GLFWMouseButtonCallback () {
        @Override
        public void invoke(long window, int button, int action, int mods) {
            mouseButton1 = glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == GLFW_PRESS;
        }
    };

    private GLFWScrollCallback scrollCallback = new GLFWScrollCallback() {
        @Override
        public void invoke(long window, double dx, double dy) {

        }
    };

    private GLFWWindowSizeCallback wsCallback = new GLFWWindowSizeCallback() {
        @Override
        public void invoke(long window, int w, int h) {
            if (w > 0 && h > 0) {
                width = w;
                height = h;

                glViewport(0,0,w,h);

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