#include "../Item.h"
#include "../../main/Game.h"
#include "../../materials/Material.h"
#include "../../input/InputHandler.h"

#include <iostream>

namespace Pixelverse {

MaterialItem::MaterialItem(materialID_t id):
						Item(Material::get(id)->name, Material::get(id)->displayName, Material::get(id)->tooltip, ItemType::MaterialPixel, 999), id(id){}

//MaterialItem::MaterialItem(const MaterialItem &original, int stackSize): Item(original, stackSize), id(original.id){}

std::shared_ptr<Mouse> MaterialItem::holdItem(const std::shared_ptr<mouse_input> input){
	if(input->scroll.y != 0.0 && input->scrollmods == GLFW_MOD_SHIFT){
		InputHandler::playerController->changeBuildSize(int(input->scroll.y));
	}
	if(input->clicking && (input->button == GLFW_MOUSE_BUTTON_1 || input->button == GLFW_MOUSE_BUTTON_2)){
		int size = InputHandler::playerController->getBuildSize();
		coordinate mousePos = coordinate(InputHandler::getMouseMapPos());
		for(int x = 0; x < size; ++x) {
			for(int y = 0; y < size; ++y) {
				Game::map->setMaterialID(mousePos + coordinate(x, y), input->button == GLFW_MOUSE_BUTTON_1, id);
			}
		}
	}
	return nullptr;
}

itemID_t MaterialItem::getID(){return id;}

std::shared_ptr<Item> MaterialItem::newInstance(){
	return std::make_shared<MaterialItem>(id);
}

} /* namespace Pixelverse */
