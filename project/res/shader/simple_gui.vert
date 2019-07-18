#version 400

in vec2 vPos;
in vec2 vTex;

out vec2 texCoords;

uniform vec2 resolution;
uniform float spriteRotation;
uniform vec2 translation;
uniform vec2 size;

uniform vec2 spriteResolution;
uniform vec2 spriteOffset;

void main(){
	vec2 scaled = vPos*size*0.5f;
	vec2 rotated = vec2(scaled.x*cos(spriteRotation) - scaled.y*sin(spriteRotation),
						scaled.x*sin(spriteRotation) + scaled.y*cos(spriteRotation));
	vec2 translated = rotated+translation-resolution/2.0;
	vec2 viewSpace = (translated/resolution*2.0);
    gl_Position = vec4(viewSpace.x, -viewSpace.y, 0.0, 1.0);
	texCoords = vTex*spriteResolution + spriteOffset*spriteResolution;
}
