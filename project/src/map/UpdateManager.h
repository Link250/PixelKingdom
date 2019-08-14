#ifndef MAP_UPDATEMANAGER_H_
#define MAP_UPDATEMANAGER_H_

#include "../utilities/DataTypes.h"

#include <memory>
#include <vector>

namespace Pixelverse {

class UpdateManager{
private:
	std::shared_ptr<std::vector<coordinate>> data, nextData;
	size_t size, nextSize;
public:
	UpdateManager();
	virtual ~UpdateManager();

	void addUpdate(coordinate pos);
	size_t getSize();
	std::shared_ptr<std::vector<coordinate>> startUpdate();
};

} /* namespace Pixelverse */

#endif /* MAP_UPDATEMANAGER_H_ */
