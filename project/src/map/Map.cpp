#include "Map.h"
#include "../main/Game.h"
#include "../input/InputHandler.h"

namespace Pixelverse {

Map::Map(): chunkManager(""){
	planet = make_shared<Planet>();
}

Map::~Map(){}

void Map::update(){
	for ( auto &e : entities ) {
		e->applyGravity();
		e->update();
	}

	planet->update();

	std::shared_ptr<std::vector<coordinate>> frontUpdateList = updates[true].startUpdate();
	std::shared_ptr<std::vector<coordinate>> backUpdateList = updates[false].startUpdate();
	size_t frontUpdatesCount = updates[true].getSize();
	size_t backUpdatesCount = updates[false].getSize();
	coordinate updateCoord, currentChunkCoord = coordinate(0x7fffffff,0x7fffffff);
	Chunk *chunk = nullptr;


	double time1 = glfwGetTime();
	if(Game::updateCount%2 == 0){
		for(size_t i = 0; i < frontUpdatesCount; i++){
			updateCoord = (*frontUpdateList)[i];
			if(updateCoord.rc_x() != currentChunkCoord.rc_x() || updateCoord.rc_y() != currentChunkCoord.rc_y())chunk = getChunk(updateCoord).get();
			if(getMaterial(updateCoord, true)->update(chunk, updateCoord, true)){
				createMaterialUpdates(updateCoord, true);
			}
		}
		for(size_t i = 0; i < backUpdatesCount; i++){
			updateCoord = (*backUpdateList)[i];
			if(updateCoord.rc_x() != currentChunkCoord.rc_x() || updateCoord.rc_y() != currentChunkCoord.rc_y())chunk = getChunk(updateCoord).get();
			if(getMaterial(updateCoord, false)->update(chunk, updateCoord, false)){
				createMaterialUpdates(updateCoord, false);
			}
		}
	}else{
		for(size_t i = 0; i < frontUpdatesCount; i++){
			updateCoord = (*frontUpdateList)[frontUpdatesCount-i-1];
			if(updateCoord.rc_x() != currentChunkCoord.rc_x() || updateCoord.rc_y() != currentChunkCoord.rc_y())chunk = getChunk(updateCoord).get();
			if(getMaterial(updateCoord, true)->update(chunk, updateCoord, true)){
				createMaterialUpdates(updateCoord, true);
			}
		}
		for(size_t i = 0; i < backUpdatesCount; i++){
			updateCoord = (*backUpdateList)[backUpdatesCount-i-1];
			if(updateCoord.rc_x() != currentChunkCoord.rc_x() || updateCoord.rc_y() != currentChunkCoord.rc_y())chunk = getChunk(updateCoord).get();
			if(getMaterial(updateCoord, false)->update(chunk, updateCoord, false)){
				createMaterialUpdates(updateCoord, false);
			}
		}
	}
	double time2 = glfwGetTime();
	if(frontUpdatesCount != 0 || backUpdatesCount != 0){
		double newTime = (time2-time1)*1000/int(backUpdatesCount + frontUpdatesCount);
		times.push_back(newTime);
		printf("time: %f updates: %i t/u: %f\n", (time2-time1)*1000, int(backUpdatesCount + frontUpdatesCount), newTime);
	}
	if(InputHandler::takeKeyClick(GLFW_KEY_F3)){
		double sum = 0;
		for(double d : times) sum += d;
		printf("%f\n", sum/times.size());//0.000582
	}
}

void Map::createMaterialUpdates(coordinate position, bool layer){
	if(setMaterialUpdating(position, !layer))updates[!layer].addUpdate(position);
	if(setMaterialUpdating(position, layer))updates[layer].addUpdate(position);
	if(setMaterialUpdating(position+coordinate(1,0), layer))updates[layer].addUpdate(position+coordinate(1,0));
	if(setMaterialUpdating(position+coordinate(0,1), layer))updates[layer].addUpdate(position+coordinate(0,1));
	if(setMaterialUpdating(position+coordinate(-1,0), layer))updates[layer].addUpdate(position+coordinate(-1,0));
	if(setMaterialUpdating(position+coordinate(0,-1), layer))updates[layer].addUpdate(position+coordinate(0,-1));
}

void Map::addMaterialUpdate(coordinate position, bool layer){
	updates[layer].addUpdate(position);
}

bool Map::setMaterialUpdating(coordinate position, bool layer){
	return planet->setMaterialUpdating(position, layer);
}

void Map::render(){
	Screen::renderPlanet(planet);

	for ( auto &e : entities ) {
		e->render();
	}
}

std::shared_ptr<Region> Map::getRegion(coordinate position){
	return planet->getRegion(position);
}

std::shared_ptr<Chunk> Map::getChunk(coordinate position){
	return planet->getChunk(position);
}

std::shared_ptr<Material> Map::getMaterial(coordinate position, bool layer){
	return Material::get(getMaterialID(position, layer));
}

materialID_t Map::getMaterialID(coordinate position, bool layer){
	return planet->getMaterialID(position, layer);
}

void Map::setMaterialID(coordinate position, bool layer, materialID_t id){
	planet->setMaterialID(position, layer, id);
//	createMaterialUpdates(position, layer);
}

bool Map::placeMaterial(coordinate position, bool layer, materialID_t id, std::shared_ptr<Player> player){
	if(planet->placeMaterial(position, layer, id, player)){
		createMaterialUpdates(position, layer);
		return true;
	}
	return false;
}

bool Map::breakMaterial(coordinate position, bool layer, std::shared_ptr<Player> player, const MiningTool *miningTool){
	if(planet->breakMaterial(position, layer, player, miningTool)){
		createMaterialUpdates(position, layer);
		return true;
	}
	return false;
}

vec2 Map::getGravity(coordinate position){
	return planet->getGravity(position);
}

int2 Map::getMaterialGravity(coordinate position){
	return planet->getMaterialGravity(position);
}


} /* namespace Pixelverse */
