#ifndef MAP_PLANET_H_
#define MAP_PLANET_H_

#include "Region.h"
#include "../utilities/FastNoise.h"

namespace Pixelverse {

class Planet{
public:
	static const int WIDTH = 32;
	static const int SIZE = WIDTH*WIDTH;
	static const int PXL_HEIGHT = 1000;//WIDTH*Region::WIDTH*Chunk::WIDTH/2;
	static const int surfaceHeight = PXL_HEIGHT*0.7;
	static const int surfaceVariation = PXL_HEIGHT*0.02;
	static const int dirtLayerHeight = PXL_HEIGHT*0.01;
	static FastNoise mainNoise, cellNoise, heatNoise;
	Planet();
	virtual ~Planet();
	std::shared_ptr<Region> getRegion(coordinate position);
	std::shared_ptr<Chunk> getChunk(coordinate position);
	materialID_t getMaterialID(coordinate pixelPos, bool layer);
	void setMaterialID(coordinate pixelPos, bool layer, materialID_t id);
	vec2 getGravity(coordinate pixelPos);
	void loadRegion(coordinate position);
	void loadChunk(coordinate position);
private:
	std::array<std::shared_ptr<Region>, SIZE> regions;
};

} /* namespace Pixelverse */

#endif /* MAP_PLANET_H_ */
