#version 120

uniform sampler2D sampler;
uniform int time;
uniform int width;
uniform int height;

const float pie = 3.14159265359;
const int type = -1;

varying vec2 tex_coords;

void main() {
	vec2 coords = tex_coords;
	if(type == 0){//-------------------------------------------------------------------------------------------wiggle
		float rad = length(tex_coords-vec2(0.5,0.5))*100+time*pie/500000000;
		vec2 distortion = vec2(cos(rad),sin(rad));
		coords += distortion/500;
		gl_FragColor = texture2D(sampler, coords);
	}else if(type == 1){//-------------------------------------------------------------------------------------blur
		vec4 color = vec4(0,0,0,0);
		int blur = 10;
			for(float x = -blur; x <= blur; x++){
				for(float y = -blur; y <= blur; y++){
					vec4 coltemp = texture2D(sampler, coords+vec2(x/width,y/height));
					color += coltemp * (blur+blur-(abs(x)+abs(y)));
				}
			}
		gl_FragColor = color/color.a;
	}else if(type == 2){//-------------------------------------------------------------------------------------desert
		float rad = tex_coords.y*100+time*pie/500000000;
		vec2 distortion = vec2(0,cos(rad));
		coords += distortion/5000;
		
		rad = (-tex_coords.y+tex_coords.x)*1000+time*pie/500000000;
		distortion = vec2(cos(rad),cos(rad));
		coords += distortion/4000;
		
		rad = (-tex_coords.y-tex_coords.x)*1000+time*pie/500000000;
		distortion = vec2(cos(rad),cos(rad));
		coords += distortion/4000;
		
		gl_FragColor = texture2D(sampler, coords)* vec4(1.5,1.2,0.8,1);
	}else{//---------------------------------------------------------------------------------------------------default
		gl_FragColor = texture2D(sampler, coords);
//		applyColorBlindness(gl_FragColor, 0.7, 2);
	}
}
