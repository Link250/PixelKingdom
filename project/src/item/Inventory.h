#ifndef ITEM_INVENTORY_H_
#define ITEM_INVENTORY_H_

#include "Item.h"
#include "../gui/gamefields/ItemField.h"

#include <vector>
#include <memory>
#include <functional>
#include <type_traits>

namespace Pixelverse {

template<typename item_t>
class Inventory{
	static_assert(std::is_base_of_v<Item, item_t>, "Type must inherit from Item");
public:
	Inventory(size_t size,
			std::function<bool(std::shared_ptr<item_t>)> restrictor =
					[](std::shared_ptr<item_t>) { return true; }):
						size(size), items(size), canContain(restrictor){}

	virtual ~Inventory(){}

	const size_t size;

	std::shared_ptr<item_t> getItem(size_t index){
		if(indexInBounds(index)){
			return items[index];
		}
		return nullptr;
	}

	std::shared_ptr<item_t> operator->(){
		return items[0];
	}

	std::shared_ptr<item_t> operator[](size_t index){
		if(indexInBounds(index)){
			return items[index];
		}
		return nullptr;
	}

	std::shared_ptr<item_t> takeItem(size_t index);

	int collectItem(std::shared_ptr<item_t> item){
		if(canContain(item)){
			int taken = 0;
			for (size_t i = 0; i < size; i++) {
				if(items[i] == nullptr){
					items[i].swap(item);
					return taken + items[i]->getStackSize();
				}else{
					taken += items[i]->takeFrom(item);
				}
			}
			return taken;
		}
		return 0;
	}

	int insertItem(size_t index, std::shared_ptr<item_t> item, int amount = INT_MAX){
		if(indexInBounds(index) && canContain(item)){
			return items[index]->takeFrom(item, amount);
		}
		return 0;
	}

	bool swapItem(size_t index, std::shared_ptr<item_t> *itemPointer){
		if(indexInBounds(index) && canContain(*itemPointer)){
			this->items[index].swap(*itemPointer);
			return true;
		}
		return false;
	}

	inline bool indexInBounds(size_t index){
		return index >= 0 && index < size;
	}

	const std::shared_ptr<item_t>* getConstItemPointer(size_t index){
		if(indexInBounds(index))
			return &(items[index]);
		return nullptr;
	}

private:
	std::vector<std::shared_ptr<item_t>> items;
	const std::function<bool(std::shared_ptr<item_t>)> canContain;
};

} /* namespace Pixelverse */

//#include "Inventory.cpp"

#endif /* ITEM_INVENTORY_H_ */
