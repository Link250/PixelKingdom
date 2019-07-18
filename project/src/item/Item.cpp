#include "Item.h"

#include "../materials/Material.h"
#include "../main/Game.h"
#include <iostream>
#include <cmath>

namespace Pixelverse {

std::vector<std::shared_ptr<Item>> Item::idList;
std::map<std::string, std::shared_ptr<Item>> Item::list;

Item::Item(
		itemID_t id,
		std::string name, std::string displayName, std::string tooltip,
		char itemType, int maxStackSize, int stackSize):
			id(id),
			name(name), displayName(displayName), tooltip(tooltip),
			itemType(itemType), maxStackSize(maxStackSize), stackSize(stackSize){
	spriteSheet = std::make_shared<SpriteSheet>("item/"+name, 32, 32);
}

Item::Item(const Item &original): id(original.id),
		name(original.name), displayName(original.displayName), tooltip(original.tooltip),
		itemType(original.itemType), maxStackSize(original.maxStackSize), stackSize(original.stackSize){
	spriteSheet = original.spriteSheet;
}

Item::~Item(){}

int Item::getStackSize(){
	return this->stackSize;
}

//called with left/right click
bool Item::useItem(){
	return false;
}

//called when collected
bool Item::collectItem(){
	return false;
}

//called when dropped
bool Item::dropItem(){
	return false;
}

//checks if this can be stacked with "other"
bool Item::isStackableWith(std::shared_ptr<Item> other){
	return other->id == this->id &&
			this->displayName.compare(other->displayName) == 0 &&
			this->tooltip.compare(other->tooltip) == 0;
}

int Item::takeFrom(std::shared_ptr<Item> other, int amount){
	if(isStackableWith(other)){
		amount = std::min(this->maxStackSize - this->stackSize, std::min(other->stackSize, std::max(amount, 0)));
		this->stackSize += amount;
		other->stackSize -= amount;
		return amount;
	}
	return 0;
}

//every 30 ticks ? every half second
bool Item::update(){return false;}

void Item::render(vec2 position, bool showAmount){
	Game::screen->drawGUISprite(spriteSheet, 0, position, {1, 1}, 0, false);
}

std::shared_ptr<Item> Item::get(std::string name){
	return list[name];
}

std::shared_ptr<Item> Item::get(itemID_t id){
	return idList[id];
}

itemID_t Item::getID(std::string name){
	return list[name]->id;
}

const std::map<std::string, std::shared_ptr<Item>> Item::getList(){
	return list;
}

const std::vector<std::shared_ptr<Item>> Item::getIDList(){
	return idList;
}

std::shared_ptr<Item> Item::createNew(std::string name, int stackSize){
	return std::shared_ptr<Item>(new Item(*list[name]));
}

void Item::addItem(std::shared_ptr<Item> newItem){
	idList.push_back(newItem);
	list[newItem->name] = newItem;
}

void Item::loadList(){
	for (std::shared_ptr<Material> material : Material::getIDList()) {
		addItem(std::make_shared<MaterialItem>(material->id, material->name, material->displayName, material->tooltip));
	}
	if(Material::getIDList().size() != idList.size()){
		std::cout << "Error in \"Item::loadList\": size missmatch" << std::endl;
	}
	//addItem(std::make_shared<MaterialItem>(idList.size()));
}

} /* namespace Pixelverse */
