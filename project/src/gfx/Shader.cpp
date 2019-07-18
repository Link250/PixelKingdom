#include "Shader.h"
#include "Model.h"
#include <iostream>
#include <fstream>

namespace Pixelverse {

Shader::Shader(string name, vector<string> uniformNames){
	this->name = name;
	uniforms = std::map<std::string, GLint>();
	vertexShader = loadShader("./res/shader/" + name + ".vert", GL_VERTEX_SHADER);
	fragmentShader = loadShader("./res/shader/" + name + ".frag", GL_FRAGMENT_SHADER);
	shaderProgram = glCreateProgram();
	glAttachShader(shaderProgram, vertexShader);
	glAttachShader(shaderProgram, fragmentShader);
	glLinkProgram(shaderProgram);

    glGenVertexArrays(1, &vertexArray);
    glBindVertexArray(vertexArray);
	vposLocation = glGetAttribLocation(shaderProgram, "vPos");
	vtexLocation = glGetAttribLocation(shaderProgram, "vTex");

	loadUniforms(uniformNames);
}

Shader::~Shader(){
	glDeleteShader(vertexShader);
	glDeleteShader(fragmentShader);
	glDeleteProgram(shaderProgram);
	glDeleteVertexArrays(1, &vertexArray);
}

GLuint Shader::loadShader(string path, int type){
	GLuint shaderID;
	ifstream rawInput(path);
	string contents((istreambuf_iterator<char>(rawInput)), istreambuf_iterator<char>());
	const GLchar *source = contents.c_str();
	rawInput.close();

	shaderID = glCreateShader(type);
	glShaderSource(shaderID, 1, &source, NULL);
	glCompileShader(shaderID);

	GLint isCompiled = 0;
	glGetShaderiv(shaderID, GL_COMPILE_STATUS, &isCompiled);
	if(isCompiled == GL_FALSE){
		GLint maxLength = 0;
		glGetShaderiv(shaderID, GL_INFO_LOG_LENGTH, &maxLength);

		// The maxLength includes the NULL character
		vector<GLchar> errorLog(maxLength);
		glGetShaderInfoLog(shaderID, maxLength, &maxLength, &errorLog[0]);

		glDeleteShader(shaderID); // Don't leak the shader.
		cout << errorLog.data();
	}
	return shaderID;
}

void Shader::loadUniforms(vector<string> names){
	for(string name : names){
		uniforms[name] = glGetUniformLocation(shaderProgram, name.c_str());
	}
}

void Shader::bind(){
	glUseProgram(shaderProgram);
	glEnableVertexAttribArray(vposLocation);
	glEnableVertexAttribArray(vtexLocation);
	glVertexAttribPointer(vposLocation, 2, GL_FLOAT, GL_FALSE, sizeof(Model::vertex), (void*) 0);
	glVertexAttribPointer(vtexLocation, 2, GL_FLOAT, GL_FALSE, sizeof(Model::vertex), (void*) (sizeof(Model::vertex::position)));
}

} /* namespace Pixelverse */
