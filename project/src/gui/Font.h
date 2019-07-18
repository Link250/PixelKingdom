#ifndef GUI_FONT_H_
#define GUI_FONT_H_

#include <memory>
#include <map>
#include "../gfx/SpriteSheet.h"
#include "../utilities/DataTypes.h"

namespace Pixelverse {

class Font{
public:
	Font(std::string name, std::string chars, int width, int height);
	virtual ~Font();
	void render(std::string message, vec2 position, int color = 0xff000000, int limit = 0, bool centeredX = true, bool centeredY = true);
private:
	std::map<char, int> charlist;
	std::shared_ptr<SpriteSheet> sprites;
};

} /* namespace Pixelverse */

#endif /* GUI_FONT_H_ */
