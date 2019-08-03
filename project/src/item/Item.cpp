#include "Item.h"

#include "../main/Game.h"
#include "../materials/Material.h"
#include <iostream>
#include <cmath>

namespace Pixelverse {

std::unique_ptr<std::vector<itemInitializer>> Item::initList;
std::vector<std::shared_ptr<Item>> Item::idList;
std::map<std::string, std::shared_ptr<Item>> Item::list;
std::map<std::string, std::shared_ptr<SpriteSheet>> Item::spriteSheets;

Item::Item(
		std::string name, std::string displayName, std::string tooltip,
		ItemType itemType, int maxStackSize, int stackSize):
			name(name), displayName(displayName), tooltip(tooltip),
			itemType(itemType), maxStackSize(maxStackSize), spriteSheet(getSpriteSheet(name)), stackSize(stackSize){}

/*Item::Item(const Item &original, int stackSize):
		name(original.name), displayName(original.displayName), tooltip(original.tooltip),
		itemType(original.itemType), maxStackSize(original.maxStackSize), stackSize(stackSize){
	spriteSheet = original.spriteSheet;
}*/

Item::~Item(){}

int Item::getStackSize(){
	return this->stackSize;
}

void Item::loadRessources(){
	spriteSheet = addSpriteSheet(name);
}

void Item::getRecipes(){}

//called while this item is being held
std::shared_ptr<Mouse> Item::holdItem(const std::shared_ptr<mouse_input> input){
//	if(input->clicking && input->changed)
//		std::cout << "item <" << name << "> used" << std::endl;
	return nullptr;
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
	return other->getID() == this->getID() &&
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

void Item::render(vec2 position, bool showAmount, bool centered){
	Screen::renderGUISprite(spriteSheet, 0, position, {1, 1}, 0, centered);
	if(showAmount && stackSize > 1)
		Screen::stackFont->render(std::to_string(stackSize), position + (centered ? vec2(-12, 12) : vec2(4, 28)));
}

std::shared_ptr<Item> Item::getItem(std::string name){
	return list[name];
}

std::shared_ptr<Item> Item::getItem(itemID_t id){
	return idList[id];
}

itemID_t Item::getItemID(std::string name){
	return list[name]->getID();
}

const std::map<std::string, std::shared_ptr<Item>> Item::getList(){
	return list;
}

const std::vector<std::shared_ptr<Item>> Item::getIDList(){
	return idList;
}

std::shared_ptr<Item> Item::createNew(std::string name, int stackSize){
	std::shared_ptr<Item> newItem = list[name]->newInstance();
	newItem->stackSize = stackSize;
	return newItem;
}

void Item::addItem(std::shared_ptr<Item> newItem){
	idList.push_back(newItem);
	list[newItem->name] = newItem;
}

itemID_t Item::registerItem(std::shared_ptr<Item> item, itemID_t *id){
	std::cout << "registering Item <" + item->name << ">" << std::endl;
	if(initList == nullptr) initList = std::make_unique<std::vector<itemInitializer>>();
	initList->push_back({item, id});
	return 0;
}

std::shared_ptr<SpriteSheet> Item::addSpriteSheet(std::string name){
	return spriteSheets[name] = std::make_shared<SpriteSheet>("item/"+name, 32, 32);
}

std::shared_ptr<SpriteSheet> Item::getSpriteSheet(std::string name){
	auto element = spriteSheets.find(name);
	if(element != spriteSheets.end())
		return element->second;
	return nullptr;
}

void Item::loadList(){
	for (std::shared_ptr<Material> material : Material::getIDList()) {
		addItem(std::make_shared<MaterialItem>(material->id));
	}
	if(Material::getIDList().size() != idList.size()){
		std::cout << "Error in \"Item::loadList\": size missmatch" << std::endl;
	}
	for(itemInitializer itemInit : *initList){
		*itemInit.id = idList.size();
		addItem(itemInit.item);
	}
	//TODO add Recipes
}

void Item::loadAllRessources(){
	for(auto item : idList){
		std::cout << "loading ressources for <" << item->name << ">" << std::endl;
		item->loadRessources();
	}
}


} /* namespace Pixelverse */
