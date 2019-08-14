#ifndef MAP_MAP_H_
#define MAP_MAP_H_

#include "Planet.h"
#include "../entities/Player.h"
#include "../materials/Material.h"
#include "ChunkManager.h"
#include "UpdateManager.h"

namespace Pixelverse {

class Map{
public:
	const static bool FRONT_LAYER = true;
	const static bool BACK_LAYER = false;

	Map();
	virtual ~Map();

	void update();
	bool setMaterialUpdating(coordinate position, bool layer);
	void createMaterialUpdates(coordinate position, bool layer);
	void addMaterialUpdate(coordinate position, bool layer);

	void render();

	std::shared_ptr<Region> getRegion(coordinate position);
	std::shared_ptr<Chunk> getChunk(coordinate position);
	std::shared_ptr<Material> getMaterial(coordinate position, bool layer);
	materialID_t getMaterialID(coordinate position, bool layer);
	void setMaterialID(coordinate position, bool layer, materialID_t id);
	bool placeMaterial(coordinate position, bool layer, materialID_t id, std::shared_ptr<Player> player);
	bool breakMaterial(coordinate position, bool layer, std::shared_ptr<Player> player, const MiningTool *miningTool);

	vec2 getGravity(coordinate position);
	int2 getMaterialGravity(coordinate position);

	ChunkManager chunkManager;
private:
	std::shared_ptr<Planet> planet;
	std::vector<std::shared_ptr<Entity>> entities;
	UpdateManager updates[2];
	std::vector<double> times;
	friend class Game;
};

} /* namespace Pixelverse */

#endif /* MAP_MAP_H_ */
