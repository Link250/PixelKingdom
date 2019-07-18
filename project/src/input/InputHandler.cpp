#include "InputHandler.h"

#include <iostream>
#include <algorithm>

namespace Pixelverse {

double InputHandler::scrollY;
vec2 InputHandler::mousePos;
std::map<int, bool> InputHandler::keypresses;
std::shared_ptr<TextSink> InputHandler::textSink;
std::vector<GameField> InputHandler::gameFields;

InputHandler::InputHandler(){}

InputHandler::~InputHandler(){}

void InputHandler::keyCallback(GLFWwindow* window, int key, int scancode, int action, int mods){
	//printf("%p", glfwGetKeyName(key, scancode));
	//std::cout << " " << key << ", " << scancode << ", " << action << ", " << mods << std::endl;

	if(textSink != nullptr){
		if(action == GLFW_PRESS){
			if(key == GLFW_KEY_ESCAPE){
				textSink->detach();
				textSink = nullptr;
			}
		}
	}else{
		if(key == GLFW_KEY_ESCAPE && action == GLFW_PRESS){
			glfwSetWindowShouldClose(window, GLFW_TRUE);
		}else{
			keypresses[key] = action;
		}
	}
}

void InputHandler::charCallback(GLFWwindow* window, unsigned int codepoint){
	if(textSink){
		textSink->addCharacter(char(codepoint));
	}
}

void InputHandler::mouseButtonCallback(GLFWwindow* window, int button, int action, int mods){
	for(auto itt = gameFields.begin(); itt != gameFields.end(); itt++){
		if(itt->active){
			if(itt->getArea().contains((int)mousePos.x, (int)mousePos.y)){
				//use mouseclick
				return;
			}
		}else{
			gameFields.erase(itt--);
		}
	}
}

void InputHandler::mouseScrollCallback(GLFWwindow* window, double xoffset, double yoffset){
	scrollY = yoffset;
}

void InputHandler::mousePositionCallback(GLFWwindow* window, double xpos, double ypos){
	mousePos = {xpos, ypos};
}

bool InputHandler::keyPressed(int key){
	return keypresses[key];
}

} /* namespace Game */
