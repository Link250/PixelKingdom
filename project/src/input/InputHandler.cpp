#include "InputHandler.h"

#include "../gfx/Screen.h"

#include <iostream>
#include <algorithm>

namespace Pixelverse {

vec2 InputHandler::mousePos;

std::shared_ptr<mouse_input> InputHandler::mouseInput = std::make_shared<mouse_input>();
std::shared_ptr<MouseInputUser> InputHandler::mouseInputUser = nullptr;
bool InputHandler::mouseInputAvailable = true;

int InputHandler::currentKeyMods = 0;
std::shared_ptr<Mouse> InputHandler::currentMouse = Mouse::instance;

std::map<int, key_input> InputHandler::keyInputs;
std::shared_ptr<TextSink> InputHandler::textSink;
std::unique_ptr<PlayerController> InputHandler::playerController;

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
			keyInputs[key] = {action, action};
		}
	}
	currentKeyMods = mods;
}

void InputHandler::charCallback(GLFWwindow* window, unsigned int codepoint){
	if(textSink){
		textSink->addCharacter(char(codepoint));
	}
}

void InputHandler::mouseButtonCallback(GLFWwindow* window, int button, int action, int mods){
	if(mouseInput->button < 0 && action == GLFW_PRESS){
		mouseInput->button = button;
		mouseInput->mods = mods;
		mouseInput->clicking = true;
		mouseInput->changed = true;
		mouseInput->clickPos = mousePos;
	}else if(mouseInput->button == button && action == GLFW_RELEASE){
		mouseInput->clicking = false;
		mouseInput->changed = true;
	}
	//std::cout << "MousePress: " << button << ", " << action << ", " << mods << std::endl;
}

void InputHandler::mouseScrollCallback(GLFWwindow* window, double xoffset, double yoffset){
	//std::cout << "MousePress: " << xoffset << ", " << yoffset << std::endl;
	mouseInput->scroll += {xoffset, yoffset};
	mouseInput->scrollmods = currentKeyMods;
}

void InputHandler::mousePositionCallback(GLFWwindow* window, double xpos, double ypos){
	mousePos = {xpos, ypos};
	mouseInput->currentPos = mousePos;
}

bool InputHandler::isMouseInputAvailable(){
	return mouseInputAvailable;
}

bool InputHandler::setMouseInputUser(std::shared_ptr<MouseInputUser> newUser){
	if(mouseInputAvailable){
		mouseInputUser = newUser;
		mouseInputAvailable = false;
		return true;
	}
	return false;
}

void InputHandler::useMouseInput(){
	if(mouseInputUser != nullptr){
		currentMouse = mouseInputUser->useMouseInput(mouseInput);
	}else{
		currentMouse = playerController->useMouseInput(mouseInput);
	}
	if(currentMouse == nullptr)currentMouse = Mouse::instance;
	if(!mouseInput->clicking){
		mouseInput->button = -1;
		mouseInput->mods = -1;
		mouseInput->scrollmods = -1;
		mouseInput->clickPos = {-1, -1};
		mouseInputUser = nullptr;
		mouseInputAvailable = true;
		//std::cout << "reset mouseInputUser" << std::endl;
	}else{
		mouseInputAvailable = false;
	}
	mouseInput->scroll = {0, 0};
	mouseInput->changed = false;
}

void InputHandler::renderMouse(){
	if(playerController->mouseItem == nullptr){
		currentMouse->render(mousePos);
	}else{
		currentMouse->render(mousePos, playerController->mouseItem);
	}
}

bool InputHandler::takeKeyClick(int key){
	if(keyInputs[key].click){
		keyInputs[key].click = false;
		return true;
	}return false;
}

bool InputHandler::keyPressed(int key){
	return keyInputs[key].pressed;
}

vec2 InputHandler::getMousePos(){
	return mousePos;
}

vec2 InputHandler::getMouseMapPos(){
	return (mousePos - vec2{double(Screen::width), double(Screen::height)}/2).rotate(-Screen::rotation)/Screen::zoom + Screen::center;
}

} /* namespace Game */
