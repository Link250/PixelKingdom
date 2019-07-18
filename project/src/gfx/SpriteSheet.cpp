#include "SpriteSheet.h"

namespace Pixelverse {

SpriteSheet::SpriteSheet(std::string path, int spriteWidth, int spriteHeight):
		texture(path), spriteWidth(spriteWidth), spriteHeight(spriteHeight), sheetWidth(texture.width/spriteWidth), sheetHeight(texture.height/spriteHeight){}

SpriteSheet::~SpriteSheet(){}

} /* namespace Pixelverse */
