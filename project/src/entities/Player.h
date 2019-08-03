#ifndef ENTITIES_PLAYER_H_
#define ENTITIES_PLAYER_H_

#include "Mob.h"
#include "Collision.h"
#include "../gfx/SpriteSheet.h"
#include "../utilities/PhysicConstants.h"
#include "../gui/gamefields/ItemField.h"
#include "../gui/gamefields/BagField.h"
#include "../item/BeltBag.h"
#include "../item/Bag.h"

namespace Pixelverse {

class Player: public Mob{
public:
	Player(vec2 position = {0, 0});
	virtual ~Player();
	virtual void update();
	virtual void render();
	void applyGravity();
	virtual void load();
	virtual void save();
	Hitbox hitbox = {-3, 3, -14, 14};
private:
	std::shared_ptr<SpriteSheet> sprites;
	Inventory<BeltBag> beltBag;
	Inventory<Bag> itemBags;
	bool canJump = true, onGround = true;
	int jumpLength = 0, jumpCooldown = 0, jumpCooldownLength = 10;
	double jumpspeed = 2;
	double jumpMaxLength = 25;
	double walkspeed = 6 * meter / second;
	double accelerationGround = 0.3 * meter / second;
	double slowdownGround = 1.2 * meter / second;
	double accelerationAir = 0.2 * meter / second;
	double slowdownAir = 0.8 * meter / second;
	friend class PlayerController;
};

} /* namespace Pixelverse */

#endif /* ENTITIES_PLAYER_H_ */
