import imgui.ImGui;

public class ImGuiLayer {
    public static boolean showText = false;
    public static int[] iterations = new int[]{32};
    public static float[] speed = new float[]{0.1f};

    public void imgui() {
        if(ImGui.begin("Cool window")) {
            ImGui.dragInt("Number of iterations", iterations, 1, 0);
            ImGui.dragFloat("Animation speed", speed, 0.001f, 0.0f, 4.0f);

            if(ImGui.button("I am a button")){
                showText = true;
            }

            if(showText) {
                ImGui.text("You clicked a button");
                ImGui.sameLine();
                if (ImGui.button("Stop showing text")) {
                    showText = false;
                }
            }
        }
        ImGui.end();
    }
}
