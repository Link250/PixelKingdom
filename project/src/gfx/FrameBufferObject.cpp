#include "FrameBufferObject.h"

namespace Pixelverse {

FrameBufferObject::FrameBufferObject(int width, int height, bool hasDepth):
		width(width), height(height), hasDepth(hasDepth), framebufferID(0), renderbufferID(0){
	reload(width, height);
}

void FrameBufferObject::reload(int width, int height){
	this->width = width;
	this->height = height;

	if(framebufferID == 0) glGenFramebuffers(1, &framebufferID);
	if(hasDepth && renderbufferID == 0) glGenRenderbuffers(1, &renderbufferID);

	glBindFramebuffer(GL_FRAMEBUFFER, framebufferID);
	glViewport (0, 0, width, height);
	
	if(hasDepth){
		glBindRenderbuffer(GL_RENDERBUFFER, renderbufferID);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT24, width, height);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER,GL_DEPTH_ATTACHMENT,GL_RENDERBUFFER, renderbufferID);
	}

	glBindFramebuffer(GL_FRAMEBUFFER, 0);
}

void FrameBufferObject::setOutputTexture(GLuint texture){
	glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D, texture, 0);
}

FrameBufferObject::~FrameBufferObject(){
	glDeleteFramebuffers(1, &framebufferID);
	if(hasDepth) glDeleteRenderbuffers(1, &renderbufferID);
}

} /* namespace Pixelverse */
