#ifndef MATERIALS_MININGTYPE_H_
#define MATERIALS_MININGTYPE_H_

namespace Pixelverse {

enum MiningType: unsigned char{
	None 	= 0x0,
	Pickaxe = 0x1,
	Shovel 	= 0x2,
	Axe 	= 0x4,
	Hammer 	= 0x8,
	/*space for additional Types*/
	Custom	= 0x80
};

} /* namespace Pixelverse */

#endif /* MATERIALS_MININGTYPE_H_ */
