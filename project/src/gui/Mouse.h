#ifndef GUI_MOUSE_H_
#define GUI_MOUSE_H_

#include "../gfx/Screen.h"
#include "../item/Item.h"

namespace Pixelverse {

class Mouse{
public:
	const static std::shared_ptr<Mouse> instance;

	Mouse();
	virtual ~Mouse();
	virtual void render(vec2 position);
	void render(vec2 position, std::shared_ptr<Item> item);
private:
	static shared_ptr<Texture> texture;
};

} /* namespace Pixelverse */

#endif /* GUI_MOUSE_H_ */
