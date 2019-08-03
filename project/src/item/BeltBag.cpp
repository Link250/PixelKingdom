#include "BeltBag.h"

namespace Pixelverse {

BeltBag::BeltBag(std::string name, std::string displayName, std::string tooltip,
	size_t size, std::function<bool(std::shared_ptr<Item>)> restrictor):
			Bag(name, displayName, tooltip, size, restrictor){}

} /* namespace Pixelverse */
