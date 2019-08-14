#ifndef MAP_DUMMYCHUNK_H_
#define MAP_DUMMYCHUNK_H_

#include "Chunk.h"

namespace Pixelverse {

class DummyChunk: public Chunk{
public:
	static std::shared_ptr<DummyChunk> instance;

	DummyChunk();
	virtual ~DummyChunk();

	virtual materialID_t getMaterialID(coordinate position, bool layer);
	virtual void setMaterialID(coordinate position, bool layer, materialID_t id);
	virtual bool placeMaterial(coordinate position, bool layer, materialID_t id, std::shared_ptr<Player> player);
	virtual bool breakMaterial(coordinate position, bool layer, std::shared_ptr<Player> player, const MiningTool *miningTool);
};

} /* namespace Pixelverse */

#endif /* MAP_DUMMYCHUNK_H_ */
