import imgui.ImGui;
import imgui.type.ImInt;

public class ImGuiLayer {
//    public static boolean showText = false;
    public static int[] iterations = new int[]{32};
    public static float[] speed = new float[]{0.033f};
    public static float[] color = new float[]{0.0f,0.1f,0.6f};
    public static float[] zoomLvl = new float[]{1.1f};
    private static ImInt fractalType = new ImInt(0);
    private static String[] fractals = new String[]{"Basic", "Julia", "Mandelbrot"};
    public static float[] xOffset = new float[]{0.0f};
    public static float[] yOffset = new float[]{0.0f};
    public static float[] cReal = new float[]{0.37f};
    public static float[] cImag = new float[]{-0.37f};

    public void imgui() {
        if(ImGui.begin("Settings")) {
            ImGui.text("Number of iterations:");
            ImGui.sameLine();
            ImGui.dragInt("## iterations", iterations, 1.f, 0.0f, 20000f);

            ImGui.text("Animation speed");
            ImGui.sameLine();
            ImGui.dragFloat("## speed", speed, 0.001f, 0.0f, 4.0f);

            ImGui.text("Zoom Level");
            ImGui.sameLine();
            ImGui.dragFloat("## zoomLvl", zoomLvl, 0.001f, 0.0f, 4.0f);

            ImGui.colorEdit3("Color", color);

            ImGui.text("Fractal");
            ImGui.sameLine();

            ImGui.listBox("## fractalType", fractalType, fractals);

            ImGui.text("X Offset");
            ImGui.sameLine();
            ImGui.dragFloat("## xOffset", xOffset, 0.001f, -4.0f, 4.0f);

            ImGui.text("Y Offset");
            ImGui.sameLine();
            ImGui.dragFloat("## yOffset", yOffset, 0.001f, -4.0f, 4.0f);

            ImGui.text("Real number");
            ImGui.sameLine();
            ImGui.dragFloat("## cReal", cReal, 0.001f, -4.0f, 4.0f);

            ImGui.text("Imaginary number");
            ImGui.sameLine();
            ImGui.dragFloat("## cImag", cImag, 0.001f, -4.0f, 4.0f);
        }
        ImGui.end();
    }

    public static String getCurrentFractalType() {
        return fractals[fractalType.get()];
    }
}
