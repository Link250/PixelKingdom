#ifndef INPUT_MOUSEINPUTUSER_H_
#define INPUT_MOUSEINPUTUSER_H_

#include "../gui/Mouse.h"
#include "mouse_input.h"

#include <memory>

namespace Pixelverse {

class MouseInputUser{
public:
	virtual ~MouseInputUser(){}
	virtual std::shared_ptr<Mouse> useMouseInput(const std::shared_ptr<mouse_input> input) = 0;
};

} /* namespace Game */

#endif /* INPUT_MOUSEINPUTUSER_H_ */
