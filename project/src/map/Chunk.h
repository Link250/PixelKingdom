#ifndef MAP_CHUNK_H_
#define MAP_CHUNK_H_

#include "../utilities/glIncludes.h"
#include "../utilities/DataTypes.h"

#include <bitset>

namespace Pixelverse {

class Chunk{
public:
	static const int WIDTH = 256;
	static const int SIZE = WIDTH*WIDTH;
	Chunk(coordinate position);
	virtual ~Chunk();
	materialID_t getMaterialID(coordinate pixelPos, bool layer);
	void setMaterialID(coordinate pixelPos, bool layer, materialID_t id);
	void bindTexture(bool layer);
	void bindOverlay();
	coordinate getPosition();
private:
	const coordinate position;
	materialID_t front[SIZE], back[SIZE];
//	int light[SIZE];
	std::bitset<SIZE*3> updates;
	GLuint textureBack, textureFront, textureOverlay;
	void genTexture(bool layer);
	void load();
};

} /* namespace Pixelverse */

#endif /* MAP_CHUNK_H_ */
