#version 450
#define doublePI 6.28318
in vec4 outPosition;

uniform float u_time;
uniform int u_iterations;
uniform float u_speed;
uniform float u_zoomLvl;
uniform float u_xOffset;
uniform float u_yOffset;
uniform int u_gradient;
uniform bool u_isAutoZoom;
uniform float u_zoomSpeed;

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
    dvec2 normalizedCoord = outPosition.xy;

    dvec2 real = vec2(0);
    dvec2 imag = normalizedCoord * 1.2f + dvec2(-0.6, 0.0);
    imag = imag * u_zoomLvl + dvec2(u_xOffset, u_yOffset);

    if (u_isAutoZoom) {
        imag = imag / pow(u_time, u_zoomSpeed) - dvec2(u_xOffset, u_yOffset);
    }

    float i;
    for (i = 0.; i < u_iterations; i++) {
        real = dmat2(real, -real.y, real.x) * real + imag;
        if (dot(real, real) > 4) break;
    }

    if (i == u_iterations) { i = 0.f; }
    fractalColor = i/u_iterations;

    if (u_gradient > 0) {
        vec3 col = gradient(fract(fractalColor + 0.5), gradients[u_gradient][0], gradients[u_gradient][1],gradients[u_gradient][2],gradients[u_gradient][3]);
        outColor = vec4(col, 1f);
        return;
    }

    outColor = vec4(vec3(fractalColor), 1f);
}