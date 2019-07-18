#include "Biome.h"

namespace Pixelverse {

std::vector<std::shared_ptr<Biome>> Biome::idList;
std::map<std::string, std::shared_ptr<Biome>> Biome::list;

Biome::Biome(
		biomeID_t id,
		std::string name, std::string displayName,
		double heightBias):
				id(id),
				name(name), displayName(displayName),
				heightBias(heightBias){}

Biome::~Biome(){}

std::shared_ptr<Biome> Biome::get(std::string name){
	return list[name];
}

std::shared_ptr<Biome> Biome::get(materialID_t id){
	return idList[id];
}

materialID_t Biome::getID(std::string name){
	return list[name]->id;
}

const std::map<std::string, std::shared_ptr<Biome>> Biome::getList(){
	return list;
}

const std::vector<std::shared_ptr<Biome>> Biome::getIDList(){
	return idList;
}

void Biome::addBiome(std::shared_ptr<Biome> newBiome){
	idList.push_back(newBiome);
	list[newBiome->name] = newBiome;
}

void Biome::loadList(){
	addBiome(std::make_shared<Forest>(idList.size()));
}

} /* namespace Pixelverse */
