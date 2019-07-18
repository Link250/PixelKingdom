#include "Model.h"
#include <iostream>

namespace Pixelverse {

Model::Model(vertex vertices[], int vertexCount, int indices[], int indexCount){
	glGenBuffers(1, &vertexBuffer);
	glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
	glBufferData(GL_ARRAY_BUFFER, sizeof(vertex)*vertexCount, vertices, GL_STATIC_DRAW);

	glGenBuffers(1, &indexBuffer);
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
	glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(int)*indexCount, indices, GL_STATIC_DRAW);
}

Model::~Model(){
	glDeleteBuffers(1, &vertexBuffer);
	glDeleteBuffers(1, &indexBuffer);
}

} /* namespace Pixelverse */
