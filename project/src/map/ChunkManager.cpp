#include "ChunkManager.h"

namespace Pixelverse {

ChunkManager::ChunkManager(std::string path): path(path){}

ChunkManager::~ChunkManager(){}

void loadChunk(coordinate position, std::shared_ptr<std::shared_ptr<Chunk>> chunk){

}

void unloadChunk(coordinate position){
	
}

} /* namespace Pixelverse */
