#ifndef ITEM_ITEMLIST_SMALLBELTBAG_H_
#define ITEM_ITEMLIST_SMALLBELTBAG_H_

#include "../BeltBag.h"

namespace Pixelverse {

class SmallBeltBag: public BeltBag{
public:
	SmallBeltBag();
	itemID_t getID();
protected:
	std::shared_ptr<Item> newInstance();
private:
	static itemID_t id;
};

} /* namespace Pixelverse */

#endif /* ITEM_ITEMLIST_SMALLBELTBAG_H_ */
