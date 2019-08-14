#include "UpdateManager.h"

namespace Pixelverse {

UpdateManager::UpdateManager(): size(0), nextSize(0){
	data = std::make_shared<std::vector<coordinate>>();
	nextData = std::make_shared<std::vector<coordinate>>();
}

UpdateManager::~UpdateManager(){}

void UpdateManager::addUpdate(coordinate pos){
	if(nextData->size() <= nextSize)
		nextData->push_back(pos);
	else
		(*nextData)[nextSize] = pos;
	nextSize++;
}

size_t UpdateManager::getSize(){
	return size;
}

std::shared_ptr<std::vector<coordinate>> UpdateManager::startUpdate(){
	//printf("1 %i %i %p %p\n", int(size), int(nextSize), data.get(), nextData.get());
	size = nextSize;
	nextSize = 0;
	std::swap(data, nextData);
	//printf("2 %i %i %p %p\n", int(size), int(nextSize), data.get(), nextData.get());
	return data;
}

} /* namespace Pixelverse */
