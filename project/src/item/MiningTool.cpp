#include "MiningTool.h"

namespace Pixelverse {

MiningTool::MiningTool(std::string name, std::string displayName, std::string tooltip,
		int miningSize, int miningRange,
		float miningStrength, float miningTier,
		MiningType miningType,
		ItemType itemType, int maxStackSize, int stackSize):
				Item(name, displayName, tooltip, itemType, maxStackSize, stackSize),
				miningSize(miningSize), miningRange(miningRange),
				miningStrength(miningStrength), miningTier(miningTier),
				miningType(miningType){}

MiningTool::~MiningTool(){}

float MiningTool::getMiningStrength() const{return miningStrength;}

float MiningTool::getMiningTier() const{return miningTier;}

MiningType MiningTool::getMiningType() const{return miningType;}

} /* namespace Pixelverse */
