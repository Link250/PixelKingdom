#include "Inventory.h"

#include <iostream>

namespace Pixelverse {

Inventory::Inventory(size_t size, std::function<bool(std::shared_ptr<Item>)> restrictor): size(size), items(size), canContain(restrictor){
	std::cout << items.size() << std::endl;
}

Inventory::~Inventory(){}

std::shared_ptr<Item> Inventory::getItem(size_t index){
	if(indexInBounds(index)){
		return items[index];
	}
	return nullptr;
}

int Inventory::collectItem(std::shared_ptr<Item> item){
	if(canContain){
		int taken = 0;
		for (size_t i = 0; i < size; i++) {
			if(items[i] == nullptr){
				items[i] = item;
				return taken + item->getStackSize();
			}else{
				taken += items[i]->takeFrom(item);
			}
		}
		return taken;
	}
	return 0;
}

int Inventory::insertItem(size_t index, std::shared_ptr<Item> item, int amount){
	if(indexInBounds(index) && canContain(item)){
		return items[index]->takeFrom(item, amount);
	}
	return 0;
}

bool Inventory::swapItem(size_t index, std::shared_ptr<Item> item){
	if(indexInBounds(index) && canContain(item)){
		this->items[index].swap(item);
		return true;
	}
	return false;
}

bool Inventory::indexInBounds(size_t index){
	return index >= 0 && index < size;
}

} /* namespace Pixelverse */
