#ifndef GUI_MOUSE_H_
#define GUI_MOUSE_H_

#include "../gfx/Screen.h"

namespace Pixelverse {

class Mouse{
public:
	Mouse();
	virtual ~Mouse();
	void update();
	void render();
	vec2 screenPos;
	vec2 mapPos;
private:
	shared_ptr<Texture> texture;
};

} /* namespace Pixelverse */

#endif /* GUI_MOUSE_H_ */
