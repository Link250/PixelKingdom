#ifndef GFX_TEXTURE_H_
#define GFX_TEXTURE_H_

#include "../utilities/glIncludes.h"
#include <string>

namespace Pixelverse {

class Texture{
public:
	GLuint textureID;
	unsigned int width, height;
	Texture(std::string path);
	virtual ~Texture();
};

} /* namespace Pixelverse */

#endif /* GFX_TEXTURE_H_ */
