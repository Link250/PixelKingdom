#include "DummyChunk.h"

namespace Pixelverse {

std::shared_ptr<DummyChunk> DummyChunk::instance = std::make_shared<DummyChunk>();

DummyChunk::DummyChunk(){}

DummyChunk::~DummyChunk(){}

materialID_t DummyChunk::getMaterialID(coordinate position, bool layer){return -1;}

void DummyChunk::setMaterialID(coordinate position, bool layer, materialID_t id){}

bool DummyChunk::placeMaterial(coordinate position, bool layer, materialID_t id, std::shared_ptr<Player> player){return false;}

bool DummyChunk::breakMaterial(coordinate position, bool layer, std::shared_ptr<Player> player, const MiningTool *miningTool){return false;}

} /* namespace Pixelverse */
