#ifndef MAP_CHUNK_H_
#define MAP_CHUNK_H_

#include "../utilities/glIncludes.h"
#include "../utilities/DataTypes.h"

#include <bitset>
#include <memory>

namespace Pixelverse {

class Player;
class MiningTool;

class Chunk{
public:
	static const int WIDTH = 256;
	static const int SIZE = WIDTH*WIDTH;
	Chunk();
	Chunk(coordinate position);
	virtual ~Chunk();

	void update();//must be called before setUpdating is used as it will reset the bitflags
	bool setMaterialUpdating(coordinate position, bool layer);
	void setLocalChunk(coordinate position, std::shared_ptr<Chunk> chunk);

	virtual materialID_t getMaterialID(coordinate position, bool layer);
	virtual void setMaterialID(coordinate position, bool layer, materialID_t id);

	std::shared_ptr<Chunk> getLocalChunk(coordinate position);

	virtual bool placeMaterial(coordinate position, bool layer, materialID_t id, std::shared_ptr<Player> player);
	virtual bool breakMaterial(coordinate position, bool layer, std::shared_ptr<Player> player, const MiningTool *miningTool);

	void bindTexture(bool layer);
	void bindLightTexture();
	void bindOverlay();//implementation is in ChunkOverlay.cpp
	coordinate getPosition();
protected:
	const coordinate position;
	const int localOffset;
	std::shared_ptr<Chunk> local[9];
	materialID_t data[SIZE*2];
	int light[SIZE];
	float temperature[SIZE], pressure[SIZE];
	std::bitset<SIZE*2> updates;
	GLuint texture[2], textureOverlay;
	GLuint lightValues, lightSources, lightReduction[2];
	bool textureUpdate[2], loading;
	void genTexture(bool layer);
	void updateTexture(bool layer);
	void genLightTextures();
	void load();
};

} /* namespace Pixelverse */

#endif /* MAP_CHUNK_H_ */
