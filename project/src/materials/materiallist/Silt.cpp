#include "../Material.h"

namespace Pixelverse {

Silt::Silt(materialID_t id) : Material(id, "silt", "Silt", "tooltip", MiningType::Pickaxe){}

bool Silt::update(Chunk *chunk, coordinate position, bool layer){
	return fall(chunk, position, layer);
}

} /* namespace Pixelverse */
