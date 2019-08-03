#ifndef INPUT_MOUSEINPUT_H_
#define INPUT_MOUSEINPUT_H_

#include "../utilities/DataTypes.h"

namespace Pixelverse {

struct mouse_input{
	int button, mods, scrollmods;
	bool clicking, changed;
	vec2 clickPos, currentPos, scroll;
	constexpr mouse_input(): button(-1), mods(-1), scrollmods(-1),
			clicking(false), changed(false),
			clickPos(-1, -1), currentPos(0, 0), scroll(0, 0){}
};

}

#endif /* INPUT_MOUSEINPUT_H_ */
