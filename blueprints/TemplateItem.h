#ifndef ITEM_ITEMLIST_TEMPLATEITEM_H_
#define ITEM_ITEMLIST_TEMPLATEITEM_H_

#include "../Item.h"

namespace Pixelverse {

class TemplateItem: public Item{
public:
	TemplateItem();
	itemID_t getID();
protected:
	std::shared_ptr<Item> newInstance();
private:
	static itemID_t id;
};

} /* namespace Pixelverse */

#endif /* ITEM_ITEMLIST_TEMPLATEITEM_H_ */