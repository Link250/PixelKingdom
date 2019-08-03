#ifndef GUI_GAMEFIELDS_BAGFIELD_H_
#define GUI_GAMEFIELDS_BAGFIELD_H_

#include "../../item/Inventory.h"
#include "ItemField.h"
#include "GameField.h"

#include <memory>

namespace Pixelverse {

class BagField: public GameField{
private:
	std::shared_ptr<Inventory<Item>> inventory;
	int inventoryWidth;
	std::vector<ItemField> itemFields;

	void placeItemFields();
public:
	BagField(std::shared_ptr<Inventory<Item>> inventory, int inventoryWidth, std::string name);
	virtual ~BagField();

	void render();
	shared_ptr<Mouse> useMouseInput(const std::shared_ptr<mouse_input> input);
};

} /* namespace Pixelverse */

#endif /* GUI_GAMEFIELDS_BAGFIELD_H_ */
