#include "Mouse.h"

#include "../main/Game.h"
#include "../input/InputHandler.h"

namespace Pixelverse {

const std::shared_ptr<Mouse> Mouse::instance = std::make_shared<Mouse>();
shared_ptr<Texture> Mouse::texture = Game::queueRessourceLoader<std::shared_ptr<Texture>>([](){
	texture = make_shared<Texture>("gui/mouse");
});

Mouse::Mouse(){}

Mouse::~Mouse(){}

void Mouse::render(vec2 position){
	Screen::renderGUITexture(texture, position, {1.0, 1.0}, 0, false);
}

void Mouse::render(vec2 position, std::shared_ptr<Item> item){
	item->render(position, true, true);
}

} /* namespace Pixelverse */
