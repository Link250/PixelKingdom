#ifndef GFX_SPRITESHEET_H_
#define GFX_SPRITESHEET_H_

#include "Texture.h"

namespace Pixelverse {

class SpriteSheet{
public:
	SpriteSheet(std::string path, int spriteWidth, int spriteHeight);
	virtual ~SpriteSheet();

	const Texture texture;
	const int spriteWidth, spriteHeight;
	const int sheetWidth, sheetHeight;
private:
};

} /* namespace Pixelverse */

#endif /* GFX_SPRITESHEET_H_ */
