#version 330
in vec2 texCoords;
in vec4 positionColor;
in vec3 normalVector;
in vec4 objectPosition;

uniform float u_ColorR;
uniform int u_colorType;
uniform vec3 u_lightSource;
uniform mat4 u_lightSourceTransl;
uniform mat4 u_View;
uniform vec3 u_ViewPosition;
uniform int u_lightingType;

uniform sampler2D textureBase;
uniform sampler2D textureNormal;

out vec4 outColor;

vec4 ambientColor = vec4(0.1, 0.5, 0.1, 1.);
vec4 diffuseColor = vec4(0.9, 0.9, 0.9, 1.);
vec4 specularColor = vec4(1.f,1.f,1.f,1.f);
vec4 lightPosition;
vec3 toLightVector;
vec3 viewDirection;

void main() {
    vec4 baseColor = vec4(1.f,1.f,1.f,1.f);

    switch (u_colorType) {
        case 0:
        baseColor = vec4(1.f,1.f,1.f,1.f);
        break;
        case 1:
        baseColor = vec4(texCoords.x, texCoords.y, 0.f, 1.f);
        break;
        case 2:
        baseColor = objectPosition;
        break;
        case 3:
        baseColor = vec4(gl_FragCoord.www,1.f);
        break;
        case 4:
        baseColor = texture(textureBase, texCoords);
        break;
        case 5:
        baseColor = vec4(normalVector, 1f);
        break;
        case 6:
        baseColor = texture(textureNormal, texCoords);
        break;
        case 7:
        lightPosition = u_View * vec4(u_lightSource, 1.);
        toLightVector = lightPosition.xyz - objectPosition.xyz;
        float distanceColor = length(toLightVector);
        baseColor = vec4(distanceColor,distanceColor,distanceColor,1f);
        break;
    }
    // ambient
    vec4 ambient = ambientColor;

    // diffuse calc
    lightPosition = u_View * vec4(u_lightSource, 1.);
    toLightVector = lightPosition.xyz - objectPosition.xyz;
    vec3 ld = normalize(toLightVector);
    vec3 nd = normalize(normalVector);
    float NDotL = max(dot(nd, ld), 0.);
    vec4 diffuse = NDotL * diffuseColor;

    // specular calc
    viewDirection = u_ViewPosition - objectPosition.xyz;
    vec3 vd = normalize(viewDirection);
    vec3 reflection = normalize(((2.0 * nd) * NDotL) - ld);
    float RDotV = max( 0.0, dot( reflection, vd));
    vec3 halfVector = normalize(ld+vd);
    float NDotH = max(0.0, dot(nd, halfVector));
    vec4 specular = specularColor * vec4(pow(NDotH, 2*4.f));

    // attenuation
    float dist = length(toLightVector);
    float att = 1f/(1.1f + 1.1f * dist + 1.1f * dist * dist);

    vec4 totalAmbient = ambient*baseColor;
    vec4 totalDiffuse = diffuse*baseColor;
    vec4 totalSpecular = specular*baseColor;

    switch (u_lightingType) {
        case 0:
        // without lighting
        outColor = baseColor;
        break;
        case 1:
        outColor = totalAmbient;
        break;
        case 2:
        outColor = totalDiffuse;
        break;
        case 3:
        outColor = totalSpecular;
        break;
        case 4:
        outColor = (ambient +  diffuse + specular) * baseColor;
        break;
        case 5:
        outColor = totalAmbient + att*(totalDiffuse + totalSpecular);
        break;
    }
}