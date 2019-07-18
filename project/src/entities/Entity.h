/*
 * Entity.h
 *
 *  Created on: 08.11.2018
 *      Author: QuantumHero
 */

#ifndef ENTITIES_ENTITY_H_
#define ENTITIES_ENTITY_H_

#include "../utilities/DataTypes.h"

namespace Pixelverse {

class Entity {
public:
	vec2 position, velocity;
	double rotation;
	Entity(vec2 position = {0, 0}, vec2 velocity = {0, 0}, double rotation = 0.0);
	virtual ~Entity();
	virtual void update() = 0;
	virtual void render() = 0;
	virtual void applyGravity();
};

} /* namespace Pixelverse */

#endif /* ENTITIES_ENTITY_H_ */
