#ifndef GUI_GAMEFIELDS_GAMEFIELD_H_
#define GUI_GAMEFIELDS_GAMEFIELD_H_

#include "../../utilities/DataTypes.h"

namespace Pixelverse {

class GameField{
private:
	pixel_area area;
public:
	bool active;
	bool focus;

	GameField();
	virtual ~GameField();

	pixel_area getArea();

};

} /* namespace Pixelverse */

#endif /* GUI_GAMEFIELDS_GAMEFIELD_H_ */
