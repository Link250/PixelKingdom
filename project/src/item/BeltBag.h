#ifndef ITEM_BELTBAG_H_
#define ITEM_BELTBAG_H_

#include "Bag.h"

namespace Pixelverse {

class BeltBag: public Bag{
public:
	BeltBag(std::string name, std::string displayName, std::string tooltip,
		size_t size, std::function<bool(std::shared_ptr<Item>)> restrictor =
				[](std::shared_ptr<Item>) { return true; });
};

} /* namespace Pixelverse */

#endif /* ITEM_BELTBAG_H_ */
