#include "Mob.h"

namespace Pixelverse {

Mob::Mob(vec2 position, vec2 velocity, double rotation, double maxHealth, double health) : Entity(position, velocity, rotation), maxHealth(maxHealth), health(health){}

Mob::~Mob(){}

} /* namespace Pixelverse */
