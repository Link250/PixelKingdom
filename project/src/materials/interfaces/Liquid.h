#ifndef MATERIALS_INTERFACES_LIQUID_H_
#define MATERIALS_INTERFACES_LIQUID_H_

#include "../../utilities/DataTypes.h"

namespace Pixelverse {

class Liquid{//TODO probably obsolete
public:
	Liquid(int viscosity);

	const int viscosity;

	static bool flow(coordinate pixelPos, bool layer, int viscosity);
private:
	static bool flowtoside(coordinate pixelPos, bool layer, int side);
};

}

#endif /* MATERIALS_INTERFACES_LIQUID_H_ */
