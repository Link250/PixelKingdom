#include "ItemField.h"

#include "../../main/Game.h"
#include <iostream>

namespace Pixelverse {


std::shared_ptr<Texture> ItemField::background = Game::queueRessourceLoader<std::shared_ptr<Texture>>([](){
	background = std::make_shared<Texture>("item/field");
});

ItemField::ItemField(const std::shared_ptr<Item> *itemPointer, int2 position): position(position), itemPointer(itemPointer){}

ItemField::~ItemField(){}

void ItemField::render(int2 offset){
	Screen::renderGUITexture(background, {double(offset.x + position.x), double(offset.y + position.y)}, {1, 1}, 0, false);
	if(itemPointer != nullptr && (*itemPointer) != nullptr){
		(*itemPointer)->render({double(offset.x + position.x + 2), double(offset.y + position.y+2)});
	}
}

} /* namespace Pixelverse */
