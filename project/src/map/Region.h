#ifndef MAP_REGION_H_
#define MAP_REGION_H_

#include "../utilities/QuadTree.h"
#include "Chunk.h"
#include <array>

namespace Pixelverse {

class Region{
public:
	static const int WIDTH = 256;
	static const int SIZE = WIDTH*WIDTH;
	Region();
	virtual ~Region();

	void update();//must be called before setUpdating is used as it will reset the bitflags
	bool setMaterialUpdating(coordinate position, bool layer);

	std::shared_ptr<Chunk> getChunk(coordinate position);
	materialID_t getMaterialID(coordinate position, bool layer);
	void setMaterialID(coordinate position, bool layer, materialID_t id);
	bool placeMaterial(coordinate position, bool layer, materialID_t id, std::shared_ptr<Player> player);
	bool breakMaterial(coordinate position, bool layer, std::shared_ptr<Player> player, const MiningTool *miningTool);

	void loadChunk(coordinate position);
private:
	quad_tree<8, std::shared_ptr<Chunk>> chunks;
	//std::array<std::shared_ptr<Chunk>, SIZE> chunks;
};

} /* namespace Pixelverse */

#endif /* MAP_REGION_H_ */
