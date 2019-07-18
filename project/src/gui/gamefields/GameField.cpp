#include "GameField.h"

namespace Pixelverse {

GameField::GameField(): active(false), focus(false){}

GameField::~GameField(){
	// TODO Auto-generated destructor stub
}

pixel_area GameField::getArea(){
	return area;
}

} /* namespace Pixelverse */
