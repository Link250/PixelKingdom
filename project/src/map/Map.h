#ifndef MAP_MAP_H_
#define MAP_MAP_H_

#include "Planet.h"
#include "../entities/Player.h"
#include "../materials/Material.h"
#include "ChunkManager.h"

namespace Pixelverse {

class Map{
public:
	const static bool FRONT_LAYER = true;
	const static bool BACK_LAYER = false;

	Map();
	virtual ~Map();
	void update();
	void render();
	std::shared_ptr<Region> getRegion(coordinate position);
	std::shared_ptr<Chunk> getChunk(coordinate position);
	std::shared_ptr<Material> getMaterial(coordinate pixelPos, bool layer);
	materialID_t getMaterialID(coordinate pixelPos, bool layer);
	void setMaterialID(coordinate pixelPos, bool layer, materialID_t id);
	vec2 getGravity(coordinate pixelPos);
	std::shared_ptr<Player> player;
private:

	std::shared_ptr<Planet> planet;
	std::vector<std::shared_ptr<Entity>> entities;
};

} /* namespace Pixelverse */

#endif /* MAP_MAP_H_ */
