#ifndef MAP_REGION_H_
#define MAP_REGION_H_

#include "Chunk.h"
#include <array>
#include <memory>

namespace Pixelverse {

class Region{
public:
	static const int WIDTH = 256;
	static const int SIZE = WIDTH*WIDTH;
	Region();
	virtual ~Region();
	std::shared_ptr<Chunk> getChunk(coordinate position);
	materialID_t getMaterialID(coordinate pixelPos, bool layer);
	void setMaterialID(coordinate pixelPos, bool layer, materialID_t id);
	void loadChunk(coordinate position);
private:
	std::array<std::shared_ptr<Chunk>, SIZE> chunks;
};

} /* namespace Pixelverse */

#endif /* MAP_REGION_H_ */
