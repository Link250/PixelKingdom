#ifndef GFX_MODEL_H_
#define GFX_MODEL_H_

#include "../utilities/glIncludes.h"

namespace Pixelverse {

class Model{
public:
	struct vertex{
		struct {float x, y;}position;
		struct {float u, v;}texture;
	};
	Model(vertex vertices[], int vertexCount, int indices[], int indexCount);
	virtual ~Model();
	GLuint vertexBuffer, indexBuffer;
};

} /* namespace Pixelverse */

#endif /* GFX_MODEL_H_ */
