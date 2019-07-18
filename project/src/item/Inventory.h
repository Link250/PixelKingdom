#ifndef ITEM_INVENTORY_H_
#define ITEM_INVENTORY_H_

#include "Item.h"

#include <vector>
#include <memory>
#include <functional>

namespace Pixelverse {

class Inventory{
public:
	Inventory(size_t size,
			std::function<bool(std::shared_ptr<Item>)> restrictor =
					[](std::shared_ptr<Item>) { return true; });
	virtual ~Inventory();

	const size_t size;

	std::shared_ptr<Item> getItem(size_t index);
	std::shared_ptr<Item> takeItem(size_t index);
	int collectItem(std::shared_ptr<Item> item);
	int insertItem(size_t index, std::shared_ptr<Item> item, int amount = INT_MAX);
	bool swapItem(size_t index, std::shared_ptr<Item> item);
	inline bool indexInBounds(size_t index);
private:
	std::vector<std::shared_ptr<Item>> items;
	const std::function<bool(std::shared_ptr<Item>)> canContain;
};

} /* namespace Pixelverse */

#endif /* ITEM_INVENTORY_H_ */
