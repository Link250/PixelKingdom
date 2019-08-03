#ifndef INPUT_INPUTHANDLER_H_
#define INPUT_INPUTHANDLER_H_

#include "../gui/gamefields/GameField.h"
#include "../utilities/glIncludes.h"
#include "../utilities/DataTypes.h"
#include "TextSink.h"
#include "MouseInputUser.h"
#include "PlayerController.h"

#include <map>
#include <memory>
#include <vector>
#include <functional>

namespace Pixelverse {

struct key_input{
	bool pressed = false, click = false;
};

class InputHandler{
private:
	static std::shared_ptr<mouse_input> mouseInput;
	static std::shared_ptr<MouseInputUser> mouseInputUser;
	static bool mouseInputAvailable;
	static int currentKeyMods;
	static std::shared_ptr<Mouse> currentMouse;
	static std::map<int, key_input> keyInputs;
	static vec2 mousePos;
public:
	static std::shared_ptr<TextSink> textSink;
	static std::unique_ptr<PlayerController> playerController;

	InputHandler();
	virtual ~InputHandler();

	static void keyCallback(GLFWwindow* window, int key, int scancode, int action, int mods);
	static void charCallback(GLFWwindow* window, unsigned int codepoint);
	static void mouseButtonCallback(GLFWwindow* window, int button, int action, int mods);
	static void mouseScrollCallback(GLFWwindow* window, double xoffset, double yoffset);
	static void mousePositionCallback(GLFWwindow* window, double xpos, double ypos);

	static bool isMouseInputAvailable();
	static bool setMouseInputUser(std::shared_ptr<MouseInputUser> mouseInputUser);
	static void useMouseInput();
	static void renderMouse();

	static bool takeKeyClick(int key);
	static bool keyPressed(int key);

	static vec2 getMousePos();
	static vec2 getMouseMapPos();
};

} /* namespace Game */

#endif /* INPUT_INPUTHANDLER_H_ */
