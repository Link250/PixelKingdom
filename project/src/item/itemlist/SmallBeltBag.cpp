#include "SmallBeltBag.h"

namespace Pixelverse {

itemID_t SmallBeltBag::id = Item::registerItem(std::make_shared<SmallBeltBag>(), &id);

SmallBeltBag::SmallBeltBag():
						BeltBag("small_belt_bag", "Small Belt Bag", "A small and simple Belt Bag to quickly access some Items", 8){}

itemID_t SmallBeltBag::getID(){return id;}

std::shared_ptr<Item> SmallBeltBag::newInstance(){
	return std::make_shared<SmallBeltBag>();
}

} /* namespace Pixelverse */
