#ifndef GFX_FRAMEBUFFEROBJECT_H_
#define GFX_FRAMEBUFFEROBJECT_H_

#include "../utilities/glIncludes.h"

namespace Pixelverse {

class FrameBufferObject{
	int width, height;
	bool hasDepth;
public:
	GLuint framebufferID, renderbufferID;
	FrameBufferObject(int width, int height, bool hasDepth);
	void reload(int width, int height);
	void setOutputTexture(GLuint texture);
	virtual ~FrameBufferObject();
};

} /* namespace Pixelverse */

#endif /* GFX_FRAMEBUFFEROBJECT_H_ */
