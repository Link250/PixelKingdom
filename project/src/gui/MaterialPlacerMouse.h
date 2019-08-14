#ifndef GUI_MATERIALPLACERMOUSE_H_
#define GUI_MATERIALPLACERMOUSE_H_

#include "Mouse.h"

namespace Pixelverse {

class MaterialPlacerMouse: public Mouse{
private:
	static std::vector<shared_ptr<Texture>> textures;
	size_t innerWidth;
public:
	MaterialPlacerMouse(size_t innerWidth);
	virtual ~MaterialPlacerMouse();
	void render(vec2 position);
	void loadNewTexture(size_t innerWidth);
	size_t getInnerWidth();
	void setInnerWidth(size_t innerWidth);
};

} /* namespace Pixelverse */

#endif /* GUI_MATERIALPLACERMOUSE_H_ */
