#include "Material.h"
#include "../main/Game.h"

namespace Pixelverse {

bool Material::fall(Chunk *chunk, coordinate position, bool layer, bool fallToSide){
	int2 grav = Game::map->getMaterialGravity(position);
	int2 side = grav.rotatedClockwise();
	if(chunk->getLocalChunk(position + grav)->placeMaterial(position + grav, layer, id, nullptr) ||
			(chunk->getMaterialID(position, !layer) == 0 &&
					chunk->getLocalChunk(position + grav)->placeMaterial(position + grav, !layer, id, nullptr))){
		chunk->setMaterialID(position, layer, 0);
		return true;
	}
	if(fallToSide){
		int dir = (Game::randTickNumber % 2)*2-1;
		if(chunk->getLocalChunk(position + side*dir)->getMaterialID(position + side*dir, layer) == 0){
			if(chunk->getLocalChunk(position + side*dir + grav)->placeMaterial(position + side*dir + grav, layer, id, nullptr)){
				chunk->setMaterialID(position, layer, 0);
				return true;
			}
		}
	}
	return false;
}

bool Material::flow(Chunk *chunk, coordinate position, bool layer, int viscocity){
	int2 grav = Game::map->getMaterialGravity(position);
	int2 side = grav.rotatedClockwise();
	if(chunk->getLocalChunk(position + grav)->placeMaterial(position + grav, layer, id, nullptr) ||
			(chunk->getMaterialID(position, !layer) == 0 &&
					chunk->getLocalChunk(position + grav)->placeMaterial(position + grav, !layer, id, nullptr))){
		chunk->setMaterialID(position, layer, 0);
		return true;
	}
	bool hasPath = true;
	for(int i = 1; i <= viscocity && hasPath; i++){
		hasPath = false;
		if(chunk->getLocalChunk(position + side*i)->getMaterialID(position + side*i, layer) == 0){
			if(chunk->getLocalChunk(position + side*i + grav)->placeMaterial(position + side*i + grav, layer, id, nullptr)){
				chunk->setMaterialID(position, layer, 0);
				return true;
			}else if(chunk->getLocalChunk(position + side*i + grav)->getMaterialID(position + side*i + grav, layer) == id)hasPath = true;
		}
		if(chunk->getLocalChunk(position + side*i)->getMaterialID(position + side*i, !layer) == 0){
			if(chunk->getLocalChunk(position + side*i + grav)->placeMaterial(position + side*i + grav, !layer, id, nullptr)){
				chunk->setMaterialID(position, layer, 0);
				return true;
			}else if(chunk->getLocalChunk(position + side*i + grav)->getMaterialID(position + side*i + grav, !layer) == id)hasPath = true;
		}
	}
	hasPath = true;
	for(int i = -1; i >= -viscocity && hasPath; i--){
		hasPath = false;
		if(chunk->getLocalChunk(position + side*i)->getMaterialID(position + side*i, layer) == 0){
			if(chunk->getLocalChunk(position + side*i + grav)->placeMaterial(position + side*i + grav, layer, id, nullptr)){
				chunk->setMaterialID(position, layer, 0);
				return true;
			}else if(chunk->getLocalChunk(position + side*i + grav)->getMaterialID(position + side*i + grav, layer) == id)hasPath = true;
		}
		if(chunk->getLocalChunk(position + side*i)->getMaterialID(position + side*i, !layer) == 0){
			if(chunk->getLocalChunk(position + side*i + grav)->placeMaterial(position + side*i + grav, !layer, id, nullptr)){
				chunk->setMaterialID(position, layer, 0);
				return true;
			}else if(chunk->getLocalChunk(position + side*i + grav)->getMaterialID(position + side*i + grav, !layer) == id)hasPath = true;
		}
	}
	return false;
}

bool Material::spread(Chunk *chunk, coordinate position, bool layer){
	//int2 grav = Game::map->getMaterialGravity(position);
	//int2 side = grav.rotatedClockwise();
	for(int i = 0; i < 5; i++){
		int2 dir = directions[(Game::randTickNumber+i)%5];
		if(chunk->getLocalChunk(position + dir)->placeMaterial(position + dir, layer^(((Game::randTickNumber+i)%5)==4), id, nullptr)){
			chunk->setMaterialID(position, layer, 0);
			return true;
		}
	}
	return false;
}

} /* namespace Pixelverse */
