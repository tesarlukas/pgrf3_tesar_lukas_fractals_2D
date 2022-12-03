#version 330
#define PI 3.1415926538
in vec2 inPosition;

uniform mat4 u_View;
uniform mat4 u_Proj;
uniform mat4 u_Transl;
uniform mat4 u_Scale;
uniform mat4 u_Rot;

uniform int u_functionType;
uniform float u_time;

out vec2 texCoords;
out vec3 toLightVector;
out vec4 positionColor;
out vec3 normalVector;
out vec4 objectPosition;


vec3 normal = vec3(0.,0.,.0f);
float nx;
float ny;
float nz;

vec3 getNormal() {
    return vec3(0., 0., 1.);
}

vec3 getTangent() {
    // TODO: implementovat
    return vec3(0);
}


void main() {
    texCoords = inPosition;

    float z = 0.f;
    vec2 pos = vec2(1.f,1.f);
    float R = 0;
    float x;
    float y;
    float azimut;
    float zenith;
    float a;
    float b;
    float u;
    float v;
    vec3 uVec;
    vec3 vVec;

    switch(u_functionType) {
        case 0:
            z = 0.f;
            normal = vec3(0f,0f,1.f);
        break;
        case 1:
            pos = inPosition * 2 - 1;
            z = 0.5 * cos(sqrt(20 * pow(pos.x, 2) + 20 * pow(pos.y, 2)));

            // normal calculation
            u = pos.x;
            v = pos.y;
            nx = -10 * u * sin(sqrt(20 * pow(u, 2) + 20 * pow(v, 2))) / sqrt(20*pow(u, 2) + 20 * pow(v, 2));
            ny = -10 * v * sin(sqrt(20 * pow(u, 2) + 20 * pow(v, 2))) / sqrt(20*pow(u, 2) + 20 * pow(v, 2));
            normal = vec3(-nx, -ny, 1f);
        break;
        case 2:
            pos = inPosition * 5 - 2.5f;
            z = cos(pos.x*pos.y + pow(cos(4 * pos.y), 2)) + sin(pos.y) - 0.4f*pos.x - 0.1f*pow(pos.y,2);

            // normal calculation
            u = pos.x;
            v = pos.y;
            nx = -v*sin(u*v + pow(cos(4*v), 2)) - 0.4;
            ny = -0.2*v + cos(v) - sin(u*v + pow(cos(4*v), 2)) * (-8*cos(4*v)*sin(4*v) + x);
            normal = vec3(-nx, -ny, 1f);
        break;
        case 3:
            pos = inPosition * 5 - 2.5f;
            z = cos(pos.x*pos.y + pow(cos(u_time * 4 * pos.y), 2)) + sin(pos.y) - 0.4f * pos.x - 0.1f * pow(pos.y,2);

            // normal calculation
            u = pos.x;
            v = pos.y;
            nx = -v*sin(u*v + pow(cos(4*v), 2)) - 0.4;
            ny = -0.2*v + cos(v) - sin(u*v + pow(cos(4*v), 2)) * (-8*cos(4*v)*sin(4*v) + x);
            normal = vec3(-nx, -ny, 1f);
        break;
        case 4:
            zenith = inPosition.x * PI;
            azimut = inPosition.y * PI * 2;
            a = 3f;
            b = 4f;

            R = a + cos(b*azimut);
            x = R * sin(zenith) * cos(azimut);
            y = R * sin(zenith) * sin(azimut);
            z = R * cos(zenith);

            // u - zenith, v - azimut
            u = zenith;
            v = azimut;

            uVec = vec3(
                cos(4*v)*cos(v)*cos(u),
                cos(4*v)*sin(v)*cos(u),
                -cos(4*v)*sin(u)
            );
            vVec = vec3(
                sin(u)*(-4*sin(4*v)*cos(v) - sin(v)*cos(4*v)),
                sin(u)*(cos(v)*cos(4*v) - 4 * sin(4*v) * sin(v)),
                -4*sin(4*v)*cos(u)
            );
            normal = cross(uVec, vVec);
        break;
        case 5:
        zenith = inPosition.x * PI * 2;
            azimut = inPosition.y * PI * 2;
            a = 5;
            b = 2;

            x = cos(zenith)*(a + sin(u_time) * b*cos(azimut)) + sin(zenith) ;
            y = sin(zenith)*(a + cos(u_time) * b*cos(azimut)) + cos(azimut);
            z = a*b*sin(azimut);

            // u - zenith, v - azimut
            u = zenith;
            v = azimut;

            uVec = vec3(
                -sin(u)*(5 + 2* sin(u_time)*cos(v))+cos(u),
                cos(u)*(2*cos(u_time)*cos(v)+5),
                0
            );
            vVec = vec3(
                -2*sin(u_time)*cos(u)*sin(v),
                -2*cos(u_time)*sin(u)*sin(v)-sin(v),
                10*cos(v)
            );
            normal = cross(uVec, vVec);
        break;
        case 6:
            zenith = inPosition.x * PI * 2;
            azimut = inPosition.y * PI * 2;
            x = 2f * cos(zenith);
            y = 2f * sin(zenith);
            z = inPosition.y;

            u = zenith;
            v = azimut;

            uVec = vec3(
                -2*sin(u),
                2*cos(u),
                0
            );
            vVec = vec3(
                0,
                0,
                1
            );
            normal = cross(uVec, vVec);
        break;
        case 7:
            zenith = inPosition.x * PI * 2;
            azimut = inPosition.y * PI * 2;
            R = pow(inPosition.x, 2) + pow(inPosition.y, 2);
            x = R * cos(zenith);
            y = R * sin(zenith);
            z = inPosition.y;

            //
            u = zenith;
            v = azimut;

            uVec = vec3(
            pow(inPosition.x, 2) + pow(inPosition.y, 2) * -1 * sin(u),
            pow(inPosition.x, 2) + pow(inPosition.y, 2) * cos(u),
            0
            );
            vVec = vec3(
            0,
            0,
            1
            );
            normal = cross(uVec, vVec);
        break;

    }

    // object position with model and view transformations for cartesian
    objectPosition = u_View * u_Transl * u_Rot * u_Scale * vec4(inPosition, z, 1.f);

    // object position with model and view transformations for spherical with cylindrical
    if (u_functionType > 3) objectPosition = u_View * u_Transl * u_Scale * vec4(x, y, z, 1.f);

    // color according to the position of grid in view coordinates
    positionColor = u_View * u_Transl * vec4(inPosition.x, inPosition.y, 0.f, 1f);


    normalVector = transpose(inverse(mat3(u_View))) * normalize(normal); // view coordinates

    vec3 tangent = mat3(u_View) * getTangent();
    vec3 bitangent = cross(normalize(normalVector), normalize(tangent));
    mat3 tbn = mat3(1);


    gl_Position = u_Proj * objectPosition;
}

