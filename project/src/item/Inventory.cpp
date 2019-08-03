#include <iostream>

namespace Pixelverse {
/*
template <typename item_t>
Inventory<item_t>::Inventory(size_t size, std::function<bool(std::shared_ptr<item_t>)> restrictor):
		size(size), items(size), canContain(restrictor){}

template <typename item_t>
std::shared_ptr<item_t> Inventory<item_t>::getItem(size_t index){
	if(indexInBounds(index)){
		return items[index];
	}
	return nullptr;
}

template <typename item_t>
int Inventory<item_t>::collectItem(std::shared_ptr<item_t> item){
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

template <typename item_t>
int Inventory<item_t>::insertItem(size_t index, std::shared_ptr<item_t> item, int amount){
	if(indexInBounds(index) && canContain(item)){
		return items[index]->takeFrom(item, amount);
	}
	return 0;
}

template <typename item_t>
bool Inventory<item_t>::swapItem(size_t index, std::shared_ptr<item_t> *itemPointer){
	if(indexInBounds(index) && canContain(*itemPointer)){
		this->items[index].swap(*itemPointer);
		return true;
	}
	return false;
}

template <typename item_t>
bool Inventory<item_t>::indexInBounds(size_t index){
	return index >= 0 && index < size;
}

template <typename item_t>
const std::shared_ptr<item_t>* Inventory<item_t>::getConstItemPointer(size_t index){
	if(indexInBounds(index))
		return &(items[index]);
	return nullptr;
}
*/
} /* namespace Pixelverse */
