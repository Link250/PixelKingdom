#ifndef GFX_TEXTURE_H_
#define GFX_TEXTURE_H_

#include "../utilities/glIncludes.h"
#include "../utilities/DataTypes.h"

#include <string>
#include <memory>
#include <functional>
#include <vector>

namespace Pixelverse {

struct color{
	unsigned char r, g, b, a;
};

class Texture{
private:
	GLuint textureID;
	unsigned int width, height;
	void loadTexture(std::vector<unsigned char> *image);
public:
	Texture(std::string path);
	Texture(int2 size, unsigned int topBarHeight = 0);
	Texture(int2 size, color c, std::function<bool(int2 size, int2 pos)> restrictor = [](int2 size, int2 pos) { return true; });
	virtual ~Texture();

	GLuint getTextureID() const;
	unsigned int getWidth() const;
	unsigned int getHeight() const;
};

} /* namespace Pixelverse */

#endif /* GFX_TEXTURE_H_ */
