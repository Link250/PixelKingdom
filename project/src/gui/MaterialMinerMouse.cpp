#include "MaterialMinerMouse.h"
#include "../input/InputHandler.h"

#include <iostream>

namespace Pixelverse {

std::vector<shared_ptr<Texture>> MaterialMinerMouse::textures;

MaterialMinerMouse::MaterialMinerMouse(size_t diameter): diameter(diameter){
	if(diameter > textures.size() || textures[diameter-1] == nullptr)
		loadNewTexture(diameter);
}

MaterialMinerMouse::~MaterialMinerMouse(){}

void MaterialMinerMouse::render(vec2 position){
	coordinate pos = coordinate(InputHandler::getMouseMapPos() - (diameter/2.0f - 0.5f)*MAP_SCALE);
	Screen::renderGameTexture(textures[diameter-1], vec2(pos.x, pos.y)*MAP_SCALE, {1, 1}, 0, false);
}

void MaterialMinerMouse::loadNewTexture(size_t diameter){
	if(diameter > textures.size())
		textures.resize(diameter);
	textures[diameter-1] = std::make_shared<Texture>(int2(diameter, diameter)*MAP_SCALE, color{255, 0, 0, 128}, [](int2 size, int2 pos){
		vec2 r = vec2(size.x, size.y)/MAP_SCALE*0.5;
		vec2 p = vec2(pos.x/MAP_SCALE, pos.y/MAP_SCALE) - r + 0.5;
		return ((p.x*p.x)/(r.x*r.x) + (p.y*p.y)/(r.y*r.y) < 1);
	});
	//std::cout << "new MaterialMinerMouse Texture with innerWidth " << diameter << " generated" << std::endl;
}

size_t MaterialMinerMouse::getDiameter(){
	return diameter;
}

void MaterialMinerMouse::setDiameter(size_t diameter){
	this->diameter = diameter;
	if(diameter > textures.size() || textures[diameter-1] == nullptr)
		loadNewTexture(diameter);
}

} /* namespace Pixelverse */
