#include "StonePickaxe.h"

namespace Pixelverse {

itemID_t StonePickaxe::id = Item::registerItem(std::make_shared<StonePickaxe>(), &id);

StonePickaxe::StonePickaxe(): Pickaxe("stone_pickaxe", "Stone Pickaxe", "A primitive Pickaxe made of Stone and Wood", 5, 50, 10, 1){}

itemID_t StonePickaxe::getID(){return id;}

std::shared_ptr<Item> StonePickaxe::newInstance(){
	return std::make_shared<StonePickaxe>();
}

} /* namespace Pixelverse */
