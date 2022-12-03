import imgui.ImGui;

public class ImGuiLayer {
//    public static boolean showText = false;
    public static int[] iterations = new int[]{32};
    public static float[] speed = new float[]{0.033f};
    public static float[] color = new float[]{0.0f,0.1f,0.6f};
    public static float[] zoomLvl = new float[]{1.1f};


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
            ImGui.dragFloat("## zoomLvl", zoomLvl, 0.001f, .5f, 4.0f);

            ImGui.colorEdit3("Color", color);

//            if(ImGui.button("I am a button")){
//                showText = true;
//            }
//
//            if(showText) {
//                ImGui.text("You clicked a button");
//                ImGui.sameLine();
//                if (ImGui.button("Stop showing text")) {
//                    showText = false;
//                }
//            }
        }
        ImGui.end();
    }
}
