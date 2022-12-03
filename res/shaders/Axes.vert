#version 330
in vec3 inPosition;
in vec3 inColor;

uniform mat4 u_View;
uniform mat4 u_Proj;

out vec4 o_Color;

void main() {
    gl_Position =  u_Proj * u_View * vec4(inPosition, 1.f);
    o_Color = vec4(inColor, 1.f);
}
