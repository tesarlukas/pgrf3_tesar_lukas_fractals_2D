#version 450
#define doublePI 6.28318
in vec4 outPosition;

uniform int u_iterations;
uniform float u_zoomLvl;
uniform float u_xOffset;
uniform float u_yOffset;
uniform float u_cReal;
uniform float u_cImag;
uniform int u_gradient;

out vec4 outColor;

float fractalColor = 0.0f;

vec3[5][4] gradients = {
    {
        vec3(0),
        vec3(0),
        vec3(0),
        vec3(0),
    },
    {
        vec3(0.5, 0.5, 0.5),
        vec3(0.5, 0.5, 0.5),
        vec3(1., .7, 0.4),
        vec3(0., 0.15, 0.2)
    },
    {
        vec3(0.5, 0.5, 0.5),
        vec3(0.5, 0.5, 0.5),
        vec3(1., 1., 1.),
        vec3(0.3, 0.2, 0.2)
    },
    {
        vec3(0.5),
        vec3(0.5),
        vec3(1.),
        vec3(0.0, 0.1, 0.2)
    },
    {
        vec3(0.5, 0.5, 0.5),
        vec3(0.5, 0.5, 0.5),
        vec3(2., 1., 0.),
        vec3(0.5, 0.2, 0.25)
    }
};

vec3 gradient(float t, vec3 a, vec3 b, vec3 c, vec3 d ) {
    return a + b*cos( doublePI*(c*t+d) );
}

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

    if (u_gradient > 0) {
        vec3 col = gradient(fract(fractalColor + 0.5), gradients[u_gradient][0], gradients[u_gradient][1],gradients[u_gradient][2],gradients[u_gradient][3]);
        outColor = vec4(col, 1f);
        return;
    }

    outColor = vec4(vec3(fractalColor), 1f);
}
