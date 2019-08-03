#include "PlayerController.h"
#include "../config/KeyConfig.h"
#include "../main/Game.h"
#include <iostream>

namespace Pixelverse {

class InputHandler{
public:
	static vec2 mousePos;
	static vec2 mouseMapPos;
	static bool takeKeyClick(int key);
	static bool keyPressed(int key);
};


PlayerController::PlayerController(std::shared_ptr<Player> player): player(player), inventoryOpen(false), selectedSlot(0), buildSize(5){
	if(player->beltBag[0] != nullptr)
		inventoryFields.push_back(make_shared<BagField>(player->beltBag->inventory, 8, player->beltBag->displayName));
	if(player->itemBags[0] != nullptr)
		inventoryFields.push_back(make_shared<BagField>(player->itemBags[0]->inventory, 8, player->itemBags[0]->displayName));
	if(player->itemBags[1] != nullptr)
		inventoryFields.push_back(make_shared<BagField>(player->itemBags[1]->inventory, 8, player->itemBags[1]->displayName));
}

PlayerController::~PlayerController(){}

void PlayerController::update(){
	if(InputHandler::takeKeyClick(KeyConfig::INVENTORY)){
		inventoryOpen = !inventoryOpen;
		for(auto field : inventoryFields){
			field->active = inventoryOpen;
			if(inventoryOpen)
				Game::addGameField(field);
		}
	}
}

shared_ptr<Mouse> PlayerController::useMouseInput(const std::shared_ptr<mouse_input> input){
	if(input->scroll.y != 0.0){
		if(input->scrollmods == 0){
			changeSelectedSlot(int(input->scroll.y));
		}else if(input->scrollmods == GLFW_MOD_CONTROL){//TODO change to key config "zoom scroll button"
			if(input->scroll.y > 0){
				if(Screen::zoom_target < 8)
					Screen::zoom_target *= 2;
			}else{
				if(Screen::zoom_target > 0.125)
					Screen::zoom_target /= 2;
			}
		}
	}
	if(player->beltBag->inventory->getItem(selectedSlot) != nullptr){
		player->beltBag->inventory->getItem(selectedSlot)->holdItem(input);
	}
	return nullptr;
}

const std::shared_ptr<Player> PlayerController::getPlayer(){return player;}

size_t PlayerController::getSelectedSlot(){return selectedSlot;}

void PlayerController::changeSelectedSlot(size_t slotChange){
	if(player->beltBag[0] != nullptr){
		selectedSlot = (selectedSlot+slotChange)%player->beltBag->inventory->size;
		if(selectedSlot < 0) selectedSlot += player->beltBag->inventory->size;
	}
}

void PlayerController::setSelectedSlot(size_t slot){
	if(player->beltBag[0] != nullptr){
		selectedSlot = slot%player->beltBag->inventory->size;
		if(selectedSlot < 0) selectedSlot += player->beltBag->inventory->size;
	}
}

int PlayerController::getBuildSize(){return buildSize;}

void PlayerController::changeBuildSize(int sizeChange){
	buildSize += sizeChange;
	if(buildSize > MAX_BUILD_SIZE) buildSize = MAX_BUILD_SIZE;
	if(buildSize < 1) buildSize = 1;
}

void PlayerController::setBuildSize(int size){
	buildSize = size;
	if(buildSize > MAX_BUILD_SIZE) buildSize = MAX_BUILD_SIZE;
	if(buildSize < 1) buildSize = 1;
}

} /* namespace Pixelverse */
