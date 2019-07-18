#include "Material.h"

namespace Pixelverse {

std::vector<std::shared_ptr<Material>> Material::idList;
std::map<std::string, std::shared_ptr<Material>> Material::list;

Material::Material(
		materialID_t id,
		std::string name, std::string displayName, std::string tooltip,
		bool solid, double friction,
		int miningType, double miningTier, double miningResistance,
		light lightReductionBack, light lightReductionFront, light lightEmission):
			id(id),
			name(name), displayName(displayName), tooltip(tooltip),
			solid(solid), friction(friction),
			miningType(miningType), miningTier(miningTier), miningResistance(miningResistance),
			lightReductionBack(lightReductionBack), lightReductionFront(lightReductionFront), lightEmission(lightEmission){}

Material::~Material(){}

bool Material::update(){return false;}
bool Material::timedUpdate(){return false;}
bool Material::randomUpdate(){return false;}

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
