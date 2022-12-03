#version 450
in vec4 outPosition;

uniform float u_time;
uniform int u_iterations;
uniform float u_speed;

out vec4 outColor;

void main() {
//    outColor = outPosition;
    vec2 normalizedCoord = outPosition.xy;

//    float distance = length(normalizedCoord);
//    outColor = vec4(distance, distance, distance, 1.f);

    float angle = u_time * u_speed;
    for (int i = 0; i < u_iterations; i++) {
        normalizedCoord = abs(normalizedCoord);
        normalizedCoord -= 0.5f;
        normalizedCoord *= 1.1f;
        normalizedCoord *= mat2(
            cos(angle), -sin(angle),
            sin(angle), cos(angle)
        );
    }
    outColor = vec4(length(normalizedCoord + vec2(0.0)), length(normalizedCoord + vec2(0.1)), length(normalizedCoord + vec2(0.6)), 1.f);
}