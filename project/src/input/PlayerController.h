#ifndef INPUT_PLAYERCONTROLER_H_
#define INPUT_PLAYERCONTROLER_H_

#include "MouseInputUser.h"
#include "../item/Item.h"
#include "../entities/Player.h"

namespace Pixelverse {

#define MAX_BUILD_SIZE 20

class PlayerController: public MouseInputUser{
private:
	std::shared_ptr<Player> player;
	std::vector<std::shared_ptr<GameField>> inventoryFields;
	bool inventoryOpen;
	size_t selectedSlot;
	int buildSize;
public:
	std::shared_ptr<Item> mouseItem;

	PlayerController(std::shared_ptr<Player> player);
	virtual ~PlayerController();

	void update();
	shared_ptr<Mouse> useMouseInput(const std::shared_ptr<mouse_input> input);

	const std::shared_ptr<Player> getPlayer();
	size_t getSelectedSlot();
	void changeSelectedSlot(size_t slotChange);
	void setSelectedSlot(size_t slot);
	int getBuildSize();
	void changeBuildSize(int sizeChange);
	void setBuildSize(int size);
};

} /* namespace Pixelverse */

#endif /* INPUT_PLAYERCONTROLER_H_ */
