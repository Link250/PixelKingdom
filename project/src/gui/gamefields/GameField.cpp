#include "../../main/Game.h"
#include "../MoveMouse.h"
#include "GameField.h"
#include <algorithm>

namespace Pixelverse {

const int GameField::topHeight;

GameField::GameField(pixel_area area, std::string title): grabbed(false), grabPos(0, 0), area(area), title(title), active(false), focus(false){
	backgroundTexture = std::make_shared<Texture>(area.size(), topHeight);
}

GameField::~GameField(){
	// TODO Auto-generated destructor stub
}

pixel_area GameField::getArea(){
	return area;
}

void GameField::render(){
	Screen::renderGUITexture(backgroundTexture, {double(area.x), double(area.y)}, {1.0, 1.0}, 0.0, false);
	Screen::mainFont->render(title, {double(area.x+16), double(area.y+18)});
}

void GameField::update(){
	area.x = std::clamp(area.x, 0, Screen::width - area.w);
	area.y = std::clamp(area.y, 0, Screen::height - area.h);
}

shared_ptr<Mouse> GameField::useMouseInput(const std::shared_ptr<mouse_input> input){
	if(input->clicking){
		if(grabbed){
			area.setPosition(int2(input->currentPos) - grabPos);
		}else{
			grabPos = int2(input->clickPos) - area.position();
			if(grabPos.y < topHeight)
				grabbed = true;
		}
	}else{
		grabbed = false;
	}
	return grabbed ? MoveMouse::instance : nullptr;
}

} /* namespace Pixelverse */
