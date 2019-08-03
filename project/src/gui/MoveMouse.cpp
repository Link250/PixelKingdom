#include "MoveMouse.h"
#include "../main/Game.h"

namespace Pixelverse {

const std::shared_ptr<MoveMouse> MoveMouse::instance = std::make_shared<MoveMouse>();
shared_ptr<Texture> MoveMouse::texture = Game::queueRessourceLoader<std::shared_ptr<Texture>>([](){
	texture = make_shared<Texture>("gui/mouse_move");
});

MoveMouse::MoveMouse(){}

MoveMouse::~MoveMouse(){}

void MoveMouse::render(vec2 position){
	Screen::renderGUITexture(texture, position);
}

} /* namespace Pixelverse */
