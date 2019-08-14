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


PlayerController::PlayerController(std::shared_ptr<Player> player): player(player), inventoryOpen(false){
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
			player->changeSelectedSlot(int(input->scroll.y));
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
	if(player->beltBag->inventory->getItem(player->selectedSlot) != nullptr){
		return player->beltBag->inventory->getItem(player->selectedSlot)->holdItem(input, player);
	}
	return nullptr;
}

const std::shared_ptr<Player> PlayerController::getPlayer(){return player;}

} /* namespace Pixelverse */
