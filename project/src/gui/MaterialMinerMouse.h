#ifndef GUI_MATERIALMINERMOUSE_H_
#define GUI_MATERIALMINERMOUSE_H_

#include "Mouse.h"

namespace Pixelverse {

class MaterialMinerMouse: public Mouse{
private:
	static std::vector<shared_ptr<Texture>> textures;
	size_t diameter;
public:
	MaterialMinerMouse(size_t diameter);
	virtual ~MaterialMinerMouse();
	void render(vec2 position);
	void loadNewTexture(size_t diameter);
	size_t getDiameter();
	void setDiameter(size_t diameter);};

} /* namespace Pixelverse */

#endif /* GUI_MATERIALMINERMOUSE_H_ */
