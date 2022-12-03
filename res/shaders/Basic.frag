#version 450
in vec4 outPosition;

uniform float u_time;

out vec4 outColor;

void main() {
//    outColor = outPosition;
    vec2 normalizedCoord = outPosition.xy;

//    float distance = length(normalizedCoord);
//    outColor = vec4(distance, distance, distance, 1.f);

    float angle = u_time * 0.1;
    for (float i = 0.0; i < 32.0; i+= 1.0) {
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