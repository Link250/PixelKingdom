#include "BagField.h"

#include "ItemField.h"
#include "../../input/InputHandler.h"

#include <iostream>

namespace Pixelverse {

BagField::BagField(std::shared_ptr<Inventory<Item>> inventory, int width, std::string name):
		GameField({100, 100, width * (ItemField::size.x + 2) - 2, int(std::ceil(double(inventory->size)/width + 1))*(ItemField::size.y + 2) - 2}, name),
		inventory(inventory), inventoryWidth(width){
	itemFields = std::vector<ItemField>();
	itemFields.reserve(inventory->size);
	for(size_t i = 0; i < inventory->size; i++){
		itemFields.push_back(ItemField(inventory->getConstItemPointer(i)));
	}
	placeItemFields();
}

BagField::~BagField(){}

void BagField::render(){
	GameField::render();
	for(auto field : itemFields){
		field.render(area.position());
	}
}

void BagField::placeItemFields(){
	for(size_t i = 0; i < itemFields.size(); i++){
		itemFields[i].position = int2((i % inventoryWidth) * (ItemField::size.x+2), (i / inventoryWidth) * (ItemField::size.y+2) + GameField::topHeight+2);
	}
}

shared_ptr<Mouse> BagField::useMouseInput(const std::shared_ptr<mouse_input> input){
	shared_ptr<Mouse> mouse = GameField::useMouseInput(input);
	if(input->clicking && input->changed && input->button >= 0){
		//std::cout << " take Item ";
		int2 fieldsClickPos = int2(input->clickPos.x - area.x, input->clickPos.y - (area.y + GameField::topHeight + 2));
		if(fieldsClickPos.y > 0){
			if(fieldsClickPos.x % ItemField::size.x < ItemField::size.x && fieldsClickPos.y % ItemField::size.y < ItemField::size.y ){
				size_t index = (fieldsClickPos.x / (ItemField::size.x+2)) + (fieldsClickPos.y / (ItemField::size.y+2)) * inventoryWidth;
				//std::cout << index;
				if(index < inventory->size){
					inventory->swapItem(index, &InputHandler::playerController->mouseItem);
				}
			}
		}
		//std::cout << std::endl;
	}
	//std::cout << "BagField Input: " << input->button << input->clicking << input->mods << std::endl;
	return mouse;
}

} /* namespace Pixelverse */
