#version 450
in vec4 outPosition;

uniform float u_time;
uniform int u_iterations;
uniform float u_speed;
uniform vec3 u_color;
uniform float u_zoomLvl;

out vec4 outColor;

void main() {
    vec2 normalizedCoord = outPosition.xy;

    float angle = u_time * u_speed;
    for (int i = 0; i < u_iterations; i++) {
        normalizedCoord = abs(normalizedCoord);
        normalizedCoord -= 0.5f;
        normalizedCoord *= u_zoomLvl;
        normalizedCoord *= mat2(
            cos(angle), -sin(angle),
            sin(angle), cos(angle)
        );
    }

    outColor = vec4(length(normalizedCoord + vec2(u_color.r)), length(normalizedCoord + vec2(u_color.g)), length(normalizedCoord + vec2(u_color.b)), 1.f);
}