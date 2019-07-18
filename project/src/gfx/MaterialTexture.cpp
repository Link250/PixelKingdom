#include "MaterialTexture.h"

#include "../utilities/lodepng.h"
#include "../materials/Material.h"

#include <iostream>
#include <fstream>

namespace Pixelverse {

MaterialTexture::MaterialTexture(){
	size_t materialAmount = Material::getList().size();

	std::vector<unsigned char> fullTexture((materialAmount-1) * height * width * 4);

	for(size_t m = 1; m < materialAmount; m++){
		std::vector<unsigned char> image;
		unsigned int w, h;
		unsigned error = lodepng::decode(image, w, h, "./res/textures/materials/" + Material::get(m)->name + ".png");
		if (error != 0 || w != width || h != height) {
			std::cout << "error " << error << ": " << lodepng_error_text(error) << std::endl;
		}
		for(size_t i = 0; i < height * width * 4; i++){
			fullTexture[i + (m-1)*height*width*4] = image[i];
		}
	}

	glGenTextures(1, &textureID);

	glBindTexture(GL_TEXTURE_2D_ARRAY, textureID);

	glTexImage3D(GL_TEXTURE_2D_ARRAY, 0, GL_RGBA, width, height, (materialAmount-1), 0, GL_RGBA, GL_UNSIGNED_BYTE, &fullTexture[0]);
//	glGenerateMipmap(GL_TEXTURE_2D_ARRAY);

	glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
	glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
	glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
//	glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
//	glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
}

MaterialTexture::~MaterialTexture(){
	glDeleteTextures(1, &textureID);
}

} /* namespace Pixelverse */
