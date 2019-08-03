#include "Fabric.h"

namespace Pixelverse {

itemID_t Fabric::id = Item::registerItem(std::make_shared<Fabric>(), &id);

Fabric::Fabric() : Item("fabric", "Fabric", "A piece of organic Fabric.", ItemType::CraftingMaterial, 99){}

itemID_t Fabric::getID(){return id;}

std::shared_ptr<Item> Fabric::newInstance(){
	return std::make_shared<Fabric>();
}

} /* namespace Pixelverse */
