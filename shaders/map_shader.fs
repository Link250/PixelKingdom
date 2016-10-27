#version 120

uniform sampler2D sampler;
uniform int layer;

varying vec2 tex_coords;

void main() {
	gl_FragColor = layer==0 ? texture2D(sampler, tex_coords) * vec4(0.6,0.6,0.6,1.0) : texture2D(sampler, tex_coords);
}