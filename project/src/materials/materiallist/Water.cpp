#include "../Material.h"

namespace Pixelverse {

Water::Water(materialID_t id) : Material(id, "water", "Water", "", false), Liquid(100){}

} /* namespace Pixelverse */
