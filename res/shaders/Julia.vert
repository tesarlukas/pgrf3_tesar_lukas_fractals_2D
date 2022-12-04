#version 450
in vec2 inPosition;

out vec4 outPosition;

void main() {
    outPosition = vec4(inPosition, 0.f, 1.f);
    gl_Position = outPosition;
}

