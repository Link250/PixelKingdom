#include "ItemField.h"

#include "../../main/Game.h"
#include <iostream>

namespace Pixelverse {


std::shared_ptr<Texture> ItemField::background = Game::queueRessourceLoader<std::shared_ptr<Texture>>([](){
	background = std::make_shared<Texture>("item/field");
});

ItemField::ItemField(int2 position, std::shared_ptr<Item> item): position(position), item(item){}

ItemField::~ItemField(){}

void ItemField::render(){
	Game::screen->drawGUITexture(background, {double(position.x), double(position.y)}, {1, 1}, 0, false);
	if(item != nullptr){
		item->render({double(position.x+2), double(position.y+2)}, true);
	}
}

} /* namespace Pixelverse */
