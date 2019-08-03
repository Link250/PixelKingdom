#ifndef ITEM_ITEMLIST_FABRIC_H_
#define ITEM_ITEMLIST_FABRIC_H_

#include "../Item.h"

namespace Pixelverse {

class Fabric: public Item{
public:
	Fabric();
	itemID_t getID();
protected:
	std::shared_ptr<Item> newInstance();
private:
	static itemID_t id;
};

} /* namespace Pixelverse */

#endif /* ITEM_ITEMLIST_FABRIC_H_ */
