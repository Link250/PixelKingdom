#ifndef ITEM_PICKAXE_H_
#define ITEM_PICKAXE_H_

#include "MiningTool.h"
#include "../materials/Material.h"

namespace Pixelverse {

class Pickaxe: public MiningTool{
public:
	Pickaxe(std::string name,
			std::string displayName,
			std::string tooltip,
			int miningSize, int miningRange,
			float miningStrength, float miningTier,
			MiningType miningType = MiningType::Pickaxe);
	virtual ~Pickaxe();

	virtual std::shared_ptr<Mouse> holdItem(const std::shared_ptr<mouse_input> input, std::shared_ptr<Player> player);
};

} /* namespace Pixelverse */

#endif /* ITEM_PICKAXE_H_ */
