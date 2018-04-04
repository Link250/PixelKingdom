#version 120

uniform sampler2D sampler;
uniform vec4 color;

varying vec2 tex_coords;

void main() {
	vec4 v = texture2D(sampler, tex_coords);
	bool useColor = (v == vec4(1.0,0.0,1.0,1.0));
	gl_FragColor = useColor ? color : v;
}