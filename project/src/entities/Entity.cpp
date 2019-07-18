#include "Entity.h"

#include "../main/Game.h"

namespace Pixelverse {

Entity::Entity(vec2 position, vec2 velocity, double rotation) : position(position), velocity(velocity), rotation(rotation){}

Entity::~Entity() {}

void Entity::applyGravity(){
	vec2 temp = Game::map->getGravity(coordinate(position));
	velocity += temp;
}

} /* namespace Game */
