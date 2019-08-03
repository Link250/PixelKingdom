#ifndef ITEM_ITEMLIST_SMALLITEMPOUCH_H_
#define ITEM_ITEMLIST_SMALLITEMPOUCH_H_

#include "../bag.h"

namespace Pixelverse {

class SmallItemPouch: public Bag{
public:
	SmallItemPouch();
	itemID_t getID();
protected:
	std::shared_ptr<Item> newInstance();
private:
	static itemID_t id;
};

} /* namespace Pixelverse */

#endif /* ITEM_ITEMLIST_SMALLITEMPOUCH_H_ */
