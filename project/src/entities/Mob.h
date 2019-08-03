#ifndef ENTITIES_MOB_H_
#define ENTITIES_MOB_H_

#include "Entity.h"

namespace Pixelverse {

class Mob: public Entity{
public:
	double maxHealth, health;
	Mob(vec2 position = {0, 0}, vec2 velocity = {0, 0}, double rotation = 0.0, double maxHealth = 100, double health = 100);
	virtual ~Mob();
	virtual void update() = 0;
	virtual void render() = 0;
	virtual void load();
	virtual void save();
};

} /* namespace Pixelverse */

#endif /* ENTITIES_MOB_H_ */
