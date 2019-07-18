#version 400

out vec4 gl_FragColor;

in vec2 texCoords;

uniform sampler2D mainTex;

void main(){
    gl_FragColor = texture(mainTex, texCoords);
}
