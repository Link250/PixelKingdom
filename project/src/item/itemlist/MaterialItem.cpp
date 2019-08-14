#include "../Item.h"
#include "../../main/Game.h"
#include "../../materials/Material.h"
#include "../../input/InputHandler.h"
#include "../../gui/MaterialPlacerMouse.h"

#include <iostream>

namespace Pixelverse {

MaterialItem::MaterialItem(materialID_t id):
						Item(Material::get(id)->name, Material::get(id)->displayName, Material::get(id)->tooltip, ItemType::MaterialPixel, 9999), id(id){}

//MaterialItem::MaterialItem(const MaterialItem &original, int stackSize): Item(original, stackSize), id(original.id){}

std::shared_ptr<Mouse> MaterialItem::holdItem(const std::shared_ptr<mouse_input> input, std::shared_ptr<Player> player){
	if(input->scroll.y != 0.0 && input->scrollmods == GLFW_MOD_SHIFT){
		player->changeBuildSize(int(input->scroll.y));
	}
	if(input->clicking && (input->button == GLFW_MOUSE_BUTTON_1 || input->button == GLFW_MOUSE_BUTTON_2)){
		int size = player->getBuildSize();
		coordinate mousePos = coordinate(InputHandler::getMouseMapPos() - (size/2.0f - 0.5f)*MAP_SCALE);
		for(int x = 0; x < size && stackSize > 0; ++x) {
			for(int y = 0; y < size && stackSize > 0; ++y) {
				if(Game::map->placeMaterial(mousePos + coordinate(x, y), input->button == GLFW_MOUSE_BUTTON_1, id, player)){
					stackSize--;
				}
			}
		}
	}
	return std::make_shared<MaterialPlacerMouse>(player->getBuildSize());
}

itemID_t MaterialItem::getID(){return id;}

std::shared_ptr<Item> MaterialItem::newInstance(){
	return std::make_shared<MaterialItem>(id);
}

} /* namespace Pixelverse */
