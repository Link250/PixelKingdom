#include "Region.h"
#include "../main/Game.h"

#include <iostream>

namespace Pixelverse {

Region::Region()
{
	// TODO Auto-generated constructor stub
	
}

Region::~Region()
{
	// TODO Auto-generated destructor stub
}

void Region::update(){
	chunks.forEach([](std::shared_ptr<Chunk> chunk){
		chunk->update();
	});
}

bool Region::setMaterialUpdating(coordinate position, bool layer){
	if(chunks.getValue(position.c_x, position.c_y) != nullptr){
		return chunks.getValue(position.c_x, position.c_y)->setMaterialUpdating(position, layer);
	}return false;
}


std::shared_ptr<Chunk> Region::getChunk(coordinate position){
	return chunks.getValue(position.c_x, position.c_y);
}

void Region::loadChunk(coordinate position){
	if(chunks.getValue(position.c_x, position.c_y) == nullptr){
		std::shared_ptr<Chunk> temp = std::make_shared<Chunk>(position);
		chunks.setValue(position.c_x, position.c_y, temp);
		for(int x = -1; x <= 1; x++)
		for(int y = -1; y <= 1; y++){
			std::shared_ptr<Chunk> neighbour = Game::map->getChunk(position + int2(x*256, y*256));
			if(neighbour != nullptr){
				neighbour->setLocalChunk(position, temp);
				temp->setLocalChunk(position + int2(x*256, y*256), neighbour);
			}
		}
	}
}

materialID_t Region::getMaterialID(coordinate position, bool layer){
	if(chunks.getValue(position.c_x, position.c_y) != nullptr){
		return chunks.getValue(position.c_x, position.c_y)->getMaterialID(position, layer);
	}return 0;
}

void Region::setMaterialID(coordinate position, bool layer, materialID_t id){
	if(chunks.getValue(position.c_x, position.c_y) != nullptr){
		chunks.getValue(position.c_x, position.c_y)->setMaterialID(position, layer, id);
	}
}

bool Region::placeMaterial(coordinate position, bool layer, materialID_t id, std::shared_ptr<Player> player){
	if(chunks.getValue(position.c_x, position.c_y) != nullptr){
		return chunks.getValue(position.c_x, position.c_y)->placeMaterial(position, layer, id, player);
	}return false;
}

bool Region::breakMaterial(coordinate position, bool layer, std::shared_ptr<Player> player, const MiningTool *miningTool){
	if(chunks.getValue(position.c_x, position.c_y) != nullptr){
		return chunks.getValue(position.c_x, position.c_y)->breakMaterial(position, layer, player, miningTool);
	}return false;
}

} /* namespace Pixelverse */
