#include "MaterialPlacerMouse.h"
#include "../input/InputHandler.h"

#include <iostream>

namespace Pixelverse {

std::vector<shared_ptr<Texture>> MaterialPlacerMouse::textures;

MaterialPlacerMouse::MaterialPlacerMouse(size_t innerWidth): innerWidth(innerWidth){
	if(innerWidth > textures.size() || textures[innerWidth-1] == nullptr)
		loadNewTexture(innerWidth);
}

MaterialPlacerMouse::~MaterialPlacerMouse(){}

void MaterialPlacerMouse::render(vec2 position){
	coordinate pos = coordinate(InputHandler::getMouseMapPos() - (innerWidth/2.0f + 1.5f)*MAP_SCALE);
	Screen::renderGameTexture(textures[innerWidth-1], vec2(pos.x, pos.y)*MAP_SCALE, {1, 1}, 0, false);
}

void MaterialPlacerMouse::loadNewTexture(size_t innerWidth){
	if(innerWidth > textures.size())
		textures.resize(innerWidth);
	textures[innerWidth-1] = std::make_shared<Texture>(int2(innerWidth+4, innerWidth+4)*MAP_SCALE, color{255, 0, 0, 128}, [](int2 size, int2 pos){
		if(pos.x >= MAP_SCALE*2 && pos.x < size.x-MAP_SCALE*2 && pos.y >= MAP_SCALE*2 && pos.y < size.y-MAP_SCALE*2)
			return false;
		if((pos.x < MAP_SCALE || pos.x >= size.x-MAP_SCALE) && (((size.x/MAP_SCALE)%2 == 1) ? (pos.y/MAP_SCALE != size.y/MAP_SCALE/2) : ((pos.y/MAP_SCALE != size.y/MAP_SCALE/2) && (pos.y/MAP_SCALE != size.y/MAP_SCALE/2-1))))
			return false;
		if((pos.y < MAP_SCALE || pos.y >= size.y-MAP_SCALE) && (((size.y/MAP_SCALE)%2 == 1) ? (pos.x/MAP_SCALE != size.x/MAP_SCALE/2) : ((pos.x/MAP_SCALE != size.x/MAP_SCALE/2) && (pos.x/MAP_SCALE != size.x/MAP_SCALE/2-1))))
			return false;
		return true;
	});
	//std::cout << "new MaterialPlayerMouse Texture with innerWidth " << innerWidth << " generated" << std::endl;
}

size_t MaterialPlacerMouse::getInnerWidth(){
	return innerWidth;
}

void MaterialPlacerMouse::setInnerWidth(size_t innerWidth){
	this->innerWidth = innerWidth;
	if(innerWidth > textures.size() || textures[innerWidth-1] == nullptr)
		loadNewTexture(innerWidth);
}

} /* namespace Pixelverse */
