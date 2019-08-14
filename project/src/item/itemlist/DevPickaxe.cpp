#include "DevPickaxe.h"

#include <limits>

namespace Pixelverse {

itemID_t DevPickaxe::id = Item::registerItem(std::make_shared<DevPickaxe>(), &id);

DevPickaxe::DevPickaxe(): Pickaxe("dev_pickaxe", "Developer Pickaxe", "Pretty OP", 20, INT_MAX, std::numeric_limits<float>::infinity(), std::numeric_limits<float>::infinity()){}

itemID_t DevPickaxe::getID(){return id;}

std::shared_ptr<Item> DevPickaxe::newInstance(){
	return std::make_shared<DevPickaxe>();
}

} /* namespace Pixelverse */
