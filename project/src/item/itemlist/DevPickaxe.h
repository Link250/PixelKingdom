#ifndef ITEM_ITEMLIST_DEVPICKAXE_H_
#define ITEM_ITEMLIST_DEVPICKAXE_H_

#include "../Pickaxe.h"

namespace Pixelverse {

class DevPickaxe: public Pickaxe{
public:
	DevPickaxe();
	itemID_t getID();
protected:
	std::shared_ptr<Item> newInstance();
private:
	static itemID_t id;
};
} /* namespace Pixelverse */

#endif /* ITEM_ITEMLIST_DEVPICKAXE_H_ */
