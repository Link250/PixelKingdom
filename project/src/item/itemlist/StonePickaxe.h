#ifndef ITEM_ITEMLIST_STONEPICKAXE_H_
#define ITEM_ITEMLIST_STONEPICKAXE_H_

#include "../Pickaxe.h"

namespace Pixelverse {

class StonePickaxe: public Pickaxe{
public:
	StonePickaxe();
	itemID_t getID();
protected:
	std::shared_ptr<Item> newInstance();
private:
	static itemID_t id;
};

} /* namespace Pixelverse */

#endif /* ITEM_ITEMLIST_STONEPICKAXE_H_ */
