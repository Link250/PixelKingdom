#ifndef GUI_GAMEFIELDS_GAMEFIELD_H_
#define GUI_GAMEFIELDS_GAMEFIELD_H_

#include "../../utilities/DataTypes.h"
#include "../../input/MouseInputUser.h"
#include "../../gfx/Texture.h"

#include <memory>

namespace Pixelverse {

class GameField: public MouseInputUser{
private:
	bool grabbed;
	int2 grabPos;
protected:
	pixel_area area;
	std::shared_ptr<Texture> backgroundTexture;
	std::string title;
public:
	const static int topHeight = 36;

	bool active;
	bool focus;

	GameField(pixel_area area, std::string title);
	virtual ~GameField();

	pixel_area getArea();

	virtual void render();
	virtual void update();
	virtual shared_ptr<Mouse> useMouseInput(const std::shared_ptr<mouse_input> input);
};

} /* namespace Pixelverse */

#endif /* GUI_GAMEFIELDS_GAMEFIELD_H_ */
