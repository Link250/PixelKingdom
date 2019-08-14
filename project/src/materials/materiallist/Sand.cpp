#include "../Material.h"
#include "../../main/Game.h"

namespace Pixelverse {

Sand::Sand(materialID_t id) : Material(id, "sand", "Sand", "tooltip", MiningType::Pickaxe){}

bool Sand::update(Chunk *chunk, coordinate position, bool layer){
	return fall(chunk, position, layer);
}

} /* namespace Pixelverse */
