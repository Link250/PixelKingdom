#ifndef GFX_MATERIALTEXTURE_H_
#define GFX_MATERIALTEXTURE_H_

#include "../utilities/glIncludes.h"
#include <string>

namespace Pixelverse {

class MaterialTexture{
public:
	GLuint textureID;
	const static unsigned int width = 256, height = 256;
	MaterialTexture();
	virtual ~MaterialTexture();
};

} /* namespace Pixelverse */

#endif /* GFX_MATERIALTEXTURE_H_ */
