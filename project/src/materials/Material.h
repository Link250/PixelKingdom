#ifndef MATERIALS_MATERIAL_H_
#define MATERIALS_MATERIAL_H_

#include "../utilities/DataTypes.h"
#include "../entities/Player.h"
#include "../item/MiningTool.h"

#include <string>
#include <vector>
#include <map>
#include <memory>

namespace Pixelverse {

class Chunk;//predefining to avoid include loop

class Material{
public:
	Material(materialID_t id,
			std::string name,
			std::string displayName,
			std::string tooltip = "",
			MiningType miningType = MiningType::None,
			float miningTier = 1.0,
			float miningResistance = 1.0/25.0,
			bool solid = true,
			float friction = 1.0,
			light lightReductionBack = {255, 255, 255},
			light lightReductionFront = {8, 8, 8},
			light lightEmission = {0, 0, 0});
	virtual ~Material();

	const materialID_t id;

	const std::string name;
	const std::string displayName;
	const std::string tooltip;

	const MiningType miningType;
	const float miningTier;
	const float miningResistance;

	const bool solid;
	const float friction;

	const light lightReductionBack;
	const light lightReductionFront;
	const light lightEmission;

	virtual bool update(Chunk *chunk, coordinate position, bool layer);
	virtual bool timedUpdate(Chunk *chunk, coordinate position, bool layer);
	virtual bool randomUpdate(Chunk *chunk, coordinate position, bool layer);

	virtual bool canPlaceAt(Chunk *chunk, coordinate position, bool layer, std::shared_ptr<Player> player = nullptr);
	virtual void placeAt(Chunk *chunk, coordinate position, bool layer, std::shared_ptr<Player> player = nullptr);
	virtual bool canBreakAt(Chunk *chunk, coordinate position, bool layer, std::shared_ptr<Player> player = nullptr, const MiningTool *miningTool = nullptr);
	virtual void breakAt(Chunk *chunk, coordinate position, bool layer, std::shared_ptr<Player> player = nullptr, const MiningTool *miningTool = nullptr);

	bool fall(Chunk *chunk, coordinate position, bool layer, bool fallToSide = true);
	bool flow(Chunk *chunk, coordinate position, bool layer, int viscocity);
	bool spread(Chunk *chunk, coordinate position, bool layer);
	static constexpr int2 directions[5] = {	int2(1,0),
										int2(0,1),
										int2(-1,0),
										int2(0,-1),
										int2(0,0)};

	//static methods for item lists etc
	static std::shared_ptr<Material> get(std::string name);
	static std::shared_ptr<Material> get(materialID_t id);
	static materialID_t getID(std::string name);
	static const std::map<std::string, std::shared_ptr<Material>> getList();
	static const std::vector<std::shared_ptr<Material>> getIDList();
	static void loadList();
private:
	static std::vector<std::shared_ptr<Material>> idList;
	static std::map<std::string, std::shared_ptr<Material>> list;
	static void addMaterial(std::shared_ptr<Material> newMat);
};

class Air: public Material{public: Air(materialID_t id);};

//--------------------------------MATERIALS---------------------------------//
class Stone: public Material{public: Stone(materialID_t id);};
class Dirt: public Material{public: Dirt(materialID_t id);};
class Gravel: public Material{
public:
	Gravel(materialID_t id);
	bool update(Chunk *chunk, coordinate position, bool layer);
};//TODO
class Silt: public Material{
public:
	Silt(materialID_t id);
	bool update(Chunk *chunk, coordinate position, bool layer);
};//TODO
class Sand: public Material{
public:
	Sand(materialID_t id);
	bool update(Chunk *chunk, coordinate position, bool layer);
};//TODO
class Clay: public Material{public: Clay(materialID_t id);};//TODO
class Loam: public Material{public: Loam(materialID_t id);};//TODO

//---------------------------------LIQUIDS----------------------------------//
class Water: public Material{
public:
	Water(materialID_t id);
	bool update(Chunk *chunk, coordinate position, bool layer);
};//TODO
class Lava: public Material{public: Lava(materialID_t id);};//TODO

//--------------------------------VEGETATION--------------------------------//
class Grass: public Material{public: Grass(materialID_t id);};
class Moss: public Material{public: Moss(materialID_t id);};//TODO
class Wood: public Material{public: Wood(materialID_t id);};//TODO
class Leaf: public Material{public: Leaf(materialID_t id);};//TODO
class Twig: public Material{public: Twig(materialID_t id);};//TODO

//-----------------------------------ORES-----------------------------------//
class CoalOre: public Material{public: CoalOre(materialID_t id);};//TODO
class IronOre: public Material{public: IronOre(materialID_t id);};//TODO
class GoldOre: public Material{public: GoldOre(materialID_t id);};//TODO

//---------------------------------CRAFTING---------------------------------//
class SimpleOven: public Material{public: SimpleOven(materialID_t id);};//TODO
class Planks: public Material{public: Planks(materialID_t id);};//TODO
class Torch: public Material{public: Torch(materialID_t id);};//TODO
class Chair: public Material{public: Chair(materialID_t id);};//TODO
class Fire: public Material{public: Fire(materialID_t id);};//TODO

//----------------------------------SMELTING--------------------------------//
class Iron: public Material{public: Iron(materialID_t id);};//TODO
class Gold: public Material{public: Gold(materialID_t id);};//TODO
class Glass: public Material{public: Glass(materialID_t id);};//TODO

} /* namespace Pixelverse */

#endif /* MATERIALS_MATERIAL_H_ */
