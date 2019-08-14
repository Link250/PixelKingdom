#include "Bag.h"

namespace Pixelverse {

Bag::Bag(std::string name, std::string displayName, std::string tooltip,
	size_t size, std::function<bool(std::shared_ptr<Item>)> restrictor):
			Item(name, displayName, tooltip, ItemType::Equipment, 1){
	inventory = std::make_shared<Inventory<Item>>(size, restrictor);
}

/*Bag::Bag(const Bag &original):Item(name, displayName, tooltip, 1, 1), inventory(inventory){

}*/

void Bag::update(){
	inventory->update();
}

} /* namespace Pixelverse */
