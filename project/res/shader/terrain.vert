#version 400

in vec2 vPos;
in vec2 vTex;

out vec2 texCoords;

uniform vec2 resolution;
uniform float spriteRotation;
uniform vec2 translation;
uniform vec2 size;
uniform float screenRotation;

void main(){
	vec2 scaled = vPos*size*0.5f;
	vec2 rotated = vec2(scaled.x*cos(spriteRotation) - scaled.y*sin(spriteRotation),
						scaled.x*sin(spriteRotation) + scaled.y*cos(spriteRotation));
	vec2 translated = rotated+translation;
	vec2 screen_rotated = vec2(translated.x*cos(screenRotation) - translated.y*sin(screenRotation),
							   translated.x*sin(screenRotation) + translated.y*cos(screenRotation));
	vec2 viewSpace = (screen_rotated/resolution*2.0);
    gl_Position = vec4(viewSpace.x, -viewSpace.y, 0.0, 1.0);
	texCoords = vTex;
}
