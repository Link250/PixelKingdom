#ifndef MAP_BIOME_H_
#define MAP_BIOME_H_

#include "../utilities/DataTypes.h"
#include "Planet.h"

#include <string>
#include <vector>
#include <map>
#include <memory>

namespace Pixelverse {

class Biome{
public:
	Biome(biomeID_t id,
			std::string name,
			std::string displayName,
			double heightBias = 1.0);
	virtual ~Biome();

	const biomeID_t id;

	const std::string name;
	const std::string displayName;

	const double heightBias;

//	bool update();
	virtual void generate(/*const Planet &planet, */coordinate position,
			materialID_t (&data)[Chunk::SIZE*2]) = 0;

	static std::shared_ptr<Biome> get(std::string name);
	static std::shared_ptr<Biome> get(biomeID_t id);
	static biomeID_t getID(std::string name);
	static const std::map<std::string, std::shared_ptr<Biome>> getList();
	static const std::vector<std::shared_ptr<Biome>> getIDList();
	static void loadList();
private:
	static std::vector<std::shared_ptr<Biome>> idList;
	static std::map<std::string, std::shared_ptr<Biome>> list;
	static void addBiome(std::shared_ptr<Biome> newBiome);
};

class Forest: public Biome{public: Forest(biomeID_t id);
	void generate(/*const Planet &planet, */coordinate position,
			materialID_t (&data)[Chunk::SIZE*2]);
};

} /* namespace Pixelverse */

#endif /* MAP_BIOME_H_ */
