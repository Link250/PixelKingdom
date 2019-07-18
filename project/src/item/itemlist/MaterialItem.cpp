#include "../Item.h"

namespace Pixelverse {

MaterialItem::MaterialItem(itemID_t id, std::string name, std::string displayName, std::string tooltip):
						Item(id, name, displayName, tooltip, 1, 999){}

} /* namespace Pixelverse */
