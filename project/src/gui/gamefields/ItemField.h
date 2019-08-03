#ifndef GUI_GAMEFIELDS_ITEMFIELD_H_
#define GUI_GAMEFIELDS_ITEMFIELD_H_

#include "../../gfx/Texture.h"
#include "../../utilities/DataTypes.h"
#include "../../item/Item.h"

#include <memory>

namespace Pixelverse {

class ItemField{
public:
	ItemField(const std::shared_ptr<Item> *itemPointer, int2 position = {0, 0});
	virtual ~ItemField();
	void render(int2 offset = {0, 0});

	static constexpr int2 size = {36, 36};
	int2 position;
private:
	const std::shared_ptr<Item> *itemPointer;
	static std::shared_ptr<Texture> background;
};

} /* namespace Pixelverse */

#endif /* GUI_GAMEFIELDS_ITEMFIELD_H_ */
