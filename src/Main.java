import imgui.ImGui;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {
    // The window handle
    private Window window;

    private AbstractRenderer renderer;

    public Main(AbstractRenderer renderer) {
        this.window = new Window(new ImGuiLayer());
        this.renderer = renderer;
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window.getWindowPtr());
        glfwDestroyWindow(window.getWindowPtr());

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Create the window
        window.init();
        if ( window.getWindowPtr() == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window.getWindowPtr(), renderer.getKeyCallback());
        glfwSetWindowSizeCallback(window.getWindowPtr(),renderer.getWsCallback());
        glfwSetMouseButtonCallback(window.getWindowPtr(),renderer.getMouseCallback());
        glfwSetCursorPosCallback(window.getWindowPtr(),renderer.getCursorCallback());
        glfwSetScrollCallback(window.getWindowPtr(),renderer.getScrollCallback());
    }

    private void loop() {
        // Set the clear color
        glClearColor(.1f, .1f, .1f, 0.0f);

        renderer.init();

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window.getWindowPtr()) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            window.getImGuiGlfw().newFrame();
            ImGui.newFrame();

            window.getImGuiLayer().imgui();
            renderer.display();

            ImGui.render();
            window.getImGuiGl3().renderDrawData(ImGui.getDrawData());

            if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
                final long backupWindowPtr = glfwGetCurrentContext();
                ImGui.updatePlatformWindows();
                ImGui.renderPlatformWindowsDefault();
                glfwMakeContextCurrent(backupWindowPtr);
            }

            glfwMakeContextCurrent(window.getWindowPtr());
            glfwSwapInterval(1);
            glfwShowWindow(window.getWindowPtr());

            glfwSwapBuffers(window.getWindowPtr()); // swap the color buffers
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new Main(new Renderer()).run();
    }
}
