#include "Liquid.h"

namespace Pixelverse {

Liquid::Liquid(int viscosity): viscosity(viscosity){}

bool Liquid::flow(coordinate pixelPos, bool layer, int viscosity){
	return true;
}

bool Liquid::flowtoside(coordinate pixelPos, bool layer, int side){
	return true;
}

}
