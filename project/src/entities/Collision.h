#ifndef ENTITIES_COLLISION_H_
#define ENTITIES_COLLISION_H_

#include "../utilities/DataTypes.h"
#include <memory>
#include <vector>

namespace Pixelverse {

struct Hitbox{
	double xmin, xmax, ymin, ymax;
};

class Collision{
public:
	bool collide_x, collide_y;
	std::vector<coordinate> collisions;
	vec2 entityPos;
	double collisionStrength;
	Collision();
	virtual ~Collision();
	static std::unique_ptr<Collision> canMoveTo(Hitbox hitbox, vec2 position, vec2 movement, double rotation, bool canSlip);
private:
	constexpr static double stepsize = 0.5;
	static bool getCollision(Hitbox hitbox, vec2 newPos, vec2 lastPos);
};

} /* namespace Pixelverse */

#endif /* ENTITIES_COLLISION_H_ */
