#include "ChunkManager.h"
#include <thread>

namespace Pixelverse {

ChunkManager::ChunkManager(std::string path): path(path){}

ChunkManager::~ChunkManager(){}

void ChunkManager::loadChunk(coordinate position, std::shared_ptr<Chunk> *chunk){
//	std::thread t1(loaderFunction, position, chunk);
}

void ChunkManager::unloadChunk(coordinate position){
	
}

void ChunkManager::loaderFunction(coordinate position, std::shared_ptr<Chunk> *chunk){
	*chunk = std::make_shared<Chunk>(position);
}

} /* namespace Pixelverse */
