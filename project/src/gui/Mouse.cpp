#include "Mouse.h"

#include "../main/Game.h"
#include "../input/InputHandler.h"

namespace Pixelverse {

Mouse::Mouse(){
	texture = make_shared<Texture>("Mouse");
}

Mouse::~Mouse(){}

void Mouse::update(){
	screenPos = InputHandler::mousePos;
	mapPos = (screenPos - vec2{double(Game::screen->width), double(Game::screen->height)}/2).rotate(-Game::screen->rotation)/Game::screen->zoom + Game::screen->center;
}

void Mouse::render(){
	Game::screen->drawGUITexture(texture, screenPos, {1.0, 1.0}, 0, false);
}

} /* namespace Pixelverse */
