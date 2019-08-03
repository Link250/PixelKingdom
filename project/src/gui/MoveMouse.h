#ifndef GUI_MOVEMOUSE_H_
#define GUI_MOVEMOUSE_H_

#include "Mouse.h"

namespace Pixelverse {

class MoveMouse: public Mouse{
public:
	const static std::shared_ptr<MoveMouse> instance;

	MoveMouse();
	virtual ~MoveMouse();
	virtual void render(vec2 position);
private:
	static shared_ptr<Texture> texture;
};

} /* namespace Pixelverse */

#endif /* GUI_MOVEMOUSE_H_ */
