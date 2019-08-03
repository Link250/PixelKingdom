#include "Font.h"

#include "../main/Game.h"

namespace Pixelverse {

Font::Font(std::string name, std::string chars, int width, int height){
	sprites = std::make_shared<SpriteSheet>("fonts/"+name, width, height);
	for(size_t i = 0; i < chars.length(); i++){
		charlist[chars[i]] = i;
	}
}

Font::~Font(){}

void Font::render(std::string message, vec2 position, int color, int limit, bool centeredX, bool centeredY){
	vec2 offset;
	for(char c : message){
		if(c == '\n'){
			offset.y += sprites->spriteHeight + 2;
			offset.x = 0;
		}else{
			Screen::renderGUISprite(sprites, charlist[c], position + offset);
			offset.x += sprites->spriteWidth;
		}
	}
}

} /* namespace Pixelverse */
