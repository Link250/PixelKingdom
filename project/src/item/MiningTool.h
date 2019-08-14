#ifndef ITEM_MININGTOOL_H_
#define ITEM_MININGTOOL_H_

#include "Item.h"
#include "../materials/MiningType.h"

namespace Pixelverse {

class MiningTool: public Item{
protected:
	int miningSize, miningRange;
	float miningStrength, miningTier;
	MiningType miningType;
public:

	MiningTool(std::string name,
			std::string displayName,
			std::string tooltip,
			int miningSize, int miningRange,
			float miningStrength, float miningTier,
			MiningType miningType,
			ItemType itemType = ItemType::Tool,
			int maxStackSize = 1,
			int stackSize = 1);
	virtual ~MiningTool();

	float getMiningStrength() const;
	float getMiningTier() const;
	MiningType getMiningType() const;
};

} /* namespace Pixelverse */

#endif /* ITEM_MININGTOOL_H_ */
