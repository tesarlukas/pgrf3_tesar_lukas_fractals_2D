#version 330
in vec3 inPosition;

uniform mat4 u_Proj;
uniform mat4 u_View;
uniform mat4 u_Transl;

void main() {
    gl_Position = u_Proj * u_View * u_Transl * vec4(inPosition,1f);
}
