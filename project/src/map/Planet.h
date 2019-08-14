#ifndef MAP_PLANET_H_
#define MAP_PLANET_H_

#include "Region.h"
#include "../noise/FastNoise.h"

namespace Pixelverse {

class Planet{
public:
	static const int WIDTH = 32;
	static const int SIZE = WIDTH*WIDTH;
	static const int PXL_HEIGHT = 2000;//WIDTH*Region::WIDTH*Chunk::WIDTH/2;
	static const int surfaceHeight = PXL_HEIGHT*0.7;
	static const int surfaceVariation = PXL_HEIGHT*0.02;
	static const int dirtLayerHeight = PXL_HEIGHT*0.01;
	static FastNoise mainNoise, cellNoise, heatNoise;

	Planet();
	virtual ~Planet();

	void update();//must be called before setUpdating is used as it will reset the bitflags
	bool setMaterialUpdating(coordinate position, bool layer);

	std::shared_ptr<Region> getRegion(coordinate position);
	std::shared_ptr<Chunk> getChunk(coordinate position);
	materialID_t getMaterialID(coordinate position, bool layer);
	void setMaterialID(coordinate position, bool layer, materialID_t id);
	bool placeMaterial(coordinate position, bool layer, materialID_t id, std::shared_ptr<Player> player);
	bool breakMaterial(coordinate position, bool layer, std::shared_ptr<Player> player, const MiningTool *miningTool);

	vec2 getGravity(coordinate position);
	int2 getMaterialGravity(coordinate position);

	void loadRegion(coordinate position);
	void loadChunk(coordinate position);
private:
	quad_tree<5, std::shared_ptr<Region>> regions;
	//std::array<std::shared_ptr<Region>, SIZE> regions;
};

} /* namespace Pixelverse */

#endif /* MAP_PLANET_H_ */
