#include "Region.h"

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

std::shared_ptr<Chunk> Region::getChunk(coordinate position){
	return chunks[position.c_x + position.c_y*WIDTH];
}

void Region::loadChunk(coordinate position){
	if(chunks[position.c_x + position.c_y*WIDTH] == nullptr){
		chunks[position.c_x + position.c_y*WIDTH] = std::make_shared<Chunk>(position);
	}
}

materialID_t Region::getMaterialID(coordinate pixelPos, bool layer){
	if(chunks[pixelPos.c_x + pixelPos.c_y*WIDTH] != nullptr){
		return chunks[pixelPos.c_x + pixelPos.c_y*WIDTH]->getMaterialID(pixelPos, layer);
	}return 0;
}

void Region::setMaterialID(coordinate pixelPos, bool layer, materialID_t id){
	if(chunks[pixelPos.c_x + pixelPos.c_y*WIDTH] != nullptr){
		chunks[pixelPos.c_x + pixelPos.c_y*WIDTH]->setMaterialID(pixelPos, layer, id);
	}
}

} /* namespace Pixelverse */
