#ifndef CONFIG_KEYCONFIG_H_
#define CONFIG_KEYCONFIG_H_

#include "../utilities/glIncludes.h"

namespace Pixelverse {

class KeyConfig{
public:
	static const int CLOSE = GLFW_KEY_ESCAPE;
	static int UP, LEFT, DOWN, RIGHT, JUMP;
	static int INFO;
};

} /* namespace Pixelverse */

#endif /* CONFIG_KEYCONFIG_H_ */
