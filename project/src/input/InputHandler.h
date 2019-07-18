#ifndef INPUT_INPUTHANDLER_H_
#define INPUT_INPUTHANDLER_H_

#include "../gui/gamefields/GameField.h"
#include "../utilities/glIncludes.h"
#include "../utilities/DataTypes.h"
#include "TextSink.h"

#include <map>
#include <memory>
#include <vector>

namespace Pixelverse {

class InputHandler{
private:
	static std::map<int, bool> keypresses;
	static std::vector<GameField> gameFields;
public:
	static double scrollY;
	static vec2 mousePos;

	static std::shared_ptr<TextSink> textSink;

	InputHandler();
	virtual ~InputHandler();
	static void keyCallback(GLFWwindow* window, int key, int scancode, int action, int mods);
	static void charCallback(GLFWwindow* window, unsigned int codepoint);
	static void mouseButtonCallback(GLFWwindow* window, int button, int action, int mods);
	static void mouseScrollCallback(GLFWwindow* window, double xoffset, double yoffset);
	static void mousePositionCallback(GLFWwindow* window, double xpos, double ypos);

	static bool keyPressed(int key);
};

} /* namespace Game */

#endif /* INPUT_INPUTHANDLER_H_ */
