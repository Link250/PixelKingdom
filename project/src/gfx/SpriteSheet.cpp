#include "SpriteSheet.h"

namespace Pixelverse {

SpriteSheet::SpriteSheet(std::string path, int spriteWidth, int spriteHeight):
		texture(path), spriteWidth(spriteWidth), spriteHeight(spriteHeight), sheetWidth(texture.getWidth()/spriteWidth), sheetHeight(texture.getHeight()/spriteHeight){}

SpriteSheet::~SpriteSheet(){}

} /* namespace Pixelverse */
