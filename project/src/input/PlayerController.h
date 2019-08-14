#ifndef INPUT_PLAYERCONTROLER_H_
#define INPUT_PLAYERCONTROLER_H_

#include "MouseInputUser.h"
#include "../item/Item.h"
#include "../entities/Player.h"

namespace Pixelverse {

class PlayerController: public MouseInputUser{
private:
	std::shared_ptr<Player> player;
	std::vector<std::shared_ptr<GameField>> inventoryFields;
	bool inventoryOpen;
public:
	std::shared_ptr<Item> mouseItem;

	PlayerController(std::shared_ptr<Player> player);
	virtual ~PlayerController();

	void update();
	shared_ptr<Mouse> useMouseInput(const std::shared_ptr<mouse_input> input);

	const std::shared_ptr<Player> getPlayer();
};

} /* namespace Pixelverse */

#endif /* INPUT_PLAYERCONTROLER_H_ */
