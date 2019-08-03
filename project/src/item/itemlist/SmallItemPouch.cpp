#include "SmallItemPouch.h"

namespace Pixelverse {

itemID_t SmallItemPouch::id = Item::registerItem(std::make_shared<SmallItemPouch>(), &id);

SmallItemPouch::SmallItemPouch():
						Bag("small_item_pouch", "Small Item Pouch", "Nothing special, but good enough for a start", 16){}

itemID_t SmallItemPouch::getID(){return id;}

std::shared_ptr<Item> SmallItemPouch::newInstance(){
	return std::make_shared<SmallItemPouch>();
}

} /* namespace Pixelverse */
