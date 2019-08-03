#version 400

out vec4 gl_FragColor;

in vec2 texCoords;

uniform usampler2D materialID;
uniform sampler2DArray textures;
uniform bool layer;
//uniform sampler2D textures[2];

void main(){
    uint id = texture(materialID, texCoords).r;
    if(id > 0){
        gl_FragColor = texture(textures, vec3(texCoords, id-1));//texture(textures[id-1], texCoords);
        if(!layer)
        	gl_FragColor.rgb *= 0.5;
    }else{
        gl_FragColor.a = 0.0;
    }
}
