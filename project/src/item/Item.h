#ifndef ITEM_ITEM_H_
#define ITEM_ITEM_H_

#include "../utilities/DataTypes.h"
#include "../gfx/SpriteSheet.h"

#include <string>
#include <vector>
#include <map>
#include <memory>

namespace Pixelverse {

class Item{
public:
	virtual ~Item();

	const itemID_t id;

	const std::string name;
	std::string displayName;
	std::string tooltip;

	const char itemType;

	const int maxStackSize;
	int getStackSize();

	//const SpriteSheet texture;

	//protected int col;
	//protected int anim;

	bool useItem(); //called with left/right click
	bool collectItem(); //called when collected
	bool dropItem(); //called when dropped

	bool isStackableWith(std::shared_ptr<Item> other); //checks if this can be stacked with "other"

	//takes from the other stck as much as possible and returns how much it took
	int takeFrom(std::shared_ptr<Item> other, int amount = INT_MAX);


	bool update();//every 30 ticks ? every half second

	void render(vec2 position, bool showAmount = true);

	static std::shared_ptr<Item> get(std::string name);
	static std::shared_ptr<Item> get(itemID_t id);
	static itemID_t getID(std::string name);
	static const std::map<std::string, std::shared_ptr<Item>> getList();
	static const std::vector<std::shared_ptr<Item>> getIDList();
	static std::shared_ptr<Item> createNew(std::string name, int stackSize = 1);
	static void loadList();
protected:
	Item(itemID_t id,
		std::string name,
		std::string displayName,
		std::string tooltip,
		char itemType,
		int maxStackSize,
		int stackSize = 1);

	Item(const Item &original);

	std::shared_ptr<SpriteSheet> spriteSheet;

	int stackSize;
private:
	static std::vector<std::shared_ptr<Item>> idList;
	static std::map<std::string, std::shared_ptr<Item>> list;
	static void addItem(std::shared_ptr<Item> newItem);
};

class MaterialItem: public Item{public: MaterialItem(itemID_t id,
		std::string name, std::string displayName, std::string tooltip);};

//Interface: Tool
class Bucket: public Item{public: Bucket(itemID_t id);}; //TODO
class Lighter: public Item{public: Lighter(itemID_t id);}; //TODO
class MagGlass: public Item{public: MagGlass(itemID_t id);}; //TODO
////sub-interface: Pickaxe ?
class StonePickaxe: public Item{public: StonePickaxe(itemID_t id);}; //TODO
class IronPickaxe: public Item{public: IronPickaxe(itemID_t id);}; //TODO
class DevPickaxe: public Item{public: DevPickaxe(itemID_t id);}; //TODO

//Interface: Bag
////Belt
class SmallBeltBag: public Item{public: SmallBeltBag(itemID_t id);}; //TODO
////Tools
class SmallToolBag: public Item{public: SmallToolBag(itemID_t id);}; //TODO
////Items
class SmallItemBag: public Item{public: SmallItemBag(itemID_t id);}; //TODO
class NormalItemBag: public Item{public: NormalItemBag(itemID_t id);}; //TODO
class GiantItemBag: public Item{public: GiantItemBag(itemID_t id);}; //TODO
////Materials
class SmallMaterialBag: public Item{public: SmallMaterialBag(itemID_t id);}; //TODO
class NormalMaterialBag: public Item{public: NormalMaterialBag(itemID_t id);}; //TODO
class GiantMaterialBag: public Item{public: GiantMaterialBag(itemID_t id);}; //TODO

//Interface: MultiPixel
//class Chair: public Item{public: Chair(itemID_t id);}; //TODO

} /* namespace Pixelverse */

#endif /* ITEM_ITEM_H_ */
