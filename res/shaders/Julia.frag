#version 450
in vec4 outPosition;

uniform float u_time;
uniform int u_iterations;
uniform float u_speed;
uniform vec3 u_color;
uniform float u_zoomLvl;
uniform float u_xOffset;
uniform float u_yOffset;
uniform float u_cReal;
uniform float u_cImag;

out vec4 outColor;

//float cReal = 0.37;
//float cImag = -0.370;
float fractalColor = 0.0f;

void main() {
    vec2 normalizedCoord = outPosition.xy;

    float real = normalizedCoord.x * u_zoomLvl + u_xOffset;
    float imag = normalizedCoord.y * u_zoomLvl + u_yOffset;

    for (int i = 0; i < u_iterations; i++) {
        float realTemp = real;
        real = real*real - imag*imag + u_cReal;
        imag = 2*realTemp*imag + u_cImag;
        if(sqrt(real*real + imag*imag) > 4) {
            fractalColor = float(i) / u_iterations;
        }
    }

    outColor = vec4(vec3(fractalColor), 1f);
}
