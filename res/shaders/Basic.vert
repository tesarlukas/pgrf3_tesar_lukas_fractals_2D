#version 450
#define PI 3.1415926538
in vec2 inPosition;

uniform float u_time;

out vec4 outPosition;

void main() {
    outPosition = vec4(inPosition, 0.f, 1.f);
    gl_Position = outPosition;
}

