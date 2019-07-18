#ifndef GFX_SHADER_H_
#define GFX_SHADER_H_

#include "../utilities/glIncludes.h"
#include <string>
#include <vector>
#include <map>
using namespace std;

namespace Pixelverse {

class Shader{
public:
	string name;
	map<string, GLint> uniforms;
	Shader(string name, vector<string> uniformNames);
	virtual ~Shader();
	void loadUniforms(vector<string> names);
	void bind();
private:
	GLuint vertexShader, fragmentShader, shaderProgram, vertexArray;
	GLint vposLocation, vtexLocation;
	GLuint loadShader(string path, int type);
};

} /* namespace Pixelverse */

#endif /* GFX_SHADER_H_ */
