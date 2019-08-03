#include "Texture.h"

#include "../utilities/lodepng.h"
#include <iostream>
#include <fstream>

namespace Pixelverse {

Texture::Texture(std::string path){
	textureID = 0;

	std::vector<unsigned char> image;
	unsigned error = lodepng::decode(image, width, height, "./res/textures/" + path + ".png");

	if (error != 0) {
		std::cout << "when trying to open " << ("\"./res/textures/" + path + ".png\" an error occured ") << error << ": " << lodepng_error_text(error) << std::endl;
	}else{
		std::vector<unsigned char> image2(width * height * 4);
		for (size_t y = 0; y < height; y++)
		for (size_t x = 0; x < width; x++)
		for (size_t c = 0; c < 4; c++) {
			image2[4 * width * y + 4 * x + c] = image[4 * width * y + 4 * x + c];
		}

		glGenTextures(1, &textureID);

		glBindTexture(GL_TEXTURE_2D, textureID);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, &image2[0]);
		glGenerateMipmap(GL_TEXTURE_2D);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
	}
}

Texture::Texture(int2 size, unsigned int topBarHeight){
	textureID = 0;
	width = size.x;
	height = size.y;

	std::vector<unsigned int> image(width * height);
	for (size_t y = 0; y < height; y++)
	for (size_t x = 0; x < width; x++){
		image[width * y + x] = y < (topBarHeight)?
				((x < 2 || y < 2 || x >= width-2 || y >= topBarHeight-2)? 0x404040ff : 0x808080ff):
				((x < 2 || y < 2 || x >= width-2 || y >= height-2)? 0x40404080 : 0x80808040);
	}

	glGenTextures(1, &textureID);

	glBindTexture(GL_TEXTURE_2D, textureID);

	glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_INT_8_8_8_8, &image[0]);
	glGenerateMipmap(GL_TEXTURE_2D);

	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
}

Texture::~Texture(){
	if(textureID != 0){
		glDeleteTextures(1, &textureID);
	}
}

} /* namespace Pixelverse */

/*static GLuint genMaskTexture(unsigned int width, unsigned int height) {
	FastNoise noise;
//	noise.SetCellularDistanceFunction(FastNoise::Euclidean);
//	noise.SetCellularReturnType(FastNoise::CellValue);

	std::vector<unsigned char> image(width * height);
	for (size_t y = 0; y < height; y++)
	for (size_t x = 0; x < width; x++){
		float f = noise.GetSimplex(x, y);
		image[width * y + x] = int((f/2+0.5)*3);
	}

	// Create one OpenGL texture
	GLuint textureID;
	glGenTextures(1, &textureID);

	// "Bind" the newly created texture : all future texture functions will modify this texture
	glBindTexture(GL_TEXTURE_2D, textureID);

	// Give the image to OpenGL
	glTexImage2D(GL_TEXTURE_2D, 0, GL_R8UI, width, height, 0, GL_RED_INTEGER, GL_UNSIGNED_BYTE, &image[0]);

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	return textureID;
}*/

/*static GLuint genTexture(unsigned int width, unsigned int height) {
	FastNoise noise;
//	noise.SetCellularDistanceFunction(FastNoise::Euclidean);
//	noise.SetCellularReturnType(FastNoise::CellValue);
	noise.SetInterp(FastNoise::Hermite);

	std::vector<unsigned char> image(width * height);
	for (size_t y = 0; y < height; y++)
	for (size_t x = 0; x < width; x++){
		float f = noise.GetSimplex(cos(double(x)*M_PI/512.0)*500, sin(double(x)*M_PI/512.0)*500, cos(double(y)*M_PI/512.0)*500, sin(double(y)*M_PI/512.0)*500);
		image[width * y + x] = (unsigned char)(f*128+128);
	}

	// Create one OpenGL texture
	GLuint textureID;
	glGenTextures(1, &textureID);

	// "Bind" the newly created texture : all future texture functions will modify this texture
	glBindTexture(GL_TEXTURE_2D, textureID);

	// Give the image to OpenGL
	glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, width, height, 0, GL_RED, GL_UNSIGNED_BYTE, &image[0]);
	glGenerateMipmap(GL_TEXTURE_2D);

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
	return textureID;
}*/
