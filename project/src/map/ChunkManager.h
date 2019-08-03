#ifndef MAP_CHUNKMANAGER_H_
#define MAP_CHUNKMANAGER_H_

#include <memory>
#include <vector>
#include <thread>
#include "Chunk.h"

namespace Pixelverse {

struct ChunkLoader{
	std::shared_ptr<std::shared_ptr<Chunk>> chunk;
	coordinate position;
	std::thread t;
};

class ChunkManager{
public:
	ChunkManager(std::string path);
	virtual ~ChunkManager();
	void loadChunk(coordinate position, std::shared_ptr<Chunk> *chunk);
	void unloadChunk(coordinate position);

	void loaderFunction(coordinate position, std::shared_ptr<Chunk> *chunk);
private:
	std::string path;
	std::vector<ChunkLoader> chunks;
};

} /* namespace Pixelverse */

#endif /* MAP_CHUNKMANAGER_H_ */
