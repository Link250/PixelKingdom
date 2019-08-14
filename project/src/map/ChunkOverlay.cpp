#include "Chunk.h"
#include "Planet.h"

namespace Pixelverse {

void Chunk::bindOverlay(){
	if(textureOverlay == 0){
		unsigned char data[Chunk::WIDTH*Chunk::WIDTH*4];

		coordinate pixelPos;
		for (pixelPos.y = position.y; pixelPos.c_y == position.c_y; pixelPos.y++)
		for (pixelPos.x = position.x; pixelPos.c_x == position.c_x; pixelPos.x++){
			int /*height, */dist;
			vec2 nPos;
			if(abs(pixelPos.x) > abs(pixelPos.y)){
				//height = abs(pixelPos.x);
				dist = abs(pixelPos.y);
				nPos = {std::copysign(Planet::surfaceHeight, pixelPos.x), double(pixelPos.y)};
			}else{
				//height = abs(pixelPos.y);
				dist = abs(pixelPos.x);
				nPos = {double(pixelPos.x), std::copysign(Planet::surfaceHeight, pixelPos.y)};
			}
			nPos /= double(Planet::surfaceHeight);
			double heightScale = std::min((1.0-double(dist)/Planet::surfaceHeight)*4, 1.0);
			double HeightValue = Planet::mainNoise.GetValue(nPos.x, nPos.y)*heightScale;
			double heatValue = Planet::heatNoise.GetValue(nPos.x, nPos.y);
			if (HeightValue <= -0.6) {//Deep Ocean
				if(heatValue <= -0.5){
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4] = 0;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+1] = 194;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+2] = 255;
				}else if(heatValue < 0){
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4] = 0;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+1] = 85;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+2] = 255;
				}else if(heatValue < 0.5){
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4] = 0;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+1] = 0;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+2] = 255;
				}else{
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4] = 74;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+1] = 0;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+2] = 255;
				}
			}else if(HeightValue <= -0.06){//Ocean
				if(heatValue <= -0.5){
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4] = 0;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+1] = 148;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+2] = 255;
				}else if(heatValue < 0){
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4] = 86;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+1] = 142;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+2] = 255;
				}else if(heatValue < 0.6){
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4] = 61;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+1] = 61;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+2] = 255;
				}else{
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4] = 89;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+1] = 70;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+2] = 174;
				}
			}else if(HeightValue < 0.05){//Coast
				if(heatValue <= -0.7){
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4] = 255;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+1] = 240;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+2] = 221;
				}else if(heatValue <= -0.3){
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4] = 255;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+1] = 229;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+2] = 181;
				}else if(heatValue < 0.3){
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4] = 255;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+1] = 216;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+2] = 151;
				}else if(heatValue < 0.7){
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4] = 255;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+1] = 193;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+2] = 94;
				}else{
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4] = 255;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+1] = 184;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+2] = 71;
				}
			}else if(HeightValue < 0.5){//Land
				if(heatValue <= -0.7){
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4] = 124;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+1] = 155;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+2] = 124;
				}else if(heatValue <= -0.3){
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4] = 70;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+1] = 163;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+2] = 74;
				}else if(heatValue < 0.3){
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4] = 0;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+1] = 255;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+2] = 0;
				}else if(heatValue < 0.7){
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4] = 209;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+1] = 255;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+2] = 0;
				}else{
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4] = 255;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+1] = 216;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+2] = 0;
				}
			}else if(HeightValue < 0.8){//Hills
				if(heatValue <= -0.6){
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4] = 91;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+1] = 175;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+2] = 175;
				}else if(heatValue < 0){
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4] = 219;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+1] = 219;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+2] = 219;
				}else if(heatValue < 0.6){
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4] = 181;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+1] = 209;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+2] = 181;
				}else{
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4] = 255;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+1] = 93;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+2] = 0;
				}
			}else{//Mountain
				if(heatValue <= -0.7){
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4] = 0;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+1] = 255;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+2] = 255;
				}else if(heatValue < 0){
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4] = 229;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+1] = 229;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+2] = 229;
				}else if(heatValue < 0.7){
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4] = 181;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+1] = 181;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+2] = 181;
				}else{
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4] = 255;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+1] = 0;
					data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+2] = 0;
				}
			}
			/*if(value >= 0){
				data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4] = int(value*255);
				data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+1] = int((1-value)*255);
				data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+2] = 0;
			}else{
				data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4] = 0;
				data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+1] = int((1+value)*255);
				data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+2] = int((-value)*255);
			}*/
			data[(Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x)*4+3] = 100;
		}

		glGenTextures(1, &textureOverlay);

		glBindTexture(GL_TEXTURE_2D, textureOverlay);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, WIDTH, WIDTH, 0, GL_RGBA, GL_UNSIGNED_BYTE, &data[0]);

	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	}
	glBindTexture(GL_TEXTURE_2D, textureOverlay);
}

} /* namespace Pixelverse */
