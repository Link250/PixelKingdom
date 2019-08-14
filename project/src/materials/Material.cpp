#include "Material.h"

namespace Pixelverse {

std::vector<std::shared_ptr<Material>> Material::idList;
std::map<std::string, std::shared_ptr<Material>> Material::list;

Material::Material(
		materialID_t id,
		std::string name, std::string displayName, std::string tooltip,
		MiningType miningType, float miningTier, float miningResistance,
		bool solid, float friction,
		light lightReductionBack, light lightReductionFront, light lightEmission):
			id(id),
			name(name), displayName(displayName), tooltip(tooltip),
			miningType(miningType), miningTier(miningTier), miningResistance(miningResistance),
			solid(solid), friction(friction),
			lightReductionBack(lightReductionBack), lightReductionFront(lightReductionFront), lightEmission(lightEmission){}

Material::~Material(){}

bool Material::update(Chunk *chunk, coordinate position, bool layer){return false;}
bool Material::timedUpdate(Chunk *chunk, coordinate position, bool layer){return false;}
bool Material::randomUpdate(Chunk *chunk, coordinate position, bool layer){return false;}

bool Material::canPlaceAt(Chunk *chunk, coordinate position, bool layer, std::shared_ptr<Player> player){return true;}
void Material::placeAt(Chunk *chunk, coordinate position, bool layer, std::shared_ptr<Player> player){}

bool Material::canBreakAt(Chunk *chunk, coordinate position, bool layer, std::shared_ptr<Player> player, const MiningTool *miningTool){
	return miningType & miningTool->getMiningType();
}

void Material::breakAt(Chunk *chunk, coordinate position, bool layer, std::shared_ptr<Player> player, const MiningTool *miningTool){
	if(player != nullptr){
		player->collectItem(Item::createNew(name));
	}
	//TODO spawn ItemEntity
}

std::shared_ptr<Material> Material::get(std::string name){
	return list[name];
}

std::shared_ptr<Material> Material::get(materialID_t id){
	return idList[id];
}

materialID_t Material::getID(std::string name){
	return list[name]->id;
}

const std::map<std::string, std::shared_ptr<Material>> Material::getList(){
	return list;
}

const std::vector<std::shared_ptr<Material>> Material::getIDList(){
	return idList;
}

void Material::addMaterial(std::shared_ptr<Material> newMat){
	idList.push_back(newMat);
	list[newMat->name] = newMat;
}

void Material::loadList(){
	addMaterial(std::make_shared<Air>(idList.size()));

	addMaterial(std::make_shared<Stone>(idList.size()));
	addMaterial(std::make_shared<Dirt>(idList.size()));
	addMaterial(std::make_shared<Gravel>(idList.size()));
	addMaterial(std::make_shared<Silt>(idList.size()));
	addMaterial(std::make_shared<Sand>(idList.size()));
	addMaterial(std::make_shared<Clay>(idList.size()));
	addMaterial(std::make_shared<Loam>(idList.size()));

	addMaterial(std::make_shared<Grass>(idList.size()));

	addMaterial(std::make_shared<Water>(idList.size()));
}

} /* namespace Pixelverse */
