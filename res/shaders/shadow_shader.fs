#version 120

uniform sampler2D sampler;
uniform sampler2D back;
uniform int width;
uniform int height;
uniform float posx;
uniform float posy;
uniform float blocksize;

varying vec2 tex_coords;

void main() {
	vec2 coords = tex_coords;
	coords.x*=blocksize/width;
	coords.y*=-blocksize/height;
	coords += vec2((posx-blocksize/2)/width, (posy+blocksize/2)/height);
	gl_FragColor = texture2D(back, coords)*texture2D(sampler, tex_coords);
}
