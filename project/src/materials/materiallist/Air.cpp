#include "../Material.h"

namespace Pixelverse {

Air::Air(materialID_t id) : Material(id, "air", "Air", "", MiningType::None, 0.0, 0.0, false, 0.1){}

} /* namespace Pixelverse */
