#include "../Material.h"

namespace Pixelverse {

Gravel::Gravel(materialID_t id) : Material(id, "gravel", "Gravel", "tooltip", MiningType::Pickaxe){}

bool Gravel::update(Chunk *chunk, coordinate position, bool layer){
	return fall(chunk, position, layer);
}

} /* namespace Pixelverse */
