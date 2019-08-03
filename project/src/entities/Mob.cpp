#include "Mob.h"

namespace Pixelverse {

Mob::Mob(vec2 position, vec2 velocity, double rotation, double maxHealth, double health) : Entity(position, velocity, rotation), maxHealth(maxHealth), health(health){}

Mob::~Mob(){}

void Mob::load(){
	Entity::load();
	//TODO load maxHealth, health
}

void Mob::save(){
	Entity::save();
	//TODO save maxHealth, health
}

} /* namespace Pixelverse */
