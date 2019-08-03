#ifndef ITEM_ITEM_H_
#define ITEM_ITEM_H_

#include "../utilities/DataTypes.h"
#include "../gfx/SpriteSheet.h"
#include "../input/mouse_input.h"

#include <string>
#include <vector>
#include <map>
#include <memory>
#include <functional>
#include <type_traits>

namespace Pixelverse {

class Mouse;
class Item;
struct itemInitializer{
	std::shared_ptr<Item> item;
	itemID_t *id;
};

enum ItemType : char{
	CraftingMaterial, MaterialPixel, Equipment, Tool, Weapon, Furniture, Consumable
};

class Item{
public:
	virtual ~Item();

	virtual itemID_t getID() = 0;
	//TODO
	//const static itemID_t id = addToList(new ItemInstance());

	const std::string name;
	std::string displayName;
	std::string tooltip;

	const ItemType itemType;

	const int maxStackSize;
	int getStackSize();

	//const SpriteSheet texture;

	//protected int col;
	//protected int anim;
	virtual void loadRessources();
	virtual void getRecipes();//TODO

	virtual std::shared_ptr<Mouse> holdItem(const std::shared_ptr<mouse_input> input);//called while this item is being held
	virtual bool collectItem(); //called when collected
	virtual bool dropItem(); //called when dropped

	virtual bool isStackableWith(std::shared_ptr<Item> other); //checks if this can be stacked with "other"

	//takes from the other stck as much as possible and returns how much it took
	virtual int takeFrom(std::shared_ptr<Item> other, int amount = INT_MAX);


	virtual bool update();//every 30 ticks ? every half second

	virtual void render(vec2 position, bool showAmount = true, bool centered = false);

	static std::shared_ptr<Item> getItem(std::string name);
	static std::shared_ptr<Item> getItem(itemID_t id);
	static itemID_t getItemID(std::string name);
	static const std::map<std::string, std::shared_ptr<Item>> getList();
	static const std::vector<std::shared_ptr<Item>> getIDList();
	static std::shared_ptr<Item> createNew(std::string name, int stackSize = 1);

	template<typename item_t>
	static std::shared_ptr<item_t> createNew(std::string name, int stackSize = 1){
		static_assert(std::is_base_of_v<Item, item_t>, "Type must inherit from Item");
		return std::dynamic_pointer_cast<item_t>(Item::createNew(name, stackSize));
	}

	static void loadList();
	static void loadAllRessources();
protected:
	Item(
		std::string name,
		std::string displayName,
		std::string tooltip,
		ItemType itemType,
		int maxStackSize,
		int stackSize = 1);

//	Item(const Item &original, int stackSize);

	static itemID_t registerItem(std::shared_ptr<Item> item, itemID_t *id);

	virtual std::shared_ptr<Item> newInstance() = 0;

	std::shared_ptr<SpriteSheet> spriteSheet;
	static std::shared_ptr<SpriteSheet> addSpriteSheet(std::string name);
	static std::shared_ptr<SpriteSheet> getSpriteSheet(std::string name);

	int stackSize;
private:
	static std::unique_ptr<std::vector<itemInitializer>> initList;
	static std::vector<std::shared_ptr<Item>> idList;
	static std::map<std::string, std::shared_ptr<Item>> list;
	static std::map<std::string, std::shared_ptr<SpriteSheet>> spriteSheets;
	static void addItem(std::shared_ptr<Item> newItem);
};

class MaterialItem: public Item{
public:
	MaterialItem(materialID_t id);
//	MaterialItem(const MaterialItem &original, int stackSize);
	virtual std::shared_ptr<Mouse> holdItem(const std::shared_ptr<mouse_input> input);
	itemID_t getID();
protected:
	std::shared_ptr<Item> newInstance();
private:
	itemID_t id;
};

/*
//Interface: Tool
class Bucket: public Item{public: Bucket(itemID_t id);}; //TODO
class Lighter: public Item{public: Lighter(itemID_t id);}; //TODO
class MagGlass: public Item{public: MagGlass(itemID_t id);}; //TODO
////sub-interface: Pickaxe ?
class StonePickaxe: public Item{public: StonePickaxe(itemID_t id);}; //TODO
class IronPickaxe: public Item{public: IronPickaxe(itemID_t id);}; //TODO
class DevPickaxe: public Item{public: DevPickaxe(itemID_t id);}; //TODO

//Interface: Bag
class Bag: public Item{
public:
	Bag(itemID_t id,std::string name, std::string displayName, std::string tooltip,
		size_t size, std::function<bool(std::shared_ptr<Item>)> restrictor);
};
////Belt
class SmallBeltBag: public Item{public: SmallBeltBag(itemID_t id);}; //TODO
////Tools
class SmallToolBag: public Item{public: SmallToolBag(itemID_t id);}; //TODO
////Items
class SmallItemBag: public Item{public: SmallItemBag(itemID_t id);}; //TODO
class MediumItemBag: public Item{public: MediumItemBag(itemID_t id);}; //TODO
class GiantItemBag: public Item{public: GiantItemBag(itemID_t id);}; //TODO
////Materials
class SmallMaterialBag: public Item{public: SmallMaterialBag(itemID_t id);}; //TODO
class MediumMaterialBag: public Item{public: MediumMaterialBag(itemID_t id);}; //TODO
class GiantMaterialBag: public Item{public: GiantMaterialBag(itemID_t id);}; //TODO

//Interface: MultiPixel
//class Chair: public Item{public: Chair(itemID_t id);}; //TODO
*/
} /* namespace Pixelverse */

#endif /* ITEM_ITEM_H_ */
