#ifndef ITEM_BAG_H_
#define ITEM_BAG_H_

#include "Item.h"
#include "Inventory.h"

namespace Pixelverse {

class Bag: public Item{
public:
	Bag(std::string name, std::string displayName, std::string tooltip,
		size_t size, std::function<bool(std::shared_ptr<Item>)> restrictor =
				[](std::shared_ptr<Item>) { return true; });
//	Bag(const Bag &original);
	std::shared_ptr<Inventory<Item>> inventory;
};

} /* namespace Pixelverse */

#endif /* ITEM_BAG_H_ */
