#include "../Material.h"

namespace Pixelverse {

Water::Water(materialID_t id) : Material(id, "water", "Water", ""){}

bool Water::update(Chunk *chunk, coordinate position, bool layer){
	return flow(chunk, position, layer, 100);
}

} /* namespace Pixelverse */
