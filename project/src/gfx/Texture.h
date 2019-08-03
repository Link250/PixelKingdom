#ifndef GFX_TEXTURE_H_
#define GFX_TEXTURE_H_

#include "../utilities/glIncludes.h"
#include "../utilities/DataTypes.h"

#include <string>
#include <memory>

namespace Pixelverse {

class Texture{
public:
	GLuint textureID;
	unsigned int width, height;
	Texture(std::string path);
	Texture(int2 size, unsigned int topBarHeight = 0);
	virtual ~Texture();
};

} /* namespace Pixelverse */

#endif /* GFX_TEXTURE_H_ */
